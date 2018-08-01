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
 * 归档触发器
 * 
 * 修改增强配置业务流程状态(审批中-已归档),更改资产卡片状态为已使用；
 * 在流程触发器中配置
 * @author zhangtian
 *
 */
public class AssetEnhanceProcessStriggerEvent extends ProcessTriggerEvent{
	public static final String zckpsubformkey="SUBFORM_GDZCZQGZBD";//固定资产卡片subformkey
	public AssetEnhanceProcessStriggerEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	@SuppressWarnings("unchecked")
	public boolean execute(){
		
		boolean flag = false;		
		HashMap newHash = new HashMap();//用于存放下面再表单上取到的数据
		//获取流程实例
		Long instanceid = this.getInstanceId();
		//获取流程标识
		String actDefId = this.getActDefId();
		
		//根据归还表单模型，取出表单的instaceid和demid (流程/主数据)
		SysDemEngine sde = DemAPI.getInstance().getDemModel(StatusConstant.ZYKP_UUID);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		
		//获取newInstanceId和Demid (通过这两个参数，得到newInstanceId)
		Long newInstanceId = DemAPI.getInstance().newInstance(StatusConstant.ZYKP_UUID, uc.get_userModel().getUserid());
		
		//查询数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		//获取行标识
		Long dataid = Long.parseLong(dataMap.get("ID").toString());
		
		/** 以下要实现的业务是将固定资产增强配置流程中的详细信息插入到资产卡片中   **/
		if (dataMap!=null) {
			//获取资产卡片编号 NO
			String no = dataMap.get("CARDNO")==null?"":dataMap.get("CARDNO").toString();
			//获取资产卡片的使用人 OUNAME
			String ouname = dataMap.get("OUNAME")==null?"":dataMap.get("OUNAME").toString();
			//获取归还原因
			String memo = dataMap.get("MEMO")==null?"无":dataMap.get("MEMO").toString();
			//获取操作系统
			String system = dataMap.get("SYSTEN")==null?"无":dataMap.get("SYSTEN").toString();
			//获取CPU
			String cpu = dataMap.get("CPU")==null?"无":dataMap.get("CPU").toString();
			//获取内存
			String memory = dataMap.get("MEMORY")==null?"无":dataMap.get("MEMORY").toString();
			//获取硬盘
			String harddisk = dataMap.get("HARDDISK")==null?"无":dataMap.get("HARDDISK").toString();
			
            //添加查询条件
			newHash.put("NO", no);
			List<HashMap> list=DemAPI.getInstance().getList(StatusConstant.ZYKP_UUID, newHash, null);
			HashMap hash=list.get(0);
		
			//获取其中需要的值
			Long dateid = Long.parseLong(hash.get("ID").toString());//修改时要用的参数
			Long Instanceid = Long.parseLong(hash.get("INSTANCEID").toString());//当前资产卡片的实例ID				
			//获取当前系统时间
			String changeDate = UtilDate.getNowdate();
			//修改资产卡片的操作
			hash.put("CHANGEDATE",changeDate);	//时间改为当前系统时间
			hash.put("STATUS", StatusConstant.ZYKP_GLZTYSY);//资产卡片状态改为已使用	
			hash.put("EXT1",system);	//操作系统 更新为申请单中的数据
			hash.put("EXT2",cpu);		//CPU更新为申请单中的数据
			hash.put("EXT3",memory);	//内存更新为申请单中的数据
			hash.put("EXT4",harddisk);	//硬盘更新为申请单中的数据
			
			//修改资产卡片状态为已使用
			flag = DemAPI.getInstance().updateFormData(StatusConstant.ZYKP_UUID, Instanceid, hash, dateid, false);
			
			//周期跟踪 —— 这是子表，需要subformkey这个参数
			List<HashMap> zczqlist = new ArrayList<HashMap>();
			HashMap zczqMap = new HashMap();
			//动作类型 —— 卡片登记为已增强
			zczqMap.put("ACTIONTYPE", StatusConstant.ZYKP_GLZTYZQ);//已增强
			//日期 —— 获取当前系统时间
			String date = UtilDate.getNowdate();
			zczqMap.put("RQ", date);
			//单位名称 —— 获取单位名称
			zczqMap.put("OCOMPANY", dataMap.get("OCNAME").toString());
			//部门名称
			zczqMap.put("ODEPARTMENT", dataMap.get("ODNAME").toString());
			//拥有者
			zczqMap.put("OUSERNAME", ouname);
			//经手人
			zczqMap.put("PROCESSMAN", ouname);
			//记录   —— 增强说明
			zczqMap.put("MEMO", "增强说明："+memo);
			//获取流程istanceid
			zczqMap.put("LCSLID",instanceid);
			//获取流程actdefid
			zczqMap.put("ACTDEFID",actDefId);
			//放入list
			zczqlist.add(zczqMap);
			//添加卡片记录
			flag=DemAPI.getInstance().saveFormDatas(StatusConstant.ZYKP_UUID, Long.parseLong(hash.get("INSTANCEID").toString()), zckpsubformkey, zczqlist, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		}

		//设置审批状态
		dataMap.put("STATUS", StatusConstant.PROCESS_STATUS_YIGUIDANG);//审批状态：审批中-已归档
		//修改状态！
		flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceid, dataMap, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		return flag;
	}
	
	

}
