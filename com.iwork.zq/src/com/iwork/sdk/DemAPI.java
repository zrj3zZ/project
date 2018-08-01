package com.iwork.sdk;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.dao.SysDemEngineDAO;
import com.iwork.core.engine.dem.model.SysDemEngine;
import com.iwork.core.engine.dem.service.SysDemWorkBoxService;
import com.iwork.core.engine.iform.dao.IFormDataDAO;
import com.iwork.core.engine.iform.dao.SysEngineIFormDAO;
import com.iwork.core.engine.iform.dao.SysEngineIFormMapDAO;
import com.iwork.core.engine.iform.dao.SysEngineSubformDAO;
import com.iwork.core.engine.iform.dao.SysInstanceDataDAO;
import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.core.engine.iform.model.SysEngineIformMap;
import com.iwork.core.engine.iform.model.SysEngineSubform;
import com.iwork.core.engine.iform.model.SysInstanceData;
import com.iwork.core.engine.iform.model.SysInstanceDataId;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataDAO;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataMapDAO;
import com.iwork.core.engine.metadata.model.SysEngineMetadata;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;
import com.iwork.core.util.SpringBeanUtil;
import org.apache.log4j.Logger;
/**
 * 数据维护API
 * @author YangDayong
 *
 */
public class DemAPI extends EngineAPI{ 
	private static Logger logger = Logger.getLogger(DemAPI.class);
	 private static DemAPI instance;  
	 private static SysEngineMetadataMapDAO sysEngineMetadataMapDAO;
	 private static SysDemWorkBoxService sysDemWorkBoxService;
	 private static IFormService iformService;
	 private static SysEngineIFormDAO sysEngineIFormDAO;
	 private static IFormDataDAO iformDataDAO;    //表单数据获取DAO
	 private static SysEngineSubformDAO sysEngineSubformDAO;
	 private static SysEngineMetadataDAO sysEngineMetadataDAO;
	 private static SysEngineIFormMapDAO sysEngineIFormMapDAO;
	 private static SysInstanceDataDAO instanceDataDAO;   //表单实例模型
	 private static SysDemEngineDAO sysDemEngineDAO;   //表单实例模型
     private static Object lock = new Object();  
	 public static DemAPI getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new DemAPI();  
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
	  * 创建新的数据管理id
	  * @param demId
	  * @param createUser
	  * @return
	  */
	 public Long newInstance(String demUUID,String createUser){
		 Long instanceId = null;
		 if(instanceDataDAO==null)
			 instanceDataDAO = (SysInstanceDataDAO)SpringBeanUtil.getBean("instanceDataDAO");
		 if(sysDemEngineDAO==null){
			 sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
		 }
		 if(instanceDataDAO!=null&&sysDemEngineDAO!=null&&demUUID!=null){
			 SysDemEngine sde = sysDemEngineDAO.getModel(demUUID);
			 Long formId = sde.getFormid();
			 	SysInstanceData model = new SysInstanceData();
			 	 instanceId =  instanceDataDAO.getMaxID(sde.getType());
			 	 SysInstanceDataId id = new SysInstanceDataId();
				 instanceId = instanceDataDAO.getMaxID(sde.getType());
				 id.setId(instanceId);
				 id.setType(EngineConstants.SYS_INSTANCE_TYPE_DEM);
				model.setId(id);
				model.setCreatedate(new Date());
				model.setCreateuser(createUser);
				model.setUpdatedate(new Date());
				model.setUpdateuser(createUser);
				model.setFormid(formId);
				instanceId = instanceDataDAO.create(model);
		 }
		 return instanceId;
	 }
	 /**
	  * 获得数据维护数据列表
	  * @param demUUID
	  * @return
	  */
	 public List<HashMap> getAllList(String demUUID,HashMap conditionMap,String fieldOrderBy){
		 Long demId = null;
		 Long formId = null;
		 SysEngineIform formModel = null;
		 SysEngineMetadata metadata = null;
		 List<SysEngineIformMap> iformMapList = null;
		 boolean flag = false;
		 if(sysDemWorkBoxService==null){
			 sysDemWorkBoxService = (SysDemWorkBoxService)SpringBeanUtil.getBean("sysDemWorkBoxService");
		 }
		 if(sysDemEngineDAO==null){
			 sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
		 } 
		 SysDemEngine sde = sysDemEngineDAO.getModel(demUUID);
		 if(sde!=null){
			 demId = sde.getId();
			 formId = sde.getFormid(); 
		 }
		 List list = sysDemWorkBoxService.getDataBoxAllList(demId, formId, conditionMap,fieldOrderBy);
		 return list;
	 }
	 /**
	  * 获得数据维护数据列表
	  * @param demUUID
	  * @return
	  */
	 public HashMap getFormData(String demUUID,Long dataid){
		 HashMap formData = null;
		 Long demId = null;
		 Long formId = null;
		 SysEngineIform formModel = null;
		 SysEngineMetadata metadata = null;
		 List<SysEngineIformMap> iformMapList = null;
		 boolean flag = false;
		 if(sysDemWorkBoxService==null){
			 sysDemWorkBoxService = (SysDemWorkBoxService)SpringBeanUtil.getBean("sysDemWorkBoxService");
		 }
		 if(sysEngineIFormDAO==null){
			 sysEngineIFormDAO = (SysEngineIFormDAO)SpringBeanUtil.getBean("sysEngineIFormDAO");
		 }
		 if(sysEngineIFormMapDAO==null){
			 sysEngineIFormMapDAO = (SysEngineIFormMapDAO)SpringBeanUtil.getBean("sysEngineIFormMapDAO");
		 }
		 if(sysDemEngineDAO==null){
			 sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
		 } 
		 if(sysEngineMetadataDAO==null){
			 sysEngineMetadataDAO = (SysEngineMetadataDAO)SpringBeanUtil.getBean("sysEngineMetadataDAO");
		 } 
		 if(iformDataDAO==null){
			 iformDataDAO = (IFormDataDAO)SpringBeanUtil.getBean("iformDataDAO");
		 } 
		 if(sysEngineMetadataMapDAO==null)
				sysEngineMetadataMapDAO = (SysEngineMetadataMapDAO)SpringBeanUtil.getBean("sysEngineMetadataMapDAO");
		 SysDemEngine sde = sysDemEngineDAO.getModel(demUUID);
		 if(sde!=null){
			 demId = sde.getId();
			 formId = sde.getFormid(); 
			 SysEngineIform  sei = sysEngineIFormDAO.getSysEngineIformModel(formId);
			 if(sei!=null){
				 metadata = sysEngineMetadataDAO.getModel(sei.getMetadataid());
				 if(metadata!=null){
					 List<SysEngineMetadataMap>  metadataList = sysEngineMetadataMapDAO.getDataList(metadata.getId());
					 if(metadata!=null){
						 try {
								formData = iformDataDAO.getFormData(metadataList, metadata.getEntityname(), dataid);
							} catch (Exception e) {
								logger.error(e,e);
							}
					 }
				 }
				
			 }
			
		 }
		 return formData;
	 }
	 /**
	  * 根据数据维护UUID，获得数据维护ID
	  * @param demUUID
	  * @return
	  */
	 public SysDemEngine getDemModel(String demUUID){
		 Long demId = null;
		 if(sysDemEngineDAO==null){
				sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
			}
			SysDemEngine sde = sysDemEngineDAO.getModel(demUUID);
			return sde;
	 }
	 /**
	  * 创建新的数据管理id
	  * @param demId
	  * @param createUser
	  * @return
	  */
	 public Long newInstance(Long demId,String createUser){
		 Long instanceId = null;
		 if(instanceDataDAO==null)
			 instanceDataDAO = (SysInstanceDataDAO)SpringBeanUtil.getBean("instanceDataDAO");
		 if(sysDemEngineDAO==null){
			 sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
		 }
		 if(instanceDataDAO!=null&&sysDemEngineDAO!=null){
			 SysDemEngine sde = sysDemEngineDAO.getModel(demId);
			 Long formId = sde.getFormid();
			 SysInstanceData model = new SysInstanceData();
			 SysInstanceDataId id = new SysInstanceDataId();
			 instanceId = instanceDataDAO.getMaxID(sde.getType());
			 id.setId(instanceId);
			 id.setType(EngineConstants.SYS_INSTANCE_TYPE_DEM);
			 model.setId(id);
			 model.setCreatedate(new Date());
			 model.setCreateuser(createUser);
			 model.setUpdatedate(new Date());
			 model.setUpdateuser(createUser);
			 model.setFormid(formId);
			 instanceId = instanceDataDAO.create(model);
		 }
		 return instanceId;
	 }
	  
