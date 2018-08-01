package com.iwork.asset.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

/**
 * 
 * 流程节点触发器
 * @author yanglianfeng
 *
 */
public class AssetRecipientsBeforeStepEvent extends ProcessStepTriggerEvent {
	public static final String subformkey="SUBFORM_GDZCLYSQZBD";//固定资产领用subformkey
	public AssetRecipientsBeforeStepEvent(UserContext uc, HashMap<String,Object> hash) {
		super(uc, hash);
	}
	
	@SuppressWarnings("unchecked")
	public boolean execute(){
		
		boolean flag = false;
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap hashdate = new HashMap();
		// 获取流程实例
		Long instanceid = this.getInstanceId();
		// 获取流程标识
		String actDefId = this.getActDefId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		//获取部门编号
		String dptno=uc.get_deptModel().getDepartmentno();
		//获取部门名称
		String dptname=uc.get_deptModel().getDepartmentname();
		//获取使用人账号
		String staffno=uc.get_userModel().getUserid();
		//获取使用人
		String staffname=uc.get_userModel().getUsername();
		//添加部门id
		hashdate.put("DPTNO", dptno);
		//添加部门
		hashdate.put("DPTNAME", dptname);
		//添加使用人ID
		hashdate.put("OUNO", staffno);
		//添加使用人
		hashdate.put("OUNAME", staffname);
		list.add(hashdate);
		List<HashMap>  subList=ProcessAPI.getInstance().getFromSubData(instanceid, subformkey);
		//如果子表内容为空时才进行以下操作，可能存在驳回的情况
		if(subList.size()==0){
			//向子表中添加使用人信息
			flag=ProcessAPI.getInstance().saveFormDatas(actDefId, instanceid, subformkey, list, false);
		}
		
		return flag;
	}
}
