package com.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

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

import com.google.gson.Gson;
import com.service.JSONCharacterEscapes;
import com.service.MyGaDatastoreService;

public class MyDataStoreManager {
	private static final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	
	private static final Logger mLogger = Logger.getLogger(MyDataStoreManager.class.getPackage().getName());
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
	public static void set(String key, String dimension, HashMap myhmap2) throws JsonGenerationException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		System.out.println("***Inside MyDataStoreManager**********");
		System.out.println("checking Emailid in MyDataStoreManager::"+key);
		System.out.println("checking dimensionn parameter::"+dimension);
		 String  myobj = convertObjectToJson(myhmap2).toString();
	 		//HashMap hm=datastoreService.convertJsonToMap(jsonData);
		  Gson gjson = new Gson();
		  // Student student = gson.fromJson(br, Student.class);  
		  GAUserDetails gaud = gjson.fromJson(myobj, GAUserDetails.class);
		  System.out.println("*************");
          System.out.println("Checking::"+gaud.getAccess_token());
          System.out.println("Checking::"+gaud.getExpires_in());
          System.out.println("Checking::"+gaud.getToken_type());
          System.out.println("Checking::"+gaud.getRefresh_token());
          System.out.println("*************");


	       HashMap myhmap = convertJsonToMap(myobj);
	       System.out.println("Checking::access_token in sop"+myhmap.get("access_token"));
           System.out.println("Checking::token_type in sop"+myhmap.get("token_type"));
           System.out.println("Checking::expires_in in sop"+myhmap.get("expires_in"));
           System.out.println("Checking:: refresh_token in sop::"+myhmap.get("refresh_token"));
           HashMap myhmap3 = new HashMap();
           myhmap2.put("access_token",myhmap.get("access_token"));
           myhmap2.put("token_type",myhmap.get("token_type"));
           myhmap2.put("expires_in",myhmap.get("expires_in"));
           System.out.println("Checking Inside MyDataStoreManager:::myhmap3:::"+myhmap3);
	       try{
	    	   System.out.println("Inside MyDataStoreManager try block");
				PersistenceManager pm = pmf.getPersistenceManager();

	    	   GAUserDetails gauserdetails = new GAUserDetails();
    	   gauserdetails.setKey(key);
    	   gauserdetails.setAccess_token(gaud.getAccess_token());
    	   gauserdetails.setExpires_in(gaud.getExpires_in());
    	   gauserdetails.setToken_type(gaud.getToken_type());
    	   gauserdetails.setRefresh_token(gaud.getRefresh_token());
    	   System.out.println("Storing in Datastore for GAUserDetails");
    	   pm.makePersistent(gauserdetails);
    	   System.out.println("done::::::");
	    	   
	       }catch(Exception e){
	    	   e.printStackTrace();
	       }
	       
	       
	       
		
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
//public static byte[] get(String key, String dimension) {
//	// TODO Auto-generated method stub
//	return null;
//}
 //Old method 
	public static byte[] get(Object key, Object dimension){
		System.out.println("Checking MyDataStoreManager for " + key + ", dimension=" +  dimension);

		//System.out.println("DataStoreManager.get(Object key, Object accountNumber): key=" + key + ", dimension=" +  dimension);
		
		byte[] value = null;
		
		try{
		
			HashMap<Object, Object> accountHashMap = null;
			value = get(key);//check
			///System.out.println("Checking value in get method try block::"+value);
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		System.out.println("let's ");
		System.out.println("Checking value::"+value);
		return value;
	}
	public static byte[] get(Object key){
		System.out.println("INSIDE MyDataStoreManager get method:::key=" + key);
		System.out.println("DataStoreManager.get(Object key): key=" + key);
	
		byte[] objectContent = null;
		
		PersistenceManager pm = pmf.getPersistenceManager();
		
		try{
		
			GAUserDetails value = null; //= pm.getObjectById(GaDataObject.class, key);
			
			try{
				value = pm.getObjectById(GAUserDetails.class, key);
				//System.out.println("data length:::"+value.getValue().length);
				System.out.println("data length acc or ref token ::"+value.getAccess_token().length());
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			System.out.println("we are in get 1 ");
			
			if(value != null){
				objectContent = new byte[0];
				//while(key != null){
					try{
						value = pm.getObjectById(GAUserDetails.class, key);					
					}catch (Exception e) {
						// TODO: handle exception
						//e.printStackTrace();
					}
					System.out.println("we are in get 2 " + key);
					System.out.println(" Lets check accesstoken in get method " + value.getAccess_token());
					System.out.println("Lets check refreshtoken in get method"+value.getRefresh_token());
					String tempaccess_token = value.getAccess_token();
					int tempexpires_in = value.getExpires_in();
					String temptoken_type = value.getToken_type();
					String temprefresh_token = value.getRefresh_token();
					
					HashMap myhmap = new HashMap();
					myhmap.put("access_token",tempaccess_token);
					myhmap.put("token_type",temptoken_type);
					myhmap.put("expires_in",tempexpires_in);
					myhmap.put("refresh_token",temprefresh_token);
					
					Gson gjson = new Gson();
					String mytempstring = gjson.toJson(myhmap);
					//System.out.println("Checking after converting to JSON::mytempstring_check:"+mytempstring);
					//String mytempstring = value.getAccess_token();
					//System.out.println("Checking mytempstring"+mytempstring);
					byte[] tempList = mytempstring.getBytes();
					System.out.println("Checking tempList in MyDataStoreManager::"+tempList);
					//byte[] tempList = (byte[]) value.getValue();
					byte[] tempOriginal = objectContent;
					objectContent = new byte[objectContent.length+tempList.length];
					System.arraycopy(tempOriginal, 0, objectContent, 0, tempOriginal.length);
					System.arraycopy(tempList, 0, objectContent, tempOriginal.length, tempList.length);
				  //  key = value.getNextDSOKey();					
					
				//}
					
				//System.out.println("Checking:::objectContent"+objectContent);
				System.out.println("size of returning arrayList " + objectContent.length);
				
			}
		}catch(JDOObjectNotFoundException je){
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			pm.close();
		}
		return objectContent;
	}
	
 

}
