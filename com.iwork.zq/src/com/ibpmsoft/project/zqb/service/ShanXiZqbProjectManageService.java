package com.ibpmsoft.project.zqb.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.dao.ShanXiZqbProjectManageDAO;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
public class ShanXiZqbProjectManageService {
	
	private final static String CN_FILENAME = "/common.properties";
	private ShanXiZqbProjectManageDAO shanXiZqbProjectManageDAO;
	private static Logger logger = Logger.getLogger(ShanXiZqbProjectManageService.class);
	public void setShanXiZqbProjectManageDAO(
			ShanXiZqbProjectManageDAO shanXiZqbProjectManageDAO) {
		this.shanXiZqbProjectManageDAO = shanXiZqbProjectManageDAO;
	}

	public Long getConfig(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
		long result=0L;
		if(parameter!=null&&!"".equals(parameter)){
			result=Long.parseLong(config.get(parameter));
		}
		return result;
	}
	
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public String xmlxCommit(Long instanceId, String projectNo) {
		String xmlxuuid = getConfigUUID("xmlxuuid");
		if(xmlxuuid!=null){
			OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
			String userid = userModel.getUserid();
			String username = userModel.getUsername();
			HashMap fromData = DemAPI.getInstance().getFromData(instanceId);
			Long lcbs=fromData.get("LCBS")==null?0:Long.parseLong(fromData.get("LCBS").toString().equals("")?"0":fromData.get("LCBS").toString());
			String taskid = "";
			String executionId = "";
			String actDefId ="";
			Long lcInstanceId = 0L;
			if(lcbs==0){
				actDefId= ProcessAPI.getInstance().getProcessActDefId("XMLXSPLC");
				Long formId = ProcessAPI.getInstance().getProcessDefaultFormId(actDefId);
				lcInstanceId = ProcessAPI.getInstance().newInstance(actDefId, formId, userid);
				HashMap lcFromData = new HashMap();
				lcFromData.put("TBR", username);
				lcFromData.put("TBRID", userid);
				lcFromData.put("TBSJ", UtilDate.getNowdate());
				lcFromData.put("PROJECTNAME", fromData.get("PROJECTNAME"));
				lcFromData.put("PROJECTNO", fromData.get("PROJECTNO"));
				lcFromData.put("CUSTOMERNO", fromData.get("CUSTOMERNO"));
				lcFromData.put("INSTANCEID", instanceId);
				lcFromData.put("LCINSTANCEID", lcInstanceId);
				lcFromData.put("JDMC", "项目立项");
				lcFromData.put("TJSJ", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				ProcessAPI.getInstance().saveFormData(actDefId, lcInstanceId, lcFromData, false);// 保存流程
				String jd1 = SystemConfig._xmlxSpLcConf.getJd1();
				Task newTaskId = ProcessAPI.getInstance().newTaskId(lcInstanceId);
				taskid = newTaskId.getId();
				executionId = newTaskId.getExecutionId();
				fromData.put("LCBH", actDefId);
				fromData.put("LCBS", lcInstanceId);
				fromData.put("STEPID", jd1);
				fromData.put("TASKID", taskid);
				fromData.put("ZHXGSJ", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				Long dataid = Long.parseLong(fromData.get("ID").toString());
				DemAPI.getInstance().updateFormData(xmlxuuid, instanceId, fromData, dataid, false);
			}else{
				executionId=lcbs.toString();
				lcInstanceId=lcbs;
				String jd1=fromData.get("STEPID").toString();
				actDefId= ProcessAPI.getInstance().getProcessActDefId("XMLXSPLC");
				List<Task> tasklist = ProcessAPI.getInstance().getUserProcessTaskList(actDefId, jd1,userid);
				if(tasklist.size()>0){
					for (Task task : tasklist) {
						if (Long.parseLong(task.getProcessInstanceId()) == lcInstanceId) {
							taskid = task.getId();
							executionId = task.getExecutionId();
						}
						
					}
				}else{
					Task t = ProcessAPI.getInstance().newTaskId(lcInstanceId);
					taskid = t.getId();
				}
			}
			StringBuffer jsonHtml = new StringBuffer();
			jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+lcInstanceId+"}");
			return jsonHtml.toString();
		}else{
			return null;
		}
	}
	
	public boolean setCustomerContent(Long instanceId){
		boolean flag = false;
		
		HashMap lxdata = DemAPI.getInstance().getFromData(instanceId);
		String gsgk = lxdata.get("GSGK")==null?"":lxdata.get("GSGK").toString();
		String xmbz = lxdata.get("XMBZ")==null?"":lxdata.get("XMBZ").toString();
		String customerinfo = lxdata.get("CUSTOMERINFO")==null?"":lxdata.get("CUSTOMERINFO").toString();
		String xmys = lxdata.get("XMYS")==null?"":lxdata.get("XMYS").toString();
		
		String customerno = lxdata.get("CUSTOMERNO")==null?"":lxdata.get("CUSTOMERNO").toString();
		HashMap conditionMap = new HashMap();
		conditionMap.put("CUSTOMERNO", customerno);
		String demUUID = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '客户主数据维护'", "UUID");
		List<HashMap> datalist = DemAPI.getInstance().getAllList(demUUID, conditionMap, "ID");
		if(datalist.size()==1){
			HashMap data = datalist.get(0);
			data.put("ZCQX", gsgk);
			data.put("SSHY", xmbz);
			data.put("ZWMC", customerinfo);
			data.put("JLNJLR", xmys);
			flag = DemAPI.getInstance().updateFormData(demUUID, Long.parseLong(data.get("INSTANCEID").toString()), data, Long.parseLong(data.get("ID").toString()), false);
		}
		return flag;
	}
	
	public String getCustomerContent(String customerno){
		List<String> lables = new ArrayList<String>();
		lables.add("ZCQX");
		lables.add("SSHY");
		lables.add("ZWMC");
		lables.add("JLNJLR");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO=?");
		Map params = new HashMap();
		params.put(1, customerno);
		List<HashMap> content = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		StringBuffer jsonHtml = new StringBuffer();
		if(content.size()==1){
			HashMap data = content.get(0);
			String zcqx = data.get("ZCQX")==null?"":data.get("ZCQX").toString();
			String sshy = data.get("SSHY")==null?"":data.get("SSHY").toString();
			String zwmc = data.get("ZWMC")==null?"":data.get("ZWMC").toString();
			String jlnjlr = data.get("JLNJLR")==null?"":data.get("JLNJLR").toString();
			jsonHtml.append("{\"ZCQX\":\""+zcqx+"\",\"SSHY\":\""+sshy+"\",\"ZWMC\":\""+zwmc+"\",\"JLNJLR\":\""+jlnjlr+"\"}");
		}else{
			jsonHtml.append("{\"ZCQX\":\"\",\"SSHY\":\"\",\"ZWMC\":\"\",\"JLNJLR\":\"\"}");
		}
		return jsonHtml.toString();
	}

	public String getXmlxContent(Long instanceId, String projectNo, String customerNo) {
		StringBuffer dcContent = new StringBuffer();
		StringBuffer sqbgContent = new StringBuffer();
		Long xmlxformid = getConfig("xmlxformid");
		Long xmlxdemid = getConfig("xmlxdemid");
		HashMap<String,Long> xmlxMapContent = getXmlxMapContent(instanceId,xmlxformid,projectNo,customerNo);
		for (Iterator setItr = xmlxMapContent.entrySet().iterator(); setItr.hasNext();) {
			Map.Entry<String, Long> entry = (Map.Entry<String, Long>) setItr.next();
			String key = entry.getKey();
			Long val = entry.getValue();
			if(key.equals("XMLX")){
				sqbgContent.append("<a href='javascript:void(0)' onclick='openFormPage(").append(xmlxformid).append(",").append(xmlxdemid).append(",").append(val).append(")").append("'>推荐挂牌项目立项申请报告</a>");
			}else if(key.equals("KH")){
				dcContent.append("<a href='javascript:void(0)' onclick='openFormPage(88,21,").append(val).append(")").append("'>推荐挂牌项目立项调查表</a>");
			}
		}
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append("{\"dcContent\":\""+dcContent.toString()+"\",\"sqbgContent\":\""+sqbgContent.toString()+"\"}");
		return jsonHtml.toString();
	}
	
	public String commitAuthority(String projectNo,String xmlx,String jdmc){
		boolean flag = false;
		Map params = new HashMap();
		params.put(1, projectNo);
		if(xmlx.equals("推荐挂牌项目")&&jdmc.equals("项目立项")){
			String manager= DBUTilNew.getDataStr("MANAGER","SELECT SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) AS MANAGER FROM BD_ZQB_PJ_BASE WHERE PROJECTNO= ? ", params);
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			flag = manager.equals(userid);
		}else if(xmlx.equals("推荐挂牌项目")&&!jdmc.equals("项目立项")){
			String manager= DBUTilNew.getDataStr("MANAGER","SELECT SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) AS MANAGER FROM BD_ZQB_PJ_BASE WHERE CUSTOMERNO= ? ", params);
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			flag = manager.equals(userid);
		}else if(xmlx.equals("并购项目")/*&&jdmc.equals("项目立项")*/){
			String manager= DBUTilNew.getDataStr("MANAGER","SELECT SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) AS MANAGER FROM BD_ZQB_BGZZLXXX WHERE XMBH= ? ", params);
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			flag = manager.equals(userid);
		}else if(xmlx.equals("股票发行项目")/*&&jdmc.equals("项目立项")*/){
			String manager= DBUTilNew.getDataStr("MANAGER","SELECT SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) AS MANAGER FROM BD_ZQB_GPFXXMB WHERE PROJECTNO= ? ", params);
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			flag = manager.equals(userid);
		}else if(xmlx.equals("一般性财务顾问项目")/*&&jdmc.equals("项目立项")*/){
			String manager= DBUTilNew.getDataStr("MANAGER","SELECT SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) AS MANAGER FROM BD_ZQB_CWGWXMXX WHERE XMBH= ? ", params);
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			flag = manager.equals(userid);
		}
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append("{\"authority\":\""+flag+"\"}");
		return jsonHtml.toString();
	}
	
	public HashMap getXmlxMapContent(Long instanceId, Long xmlxformid, String projectNo, String customerNo){
		HashMap<String,Long> result = new HashMap<String,Long>();
		StringBuffer sql = new StringBuffer("SELECT 'XMLX' TYPE,B.INSTANCEID FROM (SELECT ID FROM BD_ZQB_PJ_BASE WHERE PROJECTNO=?) A INNER JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=?) B ON A.ID=B.DATAID"
				+ " UNION"
				+ " SELECT 'KH' TYPE,B.INSTANCEID FROM (SELECT KH.ID FROM BD_ZQB_KH_BASE KH WHERE CUSTOMERNO=?) A INNER JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=?) B ON A.ID=B.DATAID");
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			//ps.setLong(1, instanceId);
			ps.setString(1, projectNo);
			ps.setLong(2, xmlxformid);
			ps.setString(3, customerNo);
			ps.setLong(4, 88);
			rs = ps.executeQuery();
			while (rs.next()) {
				String key = rs.getString("TYPE");
				Long instanceid = rs.getLong("INSTANCEID");
				result.put(key, instanceid);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}

	public String xmlxContent(String customerNo) {
		StringBuffer projectName = new StringBuffer();
		StringBuffer manager = new StringBuffer();
		if(customerNo!=null&&!customerNo.equals("")){
			StringBuffer sql = new StringBuffer("SELECT PROJECTNAME,MANAGER FROM BD_ZQB_PJ_BASE WHERE CUSTOMERNO = ?");
			Connection conn = DBUtil.open();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, customerNo);
				rs = ps.executeQuery();
				while (rs.next()) {
					projectName.append(rs.getString("PROJECTNAME"));
					manager.append(rs.getString("MANAGER"));
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, ps, rs);
			}
		}
		HashMap<String,String> returnMap = new HashMap<String,String>();
		returnMap.put("projectName", projectName.toString());
		returnMap.put("manager", manager.toString());
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
}
