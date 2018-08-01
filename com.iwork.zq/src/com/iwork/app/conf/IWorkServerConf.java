package com.iwork.app.conf;

import java.io.File;

/**
 * 配置参数
 *
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class IWorkServerConf extends Config {
    private String title;
    private String shortTitle;
    private String version;
    private String loginURL;
    private String debug;
    private String platform;
    private String srcPath;
    private String userDefaultPassword;
    private String shortmessageServer;
    private String loginClassAdapter; 
    private String ssoClassAdapter; 
    private String orgClassAdapter; 
    private String defaultLocale;  //默认语言包
    private String isMultiLanguage; //是否支持多语言
    private String groupMode; //集团化模式
    private String loginVerify; 
    private String logincont;
    private String currentpath = new File(IWorkServerConf.class.getResource("/").getPath()).getParent();
    private String userDefaultLookAndFeel;
    private String taskServerTime ;
    private String cacheTime;
    private String sessionTime;
    private String multiRole;
    private String jdbcEncrypt;
    private Integer userDefaultPasswordCount;
    private String isHttps;
    private String isRSA;
    private String clusterPath;
    private String clusterServer;
    private String entrustProcess;

	public String getClusterPath() {
		return clusterPath;
	}

	public void setClusterPath(String clusterPath) {
		this.clusterPath = clusterPath;
	}

	public String getClusterServer() {
		return clusterServer;
	}

	public void setClusterServer(String clusterServer) {
		this.clusterServer = clusterServer;
	}

	public String getEntrustProcess() {
		return entrustProcess;
	}

	public void setEntrustProcess(String entrustProcess) {
		this.entrustProcess = entrustProcess;
	}

	public String getIsRSA() {
		return isRSA;
	}

	public void setIsRSA(String isRSA) {
		this.isRSA = isRSA;
	}

	public String getIsHttps() {
		return isHttps;
	}

	public void setIsHttps(String isHttps) {
		this.isHttps = isHttps;
	}

	public Integer getUserDefaultPasswordCount() {
		return userDefaultPasswordCount;
	}

	public void setUserDefaultPasswordCount(Integer userDefaultPasswordCount) {
		this.userDefaultPasswordCount = userDefaultPasswordCount;
	}

	public String getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(String cacheTime) {
		this.cacheTime = cacheTime;
	}

	public String getCurrentpath() {
		if(currentpath==null){
			currentpath = new File(IWorkServerConf.class.getResource("/").getPath()).getParent();
		}
		return currentpath;
	}

	public void setCurrentpath(String currentpath) {
		this.currentpath = currentpath;
	}

	public String getLogincont() {
		 if (this.logincont == null){
			  logincont = "10";
		 }
		 return logincont;
	}

	public void setLogincont(String logincont) {
		this.logincont = logincont;
	}

	/**
     * @preserve 声明此方法不被JOC混淆
     */
    public void setLoginClassAdapter(String loginClassAdapter) {
        this.loginClassAdapter = loginClassAdapter;
    }

    /**
     * @preserve 声明此方法不被JOC混淆
     */
    public String getLoginClassAdapter() {
        if (this.loginClassAdapter == null){
            this.loginClassAdapter = "";
        }
        return this.loginClassAdapter;
    }
    
    /**
     * @preserve 声明此方法不被JOC混淆
     */
    public void setShortmessageServer(String shortmessageServer) {
        this.shortmessageServer = shortmessageServer;
    }

    /**
     * @preserve 声明此方法不被JOC混淆
     */
    public String getShortmessageServer() {
        if ((this.shortmessageServer == null) ||
                this.shortmessageServer.equals("")) {
            this.shortmessageServer = "off";
        }

        return this.shortmessageServer;
    }

    /**
     * @preserve 声明此方法不被JOC混淆
     */
    public void setUserDefaultPassword(String userDefaultPassword) {
        this.userDefaultPassword = userDefaultPassword;
    }

    /**
     * @preserve 声明此方法不被JOC混淆
     */
   public String getUserDefaultPassword() {
        if ((this.userDefaultPassword == null) ||
                this.userDefaultPassword.equals("")) {
            this.userDefaultPassword = "Taopuyi@2013";
        } 

        return this.userDefaultPassword;
    }
    

    /**
     * @preserve 声明此方法不被JOC混淆
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

	/**
     * @preserve 声明此方法不被JOC混淆
     */
    public String getPlatform() {
        return this.platform;
    }

	public String getUserDefaultLookAndFeel() {
		return userDefaultLookAndFeel;
	}

	public void setUserDefaultLookAndFeel(String userDefaultLookAndFeel) {
		this.userDefaultLookAndFeel = userDefaultLookAndFeel;
	}

	public String getTaskServerTime() {
		return taskServerTime;
	}

	public void setTaskServerTime(String taskServerTime) {
		this.taskServerTime = taskServerTime;
	}

	public String getSessionTime() {
		return sessionTime;
	}

	public void setSessionTime(String sessionTime) {
		this.sessionTime = sessionTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getLoginURL() {
		return loginURL;
	}
	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}

	public String getDebug() {
		return debug;
	}
	public void setDebug(String debug) {
		this.debug = debug;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLoginVerify() {
		return loginVerify;
	}

	public void setLoginVerify(String loginVerify) {
		this.loginVerify = loginVerify;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public String getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public String getIsMultiLanguage() {
		return isMultiLanguage;
	}
 
	public void setIsMultiLanguage(String isMultiLanguage) {
		this.isMultiLanguage = isMultiLanguage;
	}

	public String getSsoClassAdapter() {
		return ssoClassAdapter;
	}

	public void setSsoClassAdapter(String ssoClassAdapter) {
		this.ssoClassAdapter = ssoClassAdapter;
	}

	public String getOrgClassAdapter() {
		return orgClassAdapter;
	}

	public void setOrgClassAdapter(String orgClassAdapter) {
		this.orgClassAdapter = orgClassAdapter;
	}

	public String getGroupMode() {
		return groupMode;
	}

	public void setGroupMode(String groupMode) {
		this.groupMode = groupMode;
	}

	public String getMultiRole() {
		return multiRole;
	}

	public void setMultiRole(String multiRole) {
		this.multiRole = multiRole;
	}

	public String getJdbcEncrypt() {
		return jdbcEncrypt;
	}
	
	public void setJdbcEncrypt(String jdbcEncrypt) {
		this.jdbcEncrypt = jdbcEncrypt;
	}
}
