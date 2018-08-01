package com.iwork.connector.mdm.erp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.connection.ConnectionAPI;
import com.iwork.connector.common.ConnectorUUIDConstant;
import com.iwork.core.security.SecurityUtil;
import com.iwork.sdk.DemAPI;

/**
 * 同步项目主数据
 * @author David
 *
 */
public class MdmSynchronizeProjectToDemSchedule implements IWorkScheduleInterface{
	
	public static final String PROJECT_DEM_UUID= "bc10fd6a72f54469baeabceae6b7d154";
	public static final String WBS_DEM_UUID= "ad3880a3bc5449bbb6b3daed2355aca8";
	
	
	/**
	 * 同步
	 */
	public boolean executeBefore() throws ScheduleException {
		System.out.println("开始同步项目主数据.............................");
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		doExecute();
		
		return true;
	}

	public boolean executeAfter() throws ScheduleException {
		System.out.println("项目主数据同步完成.............................OK");
		return true; 
	}
	 
	/**
	 * 执行操作
	 * @return
	 */
	private boolean doExecute(){
		//获取项目定义列表
		List<HashMap> pjDefList =  ConnectionAPI.getInstance().getList(ConnectorUUIDConstant.BAPI_PROJECTDEF_GETLIST, new HashMap());
		for(HashMap data:pjDefList){
			//获取项目编号
			String pjno = data.get("PROJECT_DEFINITION").toString(); 
			//获取项目详细信息
			HashMap pjHash = new HashMap();
			pjHash.put("I_PROJECT_DEFINITION", pjno);
			List<HashMap> pjInfolist = ConnectionAPI.getInstance().getList(ConnectorUUIDConstant.BAPI_PROJECTDEF_GETDETAIL, pjHash);
			synchronizeProjectMdmInfo(pjInfolist);
			//获取项目WBS列表 
			HashMap hash = new HashMap();
			hash.put("I_PROJECT_DEFINITION", pjno);
			List<HashMap> wbslist = ConnectionAPI.getInstance().getList(ConnectorUUIDConstant.BAPI_BUS2054_GETDATA, hash);
			synchronizeWBSMdmInfo(wbslist);
		}
		return false;
	}
	
