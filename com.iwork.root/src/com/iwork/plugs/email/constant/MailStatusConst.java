

package com.iwork.plugs.email.constant;

public interface MailStatusConst {



	//成功
	public final static String OK="OK";
	
	//地址不正确或不存在的系统帐户
	public final static String ADDRESS_ERROR="ERROR-0700";
	
	//数据库链接错误
	public final static String DB_CONNECT_ERROR="ERROR-0303";
	
	//增加记录时错误
	public final static String DB_CREATE_ERROR="ERROR-0602";
	
	//修改记录时错误
	public final static String DB_MODIFY_ERROR="ERROR-0601";
	
	//系统异常
	public final static String SYSTEM_ERROR="ERROR-0803";
	
	//收件人不能为空
	public final static String TO_NOTNULL_ERROR="ERROR-0801";
	
	//邮件标题不能为空
	public final static String TITLE_NOTNULL_ERROR="ERROR-0802";
	
	//向外发送失败
	public final static String WWW_EMAIL_SEND_ERROR="ERROR-0805"; 
	
	//没有配置个人外部邮箱或邮箱口令
	public final static String WWW_EMAIL_INIT_ERROR="ERROR-0806";
	
	
}
