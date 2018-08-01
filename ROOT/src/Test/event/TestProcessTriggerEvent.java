package Test.event;

import java.util.HashMap;


import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;

/**
 * 流程级触发器
 * @author zouyalei
 *
 */
public class TestProcessTriggerEvent extends ProcessTriggerEvent {

	public static final String ACTDEF_ID = "CCSQLC_1:1:82704";
	public static final String SUBFORM_KEY = "SUBFORM_KEY";
	public static final String PROCESS_DEFINITION_KEY = "CCSQLC_1";
	
	public TestProcessTriggerEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute(){
		Long instanceId = this.getInstanceId();// 流程实例ID
		this.getTaskId();    // 流程任务ID
		this.getExcutionId();// 流程实例执行ID
		this.getActDefId();  // 流程ID
		this.getReceiveUser();// 转发接收人
		this.getParams();    // 其他参数
		this.getOwner();     // 获取流程发起人
		
//		// 获取流程表单HashMap
//		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
//		// 保存流程表单数据
//		HashMap<String,Object> hashdata = new HashMap<String,Object>();
//		// isLog 标识是否记录日志  true记录  false 不记录
//		ProcessAPI.getInstance().saveFormData(ACTDEF_ID, instanceId, hashdata, false);
//		// 更新流程表单数据
//		Long dataid = Long.parseLong(dataMap.get("ID").toString());
//		ProcessAPI.getInstance().updateFormData(ACTDEF_ID, instanceId, hashdata, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
//		// 保存流程表单子表单数据
//		List<HashMap> list = new ArrayList<HashMap>();
//		HashMap<String,Object> subMap = new HashMap<String,Object>();
//		list.add(subMap);
//		ProcessAPI.getInstance().saveFormDatas(ACTDEF_ID, instanceId, SUBFORM_KEY, list, false);
//		// 更新流程表单子表单数据
//		ProcessAPI.getInstance().updateFormDatas(ACTDEF_ID, SUBFORM_KEY, instanceId, list, null, null, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
//		
//		// 查询流程子表单数据 返回List
//		ProcessAPI.getInstance().getFromSubData(instanceId, SUBFORM_KEY);
//		// 根据流程key获取运行中的流程ID
//		ProcessAPI.getInstance().getProcessActDefId(PROCESS_DEFINITION_KEY);
//		// 获取指定流程默认表单ID
//		ProcessAPI.getInstance().getProcessDefaultFormId(ACTDEF_ID);
//		// 获取流程全局变量
//		Long paramType = 0L;
//		String paramName = "变量";
//		ProcessAPI.getInstance().getProcessDefParamObj(ACTDEF_ID, paramType, paramName);
//		// 获取审核意见列表
//		ProcessAPI.getInstance().getProcessOpinionList(ACTDEF_ID, instanceId);
//		// 获取办理用户执行列表
//		ProcessAPI.getInstance().getProcessTransUserList(instanceId);
		
		MessageQueueUtil.getInstance().putAlertMsg("流程级触发器！");
		
		System.out.println("流程级触发器！");
		
		return true;
	}

}
