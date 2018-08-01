package com.iwork.webservice.webservice;

import java.util.Hashtable;


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

public class DemoWebServiceHander extends WebServiceHanderAbstract{

	@Override
	public String execute(String str) {
		String returnValue = "";
		if(null != str && !"".equals(str)){
			returnValue = "DemoWebServiceHander";
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
