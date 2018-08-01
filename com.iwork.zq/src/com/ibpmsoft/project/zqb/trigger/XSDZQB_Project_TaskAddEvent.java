package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;

import com.ibpmsoft.project.zqb.common.ZQBRoleConstants;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
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
public class XSDZQB_Project_TaskAddEvent  extends DemTriggerEvent {
	public XSDZQB_Project_TaskAddEvent(UserContext me,HashMap hash){
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
		if(hash!=null){
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.PROJECT_TASK_UPDATE_KEY, map); 
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.PROJECT_TASK_UPDATE_KEY, map); 
		}else{
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.PROJECT_TASK_ADD_KEY, map); 
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.PROJECT_TASK_ADD_KEY, map); 
		}
		
			String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
			UserContext uc = this.getUserContext();
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			if(target!=null){
				if(!smsContent.equals("")){
					String mobile = target.get_userModel().getMobile();
					if(mobile!=null&&!mobile.equals("")){
						MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
					}
				}
				if(!sysMsgContent.equals("")){
						MessageAPI.getInstance().sendSysMsg(userid, "项目任务维护提醒", sysMsgContent);
				}
			}
		return true;
	}
}

