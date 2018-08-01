package com.ibpmsoft.project.xiaomi.trigger;

import java.util.HashMap;
import java.util.List;
import com.iwork.connection.constants.ConnectionConstants;
import com.iwork.connection.impl.ConnectionSAPImpl;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.constant.ProcessTaskConstant;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;

public class ProcessSetSAPActiveEvent extends ProcessTriggerEvent {
	private HashMap params = null;
	public ProcessSetSAPActiveEvent(UserContext me,HashMap hash){
		super(me,hash);
		params = hash;
	}
	
	public boolean execute() {
		boolean flag =false;
		UserContext _me = this.getContext();
		String actDefId = this.getActDefId();
		ConnectionSAPImpl impl = new  ConnectionSAPImpl();
		String ccUUID = "2ccb8a1a917146d6a5492424c57d1bf3"; 
		if(params!=null){ 
			Object obj = params.get(ProcessTaskConstant.PROCESS_TASK_FORMNO);
			if(obj!=null){
				HashMap data = new HashMap();
				data.put("I_PO", obj);
				List<HashMap> returnList = impl.execute(ccUUID,data);
				if(returnList!=null&&returnList.size()>0){
					for(HashMap returnData:returnList){
						Object object = returnData.get(ConnectionConstants.CONN_EXECUTE_RETURN_LOG_KEY);
						if(object!=null){
							String log = object.toString();
							if(log.equals("")){
								flag = true;
							}else{ 
								MessageQueueUtil.getInstance().putAlertMsg(log);
							}
						
						}
					}
					
				}
			}
		}
		return true; 
	}
	
}
