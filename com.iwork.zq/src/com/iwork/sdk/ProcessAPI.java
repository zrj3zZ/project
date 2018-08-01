package com.iwork.sdk;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.constant.SysConst;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.iform.dao.IFormDataDAO;
import com.iwork.core.engine.iform.dao.SysEngineFormBindDAO;
import com.iwork.core.engine.iform.dao.SysEngineIFormDAO;
import com.iwork.core.engine.iform.dao.SysEngineIFormMapDAO;
import com.iwork.core.engine.iform.dao.SysEngineSubformDAO;
import com.iwork.core.engine.iform.dao.SysInstanceDataDAO;
import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.core.engine.iform.model.SysEngineSubform;
import com.iwork.core.engine.iform.model.SysInstanceData;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataDAO;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataMapDAO;
import com.iwork.core.engine.metadata.model.SysEngineMetadata;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.definition.deployment.dao.ProcessDeploymentDAO;
import com.iwork.process.definition.deployment.model.ProcessDefDeploy;
import com.iwork.process.definition.deployment.service.ProcessDeploymentService;
import com.iwork.process.definition.step.model.ProcessStepForm;
import com.iwork.process.desk.handle.service.ProcessDeskManagementService;
import com.iwork.process.runtime.pvm.impl.task.PvmProcessTaskEngine;
import com.iwork.process.runtime.service.ProcessRuntimeExcuteService;
import com.iwork.process.definition.flow.service.ProcessDefParamService;
import com.iwork.process.definition.flow.model.ProcessDefParam;
import com.iwork.process.runtime.service.ProcessRuntimeSendService;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.process.tools.processopinion.service.ProcessOpinionService;
/**
 * 流程API
 * @author YangDayong
 *
 */
