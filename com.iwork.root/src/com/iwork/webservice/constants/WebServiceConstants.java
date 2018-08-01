package com.iwork.webservice.constants;

public class WebServiceConstants {
	/**
	 * 输入参数
	 */
	public static final String WS_MODEL_ID_SEQUENCT_KEY = "WebServiceConstants-WS_MODEL_ID_SEQUENCT_KEY";
	
	/**
	 * 输入参数
	 */
	public static final String WS_PARAMS_TYPE_INPUT = "input";
	
	/**
	 * 输出参数
	 */
	public static final String WS_PARAMS_TYPE_OUTPUT = "output";
	
	/**
	 * 用户名key
	 */
	public static final String KEY_USER_NAME = "userName";
	
	/**
	 * 密码key
	 */
	public static final String KEY_USER_PWD = "userPwd";
	
	/**
	 * 触发器key
	 */
	public static final String KEY_HANDER_CODE = "handerCode";
	
	/**
	 * 客户端配置UUID
	 */
	public static final String KEY_UUID = "UUID";
	
	/**
	 * 客户端配置参数内容
	 */
	public static final String KEY_PARAM_CONTENT = "content";
	
	/**
	 * SysWsBaseinfo
	 */
	public static final String CACHE_PERFIX_SYSWSBASEINFO = "CACHE_PERFIX_SYSWSBASEINFO";
	
	/**
	 * 默认缓存时间
	 */
	public static int CACHE_HOUR = 24;
	
	/**
	 * 成功
	 */
	public static int SUCCESS = 1;
	
	/**
	 * 失败
	 */
	public static int ERROR = 0;
	
	/**
	 * 布尔型，是
	 */
	public static int YES = 1;
	
	/**
	 * 布尔型，否
	 */
	public static int NO = 0;
	
	/**
	 * 通用WebService URL
	 */
	public static final String COMMON_WEB_SERVICE_URL = "/services/common";
	
	/**
	 * 类型
	 */
	public static final String WS_TYPE_COMMON = "1";
	
	/**
	 * 类型
	 */
	public static final String WS_TYPE_DIY = "2";
	
	/**
	 * 参数结构
	 */
	public static final String WS_FIELD_TYPE_STRUCTURE = "STRUCTURE";
	
	/**
	 * 字符
	 */
	public static final String WS_FIELD_TYPE_CHAR = "CHAR";

	/**
	 * 数值
	 */
	public static final String WS_FIELD_TYPE_NUM = "NUM";
	
	/**
	 * 数值,13位，小数点后保留三位
	 */
	public static final String WS_FIELD_TYPE_BCD = "BCD";
	
	/**
	 * 日期
	 */
	public static final String WS_FIELD_TYPE_DATE = "DATE";
	
	/**
	 * 验证方式 IP
	 */
	public static final String WS_CHECK_TYPE_IP = "1";
	
	/**
	 * 验证方式 用户名密码
	 */
	public static final String WS_CHECK_TYPE_PWD = "2";

	/**
	 * 输出参数
	 */
	public static final String FAIL = "output";
	
	/**
	 * 通用WebService参数个数
	 */
	public static int WS_COMMON_WEBSERVICE_EXECUTE_NUM = 4;
	
	/**
	 * 出错信息
	 */
	public static final String CONST_MESSAGE_ERROR_101 = "SOAP内容格式不正确 ";
	public static final String CONST_MESSAGE_ERROR_102 = "缺少UUID参数，或未配置相关的WebService管理信息 ";
	public static final String CONST_MESSAGE_ERROR_103 = "IP地址不在白名单中，禁止访问 ";
	public static final String CONST_MESSAGE_ERROR_104 = "IP地址在黑名单中，禁止访问 ";
	public static final String CONST_MESSAGE_ERROR_105 = "用户名或密码错误，禁止访问 ";
	public static final String CONST_MESSAGE_ERROR_106 = "参数格式错误 ";
	public static final String CONST_MESSAGE_ERROR_107 = "接口类异常 ";
	public static final String CONST_MESSAGE_ERROR_108 = "参数异常 ";
	public static final String CONST_MESSAGE_ERROR_109 = "接口已关闭 ";
	public static final String CONST_MESSAGE_ERROR_110 = "缺少UUID参数 ";
	public static final String CONST_MESSAGE_ERROR_111 = "缺少userName参数 ";
	public static final String CONST_MESSAGE_ERROR_112 = "缺少userPwd参数 ";
	public static final String CONST_MESSAGE_ERROR_113 = "错误的SoapHeader信息 ";
	
	/**
	 * 必填项
	 */
	public static final String CONST_MESSAGE_REQUIRED = "必填项 ";
	
	
	
	
	
	
	
	
	/**
	 * 系统集成组件类型-SAP组件
	 * 
	 */
	public static final String WS_PLUGS_TYPE_SAP = "sap";
	public static final String WS_PLUGS_TYPE_webservice_FOR_SAP = "sapWebservice";
	
	/**
	 * webservice组件
	 */
	public static final String WS_PLUGS_TYPE_WEBSERVICE = "webservice";
	
	public static final String WS_EXECUTE_RETURN_LOG_KEY = "WS_EXECUTE_LOG";
	
}
