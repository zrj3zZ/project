package com.iwork.webservice.webservice;

import java.util.Hashtable;

import net.sf.json.JSONObject;


/**  
 * @Project kingoa-root-src
 * @Title CommonHander.java
 * @Package com.iwork.webservice.webservice
 * @Description TODO
 * @author yanglei6 yanglei6@kingsoft.com,lmyanglei@gmail.com
 * @date 2013-11-28 下午2:23:37
 * @Copyright 2013 www.kingsoft.com All rights reserved.
 * @version v1.0  
 */

public class CommonWebServiceHander extends WebServiceHanderAbstract{

	@Override
	public String execute(String str) {
		String returnValue = "";
		if(null != str && !"".equals(str)){
			// result
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("result","结果");
			
			// resultContent sub
			JSONObject resultContentSub = new JSONObject();
			resultContentSub.put("detail1","1427-2");
			resultContentSub.put("detail2",str);
			
			// resultContent
			jsonObject.put("detail",resultContentSub);
			
			returnValue = jsonObject.toString();
//			returnValue = "{\"result\":\"结果\",\"resultContent\":\"4\"}";
		}else{
			returnValue = "error";
		}
		
		return returnValue;
	}

	@Override
	public Hashtable<String, String> execute(Hashtable<String, String> hashtable) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
