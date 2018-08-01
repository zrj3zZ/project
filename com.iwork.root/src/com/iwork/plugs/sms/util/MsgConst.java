package com.iwork.plugs.sms.util;


import java.util.HashMap;

/**
 * @author lee 定义短信通用常量
 */
public class MsgConst {
	// 发送短信开关，true=发送
	public static final boolean SEND_ON = true;

	// 号码簿上传文件目录
	public static final String PHONEBOOK_UPLOAD_ROOTDIR = "MsgPhonebookFile";

	// 参数类型
	// 敏感词
	public static final String TYPE_FILTER_WORD = "FILTER_WORD";
	// 电信运营商
	public static final String TYPE_MOBILE_SP = "MOBILE_SP";
	// 号段
	public static final String TYPE_MOBILE_NUM_SECTION = "MOBILE_NUM_SECTION";
	// 日志类型
	public static final String TYPE_LOG_TYPE = "LOG_TYPE";
	// 短信状态
	public static final String TYPE_MSG_STATUS = "MSG_STATUS";
	// 默认通道
	public static final String TYPE_DEFAULT_CHANNEL = "DEFAULT_CHANNEL";

	// 通配符正则
	// %name%
	public static final String REGEX_NAME = "\\%name\\%";
	// %attr1%
	public static final String REGEX_ATTR1 = "\\%attr1\\%";
	// %attr2%
	public static final String REGEX_ATTR2 = "\\%attr2\\%";
	// %attr3%
	public static final String REGEX_ATTR3 = "\\%attr3\\%";

	// 电信运营商最大ID
	public static final int MOBILE_SP_MAX_ID = 3;

	// 短信发送状态
	public static final int SEND_STATUS_OTHER = 0;
	public static final int SEND_STATUS_READY = 1;
	public static final int SEND_STATUS_OK = 2;
	public static final int SEND_STATUS_INVALID_NUM = 3;
	public static final int SEND_STATUS_INVALID_CONTENT = 4;
	public static final int SEND_STATUS_NO_MONEY = 5;
	public static final int SEND_STATUS_OTHER_ERR = 6;
	public static final int SEND_STATUS_API_ERR = 7;
	public static final int SEND_STATUS_EXCEED_LIMIT = 8;
	public static final int SEND_STATUS_NO_SERVICE = 9;

	// 短信发送信息
	public static final String SEND_INFO_OK = "发送成功";
	public static final String SEND_INFO_INVALID_PARAM = "参数无效";
	public static final String SEND_INFO_INVALID_USER = "用户验证没有通过";
	public static final String SEND_INFO_PERMISSION_DENY = "验证没通过";
	public static final String SEND_INFO_NO_MONEY = "资费不足";
	public static final String SEND_INFO_INVALID_SERVICE = "指定的业务尚未开通";
	public static final String SEND_INFO_INVALID_NUM = "非法的接收用户";
	public static final String SEND_INFO_EXIST_FILTER_WORD = "短信息内容中含有非法信息";
	public static final String SEND_INFO_INVALID_CONTENT = "内容非法";
	public static final String SEND_INFO_EXCEED_LIMIT = "发送限额已满";
	public static final String SEND_INFO_DB_ERR = "数据库操作错误";
	public static final String SEND_INFO_OTHER_ERR = "其他错误";

	public static final String SEND_FLAG_OK = "";

	// 日志类型
	public static final int LOG_TYPE_OTHER = 0;
	public static final int LOG_TYPE_SEND_ERR = 1;
	public static final int LOG_TYPE_USER_MANAGE = 2;
	public static final int LOG_TYPE_MSG_MANAGE = 3;
	public static final int LOG_TYPE_MSG_CONFIG = 4;
	public static final int LOG_TYPE_MSG_MSGSP = 5;
	public static final int LOG_TYPE_MSG_CHANNEL = 6;
	public static final int LOG_TYPE_ALL = -1;

	public static final HashMap<String, Integer> LOG_TYPE_HASHMAP = new HashMap<String, Integer>() {
		{
			put("LOG_TYPE1", LOG_TYPE_SEND_ERR);
			put("LOG_TYPE2", LOG_TYPE_USER_MANAGE);
			put("LOG_TYPE3", LOG_TYPE_MSG_MANAGE);
			put("LOG_TYPE4", LOG_TYPE_MSG_CONFIG);
			//put("LOG_TYPE5", LOG_TYPE_MSG_MSGSP);
			//put("LOG_TYPE6", LOG_TYPE_MSG_CHANNEL);
			put("LOG_TYPE0", LOG_TYPE_OTHER);
			put("LOG_TYPE_ALL", LOG_TYPE_ALL);
		}
	};

	// 地域
	public static final String AREA_BJ = "北京";
	public static final String AREA_ZH = "珠海";
	public static final String AREA_CD = "成都";
	public static final String AREA_DL = "大连";
	
	// 系统参数
	public static final HashMap<String, String> hsdb = new HashMap<String, String>() {
		{
			put("MOBILE_SP_NAME", "电信运营商");
			put("MOBILE_NUM_SECTION_NAME", "号段");
			put("FILTER_WORD_NAME", "敏感词");
			put("LOG_TYPE_NAME", "日志类型");
			put("MSG_STATUS_NAME", "短信状态");
			put("DEFAULT_CHANNEL_NAME", "默认通道");
		}
	};
	
	public static final String DEFAULT_CONTENT = "支持通配符：\n姓名 %name%\n属性1 %attr1%（仅限号码簿中手机号）\n属性2 %attr2%（仅限号码簿中手机号）\n属性3 %attr3%（仅限号码簿中手机号）";
	public static final String DEFAULT_NUM = "逗号分隔多个号码，姓名可省略。\n\n示例：13811111111[张三]，13611111111";
	
	public static final String DEFAULT_PHONEBOOK_GROUP_NAME = "未分组";
	
	//发送短信内容最大长度
	public static int MAX_MSG_LEN = 200;
	//发送短信号码最大数量
	public static int MAX_MSG_NUM_COUNT = 200;
	
	//update标志
	public static String FLAG_UPDATE = "UPDATE";
	
	//超长错误信息
	public static String ERR_INFO_500 = "错误编号：500参数无效";
	public static String ERR_INFO_TOO_LONG = "短信超长";
}
