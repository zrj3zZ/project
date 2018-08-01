package com.iwork.core.engine.iform.service;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.TaskService;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.iwork.commons.util.JsonUtil;
import com.iwork.commons.util.ObjectUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.commons.util.UUIDUtil;
import com.iwork.commons.util.UtilCode;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.constant.IFormStatusConstants;
import com.iwork.core.engine.dem.service.SysDemTriggerService;
import com.iwork.core.engine.iform.dao.IFormDataDAO;
import com.iwork.core.engine.iform.dao.SysEngineFormBindDAO;
import com.iwork.core.engine.iform.dao.SysEngineIFormDAO;
import com.iwork.core.engine.iform.dao.SysEngineIFormMapDAO;
import com.iwork.core.engine.iform.dao.SysEngineSubformDAO;
import com.iwork.core.engine.iform.dao.SysInstanceDataDAO;
import com.iwork.core.engine.iform.model.SysEngineFormBind;
import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.core.engine.iform.model.SysEngineIformMap;
import com.iwork.core.engine.iform.model.SysEngineSubform;
import com.iwork.core.engine.iform.model.SysInstanceData;
import com.iwork.core.engine.iform.model.SysInstanceDataId;
import com.iwork.core.engine.iform.util.MobilePageUtil;
import com.iwork.core.engine.log.service.SysIformActionLogService;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataDAO;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataMapDAO;
import com.iwork.core.engine.metadata.model.ConditionModel;
import com.iwork.core.engine.metadata.model.SysEngineMetadata;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;
import com.iwork.core.engine.plugs.component.IFormUIComponentAbst;
import com.iwork.core.engine.plugs.component.IFormUIComponentHiddenImpl;
import com.iwork.core.engine.plugs.component.IFormUIComponentProcessStepSetHiddenImpl;
import com.iwork.core.engine.plugs.component.IFormUIFactory;
import com.iwork.core.engine.plugs.component.UIComponentInterface;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.engine.runtime.tools.RuntimeELUtil;
import com.iwork.core.engine.util.WrapScriptUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.upload.util.RichTextUtil;
import com.iwork.core.util.SequenceUtil;
import com.iwork.plugs.dictionary.model.SysDictionaryBaseinfo;
import com.iwork.plugs.dictionary.service.SysDictionaryRuntimeService;
import com.iwork.process.definition.step.model.ProcessStepFormMap;
import com.iwork.process.runtime.constant.ProcessTaskConstant;

import freemarker.core.TemplateElement;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author YangDayong
 *
 */
public class IFormService{
	private static Logger logger = Logger.getLogger(IFormService.class);
	private SysEngineMetadataDAO sysEngineMetadataDAO; //存储模型
	private SysEngineMetadataMapDAO sysEngineMetadataMapDAO;//存储字段模型
	private SysEngineIFormDAO sysEngineIFormDAO;//表单模型获取DAO
	private SysEngineIFormMapDAO sysEngineIFormMapDAO;//表单域模型
	private IFormDataDAO iformDataDAO;    //表单数据获取DAO
	private SysEngineSubformDAO sysEngineSubformDAO;  //子表模型
	private SysDemTriggerService sysDemTriggerService;  //数据维护模型触发器
	private SysIformActionLogService sysIformActionLogService;  //表单动作日志记录操作
	private SysDictionaryRuntimeService sysDictionaryRuntimeService;  
	
	private SysEngineFormBindDAO iformBindDAO;   //
	private SysInstanceDataDAO instanceDataDAO;   //表单实例模型
	
	private TaskService taskService;//任务服务	
	
	private FreeMarkerConfigurer freemarderConfig;
	private RichTextUtil richTextUtil;
	private FreeMarkerConfigurer mobilefreemarderConfig;
	private  List<SysEngineIformMap> verifySuccessInfo = new ArrayList();   //绑定成功列表
	private  List verifyErrorInfo = new ArrayList();   //绑定异常功能列表
	private  List iformMapList = new ArrayList();   //表单域列表
	private  HashMap<String,String> scriptMap = new HashMap<String,String>();//用于存储已经输出到表单的script,避免重复输出
	
	//************************************以下属于为子表SCRIPT涉及的变量************************
	private String lastsel;
	private String newId;
	private String subformId;
	private String subformKey;
	private boolean subformIsHidden; //判断子表是否隐藏
	private String subformNavGrid;
	private String subformSaveArray;
	private String newRowFunctionname;
	private String newRowDefaultValue;
	private String copyRowFunctionname;
	private String insertFunctionname;
	private String selectRowsFunctionname;
	private String saveSubReportDataFunctionname;
	private String saveSubReportGridFunctionname;
	private String deleteRowsFunctionname;
	private String deleteRowDataFunctionname;
	private String subformColLenValidateFunName;
	private String subformColLenValidateMsg;
	private String subformColLenValidateIndex;
	private String subformInitVariableFunName;
	private String subformResetUrlFunName;
	private String subformOpenFormFunName;
	
	private String subformInputEventOnChangeFunName;
	
	private final String NEWLINE_SYMBOL="<BR>";//提示信息换行符
	//************************************以上属于为子表SCRIPT涉及的变量************************
	/**
	 * 初始化实例ID
	 * @param type   0:表单  1：存储
	 * @param formid 
	 */
	public Long initInstanceId(Long formid,Long type){
		//
//		List<SysInstanceData> list = instanceDataDAO.getList();
//		for(SysInstanceData model:list){
//			List<SysEngineFormBind> bindlist = iformBindDAO.getListOfInstancId(model.getId().getId());
//			for(SysEngineFormBind bind:bindlist){
//				bind.setEngineType(model.getId().getType());
//				iformBindDAO.update(bind);
//			}
//		}
		Long instanceid = instanceDataDAO.getMaxID(type);
		//Long instanceid =  SequenceUtil.getInstance().getSequenceIndexForLong(SequenceUtil.DEM_INSTANCE_ID_KEY);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		SysInstanceData model = new SysInstanceData();
		model.setCreatedate(new Date());
		model.setCreateuser(uc.get_userModel().getUserid());
		model.setUpdatedate(new Date());  
		model.setUpdateuser(uc.get_userModel().getUserid());
		model.setFormid(formid);
		SysInstanceDataId sysInstanceDataId = new SysInstanceDataId();
		sysInstanceDataId.setType(type);
		sysInstanceDataId.setId(instanceid);
		model.setId(sysInstanceDataId);
		instanceid = instanceDataDAO.create(model);
		return instanceid;
	}
	
	/**
	 *  * 初始化实例ID【已知实例ID】
	 * @param formid
	 * @param instanceId  
	 * @param engienType
	 * @return
	 */
	public Long initInstanceId(Long formid,Long instanceId,Long engienType){
		return this.initInstanceId(formid, instanceId, engienType,null);
	}
	/**
	 *  * 初始化实例ID【已知实例ID】
	 * @param formid
	 * @param instanceId  
	 * @param engienType
	 * @return
	 */
	public Long initInstanceId(Long formid,Long instanceId,Long engienType,String userId){
		
		UserContext uc = null;
		if(userId!=null){
			uc = UserContextUtil.getInstance().getUserContext(userId);
		}else{
			uc = UserContextUtil.getInstance().getCurrentUserContext();
		}
		SysInstanceData model = new SysInstanceData();
		SysInstanceDataId id = new SysInstanceDataId();
		id.setId(instanceId);
		id.setType(engienType);
		model.setCreatedate(new Date());
		model.setCreateuser(uc.get_userModel().getUserid());
		model.setUpdatedate(new Date());
		model.setUpdateuser(uc.get_userModel().getUserid());
		model.setFormid(formid);
		model.setId(id);
		Long instanceid = instanceDataDAO.create(model);
		return instanceId;
	}
	/**
	 * 表单保存
	 * @param modelId
	 * @param formid
	 * @param instanceid
	 * @param map
	 * @param isLog
	 * @param engineType
	 * @return
	 */
	public Long saveForm(Long modelId,Long formid ,Long instanceid,Map map,boolean isLog,Long engineType){
		
		return this.saveForm(null, modelId, formid, instanceid, map, isLog, engineType);
	}
	
	
	
	/**
	 * 数据同步用的API
	 * @param metadataModel
	 * @param data
	 * @param checkFieldList   是否检查重复行数据，通过ID进行检查，重复数据执行更新操作。
	 * @return
	 */
	public Long syncSaveForm(Connection conn,SysEngineMetadata metadataModel,HashMap data,List<ConditionModel> checkFieldList){
		boolean isTranstion = true;
		if(conn==null){
			conn = DBUtil.open();
			isTranstion = false;
		}
		String oprationType = "ADD"; 
		int bdid = 0;
		StringBuffer condition = null;
			if(checkFieldList!=null&&checkFieldList.size()>0){
				 condition = new StringBuffer();
				for(ConditionModel model:checkFieldList){
					//默认为等于
					if(model.getFieldExpression()==null){
						model.setFieldExpression("=");
					}
					SysEngineMetadataMap semm = sysEngineMetadataMapDAO.getModel(metadataModel.getId(), model.getFieldName());
					if(semm==null){
						continue;
					}
					String val = "";
					if(model.getFieldData()==null){
						 val = ObjectUtil.getString(data.get(model.getFieldName()));
					}else{
						 val = model.getFieldData();
					}
					
					String fieldType = semm.getFieldtype();
					//如果为数值型
					if(fieldType.equals(EngineConstants.MAP_TYPE_NUMBER)||fieldType.equals(EngineConstants.MAP_TYPE_LONG)){
						if(val.equals(""))val=null;else val = val.trim();
						if(val!=null){
							try{
								Long.parseLong(val);
							}catch(Exception e){
								try{
									val = Double.parseDouble(val)+"";
								}catch(Exception se){
									val = null;
								}
							}
						}
						condition.append(model.getFieldName()).append(model.getFieldExpression()).append(val).append(" and ");
					}else if(fieldType.equals(EngineConstants.MAP_TYPE_TEXT)||fieldType.equals(EngineConstants.MAP_TYPE_MEMO)){
						condition.append(model.getFieldName()).append(model.getFieldExpression()).append("'").append(val).append("'  and ");
					}else if(fieldType.equals(EngineConstants.MAP_TYPE_DATE)){
						condition.append(model.getFieldName()).append(model.getFieldExpression()).append(DBUtil.convertShortDate(val)).append("  and ");
					}else if(fieldType.equals(EngineConstants.MAP_TYPE_DATETIME)){
						condition.append(model.getFieldName()).append(model.getFieldExpression()).append(DBUtil.convertLongDate(val)).append(" and ");
					}else{
						condition.append(model.getFieldName()).append(model.getFieldExpression()).append("'").append(val).append("' and ");
					}
				}
				if(!condition.equals("")){
					StringBuffer sql = new StringBuffer();
					sql.append("select count(id) num from ").append(metadataModel.getEntityname()).append(" where ").append(condition).append(" 1=1");
					int num = DBUtil.getInt(sql.toString(),"num");
					if(num>0){
						 oprationType = "UPDATE";
					}else{
						bdid =SequenceUtil.getInstance().getSequenceIndex(conn,metadataModel.getEntityname());
					}
				}
			}else{
				try{
					bdid = Integer.parseInt(data.get("ID").toString());
				}catch(Exception e){
					bdid =SequenceUtil.getInstance().getSequenceIndex(conn,metadataModel.getEntityname());
				}
			}
		
		List<SysEngineMetadataMap> metadataMapList = sysEngineMetadataMapDAO.getDataList(metadataModel.getId());
		//判断是否有instanceId字段
			boolean isInstanceId = false;
			StringBuffer sql = new StringBuffer();
			StringBuffer fields = new StringBuffer();
			StringBuffer values = new StringBuffer();
			 String val = "";
			if(oprationType.equals("ADD")){
				sql.append("INSERT INTO ").append(metadataModel.getEntityname()).append(" (");
				for(SysEngineMetadataMap map:metadataMapList){ 
					 if(map==null){
					    	continue;
					    }
					String key = map.getFieldname();
				    Object obj = data.get(key);
				    if(obj!=null){
				    	if(obj instanceof String[]){
				    		StringBuffer str = new StringBuffer();
					    	try{
					    		String[] list = (String[])obj;
								if(list.length==1){
									val = list[0]; 
								}else{
										for(String tmp:list){
											if(tmp.trim().equals(""))continue;
											str.append(tmp).append(",");
										val = str.substring(0,str.length() - 1);
										}
								}
					    	}catch(Exception e){
					    		val = (String)obj.toString();
					    	}
				    	}else if(obj instanceof Date){ 
							if(obj!=null){
								Calendar c = Calendar.getInstance();
								c.setTime((Date)obj);
								int hour = c.get(Calendar.HOUR);
								int minute = c.get(Calendar.MINUTE);
								int ss = c.get(Calendar.SECOND);
								if(hour==0&&minute==0&&ss==0){
									val = UtilDate.dateFormat((Date)obj);
								}else{
									val = UtilDate.datetimeFormat((Date)obj);
								}
							}
				    		
				    	}else{
							val = obj.toString();
						}
					    String fieldType = map.getFieldtype();
							//如果为数值型
						    if(fieldType.equals(EngineConstants.MAP_TYPE_NUMBER)||fieldType.equals(EngineConstants.MAP_TYPE_LONG)){
						    	if(key.equals("INSTANCEID")){
								    	val = "-1";
								}else{
									if(val.equals(""))val=null;
							    	if(val!=null){
							    		try{
											Long.parseLong(val);
										}catch(Exception e){
											try{
												val = Double.parseDouble(val)+"";
											}catch(Exception se){
												val = null;
											}
										}
							    	}
								}
						    	values.append(val).append(","); 
						    }else if(fieldType.equals(EngineConstants.MAP_TYPE_TEXT)||fieldType.equals(EngineConstants.MAP_TYPE_MEMO)){
						         //防止SQL注入
						    	  val = val.replace("'", "''");
						    	values.append("'").append(val).append("',");
						    }else if(fieldType.equals(EngineConstants.MAP_TYPE_DATE)){
						    	values.append(DBUtil.convertShortDate(val)).append(","); 
						    }else if(fieldType.equals(EngineConstants.MAP_TYPE_DATETIME)){
						    	values.append(DBUtil.convertLongDate(val)).append(","); 
						    }
				    }
				    fields.append(key).append(",");
				}
				//补充数据插入
				fields.append("ID");
				values.append(bdid);
				sql.append(fields).append(") VALUES ").append("(").append(values).append(") ");
			}else{
				sql.append("UPDATE ").append(metadataModel.getEntityname()).append(" SET ");
				for(SysEngineMetadataMap map:metadataMapList){ 
					 if(map==null){
					    	continue;
					    }
					String key = map.getFieldname();
				    Object obj = data.get(key);
						if(obj instanceof String[]){
							StringBuffer str = new StringBuffer();
							String[] list = (String[])obj;
							if(list.length==1){
								val = list[0];  
							}else{
								//如果类型为复选框，则
								for(String tmp:list){
									if(tmp.trim().equals(""))continue;
									str.append(tmp).append(",");
									val = str.substring(0,str.length() - 1);
								}
							}
							//防止SQL注入
							val = val.replace("'", "''");
						}else if(obj instanceof Date){ 
							if(obj!=null){
								Calendar c = Calendar.getInstance();
								c.setTime((Date)obj);
								int hour = c.get(Calendar.HOUR);
								int minute = c.get(Calendar.MINUTE);
								int ss = c.get(Calendar.SECOND);
								if(hour==0&&minute==0&&ss==0){
									val = UtilDate.dateFormat((Date)obj);
								}else{
									val = UtilDate.datetimeFormat((Date)obj);
								}
							}
				    		
				    	}else if(obj instanceof String){
							val = obj.toString();
							//防止SQL注入
							val = val.replace("'", "''");
						}else{
							val = obj.toString();
							//防止SQL注入
							val = val.replace("'", "''");
						}
					
					String fieldType = map.getFieldtype();
					//如果为数值型
					if(fieldType.equals(EngineConstants.MAP_TYPE_NUMBER)||fieldType.equals(EngineConstants.MAP_TYPE_LONG)){
						if(val.equals(""))val=null;else val = val.trim();
						if(val!=null){
							try{
								Long.parseLong(val);
							}catch(Exception e){
								try{
									val = Double.parseDouble(val)+"";
								}catch(Exception se){
									val = null;
								}
							}
						}
						sql.append(key).append("=").append(val).append(",");
					}else if(fieldType.equals(EngineConstants.MAP_TYPE_TEXT)||fieldType.equals(EngineConstants.MAP_TYPE_MEMO)){
						sql.append(key).append("='").append(val).append("',");
					}else if(fieldType.equals(EngineConstants.MAP_TYPE_DATE)){
						sql.append(key).append("=").append(DBUtil.convertShortDate(val)).append(",");
					}else if(fieldType.equals(EngineConstants.MAP_TYPE_DATETIME)){
						sql.append(key).append("=").append(DBUtil.convertLongDate(val)).append(",");
					}else{
						sql.append(key).append("='").append(val).append("',");
					}
				}
				//补充数据插入
				if(condition!=null){
					sql.append(" ID ").append("=").append(bdid);
					sql.append(" WHERE ").append(condition).append(" 1=1 ");
				}
			}
			if(!sql.toString().equals("")){
				int i = DBUtil.executeUpdate(conn, sql.toString());
				if(!isTranstion){
					DBUtil.close(conn, null, null);
				}
				if(i>0){
					return new Long(bdid);
				}
			}
			
		return null;
		
	}
	
	
	/**
	 * 支持事务管理
	 * @param conn 链接
	 * @param modelId
	 * @param formid
	 * @param instanceid
	 * @param map
	 * @param isLog
	 * @param engineType
	 * @return
	 */
	public Long saveForm(Connection conn,Long modelId,Long formid ,Long instanceid,Map map,boolean isLog,Long engineType){
		int id =0;
		try
		{
		String  log = "";
		//获取表单MAP列表
		List<SysEngineIformMap> maplist = sysEngineIFormMapDAO.getDataList(formid);
		SysEngineIform iformModel = sysEngineIFormDAO.getSysEngineIformModel(formid);
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(iformModel.getMetadataid());
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(uc==null){
			uc = UserContextUtil.getInstance().getUserContext("ADMIN");
		}
		//加工页面参数列表
		//进行安全校验，防止全部数据清空
		List datalist = new ArrayList();
		HashMap params = new HashMap();
		HashMap m = ParameterMapUtil.getParameterMap(map);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0;i<maplist.size();i++){
			SysEngineIformMap model = maplist.get(i);
			if(model!=null){
				if(model.getMapType()!=null&&model.getMapType().equals(new Long(0))){
					continue;
				}
				Object obj = map.get(model.getFieldName());
				if(obj!=null){
					if(!obj.equals("")){
						if(!model.getFieldName().equals("INSTANCEID")&&!model.getFieldName().equals("ID")){
							datalist.add(obj);
						}
					}
					SysEngineMetadataMap metadataMapModel = sysEngineMetadataMapDAO.getModel(iformModel.getMetadataid(), model.getFieldName());
				/*	if(model.getFieldName().equals("NOTICEDATE") || model.getFieldName().equals("COMPANYNO")){
						String a="";
						String b=a;
						System.out.println(model.getFieldName()+"------------"+m.get(model.getFieldName()));
					}*/
					if(metadataMapModel.getFieldtype().equals("日期")||metadataMapModel.getFieldtype().equals("日期时间")){						
						SimpleDateFormat sdf1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
						Long d1=0L;
						if(m.get(model.getFieldName())!=null && !"".equals(m.get(model.getFieldName()))){
							try {
								d1 = sdf.parse(m.get(model.getFieldName()).toString()).getTime();
							} catch (Exception e) {
								 try {
									Date date=sdf1.parse(m.get(model.getFieldName()).toString());
									// d1=Long.parseLong(sdf.format(date));
									 String a=sdf.format(date);
									 d1 = sdf.parse(a).getTime();
								} catch (Exception e1) {
									return new Long(0);
								}
							}	
						}
						if(m.get(model.getFieldName())==null || m.get(model.getFieldName()).toString().equals(""))
							params.put(model.getFieldName(), obj);
						else if(d1>sdf.parse("1900-01-01").getTime()&&d1<sdf.parse("3000-12-31").getTime()){
							params.put(model.getFieldName(), obj);							
						}else{
							logger.error("568行Security-002:系统可能出现安全风险问题,试图清空表单数据，防火墙予以拦截...");
							return new Long(0);
						}
				}	
				else {
					if(metadataMapModel.getFieldLength()==null||metadataMapModel.getFieldLength().equals(""))
						params.put(model.getFieldName(), obj);
					else if(metadataMapModel.getFieldLength().contains(","))
					{
						if(m.get(model.getFieldName()) == null || m.get(model.getFieldName()).equals("")) 
							params.put(model.getFieldName(), obj);
						else
						{
							int zsLength = Integer.parseInt(metadataMapModel.getFieldLength().substring(0, metadataMapModel.getFieldLength().indexOf(",")));
							String strXS = metadataMapModel.getFieldLength().substring(metadataMapModel.getFieldLength().indexOf(",")+1);
							if(strXS.equals("")) strXS="0";
							int xsLength = Integer.parseInt(strXS);
							zsLength = zsLength - xsLength;
							String str = m.get(model.getFieldName()).toString();
							 Pattern p = Pattern.compile("^-?\\d{1,"+zsLength+"}(\\.\\d{0,"+xsLength+"})?$");
							 if(!p.matcher(str).matches()){
								 logger.error("589行Security-002:系统可能出现安全风险问题,试图清空表单数据，防火墙予以拦截...");
									return new Long(0);
							 }
							 else
								 params.put(model.getFieldName(), obj);
						}
					}
					else if(!metadataMapModel.getFieldLength().contains(",") && Integer.parseInt(metadataMapModel.getFieldLength())<m.get(model.getFieldName()).toString().length())
					{							
						 logger.error("598行Security-002:系统可能出现安全风险问题,试图清空表单数据，防火墙予以拦截...");
							return new Long(0);
					}
					else
						params.put(model.getFieldName(), obj);
					}
			}
				
				//判断富文本内容
		    	if(model!=null&&model.getDisplayType()!=null&&(model.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_ADV_RICH_TEXT)||model.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_SINGLE_RICH_TEXT))){
		    		Object obj_show = map.get(model.getFieldName()+"_EDIT");
		    		params.put(model.getFieldName()+"_EDIT", obj_show);
				}
			}
		}
		if(datalist.size()==0){			
			logger.error("614行Security-002:系统可能出现安全风险问题,试图清空表单数据，防火墙予以拦截...");
			return new Long(0);
		}
		
		//设置INSTANCEID
		params.put("INSTANCEID", instanceid);
		params.put("FORMID", formid);
		id = SequenceUtil.getInstance().getSequenceIndex(conn,metadataModel.getEntityname());
		String   sql = buildAddSQL(iformModel,id, params);
		if(conn!=null){
			try {
				conn.setAutoCommit(false);
			} catch (Exception e) {
				logger.error(e,e);
			}
		}
		int i = iformDataDAO.create(conn,sql);
		if(i>0){
			SysEngineFormBind sysEngineFormBind = new SysEngineFormBind();
			sysEngineFormBind.setFormid(formid);
			sysEngineFormBind.setInstanceid(instanceid);
			sysEngineFormBind.setMetadataid(metadataModel.getId());
			sysEngineFormBind.setDataid(new Long(id));
			sysEngineFormBind.setEngineType(engineType);
			try {
				iformBindDAO.save(sysEngineFormBind);
				if(conn!=null){
					conn.commit();
				}
			} catch (Exception e) {
				//数据回滚，提示数据插入失败
				logger.error("[SysEngineFormBind]插入失败[INSTANCEID:"+instanceid+",METADATAID:"+metadataModel.getId()+"]");
				if(conn!=null){
					try {
						conn.rollback();
					} catch (Exception e1) {
						logger.error(e1);	
					}
				}
				logger.error(e,e);
				id = 0;  //行记录ID重置回0
			}
			if(isLog){ 
				//记录修改日志
				sysIformActionLogService.addFormAddLog(modelId,formid,instanceid,sysEngineFormBind.getDataid(), map); 
			}
			log = "保存成功";
		}else{
			id = 0;  //行记录ID重置回0
		}
		}catch(Exception ex)
		{
			//logger.error(ex);
			return new Long(0);
			
		}
		return new Long(id);
	}
	
	
	/**
	 * 
	 * @param modelId
	 * @param formid
	 * @param instanceid
	 * @param map
	 * @param dataid
	 * @param isLog
	 * @param engineType
	 * @return
	 */
	public boolean updateForm(Long modelId,Long formid ,Long instanceid,Map map,Long dataid,boolean isLog,Long engineType){

		boolean flag = false; 
		try
		{
		String returnmsg = ""; 
		String  log = "";
		if(dataid==null){
			return false;
		}
		//获取表单MAP列表
		List<SysEngineIformMap> maplist = sysEngineIFormMapDAO.getDataList(formid);
		SysEngineIform iformModel = sysEngineIFormDAO.getSysEngineIformModel(formid);
//		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(iformModel.getMetadataid());
		UserContext uc =UserContextUtil.getInstance().getCurrentUserContext();
		//防止由计划任务调用时，无法获取当前班里人
		if(uc==null){
			uc = UserContextUtil.getInstance().getUserContext("ADMIN");
		}
		//加工页面参数列表
		//进行安全校验，防止全部数据清空
		List datalist = new ArrayList();
		HashMap params = new HashMap();
		HashMap m = ParameterMapUtil.getParameterMap(map);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0;i<maplist.size();i++){
			SysEngineIformMap model = maplist.get(i);
			if(model!=null){
				Object obj = map.get(model.getFieldName());
				if(obj!=null){ 	
					if(!obj.equals("")){
						if(!model.getFieldName().equals("INSTANCEID")&&!model.getFieldName().equals("ID")){
							datalist.add(obj);
						}
					}
					SysEngineMetadataMap metadataMapModel = sysEngineMetadataMapDAO.getModel(iformModel.getMetadataid(), model.getFieldName());
					if(metadataMapModel.getFieldtype().equals("日期")||metadataMapModel.getFieldtype().equals("日期时间")){
						SimpleDateFormat sdf1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
						Long d1=0L;
						if(m.get(model.getFieldName())!=null && !"".equals(m.get(model.getFieldName()))){
							try {
								d1 = sdf.parse(m.get(model.getFieldName()).toString()).getTime();
							} catch (Exception e) {
								 try {
									 Date date=sdf1.parse(m.get(model.getFieldName()).toString());
										// d1=Long.parseLong(sdf.format(date));
										 String a=sdf.format(date);
										 d1 = sdf.parse(a).getTime();
								} catch (Exception e1) {
									
								}
							}	
						}
						if(m.get(model.getFieldName())==null || m.get(model.getFieldName()).toString().equals(""))
							params.put(model.getFieldName(), obj);
						else if(d1>sdf.parse("1900-01-01").getTime()
								&&d1<sdf.parse("3000-12-31").getTime())
						{
							params.put(model.getFieldName(), obj);							
						}
						else
						{
							logger.error("745行Security-002:系统可能出现安全风险问题,试图清空表单数据，防火墙予以拦截...");
							return flag;
						}
				}	
				else {
					if(metadataMapModel.getFieldLength()==null||metadataMapModel.getFieldLength().equals(""))
						params.put(model.getFieldName(), obj);
					else if(metadataMapModel.getFieldLength().contains(","))
					{
						if(m.get(model.getFieldName()) == null || m.get(model.getFieldName()).equals("")) 
							params.put(model.getFieldName(), obj);
						else
						{
							int zsLength = Integer.parseInt(metadataMapModel.getFieldLength().substring(0, metadataMapModel.getFieldLength().indexOf(",")));
							String strXS = metadataMapModel.getFieldLength().substring(metadataMapModel.getFieldLength().indexOf(",")+1);
							if(strXS.equals("")) strXS="0";
							int xsLength = Integer.parseInt(strXS);
							zsLength = zsLength - xsLength;
							String str = m.get(model.getFieldName()).toString();
							 Pattern p = Pattern.compile("^-?\\d{1,"+zsLength+"}(\\.\\d{0,"+xsLength+"})?$");
							 if(!p.matcher(str).matches()){
								 logger.error("766行Security-002:系统可能出现安全风险问题,试图清空表单数据，防火墙予以拦截...");
									return flag;
							 }
							 else
								 params.put(model.getFieldName(), obj);
						}
					}
					else if(!metadataMapModel.getFieldLength().contains(",") && Integer.parseInt(metadataMapModel.getFieldLength())<m.get(model.getFieldName()).toString().length())
					{							
						 logger.error("775行Security-002:系统可能出现安全风险问题,试图清空表单数据，防火墙予以拦截...");
							return flag;
					}
					else
						params.put(model.getFieldName(), obj);
					}
			}
			
			//判断富文本内容
	    	if(model!=null&&model.getDisplayType()!=null&&(model.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_ADV_RICH_TEXT)||model.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_SINGLE_RICH_TEXT))){
	    		Object obj_show = map.get(model.getFieldName()+"_EDIT");
	    		params.put(model.getFieldName()+"_EDIT", obj_show);
			}
			}
		}
		if(datalist.size()==0){
			logger.error("Security-001:系统可能出现安全风险问题,试图清空表单数据，防火墙予以拦截...");
			return false;
		}
		params.put("INSTANCEID", instanceid);
		params.put("FORMID", formid);
		if(engineType==null){
			logger.error("ERROR-IS-1001参数异常，请重新打开表单");
			return false;
		}
		
		//获得绑定关系模型
		SysEngineFormBind formBindModel = iformBindDAO.getModel(formid, iformModel.getMetadataid(), instanceid,dataid,engineType);
		if(formBindModel==null){
			if(formid!=null&&iformModel.getMetadataid()!=null&&instanceid !=null&&dataid.equals(new Long(0))){
				 dataid = this.saveForm(modelId, formid, instanceid, map, isLog, engineType);
				 flag = true;
			}else{
				logger.error("ERROR-IS-1002参数异常，请重新打开表单");
				return false;
			}
		}else{
			//判断是否记录变更日志
			if(isLog){
				//记录修改日志
				sysIformActionLogService.addFormUpdateLog(modelId,formBindModel, map); 
			}
			String   sql = "";
			if(dataid!=null&&!dataid.equals(new Long(0))){
				sql = buildUpdateSQL(iformModel,dataid, params);
			}
			int i = 0; //更新记录数
				i = iformDataDAO.update(sql);
			if(i>0){
				SysInstanceData instanceData = instanceDataDAO.getModel(instanceid,engineType); 
				if(instanceData!=null){
					instanceData.setUpdatedate(new Date());
					
					instanceData.setUpdateuser(uc.get_userModel().getUserid());
					instanceDataDAO.update(instanceData);
				} 
				flag = true;
			} 
		}
		
