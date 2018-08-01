package com.ibpmsoft.project.zqb.trigger;

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

/**
 * 项目基本信息添加
 * @author David
 *
 */
public class ZQB_Event_SaveEvent  extends DemTriggerEvent {
	public ZQB_Event_SaveEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	
	/**
	 * 执行触发器
	 */
	public boolean execute() { 
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String smsContent = "";
		String sysMsgContent = "";
		String value=hash.get("SXMC").toString();
		Long dataId=Long.parseLong(hash.get("ID").toString());
		if("0".equals(map.get("instanceId").toString())){
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.EVENT_ADD_KEY, map); 
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.EVENT_ADD_KEY, map); 
			LogUtil.getInstance().addLog(dataId, "信披基本信息维护", "添加重大事项："+value);
		}else{
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.EVENT_UPDATE_KEY, map); 
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.EVENT_UPDATE_KEY, map); 
			LogUtil.getInstance().addLog(dataId, "信披基本信息维护", "修改重大事项："+value);
		} 
		String customerno = "";
		if(map.get("KHBH")!=null){
			customerno = map.get("KHBH").toString();
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
					MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, "重大事项申报提醒",smsContent,"");
				}
			}
			if(!sysMsgContent.equals("")){ 
					MessageAPI.getInstance().sendSysMsg(userid, "重大事项申报提醒", sysMsgContent);
			}
		}
		return true;
	}
}

