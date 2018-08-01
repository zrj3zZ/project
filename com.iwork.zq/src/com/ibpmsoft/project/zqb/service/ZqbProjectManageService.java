package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
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
import com.ibpmsoft.project.zqb.dao.ZqbProjectManageDAO;
import com.ibpmsoft.project.zqb.model.UploadDocModel;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.conf.XmlcConf;
import com.iwork.app.log.util.LogUtil;
import com.iwork.app.message.sysmsg.dao.SysMessageDAO;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.ListUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.ConfigurationAPI;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.OrganizationAPI;
import com.iwork.sdk.ProcessAPI;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class ZqbProjectManageService{
	private static Logger logger = Logger.getLogger(ZqbProjectManageService.class);
	private String ProjectUUID = "33833384d109463285a6a348813539f1";
	private String ProjectGroupUUID = "9f5b040a07524477bb4b6ca57b793a02";
	private String ProjectQuestionUUID = "f5c75cf3d2714f7aa1b5d943ef1d76b0";
	private String ProjectReTalkUUID = "90c55ee0bc9244f99f546932e50d3052";
	private String ProjectItemUUID = "b25ca8ed0a5a484296f2977b50db8396";
	private static final String ZQB_CUSTOMER_PURVIEW_UUID = "84ff70949eac4051806dc02cf4837bd9";
	private final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private HashMap projectSetMap;
	private ZqbProjectManageDAO zqbProjectManageDAO;

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
		return zqbProjectManageDAO.getDocList(searchkey, owner);
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
		List<HashMap> templist = DemAPI.getInstance().getList(ProjectUUID,
				null, null);
		List<String> userlist = zqbProjectManageDAO.getProjectGroupList();
		HashMap infodata = new HashMap();
		for (String userid : userlist) {
			List l = zqbProjectManageDAO.getUserJs(userid);
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
		conditionMap.put("PROJECTNO", processNo);
		if (groupNo != null) {
			conditionMap.put("GROUPID", groupNo);
		}
		return DemAPI.getInstance().getList(ProjectItemUUID, conditionMap,
				"ORDERINDEX");
	}

	private List<HashMap> getTaskMList(String groupid) {
		return zqbProjectManageDAO.getTaskMList(groupid);
	}

	/**
	 * 获得阶段列表
	 * 
	 * @return
	 */
	private List<HashMap> getTaskListForOwner(String userAddress) {
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
			// List<String> fxlist =
			// zqbProjectManageDAO.getProjectFengXianList(projectNoList,scoreType);
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
					String typeno="";
					if (hash.get("STATUS") != null
							&& !"".equals(hash.get("STATUS").toString())) {
						status = hash.get("STATUS").toString();
						xmjd = hash.get("XMJD").toString();
					}
					if(hash.get("TYPENO")!= null&&"1".equals(hash.get("TYPENO").toString())){
						typeno=hash.get("TYPENO").toString();
					}
					if (status.equals("已完成") &&!typeno.equals("1")) {
						list.add(hash);
					}
				}
			}

			if (scoreType.equals("持续督导项目")) {
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
					String typeno="";
					if (hash.get("STATUS") != null
							&& !"".equals(hash.get("STATUS").toString())) {
						status = hash.get("STATUS").toString();
					}
					if (hash.get("XMJD") != null
							&& !"".equals(hash.get("XMJD").toString())) {
						xmjd = hash.get("XMJD").toString();
					}
					if(hash.get("TYPENO") != null&&"1".equals(hash.get("TYPENO").toString())){
						typeno=hash.get("TYPENO").toString();
					}
					if (typeno.equals("1")) {
						list.add(hash);
					}
				}
			}
			if (scoreType.equals("超期项目")) {
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
					// int score = 0;
					// Date enddata = null;
					// if(hash.get("FXPGFS")!=null&&!"".equals(hash.get("FXPGFS").toString())){
					// score = Integer.parseInt(hash.get("FXPGFS").toString());
					// }
					// if(hash.get("ENDDATE")!=null){
					// enddata = (Date)hash.get("ENDDATE");
					// }
					// String status = "";
					// String xmjd = "";
					// if(hash.get("STATUS")!=null&&!"".equals(hash.get("STATUS").toString())){
					// status = hash.get("STATUS").toString();
					// }
					// if(hash.get("XMJD")!=null&&!"".equals(hash.get("XMJD").toString())){
					// xmjd = hash.get("XMJD").toString();
					// }
					// if(enddata!=null&&enddata.before(new
					// Date())&&!status.equals("已完成")&&!xmjd.equals("持续督导")&&score!=100){
					if (zqbProjectManageDAO.isProjectCQXM(hash.get("PROJECTNO")
							.toString())) {
						list.add(hash);
					}
					// }
				}
			}
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
					if (zqbProjectManageDAO.isProjectZCXM(hash.get("PROJECTNO")
							.toString())) {
						list.add(hash);
					}
					// int score = 0;
					// if(hash.get("FXPGFS")!=null&&!"".equals(hash.get("FXPGFS").toString())){
					// score = Integer.parseInt(hash.get("FXPGFS").toString());
					// }
					// String xmjd = "";
					// if(hash.get("XMJD")!=null&&!"".equals(hash.get("XMJD").toString())){
					// xmjd = hash.get("XMJD").toString();
					// }
					// String status = "";
					// if(hash.get("STATUS")!=null&&!"".equals(hash.get("STATUS").toString())){
					// status = hash.get("STATUS").toString();
					// }
					// Date enddate = null;
					// if(hash.get("ENDDATE")!=null&&!"".equals(hash.get("ENDDATE").toString())){
					// enddate = (Date)hash.get("ENDDATE");
					// }
					// if(!xmjd.equals("持续督导")&&status.equals("执行中")&&enddate.after(new
					// Date())){
					// list.add(hash);
					// }
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
			conditionMap.put(type, key);
			if (key.equals("已完成")) {
				conditionMap.put("TYPENO", "1");
			}
			if(key.equals("持续督导")){
				conditionMap.clear();
				conditionMap.put("TYPENO", "1");
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
				if (data.get("STATUS").equals("已完成") && type.equals("XMJD")
						&& !key.equals("持续督导")) {
					continue;
				}
				if (data.get("TYPENO")!=null&&data.get("TYPENO").toString().equals("1") && key.equals("执行中")) {
					continue;
				}
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
		flag = zqbProjectManageDAO.getNumber(hash);
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
		List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
				conditionMap, "ID");
		for (HashMap data : tmplist) {
			if (data.get("TYPENO")!=null&&data.get("TYPENO").toString().equals("1"))
				continue;
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
		OrgUser userModel = uc.get_userModel();
		String useraddress = UserContextUtil.getInstance().getFullUserAddress(userModel.getUserid());
		Long ismanager = getIsManager(uc);
		Long orgRoleId = userModel.getOrgroleid();
		String username = userModel.getUserid();//getUsername();
		if (projectData != null && projectData.get("OWNER") != null) {
			if (SecurityUtil.isSuperManager()) {
				flag = true;
			}
			//判断是否为填报人
			if(projectData.get("USERID")!=null&&projectData.get("USERID").equals(username)){
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
				if (projectData.get("MANAGER") != null&& projectData.get("MANAGER").toString().equals(useraddress)) {
					flag = true;
				}
			}
			// 判断是否是项目组成员
			if (!flag) {
				Long instanceid = Long.parseLong(projectData.get("INSTANCEID").toString());
				if (instanceid != null) {
					List<HashMap> sublist = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_XMCYLB");
					if (sublist != null && sublist.size() > 0) {
						for (HashMap user : sublist) {
							if (user.get("USERID") != null&& user.get("USERID").toString().equals(username)) {
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
	 * @param sssyb 
	 * @param parameterMap 
	 * 
	 * @return
	 * 2016-03-16增加SORTID，每个项目按sortid排序
	 */
	public List<HashMap> getCloseProjectList(boolean superman, int pageNow,
			int pageSize, String projectName, String xmjd2, String startDate,
			String customername,String dgzt, String sssyb, String cyrName, HashMap<String,List<String>> parameterMap) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();
		Long ismanager=getIsManager(uc);
		List<HashMap> tmplist = zqbProjectManageDAO.getCloseProjectList(
				pageNow, pageSize, projectName, xmjd2, startDate, customername,dgzt,parameterMap,sssyb,cyrName,ismanager);
		SysMessageDAO sd = new SysMessageDAO();
		for (HashMap data : tmplist) {
			data.put("ISYD",!sd.getSyMsgSize(data.get("INSTANCEID").toString()));
			list.add(data);
		}
		return list;
	}

	/**
	 * 获取当前用户负责的，正在执行的项目列表
	 * 
	 * @param startDate
	 * @param xmjd
	 * @param projectName
	 * @param cyrName 
	 * @param sssyb 
	 * @param parameterMap 
	 * 
	 * @return
	 * 2016-03-16增加SORTID，每个项目按sortid排序
	 */
	public List<HashMap> getFinishProjectList(boolean superman, int pageNow,
			int pageSize, String projectName, String xmjd, String startDate,
			String customername,String dgzt, String sssyb, String cyrName, HashMap<String, List<String>> parameterMap) {
		final int pageSize1 = pageNow * pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		Long ismanager = getIsManager(uc);
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		final String userid = userModel.getUserid().toString().trim();
		String username=userModel.getUserid();//getUsername().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		Long orgRoleId = userModel.getOrgroleid();
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
				parameter = zqbProjectManageDAO.getCyrUserMap(map, cyrName);
			}else{
				parameter = getParameterMap(map,parameterMap);
			}
		}
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,NVL(D.NUM,1) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt,A.XMCY,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,F.SORTID FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
			if (!superman) {
				if(ismanager==1){
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(") union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
				}else{
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  =? or SUBSTR(manager,0, instr(manager,'[',1)-1)  = ? or createuserid= ? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all");
					params.add(owner);
					params.add(owner);
					params.add(username);
					params.add(owner);
					params.add(userid);
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
				}
			}else{
				if(cyrName!=null&&!cyrName.equals("")){
					parameter = zqbProjectManageDAO.getCyrUserMap(map, cyrName);
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id ) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(")" + " union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
				}
			}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B"
				+ " ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like '%%'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='已完成' and A.TYPENO='1' ORDER BY A.PROJECTNO DESC,F.SORTID) C) WHERE RN > ? AND RN <= ?) J LEFT JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,F.SORTID FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		params.add(startRow1);
		params.add(pageSize1);
			if (!superman) {
				if(ismanager==1){
					sb.append(" INNER JOIN ("
							// 项目的负责人、现场负责人
							+ " select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(")"
							+ " union all"
							// 任务阶段负责人
							+ " select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(")");
					
					// 分派项目审批人
					sb.append(" union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN ("
								+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(") ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
					} else {
						// else
						sb.append(" select projectno from bd_zqb_pj_base"
								+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) ");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}else{
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  =? or SUBSTR(manager,0, instr(manager,'[',1)-1)  = ? or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ?");
					params.add(owner);
					params.add(owner);
					params.add(username);
					params.add(owner);
					params.add(userid);
					// 分派项目审批人
					sb.append(" union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					} else {
						// else
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
					
				}
			}else{
				if(cyrName!=null&&!cyrName.equals("")){
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all"
							// 任务阶段负责人
							+ " select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(")"
							// 分派项目审批人
							+ " union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
				}
			}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");

		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like '%%'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='已完成' and A.TYPENO='1' ORDER BY A.PROJECTNO DESC,F.SORTID) C) WHERE RN > ? AND RN <= ?) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_XMRWPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PJR=? ORDER BY ID) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid order by J.ID DESC,J.SORTID");
		params.add(startRow1);
		params.add(pageSize1);
		params.add(userid);
		final String sql1 = sb.toString();
		final String DDLC_UUID=ProcessAPI.getInstance().getProcessActDefId("XMSPLC");
		final List param = params;
		return zqbProjectManageDAO.getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql1);
						for (int j = 0; j < param.size(); j++) {
							query.setParameter(j, param.get(j));
						}
						List<HashMap> l = new ArrayList<HashMap>();
						List<Object[]> list = query.list();
						HashMap map;
						HashMap m = new HashMap();
						BigDecimal b;
						int n = 0;
						// String rwuuid="b25ca8ed0a5a484296f2977b50db8396";
						for (Object[] object : list) {
							map = new HashMap();
							String customername = (String) object[2];
							String owner = (String) object[3];
							String manager = (String) object[4];
							Double htje = ((BigDecimal) object[5]).doubleValue();
							Double wsje = 0.0;
							if (object[6] != null) {
								wsje = ((BigDecimal) object[6]).doubleValue();
							}
							Double ssje = 0.0;
							if (object[9] != null) {
								ssje = ((BigDecimal) object[9]).doubleValue();
							}
							String rwjd = (String) object[10];
							String spzt = (String) object[12];
							String manager1 = (String) object[13];
							Integer pjinsid = 0;
							if (object[28] != null) {
								pjinsid = Integer.valueOf(object[28].toString());
							}
							Integer xgwtinsid = 0;
							if (object[15] != null) {
								xgwtinsid = Integer.valueOf(object[15].toString());
							}
							Integer instanceid = Integer.valueOf(object[16]
									.toString());
							String xmjd = object[7] == null ? "" : object[7]
									.toString();
							String projectNo = object[1] == null ? ""
									: object[1].toString();
							String groupid = object[8] == null ? "" : object[8]
									.toString();
							String pj = object[17] == null ? "" : object[17]
									.toString();
							Integer num = Integer.valueOf(object[29].toString());
							String lcbh = object[18] == null ? "" : object[18]
									.toString();
							String stepid = object[20] == null ? "" : object[20]
									.toString();
							String xmspzt = object[22] == null ? "" : object[22]
									.toString();
							String xmcy = object[23] == null ? "" : object[23]
									.toString();
							String startdate = object[24] == null ? "" : object[24]
									.toString();
							String enddate = object[25] == null ? "" : object[25]
									.toString();
							Integer lcbs=object[19]==null?0:Integer.valueOf(object[19].toString());
							Integer taskid=object[21]==null?0:Integer.valueOf(object[21].toString());
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
							/*List<Task> tasklist=ProcessAPI.getInstance().getUserProcessTaskList(DDLC_UUID, stepid, userid);
							String lcid="";
							for (Task task : tasklist) {
								if(lcbs.toString().equals(task.getExecutionId())){
									lcid = task.getId();
								}
								
							}
							if(!"".equals(lcid)){
								map.put("TASKID", lcid);
							}else{
								map.put("TASKID", taskid);
							}*/
							Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(lcbs.toString()));
							if(newTaskId!=null&&!"".equals(newTaskId)){
								if(userid.equals(newTaskId.getAssignee())){
									map.put("TASKID", newTaskId.getId());
								}else{
									map.put("TASKID", taskid);
								}
							}else{
								map.put("TASKID", taskid);
							}
							map.put("LCBH", lcbh);
							map.put("LCBS", lcbs);
							map.put("STEPID", stepid);
							map.put("XMSPZT", xmspzt);
							map.put("XMCY", xmcy);
							map.put("STARTDATE", startdate);
							map.put("ENDDATE", enddate);
							map.put("OWNER", owner);
							map.put("MANAGER", manager);
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
	
	public List<HashMap> getFinishProjectList1(boolean superman, int pageNow,
			int pageSize, String projectName, String xmjd, String startDate,
			String customername,String dgzt) {
		final int pageSize1 = pageNow * pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		String owner = UserContextUtil.getInstance().getCurrentUserContext()._userModel
				.getUserid();//
				
		final String userid = UserContextUtil.getInstance().getCurrentUserContext()._userModel
				.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt,g.pjr pjr FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%"+customername+"%");
		}
		
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like is null");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%"+xmjd+"%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='已完成' and A.TYPENO='1' ORDER BY A.PROJECTNO DESC,GROUPID) C) WHERE RN > ? AND RN <= ?) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		params.add(startRow1);
		params.add(pageSize1);
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
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
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='已完成' and A.TYPENO='1' ORDER BY A.PROJECTNO DESC,GROUPID) C ) WHERE RN > ? AND RN <= ?) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		params.add(startRow1);
		params.add(pageSize1);
		final String sql1 = sb.toString();
		final List param = params;
		return zqbProjectManageDAO.getHibernateTemplate().executeFind(
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
						// String rwuuid="b25ca8ed0a5a484296f2977b50db8396";
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
								Long zPjid = DBUtil.getLong("SELECT INSTANCEID FROM BD_ZQB_XMRWPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PROJECTNO='"+projectNo+"' AND PJR='"+userid+"' AND GROUPID="+groupid+" ORDER BY ID", "INSTANCEID");
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
		Long dataid = (Long) hash.get("ID");
		String projectname = hash.get("PROJECTNAME").toString();
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
		boolean updateFormData = DemAPI.getInstance().updateFormData(ProjectUUID, instanceId,hash, dataid, false);
		if(updateFormData){
			LogUtil.getInstance().addLog(dataid, "项目管理维护", "关闭项目："+projectname);
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
		String projectname = hash.get("PROJECTNAME").toString();
		Long dataid = (Long) hash.get("ID");
		hash.put("TYPENO", "1");
		hash.put("STATUS", "已完成");
		// 项目转入持续督导阶段后，直接将客户保存至持续督导分派表1.若为项目督导人员负责的项目，直接默认持续督导负责人为其本人；2.其他人员负责的项目插入的客户负责人为空
		String cxduid = DBUtil.getString("select * from BD_MDM_KHQXGLB where KHBH='" + hash.get("CUSTOMERNO") + "'", "ID");
		if (cxduid == null || cxduid.equals("")) {
			String demUUIDDD = "84ff70949eac4051806dc02cf4837bd9";// 持续督导
			HashMap hashmap = new HashMap();
			Long instanceid = DemAPI.getInstance().newInstance(demUUIDDD,UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid());
			hashmap.put("instanceId", instanceid);
			hashmap.put("formid", 114);
			hashmap.put("metadataid", 36);
			hashmap.put("modelType", "DEM");
			hashmap.put("KHFZR", "");
			hashmap.put("KHBH", hash.get("CUSTOMERNO"));
			hashmap.put("KHMC", hash.get("CUSTOMERNAME"));
			hashmap.put("TXDF", "否");
			hashmap.put("SFFP", "否");
			DemAPI.getInstance().saveFormData(demUUIDDD, instanceid, hashmap,false);
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
		boolean updateFormData = DemAPI.getInstance().updateFormData(ProjectUUID, instanceId,hash, dataid, false);
		if(updateFormData){
			LogUtil.getInstance().addLog(dataid, "项目管理维护", projectname+"，转入持续督导。");
		}
		return updateFormData;
	}
	
	public boolean setProjectFinishDg(String temp) {
		String[] split = temp.split(",");
		boolean flag=false;
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
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc.get_userModel();
		Long orgRoleId = userModel.getOrgroleid();
		Long ismanager = getIsManager(uc);
		if (type != null
				&& type.equals(ZQBConstants.ISPURVIEW_TYPE_PROJECT_KEY)) {
			String currentUser = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			String owner = (String) hash.get("OWNER");
			String manager = (String) hash.get("MANAGER");
			if(orgRoleId==11||ismanager==1){
				roleType = ZQBConstants.ISPURVIEW_ROLE_TYPE_OWNER;
				return roleType;
			}
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
			} else if (hash.get("INSTANCEID")!=null&&!hash.get("INSTANCEID").toString().trim().equals("")){
				// 判断是否是项目成员
				Long instanceid = Long.parseLong(hash.get("INSTANCEID").toString());
				if (instanceid != null) {
					List<HashMap> sublist = DemAPI.getInstance()
							.getFromSubData(instanceid,
									"SUBFORM_XMCYLB");
					if(hash.get("LCBS")!=null&&!hash.get("LCBS").toString().trim().equals("")){
						Long lcId = Long.parseLong(hash.get("LCBS").toString().trim());
						List<HashMap> lcSublist = DemAPI.getInstance().getFromSubData(lcId,"SUBFORM_XMCYLB");
						if(lcSublist!=null&&!lcSublist.isEmpty()){
							sublist.addAll(lcSublist);
						}
					}
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
			list = zqbProjectManageDAO.getProjectTypeGroup(null);
		} else {
			list = zqbProjectManageDAO.getProjectTypeGroup(UserContextUtil
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
			list = zqbProjectManageDAO.getProjectTypeStatus(null);
		} else {
			list = zqbProjectManageDAO.getProjectTypeStatus(UserContextUtil
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
		List<HashMap> templist = DemAPI.getInstance().getList(ProjectUUID,
				null, null);
		List<String> userlist = zqbProjectManageDAO.getProjectGroupList();
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

		//
		// List<HashMap> pjlist = new ArrayList();
		// HashMap infodata = new HashMap();
		// for(HashMap project:templist){
		// HashMap data = new HashMap();
		// if(!isSuperman){
		// boolean flag = this.checkProjectSecurity(project, uc);
		// if(!flag)continue;
		// }
		// // pjlist.add(project);
		// Long instanceid =
		// Long.parseLong(project.get("INSTANCEID").toString());
		// List<HashMap> sublist =
		// DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_XMCYLB");
		// for(HashMap subdata:sublist){
		// if(subdata.get("USERID")!=null&&!subdata.get("USERID").toString().equals("")){
		// String userid = subdata.get("USERID").toString();
		// Object obj = data.get(userid);
		// if(obj==null){
		// data.put(userid, new Long(1));
		// }else{
		// Long num = Long.parseLong(data.get(userid).toString());
		// num++;
		// data.put(userid,num);
		// }
		// }
		// }
		// //判断项目负责人和现场负责人
		// if(project.get("OWNER")!=null){
		// String owner = project.get("OWNER").toString();
		// Object obj = data.get(owner);
		// if(obj==null){
		// data.put(owner, new Long(1));
		// }
		// }
		// //判断项目负责人和现场负责人
		// if(project.get("MANAGER")!=null){
		// String owner = project.get("MANAGER").toString();
		// Object obj = data.get(owner);
		// if(obj==null){
		// data.put(owner, new Long(1));
		// }
		// }
		//
		// //
		// Iterator iter = data.entrySet().iterator();
		// while (iter.hasNext()) {
		// Map.Entry entry = (Map.Entry) iter.next();
		// Object key = entry.getKey();
		// Object val = entry.getValue();
		// Object obj = infodata.get(key);
		// if(obj==null){
		// infodata.put(key, new Long(val.toString()));
		// }else{
		// infodata.put(key,
		// Long.parseLong(obj.toString())+Long.parseLong(val.toString()));
		// }
		// }
		// }

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
		list = zqbProjectManageDAO.getProjectFengXian(pjNoList, UserContextUtil
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
		hash = zqbProjectManageDAO.getProjectStageGroup(pjNoList,
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
			list = zqbProjectManageDAO.getProjectStageList((null));
		} else {
			list = zqbProjectManageDAO.getProjectStageList(UserContextUtil
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

	public void setZqbProjectManageDAO(ZqbProjectManageDAO zqbProjectManageDAO) {
		this.zqbProjectManageDAO = zqbProjectManageDAO;
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
		List<UploadDocModel> qDocList = zqbProjectManageDAO.getQuestionDocList(
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
					ucName = UserContextUtil.getInstance()
							.getCurrentUserContext().get_userModel()
							.getUsername();
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
							List<UploadDocModel> cDocList = zqbProjectManageDAO
									.getCommitDocList(instanceid);
							if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {
								html.append("<div class=\"comment_main pre\"><input id=\""
										+ Long.parseLong(instanceid)
										+ "\" name=\"sel"
										+ id
										+ "\" onclick=selFirst("
										+ id
										+ ") class=\"cbox\" type=\"checkbox\" role=\"checkbox\"><img  src='iwork_img/comments.png'>&nbsp;<pre>"
										+ content+"</pre>").append("<table>");
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
										"        <div class=\"comment_main pre\"><img  src='iwork_img/comments.png'>&nbsp;")
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
					html.append("	       </form> </div>\n");
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
		// 获取回复列表
		/*
		 * List<HashMap> commitlist = this.getCommitQuestionList(projectNo,
		 * taskid, talkId); for (HashMap commit : commitlist) { if
		 * (commit.get("USERID") != null) { String reaskUserid =
		 * commit.get("USERID").toString(); if (!userList.contains(reaskUserid))
		 * { userList.add(reaskUserid); } } }
		 */

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
			conditionMap.put("PROJECTNO", questionMap.get("XMBH"));
			List<HashMap> list = DemAPI.getInstance().getList(ProjectItemUUID,
					conditionMap, "ID");
			data.put("TASKNAME", list.get(0).get("TASK_NAME"));
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
		List<HashMap> list = zqbProjectManageDAO.getProjectManagerUserData();
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
			if(uc != null){
				labellist.add(uc.getUsername());
			}
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

	public int getYSZKChartDataSize(){
		return zqbProjectManageDAO.getSSZKDataSize();
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getYSZKChartData(int pageNumber,int pageSize) {
		String[] str = new String[3];
		StringBuffer label = new StringBuffer();
		StringBuffer value = new StringBuffer();
		List<HashMap> list = zqbProjectManageDAO.getSSZKData(pageNumber, pageSize);
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
			BigDecimal htje=new BigDecimal(map.get("HTJE")==null?"0":map.get("HTJE").toString());
			BigDecimal ssje=new BigDecimal(map.get("YSKE")==null?"0":map.get("YSKE").toString());
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
		List<HashMap> list = zqbProjectManageDAO.getYSZKData();
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
				Double ssje=hashMap.get("SSJE")==null?0:"".equals(hashMap.get("SSJE").toString())?0:new BigDecimal(hashMap.get("SSJE").toString()).doubleValue();
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
		List<HashMap> slist = zqbProjectManageDAO.getProjectManagerUserData();
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
		sql.append("select a.projectname,a.htje,b.ssje,to_char(a.STARTDATE,'yyyy-MM-dd') startdate,to_char(a.ENDDATE,'yyyy-MM-dd') enddate from BD_ZQB_PJ_BASE a,"
				+ "(select projectno,sum(ssje) ssje from BD_PM_TASK s where s.manager=? group by projectno)  b  where a.projectno(+)=b.projectno");
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, manager);
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
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(distinct projectno) proNum,p FROM (select projectno,owner p from bd_zqb_pj_base a union all select projectno,manager from bd_zqb_pj_base a union all select projectno,manager from BD_PM_TASK union all select distinct projectno,userid||'['||name||']' from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a inner join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a inner join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid) A GROUP BY P ORDER BY proNum");
		List namelist = new ArrayList();
		List numlist = new ArrayList();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			rset=stmt.executeQuery();
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
		// XLJ UPDATE 2015年5月6日13:57:25
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.projectno,PROJECTNAME,TAG,uname,XMJD,(CASE WHEN GXSJ IS NULL THEN TO_CHAR(CREATEDATE,'YYYY-MM-DD') ELSE GXSJ END) GXSJ FROM bd_zqb_pj_base B INNER JOIN (SELECT projectno,uname,(case MIN(TAG) when 1 then '项目负责人' when 2 then '现场负责人' when 3 then '阶段负责人' when 4 then '项目成员' else '' end) tag from (select projectno,1 tag,owner uname from bd_zqb_pj_base a where owner  =? union all select projectno,2,manager from bd_zqb_pj_base a where manager = ? union all select projectno,3,manager from BD_PM_TASK where manager = ? union all select distinct projectno,4,uname from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid||'['||b.name||']' uname  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid where uname = ? ) a  GROUP BY projectno,uname )C ON B.PROJECTNO = C.PROJECTNO LEFT JOIN (SELECT PROJECTNO,TO_CHAR(MAX(GXSJ),'YYYY-MM-DD') GXSJ FROM BD_PM_TASK GROUP BY PROJECTNO) D ON B.PROJECTNO = D.PROJECTNO ORDER BY GXSJ DESC");
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
			stmt.setString(4, username);
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
					jsonHtml.append("<td>").append(pro.get("XMJD"))
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
				cell4.setCellValue(map.get("XMJD").toString());
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
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目风险分数统计.xls");
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
		List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,conditionMap, null);
		List<ArrayList> val = new ArrayList();
		for (HashMap map : tmplist) {
			String projectNo = map.get("PROJECTNO").toString();
			String gxsj = DBUtil.getString("select TO_CHAR(GXSJ,'YYYY-MM-DD HH24:MI') AS GXSJ from BD_PM_TASK where GXSJ=(select max(GXSJ) from BD_PM_TASK where projectno='"+ projectNo + "')", "GXSJ");
			map.put("GXSJ", gxsj);
			if (map.get("STATUS").equals("已完成") && map.get("XMJD") != null
					&&map.get("TYPENO")!=null&&map.get("TYPENO").toString().equals("")) {
				continue;
			}
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
		list = zqbProjectManageDAO.getProjectFengXian(pjNoList, UserContextUtil
				.getInstance().getCurrentUserFullName());
		return list;
	}

	public List getBar2() {
		List list = null;
		boolean isSuperMan = this.getIsSuperMan();
		if (isSuperMan) {
			list = zqbProjectManageDAO.getProjectStageList((null));
		} else {
			list = zqbProjectManageDAO.getProjectStageList(UserContextUtil
					.getInstance().getCurrentUserFullName());
		}
		return list;
	}

	public List getBar3() {
		boolean isSuperMan = this.getIsSuperMan();
		List<ArrayList> list = null;
		if (isSuperMan) {
			list = zqbProjectManageDAO.getProjectTypeStatus(null);
		} else {
			list = zqbProjectManageDAO.getProjectTypeStatus(UserContextUtil
					.getInstance().getCurrentUserFullName());
		}
		return list;
	}

	public List<HashMap> getXmcy(boolean superman) {
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
		List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
				conditionMap, "ID");
		for (HashMap data : tmplist) {
			if (data.get("TYPENO")!=null&&data.get("TYPENO").equals("1"))
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
		List<HashMap> tmplist = DemAPI.getInstance().getList(ProjectUUID,
				conditionMap, "ID");
		int n = 0;
		for (HashMap data : tmplist) {
			if (data.get("TYPENO")!=null&&data.get("TYPENO").equals("1"))
				continue;
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
	
	public HashMap<String,Object> getReqireData(int pageNumber,int pageSize,String customername,String sssyb,String cyrName,String xmlx){
		HashMap<String,Object> data = zqbProjectManageDAO.getReqireData(pageNumber,pageSize,customername,sssyb,cyrName,xmlx);
		return data;
	}

	public List<HashMap> getRunProjectList1(boolean superman, int pageNumber,
			int pageSize, String projectName, String xmjd, String startDate,
			String customername,String dgzt, String sssyb, String cyrName, HashMap<String, List<String>> parameterMap) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		SysMessageDAO sd = new SysMessageDAO();
		List<HashMap> list = new ArrayList();
		List<HashMap> tmplist = new ArrayList<HashMap>();
		Long orgRoleId = uc.get_userModel().getOrgroleid();
		if (superman) {
			tmplist = zqbProjectManageDAO.getRunProjectList1(pageNumber,
					pageSize, projectName, xmjd, startDate, customername,dgzt,sssyb,cyrName);
		} else {
			if(orgRoleId==11){
				tmplist = zqbProjectManageDAO.getRunProjectList3(owner, pageNumber,
						pageSize, projectName, xmjd, startDate, customername,dgzt);
			}else{
				Long ismanager = getIsManager(uc);
				tmplist = zqbProjectManageDAO.getRunProjectList2(owner, pageNumber,
						pageSize, projectName, xmjd, startDate, customername,dgzt,parameterMap,sssyb,cyrName,ismanager);
			}
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
		// {"项目开发","签署协议","股改","尽职调查","申报材料","内核","内核反馈","申报","申报反馈"};
		SysMessageDAO sd = new SysMessageDAO();
		List<HashMap> list = new ArrayList();
		List<HashMap> tmplist = new ArrayList<HashMap>();
		tmplist = zqbProjectManageDAO.getRunProjectList1(pageNumber,
					pageSize, projectName, xmjd, startDate, customername,dgzt,null,null);
		for (HashMap data : tmplist) {
			if (data.get("XMJD").toString().equals("持续督导"))
				continue;
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
		return list;
	}

	public List getRunProjectListSize1(boolean superman, String projectName,
			String xmjd, String startDate, String customername,String dgzt, String sssyb, String cyrName, HashMap<String, List<String>> parameterMap) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		String userid = userModel.getUserid().toString().trim();
		String username=userModel.getUserid();//getUsername().toString().trim();
		Long ismanager = getIsManager(uc);
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		Long orgRoleId = userModel.getOrgroleid();
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
				parameter = zqbProjectManageDAO.getCyrUserMap(map, cyrName);
			}else{
				parameter = getParameterMap(map,parameterMap);
			}
		}
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) CNUM FROM (SELECT DISTINCT J.*,NVL(D.NUM,1) NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
			if (!superman) {
				if(ismanager==1){
					sb.append(" INNER JOIN ("
							// 项目的负责人、现场负责人
							+ " select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split("");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(") union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}else{
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  =? or SUBSTR(manager,0, instr(manager,'[',1)-1)  = ? or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all");
					params.add(owner);
					params.add(owner);
					params.add(username);
					params.add(owner);
					params.add(userid);
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}
			}else{
				if(cyrName!=null&&!cyrName.equals("")){
					parameter = zqbProjectManageDAO.getCyrUserMap(map, cyrName);
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owner1 = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owner1.length; i++) {
						if(i==(owner1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owner1[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] manager = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < manager.length; i++) {
						if(i==(manager.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(manager[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserid = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserid.length; i++) {
						if(i==(createuserid.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserid[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] manager1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < manager1.length; i++) {
						if(i==(manager1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(manager1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userid1 = parameter.get("userid").split(",");
					for (int i = 0; i < userid1.length; i++) {
						if(i==(userid1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userid1[i].replaceAll("'", ""));
					}
					sb.append(") union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}
			}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
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
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.ID DESC) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
			if (!superman) {
				if(ismanager==1){
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owner1 = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owner1.length; i++) {
						if(i==(owner1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owner1[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] manager = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < manager.length; i++) {
						if(i==(manager.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(manager[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserid = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserid.length; i++) {
						if(i==(createuserid.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserid[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] manager1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < manager1.length; i++) {
						if(i==(manager1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(manager1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userid1 = parameter.get("userid").split(",");
					for (int i = 0; i < userid1.length; i++) {
						if(i==(userid1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userid1[i].replaceAll("'", ""));
					}
					sb.append(") union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
					} else {
						// else
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append(")) ");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
				}else{
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  =? or SUBSTR(manager,0, instr(manager,'[',1)-1)  = ? or createuserid = ? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ?");
					params.add(owner);
					params.add(owner);
					params.add(username);
					params.add(owner);
					params.add(userid);
					// 分派项目审批人
					sb.append(" union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					} else {
						// else
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) ");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
				}
			}else{
				if(cyrName!=null&&!cyrName.equals("")){
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owner1 = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owner1.length; i++) {
						if(i==(owner1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owner1[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] manager = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < manager.length; i++) {
						if(i==(manager.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(manager[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserid = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserid.length; i++) {
						if(i==(createuserid.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserid[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] manager1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < manager1.length; i++) {
						if(i==(manager1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(manager1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userid1 = parameter.get("userid").split(",");
					for (int i = 0; i < userid1.length; i++) {
						if(i==(userid1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userid1[i].replaceAll("'", ""));
					}
					sb.append(") union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}
			}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
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
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.ID DESC) C) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid)");
		final String sql1 = sb.toString();
		final List param = params;
		return zqbProjectManageDAO.getHibernateTemplate().executeFind(
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
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,NVL(D.NUM,1) NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj,g.pjr pjr FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
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
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.ID DESC) C ) J LEFT JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
		
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.ID DESC) C) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		final String sql1 = sb.toString();
		final List param = params;
		return zqbProjectManageDAO.getHibernateTemplate().executeFind(
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
							String pjr = object[18]==null?"":object[18].toString();
							if(!pjr.equals(userid)){
								Long zPjid = DBUtil.getLong("SELECT INSTANCEID FROM BD_ZQB_XMRWPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PROJECTNO='"+projectNo+"' AND PJR='"+userid+"' AND GROUPID="+groupid+" ORDER BY ID", "INSTANCEID");
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

	public List getFinishProjectListSize1(boolean superman, String projectName,
			String xmjd, String startDate, String customername,String dgzt, String sssyb, String cyrName, HashMap<String,List<String>> parameterMap) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();//+ "["+ userModel.getUsername() + "]";
		final String userid = userModel.getUserid().toString().trim();
		String username=userModel.getUserid();//getUsername().toString().trim();
		Long ismanager = getIsManager(uc);
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		Long orgRoleId = userModel.getOrgroleid();
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
				parameter = zqbProjectManageDAO.getCyrUserMap(map, cyrName);
			}else{
				parameter = getParameterMap(map,parameterMap);
			}
		}
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT COUNT(*) CNUM FROM (SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
			if (!superman) {
				if(ismanager==1){
					sb.append("INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(")"
							// 分派项目审批人
							+ " union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
				}else{
					sb.append("INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  =? or SUBSTR(manager,0, instr(manager,'[',1)-1)  = ? or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all");
					params.add(owner);
					params.add(owner);
					params.add(username);
					params.add(owner);
					params.add(userid);
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
				}
			}else{
				if(cyrName!=null&&!cyrName.equals("")){
					parameter = zqbProjectManageDAO.getCyrUserMap(map, cyrName);
					sb.append("INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(") union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
				}
			}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
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
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='已完成' and A.TYPENO='1' ORDER BY A.PROJECTNO DESC,GROUPID) C)) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
			if (!superman) {
				if(ismanager==1){
					sb.append("INNER JOIN ("
							// 项目的负责人、现场负责人
							+ " select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(")");
					
					// 分派项目审批人
					sb.append(" union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
					} else {
						// else
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) ");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}else{
					sb.append("INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  =? or SUBSTR(manager,0, instr(manager,'[',1)-1)  = ? or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join ( select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ?");
					params.add(owner);
					params.add(owner);
					params.add(username);
					params.add(owner);
					params.add(userid);
					// 分派项目审批人
					sb.append(" union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					} else {
						// else
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}
			}else{
				if(cyrName!=null&&!cyrName.equals("")){
					sb.append("INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(") union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						// else
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
				}
			}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"
				+ " LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");

		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like '%%'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");			
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='已完成' and A.TYPENO='1' ORDER BY A.PROJECTNO DESC,GROUPID) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid)");
		final String sql1 = sb.toString();
		final List param = params;
		return zqbProjectManageDAO.getHibernateTemplate().executeFind(
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
	
	public List getFinishProjectListSize2(boolean superman, String projectName,
			String xmjd, String startDate, String customername,String dgzt) {
		String owner = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid();
		final String userid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,g.pjr pjr FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B"
				+ " ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like '%%'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='已完成' and A.TYPENO='1' ORDER BY A.PROJECTNO DESC,GROUPID ) C)) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
		
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like '%%'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='已完成' and A.TYPENO='1' ORDER BY A.PROJECTNO DESC,GROUPID) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		final String sql1 = sb.toString();
		final List param =params;
		return zqbProjectManageDAO.getHibernateTemplate().executeFind(
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
						// String rwuuid="b25ca8ed0a5a484296f2977b50db8396";
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
							String pjr = object[18]==null?"":object[18].toString();
							if(!pjr.equals(userid)){
								Long zPjid = DBUtil.getLong("SELECT INSTANCEID FROM BD_ZQB_XMRWPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PROJECTNO='"+projectNo+"' AND PJR='"+userid+"' AND GROUPID="+groupid+" ORDER BY ID", "INSTANCEID");
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
			String xmjd, String startDate, String customername,String dgzt, String sssyb, String cyrName, HashMap<String,List<String>> parameterMap) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		final String userid = userModel.getUserid().toString().trim();
		String username=userModel.getUserid();//getUsername().toString().trim();
		Long ismanager = getIsManager(uc);
		boolean flag = false;
		Long orgRoleId = userModel.getOrgroleid();
		if (uc != null) {
			if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {
				flag = true;
			}
		}
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
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
				parameter = zqbProjectManageDAO.getCyrUserMap(map, cyrName);
			}else{
				parameter = getParameterMap(map,parameterMap);
			}
			
		}
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) CNUM FROM (SELECT DISTINCT J.*,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj,g.pjr pjr FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
			if (!flag) {
				if(ismanager==1){
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(") union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}else{
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  =? or SUBSTR(manager,0, instr(manager,'[',1)-1)  = ? or createuserid = ? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all");
					params.add(owner);
					params.add(owner);
					params.add(username);
					params.add(owner);
					params.add(userid);
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}
			}else{
				if(cyrName!=null&&!cyrName.equals("")){
					parameter = zqbProjectManageDAO.getCyrUserMap(map, cyrName);
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(") union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ("+parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)")+") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ("+parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)")+") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ("+parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)")+")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}
			}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='已完成' AND A.TYPENO is null ORDER BY A.PROJECTNO DESC,F.SORTID) C) J LEFT JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
			if (!flag) {
				if(ismanager==1){
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(")");
					
					// 分派项目审批人
					sb.append(" union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}else{
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  =? or SUBSTR(manager,0, instr(manager,'[',1)-1)  = ? or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ?");
					params.add(owner);
					params.add(owner);
					params.add(username);
					params.add(owner);
					params.add(userid);
					// 分派项目审批人
					sb.append(" union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) ");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}
			}else{
				if(cyrName!=null&&!cyrName.equals("")){
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1)  in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1)  in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(") union all");
					// if 按多项目组审批
					if (config.equals("2")) {
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1) ");
					} else {
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
					}
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}
			}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
		}
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='已完成' AND A.TYPENO is null ORDER BY A.PROJECTNO DESC,F.SORTID ) C) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC)");
		final String sql1 = sb.toString();
		final List param = params;
		return zqbProjectManageDAO.getHibernateTemplate().executeFind(
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

	public List<HashMap> getYxmcyrz(boolean superman, int pageNumberXM,
			int pageSizeXM) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		List<HashMap> returnList = new ArrayList<HashMap>();
		List<HashMap> returntempList = new ArrayList<HashMap>();
		final int startRow = (pageNumberXM - 1) * pageSizeXM;
		final int pageSize = pageSizeXM;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		String username = userModel.getUsername();
		Long ismanager = getIsManager(uc);
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();
		// 判断是否是超权限用户
		List<HashMap<String, Object>> tmplist = null;
		Long orgRoleId = UserContextUtil.getInstance()
				.getCurrentUserContext().get_userModel().getOrgroleid();
		HashMap<String,String> parematermap = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			parematermap.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			parematermap.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			parematermap.put("createuserid", "USERID");
			parematermap.put("name", "USERNAME");
			HashMap<String, List<String>> departmentUserList = this.getDepartmentUserList();
			parameter = getParameterMap(parematermap,departmentUserList);
		}
		// 拼写sql
		StringBuffer sql = new StringBuffer();
		List params = new ArrayList();
		if (!superman) {
			if(ismanager==1){
				sql.append("select * from (select projectname, instanceid, xmjd, projectno, owner name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中' and SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
						String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
						for (int i = 0; i < owners.length; i++) {
							if(i==(owners.length-1)){
								sql.append("?");
							}else{
								sql.append("?,");
							}
							params.add(owners[i].replaceAll("'", ""));
						}
						sql.append(") union select projectname, instanceid, xmjd, projectno, manager name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中'  and SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
						String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
						for (int i = 0; i < managers.length; i++) {
							if(i==(managers.length-1)){
								sql.append("?");
							}else{
								sql.append("?,");
							}
							params.add(managers[i].replaceAll("'", ""));
						}
						sql.append(") union select projectname, instanceid, xmjd, projectno, createuserid||'['||createuser||']' name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中'  and createuserid in (");
						String[] createuserids = parameter.get("createuserid").split(",");
						for (int i = 0; i < createuserids.length; i++) {
							if(i==(createuserids.length-1)){
								sql.append("?");
							}else{
								sql.append("?,");
							}
							params.add(createuserids[i].replaceAll("'", ""));
						}
						sql.append(") union select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b  on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null and userid in (");
						String[] createuserids_ = parameter.get("createuserid").split(",");
						for (int i = 0; i < createuserids_.length; i++) {
							if(i==(createuserids_.length-1)){
								sql.append("?");
							}else{
								sql.append("?,");
							}
							params.add(createuserids_[i].replaceAll("'", ""));
						}
						sql.append(")) a inner join (select a.projectno,a.xmjd,a.projectname,b.instanceid from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null)  b on a.instanceid=b.instanceid) a order by name");
			}else{
				sql.append("select * from (select projectname, instanceid, xmjd, projectno, owner name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中' and SUBSTR(owner,0, instr(owner,'[',1)-1)=? union select projectname, instanceid, xmjd, projectno, manager name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中'  and SUBSTR(manager,0, instr(manager,'[',1)-1)=? union select projectname, instanceid, xmjd, projectno, createuserid||'['||createuser||']' name from BD_ZQB_PJ_BASE BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中'  and createuserid=? union select projectname, instanceid, xmjd, projectno, createuser name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中'  and createuserid=? union select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b  on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null and userid=? ) a inner join (select a.projectno,a.xmjd,a.projectname,b.instanceid from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null)  b on a.instanceid=b.instanceid) a order by name");
				params.add(owner);
				params.add(owner);
				params.add(owner);
				params.add(owner);
				params.add(owner);
			}
		} else {
			sql.append("select * from (select projectname, instanceid, xmjd, projectno, owner name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中' union select projectname, instanceid, xmjd, projectno, manager name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中' union select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b  on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null ) a inner join (select a.projectno,a.xmjd,a.projectname,b.instanceid from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null)  b on a.instanceid=b.instanceid ) a order by name");
		}
		tmplist = zqbProjectManageDAO.getListCYR(startRow, pageSize, sql.toString(),params);
		HashMap<String, Object> map = null;
		HashMap<String, Object> map1 = null;
		boolean flag = false;
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
			// 返回装好的list
			// for(HashMap h:returntempList){
			// flag=false;
			// if(h.get("NAME")!=null){
			// for(HashMap<String,Object> obj:returnList){
			// if(obj.get("NAME")!=null&&obj.get("NAME").toString().equals(h.get("NAME").toString())){
			// ((List)obj.get("LIST")).add(h);
			// int i= Integer.parseInt(obj.get("NUM").toString())+1;
			// obj.put("NUM", i) ;
			// obj.put("NAME",h.get("NAME").toString());
			// flag=true;
			// break;
			// }
			// }
			// if(!flag){
			// sublist=new ArrayList();
			// keyname= new HashMap<String,Object> ();
			// sublist.add(h);
			// keyname.put("LIST", sublist) ;
			// keyname.put("NUM", 1) ;
			// keyname.put("NAME",h.get("NAME").toString());
			// returnList.add(keyname);
			// }
			// }

			// }

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
	
	public List getXMListSize(boolean superman) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		String username= userModel.getUserid();//getUsername().toString().trim();
		Long ismanager = getIsManager(uc);
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();
		// 判断是否是超权限用户
		List<HashMap<String, Object>> tmplist = null;
		Long orgRoleId = userModel.getOrgroleid();
		HashMap<String,String> parematermap = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			parematermap.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			parematermap.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			parematermap.put("BOTABLE.CREATEUSERID", "USERID");
			parematermap.put("a.name", "USERNAME");
			HashMap<String, List<String>> departmentUserList = this.getDepartmentUserList();
			parameter = getParameterMap(parematermap,departmentUserList);
		}
		// 拼写sql
		StringBuffer sql = new StringBuffer();
		List params = new ArrayList();
		if (!superman) {
			if(ismanager==1){
				sql.append("select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_PJ_BASE BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status='执行中'  and (SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
				String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sql.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sql.append(") or BOTABLE.CREATEUSERID in (");
				String[] createuserids = parameter.get("BOTABLE.CREATEUSERID").split(",");
				for (int i = 0; i < createuserids.length; i++) {
					if(i==(createuserids.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(createuserids[i].replaceAll("'", ""));
				}
				sql.append(")) union select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager from (select b.instanceid, a.name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null and a.name in (");
				String[] names = parameter.get("a.name").split(",");
				for (int i = 0; i < names.length; i++) {
					if(i==(names.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(names[i].replaceAll("'", ""));
				}
				sql.append(")) a inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null) b on a.instanceid = b.instanceid ");
			}else{
				sql.append("select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_PJ_BASE BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status='执行中'  and (SUBSTR(owner,0, instr(owner,'[',1)-1)=? or SUBSTR(manager,0, instr(manager,'[',1)-1)=? or createuserid=?  ) union select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager from (select b.instanceid, a.name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null and a.name=? ) a inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null) b on a.instanceid = b.instanceid");
				params.add(owner);
				params.add(owner);
				params.add(owner);
				params.add(username);
			}
		} else {
			sql.append("select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_PJ_BASE BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status='执行中'  ");
		}
		tmplist = zqbProjectManageDAO.getListSize(sql.toString(),params);
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
		String username= userModel.getUsername().toString().trim();
		Long ismanager = getIsManager(uc);
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();
		// 判断是否是超权限用户
		List<HashMap<String, Object>> tmplist = null;
		Long orgRoleId = userModel.getOrgroleid();
		HashMap<String,String> parematermap = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			parematermap.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			parematermap.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			parematermap.put("BOTABLE.CREATEUSERID", "USERID");
			parematermap.put("a.name", "USERNAME");
			HashMap<String, List<String>> departmentUserList = this.getDepartmentUserList();
			parameter = getParameterMap(parematermap,departmentUserList);
		}
		// 拼写sql
		StringBuffer sql = new StringBuffer();
		List params = new ArrayList();
		if (!superman) {
			if(ismanager==1){
				sql.append("select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_PJ_BASE BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status='执行中'  and (SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
				String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sql.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] manager = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sql.append(") or BOTABLE.CREATEUSERID in (");
				String[] createuserids = parameter.get("BOTABLE.CREATEUSERID").split(",");
				for (int i = 0; i < createuserids.length; i++) {
					if(i==(createuserids.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(createuserids[i].replaceAll("'", ""));
				}
				sql.append(")) union select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager from (select b.instanceid, a.name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null and a.name in (");
				String[] names = parameter.get("a.name").split(",");
				for (int i = 0; i < names.length; i++) {
					if(i==(names.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(names[i].replaceAll("'", ""));
				}
				sql.append(")) a inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null) b on a.instanceid = b.instanceid ");
			}else{
				sql.append("select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_PJ_BASE BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status='执行中'  and (SUBSTR(owner,0, instr(owner,'[',1)-1)=? or SUBSTR(manager,0, instr(manager,'[',1)-1)=? or createuserid=?  )" + " union select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager from (select b.instanceid, a.name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null and a.name=? ) a inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null) b on a.instanceid = b.instanceid ");
				params.add(owner);
				params.add(owner);
				params.add(owner);
				params.add(username);
			}
		} else {
			sql.append("select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_PJ_BASE BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101  and xmjd<>'持续督导' and status='执行中'  ");
		}
		tmplist = zqbProjectManageDAO.getList(startRow, pageSize, sql.toString(),params);
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
		String owner = userModel.getUserid();// + "["
				//+ userModel.getUsername() + "]";
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();
		// 判断是否是超权限用户
		List<HashMap<String, Object>> tmplist = null;
		Long orgRoleId = userModel.getOrgroleid();
		Long ismanager = getIsManager(uc);
		HashMap<String,String> parematermap = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			parematermap.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			parematermap.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			parematermap.put("a.name", "USERNAME");
			HashMap<String,List<String>> departmentUserList = this.getDepartmentUserList();
			parameter = getParameterMap(parematermap,departmentUserList);
		}
		// 拼写sql
		StringBuffer sql = new StringBuffer();
		List params = new ArrayList();
		if (!superman) {
			if(ismanager==1){
				sql.append("select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_PJ_BASE BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and  xmjd<>'持续督导' and status='执行中'  and (SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
				String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sql.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sql.append(")) union select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager from (select b.instanceid, a.name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null and a.name in (");
				String[] names = parameter.get("a.name").split(",");
				for (int i = 0; i < names.length; i++) {
					if(i==(names.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(names[i].replaceAll("'", ""));
				}
				sql.append(")) a inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null) b on a.instanceid = b.instanceid ");
			}else{
				sql.append("select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_PJ_BASE BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and  xmjd<>'持续督导' and status='执行中'  and (owner=? or manager=? ) union select b.projectname, b.instanceid, b.xmjd, b.projectno, b.owner,b.manager from (select b.instanceid, a.name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null and a.name=?) a inner join (select a.projectno, a.xmjd, a.projectname, b.instanceid,a.owner,a.manager from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null) b on a.instanceid = b.instanceid ");
				params.add(owner);
				params.add(owner);
				params.add(userModel.getUsername());
			}
		} else {
			sql.append("select projectname,instanceid,xmjd,projectno,owner,manager from BD_ZQB_PJ_BASE BOTABLE  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status='执行中'");
		}
		tmplist = zqbProjectManageDAO.getList(startRow, pageSize, sql.toString(),params);
		HashMap<String, Object> map = null;
		if (tmplist.size() > 0) {
			for (HashMap data : tmplist) {
				map = new HashMap<String, Object>();
				List<HashMap> xmcyList = DemAPI.getInstance().getFromSubData(Long.parseLong(data.get("INSTANCEID").toString()),"SUBFORM_XMCYLB");
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
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		Long ismanager = getIsManager(uc);
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		String username = userModel.getUsername();
		if (SecurityUtil.isSuperManager()) {
			superman = true;
		}
		List<HashMap> list = new ArrayList();
		// 判断是否是超权限用户
		List<HashMap<String, Object>> tmplist = null;
		Long orgRoleId = userModel.getOrgroleid();
		HashMap<String,String> parematermap = new HashMap<String,String>();
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
		StringBuffer sql = new StringBuffer();
		List params = new ArrayList();
		if (!superman) {
			if(ismanager==1){
				sql.append("select COUNT(*) NUM from (select projectname, instanceid, xmjd, projectno, owner name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中' and SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
				String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sql.append(") union select projectname, instanceid, xmjd, projectno, manager name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中'  and SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sql.append(") union select projectname, instanceid, xmjd, projectno, createuserid||'['||createuser||']' name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中'  and createuserid in (");
				String[] createuserids = parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserids.length; i++) {
					if(i==(createuserids.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(createuserids[i].replaceAll("'", ""));
				}
				sql.append(") union select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b  on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null and userid in (");
				String[] createuserids_ = parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserids_.length; i++) {
					if(i==(createuserids_.length-1)){
						sql.append("?");
					}else{
						sql.append("?,");
					}
					params.add(createuserids_[i].replaceAll("'", ""));
				}
				sql.append(")) a inner join (select a.projectno,a.xmjd,a.projectname,b.instanceid from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null)  b on a.instanceid=b.instanceid) a order by name");
			}else{
				sql.append("select COUNT(*) NUM from (select projectname, instanceid, xmjd, projectno, owner name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中' and SUBSTR(owner,0, instr(owner,'[',1)-1)=? union select projectname, instanceid, xmjd, projectno, manager name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中'  and SUBSTR(manager,0, instr(manager,'[',1)-1)=? union select projectname, instanceid, xmjd, projectno, createuserid||'['||createuser||']' name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中'  and createuserid=? union select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b  on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null and userid=?) a inner join (select a.projectno,a.xmjd,a.projectname,b.instanceid from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null)  b on a.instanceid=b.instanceid) a order by name");
				params.add(owner);
				params.add(owner);
				params.add(owner);
				params.add(owner);
			}
		} else {
			sql.append("select COUNT(*) NUM from (select projectname, instanceid, xmjd, projectno, owner name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中' union select projectname, instanceid, xmjd, projectno, manager name from BD_ZQB_PJ_BASE BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101 and xmjd<>'持续督导' and status = '执行中' union select b.projectname, b.instanceid, b.xmjd, b.projectno,a.name from (select b.instanceid,a.userid||'['||a.name||']' name from BD_ZQB_GROUP a inner join SYS_ENGINE_FORM_BIND b  on formid = (select subformid from sys_engine_subform t where subtablekey = 'SUBFORM_XMCYLB' and rownum = 1) and metadataid = '105' and a.id = b.dataid and b.instanceid is not null ) a inner join (select a.projectno,a.xmjd,a.projectname,b.instanceid from BD_ZQB_PJ_BASE a inner join SYS_ENGINE_FORM_BIND b on formid = '91' and a.status='执行中' and metadataid = '101' and a.id = b.dataid and b.instanceid is not null)  b on a.instanceid=b.instanceid ) a order by name");
		}
		int n = 0;
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i+1, params.get(i));
			}
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
		String username = DBUtil
				.getString(
						"select USERNAME from OrgUser where departmentid=(select  id from orgdepartment where departmentno='"
								+ customerno + "')", "USERNAME");
		// 已存在用户则不可删除，否则删除
		if (username != null && !username.equals("")) {
			return "该项目所关联的客户下已存在用户，不可删除！";
		} else {
			String sql = "delete from  orgdepartment where departmentno='"
					+ customerno + "'";
			int i = DBUtil.executeUpdate(sql);
		}
		// 删除子表信息
		boolean flag = DemAPI.getInstance().removeFormData(instanceId);// 删除项目基本信息
		// 以下为删除任务信息
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
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
		StringBuffer sql = new StringBuffer("SELECT A.INSTANCEID,a.projectno,nvl(A.LCBS,'') LCBS,nvl(a.lcbh,'') lcbh, nvl(A.STEPID,'') STEPID,nvl(a.yxid,'') yxid,nvl(a.prcid,'') prcid,nvl(A.RWID,'') RWID, nvl(b.jdmc,'') TASK_NAME,nvl(A.GXR,'') GXR, nvl(to_char(A.GXSJ,'yyyy-MM-dd hh24:mi'),'') GXSJ,nvl(A.SPZT,'') SPZT,NVL(A.SSJE,'') SSJE,nvl(a.manager,'') manager,mbNum,B.ID JDBH,mbName,mb,d.jdzl,ZLName FROM BD_ZQB_KM_INFO B  LEFT JOIN (SELECT * FROM BD_PM_TASK BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 94 and BINDTABLE.metadataid = 107 WHERE projectno=?) A ON B.ID = A.GROUPID left join (select jdbh,count(*) mbNum from BD_ZQB_XMZLB group by jdbh) C on B.ID = c.jdbh LEFT JOIN  (select a.id,a.jdbh,d.FILE_SRC_NAME mbName,a.jdzl mb,b.jdzl,c.FILE_SRC_NAME ZLName from BD_ZQB_XMZLB a left join (select * from BD_ZQB_XMRWZLB where projectno=?) b on  a.jdbh = b.jdbh and a.jdzl = b.sxzl left join sys_upload_file c on b.jdzl = c.file_id left join sys_upload_file d on a.jdzl = d.file_id ) d ON B.ID = D.JDBH" + " WHERE B.STATE=1 ORDER BY b.SORTID,mb");
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
		HashMap hashZLNum = new HashMap();
		List listMB = new ArrayList();
		List listJD = new ArrayList();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rsZL = null;
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
					String ssje = rs.getString("SSJE") == null ? "" : new BigDecimal(rs.getString("SSJE")).toString();
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
								+ ssje.toString()
								+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\"><xmp>"
								+ ssje.toString() + "</xmp></td>  " + "                 ");
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
		//获取配置参数
		String isDWProjectSPTGReadOnly = ConfigUtil.readAllProperties("/isshowproject.properties").get("isDWProjectSPTGReadOnly");
		String spztcheck = "";
		String xmspzt="";
		StringBuffer sqlcheck = new StringBuffer();
		sqlcheck.append("SELECT A.SPZT XMSPZT FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J WHERE 1=1 AND PROJECTNO=? ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE FORMID = 91 AND METADATAID = 101 AND INSTANCEID IS NOT NULL) BINDTABLE ON A.ID = BINDTABLE.DATAID LEFT JOIN (SELECT * FROM BD_ZQB_XMRWPJB A INNER JOIN (SELECT MAX(ID) ID FROM BD_ZQB_XMRWPJB) B ON A.ID=B.ID) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中'");
		Connection conncheck = DBUtil.open();
		PreparedStatement pscheck = null;
		ResultSet rscheck = null;
		try {
			pscheck = conncheck.prepareStatement(sqlcheck.toString());
			pscheck.setString(1, projectno);
			rscheck = pscheck.executeQuery();
			while(rscheck.next()){
				xmspzt=rscheck.getString("XMSPZT");
				if(rscheck.getString("XMSPZT")!=null && rscheck.getString("XMSPZT").equals("审批通过")){
					spztcheck = rscheck.getString("XMSPZT");
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conncheck, pscheck, rscheck);
		}
		
		StringBuffer sql = new StringBuffer("SELECT A.INSTANCEID,a.projectno,nvl(A.LCBS,'') LCBS,nvl(a.lcbh,'') lcbh, nvl(A.STEPID,'') STEPID,nvl(a.yxid,'') yxid,nvl(a.prcid,'') prcid,nvl(A.RWID,'') RWID, nvl(b.jdmc,'') TASK_NAME,nvl(A.GXR,'') GXR, nvl(to_char(A.GXSJ,'yyyy-MM-dd hh24:mi'),'') GXSJ,nvl(A.SPZT,'') SPZT,NVL(A.SSJE,'') SSJE,nvl(a.manager,'') manager,mbNum,B.ID JDBH,mbName,mb,d.jdzl,ZLName FROM BD_ZQB_KM_INFO B  LEFT JOIN (SELECT * FROM BD_PM_TASK BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 94 and BINDTABLE.metadataid = 107 WHERE projectno=?) A ON B.ID = A.GROUPID left join (select jdbh,count(*) mbNum from BD_ZQB_XMZLB group by jdbh) C on B.ID = c.jdbh LEFT JOIN  (select a.id,a.jdbh,d.FILE_SRC_NAME mbName,a.jdzl mb,b.jdzl,c.FILE_SRC_NAME ZLName from BD_ZQB_XMZLB a left join (select * from BD_ZQB_XMRWZLB where projectno=?) b on  a.jdbh = b.jdbh and a.jdzl = b.sxzl left join sys_upload_file c on b.jdzl = c.file_id left join sys_upload_file d on a.jdzl = d.file_id ) d ON B.ID = D.JDBH" + " WHERE B.STATE=1 ORDER BY b.SORTID,mb");
		StringBuffer sqlZL = new StringBuffer();
		sqlZL.append("select a.id,a.jdbh,d.FILE_SRC_NAME mbName,a.jdzl mb,b.jdzl,c.FILE_SRC_NAME ZLName from BD_ZQB_XMZLB a left join (select * from BD_ZQB_XMRWZLB where projectno=?) b on  a.jdbh = b.jdbh and a.jdzl = b.sxzl left join sys_upload_file c on b.jdzl = c.file_id left join sys_upload_file d on a.jdzl = d.file_id inner join BD_ZQB_KM_INFO e on e.id=a.jdbh where e.state=1 order by e.sortid");
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
		HashMap hashZLNum = new HashMap();
		List listMB = new ArrayList();
		List listJD = new ArrayList();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rsZL = null;
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
					BigDecimal ssje = rs.getBigDecimal("SSJE")==null?new BigDecimal("0"):rs.getBigDecimal("SSJE");
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
					if(listJD.indexOf(jdbh) < 0 && isDWProjectSPTGReadOnly != null && isDWProjectSPTGReadOnly.equals("1") && spztcheck.equals("审批通过")){
						listJD.add(jdbh);
						content.append("<td  role=\"gridcell\"  rowspan=\""
								+ num
								+ "\" style=\"width:60px;align:center\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_MEMO\">"
								+ (spzt != null && spzt.equals("") ? "<a\">上传</a>"
										: (spzt.equals("未提交")
												|| spzt.equals("驳回") ? "<a\">"+(spzt.equals("未提交")&&"0".equals(rwid)?"":"编辑")+"</a>"
												: "编辑"))
												+ "</td>");
					}else if(listJD.indexOf(jdbh) < 0&&(xmspzt==null||xmspzt.equals("驳回"))) {
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
						
					}else{
						if(listJD.indexOf(jdbh) < 0) {
							listJD.add(jdbh);
							content.append("<td  role=\"gridcell\"  rowspan=\""
									+ num
									+ "\" style=\"width:60px;align:center\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_MEMO\">"
									+ (spzt != null && spzt.equals("") ? "上传"
											: (spzt.equals("未提交")
													|| spzt.equals("驳回") ? (spzt.equals("未提交")&&"0".equals(rwid)?"":"编辑")
													: "编辑"))
													+ "</td>");
						
						}
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
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT INFO.JDMC TASK_NAME,TASK.MANAGER,PJ.PJR,PJ.PJSJ,PJ.PJSM,TASK.GROUPID,BIND2.INSTANCEID PJINSID,PJ.ID,PJ.LDPJ,ORG.USERNAME FROM BD_PM_TASK TASK INNER JOIN BD_ZQB_XMRWPJB PJ ON TASK.PROJECTNO=? AND TASK.PROJECTNO=PJ.PROJECTNO AND TASK.GROUPID=PJ.GROUPID INNER JOIN BD_ZQB_KM_INFO INFO ON TASK.GROUPID=INFO.ID INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_IFORM IFORM INNER JOIN SYS_DEM_ENGINE ENGINE ON IFORM.ID=ENGINE.FORMID AND ENGINE.TITLE='项目任务评价表单' INNER JOIN SYS_ENGINE_FORM_BIND BIND ON IFORM.ID=BIND.FORMID AND IFORM.METADATAID=BIND.METADATAID) BIND2 ON PJ.ID=BIND2.DATAID LEFT JOIN ORGUSER ORG ON PJ.PJR=ORG.USERID ORDER BY TASK.GROUPID,PJ.ID");
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
				String pjr = rs.getString("PJR") == null ? "" : rs.getString("PJR");
				String pjrname = rs.getString("USERNAME") == null ? "" : rs.getString("USERNAME");
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
							+ pjr+"["+pjrname+"]"
							+ "\" aria-describedby=\"subformSUBFORM_SXCYRY_USERID\" style=\"text-align:left;width:60px;\"><xmp>"
							+ pjr+"["+pjrname+"]"
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
							+ "<td role=\"gridcell\" style=\"80px\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_TEL\">");
							/**只可编辑自己添加的评价信息*/
							if(pjr.equals(UserContextUtil.getInstance().getCurrentUserId())){
								content.append("<a href=\"javascript:newPJ('"+groupid+"','"+pjinsid+"')\">编辑</a>");
							}
							content.append("</td></tr>");
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
		/*String sql = "select a.jdmc TASK_NAME, f.manager , A.ID groupid, b.*, c.*,BINDTABLE.instanceid,b.id questionid,b.question QUESTION  from  	BD_ZQB_KM_INFO A,bd_zqb_xmwtfk B,(select d.*,e.userid lastuser  from (select p.id, p.PROJECTNO, p.taskno,p.questionno,p.lastdate,w.username laster from BD_ZQB_RETALK w,(select max(id) id,PROJECTNO, taskno,questionno, max(nvl(to_char(createdate,'yyyy-MM-dd hh:mi:ss'), 0)) lastdate  from BD_ZQB_RETALK     group by PROJECTNO, taskno,questionno order by id) p where w.id=p.id) d,bd_zqb_xmwtfk e where d.questionno=e.id) c, SYS_ENGINE_FORM_BIND BINDTABLE,BD_PM_TASK    f  where "
				+ " b.xmbh='"
				+ projectNo
				+ "' and b.xmbh=f.projectno  and a.ID(+) = b.taskno  and f.groupid=a.id and b.xmbh = c.PROJECTNO(+)   and b.taskno = c.taskno(+)  and b.id = BINDTABLE.dataid   and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = 97  and BINDTABLE.metadataid = 110 ";*/
		/*String sql="select b.taskname TASK_NAME,t.manager,b.groupid,BINDTABLE.instanceid,a.id questionid,question QUESTION,b.username laster,b.createdate,a.username USERNAME,a.xmbh from bd_zqb_xmwtfk a "
				+ "left join (select d.*,c.jdmc taskname,c.id groupid from (select * from BD_ZQB_RETALK where id in (select max(id) id from BD_ZQB_RETALK where projectno='"+projectNo+"' group by questionno)) d "
				+ "right join BD_ZQB_KM_INFO c on d.taskno=c.id ) b on a.id=b.questionno "
				+ "left join BD_PM_TASK t on a.xmbh=t.projectno and a.taskno=t.groupid  left join SYS_ENGINE_FORM_BIND BINDTABLE on a.id=bindtable.dataid and bindtable.instanceid is not null and BINDTABLE.formid = 97  and BINDTABLE.metadataid = 110  where xmbh='"+projectNo+"'";*/
		StringBuffer sql = new StringBuffer();
		sql.append("select c.JDMC TASK_NAME,t.manager,c.id groupid,BINDTABLE.instanceid,a.id questionid,question QUESTION,b.username laster,nvl(to_char(a.createdate,'yyyy-MM-dd hh24:mi'),'') createdate,a.username USERNAME,a.xmbh from bd_zqb_xmwtfk a left join (select d.* from (select * from BD_ZQB_RETALK where id in (select max(id) id from BD_ZQB_RETALK where projectno=? group by questionno)) d ) b on a.id=b.questionno left join BD_PM_TASK t on a.xmbh=t.projectno and a.taskno=t.groupid  left join SYS_ENGINE_FORM_BIND BINDTABLE on a.id=bindtable.dataid and bindtable.instanceid is not null and BINDTABLE.formid = 97  and BINDTABLE.metadataid = 110 left join BD_ZQB_KM_INFO c on a.taskno=c.id where xmbh=? order by questionid");
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		List<HashMap> list = DemAPI.getInstance().getList(
				"90c55ee0bc9244f99f546932e50d3052", conditionMap, "ID DESC");
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
			stmt.setString(2, projectNo);
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
							// +
							// " <td role=\"gridcell\" style=\"height:0px;width:100px;\"></td>    "
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
							// + "<td role=\"gridcell\" style=\"\" title=\""
							// + last
							// +
							// "\" aria-describedby=\"subformSUBFORM_SXCYRY_TEL\" style=\"text-align:center;width: 100px;\">"
							// + last
							// +
							// "</td>                          "
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
		params.put(1, groupid);
		String sql = "select MAX(id) id from bd_pm_group t where id< ? ";
		String id = "";
		String demUUID = "b25ca8ed0a5a484296f2977b50db8396";
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		// 校验上一任务是否存在
		int lastId = DBUTilNew.getInt("ID",sql, params);// 上一任务id
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
					sql = "select min(id) id from bd_pm_group t where id> ? ";
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
		params = new HashMap();
		params.put(1,groupid);
		// 资料说明
		sql = "select * from BD_ZQB_KM_INFO where jdbh= ? ";
		String content = DBUTilNew.getDataStr("CONTENT",sql, params);
		String sxzlmb = DBUTilNew.getDataStr("SXZLQD",sql, params);
		HashMap hash = new HashMap();
		hash.put("GROUPID", id);
		hash.put("JDZL", content);
		String name = DBUtil.getString(
				"select FILE_SRC_NAME from sys_upload_file where file_id='"
						+ sxzlmb + "'", "FILE_SRC_NAME");
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
		String demUUID = "b25ca8ed0a5a484296f2977b50db8396";
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
		// 删除流程表的数据
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
		StringBuffer content = new StringBuffer();
		if (groupid == null) {
			HashMap map = ProcessAPI.getInstance().getFromData(instanceId,
					EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			groupid = map.get("GROUPID") == null ? "" : map.get("GROUPID")
					.toString();
		}
		HashMap conditionMap = new HashMap();
		conditionMap.put("JDBH", groupid);
		//String zlstring = "7e6309501fb442faa4c61d0c4515d2d0";
		StringBuffer sxzlmb = new StringBuffer();
		List<HashMap> list = DemAPI.getInstance().getList(
				"98f297bccb85479ba29faf1fb5a05734", conditionMap, null);
		for (int i = 0; i < list.size(); i++) {
			HashMap m = list.get(i);
			String jdzl = m.get("JDZL").toString();
			sxzlmb.append(jdzl + ",");
			String name = DBUtil.getString(
					"select FILE_SRC_NAME from sys_upload_file where file_id='"
							+ jdzl + "'", "FILE_SRC_NAME");
			String subname=name;
			int length = name.length();
			if(name!=null&&name.length()>20){
				subname=name.substring(0, 17)+"...";
			}
			content.append("<tr id=\"itemTr_185"
					+ i
					+ "\" name=\"jdzlrow\"><td  width=\"180\" class=\"td_title\" id=\""
					+ i
					+ "\"><span><a title=\""+name+"\" href=\"uploadifyDownload.action?fileUUID="
					+ jdzl
					+ "\" target=\"_blank\">"
					+ subname
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
			List<HashMap> l = DemAPI.getInstance().getAllList(
					"db9b82e9d24a491aba9239cc67284b75", map, null);
			for (HashMap zlmap : l) {
				String zl = zlmap.get("JDZL").toString();
				String na = DBUtil.getString(
						"select FILE_SRC_NAME from sys_upload_file where file_id='"
								+ zl + "'", "FILE_SRC_NAME");
				content.append("<div id=\""
						+ zl
						+ "\" "
						+ "style=\"background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\">"
						+ "	<div style=\"align:right;float: right;\"><a href=\"javascript:uploadifyReomve('").append(na).append("','").append(zl).append("','").append(zl).append("');\"><img src=\"/iwork_img/del3.gif\"/></a></div>	<span><a href=\"uploadifyDownload.action?fileUUID="
						+ zl + "\" target=\"_blank\">"
						+ "<img src=\"/iwork_img/attach.png\">" + na
						+ "</a></span></div>");
			}
			content.append("</div></td></tr>");
		}
		String c = DBUtil.getString("select * from BD_ZQB_KM_INFO where id="
				+ groupid, "CONTENT");
		HashMap returnMap = new HashMap();
		returnMap.put("ATTACH", "");
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
		HashMap conditionMap = new HashMap();
		conditionMap.put("JDZL", uuid);
		List<HashMap> list = DemAPI.getInstance().getList(
				"db9b82e9d24a491aba9239cc67284b75", conditionMap, null);
		for (HashMap map : list) {
			Long instanceid = Long.parseLong(map.get("INSTANCEID").toString());
			flag1 = DemAPI.getInstance().removeFormData(instanceid);
		}
		List<HashMap> list1 = DemAPI.getInstance().getList(
				"80a37097080d496a85ceddb63e916f2c", conditionMap, null);
		for (HashMap map : list1) {
			Long instanceid = Long.parseLong(map.get("INSTANCEID").toString());
			flag2 = DemAPI.getInstance().removeFormData(instanceid);
		}
		return flag1 & flag2;
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
		list = zqbProjectManageDAO.getXMCYList(userid);
		return list;
	}

	public boolean checkXmjdExists(String projectNo, String groupid) {
		// 校验项目阶段是否存在
		boolean flag;
		String sql = "select nvl(MAX(id),0) id from BD_ZQB_KM_INFO t where id< ? ";
		String id = "";
		String demUUID = "b25ca8ed0a5a484296f2977b50db8396";// 项目任务
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		
		Map params = new HashMap();
		params.put(1, groupid);
		// 校验上一任务是否存在
		int lastId = DBUTilNew.getInt("ID",sql, params);// 上一任务id
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
		String userid = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel().getUserid();
		conditionMap.put("USERID", userid);
		int l = DemAPI.getInstance().getListSize(
				"f5c75cf3d2714f7aa1b5d943ef1d76b0", conditionMap, null);
		return l;
	}

	public List getXgwthfx(int pageNumber, int pageSize, String projectName,
			String xmjd, String question) {
		List<String> parameter=new ArrayList<String>();//存放参数
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber * pageSize;
		Map<String, String> map = ConfigUtil.readAllProperties("/common.properties");
		Long isSx = Long.parseLong(map.get("isSX")==null?"0":map.get("isSX"));
		StringBuffer sql=new StringBuffer("SELECT * FROM (SELECT WT.ID ID,WT.XMBH XMBH,WT.XMMC XMMC,WT.USERNAME USERNAME,WT.QUESTION QUESTION,KM.ID KMID,KM.JDMC JDMC,PJ.INSTANCEID INSTANCEID,PJ.LCBS LCBS,PJ.LCBH LCBH,PJ.TASKID TASKID,ROWNUM RNUM FROM BD_ZQB_XMWTFK WT LEFT JOIN BD_ZQB_KM_INFO KM ON WT.TASKNO=KM.ID LEFT JOIN (SELECT BINDTABLE.INSTANCEID,BOTABLE.PROJECTNO,BOTABLE.LCBH,BOTABLE.LCBS,BOTABLE.TASKID FROM BD_ZQB_PJ_BASE BOTABLE INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = ");
		if(isSx==1l){
			sql.append("(select id from sys_engine_iform where iform_title='项目立项表单')");
		}else{
			sql.append("91");
		}
		sql.append(" AND BINDTABLE.METADATAID = 101) PJ ON WT.XMBH=PJ.PROJECTNO WHERE 1=1 ");
		if (projectName != null && !projectName.equals("")) {
			sql.append(" AND UPPER(WT.XMMC) LIKE ?");
			parameter.add("%"+projectName+"%");
		}
		if (xmjd != null && !xmjd.equals("")) {
			sql.append(" AND UPPER(KM.JDMC) LIKE ?");
			parameter.add(xmjd);
		}
		if (question != null && !question.equals("")) {
			sql.append(" AND UPPER(WT.QUESTION) LIKE ?");
			parameter.add("%"+question+"%");
		}
		sql.append(" ORDER BY WT.ID DESC )");
		sql.append(" WHERE RNUM>? AND RNUM<=?");
		List<HashMap<String,Object>> l =zqbProjectManageDAO.getXgwtList(sql.toString(),pageNumber,pageSize,parameter);
		return l;
	}

	public String getChartProjectName(int index) {
		String manager = "";
		List<HashMap> slist = zqbProjectManageDAO.getProjectManagerUserData();
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
	private boolean check(HashMap map) {
		boolean flag;
		if(map.containsKey("JDMC")||
			map.containsKey("JDFZR")||
			map.containsKey("SSJE")||
			map.containsKey("FILENAME")||
			map.containsKey("SPZT")||
			
			map.containsKey("TWUSERNAME")||
			map.containsKey("QUESTION")||
			map.containsKey("TWCREATEDATE")||
			map.containsKey("CONTENT")||
			
			map.containsKey("PJNAME")||
			map.containsKey("PJSJ")||
			map.containsKey("LDPJ")||
			map.containsKey("PJSM")){
			flag=true;
		}else{
			flag=false;
		}
		return flag;
	}
	private boolean checkCol(Object o) {
	   return "PROJECTNAME".equals(o.toString())
			||"CUSTOMERNAME".equals(o.toString())
			||"OWNER".equals(o.toString())
			||"MANAGER".equals(o.toString())
			||"STARTDATE".equals(o.toString())
			||"ENDDATE".equals(o.toString())
			||"KHLXR".equals(o.toString())
			||"KHLXDH".equals(o.toString())
			||"GGJZR".equals(o.toString())
			||"SBJZR".equals(o.toString())
			||"HTJE".equals(o.toString())
			||"ZCLR".equals(o.toString())
			||"ZCLRDH".equals(o.toString())
			||"FZJGMC".equals(o.toString())
			||"WBCLRJG".equals(o.toString())
			||"FZJGLXR".equals(o.toString())
			||"XMBZ".equals(o.toString())
			||"SSSYB".equals(o.toString())
			||"CLSLR".equals(o.toString())
			||"SHTGR".equals(o.toString())
			||"A01".equals(o.toString())
			
			||"ZJJGMC".equals(o.toString())||"LXR".equals(o.toString())||"LXDH".equals(o.toString())||"LXYX".equals(o.toString())
			||"CLJGMC".equals(o.toString())||"CLJGFPBL".equals(o.toString())||"CLJGTBR".equals(o.toString())||"CLJGTBSJ".equals(o.toString())
			||"CLR".equals(o.toString())||"CLRFPBL".equals(o.toString())||"CLRTBR".equals(o.toString())||"CLRTBSJ".equals(o.toString())||"CLRFPBM".equals(o.toString())
			||"NAME".equals(o.toString())||"POSITION".equals(o.toString())||"TEL".equals(o.toString())||"PHONE".equals(o.toString())||"EMAIL".equals(o.toString())||"CYMEMO".equals(o.toString())
			;
	}
	public void expProjectList(HttpServletResponse response, String zcPro,String type) {
		boolean isSuperMan = this.getIsSuperMan();
		String[] split = zcPro.split(",");
		HashMap map=new HashMap();
		int colnumm=0;
		for (int i = 0; i < split.length; i++) {
			if(split[i].equals("LXR")||split[i].equals("LXDH")||split[i].equals("LXYX")){map.put(split[i],map.get("ZJJGMC"));}
			else if(split[i].equals("POSITION")||split[i].equals("TEL")||split[i].equals("PHONE")||split[i].equals("EMAIL")||split[i].equals("CYMEMO")){map.put(split[i],map.get("NAME"));}
			else if(split[i].equals("CLJGFPBL")||split[i].equals("CLJGTBR")||split[i].equals("CLJGTBSJ")){map.put(split[i],map.get("CLJGMC"));}
			else if(split[i].equals("CLRFPBL")||split[i].equals("CLRTBR")||split[i].equals("CLRTBSJ")||split[i].equals("CLRFPBM")){map.put(split[i],map.get("CLR"));}else{
				map.put(split[i],colnumm);
				colnumm++;
			}
		}
		List<String> expColList = Arrays.asList(split);
		boolean boolCUSTOMERNAME= expColList.contains("CUSTOMERNAME");
		boolean boolOWNER= expColList.contains("OWNER");
		boolean boolMANAGER= expColList.contains("MANAGER");
		boolean boolSSSYB= expColList.contains("SSSYB");
		boolean boolCLSLR= expColList.contains("CLSLR");
		boolean boolSHTGR= expColList.contains("SHTGR");
		boolean boolGZLXR= expColList.contains("GZLXR");
		boolean boolGZLXDH= expColList.contains("GZLXDH");
		boolean boolGZYJDZ= expColList.contains("GZYJDZ");
		boolean boolYJSBSJ= expColList.contains("YJSBSJ");
		boolean boolSTARTDATE= expColList.contains("STARTDATE");
		boolean boolENDDATE= expColList.contains("ENDDATE");
		boolean boolKHLXR= expColList.contains("KHLXR");
		boolean boolKHLXDH= expColList.contains("KHLXDH");
		boolean boolGGJZR= expColList.contains("GGJZR");
		boolean boolSBJZR= expColList.contains("SBJZR");
		boolean boolHTJE= expColList.contains("HTJE");
		boolean boolA01= expColList.contains("A01");
		boolean boolMEMO= expColList.contains("MEMO");
		boolean boolXMBZ= expColList.contains("XMBZ");
		boolean boolTHCL= expColList.contains("THCL");
		boolean boolZCLR= expColList.contains("ZCLR");
		boolean boolZCLRDH= expColList.contains("ZCLRDH");
		boolean boolFZJGMC= expColList.contains("FZJGMC");
		boolean boolFZJGLXR= expColList.contains("FZJGLXR");
		boolean boolWBCLRJG= expColList.contains("WBCLRJG");
		boolean boolJDMC= expColList.contains("JDMC");
		boolean boolTWUSERNAME= expColList.contains("TWUSERNAME");
		boolean boolQUESTION= expColList.contains("QUESTION");
		boolean boolTWCREATEDATE= expColList.contains("TWCREATEDATE");
		boolean boolCONTENT= expColList.contains("CONTENT");
		boolean boolPJNAME= expColList.contains("PJNAME");
		boolean boolPJSJ= expColList.contains("PJSJ");
		boolean boolLDPJ= expColList.contains("LDPJ");
		boolean boolPJSM= expColList.contains("PJSM");
		boolean boolJDFZR= expColList.contains("JDFZR");
		boolean boolSSJE= expColList.contains("SSJE");
		boolean boolFILENAME= expColList.contains("FILENAME");
		boolean boolSPZT= expColList.contains("SPZT");
		boolean boolZJJGMC= expColList.contains("ZJJGMC");
		boolean boolLXR= expColList.contains("LXR");
		boolean boolLXDH= expColList.contains("LXDH");
		boolean boolLXYX= expColList.contains("LXYX");
		boolean boolNAME= expColList.contains("NAME");
		boolean boolPOSITION= expColList.contains("POSITION");
		boolean boolTEL= expColList.contains("TEL");
		boolean boolPHONE= expColList.contains("PHONE");
		boolean boolEMAIL= expColList.contains("EMAIL");
		boolean boolCYMEMO= expColList.contains("CYMEMO");
		boolean boolCLJGMC= expColList.contains("CLJGMC");
		boolean boolCLJGFPBL= expColList.contains("CLJGFPBL");
		boolean boolCLJGTBR= expColList.contains("CLJGTBR");
		boolean boolCLJGTBSJ= expColList.contains("CLJGTBSJ");
		boolean boolCLR= expColList.contains("CLR");
		boolean boolCLRFPBL= expColList.contains("CLRFPBL");
		boolean boolCLRTBR= expColList.contains("CLRTBR");
		boolean boolCLRTBSJ= expColList.contains("CLRTBSJ");
		boolean boolCLRFPBM= expColList.contains("CLRFPBM");
		boolean boolXGWT = boolTWUSERNAME||boolQUESTION||boolTWCREATEDATE||boolCONTENT;
		boolean boolPJ = boolPJNAME || boolPJSJ || boolLDPJ || boolPJSM;
		boolean boolJD = boolJDFZR ||boolSSJE ||boolFILENAME ||boolSPZT;
		if(!boolJDMC){ boolJDMC = (boolXGWT||boolPJ||boolJD);}
		boolean boolZJJG = boolZJJGMC || boolLXR || boolLXDH || boolLXYX;
		boolean boolXMCY = boolNAME || boolPOSITION || boolTEL || boolPHONE || boolEMAIL || boolCYMEMO;
		boolean boolCLJG = boolCLJGMC || boolJDFZR || boolCLJGTBR || boolCLJGTBSJ;
		boolean boolCLRXX = boolCLR || boolCLRFPBL || boolCLRTBR || boolCLRTBSJ || boolCLRFPBM;
		boolean boolZKRYXX= expColList.contains("ZKRYXX");
		boolean boolNHRYXX= expColList.contains("NHRYXX");
		HashMap conditionMap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		List<HashMap> slist= new ArrayList<HashMap>();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		if(type.equals("run")){
			if (isSuperMan) {
				if(check(map)){
					slist = zqbProjectManageDAO.expProjectList1();
				}else{
					slist = zqbProjectManageDAO.expProjectList1None();
				}
			} else {
				if(check(map)){
					slist = zqbProjectManageDAO.expProjectList2(owner);
				}else{
					slist = zqbProjectManageDAO.expProjectList2None(owner);
				}
			}
		}else if(type.equals("cxdd")){
			if (isSuperMan) {
				if(check(map)){
					slist = zqbProjectManageDAO.expcxddProjectList1();
				}else{
					slist = zqbProjectManageDAO.expcxddProjectList1None();
				}
			} else {
				if(check(map)){
					slist = zqbProjectManageDAO.expcxddProjectList2(owner);
				}else{
					slist = zqbProjectManageDAO.expcxddProjectList2None(owner);
				}
			}
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
		int countnum=1;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell cell1 = row1.createCell((short) z++);
		cell1.setCellValue("项目名称");
		cell1.setCellStyle(style5);
		HashMap sheetMap=new HashMap();
		for(Object o : map.keySet()){
			if(checkCol(o)){
				sheetMap.put(o.toString(), null);
			}
		}
		if(boolCUSTOMERNAME){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户名称");
			cell1.setCellStyle(style5);
		}
		
		if(boolOWNER){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("部门负责人");
			cell1.setCellStyle(style5);
		}
		
		if(boolMANAGER){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目现场负责人");
			cell1.setCellStyle(style5);
		}
		
		if(boolSSSYB){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("承做部门");
			cell1.setCellStyle(style5);
		}
		
		if(boolCLSLR){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("材料受理日");
			cell1.setCellStyle(style5);
		}
		
		if(boolSHTGR){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("审核通过日");
			cell1.setCellStyle(style5);
		}
		
		if(boolGZLXR){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("股转联系人");
			cell1.setCellStyle(style5);
		}
		
		if(boolGZLXDH){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("股转联系电话");
			cell1.setCellStyle(style5);
		}
		
		if(boolGZYJDZ){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("股转邮件地址");
			cell1.setCellStyle(style5);
		}
		
		if(boolYJSBSJ){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("预计申报时间");
			cell1.setCellStyle(style5);
		}
		
		if(boolSTARTDATE){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目立项时间");
			cell1.setCellStyle(style5);
		}
		
		if(boolENDDATE){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("预计项目申报时间");
			cell1.setCellStyle(style5);
		}
		
		if(boolKHLXR){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系人");
			cell1.setCellStyle(style5);
		}
		
		if(boolKHLXDH){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系电话");
			cell1.setCellStyle(style5);
		}
		
		if(boolGGJZR){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("股改基准日");
			cell1.setCellStyle(style5);
		}
		
		if(boolSBJZR){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("申报基准日");
			cell1.setCellStyle(style5);
		}
		
		if(boolHTJE){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("合同金额(万元)");
			cell1.setCellStyle(style5);
		}
		
		if(boolA01){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("协议支出比例");
			cell1.setCellStyle(style5);
		}
		
		if(boolMEMO){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("风险评估说明");
			cell1.setCellStyle(style5);
		}
		
		if(boolXMBZ){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目备注");
			cell1.setCellStyle(style5);
		}
		
		if(boolTHCL){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("是否投行承揽");
			cell1.setCellStyle(style5);
		}
		
		if(boolZCLR){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("主承揽人");
			cell1.setCellStyle(style5);
		}
		
		if(boolZCLRDH){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("主承揽人电话");
			cell1.setCellStyle(style5);
		}
		
		if(boolFZJGMC){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("分支机构名称");
			cell1.setCellStyle(style5);
		}
		
		if(boolFZJGLXR){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("分支机构联系人");
			cell1.setCellStyle(style5);
		}
		
		if(boolWBCLRJG){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("外部承揽人机构");
			cell1.setCellStyle(style5);
		}
		
		if(boolZJJGMC||boolLXR||boolLXDH||boolLXYX){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("中介机构信息");
			cell1.setCellStyle(style5);
		}
		
		if(boolNAME||boolPOSITION||boolTEL||boolPHONE||boolEMAIL||boolCYMEMO){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目成员信息");
			cell1.setCellStyle(style5);
		}
		
		if(boolZKRYXX){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("质控人员信息");
			cell1.setCellStyle(style5);
		}
		
		if(boolNHRYXX){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("内核人员信息");
			cell1.setCellStyle(style5);
		}
		
		if(boolCLJGMC||boolCLJGFPBL||boolCLJGTBR||boolCLJGTBSJ){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("分配部门信息");
			cell1.setCellStyle(style5);
		}
		
		if(boolCLR||boolCLRFPBL||boolCLRTBR||boolCLRTBSJ||boolCLRFPBM){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("承揽人信息");
			cell1.setCellStyle(style5);
		}
		
		if(boolJDMC){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段名称");
			cell1.setCellStyle(style5);
		}
		
		if(boolJDFZR){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段负责人");
			cell1.setCellStyle(style5);
		}
		
		if(boolSSJE){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("实收金额(万元)");
			cell1.setCellStyle(style5);
		}
		
		if(boolFILENAME){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段上传资料");
			cell1.setCellStyle(style5);
		}
		
		if(boolSPZT){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("审批结果");
			cell1.setCellStyle(style5);
		}
		
		if(boolTWUSERNAME){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("提问人");
			cell1.setCellStyle(style5);
		}
		if(boolQUESTION){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("问题");
			cell1.setCellStyle(style5);
		}
		if(boolTWCREATEDATE){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("提问时间");
			cell1.setCellStyle(style5);
		}
		if(boolCONTENT){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("回复信息");
			cell1.setCellStyle(style5);
		}
		
		if(boolPJNAME){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段评价人");
			cell1.setCellStyle(style5);
		}
		if(boolPJSJ){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段评价时间");
			cell1.setCellStyle(style5);
		}
		if(boolLDPJ){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段评价结果");
			cell1.setCellStyle(style5);
		}
		if(boolPJSM){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段评价说明");
			cell1.setCellStyle(style5);
		}
		
		for (HashMap hashMap : slist) {
			List<HashMap> xgwtList= new ArrayList<HashMap>();
			List<HashMap> pjList= new ArrayList<HashMap>();
			List<HashMap> xmcyList= new ArrayList<HashMap>();
			List<HashMap> zjjgList= new ArrayList<HashMap>();
			List<HashMap> cljgList= new ArrayList<HashMap>();
			List<HashMap> clrList= new ArrayList<HashMap>();
			String instanceid = hashMap.get("LCID").toString();
			if(boolXGWT&&hashMap.get("GROUPID")!=null&&!"".equals(hashMap.get("GROUPID").toString())){
				xgwtList=zqbProjectManageDAO.expProjectXGWTList(hashMap.get("PROJECTNO").toString(),hashMap.get("GROUPID").toString());
				
			}
			if(boolPJ&&hashMap.get("GROUPID")!=null&&!"".equals(hashMap.get("GROUPID").toString())){
				pjList=zqbProjectManageDAO.expProjectPJList(hashMap.get("PROJECTNO").toString(),hashMap.get("GROUPID").toString());
			}
			if(countnum==1){
				if(xgwtList.size()==0&&pjList.size()==0){
					beginSize=1;
					countnum++;
				}else{
					beginSize=xgwtList.size()>pjList.size()?xgwtList.size():pjList.size();
					countnum++;
				}
			}
			if(pjInstanceid.longValue()!=Integer.valueOf(hashMap.get("LCID").toString())){
				HashMap pMap=new HashMap();
				String projectno = hashMap.get("PROJECTNO").toString();
				pMap.put("PROJECTNO", projectno);
				List<HashMap> list2 = DemAPI.getInstance().getList(ProjectUUID, pMap, null);
				if(!list2.isEmpty()){
					HashMap dataMap = list2.get(0);
					String spzt=dataMap.get("SPZT").toString();
					String stepid=dataMap.get("STEPID").toString();
					String jd3 = SystemConfig._xmsplcConf.getJd3();
					if("审批通过".equals(spzt)&&jd3.equals(stepid)){
						if(boolXMCY){xmcyList= DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_XMCYLB");}
						if(boolZJJG){zjjgList= DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_XMZJJG");}
						if(boolCLJG){cljgList= DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_CLJG");}
						if(boolCLRXX){clrList= DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_CLR");}
					}else{
						if(instanceid!=null&&!"".equals(instanceid)){
							Long instanceId=Long.parseLong(instanceid);
							if(boolXMCY){xmcyList= DemAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMCYLB");}
							if(boolZJJG){zjjgList= DemAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMZJJG");}
							if(boolCLJG){cljgList= DemAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");}
							if(boolCLRXX){clrList= DemAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");}
							if(xmcyList==null){
								xmcyList= new ArrayList<HashMap>();
							}
							if(zjjgList==null){
								zjjgList= new ArrayList<HashMap>();
							}
							if(cljgList==null){
								cljgList= new ArrayList<HashMap>();
							}
							if(clrList==null){
								clrList= new ArrayList<HashMap>();
							}
							
							List<HashMap> lcXmcyList = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMCYLB");
							if(lcXmcyList!=null&&lcXmcyList.isEmpty()){
								xmcyList.addAll(lcXmcyList);
								xmcyList = ListUtil.cleanList(xmcyList, true, "ID");
							}
							List<HashMap> lcZjjgList = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMZJJG");
							if(lcZjjgList!=null&&lcZjjgList.isEmpty()){
								zjjgList.addAll(lcZjjgList);
								zjjgList = ListUtil.cleanList(zjjgList, true, "ID");
							}
							List<HashMap> lcCljgList = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
							if(lcCljgList!=null&&lcCljgList.isEmpty()){
								cljgList.addAll(lcCljgList);
								cljgList = ListUtil.cleanList(cljgList, true, "ID");
							}
							List<HashMap> lcClrList = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
							if(lcClrList!=null&&lcClrList.isEmpty()){
								clrList.addAll(lcClrList);
								clrList = ListUtil.cleanList(clrList, true, "ID");
							}
						}
					}
				}
			}
			/*for(Object o : map.keySet()){
				if(checkCol(o)){
					if(sheetMap.get(o.toString())==null){
						short colLength2 = (short) (hashMap.get(o.toString()) == null ? 0 : hashMap.get(o.toString()).toString().length() * 256 * 2);
						sheetMap.put(o.toString(), colLength2);
					}else{
						short length = Short.parseShort(sheetMap.get(o.toString()).toString());
						short colLength2 = (short) (hashMap.get(o.toString()) == null ? 0 : hashMap.get(o.toString()).toString().length() * 256 * 2);
						if(length<colLength2){
							sheetMap.put(o.toString(), colLength2);
						}
					}
					
				}
			}*/
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
				if(boolCUSTOMERNAME){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("CUSTOMERNAME")==null?"":hashMap.get("CUSTOMERNAME").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolOWNER){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("OWNER")==null?"":hashMap.get("OWNER").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolMANAGER){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("XMmanager")==null?"":hashMap.get("XMmanager").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolSSSYB){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SSSYB")==null?"":hashMap.get("SSSYB").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolCLSLR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("CLSLR")==null?"":hashMap.get("CLSLR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolSHTGR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SHTGR")==null?"":hashMap.get("SHTGR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolGZLXR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("GZLXR")==null?"":hashMap.get("GZLXR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolGZLXDH){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("GZLXDH")==null?"":hashMap.get("GZLXDH").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolGZYJDZ){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("GZYJDZ")==null?"":hashMap.get("GZYJDZ").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolYJSBSJ){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("YJSBSJ")==null?"":hashMap.get("YJSBSJ").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolSTARTDATE){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("STARTDATE")==null?"":hashMap.get("STARTDATE").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolENDDATE){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("ENDDATE")==null?"":hashMap.get("ENDDATE").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolKHLXR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("KHLXR")==null?"":hashMap.get("KHLXR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolKHLXDH){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("KHLXDH")==null?"":hashMap.get("KHLXDH").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolGGJZR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("GGJZR")==null?"":hashMap.get("GGJZR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolSBJZR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SBJZR")==null?"":hashMap.get("SBJZR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolHTJE){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("HTJE")==null?"":hashMap.get("HTJE").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolA01){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("A01")==null?"":hashMap.get("A01").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolMEMO){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("MEMO")==null?"":hashMap.get("MEMO").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolXMBZ){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("XMBZ")==null?"":hashMap.get("XMBZ").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolTHCL){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("THCL")==null?"":hashMap.get("THCL").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolZCLR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("ZCLR")==null?"":hashMap.get("ZCLR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolZCLRDH){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("ZCLRDH")==null?"":hashMap.get("ZCLRDH").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolFZJGMC){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("FZJGMC")==null?"":hashMap.get("FZJGMC").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolFZJGLXR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("FZJGLXR")==null?"":hashMap.get("FZJGLXR").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolWBCLRJG){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("WBCLRJG")==null?"":hashMap.get("WBCLRJG").toString());
					cell2.setCellStyle(style4);
				}
				
				////----------------------
				if(boolZJJGMC||boolLXR||boolLXDH||boolLXYX){
					if(zjjgList.size()>0){
						StringBuffer sb=new StringBuffer();
						int c=1;
							for (HashMap cell : zjjgList) {
								sb.append(""+c+"、");
								if(boolZJJGMC){
									if(cell.get("ZJJGMC")!=null&&!"".equals(cell.get("ZJJGMC").toString())){
										sb.append(cell.get("ZJJGMC")==null?"":cell.get("ZJJGMC").toString());
									}
								}
								
								if(boolLXR){
									if(cell.get("LXR")!=null&&!"".equals(cell.get("LXR").toString())){
										if(boolZJJGMC){
											sb.append("==>");
										}
										sb.append(cell.get("LXR")==null?"":cell.get("LXR").toString());
									}
								}
								
								if(boolLXDH){
									if(cell.get("LXDH")!=null&&!"".equals(cell.get("LXDH").toString())){
										if(boolZJJGMC||boolLXR){
											sb.append("==>");
										}
										sb.append(cell.get("LXDH")==null?"":cell.get("LXDH").toString());
									}
								}
								
								if(boolLXYX){
									if(cell.get("LXYX")!=null&&!"".equals(cell.get("LXYX").toString())){
										if(boolZJJGMC||boolLXR||boolLXDH){
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
					}else{
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue("");
						cell2.setCellStyle(style4);
					}
				}
				if(boolNAME||boolPOSITION||boolTEL||boolPHONE||boolEMAIL||boolCYMEMO){
					if(xmcyList.size()>0){
						StringBuffer sb=new StringBuffer();
						int c=1;
						for (HashMap cell : xmcyList) {
							sb.append(""+c+"、");
							if(boolNAME){
								if(cell.get("NAME")!=null&&!"".equals(cell.get("NAME").toString())){
									sb.append(cell.get("NAME")==null?"":cell.get("NAME").toString());
								}
							}
							
							if(boolPOSITION){
								if(cell.get("POSITION")!=null&&!"".equals(cell.get("POSITION").toString())){
									if(boolNAME){
										sb.append("==>");
									}
									sb.append(cell.get("POSITION")==null?"":cell.get("POSITION").toString());
								}
							}
							
							if(boolTEL){
								if(cell.get("TEL")!=null&&!"".equals(cell.get("TEL").toString())){
									if(boolNAME||boolPOSITION){
										sb.append("==>");
									}
									sb.append(cell.get("TEL")==null?"":cell.get("TEL").toString());
								}
							}
							
							if(boolPHONE){
								if(cell.get("PHONE")!=null&&!"".equals(cell.get("PHONE").toString())){
									if(boolNAME||boolPOSITION||boolTEL){
										sb.append("==>");
									}
									sb.append(cell.get("PHONE")==null?"":cell.get("PHONE").toString());
								}
							}
							
							if(boolEMAIL){
								if(cell.get("EMAIL")!=null&&!"".equals(cell.get("EMAIL").toString())){
									if(boolNAME||boolPOSITION||boolTEL||boolPHONE){
										sb.append("==>");
									}
									sb.append(cell.get("EMAIL")==null?"":cell.get("EMAIL").toString());
								}
							}
							if(boolCYMEMO){
								if(cell.get("MEMO")!=null&&!"".equals(cell.get("MEMO").toString())){
									if(boolNAME||boolPOSITION||boolTEL||boolEMAIL||boolPHONE){
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
					}else{
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue("");
						cell2.setCellStyle(style4);
					}
				}
				if(boolZKRYXX){
					String projectno = hashMap.get("PROJECTNO").toString();
					StringBuffer sql = new StringBuffer("SELECT A.INSTANCEID FROM SYS_ENGINE_FORM_BIND A INNER JOIN (SELECT * FROM SYS_ENGINE_IFORM WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_ZKSPRQ')) B ON A.FORMID=B.ID AND A.METADATAID=B.METADATAID INNER JOIN (SELECT ID FROM BD_ZQB_ZKSPRQ WHERE PROJECTNO = '"+projectno+"') C ON A.DATAID=C.ID");
					Integer zkinstanceid = DBUtil.getInt(sql.toString(), "INSTANCEID");
					Long zkinsid = zkinstanceid.longValue();
					List<HashMap> zkryList= new ArrayList<HashMap>();
					if(zkinsid!=0){
						zkryList = DemAPI.getInstance().getFromSubData(zkinsid, "SUBFORM_ZKNHXGRY");
					}
					if(zkryList.size()>0){
						StringBuffer sb=new StringBuffer();
						int c=1;
						for (HashMap cell : zkryList) {
							sb.append(""+c+"、");
							sb.append(cell.get("USERNAME")==null?"":cell.get("USERNAME").toString());
							sb.append(cell.get("PHONE")==null?"":cell.get("PHONE").toString());
							sb.append(cell.get("TEL")==null?"":cell.get("TEL").toString());
							sb.append(cell.get("EMAIL")==null?"":cell.get("EMAIL").toString());
							sb.append("\r\n");
							c++;
						}
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue(sb.toString());
						cell2.setCellStyle(style6);
					}else{
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue("");
						cell2.setCellStyle(style4);
					}
				}
				if(boolNHRYXX){
					String projectno = hashMap.get("PROJECTNO").toString();
					StringBuffer sql = new StringBuffer("SELECT A.INSTANCEID FROM SYS_ENGINE_FORM_BIND A INNER JOIN (SELECT * FROM SYS_ENGINE_IFORM WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_NHSPRQ')) B ON A.FORMID=B.ID AND A.METADATAID=B.METADATAID INNER JOIN (SELECT ID FROM BD_ZQB_NHSPRQ WHERE PROJECTNO = '"+projectno+"') C ON A.DATAID=C.ID");
					Integer nhinstanceid = DBUtil.getInt(sql.toString(), "INSTANCEID");
					Long nhinsid = nhinstanceid.longValue();
					List<HashMap> nhryList= new ArrayList<HashMap>();
					if(nhinsid!=0){
						nhryList = DemAPI.getInstance().getFromSubData(nhinsid, "SUBFORM_ZKNHXGRY");
					}
					if(nhryList.size()>0){
						StringBuffer sb=new StringBuffer();
						int c=1;
						for (HashMap cell : nhryList) {
							sb.append(""+c+"、");
							sb.append(cell.get("USERNAME")==null?"":cell.get("USERNAME").toString());
							sb.append(cell.get("PHONE")==null?"":cell.get("PHONE").toString());
							sb.append(cell.get("TEL")==null?"":cell.get("TEL").toString());
							sb.append(cell.get("EMAIL")==null?"":cell.get("EMAIL").toString());
							sb.append("\r\n");
							c++;
						}
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue(sb.toString());
						cell2.setCellStyle(style6);
					}else{
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue("");
						cell2.setCellStyle(style4);
					}
				}
				if(boolCLJGMC||boolCLJGFPBL||boolCLJGTBR||boolCLJGTBSJ){
					if(cljgList.size()>0){
						StringBuffer sb=new StringBuffer();
						int c=1;
						for (HashMap cell : cljgList) {
							sb.append(""+c+"、");
							if(boolCLJGMC){
								if(cell.get("JGMC")!=null&&!"".equals(cell.get("JGMC").toString())){
									sb.append(cell.get("JGMC").toString());
								}
							}
							
							if(boolCLJGFPBL){
								if(cell.get("FPBL")!=null&&!"".equals(cell.get("FPBL").toString())){
									if(boolCLJGMC){
										sb.append("==>");
									}
									sb.append(cell.get("FPBL").toString());
								}
							}
							
							if(boolCLJGTBR){
								if(cell.get("TBR")!=null&&!"".equals(cell.get("TBR").toString())){
									if(boolCLJGMC||boolCLJGFPBL){
										sb.append("==>");
									}
									sb.append(cell.get("TBR").toString());
								}
							}
							
							if(boolCLJGTBSJ){
								if(cell.get("TBSJ")!=null&&!"".equals(cell.get("TBSJ").toString())){
									if(boolCLJGMC||boolCLJGFPBL||boolCLJGTBR){
										sb.append("==>");
									}
									sb.append(cell.get("TBSJ").toString());
								}
							}
							sb.append("\r\n");
							c++;
						}
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue(sb.toString());
						cell2.setCellStyle(style6);
					}else{
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue("");
						cell2.setCellStyle(style4);
					}
				}
				if(boolCLR||boolCLRFPBL||boolCLRTBR||boolCLRTBSJ||boolCLRFPBM){
					if(clrList.size()>0){
						StringBuffer sb=new StringBuffer();
						int c=1;
						for (HashMap cell : clrList) {
							sb.append(""+c+"、");
							if(boolCLR){
								if(cell.get("CLR")!=null&&!"".equals(cell.get("CLR").toString())){
									sb.append(cell.get("CLR").toString());
								}
							}
							
							if(boolCLRFPBL){
								if(cell.get("FPBL")!=null&&!"".equals(cell.get("FPBL").toString())){
									if(boolCLR){
										sb.append("==>");
									}
									sb.append(cell.get("FPBL").toString());
								}
							}
							
							if(boolCLRTBR){
								if(cell.get("TBR")!=null&&!"".equals(cell.get("TBR").toString())){
									if(boolCLR||boolCLRFPBL){
										sb.append("==>");
									}
									sb.append(cell.get("TBR").toString());
								}
							}
							
							if(boolCLRTBSJ){
								if(cell.get("TBSJ")!=null&&!"".equals(cell.get("TBSJ").toString())){
									if(boolCLR||boolCLRFPBL||boolCLRTBR){
										sb.append("==>");
									}
									sb.append(cell.get("TBSJ").toString());
								}
							}
							if(boolCLRTBSJ){
								if(cell.get("SSBM")!=null&&!"".equals(cell.get("SSBM").toString())){
									if(boolCLR||boolCLRFPBL||boolCLRTBR||boolCLRFPBM){
										sb.append("==>");
									}
									sb.append(cell.get("SSBM").toString());
								}
							}
							sb.append("\r\n");
							c++;
						}
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue(sb.toString());
						cell2.setCellStyle(style6);
					}else{
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue("");
						cell2.setCellStyle(style4);
					}
				}
				
				if(boolJDMC){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("JDMC")==null?"":hashMap.get("JDMC").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolJDFZR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("MANAGER")==null?"":hashMap.get("MANAGER").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolSSJE){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SSJE")==null?"":hashMap.get("SSJE").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolFILENAME){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("FILENAME")==null?"":hashMap.get("FILENAME").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolSPZT){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SPZT")==null?"":hashMap.get("SPZT").toString());
					cell2.setCellStyle(style4);
				}
			}else{
				cell2.setCellValue("");
				cell2.setCellStyle(style4);
				if(boolCUSTOMERNAME){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolOWNER){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolMANAGER){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolSSSYB){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolCLSLR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolSHTGR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolGZLXR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolGZLXDH){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolGZYJDZ){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolYJSBSJ){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolSTARTDATE){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolENDDATE){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolKHLXR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolKHLXDH){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolGGJZR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolSBJZR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolHTJE){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolA01){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolMEMO){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolXMBZ){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolTHCL){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolZCLR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolZCLRDH){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolFZJGMC){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolFZJGLXR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				if(boolWBCLRJG){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue("");
					cell2.setCellStyle(style4);
				}
				
				////----------------------
				if(boolZJJGMC||boolLXR||boolLXDH||boolLXYX){
					if(zjjgList.size()>0){
						StringBuffer sb=new StringBuffer();
						int c=1;
							for (HashMap cell : zjjgList) {
								sb.append(""+c+"、");
								if(boolZJJGMC){
									if(cell.get("ZJJGMC")!=null&&!"".equals(cell.get("ZJJGMC").toString())){
										sb.append(cell.get("ZJJGMC")==null?"":cell.get("ZJJGMC").toString());
									}
								}
								
								if(boolLXR){
									if(cell.get("LXR")!=null&&!"".equals(cell.get("LXR").toString())){
										if(boolZJJGMC){
											sb.append("==>");
										}
										sb.append(cell.get("LXR")==null?"":cell.get("LXR").toString());
									}
								}
								
								if(boolLXDH){
									if(cell.get("LXDH")!=null&&!"".equals(cell.get("LXDH").toString())){
										if(boolZJJGMC||boolLXR){
											sb.append("==>");
										}
										sb.append(cell.get("LXDH")==null?"":cell.get("LXDH").toString());
									}
								}
								
								if(boolLXYX){
									if(cell.get("LXYX")!=null&&!"".equals(cell.get("LXYX").toString())){
										if(boolZJJGMC||boolLXR||boolLXDH){
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
					}else{
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue("");
						cell2.setCellStyle(style4);
					}
				}
				if(boolNAME||boolPOSITION||boolTEL||boolPHONE||boolEMAIL||boolCYMEMO){
					if(xmcyList.size()>0){
						StringBuffer sb=new StringBuffer();
						int c=1;
						for (HashMap cell : xmcyList) {
							sb.append(""+c+"、");
							if(boolNAME){
								if(cell.get("NAME")!=null&&!"".equals(cell.get("NAME").toString())){
									sb.append(cell.get("NAME")==null?"":cell.get("NAME").toString());
								}
							}
							
							if(boolPOSITION){
								if(cell.get("POSITION")!=null&&!"".equals(cell.get("POSITION").toString())){
									if(boolNAME){
										sb.append("==>");
									}
									sb.append(cell.get("POSITION")==null?"":cell.get("POSITION").toString());
								}
							}
							
							if(boolTEL){
								if(cell.get("TEL")!=null&&!"".equals(cell.get("TEL").toString())){
									if(boolNAME||boolPOSITION){
										sb.append("==>");
									}
									sb.append(cell.get("TEL")==null?"":cell.get("TEL").toString());
								}
							}
							
							if(boolPHONE){
								if(cell.get("PHONE")!=null&&!"".equals(cell.get("PHONE").toString())){
									if(boolNAME||boolPOSITION||boolTEL){
										sb.append("==>");
									}
									sb.append(cell.get("PHONE")==null?"":cell.get("PHONE").toString());
								}
							}
							
							if(boolEMAIL){
								if(cell.get("EMAIL")!=null&&!"".equals(cell.get("EMAIL").toString())){
									if(boolNAME||boolPOSITION||boolTEL||boolPHONE){
										sb.append("==>");
									}
									sb.append(cell.get("EMAIL")==null?"":cell.get("EMAIL").toString());
								}
							}
							if(boolCYMEMO){
								if(cell.get("MEMO")!=null&&!"".equals(cell.get("MEMO").toString())){
									if(boolNAME||boolPOSITION||boolTEL||boolEMAIL||boolPHONE){
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
					}else{
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue("");
						cell2.setCellStyle(style4);
					}
				}
				if(boolCLJGMC||boolCLJGFPBL||boolCLJGTBR||boolCLJGTBSJ){
					if(cljgList.size()>0){
						StringBuffer sb=new StringBuffer();
						int c=1;
						for (HashMap cell : cljgList) {
							sb.append(""+c+"、");
							if(boolCLJGMC){
								if(cell.get("JGMC")!=null&&!"".equals(cell.get("JGMC").toString())){
									sb.append(cell.get("JGMC").toString());
								}
							}
							
							if(boolCLJGFPBL){
								if(cell.get("FPBL")!=null&&!"".equals(cell.get("FPBL").toString())){
									if(boolCLJGMC){
										sb.append("==>");
									}
									sb.append(cell.get("FPBL").toString());
								}
							}
							
							if(boolCLJGTBR){
								if(cell.get("TBR")!=null&&!"".equals(cell.get("TBR").toString())){
									if(boolCLJGMC||boolCLJGFPBL){
										sb.append("==>");
									}
									sb.append(cell.get("TBR").toString());
								}
							}
							
							if(boolCLJGTBSJ){
								if(cell.get("TBSJ")!=null&&!"".equals(cell.get("TBSJ").toString())){
									if(boolCLJGMC||boolCLJGFPBL||boolCLJGTBR){
										sb.append("==>");
									}
									sb.append(cell.get("TBSJ").toString());
								}
							}
							sb.append("\r\n");
							c++;
						}
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue(sb.toString());
						cell2.setCellStyle(style6);
					}else{
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue("");
						cell2.setCellStyle(style4);
					}
				}
				if(boolCLR||boolCLRFPBL||boolCLRTBR||boolCLRTBSJ||boolCLRFPBM){
					if(clrList.size()>0){
						StringBuffer sb=new StringBuffer();
						int c=1;
						for (HashMap cell : clrList) {
							sb.append(""+c+"、");
							if(boolCLR){
								if(cell.get("CLR")!=null&&!"".equals(cell.get("CLR").toString())){
									sb.append(cell.get("CLR").toString());
								}
							}
							
							if(boolCLRFPBL){
								if(cell.get("FPBL")!=null&&!"".equals(cell.get("FPBL").toString())){
									if(boolCLR){
										sb.append("==>");
									}
									sb.append(cell.get("FPBL").toString());
								}
							}
							
							if(boolCLRTBR){
								if(cell.get("TBR")!=null&&!"".equals(cell.get("TBR").toString())){
									if(boolCLR||boolCLRFPBL){
										sb.append("==>");
									}
									sb.append(cell.get("TBR").toString());
								}
							}
							
							if(boolCLRTBSJ){
								if(cell.get("TBSJ")!=null&&!"".equals(cell.get("TBSJ").toString())){
									if(boolCLR||boolCLRFPBL||boolCLRTBR){
										sb.append("==>");
									}
									sb.append(cell.get("TBSJ").toString());
								}
							}
							if(boolCLRTBSJ){
								if(cell.get("SSBM")!=null&&!"".equals(cell.get("SSBM").toString())){
									if(boolCLR||boolCLRFPBL||boolCLRTBR||boolCLRFPBM){
										sb.append("==>");
									}
									sb.append(cell.get("SSBM").toString());
								}
							}
							sb.append("\r\n");
							c++;
						}
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue(sb.toString());
						cell2.setCellStyle(style6);
					}else{
						cell2 = row2.createCell((short) n++);
						cell2.setCellValue("");
						cell2.setCellStyle(style4);
					}
				}
				
				if(boolJDMC){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("JDMC")==null?"":hashMap.get("JDMC").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolJDFZR){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("MANAGER")==null?"":hashMap.get("MANAGER").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolSSJE){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SSJE")==null?"":hashMap.get("SSJE").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolFILENAME){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("FILENAME")==null?"":hashMap.get("FILENAME").toString());
					cell2.setCellStyle(style4);
				}
				
				if(boolSPZT){
					cell2 = row2.createCell((short) n++);
					cell2.setCellValue(hashMap.get("SPZT")==null?"":hashMap.get("SPZT").toString());
					cell2.setCellStyle(style4);
				}
			}
				
				int x=m;
				int size=1;
				int xgwtSize=0;
				int pjSize=0;
				
				if(boolTWUSERNAME||boolQUESTION||boolTWCREATEDATE||boolCONTENT){
					xgwtSize=xgwtList.size();
				}
				if(boolPJNAME||boolPJSJ||boolLDPJ||boolPJSM){
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
						if(boolTWUSERNAME){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(boolQUESTION){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(boolTWCREATEDATE){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(boolCONTENT){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						
						if(boolPJNAME){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(boolPJSJ){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(boolLDPJ){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
						if(boolPJSM){
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
					}
				}
				for (int i = 0; i < size2; i++) {
					HSSFRow row3=null;
					int countSize=0;
					if(size>1){
						row3=sheet.createRow((int) x++);
						for (int j = 0; j <= n; j++) {
							cell2 = row3.createCell((short) j);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
						}
					}
					if(xgwtList.size()>i){
						HashMap xgwtMap = xgwtList.get(i);
						if(boolTWUSERNAME){
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
						if(boolQUESTION){
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
						if(boolTWCREATEDATE){
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
						if(boolCONTENT){
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
						if(boolTWUSERNAME){
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
						if(boolQUESTION){
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
						if(boolTWCREATEDATE){
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
						if(boolCONTENT){
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
						if(boolPJNAME){
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
						if(boolPJSJ){
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
						if(boolLDPJ){
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
						if(boolPJSM){
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
						if(boolPJNAME){
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
						if(boolPJSJ){
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
						if(boolLDPJ){
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
						if(boolPJSM){
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
					/*if(i==0&&pjInstanceid.longValue()!=Integer.valueOf(hashMap.get("LCID").toString()).longValue()){
						StringBuffer sb=new StringBuffer();
						int c=1;
						if(zjjgList.size()>0){
							for (HashMap cell : zjjgList) {
								sb.append(""+c+"、");
								if(boolZJJGMC){
									if(cell.get("ZJJGMC")!=null&&!"".equals(cell.get("ZJJGMC").toString())){
										sb.append(cell.get("ZJJGMC")==null?"":cell.get("ZJJGMC").toString());
									}
								}
								
								if(boolLXR){
									if(cell.get("LXR")!=null&&!"".equals(cell.get("LXR").toString())){
										if(boolZJJGMC){
											sb.append("==>");
										}
										sb.append(cell.get("LXR")==null?"":cell.get("LXR").toString());
									}
								}
								
								if(boolLXDH){
									if(cell.get("LXDH")!=null&&!"".equals(cell.get("LXDH").toString())){
										if(boolZJJGMC||boolLXR){
											sb.append("==>");
										}
										sb.append(cell.get("LXDH")==null?"":cell.get("LXDH").toString());
									}
								}
								
								if(boolLXYX){
									if(cell.get("LXYX")!=null&&!"".equals(cell.get("LXYX").toString())){
										if(boolZJJGMC||boolLXR||boolLXDH){
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
						}else{
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
							countSize++;
						}
					}else{
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
					if(i==0&&pjInstanceid.longValue()!=Integer.valueOf(hashMap.get("LCID").toString()).longValue()){
						StringBuffer sb=new StringBuffer();
						int c=1;
						if(xmcyList.size()>0){
							for (HashMap cell : xmcyList) {
								sb.append(""+c+"、");
								if(boolNAME){
									if(cell.get("NAME")!=null&&!"".equals(cell.get("NAME").toString())){
										sb.append(cell.get("NAME")==null?"":cell.get("NAME").toString());
									}
								}
								
								if(boolPOSITION){
									if(cell.get("POSITION")!=null&&!"".equals(cell.get("POSITION").toString())){
										if(boolNAME){
											sb.append("==>");
										}
										sb.append(cell.get("POSITION")==null?"":cell.get("POSITION").toString());
									}
								}
								
								if(boolTEL){
									if(cell.get("TEL")!=null&&!"".equals(cell.get("TEL").toString())){
										if(boolNAME||boolPOSITION){
											sb.append("==>");
										}
										sb.append(cell.get("TEL")==null?"":cell.get("TEL").toString());
									}
								}
								
								if(boolPHONE){
									if(cell.get("PHONE")!=null&&!"".equals(cell.get("PHONE").toString())){
										if(boolNAME||boolPOSITION||boolTEL){
											sb.append("==>");
										}
										sb.append(cell.get("PHONE")==null?"":cell.get("PHONE").toString());
									}
								}
								
								if(boolEMAIL){
									if(cell.get("EMAIL")!=null&&!"".equals(cell.get("EMAIL").toString())){
										if(boolNAME||boolPOSITION||boolTEL||boolPHONE){
											sb.append("==>");
										}
										sb.append(cell.get("EMAIL")==null?"":cell.get("EMAIL").toString());
									}
								}
								if(boolCYMEMO){
									if(cell.get("MEMO")!=null&&!"".equals(cell.get("MEMO").toString())){
										if(boolNAME||boolPOSITION||boolTEL||boolEMAIL||boolPHONE){
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
						}else{
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
							countSize++;
						}
					}else{
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
					
					if(i==0&&pjInstanceid.longValue()!=Integer.valueOf(hashMap.get("LCID").toString()).longValue()){
						StringBuffer sb=new StringBuffer();
						int c=1;
						if(cljgList.size()>0){
							for (HashMap cell : cljgList) {
								sb.append(""+c+"、");
								if(boolCLJGMC){
									if(cell.get("JGMC")!=null&&!"".equals(cell.get("JGMC").toString())){
										sb.append(cell.get("JGMC").toString());
									}
								}
								
								if(boolCLJGFPBL){
									if(cell.get("FPBL")!=null&&!"".equals(cell.get("FPBL").toString())){
										if(boolCLJGMC){
											sb.append("==>");
										}
										sb.append(cell.get("FPBL").toString());
									}
								}
								
								if(boolCLJGTBR){
									if(cell.get("TBR")!=null&&!"".equals(cell.get("TBR").toString())){
										if(boolCLJGMC||boolCLJGFPBL){
											sb.append("==>");
										}
										sb.append(cell.get("TBR").toString());
									}
								}
								
								if(boolCLJGTBSJ){
									if(cell.get("TBSJ")!=null&&!"".equals(cell.get("TBSJ").toString())){
										if(boolCLJGMC||boolCLJGFPBL||boolCLJGTBR){
											sb.append("==>");
										}
										sb.append(cell.get("TBSJ").toString());
									}
								}
								sb.append("\r\n");
								c++;
							}
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue(sb.toString());
							cell2.setCellStyle(style6);
							countSize++;
						}else{
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
							countSize++;
						}
					}else{
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
					
					if(i==0&&pjInstanceid.longValue()!=Integer.valueOf(hashMap.get("LCID").toString()).longValue()){
						StringBuffer sb=new StringBuffer();
						int c=1;
						if(clrList.size()>0){
							for (HashMap cell : clrList) {
								sb.append(""+c+"、");
								if(boolCLR){
									if(cell.get("CLR")!=null&&!"".equals(cell.get("CLR").toString())){
										sb.append(cell.get("CLR").toString());
									}
								}
								
								if(boolCLRFPBL){
									if(cell.get("FPBL")!=null&&!"".equals(cell.get("FPBL").toString())){
										if(boolCLR){
											sb.append("==>");
										}
										sb.append(cell.get("FPBL").toString());
									}
								}
								
								if(boolCLRTBR){
									if(cell.get("TBR")!=null&&!"".equals(cell.get("TBR").toString())){
										if(boolCLR||boolCLRFPBL){
											sb.append("==>");
										}
										sb.append(cell.get("TBR").toString());
									}
								}
								
								if(boolCLJGTBSJ){
									if(cell.get("TBSJ")!=null&&!"".equals(cell.get("TBSJ").toString())){
										if(boolCLR||boolCLRFPBL||boolCLRTBR){
											sb.append("==>");
										}
										sb.append(cell.get("TBSJ").toString());
									}
								}
								if(boolCLRTBSJ){
									if(cell.get("SSBM")!=null&&!"".equals(cell.get("SSBM").toString())){
										if(boolCLR||boolCLRFPBL||boolCLRTBR||boolCLRFPBM){
											sb.append("==>");
										}
										sb.append(cell.get("SSBM").toString());
									}
								}
								sb.append("\r\n");
								c++;
							}
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue(sb.toString());
							cell2.setCellStyle(style6);
							countSize++;
						}else{
							cell2 = row2.createCell((short) n++);
							cell2.setCellValue("");
							cell2.setCellStyle(style4);
							countSize++;
						}
					}else{
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
					}*/
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
					   if("PROJECTNAME".equals(o.toString())
						||"CUSTOMERNAME".equals(o.toString())
						||"OWNER".equals(o.toString())
						||"MANAGER".equals(o.toString())
						||"STARTDATE".equals(o.toString())
						||"ENDDATE".equals(o.toString())
						||"KHLXR".equals(o.toString())
						||"KHLXDH".equals(o.toString())
						||"GGJZR".equals(o.toString())
						||"SBJZR".equals(o.toString())
						||"HTJE".equals(o.toString())
						||"MEMO".equals(o.toString())
						||"XMBZ".equals(o.toString())
						||"ZCLR".equals(o.toString())
						||"ZCLRDH".equals(o.toString())
						||"FZJGMC".equals(o.toString())
						||"WBCLRJG".equals(o.toString())
						||"FZJGLXR".equals(o.toString())
						||"XMBZ".equals(o.toString())
						||"SSSYB".equals(o.toString())
						||"CLSLR".equals(o.toString())
						||"SHTGR".equals(o.toString())
						||"A01".equals(o.toString())
						||"ZJJGMC".equals(o.toString())||"LXR".equals(o.toString())||"LXDH".equals(o.toString())||"LXYX".equals(o.toString())
						||"CLJGMC".equals(o.toString())||"CLJGFPBL".equals(o.toString())||"CLJGTBR".equals(o.toString())||"CLJGTBSJ".equals(o.toString())
						||"CLR".equals(o.toString())||"CLRFPBL".equals(o.toString())||"CLRTBR".equals(o.toString())||"CLRTBSJ".equals(o.toString())||"CLRFPBM".equals(o.toString())
						||"NAME".equals(o.toString())||"POSITION".equals(o.toString())||"TEL".equals(o.toString())||"PHONE".equals(o.toString())||"EMAIL".equals(o.toString())||"CYMEMO".equals(o.toString())
								){
							int parseInt = Integer.parseInt(map.get(o)==null?"0":map.get(o).toString());
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
						   if("PROJECTNAME".equals(o.toString())
							||"CUSTOMERNAME".equals(o.toString())
							||"OWNER".equals(o.toString())
							||"MANAGER".equals(o.toString())
							||"STARTDATE".equals(o.toString())
							||"ENDDATE".equals(o.toString())
							||"KHLXR".equals(o.toString())
							||"KHLXDH".equals(o.toString())
							||"GGJZR".equals(o.toString())
							||"SBJZR".equals(o.toString())
							||"HTJE".equals(o.toString())
							||"MEMO".equals(o.toString())
							||"XMBZ".equals(o.toString())
							||"ZCLR".equals(o.toString())
							||"ZCLRDH".equals(o.toString())
							||"FZJGMC".equals(o.toString())
							||"WBCLRJG".equals(o.toString())
							||"FZJGLXR".equals(o.toString())
							||"XMBZ".equals(o.toString())
							||"SSSYB".equals(o.toString())
							||"CLSLR".equals(o.toString())
							||"SHTGR".equals(o.toString())
							||"A01".equals(o.toString())
							||"ZJJGMC".equals(o.toString())||"LXR".equals(o.toString())||"LXDH".equals(o.toString())||"LXYX".equals(o.toString())
							||"CLJGMC".equals(o.toString())||"CLJGFPBL".equals(o.toString())||"CLJGTBR".equals(o.toString())||"CLJGTBSJ".equals(o.toString())
							||"CLR".equals(o.toString())||"CLRFPBL".equals(o.toString())||"CLRTBR".equals(o.toString())||"CLRTBSJ".equals(o.toString())||"CLRFPBM".equals(o.toString())
							||"NAME".equals(o.toString())||"POSITION".equals(o.toString())||"TEL".equals(o.toString())||"PHONE".equals(o.toString())||"EMAIL".equals(o.toString())||"CYMEMO".equals(o.toString())
									){
								int parseInt = Integer.parseInt(map.get(o)==null?"0":map.get(o).toString());
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
				if(checkCol(o)){
					int parseInt = Integer.parseInt(map.get(o)==null?"0":map.get(o).toString());
					int size3=beginSize-1;
					sheet.addMergedRegion(new Region(hbRow, (short) parseInt, hbRow+size3, (short) parseInt));
				}
			}
		}else if(hbcount==1&&beginSize>1){
			for(Object o : map.keySet()){
				if(checkCol(o)){
					int parseInt = Integer.parseInt(map.get(o)==null?"0":map.get(o).toString());
					int size3=beginSize-1;
					sheet.addMergedRegion(new Region(hbRow, (short) parseInt, hbRow+size3, (short) parseInt));
				}
			}
		}
		for(Object o : sheetMap.keySet()){
			if(sheetMap.get(o.toString())!=null&&Short.parseShort(sheetMap.get(o.toString()).toString())>0){
				sheet.setColumnWidth(Short.parseShort(map.get(o.toString()).toString()), Short.parseShort(sheetMap.get(o.toString()).toString()));
			}
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition="";
			if(type.equals("run")){
				disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("正在执行项目信息汇总表.xls");
			}else if(type.equals("cxdd")){
				disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("持续督导项目信息汇总表.xls");
			}
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			try {
				wb.write(out1);
			} catch (Exception e) {}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					
				}
			}

		}
	}

	public boolean saveRz(String projectNo, String groupid, Double rzje) {
		Map<String,String> config=ConfigUtil.readAllProperties("/strings_zh-CN.properties");//获取连接网址配置
		String string = config.get("string_count");
		HashMap conditionMap = new HashMap();
		conditionMap.put("PROJECTNO", projectNo);
		List<HashMap> list = DemAPI.getInstance().getList(ProjectItemUUID,
				conditionMap, "ID");
		boolean isNull=false;
		int count=0;
		String groupidjl="";
		Map params = new HashMap();
		params.put(1, groupid);
		if("1".equals(string)){
			for (HashMap hashMap : list) {
				int fs = DBUTilNew.getInt("SFKRZ","SELECT SFKRZ FROM BD_ZQB_KM_INFO WHERE ID= ? ", params);
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
		List<HashMap> list = DemAPI.getInstance().getList(ProjectItemUUID,conditionMap, "ID");
		Map params = new HashMap();
		params.put(1, groupid);
		if("1".equals(string)){
			for (HashMap hashMap : list) {
				if(DBUTilNew.getInt("SFKRZ","SELECT SFKRZ FROM BD_ZQB_KM_INFO WHERE ID= ? ", params)==1){
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

	public List<HashMap> showDailyList(String projectNo, int pageNumber, int pageSize,Long formid,String createuser) {
		return zqbProjectManageDAO.getShowDailyList(projectNo,pageNumber,pageSize,formid,createuser);
	}
	
	public List<HashMap> showDailyListForIndex(String projectNo, int pageNumber, int pageSize,String startdate,String enddate, Long formid,String createuser) {
		return zqbProjectManageDAO.showDailyListForIndex(projectNo,pageNumber,pageSize,startdate,enddate,formid,createuser);
	}
	
	public int showDailyListSize(String projectNo,String projectname) {
		if(projectNo==null||"".equals(projectNo)){
			return 0;
		}
		OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();;
		Long isManager = orgUser.getIsmanager();
		Long orgRoleid = orgUser.getOrgroleid();
		String userid = orgUser.getUserid();
		StringBuffer sb=new StringBuffer();
		Map params = new HashMap();
//		params.put(1, projectNo);
		int n=1;
		sb.append("select count (*) count from (select projectdate,progress,username,tel,tracking,projectno from bd_zqb_xmrbb where 1=1 ");//and projectno= ? 
//		if(orgRoleid!=5){
//			if(isManager==1l){
//				sb.append(" AND CREATEUSERID IN (SELECT O.USERID FROM ORGUSER O WHERE O.DEPARTMENTID IN (select id from orgdepartment t start with t.id=(SELECT DEPARTMENTID FROM ORGUSER O WHERE O.USERID=　?　) connect by prior t.id=t.parentdepartmentid))");
//			}else{
//				sb.append(" AND CREATEUSERID = ?");
//			}
//			params.put(n, userid);
//			n++;
//		}
		
		if (projectNo != null && !"".equals(projectNo)) {
			sb.append(" AND XMBH like ?");
			params.put(n,"%"+projectNo+"%");
			n++;
		}
		/*if (projectname != null && !"".equals(projectname)) {
			sb.append(" OR PROJECTNO like ?");
			params.put(n,"%"+projectname+"%");
			n++;
		}*/
		
		
		sb.append(")");
		int count=DBUTilNew.getInt("count",sb.toString(),params);
		return count;
	}
	
	public HashMap getSingleDaily(Long instanceid){
		if(instanceid==null) return null;
		HashMap dailyData = DemAPI.getInstance().getFromData(instanceid);
		if(dailyData==null) return null;
		Object fj = dailyData.get("FJ");
		if(fj!=null){
			StringBuffer fjHtml = new StringBuffer("");
			List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(fj.toString());
			for (FileUpload fileUpload : sublist) {
				String xgzl2=fileUpload.getFileSrcName();
				String uuid2=fileUpload.getFileId();
				try {
					fjHtml.append("<div id=\"").append(fileUpload.getFileId()).append("\" style=\"background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\">");
					fjHtml.append("<div style=\"align:right;float: right;\">");
					/*fjHtml.append("<a href=\"javascript:uploadifyReomve('XGZL','").append(fileUpload.getFileId()).append("','").append(fileUpload.getFileId()).append("');\">");
					fjHtml.append("<img src=\"/iwork_img/del3.gif\">");
					fjHtml.append("</a>");*/
					fjHtml.append("</div>");
					fjHtml.append("<span>");
					fjHtml.append("<a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\">");
					fjHtml.append("<img src=\"/iwork_img/attach.png\">");
					fjHtml.append(fileUpload.getFileSrcName());
					fjHtml.append("</a>");
					fjHtml.append("</span>");
					fjHtml.append("</div>");
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
			dailyData.put("FJ", fjHtml.toString());
		}
		return dailyData;
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
		sheetMap.put("7", 0);
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
		cell1.setCellValue("填报人");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("日报日期");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("项目阶段");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("工作内容");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("进度说明");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("备注说明");
		cell1.setCellStyle(style5);
		for (HashMap map : dailyList) {
			HSSFRow row2 = sheet.createRow((int) m++);
			HSSFCell cell2 = row2.createCell((short) n++);
			short colLength2 = (short) (map.get("PROJECTNO") == null ? 0: map.get("PROJECTNO").toString().length() * 256 * 2);
			cell2.setCellValue(map.get("PROJECTNO")==null?"":map.get("PROJECTNO").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("PROJECTNO") == null ? 0: map.get("PROJECTNO").toString().length() * 256 * 2);
			sheetMap.put("1", colLength2>Short.parseShort(sheetMap.get("1").toString())?colLength2:Short.parseShort(sheetMap.get("1").toString()));
			cell2 = row2.createCell((short) n++);
			
			cell2.setCellValue(map.get("CREATEUSER")==null?"":map.get("CREATEUSER").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("CREATEUSER") == null ? 0: map.get("CREATEUSER").toString().length() * 256 * 2);
			sheetMap.put("2", colLength2>Short.parseShort(sheetMap.get("2").toString())?colLength2:Short.parseShort(sheetMap.get("2").toString()));
			cell2 = row2.createCell((short) n++);
			
			cell2.setCellValue(map.get("PROJECTDATE")==null?"":map.get("PROJECTDATE").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("PROJECTDATE") == null ? 0: map.get("PROJECTDATE").toString().length() * 256 * 2);
			sheetMap.put("3", colLength2>Short.parseShort(sheetMap.get("3").toString())?colLength2:Short.parseShort(sheetMap.get("3").toString()));
			cell2 = row2.createCell((short) n++);
			
			
			cell2.setCellValue(map.get("TRACKING")==null?"":map.get("TRACKING").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("TRACKING") == null ? 0: map.get("TRACKING").toString().length() * 256 * 2);
			sheetMap.put("4", colLength2>Short.parseShort(sheetMap.get("4").toString())?colLength2:Short.parseShort(sheetMap.get("4").toString()));
			cell2 = row2.createCell((short) n++);
			
			
			cell2.setCellValue(map.get("PROGRESS")==null?"":map.get("PROGRESS").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("PROGRESS") == null ? 0	: map.get("PROGRESS").toString().length() * 256 * 2);
			sheetMap.put("5", colLength2>Short.parseShort(sheetMap.get("5").toString())?colLength2:Short.parseShort(sheetMap.get("5").toString()));
			cell2 = row2.createCell((short) n++);
			
			cell2.setCellValue(map.get("USERNAME")==null?"":map.get("USERNAME").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("USERNAME") == null ? 0: map.get("USERNAME").toString().length() * 256 * 2);
			sheetMap.put("6", colLength2>Short.parseShort(sheetMap.get("6").toString())?colLength2:Short.parseShort(sheetMap.get("6").toString()));
			cell2 = row2.createCell((short) n++);
			
			
			cell2.setCellValue(map.get("TEL")==null?"":map.get("TEL").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("TEL") == null ? 0: map.get("TEL").toString().length() * 256 * 2);
			sheetMap.put("7", colLength2>Short.parseShort(sheetMap.get("7").toString())?colLength2:Short.parseShort(sheetMap.get("7").toString()));
			n=0;
		}
		int h=1;
		for (HashMap hashMap : dailyListSize) {
			if(!"0".equals(hashMap.get("COUNT").toString())){
				Integer count = Integer.parseInt(hashMap.get("COUNT").toString());
				sheet.addMergedRegion(new Region(h, (short) 0, h-1+count, (short) 0));
				h+=count;
			}
		}
		for(Object o : sheetMap.keySet()){
			if(Short.parseShort(sheetMap.get(o.toString()).toString())>0){
				sheet.setColumnWidth(Short.parseShort(o.toString())-1, Short.parseShort(sheetMap.get(o.toString()).toString()));
			}
		}
	/*	sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(2,8000);
		sheet.setColumnWidth(2,8000);
		sheet.setColumnWidth(2,8000);*/
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
	
	public List<HashMap> getDaily(String projectno){
		boolean superman = this.getIsSuperMan();
//		List<String> tmplist = new ArrayList<String>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		/*if (superman) {
			tmplist = zqbProjectManageDAO.getDailyRunProjectList();
		} else {
			tmplist = zqbProjectManageDAO.getDailyRunProjectList1(owner);
		}*/
//		tmplist.add(projectno);
		List<HashMap> list = new ArrayList<HashMap>();
		list=zqbProjectManageDAO.getDaily(projectno);
		return list;
	}
	
	public List<HashMap> getDailySize(String projectno){
		boolean superman = this.getIsSuperMan();
		List<String> tmplist = new ArrayList<String>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		/*if (superman) {
			tmplist = zqbProjectManageDAO.getDailyRunProjectList();
		} else {
			tmplist = zqbProjectManageDAO.getDailyRunProjectList1(owner);
		}*/
		tmplist.add(projectno);
		List<HashMap> list = new ArrayList<HashMap>();
		list=zqbProjectManageDAO.getDailySize(tmplist);
		return list;
	}
	public boolean getIsViewDg() {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		String spr= DBUtil.getString("select NHSPR from BD_ZQB_ZKNHSPR ","NHSPR");
		if(owner.equals(spr)){
			return true;
		}else{
			return false;
		}
	}

	public String checkPJRecord(String projectno, String groupid, String pjr,Long instanceId) {
		String info="";
		if(instanceId==0){
			String PROJECT_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '项目任务评价表单'", "UUID");
			HashMap conditionMap = new HashMap();
			conditionMap.put("PROJECTNO", projectno);
			conditionMap.put("GROUPID", groupid);
			conditionMap.put("PJR", pjr);
			List<HashMap> list = DemAPI.getInstance().getList(PROJECT_UUID,
					conditionMap, "ID");
			if(list.size()>0){
				info="该阶段您已评价，不可以重复评价。";
			}else{
				String PROJECT_TASK_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '项目任务管理'", "UUID");
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
	
	public void expProjecWordDZ(HttpServletResponse response,Long instanceId) {
		HashMap fromData = zqbProjectManageDAO.getExpProjectWordDataDZ(instanceId);
		String id = fromData.get("ID").toString();
		String projectno = fromData.get("PROJECTNO")==null?"":fromData.get("PROJECTNO").toString();
		String yrz = DBUtil.getString("SELECT SUM(RZJE) YRZ FROM BD_ZQB_XMJSXXGLB WHERE PROJECTNO='"+projectno+"' AND ID > "+id, "YRZ");
		BigDecimal yzrje = new BigDecimal(yrz==null||yrz.equals("")?"0.00":yrz).multiply(new BigDecimal("10000")).divide(new BigDecimal("1.06"),2,BigDecimal.ROUND_HALF_UP);//已入账金额
		
		List<LinkedHashMap<String,String>> linkedList= new ArrayList<LinkedHashMap<String,String>>();
		LinkedHashMap<String,String> linkedMap=null;
		LinkedHashMap<String,String> linkedMap_=null;
		Long lcID = Long.parseLong(fromData.get("LCBS")==null?"0":fromData.get("LCBS").toString());
		List<HashMap> clr=new ArrayList<HashMap>();
		List<HashMap> cljg=new ArrayList<HashMap>();
		List<HashMap> lcClr=new ArrayList<HashMap>();
		List<HashMap> lcCljg=new ArrayList<HashMap>();
		
		BigDecimal BD = new BigDecimal("1.00");
		BigDecimal sqywsr = new BigDecimal(fromData.get("RZJE").toString()).multiply(new BigDecimal("10000")).divide(BD,2,BigDecimal.ROUND_HALF_UP);
		BigDecimal sl = new BigDecimal("1.06");
		BigDecimal shywsr = sqywsr.divide(sl,2,BigDecimal.ROUND_HALF_UP);
		
		Long dataInstanceId = Long.parseLong(fromData.get("INSTANCEID")==null?"0":fromData.get("INSTANCEID").toString());
		//setPjLocking(dataInstanceId);
		
		clr = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLRGPFX");
		if(clr==null){
			clr=new ArrayList<HashMap>();
		}
		cljg = cljg=new ArrayList<HashMap>();
		linkedMap=new LinkedHashMap();
		linkedMap.put("JGMC", "场外企业融资部");
		linkedMap.put("FPBL", "");
		
		BigDecimal add = yzrje.add(shywsr);
		BigDecimal fpbm = new BigDecimal(fromData.get("FPBM")==null?"0":fromData.get("FPBM").toString()).multiply(new BigDecimal("10000")).divide(BD,2,BigDecimal.ROUND_HALF_UP);
		
		String qyrzbje = "";
		String czbmj_e = "";
		if(yzrje.compareTo(fpbm)>0){
			czbmj_e = shywsr.toString();
			qyrzbje = "";
		}
		if(yzrje.compareTo(fpbm)<=0&&add.compareTo(fpbm)>0){
			czbmj_e = add.subtract(fpbm).toString();
			qyrzbje = shywsr.subtract(new BigDecimal(czbmj_e)).toString();
		}
		if(add.compareTo(fpbm)<=0){
			czbmj_e = "";
			qyrzbje = shywsr.toString();
		}
		BigDecimal czbmje = shywsr.subtract(fpbm).divide(BD,2,BigDecimal.ROUND_HALF_UP);
		
		linkedMap.put("FPJE",qyrzbje); 
		linkedList.add(linkedMap);
		linkedMap_=new LinkedHashMap();
		linkedMap_.put("JGMC", fromData.get("CZBM")==null?"":fromData.get("CZBM").toString());
		linkedMap_.put("FPBL", "");
		
		BigDecimal htje = new BigDecimal(fromData.get("SJFXGPJG")==null?"0":fromData.get("SJFXGPJG").toString()).multiply(new BigDecimal("10000"));
		String ht = czbmje.longValue()<=0l?"0":czbmje.toString();
		
		linkedMap_.put("FPJE",czbmj_e);
		linkedList.add(linkedMap_);
		
		String projectname=fromData.get("PROJECTNAME")==null?fromData.get("CUSTOMERNAME").toString():fromData.get("PROJECTNAME").toString();
		HashMap dataMap=new HashMap();
		dataMap.put("PROJECTNAME", projectname);
		dataMap.put("SQYWSR", sqywsr);
		dataMap.put("SHYWSR", shywsr);
		dataMap.put("MONTH", fromData.get("RZRQ").toString());
		dataMap.put("XMLX", fromData.get("XMLX").toString());
		dataMap.put("XYZCBL", 0);
		dataMap.put("clr", clr);
		dataMap.put("cljg", linkedList);
		dataMap.put("cljgsize", linkedList.size());
		dataMap.put("dzjeFromCaculate", "");
		String createWord = ConfigurationAPI.getInstance().createWord(response,dataMap,"xmjsdForDxzf.ftl");
		File file = new File(createWord);
		InputStream is=null;
		OutputStream out1 = null;
		String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目结算单.doc");
		try {
			is = new FileInputStream(createWord);
			out1 = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-word;charset=UTF-8");
			response.setHeader("Content-Disposition",disposition);
			byte[] b = new byte[1024];   
			int len;
			while ((len=is.read(b)) >0) {   
				out1.write(b,0,len);   
			}
			if (is != null) {
				is.close();
			}
			file.delete();
		} catch (Exception e) {
		}finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void expProjecWord(HttpServletResponse response,Long instanceId) {
		HashMap fromData = zqbProjectManageDAO.getExpProjectWordData(instanceId);
		List<LinkedHashMap<String,String>> linkedList= new ArrayList<LinkedHashMap<String,String>>();
		LinkedHashMap<String,String> linkedMap=null;
		Long lcID = Long.parseLong(fromData.get("LCBS").toString());
		List<HashMap> clr=new ArrayList<HashMap>();
		List<HashMap> cljg=new ArrayList<HashMap>();
		List<HashMap> lcClr=new ArrayList<HashMap>();
		List<HashMap> lcCljg=new ArrayList<HashMap>();
		boolean xmlxcheck = fromData.get("XMLX")!=null&&fromData.get("XMLX").equals("定增");//判断是否定增项目
		BigDecimal sqywsr = new BigDecimal(fromData.get("RZJE").toString()).multiply(new BigDecimal("10000"));
		BigDecimal sl = new BigDecimal("1.06");
		BigDecimal shywsr = sqywsr.divide(sl,2,BigDecimal.ROUND_HALF_UP);
		String xyzcbl=fromData.get("XYZCBL")==null?"":fromData.get("XYZCBL").toString();
		BigDecimal fpje=new BigDecimal(0);
		BigDecimal addJe=new BigDecimal(0);
		if(!xmlxcheck){
			if(!xyzcbl.equals("")){
				BigDecimal xyzcblje = sqywsr.multiply(new BigDecimal(xyzcbl).divide(new BigDecimal("100"),6,BigDecimal.ROUND_FLOOR)).setScale(2,BigDecimal.ROUND_FLOOR);
				addJe=addJe.add(xyzcblje);
				fpje = shywsr.subtract(xyzcblje);
			}
		}
		
		Long dataInstanceId = Long.parseLong(fromData.get("INSTANCEID").toString());
		setPjLocking(dataInstanceId);
		if(lcID>0){
			clr = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLR");
			if(clr==null){
				clr=new ArrayList<HashMap>();
			}
			cljg = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLJG");
			if(cljg==null){
				cljg=new ArrayList<HashMap>();
			}else{
				int cljgSize = cljg.size();
				int index = 1;
				for (HashMap cljgMap : cljg) {
					linkedMap=new LinkedHashMap();
					linkedMap.put("JGMC", cljgMap.get("JGMC").toString());
					linkedMap.put("FPBL", cljgMap.get("FPBL").toString());
					if(!cljgMap.get("FPBL").toString().equals("")){
						if(index==cljgSize){
							String je = shywsr.subtract(addJe).toString();
							linkedMap.put("FPJE",je);
						}else{
							String je = fpje.compareTo(new BigDecimal(0))==1?fpje.multiply(new BigDecimal(cljgMap.get("FPBL").toString()).divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP).toString()
									:shywsr.multiply(new BigDecimal(cljgMap.get("FPBL").toString()).divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
							addJe=addJe.add(new BigDecimal(je));
							linkedMap.put("FPJE",je);
						}
					}else{
						linkedMap.put("FPJE","");
					}
					linkedList.add(linkedMap);
				}
			}
		}else{
			clr = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLR");
			if(clr==null){
				clr=new ArrayList<HashMap>();
			}
			cljg = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLJG");
			if(cljg==null){
				cljg=new ArrayList<HashMap>();
			}else{
				int cljgSize = cljg.size();
				int index = 1;
				for (HashMap cljgMap : cljg) {
					linkedMap=new LinkedHashMap();
					linkedMap.put("JGMC", cljgMap.get("JGMC").toString());
					linkedMap.put("FPBL", cljgMap.get("FPBL").toString());
					if(!cljgMap.get("FPBL").toString().equals("")){
						if(index==cljgSize){
							String je = shywsr.subtract(addJe).toString();
							linkedMap.put("FPJE",je);
						}else{
							String je = fpje.compareTo(new BigDecimal(0))==1?fpje.multiply(new BigDecimal(cljgMap.get("FPBL").toString()).divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP).toString()
									:shywsr.multiply(new BigDecimal(cljgMap.get("FPBL").toString()).divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
							addJe=addJe.add(new BigDecimal(je));
							linkedMap.put("FPJE",je);
						}
					}else{
						linkedMap.put("FPJE","");
					}
					linkedList.add(linkedMap);
				}
			}
		}
		String projectname=fromData.get("PROJECTNAME")==null?fromData.get("CUSTOMERNAME").toString():fromData.get("PROJECTNAME").toString();
		Object dzje = fromData.get("DZJE");
		HashMap dataMap=new HashMap();
		dataMap.put("PROJECTNAME", projectname);
		dataMap.put("SQYWSR", sqywsr);
		dataMap.put("SHYWSR", shywsr);
		dataMap.put("MONTH", fromData.get("RZRQ").toString());
		dataMap.put("XMLX", xmlxcheck?"定增":fromData.get("XMLX").toString());
		dataMap.put("XYZCBL", Double.parseDouble(fromData.get("XYZCBL").toString()));
		dataMap.put("clr", clr);
		dataMap.put("cljg", linkedList);
		dataMap.put("cljgsize", cljg.size());
		String createWord;
		if(xmlxcheck){
			if(dzje!=null&&!dzje.equals("")){
				if(dzje.toString().contains("%")){
					NumberFormat fn = NumberFormat.getPercentInstance();
					BigDecimal rzje = null;
					BigDecimal dzbfbbigdata = null;
					try {
						Number dzbfb= fn.parse(dzje.toString());
						rzje = new BigDecimal(fromData.get("RZJE").toString());
						dzbfbbigdata = new BigDecimal(dzbfb.toString());
					} catch (ParseException e) {}finally{
						if(rzje!=null&&dzbfbbigdata!=null){
							dataMap.put("dzjeFromCaculate", rzje.multiply(dzbfbbigdata));
						}else{
							dataMap.put("dzjeFromCaculate", "");
						}
					}
				}else{
					dataMap.put("dzjeFromCaculate", dzje);
				}
			}else{
				dataMap.put("dzjeFromCaculate", "");
			}
			createWord = ConfigurationAPI.getInstance().createWord(response,dataMap,"xmjsdForDxzf.ftl");
		}else{
			createWord = ConfigurationAPI.getInstance().createWord(response,dataMap,"xmjsd.ftl");
		}
		File file = new File(createWord);
		InputStream is=null;
		OutputStream out1 = null;
		String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目结算单.doc");
		try {
			is = new FileInputStream(createWord);
			out1 = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-word;charset=UTF-8");
			response.setHeader("Content-Disposition",disposition);
			byte[] b = new byte[1024];   
			int len;
			while ((len=is.read(b)) >0) {   
				out1.write(b,0,len);   
			}
			if (is != null) {
				is.close();
			}
			file.delete();
		} catch (Exception e) {
		}finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public String getRestNum(Long instanceId,String ids) {
		BigDecimal bd100=new BigDecimal("100.00");
		String fpbl = "";
		bd100 = getCljgRest(instanceId, bd100);
		
		String num = "0";
		if(ids!=null&&!ids.equals("")&&!ids.equals("0")){
			
			Map params = new HashMap();
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT SUM(TO_NUMBER(FPBL)) NUM FROM BD_ZQB_CLJG WHERE ID IN(  ");
			String[] strIDs = ids.split(",");
			int n=1;
			for (int i = 0; i < strIDs.length; i++) {
				if(i==(strIDs.length-1)){
					sql.append("?");
				}else{
					sql.append("?,");
				}
				params.put(n,strIDs[i].replaceAll("'", ""));
				n++;
			}
			sql.append(" ) ");
			
			//num = DBUtil.getString("SELECT SUM(TO_NUMBER(FPBL)) NUM FROM BD_ZQB_CLJG WHERE ID IN("+ids+")", "NUM");
			num = DBUTilNew.getDataStr("NUM",sql.toString(), params);
		}
		if(num==null||num.equals(""))
			num="0";
		BigDecimal bnum=new BigDecimal(num);
		num = (bd100.floatValue()+bnum.floatValue()+"");
		BigDecimal sumbnum = new BigDecimal(num);
		
		return sumbnum.toString();
	}

	public String getFpblRest(Long instanceId){
		BigDecimal bd100=new BigDecimal("100.00");
		String fpbl = "";
		
		bd100 = getClrRest(instanceId, bd100);
		
		return bd100.toString();
	}

	private BigDecimal getClrRest(Long instanceId, BigDecimal bd100) {
		String fpbl;
		List<HashMap> clr=new ArrayList<HashMap>();
		List<HashMap> lcCLR=new ArrayList<HashMap>();
		lcCLR=ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
		if(lcCLR==null){
			lcCLR=new ArrayList<HashMap>();
		}
		clr=DemAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
		if(clr==null){
			clr=new ArrayList<HashMap>();
		}
		if(!lcCLR.isEmpty()){
			clr.addAll(lcCLR);
			clr=ListUtil.cleanList(clr, true, "ID");
		}
		for (HashMap hashMap : clr) {
			fpbl = hashMap.get("FPBL").toString();
			bd100=bd100.subtract(new BigDecimal(fpbl.equals("")?"0":fpbl));
		}
		return bd100;
	}
	
	private BigDecimal getCljgRest(Long instanceId, BigDecimal bd100) {
		List<HashMap> cljg=new ArrayList<HashMap>();
		List<HashMap> lcCljg=new ArrayList<HashMap>();
		lcCljg = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
		if(lcCljg==null){
			lcCljg=new ArrayList<HashMap>();
		}
		cljg=DemAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
		if(cljg==null){
			cljg=new ArrayList<HashMap>();
		}
		if(!lcCljg.isEmpty()){
			cljg.addAll(lcCljg);
			cljg=ListUtil.cleanList(cljg, true, "ID");
		}
		for (HashMap hashMap : cljg) {
			bd100=bd100.subtract(new BigDecimal(hashMap.get("FPBL").toString()));
		}
		return bd100;
	}
	
	public List<HashMap> getPjSettlementSheetList(String customername,String rzrq,String ordertype,int pageNumber, int pageSize) {
		int pageSize1 = pageNumber*pageSize;
		int startRow1 = (pageNumber - 1) * pageSize;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		String username = uc._userModel.getUsername();
		String owner = uc._userModel.getUserid();
		boolean isSuperMan = this.getIsSuperMan();
		StringBuffer sql=new StringBuffer();
		String config = SystemConfig._xmlcConf.getConfig();
		Map<String , Object> map = new HashMap<String , Object>();
		List<String> parameter=new ArrayList<String>();//存放参数
		sql.append("SELECT * FROM (SELECT C.CUSTOMERNAME,C.XMLX,C.RZJE,C.RZRQ,C.CUSTOMERNO,C.PROJECTNO,C.INSTANCEID,ROWNUM RNUM,E.PJINSTANCEID,E.LOCKED,E.ZBSPZT,E.ZBLCBH,E.ZBLCBS,E.ZBSTEPID,E.ZBTASKID FROM (SELECT A.CUSTOMERNAME,B.XMLX,B.RZJE,(CASE WHEN B.RZRQ IS NULL THEN TO_DATE('1900-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS') ELSE B.RZRQ END) RZRQ,A.CUSTOMERNO,A.PROJECTNO,B.INSTANCEID,(CASE WHEN B.DQRQ IS NULL THEN TO_DATE('1900-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS') ELSE B.DQRQ END) DQRQ FROM (SELECT KH.*,CUSTOMER.PROJECTNO,(CASE WHEN CUSTOMER.CJSJ != NULL THEN CUSTOMER.CJSJ WHEN CUSTOMER.CREATEDATE IS NULL THEN TO_DATE('1900-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS') ELSE CUSTOMER.CREATEDATE END) CJSJ,(CASE WHEN SHTGR IS NULL THEN TO_DATE('1900-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS') ELSE SHTGR END) SHTGR FROM BD_ZQB_KH_BASE KH LEFT JOIN (SELECT DISTINCT PJ.CUSTOMERNO,PJ.PROJECTNO,PJ.CREATEDATE,(CASE WHEN PJ.ZBSPZT!='审批通过' OR PJ.ZBSPZT IS NULL THEN TO_DATE('1900-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS') ELSE SUBS.UPDATEDATE END) SHTGR,SUB.CJSJ FROM BD_ZQB_PJ_BASE PJ LEFT JOIN (SELECT DISTINCT MAIN.PROJECTNO,MAX(SUB.CJSJ) CJSJ FROM (SELECT B.CREATEDATE,S.INSTANCEID,B.SHTGR,B.PROJECTNO FROM BD_ZQB_ZBSP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM F WHERE F.IFORM_TITLE='子表流程表单')) MAIN LEFT JOIN (SELECT MAX(CJSJ) CJSJ,INSTANCEID FROM (SELECT B.TBSJ CJSJ,S.INSTANCEID FROM BD_ZQB_CLJG B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM F WHERE F.IFORM_TITLE='承揽机构表单') UNION ALL SELECT B.CJSJ,S.INSTANCEID FROM BD_ZQB_CLR B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM F WHERE F.IFORM_TITLE='承揽人表单')) GROUP BY INSTANCEID) SUB ON MAIN.INSTANCEID=SUB.INSTANCEID GROUP BY MAIN.PROJECTNO) SUB ON PJ.PROJECTNO =SUB.PROJECTNO LEFT JOIN SYS_INSTANCE_DATA SUBS ON PJ.ZBLCBS=SUBS.ID WHERE (PJ.STATUS='已完成' AND PJ.TYPENO='1') OR PJ.STATUS='执行中' ) CUSTOMER ON KH.CUSTOMERNO=CUSTOMER.CUSTOMERNO ");
		if(customername!=null&&!customername.equals("")){
			sql.append(" WHERE UPPER(KH.CUSTOMERNAME) LIKE ? ");
			parameter.add("CUSTOMERNAME");
			map.put("CUSTOMERNAME", customername.toUpperCase());
		}
		sql.append(") A LEFT JOIN (SELECT BZX.USERID,BZX.DQRQ,BZX.XMLX,BZX.RZJE,BZX.RZRQ,BZX.CUSTOMERNO,BZX.CUSTOMERNAME,SEFB.INSTANCEID FROM BD_ZQB_XMJSXXGLB BZX LEFT JOIN SYS_ENGINE_FORM_BIND SEFB ON BZX.ID=SEFB.DATAID WHERE SEFB.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='项目结算信息管理')) B ON A.CUSTOMERNO=B.CUSTOMERNO ");
		if(rzrq!=null&&!rzrq.equals("")){
			sql.append(" WHERE TO_CHAR(RZRQ,'YYYY-MM') = ? ");
			parameter.add("RZRQ");
			map.put("RZRQ", rzrq);		
		}
		if(ordertype!=null&&!ordertype.equals("")){
			if(ordertype.equals("1")){
				sql.append(" ORDER BY A.CJSJ ASC");
			}else if(ordertype.equals("2")){
				sql.append(" ORDER BY A.CJSJ DESC");
			}else if(ordertype.equals("3")){
				sql.append(" ORDER BY A.SHTGR ASC");
			}else{
				sql.append(" ORDER BY A.SHTGR DESC");
			}
		}else{
			sql.append(" ORDER BY RZRQ DESC,CUSTOMERNO,XMLX");
		}
		sql.append(" ) C LEFT JOIN (SELECT PJ.PROJECTNO PROJECTNO,PJ.ZBSPZT,PJ.ZBLCBH,PJ.ZBLCBS,PJ.ZBSTEPID,PJ.ZBTASKID,BIND.INSTANCEID PJINSTANCEID,PJ.A02 LOCKED FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT INSTANCEID,DATAID,METADATAID,FORMID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=91 AND METADATAID=101) BIND ON PJ.ID=BIND.DATAID) E ON C.PROJECTNO=E.PROJECTNO) WHERE RNUM > ? AND RNUM <= ?");
		parameter.add("startRow1");
		map.put("startRow1", startRow1);
		parameter.add("pageSize1");
		map.put("pageSize1", pageSize1);
		return zqbProjectManageDAO.getPjSettlementSheetList(sql.toString(),parameter,map);
	}
	
	public HashMap getPjSettlementSheet(String customername,
			String rzrq,String xmlx,String ordertype,int pageNumber, int pageSize) {
		int pageSize1 = pageNumber*pageSize;
		int startRow1 = (pageNumber - 1) * pageSize;
		StringBuffer sql = new StringBuffer("call DW_JS.DW_GETJS(?,?,?,?,?,?,?,?)");
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("CUSTOMERNAME", customername);
		map.put("RZLX", xmlx);
		map.put("RZY", rzrq);
		map.put("PX", ordertype);
		map.put("PAGESTART", startRow1);
		map.put("PAGEEND", pageSize1);
		
		return zqbProjectManageDAO.getPjSettlementSheet(sql.toString(),map);
	}

	public int getPjSettlementSheetListSize(String customername,String rzrq) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		String username = uc._userModel.getUsername();
		String owner = uc._userModel.getUserid();// + "["
				//+ uc._userModel.getUsername() + "]";
		boolean isSuperMan = this.getIsSuperMan();
		StringBuffer sql=new StringBuffer();
		String config = SystemConfig._xmlcConf.getConfig();
		Map<String , Object> map = new HashMap<String , Object>();
		List<String> parameter=new ArrayList<String>();//存放参数
		sql.append("SELECT COUNT(*) CNUM  FROM (SELECT A.CUSTOMERNAME,B.XMLX,B.RZJE,B.RZRQ,A.CUSTOMERNO,A.PROJECTNO,B.INSTANCEID,ROWNUM RNUM FROM (SELECT KH.*,CUSTOMER.PROJECTNO FROM BD_ZQB_KH_BASE KH LEFT JOIN (SELECT DISTINCT PJ.CUSTOMERNO,PJ.PROJECTNO FROM BD_ZQB_PJ_BASE PJ INNER JOIN BD_PM_TASK TASK ON PJ.PROJECTNO=TASK.PROJECTNO WHERE (PJ.STATUS='已完成' AND PJ.TYPENO='1') OR PJ.STATUS='执行中' ) CUSTOMER ON KH.CUSTOMERNO=CUSTOMER.CUSTOMERNO WHERE 1=1 ");
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND UPPER(KH.CUSTOMERNAME) LIKE ? ");
			parameter.add("CUSTOMERNAME");
			map.put("CUSTOMERNAME", customername.toUpperCase());
		}
		sql.append( " ORDER BY KH.ID) A LEFT JOIN (SELECT BZX.USERID,BZX.DQRQ,BZX.XMLX,BZX.RZJE,BZX.RZRQ,BZX.CUSTOMERNO,BZX.CUSTOMERNAME,SEFB.INSTANCEID FROM BD_ZQB_XMJSXXGLB BZX LEFT JOIN SYS_ENGINE_FORM_BIND SEFB ON BZX.ID=SEFB.DATAID WHERE SEFB.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='项目结算信息管理')) B ON A.CUSTOMERNO=B.CUSTOMERNO)");
		if(rzrq!=null&&!rzrq.equals("")){
			sql.append(" WHERE TO_CHAR(RZRQ,'YYYY-MM') = ? ");
			parameter.add("RZRQ");
			map.put("RZRQ", rzrq);
		}
		return zqbProjectManageDAO.getPjSettlementSheetListSize(sql.toString(),parameter,map);
	}
	

	public List<HashMap> getExpXmjsList(String xmlx,String prodate,String departmentname,Boolean isSuperMan){
		List<HashMap> list = new ArrayList<HashMap>();
		return zqbProjectManageDAO.getExpXmjsList(xmlx,prodate,departmentname,isSuperMan);
	}
	
	public List<HashMap> getExpXmjsSptgList(String xmlx,String prodate,String departmentname,Boolean isSuperMan){
		List<HashMap> list = new ArrayList<HashMap>();
		return zqbProjectManageDAO.getExpXmjsSptgList(xmlx,prodate,departmentname,isSuperMan);
	}
	
	public List<HashMap> getExpXmjsWspList(String xmlx,String prodate,String departmentname,Boolean isSuperMan){
		List<HashMap> list = new ArrayList<HashMap>();
		return zqbProjectManageDAO.getExpXmjsWspList(xmlx,prodate,departmentname,isSuperMan);
	}

	public void expxmjs(HttpServletResponse response, String prodate,String xmlxArr,String status) {
		if((status!=null&&status.equals("1")&&prodate!=null&&!prodate.equals(""))&&(xmlxArr!=null&&!xmlxArr.equals(""))||(status!=null&&status.equals("0")&&xmlxArr!=null&&!xmlxArr.equals(""))){
			String[] keyArr = xmlxArr.split(",");
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			int month = 0;
			if(status.equals("1")){
				Date date = UtilDate.StringToDate(prodate,"yyyy-MM");
				month = UtilDate.getMonth(date);
			}else if(status.equals("0")){
				month = UtilDate.getMonth(new Date());
			}
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFCellStyle style = wb.createCellStyle();
			HSSFCellStyle rstyle = wb.createCellStyle();
			HSSFCellStyle style1 = wb.createCellStyle();
			HSSFCellStyle style4 = wb.createCellStyle();
			HSSFFont headfont = wb.createFont();
			headfont.setFontName("宋体");
			headfont.setFontHeightInPoints((short) 11);// 字体大小
			
			HSSFFont redheadfont = wb.createFont();
			redheadfont.setFontName("宋体");
			redheadfont.setFontHeightInPoints((short) 11);// 字体大小
			redheadfont.setColor(HSSFColor.RED.index);
			
			HSSFFont contentfont = wb.createFont();
			contentfont.setUnderline((byte)1);
			contentfont.setFontName("宋体");
			contentfont.setFontHeightInPoints((short) 11);// 字体大小
			
			style.setFont(headfont);
			style1.setFont(contentfont);
			style4.setFont(headfont);
			rstyle.setFont(redheadfont);
			
			HSSFCellStyle style2 = wb.createCellStyle();
			style2.setBorderBottom((short) 1);
			style2.setBorderLeft((short) 1);
			style2.setBorderRight((short) 1);
			style2.setBorderTop((short) 1);
			style2.setFont(headfont);
			
			HSSFCellStyle style3 = wb.createCellStyle();
			style3.setBorderBottom((short) 1);
			style3.setBorderLeft((short) 1);
			style3.setBorderRight((short) 1);
			style3.setFont(headfont);
			
			HSSFCellStyle rstyle2 = wb.createCellStyle();
			rstyle2.setBorderBottom((short) 1);
			rstyle2.setBorderLeft((short) 1);
			rstyle2.setBorderRight((short) 1);
			rstyle2.setBorderTop((short) 1);
			rstyle2.setFont(redheadfont);
			
			HSSFCellStyle rstyle3 = wb.createCellStyle();
			rstyle3.setBorderBottom((short) 1);
			rstyle3.setBorderLeft((short) 1);
			rstyle3.setBorderRight((short) 1);
			rstyle3.setFont(redheadfont);
			
			HSSFSheet sheet = null;
			boolean isBefore=false;
			String xmlx="";
			for (String key : keyArr) {
				if(key.equals("TJGP")){
					xmlx="推荐挂牌";
					String departmentname = uc._deptModel.getDepartmentname();
					boolean isSuperMan = this.getIsSuperMan();
					List<HashMap> expXmjsList = new ArrayList<HashMap>();
					List<HashMap> sssybList = new ArrayList<HashMap>();
					//1：审批通过，0：未审批。
					if(status!=null&&status.equals("1")){
						expXmjsList = this.getExpXmjsSptgList(xmlx,prodate, departmentname, isSuperMan);
						sssybList = zqbProjectManageDAO.getSssybSptgList(xmlx,prodate, departmentname, isSuperMan);
					}else if(status!=null&&status.equals("0")){
						expXmjsList = this.getExpXmjsWspList(xmlx,prodate, departmentname, isSuperMan);
						sssybList = zqbProjectManageDAO.getSssybWspList(xmlx,prodate, departmentname, isSuperMan);
					}
					List<HashMap> clr = new ArrayList<HashMap>();
					List<HashMap> clrLc = new ArrayList<HashMap>();
					List<HashMap> cljg = new ArrayList<HashMap>();
					List<HashMap> cljgLc = new ArrayList<HashMap>();
					String projectno="";
					if(sssybList.isEmpty()){
						sheet = wb.createSheet("Sheet1");
						int m=0;//行
						int n=0;//列
						HSSFRow row1 = sheet.createRow((int) m++);
						HSSFCell titleCell = row1.createCell(1);
						titleCell.setCellValue("                (部门)");
						titleCell.setCellStyle(style1);
						titleCell = row1.createCell(2);
						titleCell.setCellValue("综合金融项目结算单（"+month+"月）");
						titleCell.setCellStyle(style);
						row1 = sheet.createRow((int) m++);
						titleCell = row1.createCell(0);
						titleCell.setCellValue("业务收入类型："+xmlx);
						titleCell.setCellStyle(style);
						List<Integer> rowList = new ArrayList<Integer>();
						row1 = sheet.createRow((int) m++);
						HSSFCell contentCell = row1.createCell(n++);
						contentCell.setCellValue("项目名称");
						contentCell.setCellStyle(style);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("");
						contentCell.setCellStyle(style);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("税前业务收入(元)");
						contentCell.setCellStyle(style);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("");
						contentCell.setCellStyle(style);
						n=0;
						row1 = sheet.createRow((int) m++);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("协议支出比例(%)");
						contentCell.setCellStyle(style);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("");
						contentCell.setCellStyle(style);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("税后业务收入(元)");
						contentCell.setCellStyle(style);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("");
						contentCell.setCellStyle(style);
						n=0;
						row1 = sheet.createRow((int) m++);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("承揽个人姓名");
						contentCell.setCellStyle(style);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("");
						contentCell.setCellStyle(style);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("承揽个人所在部门");
						contentCell.setCellStyle(style);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("");
						contentCell.setCellStyle(style);
						n=0;
						rowList.add(m);
						row1 = sheet.createRow((int) m++);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("分配部门");
						contentCell.setCellStyle(style2);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("");
						contentCell.setCellStyle(style2);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("分配比例(%)");
						contentCell.setCellStyle(style2);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("分配金额(元)");
						contentCell.setCellStyle(style2);
						n=0;
						rowList.add(m);
						row1 = sheet.createRow((int) m++);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("");
						contentCell.setCellStyle(style3);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("");
						contentCell.setCellStyle(style3);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("");
						contentCell.setCellStyle(style3);
						contentCell = row1.createCell(n++);
						contentCell.setCellValue("");
						contentCell.setCellStyle(style3);
						m++;
						m++;
						row1 = sheet.createRow((int) m++);
						
						HSSFCell cell = row1.createCell(0);
						/*cell.setCellValue("投行质量控制部负责人：");
						cell.setCellStyle(style);*/
						
						/*row1 = sheet.createRow((int) m++);
						
						cell = row1.createCell(1);
						cell.setCellValue("投行直属部执行总经理：");
						cell.setCellStyle(style);*/
						
						/*row1 = sheet.createRow((int) m++);
						
						cell = row1.createCell(1);
						cell.setCellValue("对应分配部门负责人：");
						cell.setCellStyle(style);*/
						
						/*m++;
						
						row1 = sheet.createRow((int) m++);*/
						
						/*cell = row1.createCell(1);*/
						cell.setCellValue("场外市场总部负责人：");
						cell.setCellStyle(style);
						
						row1 = sheet.createRow((int) m++);
						
						cell = row1.createCell(0);
						cell.setCellValue("分管领导：");
						cell.setCellStyle(style);
						
						/*row1 = sheet.createRow((int) m++);
						
						cell = row1.createCell(1);
						cell.setCellValue("公司领导：");
						cell.setCellStyle(style);*/
						
						for (Integer integer : rowList) {
							sheet.addMergedRegion(new Region(integer, (short) 0, integer, (short) 1));
							sheet.addMergedRegion(new Region(integer, (short) 2, integer, (short) 3));
						}
						sheet.setColumnWidth(0, 10 * 256 * 2);
						sheet.setColumnWidth(1, 20 * 256 * 2);
						sheet.setColumnWidth(2, 10 * 256 * 2);
						sheet.setColumnWidth(3, 10 * 256 * 2);
						sheet.setColumnWidth(4, 10 * 256 * 2);
						sheet.setColumnWidth(5, 10 * 256 * 2);
					}
					for (HashMap sybmap : sssybList) {
						String sssyb = sybmap.get("SSSYB").toString();
						// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
						sheet = wb.createSheet(sssyb);
						// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
						// 第四步，创建单元格，并设置值表头 设置表头居中
						int m=0;//行
						int n=0;//列
						HSSFRow row1 = sheet.createRow((int) m++);
						HSSFCell titleCell = row1.createCell(1);
						titleCell.setCellValue(sssyb+"(部门)");
						titleCell.setCellStyle(style1);
						titleCell = row1.createCell(2);
						titleCell.setCellValue("综合金融项目结算单（"+month+"月）");
						titleCell.setCellStyle(style4);
						List<Integer> rowList = new ArrayList<Integer>();
						for (HashMap hashMap : expXmjsList) {
							if(sssyb.equals(hashMap.get("SSSYB").toString())){
								String createdate = hashMap.get("CREATEDATE").toString();
								SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								Date d1;
								Date d2;
								try {
									d1 = sf.parse(createdate);//把时间格式化
									d2 = sf.parse("2016-08-20 00:00:00");//把时间格式化
									isBefore=d1.getTime() <= d2.getTime();
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									logger.error(e,e);
								}
								BigDecimal sqywsr = new BigDecimal(hashMap.get("RZJE").toString()).multiply(new BigDecimal("10000"));
								BigDecimal sl = new BigDecimal("1.06");
								BigDecimal shywsr = sqywsr.divide(sl,2,BigDecimal.ROUND_HALF_UP);
								String xyzcbl=hashMap.get("XYZCBL")==null?"":hashMap.get("XYZCBL").toString();
								BigDecimal fpje=new BigDecimal(0);
								if(!xyzcbl.equals("")){
									BigDecimal subAfterBl = new BigDecimal("100").subtract(new BigDecimal(xyzcbl)).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
									fpje = shywsr.multiply(subAfterBl).setScale(4,BigDecimal.ROUND_HALF_UP);
								}
								//审批通过与8-20日之前的项目导出项目结算单不标红
								if(hashMap.get("ZBSPZT").toString().equals("审批通过")||isBefore){
									String pro=hashMap.get("PROJECTNO").toString();
									Long dataid = Long.parseLong(hashMap.get("ID").toString());
									Long lcInstanceId = Long.parseLong(hashMap.get("LCBS").toString());
									row1 = sheet.createRow((int) m++);
									titleCell = row1.createCell(0);
									titleCell.setCellValue("业务收入类型："+xmlx);
									titleCell.setCellStyle(style);
									n=0;
									row1 = sheet.createRow((int) m++);
									HSSFCell contentCell = row1.createCell(n++);
									contentCell.setCellValue("项目名称");
									contentCell.setCellStyle(style);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(hashMap.get("PROJECTNAME").toString());
									contentCell.setCellStyle(style);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue("税前业务收入(元)");
									contentCell.setCellStyle(style);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(sqywsr.toString());
									contentCell.setCellStyle(style);
									n=0;
									row1 = sheet.createRow((int) m++);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(hashMap.get("XYZCBL")==null?"":"协议支出比例(%)");
									contentCell.setCellStyle(style);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(xyzcbl);
									contentCell.setCellStyle(style);
									
									contentCell = row1.createCell(n++);
									contentCell.setCellValue("税后业务收入(元)");
									contentCell.setCellStyle(style);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(shywsr.toString());
									contentCell.setCellStyle(style);
									
									if(!pro.equals(projectno)){
										Long dataInstanceId = DemAPI.getInstance().getInstaceId("BD_ZQB_PJ_BASE", dataid);
										clr = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLR");
										if(clr==null){
											clr=new ArrayList<HashMap>();
										}
										/*if(lcInstanceId!=0){
											clrLc = ProcessAPI.getInstance().getFromSubData(lcInstanceId, "SUBFORM_CLR");
										}else{
											clrLc=new ArrayList<HashMap>();
										}
										if(clrLc==null){
											clrLc=new ArrayList<HashMap>();
										}
										if(!clr.isEmpty()&&!clrLc.isEmpty()){
											clr.addAll(clrLc);
											clr=ListUtil.cleanList(clr, true, "ID");
										}else if(clr.isEmpty()&&!clrLc.isEmpty()){
											clr=clrLc;
										}*/
									}
									if(!clr.isEmpty()){
										//int count = 1;
										for (HashMap map : clr) {
											n=0;
											row1 = sheet.createRow((int) m++);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue("承揽个人姓名");
											contentCell.setCellStyle(style);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue(map.get("CLR").toString());
											contentCell.setCellStyle(style);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue("承揽个人所在部门");
											contentCell.setCellStyle(style);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue(map.get("SSBM")==null?"":map.get("SSBM").toString().trim().equals("")?"":map.get("SSBM").toString().trim());
											contentCell.setCellStyle(style);
											/*if(clr.size()>1){
												contentCell = row1.createCell(n++);
												contentCell.setCellValue("分配比例(%)");
												contentCell.setCellStyle(style);
												contentCell = row1.createCell(n++);
												contentCell.setCellValue(map.get("FPBL")==null?"":map.get("FPBL").toString().trim().equals("")?"":map.get("FPBL").toString().trim());
												contentCell.setCellStyle(style);
											}*/
											//count++;
										}
									}
									if(!pro.equals(projectno)){
										Long dataInstanceId = DemAPI.getInstance().getInstaceId("BD_ZQB_PJ_BASE", dataid);
										cljg = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLJG");
										if(cljg==null){
											cljg=new ArrayList<HashMap>();
										}
										/*if(lcInstanceId!=0){
											cljgLc = ProcessAPI.getInstance().getFromSubData(lcInstanceId, "SUBFORM_CLJG");
										}else{
											cljgLc=new ArrayList<HashMap>();
										}
										if(cljgLc==null){
											cljgLc=new ArrayList<HashMap>();
										}
										if(!cljg.isEmpty()&&!cljgLc.isEmpty()){
											cljg.addAll(cljgLc);
											cljg=ListUtil.cleanList(cljg, true, "ID");
										}else if(cljg.isEmpty()&&!cljgLc.isEmpty()){
											cljg=cljgLc;
										}*/
									}
									if(!cljg.isEmpty()){
										n=0;
										rowList.add(m);
										row1 = sheet.createRow((int) m++);
										contentCell = row1.createCell(n++);
										contentCell.setCellValue("分配部门");
										contentCell.setCellStyle(style2);
										contentCell = row1.createCell(n++);
										contentCell.setCellValue("");
										contentCell.setCellStyle(style2);
										contentCell = row1.createCell(n++);
										contentCell.setCellValue("分配比例(%)");
										contentCell.setCellStyle(style2);
										contentCell = row1.createCell(n++);
										contentCell.setCellValue("分配金额(元)");
										contentCell.setCellStyle(style2);
										for (HashMap map : cljg) {
											n=0;
											rowList.add(m);
											row1 = sheet.createRow((int) m++);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue(map.get("JGMC").toString());
											contentCell.setCellStyle(style3);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue("");
											contentCell.setCellStyle(style3);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue(map.get("FPBL").toString());
											contentCell.setCellStyle(style3);
											contentCell = row1.createCell(n++);
											if(!map.get("FPBL").toString().equals("")){
												contentCell.setCellValue(fpje.compareTo(new BigDecimal(0))==1?fpje.multiply(new BigDecimal(map.get("FPBL").toString()).divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP).toString()
														:shywsr.multiply(new BigDecimal(map.get("FPBL").toString()).divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
											}else{
												contentCell.setCellValue("");
											}
											contentCell.setCellStyle(style3);
										}
									}
									projectno=pro;
									m++;
								}else{
									String pro=hashMap.get("PROJECTNO").toString();
									Long dataid = Long.parseLong(hashMap.get("ID").toString());
									Long lcInstanceId = Long.parseLong(hashMap.get("LCBS").toString());
									row1 = sheet.createRow((int) m++);
									titleCell = row1.createCell(0);
									titleCell.setCellValue("业务收入类型："+xmlx);
									titleCell.setCellStyle(style);
									n=0;
									row1 = sheet.createRow((int) m++);
									HSSFCell contentCell = row1.createCell(n++);
									contentCell.setCellValue("项目名称");
									contentCell.setCellStyle(rstyle);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(hashMap.get("PROJECTNAME").toString());
									contentCell.setCellStyle(rstyle);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue("税前业务收入(元)");
									contentCell.setCellStyle(rstyle);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(sqywsr.toString());
									contentCell.setCellStyle(rstyle);
									
									n=0;
									row1 = sheet.createRow((int) m++);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(hashMap.get("XYZCBL")==null?"":"协议支出比例(%)");
									contentCell.setCellStyle(rstyle);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(xyzcbl);
									contentCell.setCellStyle(rstyle);
									
									contentCell = row1.createCell(n++);
									contentCell.setCellValue("税后业务收入(元)");
									contentCell.setCellStyle(rstyle);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(shywsr.toString());
									contentCell.setCellStyle(rstyle);
									
									if(!pro.equals(projectno)){
										Long dataInstanceId = DemAPI.getInstance().getInstaceId("BD_ZQB_PJ_BASE", dataid);
										clr = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLR");
										if(clr==null){
											clr=new ArrayList<HashMap>();
										}
										/*if(lcInstanceId!=0){
											clrLc = ProcessAPI.getInstance().getFromSubData(lcInstanceId, "SUBFORM_CLR");
										}else{
											clrLc=new ArrayList<HashMap>();
										}
										if(clrLc==null){
											clrLc=new ArrayList<HashMap>();
										}
										if(!clr.isEmpty()&&!clrLc.isEmpty()){
											clr.addAll(clrLc);
											clr=ListUtil.cleanList(clr, true, "ID");
										}else if(clr.isEmpty()&&!clrLc.isEmpty()){
											clr=clrLc;
										}*/
									}
									if(!clr.isEmpty()){
										//int count = 1;
										for (HashMap map : clr) {
											n=0;
											row1 = sheet.createRow((int) m++);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue("承揽个人姓名");
											contentCell.setCellStyle(rstyle);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue(map.get("CLR").toString());
											contentCell.setCellStyle(rstyle);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue("承揽个人所在部门");
											contentCell.setCellStyle(rstyle);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue(map.get("SSBM")==null?"":map.get("SSBM").toString().trim().equals("")?"":map.get("SSBM").toString().trim());
											contentCell.setCellStyle(rstyle);
											/*if(clr.size()>1){
												contentCell = row1.createCell(n++);
												contentCell.setCellValue("分配比例(%)");
												contentCell.setCellStyle(style);
												contentCell = row1.createCell(n++);
												contentCell.setCellValue(map.get("FPBL")==null?"":map.get("FPBL").toString().trim().equals("")?"":map.get("FPBL").toString().trim());
												contentCell.setCellStyle(style);
											}*/
											//count++;
										}
									}
									if(!pro.equals(projectno)){
										Long dataInstanceId = DemAPI.getInstance().getInstaceId("BD_ZQB_PJ_BASE", dataid);
										cljg = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLJG");
										if(cljg==null){
											cljg=new ArrayList<HashMap>();
										}
										/*if(lcInstanceId!=0){
											cljgLc = ProcessAPI.getInstance().getFromSubData(lcInstanceId, "SUBFORM_CLJG");
										}else{
											cljgLc=new ArrayList<HashMap>();
										}
										if(cljgLc==null){
											cljgLc=new ArrayList<HashMap>();
										}
										if(!cljg.isEmpty()&&!cljgLc.isEmpty()){
											cljg.addAll(cljgLc);
											cljg=ListUtil.cleanList(cljg, true, "ID");
										}else if(cljg.isEmpty()&&!cljgLc.isEmpty()){
											cljg=cljgLc;
										}*/
									}
									if(!cljg.isEmpty()){
										n=0;
										rowList.add(m);
										row1 = sheet.createRow((int) m++);
										contentCell = row1.createCell(n++);
										contentCell.setCellValue("分配部门");
										contentCell.setCellStyle(rstyle2);
										contentCell = row1.createCell(n++);
										contentCell.setCellValue("");
										contentCell.setCellStyle(rstyle2);
										contentCell = row1.createCell(n++);
										contentCell.setCellValue("分配比例(%)");
										contentCell.setCellStyle(rstyle2);
										contentCell = row1.createCell(n++);
										contentCell.setCellValue("分配金额(元)");
										contentCell.setCellStyle(rstyle2);
										for (HashMap map : cljg) {
											n=0;
											rowList.add(m);
											row1 = sheet.createRow((int) m++);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue(map.get("JGMC").toString());
											contentCell.setCellStyle(rstyle3);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue("");
											contentCell.setCellStyle(rstyle3);
											contentCell = row1.createCell(n++);
											contentCell.setCellValue(map.get("FPBL").toString());
											contentCell.setCellStyle(rstyle3);
											contentCell = row1.createCell(n++);
											if(!map.get("FPBL").toString().equals("")){
												contentCell.setCellValue(fpje.compareTo(new BigDecimal(0))==1?fpje.multiply(new BigDecimal(map.get("FPBL").toString()).divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP).toString()
														:shywsr.multiply(new BigDecimal(map.get("FPBL").toString()).divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
											}else{
												contentCell.setCellValue("");
											}
											contentCell.setCellStyle(rstyle3);
										}
									}
									projectno=pro;
									m++;
								}
							}
						}
						m++;
						row1 = sheet.createRow((int) m++);
						
						HSSFCell cell = row1.createCell(0);
						/*cell.setCellValue("投行质量控制部负责人：");
						cell.setCellStyle(style4);
						
						row1 = sheet.createRow((int) m++);
						
						cell = row1.createCell(1);
						cell.setCellValue("投行直属部执行总经理：");
						cell.setCellStyle(style4);
						
						row1 = sheet.createRow((int) m++);
						
						cell = row1.createCell(1);
						cell.setCellValue("对应分配部门负责人：");
						cell.setCellStyle(style4);
						
						m++;
						
						row1 = sheet.createRow((int) m++);
						
						cell = row1.createCell(1);*/
						cell.setCellValue("场外市场总部负责人：");
						cell.setCellStyle(style4);
						
						m+=2;
						row1 = sheet.createRow((int) m);
						cell = row1.createCell(0);
						cell.setCellValue("分管领导：");
						cell.setCellStyle(style4);
						
						/*row1 = sheet.createRow((int) m++);
						
						cell = row1.createCell(1);
						cell.setCellValue("公司领导：");
						cell.setCellStyle(style4);*/
						
						for (Integer integer : rowList) {
							sheet.addMergedRegion(new Region(integer, (short) 0, integer, (short) 1));
							//sheet.addMergedRegion(new Region(integer, (short) 2, integer, (short) 3));
						}
						sheet.setColumnWidth(0, 10 * 256 * 2);
						sheet.setColumnWidth(1, 20 * 256 * 2);
						sheet.setColumnWidth(2, 10 * 256 * 2);
						sheet.setColumnWidth(3, 10 * 256 * 2);
						sheet.setColumnWidth(4, 10 * 256 * 2);
						sheet.setColumnWidth(5, 10 * 256 * 2);
					}
				}else if(key.equals("DXZF")){
					setOtherProjectXMLX(wb,sheet,"定向增发",month,style,style1,style2,style3);
				}else if(key.equals("BGCZ")){
					setOtherProjectXMLX(wb,sheet,"并购重组",month,style,style1,style2,style3);
				}else if(key.equals("CXDD")){
					setOtherProjectXMLX(wb,sheet,"持续督导",month,style,style1,style2,style3);
				}else if(key.equals("QT")){
					setOtherProjectXMLX(wb,sheet,"其他",month,style,style1,style2,style3);
				}
			}
			OutputStream out1 = null;
			// 第六步，将文件存到指定位置
			try {
				StringBuffer disposition = new StringBuffer("attachment;filename=");
				if(status!=null&&status.equals("1")){
					disposition.append(UploadFileNameCodingUtil.StringEncoding("("+month+"月)已审批结算单.xls"));
				}else if(status!=null&&status.equals("0")){
					disposition.append(UploadFileNameCodingUtil.StringEncoding("未审批结算单.xls"));
				}
				/*response.setContentType("application/octet-stream;charset=UTF-8");*/
				response.setContentType("application/vnd.ms-excel;charset=UTF-8");
				response.setHeader("Content-disposition", disposition.toString());
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
	}
	
	public void setOtherProjectXMLX(HSSFWorkbook wb, HSSFSheet sheet, String key, int month, HSSFCellStyle style, HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3){
		sheet = wb.createSheet(key);
		int m=0;//行
		int n=0;//列
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell titleCell = row1.createCell(1);
		titleCell.setCellValue("                (部门)");
		titleCell.setCellStyle(style1);
		titleCell = row1.createCell(2);
		titleCell.setCellValue("综合金融项目结算单（"+month+"月）");
		titleCell.setCellStyle(style);
		row1 = sheet.createRow((int) m++);
		titleCell = row1.createCell(n++);
		titleCell.setCellValue("业务收入类型："+key);
		titleCell.setCellStyle(style);
		List<Integer> rowList = new ArrayList<Integer>();
		row1 = sheet.createRow((int) m++);
		HSSFCell contentCell = row1.createCell(n++);
		contentCell.setCellValue("项目名称");
		contentCell.setCellStyle(style);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("");
		contentCell.setCellStyle(style);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("含税业务收入（万元）");
		contentCell.setCellStyle(style);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("");
		contentCell.setCellStyle(style);
		n=0;
		row1 = sheet.createRow((int) m++);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("承揽人姓名");
		contentCell.setCellStyle(style);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("");
		contentCell.setCellStyle(style);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("承揽人所在部门");
		contentCell.setCellStyle(style);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("");
		contentCell.setCellStyle(style);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("分配比例(%)");
		contentCell.setCellStyle(style);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("");
		contentCell.setCellStyle(style);
		n=0;
		rowList.add(m);
		row1 = sheet.createRow((int) m++);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("分配部门");
		contentCell.setCellStyle(style2);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("");
		contentCell.setCellStyle(style2);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("分配比例(%)");
		contentCell.setCellStyle(style2);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("");
		contentCell.setCellStyle(style2);
		n=0;
		rowList.add(m);
		row1 = sheet.createRow((int) m++);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("");
		contentCell.setCellStyle(style3);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("");
		contentCell.setCellStyle(style3);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("");
		contentCell.setCellStyle(style3);
		contentCell = row1.createCell(n++);
		contentCell.setCellValue("");
		contentCell.setCellStyle(style3);
		m++;
		m++;
		row1 = sheet.createRow((int) m++);
		
		HSSFCell cell = row1.createCell(1);
		/*cell.setCellValue("投行质量控制部负责人：");
		cell.setCellStyle(style);
		
		row1 = sheet.createRow((int) m++);
		
		cell = row1.createCell(1);
		cell.setCellValue("投行直属部执行总经理：");
		cell.setCellStyle(style);
		
		row1 = sheet.createRow((int) m++);
		
		cell = row1.createCell(1);
		cell.setCellValue("对应分配部门负责人：");
		cell.setCellStyle(style);
		
		m++;
		
		row1 = sheet.createRow((int) m++);
		
		cell = row1.createCell(1);*/
		cell.setCellValue("场外市场总部负责人：");
		cell.setCellStyle(style);
		
		row1 = sheet.createRow((int) m++);
		
		cell = row1.createCell(1);
		cell.setCellValue("分管领导：");
		cell.setCellStyle(style);
		
		/*row1 = sheet.createRow((int) m++);
		
		cell = row1.createCell(1);
		cell.setCellValue("公司领导：");
		cell.setCellStyle(style);*/
		
		for (Integer integer : rowList) {
			sheet.addMergedRegion(new Region(integer, (short) 0, integer, (short) 1));
			sheet.addMergedRegion(new Region(integer, (short) 2, integer, (short) 3));
		}
		sheet.setColumnWidth(0, 10 * 256 * 2);
		sheet.setColumnWidth(1, 20 * 256 * 2);
		sheet.setColumnWidth(2, 10 * 256 * 2);
		sheet.setColumnWidth(3, 10 * 256 * 2);
		sheet.setColumnWidth(4, 10 * 256 * 2);
		sheet.setColumnWidth(5, 10 * 256 * 2);
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
	
	public HashMap<String,String> getParameterMap(HashMap<String,String> map,HashMap<String, List<String>> parameterMap){
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
	
	public HashMap<String,Object> getStrMap(HashMap<String,String> map,HashMap<String,String> parametermap){
		HashMap<String,Object> hashMap=new HashMap<String,Object>();
		List<String> list=new ArrayList<String>();
		int size = 0;
		for (String key : map.keySet()) {
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

	public List<HashMap> zqbAddxmcyDepartmentZtree() {
		return zqbProjectManageDAO.zqbAddxmcyDepartmentZtree();
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

	public String deletePjSettlementSheet(Long instanceid) {
		String info = "";
		boolean flag = false;
		HashMap hash = DemAPI.getInstance().getFromData(instanceid);
		Long dataId = Long.parseLong(hash.get("ID").toString());
		String customername = hash.get("CUSTOMERNAME").toString();
		BigDecimal je=new BigDecimal(hash.get("RZJE").toString());
		String xmlx = hash.get("XMLX").toString();
		if(xmlx.equals("推荐挂牌")){
			String customerno = hash.get("CUSTOMERNO").toString();
			HashMap valueMap=new HashMap();
			valueMap.put("CUSTOMERNO", customerno);
			List<HashMap> pjList = DemAPI.getInstance().getList(ProjectUUID, valueMap, null);
			if(!pjList.isEmpty()){
				String projectno = pjList.get(0).get("PROJECTNO").toString();
				BigDecimal rzje=new BigDecimal(hash.get("RZJE").toString());
				HashMap<String,String> conditionMap= new HashMap<String,String>();
				conditionMap.put("PROJECTNO", projectno);
				List<HashMap> list = DemAPI.getInstance().getList("b25ca8ed0a5a484296f2977b50db8396", conditionMap, "ID DESC");
				boolean updateFormData = false;
				if(list.isEmpty()){
					flag=DemAPI.getInstance().removeFormData(instanceid);
				}else{
					for (HashMap hashMap : list) {
						Object object = hashMap.get("SSJE");
						if(object!=null&&!object.toString().equals("")){
							BigDecimal ssje=new BigDecimal(hashMap.get("SSJE").toString());
							BigDecimal subtractRzje = ssje.subtract(rzje);
							int compareTo = subtractRzje.compareTo(new BigDecimal(0));
							if(compareTo!=-1){
								hashMap.put("SSJE",subtractRzje);
								flag=DemAPI.getInstance().removeFormData(instanceid);
								Long dataid = Long.parseLong(hashMap.get("ID").toString());
								Long instaceId = DemAPI.getInstance().getInstaceId("BD_PM_TASK", dataid);
								updateFormData = DemAPI.getInstance().updateFormData("b25ca8ed0a5a484296f2977b50db8396", instaceId, hashMap, dataid, false);
								break;
							}
						}
					}
					if(!updateFormData){
						flag=DemAPI.getInstance().removeFormData(instanceid);
					}
				}
			}else{
				flag=DemAPI.getInstance().removeFormData(instanceid);
			}
		}else{
			flag=DemAPI.getInstance().removeFormData(instanceid);
		}
		if (flag) {
			LogUtil.getInstance().addLog(dataId, "项目结算信息维护", "删除结算信息成功：项目名称："+customername+"，项目类型："+xmlx+"，入账金额："+je+"。");
			info = "success";
		} else {
			LogUtil.getInstance().addLog(dataId, "项目结算信息维护", "删除结算信息失败：项目名称："+customername+"，项目类型："+xmlx+"，入账金额："+je+"。");
			info = "删除失败！";
		}
		return info;
	}

	public void expywhz(HttpServletResponse response, String prodate) {
		if(prodate!=null&&!prodate.equals("")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgCompany companyModel = uc._companyModel;
			String companyname = companyModel.getCompanyname();
			companyname=companyname.substring(0, companyname.lastIndexOf("证券")+2);
			HashMap<String,BigDecimal> expYwhzMap=zqbProjectManageDAO.getExpYwhz(prodate);
			BigDecimal tjgp = expYwhzMap.get("推荐挂牌")==null?new BigDecimal(0):expYwhzMap.get("推荐挂牌");
			BigDecimal bgcz = expYwhzMap.get("并购重组")==null?new BigDecimal(0):expYwhzMap.get("并购重组");
			BigDecimal cxdd = expYwhzMap.get("持续督导")==null?new BigDecimal(0):expYwhzMap.get("持续督导");
			BigDecimal dxzf = expYwhzMap.get("定向增发")==null?new BigDecimal(0):expYwhzMap.get("定向增发");
			BigDecimal qt = expYwhzMap.get("其他")==null?new BigDecimal(0):expYwhzMap.get("其他");
			BigDecimal sum = tjgp.add(bgcz.add(cxdd.add(dxzf.add(qt))));
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			
			HSSFFont headfont = wb.createFont();
			headfont.setFontName("宋体");
			headfont.setFontHeightInPoints((short) 20);// 字体大小
			headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
			HSSFFont contentfont = wb.createFont();
			contentfont.setFontName("宋体");
			contentfont.setFontHeightInPoints((short) 11);// 字体大小
			contentfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
			
			HSSFCellStyle headstyle = wb.createCellStyle();
			headstyle.setFont(headfont);
			headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			headstyle.setBorderBottom((short) 1);
			headstyle.setBorderLeft((short) 1);
			headstyle.setBorderRight((short) 1);
			headstyle.setBorderTop((short) 1);
			
			HSSFCellStyle contentstyle = wb.createCellStyle();
			contentstyle.setFont(contentfont);
			contentstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			contentstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			contentstyle.setBorderBottom((short) 1);
			contentstyle.setBorderLeft((short) 1);
			contentstyle.setBorderRight((short) 1);
			contentstyle.setBorderTop((short) 1);
			HSSFSheet sheet = wb.createSheet(prodate+"月推荐业务收入情况汇总");
			int defaultheight=30*20;
			sheet.setDefaultRowHeight((short)defaultheight);
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			//第一行表头
			HSSFRow row = sheet.createRow((int) 0);
			HSSFCell cell = row.createCell((short) 0);
			cell.setCellValue(prodate+"月主办券商推荐业务收入情况");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 1);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 2);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 3);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 4);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 5);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 6);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 7);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 8);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			//第二行  列头
			row = sheet.createRow((int) 1);
			cell = row.createCell((short) 0);
			cell.setCellValue("序号");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 1);
			cell.setCellValue("主办券商");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 2);
			cell.setCellValue("推荐业务收入(万元)");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 3);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 4);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 5);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 6);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 7);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 8);
			cell.setCellValue("");
			cell.setCellStyle(headstyle);
			//第三行  列头
			row = sheet.createRow((int) 2);
			cell = row.createCell((short) 0);
			cell.setCellValue("");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 1);
			cell.setCellValue("");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 2);
			cell.setCellValue("推荐挂牌收入");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 3);
			cell.setCellValue("股票发行收入");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 4);
			cell.setCellValue("并购重组收入");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 5);
			cell.setCellValue("持续督导收入");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 6);
			cell.setCellValue("其他收入");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 7);
			cell.setCellValue("其他收入来源");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 8);
			cell.setCellValue("合计");
			cell.setCellStyle(contentstyle);
			//第四行    正文
			row = sheet.createRow((int) 3);
			cell = row.createCell((short) 0);
			cell.setCellValue("1");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 1);
			cell.setCellValue(companyname);
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 2);
			cell.setCellValue(tjgp.toString());
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 3);
			cell.setCellValue(dxzf.toString());
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 4);
			cell.setCellValue(bgcz.toString());
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 5);
			cell.setCellValue(cxdd.toString());
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 6);
			cell.setCellValue(qt.toString());
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 7);
			cell.setCellValue("0");
			cell.setCellStyle(contentstyle);
			cell = row.createCell((short) 8);
			cell.setCellValue(sum.toString());
			cell.setCellStyle(contentstyle);
			
			sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 8));
			sheet.addMergedRegion(new Region(1, (short) 0, 2, (short) 0));
			sheet.addMergedRegion(new Region(1, (short) 1, 2, (short) 1));
			sheet.addMergedRegion(new Region(1, (short) 2, 1, (short) 8));
			for (int i = 0; i < 9; i++) {
				sheet.setColumnWidth(i, 10 * 256 * 2);
			}
			OutputStream out1 = null;
			// 第六步，将文件存到指定位置
			try {
				String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("推荐业务收入情况汇总表.xls");
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
	}
	
	public void expydqy(HttpServletResponse response, String beginprodate, String endprodate) {
		if((beginprodate!=null&&!beginprodate.equals(""))&&(endprodate!=null&&!endprodate.equals(""))){
			List<HashMap> ydqyList = zqbProjectManageDAO.getYdqyList(beginprodate,endprodate);
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			
			HSSFFont headfont = wb.createFont();
			headfont.setFontName("宋体");
			headfont.setFontHeightInPoints((short) 11);// 字体大小
			headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
			
			HSSFFont celltitlefont = wb.createFont();
			celltitlefont.setFontName("宋体");
			celltitlefont.setFontHeightInPoints((short) 12);// 字体大小
			celltitlefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
			
			HSSFFont contentfont = wb.createFont();
			contentfont.setFontName("宋体");
			contentfont.setFontHeightInPoints((short) 12);// 字体大小
			
			HSSFCellStyle cellstyle = wb.createCellStyle();
			cellstyle.setFillForegroundColor((short) 13);
			cellstyle.setFillPattern((short) 1);
			cellstyle.setBorderBottom((short) 1);
			cellstyle.setBorderLeft((short) 1);
			cellstyle.setBorderRight((short) 1);
			cellstyle.setBorderTop((short) 1);
			
			HSSFCellStyle headstyle = wb.createCellStyle();
			headstyle.setFont(headfont);
			headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			headstyle.setBorderBottom((short) 1);
			headstyle.setBorderLeft((short) 1);
			headstyle.setBorderRight((short) 1);
			headstyle.setBorderTop((short) 1);
			
			HSSFCellStyle contentstyle = wb.createCellStyle();
			contentstyle.setFont(contentfont);
			contentstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			contentstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			contentstyle.setBorderBottom((short) 1);
			contentstyle.setBorderLeft((short) 1);
			contentstyle.setBorderRight((short) 1);
			contentstyle.setBorderTop((short) 1);
			
			HSSFCellStyle contentNumStyle = wb.createCellStyle();
			contentNumStyle.setFont(contentfont);
			contentNumStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 创建一个居中格式
			contentNumStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			contentNumStyle.setBorderBottom((short) 1);
			contentNumStyle.setBorderLeft((short) 1);
			contentNumStyle.setBorderRight((short) 1);
			contentNumStyle.setBorderTop((short) 1);
			
			HSSFCellStyle celltitlestyle = wb.createCellStyle();
			celltitlestyle.setFont(celltitlefont);
			celltitlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			celltitlestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			celltitlestyle.setBorderBottom((short) 1);
			celltitlestyle.setBorderLeft((short) 1);
			celltitlestyle.setBorderRight((short) 1);
			celltitlestyle.setBorderTop((short) 1);
			
			int rownum=0;
			HSSFSheet sheet = wb.createSheet("Sheet1");
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			//第一行表头
			sheet.setDefaultColumnStyle(5, cellstyle);
			HSSFRow row = sheet.createRow((int) rownum++);
			HSSFCell cell = row.createCell((short) 0);
			cell.setCellValue("");
			cell = row.createCell((short) 1);
			cell.setCellValue("");
			cell = row.createCell((short) 2);
			cell.setCellValue("");
			cell = row.createCell((short) 3);
			cell.setCellValue("");
			cell = row.createCell((short) 4);
			cell.setCellValue("");
			cell = row.createCell((short) 6);
			cell.setCellValue("承揽承做信息");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 7);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 8);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 9);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 10);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 11);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 12);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 13);
			cell.setCellValue("挂牌公司基本信息");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 14);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 15);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 16);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 17);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 18);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 19);
			cell.setCellValue("未来一年内的融资需求金额（万元）");
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 20);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 21);
			cell.setCellStyle(headstyle);
			cell = row.createCell((short) 22);
			cell.setCellStyle(headstyle);
			
			row = sheet.createRow((int) rownum++);
			cell = row.createCell((short) 0);
			cell.setCellValue("月份");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 1);
			cell.setCellValue("序号");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 2);
			cell.setCellValue("公司名称");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 3);
			cell.setCellValue("公司简称");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 4);
			cell.setCellValue("总收费（万元）");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 6);
			cell.setCellValue("省");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 7);
			cell.setCellValue("市");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 8);
			cell.setCellValue("区（仅限苏州）");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 9);
			cell.setCellValue("承揽部门");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 10);
			cell.setCellValue("承做部门");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 11);
			cell.setCellValue("联系人");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 12);
			cell.setCellValue("预计申报时间");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 13);
			cell.setCellValue("行业");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 14);
			cell.setCellValue("子行业");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 15);
			cell.setCellValue("股本（万元）");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 16);
			cell.setCellValue("去年净利润（万元）");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 17);
			cell.setCellValue("前年净利润（万元）");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 18);
			cell.setCellValue("近两年净利润（万元）");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 19);
			cell.setCellValue("银行贷款");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 20);
			cell.setCellValue("股权融资");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 21);
			cell.setCellValue("债券融资");
			cell.setCellStyle(celltitlestyle);
			cell = row.createCell((short) 22);
			cell.setCellValue("其他融资");
			cell.setCellStyle(celltitlestyle);
			List<Integer> numList=new ArrayList<Integer>();
			int countNum=1;
			int count=1;
			for (HashMap hashMap : ydqyList) {
				String month = hashMap.get("date")==null?"":UtilDate.monthFormat(UtilDate.StringToDate(hashMap.get("date").toString(),"yyyy-MM-dd"))+"月";
				String customername=hashMap.get("customername")==null?"":hashMap.get("customername").toString();
				String gsjc = hashMap.get("zqjc")==null?"":hashMap.get("zqjc").toString();
				String type = hashMap.get("type")==null?"":hashMap.get("type").toString();
				String zwmc = hashMap.get("zwmc")==null?"":hashMap.get("zwmc").toString();
				String zcqx = hashMap.get("zcqx")==null?"":hashMap.get("zcqx").toString();
				String sssyb = hashMap.get("sssyb")==null?"":hashMap.get("sssyb").toString();
				String fzjgmc = hashMap.get("fzjgmc")==null?"":hashMap.get("fzjgmc").toString();
				String khlxr = hashMap.get("khlxr")==null?"":hashMap.get("khlxr").toString();
				String enddate = hashMap.get("enddate")==null?"":hashMap.get("enddate").toString();
				String sshy = hashMap.get("sshy")==null?"":hashMap.get("sshy").toString();
				String gb = new BigDecimal(hashMap.get("gb")==null?"0.0":hashMap.get("gb").toString()).toString();
				String qnjlr = new BigDecimal(hashMap.get("qnjlr")==null?"0.0":hashMap.get("qnjlr").toString()).toString();
				String qiannjlr = new BigDecimal(hashMap.get("qiannjlr")==null?"0.0":hashMap.get("qiannjlr").toString()).toString();
				String jlnjlr = new BigDecimal(hashMap.get("jlnjlr")==null?"0.0":hashMap.get("jlnjlr").toString()).toString();
				String yxdk = new BigDecimal(hashMap.get("yxdk")==null?"0.0":hashMap.get("yxdk").toString()).toString();
				String gqrz = new BigDecimal(hashMap.get("gqrz")==null?"0.0":hashMap.get("gqrz").toString()).toString();
				String zqrz = new BigDecimal(hashMap.get("zqrz")==null?"0.0":hashMap.get("zqrz").toString()).toString();
				String qtrz = new BigDecimal(hashMap.get("qtrz")==null?"0.0":hashMap.get("qtrz").toString()).toString();
				String zsf = new BigDecimal(0.0).toString();
				row = sheet.createRow((int) rownum++);
				cell = row.createCell((short) 0);
				cell.setCellValue(month);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 1);
				cell.setCellValue(count);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 2);
				cell.setCellValue(customername);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 3);
				cell.setCellValue(gsjc);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 4);
				cell.setCellValue(zsf);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 6);
				cell.setCellValue(type);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 7);
				cell.setCellValue(zwmc);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 8);
				cell.setCellValue(zcqx);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 9);
				cell.setCellValue(fzjgmc);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 10);
				cell.setCellValue(sssyb);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 11);
				cell.setCellValue(khlxr);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 12);
				cell.setCellValue(enddate);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 13);
				cell.setCellValue(sshy);
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 14);
				cell.setCellValue("");
				cell.setCellStyle(contentstyle);
				cell = row.createCell((short) 15);
				cell.setCellValue(gb);
				cell.setCellStyle(contentNumStyle);
				cell = row.createCell((short) 16);
				cell.setCellValue(qnjlr);
				cell.setCellStyle(contentNumStyle);
				cell = row.createCell((short) 17);
				cell.setCellValue(qiannjlr);
				cell.setCellStyle(contentNumStyle);
				cell = row.createCell((short) 18);
				cell.setCellValue(jlnjlr);
				cell.setCellStyle(contentNumStyle);
				cell = row.createCell((short) 19);
				cell.setCellValue(yxdk);
				cell.setCellStyle(contentNumStyle);
				cell = row.createCell((short) 20);
				cell.setCellValue(gqrz);
				cell.setCellStyle(contentNumStyle);
				cell = row.createCell((short) 21);
				cell.setCellValue(zqrz);
				cell.setCellStyle(contentNumStyle);
				cell = row.createCell((short) 22);
				cell.setCellValue(qtrz);
				cell.setCellStyle(contentNumStyle);
				Integer parseInt = Integer.parseInt(hashMap.get("num").toString());
				if(countNum==parseInt){
					numList.add(countNum);
					countNum=1;
					count=1;
				}else{
					countNum++;
					count++;
				}
			}
			sheet.addMergedRegion(new Region(0, (short) 6, 0, (short) 12));
			sheet.addMergedRegion(new Region(0, (short) 13, 0, (short) 18));
			sheet.addMergedRegion(new Region(0, (short) 19, 0, (short) 22));
			int rowBeginNum=2;
			for (Integer num : numList) {
				sheet.addMergedRegion(new Region(rowBeginNum, (short) 0, rowBeginNum-1+num, (short) 0));
				rowBeginNum+=num;
			}
			sheet.setColumnWidth(0, 4 * 256 * 2);
			sheet.setColumnWidth(1, 3 * 256 * 2);
			sheet.setColumnWidth(2, 25 * 256 * 2);
			sheet.setColumnWidth(3, 6 * 256 * 2);
			sheet.setColumnWidth(4, 10 * 256 * 2);
			sheet.setColumnWidth(5, 256);
			sheet.setColumnWidth(6, 6 * 256 * 2);
			sheet.setColumnWidth(7, 6 * 256 * 2);
			sheet.setColumnWidth(8, 10 * 256 * 2);
			sheet.setColumnWidth(9, 18 * 256 * 2);
			sheet.setColumnWidth(10, 18 * 256 * 2);
			sheet.setColumnWidth(11, 8 * 256 * 2);
			sheet.setColumnWidth(12, 10 * 256 * 2);
			sheet.setColumnWidth(13, 20 * 256 * 2);
			sheet.setColumnWidth(14, 30 * 256 * 2);
			sheet.setColumnWidth(15, 10 * 256 * 2);
			sheet.setColumnWidth(16, 15 * 256 * 2);
			sheet.setColumnWidth(17, 15 * 256 * 2);
			sheet.setColumnWidth(18, 15 * 256 * 2);
			sheet.setColumnWidth(19, 10 * 256 * 2);
			sheet.setColumnWidth(20, 10 * 256 * 2);
			sheet.setColumnWidth(21, 10 * 256 * 2);
			sheet.setColumnWidth(22, 10 * 256 * 2);
			OutputStream out1 = null;
			// 第六步，将文件存到指定位置
			try {
				String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("月度签约项目统计表.xls");
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
	}

	public void expjsfc(HttpServletResponse response, String prodate,String xmlxArr) {
		String[] keyArr = xmlxArr.split(",");
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		int month = 0;
		Date date = UtilDate.StringToDate(prodate,"yyyy-MM");
		month = UtilDate.getMonth(date);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFCellStyle style = wb.createCellStyle();
		HSSFCellStyle rstyle = wb.createCellStyle();
		HSSFCellStyle style1 = wb.createCellStyle();
		HSSFCellStyle style4 = wb.createCellStyle();
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		
		HSSFFont redheadfont = wb.createFont();
		redheadfont.setFontName("宋体");
		redheadfont.setFontHeightInPoints((short) 11);// 字体大小
		redheadfont.setColor(HSSFColor.RED.index);
		
		HSSFFont contentfont = wb.createFont();
		contentfont.setUnderline((byte)1);
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 11);// 字体大小
		
		style.setFont(headfont);
		style1.setFont(contentfont);
		style4.setFont(headfont);
		rstyle.setFont(redheadfont);
		
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setBorderBottom((short) 1);
		style2.setBorderLeft((short) 1);
		style2.setBorderRight((short) 1);
		style2.setBorderTop((short) 1);
		style2.setFont(headfont);
		
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setBorderBottom((short) 1);
		style3.setBorderLeft((short) 1);
		style3.setBorderRight((short) 1);
		style3.setFont(headfont);
		
		HSSFCellStyle rstyle2 = wb.createCellStyle();
		rstyle2.setBorderBottom((short) 1);
		rstyle2.setBorderLeft((short) 1);
		rstyle2.setBorderRight((short) 1);
		rstyle2.setBorderTop((short) 1);
		rstyle2.setFont(redheadfont);
		
		HSSFCellStyle rstyle3 = wb.createCellStyle();
		rstyle3.setBorderBottom((short) 1);
		rstyle3.setBorderLeft((short) 1);
		rstyle3.setBorderRight((short) 1);
		rstyle3.setFont(redheadfont);
		
		HSSFSheet sheet = null;
		boolean isBefore=false;
		String xmlx="";
		for (String key : keyArr) {
			if(key.equals("DXZF")){
				xmlx="定增";
				String departmentname = uc._deptModel.getDepartmentname();
				boolean isSuperMan = this.getIsSuperMan();
				List<HashMap> expXmjsList = new ArrayList<HashMap>();
				List<HashMap> sssybList = new ArrayList<HashMap>();
				//
				expXmjsList = this.getExpJsfcList(xmlx,prodate, departmentname, isSuperMan);
				sssybList = zqbProjectManageDAO.getSssybJsfcList(xmlx,prodate, departmentname, isSuperMan);
				List<HashMap> clr = new ArrayList<HashMap>();
				List<HashMap> clrLc = new ArrayList<HashMap>();
				List<HashMap> cljg = new ArrayList<HashMap>();
				List<HashMap> cljgLc = new ArrayList<HashMap>();
				String projectno="";
				if(sssybList.isEmpty()){
					sheet = wb.createSheet("Sheet1");
					int m=0;//行
					int n=0;//列
					HSSFRow row1 = sheet.createRow((int) m++);
					HSSFCell titleCell = row1.createCell(1);
					titleCell.setCellValue("                (部门)");
					titleCell.setCellStyle(style1);
					titleCell = row1.createCell(2);
					titleCell.setCellValue("综合金融项目结算单（"+month+"月）");
					titleCell.setCellStyle(style);
					row1 = sheet.createRow((int) m++);
					titleCell = row1.createCell(0);
					titleCell.setCellValue("业务收入类型："+xmlx);
					titleCell.setCellStyle(style);
					List<Integer> rowList = new ArrayList<Integer>();
					row1 = sheet.createRow((int) m++);
					HSSFCell contentCell = row1.createCell(n++);
					contentCell.setCellValue("项目名称");
					contentCell.setCellStyle(style);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("");
					contentCell.setCellStyle(style);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("税前业务收入(元)");
					contentCell.setCellStyle(style);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("");
					contentCell.setCellStyle(style);
					n=0;
					row1 = sheet.createRow((int) m++);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue(""/*"协议支出比例(%)"*/);
					contentCell.setCellStyle(style);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("");
					contentCell.setCellStyle(style);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("税后业务收入(元)");
					contentCell.setCellStyle(style);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("");
					contentCell.setCellStyle(style);
					n=0;
					row1 = sheet.createRow((int) m++);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("承揽个人姓名");
					contentCell.setCellStyle(style);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("");
					contentCell.setCellStyle(style);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("承揽个人所在部门");
					contentCell.setCellStyle(style);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("");
					contentCell.setCellStyle(style);
					n=0;
					rowList.add(m);
					row1 = sheet.createRow((int) m++);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("分配部门");
					contentCell.setCellStyle(style2);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("");
					contentCell.setCellStyle(style2);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("分配比例(%)");
					contentCell.setCellStyle(style2);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("分配金额(元)");
					contentCell.setCellStyle(style2);
					n=0;
					rowList.add(m);
					row1 = sheet.createRow((int) m++);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("");
					contentCell.setCellStyle(style3);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("");
					contentCell.setCellStyle(style3);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("");
					contentCell.setCellStyle(style3);
					contentCell = row1.createCell(n++);
					contentCell.setCellValue("");
					contentCell.setCellStyle(style3);
					m++;
					m++;
					row1 = sheet.createRow((int) m++);
					
					HSSFCell cell = row1.createCell(0);
					/*cell.setCellValue("投行质量控制部负责人：");
					cell.setCellStyle(style);*/
					
					/*row1 = sheet.createRow((int) m++);
					
					cell = row1.createCell(1);
					cell.setCellValue("投行直属部执行总经理：");
					cell.setCellStyle(style);*/
					
					/*row1 = sheet.createRow((int) m++);
					
					cell = row1.createCell(1);
					cell.setCellValue("对应分配部门负责人：");
					cell.setCellStyle(style);*/
					
					/*m++;
					
					row1 = sheet.createRow((int) m++);*/
					
					/*cell = row1.createCell(1);*/
					cell.setCellValue("场外市场总部负责人：");
					cell.setCellStyle(style);
					
					row1 = sheet.createRow((int) m++);
					
					cell = row1.createCell(0);
					cell.setCellValue("分管领导：");
					cell.setCellStyle(style);
					
					/*row1 = sheet.createRow((int) m++);
					
					cell = row1.createCell(1);
					cell.setCellValue("公司领导：");
					cell.setCellStyle(style);*/
					
					for (Integer integer : rowList) {
						sheet.addMergedRegion(new Region(integer, (short) 0, integer, (short) 1));
						sheet.addMergedRegion(new Region(integer, (short) 2, integer, (short) 3));
					}
					sheet.setColumnWidth(0, 10 * 256 * 2);
					sheet.setColumnWidth(1, 20 * 256 * 2);
					sheet.setColumnWidth(2, 10 * 256 * 2);
					sheet.setColumnWidth(3, 10 * 256 * 2);
					sheet.setColumnWidth(4, 10 * 256 * 2);
					sheet.setColumnWidth(5, 10 * 256 * 2);
				}
				//处理审批通过项目
				for (HashMap sybmap : sssybList) {
					String sssyb = sybmap.get("SSSYB").toString();
					// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
					sheet = wb.createSheet(sssyb);
					// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
					// 第四步，创建单元格，并设置值表头 设置表头居中
					int m=0;//行
					int n=0;//列
					HSSFRow row1 = sheet.createRow((int) m++);
					HSSFCell titleCell = row1.createCell(1);
					titleCell.setCellValue(sssyb+"(部门)");
					titleCell.setCellStyle(style1);
					titleCell = row1.createCell(2);
					titleCell.setCellValue("综合金融项目结算单（"+month+"月）");
					titleCell.setCellStyle(style4);
					List<Integer> rowList = new ArrayList<Integer>();
					
					String projectNo = "DZ0000-00-0000";
					BigDecimal bgAdd = new BigDecimal("0.00");
					boolean f = false;
					int full=0;
					for (HashMap hashMap : expXmjsList) {
						if(sssyb.equals(hashMap.get("SSSYB").toString())){
							String projectNoThis = hashMap.get("PROJECTNO").toString();
							if(projectNoThis.equals(projectNo)){
								f = true;
							}else{
								f = false;
							}
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							//根据入账金额计算税后金额
							//税前业务收入
							BigDecimal sqywsr = new BigDecimal(hashMap.get("RZJE") == null ? "" : hashMap.get("RZJE").toString()).multiply(new BigDecimal("10000"));
							BigDecimal sl = new BigDecimal("1.06");
							//税后业务收入
							BigDecimal shywsr = sqywsr.divide(sl,2,BigDecimal.ROUND_HALF_UP);
							
							if(f){
								bgAdd = bgAdd.add(shywsr);
							}else{
								bgAdd = new BigDecimal(shywsr.toString());
							}
							
							//场外企业融资部金额
							BigDecimal dzje = new BigDecimal(hashMap.get("DZJE")==null?hashMap.get("SJFXZE").toString():hashMap.get("DZJE").toString()).multiply(new BigDecimal("10000"));
							//合同金额
							BigDecimal sjfxgpjg = new BigDecimal(hashMap.get("SJFXGPJG") == null ? "0.00" : hashMap.get("SJFXGPJG").toString()).multiply(new BigDecimal("10000"));
							
							
							//审批通过的项目导出项目结算单不标红
							String pro=hashMap.get("PROJECTNO").toString();
							Long dataid = Long.valueOf(Long.parseLong(hashMap.get("ID").toString()));
							Long lcInstanceId = Long.parseLong(hashMap.get("LCBS").toString());
							row1 = sheet.createRow((int) m++);
							titleCell = row1.createCell(0);
							titleCell.setCellValue("业务收入类型："+xmlx);
							titleCell.setCellStyle(style);
							n=0;
							row1 = sheet.createRow((int) m++);
							HSSFCell contentCell = row1.createCell(n++);
							contentCell.setCellValue("项目名称");
							contentCell.setCellStyle(style);
							contentCell = row1.createCell(n++);
							contentCell.setCellValue(hashMap.get("PROJECTNAME") == null ? "" : hashMap.get("PROJECTNAME").toString());
							contentCell.setCellStyle(style);
							contentCell = row1.createCell(n++);
							contentCell.setCellValue("税前业务收入(元)");
							contentCell.setCellStyle(style);
							contentCell = row1.createCell(n++);
							contentCell.setCellValue(sqywsr.toString());
							contentCell.setCellStyle(style);
							n=0;
							row1 = sheet.createRow((int) m++);
							contentCell = row1.createCell(n++);
							contentCell.setCellValue("");
							contentCell.setCellStyle(style);
							contentCell = row1.createCell(n++);
							contentCell.setCellValue("");
							contentCell.setCellStyle(style);
							
							contentCell = row1.createCell(n++);
							contentCell.setCellValue("税后业务收入(元)");
							contentCell.setCellStyle(style);
							contentCell = row1.createCell(n++);
							contentCell.setCellValue(shywsr.toString());
							contentCell.setCellStyle(style);
							
							Long dataInstanceId = DemAPI.getInstance().getInstaceId("BD_ZQB_GPFXXMB", dataid);
							clr = DemAPI.getInstance().getFromSubData(dataInstanceId, "SUBFORM_CLRGPFX");
							if(clr==null){
								clr=new ArrayList<HashMap>();
							}
							if(!clr.isEmpty()){
								for (HashMap map : clr) {
									n=0;
									row1 = sheet.createRow((int) m++);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue("承揽个人姓名");
									contentCell.setCellStyle(style);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(map.get("CLR").toString());
									contentCell.setCellStyle(style);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue("承揽个人所在部门");
									contentCell.setCellStyle(style);
									contentCell = row1.createCell(n++);
									contentCell.setCellValue(map.get("SSBM")==null?"":map.get("SSBM").toString().trim().equals("")?"":map.get("SSBM").toString().trim());
									contentCell.setCellStyle(style);
								}
							}
							
							
							n=0;
							rowList.add(m);
							row1 = sheet.createRow((int) m++);
							contentCell = row1.createCell(n++);
							contentCell.setCellValue("分配部门");
							contentCell.setCellStyle(style2);
							contentCell = row1.createCell(n++);
							contentCell.setCellValue("");
							contentCell.setCellStyle(style2);
							contentCell = row1.createCell(n++);
							contentCell.setCellValue("分配金额(元)");
							contentCell.setCellStyle(style2);
							int index = 1;
							
							
							n=0;
							rowList.add(m);
							row1 = sheet.createRow((int) m++);
							contentCell = row1.createCell(n++);
							contentCell.setCellValue("场外企业融资部");
							contentCell.setCellStyle(style3);
							contentCell = row1.createCell(n++);
							contentCell.setCellValue("");
							contentCell.setCellStyle(style3);
							contentCell = row1.createCell(n++);
							
							BigDecimal b_ = new BigDecimal("0.00");
							if(bgAdd.compareTo(dzje)>0){//企业融资部分配满后,得出多余的金额用于分配承做部门
								b_ = bgAdd.subtract(dzje);
							}
							contentCell.setCellValue(bgAdd.compareTo(dzje)<=0?shywsr.toString():shywsr.compareTo(b_)>0?shywsr.subtract(b_).toString():full==0?shywsr.toString():"0.00");
							full = bgAdd.compareTo(dzje)>0?1:0;
							contentCell.setCellStyle(style3);
							index++;
							
							
							String zcbm = hashMap.get("SSSYB") == null ? "" : hashMap.get("SSSYB").toString();
							if(zcbm!=null&&!zcbm.equals("")){
								n=0;
								rowList.add(m);
								row1 = sheet.createRow((int) m++);
								contentCell = row1.createCell(n++);
								contentCell.setCellValue(zcbm);
								contentCell.setCellStyle(style3);
								contentCell = row1.createCell(n++);
								contentCell.setCellValue("");
								contentCell.setCellStyle(style3);
								contentCell = row1.createCell(n++);
								contentCell.setCellValue(bgAdd.compareTo(dzje)<=0?b_.toString():shywsr.compareTo(b_)>0?b_.toString():shywsr.toString());
								contentCell.setCellStyle(style3);
								index++;
							}
							m++;
							
							projectNo = projectNoThis;
						}
					}
					m++;
					row1 = sheet.createRow((int) m++);
					
					HSSFCell cell = row1.createCell(0);
					cell.setCellValue("场外市场总部负责人：");
					cell.setCellStyle(style4);
					
					m+=2;
					row1 = sheet.createRow((int) m);
					cell = row1.createCell(0);
					cell.setCellValue("分管领导：");
					cell.setCellStyle(style4);
					
					/*row1 = sheet.createRow((int) m++);
					
					cell = row1.createCell(1);
					cell.setCellValue("公司领导：");
					cell.setCellStyle(style4);*/
					
					for (Integer integer : rowList) {
						sheet.addMergedRegion(new Region(integer, (short) 0, integer, (short) 1));
						//sheet.addMergedRegion(new Region(integer, (short) 2, integer, (short) 3));
					}
					sheet.setColumnWidth(0, 10 * 256 * 2);
					sheet.setColumnWidth(1, 20 * 256 * 2);
					sheet.setColumnWidth(2, 10 * 256 * 2);
					sheet.setColumnWidth(3, 10 * 256 * 2);
					sheet.setColumnWidth(4, 10 * 256 * 2);
					sheet.setColumnWidth(5, 10 * 256 * 2);
				}
				
				
				//处理为审批通过项目或无项目-------------
				HSSFSheet sheet2 = wb.createSheet("未审批");
				HSSFFont hfont = wb.createFont();
				hfont.setFontName("宋体");
				hfont.setFontHeightInPoints((short) 11);// 字体大小
				// 第四步，创建单元格，并设置值表头 设置表头居中
				HSSFCellStyle stylef = wb.createCellStyle();
				style4.setWrapText(true);
				style4.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
				style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
				style4.setFont(headfont);
				// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
				List<HashMap> list = new ArrayList<HashMap>();
				List<HashMap> list1 = new ArrayList<HashMap>();
				int m=0;
				int z=0;
				HSSFRow row1 = sheet2.createRow((int) m++);
				HSSFCell 
				cell1 = row1.createCell((short) z++);cell1.setCellValue("项目名称");cell1.setCellStyle(stylef);
				cell1 = row1.createCell((short) z++);cell1.setCellValue("税前收入(元)");cell1.setCellStyle(stylef);
				cell1 = row1.createCell((short) z++);cell1.setCellValue("承做部门");cell1.setCellStyle(stylef);
				cell1 = row1.createCell((short) z++);cell1.setCellValue("项目状态");cell1.setCellStyle(stylef);
				list = this.getExpJsfcListNotSptg(xmlx,prodate);
				for (HashMap map : list) {
					z=0;
					row1 = sheet2.createRow((int) m++);
					HSSFCell 
					cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("PROJECTNAME") == null ? "" : map.get("PROJECTNAME").toString());cell2.setCellStyle(stylef);
					cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("RZJE") == null ? "" : new BigDecimal(map.get("RZJE").toString()).multiply(new BigDecimal("10000")).toString());cell2.setCellStyle(stylef);
					cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("SSSYB") == null ? "" : map.get("SSSYB").toString());cell2.setCellStyle(stylef);
					cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZBSPZT") == null ? "无项目" : map.get("ZBSPZT").toString());cell2.setCellStyle(stylef);
				}
				for (int i = 0; i < 4; i++) {
					sheet2.setColumnWidth(i, 8000);
				}
				//处理为审批通过项目或无项目-------------------
				
			}
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			StringBuffer disposition = new StringBuffer("attachment;filename=");
			disposition.append(UploadFileNameCodingUtil.StringEncoding("("+month+"月)定增项目的结算分成单.xls"));
			/*response.setContentType("application/octet-stream;charset=UTF-8");*/
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-disposition", disposition.toString());
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
	public List<HashMap> getExpJsfcList(String xmlx,String prodate,String departmentname,Boolean isSuperMan){
		List<HashMap> list = new ArrayList<HashMap>();
		return zqbProjectManageDAO.getExpJsfcList(xmlx,prodate,departmentname,isSuperMan);
	}
	public List<HashMap> getExpJsfcListNotSptg(String xmlx,String prodate){
		List<HashMap> list = new ArrayList<HashMap>();
		return zqbProjectManageDAO.getExpJsfcListNotSptg(xmlx,prodate);
	}
	public List<HashMap> getExpXmjsListNoPro(String xmlx,String prodate){
		List<HashMap> list = new ArrayList<HashMap>();
		return zqbProjectManageDAO.getExpXmjsListNoPro(xmlx,prodate);
	}
	
	public String commitProject(Long instanceid, String projectNo) {
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String userid = userModel.getUserid();
		String username = userModel.getUsername();
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		HashMap lcFromData = (HashMap) fromData.clone();
		Long lcbs=fromData.get("LCBS")==null?0:Long.parseLong(fromData.get("LCBS").toString().equals("")?"0":fromData.get("LCBS").toString());
		String taskid = "";
		String executionId = "";
		String actDefId ="";
		Long instanceId = 0L;
		if(lcbs==0){
			actDefId= ProcessAPI.getInstance().getProcessActDefId("XMSPLC");
			lcFromData.put("CREATEUSER", username);
			lcFromData.put("CREATEUSERID", userid);
			lcFromData.put("CREATEDATE", UtilDate.getNowdate());
			lcFromData.put("A02", 1);
			Long formId = ProcessAPI.getInstance().getProcessDefaultFormId(actDefId);
			instanceId = ProcessAPI.getInstance().newInstance(actDefId, formId, userid);
			ProcessAPI.getInstance().saveFormData(actDefId, instanceId, lcFromData, false);// 保存流程
			String jd1 = SystemConfig._xmsplcConf.getJd1();
			List<Task> tasklist = ProcessAPI.getInstance().getUserProcessTaskList(actDefId, jd1,userid);
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			for (Task task : tasklist) {
				if (Long.parseLong(task.getProcessInstanceId()) == instanceId) {
					taskid = task.getId();
					executionId = task.getExecutionId();
				}
			}
			fromData.put("LCBH", actDefId);
			fromData.put("LCBS", instanceId);
			fromData.put("STEPID", jd1);
			fromData.put("TASKID", taskid);
			Long dataid = Long.parseLong(fromData.get("ID").toString());
			DemAPI.getInstance().updateFormData(ProjectUUID, instanceid, fromData, dataid, false);
			List<HashMap> fromSubXmcyData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_XMCYLB");
			List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_CLR");
			List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_CLJG");
			List<HashMap> fromSubZjjgData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_XMZJJG");
			
			List<HashMap> lcFromSubXmcyData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMCYLB");
			setFromSubData(lcFromSubXmcyData,actDefId,instanceId,"SUBFORM_XMCYLB",fromSubXmcyData,false);
			
			List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
			setFromSubData(lcFromSubClrData,actDefId,instanceId,"SUBFORM_CLR",fromSubClrData,false);
			
			List<HashMap> lcFromSubCljgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
			setFromSubData(lcFromSubCljgData,actDefId,instanceId,"SUBFORM_CLJG",fromSubCljgData,false);
			
			List<HashMap> lcFromSubZjjgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMZJJG");
			setFromSubData(lcFromSubZjjgData,actDefId,instanceId,"SUBFORM_XMZJJG",fromSubZjjgData,false);
		}else{
			executionId=lcbs.toString();
			instanceId=lcbs;
			String jd1=fromData.get("STEPID").toString();
			actDefId= ProcessAPI.getInstance().getProcessActDefId("XMSPLC");
			List<Task> tasklist = ProcessAPI.getInstance().getUserProcessTaskList(actDefId, jd1,userid);
			for (Task task : tasklist) {
				if (Long.parseLong(task.getProcessInstanceId()) == instanceId) {
					taskid = task.getId();
					executionId = task.getExecutionId();
				}

			}
		}
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+instanceId+"}");
		return jsonHtml.toString();
	}
	
	/**
	 * 
	 * @param getFromSubData 流程表中子表的信息
	 * @param actDefId 流程Id
	 * @param instanceId
	 * @param fromSubKey 子表名
	 * @param savefromSubData 需保存到流程数据中子表的信息
	 * @param isLog 是否记录日志
	 */
	private void setFromSubData(List<HashMap> getFromSubData, String actDefId, Long instanceId,
			String fromSubKey, List<HashMap> savefromSubData, boolean isLog) {
		if(getFromSubData!=null){
			if(getFromSubData.size()>0){
				for (HashMap hashMap : getFromSubData) {
					ProcessAPI.getInstance().removeSubFormData(actDefId,instanceId,Long.parseLong(hashMap.get("ID").toString()),fromSubKey);
				}
				if(savefromSubData!=null){
					ProcessAPI.getInstance().saveFormDatas(actDefId, instanceId, fromSubKey, savefromSubData, isLog);
				}
			}else{
				if(savefromSubData!=null){
					ProcessAPI.getInstance().saveFormDatas(actDefId, instanceId, fromSubKey, savefromSubData, isLog);
				}
			}
		}else{
			if(savefromSubData!=null){
				ProcessAPI.getInstance().saveFormDatas(actDefId, instanceId, fromSubKey, savefromSubData, isLog);
			}
		}
	}
	
	public List<HashMap<String,Long>> getProjectData(){
		StringBuffer sql = new StringBuffer("select pj.lcbs,bind.instanceid from bd_zqb_pj_base pj inner join bd_zqb_xmlcxxb lc on pj.projectno=lc.projectno inner join (select * from sys_engine_form_bind where formid=91 and metadataid=101) bind on pj.id=bind.dataid where pj.spzt is null or pj.spzt='审批通过'");
		List<HashMap<String,Long>> dataList=zqbProjectManageDAO.getProjectDataList(sql.toString());
		return dataList;
	}

	public boolean removeDaily(Long instanceid) {
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		boolean removeFormData = DemAPI.getInstance().removeFormData(instanceid);
		if(removeFormData){
			String value=fromData.get("PROJECTNO")==null?"":fromData.get("PROJECTNO").toString()+fromData.get("PROGRESS")==null?"":fromData.get("PROGRESS").toString();
			Long dataId=Long.parseLong(fromData.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "项目日报", "成功删除项目日报："+value);
		}else{
			String value=fromData.get("PROJECTNO")==null?"":fromData.get("PROJECTNO").toString()+fromData.get("PROGRESS")==null?"":fromData.get("PROGRESS").toString();
			Long dataId=Long.parseLong(fromData.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "项目日报", "删除项目日报："+value+"失败");
		}
		return removeFormData;
	}
	
	public List<HashMap> getZkList(String pjname, String jdmc, int pageNumber, int pageSize) {
		List parameter=new ArrayList();//存放参数
		StringBuffer sql=new StringBuffer("SELECT * FROM (SELECT (SELECT ZKR FROM BD_ZQB_ZKNHSPR) SZZKSPR,ZID,ZINSTANCEID,ZPJNO,ZKDATE,ZKR,ZKCBSJ,PJNAME,JDMC,OWNER,LCBH,LCBS,TASKID,LCCREATEUSER,PJPRONO,ROWNUM RM FROM (SELECT NVL(ZID,0) ZID,ZINSTANCEID,ZPJNO,ZKDATE,ZKR,ZKCBSJ,PJNAME,JDMC,OWNER,LCBH,LCBS,TASKID,PJLC.CREATEUSER LCCREATEUSER,PJ.PROJECTNO PJPRONO FROM (SELECT PJ.PROJECTNO,PJ.PROJECTNAME PJNAME,JD.JDMC,PJ.LCBH,TO_NUMBER(NVL(PJ.LCBS,0)) LCBS,PJ.TASKID,PJ.OWNER FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT TASK.GROUPID,TASK.PROJECTNO,KMINFO.JDMC FROM BD_ZQB_KM_INFO KMINFO INNER JOIN (SELECT MAX(GROUPID) GROUPID,PROJECTNO FROM BD_PM_TASK GROUP BY PROJECTNO) TASK ON KMINFO.ID=TASK.GROUPID) JD ON PJ.PROJECTNO=JD.PROJECTNO) PJ LEFT JOIN (SELECT DISTINCT CREATEUSER,PROJECTNO FROM BD_ZQB_XMLCXXB) PJLC ON PJLC.PROJECTNO=PJ.PROJECTNO LEFT JOIN (SELECT ZK.ID ZID,BINDDATA.INSTANCEID ZINSTANCEID,ZK.PROJECTNO ZPJNO,TO_CHAR(ZK.ZKDATE,'YYYY-MM-DD') ZKDATE,ZK.ZKR ZKR,TO_CHAR(ZK.CBSJ,'YYYY-MM-DD HH24:MI') ZKCBSJ FROM BD_ZQB_ZKSPRQ ZK INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_ZKSPRQ') META ON IFORM.METADATAID=META.ID) BINDDATA ON ZK.ID=BINDDATA.DATAID) ZK ON ZK.ZPJNO=PJ.PROJECTNO ORDER BY ZID DESC,LCBS DESC) WHERE 1=1 ");
		if(pjname!=null&&!pjname.equals("")){
			sql.append(" AND UPPER(PJNAME) LIKE ?");
			parameter.add(pjname);
		}
		if(jdmc!=null&&!jdmc.equals("")){
			sql.append(" AND UPPER(JDMC) LIKE ?");
			parameter.add(jdmc);
		}
		sql.append(") WHERE RM>? AND RM<=?");
		List<HashMap> dataList=zqbProjectManageDAO.getZkList(sql.toString(),parameter,pageNumber,pageSize);
		return dataList;
	}

	public int getZkListSize(String pjname, String jdmc) {
		List parameter=new ArrayList();//存放参数
		StringBuffer sql=new StringBuffer("SELECT COUNT(*) CNUM FROM (SELECT PJ.PROJECTNO,PJ.PROJECTNAME PJNAME,JD.JDMC,PJ.LCBH,TO_NUMBER(NVL(PJ.LCBS,0)) LCBS,PJ.TASKID,PJ.OWNER FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT TASK.GROUPID,TASK.PROJECTNO,KMINFO.JDMC FROM BD_ZQB_KM_INFO KMINFO INNER JOIN (SELECT MAX(GROUPID) GROUPID,PROJECTNO FROM BD_PM_TASK GROUP BY PROJECTNO) TASK ON KMINFO.ID=TASK.GROUPID) JD ON PJ.PROJECTNO=JD.PROJECTNO) PJ LEFT JOIN (SELECT DISTINCT CREATEUSER,PROJECTNO FROM BD_ZQB_XMLCXXB) PJLC ON PJLC.PROJECTNO=PJ.PROJECTNO LEFT JOIN (SELECT ZK.ID ZID,BINDDATA.INSTANCEID ZINSTANCEID,ZK.PROJECTNO ZPJNO,TO_CHAR(ZK.ZKDATE,'YYYY-MM-DD') ZKDATE,ZK.ZKR ZKR,TO_CHAR(ZK.CBSJ,'YYYY-MM-DD HH24:MI') ZKCBSJ FROM BD_ZQB_ZKSPRQ ZK INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_ZKSPRQ') META ON IFORM.METADATAID=META.ID) BINDDATA ON ZK.ID=BINDDATA.DATAID) ZK ON ZK.ZPJNO=PJ.PROJECTNO WHERE 1=1 ");
		if(pjname!=null&&!pjname.equals("")){
			sql.append(" AND UPPER(PJNAME) LIKE ?");
			parameter.add(pjname);
		}
		if(jdmc!=null&&!jdmc.equals("")){
			sql.append(" AND UPPER(JDMC) LIKE ?");
			parameter.add(jdmc);
		}
		int count=zqbProjectManageDAO.getZkListSize(sql.toString(),parameter);
		return count;
	}
	
	public List<HashMap> getNhList(String pjname, String jdmc, int pageNumber, int pageSize) {
		List parameter=new ArrayList();//存放参数
		StringBuffer sql=new StringBuffer("SELECT * FROM (SELECT (SELECT NHSPR FROM BD_ZQB_ZKNHSPR) SZNHSPR,NID,NINSTANCEID,NPJNO,NHDATE,NHSPR,NHCBSJ,PJNAME,JDMC,OWNER,LCBH,LCBS,TASKID,LCCREATEUSER,PJPRONO,ROWNUM RM FROM (SELECT NVL(NID,0) NID,NINSTANCEID,NPJNO,NHDATE,NHSPR,NHCBSJ,PJNAME,JDMC,OWNER,LCBH,LCBS,TASKID,PJLC.CREATEUSER LCCREATEUSER,PJ.PROJECTNO PJPRONO FROM (SELECT PJ.PROJECTNO,PJ.PROJECTNAME PJNAME,JD.JDMC,PJ.LCBH,TO_NUMBER(NVL(PJ.LCBS,0)) LCBS,PJ.TASKID,PJ.OWNER FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT TASK.GROUPID,TASK.PROJECTNO,KMINFO.JDMC FROM BD_ZQB_KM_INFO KMINFO INNER JOIN (SELECT MAX(GROUPID) GROUPID,PROJECTNO FROM BD_PM_TASK GROUP BY PROJECTNO) TASK ON KMINFO.ID=TASK.GROUPID) JD ON PJ.PROJECTNO=JD.PROJECTNO) PJ LEFT JOIN (SELECT DISTINCT CREATEUSER,PROJECTNO FROM BD_ZQB_XMLCXXB) PJLC ON PJLC.PROJECTNO=PJ.PROJECTNO LEFT JOIN (SELECT NH.ID NID,BINDDATA.INSTANCEID NINSTANCEID,NH.PROJECTNO NPJNO,TO_CHAR(NH.NHDATE,'YYYY-MM-DD') NHDATE,NH.NHSPR NHSPR,TO_CHAR(NH.CBSJ,'YYYY-MM-DD HH24:MI') NHCBSJ FROM BD_ZQB_NHSPRQ NH INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_NHSPRQ') META ON IFORM.METADATAID=META.ID) BINDDATA ON NH.ID=BINDDATA.DATAID) NH ON NH.NPJNO=PJ.PROJECTNO  ORDER BY NID DESC,LCBS DESC) WHERE 1=1 ");
		if(pjname!=null&&!pjname.equals("")){
			sql.append(" AND UPPER(PJNAME) LIKE ?");
			parameter.add(pjname);
		}
		if(jdmc!=null&&!jdmc.equals("")){
			sql.append(" AND UPPER(JDMC) LIKE ?");
			parameter.add(jdmc);
		}
		sql.append(") WHERE RM>? AND RM<=?");
		List<HashMap> dataList=zqbProjectManageDAO.getNhList(sql.toString(),parameter,pageNumber,pageSize);
		return dataList;
	}
	
	public int getNhListSize(String pjname, String jdmc) {
		List parameter=new ArrayList();//存放参数
		StringBuffer sql=new StringBuffer("SELECT COUNT(*) CNUM FROM (SELECT PJ.PROJECTNO,PJ.PROJECTNAME PJNAME,JD.JDMC,PJ.LCBH,to_number(nvl(PJ.LCBS,0)) LCBS,PJ.TASKID,PJ.OWNER FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT TASK.GROUPID,TASK.PROJECTNO,KMINFO.JDMC FROM BD_ZQB_KM_INFO KMINFO INNER JOIN (SELECT MAX(GROUPID) GROUPID,PROJECTNO FROM BD_PM_TASK GROUP BY PROJECTNO) TASK ON KMINFO.ID=TASK.GROUPID) JD ON PJ.PROJECTNO=JD.PROJECTNO) PJ LEFT JOIN (SELECT DISTINCT CREATEUSER,PROJECTNO FROM BD_ZQB_XMLCXXB) PJLC ON PJLC.PROJECTNO=PJ.PROJECTNO LEFT JOIN (SELECT NH.ID NID,BINDDATA.INSTANCEID NINSTANCEID,NH.PROJECTNO NPJNO,TO_CHAR(NH.NHDATE,'YYYY-MM-DD') NHDATE,NH.NHSPR NHSPR,TO_CHAR(NH.CBSJ,'YYYY-MM-DD HH24:MI') NHCBSJ FROM BD_ZQB_NHSPRQ NH INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_NHSPRQ') META ON IFORM.METADATAID=META.ID) BINDDATA ON NH.ID=BINDDATA.DATAID) NH ON NH.NPJNO=PJ.PROJECTNO WHERE 1=1");
		if(pjname!=null&&!pjname.equals("")){
			sql.append(" AND UPPER(PJNAME) LIKE ?");
			parameter.add(pjname);
		}
		if(jdmc!=null&&!jdmc.equals("")){
			sql.append(" AND UPPER(JDMC) LIKE ?");
			parameter.add(jdmc);
		}
		int count=zqbProjectManageDAO.getNhListSize(sql.toString(),parameter);
		return count;
	}

	public List getJdmcList() {
		List list = new ArrayList();
		list=zqbProjectManageDAO.getJdmcList();
		return list;
	}
	
	public void expZK(HttpServletResponse response,String pjname,String jdmc) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("ZKList");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setWrapText(true);
		style4.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		style4.setFont(headfont);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> list1 = new ArrayList<HashMap>();
		int m=0;
		int z=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell 
		cell1 = row1.createCell((short) z++);cell1.setCellValue("项目名称");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("部门负责人");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("阶段名称");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("提交人");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("呈报时间");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("质控日期");cell1.setCellStyle(style4);
		
		List parameter=new ArrayList();//存放参数
		StringBuffer sql=new StringBuffer("SELECT * FROM (SELECT ZID,ZINSTANCEID,ZPJNO,ZKDATE,ZKR,ZKCBSJ,PJNAME,JDMC,OWNER,LCCREATEUSER,ROWNUM RM FROM (SELECT ZK.ID ZID,BINDDATA.INSTANCEID ZINSTANCEID,ZK.PROJECTNO ZPJNO,TO_CHAR(ZK.ZKDATE,'YYYY-MM-DD') ZKDATE,ZK.ZKR ZKR,TO_CHAR(ZK.CBSJ,'YYYY-MM-DD HH24:MI') ZKCBSJ,PJ.PROJECTNAME PJNAME,PJ.JDMC JDMC,PJ.OWNER OWNER,PJLC.CREATEUSER LCCREATEUSER FROM BD_ZQB_ZKSPRQ ZK INNER JOIN (SELECT PJ.PROJECTNO,PJ.PROJECTNAME,JD.JDMC,PJ.OWNER FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT TASK.GROUPID,TASK.PROJECTNO,KMINFO.JDMC FROM BD_ZQB_KM_INFO KMINFO INNER JOIN (SELECT MAX(GROUPID) GROUPID,PROJECTNO FROM BD_PM_TASK GROUP BY PROJECTNO) TASK ON KMINFO.ID=TASK.GROUPID) JD ON PJ.PROJECTNO=JD.PROJECTNO) PJ ON ZK.PROJECTNO=PJ.PROJECTNO INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_ZKSPRQ') META ON IFORM.METADATAID=META.ID) BINDDATA ON ZK.ID=BINDDATA.DATAID INNER JOIN BD_ZQB_XMLCXXB PJLC ON PJLC.PROJECTNO=ZK.PROJECTNO ORDER BY ZK.ID) WHERE 1=1 ");
		if(pjname!=null&&!pjname.equals("")){
			sql.append(" AND UPPER(PJNAME) LIKE ?");
			parameter.add(pjname);
		}
		if(jdmc!=null&&!jdmc.equals("")){
			sql.append(" AND UPPER(JDMC) LIKE ?");
			parameter.add(jdmc);
		}
		sql.append(")");
		//sql.append(") WHERE RM>? AND RM<=?");
		list=zqbProjectManageDAO.expZkList(sql.toString(),parameter);
		for (HashMap map : list) {
			z=0;
			row1 = sheet.createRow((int) m++);
			HSSFCell 
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("PJNAME") == null ? "" : map.get("PJNAME").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("OWNER") == null ? "" : map.get("OWNER").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("JDMC") == null ? "" : map.get("JDMC").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("LCCREATEUSER") == null ? "" : map.get("LCCREATEUSER").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZKCBSJ") == null ? "" : map.get("ZKCBSJ").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZKDATE") == null ? "" : map.get("ZKDATE").toString());cell2.setCellStyle(style4);
		}
		for (int i = 0; i < 6; i++) {
			if(i==0){
				sheet.setColumnWidth(i, 15000);
			}else{
				sheet.setColumnWidth(i, 5000);
			}
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename="
					+ UploadFileNameCodingUtil.StringEncoding("设置质控日期.xls");
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
	public void expNH(HttpServletResponse response,String pjname,String jdmc) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("NHList");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setWrapText(true);
		style4.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		style4.setFont(headfont);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> list1 = new ArrayList<HashMap>();
		int m=0;
		int z=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell 
		cell1 = row1.createCell((short) z++);cell1.setCellValue("项目名称");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("部门负责人");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("阶段名称");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("提交人");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("呈报时间");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("内核日期");cell1.setCellStyle(style4);
		
		List parameter=new ArrayList();//存放参数
		StringBuffer sql=new StringBuffer("SELECT * FROM (SELECT NID,NINSTANCEID,NPJNO,NHDATE,NHSPR,NHCBSJ,PJNAME,JDMC,OWNER,LCCREATEUSER,ROWNUM RM FROM (SELECT NH.ID NID,BINDDATA.INSTANCEID NINSTANCEID,NH.PROJECTNO NPJNO,TO_CHAR(NH.NHDATE,'YYYY-MM-DD') NHDATE,NH.NHSPR NHSPR,TO_CHAR(NH.CBSJ,'YYYY-MM-DD HH24:MI') NHCBSJ,PJ.PROJECTNAME PJNAME,PJ.JDMC JDMC,PJ.OWNER OWNER,PJLC.CREATEUSER LCCREATEUSER FROM BD_ZQB_NHSPRQ NH INNER JOIN (SELECT PJ.PROJECTNO,PJ.PROJECTNAME,JD.JDMC,PJ.OWNER FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT TASK.GROUPID,TASK.PROJECTNO,KMINFO.JDMC FROM BD_ZQB_KM_INFO KMINFO INNER JOIN (SELECT MAX(GROUPID) GROUPID,PROJECTNO FROM BD_PM_TASK GROUP BY PROJECTNO) TASK ON KMINFO.ID=TASK.GROUPID) JD ON PJ.PROJECTNO=JD.PROJECTNO) PJ ON NH.PROJECTNO=PJ.PROJECTNO INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_NHSPRQ') META ON IFORM.METADATAID=META.ID) BINDDATA ON NH.ID=BINDDATA.DATAID INNER JOIN BD_ZQB_XMLCXXB PJLC ON PJLC.PROJECTNO=NH.PROJECTNO ORDER BY NH.ID) WHERE 1=1 ");
		if(pjname!=null&&!pjname.equals("")){
			sql.append(" AND UPPER(PJNAME) LIKE ?");
			parameter.add(pjname);
		}
		if(jdmc!=null&&!jdmc.equals("")){
			sql.append(" AND UPPER(JDMC) LIKE ?");
			parameter.add(jdmc);
		}
		sql.append(")");
		//sql.append(") WHERE RM>? AND RM<=?");
		list=zqbProjectManageDAO.expNhList(sql.toString(),parameter);
		
		
		for (HashMap map : list) {
			z=0;
			row1 = sheet.createRow((int) m++);
			HSSFCell 
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("PJNAME") == null ? "" : map.get("PJNAME").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("OWNER") == null ? "" : map.get("OWNER").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("JDMC") == null ? "" : map.get("JDMC").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("LCCREATEUSER") == null ? "" : map.get("LCCREATEUSER").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("NHCBSJ") == null ? "" : map.get("NHCBSJ").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("NHDATE") == null ? "" : map.get("NHDATE").toString());cell2.setCellStyle(style4);
		}
		for (int i = 0; i < 6; i++) {
			if(i==0){
				sheet.setColumnWidth(i, 15000);
			}else{
				sheet.setColumnWidth(i, 5000);
			}
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename="
					+ UploadFileNameCodingUtil.StringEncoding("设置内核日期.xls");
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
	
	public List getLogList(String projectname,String startDate,String endDate,String type) {
		List<String> dataList=new ArrayList<String>();//存放参数
		//StringBuffer sql=new StringBuffer("SELECT TO_CHAR(LOG.CREATEDATE,'yyyy-mm-dd hh24:mi:ss') CREATEDATE,LOG.MEMO MEMO,OUSER.USERNAME USERNAME FROM SYS_OPERATE_LOG LOG LEFT JOIN ORGUSER OUSER ON LOG.USERID=OUSER.USERID WHERE LOGTYPE=TO_CHAR((SELECT BIND.INSTANCEID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN (SELECT ID,METADATAID FROM SYS_ENGINE_IFORM WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_PJ_BASE')) IFORM ON BIND.FORMID=IFORM.ID AND BIND.METADATAID=IFORM.METADATAID WHERE DATAID=(SELECT ID FROM BD_ZQB_PJ_BASE WHERE PROJECTNAME = ?))) ORDER BY LOG.CREATEDATE DESC");
		StringBuffer sql=new StringBuffer("SELECT * FROM (SELECT ID,CREATEDATE,'{\"TYPE\":\"'||TYPE||'\",\"COLM\":\"'||FIELD_TITLE||'\",\"OLD\":\"'||OLD_VALUE||'\",\"NEW\":\"'||NEW_VALUE||'\",\"ENTITYNAME\":\"'||TABLENAME||'\"}' MEMO,USERNAME FROM (");
		sql.append(" SELECT LOG.ID,TO_CHAR(LOG.CREATEDATE,'yyyy-mm-dd hh24:mi:ss') CREATEDATE,CASE ACTION_TYPE WHEN 'ADD' THEN '数据新增' WHEN 'UPDATE' THEN '数据更新' ELSE NULL END TYPE,ORG.USERNAME,ITEM.FIELD_TITLE,ITEM.OLD_VALUE,ITEM.NEW_VALUE,ITEM.TABLENAME FROM (SELECT ID,CREATEDATE,USERID,ACTION_TYPE,FORM_NAME,INSTANCE_ID FROM SYS_IFORM_ACTION_LOG WHERE 1=1 ");
		if(startDate!=null&&!startDate.equals("")){
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') >= ? ");
			dataList.add(startDate);
		}else{
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') >= TO_CHAR(sysdate-7,'yyyy-mm-dd') ");
		}
		if(endDate!=null&&!endDate.equals("")){
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') <= ? ");
			dataList.add(endDate);
		}else{
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') <= TO_CHAR(sysdate,'yyyy-mm-dd') ");
		}
		sql.append(" AND (INSTANCE_ID=(SELECT BIND.INSTANCEID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN (SELECT ID,METADATAID FROM SYS_ENGINE_IFORM WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_PJ_BASE')) IFORM ON BIND.FORMID=IFORM.ID AND BIND.METADATAID=IFORM.METADATAID WHERE DATAID=(SELECT ID FROM BD_ZQB_PJ_BASE WHERE UPPER(PROJECTNAME) = ?)) OR INSTANCE_ID=(SELECT LCBS FROM BD_ZQB_PJ_BASE WHERE UPPER(PROJECTNAME) = ?))) LOG INNER JOIN ORGUSER ORG ON LOG.USERID=ORG.USERID INNER JOIN SYS_IFORM_ACTION_LOG_ITEM ITEM ON LOG.ID=ITEM.LOGID UNION ALL ");
		dataList.add(projectname);
		dataList.add(projectname);
		sql.append(" SELECT LOG.ID,TO_CHAR(LOG.CREATEDATE,'yyyy-mm-dd hh24:mi:ss') CREATEDATE,CASE ACTION_TYPE WHEN 'ADD' THEN '数据新增' WHEN 'UPDATE' THEN '数据更新' ELSE NULL END TYPE,ORG.USERNAME,ITEM.FIELD_TITLE,ITEM.OLD_VALUE,ITEM.NEW_VALUE,ITEM.TABLENAME FROM (SELECT ID,CREATEDATE,USERID,ACTION_TYPE,FORM_NAME,INSTANCE_ID FROM SYS_IFORM_ACTION_LOG WHERE 1=1 ");
		if(startDate!=null&&!startDate.equals("")){
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') >= ? ");
			dataList.add(startDate);
		}else{
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') >= TO_CHAR(sysdate-7,'yyyy-mm-dd') ");
		}
		if(endDate!=null&&!endDate.equals("")){
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') <= ? ");
			dataList.add(endDate);
		}else{
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') <= TO_CHAR(sysdate,'yyyy-mm-dd') ");
		}
		sql.append(" AND INSTANCE_ID=(SELECT BIND.INSTANCEID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN (SELECT ID,METADATAID FROM SYS_ENGINE_IFORM WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_PM_TASK')) IFORM ON BIND.FORMID=IFORM.ID AND BIND.METADATAID=IFORM.METADATAID WHERE DATAID=(SELECT ID FROM BD_PM_TASK WHERE UPPER(PROJECTNAME) = ?))) LOG INNER JOIN ORGUSER ORG ON LOG.USERID=ORG.USERID INNER JOIN SYS_IFORM_ACTION_LOG_ITEM ITEM ON LOG.ID=ITEM.LOGID) ");
		dataList.add(projectname);
		sql.append(" UNION ALL ");
		sql.append(" SELECT LOG.ID ID,TO_CHAR(LOG.CREATEDATE,'yyyy-mm-dd hh24:mi:ss') CREATEDATE,LOG.MEMO MEMO,OUSER.USERNAME USERNAME FROM (SELECT ID,CREATEDATE,USERID,MEMO FROM SYS_OPERATE_LOG WHERE 1=1 ");
		if(startDate!=null&&!startDate.equals("")){
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') >= ? ");
			dataList.add(startDate);
		}else{
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') >= TO_CHAR(sysdate-7,'yyyy-mm-dd') ");
		}
		if(endDate!=null&&!endDate.equals("")){
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') <= ? ");
			dataList.add(endDate);
		}else{
			sql.append(" AND TO_CHAR(CREATEDATE,'yyyy-mm-dd') <= TO_CHAR(sysdate,'yyyy-mm-dd') ");
		}
		sql.append(" AND LOGTYPE=TO_CHAR((SELECT BIND.INSTANCEID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN (SELECT ID,METADATAID FROM SYS_ENGINE_IFORM WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_PJ_BASE')) IFORM ON BIND.FORMID=IFORM.ID AND BIND.METADATAID=IFORM.METADATAID WHERE DATAID=(SELECT ID FROM BD_ZQB_PJ_BASE WHERE UPPER(PROJECTNAME) = ?)))) LOG LEFT JOIN ORGUSER OUSER ON LOG.USERID=OUSER.USERID) ");
		dataList.add(projectname);
		if(type!=null&&!type.equals("")){
			if(type.equals("1")){
				sql.append(" WHERE MEMO like ? or MEMO like ?");
				dataList.add("%\"ENTITYNAME\":\"BD_ZQB_PJ_BASE\"%");
				dataList.add("%\"ENTITYNAME\":\"BD_ZQB_XMLCXXB\"%");
			}else if(type.equals("2")){
				sql.append(" WHERE MEMO like ?");
				dataList.add("%\"ENTITYNAME\":\"BD_PM_TASK\"%");
			}else if(type.equals("3")){
				sql.append(" WHERE MEMO like ?");
				dataList.add("%\"ENTITYNAME\":\"BD_ZQB_CLJG\"%");
			}else if(type.equals("4")){
				sql.append(" WHERE MEMO like ?");
				dataList.add("%\"ENTITYNAME\":\"BD_ZQB_CLR\"%");
			}else if(type.equals("5")){
				sql.append(" WHERE MEMO like ?");
				dataList.add("%\"ENTITYNAME\":\"BD_ZQB_GROUP\"%");
			}
		}
		sql.append(" ORDER BY CREATEDATE DESC,ID DESC");
		List<HashMap> list=zqbProjectManageDAO.getLogList(sql.toString(),dataList);
		for (HashMap hashMap : list) {
			StringBuffer content=new StringBuffer();
			String memo = hashMap.get("MEMO").toString();
			JSONObject json = JSONObject.fromObject(memo);
			String entityname = json.getString("ENTITYNAME");
			if(entityname.equals("BD_ZQB_CLR")){
				String ssbm = json.getString("SSBM");
				String pjname = json.getString("PROJECTNAME");
				String status = json.getString("STATUS");
				content.append("承揽人:").append(ssbm).append(" , 所属部门:").append(ssbm);
				hashMap.put("CONTENT", content.toString());
				hashMap.put("PROJECTNAME", pjname);
				hashMap.put("STATUS", status);
				hashMap.put("TITLE", "承揽人");
			}else if(entityname.equals("BD_ZQB_CLJG")){
				String fpbl = json.getString("FPBL");
				String jgmc = json.getString("JGMC");
				String tbr = json.getString("TBR");
				String pjname = json.getString("PROJECTNAME");
				String status = json.getString("STATUS");
				content.append("分配部门:").append(jgmc).append(" , 分配比例:").append(fpbl).append("% , 填报人:").append(tbr);
				hashMap.put("CONTENT", content.toString());
				hashMap.put("PROJECTNAME", pjname);
				hashMap.put("STATUS", status);
				hashMap.put("TITLE", "分配部门");
			}else if(entityname.equals("BD_ZQB_GROUP")){
				String name = json.getString("NAME");
				String tel = json.getString("TEL");
				String email = json.getString("EMAIL");
				String phone = json.getString("PHONE");
				String departmentname = json.getString("DEPARTMENTNAME");
				String pjname = json.getString("PROJECTNAME");
				String status = json.getString("STATUS");
				content.append("姓名:").append(name).append(" , 联系电话:").append(tel).append(" , 邮箱:").append(email).append(" , 手机:").append(phone).append(" , 所属部门:").append(departmentname);
				hashMap.put("CONTENT", content.toString());
				hashMap.put("PROJECTNAME", pjname);
				hashMap.put("STATUS", status);
				hashMap.put("TITLE", "项目成员");
			}else if(entityname.equals("BD_ZQB_PJ_BASE")||entityname.equals("BD_ZQB_XMLCXXB")){
				String colm = json.getString("COLM");
				String vold = json.getString("OLD");
				String vnew = json.getString("NEW");
				String status = json.getString("TYPE");
				content.append(colm+":原值:").append(vold).append(" , 修改值:").append(vnew);
				hashMap.put("CONTENT", content.toString());
				hashMap.put("PROJECTNAME", projectname);
				hashMap.put("STATUS", status);
				hashMap.put("TITLE", "项目管理信息");
			}else if (entityname.equals("BD_PM_TASK")){
				String colm = json.getString("COLM");
				String vold = json.getString("OLD");
				String vnew = json.getString("NEW");
				String status = json.getString("TYPE");
				content.append(colm+":原值:").append(vold).append(" , 修改值:").append(vnew);
				hashMap.put("CONTENT", content.toString());
				hashMap.put("PROJECTNAME", projectname);
				hashMap.put("STATUS", status);
				hashMap.put("TITLE", "项目阶段信息");
			}
		}
		return list;
	}

	public String getZkAndNhDate(String projectno) {
		StringBuffer jsonHtml = new StringBuffer();
		HashMap resultMap=new HashMap();
		String isDwPj = getConfigUUID("isDwPj");
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql = new StringBuffer("select '质控' TYPE,TO_CHAR(ZK.ZKDATE,'yyyy-mm-dd') RDATE from bd_zqb_zksprq ZK where projectno=? union all select '内核' TYPE,TO_CHAR(NH.Nhdate,'yyyy-mm-dd') RDATE from bd_zqb_nhsprq NH where projectno=? ");
		parameter.add(projectno);
		parameter.add(projectno);
		List<HashMap<String,String>> dataMap=zqbProjectManageDAO.getZkAndNhDate(sql.toString(),parameter);
		for (HashMap<String, String> hashMap : dataMap) {
			String type = hashMap.get("TYPE");
			if(type.equals("质控")){
				resultMap.put("ZKDATE", hashMap.get("RDATE"));
			}else if(type.equals("内核")){
				resultMap.put("NHDATE", hashMap.get("RDATE"));
			}
		}
		boolean IsZkOrNh=ZQBNoticeUtil.getInstance().getZkAndNhCount()>0?true:false;
		JSONArray jsonArray=JSONArray.fromObject(resultMap);
		jsonHtml.append("{\"isDwPj\":"+isDwPj+",\"rows\":" + jsonArray.toString() + ",\"IsZkOrNh\":"+IsZkOrNh+"}");
		return jsonHtml.toString();
	}
	
	public boolean setPjLocking(Long instanceId){
		HashMap fromData = DemAPI.getInstance().getFromData(instanceId);
		if(fromData!=null){
			String lcbh = fromData.get("LCBH").toString();
			Long lcbs=fromData.get("LCBS")==null?0:Long.parseLong(fromData.get("LCBS").toString().equals("")?"0":fromData.get("LCBS").toString());
			if(!lcbh.equals("")&&lcbs!=0){
				HashMap lcfromData = ProcessAPI.getInstance().getFromData(lcbs, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(lcfromData!=null){
					Long dataid = Long.parseLong(lcfromData.get("ID").toString());
					lcfromData.put("A02",1);
					ProcessAPI.getInstance().updateFormData(lcbh, lcbs, lcfromData, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				}
			}
			Long dataid = Long.parseLong(fromData.get("ID").toString());
			fromData.put("A02",1);
			return DemAPI.getInstance().updateFormData(ProjectUUID, instanceId, fromData, dataid, false);
		}else{
			return false;
		}
	}
	
	public boolean cleanPjLocking(Long instanceId){
		HashMap fromData = DemAPI.getInstance().getFromData(instanceId);
		/*String lcbh = fromData.get("LCBH").toString();
		Long lcbs = fromData.get("LCBS").toString().equals("")?0L:Long.parseLong(fromData.get("LCBS").toString());
		if(!lcbh.equals("")&&lcbs!=0){
			HashMap lcfromData = ProcessAPI.getInstance().getFromData(lcbs, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			Long dataid = Long.parseLong(lcfromData.get("ID").toString());
			lcfromData.put("A02",0);
			ProcessAPI.getInstance().updateFormData(lcbh, lcbs, lcfromData, dataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}*/
		Long zblcbs = fromData.get("ZBLCBS").toString().equals("")?0L:Long.parseLong(fromData.get("ZBLCBS").toString());
		if(zblcbs!=0){
			Task newTaskId = ProcessAPI.getInstance().newTaskId(zblcbs);
			boolean removeProcessInstance= false;
			if(newTaskId!=null){
				String taskId = newTaskId.getId();
				String owner = newTaskId.getOwner();
				ProcessAPI.getInstance().setTaskAssignee(taskId, owner);
				removeProcessInstance = ProcessAPI.getInstance().removeProcessInstance(taskId, owner);
			}
			if(removeProcessInstance){
				fromData.put("ZBSPZT", "");
				fromData.put("ZBLCBH", "");
				fromData.put("ZBLCBS", "");
				fromData.put("ZBSTEPID", "");
				fromData.put("ZBTASKID", "");
			}
		}
		Long dataid = Long.parseLong(fromData.get("ID").toString());
		fromData.put("A02",0);
		fromData.put("ZBSPZT","");
		fromData.put("ZBLCBS","");
		return DemAPI.getInstance().updateFormData(ProjectUUID, instanceId, fromData, dataid, false);
	}

	public String getSubSpContent(Long instanceid) {
		String taskid = "";
		String executionId = "";
		String actDefId ="";
		Long instanceId = 0L;
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		HashMap lcFromData = (HashMap) fromData.clone();
		String zbspzt=fromData.get("ZBSPZT")==null?"":fromData.get("ZBSPZT").toString();
		Long lcbs=fromData.get("ZBLCBS")==null?0:Long.parseLong(fromData.get("ZBLCBS").toString().equals("")?"0":fromData.get("ZBLCBS").toString());
		Long lcInstanceId = 0L;
		if(lcbs==0&&zbspzt.equals("")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser getUserModel = uc.get_userModel();
			String username = getUserModel.getUsername();
			String userid = getUserModel.getUserid();
			actDefId= ProcessAPI.getInstance().getProcessActDefId("XMZBLC");
			lcFromData.put("CREATEUSER", username);
			lcFromData.put("CREATEUSERID", userid);
			lcFromData.put("CREATEDATE", UtilDate.getNowDatetime());
			Long formId = ProcessAPI.getInstance().getProcessDefaultFormId(actDefId);
			lcInstanceId = ProcessAPI.getInstance().newInstance(actDefId, formId, userid);
			ProcessAPI.getInstance().saveFormData(actDefId, lcInstanceId, lcFromData, false);// 保存流程
			String jd1 = SystemConfig._xmzbConf.getJd1();
			List<Task> tasklist = ProcessAPI.getInstance().getUserProcessTaskList(actDefId, jd1,userid);
			Task newTaskId = ProcessAPI.getInstance().newTaskId(lcInstanceId);
			executionId=newTaskId.getExecutionId();
			taskid=newTaskId.getId();
			fromData.put("A02", 2);
			fromData.put("ZBSPZT", "未提交");
			fromData.put("ZBLCBH", actDefId);
			fromData.put("ZBLCBS", lcInstanceId);
			fromData.put("ZBSTEPID", jd1);
			fromData.put("ZBTASKID", taskid);
			instanceId=Long.parseLong(executionId);
			Long dataid = Long.parseLong(fromData.get("ID").toString());
			DemAPI.getInstance().updateFormData(ProjectUUID, instanceid, fromData, dataid, false);
			
			List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_CLR");
			List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_CLJG");
			
			List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
			setFromSubData(lcFromSubClrData,actDefId,instanceId,"SUBFORM_CLR",fromSubClrData,false);
			List<HashMap> lcFromSubCljgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
			setFromSubData(lcFromSubCljgData,actDefId,instanceId,"SUBFORM_CLJG",fromSubCljgData,false);
			
			String lcbh = fromData.get("LCBH").toString();
			Long pjlcbs=fromData.get("LCBS")==null?0:Long.parseLong(fromData.get("LCBS").toString().equals("")?"0":fromData.get("LCBS").toString());
			if(!pjlcbs.equals("")&&pjlcbs!=0){
				HashMap lcfromData = ProcessAPI.getInstance().getFromData(pjlcbs, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(lcfromData!=null){
					Long lcDataid = Long.parseLong(lcfromData.get("ID").toString());
					lcfromData.put("A02",2);
					ProcessAPI.getInstance().updateFormData(lcbh, pjlcbs, lcfromData, lcDataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				}
				/*List<HashMap> lcFromClrData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
				setFromSubData(lcFromClrData,lcbh,pjlcbs,"SUBFORM_CLR",fromSubClrData,false);
				List<HashMap> lcFromCljgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
				setFromSubData(lcFromCljgData,lcbh,pjlcbs,"SUBFORM_CLJG",fromSubCljgData,false);*/
			}
			
		}else{
			actDefId=fromData.get("ZBSPZT")==null?"":fromData.get("ZBSPZT").toString();
			actDefId=fromData.get("ZBLCBH")==null?"":fromData.get("ZBLCBH").toString();
			taskid=fromData.get("ZBTASKID")==null?"":fromData.get("ZBTASKID").toString();
			executionId=fromData.get("ZBLCBS")==null?"0":fromData.get("ZBLCBS").toString();
		}
		instanceId=Long.parseLong(executionId);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+instanceId+"}");
		return jsonHtml.toString();
	}

	public String getSubLcSpContent(String projectno) {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("PROJECTNO", projectno);
		List<HashMap> pjList = DemAPI.getInstance().getAllList(ProjectUUID, map, null);
		HashMap pjMap=pjList.get(0);
		Long instanceid = Long.parseLong(pjMap.get("INSTANCEID").toString());
		return getSubSpContent(instanceid);
	}

	public void projectSyncData(String projectno) {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("PROJECTNO", projectno);
		List<HashMap> pjList = DemAPI.getInstance().getAllList(ProjectUUID, map, null);
		HashMap pjMap=pjList.get(0);
		String lcbh = pjMap.get("LCBH").toString();
		Long pjlcbs=pjMap.get("LCBS")==null?0:Long.parseLong(pjMap.get("LCBS").toString().equals("")?"0":pjMap.get("LCBS").toString());
		Long instanceid = Long.parseLong(pjMap.get("INSTANCEID").toString());
		if(pjlcbs!=0){
			HashMap lcfromData = ProcessAPI.getInstance().getFromData(pjlcbs, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if(lcfromData!=null){
				Long demId = DemAPI.getInstance().getDemModel(ProjectUUID).getId();
				List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_CLR");
				List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_CLJG");
				List<HashMap> fromSubXmcyData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_XMCYLB");
				List<HashMap> fromSubZjjgData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_XMZJJG");
				
				List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(pjlcbs, "SUBFORM_CLR");
				setFromSubData(lcFromSubClrData,lcbh,pjlcbs,"SUBFORM_CLR",fromSubClrData,true);
				List<HashMap> lcFromSubCljgData = ProcessAPI.getInstance().getFromSubData(pjlcbs, "SUBFORM_CLJG");
				setFromSubData(lcFromSubCljgData,lcbh,pjlcbs,"SUBFORM_CLJG",fromSubCljgData,true);
				List<HashMap> lcFromSubXmcyData = ProcessAPI.getInstance().getFromSubData(pjlcbs, "SUBFORM_XMCYLB");
				setFromSubData(lcFromSubXmcyData,lcbh,pjlcbs,"SUBFORM_XMCYLB",fromSubXmcyData,true);
				List<HashMap> lcFromSubZjjgData = ProcessAPI.getInstance().getFromSubData(pjlcbs, "SUBFORM_XMZJJG");
				setFromSubData(lcFromSubZjjgData,lcbh,pjlcbs,"SUBFORM_XMZJJG",fromSubZjjgData,true);
			}
		}
	}

	public static String getZqbGpfxProjectThisCheck(String projectno) {
		String spztcheck = "";
		StringBuffer sqlcheck = new StringBuffer("SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD, E.GROUPID,F.JDMC,E.SPZT, E.MANAGER,E.PJINSID,BINDTABLE.INSTANCEID,NVL(G.LDPJ,'评价') LDPJ FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J WHERE PROJECTNO=? AND 1=1  ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导' ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE FORMID = 172 AND INSTANCEID IS NOT NULL) BINDTABLE ON A.ID = BINDTABLE.DATAID LEFT JOIN BD_ZQB_GPFXXMPJB G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID");
		//获取配置参数
		Connection conncheck = DBUtil.open();
		PreparedStatement pscheck = null;
		ResultSet rscheck = null;
		try {
			pscheck = conncheck.prepareStatement(sqlcheck.toString());
			pscheck.setString(1, projectno);
			rscheck = pscheck.executeQuery();
			while(rscheck.next()){
				if(rscheck.getString("SPZT")!=null && rscheck.getString("SPZT").equals("审批通过")){
					spztcheck = rscheck.getString("SPZT");
					break;
				}
			}
			} catch (Exception e) {
				logger.error(e,e);
		} finally{
			DBUtil.close(conncheck, pscheck, rscheck);
		}
		return spztcheck;
	}

	public String getZqbProjectThisCheck(String projectno) {
		String spzt="";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.SPZT XMSPZT FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J WHERE 1=1 AND PROJECTNO=?) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE FORMID = 91 AND METADATAID = 101 AND INSTANCEID IS NOT NULL) BINDTABLE ON A.ID = BINDTABLE.DATAID LEFT JOIN (SELECT * FROM BD_ZQB_XMRWPJB A INNER JOIN (SELECT MAX(ID) ID FROM BD_ZQB_XMRWPJB) B ON A.ID=B.ID) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中'");
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, projectno);
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getString("XMSPZT")!=null && rs.getString("XMSPZT").equals("审批通过")){
					spzt = rs.getString("XMSPZT");
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return spzt;
	}

	public List<HashMap> getZqbProjectCostormerSet(String name, String no) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT K.CUSTOMERNAME,K.CUSTOMERNO,K.USERNAME,K.TEL,K.CUSTOMERDESC,S.INSTANCEID FROM BD_ZQB_KH_BASE K LEFT JOIN SYS_ENGINE_FORM_BIND S ON K.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='客户信息维护表单')");
		if(name!=null&&!name.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+name.trim()+"%");
		}
		if(no!=null&&!no.equals("")){
			sql.append(" AND K.CUSTOMERNO LIKE ?");
			params.add("%"+no.trim()+"%");
		}
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap map;
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i+1, params.get(i));
			}
			rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap();
				String CUSTOMERNAME = rs.getString("CUSTOMERNAME");
				String CUSTOMERNO = rs.getString("CUSTOMERNO");
				String USERNAME = rs.getString("USERNAME");
				String TEL = rs.getString("TEL");
				String INSTANCEID=rs.getString("INSTANCEID");
				String CUSTOMERDESC= rs.getString("CUSTOMERDESC");
				map.put("CUSTOMERNAME", CUSTOMERNAME==null ? "":CUSTOMERNAME);
				map.put("CUSTOMERNO", CUSTOMERNO==null ? "":CUSTOMERNO);
				map.put("USERNAME", USERNAME==null ? "":USERNAME);
				map.put("TEL", TEL==null ? "":TEL);
				map.put("CUSTOMERDESC", CUSTOMERDESC==null ? "":CUSTOMERDESC);
				map.put("INSTANCEID", INSTANCEID==null ? "":INSTANCEID);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return list;
	}
	

	public List<HashMap> getZqbGpfxProjectCostormerSet(String name, String no) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT K.CUSTOMERNAME,K.CUSTOMERNO,K.USERNAME,K.TEL,K.CUSTOMERDESC FROM BD_ZQB_KH_BASE K LEFT JOIN SYS_ENGINE_FORM_BIND S ON K.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='客户信息维护表单')");
		if(name!=null&&!name.equals("")){
			sql.append(" AND UPPER(K.CUSTOMERNAME) LIKE ?");
			params.add("%"+name.trim().toUpperCase()+"%");
		}
		if(no!=null&&!no.equals("")){
			sql.append(" AND K.CUSTOMERNO LIKE ?");
			params.add("%"+no.trim()+"%");
		}
		sql.append(" AND K.YGP='已挂牌' AND K.STATUS = '有效' ");
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap map;
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i+1, params.get(i));
			}
			rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap();
				String CUSTOMERNAME = rs.getString("CUSTOMERNAME");
				String CUSTOMERNO = rs.getString("CUSTOMERNO");
				String USERNAME = rs.getString("USERNAME");
				String TEL = rs.getString("TEL");
				String CUSTOMERDESC= rs.getString("CUSTOMERDESC");
				map.put("CUSTOMERNAME", CUSTOMERNAME==null ? "":CUSTOMERNAME);
				map.put("CUSTOMERNO", CUSTOMERNO==null ? "":CUSTOMERNO);
				map.put("USERNAME", USERNAME==null ? "":USERNAME);
				map.put("TEL", TEL==null ? "":TEL);
				map.put("CUSTOMERDESC", CUSTOMERDESC==null ? "":CUSTOMERDESC);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public List<HashMap> loadDz(Long instanceid, String projectno) {
		StringBuffer sql = new StringBuffer("SELECT C.*,D.ISEXISTS,D.ISLOCK FROM (SELECT A.ID,A.DZNR,CASE WHEN B.A03 IS NULL THEN '0' ELSE B.A03 END DZID FROM (SELECT ID,DZNR FROM BD_ZQB_XMDZ ORDER BY ID) A LEFT JOIN (SELECT A03 FROM BD_ZQB_PJ_BASE WHERE PROJECTNO=?) B ON A.ID=B.A03) C,(SELECT CASE WHEN A03 IS NULL THEN '0' ELSE A03 END ISEXISTS,CASE WHEN A02 IS NULL THEN 0 ELSE A02 END ISLOCK FROM BD_ZQB_PJ_BASE WHERE PROJECTNO=?) D");
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap map;
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, projectno);
			ps.setString(2, projectno);
			rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap();
				Long id = rs.getLong("ID");
				String dznr = rs.getString("DZNR");
				String dzid = rs.getString("DZID");
				String isexists = rs.getString("ISEXISTS");
				Long islock = rs.getLong("ISLOCK");
				map.put("ID", id);
				map.put("DZNR", dznr);
				map.put("DZID", dzid);
				map.put("ISLOCK", islock);
				map.put("ISEXISTS", isexists);
				map.put("PROJECTNO", projectno);
				map.put("INSTANCEID", instanceid);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return list;
	}

	public String setPjDz(Long instanceid, String dzid) {
		String info;
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		String projectname = fromData.get("PROJECTNAME").toString();
		fromData.put("A03", dzid);
		Long dataid = Long.parseLong(fromData.get("ID").toString());
		boolean updateFormData = DemAPI.getInstance().updateFormData(ProjectUUID, instanceid, fromData, dataid, false);
		Long lcbs=fromData.get("LCBS")==null?0:Long.parseLong(fromData.get("LCBS").toString().equals("")?"0":fromData.get("LCBS").toString());
		if(lcbs!=0){
			HashMap lcfromData = ProcessAPI.getInstance().getFromData(lcbs, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if(lcfromData!=null){
				Long lcdataid = Long.parseLong(lcfromData.get("ID").toString());
				String lcbh = fromData.get("LCBH").toString();
				lcfromData.put("A03", dzid);
				updateFormData=ProcessAPI.getInstance().updateFormData(lcbh, lcbs, lcfromData, lcdataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			}
		}
		if(updateFormData){
			LogUtil.getInstance().addLog(instanceid, "项目定增", projectname+"项目设置定增ID为"+dzid);
			info="success";
		}else{
			info="error";
		}
		return info;
	}

	public String cleanPjDz(Long instanceid) {
		String info;
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		String projectname = fromData.get("PROJECTNAME").toString();
		fromData.put("A03", "");
		Long dataid = Long.parseLong(fromData.get("ID").toString());
		boolean updateFormData = DemAPI.getInstance().updateFormData(ProjectUUID, instanceid, fromData, dataid, false);
		Long lcbs=fromData.get("LCBS")==null?0:Long.parseLong(fromData.get("LCBS").toString().equals("")?"0":fromData.get("LCBS").toString());
		if(lcbs!=0){
			HashMap lcfromData = ProcessAPI.getInstance().getFromData(lcbs, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if(lcfromData!=null){
				Long lcdataid = Long.parseLong(lcfromData.get("ID").toString());
				String lcbh = fromData.get("LCBH").toString();
				lcfromData.put("A03", "");
				updateFormData=ProcessAPI.getInstance().updateFormData(lcbh, lcbs, lcfromData, lcdataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			}
		}
		if(updateFormData){
			LogUtil.getInstance().addLog(instanceid, "项目定增", projectname+"项目取消定增");
			info="success";
		}else{
			info="error";
		}
		return info;
	}
	
	public List<HashMap> loadLcDz(Long instanceid, String projectno) {
		StringBuffer sql = new StringBuffer("SELECT C.*,D.ISEXISTS,D.ISLOCK FROM (SELECT A.ID,A.DZNR,CASE WHEN B.A03 IS NULL THEN '0' ELSE B.A03 END DZID FROM (SELECT ID,DZNR FROM BD_ZQB_XMDZ ORDER BY ID) A LEFT JOIN (SELECT A03 FROM BD_ZQB_PJ_BASE WHERE PROJECTNO=?) B ON A.ID=B.A03) C,(SELECT CASE WHEN A03 IS NULL THEN '0' ELSE A03 END ISEXISTS,CASE WHEN A02 IS NULL THEN 0 ELSE A02 END ISLOCK FROM BD_ZQB_PJ_BASE WHERE PROJECTNO=?) D");
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap map;
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, projectno);
			ps.setString(2, projectno);
			rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap();
				Long id = rs.getLong("ID");
				String dznr = rs.getString("DZNR");
				String dzid = rs.getString("DZID");
				String isexists = rs.getString("ISEXISTS");
				Long islock = rs.getLong("ISLOCK");
				map.put("ID", id);
				map.put("DZNR", dznr);
				map.put("DZID", dzid);
				map.put("ISLOCK", islock);
				map.put("ISEXISTS", isexists);
				map.put("PROJECTNO", projectno);
				map.put("INSTANCEID", instanceid);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public String setLcPjDz(Long instanceid, String dzid) {
		String info;
		HashMap lcfromData = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap conditionMap=new HashMap();
		conditionMap.put("PROJECTNO", lcfromData.get("PROJECTNO").toString());
		HashMap fromData = DemAPI.getInstance().getAllList(ProjectUUID, conditionMap, null).get(0);
		String projectname = fromData.get("PROJECTNAME").toString();
		Long lcdataid = Long.parseLong(lcfromData.get("ID").toString());
		String lcbh = fromData.get("LCBH").toString();
		lcfromData.put("A03", dzid);
		boolean updateFormData=ProcessAPI.getInstance().updateFormData(lcbh, instanceid, lcfromData, lcdataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		Long pjinsid = Long.parseLong(fromData.get("INSTANCEID").toString());
		fromData.put("A03", dzid);
		Long dataid = Long.parseLong(fromData.get("ID").toString());
		updateFormData = DemAPI.getInstance().updateFormData(ProjectUUID, pjinsid, fromData, dataid, false);
		if(updateFormData){
			LogUtil.getInstance().addLog(instanceid, "项目定增", projectname+"项目设置定增ID为"+dzid);
			info="success";
		}else{
			info="error";
		}
		return info;
	}
	
	public String cleanLcPjDz(Long instanceid) {
		String info;
		HashMap lcfromData = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap conditionMap=new HashMap();
		conditionMap.put("PROJECTNO", lcfromData.get("PROJECTNO").toString());
		HashMap fromData = DemAPI.getInstance().getAllList(ProjectUUID, conditionMap, null).get(0);
		String projectname = fromData.get("PROJECTNAME").toString();
		Long lcdataid = Long.parseLong(lcfromData.get("ID").toString());
		String lcbh = fromData.get("LCBH").toString();
		lcfromData.put("A03", "");
		boolean updateFormData=ProcessAPI.getInstance().updateFormData(lcbh, instanceid, lcfromData, lcdataid, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		Long pjinsid = Long.parseLong(fromData.get("INSTANCEID").toString());
		fromData.put("A03", "");
		Long dataid = Long.parseLong(fromData.get("ID").toString());
		updateFormData = DemAPI.getInstance().updateFormData(ProjectUUID, pjinsid, fromData, dataid, false);
		if(updateFormData){
			LogUtil.getInstance().addLog(instanceid, "项目定增", projectname+"项目取消定增");
			info="success";
		}else{
			info="error";
		}
		return info;
	}

	public String createZkData(Long zkformid, Long zkid, String projectno,String projectname, String zkspr) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zkUUID = config.get("zkuuid");
		HashMap map = new HashMap();
		map.put("PROJECTNO",projectno);
		List<HashMap> zkList = DemAPI.getInstance().getList(zkUUID, map, null);
		Long instanceid = 0L;
		if(zkList.size()==1){
			HashMap nhMap = zkList.get(0);
			instanceid = Long.parseLong(nhMap.get("INSTANCEID").toString());
		}else{
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser userModel = uc._userModel;
			instanceid = DemAPI.getInstance().newInstance(zkUUID,userModel.getUserid());
			HashMap<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("instanceId", instanceid);
			dataMap.put("PROJECTNO", projectno);
			dataMap.put("PROJECTNAME", projectname);
			dataMap.put("CBSJ", UtilDate.getNowDatetime());
			dataMap.put("ZKR", zkspr);
			DemAPI.getInstance().saveFormData(zkUUID, instanceid, dataMap,false);
		}
		
		StringBuffer jsonHtml=new StringBuffer();
		jsonHtml.append("{\"instanceid\":"+instanceid+",\"formid\":"+zkformid+",\"demId\":"+zkid+"}");
		return jsonHtml.toString();
	}

	public String createNhData(Long nhformid, Long nhid, String projectno,String projectname, String nhspr) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String nhUUID = config.get("nhuuid");
		HashMap map = new HashMap();
		map.put("PROJECTNO",projectno);
		List<HashMap> nhList = DemAPI.getInstance().getList(nhUUID, map, null);
		Long instanceid = 0L;
		if(nhList.size()==1){
			HashMap nhMap = nhList.get(0);
			instanceid = Long.parseLong(nhMap.get("INSTANCEID").toString());
		}else{
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser userModel = uc._userModel;
			instanceid = DemAPI.getInstance().newInstance(nhUUID,userModel.getUserid());
			HashMap<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("instanceId", instanceid);
			dataMap.put("PROJECTNO", projectno);
			dataMap.put("PROJECTNAME", projectname);
			dataMap.put("CBSJ", UtilDate.getNowDatetime());
			dataMap.put("NHSPR", nhspr);
			DemAPI.getInstance().saveFormData(nhUUID, instanceid, dataMap,false);
		}
		StringBuffer jsonHtml=new StringBuffer();
		jsonHtml.append("{\"instanceid\":"+instanceid+",\"formid\":"+nhformid+",\"demId\":"+nhid+"}");
		return jsonHtml.toString();
	}
}
