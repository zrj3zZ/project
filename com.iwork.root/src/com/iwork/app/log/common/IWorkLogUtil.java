package com.iwork.app.log.common;

public class IWorkLogUtil {

	public static String filterString(String srcString, String[][] filterString) {
		if (srcString == null) {
			srcString = "";
			return srcString;
		}
		String str = filterString[Integer.parseInt(srcString)][1];
		return str;
	}
}