	 /**
	  * 获得主数据列表
	  * @param demUUID
	  * @return
	  */
	 public List<HashMap> getList(String demUUID,HashMap conditionMap,String fieldOrderBy){
		 Long demId = null;
		 Long formId = null;
		 SysEngineIform formModel = null;
		 SysEngineMetadata metadata = null;
		 List<SysEngineIformMap> iformMapList = null;
			boolean flag = false;
			if(sysDemWorkBoxService==null){
				sysDemWorkBoxService = (SysDemWorkBoxService)SpringBeanUtil.getBean("sysDemWorkBoxService");
			}
			if(sysDemEngineDAO==null){
				sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
			} 
			SysDemEngine sde = sysDemEngineDAO.getModel(demUUID);
			if(sde!=null){
				demId = sde.getId();
				formId = sde.getFormid();
			}
			List list = sysDemWorkBoxService.getDataBoxList(demId, formId, conditionMap,fieldOrderBy);
			return list;
	 }
	 
	 /**
	  * 获得主数据列表
	  * （适用于分页）
	  * @param demUUID
	  * @param conditionMap
	  * @param fieldOrderBy
	  * @param pageSize
	  * @param startRow
	  * @return
	  */
	 public List<HashMap> getList(String demUUID,HashMap conditionMap,String fieldOrderBy,int pageSize, int startRow){
		 Long demId = null;
		 Long formId = null;
		 SysEngineIform formModel = null;
		 SysEngineMetadata metadata = null;
		 List<SysEngineIformMap> iformMapList = null;
		 boolean flag = false;
		 if(sysDemWorkBoxService==null){
			 sysDemWorkBoxService = (SysDemWorkBoxService)SpringBeanUtil.getBean("sysDemWorkBoxService");
		 }
		 if(sysDemEngineDAO==null){
			 sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
		 } 
		 SysDemEngine sde = sysDemEngineDAO.getModel(demUUID);
		 if(sde!=null){
			 demId = sde.getId();
			 formId = sde.getFormid();
		 }
		 List list = sysDemWorkBoxService.getDataBoxList(demId, formId, conditionMap,fieldOrderBy,pageSize,startRow);
		 return list;
	 }
	 
