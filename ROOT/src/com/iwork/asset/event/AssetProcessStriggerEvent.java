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
import com.iwork.core.util.SequenceUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 公共触发器
 * 归档触发器
 * 修改流程状态(审批中-已归档)
 * 在流程触发器中配置
 * @author yanglianfeng
 *
 */
public class AssetProcessStriggerEvent extends ProcessTriggerEvent {
	public static final String subformkey="SUBFORM_GDZCRKZB";//固定资产入库subformkey
	public static final String zckpsubformkey="SUBFORM_GDZCZQGZBD";//固定资产卡片subformkey
	public AssetProcessStriggerEvent(UserContext uc, HashMap<String,Object> hash) {
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
		// 查询数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		//获取子表的数据
		List<HashMap>  subList=ProcessAPI.getInstance().getFromSubData(instanceid, subformkey);
		//根据表单模型取出表单instanceId和demid
		SysDemEngine sde=DemAPI.getInstance().getDemModel(StatusConstant.ZYKP_UUID);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		//获取newInstanceId与Demid
		//Long newIncetanceId=DemAPI.getInstance().newInstance(StatusConstant.ZYKP_UUID, uc.get_userModel().getUserid());
		// 获取行标识
		Long dataid = Long.parseLong(dataMap.get("ID").toString());
		
		/**以下要实现的业务是将入库流程中的详细信息插入到资源卡片中  2016-1-12-**/
		
		if(dataMap!=null){
			//获取入库单编号
			String bno=dataMap.get("BNO")==null?"":dataMap.get("BNO").toString();
			//获取资产类别编号
			String categoryno=dataMap.get("CATEGORYNO")==null?"":dataMap.get("CATEGORYNO").toString();
			//获取资产类别名称
			String category=dataMap.get("CATEGORY")==null?"":dataMap.get("CATEGORY").toString();
			//获取基础信息
			//获取仓库编号
			String warehouseno=dataMap.get("WAREHOUSENO")==null?"":dataMap.get("WAREHOUSENO").toString();
			//获取仓库名称
			String warehousename=dataMap.get("WAREHOUSESNAME")==null?"":dataMap.get("WAREHOUSESNAME").toString();
			//获取供应商编号
			String vendorno=dataMap.get("VENDORNO")==null?"":dataMap.get("VENDORNO").toString();
			//获取供应商名称
			String vendorname=dataMap.get("VENDORNAME")==null?"":dataMap.get("VENDORNAME").toString();
			//获取立卡日期
			String appydate=dataMap.get("APPLYDATE")==null?"":dataMap.get("APPLYDATE").toString();
			//获取管理状态
			String glzt=dataMap.get("STATUS")==null?"":dataMap.get("STATUS").toString();
			//获取资产用途
			String zcyt=dataMap.get("USAGEINFO")==null?"":dataMap.get("USAGEINFO").toString();
			//将主表单的数据添加至新的map
			newHash.put("STORAGENO", bno);//获取入库单号
			newHash.put("CATEGORYNO", categoryno);//类别编号
			newHash.put("CATEGORY", category);//类别名称
			newHash.put("WAREHOUSENO", warehouseno);//仓库编号
			newHash.put("WAREHOUSESNAME", warehousename);//仓库名称
			newHash.put("VENDORNO", vendorno);//供应商编号
			newHash.put("VENDORNAME", vendorname);//供应商名称
			newHash.put("CARDDATE", appydate);//立卡日期
			newHash.put("USAGEINFO", zcyt);//资产用途
		}
		
		//遍历固定资源卡记录
		if(subList!=null && subList.size()>0){
			for(HashMap subHash:subList){
				//资产编号
				String no=subHash.get("NO")==null?"":subHash.get("NO").toString();
				//资产名称
				String name=subHash.get("NAME")==null?"":subHash.get("NAME").toString();
				//规格
				String specification=subHash.get("SPECIFICATION")==null?"":subHash.get("SPECIFICATION").toString();
				//型号
				String models=subHash.get("MODELS")==null?"":subHash.get("MODELS").toString();
				//单位
				String unit=subHash.get("UNIT")==null?"":subHash.get("UNIT").toString();
				//获取主要配置
				String configure=subHash.get("CONFIGURE")==null?"":subHash.get("CONFIGURE").toString();
				//获取资产介绍
				String description=subHash.get("DESCRIPTION")==null?"":subHash.get("DESCRIPTION").toString();
				//单价
				Long price=subHash.get("PRICE")==null?0l:Long.parseLong(subHash.get("PRICE").toString());
				//获取数量
				Long quantity=subHash.get("QUANTITY")==null?0l:Long.parseLong(subHash.get("QUANTITY").toString());
				//获取制造商
				String zzs=subHash.get("MANUFACTURER")==null?"":subHash.get("MANUFACTURER").toString();
				//原值//使用年限//所有者//状态变动日期//获取制造商暂无
				
				newHash.put("NAME", name);//资产名称
				newHash.put("SPECIFICATION", specification);//规格
				newHash.put("MODELS", models);//型号
				newHash.put("UNIT", unit);//单位
				newHash.put("CONFIGURE", configure);//获取主要配置
				newHash.put("DESCRIPTION", description);//获取资产介绍
				newHash.put("PRICE", price);//单价
				newHash.put("MANUFACTURER", zzs);//制造商
				int num = SequenceUtil.getInstance().getSequenceIndex("GDZCKP");
				String zcbh = String.valueOf(num); 
				String nums=no+"-"+zcbh;
				//获取当前系统时间
				String date = UtilDate.getNowdate();
				HashMap subMap=new HashMap();
				HashMap conditionMap=new HashMap();
				if(quantity>1){
					for(int i=0;i<quantity;i++){
						zcbh = String.valueOf(num); 
						nums=no+"-"+zcbh;
						List<HashMap> list=new ArrayList<HashMap>();
						Long newIncetanceId=DemAPI.getInstance().newInstance(StatusConstant.ZYKP_UUID, uc.get_userModel().getUserid());
					    num = SequenceUtil.getInstance().getSequenceIndex("GDZCKP");
					    newHash.put("STATUS", StatusConstant.ZYKP_GLZTKX);//管理状态
					    newHash.put("CHANGEDATE", date);//状态更改日期
						newHash.put("NO", nums);//资产卡片编号
						//如果固定资产的数量大于1那么将生成相应的资源卡片
						flag=DemAPI.getInstance().saveFormData(StatusConstant.ZYKP_UUID, newIncetanceId, newHash, false);
						//操作
						subMap.put("ACTIONTYPE", StatusConstant.ZYKP_GLZTKPDJ);
						subMap.put("RQ", date);
						//获取部门
						subMap.put("ODEPARTMENT", dataMap.get("DPTNAME").toString());
						//获取单位
						subMap.put("OCOMPANY", dataMap.get("COPNAME").toString());
						//获取经手人
						subMap.put("PROCESSMAN", dataMap.get("STAFFNAME").toString());
						//添加拥有者
						subMap.put("OUSERNAME", "无");
						//获取流程istanceid
						subMap.put("LCSLID",instanceid);
						//获取流程actdefid
						subMap.put("ACTDEFID",actDefId);
						list.add(subMap);
						DemAPI.getInstance().saveFormDatas(StatusConstant.ZYKP_UUID, newIncetanceId, zckpsubformkey, list, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
					}
				}else{ 
					    List<HashMap> list=new ArrayList<HashMap>();
					    Long newIncetanceId=DemAPI.getInstance().newInstance(StatusConstant.ZYKP_UUID, uc.get_userModel().getUserid());
					    newHash.put("NO", nums);//资产卡片编号
					    newHash.put("STATUS", StatusConstant.ZYKP_GLZTKX);//管理状态
					    newHash.put("CHANGEDATE", date);//状态更改日期
					    //如果固定资产的数量等于1将生成1张资源卡片
					    
					    flag=DemAPI.getInstance().saveFormData(StatusConstant.ZYKP_UUID, newIncetanceId, newHash, false);
					    
					    List<HashMap> list1=DemAPI.getInstance().getList(StatusConstant.ZYKP_UUID, conditionMap, null);
					   //操作
						subMap.put("ACTIONTYPE",StatusConstant.ZYKP_GLZTKPDJ);
						subMap.put("RQ", date);
						//获取部门
						subMap.put("ODEPARTMENT", dataMap.get("DPTNAME").toString());
						//获取单位
						subMap.put("OCOMPANY", dataMap.get("COPNAME").toString());
						//获取经手人
						subMap.put("PROCESSMAN", dataMap.get("STAFFNAME").toString());
						//添加拥有者
						subMap.put("OUSERNAME", "无");
						//获取流程istanceid
						subMap.put("LCSLID",instanceid);
						//获取流程actdefid
						subMap.put("ACTDEFID",actDefId);
						list.add(subMap);
						flag=DemAPI.getInstance().saveFormDatas(StatusConstant.ZYKP_UUID, newIncetanceId, zckpsubformkey, list, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
				}
			}
		}
	    
		// 设置审批状态
		dataMap.put("STATUS", StatusConstant.PROCESS_STATUS_YIGUIDANG);
		flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceid, dataMap, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		return flag;
	}
}