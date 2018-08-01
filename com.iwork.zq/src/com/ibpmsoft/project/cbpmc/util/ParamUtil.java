package com.ibpmsoft.project.cbpmc.util;

public class ParamUtil {
	  public static Long setLong(Object obj){
		  Long l = new Long(0);
		  if(obj!=null){
			  l = Long.parseLong(obj.toString());
		  }
		 return l;
	  }
	   
	  public static Double setDouble(Object obj){
		  Double l = new Double(0);
		  if(obj!=null){
			  l = Double.parseDouble(obj.toString());
		  }
		  return l;
	  }
}
