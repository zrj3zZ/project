package com.iwork.app.conf;

/**
* 抓取参数
*
* @author zhangrui
* @version 2.2.1
* @preserve 声明此方法不被JOC混淆
*/
public class CrawlerConf extends Config {

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
