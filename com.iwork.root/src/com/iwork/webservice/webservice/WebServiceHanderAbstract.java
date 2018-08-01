package com.iwork.webservice.webservice;

import java.util.Hashtable;

/**  
 * @Project kingoa-root-src
 * @Title WebServiceHanderAbstract.java
 * @Package com.iwork.webservice.webservice
 * @Description TODO
 * @author yanglei6 yanglei6@kingsoft.com,lmyanglei@gmail.com
 * @date 2013-11-28 下午2:39:25
 * @Copyright 2013 www.kingsoft.com All rights reserved.
 * @version v1.0  
 */

public abstract class WebServiceHanderAbstract {

	public abstract String execute(String str);
	
	public abstract Hashtable<String,String> execute(Hashtable<String,String> hashtable);
	
}
