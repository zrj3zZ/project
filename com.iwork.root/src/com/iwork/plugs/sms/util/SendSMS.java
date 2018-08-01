package com.iwork.plugs.sms.util;


import com.iwork.plugs.sms.util.interfaces.SMS;
import com.iwork.plugs.sms.util.model.ReturnModel;
import com.iwork.plugs.sms.util.model.SmsPropertiesModel;
import com.mysql.jdbc.Util;



public class SendSMS {

	private SMS sms;

	public SMS getSms() {
		return sms;
	}

	public void setSms(SMS sms) {
		this.sms = sms;
	}
	/**
	 * 
	 * @param msg 短信内容
	 * @param phoneNo 手机号码
	 * @param successtitle 发送成功时返回的标题
	 * @param model 平台用户名密码等属性
	 * @return
	 */
	public ReturnModel sendSMS(String msg, String phoneNo,String successtitle,SmsPropertiesModel model){
		Util util = new Util();
		return sms.sendSMS(msg, phoneNo,successtitle,model);
	}
}
