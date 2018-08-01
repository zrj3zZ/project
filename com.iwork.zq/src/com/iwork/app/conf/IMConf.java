package com.iwork.app.conf;


/**
 * 及时通讯配置参数
 *
 * @author david.yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class IMConf extends Config {
    private String server;
    private String source;
    private String ip;
    private String url;
    private String title ;
    private String isDebug ;
    private String debugUser ;
    
    
    
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIsDebug() {
		return isDebug;
	}
	public void setIsDebug(String isDebug) {
		this.isDebug = isDebug;
	}
	public String getDebugUser() {
		return debugUser;
	}
	public void setDebugUser(String debugUser) {
		this.debugUser = debugUser;
	}
    

}
