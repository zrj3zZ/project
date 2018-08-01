package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
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

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.FileType;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.process.runtime.pvm.impl.route.PvmRouteEngine;
import com.iwork.sdk.FileUploadAPI;

public class ShanXiZqbBgProjectManageDAO extends HibernateDaoSupport {
	private final static String CN_FILENAME = "/common.properties";
	PvmRouteEngine pvmRouteEngine = new PvmRouteEngine();

	public HashMap<String,List<HashMap<String, Object>>> getListMap(String sql, List parameter) {
		HashMap<String,List<HashMap<String, Object>>> listMap = new HashMap<String,List<HashMap<String, Object>>>();
		List<HashMap<String,Object>> dataRunList=new ArrayList<HashMap<String,Object>>();
		List<HashMap<String,Object>> dataCloseList=new ArrayList<HashMap<String,Object>>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				if(parameter.get(i) instanceof String){
					ps.setString(index++, parameter.get(i).toString().toUpperCase());
				}else if(parameter.get(i) instanceof Integer){
					ps.setInt(index++, Integer.parseInt(parameter.get(i).toString()));
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				Long instanceid = rs.getLong("INSTANCEID");
				String jyf = rs.getString("JYF");
				String jydsf = rs.getString("JYDSF");
				String sgfs = rs.getString("SGFS");
				String manager = rs.getString("MANAGER");
				String owner = rs.getString("OWNER");
				String dt = rs.getString("DT");
				Long xmzt = rs.getLong("XMZT");
				Long cnum = rs.getLong("CNUM");
				//Integer rownum = rs.getInt("RM");
				result.put("ID", id);
				result.put("INSTANCEID", instanceid);
				result.put("JYF", jyf);
				result.put("JYDSF", jydsf);
				result.put("SGFS", sgfs);
				result.put("MANAGER", manager);
				result.put("OWNER", owner);
				result.put("DT", dt);
				result.put("XMZT", xmzt);
				result.put("CNUM", cnum);
				if(xmzt==1){
					dataRunList.add(result);
				}else if(xmzt==0){
					dataCloseList.add(result);
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		listMap.put("RunList", dataRunList);
		listMap.put("CloseList", dataCloseList);
		return listMap;
	}

	public List<HashMap<String, Object>> getTaskList(String sql,String xmbh) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			ps.setString(1, xmbh);
			ps.setString(2, xmbh);
			ps.setString(3, xmbh);
			ps.setString(4, xmbh);
			ps.setString(5, xmbh);
			ps.setString(6, xmbh);
			ps.setString(7, xmbh);
			ps.setString(8, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jdmc = rs.getString("JDMC");
				String lxtbr = rs.getString("LXTBR");
				String lcbh = rs.getString("LCBH");
				Long lcbs = rs.getLong("LCBS");
				Long taskid = rs.getLong("TASKID");
				String lxfj = rs.getString("LXFJ");
				String spzt = rs.getString("SPZT");
				result.put("ID", id);
				result.put("JDMC", jdmc);
				result.put("LXTBR", lxtbr);
				result.put("LCBH", lcbh);
				result.put("LCBS", lcbs);
				result.put("TASKID", taskid);
				result.put("LXFJ", lxfj);
				result.put("SPZT", spzt);
				result.put("XMBH", xmbh);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public List<HashMap> getQtRunList(String customername,String clbm,String czbm,String cyrxm,int runPageNumber,int runPageSize){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long orgroleid = user.getOrgroleid();
		String cuid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT S.INSTANCEID,B.CUSTOMERNAME,B.STARTDATE,B.MANAGER,B.OWNER,B.FZJGMC,B.SSSYB,B.A08,B.KHLXR,B.KHLXDH,B.HTJE,B.A01,B.XMBZ");
		sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
		sb.append(" AND B.STATUS='执行中'");
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND B.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(clbm!=null&&!clbm.equals("")){
			sb.append(" AND B.FZJGMC LIKE ?");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sb.append(" AND B.SSSYB LIKE ?");
			params.add("%"+czbm+"%");
		}
		if(cyrxm!=null&&!cyrxm.equals("")){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,instr(B.MANAGER,'[',1)+1, instr(B.MANAGER,']',1)-(instr(B.MANAGER,'[',1)+1)) AS MANAGER,");
			sb.append("SUBSTR(B.OWNER,instr(B.OWNER,'[',1)+1, instr(B.OWNER,']',1)-(instr(B.OWNER,'[',1)+1)) AS OWNER,");
			sb.append("CREATEUSER");
			sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.CREATEUSER LIKE ? OR A.MANAGER LIKE ? OR A.OWNER LIKE ? OR B.NAME LIKE ?)");
			sb.append(")");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
		}
		if(orgroleid!=5){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,0, INSTR(B.MANAGER,'[',1)-1) AS MANAGER,");
			sb.append("SUBSTR(B.OWNER,0, INSTR(B.OWNER,'[',1)-1) AS OWNER,");
			sb.append("CREATEUSERID");
			sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.CREATEUSERID = ? OR A.MANAGER = ? OR A.OWNER = ? OR B.USERID = ?)");
			sb.append(")");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
		}
		
