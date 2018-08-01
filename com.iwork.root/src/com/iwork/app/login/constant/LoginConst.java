package com.iwork.app.login.constant;


/**
 * Login常量
 *
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public interface LoginConst {
	
	/**
	 * 通过web登录 
	 */
	public static final String superinfo = "aqgOd21IS2rsHiT2sF";
	public static final Long LOGIN_TYPE_WEB = new Long(1);
	/**
	 * 通过移动客户端登录
	 */
	public static final Long LOGIN_TYPE_MOBILE = new Long(2); 
	/**
	 * 管理员登录后台
	 */
	public static final Long LOGIN_TYPE_ADMIN = new Long(3);
	/**
	 * 微门户登录
	 */
	public static final Long LOGIN_TYPE_WEIPORTAL  = new Long(4);
	
	
	
	
	/**
	 * 用户登录访问类型
	 */
	public static final String USER_LOGIN_TYPE = "LOGIN_TYPE";
	/**
	 * web方式访问
	 */
	public static final Long USER_LOGIN_TYPE_WEB = new Long(201); 
	/**
	 * 安卓访问
	 */
	public static final Long USER_LOGIN_TYPE_MOBILE_ANDROID =  new Long(202); 
	/**
	 * ios访问
	 */
	public static final Long USER_LOGIN_TYPE_MOBILE_IOS =  new Long(203); 
	/**
	 * 微信访问
	 */
	public static final Long USER_LOGIN_TYPE_MOBILE_WEIXIN =  new Long(205); 
	/**
	 * windows8访问
	 */
	public static final Long USER_LOGIN_TYPE_MOBILE_WIN = new Long(204); 
	
	
	
    /**
     * 成功，可以进入系统
     *
     * @preserve 声明此方法不被JOC混淆
     */
    public static int LOGIN_STATUS_OK = 1;
    /**
     * 用户不存在
     *
     * @preserve 声明此方法不被JOC混淆
     */
    public static int LOGIN_STATUS_USER_NOTFIND = -9;
    /**
     * 口令不正确
     *
     * @preserve 声明此方法不被JOC混淆
     */
    public static int LOGIN_STATUS_PWD_ERROR = -8;

    /**
     * 帐户已经被注销
     *
     *  @preserve 声明此方法不被JOC混淆
     */
    public static int LOGIN_STATUS_DISENABLE = -7;

    /**
     * 帐户含有非法字符
     *
     * @preserve 声明此方法不被JOC混淆
     */
    public static int LOGIN_STATUS_UID_VALIADATA = -6;

    /**
     * 内部错误
     *
     *@preserve 声明此方法不被JOC混淆
     */
    public static int LOGIN_STATUS_INNER_ERROR = -5;


    /**
     * 数据库连接失败
     *
     * @preserve 声明此方法不被JOC混淆
     */
    public static int LOGIN_STATUS_DATABASE_ERROR = -999;
    
    /**
     * 不在访问列表
     */
    public static int LOGIN_ACCESS_USER_LOGIN = -10;
   
   /**
    * 禁止访问
    */
    public static int LOGIN_ACCESS_USER_VISIT = -11;
    
    /**
     * 不是绑定设备
     */
    public static int LOGIN_ACCESS_USER_BIND = -12;
     
     /**
      * 令牌错误
      */
     public static int LOGIN_ACCESS_USER_ANMENG = -13;
    
}
