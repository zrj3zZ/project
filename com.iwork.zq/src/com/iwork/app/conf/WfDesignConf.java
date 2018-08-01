package com.iwork.app.conf;


/**
 * 及时通讯配置参数
 *
 * @author david.yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class WfDesignConf extends Config {
    private String ip;
    private String port;
    
    
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
    

}