		sb.append(" ORDER BY B.ID DESC");
		final int pageSize1 = runPageSize;
		final int startRow1 = (runPageNumber - 1) * runPageSize;
		final List param = params;
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setFirstResult(startRow1);
				List<HashMap> l = new ArrayList<HashMap>();
				query.setMaxResults(pageSize1);
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal INSTANCEID =(BigDecimal)object[0]; 
					String CUSTOMERNAME =(String)object[1];
					Date STARTDATE =(Date)object[2];
					String MANAGER =(String)object[3];
					String OWNER =(String)object[4];
					String FZJGMC =(String)object[5];
					String SSSYB =(String)object[6];
					String A08 =(String)object[7];
					String KHLXR =(String)object[8];
					String KHLXDH =(String)object[9];
					BigDecimal HTJE =(BigDecimal)object[10];
					BigDecimal A01 =(BigDecimal)object[11];
					String XMBZ =(String)object[12];
					map.put("INSTANCEID", INSTANCEID==null?"0":INSTANCEID.toString());
					map.put("CUSTOMERNAME", CUSTOMERNAME);
					map.put("STARTDATE", STARTDATE==null?"":sdf.format(STARTDATE));
					map.put("MANAGER", MANAGER);
					map.put("OWNER", OWNER);
					map.put("FZJGMC", FZJGMC);
					map.put("SSSYB", SSSYB);
					map.put("A08", A08);
					map.put("KHLXR", KHLXR);
					map.put("KHLXDH", KHLXDH);
					map.put("HTJE", HTJE==null?"":HTJE.toString());
					map.put("A01", A01==null?"":A01.toString());
					map.put("XMBZ", XMBZ);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getQtRunListSize(String customername,String clbm,String czbm,String cyrxm){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long orgroleid = user.getOrgroleid();
		String cuid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT S.INSTANCEID,B.CUSTOMERNAME,B.STARTDATE,B.MANAGER,B.OWNER,B.FZJGMC,B.SSSYB,B.A08,B.KHLXR,B.KHLXDH,B.HTJE,B.A01,B.XMBZ");
		sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
		sb.append(" AND B.STATUS='执行中'");
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND B.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(clbm!=null&&!clbm.equals("")){
			sb.append(" AND B.FZJGMC LIKE ?");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sb.append(" AND B.SSSYB LIKE ?");
			params.add("%"+czbm+"%");
		}
		if(cyrxm!=null&&!cyrxm.equals("")){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,instr(B.MANAGER,'[',1)+1, instr(B.MANAGER,']',1)-(instr(B.MANAGER,'[',1)+1)) AS MANAGER,");
			sb.append("SUBSTR(B.OWNER,instr(B.OWNER,'[',1)+1, instr(B.OWNER,']',1)-(instr(B.OWNER,'[',1)+1)) AS OWNER,");
			sb.append("CREATEUSER");
			sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.CREATEUSER LIKE ? OR A.MANAGER LIKE ? OR A.OWNER LIKE ? OR B.NAME LIKE ?)");
			sb.append(")");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
		}
		if(orgroleid!=5){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,0, INSTR(B.MANAGER,'[',1)-1) AS MANAGER,");
			sb.append("SUBSTR(B.OWNER,0, INSTR(B.OWNER,'[',1)-1) AS OWNER,");
			sb.append("CREATEUSERID");
			sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.CREATEUSERID = ? OR A.MANAGER = ? OR A.OWNER = ? OR B.USERID = ?)");
			sb.append(")");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
		}
		
		sb.append(" ORDER BY B.ID DESC");
		final List param = params;
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal INSTANCEID =(BigDecimal)object[0]; 
					String CUSTOMERNAME =(String)object[1];
					Date STARTDATE =(Date)object[2];
					String MANAGER =(String)object[3];
					String OWNER =(String)object[4];
					String FZJGMC =(String)object[5];
					String SSSYB =(String)object[6];
					String A08 =(String)object[7];
					String KHLXR =(String)object[8];
					String KHLXDH =(String)object[9];
					BigDecimal HTJE =(BigDecimal)object[10];
					BigDecimal A01 =(BigDecimal)object[11];
					String XMBZ =(String)object[12];
					map.put("INSTANCEID", INSTANCEID==null?"0":INSTANCEID.toString());
					map.put("CUSTOMERNAME", CUSTOMERNAME);
					map.put("STARTDATE", STARTDATE==null?"":sdf.format(STARTDATE));
					map.put("MANAGER", MANAGER);
					map.put("OWNER", OWNER);
					map.put("FZJGMC", FZJGMC);
					map.put("SSSYB", SSSYB);
					map.put("A08", A08);
					map.put("KHLXR", KHLXR);
					map.put("KHLXDH", KHLXDH);
					map.put("HTJE", HTJE==null?"":HTJE.toString());
					map.put("A01", A01==null?"":A01.toString());
					map.put("XMBZ", XMBZ);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getQtCloseList(String customername,String clbm,String czbm,String cyrxm,int runPageNumber,int runPageSize){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long orgroleid = user.getOrgroleid();
		String cuid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT S.INSTANCEID,B.CUSTOMERNAME,B.STARTDATE,B.MANAGER,B.OWNER,B.FZJGMC,B.SSSYB,B.A08,B.KHLXR,B.KHLXDH,B.HTJE,B.A01,B.XMBZ");
		sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
		sb.append(" AND B.STATUS='已完成'");
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND B.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(clbm!=null&&!clbm.equals("")){
			sb.append(" AND B.FZJGMC LIKE ?");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sb.append(" AND B.SSSYB LIKE ?");
			params.add("%"+czbm+"%");
		}
		if(cyrxm!=null&&!cyrxm.equals("")){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,instr(B.MANAGER,'[',1)+1, instr(B.MANAGER,']',1)-(instr(B.MANAGER,'[',1)+1)) AS MANAGER,");
			sb.append("SUBSTR(B.OWNER,instr(B.OWNER,'[',1)+1, instr(B.OWNER,']',1)-(instr(B.OWNER,'[',1)+1)) AS OWNER,");
			sb.append("CREATEUSER");
			sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.CREATEUSER LIKE ? OR A.MANAGER LIKE ? OR A.OWNER LIKE ? OR B.NAME LIKE ?)");
			sb.append(")");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
		}
		if(orgroleid!=5){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,0, INSTR(B.MANAGER,'[',1)-1) AS MANAGER,");
			sb.append("SUBSTR(B.OWNER,0, INSTR(B.OWNER,'[',1)-1) AS OWNER,");
			sb.append("CREATEUSERID");
			sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.CREATEUSERID = ? OR A.MANAGER = ? OR A.OWNER = ? OR B.USERID = ?)");
			sb.append(")");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
		}
		
		sb.append(" ORDER BY B.ID DESC");
		final int pageSize1 = runPageSize;
		final int startRow1 = (runPageNumber - 1) * runPageSize;
		final List param = params;
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				List<HashMap> l = new ArrayList<HashMap>();
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal INSTANCEID =(BigDecimal)object[0]; 
					String CUSTOMERNAME =(String)object[1];
					Date STARTDATE =(Date)object[2];
					String MANAGER =(String)object[3];
					String OWNER =(String)object[4];
					String FZJGMC =(String)object[5];
					String SSSYB =(String)object[6];
					String A08 =(String)object[7];
					String KHLXR =(String)object[8];
					String KHLXDH =(String)object[9];
					BigDecimal HTJE =(BigDecimal)object[10];
					BigDecimal A01 =(BigDecimal)object[11];
					String XMBZ =(String)object[12];
					map.put("INSTANCEID", INSTANCEID==null?"0":INSTANCEID.toString());
					map.put("CUSTOMERNAME", CUSTOMERNAME);
					map.put("STARTDATE", STARTDATE==null?"":sdf.format(STARTDATE));
					map.put("MANAGER", MANAGER);
					map.put("OWNER", OWNER);
					map.put("FZJGMC", FZJGMC);
					map.put("SSSYB", SSSYB);
					map.put("A08", A08);
					map.put("KHLXR", KHLXR);
					map.put("KHLXDH", KHLXDH);
					map.put("HTJE", HTJE==null?"":HTJE.toString());
					map.put("A01", A01==null?"":A01.toString());
					map.put("XMBZ", XMBZ);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getQtCloseListSize(String customername,String clbm,String czbm,String cyrxm){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long orgroleid = user.getOrgroleid();
		String cuid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT S.INSTANCEID,B.CUSTOMERNAME,B.STARTDATE,B.MANAGER,B.OWNER,B.FZJGMC,B.SSSYB,B.A08,B.KHLXR,B.KHLXDH,B.HTJE,B.A01,B.XMBZ");
		sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
		sb.append(" AND B.STATUS='已完成'");
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND B.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(clbm!=null&&!clbm.equals("")){
			sb.append(" AND B.FZJGMC LIKE ?");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sb.append(" AND B.SSSYB LIKE ?");
			params.add("%"+czbm+"%");
		}
		if(cyrxm!=null&&!cyrxm.equals("")){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,instr(B.MANAGER,'[',1)+1, instr(B.MANAGER,']',1)-(instr(B.MANAGER,'[',1)+1)) AS MANAGER,");
			sb.append("SUBSTR(B.OWNER,instr(B.OWNER,'[',1)+1, instr(B.OWNER,']',1)-(instr(B.OWNER,'[',1)+1)) AS OWNER,");
			sb.append("CREATEUSER");
			sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.CREATEUSER LIKE ? OR A.MANAGER LIKE ? OR A.OWNER LIKE ? OR B.NAME LIKE ?)");
			sb.append(")");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
		}
		if(orgroleid!=5){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,0, INSTR(B.MANAGER,'[',1)-1) AS MANAGER,");
			sb.append("SUBSTR(B.OWNER,0, INSTR(B.OWNER,'[',1)-1) AS OWNER,");
			sb.append("CREATEUSERID");
			sb.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他项目')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.CREATEUSERID = ? OR A.MANAGER = ? OR A.OWNER = ? OR B.USERID = ?)");
			sb.append(")");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
		}
		
		sb.append(" ORDER BY B.ID DESC");
		final List param = params;
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				List<HashMap> l = new ArrayList<HashMap>();
				Query query = session.createSQLQuery(sql);
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal INSTANCEID =(BigDecimal)object[0]; 
					String CUSTOMERNAME =(String)object[1];
					Date STARTDATE =(Date)object[2];
					String MANAGER =(String)object[3];
					String OWNER =(String)object[4];
					String FZJGMC =(String)object[5];
					String SSSYB =(String)object[6];
					String A08 =(String)object[7];
					String KHLXR =(String)object[8];
					String KHLXDH =(String)object[9];
					BigDecimal HTJE =(BigDecimal)object[10];
					BigDecimal A01 =(BigDecimal)object[11];
					String XMBZ =(String)object[12];
					map.put("INSTANCEID", INSTANCEID==null?"0":INSTANCEID.toString());
					map.put("CUSTOMERNAME", CUSTOMERNAME);
					map.put("STARTDATE", STARTDATE==null?"":sdf.format(STARTDATE));
					map.put("MANAGER", MANAGER);
					map.put("OWNER", OWNER);
					map.put("FZJGMC", FZJGMC);
					map.put("SSSYB", SSSYB);
					map.put("A08", A08);
					map.put("KHLXR", KHLXR);
					map.put("KHLXDH", KHLXDH);
					map.put("HTJE", HTJE==null?"":HTJE.toString());
					map.put("A01", A01==null?"":A01.toString());
					map.put("XMBZ", XMBZ);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap<String, Object>> getQtTaskList(String xmbh) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TYXM.ID,TYXM.JDMC,JDDATA.TBR,JDDATA.INSTANCEID,JDDATA.FJ,JD.YJINS FROM (SELECT ID,SORTID,JDMC,STATE FROM BD_ZQB_TYXM_INFO WHERE XMLX='其他项目') TYXM");
		sql.append(" LEFT JOIN (");
		sql.append("SELECT ORG.USERNAME TBR,JD.XMBH,JD.JDBH,S.INSTANCEID,JD.FJ FROM BD_ZQB_TYXMJD JD LEFT JOIN SYS_ENGINE_FORM_BIND S ON JD.ID=S.DATAID");
		sql.append(" INNER JOIN ORGUSER ORG ON JD.TBRID=ORG.USERID");
		sql.append(" WHERE S.FORMID IN(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE IN('通用项目立项','通用工作进度汇报','通用资料归档','其他初步尽调')) AND XMBH=?");
		sql.append(") JDDATA ON TYXM.ID=JDDATA.JDBH");
		sql.append(" LEFT JOIN (");
		sql.append(" SELECT B.*,O.USERNAME AS CREATEUSER,D.YJINS FROM BD_ZQB_BGXMLX B LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID "); 
		sql.append(" WHERE S.FORMID=148) D ON B.LCBH=D.GGINS LEFT JOIN	ORGUSER O ON B.TBRID=O.USERID WHERE B.XMBH=?) JD ON TYXM.ID=JD.JDBH WHERE TYXM.STATE=1 ");
		sql.append(" ORDER BY TYXM.SORTID");
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, xmbh);
			ps.setString(2, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jdmc = rs.getString("JDMC");
				String tbr = rs.getString("TBR");
				String instanceid = rs.getString("INSTANCEID")==null?"0":rs.getString("INSTANCEID");
				String yjins = rs.getString("YJINS");
				StringBuffer fjstr = new StringBuffer();
				String fj = rs.getString("FJ");
				if(fj!=null&&!fj.equals("")){
					List<FileUpload> files = FileUploadAPI.getInstance().getFileUploads(fj);
					for (int i = 0; i < files.size(); i++) {
						if(i==(files.size()-1)){
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName()).append("</a>");
						}else{
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName()).append("</a>").append("<br>");
						}
					}
				}
				result.put("ID", id);
				result.put("JDMC", jdmc);
				result.put("LXTBR", tbr);
				result.put("LXFJ", fjstr);
				result.put("XMBH", xmbh);
				result.put("INSTANCEID", instanceid);
				result.put("YJINS", yjins);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public List<HashMap<String, Object>> getCwTaskList(String sql,String xmbh) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			ps.setString(1, xmbh);
			ps.setString(2, xmbh);
			ps.setString(3, xmbh);
			ps.setString(4, xmbh);
			ps.setString(5, xmbh);
			ps.setString(6, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jdmc = rs.getString("JDMC");
				String lxtbr = rs.getString("LXTBR");
				String lcbh = rs.getString("LCBH");
				Long lcbs = rs.getLong("LCBS");
				Long taskid = rs.getLong("TASKID");
				String lxfj = rs.getString("LXFJ");
				String spzt = rs.getString("SPZT");
				result.put("ID", id);
				result.put("JDMC", jdmc);
				result.put("LXTBR", lxtbr);
				result.put("LCBH", lcbh);
				result.put("LCBS", lcbs);
				result.put("TASKID", taskid);
				result.put("LXFJ", lxfj);
				result.put("SPZT", spzt);
				result.put("XMBH", xmbh);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public HashMap<String, List<HashMap<String, Object>>> getCwListMap( String sql, List parameter) {
		HashMap<String,List<HashMap<String, Object>>> listMap = new HashMap<String,List<HashMap<String, Object>>>();
		List<HashMap<String,Object>> dataRunList=new ArrayList<HashMap<String,Object>>();
		List<HashMap<String,Object>> dataCloseList=new ArrayList<HashMap<String,Object>>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				if(parameter.get(i) instanceof String){
					ps.setString(index++, parameter.get(i).toString().toUpperCase());
				}else if(parameter.get(i) instanceof Integer){
					ps.setInt(index++, Integer.parseInt(parameter.get(i).toString()));
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				Long instanceid = rs.getLong("INSTANCEID");
				String customername = rs.getString("CUSTOMERNAME");
				String manager = rs.getString("MANAGER");
				String owner = rs.getString("OWNER");
				String dt = rs.getString("DT");
				Long xmzt = rs.getLong("XMZT");
				Long cnum = rs.getLong("CNUM");
				result.put("ID", id);
				result.put("INSTANCEID", instanceid);
				result.put("CUSTOMERNAME", customername);
				result.put("MANAGER", manager);
				result.put("OWNER", owner);
				result.put("DT", dt);
				result.put("XMZT", xmzt);
				result.put("CNUM", cnum);
				if(xmzt==1){
					dataRunList.add(result);
				}else if(xmzt==0){
					dataCloseList.add(result);
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		listMap.put("RunList", dataRunList);
		listMap.put("CloseList", dataCloseList);
		return listMap;
	}
	public List<HashMap>getThxmList(String xmmc, Date startdate, Date enddate,String gjjd,String scbk,int pageSize, int pageNumber,String userid,String thxmlc){
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		//获取当前登录人信息
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String usname=uc._userModel.getUsername();
		String orgRoleId=UserContextUtil.getInstance().getCurrentUserContext()
				.get_orgRole().getId();
		String THXMKSY = getConfigUUID("THXMKSY");
		boolean flag=true;
		if(THXMKSY!=null){
			String[] xmckr=THXMKSY.split(",");
			for (int i = 0; i < xmckr.length; i++) {
				if(xmckr[i].equals(uc._userModel.getUserid())){
					flag=false;
					break;
				}
			}
		}
		sql.append("select DISTINCT xmmc,xmgjjd,TO_CHAR(fqrq,'yyyy-MM-dd') fqrq,xmscbk,ZBSCBJBR,rzxqms,spzt,lcbh,yxid,steptid,prcid,taskid,instanceid,lcinstanceid,qcrid,extend1,insid from BD_XP_TXGLRZWLB ");
		sql.append(" left join (select s.owner_ username,s.proc_inst_id_ insid from process_hi_taskinst s  union select s.assignee_ username,s.proc_inst_id_ insid from process_hi_taskinst s ) t on t.insid =lcinstanceid");
		sql.append(" WHERE 1=1"); 
		
		if(xmmc!=null&&!xmmc.equals("")){
			if(d.HasInjectionData(xmmc)){
				return l;
			}
			sql.append(" and xmmc like ?");
			parameter.add("%"+xmmc+"%");
		}
		if(gjjd!=null&&!gjjd.equals("")){
			if(d.HasInjectionData(gjjd)){
				return l;
			}
			sql.append(" and xmgjjd like ?");
			parameter.add("%"+gjjd+"%");
		}
		if(scbk!=null&&!scbk.equals("")){
			if(d.HasInjectionData(scbk)){
				return l;
			}
			sql.append(" and xmscbk = ?");
			parameter.add(scbk);
		}
		if(startdate!=null&&!startdate.equals("")){
			if(d.HasInjectionData(startdate.toString())){
				return l;
			}
			sql.append("and fqrq >= ? ");
		}
		if(enddate!=null&&!enddate.equals("")){
			if(d.HasInjectionData(enddate.toString())){
				return l;
			}
			sql.append("and fqrq <= ? ");
		}
		/*//判断当前用户是否处于复核人、领导、管理岗节点 返回false 为存在
		boolean isseeall = this.currentpermissions(userid+"["+usname+"]", thxmlc);
		//判断市场部总经理
		if(isseeall)*/
		if(flag){
			sql.append(" and username=? ");
		}
		sql.append(" order by fqrq desc ");
		final String sql1 = sql.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List<String> list = parameter;
		final Date start = startdate;
		final Date endate = enddate;
		final String user = userid;
		final String xsuser = userid+"["+usname+"]";
		final String orgRoleId1 = orgRoleId;
		final boolean flags = flag;
		//final boolean isseeall1 = isseeall;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				int j=0;
				for(int i=0;i<list.size();i++){
					query.setString(i, list.get(i).toString());
					j++;
				}
				if(start!=null){
					query.setDate(j, start);j++;
				}
				if(endate!=null){
					query.setDate(j, endate);j++;
				}
				if(flags){
					query.setString(j, user);
				}
				
				
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String xmmc = (String) object[0];
					String xmgjjd = (String) object[1];
					String fqrq = (String)object[2];
					String xmscbk = (String)object[3];
					String zbscbjbr = (String)object[4];
					String rzxqms = (String)object[5];
					String spzt = (String)object[6];
					String lcbh = (String)object[7];
					BigDecimal yxid = (BigDecimal)object[8];
					String steptid = (String)object[9];
					String prcid = (String)object[10];
					String taskid = (String)object[11];
					BigDecimal instanceid = (BigDecimal) object[12];
					String lcinstanceid = (String) object[13];
					String qcrid = (String) object[14];
					String extend1=(String) object[15];
					map.put("XMMC", xmmc);
					map.put("XMGJJD", xmgjjd);
					map.put("FQRQ", fqrq);
					map.put("XMSCBK", xmscbk);
					map.put("ZBSCBJBR", zbscbjbr);
					map.put("RZXQMS", rzxqms);
					map.put("SPZT", spzt);
					map.put("LCBH", lcbh);
					map.put("YXID", yxid==null||yxid.equals("")?"0":yxid.toString());
					map.put("STEPTID", steptid);
					map.put("PRCID", prcid);
					map.put("TASKID", taskid);
					map.put("INSTANCEID", instanceid==null||instanceid.equals("")?"0":instanceid.toString());
					map.put("LCINSTANCEID", lcinstanceid);
					map.put("extend1", extend1);
					map.put("isFBR", qcrid.equals(user)?"1":"0");
					l.add(map);
				}
				return l;
			}
		});
	}
	public HashMap getMap(String xmmc){
		StringBuffer sql=new StringBuffer();
		sql.append(" select * from ( ");
		sql.append(" select z.userid,z.username from orguser z where z.departmentid=  ");
		sql.append(" (select s.departmentid from orguser s left join BD_XP_TXRZGLLCB t on s.userid=t.qcrid where t.xmmc=?) ");
		sql.append("  and z.Ismanager=1 order by z.id desc) where rownum=1 ");
		HashMap<String,Object> map = new HashMap<String,Object>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, xmmc);
			
			rs = ps.executeQuery();
			while (rs.next()) {
				
				String userid = rs.getString("userid");
				String username = rs.getString("username");
				
				map.put("userid", userid);
				map.put("username", username);
				
				
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return map;
		
	}
	public List<HashMap> getThxmListSizels(String xmmc, Date startdate, Date enddate,String gjjd,String scbk,String thxmlc){
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		//获取当前登录人信息
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String usname=uc._userModel.getUsername();
		String orgRoleId=UserContextUtil.getInstance().getCurrentUserContext()
				.get_orgRole().getId();
		String THXMKSY = getConfigUUID("THXMKSY");
		boolean flag=true;
		if(THXMKSY!=null){
			String[] xmckr=THXMKSY.split(",");
			for (int i = 0; i < xmckr.length; i++) {
				if(xmckr[i].equals(uc._userModel.getUserid())){
					flag=false;
					break;
				}
			}
		}
		sql.append("select DISTINCT xmmc,xmgjjd,TO_CHAR(fqrq,'yyyy-MM-dd') fqrq,xmscbk,ZBSCBJBR,rzxqms,spzt,lcbh,yxid,steptid,prcid,taskid,instanceid,lcinstanceid,qcrid,extend1,insid from BD_XP_TXGLRZWLB ");
		sql.append(" left join (select s.owner_ username,s.proc_inst_id_ insid from process_hi_taskinst s  union select s.assignee_ username,s.proc_inst_id_ insid from process_hi_taskinst s ) t on t.insid =lcinstanceid");
		sql.append(" WHERE 1=1"); 
		
		if(xmmc!=null&&!xmmc.equals("")){
			if(d.HasInjectionData(xmmc)){
				return l;
			}
			sql.append(" and xmmc like ?");
			parameter.add("%"+xmmc+"%");
		}
		if(gjjd!=null&&!gjjd.equals("")){
			if(d.HasInjectionData(gjjd)){
				return l;
			}
			sql.append(" and xmgjjd like ?");
			parameter.add("%"+gjjd+"%");
		}
		if(scbk!=null&&!scbk.equals("")){
			if(d.HasInjectionData(scbk)){
				return l;
			}
			sql.append(" and xmscbk = ?");
			parameter.add(scbk);
		}
		if(startdate!=null&&!startdate.equals("")){
			if(d.HasInjectionData(startdate.toString())){
				return l;
			}
			sql.append("and fqrq >= ? ");
		}
		if(enddate!=null&&!enddate.equals("")){
			if(d.HasInjectionData(enddate.toString())){
				return l;
			}
			sql.append("and fqrq <= ? ");
		}
		/*//判断当前用户是否处于复核人、领导、管理岗节点 返回false 为存在
		boolean isseeall = this.currentpermissions(userid+"["+usname+"]", thxmlc);
		//判断市场部总经理
		if(isseeall)*/
		if(flag)
			sql.append(" and username=? ");

		final String sql1 = sql.toString();
		
		final List<String> list = parameter;
		final Date start = startdate;
		final Date endate = enddate;
		final String user = uc._userModel.getUserid();
		final String xsuser =  uc._userModel.getUserid()+"["+usname+"]";
		final String orgRoleId1 = orgRoleId;
		final boolean flags = flag;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int j=0;
				for(int i=0;i<list.size();i++){
					query.setString(i, list.get(i).toString());
					j++;
				}
				if(start!=null){
					query.setDate(j, start);j++;
				}
				if(endate!=null){
					query.setDate(j, endate);j++;
				}
				if(flags) query.setString(j, user);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String xmmc = (String) object[0];
					String xmgjjd = (String) object[1];
					String fqrq = (String)object[2];
					
					map.put("XMMC", xmmc);
					map.put("XMGJJD", xmgjjd);
					
					l.add(map);
				}
				return l;
			}
		});
			
		}
	public List<HashMap> getThxmListSize(String xmmc, String startdate, String enddate,String gjjd,String scbk,String thxmlc){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String THXMKSY = getConfigUUID("THXMKSY");
		boolean flag=true;
		if(THXMKSY!=null){
			String[] xmckr=THXMKSY.split(",");
			for (int i = 0; i < xmckr.length; i++) {
				if(xmckr[i].equals(uc._userModel.getUserid())){
					flag=false;
					break;
				}
			}
		}
		OrgUser user1 = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		DBUtilInjection d=new DBUtilInjection();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date stdate1=null;
		Date endate1=null;
		try {
			 if(startdate!=null&&!startdate.equals(""))
				 stdate1= sdf.parse(startdate);
			 if(enddate!=null&&!enddate.equals(""))
				 endate1=  sdf.parse(enddate);
		} catch (ParseException e) {
			logger.error(e,e);
		}
		List l=new ArrayList();
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		//获取当前登录人信息
	
		String usname=uc._userModel.getUsername();
		String orgRoleId=UserContextUtil.getInstance().getCurrentUserContext()
				.get_orgRole().getId();
		
		sql.append(" SELECT TO_CHAR(fqrq, 'yyyy-MM-dd') fqrq,  ZBSCBJBR,  (select departmentname from orguser where userid = y.qcrid),   xmmc,  xmgjjd,  xmscbk,    rzxqms, ");
		sql.append(" extend1,   spzt,   (select username from orguser where userid = t.username) username, z.createtime, z.content,  z.action,  z.attach,  p.step_title,  t.name_, ");
		sql.append("    y.id FROM (select distinct  Y.* from BD_XP_TXGLRZWLB Y INNER join (  select distinct * from (select s.assignee_     username,  s.proc_inst_id_ insid, s.id_, ");
		sql.append("  s.name_ from process_hi_taskinst s  union  select  s.owner_     username,   s.proc_inst_id_ insid, s.id_,  s.name_  from process_hi_taskinst s)  ) t on insid=lcinstanceid ");
		if(flag){
			sql.append(" where t.username= ? ");
		}
		sql.append("  ) Y left join (select s.assignee_     username,  s.proc_inst_id_ insid,   s.id_,   s.name_  from process_hi_taskinst s) t  on t.insid = y.lcinstanceid ");
		sql.append(" left join process_ru_opinion z on z.taskid = t.id_ left join PROCESS_STEP_MAP p   on p.act_step_id = z.act_step_id     WHERE 1 = 1 "); 
		
		if(xmmc!=null&&!xmmc.equals("")){
			if(d.HasInjectionData(xmmc)){
				return l;
			}
			sql.append(" and xmmc like ?");
			parameter.add("%"+xmmc+"%");
		}
		if(gjjd!=null&&!gjjd.equals("")){
			if(d.HasInjectionData(gjjd)){
				return l;
			}
			sql.append(" and xmgjjd like ?");
			parameter.add("%"+gjjd+"%");
		}
		if(scbk!=null&&!scbk.equals("")){
			if(d.HasInjectionData(scbk)){
				return l;
			}
			sql.append(" and xmscbk = ?");
			parameter.add(scbk);
		}
		if(startdate!=null&&!startdate.equals("")){
			if(d.HasInjectionData(startdate.toString())){
				return l;
			}
			sql.append("and fqrq >= ? ");
		}
		if(enddate!=null&&!enddate.equals("")){
			if(d.HasInjectionData(enddate.toString())){
				return l;
			}
			sql.append("and fqrq <= ? ");
		}
		/*//判断当前用户是否处于复核人、领导、管理岗节点 返回false 为存在
		boolean isseeall = this.currentpermissions(userid+"["+usname+"]", thxmlc);
		//判断市场部总经理
		if(isseeall)*/
		//sql.append(" and Y.QCRID=? ");

		sql.append("  order by y.id desc,z.createtime ");
		final String sql1 = sql.toString();
		final List<String> list = parameter;
		final Date start = stdate1;
		final Date endate = endate1;
		final String user =user1.getUserid();
		final String orgRoleId1 = orgRoleId;
		final boolean flags = flag;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				
				int j=0;
				if(flags){
					query.setString(j, user);j++;
				}
				
				for(int i=0;i<list.size();i++){
					query.setString(j, list.get(i).toString());
					j++;
				}
				if(start!=null){
					query.setDate(j, start);j++;
				}
				if(endate!=null){
					query.setDate(j, endate);j++;
				}
			
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				int n=1;
				for (Object[] object : list) {
					map = new HashMap();
					String fqrq = (String) object[0];
					String zbscbjbr = (String) object[1];
					String bm = (String)object[2];
					String xmmc = (String)object[3];
					String gjjd = (String)object[4];
					String scbk = (String)object[5];
					String rzxqms = (String)object[6];
					String smzhpj = (String)object[7];
					String smzt = (String)object[8];
					String username = (String)object[9];
					Date blsj = (Date)object[10];
					String content = (String)object[11];
					String action = (String) object[12];
					String attach = (String) object[13];
					String title = (String) object[14];
					String tbtitle=(String) object[15];
					BigDecimal  id=(BigDecimal) object[16];
					String filename="";
					if(attach!=null && !"".equals(attach)){
						String[] filenames=attach.split(",");
						for (int i = 0; i < filenames.length; i++) {
							String f =DBUTilNew.getDataStr("file_src_name", " select s.file_src_name from sys_upload_file s where file_id='"+filenames[i]+"'", null);
							filename=f+"\n";
						}
					}
					map.put("xh", n);
					map.put("fqrq", fqrq==null?"":fqrq);
					map.put("zbscbjbr",zbscbjbr==null?"":zbscbjbr);
					map.put("bm", bm==null?"":bm);
					map.put("xmmc", xmmc==null?"":xmmc);
					map.put("gjjd", gjjd==null?"":gjjd);
					map.put("scbk", scbk==null?"":scbk);
					map.put("rzxqms", rzxqms==null?"":rzxqms);
					map.put("smzhpj", smzhpj==null?"":smzhpj);
					map.put("smzt", smzt==null?"":smzt);
					map.put("blsj", blsj==null?"":blsj);
					map.put("username", username==null?"":username);
					map.put("content", content==null?"":content);
					map.put("action", action==null?"":action);
					map.put("filename", filename==null?"":filename);
					map.put("title", title==null?tbtitle:title);
					map.put("id", id);
					l.add(map);
					n++;
				}
				return l;
			}
		});
			
		}
	public boolean currentpermissions(String user,String thxmlc){
		StringBuffer sql = new StringBuffer();
		String xmjbrjd= "step_7f4c275a-fd20-9977-c739-b19f9f33d5e1";
		sql.append("select route_param from PROCESS_STEP_ROUTE where act_def_id=? and route_schema=1 and act_step_id <>?");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int j=0;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, thxmlc);
			ps.setString(2, xmjbrjd);
			rs = ps.executeQuery();
			while (rs.next()) {
				String param = rs.getString("route_param");
				if(param!=null&&!"".equals(param)){
				if(param.contains(",")){
				String [] username = param.split(",");
				for(int i=0;i<username.length;i++){
					if(username[i].equals(user)){
						return false;
					}
				}
			}
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return true;
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
