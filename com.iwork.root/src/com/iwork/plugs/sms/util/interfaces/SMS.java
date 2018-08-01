package com.iwork.plugs.sms.util.interfaces;

import com.iwork.plugs.sms.util.model.ReturnModel;
import com.iwork.plugs.sms.util.model.SmsPropertiesModel;





public interface SMS {

	/**
	 * 发送短信接口类
	 * @param msg 短信内容
	 * @param phoneNo 手机号码
	 * @param successtitle 发送成功时返回的标题,失败则返回出错编号
	 * @param model 平台用户名密码等属性
	 * @return
	 */
	public ReturnModel sendSMS(String msg,String phoneNo,String successtitle,SmsPropertiesModel model);
}
