package com.iwork.sdk;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.dao.MailWithAttachmentDAO;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.message.sysmsg.dao.SysMessageDAO;
import com.iwork.app.message.sysmsg.model.SysMessage;
import com.iwork.app.weixin.core.tools.SendMessageUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.SendMessge2RtxUtil;
import com.iwork.commons.util.SendMessge2smsUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.server.email.tools.SendMail;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.sms.bean.MailLog;
/**
 * 消息发送API
 * @author YangDayong
 *
 */
public class MessageAPI {
	private static Logger logger = Logger.getLogger(MessageAPI.class);
	 private static MailWithAttachmentDAO mailWithAttachmentDAO;
	 private static MessageAPI instance;  
	 private static SysMessageDAO sysMessageDAO;  
     private static Object lock = new Object();  
	 public static MessageAPI getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new MessageAPI();  
	                }
	            }  
	        }  
	        return instance;  
	 }	 
	 /**
		 * 发送微信
		 * @param userlist
		 * @param content
		 * @return
		 */
		public boolean sendWeiXin(String userlist,String content){
			boolean flag = false;
			if(SystemConfig._weixinConf.getServer().equals("on")){
				flag = SendMessageUtil.getInstance().sendTextMessage(userlist, content);
			}
			return flag;
		}
	/**
	 * 发送系统邮件
	 * @param fromTitle  显示来自标题
	 * @param mailAddress 多个邮件地址已逗号分割
	 * @param title
	 * @param content
	 * @return 
	 */
	public boolean sendSysMail(String fromTitle,String mailAddress,String title,String content){
		//判断是否是调试状态
		boolean flag = false;
		try
		{
			if(SystemConfig._mailServerConf.getIsDebug().equals("on")){
				mailAddress = SystemConfig._mailServerConf.getDebug_address();
			} 			
			if (SystemConfig._mailServerConf.getSmtp_services().equals("on")) {
				class SendMailThread extends Thread {
					private String fromTitle;
					private String toMailAddress;
					private String title;
					private String content;
					SendMailThread(String fromTitle,String toMailAddress, String title, String content) {
						this.fromTitle = fromTitle; 
						this.toMailAddress = toMailAddress;
						this.title = title;
						this.content = content;
					}
		
					public void run() { 
						SendMail sm = new SendMail();
						sm.setTo(toMailAddress);
						sm.setBody(content); 
						sm.setFrom(fromTitle, SystemConfig._mailServerConf.getMailFrom());
						sm.setTo(toMailAddress); 
						sm.setSubject(title);
						sm.send();
					}	
				} 
				new SendMailThread(fromTitle,mailAddress,title, content).start();
				flag = true;
			}
			
			//当前发送人的id
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String userid = uc.get_userModel().getUserid();
			//邮件接收人的名称
			Map params = new HashMap();
			params.put(1,mailAddress);
			String username =DBUTilNew.getDataStr("USERNAME","SELECT USERNAME FROM ORGUSER WHERE trim(EMAIL) = trim( ? )", params);
			if(mailWithAttachmentDAO==null){
				mailWithAttachmentDAO = (MailWithAttachmentDAO)SpringBeanUtil.getBean("mailWithAttachmentDAO");
			 }
			Date stringToDate = UtilDate.StringToDate(UtilDate.getNowDatetime(),"yyyy-MM-dd HH:mm:ss");
			if(flag){
				MailLog ml=new MailLog();
				ml.setContent(content);
				ml.setEmail(mailAddress);
				ml.setTitle(title);
				ml.setUserid(userid);
				ml.setName(username);
				ml.setStatus(1);
				ml.setSendtime(stringToDate);
				ml.setSubmittime(stringToDate);
				mailWithAttachmentDAO.addBoData(ml);
			}else{
				MailLog ml=new MailLog();
				ml.setContent(content);
				ml.setEmail(mailAddress);
				ml.setTitle(title);
				ml.setUserid(userid);
				ml.setName(username);
				ml.setStatus(0);
				ml.setSendtime(stringToDate);
				ml.setSubmittime(stringToDate);
				mailWithAttachmentDAO.addBoData(ml);
			}		
		}catch(Exception e)
		{
			logger.error(e,e);
			return false;			
		}
		return flag;
	}
	
	/**
     * 添加系统消息
     * @param userid
     * @param type
     * @param title
     * @param content
     * @param url
     * @param priority
     */
    public void sendSysMsg(String userid,String type,String title,String content,String url,int priority){
    	if(sysMessageDAO==null){
        	sysMessageDAO = (SysMessageDAO)SpringBeanUtil.getBean("sysMessageDAO");
        }
    	SysMessage sysMsg = new SysMessage();
    	sysMsg.setTitle(title);
    	sysMsg.setPriority(priority);
    	sysMsg.setStatus(SysMessage.MSG_STATUS_READ_NO);
    	sysMsg.setReceiver(userid.toUpperCase());
    	sysMsg.setUrl(url);
    	sysMessageDAO.createSysMessage(sysMsg);
    	
    }
    /**
     * 添加系统消息
     * @param userid
     * @param title
     * @param content
     * @param url
     */
    public void sendSysMsg(String userid,String title,String content,String url){
    	if(sysMessageDAO==null){
        	sysMessageDAO = (SysMessageDAO)SpringBeanUtil.getBean("sysMessageDAO");
        }
    	SysMessage sysMsg = new SysMessage();
    	sysMsg.setType(SysMessage.MSG_TYPE_SYSTEM);
    	sysMsg.setPriority(SysMessage.MSG_PRIORITY_MED);
    	sysMsg.setStatus(SysMessage.MSG_STATUS_READ_NO);
    	sysMsg.setTitle(title);
    	sysMsg.setContent(content);
    	sysMsg.setReceiver(userid.toUpperCase());
    	sysMsg.setUrl(url);
    	sysMessageDAO.createSysMessage(sysMsg);
    }
    /**
     * 添加系统消息
     * @param userid
     * @param title
     * @param content
     */
    public void sendSysMsg(String userid,String title,String content){
    	if(sysMessageDAO==null){
        	sysMessageDAO = (SysMessageDAO)SpringBeanUtil.getBean("sysMessageDAO");
        }
    	SysMessage sysMsg = new SysMessage();
    	sysMsg.setTitle(title);
    	sysMsg.setContent(content);
    	sysMsg.setPriority(SysMessage.MSG_PRIORITY_MED);
    	sysMsg.setStatus(SysMessage.MSG_STATUS_READ_NO);
    	sysMsg.setReceiver(userid.toUpperCase()); 
    	sysMessageDAO.createSysMessage(sysMsg);
    }	
	/**
	 * 发送短信
	 * @param fromContext
	 * @param smsto  多个手机号码，可通过逗号分隔 例如：13810959094,13691392209
	 * @param content
	 * @return
	 */
	public boolean sendSMS(UserContext fromContext,String numstr,String content) {
		try
		{
			//判断是否是调试状态
			if(SystemConfig._smsConf.getIsDebug().equals("on")){
				numstr = SystemConfig._smsConf.getDebugMobile();
			}
			//拆分收件人姓名手机号
			String num = numstr;
			String name ="暂无";
			if(numstr.indexOf("[")>0){
				num = numstr.substring(0,numstr.indexOf("["));
				name =  numstr.substring(numstr.indexOf("[")+1,numstr.indexOf("]"));
			}
			final String  sjname=name;	
			Hashtable datas = new Hashtable();
			if (SystemConfig._smsConf.getServer().equals("on")) {
				class SendSMSThread extends Thread {
					private Hashtable datas;
					private String fromUser;
					private String smsto;
					private String content;
					private UserContext uc = null;
					SendSMSThread(UserContext fromUser, String smsto, String content,Hashtable datas) {
						this.uc = fromUser;
						this.fromUser = fromUser.get_userModel().getUserid();
						this.smsto = smsto;
						this.content = content;
						this.datas = datas;
					}
					public void run() {
						String info="";
						if (smsto != null && !smsto.equals("")) {
							String[] ren = smsto.split(",");
							for (int i = 0; i < ren.length; i++) {
								String tel = ren[i];
								if(tel!=null&&!tel.equals("")&&tel.length()==11){
									info=SendMessge2smsUtil.sendSMSforHttp(sjname,fromUser,tel, content.toString());
								}
								this.datas.put("status",info);
							}
						}
					}
				}
				new SendSMSThread(fromContext, num, content,datas).start(); 
			}
		
			while(datas.get("status")==null){
				try {
		            Thread.sleep(1000);
		        } catch (InterruptedException e) {
		            logger.error(e,e); 
		        }
			}
			if(datas.get("status").toString().equals("success")){
				return true;
			}else{
				return false;
			}
		}
		catch(Exception e)	{logger.error(e,e);return false;}		
		
	}
	public boolean sendSMS(String numstr,String content) {
		//判断是否是调试状态
		if(SystemConfig._smsConf.getIsDebug().equals("on")){
			numstr = SystemConfig._smsConf.getDebugMobile();
		}
		//拆分收件人姓名手机号
		String num = numstr;
		String name ="暂无";
		if(numstr.indexOf("[")>0){
			num = numstr.substring(0,numstr.indexOf("["));
			name =  numstr.substring(numstr.indexOf("[")+1,numstr.indexOf("]"));
		}
		final String  sjname=name;	
		boolean flag = false;
		if (SystemConfig._smsConf.getServer().equals("on")) { 
			class SendSMSThread extends Thread {
				private String smsto;
				private String content;			
				SendSMSThread(  String smsto, String content) {
					this.smsto = smsto; 
					this.content = content;
				}
				public void run() {
					if (smsto != null && !smsto.equals("")) {
						String[] ren = smsto.split(",");
						for (int i = 0; i < ren.length; i++) {
							String tel = ren[i];
							if(tel!=null&&!tel.equals("")&&tel.length()==11){
								SendMessge2smsUtil.sendSMSforHttp(sjname,null,tel, content.toString());
							}
						}
					}
				}
			}
			new SendSMSThread(num, content).start(); 
		}
		return flag;
	}
	
	public boolean sendSMS(String userid,String smsto,String content) {
		//判断是否是调试状态
		if(SystemConfig._smsConf.getIsDebug().equals("on")){
			smsto = SystemConfig._smsConf.getDebugMobile();
		}
		boolean flag = false;
		if (SystemConfig._smsConf.getServer().equals("on")) { 
			class SendSMSThread extends Thread {
				private String userid;
				private String smsto;
				private String content;
				SendSMSThread(String userid, String smsto, String content) {
					this.userid = userid; 
					this.smsto = smsto; 
					this.content = content;
				}
				public void run() {
					if (smsto != null && !smsto.equals("")) {
						String[] ren = smsto.split(",");
						for (int i = 0; i < ren.length; i++) {
							String tel = ren[i];
							if(tel!=null&&!tel.equals("")&&tel.length()==11){
								SendMessge2smsUtil.sendSMSforHttp(null,userid,tel, content.toString());
							}
						}
					}
				}
			}
			new SendSMSThread(userid, smsto, content).start(); 
		}
		return flag;
	}
	
	
	/**
	 * 发送im即时消息
	 * @param fromUser 申请人
	 * @param toUser 收通知人员名单列表，如“LIUYAQI[刘雅琪] ZHANGHAORAN[张浩冉]”，中间是用空格分隔。
	 * @param title 通知标题
	 */
	public void sendIM(String fromUser, String toUser, String title) {
		//判断是否是调试状态
		if(SystemConfig._imConf.getIsDebug().equals("on")){
			toUser = SystemConfig._imConf.getDebugUser();
		}
		UserContext uc = UserContextUtil.getInstance().getUserContext(fromUser);
		if (SystemConfig._imConf.getServer().equals("on")) { 
			class SendImThread extends Thread {
				private String fromUser;
				private String toUser;
				private String title;
				private UserContext uc = null;
				SendImThread(UserContext fromUser, String toUser, String title) {
					this.uc = fromUser;
					this.fromUser = fromUser.get_userModel().getUserid();
					this.toUser = toUser;
					this.title = title;
				}
				public void run() {
					String bm = "";
					OrgDepartment dModel = uc.get_deptModel();
					if (dModel != null && !dModel.equals("")) {
						bm = dModel.getDepartmentname();
					}
					StringBuffer content = new StringBuffer();
					content.append("【发 送 人】").append(uc.get_userModel().getUsername()).append("(").append(uc.get_userModel().getUserid()).append(")").append("\n");
					content.append("【所在部门】").append(bm).append("\n");
					content.append("【任务标题】").append(title).append("\n").append(
							"【任务来源】").append(SystemConfig._imConf.getSource())
							.append("\n");
					if (toUser != null && !toUser.equals("")) {
						String[] ren = toUser.split(" ");
						for (int i = 0; i < ren.length; i++) {
							String ren2[] = ren[i].split("<");
								SendMessge2RtxUtil.sendIMforHttp(fromUser, ren2[0], content.toString());
						}
					}
				}
			}
			new SendImThread(uc, toUser, title).start(); 
		}
	}
	
	/**
	 * 发送im即时消息
	 * @param fromUser 申请人
	 * @param toUser 收通知人员名单列表，如“LIUYAQI[刘雅琪] ZHANGHAORAN[张浩冉]”，中间是用空格分隔。
	 * @param title 通知标题
	 */
	public void sendIM(String fromUser, String toUser, String title,String content) {
		UserContext uc = UserContextUtil.getInstance().getUserContext(fromUser);
		//判断是否是调试状态
		if(SystemConfig._imConf.getIsDebug().equals("on")){
			toUser = SystemConfig._imConf.getDebugUser();
		}
		if (SystemConfig._imConf.getServer().equals("on")) { 
			class SendImThread extends Thread {
				private String fromUser;
				private String toUser;
				private String title;
				private String content;
				private UserContext uc = null;
				SendImThread(UserContext fromUser, String toUser, String title,String content) {
					this.uc =fromUser;
					this.fromUser = fromUser.get_userModel().getUserid();
					this.toUser = toUser;
					this.title = title;
					this.content = content;
				}
				public void run() {
					String bm = "";
					OrgDepartment dModel = uc.get_deptModel();
					if (dModel != null && !dModel.equals("")) {
						bm = dModel.getDepartmentname();
					}
					if (toUser != null && !toUser.equals("")) {
						String[] ren = toUser.split(",");
						for (int i = 0; i < ren.length; i++) { 
							String userid = UserContextUtil.getInstance().getUserId(ren[i]);
							SendMessge2RtxUtil.sendIMforHttp(fromUser, userid, content);
						}
					}
				}
			}
			new SendImThread(uc, toUser, title,content).start(); 
		}
	}
	public boolean sendSysMail(String fromTitle,String mailAddress,String title,String content,String filename){
		boolean flag = false;
		try
		{
			if(SystemConfig._mailServerConf.getIsDebug().equals("on")){
				mailAddress = SystemConfig._mailServerConf.getDebug_address();
			}
			if (SystemConfig._mailServerConf.getSmtp_services().equals("on")) {
				class SendMailThread extends Thread {
					private String fromTitle;
					private String toMailAddress;
					private String title;
					private String content;
					private String filename;
					SendMailThread(String fromTitle,String toMailAddress, String title, String content,String filename) {
						this.fromTitle = fromTitle; 
						this.toMailAddress = toMailAddress;
						this.title = title;
						this.content = content;
						this.filename=filename;
					}
					
					public void run() { 
						SendMail sm = new SendMail();
						sm.setTo(toMailAddress);
						sm.setBody(content); 
						sm.setFrom(fromTitle, SystemConfig._mailServerConf.getMailFrom());
						sm.setSubject(title); 
						if(filename!=null&&!filename.equals("")){
							String[] fjarr=filename.split(";");
							for(String fj:fjarr){
								sm.addFileAffix(fj);
							}
						}
						sm.send();
					}	
				} 
				new SendMailThread(fromTitle,mailAddress,title, content,filename).start();
				flag = true;
			}
		}
		catch(Exception e){logger.error(e,e); }
		try
		{
			//当前发送人的id
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String userid = uc.get_userModel().getUserid();
			Map params = new HashMap();
			params.put(1,mailAddress);
			String username = DBUTilNew.getDataStr("USERNAME","SELECT USERNAME FROM ORGUSER WHERE trim(EMAIL) = ? ", params);
			if(mailWithAttachmentDAO==null){
				mailWithAttachmentDAO = (MailWithAttachmentDAO)SpringBeanUtil.getBean("mailWithAttachmentDAO");
			}
			Date stringToDate = UtilDate.StringToDate(UtilDate.getNowDatetime(),"yyyy-MM-dd HH:mm:ss");
			if(flag){
				MailLog ml=new MailLog();
				ml.setContent(content);
				ml.setEmail(mailAddress);
				ml.setTitle(title);
				ml.setAttach(filename);
				ml.setUserid(userid);
				ml.setName(username);
				ml.setStatus(1);
				ml.setSendtime(stringToDate);
				ml.setSubmittime(stringToDate);
				mailWithAttachmentDAO.addBoData(ml);
			}else{
				MailLog ml=new MailLog();
				ml.setContent(content);
				ml.setEmail(mailAddress);
				ml.setTitle(title);
				ml.setAttach(filename);
				ml.setUserid(userid);
				ml.setName(username);
				ml.setStatus(0);
				ml.setSendtime(stringToDate);
				ml.setSubmittime(stringToDate);
				mailWithAttachmentDAO.addBoData(ml);
			}
		}
		catch(Exception e){logger.error(e,e); }
		return flag;
	}
}
