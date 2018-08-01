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
 * IWORK系统平台数据表单引擎优化配置参数
 *
 * @author david.yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class IformConf extends Config {
    private String template_src;
    private String template_home;
    private String template_history;
    private String is_diy_path;

	public String getTemplate_home() {
		return template_home;
	}

	public void setTemplate_home(String template_home) {
		this.template_home = template_home;
	}

	public String getIs_diy_path() {
		return is_diy_path;
	}

	public void setIs_diy_path(String is_diy_path) {
		this.is_diy_path = is_diy_path;
	}

	public String getTemplate_history() {
		return template_history;
	}

	public void setTemplate_history(String template_history) {
		this.template_history = template_history;
	}

	public String getTemplate_src() {
		return template_src;
	}

	public void setTemplate_src(String template_src) {
		this.template_src = template_src;
	}

	
 
}
