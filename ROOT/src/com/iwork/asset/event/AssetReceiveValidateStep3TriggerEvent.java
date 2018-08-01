package com.iwork.asset.event;




import java.util.HashMap;
import java.util.List;

import com.iwork.asset.constant.StatusConstant;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.model.SysDemEngine;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 
 * 归档触发器
 * 验证子表是否存在相同的固定资产卡片编号
 * 在流程触发器中配置
 * @author yanglianfeng
 *
 */
public class AssetReceiveValidateStep3TriggerEvent extends ProcessStepTriggerEvent {
	
	public static final String subformkey="SUBFORM_GDZCLYSQZBD";//固定资产领用subformkey
	public AssetReceiveValidateStep3TriggerEvent(UserContext uc, HashMap<String,Object> hash) {
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
		
		// 查询数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		// 获取行标识
		Long dataid = Long.parseLong(dataMap.get("ID").toString());
		//获取子表的数据
		List<HashMap>  subList=ProcessAPI.getInstance().getFromSubData(instanceid, subformkey);
		
		
		//判断子表中存在相同的记录数，如果存在相同的记录则给予提示并返回false
		if(subList!=null && subList.size()>0){
			for(int i=0;i<subList.size();i++){
				int sum = 0;
				HashMap<String,Object> subHash = subList.get(i);
				//资产卡片编号
				String no=subHash.get("NO")==null?"":subHash.get("NO").toString();
				if("".equals(no)){
					flag = false;
					MessageQueueUtil.getInstance().putAlertMsg("对不起，请分配指定用户固定资产！");
					break;
				}
				for(int j=0;j<subList.size();j++){
					HashMap<String,Object> subMap2 = subList.get(j);
					String no1 = subMap2.get("NO")==null?"":subMap2.get("NO").toString();
					if(no.equals(no1)){
						sum++;
					}
				}
				if(sum>1){
					flag = false;
					MessageQueueUtil.getInstance().putAlertMsg("对不起，同一张资产卡片不能分配给多人！");
					break;
				}else{
				    flag = true;
				}
			}
		}else{
			flag = true;
		}
		return flag;
	}
}