package com.service;

import java.util.ArrayList;
import java.util.HashMap;

public class MyV2CorrectionService {

	public static HashMap convertArraytoMap(ArrayList arrayList, int i) {

		

		 ArrayList temparr  = new ArrayList(arrayList);

		   HashMap outerhm = new HashMap();
		   HashMap innerhm = new HashMap();
		 for (int i1 = 0; i1 < temparr.size(); i1++) {

			    String Account = temparr.get(0).toString();
			    String Action =temparr.get(1).toString();
			    String Agent = temparr.get(2).toString();
			    String OBNumber= temparr.get(3).toString();
			    String InboundConnId= temparr.get(4).toString();
			    String OutboundConnId =temparr.get(5).toString();
			    String TimeStamp = temparr.get(6).toString();
			    String Total = temparr.get(7).toString();
			    
			    innerhm.put("Account", Account);
			    innerhm.put("Action", Action);
			    innerhm.put("Agent", Agent);
			    innerhm.put("OBNumber", OBNumber);
			    innerhm.put("InboundConnId", InboundConnId);
			    innerhm.put("OutboundConnId", OutboundConnId);
			    innerhm.put("TimeStamp", TimeStamp);
			    innerhm.put("Total", Total);
			    
			    
			    
			}

		    outerhm.putAll(innerhm);
		 HashMap hm1 = new HashMap();
		// System.out.println("******");
		// System.out.println("0::"+object);
		 
		 
		// System.out.println("******");
		return outerhm;
	
	}

	public static HashMap removefaultfordata(HashMap hashMap, int j) {

		 HashMap tempmap = new HashMap(hashMap);
         String account = tempmap.get("Account").toString();
      String action =  tempmap.get("Action").toString();
      String Agent = tempmap.get("Agent").toString();
      String obnumber = tempmap.get("OBNumber").toString();
     String ibconnid =  tempmap.get("InboundConnId").toString();
     String obconnid =  tempmap.get("OutboundConnId").toString();
      String timestamp = tempmap.get("TimeStamp").toString();
      String total =tempmap.get("Total").toString();
          if(account.contains("@a-cti.com")){
       	   if(Agent.contains("GMT")){
       		   Agent =account;
       		   account ="N/A";
       	   }
          }
          HashMap tempmap2 = new HashMap();
          
          tempmap2.put("Account",account);
          tempmap2.put("Action",action);
          tempmap2.put("Agent",Agent);
          tempmap2.put("OBNumber",obnumber);
          tempmap2.put("InboundConnId",ibconnid);
          tempmap2.put("OutboundConnId",obconnid);
          tempmap2.put("TimeStamp",timestamp);
          tempmap2.put("Total",total);
                
          
      
		return tempmap2;
	
	}
/**
 * Helper method for converting HashMap to ArrayList. This method is created by solomon. V2Outboundreport Url is using this method
 * @param hashMap
 * @return
 */
//	public static ArrayList convertHmaptoArrayList(HashMap hashMap) {
//		
//	}

//	public static ArrayList convertHmaptoArrayList(HashMap hashMap) {
//		
//	}

}
