/*
 * Copyright 2003,2004,2005,2006,2007,2008 Actionsoft Co.,Ltd
 * AWS BPM(Business Process Management) PLATFORM Source 
 * AWS is a application middleware for BPM system,
 * Powered by actionsoft,China.
 * 
 * 本软件工程版权归属北京炎黄盈动科技发展有限责任公司
 * 所有，受国家版权局及相关法律保护，未经书面法律许可，
 * 任何单位或个人都不得泄漏和公开此源码全部或部分文件。
 * 属于北京炎黄盈动公司机密知识产权，违者必究。
 * 
 * http://www.actionsoft.com.cn
 * 
 */

package com.iwork.app.conf;


/**
 * 
 *系统邮件配置
 * @author david.yang
 * @version 2.2.1 
 * @preserve 声明此方法不被JOC混淆
 */
public class MailServerConf extends Config {
   private String mailFrom;
   private String mailuser;
   private String password;
   private String smtp_services;
   private String smtp_host;
   private String smtp_port;
   private String smtp_auth;
   private String smtp_ssl;
   private String pop3_services;
   private String pop3_host;
   private String pop3_port;
   private String pop3_auth;
   private String pop3_ssl;
   private String pop3_backup;
   private String isDebug;
   private String debug_address;
   
	   
	public String getMailuser() {
		return mailuser;
	}
	public void setMailuser(String mailuser) {
		this.mailuser = mailuser;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSmtp_services() {
		return smtp_services;
	}
	public void setSmtp_services(String smtp_services) {
		this.smtp_services = smtp_services;
	}
	public String getSmtp_host() {
		return smtp_host;
	}
	public void setSmtp_host(String smtp_host) {
		this.smtp_host = smtp_host;
	}
	public String getSmtp_port() {
		return smtp_port;
	}
	public void setSmtp_port(String smtp_port) {
		this.smtp_port = smtp_port;
	}
	public String getSmtp_auth() {
		return smtp_auth;
	}
	public void setSmtp_auth(String smtp_auth) {
		this.smtp_auth = smtp_auth;
	}
	public String getSmtp_ssl() {
		return smtp_ssl;
	}
	public void setSmtp_ssl(String smtp_ssl) {
		this.smtp_ssl = smtp_ssl;
	}
	public String getPop3_services() {
		return pop3_services;
	}
	public void setPop3_services(String pop3_services) {
		this.pop3_services = pop3_services;
	}
	public String getPop3_host() {
		return pop3_host;
	}
	public void setPop3_host(String pop3_host) {
		this.pop3_host = pop3_host;
	}
	public String getPop3_port() {
		return pop3_port;
	}
	public void setPop3_port(String pop3_port) {
		this.pop3_port = pop3_port;
	}
	public String getPop3_auth() {
		return pop3_auth;
	}
	public void setPop3_auth(String pop3_auth) {
		this.pop3_auth = pop3_auth;
	}
	public String getPop3_ssl() {
		return pop3_ssl;
	}
	public void setPop3_ssl(String pop3_ssl) {
		this.pop3_ssl = pop3_ssl;
	}
	public String getPop3_backup() {
		return pop3_backup;
	}
	public void setPop3_backup(String pop3_backup) {
		this.pop3_backup = pop3_backup;
	}
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	public String getIsDebug() {
		return isDebug;
	}
	public void setIsDebug(String isDebug) {
		this.isDebug = isDebug;
	}
	public String getDebug_address() {
		return debug_address;
	}
	public void setDebug_address(String debug_address) {
		this.debug_address = debug_address;
	}
	   
}
