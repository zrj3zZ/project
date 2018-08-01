package com.ibpmsoft.project.zqb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.math.BigDecimal;
import java.util.Map;
import org.activiti.engine.task.Task;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.ProcessAPI;

public class ZqbImpEventDAO extends HibernateDaoSupport {
	public List<HashMap> getList(int pageSize, int pageNumber, String sxmc,
		String startdate, String enddate){
	UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
	final String userid = uc.get_userModel().getUserid();
	final String userfullname = userid + "[" + uc.get_userModel().getUsername() + "]";
	List<HashMap> returnList = new ArrayList<HashMap>();
	final int startRow1 = (pageNumber - 1) * pageSize;
	final int pageSize1 = pageSize;
	StringBuffer sql = new StringBuffer(
			"SELECT C.INSTANCEID,A.SXMC,TO_CHAR(A.ZCFKSJ,'yyyy-MM-dd hh24:mi:ss') ZCFKSJ,A.FKZT,A.LCBH,A.LCBS,A.YXID,A.RWID,A.ID,A.STEPID,A.CJR,A.SPZT FROM BD_XP_ZYDDSXB A," +
			"SYS_ENGINE_FORM_BIND C WHERE 1=1 AND FORMID = '142' AND METADATAID = '156' AND A.ID = C.DATAID  AND C.INSTANCEID IS NOT NULL ");
	if (sxmc != null && !sxmc.equals("")) {
		sql.append(" AND A.SXMC LIKE ?");
	}
	if (startdate != null && !"".equals(startdate)) {
		sql.append(" AND TO_CHAR(A.ZCFKSJ,'yyyy-MM-dd')>= ?");
	}
	if (enddate != null && !"".equals(enddate)) {
		sql.append(" AND TO_CHAR(A.ZCFKSJ,'yyyy-MM-dd')<= ?");
	}
	sql.append(" AND (CJR=? OR SPR=? "
					+ " OR INSTANCEID IN (SELECT INSTANCEID FROM BD_XP_ZYDDSXFKRB A INNER JOIN SYS_ENGINE_FORM_BIND B ON FORMID ="
					+ " (SELECT SUBFORMID FROM SYS_ENGINE_SUBFORM T WHERE SUBTABLEKEY = 'SUBFORM_SXFKRBD')"
					+ " AND METADATAID = '159' AND A.ID = B.DATAID)) ORDER BY A.FKZT,A.ID DESC");
	//Long actDefId=ProcessAPI.getInstance().getProcessActDefId("");
	//ProcessAPI.getInstance().getProcessOpinionList(actDefId, instanceid);
	final String sql1 = sql.toString();
	
	final String final_sxmc  = sxmc;
	final String final_startdate  = startdate;
	final String final_enddate  = enddate;

	return this.getHibernateTemplate().executeFind(new HibernateCallback() {
		
		
		public Object doInHibernate(org.hibernate.Session session) throws HibernateException,
				SQLException {
			DBUtilInjection d=new DBUtilInjection();
			List<Map> l = new ArrayList<Map>();
			Query query = session.createSQLQuery(sql1);
			int i=0;
			if (final_sxmc != null && !final_sxmc.equals("")) {
				if(d.HasInjectionData(final_sxmc)){
					return l;
				}
				query.setString(i, "%" + final_sxmc + "%");i++;
			}
			if (final_startdate != null && !final_startdate.equals("")) {
				if(d.HasInjectionData(final_startdate)){
					return l;
				}
				query.setString(i, final_startdate);i++;
			}
			if (final_enddate != null && !final_enddate.equals("")) {
				if(d.HasInjectionData(final_enddate)){
					return l;
				}
				query.setString(i, final_enddate);i++;
			}
			if (userid != null && !userid.equals("")) {
				if(d.HasInjectionData(userid)){
					return l;
				}
				
			}
			if (userfullname != null && !userfullname.equals("")) {
				if(d.HasInjectionData(userfullname)){
					return l;
				}
				
			}
			query.setString(i, userid);i++;
			query.setString(i, userfullname);i++;
			
			query.setFirstResult(startRow1);
			query.setMaxResults(pageSize1);
			
			List<Object[]> list = query.list();
			HashMap map;
			HashMap m = new HashMap();
			BigDecimal b;
			int n = 0;
			for (Object[] object : list) {
				map = new HashMap();
				BigDecimal instanceid = (BigDecimal) object[0];
				String sxmc = (String) object[1];
				String ZCFKSJ = (String) object[2];
				String FKZT = (String) object[3];
				String LCBH=(String)object[4];
				String  LCBS= (String) object[5];
				String  YXID= (String) object[6];
				String  RWID= (String) object[7];
				BigDecimal id = (BigDecimal) object[8];
				String STEPID=(String) object[9];
				String cjr=(String)object[10];
				String spzt=(String)object[11];
				map.put("INSTANCEID", instanceid);
				map.put("SXMC", sxmc);
				map.put("ZCFKSJ",ZCFKSJ);
				map.put("FKZT", FKZT);
				map.put("LCBH", LCBH);
				map.put("LCBS", LCBS);
				map.put("YXID", YXID);
				map.put("RWID", RWID);
				map.put("STEPID", STEPID);
				map.put("SPZT", spzt);
				map.put("ID", id);
				if(LCBS!=null&&!LCBS.equals("")){
					List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(LCBH, Long.parseLong(LCBS));
				    if(pro.size()>0){
				    	Long prc=pro.get(0).getPrcDefId();
				    	map.put("PRCID", prc);
				    }
				}
				//创建人而且是未反馈的时候，是可以编辑的，反馈之后不可以编辑
				if(userid.equals(cjr)&&
						(map.get("FKZT")==null||map.get("FKZT").equals("")||map.get("FKZT").equals("0"))){
					map.put("ISBJ", true);
				}else{
					map.put("ISBJ", false);
				}
				//更新taskid
				if(map.get("STEPID")!=null){
				//任务列表
				List<Task> tasklist=ProcessAPI.getInstance().getUserProcessTaskList("ZYDDSXLC:1:81304", map.get("STEPID").toString(), userid);
				  for(Task task:tasklist){
					  if(map.get("YXID")==null){
						  map.put("YXID", 0);
					  }
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
				if(map.get("FKZT")!=null&&!map.get("FKZT").equals("")&&map.get("FKZT").equals("1")){
					map.put("FKZT","已反馈");
				}else{
					map.put("FKZT","未反馈");
				}
				if (map.get("SPZT") != null
						&& !map.get("SPZT").toString().equals("")
						&& !map.get("SPZT").toString().equals("驳回")) {
					map.put("ISFK", false);
				} else {
					map.put("ISFK", true);
				}
				l.add(map);
			}
			return l;
		}
	});

	}		
	
	@SuppressWarnings("unchecked")
	public List<HashMap> getcxddList(int pageSize, int pageNumber, String sxmc,
			String startdate, String enddate){
			StringBuffer sql = new StringBuffer(
					"select a.instanceid,a.sxmc,to_char(a.ZCFKSJ,'yyyy-MM-dd hh24:mi:ss') ZCFKSJ,a.FKZT,a.LCBH,a.LCBS,a.YXID,a.RWID,a.ID,a.STEPID,a.CJR,a.spzt FROM BD_XP_ZYDDSXB a where 1=1");
			if (sxmc != null && !sxmc.equals("")) {
				sql.append(" and a.sxmc like ?");
			}
			if (startdate != null && !"".equals(startdate)) {
				sql.append(" and to_char(a.ZCFKSJ,'yyyy-MM-dd')>= ?");
			}
			if (enddate != null && !"".equals(enddate)) {
				sql.append(" and to_char(a.ZCFKSJ,'yyyy-MM-dd')<= ?");
			}
			final String sql1 = sql.toString();
			final int startRow1 = (pageNumber - 1) * pageSize;
			final int pageSize1 = pageSize;
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			final String userid = uc.get_userModel().getUserid();
				
					@SuppressWarnings("rawtypes")
					List<HashMap> l = new ArrayList<HashMap>();
					Connection conn=DBUtil.open();
					PreparedStatement stmt = null;
					ResultSet rs=null;
					try{
						stmt = conn.prepareStatement(sql1);
						int i=1;
						if (sxmc != null && !sxmc.equals("")) {
							stmt.setString(i, "%" + sxmc + "%");i++;
						}
						if (startdate != null && !"".equals(startdate)) {
							stmt.setString(i, startdate);i++;
						}
						if (enddate != null && !"".equals(enddate)) {
							stmt.setString(i, enddate);i++;
						}
						rs = stmt.executeQuery();
					HashMap map;
					HashMap m = new HashMap();
					BigDecimal b;
					int n = 0;
					while (rs.next()) {
						map = new HashMap();
						BigDecimal instanceid = rs.getBigDecimal(1);
						 sxmc = rs.getString(2);
						String ZCFKSJ = rs.getString(3);
						String FKZT = rs.getString(4);
						String LCBH=rs.getString(5);
						String  LCBS= rs.getString(6);
						String  YXID= rs.getString(7);
						String  RWID= rs.getString(8);
						BigDecimal id = rs.getBigDecimal(9);
						String STEPID=rs.getString(10);
						String cjr=rs.getString(11);
						String spzt=rs.getString(12);
						map.put("INSTANCEID", instanceid);
						map.put("SXMC", sxmc);
						map.put("ZCFKSJ",ZCFKSJ);
						map.put("FKZT", FKZT);
						map.put("LCBH", LCBH);
						map.put("LCBS", LCBS);
						map.put("YXID", YXID);
						map.put("RWID", RWID);
						map.put("STEPID", STEPID);
						map.put("SPZT", spzt);
						map.put("ID", id);
						if(LCBS!=null&&!LCBS.equals("")){
							List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(LCBH, Long.parseLong(LCBS));
						    if(pro.size()>0){
						    	Long prc=pro.get(0).getPrcDefId();
						    	map.put("PRCID", prc);
						    }
						}
						//更新taskid
						if(map.get("STEPID")!=null){
						//任务列表
						List<Task> tasklist=ProcessAPI.getInstance().getUserProcessTaskList("ZYDDSXLC:1:81304", map.get("STEPID").toString(), userid);
						  for(Task task:tasklist){
							  if(map.get("YXID")==null){
								  map.put("YXID", 0);
							  }
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
						if(map.get("FKZT")!=null&&!map.get("FKZT").equals("")&&map.get("FKZT").equals("1")){
							map.put("FKZT","已反馈");
						}else{
							map.put("FKZT","未反馈");
						}
						if (map.get("SPZT") != null
								&& !map.get("SPZT").toString().equals("")
								&& !map.get("SPZT").toString().equals("驳回")) {
							map.put("ISFK", false);
						} else {
							map.put("ISFK", true);
						}
						l.add(map);
					}
					}catch (Exception e){
						logger.error(e,e);
					}finally{
						DBUtil.close(conn, stmt, rs);
					}					
					return l;
			}		
			
		}
		