//		
//		//获得绑定关系模型
//		SysEngineFormBind formBindModel = iformBindDAO.getModel(formid, iformModel.getMetadataid(), instanceid,dataid,engineType);
//		if(formBindModel==null){
//			this.log.error("ERROR-IS-1002参数异常，请重新打开表单");
//			return false;
//		}
//		//判断是否记录变更日志
//		if(isLog){
//			//记录修改日志
//			sysIformActionLogService.addFormUpdateLog(modelId,formBindModel, map); 
//		}
//		String   sql = buildUpdateSQL(iformModel,dataid, params);
//		int i = 0; //更新记录数
//			i = iformDataDAO.update(sql);
//		if(i>0){
//			SysInstanceData instanceData = instanceDataDAO.getModel(instanceid,engineType); 
//			if(instanceData!=null){
//				instanceData.setUpdatedate(new Date());
//				
//				instanceData.setUpdateuser(uc.get_userModel().getUserid());
//				instanceDataDAO.update(instanceData);
//			} 
//			flag = true;
//		} 
//		else if(i==0){
//			Long id  = this.saveForm(modelId, formid, instanceid, map, isLog, engineType);
//			if(id>0){
//				flag = true;
//			}
//		}
		}
		catch(Exception e)
		{
			logger.error(e,e);		
		}
		return flag;
	}
	
	/**
	 * 删除指定实例的全部数据
	 * @param formid
	 * @param instanceid
	 * @param dataid
	 * @return
	 */
	public void removeFormData(Long instanceid,Long engineType){
		List<SysEngineFormBind> bindlist = iformBindDAO.getListOfInstancId(instanceid,engineType);
		for(SysEngineFormBind sefb:bindlist){
			SysEngineMetadata sem = sysEngineMetadataDAO.getModel(sefb.getMetadataid());
			if(sem!=null){
				iformDataDAO.remove(sem.getEntityname(),sefb.getDataid());
			}
		}
		//删除绑定记录
		iformBindDAO.deleteFormBindList(instanceid,engineType);
		//删除实例记录
		instanceDataDAO.delete(instanceid,engineType);
	}
	
	/**
	 * 删除子表记录
	 * @param formid
	 * @param instanceid
	 * @param dataid
	 * @return
	 */
	public boolean removeSubformData(Long formid ,Long instanceid,String dataids){
		SysEngineIform iformModel = sysEngineIFormDAO.getSysEngineIformModel(formid);
		SysEngineMetadata metadataModle = sysEngineMetadataDAO.getModel(iformModel.getMetadataid());
		boolean flag = true;
		if(dataids!=null&&dataids.length()>0){
			String[] ids = dataids.split(",");
			for(int i=0;i<ids.length;i++){
				Long l = Long.parseLong(ids[i]);
				if(l>=0){
					flag = iformDataDAO.remove(metadataModle.getEntityname(), l);
				}
			}
		}
		return flag;
	}
	/**
	 * 构建SQL
	 * @param formid
	 * @param instanceid
	 * @param hash
	 * @return
	 */
	private String buildAddSQL(SysEngineIform iformModel,int boid,HashMap hash){
		SysEngineMetadata metadataModle = sysEngineMetadataDAO.getModel(iformModel.getMetadataid());
		StringBuffer sql = new StringBuffer();
		StringBuffer fields = new StringBuffer();
		StringBuffer values = new StringBuffer();
		
		sql.append("INSERT INTO ").append(metadataModle.getEntityname()).append(" (");
		Iterator iter = hash.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    String key = (String)entry.getKey();
		    String val = "";
		    Object obj = entry.getValue();
		    SysEngineIformMap formMap = sysEngineIFormMapDAO.getModel(iformModel.getId(), key);
		    if(formMap==null){
		    	continue;
		    }
		    if(obj!=null){
		    	if(obj instanceof String[]){ 
		    		StringBuffer str = new StringBuffer();
			    	try{
			    		String[] list = (String[])obj;
						if(list.length==1){
							val = list[0]; 
						}else{
							//如果类型为复选框，则
							if(formMap!=null&&formMap.getDisplayType()!=null&&formMap.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_CHECKBOX)){
								for(String tmp:list){
									if(tmp.trim().equals(""))continue;
									str.append(tmp).append(",");
								}
								val = str.substring(0,str.length() - 1);
							}else{
								val = list[0]; 
							}
						}
	//		    		if(obj instanceof String[]){
	//		    			String[] value   =   (String[])entry.getValue();
	//			    		if(value!=null)
	//				    		val = value[0];
	//		    		}else{
	//		    			val = obj.toString();
	//		    		}
	//		    		val = val.trim();
			    		
			    	}catch(Exception e){
			    		val = (String)entry.getValue();
			    	}
		    	}else if(obj instanceof Date){ 
					if(obj!=null){
						Calendar c = Calendar.getInstance();
						c.setTime((Date)obj);
						int hour = c.get(Calendar.HOUR);
						int minute = c.get(Calendar.MINUTE);
						int ss = c.get(Calendar.SECOND);
						if(hour==0&&minute==0&&ss==0){
							val = UtilDate.dateFormat((Date)obj);
						}else{
							val = UtilDate.datetimeFormat((Date)obj);
						}
					}
		    		
		    	}else{
					val = obj.toString();
				}
		    	
		    }
		    fields.append(key).append(",");
		    SysEngineMetadataMap metadataMap = sysEngineMetadataMapDAO.getModel(iformModel.getMetadataid(), key);
		    if(metadataMap==null){
		    	continue;
		    }
		    //防止SQL注入
		    val = val.replace("'", "''");
		    String fieldType = metadataMap.getFieldtype();
		    //如果为数值型
		    if(fieldType.equals(EngineConstants.MAP_TYPE_NUMBER)||fieldType.equals(EngineConstants.MAP_TYPE_LONG)){
		    	if(val.equals(""))val=null;
		    	if(val!=null){
		    		try{
						Long.parseLong(val);
					}catch(Exception e){
						try{
							val = Double.parseDouble(val)+"";
						}catch(Exception se){
							val = null;
						}
					}
		    	}
		    	values.append(val).append(","); 
		    }else if(fieldType.equals(EngineConstants.MAP_TYPE_TEXT)||fieldType.equals(EngineConstants.MAP_TYPE_MEMO)){
		         //防止SQL注入  20170417 李翔 单引号转换重复
		    	  //val = val.replace("'", "''");
		    	  //
		    	  if(formMap!=null&&formMap.getDisplayType()!=null&&(formMap.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_ADV_RICH_TEXT)||formMap.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_SINGLE_RICH_TEXT))){
		    		   String uuid = UUIDUtil.getUUID();
						val = uuid; 
		    		   if( hash.get(key+"_EDIT")!=null){
		    			   String content = ObjectUtil.getString(hash.get(key+"_EDIT"));
		    			   boolean flag = richTextUtil.saveText(uuid, content);
		    		   }
		    		   
		    		  
				  }
		    	values.append("'").append(val).append("',");
		    }else if(fieldType.equals(EngineConstants.MAP_TYPE_DATE)){
		    	values.append(DBUtil.convertShortDate(val)).append(","); 
		    }else if(fieldType.equals(EngineConstants.MAP_TYPE_DATETIME)){
		    	values.append(DBUtil.convertLongDate(val)).append(","); 
		    }
		}
		//补充数据插入
		fields.append("ID");
		values.append(boid);
		sql.append(fields).append(") VALUES ").append("(").append(values).append(") ");
		return sql.toString();
	} 
	/**
	 * 构建SQL
	 * @param formid
	 * @param instanceid
	 * @param hash
	 * @return
	 */ 
	private String buildUpdateSQL(SysEngineIform iformModel,Long boid,HashMap hash){
		SysEngineMetadata metadataModle = sysEngineMetadataDAO.getModel(iformModel.getMetadataid());
		
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ").append(metadataModle.getEntityname()).append(" SET ");
		Iterator iter = hash.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
			String key = (String)entry.getKey();
			String val = "";
			SysEngineIformMap formMap = null;
			if(entry.getValue()!=null){ 
				formMap = sysEngineIFormMapDAO.getModel(iformModel.getId(), key);
				if(formMap==null){
			    	continue;
			    }  
				Object obj = entry.getValue();
				if(obj instanceof String[]){
					StringBuffer str = new StringBuffer();
					String[] list = (String[])obj;
					if(list.length==1){
						if(formMap!=null&&formMap.getDisplayType()!=null&&formMap.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_TXTBOX_DATE_TIME)){
							if(obj!=null){
								String datetimeStr = ObjectUtil.getString(obj);
								if(datetimeStr.indexOf("T")>0){
									datetimeStr = datetimeStr.replace("T", " ");
									String format = "yyyy-MM-dd HH:mm:ss";
									Date datetime = UtilDate.StringToDate(datetimeStr, format);
									String localdatetime = UtilDate.datetimeFormat(datetime);
									val = localdatetime;
								}else{
									val = list[0];
								}
							}
						}else{
							val = list[0];  
						}
						
					}else{
						//如果类型为复选框，则
						if(formMap!=null&&formMap.getDisplayType()!=null&&formMap.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_CHECKBOX)){
							for(String tmp:list){
								if(tmp.trim().equals(""))continue;
								str.append(tmp).append(",");
							}
							val = str.substring(0,str.length() - 1);
						}else{
							val = list[0]; 
						}
					}
					//防止SQL注入
					val = val.replace("'", "''");
				}else if(obj instanceof Date){ 
					if(obj!=null){
						Calendar c = Calendar.getInstance();
						c.setTime((Date)obj);
						int hour = c.get(Calendar.HOUR);
						int minute = c.get(Calendar.MINUTE);
						int ss = c.get(Calendar.SECOND);
						if(hour==0&&minute==0&&ss==0){
							val = UtilDate.dateFormat((Date)obj);
						}else{
							val = UtilDate.datetimeFormat((Date)obj);
						}
					}
		    		
		    	}else if(obj instanceof String){
					val = obj.toString();
					//防止SQL注入
					val = val.replace("'", "''");
				}else{
					val = obj.toString();
					//防止SQL注入
					val = val.replace("'", "''");
				}
