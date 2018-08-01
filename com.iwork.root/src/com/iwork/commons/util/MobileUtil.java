package com.iwork.commons.util;
import com.iwork.app.login.control.LoginContext;
import com.iwork.core.organization.tools.UserContextUtil;
/**
 * 涉及移动端的的方法等的工具类。
 * @since  0.4
 */

public class MobileUtil {
	private static MobileUtil instance;  
	public static final String IOS_TXT = "<span  class=\"deviceType\">[发自iPhone]</span>";
	public static final String ANDROID_TXT = "<span class=\"deviceType\">[发自android]</span>";
	public static final String OTHER_TXT = "发自未知设备";
	  private static Object lock = new Object();  
	public static MobileUtil getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new MobileUtil();  
                }
            }
        }
        return instance;
	}
	 public String getFromTxt(){
		 String deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
		 String txt = "";
		 if(deviceType!=null){
			 if(deviceType.equals(LoginContext.LOGIN_DEVICE_TYPE_IOS)){
				 txt = IOS_TXT;
			 }else if(deviceType.equals(LoginContext.LOGIN_DEVICE_TYPE_ANDROID)){
				 txt = ANDROID_TXT;
			 }
		 }
		 return txt;
	 }

}