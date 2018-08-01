package com.ibpmsoft.project.zqb.event;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 流程撤销时触发
 * @author zouyalei
 *
 */
public class zqbThxmRevokeEvent extends ProcessTriggerEvent {

	public zqbThxmRevokeEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}

	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件     

	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	
	public boolean execute() {
		boolean flag = false;
		//判断当前撤销节点是否为审核人节点
		String fhrjd = this.getConfigUUID("fhrjd").toString();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap conditionMap=new HashMap();
		if(dataMap!=null){
			//获取当前日期时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String  nowdatetime = df.format(new Date());
			
			//根据项目名称判断是否存在，已经存在，不存在保存，存在更新
			String sxmc = dataMap.get("XMMC").toString();
				conditionMap.put("XMMC", sxmc);
				List<HashMap> lcbsexists=DemAPI.getInstance().getList(this.getConfigUUID("thxmuuid"), conditionMap, null);
				if(lcbsexists!=null&&lcbsexists.size()==1){
					//更新物理表数据
					for(HashMap<String,Object> hash:lcbsexists){
						hash.put("QCRXM", dataMap.get("QCRXM")==null?"":dataMap.get("QCRXM").toString());
						hash.put("QCRID", dataMap.get("QCRID")==null?"":dataMap.get("QCRID").toString());
						hash.put("XMMC", dataMap.get("XMMC")==null?"":dataMap.get("XMMC").toString().trim());
						hash.put("XMGJJD", dataMap.get("XMGJJD")==null?"":dataMap.get("XMGJJD").toString());
						hash.put("FQRQ", dataMap.get("FQRQ")==null?"":dataMap.get("FQRQ").toString());
						hash.put("XMSCBK", dataMap.get("XMSCBK")==null?"":dataMap.get("XMSCBK").toString());
						hash.put("RZXQMS", dataMap.get("RZXQMS")==null?"":dataMap.get("RZXQMS").toString());
						hash.put("SFGP", dataMap.get("SFGP")==null?"":dataMap.get("SFGP").toString());
						hash.put("SBSJ", dataMap.get("SBSJ")==null?"":dataMap.get("SBSJ").toString());
						hash.put("XMMS", dataMap.get("XMMS")==null?"":dataMap.get("XMMS").toString());
						hash.put("FJSC", dataMap.get("FJSC")==null?"":dataMap.get("FJSC").toString());
						hash.put("EXTEND1", dataMap.get("EXTEND1")==null?"":dataMap.get("EXTEND1").toString());
						hash.put("EXTEND2", dataMap.get("EXTEND2")==null?"":dataMap.get("EXTEND2").toString());
						hash.put("EXTEND3", dataMap.get("EXTEND3")==null?"":dataMap.get("EXTEND3").toString());
						hash.put("EXTEND4", dataMap.get("EXTEND4")==null?"":dataMap.get("EXTEND4").toString());
						hash.put("EXTEND5", dataMap.get("EXTEND5")==null?"":dataMap.get("EXTEND5").toString());
						if(fhrjd.equals(hash.get("STEPTID"))){
						dataMap.put("ZBSCBJBR", "");
						hash.put("ZBSCBJBR", "");
						}
						hash.put("SPZT", "跟进中");
						hash.put("LCBH", this.getActDefId());
						hash.put("YXID", this.getExcutionId());
						hash.put("TASKID", this.getTaskId());
						List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(), this.getExcutionId());
						 if(pro!=null&pro.size()>0){
						    	Long prc=pro.get(0).getPrcDefId();
						    	hash.put("PRCID", prc);
						    }
						hash.put("LCINSTANCEID", this.getInstanceId());
						//更新流程表数据
						ProcessAPI.getInstance().updateFormData(this.getActDefId(), this.getInstanceId(), dataMap, (Long) dataMap.get("ID"), false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
						flag = DemAPI.getInstance().updateFormData(this.getConfigUUID("thxmuuid"), Long.parseLong(hash.get("INSTANCEID").toString()), hash, Long.parseLong(hash.get("ID").toString()), false);
						String value=hash.get("XMMC")==null?"":hash.get("XMMC").toString();
						Long dataId=Long.parseLong(hash.get("ID").toString());
						LogUtil.getInstance().addLog(dataId, "投行项目流程管理", "流程："+value+"---被撤销！");
				}
			}
		}
		return flag;
	}

}