//				val = value[0]; 
			} 
			SysEngineMetadataMap metadataMap = sysEngineMetadataMapDAO.getModel(iformModel.getMetadataid(), key);
			if(metadataMap==null)continue;
			String fieldType = metadataMap.getFieldtype();
			//如果为数值型
			if(fieldType.equals(EngineConstants.MAP_TYPE_NUMBER)||fieldType.equals(EngineConstants.MAP_TYPE_LONG)){
				if(val.equals(""))val=null;else val = val.trim();
				if(val!=null){
					try{
						Long.parseLong(val);
					}catch(Exception e){
						try{
							val = Double.parseDouble(val)+"";
						}catch(Exception se){
							val = null;
						}
					}
				}
				sql.append(key).append("=").append(val).append(",");
			}else if(fieldType.equals(EngineConstants.MAP_TYPE_TEXT)||fieldType.equals(EngineConstants.MAP_TYPE_MEMO)){
				//处理富文本
				 if(formMap!=null&&formMap.getDisplayType()!=null&&(formMap.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_ADV_RICH_TEXT)||formMap.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_SINGLE_RICH_TEXT))){
					 String uuid = "";
		    		   if(val!=null&&!val.equals("")){
		    			   uuid = val;
		    		   }else{
		    			   uuid = UUIDUtil.getUUID();
		    			   sql.append(key).append("='").append(uuid).append("',");
		    		   }
		    		    String valuekey = key+"_EDIT";
		    		   if(hash.get(valuekey)!=null){
		    			   String content = ObjectUtil.getString(hash.get(valuekey));
			    		   if(content!=null){
			    			   boolean flag = richTextUtil.updateText(uuid, content);
			    		   }
		    		   }
		    		   
				  }else{
					  sql.append(key).append("='").append(val).append("',");
				  }
			}else if(fieldType.equals(EngineConstants.MAP_TYPE_DATE)){
				sql.append(key).append("=").append(DBUtil.convertShortDate(val)).append(",");
			}else if(fieldType.equals(EngineConstants.MAP_TYPE_DATETIME)){
				sql.append(key).append("=").append(DBUtil.convertLongDate(val)).append(",");
			}else{
				sql.append(key).append("='").append(val).append("',");
			}
		}
		//补充数据插入
		sql.append(" ID ").append("=").append(boid);
		sql.append(" WHERE ").append("ID=").append(boid);
		return sql.toString();
	}
	
	

	
	/**
	 * 获得存储工作箱列
	 * @return
	 */
	public String getWorkBoxColumn(Long formid){
		StringBuffer html = new StringBuffer();
		html.append("columns:[[").append("\n");
//		 html.append("{field:'ID',title:'ID',width:30,hidden:true},").append("\n");
//		 html.append("{field:'INSTANCEID',title:'实例ID',width:30,hidden:true},").append("\n");
		 List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(formid);
		 for(int i=0;i<list.size();i++){
			 SysEngineIformMap mapModel =  list.get(i); 
			//设置默认值
			 	if(mapModel.getFieldName()==null)continue; //如果字段为空，则忽略本列
			 	if(mapModel.getDisplaywidth()==null)mapModel.setDisplaywidth(new Long(30));//显示宽度
			 	if(mapModel.getFieldTitle()==null)mapModel.setFieldTitle("");//显示宽度
			 html.append("{");
			 	html.append("field:'").append(mapModel.getFieldName()).append("',title:'").append(mapModel.getFieldTitle()).append("',sortable:true");
			 	if(mapModel.getDisplaywidth()!=0){
			 		html.append(",width:").append(mapModel.getDisplaywidth());
			 	}else{
			 		html.append(",width:").append("30");
			 	}
			 html.append("}"); 
			 if(i+1<list.size()){
				 html.append(",").append("\n");
			 }
			 
		 }
		 html.append("]],").append("\n");
		 
		return html.toString();
	}
	
	
	/**
	 * 获得表单存储维护工作箱列表
	 * @param formid
	 * @return
	 */
	public String getIformWorkBoxScript(Long formid){
		StringBuffer script = new StringBuffer();
		script.append("<script>").append("\n");
		script.append("var lastsel;").append("\n");
		script.append("jQuery(\"#iform_grid\").jqGrid({").append("\n");
		//获得数据URL
		script.append("   	url:'getDataformjson.action?formid=").append(formid).append("',").append("\n");
		script.append("	datatype: \"json\",").append("\n");
		script.append("	mtype: \"POST\",").append("\n");
		script.append("	autowidth:true,").append("\n");
		//获得列标题
		script.append("   	colNames:").append(this.getWorkBoxColumTitle(formid)).append(",").append("\n");
		script.append("   	colModel:").append(this.getWorkBoxColumModel(formid)).append(",").append("\n");
		script.append("   	rowNum:10,").append("\n");
		script.append("   	rowList:[10,20,30,40],").append("\n");
		script.append("   	multiselect: true,").append("\n");
		script.append("   	pager: '#prowed_ifrom_grid',").append("\n");
		script.append("   	prmNames:{rows:\"page.pageSize\",page:\"page.curPageNo\",sort:\"page.orderBy\",order:\"page.order\"},").append("\n");
		script.append("     jsonReader: {").append("\n");
		script.append("     	root: \"dataRows\",").append("\n");
		script.append("     	page: \"curPage\",").append("\n");
		script.append("     	total: \"totalPages\",").append("\n");
		script.append("     	records: \"totalRecords\",").append("\n");
		script.append("     	repeatitems: false,").append("\n");
		script.append("     	id: \"id\",").append("\n");
		script.append("     	userdata: \"userdata\"").append("\n");
		script.append("    },").append("\n");
		
		script.append("   	sortname: 'id',").append("\n");
		script.append("    	viewrecords: true,").append("\n");
		script.append("    	resizable:true,").append("\n");
//		script.append("     width: \"100%\",").append("\n");
		script.append("     height: \"100%\",").append("\n");
		script.append("     sortorder: \"desc\",").append("\n");
		script.append("		ondblClickRow: function(id){").append("\n");
		script.append("		if(id && id!==lastsel){").append("\n");
		
		script.append("		}").append("\n");
		script.append("	}").append("\n"); 
		script.append("});").append("\n"); 
		script.append("jQuery(\"#iform_grid\").jqGrid('navGrid',\"#prowed_iform_grid").append("\",{edit:false,closeOnEscape:true,add:false,del:false});").append("\n");
		script.append("</script>").append("\n");
		return script.toString();
	}
	
	/**
	 * 获得流程工作箱列表标题
	 * @param formid
	 * @return
	 */
	private String getWorkBoxColumTitle(Long formid){
		List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(formid);
		StringBuffer html = new StringBuffer();
		List item = new ArrayList();
		item.add("序号");
		if(list==null)return "";
		for(int i=0;i<list.size();i++){
			SysEngineIformMap mapModel = list.get(i);
			item.add(mapModel.getFieldTitle());
		}
		item.add("序号");
		
		JSONArray json = JSONArray.fromObject(item);
		html.append(json);
		return html.toString();
	}
	/**
	 * 获得流程工作箱列表模板
	 * @param formid
	 * @return
	 */
	private String getWorkBoxColumModel(Long formid){
		List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(formid);
		StringBuffer html = new StringBuffer();
		List item = new ArrayList();
		//装载ID序列
		Map rownum = new HashMap();
		rownum.put("name","ROWNUM");
		rownum.put("index","1");
		rownum.put("width","60");
		item.add(rownum);
		if(list==null)return "";
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			SysEngineIformMap mapModel = list.get(i);
			map.put("name", mapModel.getFieldName());
			map.put("index",mapModel.getMapindex()); 
			if(mapModel.getDisplaywidth()==null)mapModel.setDisplaywidth(new Long(100));
			map.put("width", mapModel.getDisplaywidth());
			map.put("align","left");
			item.add(map);
		}
		JSONArray json = JSONArray.fromObject(item);
		html.append(json);
		return html.toString();
	}
	
	/**
	 * 获得预览界面
	 * @return
	 */
	public String getPreviewPage(Long formid){
		//获得表单模型
		SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
		//获得存储模型
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIform.getMetadataid());
		
		StringWriter stringWriter = new StringWriter();
		Map root = new HashMap();
		try {
			Configuration freemarkerCfg = freemarderConfig.createConfiguration();
			Template template = null;
			try{
				template = freemarkerCfg.getTemplate(sysEngineIform.getIformTemplate());
			}catch(Exception e){
				logger.error(e,e);
			}
			if(template==null)return "<h3>未发现定义的名称为["+sysEngineIform.getIformTemplate()+"]的表单模版</h3>";
			BufferedWriter writer = new BufferedWriter(stringWriter);
			template.setEncoding("GBK");
			String html = template.getNumberFormat();
			TemplateElement te=template.getRootTreeNode();  
			for(Enumeration children = te.children(); children.hasMoreElements();){  
			                Object obj=children.nextElement();  
			                if("class freemarker.core.DollarVariable".equals(obj.getClass().toString())){
			                	TemplateElement   t =(TemplateElement)obj;
			                    String label = t.getCanonicalForm().replace("${","").replace("}","").toUpperCase();
			                    String uiDefine = this.getUICompoment(sysEngineIform,metadataModel,null,new Long(0),label,"",IFormStatusConstants.FORM_FIELD_MODIFY,EngineConstants.SYS_INSTANCE_TYPE_DEM);
			                    if(uiDefine!=null){
			                    	root.put(label,uiDefine);
			                    	iformMapList.add(label);
			                    }else{
			                    	String log = ""+t.getCanonicalForm()+"未找到域对象";
			                    	verifyErrorInfo.add(log);
			                    	root.put(label,"");
			                    }
			                }  
			} 
			template.process(root, writer);
			StringReader reader = new StringReader(stringWriter.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
		return stringWriter.toString();
	}
	
	/**
	 * 获得主表单数据
	 * @param formid
	 * @param instanceid
	 * @return
	 */
	public HashMap getMainFormData(Long formid,Long instanceid,Long engineType){
		//获得表单模型
		SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
		//获得域列表模型
		List<SysEngineIformMap> maplist = sysEngineIFormMapDAO.getDataList(formid);
		//获得存储模型
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIform.getMetadataid());
		List<SysEngineMetadataMap> metadataList = sysEngineMetadataMapDAO.getDataList(sysEngineIform.getMetadataid());
		//获得表单数据 
		HashMap formdata = null;
		if(instanceid!=null&&formid!=null){   //数据有效判断
			SysEngineFormBind formbind = iformBindDAO.getModel(formid,sysEngineIform.getMetadataid(),instanceid,engineType);
			if(formbind!=null){
				try {
					formdata = iformDataDAO.getFormData(metadataList,metadataModel.getEntityname(), formbind.getDataid());
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
		return formdata;
	}
	
	/**
	 *获得只读表单
	 * @param formid 表单ID
	 * @param instanceid  流程实例ID
	 * @return
	 */
	public String getFormPage(Long formid,Long instanceid,Long engineType){
		//获得表单模型 
		SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
		Long pageType = IFormStatusConstants.FORM_PAGE_READ; 
		if(sysEngineIform!=null){
			String master = sysEngineIform.getMaster();
			master = UserContextUtil.getInstance().getUserId(master.toUpperCase());
			String currentUser = UserContextUtil.getInstance().getCurrentUserId();
			if(master.equals(currentUser)||SecurityUtil.isSuperManager()){
				pageType = IFormStatusConstants.FORM_PAGE_MODIFY;
			}else{
				 pageType = IFormStatusConstants.FORM_PAGE_READ;
			}
		}
		return  getFormPage(formid,instanceid,pageType,null,null,null,engineType); 
	}
	
	/**
	 * 获得数据维护模型表单
	 * @param demId
	 * @param formid
	 * @param instanceid
	 * @return
	 */
	public String getFormPage(Long formid,Long instanceid,boolean isSecurity,Long engineType){
		if(isSecurity){
			//获得表单模型
			SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
			Long pageType = IFormStatusConstants.FORM_PAGE_READ; 
			if(sysEngineIform!=null){
				if(isSecurity||SecurityUtil.isSuperManager()){
					pageType = IFormStatusConstants.FORM_PAGE_MODIFY;
				}else{ 
					pageType = IFormStatusConstants.FORM_PAGE_READ;
				} 
			} 
			return  getFormPage(formid,instanceid,pageType,null,null,null,engineType); 
		}else{
			return "权限不足，请联系管理员";
		}
		
	}
	
	/**
	 * 获得数据维护模型表单
	 * @param demId
	 * @param formid
	 * @param instanceid
	 * @return
	 */
	public String getFormPage(Long formid,Long instanceid,boolean isSecurity,Map paramMap,Long engineType){
		if(isSecurity){
			//获得表单模型
			SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
			Long pageType = IFormStatusConstants.FORM_PAGE_READ; 
			if(sysEngineIform!=null){
				if(isSecurity||SecurityUtil.isSuperManager()){
					pageType = IFormStatusConstants.FORM_PAGE_MODIFY;
				}else{ 
					pageType = IFormStatusConstants.FORM_PAGE_READ;
				} 
			} 
			return  getFormPage(formid,instanceid,pageType,null,null,paramMap,engineType); 
		}else{
			return "权限不足，请联系管理员";
		}
	}
	
	/**
	 * 获得数据维护模型表单
	 * @param demId
	 * @param formid
	 * @param instanceid
	 * @return
	 */
	public String getSubFormPage(Long formid,Long instanceid,Long dataid,boolean isSecurity,Map paramMap,Long engineType){
			//获得表单模型
			SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
			Long pageType = IFormStatusConstants.FORM_PAGE_READ; 
			if(sysEngineIform!=null){
				if(isSecurity){
					pageType = IFormStatusConstants.FORM_PAGE_MODIFY;
				}else{ 
					pageType = IFormStatusConstants.FORM_PAGE_READ;
				} 
			} 
			//获取当前任务及子表权限设置
			
			return  getSubFormPage(formid,instanceid,dataid,pageType,null,null,paramMap,engineType); 
	}
	 
	/**
	 * @param modelId
	 * @param formid     表单ID
	 * @param instanceid  流程实例ID
	 * @param formStatus  表单权限状态  只读/可编辑
	 * @param psfmList   表单模型列表
	 * @param taskId   任务ID
	 * @param pageParams  页面初始录入参数
	 * @return
	 */
	public String getFormPage(Long formid,Long instanceid,Long formStatus,HashMap<String,ProcessStepFormMap> psfmList,String taskId,Map pageParams,Long engineType){
		//获得表单模型 
		SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
		if(sysEngineIform==null){
			return "未发现表单模板,请联系管理员";
		}
		//获得域列表模型
		List<SysEngineIformMap> maplist = sysEngineIFormMapDAO.getDataList(formid);
		//获得存储模型
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIform.getMetadataid());
		List<SysEngineMetadataMap> metadataList = sysEngineMetadataMapDAO.getDataList(sysEngineIform.getMetadataid());
		if(sysEngineIform.getIformTitle()==null){
			sysEngineIform.setIformTitle("nofindtitle");
		}
		//初始化参数
		if(sysEngineIform.getIformTemplate()==null)sysEngineIform.setIformTemplate(sysEngineIform.getIformTitle().trim()+".flt");
		//获取流程任务参数
		HashMap taskParam = null;
		if(taskId!=null&&!taskId.equals("0")){
			try{
				Object obj = taskService.getVariables(taskId);
				if(obj!=null){
					taskParam = (HashMap)taskService.getVariables(taskId);
				}
			}catch(Exception e){
				
			}
		}
		if(taskParam==null){
			taskParam = new HashMap();
		}
		
		HashMap formData = ParameterMapUtil.getParameterMap(pageParams);
		
		taskParam.put(ProcessTaskConstant.PROCESS_TASK_FORMDATA, formData); 
		
		StringWriter stringWriter = new StringWriter();
		Map root = new HashMap();
		try {
				Configuration freemarkerCfg = freemarderConfig.createConfiguration();
				freemarkerCfg.setDefaultEncoding("GB2312");
				Template template = null;
				try{
					
					template = freemarkerCfg.getTemplate(sysEngineIform.getIformTemplate());
					template.setEncoding("UTF-8");
				}catch(Exception e){
					logger.error(e,e);
				}
				if(template==null)return "<h3>未发现定义的名称为【"+sysEngineIform.getIformTemplate()+"】的表单模版</h3>";
				BufferedWriter writer = new BufferedWriter(stringWriter);
				TemplateElement te=template.getRootTreeNode(); 
			//获得表单数据 
			HashMap formdata = null;
			if(instanceid!=null&&formid!=null){   //数据有效判断 
				long s1 = System.currentTimeMillis();
				SysEngineFormBind formbind = iformBindDAO.getModel(formid,sysEngineIform.getMetadataid(),instanceid,engineType);
				long e1 = System.currentTimeMillis();
				if(formbind!=null){
					try {
						long s = System.currentTimeMillis();
						formdata = iformDataDAO.getFormData(metadataList,metadataModel.getEntityname(), formbind.getDataid());
						long e = System.currentTimeMillis();
					} catch (Exception e) {
						logger.error(e,e);
					}
				}
			}
			if(formdata==null){
				formdata = new HashMap();
			}
			//================================================================================================================================================
			long s2 = System.currentTimeMillis();
			//获取子表信息
			List<SysEngineSubform>  subTablelist = sysEngineSubformDAO.getDataList(formid);
			//循环组件
			for(Enumeration children = te.children(); children.hasMoreElements();){  
				Object obj=children.nextElement();  
				if("class freemarker.core.DollarVariable".equals(obj.getClass().toString())){
					TemplateElement   t =(TemplateElement)obj;
					String label = t.getCanonicalForm().replace("${","").replace("}","");
					String field = label.toUpperCase();
					//表单域
					String value = "";
					if(formdata!=null){
						Object data = formdata.get(field);
						if(data!=null){
								if (data instanceof String){
									value = data.toString(); 
								}else if(data instanceof Date){
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Date d = (Date)data; 
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(d);
									int hour = calendar.get(Calendar.HOUR);   
									int minute = calendar.get(Calendar.MINUTE);   
									int seconds =calendar.get(Calendar.SECOND);   
									if(hour==0&&minute==0&&seconds==0){
										sdf = new SimpleDateFormat("yyyy-MM-dd");
									}  
									value = sdf.format(data); 
								}else if(data instanceof Long){
									value = data.toString(); 
								}else{
									value = data.toString(); 
								} 
							}
						//如果显示信息为空，则显示传入的表单参数
						if("".equals(value)&&pageParams!=null){
							Object param = pageParams.get(field);
							if(param!=null){
								if(param instanceof String[]){
									value = ObjectUtil.getString(param);
								}else if(param instanceof Map){
									Map.Entry pairs = (Map.Entry)param;
									if(pairs!=null){
										value = (String)pairs.getValue();
										value = UtilCode.convert2UTF8(value);
									}
								}else{
									Map.Entry pairs = (Map.Entry)param;
									if(pairs!=null){
										value = (String)pairs.getValue();
										value = UtilCode.convert2UTF8(value);
									}
								}
								
							}
						} 
					}else{
						if("".equals(value)&&pageParams!=null){
							Object param = pageParams.get(field);
							if(param!=null){
								value = ObjectUtil.getString(param);
							}
						}
					}  
					String uiDefine = "";
					//设置字段权限状态参数，默认为只读
					int status = IFormStatusConstants.FORM_FIELD_READ;
					//===============================判断表单权限===========================================
					if(formStatus.equals(IFormStatusConstants.FORM_PAGE_MODIFY)){ 
						try{
							//判断字段权限 
							if(psfmList!=null){
								ProcessStepFormMap psfm = (ProcessStepFormMap)psfmList.get(formid+field);
								if(psfm!=null){
									if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_MODIFY)){//编辑状态
										status = IFormStatusConstants.FORM_FIELD_MODIFY;
									}else if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_READONLY)){
										status = IFormStatusConstants.FORM_FIELD_READ;
									}else if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_HIDDEN)){
										status = IFormStatusConstants.FORM_FIELD_PROCESS_STEP_HIDDEN;
									} 
								}else{
									status = IFormStatusConstants.FORM_FIELD_MODIFY;
								}
							}else{
								status = IFormStatusConstants.FORM_FIELD_MODIFY;
							}
						}catch(Exception e){
							logger.error(e,e);
						}
					}else{
						try{
							//判断字段权限 
							if(psfmList!=null){
								ProcessStepFormMap psfm = (ProcessStepFormMap)psfmList.get(formid+field);
								if(psfm!=null){
									if(psfm!=null){   
										if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_HIDDEN)){
											status = IFormStatusConstants.FORM_FIELD_PROCESS_STEP_HIDDEN;
										} 
									}
								}
							}
						}catch(Exception e){
							logger.error(e,e);
						}
					}
					//获得域描述
					try{
					uiDefine = this.getUICompoment(sysEngineIform,metadataModel,taskParam,instanceid,field,value,status,engineType);
					}catch(Exception e){
						uiDefine = "#组件异常请检查设置["+field+"]";
					}
					//域标签非空判断
					if(uiDefine!=null&&!"".equals(uiDefine)){
						root.put(label,uiDefine);  
					}else{ 
						if(subTablelist!=null){
							boolean isSubForm= false;
							for(int i=0;i<subTablelist.size();i++){
								SysEngineSubform subform = subTablelist.get(i);
								if(subform.getSubtablekey()==null)continue;
								if(subform.getSubtablekey().toUpperCase().equals(field)){
									if(subform.getType().equals(new Long(0))||subform.getType().equals(new Long(1))){
										String subFormScript = this.getOnEditSubFormScript(subform,instanceid,formStatus,psfmList,engineType,pageParams);
										if(subformIsHidden&&engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_PROCESS)){
											root.put(label,"");
										}else{
											root.put(label,subFormScript);
										}
									}else if(subform.getType().equals(new Long(3))){   //扩展自定义子表
										String subFormScript = this.getExtSubFormScript(subform,instanceid,formStatus,psfmList,engineType,pageParams);
										if(subformIsHidden&&engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_PROCESS)){
											root.put(label,"");
										}else{
											root.put(label,subFormScript);
										}
									}else{ 
										String subFormScript = this.getOnFormSubFormScript(subform,instanceid,formStatus,psfmList,engineType,pageParams);
										if(subformIsHidden&&engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_PROCESS)){
											root.put(label,"");
										}else{
											root.put(label,subFormScript);
										}
									}
									isSubForm = true;
									break;
								}
							}
							if(!isSubForm){
								root.put(label,"");
							}
						}else{
							root.put(label,"");
						}
					}
				}  
			} 
			long e2 = System.currentTimeMillis();			
			template.process(root, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error(e,e);
			stringWriter.append("数据异常，请联系管理员!<br>["+e.getMessage()+"]");
		} 
		
		return stringWriter.toString();
	}
	/**
	 * @param modelId
	 * @param formid     表单ID
	 * @param instanceid  流程实例ID
	 * @param formStatus  表单权限状态  只读/可编辑
	 * @param psfmList   表单模型列表
	 * @param taskId   任务ID
	 * @param pageParams  页面初始录入参数
	 * @return
	 */
	public String getSubFormPage(Long formid,Long instanceid,Long dataid,Long formStatus,HashMap<String,ProcessStepFormMap> psfmList,String taskId,Map pageParams,Long engineType){
		//获得表单模型 
		SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
		if(sysEngineIform==null){
			return "未发现表单模板,请联系管理员";
		}
		//获得域列表模型
		List<SysEngineIformMap> maplist = sysEngineIFormMapDAO.getDataList(formid);
		//获得存储模型
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIform.getMetadataid());
		List<SysEngineMetadataMap> metadataList = sysEngineMetadataMapDAO.getDataList(metadataModel.getId());

		if(sysEngineIform.getIformTitle()==null){
			sysEngineIform.setIformTitle("nofindtitle");
		}
		//初始化参数
		if(sysEngineIform.getIformTemplate()==null)sysEngineIform.setIformTemplate(sysEngineIform.getIformTitle().trim()+".flt");
		//获取流程任务参数
		HashMap taskParam = null;
		if(taskId!=null&&!taskId.equals("0")){
			try{ 
				Object obj = taskService.getVariables(taskId);
				if(obj!=null){
					taskParam = (HashMap)taskService.getVariables(taskId);
				}
			}catch(Exception e){
				
			}
		}
		if(taskParam==null){
			taskParam = new HashMap();
		}
		taskParam.put(ProcessTaskConstant.PROCESS_TASK_FORMDATA, pageParams); 
		
		StringWriter stringWriter = new StringWriter();
		Map root = new HashMap();
		try {
			Configuration freemarkerCfg = freemarderConfig.createConfiguration();
			freemarkerCfg.setDefaultEncoding("GB2312");
			Template template = null;
			try{
				
				template = freemarkerCfg.getTemplate(sysEngineIform.getIformTemplate());
				template.setEncoding("UTF-8");
			}catch(Exception e){
				logger.error(e,e);
			}
			if(template==null)return "<h3>未发现定义的名称为【"+sysEngineIform.getIformTemplate()+"】的表单模版</h3>";
			BufferedWriter writer = new BufferedWriter(stringWriter);
			TemplateElement te=template.getRootTreeNode(); 
			//获得表单数据 
			HashMap formdata = null;
			if(instanceid!=null&&formid!=null&&dataid!=null){   //数据有效判断 
					try {
						formdata = iformDataDAO.getFormData(metadataList,metadataModel.getEntityname(), dataid);
					} catch (Exception e) {
						logger.error(e,e);
					}
			}
			//获取子表信息
			List<SysEngineSubform>  subTablelist = sysEngineSubformDAO.getDataList(formid);
			//循环组件
			for(Enumeration children = te.children(); children.hasMoreElements();){  
				Object obj=children.nextElement();  
				if("class freemarker.core.DollarVariable".equals(obj.getClass().toString())){
					TemplateElement   t =(TemplateElement)obj;
					String label = t.getCanonicalForm().replace("${","").replace("}","");
					String field = label.toUpperCase();
					//表单域
					String value = "";
					if(formdata!=null){
						Object data = formdata.get(field);
						if(data!=null){
							if (data instanceof String){
								value = data.toString(); 
							}else if(data instanceof Date){
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date d = (Date)data; 
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(d);
								int hour = calendar.get(Calendar.HOUR);   
								int minute = calendar.get(Calendar.MINUTE);   
								int seconds =calendar.get(Calendar.SECOND);   
								if(hour==0&&minute==0&&seconds==0){
									sdf = new SimpleDateFormat("yyyy-MM-dd");
								}  
								value = sdf.format(data); 
							}else if(data instanceof Long){
								value = data.toString(); 
							}else{
								value = data.toString(); 
							}  
						}
						//如果显示信息为空，则显示传入的表单参数
						if("".equals(value)&&pageParams!=null){
							Object param = pageParams.get(field);
							if(param!=null){
								Map.Entry pairs = (Map.Entry)param;
								if(pairs!=null){
									value = (String)pairs.getValue();
								}
							}
						} 
					}else{
						if("".equals(value)&&pageParams!=null){
							Object param = pageParams.get(field);
							if(param!=null){
								String[] str = (String[])param;
								if(str!=null&&str.length>0){
									value = (String)str[0];
								}
							}
						}
					}  
					String uiDefine = "";
					//设置字段权限状态参数，默认为只读
					int status = IFormStatusConstants.FORM_FIELD_READ;
					//===============================判断表单权限===========================================
					if(formStatus.equals(IFormStatusConstants.FORM_PAGE_MODIFY)){ 
						try{
							//判断字段权限 
							if(psfmList!=null){
								ProcessStepFormMap psfm = (ProcessStepFormMap)psfmList.get(formid+field);
								if(psfm!=null){
									if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_MODIFY)){//编辑状态
										status = IFormStatusConstants.FORM_FIELD_MODIFY;
									}else if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_READONLY)){
										status = IFormStatusConstants.FORM_FIELD_READ;
									}else if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_HIDDEN)){
										status = IFormStatusConstants.FORM_FIELD_HIDDEN;
									} 
								}else{
									status = IFormStatusConstants.FORM_FIELD_MODIFY;
								}
							}else{
								status = IFormStatusConstants.FORM_FIELD_MODIFY;
							}
						}catch(Exception e){
							logger.error(e,e);
						}
					}else{
						try{
							//判断字段权限 
							if(psfmList!=null){
								ProcessStepFormMap psfm = (ProcessStepFormMap)psfmList.get(formid+field);
								if(psfm!=null){
									if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_MODIFY)){//编辑状态
										status = IFormStatusConstants.FORM_FIELD_MODIFY;
									}else if(psfm!=null){   
										if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_HIDDEN)){
											status = IFormStatusConstants.FORM_FIELD_HIDDEN;
										}
									}
								}
							}
						}catch(Exception e){
							logger.error(e,e);
						}
					}
					//获得域描述
					uiDefine = this.getUICompoment(sysEngineIform,metadataModel,taskParam,instanceid,field,value,status,engineType);
					//域标签非空判断
					if(uiDefine!=null&&!"".equals(uiDefine)){
						root.put(label,uiDefine);  
					}else{ 
						if(subTablelist!=null){
							boolean isSubForm= false;
							for(int i=0;i<subTablelist.size();i++){
								SysEngineSubform subform = subTablelist.get(i);
								
								if(subform.getSubtablekey()==null)continue;
								if(subform.getSubtablekey().toUpperCase().equals(field)){
									if(subform.getType().equals(new Long(0))||subform.getType().equals(new Long(1))){
										String subFormScript = this.getOnEditSubFormScript(subform,instanceid,formStatus,psfmList,engineType,pageParams);
										if(subformIsHidden&&engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_PROCESS)){
											root.put(label,"");
										}else{
											root.put(label,subFormScript);
										}
									}else{
										String subFormScript = this.getOnFormSubFormScript(subform,instanceid,formStatus,psfmList,engineType,pageParams);
										if(subformIsHidden&&engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_PROCESS)){
											root.put(label,"");
										}else{
											root.put(label,subFormScript);
										}
									}
									isSubForm = true;
									break;
								}
							}
							if(!isSubForm){
								root.put(label,"");
							}
						}else{
							root.put(label,"");
						}
					}
				}  
			} 
			template.process(root, writer);
			//StringReader reader = new StringReader(stringWriter.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error(e,e);
			stringWriter.append("数据异常，请联系管理员!<br>["+e.getMessage()+"]");
		} 
		
		return stringWriter.toString();
	}
	
	/**
	 * 获取移动端显示表单页面
	 * @param formid
	 * @param instanceid
	 * @param formStatus
	 * @param psfmList  流程节点域权限列表
	 * @param processParam
	 * @return
	 */
	public String getMobileFormPage(Long formid,Long instanceid,Long formStatus,HashMap<String,ProcessStepFormMap> psfmList,String taskId,Map pageParams){
		Long engineType = EngineConstants.SYS_INSTANCE_TYPE_PROCESS;
		//获得表单模型 
		SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
		if(sysEngineIform==null){
			return "未发现表单模板,请联系管理员";
		}
		//获得域列表模型
		List<SysEngineIformMap> maplist = sysEngineIFormMapDAO.getDataList(formid);
		//获得存储模型
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIform.getMetadataid());
		List<SysEngineMetadataMap> metadataList = sysEngineMetadataMapDAO.getDataList(metadataModel.getId());
		if(sysEngineIform.getIformTitle()==null){
			sysEngineIform.setIformTitle("nofindtitle");
		}
		//初始化参数
		if(sysEngineIform.getIformTemplate()==null)sysEngineIform.setIformTemplate(sysEngineIform.getIformTitle().trim()+".flt");
		//获取流程任务参数
		HashMap taskParam = null;
		if(taskId!=null&&!taskId.equals("0")){
			try{
				Object obj = taskService.getVariables(taskId);
				if(obj!=null){
					taskParam = (HashMap)taskService.getVariables(taskId);
				}
			}catch(Exception e){
				
			}
		}
		if(taskParam==null){
			taskParam = new HashMap();
		}
		taskParam.put(ProcessTaskConstant.PROCESS_TASK_FORMDATA, pageParams); 
		
		StringWriter stringWriter = new StringWriter();
		Map root = new HashMap();
		try {
				Configuration freemarkerCfg = mobilefreemarderConfig.createConfiguration();
				freemarkerCfg.setDefaultEncoding("GB2312");
				Template template = null;
				try{
					
					template = freemarkerCfg.getTemplate(sysEngineIform.getIformTemplate());
					template.setEncoding("UTF-8");
				}catch(Exception e){
				}
//				if(template==null)return "<h3>未发现定义的名称为【"+sysEngineIform.getIformTemplate()+"】的表单模版</h3>";
				if(template==null)return this.getMobileDefaultFormPage(formid, instanceid, formStatus, psfmList, taskId, pageParams);
				BufferedWriter writer = new BufferedWriter(stringWriter);
				TemplateElement te=template.getRootTreeNode();  
			//获得表单数据 
			HashMap formdata = null;
			if(instanceid!=null&&formid!=null){   //数据有效判断 
				SysEngineFormBind formbind = iformBindDAO.getModel(formid,sysEngineIform.getMetadataid(),instanceid,engineType);
				if(formbind!=null){
					try {
						formdata = iformDataDAO.getFormData(metadataList,metadataModel.getEntityname(), formbind.getDataid());
					} catch (Exception e) {
						logger.error(e,e);
					}
				}
			}
			//获取子表信息
			List<SysEngineSubform>  subTablelist = sysEngineSubformDAO.getDataList(formid);
			//循环组件
			for(Enumeration children = te.children(); children.hasMoreElements();){  
				Object obj=children.nextElement();  
				if("class freemarker.core.DollarVariable".equals(obj.getClass().toString())){
					TemplateElement   t =(TemplateElement)obj;
					String label = t.getCanonicalForm().replace("${","").replace("}","");
					String field = label.toUpperCase();
					//表单域
					String value = "";
					if(formdata!=null){
						Object data = formdata.get(field);
						if(data!=null){
								if (data instanceof String){
									value = data.toString(); 
								}else if(data instanceof Date){
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Date d = (Date)data; 
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(d);
									int hour = calendar.get(Calendar.HOUR);   
									int minute = calendar.get(Calendar.MINUTE);   
									int seconds =calendar.get(Calendar.SECOND);   
									if(hour==0&&minute==0&&seconds==0){
										sdf = new SimpleDateFormat("yyyy-MM-dd");
									}  
									value = sdf.format(data); 
								}else if(data instanceof Long){
									value = data.toString(); 
								}else{
									value = data.toString(); 
								}  
							}
						//如果显示信息为空，则显示传入的表单参数
						if("".equals(value)&&pageParams!=null){
							Object param = pageParams.get(field);
							if(param!=null){
								Map.Entry pairs = (Map.Entry)param;
								if(pairs!=null){
									value = (String)pairs.getValue();
									value = UtilCode.convert2UTF8(value);
								}
							}
						} 
					}else{
						if("".equals(value)&&pageParams!=null){
							Object param = pageParams.get(field);
							if(param!=null){
								value = ObjectUtil.getString(param);
							}
						}
					}  
					String uiDefine = "";
					//设置字段权限状态参数，默认为只读
					int status = IFormStatusConstants.FORM_FIELD_READ;
					//===============================判断表单权限===========================================
					if(formStatus.equals(IFormStatusConstants.FORM_PAGE_MODIFY)){ 
						try{
							//判断字段权限 
							if(psfmList!=null){
								ProcessStepFormMap psfm = (ProcessStepFormMap)psfmList.get(formid+field);
								if(psfm!=null){
									if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_MODIFY)){//编辑状态
										status = IFormStatusConstants.FORM_FIELD_MODIFY;
									}else if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_READONLY)){
										status = IFormStatusConstants.FORM_FIELD_READ;
									}else if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_HIDDEN)){
										status = IFormStatusConstants.FORM_FIELD_PROCESS_STEP_HIDDEN;
									} 
								}else{
									status = IFormStatusConstants.FORM_FIELD_MODIFY;
								}
							}else{
								status = IFormStatusConstants.FORM_FIELD_MODIFY;
							}
						}catch(Exception e){
							logger.error(e,e);
						}
					}else{
						try{
							//判断字段权限 
							if(psfmList!=null){
								ProcessStepFormMap psfm = (ProcessStepFormMap)psfmList.get(formid+field);
								if(psfm!=null){
									if(psfm!=null){   
										if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_HIDDEN)){
											status = IFormStatusConstants.FORM_FIELD_PROCESS_STEP_HIDDEN;
										} 
									}
								}
							}
						}catch(Exception e){
							logger.error(e,e);
						}
					}
					//获得域描述
					uiDefine = this.getMobileUICompoment(sysEngineIform,metadataModel,taskParam,instanceid,field,value,status);
					//域标签非空判断
					if(uiDefine!=null&&!"".equals(uiDefine)){
						root.put(label,uiDefine);  
					}else{
						if(subTablelist!=null){
							//获取子表信息
							for(SysEngineSubform subform:subTablelist){
								if(taskId==null||"".equals(taskId)){
									taskId = "0";
								}
							} 
							boolean isSubForm= false;
							for(int i=0;i<subTablelist.size();i++){
								SysEngineSubform subform = subTablelist.get(i);
								if(subform.getSubtablekey()==null)continue;
								if(subform.getSubtablekey().toUpperCase().equals(field)){
									StringBuffer mobileHTML = new StringBuffer();
									mobileHTML.append("<a href=\"javascript:showSubform(").append(taskId).append(",").append(instanceid).append(",").append(subform.getId()).append(",'").append(subform.getTitle()).append("');\" data-role=\"button\" data-icon=\"grid\">").append(subform.getTitle()).append("</a>").append("\n");
										if(subformIsHidden&&engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_PROCESS)){
											root.put(label,"");
										}else{
											root.put(label,mobileHTML);
										}
									isSubForm = true;
									break;
								}
							}
							if(!isSubForm){
								root.put(label,"");
							}
						}else{
							root.put(label,"");
						}
					}
				}  
			} 
			template.process(root, writer);
			//StringReader reader = new StringReader(stringWriter.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error(e,e);
		
		} 
		
		return stringWriter.toString();
	}
	
	/**
	 * 获取移动端显示表单页面
	 * @param formid
	 * @param instanceid
	 * @param formStatus
	 * @param psfmList  流程节点域权限列表
	 * @param processParam
	 * @return
	 */
	public String getMobileDefaultFormPage(Long formid,Long instanceid,Long formStatus,HashMap<String,ProcessStepFormMap> psfmList,String taskId,Map pageParams){
		//获得表单模型 
		SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
		if(sysEngineIform==null){
			return "未发现表单模板,请联系管理员";
		}
		//获得域列表模板
		List<SysEngineIformMap> maplist = sysEngineIFormMapDAO.getDataList(formid);
		//获得存储模型
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIform.getMetadataid());
		List<SysEngineMetadataMap> metadataList = sysEngineMetadataMapDAO.getDataList(metadataModel.getId());
		//初始化参数
		if(sysEngineIform.getIformTemplate()==null)sysEngineIform.setIformTemplate(sysEngineIform.getIformTitle()+".flt");
		//获取流程任务参数
		HashMap taskParam = null;
		if(taskId!=null&&!taskId.equals("")&&!taskId.equals("0")){
			try{
				taskParam = (HashMap)taskService.getVariables(taskId);
			}catch(Exception e){}
		}
		StringBuffer mobileHTML = new StringBuffer();
		mobileHTML.append(" <div class=\"weui_cells weui_cells_form\">");
		try {
			//获得表单数据 
			HashMap formdata = null;
			if(instanceid!=null&&formid!=null){   //数据有效判断
				SysEngineFormBind formbind = iformBindDAO.getModel(formid,sysEngineIform.getMetadataid(),instanceid,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(formbind!=null){
					try {
						formdata = iformDataDAO.getFormData(metadataList,metadataModel.getEntityname(), formbind.getDataid());
					} catch (Exception e) {
						logger.error(e,e);
					}
				}
			}
			//循环组件
			for(SysEngineIformMap semm : maplist){
				//表单域
				String field = semm.getFieldName(); 
				String value = "";
				if(formdata!=null){
					if(formdata.get(field)!=null){
						Object obj = formdata.get(field);
						if(obj instanceof Date){
							int hours = ((Date) obj).getHours();
							int minutes = ((Date) obj).getMinutes();
							if(hours==0&&minutes==0){
								SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd");
								value = dateformat1.format((Date)obj);
							}else{
								SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								value = dateformat1.format((Date)obj);
							}
							
						}else{
							value = formdata.get(field).toString();
						}
						
					}
				}
				String uiDefine = "";
				//设置字段权限状态参数，默认为只读
				int status = IFormStatusConstants.FORM_FIELD_READ;
				//===============================判断表单权限===========================================
				if(formStatus.equals(IFormStatusConstants.FORM_PAGE_MODIFY)){
					try{
						//判断字段权限
						if(psfmList!=null){
							ProcessStepFormMap psfm = (ProcessStepFormMap)psfmList.get(formid+field);
							if(psfm!=null){ 
								if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_MODIFY)){//编辑状态
									status = IFormStatusConstants.FORM_FIELD_MODIFY;
								}else if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_READONLY)){
									status = IFormStatusConstants.FORM_FIELD_READ;
								}else if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_HIDDEN)){
									status = IFormStatusConstants.FORM_FIELD_HIDDEN;
								} 
							}
						}else{
							status = IFormStatusConstants.FORM_FIELD_MODIFY;
						}
					}catch(Exception e){
						logger.error(e,e);
					}
				}
				String temp = value;
				//获得域描述
				uiDefine = this.getMobileUICompoment(sysEngineIform,metadataModel,taskParam,instanceid,field,value,status);
