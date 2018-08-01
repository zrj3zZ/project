package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.Region;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ibpmsoft.project.zqb.common.ZQBConstants;
import com.ibpmsoft.project.zqb.common.ZQBRoleConstants;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.dao.ZqbGpfxProjectManageDAO;
import com.ibpmsoft.project.zqb.model.UploadDocModel;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.conf.XmlcConf;
import com.iwork.app.log.util.LogUtil;
import com.iwork.app.message.sysmsg.dao.SysMessageDAO;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.OrganizationAPI;
import com.iwork.sdk.ProcessAPI;
public class ZqbGpfxProjectManageService {
	private static Logger logger = Logger.getLogger(ZqbGpfxProjectManageService.class);
	private String ProjectGroupUUID = "9f5b040a07524477bb4b6ca57b793a02";
	private static final String ZQB_CUSTOMER_PURVIEW_UUID = "84ff70949eac4051806dc02cf4837bd9";
	private final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private HashMap projectSetMap;
	private ZqbGpfxProjectManageDAO zqbGpfxProjectManageDAO;
	
	public ZqbGpfxProjectManageDAO getZqbGpfxProjectManageDAO() {
		return zqbGpfxProjectManageDAO;
	}

	public void setZqbGpfxProjectManageDAO(
			ZqbGpfxProjectManageDAO zqbGpfxProjectManageDAO) {
		this.zqbGpfxProjectManageDAO = zqbGpfxProjectManageDAO;
	}

