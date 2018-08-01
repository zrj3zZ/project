package com.ibpmsoft.project.zqb.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;


import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.ProcessAPI;

public class zqbCclcBeforeOnloadEvent extends ProcessStepTriggerEvent{

	public zqbCclcBeforeOnloadEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}
	public boolean execute() {
	boolean flag = false;
	UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
	Long instanceId = this.getInstanceId();
	// 获取流程表单数据
	HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
	if(dataMap!=null){
		//获取当前日期时间
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd日HH点");//设置日期格式
//	
//		String nowdatetime = df.format(new Date());
		//获得当前办理人id
		String qssj=dataMap.get("QSSJ")==null?"":dataMap.get("QSSJ").toString();
		String jssj=dataMap.get("JSSJ")==null?"":dataMap.get("JSSJ").toString();
		if("".equals(qssj)){
			
			
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Long qjsj=null;
		try {
			
			long time = sdf.parse(jssj).getTime();
			long time2 = sdf.parse(qssj).getTime();
			qjsj=(time-time2)/(1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dataMap.put("LCBH", this.getActDefId());
		dataMap.put("LZJD", this.getActStepId());
		dataMap.put("LZWZ",this.getExcutionId());
		dataMap.put("RWID", this.getTaskId());
		dataMap.put("EXTEND1", qjsj);
		String lcbh=this.getActDefId();
		lcbh=lcbh.substring(0, 2);
		if(lcbh.toLowerCase().equals("cc")){
			dataMap.put("BSLX", "出差");
		}else{
			dataMap.put("BSLX", "外出");
		}		
		List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(), this.getExcutionId());
		if(pro!=null&pro.size()>0){
			Long prc=pro.get(0).getPrcDefId();
			dataMap.put("LCBS", prc);
		 }
		String processActDefId = this.getActDefId();
		Long lcDataId=Long.parseLong(dataMap.get("ID").toString());
		flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
//		LogUtil.getInstance().addLog(Long.parseLong(dataMap.get("ID").toString()), "工作底稿调阅", "创建流程："+dataMap.get("DYZLNR").toString());
//			
//		String mailContent = "";
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("DYZLNR",dataMap.get("DYZLNR") == null ? "" : dataMap.get("DYZLNR"));
//		//编辑短信内容
//		mailContent =uc.get_userModel().getUsername() + "提交了："+dataMap.get("DYZLNR").toString()+"的流程，请求审核！";
//		//发送短信
//		String assignee = ProcessAPI.getInstance().newTaskId(instanceId).getAssignee();
//		String username = "";
//		String noticename = (dataMap.get("DYZLNR").toString()==null||dataMap.get("DYZLNR").equals(""))?"项目名称：空。":"项目名称："+dataMap.get("DYZLNR").toString()+"。";
//		UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
//		if (target != null) {
//				String mobile = target.get_userModel().getMobile();
//				if (mobile != null && !mobile.equals("") ) {
//					mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
//					MessageAPI.getInstance().sendSMS(uc, mobile, mailContent);
//				}
//				String email = target.get_userModel().getEmail();
//				if(email!=null&&!email.equals("") && uc != null){
//					username = uc.get_userModel().getUsername();
//					MessageAPI.getInstance().sendSysMail(username, email, "工作底稿调阅审批提醒", mailContent+"<br>"+noticename);
//				}
//		}
	}
	return flag;
	}
}
