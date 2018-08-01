package com.iwork.app.scancode.impl;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.iwork.app.scancode.interceptor.ScanCodeInterface;
import com.iwork.commons.util.ObjectUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.runtime.service.ProcessRuntimeExcuteService;
import com.iwork.sdk.ProcessAPI;

public class WeiXinProcessFormpageeScanCodeImpl implements ScanCodeInterface {
	private ProcessRuntimeExcuteService processRuntimeExcuteService;
	public String buildQRCode(Map params, String codeType) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		String instanceId = ObjectUtil.getString(params.get("instanceId"));
		String excutionId = ObjectUtil.getString(params.get("excutionId"));
		String engineType = ObjectUtil.getString(params.get("engineType"));
		String formid = ObjectUtil.getString(params.get("formid"));
		str.append("fbc,").append(engineType).append(",").append(formid).append(",").append(instanceId).append(",").append(excutionId);
		return str.toString();
	}
	public String buildBarCode(Map params, String codeType) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		String instanceId = ObjectUtil.getString(params.get("instanceId"));
		String formid = ObjectUtil.getString(params.get("formid"));
		HashMap hash = ProcessAPI.getInstance().getFromData(Long.parseLong(instanceId), Long.parseLong(formid), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String code = ObjectUtil.getString(hash.get("LCBH"));
		if(code!=null){
			 String tmp = code.substring(6);
			str.append(tmp);
		}
		return str.toString();
	}

	public String getResult(String codeStr) {
		String[] params = codeStr.split(",");
		StringBuffer result = new StringBuffer();
		if(params!=null&&params.length==5){
			String instanceId = params[3];
			String excutionId = params[4];
			String formId =  params[2];
			if (processRuntimeExcuteService == null)
				processRuntimeExcuteService = (ProcessRuntimeExcuteService) SpringBeanUtil.getBean("processRuntimeExcuteService");
			Task task =	processRuntimeExcuteService.getTaskService().createTaskQuery().executionId(excutionId).singleResult();
			if(task!=null){
				String actDefId = task.getProcessDefinitionId();
				result.append("url:weixin_processFormPage.action?actDefId=").append(actDefId).append("&instanceId=").append(instanceId).append("&excutionId=").append(excutionId).append("&taskId=").append(task.getId());
			}else{
				ProcessInstance excution = processRuntimeExcuteService.getRuntimeService().createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
				if(excution!=null){
					result.append("url:weixin_processFormPage.action?actDefId=").append(excution.getProcessDefinitionId()).append("&instanceId=").append(instanceId).append("&excutionId=").append(excutionId);
				}else{
					result.append("error");
				}
			}
		}
		return result.toString();
	}
}
