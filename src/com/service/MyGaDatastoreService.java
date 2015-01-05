package com.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.zip.DataFormatException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import com.util.DataStoreManager;
import com.util.MyDataStoreManager;
import com.util.ZipData;

public class MyGaDatastoreService {
	private static final Logger mLogger = Logger.getLogger(MyGaDatastoreService.class.getPackage().getName());
	static final ObjectMapper mapper = new ObjectMapper();
	 //static AppCacheManager appcachemanager= new AppCacheManager();
	static {
		mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, true);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		mapper.getJsonFactory().setCharacterEscapes(new JSONCharacterEscapes());
		
	}
//	public void storeTempData(String string, HashMap<String, String> resultMap,
//			String string2) {
//		// TODO Auto-generated method stub
//		
//	}
	public String storeTempData(String key, Object obj, String dimension) throws JsonGenerationException, IOException
    {    
		System.out.println("*******Inside MyGaDataStoreService************");
    	System.out.println("Checking key in storeTempData::"+key);
    	System.out.println("Checking obj in storeTempData::"+obj);
    	System.out.println("Checking dimension in storeTempData::"+dimension);
    	 String  myobj = convertObjectToJson(obj).toString();
 		//HashMap hm=datastoreService.convertJsonToMap(jsonData);
       HashMap myhmap = convertJsonToMap(myobj);
            System.out.println("Checking::access_token in sop"+myhmap.get("access_token"));
            System.out.println("Checking::token_type in sop"+myhmap.get("token_type"));
            System.out.println("Checking::expires_in in sop"+myhmap.get("expires_in"));
            System.out.println("Checking:: refresh_token in sop::"+myhmap.get("refresh_token"));
            HashMap myhmap2 = new HashMap();
            myhmap2.put("access_token",myhmap.get("access_token"));
            myhmap2.put("token_type",myhmap.get("token_type"));
            myhmap2.put("expires_in",myhmap.get("expires_in"));
            myhmap2.put("refresh_token", myhmap.get("refresh_token"));
            System.out.println("Checking :::myhmap2:::"+myhmap2);
            //myhmap2.put("refresh_token",myhmap.get("refresh_token"));
            
//            String tempaccess_token = (String) myhmap.get("access_token");
//            String temptoken_type =(String)myhmap.get("token_type");
//            Integer tempexpires_in = (Integer) myhmap.get("expires_in");
//            String temprefresh_token =(String)myhmap.get("refresh_token");
//            System.out.println("tempaccess_token::"+tempaccess_token);
//            System.out.println("tempexpires_in:: "+tempexpires_in);
//            System.out.println("temptoken_type"+temptoken_type);
//            System.out.println("temprefresh_token"+temprefresh_token);
         System.out.println("**********Ends************");   
    	 
    	try {
		System.out.println("Inside storeTempData try block");
		//				DataStoreManager.set(key,dimension,compressData);
		System.out.println("Calling MyDatastoreManager.set():::");
           MyDataStoreManager.set(key,dimension,myhmap2);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
    }
	
	 public static String convertObjectToJson( Object historyObject ) throws JsonGenerationException , JsonMappingException , IOException
	    {
	    	
	    	Writer writer = new StringWriter();
	    	String result = "";
	    	mapper.writeValue( writer , historyObject );
	    	result = writer.toString();
	    	return result;
	    }
	 
	 public static HashMap <String,String> convertJsonToMap( String obj )	throws JsonParseException ,
		JsonMappingException ,
		IOException
		{
			HashMap <String,String> rowDataArrayList = mapper.readValue( obj ,
			new TypeReference <HashMap <String,String>>()
			{
			} );
			return rowDataArrayList;
		}

//	public String getTempData(String string, String string2) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	 public String getTempData(String key, String dimension) throws JsonMappingException
//	    {String jsonData="";
//	    	try {
//				
//				System.out.println("DataCompressed");
//				//setting into datastore
//				System.out.println("Checking Key:::"+key);
//				System.out.println("Checking dimension::"+dimension);
//				System.out.println("Checking key and dimension::using get:::"+DataStoreManager.get(key,dimension));
//				//byte[] rowDataInByte =DataStoreManager.get(key,dimension);
//				byte[] rowDataInByte1=MyDataStoreManager.get(key,dimension);
//				jsonData=ZipData.extractBytes(rowDataInByte1);
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (DataFormatException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
// 		return jsonData;
//	    }

	public String getTempData(String key, String dimension)throws JsonMappingException {
		String jsonData="";
		String jsonData1="";
    	try {
			System.out.println("Inside getTempData method::");
			System.out.println("DataCompressed");
			//setting into datastore
			System.out.println("Checking Key:::"+key);
			System.out.println("Checking dimension::"+dimension);
			System.out.println("Checking key and dimension::using get:::"+DataStoreManager.get(key,dimension));
			//byte[] rowDataInByte =DataStoreManager.get(key,dimension);
			byte[] rowDataInByte1=MyDataStoreManager.get(key,dimension);
			//System.out.println("Checking rowDataInByte1::"+rowDataInByte1);
	    	 jsonData1 = new String(rowDataInByte1);
     		//System.out.println("Testing :: jsonData1"+jsonData1);
			//we are not using jsonData .
			jsonData=ZipData.extractBytes(rowDataInByte1);
			
			//System.out.println("***********Check starts***************");
//			String jsonData1 = new String(rowDataInByte1);
//			System.out.println("Testing :: jsonData1"+jsonData1);
//			String jsonData2 = rowDataInByte1.toString();
//			System.out.println("Testing::jsonData2"+jsonData2);
//			//myString = new JSONObject().put("JSON", "Hello, World!").toString();
//			
//			String myString="";
//			try {
//				JSONObject myjsonobj = new JSONObject();
//				myjsonobj.put("access_token", jsonData1);
//				System.out.println("Testing myjsonobj after converting to JSON::"+myjsonobj);
//				 myString = new JSONObject().put("access_token",jsonData1).toString();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			   System.out.println("Testing myString after converting to JSON::"+myString);
			   //System.out.println("Testing myjsonobj after converting to JSON::"+myjsonobj);
			//System.out.println("Checking jsonData in MyDataStoreManager::"+jsonData);
		//	System.out.println("***********Check ends***************");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return jsonData1;
		// TODO Auto-generated method stub
		
	}
	 

}
