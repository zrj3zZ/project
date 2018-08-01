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
 * 修改归还业务流程状态(审批中-已归档),使用人清空，更改资产卡片状态为空闲；
 * 在流程触发器中配置
 * @author zhangtian
 *
 */
public class AssetReturnProcessStriggerEvent extends ProcessTriggerEvent{
	public static final String zckpsubformkey="SUBFORM_GDZCZQGZBD";//固定资产卡片subformkey
	public AssetReturnProcessStriggerEvent(UserContext uc, HashMap hash) {
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
		SysDemEngine sde = DemAPI.getInstance().getDemModel(StatusConstant.ZYKP_UUID);//根据UUID，是在StatusConstant中封装的常量；主数据相对应的位置有自动生成的访问路径，=后面的就是所需要的UUID
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		
		//获取newInstanceId和Demid (通过这两个参数，得到newInstanceId)
		Long newInstanceId = DemAPI.getInstance().newInstance(StatusConstant.ZYKP_UUID, uc.get_userModel().getUserid());
		//查询数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		//获取行标识
		Long dataid = Long.parseLong(dataMap.get("ID").toString());
		
		/** 以下要实现的业务是将固定资产归还流程中的详细信息插入到资产卡片中   **/
		if (dataMap!=null) {
			//获取资产卡片编号 NO
			String no = dataMap.get("CARDNO")==null?"":dataMap.get("CARDNO").toString();
			//获取资产卡片的使用人 OUNAME
			String ouname = dataMap.get("OUNAME")==null?"":dataMap.get("OUNAME").toString();
			//获取归还原因
			String memo = dataMap.get("MEMO")==null?"无":dataMap.get("MEMO").toString();

            //添加查询条件
			newHash.put("NO", no);	
			List<HashMap> list=DemAPI.getInstance().getList(StatusConstant.ZYKP_UUID, newHash, null);	
			HashMap hash=list.get(0);//此时，我知道list中有一行数据，就可以不用遍历了，get(0)就是取到了它hashMap的值
		
			//获取其中需要的值
			Long dateid = Long.parseLong(hash.get("ID").toString());//修改时要用的参数
			Long Instanceid = Long.parseLong(hash.get("INSTANCEID").toString());//当前资产卡片的实例ID				
			//获取当前系统时间
			String changeDate = UtilDate.getNowdate();
			//修改的操作，把需要修改的内容放入相应的字段
			hash.put("CHANGEDATE",changeDate);	//时间改为当前系统时间
			hash.put("STATUS", StatusConstant.ZYKP_GLZTKX);//资产卡片状态改为空闲	
			hash.put("OUNAME","");//资产卡片拥有者OUNAME清空
			hash.put("OUID","");//资产卡片拥有者OUID清空，(这样，我的资产卡片中就不会在有这条信息)
			
			//修改实现的方法，资产卡片状态为空闲，拥有者清空
			flag = DemAPI.getInstance().updateFormData(StatusConstant.ZYKP_UUID, Instanceid, hash, dateid, false);
			
			//周期跟踪 —— 这是子表，需要subformkey这个参数
			List<HashMap> zcghlist = new ArrayList<HashMap>();
			HashMap zcghMap = new HashMap();
			//动作类型 —— 卡片登记为已归还
			zcghMap.put("ACTIONTYPE", StatusConstant.ZYKP_GLZTYGH);//已归还
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
			//记录   —— 归还原因
			zcghMap.put("MEMO", "归还原因："+memo);
			//获取流程istanceid
			zcghMap.put("LCSLID",instanceid);
			//获取流程actdefid
			zcghMap.put("ACTDEFID",actDefId);
			//放入list
			zcghlist.add(zcghMap);
			//添加卡片记录
			flag=DemAPI.getInstance().saveFormDatas(StatusConstant.ZYKP_UUID, Long.parseLong(hash.get("INSTANCEID").toString()), zckpsubformkey, zcghlist, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		}

		//设置审批状态
		dataMap.put("STATUS", StatusConstant.PROCESS_STATUS_YIGUIDANG);//审批状态：审批中-已归档
		//修改状态！(通过上面一系列参数，修改状态)
		flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceid, dataMap, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		return flag;
	}
	
	

}
