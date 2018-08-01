package com.iwork.asset.event;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.asset.constant.StatusConstant;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.model.SysDemEngine;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 
 * 归档触发器
 * 修改流程状态(审批中-已归档),更改资源卡片管理状态为已使用
 * 在流程触发器中配置
 * @author yanglianfeng
 *
 */
public class AssetReceiveProcessEvent extends ProcessTriggerEvent {
	public static final String zckpsubformkey="SUBFORM_GDZCZQGZBD";//固定资产卡片subformkey
	public static final String subformkey="SUBFORM_GDZCLYSQZBD";//固定资产领用subformkey
	public AssetReceiveProcessEvent(UserContext uc, HashMap<String,Object> hash) {
		super(uc, hash);
	}
	
	@SuppressWarnings("unchecked")
	public boolean execute(){
		
		boolean flag = false;
		HashMap newHash=new HashMap();
		// 获取流程实例
		Long instanceid = this.getInstanceId();
		// 获取流程标识
		String actDefId = this.getActDefId();
		
		//根据表单模型取出表单instanceId和demid
		SysDemEngine sde=DemAPI.getInstance().getDemModel(StatusConstant.ZYKP_UUID);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		 
		
		//获取newInstanceId与Demid
		Long newIncetanceId=DemAPI.getInstance().newInstance(StatusConstant.ZYKP_UUID, uc.get_userModel().getUserid());
		
		// 查询数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		// 获取行标识
		Long dataid = Long.parseLong(dataMap.get("ID").toString());
		//获取子表的数据
		List<HashMap>  subList=ProcessAPI.getInstance().getFromSubData(instanceid, subformkey);
		//遍历固定资源卡记录
		if(subList!=null && subList.size()>0){
			for(HashMap subHash:subList){
				//资产卡片编号
				String no=subHash.get("NO")==null?"":subHash.get("NO").toString();
				newHash.put("NO", no);
				//获取拥有者
				String ouname=subHash.get("OUNAME")==null?"":subHash.get("OUNAME").toString();
				String dptname=subHash.get("DPTNAME")==null?"":subHash.get("DPTNAME").toString();
				String ouno=subHash.get("OUNO")==null?"":subHash.get("OUNO").toString();
				List<HashMap> list=DemAPI.getInstance().getList(StatusConstant.ZYKP_UUID, newHash, null);
				for(HashMap hash:list){
					Long dateId=hash.get("ID")==null?0L:Long.parseLong(hash.get("ID").toString());
					Long.parseLong(hash.get("INSTANCEID").toString());
					hash.put("STATUS", StatusConstant.ZYKP_GLZTYSY);
					hash.put("OUNAME",ouname);
					hash.put("DPTNAME",dptname);
					hash.put("OUID",ouno);
					//获取当前系统时间
					String chagneDate = UtilDate.getNowdate();
					hash.put("CHANGEDATE",chagneDate);
					//做更新操作,将资产卡片的状态设置为已使用
					flag=DemAPI.getInstance().updateFormData(StatusConstant.ZYKP_UUID, Long.parseLong(hash.get("INSTANCEID").toString()), hash, dateId, false);
					//向资源卡片子表添加记录
					List<HashMap> zysubList=new ArrayList<HashMap>();
					HashMap subMap=new HashMap();
					//操作
					subMap.put("ACTIONTYPE", StatusConstant.ZYKP_GLZTYLY);
					//获取当前系统时间
					String date = UtilDate.getNowdate();
					subMap.put("RQ", date);
					//获取部门
					subMap.put("ODEPARTMENT", hash.get("DPTNAME").toString());
					//获取单位
					subMap.put("OCOMPANY", subHash.get("COPNAME").toString());
					//获取经手人
					subMap.put("PROCESSMAN", dataMap.get("STAFFNAME").toString());
					//添加拥有者
					subMap.put("OUSERNAME",ouname);
					//获取流程istanceid
					subMap.put("LCSLID",instanceid);
					//获取流程actdefid
					subMap.put("ACTDEFID",actDefId);
					zysubList.add(subMap);
					//添加卡片记录
					flag=DemAPI.getInstance().saveFormDatas(StatusConstant.ZYKP_UUID, Long.parseLong(hash.get("INSTANCEID").toString()), zckpsubformkey, zysubList, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
				}
			}
		}
		// 设置审批状态
		dataMap.put("STATUS", StatusConstant.PROCESS_STATUS_YIGUIDANG);
		flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceid, dataMap, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		return flag;
	}
}