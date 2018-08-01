package com.iwork.commons.util;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import org.apache.log4j.Logger;
import cjscpool2.ASClient2;
import cjscpool2.ASClientFactory2;

public class CjSendMessgeUtil {
	private static Logger logger = Logger.getLogger(CjSendMessgeUtil.class);
	public static void ASClentLongConnectionSendSMS(String sender,String toNumber,String content){
		ASClient2 client = ASClientFactory2.getASClient();
		if (client != null) {
			client.setHead(10, 140235);
			client.setRange(8);
			client.setField("i_channel_type");
			client.setField("i_app_id");
			client.setField("i_mess_id");
			client.setField("i_mess_destaddr");
			client.setField("i_mess_subject");
			client.setField("i_mess_content");
			client.setField("i_mess_operator");
			client.setField("i_mess_sender_name");
			client.setValue("1"); // 设置渠道类型。
			client.setValue("10102"); // 设置应用的类型。
			client.setValue("10102"); // 设置信息的类型。
			client.setValue(toNumber); // 设置收短信地址
			client.setValue(""); // 发送短信时，此处不必设置。
			client.setValue(content); // 设置短信内容
			client.setValue("T_xsbcxdd"); // 设置为OA用户名
			client.setValue("");// 设置落款单位名称
			try {
				client.sendReceive();
				// 发送失败
				if (client.getErrorNo() != 0) {
				}
				// 发送成功
				else {
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
			client.freePack();
			client.disConnect();
		}
	}
	
	public static int ASClientShortConnectionSendSMS(String toNumber,String content){
		int num = -1;
		ASClient2 client = null;
		// 建立到长江中间件的连接
		client = new ASClient2();
		if (client != null) {
			// 设置要调用的功能号
			client.setHead(10, 140235);
			client.setRange(8);
			client.setField("i_channel_type");
			client.setField("i_app_id");
			client.setField("i_mess_id");
			client.setField("i_mess_destaddr");
			client.setField("i_mess_subject");
			client.setField("i_mess_content");
			client.setField("i_mess_operator");
			client.setField("i_mess_sender_name");
			client.setValue("1"); // 设置渠道类型。
			client.setValue("10102"); // 设置应用的类型。
			client.setValue("10102"); // 设置信息的类型。
			client.setValue(toNumber); // 设置收短信地址
			client.setValue(""); // 发送短信时，此处不必设置。
			client.setValue(content); // 设置短信内容
			client.setValue("T_xsbcxdd"); // 设置为OA用户名
			client.setValue("");// 设置落款单位名称
			try {
				client.sendReceive();
				// 发送失败
				if (client.getErrorNo() != 0) {
				}
				// 发送成功
				else {
					num=0;
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
			// 打印所有的返回结果
			//println(client);
			client.close();
		}
		return num;
	}
	
	public static void ASClentLongConnectionSendEmail(String fromTitle,String mailAddress,String title, String content){
		UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = userContext.get_userModel().getUserid();
		ASClient2 client = ASClientFactory2.getASClient();
		if (client != null) {
			client.setHead(10, 140235);
			client.setRange(8);
			client.setField("i_channel_type");
			client.setField("i_app_id");
			client.setField("i_mess_id");
			client.setField("i_mess_destaddr");
			client.setField("i_mess_subject");
			client.setField("i_mess_content");
			client.setField("i_mess_operator");
			client.setField("i_mess_sender_name");
			client.setValue("2"); //设置渠道类型。
			client.setValue("2000"); //设置应用的类型。
			client.setValue("2000"); //设置信息的类型。
			client.setValue(mailAddress); //设置收件人地址
			client.setValue(fromTitle); //设置邮件标题
			client.setValue(content); //设置邮件内容
			client.setValue(userid); //设置操作--为 OA用户名
			client.setValue("");//发送邮件的话，不必设置落款单位名称
			try {
				client.sendReceive();
				// 发送失败
				if (client.getErrorNo() != 0) {
				}
				// 发送成功
				else {
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
			client.freePack();
			client.disConnect();
		}
	}
	
	public static void ASClientShortConnectionSendEmail(String fromTitle,String mailAddress,String title, String content){
		UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = userContext.get_userModel().getUserid();
		ASClient2 client = null;
		// 建立到长江中间件的连接
		client = new ASClient2();
		if (client != null) {
			// 设置要调用的功能号
			client.setHead(10, 140235);
			client.setRange(8);
			client.setField("i_channel_type");
			client.setField("i_app_id");
			client.setField("i_mess_id");
			client.setField("i_mess_destaddr");
			client.setField("i_mess_subject");
			client.setField("i_mess_content");
			client.setField("i_mess_operator");
			client.setField("i_mess_sender_name");
			client.setValue("2"); //设置渠道类型。
			client.setValue("2000"); //设置应用的类型。
			client.setValue("2000"); //设置信息的类型。
			client.setValue(mailAddress); //设置收件人地址
			client.setValue(fromTitle); //设置邮件标题
			client.setValue(content); //设置邮件内容
			client.setValue(userid); //设置操作--为 OA用户名
			client.setValue("");//发送邮件的话，不必设置落款单位名称
			try {
				client.sendReceive();
				// 发送失败
				if (client.getErrorNo() != 0) {
				}
				// 发送成功
				else {
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
			// 打印所有的返回结果
			//println(client);
			client.close();
		}
	}
	
	/*private static void println(ASClient2 client) {
				+ client.getErrorMsg() + " content=" + client.getBuffer());
		while (client.next()) {
			for (short i = 0; i < client.getFieldNum(); i = (short) (i + 1)) {
				String field = client.getFieldName(i);
				String value = client.fieldByName(field);
			}
		}
	}*/
}
