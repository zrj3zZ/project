package com.iwork.app.weixin.process.action;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.trigger.TriggerAPI;
import com.iwork.core.engine.trigger.model.BaseTriggerModel;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.process.definition.step.model.ProcessStepMap;
import com.iwork.process.definition.step.service.ProcessStepTriggerService;
import com.iwork.process.runtime.constant.SendPageTypeConstant;
import com.iwork.process.runtime.pvm.impl.execute.PvmDefExecuteEngine;
import com.iwork.process.runtime.pvm.impl.execute.PvmProcessSecurityEngine;
import com.iwork.process.runtime.pvm.impl.variable.RouteModel;
import com.iwork.process.runtime.pvm.impl.variable.SendPageModel;
import com.iwork.process.runtime.service.ProcessRuntimeOperateService;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

public class WeiXinProcessRuntimeOperateAction extends ActionSupport
{
  private ProcessRuntimeOperateService processRuntimeOperateService;
  private ProcessStepTriggerService processStepTriggerService;
  private String actDefId;
  private String action;
  private Long prcDefId;
  private String actStepDefId = "";
  private Long formId;
  private String taskId = "0";
  private String currentUser;
  private Long instanceId;
  private Long excutionId;
  private Long dataid;
  private List<SendPageModel> sendList;
  private String mjId;
  private String targetStepId;
  private String targetStepName;
  private String title;
  private String opinion;
  private Long opinionFlag;
  private boolean isParallel;
  private String ccUsers;
  private List<OrgUser> receiveUser;
  private HashMap backUser;
  private HashMap ccUser;
  private int maxUser;
  private Long ccinstal = Long.valueOf(0L);
  private String tipsInfo;
  private String inputField;
  private Long backType;
  private HashMap remindTypeList;
  private List opinions;
  private String backStepList;
  private String trans_tip;
  private String deviceType;
  private String cslc;
  private String serverflg;
  
  public String getCslc() {
		return cslc;
	}

