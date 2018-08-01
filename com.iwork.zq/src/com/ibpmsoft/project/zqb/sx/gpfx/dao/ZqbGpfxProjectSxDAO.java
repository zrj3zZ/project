package com.ibpmsoft.project.zqb.sx.gpfx.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;

public class ZqbGpfxProjectSxDAO extends HibernateDaoSupport {

	public List<HashMap> getRunlist(int pageSize, int pageNumber, String customername, String sshy, String czbm, String cyrname) {
//		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,K.SSHY,B.GPFXSL,B.MJZJZE,B.MANAGER FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
//		List params=new ArrayList();
//		if(customername!=null&&!"".equals(customername)){
//			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
//			params.add("%" + customername + "%");
//		}
//		if(sshy!=null&&!"".equals(sshy)){
//			sb.append(" AND K.SSHY LIKE ? ");
//			params.add("%"+sshy+"%'");
//		}
//		if(czbm!=null&&!"".equals(czbm)){
//			sb.append(" AND B.CZBM LIKE ? ");
//			params.add("%" + czbm + "%'");
//		}
//		if(cyrname!=null&&!"".equals(cyrname)){
//			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? ");
//			params.add("%" + cyrname.toUpperCase() + "%'");
//			params.add("%" + cyrname.toUpperCase() + "%'");
//			sb.append(" OR B.PROJECTNO IN (");
//			sb.append(" SELECT A.PROJECTNO FROM (");
//			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
//			sb.append(" ) A LEFT JOIN (");
//			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
//			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
//			sb.append(" )");
//			params.add("%" + cyrname.toUpperCase() + "%'");
//			sb.append(")");
//		}
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,K.SSHY,B.GPFXSL,B.MJZJZE,B.MANAGER FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(sshy!=null&&!"".equals(sshy)){
			sb.append(" AND K.SSHY LIKE ? ");
			params.add("%" + sshy + "%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrname!=null&&!"".equals(cyrname)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? ");
			params.add("%" + cyrname.toUpperCase() + "%");
			params.add("%" + cyrname.toUpperCase() + "%");
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrname.toUpperCase() + "%");
			sb.append(")");
		}
		sb.append(" ORDER BY B.PROJECTNAME,B.CREATEDATE");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
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
//				query.setFirstResult(0);
//			
//				query.setMaxResults(10);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String owner = (String) object[1];
					String customername = (String) object[2];
					String zqdm = (String) object[3];
					String zqjc = (String) object[4];
					String zrfs = (String) object[5];
					String sshy = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					String manager = (String) object[9];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("SSHY",sshy);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					map.put("MANAGER",manager);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getRunlistSize(String customername, String sshy, String czbm, String cyrname) {
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,K.SSHY,B.GPFXSL,B.MJZJZE FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(sshy!=null&&!"".equals(sshy)){
			sb.append(" AND K.SSHY LIKE ? ");
			params.add("%" + sshy + "%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrname!=null&&!"".equals(cyrname)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? ");
			params.add("%" + cyrname.toUpperCase() + "%");
			params.add("%" + cyrname.toUpperCase() + "%");
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrname.toUpperCase() + "%");
			sb.append(")");
		}
		final List param=params;
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
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
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String owner = (String) object[1];
					String customername = (String) object[2];
					String zqdm = (String) object[3];
					String zqjc = (String) object[4];
					String zrfs = (String) object[5];
					String sshy = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("SSHY",sshy);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getCloselist(int pageSize1, int pageNumber1, String customername, String sshy, String czbm, String cyrname) {
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,K.SSHY,B.GPFXSL,B.MJZJZE,B.JSYY,B.MEMO,B.MANAGER FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='已完成'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(sshy!=null&&!"".equals(sshy)){
			sb.append(" AND K.SSHY LIKE ? ");
			params.add("%" + sshy + "%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrname!=null&&!"".equals(cyrname)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? )");
			params.add("%" + cyrname.toUpperCase() + "%");
			params.add("%" + cyrname.toUpperCase() + "%");
		}
		sb.append(" ORDER BY B.PROJECTNAME,B.CREATEDATE");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize = pageSize1;
		final int startRow = (pageNumber1 - 1) * pageSize;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
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
				query.setFirstResult(startRow);
				query.setMaxResults(pageSize);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String owner = (String) object[1];
					String customername = (String) object[2];
					String zqdm = (String) object[3];
					String zqjc = (String) object[4];
					String zrfs = (String) object[5];
					String sshy = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					String jsyy = (String) object[9];
					String memo = (String) object[10];
					String manager = (String) object[11];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("SSHY",sshy);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					map.put("JSYY",jsyy);
					map.put("MEMO",memo);
					map.put("MANAGER",manager);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getCloselistSize(String customername, String sshy, String czbm, String cyrname) {
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,K.SSHY,B.GPFXSL,B.MJZJZE,B.JSYY,B.MEMO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='已完成'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(sshy!=null&&!"".equals(sshy)){
			sb.append(" AND K.SSHY LIKE ? ");
			params.add("%" + sshy + "%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrname!=null&&!"".equals(cyrname)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? )");
			params.add("%" + cyrname.toUpperCase() + "%");
			params.add("%" + cyrname.toUpperCase() + "%");
		}
		final List param =params;
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
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
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String owner = (String) object[1];
					String customername = (String) object[2];
					String zqdm = (String) object[3];
					String zqjc = (String) object[4];
					String zrfs = (String) object[5];
					String sshy = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					String jsyy = (String) object[9];
					String memo = (String) object[10];
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("SSHY",sshy);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					map.put("MEMO",memo);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> accountedForList(int pageSize,int pageNumber,String customername,String departmentname,String dzrqbegin,String dzrqend,String xmlx,String xylx) {
		StringBuffer sb = new StringBuffer("SELECT TO_CHAR(B.DZRQ,'yyyy-MM') MONTH,B.CUSTOMERNAME,B.XYLX,B.XMLX,B.DZJE,B.DZRQ,B.DEPARTMENTNAME,O.USERNAME||'['||O.USERID||']' LRR,B.CJSJ,B.SPZT,B.LCBS,B.TASKID,B.LCBH,K.CUSTOMERNO,K.CUSTOMERNAME KCNAME FROM BD_ZQB_CWRZB B LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO LEFT JOIN ORGUSER O ON B.LRR=O.USERID WHERE 1=1");
		List params=new ArrayList();
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String userid = user.getUserid();
		Long orgroleid = user.getOrgroleid();
		Long ismanager = user.getIsmanager();
		String node1 = DBUtil.getString("SELECT SUBSTR(NODE1,0, instr(NODE1,'[',1)-1) AS NODE1 FROM BD_ZQB_SXSPRYSZ WHERE LCMC='财务入账' AND XMLX='财务入账项目'", "NODE1");
		if(orgroleid==9||orgroleid==5||(node1!=null&&!userid.equals("")&&userid.equals(node1))){
			
		}else if(orgroleid==6l||ismanager==1l){
			String deptname = user.getDepartmentname();
			sb.append(" AND (B.DEPARTMENTNAME = ? OR B.LRR= ? )");
			params.add(deptname);
			params.add(userid);
		}else if(orgroleid!=6l&&ismanager!=1l){
			if(orgroleid!=9&&orgroleid!=5&&node1!=null&&!userid.equals("")&&!userid.equals(node1)){
				sb.append(" AND B.LRR= ? ");
				params.add(userid);
			}
		}
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(departmentname!=null&&!"".equals(departmentname)){
			sb.append(" AND B.DEPARTMENTNAME LIKE ? ");
			params.add("%" + departmentname + "%");
		}
		if(dzrqbegin!=null&&!"".equals(dzrqbegin)){
			sb.append(" AND B.DZRQ > TO_DATE( ? ,'yyyy-MM-dd hh24:mi:ss')");
			params.add(dzrqbegin);
		}
		if(dzrqend!=null&&!"".equals(dzrqend)){
			sb.append(" AND B.DZRQ < TO_DATE( ? ,'yyyy-MM-dd hh24:mi:ss')");
			params.add(dzrqend);
		}
		if(xmlx!=null&&!"".equals(xmlx)){
			sb.append(" AND B.XMLX = ? ");
			params.add(xmlx);
		}
		if(xylx!=null&&!"".equals(xylx)){
			sb.append(" AND B.XYLX = ? ");
			params.add(xylx);
		}
		sb.append(" ORDER BY B.DZRQ DESC");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String MONTH = (String) object[0];
					String CUSTOMERNAME = (String) object[1];
					String XYLX = (String) object[2];
					String XMLX = (String) object[3];
					BigDecimal DZJE =(BigDecimal)object[4];
					String DZRQ = sdf.format((Date) object[5]);
					String DEPARTMENTNAME = (String) object[6];
					String LRR = (String) object[7];
					String CJSJ = sdf.format((Date) object[8]);
					String SPZT =(String)object[9];
					String LCBS =(String)object[10];
					String TASKID =(String)object[11];
					String LCBH =(String)object[12];
					String CUSTOMERNO = (String) object[13];
					String KCUSTOMERNAME = (String) object[14];
					map.put("MONTH",MONTH);
					map.put("CUSTOMERNAME",CUSTOMERNAME);
					map.put("XYLX",XYLX);
					map.put("XMLX",XMLX);
					map.put("DZJE",DZJE);
					map.put("DZRQ",DZRQ);
					map.put("DEPARTMENTNAME",DEPARTMENTNAME);
					map.put("LRR",LRR);
					map.put("CJSJ",CJSJ);
					map.put("SPZT",SPZT);
					map.put("LCBS",LCBS);
					map.put("TASKID",TASKID);
					map.put("LCBH",LCBH);
					map.put("CUSTOMERNO",CUSTOMERNO);
					map.put("KCUSTOMERNAME",KCUSTOMERNAME);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> accountedForList(String customername,String departmentname,String dzrqbegin,String dzrqend,String xmlx,String xylx) {
		StringBuffer sb = new StringBuffer("SELECT TO_CHAR(B.DZRQ,'yyyy-MM') MONTH,B.CUSTOMERNAME,B.XYLX,B.XMLX,B.DZJE,B.DZRQ,B.DEPARTMENTNAME,O.USERNAME||'['||O.USERID||']' LRR,B.CJSJ,B.SPZT,B.LCBS,B.TASKID,B.LCBH,K.CUSTOMERNO,K.CUSTOMERNAME KCNAME FROM BD_ZQB_CWRZB B LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO LEFT JOIN ORGUSER O ON B.LRR=O.USERID WHERE 1=1");
		List params=new ArrayList();
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String userid = user.getUserid();
		Long orgroleid = user.getOrgroleid();
		Long ismanager = user.getIsmanager();
		String node1 = DBUtil.getString("SELECT SUBSTR(NODE1,0, instr(NODE1,'[',1)-1) AS NODE1 FROM BD_ZQB_SXSPRYSZ WHERE LCMC='财务入账' AND XMLX='财务入账项目'", "NODE1");
		if(orgroleid==9||orgroleid==5||(node1!=null&&!userid.equals("")&&userid.equals(node1))){
			
		}else if(orgroleid==6l||ismanager==1l){
			String deptname = user.getDepartmentname();
			sb.append(" AND (B.DEPARTMENTNAME = ? OR B.LRR= ? )");
			params.add(deptname);
			params.add(userid);
		}else if(orgroleid!=6l&&ismanager!=1l){
			if(orgroleid!=9&&orgroleid!=5&&node1!=null&&!userid.equals("")&&!userid.equals(node1)){
				sb.append(" AND B.LRR= ?　");
				params.add(userid);
			}
		}
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(departmentname!=null&&!"".equals(departmentname)){
			sb.append(" AND B.DEPARTMENTNAME LIKE ? ");
			params.add("%" + departmentname + "%");
		}
		if(dzrqbegin!=null&&!"".equals(dzrqbegin)){
			sb.append(" AND B.DZRQ > TO_DATE( ? ,'yyyy-MM-dd hh24:mi:ss')");
			params.add(dzrqbegin);
		}
		if(dzrqend!=null&&!"".equals(dzrqend)){
			sb.append(" AND B.DZRQ < TO_DATE( ? ,'yyyy-MM-dd hh24:mi:ss')");
			params.add(dzrqend);
		}
		if(xmlx!=null&&!"".equals(xmlx)){
			sb.append(" AND B.XMLX = ? ");
			params.add(xmlx);
		}
		if(xylx!=null&&!"".equals(xylx)){
			sb.append(" AND B.XYLX = ? ");
			params.add(xylx);
		}
		sb.append(" ORDER BY B.DZRQ DESC");
		final List param=params;
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String MONTH = (String) object[0];
					String CUSTOMERNAME = (String) object[1];
					String XYLX = (String) object[2];
					String XMLX = (String) object[3];
					BigDecimal DZJE =(BigDecimal)object[4];
					String DZRQ = sdf.format((Date) object[5]);
					String DEPARTMENTNAME = (String) object[6];
					String LRR = (String) object[7];
					String CJSJ = sdf.format((Date) object[8]);
					String SPZT =(String)object[9];
					String LCBS =(String)object[10];
					String TASKID =(String)object[11];
					String LCBH =(String)object[12];
					String CUSTOMERNO = (String) object[13];
					String KCUSTOMERNAME = (String) object[14];
					map.put("MONTH",MONTH);
					map.put("CUSTOMERNAME",CUSTOMERNAME);
					map.put("XYLX",XYLX);
					map.put("XMLX",XMLX);
					map.put("DZJE",DZJE);
					map.put("DZRQ",DZRQ);
					map.put("DEPARTMENTNAME",DEPARTMENTNAME);
					map.put("LRR",LRR);
					map.put("CJSJ",CJSJ);
					map.put("SPZT",SPZT);
					map.put("LCBS",LCBS);
					map.put("TASKID",TASKID);
					map.put("LCBH",LCBH);
					map.put("CUSTOMERNO",CUSTOMERNO);
					map.put("KCUSTOMERNAME",KCUSTOMERNAME);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> accountedForExp() {
		StringBuffer sb = new StringBuffer("SELECT * FROM (");
		sb.append(" SELECT TO_CHAR(B.DZRQ,'yyyy-MM') MONTH,B.CUSTOMERNAME,B.XYLX,B.XMLX,B.DZJE,B.DZRQ,B.DEPARTMENTNAME,O.USERNAME||'['||O.USERID||']' LRR,B.CJSJ FROM BD_ZQB_CWRZB B LEFT JOIN ORGUSER O ON B.LRR=O.USERID");
		sb.append(" UNION ALL");
		sb.append(" SELECT DISTINCT TO_CHAR(B.DZRQ,'yyyy-MM') MONTH,'','','',SUM(DZJE) OVER(PARTITION BY TO_CHAR(B.DZRQ,'yyyy-MM')) GROUPSUM,NULL,'','',NULL FROM BD_ZQB_CWRZB B");
		sb.append(" UNION ALL");
		sb.append(" SELECT DISTINCT TO_CHAR(B.DZRQ,'yyyy') MONTH,'','','',SUM(DZJE) OVER(PARTITION BY TO_CHAR(B.DZRQ,'YYYY')) GROUPSUM,NULL,'','',NULL FROM BD_ZQB_CWRZB B");
		sb.append(" ) ORDER BY MONTH DESC,CUSTOMERNAME");
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String MONTH = (String) object[0];
					String CUSTOMERNAME = (String) object[1];
					String XYLX = (String) object[2];
					String XMLX = (String) object[3];
					BigDecimal DZJE =(BigDecimal)object[4];
					Date DZRQ = (Date) object[5];
					String DEPARTMENTNAME = (String) object[6];
					String LRR = (String) object[7];
					Date CJSJ = (Date) object[8];
					map.put("MONTH",MONTH);
					map.put("CUSTOMERNAME",CUSTOMERNAME);
					map.put("XYLX",XYLX);
					map.put("XMLX",XMLX);
					map.put("DZJE",DZJE);
					map.put("DZRQ",DZRQ);
					map.put("DEPARTMENTNAME",DEPARTMENTNAME);
					map.put("LRR",LRR);
					map.put("CJSJ",CJSJ);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> fixedReserveForExp(String month,String type,String status) {
		StringBuffer sb = new StringBuffer("SELECT REPLACE(O.COMPANYNAME,'股份有限公司','') COMPANYNAME,K.CUSTOMERNAME,K.ZRFS,K.ZQDM,K.ZQJC,K.SSHY,B.GPFXSL,B.MJZJZE,B.JSYY,B.MEMO FROM BD_ZQB_GPFXXMB B LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO LEFT JOIN ORGUSER O ON K.CUSTOMERNO=O.EXTEND1 WHERE 1=1");
		List params = new ArrayList();
		if(status.equals("run")&&type.equals("before")){
			sb.append(" AND B.STARTDATE < TO_DATE( ? ,'yyyy-MM')");
			params.add(month);
		}
		if(status.equals("run")&&type.equals("thismonth")){
			sb.append(" AND B.STATUS!='已完成'");
			sb.append(" AND (B.GPFXJZ!='股转系统报批' OR B.GPFXJZ IS NULL)");
			if(month!=null&&!month.equals("")){
				sb.append(" AND TO_CHAR(B.STARTDATE,'yyyy-MM') = ? ");
				params.add(month);
			}
		}
		if(status.equals("close")&&type.equals("thismonth")){
			sb.append(" AND (B.STATUS='已完成' OR B.GPFXJZ='股转系统报批')");
			if(month!=null&&!month.equals("")){
				sb.append(" AND TO_CHAR(B.STARTDATE,'yyyy-MM') = ? ");
				params.add(month);
			}
		}
		final List param=params;
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
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String COMPANYNAME = (String) object[0];
					String CUSTOMERNAME = (String) object[1];
					String ZRFS = (String) object[2];
					String ZQDM = (String) object[3];
					String ZQJC = (String) object[4];
					String SSHY =(String)object[5];
					BigDecimal GPFXSL = (BigDecimal) object[6];
					BigDecimal MJZJZE = (BigDecimal) object[7];
					String JSYY =(String)object[8];
					String MEMO =(String)object[9];
					map.put("COMPANYNAME",COMPANYNAME);
					map.put("CUSTOMERNAME",CUSTOMERNAME);
					map.put("ZRFS",ZRFS);
					map.put("ZQDM",ZQDM);
					map.put("ZQJC",ZQJC);
					map.put("SSHY",SSHY);
					map.put("GPFXSL",GPFXSL);
					map.put("MJZJZE",MJZJZE);
					map.put("JSYY",JSYY);
					map.put("MEMO",MEMO);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> fixedReserveForExpClosePro(String month) {
		StringBuffer sb = new StringBuffer("SELECT REPLACE(O.COMPANYNAME,'股份有限公司','') COMPANYNAME,K.CUSTOMERNAME,K.ZRFS,K.ZQDM,K.ZQJC,K.SSHY,B.GPFXSL,B.MJZJZE FROM BD_ZQB_GPFXXMB B LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO LEFT JOIN ORGUSER O ON K.CUSTOMERNO=O.EXTEND1 WHERE B.STATUS='已完成'");
		List params = new ArrayList();
		if(month!=null&&!month.equals("")){
			sb.append(" AND TO_CHAR(B.STARTDATE,'yyyy-MM') = ? ");
			params.add(month);
		}
		final List param=params;
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
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
				for (Object[] object : list) {
					map = new HashMap();
					String COMPANYNAME = (String) object[0];
					String CUSTOMERNAME = (String) object[1];
					String ZRFS = (String) object[2];
					String ZQDM = (String) object[3];
					String ZQJC = (String) object[4];
					String SSHY =(String)object[5];
					BigDecimal GPFXSL = (BigDecimal) object[6];
					BigDecimal MJZJZE = (BigDecimal) object[7];
					map.put("COMPANYNAME",COMPANYNAME);
					map.put("CUSTOMERNAME",CUSTOMERNAME);
					map.put("ZRFS",ZRFS);
					map.put("ZQDM",ZQDM);
					map.put("ZQJC",ZQJC);
					map.put("SSHY",SSHY);
					map.put("GPFXSL",GPFXSL);
					map.put("MJZJZE",MJZJZE);
					l.add(map);
				}
				return l;
			}
		});
	}

	
	public List<HashMap> issueObjectForExp() {
		StringBuffer sb = new StringBuffer("SELECT pt.* FROM (");
		List params = new ArrayList();
		sb.append(" SELECT COMPANYNAME,ZQDM,ZQJC,STR,COUNT(*) NUMS FROM (");
		//股东类型
		sb.append(" SELECT MAIN.COMPANYNAME,MAIN.ZQDM,MAIN.ZQJC,SUB.GDLX STR FROM");
		sb.append(" (SELECT REPLACE(O.COMPANYNAME,'股份有限公司','') COMPANYNAME,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO LEFT JOIN ORGUSER O ON K.CUSTOMERNO=O.EXTEND1"); 
		sb.append(" LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息')) MAIN");
		sb.append(" LEFT JOIN");
		sb.append(" (SELECT B.*,S.INSTANCEID FROM BD_ZQB_GPFXXMDZDX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定增对象')) SUB"); 
		sb.append(" ON MAIN.INSTANCEID=SUB.INSTANCEID");

		sb.append(" union all");
		//获取渠道
		sb.append(" SELECT MAIN.COMPANYNAME,MAIN.ZQDM,MAIN.ZQJC,SUB.HQQD STR FROM");
		sb.append(" (SELECT REPLACE(O.COMPANYNAME,'股份有限公司','') COMPANYNAME,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO LEFT JOIN ORGUSER O ON K.CUSTOMERNO=O.EXTEND1"); 
		sb.append(" LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息')) MAIN");
		sb.append(" LEFT JOIN");
		sb.append(" (SELECT B.*,S.INSTANCEID FROM BD_ZQB_GPFXXMDZDX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定增对象')) SUB"); 
		sb.append(" ON MAIN.INSTANCEID=SUB.INSTANCEID");

		sb.append(" ) GROUP BY COMPANYNAME,ZQDM,ZQJC,STR");
		sb.append(" ) PIVOT (SUM(NUMS) FOR STR IN('公司股东（自然人）A','董监高 B','核心员工 C','其他自然人投资者 D','自然人投资者小计 E','公司股东（机构）F','一般法人机构 G','专业机构H','机构投资者小计I','营业部客户 J','券商资管（非营业部客户）K','券商直投（非营业部客户） L','基金公司（非营业部客户） M','大股东关联方N','其他客户群体 O','其他客户群体获取渠道 P' ");
//		String[] owners = cols.split(",");
//		for (int i = 0; i < owners.length; i++) {
//			if(i==(owners.length-1)){
//				sb.append("?");
//			}else{
//				sb.append("?,");
//			}
//			params.add(owners[i].replaceAll("'", ""));
//		}
		sb.append("  )) pt");
//		final String col=cols;
		final String sql = sb.toString();
		final List param =params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
//				for (int i = 0; i < param.size(); i++) {
//					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
//						String params=param.get(i).toString().replace("%", "").trim();
//						if(d.HasInjectionData(params)){
//							return l;
//						}
//						}
//					query.setParameter(i, param.get(i));
//				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String COMPANYNAME = (String) object[0];
					String ZQDM = (String) object[1];
					String ZQJC = (String) object[2];
					BigDecimal A = (BigDecimal) object[3];
					BigDecimal B = (BigDecimal) object[4];
					BigDecimal C = (BigDecimal) object[5];
					BigDecimal D = (BigDecimal) object[6];
					BigDecimal E = (BigDecimal) object[7];
					BigDecimal F = (BigDecimal) object[8];
					BigDecimal G = (BigDecimal) object[9];
					BigDecimal H = (BigDecimal) object[10];
					BigDecimal I = (BigDecimal) object[11];
					BigDecimal J = (BigDecimal) object[12];
					BigDecimal K = (BigDecimal) object[13];
					BigDecimal L = (BigDecimal) object[14];
					BigDecimal M = (BigDecimal) object[15];
					BigDecimal N = (BigDecimal) object[16];
					BigDecimal O = (BigDecimal) object[17];
					BigDecimal P = (BigDecimal) object[18];
					map.put("COMPANYNAME",COMPANYNAME);
					map.put("ZQDM",ZQDM);
					map.put("ZQJC",ZQJC);
					map.put("A",A);
					map.put("B",B);
					map.put("C",C);
					map.put("D",D);
					map.put("E",E);
					map.put("F",F);
					map.put("G",G);
					map.put("H",H);
					map.put("I",I);
					map.put("J",J);
					map.put("K",K);
					map.put("L",L);
					map.put("M",M);
					map.put("N",N);
					map.put("O",O);
					map.put("P",P);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public HashMap<String, Object> getDataMap(String dzrq) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");
		StringBuffer sql = new StringBuffer("SELECT MQYDATA.MQYCOUNT,YQYDATA.YQYCOUNT,XMQY.QSXYRQ,QSXYRQDATE,YEARTOTAL.YXYJE,MONTHTOTAL.MXYJE,MRZDATA.MTOTAL,YRZDATA.YTOTAL FROM (SELECT QSXYRQ QSXYRQDATE,TO_CHAR(QSXYRQ,'YYYY-MM') QSXYRQ,TO_CHAR(QSXYRQ,'YYYY-MM') MONTHRQ,TO_CHAR(QSXYRQ,'YYYY') YEARRQ FROM BD_ZQB_XMQY WHERE TO_CHAR(QSXYRQ,'YYYY-MM')=?) XMQY INNER JOIN (SELECT COUNT(1) MQYCOUNT,QSXYRQ FROM (SELECT CUSTOMERNO,TO_CHAR(QSXYRQ,'YYYY-MM') QSXYRQ FROM BD_ZQB_XMQY) GROUP BY QSXYRQ,CUSTOMERNO) MQYDATA ON XMQY.QSXYRQ=MQYDATA.QSXYRQ INNER JOIN (SELECT COUNT(1) YQYCOUNT,QSXYRQ FROM (SELECT CUSTOMERNO,TO_CHAR(QSXYRQ,'YYYY') QSXYRQ FROM BD_ZQB_XMQY) GROUP BY QSXYRQ,CUSTOMERNO) YQYDATA ON XMQY.YEARRQ=YQYDATA.QSXYRQ LEFT JOIN (SELECT QSXYRQ,SUM(XYJE) AS MXYJE FROM (SELECT TO_CHAR(XMQY.QSXYRQ,'YYYY-MM') QSXYRQ,XMQY.XYJE FROM (SELECT A.* FROM BD_ZQB_XMQY A INNER JOIN (SELECT MAX(ID) ID,CUSTOMERNO,MAX(QSXYRQ) QSXYRQ FROM (SELECT ID,CUSTOMERNO,GSMC,XYLX,DECODE(XYLX,'改制融资推荐挂牌财务顾问协议',ID,'保密协议',ID,'定增财务顾问协议',ID,'并购财务顾问协议',ID,'推荐挂牌并持续督导协议书','5','推荐挂牌并持续督导协议书之补充协议','5','其他',ID) XYLXTYPE,XMLX,QSXYRQ FROM BD_ZQB_XMQY) GROUP BY XYLXTYPE,CUSTOMERNO,XMLX) B ON A.ID=B.ID AND A.CUSTOMERNO=B.CUSTOMERNO) XMQY) GROUP BY QSXYRQ) MONTHTOTAL ON XMQY.MONTHRQ=MONTHTOTAL.QSXYRQ LEFT JOIN (SELECT QSXYRQ,SUM(XYJE) AS YXYJE FROM (SELECT TO_CHAR(XMQY.QSXYRQ,'YYYY') QSXYRQ,XMQY.XYJE FROM (SELECT A.* FROM BD_ZQB_XMQY A INNER JOIN (SELECT MAX(ID) ID,CUSTOMERNO,MAX(QSXYRQ) QSXYRQ FROM (SELECT ID,CUSTOMERNO,GSMC,XYLX,DECODE(XYLX,'改制融资推荐挂牌财务顾问协议',ID,'保密协议',ID,'定增财务顾问协议',ID,'并购财务顾问协议',ID,'推荐挂牌并持续督导协议书','5','推荐挂牌并持续督导协议书之补充协议','5','其他',ID) XYLXTYPE,XMLX,QSXYRQ FROM BD_ZQB_XMQY) GROUP BY XYLXTYPE,CUSTOMERNO,XMLX) B ON A.ID=B.ID AND A.CUSTOMERNO=B.CUSTOMERNO) XMQY) GROUP BY QSXYRQ) YEARTOTAL ON XMQY.YEARRQ=YEARTOTAL.QSXYRQ LEFT JOIN (SELECT SUM(DECODE(DZJE,NULL,NULL,DZJE)) MTOTAL,DZRQ FROM (SELECT TO_CHAR(DZRQ,'YYYY-MM') DZRQ,DZJE FROM BD_ZQB_CWRZB ) GROUP BY DZRQ) MRZDATA ON XMQY.QSXYRQ=MRZDATA.DZRQ LEFT JOIN (SELECT SUM(DECODE(DZJE,NULL,NULL,DZJE)) YTOTAL,DZRQ FROM (SELECT TO_CHAR(DZRQ,'YYYY') DZRQ,DZJE FROM BD_ZQB_CWRZB ) GROUP BY DZRQ) YRZDATA ON XMQY.YEARRQ=YRZDATA.DZRQ");
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, dzrq);
			rs = ps.executeQuery();
			while (rs.next()) {
				Integer mqycount = rs.getInt("MQYCOUNT");
				Integer yqycount = rs.getInt("YQYCOUNT");
				BigDecimal mxyje = rs.getBigDecimal("MXYJE");
				BigDecimal yxyje = rs.getBigDecimal("YXYJE");
				Integer mtotal = rs.getInt("MTOTAL");
				Integer ytotal = rs.getInt("YTOTAL");
				String qsxyrq = rs.getDate("QSXYRQDATE")==null?"":df.format(rs.getDate("QSXYRQDATE"));
				dataMap.put("MQYCOUNT", mqycount);
				dataMap.put("YQYCOUNT", yqycount);
				dataMap.put("MXYJE", mxyje);
				dataMap.put("YXYJE", yxyje);
				dataMap.put("MTOTAL", mtotal);
				dataMap.put("YTOTAL", ytotal);
				dataMap.put("QSXYRQ", qsxyrq);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataMap;
	}

	public List<HashMap<String, Object>> getXmsrhzList() {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		StringBuffer sql = new StringBuffer();
				sql.append("SELECT * FROM (SELECT KH.ZQJC,KH.ZQDM,XMGP.GPRQ,XMJD.TYPE,QYBM.SSBM,PJ.FZJGMC JCXSBM,PJ.A01 JCFPBL,PJ.ZCLR,PJ.MANAGER,NVL(GPQYJE.GPQYJE,0) GPQYJE,NVL(PJ.A08,0) DDQYJE,NVL(GPQYJE.GPQYJE-XMRZ.GPSRJE,0) GPSRCE,NVL(NSRDATA.NSRJE,0) NSRJE,NVL(XMRZ.GPSRJE,0) GPSRJE,NVL(XMRZ.DDSRJE,0) DDSRJE,NVL(XMRZ.BGSRJE,0) BGSRJE,NVL(XMRZ.DZSRJE,0) DZSRJE,NVL(XMRZ.CWSRJE,0) CWSRJE,NVL(XMRZ.QTSRJE,0) QTSRJE,NVL((XMRZ.GPSRJE*PJ.A01),0) JCFPJE,NVL((NSRDATA.NSRJE-(XMRZ.GPSRJE*PJ.A01)),0) NJSR FROM ");
				
				sql.append(" (SELECT * FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO IN(SELECT CUSTOMERNO FROM BD_ZQB_CWRZB WHERE 1=1 AND SPZT='审批通过' ");
				
				OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
				String userid = user.getUserid();
				Long orgroleid = user.getOrgroleid();
				Long ismanager = user.getIsmanager();
				String node1 = DBUtil.getString("SELECT SUBSTR(NODE1,0, instr(NODE1,'[',1)-1) AS NODE1 FROM BD_ZQB_SXSPRYSZ WHERE LCMC='财务入账' AND XMLX='财务入账项目'", "NODE1");
				if(orgroleid==9||orgroleid==5||(node1!=null&&!userid.equals("")&&userid.equals(node1))){
					
				}else if(orgroleid==6l||ismanager==1l){
					String deptname = user.getDepartmentname();
					sql.append(" AND (DEPARTMENTNAME = ? OR LRR = ?)");
				}else if(orgroleid!=6l&&ismanager!=1l){
					if(orgroleid!=9&&orgroleid!=5&&node1!=null&&!userid.equals("")&&!userid.equals(node1)){
						sql.append(" AND LRR = ?");
					}
				}
				
				
				sql.append(" )) KH LEFT JOIN BD_ZQB_PJ_BASE PJ ON KH.CUSTOMERNO=PJ.CUSTOMERNO LEFT JOIN (SELECT CUSTOMERNO,SUM(DECODE(XYJE,NULL,0,XYJE)) GPQYJE FROM BD_ZQB_XMQY WHERE XMLX='挂牌项目' GROUP BY CUSTOMERNO) GPQYJE ON KH.CUSTOMERNO=GPQYJE.CUSTOMERNO");
				sql.append(" LEFT JOIN (SELECT QY.CUSTOMERNO,QY.SSBM FROM BD_ZQB_XMQY QY INNER JOIN (SELECT MAX(ID) QYID,CUSTOMERNO FROM BD_ZQB_XMQY GROUP BY CUSTOMERNO) QYTEMP ON QY.ID=QYTEMP.QYID AND QY.CUSTOMERNO=QYTEMP.CUSTOMERNO) QYBM ON KH.CUSTOMERNO=QYBM.CUSTOMERNO");
				sql.append(" LEFT JOIN (");
				
				sql.append("SELECT CUSTOMERNO,SUM(DECODE(XMLX,'挂牌项目',DZJE,NULL)) GPSRJE,SUM(DECODE(XMLX,'督导项目',DZJE,NULL)) DDSRJE,SUM(DECODE(XMLX,'并购项目',DZJE,NULL)) BGSRJE,SUM(DECODE(XMLX,'定增项目',DZJE,NULL)) DZSRJE,SUM(DECODE(XMLX,'财务顾问',DZJE,NULL)) CWSRJE,SUM(DECODE(XMLX,'其他',DZJE,NULL)) QTSRJE FROM BD_ZQB_CWRZB WHERE 1=1");
				
				sql.append(" GROUP BY XMLX,CUSTOMERNO ORDER BY CUSTOMERNO) XMRZ ON KH.CUSTOMERNO=XMRZ.CUSTOMERNO");
				sql.append(" LEFT JOIN (SELECT CUSTOMERNO,SUM(DZJE) NSRJE FROM BD_ZQB_CWRZB GROUP BY TO_CHAR(DZRQ,'YYYY'),CUSTOMERNO) NSRDATA ON KH.CUSTOMERNO=NSRDATA.CUSTOMERNO");
				sql.append(" LEFT JOIN BD_ZQB_GPDJJGD XMGP ON KH.CUSTOMERNO=XMGP.CUSTOMERNO LEFT JOIN (SELECT CUSTOMERNO,DECODE(TYPE,(SELECT ID FROM BD_ZQB_KM_INFO WHERE JDMC='挂牌登记及归档'),'挂牌',(SELECT ID FROM BD_ZQB_KM_INFO WHERE JDMC='股改'),'股改',(SELECT ID FROM BD_ZQB_KM_INFO WHERE JDMC='项目立项'),'项目立项','') TYPE FROM (");
				sql.append(" SELECT MAX(TYPE) TYPE,CUSTOMERNO FROM (SELECT (SELECT ID FROM BD_ZQB_KM_INFO WHERE JDMC='挂牌登记及归档') TYPE,KH.CUSTOMERNO FROM BD_ZQB_KH_BASE KH INNER JOIN BD_ZQB_GPDJJGD XMGP ON KH.CUSTOMERNO=XMGP.CUSTOMERNO");
				sql.append(" UNION SELECT (SELECT ID FROM BD_ZQB_KM_INFO WHERE JDMC='股改') TYPE,KH.CUSTOMERNO FROM BD_ZQB_KH_BASE KH INNER JOIN BD_ZQB_LCGG LCGG ON KH.CUSTOMERNO=LCGG.CUSTOMERNO");
				sql.append(" UNION SELECT (SELECT ID FROM BD_ZQB_KM_INFO WHERE JDMC='项目立项') TYPE,KH.CUSTOMERNO FROM BD_ZQB_KH_BASE KH INNER JOIN BD_ZQB_PJ_BASE PJ ON KH.CUSTOMERNO=PJ.CUSTOMERNO");
				sql.append(" ) GROUP BY CUSTOMERNO)) XMJD ON KH.CUSTOMERNO=XMJD.CUSTOMERNO)");
		sql.append(" WHERE (TYPE IS NOT NULL OR MANAGER IS NOT NULL OR GPQYJE>0 OR NSRJE>0) and NSRJE<>0");
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			int i = 1;
			if(orgroleid==6l||ismanager==1l){
				String deptname = user.getDepartmentname();
				ps.setString(i, deptname);i++;
				ps.setString(i, userid);i++;
			}else if(orgroleid!=6l&&ismanager!=1l){
				if(orgroleid!=9&&orgroleid!=5&&node1!=null&&!userid.equals("")&&!userid.equals(node1)){
					ps.setString(i, userid);
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				String zqjc = rs.getString("ZQJC");
				String zqdm = rs.getString("ZQDM");
				Date gprq = rs.getDate("GPRQ");
				String type = rs.getString("TYPE");
				String ssbm = rs.getString("SSBM");
				String jcxsbm = rs.getString("JCXSBM");
				BigDecimal jcfpbl = rs.getBigDecimal("JCFPBL");
				String zclr = rs.getString("ZCLR");
				String manager = rs.getString("MANAGER");
				BigDecimal gpqyje = rs.getBigDecimal("GPQYJE");
				BigDecimal ddqyje = rs.getBigDecimal("DDQYJE");
				BigDecimal gpsrce = rs.getBigDecimal("GPSRCE");
				BigDecimal nsrje = rs.getBigDecimal("NSRJE");
				BigDecimal gpsrje = rs.getBigDecimal("GPSRJE");
				BigDecimal ddsrje = rs.getBigDecimal("DDSRJE");
				BigDecimal dzsrje = rs.getBigDecimal("DZSRJE");
				BigDecimal bgsrje = rs.getBigDecimal("BGSRJE");
				BigDecimal cwsrje = rs.getBigDecimal("CWSRJE");
				BigDecimal qtsrje = rs.getBigDecimal("QTSRJE");
				BigDecimal jcfpje = rs.getBigDecimal("JCFPJE");
				BigDecimal njsr = rs.getBigDecimal("NJSR");
				result.put("ZQJC", zqjc);
				result.put("ZQDM", zqdm);
				result.put("GPRQ", gprq);
				result.put("TYPE", type);
				result.put("SSBM", ssbm);
				result.put("JCXSBM", jcxsbm);
				result.put("JCFPBL", jcfpbl);
				result.put("ZCLR", zclr);
				result.put("MANAGER", manager);
				result.put("GPQYJE", gpqyje);
				result.put("DDQYJE", ddqyje);
				result.put("GPSRCE", gpsrce);
				result.put("NSRJE", nsrje);
				result.put("GPSRJE", gpsrje);
				result.put("DDSRJE", ddsrje);
				result.put("DZSRJE", dzsrje);
				result.put("BGSRJE", bgsrje);
				result.put("CWSRJE", cwsrje);
				result.put("QTSRJE", qtsrje);
				result.put("JCFPJE", jcfpje);
				result.put("NJSR", njsr);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
}
