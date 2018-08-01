package com.iwork.plugs.rss.util;

import java.util.HashMap;
import java.util.List;

/**
 * 设置RSS查询方式
 * 
 * @author SuQi
 * 
 */
public class IworkPlugsRssUtil {

	public static HashMap<String, String> hash = new HashMap<String, String>();
	static {
		hash.put("BAIDU", "http://news.baidu.com/ns?tn=newsrss&sr=0&cl=2&rn=10&ct=0&word=title%3A");
		hash.put("GOOGLE", "http://news.google.com/news?hl=zh-CN&ned=cn&ie=UTF-8&output=rss&q=");
		hash.put("YAHOO", "http://cn.news.yahoo.com/searchrss2.html?pid=yisou&title=1&b=1&sort=g&p=");
	}

	public static boolean isItEquals(List list, String str) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			if (str.equals(list.get(i).toString())) {
				return true;
			}
		}
		return false;
	}

}
