package com.iwork.core.organization.event;

import java.util.HashMap;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

public class OrgPurviewCheckTriggerEvent extends ProcessStepTriggerEvent {
	private UserContext _me;
	private HashMap params;
	public OrgPurviewCheckTriggerEvent(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	}
	
	/**
	 * 检查当前账号是否已经创建，如果未创建不允许进行下一步授权操作
	 */
	public boolean execute() {
		boolean flag = true;
		//获取当前用户ID
		 HashMap hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(hash!=null&&hash.get("SQRZH")!=null){
			String userid = hash.get("SQRZH").toString();
			OrgUser model = UserContextUtil.getInstance().getOrgUserInfo(userid);
			if(model==null){
				MessageQueueUtil.getInstance().putAlertMsg("当前申请用户的账号未创建，请创建账号后进行下一步操作");
				flag = false;
			}
		}else{
			flag = false;
		}
		return flag;
	}
}
