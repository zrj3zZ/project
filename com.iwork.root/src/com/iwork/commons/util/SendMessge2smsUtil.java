package com.iwork.commons.util;

import java.util.Date;
import org.apache.log4j.Logger;

import org.tempuri.Service1;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.util.MD5;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.sms.bean.MsgMst;
import com.iwork.plugs.sms.dao.MsgDao;


public class SendMessge2smsUtil {
	private static Logger logger = Logger.getLogger(SendMessge2smsUtil.class);
	private static MsgDao msgDao;
	/**
	 * 发送短信
	 * @param sender
	 * @param receiver
	 * @param info
	 */
	public static String sendSMSforHttp(String name,String sender, String toNumber, String content) {
		// TODO Auto-generated method stub
		String info = "";
		String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer");
		Date stringToDate = UtilDate.StringToDate(UtilDate.getNowDatetime(),"yyyy-MM-dd HH:mm:ss");
		int num = 0;
		String sign = "";
		if(configParameter!=null&&configParameter.equals("htzq")){
			try{
				num = SmsSendHtUtil.sendSMSforHttp(toNumber, content);
			}
			catch(Exception e){
				logger.error(e,e);
			}
			num=num==0?1:0;//遵照短信之前代码约定，进行判断是否正确发送短信
		}else if(configParameter!=null&&configParameter.equals("cjzq")){
			try{
				num = CjSendMessgeUtil.ASClientShortConnectionSendSMS(toNumber,content);
			}
			catch(Exception e){
				logger.error(e,e);
			}
		}else if(configParameter!=null&&configParameter.equals("dgzq")){			
			num = SmsSendHtUtil.sendSMSDg(toNumber, content);
			sign=String.valueOf(num);
			//0成功，1失败
			num=num==100?0:1;
		}else if(configParameter!=null&&configParameter.equals("xnzq")){			
			num = SmsSendHtUtil.sendSMSXn(toNumber, content);
			sign=String.valueOf(num);
			//0成功，1失败
		}else{
			try{
				String username = SystemConfig._smsConf.getSn();
				String password =SystemConfig._smsConf.getPwd();
				MD5 md5 = new MD5();
				String md5pwd = md5.getEncryptedPwd(username+password);
				Service1 service = new Service1();
				num = service.getBasicHttpBindingIService1().smsSend(username, password, md5pwd,toNumber,content);
			}catch(Exception e){
				logger.error(e,e);
			}
		}
		if(msgDao==null){
			msgDao = (MsgDao)SpringBeanUtil.getBean("msgDao");
		}
		if(num==0){//发送成功
			MsgMst mmst=new MsgMst();
			mmst.setUserid(sender);
			mmst.setMobilenum(toNumber);
			mmst.setContent(content);
			mmst.setStatus(1);//发送成功
			mmst.setSendtime(stringToDate);
			mmst.setSubmittime(stringToDate);
			mmst.setPathname(name);
			mmst.setSign(sign);
			msgDao.save(mmst);
			info="success";
		}else{//发送失败
			MsgMst mmst=new MsgMst();
			mmst.setUserid(sender);
			mmst.setMobilenum(toNumber);
			mmst.setContent(content);
			mmst.setStatus(0);
			mmst.setSendtime(stringToDate);
			mmst.setSubmittime(stringToDate);
			mmst.setPathname(name);
			mmst.setSign(sign);
			msgDao.save(mmst);
			info="error";
		}
		return info;
	}
}