	/**
	 * 同步项目主数据
	 * @param wbslist
	 */
	private void synchronizeProjectMdmInfo(List<HashMap> pjInfolist){
		for(HashMap hash:pjInfolist){
			String pj_no = hash.get("PSPID").toString(); //项目定义
			String pj_name = hash.get("POST1").toString(); //项目名称
			
			String txt30 = hash.get("TXT30").toString(); //项目状态
			String vernr = hash.get("VERNR").toString(); //负责人编号
			String verna = hash.get("VERNA").toString(); //负责人名称
			String startdate =null;
			String finishdate =null;
			String createDate = null;
			if( hash.get("ERDAT")!=null){
				createDate = hash.get("ERDAT").toString();  //记录创建日期
			}
			if( hash.get("PLFAZ")!=null){
				startdate = hash.get("PLFAZ").toString(); //开始日期
			}
			if( hash.get("PLSEZ")!=null){
				finishdate = hash.get("PLSEZ").toString(); //开始日期
			}
			//p判断当前主数据中是否存在
			
			HashMap conditionMap = new HashMap();
			conditionMap.put("XMBH", pj_no);
			List<HashMap> list = DemAPI.getInstance().getList(PROJECT_DEM_UUID, conditionMap, null);
			
			if(list!=null&&list.size()>0){
				HashMap data = list.get(0);
				if(data!=null){
//					data.put("WBSMS", description);
//					data.put("LSXWBS", wbs_right);
//					data.put("PARENTWBS", description);
//					data.put("WBSMS", wbs_up);
//					data.put("GSDM", "3003");
//					data.put("GC", plant);
//					data.put("XMLX", pjType);
					if(startdate!=null){
						Date sdate = UtilDate.StringToDate(startdate,"yyyyMMdd");
						data.put("KSRQ", sdate);
					}
					if(finishdate!=null){
						Date sdate = UtilDate.StringToDate(finishdate,"yyyyMMdd");
						data.put("JSRQ", finishdate);
					}
					Long instanceid = Long.parseLong(data.get("INSTANCEID").toString());
					Long dataid = Long.parseLong(data.get("ID").toString());
					//更新操作，暂时不记录日志信息
					//boolean flag = DemAPI.getInstance().updateFormData(WBS_DEM_UUID, instanceid, data, dataid, false);
					String logTxt =  "WBS主数据["+pj_no+"]["+pj_name+"]同步成功--[更新]";
//					if(flag){
						//SysLogAPI.getInstance().addOperationLog("synchronizeWBSMdmInfo", "BD_MDM_WBS", "更新", dataid.toString(), "WBS主数据同步成功");
//					}
				}
			}else{
				HashMap data = new HashMap();
				data.put("CREATUSER", SecurityUtil.supermanager);
				data.put("CREATTIME",UtilDate.getNowdate());
				data.put("XMMC", pj_name);
				data.put("XMBH", pj_no);
				data.put("XMFZR", verna);
				data.put("YZZT", txt30);
				data.put("XMZT", txt30);
				
				if(startdate!=null){
					Date sdate = UtilDate.StringToDate(startdate,"yyyyMMdd");
					data.put("STARTDATE", sdate);
				}
				if(finishdate!=null){
					Date sdate = UtilDate.StringToDate(finishdate,"yyyyMMdd");
					data.put("ENDDATE", finishdate);
				}
				Long newinstanceid = DemAPI.getInstance().newInstance(PROJECT_DEM_UUID, SecurityUtil.supermanager);
				boolean flag = DemAPI.getInstance().saveFormData(PROJECT_DEM_UUID, newinstanceid, data, false);
				if(flag){
					String logTxt =  "项目主数据主数据["+pj_no+"]["+pj_name+"]同步成功--[新增]";
					//SysLogAPI.getInstance().addOperationLog("synchronizeWBSMdmInfo", "BD_MDM_WBS", "新增", newinstanceid.toString(),logTxt);
				}
			}
		}
		
	}
	private void synchronizeWBSMdmInfo(List<HashMap> wbslist){
		for(HashMap hash:wbslist){
			String wbs_element = "";
			String pj_desc ="";
			String description = "";
			String wbs_right = "";
			String wbs_up = "";
			String plant =  "";
			String pjType ="";
			String comp_code ="";
			if(hash.get("WBS_ELEMENT")!=null)wbs_element = hash.get("WBS_ELEMENT").toString();
			if(hash.get("PROJECT_DEFINITION")!=null)pj_desc = hash.get("PROJECT_DEFINITION").toString();//项目定义
			if(hash.get("DESCRIPTION")!=null)description = hash.get("DESCRIPTION").toString();//项目定义
			if(hash.get("WBS_RIGHT")!=null)wbs_right = hash.get("WBS_RIGHT").toString(); //临上项
			if(hash.get("WBS_UP")!=null)wbs_up = hash.get("WBS_UP").toString();  //父项
			if(hash.get("PLANT")!=null)plant = hash.get("PLANT").toString();  //工厂
			if(hash.get("PROJ_TYPE")!=null)pjType = hash.get("PROJ_TYPE").toString();  //项目类型
			if(hash.get("REQUEST_COMP_CODE")!=null)comp_code = hash.get("REQUEST_COMP_CODE").toString();  //项目类型
			
			String startdate =null;
			String finishdate =null;
			if( hash.get("WBS_ACTUAL_START_DATE")!=null){
				startdate = hash.get("WBS_ACTUAL_START_DATE").toString(); //开始日期
			}
			if( hash.get("WBS_ACTUAL_FINISH_DATE")!=null){
				finishdate = hash.get("WBS_ACTUAL_FINISH_DATE").toString(); //开始日期
			}
			//p判断当前主数据中是否存在
			
			HashMap conditionMap = new HashMap();
			conditionMap.put("XMDY", pj_desc);
			conditionMap.put("WBSYS", wbs_element);
			List<HashMap> list = DemAPI.getInstance().getList(WBS_DEM_UUID, conditionMap, null);
			
			if(list!=null&&list.size()>0){
				HashMap data = list.get(0);
				if(data!=null){
//					data.put("WBSMS", description);
//					data.put("LSXWBS", wbs_right);
//					data.put("PARENTWBS", description);
//					data.put("WBSMS", wbs_up);
//					data.put("GSDM", "3003");
//					data.put("GC", plant);
//					data.put("XMLX", pjType);
					if(startdate!=null){
						Date sdate = UtilDate.StringToDate(startdate,"yyyyMMdd");
						data.put("KSRQ", sdate);
					}
					if(finishdate!=null){
						Date sdate = UtilDate.StringToDate(finishdate,"yyyyMMdd");
						data.put("JSRQ", finishdate);
					}
					Long instanceid = Long.parseLong(data.get("INSTANCEID").toString());
					Long dataid = Long.parseLong(data.get("ID").toString());
					//更新操作，暂时不记录日志信息
					//boolean flag = DemAPI.getInstance().updateFormData(WBS_DEM_UUID, instanceid, data, dataid, false);
					String logTxt =  "WBS主数据["+wbs_element+"]["+description+"]同步成功--[更新]";
//					if(flag){
					//SysLogAPI.getInstance().addOperationLog("synchronizeWBSMdmInfo", "BD_MDM_WBS", "更新", dataid.toString(), "WBS主数据同步成功");
//					}
				}
			}else{
				HashMap data = new HashMap();
				data.put("XMDY", pj_desc);
				data.put("WBSYS", wbs_element);
				data.put("WBSMS", description);
				data.put("LSXWBS", wbs_right);
				data.put("PARENTWBS", wbs_up);
				data.put("GSDM", "3003");
				data.put("GC", plant);
				data.put("XMLX", pjType);
				if(startdate!=null){
					Date sdate = UtilDate.StringToDate(startdate,"yyyyMMdd");
					data.put("KSRQ", sdate);
				}
				if(finishdate!=null){
					Date sdate = UtilDate.StringToDate(finishdate,"yyyyMMdd");
					data.put("JSRQ", finishdate);
				}
				Long newinstanceid = DemAPI.getInstance().newInstance(WBS_DEM_UUID, SecurityUtil.supermanager);
				boolean flag = DemAPI.getInstance().saveFormData(WBS_DEM_UUID, newinstanceid, data, false);
				if(flag){
					String logTxt =  "WBS主数据["+wbs_element+"]["+description+"]同步成功--[新增]";
					//SysLogAPI.getInstance().addOperationLog("synchronizeWBSMdmInfo", "BD_MDM_WBS", "新增", newinstanceid.toString(),logTxt);
				}
			}
			
		}
		
	}
	
}
