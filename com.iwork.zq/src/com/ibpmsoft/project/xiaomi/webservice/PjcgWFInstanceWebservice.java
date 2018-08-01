package com.ibpmsoft.project.xiaomi.webservice;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.ibpmsoft.project.xiaomi.model.BjdFjModel;
import com.ibpmsoft.project.xiaomi.util.SAPUserUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataDAO;
import com.iwork.core.engine.metadata.model.SysEngineMetadata;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.upload.dao.FileUploadDAO;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.tools.processopinion.service.ProcessOpinionService;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;
/**
 * 有三个流程，手机下手机的采购，手机给配件下采购，配件下配件的采购
 * 其中，手机下的两个都有报价单号，但是配件给配件下的是没有的，备注里是其他信息
 * @author davidyang
 *
 */
	@WebService
	public class PjcgWFInstanceWebservice{ 
		private static Logger logger = Logger.getLogger(PjcgWFInstanceWebservice.class);

		private  HistoryService historyService;//历史服务	
		private RuntimeService runtimeService; //运行时服务
		private String sjProcessKey = "XMSJYJCGSPLC";  //手机部门发起的硬件原材料采购流程
		private String sjpjProcessKey = "SJYJPJCGLC";  //手机部门发起的配件流程
		private String pjProcessKey = "PJCGSQ";  //小米网发起的配件采购流程 
		  
		private String sjActDefId = "XMSJYJCGSPLC:1:56504";  //手机部门发起的硬件原材料采购流程
		private String sjpjActDefId = "SJYJPJCGLC:1:56904";  //手机部门发起的配件流程
		private String pjActDefId = "PJCGSQ:1:51204";  //小米网发起的配件采购流程
		 
		private String pjSubsheet = "SUBFORM_PJCGSQDMX";
		private String sjSubsheet = "SUBFORM_BJCGSQDXXM";
		private String actDefId ;
		private String subSheet ;
		private StringBuffer log;
		private List<BjdFjModel> pricePageNoList;
		private  FileUploadDAO fileUploadDao;
		@WebMethod
		public	String execute(String json) { 
			pricePageNoList = new ArrayList();  //报价单编号列表 
			boolean isCheckPricePage = false;   //判断是否检查报价单
			
			 log = new StringBuffer(); 
			JSONObject jsonObject=null;
			try{ 
			 jsonObject=JSONObject.fromObject(json);
			}catch(Exception e){
				log.append("JSON格式异常"); 
				return log.toString();
			}
			// 获取单据编号，判断是否此单号已经生成过
			String ddh = ""; 
			if(jsonObject.get("DDH")!=null){
				ddh = jsonObject.get("DDH").toString();
			}else{ 
				log.append("单据编号为空"); 
				return log.toString();	
			}
			//判断当前单据是否有正在执行的单据，如果有执行的单据，则不允许重复创建
			//=================================================================
			if(runtimeService==null){
				runtimeService = (RuntimeService) SpringBeanUtil.getBean("runtimeService");
			}
			
			try{
			List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().variableValueEquals("DDH", ddh).list();
			if(instanceList!=null&&instanceList.size()>0){ 
				List<HistoricTaskInstance> histTaskList = historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceList.get(0).getId()).list();
				boolean isHaving = true;
				for(HistoricTaskInstance task:histTaskList){
					if(task.getDeleteReason()!=null&&task.getDeleteReason().equals("deleted")){
						isHaving = false;
						break;
					}
				} 
				if(isHaving){
					log.append("当前单据已在BPM系统中进行流转，不允许重复创建").append("\\n");
					return log.toString();
				}
				
			}
			}catch(Exception e){
				
			}
			//=================================================================
			String ddlx = null;
			String sapno = null;
			ProcessAPI processAPI = ProcessAPI.getInstance();
			//创建流程 
			try { 
				Object mapObj = jsonObject.get("SQRZH");
				if (mapObj != null) {
					sapno = mapObj.toString();
				}
			} catch (Exception e) {
				log.append("签订日期获取失败").append("\\n");
				logger.error(e,e);
			}
			
			Long formId = null;
			String taskId = "";
			String excutionId = "";
			String userid = "";
			 UserContext usercontext = null; 
			//获取当前系统账号
			if(sapno!=null&&!"".equals(sapno)){
				SAPUserUtil sapUserUtil = new SAPUserUtil();
				userid = sapUserUtil.getCurrentUserId(sapno);
				usercontext = UserContextUtil.getInstance().getUserContext(userid);
			}else{
				log.append("申请人账号异常");
				return log.toString();
			}
			
			if(userid==null||userid.equals("")||usercontext==null){
				userid = SecurityUtil.supermanager;
				log.append("申请人账号为：").append(sapno).append(",当前系统未发现绑定的SAP账号");
				return log.toString();
			}
			//获取订单类型，根据不同的订单类型，生成不同流程
			try { 
				Object mapObj = jsonObject.get("DDLX");
				if (mapObj != null) {
					ddlx = mapObj.toString();
				}
				if(ddlx!=null){
					if(ddlx.equals("PJ")){
						//如果发起的流程为配件流程，则判断当前用户所在部门信息，如果是小米网，则发起
						String deptno = usercontext.get_deptModel().getDepartmentno();
						if(deptno.indexOf("02")==0){
							//通过流程模型key获取实际流程ID
							String tmpActDefId = processAPI.getProcessActDefId(pjProcessKey); 
							if(tmpActDefId!=null){
								pjActDefId = tmpActDefId;
							}
							actDefId = pjActDefId;
						}else if(deptno.indexOf("03")==0){
							//通过流程模型key获取实际流程ID
							String tmpActDefId = processAPI.getProcessActDefId(sjpjProcessKey); 
							if(tmpActDefId!=null){
								sjpjActDefId = tmpActDefId;
							}
							actDefId = sjpjActDefId; 
							//检查报价单
							isCheckPricePage = true;
						}else{
							log.append("您所在的部门无权限发起采购流程");
							return log.toString();
						}
						subSheet = pjSubsheet;
					}else{
						String tmpActDefId = processAPI.getProcessActDefId(sjProcessKey); 
						if(tmpActDefId!=null){
							sjActDefId = tmpActDefId;
						} 
						actDefId = sjActDefId;
						subSheet = sjSubsheet;
						//检查报价单 
						isCheckPricePage = true;
					}
				}
			} catch (Exception e) {
				logger.error(e,e);
			} 
			if(actDefId==null){
				return log.toString();
			} 
			UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
			if(uc==null){
				log.append("当前系统未找到为【").append(userid).append("】的系统账号");
				return log.toString();
			}
			HashMap hash = new HashMap(); 
//			6 	订单号	DDH	
			hash.put("DDH", ddh); 
			//申请人账号 
			hash.put("SQRZH",userid);
			hash.put("SQR",uc.get_userModel().getUsername());
			List<OrgDepartment> deptlist = uc.get_parentDeptList();
			String deptfullname = "";
			StringBuffer parentDeptstr = new StringBuffer();
			if(deptlist!=null){
				for(OrgDepartment dept:deptlist){
					parentDeptstr.append(dept.getDepartmentname());;
				}
			}
			if(parentDeptstr.toString().equals("")){
				deptfullname = uc.get_deptModel().getDepartmentname();
			}else{
				deptfullname = parentDeptstr.append("，").append(uc.get_deptModel().getDepartmentname()).toString();
			} 
			
			hash.put("SQRBMMC",deptfullname);
			hash.put("SQRBMID",uc.get_deptModel().getId());
			hash.put("SQRDH",uc.get_userModel().getMobile());
			//装载主表对象
			String jsonLog = this.getJsonHashMap(jsonObject, hash);
			if(jsonLog!=null){ 
				log.append(jsonLog);
			}
			//获取行项目数据信息 
			List list = null;
			JSONArray jsonArray  = jsonObject.getJSONArray("SUB_TABLE_DATA");
			 list = this.getJsonArrayList(jsonArray); 
			 //获取报价单编号列表
			List pricePageList =  this.getPricePageList(list);
			 //检查报价单信息
			String tmp = this.checkPricePage(pricePageList); 
			if(tmp!=null&&!"".equals(tmp)){ 
				log.append(tmp);
			}
			 
			if(log==null||log.toString().equals("")){
				 //装载附件
				String fjStr = buildFJStr(); 
				hash.put("FJ", fjStr); 
				formId = processAPI.getProcessDefaultFormId(actDefId); 
				Long instanceid = processAPI.newInstance(actDefId,formId, userid,ddh,hash); 
				Task task = processAPI.newTaskId(instanceid);
				if(task!=null){ 
					taskId = task.getId();
					excutionId = task.getExecutionId();
				}
				processAPI.saveFormData(actDefId, instanceid, hash, false);
				//初始化数据
				processAPI.saveFormDatas(actDefId,instanceid, subSheet, list,false);
				//判断当前SAP单据编号是否存在过
				String link = this.getFormPageLink(ddh, instanceid,ddlx);
				if(link!=null&&!link.equals("")){
					ProcessOpinionService processOpinionService = (ProcessOpinionService) SpringBeanUtil.getBean("processOpinionService");
					if(processOpinionService!=null){
						processOpinionService.sendAction("任务终止历史",actDefId, null, task.getTaskDefinitionKey(),instanceid,taskId,  Long.parseLong(task.getExecutionId()),link);
					}
				} 
				return "success";
			}else{
				return log.toString();
			}
			//给当前用户发送邮件
			
		}
		 
		/**
		 * 装载所有的附件列表，构建成一个新的附件列表字符串
		 * @return
		 */
		private String buildFJStr(){
			StringBuffer fjStr = new StringBuffer();
			for(BjdFjModel model:pricePageNoList){
				String str = model.getFjNo();
				String[] item = str.split(","); 
				int count=0;
				for(String tmp:item){
					if(tmp.trim().equals("")){
						continue;
					}
					//判断当前附件是否存在，如果存在，执行附件复制操作,并将新的附件id进行赋值
					FileUpload fileUpload = this.getFileUpload(tmp.trim());
					if(fileUpload!=null){
						count++;
						//=======修改文件名====
						String fileName = fileUpload.getFileSrcName().substring(0,fileUpload.getFileSrcName().lastIndexOf("."));
						String temp = "";
						if(pricePageNoList.size()>1){
							temp = model.getPricePageNo()+"_"+count; 
						}else{
							temp = model.getPricePageNo(); 
						}
						
						String newFileName = fileUpload.getFileSrcName().replace(fileName,temp);
						fileUpload.setFileSrcName(newFileName);  
						//===================
						FileUpload copyUploadFile = this.copyFileUpload(fileUpload);
						if(copyUploadFile!=null){
							fjStr.append(",").append(copyUploadFile.getFileId());  
						}
					}
				}
			}
			if(fjStr.length()>1){
				return fjStr.deleteCharAt(0).toString();
			}
			return fjStr.toString();
		}
		
		/**
		 * 获取附件模型对象
		 * @param id
		 * @return
		 */
		private FileUpload copyFileUpload(FileUpload fileUpload) {
			if(fileUploadDao==null){
				fileUploadDao = (FileUploadDAO)SpringBeanUtil.getBean("uploadifyDAO");
			}
			return fileUploadDao.copyUploadFile(fileUpload);
		}
		
		/**
		 * 获取附件模型对象
		 * @param id
		 * @return
		 */
		private FileUpload getFileUpload(Serializable id) {
			if(fileUploadDao==null){
				fileUploadDao = (FileUploadDAO)SpringBeanUtil.getBean("uploadifyDAO");
			}
			return fileUploadDao.getFileUpload(FileUpload.class, id);
		} 
		
		/**
		 * 获取SAP
		 * @param sapFromNo
		 * @return
		 */
		private String getFormPageLink(String sapFormNo,Long instanceId,String ddlx){
			StringBuffer html = new StringBuffer();
			String bdTableName = "";
			if(ddlx.equals("PJ")){
				bdTableName = "BD_CG_SQD";
			}else{
				bdTableName = "BD_CG_PJCGSQD";
			}
			Long metadataid = 15l;
			StringBuffer sql = new StringBuffer();
			sql.append("select bind.instanceId,bind.formid,BD.DDH,bind.metadataid from "+bdTableName+" BD,sys_engine_form_bind bind where BD.Id = bind.dataid and bind.instanceId<>? and BD.DDH = ? and metadataid= ? order by bd.id desc");
			Connection conn = null;
			PreparedStatement stat = null;
			ResultSet rest = null;
			try {
				 conn = DBUtil.open();
				 stat = conn.prepareStatement(sql.toString());
				 stat.setLong(1, instanceId);
				 stat.setString(2, sapFormNo);
				 stat.setLong(3, metadataid);
				 rest = stat.executeQuery();
				 int count = 0;
				while(rest.next()){
					if(count>3){
						break;
					}
					Long his_instanceId = rest.getLong("instanceId");
					String his_ddh = rest.getString("DDH");
					if(his_instanceId!=null&&his_ddh!=null){
						html.append(this.buildLink(his_instanceId, his_ddh));
					}
					count++;
				}
			} catch (Exception e) {
				logger.error(e,e);
			}finally{
				DBUtil.close(conn, stat, rest);
			}
			
			return html.toString();
		}
		/**
		 * 
		 * @param instanceId
		 * @param ddh
		 * @param formid
		 * @return
		 */
		private String buildLink(Long instanceId,String ddh){
			StringBuffer html = new StringBuffer();
			if(historyService==null){
				historyService = (HistoryService) SpringBeanUtil.getBean("historyService");
			}
			List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processDefinitionId(actDefId).processInstanceId(instanceId+"").list();
			String taskId = "";
			String executionId = "";
			String title = "";
			for(HistoricTaskInstance historicTask:list){
				taskId = historicTask.getId();
				executionId = historicTask.getExecutionId();
				title = historicTask.getDescription();
				break;
			}
			if(!taskId.equals("")&&!executionId.equals("")){
				html.append("<a target=\"_blank\" href=\"");
				html.append("loadProcessFormPage.action?actDefId=").append(actDefId).append("&instanceId=").append(instanceId).append("&excutionId=").append(executionId).append("&taskId=").append(taskId);
				html.append("\">").append(title).append("</a>").append("<br/>");
			}
			return html.toString();
		}
		
		/**
		 * 获得报价单列表(去除重复)
		 * @param list
		 * @return
		 */
		private List getPricePageList(List<HashMap> list){
			List<String> pricePageList = null;
			if(list!=null){
				pricePageList = new ArrayList();
				for(HashMap hash:list){
					
					if(hash.get("JE")!=null){
						String je = (String)hash.get("JE");
						if(je!=null){
							try{
							float num = Float.parseFloat(je);
							if(num==0){
								continue;
							}
							}catch(Exception e){
								continue;
							}
						}
					}
					if(hash.get("BZ")!=null){
						String formNo = (String)hash.get("BZ");
						if(formNo.trim().equals("")){ 
							String meomo = (String)hash.get("MS");
							log.append("描述为[").append(meomo).append("]").append("的行项目中未填写报价单号！");
							break;
						}else{ 
							if(!pricePageList.contains(formNo)){ 
								pricePageList.add(formNo);
							}
						}
					}else{
						log.append("报价单号不完整，请在行项目中填写报价单号");
					}
				}
				
			}else{
				log.append("未发现行项目信息");
			}
			return pricePageList;
		}
		
		/**
		 * 检查报价单是否存在
		 * @param pageNo
		 * @return
		 */
		private String checkPricePage(List<String> list){
			StringBuffer log = new StringBuffer();
			if(list!=null){
				for(String formNo:list){
						String fjNo = this.getPriceFileNo(formNo);
						if(!fjNo.equals("")){
							BjdFjModel model = new BjdFjModel();
							model.setPricePageNo(formNo);
							model.setFjNo(fjNo);
							pricePageNoList.add(model);
						}else{
							log.append("报价单号为[").append(formNo).append("]").append("在BPM系统中未找到！");
						}
				}
				
			}else{
				log.append("未发现行项目信息");
			}
			
			
			return log.toString();
		}
		
		/**
		 * 获取报价单附件编号
		 * @return
		 */
		private String getPriceFileNo(String formNo){
			StringBuffer fjNo = new StringBuffer();
			StringBuffer sql = new StringBuffer();
			sql.append("select BJDFJ from BD_CG_BJDSJWH where UPPER(BJDBH) = ?");
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rest = null;
			try {
				conn = DBUtil.open();
				 stmt = conn.prepareStatement(sql.toString());
				 stmt.setString(1, formNo.toUpperCase());
				 rest = stmt.executeQuery(sql.toString());
				while(rest.next()){
					String fj = rest.getString("BJDFJ");
					if(fj!=null){
						fjNo.append(fj);
					}
				}
			
			} catch (Exception e) {
				
			}finally{
				DBUtil.close(conn, stmt, rest);
			}
			return fjNo.toString();
		}
		/**
		 * 根据存储名称获得数据库元数据ID
		 * @param bdTableName
		 * @return
		 */
		private Long getMetaDataId(String bdTableName){
			SysEngineMetadataDAO sysEngineMetadataDAO = (SysEngineMetadataDAO) SpringBeanUtil.getBean("sysEngineMetadataDAO");
			SysEngineMetadata metadata = sysEngineMetadataDAO.getModel(bdTableName);
			if(metadata!=null){
				return metadata.getId();
			}else{
				return null; 
			}
		}
		
		/**
		 * 装载主表集合
		 * @param jsonObject
		 * @param hash
		 * @return
		 */
		private String getJsonHashMap(JSONObject jsonObject,HashMap hash) { 
			StringBuffer log = new StringBuffer();
//			7 	签订日期
			try { 
				Object mapObj = jsonObject.get("QDRQ");
				if (mapObj != null) {
					hash.put("QDRQ", mapObj); 
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			9 	卖方	MF	 
			try {
				Object mapObj = jsonObject.get("MF");
				if (mapObj != null) {
					hash.put("MF", mapObj);
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			10 	卖方联系人	MFLXR	
			try {
				Object mapObj = jsonObject.get("MFLXR");
				if (mapObj != null) {
					hash.put("MFLXR", mapObj);
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			11 	卖方联系电话	MFLXDH		
			try {
				Object mapObj = jsonObject.get("MFLXDH");
				if (mapObj != null) {
					hash.put("MFLXDH", mapObj);
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			12 	卖方传真	MFCZ		
			try {
				Object mapObj = jsonObject.get("MFCZ");
				if (mapObj != null) {
					hash.put("MFCZ", mapObj);
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			13 	卖方地址	MFDZ		
			try {
				Object mapObj = jsonObject.get("MFDZ");
				if (mapObj != null) {
					hash.put("MFDZ", mapObj);
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			14 	买方	BUYERNAME	
			try {
				Object mapObj = jsonObject.get("BUYERNAME");
				if (mapObj != null) {
					hash.put("BUYERNAME", mapObj);
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			15 	买房联系人	BUYER			
			try {
				Object mapObj = jsonObject.get("BUYER");
				if (mapObj != null) {
					hash.put("BUYER", mapObj);
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			16 	买房联系电话	BUYERPHONE	
			try {
				Object mapObj = jsonObject.get("BUYERPHONE");
				if (mapObj != null) {
					hash.put("BUYERPHONE", mapObj);
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			17 	买方传真	BUYERFAX		
			try {
				Object mapObj = jsonObject.get("BUYERFAX");
				if (mapObj != null) {
					hash.put("BUYERFAX", mapObj);
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			18 	买方地址	BUYERADD	
			try {
				Object mapObj = jsonObject.get("BUYERADD");
				if (mapObj != null) {
					hash.put("BUYERADD", mapObj);
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			19 	合计总金额	HJZJE		
			try {
				Object mapObj = jsonObject.get("HJZJE");
				if (mapObj != null) {
					hash.put("HJZJE", mapObj); 
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			20 	合计数量	HJSL	
			try {
				Object mapObj = jsonObject.get("HJSL");
				if (mapObj != null) {
					hash.put("HJSL", mapObj); 
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			21 	交货地点	JHDD	
			try {
				Object mapObj = jsonObject.get("JHDD");
				if (mapObj != null) {
					hash.put("JHDD", mapObj); 
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			22 	付款时间及方式	FKSJJFS		
			try {
				Object mapObj = jsonObject.get("FKSJJFS");
				if (mapObj != null) {
					hash.put("FKSJJFS", mapObj); 
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			23 	最迟装运日期	ZCZYRQ
			try {
				Object mapObj = jsonObject.get("ZCZYRQ");
				if (mapObj != null) {
					hash.put("ZCZYRQ", mapObj); 
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			24 	结算方式	JSFS
			try {
				Object mapObj = jsonObject.get("JSFS");
				if (mapObj != null) {
					hash.put("JSFS", mapObj); 
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			25 	结算账号	JSZH			
			try {
				Object mapObj = jsonObject.get("JSZH");
				if (mapObj != null) {
					hash.put("JSZH", mapObj); 
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			26 	开户名称	KHMC			
			hash.put("KHMC", "小米科技");
			try {
				Object mapObj = jsonObject.get("KHMC");
				if (mapObj != null) {
					hash.put("KHMC", mapObj); 
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
//			27 	开户银行	KHYX
			//hash.put("KHYX", "工商银行海淀区上地支行 ");
			try {
				Object mapObj = jsonObject.get("KHYX");
				if (mapObj != null) {
					hash.put("KHYX", mapObj); 
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
			try {
				Object mapObj = jsonObject.get("JSBZ");
				if (mapObj != null) {
					hash.put("JSBZ", mapObj); 
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
			return null;
		}       
		/**                                                                                                 
		 * 获取集合                                                                                             
		 *                                                                                                  
		 * @param jsonArray                                                                                 
		 * @return                                                                                          
		 */                                                                                                 
		private List getJsonArrayList(JSONArray jsonArray) {                       
			List list = null;                                                                                  
			if (jsonArray != null) {                                                                          
				list = new ArrayList();                                                                               
				Iterator iterator = jsonArray.listIterator();                                                   
				while (iterator.hasNext()) {                                                                    
					JSONObject jsonObject = (JSONObject) iterator.next();                                         
					if (jsonObject != null) {                                                                     
						HashMap subData = new HashMap();                                                           
						//项目	
						try {
							Object mapObj = jsonObject.get("XM");
							if (mapObj != null) {
								subData.put("XM", mapObj);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}
//						subData.put("XM","红米手机");
//						小米料号
						try {
							Object mapObj = jsonObject.get("XMLH");
							if (mapObj != null) {
								subData.put("XMLH", mapObj);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}
//						subData.put("XMLH","xm20130805001"); 
//						供应商料号	 
						try {
							Object mapObj = jsonObject.get("GYSLH");
							if (mapObj != null) {
								subData.put("GYSLH", mapObj);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}
//						subData.put("GYSLH","gys20120814");
//						描述	
						try {
							Object mapObj = jsonObject.get("MS");
							if (mapObj != null) {
								subData.put("MS", mapObj);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}
//						subData.put("MS","4.7寸液晶屏");
//						供应商
						try {
							Object mapObj = jsonObject.get("GYS");
							if (mapObj != null) {
								subData.put("GYS", mapObj);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}
//						subData.put("GYS","夏普");
//						数量	
						try {
							Object mapObj = jsonObject.get("SL");
							if (mapObj != null) {
								subData.put("SL", mapObj);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}
//						subData.put("SL","500");
//						交货日期	
						try {
							Object mapObj = jsonObject.get("JHRQ");
							if (mapObj != null) {
								subData.put("JHRQ", mapObj);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}
//						subData.put("JHRQ","2013-10-01");
//						备注	
						try {
							Object mapObj = jsonObject.get("BZ");
							if (mapObj != null) {
								subData.put("BZ", mapObj);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}
//						subData.put("BZ","");
//						单价	
						try {
							Object mapObj = jsonObject.get("DJ");
							if (mapObj != null) {
								subData.put("DJ", mapObj);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}
//						subData.put("DJ","200");
//						金额	
						try {
							Object mapObj = jsonObject.get("JE");
							if (mapObj != null) {
								subData.put("JE", mapObj);
							}
						} catch (Exception e) {
						logger.error(e,e);
						}
//						金额	
						try {
							Object mapObj = jsonObject.get("JSBZ");
							if (mapObj != null) {
								subData.put("JSBZ", mapObj);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}
//						subData.put("JE","100000");
						list.add(subData);                                                                                  
					}                                                                                             
				}                                                                                               
			}                                                                                                 
			return list;                                                                                         
		}                                                                                                   
	}


