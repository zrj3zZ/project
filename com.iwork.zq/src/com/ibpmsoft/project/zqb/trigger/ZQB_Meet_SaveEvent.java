package com.ibpmsoft.project.zqb.trigger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import org.apache.log4j.Logger;
/**
 * 项目基本信息添加
 * @author David
 *
 */
public class ZQB_Meet_SaveEvent  extends DemTriggerEvent {
	private static Logger logger = Logger.getLogger(ZQB_Meet_SaveEvent.class);
	public ZQB_Meet_SaveEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	public  String dateToStr(Date date) {
		  
		   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		   String str = format.format(date);
		   return str;
	} 
  public  boolean compare_date(String DATE1, String DATE2) {
         
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
         try {
             Date dt1 = df.parse(DATE1);
             Date dt2 = df.parse(DATE2);
             if (dt1.getTime() > dt2.getTime()) {
                 return false;
             } else if (dt1.getTime() <= dt2.getTime()) {
                 return true;
             } 
         } catch (Exception e) {
        	 logger.error(e,e);
         }
         return true;
     }
	
	/**
	 * 执行触发器
	 */
	public boolean execute() { 
		//添加会议计划就给对应的持续督导人员发短信通知，告知内容格式如下：【公司简称】于2014-12-24日计划召开第一届第十一次董事会，请您关注！
		//（注：已召开过的会议，后填到系统中的不进行短信提醒，但系统消息提醒谁填了什么会议就可以）
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String smsContent = "";
		String sysMsgContent = "";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(hash!=null){
			if(compare_date(dateToStr(new Date()),map.get("PLANTIME").toString())){
				try {
					map.put("PLANTIME", df.format(df.parse(map.get("PLANTIME").toString())));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
				smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.MEET_UPDATE_KEY, map); 
			}
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.MEET_UPDATE_KEY, map);
			String value=hash.get("MEETNAME")==null?"":hash.get("MEETNAME").toString();
			Long dataId=Long.parseLong(hash.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "三会计划", "更新会议："+value);
		}else{
			if(compare_date(dateToStr(new Date()),map.get("PLANTIME").toString())){
				try {
					map.put("PLANTIME", df.format(df.parse(map.get("PLANTIME").toString())));
				} catch (ParseException e) {
					logger.error(e,e);
				}
				smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.MEET_ADD_KEY, map); 
			}
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.MEET_ADD_KEY, map);
			String value=map.get("MEETNAME")==null?"":map.get("MEETNAME").toString();
			LogUtil.getInstance().addLog(0L, "三会计划", "添加会议："+value);
		} 
		String customerno = "";
		if(map.get("CUSTOMERNO")!=null){
			customerno = map.get("CUSTOMERNO").toString();
		}
		String useraddress = ZQBNoticeUtil.getInstance().getDuDaoCustomer(customerno);
		String userid = UserContextUtil.getInstance().getUserId(useraddress);
		UserContext uc = this.getUserContext();
		UserContext target = UserContextUtil.getInstance().getUserContext(userid); 
		if(target!=null){
			if(!smsContent.equals("")){
				String mobile = target.get_userModel().getMobile();
				if(mobile!=null&&!mobile.equals("")){
					MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
				}
				String email = target.get_userModel().getEmail();
				if(email!=null&&!email.equals("") && uc != null){
					MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, "会议信息维护提醒",smsContent,"");
				}
			}
			if(!sysMsgContent.equals("")){ 
					MessageAPI.getInstance().sendSysMsg(userid, "会议信息维护提醒", sysMsgContent);
			}
		}
		return true;
	}
}

