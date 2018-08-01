package com.iwork.process.runtime.pvm.impl.execute;

import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.runtime.constant.BackTypeConstant;
import com.iwork.process.runtime.constant.ProcessTaskConstant;
import com.iwork.process.tools.processopinion.dao.ProcessOpinionDAO;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;

import java.util.*;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.*;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.el.UelExpressionCondition;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.task.Task;

public class PvmDefExecuteEngine
{

    public PvmDefExecuteEngine()
    {
    }

    public static PvmDefExecuteEngine getInstance()
    {
        if(instance == null)
            synchronized(lock)
            {
                if(instance == null)
                    instance = new PvmDefExecuteEngine();
            }
        if(processEngine == null)
            processEngine = (ProcessEngine)SpringBeanUtil.getBean("processEngine");
        return instance;
    }

    public boolean isBackPrevious(ActivityImpl activityImpl)
    {
        boolean flag = true;
        if(activityImpl != null)
        {
            List in_trans = activityImpl.getIncomingTransitions();
            for(Iterator iterator = in_trans.iterator(); iterator.hasNext();)
            {
                PvmTransition pt = (PvmTransition)iterator.next();
                ActivityImpl srcActivity = (ActivityImpl)pt.getSource();
                if(srcActivity != null && (srcActivity.getActivityBehavior() instanceof ParallelGatewayActivityBehavior))
                {
                    flag = false;
                    break;
                }
            }

        } else
        {
            flag = false;
        }
        return flag;
    }

