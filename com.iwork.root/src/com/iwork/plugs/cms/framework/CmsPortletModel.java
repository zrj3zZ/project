package com.iwork.plugs.cms.framework;

/**
 * 一个栏目对象
 * @author David
 *
 */
public class CmsPortletModel {
	/**
	 * 实现类
	 */
	private String infoClass;
	
	private String windowType;
	
	private String key;
	
	
	/**
	 * 接口
	 */
	private String infoInterface;
	/**
	 * 信息项标题
	 */
	private String windowTitle;
	/**
	 * 是否显示外面的边框
	 */
	private boolean isBorder;
	/**
	 * 如果内容显示为列表，最多输出行数
	 */
	private int rows;
	/**
	 * 固定高度，如果大于此高度自动给出滚动条
	 */
	private int windowHeight;
	
	private int windowWidth;
	
	private boolean title_ishidden;

	
	private String portal_url = "";
	
	private String params = "";
	
	private String cache = "";
	
	
	public String getCache() {
		return cache;
	}
	public void setCache(String cache) {
		this.cache = cache;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setKsPortalInfoObject(CmsPortletInterface ksPortalInfoObject) {
		this.ksPortalInfoObject = ksPortalInfoObject;
	}
	public boolean isTitle_ishidden() {
		return title_ishidden;
	}
	public void setTitle_ishidden(boolean titleIshidden) {
		title_ishidden = titleIshidden;
	}
	public int getWindowWidth() {
		return windowWidth;
	}
	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getPortal_url() {
		return portal_url;
	}
	public void setPortal_url(String portalUrl) {
		portal_url = portalUrl;
	}
	private CmsPortletInterface ksPortalInfoObject;
	
	
	public CmsPortletInterface getKsPortalInfoObject() {
		return ksPortalInfoObject;
	}
	public void setRelatedInfoObject(CmsPortletInterface ksPortalInfoObject) {
		ksPortalInfoObject = ksPortalInfoObject;
	}
	public String getInfoClass() {
		return infoClass;
	}
	public void setInfoClass(String infoClass) {
		this.infoClass = infoClass;
	}
	public String getInfoInterface() {
		return infoInterface;
	}
	public void setInfoInterface(String infoInterface) {
		this.infoInterface = infoInterface;
	}
	
	public boolean isBorder() {
		return isBorder;
	}
	public void setBorder(boolean isBorder) {
		this.isBorder = isBorder;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getWindowHeight() {
		return windowHeight;
	}
	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}
	public String getWindowTitle() {
		return windowTitle;
	}
	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}
	public String getWindowType() {
		return windowType;
	}
	public void setWindowType(String windowType) {
		this.windowType = windowType;
	}
}
