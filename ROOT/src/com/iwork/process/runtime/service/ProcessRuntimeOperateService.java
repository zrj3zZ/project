//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iwork.process.runtime.service;

import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.FileType;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.ObjectUtil;
import com.iwork.commons.util.PurviewCommonUtil;
import com.iwork.core.constant.SysConst;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.iform.dao.SysEngineIFormDAO;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.definition.deployment.model.ProcessDefDeploy;
import com.iwork.process.definition.flow.model.ProcessDefActionset;
import com.iwork.process.definition.flow.model.ProcessDefMap;
import com.iwork.process.definition.flow.service.ProcessDefActionService;
import com.iwork.process.definition.flow.service.ProcessDefMapService;
import com.iwork.process.definition.flow.service.ProcessDefParamService;
import com.iwork.process.definition.flow.service.ProcessDefTriggerService;
import com.iwork.process.definition.step.dao.ProcessStepFormDAO;
import com.iwork.process.definition.step.model.ProcessStepManualJump;
import com.iwork.process.definition.step.model.ProcessStepMap;
import com.iwork.process.definition.step.model.ProcessStepRoute;
import com.iwork.process.definition.step.model.ProcessStepSysjump;
import com.iwork.process.definition.step.model.ProcessStepTrigger;
import com.iwork.process.definition.step.service.ProcessStepFormMapService;
import com.iwork.process.definition.step.service.ProcessStepFormService;
import com.iwork.process.definition.step.service.ProcessStepManualJumpService;
import com.iwork.process.definition.step.service.ProcessStepMapService;
import com.iwork.process.definition.step.service.ProcessStepRouteService;
import com.iwork.process.definition.step.service.ProcessStepSysjumpService;
import com.iwork.process.definition.step.service.ProcessStepTriggerService;
import com.iwork.process.definition.step.util.ProcessDefinitionStepUtil;
import com.iwork.process.runtime.constant.BackTypeConstant;
import com.iwork.process.runtime.constant.SendPageTypeConstant;
import com.iwork.process.runtime.dao.ProcessRuntimeCcDAO;
import com.iwork.process.runtime.model.ProcessRuCc;
import com.iwork.process.runtime.pvm.impl.execute.PvmDefExecuteEngine;
import com.iwork.process.runtime.pvm.impl.route.PvmRouteEngine;
import com.iwork.process.runtime.pvm.impl.route.SysRouteFactory;
import com.iwork.process.runtime.pvm.impl.route.SysRouteSchemaAbst;
import com.iwork.process.runtime.pvm.impl.route.SysRouteUtil;
import com.iwork.process.runtime.pvm.impl.system.PvmSystemJumpEngine;
import com.iwork.process.runtime.pvm.impl.trigger.PvmStepTriggerEngine;
import com.iwork.process.runtime.pvm.impl.variable.RouteModel;
import com.iwork.process.runtime.pvm.impl.variable.SendPageModel;
import com.iwork.process.runtime.pvm.impl.variable.SysJumpParams;
import com.iwork.process.tools.processopinion.dao.ProcessOpinionDAO;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.ProcessAPI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ReceiveTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.helper.ClassDelegate;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProcessRuntimeOperateService {
    private static final Log log = LogFactory.getLog(ProcessRuntimeOperateService.class);
    private ProcessEngine processEngine;
    private TaskService taskService;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;
    private ProcessStepFormDAO processStepFormDAO;
    private SysEngineIFormDAO sysEngineIFormDAO;
    private IFormService iformService;
    private ProcessRuntimeCcDAO processRuntimeCcDAO;
    private ProcessOpinionDAO processOpinionDAO;
    private ProcessDefActionService processDefActionService;
    private ProcessDefMapService processDefMapService;
    private ProcessDefParamService processDefParamService;
    private ProcessDefTriggerService processDefTriggerService;
    private ProcessStepFormService processStepFormService;
    private ProcessStepFormMapService processStepFormMapService;
    private ProcessStepManualJumpService processStepManualJumpService;
    private ProcessStepMapService processStepMapService;
    private ProcessStepSysjumpService processStepSysjumpService;
    private ProcessStepTriggerService processStepTriggerService;
    private ProcessRuntimeSignsService processRuntimeSignsService;
    private ProcessStepRouteService processStepRouteService;

    public ProcessRuntimeOperateService() {
    }

    public RouteModel executeHandle(String actDefId, Long prcDefId, String actStepId, String taskId, HashMap pageParams) {
        if (actStepId != null && !"".equals(actStepId)) {
            RouteModel model = new RouteModel();
            PvmDefExecuteEngine pvmDefExecuteEngine = PvmDefExecuteEngine.getInstance();
            PvmSystemJumpEngine pvmSystemJumpEngine = PvmSystemJumpEngine.getInstance();
            PvmStepTriggerEngine pvmStepTriggerEngine = PvmStepTriggerEngine.getInstance();
            RepositoryService rps = this.processEngine.getRepositoryService();
            ProcessDefinitionEntity pde = (ProcessDefinitionEntity)((RepositoryServiceImpl)rps).getDeployedProcessDefinition(actDefId);
            ProcessStepMap stepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepId);
            if (stepMap == null) {
                return null;
            } else {
                if (stepMap != null && stepMap.getIsCc().equals(1L)) {
                    model.setCC(1L);
                }

                if (stepMap.getIsAddOpinion() != null && stepMap.getIsAddOpinion().equals(SysConst.on)) {
                    model.setIsOpinion(SysConst.on);
                } else {
                    model.setIsOpinion(SysConst.off);
                }

                if (stepMap.getIsOpinionAttach() != null && stepMap.getIsOpinionAttach().equals(SysConst.on)) {
                    model.setIsOpinionAttach(SysConst.on);
                } else {
                    model.setIsOpinionAttach(SysConst.off);
                }

                String activityId = null;
                ProcessDefMap defMap = this.processDefMapService.getProcessDefMapModel(prcDefId, actDefId);
                Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
                if (defMap != null && defMap.getDefTitle() != null && defMap.getDefTitle().indexOf("%") > -1) {
                    model.setTitle(this.reBuildTitle(task, defMap.getDefTitle()));
                } else if (task != null) {
                    model.setTitle(task.getDescription());
                }

                if (task != null && stepMap.getStepType() != null && stepMap.getStepType().equals(new Long(2L))) {
                    boolean isCheck = false;
                    List list = this.processRuntimeSignsService.getProcessRuntimeSignsDAO().getFinishSignsList(actDefId, actStepId, Long.parseLong(task.getProcessInstanceId()), Long.parseLong(task.getExecutionId()), Long.parseLong(taskId));
                    if (list != null && list.size() == 0) {
                        int size = this.processRuntimeSignsService.getProcessRuntimeSignsDAO().getUnSignsSize(actDefId, actStepId, Long.parseLong(task.getProcessInstanceId()), Long.parseLong(task.getExecutionId()), Long.parseLong(taskId));
                        if (size != 0) {
                            isCheck = true;
                        }
                    } else if (list != null && list.size() > 0) {
                        isCheck = true;
                    }

                    if (!isCheck) {
                        model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE);
                        model.setAddressHTML("<div style=\"text-align:center\"><img border='0' src='iwork_img/warning.jpg'>当前节点为会签节点，请点击【发送会签】按钮，完成会签后执行顺序办理</div>");
                        return model;
                    }
                }

                Object backUserId = null;
                Object backStepId = null;

                try {
                    backUserId = this.runtimeService.getVariable(task.getExecutionId(), BackTypeConstant.BACK_TO_BACK_USERID);
                    backStepId = this.runtimeService.getVariable(task.getExecutionId(), BackTypeConstant.BACK_TO_BACK_STEPID);
                } catch (Exception var32) {
                    ;
                }

                if (backUserId != null && backStepId != null) {
                    String backUser = backUserId.toString();
                    String backStep = backStepId.toString();
                    String currentUserId = UserContextUtil.getInstance().getCurrentUserId();
                    if (currentUserId.equals(backUser) && backStep.equals(actStepId)) {
                        model.setAction("驳回响应");
                        Object backSrcUserId = this.runtimeService.getVariable(task.getExecutionId(), BackTypeConstant.BACK_TO_BACK_SRC_USERID);
                        Object backSrcStepId = this.runtimeService.getVariable(task.getExecutionId(), BackTypeConstant.BACK_TO_BACK_SRC_STEPID);
                        Object backSrcStepName = this.runtimeService.getVariable(task.getExecutionId(), BackTypeConstant.BACK_TO_BACK_SRC_STEPNAME);
                        if (backSrcStepName == null) {
                            ProcessStepMap processStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, backSrcStepId.toString());
                            if (processStepMap != null) {
                                model.setNextStepName(processStepMap.getStepTitle());
                            }
                        } else {
                            model.setNextStepName(backSrcStepName.toString());
                        }

                        model.setNextStepId(backSrcStepId.toString());
                        List<OrgUser> userList = new ArrayList();
                        HashMap fixedNextUser = new HashMap();
                        boolean isaddress = UserContextUtil.getInstance().checkAddress(backSrcUserId.toString());
                        String addressHtml;
                        if (isaddress) {
                            addressHtml = UserContextUtil.getInstance().getUserId(backSrcUserId.toString());
                            UserContext targetuser = UserContextUtil.getInstance().getUserContext(addressHtml);
                            fixedNextUser.put(addressHtml, targetuser.get_userModel());
                            userList.add(targetuser.get_userModel());
                        }

                        model.setFixedNextUser(fixedNextUser);
                        model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE);
                        addressHtml = SysRouteUtil.getAddressHTML(userList, SysRouteUtil.ROUTE_TYPE_FIXED, "WEB");
                        model.setAddressHTML(addressHtml);
                        return model;
                    }
                }

                List<ProcessStepSysjump> list = this.processStepSysjumpService.getProcessStepSysjumpList(actDefId, prcDefId, actStepId);
                UserContext context = UserContextUtil.getInstance().getCurrentUserContext();
                if (context == null) {
                    context = UserContextUtil.getInstance().getUserContext("ADMIN");
                }

                String ccUsers;
                String owner;
                int count;
                String temp;
                if (list != null) {
                    Iterator var21 = list.iterator();

                    label328:
                    while(true) {
                        ProcessStepSysjump pssj;
                        do {
                            do {
                                if (!var21.hasNext()) {
                                    break label328;
                                }

                                pssj = (ProcessStepSysjump)var21.next();
                            } while(!pvmSystemJumpEngine.checkSysJumpPurview(pssj, context));
                        } while(!pvmSystemJumpEngine.filterSysJumpRule(pssj, prcDefId, task, pageParams));

                        if (pssj.getIsJump() != null && pssj.getIsJump().equals(SysConst.on) && pssj.getTargetStep() != null) {
                            activityId = pssj.getTargetStep();
                        }

                        if (pssj.getIsNextuser() != null && pssj.getIsNextuser().equals(new Long(1L))) {
                            ccUsers = pssj.getNextUser();
                            if (ccUsers != null) {
                                String[] nextUser = ccUsers.split(",");
                                HashMap fixedNextUser = new HashMap();
                                String[] var28 = nextUser;
                                count = nextUser.length;

                                for(int var26 = 0; var26 < count; ++var26) {
                                    owner = var28[var26];
                                    boolean isaddress = UserContextUtil.getInstance().checkAddress(owner.trim());
                                    if (isaddress) {
                                        String userid = UserContextUtil.getInstance().getUserId(owner);
                                        UserContext targetuser = UserContextUtil.getInstance().getUserContext(userid);
                                        fixedNextUser.put(userid, targetuser.get_userModel());
                                    }
                                }

                                model.setFixedNextUser(fixedNextUser);
                            }
                        }

                        if (pssj.getIsRemind() != null && pssj.getIsRemind().equals(new Long(1L)) && pssj.getMsgUsers() != null) {
                            new ArrayList();
                            temp = pssj.getMsgUsers();
                            if (temp != null) {
                                model.setCcUser(temp);
                            }
                        }

                        model.setAction("规则跳转");
                    }
                }

                if (activityId == null) {
                    ProcessStepTrigger psr = this.processStepTriggerService.getProcessStepTriggerModel(actDefId, prcDefId, actStepId, "SYSTEM_JUMP");
                    if (psr != null) {
                        SysJumpParams sjp = pvmStepTriggerEngine.getSysJumpTrigger(context, psr, task);
                        if (sjp != null) {
                            if (sjp.getNextStepId() != null) {
                                activityId = sjp.getNextStepId();
                                model.setAction("规则跳转[*]");
                            }

                            HashMap nextUserList = sjp.getNextUserList();
                            model.setFixedNextUser(nextUserList);
                            if (model.isCC().equals(SysConst.on)) {
                                if (sjp.getCcUserId() != null && model.getCcUser() != null) {
                                    model.setCcUser(model.getCcUser() + "," + sjp.getCcUserId());
                                } else {
                                    model.setCcUser(sjp.getCcUserId());
                                }
                            }
                        }
                    }
                }

                ActivityImpl targetActivity;
                if (activityId == null) {
                    targetActivity = pde.findActivity(actStepId);
                    List<PvmTransition> out_trans = targetActivity.getOutgoingTransitions();
                    Iterator var49 = out_trans.iterator();

                    while(var49.hasNext()) {
                        PvmTransition pt = (PvmTransition)var49.next();
                        targetActivity = (ActivityImpl)pt.getDestination();
                        if (targetActivity != null) {
                            if (targetActivity.getActivityBehavior() instanceof NoneEndEventActivityBehavior) {
                                activityId = targetActivity.getId();
                                model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES);
                                break;
                            }

                            if (targetActivity.getActivityBehavior() instanceof UserTaskActivityBehavior) {
                                activityId = targetActivity.getId();
                                model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE);
                                break;
                            }

                            if (targetActivity.getActivityBehavior() instanceof ParallelGatewayActivityBehavior) {
                                owner = task.getProcessInstanceId();
                                List<Task> parallTaskList = this.taskService.createTaskQuery().processDefinitionId(actDefId).processInstanceId(owner).list();
                                if (parallTaskList.size() <= 1) {
                                    model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE);
                                } else {
                                    count = 0;
                                    Iterator var64 = parallTaskList.iterator();

                                    while(var64.hasNext()) {
                                        Task tmpTask = (Task)var64.next();
                                        if (!tmpTask.getId().equals(task.getId()) && !tmpTask.getTaskDefinitionKey().equals(targetActivity.getId())) {
                                            ++count;
                                        }
                                    }

                                    if (count > 0) {
                                        model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_PARALLELGATEWAY_HANDLE);
                                    } else {
                                        model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE);
                                    }
                                }

                                activityId = targetActivity.getId();
                                break;
                            }

                            if (targetActivity.getActivityBehavior() instanceof ExclusiveGatewayActivityBehavior) {
                                model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE);
                                activityId = targetActivity.getId();
                                break;
                            }

                            if (targetActivity.getActivityBehavior() instanceof ClassDelegate) {
                                model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_JAVA_SERVICE);
                                activityId = targetActivity.getId();
                                model.setNextStepId(activityId);
                                break;
                            }

                            if (targetActivity.getActivityBehavior() instanceof ReceiveTaskActivityBehavior) {
                                model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_WAIT);
                                activityId = targetActivity.getId();
                                owner = (String)targetActivity.getProperty("name");
                                model.setNextStepId(activityId);
                                model.setNextStepName(owner);
                            }
                        }
                    }
                }

                if (activityId != null) {
                    targetActivity = pde.findActivity(activityId);
                    if (targetActivity.getActivityBehavior() instanceof NoneEndEventActivityBehavior) {
                        activityId = targetActivity.getId();
                        model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES);
                    } else if (targetActivity.getActivityBehavior() instanceof UserTaskActivityBehavior) {
                        activityId = targetActivity.getId();
                        model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE);
                    } else if (targetActivity.getActivityBehavior() instanceof ClassDelegate) {
                        model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_JAVA_SERVICE);
                    } else if (targetActivity.getActivityBehavior() instanceof ReceiveTaskActivityBehavior) {
                        activityId = targetActivity.getId();
                        model.setNextStepId(activityId);
                        model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_WAIT);
                    }

                    ProcessStepMap nextStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, activityId);
                    if (nextStepMap != null && nextStepMap.getStepMax() != null) {
                        model.setMaxUser(nextStepMap.getStepMax());
                    } else {
                        model.setMaxUser(new Long(-1L));
                    }

                    if (!model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE) && !model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_PARALLELGATEWAY_HANDLE)) {
                        if (model.getSendPageCode() != null && model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_WAIT)) {
                            pde.findActivity(activityId);
                            model.setNextStepId(activityId);
                        } else if (model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES)) {
                            if (model.isCC() != null && model.isCC().equals(SysConst.on) && defMap != null && defMap.getNoticeType() != null && !defMap.getNoticeType().equals(SysConst.off)) {
                                ccUsers = "";
                                if (defMap.getNoticeType().equals(new Long(1L))) {
                                    List<HistoricTaskInstance> hislist = ((HistoricTaskInstanceQuery)this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc()).list();
                                    if (hislist != null && hislist.size() > 0) {
                                        HistoricTaskInstance instance = (HistoricTaskInstance)hislist.get(0);
                                        owner = instance.getOwner();
                                        if (owner != null) {
                                            ccUsers = UserContextUtil.getInstance().getFullUserAddress(owner);
                                        }
                                    }
                                } else if (defMap.getNoticeType().equals(new Long(2L))) {
                                    ccUsers = defMap.getArchiveNotice();
                                }

                                if (model.getCcUser() != null && !"".equals(model.getCcUser())) {
                                    temp = model.getCcUser();
                                    if (ccUsers != null) {
                                        model.setCcUser(temp + "," + ccUsers);
                                    }
                                } else {
                                    model.setCcUser(ccUsers);
                                }
                            }

                            model.setNextStepId(activityId);
                            model.setNextStepName("归档");
                        }
                    } else {
                        ActivityImpl activityImpl = pde.findActivity(activityId);
                        if (activityImpl != null) {
                            this.buildActivityRouteModel(model, activityImpl, prcDefId, actDefId, task);
                        }

                        if (model.getAddressHTML() == null || model.getAddressHTML().equals("")) {
                            model.setAddressHTML("<div style=\"text-align:center\"><img border='0' src='iwork_img/warning.jpg'>未定义路由策略，请联系系统管理员（错误号：ERRPR-0018）</div>");
                        }

                        if (stepMap != null && stepMap.getIsCc() != null && stepMap.getIsCc().equals(SysConst.on)) {
                            model.setCC(SysConst.on);
                            temp = pvmDefExecuteEngine.getCCUserList(actDefId, prcDefId, actStepId, taskId);
                            if (model.getCcUser() != null && !"".equals(model.getCcUser())) {
                                 temp = model.getCcUser();
                                if (temp != null) {
                                    model.setCcUser(temp + "," + temp);
                                }
                            } else {
                                model.setCcUser(temp);
                            }
                        }
                    }
                } else if (model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_WAIT)) {
                    return model;
                }

                return model;
            }
        } else {
            return null;
        }
    }

    public RouteModel buildActivityRouteModel(RouteModel model, ActivityImpl activityImpl, Long prcDefId, String actDefId, Task task) {
        PvmRouteEngine pvmRouteEngine = PvmRouteEngine.getInstance();
        PvmDefExecuteEngine pvmDefExecuteEngine = PvmDefExecuteEngine.getInstance();
        String activityId = activityImpl.getId();
        String taskId = task.getId();
        ProcessStepMap targetStepMap;
        ProcessStepMap psm;
        String purview;
        String ccUserStr;
        int flag;
        String temp;
        if (activityImpl.getActivityBehavior() instanceof UserTaskActivityBehavior) {
            targetStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, activityId);
            if (targetStepMap == null) {
                psm = new ProcessStepMap();
                psm.setActDefId(actDefId);
                psm.setActStepId(activityId);
                this.processStepMapService.initializeMapModel(psm);
                model.setNextStepName(activityImpl.getProperty("name").toString());
            } else {
                model.setNextStepName(targetStepMap.getStepTitle());
            }

            if (model.getFixedNextUser() != null && model.getFixedNextUser().size() > 0) {
                HashMap hash = model.getFixedNextUser();
                temp = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
                model.setAddressHTML(SysRouteUtil.getAddressHTML(hash, SysRouteUtil.ROUTE_TYPE_OPTION, temp));
            } else {
                ccUserStr = pvmRouteEngine.getHandlePage(actDefId, prcDefId, activityId, task);
                if (ccUserStr != null) {
                    model.setAddressHTML(ccUserStr);
                }

                if (this.processStepRouteService == null) {
                    this.processStepRouteService = (ProcessStepRouteService)SpringBeanUtil.getBean("processStepRouteService");
                }

                List<ProcessStepRoute> list = this.processStepRouteService.getRouteList(actDefId, prcDefId, activityId);
                UserContext context = UserContextUtil.getInstance().getCurrentUserContext();
                new Long(1L);
                new Long(0L);
                new HashMap();
                Iterator var18 = list.iterator();

                label181:
                while(true) {
                    List orgList;
                    do {
                        do {
                            ProcessStepRoute StepRouteModel;
                            boolean flag1;
                            do {
                                if (!var18.hasNext()) {
                                    break label181;
                                }

                                StepRouteModel = (ProcessStepRoute)var18.next();
                                flag1 = true;
                                if (model != null) {
                                    purview = StepRouteModel.getRoutePurview();
                                    if (purview != null && !purview.equals("")) {
                                        flag1 = PurviewCommonUtil.getInstance().checkUserInPurview(context.get_userModel().getUserid(), purview);
                                    } else {
                                        flag1 = true;
                                    }
                                }
                            } while(!flag1);

                            SysRouteSchemaAbst route = (SysRouteSchemaAbst)SysRouteFactory.getRouteInstance(context, StepRouteModel, task);
                            orgList = route.getRouteUser();
                        } while(orgList == null);
                    } while(orgList.size() <= 0);

                    Iterator var23 = orgList.iterator();

                    while(true) {
                        String nextUsers;
                        do {
                            if (!var23.hasNext()) {
                                continue label181;
                            }

                            OrgUser orgUser = (OrgUser)var23.next();
                            nextUsers = orgUser.getUserid();
                        } while(nextUsers == null);

                        String[] nextUser = nextUsers.split(",");
                        HashMap fixedNextUser = new HashMap();
                        String[] var29 = nextUser;
                        int var28 = nextUser.length;

                        for(int var27 = 0; var27 < var28; ++var27) {
                            String str = var29[var27];
                            boolean isaddress = UserContextUtil.getInstance().checkAddress(str.trim());
                            if (isaddress) {
                                String userid = UserContextUtil.getInstance().getUserId(str);
                                UserContext targetuser = UserContextUtil.getInstance().getUserContext(userid);
                                fixedNextUser.put(userid, targetuser.get_userModel().getUserid());
                            }
                        }

                        model.setFixedNextUser(fixedNextUser);
                    }
                }
            }

            flag = pvmRouteEngine.getHandleType(actDefId, activityId, taskId);
            model.setHandleType(flag);
            model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE);
        } else {
            List outgoingTrans;
            int num;

            if (activityImpl.getActivityBehavior() instanceof ParallelGatewayActivityBehavior) {
                outgoingTrans = activityImpl.getOutgoingTransitions();
                new StringBuffer();
                StringBuffer addressHTMLPage = new StringBuffer();
                num = 0;
                Iterator var15 = outgoingTrans.iterator();

                label138:
                while(true) {
                    while(true) {
                        if (!var15.hasNext()) {
                            break label138;
                        }

                        PvmTransition trans = (PvmTransition)var15.next();
                        ActivityImpl gatewayActivity = (ActivityImpl)trans.getDestination();
                        if (gatewayActivity.getActivityBehavior() instanceof UserTaskActivityBehavior) {
                            targetStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, gatewayActivity.getId());
                            String stepName = "";
                            if (targetStepMap == null) {
                                 psm = new ProcessStepMap();
                                psm.setActDefId(actDefId);
                                psm.setActStepId(gatewayActivity.getId());
                                this.processStepMapService.initializeMapModel(psm);
                                stepName = gatewayActivity.getProperty("name").toString();
                                addressHTMLPage.append("<tr><td class=\"StepTitle\" colspan=\"2\"><img src='iwork_img/domain.gif' border='0'>").append(gatewayActivity.getProperty("name").toString()).append("</td></tr>");
                            } else {
                                stepName = targetStepMap.getStepTitle();
                                addressHTMLPage.append("<tr><td class=\"StepTitle\" colspan=\"2\"><img src='iwork_img/domain.gif' border='0'>").append(targetStepMap.getStepTitle()).append("</td></tr>");
                            }

                            List<OrgUser> userList = pvmRouteEngine.getHandleUser(actDefId, prcDefId, gatewayActivity.getId(), task);
                            addressHTMLPage.append("<tr>").append("\n");
                            addressHTMLPage.append("<td class=\"ItemTitle\"><input type=\"hidden\" name=\"gatewayStepName").append(gatewayActivity.getId()).append("\" value=\"").append(stepName).append("\"></td>").append("\n");
                            addressHTMLPage.append("<td   class=\"pageInfo\">").append("\n");
                            addressHTMLPage.append("<div id=\"div\">").append("\n");
                            addressHTMLPage.append("<ul>").append("\n");
                            if (userList != null && userList.size() > 0) {
                                purview = "";
                                purview = "checked";

                                for(Iterator var57 = userList.iterator(); var57.hasNext(); ++num) {
                                    OrgUser userModel = (OrgUser)var57.next();
                                    addressHTMLPage.append("<li><input type=\"checkbox\" ").append(purview).append(" name=\"receiveUser\" value=\"").append(gatewayActivity.getId()).append("|").append(userModel.getUserid()).append("\" id=\"receiveUser-").append(num).append("\"/>").append("<label for=\"receiveUser-").append(num).append("\" class=\"checkboxLabel\">").append(userModel.getUsername()).append("</label></li>").append("\n");
                                }
                            } else {
                                addressHTMLPage.append("<li><table><tr><td><input type=\"text\" class=\"tempInput\" id=\"tmp_").append(num).append("\" onchange=\"$('#receiveUser-").append(num).append("').val($('#targetGatewayId").append(num).append("').val()+'|'+this.value);\" value=\"\"></td><td><a href=\"###\" title=\"单选地址薄\" onclick=\"showAddress('tmp_").append(num).append("');\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-radiobook\"></a></td></tr></table><input type=\"hidden\" value=\"\" name=\"receiveUser\" id=\"receiveUser-").append(num).append("\"><input type=\"hidden\" value=\"").append(gatewayActivity.getId()).append("\" name=\"targetGatewayId").append(num).append("\" id=\"targetGatewayId").append(num).append("\"></li>").append("\n");
                                ++num;
                            }

                            addressHTMLPage.append("</ul>").append("\n");
                            addressHTMLPage.append("</td>").append("\n");
                            addressHTMLPage.append("</tr>").append("\n");
                            model.setNextStepName("并行办理");
                            model.setAddressHTML(addressHTMLPage.toString());
                             flag = pvmRouteEngine.getHandleType(actDefId, activityId, taskId);
                            model.setHandleType(flag);
                        } else {
                            this.buildActivityRouteModel(model, activityImpl, prcDefId, actDefId, task);
                        }
                    }
                }
            } else if (activityImpl.getActivityBehavior() instanceof NoneEndEventActivityBehavior) {
                targetStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, activityId);
                if (targetStepMap == null) {
                    psm = new ProcessStepMap();
                    psm.setActDefId(actDefId);
                    psm.setActStepId(activityId);
                    targetStepMap = this.processStepMapService.initializeMapModel(psm);
                }

                if (targetStepMap.getIsCc().equals(SysConst.on)) {
                    ccUserStr = pvmDefExecuteEngine.getCCUserList(actDefId, prcDefId, activityId, taskId);
                    if (model.getCcUser() != null && !"".equals(model.getCcUser())) {
                        temp = model.getCcUser();
                        model.setCcUser(temp + "," + ccUserStr);
                    } else {
                        model.setCcUser(ccUserStr);
                    }
                }

                model.setMaxUser(targetStepMap.getStepMax());
                flag = pvmRouteEngine.getHandleType(actDefId, activityId, taskId);
                model.setHandleType(flag);
                model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES);
            } else if (activityImpl.getActivityBehavior() instanceof ExclusiveGatewayActivityBehavior) {
                outgoingTrans = activityImpl.getOutgoingTransitions();
                StringBuffer html = new StringBuffer();
                num = 1;
                Iterator var42 = outgoingTrans.iterator();

                while(true) {
                    while(var42.hasNext()) {
                        PvmTransition outTrans = (PvmTransition)var42.next();
                        ActivityImpl activity_step = (ActivityImpl)outTrans.getDestination();
                        String stepName = "";
                        targetStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, activity_step.getId());
                        if (targetStepMap == null) {
                            stepName = activity_step.getProperty("name").toString();
                        } else {
                            stepName = targetStepMap.getStepTitle();
                        }

                        if (activity_step.getActivityBehavior() instanceof UserTaskActivityBehavior) {
                            html.append("<tr >").append("\n");
                            html.append("<td colspan=\"2\" onclick='$(\"#parallelGateway").append(outTrans.getId()).append("").append("").append("\").attr(\"checked\",true);' style=\"border-bottom:1px solid #efefef;background-color:#EEF5FF;font-faimly:黑体;font-size:14px;padding:5px;font-weight:800;\">").append("<label><input type=radio  id='parallelGateway").append(outTrans.getId()).append("' name='parallelGateway' value='").append(outTrans.getId()).append("' >").append(stepName).append("</label>").append("</td>").append("\n");
                            html.append("</tr>").append("\n");
                            List<OrgUser> userList = pvmRouteEngine.getHandleUser(actDefId, prcDefId, activity_step.getId(), task);
                            html.append("<tr>").append("\n");
                            html.append("<td class=\"ItemTitle\"><input type=\"hidden\" name=\"gatewayStepName").append(activity_step.getId()).append("\" value=\"").append(stepName).append("\"></td>").append("\n");
                            html.append("<td  class=\"pageInfo\" onclick='$(\"#parallelGateway").append(outTrans.getId()).append("").append("").append("\").attr(\"checked\",true);'>").append("\n");
                            html.append("<div id=\"div\" >").append("\n");
                            html.append("<ul>").append("\n");
                            if (userList != null && userList.size() > 0) {
                                for(Iterator var56 = userList.iterator(); var56.hasNext(); ++num) {
                                    OrgUser userModel = (OrgUser)var56.next();
                                    html.append("<li onclick='$(\"#parallelGateway").append(outTrans.getId()).append("").append("").append("\").attr(\"checked\",true);'><input type=\"checkbox\"  name=\"receiveUser\" value=\"").append(activity_step.getId()).append("|").append(userModel.getUserid()).append("\" id=\"receiveUser-").append(num).append("\"/>").append("<label for=\"receiveUser-").append(num).append("\" class=\"checkboxLabel\">").append(userModel.getUsername()).append("</label></li>").append("\n");
                                }
                            } else {
                                html.append("<li>");
                                html.append("<table style=\"width:200px\">");
                                html.append("<tr><td>");
                                html.append("<input type=\"text\" name=\"tempAddress").append(outTrans.getId()).append("\" id=\"tempAddress").append(outTrans.getId()).append("\" onclick='$(\"#parallelGateway").append(outTrans.getId()).append("").append("").append("\").attr(\"checked\",true);' onchange=\"$('#receiveUser').val('").append(activity_step.getId()).append("'+'|'+this.value);\" value=\"\">").append("\n");
                                html.append("</td>");
                                html.append("<td><a href=\"###\" title=\"单选地址薄\" onclick=\"showAddress('tempAddress").append(outTrans.getId()).append("');\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-radiobook\"></a>").append("</td>");
                                html.append("</tr></table>");
                                html.append("</li>");
                            }

                            html.append("</ul>").append("\n");
                            html.append("</td>").append("\n");
                            html.append("</tr>").append("\n");
                        } else {
                            this.buildActivityRouteModel(model, activity_step, prcDefId, actDefId, task);
                        }
                    }

                    html.append("<input type=\"hidden\" value=\"\" name=\"receiveUser\" id=\"receiveUser\"><input type=\"hidden\"  name=\"targetGatewayId").append(num).append("\" id=\"targetGatewayId").append(num).append("\">");
                    model.setAddressHTML(html.toString());
                    model.setNextStepName("单一分支");
                    num = pvmRouteEngine.getHandleType(actDefId, activityId, taskId);
                    model.setHandleType(num);
                    model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE);
                    break;
                }
            }
        }

        model.setNextStepId(activityImpl.getId());
        return model;
    }

    public SendPageModel executeBack(String actDefId, Long prcDefId, String actStepId, String taskId, Long backType) {
        if (backType == null) {
            backType = new Long(1L);
        }

        SendPageModel model = new SendPageModel();
        this.setTaskService(this.processEngine.getTaskService());
        Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
        model.setOpinion(this.getOpinionContent(actDefId, prcDefId, taskId));
        model.setTitle(task.getDescription());
        String title = this.getProcessTaskTitle(actDefId, prcDefId, actStepId, taskId);
        model.setTitle(title);
        ActivityImpl backNode = null;
        HistoricActivityInstance instance = PvmDefExecuteEngine.getInstance().getstartActivityId(taskId);
        if (instance.getActivityId().equals(actStepId)) {
            model.setMsgInfo("ERROR-10011");
            return model;
        } else {
            if (!backType.equals(new Long(0L)) && !backType.equals(new Long(2L))) {
                ExecutionEntity exe = (ExecutionEntity)this.runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
                RepositoryService rps = this.processEngine.getRepositoryService();
                ProcessDefinitionEntity pde = (ProcessDefinitionEntity)((RepositoryServiceImpl)rps).getDeployedProcessDefinition(task.getProcessDefinitionId());
                ActivityImpl ai = pde.findActivity(exe.getActivityId());
                HistoricActivityInstance backActivityInstance = PvmDefExecuteEngine.getInstance().getBackActivityId(taskId, actStepId);
                String userId;
                if (backActivityInstance != null) {
                    backNode = pde.findActivity(backActivityInstance.getActivityId());
                    if (backNode != null) {
                        if (!(backNode.getActivityBehavior() instanceof UserTaskActivityBehavior)) {
                            if (backNode.getActivityBehavior() instanceof NoneStartEventActivityBehavior) {
                                model.setMsgInfo("ERROR-10011");
                            } else {
                                model.setMsgInfo("ERROR-10012");
                            }
                        } else {
                            String nodename = "";
                            ProcessStepMap targetStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, backNode.getId());
                            if (targetStepMap != null && targetStepMap.getStepTitle() != null) {
                                nodename = targetStepMap.getStepTitle();
                            } else {
                                nodename = (String)backNode.getProperty("name");
                            }

                            userId = backActivityInstance.getAssignee();
                            userId = UserContextUtil.getInstance().filterDisabledUser(userId);
                            if (userId != null) {
                                model.setTargetStepName(nodename);
                                model.setTargetStepId(backNode.getId());
                                UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
                                List<OrgUser> receiveUser = new ArrayList();
                                receiveUser.add(uc.get_userModel());
                                model.setReceiveUser(receiveUser);
                                model.setMsgInfo("success");
                            }
                        }
                    }
                }

                if (model.getMsgInfo() == null) {
                    List<PvmTransition> sourceTransition = ai.getIncomingTransitions();
                    if (sourceTransition.size() > 0) {
                        TransitionImpl transitionImpl = (TransitionImpl)sourceTransition.get(0);
                        backNode = transitionImpl.getSource();
                        model = new SendPageModel();
                        userId = (String)backNode.getProperty("name");
                        model.setTargetStepName(userId);
                        model.setTargetStepId(backNode.getId());
                        List<OrgUser> hanleUser = PvmRouteEngine.getInstance().getHandleUser(actDefId, prcDefId, backNode.getId(), task);
                        model.setReceiveUser(hanleUser);
                    }
                }

                if (backNode != null) {
                    if (backNode.getActivityBehavior() instanceof UserTaskActivityBehavior) {
                        ProcessStepMap curentStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepId);
                        if (curentStepMap != null && curentStepMap.getIsCc() != null && curentStepMap.getIsCc().equals(SysConst.on)) {
                            model.setIsCC(SysConst.on);
                        }

                        model.setIsOpinion(SysConst.on);
                        if (actStepId.equals("999999")) {
                            ProcessDefActionset act = this.processDefActionService.getProcessDefActionDao().getModel(prcDefId, actDefId, "addSign");
                            if (act != null) {
                                model.setIsOpinion(act.getIsOpinion());
                            }
                        }

                        model.setMsgInfo("success");
                    } else if (backNode.getActivityBehavior() instanceof NoneStartEventActivityBehavior) {
                        model.setMsgInfo("ERROR-10011");
                    } else {
                        model.setMsgInfo("ERROR-10012");
                    }
                }
            } else {
                model.setTargetStepId(instance.getActivityId());
                model.setTargetStepName(instance.getActivityName());
                String userId = instance.getAssignee();
                UserContext targetuser = UserContextUtil.getInstance().getUserContext(userId);
                if (targetuser != null) {
                    String addressHTMLPage = SysRouteUtil.getAddressHTML(targetuser);
                    model.setAddressHTML(addressHTMLPage);
                    List<OrgUser> receiveUser = new ArrayList();
                    receiveUser.add(targetuser.get_userModel());
                    model.setReceiveUser(receiveUser);
                    ProcessStepMap curentStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepId);
                    if (curentStepMap != null && curentStepMap.getIsCc() != null && curentStepMap.getIsCc().equals(SysConst.on)) {
                        model.setIsCC(SysConst.on);
                    }

                    model.setIsOpinion(SysConst.on);
                    model.setMsgInfo("success");
                    if (backType.equals(new Long(2L))) {
                        model.setSendPageType(backType);
                    }
                }
            }

            return model;
        }
    }

    public String getBackOtherList(String actDefId, Long prcDefId, String actStepId, String taskId, Long backType) {
        StringBuffer html = new StringBuffer();
        Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
        List<HistoricTaskInstance> list = null;
        List<HistoricTaskInstance> listtwo = new ArrayList();
        if (task != null) {
            RepositoryService rps = this.processEngine.getRepositoryService();
            ProcessDefinitionEntity pde = (ProcessDefinitionEntity)((RepositoryServiceImpl)rps).getDeployedProcessDefinition(actDefId);
            List<ActivityImpl> step_list = pde.getActivities();
            ActivityImpl current_activity = pde.findActivity(actStepId);
            ProcessDefinitionStepUtil pdsutil = ProcessDefinitionStepUtil.getInstance();
            Long level = pdsutil.getProcessLevel(current_activity);
            List<ActivityImpl> checklist = pdsutil.getFixedLevelStepList(actDefId, step_list, level);
            html.append("<select onchange=\"setParam(this)\">").append("\n");
            HashMap data = new HashMap();
            list = this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().processDefinitionId(actDefId).processInstanceId(task.getProcessInstanceId()).list();
            List<HashMap> lists=new ArrayList();
            List lable=new ArrayList();
            lable.add("assignee_");
            StringBuffer sql=new StringBuffer();
            sql.append("  select * from( select r.assignee_ from process_hi_taskinst r where r.proc_inst_id_="+task.getProcessInstanceId()+" order by r.id_ ) where rownum<( ");
            sql.append(" select rn from（select t.*,rownum rn from( ");
            sql.append(" select s.assignee_ from process_hi_taskinst s where s.proc_inst_id_="+task.getProcessInstanceId()+" order by s.id_ ) t） where assignee_='"+task.getAssignee()+"' and rownum=1) ");
            lists= DBUTilNew.getDataList(lable,sql.toString(),null);
            if(lists!=null && lists.size()>0){
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getName().equals("任务撤销")) continue;
                    for (int j=0;j<lists.size();j++){
                        if(list.get(i).getAssignee().equals(lists.get(j).get("assignee_")))
                            listtwo.add(list.get(i));
                    }
                }
            }
            html.append("<option value=\"\">--选择您要驳回的节点--</option>\n");
            Iterator var18 = listtwo.iterator();
            while(true) {
                HistoricTaskInstance historic;
                UserContext uc;
                String taskkey;
                do {
                    Object obj;
                    do {
                        do {
                            do {
                                if (!var18.hasNext()) {
                                    html.append("</select>").append("\n");
                                    return html.toString();
                                }

                                historic = (HistoricTaskInstance)var18.next();
                                String assignee = historic.getAssignee();
                                uc = UserContextUtil.getInstance().getUserContext(assignee);
                            } while(uc == null);

                            taskkey = historic.getTaskDefinitionKey();
                            obj = data.get(taskkey);
                        } while(obj != null);
                    } while(taskkey.equals(actStepId));
                } while(taskkey.equals("999999"));

                boolean isLevel = false;
                Iterator var25 = checklist.iterator();

                while(var25.hasNext()) {
                    ActivityImpl activity = (ActivityImpl)var25.next();
                    if (activity.getId().equals(historic.getTaskDefinitionKey())) {
                        isLevel = true;
                        break;
                    }
                }

                if (isLevel && historic.getName() != null && !historic.getName().equals("驳回")) {
                    String value = historic.getTaskDefinitionKey() + "|" + uc.get_userModel().getUsername() + "|" + uc.get_userModel().getUserid();
                    html.append("<option value=\"").append(value).append("\">").append(historic.getName()).append("</option>\n");
                    data.put(historic.getTaskDefinitionKey(), historic);
                }
            }
        } else {
            return html.toString();
        }
    }

    public RouteModel executeManualJump(String actDefId, Long prcDefId, String actStepId, String taskId, String targetStepDefId, String mjId, HashMap params) {
        RouteModel model = new RouteModel();
        PvmStepTriggerEngine pvmStepTriggerEngine = PvmStepTriggerEngine.getInstance();
        PvmDefExecuteEngine pvmDefExecuteEngine = PvmDefExecuteEngine.getInstance();
        RepositoryService rps = this.processEngine.getRepositoryService();
        Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return null;
        } else {
            ProcessDefMap defMap = this.processDefMapService.getProcessDefMapModel(prcDefId, actDefId);
            if (defMap != null && defMap.getDefTitle() != null && defMap.getDefTitle().indexOf("%") > -1) {
                model.setTitle(this.reBuildTitle(task, defMap.getDefTitle()));
            } else if (task != null) {
                model.setTitle(task.getDescription());
            }

            ProcessStepMap stepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepId);
            if (stepMap == null) {
                stepMap = new ProcessStepMap();
                stepMap = this.processStepMapService.initializeMapModel(stepMap);
            }

            if (task != null && stepMap.getStepType() != null && stepMap.getStepType().equals(new Long(2L))) {
                List list = this.processRuntimeSignsService.getProcessRuntimeSignsDAO().getFinishSignsList(actDefId, actStepId, Long.parseLong(task.getProcessInstanceId()), Long.parseLong(task.getExecutionId()), Long.parseLong(taskId));
                if (list == null || list.size() == 0) {
                    model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_HANDLE);
                    model.setAddressHTML("<div style=\"text-align:center\"><img border='0' src='iwork_img/warning.jpg'>当前节点为会签节点，请点击【发送会签】按钮，完成会签后执行顺序办理</div>");
                    return model;
                }
            }

            PvmRouteEngine pvmRouteEngine = PvmRouteEngine.getInstance();
            ProcessDefinitionEntity pde = (ProcessDefinitionEntity)((RepositoryServiceImpl)rps).getDeployedProcessDefinition(actDefId);
            ActivityImpl activityImpl;
            if (targetStepDefId == null) {
                activityImpl = pde.findActivity(actStepId);
                List<PvmTransition> out_trans = activityImpl.getOutgoingTransitions();
                Iterator var20 = out_trans.iterator();

                while(var20.hasNext()) {
                    PvmTransition pt = (PvmTransition)var20.next();
                    ActivityImpl targetActivity = (ActivityImpl)pt.getDestination();
                    if (targetActivity != null) {
                        if (targetActivity.getActivityBehavior() instanceof NoneEndEventActivityBehavior) {
                            targetStepDefId = targetActivity.getId();
                            model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES);
                            break;
                        }

                        if (targetActivity.getActivityBehavior() instanceof UserTaskActivityBehavior) {
                            targetStepDefId = targetActivity.getId();
                            PvmDefExecuteEngine.getInstance().getBackActivityId(taskId, targetStepDefId);
                            break;
                        }
                    }
                }
            }

            String addressHTMLPage;
            if (targetStepDefId != null && model.getSendPageCode() != SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES) {
                ProcessStepMap targetStepMap;
                String ccUserStr;
                String temp;
                if (targetStepDefId.equals("MJ_END")) {
                    List<ActivityImpl> list = pde.getActivities();
                    Iterator var38 = list.iterator();

                    while(var38.hasNext()) {
                        ActivityImpl ai = (ActivityImpl)var38.next();
                        if (ai.getActivityBehavior() instanceof NoneEndEventActivityBehavior) {
                            model.setNextStepId(ai.getId());
                            model.setSendPageCode(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES);
                            break;
                        }
                    }
                } else if (targetStepDefId.equals("MJ_BACK_SENDER")) {
                    SendPageModel spm = this.executeBack(actDefId, prcDefId, actStepId, taskId, (Long)null);
                    model.setNextStepId(spm.getTargetStepId());
                    model.setTitle(spm.getTitle());
                    model.setNextStepName(spm.getTargetStepName());
                    addressHTMLPage = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
                    ccUserStr = SysRouteUtil.getAddressHTML(spm.getReceiveUser(), new Long(1L), addressHTMLPage);
                    if (ccUserStr != null) {
                        model.setAddressHTML(ccUserStr);
                    }
                } else if (targetStepDefId.equals("MJ_BACK_OWNER")) {
                    HistoricActivityInstance instance = PvmDefExecuteEngine.getInstance().getstartActivityId(taskId);
                    model.setNextStepId(instance.getActivityId());
                    model.setNextStepName(instance.getActivityName());
                    addressHTMLPage = instance.getAssignee();
                    UserContext targetuser = UserContextUtil.getInstance().getUserContext(addressHTMLPage);
                    temp = SysRouteUtil.getAddressHTML(targetuser);
                    model.setAddressHTML(temp);
                } else {
                    if (targetStepDefId.equals("MJ_TRANS")) {
                        return this.executeHandle(actDefId, prcDefId, actStepId, taskId, params);
                    }

                    activityImpl = pde.findActivity(targetStepDefId);
                    if (activityImpl != null) {
                        model.setNextStepId(activityImpl.getId());
                        if (activityImpl.getActivityBehavior() instanceof UserTaskActivityBehavior) {
                            addressHTMLPage = pvmRouteEngine.getHandlePage(actDefId, prcDefId, targetStepDefId, task);
                            if (addressHTMLPage != null) {
                                model.setAddressHTML(addressHTMLPage);
                            }

                            int flag = pvmRouteEngine.getHandleType(actDefId, targetStepDefId, taskId);
                            model.setHandleType(flag);
                        } else if (activityImpl.getActivityBehavior() instanceof NoneEndEventActivityBehavior) {
                            int flag = pvmRouteEngine.getHandleType(actDefId, targetStepDefId, taskId);
                            model.setHandleType(flag);
                        }

                        targetStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, targetStepDefId);
                        if (targetStepMap == null) {
                            ProcessStepMap psm = new ProcessStepMap();
                            psm.setActDefId(actDefId);
                            psm.setActStepId(actStepId);
                            targetStepMap = this.processStepMapService.initializeMapModel(psm);
                        }

                        if (targetStepMap.getIsCc() != null && targetStepMap.getIsCc().equals(SysConst.on)) {
                            ccUserStr = pvmDefExecuteEngine.getCCUserList(actDefId, prcDefId, actStepId, taskId);
                            if (model.getCcUser() != null && !"".equals(model.getCcUser())) {
                                temp = model.getCcUser();
                                model.setCcUser(temp + "," + ccUserStr);
                            } else {
                                model.setCcUser(ccUserStr);
                            }
                        }

                        model.setMaxUser(targetStepMap.getStepMax());
                    }
                }

                 targetStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, model.getNextStepId());
                if (targetStepMap == null) {
                    targetStepMap = new ProcessStepMap();
                    targetStepMap.setActDefId(actDefId);
                    targetStepMap.setActStepId(actStepId);
                    targetStepMap = this.processStepMapService.initializeMapModel(targetStepMap);
                     activityImpl = pde.findActivity(model.getNextStepId());
                    model.setNextStepName(activityImpl.getProperty("name").toString());
                } else {
                    model.setNextStepName(targetStepMap.getStepTitle());
                }

                model.setMaxUser(targetStepMap.getStepMax());
                targetStepMap = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepId);
                if (targetStepMap.getIsCc() != null && targetStepMap.getIsCc().equals(SysConst.on)) {
                    model.setCC(SysConst.on);
                    ccUserStr = pvmDefExecuteEngine.getCCUserList(actDefId, prcDefId, actStepId, taskId);
                    if (model.getCcUser() != null && !"".equals(model.getCcUser())) {
                        temp = model.getCcUser();
                        model.setCcUser(temp + "," + ccUserStr);
                    } else {
                        model.setCcUser(ccUserStr);
                    }
                }

                if (targetStepMap.getIsAddOpinion() != null && targetStepMap.getIsAddOpinion().equals(SysConst.on)) {
                    model.setIsOpinion(SysConst.on);
                } else {
                    model.setIsOpinion(SysConst.off);
                }

                if (mjId != null) {
                    List<ProcessStepManualJump> jumplist = this.processStepManualJumpService.getProcessStepManualJumpList(actDefId, prcDefId, actStepId);
                    Iterator var40 = jumplist.iterator();

                    while(var40.hasNext()) {
                        ProcessStepManualJump psmj = (ProcessStepManualJump)var40.next();
                        if (psmj.getId().equals(Long.parseLong(mjId))) {
                            model.setAction(psmj.getMjName());
                            if (psmj.getIsRemind() != null && psmj.getIsRemind().equals(SysConst.on)) {
                                if (model.getCcUser() != null && !"".equals(model.getCcUser())) {
                                     temp = model.getCcUser();
                                    model.setCcUser(temp + "," + psmj.getMsgUsers());
                                } else {
                                    model.setCcUser(psmj.getMsgUsers());
                                }
                            }
                            break;
                        }
                    }
                }
            }

            if (model.getSendPageCode() != null && model.getSendPageCode().equals(SendPageTypeConstant.SP_TYPE_TEXT_ARCHIVES) && model.isCC() != null && model.isCC().equals(SysConst.on) && defMap != null && defMap.getNoticeType() != null && !defMap.getNoticeType().equals(SysConst.off)) {
                String ccUsers = "";
                if (defMap.getNoticeType().equals(new Long(1L))) {
                    if (task.getOwner() != null) {
                        ccUsers = UserContextUtil.getInstance().getFullUserAddress(task.getOwner());
                    }
                } else if (defMap.getNoticeType().equals(new Long(2L))) {
                    ccUsers = defMap.getArchiveNotice();
                }

                if (model.getCcUser() != null && !"".equals(model.getCcUser())) {
                    addressHTMLPage = model.getCcUser();
                    if (ccUsers != null) {
                        model.setCcUser(addressHTMLPage + "," + ccUsers);
                    }
                } else {
                    model.setCcUser(ccUsers);
                }
            }

            return model;
        }
    }

    public String getCCReciveUser(String actDefId, String actStepId, String taskId, Long instanceId, Long excutionId) {
        StringBuffer reciveUsers = new StringBuffer();
        List<ProcessRuCc> list = this.processRuntimeCcDAO.getCCModelList(actDefId, actStepId, taskId, instanceId, excutionId);
        int num = 0;
        Iterator var10 = list.iterator();

        while(var10.hasNext()) {
            ProcessRuCc model = (ProcessRuCc)var10.next();
            String targetUser = model.getTargetUser();
            if (targetUser != null) {
                ++num;
                targetUser = UserContextUtil.getInstance().getFullUserAddress(targetUser);
                if (targetUser != null) {
                    if (num > 1) {
                        reciveUsers.append(",");
                    }

                    reciveUsers.append(targetUser);
                }
            }
        }

        return reciveUsers.toString();
    }

    public String getProcessTaskTitle(String actDefId, Long prcDefId, String actStepId, String taskId) {
        String title = "";
        Map map = this.taskService.getVariables(taskId);
        if (map != null && map.get("PROCESS_TASK_TITLE") != null) {
            title = map.get("PROCESS_TASK_TITLE").toString();
        } else {
            Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task != null) {
                title = task.getDescription();
            } else {
                HistoricTaskInstance hti = (HistoricTaskInstance)this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
                if (hti != null) {
                    title = hti.getDescription();
                }
            }
        }

        return title;
    }

    public HashMap getTaskRemindCheckBox(String actDefId, Long prcDefId, String actStepId) {
        HashMap remindTypeList = new HashMap();
        ProcessStepMap map = this.processStepMapService.getProcessDefMapModel(prcDefId, actDefId, actStepId);
        if (map != null) {
            if ("on".equals(SystemConfig._imConf.getServer()) && map.getSendIm() != null && map.getSendIm().equals(new Long(1L))) {
                remindTypeList.put("_im", "即时通讯");
            }

            if ("on".equals(SystemConfig._mailServerConf.getSmtp_services()) && map.getSendEmail() != null && map.getSendEmail().equals(new Long(1L))) {
                remindTypeList.put("_email", "邮件提醒");
            }

            if ("on".equals(SystemConfig._smsConf.getServer()) && map.getSendSms() != null && map.getSendSms().equals(new Long(1L))) {
                remindTypeList.put("_sms", "短信提醒");
            }

            if (map.getSendSysmsg() != null && map.getSendSysmsg().equals(new Long(1L))) {
                remindTypeList.put("_sysmsg", "系统消息");
            }

            if (SystemConfig._weixinConf != null && "on".equals(SystemConfig._weixinConf.getServer()) && map.getSendWeixin() != null && map.getSendWeixin().equals(new Long(1L))) {
                remindTypeList.put("_weixin", "微信提醒");
            }
        }

        return remindTypeList.size() > 0 ? remindTypeList : null;
    }

    public String getOpinionContent(String actDefId, Long prcDefId, String taskId) {
        String content = "";
        Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            String currentUserid = UserContextUtil.getInstance().getCurrentUserId();
            ProcessRuOpinion opinion = this.processOpinionDAO.getProData(currentUserid, actDefId, prcDefId, task.getTaskDefinitionKey(), Long.parseLong(task.getProcessInstanceId()), task.getId(), Long.parseLong(task.getExecutionId()));
            if (opinion != null) {
                content = opinion.getContent();
            }
        }

        return content;
    }

    public String getOpinionAttach(String actDefId, Long prcDefId, String taskId) {
        StringBuffer fieldHtml = new StringBuffer();
        Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            String currentUserid = UserContextUtil.getInstance().getCurrentUserId();
            ProcessRuOpinion opinion = this.processOpinionDAO.getProData(currentUserid, actDefId, (Long)null, task.getTaskDefinitionKey(), Long.parseLong(task.getProcessInstanceId()), task.getId(), Long.parseLong(task.getExecutionId()));
            if (opinion != null && opinion.getAttach() != null && !opinion.getAttach().equals("")) {
                List<FileUpload> list = FileUploadAPI.getInstance().getFileUploads(opinion.getAttach());
                Iterator var10 = list.iterator();

                while(var10.hasNext()) {
                    FileUpload fileUpload = (FileUpload)var10.next();
                    if (fileUpload != null) {
                        String fileDivId = fileUpload.getFileId();
                        fieldHtml.append("<div  id=\"").append(fileDivId).append("\" style=\"background-color: #F5F5F5;vertical-align:middle;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\">\n");
                        fieldHtml.append("<div style=\"align:right;float:right;\">\n");
                        fieldHtml.append("<a href=\"javascript:uploadifyReomve('attach','").append(fileUpload.getFileId()).append("','").append(fileDivId).append("');\"><img src=\"/iwork_img/del3.gif\"/></a>\n");
                        fieldHtml.append("</div>\n");
                        String icon = "iwork_img/attach.png";
                        if (fileUpload.getFileSrcName() != null) {
                            icon = FileType.getFileIcon(fileUpload.getFileSrcName());
                        }

                        fieldHtml.append("<span><a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append(fileUpload.getFileSrcName()).append("</a></span>\n");
                        fieldHtml.append("</div>\n");
                    }
                }
            }
        }

        return fieldHtml.toString();
    }

    private String reBuildTitle(Task task, String defTitle) {
        String reBuildtitle = "";
        if (task != null) {
            Map maps = this.processEngine.getRuntimeService().getVariables(task.getProcessInstanceId());
            if (maps != null) {
                ProcessDefDeploy deploy = this.processDefMapService.getProcessDeploymentDAO().getDeployModelByActDefId(task.getProcessDefinitionId());
                String formNo = ObjectUtil.getString(maps.get("PROCESS_TASK_FORMNO"));
                String title = ObjectUtil.getString(maps.get("PROCESS_TASK_TITLE"));
                String owner = ObjectUtil.getString(maps.get("PROCESS_TASK_OWNER"));
                String createDate = ObjectUtil.getString(maps.get("PROCESS_INSTANCE_CREATEDATE"));
                HashMap formmap = ProcessAPI.getInstance().getFromData(Long.parseLong(task.getProcessInstanceId()), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
                String defaultValue = defTitle;
                Pattern ptn = Pattern.compile("\\%(.*?)\\%");
                Matcher m = ptn.matcher(defTitle);

                while(m.find()) {
                    String label = m.group(1);
                    String value = "";
                    if (formmap.get(label.toUpperCase()) != null) {
                        value = ObjectUtil.getString(formmap.get(label.toUpperCase()));
                    }

                    if (value == null) {
                        value = "";
                    }

                    if (value != null && label != null) {
                        defaultValue = defaultValue.replace("%" + label + "%", value);
                    }
                }

                if (defaultValue.indexOf("${PROCESS_NAME}") > -1) {
                    if (deploy != null) {
                        defaultValue = defaultValue.replace("${PROCESS_NAME}", deploy.getTitle());
                    } else {
                        defaultValue = defaultValue.replace("${PROCESS_NAME}", "");
                    }
                }

                if (defaultValue.indexOf("${FORM_NO}") > -1) {
                    defaultValue = defaultValue.replace("${FORM_NO}", formNo);
                }

                if (defaultValue.indexOf("${OWNER}") > -1) {
                    UserContext ownerModel = UserContextUtil.getInstance().getUserContext(owner);
                    if (ownerModel != null) {
                        defaultValue = defaultValue.replace("${OWNER}", ownerModel.get_userModel().getUsername());
                    }
                }

                if (defaultValue.indexOf("${CREATEDATE}") > -1) {
                    defaultValue = defaultValue.replace("${CREATEDATE}", createDate);
                }

                reBuildtitle = defaultValue;
                this.processEngine.getRuntimeService().setVariable(task.getExecutionId(), "PROCESS_TASK_TITLE", defaultValue);
            }
        }

        return reBuildtitle;
    }

    public ProcessEngine getProcessEngine() {
        return this.processEngine;
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public TaskService getTaskService() {
        return this.taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public RepositoryService getRepositoryService() {
        return this.repositoryService;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public RuntimeService getRuntimeService() {
        return this.runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public ProcessStepFormDAO getProcessStepFormDAO() {
        return this.processStepFormDAO;
    }

    public void setProcessStepFormDAO(ProcessStepFormDAO processStepFormDAO) {
        this.processStepFormDAO = processStepFormDAO;
    }

    public ProcessDefMapService getProcessDefMapService() {
        return this.processDefMapService;
    }

    public void setProcessDefMapService(ProcessDefMapService processDefMapService) {
        this.processDefMapService = processDefMapService;
    }

    public ProcessDefParamService getProcessDefParamService() {
        return this.processDefParamService;
    }

    public void setProcessDefParamService(ProcessDefParamService processDefParamService) {
        this.processDefParamService = processDefParamService;
    }

    public ProcessDefTriggerService getProcessDefTriggerService() {
        return this.processDefTriggerService;
    }

    public void setProcessDefTriggerService(ProcessDefTriggerService processDefTriggerService) {
        this.processDefTriggerService = processDefTriggerService;
    }

    public ProcessStepFormService getProcessStepFormService() {
        return this.processStepFormService;
    }

    public void setProcessStepFormService(ProcessStepFormService processStepFormService) {
        this.processStepFormService = processStepFormService;
    }

    public ProcessStepFormMapService getProcessStepFormMapService() {
        return this.processStepFormMapService;
    }

    public void setProcessStepFormMapService(ProcessStepFormMapService processStepFormMapService) {
        this.processStepFormMapService = processStepFormMapService;
    }

    public ProcessStepManualJumpService getProcessStepManualJumpService() {
        return this.processStepManualJumpService;
    }

    public void setProcessStepManualJumpService(ProcessStepManualJumpService processStepManualJumpService) {
        this.processStepManualJumpService = processStepManualJumpService;
    }

    public ProcessStepMapService getProcessStepMapService() {
        return this.processStepMapService;
    }

    public void setProcessStepMapService(ProcessStepMapService processStepMapService) {
        this.processStepMapService = processStepMapService;
    }

    public ProcessStepSysjumpService getProcessStepSysjumpService() {
        return this.processStepSysjumpService;
    }

    public void setProcessStepSysjumpService(ProcessStepSysjumpService processStepSysjumpService) {
        this.processStepSysjumpService = processStepSysjumpService;
    }

    public ProcessStepTriggerService getProcessStepTriggerService() {
        return this.processStepTriggerService;
    }

    public void setProcessStepTriggerService(ProcessStepTriggerService processStepTriggerService) {
        this.processStepTriggerService = processStepTriggerService;
    }

    public SysEngineIFormDAO getSysEngineIFormDAO() {
        return this.sysEngineIFormDAO;
    }

    public void setSysEngineIFormDAO(SysEngineIFormDAO sysEngineIFormDAO) {
        this.sysEngineIFormDAO = sysEngineIFormDAO;
    }

    public IFormService getIformService() {
        return this.iformService;
    }

    public void setIformService(IFormService iformService) {
        this.iformService = iformService;
    }

    public ProcessRuntimeCcDAO getProcessRuntimeCcDAO() {
        return this.processRuntimeCcDAO;
    }

    public void setProcessRuntimeCcDAO(ProcessRuntimeCcDAO processRuntimeCcDAO) {
        this.processRuntimeCcDAO = processRuntimeCcDAO;
    }

    public ProcessOpinionDAO getProcessOpinionDAO() {
        return this.processOpinionDAO;
    }

    public void setProcessOpinionDAO(ProcessOpinionDAO processOpinionDAO) {
        this.processOpinionDAO = processOpinionDAO;
    }

    public void setProcessDefActionService(ProcessDefActionService processDefActionService) {
        this.processDefActionService = processDefActionService;
    }

    public void setProcessRuntimeSignsService(ProcessRuntimeSignsService processRuntimeSignsService) {
        this.processRuntimeSignsService = processRuntimeSignsService;
    }
}
