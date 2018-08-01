package com.ibpmsoft.project.zqb.dao;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.activiti.engine.task.Task;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.FileType;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.dao.FileUploadDAO;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.ProcessAPI;

public class ZqbAnnouncementDAO extends HibernateDaoSupport {
	private static final String GGSMJ_UUID="cfa092653f594bf4b71fbd49baa64662";
	private final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private static final String CXDDFP_UUID="84ff70949eac4051806dc02cf4837bd9";

	public List<HashMap> getList(String khbh,
			String noticename,String noticetype, String startdate, String enddate,String bzlx,String spzt,String zqdm,String zqjc,String dq,String companyno) {
		List l=new ArrayList();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		/*if("".equals(khbh)||khbh==null){
			return l;
		}*/
		final String userid = uc.get_userModel().getUserid();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT INSTANCEID,NOTICENAME,NOTICETYPE,TO_CHAR(NOTICEDATE,'YYYY-MM-DD') NOTICEDATE," +
				"CREATENAME,TO_CHAR(A.CREATEDATE,'yyyy-MM-dd') CREATEDATE,A.ID,LCBH,LCBS,YXID,RWID,STEPID,NOTICETEXT,SPZT,PRCID,MEETID,MEETNAME,NOTICEFILE,GGDF,QCRID,B.ID BID,"+ 
				"A.ZQJCXS," +
				"ROUND(LENGTH(REPLACE(SMJ,',',''))/32) NUM,NOTICENO," +
				"A.COMPANYNO " +
				"FROM BD_MEET_QTGGZL A LEFT JOIN BD_XP_GZXTYJB B ON A.ID=B.GGID "+ 				
				"LEFT JOIN BD_XP_GGSMJ SM ON A.ID=SM.GGID");
		 //XLJ 2017年5月24日11:34:45新增开始
		 if (dq != null && !"".equals(dq)) {
			 sql.append(" INNER JOIN (SELECT CUSTOMERNO,ZCQX FROM BD_ZQB_KH_BASE WHERE ZCQX LIKE ?) KH ON KH.CUSTOMERNO=A.KHBH");
			 params.add("%" + dq + "%");
		}
			sql.append(" WHERE 1=1 ");
		 //XLJ 2017年5月24日11:34:49新增截止
		if(khbh!=null&&!khbh.equals("")){
			sql.append(" AND A.KHBH=?");
			params.add(khbh);
		}else{
			OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
			Long orgroleid = user.getOrgroleid();
			if(orgroleid==3l){
				khbh = user.getExtend1();
				sql.append(" AND A.KHBH=?");
				params.add(khbh);
			}else if(orgroleid==5l){
			}else{
				String currentuserid = user.getUserid();
				sql.append(" AND A.KHBH IN (SELECT khbh FROM BD_MDM_KHQXGLB WHERE 1=1 AND(SUBSTR(KHFZR,0, instr(KHFZR,'[',1)-1) =? OR SUBSTR(ZZCXDD,0, instr(ZZCXDD,'[',1)-1) =? OR SUBSTR(FHSPR,0, instr(FHSPR,'[',1)-1) =? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) =? OR SUBSTR(CWSCBFZR2,0, instr(CWSCBFZR2,'[',1)-1) =? OR SUBSTR(CWSCBFZR3,0, instr(CWSCBFZR3,'[',1)-1) =? OR SUBSTR(qynbrysh, instr(qynbrysh,'[',1)-1) =?))");
				params.add(currentuserid);
				params.add(currentuserid);
				params.add(currentuserid);
				params.add(currentuserid);
				params.add(currentuserid);
				params.add(currentuserid);
				params.add(currentuserid);
			}
		}
		if (zqdm != null && !"".equals(zqdm)) {
			sql.append(" AND A.ZQDMXS LIKE ?");
			params.add("%" + zqdm + "%");
		}
		if (zqjc != null && !"".equals(zqjc)) {
			sql.append(" AND A.ZQJCXS LIKE ?");
			params.add("%" + zqjc + "%");
		}
		if (noticename != null && !"".equals(noticename)) {
			sql.append(" AND A.NOTICENAME LIKE ?");
			params.add("%" + noticename + "%");
		}
		if (startdate != null && !"".equals(startdate)) {
			sql.append(" AND TO_CHAR(A.NOTICEDATE,'yyyy-MM-dd')>= ?");
			params.add(startdate);
		}
		if (noticetype != null && !"".equals(noticetype)) {
			sql.append(" AND A.NOTICETYPE = ?");
			params.add(noticetype);
		}
		if (enddate != null && !"".equals(enddate)) {
			sql.append(" AND TO_CHAR(A.NOTICEDATE,'yyyy-MM-dd')<= ?");
			params.add(enddate);
		}
		if(spzt!=null&&!spzt.equals("")){
			sql.append(" AND A.SPZT=?");
			params.add(spzt);
		}
		if (companyno != null && !"".equals(companyno)) {
			sql.append(" AND A.COMPANYNO = ?");
			params.add(companyno);
		}
		/*if(bzlx!=null&&!bzlx.equals("")){
			sql.append(" AND C.XPSXNAME LIKE '%" + bzlx + "%'");
		}*/
		sql.append(" ORDER BY A.ID DESC");
		//Long actDefId=ProcessAPI.getInstance().getProcessActDefId("");
		//ProcessAPI.getInstance().getProcessOpinionList(actDefId, instanceid);
		HashMap conditionMap=new HashMap();
		conditionMap.put("KHBH", khbh);
		boolean fbr=false;
		List<HashMap> list=DemAPI.getInstance().getList(CXDDFP_UUID, conditionMap, null);
		if(list.size()>0){
			if(list.get(0).get("GGFBR")!=null&&!list.get(0).get("GGFBR").equals("")){
				String ggfbr=list.get(0).get("GGFBR").toString().substring(0,list.get(0).get("GGFBR").toString().indexOf("["));
				if(ggfbr.equals(uc.get_userModel().getUserid())){
					fbr=true;
				}
			}
		}
		final boolean fbrzz=fbr;
		final String sql1 = sql.toString();
		final String DDLC_UUID=ProcessAPI.getInstance().getProcessActDefId("CXDDYFQLC");
		final String DMLC_UUID= ProcessAPI.getInstance().getProcessActDefId("GGSPLC");
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				Long orgroleid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid();
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal instanceid = (BigDecimal) object[0];
					String noticename = (String) object[1];
					String noticetype = (String) object[2];
					String noticedate = (String) object[3];
					String createname=(String)object[4];
					String  createdate= (String) object[5];
					BigDecimal id = (BigDecimal) object[6];
					String lcbh=(String)object[7];
					String  lcbs= (String) object[8];
					String  yxid= (String) object[9];
					String  rwid= (String) object[10];
					String stepid=(String) object[11];
					String noticetext=(String) object[12];
					String spzt=(String) object[13];
					String prcid=(String) object[14];
					String meetid=(String) object[15];
					String meetname=(String) object[16];
					String noticefile=(String) object[17];
					String ggdf=(String) object[18];
					String qcrid=(String) object[19];
					BigDecimal gzyjid=(BigDecimal) object[20];
					//String xpsxname=(String) object[21];
					String zqjcxs=(String) object[21];
					BigDecimal num=(BigDecimal) object[22];
					String noticeno=(String) object[23];
					String companyno=(String) object[24];
					
					map.put("INSTANCEID", instanceid.toString());
					map.put("NOTICENAME", noticename);
					map.put("NOTICETYPE", noticetype);
					map.put("NOTICEDATE", noticedate);
					map.put("CREATENAME", createname);
					map.put("CREATEDATE", noticedate);
					map.put("NOTICETEXT", noticetext);
					map.put("ID", id);
					map.put("LCBH", lcbh);
					map.put("LCBS", lcbs);
					map.put("YXID", yxid);
					map.put("RWID", rwid);
					map.put("STEPID", stepid);
					map.put("SPZT", spzt);
					map.put("PRCID", prcid);
					map.put("FBR", fbrzz);
					map.put("MEETID", meetid);
					map.put("MEETNAME", meetname);
					map.put("NOTICEFILE", noticefile);
					map.put("GGDF", ggdf);
					map.put("GZYJID", gzyjid);
					/*map.put("BZLX", xpsxname);*/
					map.put("ZQJCXS", zqjcxs);
					map.put("NUM", num==null?"":num.toString());
					map.put("NOTICENO", noticeno);
					map.put("COMPANYNO", companyno);
					
