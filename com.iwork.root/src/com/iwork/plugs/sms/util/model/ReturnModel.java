package com.iwork.plugs.sms.util.model;

/**
 * 发送短信后的适回值javabean
 * @author WangRongtao
 *
 */
public class ReturnModel {

	private String returnkey;//返回值
	private String returnvalue;//返回值对应的含义
	private String issuccess;//是否发送成功
	private String platform;//短信平台的名称

	public String getReturnkey() {
		return returnkey;
	}

	public String getReturnvalue() {
		return returnvalue;
	}

	public void setReturnvalue(String returnvalue) {
		this.returnvalue = returnvalue;
	}

	public String getIssuccess() {
		return issuccess;
	}

	public void setIssuccess(String issuccess) {
		this.issuccess = issuccess;
	}

	public void setReturnkey(String returnkey) {
		this.returnkey = returnkey;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
}