public class ProcessAPI extends EngineAPI{
	private static Logger logger = Logger.getLogger(ProcessAPI.class);
	 private static ProcessAPI instance;
	 private static ProcessRuntimeExcuteService processRuntimeExcuteService;  
	 private static ProcessRuntimeSendService processRuntimeSendService;  
	 private static SysEngineMetadataMapDAO sysEngineMetadataMapDAO;
	 private static IFormService iformService;
	 private static SysEngineIFormDAO sysEngineIFormDAO;
	 private static SysEngineSubformDAO sysEngineSubformDAO;
	 private static SysEngineMetadataDAO sysEngineMetadataDAO;
	 private static SysEngineIFormMapDAO sysEngineIFormMapDAO;
	 private static ProcessDefParamService processDefParamService;  
	 private static ProcessDeskManagementService processDeskManagementService;
	 private static SysInstanceDataDAO instanceDataDAO; // 表单实例模型
	 private ProcessOpinionService processOpinionService;   //审核意见
	 private static SysEngineFormBindDAO iformBindDAO; // 表单实例模型
	 private IFormDataDAO iformDataDAO;    //表单数据获取DAO
	 private ProcessDeploymentService processDeploymentService;    //流程模型
	 private static ProcessDeploymentDAO processDeploymentDAO;  
     private static Object lock = new Object();  
	 public static ProcessAPI getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new ProcessAPI();  
	                }
	            } 
	        }  
	        return instance;  
	 }
	 
	 /**
	  * 获得实例数据
	  * @param entityName 存储表名
	  * @param dataid 行记录ID
	  * @return
	  */
	 public Long getInstaceId(String entityName,Long dataid){
		return  this.getInstaceId(entityName, dataid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
	 }
	 
	 /**
	  * 获得流程办理用户执行列表
	  * @param instanceId
	  * @return
	  */
	 public List<OrgUser> getProcessTransUserList(Long instanceId){
		 List<OrgUser> list = null;
		 if(instanceId!=null&&!instanceId.equals(new Long(0))){
			 String processInstanceId = instanceId+"";
			 list = PvmProcessTaskEngine.getInstance().getTaskUserList(processInstanceId);
		 }
		 return list;
	 }
	 
	 /**
	  * 获取流程表单，子表数据列表
	  * @param formId
	  * @param instanceid
	  * @param subformkey
	  * @return
	  */
	 public List<HashMap> getSubFormDataList(Long formId ,Long instanceid,String subformkey){
	 	boolean flag = false;
	 	List<HashMap> list = null;
	 	if (sysEngineSubformDAO == null)
	 		sysEngineSubformDAO = (SysEngineSubformDAO) SpringBeanUtil.getBean("sysEngineSubformDAO");
	 	if (iformService == null)
	 		iformService = (IFormService) SpringBeanUtil.getBean("iformService");
	 	
	 	if (iformService != null && sysEngineSubformDAO != null) {
	 		List<SysEngineSubform> temp = sysEngineSubformDAO.getSubFormListforFormKey(formId,subformkey);
	 		if (temp != null && temp.size() > 0) {
	 			SysEngineSubform model = temp.get(0);
	 			if(model!=null){ 
	 				Long subFormId = model.getSubformid();
	 				list = iformService.getGridData(subFormId, instanceid);;
	 			}
	 		}
	 	}
	 	return list;
	 }
	 
	 /**
	  * 根据流程KEY，获取可运行的流程ID
	  * @param processDefinitionKey
	  * @return
	  */
	 public String getProcessActDefId(String processDefinitionKey){
		 String actDefId = null;
		 if(processRuntimeExcuteService==null)
			 processRuntimeExcuteService = (ProcessRuntimeExcuteService)SpringBeanUtil.getBean("processRuntimeExcuteService");
		 List<ProcessDefDeploy> list = processRuntimeExcuteService.getProcessDeploymentService().getProcessDeploymentDAO().getDeployListbyRun(processDefinitionKey, SysConst.on);
		 for(ProcessDefDeploy deploy:list){
			 actDefId = deploy.getActDefId();
		 }
		 return actDefId;
	 }
	 
	 /**
	  * 获取指定流程的默认表单ID
	  * @param actDefId
	  * @return
	  */
	 public Long getProcessDefaultFormId(String actDefId){
		 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
		  boolean matches=actDefId.matches(match);
		  if(!matches){
				return 0L;
		  }
		 if(processRuntimeExcuteService==null)
			 processRuntimeExcuteService = (ProcessRuntimeExcuteService)SpringBeanUtil.getBean("processRuntimeExcuteService");
		 Long formId = null;
		 ProcessDefDeploy pdd = processRuntimeExcuteService.getProcessDeploymentService().getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
		 if(pdd!=null){
			 String actDefStepId = processRuntimeExcuteService.getProcessTopStepId(actDefId);
				List<ProcessStepForm> psflist = processRuntimeExcuteService.getStepFromList(actDefId, pdd.getId(), actDefStepId);
				if(psflist!=null&&psflist.size()>0){
					ProcessStepForm psform = processRuntimeExcuteService.getDefProcessStepForm(null, psflist);
					if(psform!=null){
						formId = psform.getFormid();
					}
				}
		 }
		return formId;
	 }
	 /**
	  * 创建新的流程实例
	  * @param actDefId
	  * @return
	  */
	 public Long newInstance(String actDefId,Long formId,String createUser){
		 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
		  boolean matches=actDefId.matches(match);
		  if(!matches){
				return 0L;
		  }
		 Long instanceId = null;
		 if(formId==null){
			 formId =  this.getProcessDefaultFormId(actDefId);
		 } 
		 if(processRuntimeExcuteService==null)
			 processRuntimeExcuteService = (ProcessRuntimeExcuteService)SpringBeanUtil.getBean("processRuntimeExcuteService");
		 if(processDeploymentDAO==null)
			 processDeploymentDAO = (ProcessDeploymentDAO)SpringBeanUtil.getBean("processDeploymentDAO");
		 if(iformService==null)
			 iformService = (IFormService)SpringBeanUtil.getBean("iformService");
		if(processRuntimeExcuteService!=null&&processDeploymentDAO!=null){
			ProcessDefDeploy pdd = processDeploymentDAO.getDeployModelByActDefId(actDefId);
			if(pdd!=null){
				instanceId =  processRuntimeExcuteService.initProcessInstanceId(actDefId, pdd.getId(), createUser);
				//注册流程实例
				if(iformService!=null){
					iformService.initInstanceId(formId, instanceId, new Long(1));
				}
			}
			
		}
		 return instanceId;
	 }
	 
	 /**
	  * 获得指定用户、指定流程、指定节点的任务列表
	  * @param actDefId
	  * @param actStepId
	  * @param userId
	  * @return
	  */
	 public List<Task> getUserProcessTaskList(String actDefId,String actStepId,String userId){
		 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
		  boolean matches=actDefId.matches(match);
		  if(!matches){
				return null;
		  }
		  if( !DBUTilNew.validActStepId(actStepId) ){
			  return null;
			 }
		 if(processRuntimeExcuteService==null)
			 processRuntimeExcuteService = (ProcessRuntimeExcuteService)SpringBeanUtil.getBean("processRuntimeExcuteService");
		 List<Task>  list = processRuntimeExcuteService.getTaskService().createTaskQuery().processDefinitionId(actDefId).taskDefinitionKey(actStepId).taskAssignee(userId).list();
		 return list;
	 }
	 /**
	  * 创建新的流程实例
	  * @param actDefId
	  * @return
	  */
	 public Long setJumpTask(String actDefId,Long instanceId,String actStepId,String taskOwner){
		 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
		  boolean matches=actDefId.matches(match);
		  if(!matches){
				return 0L;
		  }
		  if( !DBUTilNew.validActStepId(actStepId) ){
			  return 0L;
			 }
		 if(processRuntimeExcuteService==null)
			 processRuntimeExcuteService = (ProcessRuntimeExcuteService)SpringBeanUtil.getBean("processRuntimeExcuteService");
		 return instanceId;
	 }
	 
	 /**
	  * 获得数据维护数据列表
	  * @param demUUID
	  * @return
	  */
	 public List<HashMap> getList(Long formId,String sortname,String fieldOrderBy){
		 	List<HashMap> list = null;
		 	 if(sysEngineIFormDAO==null)
		 		sysEngineIFormDAO = (SysEngineIFormDAO)SpringBeanUtil.getBean("sysEngineIFormDAO");
		 	 
		 	 if(sysEngineMetadataDAO==null)
		 		sysEngineMetadataDAO = (SysEngineMetadataDAO)SpringBeanUtil.getBean("sysEngineMetadataDAO");
		 	 
		 	 if(sysEngineMetadataMapDAO==null)
		 		sysEngineMetadataMapDAO = (SysEngineMetadataMapDAO)SpringBeanUtil.getBean("sysEngineMetadataMapDAO");
		 	 if(sysEngineIFormMapDAO==null)
		 		sysEngineIFormMapDAO = (SysEngineIFormMapDAO)SpringBeanUtil.getBean("sysEngineIFormMapDAO");
		 	 if(iformDataDAO==null)
		 		iformDataDAO = (IFormDataDAO)SpringBeanUtil.getBean("iformDataDAO");
		 	 
		 	 
		 	SysEngineIform formModel = sysEngineIFormDAO.getSysEngineIformModel(formId);
			SysEngineMetadata metadata = sysEngineMetadataDAO.getModel(formModel.getMetadataid());
//			List<SysEngineIformMap> iformMapList = sysEngineIFormMapDAO.getDataList(formId);
			List<SysEngineMetadataMap>  metadataMapList = sysEngineMetadataMapDAO.getDataList(formModel.getMetadataid());
			try{
				//获取总行数
				list = iformDataDAO.getDataList(formModel,metadataMapList,metadata.getEntityname(),sortname, fieldOrderBy, 0, 0);
			}catch(Exception e){
				logger.error(e,e);
			}
			return list; 
	 }
	 
	 /**
	  * 创建新的流程实例
	  * @param actDefId
	  * @param formId
	  * @param createUser
	  * @param formno  自定义流程单据编号
	  * @return
	  */
	 public Long newInstance(String actDefId,Long formId,String createUser,String formno){
		 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
		  boolean matches=actDefId.matches(match);
		  if(!matches){
				return 0L;
		  }
		 Long instanceId = null;
		 if(processRuntimeExcuteService==null)
			 processRuntimeExcuteService = (ProcessRuntimeExcuteService)SpringBeanUtil.getBean("processRuntimeExcuteService");
		 if(processDeploymentDAO==null)
			 processDeploymentDAO = (ProcessDeploymentDAO)SpringBeanUtil.getBean("processDeploymentDAO");
		 if(iformService==null)
			 iformService = (IFormService)SpringBeanUtil.getBean("iformService");
		 if(processRuntimeExcuteService!=null&&processDeploymentDAO!=null){
			 ProcessDefDeploy pdd = processDeploymentDAO.getDeployModelByActDefId(actDefId);
			 if(pdd!=null){
				 instanceId =  processRuntimeExcuteService.initProcessInstanceId(actDefId, pdd.getId(), createUser,formno);
				 //注册流程实例
				 if(iformService!=null){
					 iformService.initInstanceId(formId, instanceId, new Long(1));
				 }
			 }
			 
		 }
		 return instanceId;
	 }
	 /**
	  * 创建新的流程实例
	  * @param actDefId
	  * @param formId
	  * @param createUser
	  * @param formno
	  * @param hash
	  * @return
	  */
	 public Long newInstance(String actDefId,Long formId,String createUser,String formno,HashMap hash){
		 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
		  boolean matches=actDefId.matches(match);
		  if(!matches){
				return 0L;
		  }
		 Long instanceId = null;
		 if(processRuntimeExcuteService==null)
			 processRuntimeExcuteService = (ProcessRuntimeExcuteService)SpringBeanUtil.getBean("processRuntimeExcuteService");
		 if(processDeploymentDAO==null)
			 processDeploymentDAO = (ProcessDeploymentDAO)SpringBeanUtil.getBean("processDeploymentDAO");
		 if(iformService==null)
			 iformService = (IFormService)SpringBeanUtil.getBean("iformService");
		 if(processRuntimeExcuteService!=null&&processDeploymentDAO!=null){
			 ProcessDefDeploy pdd = processDeploymentDAO.getDeployModelByActDefId(actDefId);
			 if(pdd!=null){
				 instanceId =  processRuntimeExcuteService.initProcessInstanceId(actDefId, pdd.getId(), createUser,formno,hash);
				 //注册流程实例
				 if(iformService!=null){
					 iformService.initInstanceId(formId, instanceId, new Long(1));
				 }
			 }
			 
		 }
		 return instanceId;
	 }
	 
	 /**
	  * 根据当前流程实例ID，获取最新任务ID
	  * @param instanceId
	  * @return
	  */
	 public Task newTaskId(Long instanceId){
		 Task task =  null;
		 if(processRuntimeExcuteService==null)
			 processRuntimeExcuteService = (ProcessRuntimeExcuteService)SpringBeanUtil.getBean("processRuntimeExcuteService");
		 if(processRuntimeExcuteService!=null){
			 task = processRuntimeExcuteService.getProcessTask(instanceId);
		 }
		 return task;
	 }
	 /**
	  * 根据当前任务ID,获得任务模型
	  * @param instanceId
	  * @return
	  */
	 public Task getTask(String taskId){
		 Task task =  null;
		 if(processRuntimeExcuteService==null)
			 processRuntimeExcuteService = (ProcessRuntimeExcuteService)SpringBeanUtil.getBean("processRuntimeExcuteService");
		 if(processRuntimeExcuteService!=null){
			 task = processRuntimeExcuteService.getProcessTask(taskId);
		 }
		
		 return task;
	 }

		/**
		 * 保存表单数据
		 * 
		 * @param instanceId
		 * @param hashdata
		 * @return
		 */
		@SuppressWarnings("static-access")
		public boolean saveFormData(String actDefId,Long instanceId, HashMap hashdata,boolean isLog) {
			 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
			  boolean matches=actDefId.matches(match);
			  if(!matches){
					return false;
			  }
			boolean flag = false;
			if (processDeploymentDAO == null)
				processDeploymentDAO = (ProcessDeploymentDAO) SpringBeanUtil.getInstance()
						.getBean("processDeploymentDAO");
			ProcessDefDeploy pdd = processDeploymentDAO.getDeployModelByActDefId(actDefId);
			if(pdd==null){return false;}
			
			if (instanceDataDAO == null)
				instanceDataDAO = (SysInstanceDataDAO) SpringBeanUtil.getInstance()
				.getBean("instanceDataDAO");
			SysInstanceData sid = instanceDataDAO.getModel(instanceId,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if (sid != null) {
				Long formId = sid.getFormid();
				if (iformService == null)
					iformService = (IFormService) SpringBeanUtil.getInstance()
							.getBean("iformService");
				if (iformService != null) {
					Long id = iformService.saveForm(pdd.getId(),formId, instanceId, hashdata,isLog,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					if (id > 0) {
						flag = true;
					}
				}
			}
			return flag;
		}

		/**
		 * 保存表单数据
		 * 
		 * @param instanceId
		 * @param hashdata
		 * @return
		 */
		@SuppressWarnings({ "static-access", "rawtypes" })
		public boolean saveFormDatas(String actDefId,Long instanceId, String subformkey,
				List<HashMap> list,boolean isLog) {
			 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
			  boolean matches=actDefId.matches(match);
			  if(!matches){
					return false;
			  }
			boolean flag = false;
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			if (sysEngineSubformDAO == null)
				sysEngineSubformDAO = (SysEngineSubformDAO) SpringBeanUtil.getBean("sysEngineSubformDAO");
			if (processDeploymentDAO == null)
				processDeploymentDAO = (ProcessDeploymentDAO) SpringBeanUtil.getBean("processDeploymentDAO");
			ProcessDefDeploy pdd = processDeploymentDAO.getDeployModelByActDefId(actDefId);
			if(pdd==null){return false;}
			Long formId = this.getFormIdByInstanceId(instanceId,EngineConstants.SYS_INSTANCE_TYPE_PROCESS); 
			if (iformService != null && sysEngineSubformDAO != null) {
				List<SysEngineSubform> temp = sysEngineSubformDAO.getSubFormListforFormKey(formId,subformkey);
				if (temp != null && temp.size() > 0) {
					SysEngineSubform model = temp.get(0);
					Long subFormId = model.getSubformid();
					for (HashMap hash : list) {
						iformService.saveForm(pdd.getId(),subFormId, instanceId, hash,isLog,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					}
				}
				return true;
			}
			return flag;
		}

		/**
		 * 更新表单数据
		 * @param instanceId 实例ID
		 * @param hashdata 更新数据
		 * @param dataid 行标识ID
		 * @param islong 是否记录日志
		 * @return
		 */
		public boolean updateFormData(String actDefId,Long formId,Long instanceId, HashMap hashdata, Long dataid,boolean islog,Long engineType) {
			 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
			  boolean matches=actDefId.matches(match);
			  if(!matches){
					return false;
			  }
			boolean flag = false;
			if(formId==null){
				 formId = this.getFormIdByInstanceId(instanceId,engineType); 
			} 
			if (processDeploymentDAO == null)
				processDeploymentDAO = (ProcessDeploymentDAO) SpringBeanUtil.getBean("processDeploymentDAO");
			ProcessDefDeploy pdd = processDeploymentDAO.getDeployModelByActDefId(actDefId);
			if(pdd==null){return false;}
			
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			if (iformService != null) {
				flag = iformService.updateForm(pdd.getId(),formId, instanceId, hashdata, dataid,islog,engineType);
			}
			return flag;
		}
		
		/**
		 * 更新表单数据
		 * @param instanceId 实例ID
		 * @param hashdata 更新数据
		 * @param dataid 行标识ID
		 * @param islong 是否记录日志
		 * @return
		 */
		public boolean updateFormData(String actDefId,Long instanceId, HashMap hashdata, Long dataid,boolean islog,Long engineType) {
			 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
			  boolean matches=actDefId.matches(match);
			  if(!matches){
					return false;
			  }
			return updateFormData(actDefId,null,instanceId,hashdata,dataid,islog,engineType);
		}
		
		/**
		 * 获得子表数据列表
		 * @param instanceId
		 * @param subformkey
		 * @return
		 */
		public List<HashMap> getFromSubData(Long instanceId, String subformkey) {
			return this.getFromSubData(instanceId, subformkey, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		/**
		 * 更新表单数据
		 * @param instanceId 实例ID
		 * @param hashdata 更新数据
		 * @param dataid 行标识ID
		 * @param islong 是否记录日志
		 * @return
		 */
		public ArrayList<Integer> updateFormDatas(String actDefId, String subformkey,Long instanceId, List<HashMap> list,String setKey,String setValue,boolean islog,Long engineType) {
			 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
			  boolean matches=actDefId.matches(match);
			  if(!matches){
					return null;
			  }
			boolean flag = false;
			ArrayList<Integer> idlist = new ArrayList();
			if (processDeploymentDAO == null) 
				processDeploymentDAO = (ProcessDeploymentDAO) SpringBeanUtil.getBean("processDeploymentDAO");
			ProcessDefDeploy pdd = processDeploymentDAO.getDeployModelByActDefId(actDefId);
			if(pdd==null){return null;}
			if (sysEngineSubformDAO == null)
				sysEngineSubformDAO = (SysEngineSubformDAO) SpringBeanUtil.getBean("sysEngineSubformDAO");
			Long formId = this.getFormIdByInstanceId(instanceId,EngineConstants.SYS_INSTANCE_TYPE_PROCESS); 
			List<SysEngineSubform> temp = sysEngineSubformDAO.getSubFormListforFormKey(formId,subformkey);
			if(temp!=null&&temp.size()>0){
				SysEngineSubform model = temp.get(0);
				Long subFormId = model.getSubformid();
				if (iformService == null)
					iformService = (IFormService) SpringBeanUtil.getBean("iformService");
				if (iformService != null) {  
//					List<SysEngineFormBind> bindlist = iformService.getDataids(subFormId, instanceId, engineType);
					for(HashMap hash:list){
						if(setKey!=null){ 
							hash.put(setKey, setValue);
						}
						BigDecimal id = (BigDecimal)hash.get("ID");
//						for(SysEngineFormBind bind:bindlist){
//							Long dataid = bind.getDataid();
//							if(id.longValue()==dataid){
//								break;
//							}
//						}
						
						flag = iformService.updateForm(pdd.getId(),subFormId, instanceId, hash, id.longValue(),islog,engineType);
						if(flag){
							if(hash.get("ID")!=null){
								BigDecimal tmpID = (BigDecimal)hash.get("ID");
								idlist.add(tmpID.intValue());
							}
						}
					}
				}
			}
			return idlist;
		}
		
		public boolean removeSubFormData(String actDefId,Long instanceid,Long dataid,String subformkey){
			 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
			  boolean matches=actDefId.matches(match);
			  if(!matches){
					return false;
			  }
			boolean flag = false;
			Long processDefaultFormId = this.getProcessDefaultFormId(actDefId);
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			if (sysEngineSubformDAO == null)
				sysEngineSubformDAO = (SysEngineSubformDAO) SpringBeanUtil
				.getInstance().getBean("sysEngineSubformDAO");
			if (iformService != null && sysEngineSubformDAO != null) {
				List<SysEngineSubform> temp = sysEngineSubformDAO.getSubFormListforFormKey(processDefaultFormId,subformkey);
				if (temp != null && temp.size() > 0) {
					SysEngineSubform model = temp.get(0);
					if(model!=null&&dataid!=null){ 
						Long subFormId = model.getSubformid();
						flag = iformService.removeSubformData(subFormId, instanceid, dataid.toString());
					}
				}
			}
			return flag;
		}
		
		public boolean removeProcessInstance(String taskId,String userId){
			if (processDeskManagementService == null) 
				processDeskManagementService = (ProcessDeskManagementService) SpringBeanUtil.getBean("processDeskManagementService");
			boolean	flag = processDeskManagementService.deleteTask(String.valueOf(taskId), userId);
			return flag;
		}
		
		/**
		 * 获取流程全局变量
		 * @param actDefId
		 * @param prcDefId
		 * @param paramType  ProcessParamsConstant.PROCESS_PARAM_STATICE 静态参数 
		 * @param paramType  ProcessParamsConstant.PROCESS_PARAM_OU  组织变量
		 * @param paramType  ProcessParamsConstant.PROCESS_PARAM_EXTEND 外部数据源
		 * @param paramName
		 * @return
		 */
		public ProcessDefParam getProcessDefParamObj(String actDefId,Long  paramType, String paramName){
			 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
			  boolean matches=actDefId.matches(match);
			  if(!matches){
					return null;
			  }
			if (processDefParamService == null) 
				processDefParamService = (ProcessDefParamService) SpringBeanUtil.getBean("processDefParamService"); 
			return processDefParamService.getDefParamModel(actDefId, paramType, paramName); 
		}
		/**
		 * 发送流程抄送通知
		 * @param actDefId
		 * @param actStepDefId
		 * @param taskId 
		 * @param instanceId
		 * @param excutionId
		 * @param title
		 * @param users
		 * @param param
		 */
		public void sendProcessCC(String actDefId, String actStepDefId,String taskId,Long instanceId, Long excutionId,String title,String users){
			 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
			  boolean matches=actDefId.matches(match);
			  if(!matches){
					return ;
			  }
			if (processRuntimeSendService == null) 
				processRuntimeSendService = (ProcessRuntimeSendService) SpringBeanUtil.getBean("processRuntimeSendService");
			processRuntimeSendService.excuteCC(actDefId, actStepDefId, taskId, instanceId, excutionId, title, users, null);
			
		}
		
		/**
		 * 获取审核意见列表
		 * @param actDefId
		 * @param instanceid
		 * @return
		 */
		public List<ProcessRuOpinion> getProcessOpinionList(String actDefId,Long instanceid){
			 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
			  boolean matches=actDefId.matches(match);
			  if(!matches){
					return null;
			  }
			if (processOpinionService == null)   
				processOpinionService = (ProcessOpinionService) SpringBeanUtil.getBean("processOpinionService");
			return processOpinionService.getProcessRuOpinionList(actDefId, instanceid);
		}
		
		 public void setTaskAssignee(String taskId,String assignee){
			 if(processRuntimeExcuteService==null)
				 processRuntimeExcuteService = (ProcessRuntimeExcuteService)SpringBeanUtil.getBean("processRuntimeExcuteService");
			processRuntimeExcuteService.getTaskService().setAssignee(taskId, assignee);
		 }
		 
		 /**
		 * 删除实例数据
		 * @param demUUID
		 * @param instanceId
		 * @return
		 */
		public boolean removeFormData(Long instanceId){
			boolean flag = false;
			Long formId = this.getFormIdByInstanceId(instanceId,EngineConstants.SYS_INSTANCE_TYPE_PROCESS); 
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			if (iformService != null) {
				iformService.removeFormData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				flag = true;
			}
			return flag;
		}
		
		/**
		 * 获取流程模型ID
		 * @param actDefId
		 * @return
		 */
		public Long getProcessOpinion(String actDefId){
			 String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
			  boolean matches=actDefId.matches(match);
			  if(!matches){
					return 0L;
			  }
			Long prcDefId=0L;
			if (processDeploymentService == null)   
				processDeploymentService = (ProcessDeploymentService) SpringBeanUtil.getBean("processDeploymentService");
			ProcessDefDeploy deploy = processDeploymentService.getProcessDeploymentDAO().getDeployModelByActDefId(actDefId);
	        if(deploy != null)
	        	prcDefId = deploy.getId();
			return prcDefId;
		}
}
