package com.iwork.plugs.cms.framework;

public class DomainModel {
	private String windowTitle = "";
	private String cache = "";
	private String cacheTime = "";
	private String portalPage = "";
	
	public String getPortalPage() {
		return portalPage;
	}
	public void setPortalPage(String portalpage) {
		this.portalPage = portalpage;
	}
	public String getWindowTitle() {
		return windowTitle;
	}
	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}
	public String getCache() {
		return cache;
	}
	public void setCache(String cache) {
		this.cache = cache;
	}
	public String getCacheTime() {
		return cacheTime;
	}
	public void setCacheTime(String cacheTime) {
		this.cacheTime = cacheTime;
	}
	
	
}