	public void setCslc(String cslc) {
		this.cslc = cslc;
	}
  public String executeHandle()
  {
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.instanceId != null) && (this.excutionId != null) && (this.taskId != null)) {
      this.deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();

      this.opinion = this.processRuntimeOperateService.getOpinionContent(this.actDefId, this.prcDefId, this.taskId);

      this.remindTypeList = this.processRuntimeOperateService.getTaskRemindCheckBox(this.actDefId, this.prcDefId, this.actStepDefId);

      boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(this.taskId);
      if (flag) {
        HashMap triggerParams = new HashMap();

        BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "TRAN_BEFOR");
        boolean isrun = true;
        if (triggerModel != null) {
          UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
          HttpServletRequest request = ServletActionContext.getRequest();
        //  HashMap params = (HashMap)request.getParameterMap();
          HashMap params = ParameterMapUtil.getParameterMap(request.getParameterMap());
          triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
          triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
          triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
          triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
          triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
          triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
          triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
          isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
        }

        if (isrun) {
          HttpServletRequest request = ServletActionContext.getRequest();
        //  HashMap pageParams = (HashMap)request.getParameterMap();
          HashMap pageParams = ParameterMapUtil.getParameterMap(request.getParameterMap());
          RouteModel model = this.processRuntimeOperateService.executeHandle(this.actDefId, this.prcDefId, this.actStepDefId, this.taskId, pageParams);
          if (model != null) {

        	String zqServer = ConfigUtil.readAllProperties("/common.properties").get("zqServer");
        	String Jd3 = SystemConfig._hlGpsbzkLcConf.getJd3();
            String Jd2 = SystemConfig._hlGpsbzkLcConf.getJd2();
            if(zqServer.equals("hlzq")&&(actStepDefId.equals(Jd3)||actStepDefId.equals(Jd2))){
            	serverflg="verification";
            }
        	
            this.ccinstal = model.isCC();
            this.ccUsers = model.getCcUser();
            this.title = model.getTitle();
            this.opinionFlag = model.getIsOpinion();
            if (model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE)) {
              List sendList = new ArrayList();

              SendPageModel sendModel = new SendPageModel();

              sendModel.setTargetStepId(model.getNextStepId());

              String t_activityName = model.getNextStepName();
              if (t_activityName != null) {
                sendModel.setTargetStepName(t_activityName);
              }

              sendModel.setAddressHTML(model.getAddressHTML());

              sendModel.setSendPageType(SendPageTypeConstant.SP_TYPE_CHECKLIST_SELECT_ALL);
              sendList.add(sendModel);
              setSendList(sendList);
              if (model.getAction() != null)
                this.action = model.getAction();
              else {
                this.action = "顺序流转";
              }

              this.trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
              return "success";
            }if (model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_PARALLELGATEWAY_HANDLE)) {
              List sendList = new ArrayList();

              SendPageModel sendModel = new SendPageModel();

              sendModel.setTargetStepId(model.getNextStepId());

              String t_activityName = model.getNextStepName();
              if (t_activityName != null) {
                sendModel.setTargetStepName(t_activityName);
              }

              sendModel.setAddressHTML(model.getAddressHTML());

              sendModel.setSendPageType(SendPageTypeConstant.SP_TYPE_CHECKLIST_SELECT_ALL);
              sendList.add(sendModel);
              setSendList(sendList);
              if (model.getAction() != null)
                this.action = model.getAction();
              else {
                this.action = "顺序流转";
              }

              this.trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
              return "PARALLELGATEWAY";
            }if (model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_JAVA_SERVICE)) {
              this.action = "服务";

              return "success";
            }if (model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES)) {
              this.action = "归档";
              int indexOf = actDefId.indexOf(":");
              cslc=actDefId.substring(0, indexOf);
//              Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
//              String result=config.get("chaosong");
//              String[] split = result.split(",");
              Boolean fla=false;
//              for (int i = 0; i < split.length; i++) {
//              	if(cslc.equals(split[i])){
//              		fla=true;
//              		break;
//              	};
//				}
              UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
             
          	if(cslc.equals("DZSBNH")||cslc.equals("ZZEJDGZFKWT")||cslc.equals("DZEJDGZFKWT")||cslc.equals("ZZYJDGZFKWT")||cslc.equals("SGGZFKWTTZ")){
          		fla=true;
          	}
              if(fla){
              String cuser= uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
              
              HashMap fromData = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
              String projectno=null;
              if(cslc.equals("DZSBNH")||cslc.equals("DZEJDGZFKWT")){
              	projectno=fromData.get("PROJECTNO")==null?"":fromData.get("PROJECTNO").toString();
              }else if(cslc.equals("ZZYJDGZFKWT")||cslc.equals("ZZEJDGZFKWT")||cslc.equals("SGGZFKWTTZ")){
              	
              	 projectno =fromData.get("XMBH")==null?"":fromData.get("XMBH").toString();
              }
              List list =new ArrayList();
              list.add("TBRID");
              list.add("PROJECTNO"); 
              list.add("OWNER");
              list.add("MANAGER");
              
              StringBuffer sql=new StringBuffer();
              Map params=new HashMap();
              if(cslc.equals("DZSBNH")||cslc.equals("DZEJDGZFKWT")){
              	sql.append("select CREATEUSER||'['||CREATEUSERID||']' TBRID,PROJECTNO,OWNER,MANAGER from BD_ZQB_GPFXXMB where PROJECTNO=?");
              	
              }else if(cslc.equals("ZZYJDGZFKWT")||cslc.equals("ZZEJDGZFKWT")||cslc.equals("SGGZFKWTTZ")){
              	sql.append("select  TBRID,XMBH PROJECTNO,OWNER,MANAGER from BD_ZQB_BGZZLXXX where XMBH =?");
              }
              params.put(1, projectno);
              List<HashMap> dataList = DBUTilNew.getDataList(list, sql.toString(), params);
               ccUsers=null;
              if(!dataList.isEmpty()){
              for (HashMap hashMap : dataList) {
              	
              	ccUsers=hashMap.get("TBRID").toString();
              	
              	String usename=ccUsers.substring(0,ccUsers.indexOf("["));
              	String useid=ccUsers.substring(ccUsers.indexOf("[")+1,ccUsers.length()-1);
              	ccUsers=useid+"["+usename+"]";
              	if(ccUsers.equals(cuser)){
              		ccUsers=null;
              	}
              	if(ccUsers!=null){
              	if(!hashMap.get("OWNER").toString().equals(ccUsers)){
              		
              		ccUsers=ccUsers+","+hashMap.get("OWNER").toString();
              	}
              	}else{
              		if(!hashMap.get("OWNER").toString().equals(cuser)){
              			ccUsers=hashMap.get("OWNER").toString();
              		}
              		
              		
              	}
              	if(ccUsers!=null){
              	if(ccUsers.contains(",")){
              		
              		String[] split = ccUsers.split(",");
              		Boolean a=false;
              		for (int i = 0; i < split.length; i++) {
              			if(hashMap.get("MANAGER").toString().equals(split[i])){
              				a=true;
              			}
							
						}
              		if(!a){
              			ccUsers=ccUsers+","+hashMap.get("MANAGER").toString();
              		}
              	}else{
              		
              			if((!hashMap.get("MANAGER").toString().equals(ccUsers))&&(!hashMap.get("MANAGER").toString().equals(cuser))){
              				ccUsers=ccUsers+","+hashMap.get("MANAGER").toString();
              		}
              		}
              		
              		
              	}else{
          			if(!hashMap.get("MANAGER").toString().equals(cuser)){
          				ccUsers=hashMap.get("MANAGER").toString();
          			}
          			
          			
              	}
              	String PROJECTNO = hashMap.get("PROJECTNO").toString();
              	Map params1=new HashMap();
                  
                 
                  params1.put(1, projectno);
                  StringBuffer sql1=new StringBuffer();
              	sql1.append("select c.username from (");
              	if(cslc.equals("DZSBNH")||cslc.equals("DZEJDGZFKWT")){
              		sql1.append("select s.instanceid,p.projectno from BD_ZQB_GPFXXMB p left join sys_engine_form_bind s on p.ID=S.DATAID where s.formid=(select id from sys_engine_iform where iform_title='股票发行项目信息')");
              	}else if(cslc.equals("ZZYJDGZFKWT")||cslc.equals("ZZEJDGZFKWT")){
              		sql1.append("select s.instanceid,p.xmbh from BD_ZQB_BGZZLXXX p left join sys_engine_form_bind s on p.ID=S.DATAID where s.formid=(select id from sys_engine_iform where iform_title='重组项目管理')");
              		
              		
              	}else if(cslc.equals("SGGZFKWTTZ")){
              		sql1.append("select s.instanceid,p.xmbh from BD_ZQB_BGZZLXXX p left join sys_engine_form_bind s on p.ID=S.DATAID where s.formid=(select id from sys_engine_iform where iform_title='收购项目管理')");
              	}
              				
              	sql1.append(") a left join (");

              	sql1.append("select b.userid||'['||b.name||']' username,s.instanceid from bd_zqb_group b left join sys_engine_form_bind s on b.id=s.dataid where s.formid=(select id from sys_engine_iform where iform_title='项目成员列表')");
              	if(cslc.equals("DZSBNH")||cslc.equals("DZEJDGZFKWT")){
              		
              		sql1.append(") c on a.instanceid=c.instanceid where a.projectno=?");
              	}else if(cslc.equals("ZZYJDGZFKWT")||cslc.equals("ZZEJDGZFKWT")||cslc.equals("SGGZFKWTTZ")){
              		sql1.append(") c on a.instanceid=c.instanceid where a.xmbh=?");
              	}
              	 List list1 =new ArrayList();
                  
                   list1.add("username");
                  
              	 List<HashMap> dataList1 = DBUTilNew.getDataList(list1, sql1.toString(), params1);
              	 if(!dataList1.isEmpty()){
              		 
              	 
              	for (HashMap hashMap2 : dataList1) {
              		String username=null;
              		if(!hashMap2.isEmpty()){
              			if(ccUsers!=null){
              				if(ccUsers.contains(",")){
              					
              			
              		String[] split = ccUsers.split(",");
              		Boolean a=false;
              		for (int i = 0; i < split.length; i++) {
              		 username=hashMap2.get("username")==null?"":hashMap2.get("username").toString();
              			
              			if(split[i].equals(username)){
              				a=true;
              			}
              		}
						
              		if(!a){
              			if(username!=null||"".equals(username)){
              			ccUsers=ccUsers+","+username;
              			}
              		}
              				}else{
              					username=hashMap2.get("username")==null?"":hashMap2.get("username").toString();
              					if(username!=null||"".equals(username)){
              					if(!ccUsers.equals(username)&&!cuser.equals(username)){
              						ccUsers=ccUsers+","+username;
              				}
              					}
              			}
					
              	 }else{
              		 if(username!=null||"".equals(username)){
              			 if(!ccUsers.equals(username)&&!cuser.equals(username)){
              				 ccUsers=username;
              				 
              				 
              			 }
              		 } 
              	 }
              	 }
              	}
              	 }
              	
				}
              
              }
              }
              trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
              return "ARCHIVES";
            }
          }
        }
      }
    }
    this.tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
    return "error";
  }

  public String executeManualJump()
  {
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.instanceId != null) && (this.excutionId != null) && (this.taskId != null) && (this.targetStepId != null))
    {
      this.remindTypeList = this.processRuntimeOperateService.getTaskRemindCheckBox(this.actDefId, this.prcDefId, this.actStepDefId);

      this.opinion = this.processRuntimeOperateService.getOpinionContent(this.actDefId, this.prcDefId, this.taskId);
      boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(this.taskId);
      if (flag) {
        HashMap triggerParams = new HashMap();

        BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "TRAN_BEFOR");
        boolean isrun = true;
        if (triggerModel != null) {
          UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
          HttpServletRequest request = ServletActionContext.getRequest();
          Map params = request.getParameterMap();

          triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
          triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
          triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
          triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
          triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
          triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
          triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
          isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
        }

        if (isrun) {
          HttpServletRequest request = ServletActionContext.getRequest();
          HashMap pageParams = ParameterMapUtil.getParameterMap(request.getParameterMap());
          RouteModel model = this.processRuntimeOperateService.executeManualJump(this.actDefId, this.prcDefId, this.actStepDefId, this.taskId, this.targetStepId, this.mjId, pageParams);
          List sendList = new ArrayList();
          SendPageModel sendModel = new SendPageModel();

          sendModel.setTargetStepId(model.getNextStepId());

          String t_activityName = model.getNextStepName();
          if (t_activityName != null) {
            sendModel.setTargetStepName(t_activityName);
          }
          this.ccinstal = model.isCC();
          this.ccUsers = model.getCcUser();
          this.opinionFlag = model.getIsOpinion();

          sendModel.setAddressHTML(model.getAddressHTML());

          sendModel.setSendPageType(SendPageTypeConstant.SP_TYPE_CHECKLIST_SELECT_ALL);
          sendList.add(sendModel);
          setSendList(sendList);

          if (model.getAction() != null)
            this.action = model.getAction();
          else {
            this.action = "任务跳转";
          }

          if ((model.getSendPageCode() != null) && (model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES))) {
            this.title = model.getTitle();

            this.trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
            return "ARCHIVES";
          }

          this.trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
          return "success";
        }

        this.tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
        return "error";
      }

      this.tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
      return "error";
    }

    this.tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
    return "error";
  }

  public String executeBack()
  {
    SendPageModel backModel = null;
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.instanceId != null) && (this.excutionId != null) && (this.taskId != null)) {
      if (this.backType == null) {
        this.backType = new Long(1L);
      }

      this.opinion = this.processRuntimeOperateService.getOpinionContent(this.actDefId, this.prcDefId, this.taskId);

      this.remindTypeList = this.processRuntimeOperateService.getTaskRemindCheckBox(this.actDefId, this.prcDefId, this.actStepDefId);
      boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(this.taskId);
      if (flag) {
        HashMap triggerParams = new HashMap();

        BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "TRAN_BACK");
        boolean isrun = true;
        Map params;
        if (triggerModel != null) {
          UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
          HttpServletRequest request = ServletActionContext.getRequest();
          params = request.getParameterMap();

          triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
          triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
          triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
          triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
          triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
          triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
          triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
          isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
        }

        if (isrun) {
          backModel = this.processRuntimeOperateService.executeBack(this.actDefId, this.prcDefId, this.actStepDefId, this.taskId, this.backType);
          if (backModel.getMsgInfo() != null) {
            setTargetStepId(backModel.getTargetStepId());
            setTargetStepName(backModel.getTargetStepName());
            List<OrgUser> backUserList = backModel.getReceiveUser();
            setReceiveUser(backUserList);
            this.backUser = new HashMap();
            if (backUserList != null) {
              for (OrgUser orgUser : backUserList) {
                this.backUser.put(orgUser.getUserid(), orgUser.getUsername());
              }
            }
            if (backModel.getMsgInfo().equals("success"))
            {
              this.isParallel = PvmDefExecuteEngine.getInstance().checkActivityIsParallel(this.instanceId);
              this.ccinstal = backModel.getIsCC();
              this.opinionFlag = backModel.getIsOpinion();
              this.action = "驳回";
              this.title = backModel.getTitle();

              this.trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
              return "success";
            }if (backModel.getMsgInfo().equals("ERROR-10011")) {
              setTipsInfo("起草节点不允许执行“驳回”操作");
              return "nofind";
            }if (backModel.getMsgInfo().equals("ERROR-10012")) {
              setTipsInfo("上一步骤为并行办理，无法执行此操作");
              return "nofind";
            }
            setTipsInfo("未找到上一办理人，不能执行“驳回”操作");
            return "nofind";
          }
        }
      }
    }

    this.tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
    return "error";
  }

  public String executeOtherBack()
  {
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.instanceId != null) && (this.excutionId != null) && (this.taskId != null)) {
      if (this.backType == null) {
        this.backType = new Long(1L);
      }

      this.opinion = this.processRuntimeOperateService.getOpinionContent(this.actDefId, this.prcDefId, this.taskId);

      this.remindTypeList = this.processRuntimeOperateService.getTaskRemindCheckBox(this.actDefId, this.prcDefId, this.actStepDefId);

      boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(this.taskId);
      if (flag) {
        HashMap triggerParams = new HashMap();

        BaseTriggerModel triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "TRAN_BACK");
        boolean isrun = true;
        if (triggerModel != null) {
          UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
          HttpServletRequest request = ServletActionContext.getRequest();
          Map params = request.getParameterMap();

          triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
          triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
          triggerParams.put("PROCESS_PARAMETER_FORMID", this.formId);
          triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
          triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
          triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
          triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
          isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
        }

        if (isrun) {
          this.backStepList = this.processRuntimeOperateService.getBackOtherList(this.actDefId, this.prcDefId, this.actStepDefId, this.taskId, this.backType);
          if ((this.backStepList != null) && (this.backStepList.indexOf("option") > 0))
          {
            this.isParallel = PvmDefExecuteEngine.getInstance().checkActivityIsParallel(this.instanceId);

            this.trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
            ProcessStepMap psm = this.processRuntimeOperateService.getProcessStepMapService().getProcessDefMapModel(this.prcDefId, this.actDefId, this.actStepDefId);
            if (psm != null) {
              this.ccinstal = psm.getIsCc();
              this.opinionFlag = psm.getIsAddOpinion();
              this.action = "驳回";
            }
            return "success";
          }
          setTipsInfo("未找到上一办理人，不能执行“驳回”操作");
          return "nofind";
        }
      }
    }

    this.tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
    return "error";
  }

  public String executeCC()
  {
    if ((this.actDefId != null) && (this.prcDefId != null) && (this.actStepDefId != null) && (this.instanceId != null) && (this.taskId != null) && (this.excutionId != null))
    {
      this.opinion = this.processRuntimeOperateService.getOpinionContent(this.actDefId, this.prcDefId, this.taskId);
      try
      {
        if (this.ccUsers != null)
          this.ccUsers = new String(this.ccUsers);
      } catch (Exception localException) {
      }
    }
    return "success";
  }

  public String executeForward()
  {
    if ((this.actDefId != null) && (this.prcDefId != null) && (this.actStepDefId != null) && (this.instanceId != null) && (this.taskId != null) && (this.excutionId != null))
    {
      this.opinion = this.processRuntimeOperateService.getOpinionContent(this.actDefId, this.prcDefId, this.taskId);

      this.remindTypeList = this.processRuntimeOperateService.getTaskRemindCheckBox(this.actDefId, this.prcDefId, this.actStepDefId);
      this.currentUser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
      this.title = this.processRuntimeOperateService.getProcessTaskTitle(this.actDefId, this.prcDefId, this.actStepDefId, this.taskId);
      this.action = "转发";

      this.trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
    }
    return "success";
  }

  public String executeAddSign()
  {
    if ((this.actDefId != null) && (this.prcDefId != null) && (this.actStepDefId != null) && (this.instanceId != null) && (this.taskId != null) && (this.excutionId != null))
    {
      this.opinion = this.processRuntimeOperateService.getOpinionContent(this.actDefId, this.prcDefId, this.taskId);

      ProcessStepMap model = this.processRuntimeOperateService.getProcessStepMapService().getProcessDefMapModel(this.prcDefId, this.actDefId, this.actStepDefId);
      if (model != null) {
        this.ccinstal = model.getIsCc();
        this.opinionFlag = model.getIsAddOpinion();
      }

      this.title = this.processRuntimeOperateService.getProcessTaskTitle(this.actDefId, this.prcDefId, this.actStepDefId, this.taskId);

      this.remindTypeList = this.processRuntimeOperateService.getTaskRemindCheckBox(this.actDefId, this.prcDefId, this.actStepDefId);
      setTargetStepId("999999");
      setTargetStepName("加签操作");
      this.action = "送加签";

      this.trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
    }
    return "success";
  }

  public String showProcessRadioAddress()
  {
    return "success";
  }

  public String executeBackAddSign()
  {
    if (this.backType == null) {
      this.backType = new Long(1L);
    }

    SendPageModel backModel = null;
    if ((this.actDefId != null) && (this.actStepDefId != null) && (this.excutionId != null) && (this.taskId != null))
    {
      this.opinion = this.processRuntimeOperateService.getOpinionContent(this.actDefId, this.prcDefId, this.taskId);

      boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(this.taskId);
      if (flag) {
        backModel = this.processRuntimeOperateService.executeBack(this.actDefId, this.prcDefId, this.actStepDefId, this.taskId, null);
        if (backModel != null) {
          setTargetStepId(backModel.getTargetStepId());
          setTargetStepName(backModel.getTargetStepName());
          this.backUser = new HashMap();
          if (backModel.getReceiveUser() != null) {
            this.receiveUser = backModel.getReceiveUser();
            for (OrgUser orgUser : backModel.getReceiveUser()) {
              this.backUser.put(orgUser.getUserid(), orgUser.getUsername());
            }
          }
          this.opinionFlag = backModel.getIsOpinion();
          this.action = "加签完毕";
          this.remindTypeList = this.processRuntimeOperateService.getTaskRemindCheckBox(this.actDefId, this.prcDefId, backModel.getTargetStepId());

          this.title = backModel.getTitle();

          this.trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
          return "success";
        }
        this.tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
        return "error";
      }

      this.tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
      return "error";
    }

    this.tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
    return "error";
  }

  public String executeSigns()
  {
    this.action = "会签";
    this.remindTypeList = this.processRuntimeOperateService.getTaskRemindCheckBox(this.actDefId, this.prcDefId, this.actStepDefId);
    return "success";
  }

  public void showOpinionContent()
  {
    if ((this.actDefId != null) && (this.prcDefId != null) && (this.taskId != null)) {
      this.opinion = this.processRuntimeOperateService.getOpinionContent(this.actDefId, this.prcDefId, this.taskId);
      ResponseUtil.write(this.opinion);
    }
  }

  public String showOpinionTemplate()
  {
    this.opinions = this.processRuntimeOperateService.getProcessOpinionDAO().loadUserDefinedOpinions();
    if (this.opinions == null) {
      this.processRuntimeOperateService.getProcessOpinionDAO().loadDefaultOpinions();
      this.opinions = this.processRuntimeOperateService.getProcessOpinionDAO().loadUserDefinedOpinions();
    }
    return "success";
  }

  public String getActDefId()
  {
    return this.actDefId;
  }

  public void setActDefId(String actDefId) {
    this.actDefId = actDefId;
  }

  public Long getPrcDefId() {
    return this.prcDefId;
  }

  public void setPrcDefId(Long prcDefId) {
    this.prcDefId = prcDefId;
  }

  public String getActStepDefId() {
    return this.actStepDefId;
  }

  public void setActStepDefId(String actStepDefId) {
    this.actStepDefId = actStepDefId;
  }

  public Long getFormId() {
    return this.formId;
  }

  public void setFormId(Long formId) {
    this.formId = formId;
  }

  public Long getInstanceId() {
    return this.instanceId;
  }

  public void setInstanceId(Long instanceId) {
    this.instanceId = instanceId;
  }

  public Long getExcutionId() {
    return this.excutionId;
  }

  public void setExcutionId(Long excutionId) {
    this.excutionId = excutionId;
  }

  public Long getDataid() {
    return this.dataid;
  }

  public void setDataid(Long dataid) {
    this.dataid = dataid;
  }

  public String getTaskId() {
    return this.taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public ProcessRuntimeOperateService getProcessRuntimeOperateService()
  {
    return this.processRuntimeOperateService;
  }

  public void setProcessRuntimeOperateService(ProcessRuntimeOperateService processRuntimeOperateService) {
    this.processRuntimeOperateService = processRuntimeOperateService;
  }
  public int getMaxUser() {
    return this.maxUser;
  }
  public void setMaxUser(int maxUser) {
    this.maxUser = maxUser;
  }
  public List<SendPageModel> getSendList() {
    return this.sendList;
  }
  public void setSendList(List<SendPageModel> sendList) {
    this.sendList = sendList;
  }
  public String getTargetStepId() {
    return this.targetStepId;
  }
  public void setTargetStepId(String targetStepId) {
    this.targetStepId = targetStepId;
  }
  public List<OrgUser> getReceiveUser() {
    return this.receiveUser;
  }
  public void setReceiveUser(List<OrgUser> receiveUser) {
    this.receiveUser = receiveUser;
  }
  public HashMap getCcUser() {
    return this.ccUser;
  }
  public void setCcUser(HashMap ccUser) {
    this.ccUser = ccUser;
  }
  public String getTargetStepName() {
    return this.targetStepName;
  }
  public void setTargetStepName(String targetStepName) {
    this.targetStepName = targetStepName;
  }
  public String getMjId() {
    return this.mjId;
  }
  public void setMjId(String mjId) {
    this.mjId = mjId;
  }
  public String getCurrentUser() {
    return this.currentUser;
  }
  public void setCurrentUser(String currentUser) {
    this.currentUser = currentUser;
  }
  public String getTipsInfo() {
    return this.tipsInfo;
  }
  public void setTipsInfo(String tipsInfo) {
    this.tipsInfo = tipsInfo;
  }
  public String getCcUsers() {
    return this.ccUsers;
  }
  public void setCcUsers(String ccUsers) {
    this.ccUsers = ccUsers;
  }
  public Long getCcinstal() {
    return this.ccinstal;
  }
  public void setCcinstal(Long ccinstal) {
    this.ccinstal = ccinstal;
  }
  public String getTitle() {
    return this.title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  public ProcessStepTriggerService getProcessStepTriggerService()
  {
    return this.processStepTriggerService;
  }

  public void setProcessStepTriggerService(ProcessStepTriggerService processStepTriggerService)
  {
    this.processStepTriggerService = processStepTriggerService;
  }

  public HashMap getBackUser() {
    return this.backUser;
  }
  public void setBackUser(HashMap backUser) {
    this.backUser = backUser;
  }

  public String getInputField() {
    return this.inputField;
  }

  public void setInputField(String inputField) {
    this.inputField = inputField;
  }

  public HashMap getRemindTypeList() {
    return this.remindTypeList;
  }

  public void setRemindTypeList(HashMap remindTypeList) {
    this.remindTypeList = remindTypeList;
  }

  public String getAction() {
    return this.action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getOpinion() {
    return this.opinion;
  }

  public void setOpinion(String opinion) {
    this.opinion = opinion;
  }

  public Long getOpinionFlag()
  {
    return this.opinionFlag;
  }

  public void setOpinionFlag(Long opinionFlag) {
    this.opinionFlag = opinionFlag;
  }

  public Long getBackType() {
    return this.backType;
  }

  public void setBackType(Long backType) {
    this.backType = backType;
  }

  public boolean isParallel() {
    return this.isParallel;
  }

  public String getTrans_tip() {
    return this.trans_tip;
  }

  public String getBackStepList() {
    return this.backStepList;
  }

  public String getDeviceType() {
    return this.deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }

  public List getOpinions() {
    return this.opinions;
  }

public String getServerflg() {
	return serverflg;
}

public void setServerflg(String serverflg) {
	this.serverflg = serverflg;
}


}