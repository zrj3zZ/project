package com.ibpmsoft.project.cbpmc.instorage.trigger;

import java.util.HashMap;
import java.util.List;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

public class EAM_InStorageStepVerificationTrigger  extends ProcessStepTriggerEvent{
	private UserContext _me;
	private HashMap params;
	private String log = "";
	public EAM_InStorageStepVerificationTrigger(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	}
	
	public boolean execute() {
		boolean flag = true;
		String subformkey = "SUBFORM_SWZCMXB_1";
		List<HashMap> list = ProcessAPI.getInstance().getFromSubData(this.getInstanceId(), subformkey);
		if(list==null||list.size()==0){
			flag = false;
			MessageQueueUtil.getInstance().putAlertMsg("实物资产明细不能为空");
		}else{
			for(HashMap hash:list){
				String username = "";
				String afterplace = "";
				if(hash.get("USERNAME")!=null){
					username =hash.get("USERNAME").toString();
				}
				if(hash.get("AFTERPLACE")!=null){
					afterplace = hash.get("AFTERPLACE").toString();
				}
				if(username.equals("")||afterplace.equals("")){
					flag = false;
					StringBuffer msg = new StringBuffer();
					msg.append("责任人或实际使用地点不能为空"); 
					MessageQueueUtil.getInstance().putAlertMsg(msg.toString());
					break;
				}
			}
		}
		
		return flag;
	}
}
