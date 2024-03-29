package com.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.analytics.model.GaData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.service.AnalyticsMailer;
import com.service.Authenticate;
import com.service.GADataService;
import com.service.GaDatastoreService;
import com.service.GaReportProcessor;
import com.service.MyGaDatastoreService;
import com.service.MyV2CorrectionService;
import com.util.CsvUtil;
import com.util.DataStoreManager;
import com.util.EmailAttachmentView;
import com.util.EmailUtil;
import com.util.GAUtil;
import com.util.StringUtil;
import com.util.ZipData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.Vector;
import java.util.zip.DataFormatException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

// TODO: Auto-generated Javadoc
/**
 * The Class GAScheduler.
 * Schedules Data Fetch operation from Google Analytics API and also batch processing of Agent Data. 
 */
@Controller
public class GAScheduler {
	

	/** The Constant mapper. */
	static final ObjectMapper mapper = new ObjectMapper();
	ResourceBundle resourceBundle= ResourceBundle.getBundle("GaReportConstant");
	
	/** The Constant mLogger. */
	private static final Logger mLogger = Logger.getLogger(GAScheduler.class.getPackage().getName());
	static {
		mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, true);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		
	}
	
	/** The date. */
	static String date="";
	HashMap outerhminmain = new HashMap();
	HashMap ohmforremovefault = new HashMap();
	ArrayList finalArraylist =  new ArrayList();
	
	
	
	/**
	 * Fetch batch data.
	 *
	 * @param req the req
	 * @param res the res
	 * FetchBatchData works on a backend with daily cron to store Analytics data in a batch of 9999 rows in one DataStore entry.
	 *   
	 */
	@RequestMapping("/fetchBatchData.do")	
	public void fetchBatchData(HttpServletRequest req,HttpServletResponse res)
	{	
		try {
			String dateFrom=req.getParameter("dateFrom");
			String date=null;
			if(dateFrom==null)
			{
				Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//cal.roll(Calendar.DATE, false);
			cal.add(Calendar.DATE, -1);
			System.out.println(sdf.format(cal.getTime()) + "::timeZone::"
					+ sdf.getTimeZone());
			date = (sdf.format(cal.getTime())).toString();
			}
			else
			{
				date =dateFrom;
			}
			
			String dimensions="ga:eventCategory,ga:eventAction,ga:eventLabel,ga:customVarValue1,ga:customVarValue2,ga:customVarValue3,ga:customVarValue4";
			String accessToken="";
			GaData gaData=null;
			ArrayList<ArrayList<?>> rowData=null;
			ArrayList<String> gaJson=null;
			int z=0;
			//GaDatastoreService datastoreService= new GaDatastoreService();
			//MyChange
			MyGaDatastoreService mydatastoreService = new MyGaDatastoreService();
			Authenticate  authenticate =new Authenticate();
			System.out.println("access GA API using::"+resourceBundle.getString("email").toString());
//			String jsonData=new GaDatastoreService().getTempData(resourceBundle.getString("email").toString(),"refresh_token");
			//Mychange
			String jsonData1=new MyGaDatastoreService().getTempData(resourceBundle.getString("email").toString(),"refresh_token");

			System.out.println("jsonData ::: "+jsonData1);
    		HashMap hm=mydatastoreService.convertJsonToMap(jsonData1);
    		System.out.println("hm ::: "+hm);
    		if(hm.get("refresh_token")!=null)
    		{System.out.println("refresh Toekn found");
    			 accessToken = authenticate.updateAccessTokenWithResfreshToken(hm.get("refresh_token").toString());
    			System.out.println("accessToken");
    			//temp=(GoogleTokenResponse)authenticate.getNewToken(hm.get("refresh_token").toString());
    		}else
    		{
    			System.out.println("no Refresh token");
    		}
					//System.out.println("Filter seelcted for Ga Query"+filter+"for table Id"+tableId);
					ArrayList<GaData> list=new ArrayList<GaData>();
						 
						authenticate.gaQurey(null,accessToken, date, date,true,list,dimensions, resourceBundle.getString("SBLive"),"", null );
						System.out.println(list.size());
						System.out.println("Ga Data --ArrayList fetched and stored");
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AnalyticsMailer am= new AnalyticsMailer();
			 try {
				am.initMail("",e.toString(),date,resourceBundle.getString("errorMail").toString(),"GA Exception-fetchBatchData","","", null);
			} catch (UnsupportedEncodingException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		
		}
	
		
	}
	



/**
 * Process agent report.
 *
 * @param req the req
 * @param res the res
 * @throws Exception the exception
 * @throws IOException Signals that an I/O exception has occurred.
 * 
 * processAgentReport works on a backend with daily cron to process agent action report for Performance Management Team.
 * It stores a json and CSV for the same.
 * 
 */
@RequestMapping("/processAgentReport.do")	
public void processAgentReport(HttpServletRequest req,HttpServletResponse res) throws Exception, IOException
{
	System.out.println("Cron JOb Triggered @"+System.currentTimeMillis());
	System.out.println("target->> /processAgentReport.do ");
	String dateFrom=req.getParameter("dateFrom");
	
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	sdf.setTimeZone( TimeZone.getTimeZone( "PST8PDT" ) );
	
	if (req.getParameter("dateFrom") != null) {
		date = req.getParameter("dateFrom");
		System.out.println("Custom Date:" + date);
	} else {
		//cal.roll(Calendar.DATE, false);
		cal.add(Calendar.DATE, -1);
		System.out.println(sdf.format(cal.getTime()) + "::timeZone::"
				+ sdf.getTimeZone());
		date = (sdf.format(cal.getTime())).toString();
		dateFrom=date;
	}
	
	System.out.println("dataFrom:::;   "+dateFrom);
	String dimensions="ga:eventCategory,ga:eventAction,ga:eventLabel,ga:customVarValue1,ga:customVarValue2,ga:customVarValue3,ga:customVarValue4";
	ArrayList<ArrayList<?>> rows=null;
	String processedData=null;
	try {
		rows=GaDatastoreService.fetchGADataBatch(date, null, dimensions, GAUtil.getkeyElementFromDimension(dimensions), "SBLIVE", null);
		if(rows!=null)
		{
			String jsonData;
			System.out.println("batch Size::::"+rows.size());
			/*
			Processing  rawData For Agent Details report
			*/
			ArrayList agentActionData=new GaReportProcessor().AgentActionReport(rows);
			
			ArrayList<ArrayList<?>> arr=new ArrayList<ArrayList<?>>(agentActionData);
			System.out.println("########Storing Processed Array#####KEy::"+"AgentActionCount_"+dateFrom.replaceAll("-", ""));
			DataStoreManager.set("AgentActionCount_"+dateFrom.replaceAll("-", ""),dateFrom.replaceAll("-", ""),ZipData.compressBytes(GaDatastoreService.convertObjectToJson(arr).toString()));
			//Reordering AgentData
			arr=new GaReportProcessor().reorderAgentReport(agentActionData);
			processedData=CsvUtil.formatCsvfromArray(arr,',',Charset.defaultCharset()).toString();
			DataStoreManager.set("AgentActionCount_CSV"+dateFrom.replaceAll("-", ""),dateFrom.replaceAll("-", ""),ZipData.compressBytes(processedData.toString()));
			System.out.println("########Storing CSV Data#####KEy::"+"AgentActionCount_CSV_"+dateFrom.replaceAll("-", ""));
			 
		}
		
	} catch (ClassNotFoundException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		AnalyticsMailer am= new AnalyticsMailer();
		 try {
			am.initMail("",e.toString(),date,resourceBundle.getString("errorMail").toString(),"GA Exception-processAgentReport","","", null);
			
		} catch (UnsupportedEncodingException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
	}
	
	
}

/**
 * Agent action email service.
 *
 * @param req the req
 * @param res the res
 * @throws Exception the exception
 * @throws IOException Signals that an I/O exception has occurred.
 *  sends email for agent action report.
 */
@RequestMapping("/agentActionEmailService.do")
public void agentActionEmailService(HttpServletRequest req,HttpServletResponse res) throws Exception, IOException
		{
	
		System.out.println("Cron JOb Triggered @" + System.currentTimeMillis());
		System.out.println("target->> /email tester.do ");

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("PST8PDT"));
		String testEmail=req.getParameter("email");
		//testEmail="test";

		if (req.getParameter("dateFrom") != null) {
			date = req.getParameter("dateFrom");
			System.out.println("Custom Date:" + date);
		} else {
			//cal.roll(Calendar.DATE, false);
			cal.add(Calendar.DATE, -1);
			System.out.println(sdf.format(cal.getTime()) + "::timeZone::"
					+ sdf.getTimeZone());
			date = (sdf.format(cal.getTime())).toString();
		}

		
		String csvData = null;
		try {
		
		byte[] b=DataStoreManager.get("AgentActionCount_CSV"+date.replaceAll("-", ""),date.replaceAll("-", ""));
    	if(b.length>1)
    	{	
				csvData=ZipData.extractBytes(b);
    	}else
    	{
    		System.out.println("CSV Data Not present in Cache");
    	}
		
		String msgText = "[TEST] Please Find Attached Agent Action Report for "+date;  
		
		System.out.println(msgText);
		AnalyticsMailer am= new AnalyticsMailer();
			 
				 if(csvData!=null && !csvData.equals(""))
					 {
					String cc="slmnmark1@gmail.com";
					String bcc="androidcodefreak@gmail.com";
					
					
					if(testEmail!=null && testEmail.equals("test"))
						am.initMail(csvData,msgText,date,resourceBundle.getString("errorMail").toString(),"Agent Action Report","","","AgentActionReport");
					else
						am.initMail(csvData,msgText,date,"slmnmark@gmail.com","Agent Action Report",cc,bcc, "AgentActionReport");
					//
					 }
				 else
			    		System.out.println("CSV	 Data Not present");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				AnalyticsMailer am= new AnalyticsMailer();
				 try {
					am.initMail("",e.toString(),date,resourceBundle.getString("errorMail").toString(),"GA Exception-agentActionEmailService","","", null);
					
				} catch (UnsupportedEncodingException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				
			}
			 System.out.println("Service complete");
			 
		}

/**
 * Fetch v2 outbound.
 *
 * @param req the req
 * @param res the res
 * 
 * fetch v2 outbound data based on a dateFrom,dateTo  from Google Analytics , stores as an entry in DataStore and send email to marked addresses. 
 * Note: It works on a backend 'gabackend';
 */
@RequestMapping("/fetchV2Outbound.do")	
public void fetchV2Outbound(HttpServletRequest req,HttpServletResponse res)
{System.out.println("fetchV2Outbound");
	
	AnalyticsMailer am= new AnalyticsMailer();

	try {
		String dateFrom=req.getParameter("dateFrom");
		String dateTo=req.getParameter("dateTo");
		String dateRange=req.getParameter("range");
		String email=req.getParameter("email");
		String date=null;
		if(dateFrom==null && dateTo==null)
		{
			Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//cal.roll(Calendar.DATE, false);
		cal.add(Calendar.DATE, -1);
		System.out.println(sdf.format(cal.getTime()) + "::timeZone::"
				+ sdf.getTimeZone());
		date = (sdf.format(cal.getTime())).toString();
		dateFrom=date;
		dateTo=date;
		}
		else
		{System.out.println("Setting date Range");
			date =dateFrom;
			
		}
		if(dateRange==null)
		{
			dateRange="today";
		}
		
		String dimensions="ga:eventCategory,ga:eventAction,ga:eventLabel,ga:customVarValue1,ga:customVarValue2,ga:customVarValue3,ga:customVarValue4";
		
				ArrayList<GaData> list=new ArrayList<GaData>();
				Authenticate  authenticate =new Authenticate();
				ArrayList<ArrayList<?>> arrayList=null;
				String accessToken="";
		    	String processedData=null;
		    	System.out.println("access GA API using::"+resourceBundle.getString("email").toString());
		    	//String jsonData=new GaDatastoreService().getTempData(resourceBundle.getString("email").toString(),"access_token");
		    	//My change
		    	String jsonData1=new MyGaDatastoreService().getTempData(resourceBundle.getString("email").toString(),"refresh_token");
	    		//GaDatastoreService datastoreService=null;
		    	//My change
	    		MyGaDatastoreService mydatastoreService = new MyGaDatastoreService();
                System.out.println("Checking jsonData1 in V2Outbound::"+jsonData1);
				HashMap hm=mydatastoreService.convertJsonToMap(jsonData1);
	    		if(hm.get("refresh_token")!=null)
	    		{System.out.println("refresh Toekn found");
	    			 accessToken = authenticate.updateAccessTokenWithResfreshToken(hm.get("refresh_token").toString());
	    			System.out.println("accessToken");
	    			System.out.println("Checking what is access Token::"+accessToken);
	    			//temp=(GoogleTokenResponse)authenticate.getNewToken(hm.get("refresh_token").toString());
	    		
					authenticate.gaQurey(null,accessToken, dateFrom, dateTo,true,list,dimensions, resourceBundle.getString("V2App"),"ga:eventAction==Call Transfer,ga:eventAction==Dialing", "monthly" );
					System.out.println(list.size());
					System.out.println("Ga Data --ArrayList fetched and stored");
					arrayList=(new GaDatastoreService()).fetchGADataBatch(date,dateTo,dimensions,GAUtil.getkeyElementFromDimension(dimensions), "V2Outbound", dateRange);
					//Gson gjsonfortest = new Gson();
				//	String mytempstring = gjson.toJson(myhmap);
					//ArrayList myarraytemp = new ArrayList(arrayList);
//					Gson gjsonfortest = new Gson();
//					String myarraylist = gjsonfortest.toJson(arrayList);
					//System.out.println("Checking arraylist in V2::"+myarraylist);
					System.out.println("********Change starts here*************");
					 HashMap innerhminmain = new HashMap();
					 for(int i=0; i<arrayList.size(); i++){
			        	  HashMap innertemp = new HashMap();
			        	  
			        	  innertemp = MyV2CorrectionService.convertArraytoMap((ArrayList) arrayList.get(i),i);
			        	  HashMap temp2 = new HashMap(innertemp);
			        	 // innerhminmain.putAll(innertemp);
			        	  innerhminmain.put(i,innertemp);			        	      
			             
			          }
					  outerhminmain.putAll(innerhminmain);
					  Gson gjson = new Gson();
				       // JsonObject myjson = new JsonObject(outerhminmain);
				       // System.out.println(myjson);
					  HashMap ihmforremovefault = new HashMap();
				        for(int j=0; j<outerhminmain.size(); j++){
				        	//System.out.println("Checking ::**::"+outerhminmain.get(j));
				        	HashMap changedmap = MyV2CorrectionService.removefaultfordata((HashMap) outerhminmain.get(j),j);
				        	ihmforremovefault.put(j, changedmap);
				        }
				        ohmforremovefault.putAll(ihmforremovefault);
				      //  System.out.println("After Change::"+ohmforremovefault);
				        JSONObject myjson1 = new JSONObject(ohmforremovefault);
				        //System.out.println("After Change::Json::"+myjson1);
				        
				         //System.out.println("Convert hashmap to Array of ArrayList:::");
				         ArrayList finalinArlist =  new ArrayList();
				         for(int k=0;k<ohmforremovefault.size(); k++){
				        	
				        	 ArrayList chagedlist = convertHmaptoArrayList((HashMap) ohmforremovefault.get(k));
				        	 //finalinArlist.add(chagedlist);
				        	 finalArraylist.add(chagedlist);
				         }
				        //finalArraylist.add(finalinArlist);
				      //  System.out.println("Thank Jesus..! got it::"+finalArraylist);
				        
					
					System.out.println("********Change ends here***************");
					
					   
					
					
					arrayList=(new GaDatastoreService()).addReportHeaders(arrayList);
		    		System.out.println("processed Data"+arrayList.size());
		    		processedData=CsvUtil.formatCsvfromArray(arrayList,',',Charset.defaultCharset()).toString();
		    		
		    		String msgText = "Please Find Attached V2 Outbound Report for "+date;
		    		if(dateRange!=null && dateRange.equals("monthly")){
		    					msgText = " Please Find Attached V2 Outbound Report for date  "+dateFrom+"->"+dateTo;
		    			}
		    		if(email!=null && !email.equals("") && StringUtil.isEmail(email))
		    		{  System.out.println("sending email to ::"+email);
		    			am.initMail(processedData,msgText,date,email,"V2 Outbound Report",resourceBundle.getString("errorMail").toString(),"", "V2Outbound_");
		    			res.getWriter().println("V2 Outbound Report Generated. Please check your inbox :-"+email);
		    			//Response.AddHeader("Access-Control-Allow-Origin", "*");
		    			res.addHeader("Access-Control-Allow-Origin", "*");
		    		}else
		    		{System.out.println("Sending email..");
		    			am.initMail(processedData,msgText,date,resourceBundle.getString("errorMail").toString(),"V2 Outbound Report","slmnmark@gmail.com,slmnmark1@gmail.com","", "V2Outbound_");
		    		}
	    		}else
	    		{
	    			System.out.println("no Refresh token");
	    			
	    		}	
					
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
		 try {
			am.initMail("",e.toString(),date,resourceBundle.getString("errorMail").toString(),"GA Exception","","", null);
			
		} catch (UnsupportedEncodingException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally
	{
		am=null;
		
	}
				

	
}

/**
 *   Helper method for converting HashMap to ArrayList. This method is created by solomon. V2Outboundreport Url is using this method
 * @param hashMap
 * @return
 */

private ArrayList convertHmaptoArrayList(HashMap ohmforremovefault) {
	ArrayList list = new ArrayList();
	list.add(ohmforremovefault.get("Account"));
	list.add(ohmforremovefault.get("Action"));
	list.add(ohmforremovefault.get("Agent"));
	list.add(ohmforremovefault.get("OBNumber"));
	list.add(ohmforremovefault.get("InboundConnId"));
	list.add(ohmforremovefault.get("OutboundConnId"));
	list.add(ohmforremovefault.get("TimeStamp"));
	list.add(ohmforremovefault.get("Total"));
	return list;
}




/**
 * Parses the batched data into one String and stores as jsonFormat for the reporting tool gadataservice and further processing.
 * 
 *
 * @param req the req
 * @param res the res
 */
@RequestMapping("/parseGAJSON.do")	
public void parseGAJSON(HttpServletRequest req,HttpServletResponse res)
{	
	String dateFrom=req.getParameter("dateFrom");
	
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	sdf.setTimeZone(TimeZone.getTimeZone("PST8PDT"));

	if (req.getParameter("dateFrom") != null) {
		date = req.getParameter("dateFrom");
		System.out.println("Custom Date:" + date);
	} else {
		//cal.roll(Calendar.DATE, false);
		cal.add(Calendar.DATE, -1);
		System.out.println(sdf.format(cal.getTime()) + "::timeZone::"
				+ sdf.getTimeZone());
		date = (sdf.format(cal.getTime())).toString();
	}
	
	dateFrom=date;
	String dimensions="ga:eventCategory,ga:eventAction,ga:eventLabel,ga:customVarValue1,ga:customVarValue2,ga:customVarValue3,ga:customVarValue4";
	ArrayList<ArrayList<?>> rows=null;
	try {
		String key="GaDataObject_"+"SBLIVE"+"_"+dateFrom.replaceAll("-", "")+"_"+GAUtil.getkeyElementFromDimension(dimensions);
		byte [] jsonData=DataStoreManager.get(key+"_json", dateFrom.replaceAll("-", ""));
		//System.out.println("JsonData in parseJSON::"+jsonData);
		String processedData=null;
		
				rows=GaDatastoreService.fetchGADataBatch(dateFrom, null, dimensions, GAUtil.getkeyElementFromDimension(dimensions), "SBLIVE", null);
				
				if(rows!=null)
	    		{
					System.out.println("batch Size::::"+rows.size());
					//res.getWriter().println(GaDatastoreService.convertObjectToJson(rows).toString());
					processedData=GaDatastoreService.convertObjectToJson(rows).toString();
					byte [] compressData=ZipData.compressBytes(processedData);
					System.out.println(" JSON DataCompressed");
					DataStoreManager.set(key+"_json", dateFrom.replaceAll("-", ""), compressData);
					System.out.println("JSON Data Stored in DataStore  :  "+key+"_json");
				
	    		}
					
	} 
	catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
		 
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}




	
/**
 * Gets the stack trace.
 *
 * @param t the t
 * @return the stack trace
 */
public static String getStackTrace(Throwable t)
{
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    t.printStackTrace(pw);
    pw.flush();
    sw.flush();
    return sw.toString();
}


/**
 * Prints the stack trace.
 *
 * @param t the t
 */
public static void printStackTrace(Throwable t) {
mLogger.error(getStackTrace(t));
}
	
	
	

}
