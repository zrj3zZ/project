package com.iwork.process.runtime.action;

import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.trigger.TriggerAPI;
import com.iwork.core.engine.trigger.model.BaseTriggerModel;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.addressbook.service.MultiAddressBookService;
import com.iwork.process.definition.step.model.ProcessStepMap;
import com.iwork.process.definition.step.service.ProcessStepTriggerService;
import com.iwork.process.runtime.constant.SendPageTypeConstant;
import com.iwork.process.runtime.pvm.impl.execute.PvmDefExecuteEngine;
import com.iwork.process.runtime.pvm.impl.execute.PvmProcessSecurityEngine;
import com.iwork.process.runtime.pvm.impl.route.SysRouteUtil;
import com.iwork.process.runtime.pvm.impl.variable.RouteModel;
import com.iwork.process.runtime.pvm.impl.variable.SendPageModel;
import com.iwork.process.runtime.service.ProcessRuntimeOperateService;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class ProcessRuntimeOperateAction extends ActionSupport
{

    public ProcessRuntimeOperateAction()
    {
        actStepDefId = "";
        taskId = "0";
        isOpinion = Long.valueOf(0L);
        isOpinionAttach = Long.valueOf(0L);
        ccinstal = Long.valueOf(0L);
    }
    private final static String CN_FILENAME = "/common.properties";
    private String cslc;
    
    public String getCslc() {
		return cslc;
	}

	public void setCslc(String cslc) {
		this.cslc = cslc;
	}

	public String executeHandle()
    {  
        if(actDefId != null && actStepDefId != null && instanceId != null && excutionId != null && taskId != null)
        {
        	String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
      		boolean matches=this.actDefId.matches(match);
      		if(!matches){
      			
      			return "error";
      		}
      	  if( !DBUTilNew.validActStepId(this.actStepDefId) ){
      		return "error";
    	  }
            deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
            opinion = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
            opinionAttachHtml = processRuntimeOperateService.getOpinionAttach(actDefId, prcDefId, taskId);
            remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
            boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(taskId);
            if(flag)
            {
                HashMap triggerParams = new HashMap();
                BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, "TRAN_BEFOR");
                boolean isrun = true;
                if(triggerModel != null)
                {
                    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
                    HttpServletRequest request = ServletActionContext.getRequest();
                    HashMap params = ParameterMapUtil.getParameterMap(request.getParameterMap());
                    triggerParams.put("PROCESS_PARAMETER_ACTDEFID", actDefId);
                    triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", actStepDefId);
                    triggerParams.put("PROCESS_PARAMETER_FORMID", formId);
                    triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceId);
                    triggerParams.put("PROCESS_PARAMETER_TASKID", taskId);
                    triggerParams.put("PROCESS_PARAMETER_EXECUTEID", excutionId);
                    triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
                    isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, instanceId);
                }
                if(isrun)
                {
                    HttpServletRequest request = ServletActionContext.getRequest();
                    HashMap pageParams = ParameterMapUtil.getParameterMap(request.getParameterMap());
                	RouteModel model = processRuntimeOperateService.executeHandle(actDefId, prcDefId, actStepDefId, taskId, pageParams);
                    
                	/**修改撤消后的提交逻辑-----开始*/
                	boolean checkTsk = ProcessAPI.getInstance().newTaskId(instanceId).getName().equals("任务撤销");
                    if(checkTsk){
                    	String struts=DBUTilNew.getDataStr("NAME_", "select * from (select t.*,rownum rn from (select s.name_ from process_hi_taskinst s where s.proc_inst_id_="+instanceId+" order by s.start_time_ desc) t ) where rn =2", null);
                    	if(!"驳回".equals(struts)){
	                    	List lables = new ArrayList();
	                    	lables.add("TASK_DEF_KEY_");
	                    	lables.add("NAME_");
	                    	lables.add("ASSIGNEE_");

	                    	String sql = "SELECT P.TASK_DEF_KEY_,P.NAME_,P.ASSIGNEE_ FROM (SELECT ROWNUM AS RN,P.* FROM (SELECT P.* FROM PROCESS_HI_TASKINST P WHERE P.PROC_INST_ID_=? ORDER BY ID_ DESC) P) P WHERE P.RN=2";

	                    	Map params = new HashMap();
	                    	params.put(1, instanceId);

	                    	List<HashMap> target = DBUtil.getDataList(lables, sql, params);

                            if (target != null && target.size() == 1) {
                                List ls = new ArrayList();
                                ls.add("EXTEND1");
                                ls.add("EXTEND2");
                                ls.add("EXTEND3");

                                List<HashMap> lst = DBUtil.getDataList(ls, "SELECT EXTEND1,EXTEND2,EXTEND3 FROM process_hi_taskinst WHERE PROC_INST_ID_=" + instanceId+" ORDER BY ID_", null);
                                if (target.get(0).get("NAME_").toString().equals(lst.get(0).get("EXTEND3").toString())) {
                                    List userList = new ArrayList();
                                    userList.add(UserContextUtil.getInstance().getUserContext(target.get(0).get("ASSIGNEE_").toString())._userModel);
                                    String str = SysRouteUtil.getAddressHTML(userList, SysRouteUtil.ROUTE_TYPE_FIXED, "WEB");
                                    model.setAddressHTML(str);
                                    model.setNextStepName(target.get(0).get("NAME_").toString());
                                    model.setNextStepId(target.get(0).get("TASK_DEF_KEY_").toString());
                                }else{
                                    if(!lst.get(0).get("EXTEND3").toString().equals(UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid())){
                                        String jdmc=DBUTilNew.getDataStr("ACT_NAME_","SELECT * FROM( select S.ACT_NAME_ from process_hi_actinst s where s.proc_inst_id_="+instanceId+" and s.assignee_='QIUSHUANG' order by id_ ) WHERE ROWNUM=1",null);
                                        if(jdmc==null || "".equals(jdmc))
                                            jdmc=lst.get(0).get("EXTEND2").toString();
                                        List userList = new ArrayList();
                                        userList.add(UserContextUtil.getInstance().getUserContext(lst.get(0).get("EXTEND3").toString())._userModel);
                                        String str = SysRouteUtil.getAddressHTML(userList, SysRouteUtil.ROUTE_TYPE_FIXED, "WEB");
                                        model.setAddressHTML(str);
                                        model.setNextStepName(jdmc);
                                        model.setNextStepId(lst.get(0).get("EXTEND1").toString());
                                    }
                                }
                            }
                    	}
                    	/**修改撤消后的提交逻辑-----结束*/
                    	
                    }
                    if(model != null)
                    {
                        ccinstal = model.isCC();
                        ccUsers = model.getCcUser();
                        isOpinionAttach = model.getIsOpinionAttach();
                        title = model.getTitle();
                        if(model.getMaxUser() != null)
                            maxUser = model.getMaxUser().intValue();
                        isOpinion = model.getIsOpinion();
                        if(model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE))
                        {
                            List sendList = new ArrayList();
                            SendPageModel sendModel = new SendPageModel();
                            sendModel.setTargetStepId(model.getNextStepId());
                            String t_activityName = model.getNextStepName();
                            if(t_activityName != null)
                                sendModel.setTargetStepName(t_activityName);
                            sendModel.setAddressHTML(model.getAddressHTML());
                            sendModel.setSendPageType(SendPageTypeConstant.SP_TYPE_CHECKLIST_SELECT_ALL);
                            sendList.add(sendModel);
                            setSendList(sendList);
                            if(model.getAction() != null)
                                action = model.getAction();
                            else
                                action = "\u987A\u5E8F\u6D41\u8F6C";
                            trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
                            
                            /*
                             * 之前做了一个更具节点判断是否填写流程审批意见 ，现在全都要填 所以放弃这部分代码
                             * 
                            String zqServer = ConfigUtil.readAllProperties("/common.properties").get("zqServer");
                            String Jd3 = SystemConfig._hlGpsbzkLcConf.getJd3();
                            String Jd2 = SystemConfig._hlGpsbzkLcConf.getJd2();
                            if(zqServer.equals("hlzq")&&(actStepDefId.equals(Jd3)||actStepDefId.equals(Jd2))){
                            	serverflg="verification";
                            }
                            	*/
                            return "success";
                        }
                        if(model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_PARALLELGATEWAY_HANDLE))
                        {
                            List sendList = new ArrayList();
                            SendPageModel sendModel = new SendPageModel();
                            sendModel.setTargetStepId(model.getNextStepId());
                            String t_activityName = model.getNextStepName();
                            if(t_activityName != null)
                                sendModel.setTargetStepName(t_activityName);
                            sendModel.setAddressHTML(model.getAddressHTML());
                            sendModel.setSendPageType(SendPageTypeConstant.SP_TYPE_CHECKLIST_SELECT_ALL);
                            sendList.add(sendModel);
                            setSendList(sendList);
                            if(model.getAction() != null)
                                action = model.getAction();
                            else
                                action = "\u987A\u5E8F\u6D41\u8F6C";
                            trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
                            return "PARALLELGATEWAY";
                        }
                        if(model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_WAIT))
                        {
                            action = "\u987A\u5E8F\u6D41\u8F6C";
                            targetStepId = model.getNextStepId();
                            targetStepName = model.getNextStepName();
                            trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
                            return "SERVICE";
                        }
                        if(model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES))
                        {
                            action = "\u5F52\u6863";
                            int indexOf = actDefId.indexOf(":");
                            cslc=actDefId.substring(0, indexOf);
//                            Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
//                            String result=config.get("chaosong");
//                            String[] split = result.split(",");
                            Boolean fla=false;
//                            for (int i = 0; i < split.length; i++) {
//                            	if(cslc.equals(split[i])){
//                            		fla=true;
//                            		break;
//                            	};
//							}
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
                        if(model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_SIGNS))
                        {
                            action = "\u4F1A\u7B7E";
                            trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
                            return "success";
                        }
                    }
                }
            }
        }
        tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
        return "error";
    }

    public String executeManualJump()
    {
        if(actDefId != null && actStepDefId != null && instanceId != null && excutionId != null && taskId != null && targetStepId != null)
        {
        	String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
      		boolean matches=this.actDefId.matches(match);
      		if(!matches){
      			
      			return "error";
      		}
      	  if( !DBUTilNew.validActStepId(this.actStepDefId) ){
      		return "error";
    	  }
            remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
            opinion = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
            opinionAttachHtml = processRuntimeOperateService.getOpinionAttach(actDefId, prcDefId, taskId);
            boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(taskId);
            if(flag)
            {
                HashMap triggerParams = new HashMap();
                BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, "TRAN_BEFOR");
                boolean isrun = true;
                if(triggerModel != null)
                {
                    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
                    HttpServletRequest request = ServletActionContext.getRequest();
                    java.util.Map params = ParameterMapUtil.getParameterMap(request.getParameterMap());
                    triggerParams.put("PROCESS_PARAMETER_ACTDEFID", actDefId);
                    triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", actStepDefId);
                    triggerParams.put("PROCESS_PARAMETER_FORMID", formId);
                    triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceId);
                    triggerParams.put("PROCESS_PARAMETER_TASKID", taskId);
                    triggerParams.put("PROCESS_PARAMETER_EXECUTEID", excutionId);
                    triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
                    isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, instanceId);
                }
                if(isrun)
                {
                    HttpServletRequest request = ServletActionContext.getRequest();
                    HashMap pageParams = ParameterMapUtil.getParameterMap(request.getParameterMap());
                    RouteModel model = processRuntimeOperateService.executeManualJump(actDefId, prcDefId, actStepDefId, taskId, targetStepId, mjId, pageParams);
                    List sendList = new ArrayList();
                    SendPageModel sendModel = new SendPageModel();
                    sendModel.setTargetStepId(model.getNextStepId());
                    String t_activityName = model.getNextStepName();
                    if(t_activityName != null)
                        sendModel.setTargetStepName(t_activityName);
                    ccinstal = model.isCC();
                    ccUsers = model.getCcUser();
                    isOpinion = model.getIsOpinion();
                    if(model.getMaxUser() != null)
                        maxUser = model.getMaxUser().intValue();
                    sendModel.setAddressHTML(model.getAddressHTML());
                    sendModel.setSendPageType(SendPageTypeConstant.SP_TYPE_CHECKLIST_SELECT_ALL);
                    sendList.add(sendModel);
                    setSendList(sendList);
                    if(model.getAction() != null)
                        action = model.getAction();
                    else
                        action = "\u4EFB\u52A1\u8DF3\u8F6C";
                    if(model.getSendPageCode() != null && model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES))
                    {
                        title = model.getTitle();
                        trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
                        return "ARCHIVES";
                    } else
                    {
                        trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
                        return "success";
                    }
                } else
                {
                    tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
                    return "error";
                }
            } else
            {
                tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
                return "error";
            }
        } else
        {
            tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
            return "error";
        }
    }

    public String executeBack()
    {
        SendPageModel backModel = null;
        if(actDefId != null && actStepDefId != null && instanceId != null && excutionId != null && taskId != null)
        {
            if(backType == null)
                backType = new Long(1L);
            opinion = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
            opinionAttachHtml = processRuntimeOperateService.getOpinionAttach(actDefId, prcDefId, taskId);
            remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
            boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(taskId);
            if(flag)
            {
                HashMap triggerParams = new HashMap();
                BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, "TRAN_BACK");
                boolean isrun = true;
                if(triggerModel != null)
                {
                    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
                    HttpServletRequest request = ServletActionContext.getRequest();
                    java.util.Map params = ParameterMapUtil.getParameterMap(request.getParameterMap());
                    triggerParams.put("PROCESS_PARAMETER_ACTDEFID", actDefId);
                    triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", actStepDefId);
                    triggerParams.put("PROCESS_PARAMETER_FORMID", formId);
                    triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceId);
                    triggerParams.put("PROCESS_PARAMETER_TASKID", taskId);
                    triggerParams.put("PROCESS_PARAMETER_EXECUTEID", excutionId);
                    triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
                    isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, instanceId);
                }
                if(isrun)
                {
                    if(backType==1){
                        List lables=new ArrayList();
                        lables.add("act_name_");
                        lables.add("assignee_");
                        lables.add("act_id_");
                        lables.add("rn");
                        List<HashMap> list=DBUTilNew.getDataList(lables,"select t.act_name_,t.assignee_,t.act_id_,rownum rn from(select s.* from process_hi_actinst s where s.proc_inst_id_="+instanceId+" order by id_) t",null);
                        backModel = processRuntimeOperateService.executeBack(actDefId, prcDefId, actStepDefId, taskId, backType);
                        if(list!=null && list.size()>0){
                            UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
                            if(backModel.getMsgInfo() != null){
                                boolean status=true;
                                for (int i=0;i<list.size();i++){
                                    if(!status) break;
                                    if(uc.get_userModel().getUserid().equals(list.get(i).get("assignee_"))){
                                        int rn=Integer.parseInt(list.get(i).get("rn").toString())-1;
                                        for (int j=0;j<list.size();j++){
                                            if(Integer.parseInt(list.get(j).get("rn").toString())==rn){
                                            //   if( !backModel.getReceiveUser().get(0).getUserid().toString().equals(list.get(j).get("assignee_").toString())){
                                                   backModel.setTargetStepId(list.get(j).get("act_id_").toString());
                                                   backModel.setTargetStepName(list.get(j).get("act_name_").toString());
                                                   UserContext targetuser = UserContextUtil.getInstance().getUserContext(list.get(j).get("assignee_").toString());
                                                   List<OrgUser> receiveUser = new ArrayList();
                                                   receiveUser.add(targetuser.get_userModel());
                                                   backModel.setReceiveUser(receiveUser);
                                                   status=false;
                                                   break;
                                             //  }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        backModel = processRuntimeOperateService.executeBack(actDefId, prcDefId, actStepDefId, taskId, backType);
                    }
                    if(backModel.getMsgInfo() != null)
                    {
                        setTargetStepId(backModel.getTargetStepId());
                        setTargetStepName(backModel.getTargetStepName());
                        List backUserList = backModel.getReceiveUser();
                        setReceiveUser(backUserList);
                        backUser = new HashMap();
                        if(backUserList != null)
                        {
                            OrgUser orgUser;
                            for(Iterator iterator = backUserList.iterator(); iterator.hasNext(); backUser.put(orgUser.getUserid(), orgUser.getUsername()))
                                orgUser = (OrgUser)iterator.next();

                        }
                        if(backModel.getMsgInfo().equals("success"))
                        {
                            isParallel = PvmDefExecuteEngine.getInstance().checkActivityIsParallel(instanceId);
                            ccinstal = backModel.getIsCC();
                            isOpinion = backModel.getIsOpinion();
                            action = "\u9A73\u56DE";
                            title = backModel.getTitle();
                            trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
                            return "success";
                        }
                        if(backModel.getMsgInfo().equals("ERROR-10011"))
                        {
                            setTipsInfo("\u8D77\u8349\u8282\u70B9\u4E0D\u5141\u8BB8\u6267\u884C\u201C\u9A73\u56DE\u201D\u64CD\u4F5C");
                            return "nofind";
                        }
                        if(backModel.getMsgInfo().equals("ERROR-10012"))
                        {
                            setTipsInfo("\u4E0A\u4E00\u6B65\u9AA4\u4E3A\u5E76\u884C\u529E\u7406\uFF0C\u65E0\u6CD5\u6267\u884C\u6B64\u64CD\u4F5C");
                            return "nofind";
                        } else
                        {
                            setTipsInfo("\u672A\u627E\u5230\u4E0A\u4E00\u529E\u7406\u4EBA\uFF0C\u4E0D\u80FD\u6267\u884C\u201C\u9A73\u56DE\u201D\u64CD\u4F5C");
                            return "nofind";
                        }
                    }
                }
            }
        }
        tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
        return "error";
    }

    public String executeOtherBack()
    {
        if(actDefId != null && actStepDefId != null && instanceId != null && excutionId != null && taskId != null)
        {
            if(backType == null)
                backType = new Long(1L);
            opinion = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
            opinionAttachHtml = processRuntimeOperateService.getOpinionAttach(actDefId, prcDefId, taskId);
            remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
            boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(taskId);
            if(flag)
            {
                HashMap triggerParams = new HashMap();
                BaseTriggerModel triggerModel = processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepDefId, "TRAN_BACK");
                boolean isrun = true;
                if(triggerModel != null)
                {
                    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
                    HttpServletRequest request = ServletActionContext.getRequest();
                    java.util.Map params = ParameterMapUtil.getParameterMap(request.getParameterMap());
                    triggerParams.put("PROCESS_PARAMETER_ACTDEFID", actDefId);
                    triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", actStepDefId);
                    triggerParams.put("PROCESS_PARAMETER_FORMID", formId);
                    triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceId);
                    triggerParams.put("PROCESS_PARAMETER_TASKID", taskId);
                    triggerParams.put("PROCESS_PARAMETER_EXECUTEID", excutionId);
                    triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
                    isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, instanceId);
                }
                if(isrun)
                {
                    backStepList = processRuntimeOperateService.getBackOtherList(actDefId, prcDefId, actStepDefId, taskId, backType);
                    if(backStepList != null && backStepList.indexOf("option") > 0)
                    {
                        isParallel = PvmDefExecuteEngine.getInstance().checkActivityIsParallel(instanceId);
                        trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
                        ProcessStepMap psm = processRuntimeOperateService.getProcessStepMapService().getProcessDefMapModel(prcDefId, actDefId, actStepDefId);
                        if(psm != null)
                        {
                            ccinstal = psm.getIsCc();
                            isOpinion = psm.getIsAddOpinion();
                            action = "\u9A73\u56DE";
                        }
                        return "success";
                    } else
                    {
                        setTipsInfo("\u672A\u627E\u5230\u4E0A\u4E00\u529E\u7406\u4EBA\uFF0C\u4E0D\u80FD\u6267\u884C\u201C\u9A73\u56DE\u201D\u64CD\u4F5C");
                        return "nofind";
                    }
                }
            }
        }
        tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
        return "error";
    }

    public String executeCC()
    {
        if(actDefId != null && prcDefId != null && actStepDefId != null && instanceId != null && taskId != null && excutionId != null)
            opinion = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
        title = processRuntimeOperateService.getProcessTaskTitle(actDefId, prcDefId, actStepDefId, taskId);
        try
        {
            if(ccUsers != null)
            {
                ccUsers = new String(ccUsers);
                selectJSON = multiAddressBookService.getSelectOrgJson(ccUsers);
            }
        }
        catch(Exception exception) { }
        return "success";
    }

    public String executeForward()
    {
        if(actDefId != null && prcDefId != null && actStepDefId != null && instanceId != null && taskId != null && excutionId != null)
        {
            opinion = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
            opinionAttachHtml = processRuntimeOperateService.getOpinionAttach(actDefId, prcDefId, taskId);
            remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
            currentUser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
            title = processRuntimeOperateService.getProcessTaskTitle(actDefId, prcDefId, actStepDefId, taskId);
            action = "\u8F6C\u53D1";
            trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
        }
        return "success";
    }

    public String executeAddSign()
    {
        if(actDefId != null && prcDefId != null && actStepDefId != null && instanceId != null && taskId != null && excutionId != null)
        {
            opinion = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
            opinionAttachHtml = processRuntimeOperateService.getOpinionAttach(actDefId, prcDefId, taskId);
            ProcessStepMap model = processRuntimeOperateService.getProcessStepMapService().getProcessDefMapModel(prcDefId, actDefId, actStepDefId);
            if(model != null)
            {
                ccinstal = model.getIsCc();
                isOpinion = model.getIsAddOpinion();
            }
            title = processRuntimeOperateService.getProcessTaskTitle(actDefId, prcDefId, actStepDefId, taskId);
            remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
            setTargetStepId("999999");
            setTargetStepName("\u52A0\u7B7E\u64CD\u4F5C");
            action = "\u9001\u52A0\u7B7E";
            trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
        }
        return "success";
    }

    public String showProcessRadioAddress()
    {
        return "success";
    }

    public String executeBackAddSign()
    {
        if(backType == null)
            backType = new Long(1L);
        SendPageModel backModel = null;
        if(actDefId != null && actStepDefId != null && excutionId != null && taskId != null)
        {
            opinion = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
            opinionAttachHtml = processRuntimeOperateService.getOpinionAttach(actDefId, prcDefId, taskId);
            boolean flag = PvmProcessSecurityEngine.getInstance().checkProcessHandleOperation(taskId);
            if(flag)
            {
                backModel = processRuntimeOperateService.executeBack(actDefId, prcDefId, actStepDefId, taskId, null);
                if(backModel != null)
                {
                    setTargetStepId(backModel.getTargetStepId());
                    setTargetStepName(backModel.getTargetStepName());
                    backUser = new HashMap();
                    if(backModel.getReceiveUser() != null)
                    {
                        receiveUser = backModel.getReceiveUser();
                        OrgUser orgUser;
                        for(Iterator iterator = backModel.getReceiveUser().iterator(); iterator.hasNext(); backUser.put(orgUser.getUserid(), orgUser.getUsername()))
                            orgUser = (OrgUser)iterator.next();

                    }
                    isOpinion = backModel.getIsOpinion();
                    action = "\u52A0\u7B7E\u5B8C\u6BD5";
                    remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, backModel.getTargetStepId());
                    title = backModel.getTitle();
                    trans_tip = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TRANS_TIP);
                    return "success";
                } else
                {
                    tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
                    return "error";
                }
            } else
            {
                tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
                return "error";
            }
        } else
        {
            tipsInfo = MessageQueueUtil.getInstance().getMessageForCurrentUserStr(MessageQueueUtil.MSG_TYPE_TIP);
            return "error";
        }
    }

    public String executeSignsNext()
    {
        return executeHandle();
    }

    public String executeSigns()
    {
        action = "\u4F1A\u7B7E";
        remindTypeList = processRuntimeOperateService.getTaskRemindCheckBox(actDefId, prcDefId, actStepDefId);
        return "success";
    }

    public void showOpinionContent()
    {
        if(actDefId != null && prcDefId != null && taskId != null)
        {
            opinion = processRuntimeOperateService.getOpinionContent(actDefId, prcDefId, taskId);
            ResponseUtil.write(opinion);
        }
    }

    public String getActDefId()
    {
        return actDefId;
    }

    public void setActDefId(String actDefId)
    {
        this.actDefId = actDefId;
    }

    public Long getPrcDefId()
    {
        return prcDefId;
    }

    public void setPrcDefId(Long prcDefId)
    {
        this.prcDefId = prcDefId;
    }

    public String getActStepDefId()
    {
        return actStepDefId;
    }

    public void setActStepDefId(String actStepDefId)
    {
        this.actStepDefId = actStepDefId;
    }

    public Long getFormId()
    {
        return formId;
    }

    public void setFormId(Long formId)
    {
        this.formId = formId;
    }

    public Long getInstanceId()
    {
        return instanceId;
    }

    public void setInstanceId(Long instanceId)
    {
        this.instanceId = instanceId;
    }

    public Long getExcutionId()
    {
        return excutionId;
    }

    public void setExcutionId(Long excutionId)
    {
        this.excutionId = excutionId;
    }

    public Long getDataid()
    {
        return dataid;
    }

    public void setDataid(Long dataid)
    {
        this.dataid = dataid;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public ProcessRuntimeOperateService getProcessRuntimeOperateService()
    {
        return processRuntimeOperateService;
    }

    public void setProcessRuntimeOperateService(ProcessRuntimeOperateService processRuntimeOperateService)
    {
        this.processRuntimeOperateService = processRuntimeOperateService;
    }

    public int getMaxUser()
    {
        return maxUser;
    }

    public void setMaxUser(int maxUser)
    {
        this.maxUser = maxUser;
    }

    public List getSendList()
    {
        return sendList;
    }

    public void setSendList(List sendList)
    {
        this.sendList = sendList;
    }

    public String getTargetStepId()
    {
        return targetStepId;
    }

    public void setTargetStepId(String targetStepId)
    {
        this.targetStepId = targetStepId;
    }

    public List getReceiveUser()
    {
        return receiveUser;
    }

    public void setReceiveUser(List receiveUser)
    {
        this.receiveUser = receiveUser;
    }

    public HashMap getCcUser()
    {
        return ccUser;
    }

    public void setCcUser(HashMap ccUser)
    {
        this.ccUser = ccUser;
    }

    public String getTargetStepName()
    {
        return targetStepName;
    }

    public void setTargetStepName(String targetStepName)
    {
        this.targetStepName = targetStepName;
    }

    public String getMjId()
    {
        return mjId;
    }

    public void setMjId(String mjId)
    {
        this.mjId = mjId;
    }

    public String getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(String currentUser)
    {
        this.currentUser = currentUser;
    }

    public String getTipsInfo()
    {
        return tipsInfo;
    }

    public void setTipsInfo(String tipsInfo)
    {
        this.tipsInfo = tipsInfo;
    }

    public String getCcUsers()
    {
        return ccUsers;
    }

    public void setCcUsers(String ccUsers)
    {
        this.ccUsers = ccUsers;
    }

    public Long getCcinstal()
    {
        return ccinstal;
    }

    public void setCcinstal(Long ccinstal)
    {
        this.ccinstal = ccinstal;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public ProcessStepTriggerService getProcessStepTriggerService()
    {
        return processStepTriggerService;
    }

    public void setProcessStepTriggerService(ProcessStepTriggerService processStepTriggerService)
    {
        this.processStepTriggerService = processStepTriggerService;
    }

    public HashMap getBackUser()
    {
        return backUser;
    }

    public void setBackUser(HashMap backUser)
    {
        this.backUser = backUser;
    }

    public String getInputField()
    {
        return inputField;
    }

    public void setInputField(String inputField)
    {
        this.inputField = inputField;
    }

    public HashMap getRemindTypeList()
    {
        return remindTypeList;
    }

    public void setRemindTypeList(HashMap remindTypeList)
    {
        this.remindTypeList = remindTypeList;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getOpinion()
    {
        return opinion;
    }

    public void setOpinion(String opinion)
    {
        this.opinion = opinion;
    }

    public Long getIsOpinion()
    {
        return isOpinion;
    }

    public void setIsOpinion(Long isOpinion)
    {
        this.isOpinion = isOpinion;
    }

    public Long getBackType()
    {
        return backType;
    }

    public void setBackType(Long backType)
    {
        this.backType = backType;
    }

    public boolean isParallel()
    {
        return isParallel;
    }

    public String getTrans_tip()
    {
        return trans_tip;
    }

    public String getBackStepList()
    {
        return backStepList;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public Long getIsOpinionAttach()
    {
        return isOpinionAttach;
    }

    public void setIsOpinionAttach(Long isOpinionAttach)
    {
        this.isOpinionAttach = isOpinionAttach;
    }

    public String getOpinionAttachHtml()
    {
        return opinionAttachHtml;
    }

    public void setOpinionAttachHtml(String opinionAttachHtml)
    {
        this.opinionAttachHtml = opinionAttachHtml;
    }

    public String getSelectJSON()
    {
        return selectJSON;
    }

    public void setMultiAddressBookService(MultiAddressBookService multiAddressBookService)
    {
        this.multiAddressBookService = multiAddressBookService;
    }

   
	public String getServerflg() {
		return serverflg;
	}

	public void setServerflg(String serverflg) {
		this.serverflg = serverflg;
	}
	private ProcessRuntimeOperateService processRuntimeOperateService;
    private ProcessStepTriggerService processStepTriggerService;
    private MultiAddressBookService multiAddressBookService;
    private String actDefId;
    private String action;
    private Long prcDefId;
    private String actStepDefId;
    private Long formId;
    private String taskId;
    private String currentUser;
    private Long instanceId;
    private Long excutionId;
    private Long dataid;
    private List sendList;
    private String serverflg; //等于verification时。华龙的 公告呈报，日常业务呈报时 督导部门负责人，业务部负责人意见必填时使用
    private String mjId;
    private String targetStepId;
    private String targetStepName;
    private String title;
    private String opinion;
    private Long isOpinion;
    private Long isOpinionAttach;
    private boolean isParallel;
    private String ccUsers;
    private List receiveUser;
    private HashMap backUser;
    private HashMap ccUser;
    private int maxUser;
    private Long ccinstal;
    private String tipsInfo;
    private String inputField;
    private Long backType;
    private HashMap remindTypeList;
    private String backStepList;
    private String tips_Title;
    private String trans_tip;
    private String tips_Content;
    private String deviceType;
    private String opinionAttachHtml;
    private String selectJSON;
}
