package com.iwork.plugs.sms.util.model;
/**
 * 所有短信平台的用户名\密码和其它参数
 * @author WangRongtao
 *
 */
public class SmsPropertiesModel {

	private String user;
	private String pwd;
	private String extend1;
	private String extend2;
	private String extend3;
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getExtend1() {
		return extend1;
	}
	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}
	public String getExtend2() {
		return extend2;
	}
	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}
	public String getExtend3() {
		return extend3;
	}
	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}
	
}
