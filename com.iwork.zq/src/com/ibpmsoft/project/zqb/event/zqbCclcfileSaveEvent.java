package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbCclcfileSaveEvent extends ProcessTriggerEvent {
	public zqbCclcfileSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}

	private static final String CN_FILENAME = "/common.properties"; // 抓取网站配置文件

	public String getConfigUUID(String parameter) {
		String config = ConfigUtil.readValue(CN_FILENAME, parameter);
		return config;
	}

	@SuppressWarnings("unchecked")
	public boolean execute() {

		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId,
				EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			// 获取当前日期时间
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
//			String nowdatetime = df.format(new Date());
			// 获得当前办理人id

			dataMap.put("ZT", "审批通过");
//			dataMap.put("GDSJ", nowdatetime);
			dataMap.put("LCBH", this.getActDefId());
			dataMap.put("LZWZ", this.getExcutionId());
			dataMap.put("RWID", this.getTaskId());

			List<ProcessRuOpinion> pro = ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(),
					this.getExcutionId());
			if (pro != null & pro.size() > 0) {
				Long prc = pro.get(0).getPrcDefId();
				dataMap.put("LCBS", prc);
			}
			String processActDefId = this.getActDefId();
			Long lcDataId = Long.parseLong(dataMap.get("ID").toString());
//			String DYZLNR = dataMap.get("DYZLNR").toString();
			flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false,
					EngineConstants.SYS_INSTANCE_TYPE_PROCESS);

	if(flag){
		String qssj=dataMap.get("QSSJ")==null?"":dataMap.get("QSSJ").toString();
		String jssj=dataMap.get("JSSJ")==null?"":dataMap.get("JSSJ").toString();
		if(!"".equals(qssj)){
			qssj=StringUtils.replace(qssj," ","日");
//			int i=qssj.indexOf(":");
//			qssj=qssj.substring(0,i)+"点";
		}else{
			qssj="未知时间";
		}		
		if(!"".equals(jssj)){
			jssj=StringUtils.replace(jssj," ","日");
//			int y=jssj.indexOf(":");
//			jssj=jssj.substring(0,y)+"点";
			
		}else{
			jssj="未知时间";
		}
		
				String lx = dataMap.get("BSLX").toString();
				
				String content=null;
				if("出差".equals(lx)){
					
					 content = "出差:您因"+dataMap.get("CCSY").toString()+"申请"+qssj+"日至"+jssj+"日到"+dataMap.get("CCMDD").toString()+"出差，审核通过";
				}else{
					 content = "外出:您因"+dataMap.get("CCSY").toString()+"申请"+qssj+"日至"+jssj+"日外出，审核通过";
					 
				}
				String lcbh=this.getActDefId();
				lcbh=lcbh.substring(0, 2);
				
				String title = dataMap.get("YHM").toString() +dataMap.get("QSSJ").toString()+lx+ "，特此通知！";
				if("吕红贞".equals(uc._userModel.getUsername())){
					
					
					}else{
						if(lcbh.toLowerCase().equals("cc")){
							
						}else{
							ProcessAPI.getInstance().sendProcessCC(this.getActDefId(), "step_955eb832-e757-d48a-908e-a7f2616a2159", this.getTaskId().toString(), instanceId, this.getExcutionId(), title, "LVHONGZHEN[吕红贞]");
						}
					}
				//获得目标节点信息
				Map params = new HashMap();
				params.put(1, dataMap.get("YHID"));
				String targetUserid = dataMap.get("YHID").toString();
				
				UserContext user =  UserContextUtil.getInstance().getUserContext("ZHAOWEI");
				String userm=user.get_userModel().getMobile();
				if(!uc.get_userModel().getUsername().equals(user.get_userModel().getUsername())){
					String content1=null;
					if(userm!=null&&!"".equals(userm)){
					if("出差".equals(lx)){
						content1 =  "出差:"+dataMap.get("YHM").toString()+"因"+dataMap.get("CCSY").toString()+"申请"+qssj+"日至"+jssj+"日到"+dataMap.get("CCMDD").toString()+"出差，审核通过";
						
					}else{
						
						content1 =  "外出:"+dataMap.get("YHM").toString()+"因"+dataMap.get("CCSY").toString()+"申请"+qssj+"日至"+jssj+"日外出，审核通过";
					}
					
					MessageAPI.getInstance().sendSMS(uc, userm, content1.toString());
					}
				}
				UserContext target = UserContextUtil.getInstance().getUserContext(targetUserid);
				if (target != null) {
					if (!content.equals("")) {
						//发送短信
						String mobile = target.get_userModel().getMobile();
						if (mobile != null && !mobile.equals("")) {
							mobile = target.get_userModel().getMobile();
							MessageAPI.getInstance().sendSMS(uc, mobile, content.toString());
						}
						//发送邮件
						String email = target.get_userModel().getEmail();
						if (email != null && !email.equals("")) {
							String senduser = target.get_userModel().getUsername();
							MessageAPI.getInstance().sendSysMail(senduser, email, lx, content.toString());
						}
					}
				}
				
			
				
			

			}
		}
		
		return flag;
	}
}
