package com.service;

import java.util.ArrayList;
import java.util.HashMap;

public class V2CorrectionforAgentAction {

	public static HashMap convertArraytoMap(ArrayList arrayList, int i) {

		

		 ArrayList temparr  = new ArrayList(arrayList);

		   HashMap outerhm = new HashMap();
		   HashMap innerhm = new HashMap();
		 for (int i1 = 0; i1 < temparr.size(); i1++) {

			    String one = temparr.get(0).toString();
			    String two =temparr.get(1).toString();
			    String three = temparr.get(2).toString();
			    String four= temparr.get(3).toString();
			    String five= temparr.get(4).toString();
			    String six =temparr.get(5).toString();
			    String seven = temparr.get(6).toString();
			    String eight = temparr.get(7).toString();
			    
			    innerhm.put("one", one);
			    innerhm.put("two", two);
			    innerhm.put("three", three);
			    innerhm.put("four", four);
			    innerhm.put("five", five);
			    innerhm.put("six", six);
			    innerhm.put("seven", seven);
			    innerhm.put("eight", eight);
			    
			    
			    
			}

		    outerhm.putAll(innerhm);
		 HashMap hm1 = new HashMap();
		// System.out.println("******");
		// System.out.println("0::"+object);
		 
		 
		// System.out.println("******");
		return outerhm;
	
	}
    /*------------------------------------------------------------------*/
	public static HashMap removefaultfordata(HashMap hashMap, int j) {

		 HashMap tempmap = new HashMap(hashMap);
        String one = tempmap.get("one").toString();
     String two =  tempmap.get("two").toString();
     String three = tempmap.get("three").toString();
     String four = tempmap.get("four").toString();
    String five =  tempmap.get("five").toString();
    String six =  tempmap.get("six").toString();
     String seven = tempmap.get("seven").toString();
     String eight =tempmap.get("eight").toString();
         if(two.contains("DI")){
      	    two ="DI Select";
         }
         HashMap tempmap2 = new HashMap();
         
         tempmap2.put("one",one);
         tempmap2.put("two",two);
         tempmap2.put("three",three);
         tempmap2.put("four",four);
         tempmap2.put("five",five);        
         tempmap2.put("six",six);
         tempmap2.put("seven",seven);
         tempmap2.put("eight",eight);
               
         
     
		return tempmap2;
	
	}
	/*----------------------------------------------------------------*/
	
	

}