					if(noticename.matches(".*交易.*")){
						map.put("JIAOYI", 1);
					}else{
						map.put("JIAOYI", 0);
					}
					Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(yxid));
					if(newTaskId!=null){
						String assignee = newTaskId.getAssignee();
						if(qcrid!=null&&qcrid.equals(assignee)){
							map.put("ISBR", true);
						}else{
							map.put("ISBR", false);
						}
					}
					if(lcbs!=null&&!lcbs.equals("")){ 
						if(lcbh!=null){
							try{
						List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(lcbh, Long.parseLong(lcbs));
					    if(pro!=null&pro.size()>0){
					    	Long prc=pro.get(0).getPrcDefId();
					    	map.put("PRCID", prc);
					    }
							}catch(Exception e){
								logger.error(e,e);
							}
						}
					}
					if(map.get("STEPID")!=null){
						//任务列表
						List<Task> tasklist=ProcessAPI.getInstance().getUserProcessTaskList(DDLC_UUID, map.get("STEPID").toString(), userid);
						List<Task> dmtasklist=ProcessAPI.getInstance().getUserProcessTaskList(DMLC_UUID, map.get("STEPID").toString(), userid);
						List<Task> list =(tasklist==null?new ArrayList<Task>():tasklist);
						if(dmtasklist!=null&&dmtasklist.size()>0){
							list.addAll(dmtasklist);
						}
						  for(Task task:list){
							  if (Long.parseLong(task.getProcessInstanceId()) == Long.parseLong(map.get("LCBS").toString())
									  &&Long.parseLong(task.getExecutionId())==Long.parseLong(map.get("YXID").toString())) {
								String 	taskid = task.getId();
								map.put("RWID", taskid);
								break;
								}
						  }
						}else{
							map.put("RWID", "");
						}
					//
					if (map.get("SPZT") != null
							&& !map.get("SPZT").toString().equals("")
							&& !map.get("SPZT").toString().equals("驳回")) {
						map.put("ISBJ", false);
					} else {
						map.put("ISBJ", true);
					}
					if(map.get("SPZT")!=null&&!map.get("SPZT").equals("")&&map.get("SPZT").equals("审批通过")){
						map.put("ISTJ", true);
					}else{
						map.put("ISTJ", false);
					}
					HashMap smjmap=new HashMap();
					smjmap.put("GGINS", instanceid);
					List<HashMap> list=DemAPI.getInstance().getList(GGSMJ_UUID, smjmap, null);
					String smjins="";
					if(list.size()>0){
						smjins=list.get(0).get("INSTANCEID").toString();
						map.put("ISSMJINS", true);
					}else{
						map.put("ISSMJINS", false);
					}
					map.put("SMJINS", smjins);//扫描件instanceid
					map.put("orgroleid", orgroleid);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getList(String khbh,int pageSize, int pageNumber,
			String noticename,String noticetype, String startdate, String enddate,String bzlx,String spzt,String zqdm,String zqjc,String dq,String companyno) {
		try {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date Date1 = null;
		java.util.Date Date2 = null;
		int ct1 = 0;
		if(startdate!=null&&!startdate.equals(""))
			Date1 = format.parse(startdate);
		if(enddate!=null&&!enddate.equals(""))
			Date2 = format.parse(enddate);
		if(Date1!=null&&Date2!=null)
			ct1 = Date1.compareTo(Date2);
	    if(ct1>0){
	    	return null;
	    }
		} catch (ParseException e1) {
			return null;
		}
		List l=new ArrayList();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		/*if("".equals(khbh)||khbh==null){
			return l;
		}*/
		final String userid = uc.get_userModel().getUserid();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT INSTANCEID,NOTICENAME,NOTICETYPE,TO_CHAR(NOTICEDATE,'YYYY-MM-DD') NOTICEDATE," +
				"CREATENAME,TO_CHAR(A.CREATEDATE,'yyyy-MM-dd') CREATEDATE,A.ID,LCBH,LCBS,YXID,RWID,STEPID,NOTICETEXT,SPZT,PRCID,MEETID,MEETNAME,NOTICEFILE,GGDF,QCRID,B.ID BID,"+ 
				"A.ZQJCXS," +
				"ROUND(LENGTH(REPLACE(SMJ,',',''))/32) NUM,NOTICENO," +
				"A.COMPANYNO,O.NUM AS ONUM,O.GGNAME " +
				"FROM BD_MEET_QTGGZL A LEFT JOIN (SELECT COUNT(*) AS NUM,PLRQ,GSDM,LISTAGG(GGNAME,'\n') WITHIN GROUP (ORDER BY GGNAME) AS GGNAME FROM YPLGG GROUP BY PLRQ,GSDM) O ON A.NOTICEDATE=O.PLRQ AND A.ZQDMXS=O.GSDM LEFT JOIN BD_XP_GZXTYJB B ON A.ID=B.GGID "+ 				
				"LEFT JOIN BD_XP_GGSMJ SM ON A.ID=SM.GGID");
		 //XLJ 2017年5月24日11:34:45新增开始
		 if (dq != null && !"".equals(dq)) {
			 sql.append(" INNER JOIN (SELECT CUSTOMERNO,ZCQX FROM BD_ZQB_KH_BASE WHERE ZCQX LIKE ?) KH ON KH.CUSTOMERNO=A.KHBH");
			 params.add("%" + dq + "%");
		}
			sql.append(" WHERE 1=1 ");
		 //XLJ 2017年5月24日11:34:49新增截止
		if(khbh!=null&&!khbh.equals("")){
			sql.append(" AND A.KHBH=?");
			params.add(khbh);
		}else{
			OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
			Long orgroleid = user.getOrgroleid();
			if(orgroleid==3l){
				khbh = user.getExtend1();
				sql.append(" AND A.KHBH=?");
				params.add(khbh);
			}else if(orgroleid==5l){
			}else{
				String currentuserid = user.getUserid();
				sql.append(" AND A.KHBH IN (SELECT khbh FROM BD_MDM_KHQXGLB WHERE 1=1 AND(SUBSTR(KHFZR,0, instr(KHFZR,'[',1)-1) =? OR SUBSTR(ZZCXDD,0, instr(ZZCXDD,'[',1)-1) =? OR SUBSTR(FHSPR,0, instr(FHSPR,'[',1)-1) =? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) =? OR SUBSTR(CWSCBFZR2,0, instr(CWSCBFZR2,'[',1)-1) =? OR SUBSTR(CWSCBFZR3,0, instr(CWSCBFZR3,'[',1)-1) =? OR SUBSTR(qynbrysh, instr(qynbrysh,'[',1)-1) =?))");
				params.add(currentuserid);
				params.add(currentuserid);
				params.add(currentuserid);
				params.add(currentuserid);
				params.add(currentuserid);
				params.add(currentuserid);
				params.add(currentuserid);
			}
		}
		if (zqdm != null && !"".equals(zqdm)) {
			sql.append(" AND A.ZQDMXS LIKE ?");
			params.add("%" + zqdm + "%");
		}
		if (zqjc != null && !"".equals(zqjc)) {
			sql.append(" AND A.ZQJCXS LIKE ?");
			params.add("%" + zqjc + "%");
		}
		if (noticename != null && !"".equals(noticename)) {
			sql.append(" AND A.NOTICENAME LIKE ?");
			params.add("%" + noticename + "%");
		}
		if (startdate != null && !"".equals(startdate)) {
			sql.append(" AND TO_CHAR(A.NOTICEDATE,'yyyy-MM-dd')>= ?");
			params.add(startdate);
		}
		if (noticetype != null && !"".equals(noticetype)) {
			sql.append(" AND A.NOTICETYPE = ?");
			params.add(noticetype);
		}
		if (enddate != null && !"".equals(enddate)) {
			sql.append(" AND TO_CHAR(A.NOTICEDATE,'yyyy-MM-dd')<= ?");
			params.add(enddate);
		}
		if(spzt!=null&&!spzt.equals("")){
			sql.append(" AND A.SPZT=?");
			params.add(spzt);
		}
		if (companyno != null && !"".equals(companyno)) {
			sql.append(" AND A.COMPANYNO = ?");
			params.add(companyno);
		}
		/*if(bzlx!=null&&!bzlx.equals("")){
			sql.append(" AND C.XPSXNAME LIKE '%" + bzlx + "%'");
		}*/
		sql.append(" ORDER BY A.ID DESC");
		//Long actDefId=ProcessAPI.getInstance().getProcessActDefId("");
		//ProcessAPI.getInstance().getProcessOpinionList(actDefId, instanceid);
		HashMap conditionMap=new HashMap();
		conditionMap.put("KHBH", khbh);
		boolean fbr=false;
		List<HashMap> list=DemAPI.getInstance().getList(CXDDFP_UUID, conditionMap, null);
		if(list.size()>0){
			if(list.get(0).get("GGFBR")!=null&&!list.get(0).get("GGFBR").equals("")){
				String ggfbr=list.get(0).get("GGFBR").toString().substring(0,list.get(0).get("GGFBR").toString().indexOf("["));
				if(ggfbr.equals(uc.get_userModel().getUserid())){
					fbr=true;
				}
			}
		}
		final boolean fbrzz=fbr;
		final String sql1 = sql.toString();
		final String DDLC_UUID=ProcessAPI.getInstance().getProcessActDefId("CXDDYFQLC");
		final String DMLC_UUID= ProcessAPI.getInstance().getProcessActDefId("GGSPLC");
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				Long orgroleid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid();
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal instanceid = (BigDecimal) object[0];
					String noticename = (String) object[1];
					String noticetype = (String) object[2];
					String noticedate = (String) object[3];
					String createname=(String)object[4];
					String  createdate= (String) object[5];
					BigDecimal id = (BigDecimal) object[6];
					String lcbh=(String)object[7];
					String  lcbs= (String) object[8];
					String  yxid= (String) object[9];
					String  rwid= (String) object[10];
					String stepid=(String) object[11];
					String noticetext=(String) object[12];
					String spzt=(String) object[13];
					String prcid=(String) object[14];
					String meetid=(String) object[15];
					String meetname=(String) object[16];
					String noticefile=(String) object[17];
					String ggdf=(String) object[18];
					String qcrid=(String) object[19];
					BigDecimal gzyjid=(BigDecimal) object[20];
					//String xpsxname=(String) object[21];
					String zqjcxs=(String) object[21];
					BigDecimal num=(BigDecimal) object[22];
					String noticeno=(String) object[23];
					String companyno=(String) object[24];
					BigDecimal oNum=(BigDecimal) object[25];
					String ggname=(String) object[26];
					
					map.put("INSTANCEID", instanceid.toString());
					map.put("NOTICENAME", noticename);
					map.put("NOTICETYPE", noticetype);
					map.put("NOTICEDATE", noticedate);
					map.put("CREATENAME", createname);
					map.put("CREATEDATE", noticedate);
					map.put("NOTICETEXT", noticetext);
					map.put("ID", id);
					map.put("LCBH", lcbh);
					map.put("LCBS", lcbs);
					map.put("YXID", yxid);
					map.put("RWID", rwid);
					map.put("STEPID", stepid);
					map.put("SPZT", spzt);
					map.put("PRCID", prcid);
					map.put("FBR", fbrzz);
					map.put("MEETID", meetid);
					map.put("MEETNAME", meetname);
					map.put("NOTICEFILE", noticefile);
					map.put("GGDF", ggdf);
					map.put("GZYJID", gzyjid);
					/*map.put("BZLX", xpsxname);*/
					map.put("ZQJCXS", zqjcxs);
					map.put("NUM", num==null?"":num.toString());
					map.put("NOTICENO", noticeno);
					map.put("COMPANYNO", companyno);
					map.put("ONUM", oNum==null?"":oNum.toString());
					map.put("GGNAME", ggname);
					
					if(noticename.matches(".*交易.*")){
						map.put("JIAOYI", 1);
					}else{
						map.put("JIAOYI", 0);
					}
					Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(yxid));
					if(newTaskId!=null){
						String assignee = newTaskId.getAssignee();
						if(qcrid!=null&&qcrid.equals(assignee)){
							map.put("ISBR", true);
						}else{
							map.put("ISBR", false);
						}
					}
					if(lcbs!=null&&!lcbs.equals("")){ 
						if(lcbh!=null){
							try{
						List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(lcbh, Long.parseLong(lcbs));
					    if(pro!=null&pro.size()>0){
					    	Long prc=pro.get(0).getPrcDefId();
					    	map.put("PRCID", prc);
					    }
							}catch(Exception e){
								logger.error(e,e);
							}
						}
					}
					if(map.get("STEPID")!=null){
						//任务列表
						List<Task> tasklist=ProcessAPI.getInstance().getUserProcessTaskList(DDLC_UUID, map.get("STEPID").toString(), userid);
						List<Task> dmtasklist=ProcessAPI.getInstance().getUserProcessTaskList(DMLC_UUID, map.get("STEPID").toString(), userid);
						List<Task> list =(tasklist==null?new ArrayList<Task>():tasklist);
						if(dmtasklist!=null&&dmtasklist.size()>0){
							list.addAll(dmtasklist);
						}
						  for(Task task:list){
							  if (Long.parseLong(task.getProcessInstanceId()) == Long.parseLong(map.get("LCBS").toString())
									  &&Long.parseLong(task.getExecutionId())==Long.parseLong(map.get("YXID").toString())) {
								String 	taskid = task.getId();
								map.put("RWID", taskid);
								break;
								}
						  }
						}else{
							map.put("RWID", "");
						}
					//
					if (map.get("SPZT") != null
							&& !map.get("SPZT").toString().equals("")
							&& !map.get("SPZT").toString().equals("驳回")) {
						map.put("ISBJ", false);
					} else {
						map.put("ISBJ", true);
					}
					if(map.get("SPZT")!=null&&!map.get("SPZT").equals("")&&map.get("SPZT").equals("审批通过")){
						map.put("ISTJ", true);
					}else{
						map.put("ISTJ", false);
					}
					HashMap smjmap=new HashMap();
					smjmap.put("GGINS", instanceid);
					List<HashMap> list=DemAPI.getInstance().getList(GGSMJ_UUID, smjmap, null);
					String smjins="";
					if(list.size()>0){
						smjins=list.get(0).get("INSTANCEID").toString();
						map.put("ISSMJINS", true);
					}else{
						map.put("ISSMJINS", false);
					}
					map.put("SMJINS", smjins);//扫描件instanceid
					map.put("orgroleid", orgroleid);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getCompanyEnterprisesList(int pageSize, int pageNumber, 
			String fullname, String model, String orderby, String starttime, String endtime) {
		if(starttime!=null && endtime!=null && !"".equals(starttime) && !"".equals(endtime)){
			try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date Date1 = format.parse(starttime);
				java.util.Date Date2 = format.parse(endtime);
				int ct=Date1.compareTo(Date2);
				if(ct>0){
					return null;
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
		}
		StringBuffer sb = new StringBuffer();
		List params = new ArrayList();
		String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
		sb.append("select FULLNAME,DEPARTMENTNAME,TO_CHAR(WM_CONCAT(MODEL)),SUM(SUMFS) SUMFS from (SELECT FULLNAME,DEPARTMENTNAME,"
				+ "CAST(MODEL AS VARCHAR(64)) MODEL,SUM(FS) SUMFS FROM (SELECT * FROM (SELECT A.BKFRID||'['||O.USERNAME||']' AS FULLNAME,"
				+ "O.DEPARTMENTNAME,A.FS,  decode(S.ENTITYTITLE,'其他公告资料流程','公告','日常业务呈报',S.ENTITYTITLE,'项目') AS MODEL,"
				+ "A.KFSJ FROM BD_XP_KFRZJLB A LEFT JOIN ORGUSER O ON A.BKFRID=O.USERID"
				+ " LEFT JOIN SYS_ENGINE_METADATA S ON A.TABLENAME=S.ENTITYNAME WHERE O.ORGROLEID<>3) WHERE 1=1"); 
		if(fullname!=null&&!fullname.equals("")){
			sb.append(" AND FULLNAME LIKE ?"); 
			params.add("%"+fullname+"%");
		}
		if(model!=null&&!model.equals("")){
			sb.append(" AND MODEL=?");
			params.add(model);
		}else{
			if(!configParameter.equals("sxzq") /*&& !configParameter.equals("hlzq")*/){
				sb.append(" AND (MODEL='日常业务呈报' or MODEL='公告') ");
			}
		}
		if(starttime!=null&&!starttime.equals("")){
			sb.append(" AND TO_DATE(?,'yyyy-MM-dd HH24:mi:ss') <= TO_DATE(KFSJ,'yyyy-MM-dd HH24:mi:ss')");
			params.add(starttime);
		}
		if(endtime!=null&&!endtime.equals("")){
			sb.append(" AND TO_DATE(KFSJ,'yyyy-MM-dd HH24:mi:ss') <= TO_DATE(?,'yyyy-MM-dd HH24:mi:ss')");
			params.add(endtime);
		}
		
		sb.append(") GROUP BY FULLNAME,DEPARTMENTNAME,MODEL"); 
		if(orderby!=null && orderby.equals("SUMFS") ){
			sb.append(" ORDER BY SUMFS desc");
		}else if(orderby!=null && orderby.equals("FULLNAME")){
			sb.append(" ORDER BY FULLNAME");
		}else{
			sb.append(" ORDER BY DEPARTMENTNAME");
		}
		sb.append("  ) group by FULLNAME,DEPARTMENTNAME ");
		
		if(orderby!=null && orderby.equals("SUMFS") ){
			sb.append(" ORDER BY SUMFS desc");
		}else if(orderby!=null && orderby.equals("FULLNAME")){
			sb.append(" ORDER BY FULLNAME");
		}else{
			sb.append(" ORDER BY DEPARTMENTNAME");
		}
		final String sql1 = sb.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String FULLNAME = (String) object[0];
					String DEPARTMENTNAME = (String) object[1];
					String MODEL = (String)object[2];
					BigDecimal SUMFS = (BigDecimal) object[3];
					map.put("FULLNAME", FULLNAME);
					map.put("DEPARTMENTNAME", DEPARTMENTNAME);
					map.put("MODEL", MODEL);
					map.put("SUMFS", SUMFS);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getCompanyEnterprisesSize(String fullname, String model, String orderby,
		String starttime, String endtime) {
		if(starttime!=null && endtime!=null && !"".equals(starttime) && !"".equals(endtime)){
			try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date Date1 = format.parse(starttime);
				java.util.Date Date2 = format.parse(endtime);
				int ct=Date1.compareTo(Date2);
				if(ct>0){
					return new ArrayList();
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return new ArrayList();
			}
		}
			StringBuffer sb = new StringBuffer();
			List params = new ArrayList();
			String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
			sb.append("select FULLNAME,DEPARTMENTNAME,to_char(WM_CONCAT(TO_CHAR(MODEL))),SUM(SUMFS) from "
					+ "(SELECT FULLNAME,DEPARTMENTNAME,CAST(MODEL AS VARCHAR(64)) MODEL,SUM(FS) SUMFS "
					+ "FROM (SELECT * FROM (SELECT A.BKFRID||'['||O.USERNAME||']' AS FULLNAME,O.DEPARTMENTNAME,A.FS,"
					+ "decode(S.ENTITYTITLE,'其他公告资料流程','公告','日常业务呈报',S.ENTITYTITLE,'项目') AS MODEL,A.KFSJ FROM BD_XP_KFRZJLB A"
					+ " LEFT JOIN ORGUSER O ON A.BKFRID=O.USERID LEFT JOIN SYS_ENGINE_METADATA S ON A.TABLENAME=S.ENTITYNAME WHERE O.ORGROLEID<>3) WHERE 1=1"); 
			if(fullname!=null&&!fullname.equals("")){
				sb.append(" AND FULLNAME LIKE ?"); 
				params.add("%"+fullname+"%");
			}
			if(model!=null&&!model.equals("")){
				sb.append(" AND MODEL=?");
				params.add(model);
			}else{
				if(!configParameter.equals("sxzq") /*&& !configParameter.equals("hlzq")*/){
					sb.append(" AND (MODEL='日常业务呈报' or MODEL='公告') ");
				}
			}
			if(starttime!=null&&!starttime.equals("")){
				sb.append(" AND TO_DATE(?,'yyyy-MM-dd HH24:mi:ss') <= TO_DATE(KFSJ,'yyyy-MM-dd HH24:mi:ss')");
				params.add(starttime);
			}
			if(endtime!=null&&!endtime.equals("")){
				sb.append(" AND TO_DATE(KFSJ,'yyyy-MM-dd HH24:mi:ss') <= TO_DATE(?,'yyyy-MM-dd HH24:mi:ss')");
				params.add(endtime);
			}
			
			sb.append(") GROUP BY FULLNAME,DEPARTMENTNAME,MODEL"); 
			if(orderby!=null&&!orderby.equals("")){
				sb.append(" ORDER BY ?");
				params.add(orderby);
			}else{
				sb.append(" ORDER BY DEPARTMENTNAME");
			}
			sb.append("  ) group by FULLNAME,DEPARTMENTNAME ");
			final String sql1 = sb.toString();
			final List param = params;
			return this.getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
					Query query = session.createSQLQuery(sql1);
					List<HashMap> l = new ArrayList<HashMap>();
					DBUtilInjection d=new DBUtilInjection();
					for (int i = 0; i < param.size(); i++) {
						if(param.get(i)!=null && !"".equals(param.get(i).toString())){
							String params=param.get(i).toString().replace("%", "").trim();
							if(d.HasInjectionData(params)){
								return l;
							}
							}
						query.setParameter(i, param.get(i));
					}
					List<Object[]> list = query.list();
					HashMap map;
					int n = 0;
					for (Object[] object : list) {
						map = new HashMap();
						String FULLNAME = (String) object[0];
						String DEPARTMENTNAME = (String) object[1];
						String MODEL = (String)object[2];
						BigDecimal SUMFS = (BigDecimal) object[3];
						map.put("FULLNAME", FULLNAME);
						map.put("DEPARTMENTNAME", DEPARTMENTNAME);
						map.put("MODEL", MODEL);
						map.put("SUMFS", SUMFS);
						l.add(map);
					}
					return l;
				}
			});
		}
	
	public List<HashMap> submsgEnterprisesList(int pageSize, int pageNumber, 
			String fullname, String model, String orderby, String starttime, String endtime, String departmentname) {
		StringBuffer sb = new StringBuffer();
		List params = new ArrayList();
		sb.append("SELECT TAG,TABLENAME,ACTDEFID,FULLNAME,DEPARTMENTNAME,SUMFS,CAST(MODEL AS VARCHAR(64)) MODEL,KFSJ,TASKID "
				+ "FROM (SELECT A.TAG,A.TABLENAME,A.ACTDEFID,A.BKFRID||'['||O.USERNAME||']' AS FULLNAME,O.DEPARTMENTNAME,A.FS AS SUMFS, "
				+ "decode(S.ENTITYTITLE,'其他公告资料流程','公告','日常业务呈报',S.ENTITYTITLE,'项目') AS MODEL,A.KFSJ,A.TASKID "
				+ "FROM BD_XP_KFRZJLB A LEFT JOIN ORGUSER O ON A.BKFRID=O.USERID   "
				+ "LEFT JOIN SYS_ENGINE_METADATA S ON A.TABLENAME = S.ENTITYNAME WHERE O.ORGROLEID<>3) WHERE 1=1");
		String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
		if(fullname!=null&&!fullname.equals("")){
			sb.append(" AND FULLNAME LIKE ?");
			params.add("%"+fullname+"%");
		} 
		if(model!=null&&!model.equals("")){
			sb.append(" AND MODEL=?");
			params.add(model);
		}else{
			if(!configParameter.equals("sxzq") /*&& !configParameter.equals("hlzq")*/){
				sb.append(" AND (MODEL='日常业务呈报' or MODEL='公告') ");
			}
		}
		if(starttime!=null&&!starttime.equals("")){
			sb.append(" AND TO_DATE(?,'yyyy-MM-dd HH24:mi:ss') <= TO_DATE(KFSJ,'yyyy-MM-dd HH24:mi:ss')");
			params.add(starttime);
		} 
		if(endtime!=null&&!endtime.equals("")){
			sb.append(" AND TO_DATE(KFSJ,'yyyy-MM-dd HH24:mi:ss') <= TO_DATE(?,'yyyy-MM-dd HH24:mi:ss')");
			params.add(endtime);
		}
		if(departmentname!=null&&!departmentname.equals("")){
			sb.append(" AND DEPARTMENTNAME=?");
			params.add(departmentname);
		}
		if(orderby!=null&&!orderby.equals("")){
			sb.append(" ORDER BY ?");
			params.add(orderby);
		}else{
			sb.append(" ORDER BY DEPARTMENTNAME");
		}

		final String sql1 = sb.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal TAG = (BigDecimal) object[0];
					String TABLENAME = (String) object[1];
					String ACTDEFID = (String)object[2];
					String FULLNAME = (String)object[3];
					String DEPARTMENTNAME = (String)object[4];
					BigDecimal SUMFS = (BigDecimal) object[5];
					String MODEL = (String)object[6];
					String KFSJ = (String)object[7];
					BigDecimal taskid = (BigDecimal)object[8];
					
					String actDefId = ACTDEFID;
					String entityName = TABLENAME;
					Long dataid = TAG.longValue();
					Long instanceId = ProcessAPI.getInstance().getInstaceId(entityName, dataid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					String URL ="loadProcessFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+instanceId+"&taskId="+taskid;
					
					HashMap formdata = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					String ITEMNAME = "-空-";
					
					if(TABLENAME.equals("BD_XP_QTGGZLLC")){
						ITEMNAME = formdata.get("NOTICENAME").toString();
					}
					else if(TABLENAME.equals("BD_ZQB_XMLXLCB")){
						ITEMNAME = formdata.get("PROJECTNAME").toString();
					}else if(TABLENAME.equals("BD_ZQB_LCGG")){
						ITEMNAME = formdata.get("GSMC").toString();
					}else if(TABLENAME.equals("BD_ZQB_SQNH")){
						ITEMNAME = formdata.get("CUSTOMERNAME").toString();
					}else if(TABLENAME.equals("BD_ZQB_NHFK")){
						ITEMNAME = formdata.get("CUSTOMERNAME").toString();
					}else if(TABLENAME.equals("BD_ZQB_GZFKJHF")){
						ITEMNAME = formdata.get("CUSTOMERNAME").toString();
					}else if(TABLENAME.equals("BD_ZQB_GPDJJGD")){
						ITEMNAME = formdata.get("GSMC").toString();
					}
					else if(TABLENAME.equals("BD_ZQB_GPFXXMNFXLCB")||TABLENAME.equals("BD_ZQB_GPFXXMFAZLBSLC")||TABLENAME.equals("BD_ZQB_GPFXXMSBZLLCB")||TABLENAME.equals("BD_ZQB_GPFXXMSJFXLCB")){
						ITEMNAME = formdata.get("PROJECTNAME").toString();
					}
					else if(TABLENAME.equals("BD_ZQB_BGXMLX")||TABLENAME.equals("BD_ZQB_BGFAZLBS")||TABLENAME.equals("BD_ZQB_BGSBZL")||TABLENAME.equals("BD_ZQB_BGZLGD")){
						ITEMNAME = DBUtil.getString("SELECT JYF FROM BD_ZQB_BGZZLXXX WHERE XMBH='"+formdata.get("XMBH").toString()+"'", "JYF");
					}
					else if(TABLENAME.equals("BD_ZQB_CWXMLX")||TABLENAME.equals("BD_ZQB_CWGZJDHB")||TABLENAME.equals("BD_ZQB_CWZLGD")){
						ITEMNAME = DBUtil.getString("SELECT CUSTOMERNAME FROM BD_ZQB_CWGWXMXX WHERE XMBH='"+formdata.get("XMBH").toString()+"'", "CUSTOMERNAME");
					}else if(TABLENAME.equals("RCYWCB")){
						ITEMNAME = formdata.get("SXMC").toString();
					}
					
					map.put("TAG", TAG);
					map.put("TABLENAME", TABLENAME);
					map.put("ACTDEFID", ACTDEFID);
					map.put("FULLNAME", FULLNAME);
					map.put("DEPARTMENTNAME", DEPARTMENTNAME);
					map.put("SUMFS", SUMFS);
					map.put("MODEL", MODEL);
					map.put("KFSJ", KFSJ);
					map.put("URL", URL);
					map.put("ITEMNAME", ITEMNAME);
					l.add(map);
				}
				
				return l;
			}
		});
	}
	
	public List<HashMap> submsgEnterprisesSize(String fullname, String model, String orderby,
		String starttime, String endtime, String departmentname) {
			String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
			StringBuffer sb = new StringBuffer();
			List params = new ArrayList();
			sb.append("SELECT TAG,TABLENAME,ACTDEFID,FULLNAME,DEPARTMENTNAME,SUMFS,CAST(MODEL AS VARCHAR(64)) MODEL,KFSJ,TASKID FROM"); 
			sb.append(" (");
			sb.append("SELECT A.TAG,A.TABLENAME,A.ACTDEFID,A.BKFRID||'['||O.USERNAME||']' AS FULLNAME,O.DEPARTMENTNAME,");
			sb.append("A.FS AS SUMFS,   decode(S.ENTITYTITLE,'其他公告资料流程','公告','日常业务呈报',S.ENTITYTITLE,'项目') AS MODEL,A.KFSJ,A.TASKID"); 
			sb.append(" FROM BD_XP_KFRZJLB A LEFT JOIN ORGUSER O ON A.BKFRID=O.USERID   LEFT JOIN SYS_ENGINE_METADATA S ON A.TABLENAME = S.ENTITYNAME WHERE O.ORGROLEID<>3"); 
			sb.append(")");
			sb.append(" WHERE 1=1");

			if(fullname!=null&&!fullname.equals("")){
				sb.append(" AND FULLNAME LIKE ?");
				params.add("%"+fullname+"%");
			} 
			if(model!=null&&!model.equals("")){
				sb.append(" AND MODEL=?");
				params.add(model);
			}else{
				if(!configParameter.equals("sxzq") /*&& !configParameter.equals("hlzq")*/){
					sb.append(" AND (MODEL='日常业务呈报' or MODEL='公告') ");
				}
			}
			if(starttime!=null&&!starttime.equals("")){
				sb.append(" AND TO_DATE(?,'yyyy-MM-dd HH24:mi:ss') <= TO_DATE(KFSJ,'yyyy-MM-dd HH24:mi:ss')");
				params.add(starttime);
			} 
			if(endtime!=null&&!endtime.equals("")){
				sb.append(" AND TO_DATE(KFSJ,'yyyy-MM-dd HH24:mi:ss') <= TO_DATE(?,'yyyy-MM-dd HH24:mi:ss')");
				params.add(endtime);
			}
			if(departmentname!=null&&!departmentname.equals("")){
				sb.append(" AND DEPARTMENTNAME=?");
				params.add(departmentname);
			}
			if(orderby!=null&&!orderby.equals("")){
				sb.append(" ORDER BY ?");
				params.add(orderby);
			}else{
				sb.append(" ORDER BY DEPARTMENTNAME");
			}

			final String sql1 = sb.toString();
			final List param = params;
			return this.getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
					Query query = session.createSQLQuery(sql1);
					DBUtilInjection d=new DBUtilInjection();
					List<HashMap> l = new ArrayList<HashMap>();
					for (int i = 0; i < param.size(); i++) {
						if(param.get(i)!=null && !"".equals(param.get(i).toString())){
							String params=param.get(i).toString().replace("%", "").trim();
							if(d.HasInjectionData(params)){
								return l;
							}
							}
						query.setParameter(i, param.get(i));
					}
					List<Object[]> list = query.list();
					HashMap map;
					int n = 0;
					for (Object[] object : list) {
						map = new HashMap();
						BigDecimal TAG = (BigDecimal) object[0];
						String TABLENAME = (String) object[1];
						String ACTDEFID = (String)object[2];
						String FULLNAME = (String)object[3];
						String DEPARTMENTNAME = (String)object[4];
						BigDecimal SUMFS = (BigDecimal) object[5];
						String MODEL = (String)object[6];
						String KFSJ = (String)object[7];
						BigDecimal taskid = (BigDecimal)object[8];
						
						String actDefId = ACTDEFID;
						String entityName = TABLENAME;
						Long dataid = TAG.longValue();
						Long instanceId = ProcessAPI.getInstance().getInstaceId(entityName, dataid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
						String URL ="loadProcessFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+instanceId+"&taskId="+taskid;
						
						HashMap formdata = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
						String ITEMNAME = "-空-";
						if(TABLENAME.equals("BD_XP_QTGGZLLC")){
							ITEMNAME = formdata.get("NOTICENAME").toString();
						}
						else if(TABLENAME.equals("BD_ZQB_XMLXLCB")){
							ITEMNAME = formdata.get("PROJECTNAME").toString();
						}else if(TABLENAME.equals("BD_ZQB_LCGG")){
							ITEMNAME = formdata.get("GSMC").toString();
						}else if(TABLENAME.equals("BD_ZQB_SQNH")){
							ITEMNAME = formdata.get("CUSTOMERNAME").toString();
						}else if(TABLENAME.equals("BD_ZQB_NHFK")){
							ITEMNAME = formdata.get("CUSTOMERNAME").toString();
						}else if(TABLENAME.equals("BD_ZQB_GZFKJHF")){
							ITEMNAME = formdata.get("CUSTOMERNAME").toString();
						}else if(TABLENAME.equals("BD_ZQB_GPDJJGD")){
							ITEMNAME = formdata.get("GSMC").toString();
						}
						else if(TABLENAME.equals("BD_ZQB_GPFXXMNFXLCB")||TABLENAME.equals("BD_ZQB_GPFXXMFAZLBSLC")||TABLENAME.equals("BD_ZQB_GPFXXMSBZLLCB")||TABLENAME.equals("BD_ZQB_GPFXXMSJFXLCB")){
							ITEMNAME = formdata.get("PROJECTNAME").toString();
						}
						else if(TABLENAME.equals("BD_ZQB_BGXMLX")||TABLENAME.equals("BD_ZQB_BGFAZLBS")||TABLENAME.equals("BD_ZQB_BGSBZL")||TABLENAME.equals("BD_ZQB_BGZLGD")){
							ITEMNAME = DBUtil.getString("SELECT JYF FROM BD_ZQB_BGZZLXXX WHERE XMBH='"+formdata.get("XMBH").toString()+"'", "JYF");
						}
						else if(TABLENAME.equals("BD_ZQB_CWXMLX")||TABLENAME.equals("BD_ZQB_CWGZJDHB")||TABLENAME.equals("BD_ZQB_CWZLGD")){
							ITEMNAME = DBUtil.getString("SELECT CUSTOMERNAME FROM BD_ZQB_CWGWXMXX WHERE XMBH='"+formdata.get("XMBH").toString()+"'", "CUSTOMERNAME");
						}
						
						map.put("TAG", TAG);
						map.put("TABLENAME", TABLENAME);
						map.put("ACTDEFID", ACTDEFID);
						map.put("FULLNAME", FULLNAME);
						map.put("DEPARTMENTNAME", DEPARTMENTNAME);
						map.put("SUMFS", SUMFS);
						map.put("MODEL", MODEL);
						map.put("KFSJ", KFSJ);
						map.put("URL", URL);
						map.put("ITEMNAME", ITEMNAME);
						l.add(map);
					}
					return l;
				}
			});
		}
	
	public List<HashMap> AnnouncementUsermulti(String khbh) {
		StringBuffer sb = new StringBuffer(" select 99999999 id,username || '[' || userid || ']' departmentname,departmentid parentdepartmentid,userid from orguser where orgroleid!=3 or extend1=? union select id,departmentname,parentdepartmentid,'' userid from orgdepartment where parentdepartmentid != 51 or departmentno=?");
		final String sql1 = sb.toString();
		final String final_khbh = khbh;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setParameter(0, final_khbh);
				query.setParameter(1, final_khbh);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String name = (String) object[1];
					BigDecimal pId = (BigDecimal) object[2];
					String userid = (String) object[3];
					map.put("id", id);
					map.put("pId", pId);
					map.put("name", name);
					map.put("userid", userid);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> announcementDxyjtzr(String gzyjid){
		StringBuffer sb = new StringBuffer("SELECT ID,GZYJLRR,TO_CHAR(GZYJLRSJ,'yyyy-MM-dd HH:mm') GZYJLRSJ,GZYJJSM,GZYJSMWD,DXYJTZR,GGID,KHBH,GZYJLRRID FROM BD_XP_GZXTYJB WHERE ID=?");
		final String sql1 = sb.toString();
		
			
		final String final_gzyjid = gzyjid;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setParameter(0, final_gzyjid);
				List<Map> l = new ArrayList<Map>();
				DBUtilInjection d=new DBUtilInjection();
				
				if(final_gzyjid!=null && !"".equals(final_gzyjid)){
					
					if(d.HasInjectionData(final_gzyjid)){
						return l;
					}
					}
				List<Object[]> li = query.list();
				HashMap map;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];//---
					String gzyjlrr = (String) object[1];//---
					String gzyjlrsj = (String) object[2];//---
					String gzyjjsm = (String) object[3];//---
					
					String gzyjsmwd=(String)object[4];
					StringBuffer gzyjsmwdsb=new StringBuffer();//---
					if(gzyjsmwd!=null&&!"".equals(gzyjsmwd)){
						List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(gzyjsmwd);
						for (FileUpload fileUpload : sublist) {
							String xgzl2=fileUpload.getFileSrcName();
							String uuid2=fileUpload.getFileId();
							try {
								gzyjsmwdsb.append("<div style=\"background-color: #F5F5F5;float:left;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\" id=\""+uuid2+"\">	<div style=\"align:right;float: right;\"></div>	<span> <a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+uuid2+"\"> <img src=\"/iwork_img/attach.png\">"+substring(xgzl2,20,"UTF-8")+(xgzl2.length() > 20 ? "..." : "")+"</a></span></div>");
							} catch (UnsupportedEncodingException e) {
								logger.error(e,e);
							}
						}
					}
					String dxyjtzr=(String)object[5];//---
					StringBuffer dxyjtzrsb=new StringBuffer();//---
					String name;
					if(dxyjtzr!=null&&!"".equals(dxyjtzr)){
						String[] dxyjtzrarray = dxyjtzr.split(",");
						for (int i = 0; i < dxyjtzrarray.length; i++) {
							name = UserContextUtil.getInstance().getUserContext(dxyjtzrarray[i]).get_userModel().getUsername()+"["+dxyjtzrarray[i]+"]";
							dxyjtzrsb.append("<div id='"+dxyjtzrarray[i]+"'><span class='selItem'>"+name+"</span></div>");
						}
					}
					
					String ggid=(String)object[6];//---
					String khbh=(String)object[7];//---
					String gzyjlrrid=(String)object[8];//---
					map.put("ID", id);
					map.put("GZYJLRR", gzyjlrr);
					map.put("GZYJLRSJ", gzyjlrsj);
					map.put("GZYJJSM", gzyjjsm);
					map.put("GZYJSMWD", gzyjsmwdsb.toString());
					map.put("DXYJTZR", dxyjtzr);
					map.put("DXYJTZRSB", dxyjtzrsb.toString());
					map.put("GGID", ggid);
					map.put("KHBH", khbh);
					map.put("GZYJLRRID", gzyjlrrid);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getGzxtyjhfList(String gzyjid) {
		StringBuffer sb = new StringBuffer("SELECT ID,GZYJHFR,TO_CHAR(GZYJHFSJ,'yyyy-MM-dd HH24:mi') GZYJHFSJ,GZYJHFXX,GZYJHFZL,DXYJTZR,GZYJID,KHBH,GZYJHFRID FROM BD_XP_GZXTYJHFB WHERE GZYJID=? ORDER BY GZYJHFSJ");
		final String sql1 = sb.toString();
		final String final_gzyjid = gzyjid;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setParameter(0, final_gzyjid);
				List<Map> l = new ArrayList<Map>();
				DBUtilInjection d=new DBUtilInjection();
				
				if(final_gzyjid!=null && !"".equals(final_gzyjid)){
					
					if(d.HasInjectionData(final_gzyjid)){
						return l;
					}
					}
				List<Object[]> li = query.list();
				HashMap map;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];//---
					String gzyjlrr = (String) object[1];//---
					String gzyjlrsj = (String) object[2];//---
					String gzyjjsm = (String) object[3];//---
					
					String gzyjsmwd=(String)object[4];
					StringBuffer gzyjsmwdsb=new StringBuffer();//---
					if(gzyjsmwd!=null&&!"".equals(gzyjsmwd)){
						List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(gzyjsmwd);
						for (FileUpload fileUpload : sublist) {
							String xgzl2=fileUpload.getFileSrcName();
							String uuid2=fileUpload.getFileId();
							try {
								gzyjsmwdsb.append("<div style=\"background-color: #F5F5F5;float:left;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\" id=\""+uuid2+"\">	<div style=\"align:right;float: right;\"></div>	<span> <a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+uuid2+"\"> <img src=\"/iwork_img/attach.png\">"+substring(xgzl2,20,"UTF-8")+(xgzl2.length() > 20 ? "..." : "")+"</a></span></div>");
							} catch (UnsupportedEncodingException e) {
								logger.error(e,e);
							}
						}
					}
					String dxyjtzr=(String)object[5];//---
					StringBuffer dxyjtzrsb=new StringBuffer();//---
					String name;
					if(dxyjtzr!=null&&!"".equals(dxyjtzr)){
						String[] dxyjtzrarray = dxyjtzr.split(",");
						for (int i = 0; i < dxyjtzrarray.length; i++) {
							name = UserContextUtil.getInstance().getUserContext(dxyjtzrarray[i]).get_userModel().getUsername()+"["+dxyjtzrarray[i]+"]";
							dxyjtzrsb.append("<div id='"+dxyjtzrarray[i]+"'><span class='selItem'>"+name+"</span></div>");
						}
					}
					
					String ggid=(String)object[6];//---
					String khbh=(String)object[7];//---
					String gzyjlrrid=(String)object[8];//---
					map.put("ID", id);
					map.put("GZYJLRR", gzyjlrr);
					map.put("GZYJLRSJ", gzyjlrsj);
					map.put("GZYJJSM", gzyjjsm);
					map.put("GZYJSMWD", gzyjsmwdsb.toString());
					map.put("DXYJTZR", dxyjtzr);
					map.put("DXYJTZRSB", dxyjtzrsb.toString());
					map.put("GGID", ggid);
					map.put("KHBH", khbh);
					map.put("GZYJLRRID", gzyjlrrid);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	
	public int getTotalListSize(String khbh,String noticename,String noticetype, String startdate,
			String enddate,String bzlx, String spzt,String zqdm,String zqjc,String dq,String companyno) {
		int count = 0;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date Date1 = null;
			java.util.Date Date2 = null;
			int ct1 = 0;
			if(startdate!=null&&!startdate.equals(""))
				Date1 = format.parse(startdate);
			if(enddate!=null&&!enddate.equals(""))
				Date2 = format.parse(enddate);
			if(Date1!=null&&Date2!=null)
				ct1 = Date1.compareTo(Date2);
		
		if(ct1>0){
			return count;
		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return count;
		}
		
		Map params = new HashMap();
		int n = 1;
		StringBuffer sql =new StringBuffer( "select count(*) as count FROM BD_MEET_QTGGZL a ");
		
		//新增开始
		if (dq != null && !"".equals(dq)) {
			sql.append("INNER JOIN (SELECT CUSTOMERNO,ZCQX FROM BD_ZQB_KH_BASE WHERE ZCQX LIKE ? ) KH ON KH.CUSTOMERNO=A.KHBH");
			params.put(n, "%"+dq+"%");
			n++;
		}
		sql.append(" where 1=1");
		 //新增截止
		if(khbh!=null&&!khbh.equals("")){
			sql.append(" and a.khbh= ? ");
			params.put(n, khbh);
			n++;

		}else{
			OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
			Long orgroleid = user.getOrgroleid();
			if(orgroleid==3l){
				khbh = user.getExtend1();
				sql.append(" AND A.KHBH= ? ");
				params.put(n, khbh);
				n++;
			}else if(orgroleid==5l){
			}else{
				String currentuserid = user.getUserid();
				sql.append(" AND A.KHBH IN (SELECT khbh FROM BD_MDM_KHQXGLB WHERE 1=1 AND(SUBSTR(KHFZR,0, instr(KHFZR,'[',1)-1) ='").append(currentuserid).append("' OR SUBSTR(ZZCXDD,0, instr(ZZCXDD,'[',1)-1) ='").append(currentuserid).append("' OR SUBSTR(FHSPR,0, instr(FHSPR,'[',1)-1) ='").append(currentuserid).append("' OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) ='").append(currentuserid).append("' OR SUBSTR(CWSCBFZR2,0, instr(CWSCBFZR2,'[',1)-1) ='").append(currentuserid).append("' OR SUBSTR(CWSCBFZR3,0, instr(CWSCBFZR3,'[',1)-1) ='").append(currentuserid).append("' OR SUBSTR(qynbrysh, instr(qynbrysh,'[',1)-1) ='").append(currentuserid).append("'))");
			}
		}
		if (zqdm != null && !"".equals(zqdm)) {
			sql.append(" and a.zqdmxs like ? ");
			params.put(n, "%" + zqdm + "%");
			n++;
		}
		if (zqjc != null && !"".equals(zqjc)) {
			sql.append(" and a.zqjcxs like ? ");
			params.put(n, "%" + zqjc + "%");
			n++;
		}
		if (noticename != null && !"".equals(noticename)) {
			sql.append(" and a.noticename like  ? ");
			params.put(n, "%" + noticename + "%");
			n++;
		}
		if (noticetype != null && !"".equals(noticetype)) {
			sql.append(" and a.noticetype like ? ");
			params.put(n, noticetype);
			n++;
		}
		if (startdate != null && !"".equals(startdate)) {
			sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')>= ? ");
			params.put(n, startdate);
			n++;
		}
		if (enddate != null && !"".equals(enddate)) {
			sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')<= ? ");
			params.put(n, enddate);
			n++;
		}
		if(spzt!=null&&!spzt.equals("")){
			sql.append(" AND A.SPZT= ? ");
			params.put(n, spzt);
			n++;
		}
		if (companyno != null && !"".equals(companyno)) {
			sql.append(" and a.companyno = ? ");
			params.put(n, companyno);
			n++;
		}
		count = DBUTilNew.getInt("count",sql.toString(), params);
		return count;
	}
	
	public FileUpload save(FileUpload fileModel)
	  {
	    if (fileModel != null) {
	      String str = (String)getHibernateTemplate().save(fileModel);
	    }
	    return fileModel;
	  }

	public List<HashMap> getTzggList(String tzlx1 ,String owner,String tzbt,String spzt, int pageNumber, int pageSize) {
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		List params = new ArrayList();
		/*StringBuffer sb = new StringBuffer("select * from BD_XP_TZGGB where 1=1 ");*/
		StringBuffer sb = new StringBuffer("select id,tzbt,to_char(zchfsj,'yyyy-MM-dd hh24:mi') zchfsj,tznr,xgzl,hfr,sftz,fsr,jszt,fszt,to_char(fssj,'yyyy-MM-dd hh24:mi') fssj,tzlx from BD_XP_TZGGB where 1=1 ");
		if(owner!=null&&!"".equals(owner)){
			sb.append( " and fsr = ?");
			params.add(owner);
		}
		if("".equals(owner)){
			sb.append( " and (fsr != '超级管理员' or tzlx='股东质押查询')");
		}
		if(tzbt!=null&&!"".equals(tzbt)){
			sb.append( " and tzbt like ?");
			params.add("%"+tzbt+"%");
		}
		if(spzt!=null&&!"".equals(spzt)&&!"全部".equals(spzt)){
			sb.append( " and fszt = ?");
			params.add(spzt);
		}
		if(tzlx1!=null&&!"".equals(tzlx1)){
			sb.append( " and tzlx = ?");
			params.add(tzlx1);
		}
		sb.append(" order by id desc");
		final String sql1 = sb.toString();
		final List param = params;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				List<Map> l = new ArrayList<Map>();
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String tzbt = (String) object[1];
					String zchfsj = (String) object[2];// 提问时间
					String tznr = (String) object[3];
					String xgzl=(String)object[4];
					String  hfr= (String) object[5];
					String sftz = (String) object[6];
					String fsr=(String)object[7];
					String  jszt= (String) object[8];
					String  fszt= (String) object[9];
					String fssj = (String) object[10];// 发送时间
					String  tzlx= (String) object[11];
					map.put("ID", id.toString());
					map.put("TZBT", tzbt);
					map.put("ZCHFSJ", zchfsj);
					map.put("TZNR", tznr);
					map.put("XGZL", xgzl);
					map.put("HFR", hfr);
					map.put("SFTZ", sftz);
					map.put("FSR", fsr);
					map.put("FSSJ", fssj);
					map.put("JSZT", jszt);
					map.put("FSZT", fszt);
					map.put("HFXXBFORMID", fszt);
					map.put("FSZT", fszt);
					map.put("TZLX", tzlx);
					l.add(map);
				}
				return l;
			}
		});
	}

	public int getTzggListSize(String tzlx,String owner, String tzbt, String spzt) {
		int count=0;
		Map params = new HashMap();
		int n = 1;
		StringBuffer sb = new StringBuffer("select count(*) as count from BD_XP_TZGGB where 1=1 ");
		if(owner!=null&&!"".equals(owner)){
			sb.append( " and fsr = ? ");
			params.put(n, owner);
			n++;
		}
		if("".equals(owner)){
			sb.append( " and (fsr != '超级管理员' or tzlx='股东质押查询')");
		}
		if(tzbt!=null&&!"".equals(tzbt)){
			sb.append( " and tzbt like ? ");
			params.put(n, "%"+tzbt+"%");
			n++;
		}
		if(spzt!=null&&!"".equals(spzt)&&!"全部".equals(spzt)){
			sb.append( " and fszt = ? ");
			params.put(n, spzt);
			n++;
		}
		if(tzlx!=null&&!"".equals(tzlx)){
			sb.append( " and tzlx = ? ");
			params.put(n, tzlx);
			n++;
		}
		String sql=sb.toString();
		count = DBUTilNew.getInt("count",sql.toString(), params);
		return count;
	}
	public List<HashMap> getsygdt(final String userid,final String username) {
				
				List params = new ArrayList();
				StringBuffer sb = new StringBuffer("SELECT * FROM (");
						sb.append("SELECT TZ.ID,TZ.TZBT,TO_CHAR(TZ.ZCHFSJ,'YYYY-MM-DD HH24:MI') ZCHFSJ,TZ.TZNR,TZ.XGZL,TZ.HFR,TZ.SFTZ,TZ.FSR,HF.STATUS HFSTATUS,TZ.FSZT,TO_CHAR(TZ.FSSJ,'YYYY-MM-DD HH24:MI') FSSJ,GT.STATUS GTSTATUS,HFXX.ID HID,GT.CONTENT GTCONTENT,HFXX.CONTENT HFCONTENT,HFXX.INSTANCEID INSTANCEID,TZ.TZLX,TZ.SFYHF, TZ.EXTEND5 FROM BD_XP_TZGGB TZ INNER JOIN BD_XP_HFQKB HF ON TZ.ID=HF.GGID LEFT JOIN BD_VOTE_XPZCFKGTB GT ON TZ.ID=GT.GGID AND HF.CUSTOMERNO = GT.CUSTOMERNO LEFT JOIN BD_XP_HFXXB HFXX ON HF.GGID=HFXX.GGID AND HF.HFID=HFXX.ID ");
						sb.append(" WHERE 1=1 AND TZ.TZBT is not null");
				String zzggid="";
				if(userid!=null&&!"".equals(userid)){
					sb.append( " AND HF.USERID=? ");
					params.add(userid);
				}
				sb.append(" UNION");
				sb.append(" SELECT TZ.ID,TZ.TZBT,TO_CHAR(TZ.ZCHFSJ,'YYYY-MM-DD HH24:MI') ZCHFSJ,TZ.TZNR,TZ.XGZL,TZ.HFR,TZ.SFTZ,TZ.FSR,HF.STATUS HFSTATUS,TZ.FSZT,TO_CHAR(TZ.FSSJ,'YYYY-MM-DD HH24:MI') FSSJ,");
				sb.append(" NULL GTSTATUS,NULL HID,NULL GTCONTENT,NULL HFCONTENT,NULL INSTANCEID,NULL,NULL, TZ.EXTEND5");
				sb.append(" FROM BD_XP_TZGGB TZ INNER JOIN BD_XP_DDGZHFQK HF ON TZ.ID=HF.GGID");
				sb.append(" WHERE 1=1  AND HF.USERID=?");
				params.add(userid);
				sb.append(")  where hfstatus='未回复'");
				sb.append(" ORDER BY ID DESC");
				final String sql1 = sb.toString();
				final List param = params;
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				return this.getHibernateTemplate().executeFind(new HibernateCallback() {
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql1);
						DBUtilInjection d=new DBUtilInjection();
						List<Map> l = new ArrayList<Map>();
						for (int i = 0; i < param.size(); i++) {
							if(param.get(i)!=null && !"".equals(param.get(i).toString())){
								String params=param.get(i).toString().replace("%", "").trim();
								if(d.HasInjectionData(params)){
									return l;
								}
								}
							query.setParameter(i, param.get(i));
						}
						List<Object[]> li = query.list();
						HashMap map;
						HashMap m = new HashMap();
						BigDecimal b;
						int n = 0;
						for (Object[] object : li) {
							map = new HashMap();
							BigDecimal id = (BigDecimal) object[0];
							String tzbt = (String) object[1];
							String zchfsj = (String) object[2];
							String tznr = (String) object[3];
							String xgzl=(String)object[4];
							String  hfr= (String) object[5];
							String sftz = (String) object[6];
							String fsr=(String)object[7];
							String  jszt= (String) object[8];
							String  fszt= (String) object[9];
							String fssj = (String) object[10];
							String status = (String) object[11];
							BigDecimal hfid = (BigDecimal) object[12];
							String gtcontent = (String) object[13]==null?"":(String) object[13];
							String hfcontent = (String) object[14]==null?"":(String) object[14];
							BigDecimal instanceid = (BigDecimal) object[15];
							String  tzlx= (String) object[16];
							String  sfyhf= (String) object[17];
							String  extend5= (String) object[18];
							map.put("ID", id);
							map.put("TZBT", tzbt);
							map.put("ZCHFSJ", zchfsj);
							map.put("TZNR", tznr);
							map.put("XGZL", xgzl);
							map.put("HFR", hfr);
							map.put("SFTZ", sftz);
							map.put("FSR", fsr);
							map.put("FSSJ", fssj);
							map.put("JSZT", jszt);
							map.put("FSZT", fszt);
							map.put("STATUS", status==null?"":status);
							map.put("HFID", hfid==null?"0":hfid);
							map.put("GTCONTENT",gtcontent);
							map.put("HFCONTENT",hfcontent);
							map.put("INSTANCEID",instanceid==null?"0":instanceid);
							map.put("TZLX",tzlx);
							map.put("SFYHF",sfyhf);
							map.put("EXTEND5",extend5);
							l.add(map);
						}
						return l;
					}
				});
			}
	
	public List<HashMap> getTZGGReceptionList(final String userid,final String username,final String owner,final String tzbt,
			final String spzt,  int pageNumber, int pageSize) {
				final int pageSize1 = pageSize;
				final int startRow1 = (pageNumber - 1) * pageSize;
				List params = new ArrayList();
				StringBuffer sb = new StringBuffer("SELECT * FROM (");
						sb.append("SELECT TZ.ID,TZ.TZBT,TO_CHAR(TZ.ZCHFSJ,'YYYY-MM-DD HH24:MI') ZCHFSJ,TZ.TZNR,TZ.XGZL,TZ.HFR,TZ.SFTZ,TZ.FSR,HF.STATUS HFSTATUS,TZ.FSZT,TO_CHAR(TZ.FSSJ,'YYYY-MM-DD HH24:MI') FSSJ,GT.STATUS GTSTATUS,HFXX.ID HID,GT.CONTENT GTCONTENT,HFXX.CONTENT HFCONTENT,HFXX.INSTANCEID INSTANCEID,TZ.TZLX,TZ.SFYHF FROM BD_XP_TZGGB TZ INNER JOIN BD_XP_HFQKB HF ON TZ.ID=HF.GGID LEFT JOIN BD_VOTE_XPZCFKGTB GT ON TZ.ID=GT.GGID AND HF.CUSTOMERNO = GT.CUSTOMERNO LEFT JOIN BD_XP_HFXXB HFXX ON HF.GGID=HFXX.GGID AND HF.HFID=HFXX.ID ");
						sb.append(" WHERE 1=1 AND TZ.TZBT is not null");
				String zzggid="";
				if(userid!=null&&!"".equals(userid)){
					sb.append( " AND HF.USERID=? ");
					params.add(userid);
				}
				if(tzbt!=null&&!"".equals(tzbt)){
					sb.append( " AND TZ.TZBT LIKE ?");
					params.add(tzbt);
				}
				if(spzt!=null&&!"".equals(spzt)&&!"全部".equals(spzt)){
					String ggidlist = getGGIDList(spzt,userid);
					if(ggidlist!=null&&!"".equals(ggidlist)){
						String[] split = ggidlist.split(",");
						sb.append( " AND TZ.ID IN (");
						for (int i = 0; i < split.length; i++) {
							if(i==(split.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(split[i]);
						}
						sb.append(")");
					}
					sb.append(" AND HF.STATUS =?");
					params.add(spzt);
				}
				
				sb.append(" UNION");
				sb.append(" SELECT TZ.ID,TZ.TZBT,TO_CHAR(TZ.ZCHFSJ,'YYYY-MM-DD HH24:MI') ZCHFSJ,TZ.TZNR,TZ.XGZL,TZ.HFR,TZ.SFTZ,TZ.FSR,HF.STATUS HFSTATUS,TZ.FSZT,TO_CHAR(TZ.FSSJ,'YYYY-MM-DD HH24:MI') FSSJ,");
				sb.append(" NULL GTSTATUS,NULL HID,NULL GTCONTENT,NULL HFCONTENT,NULL INSTANCEID,NULL,NULL");
				sb.append(" FROM BD_XP_TZGGB TZ INNER JOIN BD_XP_DDGZHFQK HF ON TZ.ID=HF.GGID");
				sb.append(" WHERE 1=1  AND HF.USERID=?");
				params.add(userid);
				sb.append(")");
				
				sb.append(" ORDER BY ID DESC");
				final String sql1 = sb.toString();
				final List param = params;
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				return this.getHibernateTemplate().executeFind(new HibernateCallback() {
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql1);
						DBUtilInjection d=new DBUtilInjection();
						List<Map> l = new ArrayList<Map>();
						for (int i = 0; i < param.size(); i++) {
							if(param.get(i)!=null && !"".equals(param.get(i).toString())){
								String params=param.get(i).toString().replace("%", "").trim();
								if(d.HasInjectionData(params)){
									return l;
								}
								}
							query.setParameter(i, param.get(i));
						}
						query.setFirstResult(startRow1);
						query.setMaxResults(pageSize1);
						List<Object[]> li = query.list();
						HashMap map;
						HashMap m = new HashMap();
						BigDecimal b;
						int n = 0;
						for (Object[] object : li) {
							map = new HashMap();
							BigDecimal id = (BigDecimal) object[0];
							String tzbt = (String) object[1];
							String zchfsj = (String) object[2];
							String tznr = (String) object[3];
							String xgzl=(String)object[4];
							String  hfr= (String) object[5];
							String sftz = (String) object[6];
							String fsr=(String)object[7];
							String  jszt= (String) object[8];
							String  fszt= (String) object[9];
							String fssj = (String) object[10];
							String status = (String) object[11];
							BigDecimal hfid = (BigDecimal) object[12];
							String gtcontent = (String) object[13]==null?"":(String) object[13];
							String hfcontent = (String) object[14]==null?"":(String) object[14];
							BigDecimal instanceid = (BigDecimal) object[15];
							String  tzlx= (String) object[16];
							String  sfyhf= (String) object[17];
							map.put("ID", id);
							map.put("TZBT", tzbt);
							map.put("ZCHFSJ", zchfsj);
							map.put("TZNR", tznr);
							map.put("XGZL", xgzl);
							map.put("HFR", hfr);
							map.put("SFTZ", sftz);
							map.put("FSR", fsr);
							map.put("FSSJ", fssj);
							map.put("JSZT", jszt);
							map.put("FSZT", fszt);
							map.put("STATUS", status==null?"":status);
							map.put("HFID", hfid==null?"0":hfid);
							map.put("GTCONTENT",gtcontent);
							map.put("HFCONTENT",hfcontent);
							map.put("INSTANCEID",instanceid==null?"0":instanceid);
							map.put("TZLX",tzlx);
							map.put("SFYHF",sfyhf);
							l.add(map);
						}
						return l;
					}
				});
			}
	
	public String getGGIDList(String status) {
		StringBuffer sb=new StringBuffer();
		String sql="select distinct ggid from Bd_Xp_Hfqkb where status= ? order by ggid desc";
		DBUtilInjection d=new DBUtilInjection();
			
			if(status!=null && !"".equals(status)){
				
				if(d.HasInjectionData(status)){
					return "";
				}
				}
		
		Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		Query query=session.createSQLQuery(sql);
		List list = query.setString(0, status).list();
		for (Object obj : list) {
			if(obj!=null){
				sb.append(obj.toString()+",");
			}
		}
		if(sb.length()<1){
			return null;
		}
		return sb.toString().substring(0, sb.length()-1);
	}
	
	public String getGGIDList(String status,String userid) {
		StringBuffer sb=new StringBuffer();
		String sql="select distinct ggid from Bd_Xp_Hfqkb where status= ? and userid= ? order by ggid desc";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
	    if ((status != null) && (!"".equals(status))) {
        	if(d.HasInjectionData(status)){
				return null;
			}
          
        }
	    if ((userid != null) && (!"".equals(userid))) {
        	if(d.HasInjectionData(userid)){
				return null;
			}
          
        }
		Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		Query query=session.createSQLQuery(sql);
		List list = query.setString(0, status).setString(1, userid).list();
		for (Object obj : list) {
			if(obj!=null){
				sb.append(obj.toString()+",");
			}
		}
		if(sb.length()<1){
			return null;
		}
		return sb.toString().substring(0, sb.length()-1);
	}

	public int getTzggReceptionListSize(String userid,String name,String owner, String tzbt, String spzt) {
		int count=0;
		String zzggid="";
		//select tz.id,tz.tzbt,to_char(tz.zchfsj,'yyyy-MM-dd hh24:mi') zchfsj,tz.tznr,tz.xgzl,tz.hfr,tz.sftz,tz.fsr,hf.status hfstatus,tz.fszt,to_char(tz.fssj,'yyyy-MM-dd hh24:mi') fssj,gt.status gtstatus from BD_XP_TZGGB tz inner join BD_XP_HFQKB hf on tz.id=hf.ggid left join BD_VOTE_XPZCFKGTB gt on tz.id=gt.ggid and gt.username='"+username+"' where 1=1
		StringBuffer sb = new StringBuffer("select count(*) as count from BD_XP_TZGGB tz inner join BD_XP_HFQKB hf on tz.id=hf.ggid left join BD_VOTE_XPZCFKGTB gt on tz.id=gt.ggid and hf.customerno = gt.customerno and gt.username='"+name+"' where 1=1");
		if(userid!=null&&!"".equals(userid)){
			sb.append( " and hf.userid='"+userid+"' ");
		}
		if(tzbt!=null&&!"".equals(tzbt)){
			sb.append( " and tz.tzbt like '%"+tzbt+"%'");
		}
		if(spzt!=null&&!"".equals(spzt)&&!"全部".equals(spzt)){
			String ggidlist = getGGIDList(spzt,userid);
			if(ggidlist!=null&&!"".equals(ggidlist)){
				String[] split = ggidlist.split(",");
				StringBuffer customerG=new StringBuffer();
				for (int i = 0; i < split.length; i++) {
					if (i == (split.length - 1)) {  
						customerG.append(split[i]); 
				     }else if((i%999)==0 && i>0){  
				    	 customerG.append(split[i]).append(") or tz.id in ("); 
				     }else{  
				    	 customerG.append(split[i]).append(",");  
				     }  
				}
				zzggid=customerG.toString();
				sb.append( " and tz.id in ("+zzggid+")");
			}
			sb.append(" and hf.status='"+spzt+"'");
		}
		String sql=sb.toString();
		count = DBUtil.getInt(sql.toString(), "count");
		return count;
	}

	public List<HashMap> getNoticeList(String roleid, String status,String ggid, int pageNumber, int pageSize) {
		List<HashMap> l2 = new ArrayList<HashMap>();
		if(ggid!=null&&!"".equals(ggid)){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String username = uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
			//roleid=uc.get_userModel().getOrgroleid().toString();
			Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
			final int pageSize1 = pageSize;
			final int startRow1 = (pageNumber - 1) * pageSize;
			List params = new ArrayList();
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT DISTINCT B.ID, B.GSDM ,B.GSMC,B.XM,B.USERID,B.STATUS,TO_CHAR(B.HFSJ,'yyyy-MM-dd hh24:mi') HFSJ,B.GGID,B.HFID,O.MOBILE,S.INSTANCEID,B.SFCK,X.EXTEND1,B.EXTEND1 MEMO FROM BD_XP_HFQKB B");
			sb.append(" LEFT JOIN BD_XP_HFXXB X ON B.HFID=X.ID");
			sb.append(" LEFT JOIN ORGUSER O ON B.USERID=O.USERID");
			sb.append(" LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE FORMID=?) S ON B.HFID=S.DATAID WHERE 1=1 ");
			params.add(config.get("hfxxbformid"));
			if(ggid!=null&&!"".equals(ggid)){
				sb.append(" AND B.GGID = ?");
				params.add(ggid);
			}
			if(status!=null&&!"".equals(status)&&!"全部".equals(status)){
				sb.append(" AND B.STATUS = ?");
				params.add(status);
			}
			if(roleid!=null && roleid.equals("flag")){
				sb.append(" AND B.CUSTOMERNO IN (SELECT S.KHBH FROM BD_MDM_KHQXGLB S WHERE S.KHFZR=? OR S.ZZCXDD=? OR S.ZZSPR=? OR S.FHSPR=? OR S.CWSCBFZR2=? OR S.CWSCBFZR3=? or s.qynbrysh=? )");
				params.add(username);
				params.add(username);
				params.add(username);
				params.add(username);
				params.add(username);
				params.add(username);
				params.add(username);
			}
			final String sql1 = sb.toString();
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			final String ggid1=ggid;
			final List param = params;
			return this.getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(org.hibernate.Session session)
						throws HibernateException, SQLException {
					Query query = session.createSQLQuery(sql1);
					DBUtilInjection d=new DBUtilInjection();
					List<Map> l = new ArrayList<Map>();
					for (int i = 0; i < param.size(); i++) {
						if(param.get(i)!=null && !"".equals(param.get(i).toString())){
							String params=param.get(i).toString().replace("%", "").trim();
							if(d.HasInjectionData(params)){
								return l;
							}
							}
						query.setParameter(i, param.get(i));
					}
					query.setFirstResult(startRow1);
					query.setMaxResults(pageSize1);
					List<Object[]> li = query.list();
					HashMap map;
					HashMap m = new HashMap();
					BigDecimal b;
					int n = 0;
					String xgzl=null;
					Map para = new HashMap();
					para.put(1, ggid1);
					String tzlx = com.iwork.commons.util.DBUtil.getDataStr("TZLX", "SELECT TZLX FROM BD_XP_TZGGB WHERE ID=?", para);
					for (Object[] object : li) {
						int ydrs = 0;
						int sdrs = 0;
						map = new HashMap();
						BigDecimal id = (BigDecimal) object[0];
						String gsdm = (String) object[1];
						String gsmc = (String) object[2];
						String xm=(String)object[3];
						String userid=(String)object[4];
						String  sjh= (String)object[9];
						String status = (String) object[5];
						if(status.equals("已回复")){
							xgzl = DBUtil.getString("select XGZL from bd_xp_hfxxb where ggid="+ggid1+" and hfr='"+xm+"'", "XGZL");
						}else{
							xgzl=null;
						}
						String hfsj = (String) object[6];
						BigDecimal ggid = (BigDecimal) object[7];
						BigDecimal hfid = (BigDecimal) object[8];
						BigDecimal instanceid = (BigDecimal) object[10];
						BigDecimal sfck = (BigDecimal) object[11];
						String extend1 = (String) object[12];
						String memo = (String) object[13];
						if(instanceid!=null){
							List<HashMap> rs = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid.toString()), "SUBFORM_PXTZRYB");
							ydrs=rs.size();
							for (HashMap map2 :rs){
								if(map2.get("SFQD").equals("是")){
									sdrs+=1;
								}
							}
						}
						map.put("ID", id.toString());
						map.put("GSDM", gsdm);
						map.put("GSMC", gsmc);
						map.put("XM", xm);
						map.put("USERID", userid);
						map.put("SJH", sjh);
						map.put("STATUS", status);
						map.put("HFSJ", hfsj);
						map.put("GGID", ggid==null?"0":ggid.toString());
						map.put("HFID", hfid==null?"0":hfid.toString());
						map.put("INSTANCEID", instanceid==null?"0":instanceid);
						map.put("YDRS", ydrs);
						map.put("SDRS", sdrs);
						map.put("XGZL", xgzl);
						map.put("SFCK",sfck==null?"":sfck.toString() );
						if((sfck==null?"":sfck.toString()).equals("1")){
							map.put("SFCK", "是");
						}
						map.put("EXTEND1", extend1==null?"":extend1.equals("1")?"是":"否");
						map.put("MEMO", memo);
						map.put("TZLX", tzlx);
						l.add(map);
					}
					return l;
				}
			});
		}
		return l2;
	}

	public int getNoticeListSize(String roleid, String status, String ggid) {
		int count=0;
		Map params = new HashMap();
		int n = 1;
		if(ggid!=null&&!"".equals(ggid)){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String username = uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
			StringBuffer sb = new StringBuffer("select count(*) as count from Bd_Xp_Hfqkb where 1=1 ");
			if(status!=null&&!"".equals(status)&&!"全部".equals(status)){
				sb.append( " and status = ? ");
				params.put(n, status);
				n++;
			}
			if(ggid!=null&&!"".equals(ggid)){
				sb.append( " and ggid = ? ");
				params.put(n, ggid);
				n++;
			}
			if(roleid!=null&& roleid.equals("flag")){
				sb.append(" AND CUSTOMERNO IN (SELECT S.KHBH FROM BD_MDM_KHQXGLB S WHERE S.KHFZR=? OR S.ZZCXDD=? OR S.ZZSPR=? OR S.FHSPR=? OR S.CWSCBFZR2=? OR S.CWSCBFZR3=? or s.qynbrysh=? )");
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
			}
			String sql=sb.toString();
			count = DBUTilNew.getInt("count",sql.toString(),params);
		}
		return count;
	}
	
	public int getNoticeListSize(String roleid, String ggid) {
		int count=0;
		Map params = new HashMap();
		int n = 1;
		if(ggid!=null&&!"".equals(ggid)){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String username = uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
			StringBuffer sb = new StringBuffer("select count(*) as count from Bd_Xp_Hfqkb where 1=1 ");
			if(ggid!=null&&!"".equals(ggid)){
				sb.append( " and ggid = ? ");
				params.put(n, ggid);n++;
			}
			if(roleid!=null&&roleid.equals("4")){
				sb.append(" AND CUSTOMERNO IN (SELECT S.KHBH FROM BD_MDM_KHQXGLB S WHERE S.KHFZR=? OR S.ZZCXDD=? OR S.ZZSPR=? OR S.FHSPR=? OR S.CWSCBFZR2=? OR S.CWSCBFZR3=? or s.qynbrysh=? )");
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
			}
			String sql=sb.toString();
			count = DBUTilNew.getInt("count",sql.toString(),params);
		}
		return count;
	}

	public List<HashMap> getHfqkbByGgid(String ggid) {
		List<HashMap> result = new ArrayList<HashMap>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.USERID,S.INSTANCEID,O.MOBILE,O.EMAIL FROM BD_XP_HFQKB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN ORGUSER O ON B.USERID=O.USERID WHERE S.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='回复情况表单') AND B.GGID=? AND (B.TZZT IS NULL OR B.TZZT=0)");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, ggid);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap data = new HashMap();
				String userid = rs.getString("USERID");
				String instanceid = rs.getString("INSTANCEID");
				String mobile = rs.getString("MOBILE");
				String email = rs.getString("EMAIL");
				data.put("USERID",userid);
				data.put("INSTANCEID",instanceid);
				data.put("MOBILE",mobile);
				data.put("EMAIL",email);
				result.add(data);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> getXMJBRbByGgid(String ggid) {
		List<HashMap> result = new ArrayList<HashMap>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT O.USERID,0 AS INSTANCEID,O.MOBILE,O.EMAIL FROM (SELECT DISTINCT SUBSTR(B.KHFZR,0, instr(B.KHFZR,'[',1)-1) KHFZR FROM BD_XP_HFQKB A LEFT JOIN BD_MDM_KHQXGLB B ON A.CUSTOMERNO=B.KHBH WHERE A.GGID=? AND B.ID IS NOT NULL AND B.KHFZR IS NOT NULL) A LEFT JOIN ORGUSER O ON A.KHFZR=O.USERID");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, ggid);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap data = new HashMap();
				String userid = rs.getString("USERID");
				String instanceid = rs.getString("INSTANCEID");
				String mobile = rs.getString("MOBILE");
				String email = rs.getString("EMAIL");
				data.put("USERID",userid);
				data.put("INSTANCEID",instanceid);
				data.put("MOBILE",mobile);
				data.put("EMAIL",email);
				result.add(data);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> getHfqkbByGgStatus(String ggid) {
		List<HashMap> result = new ArrayList<HashMap>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.USERID,S.INSTANCEID,O.MOBILE,O.EMAIL FROM BD_XP_HFQKB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN ORGUSER O ON B.USERID=O.USERID WHERE S.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='回复情况表单') AND B.GGID=? AND B.STATUS='未回复'");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, ggid);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap data = new HashMap();
				String userid = rs.getString("USERID");
				String instanceid = rs.getString("INSTANCEID");
				String mobile = rs.getString("MOBILE");
				String email = rs.getString("EMAIL");
				data.put("USERID",userid);
				data.put("INSTANCEID",instanceid);
				data.put("MOBILE",mobile);
				data.put("EMAIL",email);
				result.add(data);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> loadAnnouncementList(String ggid) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select id,tzbt,to_char(zchfsj,'yyyy-MM-dd hh24:mi') zchfsj,tznr,xgzl,hfr,sftz,fsr,jszt,fszt,to_char(fssj,'yyyy-MM-dd hh24:mi') fssj,TZLX,dctm,wjfile,sfyhf,extend5 from BD_XP_TZGGB where 1=1 ");
		if(ggid!=null&&!"".equals(ggid)&&!"undefined".equals(ggid)){
			sb.append( " and id = ?");
			params.add(ggid);
		}
		final String sql1 = sb.toString();
		final List param = params;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String tzbt = (String) object[1];
					String zchfsj = (String) object[2];
					String tznr = (String) object[3];
					String xgzl=(String)object[4];
					StringBuffer count=new StringBuffer("");
					if(xgzl!=null&&!"".equals(xgzl)){
						List<FileUpload> sublist = FileUploadAPI.getInstance()
								.getFileUploads(xgzl);
						for (FileUpload fileUpload : sublist) {
							String xgzl2=fileUpload.getFileSrcName();
							String uuid2=fileUpload.getFileId();
							try {
								count.append("<div id=\"").append(fileUpload.getFileId()).append("\" style=\"background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\">");
								count.append("<div style=\"align:right;float: right;\">");
								count.append("<a href=\"javascript:uploadifyReomve('XGZL','").append(fileUpload.getFileId()).append("','").append(fileUpload.getFileId()).append("');\">");
								count.append("<img src=\"/iwork_img/del3.gif\">");
								count.append("</a>");
								count.append("</div>");
								count.append("<span>");
								count.append("<a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\">");
								count.append("<img src=\"/iwork_img/attach.png\">");
								count.append(fileUpload.getFileSrcName());
								count.append("</a>");
								count.append("</span>");
								count.append("</div>");
							} catch (Exception e) {
								logger.error(e,e);
							}
						}
					}
					String  hfr= (String) object[5];
					String sftz = (String) object[6];
					String fsr=(String)object[7];
					String  jszt= (String) object[8];
					String  fszt= (String) object[9];
					String fssj = (String) object[10];
					String tzlx = (String) object[11];
					String dctm = (String) object[12];
					String wjfile = (String) object[13];
					String sfyhf = (String) object[14];
					String extend5 = (String) object[15];
					map.put("ID", id);
					map.put("TZBT", tzbt);
					map.put("ZCHFSJ", zchfsj);
					map.put("TZNR", tznr);
					map.put("XGZL", xgzl);
					map.put("HFR", hfr);
					map.put("SFTZ", sftz);
					map.put("FSR", fsr);
					map.put("FSSJ", fssj);
					map.put("JSZT", jszt);
					map.put("FSZT", fszt);
					map.put("COUNT",count.toString());
					map.put("TZLX",tzlx);
					map.put("DCTM",dctm);
					map.put("WJFILE",wjfile);
					map.put("EXTEND5",extend5);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getNoticeList(String ggid) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select id,tzbt,to_char(zchfsj,'yyyy-MM-dd hh24:mi') zchfsj,tznr,xgzl,hfr,sftz,fsr,jszt,fszt,to_char(fssj,'yyyy-MM-dd hh24:mi') fssj,tzlx from BD_XP_TZGGB where 1=1 ");
		if(ggid!=null&&!"".equals(ggid)){
			sb.append( " and id = ?");
			params.add(ggid);
		}
		final String sql1 = sb.toString();
		final List param = params;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String tzbt = (String) object[1];
					String zchfsj = (String) object[2];
					String tznr = (String) object[3];
					String xgzl=(String)object[4];
					String uuid="";
					StringBuffer count=new StringBuffer();
					count.append("<table>");
					if(xgzl!=null&&!"".equals(xgzl)){
						List<FileUpload> sublist = FileUploadAPI.getInstance()
								.getFileUploads(xgzl);
						for (FileUpload fileUpload : sublist) {
							xgzl=fileUpload.getFileSrcName();
							uuid=fileUpload.getFileId();
							count.append("<tr><td><a href=\"uploadifyDownload.action?fileUUID="+uuid+"\" style=\"color: #0000ff;border-bottom: 1px dotted #efefef; color: #0000ff; font-size: 12px; font-weight: 100; line-height: 15px; padding-left: 3px; padding-top: 5px; text-align: left; vertical-align: middle; word-break: break-all; word-wrap: break-word;\">"+xgzl+"</a></td></tr>");
						}
					}
					count.append("</table>");
					String  hfr= (String) object[5];
					String sftz = (String) object[6];
					String fsr=(String)object[7];
					String  jszt= (String) object[8];
					String  fszt= (String) object[9];
					String fssj = (String) object[10];
					String tzlx = (String) object[11];
					map.put("ID", id);
					map.put("TZBT", tzbt);
					map.put("ZCHFSJ", zchfsj);
					map.put("TZNR", tznr);
					map.put("XGZL", count.toString());
					map.put("HFR", hfr);
					map.put("SFTZ", sftz);
					map.put("FSR", fsr);
					map.put("FSSJ", fssj);
					map.put("JSZT", jszt);
					map.put("FSZT", fszt);
					map.put("TZLX", tzlx);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getNoticeList1(Long ggid) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select id,tzbt,to_char(zchfsj,'yyyy-MM-dd hh24:mi') zchfsj,tznr,xgzl,hfr,sftz,fsr,jszt,fszt,to_char(fssj,'yyyy-MM-dd hh24:mi') fssj,tzlx from BD_XP_TZGGB where 1=1 ");
		if(ggid!=null&&!"".equals(ggid)){
			sb.append( " and id = ?");
			params.add(ggid);
		}
		final String sql1 = sb.toString();
		final List param = params;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String tzbt = (String) object[1];
					String zchfsj = (String) object[2];
					String tznr = (String) object[3];
					String xgzl=(String)object[4];
					String uuid="";
					StringBuffer count=new StringBuffer();
					count.append("<table>");
					if(xgzl!=null&&!"".equals(xgzl)){
						List<FileUpload> sublist = FileUploadAPI.getInstance()
								.getFileUploads(xgzl);
						for (FileUpload fileUpload : sublist) {
							xgzl=fileUpload.getFileSrcName();
							uuid=fileUpload.getFileId();
							count.append("<tr><td><a href=\"uploadifyDownload.action?fileUUID="+uuid+"\" style=\"color: #0000ff;border-bottom: 1px dotted #efefef; color: #0000ff; font-size: 12px; font-weight: 100; line-height: 15px; padding-left: 3px; padding-top: 5px; text-align: left; vertical-align: middle; word-break: break-all; word-wrap: break-word;\">"+xgzl+"</a></td></tr>");
						}
					}
					count.append("</table>");
					String  hfr= (String) object[5];
					String sftz = (String) object[6];
					String fsr=(String)object[7];
					String  jszt= (String) object[8];
					String  fszt= (String) object[9];
					String fssj = (String) object[10];
					String tzlx = (String) object[11];
					map.put("ID", id);
					map.put("TZBT", tzbt);
					map.put("ZCHFSJ", zchfsj);
					map.put("TZNR", tznr);
					map.put("XGZL", count.toString());
					map.put("HFR", hfr);
					map.put("SFTZ", sftz);
					map.put("FSR", fsr);
					map.put("FSSJ", fssj);
					map.put("JSZT", jszt);
					map.put("FSZT", fszt);
					map.put("TZLX", tzlx);
					l.add(map);
				}
				return l;
			}
		});
	}
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

	public int getNoticeWHFListSize(String roleid, String ggid) {
		int count=0;
		int n=1;
		Map params = new HashMap();
		if(ggid!=null&&!"".equals(ggid)){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String username = uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
			StringBuffer sb = new StringBuffer("select count(*) as count from Bd_Xp_Hfqkb where 1=1 ");
			if(ggid!=null&&!"".equals(ggid)){
				sb.append( " and ggid = ? ");
				params.put(n, ggid);n++;
			}
			sb.append(" and status='未回复'");
			if(roleid!=null&&roleid.equals("4")){
				sb.append(" AND CUSTOMERNO IN (SELECT S.KHBH FROM BD_MDM_KHQXGLB S WHERE S.KHFZR=? OR S.ZZCXDD=? OR S.ZZSPR=? OR S.FHSPR=? OR S.CWSCBFZR2=? OR S.CWSCBFZR3=? or s.qynbrysh=? )");
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
				params.put(n,username);n++;
			}
			String sql=sb.toString();
			count = DBUTilNew.getInt("count",sql.toString(),params);
		}
		return count;
	}

	public int getTZGGWDSize(String owner) {
		int count=0;
		Map params = new HashMap();
		StringBuffer sb = new StringBuffer("select count(*) as count from Bd_Xp_Hfqkb where 1=1 ");
		if(owner!=null&&!"".equals(owner)){
			sb.append( " and xm = ? ");
			params.put(1, owner);
		}
		sb.append(" and status='未回复'");
		String sql=sb.toString();
		count = DBUTilNew.getInt("count",sql.toString(),params);
		return count;
	}
	
	public List<HashMap> getList() {
		StringBuffer sb = new StringBuffer("select fp.qynbrysh qynbrysh, fp.khfzr khfzr,fp.fhspr fhspr,fp.zzspr zzspr,fp.ggfbr ggfbr,fp.khbh khbh,fp.zzcxdd zzcxdd,kh.zqjc zqjc,kh.zqdm zqdm,fp.khmc khmc,fp.id id from BD_MDM_KHQXGLB fp,bd_zqb_kh_base kh where fp.khbh=kh.customerno order by kh.zqdm");
		List<HashMap> l = new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				HashMap map = new HashMap();
				String khfzr = rs.getString("khfzr");
				String fhspr = rs.getString("fhspr");
				String zzspr = rs.getString("zzspr");
				String ggfbr = rs.getString("ggfbr");
				String khbh = rs.getString("khbh");
				String zzcxdd= rs.getString("zzcxdd");
				String zqjc= rs.getString("zqjc");
				String zqdm = rs.getString("zqdm");
				String khmc = rs.getString("khmc");
				String id = rs.getString("id");
				String qynbrysh=rs.getString("qynbrysh");
				map.put("KHFZR", khfzr);
				map.put("FHSPR", fhspr);
				map.put("ZZSPR", zzspr);
				map.put("GGFBR", ggfbr);
				map.put("KHBH", khbh);
				map.put("ZZCXDD", zzcxdd);
				map.put("ZQJC", zqjc);
				map.put("ZQDM", zqdm);
				map.put("KHMC", khmc);
				map.put("ID", id);
				map.put("qynbrysh", qynbrysh);
				l.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return l;
	}

	public List<HashMap> getNmsxList(String khmc, String khbh,
			String nmxxStart, String nmxxEnd, String nmxx,int pageNumber,int pageSize) {
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String id = config.get("nmxxformid");
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select nmxx.ID ID,nmxx.NMSX NMSX,to_char(nmxx.NMSJ,'yyyy-MM-dd') NMSJ,nmxx.NMXGZL NMXGZL,nmxx.KHMC KHMC,nmxx.KHBH KHBH,BINDTABLE.instanceid instanceid from bd_xp_nmxx nmxx inner join SYS_ENGINE_FORM_BIND BINDTABLE on nmxx.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=? WHERE 1=1 ");
		params.add(id);
		if(khmc!=null){
			sb.append("  and KHMC=? ");
			params.add(khmc);
		}
		if(khbh!=null){
			sb.append(" and khbh=? ");
			params.add(khbh);
		}
		if(nmxxStart!=null&&!"".equals(nmxxStart)){
			sb.append(" and to_char(NMSJ,'yyyy-MM-dd')>= ? ");
			params.add(nmxxStart);
		}
		if(nmxxEnd!=null&&!"".equals(nmxxEnd)&&!nmxxEnd.equals(nmxxStart)){
			sb.append(" and to_char(NMSJ,'yyyy-MM-dd')<= ? ");
			params.add(nmxxEnd);
		}
		if(nmxx!=null&&!"".equals(nmxx)){
			sb.append(" and nmsx like ?");
			params.add("%"+nmxx+"%");
		}
		sb.append(" order by nmsj desc,id desc");
		final String sql1 = sb.toString();
		final List param = params;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String nmsx = (String) object[1];
					String nmsj = (String) object[2];
					String nmkhmc=(String)object[4];
					String  nmkhbh= (String) object[5];
					BigDecimal instanceid = (BigDecimal) object[6];
					String nmsxzl = (String) object[3];
					map.put("ID", id.toString());
					map.put("NMSX", nmsx);
					map.put("NMSXZL", nmsxzl==null?true:nmsxzl);
					map.put("NMSJ", nmsj);
					map.put("KHMC", nmkhmc);
					map.put("KHBH", nmkhbh);
					map.put("INSTANCEID", instanceid.toString());
					l.add(map);
				}
				return l;
			}
		});
	}

	public int getNmsxListSize(String khmc, String khbh,String nmxxStart, String nmxxEnd, String nmxx) {
		int count=0;
		Map params = new HashMap();
		int n = 1;
		StringBuffer sb = new StringBuffer("select count(*) as count from bd_xp_nmxx WHERE KHMC= ?  and khbh= ? ");
		params.put(n, khmc);
		n++;
		params.put(n, khbh);
		n++;
		if(!"".equals(nmxxStart)&&nmxxStart!=null){
			sb.append(" and to_char(nmsj,'yyyy-MM-dd')>= ? ");
			params.put(n, nmxxStart);
			n++;
		}
		if(!"".equals(nmxxEnd)&&nmxxEnd!=null){
			sb.append(" and to_char(nmsj,'yyyy-MM-dd')<= ? ");
			params.put(n, nmxxEnd);
			n++;
		}
		if(!"".equals(nmxx)&&nmxx!=null){
			sb.append(" and nmsx like ? ");
			params.put(n, "%"+nmxx+"%");
			n++;
		}
		String sql=sb.toString();
		count = DBUTilNew.getInt("count",sql.toString(),params);
		return count;
	}

	public List<HashMap> getVoteList(int pageNumber, int pageSize) {
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		StringBuffer sb = new StringBuffer("select id,tzbt,to_char(zchfsj,'yyyy-MM-dd hh24:mi') zchfsj,tznr,xgzl,hfr,sftz,fsr,jszt,fszt,to_char(fssj,'yyyy-MM-dd hh24:mi') fssj from BD_XP_TZGGB where TZBT like '挂牌公司重大事项信息披露自查反馈表%' order by id desc");
		final String sql1 = sb.toString();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String tzbt = (String) object[1];
					String zchfsj = (String) object[2];// 提问时间
					String dqrq=zchfsj.substring(8,10);
					String tznr = (String) object[3];
					String xgzl=(String)object[4];
					String  hfr= (String) object[5];
					String sftz = (String) object[6];
					String fsr=(String)object[7];
					String  jszt= (String) object[8];
					String  fszt= (String) object[9];
					String fssj = (String) object[10];// 发送时间
					map.put("ID", id.toString());
					map.put("TZBT", tzbt);
					map.put("ZCHFSJ", zchfsj);
					map.put("TZNR", tznr);
					map.put("XGZL", xgzl);
					map.put("HFR", hfr);
					map.put("SFTZ", sftz);
					map.put("FSR", fsr);
					map.put("FSSJ", fssj);
					map.put("JSZT", jszt);
					map.put("FSZT", fszt);
					map.put("DQRQ", dqrq);
					l.add(map);
				}
				return l;
			}
		});
	}

	public int getVoteListSize() {
		int count=0;
		StringBuffer sb = new StringBuffer("select count(*) as count from BD_XP_TZGGB where TZBT like '挂牌公司重大事项信息披露自查反馈表%' ");
		String sql=sb.toString();
		count = DBUtil.getInt(sql.toString(), "count");
		return count;
	}
	
	public List<HashMap> getCxddList(int pageNumber, int pageSize) {
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		StringBuffer sb = new StringBuffer("select id,tzbt,to_char(zchfsj,'yyyy-MM-dd hh24:mi') zchfsj,tznr,xgzl,hfr,sftz,fsr,jszt,fszt,to_char(fssj,'yyyy-MM-dd hh24:mi') fssj from BD_XP_TZGGB where TZBT like '持续督导日常工作反馈%' order by id desc");
		final String sql1 = sb.toString();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String tzbt = (String) object[1];
					String zchfsj = (String) object[2];// 提问时间
					String tznr = (String) object[3];
					String xgzl=(String)object[4];
					String  hfr= (String) object[5];
					String sftz = (String) object[6];
					String fsr=(String)object[7];
					String  jszt= (String) object[8];
					String  fszt= (String) object[9];
					String fssj = (String) object[10];// 发送时间
					map.put("ID", id);
					map.put("TZBT", tzbt);
					map.put("ZCHFSJ", zchfsj);
					map.put("TZNR", tznr);
					map.put("XGZL", xgzl);
					map.put("HFR", hfr);
					map.put("SFTZ", sftz);
					map.put("FSR", fsr);
					map.put("FSSJ", fssj);
					map.put("JSZT", jszt);
					map.put("FSZT", fszt);
					l.add(map);
				}
				return l;
			}
		});
	}

	public int getCxddListSize() {
		int count=0;
		StringBuffer sb = new StringBuffer("select count(*) as count from BD_XP_TZGGB where TZBT like '持续督导日常工作反馈%' ");
		String sql=sb.toString();
		count = DBUtil.getInt(sql.toString(), "count");
		return count;
	}
	
	public List<HashMap> getNoFeedBackListCxdd(String ggid,String status) {
		StringBuffer sb = new StringBuffer();
		List params = new ArrayList();
		sb.append("SELECT A.ID,B.XM,B.STATUS,B.HFSJ,B.GGID,B.USERID,C.LOCKSTATUS FROM BD_XP_TZGGB A LEFT JOIN BD_XP_DDGZHFQK B ON A.ID=B.GGID LEFT JOIN BD_VOTE_CXDDRCGZFK C ON B.GGID=C.TZGGID AND B.USERID=C.USERID WHERE 1=1");
		if(status!=null&&!status.equals("")){
			sb.append(" AND B.STATUS=?");
			params.add(status);
		}
		sb.append(" AND A.ID=?");
		params.add(ggid);
		final String sql = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				List<Map> l = new ArrayList<Map>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> li = query.list();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				HashMap map;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String xm = (String) object[1];
					String status = (String) object[2];
					Date hfsj = (Date) object[3];
					BigDecimal ggid = (BigDecimal) object[4];
					String userid = (String) object[5];
					String lockstatus = (String) object[6];
					map.put("ID", id);
					map.put("XM", xm);
					map.put("STATUS", status);
					map.put("HFSJ", sdf.format(hfsj));
					map.put("GGID", ggid);
					map.put("USERID", userid);
					map.put("LOCKSTATUS", lockstatus==null?"0":lockstatus);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getCxddFeedBackList(Long ggid,String userid){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM BD_VOTE_CXDDRCGZFK WHERE USERID=? AND TZGGID=?");
		List params = new ArrayList();
		params.add(userid);
		params.add(ggid);
		final String sql = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				List<Map> l = new ArrayList<Map>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> li = query.list();
				HashMap map;
				int n = 1;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String customername = (String) object[1];
					String customerno = (String) object[2];
					String username = (String) object[3];
					String userid = (String) object[4];
					BigDecimal tzggid = (BigDecimal) object[5];
					map.put("ID", id);
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					map.put("USERNAME", username);
					map.put("USERID", userid);
					map.put("TZGGID", tzggid);
					for (int i = 6; i <= 55; i++) {
						map.put("WT"+n,(String) object[i]);
						n++;
					}
					n=1;
					for (int i = 56; i <= 105; i++) {
						map.put("AS"+n,(String) object[i]);
						n++;
					}
					n=1;
					for (int i = 106; i <= 155; i++) {
						map.put("BZ"+n,(String) object[i]);
						n++;
					}
					n=1;
					for (int i = 156; i <= 205; i++) {
						map.put("PL"+n,(String) object[i]);
						n++;
					}
					n=1;
					for (int i = 206; i <= 255; i++) {
						map.put("PZ"+n,(String) object[i]);
						n++;
					}
					n=1;
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getFeedBackList(String ggid) {
		StringBuffer sb = new StringBuffer("select id,tzbt,to_char(zchfsj,'yyyy-MM-dd hh24:mi') zchfsj,tznr,xgzl,hfr,sftz,fsr,jszt,fszt,to_char(fssj,'yyyy-MM-dd hh24:mi') fssj from BD_XP_TZGGB where TZBT like '挂牌公司重大事项信息披露自查反馈表%' order by id desc");
		
		final String sql1 = sb.toString();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String tzbt = (String) object[1];
					String zchfsj = (String) object[2];// 提问时间
					String tznr = (String) object[3];
					String xgzl=(String)object[4];
					String  hfr= (String) object[5];
					String sftz = (String) object[6];
					String fsr=(String)object[7];
					String  jszt= (String) object[8];
					String  fszt= (String) object[9];
					String fssj = (String) object[10];// 发送时间
					map.put("ID", id);
					map.put("TZBT", tzbt);
					map.put("ZCHFSJ", zchfsj);
					map.put("TZNR", tznr);
					map.put("XGZL", xgzl);
					map.put("HFR", hfr);
					map.put("SFTZ", sftz);
					map.put("FSR", fsr);
					map.put("FSSJ", fssj);
					map.put("JSZT", jszt);
					map.put("FSZT", fszt);
					l.add(map);
				}
				return l;
			}
		});
	}

	
	
	public List<HashMap> getNoFeedBackList(final String ggid, String username, boolean isSuperMan,String zqdm, String status,String sszjj,String ssbm) {
		StringBuffer sb=new StringBuffer();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String name=uc.get_userModel().getUsername();
		List params = new ArrayList();
		sb.append("select distinct kh.zqjc,kh.zqdm,org.username,org.mobile,org.email,HF.customerno extend1,hf.status,");
		sb.append("gt.status gtstatus,");
		sb.append("b.LOCKSTATUS,gt.content gtcontent,kh.extend4 sszjj,kh.zcqx ssbm from");
		sb.append("(");
		sb.append("select distinct userid,ggid,status,customerno from bd_xp_hfqkb where ggid=?");
		params.add(ggid);
		sb.append(") hf left join bd_vote_wjdc b on b.customerno = hf.customerno and b.tzggid=hf.ggid");
		sb.append(" left join orguser org on org.userid = hf.userid");
		sb.append(" left join bd_vote_xpzcfkgtb gt ");
		sb.append(" on hf.ggid=gt.ggid  and hf.customerno=gt.customerno");
		sb.append(" left join bd_zqb_kh_base kh on kh.customerno = hf.customerno   where 1=1");
		List<OrgUserMap> userMap = UserContextUtil.getInstance().getCurrentUserContext().get_userMapList();
		List<String> checkList = new ArrayList<String>();
		for (OrgUserMap orgUserMap : userMap) {
			checkList.add(orgUserMap.getOrgroleid());
		}
		if(uc.get_userModel().getOrgroleid().equals(new Long(4))||checkList.contains("4")){
			sb.append(" and  kh.zqdm in( select bzkb.zqdm from bd_zqb_kh_base bzkb where bzkb.customerno in(select s.khbh from bd_mdm_khqxglb s where s.khfzr=? or s.zzcxdd=? or s.zzspr=? or s.fhspr=? or s.cwscbfzr2=? or s.cwscbfzr3=?  or s.GGFBR=? or s.qynbrysh=? ) )"); 
			params.add(username);
			params.add(username);
			params.add(username);
			params.add(username);
			params.add(username);
			params.add(username);
			params.add(username);
			params.add(username);
		}
		if(zqdm!=null&&!"".equals(zqdm)){
			sb.append(" and kh.zqdm like ? ");
			params.add("%"+zqdm+"%");
		}			
		if(status!=null&&!"".equals(status)){
			sb.append(" and hf.status=?");
			params.add(status);
		}
		if(sszjj!=null&&!"".equals(sszjj)){
			sb.append(" and kh.extend4 like ?");
			params.add("%"+sszjj+"%");
		}
		if(ssbm!=null&&!"".equals(ssbm)){
			sb.append(" and kh.zcqx like ?");
			params.add("%"+ssbm+"%");
		}
		sb.append(" order by hf.status desc,kh.zqdm");
	
		final String sql1 = sb.toString();
		final List param = params;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					String zqjc = (String) object[0];
					String zqdm = (String) object[1];// 提问时间
					String username = (String) object[2];// 提问时间
					String mobile = (String) object[3];
					String email=(String)object[4];
					String customerno=(String)object[5];
					String status=(String)object[6];
					String gtstatus=(String)object[7];
					String lockstatus = (String)object[8];
					String gtcontent = (String)object[9];
					String sszjj1 = (String)object[10];
					String ssbm1 = (String)object[11];
					map.put("ZQJC", zqjc);
					map.put("ZQDM", zqdm);
					map.put("USERNAME", username);
					map.put("TEL", mobile==null || mobile.equals("")?"":mobile);
					map.put("EMAIL", email==null || email.equals("")?"":email);
					map.put("CUSTOMERNO", customerno);
					map.put("GTCONTENT", gtcontent);
					map.put("STATUS", "未回复".equals(status)?"未反馈":"已回复".equals(status)?"已反馈":"");
					map.put("GTSTATUS", "".equals(gtstatus)?"":"已确认沟通".equals(gtstatus)?"已确认沟通":"");
					map.put("LOCKSTATUS", lockstatus==null?"0":lockstatus);
					map.put("SSZJJ", sszjj1==null?"":sszjj1);
					map.put("SSBM", ssbm1==null?"":ssbm1);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<String> getCustomernoList(String username) {
		StringBuffer sb = new StringBuffer("select kh.customerno,kh.customername from bd_mdm_khqxglb fp left join bd_zqb_kh_base kh on fp.khbh=kh.customerno where KHFZR like ? or GGFBR like ? or ZZCXDD like ? or FHSPR like ? or ZZSPR like ?");
		final String sql1 = sb.toString();
		List l= new ArrayList();
		DBUtilInjection d=new DBUtilInjection();
		
			if(username!=null && !"".equals(username)){
				
				if(d.HasInjectionData(username)){
					return l;
				}
				}
			
		final String final_username = username;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setParameter(0, "%"+final_username+"%");
				query.setParameter(1, "%"+final_username+"%");
				query.setParameter(2, "%"+final_username+"%");
				query.setParameter(3, "%"+final_username+"%");
				query.setParameter(4, "%"+final_username+"%");
				List<String> l = new ArrayList<String>();
				List<Object[]> li = query.list();
				for (Object[] object : li) {
					String customerno = (String) object[0];
					l.add(customerno);
				}
				return l;
			}
		});
	}

	public int getNoFeedBackListSize1(String ggid, String username,boolean isSuperMan,String zqdm,String sszjj,String ssbm) {
		int count=0;
		StringBuffer sb=new StringBuffer();
		Map params = new HashMap();
		int n = 1;
		if(isSuperMan){
			sb.append("select count(*) count from (select distinct kh.zqjc,kh.zqdm,org.username,org.mobile,org.email,HF.customerno extend1,hf.status,gt.status gtstatus,b.LOCKSTATUS,kh.extend4,kh.zcqx from");
			sb.append("(");
			sb.append("select distinct userid,ggid,status,customerno from bd_xp_hfqkb where ggid= ? ");
			params.put(n, ggid);
			n++;
			sb.append(") hf left join bd_vote_wjdc b on b.customerno = hf.customerno and b.tzggid=hf.ggid");
			sb.append(" left join orguser org on org.userid = hf.userid");
			sb.append(" left join bd_vote_xpzcfkgtb gt ");
			sb.append(" on hf.ggid=gt.ggid  and hf.customerno=gt.customerno");
			sb.append(" left join bd_zqb_kh_base kh on kh.customerno = hf.customerno   where 1=1 ");
			if(zqdm!=null&&!"".equals(zqdm)){
				sb.append(" and kh.zqdm like ? ");
				params.put(n, "%"+zqdm+"%");
				n++;
			}
			if(sszjj!=null&&!"".equals(sszjj)){
				sb.append(" and kh.extend4 like ? ");
				params.put(n, "%"+sszjj+"%");
				n++;
			}
			if(ssbm!=null&&!"".equals(ssbm)){
				sb.append(" and kh.zcqx like ? ");
				params.put(n, "%"+ssbm+"%");
				n++;
			}
			sb.append(" and hf.status='未回复')");
		}else{//			
			sb.append("select count(*) count from (select distinct kh.zqjc,kh.zqdm,org.username,org.mobile,org.email,HF.customerno extend1,hf.status,gt.status gtstatus,b.LOCKSTATUS,kh.extend4,kh.zcqx  from");
			sb.append("(");
			sb.append("select distinct userid,ggid,status,customerno from bd_xp_hfqkb where ggid= ? ");
			params.put(n, ggid);
			n++;
			sb.append(") hf left join bd_vote_wjdc b on b.customerno = hf.customerno and b.tzggid=hf.ggid");
			sb.append(" left join orguser org on org.userid = hf.userid");
			sb.append(" left join bd_vote_xpzcfkgtb gt ");
			sb.append(" on hf.ggid=gt.ggid  and hf.customerno=gt.customerno");			
			sb.append(" left join bd_zqb_kh_base kh on kh.customerno = hf.customerno ");
			sb.append(" left join bd_mdm_khqxglb fp on fp.khbh=kh.customerno where 1=1 ");
			if(zqdm!=null&&!"".equals(zqdm)){
				sb.append(" and kh.zqdm like ? ");
				params.put(n, "%"+zqdm+"%");
				n++;
			}
			if(sszjj!=null&&!"".equals(sszjj)){
				sb.append(" and kh.extend4 like ? ");
				params.put(n, "%"+sszjj+"%");
				n++;
			}
			if(ssbm!=null&&!"".equals(ssbm)){
				sb.append(" and kh.zcqx like ? ");
				params.put(n, "%"+ssbm+"%");
				n++;
			}
			sb.append(" and (     khfzr = ?    or zzcxdd = ?  or zzspr = ?  or fhspr =?   or cwscbfzr2 =?   or cwscbfzr3 = ?  or GGFBR =?  or qynbrysh=? )");
			params.put(n, username);
			n++;
			params.put(n, username);
			n++;
			params.put(n, username);
			n++;
			params.put(n, username);
			n++;
			params.put(n, username);
			n++;
			params.put(n, username);
			n++;
			params.put(n, username);
			n++;
			params.put(n, username);
			n++;
			sb.append(" and hf.status='未回复')");
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			List<OrgUserMap> userMap = uc.get_userMapList();
			List<String> checkList = new ArrayList<String>();
			for (OrgUserMap orgUserMap : userMap) {
				checkList.add(orgUserMap.getOrgroleid());
			}
			/*if(uc.get_userModel().getOrgroleid().equals(new Long(4))||checkList.contains("4")){
				sb.append(" and  kh.zqdm in( select bzkb.zqdm from bd_zqb_kh_base bzkb where bzkb.customerno in(select s.khbh from bd_mdm_khqxglb s where s.khfzr=? or s.zzcxdd=? or s.zzspr=? or s.fhspr=? or s.cwscbfzr2=? or s.cwscbfzr3=? ) ))"); 
				params.put(n, username);n++;
				params.put(n, username);n++;
				params.put(n, username);n++;
				params.put(n, username);n++;
				params.put(n, username);n++;
				params.put(n, username);n++;
			}*/
		}

		String sql=sb.toString();
		count = DBUTilNew.getInt("count",sql.toString(),params);
		return count;
	}
	
	public List<HashMap> getBondList(String sql,List params) {
		//final int pageSize1 = pageSize;
		//final int startRow1 = (pageNumber - 1) * pageSize;
		final String sql1 = sql;
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				//query.setFirstResult(startRow1);
				//query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String zqmc = (String) object[1];
					String jc = (String) object[2];
					String fxrmc = (String) object[3];
					String fxrssd = (String) object[4];
					String fxsc = (String) object[5];
					String fxfs = (String) object[6];
					String zqfzbm = (String) object[7];
					String zqzbr = (String) object[8];
					String zqllsm = (String) object[10];
					BigDecimal fxjg = (BigDecimal) object[11];
					String chfs = (String) object[12];
					String fxsm = (String) object[13];
					String xyjb = (String) object[14];
					String createuser = (String) object[16];
					String createuserid = (String) object[17];
					BigDecimal instanceid = (BigDecimal) object[19];
					BigDecimal num = (BigDecimal) object[23];
					String fxsj = (Date) object[24]==null?"":sdf.format((Date) object[24]);
					String dffxggytz = (Date) object[25]==null?"":sdf.format((Date) object[25]);
					String jyszqdffx = (Date) object[26]==null?"":sdf.format((Date) object[26]);
					String zjlsqkhz = (Date) object[27]==null?"":sdf.format((Date) object[27]);
					String hksj = (Date) object[28]==null?"":sdf.format((Date) object[28]);
					map.put("ID", id.toString());
					map.put("ZQMC", zqmc);
					map.put("JC", jc);
					map.put("FXRMC", fxrmc);
					map.put("FXRSSD", fxrssd);
					map.put("FXSC", fxsc);
					map.put("FXFS", fxfs);
					map.put("ZQFZBM", zqfzbm);
					map.put("ZQZBR", zqzbr);
					map.put("ZQLLSM", zqllsm);
					map.put("FXJG", fxjg);
					map.put("CHFS", chfs);
					map.put("FXSM", fxsm);
					map.put("XYJB", xyjb);
					map.put("CREATEUSER", createuser);
					map.put("CREATEUSERID", createuserid);
					map.put("INSTANCEID", instanceid.toString());
					map.put("NUM", num);
					map.put("FXSJ", fxsj);
					map.put("DFFXGGYTZ", dffxggytz);
					map.put("JYSZQDFFX", jyszqdffx);
					map.put("ZJLSQKHZ", zjlsqkhz);
					map.put("HKSJ", hksj);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getBondListSize(String sql,List params) {
		final String sql1 = sql;
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				//HashMap map;
				for (Object[] object : list) {
					//map = new HashMap();
					l.add(null);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getBondSubData(String sql,List params){
		final String sql1 = sql;
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal chzqbjdybl = (BigDecimal) object[0];
					String fxsj = (Date) object[1]==null?"":sdf.format((Date) object[1]);
					BigDecimal instanceid = (BigDecimal) object[2];
					String dffxggytz = (Date) object[3]==null?"":sdf.format((Date) object[3]);
					String jyszqdffx = (Date) object[4]==null?"":sdf.format((Date) object[4]);
					String zjlsqkhz = (Date) object[5]==null?"":sdf.format((Date) object[5]);
					String hksj = (Date) object[6]==null?"":sdf.format((Date) object[6]);
					map.put("FXSJ", fxsj);
					map.put("CHZQBJDYBL", chzqbjdybl);
					map.put("INSTANCEID", instanceid.toString());
					map.put("DFFXGGYTZ", dffxggytz);
					map.put("JYSZQDFFX", jyszqdffx);
					map.put("ZJLSQKHZ", zjlsqkhz);
					map.put("HKSJ", hksj);
					l.add(map);
					
				}
				return l;
			}
		});
	}

	public String zqbFzgsCwbb(String khbh, String id) {
		StringBuffer content = new StringBuffer();
		content.append("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" aria-labelledby=\"gbox_subformSUBFORM_CWBBBD\" role=\"grid\" style=\"width:100%\" class=\"ui-jqgrid-htable\">"
						+ "<thead>"
							+ "<tr role=\"rowheader\" class=\"ui-jqgrid-labels\">"
								/*+ "<th class=\"ui-state-default ui-th-column ui-th-ltr\" role=\"columnheader\" id=\"subformSUBFORM_CWBBBD_rn\" style=\"width: 25px;\">"
									+ "<div id=\"jqgh_subformSUBFORM_CWBBBD_rn\">"
										+ "<span style=\"display:none\" class=\"s-ico\">"
											+ "<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
											+ "<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
										+ "</span>"
									+ "</div>"
								+ "</th>"
								+ "<th class=\"ui-state-default ui-th-column ui-th-ltr\" role=\"columnheader\" id=\"subformSUBFORM_CWBBBD_cb\" style=\"width: 20px;\">"
									+ "<div id=\"jqgh_subformSUBFORM_CWBBBD_cb\">"
										+ "<input type=\"checkbox\" class=\"cbox\" id=\"cb_subformSUBFORM_CWBBBD\" role=\"checkbox\">"
										+ "<span style=\"display:none\" class=\"s-ico\">"
											+ "<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
											+ "<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
										+ "</span>"
									+ "</div>"
								+ "</th>"*/
								+ "<th class=\"ui-state-default ui-th-column ui-th-ltr\" role=\"columnheader\" id=\"subformSUBFORM_CWBBBD_BBMC\" style=\"width: 18%;\">"
									+ "<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor: col-resize;\">&nbsp;</span>"
									+ "<div id=\"jqgh_subformSUBFORM_CWBBBD_BBMC\" class=\"ui-jqgrid-sortable\">报表名称"
										+ "<span style=\"display:none\" class=\"s-ico\">"
											+ "<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
											+ "<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
										+ "</span>"
									+ "</div>"
								+ "</th>"
								+ "<th class=\"ui-state-default ui-th-column ui-th-ltr\" role=\"columnheader\" id=\"subformSUBFORM_CWBBBD_SCSJ\" style=\"width: 18%;\">"
									+ "<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor: col-resize;\">&nbsp;</span>"
									+ "<div id=\"jqgh_subformSUBFORM_CWBBBD_SCSJ\" class=\"ui-jqgrid-sortable\">上传时间"
										+ "<span style=\"display:none\" class=\"s-ico\">"
											+ "<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
											+ "<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
										+ "</span>"
									+ "</div>"
								+ "</th>"
								+ "<th class=\"ui-state-default ui-th-column ui-th-ltr\" role=\"columnheader\" id=\"subformSUBFORM_CWBBBD_SCR\" style=\"width: 18%;\">"
									+ "<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor: col-resize;\">&nbsp;</span>"
									+ "<div id=\"jqgh_subformSUBFORM_CWBBBD_SCR\" class=\"ui-jqgrid-sortable\">上传人"
										+ "<span style=\"display:none\" class=\"s-ico\">"
											+ "<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
											+ "<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
										+ "</span>"
									+ "</div>"
								+ "</th>"
								+ "<th class=\"ui-state-default ui-th-column ui-th-ltr\" role=\"columnheader\" id=\"subformSUBFORM_CWBBBD_SFHB\" style=\"width: 18%;\">"
									+ "<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor: col-resize;\">&nbsp;</span>"
									+ "<div id=\"jqgh_subformSUBFORM_CWBBBD_SFHB\" class=\"ui-jqgrid-sortable\">是否合并"
										+ "<span style=\"display:none\" class=\"s-ico\">"
											+ "<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
											+ "<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
										+ "</span>"
									+ "</div>"
								+ "</th>"
								/*+ "<th class=\"ui-state-default ui-th-column ui-th-ltr\" role=\"columnheader\" id=\"subformSUBFORM_CWBBBD_CZ\" style=\"width: 120px;\">"
									+ "<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor: col-resize;\">&nbsp;</span>"
									+ "<div id=\"jqgh_subformSUBFORM_CWBBBD_CZ\" class=\"ui-jqgrid-sortable\">财务报表"
										+ "<span style=\"display:none\" class=\"s-ico\">"
											+ "<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
											+ "<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
										+ "</span>"
									+ "</div>"
								+ "</th>"*/
								+ "<th class=\"ui-state-default ui-th-column ui-th-ltr\" role=\"columnheader\" id=\"subformSUBFORM_CWBBBD_ID\" style=\"width: 10%;\">"
									+ "<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor: col-resize;\">&nbsp;</span>"
									+ "<div id=\"jqgh_subformSUBFORM_CWBBBD_ID\" class=\"ui-jqgrid-sortable\">操作"
										+ "<span style=\"display:none\" class=\"s-ico\">"
											+ "<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
											+ "<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
										+ "</span>"
									+ "</div>"
								+ "</th>"
							+ "</tr>"
						+ "</thead>"
					+ "</table>");
		content.append(""
		+ "<div class=\"ui-jqgrid-bdiv\" style=\"height: 100%; width: 100%;\">"
			+ "<div style=\"position:relative;\">"
			+ "<div>"
			+ "</div>"
				+ "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" id=\"subformSUBFORM_CWBBBD\" tabindex=\"1\" role=\"grid\" aria-multiselectable=\"true\" aria-labelledby=\"gbox_subformSUBFORM_CWBBBD\" class=\"ui-jqgrid-btable\" style=\"width:100%;\">"
					+ "<tbody>"
					+ "<tr style=\"height:auto\" role=\"row\" class=\"jqgfirstrow\">"
						+ "<td style=\"height:0px;width:18%;\" role=\"gridcell\"></td>"
						+ "<td style=\"height:0px;width:18%;\" role=\"gridcell\"></td>"
						+ "<td style=\"height:0px;width:18%;\" role=\"gridcell\"></td>"
						+ "<td style=\"height:0px;width:18%;\" role=\"gridcell\"></td>"
						+ "<td style=\"height:0px;width:10%;\" role=\"gridcell\"></td>"
					+ "</tr>");
		StringBuffer sql = new StringBuffer("SELECT A.INSTANCEID,B.ID BID,B.BBMC,B.SCSJ,B.SCR,B.SFHB,B.CZ,B.KHBH,B.FZGSMC FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_CWBB')) A LEFT JOIN BD_ZQB_CWBB B ON A.DATAID = B.ID WHERE 1=1 AND B.DELETESTATUS <> '1' AND B.ID IS NOT NULL AND B.KHBH=? AND B.FZGSID=?");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, khbh);
			ps.setString(2, id);
			rs = ps.executeQuery();
			while(rs.next()){
				content.append("<tr class=\"ui-widget-content jqgrow ui-row-ltr\" role=\"row\" tabindex=\"-1\">"
						+ "<td aria-describedby=\"subformSUBFORM_CWBBBD_BBMC\" title=\""+rs.getString("BBMC")+"\" role=\"gridcell\" style=\"width:18%;\">"+rs.getString("BBMC")+"</td>"
						+ "<td aria-describedby=\"subformSUBFORM_CWBBBD_SCSJ\" title=\""+rs.getString("SCSJ").substring(0, 10)+"\" role=\"gridcell\" style=\"width:18%;\">"+rs.getString("SCSJ").substring(0, 10)+"</td>"
						+ "<td aria-describedby=\"subformSUBFORM_CWBBBD_SCR\" title=\""+rs.getString("SCR")+"\" role=\"gridcell\" style=\"width:18%;\">"+rs.getString("SCR")+"</td>"
						+ "<td aria-describedby=\"subformSUBFORM_CWBBBD_SFHB\" title=\""+rs.getString("SFHB")+"\" role=\"gridcell\" style=\"width:18%;\">"+rs.getString("SFHB")+"</td>"
						+ "<td aria-describedby=\"subformSUBFORM_CWBBBD_KHBH\" title=\"\" role=\"gridcell\" style=\"width:10%;\">");
						if(rs.getString("CZ") == null || rs.getString("CZ").equals("")){
							content.append("<a href=\"javascript:upload("+rs.getString("INSTANCEID")+")\">上传</a>");
						}else{
							content.append("<a href=\"javascript:check("+rs.getString("INSTANCEID")+")\">查看</a>");
						}
						content.append("&nbsp;|&nbsp;");
						content.append("<a href=\"javascript:deleteThis("+rs.getString("BID")+")\">删除</a>");
						content.append("</td>"
						+ "</tr>");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
			content.append(
					"</tbody>"
							+ "</table>"
							+ "</div>"
							+ "</div>");
		}
		return content.toString();
	}

	public String zqbGlfGetThisData(String gsmc, String khbh) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID,GSMC,GSLX,ZCH,ZCZB,SCJYFW,ZCDZ,FRDB,JJXZ,GLGX,BZXX,CGBL,KHBH,KHMC FROM BD__FZGSGLZB WHERE ID IN (SELECT MAX(ID) FROM BD__FZGSGLZB WHERE GSMC=? AND KHBH=?)");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, gsmc);
			ps.setString(2, khbh);
			rs = ps.executeQuery();
			while(rs.next()){
				sb.append("FZGSID-"+(rs.getString("ID")==null?"":rs.getString("ID"))+",");
				sb.append("GSMC-"+(rs.getString("GSMC")==null?"":rs.getString("GSMC"))+",");
				sb.append("ZCH-"+(rs.getString("ZCH")==null?"":rs.getString("ZCH"))+",");
				sb.append("ZCDZ-"+(rs.getString("ZCDZ")==null?"":rs.getString("ZCDZ"))+",");
				sb.append("JJXZ-"+(rs.getString("JJXZ")==null?"":rs.getString("JJXZ"))+",");
				sb.append("CGBL-"+(rs.getString("CGBL")==null?"":rs.getString("CGBL"))+",");
				sb.append("ZCZB-"+(rs.getString("ZCZB")==null?"":rs.getString("ZCZB"))+",");
				sb.append("FRDB-"+(rs.getString("FRDB")==null?"":rs.getString("FRDB"))+",");
				sb.append("GLGX-"+(rs.getString("GLGX")==null?"":rs.getString("GLGX"))+",");
				//radio
				sb.append("GSLX-"+(rs.getString("GSLX")==null?"":rs.getString("GSLX"))+",");
				//textarea
				sb.append("SCJYFW-"+(rs.getString("SCJYFW")==null?"":rs.getString("SCJYFW"))+",");
				sb.append("BZXX-"+(rs.getString("BZXX")==null?"":rs.getString("BZXX")));
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return sb.toString();
	}
	
	public String zqbFzgsGetThisData(String gsmc, String khbh) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID,GSMC,GSLX,ZCH,ZCZB,SCJYFW,ZCDZ,FRDB,JJXZ,GLGX,BZXX,CGBL,KHBH,KHMC FROM BD_ZQB_GLF WHERE ID IN (SELECT MAX(ID) FROM BD_ZQB_GLF WHERE GSMC=? AND KHBH=?)");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, gsmc);
			ps.setString(2, khbh);
			rs = ps.executeQuery();
			while(rs.next()){
				sb.append("GLFID-"+(rs.getString("ID")==null?"":rs.getString("ID"))+",");
				sb.append("GSMC-"+(rs.getString("GSMC")==null?"":rs.getString("GSMC"))+",");
				sb.append("ZCH-"+(rs.getString("ZCH")==null?"":rs.getString("ZCH"))+",");
				sb.append("ZCDZ-"+(rs.getString("ZCDZ")==null?"":rs.getString("ZCDZ"))+",");
				sb.append("JJXZ-"+(rs.getString("JJXZ")==null?"":rs.getString("JJXZ"))+",");
				sb.append("CGBL-"+(rs.getString("CGBL")==null?"":rs.getString("CGBL"))+",");
				sb.append("ZCZB-"+(rs.getString("ZCZB")==null?"":rs.getString("ZCZB"))+",");
				sb.append("FRDB-"+(rs.getString("FRDB")==null?"":rs.getString("FRDB"))+",");
				sb.append("GLGX-"+(rs.getString("GLGX")==null?"":rs.getString("GLGX"))+",");
				//radio
				sb.append("GSLX-"+(rs.getString("GSLX")==null?"":rs.getString("GSLX"))+",");
				//textarea
				sb.append("SCJYFW-"+(rs.getString("SCJYFW")==null?"":rs.getString("SCJYFW"))+",");
				sb.append("BZXX-"+(rs.getString("BZXX")==null?"":rs.getString("BZXX")));
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return sb.toString();
	}
	
	public List<HashMap> getDustbinList(String khbh, int pageSize,
			int pageNumber, String noticename, String noticetype,
			String startdate, String enddate,String bzlx) {
		if(startdate!=null && enddate!=null && !"".equals(startdate) && !"".equals(enddate)){
			try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date Date1 = format.parse(startdate);
				java.util.Date Date2 = format.parse(enddate);
				int ct=Date1.compareTo(Date2);
				if(ct>0){
					return null;
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
		}
		List params = new ArrayList();
		List l=new ArrayList();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if("".equals(khbh)||khbh==null){
			return l;
		}
		final String userid = uc.get_userModel().getUserid();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		StringBuffer sql = new StringBuffer("select instanceid,noticename,noticetype,to_char(noticedate,'yyyyMMdd') noticedate," +
				"createname,to_char(createdate,'yyyyMMdd') createdate,a.id,LCBH,LCBS,YXID,RWID,STEPID,NOTICETEXT,SPZT,PRCID,MEETID,MEETNAME,NOTICEFILE,"
				
				+ "A.ZQJCXS,A.CREATEID  from BD_XP_GGLJX a "
				
				+ "where 1=1 ");
		if(khbh!=null&&!khbh.equals("")){
			sql.append(" and a.khbh=?");
			params.add(khbh);
		}
		/*if(bzlx!=null&&!bzlx.equals("")){
			sql.append(" and c.xpsxname like '%" + bzlx + "%'");
		}*/
		if (noticename != null && !"".equals(noticename)) {
			sql.append(" and a.noticename like ?");
			params.add("%" + noticename + "%");
		}
		if (startdate != null && !"".equals(startdate)) {
			sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')>= ?");
			params.add(startdate);
		}
		if (noticetype != null && !"".equals(noticetype)) {
			sql.append(" and a.noticetype = ?");
			params.add(noticetype);
		}
		if (enddate != null && !"".equals(enddate)) {
			sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')<= ?");
			params.add(enddate);
		}
		sql.append(" order by a.id desc");
		final String sql1 = sql.toString();
		final String DDLC_UUID=ProcessAPI.getInstance().getProcessActDefId("CXDDYFQLC");
		final String DMLC_UUID= ProcessAPI.getInstance().getProcessActDefId("GGSPLC");
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal instanceid = (BigDecimal) object[0];
					String noticename = (String) object[1];
					String noticetype = (String) object[2];
					String noticedate = (String) object[3];
					String createname=(String)object[4];
					String  createdate= (String) object[5];
					BigDecimal id = (BigDecimal) object[6];
					String lcbh=(String)object[7];
					String  lcbs= (String) object[8];
					String  yxid= (String) object[9];
					String  rwid= (String) object[10];
					String stepid=(String) object[11];
					String noticetext=(String) object[12];
					String spzt=(String) object[13];
					String prcid=(String) object[14];
					String meetid=(String) object[15];
					String meetname=(String) object[16];
					String noticefile=(String) object[17];
					String zqjcxs=(String) object[18];
					String createid=(String) object[19];
					if(createid!=null||"".equals(createid)){
					Long orgroleid = UserContextUtil.getInstance().getUserContext(createid).get_userModel().getOrgroleid();
					map.put("SCROLEID",orgroleid);
					}else{
						map.put("SCROLEID",null);
					}
					map.put("INSTANCEID", instanceid.toString());
					map.put("NOTICENAME", noticename);
					map.put("NOTICETYPE", noticetype);
					map.put("NOTICEDATE", noticedate);
					map.put("CREATENAME", createname);
					map.put("CREATEDATE", noticedate);
					map.put("NOTICETEXT", noticetext);
					map.put("ID", id);
					map.put("LCBH", lcbh);
					map.put("LCBS", lcbs);
					map.put("YXID", yxid);
					map.put("RWID", rwid);
					map.put("STEPID", stepid);
					map.put("SPZT", spzt);
					map.put("MEETID", meetid);
					map.put("MEETNAME", meetname);
					map.put("NOTICEFILE", noticefile);
					map.put("ZQJCXS", zqjcxs);
					
					if(lcbs!=null&&!lcbs.equals("")){ 
						if(lcbh!=null){
							try{
						List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(lcbh, Long.parseLong(lcbs));
					    if(pro!=null&pro.size()>0){
					    	Long prc=pro.get(0).getPrcDefId();
					    	map.put("PRCID", prc);
					    }
							}catch(Exception e){
								logger.error(e,e);
							}
						}
					}
					HashMap smjmap=new HashMap();
					smjmap.put("GGINS", instanceid);
					List<HashMap> list=DemAPI.getInstance().getList(GGSMJ_UUID, smjmap, null);
					String smjins="";
					if(list.size()>0){
						smjins=list.get(0).get("INSTANCEID").toString();
						map.put("ISSMJINS", true);
					}else{
						map.put("ISSMJINS", false);
					}
					map.put("SMJINS", smjins);//扫描件instanceid	
					l.add(map);
				}
				return l;
			}
		});
	}

	public int getDustbinListSize(String khbh, String noticename,
			String noticetype, String startdate, String enddate,String bzlx) {
		int count = 0;
		if(startdate!=null && enddate!=null && !"".equals(startdate) && !"".equals(enddate)){
		    try {
		    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    	java.util.Date Date1 = format.parse(startdate);
				java.util.Date Date2 = format.parse(enddate);
				int ct=Date1.compareTo(Date2);
				if(ct>0){
					return count;
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return count;
			}
		}
		TreeMap<String, String> parameterMap=new TreeMap<String, String>();
		if("".equals(khbh)||khbh==null){
			return count;
		}
		StringBuffer sql =new StringBuffer( "select count(*) as count FROM BD_XP_GGLJX a "
			
				+ "where 1=1 ");
		if(khbh!=null&&!khbh.equals("")){
			sql.append(" and a.khbh=?");
			parameterMap.put("KHBH", khbh);
		}
	
		if (noticename != null && !"".equals(noticename)) {
			sql.append(" and a.noticename like ?");
			parameterMap.put("NOTICENAME", noticename);
		}
		if (noticetype != null && !"".equals(noticetype)) {
			sql.append(" and a.noticetype like ?");
			parameterMap.put("NOTICETYPE", noticetype);
		}
		if (startdate != null && !"".equals(startdate)) {
			sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')>= ?");
			parameterMap.put("STARTDATE", startdate);
		}
		if (enddate != null && !"".equals(enddate)) {
			sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')<= ?");
			parameterMap.put("ENDDATE", enddate);
		}
		List list = new ArrayList();
		int c = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			int whileCount=1;
			Iterator<String> iterator = parameterMap.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				if(key.equals("NOTICETYPE")||key.equals("NOTICENAME")||key.equals("XPSXNAME")){
					stmt.setString(whileCount, "%"+parameterMap.get(key)+"%");
				}else{
					stmt.setString(whileCount, parameterMap.get(key));
				}
				whileCount++;
			}
			rset = stmt.executeQuery();
			rset.next();
			count = rset.getInt("count");
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return count;
	}

	public List<HashMap> zqbGlfGgGlf(String khbh,String gsmc,String gslx,String zch,String zcdz,int pageNumber,int pageSize) {
		List<HashMap> list = new ArrayList<HashMap>();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT ID,GSMC,GSLX,ZCH,ZCZB,SCJYFW,ZCDZ,FRDB,JJXZ,GLGX,BZXX,CGBL,KHMC,FZGSID FROM BD_ZQB_GLF WHERE 1=1");
		
		sb.append(" AND KHBH=?");
		params.add(khbh);
		
		if(gsmc!=null&&!gsmc.equals("")){
			sb.append(" AND GSMC LIKE ?");
			params.add("%"+gsmc+"%");
		}
		if(gslx!=null&&!gslx.equals("")){
			sb.append(" AND GSLX=?");
			params.add(gslx);
		}
		if(zch!=null&&!zch.equals("")){
			sb.append(" AND ZCH LIKE ?");
			params.add("%"+zch+"%");
		}
		if(zcdz!=null&&!zcdz.equals("")){
			sb.append(" AND ZCDZ LIKE ?");
			params.add("%"+zcdz+"%");
		}
		final String sql = sb.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal)object[0];
					String GSMC =(String) object[1];
					String GSLX =(String) object[2];
					String ZCH =(String) object[3];
					BigDecimal ZCZB =(BigDecimal) object[4];
					String SCJYFW =(String) object[5];
					String ZCDZ =(String) object[6];
					String FRDB =(String) object[7];
					String JJXZ =(String) object[8];
					String GLGX =(String) object[9];
					String BZXX =(String) object[10];
					BigDecimal CGBL =(BigDecimal) object[11];
					String KHMC =(String) object[12];
					String FZGSID =(String) object[13];
					map.put("ID", ID);
					map.put("GSMC", GSMC);
					map.put("GSLX", GSLX);
					map.put("ZCH", ZCH);
					map.put("ZCZB", ZCZB);
					map.put("SCJYFW", SCJYFW);
					map.put("ZCDZ", ZCDZ);
					map.put("FRDB", FRDB);
					map.put("JJXZ", JJXZ);
					map.put("GLGX", GLGX);
					map.put("BZXX", BZXX);
					map.put("CGBL", CGBL);
					map.put("KHMC", KHMC);
					map.put("FZGSID", FZGSID);
					l.add(map);
				}
				return l;
			}
		});		
	}

	public List<HashMap> sysdemMdmXpzcwt() {
		List<HashMap> list = new ArrayList<HashMap>();
		StringBuffer sb = new StringBuffer("SELECT BZKI.ID,BZKI.QUESTION,BZKI.DEFULTANSWER,BZKI.STATUS,BZKI.SORTID,SYSDA.INSTANCEID,BZKI.TYPE FROM BD_VOTE_WJDCWTB BZKI LEFT JOIN (SELECT MAX(INSTANCEID) AS INSTANCEID,MAX(DATAID) AS DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单') GROUP BY DATAID) SYSDA ON BZKI.ID=SYSDA.DATAID WHERE BZKI.TYPE IN('信披自查','督导反馈','知悉问题') ORDER BY BZKI.SORTID");
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal)object[0];
					String QUESTION =(String) object[1];
					String DEFULTANSWER =(String) object[2];
					String STATUS =(String) object[3];
					BigDecimal SORTID =(BigDecimal) object[4];
					BigDecimal INSTANCEID = (BigDecimal)object[5];
					String TYPE=(String)object[6];
					map.put("ID", ID);
					map.put("QUESTION", QUESTION);
					map.put("DEFULTANSWER", DEFULTANSWER);
					map.put("STATUS", STATUS);
					map.put("SORTID", SORTID);
					map.put("INSTANCEID", INSTANCEID==null?"0":INSTANCEID.toString());
					map.put("TYPE", TYPE.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> sysdemMdmXpzcwtSize() {
		List<HashMap> list = new ArrayList<HashMap>();
		StringBuffer sb = new StringBuffer("SELECT BZKI.ID,BZKI.QUESTION,BZKI.DEFULTANSWER,BZKI.STATUS,BZKI.SORTID,SYSDA.INSTANCEID FROM BD_VOTE_WJDCWTB BZKI LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '信披自查问题表单')) SYSDA ON BZKI.ID=SYSDA.DATAID ORDER BY BZKI.SORTID");
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal)object[0];
					String QUESTION =(String) object[1];
					String DEFULTANSWER =(String) object[2];
					String STATUS =(String) object[3];
					BigDecimal SORTID =(BigDecimal) object[4];
					BigDecimal INSTANCEID = (BigDecimal)object[5];
					map.put("ID", ID);
					map.put("QUESTION", QUESTION);
					map.put("DEFULTANSWER", DEFULTANSWER);
					map.put("STATUS", STATUS);
					map.put("SORTID", SORTID);
					map.put("INSTANCEID", INSTANCEID.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> sysdemMdmXmjdzl() {
		List<HashMap> list = new ArrayList<HashMap>();
		StringBuffer sb = new StringBuffer("SELECT BZKI.ID,BZKI.JDBH,BZKI.JDMC,BZKI.CJRZH,BZKI.CJRXM,BZKI.CJSJ,BZKI.CONTENT,BZKI.SXZLQD,BZKI.SFKRZ,BZKI.SORTID,SYSDA.INSTANCEID FROM BD_ZQB_KM_INFO BZKI LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单')) SYSDA ON BZKI.ID=SYSDA.DATAID WHERE BZKI.STATE=1 ORDER BY BZKI.SORTID");
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				//query.setFirstResult(startRow1);
				//query.setMaxResults(pageSize1);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal)object[0];
					String JDBH =(String) object[1];
					String JDMC =(String) object[2];
					String CJRZH =(String) object[3];
					String CJRXM =(String) object[4];
					Date CJSJ =(Date) object[5];
					String CONTENT =(String) object[6];
					String SXZLQD =(String) object[7];
					BigDecimal SFKRZ =(BigDecimal) object[8];
					BigDecimal SORTID =(BigDecimal) object[9];
					BigDecimal INSTANCEID = (BigDecimal)object[10];
					map.put("ID", ID.toString());
					map.put("JDBH", JDBH);
					map.put("JDMC", JDMC);
					map.put("CJRZH", CJRZH);
					map.put("CJRXM", CJRXM);
					map.put("CJSJ", CJSJ);
					map.put("CONTENT", CONTENT);
					map.put("SXZLQD", SXZLQD);
					map.put("SFKRZ", SFKRZ);
					map.put("SORTID", SORTID);
					map.put("INSTANCEID", INSTANCEID.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> sysdemMdmXmlx() {
		List<HashMap> list = new ArrayList<HashMap>();
		StringBuffer sb = new StringBuffer("SELECT BZKI.ID,BZKI.LXMC,SYSDA.INSTANCEID FROM BD_ZQB_XMLXB BZKI LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目类型')) SYSDA ON BZKI.ID=SYSDA.DATAID");
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				//query.setFirstResult(startRow1);
				//query.setMaxResults(pageSize1);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal)object[0];
					String LXMC =(String) object[1];
					BigDecimal INSTANCEID = (BigDecimal)object[2];
					map.put("ID", ID);
					map.put("LXMC", LXMC);
					map.put("INSTANCEID", INSTANCEID.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> sysdemMdmTyxmjdzl(String xmlx) {
		List<HashMap> list = new ArrayList<HashMap>();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT BZKI.ID,BZKI.XMLX,BZKI.JDMC,BZKI.CJRZH,BZKI.CJRXM,BZKI.CJSJ,BZKI.CONTENT,BZKI.SXZLQD,SYSDA.INSTANCEID FROM BD_ZQB_TYXM_INFO BZKI LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单')) SYSDA ON BZKI.ID=SYSDA.DATAID WHERE 1=1 AND BZKI.STATE=1");
		if(xmlx!=null&&!xmlx.equals("")){
			sb.append(" AND BZKI.XMLX LIKE ?");
			params.add("%"+xmlx+"%");
		}
		sb.append(" ORDER BY BZKI.SORTID");
		final String sql = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				//query.setFirstResult(startRow1);
				//query.setMaxResults(pageSize1);
				List<Object[]> li = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal)object[0];
					String XMLX =(String) object[1];
					String JDMC =(String) object[2];
					String CJRZH =(String) object[3];
					String CJRXM =(String) object[4];
					Date CJSJ =(Date) object[5];
					String CONTENT =(String) object[6];
					String SXZLQD =(String) object[7];
					BigDecimal INSTANCEID = (BigDecimal)object[8];
					map.put("ID", ID);
					map.put("XMLX", XMLX);
					map.put("JDMC", JDMC);
					map.put("CJRZH", CJRZH);
					map.put("CJRXM", CJRXM);
					map.put("CJSJ", CJSJ);
					map.put("CONTENT", CONTENT);
					map.put("SXZLQD", SXZLQD);
					map.put("INSTANCEID", INSTANCEID.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getGZSCList(String tzbt,String fkzt, int pageNumber, int pageSize) {
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		List params = new ArrayList();
		String userid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
		Long orgroleid=UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getOrgroleid();
		StringBuffer sb = new StringBuffer("SELECT INSTANCEID,B.ID,B.FSRID,B.FSR,B.TZBT,B.TZNR,B.FSSJ FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查')) A LEFT JOIN BD_XP_GZSC B ON A.DATAID = B.ID WHERE  B.ID IS NOT NULL ");
		if(orgroleid!=5){
		sb.append("AND B.FSRID=? ");
		params.add(userid);
		}
		if(tzbt!=null&&!tzbt.equals("")){
			sb.append(" AND B.TZBT LIKE ?");
			params.add("%"+tzbt+"%");
		}
		if(fkzt!=null&&!fkzt.equals("")){
			sb.append(" AND B.FKZT=?");
			params.add(fkzt);
		}
		sb.append(" ORDER BY B.ID DESC");
		final String sql1 = sb.toString();
		final List param = params;
		
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> li = query.list();
				HashMap map;
				SimpleDateFormat sdf;
				for (Object[] object : li) {
					map = new HashMap();
					sdf = new SimpleDateFormat("yyyy-MM-dd");
					BigDecimal instanceid = (BigDecimal) object[0];
					BigDecimal cid = (BigDecimal) object[1];
					String tzbt = (String) object[4];
					String tznr = (String) object[5];
					Date fssj = (Date) object[6];
					map.put("CID", cid==null?"0":cid.toString());
					map.put("INSTANCEID", instanceid.toString());
					map.put("TZBT", tzbt);
					map.put("TZNR", tznr);
					map.put("FSSJ", sdf.format(fssj));
					l.add(map);
				}
				return l;
			}
		});
	}
	public int getGZSCListSize(String tzbt, String fkzt) {
		int count=0;
		Map params = new HashMap();
		int n = 1;
		String userid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
		Long orgroleid=UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getOrgroleid();
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) COUNT FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查')) A LEFT JOIN BD_XP_GZSC B ON A.DATAID = B.ID WHERE  B.ID IS NOT NULL  ");
		if(orgroleid!=5){
		sb.append(" AND B.FSRID=? ");
		params.put(n, userid);
		n++;
		}
		if(tzbt!=null&&!tzbt.equals("")){
			sb.append(" AND B.TZBT LIKE ? ");
			params.put(n, "%"+tzbt+"%");
			n++;
		}
		if(fkzt!=null&&!fkzt.equals("")){
			sb.append(" AND B.FKZT=? ");
			params.put(n, fkzt);
		}
		count = DBUtil.getInt(sb.toString(), "COUNT");
		return count;
	}
	
	public List<HashMap> getGZSCHFList(String id,String fkzt) {
		List param = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT ZQDM,DEPARTMENTNAME,NAME,PHONE,EINS,FKZT,HFSJ,USERID,INSTANCEID,FSRID,FSR,TZBT,TZNR,FSSJ,CID,DID,EMAIL FROM (SELECT C.INSTANCEID,C.FSRID,C.FSR,C.TZBT,C.TZNR,C.FSSJ,C.ID CID,D.ID DID,D.USERID,D.NAME,D.FKZT,D.PHONE,D.EMAIL,E.INSTANCEID EINS,D.DEPARTMENTNAME,E.HFSJ,F.ZQDM,ROWNUM RNUM FROM (SELECT INSTANCEID,B.ID,B.FSRID,B.FSR,B.TZBT,B.TZNR,B.FSSJ FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查')) A LEFT JOIN BD_XP_GZSC B ON A.DATAID = B.ID) C LEFT JOIN (SELECT A.INSTANCEID,B.ID,B.USERID,B.NAME,B.PHONE,B.EMAIL,B.FKZT,ORG.USERNAME,ORG.EXTEND1,ORG.DEPARTMENTNAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查事项反馈人表表单')) A LEFT JOIN BD_XP_GZSCSXFKRB B ON A.DATAID = B.ID LEFT JOIN ORGUSER ORG ON B.USERID=ORG.USERID) D ON C.INSTANCEID = D.INSTANCEID AND D.ID IS NOT NULL AND C.ID IS NOT NULL LEFT JOIN BD_ZQB_KH_BASE F ON D.EXTEND1=F.CUSTOMERNO LEFT JOIN (SELECT INSTANCEID,B.DID,B.HFR,B.HFSJ FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查回复')) A LEFT JOIN BD_XP_GZBCHF B ON A.DATAID = B.ID) E ON D.ID=E.DID) WHERE USERID IS NOT NULL AND CID=?");
		param.add(id);
		if(fkzt!=null&&!fkzt.equals("")){
			sb.append("AND FKZT=?");
			param.add(fkzt);
		}
		sb.append(" ORDER BY FKZT DESC");
		final String sql1 = sb.toString();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		final List params = param;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < params.size(); i++) {
					if(params.get(i)!=null && !"".equals(params.get(i).toString())){
						String params2=params.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params2)){
							return l;
						}
						}
					query.setParameter(i, params.get(i));
				}
				List<Object[]> li = query.list();
				HashMap map;
				SimpleDateFormat sdf;
				for (Object[] object : li) {
					map = new HashMap();
					sdf = new SimpleDateFormat("yyyy-MM-dd");
					String zqdm = (String) object[0];
					String departmentname = (String) object[1];
					String name = (String) object[2];
					String phone = (String) object[3];
					BigDecimal eins = (BigDecimal) object[4];
					String fkzt = (String) object[5];
					Date hfsj = (Date) object[6];
					map.put("ZQDM", zqdm);
					map.put("DEPARTMENTNAME", departmentname);
					map.put("NAME", name);
					map.put("PHONE", phone);
					map.put("INSTANCEID", eins!=null?eins.toString():"");
					map.put("FKZT", fkzt);
					map.put("HFSJ", hfsj!=null?sdf.format(hfsj):"");
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap<String, Object>> getItemList(String sql,List<String> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long sxid = rs.getLong("SXID");
				Long sxinstanceid = rs.getLong("SXINSTANCEID");
				String sxmc = rs.getString("SXMC");
				String sxsygz = rs.getString("SXSYGZ");
				String sxplyq = rs.getString("SXPLYQ");
				Integer rownum = rs.getInt("RM");
				result.put("SXID", sxid);
				result.put("SXINSTANCEID", sxinstanceid);
				result.put("SXMC", sxmc);
				result.put("SXSYGZ", sxsygz);
				result.put("SXPLYQ", sxplyq);
				result.put("ROWNUM", rownum);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public Integer getItemListSize(String sql,List<String> parameter) {
		int count=0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List<HashMap<String, Object>> getItemContentList(String sql,List<String> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long nrid = rs.getLong("NRID");
				Long cinstanceid = rs.getLong("CINSTANCEID");
				String plnr = rs.getString("PLNR");
				String sxmc = rs.getString("SXMC");
				Integer rownum = rs.getInt("RM");
				result.put("NRID", nrid);
				result.put("CINSTANCEID", cinstanceid);
				result.put("PLNR", plnr);
				result.put("SXMC", sxmc);
				result.put("ROWNUM", rownum);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public int getItemContentListSize(String sql, List<String> parameter) {
		int count=0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List<HashMap<String, Object>> getItemBcList(String sql,List<String> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long bcid = rs.getLong("BCID");
				Long bcinstanceid = rs.getLong("BCINSTANCEID");
				String sxmc = rs.getString("SXMC");
				String plbc = rs.getString("PLBC");
				Integer rownum = rs.getInt("RM");
				result.put("BCID", bcid);
				result.put("BCINSTANCEID", bcinstanceid);
				result.put("SXMC", sxmc);
				result.put("PLBC", plbc);
				result.put("ROWNUM", rownum);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public int getItemBcListSize(String sql, List<String> parameter) {
		int count=0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List<HashMap<String, Object>> getItemStepList(String sql,List<String> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long bzid = rs.getLong("BZID");
				Long bzinstanceid = rs.getLong("BZINSTANCEID");
				String sxmc = rs.getString("SXMC");
				String bznr = rs.getString("BZNR");
				Integer rownum = rs.getInt("RM");
				result.put("BZID", bzid);
				result.put("BZINSTANCEID", bzinstanceid);
				result.put("SXMC", sxmc);
				result.put("BZNR", bznr);
				result.put("ROWNUM", rownum);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public int getItemStepListSize(String sql, List<String> parameter) {
		int count=0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List<String> getItemTitleList(String sql) {
		List<String> dataList=new ArrayList<String>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String sxmc = rs.getString("SXMC");
				dataList.add(sxmc);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public void itemMoveUp(String entityname, Long itemDemId, Long sxinstanceid, String type) {
		HashMap data1 = DemAPI.getInstance().getFromData(sxinstanceid);
		try{
		Long data1Index = Long.parseLong(data1.get("ORDERINDEX").toString());
		String appendSql="";
		if(data1.get("SXID")!=null){
			appendSql=" AND SXID="+Long.parseLong(data1.get("SXID").toString());
		}
		String sql="";
		if(type.equals("up")){
			sql="SELECT INSTANCEID FROM (SELECT INSTANCEID,ROWNUM RW FROM (SELECT BIND.INSTANCEID INSTANCEID FROM "+entityname+" ENTITY INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='"+entityname+"') META ON IFORM.METADATAID=META.ID) BIND ON ENTITY.ID=BIND.DATAID WHERE ORDERINDEX<"+data1Index+appendSql+" ORDER BY ORDERINDEX DESC)) WHERE RW=1";
		}else if(type.equals("down")){
			sql="SELECT INSTANCEID FROM (SELECT INSTANCEID,ROWNUM RW FROM (SELECT BIND.INSTANCEID INSTANCEID FROM "+entityname+" ENTITY INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='"+entityname+"') META ON IFORM.METADATAID=META.ID) BIND ON ENTITY.ID=BIND.DATAID WHERE ORDERINDEX>"+data1Index+appendSql+" ORDER BY ORDERINDEX ASC)) WHERE RW=1";
		}else if(type.equals("top")){
			sql="SELECT INSTANCEID FROM (SELECT INSTANCEID,ROWNUM RW FROM (SELECT BIND.INSTANCEID INSTANCEID FROM "+entityname+" ENTITY INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='"+entityname+"') META ON IFORM.METADATAID=META.ID) BIND ON ENTITY.ID=BIND.DATAID WHERE ORDERINDEX<"+data1Index+appendSql+" ORDER BY ORDERINDEX ASC)) WHERE RW=1";
		}else if(type.equals("bottom")){
			sql="SELECT INSTANCEID FROM (SELECT INSTANCEID,ROWNUM RW FROM (SELECT BIND.INSTANCEID INSTANCEID FROM "+entityname+" ENTITY INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='"+entityname+"') META ON IFORM.METADATAID=META.ID) BIND ON ENTITY.ID=BIND.DATAID WHERE ORDERINDEX>"+data1Index+appendSql+" ORDER BY ORDERINDEX DESC)) WHERE RW=1";
		}
		Long instanceid = DBUtil.getLong(sql, "INSTANCEID");
		if(sxinstanceid!=0&&instanceid!=0){
			HashMap data2 = DemAPI.getInstance().getFromData(instanceid);
			Long data2Index = Long.parseLong(data2.get("ORDERINDEX").toString());
			Long data1Id = Long.parseLong(data1.get("ID").toString());
			Long data2Id = Long.parseLong(data2.get("ID").toString());
			data1.put("ORDERINDEX", data2Index);
			data2.put("ORDERINDEX", data1Index);
			boolean flaga = DemAPI.getInstance().updateFormData(itemDemId, sxinstanceid, data1, data1Id, false);
			if(flaga){
				boolean flagb = DemAPI.getInstance().updateFormData(itemDemId, instanceid, data2, data2Id, false);
				if(!flagb){
					while(true){
						boolean flagc = DemAPI.getInstance().updateFormData(itemDemId, sxinstanceid, data2, data1Id, false);
						if(flagc){break;}
					}
				}
			}
		}
		}catch(Exception e){
			logger.error(e,e);
		}
	}
	public List<HashMap> getXpsxlist(String roleid,String zqjc, String zqdm, String xpsxname,
			String noticename, String noticetype, String noticedatestart,
			String noticedateend, int pageNumber, int pageSize) {
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List params = new ArrayList();
		List<HashMap> list = new ArrayList<HashMap>();
		StringBuffer sb = new StringBuffer("SELECT ZQDM,ZQJC,XPSXNAME,XPFILE,MAX(NOTICENAME) NOTICENAME,TXRQ FROM (");
						sb.append(" SELECT ZQDM,ZQJC,XPSXNAME,XPFILE,TO_CHAR(WMSYS.WM_CONCAT(NOTICENAME) OVER(PARTITION BY ZQDM,ZQJC,XPSXNAME,XPFILE,TXRQ ORDER BY RN)) NOTICENAME,TO_CHAR(TXRQ,'yyyy-MM-dd hh24:mi:ss') TXRQ");
						sb.append(" FROM(");
						sb.append(" SELECT ROWNUM RN,KH.ZQDM,KH.ZQJC,XPSX.ID || '#' || XPSX.XPSXNAME || '#' || XPSX.CUSTOMERNO || '#' || XPSX.XPSXID XPSXNAME,XPSXFILE.XPFILE,GG.INSTANCEID || '#' || GG.NOTICENAME NOTICENAME,XPSX.TXRQ FROM BD_XP_XPSXQTB XPSX");  
						sb.append(" LEFT JOIN (SELECT XPSXQTID,TO_CHAR(WMSYS.WM_CONCAT(XPFILE)) XPFILE FROM BD_XP_XPSXBCWJB GROUP BY XPSXQTID) XPSXFILE ON XPSX.ID=XPSXFILE.XPSXQTID");  
						sb.append(" LEFT JOIN BD_ZQB_KH_BASE KH ON XPSX.CUSTOMERNO=KH.CUSTOMERNO");  
						sb.append(" LEFT JOIN (");
									sb.append(" SELECT B.INSTANCEID,A.BZLX,A.NOTICENAME,A.NOTICETYPE,A.NOTICEDATE,A.KHBH FROM BD_MEET_QTGGZL A");
									sb.append(" LEFT JOIN (SELECT ID,DATAID,INSTANCEID FROM SYS_ENGINE_FORM_BIND");
									sb.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='公告呈报管理')) B ON A.ID=B.DATAID");
									sb.append(" ORDER BY B.INSTANCEID DESC");
									sb.append(" ) GG ON GG.KHBH=XPSX.CUSTOMERNO AND GG.BZLX=XPSX.ID");
						sb.append(" WHERE 1=1");
		if(roleid.equals("3")){
			String khbh = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1();
			sb.append(" AND KH.CUSTOMERNO = ?");
			params.add(khbh);
		}
		if(roleid.equals("4")){
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			sb.append(" AND KH.CUSTOMERNO IN (SELECT KHBH FROM BD_MDM_KHQXGLB B WHERE SUBSTR(B.KHFZR,0, instr(B.KHFZR,'[',1)-1) =? OR SUBSTR(B.ZZCXDD,0, instr(B.ZZCXDD,'[',1)-1) =? OR SUBSTR(B.FHSPR,0, instr(B.FHSPR,'[',1)-1) =? OR SUBSTR(B.ZZSPR,0, instr(B.ZZSPR,'[',1)-1) =? OR SUBSTR(B.CWSCBFZR2,0, instr(B.CWSCBFZR2,'[',1)-1) =? OR SUBSTR(B.CWSCBFZR3,0, instr(B.CWSCBFZR3,'[',1)-1) =? OR SUBSTR(B.GGFBR,0, instr(B.GGFBR,'[',1)-1) =? OR SUBSTR(B.qynbrysh,0, instr(B.qynbrysh,'[',1)-1) =?)");
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
		}
		if(zqjc!=null&&!zqjc.equals("")){
			sb.append(" AND KH.ZQJC LIKE ?");
			params.add("%"+zqjc+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND KH.ZQDM =?");
			params.add(zqdm);
		}
		if(xpsxname!=null&&!xpsxname.equals("")){
			sb.append(" AND XPSX.XPSXNAME LIKE ?");
			params.add("%"+xpsxname+"%");
		}
		if(noticename!=null&&!noticename.equals("")){
			sb.append(" AND XPSX.ID IN (SELECT BZLX FROM BD_MEET_QTGGZL WHERE NOTICENAME LIKE ?)");
			params.add("%"+noticename+"%");
		}
		if(noticetype!=null&&!noticetype.equals("")){
			sb.append(" AND XPSX.ID IN (SELECT BZLX FROM BD_MEET_QTGGZL WHERE NOTICETYPE = ?)");
			params.add(noticetype);
		}
		if(noticedatestart!=null&&!noticedatestart.equals("")){
			sb.append(" AND XPSX.ID IN (SELECT BZLX FROM BD_MEET_QTGGZL WHERE NOTICEDATE >= TO_DATE(?,'yyyy-MM-dd'))");
			params.add(noticedatestart);
		}
		if(noticedateend!=null&&!noticedateend.equals("")){
			sb.append(" AND XPSX.ID IN (SELECT BZLX FROM BD_MEET_QTGGZL WHERE NOTICEDATE <= TO_DATE(?,'yyyy-MM-dd'))");
			params.add(noticedatestart);
		}
		sb.append(" )) GROUP BY ZQDM,ZQJC,XPSXNAME,XPFILE,TXRQ ORDER BY TO_DATE(TXRQ,'yyyy-mm-dd hh24:mi:ss') DESC");
		final String sql = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> li = query.list();
				HashMap map;
				SimpleDateFormat sdf;
				for (Object[] object : li) {
					map = new HashMap();
					sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String ZQDM =(String) object[0];
					String ZQJC =(String) object[1];
					
					String xpsxhtml = "";
					String XPSXNAME =(String) object[2];
					String[] xpsxarray = XPSXNAME.split("#");
					xpsxhtml = "<div id='"+xpsxarray[0]+"O'><span class='selItem'><a href='javascript:checkXpsx("+xpsxarray[0]+")'>"+xpsxarray[1]+"</a>&nbsp;&nbsp;<a href='javascript:deleteXpsx("+xpsxarray[0]+")'><img id='"+xpsxarray[0]+"' src='/iwork_img/del3.gif' border='0'></a></span></div>";
					
					/*String PLNR =(String) object[3];
					String PLBC =(String) object[4];*/
					
					String xpfilehtml = "";
					String XPFILE =(String) object[3];
					if(XPFILE!=null&&!XPFILE.equals("")){
					String[] xpfilearray = XPFILE.split(",");
						for (int i = 0; i < xpfilearray.length; i++) {
							String filename = DBUtil.getString("SELECT FILE_SRC_NAME FROM SYS_UPLOAD_FILE WHERE FILE_ID='"+xpfilearray[i]+"'", "FILE_SRC_NAME");
							if(filename!=null&&!filename.equals("")){
								xpfilehtml += "<div id='"+xpfilearray[i]+"O'><span id='"+xpfilearray[i]+"' onclick='downloadfile(this)' class='selItem'>&nbsp;&nbsp;<a title='"+xpfilearray[i]+"' href='javascript:void(0)'>"+filename+"</a></span><img style='cursor:pointer;' id='"+xpfilearray[i]+"' src='/iwork_img/del3.gif' onclick='deletefile(this)' border='0'></div>";
							}
						}
					}
					
					String NOTICENAME =(String) object[4];
					String html = "";
					if(NOTICENAME!=null&&!NOTICENAME.equals("")&&!NOTICENAME.equals("#")){
						String[] array = NOTICENAME.split(",");
						String instanceid = "";
						String noticename = "";
						for (int i = 0; i < array.length; i++) {
							if(!array[i].equals("#")){
								String[] arraysyb = array[i].split("#");
								instanceid = arraysyb[0];
								noticename = arraysyb[1];
								html += "<div id='"+instanceid+"O' title='"+ZQDM+","+ZQJC+"'><span class='selItem'><a href='javascript:cheackAnncement("+instanceid+")'>"+noticename+"</a>&nbsp;&nbsp;<a href='javascript:deleteAnncement("+instanceid+")'><img id='"+instanceid+"' src='/iwork_img/del3.gif' border='0'></a></span></div>";
							}
						}
					}
					String CUSTOMERNAME="";
					//if(xpsxarray.length==3){
					CUSTOMERNAME = DBUtil.getString("SELECT CUSTOMERNAME FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO='"+xpsxarray[2]+"'", "CUSTOMERNAME");
					//}
					String TXRQ =(String)object[5];
					map.put("ZQDM", ZQDM);
					map.put("ZQJC", ZQJC);
					map.put("XPSXNAME", xpsxhtml);
					if(xpsxarray.length>=4){
						map.put("XPSXID", xpsxarray[0]);
						map.put("XPSX", xpsxarray[1]);
						map.put("CUSTOMERNO", xpsxarray[2]);
					}
					map.put("CUSTOMERNAME", CUSTOMERNAME);
					String PLNR = getTypeStr(xpsxarray[3],"内容");
					String PLBC = getTypeStr(xpsxarray[3],"备查");
					map.put("PLNR", PLNR==null?"":PLNR.toString());
					map.put("PLBC", PLBC==null?"":PLBC.toString());
					map.put("XPFILE", xpfilehtml);
					map.put("NOTICENAME", html);
					map.put("TXRQ", TXRQ);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getXpsxlistSize(String roleid,String zqjc, String zqdm, String xpsxname,
			String noticename, String noticetype, String noticedatestart,
			String noticedateend) {
		List<HashMap> list = new ArrayList<HashMap>();
		StringBuffer sb = new StringBuffer("SELECT ZQDM,ZQJC,XPSXNAME,XPFILE,TO_CHAR(WMSYS.WM_CONCAT(NOTICENAME)) NOTICENAME,TO_CHAR(TXRQ,'yyyy-MM-dd hh24:mi:ss') TXRQ FROM ( ");
				//--信批事项前台表PLNR,PLBC,
				sb.append(" SELECT KH.ZQDM,KH.ZQJC,XPSX.ID || '#' || XPSX.XPSXNAME || '#' || XPSX.CUSTOMERNO || '#' || XPSX.XPSXID XPSXNAME,XPSXFILE.XPFILE,GG.INSTANCEID || '#' || GG.NOTICENAME NOTICENAME,XPSX.TXRQ FROM BD_XP_XPSXQTB XPSX ");
				//--信批事项备查文件表B.PLNR,C.PLBC,
				sb.append(" LEFT JOIN (SELECT XPSXQTID,TO_CHAR(WMSYS.WM_CONCAT(XPFILE)) XPFILE FROM BD_XP_XPSXBCWJB GROUP BY XPSXQTID) XPSXFILE ON XPSX.ID=XPSXFILE.XPSXQTID ");
				//--客户表
				sb.append(" LEFT JOIN BD_ZQB_KH_BASE KH ON XPSX.CUSTOMERNO=KH.CUSTOMERNO ");
				//--信披事项内容
				//sb.append(" LEFT JOIN (SELECT SXID,TO_CHAR(WMSYS.WM_CONCAT('</br>' || ORDERINDEX || '、' || PLNR)) PLNR FROM BD_XP_XPSXNR GROUP BY SXID) B ON XPSX.XPSXID=B.SXID ");
				//--信披事项备查
				//sb.append(" LEFT JOIN (SELECT SXID,TO_CHAR(WMSYS.WM_CONCAT('</br>' || ORDERINDEX || '、' || PLBC)) PLBC FROM BD_XP_XPSXBC GROUP BY SXID) C ON XPSX.XPSXID=C.SXID ");
				//--公告物理表
				sb.append(" LEFT JOIN (SELECT B.INSTANCEID,A.BZLX,A.NOTICENAME,A.NOTICETYPE,A.NOTICEDATE,A.KHBH FROM BD_MEET_QTGGZL A ");
				sb.append("           LEFT JOIN (SELECT ID,DATAID,INSTANCEID FROM SYS_ENGINE_FORM_BIND ");
				sb.append("           WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='公告呈报管理')) B ON A.ID=B.DATAID) GG ");
				sb.append("           ON GG.KHBH=XPSX.CUSTOMERNO AND GG.BZLX=XPSX.ID ");
				
				sb.append(" WHERE 1=1 ");
		if(roleid.equals("3")){
			String khbh = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1();
			sb.append(" AND KH.CUSTOMERNO = ? ");
		}
		if(roleid.equals("4")){
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			sb.append(" AND KH.CUSTOMERNO IN (SELECT KHBH FROM BD_MDM_KHQXGLB B WHERE SUBSTR(B.KHFZR,0, instr(B.KHFZR,'[',1)-1) =? OR SUBSTR(B.ZZCXDD,0, instr(B.ZZCXDD,'[',1)-1) =? OR SUBSTR(B.FHSPR,0, instr(B.FHSPR,'[',1)-1) =? OR SUBSTR(B.ZZSPR,0, instr(B.ZZSPR,'[',1)-1) =? OR SUBSTR(B.CWSCBFZR2,0, instr(B.CWSCBFZR2,'[',1)-1) =? OR SUBSTR(B.CWSCBFZR3,0, instr(B.CWSCBFZR3,'[',1)-1) =? OR SUBSTR(B.GGFBR,0, instr(B.GGFBR,'[',1)-1) =? OR SUBSTR(B.qynbrysh,0, instr(B.qynbrysh,'[',1)-1) =?) ");
		}
		if(zqjc!=null&&!zqjc.equals("")){
			sb.append(" AND KH.ZQJC LIKE ? ");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND KH.ZQDM =? ");
		}
		if(xpsxname!=null&&!xpsxname.equals("")){
			sb.append(" AND XPSX.XPSXNAME LIKE ? ");
		}
		if(noticename!=null&&!noticename.equals("")){
			sb.append(" AND XPSX.ID IN (SELECT BZLX FROM BD_MEET_QTGGZL WHERE NOTICENAME LIKE ?) ");
		}
		if(noticetype!=null&&!noticetype.equals("")){
			sb.append(" AND XPSX.ID IN (SELECT BZLX FROM BD_MEET_QTGGZL WHERE NOTICETYPE = ?) ");
		}
		if(noticedatestart!=null&&!noticedatestart.equals("")){
			sb.append(" AND XPSX.ID IN (SELECT BZLX FROM BD_MEET_QTGGZL WHERE NOTICEDATE >= TO_DATE(?,'yyyy-MM-dd')) ");
		}
		if(noticedateend!=null&&!noticedateend.equals("")){
			sb.append(" AND XPSX.ID IN (SELECT BZLX FROM BD_MEET_QTGGZL WHERE NOTICEDATE <= TO_DATE(?,'yyyy-MM-dd')) ");
		}
		sb.append(" ) GROUP BY ZQDM,ZQJC,XPSXNAME,XPFILE,TXRQ ORDER BY TO_DATE(TXRQ,'yyyy-mm-dd hh24:mi:ss') DESC");
		final String sql = sb.toString();
		final String final_roleid = roleid;
		final String final_zqjc = zqjc;
		final String final_zqdm = zqdm;
		final String final_xpsxname = xpsxname;
		final String final_noticename = noticename;
		final String final_noticetype = noticetype;
		final String final_noticedatestart = noticedatestart;
		final String final_noticedateend = noticedateend;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				int i=0;
				DBUtilInjection d=new DBUtilInjection();
			    List lis=new ArrayList();
				if(final_roleid.equals("3")){
					String khbh = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1();
					if ((khbh != null) && (!"".equals(khbh))) {
						if(d.HasInjectionData(khbh)){
							return lis;
						}
						
					}
					query.setString(i, khbh);i++;
				}
				if(final_roleid.equals("4")){
					
					String userid = UserContextUtil.getInstance().getCurrentUserId();
					if ((userid != null) && (!"".equals(userid))) {
						if(d.HasInjectionData(userid)){
							return lis;
						}
						
					}
					query.setString(i, userid);i++;
					query.setString(i, userid);i++;
					query.setString(i, userid);i++;
					query.setString(i, userid);i++;
					query.setString(i, userid);i++;
					query.setString(i, userid);i++;
					query.setString(i, userid);i++;
					query.setString(i, userid);i++;
				}
				if(final_zqjc!=null&&!final_zqjc.equals("")){
					if(d.HasInjectionData(final_zqjc)){
						return lis;
					}
					query.setString(i, "'%"+final_zqjc+"%'");i++;
				}
				if(final_zqdm!=null&&!final_zqdm.equals("")){
					if(d.HasInjectionData(final_zqdm)){
						return lis;
					}
					query.setString(i, final_zqdm);i++;
				}
				if(final_xpsxname!=null&&!final_xpsxname.equals("")){
					if(d.HasInjectionData(final_xpsxname)){
						return lis;
					}
					query.setString(i, "'%"+final_xpsxname+"%'");i++;
				}
				if(final_noticename!=null&&!final_noticename.equals("")){
					if(d.HasInjectionData(final_noticename)){
						return lis;
					}
					query.setString(i, "'%"+final_noticename+"%'");i++;
				}
				if(final_noticetype!=null&&!final_noticetype.equals("")){
					if(d.HasInjectionData(final_noticetype)){
						return lis;
					}
					query.setString(i, final_noticetype);i++;
				}
				if(final_noticedatestart!=null&&!final_noticedatestart.equals("")){
					if(d.HasInjectionData(final_noticedatestart)){
						return lis;
					}
					query.setString(i, final_noticedatestart);i++;
				}
				if(final_noticedateend!=null&&!final_noticedateend.equals("")){
					if(d.HasInjectionData(final_noticedateend)){
						return lis;
					}
					query.setString(i, final_noticedateend);i++;
				}
				List<Map> l = new ArrayList<Map>();
				List<Object[]> li = query.list();
				HashMap map;
				for (Object[] object : li) {
					map = new HashMap();
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getXpsxListModel() {
		StringBuffer sb = new StringBuffer("SELECT A.ID,A.SXMC FROM BD_XP_XPSXB A ORDER BY A.ORDERINDEX");
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> li = query.list();
				HashMap map;
				SimpleDateFormat sdf;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id=(BigDecimal)object[0];
					String sxmc = (String)object[1];
					map.put("ID", id);
					map.put("SXMC", sxmc);
					l.add(map);
				}
				return l;
			}
		});
	}
	public String getXpsxCompanyList(){
		StringBuffer zqdmzqjc = new StringBuffer();
		StringBuffer sb = new StringBuffer("SELECT BD.CUSTOMERNO,BD.ZQJC || '(' || BD.ZQDM || ')' ZQJC FROM BD_ZQB_KH_BASE BD WHERE 1=1 AND BD.ZQDM IS NOT NULL AND BD.ZQJC IS NOT NULL");
		Long roleid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getOrgroleid();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		if(roleid!=3l&&roleid!=5l){
			sb.append(" AND BD.CUSTOMERNO IN (SELECT KHBH FROM BD_MDM_KHQXGLB B WHERE SUBSTR(B.KHFZR,0, instr(B.KHFZR,'[',1)-1) =? OR SUBSTR(B.ZZCXDD,0, instr(B.ZZCXDD,'[',1)-1) =? OR SUBSTR(B.FHSPR,0, instr(B.FHSPR,'[',1)-1) =? OR SUBSTR(B.ZZSPR,0, instr(B.ZZSPR,'[',1)-1) =? OR SUBSTR(B.CWSCBFZR2,0, instr(B.CWSCBFZR2,'[',1)-1) =? OR SUBSTR(B.CWSCBFZR3,0, instr(B.CWSCBFZR3,'[',1)-1) =? OR SUBSTR(B.GGFBR,0, instr(B.GGFBR,'[',1)-1) =? OR SUBSTR(B.qynbrysh,0, instr(B.qynbrysh,'[',1)-1) =?) ");
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			if(roleid!=3l&&roleid!=5l){
				ps.setString(0, userid);
				ps.setString(1, userid);
				ps.setString(2, userid);
				ps.setString(3, userid);
				ps.setString(4, userid);
				ps.setString(5, userid);
				ps.setString(6, userid);
				ps.setString(7, userid);
			}
			rs = ps.executeQuery();
			while(rs.next()){
				zqdmzqjc.append(rs.getString(1)+"$");
				zqdmzqjc.append(rs.getString(2)+",");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		
		return zqdmzqjc.toString();
	}

	public List<HashMap> getXpsxById(String id) {
		StringBuffer sb = new StringBuffer();//("SELECT ID,SXMC,XPSXNAME,USERNAME,TXRQ,SYGZ,PLYQ,TO_CHAR(WMSYS.WM_CONCAT('</br>' ||  BZNR)) BZNR FROM ( ");
		sb.append("SELECT A.ID,A.SXMC,A.SYGZ,A.PLYQ FROM BD_XP_XPSXB A WHERE A.ID=?");// LEFT JOIN BD_XP_XPSXBZ D ON A.ID=D.SXID ,D.ORDERINDEX|| '、' ||D.BZNR BZNR    
		//sb.append(" )GROUP BY ID,SXMC,XPSXNAME,USERNAME,TXRQ,SYGZ,PLYQ ");
		final String sql1 = sb.toString();
		final String final_id = id;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				
					if(final_id!=null && !"".equals(final_id)){
						
						if(d.HasInjectionData(final_id)){
							return l;
						}
						}
					
				query.setParameter(0, final_id);
				List<Object[]> li = query.list();
				HashMap map;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal) object[0];
					String SXMC = (String) object[1];
					String SYGZ = (String) object[2];
					String PLYQ = (String) object[3];
					map.put("ID", ID);
					map.put("SXMC", SXMC);
					map.put("SYGZ", SYGZ);
					map.put("PLYQ", PLYQ);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getGlhyById(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select s.meetname,s.meettype,s.status,to_char(s.plantime,'yyyy-MM-dd hh24:mi') plantime,s.hysx,s.year,s.jc,s.hc,s.zywyh,s.fj,s.yazlfj from BD_MEET_PLAN s where id = ? ");
		final String sql1 = sb.toString();
		final String final_id = id;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				
					if(final_id!=null && !"".equals(final_id)){
						
						if(d.HasInjectionData(final_id)){
							return l;
						}
						}
				query.setParameter(0, final_id);
				
				List<Object[]> li = query.list();
				HashMap map;
				for (Object[] object : li) {
					map = new HashMap();
					String HYMC = (String) object[0];
					String HYLX = (String) object[1];
					String ZT = (String) object[2];
					String HYSJ = (String) object[3];
					String DRLX = (String) object[4];
					BigDecimal YEAR = (BigDecimal) object[5];
					BigDecimal JC = (BigDecimal) object[6];
					BigDecimal HC = (BigDecimal) object[7];
					String ZYWYH = (String) object[8];
					String fj = (String) object[9];
					String yazlfj = (String) object[10];
					map.put("HYMC", HYMC);
					map.put("HYLX", HYLX);
					map.put("ZT", ZT);
					map.put("HYSJ", HYSJ);
					map.put("DRLX", DRLX);
					map.put("YEAR", YEAR);
					map.put("JC", JC);
					map.put("HC", HC);
					map.put("ZYWYH", ZYWYH);
					map.put("fj", fj);
					map.put("yazlfj", yazlfj);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getTypeList(String id,String nr) {
		StringBuffer sb = new StringBuffer();
		List params = new ArrayList();
		if(nr.equals("适用规则")){
			sb.append("SELECT A.ID,A.SYGZ FROM BD_XP_XPSXB A WHERE ID IN(");
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==ids.length-1){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(ids[i]);
			}
			sb.append(")");
		}
		if(nr.equals("披露要求")){
			sb.append("SELECT A.ID,A.PLYQ FROM BD_XP_XPSXB A WHERE ID IN(");
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==ids.length-1){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(ids[i]);
			}
			sb.append(")");
		}
		if(nr.equals("步骤")){
			sb.append("SELECT D.ID,D.BZNR FROM BD_XP_XPSXBZ D WHERE D.SXID IN(");
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==ids.length-1){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(ids[i]);
			}
			sb.append(") ORDER BY ORDERINDEX");
		}
		if(nr.equals("备查")){
			sb.append("SELECT C.ID,C.PLBC FROM BD_XP_XPSXBC C WHERE C.SXID IN(");
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==ids.length-1){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(ids[i]);
			}
			sb.append(") ORDER BY ORDERINDEX");
		}
		if(nr.equals("内容")){
			sb.append("SELECT B.ID,B.PLNR FROM BD_XP_XPSXNR B WHERE B.SXID IN(");
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==ids.length-1){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(ids[i]);
			}
			sb.append(") ORDER BY ORDERINDEX");
		}
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> li = query.list();
				HashMap map;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String nr = (String) object[1];
					map.put("NR", nr);
					l.add(map);
				}
				return l;
			}
		});
	}
	public String getTypeStr(String id,String nr) {
		StringBuffer sb = new StringBuffer();
		if(nr.equals("备查")){
			sb.append("SELECT D.SXMC||'--'||C.PLBC STR FROM BD_XP_XPSXBC C LEFT JOIN BD_XP_XPSXB D ON C.SXID=D.ID WHERE C.SXID IN (");
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==(ids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
			}
			sb.append(") ORDER BY D.ID,C.ORDERINDEX");
		}
		if(nr.equals("内容")){
			sb.append("SELECT D.SXMC||'--'||C.PLBC STR FROM BD_XP_XPSXNR C LEFT JOIN BD_XP_XPSXB D ON C.SXID=D.ID WHERE C.SXID IN (");
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==(ids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
			}
			sb.append(") ORDER BY D.ID,C.ORDERINDEX");
		}
		StringBuffer html  = new StringBuffer("");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				ps.setString(i+1, ids[i]);
			}
			rs = ps.executeQuery();
			int i=1;
			while(rs.next()){
				html.append(i + "、" + rs.getString(1) + "</br>");
				i++;
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return html.toString();
	}
	
	public String XpsxGgGetbcfile(String id) {
		StringBuffer sb = new StringBuffer("SELECT TO_CHAR(WMSYS.WM_CONCAT(XPFILE)) XPFILE FROM BD_XP_XPSXBCWJB WHERE XPSXQTID=?");
		StringBuffer html  = new StringBuffer();
		Integer rowspannum = null;
		html.append("<table id=\"DIVSXBCFILE\" width=\"725px;\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\">");
		html.append("		<tbody>");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, id);
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getString(1)!=null&&!rs.getString(1).equals("")){
					String[] srtarr = rs.getString(1).split(",");
					String file_ids = rs.getString(1).toString().replaceAll(",", "','");
					rowspannum = DBUtil.getInt("SELECT COUNT(FILE_ID) NUM FROM SYS_UPLOAD_FILE WHERE FILE_ID IN ('"+file_ids+"')", "NUM");
					int i = 0;
					String icon = "iwork_img/attach.png";
					for (int j = 0; j < srtarr.length; j++) {
						String strarri = srtarr[j];
						FileUpload fileUpload = getFileUpload(strarri);
						String filename = DBUtil.getString("SELECT FILE_SRC_NAME FROM SYS_UPLOAD_FILE WHERE FILE_ID='"+strarri+"'", "FILE_SRC_NAME");
						if(filename!=null&&!filename.equals("")){
							icon = FileType.getFileIcon(filename);
								html.append("<tr id=\""+strarri+"\">");
								if(i==0){
								html.append("	<td id=\"rowspantdsxbc\" align=\"center\" rowspan=\""+rowspannum+"\" width=\"90px;\" align=\"left\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\">"
													+ "事项备查文件</br>"
													+ "<a class=\"easyui-linkbutton l-btn l-btn-plain\" plain=\"true\" iconCls=\"icon-add\" onclick=\"showUploadifyPageSXBCFILE();return false;\" style=\"color:#5e4db0;text-decoration:underline;cursor:pointer;\"><span class=\"l-btn-left\"><span class=\"l-btn-text icon-add\" style=\"padding-left: 20px;\">上传附件</span></span></a>"
											+ "</td>");
								}
								html.append("	<td title=\""+filename+"\" width=\"230px;\" align=\"left\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\">");
								html.append("		<a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+strarri+"\">");
								html.append("			<img src=\""+icon+"\" style=\"margin:3px\">");
								html.append("			"+(filename.length()>22?filename.substring(0, 19)+"...":filename)+"");
								html.append("		</a>");
								html.append("	</td>");
								html.append("	<td width=\"180px;\" align=\"center\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\">");
								html.append("		<a href=\"javascript:deletefile('"+strarri+"');xpbcUploadifyReomve('SXBCFILE','"+strarri+"','"+strarri+"');\" style=\"text-decoration:none;\">【删除】</a>");
								html.append("	</td>");
								html.append("	<td width=\"135px;\" align=\"center\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\">"+fileUpload.getUploadTime()+"</td>");
								html.append("	<td width=\"90px;\" align=\"center\" style=\"border-bottom:1px solid #eee;color:#004080;\">  </td>");
								html.append("</tr>");
								i++;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			if(rowspannum==null||rowspannum==0){
				html.append("<tr style=\"height:0px;\">");
				html.append("	<td id=\"rowspantdsxbc\" align=\"center\" width=\"90px;\" align=\"left\" style=\"border-right:1px solid #eee;color:#004080;\">"
							+ "事项备查文件</br>"
							+ "<a class=\"easyui-linkbutton l-btn l-btn-plain\" plain=\"true\" iconCls=\"icon-add\" onclick=\"showUploadifyPageSXBCFILE();return false;\" style=\"color:#5e4db0;text-decoration:underline;cursor:pointer;\"><span class=\"l-btn-left\"><span class=\"l-btn-text icon-add\" style=\"padding-left: 20px;\">上传附件</span></span></a>"
							+ "</td>");
				html.append("	<td title=\"\" width=\"230px;\" align=\"left\" style=\"border-right:1px solid #eee;\"></td>");
				html.append("	<td width=\"180px;\" align=\"center\" style=\"border-right:1px solid #eee;\"></td>");
				html.append("	<td width=\"135px;\" align=\"center\" style=\"border-right:1px solid #eee;color:#004080;\"></td>");
				html.append("	<td width=\"90px;\" align=\"center\" style=\"color:#004080;\">  </td>");
				html.append("</tr>");
			}
			html.append("		</tbody>");
			html.append("</table>");
			DBUtil.close(conn, ps, rs);
		}
		return html.toString();
	}
	
	public FileUpload getFileUpload(Serializable id) {
		FileUploadDAO fileUploadDao = null;
		if(fileUploadDao==null){
			fileUploadDao = (FileUploadDAO)SpringBeanUtil.getBean("uploadifyDAO");
		}
		return fileUploadDao.getFileUpload(FileUpload.class, id);
	}
	
	public List<HashMap> getZqdmList(String roleid) {
		StringBuffer sb = new StringBuffer("SELECT BD.ZQDM,BD.ZQJC FROM BD_ZQB_KH_BASE BD WHERE 1=1 AND BD.ZQDM IS NOT NULL AND BD.ZQJC IS NOT NULL");
		if(!roleid.equals("3")&&!roleid.equals("5")){
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			sb.append(" AND BD.CUSTOMERNO IN (SELECT KHBH FROM BD_MDM_KHQXGLB B WHERE SUBSTR(B.KHFZR,0, instr(B.KHFZR,'[',1)-1) ='"+userid+"' OR SUBSTR(B.ZZCXDD,0, instr(B.ZZCXDD,'[',1)-1) ='"+userid+"' OR SUBSTR(B.FHSPR,0, instr(B.FHSPR,'[',1)-1) ='"+userid+"' OR SUBSTR(B.ZZSPR,0, instr(B.ZZSPR,'[',1)-1) ='"+userid+"' OR SUBSTR(B.CWSCBFZR2,0, instr(B.CWSCBFZR2,'[',1)-1) ='"+userid+"' OR SUBSTR(B.CWSCBFZR3,0, instr(B.CWSCBFZR3,'[',1)-1) ='"+userid+"' OR SUBSTR(B.GGFBR,0, instr(B.GGFBR,'[',1)-1) ='"+userid+"' OR SUBSTR(B.qynbrysh,0, instr(B.qynbrysh,'[',1)-1) ='"+userid+"') ");
		}
		sb.append(" ORDER BY TO_NUMBER(BD.ZQDM)");
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> li = query.list();
				HashMap map;
				for (Object[] object : li) {
					map = new HashMap();
					String ZQDM = (String) object[0];
					String ZQJC = (String) object[1];
					map.put("ZQDM", ZQDM);
					map.put("ZQJC", ZQJC);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap<String, Object>> getXpqaList(String sql,List<String> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				String zqjc = rs.getString("ZQJC");
				String zqdm = rs.getString("ZQDM");
				String khbh = rs.getString("KHBH");
				String xppf = rs.getString("XPPF");
				result.put("ZQJC", zqjc);
				result.put("ZQDM", zqdm);
				result.put("XPPF", xppf);
				result.put("KHBH", khbh);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public List<HashMap<String, Object>> getNoticeDetails(String sql,List<String> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				Long instanceid = rs.getLong("INSTANCEID");
				String zqjcxs = rs.getString("ZQJCXS");
				String zqdmxs = rs.getString("ZQDMXS");
				String noticename = rs.getString("NOTICENAME");
				String noticeno = rs.getString("NOTICENO");
				String noticetype = rs.getString("NOTICETYPE");
				String noticedate = rs.getString("NOTICEDATE");
				Long lcbs = rs.getLong("LCBS");
				String lcbh = rs.getString("LCBH");
				String stepid = rs.getString("STEPID");
				String ggdf = rs.getString("GGDF");
				result.put("ID", id);
				result.put("INSTANCEID", instanceid);
				result.put("ZQJCXS", zqjcxs);
				result.put("ZQDMXS", zqdmxs);
				result.put("NOTICENAME", noticename);
				result.put("NOTICENO", noticeno);
				result.put("NOTICETYPE", noticetype);
				result.put("NOTICEDATE", noticedate);
				result.put("LCBS", lcbs);
				result.put("LCBH", lcbh);
				result.put("STEPID", stepid);
				result.put("GGDF", ggdf);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public List<HashMap<String, Object>> getOpinionUserList(String sql,List<String> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				String createuser = rs.getString("CREATEUSER");
				String username = rs.getString("USERNAME");
				Long onum = rs.getLong("ONUM");
				result.put("CREATEUSER", createuser);
				result.put("USERNAME", username);
				result.put("ONUM", onum);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public List<HashMap<String, Object>> getopinionUserContentList(String sql, List<HashMap<String, Object>> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				HashMap<String,Object> hashMap = parameter.get(i);
				if(hashMap.get("TYPE").equals("string")){
					ps.setString(index++, hashMap.get("VALUE").toString());
				}else if(hashMap.get("TYPE").equals("int")){
					ps.setInt(index++, Integer.parseInt(hashMap.get("VALUE").toString()));
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long ggid = rs.getLong("GGID");
				Long xh = rs.getLong("XH");
				Long rnum = rs.getLong("RNUM");
				String noticename = rs.getString("NOTICENAME");
				String zqjcxs = rs.getString("ZQJCXS");
				String zqdmxs = rs.getString("ZQDMXS");
				String createtime = rs.getString("CREATETIME");
				String content = rs.getString("CONTENT");
				result.put("GGID", ggid);
				result.put("RNUM", rnum);
				result.put("XH", xh);
				result.put("NOTICENAME", noticename);
				result.put("ZQJCXS", zqjcxs);
				result.put("ZQDMXS", zqdmxs);
				result.put("CREATETIME", createtime);
				result.put("CONTENT", content);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public int getOpinionUserContentListSize(String sql,List<String> parameter) {
		int count=0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List<HashMap<String, Object>> docDownload(String recordid,String fj) {
		StringBuffer sb=new StringBuffer();
		if("NOTICEFILE".equals(fj)){
			 sb.append("SELECT IV.DESCRIPT, SF.FILE_URL, GG.NOTICENAME"
						+ " FROM IWORK_WEBOFFCIE_VERSION IV  LEFT JOIN SYS_UPLOAD_FILE SF  ON IV.FILE_ID = SF.FILE_ID"
						+ " LEFT JOIN (SELECT BXG.ID,BXG.NOTICENAME,REGEXP_SUBSTR(BXG.NOTICEFILE, '[^,]+', 1, RN) COMPS"
						+ " FROM BD_XP_QTGGZLLC BXG,  (SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50)  WHERE REGEXP_SUBSTR(BXG.NOTICEFILE, '[^,]+', 1, RN) IS NOT NULL"
						+ " UNION ALL"
						+ " SELECT BXG.ID,BXG.NOTICENAME,REGEXP_SUBSTR(BXG.COMPANYNAME, '[^,]+', 1, RN) COMPS"
						+ " FROM BD_XP_QTGGZLLC BXG,  (SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50)  WHERE REGEXP_SUBSTR(BXG.COMPANYNAME, '[^,]+', 1, RN) IS NOT NULL) GG"
						+ " ON GG.COMPS = IV.RECORDID WHERE IV.FILE_ID=?");
		}else{
			
			
			 sb.append("SELECT IV.DESCRIPT, SF.FILE_URL, GG.SXMC"
						+ " FROM IWORK_WEBOFFCIE_VERSION IV  LEFT JOIN SYS_UPLOAD_FILE SF  ON IV.FILE_ID = SF.FILE_ID"
						+ " LEFT JOIN (SELECT BXG.ID,BXG.SXMC,REGEXP_SUBSTR(BXG.SXFJ, '[^,]+', 1, RN) COMPS"
						+ " FROM RCYWCB BXG,  (SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50)  WHERE REGEXP_SUBSTR(BXG.SXFJ, '[^,]+', 1, RN) IS NOT NULL"
						+ " UNION ALL"
						+ " SELECT BXG.ID,BXG.SXMC,REGEXP_SUBSTR(BXG.COMPANYNAME, '[^,]+', 1, RN) COMPS"
						+ " FROM RCYWCB BXG,  (SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50)  WHERE REGEXP_SUBSTR(BXG.COMPANYNAME, '[^,]+', 1, RN) IS NOT NULL) GG"
						+ " ON GG.COMPS = IV.RECORDID WHERE IV.FILE_ID=?");
		}
		
		final String sql1 = sb.toString();
		
		final String final_recordid = recordid;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				DBUtilInjection d=new DBUtilInjection();
				
					if(final_recordid!=null && !"".equals(final_recordid)){
						
						if(d.HasInjectionData(final_recordid)){
							return l;
						}
						}
					
				query.setString(0, final_recordid);
				
				
				List<Object[]> li = query.list();
				
				HashMap map;
				HashMap map1;
				for (Object[] object : li) {
					map = new HashMap();
					String DESCRIPT=(String)object[0];
					String FILE_URL = (String)object[1];
					String NOTICENAME = (String)object[2];
					map.put("DESCRIPT", DESCRIPT!=null?DESCRIPT.replaceAll(":", "-").replaceAll(" ", ""):"");
					map.put("FILE_URL", FILE_URL);
					map.put("NOTICENAME", NOTICENAME);
					l.add(map);
				}
				
				
				return l;
			}
		});
	}
	
	public HashMap getAnswerMap() {
		HashMap<String,String> result = new HashMap<String,String>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT QUESTION,DEFULTANSWER FROM BD_VOTE_WJDCWTB WHERE STATUS='显示'");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				String question = rs.getString("QUESTION");
				String defultanswer = rs.getString("DEFULTANSWER");
				result.put(question, defultanswer);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}

	public List<HashMap> industrymsgManegement(int pageSize,int pageNumber,String zhuhy, String zihy) {
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.ZHUHY,B.ZIHY FROM BD_XP_HYXXB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM B WHERE B.IFORM_TITLE='行业信息维护表单')");
		List params = new ArrayList();
		if(zhuhy!=null&&!"".equals(zhuhy)){
			sb.append(" AND B.ZHUHY LIKE ?");
			params.add("%"+zhuhy+"%");
		}
		if(zihy!=null&&!"".equals(zihy)){
			sb.append(" AND B.ZIHY LIKE ?");
			params.add("%"+zihy+"%");
		}
		final String sql1 = sb.toString();
		final List param = params;
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> li = query.list();
				HashMap map;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String zhuhy = (String)object[1];
					String zihy = (String)object[2];
					map.put("INSTANCEID", instanceid.toString());
					map.put("ZHUHY", zhuhy);
					map.put("ZIHY", zihy);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> industrymsgManegementSize(String zhuhy, String zihy) {
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.ZHUHY,B.ZIHY FROM BD_XP_HYXXB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM B WHERE B.IFORM_TITLE='行业信息维护表单')");
		List params = new ArrayList();
		if(zhuhy!=null&&!"".equals(zhuhy)){
			sb.append(" AND B.ZHUHY LIKE ?");
			params.add("%"+zhuhy+"%");
		}
		if(zihy!=null&&!"".equals(zihy)){
			sb.append(" AND B.ZIHY LIKE ?");
			params.add("%"+zihy+"%");
		}
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> li = query.list();
				HashMap map;
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String zhuhy = (String)object[1];
					String zihy = (String)object[2];
					map.put("INSTANCEID", instanceid.toString());
					map.put("ZHUHY", zhuhy);
					map.put("ZIHY", zihy);
					l.add(map);
				}
				return l;
			}
		});
	}
	/**
	 * 信披质量评估导出
	 * @param zqdm
	 * @param zqjc
	 * @param startdate
	 * @return
	 */
	public List<HashMap> getPxjlList(String zqdm,String zqjc,String startdate,String enddate){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		OrgUser user = uc.get_userModel();
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		sql.append("  select * from ( ");
		sql.append(" SELECT z.id,ZQJC, ZQDM, S.KHBH, TO_CHAR(XPPF, 'FM9999990.00') XPPF,Z.NOTICENAME,Z.NOTICENO,Z.NOTICEDATE,Z.Ggdf,2 FS,to_char(2) KFRID,to_char(2) BKFRID,to_char(2) KFSJ,to_char(2) TYPE  ");
		sql.append("  ,FK.ACTION,to_char(FK.CREATETIME,'yyyy-MM-dd hh24:Mi:ss') CREATETIME,FK.CONTENT,FK.USERNAME,FK.STEP_TITLE,FK.BS ");
		sql.append(" FROM (SELECT KH.ZQJC, KH.ZQDM, KHBH, XPPF  FROM (SELECT KHBH,  CAST(DECODE(AVG(GGDF),  0,   '0.00',  TRIM(TO_CHAR(AVG(GGDF), '9999999.99'))) AS  NUMBER(9, 2)) AS XPPF   ");
		sql.append("  FROM BD_MEET_QTGGZL WHERE GGDF IS NOT NULL  AND SPZT = '审批通过'    ");
		if(startdate!=null&&!startdate.equals("")){
			sql.append(" AND TO_CHAR(NOTICEDATE,'yyyy-MM-dd') >= ?");
			parameter.add(startdate);
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append(" AND TO_CHAR(NOTICEDATE,'yyyy-MM-dd') <= ? ");
			parameter.add(enddate);
		}
		sql.append(" GROUP BY KHBH) GG  INNER JOIN (SELECT ZQDM, ZQJC, CUSTOMERNO  FROM BD_ZQB_KH_BASE WHERE 1 = 1) KH  ");
		sql.append("   ON GG.KHBH = KH.CUSTOMERNO  ORDER BY XPPF DESC, ZQDM) S LEFT JOIN BD_MEET_QTGGZL Z ON S.KHBH=Z.KHBH LEFT JOIN  (SELECT KFOP.ACTION,KFOP.CREATETIME,KFOP.CONTENT,KFOP.INSTANCEID,ORG.USERNAME,PRM.STEP_TITLE , '' BS ");
		sql.append("  FROM process_ru_opinion KFOP LEFT JOIN ORGUSER ORG ON ORG.USERID=KFOP.CREATEUSER LEFT JOIN PROCESS_STEP_MAP PRM ON PRM.ACT_DEF_ID=KFOP.ACT_DEF_ID AND PRM.ACT_STEP_ID=KFOP.ACT_STEP_ID) FK ON  ");
		sql.append("  FK.INSTANCEID=Z.LCBS    WHERE Z.SPZT='审批通过'   ");
		sql.append("   UNION all ");
		sql.append("  SELECT z.id,ZQJC, ZQDM, S.KHBH, TO_CHAR(XPPF, 'FM9999990.00') XPPF,Z.NOTICENAME,Z.NOTICENO,Z.NOTICEDATE,Z.Ggdf,FK.FS,FK.KFRID,FK.BKFRID,FK.KFSJ,FK.TYPE,to_char(1) ACTION,to_char(1) CREATETIME,to_char(1) CONTENT  ");
		sql.append("  ,to_char(1) USERNAME,to_char(1) STEP_TITLE ,FK.BS ");
		sql.append("  FROM (SELECT KH.ZQJC, KH.ZQDM, KHBH, XPPF  FROM (SELECT KHBH,  CAST(DECODE(AVG(GGDF),  0,   '0.00',  TRIM(TO_CHAR(AVG(GGDF), '9999999.99'))) AS  NUMBER(9, 2)) AS XPPF ");
		sql.append("  FROM BD_MEET_QTGGZL WHERE GGDF IS NOT NULL  AND SPZT = '审批通过'   ");
		if(startdate!=null&&!startdate.equals("")){
			sql.append(" AND TO_CHAR(NOTICEDATE,'yyyy-MM-dd') >= ?");
			parameter.add(startdate);
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append(" AND TO_CHAR(NOTICEDATE,'yyyy-MM-dd') <= ? ");
			parameter.add(enddate);
		}
		sql.append("  GROUP BY KHBH) GG  INNER JOIN (SELECT ZQDM, ZQJC, CUSTOMERNO  FROM BD_ZQB_KH_BASE WHERE 1 = 1) KH ");
		sql.append("  ON GG.KHBH = KH.CUSTOMERNO  ORDER BY XPPF DESC, ZQDM) S LEFT JOIN BD_MEET_QTGGZL Z ON S.KHBH=Z.KHBH RIGHT JOIN  (SELECT KFQK.FS, ");
		sql.append(" (SELECT USERNAME FROM ORGUSER WHERE USERID=KFQK.KFRID) KFRID,(SELECT USERNAME FROM ORGUSER WHERE USERID=KFQK.BKFRID) BKFRID");
		sql.append(" ,KFQK.KFSJ,KFQK.TYPE,TASKINST.PROC_INST_ID_   ,KFQK.MEMO BS  ");
		sql.append("    FROM bd_xp_kfrzjlb KFQK LEFT JOIN PROCESS_HI_TASKINST TASKINST ON KFQK.TASKID=TASKINST.ID_  WHERE KFQK.FS IS NOT NULL) FK ON  ");
		sql.append(" FK.PROC_INST_ID_=Z.LCBS    WHERE Z.SPZT='审批通过' )  zt where 1=1");
		if (zqjc != null && !zqjc.equals("")) {
			sql.append(" and UPPER(zt.ZQJC) like ? ");
			parameter.add("%"+zqjc.toUpperCase()+"%");
		}
		if (zqdm != null && !zqdm.equals("")) {
			sql.append("and UPPER(zt.ZQDM) like ? ");
			parameter.add("%"+zqdm.toUpperCase()+"%");
		}

		if(startdate!=null&&!startdate.equals("")){
			sql.append(" AND TO_CHAR(zt.NOTICEDATE,'yyyy-MM-dd') >= ?");
			parameter.add(startdate);
		}else{
			sql.append(" AND TO_CHAR(zt.NOTICEDATE,'yyyy-MM-dd') >= TO_CHAR(ADD_MONTHS(SYSDATE,-6),'yyyy-MM-dd')");
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append(" AND TO_CHAR(zt.NOTICEDATE,'yyyy-MM-dd') <= ? ");
			parameter.add(enddate);
		}else{
			sql.append(" AND TO_CHAR(zt.NOTICEDATE,'yyyy-MM-dd') <= TO_CHAR(SYSDATE,'yyyy-MM-dd') ");
		}
		/*if(user.getOrgroleid()==4){
			sql.append(" and zt.ZQDM in( select bzkb.zqdm from bd_zqb_kh_base bzkb where bzkb.customerno in(select s.khbh from bd_mdm_khqxglb s where s.khfzr=? or s.zzcxdd=? or s.zzspr=? or s.fhspr=? or s.cwscbfzr2=? or s.cwscbfzr3=? ) ) ");
			parameter.add(username);
			parameter.add(username);
			parameter.add(username);
			parameter.add(username);
			parameter.add(username);
			parameter.add(username);
		}*/
		sql.append("  ORDER BY zt.ZQJC,zt.ID,zt.CREATETIME,zt.KFSJ ");
		final String sql1 = sql.toString();
		final List<String> list = parameter;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i)!=null && !"".equals(list.get(i).toString())){
						String params=list.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, list.get(i));
				}
				List<Object[]> list = query.list();
				
				HashMap map=null;
				String flag="";
				String FS1 = "";
				String FKRID1 = "";
				String BFKRID1 = "";
				String KFSJ1 = "";
				String TYPE1= "";
				String ACTION1 = "";
				String CREATETIME1 = "";
				String CONTENT1 = "";
				String USERNAME1 = "";
				String STEP_TITLE1= "";
				String gg="";
				int gssl=0;
				int ggsl=0;
				for (Object[] object : list) {
					//map = new HashMap();
					BigDecimal id=(BigDecimal) object[0];
					String ZQJC = (String) object[1];
					String ZQDM = (String)object[2];
					String KHBH= (String)object[3];
					String XPPF = (String)object[4];
					String NOTICENAME = (String)object[5];
					String NOTICENO = (String) object[6];
					Date NOTICEDATE = (Date)object[7];
					String Ggdf= (String)object[8];
					BigDecimal FS = (BigDecimal)object[9];
					String FKRID = (String)object[10];
					String BFKRID = (String) object[11];
					String KFSJ = (String)object[12];
					String TYPE= (String)object[13];
					String ACTION = (String)object[14];
					String CREATETIME = (String)object[15];
					String CONTENT = (String) object[16]==null?"":(String) object[16];
					String USERNAME = (String)object[17];
					String STEP_TITLE= (String)object[18];
					String BZ= (String)object[19]==null?"":(String) object[19];
					if(flag==""){
						map = new HashMap();
						flag=id.toString();
					}
					
					if(id.toString().equals("39")){
						int i=0;
					}
					//第二
					if(flag.equals(id.toString())){
						ggsl++;
						
						map.put("id", id);
						map.put("KHBH", KHBH);
						map.put("ZQJC", ZQJC);
						map.put("ZQDM", ZQDM);
						map.put("XPPF", XPPF);
						map.put("NOTICENAME", NOTICENAME);
						map.put("NOTICENO", NOTICENO);
						map.put("NOTICEDATE", NOTICEDATE);
						map.put("Ggdf", Ggdf);
						map.put("ggsl", ggsl);
						map.put("gssl", gssl);
						if(!FKRID.equals("2")){
							String typename="";
							String name="";
							if(FS1==""){
								if(TYPE!=null){
								String[] types=TYPE.split(",");
								for (int i = 0; i < types.length; i++) {
									Map params = new HashMap();
									params.put(1,types[i]);
									name=DBUTilNew.getDataStr("KFLB", "SELECT KFLB FROM bd_xp_kflxb WHERE ID=? ", params);
									if(!"".equals(BZ)){
										typename=BZ+","+name;
									}else{
										typename=name;
									}
									
								}}
								FS1=FS.toString();
								FKRID1=FKRID;
								BFKRID1=BFKRID;
								KFSJ1=KFSJ;
								TYPE1=typename;
							}else{
								if(TYPE!=null){
								String[] types=TYPE.split(",");
								for (int i = 0; i < types.length; i++) {
									Map params = new HashMap();
									params.put(1,types[i]);
									name=DBUTilNew.getDataStr("KFLB", "SELECT KFLB FROM bd_xp_kflxb WHERE ID=? ", params);
									typename=typename+","+name;
								}}
								FS1=FS1+"@@"+FS;
								FKRID1=FKRID1+"@@"+FKRID;
								BFKRID1=BFKRID1+"@@"+BFKRID;
								KFSJ1=KFSJ1+"@@"+KFSJ;
								TYPE1=TYPE1+"@@"+typename;
							}
							
						}
						map.put("FS", FS1);
						map.put("FKRID", FKRID1);
						map.put("BFKRID", BFKRID1);
						map.put("KFSJ", KFSJ1);
						map.put("TYPE", TYPE1);
						
						if(!ACTION.equals("1")){
							if(ACTION1==""){
								ACTION1=ACTION;
								CREATETIME1=CREATETIME;
								CONTENT1=CONTENT;
								USERNAME1=USERNAME;
								STEP_TITLE1=STEP_TITLE;
							}else{
								ACTION1=ACTION1+"@@"+ACTION;
								CREATETIME1=CREATETIME1+"@@"+CREATETIME;
								CONTENT1=CONTENT1+"@@"+CONTENT;
								USERNAME1=USERNAME1+"@@"+USERNAME;
								STEP_TITLE1=STEP_TITLE1+"@@"+STEP_TITLE;
							}
						}
						map.put("ACTION", ACTION1);
						map.put("CREATETIME", CREATETIME1);
						map.put("CONTENT", CONTENT1==null?"":CONTENT1);
						map.put("USERNAME", USERNAME1);
						map.put("STEP_TITLE", STEP_TITLE1);
						
					}else{
						l.add(map);
						flag=id.toString();
						map = new HashMap();
						ggsl=0;
						FS1 = "";
						FKRID1 = "";
						BFKRID1 = "";
						KFSJ1 = "";
						TYPE1= "";
						ACTION1 = "";
						CREATETIME1 = "";
						CONTENT1 = "";
						USERNAME1 = "";
						STEP_TITLE1= "";
						map.put("id", id);
						map.put("KHBH", KHBH);
						map.put("ZQJC", ZQJC);
						map.put("ZQDM", ZQDM);
						map.put("XPPF", XPPF);
						map.put("NOTICENAME", NOTICENAME);
						map.put("NOTICENO", NOTICENO);
						map.put("NOTICEDATE", NOTICEDATE);
						map.put("Ggdf", Ggdf);
						map.put("gssl", gssl);
						map.put("ggsl", ggsl);
						if(!FKRID.equals("2")){
							String typename="";
							String name="";
							if(TYPE!=null){
								String[] types=TYPE.split(",");
								for (int i = 0; i < types.length; i++) {
									Map params = new HashMap();
									params.put(1,types[i]);
									name=DBUTilNew.getDataStr("KFLB", "SELECT KFLB FROM bd_xp_kflxb WHERE ID=? ", params);
									if(!"".equals(BZ)){
										typename=BZ+","+name;
									}else{
										typename=name;
									}
								}
							}
								FS1=FS.toString();
								FKRID1=FKRID;
								BFKRID1=BFKRID;
								KFSJ1=KFSJ;
								TYPE1=typename;
							
							
						}
						map.put("FS", FS1);
						map.put("FKRID", FKRID1);
						map.put("BFKRID", BFKRID1);
						map.put("KFSJ", KFSJ1);
						map.put("TYPE", TYPE1);
						
						if(!ACTION.equals("1")){
							
								ACTION1=ACTION;
								CREATETIME1=CREATETIME;
								CONTENT1=CONTENT;
								USERNAME1=USERNAME;
								STEP_TITLE1=STEP_TITLE;
							
						}
						map.put("ACTION", ACTION1);
						map.put("CREATETIME", CREATETIME1);
						map.put("CONTENT", CONTENT1==null?"":CONTENT1);
						map.put("USERNAME", USERNAME1);
						map.put("STEP_TITLE", STEP_TITLE1);
					}
					
				}
				if(map !=null && !map.isEmpty())
					l.add(map);
				
				List<HashMap> ls = new ArrayList<HashMap>();
				HashMap maps=null;
				for (int i = 0; i < l.size(); i++) {
					
					String[] action=l.get(i).get("ACTION").toString().split("@@");
					String[] createtime=l.get(i).get("CREATETIME").toString().split("@@");
					String[] content=l.get(i).get("CONTENT").toString().split("@@");
					String[] username=l.get(i).get("USERNAME").toString().split("@@");
					String[] step=l.get(i).get("STEP_TITLE").toString().split("@@");
					String[] FS=l.get(i).get("FS").toString().split("@@");
					String[] FKRID=l.get(i).get("FKRID").toString().split("@@");
					String[] BFKRID=l.get(i).get("BFKRID").toString().split("@@");
					String[] KFSJ=l.get(i).get("KFSJ").toString().split("@@");
					String[] TYPE=l.get(i).get("TYPE").toString().split("@@");
					for (int j = 0; j < username.length; j++) {
							maps = new HashMap();
							maps.put("id", l.get(i).get("id").toString());
							maps.put("KHBH", l.get(i).get("KHBH").toString());
							maps.put("ZQJC", l.get(i).get("ZQJC").toString());
							maps.put("ZQDM", l.get(i).get("ZQDM").toString());
							maps.put("XPPF", l.get(i).get("XPPF").toString());
							maps.put("NOTICENAME", l.get(i).get("NOTICENAME").toString());
							maps.put("NOTICENO", l.get(i).get("NOTICENO").toString());
							maps.put("NOTICEDATE", l.get(i).get("NOTICEDATE").toString());
							maps.put("Ggdf", l.get(i).get("Ggdf").toString());
							maps.put("ggsl", l.get(i).get("ggsl").toString());
							maps.put("gssl", l.get(i).get("gssl").toString());
							
							maps.put("ACTION", action[j]);
							maps.put("CREATETIME", createtime[j]);
							try {
								maps.put("CONTENT", content[j]);
							} catch (Exception e) {
								maps.put("CONTENT", "");
							}
							try {
								maps.put("FS", FS[j]);
							} catch (Exception e) {
								maps.put("FS","");
							}
							try {
								maps.put("FKRID", FKRID[j]);
							} catch (Exception e) {
								maps.put("FKRID","");
							}
							try {
								maps.put("BFKRID", BFKRID[j]);
							} catch (Exception e) {
								maps.put("BFKRID", "");
							}
							try {
								maps.put("KFSJ", KFSJ[j]);
							} catch (Exception e) {
								maps.put("KFSJ", "");
							}
							try {
								maps.put("TYPE", TYPE[j]);
							} catch (Exception e) {
								maps.put("TYPE", "");
							}
							maps.put("USERNAME", username[j]);
							maps.put("STEP_TITLE", step[j]);
							ls.add(maps);
						
							
						
						
						
					}
				}
				return ls;
			}
		});
	}
	/**
	 * 员工绩效导出
	 * @param zqdm
	 * @param zqjc
	 * @param startdate fullname,model,orderby,starttime,endtime
	 * @param enddate
	 * @return
	 */
	public List<HashMap> getYgjxList(String fullname,String model,String orderby,String startdate, String enddate){
		String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date stdate=null;
		Date endate=null;
	
			 try {
				if(startdate!=null&&!startdate.equals(""))
					 stdate= sdf.parse(startdate);
				 if(enddate!=null&&!enddate.equals(""))
					 endate=  sdf.parse(enddate);
				 	if(stdate.compareTo(endate)>0){
				 		return new ArrayList();
				 	}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ArrayList();
             }
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		OrgUser user = uc.get_userModel();
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		sql.append(" SELECT *  FROM (SELECT  O.USERNAME  AS FULLNAME, O.DEPARTMENTNAME,   A.FS,  decode(S.ENTITYTITLE,  '其他公告资料流程', '公告',  '日常业务呈报', S.ENTITYTITLE,  '项目') AS MODEL,  ");
		sql.append(" A.KFSJ,A.KFRID,A.BKFRID,A.TYPE,A.TABLENAME,A.TAG,T.ZF FROM BD_XP_KFRZJLB A LEFT JOIN ORGUSER O  ON A.BKFRID = O.USERID  LEFT JOIN SYS_ENGINE_METADATA S  ON A.TABLENAME = S.ENTITYNAME  ");
		sql.append("  LEFT JOIN (SELECT   SUM(A.FS) ZF,  A.BKFRID FROM BD_XP_KFRZJLB A GROUP BY  A.BKFRID ) T ON T.BKFRID=A.BKFRID ");
		sql.append("  WHERE O.ORGROLEID <> 3) WHERE 1 = 1   ");
	
		if(startdate!=null&&!startdate.equals("")){
			sql.append(" AND TO_DATE(?,'yyyy-MM-dd HH24:mi:ss') <= TO_DATE(KFSJ,'yyyy-MM-dd HH24:mi:ss')");
			parameter.add(startdate);
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append(" AND TO_DATE(KFSJ,'yyyy-MM-dd HH24:mi:ss') <= TO_DATE(?,'yyyy-MM-dd HH24:mi:ss')");
			parameter.add(enddate);
		}
		if(fullname!=null&&!fullname.equals("")){
			sql.append(" AND  FULLNAME LIKE ?"); 
			parameter.add("%"+fullname+"%");
		}
		if(model!=null&&!model.equals("")){
			sql.append(" AND MODEL LIKE ?");
			parameter.add("%"+model+"%");
		}else{
			/*if(!configParameter.equals("sxzq") && !configParameter.equals("hlzq")){
				sql.append(" AND (MODEL='日常业务呈报' or MODEL='公告') ");
			}*/
			sql.append(" AND (MODEL='日常业务呈报' or MODEL='公告') ");
		}
	
		if(orderby!=null&&!orderby.equals("")){
			sql.append(" ORDER BY ?");
			parameter.add(orderby);
		}else{
			sql.append(" ORDER BY DEPARTMENTNAME");
		}
		sql.append(" ,FULLNAME");
		final String sql1 = sql.toString();
		final List<String> list = parameter;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i)!=null && !"".equals(list.get(i).toString())){
						String params=list.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, list.get(i));
				}
				List<Object[]> list = query.list();
				
				HashMap map=null;
				
				for (Object[] object : list) {
					map=new HashMap<String,String>();
					BigDecimal TAG=(BigDecimal)object[9];
					String TABLENAME = (String) object[8];
					String FULLNAME= (String)object[0];
					String DEPARTMENTNAME = (String)object[1];
					String MODEL = (String) object[3];
					String KFSJ = (String)object[4];
					String KFRID = (String)object[5];
					BigDecimal ZF = (BigDecimal) object[2];
					String TYPES = (String) object[7];
					BigDecimal ZFS=(BigDecimal)object[10];
					if("BD_XP_TXRZGLLCB".equals(TABLENAME)){
						continue;
					}
					Long dataid = TAG.longValue();
					Long instanceId = ProcessAPI.getInstance().getInstaceId(TABLENAME, dataid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					HashMap formdata = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					if(formdata==null){
						continue;
					}
					map.put("FSMK","");
					if("RCYWCB".equals(TABLENAME)){
						String khbh=formdata.get("KHBH").toString();
						Map params = new HashMap();
						params.put(1, khbh);
						String zqdm=DBUTilNew.getDataStr("zqdm", "select s.zqdm from bd_zqb_kh_base s where s.customerno= ? ", params);
						String zqjc=DBUTilNew.getDataStr("zqjc", "select s.zqjc from bd_zqb_kh_base s where s.customerno= ? ", params);
						map.put("FSMK", "日程业务呈报-"+zqdm+"-"+zqjc+"("+formdata.get("CREATEDATE").toString()+")"+"-"+formdata.get("SXMC").toString());
					}else if("BD_XP_QTGGZLLC".equals(TABLENAME)){
						String khbh=formdata.get("KHBH").toString();
						Map params = new HashMap();
						params.put(1, khbh);
						
						map.put("FSMK", "公告-"+formdata.get("ZQDMXS").toString()+"-"+formdata.get("ZQJCXS").toString()+"-"+formdata.get("NOTICENO").toString()+"-"+formdata.get("NOTICENAME").toString());
					}
					Map param = new HashMap();
					param.put(1, KFRID);
					String KFRXM=DBUTilNew.getDataStr("USERNAME", "SELECT USERNAME FROM ORGUSER WHERE USERID= ? ", param);
					map.put("ZF", ZF==null?"":ZF);
					map.put("FULLNAME", FULLNAME);
					map.put("DEPARTMENTNAME", DEPARTMENTNAME);
					map.put("MODEL", MODEL);
					map.put("KFSJ", KFSJ);
					map.put("KFRXM", KFRXM);
					map.put("ZFS", ZFS==null?"":ZFS);
					String KFLB="";
					if(TYPES!=null && !"".equals(TYPES)){
						String[] str=TYPES.split(",");
						for (int i = 0; i < str.length; i++) {
							Map params = new HashMap();
							params.put(1, str[i]);
							String kflb=DBUTilNew.getDataStr("kflb", "select kflb from bd_xp_kflxb where  id= ? ", params);
							if(i==str.length-1){
								KFLB=KFLB+kflb;
							}else{
								KFLB=KFLB+kflb+"\n";
							}
						}
					}
					map.put("KFLB", KFLB);
					l.add(map);
				}
				
				return l;
			}
		});
	}
	public List<HashMap<String,String>> getWtdaList(String ggid){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String usernames=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		List wtList=getWtList(ggid);
		List<HashMap<String,String>> dataList=new ArrayList<HashMap<String,String>>();
		List<String> parameter = new ArrayList();// 存放参数
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		sql.append(" select * from (  select distinct kh.zqjc,   kh.zqdm,    org.username,    hf.status,  hf.ggid from (select distinct userid, ggid, status, customerno  from bd_xp_hfqkb  where ggid = ? ) hf left join bd_vote_wjdc b ");
		sql.append("  on b.customerno = hf.customerno and b.tzggid = hf.ggid  left join orguser org  on org.userid = hf.userid left join bd_vote_xpzcfkgtb gt on hf.ggid = gt.ggid and hf.customerno = gt.customerno");
		sql.append("   left join bd_zqb_kh_base kh  on kh.customerno = hf.customerno where 1 = 1 ");
		parameter.add(ggid);
		if(uc.get_userModel().getOrgroleid().equals(new Long(4))){
			sql.append("  and  kh.zqdm in (select bzkb.zqdm    from bd_zqb_kh_base bzkb   where bzkb.customerno in  (select s.khbh   from bd_mdm_khqxglb s ");
			sql.append("     where s.khfzr =?   or s.zzcxdd =?     or s.zzspr = ?   or s.fhspr =? ");
			sql.append("   or s.cwscbfzr2 = ?    or s.cwscbfzr3 = ?     or GGFBR =? or qynbrysh =?)) ");
			parameter.add(usernames);
			parameter.add(usernames);
			parameter.add(usernames);
			parameter.add(usernames);
			parameter.add(usernames);
			parameter.add(usernames);
			parameter.add(usernames);
			parameter.add(usernames);
		}
		sql.append("  ) zfk left join bd_vote_wjdc bvw on bvw.tzggid=zfk.ggid and bvw.userid=zfk.zqdm order by zfk.status desc, zfk.zqdm");
	
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for(int i=0;i<parameter.size();i++){
				ps.setString(i+1, parameter.get(i));
			}
			rs = ps.executeQuery();
			int count=1;
			while (rs.next()) {
				HashMap<String,String> result = new HashMap<String,String>();
				String status = rs.getString("STATUS");
				String zqjc = rs.getString("ZQJC");
				String zqdm = rs.getString("ZQDM");
				String username = rs.getString("USERNAME");
				result.put("zqjc", zqjc==null?"":zqjc);
				result.put("zqdm", zqdm==null?"":zqdm);
				result.put("username",username==null?"":username);
		
				for (int i = 0; i < 50; i++) {
					String wt= rs.getString("WT"+(i+1))==null?"":rs.getString("WT"+(i+1));
					String pl= rs.getString("PL"+(i+1))==null?"":rs.getString("PL"+(i+1));
					String as= rs.getString("AS"+(i+1))==null?"":rs.getString("AS"+(i+1));
					String bz= rs.getString("BZ"+(i+1))==null?"":rs.getString("BZ"+(i+1));
					String pz= rs.getString("PZ"+(i+1))==null?"":rs.getString("PZ"+(i+1));
					String yj= rs.getString("YJXYSFFS"+(i+1))==null?"":rs.getString("YJXYSFFS"+(i+1));
					for (int j = 0; j < wtList.size(); j++) {
						result.put("pl"+(i+1),"");
						result.put("as"+(i+1),"");
						result.put("bz"+(i+1),"");
						result.put("yj"+(i+1),"");
						Map map = (HashMap) wtList.get(j);
						
						if(map.get("question").toString().equals(wt)&&status.equals("已回复")){
							result.put("pl"+(i+1),pl);
							result.put("as"+(i+1),as);
							if(bz==""  && pz ==""){
								result.put("bz"+(i+1),"");
							}else{
								result.put("bz"+(i+1),"情况说明："+bz+"\n\n"+"批注："+pz);
							}
							result.put("yj"+(i+1),yj);
							break;
							
						}
					}
				}
				count++;
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	//比较问题，确认下是使用问题表的问题还是使用答案表存的问题
	public List<HashMap> getWtList(String ggid){
		List zzwtList=null;
		List wtList=getYwt();
		List<String> parameter = new ArrayList();// 存放参数
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		sql.append("  select * from (select * from bd_vote_wjdc where tzggid= ? order by id) where rownum=1  ");
		parameter.add(ggid);
		try {
			List<HashMap> l = new ArrayList<HashMap>();
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for(int i=0;i<parameter.size();i++){
				ps.setString(i+1, parameter.get(i));
			}
			rs = ps.executeQuery();
			int count=0;
			int wts=0;
			while (rs.next()) {
				HashMap maps=null;
				for (int i = 0; i < 50; i++) {
					maps=new HashMap<String,String>();
					String wt= rs.getString("WT"+(i+1))==null?"":rs.getString("WT"+(i+1));
					if(!wt.equals("")){
						maps.put("question", wt);
						maps.put("type", "信披自查");
						wts++;
						l.add(maps);
					}
					for (int j = 0; j < wtList.size(); j++) {
						Map map = (HashMap) wtList.get(j);
						if(map.get("question").toString().equals(wt)){
							count++;
							break;
						}
					}
				}
			}
			if(wtList.size()==wts && count==wtList.size()){
				zzwtList=wtList;
			}else{
				zzwtList=l;
			}
		//	System.out.println("问题相同数："+count+"\n问题总条数："+wts+"\n答题库问题条数："+wtList.size()+"\n");
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return zzwtList;
		
	}
	public List<HashMap> getYwt(){
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		sql.append(" select question,type from bd_vote_wjdcwtb where type='信披自查' or type='知悉问题' order by type , sortid  ");
		final String sql1 = sql.toString();
		final List<String> list = parameter;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map=null;
				for (Object[] object : list) {
					map=new HashMap<String,String>();
					String question = (String) object[0];
					String type = (String) object[1];
					map.put("question", question);
					map.put("type", type);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getReportList(int pageNumber, int pageSize, String departmentname, String username) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc ._userModel;
		final String orguserid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT B.INSTANCEID,B.ILLUSTRATION,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE FROM BDXPDDXXBGB B LEFT JOIN ORGUSER O ON B.CREATEUSERID=O.USERID WHERE 1=1"); 
		if(user.getOrgroleid()!=5l){
			sb.append(" AND ");
			sb.append("(");
			sb.append("B.CREATEUSERID=?");
			params.add(orguserid);
			if(user.getIsmanager()==1){
				sb.append(" OR ");
				sb.append("B.CREATEUSERID IN(SELECT USERID FROM ORGUSER WHERE DEPARTMENTNAME=(SELECT DEPARTMENTNAME FROM ORGUSER WHERE USERID=?))");
				params.add(orguserid);
			}
			sb.append(")");
		}
		if(departmentname!=null&&!departmentname.equals("")){
			sb.append(" AND O.DEPARTMENTNAME LIKE ?");
			params.add("%"+departmentname+"%");
		}
		if(username!=null&&!username.equals("")){
			sb.append(" AND B.CREATEUSER LIKE ?");
			params.add("%"+username+"%");
		}
		sb.append(" AND B.ID IN(SELECT MAX(ID) FROM BDXPDDXXBGB GROUP BY CREATEUSERID) ORDER BY B.ID DESC");
		final String sql1 = sb.toString();
		final List param = params;
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> li = query.list();
				HashMap map;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String illustration = (String)object[1];
					String createuser = (String)object[2];
					String createuserid = (String)object[3];
					Date createdate = (Date)object[4];
					map.put("INSTANCEID", instanceid.toString());
					map.put("ILLUSTRATION", illustration);
					map.put("CREATEUSER", createuser);
					map.put("CREATEUSERID", createuserid);
					map.put("CREATEDATE", createdate==null?"":sdf.format(createdate));
					map.put("SHOWDEL", orguserid.equals(createuserid));
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getReportListSize(String departmentname, String username) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc ._userModel;
		String orguserid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT B.INSTANCEID,B.ILLUSTRATION,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE FROM BDXPDDXXBGB B LEFT JOIN ORGUSER O ON B.CREATEUSERID=O.USERID WHERE 1=1"); 
		if(user.getOrgroleid()!=5l){
			sb.append(" AND ");
			sb.append("(");
			sb.append("B.CREATEUSERID=?");
			params.add(orguserid);
			if(user.getIsmanager()==1){
				sb.append(" OR ");
				sb.append("B.CREATEUSERID IN(SELECT USERID FROM ORGUSER WHERE DEPARTMENTNAME=(SELECT DEPARTMENTNAME FROM ORGUSER WHERE USERID=?))");
				params.add(orguserid);
			}
			sb.append(")");
		}
		if(departmentname!=null&&!departmentname.equals("")){
			sb.append(" AND O.DEPARTMENTNAME LIKE ?");
			params.add("%"+departmentname+"%");
		}
		if(username!=null&&!username.equals("")){
			sb.append(" AND B.CREATEUSER LIKE ?");
			params.add("%"+username+"%");
		}
		sb.append(" AND B.ID IN(SELECT MAX(ID) FROM BDXPDDXXBGB GROUP BY CREATEUSERID) ORDER BY B.ID DESC");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> li = query.list();
				HashMap map;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String illustration = (String)object[1];
					String createuser = (String)object[2];
					String createuserid = (String)object[3];
					Date createdate = (Date)object[4];
					map.put("INSTANCEID", instanceid.toString());
					map.put("ILLUSTRATION", illustration);
					map.put("CREATEUSER", createuser);
					map.put("CREATEUSERID", createuserid);
					map.put("CREATEDATE", createdate==null?"":sdf.format(createdate));
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getReportList(int pageNumber, int pageSize, String userid) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc ._userModel;
		final String orguserid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT B.INSTANCEID,B.ILLUSTRATION,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE FROM BDXPDDXXBGB B WHERE B.CREATEUSERID = ? ORDER BY B.ID DESC");
		params.add(userid);
		final String sql1 = sb.toString();
		final List param = params;
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> li = query.list();
				HashMap map;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String illustration = (String)object[1];
					String createuser = (String)object[2];
					String createuserid = (String)object[3];
					Date createdate = (Date)object[4];
					map.put("INSTANCEID", instanceid.toString());
					map.put("ILLUSTRATION", illustration);
					map.put("CREATEUSER", createuser);
					map.put("CREATEUSERID", createuserid);
					map.put("CREATEDATE", createdate==null?"":sdf.format(createdate));
					map.put("SHOWDEL", orguserid.equals(createuserid));
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getReportListSize(String userid) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT B.INSTANCEID,B.ILLUSTRATION,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE FROM BDXPDDXXBGB B WHERE B.CREATEUSERID = ?");
		params.add(userid);
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<Map> l = new ArrayList<Map>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> li = query.list();
				HashMap map;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				for (Object[] object : li) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String illustration = (String)object[1];
					String createuser = (String)object[2];
					String createuserid = (String)object[3];
					Date createdate = (Date)object[4];
					map.put("INSTANCEID", instanceid.toString());
					map.put("ILLUSTRATION", illustration);
					map.put("CREATEUSER", createuser);
					map.put("CREATEUSERID", createuserid);
					map.put("CREATEDATE", createdate==null?"":sdf.format(createdate));
					l.add(map);
				}
				return l;
			}
		});
	}
}