//					if(!temp.equals("")){  
				//构建移动表单表格
				mobileHTML.append(MobilePageUtil.buildMobileHTML(semm, uiDefine, ""));
//					}
			} 
			mobileHTML.append("</div>");
			//获取子表信息
			List<SysEngineSubform>  subTablelist = sysEngineSubformDAO.getDataList(formid);
			for(SysEngineSubform subform:subTablelist){
				if(taskId==null||"".equals(taskId)){
					taskId = "0";
				}
				mobileHTML.append("<a href=\"javascript:showSubform(").append(taskId).append(",").append(instanceid).append(",").append(subform.getId()).append(",'").append(subform.getTitle()).append("');\" data-role=\"button\" data-icon=\"grid\">").append(subform.getTitle()).append("</a>").append("\n");
			} 
			//
		} catch (Exception e) {
			mobileHTML.append("数据异常，请联系管理员!<br>["+e.getMessage()+"]");
			logger.error(e,e);
		}  
		return mobileHTML.toString();
	}
	
	/**
	 * 获取表单行记录ID【适用与主表】
	 * @param formid
	 * @param instanceid
	 * @return
	 */
	public Long getDataid(Long formid,Long instanceid,Long engineType){
		Long dataid = new Long(0);
		if(instanceid!=0){
			SysEngineIform iformModel = sysEngineIFormDAO.getSysEngineIformModel(formid);
			if(iformModel==null)return dataid;
			SysEngineFormBind formBind = iformBindDAO.getModel(formid, iformModel.getMetadataid(), instanceid,engineType);
			if(formBind!=null){
				dataid =formBind.getDataid();
			}
		}
		return dataid;
	}
	/**
	 * 获取表单行记录ID【适用与主表】
	 * @param formid
	 * @param instanceid
	 * @return
	 */
	public List<SysEngineFormBind> getDataids(Long formid,Long instanceid,Long engineType){
		 List<SysEngineFormBind> list = new ArrayList();
		Long dataid = new Long(0);
		if(instanceid!=0){
			SysEngineIform iformModel = sysEngineIFormDAO.getSysEngineIformModel(formid);
			if(iformModel==null)return null;
			list = iformBindDAO.getBindList(formid, iformModel.getMetadataid(), instanceid,engineType);
		}
		return list;
	}
	/**
	 * 导出子表模板
	 */
	public void exportGridExcelModal(Long subformid,Long instanceid,HttpServletResponse response){
		HSSFWorkbook wb = new HSSFWorkbook();
		this.getTitleRow(wb, subformid);
		SysEngineIform ifromModel = sysEngineIFormDAO.getSysEngineIformModel(subformid);
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(ifromModel.getMetadataid());
		this.downExcel(wb, metadataModel.getEntitytitle(), response);
	}
	/**
	 * 导出子表数据
	 * @param subformid
	 * @param instanceid
	 * @param response
	 */
	public void exportGridExcelData(Long subformid,Long instanceid,HttpServletResponse response){
		HSSFWorkbook wb = new HSSFWorkbook();
		if(subformid==null) return;
		this.getTitleRow(wb, subformid);
		List list = this.getGridData(subformid, instanceid);
		if(instanceid>0){
			this.dataToExcel(list, wb);
		}else{
			this.dataToExcel(new ArrayList(), wb);
		}
		SysEngineIform ifromModel = sysEngineIFormDAO.getSysEngineIformModel(subformid);
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(ifromModel.getMetadataid());
		this.downExcel(wb, metadataModel.getEntitytitle(), response);
	}
	
	/**
	 * 子表excel导入
	 * @param modelId
	 * @param instanceId
	 * @param subformid
	 * @param file
	 * @param engineType
	 * @return
	 */
	public String subSheetExcelImp(Long modelId,Long instanceId,Long subformid,File file,Long engineType){
		String msg = "success";
		try {
				InputStream is = new FileInputStream(file);
				HSSFWorkbook hwk = new HSSFWorkbook(is);
				HSSFSheet sheet = hwk.getSheetAt(0);// 得到book第一个工作薄sheet
				// 第3行定义了导入动作
				int rowIndex = 0;
				HSSFRow row = sheet.getRow(rowIndex);
				HSSFCell cell = null;
					row = sheet.getRow(rowIndex);
					Hashtable fields = new Hashtable();
					for (short i = 0; i < row.getLastCellNum(); i++) {
						cell = row.getCell(i);
						if(cell==null||cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
							continue;
						}
						String fieldName = "";
						String temp = cell.getStringCellValue();
						if (temp.indexOf("<") > 0) {
							fieldName = temp.substring(0, cell.getStringCellValue().indexOf("<"));
						} else { 
							fieldName = temp;
						}
						fields.put(new Short(i), fieldName);
					}
					if(fields.size()<1){
						msg = "typeerror";
						return msg;
					}
					
					SysEngineSubform subFormModel = sysEngineSubformDAO.getModel(subformid);
					// 循环excel的数据
					rowIndex++;
					StringBuffer bindData;
					int dataNum = 0;
					int erroecount = 0;   //导入异常行数
					for (int i = rowIndex; i <= sheet.getLastRowNum(); i++) {
						row = sheet.getRow(i);
						bindData = new StringBuffer();
						HashMap rowdata = new HashMap();
						// 循环列
						for (short column = 0; column < row.getLastCellNum(); column++) {
							cell = row.getCell(column);
							String value = "";
							Object ofieldName = fields.get(new Short(column));
							if (ofieldName == null)
								continue;
							String fieldName = ((String) ofieldName).toUpperCase();
							if (cell == null) {
								value = "";
							} else {
								if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
									
									if (HSSFDateUtil.isCellDateFormatted(cell)) {// 日期
										value = UtilDate.dateFormat(cell.getDateCellValue());
									} else {
										value = Double.toString(cell.getNumericCellValue());
										// 排除excel的幂
										if (value != null) {
											if (value.toUpperCase().indexOf("E") > 0) {
												//  解决常整型数据出现科学计数法问题
												value = NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
												value = value.replaceAll(",", "");
											}
											// 小数位数
											if (value.indexOf(".0") > 0 && value.lastIndexOf("0") == value.length() - 1) {
												value = value.substring(0, value.length() - 2);
											} 
										} 
									}
								}else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
									try {
										 value = String.valueOf(cell.getNumericCellValue());
									} catch (Exception e) {
										 value = String.valueOf(cell.getRichStringCellValue());
									}
								} else {
									value = cell.getStringCellValue();
									if (value == null)
										value = "";
								}
							}
							value = value.trim();
							rowdata.put(fieldName, value);
						}
						if(fields.size()<1){
							continue;
						}else{
							//添加至子表(excel导入，统一不计入变更日志)
							Long flag = this.saveForm(modelId,subFormModel.getSubformid(), instanceId, rowdata,false,engineType);
							if(flag<0){
								erroecount++;
							}
							dataNum++;
						}
					} 
					if(dataNum==0){
						msg = "error";
					}
					if(erroecount>0){
						msg = "第"+erroecount+"条数据导入异常!";
					}
		} catch (FileNotFoundException e) {
			msg = "error";
			logger.error(e,e);
		}catch (Exception e) {
			msg = "error";
			logger.error(e,e);
		}
		return msg;
	}
	/**
	 * 把数据装载到EXCEL表中
	 * @param list
	 * @param wb
	 */
	public void dataToExcel(List list,HSSFWorkbook wb){
		if(wb!=null){
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow titleRow = sheet.getRow(0);
			if(list!=null&&titleRow!=null){
				int rowCount =1;
				for(ListIterator iterator =list.listIterator();iterator.hasNext();){
					HashMap hash = (HashMap)iterator.next();
					HSSFRow newRow = sheet.createRow(rowCount);
					HSSFCell indexCell = newRow.createCell(0);
					indexCell.setCellValue(rowCount);
					rowCount++;
					//根据第一行的列字段来定位每一列存的列宽
					for(int i=1;i<titleRow.getLastCellNum();i++){
						String value = titleRow.getCell(i).getStringCellValue();
						if(value!=null&&value.indexOf("<")>0){
							String columnName = value.substring(0, value.indexOf("<"));
							String columnValue ="";
							Object obj = hash.get(columnName);
							if(obj!=null && obj instanceof java.sql.Date){
								SimpleDateFormat sf =   new SimpleDateFormat("yyyy-MM-dd"); 
								columnValue = sf.format(((java.sql.Date)hash.get(columnName)).getTime());
								HSSFCell cell = newRow.createCell(i);
								cell.setCellValue(columnValue==null?"":columnValue);
							}else if(obj!=null && obj instanceof BigDecimal){
								HSSFCell cell = newRow.createCell(i);
								BigDecimal bd = (BigDecimal)obj;
								double doubleValue = bd.doubleValue();
								cell.setCellValue(doubleValue);
							}else if(obj!=null && obj instanceof String){
								HSSFCell cell = newRow.createCell(i);
								columnValue = obj.toString();
								cell.setCellValue(columnValue);
							}else{
								if(obj == null){
									columnValue = "";
								}else{
									columnValue = obj.toString();
								}
								HSSFCell cell = newRow.createCell(i);
								cell.setCellValue(columnValue);
							}
						}
					}
				}
			}
		}
		
	}
	/**
	 * 取子表数据
	 * @param subformid
	 * @param instanceid
	 * @return 
	 */
	public List getGridData(Long subformid,Long instanceid){
		SysEngineIform ifromModel = sysEngineIFormDAO.getSysEngineIformModel(subformid);
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(ifromModel.getMetadataid());
		List<SysEngineIformMap> subFormMapList = sysEngineIFormMapDAO.getDataList(subformid);
		List list=null;
		try { 
			int subformdatasize = sysEngineSubformDAO.getSubFormDataSize(ifromModel,subFormMapList, metadataModel.getEntityname(), instanceid);
			list = sysEngineSubformDAO.getSubFormDataList(ifromModel,subFormMapList, metadataModel.getEntityname(), instanceid,subformdatasize,0,null);
		} catch (Exception e) {  
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
		return list;
	}
	
	/**
	 * 取子表数据
	 * @param subformid
	 * @param instanceid
	 * @return 
	 */
	public HashMap getGridDataItem(Long subformid,Long instanceid,Long dataid){
		HashMap hashData = null; 
		SysEngineIform ifromModel = sysEngineIFormDAO.getSysEngineIformModel(subformid);
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(ifromModel.getMetadataid());
		List<SysEngineIformMap> subFormMapList = sysEngineIFormMapDAO.getDataList(subformid);
		List list=null;
		try { 
			hashData = sysEngineSubformDAO.getSubFormDataListItem(ifromModel, subFormMapList,  metadataModel.getEntityname(), instanceid, dataid);
		} catch (Exception e) {  
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
		return hashData;
	}
	/**
	 * 把生成的excel导出到本地
	 * @param wb
	 * @param fileName
	 * @param response
	 */
	private void downExcel(HSSFWorkbook wb,String fileName,HttpServletResponse response){
		try {
			// 添加头信息，为"文件下载/另存为"对话框指定默认文件名 
			String disposition = "attachment;filename=" + URLEncoder.encode(fileName+".xls", "UTF-8");
			// 指定返回的是一个不能被客户端读取的流，必须被下载   
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			BufferedOutputStream bos = null; 
			bos = new BufferedOutputStream(response.getOutputStream());
			wb.write(bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
	}
	/**
	 * 构件Excel标题
	 * @param row
	 * @param style
	 * @param subform
	 * @return
	 */
	private void getTitleRow(HSSFWorkbook wb,Long subformid){
		
		List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(subformid);
		SysEngineIform ifromModel = sysEngineIFormDAO.getSysEngineIformModel(subformid);
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(ifromModel.getMetadataid());
		HSSFSheet sheet = wb.createSheet(metadataModel.getEntityname());
		HSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		HSSFRow titleRow = sheet.createRow((short) 0);
		HSSFCell cell = null;
		if(titleRow!=null){
			cell = titleRow.createCell(0);
			cell.setCellStyle(style);
			for(int i=0;i<list.size();i++){
				SysEngineIformMap mapModel = list.get(i);
				String name = mapModel.getFieldTitle();
				String id = mapModel.getFieldName();
				int column = i+1;
				cell = titleRow.createCell(column);
				String value = id+"<"+name+">";
				cell.setCellType(1);
				cell.setCellValue(value);
				cell.setCellStyle(style);
				short colLength = (short) ((value.length()) * 256 * 2);
				if (sheet.getColumnWidth(column) < colLength) {
					sheet.setColumnWidth(column, colLength);
				}
			}
		}
	}
	
	/**
	 * 获得子表脚本
	 * @param subform
	 * @return
	 */
	public String getOnEditSubFormScript(SysEngineSubform subform,Long instanceid,Long formStatus,HashMap<String,ProcessStepFormMap> psfmList,Long engineType,Map params){
		boolean formIsModify = false;
		boolean subformIsModify = false;
		if(formStatus==IFormStatusConstants.FORM_PAGE_MODIFY){
			formIsModify = true;
		}
		Long type = subform.getType();//0是编辑子表，1是视窗子表
		lastsel = "lastsel"+subform.getId(); 
		newId = "newId"+subform.getId();
		subformId = subform.getId().toString();
		subformKey = "subform"+subform.getSubtablekey();
		subformNavGrid = "prowed"+subform.getSubtablekey();
		subformSaveArray = "saveArray"+subform.getId();
		newRowFunctionname = "newRow"+subform.getId();
		selectRowsFunctionname = "selectRows"+subform.getId(); 
		copyRowFunctionname = "copyRow"+subform.getId();
		insertFunctionname = "insertRow"+subform.getId();
		newRowDefaultValue = "newRowDefaultValue"+subform.getId();
		saveSubReportDataFunctionname = "saveSubReportData"+subform.getId();
		saveSubReportGridFunctionname = "saveSubReportGrid"+subform.getId();
		deleteRowsFunctionname = "deleteRows"+subform.getId();
		deleteRowDataFunctionname = "deleteRowData"+subform.getId();
		subformColLenValidateFunName = "colLenValidate"+subform.getId();
		subformColLenValidateFunName = WrapScriptUtil.wrapFunctionName(subformColLenValidateFunName);
		subformColLenValidateMsg = "colLenValidateMsg"+subform.getId();
		subformColLenValidateIndex = "colLenValidateIndex"+subform.getId();
		subformInitVariableFunName = "initVariable"+subform.getId();
		subformResetUrlFunName = "reSetUrl"+subform.getId();
		subformOpenFormFunName = "openForm"+subform.getId();
		StringBuffer script = new StringBuffer();
		 
//		script.append("<div style=\"border:1px solid #efefef;text-align:left\">").append("\n");

		script.append("<script>").append("\n");
		//定义script变量
		script.append("var "+lastsel+";").append("\n");
		script.append("var "+newId+"=0;").append("\n");
		script.append("var "+subformSaveArray+" = new Array();").append("\n");
		script.append("var "+subformColLenValidateMsg+"='';").append("\n");
		script.append("var "+subformColLenValidateIndex+"=0;").append("\n");
		//子表所用到的外观组件script
		script.append(this.getSubFormUIScript(subform, instanceid)).append("\n");
		//子表增删改查script
		script.append(this.getSubformCrudScript(subform,instanceid,type,engineType)).append("\n");
		//校验列输入文本是否超长script
		script.append(this.getColLengthValidate(subform, instanceid)).append("\n");
		script.append("jQuery(\"#subform").append(subform.getSubtablekey()).append("\").jqGrid({").append("\n");
		//获得数据URL
		if(instanceid!=0){
			script.append("	url:'").append("subform_load.action?instanceId=").append(instanceid).append("&subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("',").append("\n");
		}else{
			script.append("	url:'").append("subform_load.action?instanceId=").append(instanceid).append("&subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("',").append("\n");
		}
		//获得列标题
		script.append("	colNames:").append(this.getSubFormTitle(subform)).append(",").append("\n");
		script.append("	colModel:").append(this.getcolModel(subform,instanceid,formStatus,psfmList)).append(",").append("\n");
		script.append("	rowNum:50,").append("\n"); 
		script.append("	rowList:[30,50,100,150],").append("\n");
		script.append("	loadui:'block',").append("\n");
		script.append("	rownumbers:true,").append("\n");
		//=============================表单权限判断===============================================
		if(formStatus.equals(IFormStatusConstants.FORM_PAGE_MODIFY)){
			script.append("	multiselect: true,").append("\n");
		}else{
			script.append("	multiselect: false,").append("\n");
		}
		//===============================END========================================================
		String sortname = "";
		String sortType = "";
		if(subform.getOrderColumn()!=null&&!subform.getOrderColumn().equals("")){
			sortname = subform.getOrderColumn();
		}
		if(subform.getOrderType()!=null&&!subform.getOrderType().equals("")){
			sortType = subform.getOrderType();
		}
		
		if(sortname.equals(""))sortname="id";
		if(sortType.equals(""))sortname="desc";
		
		script.append("	sortname: \""+sortname+"\",").append("\n");
		script.append("	viewrecords: true,").append("\n");
		script.append("	resizable:true,").append("\n");
		script.append("	datatype: \"json\",").append("\n");
		script.append("	mtype: \"POST\",").append("\n");
		Long autoWidth = subform.getAutowidth();
		Long gridWidth = subform.getGridwidth();
		if(autoWidth!=null&&gridWidth!=null&&gridWidth>0&&autoWidth==1){
			script.append("	width: '"+gridWidth+"',").append("\n");
		}else{
			script.append("	autowidth:true,").append("\n");
		}
		script.append("	height: \"100%\",").append("\n"); 
		//当列宽之和大于表格设定的宽度时，生成横向滚动条
		script.append("	shrinkToFit: false,").append("\n");
		script.append("	sortorder: \""+sortType+"\",").append("\n");
		script.append("	pager: '#prowed").append(subform.getSubtablekey()).append("',").append("\n");
		script.append("	prmNames:{rows:\"page.pageSize\",page:\"page.curPageNo\",sort:\"page.orderBy\",order:\"page.order\"},").append("\n");
		script.append("	jsonReader: {").append("\n");
		script.append("		root: \"dataRows\",").append("\n");
		script.append("		page: \"curPage\",").append("\n");
		script.append("		total: \"totalPages\",").append("\n");
		script.append("		records: \"totalRecords\",").append("\n");
		script.append("		repeatitems: false,").append("\n");
		script.append("		id: \"id\",").append("\n");
		script.append("		userdata: \"userdata\"").append("\n");
		script.append("	}").append("\n");
		
		//判断子表操作权限，如果子表设置，全部字段为只读或隐藏，则隐藏工具条
		if(formStatus.equals(IFormStatusConstants.FORM_PAGE_MODIFY)){
			formIsModify = checkSubSheetBtnPurview(subform,psfmList); 
		}
		if(formIsModify){//判断编辑权限 
			//只有编辑子表才能直接对编辑子表，视窗子表是弹出窗口编辑
//			if(0==type){
				script.append(",ondblClickRow: function(id){").append("\n");
				script.append("	if(id && id!=="+lastsel+"){").append("\n");
				script.append("		jQuery('#subform").append(subform.getSubtablekey()).append("').jqGrid('editRow',"+lastsel+",false);").append("\n");
				script.append("		jQuery('#subform").append(subform.getSubtablekey()).append("').jqGrid('editRow',id,false);").append("\n");
				script.append("			"+lastsel+"=id;").append("\n");
				script.append("			"+subformSaveArray+".push(id);").append("\n");
				script.append("		}").append("\n");
				script.append("	},").append("\n"); 
//			}else{
//				script.append(",ondblClickRow: function(rowid,iCol,cellcontent,e){").append("\n");
//				script.append("		"+subformOpenFormFunName+"(rowid);").append("\n"); 
//				script.append("	},").append("\n"); 
//			} 
			if(engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_DEM)){
				String modelId = ObjectUtil.getString(params.get("demId"));
				script.append("	editurl:'subformSaveItem.action?modelId=").append(modelId).append("&engineType=").append(engineType).append("&instanceId=").append(instanceid).append("&subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("'\n");
			}else{
				String actDefId = "";
				String actStepDefId = null;
				String taskId = null;
				String excutionId = null;
				
				Object obj = params.get("actDefId");
				if(obj instanceof String){
					actDefId = obj.toString();
				}else if(obj instanceof String[]){
					String[] objgroup = (String[])obj;
					if(objgroup!=null&&objgroup.length>0){
						actDefId = objgroup[0];
					}
				}
				
				obj = params.get("actStepDefId");
				if(obj instanceof String){
					actStepDefId = obj.toString();
				}else if(obj instanceof String[]){
					String[] objgroup = (String[])obj;
					if(objgroup!=null&&objgroup.length>0){
						actStepDefId = objgroup[0];
					}
				}
				obj = params.get("taskId");
				if(obj instanceof String){
					taskId = obj.toString();
				}else if(obj instanceof String[]){
					String[] objgroup = (String[])obj;
					if(objgroup!=null&&objgroup.length>0){
						taskId = objgroup[0];
					}
				}
				obj = params.get("excutionId");
				if(obj instanceof String){
					excutionId = obj.toString();
				}else if(obj instanceof String[]){
					String[] objgroup = (String[])obj;
					if(objgroup!=null&&objgroup.length>0){
						excutionId = objgroup[0];
					}
				}  
				script.append("	editurl:'subformSaveItem.action?actDefId=").append(actDefId).append("&actStepDefId=").append(actStepDefId).append("&taskId=").append(taskId).append("&excutionId=").append(excutionId).append("&engineType=").append(engineType).append("&instanceId=").append(instanceid).append("&subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("'\n");

			}
		}
		script.append("});").append("\n"); 
		//script.append("jQuery(\"#").append(subformKey).append("\").jqGrid('navGrid',\"#").append(subformNavGrid).append("\",{edit:false,closeOnEscape:false,search:false,add:false,del:false});").append("\n");
		script.append("</script>").append("\n");
		StringBuffer subSheetToolbar = new StringBuffer(); 
		
		StringBuffer html = new StringBuffer(); 
		html.append("<div class=\"subReportTable\" id=\"subTableDiv").append(subform.getId()).append("\">").append("\n");
		html.append("			<div  id=\"subTableTitle").append(subform.getId()).append("\" class=\"subsheet_title\" > <div class=\"subsheet_title_light\"><span class=\"subsheet_title_icon\">"+subform.getTitle()+"</span></div></div>").append("\n");
		if(formIsModify){ 
			boolean toolbarIsShow = false;
			subSheetToolbar.append("			<div  id=\"subTableTools").append(subform.getId()).append("\"  class=\"tools_nav\">"); 
			//数据字典
			String dicBtn = "选择";
			SysDictionaryBaseinfo sdb = sysDictionaryRuntimeService.getSysDictionaryBaseInfoDAO().getModel(subform.getDictionaryId());
			if(sdb!=null){
				dicBtn = sdb.getDicName();
			}
			if(subform.getDictionaryId()!=null&&!subform.getDictionaryId().equals(new Long(0))){
				toolbarIsShow = true;
				subSheetToolbar.append("			<a href='###' id='").append(selectRowsFunctionname).append("' onclick=\"javascript:").append(selectRowsFunctionname).append("(").append(subform.getDictionaryId()).append(");return ;\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-dictionary\">").append(dicBtn).append("</a>").append("\n");
			}    
			//新增按钮
			if(subform.getIsAdd()!=null&&!subform.getIsAdd().equals(new Long(0))){
				toolbarIsShow = true;
				subSheetToolbar.append("			<a href='###' id='").append(newRowFunctionname).append("' onclick=\"javascript: ").append(newRowFunctionname).append("();return;\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-add\">新增</a>").append("\n");
			} 
//			subSheetToolbar.append("			<a href='###' id='").append(selectRowsFunctionname).append("' onclick=\"javascript:$('#"+subformKey+"').trigger('reloadGrid');\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-dictionary\">").append("刷新").append("</a>").append("\n");
			//视窗子表没有保存按钮，因为保存操作只在窗口中进行
//			if(type.equals(new Long(0))){ 
			if(subform.getIsSave()!=null&&!subform.getIsSave().equals(new Long(0))){
				toolbarIsShow = true;
				subSheetToolbar.append("								<a href='###' id='").append(saveSubReportDataFunctionname).append("' onclick=\"javascript:").append(saveSubReportDataFunctionname).append("();return;\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-save\">保存</a>").append("\n");
			}
			//subSheetToolbar.append("								<a href='###' onclick=\"javascript:$('#"+subformKey+"').trigger('reloadGrid');"+subformInitVariableFunName+"();return false;\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-reload\">刷新</a>").append("\n");
			//复制按钮
			if(subform.getIsCopy()!=null&&!subform.getIsCopy().equals(new Long(0))){ 
				toolbarIsShow = true;
				subSheetToolbar.append("								<a href='###' id='").append(copyRowFunctionname).append("' onclick=\"javascript:").append(copyRowFunctionname).append("();return;\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-copy\">复制</a>").append("\n");
			}  
			if(subform.getIsDel()!=null&&!subform.getIsDel().equals(new Long(0))){
				toolbarIsShow = true;
				subSheetToolbar.append("								<a href='###' id='").append(deleteRowsFunctionname).append("' onclick=\"javascript:").append(deleteRowsFunctionname).append("();return ;\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-cancel\">删除</a>").append("\n");
			}
			if(subform.getExcelImp()!=null&&!subform.getExcelImp().equals(new Long(0))){
				toolbarIsShow = true;
				subSheetToolbar.append("								<a href='###' onclick=\"javascript:openImpExcel(").append(instanceid).append(",").append(subform.getId()).append(",'").append(subform.getSubtablekey()).append("',").append(engineType).append(");return;\"   class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-excel-imp\">导入</a>").append("\n");
			}
			if(subform.getExcelExp()!=null&&!subform.getExcelExp().equals(new Long(0))){
				toolbarIsShow = true;
				subSheetToolbar.append("								<a href=\"subformExpExcelData.action?subformid="+subform.getSubformid()+"&instanceId="+instanceid+"\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-excel-exp\">导出</a>").append("\n");
			}
			subSheetToolbar.append("</div>");
			if(toolbarIsShow){
				html.append(subSheetToolbar);
			}
		}
		html.append("<table id='subform").append(subform.getSubtablekey()).append("'></table>").append("\n");
		html.append("<div id='prowed").append(subform.getSubtablekey()).append("'></div>").append("\n");
		html.append("	</div>").append("\n");
		html.append(script);
		return html.toString();
	}
	
	/**
	 * 获得子表脚本
	 * @param subform
	 * @return
	 */
	public String getOnFormSubFormScript(SysEngineSubform subform,Long instanceid,Long formStatus,HashMap<String,ProcessStepFormMap> psfmList,Long engineType,Map params){
		boolean formIsModify = false;
		boolean subformIsModify = false;
		if(formStatus==IFormStatusConstants.FORM_PAGE_MODIFY){
			formIsModify = true;
		}
		Long type = subform.getType();//0是编辑子表，1是视窗子表
		lastsel = "lastsel"+subform.getId(); 
		newId = "newId"+subform.getId(); 
		subformKey = "subform"+subform.getSubtablekey();
		subformNavGrid = "prowed"+subform.getSubtablekey();
		subformSaveArray = "saveArray"+subform.getId();
		newRowFunctionname = "newRow"+subform.getId();
		selectRowsFunctionname = "selectRows"+subform.getId(); 
		copyRowFunctionname = "copyRow"+subform.getId();
		insertFunctionname = "insertRow"+subform.getId();
		newRowDefaultValue = "newRowDefaultValue"+subform.getId();
		saveSubReportDataFunctionname = "saveSubReportData"+subform.getId();
		saveSubReportGridFunctionname = "saveSubReportGrid"+subform.getId();
		deleteRowsFunctionname = "deleteRows"+subform.getId();
		deleteRowDataFunctionname = "deleteRowData"+subform.getId();
		subformColLenValidateFunName = "colLenValidate"+subform.getId();
		subformColLenValidateFunName = WrapScriptUtil.wrapFunctionName(subformColLenValidateFunName);
		subformColLenValidateMsg = "colLenValidateMsg"+subform.getId();
		subformColLenValidateIndex = "colLenValidateIndex"+subform.getId();
		subformInitVariableFunName = "initVariable"+subform.getId();
		subformResetUrlFunName = "reSetUrl"+subform.getId();
		subformOpenFormFunName = "openForm"+subform.getId();
		StringBuffer script = new StringBuffer();
		
//		script.append("<div style=\"border:1px solid #efefef;text-align:left\">").append("\n");
		
		script.append("<script>").append("\n");
		//定义script变量
		script.append("var "+lastsel+";").append("\n");
		script.append("var "+newId+"=0;").append("\n");
		script.append("var "+subformSaveArray+"=new Array();").append("\n");
		script.append("var "+subformColLenValidateMsg+"='';").append("\n");
		script.append("var "+subformColLenValidateIndex+"=0;").append("\n");
		//子表所用到的外观组件script
		script.append(this.getSubFormUIScript(subform, instanceid)).append("\n");
		//子表增删改查script
		script.append(this.getOnFormSubformCrudScript(subform,instanceid,type,engineType)).append("\n");
		//校验列输入文本是否超长script
		script.append(this.getColLengthValidate(subform, instanceid)).append("\n");
		script.append("jQuery(\"#subform").append(subform.getSubtablekey()).append("\").jqGrid({").append("\n");
		//获得数据URL
		if(instanceid!=0){
			script.append("	url:'").append("subform_load.action?engineType=").append(engineType).append("&instanceId=").append(instanceid).append("&subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("',").append("\n");
		}else{
			script.append("	url:'").append("subform_load.action?engineType=").append(engineType).append("&instanceId=").append(instanceid).append("&subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("',").append("\n");
		}
		//获得列标题
		script.append("	colNames:").append(this.getSubFormTitle(subform)).append(",").append("\n");
		script.append("	colModel:").append(this.getcolModel(subform,instanceid,formStatus,psfmList)).append(",").append("\n");
		script.append("	rowNum:50,").append("\n"); 
		script.append("	rowList:[30,50,100,150],").append("\n");
		script.append("	loadui:'block',").append("\n");
		script.append("	rownumbers:true,").append("\n");
		//=============================表单权限判断===============================================
		if(formStatus.equals(IFormStatusConstants.FORM_PAGE_MODIFY)){
			script.append("	multiselect: true,").append("\n");
		}else{
			script.append("	multiselect: false,").append("\n");
		}
		//===============================END========================================================
		String sortname = "";
		String sortType = "";
		if(subform.getOrderColumn()!=null&&!subform.getOrderColumn().equals("")){
			sortname = subform.getOrderColumn();
		}
		if(subform.getOrderType()!=null&&!subform.getOrderType().equals("")){
			sortType = subform.getOrderType();
		}
		
		if(sortname.equals(""))sortname="id";
		if(sortType.equals(""))sortname="desc";
		
		script.append("	sortname: \""+sortname+"\",").append("\n");
		script.append("	viewrecords: true,").append("\n");
		script.append("	resizable:true,").append("\n");
		script.append("	datatype: \"json\",").append("\n");
		script.append("	mtype: \"POST\",").append("\n");
		Long autoWidth = subform.getAutowidth();
		Long gridWidth = subform.getGridwidth();
		if(autoWidth!=null&&gridWidth!=null&&gridWidth>0&&autoWidth==1){
			script.append("	width: '"+gridWidth+"',").append("\n");
		}else{
			script.append("	autowidth:true,").append("\n");
		}
		script.append("	height: \"100%\",").append("\n"); 
		//当列宽之和大于表格设定的宽度时，生成横向滚动条
		script.append("	shrinkToFit: false,").append("\n");
		script.append("	sortorder: \""+sortType+"\",").append("\n");
		script.append("	pager: '#prowed").append(subform.getSubtablekey()).append("',").append("\n");
		script.append("	prmNames:{rows:\"page.pageSize\",page:\"page.curPageNo\",sort:\"page.orderBy\",order:\"page.order\"},").append("\n");
		script.append("	jsonReader: {").append("\n");
		script.append("		root: \"dataRows\",").append("\n");
		script.append("		page: \"curPage\",").append("\n");
		script.append("		total: \"totalPages\",").append("\n");
		script.append("		records: \"totalRecords\",").append("\n");
		script.append("		repeatitems: false,").append("\n");
		script.append("		id: \"id\",").append("\n");
		script.append("		userdata: \"userdata\"").append("\n");
		script.append("	}").append("\n");
		
		//判断子表操作权限，如果子表设置，全部字段为只读或隐藏，则隐藏工具条
		if(formStatus.equals(IFormStatusConstants.FORM_PAGE_MODIFY)){
			formIsModify = checkSubSheetBtnPurview(subform,psfmList); 
		}
			//只有编辑子表才能直接对编辑子表，视窗子表是弹出窗口编辑
			script.append(",ondblClickRow: function(rowid,iCol,cellcontent,e){").append("\n");
			script.append("		"+subformOpenFormFunName+"(rowid);").append("\n"); 
			script.append("	}").append("\n"); 
		script.append("});").append("\n"); 
		//script.append("jQuery(\"#").append(subformKey).append("\").jqGrid('navGrid',\"#").append(subformNavGrid).append("\",{edit:false,closeOnEscape:false,search:false,add:false,del:false});").append("\n");
		script.append("</script>").append("\n"); 
		StringBuffer subSheetToolbar = new StringBuffer(); 
		
		StringBuffer html = new StringBuffer(); 
		html.append("<div class=\"subReportTable\" id=\"subTableDiv").append(subform.getId()).append("\">").append("\n");
		html.append("			<div  id=\"subTableTitle").append(subform.getId()).append("\" class=\"subsheet_title\" > <div class=\"subsheet_title_light\"><span class=\"subsheet_title_icon\">"+subform.getTitle()+"</span></div></div>").append("\n");
		if(formIsModify){ 
			boolean toolbarIsShow = false;
			subSheetToolbar.append("			<div  id=\"subTableTools").append(subform.getId()).append("\"  class=\"tools_nav\">"); 
			//数据字典
			if(subform.getDictionaryId()!=null&&!subform.getDictionaryId().equals(new Long(0))){
				toolbarIsShow = true;
				subSheetToolbar.append("			<a href='###' id='").append(selectRowsFunctionname).append("' onclick=\"javascript:").append(selectRowsFunctionname).append("(").append(subform.getDictionaryId()).append(");return ;\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-dictionary\">").append("选择").append("</a>").append("\n");
			}    
			//新增按钮
			if(subform.getIsAdd()!=null&&!subform.getIsAdd().equals(new Long(0))){
				toolbarIsShow = true;
				subSheetToolbar.append("			<a href='###' id='").append(newRowFunctionname).append("' onclick=\"javascript: ").append(newRowFunctionname).append("();return;\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-add\">新增</a>").append("\n");
			} 
			
			//subSheetToolbar.append("								<a href='###' onclick=\"javascript:$('#"+subformKey+"').trigger('reloadGrid');"+subformInitVariableFunName+"();return false;\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-reload\">刷新</a>").append("\n");
			//复制按钮
//			if(subform.getIsCopy()!=null&&!subform.getIsCopy().equals(new Long(0))){ 
//				toolbarIsShow = true;
//				subSheetToolbar.append("								<a href='###' id='").append(copyRowFunctionname).append("' onclick=\"javascript:").append(copyRowFunctionname).append("();return;\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-copy\">复制</a>").append("\n");
//			}  
			if(subform.getIsDel()!=null&&!subform.getIsDel().equals(new Long(0))){
				toolbarIsShow = true;
				subSheetToolbar.append("								<a href='###' id='").append(deleteRowsFunctionname).append("' onclick=\"javascript:").append(deleteRowsFunctionname).append("();return ;\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-cancel\">删除</a>").append("\n");
			}
			if(subform.getExcelImp()!=null&&!subform.getExcelImp().equals(new Long(0))){
				toolbarIsShow = true;
				subSheetToolbar.append("								<a href='###' onclick=\"javascript:openImpExcel(").append(instanceid).append(",").append(subform.getId()).append(",'").append(subform.getSubtablekey()).append("',").append(engineType).append(");return;\"   class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-excel-imp\">导入</a>").append("\n");
			}
			if(subform.getExcelExp()!=null&&!subform.getExcelExp().equals(new Long(0))){
				toolbarIsShow = true;
				subSheetToolbar.append("								<a href=\"subformExpExcelData.action?subformid="+subform.getSubformid()+"&instanceId="+instanceid+"\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-excel-exp\">导出</a>").append("\n");
			}
			subSheetToolbar.append("</div>");
			if(toolbarIsShow){
				html.append(subSheetToolbar);
			}
		}
		html.append("<table id='subform").append(subform.getSubtablekey()).append("'></table>").append("\n");
		html.append("<div id='prowed").append(subform.getSubtablekey()).append("'></div>").append("\n");
		html.append("	</div>").append("\n");
		html.append(script);
		return html.toString();
	}
	/**
	 * 获得子表脚本
	 * @param subform
	 * @return
	 */
	public String getExtSubFormScript(SysEngineSubform subform,Long instanceid,Long formStatus,HashMap<String,ProcessStepFormMap> psfmList,Long engineType,Map params){
		boolean formIsModify = false;
		boolean subformIsModify = false;
		if(formStatus==IFormStatusConstants.FORM_PAGE_MODIFY){
			formIsModify = true;
		}
		Long type = subform.getType();//0是编辑子表，1是视窗子表
		lastsel = "lastsel"+subform.getId(); 
		newId = "newId"+subform.getId(); 
		subformKey = "subform"+subform.getSubtablekey();
		subformNavGrid = "prowed"+subform.getSubtablekey();
		subformSaveArray = "saveArray"+subform.getId();
		newRowFunctionname = "newRow"+subform.getId();
		selectRowsFunctionname = "selectRows"+subform.getId(); 
		copyRowFunctionname = "copyRow"+subform.getId();
		insertFunctionname = "insertRow"+subform.getId();
		newRowDefaultValue = "newRowDefaultValue"+subform.getId();
		saveSubReportDataFunctionname = "saveSubReportData"+subform.getId();
		saveSubReportGridFunctionname = "saveSubReportGrid"+subform.getId();
		deleteRowsFunctionname = "deleteRows"+subform.getId();
		deleteRowDataFunctionname = "deleteRowData"+subform.getId();
		subformColLenValidateFunName = "colLenValidate"+subform.getId();
		subformColLenValidateFunName = WrapScriptUtil.wrapFunctionName(subformColLenValidateFunName);
		subformColLenValidateMsg = "colLenValidateMsg"+subform.getId();
		subformColLenValidateIndex = "colLenValidateIndex"+subform.getId();
		subformInitVariableFunName = "initVariable"+subform.getId();
		subformResetUrlFunName = "reSetUrl"+subform.getId();
		subformOpenFormFunName = "openForm"+subform.getId();
		StringBuffer script = new StringBuffer();
		
		script.append("<script type=\"text/javascript\" >").append("\n");
		//定义script变量
		script.append("jQuery(\"#subform").append(subform.getSubtablekey()).append("\").jqGrid({").append("\n");
		//获得数据URL
		if(instanceid!=0){
			script.append("	url:'").append("iwork_showTreeJson.action?engineType=").append(engineType).append("&instanceId=").append(instanceid).append("&subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("',").append("\n");
		}else{
			script.append("	url:'").append("iwork_showTreeJson.action?engineType=").append(engineType).append("&instanceId=").append(instanceid).append("&subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("',").append("\n");
		}
		script.append("	treedatatype: \"xml\",").append("\n");
		script.append("	mtype: \"POST\",").append("\n");
		script.append("colNames:[\"id\",\"名称\",\"数量\", \"借方\", \"贷方\",\"余额\"],").append("\n");
		script.append("colModel:[").append("\n");
		script.append("{name:'id',index:'id', width:1,hidden:true,key:true},").append("\n");
		script.append("{name:'name',index:'name', width:180},").append("\n");
		script.append("{name:'num',index:'acc_num', width:80, align:\"center\"},").append("\n");
		script.append(" {name:'debit',index:'debit', width:80, align:\"right\"}, ").append("\n");   
		script.append("{name:'credit',index:'credit', width:80,align:\"right\"},").append("\n");    
		script.append("{name:'balance',index:'balance', width:80,align:\"right\"}").append("\n");    
		script.append("],").append("\n");
		script.append("height:'auto',").append("\n");
		script.append("width:'800',").append("\n");
		script.append("pager : \"#prowed").append(subform.getSubtablekey()).append("\",").append("\n");
		script.append("treeGrid: true,").append("\n");
		script.append("shrinkToFit: true,").append("\n");
		script.append(" ExpandColumn : 'name'").append("\n"); 
		script.append("});").append("\n"); 
		script.append("jQuery(\"#").append(subformKey).append("\").jqGrid('navGrid',\"#").append(subformNavGrid).append("\",{edit:false,closeOnEscape:false,search:false,add:false,del:false});").append("\n");
		script.append("</script>").append("\n"); 
		StringBuffer subSheetToolbar = new StringBuffer(); 
		
		StringBuffer html = new StringBuffer(); 
		html.append("<div class=\"subReportTable\" id=\"subTableDiv").append(subform.getId()).append("\">").append("\n");
		html.append("			<div  id=\"subTableTitle").append(subform.getId()).append("\" class=\"subsheet_title\" > <div class=\"subsheet_title_light\"><span class=\"subsheet_title_icon\">"+subform.getTitle()+"</span></div></div>").append("\n");
		html.append("<table id='subform").append(subform.getSubtablekey()).append("'></table>").append("\n");
		html.append("<div id='prowed").append(subform.getSubtablekey()).append("'></div>").append("\n");
//		html.append("	</div>").append("\n");
		html.append(script);
		return html.toString();
	}
	/**
	 * 检查子表操作按钮权限
	 * @param psfmList
	 * @return
	 */
	private boolean checkSubSheetBtnPurview(SysEngineSubform subform,HashMap<String,ProcessStepFormMap> psfmList){
		boolean flag = false;
		if(psfmList!=null&&psfmList.size()>0){
			List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(subform.getSubformid());
			for(SysEngineIformMap model:list){
				ProcessStepFormMap psfm = (ProcessStepFormMap)psfmList.get(subform.getSubformid()+model.getFieldName());
				if(psfm!=null&&psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_MODIFY)){
					flag = true; 
					break;
				}
			}
		}else{ 
			flag = true;
		}
		
		return flag;
	}
	/**
	 * 取子表所用到的javascript，输出到页面
	 * @param subform
	 * @param instanceid
	 * @return
	 */
	private String getSubFormUIScript(SysEngineSubform subform,Long instanceid){
		List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(subform.getSubformid());
		SysEngineIform ifromModel = sysEngineIFormDAO.getSysEngineIformModel(subform.getSubformid());
		String script = "";
		if(ifromModel!=null){ 
			SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(ifromModel.getMetadataid());
			if(!scriptMap.containsKey(subformInitVariableFunName)){
				script+=(getInitVariableScript()==null?"":getInitVariableScript())+"\n";
				scriptMap.put(subformInitVariableFunName, "");
			}
			for(int i=0;i<list.size();i++){
				SysEngineIformMap mapModel = list.get(i);
				if(mapModel.getDisplayType()!=null){
					if(!scriptMap.containsKey(mapModel.getDisplayType())){
						SysEngineIformMap sysEngineIformMap = sysEngineIFormMapDAO.getModel(subform.getSubformid(), mapModel.getFieldName());
						SysEngineMetadataMap metadataMap = sysEngineMetadataMapDAO.getModel(metadataModel.getId(),mapModel.getFieldName());
						UIComponentInterface uicompone = IFormUIFactory.getUIInstance(metadataMap, sysEngineIformMap,"");
						String s = uicompone.getSubFormColumnModelScript(mapModel.getDisplayType());
						script+=(s==null?"":s)+"\n";
						scriptMap.put(mapModel.getDisplayType(), mapModel.getDisplayType());
					}
				}
			}
		}
		
		return script;
	}
	/**
	 * 视窗子表配置
	 * @return
	 */
	private String getOpenFormScript(){
		StringBuffer s = new StringBuffer();
		s.append("function "+subformOpenFormFunName+"(rowId){").append("\n");
		//弹出子表前刷新url和editurl
		s.append("	"+subformResetUrlFunName+"();").append("\n");
		s.append("	$('#"+subformKey+"').jqGrid(").append("\n");
		s.append("		'editGridRow',").append("\n");
		s.append("		rowId,").append("\n");
		s.append("  	{").append("\n");
		s.append("  		top:200,").append("\n");
		s.append("  		left:400,").append("\n");
		s.append("  		modal:true,").append("\n");
		s.append("  		reloadAfterSubmit:true,").append("\n");//onClose
		//关闭弹出子表前，刷新当前页面，以免弹出子表破坏主表单的页面元素
		s.append("  		onClose:function(){").append("\n");
		s.append("  			document.getElementById('blockPage').style.display='block';").append("\n");
		s.append("  			$('#").append(subformKey).append("').trigger('reloadGrid');").append("\n");
//		s.append("  			window.location.href='openFormPage.action?formid='+document.getElementById('formid').value+'&instanceId='+document.getElementById('instanceId').value;").append("\n");
		s.append("  			document.getElementById('blockPage').style.display='';").append("\n");
		s.append("  		},").append("\n");
		//提交前校验字段长度
		s.append("  		beforeSubmit: function(posdata,formid){").append("\n");
		s.append("  			if("+subformColLenValidateMsg+"!=null&&"+subformColLenValidateMsg+"!=''){").append("\n");
		s.append("  				var msg = "+subformColLenValidateMsg+";").append("\n");
		s.append("  				"+subformColLenValidateMsg+"='';").append("\n");
		s.append("  				return [false,msg];").append("\n");
		s.append("  			}").append("\n");
		s.append("  			return[true,''];").append("\n");
		s.append("  		}").append("\n");
		s.append("  	}").append("\n");
		s.append("  );").append("\n"); 
		s.append("}").append("\n");
		return s.toString();
	}
	/**
	 * 视窗子表配置
	 * @return
	 */
	private String getOnFormOpenFormScript(SysEngineSubform subform,Long instanceid,Long engineType){
		StringBuffer s = new StringBuffer();
		String modelType = "";
		s.append("function "+subformOpenFormFunName+"(rowId){").append("\n");
		s.append("var rd = $('#").append(subformKey).append("').jqGrid('getRowData',rowId);").append("\n");
		if(engineType!=null&&engineType.equals(new Long(0))){
			modelType = IFormStatusConstants.FORM_RUNTIME_TYPE_DEM;
			s.append("var pageUrl = 'subform_item_load.action?dataid='+rd.ID+'&oper=edit&subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("&instanceId=").append(instanceid).append("&engineType=").append(engineType).append("&modelType=").append(modelType).append("';\n");
		}else{
			modelType = IFormStatusConstants.FORM_RUNTIME_TYPE_PROCESS;
			s.append("var actDefId = parent.$(\"#actDefId\").val();").append("\n");
			s.append("var actStepDefId = parent.$(\"#actStepDefId\").val();").append("\n");
			s.append("var prcDefId = parent.$(\"#prcDefId\").val();").append("\n");
			s.append("var taskId = parent.$(\"#taskId\").val();").append("\n");
			s.append("var formId = parent.$(\"#formId\").val();").append("\n");
			s.append("var pageUrl = 'processRuntimeloadSubformPage.action?dataid='+rd.ID+'&actDefId='+actDefId+'&actStepDefId='+actStepDefId+'&prcDefId='+prcDefId+'&taskId='+taskId+'&stepformId='+formId+'&oper=edit&subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("&instanceId=").append(instanceid).append("&engineType=").append(engineType).append("&modelType=").append(modelType).append("';\n");
		}
		 
		s.append("	art.dialog.open(pageUrl,{").append("\n");
		s.append("		id:'subFormItemDlg',").append("\n");
		s.append("		title:\"编辑行\",").append("\n");
		s.append("		lock:true,").append("\n");
		s.append("		background: '#999',").append("\n");
		s.append("		opacity: 0.87,").append("\n");
		s.append("		width:'90%',").append("\n");
		s.append("		height:'90%',").append("\n");
		s.append("		onClose:function(){").append("\n");
		s.append("  			$('#").append(subformKey).append("').trigger('reloadGrid');").append("\n");
		s.append("		}").append("\n");
		s.append("  });").append("\n"); 
		
//		s.append("  $('#").append(subformKey).append("').trigger('reloadGrid');").append("\n");
		s.append("}").append("\n");
		return s.toString();
	}
	/**
	 * 初始化js变量
	 * @return
	 */
	private String getInitVariableScript(){
		StringBuffer s = new StringBuffer();
		s.append("function "+subformInitVariableFunName+"(){").append("\n");
		s.append("	"+lastsel+"='';").append("\n");
		s.append("  "+newId+"=0;").append("\n");
		s.append("  "+subformSaveArray+"=new Array();").append("\n");
		s.append("  "+subformColLenValidateMsg+"='';").append("\n");
		s.append("  "+subformColLenValidateIndex+"=0;").append("\n");
		s.append("}").append("\n");
		return s.toString();
	}
	/**
	 * 子表单选地址簿JS
	 * @return
	 */
	private String getPlugsScript(SysEngineSubform subform){
		StringBuffer s = new StringBuffer();
		List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(subform.getSubformid());
		Long type = subform.getType();//0是编辑子表，1是视窗子表
		for(int i=0;i<list.size();i++){
			SysEngineIformMap mapModel = list.get(i);
			String functionName = mapModel.getFieldName()+mapModel.getDisplayType()+subform.getId();
			if(IFormStatusConstants.FORM_FIELD_TYPE_RADIOBOOK.equals(mapModel.getDisplayType())||IFormStatusConstants.FORM_FIELD_TYPE_MULTIBOOK.equals(mapModel.getDisplayType())||IFormStatusConstants.FORM_FIELD_TYPE_DEPTBOOK.equals(mapModel.getDisplayType())){
				String referenceValue = mapModel.getDisplayEnum();//参考值
				boolean isOrg = false;
				boolean isRole = false;
				boolean isGroup = false;
				boolean parentDept = false;
				boolean currentDept = false;
				String startDept = "";
				String targetUserId = "";
				String targetUserName = "";
				String targetDeptId = "";
				String targetDeptName = "";
				String defaultField = mapModel.getFieldName();
				if (referenceValue != null && referenceValue.trim().length()>0) {
					JSONObject json = JsonUtil.strToJson(IFormUIComponentAbst.wrapJsonDisplayEnum(referenceValue.toUpperCase()));
					if(json!=null){
						if(json.containsKey("PARENTDEPT")){
							parentDept = (Boolean)json.get("PARENTDEPT");
						}
						if(json.containsKey("CURRENTDEPT")){
							currentDept = (Boolean)json.get("CURRENTDEPT");
						}
						if(json.containsKey("STARTDEPT")){
							startDept = (String)json.get("STARTDEPT");
						}
						if(json.containsKey("TARGETUSERID")){
							targetUserId = (String)json.get("TARGETUSERID");
						}
						if(json.containsKey("TARGETUSERNAME")){
							targetUserName = (String)json.get("TARGETUSERNAME");
						}
						if(json.containsKey("TARGETDEPTID")){
							targetDeptId = (String)json.get("TARGETDEPTID");
						}
						if(json.containsKey("TARGETDEPTNAME")){
							targetDeptName = (String)json.get("TARGETDEPTNAME");
						}
						if(json.containsKey("ISORG")){
							isOrg = (Boolean)json.get("ISORG");
						}
						if(json.containsKey("ISROLE")){
							isRole = (Boolean)json.get("ISROLE");
						}
						if(json.containsKey("ISROLE")){
							isGroup = (Boolean)json.get("ISROLE");
						}
					}
				}
				s.append("function "+WrapScriptUtil.wrapFunctionName(functionName)+"(element){").append("\n");//$(element).click(
				s.append("	$(element).click(function(){").append("\n");
				s.append("		var thisId = $(element).attr('id');").append("\n");
				s.append("		var rowIdPrefix = '';").append("\n");
				//编辑子表中每行的字段的id是rowid_columnName,而视窗子表为columnName
				if(0==type){
					s.append("		var rowId = thisId.substring(0,thisId.indexOf('_'));").append("\n");
					s.append("		rowIdPrefix = rowId+'_';").append("\n");
				}
				s.append("		var parentDept = '';").append("\n");
				s.append("		var currentDept = '';").append("\n");
				s.append("		var startDept = '';").append("\n");
				s.append("		var targetUserId = '';").append("\n");
				s.append("		var targetUserName = '';").append("\n");
				s.append("		var targetDeptId = '';").append("\n");
				s.append("		var targetDeptName = '';").append("\n");
				s.append("		var defaultField = '';").append("\n");
				if(parentDept){
					s.append("		parentDept = '"+parentDept+"';").append("\n");
				}
				if(currentDept){
					s.append("		currentDept = '"+currentDept+"';").append("\n");
				}
				if(startDept!=null&&!"".equals(startDept)){
					s.append("		startDept = '"+startDept+"';").append("\n");
				}
				if(targetUserId!=null&&!"".equals(targetUserId)){
					s.append("		targetUserId = rowIdPrefix+'"+targetUserId+"';").append("\n");
				}
				if(targetUserName!=null&&!"".equals(targetUserName)){
					s.append("		targetUserName = rowIdPrefix+'"+targetUserName+"';").append("\n");
				}
				if(targetDeptId!=null&&!"".equals(targetDeptId)){
					s.append("		targetDeptId = rowIdPrefix+'"+targetDeptId+"';").append("\n");
				}
				if(targetDeptName!=null&&!"".equals(targetDeptName)){
					s.append("		targetDeptName = rowIdPrefix+'"+targetDeptName+"';").append("\n");
				}
				if(defaultField!=null&&!"".equals(defaultField)){
					s.append("		defaultField = rowIdPrefix+'"+defaultField+"';").append("\n");
				}
				if("RadioBook".equals(mapModel.getDisplayType())){
					s.append("		radio_book(parentDept, currentDept, startDept, targetUserId, '', targetUserName, targetDeptId, targetDeptName, defaultField);").append("\n");
				}else if("MultiBook".equals(mapModel.getDisplayType())){
					s.append("		multi_book(parentDept, currentDept, startDept, targetUserId,'', targetUserName, targetDeptId, targetDeptName, defaultField);").append("\n");
				} else if(IFormStatusConstants.FORM_FIELD_TYPE_DEPTBOOK.equals(mapModel.getDisplayType())){
					s.append("		dept_book(parentDept, currentDept, startDept, targetUserId,'', targetUserName, targetDeptId, targetDeptName, defaultField);").append("\n");
				} 
				s.append("	});").append("\n");
				s.append("}").append("\n");
			}else if(IFormStatusConstants.FORM_FIELD_TYPE_DICTIONARYCHOOSER.equals(mapModel.getDisplayType())){
				s.append("function "+WrapScriptUtil.wrapFunctionName(functionName)+"(element){").append("\n");//$(element).click(
				s.append("	$(element).click(function(){").append("\n");
				s.append("		var thisId = $(element).attr('id');").append("\n");
				s.append("		var rowIdPrefix = '';").append("\n");
				//编辑子表中每行的字段的id是rowid_columnName,而视窗子表为columnName
				String referenceValue = mapModel.getDisplayEnum();//参考值
				//如果参考值中为UUID，则需要转换成id
				Long dictionaryId = new Long(0);
				
				if(referenceValue!=null&&!referenceValue.equals("")){
					try{
						//字典ID转换
						dictionaryId = Long.parseLong(referenceValue);
					}catch(Exception e){
						//通过UUID获取数据字典模型ID
						SysDictionaryBaseinfo sdb = sysDictionaryRuntimeService.getSysDictionaryBaseInfoDAO().getModel(referenceValue);
						if(sdb!=null){
							dictionaryId = sdb.getId();
						}
					}
				}
				if(0==type){ 
					s.append("		var rowId = thisId.substring(0,thisId.indexOf('_'));").append("\n");
					s.append("		rowIdPrefix = rowId+'_';").append("\n");
				}
				s.append("	openSubDictionary('").append(dictionaryId).append("',rowIdPrefix);").append("\n");
				s.append("	});").append("\n");
				s.append("}").append("\n");  
			}else if(IFormStatusConstants.FORM_FIELD_TYPE_WBS_DIRECTORY.equals(mapModel.getDisplayType())){
				s.append("function "+WrapScriptUtil.wrapFunctionName(functionName)+"(element){").append("\n");//$(element).click(
				s.append("	$(element).click(function(){").append("\n");
				s.append("		showWBSbook(this);").append("\n");
				s.append("	});").append("\n");
				s.append("}").append("\n");  
			}
		}
		return s.toString();
	}
	
	
	/**
	 * 子表增删改查所用到的script
	 * @param subform
	 * @param instanceid
	 * @param type
	 * @return
	 */
	private String getOnFormSubformCrudScript(SysEngineSubform subform,Long instanceid,Long type,Long engineType){
		StringBuffer script = new StringBuffer();
		script.append("var "+newRowDefaultValue+"="+this.getDefaultValueScript(subform, instanceid)+";").append("\n");//默认值
		
		if(subform.getIsAdd()!=null&&!subform.getIsAdd().equals(new Long(0))){
			script.append(this.getOnFormNewRowScript(subform,instanceid,engineType));//新增行 
		}
		//暂不支持字典 
//		if(subform.getDictionaryId()!=null&&!subform.getDictionaryId().equals(new Long(0))){
//			script.append(this.getSelectRowsScript(subform.getId(),subform.getDictionaryId()));//数据字典 
//		}  
		//判断复制按钮显示条件
		if(subform.getIsCopy()!=null&&!subform.getIsCopy().equals(new Long(0))){ 
			script.append(this.getOnformCopyRowScript(instanceid, subform.getSubformid()));//复制行
		}
		script.append(this.getDeleteRowsScript());//删除行
		script.append(this.getDeleteRowDataScript(instanceid, subform.getSubformid(),type));//删除行数据，由删除行方法调用
		script.append(this.getReSetGridUrlScript(subform.getSubformid(),engineType));//如果页面instanceid加载时，保存时重写子表url和editurl
		//加载组件脚本
		script.append(this.getPlugsScript(subform));
		script.append(this.getOnFormOpenFormScript(subform,instanceid,engineType));//视窗子表弹出表
		return script.toString();
	}
	
	/**
	 * 子表增删改查所用到的script
	 * @param subform
	 * @param instanceid
	 * @param type
	 * @return
	 */
	private String getSubformCrudScript(SysEngineSubform subform,Long instanceid,Long type,Long engineType){
		StringBuffer script = new StringBuffer();
		script.append("var "+newRowDefaultValue+"="+this.getDefaultValueScript(subform, instanceid)+";").append("\n");//默认值
		
		if(subform.getIsAdd()!=null&&!subform.getIsAdd().equals(new Long(0))){
			script.append(this.getNewRowScript(type));//新增行
		}
		if(subform.getDictionaryId()!=null&&!subform.getDictionaryId().equals(new Long(0))){
			script.append(this.getSelectRowsScript(subform.getId(),subform.getDictionaryId()));//数据字典 
		}  
		//判断复制按钮显示条件
		if(subform.getIsCopy()!=null&&!subform.getIsCopy().equals(new Long(0))){ 
			script.append(this.getCopyRowScript(instanceid, subform.getSubformid()));//复制行
		}
		script.append(this.getSaveSubReportScript());//保存子表
		script.append(this.getDeleteRowsScript());//删除行
		script.append(this.getDeleteRowDataScript(instanceid, subform.getSubformid(),type));//删除行数据，由删除行方法调用
		script.append(getSaveSubReportGridScript(type,subform.getTitle()));//主表保存事件调用的子表保存方f法
		script.append(this.getReSetGridUrlScript(subform.getSubformid(),engineType));//如果页面instanceid加载时，保存时重写子表url和editurl
		if(type!=0){
			script.append(this.getOpenFormScript());//视窗子表弹出表
		}
		//加载组件脚本
		script.append(this.getPlugsScript(subform));
		return script.toString();
	}
	
	/**
	 * 新增加一行时，需要带出默认值，格式如下：
	 * var dataRow = {   
	        id : 99,  
	        lastName : "Zhang",  
	        firstName : "San",  
	        email : "zhang_san@126.com",  
	        telNo : "0086-12345678"  
	    };  
	 * @param subform
	 * @param instanceid
	 * @return
	 */
	private String getDefaultValueScript(SysEngineSubform subform,Long instanceid){
		StringBuffer script = new StringBuffer();
		String s = script.toString();
		script.append("{").append("\n");
		script.append("	id:"+newId+",").append("\n");
		List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(subform.getSubformid());
		SysEngineIform ifromModel = sysEngineIFormDAO.getSysEngineIformModel(subform.getSubformid());
		if(ifromModel!=null){
			SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(ifromModel.getMetadataid());
			if(list==null||list.size()<=0)
				return ""; 
			for(int i=0;i<list.size();i++){
				SysEngineIformMap mapModel = list.get(i);
				String name = mapModel.getFieldName();
				String defaultValue = mapModel.getFieldDefault();
				//动态@标签
				if(defaultValue!=null&&defaultValue.indexOf("%") > -1){ 
					ExpressionParamsModel model = new ExpressionParamsModel();
					model.setFormid(subform.getIformId());
					model.setEntityname(metadataModel.getEntityname());
					model.setInstanceid(instanceid);
					UserContext uc =UserContextUtil.getInstance().getCurrentUserContext();
					model.setContext(uc);
					defaultValue = RuntimeELUtil.getInstance().convertMacrosValue(defaultValue,model);
				}
				if(defaultValue!=null&&!"".equals(defaultValue)){
					script.append("	"+name+":'"+defaultValue+"',").append("\n");
				}else{
					script.append("	"+name+":'',").append("\n");
				}
			}
			s = script.toString();
			if(s.lastIndexOf(",")>-1){
				s=s.substring(0, s.lastIndexOf(","))+"}";
			}
		}
		return s;
	}
	
	
	/**
	 * 增加行所调用的function
	 * @param type
	 * @return
	 */
	private String getSelectRowsScript(Long subid,Long dictionaryId){
		StringBuffer s = new StringBuffer(); 
		s.append("function "+selectRowsFunctionname+"(dictionaryId){").append("\n"); 
		s.append("		openSubGridDictionary(").append(dictionaryId).append(",'").append(subformKey).append("',").append(subid).append(");							").append("\n");
		s.append("}").append("\n");  
		s.append("function formatCurrency(num){").append("\n"); 
		s.append("		num = num.toString().replace(/\\$|\\,/g,'');").append("\n");
		s.append("		if(isNaN(num))num = \"0\";").append("\n");
		s.append("		sign = (num == (num = Math.abs(num)));").append("\n");
		s.append("		num = Math.floor(num*100+0.50000000001);").append("\n");
		s.append("		cents = num%100;").append("\n");
		s.append("		num = Math.floor(num/100).toString();").append("\n");
		s.append("		if(cents<10)cents = \"0\" + cents;").append("\n");
		s.append("		for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)").append("\n");
		s.append("		num = num.substring(0,num.length-(4*i+3))+','+").append("\n");
		s.append("		num.substring(num.length-(4*i+3)); ").append("\n");
		s.append("		return (((sign)?'':'-')  + num + '.' + cents); ").append("\n");
		s.append("}").append("\n");  
		return s.toString();
	}
	/**
	 * 增加行所调用的function
	 * @param type 
	 * @return
	 */
	private String getOnformCopyRowScript(Long instanceId,Long subFormId){
		StringBuffer s = new StringBuffer();
		s.append("function "+copyRowFunctionname+"(){").append("\n");
		s.append("			var rowNums  =	$('#").append(subformKey).append("').jqGrid('getGridParam', 'selarrrow');").append("\n");
		s.append("			if(rowNums.length<1){art.dialog.tips('请选择择您要复制的行')}").append("\n"); 
//		s.append("			for(var i=0;i<rowNums.length;i++){").append("\n");
//		s.append("				var rowid = rowNums[i];						").append("\n");
//		s.append("				var rd = $('#").append(subformKey).append("').jqGrid('getRowData',rowid);						").append("\n");
//		s.append("				"+newId+"--;").append("\n");
////		s.append("			 					rd.ID='-1';							").append("\n");   
//		s.append("				"+subformSaveArray+".push("+newId+");").append("\n");
//		s.append("					  $('#").append(subformKey).append("').jqGrid('addRowData',"+newId+", rd);					").append("\n");
//		s.append("					  $('#").append(subformKey).append("').editRow("+newId+");").append("\n");  
//		s.append("			} ").append("\n");  
		s.append("}").append("\n");  
		return s.toString();
	}
	/**
	 * 增加行所调用的function
	 * @param type 
	 * @return
	 */
	private String getCopyRowScript(Long instanceId,Long subFormId){
		StringBuffer s = new StringBuffer();
		s.append("function "+copyRowFunctionname+"(){").append("\n");
//		s.append("   ").append(saveSubReportGridFunctionname).append("();\n");
		s.append("			var rowNums  =	$('#").append(subformKey).append("').jqGrid('getGridParam', 'selarrrow');").append("\n");
		s.append("			if(rowNums.length<1){art.dialog.tips('请选择择您要复制的行')}").append("\n");
		s.append("			for(var i=0;i<rowNums.length;i++){;").append("\n");
		s.append("				var rowid = rowNums[i];						").append("\n");
		s.append("				var rd1 = $('#").append(subformKey).append("').restoreRow(rowid);	").append("\n");
		s.append("				var rd = $('#").append(subformKey).append("').jqGrid('getRowData',rowid);						").append("\n");
		  
//		s.append("				insertRows($('#").append(subformKey).append("'),").append(newId).append(",").append(subformSaveArray).append(",rd").append(");").append("\n");
//		s.append("				var cell = $('#").append(subformKey).append("').jqGrid('getCell',rowid);						").append("\n");
		s.append("				"+newId+"--;").append("\n");
//		s.append("			 					rd.ID='-1';							").append("\n");   
		s.append("				"+subformSaveArray+".push("+newId+");").append("\n");
		s.append("					  $('#").append(subformKey).append("').jqGrid('addRowData',"+newId+", rd);					").append("\n");
		s.append("					  $('#").append(subformKey).append("').editRow("+newId+");").append("\n");  
		s.append("			}").append("\n");  
		s.append("}").append("\n");  
		return s.toString();
	}
	 
	
	/**
	 * 增加行所调用的function
	 * @param type
	 * @return
	 */
	private String getOnFormNewRowScript(SysEngineSubform subform,Long instanceid,Long engineType){
		StringBuffer s = new StringBuffer();
		s.append("function "+newRowFunctionname+"(){").append("\n");
//		s.append("		alert('").append(subform.getSubtablekey()).append("');").append("\n");
		String modelType = "";
		s.append("		var valid = mainFormValidator.form();").append("\n");
		s.append("		if(!valid){").append("\n");
		s.append("			return;").append("\n");
		s.append("		}").append("\n");
		
		s.append("	var instanceId = document.getElementById('instanceId').value;").append("\n");
		s.append("	if(instanceId==null||instanceId==''||instanceId==0){").append("\n");
		s.append("			art.dialog.confirm('表单未保存，是否执行表单保存操作？', function(){").append("\n");	
		s.append("				saveForm();").append("\n");	
		s.append("			});").append("\n");	 
	//	s.append("			art.dialog.tips('请先对主表单进行保存!',2);").append("\n");
		s.append("			return;").append("\n");
		s.append("	}").append("\n");
		if(engineType==null){
			engineType = new Long(1);
		}
		if(engineType.equals(new Long(1))){
			modelType = IFormStatusConstants.FORM_RUNTIME_TYPE_PROCESS;
			s.append("var actDefId = $(\"#actDefId\").val();").append("\n");
			s.append("var prcDefId = $(\"#prcDefId\").val();").append("\n");
			s.append("var actStepDefId = $(\"#actStepDefId\").val();").append("\n"); 
			s.append("var pageUrl = 'processRuntimeloadSubformPage.action?actDefId='+actDefId+'&prcDefId='+prcDefId+'&actStepDefId='+actStepDefId+'&subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("&instanceId=").append(instanceid).append("&modelType=").append(modelType).append("&engineType=").append(engineType).append("';\n");
		}else{ 
			modelType = IFormStatusConstants.FORM_RUNTIME_TYPE_DEM;
			s.append("var pageUrl = 'subform_item_add.action?subformid=").append(subform.getSubformid()).append("&subformkey=").append(subform.getSubtablekey()).append("&instanceId=").append(instanceid).append("&modelType=").append(modelType).append("&engineType=").append(engineType).append("';\n");
		}
		s.append("	art.dialog.data('defaultValue', ").append(newRowDefaultValue).append(");// 存储默认数据").append("\n"); 
		s.append("	art.dialog.open(pageUrl,{").append("\n");
		s.append("		id:'subFormItemDlg',").append("\n");
		s.append("		title:\"新增行\",").append("\n");
		s.append("		lock:true,").append("\n");
		s.append("		background: '#999',").append("\n");
		s.append("		opacity: 0.87,").append("\n");
		s.append("		width:'90%',").append("\n");
		s.append("		height:'90%',").append("\n");
		s.append("		onClose:function(){").append("\n");
		s.append("  			$('#").append(subformKey).append("').trigger('reloadGrid');").append("\n");
		s.append("		}").append("\n");
		s.append("  });").append("\n"); 
		
		s.append("}").append("\n"); 
		return s.toString();
	}
	/**
	 * 增加行所调用的function
	 * @param type
	 * @return
	 */
	private String getNewRowScript(Long type){
		StringBuffer s = new StringBuffer();
		s.append("function "+newRowFunctionname+"(){").append("\n");
		s.append("	var instanceId = document.getElementById('instanceId').value;").append("\n");
		s.append("	if(instanceId==null||instanceId==''||instanceId==0||is_form_changed()){").append("\n");
		s.append("		var valid = mainFormValidator.form();").append("\n");
		s.append("		if(!valid){").append("\n");
		s.append("			return false;").append("\n");
		s.append("		}").append("\n");
		s.append("		mainFormAlertFlag=true;").append("\n");
		//s.append("		lockPageFlag=false;").append("\n"); 
		s.append("		saveSubReportFlag=true;").append("\n"); 
		
	/*	s.append("			art.dialog.confirm('主表单未保存，是否执行表单保存操作？', function(){").append("\n");	
		s.append("				saveForm();").append("\n");	
		s.append("			});").append("\n");	 
//		s.append("				art.dialog.tips('请先保存主表');").append("\n");
		s.append("				return;").append("\n");*/
		//s.append("		document.getElementById('submitbtn').click();").append("\n");
		//循环检查instnaceid是否超时，超过4秒就跳出
		s.append("		var timeCount=1;").append("\n");
		s.append("		var executeFun=setInterval(function(){").append("\n");
		s.append("			instanceId = document.getElementById('instanceId').value;").append("\n");
		s.append("			if(instanceId>0){").append("\n");
		s.append("				clearInterval(executeFun);").append("\n");
		s.append("				"+newId+"--;").append("\n");
//		if(0==type){
			s.append("				$('#"+subformKey+"').addRowData("+newId+","+newRowDefaultValue+",'last');").append("\n");
			s.append("				$('#"+subformKey+"').editRow("+newId+");").append("\n");
			s.append("				"+subformSaveArray+".push("+newId+");").append("\n");
			
//		}else{	
//			s.append("		"+subformOpenFormFunName+"('new');").append("\n"); 
//		}
		s.append("				return true;").append("\n");
		s.append("			}else if(timeCount>2){").append("\n");
		s.append("				clearInterval(executeFun)").append("\n");
		s.append("			art.dialog.confirm('主表单未保存，是否执行表单保存操作？', function(){").append("\n");	
		s.append("				saveForm();").append("\n");	
		s.append("			});").append("\n");	 
		
//		s.append("				art.dialog.tips('请先保存主表');").append("\n");
		s.append("				return false;").append("\n");
		s.append("			}").append("\n");
		s.append("			timeCount++;").append("\n");
		s.append("			return false;").append("\n");
		s.append("		},500);").append("\n");
		s.append("	}else{").append("\n");
//		if(0==type){
			s.append("		"+newId+"--;").append("\n");
			s.append("		$('#"+subformKey+"').addRowData("+newId+","+newRowDefaultValue+",'last');").append("\n");
			s.append("		$('#"+subformKey+"').editRow("+newId+");").append("\n");
			s.append("		"+subformSaveArray+".push("+newId+");").append("\n");
			s.append("		return true;").append("\n"); 
//		}else{
//			s.append("		"+newId+"--;").append("\n");
//			//弹出子表前刷新url和editurl
//			//s.append("		"+subformResetUrlFunName+"();").append("\n");
//			//s.append("		$('#"+subformKey+"').jqGrid('editGridRow','new',{top:200,left:400,reloadAfterSubmit:true,beforeSubmit: function(posdata,formid){if("+subformColLenValidateMsg+"!=null&&"+subformColLenValidateMsg+"!=''){var msg = "+subformColLenValidateMsg+";"+subformColLenValidateMsg+"='';return [false,msg];}return[true,''];}});").append("\n");
//			s.append("		"+subformOpenFormFunName+"('new');").append("\n"); 
//			s.append("		return true;").append("\n");
//		}
		s.append("	}").append("\n");
	
		s.append("}").append("\n");
		return s.toString();
	}
	/**
	 * 当表单instanceid时，子表保存时重写url和editurl
	 * @param subformId
	 * @return
	 */
	private String getReSetGridUrlScript(Long subformId,Long engineType){
		if(engineType==null){
			engineType = EngineConstants.SYS_INSTANCE_TYPE_PROCESS;
		}
		StringBuffer script = new StringBuffer();
		StringBuffer s = new StringBuffer();
		script.append("function "+subformResetUrlFunName+"(){").append("\n");//
		script.append("	foid="+subformId+";");
		script.append("	var instansid = document.getElementById(\"instanceId\").value").append("\n");
		if(engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_DEM)){
			s.append("'subformSaveItem.action?instanceId=").append("'+document.getElementById(\"instanceId\").value+'").append("&subformkey=").append(subformKey).append("&subformid=").append(subformId).append("&modelId=").append("'+document.getElementById(\"modelId\").value+'").append("&modelType='+document.getElementById(\"modelType\").value+'&isLog='+document.getElementById(\"isLog\").value");
		}else{
			s.append("'subformSaveItem.action?instanceId=").append("'+document.getElementById(\"instanceId\").value+'").append("&subformkey=").append(subformKey).append("&actDefId=").append("'+document.getElementById(\"actDefId\").value+'").append("&actStepDefId=").append("'+document.getElementById(\"actStepDefId\").value+'").append("&prcDefId=").append("'+document.getElementById(\"prcDefId\").value+'").append("&excutionId=").append("'+document.getElementById(\"excutionId\").value+'").append("&taskId=").append("'+document.getElementById(\"taskId\").value+'").append("&subformid=").append(subformId).append("&modelId=").append("'+document.getElementById(\"modelId\").value+'").append("&modelType='+document.getElementById(\"modelType\").value+'&isLog='+document.getElementById(\"isLog\").value");
		}
		script.append("		$('#"+subformKey+"').setGridParam({editurl:"+s.toString()+"});").append("\n");
		s = new StringBuffer();
		if(engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_DEM)){
			s.append("'subform_load.action?instanceId=").append("'+document.getElementById(\"instanceId\").value+'").append("&subformkey=").append(subformKey).append("&subformid=").append(subformId).append("&modelId=").append("'+document.getElementById(\"modelId\").value+'").append("&modelType='+document.getElementById(\"modelType\").value+'&isLog='+document.getElementById(\"isLog\").value");
		}else{
			s.append("'subform_load.action?instanceId=").append("'+document.getElementById(\"instanceId\").value+'").append("&subformkey=").append(subformKey).append("&actDefId=").append("'+document.getElementById(\"actDefId\").value+'").append("&actStepDefId=").append("'+document.getElementById(\"actStepDefId\").value+'").append("&prcDefId=").append("'+document.getElementById(\"prcDefId\").value+'").append("&excutionId=").append("'+document.getElementById(\"excutionId\").value+'").append("&taskId=").append("'+document.getElementById(\"taskId\").value+'").append("&subformid=").append(subformId).append("&modelId=").append("'+document.getElementById(\"modelId\").value+'").append("&modelType='+document.getElementById(\"modelType\").value+'&isLog='+document.getElementById(\"isLog\").value");
		}
		script.append("		$('#"+subformKey+"').setGridParam({url:"+s.toString()+"});").append("\n");
		script.append("	}").append("\n");
		return script.toString();
	}
	/**
	 * 主表保存时，调用的子表保存function
	 * @return
	 */
	private String getSaveSubReportGridScript(Long type,String subfromTitle){
		StringBuffer s = new StringBuffer();
		if(0==type){
			s.append("function "+saveSubReportGridFunctionname+"(){").append("\n");
			//刷新子表url和editurl
			s.append("	"+subformResetUrlFunName+"();").append("\n");
			s.append("	var failArray = new Array();").append("\n");
			s.append("	var alertMsg = '';").append("\n");
			s.append("	if("+subformSaveArray+"!=null&&"+subformSaveArray+".length>0){").append("\n");
			//先循环执行本地保存（没有提交到后台），用于校验多条记录的有校性，有一条记录校验不通过则不执行保存
			s.append("		$('#"+subformKey+"').resetSelection();").append("\n");
			s.append("		for(var i=0;i<"+subformSaveArray+".length;i++){").append("\n");
			s.append("			"+subformColLenValidateIndex+"=$('#"+subformKey+"').getInd("+subformSaveArray+"[i],false);").append("\n");
			s.append("			var r1 = $('#"+subformKey+"').saveRow("+subformSaveArray+"[i],false,'clientArray');").append("\n");
			//校验过后把该行记录重置为编辑状态，使后面能执行保存subformColLenValidateIndex
			s.append("			$('#"+subformKey+"').jqGrid('editRow',"+subformSaveArray+"[i],false);").append("\n");
			//把校验每行各个字段输入长度超长的提示信息合并
			s.append("			if("+subformColLenValidateMsg+".length>0){").append("\n");
			s.append("				alertMsg+="+"'"+subfromTitle+":'+"+subformColLenValidateMsg+"+'"+this.NEWLINE_SYMBOL+"';").append("\n");
			//每行校验完初始化该行字段超长信息
			s.append("				"+subformColLenValidateMsg+"='';").append("\n");
			s.append("			}").append("\n");
			//如果保存后返回的信息不为true，则说明jgrid校验失败，记录失败信息
			s.append("			if(r1!=true&&r1!='true'){").append("\n");
			s.append("				if(r1.indexOf(\""+EngineConstants.NOTNULL_FIELD_SUFFIX+"\")>=0){").append("\n");
			s.append("					r1=r1.replace(\""+EngineConstants.NOTNULL_FIELD_SUFFIX+"\",'');").append("\n");
			s.append("				}").append("\n"); 
			s.append("				alertMsg+='"+subfromTitle+":'+r1+'"+this.NEWLINE_SYMBOL+"';").append("\n");
			s.append("				$('#"+subformKey+"').setSelection("+subformSaveArray+"[i],true);").append("\n");
			s.append("			}").append("\n");
			s.append("		}").append("\n");
			//jqgrid自身校验机制校验存在不合法记录时返回提示信息，不提交到后台
			s.append("		if(alertMsg.length>0){").append("\n");
			s.append("			"+subformColLenValidateMsg+"='';").append("\n");
			s.append("			return alertMsg;").append("\n");
			s.append("		}else{").append("\n");
			//数据有效性校验通过后，再循环执行保存
			s.append("			var flag=true;").append("\n");
			s.append("			for(var i=0;i<"+subformSaveArray+".length;i++){").append("\n");
			s.append("				var responseText2='';").append("\n");
			s.append("				var r = $('#"+subformKey+"').saveRow("+subformSaveArray+"[i],function(response){responseText2=response.responseText;return true;});").append("\n");
			
			//如果数据有效性没通过（r为不为true说明没通过），或者执行保存时失败，则把该记录放进保存失败的数组中,并且让该记录处于编辑状态
			s.append("				if((r!=true&&r!='true')||responseText2.indexOf('false')>=0){").append("\n");
			s.append("					flag=false;").append("\n");
			s.append("					$('#"+subformKey+"').jqGrid('editRow',"+subformSaveArray+"[i],false);").append("\n");
			s.append("					failArray.push("+subformSaveArray+"[i]);").append("\n");
			s.append("				}").append("\n");
			s.append("			}").append("\n");
			//初始化校验字段长度合法的字段
			s.append("			"+subformColLenValidateMsg+"='';").append("\n");
			s.append("			if(flag==false){").append("\n");
			s.append("				"+subformSaveArray+"=failArray;").append("\n");
			s.append("				"+lastsel+"=0;").append("\n");
			s.append("				var failIndex='';").append("\n");
			s.append("				for(var i=0;i<"+subformSaveArray+".length;i++){").append("\n");
			s.append("					if(i=="+subformSaveArray+".length-1){").append("\n");
			s.append("						failIndex+=$('#"+subformKey+"').getInd("+subformSaveArray+"[i],false);").append("\n");
			s.append("					}else{").append("\n");
			s.append("						failIndex+=$('#"+subformKey+"').getInd("+subformSaveArray+"[i],false)+',';").append("\n");
			s.append("					}").append("\n");
			s.append("				}").append("\n"); 
			s.append("				alertMsg+='"+subfromTitle+":第'+failIndex+'条记录保存失败，请稍后重试"+this.NEWLINE_SYMBOL+"';").append("\n");
			s.append("			}else{").append("\n");
			s.append("				"+subformSaveArray+"=new Array();").append("\n");
			s.append("				"+lastsel+"=0;").append("\n");
			s.append("				$('#"+subformKey+"').trigger('reloadGrid');").append("\n");
			s.append("			}").append("\n");
			s.append("		}").append("\n");
			s.append("	}").append("\n");
			s.append("	return alertMsg;").append("\n");
			s.append("}").append("\n");
			//如果为编辑子表则添加主表保存用到的保存子表方方法
			s.append("if(subReportSaveFunction!=null){").append("\n");
			s.append("	var arrFlag=true;").append("\n");
			s.append("	for(var i=0;i<subReportSaveFunction.length;i++){").append("\n");
			s.append("		var saveFun = subReportSaveFunction[i];").append("\n");
			s.append("		if(saveFun.toString()=="+saveSubReportGridFunctionname+".toString()){").append("\n");
			s.append("			arrFlag=false;").append("\n");
			s.append("		}").append("\n");
			s.append("	}").append("\n");
			s.append("	if(arrFlag){").append("\n");
			s.append("		subReportSaveFunction.push('"+subformId+"');").append("\n");
			s.append("	}").append("\n");
			s.append("}").append("\n");
		}
		return s.toString();
	}
	/**
	 * 保存子表时调用的function
	 * @return
	 */
	private String getSaveSubReportScript(){
		StringBuffer s = new StringBuffer();
		s.append("function "+saveSubReportDataFunctionname+"(){").append("\n");
		//刷新子表url和editurl
		s.append("	"+subformResetUrlFunName+"();").append("\n");
		s.append("	document.getElementById('blockPage').style.display='block';").append("\n");
		s.append("	var failArray = new Array();").append("\n");
		s.append("	var alertMsg = '';").append("\n");
		s.append("	if("+subformSaveArray+"!=null&&"+subformSaveArray+".length>0){").append("\n");
		//先循环执行本地保存（没有提交到后台），用于校验多条记录的有校性，有一条记录校验不通过则不执行保存
		s.append("		$('#"+subformKey+"').resetSelection();").append("\n");
		s.append("			var _index1='';").append("\n");
		s.append("		for(var i=0;i<"+subformSaveArray+".length;i++){").append("\n");
		s.append("			").append(subformColLenValidateIndex).append(" = $('#"+subformKey+"').getInd("+subformSaveArray+"[i],false);").append("\n");
		s.append("    		if(").append(subformColLenValidateIndex).append("==''){");
		s.append("    			continue;"); 
		s.append("    		}");
		s.append("			 if(_index1==").append(subformColLenValidateIndex).append(")continue;").append("\n");
		s.append("				_index1 = ").append(subformColLenValidateIndex).append(";").append("\n");
		s.append("			var r1 = $('#").append(subformKey).append("').saveRow("+subformSaveArray+"[i],false,'clientArray');").append("\n");
		/////===============================================================================================================
		//============================================================================================================
		s.append("	if(r1!=true){").append("\n");
		//s.append("				art.dialog.tips(r1);").append("\n");
		s.append("				document.getElementById('blockPage').style.display='none';").append("\n");
		s.append("			return;").append("\n");
		s.append("	}").append("\n");
		//校验过后把该行记录重置为编辑状态，使后面能执行保存subformColLenValidateIndex
		s.append("			$('#"+subformKey+"').jqGrid('editRow',"+subformSaveArray+"[i],false);").append("\n");
		//把校验每行各个字段输入长度超长的提示信息合并
		s.append("			if("+subformColLenValidateMsg+".length>0){").append("\n");
		s.append("				alertMsg+="+subformColLenValidateMsg+"+'"+this.NEWLINE_SYMBOL+"';").append("\n");
		//每行校验完初始化该行字段超长信息
		s.append("				"+subformColLenValidateMsg+"='';").append("\n");
		s.append("			}").append("\n");
		s.append("			if(r1==false||r1=='false'){").append("\n");
		s.append("			document.getElementById('blockPage').style.display='none';").append("\n");
		s.append("				if(r1.indexOf(\""+EngineConstants.NOTNULL_FIELD_SUFFIX+"\")>=0){").append("\n");
		s.append("					r1=r1.replace(\""+EngineConstants.NOTNULL_FIELD_SUFFIX+"\",'');").append("\n");
		s.append("				}").append("\n");
		s.append("				alertMsg+='第'+"+subformColLenValidateIndex+"+'行：'+r1+'"+this.NEWLINE_SYMBOL+"';").append("\n");
		s.append("				$('#"+subformKey+"').setSelection("+subformSaveArray+"[i],true);").append("\n");
		s.append("			}").append("\n");
		s.append("		}").append("\n");
		
		//如果有字段校验信息则提示，并返回值
		s.append("		if(alertMsg!=null&&alertMsg.length>0){").append("\n");
		s.append("			art.dialog.tips(alertMsg);").append("\n");
		s.append("			"+subformColLenValidateMsg+"='';").append("\n");
		s.append("			alertMsg='';").append("\n");
		s.append("			document.getElementById('blockPage').style.display='none';").append("\n");
		s.append("			return false;").append("\n");
		s.append("		}else{").append("\n");
		//数据有效性校验通过后，再循环执行保存 
		s.append("			var flag=true;").append("\n");
		s.append("			var _index2='';").append("\n");
		s.append("			for(var i=0;i<"+subformSaveArray+".length;i++){").append("\n");
		s.append("				var responseText2='';").append("\n");
		//判断一下获取对象是否为空
		s.append("			").append(subformColLenValidateIndex).append(" = $('#"+subformKey+"').getInd("+subformSaveArray+"[i],false);").append("\n");
		s.append("    		if(").append(subformColLenValidateIndex).append("==''){");
		s.append("    			continue;");
		s.append("    		}"); 
		s.append("			 if(_index2==").append(subformColLenValidateIndex).append(")continue;").append("\n");
		s.append("				_index2 = ").append(subformColLenValidateIndex).append(";").append("\n");
		s.append("				var r = $('#"+subformKey+"').saveRow("+subformSaveArray+"[i],function(response){responseText2=response.responseText;return true;});").append("\n");
		//如果数据有效性没通过（r为false说明没通过），或者执行保存时失败，则把该记录放进保存失败的数组中,并且让该记录处于编辑状态
		
//		s.append("			alert(\">>>>1:\"+responseText2+r);").append("\n");
		s.append("				if(r==false||r=='false'||responseText2.indexOf('false')>=0){").append("\n");
		s.append("					flag=false;").append("\n");
		s.append("					$('#"+subformKey+"').jqGrid('editRow',"+subformSaveArray+"[i],false);").append("\n");
		s.append("					failArray.push("+subformSaveArray+"[i]);").append("\n");
		s.append("				}").append("\n"); 
		s.append("			}").append("\n");
		//初始化校验字段长度合法的字段 
		s.append("			"+subformColLenValidateMsg+"='';").append("\n");
//		s.append("			alert(\">>>>2:\"+flag);").append("\n");
		s.append("			if(flag==false){").append("\n");
		s.append("				"+subformSaveArray+"=failArray;").append("\n");
		s.append("				"+lastsel+"=0;").append("\n");
		s.append("				var failIndex='';").append("\n");
		s.append("				for(var i=0;i<"+subformSaveArray+".length;i++){").append("\n");
		s.append("					if(i=="+subformSaveArray+".length-1){").append("\n");
		s.append("						failIndex+=$('#"+subformKey+"').getInd("+subformSaveArray+"[i],false);").append("\n");
		s.append("					}else{").append("\n");
		s.append("						failIndex+=$('#"+subformKey+"').getInd("+subformSaveArray+"[i],false)+',';").append("\n");
		s.append("					}").append("\n"); 
		s.append("				}").append("\n");
		s.append("				art.dialog.tips('第'+failIndex+'条记录保存失败，请稍后重试');").append("\n");
		s.append("				document.getElementById('blockPage').style.display='none';").append("\n");
		s.append("				").append(subformSaveArray).append(" = new Array();").append("\n");
 
		s.append("				return false;").append("\n");
		s.append("			}else{").append("\n");
//		s.append("				"+subformSaveArray+"=new Array();").append("\n");
		s.append("				"+lastsel+"=0;").append("\n");
		s.append("				art.dialog.tips('保存成功',1);").append("\n");
		s.append("		zbtx(foid);		");
		s.append("				try{\n");
		s.append("					subformSaveAfterEvent('").append(subformKey).append("');\n");
		s.append("				}catch(e){}\n");
		//s.append("				alert('保存成功');").append("\n"); 
		s.append("				$('#"+subformKey+"').trigger('reloadGrid');").append("\n");
		s.append("				document.getElementById('blockPage').style.display='none';").append("\n");
		s.append("				").append(subformSaveArray).append(" = new Array();").append("\n");

		s.append("				return true;").append("\n");
		s.append("			}").append("\n");
		s.append("		}").append("\n");
		s.append("	}else{").append("\n");
		s.append("		art.dialog.tips('没有发现修改过的记录');").append("\n");
		s.append("		document.getElementById('blockPage').style.display='none';").append("\n");
		s.append("				").append(subformSaveArray).append(" = new Array();").append("\n");
		s.append("		return false;").append("\n");
		s.append("	}").append("\n");
		s.append("}").append("\n");
		s.append("    function zbtx(foid) {  \n");
		s.append("   var instanceId = $('#instanceId').val();    \n");
		s.append("   var formid = $('#formid').val();    \n");
		s.append("    var filename=$('#XM').val();   \n");
		s.append("   if(instanceId ==null || instanceId ==''){    \n");
		s.append("   instanceId ='0';    \n");
		s.append("    }   \n");
		s.append("   $.post('zbtyff.action', { instanceId : instanceId,formid :formid,foid:foid},function (data) {  });    \n");
		s.append("    }   \n");
		return s.toString();
	}
	/**
	 * 删除某一行或几行记录时调用的function
	 * @return
	 */
	private String getDeleteRowsScript(){
		StringBuffer s = new StringBuffer();
		s.append("function "+deleteRowsFunctionname+"(){").append("\n");
		s.append("	var selectedIdArr = $('#"+subformKey+"').jqGrid('getGridParam', 'selarrrow');").append("\n");
		s.append("	if(selectedIdArr!=null&&selectedIdArr.length>0){").append("\n");
		s.append("		if(confirm('删除数据会使未保存的数据丢失,确定删除吗?')){").append("\n");
		s.append("			"+deleteRowDataFunctionname+"(selectedIdArr);").append("\n");
		s.append("		}").append("\n");
		s.append("	}else{").append("\n");
		s.append("		art.dialog.tips('请选择择您要删除的记录?');").append("\n");
		s.append("	}").append("\n");
		s.append("}").append("\n");
		return s.toString();
	}
	
	/**
	 * 执行删除数据时调用的script
	 * @param instanceId
	 * @param subformid
	 * @param type
	 * @return
	 */
	private String getDeleteRowDataScript(Long instanceId,Long subformid,Long type){
		StringBuffer s = new StringBuffer();
		s.append("function "+deleteRowDataFunctionname+"(selectedIdArr){").append("\n");
		s.append("		var flag = true;").append("\n");
		s.append("try{").append("\n");
		s.append("		flag = deleteSubFormItemBeforeEvent(selectedIdArr);").append("\n");
		s.append("		if(!flag){return;}").append("\n");
		s.append("}catch(e){}").append("\n");
		s.append("	$.ajax({").append("\n");
		s.append("		type: 'POST',").append("\n");
		s.append("		 async:false,").append("\n");
		s.append("		url: 'subformItem_remove.action?instanceId=").append(instanceId).append("&subformid=").append(subformid).append("&id='+selectedIdArr.join(','),").append("\n");
		/*	为了使grid不刷新，把未保存成功的数据丢失，所以自定义的删除后台数据方法，没调jqgrid本身的删除记录方法.
		 * 先把选中的行记录id传到后台（不管是新增的还是已经存在的），后台只删除数据库中已经存在的数据，然后把页面grid中的行记录删除.
		 */
//		s.append("		data:'instanceId="+instanceId+"&subformid="+subformid+"&id='+selectedIdArr.join(','),").append("\n");
		s.append("		success: function (subFormDataGrid) {").append("\n");
		s.append("	zbtx("+subformid+");	\n");
		s.append("			var length=selectedIdArr.length;").append("\n");
		s.append("			for(var i =0;i<length;i++){").append("\n");
		//删除行记录后，从保存数组中移除已经删除的记录，不再行保存
		s.append("				if("+subformSaveArray+"!=null&&"+subformSaveArray+".length>0){").append("\n");
		s.append("					for(var j=0;j<"+subformSaveArray+".length;j++){").append("\n");
		s.append("						if("+subformSaveArray+"[j]==selectedIdArr[0]){").append("\n");
		s.append("							"+subformSaveArray+".splice(j,1);").append("\n");
		s.append("							break;").append("\n");
		s.append("						}").append("\n");
		s.append("					}").append("\n");
		s.append("				}").append("\n");
		//delRowData方法只在页面删除行记录，并没有从后台删除，不会刷新grid
		//selectRowIds是一个指向选中行数组的一个引用，在用jqgrid删除数据时会动态的变化,并以将数组的长度赋给一个变量，则循环的条件就不会变化了，每次都取第 0个数据行
		s.append("				$('#"+subformKey+"').jqGrid('delRowData', selectedIdArr[0]);").append("\n");
		s.append("			}").append("\n");
		//如果是formedit模式，删除完毕后则刷新grid
		if(0!=type){
			s.append("			$('#"+subformKey+"').trigger('reloadGrid');").append("\n");
		}
		s.append("try{").append("\n");
		s.append("		deleteSubFormAfterEvent(selectedIdArr);").append("\n");
		s.append("}catch(e){}").append("\n");
		
		s.append("			art.dialog.tips('删除成功!',1);").append("\n");
		s.append("		}").append("\n");
		s.append("	});").append("\n");
		s.append("}").append("\n");
		return s.toString();
	}
	/**
	 * 生成校验子表列输入文本是否超长script
	 * @param subform
	 * @param instanceid
	 * @return
	 */
	private String getColLengthValidate(SysEngineSubform subform,Long instanceid){
		List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(subform.getSubformid());
		SysEngineIform ifromModel = sysEngineIFormDAO.getSysEngineIformModel(subform.getSubformid());
		if(ifromModel==null){
			return "";
		}
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(ifromModel.getMetadataid());
		StringBuffer script = new StringBuffer();
		script.append("function "+subformColLenValidateFunName+"(value, colname){").append("\n");
		script.append("		var realLength=0,len=value.length,charCode=-1;").append("\n");
		script.append("		for(var i=0;i<len;i++){").append("\n");
		script.append("			charCode = value.charCodeAt(i);").append("\n");
		script.append("			if(charCode>=0&&charCode<=128){").append("\n");
		script.append("				realLength+=1;").append("\n");
		script.append("			}else{").append("\n");
		script.append("				realLength+=2;").append("\n");
		script.append("			}").append("\n");
		script.append("		}").append("\n");
		for(int i=0;i<list.size();i++){
			SysEngineIformMap mapModel = list.get(i);
			String colname = mapModel.getFieldTitle();
			SysEngineMetadataMap metadataMap = sysEngineMetadataMapDAO.getModel(ifromModel.getMetadataid(), mapModel.getFieldName());		
			if(metadataMap==null)continue;
			String type = metadataMap.getFieldtype();
			if(EngineConstants.MAP_TYPE_TEXT.equals(type)||EngineConstants.MAP_TYPE_MEMO.equals(type)){
				String fieldLength = metadataMap.getFieldLength();
				int length = Integer.parseInt(fieldLength==null?"0":fieldLength.trim());
				script.append("		if((colname=='"+colname+"'||colname.indexOf('"+colname+"<')==0)&&"+length+"<realLength){").append("\n");
				script.append("			if(colname.indexOf(\""+EngineConstants.NOTNULL_FIELD_SUFFIX+"\")>=0){;").append("\n");
				script.append("				colname=colname.replace(\""+EngineConstants.NOTNULL_FIELD_SUFFIX+"\",'');").append("\n");
				script.append("			}").append("\n");
				script.append("			if("+subformColLenValidateIndex+">0){;").append("\n");
				script.append("				"+subformColLenValidateMsg+"+=\"第\"+"+subformColLenValidateIndex+"+\"行：'\"+colname+\"'的输入长度必须小于等于"+length+this.NEWLINE_SYMBOL+"\";").append("\n");
				script.append("			}else{").append("\n");
				script.append("				"+subformColLenValidateMsg+"+=\"'\"+colname+\"'的输入长度必须小于等于"+length+this.NEWLINE_SYMBOL+"\";").append("\n");
				script.append("			}").append("\n");
				script.append("			return [true,''];").append("\n");
				script.append("		}").append("\n");
			}
		}	
		script.append("		else{").append("\n");
		script.append("			return [true,''];").append("\n");
		script.append("		}").append("\n");
		script.append("		art.dialog.tips(").append(subformColLenValidateMsg).append(");").append("\n");
		script.append("}").append("\n");
		return script.toString();
	}
	/**
	 * 获得子表列及属性信息
	 * @param subform
	 * @return
	 */
	private String getcolModel(SysEngineSubform subform,Long instanceid,Long formStatus,HashMap<String,ProcessStepFormMap> psfmList){
		List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(subform.getSubformid());
		SysEngineIform ifromModel = sysEngineIFormDAO.getSysEngineIformModel(subform.getSubformid());
		if(ifromModel==null){
			return "";
		}
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(ifromModel.getMetadataid());
		StringBuffer html = new StringBuffer();
		List item = new ArrayList();
		subformIsHidden = true;
		if(list==null)return "";
		for(int i=0;i<list.size();i++){
			Map<String,Object> rootMap = new HashMap();
			SysEngineIformMap mapModel = list.get(i);
			//=======================================================================================
			//                                   表单权限判断                                               *
			//=======================================================================================
			boolean isRead = false;  //字段是否允许编辑
			boolean isModify = false;  //字段是否允许编辑
			boolean isHidden = false;  //字段是否隐藏 
			if(psfmList!=null){
				if(formStatus.equals(IFormStatusConstants.FORM_PAGE_MODIFY)){
						ProcessStepFormMap psfm = (ProcessStepFormMap)psfmList.get(subform.getSubformid()+mapModel.getFieldName());
						if(psfm!=null){
							if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_MODIFY)){
								isModify = true;
								//只要有一个字段可编辑，设置子表不做隐藏
								subformIsHidden = false;
							}else if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_READONLY)){
								isModify = false;
								isRead = true;
								//只要有一个字段可编辑，设置子表不做隐藏
								subformIsHidden = false;
							}else if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_HIDDEN)){
								isModify = false;
								isHidden = true;
							}
						}else{
							isRead = false;
							isModify = true;
							isHidden = false;
						}
						
				}else{
					isModify = false;
					isRead = true;
					ProcessStepFormMap psfm = (ProcessStepFormMap)psfmList.get(subform.getSubformid()+mapModel.getFieldName());
					if(psfm!=null){
						if(psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_HIDDEN)){
							isHidden = true;
						}else{
							//只要有一个字段可编辑，设置子表不做隐藏
							subformIsHidden = false;
						}
					}else{
						//只要有一个字段费隐藏字段，设置子表不做隐藏
						subformIsHidden = false;
						isModify = false;
						isHidden = false;
					}
				}
			}else{
				isModify = true;
				isHidden = false;
				subformIsHidden = false;
			}
			//==================================END=========================================================
			//默认值
			String defaultValue = mapModel.getFieldDefault();
			//动态标签
			if(defaultValue!=null&&defaultValue.indexOf("%") > -1){ 
				ExpressionParamsModel model = new ExpressionParamsModel();
				model.setFormid(subform.getSubformid());
				model.setEntityname(metadataModel.getEntityname());
				model.setInstanceid(instanceid);
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				model.setContext(uc);
				defaultValue = RuntimeELUtil.getInstance().convertMacrosValue(defaultValue,model);
			}
			//参考值
			String displayEnum = mapModel.getDisplayEnum();
			if(mapModel.getDisplaywidth()==null||mapModel.getDisplaywidth()==0){
				mapModel.setDisplaywidth(new Long(80));
			}
			//0选填  、1表示必填
			long notnull = mapModel.getFieldNotnull();
			//开始装配columnModel,先装配公共属性，并以外观共用
			
			String notnullStr = "";
			if(notnull==1){
				Map<String,Object> ruleMap = new HashMap();
				ruleMap.put("required", true);
				rootMap.put("editrules", ruleMap);
				//notnullStr = "<font color='red'>*</font>";
				/*必填项加后缀
				Map<String,Object> formoptionsMap = new HashMap();
				formoptionsMap.put("elmsuffix", "<font color='red'>*</font>");
				rootMap.put("formoptions", formoptionsMap);
				*/
			}
			
			//默认值
			if(defaultValue!=null&&!"".equals(defaultValue)){
				Map<String,Object> editoptionsMap = new HashMap<String,Object>();
				editoptionsMap.put("defaultValue", defaultValue);
				rootMap.put("editoptions", editoptionsMap);
			}
			rootMap.put("name", mapModel.getFieldName());
			rootMap.put("index",mapModel.getFieldName());
			rootMap.put("width",mapModel.getDisplaywidth());
			rootMap.put("edittype", "text");
			//rootMap.put("resizable", true);
			if(mapModel.getHtmlInner()!=null&&!"".equals(mapModel.getHtmlInner())){
				Map<String,Object> editoptionsMap = new HashMap();
				List eventlist = new ArrayList();
				String htmlInner = mapModel.getHtmlInner().toLowerCase();
				//Change事件
				if(htmlInner.indexOf("onchange")>-1){
					HashMap onchange = new HashMap(); 
					onchange.put("type","change"); 
					onchange.put("fn", "function(e){try{subformInputEvent('"+subformKey+"',e.target.name,e.target.value,$(this).attr('id'),'onchange');}catch(e){}}"); 
					eventlist.add(onchange);
				}
				if(htmlInner.indexOf("onblur")>-1){
					//失去焦点事件 
					HashMap onblur = new HashMap();  
					onblur.put("type","onblur"); 
					onblur.put("fn", "function(e){try{subformInputEvent('"+subformKey+"',e.target.name,e.target.value,$(this).attr('id'),'onblur');}catch(e){}}"); 
					eventlist.add(onblur); 
				}
				if(htmlInner.indexOf("keypress")>-1){
					//失去焦点事件
					HashMap keypress = new HashMap();
					keypress.put("type","keypress");
					keypress.put("fn", "function(e){try{subformInputEvent('"+subformKey+"',e.target.name,e.target.value,$(this).attr('id'),'keypress');}catch(e){}}"); 
					eventlist.add(keypress);  
				}
				if(eventlist.size()>0){
					editoptionsMap.put("dataEvents",eventlist); 
					rootMap.put("editoptions", editoptionsMap);
				}
			} 
			//默认是可编辑的文本框
			if(mapModel.getDisplayType()!=null&&!isRead){
				//判断是否为隐藏域
				if(IFormStatusConstants.FORM_FIELD_TYPE_HIDDEN.equals(mapModel.getDisplayType())){
					rootMap.put("hidden",true); 
				}else{
					//校验字段长度
					Map<String,Object> ruleMap = null;
					if(rootMap.containsKey("editrules")){
						ruleMap = (Map<String,Object>)rootMap.get("editrules");
					}else{
						ruleMap = new HashMap<String,Object>();
					}
					ruleMap.put("custom",true);
					ruleMap.put("custom_func",subformColLenValidateFunName);
					rootMap.put("editrules", ruleMap);
					//类型验证判断
					if(mapModel.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_TXTBOX)){//设置文本域的行和高
						if(mapModel.getInputHeight()==null)mapModel.setInputHeight(new Long(0));
						if(mapModel.getInputWidth()==null)mapModel.setInputWidth(new Long(0));
						long areaRows =mapModel.getInputHeight()==0?3:mapModel.getInputHeight();
						long areaCols =mapModel.getDisplaywidth()==0?10:mapModel.getDisplaywidth(); 
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						}
						editoptionsMap.put("rows", areaRows);
						editoptionsMap.put("cols", areaCols);
						StringBuffer style = new StringBuffer();
						style.append("border:1px solid #fff;");
						style.append("width:").append(areaCols).append("px;");
						editoptionsMap.put("style", style.toString());
						rootMap.put("editoptions", editoptionsMap);
					}else if(mapModel.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_TXTBOX_MONEY)){//设置文本域的行和高
						long areaRows =mapModel.getInputHeight()==0?3:mapModel.getInputHeight();
						long areaCols =mapModel.getInputWidth()==0?10:mapModel.getInputWidth(); 
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						}
					
						editoptionsMap.put("rows", areaRows); 
						editoptionsMap.put("cols", areaCols);
						StringBuffer style = new StringBuffer();
						style.append("border:1px solid #fff;");
						style.append("width:").append(areaCols).append("px;");
						editoptionsMap.put("style", style.toString());
						rootMap.put("editoptions", editoptionsMap);
						rootMap.put("formatter", "currency"); 
						rootMap.put("formatoptions", "{decimalSeparator:\".\", thousandsSeparator: \",\", decimalPlaces: 2, prefix: \"￥\", suffix:\"\", defaulValue: 0}"); 
					}else if(mapModel.getDisplayType().equals(IFormStatusConstants.FORM_FIELD_TYPE_TXTBOX_NUMBER)){//设置文本域的行和高
						long areaRows =mapModel.getInputHeight()==null?3:mapModel.getInputHeight();
						long areaCols =mapModel.getInputWidth()==null?10:mapModel.getInputWidth(); 
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						}
						StringBuffer style = new StringBuffer();
						style.append("border:1px solid #fff;");
						style.append("width:").append(areaCols).append("px;");
						editoptionsMap.put("style", style.toString());
						editoptionsMap.put("rows", areaRows); 
						editoptionsMap.put("cols", areaCols);
						rootMap.put("editoptions", editoptionsMap);
						rootMap.put("formatter", "number"); 
//						rootMap.put("formatoptions", "{defaulValue: 0}"); 
					}else if(IFormStatusConstants.FORM_FIELD_TYPE_RADIOBOOK.equals(mapModel.getDisplayType())||IFormStatusConstants.FORM_FIELD_TYPE_MULTIBOOK.equals(mapModel.getDisplayType())||IFormStatusConstants.FORM_FIELD_TYPE_DEPTBOOK.equals(mapModel.getDisplayType())){
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						}
						StringBuffer style = new StringBuffer();
						style.append("cursor:pointer;");
						style.append("border:1px solid #fff;");
						editoptionsMap.put("style", style.toString());
						editoptionsMap.put("readonly", "readonly");
						String functionName = mapModel.getFieldName()+mapModel.getDisplayType()+subform.getId();
						editoptionsMap.put("dataInit",WrapScriptUtil.wrapFunctionName(functionName));
						rootMap.put("editoptions", editoptionsMap);
					}else if(IFormStatusConstants.FORM_FIELD_TYPE_WBS_DIRECTORY.equals(mapModel.getDisplayType())){
						Map<String,Object> editoptionsMap = null; 
						if(rootMap.containsKey("editoptions")){
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>(); 
						}
						StringBuffer style = new StringBuffer();
						style.append("cursor:pointer;");
						style.append("border:1px solid #fff;");
						editoptionsMap.put("style", style.toString());
						editoptionsMap.put("readonly", "readonly");
						String functionName = mapModel.getFieldName()+mapModel.getDisplayType()+subform.getId();
						editoptionsMap.put("dataInit",WrapScriptUtil.wrapFunctionName(functionName));
						rootMap.put("editoptions", editoptionsMap);
					}else if(IFormStatusConstants.FORM_FIELD_TYPE_FI_SUBJECT.equals(mapModel.getDisplayType())){
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){ 
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						}
						StringBuffer style = new StringBuffer();
						style.append("cursor:pointer;");
						style.append("border:1px solid #fff;");
						editoptionsMap.put("style", style.toString());
						editoptionsMap.put("readonly", "readonly");
						String functionName = mapModel.getFieldName()+mapModel.getDisplayType()+subform.getId();
						editoptionsMap.put("dataInit",WrapScriptUtil.wrapFunctionName(functionName));
						rootMap.put("editoptions", editoptionsMap);
					}else if(IFormStatusConstants.FORM_FIELD_TYPE_READONLY.equals(mapModel.getDisplayType())){
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						}
						StringBuffer style = new StringBuffer();
						style.append("cursor:pointer;");
						style.append("background:#efefef;");
						style.append("border:1px solid #fff;color:#999");
						editoptionsMap.put("style", style.toString());
						editoptionsMap.put("readonly", "readonly");
						rootMap.put("editoptions", editoptionsMap);
					}else if(IFormStatusConstants.FORM_FIELD_TYPE_TXTBOX_READONLY.equals(mapModel.getDisplayType())){
						if(mapModel.getInputHeight()==null)mapModel.setInputHeight(new Long(0));
						if(mapModel.getInputWidth()==null)mapModel.setInputWidth(new Long(0));
						long areaRows =mapModel.getInputHeight()==0?3:mapModel.getInputHeight();
						long areaCols =mapModel.getInputWidth()==0?10:mapModel.getInputWidth(); 
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						}
						editoptionsMap.put("readonly", "readonly");
						StringBuffer style = new StringBuffer();
						style.append("background:#F2F2F2;border:1px solid #fff;");
						style.append("width:").append(areaCols).append(";");
						
						editoptionsMap.put("style", style.toString());
						rootMap.put("editoptions", editoptionsMap);
					}else if(IFormStatusConstants.FORM_FIELD_TYPE_DICTIONARYCHOOSER.equals(mapModel.getDisplayType())){
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						} 
						StringBuffer style = new StringBuffer();
						style.append("cursor:pointer;");
						style.append("border:1px solid #fff;");
						editoptionsMap.put("style", style.toString());
						editoptionsMap.put("readonly", "readonly");
						String functionName = mapModel.getFieldName()+mapModel.getDisplayType()+subform.getId();
						editoptionsMap.put("dataInit",WrapScriptUtil.wrapFunctionName(functionName));
						rootMap.put("editoptions", editoptionsMap);
					}else if(IFormStatusConstants.FORM_FIELD_TYPE_CALCULATE.equals(mapModel.getDisplayType())){
						//isModify = false;
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						}
						editoptionsMap.put("readonly", "readonly");
