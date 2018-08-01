package com.ibpmsoft.project.cbpmc.util;

public class StringUtil {
	public static String isNull(Object inputData)
	  {
	    if (inputData == null) {
	      return ""; 
	    }
	    return inputData.toString();
	  }

	  public static String isNullToZero(String inputData)
	  {
	    if ((inputData == null) || (inputData.equals(""))) {
	      return "0";
	    }
	    return inputData;
	  }

	  public static boolean isNumberic(String str) {
	    int i = str.length();
	    do { int chr = str.charAt(i);
	      if ((chr < 48) || (chr > 57))
	        return false;
	      i--; } while (i >= 0);

	    return true;
	  }
}