	public Long getConfig(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		long result=0L;
		if(parameter!=null&&!"".equals(parameter)){
			result=Long.parseLong(config.get(parameter));
		}
		return result;
	}
	
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}
	
	public boolean removeItem(String projectNo, Long taskid, Long instanceid) {
		return DemAPI.getInstance().removeFormData(instanceid);
	}

	/**
	 * 获取项目文档列表
	 * 
	 * @return
	 */
	public List<UploadDocModel> showProjectDocList(String searchkey) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		boolean isSuperMan = this.getIsSuperMan();
		String owner = "";
		if (!isSuperMan) {
			owner = uc._userModel.getUserid();// + "["
					//+ uc._userModel.getUsername() + "]";
		}
		return zqbGpfxProjectManageDAO.getDocList(searchkey, owner);
	}

	/**
	 * 按类型进行划分，获取当前用户权限范围内的项目饼图
	 * 
	 * @return
	 */
	public String[] getProjectTypeBarChart3(boolean isSuperman) {

		StringBuffer jsonHtml = new StringBuffer();
		// 判断当前用户是否为
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		List<HashMap> templist = DemAPI.getInstance().getList(ProjectUUID,
				null, null);
		List<String> userlist = zqbGpfxProjectManageDAO.getProjectGroupList();
		HashMap infodata = new HashMap();
		for (String userid : userlist) {
			List l = zqbGpfxProjectManageDAO.getUserJs(userid);
			for (HashMap project : templist) {
				UserContext context = UserContextUtil.getInstance()
						.getUserContext(userid);
				boolean flag = this.checkProjectSecurity(project, context);
				if (!flag)
					continue;
				project.put("POSITION", ((List) l.get(0)).get(0));
				project.put("NAME", ((List) l.get(0)).get(1));
				// Object obj = infodata.get(project.get("NAME"));
				Object obj = infodata.get(userid);
				if (obj == null) {
					// infodata.put(project.get("NAME"), new Long(1));
					infodata.put(userid, new Long(1));
				} else {
					// Long num =
					// Long.parseLong(infodata.get(project.get("NAME")).toString());
					Long num = Long.parseLong(infodata.get(userid).toString());
					num++;
					// infodata.put(project.get("NAME"),num);
					infodata.put(userid, num);
				}

			}

		}
		List slist = new ArrayList();
		Iterator iter = infodata.entrySet().iterator();
		List lablelist = new ArrayList();
		List valuelist = new ArrayList();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			// String name =
			// UserContextUtil.getInstance().getFullUserAddress(key.toString());
			Object val = entry.getValue();
			List item = new ArrayList();
			// item.add(e);
			lablelist.add(key);
			valuelist.add(val);
			slist.add(item);

		}

		StringBuffer label = new StringBuffer();
		StringBuffer value = new StringBuffer();

		JSONArray list1 = JSONArray.fromObject(lablelist);
		JSONArray list2 = JSONArray.fromObject(valuelist);
		label.append(list1);
		value.append(list2);
		String[] data = { label.toString(), value.toString() };
		return data;
	}

	public boolean drawUpdateItem(String projectName, String projectNo,
			Long taskid, Long instanceid, String startDate, String endDate) {
		Long formId = DemAPI.getInstance().getFormIdByInstanceId(
				new Long(instanceid), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		HashMap hash = DemAPI.getInstance().getFromData(instanceid, formId,
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String formatStr = "yyyy-MM-dd";
		// Date start = UtilDate.StringToDate(startDate, formatStr);
		// Date end = UtilDate.StringToDate(endDate, formatStr);
		hash.put("STARTDATE", startDate);
		hash.put("ENDDATE", endDate);
		hash.put("projectName", projectName);
		// ============================消息提醒===============================================

		String smsContent = "";
		String sysMsgContent = "";
		smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(
				ZQB_Notice_Constants.PROJECT_TASK_UPDATE_TIME_KEY, hash);
		sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(
				ZQB_Notice_Constants.PROJECT_TASK_UPDATE_TIME_KEY, hash);
		String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(
				ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		UserContext target = UserContextUtil.getInstance().getUserContext(
				userid);
		if (target != null) {
			if (!smsContent.equals("")) {
				String mobile = target.get_userModel().getMobile();
				if (mobile != null && !mobile.equals("")) {
					MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
				}
			}
			if (!sysMsgContent.equals("")) {
				MessageAPI.getInstance().sendSysMsg(userid, "项目任务时间变更",
						sysMsgContent);
			}
		}
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		return DemAPI.getInstance().updateFormData(ProjectItemUUID, instanceid,
				hash, taskid, false);
	}

	/**
	 * 加载甘特图json
	 * 
	 * @param projectNo
	 * @return
	 */
	public String loadGanttJson(String projectNo) {
		List<HashMap> list = new ArrayList();
		StringBuffer jsonHtml = new StringBuffer();
		// 获得阶段列表
		List<HashMap> groupList = this.getGroupList(projectNo);
		for (HashMap group : groupList) {
			HashMap hash = new HashMap();
			hash.put("id", group.get("ID"));
			hash.put("name", group.get("GROUPNAME"));
			List<HashMap> taskList = this.getTaskList(projectNo, group
					.get("ID").toString());
			List<HashMap> sublist = new ArrayList();
			int count = 0;
			for (HashMap task : taskList) {
				count++;
				HashMap valueMap = new HashMap();
				valueMap.put("id", task.get("ID"));
				valueMap.put("name", task.get("TASK_NAME"));
				String startDate = null;
				String endDate = null;
				if (task.get("STARTDATE") != null) {
					startDate = task.get("STARTDATE").toString();
				}
				if (task.get("ENDDATE") != null) {
					endDate = task.get("ENDDATE").toString();
				}
				valueMap.put("start", startDate);
				valueMap.put("end", endDate);

				valueMap.put("priority", task.get("PRIORITY"));
				valueMap.put("scale", task.get("SCALE"));
				valueMap.put("memo", task.get("MEMO"));
				valueMap.put("manager", task.get("MANAGER"));
				valueMap.put("instanceid", task.get("INSTANCEID"));
				int scale = 0;
				if (task.get("SCALE") != null) {
					try {
						scale = Integer.parseInt(task.get("SCALE").toString());
					} catch (NumberFormatException e) {
					}
				}
				String color = "FFFFCC";
				String text = "";
				if (scale == 100) {
					color = "#efefef";
					text = "✔已完成" + scale + "%";

				} else if (scale == 0) {
					color = "#FFFFCC";
					text = "计划中";
				} else {
					text = "计划中";
					color = "#FFFFCC";
				}
				String taskid = task.get("ID").toString();
				List<HashMap> questionList = this.getQuestionList(projectNo,
						Long.parseLong(taskid));
				if (questionList != null && questionList.size() > 0) {
					StringBuffer questr = new StringBuffer();
					for (HashMap hm : questionList) {
						questr.append(hm.get("QUESTION").toString());
					}
					StringBuffer questionIcon = new StringBuffer();
					questionIcon
							.append("<a href=\"javascript:showQuestion('")
							.append(task.get("TASK_NAME"))
							.append("','")
							.append(projectNo)
							.append("',")
							.append(taskid)
							.append(");\"><img alt ='")
							.append(questr.toString())
							.append("' src='iwork_img/icon/help_blue.gif'></a>");
					// 判断是否有回复过
					List<HashMap> reTalkList = this.getCommitQuestionList(
							projectNo, Long.parseLong(taskid), null);
					if (reTalkList != null && reTalkList.size() > 0) {
						questionIcon
								.append("&nbsp;<a href=\"javascript:showQuestion('")
								.append(task.get("TASK_NAME")).append("','")
								.append(projectNo).append("',").append(taskid)
								.append(");\"><img alt ='")
								.append(questr.toString())
								.append("' src='iwork_img/comments.png'></a>");
					}
					text += questionIcon.toString();
				}
				valueMap.put("color", color);
				valueMap.put("text", text);
				sublist.add(valueMap);
			}
			hash.put("series", sublist);
			list.add(hash);
		}
		// ===========================================
		JSONArray json = JSONArray.fromObject(list);
		jsonHtml.append(json);
		String temp = jsonHtml.toString();
		temp = temp.replace("\"new Date(", "new Date(").replace(")\"", ")");
		// temp = temp.replace("_text", "text");
		return temp;
	}

	public List<HashMap> loadGanttJson1(String projectNo) {
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		List<HashMap> list = DemAPI.getInstance().getList(ProjectItemUUID,
				conditionMap, "ID");
		List<HashMap> list1 = DemAPI.getInstance().getList(ProjectGroupUUID,
				conditionMap, "ID");
		for (HashMap hashMap : list) {
			for (HashMap hashMap1 : list1) {
				if (hashMap.get("GROUPID").toString()
						.equals(hashMap1.get("ID").toString())) {
					hashMap.put("GROUPNAME", hashMap1.get("GROUPNAME")
							.toString());
				}
			}
		}
		/*
		 * List<HashMap> list = new ArrayList(); StringBuffer jsonHtml = new
		 * StringBuffer(); // 获得阶段列表 List<HashMap> groupList =
		 * this.getGroupList(projectNo); for (HashMap group : groupList) {
		 * HashMap hash = new HashMap(); hash.put("id", group.get("ID"));
		 * hash.put("name", group.get("GROUPNAME")); List<HashMap> taskList =
		 * this.getTaskList(projectNo, group .get("ID").toString());
		 * List<HashMap> sublist = new ArrayList(); int count = 0; for (HashMap
		 * task : taskList) { count++; HashMap valueMap = new HashMap();
		 * valueMap.put("id", task.get("ID")); valueMap.put("name",
		 * task.get("TASK_NAME")); String startDate = null; String endDate =
		 * null; if (task.get("STARTDATE") != null) { startDate =
		 * task.get("STARTDATE").toString(); } if (task.get("ENDDATE") != null)
		 * { endDate = task.get("ENDDATE").toString(); } valueMap.put("start",
		 * startDate); valueMap.put("end", endDate);
		 * 
		 * valueMap.put("priority", task.get("PRIORITY")); valueMap.put("scale",
		 * task.get("SCALE")); valueMap.put("memo", task.get("MEMO"));
		 * valueMap.put("manager", task.get("MANAGER"));
		 * valueMap.put("instanceid", task.get("INSTANCEID")); int scale = 0; if
		 * (task.get("SCALE") != null) { try { scale =
		 * Integer.parseInt(task.get("SCALE").toString()); } catch
		 * (NumberFormatException e) { } } String color = "FFFFCC"; String text
		 * = ""; if (scale == 100) { color = "#efefef"; text = "✔已完成" + scale +
		 * "%";
		 * 
		 * } else if (scale == 0) { color = "#FFFFCC"; text = "计划中"; } else {
		 * text = "计划中"; color = "#FFFFCC"; } String taskid =
		 * task.get("ID").toString(); List<HashMap> questionList =
		 * this.getQuestionList(projectNo, Long.parseLong(taskid)); if
		 * (questionList != null && questionList.size() > 0) { StringBuffer
		 * questr = new StringBuffer(); for (HashMap hm : questionList) {
		 * questr.append(hm.get("QUESTION").toString()); } StringBuffer
		 * questionIcon = new StringBuffer(); questionIcon
		 * .append("<a href=\"javascript:showQuestion('")
		 * .append(task.get("TASK_NAME")) .append("','") .append(projectNo)
		 * .append("',") .append(taskid) .append(");\"><img alt ='")
		 * .append(questr.toString())
		 * .append("' src='iwork_img/icon/help_blue.gif'></a>"); // 判断是否有回复过
		 * List<HashMap> reTalkList = this.getCommitQuestionList( projectNo,
		 * Long.parseLong(taskid), null); if (reTalkList != null &&
		 * reTalkList.size() > 0) { questionIcon
		 * .append("&nbsp;<a href=\"javascript:showQuestion('")
		 * .append(task.get("TASK_NAME")).append("','")
		 * .append(projectNo).append("',").append(taskid)
		 * .append(");\"><img alt ='") .append(questr.toString())
		 * .append("' src='iwork_img/comments.png'></a>"); } text +=
		 * questionIcon.toString(); } valueMap.put("color", color);
		 * valueMap.put("text", text); sublist.add(valueMap); }
		 * hash.put("series", sublist); list.add(hash); } //
		 * =========================================== JSONArray json =
		 * JSONArray.fromObject(list); jsonHtml.append(json); String temp =
		 * jsonHtml.toString(); temp = temp.replace("\"new Date(",
		 * "new Date(").replace(")\"", ")"); // temp = temp.replace("_text",
		 * "text");
		 */return list;
	}

	/**
	 * 获得列表
	 * 
	 * @param processNo
	 * @return
	 */
	private List<HashMap> getQuestionList(String processNo, Long taskno) {
		HashMap conditionMap = new HashMap();
		conditionMap.put("XMBH", processNo);
		conditionMap.put("TASKNO", taskno);
		//股票发行问题反馈表单
		String ProjectQuestionUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行问题反馈表单'", "UUID");
		return DemAPI.getInstance().getList(ProjectQuestionUUID, conditionMap,
				"ID");
	}

	/**
	 * 获得阶段列表
	 * 
	 * @return
	 */
	private List<HashMap> getGroupList(String processNo) {
		HashMap conditionMap = new HashMap();
		// conditionMap.put("PROJECTNO", processNo);
		return DemAPI.getInstance().getList(ProjectGroupUUID, conditionMap,
				"ID");
	}

	/**
	 * 获得阶段列表
	 * 
	 * @return
	 */
	private List<HashMap> getTaskList(String processNo, String groupNo) {
		HashMap conditionMap = new HashMap();
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		conditionMap.put("PROJECTNO", processNo);
		if (groupNo != null) {
			conditionMap.put("GROUPID", groupNo);
		}
		return DemAPI.getInstance().getList(ProjectItemUUID, conditionMap,
				"ORDERINDEX");
	}

	private List<HashMap> getTaskMList(String groupid) {
		return zqbGpfxProjectManageDAO.getTaskMList(groupid);
	}

	/**
	 * 获得阶段列表
	 * 
	 * @return
	 */
	private List<HashMap> getTaskListForOwner(String userAddress) {
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		HashMap conditionMap = new HashMap();
		conditionMap.put("MANAGER", userAddress);
		return DemAPI.getInstance().getList(ProjectItemUUID, conditionMap,
				"ORDERINDEX");
	}

	/**
	 * 获取当前用户负责的，正在执行的项目列表
	 * 
	 * @return
	 */
	public List<HashMap> getSearchProjectList(boolean superman, String type,
			String searchkey) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		HashMap conditionMap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		List<HashMap> list = new ArrayList();
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		String key = "";
		if (searchkey.indexOf(",") > 0) {
			key = searchkey.substring(0, searchkey.indexOf(","));
		} else {
			key = searchkey.replaceAll("\\d", "");
		}
		if (type.equals("SCORE")) {
			String scoreType = searchkey.contains(",")?searchkey.substring(0, searchkey.indexOf(",")):searchkey.replaceAll("\\d", "");
			List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
					conditionMap, "ID");
			Collections.reverse(tmplist);
			List<HashMap> projectNoList = this.getRunProjectList(superman);
			if (scoreType.equals("关闭项目")) {
				for (HashMap hash : tmplist) {
					HashMap condition = new HashMap();
					condition.put("PROJECTNO", hash.get("PROJECTNO"));
					List<HashMap> l = DemAPI.getInstance().getList(
							ProjectItemUUID, condition, "ID desc");
					String updateDate = "";
					if (l.size() > 0 && l.get(0).get("GXSJ") != null) {
						try {
							updateDate = "".equals(l.get(0).get("GXSJ").toString())==true?"":sd.format(sd.parse(l.get(0)
									.get("GXSJ").toString()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							logger.error(e,e);
						}
					} else {
						updateDate = "";
					}
					hash.put("GXSJ", updateDate);
					if (!superman) {
						boolean flag = this.checkProjectSecurity(hash, uc);
						if (!flag)
							continue;
					}
					String status = "";
					String xmjd = "";
					if (hash.get("STATUS") != null
							&& !"".equals(hash.get("STATUS").toString())) {
						status = hash.get("STATUS").toString();
						xmjd = hash.get("XMJD").toString();
					}
					if (status.equals("已完成") && !xmjd.equals("持续督导")) {
						list.add(hash);
					}
				}
			}

			/*if (scoreType.equals("超期项目")) {
				for (HashMap hash : tmplist) {
					// if(!checkIsExists(hash)){
					// continue;
					// }
					HashMap condition = new HashMap();
					condition.put("PROJECTNO", hash.get("PROJECTNO"));
					List<HashMap> l = DemAPI.getInstance().getList(
							ProjectItemUUID, condition, "ID desc");
					String updateDate = "";
					if (l.size() > 0 && l.get(0).get("GXSJ") != null) {
						try {
							updateDate = "".equals(l.get(0).get("GXSJ").toString())==true?"":sd.format(sd.parse(l.get(0)
									.get("GXSJ").toString()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							logger.error(e,e);
						}
					} else {
						updateDate = "";
					}
					hash.put("GXSJ", updateDate);
					if (!superman) {
						boolean flag = this.checkProjectSecurity(hash, uc);
						if (!flag)
							continue;
					}
					if (zqbGpfxProjectManageDAO.isProjectCQXM(hash.get("PROJECTNO")
							.toString())) {
						list.add(hash);
					}
				}
			}*/
			if (scoreType.equals("正常项目")) {
				for (HashMap hash : tmplist) {
					HashMap condition = new HashMap();
					condition.put("PROJECTNO", hash.get("PROJECTNO"));
					List<HashMap> l = DemAPI.getInstance().getList(
							ProjectItemUUID, condition, "ID desc");
					String updateDate = "";
					if (l.size() > 0 && l.get(0).get("GXSJ") != null) {
						try {
							updateDate = "".equals(l.get(0).get("GXSJ").toString())==true?"":sd.format(sd.parse(l.get(0)
									.get("GXSJ").toString()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							logger.error(e,e);
						}
					} else {
						updateDate = "";
					}
					hash.put("GXSJ", updateDate);
					if (!superman) {
						boolean flag = this.checkProjectSecurity(hash, uc);
						if (!flag)
							continue;
					}
					if (zqbGpfxProjectManageDAO.isProjectZCXM(hash.get("PROJECTNO")
							.toString())) {
						list.add(hash);
					}
				}
			}
			if (scoreType.equals("60-79分")) {
				for (HashMap hash : tmplist) {
					if (!superman) {
						boolean flag = this.checkProjectSecurity(hash, uc);
						if (!flag)
							continue;
					}
					int score = 0;
					if (hash.get("FXPGFS") != null
							&& !"".equals(hash.get("FXPGFS").toString())) {
						score = Integer.parseInt(hash.get("FXPGFS").toString());
					}
					if (score >= 60 && score < 80) {
						list.add(hash);
					}
				}
			}
		} else if (type.equals("OWNER")) {
			List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
					null, "ID DESC");
			for (HashMap map : tmplist) {
				String userid = UserContextUtil.getInstance().getUserId(key);
				UserContext context = UserContextUtil.getInstance()
						.getUserContext(userid);
				if (context != null) {
					boolean flag = this.checkProjectSecurity(map, context);
					if (flag) {
						list.add(map);
					}
				}

			}

		} else {
			String owner = uc._userModel.getUserid();// + "["
					//+ uc._userModel.getUsername() + "]";
			// 判断是否是超权限用户
			conditionMap.put(type, key.equals("无阶段")?"项目开发":key);
			if (key.equals("已完成")) {
				conditionMap.put("STATUS", "已完成");
			}

			List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
					conditionMap, "ID DESC");
			for (HashMap data : tmplist) {
				HashMap condition = new HashMap();
				condition.put("PROJECTNO", data.get("PROJECTNO"));
				List<HashMap> l = DemAPI.getInstance().getList(ProjectItemUUID,
						condition, "ID desc");
				String updateDate = "";
				if (l.size() > 0 && l.get(0).get("GXSJ") != null) {
					try {
						updateDate = "".equals(l.get(0).get("GXSJ").toString())==true?"":sd.format(sd.parse(l.get(0).get("GXSJ")
								.toString()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				} else {
					updateDate = "";
				}
				data.put("GXSJ", updateDate);
				if (!superman) {
					boolean flag = this.checkProjectSecurity(data, uc);
					if (!flag)
						continue;
				}
				String purview = this.isPurview(data,
						ZQBConstants.ISPURVIEW_TYPE_PROJECT_KEY);
				if (purview.equals(ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER)) {
					data.put("ISPURVIEW", true);
					list.add(data);
				} else if (purview
						.equals(ZQBConstants.ISPURVIEW_ROLE_TYPE_VISITR)
						|| superman) {
					data.put("ISPURVIEW", false);
					list.add(data);
				} else if (purview
						.equals(ZQBConstants.ISPURVIEW_ROLE_TYPE_GUEST)) {
					continue;
				}
			}
		}
		// Collections.reverse(list);
		return list;
	}

	public boolean checkIsExists(HashMap hash) {
		boolean flag = false;
		flag = zqbGpfxProjectManageDAO.getNumber(hash);
		return flag;
	}

	/**
	 * 获取当前用户负责的，正在执行的项目列表
	 * 
	 * @return
	 */
	public List<HashMap> getRunProjectList(boolean superman) {

		HashMap conditionMap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		// 判断是否是超权限用户
		conditionMap.put("STATUS", "执行中");
		// String[] param =
		// {"项目开发","签署协议","股改","尽职调查","申报材料","内核","内核反馈","申报","申报反馈"};
		SysMessageDAO sd = new SysMessageDAO();
		List<HashMap> list = new ArrayList();
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
				conditionMap, "ID");
		for (HashMap data : tmplist) {
			if (!superman) {
				boolean flag = this.checkProjectSecurity(data, uc);
				if (!flag)
					continue;
			}
			data.put("ISYD",
					!sd.getSyMsgSize(data.get("INSTANCEID").toString()));
			String purview = this.isPurview(data,
					ZQBConstants.ISPURVIEW_TYPE_PROJECT_KEY);
			if (purview.equals(ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER)) {
				data.put("ISPURVIEW", true);
				list.add(data);
			} else if (purview.equals(ZQBConstants.ISPURVIEW_ROLE_TYPE_VISITR)
					|| superman) {
				data.put("ISPURVIEW", false);
				list.add(data);
			} else if (purview.equals(ZQBConstants.ISPURVIEW_ROLE_TYPE_GUEST)) {
				continue;
			}
		}
		Collections.reverse(list);
		return list;
	}

	/**
	 * 检查项目权限
	 * 
	 * @param projectData
	 * @return
	 */
	private boolean checkProjectSecurity(HashMap projectData, UserContext uc) {
		boolean flag = false;
		OrgUser userModel = uc._userModel;
		String useraddress = UserContextUtil.getInstance().getFullUserAddress(
				uc.get_userModel().getUserid());
		Long ismanager = getIsManager(uc);
		Long orgRoleId = userModel.getOrgroleid();
		if (projectData != null && projectData.get("OWNER") != null) {
			if (SecurityUtil.isSuperManager()) {
				flag = true;
			}
			// 判断是否是部门负责人
			if(ismanager==1){
				flag = true;
			}
			// 判断是否是项目负责人
			if (projectData.get("OWNER").toString().equals(useraddress)) {
				flag = true;
			}
			// 判断是否是项目现场负责人
			if (!flag) {
				if (projectData.get("MANAGER") != null
						&& projectData.get("MANAGER").toString()
								.equals(useraddress)) {
					flag = true;
				}
			}
			// 判断是否是项目组成员
			if (!flag) {
				Long instanceid = Long.parseLong(projectData.get("INSTANCEID")
						.toString());
				if (instanceid != null) {
					List<HashMap> sublist = DemAPI.getInstance()
							.getFromSubData(instanceid, "SUBFORM_XMCYLB");
					if (sublist != null && sublist.size() > 0) {
						for (HashMap user : sublist) {
							if (user.get("USERID") != null
									&& user.get("USERID")
											.toString()
											.equals(userModel.getUserid())) {
								flag = true;
								break;
							}

						}

					}
				}
			}
		}
		return flag;
	}

	/**
	 * 获取当前用户负责的，正在执行的项目列表
	 * 
	 * @param startDate
	 * @param xmjd2
	 * @param projectName
	 * @param cyrName 
	 * @param parameterMap 
	 * @param sssyb 
	 * 
	 * @return
	 */
	public List<HashMap> getCloseProjectList(boolean superman, int pageNow,
			int pageSize, String projectName, String xmjd2, String startDate,
			String customername,String dgzt, String czbm, String cyrName, HashMap<String,List<String>> parameterMap) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();
		Long ismanager=getIsManager(uc);
		List<HashMap> tmplist = zqbGpfxProjectManageDAO.getCloseProjectList(
				pageNow, pageSize, projectName, xmjd2, startDate, customername,dgzt,parameterMap,czbm,cyrName,ismanager);
		SysMessageDAO sd = new SysMessageDAO();
		for (HashMap data : tmplist) {
			data.put("ISYD",!sd.getSyMsgSize(data.get("INSTANCEID").toString()));
			list.add(data);
		}
		return list;
	}
	
	public List<HashMap> getFinishProjectList1(boolean superman, int pageNow,
			int pageSize, String projectName, String xmjd, String startDate,
			String customername,String dgzt) {
		final int pageSize1 = pageNow * pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		String owner = UserContextUtil.getInstance().getCurrentUserContext()._userModel
				.getUserid();//
				//+ "["
				//+ UserContextUtil.getInstance().getCurrentUserContext()._userModel
				//.getUsername() + "]";
		final String userid = UserContextUtil.getInstance().getCurrentUserContext()._userModel
				.getUserid().toString().trim();
		List params = new ArrayList();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD, E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt,g.pjr pjr FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like '%%'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='已完成' and A.XMJD='持续督导' ORDER BY A.PROJECTNO DESC,GROUPID"
				+ " ) C) WHERE RN > ? AND RN <= ? ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from ("
				+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj"
				+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		params.add(startRow1);
		params.add(pageSize1);
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
		
		// WHERE JDMC LIKE '%股改%'
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like '%%'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='已完成' and A.XMJD='持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C"
				+ " ) WHERE RN > ? AND RN <= ? ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		params.add(startRow1);
		params.add(pageSize1);
		final List param=params;
		final String sql1 = sb.toString();
		return zqbGpfxProjectManageDAO.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql1);
						for (int i = 0; i < param.size(); i++) {
							query.setParameter(i, param.get(i));
						}
						List<HashMap> l = new ArrayList<HashMap>();
						List<Object[]> list = query.list();
						HashMap map;
						HashMap m = new HashMap();
						BigDecimal b;
						int n = 0;
						for (Object[] object : list) {
							map = new HashMap();
							String customername = (String) object[2];
							String owner = (String) object[3];
							double htje = ((BigDecimal) object[4]).doubleValue();
							double wsje = 0.0;
							if (object[5] != null) {
								wsje = ((BigDecimal) object[5]).doubleValue();
							}
							double ssje = 0.0;
							if (object[8] != null) {
								ssje = ((BigDecimal) object[8]).doubleValue();
							}
							String rwjd = (String) object[9];
							String spzt = (String) object[11];
							String manager1 = (String) object[12];
							Integer pjinsid = 0;
							if (object[13] != null) {
								pjinsid = Integer.valueOf(object[13].toString());
							}
							Integer xgwtinsid = 0;
							if (object[14] != null) {
								xgwtinsid = Integer.valueOf(object[14].toString());
							}
							Integer instanceid = Integer.valueOf(object[15]
									.toString());
							String xmjd = object[6] == null ? "" : object[6]
									.toString();
							String projectNo = object[1] == null ? ""
									: object[1].toString();
							String groupid = object[7] == null ? "" : object[7]
									.toString();
							String pj = object[16] == null ? "" : object[16]
									.toString();
							Integer num = Integer.valueOf(object[24].toString());
							
							String lcbh = object[17] == null ? "" : object[17]
									.toString();
							String stepid = object[19] == null ? "" : object[19]
									.toString();
							String xmspzt = object[21] == null ? "" : object[21]
									.toString();
							Integer lcbs=object[18]==null?0:Integer.valueOf(object[18].toString());
							String pjr = object[22]==null?"":object[22].toString();
							if(!pjr.equals(userid)){
								Long zPjid = DBUtil.getLong("SELECT INSTANCEID FROM BD_ZQB_GPFXXMPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_GPFXXMPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PROJECTNO='"+projectNo+"' AND PJR='"+userid+"' AND GROUPID="+groupid+" ORDER BY ID", "INSTANCEID");
								if(zPjid!=null){
									pjinsid=Integer.parseInt(zPjid.toString());
								}else{
									pjinsid=0;
								}
							}
							Integer taskid=object[20]==null?0:Integer.valueOf(object[20].toString());
							if(lcbs!=null&&!lcbs.equals("")&&lcbs!=0){ 
								if(lcbh!=null){
									try{
										List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(lcbh, Long.parseLong(lcbs.toString()));
										if(pro!=null&pro.size()>0){
											Long prc=pro.get(0).getPrcDefId();
											map.put("PRCID", prc);
										}
									}catch(Exception e){
										logger.error(e,e);
									}
								}
							}
							map.put("LCBH", lcbh);
							map.put("LCBS", lcbs);
							map.put("STEPID", stepid);
							map.put("TASKID", taskid);
							map.put("XMSPZT", xmspzt);
							
							
							map.put("OWNER", owner);
							map.put("PROJECTNO", projectNo);
							map.put("INSTANCEID", instanceid);
							map.put("XMJD", xmjd);
							map.put("RWJD", rwjd);
							map.put("SPZT", spzt);
							map.put("PJ", pj);
							map.put("CUSTOMERNAME", customername);
							map.put("HTJE", htje);
							map.put("SSJE", ssje);
							map.put("WSJE", wsje);
							map.put("JDFZR", manager1);
							map.put("NUM", num);
							map.put("GROUPID", groupid);
							map.put("PJINSID", pjinsid);
							l.add(map);
						}
						return l;
					}
				});
	}

	/**
	 * 关闭项目
	 * 
	 * @param projectNo
	 * @param instanceId
	 * @return
	 */
	public boolean projectClose(String projectNo, Long instanceId) {
		HashMap hash = DemAPI.getInstance().getFromData(instanceId,
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String projectname = hash.get("PROJECTNAME").toString();
		Long dataid = (Long) hash.get("ID");
		hash.put("STATUS", "已完成");
		// ============================消息提醒===============================================

		String smsContent = "";
		String sysMsgContent = "";
		smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(
				ZQB_Notice_Constants.PROJECT_BASE_CLOSE_KEY, hash);
		sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(
				ZQB_Notice_Constants.PROJECT_BASE_CLOSE_KEY, hash);
		String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(
				ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		UserContext target = UserContextUtil.getInstance().getUserContext(
				userid);
		if (target != null) {
			if (!smsContent.equals("")) {
				String mobile = target.get_userModel().getMobile();
				if (mobile != null && !mobile.equals("")) {
					MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
				}
			}
			if (!sysMsgContent.equals("")) {
				MessageAPI.getInstance().sendSysMsg(userid, "项目任务时间变更",
						sysMsgContent);
			}
		}
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		boolean updateFormData = DemAPI.getInstance().updateFormData(ProjectUUID, instanceId,hash, dataid, false);
		if(updateFormData){
			LogUtil.getInstance().addLog(dataid, "股票发行项目管理维护", "关闭项目："+projectname);
		}
		return updateFormData;
	}

	/**
	 * 关闭项目
	 * 
	 * @param projectNo
	 * @param instanceId
	 * @return
	 */
	public boolean projectFinish(String projectNo, Long instanceId) {
		HashMap hash = DemAPI.getInstance().getFromData(instanceId,
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		Long dataid = (Long) hash.get("ID");
		hash.put("XMJD", "持续督导");
		hash.put("STATUS", "已完成");
		// 项目转入持续督导阶段后，直接将客户保存至持续督导分派表1.若为项目督导人员负责的项目，直接默认持续督导负责人为其本人；2.其他人员负责的项目插入的客户负责人为空
		String cxduid = DBUtil.getString(
				"select * from BD_MDM_KHQXGLB where KHBH='"
						+ hash.get("CUSTOMERNO") + "'", "ID");
		if (cxduid == null || cxduid.equals("")) {
			String demUUIDDD = "84ff70949eac4051806dc02cf4837bd9";// 持续督导
			HashMap hashmap = new HashMap();
			Long instanceid = DemAPI.getInstance()
					.newInstance(
							demUUIDDD,
							UserContextUtil.getInstance()
									.getCurrentUserContext()._userModel
									.getUserid());
			hashmap.put("instanceId", instanceid);
			hashmap.put("formid", 114);
			hashmap.put("metadataid", 36);
			hashmap.put("modelType", "DEM");
			hashmap.put("KHFZR",
					hash.get("OWNER").equals("PJDU[项目督导]") ? hash.get("OWNER")
							: "");
			hashmap.put("KHBH", hash.get("CUSTOMERNO"));
			hashmap.put("KHMC", hash.get("CUSTOMERNAME"));
			hashmap.put("TXDF", "否");
			hashmap.put("SFFP", hash.get("OWNER").equals("PJDU[项目督导]") ? "是"
					: "否");
			DemAPI.getInstance().saveFormData(demUUIDDD, instanceid, hashmap,
					false);
		}
		// ==========转入持续督导阶段后，将客户更新为已挂牌
		if (hash.get("CUSTOMERNO") != null) {
			String demUUID = "a243efd832bf406b9caeaec5df082e28";// 客户管理主数据
			HashMap map = new HashMap();
			map.put("CUSTOMERNO", hash.get("CUSTOMERNO").toString());
			map.put("CUSTOMERNAME", hash.get("CUSTOMERNAME").toString());
			List<HashMap> customerlist = DemAPI.getInstance().getList(demUUID,
					map, null);
			HashMap mapgp = new HashMap();
			for (int i = 0; i < customerlist.size(); i++) {
				HashMap customer = customerlist.get(i);
				if (customer.get("YGP") == null
						|| customer.get("YGP").toString().equals("未挂牌")) {
					mapgp = customer;
					mapgp.put("YGP", "已挂牌");
					boolean flag = DemAPI.getInstance().updateFormData(
							demUUID,
							Long.valueOf(
									String.valueOf(customer.get("INSTANCEID")))
									.longValue(), mapgp,
							Long.parseLong(customer.get("ID").toString()),
							false);
				}

			}

		}
		// 转入持续督导阶段后，直接往部门表中插入一条记录
		OrgDepartment model = new OrgDepartment();
		model.setDepartmentname(hash.get("CUSTOMERNAME").toString());
		model.setDepartmentno(hash.get("CUSTOMERNO").toString());
		model.setCompanyid("2");
		model.setParentdepartmentid(new Long(51));
		model.setDepartmentstate("0");
		/*String id = DBUtil.getString(
				"select * from ORGDEPARTMENT where DEPARTMENTNO='"
						+ model.getDepartmentno() + "'", "ID");*/
		String id=DBUtil.getString("select * from ORGDEPARTMENT where DEPARTMENTNAME='"+model.getDepartmentname()+"'", "ID");
		if (id != null && !id.equals("")) {
			model.setId(Long.parseLong(id));
			OrganizationAPI.getInstance().updateDepartment(model);
		} else {
			OrganizationAPI.getInstance().addDepartment(model);
		}
		// ============================消息提醒===============================================

		String smsContent = "";
		String sysMsgContent = "";
		smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(
				ZQB_Notice_Constants.PROJECT_STATUS_UPDATE_KEY, hash);
		sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(
				ZQB_Notice_Constants.PROJECT_STATUS_UPDATE_KEY, hash);
		String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(
				ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		UserContext target = UserContextUtil.getInstance().getUserContext(
				userid);
		if (target != null) {
			if (!smsContent.equals("")) {
				String mobile = target.get_userModel().getMobile();
				if (mobile != null && !mobile.equals("")) {
					MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
				}
			}
			if (!sysMsgContent.equals("")) {
				MessageAPI.getInstance().sendSysMsg(userid, "项目转入“持续督导阶段提醒“",
						sysMsgContent);
			}
		}
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		return DemAPI.getInstance().updateFormData(ProjectUUID, instanceId,
				hash, dataid, false);
	}
	
	public boolean setProjectFinishDg(String temp) {
		String[] split = temp.split(",");
		boolean flag=false;
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		for (String string : split) {
			long instanceId = Long.parseLong(string);
			HashMap hash = DemAPI.getInstance().getFromData(instanceId,
					EngineConstants.SYS_INSTANCE_TYPE_DEM);
			Long dataid = (Long) hash.get("ID");
			hash.put("QRDG", "已完成");
			// 项目转入持续督导阶段后，直接将客户保存至持续督导分派表1.若为项目督导人员负责的项目，直接默认持续督导负责人为其本人；2.其他人员负责的项目插入的客户负责人为空
			flag=DemAPI.getInstance().updateFormData(ProjectUUID, instanceId,
					hash, dataid, false);
		}
		return flag;
	}

	/**
	 * 获取当前用户负责的，正在执行的项目列表
	 * 
	 * @return
	 */
	public HashMap getFinishProjectModel(String projectNo) {
		String demUUID = "33833384d109463285a6a348813539f1";
		HashMap hash = new HashMap();
		HashMap conditionMap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		// conditionMap.put("OWNER", owner);
		conditionMap.put("PROJECTNO", projectNo);
		List<HashMap> list = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		if (list != null && list.size() > 0) {
			hash = list.get(0);
		}
		return hash;
	}

	/**
	 * 判断当前用户的权限类型
	 * 
	 * @param hash
	 * @param type
	 * @return
	 */
	public String isPurview(HashMap hash, String type) {
		String roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_GUEST;
		if (type != null
				&& type.equals(ZQBConstants.ISPURVIEW_TYPE_PROJECT_KEY)) {
			String currentUser = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			String owner = (String) hash.get("OWNER");
			String manager = (String) hash.get("MANAGER");

			if (projectSetMap == null) {
				projectSetMap = DemAPI.getInstance().getFromData(
						new Long(35722), EngineConstants.SYS_INSTANCE_TYPE_DEM);
			}
			if (owner != null && currentUser.equals(owner)) {
				roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER;
			} else if (manager != null && currentUser.equals(manager)) {
				roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER;
			} else if (SecurityUtil.isSuperManager()) {
				roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER;
			} else {
				if (projectSetMap != null
						&& projectSetMap.get("SUPERMAN") != null) {
					String tmpUser = projectSetMap.get("SUPERMAN").toString();
					String supermanId = UserContextUtil.getInstance()
							.getUserId(tmpUser);
					if (supermanId == null)
						supermanId = "";
					if (supermanId.equals(currentUser)) {
						roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_VISITR;
					} else {
						// 判断是否是项目成员
						Long instanceid = Long.parseLong(hash.get("INSTANCEID")
								.toString());
						if (instanceid != null) {
							List<HashMap> sublist = DemAPI.getInstance()
									.getFromSubData(instanceid,
											"SUBFORM_XMCYLB");
							if (sublist != null && sublist.size() > 0) {
								for (HashMap user : sublist) {
									if (user.get("USERID") != null
											&& user.get("USERID")
													.toString()
													.equals(UserContextUtil
															.getInstance()
															.getCurrentUserContext()
															.get_userModel()
															.getUserid())) {
										roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER;
										return roleType;
									}

								}

							}
						}
						roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_GUEST;
					}
				} else {
					roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_GUEST;
				}
			}
		}
		return roleType;

	}

	/**
	 * 判断当前用户的权限类型
	 * 
	 * @param hash
	 * @param type
	 * @return
	 */
	public String isPurviewUser(HashMap hash, String type, String currentUser) {
		String roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_GUEST;
		if (type != null
				&& type.equals(ZQBConstants.ISPURVIEW_TYPE_PROJECT_KEY)) {
			String owner = (String) hash.get("OWNER");
			String manager = (String) hash.get("MANAGER");

			if (projectSetMap == null) {
				projectSetMap = DemAPI.getInstance().getFromData(
						new Long(35722), EngineConstants.SYS_INSTANCE_TYPE_DEM);
			}
			if (owner != null && currentUser.equals(owner)) {
				roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER;
			} else if (manager != null && currentUser.equals(manager)) {
				roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER;
			} else if (SecurityUtil.isSuperManager()) {
				roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER;
			} else {
				if (projectSetMap != null
						&& projectSetMap.get("SUPERMAN") != null) {
					String tmpUser = projectSetMap.get("SUPERMAN").toString();
					String supermanId = UserContextUtil.getInstance()
							.getUserId(tmpUser);
					if (supermanId == null)
						supermanId = "";
					if (supermanId.equals(currentUser)) {
						roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_VISITR;
					} else {
						// 判断是否是项目成员
						Long instanceid = Long.parseLong(hash.get("INSTANCEID")
								.toString());
						if (instanceid != null) {
							List<HashMap> sublist = DemAPI.getInstance()
									.getFromSubData(instanceid,
											"SUBFORM_XMCYLB");
							if (sublist != null && sublist.size() > 0) {
								for (HashMap user : sublist) {
									if (user.get("USERID") != null
											&& user.get("USERID")
													.toString()
													.equals(UserContextUtil
															.getInstance()
															.getCurrentUserContext()
															.get_userModel()
															.getUserid())) {
										roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER;
										return roleType;
									}

								}

							}
						}
						roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_GUEST;
					}
				} else {
					roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_GUEST;
				}
			}
		}
		return roleType;

	}

	/**
	 * 按类型进行划分，获取当前用户权限范围内的项目饼图
	 * 
	 * @return
	 */
	public String getProjectTypePieChart(boolean isSuperman, String type) {
		StringBuffer jsonHtml = new StringBuffer();
		List<ArrayList> list = null;
		if (isSuperman) {
			list = zqbGpfxProjectManageDAO.getProjectTypeGroup(null);
		} else {
			list = zqbGpfxProjectManageDAO.getProjectTypeGroup(UserContextUtil
					.getInstance().getCurrentUserFullName());
		}
		if (list != null) {
			// 判断当前用户是否为
			if (type.equals("chart")) {
				// ===========================================
				JSONArray json = JSONArray.fromObject(list);
				jsonHtml.append(json);
			} else {
				jsonHtml.append("<table>").append("\n");
				jsonHtml.append("<tr><th>项目类型</th><th>数量</th></tr>").append(
						"\n");
				for (ArrayList item : list) {
					if (item == null) {
						continue;
					}
					String title = (String) item.get(0);
					int value = Integer.parseInt(item.get(1).toString());
					jsonHtml.append("<tr>").append("\n");
					jsonHtml.append("<td>").append(title).append("</td>")
							.append("\n");
					jsonHtml.append("<td>").append(value).append("</td>")
							.append("\n");
					jsonHtml.append("</tr>").append("\n");
				}

				jsonHtml.append("</table>").append("\n");
			}
		}
		return jsonHtml.toString();
	}

	/**
	 * 按类型进行划分，获取当前用户权限范围内的项目饼图
	 * 
	 * @return
	 */
	public String getProjectTypePieChart2(boolean isSuperman, String type) {
		StringBuffer jsonHtml = new StringBuffer();
		// 判断当前用户是否为

		List<ArrayList> list = null;
		if (isSuperman) {
			list = zqbGpfxProjectManageDAO.getProjectTypeStatus(null);
		} else {
			list = zqbGpfxProjectManageDAO.getProjectTypeStatus(UserContextUtil
					.getInstance().getCurrentUserFullName());
		}
		if (list != null) {
			// 判断当前用户是否为
			if (type.equals("chart")) {
				// ===========================================
				JSONArray json = JSONArray.fromObject(list);
				jsonHtml.append(json);
			} else {
				jsonHtml.append("<table>").append("\n");
				jsonHtml.append("<tr><th>项目阶段</th><th>项目数量</th></tr>").append(
						"\n");
				for (ArrayList item : list) {
					if (item == null) {
						continue;
					}
					String title = (String) item.get(0);
					int value = Integer.parseInt(item.get(1).toString());
					jsonHtml.append("<tr>").append("\n");
					jsonHtml.append("<td>").append(title).append("</td>")
							.append("\n");
					jsonHtml.append("<td>").append(value).append("</td>")
							.append("\n");
					jsonHtml.append("</tr>").append("\n");
				}

				jsonHtml.append("</table>").append("\n");
			}
		}
		return jsonHtml.toString();
	}

	/**
	 * 获得项目成员负责的项目个数清单
	 * 
	 * @param isSuperman
	 * @param type
	 * @return
	 */
	public String getProjectUserTableList(boolean isSuperman) {

		HashMap<String, ArrayList> data = new HashMap();
		HashMap conditionMap = new HashMap();
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
				conditionMap, null);
		for (HashMap map : tmplist) {
			String projectNo = map.get("PROJECTNO").toString();
			// 获取现场负责人
			if (map.get("OWNER") != null) {
				String owner = map.get("OWNER").toString();
				if (data.get(owner) == null) {
					ArrayList list = new ArrayList();
					list.add(map);
					data.put(owner, list);
				} else {
					ArrayList list1 = data.get(owner);
					if (list1 != null) {
						if (list1.contains(map)) {
							continue;
						} else {
							list1.add(map);
						}
					}
				}
			}
			// 获得项目负责人
			if (map.get("MANAGER") != null) {
				String owner = map.get("MANAGER").toString();
				if (data.get(owner) == null) {
					ArrayList list = new ArrayList();
					list.add(map);
					data.put(owner, list);
				} else {
					ArrayList list1 = data.get(owner);
					if (list1 != null) {
						if (list1.contains(map)) {
							continue;
						} else {
							list1.add(map);
						}
					}
				}
			}
			// 获取项目成员
			Long instanceid = Long.parseLong(map.get("INSTANCEID").toString());
			List<HashMap> sublist = DemAPI.getInstance().getFromSubData(
					instanceid, "SUBFORM_XMCYLB");
			for (HashMap subItem : sublist) {
				if (subItem.get("USERID") != null) {
					String userid = subItem.get("USERID").toString();
					String address = UserContextUtil.getInstance()
							.getFullUserAddress(userid);
					if (data.get(address) == null) {
						ArrayList list = new ArrayList();
						list.add(map);
						data.put(address, list);
					} else {
						ArrayList list1 = data.get(address);
						if (list1 != null) {
							if (list1.contains(map)) {
								continue;
							} else {
								list1.add(map);
							}
						}
					}
				}
			}
		}
		StringBuffer html = new StringBuffer();

		if (data != null) {
			html.append(
					"<table width=\"95%\" style=\"margin-left:auto;margin-right:auto\">")
					.append("\n");
			Iterator iterator = data.entrySet().iterator();
			while (iterator.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) iterator
						.next();
				String address = (String) entry.getKey();
				if (address == null)
					continue;
				OrgUser orgUser = UserContextUtil.getInstance().getOrgUserInfo(
						address);
				if (orgUser == null) {
					continue;
				}
				ArrayList<HashMap> pjnolist = (ArrayList) entry.getValue();
				html.append("<tr class=\"listtitle\">").append("\n");
				html.append("<td colspan=\"6\">").append(orgUser.getUsername())
						.append("(").append(pjnolist.size()).append(")")
						.append("</td>");
				html.append("</tr>").append("\n");

				for (HashMap item : pjnolist) {

					html.append("<tr  class=\"listdata\">").append("\n");
					html.append("<td style=\"padding-left:10px;\">")
							.append(item.get("PROJECTNAME")).append("</td>");
					html.append("<td>").append(item.get("PROJECTNO"))
							.append("</td>");
					html.append("<td>").append(item.get("STARTDATE"))
							.append("</td>");
					html.append("<td>").append(item.get("ENDDATE"))
							.append("</td>");
					html.append("<td>").append(item.get("XMJD"))
							.append("</td>");
					html.append("<td>").append(item.get("STATUS"))
							.append("</td>");
					html.append("</tr>").append("\n");
				}
			}
			html.append("</table>").append("\n");
		}
		return html.toString();
	}

	/**
	 * 按类型进行划分，获取当前用户权限范围内的项目饼图
	 * 
	 * @return
	 */
	public String getProjectTypePieChart3(boolean isSuperman, String type) {
		StringBuffer jsonHtml = new StringBuffer();
		// 判断当前用户是否为
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		List<HashMap> templist = DemAPI.getInstance().getList(ProjectUUID,
				null, null);
		List<String> userlist = zqbGpfxProjectManageDAO.getProjectGroupList();

		HashMap infodata = new HashMap();
		for (String userid : userlist) {
			for (HashMap project : templist) {
				UserContext context = UserContextUtil.getInstance()
						.getUserContext(userid);
				boolean flag = this.checkProjectSecurity(project, context);
				if (!flag)
					continue;
				Object obj = infodata.get(userid);
				if (obj == null) {
					infodata.put(userid, new Long(1));
				} else {
					Long num = Long.parseLong(infodata.get(userid).toString());
					num++;
					infodata.put(userid, num);
				}
			}

		}

		// 判断当前用户是否为
		if (type.equals("chart")) {
			// ===========================================
			List slist = new ArrayList();
			Iterator iter = infodata.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				String name = UserContextUtil.getInstance().getFullUserAddress(
						key.toString());
				Object val = entry.getValue();
				List item = new ArrayList();
				item.add(name);
				item.add(val);
				slist.add(item);
			}
			JSONArray json = JSONArray.fromObject(slist);
			jsonHtml.append(json);
		} else {
			jsonHtml.append("<table>").append("\n");
			jsonHtml.append("<tr><th>项目负责人</th><th>负责项目数量</th></tr>").append(
					"\n");
			Iterator iter = infodata.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				jsonHtml.append("<tr>").append("\n");
				jsonHtml.append("<td>").append(key).append("</td>")
						.append("\n");
				jsonHtml.append("<td>").append(val).append("</td>")
						.append("\n");
				jsonHtml.append("</tr>").append("\n");

			}
			jsonHtml.append("</table>").append("\n");
		}
		return jsonHtml.toString();
	}

	/**
	 * 按类型进行划分，获取当前用户权限范围内的项目饼图
	 * 
	 * @return
	 */
	public String getProjectTypePieChart4(boolean isSuperman, String type) {
		StringBuffer jsonHtml = new StringBuffer();
		// 判断当前用户是否为
		// 如果当前用户非超级用户，则判断项目过滤权限
		List<String> pjNoList = null;
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		if (!isSuperman) {
			pjNoList = new ArrayList();
			List<HashMap> projectList = DemAPI.getInstance().getList(
					ProjectUUID, null, null);
			for (HashMap data : projectList) {
				boolean flag = this.checkProjectSecurity(data, UserContextUtil
						.getInstance().getCurrentUserContext());
				if (flag) {
					pjNoList.add(data.get("PROJECTNO").toString());
				}
			}
		}
		List<ArrayList> list = null;
		list = zqbGpfxProjectManageDAO.getProjectFengXian(pjNoList, UserContextUtil
				.getInstance().getCurrentUserFullName());
		if (list != null) {

			// 判断当前用户是否为
			if (type.equals("chart")) {
				// ===========================================
				JSONArray json = JSONArray.fromObject(list);
				jsonHtml.append(json);
			} else {
				jsonHtml.append("<table>").append("\n");
				jsonHtml.append("<tr><th>风险阶段</th><th>数量</th></tr>").append(
						"\n");
				for (ArrayList item : list) {
					if (item == null) {
						continue;
					}
					String title = (String) item.get(0);
					int value = Integer.parseInt(item.get(1).toString());
					jsonHtml.append("<tr>").append("\n");
					jsonHtml.append("<td>").append(title).append("</td>")
							.append("\n");
					jsonHtml.append("<td>").append(value).append("</td>")
							.append("\n");
					jsonHtml.append("</tr>").append("\n");
				}

				jsonHtml.append("</table>").append("\n");
			}
		}
		return jsonHtml.toString();
	}

	/**
	 * 按类型进行划分，获取当前用户权限范围内的项目饼图
	 * 
	 * @return
	 */
	public String[] getProjectTypeBarChart(boolean isSuperman) {
		StringBuffer label = new StringBuffer();
		StringBuffer value = new StringBuffer();
		HashMap hash = null;
		List<String> pjNoList = null;
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		if (!isSuperman) {
			pjNoList = new ArrayList();
			List<HashMap> projectList = DemAPI.getInstance().getList(
					ProjectUUID, null, "ID");
			for (HashMap data : projectList) {
				boolean flag = this.checkProjectSecurity(data, UserContextUtil
						.getInstance().getCurrentUserContext());
				if (flag) {
					pjNoList.add(data.get("PROJECTNO").toString());
				}
			}
		}
		hash = zqbGpfxProjectManageDAO.getProjectStageGroup(pjNoList,
				UserContextUtil.getInstance().getCurrentUserFullName());
		List lablelist = (List) hash.get("label");
		List valuelist = (List) hash.get("value");
		// ===========================================
		JSONArray list1 = JSONArray.fromObject(lablelist);
		JSONArray list2 = JSONArray.fromObject(valuelist);
		label.append(list1);
		value.append(list2);
		String[] data = { label.toString(), value.toString() };
		return data;
	}

	/**
	 * 按类型进行划分，获取当前用户权限范围内的项目饼图
	 * 
	 * @return
	 */
	public String getProjectTypeBarGrid(boolean isSuperman) {
		StringBuffer jsonHtml = new StringBuffer();
		List<HashMap> list = null;
		if (isSuperman) {
			list = zqbGpfxProjectManageDAO.getProjectStageList((null));
		} else {
			list = zqbGpfxProjectManageDAO.getProjectStageList(UserContextUtil
					.getInstance().getCurrentUserFullName());
		}
		if (list != null) {
			jsonHtml.append("<table>").append("\n");
			jsonHtml.append("<tr><th>项目阶段</th><th>项目数量</th></tr>").append("\n");
			for (HashMap item : list) {
				if (item == null) {
					continue;
				}
				String title = (String) item.get("xmjd");
				int value = Integer.parseInt(item.get("count").toString());
				jsonHtml.append("<tr>").append("\n");
				jsonHtml.append("<td>").append(title).append("</td>")
						.append("\n");
				jsonHtml.append("<td>").append(value).append("</td>")
						.append("\n");
				jsonHtml.append("</tr>").append("\n");
			}
			jsonHtml.append("</table>").append("\n");
		}
		return jsonHtml.toString();
	}

	public boolean getIsSuperMan() {
		String roleTyle = "";
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			Long orgRoleId = uc.get_userModel().getOrgroleid();
			if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {
				flag = true;
			}
		}
		return flag;
	}

	public String getQuestionTalkList(String projectNo, Long taskid,
			Long questionId) {
		StringBuffer html = new StringBuffer();
		String ucName = "";
		List<HashMap> list = this.getQuestionList(projectNo, taskid);
		List<UploadDocModel> qDocList = zqbGpfxProjectManageDAO.getQuestionDocList(
				projectNo, taskid);
		html.append("");
		if (list != null) {
			for (HashMap map : list) {
				String id = "";
				String username = "";
				String question = "";
				String date = "";
				if (map.get("ID") != null)
					id = map.get("ID").toString();
				if (map.get("USERNAME") != null)
					username = map.get("USERNAME").toString();
				if (map.get("QUESTION") != null)
					question = map.get("QUESTION").toString();
				if (map.get("USERNAME") != null)
					username = map.get("USERNAME").toString();
				if (map.get("CREATEDATE") != null)
					date = map.get("CREATEDATE").toString();
				if (id.equals(questionId.toString())) {
					html.append("<div class=\"other\">\n");
					html.append(
							"<div class=\"other_bottom\"><img src=\"iwork_img/user_comment.png\"/>")
							.append(username).append(":\n");
					html.append(question);
					html.append("  	<span>").append(date).append("</span>\n");
					Long orgRoleId = UserContextUtil.getInstance()
							.getCurrentUserContext().get_userModel().getOrgroleid();
					if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {
						html.append("<input id=\"sel")
								.append(id)
								.append("\" class=\"cbox\" type=\"checkbox\" onclick=allXuan("
										+ id + ") role=\"checkbox\">")
								.append("全选")
								.append("\t")
								.append("<a href=\"javascript:removeAllQuestion('")
								.append(projectNo).append("',").append(taskid)
								.append(",").append(Long.parseLong(id))
								.append(")\">删除</a>\n");
					}
					html.append("    </div>\n");
					html.append("	    <div class=\"comment\" id=\"commit")
							.append(id).append("\"> \n");
					List<HashMap> reTalklist = this.getCommitQuestionList(
							projectNo, taskid, Long.parseLong(id));
					int count = 0;
					html.append("<div style=\"padding:10px;\"><table width=\"100%\" class=\"docItem\">");
					for (UploadDocModel uploadDocModel : qDocList) {
						if (uploadDocModel.getTaskName().equals(
								map.get("QUESTION").toString())) {
							List<FileUpload> list2 = uploadDocModel.getList();
							for (FileUpload fileUpload : list2) {
								html.append("<tr><td>"
										+ fileUpload.getPrepare2()
										+ "&nbsp;<a href=\""
										+ fileUpload.getFileUrl()
										+ "\" target=\"_blank\" font-size:12px;>"
										+ fileUpload.getFileSrcName()
										+ "</a></td><td style=\"text-align:right\">"
										+ fileUpload.getUploadTime()
										+ "</td></tr>");
							}
						}
					}
					html.append("</table></div>");
					if (reTalklist != null) {
						count = reTalklist.size();
					}
					html.append(
							"	        <div class=\"comment_title\">回复（<span id=\"reCount")
							.append(id).append("\">").append(count)
							.append("</span>）</div>\n");
					if (count == 0) {
						html.append("        <div class=\"comment_bottom\"></div>\n");
					} else {
						for (HashMap retalk : reTalklist) {
							String userid = "";
							String user = "";
							String content = "";
							String createdate = "";
							String instanceid = "";
							if (retalk.get("CONTENT") != null) {
								content = retalk.get("CONTENT").toString();
							}
							if (retalk.get("USERID") != null) {
								userid = retalk.get("USERID").toString();
							}
							if (retalk.get("USERNAME") != null) {
								user = retalk.get("USERNAME").toString();
							}
							if (retalk.get("CREATEDATE") != null) {
								createdate = retalk.get("CREATEDATE")
										.toString();
							}
							if (retalk.get("INSTANCEID") != null) {
								instanceid = retalk.get("INSTANCEID")
										.toString();
							}
							List<UploadDocModel> cDocList = zqbGpfxProjectManageDAO
									.getCommitDocList(instanceid);
							if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {
								html.append("<div class=\"comment_main\"><input id=\""
										+ Long.parseLong(instanceid)
										+ "\" name=\"sel"
										+ id
										+ "\" onclick=selFirst("
										+ id
										+ ") class=\"cbox\" type=\"checkbox\" role=\"checkbox\"><img  src='iwork_img/comments.png'>&nbsp;<pre>"
										+ content).append("</pre><table>");
								for (UploadDocModel uploadDocModel : cDocList) {
									List<FileUpload> list2 = uploadDocModel
											.getList();
									for (FileUpload fileUpload : list2) {
										html.append("<tr><td>"
												+ fileUpload.getPrepare2()
												+ "&nbsp;<a href=\""
												+ fileUpload.getFileUrl()
												+ "\" target=\"_blank\" font-size:12px;>"
												+ fileUpload.getFileSrcName()
												+ "</a></td></tr>");
									}
								}
								html.append("</table></div>\n");
							} else {
								html.append(
										"        <div class=\"comment_main\"><img  src='iwork_img/comments.png'>&nbsp;")
										.append("<pre>"+content+"</pre>").append("<table>");
								for (UploadDocModel uploadDocModel : cDocList) {
									List<FileUpload> list2 = uploadDocModel
											.getList();
									for (FileUpload fileUpload : list2) {
										html.append("<tr><td>"
												+ fileUpload.getPrepare2()
												+ "&nbsp;<a href=\""
												+ fileUpload.getFileUrl()
												+ "\" target=\"_blank\" font-size:12px;>"
												+ fileUpload.getFileSrcName()
												+ "</a></td></tr>");
									}
								}
								html.append("</table></div>\n");
							}
							html.append("		<div class=\"comment_bottom\">")
									.append(user).append(" - ")
									.append(createdate);
							if (userid.equals(UserContextUtil.getInstance()
									.getCurrentUserId())) {
								html.append(
										"<span><a href=\"javascript:removeQuestion('question',")
										.append(instanceid)
										.append(")\"><img src='iwork_img/delete2.gif'></a></span>\n");
							}

							html.append("</div>\n");
						}
					}
					html.append("	        <div class=\"reTalk\">\n");
					html.append("	        	<div class=\"reTalkBtn\">\n");
					html.append(
							"	        		<a href=\"javascript:showReTalkInput(")
							.append(id).append(")\">回复</a>\n");
					html.append("	        	</div>\n");
					html.append(
							"		        <div class=\"reTalkInput\" id=\"reTalkInputDiv")
							.append(id).append("\">\n");
					html.append("<form id=\"iformMain\" name=\"iformMain\">");
					html.append("<table><tr><td colspan=\"2\">");
					html.append("<div id=\"DIVATTACH"
							+ id
							+ "\"><div><input id=\"ATTACH"
							+ id
							+ "\" class=\"{maxlength:512}\" type=\"hidden\" value=\"\" name=\"ATTACH"
							+ id
							+ "\" size=\"100\" _value=\"\"></div><div><button onclick=\"showUploadifyPageATTACH('ATTACH"
							+ id
							+ "','ATTACH"
							+ id
							+ "','DIVATTACH"
							+ id
							+ "','true');return false;\">附件上传</button></div></div>");
					html.append("</td></tr><tr><td>");
					html.append("		        	<textarea name=\"reTalkArea")
							.append(id)
							.append("\"  id=\"reTalkArea")
							.append(id)
							.append("\" cols=\"\" class=\"{string:true,maxlength:1024}\" rows=\"\" style=\"height:60px; width:450px; border:1px solid #056ea4; padding:10px;\"></textarea>\n");
					html.append("</td><td>");
					html.append(
							"		        	<input type=\"button\" style=\"width:50px;height:60px;\" onClick=commitTalk(")
							.append(id).append(") value=\"提交\">\n");
					html.append("</td></tr></table>");
					html.append("	        </div>\n");
					html.append("        </div>\n");
					html.append("    </div>\n");
					html.append("	  </div>\n");
				}
			}
		} else {
			html.append("<div>未发现反馈的问题记录</div>");
		}

		return html.toString();
	}

	// 项目问题列表
	public String showQuestion(String projectNo, Long taskid, Long questionId) {
		StringBuffer html = new StringBuffer();
		String ucName = "";
		String taskname = "";
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		List<HashMap> list = this.getQuestionList(projectNo, taskid);
		if (list != null) {
			html.append("<table width=\"100%\" text-align:center;>").append(
					"\n");
			html.append(
					"<tr class=\"header\" height=\"30px\" style=\"border-bottom: #efefef 1px solid; border-left: #efefef 1px solid; border-top: #efefef 1px solid; border-right: #efefef 1px solid;\"><td>问题名称</td><td>问题回复</td></tr>")
					.append("\n");
			for (HashMap map : list) {
				String id = "";
				String username = "";
				String question = "";
				String date = "";
				if (map.get("ID") != null)
					id = map.get("ID").toString();
				if (map.get("USERNAME") != null)
					username = map.get("USERNAME").toString();
				if (map.get("QUESTION") != null)
					question = map.get("QUESTION").toString();
				if (map.get("USERNAME") != null)
					username = map.get("USERNAME").toString();
				if (map.get("CREATEDATE") != null)
					date = map.get("CREATEDATE").toString();
				HashMap hashMap = new HashMap();
				List<HashMap> list2 = DemAPI.getInstance().getList(
						ProjectItemUUID, hashMap, null);
				for (HashMap hashMap2 : list2) {
					if (hashMap2.get("ID").toString().equals(taskid.toString())) {
						taskname = hashMap2.get("TASK_NAME").toString();
					}
				}
				html.append("<tr height=\"30px\" style=\"border-bottom: #efefef 1px solid; border-left: #efefef 1px solid; border-top: #efefef 1px solid; border-right: #efefef 1px solid;\"><td>"
						+ map.get("QUESTION")
						+ "</td><td><a href=\"javascript:showQuestion('"
						+ taskname
						+ "','"
						+ projectNo
						+ "','"
						+ taskid
						+ "','"
						+ id + "')\" font-size:12px;>查看并回复</a></td></tr>");
			}
		} else {
			html.append("<div>未发现反馈的问题记录</div>");
		}
		html.append("</table>").append("\n");
		return html.toString();
	}

	//

	public boolean getQuestionTalkAllList(String projectNo, Long taskid,
			Long questionId, String instanceId) {
		String[] instanceIdArr = null;
		if (instanceId != null) {
			instanceIdArr = instanceId.split(",");
		}
		List<HashMap> list = this.getQuestionList(projectNo, taskid);
		boolean flag = false;
		for (HashMap map : list) {
			String id = "";
			if (map.get("ID") != null) {
				id = map.get("ID").toString();
			}
			if (id.equals(questionId.toString())) {
				List<HashMap> reTalklist = this.getCommitQuestionList(
						projectNo, taskid, Long.parseLong(id));
				for (HashMap hashMap : reTalklist) {
					String instanceid = "";
					if (hashMap.get("INSTANCEID") != null) {
						instanceid = hashMap.get("INSTANCEID").toString();
					}
					if (instanceIdArr.length != 0) {
						for (int i = 0; i < instanceIdArr.length; i++) {
							if (instanceid.equals(instanceIdArr[i])) {
								DemAPI.getInstance().removeFormData(
										Long.parseLong(instanceIdArr[i]));
							}
						}
					} else {
						DemAPI.getInstance().removeFormData(
								Long.parseLong(instanceid));
					}
				}
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获得回复类别
	 * 
	 * @param projectNo
	 * @param taskid
	 * @param talkId
	 * @return
	 */
	private List<HashMap> getCommitQuestionList(String projectNo, Long taskid,
			Long talkId) {
		HashMap hashdata = new HashMap();
		hashdata.put("PROJECTNO", projectNo);
		hashdata.put("TASKNO", taskid);
		if (talkId != null) {
			hashdata.put("QUESTIONNO", talkId);
		}
		//股票发行问题回复表单
		String ProjectReTalkUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行问题回复表单'", "UUID");
		return DemAPI.getInstance().getList(ProjectReTalkUUID, hashdata, "ID");
	}

	/**
	 * 
	 * @param projectNo
	 * @param taskid
	 * @param talkId
	 * @param question
	 * @param attach
	 * @return
	 */
	public boolean commitQuestion(String projectNo, Long taskid, Long talkId,
			String question, String attach) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String ProjectReTalkUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行问题回复表单'", "UUID");
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		Long instanceid = DemAPI.getInstance().newInstance(ProjectReTalkUUID,
				uc._userModel.getUserid());
		HashMap hashdata = new HashMap();
		hashdata.put("PROJECTNO", projectNo);
		hashdata.put("TASKNO", taskid);
		hashdata.put("CONTENT", question);
		hashdata.put("QUESTIONNO", talkId);
		hashdata.put("USERID", uc._userModel.getUserid());
		hashdata.put("USERNAME", uc._userModel.getUsername());
		hashdata.put("CREATEDATE", UtilDate.getNowdate());
		hashdata.put("ATTACH", attach);
		//

		// 判断当前用户身份，如果为市场部总经理，则发送提醒给项目负责人，如果当前用户为项目负责人则发送给市场部总经理
		String title = "[" + uc._userModel.getUsername() + "]在"
				+ SystemConfig._iworkServerConf.getShortTitle() + "中给您进行了问题回复";
		List<String> userList = new ArrayList();
		// 获取问题提出人列表
		HashMap conditionMap = new HashMap();
		conditionMap.put("XMBH", projectNo);
		conditionMap.put("TASKNO", taskid);
		String ProjectQuestionUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行问题反馈表单'", "UUID");
		List<HashMap> questionlist = DemAPI.getInstance().getList(
				ProjectQuestionUUID, conditionMap, "ID");
		HashMap questionMap = null;
		for (HashMap hash : questionlist) {
			if (hash.get("ID") != null) {
				Long tmpId = Long.parseLong(hash.get("ID").toString());
				if (tmpId.equals(talkId)) {
					questionMap = hash;
					break;
				}

			}
		}
		String userid = "";
		if (questionMap != null && questionMap.get("USERID") != null) {
			String usrid = questionMap.get("USERID").toString();
			if (!userList.contains(usrid)) {
				userList.add(usrid);
			}
		}

		// 发送提醒消息
		for (String uid : userList) {
			if (uid.equals(uc._userModel.getUserid())) {
				continue;
			}
			String content = question;
			HashMap data = new HashMap();
			data.put("CONTENT", content);
			data.put("XMMC", questionMap.get("XMMC"));
			HashMap conditionMap2 = new HashMap();
			conditionMap2.put("PROJECTNO", questionMap.get("XMBH"));
			conditionMap2.put("GROUPID", taskid);
			List<HashMap> list = DemAPI.getInstance().getList(ProjectItemUUID,
					conditionMap2, "ID");
			data.put("TASKNAME","");
			this.sendReTaskNotice(uid, data);
		}

		return DemAPI.getInstance().saveFormData(ProjectReTalkUUID, instanceid,
				hashdata, false);
	}

	/*
	 * private boolean sendReTaskNotice(String sender, HashMap data) {
	 * data.put("USERNAME", UserContextUtil.getInstance()
	 * .getCurrentUserContext()._userModel.getUsername());
	 * 
	 * // 判断发送短信 String smsContent = ""; String sysMsgContent = ""; smsContent =
	 * ZQBNoticeUtil.getInstance().getNoticeSmsContent(
	 * ZQB_Notice_Constants.PROJECT_QUESTION_ADD_KEY, data); sysMsgContent =
	 * ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(
	 * ZQB_Notice_Constants.PROJECT_QUESTION_ADD_KEY, data); String userid =
	 * ZQBNoticeUtil.getInstance().getNoticeUserId(
	 * ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG); UserContext uc =
	 * UserContextUtil.getInstance().getCurrentUserContext(); UserContext target
	 * = UserContextUtil.getInstance().getUserContext( userid); if (target !=
	 * null) { if (!smsContent.equals("")) { String mobile =
	 * target.get_userModel().getMobile(); if (mobile != null &&
	 * !mobile.equals("")) { MessageAPI.getInstance().sendSMS(uc, mobile,
	 * smsContent); } } if (!sysMsgContent.equals("")) {
	 * MessageAPI.getInstance().sendSysMsg(userid, "问题回复", sysMsgContent); } }
	 * return false; }
	 */

	private boolean sendReTaskNotice(String sender, HashMap data) {
		data.put("USERNAME", UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUsername());

		// 判断发送短信
		String smsContent = "";
		String sysMsgContent = "";
		// 【项目阶段问题反馈】访客 :dd
		smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(
				ZQB_Notice_Constants.PROJECT_QUESTION_RETALK_KEY, data);
		// 【项目阶段问题反馈】访客 :111dd
		sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(
				ZQB_Notice_Constants.PROJECT_QUESTION_RETALK_KEY, data);
		String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(
				ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		UserContext target = UserContextUtil.getInstance().getUserContext(
				sender);
		if (target != null) {
			if (!smsContent.equals("")) {
				String mobile = target.get_userModel().getMobile();
				if (mobile != null && !mobile.equals("")) {
					MessageAPI.getInstance()
							.sendSMS(target, mobile, smsContent);
				}
			}
			if (!sysMsgContent.equals("")) {
				MessageAPI.getInstance().sendSysMsg(sender, "问题回复",
						sysMsgContent);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param projectNo
	 * @param taskid
	 * @param talkId
	 * @param question
	 * @return
	 */
	public boolean removeQuestion(String type, Long instanceid) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		return DemAPI.getInstance().removeFormData(instanceid);
	}

	public String[] getManagerBarData() {
		String[] str = new String[3];
		StringBuffer label = new StringBuffer();
		StringBuffer value = new StringBuffer();
		List<HashMap> list = zqbGpfxProjectManageDAO.getProjectManagerUserData();
		List labellist = new ArrayList();
		List fulllist = new ArrayList();
		List datalist1 = new ArrayList();
		List datalist2 = new ArrayList();
		int count = 0;
		for (HashMap map : list) {
			count++;
			List data1 = new ArrayList();
			List data2 = new ArrayList();
			String manager = map.get("manager").toString();
			OrgUser uc = UserContextUtil.getInstance().getOrgUserInfo(manager);
			BigDecimal htje=new BigDecimal(map.get("htje").toString());
			BigDecimal ssje=new BigDecimal(map.get("ssje").toString());
			data1.add(htje);
			data1.add(count);
			data2.add(ssje);
			data2.add(count);
			datalist1.add(data1);
			datalist2.add(data2);
			labellist.add(uc.getUsername());
		}

		fulllist.add(datalist2);
		fulllist.add(datalist1);
		JSONArray list1 = JSONArray.fromObject(fulllist);
		JSONArray list3 = JSONArray.fromObject(labellist);
		label.append(list3);
		value.append(list1);
		String[] data = { label.toString(), value.toString() };
		return data;
	}

	/**
	 * 
	 * @return
	 */
	public String[] getYSZKChartData() {
		String[] str = new String[3];
		StringBuffer label = new StringBuffer();
		StringBuffer value = new StringBuffer();
		List<HashMap> list = zqbGpfxProjectManageDAO.getSSZKData();
		List labellist = new ArrayList();
		List fulllist = new ArrayList();
		List datalist1 = new ArrayList();
		List datalist2 = new ArrayList();
		int count = 0;
		for (HashMap map : list) {
			count++;
			List data1 = new ArrayList();
			List data2 = new ArrayList();
			String manager = map.get("PROJECTNAME")==null?"":map.get("PROJECTNAME").toString();
			BigDecimal htje=new BigDecimal(map.get("HTJE").toString());
			BigDecimal ssje=new BigDecimal(map.get("YSKE").toString());
			data1.add(htje);
			data1.add(count);
			data2.add(ssje);
			data2.add(count);
			datalist1.add(data1);
			datalist2.add(data2);
			labellist.add(manager);
		}

		fulllist.add(datalist2);
		fulllist.add(datalist1);
		JSONArray list1 = JSONArray.fromObject(fulllist);
		JSONArray list3 = JSONArray.fromObject(labellist);
		label.append(list3);
		value.append(list1);
		String[] data = { label.toString(), value.toString() };
		return data;
	}

	/**
	 * 获得阶段列表
	 * 
	 * @param index
	 * @return
	 */
	public List getJieDuanList(int index, int htje, int wsje) {
		List<HashMap> list = zqbGpfxProjectManageDAO.getYSZKData();
		if (list != null) {
			String projectNo = "";
			String projectName = "";
			int je = 0;
			for (int i = 0; i < list.size(); i++) {
				if (index == i + 1) {
					HashMap map = list.get(i);
					projectNo = map.get("PROJECTNO").toString();
					projectName = map.get("PROJECTNAME")==null?"":map.get("PROJECTNAME").toString();
					htje = Integer.parseInt(map.get("HTJE").toString());
					break;
				}
			}
			List<HashMap> taskList = this.getTaskList(projectNo, null);
			for (HashMap hashMap : taskList) {
				List<HashMap> taskMList = this.getTaskMList("".equals(hashMap.get(
						"GROUPID").toString())==true?"0":hashMap.get(
								"GROUPID").toString());
				for (HashMap hashMap2 : taskMList) {
					hashMap.put("JDMC", hashMap2.get("JDMC").toString());
				}
				Double ssje=new BigDecimal(hashMap.get("SSJE").toString()).doubleValue();
				je += ssje;
			}
			wsje = htje - je;
			taskList.get(0).put("HTJE", htje);
			taskList.get(0).put("WSJE", wsje);
			return taskList;
		}
		return null;
	}

	/**
	 * 根据人员获得项目列表
	 * 
	 * @param index
	 * @return
	 */
	public List getChartProjectList(int index) {
		List<HashMap> slist = zqbGpfxProjectManageDAO.getProjectManagerUserData();
		if (slist != null) {
			String manager = "";
			String projectName = "";
			for (int i = 0; i < slist.size(); i++) {
				if (index == i + 1) {
					HashMap map = slist.get(i);
					manager = map.get("manager").toString();
					break;
				}
			}
			List datalist = getProjectJX(manager);
			/*
			 * List<HashMap> list = getTaskListForOwner(manager); List datalist
			 * = new ArrayList(); for (HashMap map : list) { if
			 * (map.get("PROJECTNO") != null) { HashMap conditionMap = new
			 * HashMap(); // 判断是否是超权限用户 conditionMap.put("PROJECTNO",
			 * map.get("PROJECTNO") .toString()); List<HashMap> tmplist =
			 * DemAPI.getInstance().getList( ProjectUUID, conditionMap, null);
			 * if (tmplist != null && tmplist.size() > 0) { HashMap data =
			 * tmplist.get(0); if
			 * (data.get("XMJD").toString().equals("持续督导")||data
			 * .get("STATUS").toString().equals("已完成")) continue; BigDecimal
			 * wsje=new BigDecimal( map.get("HTJE") == null ? "" :
			 * map.get("HTJE") .toString()).subtract(new BigDecimal(
			 * map.get("SSJE") == null ? "" : map.get("SSJE")
			 * .toString()));//未收金额 map.put("WSJE", wsje); datalist.add(map); }
			 * } }
			 */
			return datalist;
		}
		return null;
	}

	private List getProjectJX(String manager) {
		List datalist = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.projectname,a.htje,b.ssje,to_char(a.STARTDATE,'yyyy-MM-dd') startdate,to_char(a.ENDDATE,'yyyy-MM-dd') enddate from BD_ZQB_PJ_BASE a,(select projectno,sum(ssje) ssje from BD_PM_TASK s where SUBSTR(s.manager,0, instr(s.manager,'[',1)-1) =? group by projectno)  b  where a.projectno(+)=b.projectno");
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, UserContextUtil.getInstance().getCurrentUserId());
			rset = stmt.executeQuery();
			while (rset.next()) {
				String projectname = rset.getString("PROJECTNAME");
				Double ht = rset.getDouble("HTJE");
				Double ss = rset.getDouble("SSJE");
				String startDate = rset.getString("STARTDATE");
				String endDate = rset.getString("ENDDATE");
				HashMap map = new HashMap();
				map.put("PROJECTNAME", projectname);
				map.put("HTJE", ht);
				map.put("SSJE", ss);
				map.put("STARTDATE", startDate);
				map.put("ENDDATE", endDate);
				datalist.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return datalist;
	}

	public String[] getParticipantBarData() {
		// XLJ UPDATE 2015年5月6日13:57:25
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(distinct projectno) proNum,p FROM ("
				// 项目的负责人
				+ " select projectno,owner p from BD_ZQB_GPFXXMB a"
				+ " union all"
				// 现场负责人
				+ " select projectno,manager from BD_ZQB_GPFXXMB a"
				+ " union all"
				// 任务阶段负责人
				+ " select projectno,manager from BD_ZQB_GPFXXMRWB"
				+ " union all"
				// 项目及成员信息
				+ " select distinct projectno,userid||'['||name||']' from ("
				+ " select instanceid,dataid projectid,b.projectname,b.projectno from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=?) a"
				+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
				+ " ) c inner join ("
				+ " select a.instanceid,dataid,userid,b.name  from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
				+ " left join BD_ZQB_GROUP b on a.dataid = b.id"
				+ " ) d on c.instanceid = d.instanceid) A GROUP BY P"
				+ " ORDER BY proNum");
		List namelist = new ArrayList();
		List numlist = new ArrayList();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setLong(1, gpfxFormId);
			rset = stmt.executeQuery();
			while (rset.next()) {
				String name = rset.getString("p");
				if(name!=null&&!"".equals(name)){
					namelist.add(name);
					String projectNum = rset.getString("proNum");
					numlist.add(projectNum);
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		StringBuffer label = new StringBuffer();
		StringBuffer value = new StringBuffer();

		JSONArray list1 = JSONArray.fromObject(namelist);
		JSONArray list2 = JSONArray.fromObject(numlist);
		label.append(list1);
		value.append(list2);
		// [["项目2","场1","李玉萍","项目1"], [3,5,7,7]]
		String[] sj = { label.toString(), value.toString() };
		return sj;
	}

	/**
	 * 根据人员获得项目列表
	 * 
	 * @param index
	 * @return
	 */
	public List<HashMap> getParticipantChartProjectList(String username) {
		ArrayList list1 = new ArrayList();
		// XLJ UPDATE 2015年5月6日13:57:25.
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.projectno,PROJECTNAME,TAG,uname,(case XMJD when '项目开发' then '无阶段' else XMJD END) XMJD,"
				+ " (CASE WHEN GXSJ IS NULL THEN TO_CHAR(CREATEDATE,'YYYY-MM-DD') ELSE GXSJ END) GXSJ"
				+ "   FROM BD_ZQB_GPFXXMB B INNER JOIN"
				+ " (  "
				+ " SELECT projectno,uname,(case MIN(TAG) when 1 then '项目负责人' when 2 then '现场负责人' when 3 then '阶段负责人' when 4 then '项目成员' else '' end) tag"
				+ "    from ("
				// 项目的负责人
				+ "    select projectno,1 tag,owner uname from BD_ZQB_GPFXXMB a where owner =?"
				+ " union all"
				// 现场负责人
				+ " select projectno,2,manager from BD_ZQB_GPFXXMB a where manager = ?"
				+ " union all"
				// 任务阶段负责人
				+ " select projectno,3,manager from BD_ZQB_GPFXXMRWB where manager = ?"
				+ " union all"
				// 项目及成员信息
				+ " select distinct projectno,4,uname from ("
				+ " select instanceid,dataid projectid,b.projectname,b.projectno from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=?) a"
				+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
				+ " ) c inner join ("
				+ " select a.instanceid,dataid,userid||'['||b.name||']' uname  from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
				+ " left join BD_ZQB_GROUP b on a.dataid = b.id"
				+ " ) d on c.instanceid = d.instanceid"
				+ " where uname = ?"
				+ " ) a  GROUP BY projectno,uname"
				+ " )C ON B.PROJECTNO = C.PROJECTNO"
				+ " LEFT JOIN (SELECT PROJECTNO,TO_CHAR(MAX(GXSJ),'YYYY-MM-DD') GXSJ FROM BD_ZQB_GPFXXMRWB GROUP BY PROJECTNO) D"
				+ " ON B.PROJECTNO = D.PROJECTNO ORDER BY GXSJ DESC");
		List namelist = new ArrayList();
		List numlist = new ArrayList();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, username);
			stmt.setString(2, username);
			stmt.setString(3, username);
			stmt.setLong(4, gpfxFormId);
			stmt.setString(5, username);
			rset = stmt.executeQuery();
			while (rset.next()) {
				HashMap tmpMap = new HashMap();
				tmpMap.put("NAME", rset.getString("uname"));
				tmpMap.put("PROJECTNAME", rset.getString("PROJECTNAME"));
				tmpMap.put("POSITION", rset.getString("TAG"));// 角色
				tmpMap.put("XMJD", rset.getString("XMJD"));
				tmpMap.put("GXSJ", rset.getString("GXSJ"));
				tmpMap.put("PROJECTNO", rset.getString("PROJECTNO"));
				list1.add(tmpMap);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list1;
	}

	public String getParticipantGridBarData() {
		StringBuffer jsonHtml = new StringBuffer();
		List<ArrayList> val = getBar();
		if (val != null) {

			jsonHtml.append("<table>").append("\n");
			jsonHtml.append(
					"<tr><th>项目名称</th><th>项目角色</th><th>人员姓名</th><th>进展阶段</th><th>填报日期</th><th>最新更新时间</th></tr>")
					.append("\n");
			for (ArrayList item : val) {
				if (item == null) {
					continue;
				}
				for (int i = 0; i < item.size(); i++) {
					HashMap pro = (HashMap) item.get(i);
					jsonHtml.append("<tr>").append("\n");
					jsonHtml.append("<td>").append(pro.get("PROJECTNAME"))
							.append("</td>").append("\n");
					jsonHtml.append("<td>").append(pro.get("POSITION"))
							.append("</td>").append("\n");
					jsonHtml.append("<td>").append(pro.get("NAME"))
							.append("</td>").append("\n");
					jsonHtml.append("<td>").append(pro.get("XMJD").toString().equals("项目开发")?"无阶段":pro.get("XMJD").toString())
							.append("</td>").append("\n");
					jsonHtml.append("<td>").append(pro.get("CREATEDATE"))
							.append("</td>").append("\n");
					jsonHtml.append("<td>").append(pro.get("GXSJ"))
							.append("</td>").append("\n");
					jsonHtml.append("</tr>").append("\n");
				}
			}

			jsonHtml.append("</table>").append("\n");
		}

		return jsonHtml.toString();

	}

	public void doExcelExp(HttpServletResponse response) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目参与人统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("项目名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("项目角色");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("人员姓名");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("进展阶段");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("填报日期");
		cell.setCellStyle(style);

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List list = getBar();
		List<Map> l;
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		for (int i = 0; i < list.size(); i++) {

			l = (ArrayList<Map>) list.get(i);
			for (int j = 0; j < l.size(); j++) {
				Map map = (HashMap) l.get(j);
				row = sheet.createRow((int) n++);
				// 第四步，创建单元格，并设置值
				HSSFCell cell1 = row.createCell((short) 0);
				cell1.setCellValue(map.get("PROJECTNAME").toString());
				cell1.setCellStyle(style1);
				HSSFCell cell2 = row.createCell((short) 1);
				cell2.setCellValue(map.get("POSITION").toString());
				cell2.setCellStyle(style1);
				HSSFCell cell3 = row.createCell((short) 2);
				cell3.setCellValue(map.get("NAME").toString());
				cell3.setCellStyle(style1);
				HSSFCell cell4 = row.createCell((short) 3);
				cell4.setCellValue(map.get("XMJD").toString().equals("项目开发")?"无阶段":map.get("XMJD").toString());
				cell4.setCellStyle(style1);

				// 增加时间列
				HSSFCell cell5 = row.createCell((short) 4);
				cell5.setCellValue(map.get("CREATEDATE").toString());
				cell5.setCellStyle(style1);

				short colLength = (short) (map.get("PROJECTNAME").toString()
						.length() * 256 * 2);
				if (sheet.getColumnWidth(m) < colLength) {
					sheet.setColumnWidth(m, colLength);
				}
				m++;
			}

		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目参与人统计.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
			// out1.flush();
			// out1.close();

		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}
	}

	public void doExcelExp1(HttpServletResponse response) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目风险分数统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("风险阶段");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("数量");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List list = getBar1();
		List<Map> l;
		List<ArrayList> list1 = list;
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		for (List item : list1) {
			if (item == null) {
				continue;
			}
			String title = (String) item.get(0);
			int value = Integer.parseInt(item.get(1).toString());
			row = sheet.createRow((int) n++);
			// 第四步，创建单元格，并设置值
			HSSFCell cell1 = row.createCell((short) 0);
			cell1.setCellValue(title);
			cell1.setCellStyle(style1);
			HSSFCell cell2 = row.createCell((short) 1);
			cell2.setCellValue(value);
			cell2.setCellStyle(style1);
			short colLength = (short) (title.length() * 256 * 2);
			if (sheet.getColumnWidth(m) < colLength) {
				sheet.setColumnWidth(m, colLength);
			}
			m++;
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename="
					+ UploadFileNameCodingUtil.StringEncoding("项目风险分数统计.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {

			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}
	}

	public void doExcelExp2(HttpServletResponse response) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目阶段统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("项目阶段");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("项目数量");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = getBar2();
		List<Map> l;
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		for (HashMap item : list) {
			if (item == null) {
				continue;
			}
			String title = (String) item.get("xmjd");
			int value = Integer.parseInt(item.get("count").toString());
			row = sheet.createRow((int) n++);
			// 第四步，创建单元格，并设置值
			HSSFCell cell1 = row.createCell((short) 0);
			cell1.setCellValue(title);
			cell1.setCellStyle(style1);
			HSSFCell cell2 = row.createCell((short) 1);
			cell2.setCellValue(value);
			cell2.setCellStyle(style1);
			short colLength = (short) (title.length() * 256 * 2);
			if (sheet.getColumnWidth(m) < colLength) {
				sheet.setColumnWidth(m, colLength);
			}
			m++;
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目阶段统计.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {

			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}
	}

	public void doExcelExp3(HttpServletResponse response) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目完成率统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("项目阶段");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("项目数量");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List list = getBar3();
		List<Map> l;
		List<ArrayList> list1 = list;
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		for (List item : list1) {
			if (item == null) {
				continue;
			}
			String title = (String) item.get(0);
			int value = Integer.parseInt(item.get(1).toString());
			row = sheet.createRow((int) n++);
			// 第四步，创建单元格，并设置值
			HSSFCell cell1 = row.createCell((short) 0);
			cell1.setCellValue(title);
			cell1.setCellStyle(style1);
			HSSFCell cell2 = row.createCell((short) 1);
			cell2.setCellValue(value);
			cell2.setCellStyle(style1);
			short colLength = (short) (title.length() * 256 * 2);
			if (sheet.getColumnWidth(m) < colLength) {
				sheet.setColumnWidth(m, colLength);
			}
			m++;

		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目完成率统计.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {

			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}
	}

	public List getBar() {
		StringBuffer jsonHtml = new StringBuffer();
		HashMap<String, Long> data = new HashMap();
		HashMap<String, ArrayList> data1 = new HashMap();
		HashMap conditionMap = new HashMap();
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
				conditionMap, null);
		List<ArrayList> val = new ArrayList();
		for (HashMap map : tmplist) {
			String projectNo = map.get("PROJECTNO").toString();
			String gxsj = DBUtil.getString("select TO_CHAR(GXSJ,'YYYY-MM-DD HH24:MI') AS GXSJ from BD_ZQB_GPFXXMRWB where GXSJ=(select max(GXSJ) from BD_ZQB_GPFXXMRWB where projectno='"+ projectNo + "')", "GXSJ");
			map.put("GXSJ", gxsj);
			OrgUser orgUser = null;
			orgUser = UserContextUtil.getInstance().getOrgUserInfo(
					map.get("OWNER").toString());
			if (orgUser != null) {
				// 获取现场负责人
				if (map.get("OWNER") != null) {
					String owner = map.get("OWNER").toString();

					if (null != owner) {

						HashMap m = (HashMap) map.clone();
						m.put("NAME", orgUser.getUsername());
						m.put("POSITION", "项目负责人");
						if (data1.get(owner) == null) {
							ArrayList list = new ArrayList();
							list.add(m);
							data1.put(owner, list);
							data.put(owner, new Long(1));
						} else {
							ArrayList list1 = data1.get(owner);
							if (list1.contains(m)
									&& ((Map) list1.get(list1.indexOf(m))).get(
											"POSITION").equals("项目负责人")) {
								continue;
							} else {
								Long num = data.get(owner);
								num++;
								data.put(owner, num++);
								list1.add(m);
								data1.put(owner, list1);
							}
						}
					}
				}
			}
			// 获得项目负责人
			orgUser = UserContextUtil.getInstance().getOrgUserInfo(
					map.get("MANAGER").toString());
			if (orgUser != null) {
				if (map.get("MANAGER") != null) {
					String owner = map.get("MANAGER").toString();
					if (null != owner) {
						HashMap m = (HashMap) map.clone();
						m.put("NAME", orgUser.getUsername());
						m.put("POSITION", "项目现场负责人");
						if (data1.get(owner) == null) {
							ArrayList list = new ArrayList();
							list.add(m);
							data1.put(owner, list);
							data.put(owner, new Long(1));
						} else {
							ArrayList list1 = data1.get(owner);
							if (list1.contains(m)
									&& ((Map) list1.get(list1.indexOf(m))).get(
											"POSITION").equals("项目现场负责人")) {
								continue;
							} else {
								Long num = data.get(owner);
								num++;
								data.put(owner, num++);
								list1.add(m);
								data1.put(owner, list1);
							}
						}
					}
				}
			}
			// 获取项目成员
			Long instanceid = Long.parseLong(map.get("INSTANCEID").toString());
			List<HashMap> sublist = DemAPI.getInstance().getFromSubData(
					instanceid, "SUBFORM_XMCYLB");
			if(sublist==null){
				sublist=new ArrayList<HashMap>();
			}
			for (HashMap subItem : sublist) {
				if (subItem.get("USERID") != null && !subItem.equals("")) {
					String userid = subItem.get("USERID").toString();
					String address = UserContextUtil.getInstance()
							.getFullUserAddress(userid);
					if (null != address) {
						orgUser = UserContextUtil.getInstance().getOrgUserInfo(
								address);
						if (orgUser != null) {

							HashMap m = (HashMap) map.clone();
							m.put("NAME", orgUser.getUsername());
							m.put("POSITION", "项目成员");
							if (data1.get(address) == null) {
								ArrayList list = new ArrayList();

								list.add(m);
								data1.put(address, list);
								data.put(address, new Long(1));
							} else {
								ArrayList list1 = data1.get(address);
								if (list1 != null) {
									if (list1.contains(m)
											&& ((Map) list1.get(list1
													.indexOf(m))).get(
													"POSITION").equals("项目成员")) {
										continue;
									} else {
										Long num = data.get(address);
										num++;
										list1.add(m);
										data.put(address, num++);
										data1.put(address, list1);
									}
								}
							}
						}
					}
				}
			}
			HashMap condition = new HashMap();
			condition.put("PROJECTNO", map.get("PROJECTNO"));
			List<HashMap> l = DemAPI.getInstance().getList(ProjectItemUUID,
					condition, "GXSJ");
			for (HashMap hashMap : l) {
				if ((hashMap.get("MANAGER") != null && !hashMap.equals(""))&&!hashMap.get("MANAGER").toString().equals(map.get("OWNER"))&&!hashMap.get("MANAGER").equals(map.get("MANAGER").toString())) {
					String userid = hashMap.get("MANAGER").toString();
					String address = UserContextUtil.getInstance()
							.getFullUserAddress(userid);
					if (null != address) {
						orgUser = UserContextUtil.getInstance().getOrgUserInfo(
								address);
						if (orgUser != null) {
							HashMap m = (HashMap) map.clone();
							m.put("NAME", orgUser.getUsername());
							m.put("POSITION", "阶段负责人");
							if (data1.get(address) == null) {
								ArrayList list = new ArrayList();

								list.add(m);
								data1.put(address, list);
								data.put(address, new Long(1));
							} else {
								ArrayList list1 = data1.get(address);
								if (list1 != null) {
									if (list1.contains(m)
											&& ((Map) list1.get(list1
													.indexOf(m))).get(
													"POSITION").equals("阶段负责人")) {
										continue;
									} else {
										Long num = data.get(address);
										num++;
										list1.add(m);
										data.put(address, num++);
										data1.put(address, list1);
									}
								}
							}
						}
					}
				}
			}
		}

		List slist = new ArrayList();
		Iterator iter = data.entrySet().iterator();
		List lablelist = new ArrayList();
		List valuelist = new ArrayList();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val1 = entry.getValue();
			if (key == null)
				continue;
			OrgUser orgUser1 = UserContextUtil.getInstance().getOrgUserInfo(
					key.toString());
			if (orgUser1 == null) {
				continue;
			}

			List item = new ArrayList();
			lablelist.add(key);
			valuelist.add(val1);
			slist.add(item);

		}

		// 排序
		for (int i = 0; i < valuelist.size(); i++) {
			for (int j = i; j < valuelist.size(); j++) {
				if (Integer.parseInt(valuelist.get(i).toString()) < Integer
						.parseInt(valuelist.get(j).toString())) {
					Object temp = valuelist.get(i);
					Object temp1 = lablelist.get(i);
					valuelist.set(i, valuelist.get(j));
					lablelist.set(i, lablelist.get(j));
					valuelist.set(j, temp);
					lablelist.set(j, temp1);

				}
			}
		}
		for (int i = 0; i < lablelist.size(); i++) {
			Object temp2 = lablelist.get(i);
			ArrayList temp3 = data1.get(temp2.toString());
			val.add(temp3);
		}

		return val;

	}

	public List getBar1() {
		boolean isSuperMan = this.getIsSuperMan();
		List<String> pjNoList = null;
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		if (!isSuperMan) {
			pjNoList = new ArrayList();
			List<HashMap> projectList = DemAPI.getInstance().getList(
					ProjectUUID, null, null);
			for (HashMap data : projectList) {
				boolean flag = this.checkProjectSecurity(data, UserContextUtil
						.getInstance().getCurrentUserContext());
				if (flag) {
					pjNoList.add(data.get("PROJECTNO").toString());
				}
			}
		}
		List<ArrayList> list = null;
		list = zqbGpfxProjectManageDAO.getProjectFengXian(pjNoList, UserContextUtil
				.getInstance().getCurrentUserFullName());
		return list;
	}

	public List getBar2() {
		List list = null;
		boolean isSuperMan = this.getIsSuperMan();
		if (isSuperMan) {
			list = zqbGpfxProjectManageDAO.getProjectStageList((null));
		} else {
			list = zqbGpfxProjectManageDAO.getProjectStageList(UserContextUtil
					.getInstance().getCurrentUserFullName());
		}
		return list;
	}

	public List getBar3() {
		boolean isSuperMan = this.getIsSuperMan();
		List<ArrayList> list = null;
		if (isSuperMan) {
			list = zqbGpfxProjectManageDAO.getProjectTypeStatus(null);
		} else {
			list = zqbGpfxProjectManageDAO.getProjectTypeStatus(UserContextUtil
					.getInstance().getCurrentUserFullName());
		}
		return list;
	}

	public List<HashMap> getXmcy(boolean superman) {
		HashMap conditionMap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();

		// 判断是否是超权限用户
		conditionMap.put("STATUS", "执行中");
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
				conditionMap, "ID");
		for (HashMap data : tmplist) {
			if (data.get("XMJD").toString().equals("持续督导"))
				continue;
			if (!superman) {
				boolean flag = this.checkProjectSecurity(data, uc);
				if (!flag)
					continue;
			}
			List<HashMap> xmcyList = DemAPI.getInstance().getFromSubData(
					Long.parseLong(data.get("INSTANCEID").toString()),
					"SUBFORM_XMCYLB");
			String names = "";
			for (HashMap xmcy : xmcyList) {
				names += xmcy.get("NAME") + ",";
			}
			data.put("NAMES",
					!names.equals("") ? names.substring(0, names.length() - 2)
							: names);
			HashMap condition = new HashMap();
			condition.put("PROJECTNO", data.get("PROJECTNO"));

			List<HashMap> l = DemAPI.getInstance().getList(ProjectItemUUID,
					condition, "GXSJ");
			if (l.size() > 0) {
				Collections.reverse(l);
				data.put("GXSJ", l.get(0).get("GXSJ"));
			} else {
				data.put("GXSJ", "");
			}
			list.add(data);

		}
		return list;
	}

	/**
	 * 以项目为基准统计
	 * 
	 * @param superman
	 * @return
	 */
	public String getYxmwjz(boolean superman, int pageNumberXM, int pageSizeXM) {
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> tmplist = getXMList(superman, pageNumberXM,
				pageSizeXM);
		if (tmplist.size() > 0) {
			jsonHtml.append(
					"<table width=\"100%\" style=\"border:1px solid #efefef\">")
					.append("\n");
			jsonHtml.append(
					"<tr class=\"header\"><td>项目名称</td><td>项目最新进展</td><td>最后更新时间</td><td>参与人</td></tr>")
					.append("\n");
			for (Map<String, Object> data : tmplist) {
				jsonHtml.append("<tr class=\"cell\">").append("\n");
				jsonHtml.append("<td>").append(data.get("PROJECTNAME"))
						.append("</td>").append("\n");
				jsonHtml.append("<td>").append(data.get("XMJD"))
						.append("</td>").append("\n");

				jsonHtml.append("<td>").append(data.get("GXSJ"))
						.append("</td>").append("\n");

				jsonHtml.append("<td>").append(data.get("NAMES"))
						.append("</td>").append("\n");
				jsonHtml.append("</tr>").append("\n");
			}
			jsonHtml.append("</table>").append("\n");

		}
		return jsonHtml.toString();
	}

	/**
	 * 按项目统计总条数
	 * 
	 * @param superman
	 * @return
	 */
	public int getTotalNum(boolean superman) {
		HashMap conditionMap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();

		// 判断是否是超权限用户
		conditionMap.put("STATUS", "执行中");
		String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
				conditionMap, "ID");
		int n = 0;
		for (HashMap data : tmplist) {
			if (!superman) {
				boolean flag = this.checkProjectSecurity(data, uc);
				if (!flag)
					continue;
			}
			n++;
		}
		return n;
	}

	public void doXmcyExp(HttpServletResponse response, boolean flag) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目及成员信息");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("序号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("项目名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("项目负责人");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("项目成员");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("进展");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("最新更新日期");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = getXmcy(flag);
		List<Map> l;
		List<HashMap> list1 = list;
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		for (HashMap item : list1) {
			if (item == null) {
				continue;
			}
			m++;
			row = sheet.createRow((int) n++);
			// 第四步，创建单元格，并设置值
			HSSFCell cell0 = row.createCell((short) 0);
			cell0.setCellValue(m);
			cell0.setCellStyle(style1);
			HSSFCell cell1 = row.createCell((short) 1);
			cell1.setCellValue(item.get("PROJECTNAME").toString());
			cell1.setCellStyle(style1);
			HSSFCell cell2 = row.createCell((short) 2);
			cell2.setCellValue(item.get("OWNER").toString());
			cell2.setCellStyle(style1);
			HSSFCell cell3 = row.createCell((short) 3);
			cell3.setCellValue(item.get("NAMES").toString());
			cell3.setCellStyle(style1);
			HSSFCell cell4 = row.createCell((short) 4);
			cell4.setCellValue(item.get("XMJD").toString());
			cell4.setCellStyle(style1);
			HSSFCell cell5 = row.createCell((short) 5);
			cell5.setCellValue(item.get("GXSJ") != null ? item.get("GXSJ")
					.toString() : "");
			cell5.setCellStyle(style1);
			short colLength = (short) (item.get("PROJECTNAME").toString()
					.length() > item.get("OWNER").toString().length() ? item
					.get("PROJECTNAME").toString().length() : item.get("OWNER")
					.toString().length() * 256 * 2);
			if (sheet.getColumnWidth(m) < colLength) {
				sheet.setColumnWidth(m, colLength);
			}

		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目及成员信息.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {

			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}

		}
	}

	public List<HashMap> getRunProjectList1(boolean superman, int pageNumber,
			int pageSize, String projectName, String xmjd, String startDate,
			String customername,String dgzt, String czbm, String cyrName, HashMap<String,List<String>> parameterMap) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		// 判断是否是超权限用户
		SysMessageDAO sd = new SysMessageDAO();
		List<HashMap> list = new ArrayList();
		List<HashMap> tmplist = new ArrayList<HashMap>();
		if (superman) {
			tmplist = zqbGpfxProjectManageDAO.getRunProjectList1(pageNumber,
					pageSize, projectName, xmjd, startDate, customername,dgzt,czbm,cyrName);
		} else {
			Long ismanager=getIsManager(uc);
			tmplist = zqbGpfxProjectManageDAO.getRunProjectList2(owner, pageNumber,
					pageSize, projectName, xmjd, startDate, customername,dgzt,parameterMap,czbm,cyrName,ismanager);
		}
		for (HashMap data : tmplist) {
			data.put("ISYD",!sd.getSyMsgSize(data.get("INSTANCEID").toString()));
			list.add(data);
		}
		return list;
	}
	
	public List<HashMap> getRunProjectList3(boolean superman, int pageNumber,
			int pageSize, String projectName, String xmjd, String startDate,
			String customername,String dgzt) {
		HashMap conditionMap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		// 判断是否是超权限用户
		conditionMap.put("STATUS", "执行中");
		// String[] param =
		// {"项目开发","签署协议","股改","尽职调查","申报材料","内核","内核反馈","申报","申报反馈"};
		SysMessageDAO sd = new SysMessageDAO();
		List<HashMap> list = new ArrayList();
		List<HashMap> tmplist = new ArrayList<HashMap>();
			tmplist = zqbGpfxProjectManageDAO.getRunProjectList1(pageNumber,
					pageSize, projectName, xmjd, startDate, customername,dgzt,null,null);
		/*
		 * List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
		 * conditionMap, "ID");
		 */
		for (HashMap data : tmplist) {
			if (data.get("XMJD").toString().equals("持续督导"))
				continue;
			/*
			 * if (!superman) { boolean flag = this.checkProjectSecurity(data,
			 * uc); if (!flag) continue; }
			 */
			String gxsj = DBUtil
					.getString(
							"select TO_CHAR(GXSJ,'YYYY-MM-DD HH24:MI') AS GXSJ from BD_PM_TASK where GXSJ=(select max(GXSJ) from BD_PM_TASK where projectno='"
									+ data.get("PROJECTNO") + "')", "GXSJ");
			data.put("GXSJ", gxsj);
			data.put("ISYD",
					!sd.getSyMsgSize(data.get("INSTANCEID").toString()));
			String purview = this.isPurview(data,
					ZQBConstants.ISPURVIEW_TYPE_PROJECT_KEY);
			if (purview.equals(ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER)) {
				data.put("ISPURVIEW", true);
				list.add(data);
			} else if (purview.equals(ZQBConstants.ISPURVIEW_ROLE_TYPE_VISITR)
					|| superman) {
				data.put("ISPURVIEW", false);
				list.add(data);
			} else if (purview.equals(ZQBConstants.ISPURVIEW_ROLE_TYPE_GUEST)) {
				data.put("ISPURVIEW", true);
				list.add(data);
			}
		}
		// Collections.reverse(list);
		return list;
	}

	public List getRunProjectListSize1(boolean superman, String projectName,
			String xmjd, String startDate, String customername,String dgzt, String czbm, String cyrName, HashMap<String,List<String>> parameterMap) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		final String userid = userModel.getUserid().toString().trim();
		Long ismanager = getIsManager(uc);
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		Long orgRoleId = userModel.getOrgroleid();
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		map.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
		map.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
		map.put("createuserid", "USERID");
		map.put("userid", "USERID");
		map.put("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)", "USERID");
		map.put("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)", "USERID");
		map.put("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)", "USERID");
		if(ismanager==1){
			if(cyrName!=null&&!cyrName.equals("")){
				parameter = zqbGpfxProjectManageDAO.getCyrUserMap(map, cyrName);
			}else{
				parameter = getParameterMap(map,parameterMap);
			}
		}
		List params =new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) CNUM FROM (SELECT DISTINCT J.*,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD, E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj,g.pjr pjr FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
		if (!superman) {
			if(ismanager==1){
				/*sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ("
						+ parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)")
						+ ")  or createuserid in ("+parameter.get("createuserid")+") or SUBSTR(manager,0, instr(manager,'[',1)-1) in ("
						+ parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)")
						+ ") union all"
						// 任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ("
						+ parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)")
						+ ") union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid="+gpfxFormId+") a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ("
						+ parameter.get("userid") + ")"
						// 分派项目审批人
						+ " union all");*/
				sb.append(" INNER JOIN ( select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				
				sb.append(" )  or createuserid in ( ");
				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(" ) union all select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sb.append(" ) union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a");
				params.add(gpfxFormId);
				sb.append( " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
				String[] userids =parameter.get("userid").split(",");
				for (int i = 0; i < userids.length; i++) {
					if(i==(userids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userids[i].replaceAll("'", ""));
				}
				sb.append(" ) union all");
				
				// if 按多项目组审批
				// if 按多项目组审批
				if (config.equals("2")) {
					/*sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)")
							+ ") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)")
							+ ") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)") + ")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");*/
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					
					// else
				} else {
					/*sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
							+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (" + parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)") + ") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (" + parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)")
							+ ") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (" + parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)") + "))");*/
					sb.append(" select projectno from BD_ZQB_GPFXXMB WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					
					
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
										for (int i = 0; i < ZZSPR.length; i++) {
											if(i==(ZZSPR.length-1)){
												sb.append("?");
											}else{
												sb.append("?,");
											}
											params.add(ZZSPR[i].replaceAll("'", ""));
										}
					sb.append(" ))");
					
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}else{
				/*sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) ='"
						+ owner
						+ "'  or createuserid='"+userid+"' or SUBSTR(manager,0, instr(manager,'[',1)-1) = '"
						+ owner
						+ "' union all"
						// 任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = '"
						+ owner
						+ "' union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid="+gpfxFormId+") a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = '"
						+ userid + "'"
						// 分派项目审批人
						+ " union all");*/
				sb.append(" INNER JOIN ( select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) = ?  or createuserid= ? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						// 任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ? "
						// 分派项目审批人
						+ " union all");
				params.add(owner);
				params.add(userid);
				params.add(owner);
				params.add(owner);
				params.add(gpfxFormId);
				params.add(userid);
				if (config.equals("2")) {
					
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				} else {
					
					sb.append(" select projectno from BD_ZQB_GPFXXMB WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? )");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}else{
			if(cyrName!=null&&!cyrName.equals("")){
				parameter = zqbGpfxProjectManageDAO.getCyrUserMap(map, cyrName);
				/*sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ("
						+ parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)")
						+ ")  or createuserid in ("+parameter.get("createuserid")+") or SUBSTR(manager,0, instr(manager,'[',1)-1) in ("
						+ parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)")
						+ ") union all"
						// 任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ("
						+ parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)")
						+ ") union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid="+gpfxFormId+") a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ("
						+ parameter.get("userid") + ")"
						// 分派项目审批人
						+ " union all");*/
				sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sb.append(" )  or createuserid in ( ");
				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				
				sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(" ) union all select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sb.append(" ) union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a");
				params.add(gpfxFormId);
				sb.append(" left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
				String[] userids =parameter.get("userid").split(",");
				for (int i = 0; i < userids.length; i++) {
					if(i==(userids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userids[i].replaceAll("'", ""));
				}
				sb.append(" )  union all");
				
				if (config.equals("2")) {
					/*sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)")
							+ ") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)")
							+ ") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)") + ")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");*/
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append("  ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
				
				} else {
					/*sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
							+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (" + parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)") + ") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (" + parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)")
							+ ") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (" + parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)") + "))");*/
					sb.append(" select projectno from BD_ZQB_GPFXXMB WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
							+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (  ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" ))");
					
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sb.append(" AND CZBM like ? ");
			params.add("%" + czbm + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		sb.append(" ) A  LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.ID DESC"
				+ " ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
				+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
				+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
		params.add(gpfxFormId);
		if (!superman) {
			if(ismanager==1){
				/*sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ("
						+ parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)")
						+ ")  or createuserid in ("+parameter.get("createuserid")+") or SUBSTR(manager,0, instr(manager,'[',1)-1) in ("
						+ parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)")
						+ ")"
						+ " union all"
						// 任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ("
						+ parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)")
						+ ") union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid="+gpfxFormId+") a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
						+ " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ("
						+ parameter.get("userid") + ")");*/
				sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sb.append(" )  or createuserid in ( ");
				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(" ) union all"
						// 任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sb.append(" ) union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a ");
				params.add(gpfxFormId);
				sb.append(" left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
						+ " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
				String[] userids =parameter.get("userid").split(",");
				for (int i = 0; i < userids.length; i++) {
					if(i==(userids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userids[i].replaceAll("'", ""));
				}
				sb.append(" )");
				
				sb.append(" union all");
				// if 按多项目组审批
				if (config.equals("2")) {
					/*sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ("
							+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)")
							+ ") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)")
							+ ") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)") + ")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");*/
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ( select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					
				} else {
					// else
					/*sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)")
							+ ") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)")
							+ ") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)") + "))");*/
					sb.append(" select projectno from BD_ZQB_GPFXXMB WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (  ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" ))");
					
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
			}else{
				/*sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) ='"
						+ owner
						+ "'  or createuserid='"+userid+"' or SUBSTR(manager,0, instr(manager,'[',1)-1) = '"
						+ owner
						+ "'"
						+ " union all"
						// 任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = '"
						+ owner
						+ "' union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid="+gpfxFormId+") a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
						+ " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = '"
						+ userid + "'");*/
				sb.append(" INNER JOIN ( select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ?"
						+ " union all"
						// 任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=?) a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
						+ " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ? ");
				params.add(owner);
				params.add(userid);
				params.add(owner);
				params.add(owner);
				params.add(gpfxFormId);
				params.add(userid);
				sb.append(" union all");
				// if 按多项目组审批
				if (config.equals("2")) {
					
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ( select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? " + " ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				} else {
					
					sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)" + " ");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
			}
		}else{
			if(cyrName!=null&&!cyrName.equals("")){
				
				sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sb.append(" )  or createuserid in ( ");
				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");	
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(" ) union all"
						// 任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sb.append(" ) union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=?) a" );
				params.add(gpfxFormId);
				sb.append(" left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
				String[] userids =parameter.get("userid").split(",");
				for (int i = 0; i < userids.length; i++) {
					if(i==(userids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userids[i].replaceAll("'", ""));
				}
				sb.append(" ) union all");
			
				// if 按多项目组审批
				if (config.equals("2")) {
					/*sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)")
							+ ") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)")
							+ ") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ("
							+ parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)") + ")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");*/
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					
				} else {
					sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
							+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" ))");
					
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
			
		}
		if(czbm!=null&&!czbm.equals("")){
			sb.append(" AND CZBM like ? ");
			params.add("%" + czbm + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO ");

		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.ID DESC) C"
				+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid)");
		params.add(gpfxFormId);
		final List param=params;
		final String sql1 = sb.toString();
		return zqbGpfxProjectManageDAO.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql1);
						for (int i = 0; i < param.size(); i++) {
							query.setParameter(i, param.get(i));
						}
						List<Integer> list = query.list();
						return list;
					}
				});
	}
	
	public List getRunProjectListSize3(boolean superman, String projectName,
			String xmjd, String startDate, String customername,String dgzt) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		final String userid = UserContextUtil.getInstance().getCurrentUserContext()._userModel
				.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params=new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT DISTINCT J.*,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD, E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj,g.pjr pjr FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.ID DESC"
				+ " ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
				+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
				+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
		
		// WHERE JDMC LIKE '%股改%'
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.ID DESC) C ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		final String sql1 = sb.toString();
		final List param =params;
		return zqbGpfxProjectManageDAO.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql1);
						for (int i = 0; i < param.size(); i++) {
							query.setParameter(i, param.get(i));
						}
						List<HashMap> l = new ArrayList<HashMap>();
						List<Object[]> list = query.list();
						HashMap map;
						HashMap m = new HashMap();
						BigDecimal b;
						int n = 0;
						for (Object[] object : list) {
							map = new HashMap();
							String customername = (String) object[2];
							String owner = (String) object[3];
							Double htje = ((BigDecimal) object[4]).doubleValue();
							Double wsje = 0.0;
							if (object[5] != null) {
								wsje = ((BigDecimal) object[5]).doubleValue();
							}
							Double ssje = 0.0;
							if (object[8] != null) {
								ssje = ((BigDecimal) object[8]).doubleValue();
							}
							String rwjd = (String) object[9];
							String spzt = (String) object[11];
							String manager1 = (String) object[12];
							Integer pjinsid = 0;
							if (object[13] != null) {
								pjinsid = Integer.valueOf(object[13].toString());
							}
							Integer xgwtinsid = 0;
							if (object[14] != null) {
								xgwtinsid = Integer.valueOf(object[14].toString());
							}
							Integer instanceid = Integer.valueOf(object[15]
									.toString());
							String xmjd = object[6] == null ? "" : object[6]
									.toString();
							String projectNo = object[1] == null ? ""
									: object[1].toString();
							String groupid = object[7] == null ? "" : object[7]
									.toString();
							String pj = object[17] == null ? "" : object[17]
									.toString();
							Integer num = Integer.valueOf(object[19].toString());
							String pjr = object[17]==null?"":object[17].toString();
							if(!pjr.equals(userid)){
								Long zPjid = DBUtil.getLong("SELECT INSTANCEID FROM BD_ZQB_GPFXXMPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_GPFXXMPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PROJECTNO='"+projectNo+"' AND PJR='"+userid+"' AND GROUPID="+groupid+" ORDER BY ID", "INSTANCEID");
								if(zPjid!=null){
									pjinsid=Integer.parseInt(zPjid.toString());
								}else{
									pjinsid=0;
								}
							}
							map.put("OWNER", owner);
							map.put("PROJECTNO", projectNo);
							map.put("INSTANCEID", instanceid);
							map.put("XMJD", xmjd);
							map.put("RWJD", rwjd);
							map.put("SPZT", spzt);
							map.put("CUSTOMERNAME", customername);
							map.put("HTJE", htje);
							map.put("SSJE", ssje);
							map.put("WSJE", wsje);
							map.put("JDFZR", manager1);
							map.put("NUM", num);
							map.put("GROUPID", groupid);
							map.put("PJINSID", pjinsid);
							l.add(map);
						}
						return l;
					}
				});
	}

	
	public List getFinishProjectListSize2(boolean superman, String projectName,
			String xmjd, String startDate, String customername,String dgzt) {
		String owner = UserContextUtil.getInstance().getCurrentUserContext()._userModel
				.getUserid();
				//+ "["
				//+ UserContextUtil.getInstance().getCurrentUserContext()._userModel
				//.getUsername() + "]";
		final String userid = UserContextUtil.getInstance().getCurrentUserContext()._userModel
				.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params=new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD, E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,g.pjr pjr FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like '%%'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='已完成' and A.XMJD='持续督导' ORDER BY A.PROJECTNO DESC,GROUPID"
				+ " ) C)) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from ( SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj"
				+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO  LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
		
		// WHERE JDMC LIKE '%股改%'
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like '%%'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='已完成' and A.XMJD='持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C"
				+ " )) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		final String sql1 = sb.toString();
		final List param=params;
		return zqbGpfxProjectManageDAO.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql1);
						for (int i = 0; i < param.size(); i++) {
							query.setParameter(i, param.get(i));
						}
						List<HashMap> l = new ArrayList<HashMap>();
						List<Object[]> list = query.list();
						HashMap map;
						HashMap m = new HashMap();
						BigDecimal b;
						int n = 0;
						for (Object[] object : list) {
							map = new HashMap();
							String customername = (String) object[2];
							String owner = (String) object[3];
							Double htje = ((BigDecimal) object[4]).doubleValue();
							Double wsje = 0.0;
							if (object[5] != null) {
								wsje = ((BigDecimal) object[5]).doubleValue();
							}
							Double ssje = 0.0;
							if (object[8] != null) {
								ssje = ((BigDecimal) object[8]).doubleValue();
							}
							String rwjd = (String) object[9];
							String spzt = (String) object[11];
							String manager1 = (String) object[12];
							Integer pjinsid = 0;
							if (object[13] != null) {
								pjinsid = Integer.valueOf(object[13].toString());
							}
							Integer xgwtinsid = 0;
							if (object[14] != null) {
								xgwtinsid = Integer.valueOf(object[14].toString());
							}
							Integer instanceid = Integer.valueOf(object[15]
									.toString());
							String xmjd = object[6] == null ? "" : object[6]
									.toString();
							String projectNo = object[1] == null ? ""
									: object[1].toString();
							String groupid = object[7] == null ? "" : object[7]
									.toString();
							String pj = object[17] == null ? "" : object[17]
									.toString();
							Integer num = Integer.valueOf(object[19].toString());
							String pjr = object[17]==null?"":object[17].toString();
							if(!pjr.equals(userid)){
								Long zPjid = DBUtil.getLong("SELECT INSTANCEID FROM BD_ZQB_GPFXXMPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_GPFXXMPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PROJECTNO='"+projectNo+"' AND PJR='"+userid+"' AND GROUPID="+groupid+" ORDER BY ID", "INSTANCEID");
								if(zPjid!=null){
									pjinsid=Integer.parseInt(zPjid.toString());
								}else{
									pjinsid=0;
								}
							}
							map.put("OWNER", owner);
							map.put("PROJECTNO", projectNo);
							map.put("INSTANCEID", instanceid);
							map.put("XMJD", xmjd);
							map.put("RWJD", rwjd);
							map.put("SPZT", spzt);
							map.put("CUSTOMERNAME", customername);
							map.put("HTJE", htje);
							map.put("SSJE", ssje);
							map.put("WSJE", wsje);
							map.put("JDFZR", manager1);
							map.put("NUM", num);
							map.put("GROUPID", groupid);
							map.put("PJINSID", pjinsid);
							l.add(map);
						}
						return l;
					}
				});
	}

	public List getCloseProjectListSize1(boolean superman, String projectName,
			String xmjd, String startDate, String customername,String dgzt, String czbm, String cyrName, HashMap<String,List<String>> parameterMap) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();// + "["+ userModel.getUsername() + "]";
		List params=new ArrayList();
		final String userid = userModel.getUserid().toString().trim();
		Long ismanager = getIsManager(uc);
		boolean flag = false;
		if (uc != null) {
			Long orgRoleId = userModel.getOrgroleid();
			if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {
				flag = true;
			}
		}
		// 因为有分页，所以查询必须写到一块儿
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		String config = SystemConfig._xmlcConf.getConfig();
		Long orgRoleId = userModel.getOrgroleid();
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			map.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			map.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			map.put("createuserid", "USERID");
			map.put("userid", "USERID");
			map.put("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)", "USERID");
			map.put("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)", "USERID");
			map.put("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)", "USERID");
			parameter = getParameterMap(map,parameterMap);
		}
		StringBuffer sb = new StringBuffer(
				"SELECT COUNT(*) CNUM FROM (SELECT DISTINCT J.*,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD, E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj,g.pjr pjr FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
		if (!flag) {
			if(ismanager==1){
				sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sb.append(" )  or createuserid in ( ");

				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in (  ");
				
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(" ) union all select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sb.append(" ) union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=? ) a");
				params.add(gpfxFormId);
				sb.append( " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
				String[] userids =parameter.get("userid").split(",");
				for (int i = 0; i < userids.length; i++) {
					if(i==(userids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userids[i].replaceAll("'", ""));
				}
				sb.append(" ) union all");
				
				// if 按多项目组审批
				if (config.equals("2")) {
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					
				} else {
					sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
							+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" ))");
					
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}else{
				sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						// 任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ? "
						// 分派项目审批人
						+ " union all");
				params.add(owner);
				params.add(userid);
				params.add(owner);
				params.add(owner);
				params.add(gpfxFormId);
				params.add(userid);
				// if 按多项目组审批
				if (config.equals("2")) {
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				} else {
					sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
							+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? )");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sb.append(" AND CLBM like ? ");
			params.add("%" + czbm + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO ");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='已完成' AND A.XMJD <> '持续督导' ORDER BY A.ID DESC"
				+ " ) C) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
				+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
				+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
		params.add(gpfxFormId);
		if (!flag) {
			if(ismanager==1){
				sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sb.append(" )  or createuserid in ( ");
				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(" ) union all select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sb.append(" ) union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=?) a ");
				params.add(gpfxFormId);
				sb.append( " left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
						+ " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
				String[] userids =parameter.get("userid").split(",");
				for (int i = 0; i < userids.length; i++) {
					if(i==(userids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userids[i].replaceAll("'", ""));
				}
				sb.append(" )");
				
				sb.append(" union all");
				// if 按多项目组审批
				if (config.equals("2")) {
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ( select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					
				} else {
					sb.append(" select projectno from BD_ZQB_GPFXXMB WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" ))" + " ");
					
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}else{
				sb.append(" INNER JOIN ("
						// 项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) = ? or createuserid= ? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? "
						+ " union all"
						// 任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						// 项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
						+ " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ? ");
				params.add(owner);
				params.add(userid);
				params.add(owner);
				params.add(owner);
				params.add(gpfxFormId);
				params.add(userid);
				sb.append(" union all");
				// if 按多项目组审批
				if (config.equals("2")) {
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ( select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? " + " ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				} else {
					// else
					sb.append(" select projectno from BD_ZQB_GPFXXMB WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? )" + " ");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sb.append(" AND CLBM like ? ");
			params.add("%" + czbm + "%");
		}
		sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO ");

		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='已完成' AND A.XMJD <> '持续督导' ORDER BY A.ID DESC) C"
				+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid)");
		params.add(gpfxFormId);
		final String sql1 = sb.toString();
		final List param=params;
		return zqbGpfxProjectManageDAO.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql1);	
						for (int i = 0; i < param.size(); i++) {
							query.setParameter(i, param.get(i));
						}
						List<Integer> list = query.list();
						return list;
					}
				});
	}

	public String getGpfxfpbmSpContent(Long instanceid) {
		String taskid = "";
		String executionId = "";
		String actDefId ="";
		Long instanceId = 0L;
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		HashMap lcFromData = (HashMap) fromData.clone();
		String zbspzt=fromData.get("SPZT")==null?"":fromData.get("SPZT").toString();
		Long lcbs=fromData.get("LCBS")==null?0:Long.parseLong(fromData.get("LCBS").toString().equals("")?"0":fromData.get("LCBS").toString());
		Long lcInstanceId = 0L;
		if(lcbs==0&&zbspzt.equals("")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser getUserModel = uc.get_userModel();
			String username = getUserModel.getUsername();
			String userid = getUserModel.getUserid();
			actDefId= ProcessAPI.getInstance().getProcessActDefId("GPFXFPBM");
			lcFromData.put("CREATEUSER", username);
			lcFromData.put("CREATEUSERID", userid);
			Long formId = ProcessAPI.getInstance().getProcessDefaultFormId(actDefId);
			lcInstanceId = ProcessAPI.getInstance().newInstance(actDefId, formId, userid);
			ProcessAPI.getInstance().saveFormData(actDefId, lcInstanceId, lcFromData, false);// 保存流程
			String jd1 = SystemConfig._bmfzrspConf.getJd1();
			List<Task> tasklist = ProcessAPI.getInstance().getUserProcessTaskList(actDefId, jd1,userid);
			Task newTaskId = ProcessAPI.getInstance().newTaskId(lcInstanceId);
			executionId=newTaskId.getExecutionId();
			taskid=newTaskId.getId();
			fromData.put("SPZT", "未提交");
			fromData.put("LCBH", actDefId);
			fromData.put("LCBS", lcInstanceId);
			fromData.put("STEPID", jd1);
			fromData.put("TASKID", taskid);
			instanceId=Long.parseLong(executionId);
			Long dataid = Long.parseLong(fromData.get("ID").toString());
			String bgczuuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
			DemAPI.getInstance().updateFormData(bgczuuid, instanceid, fromData, dataid, false);
			
			String lcbh = fromData.get("LCBH").toString();
			Long pjlcbs=fromData.get("LCBS")==null?0:Long.parseLong(fromData.get("LCBS").toString().equals("")?"0":fromData.get("LCBS").toString());
			if(!pjlcbs.equals("")&&pjlcbs!=0){
				HashMap lcfromData = ProcessAPI.getInstance().getFromData(pjlcbs, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(lcfromData!=null){
					Long lcDataid = Long.parseLong(lcfromData.get("ID").toString());
					ProcessAPI.getInstance().updateFormData(lcbh, pjlcbs, lcfromData, lcDataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				}
			}
			
		}else{
			actDefId=fromData.get("SPZT")==null?"":fromData.get("SPZT").toString();
			actDefId=fromData.get("LCBH")==null?"":fromData.get("LCBH").toString();
			taskid=fromData.get("TASKID")==null?"":fromData.get("TASKID").toString();
			executionId=fromData.get("LCBS")==null?"0":fromData.get("LCBS").toString();
		}
		instanceId=Long.parseLong(executionId);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+instanceId+"}");
		return jsonHtml.toString();
	}
	
	public List<HashMap> getYxmcyrz(boolean superman, int pageNumberXM,
			int pageSizeXM) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		List<HashMap> returnList = new ArrayList<HashMap>();
		List<HashMap> returntempList = new ArrayList<HashMap>();
		final int startRow = (pageNumberXM - 1) * pageSizeXM;
		final int pageSize = pageSizeXM;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		Long ismanager = getIsManager(uc);
		String owner = userModel.getUserid();
		String username = userModel.getUsername();
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();
		// 判断是否是超权限用户
		List<HashMap<String, Object>> tmplist = null;
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		Long orgRoleId = userModel.getOrgroleid();
		HashMap<String,String> parematermap = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			parematermap.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			parematermap.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			parematermap.put("createuserid", "USERID");
			parematermap.put("a.name", "USERNAME");
			HashMap<String,List<String>> departmentUserList = this.getDepartmentUserList();
			parameter = getParameterMap(parematermap,departmentUserList);
		}
		// 拼写sql
		String sql;
		List params = new ArrayList();
		if (!superman) {
			if(ismanager==1){
				sql = "select * from (select projectname, instanceid, xmjd, projectno, owner name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "       and BINDTABLE.INSTANCEID is not null "
						+ "                                and BINDTABLE.formid =  ?  ";
				params.add(gpfxFormId);
				sql = sql +"    and status = '执行中' and SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ";
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sql = sql + ") "
						+ "union "
						+ "select projectname, instanceid, xmjd, projectno, manager name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "        and BINDTABLE.INSTANCEID is not null "
						+ "       and BINDTABLE.formid = ?    and status = '执行中' ";
				params.add(gpfxFormId);
				sql = sql+" and SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ";
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sql = sql + " ) "
						+ "union "
						+ "select projectname, instanceid, xmjd, projectno, createuser name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "        and BINDTABLE.INSTANCEID is not null "
						+ "       and BINDTABLE.formid = ? "; 
				params.add(gpfxFormId);
				sql = sql +"   and status = '执行中'  and createuserid in ( "; 
				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sql = sql+ " ) "
						+ "	union "
						+ "select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name "
						+ "  from BD_ZQB_GROUP a "
						+ " inner join SYS_ENGINE_FORM_BIND b  on formid = "
						+ "                                (select subformid "
						+ "                                  from sys_engine_subform t "
						+ "                                 where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) "
						+ "                            and metadataid = '105' "
						+ "                           and a.id = b.dataid "
						+ "                            and b.instanceid is not null and a.name in ( ";
				String[] name =parameter.get("a.name").split(",");
				for (int i = 0; i < name.length; i++) {
					if(i==(name.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					params.add(name[i].replaceAll("'", ""));
				}
				
				sql = sql +" ) ) a inner join "
						+ "(select a.projectno,a.xmjd,a.projectname,b.instanceid "
						+ "  from BD_ZQB_GPFXXMB a "
						+ " inner join SYS_ENGINE_FORM_BIND b "
						+ " on formid = ? "; 
				params.add(gpfxFormId);
				sql = sql +" and a.status='执行中' "
						+ "   and a.id = b.dataid "
						+ " and b.instanceid is not null)  b on a.instanceid=b.instanceid ) a order by name";
		
			}else{
				sql = "select * from (select projectname, instanceid, xmjd, projectno, owner name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "       and BINDTABLE.INSTANCEID is not null "
						+ "      and BINDTABLE.formid =  ?   and status = '执行中' and SUBSTR(owner,0, instr(owner,'[',1)-1)= ? "
						+ "union "
						+ "select projectname, instanceid, xmjd, projectno, manager name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "        and BINDTABLE.INSTANCEID is not null "
						+ "       and BINDTABLE.formid = ?    and status = '执行中'  and SUBSTR(manager,0, instr(manager,'[',1)-1)=? "
						+ "union "
						+ "select projectname, instanceid, xmjd, projectno, createuser name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "        and BINDTABLE.INSTANCEID is not null "
						+ "       and BINDTABLE.formid = ?    and status = '执行中'  and createuserid=? "
						+ "	union "
						+ "select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name "
						+ "  from BD_ZQB_GROUP a "
						+ " inner join SYS_ENGINE_FORM_BIND b  on formid = "
						+ "                                (select subformid "
						+ "                                  from sys_engine_subform t "
						+ "                                 where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) "
						+ "                            and metadataid = '105' "
						+ "                           and a.id = b.dataid "
						+ "                            and b.instanceid is not null and name=? ) a inner join "
						+ "(select a.projectno,a.xmjd,a.projectname,b.instanceid "
						+ "  from BD_ZQB_GPFXXMB a "
						+ " inner join SYS_ENGINE_FORM_BIND b "
						+ " on formid = ? and a.status='执行中' "
						+ "   and a.id = b.dataid "
						+ " and b.instanceid is not null)  b on a.instanceid=b.instanceid ) a order by name";
				params.add(gpfxFormId);
				params.add(owner);
				params.add(gpfxFormId);
				params.add(owner);
				params.add(gpfxFormId);
				params.add(owner);
				params.add(username);
				params.add(gpfxFormId);
			}
		} else {
			sql = "select * from (select projectname, instanceid, xmjd, projectno, owner name "
					+ "  from BD_ZQB_GPFXXMB BOTABLE "
					+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
					+ "       and BINDTABLE.INSTANCEID is not null "
					+ "                                and BINDTABLE.formid =?       and status = '执行中' "
					+ "union "
					+ "select projectname, instanceid, xmjd, projectno, manager name "
					+ "  from BD_ZQB_GPFXXMB BOTABLE "
					+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
					+ "        and BINDTABLE.INSTANCEID is not null "
					+ "       and BINDTABLE.formid = ?    and status = '执行中' "
					+ "	union "
					+ "select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name "
					+ "  from BD_ZQB_GROUP a "
					+ " inner join SYS_ENGINE_FORM_BIND b  on formid = "
					+ "                                (select subformid "
					+ "                                  from sys_engine_subform t "
					+ "                                 where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) "
					+ "                            and metadataid = '105' "
					+ "                           and a.id = b.dataid "
					+ "                            and b.instanceid is not null ) a inner join "
					+ "(select a.projectno,a.xmjd,a.projectname,b.instanceid "
					+ "  from BD_ZQB_GPFXXMB a "
					+ " inner join SYS_ENGINE_FORM_BIND b "
					+ " on formid =? and a.status='执行中' "
					+ "   and a.id = b.dataid "
					+ " and b.instanceid is not null)  b on a.instanceid=b.instanceid ) a order by name";
			params.add(gpfxFormId);
			params.add(gpfxFormId);
			params.add(gpfxFormId);
		}
		tmplist = zqbGpfxProjectManageDAO.getListCYR(startRow, pageSize, sql,params);
		HashMap<String, Object> map = null;
		HashMap<String, Object> map1 = null;
		boolean flag = false;
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		if (tmplist.size() > 0) {
			for (HashMap data : tmplist) {
				flag = false;
				map1 = new HashMap<String, Object>();
				HashMap condition = new HashMap();
				condition.put("PROJECTNO", data.get("PROJECTNO"));
				List<HashMap> l = DemAPI.getInstance().getList(ProjectItemUUID,
						condition, "ID desc");
				String updateDate = "";
				if (l.size() > 0 && l.get(0).get("GXSJ") != null) {
					try {
						updateDate = "".equals(l.get(0).get("GXSJ").toString())==true?"":sd.format(sd.parse(l.get(0).get("GXSJ")
								.toString()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				} else {
					updateDate = "";
				}
				for (HashMap<String, Object> obj : returntempList) {
					if (obj.get("NAME") != null
							&& obj.get("NAME").toString()
									.equals(data.get("NAME").toString())) {
						int i = Integer.parseInt(obj.get("NUM").toString()) + 1;
						obj.put("NUM", i);
						HashMap m = new HashMap();
						m.put("PROJECTNAME", data.get("PROJECTNAME"));
						m.put("XMJD", data.get("XMJD"));
						m.put("NAME", data.get("NAME"));
						m.put("GXSJ", updateDate);
						m.put("NUM", i);
						returntempList.add(m);
						flag = true;
						break;
					}
				}
				if (!flag) {
					map1.put("PROJECTNAME", data.get("PROJECTNAME"));
					map1.put("XMJD", data.get("XMJD"));
					map1.put("NAME", data.get("NAME"));
					map1.put("GXSJ", updateDate);
					map1.put("NUM", 1);
					returntempList.add(map1);
				}
			}
			HashMap<String, Object> keyname = null;
			List sublist = null;
		}
		return returntempList;
	}

	public int getTotalXmcyrNum(boolean superman) {
		return 0;
	}

	/**
	 * 以项目为基准统计导出
	 * 
	 * @return
	 */
	public void doExcelExpXM(HttpServletResponse response, boolean superman,
			int pageNumberXM, int pageSizeXM) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("以项目为基准统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("项目名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("项目最新进展");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("最后更新时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("参与人");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<Map<String, Object>> list = getXMExpList(superman, pageNumberXM,
				pageSizeXM);
		List<Map> l;
		List<Map<String, Object>> list1 = list;
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		for (Map<String, Object> item : list1) {
			if (item == null) {
				continue;
			}
			m++;
			String title = (String) item.get("PROJECTNAME");
			String xmjz = (String) item.get("XMJD");
			String gxsj = (String) item.get("GXSJ");
			String names = (String) item.get("NAMES");
			row = sheet.createRow((int) n++);
			// 第四步，创建单元格，并设置值
			HSSFCell cell1 = row.createCell((short) 0);
			cell1.setCellValue(title);
			cell1.setCellStyle(style1);
			HSSFCell cell2 = row.createCell((short) 1);
			cell2.setCellValue(xmjz);
			cell2.setCellStyle(style1);
			HSSFCell cell3 = row.createCell((short) 2);
			cell3.setCellValue(gxsj);
			cell3.setCellStyle(style1);
			HSSFCell cell4 = row.createCell((short) 3);
			cell4.setCellValue(names);
			cell4.setCellStyle(style1);
			short titleLength = (short) (title==null?0:title.length() * 256 * 3);
			short namesLength = (short) (names==null?0:names.length() * 256 * 3);
			short colLength = titleLength > namesLength ? titleLength
					: namesLength;
			if (sheet.getColumnWidth(0) < titleLength) {
				sheet.setColumnWidth(0, titleLength);
				sheet.setColumnWidth(1, titleLength);
				sheet.setColumnWidth(2, titleLength);
			}
			if (sheet.getColumnWidth(3) < namesLength) {
				sheet.setColumnWidth(3, namesLength);
			}
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("以项目为基准统计.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {

			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}

	}

	/**
	 * 以成员为基准统计导出
	 * 
	 * @return
	 */
	public void doExcelExpCY(HttpServletResponse response, boolean superman,
			int pageNumberXM, int pageSizeXM) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("以成员为基准统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("参与人名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("项目数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("项目名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("项目最新进展");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("最后更新时间");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = getYxmcyrz(superman, pageNumberXM, pageSizeXM);
		List<HashMap> l;
		List<HashMap> list1 = list;
		int n = 1;
		if (list == null) {
			return;
		}
		Map map = new HashMap();
		int m = 0;
		for (Map item : list1) {
			if (item == null) {
				continue;
			}
			m++;
			String name = (String) item.get("NAME");
			int num = Integer.parseInt(item.get("NUM").toString());
			String projectname = (String) item.get("PROJECTNAME");
			String xmjd = (String) item.get("XMJD");
			String gxsj = (String) item.get("GXSJ");
			row = sheet.createRow((int) n++);

			// 第四步，创建单元格，并设置值
			HSSFCell cell1 = row.createCell((short) 0);
			cell1.setCellValue(name);
			cell1.setCellStyle(style1);
			HSSFCell cell2 = row.createCell((short) 1);
			cell2.setCellValue(num);
			cell2.setCellStyle(style1);
			// 如何记录已显示人员的map里没有记录，或者不等于当前的用户
			if (map.get("NAME") == null
					|| !map.get("NAME").toString()
							.equals(item.get("NAME").toString())) {
				// 单元格合并
				// 四个参数分别是：起始行，起始列，结束行，结束列
				if (map.get("NAME") != null) {
					sheet.addMergedRegion(new Region(Integer.parseInt(map.get(
							"begin").toString()), (short) 0, n - 2, (short) 0));
					sheet.addMergedRegion(new Region(Integer.parseInt(map.get(
							"begin").toString()), (short) 1, n - 2, (short) 1));
				}
				map.put("NAME", (item.get("NAME") != null ? item.get("NAME")
						.toString() : ""));
				map.put("begin", n - 1);

			}
			HSSFCell cell3 = row.createCell((short) 2);
			cell3.setCellValue(projectname);
			cell3.setCellStyle(style1);
			HSSFCell cell4 = row.createCell((short) 3);
			cell4.setCellValue(xmjd);
			cell4.setCellStyle(style1);
			HSSFCell cell5 = row.createCell((short) 4);
			cell5.setCellValue(gxsj);
			cell5.setCellStyle(style1);
			short titleLength = (short) (name==null?0:name.length() * 256 * 2);
			short namesLength = (short) (projectname==null?0:projectname.length() * 256 * 2);
			short colLength = titleLength > namesLength ? titleLength
					: namesLength;
			if (sheet.getColumnWidth(m) < colLength) {
				sheet.setColumnWidth(m, colLength);
			}
		}
		if (map.size() != 0) {
			sheet.addMergedRegion(new Region(Integer.parseInt(map.get("begin")
					.toString()), (short) 0, n - 1, (short) 0));
			sheet.addMergedRegion(new Region(Integer.parseInt(map.get("begin")
					.toString()), (short) 1, n - 1, (short) 1));
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("以成员为基准统计.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {

			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}

	}

	public List getXMListSize(boolean superman){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();
		Long ismanager = getIsManager(uc);
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List params=new ArrayList();
		List<HashMap> list = new ArrayList();
		// 判断是否是超权限用户
		List<HashMap<String, Object>> tmplist = null;
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		Long orgRoleId = userModel.getOrgroleid();
		HashMap<String,String> parematermap = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			parematermap.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			parematermap.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			parematermap.put("createuser", "USERID");
			parematermap.put("a.name", "USERNAME");
			HashMap<String,List<String>> departmentUserList = this.getDepartmentUserList();
			parameter = getParameterMap(parematermap,departmentUserList);
		}
		// 拼写sql
		String sql;
		if (!superman) {
			if(ismanager==1){
				sql = "select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_GPFXXMB"
						+ " BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "  and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = ? ";
				params.add(gpfxFormId);
				sql = sql +" and status='执行中' "; 
				params.add(gpfxFormId);
				sql = sql +" and (SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ";
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sql = sql +" ) or "
						+ " SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ";
				
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sql = sql + " ))"
						+ " union "
						+ " select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager "
						+ "  from (select b.instanceid, a.name "
						+ "    from BD_ZQB_GROUP a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid = "
						+ "       (select subformid "
						+ "                           from sys_engine_subform t "
						+ "                        where subtablekey = "
						+ "                              'SUBFORM_XMCYLB' and rownum = 1) "
						+ "                  and metadataid = '105' "
						+ "                 and a.id = b.dataid "
						+ "                 and b.instanceid is not null and a.name in ( ";
				String[] name =parameter.get("a.name").split(",");
				for (int i = 0; i < name.length; i++) {
					if(i==(name.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					params.add(name[i].replaceAll("'", ""));
				}
				sql = sql + " )) a"
						+ " inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager "
						+ "   from BD_ZQB_GPFXXMB a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid = ? and a.status='执行中' "
						+ "                         and a.id = b.dataid "
						+ "                        and b.instanceid is not null) b on a.instanceid = "
						+ "               b.instanceid ";
				params.add(gpfxFormId);
			}else{
				sql = "select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_GPFXXMB"
						+ " BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "  and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = ? and status='执行中'  and (SUBSTR(owner,0, instr(owner,'[',1)-1)= ? or "
						+ " SUBSTR(manager,0, instr(manager,'[',1)-1)=?  )"
						+ " union "
						+ " select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager "
						+ "  from (select b.instanceid, a.name "
						+ "    from BD_ZQB_GROUP a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid = "
						+ "       (select subformid "
						+ "                           from sys_engine_subform t "
						+ "                        where subtablekey = "
						+ "                              'SUBFORM_XMCYLB' and rownum = 1) "
						+ "                  and metadataid = '105' "
						+ "                 and a.id = b.dataid "
						+ "                 and b.instanceid is not null and a.name=? ) a"
						+ " inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager "
						+ "   from BD_ZQB_GPFXXMB a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid =? and a.status='执行中'  and a.id = b.dataid  and b.instanceid is not null) b on a.instanceid =     b.instanceid ";
				params.add(gpfxFormId);
				params.add(owner);
				params.add(owner);
				params.add(userModel.getUsername());
				params.add(gpfxFormId);
			}
		} else {
			sql = "select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_GPFXXMB "
					+ " BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
					+ "  and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = ?  and status='执行中'  ";
			params.add(gpfxFormId);
		}
		tmplist = zqbGpfxProjectManageDAO.getListSize(sql,params);
		return tmplist;
	}
	public List<Map<String, Object>> getXMList(boolean superman,
			int pageNumberXM, int pageSizeXM) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		final int startRow = (pageNumberXM - 1) * pageSizeXM;
		final int pageSize = pageSizeXM;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		Long ismanager = getIsManager(uc);
		List param = new ArrayList();
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();
		// 判断是否是超权限用户
		List<HashMap<String, Object>> tmplist = null;
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		Long orgRoleId = userModel.getOrgroleid();
		HashMap<String,String> parematermap = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			parematermap.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			parematermap.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			parematermap.put("createuser", "USERID");
			parematermap.put("a.name", "USERNAME");
			HashMap<String,List<String>> departmentUserList = this.getDepartmentUserList();
			parameter = getParameterMap(parematermap,departmentUserList);
		}
		// 拼写sql
		String sql;
		if (!superman) {
			if(ismanager==1){
				sql = "select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_GPFXXMB"
						+ " BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "  and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid =? and status='执行中' "; 
				param.add(gpfxFormId);
				sql = sql +"  and (SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ";
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					param.add(owners[i].replaceAll("'", ""));
				}
				sql = sql +" ) or "
						+ " SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ";
				
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					param.add(manager[i].replaceAll("'", ""));
				}
				sql = sql + " ))"
						+ " union "
						+ " select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager "
						+ "  from (select b.instanceid, a.name "
						+ "    from BD_ZQB_GROUP a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid = "
						+ "       (select subformid "
						+ "                           from sys_engine_subform t "
						+ "                        where subtablekey = "
						+ "                              'SUBFORM_XMCYLB' and rownum = 1) "
						+ "                  and metadataid = '105' "
						+ "                 and a.id = b.dataid "
						+ "                 and b.instanceid is not null and a.name in ( ";
				String[] name =parameter.get("a.name").split(",");
				for (int i = 0; i < name.length; i++) {
					if(i==(name.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					param.add(name[i].replaceAll("'", ""));
				}
				sql = sql + " )) a"
						+ " inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager "
						+ "   from BD_ZQB_GPFXXMB a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid = ?  and a.status='执行中'  and a.id = b.dataid  and b.instanceid is not null) b on a.instanceid =  b.instanceid ";
				
				
				param.add(gpfxFormId);
			}else{
				sql = "select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_GPFXXMB"
						+ " BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "  and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = ? and status='执行中'  and (SUBSTR(owner,0, instr(owner,'[',1)-1)= ? or "
						+ " SUBSTR(manager,0, instr(manager,'[',1)-1)=?  )"
						+ " union "
						+ " select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager "
						+ "  from (select b.instanceid, a.name "
						+ "    from BD_ZQB_GROUP a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid = "
						+ "       (select subformid "
						+ "                           from sys_engine_subform t "
						+ "                        where subtablekey = "
						+ "                              'SUBFORM_XMCYLB' and rownum = 1) "
						+ "                  and metadataid = '105' "
						+ "                 and a.id = b.dataid "
						+ "                 and b.instanceid is not null and a.name= ? ) a"
						+ " inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager "
						+ "   from BD_ZQB_GPFXXMB a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid = ? and a.status='执行中'   and a.id = b.dataid     and b.instanceid is not null) b on a.instanceid =   b.instanceid ";
				param.add(gpfxFormId);
				param.add(owner);
				param.add(owner);
				param.add(userModel.getUsername());
				param.add(gpfxFormId);
			}
		} else {
			sql = "select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_GPFXXMB "
					+ " BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
					+ "  and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = ?  and status='执行中'  ";
			param.add(gpfxFormId);
		}
		tmplist = zqbGpfxProjectManageDAO.getList(startRow, pageSize, sql,param);
		HashMap<String, Object> map = null;
		if (tmplist.size() > 0) {
			for (HashMap data : tmplist) {
				map = new HashMap<String, Object>();
				List<HashMap> xmcyList = DemAPI.getInstance().getFromSubData(
						Long.parseLong(data.get("INSTANCEID").toString()),
						"SUBFORM_XMCYLB");
				if(xmcyList==null){
					xmcyList= new ArrayList<HashMap>();
				}
				String own = data.get("OWNER") == null ? ""
						: "<a href=\"javascript:editUser('"
								+ data.get("OWNER").toString() + "')\">"
								+ data.get("OWNER").toString() + "</a>";
				/* <a href="javascript:editUser('')"></a> */
				String manager = data.get("MANAGER") == null ? ""
						: "<a href=\"javascript:editUser('"
								+ data.get("MANAGER").toString() + "')\">"
								+ data.get("MANAGER").toString() + "</a>";
				String names = "";
				List<HashMap> xmcytemp = new ArrayList<HashMap>();
				if (own.equals(manager)) {
					HashMap xmcyb = new HashMap();
					xmcyb.put("NAME", xmcyb);
					xmcytemp.add(xmcyb);
					names = (own + ",");
				} else {
					HashMap xmcyb = new HashMap();
					xmcyb.put("NAME", own);
					xmcytemp.add(xmcyb);
					xmcyb = new HashMap();
					xmcyb.put("NAME", manager);
					xmcytemp.add(xmcyb);
					names = own + "," + manager + ",";
				}
				for (HashMap xmcy : xmcyList) {
					HashMap xmcyb = new HashMap();
					xmcyb.put(
							"NAME",
							"<a href=\"javascript:editUser('"
									+ xmcy.get("USERID") + "["
									+ xmcy.get("NAME") + "]" + "')\">"
									+ xmcy.get("USERID") + "["
									+ xmcy.get("NAME") + "]" + "</a>");
					if (!xmcytemp.contains(xmcyb)) {
						xmcytemp.add(xmcyb);
						names += "<a href=\"javascript:editUser('"
								+ xmcy.get("USERID") + "[" + xmcy.get("NAME")
								+ "]" + "')\">" + xmcy.get("USERID") + "["
								+ xmcy.get("NAME") + "]" + "</a>" + ",";
					}
				}
				HashMap condition = new HashMap();
				condition.put("PROJECTNO", data.get("PROJECTNO"));
				String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
				List<HashMap> l = DemAPI.getInstance().getList(ProjectItemUUID,
						condition, "ID desc");
				String updateDate = "";
				if (l.size() > 0 && l.get(0).get("GXSJ") != null) {
					try {
						updateDate = "".equals(l.get(0).get("GXSJ").toString())?"":sd.format(sd.parse(l.get(0).get("GXSJ").toString()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				} else {
					updateDate = "";
				}

				names = !names.equals("") ? names.substring(0,
						names.length() - 1) : names;
				map.put("PROJECTNAME", data.get("PROJECTNAME"));
				map.put("XMJD", data.get("XMJD"));
				map.put("NAMES", names);
				map.put("GXSJ", updateDate);
				returnList.add(map);
			}

		}
		return returnList;
	}

	public List<Map<String, Object>> getXMExpList(boolean superman,
			int pageNumberXM, int pageSizeXM) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		final int startRow = (pageNumberXM - 1) * pageSizeXM;
		final int pageSize = pageSizeXM;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		Long ismanager = getIsManager(uc);
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();
		// 判断是否是超权限用户
		List<HashMap<String, Object>> tmplist = null;
		// 拼写sql
		Long gpfxXMFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		Long gpfxXMDemId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "ID");
		Long orgRoleId = userModel.getOrgroleid();
		HashMap<String,String> parematermap = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			parematermap.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			parematermap.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			parematermap.put("a.name", "USERNAME");
			HashMap<String,List<String>> departmentUserList = this.getDepartmentUserList();
			parameter = getParameterMap(parematermap,departmentUserList);
		}
		List param=new ArrayList();
		String sql;
		if (!superman) {
			if(ismanager==1){
				sql = "select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_GPFXXMB"
						+ " BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "  and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = ?  and status='执行中' ";
				param.add(gpfxXMFormId);
				sql = sql+"  and (SUBSTR(owner,0, instr(owner,'[',1)-1) in( ";
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					param.add(owners[i].replaceAll("'", ""));
				}
				sql = sql +" ) or "
						+ " SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ";
				
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					param.add(manager[i].replaceAll("'", ""));
				}
				sql = sql + " ))"
						+ " union "
						+ " select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager "
						+ "  from (select b.instanceid, a.name "
						+ "    from BD_ZQB_GROUP a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid = "
						+ "       (select subformid "
						+ "                           from sys_engine_subform t "
						+ "                        where subtablekey = "
						+ "                              'SUBFORM_XMCYLB' and rownum = 1) "
						+ "                  and metadataid = '105' "
						+ "                 and a.id = b.dataid "
						+ "                 and b.instanceid is not null and a.name in ( ";
				String[] name =parameter.get("a.name").split(",");
				for (int i = 0; i < name.length; i++) {
					if(i==(name.length-1)){
						sql = sql + " ? ";
					}else{
						sql = sql + " ?, ";
					}
					param.add(name[i].replaceAll("'", ""));
				}
				sql = sql + " )) a"
						+ " inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager "
						+ "   from BD_ZQB_GPFXXMB a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid = ? and a.status='执行中'  and a.id = b.dataid  and b.instanceid is not null) b on a.instanceid =  b.instanceid ";
				
			
				param.add(gpfxXMFormId);
			}else{
				sql = "select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_GPFXXMB"
						+ " BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "  and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = ? and and status='执行中'  and (SUBSTR(owner,0, instr(owner,'[',1)-1)= ? or "
						+ " SUBSTR(manager,0, instr(manager,'[',1)-1)= ?  )"
						+ " union "
						+ " select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager "
						+ "  from (select b.instanceid, a.name "
						+ "    from BD_ZQB_GROUP a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid = "
						+ "       (select subformid "
						+ "                           from sys_engine_subform t "
						+ "                        where subtablekey = "
						+ "                              'SUBFORM_XMCYLB' and rownum = 1) "
						+ "                  and metadataid = '105' "
						+ "                 and a.id = b.dataid "
						+ "                 and b.instanceid is not null and a.name= ? ) a"
						+ " inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager "
						+ "   from BD_ZQB_GPFXXMB a "
						+ "  inner join SYS_ENGINE_FORM_BIND b on formid = ? and a.status='执行中'  and a.id = b.dataid and b.instanceid is not null) b on a.instanceid =  b.instanceid ";
				param.add(gpfxXMFormId);
				param.add(owner);
				param.add(owner);
				param.add(userModel.getUsername());
				param.add(gpfxXMFormId);
			}
		} else {
			sql = "select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_GPFXXMB "
					+ " BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
					+ "  and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid =?  and status='执行中'  ";
			param.add(gpfxXMFormId);
		}
		tmplist = zqbGpfxProjectManageDAO.getList(startRow, pageSize, sql,param);
		HashMap<String, Object> map = null;
		if (tmplist.size() > 0) {
			for (HashMap data : tmplist) {
				map = new HashMap<String, Object>();
				List<HashMap> xmcyList = DemAPI.getInstance().getFromSubData(
						Long.parseLong(data.get("INSTANCEID").toString()),
						"SUBFORM_XMCYLB");
				if(xmcyList==null){
					xmcyList= new ArrayList<HashMap>();
				}
				String own = data.get("OWNER") == null ? "" : data.get("OWNER")
						.toString();
				String manager = data.get("MANAGER") == null ? "" : data.get(
						"MANAGER").toString();
				String names = "";
				List<HashMap> xmcytemp = new ArrayList<HashMap>();
				if (own.equals(manager)) {
					HashMap xmcyb = new HashMap();
					xmcyb.put("NAME", xmcyb);
					xmcytemp.add(xmcyb);
					names = (own + ",");
				} else {
					HashMap xmcyb = new HashMap();
					xmcyb.put("NAME", own);
					xmcytemp.add(xmcyb);
					xmcyb = new HashMap();
					xmcyb.put("NAME", manager);
					xmcytemp.add(xmcyb);
					names = own + "," + manager + ",";
				}
				for (HashMap xmcy : xmcyList) {
					HashMap xmcyb = new HashMap();
					xmcyb.put("NAME",
							xmcy.get("USERID") + "[" + xmcy.get("NAME") + "]");
					if (!xmcytemp.contains(xmcyb)) {
						xmcytemp.add(xmcyb);
						names += xmcy.get("USERID") + "[" + xmcy.get("NAME")
								+ "]" + ",";
					}
				}
				HashMap condition = new HashMap();
				condition.put("PROJECTNO", data.get("PROJECTNO"));
				String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
				List<HashMap> l = DemAPI.getInstance().getList(ProjectItemUUID,
						condition, "ID desc");
				String updateDate = "";
				if (l.size() > 0 && l.get(0).get("GXSJ") != null) {
					try {
						updateDate = "".equals(l.get(0).get("GXSJ").toString())==true?"":sd.format(sd.parse(l.get(0).get("GXSJ")
								.toString()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				} else {
					updateDate = "";
				}

				names = !names.equals("") ? names.substring(0,
						names.length() - 1) : names;
				map.put("PROJECTNAME", data.get("PROJECTNAME"));
				map.put("XMJD", data.get("XMJD"));
				map.put("NAMES", names);
				map.put("GXSJ", updateDate);
				returnList.add(map);
			}

		}
		return returnList;
	}

	/**
	 * 按项目统计总条数
	 * 
	 * @param superman
	 * @return
	 */
	public int getTotalCYNum(boolean superman) {
		List<HashMap> returnList = new ArrayList<HashMap>();
		List<HashMap> returntempList = new ArrayList<HashMap>();
		HashMap conditionMap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();
		String username = userModel.getUsername();
		Long ismanager = getIsManager(uc);
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();
		// 判断是否是超权限用户
		conditionMap.put("STATUS", "执行中");
		List<HashMap<String, Object>> tmplist = null;
		// 拼写sql
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		Long orgRoleId = userModel.getOrgroleid();
		HashMap<String,String> parematermap = new HashMap<String,String>();
		HashMap<String,Object> strmap = new HashMap<String,Object>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			parematermap.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			parematermap.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			parematermap.put("createuserid", "USERID");
			parematermap.put("name", "USERNAME");
			HashMap<String,List<String>> departmentUserList = this.getDepartmentUserList();
			parameter = getParameterMap(parematermap,departmentUserList);
		}
		// 拼写sql
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		if (!superman) {
			if(ismanager==1){
				sql.append("select COUNT(*) NUM from (select projectname, instanceid, xmjd, projectno, owner name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "       and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = ?  and status = '执行中' ");
				params.add(gpfxFormId);
				sql.append(" and SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sql.append(" ) "
						+ "union "
						+ "select projectname, instanceid, xmjd, projectno, manager name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "        and BINDTABLE.INSTANCEID is not null "
						+ "       and BINDTABLE.formid = ?    and status = '执行中' ");
				params.add(gpfxFormId);
				sql.append("  and SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sql.append(" ) "
						+ "union "
						+ "select projectname, instanceid, xmjd, projectno, createuser name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "        and BINDTABLE.INSTANCEID is not null  and BINDTABLE.formid = ?  and status = '执行中' ");
				params.add(gpfxFormId);
				sql.append("  and createuserid in ( ");
				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sql.append(" ) "
						+ "	union "
						+ "select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name "
						+ "  from BD_ZQB_GROUP a "
						+ " inner join SYS_ENGINE_FORM_BIND b  on formid = "
						+ "                                (select subformid "
						+ "                                  from sys_engine_subform t "
						+ "                                 where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) "
						+ "                            and metadataid = '105' "
						+ "                           and a.id = b.dataid "
						+ "                            and b.instanceid is not null and name in ( ");
				String[] name =parameter.get("name").split(",");
				for (int i = 0; i < name.length; i++) {
					if(i==(name.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(name[i].replaceAll("'", ""));
				}
				sql.append(" ) ) a inner join "
						+ "(select a.projectno,a.xmjd,a.projectname,b.instanceid "
						+ "  from BD_ZQB_GPFXXMB a "
						+ " inner join SYS_ENGINE_FORM_BIND b "
						+ " on formid = ? and a.status='执行中' "
						+ "   and a.id = b.dataid "
						+ " and b.instanceid is not null)  b on a.instanceid=b.instanceid) a order by name");
				
				
			}else{
				sql.append("select COUNT(*) NUM from (select projectname, instanceid, xmjd, projectno, owner name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "       and BINDTABLE.INSTANCEID is not null "
						+ "   and BINDTABLE.formid = ? and status = '执行中' and SUBSTR(owner,0, instr(owner,'[',1)-1)= ? "
						+ "union "
						+ "select projectname, instanceid, xmjd, projectno, manager name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "        and BINDTABLE.INSTANCEID is not null "
						+ "       and BINDTABLE.formid = ?    and status = '执行中'  and SUBSTR(manager,0, instr(manager,'[',1)-1)= ? "
						+ "union "
						+ "select projectname, instanceid, xmjd, projectno, createuser name "
						+ "  from BD_ZQB_GPFXXMB BOTABLE "
						+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
						+ "        and BINDTABLE.INSTANCEID is not null "
						+ "       and BINDTABLE.formid = ?    and status = '执行中'  and createuserid= ? "
						+ "	union "
						+ "select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name "
						+ "  from BD_ZQB_GROUP a "
						+ " inner join SYS_ENGINE_FORM_BIND b  on formid = "
						+ "                                (select subformid "
						+ "                                  from sys_engine_subform t "
						+ "                                 where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) "
						+ "                            and metadataid = '105' "
						+ "                           and a.id = b.dataid "
						+ "                            and b.instanceid is not null and name= ? ) a inner join "
						+ "(select a.projectno,a.xmjd,a.projectname,b.instanceid "
						+ "  from BD_ZQB_GPFXXMB a "
						+ " inner join SYS_ENGINE_FORM_BIND b "
						+ " on formid = ? and a.status='执行中' "
						+ "   and a.id = b.dataid "
						+ " and b.instanceid is not null)  b on a.instanceid=b.instanceid) a order by name");
				params.add(gpfxFormId);
				params.add(owner);
				params.add(gpfxFormId);
				params.add(owner);
				params.add(gpfxFormId);
				params.add(owner);
				params.add(username);
				params.add(gpfxFormId);
			}
		} else {
			sql.append("select COUNT(*) NUM from (select projectname, instanceid, xmjd, projectno, owner name "
					+ "  from BD_ZQB_GPFXXMB BOTABLE "
					+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
					+ "       and BINDTABLE.INSTANCEID is not null "
					+ "   and BINDTABLE.formid = ?  and status = '执行中' "
					+ "union "
					+ "select projectname, instanceid, xmjd, projectno, manager name "
					+ "  from BD_ZQB_GPFXXMB BOTABLE "
					+ " inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid "
					+ "        and BINDTABLE.INSTANCEID is not null "
					+ "       and BINDTABLE.formid = ?    and status = '执行中' "
					+ "	union "
					+ "select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name "
					+ "  from BD_ZQB_GROUP a "
					+ " inner join SYS_ENGINE_FORM_BIND b  on formid = "
					+ "                                (select subformid "
					+ "                                  from sys_engine_subform t "
					+ "                                 where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) "
					+ "                            and metadataid = '105' "
					+ "                           and a.id = b.dataid "
					+ "                            and b.instanceid is not null ) a inner join "
					+ "(select a.projectno,a.xmjd,a.projectname,b.instanceid "
					+ "  from BD_ZQB_GPFXXMB a "
					+ " inner join SYS_ENGINE_FORM_BIND b "
					+ " on formid = ? and a.status='执行中' "
					+ "   and a.id = b.dataid "
					+ " and b.instanceid is not null)  b on a.instanceid=b.instanceid ) a order by name");
			params.add(gpfxFormId);
			params.add(gpfxFormId);
			params.add(gpfxFormId);
		}
		int n = 0;
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			if (rs.next()) {
				n = rs.getInt("NUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}

		return n;
	}

	public boolean removeProject(String projectNo, Long instanceId) {
		// 删除子表信息
		boolean flag = DemAPI.getInstance().removeFormData(instanceId);// 删除项目基本信息
		// 以下为删除任务信息
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		List<HashMap> list = DemAPI.getInstance().getList(ProjectItemUUID,
				conditionMap, null);
		if (list.size() > 0) {
			for (HashMap itemMap : list) {
				Long instanceid = Long.parseLong(itemMap.get("INSTANCEID")
						.toString());
				flag = flag && DemAPI.getInstance().removeFormData(instanceid);
			}
		}
		return flag;
	}

	/**
	 * 删除持续督导项目
	 * 
	 * @param projectNo
	 * @param instanceid
	 * @return
	 */
	public String removeCxddProject(String projectNo, Long instanceId) {
		HashMap pjmap = DemAPI.getInstance().getFromData(instanceId,
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String customerno = pjmap.get("CUSTOMERNO").toString();

		// 删除持续督导分派信息
		// 查询是否已进行过持续督导分派
		HashMap condition = new HashMap();
		condition.put("KHBH", customerno);
		List<HashMap> l = DemAPI.getInstance().getList(
				ZQB_CUSTOMER_PURVIEW_UUID, condition, null);
		if (l.size() > 0) {
			if (l.size() == 1) {
				if (l.get(0).get("KHFZR") != null
						&& !l.get(0).get("KHFZR").equals("")) {
					return "已经分派过持续督导专员，无法删除！";
				} else {
					Long instance = Long.parseLong(l.get(0).get("INSTANCEID")
							.toString());
					boolean isdel = DemAPI.getInstance().removeFormData(
							instance);
				}
			} else {
				return "存在多条持续督导分派记录！";
			}
		}
		String username = DBUtil.getString("select USERNAME from OrgUser where departmentid=(select  id from orgdepartment where departmentno='"+ customerno + "')", "USERNAME");
		// 已存在用户则不可删除，否则删除
		if (username != null && !username.equals("")) {
			return "该项目所关联的客户下已存在用户，不可删除！";
		} else {
			String sql = "delete from  orgdepartment where departmentno='"+ customerno + "'";
			int i = DBUtil.executeUpdate(sql);
		}
		// 删除子表信息
		boolean flag = DemAPI.getInstance().removeFormData(instanceId);// 删除项目基本信息
		// 以下为删除任务信息
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		List<HashMap> list = DemAPI.getInstance().getList(ProjectItemUUID,
				conditionMap, null);
		if (list.size() > 0) {
			for (HashMap itemMap : list) {
				Long instanceid = Long.parseLong(itemMap.get("INSTANCEID")
						.toString());
				flag = flag && DemAPI.getInstance().removeFormData(instanceid);
			}
		}
		if (flag) {
			return "success";
		} else {
			return "删除失败";
		}
	}

	public String loadProject(String projectno) {
		StringBuffer sql = new StringBuffer(
				"SELECT A.INSTANCEID,a.projectno,nvl(A.LCBS,'') LCBS, "
						+ "nvl(a.lcbh,'') lcbh, nvl(A.STEPID,'') STEPID,nvl(a.yxid,'') yxid,nvl(a.prcid,'') prcid,"
						+ " nvl(A.RWID,'') RWID, nvl(b.jdmc,'') TASK_NAME,nvl(A.GXR,'') GXR, nvl(to_char(A.GXSJ,'yyyy-MM-dd hh24:mi'),'') GXSJ,nvl(A.SPZT,'') SPZT,"
						+ " NVL(A.SSJE,'') SSJE,nvl(a.manager,'') manager,mbNum,B.ID JDBH"
						+ " ,mbName,mb,d.jdzl,ZLName"
						+ " FROM BD_ZQB_KM_INFO B  LEFT JOIN"
						+ " (SELECT * FROM BD_PM_TASK BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid"
						+ " and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 94 and BINDTABLE.metadataid = 107 WHERE projectno=?) A"
						+ " ON B.ID = A.GROUPID"
						+ " left join (select jdbh,count(*) mbNum from BD_ZQB_XMZLB group by jdbh) C"
						+ " on B.ID = c.jdbh"
						+ " LEFT JOIN  (select a.id,a.jdbh,d.FILE_SRC_NAME mbName,a.jdzl mb,b.jdzl,c.FILE_SRC_NAME ZLName"
						+ " from BD_ZQB_XMZLB a left join (select * from BD_ZQB_XMRWZLB where projectno=?) b"
						+ " on  a.jdbh = b.jdbh and a.jdzl = b.sxzl "
						+ " left join sys_upload_file c on b.jdzl = c.file_id"
						+ " left join sys_upload_file d on a.jdzl = d.file_id ) d"
						+ " ON B.ID = D.JDBH" + " ORDER BY b.ID,mb");
		StringBuffer sqlZL = new StringBuffer();
		sqlZL.append("select a.id,a.jdbh,d.FILE_SRC_NAME mbName,a.jdzl mb,b.jdzl,c.FILE_SRC_NAME ZLName from BD_ZQB_XMZLB a left join (select * from BD_ZQB_XMRWZLB where projectno=?) b on  a.jdbh = b.jdbh and a.jdzl = b.sxzl left join sys_upload_file c on b.jdzl = c.file_id left join sys_upload_file d on a.jdzl = d.file_id order by a.jdbh,a.jdzl");
		StringBuffer content = new StringBuffer();
		content.append("<table class=\"ui-jqgrid-htable ke-zeroborder\" role=\"grid\""
				+ " aria- labelledby=\"gbox_subformSUBFORM_BGSRZXX\"           "
				+ " style=\"width:850px;\" border=\"0\" cellpadding=\"0\"      "
				+ " cellspacing=\"0\">"
				+ " <thead>           "
				+ " 	<tr class=\"ui-jqgrid-labels\" role=\"rowheader\">       "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_rn\" role=\"columnheader\"             "
				+ " 			style=\"width:15px;\">                               "
				+ " 			<div id=\"jqgh_subformSUBFORM_BGSRZXX_rn\">          "
				+ " 				<span class=\"s-ico\" style=\"display:none;\"><span"
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\"      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div> <br />                                        "
				+ " 		</th>         "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZLX\" role=\"columnheader\"           "
				+ " 			style=\"width:50px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZLX\">           "
				+ " 				阶段名称<span class=\"s-ico\" style=\"display:none;\"><span      "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_ZW\" role=\"columnheader\"             "
				+ " 			style=\"width:50px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_ZW\">             "
				+ " 				阶段负责人<span class=\"s-ico\" style=\"display:none;\"><span    "
				+ " 				class=\"ui-grid-ico-sort ui-icon-asc ui-state-     "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_ZW\" role=\"columnheader\"             "
				+ " 			style=\"width:50px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_ZW\">             "
				+ " 				实收金额(万元)<span class=\"s-ico\" style=\"display:none;\"><span    "
				+ " 				class=\"ui-grid-ico-sort ui-icon-asc ui-state-     "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZQSRQ\" role=\"columnheader\"         "
				+ " 			style=\"width:100px;\"><span                         "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZQSRQ\">         "
				+ " 				所需资料模板<span class=\"s-ico\" style=\"display:none;\"><span  "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "

				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZZT\" role=\"columnheader\"           "
				+ " 			style=\"width:100px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZZT\">           "
				+ " 				上传资料<span class=\"s-ico\" style=\"display:none;\"><span        "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_LRQX\" role=\"columnheader\"           "
				+ " 			style=\"width:40px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_LRQX\">           "
				+ " 				状态<span class=\"s-ico\" style=\"display:none;\"><span          "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "                         "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZJSRQ\" role=\"columnheader\"         "
				+ " 			style=\"width:40px;\"><span                         "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZJSRQ\">         "
				+ " 				操作<span class=\"s-ico\" style=\"display:none;\"><span          "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 	</tr>           "
				+ " </thead>          " + "										</table>            ");
		content.append("<div style=\"height: 100%; width: 850px;\" class=\"ui-jqgrid-bdiv\">     "
				+ "           <div style=\"position:relative;\"><div>                       "
				+ "</div>            "
				+ "<table style=\"width: 850px;\" class=\"ui-jqgrid-btable\" aria-labelledby=\"gbox_subformSUBFORM_SXCYRY\" aria-multiselectable=\"true\"        "
				+ "                  "
				+ "role=\"grid\" tabindex=\"1\" id=\"subformSUBFORM_SXCYRY\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
				+ "<tbody>           ");
		XmlcConf conf = SystemConfig._xmlcConf;
		String config = conf.getConfig();
		// 如果配置为1，则审批节点为初审负责人-》复审负责人-》领导审批
		// 如果配置为2，则审批节点为项目负责人-》质控部负责人1-》质控部负责人2
		String pro = config.equals("1") ? "XMRWLCX" : "DXMBXMRWLCX";
		String xmlcServer = ProcessAPI.getInstance().getProcessActDefId(pro);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rsZL = null;
		HashMap hashZLNum = new HashMap();
		List listMB = new ArrayList();
		List listJD = new ArrayList();
		try {
			stmt =conn.prepareStatement(sqlZL.toString());
			stmt.setString(1, projectno);
			rsZL = stmt.executeQuery();
			int i = 0;
			if (rsZL != null) {
				while (rsZL.next()) {
					String jdbh = rsZL.getString("jdbh") == null ? "" : rsZL
							.getString("jdbh");
					String mb = rsZL.getString("mb") == null ? "" : rsZL
							.getString("mb");
					// 循环查出每个模板的资料数量
					if (hashZLNum.containsKey(jdbh + "_" + mb)) {
						hashZLNum.put(jdbh + "_" + mb, Integer
								.parseInt(hashZLNum.get(jdbh + "_" + mb)
										.toString()) + 1);
					} else {
						hashZLNum.put(jdbh + "_" + mb, 1);
					}
				}
			}
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, projectno);
			stmt.setString(2, projectno);
			ResultSet rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					int jdbh = rs.getInt("JDBH");
					int mbNum = rs.getInt("MBNUM");
					int num = 0;
					if (rsZL != null) {
						if (!hashZLNum.isEmpty()) {
							Iterator iter = hashZLNum.entrySet().iterator();
							while (iter.hasNext()) {
								Map.Entry entry = (Map.Entry) iter.next();
								String strKey = entry.getKey().toString();
								if (strKey.indexOf(jdbh + "_") < 0) {
									continue;
								}
								int iZLNum = Integer.parseInt(entry.getValue()
										.toString());
								num += iZLNum;
							}
						}
					}
					if (num == 0) {
						num = mbNum;
					}
					Long instanceid = rs.getLong("INSTANCEID");
					String lcbs = rs.getString("LCBS") == null ? "" : rs
							.getString("LCBS");
					String stepid = rs.getString("STEPID") == null ? "" : rs
							.getString("STEPID");
					String rwid = rs.getString("RWID") == null ? "" : rs
							.getString("RWID");
					String taskname = rs.getString("TASK_NAME") == null ? ""
							: rs.getString("TASK_NAME");
					String manager = rs.getString("MANAGER") == null ? "" : rs
							.getString("MANAGER");
					String gxr = rs.getString("GXR") == null ? "" : rs
							.getString("GXR");
					String gxsj = rs.getString("GXSJ") == null ? "" : rs
							.getString("GXSJ");
					String spzt = rs.getString("SPZT") == null ? "" : rs
							.getString("SPZT");
					String ssje = rs.getString("SSJE") == null ? "" : rs
							.getString("SSJE");
					String projectnu = rs.getString("PROJECTNO") == null ? ""
							: rs.getString("PROJECTNO");
					String lcbh = rs.getString("LCBH") == null ? "" : rs
							.getString("LCBH");
					String yxid = rs.getString("YXID") == null ? "" : rs
							.getString("YXID");
					String prc = rs.getString("PRCID") == null ? "" : rs
							.getString("PRCID");
					Long prcid = Long.parseLong("0");
					if (lcbs != null && !lcbs.equals("")) {
						if (lcbh != null) {
							try {
								List<ProcessRuOpinion> prolist = ProcessAPI
										.getInstance().getProcessOpinionList(
												lcbh, Long.parseLong(lcbs));
								if (prolist != null & prolist.size() > 0) {
									prcid = prolist.get(0).getPrcDefId();
								}
							} catch (Exception e) {
								logger.error(e,e);
							}
						}
					}

					if (stepid != null && !stepid.equals("")) {

						List<Task> tasklist = ProcessAPI.getInstance()
								.getUserProcessTaskList(xmlcServer, stepid,
										userid);
						List<Task> list1 = (tasklist == null ? new ArrayList<Task>()
								: tasklist);
						for (Task task : list1) {
							if (Long.parseLong(task.getProcessInstanceId()) == Long
									.parseLong(lcbs)
									&& Long.parseLong(task.getExecutionId()) == Long
											.parseLong(lcbs)) {
								String taskid = task.getId();
								rwid = taskid;
								break;
							}
						}
					} else {
						rwid = "";
					}
					String url = "loadProcessFormPage.action?actDefId=" + lcbh
							+ "&instanceId=" + lcbs + "&excutionId=" + lcbs
							+ "&taskId=" + rwid + "&PROJECTNO=" + projectnu
							+ "&GROUPID=" + jdbh;
					content.append(" <tr aria-selected=\"true\" id=\"29\" tabindex=\"-1\" class=\"ui-widget-content jqgrow ui-row-ltr \">");

					if (listJD.indexOf(jdbh) < 0) {
						i = i + 1;
						content.append("<td role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" class=\"ui-state-default jqgrid-rownum\" style=\"text-align:center;width:25px;\" title=\""
								+ i
								+ "\" aria-describedby=\"subformSUBFORM_XMBASE_rn\">"
								+ i
								+ "</td>              "
								+ "<td role=\"gridcell\"   rowspan=\""
								+ num
								+ "\" style=\"width:74px;\" title=\""
								+ taskname
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_USERID\"><xmp>"
								+ taskname
								+ "</xmp></td>                 "
								+ "<td role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" style=\"width:74px;\" title=\""
								+ manager
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\"><xmp>"
								+ manager
								+ "</xmp></td>                   "
								+ "<td role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" style=\"width:74px;text-align: center;\" title=\""
								+ ssje
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\"><xmp>"
								+ ssje + "</xmp></td>  " + "                 ");
					}
					StringBuffer strPart = new StringBuffer();
					String mb = rs.getString("mb") == null ? "" : rs
							.getString("mb");
					String mbName = rs.getString("mbName") == null ? "" : rs
							.getString("mbName");
					String ZLName = rs.getString("ZLName") == null ? "" : rs
							.getString("ZLName");
					String jdzl = rs.getString("jdzl") == null ? "" : rs
							.getString("jdzl");
					int zlNum = 1;
					if (hashZLNum.containsKey(jdbh + "_" + mb)) {
						zlNum = Integer.parseInt(hashZLNum.get(jdbh + "_" + mb)
								.toString());
					}
					// 资料模板
					if (listMB.indexOf(jdbh + "_" + mb) < 0) {

						content.append("<td role=\"gridcell\" style=\"width:146px;\" rowspan=\""
								+ zlNum
								+ "\" title=\""
								+ mbName
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_PHONE\">"
								+ "<span><a href=\"uploadifyDownload.action?fileUUID="
								+ mb
								+ "\" target=\"_blank\"><xmp>"
								+ mbName
								+ "</xmp></a></span></td> ");

						listMB.add(jdbh + "_" + mb);
					}
					try {
						content.append("<td role=\"gridcell\" style=\"width:146px;\" title=\""
								+ ZLName
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_EMAIL\"> <span><a href=\"uploadifyDownload.action?fileUUID="
								+ jdzl
								+ "\" target=\"_blank\"><xmp>"
								+ substring(ZLName, 45, "UTF-8")
								+ (ZLName.length() > 42 ? "..." : "")
								+ "</xmp></a></span></td>");
					} catch (UnsupportedEncodingException e) {
						logger.error(e,e);
					}
					if (listJD.indexOf(jdbh) < 0) {
						listJD.add(jdbh);
						content.append("<td role=\"gridcell\"  rowspan=\""
								+ num
								+ "\"  style=\"width:60px;\" title=\""
								+ spzt
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_EMAIL\"><a href=\"javascript:readLc('"
								+ lcbh
								+ "','"
								+ lcbs
								+ "','"
								+ yxid
								+ "',"
								+ "'"
								+ rwid
								+ "','"
								+ prcid
								+ "','"
								+ stepid
								+ "')\">"
								+ spzt
								+ "</a></td>                        "
								+ "<td  role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" style=\"width:60px;align:center\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_MEMO\">"
								+ (spzt != null && spzt.equals("") ? "<a href=\"javascript:addTask('"
										+ jdbh + "')\">上传</a>"
										: (spzt.equals("未提交")
												|| spzt.equals("驳回") ? "<a href=\"javascript:tj('"
												+ url + "')\">"+(spzt.equals("未提交")&&"0".equals(rwid)?"":"编辑")+"</a>"
												: "<a href=\"javascript:loadItem("
														+ instanceid
														+ ",'"
														+ jdbh
														+ "'"
														+ ")\">查看</a>"))
								+ "</td>");
					}
					content.append("</tr>");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rsZL);
		}
		content.append("</tbody>          " + "</table>          "
				+ "</div>            " + "</div>            ");
		return content.toString().replace("rowspan=\"0\"", "");

	}
	
	public String loadProjectNew(String projectno) {
		StringBuffer sql = new StringBuffer(
				"SELECT A.INSTANCEID,a.projectno,nvl(A.LCBS,'') LCBS, "
						+ "nvl(a.lcbh,'') lcbh, nvl(A.STEPID,'') STEPID,nvl(a.yxid,'') yxid,nvl(a.prcid,'') prcid,"
						+ " nvl(A.RWID,'') RWID, nvl(b.jdmc,'') TASK_NAME,nvl(A.GXR,'') GXR, nvl(to_char(A.GXSJ,'yyyy-MM-dd hh24:mi'),'') GXSJ,nvl(A.SPZT,'') SPZT,"
						+ " NVL(A.SSJE,'') SSJE,nvl(a.manager,'') manager,mbNum,B.ID JDBH"
						+ " ,mbName,mb,d.jdzl,ZLName"
						+ " FROM BD_ZQB_KM_INFO B  LEFT JOIN"
						+ " (SELECT * FROM BD_PM_TASK BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid"
						+ " and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 94 and BINDTABLE.metadataid = 107 WHERE projectno=?) A"
						+ " ON B.ID = A.GROUPID"
						+ " left join (select jdbh,count(*) mbNum from BD_ZQB_XMZLB group by jdbh) C"
						+ " on B.ID = c.jdbh"
						+ " LEFT JOIN  (select a.id,a.jdbh,d.FILE_SRC_NAME mbName,a.jdzl mb,b.jdzl,c.FILE_SRC_NAME ZLName"
						+ " from BD_ZQB_XMZLB a left join (select * from BD_ZQB_XMRWZLB where projectno=?) b"
						+ " on  a.jdbh = b.jdbh and a.jdzl = b.sxzl "
						+ " left join sys_upload_file c on b.jdzl = c.file_id"
						+ " left join sys_upload_file d on a.jdzl = d.file_id ) d"
						+ " ON B.ID = D.JDBH" + " ORDER BY b.ID,mb");
		StringBuffer sqlZL = new StringBuffer();
		sqlZL.append("select a.id,a.jdbh,d.FILE_SRC_NAME mbName,a.jdzl mb,b.jdzl,c.FILE_SRC_NAME ZLName from BD_ZQB_XMZLB a left join (select * from BD_ZQB_XMRWZLB where projectno=?) b on  a.jdbh = b.jdbh and a.jdzl = b.sxzl left join sys_upload_file c on b.jdzl = c.file_id left join sys_upload_file d on a.jdzl = d.file_id order by a.jdbh,a.jdzl");
		StringBuffer content = new StringBuffer();
		content.append("<table class=\"ui-jqgrid-htable ke-zeroborder\" role=\"grid\""
				+ " aria- labelledby=\"gbox_subformSUBFORM_BGSRZXX\"           "
				+ " style=\"width:850px;\" border=\"0\" cellpadding=\"0\"      "
				+ " cellspacing=\"0\">"
				+ " <thead>           "
				+ " 	<tr class=\"ui-jqgrid-labels\" role=\"rowheader\">       "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_rn\" role=\"columnheader\"             "
				+ " 			style=\"width:15px;\">                               "
				+ " 			<div id=\"jqgh_subformSUBFORM_BGSRZXX_rn\">          "
				+ " 				<span class=\"s-ico\" style=\"display:none;\"><span"
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\"      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div> <br />                                        "
				+ " 		</th>         "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZLX\" role=\"columnheader\"           "
				+ " 			style=\"width:50px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZLX\">           "
				+ " 				阶段名称<span class=\"s-ico\" style=\"display:none;\"><span      "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_ZW\" role=\"columnheader\"             "
				+ " 			style=\"width:50px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_ZW\">             "
				+ " 				阶段负责人<span class=\"s-ico\" style=\"display:none;\"><span    "
				+ " 				class=\"ui-grid-ico-sort ui-icon-asc ui-state-     "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_ZW\" role=\"columnheader\"             "
				+ " 			style=\"width:50px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_ZW\">             "
				+ " 				实收金额(万元)<span class=\"s-ico\" style=\"display:none;\"><span    "
				+ " 				class=\"ui-grid-ico-sort ui-icon-asc ui-state-     "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZQSRQ\" role=\"columnheader\"         "
				+ " 			style=\"width:100px;\"><span                         "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZQSRQ\">         "
				+ " 				所需资料模板<span class=\"s-ico\" style=\"display:none;\"><span  "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZZT\" role=\"columnheader\"           "
				+ " 			style=\"width:100px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZZT\">           "
				+ " 				上传资料<span class=\"s-ico\" style=\"display:none;\"><span        "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZJSRQ\" role=\"columnheader\"         "
				+ " 			style=\"width:40px;\"><span                         "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZJSRQ\">         "
				+ " 				操作<span class=\"s-ico\" style=\"display:none;\"><span          "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 	</tr>           "
				+ " </thead>          " + "										</table>            ");
		content.append("<div style=\"height: 100%; width: 850px;\" class=\"ui-jqgrid-bdiv\">     "
				+ "           <div style=\"position:relative;\"><div>                       "
				+ "</div>            "
				+ "<table style=\"width: 850px;\" class=\"ui-jqgrid-btable\" aria-labelledby=\"gbox_subformSUBFORM_SXCYRY\" aria-multiselectable=\"true\"        "
				+ "                  "
				+ "role=\"grid\" tabindex=\"1\" id=\"subformSUBFORM_SXCYRY\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
				+ "<tbody>           ");
		XmlcConf conf = SystemConfig._xmlcConf;
		String config = conf.getConfig();
		// 如果配置为1，则审批节点为初审负责人-》复审负责人-》领导审批
		// 如果配置为2，则审批节点为项目负责人-》质控部负责人1-》质控部负责人2
		String pro = config.equals("1") ? "XMRWLCX" : "DXMBXMRWLCX";
		String xmlcServer = ProcessAPI.getInstance().getProcessActDefId(pro);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rsZL = null;
		HashMap hashZLNum = new HashMap();
		List listMB = new ArrayList();
		List listJD = new ArrayList();
		try {
			stmt =conn.prepareStatement(sqlZL.toString());
			stmt.setString(1, projectno);
			rsZL = stmt.executeQuery();
			int i = 0;
			if (rsZL != null) {
				while (rsZL.next()) {
					String jdbh = rsZL.getString("jdbh") == null ? "" : rsZL
							.getString("jdbh");
					String mb = rsZL.getString("mb") == null ? "" : rsZL
							.getString("mb");
					// 循环查出每个模板的资料数量
					if (hashZLNum.containsKey(jdbh + "_" + mb)) {
						hashZLNum.put(jdbh + "_" + mb, Integer
								.parseInt(hashZLNum.get(jdbh + "_" + mb)
										.toString()) + 1);
					} else {
						hashZLNum.put(jdbh + "_" + mb, 1);
					}
				}
			}
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, projectno);
			stmt.setString(2, projectno);
			ResultSet rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					int jdbh = rs.getInt("JDBH");
					int mbNum = rs.getInt("MBNUM");
					int num = 0;
					if (rsZL != null) {
						if (!hashZLNum.isEmpty()) {
							Iterator iter = hashZLNum.entrySet().iterator();
							while (iter.hasNext()) {
								Map.Entry entry = (Map.Entry) iter.next();
								String strKey = entry.getKey().toString();
								if (strKey.indexOf(jdbh + "_") < 0) {
									continue;
								}
								int iZLNum = Integer.parseInt(entry.getValue()
										.toString());
								num += iZLNum;
							}
						}
					}
					if (num == 0) {
						num = mbNum;
					}
					Long instanceid = rs.getLong("INSTANCEID");
					String lcbs = rs.getString("LCBS") == null ? "" : rs
							.getString("LCBS");
					String stepid = rs.getString("STEPID") == null ? "" : rs
							.getString("STEPID");
					String rwid = rs.getString("RWID") == null ? "" : rs
							.getString("RWID");
					String taskname = rs.getString("TASK_NAME") == null ? ""
							: rs.getString("TASK_NAME");
					String manager = rs.getString("MANAGER") == null ? "" : rs
							.getString("MANAGER");
					String gxr = rs.getString("GXR") == null ? "" : rs
							.getString("GXR");
					String gxsj = rs.getString("GXSJ") == null ? "" : rs
							.getString("GXSJ");
					String spzt = rs.getString("SPZT") == null ? "" : rs
							.getString("SPZT");
					String ssje = rs.getString("SSJE") == null ? "" : rs
							.getString("SSJE");
					String projectnu = rs.getString("PROJECTNO") == null ? ""
							: rs.getString("PROJECTNO");
					String lcbh = rs.getString("LCBH") == null ? "" : rs
							.getString("LCBH");
					String yxid = rs.getString("YXID") == null ? "" : rs
							.getString("YXID");
					String prc = rs.getString("PRCID") == null ? "" : rs
							.getString("PRCID");
					Long prcid = Long.parseLong("0");
					if (lcbs != null && !lcbs.equals("")) {
						if (lcbh != null) {
							try {
								List<ProcessRuOpinion> prolist = ProcessAPI
										.getInstance().getProcessOpinionList(
												lcbh, Long.parseLong(lcbs));
								if (prolist != null & prolist.size() > 0) {
									prcid = prolist.get(0).getPrcDefId();
								}
							} catch (Exception e) {
								logger.error(e,e);
							}
						}
					}
					
					if (stepid != null && !stepid.equals("")) {
						
						List<Task> tasklist = ProcessAPI.getInstance()
								.getUserProcessTaskList(xmlcServer, stepid,
										userid);
						List<Task> list1 = (tasklist == null ? new ArrayList<Task>()
								: tasklist);
						for (Task task : list1) {
							if (Long.parseLong(task.getProcessInstanceId()) == Long
									.parseLong(lcbs)
									&& Long.parseLong(task.getExecutionId()) == Long
									.parseLong(lcbs)) {
								String taskid = task.getId();
								rwid = taskid;
								break;
							}
						}
					} else {
						rwid = "";
					}
					String url = "loadProcessFormPage.action?actDefId=" + lcbh
							+ "&instanceId=" + lcbs + "&excutionId=" + lcbs
							+ "&taskId=" + rwid + "&PROJECTNO=" + projectnu
							+ "&GROUPID=" + jdbh;
					content.append(" <tr aria-selected=\"true\" id=\"29\" tabindex=\"-1\" class=\"ui-widget-content jqgrow ui-row-ltr \">");
					
					if (listJD.indexOf(jdbh) < 0) {
						i = i + 1;
						content.append("<td role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" class=\"ui-state-default jqgrid-rownum\" style=\"text-align:center;width:25px;\" title=\""
								+ i
								+ "\" aria-describedby=\"subformSUBFORM_XMBASE_rn\">"
								+ i
								+ "</td>              "
								+ "<td role=\"gridcell\"   rowspan=\""
								+ num
								+ "\" style=\"width:74px;\" title=\""
								+ taskname
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_USERID\">"
								+ taskname
								+ "</td>                 "
								+ "<td role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" style=\"width:74px;\" title=\""
								+ manager
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\"><xmp>"
								+ manager
								+ "</xmp></td>                   "
								+ "<td role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" style=\"width:74px;text-align: center;\" title=\""
								+ ssje
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\"><xmp>"
								+ ssje + "</xmp></td>  " + "                 ");
					}
					StringBuffer strPart = new StringBuffer();
					String mb = rs.getString("mb") == null ? "" : rs
							.getString("mb");
					String mbName = rs.getString("mbName") == null ? "" : rs
							.getString("mbName");
					String ZLName = rs.getString("ZLName") == null ? "" : rs
							.getString("ZLName");
					String jdzl = rs.getString("jdzl") == null ? "" : rs
							.getString("jdzl");
					int zlNum = 1;
					if (hashZLNum.containsKey(jdbh + "_" + mb)) {
						zlNum = Integer.parseInt(hashZLNum.get(jdbh + "_" + mb)
								.toString());
					}
					// 资料模板
					if (listMB.indexOf(jdbh + "_" + mb) < 0) {
						
						content.append("<td role=\"gridcell\" style=\"width:146px;\" rowspan=\""
								+ zlNum
								+ "\" title=\""
								+ mbName
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_PHONE\">"
								+ "<span><a href=\"uploadifyDownload.action?fileUUID="
								+ mb
								+ "\" target=\"_blank\"><xmp>"
								+ mbName
								+ "</xmp></a></span></td> ");
						
						listMB.add(jdbh + "_" + mb);
					}
					try {
						content.append("<td role=\"gridcell\" style=\"width:146px;\" title=\""
								+ ZLName
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_EMAIL\"> <span><a href=\"uploadifyDownload.action?fileUUID="
								+ jdzl
								+ "\" target=\"_blank\"><xmp>"
								+ substring(ZLName, 45, "UTF-8")
								+ (ZLName.length() > 42 ? "..." : "")
								+ "</xmp></a></span></td>");
					} catch (UnsupportedEncodingException e) {
						logger.error(e,e);
					}
					if (listJD.indexOf(jdbh) < 0) {
						listJD.add(jdbh);
						content.append("<td  role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" style=\"width:60px;align:center\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_MEMO\">"
								+ (spzt != null && spzt.equals("") ? "<a href=\"javascript:addTask('"
										+ jdbh + "')\">上传</a>"
										: (spzt.equals("未提交")
												|| spzt.equals("驳回") ? "<a href=\"javascript:tj('"
												+ url + "')\">"+(spzt.equals("未提交")&&"0".equals(rwid)?"":"编辑")+"</a>"
												: "<a href=\"javascript:loadItem("
												+ instanceid
												+ ",'"
												+ jdbh
												+ "'"
												+ ")\">编辑</a>"))
												+ "</td>");
					}
					content.append("</tr>");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rsZL);
		}
		content.append("</tbody>          " + "</table>          "
				+ "</div>            " + "</div>            ");
		return content.toString().replace("rowspan=\"0\"", "");
		
	}

	/**
	 * 加载评价表单
	 * 
	 * @param projectNo
	 * @return
	 */
	public String loadPj(String projectNo) {
		//项目任务管理
		StringBuffer sql = new StringBuffer();
		sql.append("select info.jdmc TASK_NAME,task.MANAGER,pj.pjr,pj.pjsj,pj.pjsm,task.GROUPID,BIND2.instanceid PJINSID,pj.id,pj.ldpj from BD_ZQB_GPFXXMRWB task inner join BD_ZQB_GPFXXMPJB pj on task.projectno=? and task.projectno=pj.projectno and task.groupid=pj.groupid inner join BD_ZQB_TYXM_INFO info on task.groupid=info.id inner join (select BIND.Instanceid,BIND.Dataid from SYS_ENGINE_IFORM IFORM inner join sys_dem_engine engine on IFORM.ID=engine.FORMID and engine.title='股票发行评价表单' inner join SYS_ENGINE_FORM_BIND BIND on IFORM.Id=BIND.Formid and IFORM.Metadataid=BIND.Metadataid) bind2 on pj.id=bind2.dataid  order by task.groupid,pj.id");
		StringBuffer content = new StringBuffer();
		content.append("										<table class=\"ui-jqgrid-htable ke-zeroborder\" role=\"grid\""
				+ " aria- labelledby=\"gbox_subformSUBFORM_BGSRZXX\"           "
				+ " style=\"width:850px;\" border=\"0\" cellpadding=\"0\"      "
				+ " cellspacing=\"0\">"
				+ " <thead>           "
				+ " 	<tr class=\"ui-jqgrid-labels\" role=\"rowheader\">       "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_rn\" role=\"columnheader\"             "
				+ " 			style=\"width:20px;\">                               "
				+ " 			<div id=\"jqgh_subformSUBFORM_BGSRZXX_rn\">          "
				+ " 				<span class=\"s-ico\" style=\"display:none;\"><span"
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\"      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div> <br />                                        "
				+ " 		</th>         "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_cb\" role=\"columnheader\"                                                      "
				+ " 			style=\"width:20px;\">                               "
				+ " 			<div id=\"jqgh_subformSUBFORM_BGSRZXX_cb\">          "
				+ " 				<input class=\"cbox\" id=\"chkAll\"                                                    "
				+ " role=\"checkbox\" type=\"checkbox\" name=\"colname\"  onclick=\"selectAll()\"/><span class=\"s-ico\"                                             "
				+ " style=\"display:none;\"><span                    "
				+ " class=\"ui-grid-ico-sort ui-icon-                "
				+ "asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-  "
				+ "disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\"                             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div>                                               "
				+ " 		</th>                                                  "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZLX\" role=\"columnheader\"                                                    "
				+ " 			style=\"width:50px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZLX\">           "
				+ " 				阶段名称<span class=\"s-ico\" style=\"display:none;\"><span                                               "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_ZW\" role=\"columnheader\"                                                      "
				+ " 			style=\"width:100px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_ZW\">             "
				+ " 				阶段负责人<span class=\"s-ico\" style=\"display:none;\"><span                                             "
				+ " 				class=\"ui-grid-ico-sort ui-icon-asc ui-state-     "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZQSRQ\" role=\"columnheader\"                                                  "
				+ " 			style=\"width:60px;\"><span                         "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZQSRQ\">         "
				+ " 				评价人<span class=\"s-ico\" style=\"display:none;\"><span                                           "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_BZ\" role=\"columnheader\"                                                      "
				+ " 			style=\"width:60px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_BZ\">             "
				+ " 				评价时间<span class=\"s-ico\" style=\"display:none;\"><span                                               "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZZT\" role=\"columnheader\"                                                    "
				+ " 			style=\"width:60px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZZT\">           "
				+ " 				评价结果<span class=\"s-ico\" style=\"display:none;\"><span                                                 "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZZT\" role=\"columnheader\"                                                    "
				+ " 			style=\"width:80px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZZT\">           "
				+ " 				评价说明<span class=\"s-ico\" style=\"display:none;\"><span                                                 "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_XC\" role=\"columnheader\"                                                      "
				+ " 			style=\"width:80px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_XC\">             "
				+ " 				操作<span class=\"s-ico\" style=\"display:none;\"><span                                               "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 	</tr>                                                    "
				+ " </thead>                                                   "
				+ "										</table>                                                     ");
		content.append("<div style=\"height: 100%; width: 850px;\" class=\"ui-jqgrid-bdiv\">     "
				+ "           <div style=\"position:relative;\"><div>                       "
				+ "</div>            "
				+ "<table style=\"width: 850px;\" class=\"ui-jqgrid-btable\" aria-multiselectable=\"true\"                                                 "
				+ "                  "
				+ "role=\"grid\" tabindex=\"1\" id=\"subformSUBFORM_SXCYRY\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">                     "
				+ "<tbody> ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, projectNo);
			rs = stmt.executeQuery();
			int i = 1;
			while (rs.next()) {
				String task_name = rs.getString("TASK_NAME") == null ? "" : rs
						.getString("TASK_NAME");
				String manager = rs.getString("MANAGER") == null ? "" : rs
						.getString("MANAGER");
				String pjr = rs.getString("PJR") == null ? "" : rs
						.getString("PJR");
				Date pjsj = rs.getDate("PJSJ");
				String pjsjstr = pjsj != null ? sdf.format(pjsj) : "";
				String pjsm = rs.getString("PJSM") == null ? "" : rs
						.getString("PJSM");
				String groupid = rs.getString("GROUPID") == null ? "" : rs
						.getString("GROUPID");
				String pjinsid = rs.getString("PJINSID") == null ? "" : rs
						.getString("PJINSID");
				String id = rs.getString("ID") == null ? "" : rs
						.getString("ID");
				String pjjg = rs.getString("LDPJ") == null ? "" : rs.getString(
						"LDPJ").toString();
				try {
					content.append("<tr class=\"jqgfirstrow\" role=\"row\" style=\"height:auto\">            "
							+ "<td role=\"gridcell\" style=\"height:0px;width:20px;\"></td>             "
							+ "<td role=\"gridcell\" style=\"height:0px;width:20px;\"></td>             "
							+ "<td role=\"gridcell\" style=\"height:0px;width:50px;\"></td>             "
							+ "<td role=\"gridcell\" style=\"height:0px;width:100px;\"></td>             "
							+ " <td role=\"gridcell\" style=\"height:0px;width:60px;\"></td>            "
							+ " <td role=\"gridcell\" style=\"height:0px;width:60px;\"></td>            "
							+ " <td role=\"gridcell\" style=\"height:0px;width:60px;\"></td>    "
							+ " <td role=\"gridcell\" style=\"height:0px;width:80px;\"></td>    "
							+ " <td role=\"gridcell\" style=\"height:0px;width:80px;\"></td>    "
							+ " </tr>            "
							+ " <tr class=\" ui-widget-content jqgrow  \" >                                                   "
							+ " <td role=\"gridcell\" class=\"ui-state-default jqgrid-rownum\" style=\"text-align:center;width:20px"
							+ "px;\" title=\""
							+ i
							+ "\" aria-describedby=\"subformSUBFORM_XMBASE_rn\">"
							+ i
							+ "</td>              "
							// +
							// "<td role=\"gridcell\" style=\"width: 25px;\" align=\"center\"  valign=\"middle\"  aria-describedby=\"subformSUBFORM_XMBASE_cb\">                   "
							// +
							// "	     <input type=\"checkbox\" name=\"colname\" class=\"cbox\"                                                   "
							// + "				role=\"checkbox\" id=\""
							// + pjinsid
							// + "\">"
							// + "</td>             "
							+ "<td role=\"gridcell\" style=\"width: 20px;padding-left:8px;\" aria-describedby=\"subformSUBFORM_SXFKRBD_cb\">"
							+ "<input role=\"checkbox\" id=\""
							+ pjinsid
							+ "\" class=\"cbox\" name=\"colname\" type=\"checkbox\"></td>"
							+ "<td role=\"gridcell\"  title=\""
							+ task_name
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_POSITION\" style=\"text-align:left;width: 50px;padding-left:10px;\" ><xmp>"
							+ task_name
							+ "</xmp></td>        "
							+ "<td role=\"gridcell\"  title=\""
							+ manager
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_POSITION\" style=\"text-align:left;width: 100px;\"><xmp>"
							+ manager
							+ "</xmp></td>        "
							+ "<td role=\"gridcell\" title=\""
							+ pjr
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_USERID\" style=\"text-align:left;width:60px;\"><xmp>"
							+ pjr
							+ "</xmp></td>                 "
							+ "<td role=\"gridcell\" style=\"\" title=\""
							+ pjsj
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\" style=\"text-align:left;width: 60px;\"><xmp>"
							+ pjsj
							+ "</xmp></td>                   "
							+ "<td role=\"gridcell\" style=\"80px;\" title=\""
							+ pjjg
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\" style=\"text-align:left;width: 60px;\"><xmp>"
							+ pjjg
							+ "</xmp></td>                   "
							+ "<td role=\"gridcell\" style=\"80px;\" title=\""
							+ pjsm
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_TEL\"><xmp>"
							+ substring(pjsm, 12, "UTF-8")
							+ (pjsm.length() > 12 ? "..." : "")
							+ "</xmp></td>                          "
							+ "<td role=\"gridcell\" style=\"80px\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_TEL\"><a href=\"javascript:newPJ('"
							+ groupid
							+ "','"
							+ pjinsid
							+ "')\">编辑</a></td>                          "

							+ "</tr>   ");
				} catch (UnsupportedEncodingException e) {
					logger.error(e,e);
				}
				i++;
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		content.append("</tbody>                     " + "										          "
				+ "                             " + "									            "
				+ "                             "
				+ "</table>                     ");
		return content.toString();
	}

	public String loadXgwt(String projectNo) {
		Long gpfxwtfkFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行问题反馈表单'", "FORMID");
		StringBuffer sql = new StringBuffer();
		sql.append("select c.JDMC TASK_NAME,t.manager,c.id groupid,BINDTABLE.instanceid,a.id questionid,question QUESTION,b.username laster,nvl(to_char(a.createdate,'yyyy-MM-dd hh24:mi'),'') createdate,a.username USERNAME,a.xmbh from BD_ZQB_GPFXXGWTB a "
				+ "left join (select d.* from (select * from BD_ZQB_GPFXWTHFB where id in (select max(id) id from BD_ZQB_GPFXWTHFB where projectno=? group by questionno)) d ) b on a.id=b.questionno "
				+ "left join BD_ZQB_GPFXXMRWB t on a.xmbh=t.projectno and a.taskno=t.groupid  left join SYS_ENGINE_FORM_BIND BINDTABLE on a.id=bindtable.dataid and bindtable.instanceid is not null and BINDTABLE.formid = ? left join BD_ZQB_TYXM_INFO c on a.taskno=c.id where xmbh=? order by questionid");
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		StringBuffer content = new StringBuffer();
		content.append("										<table class=\"ui-jqgrid-htable ke-zeroborder\" role=\"grid\""
				+ " aria- labelledby=\"gbox_subformSUBFORM_BGSRZXX\"           "
				+ " style=\"width:850px;\" border=\"0\" cellpadding=\"0\"      "
				+ " cellspacing=\"0\">                                         "
				+ " <thead>                                                    "
				+ " 	<tr class=\"ui-jqgrid-labels\" role=\"rowheader\">       "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_rn\" role=\"columnheader\"                                                      "
				+ " 			style=\"width:25px;\">                               "
				+ " 			<div id=\"jqgh_subformSUBFORM_BGSRZXX_rn\">          "
				+ " 				<span class=\"s-ico\" style=\"display:none;\"><span"
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\"      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div> <br />                                        "
				+ " 		</th>                                                  "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_cb\" role=\"columnheader\"                                                      "
				+ " 			style=\"width:25px;\">                               "
				+ " 			<div id=\"jqgh_subformSUBFORM_BGSRZXX_cb\">          "
				+ " 				<input class=\"cbox\"  id=\"chkAll\"                                                     "
				+ " role=\"checkbox\" type=\"checkbox\"  name=\"colname\"  onclick=\"selectAll()\"/><span class=\"s-ico\"                                             "
				+ " style=\"display:none;\"><span                    "
				+ " class=\"ui-grid-ico-sort ui-icon-                "
				+ "asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-  "
				+ "disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\"                             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div>                                               "
				+ " 		</th>                                                  "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZLX\" role=\"columnheader\"                                                    "
				+ " 			style=\"width:100px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZLX\">           "
				+ " 				阶段名称<span class=\"s-ico\" style=\"display:none;\"><span                                               "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_ZW\" role=\"columnheader\"                                                      "
				+ " 			style=\"width:100px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_ZW\">             "
				+ " 				阶段负责人<span class=\"s-ico\" style=\"display:none;\"><span                                             "
				+ " 				class=\"ui-grid-ico-sort ui-icon-asc ui-state-     "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZQSRQ\" role=\"columnheader\"                                                  "
				+ " 			style=\"width:100px;\"><span                         "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZQSRQ\">         "
				+ " 				提问人<span class=\"s-ico\" style=\"display:none;\"><span                                           "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZQSRQ\" role=\"columnheader\"                                                  "
				+ " 			style=\"width:100px;\"><span                         "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZQSRQ\">         "
				+ " 				问题<span class=\"s-ico\" style=\"display:none;\"><span                                           "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_BZ\" role=\"columnheader\"                                                      "
				+ " 			style=\"width:100px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_BZ\">             "
				+ " 				提问时间<span class=\"s-ico\" style=\"display:none;\"><span                                               "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZZT\" role=\"columnheader\"                                                    "
				+ " 			style=\"width:100px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZZT\">           "
				+ " 				最后回复人<span class=\"s-ico\" style=\"display:none;\"><span                                                 "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                         "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_XC\" role=\"columnheader\"                                                      "
				+ " 			style=\"width:100pxpx;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_XC\">             "
				+ " 				操作<span class=\"s-ico\" style=\"display:none;\"><span                                               "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"                                                      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th>                                          "
				+ " 	</tr>                                                    "
				+ " </thead>                                                   "
				+ "										</table>                                                     ");
		content.append("<div style=\"height: 100%; width: 850px;\" class=\"ui-jqgrid-bdiv\">     "
				+ "           <div style=\"position:relative;\"><div>                       "
				+ "</div>            "
				+ "<table style=\"width: 845px;\" class=\"ui-jqgrid-btable\" aria-labelledby=\"gbox_subformSUBFORM_SXCYRY\" aria-multiselectable=\"true\"                                                 "
				+ "                  "
				+ "role=\"grid\" tabindex=\"1\" id=\"subformSUBFORM_SXCYRY\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">                     "
				+ "<tbody> ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, projectNo);
			stmt.setLong(2, gpfxwtfkFormId);
			stmt.setString(3, projectNo);
			rs = stmt.executeQuery();
			int i = 1;
			while (rs.next()) {
				String task_name = rs.getString("TASK_NAME") == null ? "" : rs
						.getString("TASK_NAME");
				String manager = rs.getString("MANAGER") == null ? "" : rs
						.getString("MANAGER");
				String username = rs.getString("USERNAME") == null ? "" : rs
						.getString("USERNAME");
				String createdate = rs.getString("CREATEDATE") == null ? "" : rs
						.getString("CREATEDATE");// 提问时间
				String lastuser = "";
				lastuser = rs.getString("LASTER") == null ? "" : rs
						.getString("LASTER");
				String last = rs.getString("CREATEDATE") == null ? "" : rs
						.getString("CREATEDATE").toString();// 最后提问时间
				String groupid = rs.getString("GROUPID") == null ? "" : rs
						.getString("GROUPID");
				String instanceid = rs.getString("INSTANCEID") == null ? ""
						: rs.getString("INSTANCEID");
				String questionid = rs.getString("QUESTIONID") == null ? ""
						: rs.getString("QUESTIONID");
				String question = rs.getString("QUESTION") == null ? "" : rs
						.getString("QUESTION");
				String xmbh = rs.getString("XMBH") == null ? "" : rs
						.getString("XMBH");
				try {
					String que = substring(question, 12, "UTF-8")
							+ (question.length() > 12 ? "..." : "");
					content.append("<tr class=\"jqgfirstrow\" role=\"row\" >            "
							+ "<td role=\"gridcell\" style=\"height:0px;width:25px;\"></td>             "
							+ "<td role=\"gridcell\" style=\"height:0px;width:25px;\"></td>             "
							+ "<td role=\"gridcell\" style=\"height:0px;width:100px;\"></td>             "
							+ "<td role=\"gridcell\" style=\"height:0px;width:100px;\"></td>             "
							+ " <td role=\"gridcell\" style=\"height:0px;width:100px;\"></td>            "
							+ " <td role=\"gridcell\" style=\"height:0px;width:100px;\"></td>            "
							+ " <td role=\"gridcell\" style=\"height:0px;width:100px;\"></td>    "
							+ " <td role=\"gridcell\" style=\"height:0px;width:100px;\"></td>    "
							+ " </tr>            "
							+ " <tr aria-selected=\"true\" id=\"29\" tabindex=\"-1\" role=\"row\" class=\" ui-widget-content jqgrow  \" >                                                   "
							+ " <td role=\"gridcell\" class=\"ui-state-default jqgrid-rownum\" style=\"text-align:center;width: 25px;\" title=\""
							+ i
							+ "\" aria-describedby=\"subformSUBFORM_XMBASE_rn\">"
							+ i
							+ "</td>              "
							+ "<td role=\"gridcell\" style=\"text-align:center;width: 20px;\" aria-describedby=\"subformSUBFORM_SXFKRBD_cb\">"
							+ "<input role=\"checkbox\" id=\""
							+ instanceid
							+ "\" class=\"cbox\" name=\"colname\" type=\"checkbox\"></td>"
							+ "<td role=\"gridcell\"  style=\"align:left;\" title=\""
							+ task_name
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_POSITION\" style=\"text-align:center;width: 100px;\" ><xmp>"
							+ task_name
							+ "</xmp></td>        "
							+ "<td role=\"gridcell\"  style=\"align:left;\"  title=\""
							+ manager
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_POSITION\" style=\"text-align:center;width: 100px;\"><xmp>"
							+ manager
							+ "</xmp></td>        "
							+ "<td role=\"gridcell\"  style=\"align:left;\"  style=\"\" title=\""
							+ username
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_USERID\" style=\"text-align:center;width:100px;\"><xmp>"
							+ username
							+ "</xmp></td>                 "
							+ "<td role=\"gridcell\"  style=\"align:left;\"  style=\"\" title=\""
							+ question
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_USERID\" style=\"text-align:center;width:100px;\"><xmp>"
							+ que
							+ "</xmp></td>                 "
							+ "<td role=\"gridcell\"   style=\"align:left;\"  style=\"\" title=\""
							+ createdate
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\" style=\"text-align:center;width: 100px;\"><xmp>"
							+ createdate
							+ "</xmp></td>                   "
							+ "<td role=\"gridcell\"  style=\"align:left;\"  style=\"\" title=\""
							+ lastuser
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_TEL\" style=\"text-align:center;width: 100px;\"><xmp>"
							+ lastuser
							+ "</xmp></td>                          "
							+ "<td   style=\"align:left;\"  style=\"\" title=\"\" ><a href=\"javascript:editQuestion('"
							+ instanceid
							+ "')\">编辑问题</a>|"
							+ "<a href=\"javascript:showQuestion('"
							+ task_name
							+ "','"
							+ groupid
							+ "','"
							+ questionid
							+ "')\">回复</a>	</td>" + "</tr>");
				} catch (UnsupportedEncodingException e) {
					logger.error(e,e);
				}
				i++;
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		content.append("</tbody>                     " + "										          "
				+ "                             " + "									            "
				+ "                             "
				+ "</table>                     ");
		return content.toString();
	}

	public String checkRwid(String projectNo, String groupid) {
		Map params = new HashMap();
		int n=1;
		String sql = "select MAX(id) id from BD_ZQB_TYXM_INFO t where XMLX='股票发行项目' and id< ?";
		params.put(1, groupid);
		n++;
		String id = "";
		String demUUID = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		// 校验上一任务是否存在
		int lastId = DBUTilNew.getInt("ID",sql,params);// 上一任务id
		if (lastId != 0) {
			// 有，则判断是否已经存在于项目任务阶段，已存在则正确，不存在，则错误
			conditionMap.put("GROUPID", lastId);
			List<HashMap> list = DemAPI.getInstance().getList(demUUID,
					conditionMap, null);// 找到上一个
			if (list.size() > 0) {
				// 判断是否该级别已存在
				conditionMap.put("GROUPID", groupid);
				List<HashMap> list1 = DemAPI.getInstance().getList(demUUID,
						conditionMap, null);// 找到上一个
				if (list1.size() > 0) {// 去大于1的
					sql = "select min(id) id from BD_ZQB_TYXM_INFO t where XMLX='股票发行项目' and id> ? ";
					params.put(1, groupid);
					n++;
					int nextId = DBUTilNew.getInt("ID",sql, params);// 上一任务id
					if (nextId != 0) {
						id = String.valueOf(nextId);
					} else {
						id = "0";
					}

				} else {
					id = "0";
				}
			} else {
				id = String.valueOf(lastId);
			}
		}
		// 资料说明
		params = new HashMap();
		params.put(1, groupid);
		sql = "select * from BD_ZQB_TYXM_INFO where XMLX='股票发行项目' and ID= ?";
		String content = DBUTilNew.getDataStr("CONTENT",sql, params);
		String sxzlmb = DBUTilNew.getDataStr("SXZLQD",sql,params );
		HashMap hash = new HashMap();
		hash.put("GROUPID", id);
		hash.put("JDZL", content);
		params = new HashMap();
		params.put(1, sxzlmb);
		String name = DBUTilNew.getDataStr( "FILE_SRC_NAME","select FILE_SRC_NAME from sys_upload_file where file_id= ? ",params);
		hash.put("SXZLMB", name);
		hash.put("UUID", sxzlmb);
		JSONArray json = JSONArray.fromObject(hash);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}

	/**
	 * 删除评价信息
	 * 
	 * @param projectNo
	 * @param taskid
	 * @param instanceid
	 * @return
	 */
	public boolean removePJ(Long instanceid) {
		// 删除评价信息
		boolean flag = DemAPI.getInstance().removeFormData(instanceid);
		// 更新项目基本信息的pjinsid为‘’
		HashMap conditionMap = new HashMap();
		String demUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		List<HashMap> rwlist = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		for (HashMap map : rwlist) {
			map.put("PJINSID", "");
			Long instanceId = map.get("INSTANCEID") == null ? 0 : Long
					.parseLong(map.get("INSTANCEID").toString());
			Long dataid = map.get("ID") == null ? 0 : Long.parseLong(map.get(
					"ID").toString());
			if (instanceId != 0 && dataid != 0) {
				DemAPI.getInstance().updateFormData(demUUID, instanceId, map,
						dataid, false);
			}
		}
		return flag;
	}

	/**
	 * 删除相关问题
	 * 
	 * @param instanceid
	 * @return
	 */
	public boolean deleteXgwt(Long instanceid) {
		boolean flag = false;
		// 删除所选的问题和回复信息
		// 1.删除回复信息
		Map map = DemAPI.getInstance().getFromData(instanceid,
				EngineConstants.SYS_INSTANCE_TYPE_DEM);// 问题
		String ProjectReTalkUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行问题回复表单'", "UUID");
		if (map.get("ID") != null && !map.get("ID").equals("")) {
			Long questionId = Long.parseLong(map.get("ID").toString());
			HashMap conditionMap = new HashMap();
			conditionMap.put("QUESTIONNO", questionId);
			List<HashMap> retalkList = DemAPI.getInstance().getList(
					ProjectReTalkUUID, conditionMap, null);
			for (HashMap retalk : retalkList) {
				Long talkInsId = retalk.get("INSTANCEID") == null ? 0 : Long
						.parseLong(retalk.get("INSTANCEID").toString());
				DemAPI.getInstance().removeFormData(talkInsId);
			}
		}
		flag = DemAPI.getInstance().removeFormData(instanceid);
		return flag;
	}

	public boolean deleteTask(Long instanceid) {
		// 删除任务
		// 1.删除流程
		boolean flag = false;
		HashMap hashMap = DemAPI.getInstance().getFromData(instanceid,
				EngineConstants.SYS_INSTANCE_TYPE_DEM);// 表中数据
		Long lcbs = null;
		if (hashMap.get("LCBS") != null
				&& !hashMap.get("LCBS").toString().equals("")) {
			lcbs = Long.parseLong(hashMap.get("LCBS").toString());
		}
		String task_name = hashMap.get("TASK_NAME") == null ? "" : hashMap
				.get("TASK_NAME").toString();
		String projectno = hashMap.get("PROJECTNO") == null ? "" : hashMap
				.get("PROJECTNO").toString();
		String projectname = hashMap.get("PROJECTNAME") == null ? ""
				: hashMap.get("PROJECTNAME").toString();
		String groupid = hashMap.get("GROUPID") == null ? "" : hashMap.get(
				"GROUPID").toString();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from BD_XP_ZYDDSXLCB where task_name='" + task_name
				+ "' and projectno='" + projectno
				+ "' and projectname='" + projectname
				+ "' and groupid='" + groupid + "'");
		DBUtil.executeUpdate(sql.toString());
		XmlcConf conf = SystemConfig._xmlcConf;
		String config = conf.getConfig();
		String pro = config.equals("1") ? "XMRWLCX" : "DXMBXMRWLCX";
		String xmlcServer = ProcessAPI.getInstance().getProcessActDefId(pro);
		// 删除流程
		if (lcbs != null) {
			List<OrgUser> userlist = ProcessAPI.getInstance()
					.getProcessTransUserList(lcbs);
			if (userlist != null && userlist.size() > 0) {
				for (int i = 0; i < userlist.size(); i++) {
					String userid = userlist.get(i).getUserid();
					List<Task> tasklist = ProcessAPI.getInstance()
							.getUserProcessTaskList(xmlcServer, conf.getJd1(),
									userid);
					for (Task task : tasklist) {
						if (Long.parseLong(task.getProcessInstanceId()) == lcbs) {
							// 删除流程表数据
							ProcessAPI.getInstance().removeProcessInstance(
									task.getId(), userid);// lcbs.toString()
						}

					}
				}
			}
		}
		// 删除物理表
		flag = DemAPI.getInstance().removeFormData(instanceid);
		return false;
	}

	/**
	 * 获取阶段资料
	 * 
	 * @param projectNo
	 * @param groupid
	 * @param instanceId
	 * @return
	 */
	public String getJdzl(String projectNo, String groupid, Long instanceId) {
		String spzt = null;
		if(projectNo!=null&&!projectNo.equals("")&&groupid!=null&&!groupid.equals("")){
			Map params = new HashMap();
			params.put(1, groupid);
			params.put(2, projectNo);
			spzt = DBUTilNew.getDataStr("SPZT","SELECT SPZT FROM BD_ZQB_GPFXXMRWB WHERE GROUPID= ?  AND PROJECTNO= ? ", params);
		}
		StringBuffer content = new StringBuffer();
		if (groupid == null) {
			HashMap map = ProcessAPI.getInstance().getFromData(instanceId,
					EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			groupid = map.get("GROUPID") == null ? "" : map.get("GROUPID")
					.toString();
		}
		HashMap conditionMap = new HashMap();
		conditionMap.put("JDBH", groupid);
		/*String zlstring = "7e6309501fb442faa4c61d0c4515d2d0";*/
		StringBuffer sxzlmb = new StringBuffer();
		String XMZLUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '通用项目资料表单'", "UUID");
		List<HashMap> list = DemAPI.getInstance().getList(XMZLUUID, conditionMap, null);
		for (int i = 0; i < list.size(); i++) {
			HashMap m = list.get(i);
			String jdzl = m.get("JDZL").toString();
			sxzlmb.append(jdzl + ",");
			String name = DBUtil.getString(
					"select FILE_SRC_NAME from sys_upload_file where file_id='"
							+ jdzl + "'", "FILE_SRC_NAME");
			content.append("<tr id=\"itemTr_185"
					+ i
					+ "\" name=\"jdzlrow\"><td  width=\"180\" class=\"td_title\" id=\""
					+ i
					+ "\"><span><a href=\"uploadifyDownload.action?fileUUID="
					+ jdzl
					+ "\" target=\"_blank\">"
					+ name
					+ "</a></span></td><td class=\"td_data\" name=\"model\" id=\""
					+ jdzl + "" + "\">");
			content.append("<div id='DIVJDZL"
					+ i
					+ "' style='width:100px' name='jdzldiv' filediv='filediv'><div name='jdzldiv'><input type=hidden size=100 id='JDZL"
					+ i
					+ "'  class = '{maxlength:1024}' "
					+ " name='JDZL"
					+ i
					+ "' value=''/></div><button   onclick='showUploadifyPageJDZL(\"DIVJDZL"
					+ i + "\",\"JDZL" + i + "\");return false;' >附件上传</button>");
			HashMap map = new HashMap();
			map.put("PROJECTNO", projectNo);
			map.put("JDBH", groupid);
			map.put("SXZL", jdzl);
			String XMRWZLUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '通用项目任务资料表单'", "UUID");
			List<HashMap> l = DemAPI.getInstance().getAllList(
					XMRWZLUUID, map, null);
			for (HashMap zlmap : l) {
				String zl = zlmap.get("JDZL").toString();
				String na = DBUtil.getString(
						"select FILE_SRC_NAME from sys_upload_file where file_id='"
								+ zl + "'", "FILE_SRC_NAME");
				content.append("<div id=\""
						+ zl
						+ "\" "
						+ "style=\"background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\">"
						+ "	<div style=\"align:right;float: right;\">");
						if(spzt!=null&&!spzt.equals("")&&!spzt.equals("审批通过")&&!spzt.equals("已提交")){
							content.append("<a href=\"javascript:uploadifyReomve('JDZL" + i + "','" + zl + "','" + zl + "');\">" + "<img src=\"/iwork_img/del3.gif\"></a>");
						}
						content.append("</div><span><a href=\"uploadifyDownload.action?fileUUID="
						+ zl + "\" target=\"_blank\">"
						+ "<img src=\"/iwork_img/attach.png\">" + na
						+ "</a></span></div>");
			}
			content.append("</div></td></tr>");
		}
		String c = DBUtil.getString("select * from BD_ZQB_TYXM_INFO where id="
				+ groupid, "CONTENT");
		HashMap returnMap = new HashMap();
		/*returnMap.put("ATTACH", zlstring);*/
		returnMap.put("CONTENT", content.toString());
		returnMap.put("SXZLMB", sxzlmb.toString());
		returnMap.put("C", c);
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public String getJdzlTwo(String projectNo, String groupid, Long instanceId) {
		String spzt = null;
		if(projectNo!=null&&!projectNo.equals("")&&groupid!=null&&!groupid.equals("")){
			spzt = DBUtil.getString("SELECT SPZT FROM BD_ZQB_GPFXXMRWB WHERE GROUPID='"+groupid+"' AND PROJECTNO='"+projectNo+"'", "SPZT");
		}
		StringBuffer content = new StringBuffer();
		if (groupid == null) {
			HashMap map = ProcessAPI.getInstance().getFromData(instanceId,
					EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			groupid = map.get("GROUPID") == null ? "" : map.get("GROUPID")
					.toString();
		}
		HashMap conditionMap = new HashMap();
		conditionMap.put("JDBH", groupid);
		/*String zlstring = "7e6309501fb442faa4c61d0c4515d2d0";*/
		StringBuffer sxzlmb = new StringBuffer();
		String XMZLUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '通用项目资料表单'", "UUID");
		List<HashMap> list = DemAPI.getInstance().getList(XMZLUUID, conditionMap, null);
		for (int i = 0; i < list.size(); i++) {
			HashMap m = list.get(i);
			String jdzl = m.get("JDZL").toString();
			sxzlmb.append(jdzl + ",");
			String name = DBUtil.getString(
					"select FILE_SRC_NAME from sys_upload_file where file_id='"
							+ jdzl + "'", "FILE_SRC_NAME");
			content.append("<tr id=\"itemTr_185"
					+ i
					+ "\" name=\"jdzlrow\"><td  width=\"180\" class=\"td_title\" id=\""
					+ i
					+ "\"><span><a href=\"uploadifyDownload.action?fileUUID="
					+ jdzl
					+ "\" target=\"_blank\">"
					+ name
					+ "</a></span></td><td class=\"td_data\" name=\"model\" id=\""
					+ jdzl + "" + "\">");
			content.append("<div id='DIVJDZL"
					+ i
					+ "' style='width:100px' name='jdzldiv' filediv='filediv'><div name='jdzldiv'><input type=hidden size=100 id='JDZL"
					+ i
					+ "'  class = '{maxlength:1024}' "
					+ " name='JDZL"
					+ i
					+ "' value=''/></div>");
			HashMap map = new HashMap();
			map.put("PROJECTNO", projectNo);
			map.put("JDBH", groupid);
			map.put("SXZL", jdzl);
			String XMRWZLUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '通用项目任务资料表单'", "UUID");
			List<HashMap> l = DemAPI.getInstance().getAllList(
					XMRWZLUUID, map, null);
			for (HashMap zlmap : l) {
				String zl = zlmap.get("JDZL").toString();
				String na = DBUtil.getString(
						"select FILE_SRC_NAME from sys_upload_file where file_id='"
								+ zl + "'", "FILE_SRC_NAME");
				content.append("<div id=\""
						+ zl
						+ "\" "
						+ "style=\"background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\">"
						+ "<div style=\"align:right;float: right;\">");
						if(spzt!=null&&!spzt.equals("")&&!spzt.equals("审批通过")&&!spzt.equals("已提交")){
							content.append("<a href=\"javascript:uploadifyReomve('").append(na).append("','").append(zl).append("','").append(zl).append("');\"><img src=\"/iwork_img/del3.gif\"/></a>");
						}
						content.append("</div><span><a href=\"uploadifyDownload.action?fileUUID="
						+ zl + "\" target=\"_blank\">"
						+ "<img src=\"/iwork_img/attach.png\">" + na
						+ "</a></span></div>");
			}
			content.append("</div></td></tr>");
		}
		String c = DBUtil.getString("select * from BD_ZQB_TYXM_INFO where id="
				+ groupid, "CONTENT");
		HashMap returnMap = new HashMap();
		/*returnMap.put("ATTACH", zlstring);*/
		returnMap.put("CONTENT", content.toString());
		returnMap.put("SXZLMB", sxzlmb.toString());
		returnMap.put("C", c);
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}

	public boolean deleteFile(String uuid) {
		boolean flag1 = false;
		boolean flag2 = false;
		String PROJECT_TASK_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '通用项目任务资料表单'", "UUID");
		HashMap conditionMap = new HashMap();
		conditionMap.put("JDZL", uuid);
		List<HashMap> list = DemAPI.getInstance().getList(PROJECT_TASK_UUID, conditionMap, null);
		for (HashMap map : list) {
			Long instanceid = Long.parseLong(map.get("INSTANCEID").toString());
			flag1 = DemAPI.getInstance().removeFormData(instanceid);
		}
		return flag1;
	}

	public boolean deleteXMZL(String uuid) {
		boolean flag = false;
		HashMap conditionMap = new HashMap();
		conditionMap.put("JDZL", uuid);
		List<HashMap> list = DemAPI.getInstance().getList(
				"98f297bccb85479ba29faf1fb5a05734", conditionMap, null);
		for (HashMap map : list) {
			Long instanceid = Long.parseLong(map.get("INSTANCEID").toString());
			flag = DemAPI.getInstance().removeFormData(instanceid);
		}

		return flag;
	}

	public boolean checkPJ(String projectNo, String groupid) {
		boolean flag = false;
		HashMap map = new HashMap();
		map.put("GROUPID", groupid);
		map.put("PROJECTNO", projectNo);
		List list = DemAPI.getInstance().getList(
				"9ae9394e3bfa4ef792c13e2db2fa12e2", map, null);
		if (list.size() <= 0) {
			flag = true;
		}
		return flag;
	}

	public List<HashMap> getXMCYList(String userid) {
		List<HashMap> list = new ArrayList();
		list = zqbGpfxProjectManageDAO.getXMCYList(userid);
		return list;
	}

	public boolean checkXmjdExists(String projectNo, String groupid) {
		// 校验项目阶段是否存在
		boolean flag;
		String sql = "select nvl(MAX(id),0) id from BD_ZQB_KM_INFO t where id<"
				+ groupid;
		String id = "";
		String demUUID = "b25ca8ed0a5a484296f2977b50db8396";// 项目任务
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		// 校验上一任务是否存在
		int lastId = DBUtil.getInt(sql, "ID");// 上一任务id
		if (lastId != 0) {
			// 有，则判断是否已经存在于项目任务阶段，已存在则正确，不存在，则错误
			conditionMap.put("GROUPID", lastId);
			List<HashMap> list = DemAPI.getInstance().getList(demUUID,
					conditionMap, null);// 找到上一个
			if (list.size() > 0) {
				flag = true;
			} else {
				flag = false;
			}
		} else {
			flag = true;
		}
		// 资料说明
		// sql = "select * from BD_ZQB_KM_INFO where jdbh='" + groupid + "'";
		// String content = DBUtil.getString(sql, "CONTENT");
		// String sxzlmb = DBUtil.getString(sql, "SXZLQD");
		// HashMap hash = new HashMap();
		// hash.put("GROUPID", id);
		// hash.put("JDZL", content);
		// String name = DBUtil.getString(
		// "select FILE_SRC_NAME from sys_upload_file where file_id='"
		// + sxzlmb + "'", "FILE_SRC_NAME");
		// hash.put("SXZLMB", name);
		// hash.put("UUID", sxzlmb);
		// JSONArray json = JSONArray.fromObject(hash);
		// StringBuffer jsonHtml = new StringBuffer();
		// jsonHtml.append(json);
		// jsonHtml.toString()
		return flag;
	}

	/**
	 * 截取字符长度
	 * 
	 * @param text
	 * @param length
	 * @param encode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String substring(String text, int length, String encode)
			throws UnsupportedEncodingException {
		if (text == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int currentLength = 0;
		for (char c : text.toCharArray()) {
			currentLength += String.valueOf(c).getBytes(encode).length;
			if (currentLength <= length) {
				sb.append(c);
			} else {
				break;
			}
		}
		return sb.toString();

	}

	public int getXgwthfSize(String projectName, String xmjd, String question) {
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		if (projectName != null && !projectName.equals("")) {
			conditionMap.put("XMMC", projectName);
		}
		if (xmjd != null && !xmjd.equals("")) {
			conditionMap.put("TASKNO", xmjd);
		}
		if (question != null && !question.equals("")) {
			conditionMap.put("QUESTION", question);
		}
		String PROJECT_WTFK_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行问题反馈表单'", "UUID");
		String userid = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel().getUserid();
		conditionMap.put("USERID", userid);
		int l = DemAPI.getInstance().getListSize(PROJECT_WTFK_UUID, conditionMap, null);
		return l;
	}

	public List getXgwthfx(int pageNumber, int pageSize, String projectName,
			String xmjd, String question) {
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		if (projectName != null && !projectName.equals("")) {
			conditionMap.put("XMMC", projectName);
		}
		if (xmjd != null && !xmjd.equals("")) {
			conditionMap.put("TASKNO", xmjd);
		}
		if (question != null && !question.equals("")) {
			conditionMap.put("QUESTION", question);
		}
		String userid = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel().getUserid();
		conditionMap.put("USERID", userid);
		int startRow = (pageNumber - 1) * pageSize;
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		String PROJECT_WTFK_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行问题反馈表单'", "UUID");
		List<HashMap> l = DemAPI.getInstance().getList(PROJECT_WTFK_UUID, conditionMap, "id desc",pageSize, startRow);
		for (HashMap map : l) {
			// 根据任务id获取任务名称，并将值放入map中
			if (map.get("TASKNO") != null && !map.get("TASKNO").equals("")) {
				String sql = "select JDMC from BD_ZQB_TYXM_INFO where id="
						+ map.get("TASKNO");
				String taskname = DBUtil.getString(sql, "JDMC") == null ? ""
						: DBUtil.getString(sql, "JDMC");
				map.put("TASKNAME", taskname);
			}
			String sql1 = "select instanceid from BD_ZQB_GPFXXMB BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid"
					+ " and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = "+gpfxFormId+" and projectno='"
					+ map.get("XMBH") + "'";
			String xmid = DBUtil.getString(sql1, "instanceid") == null ? ""
					: DBUtil.getString(sql1, "instanceid");
			map.put("XMID", xmid);
		}
		return l;

	}

	public String getChartProjectName(int index) {
		String manager = "";
		List<HashMap> slist = zqbGpfxProjectManageDAO.getProjectManagerUserData();
		if (slist != null) {
			String projectName = "";
			for (int i = 0; i < slist.size(); i++) {
				if (index == i + 1) {
					HashMap map = slist.get(i);
					manager = map.get("manager").toString();
					break;
				}
			}
		}
		return manager;
	}

	public void expProjectList(HttpServletResponse response, String zcPro) {
		boolean isSuperMan = this.getIsSuperMan();
		String[] split = zcPro.split(",");
		HashMap map=new HashMap();
		for (int i = 0; i < split.length; i++) {
			map.put(split[i],i);
		}
		HashMap conditionMap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		List<HashMap> slist= new ArrayList<HashMap>();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		if (isSuperMan) {
			slist = zqbGpfxProjectManageDAO.expProjectList1();
		} else {
			slist = zqbGpfxProjectManageDAO.expProjectList2(owner);
		}
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目信息汇总");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor((short) 22);
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 1);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillForegroundColor((short) 22);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBorderLeft((short) 1);
		style6.setBorderRight((short) 1);
		style6.setBorderTop((short) 1);
		style6.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//style5.setFillForegroundColor(HSSFColor.LIME.index);
		style5.setFillForegroundColor((short) 22);
		//style5.setFillForegroundColor(HSSFColor.WHITE.index);
		style5.setFillPattern((short) 1);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> list1 = new ArrayList<HashMap>();
		int z = 0;
		int n = 0;
		int m = 0;
		int zz=1;
		int sListSize=slist.size();
		int count = 1;
		int hbcount = 1;
		int hbSize=0;
		int hbRow=0;
		Integer pjInstanceid=0;
		boolean falg=false;
		int beginRow=0;
		int endRow=0;
		int beginSize=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell cell1 = row1.createCell((short) z++);
		cell1.setCellValue("项目名称");
		cell1.setCellStyle(style5);
		HashMap sheetMap=new HashMap();
		for(Object o : map.keySet()){
			if("PROJECTNAME".equals(o.toString())||"CUSTOMERNAME".equals(o.toString())||"OWNER".equals(o.toString())||"MANAGER".equals(o.toString())||"STARTDATE".equals(o.toString())||"ENDDATE".equals(o.toString())||"KHLXR".equals(o.toString())||"KHLXDH".equals(o.toString())||"GGJZR".equals(o.toString())||"SBJZR".equals(o.toString())||"HTJE".equals(o.toString())||"ZCLR".equals(o.toString())||"ZCLRDH".equals(o.toString())||"FZJGMC".equals(o.toString())||"WBCLRJG".equals(o.toString())||"FZJGLXR".equals(o.toString())){
						sheetMap.put(o.toString(), null);
			}
		}
		if(Arrays.asList(split).contains("CUSTOMERNAME")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户名称");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("OWNER")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目负责人");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("MANAGER")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目现场负责人");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("STARTDATE")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目开始时间");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("ENDDATE")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("预计项目完成时间");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("KHLXR")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系人");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("KHLXDH")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系电话");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("GGJZR")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("公告基准日");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("SBJZR")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("申报基准日");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("HTJE")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("合同金额");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("MEMO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("风险评估说明");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("XMBZ")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目备注");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("ZCLR")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("主承揽人");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("ZCLRDH")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("主承揽人电话");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("FZJGMC")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("分支机构名称");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("FZJGLXR")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("分支机构联系人");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("WBCLRJG")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("外部承揽人机构");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("JDMC")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段名称");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("TWUSERNAME")&&!Arrays.asList(split).contains("JDMC")){
			falg=true;
		}
		if(Arrays.asList(split).contains("QUESTION")&&!Arrays.asList(split).contains("JDMC")){
			falg=true;
		}
		if(Arrays.asList(split).contains("TWCREATEDATE")&&!Arrays.asList(split).contains("JDMC")){
			falg=true;
		}
		if(Arrays.asList(split).contains("CONTENT")&&!Arrays.asList(split).contains("JDMC")){
			falg=true;
		}
		if(Arrays.asList(split).contains("PJNAME")&&!Arrays.asList(split).contains("JDMC")){
			falg=true;
		}
		if(Arrays.asList(split).contains("PJSJ")&&!Arrays.asList(split).contains("JDMC")){
			falg=true;
		}
		if(Arrays.asList(split).contains("LDPJ")&&!Arrays.asList(split).contains("JDMC")){
			falg=true;
		}
		if(Arrays.asList(split).contains("PJSM")&&!Arrays.asList(split).contains("JDMC")){
			falg=true;
		}
		if(falg){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段名称");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("JDFZR")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段负责人");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("SSJE")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("实收金额");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("FILENAME")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段上传资料");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("SPZT")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("审批结果");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("TWUSERNAME")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("提问人");
			cell1.setCellStyle(style5);
		}
		if(Arrays.asList(split).contains("QUESTION")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("问题");
			cell1.setCellStyle(style5);
		}
		if(Arrays.asList(split).contains("TWCREATEDATE")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("提问时间");
			cell1.setCellStyle(style5);
		}
		if(Arrays.asList(split).contains("CONTENT")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("回复信息");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("PJNAME")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段评价人");
			cell1.setCellStyle(style5);
		}
		if(Arrays.asList(split).contains("PJSJ")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段评价时间");
			cell1.setCellStyle(style5);
		}
		if(Arrays.asList(split).contains("LDPJ")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段评价结果");
			cell1.setCellStyle(style5);
		}
		if(Arrays.asList(split).contains("PJSM")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段评价说明");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("ZJJGMC")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("中介机构信息");
			cell1.setCellStyle(style5);
		}
		
		if(Arrays.asList(split).contains("NAME")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目成员信息");
			cell1.setCellStyle(style5);
		}
		for (HashMap hashMap : slist) {
			List<HashMap> xgwtList= new ArrayList<HashMap>();
			List<HashMap> pjList= new ArrayList<HashMap>();
			List<HashMap> xmcyList= new ArrayList<HashMap>();
			List<HashMap> zjjgList= new ArrayList<HashMap>();
			if(hashMap.get("GROUPID")!=null&&!"".equals(hashMap.get("GROUPID").toString())){
				xgwtList=zqbGpfxProjectManageDAO.expProjectXGWTList(hashMap.get("PROJECTNO").toString(),hashMap.get("GROUPID").toString());
				pjList=zqbGpfxProjectManageDAO.expProjectPJList(hashMap.get("PROJECTNO").toString(),hashMap.get("GROUPID").toString());
			}
			if(pjInstanceid.longValue()!=Integer.valueOf(hashMap.get("LCID").toString())){
				xmcyList= DemAPI.getInstance().getFromSubData(Long.parseLong(hashMap.get("LCID").toString()), "SUBFORM_XMCYLB");
				zjjgList= DemAPI.getInstance().getFromSubData(Long.parseLong(hashMap.get("LCID").toString()), "SUBFORM_XMZJJG");
			}
			for(Object o : map.keySet()){
				if("PROJECTNAME".equals(o.toString())||"CUSTOMERNAME".equals(o.toString())||"OWNER".equals(o.toString())||"MANAGER".equals(o.toString())||"STARTDATE".equals(o.toString())||"ENDDATE".equals(o.toString())||"KHLXR".equals(o.toString())||"KHLXDH".equals(o.toString())||"GGJZR".equals(o.toString())||"SBJZR".equals(o.toString())||"HTJE".equals(o.toString())||"ZCLR".equals(o.toString())||"ZCLRDH".equals(o.toString())||"FZJGMC".equals(o.toString())||"WBCLRJG".equals(o.toString())||"FZJGLXR".equals(o.toString())){
					if(sheetMap.get(o.toString())==null){
						short colLength2 = (short) (hashMap.get(o.toString()) == null ? 0
								: hashMap.get(o.toString()).toString().length() * 256 * 2);
						sheetMap.put(o.toString(), colLength2);
					}else{
						short length = Short.parseShort(sheetMap.get(o.toString()).toString());
						short colLength2 = (short) (hashMap.get(o.toString()) == null ? 0
								: hashMap.get(o.toString()).toString().length() * 256 * 2);
						if(length<colLength2){
							sheetMap.put(o.toString(), colLength2);
						}
					}
					
				}
			}
			if(pjInstanceid.longValue()!=Integer.valueOf(hashMap.get("LCID").toString()).longValue()){
				beginRow=m;
			}
			if(pjInstanceid.longValue()==Integer.valueOf(hashMap.get("LCID").toString()).longValue()){
				hbRow=beginRow;
			}
			HSSFRow row2 = sheet.createRow((int) m++);
			HSSFCell cell2 = row2.createCell((short) n++);
			if(pjInstanceid.longValue()!=Integer.valueOf(hashMap.get("LCID").toString()).longValue()){
				cell2.setCellValue(hashMap.get("PROJECTNAME")==null?"":hashMap.get("PROJECTNAME").toString());
				cell2.setCellStyle(style4);
				if(Arrays.asList(split).contains("CUSTOMERNAME")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("CUSTOMERNAME")==null?"":hashMap.get("CUSTOMERNAME").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("OWNER")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("OWNER")==null?"":hashMap.get("OWNER").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("MANAGER")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("XMmanager")==null?"":hashMap.get("XMmanager").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("STARTDATE")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("STARTDATE")==null?"":hashMap.get("STARTDATE").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("ENDDATE")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("ENDDATE")==null?"":hashMap.get("ENDDATE").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("KHLXR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("KHLXR")==null?"":hashMap.get("KHLXR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("KHLXDH")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("KHLXDH")==null?"":hashMap.get("KHLXDH").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("GGJZR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("GGJZR")==null?"":hashMap.get("GGJZR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("SBJZR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SBJZR")==null?"":hashMap.get("SBJZR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("HTJE")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("HTJE")==null?"":hashMap.get("HTJE").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("MEMO")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("MEMO")==null?"":hashMap.get("MEMO").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("XMBZ")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("XMBZ")==null?"":hashMap.get("XMBZ").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("ZCLR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("ZCLR")==null?"":hashMap.get("ZCLR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("ZCLRDH")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("ZCLRDH")==null?"":hashMap.get("ZCLRDH").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("FZJGMC")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("FZJGMC")==null?"":hashMap.get("FZJGMC").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("FZJGLXR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("FZJGLXR")==null?"":hashMap.get("FZJGLXR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("WBCLRJG")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("WBCLRJG")==null?"":hashMap.get("WBCLRJG").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("JDMC")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("JDMC")==null?"":hashMap.get("JDMC").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("JDFZR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("MANAGER")==null?"":hashMap.get("MANAGER").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("SSJE")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SSJE")==null?"":hashMap.get("SSJE").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("FILENAME")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("FILENAME")==null?"":hashMap.get("FILENAME").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("SPZT")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SPZT")==null?"":hashMap.get("SPZT").toString());
					cell2.setCellStyle(style4);
				}
			}else{
				cell2.setCellValue("");
				cell2.setCellStyle(style4);
				if(Arrays.asList(split).contains("CUSTOMERNAME")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("OWNER")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("MANAGER")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("STARTDATE")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("ENDDATE")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("KHLXR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("KHLXDH")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("GGJZR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("SBJZR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("HTJE")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("MEMO")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("XMBZ")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("ZCLR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("ZCLRDH")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("FZJGMC")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("FZJGLXR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("WBCLRJG")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("JDMC")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("JDMC")==null?"":hashMap.get("JDMC").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("JDFZR")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("MANAGER")==null?"":hashMap.get("MANAGER").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("SSJE")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SSJE")==null?"":hashMap.get("SSJE").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("FILENAME")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("FILENAME")==null?"":hashMap.get("FILENAME").toString());
					cell2.setCellStyle(style4);
				}
				
				if(Arrays.asList(split).contains("SPZT")){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SPZT")==null?"":hashMap.get("SPZT").toString());
					cell2.setCellStyle(style4);
				}
			}
				
				int x=m;
				int size=1;
				int xgwtSize=0;
				int pjSize=0;
				
				if(Arrays.asList(split).contains("TWUSERNAME")||Arrays.asList(split).contains("QUESTION")||Arrays.asList(split).contains("TWCREATEDATE")||Arrays.asList(split).contains("CONTENT")){
					xgwtSize=xgwtList.size();
				}
				if(Arrays.asList(split).contains("PJNAME")||Arrays.asList(split).contains("PJSJ")||Arrays.asList(split).contains("LDPJ")||Arrays.asList(split).contains("PJSM")){
					pjSize=pjList.size();
				}
				
				int size2=xgwtSize>pjSize?xgwtSize:pjSize;
				
				if(size2>0){
					for(Object o : map.keySet()){
						if("JDMC".equals(o.toString())||"JDFZR".equals(o.toString())||"SSJE".equals(o.toString())||"FILENAME".equals(o.toString())||"SPZT".equals(o.toString())){
							int parseInt = Integer.parseInt(map.get(o).toString());
							sheet.addMergedRegion(new Region(m-1, (short) parseInt, m-1+size2-1, (short) parseInt));
						}
					}
				}
				if(pjInstanceid.longValue()!=Integer.valueOf(hashMap.get("LCID").toString()).longValue()){
					if(size2==0){
						if(Arrays.asList(split).contains("TWUSERNAME")){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(Arrays.asList(split).contains("QUESTION")){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(Arrays.asList(split).contains("TWCREATEDATE")){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(Arrays.asList(split).contains("CONTENT")){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						
						if(Arrays.asList(split).contains("PJNAME")){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(Arrays.asList(split).contains("PJSJ")){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(Arrays.asList(split).contains("LDPJ")){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(Arrays.asList(split).contains("PJSM")){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(Arrays.asList(split).contains("ZJJGMC")||Arrays.asList(split).contains("LXR")||Arrays.asList(split).contains("LXDH")||Arrays.asList(split).contains("LXYX")){
							if(zjjgList.size()>0){
								StringBuffer sb=new StringBuffer();
								int c=1;
									for (HashMap cell : zjjgList) {
										sb.append(""+c+"、");
										if(Arrays.asList(split).contains("ZJJGMC")){
											if(cell.get("ZJJGMC")!=null&&!"".equals(cell.get("ZJJGMC").toString())){
												sb.append(cell.get("ZJJGMC")==null?"":cell.get("ZJJGMC").toString());
											}
										}
										
										if(Arrays.asList(split).contains("LXR")){
											if(cell.get("LXR")!=null&&!"".equals(cell.get("LXR").toString())){
												if(Arrays.asList(split).contains("ZJJGMC")){
													sb.append("==>");
												}
												sb.append(cell.get("LXR")==null?"":cell.get("LXR").toString());
											}
										}
										
										if(Arrays.asList(split).contains("LXDH")){
											if(cell.get("LXDH")!=null&&!"".equals(cell.get("LXDH").toString())){
												if(Arrays.asList(split).contains("ZJJGMC")||Arrays.asList(split).contains("LXR")){
													sb.append("==>");
												}
												sb.append(cell.get("LXDH")==null?"":cell.get("LXDH").toString());
											}
										}
										
										if(Arrays.asList(split).contains("LXYX")){
											if(cell.get("LXYX")!=null&&!"".equals(cell.get("LXYX").toString())){
												if(Arrays.asList(split).contains("ZJJGMC")||Arrays.asList(split).contains("LXR")||Arrays.asList(split).contains("LXDH")){
													sb.append("==>");
												}
												sb.append(cell.get("LXYX")==null?"":cell.get("LXYX").toString());
											}
										}
										sb.append("\r\n");
										c++;
									}
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue(sb.toString());
								cell2.setCellStyle(style6);
							}
						}
						if(Arrays.asList(split).contains("NAME")||Arrays.asList(split).contains("POSITION")||Arrays.asList(split).contains("TEL")||Arrays.asList(split).contains("PHONE")||Arrays.asList(split).contains("EMAIL")||Arrays.asList(split).contains("CYMEMO")){
							if(xmcyList.size()>0){
								StringBuffer sb=new StringBuffer();
								int c=1;
								for (HashMap cell : xmcyList) {
									sb.append(""+c+"、");
									if(Arrays.asList(split).contains("NAME")){
										if(cell.get("NAME")!=null&&!"".equals(cell.get("NAME").toString())){
											sb.append(cell.get("NAME")==null?"":cell.get("NAME").toString());
										}
									}
									
									if(Arrays.asList(split).contains("POSITION")){
										if(cell.get("POSITION")!=null&&!"".equals(cell.get("POSITION").toString())){
											if(Arrays.asList(split).contains("NAME")){
												sb.append("==>");
											}
											sb.append(cell.get("POSITION")==null?"":cell.get("POSITION").toString());
										}
									}
									
									if(Arrays.asList(split).contains("TEL")){
										if(cell.get("TEL")!=null&&!"".equals(cell.get("TEL").toString())){
											if(Arrays.asList(split).contains("NAME")||Arrays.asList(split).contains("POSITION")){
												sb.append("==>");
											}
											sb.append(cell.get("TEL")==null?"":cell.get("TEL").toString());
										}
									}
									
									if(Arrays.asList(split).contains("PHONE")){
										if(cell.get("PHONE")!=null&&!"".equals(cell.get("PHONE").toString())){
											if(Arrays.asList(split).contains("NAME")||Arrays.asList(split).contains("POSITION")||Arrays.asList(split).contains("TEL")){
												sb.append("==>");
											}
											sb.append(cell.get("PHONE")==null?"":cell.get("PHONE").toString());
										}
									}
									
									if(Arrays.asList(split).contains("EMAIL")){
										if(cell.get("EMAIL")!=null&&!"".equals(cell.get("EMAIL").toString())){
											if(Arrays.asList(split).contains("NAME")||Arrays.asList(split).contains("POSITION")||Arrays.asList(split).contains("TEL")||Arrays.asList(split).contains("PHONE")){
												sb.append("==>");
											}
											sb.append(cell.get("EMAIL")==null?"":cell.get("EMAIL").toString());
										}
									}
									if(Arrays.asList(split).contains("CYMEMO")){
										if(cell.get("MEMO")!=null&&!"".equals(cell.get("MEMO").toString())){
											if(Arrays.asList(split).contains("NAME")||Arrays.asList(split).contains("POSITION")||Arrays.asList(split).contains("TEL")||Arrays.asList(split).contains("EMAIL")||Arrays.asList(split).contains("PHONE")){
												sb.append("==>");
											}
											sb.append(cell.get("MEMO")==null?"":cell.get("MEMO").toString());
										}
									}
									sb.append("\r\n");
									c++;
								}
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue(sb.toString());
								cell2.setCellStyle(style6);
							}
						}
					}
				}
				for (int i = 0; i < size2; i++) {
					HSSFRow row3=null;
					int countSize=0;
					if(size>1){
						row3=sheet.createRow((int) x++);
					}
					if(xgwtList.size()>i){
						HashMap xgwtMap = xgwtList.get(i);
						if(size>1){
							for (int j = 0; j <= n; j++) {
								cell2 = row3.createCell((short) j);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
							}
						}
						if(Arrays.asList(split).contains("TWUSERNAME")){
								if(size>1){
									cell2 = row3.createCell((short) n++);
									cell2.setCellValue(xgwtMap.get("TWUSERNAME")==null?"":xgwtMap.get("TWUSERNAME").toString());
									cell2.setCellStyle(style4);
									countSize++;
								}else{
									cell2 = row2.createCell((short) n++);
									cell2.setCellValue(xgwtMap.get("TWUSERNAME")==null?"":xgwtMap.get("TWUSERNAME").toString());
									cell2.setCellStyle(style4);
									countSize++;
								}
						}
						if(Arrays.asList(split).contains("QUESTION")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue(xgwtMap.get("QUESTION")==null?"":xgwtMap.get("QUESTION").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue(xgwtMap.get("QUESTION")==null?"":xgwtMap.get("QUESTION").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						if(Arrays.asList(split).contains("TWCREATEDATE")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue(xgwtMap.get("TWCREATEDATE")==null?"":xgwtMap.get("TWCREATEDATE").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue(xgwtMap.get("TWCREATEDATE")==null?"":xgwtMap.get("TWCREATEDATE").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						if(Arrays.asList(split).contains("CONTENT")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								String content=xgwtMap.get("CONTENT")==null?"":"回复内容："+xgwtMap.get("CONTENT").toString()+",";
								String hfcreatedate=xgwtMap.get("HFCREATEDATE")==null?"":" 回复时间："+xgwtMap.get("HFCREATEDATE").toString()+" , ";
								String hfusername=xgwtMap.get("HFUSERNAME")==null?"":"回复人："+xgwtMap.get("HFUSERNAME").toString();
								String con=content+hfcreatedate+hfusername;
								cell2.setCellValue(con);
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								String content=xgwtMap.get("CONTENT")==null?"":"回复内容："+xgwtMap.get("CONTENT").toString()+",\r\n";
								String hfcreatedate=xgwtMap.get("HFCREATEDATE")==null?"":" 回复时间："+xgwtMap.get("HFCREATEDATE").toString()+" , ";
								String hfusername=xgwtMap.get("HFUSERNAME")==null?"":"回复人："+xgwtMap.get("HFUSERNAME").toString();
								String con=content+hfcreatedate+hfusername;
								cell2.setCellValue(con);
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						
					}else{
						if(Arrays.asList(split).contains("TWUSERNAME")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						if(Arrays.asList(split).contains("QUESTION")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						if(Arrays.asList(split).contains("TWCREATEDATE")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						if(Arrays.asList(split).contains("CONTENT")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
					}
					if(pjList.size()>i){
						HashMap pjMap = pjList.get(i);
						if(Arrays.asList(split).contains("PJNAME")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue(pjMap.get("PJR")==null?"":pjMap.get("PJR").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue(pjMap.get("PJR")==null?"":pjMap.get("PJR").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						if(Arrays.asList(split).contains("PJSJ")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue(pjMap.get("PJSJ")==null?"":pjMap.get("PJSJ").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue(pjMap.get("PJSJ")==null?"":pjMap.get("PJSJ").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						if(Arrays.asList(split).contains("LDPJ")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue(pjMap.get("PJJG")==null?"":pjMap.get("PJJG").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue(pjMap.get("PJJG")==null?"":pjMap.get("PJJG").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						if(Arrays.asList(split).contains("PJSM")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue(pjMap.get("PJSM")==null?"":pjMap.get("PJSM").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue(pjMap.get("PJSM")==null?"":pjMap.get("PJSM").toString());
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
					}else{
						if(Arrays.asList(split).contains("PJNAME")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						if(Arrays.asList(split).contains("PJSJ")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						if(Arrays.asList(split).contains("LDPJ")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
						if(Arrays.asList(split).contains("PJSM")){
							if(size>1){
								cell2 = row3.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}else{
								cell2 = row2.createCell((short) n++);
								cell2.setCellValue("");
								cell2.setCellStyle(style4);
								countSize++;
							}
						}
					}
					if(i==0&&pjInstanceid.longValue()!=Integer.valueOf(hashMap.get("LCID").toString()).longValue()){
						StringBuffer sb=new StringBuffer();
						int c=1;
						for (HashMap cell : zjjgList) {
							sb.append(""+c+"、");
							if(Arrays.asList(split).contains("ZJJGMC")){
								if(cell.get("ZJJGMC")!=null&&!"".equals(cell.get("ZJJGMC").toString())){
									sb.append(cell.get("ZJJGMC")==null?"":cell.get("ZJJGMC").toString());
								}
							}
							
							if(Arrays.asList(split).contains("LXR")){
								if(cell.get("LXR")!=null&&!"".equals(cell.get("LXR").toString())){
									if(Arrays.asList(split).contains("ZJJGMC")){
										sb.append("==>");
									}
									sb.append(cell.get("LXR")==null?"":cell.get("LXR").toString());
								}
							}
							
							if(Arrays.asList(split).contains("LXDH")){
								if(cell.get("LXDH")!=null&&!"".equals(cell.get("LXDH").toString())){
									if(Arrays.asList(split).contains("ZJJGMC")||Arrays.asList(split).contains("LXR")){
										sb.append("==>");
									}
									sb.append(cell.get("LXDH")==null?"":cell.get("LXDH").toString());
								}
							}
							
							if(Arrays.asList(split).contains("LXYX")){
								if(cell.get("LXYX")!=null&&!"".equals(cell.get("LXYX").toString())){
									if(Arrays.asList(split).contains("ZJJGMC")||Arrays.asList(split).contains("LXR")||Arrays.asList(split).contains("LXDH")){
										sb.append("==>");
									}
									sb.append(cell.get("LXYX")==null?"":cell.get("LXYX").toString());
								}
							}
							sb.append("\r\n");
							c++;
						}
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue(sb.toString());
						cell2.setCellStyle(style6);
						countSize++;
					}
					if(i==0&&pjInstanceid.longValue()!=Integer.valueOf(hashMap.get("LCID").toString()).longValue()){
						StringBuffer sb=new StringBuffer();
						int c=1;
						for (HashMap cell : xmcyList) {
							sb.append(""+c+"、");
							if(Arrays.asList(split).contains("NAME")){
								if(cell.get("NAME")!=null&&!"".equals(cell.get("NAME").toString())){
									sb.append(cell.get("NAME")==null?"":cell.get("NAME").toString());
								}
							}
							
							if(Arrays.asList(split).contains("POSITION")){
								if(cell.get("POSITION")!=null&&!"".equals(cell.get("POSITION").toString())){
									if(Arrays.asList(split).contains("NAME")){
										sb.append("==>");
									}
									sb.append(cell.get("POSITION")==null?"":cell.get("POSITION").toString());
								}
							}
							
							if(Arrays.asList(split).contains("TEL")){
								if(cell.get("TEL")!=null&&!"".equals(cell.get("TEL").toString())){
									if(Arrays.asList(split).contains("NAME")||Arrays.asList(split).contains("POSITION")){
										sb.append("==>");
									}
									sb.append(cell.get("TEL")==null?"":cell.get("TEL").toString());
								}
							}
							
							if(Arrays.asList(split).contains("PHONE")){
								if(cell.get("PHONE")!=null&&!"".equals(cell.get("PHONE").toString())){
									if(Arrays.asList(split).contains("NAME")||Arrays.asList(split).contains("POSITION")||Arrays.asList(split).contains("TEL")){
										sb.append("==>");
									}
									sb.append(cell.get("PHONE")==null?"":cell.get("PHONE").toString());
								}
							}
							
							if(Arrays.asList(split).contains("EMAIL")){
								if(cell.get("EMAIL")!=null&&!"".equals(cell.get("EMAIL").toString())){
									if(Arrays.asList(split).contains("NAME")||Arrays.asList(split).contains("POSITION")||Arrays.asList(split).contains("TEL")||Arrays.asList(split).contains("PHONE")){
										sb.append("==>");
									}
									sb.append(cell.get("EMAIL")==null?"":cell.get("EMAIL").toString());
								}
							}
							if(Arrays.asList(split).contains("CYMEMO")){
								if(cell.get("MEMO")!=null&&!"".equals(cell.get("MEMO").toString())){
									if(Arrays.asList(split).contains("NAME")||Arrays.asList(split).contains("POSITION")||Arrays.asList(split).contains("TEL")||Arrays.asList(split).contains("EMAIL")||Arrays.asList(split).contains("PHONE")){
										sb.append("==>");
									}
									sb.append(cell.get("MEMO")==null?"":cell.get("MEMO").toString());
								}
							}
							sb.append("\r\n");
							c++;
						}
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue(sb.toString());
						cell2.setCellStyle(style6);
						countSize++;
					}
					if(size++<size2){
						n-=countSize;
					}
				}
				if(m<x){
					m=x;
				}
			if(n<z){
				n=z;
			}
			z=0;
			n=0;
			if(pjInstanceid.longValue()==Integer.valueOf(hashMap.get("LCID").toString()).longValue()){
				count++;
				if(size2==0){
					size2=1;
				}
				beginSize+=size2;
				hbcount=count;
			}else if(pjInstanceid!=0){
				if(hbcount!=1){
					for(Object o : map.keySet()){
						if("PROJECTNAME".equals(o.toString())||"CUSTOMERNAME".equals(o.toString())||"OWNER".equals(o.toString())||"MANAGER".equals(o.toString())||"STARTDATE".equals(o.toString())||"ENDDATE".equals(o.toString())||"KHLXR".equals(o.toString())||"KHLXDH".equals(o.toString())||"GGJZR".equals(o.toString())||"SBJZR".equals(o.toString())||"HTJE".equals(o.toString())||"MEMO".equals(o.toString())||"XMBZ".equals(o.toString())||"ZCLR".equals(o.toString())||"ZCLRDH".equals(o.toString())||"FZJGMC".equals(o.toString())||"WBCLRJG".equals(o.toString())||"FZJGLXR".equals(o.toString())){
							int parseInt = Integer.parseInt(map.get(o).toString());
							int size3=beginSize-1;
							sheet.addMergedRegion(new Region(hbRow, (short) parseInt, hbRow+size3, (short) parseInt));
						}
					}
					count=1;
					if(Integer.valueOf(hashMap.get("LCID").toString()).longValue()==Integer.valueOf(slist.get(zz).get("LCID").toString()).longValue()){
						if(size2==0){
							size2=1;
						}
						beginSize=size2;
					}else{
						beginSize=0;
					}
					hbcount=1;
				}else if(hbcount==1&&Integer.valueOf(hashMap.get("LCID").toString()).longValue()!=Integer.valueOf(slist.get(zz).get("LCID").toString()).longValue()){
					if(size2==0){
						size2=1;
					}
					beginSize=size2;
					for(Object o : map.keySet()){
							if("PROJECTNAME".equals(o.toString())||"CUSTOMERNAME".equals(o.toString())||"OWNER".equals(o.toString())||"MANAGER".equals(o.toString())||"STARTDATE".equals(o.toString())||"ENDDATE".equals(o.toString())||"KHLXR".equals(o.toString())||"KHLXDH".equals(o.toString())||"GGJZR".equals(o.toString())||"SBJZR".equals(o.toString())||"HTJE".equals(o.toString())||"MEMO".equals(o.toString())||"XMBZ".equals(o.toString())||"ZCLR".equals(o.toString())||"ZCLRDH".equals(o.toString())||"FZJGMC".equals(o.toString())||"WBCLRJG".equals(o.toString())||"FZJGLXR".equals(o.toString())){
								int parseInt = Integer.parseInt(map.get(o).toString());
								int size3=beginSize-1;
								sheet.addMergedRegion(new Region(hbRow, (short) parseInt, hbRow+size3, (short) parseInt));
							}
						}
					beginSize=0;
				}else if(hbcount==1&&Integer.valueOf(hashMap.get("LCID").toString()).longValue()==Integer.valueOf(slist.get(zz).get("LCID").toString()).longValue()){
					if(size2==0){
						size2=1;
					}
					beginSize+=size2;
				}
			}
			pjInstanceid=Integer.valueOf(hashMap.get("LCID").toString());
			if(zz<sListSize-1){
				zz++;
			}else{
				zz=sListSize-1;
			}
		}
		
		if(hbcount!=1){
			for(Object o : map.keySet()){
				if("PROJECTNAME".equals(o.toString())||"CUSTOMERNAME".equals(o.toString())||"OWNER".equals(o.toString())||"MANAGER".equals(o.toString())||"STARTDATE".equals(o.toString())||"ENDDATE".equals(o.toString())||"KHLXR".equals(o.toString())||"KHLXDH".equals(o.toString())||"GGJZR".equals(o.toString())||"SBJZR".equals(o.toString())||"HTJE".equals(o.toString())||"ZCLR".equals(o.toString())||"ZCLRDH".equals(o.toString())||"FZJGMC".equals(o.toString())||"WBCLRJG".equals(o.toString())||"FZJGLXR".equals(o.toString())){
					int parseInt = Integer.parseInt(map.get(o).toString());
					int size3=beginSize-1;
					sheet.addMergedRegion(new Region(hbRow, (short) parseInt, hbRow+size3, (short) parseInt));
				}
			}
		}else if(hbcount==1&&beginSize>1){
			for(Object o : map.keySet()){
				if("PROJECTNAME".equals(o.toString())||"CUSTOMERNAME".equals(o.toString())||"OWNER".equals(o.toString())||"MANAGER".equals(o.toString())||"STARTDATE".equals(o.toString())||"ENDDATE".equals(o.toString())||"KHLXR".equals(o.toString())||"KHLXDH".equals(o.toString())||"GGJZR".equals(o.toString())||"SBJZR".equals(o.toString())||"HTJE".equals(o.toString())||"ZCLR".equals(o.toString())||"ZCLRDH".equals(o.toString())||"FZJGMC".equals(o.toString())||"WBCLRJG".equals(o.toString())||"FZJGLXR".equals(o.toString())){
					int parseInt = Integer.parseInt(map.get(o).toString());
					int size3=beginSize-1;
					sheet.addMergedRegion(new Region(hbRow, (short) parseInt, hbRow+size3, (short) parseInt));
				}
			}
		}
		for(Object o : sheetMap.keySet()){
			if(Short.parseShort(sheetMap.get(o.toString()).toString())>0){
				sheet.setColumnWidth(Short.parseShort(map.get(o.toString()).toString()), Short.parseShort(sheetMap.get(o.toString()).toString()));
			}
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目信息汇总表.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);

		} catch (Exception e) {

			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}

		}
	}

	public boolean saveRz(String projectNo, String groupid, Double rzje) {
		Map<String,String> config=ConfigUtil.readAllProperties("/strings_zh-CN.properties");//获取连接网址配置
		String string = config.get("string_count");
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		List<HashMap> list = DemAPI.getInstance().getList(ProjectItemUUID,
				conditionMap, "ID");
		boolean isNull=false;
		int count=0;
		String groupidjl="";
		if("1".equals(string)){
			Map params = new HashMap();
			params.put(1, groupid);
			for (HashMap hashMap : list) {
				int fs = DBUTilNew.getInt("SFKRZ","SELECT SFKRZ FROM BD_ZQB_KM_INFO WHERE ID= ? ",params );
				if(fs==1){
					groupidjl=hashMap.get("GROUPID").toString();
				}
				if(hashMap.get("GROUPID").toString().equals(groupid)&&fs==1){
					Double parseDouble = "".equals(hashMap.get("SSJE").toString())==true?0.0:Double.parseDouble(hashMap.get("SSJE").toString().trim());
					if((hashMap.get("SSJE")==null||"".equals(hashMap.get("SSJE").toString()))||parseDouble.intValue()==0){
						hashMap.put("SSJE", rzje);
						DemAPI.getInstance().updateFormData(ProjectItemUUID, Long.parseLong(hashMap.get("INSTANCEID").toString()),
								hashMap, Long.parseLong(hashMap.get("ID").toString()), false);
						return true;
					}else{
						isNull=true;
					}
				}else if(isNull){
					Double parseDouble = "".equals(hashMap.get("SSJE").toString())==true?0.0:Double.parseDouble(hashMap.get("SSJE").toString().trim());
					if(((hashMap.get("SSJE")==null||"".equals(hashMap.get("SSJE").toString()))||parseDouble.intValue()==0)&&fs==1){
						count++;
						hashMap.put("SSJE", rzje);
						DemAPI.getInstance().updateFormData(ProjectItemUUID, Long.parseLong(hashMap.get("INSTANCEID").toString()),
								hashMap, Long.parseLong(hashMap.get("ID").toString()), false);
						return true;
					}
				}
			}
			if(count==0){
				HashMap map2 = new HashMap();
				map2.put("PROJECTNO", projectNo);
				map2.put("GROUPID", groupidjl);
				List<HashMap> list2 = DemAPI.getInstance().getList(ProjectItemUUID,
						map2, "ID");
				HashMap hashMap = list2.get(0);
				Double parseDouble = "".equals(hashMap.get("SSJE").toString())==true?0.0:Double.parseDouble(hashMap.get("SSJE").toString().trim());
				if((hashMap.get("SSJE")==null||"".equals(hashMap.get("SSJE").toString()))||parseDouble.intValue()==0){
					count++;
					hashMap.put("SSJE", rzje);
				}else{
					count++;
					hashMap.put("SSJE", Double.parseDouble(hashMap.get("SSJE").toString().trim())+rzje);
				}
				DemAPI.getInstance().updateFormData(ProjectItemUUID, Long.parseLong(hashMap.get("INSTANCEID").toString()),
							hashMap, Long.parseLong(hashMap.get("ID").toString()), false);
				return true;
			}
		}else{
			for (HashMap hashMap : list) {
				if(hashMap.get("GROUPID").toString().equals(groupid)){
					Double parseDouble = "".equals(hashMap.get("SSJE").toString())==true?0.0:Double.parseDouble(hashMap.get("SSJE").toString().trim());
					if((hashMap.get("SSJE")==null||"".equals(hashMap.get("SSJE").toString()))||parseDouble.intValue()==0){
						hashMap.put("SSJE", rzje);
						DemAPI.getInstance().updateFormData(ProjectItemUUID, Long.parseLong(hashMap.get("INSTANCEID").toString()),
								hashMap, Long.parseLong(hashMap.get("ID").toString()), false);
						return true;
					}else{
						isNull=true;
					}
				}else if(isNull){
					Double parseDouble = "".equals(hashMap.get("SSJE").toString())==true?0.0:Double.parseDouble(hashMap.get("SSJE").toString().trim());
					if((hashMap.get("SSJE")==null||"".equals(hashMap.get("SSJE").toString()))||parseDouble.intValue()==0){
						count++;
						hashMap.put("SSJE", rzje);
						DemAPI.getInstance().updateFormData(ProjectItemUUID, Long.parseLong(hashMap.get("INSTANCEID").toString()),
								hashMap, Long.parseLong(hashMap.get("ID").toString()), false);
						return true;
					}
				}
			}
			if(count==0){
				if(list.size()>0){
					HashMap hashMap = list.get(list.size()-1);
					Double parseDouble = "".equals(hashMap.get("SSJE").toString())==true?0.0:Double.parseDouble(hashMap.get("SSJE").toString().trim());
					if((hashMap.get("SSJE")==null||"".equals(hashMap.get("SSJE").toString()))||parseDouble.intValue()==0){
						count++;
						hashMap.put("SSJE", rzje);
					}else{
						count++;
						hashMap.put("SSJE", Double.parseDouble(hashMap.get("SSJE").toString().trim())+rzje);
					}
					DemAPI.getInstance().updateFormData(ProjectItemUUID, Long.parseLong(hashMap.get("INSTANCEID").toString()),
							hashMap, Long.parseLong(hashMap.get("ID").toString()), false);
					return true;
				}
			}
		}
		
		return count==0?false:true;
	}

	public Double getRzList(String projectNo, String groupid) {
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		conditionMap.put("GROUPID", groupid);
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		List<HashMap> list = DemAPI.getInstance().getList(ProjectItemUUID,
				conditionMap, "ID");
		return "".equals(list.get(0).get("SSJE").toString())==true?0.0:Double.parseDouble(list.get(0).get("SSJE").toString().trim());
	}

	public boolean editRz(String projectNo, String groupid, Double rzje) {
		Map<String,String> config=ConfigUtil.readAllProperties("/strings_zh-CN.properties");//获取连接网址配置
		String string = config.get("string_count");
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		conditionMap.put("GROUPID", groupid);
		String ProjectItemUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		List<HashMap> list = DemAPI.getInstance().getList(ProjectItemUUID,
				conditionMap, "ID");
		if("1".equals(string)){
			Map params = new HashMap();
			params.put(1, groupid);
			for (HashMap hashMap : list) {
				if(DBUTilNew.getInt("SFKRZ","SELECT SFKRZ FROM BD_ZQB_KM_INFO WHERE ID= ? ",params)==1){
				hashMap.put("SSJE", rzje);
				DemAPI.getInstance().updateFormData(ProjectItemUUID, Long.parseLong(hashMap.get("INSTANCEID").toString()),
						hashMap, Long.parseLong(hashMap.get("ID").toString()), false);
				return true;
				}
			}
		}else{
			for (HashMap hashMap : list) {
				hashMap.put("SSJE", rzje);
				DemAPI.getInstance().updateFormData(ProjectItemUUID, Long.parseLong(hashMap.get("INSTANCEID").toString()),
						hashMap, Long.parseLong(hashMap.get("ID").toString()), false);
				return true;
			}
		}
		return false;
	}

	public List<HashMap> showDailyList(String projectNo, int pageNumber, int pageSize,String startdate,String enddate, Long formid) {
		return zqbGpfxProjectManageDAO.getShowDailyList(projectNo,pageNumber,pageSize,startdate,enddate,formid);
	}

	public int showDailyListSize(String projectNo,String startdate,String enddate) {
		Map params = new HashMap();
		int n=1;
		StringBuffer sb=new StringBuffer();
		sb.append("select count (*) count from (select projectdate,progress,username,tel,tracking,projectno from bd_zqb_xmrbb where projectno= ? ");
		params.put(n, projectNo);
		n++;
		if (startdate != null && !"".equals(startdate)) {
			sb.append(" and to_char(projectdate,'yyyy-MM-dd')>=  ? ");
			params.put(n, startdate);
			n++;
		}
		if (enddate != null && !"".equals(enddate)) {
			sb.append(" and to_char(projectdate,'yyyy-MM-dd')<=  ? ");
			params.put(n, enddate);
		}
		sb.append(")");
		int count=DBUTilNew.getInt("count",sb.toString(),params);
		return count;
	}

	public void expDaily(HttpServletResponse response,String projectNo) {
		List<HashMap> dailyList = this.getDaily(projectNo);
		List<HashMap> dailyListSize = this.getDailySize(projectNo);
		HashMap sheetMap=new HashMap();
		sheetMap.put("1", 0);
		sheetMap.put("2", 0);
		sheetMap.put("3", 0);
		sheetMap.put("4", 0);
		sheetMap.put("5", 0);
		sheetMap.put("6", 0);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目日报");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor((short) 22);
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 1);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillForegroundColor((short) 22);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBorderLeft((short) 1);
		style6.setBorderRight((short) 1);
		style6.setBorderTop((short) 1);
		style6.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//style5.setFillForegroundColor(HSSFColor.LIME.index);
		style5.setFillForegroundColor((short) 22);
		//style5.setFillForegroundColor(HSSFColor.WHITE.index);
		style5.setFillPattern((short) 1);
		
		int m=0;
		int z=0;
		int n=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell cell1 = row1.createCell((short) z++);
		cell1.setCellValue("项目名称");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("日报日期");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("进展情况");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("对方联系人");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("联系电话");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("跟踪进度");
		cell1.setCellStyle(style5);
		for (HashMap map : dailyList) {
			HSSFRow row2 = sheet.createRow((int) m++);
			HSSFCell cell2 = row2.createCell((short) n++);
			cell2.setCellValue(map.get("PROJECTNAME")==null?"":map.get("PROJECTNAME").toString());
			cell2.setCellStyle(style4);
			short colLength2 = (short) (map.get("PROJECTNAME") == null ? 0
					: map.get("PROJECTNAME").toString().length() * 256 * 2);
			sheetMap.put("1", colLength2>Short.parseShort(sheetMap.get("1").toString())?colLength2:Short.parseShort(sheetMap.get("1").toString()));
			cell2 = row2.createCell((short) n++);
			cell2.setCellValue(map.get("PROJECTDATE")==null?"":map.get("PROJECTDATE").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("PROJECTDATE") == null ? 0
					: map.get("PROJECTDATE").toString().length() * 256 * 2);
			sheetMap.put("2", colLength2>Short.parseShort(sheetMap.get("2").toString())?colLength2:Short.parseShort(sheetMap.get("2").toString()));
			cell2 = row2.createCell((short) n++);
			cell2.setCellValue(map.get("PROGRESS")==null?"":map.get("PROGRESS").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("PROGRESS") == null ? 0
					: map.get("PROGRESS").toString().length() * 256 * 2);
			sheetMap.put("3", colLength2>Short.parseShort(sheetMap.get("3").toString())?colLength2:Short.parseShort(sheetMap.get("3").toString()));
			cell2 = row2.createCell((short) n++);
			cell2.setCellValue(map.get("USERNAME")==null?"":map.get("USERNAME").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("USERNAME") == null ? 0
					: map.get("USERNAME").toString().length() * 256 * 2);
			sheetMap.put("4", colLength2>Short.parseShort(sheetMap.get("4").toString())?colLength2:Short.parseShort(sheetMap.get("4").toString()));
			cell2 = row2.createCell((short) n++);
			cell2.setCellValue(map.get("TEL")==null?"":map.get("TEL").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("TEL") == null ? 0
					: map.get("TEL").toString().length() * 256 * 2);
			sheetMap.put("5", colLength2>Short.parseShort(sheetMap.get("5").toString())?colLength2:Short.parseShort(sheetMap.get("5").toString()));
			cell2 = row2.createCell((short) n++);
			cell2.setCellValue(map.get("TRACKING")==null?"":map.get("TRACKING").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("TRACKING") == null ? 0
					: map.get("TRACKING").toString().length() * 256 * 2);
			sheetMap.put("6", colLength2>Short.parseShort(sheetMap.get("6").toString())?colLength2:Short.parseShort(sheetMap.get("6").toString()));
			n=0;
		}
		int h=1;
		for (HashMap hashMap : dailyListSize) {
			Integer count = Integer.parseInt(hashMap.get("COUNT").toString());
			sheet.addMergedRegion(new Region(h, (short) 0, h-1+count, (short) 0));
			h+=count;
		}
		for(Object o : sheetMap.keySet()){
			if(Short.parseShort(sheetMap.get(o.toString()).toString())>0){
				sheet.setColumnWidth(Short.parseShort(o.toString())-1, Short.parseShort(sheetMap.get(o.toString()).toString()));
			}
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目日报表.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
	}
	
	public List<HashMap> getDaily(String projectNo){
		boolean superman = this.getIsSuperMan();
		List<String> tmplist = new ArrayList<String>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		/*if (superman) {
			tmplist = zqbGpfxProjectManageDAO.getDailyRunProjectList();
		} else {
			tmplist = zqbGpfxProjectManageDAO.getDailyRunProjectList1(owner);
		}*/
		tmplist.add(projectNo);
		List<HashMap> list = new ArrayList<HashMap>();
		list=zqbGpfxProjectManageDAO.getDaily(tmplist);
		return list;
	}
	
	public List<HashMap> getDailySize(String projectNo){
		boolean superman = this.getIsSuperMan();
		List<String> tmplist = new ArrayList<String>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		/*if (superman) {
			tmplist = zqbGpfxProjectManageDAO.getDailyRunProjectList();
		} else {
			tmplist = zqbGpfxProjectManageDAO.getDailyRunProjectList1(owner);
		}*/
		tmplist.add(projectNo);
		List<HashMap> list = new ArrayList<HashMap>();
		list=zqbGpfxProjectManageDAO.getDailySize(tmplist);
		return list;
	}

	public Long getFormid() {
		return DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
	}

	public Long getDemId() {
		return DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "ID");
	}
	
	public Long getTYFormid(String str) {
		Map params = new HashMap();
		params.put(1,str);
		return DBUTilNew.getLong( "FORMID","SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE= ? ",params);
	}

	public Long getTYDemId(String str) {
		Map params = new HashMap();
		params.put(1,str);
		return DBUTilNew.getLong("ID","SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE= ? ",params );
	}
	
	public String loadGpfxProject(String projectno) {
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目任务'", "FORMID");
		StringBuffer sql = new StringBuffer(
				"SELECT A.INSTANCEID,A.PROJECTNO,NVL(A.LCBS,'') LCBS, "
						+ "NVL(A.LCBH,'') LCBH, NVL(A.STEPID,'') STEPID,NVL(A.YXID,'') YXID,"
						+ " NVL(A.RWID,'') RWID, NVL(B.JDMC,'') TASK_NAME,NVL(TO_CHAR(A.GXSJ,'YYYY-MM-DD HH24:MI'),'') GXSJ,NVL(A.SPZT,'') SPZT,"
						+ " NVL(A.MANAGER,'') MANAGER,MBNUM,B.ID JDBH"
						+ " ,MBNAME,MB,D.JDZL,ZLNAME"
						+ " FROM BD_ZQB_TYXM_INFO B  LEFT JOIN"
						+ " (SELECT * FROM BD_ZQB_GPFXXMRWB BOTABLE INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID"
						+ " AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = ? WHERE PROJECTNO=?) A"
						+ " ON B.ID = A.GROUPID"
						+ " LEFT JOIN (SELECT JDBH,COUNT(*) MBNUM FROM BD_ZQB_TYXMZLB GROUP BY JDBH) C"
						+ " ON B.ID = C.JDBH"
						+ " LEFT JOIN  (SELECT A.ID,A.JDBH,D.FILE_SRC_NAME MBNAME,A.JDZL MB,B.JDZL,C.FILE_SRC_NAME ZLNAME"
						+ " FROM BD_ZQB_TYXMZLB A LEFT JOIN (SELECT * FROM BD_ZQB_TYXMRWZLB WHERE PROJECTNO=?) B"
						+ " ON  A.JDBH = B.JDBH AND A.JDZL = B.SXZL "
						+ " LEFT JOIN SYS_UPLOAD_FILE C ON B.JDZL = C.FILE_ID"
						+ " LEFT JOIN SYS_UPLOAD_FILE D ON A.JDZL = D.FILE_ID ) D"
						+ " ON B.ID = D.JDBH" + " WHERE B.STATE=1 ORDER BY B.SORTID,MB");
		StringBuffer sqlZL = new StringBuffer();
		sqlZL.append("select a.id,a.jdbh,d.FILE_SRC_NAME mbName,a.jdzl mb,b.jdzl,c.FILE_SRC_NAME ZLName from BD_ZQB_TYXMZLB a left join (select * from BD_ZQB_TYXMRWZLB where projectno=?) b on  a.jdbh = b.jdbh and a.jdzl = b.sxzl left join sys_upload_file c on b.jdzl = c.file_id left join sys_upload_file d on a.jdzl = d.file_id order by a.jdbh,a.jdzl");
		StringBuffer content = new StringBuffer();
		content.append("<table class=\"ui-jqgrid-htable ke-zeroborder\" role=\"grid\""
				+ " aria- labelledby=\"gbox_subformSUBFORM_BGSRZXX\"           "
				+ " style=\"width:850px;\" border=\"0\" cellpadding=\"0\"      "
				+ " cellspacing=\"0\">"
				+ " <thead>           "
				+ " 	<tr class=\"ui-jqgrid-labels\" role=\"rowheader\">       "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_rn\" role=\"columnheader\"             "
				+ " 			style=\"width:15px;\">                               "
				+ " 			<div id=\"jqgh_subformSUBFORM_BGSRZXX_rn\">          "
				+ " 				<span class=\"s-ico\" style=\"display:none;\"><span"
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\"      "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div> <br />                                        "
				+ " 		</th>         "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZLX\" role=\"columnheader\"           "
				+ " 			style=\"width:50px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZLX\">           "
				+ " 				阶段名称<span class=\"s-ico\" style=\"display:none;\"><span      "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_ZW\" role=\"columnheader\"             "
				+ " 			style=\"width:50px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_ZW\">             "
				+ " 				阶段负责人<span class=\"s-ico\" style=\"display:none;\"><span    "
				+ " 				class=\"ui-grid-ico-sort ui-icon-asc ui-state-     "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZQSRQ\" role=\"columnheader\"         "
				+ " 			style=\"width:100px;\"><span                         "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZQSRQ\">         "
				+ " 				所需资料模板<span class=\"s-ico\" style=\"display:none;\"><span  "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "

				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZZT\" role=\"columnheader\"           "
				+ " 			style=\"width:100px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZZT\">           "
				+ " 				上传资料<span class=\"s-ico\" style=\"display:none;\"><span        "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_LRQX\" role=\"columnheader\"           "
				+ " 			style=\"width:40px;\"><span                          "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_LRQX\">           "
				+ " 				状态<span class=\"s-ico\" style=\"display:none;\"><span          "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "                         "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\"  "
				+ " 			id=\"subformSUBFORM_BGSRZXX_RZJSRQ\" role=\"columnheader\"         "
				+ " 			style=\"width:40px;\"><span                         "
				+ " 			class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\"      "
				+ " 			style=\"cursor:col-resize;\">&nbsp;</span>           "
				+ " 			<div class=\"ui-jqgrid-sortable\"                    "
				+ " 				id=\"jqgh_subformSUBFORM_BGSRZXX_RZJSRQ\">         "
				+ " 				操作<span class=\"s-ico\" style=\"display:none;\"><span          "
				+ " class=\"ui-grid-ico-sort ui-icon-asc ui-state-   "
				+ "disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"                             "
				+ " sort=\"asc\"></span><span                        "
				+ " class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-                                "
				+ "triangle-1-s ui-sort-ltr\"             "
				+ " sort=\"desc\"></span></span>                     "
				+ " 			</div></th> "
				+ " 	</tr>           "
				+ " </thead>          " + "										</table>            ");
		content.append("<div style=\"height: 100%; width: 850px;\" class=\"ui-jqgrid-bdiv\">     "
				+ "           <div style=\"position:relative;\"><div>                       "
				+ "</div>            "
				+ "<table style=\"width: 850px;\" class=\"ui-jqgrid-btable\" aria-labelledby=\"gbox_subformSUBFORM_SXCYRY\" aria-multiselectable=\"true\"        "
				+ "                  "
				+ "role=\"grid\" tabindex=\"1\" id=\"subformSUBFORM_SXCYRY\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
				+ "<tbody>           ");
		XmlcConf conf = SystemConfig._xmlcConf;
		String config = conf.getConfig();
		// 如果配置为1，则审批节点为初审负责人-》复审负责人-》领导审批
		// 如果配置为2，则审批节点为项目负责人-》质控部负责人1-》质控部负责人2
		String pro = config.equals("1") ? "GPFXXMRWLC" : "DGPFXXMRWLC";
		String xmlcServer = ProcessAPI.getInstance().getProcessActDefId(pro);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rsZL = null;
		HashMap hashZLNum = new HashMap();
		List listMB = new ArrayList();
		List listJD = new ArrayList();
		try {
			stmt =conn.prepareStatement(sqlZL.toString());
			stmt.setString(1, projectno);
			rsZL = stmt.executeQuery();
			int i = 0;
			if (rsZL != null) {
				while (rsZL.next()) {
					String jdbh = rsZL.getString("jdbh") == null ? "" : rsZL
							.getString("jdbh");
					String mb = rsZL.getString("mb") == null ? "" : rsZL
							.getString("mb");
					// 循环查出每个模板的资料数量
					if (hashZLNum.containsKey(jdbh + "_" + mb)) {
						hashZLNum.put(jdbh + "_" + mb, Integer
								.parseInt(hashZLNum.get(jdbh + "_" + mb)
										.toString()) + 1);
					} else {
						hashZLNum.put(jdbh + "_" + mb, 1);
					}
				}
			}
			stmt =conn.prepareStatement(sql.toString());
			stmt.setLong(1, gpfxFormId);
			stmt.setString(2, projectno);
			stmt.setString(3, projectno);
			ResultSet rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					int jdbh = rs.getInt("JDBH");
					int mbNum = rs.getInt("MBNUM");
					int num = 0;
					if (rsZL != null) {
						if (!hashZLNum.isEmpty()) {
							Iterator iter = hashZLNum.entrySet().iterator();
							while (iter.hasNext()) {
								Map.Entry entry = (Map.Entry) iter.next();
								String strKey = entry.getKey().toString();
								if (strKey.indexOf(jdbh + "_") < 0) {
									continue;
								}
								int iZLNum = Integer.parseInt(entry.getValue()
										.toString());
								num += iZLNum;
							}
						}
					}
					if (num == 0) {
						num = mbNum;
					}
					Long instanceid = rs.getLong("INSTANCEID");
					String lcbs = rs.getString("LCBS") == null ? "" : rs
							.getString("LCBS");
					String stepid = rs.getString("STEPID") == null ? "" : rs
							.getString("STEPID");
					String rwid = rs.getString("RWID") == null ? "" : rs
							.getString("RWID");
					String taskname = rs.getString("TASK_NAME") == null ? ""
							: rs.getString("TASK_NAME");
					String manager = rs.getString("MANAGER") == null ? "" : rs
							.getString("MANAGER");
					String gxsj = rs.getString("GXSJ") == null ? "" : rs
							.getString("GXSJ");
					String spzt = rs.getString("SPZT") == null ? "" : rs
							.getString("SPZT");
					String projectnu = rs.getString("PROJECTNO") == null ? ""
							: rs.getString("PROJECTNO");
					String lcbh = rs.getString("LCBH") == null ? "" : rs
							.getString("LCBH");
					String yxid = rs.getString("YXID") == null ? "" : rs
							.getString("YXID");
					Long prcid = Long.parseLong("0");
					if (lcbs != null && !lcbs.equals("")) {
						if (lcbh != null) {
							try {
								List<ProcessRuOpinion> prolist = ProcessAPI
										.getInstance().getProcessOpinionList(
												lcbh, Long.parseLong(lcbs));
								if (prolist != null & prolist.size() > 0) {
									prcid = prolist.get(0).getPrcDefId();
								}
							} catch (Exception e) {
								logger.error(e,e);
							}
						}
					}

					if (stepid != null && !stepid.equals("")) {

						List<Task> tasklist = ProcessAPI.getInstance()
								.getUserProcessTaskList(xmlcServer, stepid,
										userid);
						List<Task> list1 = (tasklist == null ? new ArrayList<Task>()
								: tasklist);
						for (Task task : list1) {
							if (Long.parseLong(task.getProcessInstanceId()) == Long
									.parseLong(lcbs)
									&& Long.parseLong(task.getExecutionId()) == Long
											.parseLong(lcbs)) {
								String taskid = task.getId();
								rwid = taskid;
								break;
							}
						}
					} else {
						rwid = "";
					}
					String url = "loadProcessFormPage.action?actDefId=" + lcbh
							+ "&instanceId=" + lcbs + "&excutionId=" + lcbs
							+ "&taskId=" + rwid + "&PROJECTNO=" + projectnu
							+ "&GROUPID=" + jdbh;
					content.append(" <tr aria-selected=\"true\" id=\"29\" tabindex=\"-1\" class=\"ui-widget-content jqgrow ui-row-ltr \">");

					if (listJD.indexOf(jdbh) < 0) {
						i = i + 1;
						content.append("<td role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" class=\"ui-state-default jqgrid-rownum\" style=\"text-align:center;width:25px;\" title=\""
								+ i
								+ "\" aria-describedby=\"subformSUBFORM_XMBASE_rn\">"
								+ i
								+ "</td>              "
								+ "<td role=\"gridcell\"   rowspan=\""
								+ num
								+ "\" style=\"width:74px;\" title=\""
								+ taskname
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_USERID\"><xmp>"
								+ taskname
								+ "</xmp></td>                 "
								+ "<td role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" style=\"width:74px;\" title=\""
								+ manager
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\"><xmp>"
								+ manager
								+ "</xmp></td>");
					}
					StringBuffer strPart = new StringBuffer();
					String mb = rs.getString("mb") == null ? "" : rs
							.getString("mb");
					String mbName = rs.getString("mbName") == null ? "" : rs
							.getString("mbName");
					String ZLName = rs.getString("ZLName") == null ? "" : rs
							.getString("ZLName");
					String jdzl = rs.getString("jdzl") == null ? "" : rs
							.getString("jdzl");
					int zlNum = 1;
					if (hashZLNum.containsKey(jdbh + "_" + mb)) {
						zlNum = Integer.parseInt(hashZLNum.get(jdbh + "_" + mb)
								.toString());
					}
					// 资料模板
					if (listMB.indexOf(jdbh + "_" + mb) < 0) {

						content.append("<td role=\"gridcell\" style=\"width:146px;\" rowspan=\""
								+ zlNum
								+ "\" title=\""
								+ mbName
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_PHONE\">"
								+ "<span><a href=\"uploadifyDownload.action?fileUUID="
								+ mb
								+ "\" target=\"_blank\"><xmp>"
								+ mbName
								+ "</xmp></a></span></td> ");

						listMB.add(jdbh + "_" + mb);
					}
					try {
						content.append("<td role=\"gridcell\" style=\"width:146px;\" title=\""
								+ ZLName
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_EMAIL\"> <span><a href=\"uploadifyDownload.action?fileUUID="
								+ jdzl
								+ "\" target=\"_blank\"><xmp>"
								+ substring(ZLName, 45, "UTF-8")
								+ (ZLName.length() > 42 ? "..." : "")
								+ "</xmp></a></span></td>");
					} catch (UnsupportedEncodingException e) {
						logger.error(e,e);
					}
					if (listJD.indexOf(jdbh) < 0) {
						listJD.add(jdbh);
						content.append("<td role=\"gridcell\"  rowspan=\""
								+ num
								+ "\"  style=\"width:60px;\" title=\""
								+ spzt
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_EMAIL\"><a href=\"javascript:readLc('"
								+ lcbh
								+ "','"
								+ lcbs
								+ "','"
								+ yxid
								+ "',"
								+ "'"
								+ rwid
								+ "','"
								+ prcid
								+ "','"
								+ stepid
								+ "')\">"
								+ spzt
								+ "</a></td>                        "
								+ "<td  role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" style=\"width:60px;align:center\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_MEMO\">"
								+ (spzt != null && spzt.equals("") ? "<a href=\"javascript:addTask('"
										+ jdbh + "')\">上传</a>"
										: (spzt.equals("未提交")
												|| spzt.equals("驳回") ? "<a href=\"javascript:tj('"
												+ url + "')\">"+(spzt.equals("未提交")&&"0".equals(rwid)?"":"编辑")+"</a>"
												: "<a href=\"javascript:loadItem("
												+ instanceid
												+ ",'"
												+ jdbh
												+ "'"
												+ ")\">查看</a>"))
												+ "</td>");
					}
					content.append("</tr>");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rsZL);
		}
		content.append("</tbody>          " + "</table>          "
				+ "</div>            " + "</div>            ");
		return content.toString().replace("rowspan=\"0\"", "");
	}

	public String checkXMPJ(String groupid, String pjsm, String projectno,
			String pjr,Long instanceid) {
		String info="";
		if(instanceid==0){
			String PROJECT_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行评价表单'", "UUID");
			HashMap conditionMap = new HashMap();
			conditionMap.put("PROJECTNO", projectno);
			conditionMap.put("GROUPID", groupid);
			conditionMap.put("PJR", pjr);
			List<HashMap> list = DemAPI.getInstance().getList(PROJECT_UUID,
					conditionMap, "ID");
			if(list.size()>0){
				info="该阶段您已评价，不可以重复评价。";
			}else{
				String PROJECT_TASK_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
				HashMap map2 = new HashMap();
				map2.put("PROJECTNO", projectno);
				map2.put("GROUPID", groupid);
				List<HashMap> list2 = DemAPI.getInstance().getList(PROJECT_TASK_UUID,
						map2, "ID");
				if(list2.size()==0){
					info="该阶段没有信息，无法添加评价信息。";
				}
			}
		}
		return info;
	}
	
	public List<HashMap<String,String>> getOrgUserMapList(Long deptId,OrgUser user){
		List<HashMap<String,String>> maplist = new ArrayList();
		String userid = user.getUserid();
		List<OrgDepartment> subDepartmentList = OrganizationAPI.getInstance().getSubDepartmentList("2", deptId);
		for (OrgDepartment orgDepartment : subDepartmentList) {
			List<HashMap<String,String>> orgUserMapList = OrganizationAPI.getInstance().getOrgUserMap(orgDepartment.getId(),userid);
			List<HashMap<String,String>> subListOrguser = this.getOrgUserMapList(orgDepartment.getId(),user);
			if( subListOrguser==null) subListOrguser = new ArrayList();
				for (HashMap<String, String> hashMap : orgUserMapList) {
					subListOrguser.add(hashMap);
				}
			maplist.addAll(subListOrguser);
		}
		return maplist;
	}
	
	public HashMap<String,List<String>> getDepartmentUserList(){
		HashMap<String,List<String>> listMap=new HashMap<String,List<String>>();
		List<Long> deptList=new ArrayList<Long>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		List<OrgUserMap> userMapList = uc._userMapList;
		for (OrgUserMap orgUserMap : userMapList) {
			String ismanager = orgUserMap.getIsmanager();
			if(ismanager!=null&&ismanager.equals("1")){
				String departmentid = orgUserMap.getDepartmentid();
				deptList.add(Long.parseLong(departmentid));
			}
		}
		Long ismanager = userModel.getIsmanager();
		if(ismanager==1){
			Long departmentid = userModel.getDepartmentid();
			deptList.add(departmentid);
		}
		List<String> owner=new ArrayList<String>();
		List<String> userid=new ArrayList<String>();
		List<String> username=new ArrayList<String>();
		String userId = userModel.getUserid();
		String userName = userModel.getUsername();
		owner.add(userId+"["+userName+"]");
		userid.add(userId);
		username.add(userName);
		for (Long deptId : deptList) {
			List<HashMap<String,String>> orgUserMapList = OrganizationAPI.getInstance().getOrgUserMap(deptId,userId);
			List<HashMap<String,String>> getOrgUserMap = getOrgUserMapList(deptId,userModel);
			if(!getOrgUserMap.isEmpty()){
				orgUserMapList.addAll(getOrgUserMap);
			}
			for (HashMap<String, String> hashMap : orgUserMapList) {
				String uid = hashMap.get("USERID").toString();
				String uname = hashMap.get("USERNAME").toString();
				String ownerTemp = uid+"["+uname+"]";
				owner.add(ownerTemp);
				userid.add(uid);
				username.add(uname);
			}
		}
		listMap.put("OWNER", owner);
		listMap.put("USERID", userid);
		listMap.put("USERNAME", username);
		return listMap;
	}
	
	public HashMap<String,String> getParameterMap(HashMap<String,String> map, HashMap<String,List<String>> parameterMap){
		HashMap<String,String> hashMap=new HashMap<String,String>();
		if(parameterMap!=null&&!parameterMap.isEmpty()){
			for (String key : map.keySet()) {
				List<String> list = parameterMap.get(map.get(key));
				StringBuffer text=new StringBuffer();
				for (int i = 0; i < list.size(); i++) {
					if (i == (list.size() - 1)) {  
						text.append("'"+list.get(i)+"'"); 
					}else if((i%999)==0 && i>0){  
						text.append("'"+list.get(i)+"'").append(") or "+key+" in ("); 
					}else{  
						text.append("'"+list.get(i)+"'").append(",");
					}  
				}
				hashMap.put(key, text.toString());
			}
		}
		return hashMap;
	}

	public HashMap<String,Object> getStrMap(HashMap<String,String> map,HashMap<String,String> parametermap, Long gpfxFormId){
		HashMap<String,Object> hashMap=new HashMap<String,Object>();
		List<Object> list=new ArrayList<Object>();
		Set<String> keySet = map.keySet();
		int size = 0;
		for (String key : keySet) {
			list.add(gpfxFormId);
			StringBuffer str=new StringBuffer();
			String[] split = parametermap.get(key).toString().split(",");
			for (int i=0;i < split.length; i++) {
				if(split[i]!=null&&!"".equals(split[i].toString())){
					if (i == (split.length - 1)){
						str.append("?");
					}else if((i%999)==0 && i>0){
						str.append("?").append(") or "+key+" in ("); 
					}else{
						str.append("?").append(",");
					}
					list.add(split[i].toString().replace("'",""));
				}
			}
			hashMap.put(key, str.toString());
		}
		hashMap.put("list", list);
		return hashMap;
	}
	
	public Long getIsManager(UserContext uc) {
		Long isManager=0L;
		OrgUser userModel = uc.get_userModel();
		Long ismanager = userModel.getIsmanager();
		if(ismanager==1){
			isManager=ismanager;
		}else{
			List<OrgUserMap> userMapList = uc.get_userMapList();
			for (OrgUserMap orgUserMap : userMapList) {
				if(orgUserMap.getIsmanager()==null){
					ismanager = 0l;
				}else{
					ismanager = Long.parseLong(orgUserMap.getIsmanager());
				}
				if(ismanager==1){
					isManager=ismanager;
				}
				break;
			}
		}
		return isManager;
	}
	public String gpfxproAssociate(String customerno){
		List<HashMap> items = zqbGpfxProjectManageDAO.gpfxproAssociate(customerno);
		StringBuffer html = new StringBuffer();
		html.append("<tr width='90%'>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#004080;border-right:0px;' width='4%'>");
				html.append("选择");
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#004080;border-right:0px;' width='8%'>");
				html.append("定增时间");
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#004080;border-right:0px;' width='8%'>");
				html.append("立项日期");
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#004080;border-right:0px;' width='18%'>");
				html.append("项目负责人");
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#004080;border-right:0px;' width='13%'>");
				html.append("承做部门");
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#004080;border-right:0px;' width='7%'>");
				html.append("合同金额(万元)");
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#004080;border-right:0px;' width='5%'>");
				html.append("已入账(万元)");
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#004080;' width='15%'>");
				html.append("企业融资部应收(万元)");
			html.append("</td>");
		html.append("</tr>");
		for (HashMap data : items) {
		html.append("<tr width='90%'>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#0000FF;border-right:0px;border-top:0px;' width='4%'>");
				html.append("<input onclick='setPro(this);' name='PRO' type='radio' value='").append(data.get("PROJECTNO")).append("'>");
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#0000FF;border-right:0px;border-top:0px;' width='8%'>");
				html.append("第").append(data.get("DZSJ")).append("次定增");
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#0000FF;border-right:0px;border-top:0px;' width='8%'>");
				html.append(data.get("LXSJ"));
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#0000FF;border-right:0px;border-top:0px;' width='18%'>");
				html.append(data.get("XMFZR"));
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#0000FF;border-right:0px;border-top:0px;' width='13%'>");
				html.append(data.get("CZBM"));
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#0000FF;border-right:0px;border-top:0px;' width='7%'>");
				html.append(data.get("HTJE"));
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#0000FF;border-right:0px;border-top:0px;' width='5%'>");
				html.append(data.get("YRZ"));
			html.append("</td>");
			html.append("<td style='border:1px solid #999999;padding:0px;color:#0000FF;border-top:0px;' width='15%'>");
				html.append(data.get("QYRZBYS"));
			html.append("</td>");
		html.append("</tr>");
		}
		return html.toString();
	}
	public boolean getIsViewSearch() {
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			OrgUser userModel = uc.get_userModel();
			Long orgRoleId = userModel.getOrgroleid();
			Long ismanager = getIsManager(uc);
			if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9)) || ismanager==1) {
				flag = true;
			}
		}
		return flag;
	}

	public boolean removeDaily(Long instanceid) {
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		boolean removeFormData = DemAPI.getInstance().removeFormData(instanceid);
		if(removeFormData){
			String value=fromData.get("PROJECTNO")==null?"":fromData.get("PROJECTNO").toString()+fromData.get("PROGRESS")==null?"":fromData.get("PROGRESS").toString();
			Long dataId=Long.parseLong(fromData.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "项目日报", "成功删除会议："+value);
		}else{
			String value=fromData.get("PROJECTNO")==null?"":fromData.get("PROJECTNO").toString()+fromData.get("PROGRESS")==null?"":fromData.get("PROGRESS").toString();
			Long dataId=Long.parseLong(fromData.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "项目日报", "删除会议："+value+"失败");
		}
		return removeFormData;
	}
}
