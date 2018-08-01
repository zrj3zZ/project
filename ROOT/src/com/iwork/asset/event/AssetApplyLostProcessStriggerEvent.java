package com.iwork.asset.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.iwork.asset.constant.StatusConstant;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 
 * 节点触发器
 * 流程节点触发器
 * 丢失流程状态(起草-审批中)
 * 修改资产卡片状态（申请丢失）
 * 在流程第一个节点配置
 * @author zhangruibo
 *
 */
public class AssetApplyLostProcessStriggerEvent extends ProcessStepTriggerEvent{
	public static final String zckpsubformkey="SUBFORM_GDZCZQGZBD";//固定资产卡片subformkey
	public AssetApplyLostProcessStriggerEvent(UserContext uc, HashMap<String,Object> hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}
	@SuppressWarnings("unchecked")
	public boolean execute(){
		
		boolean flag = false;
		HashMap newHash=new HashMap();
		// 获取流程实例
		
		Long instanceid = this.getInstanceId();
		// 获取流程标识
		String actDefId = this.getActDefId();
		// 查询数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		// 获取行标识
		Long dataid = Long.parseLong(dataMap.get("ID").toString());
		//获取当前系统时间
		String date = UtilDate.getNowdate();
		
		/**以下要实现的业务是将丢失流程中的详细信息插入到资源卡片中  2016-1-20-**/
		
		if(dataMap!=null){
			//获取资产卡片
			String cardno=dataMap.get("CARDNO")==null?"":dataMap.get("CARDNO").toString();
			newHash.put("NO", cardno);//资产卡片编号
			//获取管理状态
			String status=dataMap.get("STATUS")==null?"":dataMap.get("STATUS").toString();
			newHash.put("NO", cardno);//资产卡片编号
		
			List<HashMap> sublist=DemAPI.getInstance().getList(StatusConstant.ZYKP_UUID, newHash, null);
			HashMap hash=sublist.get(0);
			//管理状态（申请报废）
			hash.put("STATUS", StatusConstant.ZYKP_GLZTDSZ);
			//获取当前系统时间
			hash.put("CHANGEDATE", date);
			//获取incetanceid与dateid
			Long instanceId=Long.parseLong(hash.get("INSTANCEID").toString());
			Long dateid=Long.parseLong(hash.get("ID").toString());
			flag=DemAPI.getInstance().updateFormData(StatusConstant.ZYKP_UUID,instanceId , hash, dateid, false);
		
			HashMap hashMap=new HashMap();
			//向资源卡片子表添加记录
			List<HashMap> zysubList=new ArrayList<HashMap>();
			//操作
			hashMap.put("ACTIONTYPE",StatusConstant.ZYKP_GLZTDSZ);
			//添加管理变更日期
			hashMap.put("RQ", date);
			//获取部门
			hashMap.put("ODEPARTMENT", dataMap.get("ODNAME").toString());
			//获取单位
			hashMap.put("OCOMPANY", dataMap.get("OCNAME").toString());
			//获取经手人
			hashMap.put("PROCESSMAN", dataMap.get("OUNAME").toString());
			//添加拥有者
			hashMap.put("OUSERNAME",dataMap.get("OUNAME").toString());
			//获取流程istanceid
			hashMap.put("LCSLID",instanceid);
			//获取流程actdefid
			hashMap.put("ACTDEFID",actDefId);
			zysubList.add(hashMap);
		
			//添加卡片记录
			flag=DemAPI.getInstance().saveFormDatas(StatusConstant.ZYKP_UUID, Long.parseLong(hash.get("INSTANCEID").toString()), zckpsubformkey, zysubList, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		}
		// 设置审批状态
		dataMap.put("STATUS", StatusConstant.PROCESS_STATUS_SHENPIZHONG);
		flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceid, dataMap, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		return flag;
	}
}
