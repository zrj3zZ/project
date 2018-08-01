package com.iwork.plugs.meeting.util;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class StringTokenizerUtil {
	public static Object[] stringTokenizer(String str, String delim) {
		StringTokenizer st = new StringTokenizer(str, delim);

		String[] s = null;
		ArrayList result = new ArrayList();
		while (st.hasMoreTokens()) {
			String ss = st.nextToken();
			result.add(ss);
		}
		if (result.size() > 0) {
			s = new String[result.size()];
			result.toArray(s);
		}
		return s;
	}
}