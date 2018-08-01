package com.iwork.plugs.email.constant;

public interface BoxTypeConst {

	public static Long TYPE_READER = new Long(2);//
	public static Long TYPE_DRAFT_MAIL = new Long(1);// 撰写新邮件
	public static Long TYPE_DRAFT = new Long(0);// 草稿箱
	public static Long TYPE_SEND = new Long(-1);// 发件箱
	public static Long TYPE_TRANSACT =new Long( -2);// 收件箱
	public static Long TYPE_DELETED = new Long(-3);// 垃圾箱
	public static Long TYPE_SEARCH = new Long(-4);//
	public static Long TYPE_NOTEBOOK = new Long(-5);//
	public static Long TYPE_ADDRESS = new Long(-6);//
	public static Long TYPE_SETTING = new Long(-7);// 邮箱设置
	public static Long TYPE_MODEL = new Long(-8);// 常用模板
	public static Long TYPE_FILTER = new Long(-9);// 邮件黑名单
	public static Long TYPE_IMPORTANT = new Long(-10);// 标记为重要的
	public static Long TYPE_JUNK = new Long(-11);// 垃圾邮件
	public static Long TYPE_MANAGEFOLDER = new Long(-12);// 管理文件夹
	public static Long TYPE_ALL = new Long(-99);//
	
	public static Long IS_SEND_YES = new Long(1);     // 已发送
	public static Long IS_SEND_NO = new Long(0);      // 未发送
	public static Long IS_IMPORTANT_YES = new Long(1);// 重要邮件
	public static Long IS_IMPORTANT_NO = new Long(0); // 非重要邮件
	public static Long IS_DEL_YES = new Long(1);      // 已删除
	public static Long IS_DEL_NO = new Long(0);       // 未删除
	public static Long IS_STAR_YES = new Long(1);     // 标星
	public static Long IS_STAR_NO = new Long(0);      // 未标星
	public static Long IS_ARCHIVES_YES = new Long(1); // 已存档
	public static Long IS_ARCHIVES_NO = new Long(0);  // 未存档
	public static Long IS_RE_YES = new Long(1);       // 已回复
	public static Long IS_RE_NO = new Long(0);        // 未回复
	public static Long IS_READ_YES = new Long(1);     // 已读
	public static Long IS_READ_NO = new Long(0);      // 未读
	
}
