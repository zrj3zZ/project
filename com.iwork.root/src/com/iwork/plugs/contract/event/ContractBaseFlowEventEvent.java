package com.iwork.plugs.contract.event;

import java.util.HashMap;
import java.util.List;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.fi.util.CalculateUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class ContractBaseFlowEventEvent extends ProcessTriggerEvent {

	public static final String HTJBXX_UUID = "0d48d769d5734df3b94c64421440c770";//合同基本信息表
	public static final String HTJHJH_UUID = "f1e455a42a294d80ac99d09f4fcd7baf";//合同交货计划表
	public static final String HTFPJH_UUID = "f76af806ee7740c19eae9be7fd0a14ec";//合同发票计划表
	public static final String HTJH_UUID = "525e3a9555494ef5a052c0386a17802c";//合同计划表
	public static final String SUB_FORM_KEY_TASK1 = "SUBFORM_HTJHJHSPB";//合同交货计划审批表
	public static final String SUB_FORM_KEY_TASK2 = "SUBFORM_HTFPJHSQB";//合同发票计划审批表
	public static final String SUB_FORM_KEY_TASK3 = "SUBFORM_HTZXJHSPB";//合同执行计划审批表
	
	
	public ContractBaseFlowEventEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		boolean flag = false;
		Long instanceid = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 查询流程数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		Long newInstanceid = DemAPI.getInstance().newInstance(HTJBXX_UUID, uc.get_userModel().getUserid());
		HashMap<String,Object> conditionMap = new HashMap<String,Object>();
		conditionMap.put("PACTNO", dataMap.get("PACTNO"));
		String fromno =  (String)dataMap.get("FROMNO");
		//根据合同编号合同标题查询基本信息是否有数据
		int contactSize = DemAPI.getInstance().getListSize(HTJBXX_UUID, conditionMap, null);
		List<HashMap> list = DemAPI.getInstance().getList(HTJBXX_UUID, conditionMap, null);
		HashMap map = new HashMap();
		//如果有对应的合同基本信息记录
		if(list!=null && contactSize!=0){
			for(HashMap mapda:list){
				flag=DemAPI.getInstance().updateFormData(HTJBXX_UUID, CalculateUtil.changeTypeToLong(mapda.get("INSTANCEID")), mapda, CalculateUtil.changeTypeToLong(mapda.get("ID")), false);
			}
		}else{
			flag = addSubContactFromDate(newInstanceid,dataMap);
		}
		if(flag!=true){
			
		}
		String subform = "";
		if(dataMap!=null){
			 subform = "SUBFORM_HTJHJHSPB";
			}
		//新增合同交货计划
		List<HashMap> subList = ProcessAPI.getInstance().getFromSubData(instanceid,subform);
		if(subList!=null && subList.size()!=0){
			flag = addSubContactTaskInfo(subList,fromno);
		}
		if(flag!=true){
			
		}
		if(dataMap!=null){
			subform = "SUBFORM_HTFPJHSQB";
		}
		//新增合同发票计划
		List<HashMap> fpList = ProcessAPI.getInstance().getFromSubData(instanceid, subform);
		if(fpList!=null && fpList.size()!=0){
			flag = addSubContactFP(fpList,fromno);
		}
		if(flag!=true){
			
		}
		if(dataMap!=null){
			 subform = "SUBFORM_HTZXJHSPB";
			}
		//新增合同执行计划
		List<HashMap> zxList = ProcessAPI.getInstance().getFromSubData(instanceid, subform);
		if(zxList!=null && zxList.size()!=0){
			flag = addaddSubContactZXJH(zxList,fromno);
		}
		
		return flag;
	}
	/**
	 * 新增基本信息表
	 */
	public boolean addSubContactFromDate(Long newinstanceId,HashMap subList){
		boolean flag = false;
		if(newinstanceId!=null && subList!=null){
			flag = DemAPI.getInstance().saveFormData(HTJBXX_UUID, newinstanceId, subList, false);
		}
		
		return flag;
	}
	/**
	 * 新增合同交货计划表
	 * @param subList
	 * @return
	 */
	public boolean addSubContactTaskInfo(List<HashMap> subList,String fromno){
		boolean flag = false;	
		if(subList!=null){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			for(HashMap<String,Object> subMap:subList){
				subMap.put("FROMNO", fromno);
				Long instanceId = DemAPI.getInstance().newInstance(HTJHJH_UUID, uc.get_userModel().getUserid());
				flag = DemAPI.getInstance().saveFormData(HTJBXX_UUID, instanceId, subMap, false);
			}
		}
		
		return flag;
	}
	/**
	 * 新增合同发票计划表
	 */
	public boolean addSubContactFP(List<HashMap> subList,String fromno){
		boolean flag = false;		
		if(subList != null){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			for(HashMap<String,Object> subMap:subList){
				subMap.put("FROMNO", fromno);
				Long instanceId = DemAPI.getInstance().newInstance(HTFPJH_UUID, uc.get_userModel().getUserid());
				flag = DemAPI.getInstance().saveFormData(HTFPJH_UUID, instanceId, subMap, false);
			}
		}
		return flag;
	}
	/**
	 * 新增合同执行计划
	 */
	public boolean addaddSubContactZXJH(List<HashMap> subList,String fromno){
		boolean flag = false;	
		if(subList != null){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			for(HashMap<String,Object> subMap:subList){
				subMap.put("FROMNO", fromno);
				Long instanceId = DemAPI.getInstance().newInstance(HTJH_UUID, uc.get_userModel().getUserid());
				flag = DemAPI.getInstance().saveFormData(HTJH_UUID, instanceId, subMap, false);
			}
		}
		return flag;
	}
}

