
package com.iwork.app.login.control;
import java.io.Serializable;
/**
 * @author David.Yang
 *
 * 用户身份登录上下文数据
 * @preserve 声明此方法不被JOC混淆
 */
public class LoginContext implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7529609746904615151L;
	public static final String LOGIN_DEVICE_TYPE_WEB="web"; 
    public static final String LOGIN_DEVICE_TYPE_WEIXIN="weixin"; 
    public static final String LOGIN_DEVICE_TYPE_IOS="iosMobile";
    public static final String LOGIN_DEVICE_TYPE_ANDROID="androidMobile";
    
    private String uid;
    private String pwd;
    private String MD5Pwd;
    private String ip;
    private String deviceType;
    private Long loginType;
    private String param1;
    private String param2;
    
    
    

	/**
     * @return Returns the mD5Pwd.
     * @preserve 声明此方法不被JOC混淆
     */
    public String getMD5Pwd() {
        return MD5Pwd;
    }
    /**
     * @param pwd The mD5Pwd to set.
     * @preserve 声明此方法不被JOC混淆
     */
    public void setMD5Pwd(String pwd) {
        MD5Pwd = pwd;
    }
    /**
     * @return Returns the ip.
     * @preserve 声明此方法不被JOC混淆
     */
    public String getIp() {
        return ip;
    }
    /**
     * @param ip The ip to set.
     * @preserve 声明此方法不被JOC混淆
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
    /**
     * @return Returns the pwd.
     * @preserve 声明此方法不被JOC混淆
     */
    public String getPwd() {
        return pwd;
    }
    /**
     * @param pwd The pwd to set.
     * @preserve 声明此方法不被JOC混淆
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    /**
     * @return Returns the uid.
     * @preserve 声明此方法不被JOC混淆
     */
    public String getUid() {
        return uid;
    }
    /**
     * @param uid The uid to set.
     * @preserve 声明此方法不被JOC混淆
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public Long getLoginType() {
		return loginType;
	}
	public void setLoginType(Long loginType) {
		this.loginType = loginType;
	}
	

}
