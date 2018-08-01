package com.ibpmsoft.project.zqb.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.ibpmsoft.project.zqb.service.ZqbCheckService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbCheckAction extends ActionSupport{

	private String userid;
	private String customerno;
	private String customername;
	private String password;
	private Long tzggid;
	private Long instanceid;
	private ZqbCheckService zqbCheckService;
	private String deptname;
	private Long deptid;
	private String projectno;
	private String jdmc;
	private String actDefId;
	private String actStepDefId;
	private Long instanceId;
	private String khbh;
	private String countdownscore;
	private Long id;
	private String xmbh;
	private Long prcDefId;
	private String taskId;
	private String type;
	private String memo;
	private String bkfr;
	private Long backType;
	private String receiveUser;
	
	public String getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}
	//短信类型
	private Long dxlx;

	public Long getDxlx() {
		return dxlx;
	}

	public void setDxlx(Long dxlx) {
		this.dxlx = dxlx;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMemo() {
		return memo;
	}

	public String getBkfr() {
		return bkfr;
	}

	public void setBkfr(String bkfr) {
		this.bkfr = bkfr;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getPrcDefId() {
		return prcDefId;
	}

	public void setPrcDefId(Long prcDefId) {
		this.prcDefId = prcDefId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Long getBackType() {
		return backType;
	}

	public void setBackType(Long backType) {
		this.backType = backType;
	}

	public String getCountdownscore() {
		return countdownscore;
	}

	public void setCountdownscore(String countdownscore) {
		this.countdownscore = countdownscore;
	}

	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	public String getActStepDefId() {
		return actStepDefId;
	}

	public void setActStepDefId(String actStepDefId) {
		this.actStepDefId = actStepDefId;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public String getJdmc() {
		return jdmc;
	}

	public void setJdmc(String jdmc) {
		this.jdmc = jdmc;
	}

	public String getProjectno() {
		return projectno;
	}

	public void setProjectno(String projectno) {
		this.projectno = projectno;
	}

	public Long getDeptid() {
		return deptid;
	}

	public void setDeptid(Long deptid) {
		this.deptid = deptid;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public Long getTzggid() {
		return tzggid;
	}

	public void setTzggid(Long tzggid) {
		this.tzggid = tzggid;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getXmbh() {
		return xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}
	public ZqbCheckService getZqbCheckService() {
		return zqbCheckService;
	}

	public void setZqbCheckService(ZqbCheckService zqbCheckService) {
		this.zqbCheckService = zqbCheckService;
	}

	public void checkOrguser(){
		String info=zqbCheckService.getCheckOrguser(userid);
		ResponseUtil.write(info);
	}
	
	public void checkProject(){
		String info=zqbCheckService.getcheckProject(customerno,customername);
		ResponseUtil.write(info);
	}
	
	public void checkVote(){
		String info=zqbCheckService.getcheckVote(customerno,tzggid,instanceid);
		ResponseUtil.write(info);
	}
	
	public void checkPwd(){
		String info=zqbCheckService.getcheckPwd(password,userid);
		ResponseUtil.write(info);
	}
	
	public void checkDeptName(){
		String info=zqbCheckService.getcheckDeptName(deptid,deptname);
		ResponseUtil.write(info);
	}
	
	public void checkProjectSubSP(){
		String info=zqbCheckService.getcheckProjectSubSP(instanceid);
		ResponseUtil.write(info);
	}
	
	public void checkProjectBgczSP(){
		String info=zqbCheckService.getcheckProjectBgczSP(instanceid);
		ResponseUtil.write(info);
	}
	
	public void checkLcProjectSubSP(){
		String info=zqbCheckService.getcheckLcProjectSubSP(projectno);
		ResponseUtil.write(info);
	}
	
	public void checkXmjd(){
		String info=zqbCheckService.getcheckXmjd(customerno,jdmc);
		ResponseUtil.write(info);
	}
	
	public void setLxxxReadonly(){
		String info=zqbCheckService.setLxxxReadonly(projectno);
		ResponseUtil.write(info);
	}
	
	public void checkLcgg(){
		String info=zqbCheckService.checkLcgg(actDefId,actStepDefId,instanceId,khbh);
		ResponseUtil.write(info);
	}
	public void checkyz(){
		String info=zqbCheckService.checkyz(instanceId);
		ResponseUtil.write(info);
	}
	public void checkRycx(){
		String info=zqbCheckService.checkRycx();
		ResponseUtil.write(info);
	}
	public void checkBgXmjd(){
		String info=zqbCheckService.getcheckBgXmjd(xmbh,jdmc);
		ResponseUtil.write(info);
	}
	
	public void checkCwXmjd(){
		String info=zqbCheckService.getcheckCwXmjd(xmbh,jdmc);
		ResponseUtil.write(info);
	}
	public void checkSfkfId(){
		String info=zqbCheckService.checkSfkfId(actDefId,actStepDefId);
		ResponseUtil.write(info);
	}
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件     
	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	//根据流程判断驳回短信
	public void obtainUserid(){
		String zqServer = getConfigUUID("zqServer");
		//公告审批流程
		//获取当前日期时间
		SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");//设置日期格式
		String  nowdatetime = df.format(new Date());
		if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
					instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String smsContent = "";
			String mailContent = "";
			String senduser = "";
			String noticename = (dataMap.get("NOTICENAME").toString()==null||dataMap.get("NOTICENAME").equals(""))?"公告标题：空。":"公告标题："+dataMap.get("NOTICENAME").toString()+"。";
			String ggzy = (dataMap.get("GGZY").toString()==null||dataMap.get("GGZY").equals(""))?"公告摘要：空。":"公告摘要："+dataMap.get("GGZY").toString()+"。";
			mailContent = dataMap.get("ZQJCXS")==null?dataMap.get("KHMC").toString():dataMap.get("ZQJCXS") + nowdatetime+ ","+dataMap.get("NOTICENAME").toString()+"，被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				senduser = tg.get_userModel().getUsername();
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile, mailContent);
					}
					String email = target.get_userModel().getEmail();
					String zqserver = getConfigUUID("zqServer")==null?"":getConfigUUID("zqServer");
					if(!zqserver.equals("xnzq")){
						if(email != null && !email.equals("")){
							MessageAPI.getInstance().sendSysMail(senduser, email, "公告驳回", mailContent+"<br>"+ggzy);
						}
				}
			}
		//日常业务呈报流程
		}else if(actDefId.startsWith("RCYWCB")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			// 获取流程表单数据
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
					instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if (dataMap != null) {
				String mailContent = "";
				mailContent = dataMap.get("KHMC").toString() + nowdatetime+","+dataMap.get("SXMC").toString()+"，被驳回！";
				UserContext target = UserContextUtil.getInstance().getUserContext(userid);
				UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
				if (target != null && tg != null) {
					String senduser = tg.get_userModel().getUsername();
					if (!mailContent.equals("")) {
						String mobile = target.get_userModel().getMobile();
						if (mobile != null && !mobile.equals("")) {
							mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
							MessageAPI.getInstance().sendSMS(uc, mobile, mailContent);
						}
						String zqserver = getConfigUUID("zqServer")==null?"":getConfigUUID("zqServer");
						if(!zqserver.equals("xnzq")){
							String email = target.get_userModel().getEmail();
							
							if(email != null && !email.equals("")){
								MessageAPI.getInstance().sendSysMail(senduser, email, "事项驳回", mailContent);
							}
						}
						
					}
				}
			}
		//财务工作进度汇报流程
		}else if(actDefId.startsWith("CWGZJDHB")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String customername = dataMap.get("COMPANYNAME").toString();
			String smsContent = customername+"一般财务工作进度汇报审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "一般财务工作进度汇报审核", smsContent);
					}
				}
			}
		//财务资料归档审批流程
		}else if(actDefId.startsWith("CWZLGDSPLC")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String customername = dataMap.get("COMPANYNAME").toString();
			String smsContent = customername+"一般财务资料归档审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "一般财务资料归档", smsContent);
					}
				}
			}
		//财务项目立项审批流程
		}else if(actDefId.startsWith("CWXMLXSPLC")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String customername = dataMap.get("COMPANYNAME").toString();
			String smsContent = customername+"一般性财务顾问项目立项审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "一般性财务顾问项目立项审核", smsContent);
					}
				}
			}
		//并购资料归档审批流程
		}else if(actDefId.startsWith("BGZLGDSPLC")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			// 获取流程表单数据
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String jyf = dataMap.get("COMPANYNAME").toString();
			String smsContent = jyf+"并购资料归档流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "并购资料归档", smsContent);
					}
				}
			}
		//并购申报资料审批流程
		}else if(actDefId.startsWith("BGSBZLSPLC")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			// 获取流程表单数据
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String jyf = dataMap.get("COMPANYNAME").toString();
			String smsContent = jyf+"并购申报资料审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "并购申报资料审核", smsContent);
					}
				}
			}
		//并购方案资料报审流程
		}else if(actDefId.startsWith("BGFAZLBS")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			// 获取流程表单数据
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String jyf = dataMap.get("COMPANYNAME").toString();
			String smsContent = jyf+"并购方案资料报审审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "并购方案资料报审审核", smsContent);
					}
				}
			}
		//并购项目立项审批流程
		}else if(actDefId.startsWith("BGXMLXSPLC")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			// 获取流程表单数据
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String jyf = dataMap.get("COMPANYNAME").toString();
			String smsContent = jyf+"并购项目立项审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "项目立项审核", smsContent);
					}
				}
			}
		//定增方案资料报审
		}else if(actDefId.startsWith("FAZLBS")&&(zqServer!=null&&!zqServer.equals("hlzq"))){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String projectname = dataMap.get("PROJECTNAME").toString();
			String smsContent = "股票发行项目:"+projectname+",方案资料报审信息已被驳回!";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "方案资料报审信息审批", smsContent);
					}
				}
			}
		//定增申报资料
		}else if(actDefId.startsWith("SBZL")&&(zqServer!=null&&!zqServer.equals("hlzq"))){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String projectname = dataMap.get("PROJECTNAME").toString();
			String smsContent = "股票发行项目:"+projectname+",申报资料信息已被驳回!";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "申报资料信息审批", smsContent);
					}
				}
			}
		//定增资料归档
		}else if(actDefId.startsWith("ZLGDSJFX")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String projectname = dataMap.get("PROJECTNAME").toString();
			String smsContent = "股票发行项目:"+projectname+",实际发行信息已被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "实际发行信息审批", smsContent);
					}
				}
			}
		//定增项目立项
		}else if(actDefId.startsWith("XMLXNFX")&&(zqServer!=null&&!zqServer.equals("hlzq"))){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String projectname = dataMap.get("PROJECTNAME").toString();
			String smsContent = "股票发行项目:"+projectname+",拟发行信息已被驳回!";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "拟发行信息审批", smsContent);
					}
				}
			}
		//挂牌登记及归档
		}else if(actDefId.startsWith("GPDJJGD")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String customername = dataMap.get("GSMC").toString();
			String smsContent = customername+"挂牌登记及归档审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "挂牌登记及归档", smsContent);
					}
				}
			}
		//股转反馈及回复
		}else if(actDefId.startsWith("GZFKJHF")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String customername = dataMap.get("CUSTOMERNAME").toString();
			String smsContent = customername+"股转反馈审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "股转反馈审核", smsContent);
					}
				}
			}
		//项目立项审批流程
		}else if(actDefId.startsWith("XMLXSPLC")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String projectname = dataMap.get("PROJECTNAME").toString();
			String smsContent = projectname+"项目立项审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "项目立项审核", smsContent);
					}
				}
			}
		//内核反馈审核
		}else if(actDefId.startsWith("NHFKSH")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String customername = dataMap.get("CUSTOMERNAME").toString();
			String smsContent = customername+"内核反馈审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "内核反馈审核", smsContent);
					}
				}
			}
		//项目申报审核
		}else if(actDefId.startsWith("XMNHSH")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String customername = dataMap.get("CUSTOMERNAME").toString();
			String smsContent = customername+"申报审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "申报审核", smsContent);
					}
				}
			}
		//项目股改审核
		}else if(actDefId.startsWith("XMGGSH")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String ygsmc = dataMap.get("YGSMC").toString();
			String smsContent = ygsmc+"项目股改审核流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "项目股改审核", smsContent);
					}
				}
			}
		}else if(actDefId.startsWith("TXXMGLLC")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			String XMMC = dataMap.get("XMMC").toString();
			String smsContent = XMMC+"--投行部项目融资需求管理流程被驳回！";
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
			if (target != null && tg != null) {
				String senduser = tg.get_userModel().getUsername();
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "投行部项目融资需求管理", smsContent);
					}
				}
			}
		}
	}
	public void scoreSet(){
		//type.equals()
		String info=zqbCheckService.scoreSet(instanceid,id,actDefId,countdownscore);
		ResponseUtil.write(info);
	}
	public void tjscoreSet(){
		memo=memo==null?"":memo;
		type=type==null?"":type;
		String info=zqbCheckService.tjscoreSet(instanceid,id,actDefId,countdownscore,type,memo,bkfr);
		ResponseUtil.write(info);
	}
	
	
	
	//会签短信/邮件提示/（dxlx == 1 时）加签完成短信
	public void hqdx(){
		dxlx=(Long) (dxlx==null?0l:dxlx);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String[] uslist = userid.split(",");
		//短信内容
		String SMSContent ="";
		//邮件标题
		String emailTitle="";
		//根据不同的项目发送会签提醒
			//公告审批流程
			if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")){
				if(dxlx==1l){
					SMSContent = dataMap.get("KHMC").toString() + "的公告："+dataMap.get("NOTICENAME").toString()+"，已加签完毕！";
					emailTitle = "公告审批加签完毕提醒";
				}else{
					SMSContent = dataMap.get("KHMC").toString() + "的公告："+dataMap.get("NOTICENAME").toString()+"，需要会签！";
					emailTitle = "公告审批会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//日常业务呈报流程
			}else if(actDefId.startsWith("RCYWCB")){
				if(dxlx==1l){
					SMSContent = dataMap.get("KHMC").toString() + "的事项："+dataMap.get("SXMC").toString()+"，已加签完毕！";
					emailTitle = "日常业务呈报加签完毕提醒";
				}else{
					SMSContent = dataMap.get("KHMC").toString() + "的事项："+dataMap.get("SXMC").toString()+"，需要会签！";
					emailTitle = "日常业务呈报会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					if (dataMap != null) {
						UserContext target = UserContextUtil.getInstance().getUserContext(usid);
						UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
						if (target != null && tg != null) {
							String senduser = tg.get_userModel().getUsername();
							if (!SMSContent.equals("")) {
								String mobile = target.get_userModel().getMobile();
								if (mobile != null && !mobile.equals("")) {
									MessageAPI.getInstance().sendSMS(uc, mobile, SMSContent);
								}
								String email = target.get_userModel().getEmail();
								if(email != null && !email.equals("")){
									MessageAPI.getInstance().sendSysMail(senduser, email, emailTitle, SMSContent);
								}
							}
						}
					}
				}
		    //财务工作进度汇报流程
			}else if(actDefId.startsWith("CWGZJDHB")){
				String customername = dataMap.get("COMPANYNAME").toString();
				if(dxlx==1l){
					SMSContent = customername+"一般财务工作进度汇报审核流程已加签完毕！";
					emailTitle = "一般财务工作进度汇报审核加签完毕提醒";
				}else{
					SMSContent = customername+"一般财务工作进度汇报审核流程需要会签！";
					emailTitle = "一般财务工作进度汇报审核会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//财务资料归档审批流程
			}else if(actDefId.startsWith("CWZLGDSPLC")){
				String customername = dataMap.get("COMPANYNAME").toString();
				if(dxlx==1l){
					SMSContent = customername+"一般财务资料归档审核流程已加签完毕！";
					emailTitle = "一般财务工作进度汇报审核加签完毕提醒";
				}else{
					SMSContent = customername+"一般财务资料归档审核流程需要会签！";
					emailTitle ="一般财务资料归档会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//财务项目立项审批流程
			}else if(actDefId.startsWith("CWXMLXSPLC")){
				String customername = dataMap.get("COMPANYNAME").toString();
				if(dxlx==1l){
					SMSContent = customername+"一般性财务顾问项目立项审核流程已加签完毕！";
					emailTitle = "一般性财务顾问项目立项审核加签完毕提醒";
				}else{
					SMSContent = customername+"一般性财务顾问项目立项审核流程需要会签！";
					emailTitle = "一般性财务顾问项目立项审核会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//并购资料归档审批流程
			}else if(actDefId.startsWith("BGZLGDSPLC")){
				String jyf = dataMap.get("COMPANYNAME").toString();
				if(dxlx==1l){
					SMSContent = jyf+"并购资料归档流程已加签完毕！";
					emailTitle = "并购资料归档加签完毕提醒";
				}else{
					SMSContent = jyf+"并购资料归档流程需要会签！";
					emailTitle = "并购资料归档会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//并购申报资料审批流程
			}else if(actDefId.startsWith("BGSBZLSPLC")){
				String jyf = dataMap.get("COMPANYNAME").toString();
				if(dxlx==1l){
					SMSContent = jyf+"并购申报资料审核流程已加签完毕！";
					emailTitle = "并购申报资料审核加签完毕提醒";
				}else{
					SMSContent = jyf+"并购申报资料审核流程需要会签！";
					emailTitle = "并购申报资料审核会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email,emailTitle, SMSContent);
							}
						}
					}
				}
			//并购方案资料报审流程
			}else if(actDefId.startsWith("BGFAZLBS")){
				String jyf = dataMap.get("COMPANYNAME").toString();
				if(dxlx==1l){
					SMSContent = jyf+"并购方案资料报审审核流程已加签完毕！";
					emailTitle = "并购方案资料报审审核加签完毕提醒";
				}else{
					SMSContent = jyf+"并购方案资料报审审核流程需要会签！";
					emailTitle = "并购方案资料报审审核会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//并购项目立项审批流程
			}else if(actDefId.startsWith("BGXMLXSPLC")){
				String jyf = dataMap.get("COMPANYNAME").toString();
				if(dxlx==1l){
					SMSContent = jyf+"并购项目立项审核流程已加签完毕！";
					emailTitle = "并购项目立项审核加签完毕提醒";
				}else{
					SMSContent = jyf+"并购项目立项审核流程需要会签！";
					emailTitle = "项目立项审核会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//定增方案资料报审
			}else if(actDefId.startsWith("FAZLBS")){
				String projectname = dataMap.get("PROJECTNAME").toString();
				if(dxlx==1l){
					SMSContent = "股票发行项目:"+projectname+",方案资料报审信息已加签完毕!";
					emailTitle = "方案资料报审信息审批加签完毕提醒";
				}else{
					SMSContent = "股票发行项目:"+projectname+",方案资料报审信息需要会签!";
					emailTitle = "方案资料报审信息审批会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//定增申报资料
			}else if(actDefId.startsWith("SBZL")){
				String projectname = dataMap.get("PROJECTNAME").toString();
				if(dxlx==1l){
					SMSContent = "股票发行项目:"+projectname+",申报资料信息已加签完毕!";
					emailTitle = "申报资料信息加签完毕提醒";
				}else{
					SMSContent = "股票发行项目:"+projectname+",申报资料信息需要会签!";
					emailTitle = "申报资料信息审批会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//定增资料归档
			}else if(actDefId.startsWith("ZLGDSJFX")){
				String projectname = dataMap.get("PROJECTNAME").toString();
				if(dxlx==1l){
					SMSContent = "股票发行项目:"+projectname+",实际发行信息已加签完毕!";
					emailTitle = "实际发行信息审批加签完毕提醒";
				}else{
					SMSContent = "股票发行项目:"+projectname+",实际发行信息需要会签！";
					emailTitle = "实际发行信息审批会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//定增项目立项
			}else if(actDefId.startsWith("XMLXNFX")){
				String projectname = dataMap.get("PROJECTNAME").toString();
				if(dxlx==1l){
					SMSContent = "股票发行项目:"+projectname+",拟发行信息已加签完毕!";
					emailTitle = "拟发行信息需加签完毕提醒";
				}else{
					SMSContent = "股票发行项目:"+projectname+",拟发行信息需要会签!";
					emailTitle ="拟发行信息审批会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//挂牌登记及归档
			}else if(actDefId.startsWith("GPDJJGD")){
				String customername = dataMap.get("GSMC").toString();
				if(dxlx==1l){
					SMSContent = customername+"挂牌登记及归档审核流程已加签完毕!";
					emailTitle = "挂牌登记及归档加签完毕提醒";
				}else{
					SMSContent = customername+"挂牌登记及归档审核流程需要会签！";
					emailTitle = "挂牌登记及归档会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//股转反馈及回复
			}else if(actDefId.startsWith("GZFKJHF")){
				String customername = dataMap.get("CUSTOMERNAME").toString();
				if(dxlx==1l){
					SMSContent = customername+"股转反馈审核流程已加签完毕!";
					emailTitle = "股转反馈审核流程加签完毕提醒";
				}else{
					SMSContent = customername+"股转反馈审核流程需要会签！";
					emailTitle="股转反馈审核会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email,emailTitle, SMSContent);
							}
						}
					}	
				}
			//项目立项审批流程
			}else if(actDefId.startsWith("XMLXSPLC")){
				String projectname = dataMap.get("PROJECTNAME").toString();
				if(dxlx==1l){
					SMSContent = projectname+"项目立项审核流程已加签完毕!";
					emailTitle = "项目立项审核流程加签完毕提醒";
				}else{
					SMSContent = projectname+"项目立项审核流程需要会签！";
					emailTitle = "项目立项审核会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//内核反馈审核
			}else if(actDefId.startsWith("NHFKSH")){
				String customername = dataMap.get("CUSTOMERNAME").toString();
				if(dxlx==1l){
					SMSContent =customername+"内核反馈审核流程已加签完毕!";
					emailTitle = "项目立项审核流程加签完毕提醒";
				}else{
					SMSContent = customername+"内核反馈审核流程需要会签！";
					emailTitle=	"项目立项审核流程会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//项目申报审核
			}else if(actDefId.startsWith("XMNHSH")){
				String customername = dataMap.get("CUSTOMERNAME").toString();
				if(dxlx==1l){
					SMSContent =customername+"申报审核流程已加签完毕!";
					emailTitle = "申报审核流程加 签完毕提醒";
				}else{
					SMSContent = customername+"申报审核流程需要会签！";
					emailTitle = "申报审核会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			//项目股改审核
			}else if(actDefId.startsWith("XMGGSH")){
				String ygsmc = dataMap.get("YGSMC").toString();
				if(dxlx==1l){
					SMSContent =customername+"项目股改审核流程已加签完毕!";
					emailTitle = "项目股改审核流程加 签完毕提醒";
				}else{
					SMSContent = ygsmc+"项目股改审核流程需要会签！";
					emailTitle = "项目股改审核会签提醒";
				}
				for(int i=0;i<uslist.length;i++){
					String usid = uslist[i];
					UserContext target = UserContextUtil.getInstance().getUserContext(usid);
					UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
					if (target != null && tg != null) {
						String senduser = tg.get_userModel().getUsername();
						if (!SMSContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,SMSContent);
							}
							String email = target.get_userModel().getEmail();
							if (email != null && !email.equals("")) {
								MessageAPI.getInstance().sendSysMail(senduser,email, emailTitle, SMSContent);
							}
						}
					}
				}
			}		
	}
	//获取提交被扣分人账号
	public void XSBKFR(){
		String backStepList = zqbCheckService.getBackOtherList(this.actDefId, this.prcDefId, this.actStepDefId, this.taskId, this.backType,this.instanceId);
		ResponseUtil.write(backStepList);
	}
	public void isShowKF(){
		String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
		String isShowKF = "true";
		if(actDefId.startsWith("GGSPLC")){
			if(actStepDefId.equals(SystemConfig._ggsplcConf.getJd1())||actStepDefId.equals(SystemConfig._ggsplcConf.getJd8())){
				isShowKF = "false";
			}
		}else if(actDefId.startsWith("CXDDYFQLC")){
			if(actStepDefId.equals(SystemConfig._cxddfqlcConf.getJd1())||actStepDefId.equals(SystemConfig._cxddfqlcConf.getJd7())){
				isShowKF = "false";
			}
		}
		else if(actDefId.startsWith("RCYWCB")){
			if(actStepDefId.equals(SystemConfig._rcywcbLcConf.getJd0())){
				isShowKF = "false";
			}
		 //重要督导事项流程不显示扣分项
		}else if(actDefId.startsWith("ZYDDSXLC")){
			isShowKF = "false";
		//重要督导事项流程不显示扣分项
		}else if(actDefId.startsWith("TXXMGLLC")){
			isShowKF = "false";
		}else if(actDefId.startsWith("DGDYLC")){
			isShowKF = "false";
		}
		//山西不显示扣分项
		if(configParameter.equals("sxzq")){
			if(actDefId.startsWith("CWRZSP")){
				isShowKF = "false";
			}else if(actDefId.startsWith("XMLXSPLC")){
				if(actStepDefId.equals(SystemConfig._xmlxSpLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("XMGGSH")){
				if(actStepDefId.equals(SystemConfig._gugaiLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("XMNHSH")){
				if(actStepDefId.equals(SystemConfig._xmnhLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("NHFKSH")){
				if(actStepDefId.equals(SystemConfig._nhfkLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("GZFKJHF")){
				if(actStepDefId.equals(SystemConfig._gzfkLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("GPDJJGD")){
				if(actStepDefId.equals(SystemConfig._guapaiLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("XMLXNFX")){
				if(actStepDefId.equals(SystemConfig._xmlxnfxLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("FAZLBS")){
				if(actStepDefId.equals(SystemConfig._fazlbsLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("SBZL")){
				if(actStepDefId.equals(SystemConfig._sbzlLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("ZLGDSJFX")){
				if(actStepDefId.equals(SystemConfig._zlgdsjfxLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("BGXMLXSPLC")){
				if(actStepDefId.equals(SystemConfig._bgXmlxSpLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("BGFAZLBS")){
				if(actStepDefId.equals(SystemConfig._bgFangAnBSSpLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("BGSBZLSPLC")){
				if(actStepDefId.equals(SystemConfig._bgSbzlSpLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("BGZLGDSPLC")){
				if(actStepDefId.equals(SystemConfig._bgZlgdSpLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("CWXMLXSPLC")){
				if(actStepDefId.equals(SystemConfig._cwXmlxSpLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("CWGZJDHB")){
				if(actStepDefId.equals(SystemConfig._cwGzjdhbSpLcConf.getJd1())){
					isShowKF = "false";
				}
			}else if(actDefId.startsWith("CWZLGDSPLC")){
				if(actStepDefId.equals(SystemConfig._cwZlgdSpLcConf.getJd1())){
					isShowKF = "false";
				}
			}
		}else if(configParameter.equals("hlzq")){
			//收购项目管理
			if(actDefId.startsWith("SGLXXX") || actDefId.startsWith("SGFAZLBS") || actDefId.startsWith("SGGZFKWTTZ") || actDefId.startsWith("SGGZFKHFSH")){
				if(actDefId.startsWith("SGLXXX") && actStepDefId.equals(SystemConfig._hlSgxmlxLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("SGFAZLBS") && actStepDefId.equals(SystemConfig._hlSgzlbsLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("SGGZFKWTTZ") && actStepDefId.equals(SystemConfig._hlSgfkwtLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("SGGZFKHFSH") && actStepDefId.equals(SystemConfig._hlSgfkhfLcConf.getJd1())){
					isShowKF = "false";
				}
				//挂牌项目管理
			}else if(actDefId.startsWith("XMLXSPLC") || actDefId.startsWith("XMGGSH") || actDefId.startsWith("GPDJJGD") || actDefId.startsWith("NHFKSH") || actDefId.startsWith("XMNHSH") || actDefId.startsWith("GZFKJHF") || actDefId.startsWith("GPSCXXPL") || actDefId.startsWith("GPLSXXPL") || actDefId.startsWith("GPECXXPL") ){
				if(actDefId.startsWith("XMLXSPLC") && actStepDefId.equals(SystemConfig._hlGpxmlxLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("XMGGSH") && actStepDefId.equals(SystemConfig._hlGpsbzkLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("GPDJJGD") && actStepDefId.equals(SystemConfig._hlGpsbgzLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("NHFKSH") && actStepDefId.equals(SystemConfig._hlGpnhfkLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("XMNHSH") && actStepDefId.equals(SystemConfig._hlGpgzfkLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("GZFKJHF") && actStepDefId.equals(SystemConfig._hlGpfkhfLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("GPSCXXPL") && actStepDefId.equals(SystemConfig._hlGpscplLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("GPLSXXPL") && actStepDefId.equals(SystemConfig._hlGplsplLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("GPECXXPL") && actStepDefId.equals(SystemConfig._hlGpecplLcConf.getJd1())){
					isShowKF = "false";
				}
				//定增项目管理(200人以内)
			}else if(actDefId.startsWith("XMLXNFX") || actDefId.startsWith("FAZLBS") || actDefId.startsWith("DZSBNH") || actDefId.startsWith("DZNHFK") || actDefId.startsWith("SBZL") || actDefId.startsWith("DZEJDGZFKWT") || actDefId.startsWith("DZEJDGZFKHF")  ){
				if(actDefId.startsWith("XMLXNFX") && actStepDefId.equals(SystemConfig._hlDzxmlxLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("FAZLBS") && actStepDefId.equals(SystemConfig._hlDzgpfxLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("DZSBNH") && actStepDefId.equals(SystemConfig._hlDzyjgwLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("DZNHFK") && actStepDefId.equals(SystemConfig._hlDzyjghLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("SBZL") && actStepDefId.equals(SystemConfig._hlDzsbzlLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("DZEJDGZFKWT") && actStepDefId.equals(SystemConfig._hlDzejgwLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("DZEJDGZFKHF") && actStepDefId.equals(SystemConfig._hlDzejghLcConf.getJd1())){
					isShowKF = "false";
				}
				//重组项目管理                                                                                                                                                                            ////一阶段反馈问题
			}else if(actDefId.startsWith("ZZXMLX") || actDefId.startsWith("ZZYJ") || actDefId.startsWith("ZZYJDGZFKWT") || actDefId.startsWith("ZZYJDGZFKHF") || actDefId.startsWith("ZZSBNH") || actDefId.startsWith("ZZNHHF") || actDefId.startsWith("ZZEJDGZFKWT") || actDefId.startsWith("ZZEJDGZFKHF") || actDefId.startsWith("ZZSBBA") ){
				if(actDefId.startsWith("ZZXMLX") && actStepDefId.equals(SystemConfig._hlBgxmlxLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("ZZYJ") && actStepDefId.equals(SystemConfig._hlBgCzyjLcConf.getJd1())){
					isShowKF = "false";
					
				}//一阶段反馈问题
				else if(actDefId.startsWith("ZZYJDGZFKWT") && actStepDefId.equals(SystemConfig._hlBgCzyjwtLcConf.getJd1())){
					isShowKF = "false";
				}
				else if(actDefId.startsWith("ZZYJDGZFKHF") && actStepDefId.equals(SystemConfig._hlBgCzyjhfLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("ZZSBNH") && actStepDefId.equals(SystemConfig._hlBgSbnhLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("ZZNHHF") && actStepDefId.equals(SystemConfig._hlBgNhhfLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("ZZEJDGZFKWT") && actStepDefId.equals(SystemConfig._hlBgCzyjwtLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("ZZEJDGZFKHF") && actStepDefId.equals(SystemConfig._hlBgEjhfLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("ZZSBBA") && actStepDefId.equals(SystemConfig._hlBgSbzlLcConf.getJd1())){
					isShowKF = "false";
				}
				//一般性财务顾问项目管理
			}else if(actDefId.startsWith("YBCWXMLX") || actDefId.startsWith("YBCWGZJDHB")){
				if(actDefId.startsWith("YBCWXMLX") && actStepDefId.equals(SystemConfig._hlYbcwxmlxLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("YBCWGZJDHB") && actStepDefId.equals(SystemConfig._hlYbcwCwgzjdLcConf.getJd1())){
					isShowKF = "false";
					
				}
				//定增项目管理(200人以上)
			}else if(actDefId.startsWith("DZXMLXEBYS") || actDefId.startsWith("GPFXEBYS") || actDefId.startsWith("SBNHEBYS") || actDefId.startsWith("NHFKEBYS") || actDefId.startsWith("FKHFEBYS") || actDefId.startsWith("GZBAEBYS")){
				if(actDefId.startsWith("DZXMLXEBYS") && actStepDefId.equals(SystemConfig._hlDzwxmlxLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("GPFXEBYS") && actStepDefId.equals(SystemConfig._hlDzwgpfxLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("SBNHEBYS") && actStepDefId.equals(SystemConfig._hlDzwsbnhLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("NHFKEBYS") && actStepDefId.equals(SystemConfig._hlDzwnhfkLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("FKHFEBYS") && actStepDefId.equals(SystemConfig._hlDzwfkhfLcConf.getJd1())){
					isShowKF = "false";
				}else if(actDefId.startsWith("GZBAEBYS") && actStepDefId.equals(SystemConfig._hlDzwgzbaLcConf.getJd1())){
					isShowKF = "false";
				}
			}
		}
		ResponseUtil.write(isShowKF);
	}
	public void sxkf(){
		String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
		String isShowKF = "true";
		if(configParameter.equals("sxzq") || configParameter.equals("hlzq") ){
			if(actDefId.startsWith("GGSPLC")){
					isShowKF = "jflx";
			}else if(actDefId.startsWith("CXDDYFQLC")){
					isShowKF = "jflx";
			}
			else if(actDefId.startsWith("RCYWCB")){
					isShowKF = "jflx";
			}else{
				isShowKF = "jffs";
			}
		}
		ResponseUtil.write(isShowKF);
	}
	public void bhisShowKF(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		
		String roleid="";
		if(receiveUser!=null && !"".equals(receiveUser)){
			Map params = new HashMap();
			params.put(1, receiveUser);
			roleid=DBUtil.getDataStr("orgroleid", "select orgroleid from orguser  where userid= ? ", params);
		}
		String isShowKF = "false";
		String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
		if(user.getUserid().equals(receiveUser)){
			isShowKF = "false";
			
		}
		//xlj update 2018年1月24日11:34:12 西南想要驳回给督导扣分的扣分类型
		//else if(configParameter.equals("ztzq") || configParameter.equals("dgzq") || configParameter.equals("sxzq")  || configParameter.equals("hlzq") || configParameter.equals("xnzq")){
			if(configParameter.equals("sxzq") && actDefId.startsWith("CWRZSP")){
				isShowKF = "false";
			}else{
				isShowKF = "true";
			}
		/*}else{
			if("3".equals(roleid)){
				isShowKF = "true";
			}
		}*/
		ResponseUtil.write(isShowKF);
	}
	//获取扣分类型
	public void kflx(){
		String backStepList = zqbCheckService.getkflx();
		ResponseUtil.write(backStepList);
	}
	//获取登录用户角色id 
	public void selectuserrole(){
		String isShow="false";
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long orgroleid = uc.get_userModel().getOrgroleid();
		if(orgroleid==3)
			isShow="true";
		ResponseUtil.write(isShow);
	}
	public void getProcessPriority(){
		Task task = ProcessAPI.getInstance().getTask(taskId);
		String data = "0";
		if(task!=null){
			if(task.getPriority()==80)
				data="1";
			else
				data="0";
		}else{
			data="0";
		}
		ResponseUtil.write(data);
	}
	/*
	 * 
	 * 通过用户名获取USERID，返回值空 及不存在，目前是做用户判断使用
	 * 
	 */
	public String validateUser(String username){
		Map params = new HashMap();
		params.put(1, username);
		String USERID = DBUtil.getDataStr("USERID","select USERID from orguser where username = ? ", params);
		if (USERID == null || USERID.trim().length() == 0)
			USERID = "";
		return USERID;
	}
}
