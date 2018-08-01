package com.iwork.app.mobile.stack;

import java.util.HashMap;
	public class MobileIdentityManage {
	public static HashMap identityStackList = new HashMap();
	private static Object lock = new Object();
	private static MobileIdentityManage instance;  
	public static MobileIdentityManage getInstance(){  
	    if (instance == null){  
	        synchronized( lock ){  
	            if (instance == null){  
	                instance = new MobileIdentityManage();  
	            }
	        }  
	    }  
	    return instance;  
	}
	
	/**
	 * 装载身份验证信息
	 * @param userid
	 * @param sessionId
	 */
	public void putIdentity(String userid,String sessionId){
		if(userid!=null&&sessionId!=null){
			identityStackList.put(sessionId,userid);
		}
	}
	
	/**
	 * 验证身份
	 * @param userid
	 * @param sessionId
	 * @return
	 */
	public boolean checkIdentity(String sessionId){
		boolean flag = false;
		String activeUser = (String)identityStackList.get(sessionId);
		if(activeUser!=null){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 验证身份
	 * @param userid
	 * @param sessionId
	 * @return
	 */
	public boolean checkIdentity(String userid,String sessionId){
		boolean flag = false;
		String activeUser = (String)identityStackList.get(sessionId);
		if(activeUser!=null){
			if(activeUser.equals(userid)){
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 获得sessionId
	 * @param userid
	 * @return
	 */
	public String getUserId(String sessionId){
		String userid = (String)identityStackList.get(sessionId);
		return userid;
	}
}
