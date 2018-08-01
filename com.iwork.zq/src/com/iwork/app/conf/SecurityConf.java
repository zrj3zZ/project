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
 *分级保护配置
 * @author david.yang
 * @version 2.2.1 
 * @preserve 声明此方法不被JOC混淆
 */
public class SecurityConf extends Config {
	  private String role_mode;
	    private String sys_owner;
	    private String security_owner;
	    private String audit_owner;
	    private String log_auto_clear;
	    private int log_days ;
	    
	    
	    
		public String getRole_mode() {
			return role_mode;
		}
		public void setRole_mode(String roleMode) {
			role_mode = roleMode;
		}
		public String getSys_owner() {
			return sys_owner;
		}
		public void setSys_owner(String sysOwner) {
			sys_owner = sysOwner;
		}
		public String getSecurity_owner() {
			return security_owner;
		}
		public void setSecurity_owner(String securityOwner) {
			security_owner = securityOwner;
		}
		public String getAudit_owner() {
			return audit_owner;
		}
		public void setAudit_owner(String auditOwner) {
			audit_owner = auditOwner;
		}
		public int getLog_days() {
			return log_days;
		}
		public void setLog_days(int logDays) {
			log_days = logDays;
		}
		public String getLog_auto_clear() {
			return log_auto_clear;
		}
		public void setLog_auto_clear(String logAutoClear) {
			log_auto_clear = logAutoClear;
		}
	   
}