    public HistoricActivityInstance getBackActivityId(String taskId, String currentActivityId)
    {
        HistoricActivityInstance instance = null;
        if(processEngine == null)
            processEngine = (ProcessEngine)SpringBeanUtil.getBean("processEngine");
        HistoryService historyService = processEngine.getHistoryService();
        TaskService taskService = processEngine.getTaskService();
        Task task = (Task)taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = task.getProcessDefinitionId();
        String executionId = task.getExecutionId();
        String stepName = task.getName();
        Long hisTaskId=0L;
        List<HistoricTaskInstance> list2 = (historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).taskDefinitionKey(currentActivityId).orderByTaskId().desc()).list();
        for (HistoricTaskInstance historicTaskInstance : list2) {
			if(!historicTaskInstance.getName().equals("驳回")){
				hisTaskId=Long.parseLong(historicTaskInstance.getId());
				break;
			}
		}
        List list = ((HistoricActivityInstanceQuery)historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByHistoricActivityInstanceEndTime().desc()).list();
        if(list != null)
        {
            int count = 0;
            Iterator iterator = list.iterator();
            while(iterator.hasNext()) 
            {
                HistoricActivityInstance history = (HistoricActivityInstance)iterator.next();
                if(history.getActivityId().equals("999999") || history.getActivityId().equals(currentActivityId)){
                	continue;
                }
                if(count == 1)
                    break;
                if(stepName != null && !stepName.equals("\u9A73\u56DE"))
                {
                    if(processOpinionDAO == null)
                        processOpinionDAO = (ProcessOpinionDAO)SpringBeanUtil.getBean("processOpinionDAO");
                    ProcessRuOpinion opinion = processOpinionDAO.getProData(history.getAssignee(), history.getProcessDefinitionId(), null, history.getActivityId(), Long.valueOf(Long.parseLong(history.getProcessInstanceId())), (new StringBuilder(String.valueOf(Long.parseLong(history.getId()) + 1L))).toString(), Long.valueOf(Long.parseLong(history.getExecutionId())));
                    if(opinion != null && opinion.getAction().equals("\u9A73\u56DE"))
                        continue;
                }
                if(hisTaskId<Long.parseLong(history.getId())){
                	continue;
                }
                instance = history;
                count++;
            }
        }
        return instance;
    }

    public HistoricActivityInstance getstartActivityId(String taskId)
    {
        HistoricActivityInstance instance = null;
        if(processEngine == null)
            processEngine = (ProcessEngine)SpringBeanUtil.getBean("processEngine");
        HistoryService historyService = processEngine.getHistoryService();
        TaskService taskService = processEngine.getTaskService();
        Task task = (Task)taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task != null)
        {
            String targetStepDefId = "";
            org.activiti.engine.RepositoryService rps = processEngine.getRepositoryService();
            ProcessDefinitionEntity pde = (ProcessDefinitionEntity)((RepositoryServiceImpl)rps).getDeployedProcessDefinition(task.getProcessDefinitionId());
            List list = pde.getActivities();
            for(Iterator iterator = list.iterator(); iterator.hasNext();)
            {
                ActivityImpl ai = (ActivityImpl)iterator.next();
                if(ai.getActivityBehavior() instanceof NoneStartEventActivityBehavior)
                {
                    List out_trans = ai.getOutgoingTransitions();
                    for(Iterator iterator2 = out_trans.iterator(); iterator2.hasNext();)
                    {
                        PvmTransition pt = (PvmTransition)iterator2.next();
                        ActivityImpl targetActivity = (ActivityImpl)pt.getDestination();
                        if(targetActivity != null && (targetActivity.getActivityBehavior() instanceof UserTaskActivityBehavior))
                        {
                            targetStepDefId = targetActivity.getId();
                            break;
                        }
                    }

                    break;
                }
            }

            List historicList = ((HistoricActivityInstanceQuery)historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByHistoricActivityInstanceEndTime().desc()).list();
            if(list != null && !targetStepDefId.equals(""))
            {
                int count = 0;
                for(Iterator iterator1 = historicList.iterator(); iterator1.hasNext();)
                {
                    HistoricActivityInstance history = (HistoricActivityInstance)iterator1.next();
                    if(history.getActivityId().equals(targetStepDefId))
                    {
                        instance = history;
                        break;
                    }
                }

            }
        }
        return instance;
    }

    public boolean executeJump(String taskId, ActivityImpl ai, ActivityImpl backActivity, String userid[], Map taskVarParam)
    {
        String uuid = "";
        boolean flag = false;
        if(backActivity.getActivityBehavior() instanceof UserTaskActivityBehavior)
        {
            ProcessEngine pe = (ProcessEngine)SpringBeanUtil.getBean("processEngine");
            TaskService taskService = pe.getTaskService();
            RuntimeService runtimeService = pe.getRuntimeService();
            Task task = (Task)taskService.createTaskQuery().taskId(taskId).singleResult();
            ExecutionEntity exe = (ExecutionEntity)runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
            uuid = UUID.randomUUID().toString();
            TransitionImpl transition = ai.createOutgoingTransition(uuid);
            transition.setDestination(backActivity);
            ExpressionManager em = ((UserTaskActivityBehavior)ai.getActivityBehavior()).getExpressionManager();
            String cmd = (new StringBuilder("${TransitionImpl == \"")).append(uuid).append("\"}").toString();
            org.activiti.engine.impl.Condition expressionCondition = new UelExpressionCondition(em.createExpression(cmd));
            transition.setProperty("conditionText", cmd);
            transition.setProperty("condition", expressionCondition);
            if(taskVarParam == null)
                taskVarParam = new HashMap();
            taskVarParam.put("TransitionImpl", uuid);
            taskVarParam.put("PROCESS_TASK_OWNER", UserContextUtil.getInstance().getCurrentUserId());
            taskVarParam.put("assignee", userid);
            taskVarParam.put("PROCESS_EXCUTION_READ_STATUS", ProcessTaskConstant.PROCESS_EXCUTION_UNREAD);
            taskService.complete(taskId, taskVarParam);
            ai.removeOutgoingTransition(uuid);
            exe.setActivity(ai);
            flag = true;
        }
        return flag;
    }

    public boolean executeBack(String taskId, ActivityImpl ai, ActivityImpl backActivity, String userid, String action, String priority, Long backType)
    {
        String uuid = "";
        boolean flag = false;
        if(backActivity.getActivityBehavior() instanceof UserTaskActivityBehavior)
        {
            ProcessEngine pe = (ProcessEngine)SpringBeanUtil.getBean("processEngine");
            TaskService taskService = pe.getTaskService();
            RuntimeService runtimeService = pe.getRuntimeService();
            Task task = (Task)taskService.createTaskQuery().taskId(taskId).singleResult();
            if(backType.equals(new Long(2L)) && task != null)
            {
                String currentUserId = UserContextUtil.getInstance().getCurrentUserId();
                runtimeService.setVariable(task.getExecutionId(), BackTypeConstant.BACK_TO_BACK_USERID, userid);
                runtimeService.setVariable(task.getExecutionId(), BackTypeConstant.BACK_TO_BACK_STEPID, backActivity.getId());
                runtimeService.setVariable(task.getExecutionId(), BackTypeConstant.BACK_TO_BACK_SRC_USERID, currentUserId);
                runtimeService.setVariable(task.getExecutionId(), BackTypeConstant.BACK_TO_BACK_SRC_STEPID, ai.getId());
                runtimeService.setVariable(task.getExecutionId(), BackTypeConstant.BACK_TO_BACK_SRC_STEPNAME, task.getName());
            }
            ExecutionEntity exe = (ExecutionEntity)runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
            uuid = UUID.randomUUID().toString();
            TransitionImpl transition = ai.createOutgoingTransition(uuid);
            transition.setDestination(backActivity);
            ExpressionManager em = ((UserTaskActivityBehavior)ai.getActivityBehavior()).getExpressionManager();
            String cmd = (new StringBuilder("${TransitionImpl == \"")).append(uuid).append("\"}").toString();
            org.activiti.engine.impl.Condition expressionCondition = new UelExpressionCondition(em.createExpression(cmd));
            transition.setProperty("conditionText", cmd);
            transition.setProperty("condition", expressionCondition);
            Map taskVarTable = new HashMap();
            taskVarTable.put("TransitionImpl", uuid);
            taskVarTable.put("PROCESS_TASK_OWNER", UserContextUtil.getInstance().getCurrentUserId());
            taskVarTable.put("assignee", userid);
            taskVarTable.put("PROCESS_EXCUTION_READ_STATUS", ProcessTaskConstant.PROCESS_EXCUTION_UNREAD);
            if(action != null)
            {
                taskVarTable.put("PROCESS_EXCUTION_PROCESS_STEP_NAME", action);
                taskVarTable.put("PROCESS_TASK_PRIORITY", priority);
            }
            taskService.complete(taskId, taskVarTable);
            ai.removeOutgoingTransition(uuid);
            exe.setActivity(ai);
            flag = true;
        }
        return flag;
    }

    public boolean checkActivityIsParallel(Long instanceId)
    {
        boolean flag = false;
        if(instanceId != null)
        {
            if(processEngine == null)
                processEngine = (ProcessEngine)SpringBeanUtil.getBean("processEngine");
            List list = processEngine.getRuntimeService().createExecutionQuery().processInstanceId(instanceId.toString()).list();
            if(list != null && list.size() > 1)
                flag = true;
        }
        return flag;
    }

    public boolean killOtherTask(String taskId, Long processInstanceId)
    {
        boolean flag = false;
        if(taskId != null && processInstanceId != null && checkActivityIsParallel(processInstanceId))
        {
            if(processEngine == null)
                processEngine = (ProcessEngine)SpringBeanUtil.getBean("processEngine");
            TaskService taskService = processEngine.getTaskService();
            List list = taskService.createTaskQuery().processInstanceId(processInstanceId.toString()).list();
            for(Iterator iterator = list.iterator(); iterator.hasNext();)
            {
                Task task = (Task)iterator.next();
                if(!task.getId().equals(taskId))
                {
                    taskService.deleteTask(task.getId(), true);
                    flag = true;
                }
            }

        }
        return flag;
    }

    public String getCCUserList(String actDefId, Long prcDefId, String actStepId, String s)
    {
        return null;
    }

    private static PvmDefExecuteEngine instance;
    private static ProcessEngine processEngine;
    private static ProcessOpinionDAO processOpinionDAO;
    private static Object lock = new Object();

}
