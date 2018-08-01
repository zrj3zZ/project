
package com.iwork.plugs.email.constant;

public interface MailTypeConst {

	/**
	 * 内部邮件
	 */
	public static int Mail_TYPE_INNER_MAIL = 0;

	/**
	 * 内部系统级邮件
	 */
	public static int Mail_TYPE_INNER_WORK = 1;
	
	/**
	 * 常用个人邮件模板
	 */
	public static int Mail_TYPE_MODEL = 6;

	/**
	 * 互联网邮件
	 */
	public static int Mail_TYPE_OUTER_MAIL = 9;
	
	
	/**
	 * 邮件用户名
	 */
	public static String MAIL_PERSION_CONFIG_USERNAME = "MAIL_PERSION_USERNAME";
	/**
	 * 邮件密码
	 */
	public static String MAIL_PERSION_CONFIG_PWD = "MAIL_PERSION_PWD";
	
	/**
	 * 执行转发动作
	 */
	public static final Long MAIL_ACTION_TYPE_ADD = new Long(1);
	public static final Long MAIL_ACTION_TYPE_FORWARD = new Long(2);
	public static final Long MAIL_ACTION_TYPE_REPLY = new Long(3);
	public static final Long MAIL_ACTION_TYPE_REPLYALL = new Long(4);
}