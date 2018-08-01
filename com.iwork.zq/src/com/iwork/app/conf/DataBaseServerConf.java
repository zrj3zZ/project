package com.iwork.app.conf;
/**
 * 
 * @author David.yang
 *
 */
public class DataBaseServerConf extends Config{
		private String supply;
	    private String driver;
	    private String url;
	    private String userName;
	    private String password;
	    private String debug;

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public void setSupply(String supply) {
	        this.supply = supply;
	    }

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public String getSupply() {
	        return (this.supply == null) ? "" : this.supply;
	    }

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public void setDriver(String driver) {
	        this.driver = driver;
	    }

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public String getDriver() {
	        return (this.driver == null) ? "" : this.driver;
	    }

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public void setUrl(String url) {
	        this.url = url;
	    }

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public String getUrl() {
	        return (this.url == null) ? "http://www.iworksoft.com.cn/"
	                                  : this.url;
	    }

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public void setUserName(String userName) {
	        this.userName = userName;
	    }

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public String getUserName() {
	        return (this.userName == null) ? "" : this.userName;
	    }

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public void setPassword(String password) {
	        this.password = password;
	    }

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public String getPassword() {
	        return (this.password == null) ? "" : this.password;
	    }

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public void setDebug(String debug) {
	        this.debug = debug;
	    }

	    /**
	     * @preserve 声明此方法不被JOC混淆
	     */
	    public String getDebug() {
	        return (this.debug == null) ? "" : this.debug;
	    }

}
