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
 * 节点触发器
 * 
 * 修改归还业务流程状态(起草-审批中),更改资产卡片状态为归还中；
 * 在流程触发器中配置
 * @author zhangtian
 *
 */
public class AssetReturnStatusModifyStriggerEvent extends ProcessStepTriggerEvent{
	public static final String zckpsubformkey="SUBFORM_GDZCZQGZBD";//固定资产卡片subformkey
	public AssetReturnStatusModifyStriggerEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public boolean execute(){
		
		boolean flag = false;
		HashMap newHash = new HashMap();
		//获取流程实例ID  (this.可以直接获取方法)
		Long instanceid = this.getInstanceId();
		//获取流程唯一标识
		String actDefId = this.getActDefId();
		//查询数据  (后面参数是固定的，流程、主数据、报表一一对应)
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		//获取行标识 ,即其所对应的ID (把它变成字符输出，再强转成Long型;当确定字符不是空时，才可以强转)
		Long dataid = Long.parseLong(dataMap.get("ID").toString());
		
		/** 以下要实现的业务是将固定资产归还流程中的详细信息插入到资产卡片中   **/
		if(dataMap!=null){
			//获取资产卡片编号 NO
			String no = dataMap.get("CARDNO")==null?"":dataMap.get("CARDNO").toString();
			//获取资产卡片的使用人 OUNAME
			String ouname = dataMap.get("OUNAME")==null?"":dataMap.get("OUNAME").toString();
            //添加查询条件
			newHash.put("NO", no);
			//这是从主数据中，根据资产卡片编号获取值
			List<HashMap> list=DemAPI.getInstance().getList(StatusConstant.ZYKP_UUID, newHash, null);
			HashMap hash=list.get(0);//返回HashMap的值		
			
			//获取其中需要的值
			Long dateid = Long.parseLong(hash.get("ID").toString());//修改时要用的参数
			Long Instanceid = Long.parseLong(hash.get("INSTANCEID").toString());//当前资产卡片的实例ID				
			//获取当前系统时间
			String changeDate = UtilDate.getNowdate();
			//修改的操作，把需要修改的内容放入相应的字段
			hash.put("CHANGEDATE",changeDate);	//时间改为当前系统时间
			hash.put("STATUS", StatusConstant.ZYKP_GLZTGHZ);//资产卡片状态改为归还中
			
			//修改实现的方法，资产卡片状态为归还中
			flag = DemAPI.getInstance().updateFormData(StatusConstant.ZYKP_UUID, Instanceid, hash, dateid, false);
			
			//周期跟踪 —— 这是子表，需要subformkey这个参数
			List<HashMap> zcghlist = new ArrayList<HashMap>();
			HashMap zcghMap = new HashMap();
			//动作类型 —— 卡片登记为归还中
			zcghMap.put("ACTIONTYPE", StatusConstant.ZYKP_GLZTGHZ);//归还中
			//日期 —— 获取当前系统时间
			String date = UtilDate.getNowdate();
			zcghMap.put("RQ", date);
			//单位名称 —— 获取单位名称
			zcghMap.put("OCOMPANY", dataMap.get("OCNAME").toString());
			//部门名称
			zcghMap.put("ODEPARTMENT", dataMap.get("ODNAME").toString());
			//拥有者
			zcghMap.put("OUSERNAME", ouname);
			//经手人
			zcghMap.put("PROCESSMAN", ouname);
			//获取流程istanceid
			zcghMap.put("LCSLID",instanceid);
			//获取流程actdefid
			zcghMap.put("ACTDEFID",actDefId);
			//放入list
			zcghlist.add(zcghMap);
			//添加卡片记录
			flag=DemAPI.getInstance().saveFormDatas(StatusConstant.ZYKP_UUID, Long.parseLong(hash.get("INSTANCEID").toString()), zckpsubformkey, zcghlist, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		}
		
		//设置审批状态  (把状态-审批中放入dataMap中)
		dataMap.put("STATUS", StatusConstant.PROCESS_STATUS_SHENPIZHONG);
		//修改状态！(通过上面一系列参数，修改状态)
		flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceid, dataMap, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		return flag;
	}
	
}