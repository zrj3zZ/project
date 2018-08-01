package com.iwork.plugs.sms.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.iwork.plugs.sms.util.interfaces.SMS;
import com.iwork.plugs.sms.util.model.ReturnModel;
import com.iwork.plugs.sms.util.model.SmsPropertiesModel;
import com.iwork.plugs.sms.util.model.returnValue.ARReturnValue;
import org.apache.log4j.Logger;




/**
 * 奥软短信平台实现类
 * @author WangRongtao
 *
 */
public class SMS_AR implements SMS{
	private static Logger logger = Logger.getLogger(SMS_AR.class);
	/**
	 * 实现发送短信方法
	 * @param msg 短信内容
	 * @param phoneNo 手机号码
	 * @param successtitle 发送成功时返回的标题,失败则返回出错编号
	 * @param model 平台用户名密码等属性
	 * @return
	 */
	public ReturnModel sendSMS(String msg, String phoneNo,String successtitle,SmsPropertiesModel model) {
		ReturnModel rmodel = new ReturnModel();
		URLConnection conn;
		StringBuilder sb = new StringBuilder();
		String urlstr = "http://211.147.222.37/sendsms/?username="+model.getUser()+"&pwd="+model.getPwd()+"&msg="+msg+"&action=sendmsg&tmobile="+phoneNo;   
		try { 
			URL url=new URL(urlstr);
			conn=url.openConnection(); 
			conn.setDoOutput(true);
			BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));   
			String line=null;
			while((line=reader.readLine())!=null){   
			      sb.append(line);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		ARReturnValue ar = new ARReturnValue();
		String flag = "false";
		String key = sb.toString();
		if(key!=null&&!"".equals(key)&&Integer.parseInt(key)==1){
			flag = "true";
		}else{
			flag = "false";
		}
		rmodel.setReturnkey(key);
		rmodel.setReturnvalue(ar.returnValue(key,successtitle));
		rmodel.setIssuccess(flag);
		rmodel.setPlatform("梦搜");
		return rmodel;
	}

}
