package com.iwork.app.navigation.web.model;

public class TopMenuModel {
	
	public static final String LAYOUT_OPENTYPE_FULL = "full";
	public static final String LAYOUT_OPENTYPE_LEFTMENU = "leftMenu";
	private String title;
	private String url;
	private String openType;
	private String key;
	/**
	 * 判读登陆次数
	 */
	public static int user_Login_Count = 1;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOpenType() {
		return openType;
	}
	public void setOpenType(String openType) {
		this.openType = openType;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
}
