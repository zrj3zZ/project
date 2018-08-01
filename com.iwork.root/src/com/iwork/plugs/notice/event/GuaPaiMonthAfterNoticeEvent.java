package com.iwork.plugs.notice.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;

public class GuaPaiMonthAfterNoticeEvent implements IWorkScheduleInterface {
	private static Logger logger = Logger.getLogger(GuaPaiMonthAfterNoticeEvent.class);
	private final static String CN_FILENAME = "/common.properties";

	public boolean executeAfter() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.dateFormat(new Date()) + "]-挂牌后一个月通知结束......");
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.dateFormat(new Date()) + "]-挂牌后一个月通知前...... ");
		sendMsg();
		sendMsgForGpfx();
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.dateFormat(new Date()) + "]-挂牌后一个月通知中...... ");
		return true;
	}
	
	public void sendMsgForGpfx(){
		List<HashMap> dataList = getDataListForGpfx();
		String demUUID = com.iwork.commons.util.DBUtil.getDataStr("UUID", "SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='申报资料信息'", null);
		UserContext uc = UserContextUtil.getInstance().getUserContext(SecurityUtil.supermanager);
		Long instanceid;
		Long dataid;
		Date updatedate = null;
		Date sysdate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (HashMap hashMap : dataList) {//项目资料需归档
			instanceid = Long.parseLong(hashMap.get("INSTANCEID").toString());
			dataid = Long.parseLong(hashMap.get("ID").toString());
			try {
				updatedate = sdf.parse(hashMap.get("UPDATEDATE").toString());
				sysdate = sdf.parse(hashMap.get("SYSDATE").toString());
			} catch (ParseException e) {
				logger.error(e,e);
			}
			HashMap formData = DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			formData.put("TZZT", 1);
			boolean flag = DemAPI.getInstance().updateFormData(demUUID, instanceid, formData, dataid, false);
			boolean bool = updatedate==null||sysdate==null?false:sysdate.after(updatedate);
			if(flag&&bool){
				String userid = hashMap.get("CREATEUSERID").toString();
				String projectname = hashMap.get("PROJECTNAME").toString();
				Object mobile = hashMap.get("MOBILE");
				if(mobile!=null&&!mobile.toString().equals("")&&mobile.toString().matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}")){
					MessageAPI.getInstance().sendSMS(uc, mobile.toString(),"股票发行项目:"+projectname+",资料需归档。");
				}
				MessageAPI.getInstance().sendSysMsg(userid,"定增项目资料归档提示",projectname+"项目资料需归档。");
			}
		}
	}
	
	public List<HashMap> getDataListForGpfx(){
		List lables = new ArrayList();
		lables.add("ID");
		lables.add("CREATEUSERID");
		lables.add("PROJECTNAME");
		lables.add("INSTANCEID");
		lables.add("MOBILE");
		lables.add("EMAIL");
		lables.add("UPDATEDATE");
		lables.add("SYSDATE");
		String sql = "SELECT B.ID,B.CREATEUSERID,B.PROJECTNAME,S.INSTANCEID,O.MOBILE,O.EMAIL,ADD_MONTHS(INS.UPDATEDATE,+1) UPDATEDATE,SYSDATE FROM BD_ZQB_GPFXXMSBZLSB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN ORGUSER O ON B.CREATEUSERID=O.USERID LEFT JOIN SYS_INSTANCE_DATA INS ON INS.ID=S.INSTANCEID WHERE S.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='申报资料信息') AND B.SPZT='审批通过' AND B.TZZT IS NULL";
		return com.iwork.commons.util.DBUtil.getDataList(lables, sql, null);
	}
	
 	public void sendMsg(){
		List<HashMap<String,Object>> dataList = getDataList();
		UserContext uc = UserContextUtil.getInstance().getUserContext(SecurityUtil.supermanager);
		for (HashMap<String, Object> hashMap : dataList) {
			String processActDefId = hashMap.get("LCBH").toString();
			Long instanceId = Long.parseLong(hashMap.get("INSTANCEID").toString());
			Long dataId = Long.parseLong(hashMap.get("GPID").toString());
			HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			dataMap.put("TZZT", 1);
			boolean flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if(flag){
				String userid = hashMap.get("USERID").toString();
				String gsmc = hashMap.get("GSMC").toString();
				String mobile = hashMap.get("MOBILE").toString();
				if(mobile.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}")){
					MessageAPI.getInstance().sendSMS(uc, mobile,gsmc+"项目资料需归档。");
				}
				MessageAPI.getInstance().sendSysMsg(userid,"挂牌登记及归档提醒",gsmc+"项目资料需归档。");
			}
		}
		
		List<HashMap<String,Object>> bgDataList = getBgDataList();
		String bgxmxxUUID = getConfigUUID("bgxmxxuuid");
		for (HashMap<String, Object> hashMap : bgDataList) {
			Long dataId = Long.parseLong(hashMap.get("DATAID").toString());
			Long instanceId = Long.parseLong(hashMap.get("INSTANCEID").toString());
			HashMap fromData = DemAPI.getInstance().getFromData(instanceId);
			//1是通知 0是未通知
			fromData.put("XMTZZT", 1);
			boolean flag=DemAPI.getInstance().updateFormData(bgxmxxUUID, instanceId, fromData, dataId, false);
			if(flag){
				String userid = hashMap.get("USERID").toString();
				String jyf = hashMap.get("JYF").toString();
				String mobile = hashMap.get("MOBILE").toString();
				if(mobile.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}")){
					MessageAPI.getInstance().sendSMS(uc, mobile,"并购重组项目【"+jyf+"】如已完成，请做资料归档，如项目终止，请关闭该项目。");
				}
				MessageAPI.getInstance().sendSysMsg(userid,"并购重组项目","并购重组项目【"+jyf+"】如已完成，请做资料归档，如项目终止，请关闭该项目。");
			}
		}
		
		List<HashMap<String,Object>> cwDataList = getCwDataList();
		String cwgwxxUUID = getConfigUUID("cwgwxxuuid");
		for (HashMap<String, Object> hashMap : cwDataList) {
			Long dataId = Long.parseLong(hashMap.get("DATAID").toString());
			Long instanceId = Long.parseLong(hashMap.get("INSTANCEID").toString());
			HashMap fromData = DemAPI.getInstance().getFromData(instanceId);
			//1是通知 0是未通知
			fromData.put("XMTZZT", 1);
			boolean flag=DemAPI.getInstance().updateFormData(cwgwxxUUID, instanceId, fromData, dataId, false);
			if(flag){
				String userid = hashMap.get("USERID").toString();
				String customername = hashMap.get("CUSTOMERNAME").toString();
				String mobile = hashMap.get("MOBILE").toString();
				if(mobile.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}")){
					MessageAPI.getInstance().sendSMS(uc, mobile,"一般性财务顾问项目【"+customername+"】如已完成，请做资料归档，如项目终止，请关闭该项目。");
				}
				MessageAPI.getInstance().sendSysMsg(userid,"一般性财务顾问项目","一般性财务顾问项目【"+customername+"】如已完成，请做资料归档，如项目终止，请关闭该项目。");
			}
		}
	}
	
	public List<HashMap<String, Object>> getDataList(){
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT GPDATA.TBRID,ORG.MOBILE,GPDATA.GSMC,GPDATA.INSTANCEID,GPDATA.LCBH,GPDATA.ID FROM (SELECT ID,INSTANCEID,TBRID,GSMC,SPZT,LCBH,TZZT FROM BD_ZQB_GPDJJGD WHERE TO_CHAR(ADD_MONTHS(GPRQ,+1),'YYYY-MM-DD') <= TO_CHAR(SYSDATE,'YYYY-MM-DD')) GPDATA INNER JOIN ORGUSER ORG ON GPDATA.TBRID=ORG.USERID AND GPDATA.SPZT!='审批通过' AND ORG.MOBILE IS NOT NULL AND GPDATA.TZZT = 0");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				String tbrid = rs.getString("TBRID");
				String gsmc = rs.getString("GSMC");
				String mobile = rs.getString("MOBILE");
				Long instanceid = rs.getLong("INSTANCEID");
				String lcbh = rs.getString("LCBH");
				Long gpid = rs.getLong("ID");
				result.put("USERID", tbrid);
				result.put("MOBILE", mobile);
				result.put("GSMC", gsmc);
				result.put("INSTANCEID", instanceid);
				result.put("LCBH", lcbh);
				result.put("GPID", gpid);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public List<HashMap<String, Object>> getBgDataList(){
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT BGCZLX.ID,BGCZLX.JYF,BGCZLX.INSTANCEID,SBZL.TBRID,ORG.MOBILE FROM (SELECT TBRID,XMBH,LCBH,LCBS,TASKID FROM BD_ZQB_BGSBZL WHERE SPZT='审批通过') SBZL "
				+ " INNER JOIN ORGUSER ORG ON SBZL.TBRID=ORG.USERID "
				+ " INNER JOIN (SELECT ID,JYF,XMBH,INSTANCEID FROM BD_ZQB_BGZZLXXX WHERE XMTZZT=0) BGCZLX ON SBZL.XMBH=BGCZLX.XMBH "
				+ " INNER JOIN PROCESS_HI_TASKINST HTASK ON SBZL.LCBH=HTASK.PROC_DEF_ID_ AND SBZL.TASKID=HTASK.ID_ AND SBZL.LCBS=HTASK.EXECUTION_ID_  "
				+ " AND TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(END_TIME_,'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS'),+1),'YYYY-MM-DD') <= TO_CHAR(SYSDATE,'YYYY-MM-DD')");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jyf = rs.getString("JYF");
				Long instanceid = rs.getLong("INSTANCEID");
				String tbrid = rs.getString("TBRID");
				String mobile = rs.getString("MOBILE");
				result.put("DATAID", id);
				result.put("JYF", jyf);
				result.put("INSTANCEID", instanceid);
				result.put("USERID", tbrid);
				result.put("MOBILE", mobile);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public List<HashMap<String, Object>> getCwDataList(){
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT CWGWXX.ID,CWGWXX.CUSTOMERNAME,CWGWXX.INSTANCEID,GZJDHB.TBRID,ORG.MOBILE FROM (SELECT TBRID,XMBH,LCBH,LCBS,TASKID FROM BD_ZQB_CWGZJDHB WHERE SPZT='审批通过') GZJDHB "
				+ " INNER JOIN ORGUSER ORG ON GZJDHB.TBRID=ORG.USERID "
				+ " INNER JOIN (SELECT ID,CUSTOMERNAME,XMBH,INSTANCEID FROM BD_ZQB_CWGWXMXX WHERE XMTZZT=0) CWGWXX ON GZJDHB.XMBH=CWGWXX.XMBH "
				+ " INNER JOIN PROCESS_HI_TASKINST HTASK ON GZJDHB.LCBH=HTASK.PROC_DEF_ID_ AND GZJDHB.TASKID=HTASK.ID_ AND GZJDHB.LCBS=HTASK.EXECUTION_ID_  "
				+ " AND TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(END_TIME_,'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS'),+1),'YYYY-MM-DD') <= TO_CHAR(SYSDATE,'YYYY-MM-DD')");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String customername = rs.getString("CUSTOMERNAME");
				Long instanceid = rs.getLong("INSTANCEID");
				String tbrid = rs.getString("TBRID");
				String mobile = rs.getString("MOBILE");
				result.put("DATAID", id);
				result.put("CUSTOMERNAME", customername);
				result.put("INSTANCEID", instanceid);
				result.put("USERID", tbrid);
				result.put("MOBILE", mobile);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}
}
