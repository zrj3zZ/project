package com.iwork.app.persion.model;


/**
 * SysPersonConfig entity. @author MyEclipse Persistence Tools
 */

public class SysPersonConfig implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	

	public static String DATABASE_ENTITY = "SysPersonConfig";
	
	/**
	 * 页面布局
	 */
	public static final String SYS_CONF_TYPE_PAGE_LAYOUT = "pageLayoutSet";
	/**
	 * 表单布局
	 */
	public static final String SYS_CONF_TYPE_FORM_LAYOUT = "formLayoutSet";
	/**
	 * 首页布局
	 */
	public static final String SYS_CONF_TYPE_SKINS_LAYOUT = "skinLayoutSet";
	
	
	/**
	 * 日程模块默认视图
	 */
	public static final String SYS_CALENDAR_DEFAULT_VIEW = "CalendarDefault";
	/**
	 * 日程模块是否共享
	 */
	public static final String SYS_CALENDAR_IS_SHARE = "CalendarIsShare";
	/**
	 * 日程模块是否共享
	 */
	public static final String SYS_CALENDAR_IS_WRITER = "CalendarIsWriter";
	/**
	 * * 日程模块读写类型
	*/
	public static final String SYS_CALENDAR_TYPE_PURVIEW = "CalendarIsPurviewType";
	/**
	 * 读写用户权限
	 */
	public static final String SYS_CALENDAR_USER_PURVIEW = "CalendarUserPurview";
	
	
	
	
	// Fields

	private Long id;
	private String userid;
	private String type;
	private String value;

	// Constructors

	/** default constructor */
	public SysPersonConfig() {
	}

	/** full constructor */
	public SysPersonConfig(String userid, String type, String value) {
		this.userid = userid;
		this.type = type;
		this.value = value;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