//						editoptionsMap.put("disabled", "disabled");
						rootMap.put("editoptions", editoptionsMap);
						if(mapModel.getDisplayEnum()!=null&&!mapModel.getDisplayEnum().equals("")){ 
							rootMap.put("formatter", "function(val"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"opts"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"T){return "+mapModel.getDisplayEnum()+";}"); 
						}
						//rootMap.put("unformat", "function(val"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"opts"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"rowdata){return (rowdata.PRICE*rowdata.SQSL);}"); 
					}else if(IFormStatusConstants.FORM_FIELD_TYPE_CALCULATE_MONEY.equals(mapModel.getDisplayType())){
						//isModify = false;
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){ 
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						}
						StringBuffer style = new StringBuffer();
						style.append("cursor:pointer;");
						style.append("border:1px solid #fff;");
						editoptionsMap.put("style", style.toString());
						editoptionsMap.put("readonly", "readonly");
						rootMap.put("editoptions", editoptionsMap);
						if(mapModel.getDisplayEnum()!=null&&!mapModel.getDisplayEnum().equals("")){ 
							rootMap.put("formatter", "function(val"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"opts"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"T){var value= "+mapModel.getDisplayEnum()+"; return '￥'+value.toFixed(2);}");
							rootMap.put("unformat", "function(val"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"opts"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"T){val = val.replace('￥','');val = val.replace(',',''); return val;}");
						} 
						//rootMap.put("unformat", "function(val"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"opts"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"rowdata){return (rowdata.PRICE*rowdata.SQSL);}"); 
					}else if(IFormStatusConstants.FORM_FIELD_TYPE_TXTBOX_DATE.equals(mapModel.getDisplayType())){
						//isModify = false;
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){ 
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						}
						StringBuffer style = new StringBuffer();
						style.append("cursor:pointer;");
						style.append("border:1px solid #fff;");
						editoptionsMap.put("style", style.toString());
						
						rootMap.put("editoptions", editoptionsMap);
							rootMap.put("formatter", "{date:{ ISO8601Long:\"Y-m-d H:i:s\"}}"); 
						//rootMap.put("unformat", "function(val"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"opts"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"rowdata){return (rowdata.PRICE*rowdata.SQSL);}"); 
					}else if(IFormStatusConstants.FORM_FIELD_TYPE_TXTBOX_ISO_DATE.equals(mapModel.getDisplayType())){
						//isModify = false;
						Map<String,Object> editoptionsMap = null;
						if(rootMap.containsKey("editoptions")){ 
							editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
						}else{
							editoptionsMap = new HashMap<String,Object>();
						}
						StringBuffer style = new StringBuffer();
						style.append("cursor:pointer;");
						style.append("border:1px solid #fff;");
						editoptionsMap.put("style", style.toString());
						rootMap.put("editoptions", editoptionsMap);
							rootMap.put("formatter", "{date:{ ISO8601Long:\"Y-m-d H:i:s\"}}"); 
						//rootMap.put("unformat", "function(val"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"opts"+EngineConstants.JSON_ESCAPE_COMMA_LABLE+"rowdata){return (rowdata.PRICE*rowdata.SQSL);}"); 
					}
				}
				//不同外观装配自身特有属性（非公共属性）
				SysEngineIformMap sysEngineIformMap = sysEngineIFormMapDAO.getModel(subform.getSubformid(), mapModel.getFieldName());
				SysEngineMetadataMap metadataMap = sysEngineMetadataMapDAO.getModel(metadataModel.getId(),mapModel.getFieldName());
				UIComponentInterface uicompone = IFormUIFactory.getUIInstance(metadataMap, sysEngineIformMap,"");
				if(displayEnum!=null&&displayEnum.indexOf("%")>0){
					ExpressionParamsModel model = new ExpressionParamsModel();
					model.setFormid(subform.getSubformid());
					model.setEntityname(metadataModel.getEntityname());
					model.setInstanceid(instanceid);
					UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
					model.setContext(uc);
					displayEnum = RuntimeELUtil.getInstance().convertMacrosValue(displayEnum,model);
				}
				uicompone.parseToJson(displayEnum,mapModel.getDisplayType(),rootMap);
			}else{
				//判断是否为隐藏域
				if(IFormStatusConstants.FORM_FIELD_TYPE_HIDDEN.equals(mapModel.getDisplayType())){
					rootMap.put("hidden",true); 
				}else{
					Map<String,Object> editoptionsMap = null;
					if(rootMap.containsKey("editoptions")){
						editoptionsMap = (Map<String,Object>)rootMap.get("editoptions");
					}else{
						editoptionsMap = new HashMap<String,Object>();
					}
					StringBuffer style = new StringBuffer();
					style.append("cursor:pointer;");
					style.append("background:#efefef;");
					style.append("border:1px solid #fff;color:#999");
					editoptionsMap.put("style", style.toString());
					editoptionsMap.put("readonly", "readonly"); 
					rootMap.put("editoptions", editoptionsMap);
				}
			}
			
			//======================设置显示状态==========================
			if(isHidden){
				rootMap.put("hidden",true);
			}
			//======================设置编辑状态==========================
			if(isModify){
				rootMap.put("editable", true);
			}else{
				rootMap.put("editable", true);
			}
			
			item.add(rootMap);
		}
		//装载ID序列
		Map map = new HashMap();
		map.put("name","ID");
		map.put("index","1");
		map.put("key",true);
		map.put("width","100");
		map.put("hidden",true);
		item.add(map);
		JSONArray json = JSONArray.fromObject(item);
		//JSONOject.formOject("");
		//把生成的json字符串中的变量过滤掉"
		String jsonString = json.toString().replaceAll("\""+EngineConstants.JAVASCRIPT_FUNCTIONNAME_PREFIX, EngineConstants.JAVASCRIPT_FUNCTIONNAME_PREFIX);
		jsonString = jsonString.replaceAll(EngineConstants.JSON_ESCAPE_COMMA_LABLE,EngineConstants.JSON_ESCAPE_COMMA_VALUE);
		jsonString = jsonString.replaceAll(EngineConstants.JAVASCRIPT_FUNCTIONNAME_SUFFIX+"\"", EngineConstants.JAVASCRIPT_FUNCTIONNAME_SUFFIX);
		html.append(jsonString);
		return html.toString(); 
	}
	
	
	
	/**
	 * 
	 * @param subform
	 * @return
	 */
	private String getSubFormTitle(SysEngineSubform subform){
		List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(subform.getSubformid());
		StringBuffer html = new StringBuffer();
		List item = new ArrayList();
		//item.add("序号");
		if(list==null)return "";
		for(int i=0;i<list.size();i++){
			SysEngineIformMap mapModel = list.get(i);
			String title = mapModel.getFieldTitle();
			Long blankFlag = mapModel.getFieldNotnull();
			if(blankFlag==1){
				title+=EngineConstants.NOTNULL_FIELD_SUFFIX;
			}
			item.add(title);
		}
		item.add("序号");
		JSONArray json = JSONArray.fromObject(item);
		html.append(json);
		return html.toString();
	}
	
	
	/**
	 * 获得电子表单基本信息
	 * @param formid
	 * @return
	 */
	public String getFormBaseInfo(Long formid){
		StringBuffer baseInfo = new StringBuffer();
		SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
		if(sysEngineIform!=null){
			baseInfo.append("<div class=\"info_row\">").append("\n");
				baseInfo.append("<div class=\"info_name\">").append("[表单名称]").append("</div>").append("<div class=\"info_value\">").append(sysEngineIform.getIformTitle()).append("</div>").append("\n");
			baseInfo.append("</div>").append("\n");
			baseInfo.append("<div class=\"info_row\">").append("\n");
			baseInfo.append("<div class=\"info_name\">").append("[更新日期]").append("</div>").append("<div class=\"info_value\">").append(UtilDate.dateFormat(sysEngineIform.getUpdatedate())).append("</div>").append("\n");
			baseInfo.append("</div>").append("\n");
			baseInfo.append("<div class=\"info_row\">").append("\n");
				baseInfo.append("<div class=\"info_name\">").append("[管理员]").append("</div>").append("<div class=\"info_value\">").append(sysEngineIform.getMaster()).append("</div>").append("\n");
			baseInfo.append("</div>").append("\n");
//			baseInfo.append("<div class=\"info_row\">");
//			baseInfo.append("<div class=\"info_name\">").append("[模版名称]").append("</div>").append("<div class=\"info_value\">").append(sysEngineIform.getIformTemplate()).append("</div>");
//			baseInfo.append("</div>");
		}
		return baseInfo.toString();
	}
	
	/**
	 * 正确的匹配列
	 * @return
	 */
	public String getVerifySuccessInfo(){
		StringBuffer VerifyHtml = new StringBuffer();
		VerifyHtml.append("<ul>").append("\n");
		if(verifySuccessInfo!=null){
			for(int i=0;i<verifySuccessInfo.size();i++){
				SysEngineIformMap  iformmap = verifySuccessInfo.get(i);
				VerifyHtml.append("<li >[OK]").append("<b>").append(iformmap.getFieldTitle()).append("</b>").append("[").append(iformmap.getFieldName()).append("]").append("绑定成功").append("</li>").append("\n");
			}
		}
		VerifyHtml.append("</ul>").append("\n");
		return VerifyHtml.toString();
	}
	
	/**
	 * 标签匹配异常列表
	 * @return
	 */
	public String getVerifyErrorInfo(Long formid){
		StringBuffer VerifyHtml = new StringBuffer();
		VerifyHtml.append("<ul>").append("\n");
		//循环未匹配信息
		if(verifyErrorInfo!=null){
			for(int i=0;i<verifyErrorInfo.size();i++){
				String  info = (String)verifyErrorInfo.get(i);
				VerifyHtml.append("<li ><img src='iwork_img/nondynamic.gif' border=0/>").append(info).append("</li>").append("\n");
			}
		}
		List<SysEngineIformMap> list = sysEngineIFormMapDAO.getDataList(formid);
		//循环未绑定信息
		if(list!=null){
			for(int i=0;i<list.size();i++){
				boolean flag = false;
				SysEngineIformMap  iformmap = list.get(i);
				for(int j=0;j<iformMapList.size();j++){
					String label = (String)iformMapList.get(j);
					if(iformmap.getFieldName().equals(label)){
						flag =true;
						break;
					}
				}
				if(!flag)
					VerifyHtml.append("<li ><img src='iwork_img/nondynamic.gif' border=0/>").append("<b>").append(iformmap.getFieldTitle()).append("</b>").append("[").append(iformmap.getFieldName()).append("]未绑定</li>").append("\n");
			}
		}
		
		VerifyHtml.append("</ul>").append("\n");
		return VerifyHtml.toString();
	}
	
	
	/**
	 * 获得【移动端】表单域的外观样式
	 * @param sysEngineIform
	 * @param meatadataModel
	 * @param instanceid
	 * @param fieldName
	 * @param value
	 * @param status
	 * @param taskParam 任务参数
	 * @return
	 */
	String getMobileUICompoment(SysEngineIform sysEngineIform,SysEngineMetadata meatadataModel,HashMap taskParam,Long instanceid,String fieldName,String value,int status){
		if(instanceid==null)
			instanceid=new Long(0); 
		Long formid =  sysEngineIform.getId();
		SysEngineIformMap sysEngineIformMap = sysEngineIFormMapDAO.getModel(formid, fieldName);
		SysEngineMetadataMap metadataMap = sysEngineMetadataMapDAO.getModel(meatadataModel.getId(),fieldName);
		if(metadataMap!=null&&sysEngineIformMap!=null){
			UIComponentInterface uicompone = IFormUIFactory.getUIInstance(metadataMap, sysEngineIformMap,"");
			HashMap params = new HashMap();
			params.put("formid", formid);
			params.put("metadataid", meatadataModel.getId());
			params.put("fieldName", fieldName);
			params.put("instanceid", instanceid);
			//=======装载流程参数========
			if(taskParam!=null){
				 //装载流程单据编号
				String formno = (String)taskParam.get(ProcessTaskConstant.PROCESS_TASK_FORMNO);
				if(formno!=null)params.put(ProcessTaskConstant.PROCESS_TASK_FORMNO, formno);
			}
			//获取动态默认值
			if(sysEngineIformMap.getFieldDefault()==null)
				sysEngineIformMap.setFieldDefault("");
			if (value.length() == 0 || (metadataMap.getFieldtype().equals("数值") && value.equals("0"))) {
				String defValue = sysEngineIformMap.getFieldDefault();
				ExpressionParamsModel model = new ExpressionParamsModel();
				model.setFormid(formid);
				model.setEntityname(meatadataModel.getEntityname());
				model.setInstanceid(instanceid);
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				model.setContext(uc);
				//=======装载流程参数========
				if(taskParam!=null){
					 //装载流程单据编号
					model.setTaskParam(taskParam);
				}
				
				//获取动态变量
				if(sysEngineIformMap.getFieldDefault().indexOf("%") > -1){
					value = RuntimeELUtil.getInstance().convertMacrosValue(defValue,model);
				}else{
					value = defValue;
				}
			}
			uicompone.setValue(value);
			String uiDefine = "";
				//判断字段权限
				if(status == IFormStatusConstants.FORM_FIELD_MODIFY){
					uiDefine = uicompone.getMobileHtmlDefine(params);
				}else if(status == IFormStatusConstants.FORM_FIELD_READ){
					uiDefine = uicompone.getReadHtmlDefine(params);
				}else if(status == IFormStatusConstants.FORM_FIELD_HIDDEN){  //隐藏状状态
					IFormUIComponentHiddenImpl component = new IFormUIComponentHiddenImpl(metadataMap,sysEngineIformMap,value);
					component.getModifyHtmlDefine(params);
				}
			//匹配成功后，从集合中移除此表单域对象,同时添加至成功列表中
			iformMapList.remove(sysEngineIformMap);
			verifySuccessInfo.add(sysEngineIformMap);
			return uiDefine;
		}else{
			return null;
		}
	}
	/**
	 * 获得表单域的外观样式
	 * @param sysEngineIform
	 * @param meatadataModel
	 * @param instanceid
	 * @param fieldName
	 * @param value
	 * @param status
	 * @param taskParam 任务参数
	 * @return
	 */
	public String getUICompoment(SysEngineIform sysEngineIform,SysEngineMetadata meatadataModel,HashMap taskParam,Long instanceid,String fieldName,String value,int status,Long engineType){
		if(instanceid==null)
			instanceid=new Long(0); 
		Long formid =  sysEngineIform.getId();
		SysEngineIformMap sysEngineIformMap = sysEngineIFormMapDAO.getModel(formid, fieldName);
		SysEngineMetadataMap metadataMap = null;
		if(sysEngineIformMap!=null&&sysEngineIformMap.getMapType()!=null&&sysEngineIformMap.getMapType().equals(new Long(1))){
			metadataMap = sysEngineMetadataMapDAO.getModel(meatadataModel.getId(),fieldName);
		}
		
		if(sysEngineIformMap!=null){
			UIComponentInterface uicompone = IFormUIFactory.getUIInstance(metadataMap, sysEngineIformMap,"");
			HashMap params = new HashMap();
			params.put("formid", formid);
			params.put("metadataid", meatadataModel.getId());
			params.put("fieldName", fieldName);
			params.put("instanceid", instanceid);
			params.put("engineType", engineType);
			//=======装载流程参数========
			if(taskParam!=null){
				//装载流程单据编号
				String formno = (String)taskParam.get(ProcessTaskConstant.PROCESS_TASK_FORMNO);
				if(formno!=null)params.put(ProcessTaskConstant.PROCESS_TASK_FORMNO, formno);
				Object formdata = taskParam.get(ProcessTaskConstant.PROCESS_TASK_FORMDATA);
				if(formdata!=null){
					params.put(ProcessTaskConstant.PROCESS_TASK_FORMDATA, formdata);
				}
			}
			//获取动态默认值
			if(sysEngineIformMap.getFieldDefault()==null)
				sysEngineIformMap.setFieldDefault(""); 
			
			String fieldDefault = sysEngineIformMap.getFieldDefault();
			//判断是否调用动态运行时参数对象
			boolean isRV = false;
			if(value.length() == 0){
				isRV = true;
			}else if(metadataMap!=null&&metadataMap.getFieldtype().equals("数值") &&value.equals("0")){
				isRV = true;
			}else if(fieldDefault.indexOf("%sum")>-1){
				isRV = true;
			}
			if(isRV) {
				String defValue = sysEngineIformMap.getFieldDefault();
				ExpressionParamsModel model = new ExpressionParamsModel();
				model.setFormid(formid); 
				model.setEntityname(meatadataModel.getEntityname());
				model.setInstanceid(instanceid);
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				model.setContext(uc);
				model.setEngineType(engineType);
				//=======装载流程参数========
				if(taskParam!=null){ 
					//装载流程单据编号
					model.setTaskParam(taskParam);
				}
				if(defValue==null)defValue="";
				if(value==null)value="";
				
				//获取动态变量
				if(sysEngineIformMap.getFieldDefault().indexOf("%") > -1){
					value = RuntimeELUtil.getInstance().convertMacrosValue(defValue,model);
				}else if(value.equals("")&&!"".equals(defValue)){
					value = defValue;
				}
			} 
			uicompone.setValue(value);
			String uiDefine = "";
			//判断字段权限
			if(status == IFormStatusConstants.FORM_FIELD_MODIFY){
				uiDefine = uicompone.getModifyHtmlDefine(params);
			}else if(status == IFormStatusConstants.FORM_FIELD_READ){
				uiDefine = uicompone.getReadHtmlDefine(params);
			}else if(status == IFormStatusConstants.FORM_FIELD_HIDDEN){  //隐藏状状态
				IFormUIComponentHiddenImpl component = new IFormUIComponentHiddenImpl(metadataMap,sysEngineIformMap,value);
				uiDefine = component.getModifyHtmlDefine(params); 
			}else if(status == IFormStatusConstants.FORM_FIELD_PROCESS_STEP_HIDDEN){  //隐藏状状态
				IFormUIComponentProcessStepSetHiddenImpl component = new IFormUIComponentProcessStepSetHiddenImpl(metadataMap,sysEngineIformMap,value); 
				uiDefine = component.getModifyHtmlDefine(params); 
			}else{  //隐藏状状态
				IFormUIComponentHiddenImpl component = new IFormUIComponentHiddenImpl(metadataMap,sysEngineIformMap,value);
				uiDefine = component.getModifyHtmlDefine(params); 
			}
			//匹配成功后，从集合中移除此表单域对象,同时添加至成功列表中
			iformMapList.remove(sysEngineIformMap);
			verifySuccessInfo.add(sysEngineIformMap);
			return uiDefine;
		}else{
			return null;
		}
	}
	
	/**
	 * 获取当前录入项历史的录入内容
	 * @param fieldName
	 * @param formId
	 * @return
	 */
	public String getFormInputHistoryJSON(String fieldName,String searchkey,Long formId){
		SysEngineIform model = sysEngineIFormDAO.getSysEngineIformModel(formId);
		StringBuffer json = new StringBuffer();
		if(model!=null){
			SysEngineMetadata metadata = sysEngineMetadataDAO.getModel(model.getMetadataid());
			List list = iformDataDAO.getInputList(metadata.getEntityname(), fieldName, searchkey, null);
			JSONArray jsonArray = JsonUtil.listToJson(list);
			if(jsonArray!=null){
				json.append(jsonArray);
			}
		}
		return json.toString();
	}
	
	
	public FreeMarkerConfigurer getFreemarderConfig() {
		return freemarderConfig;
	}
	public void setFreemarderConfig(FreeMarkerConfigurer freemarderConfig) {
		this.freemarderConfig = freemarderConfig;
	}
	public SysEngineIFormDAO getSysEngineIFormDAO() {
		return sysEngineIFormDAO;
	}
	public void setSysEngineIFormDAO(SysEngineIFormDAO sysEngineIFormDAO) {
		this.sysEngineIFormDAO = sysEngineIFormDAO;
	}


	public SysEngineSubformDAO getSysEngineSubformDAO() {
		return sysEngineSubformDAO;
	}
	public void setSysEngineSubformDAO(SysEngineSubformDAO sysEngineSubformDAO) {
		this.sysEngineSubformDAO = sysEngineSubformDAO;
	}


	public SysEngineMetadataMapDAO getSysEngineMetadataMapDAO() {
		return sysEngineMetadataMapDAO;
	}


	public void setSysEngineMetadataMapDAO(
			SysEngineMetadataMapDAO sysEngineMetadataMapDAO) {
		this.sysEngineMetadataMapDAO = sysEngineMetadataMapDAO;
	}


	public SysEngineMetadataDAO getSysEngineMetadataDAO() {
		return sysEngineMetadataDAO;
	}


	public void setSysEngineMetadataDAO(SysEngineMetadataDAO sysEngineMetadataDAO) {
		this.sysEngineMetadataDAO = sysEngineMetadataDAO;
	}


	public SysEngineIFormMapDAO getSysEngineIFormMapDAO() {
		return sysEngineIFormMapDAO;
	}


	public void setSysEngineIFormMapDAO(SysEngineIFormMapDAO sysEngineIFormMapDAO) {
		this.sysEngineIFormMapDAO = sysEngineIFormMapDAO;
	}

	public IFormDataDAO getIformDataDAO() {
		return iformDataDAO;
	}

	public void setIformDataDAO(IFormDataDAO iformDataDAO) {
		this.iformDataDAO = iformDataDAO;
	}
	public SysEngineFormBindDAO getIformBindDAO() {
		return iformBindDAO;
	}
	public void setIformBindDAO(SysEngineFormBindDAO iformBindDAO) {
		this.iformBindDAO = iformBindDAO;
	}
	public SysInstanceDataDAO getInstanceDataDAO() {
		return instanceDataDAO;
	}
	public void setInstanceDataDAO(SysInstanceDataDAO instanceDataDAO) {
		this.instanceDataDAO = instanceDataDAO;
	}
	public TaskService getTaskService() {
		return taskService;
	}
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
	public void setSysDemTriggerService(SysDemTriggerService sysDemTriggerService) {
		this.sysDemTriggerService = sysDemTriggerService;
	}

	public void setSysIformActionLogService(
			SysIformActionLogService sysIformActionLogService) {
		this.sysIformActionLogService = sysIformActionLogService;
	}
	public void setSysDictionaryRuntimeService(
			SysDictionaryRuntimeService sysDictionaryRuntimeService) {
		this.sysDictionaryRuntimeService = sysDictionaryRuntimeService;
	}

	public FreeMarkerConfigurer getMobilefreemarderConfig() {
		return mobilefreemarderConfig;
	}

	public void setMobilefreemarderConfig(
			FreeMarkerConfigurer mobilefreemarderConfig) {
		this.mobilefreemarderConfig = mobilefreemarderConfig;
	}

	public RichTextUtil getRichTextUtil() {
		return richTextUtil;
	}

	public void setRichTextUtil(RichTextUtil richTextUtil) {
		this.richTextUtil = richTextUtil;
	}
	
}
