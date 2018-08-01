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
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 董秘发起流程发送后触发
 * @author zouyalei
 *
 */
public class zqbThxmAfterSaveEvent extends ProcessStepTriggerEvent {

	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件     
	public zqbThxmAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}

	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	
	public boolean execute() {
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap conditionMap=new HashMap();
		if(dataMap!=null){
			//获取当前日期时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String  nowdatetime = df.format(new Date());
			//获得当前办理人id
			String userid = uc.get_userModel().getUserid();
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
						if( dataMap.get("FQRQ")!=null && !"".equals(dataMap.get("FQRQ"))){
							hash.put("FQRQ", dataMap.get("FQRQ")==null?"":dataMap.get("FQRQ").toString());
						}
						
						hash.put("XMSCBK", dataMap.get("XMSCBK")==null?"":dataMap.get("XMSCBK").toString());
						hash.put("RZXQMS", dataMap.get("RZXQMS")==null?"":dataMap.get("RZXQMS").toString());
						hash.put("SFGP", dataMap.get("SFGP")==null?"":dataMap.get("SFGP").toString());
						if( dataMap.get("SBSJ")!=null && !"".equals(dataMap.get("SBSJ"))){
							hash.put("SBSJ", dataMap.get("SBSJ")==null?"":dataMap.get("SBSJ").toString());
						}
						
						hash.put("XMMS", dataMap.get("XMMS")==null?"":dataMap.get("XMMS").toString());
						hash.put("FJSC", dataMap.get("FJSC")==null?"":dataMap.get("FJSC").toString());
						hash.put("EXTEND1", dataMap.get("EXTEND1")==null?"":dataMap.get("EXTEND1").toString());
						hash.put("EXTEND2", dataMap.get("EXTEND2")==null?"":dataMap.get("EXTEND2").toString());
						hash.put("EXTEND3", dataMap.get("EXTEND3")==null?"":dataMap.get("EXTEND3").toString());
						hash.put("EXTEND4", dataMap.get("EXTEND4")==null?"":dataMap.get("EXTEND4").toString());
						hash.put("EXTEND5", dataMap.get("EXTEND5")==null?"":dataMap.get("EXTEND5").toString());
						
						
						hash.put("SPZT", "跟进中");
						hash.put("LCBH", this.getActDefId());
						hash.put("STEPTID", this.getActStepId());
						hash.put("YXID", this.getExcutionId());
						hash.put("TASKID", this.getTaskId());
						List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(), this.getExcutionId());
						 if(pro!=null&pro.size()>0){
						    	Long prc=pro.get(0).getPrcDefId();
						    	hash.put("PRCID", prc);
						    }
						hash.put("LCINSTANCEID", this.getInstanceId());
						
						flag = DemAPI.getInstance().updateFormData(this.getConfigUUID("thxmuuid"), Long.parseLong(hash.get("INSTANCEID").toString()), hash, Long.parseLong(hash.get("ID").toString()), false);
						String value=hash.get("XMMC")==null?"":hash.get("XMMC").toString();
						Long dataId=Long.parseLong(hash.get("ID").toString());
						LogUtil.getInstance().addLog(dataId, "投行项目流程管理", "提交流程："+value);
					}
				}else{
					Long instanceid = DemAPI.getInstance().newInstance(this.getConfigUUID("thxmuuid"), userid);
					
					HashMap<String,Object> hashdata = new HashMap<String,Object>();
					hashdata.put("QCRXM", dataMap.get("QCRXM")==null?"":dataMap.get("QCRXM").toString());
					hashdata.put("QCRID", dataMap.get("QCRID")==null?"":dataMap.get("QCRID").toString());
					hashdata.put("XMMC", dataMap.get("XMMC")==null?"":dataMap.get("XMMC").toString().trim());
					hashdata.put("XMGJJD", dataMap.get("XMGJJD")==null?"":dataMap.get("XMGJJD").toString());
					hashdata.put("FQRQ", dataMap.get("FQRQ")==null?"":dataMap.get("FQRQ").toString());
					hashdata.put("XMSCBK", dataMap.get("XMSCBK")==null?"":dataMap.get("XMSCBK").toString());
					hashdata.put("RZXQMS", dataMap.get("RZXQMS")==null?"":dataMap.get("RZXQMS").toString());
					hashdata.put("SFGP", dataMap.get("SFGP")==null?"":dataMap.get("SFGP").toString());
					hashdata.put("SBSJ", dataMap.get("SBSJ")==null?"":dataMap.get("SBSJ").toString());
					hashdata.put("XMMS", dataMap.get("XMMS")==null?"":dataMap.get("XMMS").toString());
					hashdata.put("FJSC", dataMap.get("FJSC")==null?"":dataMap.get("FJSC").toString());
					hashdata.put("EXTEND1", dataMap.get("EXTEND1")==null?"":dataMap.get("EXTEND1").toString());
					hashdata.put("EXTEND2", dataMap.get("EXTEND2")==null?"":dataMap.get("EXTEND2").toString());
					hashdata.put("EXTEND3", dataMap.get("EXTEND3")==null?"":dataMap.get("EXTEND3").toString());
					hashdata.put("EXTEND4", dataMap.get("EXTEND4")==null?"":dataMap.get("EXTEND4").toString());
					hashdata.put("EXTEND5", dataMap.get("EXTEND5")==null?"":dataMap.get("EXTEND5").toString());
					
					
					hashdata.put("SPZT", "跟进中");
					hashdata.put("LCBH", this.getActDefId());
					hashdata.put("STEPTID", this.getActStepId());
					hashdata.put("YXID", this.getExcutionId());
					hashdata.put("TASKID", this.getTaskId());
					hashdata.put("LCINSTANCEID", this.getInstanceId());
					List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(), this.getExcutionId());
					 if(pro!=null&pro.size()>0){
					    	Long prc=pro.get(0).getPrcDefId();
					    	hashdata.put("PRCID", prc);
					    }
					
					flag = DemAPI.getInstance().saveFormData(this.getConfigUUID("thxmuuid"), instanceid, hashdata, false);
					HashMap map=new HashMap();
					map.put("XMMC", sxmc);
					HashMap hashMap = DemAPI.getInstance().getList(this.getConfigUUID("thxmuuid"), map, null).get(0);
					String value=hashMap.get("XMMC")==null?"":hashMap.get("XMMC").toString();
					Long dataId=Long.parseLong(hashMap.get("ID").toString());
					LogUtil.getInstance().addLog(dataId, "投行项目流程管理", "创建流程："+value);
				}
			String mailContent = "";
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("XMMC",dataMap.get("XMMC") == null ? "" : dataMap.get("XMMC"));
			//编辑短信内容
			mailContent =uc.get_userModel().getUsername() + "提交了："+dataMap.get("XMMC").toString()+"的流程，请求审核！";
			//发送短信
			String assignee = ProcessAPI.getInstance().newTaskId(instanceId).getAssignee();
			String username = "";
			String noticename = (dataMap.get("XMMC").toString()==null||dataMap.get("XMMC").equals(""))?"项目名称：空。":"项目名称："+dataMap.get("XMMC").toString()+"。";
			UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
			if (target != null) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("") ) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile, mailContent);
					}
					String email = target.get_userModel().getEmail();
    				if(email!=null&&!email.equals("") && uc != null){
    					username = uc.get_userModel().getUsername();
    					MessageAPI.getInstance().sendSysMail(username, email, "投行项目流程审批提醒", mailContent+"<br>"+noticename);
    				}
			}
		}
		return flag;
	}

}