	 /**
	  * 获得总条目数
	  * @param demUUID
	  * @param conditionMap 
	  * @param fieldOrderBy
	  * @return
	  */
	 public int getListSize(String demUUID,HashMap conditionMap,String fieldOrderBy){
		 Long demId = null;
		 Long formId = null;
		 SysEngineIform formModel = null;
		 SysEngineMetadata metadata = null;
		 List<SysEngineIformMap> iformMapList = null;
		 boolean flag = false;
		 if(sysDemWorkBoxService==null){
			 sysDemWorkBoxService = (SysDemWorkBoxService)SpringBeanUtil.getBean("sysDemWorkBoxService");
		 }
		 if(sysDemEngineDAO==null){
			 sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
		 } 
		 SysDemEngine sde = sysDemEngineDAO.getModel(demUUID);
		 if(sde!=null){
			 demId = sde.getId();
			 formId = sde.getFormid();
		 }
		 int count = sysDemWorkBoxService.getDataBoxListSize(demId, formId, conditionMap, fieldOrderBy);
		 return count;
	 }
	 /**
		 * 保存表单数据
		 * 
		 * @param instanceId
		 * @param hashdata
		 * @return
		 */
		@SuppressWarnings("static-access")
		public boolean saveFormData(Long demId,Long instanceId, HashMap hashdata,boolean isLog,Long engineType) {
			boolean flag = false;
			if (instanceDataDAO == null)
				instanceDataDAO = (SysInstanceDataDAO) SpringBeanUtil.getInstance()
						.getBean("instanceDataDAO");
			SysInstanceData sid = instanceDataDAO.getModel(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
			if (sid != null) {
				Long formId = sid.getFormid();
				if (iformService == null)
					iformService = (IFormService) SpringBeanUtil.getInstance()
							.getBean("iformService");
				if (iformService != null) {
					Long id = iformService.saveForm(demId,formId, instanceId, hashdata,isLog,engineType);
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
		@SuppressWarnings("static-access")
		public boolean saveFormData(String demUUID,Long instanceId, HashMap hashdata,boolean isLog) {
			Long demId = null;
			boolean flag = false;
			if(sysDemEngineDAO==null){
				sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
				SysDemEngine sde = sysDemEngineDAO.getModel(demUUID);
				if(sde!=null){
					demId = sde.getId();
				}
			}
			if (instanceDataDAO == null)
				instanceDataDAO = (SysInstanceDataDAO) SpringBeanUtil.getInstance()
				.getBean("instanceDataDAO");
			SysInstanceData sid = instanceDataDAO.getModel(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
			if (sid != null) {
				Long formId = sid.getFormid();
				if (iformService == null)
					iformService = (IFormService) SpringBeanUtil.getInstance()
					.getBean("iformService");
				if (iformService != null) {
					Long id = iformService.saveForm(demId,formId, instanceId, hashdata,isLog,EngineConstants.SYS_INSTANCE_TYPE_DEM);
					if (id > 0) {
						flag = true;
					}
				}
			}
			return flag;
		}

		
		/**
		 * 保存表单数据
		 * @param demId
		 * @param instanceId
		 * @param subformkey
		 * @param list
		 * @param isLog
		 * @return
		 */
		public boolean saveFormDatas(Long demId,Long instanceId, String subformkey,
				List<HashMap> list,boolean isLog) {
			boolean flag = false;
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			if (sysEngineSubformDAO == null)
				sysEngineSubformDAO = (SysEngineSubformDAO) SpringBeanUtil
						.getInstance().getBean("sysEngineSubformDAO");
			Long formId = this.getFormIdByInstanceId(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM); 
			if (iformService != null && sysEngineSubformDAO != null) {
				List<SysEngineSubform> temp = sysEngineSubformDAO.getSubFormListforFormKey(formId,subformkey);
				if (temp != null && temp.size() > 0) {
					SysEngineSubform model = temp.get(0);
					Long subFormId = model.getSubformid();
					for (HashMap hash : list) {
						iformService.saveForm(demId,subFormId, instanceId, hash,isLog,EngineConstants.SYS_INSTANCE_TYPE_DEM);
					}
				}
				return true;
			}
			return flag;
		}
		/**
		 * 保存表单数据
		 * @param demId
		 * @param instanceId
		 * @param subformkey
		 * @param list
		 * @param isLog
		 * @return
		 */
		@SuppressWarnings({ "static-access", "rawtypes" }) 
		public boolean saveFormDatas(String demUUID,Long instanceId, String subformkey,
				List<HashMap> list,boolean isLog,Long engineType) {
			Long demId = null;
			boolean flag = false;
			if(sysDemEngineDAO==null){
				sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
				SysDemEngine sde = sysDemEngineDAO.getModel(demUUID);
				if(sde!=null){
					demId = sde.getId();
				}
			}
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			if (sysEngineSubformDAO == null)
				sysEngineSubformDAO = (SysEngineSubformDAO) SpringBeanUtil
				.getInstance().getBean("sysEngineSubformDAO");
			Long formId = this.getFormIdByInstanceId(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM); 
			if (iformService != null && sysEngineSubformDAO != null) {
				List<SysEngineSubform> temp = sysEngineSubformDAO.getSubFormListforFormKey(formId,subformkey);
				if (temp != null && temp.size() > 0) {
					SysEngineSubform model = temp.get(0);
					Long subFormId = model.getSubformid();
					for (HashMap hash : list) {
						iformService.saveForm(demId,subFormId, instanceId, hash,isLog,engineType);
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
		 * @param modelId 模型ID,数据维护是传入demId  流程表单模型则传入 proDefId
		 * @param islong 是否记录日志
		 * @return
		 */
		public boolean updateFormData(String demUUID ,Long instanceId, HashMap hashdata, Long dataid,boolean islog) {
			Long demId = null;
			boolean flag = false;
			if(sysDemEngineDAO==null){
				 sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
				 SysDemEngine sde = sysDemEngineDAO.getModel(demUUID);
				 if(sde!=null){
					 demId = sde.getId();
				 }
			 }
			Long formId = this.getFormIdByInstanceId(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM); 
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			if (iformService != null) {
				flag = iformService.updateForm(demId,formId, instanceId, hashdata, dataid,islog,EngineConstants.SYS_INSTANCE_TYPE_DEM);
			}
			return flag;
		}
		
		/**
		 * 更新表单数据
		 * @param instanceId 实例ID
		 * @param hashdata 更新数据
		 * @param dataid 行标识ID
		 * @param modelId 模型ID,数据维护是传入demId  流程表单模型则传入 proDefId
		 * @param islong 是否记录日志
		 * @return
		 */
		public boolean updateFormData(Long demId ,Long instanceId, HashMap hashdata, Long dataid,boolean islog) {
			boolean flag = false;
			Long formId = this.getFormIdByInstanceId(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM); 
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			if (iformService != null) {
				flag = iformService.updateForm(demId,formId, instanceId, hashdata, dataid,islog,EngineConstants.SYS_INSTANCE_TYPE_DEM);
			}
			return flag;
		}
		/**
		 * 更新子表行项目数据数据
		 * @param instanceId 实例ID
		 * @param hashdata 更新数据
		 * @param dataid 行标识ID
		 * @param modelId 模型ID,数据维护是传入demId  流程表单模型则传入 proDefId
		 * @param islong 是否记录日志
		 * @return
		 */
		public boolean updateSubFormData(Long demId,String subformkey ,Long instanceId, HashMap hashdata,boolean islog) {
			boolean flag = false;
			Long formId = this.getFormIdByInstanceId(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM); 
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			
			if (sysEngineSubformDAO == null)
				sysEngineSubformDAO = (SysEngineSubformDAO) SpringBeanUtil
						.getInstance().getBean("sysEngineSubformDAO");
				if (iformService != null && sysEngineSubformDAO != null) {
					List<SysEngineSubform> temp = sysEngineSubformDAO.getSubFormListforFormKey(formId,subformkey);
					if (temp != null && temp.size() > 0) {
						Long dataid = new Long(0) ;
						//获取DATAID 
						if(hashdata.get("ID")!=null){
							dataid = Long.parseLong(hashdata.get("ID").toString());
						}
						SysEngineSubform model = temp.get(0);
						if(model!=null){ 
							Long subFormId = model.getSubformid();
							flag = iformService.updateForm(demId,subFormId,instanceId, hashdata, dataid,islog,EngineConstants.SYS_INSTANCE_TYPE_DEM);
						}
					}
			}
			return flag;
		}
		
		/**
		 * 删除实例数据
		 * @param demUUID
		 * @param instanceId
		 * @return
		 */
		public boolean removeFormData(Long instanceId){
			boolean flag = false;
			Long formId = this.getFormIdByInstanceId(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM); 
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			if (iformService != null) {
				iformService.removeFormData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
				flag = true;
			}
			return flag;
		}
		
		/**
		 * 删除实例数据
		 * @param demUUID
		 * @param instanceId
		 * @return
		 */
		public boolean removeSubFormData(String demUUID,Long instanceid,Long dataid,String subformkey){
			boolean flag = false;
			Long demId = null;
			if(sysDemEngineDAO==null){
				 sysDemEngineDAO = (SysDemEngineDAO)SpringBeanUtil.getBean("sysDemEngineDAO");
			 }
			SysDemEngine sde = sysDemEngineDAO.getModel(demUUID);
			 if(sde!=null){
				 demId = sde.getId();
				 this.removeSubFormData(demId, instanceid, dataid, subformkey);
			 }
			return flag;
		}
		/**
		 * 
		 * @param instanceid
		 * @param subformkey
		 * @param dataid
		 * @return
		 */
		public HashMap getSubFormDataItem(Long instanceid,String subformkey,Long dataid){
			boolean flag = false;
			HashMap hashData = null;
			Long formId = this.getFormIdByInstanceId(instanceid,EngineConstants.SYS_INSTANCE_TYPE_DEM); 
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			
			if (sysEngineSubformDAO == null)
				sysEngineSubformDAO = (SysEngineSubformDAO) SpringBeanUtil
				.getInstance().getBean("sysEngineSubformDAO");
			if (iformService != null && sysEngineSubformDAO != null) {
				List<SysEngineSubform> temp = sysEngineSubformDAO.getSubFormListforFormKey(formId,subformkey);
				if (temp != null && temp.size() > 0) {
					SysEngineSubform model = temp.get(0);
					if(model!=null&&dataid!=null){ 
						Long subFormId = model.getSubformid();
						hashData = iformService.getGridDataItem(subFormId, instanceid, dataid);
					}
				}
			}
			return hashData;
		}
		/**
		 * 删除实例数据
		 * @param demUUID
		 * @param instanceId
		 * @return
		 */
		public boolean removeSubFormData(Long demId,Long instanceid,Long dataid,String subformkey){
			boolean flag = false;
			Long formId = this.getFormIdByInstanceId(instanceid,EngineConstants.SYS_INSTANCE_TYPE_DEM); 
			if (iformService == null)
				iformService = (IFormService) SpringBeanUtil.getBean("iformService");
			
			if (sysEngineSubformDAO == null)
				sysEngineSubformDAO = (SysEngineSubformDAO) SpringBeanUtil
				.getInstance().getBean("sysEngineSubformDAO");
			if (iformService != null && sysEngineSubformDAO != null) {
				List<SysEngineSubform> temp = sysEngineSubformDAO.getSubFormListforFormKey(formId,subformkey);
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
		/**
		 * 获得子表数据列表
		 * @param instanceId
		 * @param subformkey
		 * @return
		 */
		public List<HashMap> getFromSubData(Long instanceId, String subformkey) {
			return this.getFromSubData(instanceId, subformkey, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			
		}
		
}
