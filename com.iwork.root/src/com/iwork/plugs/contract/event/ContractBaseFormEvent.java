package com.iwork.plugs.contract.event;

import java.util.HashMap;

import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import org.apache.log4j.Logger;
public class ContractBaseFormEvent extends ProcessStepTriggerEvent {
	private static Logger logger = Logger.getLogger(ContractBaseFormEvent.class);
	private UserContext _me;
	private HashMap params;
	public ContractBaseFormEvent(UserContext me,HashMap hash){
		super(me,hash);
		_me= me;
		params= hash;
		}
	public boolean execute() {
		if(params!=null){
		//获取流程ID
		String actDefId =this.getActDefId();
		//获取流程实例ID
		Long instanceId = this.getInstanceId();
		//获取表名
		String tableName = this.getTableName();
		//获得表单填报数据
		HashMap FromData  = this.getFormData();
		String strs=cStrArrToSingleStr(FromData.get("PACTTITLE"));
		
		//获得流程节点ID
		String stepId = this.getActStepId();
		try{
		MessageQueueUtil.getInstance().putAlertMsg("测试弹出窗口消息");
		}catch(Exception e){logger.error(e,e);}
		}
		return true;
		}
	public String cStrArrToSingleStr(Object formMap){
		String strValue = "";
		String[] strArr ;
		if(formMap!=null && formMap.getClass().isArray()){
			try{
				strArr = (String[])formMap;
				strValue = strArr[0];
			}catch(Exception e){
				logger.error(e,e);
			}
		}
		return strValue;
	} 
}
