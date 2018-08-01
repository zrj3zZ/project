package com.iwork.plugs.sms.util;

import com.iwork.plugs.sms.util.interfaces.SMS;
import com.iwork.plugs.sms.util.model.PlatformModel;
import com.iwork.plugs.sms.util.model.ReturnModel;
import com.iwork.plugs.sms.util.model.SmsPropertiesModel;






public class SendSMSFactory {

	/**
	 * 发送短信工厂类
	 * @param functionkey 功能模块名称，用于配置文件取登录的短信平台
	 * @param msg 短信内容
	 * @param phoneNo 手机号码
	 * @param successtitle 发送成功时返回的标题
	 * @return
	 */
	public ReturnModel factory(String functionkey,String msg, String phoneNo,String successtitle){
		SMS sms;
		SendSMS send = new SendSMS();
		//平台类型
		PlatformModel model = new PlatformModel();
		//平台用户名密码等属性
		SmsPropertiesModel promodel = new SmsPropertiesModel();
		//返回值
		ReturnModel rmodel = null;
		Util util = new Util();
		//根据功能名取平台名称
		String platform="TRRT";
		//根据平台名称取该平台的用户名及相关属性
		String username = "jinshan";
		String pwd = "kingsoft2010";
		promodel.setUser(username);
		promodel.setPwd(pwd);
		if(model.getPLATFORM_TRRT().equals(platform)){
			sms = new SMS_TRRT();
		}else if(model.getPLATFORM_DSF().equals(platform)){
			String extend1 = "0";
			promodel.setExtend1(extend1);
			sms = new SMS_DSF();
		}else if(model.getPLATFORM_AR().equals(platform)){
			sms = new SMS_AR();
		}else{
			sms = new SMS_TRRT();
			return rmodel;
		}
		send.setSms(sms);
		rmodel=send.sendSMS(msg, phoneNo,successtitle,promodel);
		return rmodel;
	}
}
