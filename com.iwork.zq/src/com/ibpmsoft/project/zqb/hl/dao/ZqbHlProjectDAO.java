package com.ibpmsoft.project.zqb.hl.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.commons.FileType;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.sdk.FileUploadAPI;

public class ZqbHlProjectDAO extends HibernateDaoSupport {
	
	public List<HashMap<String, Object>> getYbcwTaskList(String xmbh) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();//,B.INSTANCEID,,J.INSTANCEID LCBH,J.LCBS,J.LCBS,J.ATTACH,J.TASKID,J.HTJE,J.SPZT,J.CREATEUSER,J.YJZXYNJLR
		sql.append("SELECT TYXM.ID,TYXM.JDMC,J.* FROM (SELECT SORTID,ID,JDMC,STATE FROM BD_ZQB_TYXM_INFO WHERE XMLX='一般性财务顾问项目') TYXM");
		sql.append(" LEFT JOIN (SELECT B.HTJE,B.LCBH,B.LCBS,B.TASKID,B.ATTACH,B.SPZT,B.YJZXYNJLR,O.USERNAME AS CREATEUSER,D.YJINS FROM BD_ZQB_ZBSP B LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148) D ON B.LCBH=D.GGINS LEFT JOIN ORGUSER O ON B.CREATEUSERID=O.USERID WHERE B.PROJECTNO=?) J  ON TYXM.ID=J.YJZXYNJLR WHERE TYXM.STATE=1");//
		sql.append(" ORDER BY TYXM.SORTID");
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jdmc = rs.getString("JDMC");
				String lcbh = rs.getString("LCBH");
				String lcbs = rs.getString("LCBS");
				String taskid = rs.getString("TASKID");
				String spzt = rs.getString("SPZT");
				String createuser = rs.getString("CREATEUSER");
				String instanceid = rs.getString("HTJE");
				String yjins = rs.getString("YJINS");
				StringBuffer fjstr = new StringBuffer();
				String fj = rs.getString("ATTACH");
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
				String lzjd="";
				if( taskid!=null){
					lzjd=DBUTilNew.getDataStr("task_def_key_", "select s.task_def_key_ from Process_Hi_Taskinst s WHERE s.ID_='"+taskid+"' order by id_ desc ", null);
				}
				result.put("LZJD", lzjd);
				result.put("ID", id);
				result.put("JDMC", jdmc);
				result.put("XMBH", xmbh);
				result.put("LCBH", lcbh);
				result.put("LCBS", lcbs);
				result.put("TASKID", taskid);
				result.put("SPZT", spzt);
				result.put("CREATEUSER", createuser);
				result.put("INSTANCEID", instanceid);
				result.put("YJINS", yjins);
				result.put("FJSTR", fjstr);
				dataList.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap> getDzwthRunlist(String customername,String fxzt , String clbm,String czbm,String cyrxm,int runPageNumber,int runPageSize) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM,B.GPFXSL,B.MJZJZE,B.MANAGER,B.fxzt,B.CLBM FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.CREATEUSER LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR B.CREATEUSERID = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		if( fxzt!=null&&!"".equals(fxzt)){
			sb.append(" AND B.fxzt = ? ");
			params.add(fxzt);
		}
		
		sb.append(" ORDER BY B.PROJECTNAME,B.CREATEDATE DESC");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = runPageSize;
		final int startRow1 = (runPageNumber - 1) * runPageSize;
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
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
					String czbm = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					String manager = (String) object[9];
					String fxzt = (String) object[10];
					String clbm = (String) object[11];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					map.put("MANAGER",manager);
					map.put("FXZT",fxzt);
					map.put("CLBM",clbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getYbcwRunlist(String customername,String clbm,String czbm,String cyrxm,int runPageNumber,int runPageSize) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.CUSTOMERNAME,B.CLBM,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='1'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.TBSJ DESC,B.CUSTOMERNAME");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = runPageSize;
		final int startRow1 = (runPageNumber - 1) * runPageSize;
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String customername = (String) object[1];
					String clbm = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",customername);
					map.put("CLBM",clbm);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getDzwothRunlist(String customername,String fxzt,String clbm,String czbm,String cyrxm,int runPageNumber,int runPageSize) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM,B.GPFXSL,B.MJZJZE,B.MANAGER,B.FXZT,B.CLBM FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='执行中'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.CREATEUSER LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='执行中'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR B.CREATEUSERID = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='执行中'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		if(fxzt!=null&&!"".equals(fxzt)){
			sb.append(" AND fxzt LIKE ? ");
			params.add("%"+fxzt.trim()+"%");
		}
		sb.append(" ORDER BY B.CREATEDATE DESC,B.PROJECTNAME");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = runPageSize;
		final int startRow1 = (runPageNumber - 1) * runPageSize;
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
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
					String czbm = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					String manager = (String) object[9];
					String fxzt = (String) object[10];
					String clbm = (String) object[11];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					map.put("MANAGER",manager);
					map.put("FXZT",fxzt);
					map.put("CLBM",clbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getDzwthRunlistSize(String customername,String fxzt,String clbm,String czbm,String cyrxm) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM,B.GPFXSL,B.MJZJZE,B.MANAGER FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.CREATEUSER LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR B.CREATEUSERID = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='执行中'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		if(fxzt!=null&&!"".equals(fxzt)){
			sb.append(" AND fxzt LIKE ? ");
			params.add("%"+fxzt.trim()+"%");
		}
		sb.append(" ORDER BY B.PROJECTNAME,B.CREATEDATE DESC");
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
					BigDecimal instanceid=(BigDecimal)object[0];
					String owner = (String) object[1];
					String customername = (String) object[2];
					String zqdm = (String) object[3];
					String zqjc = (String) object[4];
					String zrfs = (String) object[5];
					String czbm = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					String manager = (String) object[9];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					map.put("MANAGER",manager);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getDzwothRunlistSize(String customername,String fxzt,String clbm,String czbm,String cyrxm) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM,B.GPFXSL,B.MJZJZE,B.MANAGER FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='执行中'");
		List params=new ArrayList();
		
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.CREATEUSER LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='执行中'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR B.CREATEUSERID = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='执行中'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}

		if(fxzt!=null&&!"".equals(fxzt)){
			sb.append(" AND fxzt LIKE ? ");
			params.add("%"+fxzt.trim()+"%");
		}
		sb.append(" ORDER BY B.PROJECTNAME,B.CREATEDATE DESC");
		
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
					BigDecimal instanceid=(BigDecimal)object[0];
					String owner = (String) object[1];
					String customername = (String) object[2];
					String zqdm = (String) object[3];
					String zqjc = (String) object[4];
					String zrfs = (String) object[5];
					String czbm = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					String manager = (String) object[9];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					map.put("MANAGER",manager);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getYbcwRunlistSize(String customername,String clbm,String czbm,String cyrxm) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.CUSTOMERNAME,B.CLBM,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='1'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.CUSTOMERNAME,B.TBSJ");
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
					BigDecimal instanceid=(BigDecimal)object[0];
					String customername = (String) object[1];
					String clbm = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",customername);
					map.put("CLBM",clbm);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getDzwthCloselist(String customername,String clbm,String czbm,String cyrxm,int closePageNumber,int closePageSize) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM,B.GPFXSL,B.MJZJZE,B.MANAGER FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='已完成'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.CREATEUSER LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='已完成'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR B.CREATEUSERID = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='已完成'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.PROJECTNAME,B.CREATEDATE DESC");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = closePageSize;
		final int startRow1 = (closePageNumber - 1) * closePageSize;
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
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
					String czbm = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					String manager = (String) object[9];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					map.put("MANAGER",manager);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getDzwothCloselist(String customername,String clbm,String czbm,String cyrxm,int closePageNumber,int closePageSize) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM,B.GPFXSL,B.MJZJZE,B.MANAGER FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='已完成'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.CREATEUSER LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='已完成'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR B.CREATEUSERID = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='已完成'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.PROJECTNAME,B.CREATEDATE DESC");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = closePageSize;
		final int startRow1 = (closePageNumber - 1) * closePageSize;
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
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
					String czbm = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					String manager = (String) object[9];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					map.put("MANAGER",manager);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getYbcwCloselist(String customername,String clbm,String czbm,String cyrxm,int closePageNumber,int closePageSize) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.CUSTOMERNAME,B.CLBM,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='0'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.TBSJ DESC,B.CUSTOMERNAME");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = closePageSize;
		final int startRow1 = (closePageNumber - 1) * closePageSize;
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String customername = (String) object[1];
					String clbm = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",customername);
					map.put("CLBM",clbm);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getDzwthCloselistSize(String customername,String clbm,String czbm,String cyrxm) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM,B.GPFXSL,B.MJZJZE,B.MANAGER FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='已完成'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.CREATEUSER LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='已完成'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR B.CREATEUSERID = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.STATUS='已完成'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.PROJECTNAME,B.CREATEDATE");
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
					BigDecimal instanceid=(BigDecimal)object[0];
					String owner = (String) object[1];
					String customername = (String) object[2];
					String zqdm = (String) object[3];
					String zqjc = (String) object[4];
					String zrfs = (String) object[5];
					String czbm = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					String manager = (String) object[9];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					map.put("MANAGER",manager);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getDzwothCloselistSize(String customername,String clbm,String czbm,String cyrxm) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.OWNER,K.CUSTOMERNAME,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM,B.GPFXSL,B.MJZJZE,B.MANAGER FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='已完成'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND K.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.CREATEUSER LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='已完成'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR B.CREATEUSERID = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.PROJECTNO IN (");
			sb.append(" SELECT A.PROJECTNO FROM (");
			sb.append(" SELECT S.INSTANCEID,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息表单') AND B.STATUS='已完成'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.PROJECTNAME,B.CREATEDATE");
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
					BigDecimal instanceid=(BigDecimal)object[0];
					String owner = (String) object[1];
					String customername = (String) object[2];
					String zqdm = (String) object[3];
					String zqjc = (String) object[4];
					String zrfs = (String) object[5];
					String czbm = (String) object[6];
					BigDecimal gpfxsl = (BigDecimal) object[7];
					BigDecimal mjzjze = (BigDecimal) object[8];
					String manager = (String) object[9];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("OWNER",owner);
					map.put("CUSTOMERNAME",customername);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					map.put("GPFXSL",gpfxsl==null?"0":gpfxsl.toString());
					map.put("MJZJZE",mjzjze==null?"0":mjzjze.toString());
					map.put("MANAGER",manager);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getYbcwCloselistSize(String customername,String clbm,String czbm,String cyrxm) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();//
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.CLBM,B.CUSTOMERNAME,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='0'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.CUSTOMERNAME LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.CLBM LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_CWGWXMXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='一般财务顾问项目') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.CUSTOMERNAME,B.TBSJ DESC");
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
					String customername = (String) object[1];
					String clbm = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",customername);
					map.put("CLBM",clbm);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap<String, Object>> getDzWothTaskList(String xmbh) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TYXM.ID,TYXM.JDMC,JDDATA.* FROM (SELECT * FROM BD_ZQB_TYXM_INFO WHERE XMLX='定增项目（200人以上）') TYXM");
		sql.append(" LEFT JOIN (SELECT B.*,D.YJINS FROM BD_ZQB_GPFXXMNFXXXLCB B LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148) D ON B.LCBH=D.GGINS WHERE B.PROJECTNO=?) JDDATA ON TYXM.ID=JDDATA.GROUPID WHERE TYXM.STATE=1");
		sql.append(" ORDER BY TYXM.SORTID");
		
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jdmc = rs.getString("JDMC");
				String lcbh = rs.getString("LCBH");
				String lcbs = rs.getString("LCBS");
				String rwid = rs.getString("RWID");
				String spzt = rs.getString("SPZT");
				String createuser = rs.getString("CREATEUSER");
				String yjins = rs.getString("YJINS");
				StringBuffer fjstr = new StringBuffer();
				String fj = rs.getString("NFXFILE");
				if(fj!=null&&!fj.equals("")){
					List<FileUpload> files = FileUploadAPI.getInstance().getFileUploads(fj);
					for (int i = 0; i < files.size(); i++) {
						if(i==(files.size()-1)){
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName().length()>30?(files.get(i).getFileSrcName().substring(0, 30)+"..."):files.get(i).getFileSrcName()).append("</a>");
						}else{
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName().length()>30?(files.get(i).getFileSrcName().substring(0, 30)+"..."):files.get(i).getFileSrcName()).append("</a>").append("<br>");
						}
					}
				}
				String lzjd="";
				if( rwid!=null){
					lzjd=DBUTilNew.getDataStr("task_def_key_", "select s.task_def_key_ from Process_Hi_Taskinst s WHERE s.ID_='"+rwid+"' order by id_ desc ", null);
				}
				result.put("LZJD", lzjd);
				result.put("ID", id);
				result.put("JDMC", jdmc);
				result.put("LCBH", lcbh);
				result.put("LCBS", lcbs);
				result.put("TASKID", rwid);
				result.put("SPZT", spzt);
				result.put("CREATEUSER", createuser);
				result.put("YJINS", yjins);
				result.put("FJSTR", fjstr);
				dataList.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap<String, Object>> getDzWthTaskList(String xmbh) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TYXM.ID,TYXM.JDMC,JDDATA.* FROM (SELECT * FROM BD_ZQB_TYXM_INFO WHERE XMLX='定增项目（200人以内）') TYXM");
		sql.append(" LEFT JOIN (SELECT B.*,D.YJINS FROM BD_ZQB_GPFXXMNFXLCB B LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148) D ON B.LCBH=D.GGINS WHERE B.PROJECTNO=?) JDDATA ON TYXM.ID=JDDATA.GROUPID WHERE TYXM.STATE=1");
		sql.append(" ORDER BY TYXM.SORTID");
		
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jdmc = rs.getString("JDMC");
				String lcbh = rs.getString("LCBH");
				String lcbs = rs.getString("LCBS");
				String lzjd = rs.getString("LZJD");
				String rwid = rs.getString("RWID");
				String spzt = rs.getString("SPZT");
				String createuser = rs.getString("CREATEUSER");
				String yjins = rs.getString("YJINS");
				StringBuffer fjstr = new StringBuffer();
				String fj = rs.getString("NFXFILE");
				if(fj!=null&&!fj.equals("")){
					List<FileUpload> files = FileUploadAPI.getInstance().getFileUploads(fj);
					for (int i = 0; i < files.size(); i++) {
						if(i==(files.size()-1)){
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName().length()>30?(files.get(i).getFileSrcName().substring(0, 30)+"..."):files.get(i).getFileSrcName()).append("</a>");
						}else{
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName().length()>30?(files.get(i).getFileSrcName().substring(0, 30)+"..."):files.get(i).getFileSrcName()).append("</a>").append("<br>");
						}
					}
				}
				result.put("ID", id);
				result.put("JDMC", jdmc);
				result.put("LCBH", lcbh);
				result.put("LCBS", lcbs);
				result.put("TASKID", rwid);
				result.put("SPZT", spzt);
				result.put("CREATEUSER", createuser);
				result.put("FJSTR", fjstr);
				result.put("YJINS", yjins);
				result.put("LZJD", lzjd==null?"":lzjd);
				dataList.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public List<HashMap> getSgRunlist(String customername,String sgfs,String khlxdh,String czbm,String cyrxm,int runPageNumber,int runPageSize) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.JYF,B.JYDSF,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM,B.SGFS,B.khlxdh FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='1'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.JYF LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(khlxdh!=null&&!"".equals(khlxdh)){
			sb.append(" AND B.khlxdh LIKE ? ");
			params.add("%"+khlxdh+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		if(sgfs!=null&&!"".equals(sgfs)){
			sb.append(" AND B.sgfs LIKE ? ");
			params.add("%"+sgfs+"%");
		}
		sb.append(" ORDER BY B.TBSJ DESC,B.JYF");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = runPageSize;
		final int startRow1 = (runPageNumber - 1) * runPageSize;
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String jyf = (String) object[1];
					String jydsf = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					String sgfs = (String) object[9];
					String khlxdh = (String) object[10];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",jyf);
					map.put("JYDSF",jydsf);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					map.put("SGFS",sgfs);
					map.put("KHLXDH",khlxdh);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getBgRunlist(String customername,String jydsf,String clbm,String czbm,String cyrxm,int runPageNumber,int runPageSize) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.JYF,B.JYDSF,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT=1");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.JYF LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.JYDSF LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		if(jydsf!=null&&!"".equals(jydsf)){
			sb.append(" AND jydsf like ? ");
			params.add("%"+jydsf+"%");
		}
		sb.append(" ORDER BY B.TBSJ DESC,B.JYF");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = runPageSize;
		final int startRow1 = (runPageNumber - 1) * runPageSize;
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String jyf = (String) object[1];
					String jydsf = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",jyf);
					map.put("JYDSF",jydsf);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getSgRunlistSize(String customername,String sgfs,String khlxdh,String czbm,String cyrxm) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.JYF,B.JYDSF,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='1'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.JYF LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(khlxdh!=null&&!"".equals(khlxdh)){
			sb.append(" AND B.khlxdh LIKE ? ");
			params.add("%"+khlxdh+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		if(sgfs!=null&&!"".equals(sgfs)){
			sb.append(" AND B.sgfs LIKE ? ");
			params.add("%"+sgfs+"%");
		}
		sb.append(" ORDER BY B.JYF,B.TBSJ");
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
					BigDecimal instanceid=(BigDecimal)object[0];
					String jyf = (String) object[1];
					String jydsf = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",jyf);
					map.put("JYDSF",jydsf);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getBgRunlistSize(String customername,String jydsf,String clbm,String czbm,String cyrxm) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.JYF,B.JYDSF,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT='1'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.JYF LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.JYDSF LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT='1'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		if(jydsf!=null&&!"".equals(jydsf)){
			sb.append(" AND jydsf like ? ");
			params.add("%"+jydsf+"%");
		}
		sb.append(" ORDER BY B.JYF,B.TBSJ");
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
					BigDecimal instanceid=(BigDecimal)object[0];
					String jyf = (String) object[1];
					String jydsf = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",jyf);
					map.put("JYDSF",jydsf);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getSgCloselist(String customername,String clbm,String czbm,String cyrxm,int closePageNumber,int closePageSize) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.JYF,B.JYDSF,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='0'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.JYF LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.JYDSF LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.TBSJ DESC,B.JYF");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = closePageSize;
		final int startRow1 = (closePageNumber - 1) * closePageSize;
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String jyf = (String) object[1];
					String jydsf = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",jyf);
					map.put("JYDSF",jydsf);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getBgCloselist(String customername,String clbm,String czbm,String cyrxm,int closePageNumber,int closePageSize) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.JYF,B.JYDSF,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT='0'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.JYF LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.JYDSF LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.TBSJ DESC,B.JYF");
		final List param=params;
		final String sql = sb.toString();
		final int pageSize1 = closePageSize;
		final int startRow1 = (closePageNumber - 1) * closePageSize;
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid=(BigDecimal)object[0];
					String jyf = (String) object[1];
					String jydsf = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",jyf);
					map.put("JYDSF",jydsf);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getSgCloselistSize(String customername,String clbm,String czbm,String cyrxm) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.JYF,B.JYDSF,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='0'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.JYF LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.JYDSF LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='收购项目管理') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.JYF,B.TBSJ");
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
					String jyf = (String) object[1];
					String jydsf = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",jyf);
					map.put("JYDSF",jydsf);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getBgCloselistSize(String customername,String clbm,String czbm,String cyrxm) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgRoleId = user.getOrgroleid();
		String cuid = user.getUserid();
		StringBuffer sb = new StringBuffer("SELECT S.INSTANCEID,B.JYF,B.JYDSF,B.OWNER,B.MANAGER,K.ZQDM,K.ZQJC,K.ZRFS,B.CZBM FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT='0'");
		List params=new ArrayList();
		if(customername!=null&&!"".equals(customername)){
			sb.append(" AND B.JYF LIKE ? ");
			params.add("%" + customername + "%");
		}
		if(clbm!=null&&!"".equals(clbm)){
			sb.append(" AND B.JYDSF LIKE ? ");
			params.add("%"+clbm+"%");
		}
		if(czbm!=null&&!"".equals(czbm)){
			sb.append(" AND B.CZBM LIKE ? ");
			params.add("%" + czbm + "%");
		}
		if(cyrxm!=null&&!"".equals(cyrxm)){
			sb.append(" AND (B.OWNER LIKE ? OR B.MANAGER LIKE ? OR B.TBRID LIKE ? ");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE NAME LIKE ? ");
			sb.append(" )");
			params.add("%" + cyrxm.toUpperCase() + "%");
			sb.append(")");
		}
		if(orgRoleId!=5l){
			sb.append(" AND (SUBSTR(B.OWNER,0,INSTR(B.OWNER,'[',1)-1) = ? OR SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) = ? OR SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1)+1,INSTR(B.TBRID,']',1)-INSTR(B.TBRID,'[',1)-1) = ? ");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			sb.append(" OR B.XMBH IN (");
			sb.append(" SELECT A.XMBH FROM (");
			sb.append(" SELECT S.INSTANCEID,B.XMBH FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='重组项目管理') AND B.XMZT='0'");
			sb.append(" ) A LEFT JOIN (");
			sb.append(" SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sb.append(" ) B ON A.INSTANCEID=B.INSTANCEID WHERE USERID = ? ");
			sb.append(" )");
			params.add(cuid);
			sb.append(")");
		}
		sb.append(" ORDER BY B.JYF,B.TBSJ");
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
					BigDecimal instanceid=(BigDecimal)object[0];
					String jyf = (String) object[1];
					String jydsf = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					String zqdm = (String) object[5];
					String zqjc = (String) object[6];
					String zrfs = (String) object[7];
					String czbm = (String) object[8];
					map.put("INSTANCEID",instanceid==null?"0":instanceid.toString());
					map.put("CUSTOMERNAME",jyf);
					map.put("JYDSF",jydsf);
					map.put("OWNER",owner);
					map.put("MANAGER",manager);
					map.put("ZQDM",zqdm);
					map.put("ZQJC",zqjc);
					map.put("ZRFS",zrfs);
					map.put("CZBM",czbm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap<String, Object>> getSgTaskList(String xmbh) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TYXM.ID,TYXM.JDMC,JDDATA.* FROM (SELECT * FROM BD_ZQB_TYXM_INFO WHERE XMLX='收购项目管理') TYXM");
		sql.append(" LEFT JOIN (SELECT B.*,O.USERNAME AS CREATEUSER,D.YJINS FROM BD_ZQB_BGXMLX B LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148) D ON B.LCBH=D.GGINS LEFT JOIN ORGUSER O ON B.TBRID=O.USERID WHERE B.XMBH=?) JDDATA ON TYXM.ID=JDDATA.JDBH WHERE TYXM.STATE=1");
		sql.append(" ORDER BY TYXM.SORTID");
		
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jdmc = rs.getString("JDMC");
				String lcbh = rs.getString("LCBH");
				String lcbs = rs.getString("LCBS");
				String taskid = rs.getString("TASKID");
				String spzt = rs.getString("SPZT");
				String createuser = rs.getString("CREATEUSER");
				String instanceid = rs.getString("INSTANCEID");
				String yjins = rs.getString("YJINS");
				StringBuffer fjstr = new StringBuffer();
				String fj = rs.getString("FJ");
				if(fj!=null&&!fj.equals("")){
					List<FileUpload> files = FileUploadAPI.getInstance().getFileUploads(fj);
					for (int i = 0; i < files.size(); i++) {
						if(i==(files.size()-1)){
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName().length()>30?(files.get(i).getFileSrcName().substring(0, 30)+"..."):files.get(i).getFileSrcName()).append("</a>");
						}else{
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName().length()>30?(files.get(i).getFileSrcName().substring(0, 30)+"..."):files.get(i).getFileSrcName()).append("</a>").append("<br>");
						}
					}
				}
				String lzjd="";
				if( taskid!=null){
					lzjd=DBUTilNew.getDataStr("task_def_key_", "select s.task_def_key_ from Process_Hi_Taskinst s WHERE s.ID_='"+taskid+"' order by id_ desc ", null);
				}
				result.put("LZJD", lzjd);
				result.put("ID", id);
				result.put("JDMC", jdmc);
				result.put("LCBH", lcbh);
				result.put("LCBS", lcbs);
				result.put("TASKID", taskid);
				result.put("SPZT", spzt);
				result.put("CREATEUSER", createuser);
				result.put("INSTANCEID", instanceid);
				result.put("YJINS", yjins);
				result.put("FJSTR", fjstr);
				dataList.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap<String, Object>> getBgTaskList(String xmbh) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TYXM.ID,TYXM.JDMC,JDDATA.* FROM (SELECT * FROM BD_ZQB_TYXM_INFO WHERE XMLX='并购项目') TYXM");
		sql.append(" LEFT JOIN (SELECT B.*,O.USERNAME AS CREATEUSER,D.YJINS FROM BD_ZQB_BGXMLX B LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148) D ON B.LCBH=D.GGINS LEFT JOIN ORGUSER O ON B.TBRID=O.USERID WHERE B.XMBH=?) JDDATA ON TYXM.ID=JDDATA.JDBH WHERE TYXM.STATE=1");
		sql.append(" ORDER BY TYXM.SORTID");
		
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jdmc = rs.getString("JDMC");
				String lcbh = rs.getString("LCBH");
				String lcbs = rs.getString("LCBS");
				String taskid = rs.getString("TASKID");
				String spzt = rs.getString("SPZT");
				String createuser = rs.getString("CREATEUSER");
				String instanceid = rs.getString("INSTANCEID");
				String yjins = rs.getString("YJINS");
				StringBuffer fjstr = new StringBuffer();
				String fj = rs.getString("FJ");
				if(fj!=null&&!fj.equals("")){
					List<FileUpload> files = FileUploadAPI.getInstance().getFileUploads(fj);
					for (int i = 0; i < files.size(); i++) {
						if(i==(files.size()-1)){
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName().length()>30?(files.get(i).getFileSrcName().substring(0, 30)+"..."):files.get(i).getFileSrcName()).append("</a>");
						}else{
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName().length()>30?(files.get(i).getFileSrcName().substring(0, 30)+"..."):files.get(i).getFileSrcName()).append("</a>").append("<br>");
						}
					}
				}
				String lzjd="";
				if( taskid!=null){
					lzjd=DBUTilNew.getDataStr("task_def_key_", "select s.task_def_key_ from Process_Hi_Taskinst s WHERE s.ID_='"+taskid+"' order by id_ desc ", null);
				}
				result.put("LZJD", lzjd);
				result.put("ID", id);
				result.put("JDMC", jdmc);
				result.put("LCBH", lcbh);
				result.put("LCBS", lcbs);
				result.put("TASKID", taskid);
				result.put("SPZT", spzt);
				result.put("CREATEUSER", createuser);
				result.put("INSTANCEID", instanceid);
				result.put("YJINS", yjins);
				result.put("FJSTR", fjstr);
				dataList.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap> getRunList(String customername,String xmjd,String cyrxm,int runPageNumber,int runPageSize){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long orgroleid = user.getOrgroleid();
		String cuid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT S.INSTANCEID,B.PROJECTNAME,B.STARTDATE,B.MANAGER,B.OWNER,B.FZJGMC,B.SSSYB,B.A08,B.KHLXR,B.KHLXDH,B.HTJE,B.A01,B.XMBZ,B.XMJD");
		sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
		sb.append(" AND B.STATUS='执行中'");
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND B.PROJECTNAME LIKE ?");
			params.add("%"+customername+"%");
		}
//		if(clbm!=null&&!clbm.equals("")){
//			sb.append(" AND B.FZJGMC LIKE ?");
//			params.add("%"+clbm+"%");
//		}
//		if(czbm!=null&&!czbm.equals("")){
//			sb.append(" AND B.GSGK LIKE ?");
//			params.add("%"+czbm+"%");
//		}
		if(cyrxm!=null&&!cyrxm.equals("")){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,instr(B.MANAGER,'[',1)+1, instr(B.MANAGER,']',1)-(instr(B.MANAGER,'[',1)+1)) AS MANAGER,");
			sb.append("SUBSTR(B.DDAP,instr(B.DDAP,'[',1)+1, instr(B.DDAP,']',1)-(instr(B.DDAP,'[',1)+1)) AS DDAP,");
			sb.append("SUBSTR(B.XMZH,instr(B.XMZH,'[',1)+1, instr(B.XMZH,']',1)-(instr(B.XMZH,'[',1)+1)) AS XMZH,");
			sb.append("SUBSTR(B.XMXY,instr(B.XMXY,'[',1)+1, instr(B.XMXY,']',1)-(instr(B.XMXY,'[',1)+1)) AS XMXY,");
			sb.append("SUBSTR(B.XMLS,instr(B.XMLS,'[',1)+1, instr(B.XMLS,']',1)-(instr(B.XMLS,'[',1)+1)) AS XMLS,");
			sb.append("SUBSTR(B.OWNER,instr(B.OWNER,'[',1)+1, instr(B.OWNER,']',1)-(instr(B.OWNER,'[',1)+1)) AS OWNER"); 
			sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.MANAGER LIKE ? OR A.DDAP LIKE ? OR A.XMZH LIKE ? OR A.XMXY LIKE ? OR A.XMLS LIKE ? OR A.OWNER LIKE ? OR B.NAME LIKE ?)");
			sb.append(")");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
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
			sb.append("SUBSTR(B.DDAP,0, INSTR(B.DDAP,'[',1)-1) AS DDAP,");
			sb.append("SUBSTR(B.XMZH,0, INSTR(B.XMZH,'[',1)-1) AS XMZH,");
			sb.append("SUBSTR(B.XMXY,0, INSTR(B.XMXY,'[',1)-1) AS XMXY,");
			sb.append("SUBSTR(B.XMLS,0, INSTR(B.XMLS,'[',1)-1) AS XMLS,");
			sb.append("SUBSTR(B.OWNER,0, INSTR(B.OWNER,'[',1)-1) AS OWNER,");
			sb.append("B.CREATEUSERID AS CUID");
			sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.MANAGER = ? OR A.DDAP = ? OR A.XMZH = ? OR A.XMXY = ? OR A.XMLS = ? OR A.OWNER = ? OR A.CUID = ? OR B.USERID = ?)");
			sb.append(")");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
		}
		if( xmjd!=null&&!xmjd.equals("")){
			sb.append(" AND B.xmjd LIKE ?");
			params.add("%" +xmjd.trim() +"%");
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
				query.setMaxResults(pageSize1);
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
					String PROJECTNAME =(String)object[1];
					Date STARTDATE =(Date)object[2];
					String MANAGER =(String)object[3];
					String OWNER =(String)object[4];
					String FZJGMC =(String)object[5];
					String SSSYB =(String)object[6];
					BigDecimal A08 =(BigDecimal)object[7];
					String KHLXR =(String)object[8];
					String KHLXDH =(String)object[9];
					BigDecimal HTJE =(BigDecimal)object[10];
					BigDecimal A01 =(BigDecimal)object[11];
					String XMBZ =(String)object[12];
					String XMJD =(String)object[13];
					map.put("INSTANCEID", INSTANCEID==null?"0":INSTANCEID.toString());
					map.put("PROJECTNAME", PROJECTNAME);
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
					map.put("XMJD", XMJD);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getRunListSize(String customername,String xmjd,String cyrxm){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long orgroleid = user.getOrgroleid();
		String cuid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT S.INSTANCEID,B.PROJECTNAME,B.STARTDATE,B.MANAGER,B.OWNER,B.FZJGMC,B.SSSYB,B.A08,B.KHLXR,B.KHLXDH,B.HTJE,B.A01,B.XMBZ");
		sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
		sb.append(" AND B.STATUS='执行中'");
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND B.PROJECTNAME LIKE ?");
			params.add("%"+customername+"%");
		}
//		if(clbm!=null&&!clbm.equals("")){
//			sb.append(" AND B.FZJGMC LIKE ?");
//			params.add("%"+clbm+"%");
//		}
//		if(czbm!=null&&!czbm.equals("")){
//			sb.append(" AND B.GSGK LIKE ?");
//			params.add("%"+czbm+"%");
//		}
		if(cyrxm!=null&&!cyrxm.equals("")){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,instr(B.MANAGER,'[',1)+1, instr(B.MANAGER,']',1)-(instr(B.MANAGER,'[',1)+1)) AS MANAGER,");
			sb.append("SUBSTR(B.DDAP,instr(B.DDAP,'[',1)+1, instr(B.DDAP,']',1)-(instr(B.DDAP,'[',1)+1)) AS DDAP,");
			sb.append("SUBSTR(B.XMZH,instr(B.XMZH,'[',1)+1, instr(B.XMZH,']',1)-(instr(B.XMZH,'[',1)+1)) AS XMZH,");
			sb.append("SUBSTR(B.XMXY,instr(B.XMXY,'[',1)+1, instr(B.XMXY,']',1)-(instr(B.XMXY,'[',1)+1)) AS XMXY,");
			sb.append("SUBSTR(B.XMLS,instr(B.XMLS,'[',1)+1, instr(B.XMLS,']',1)-(instr(B.XMLS,'[',1)+1)) AS XMLS,");
			sb.append("SUBSTR(B.OWNER,instr(B.OWNER,'[',1)+1, instr(B.OWNER,']',1)-(instr(B.OWNER,'[',1)+1)) AS OWNER"); 
			sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.MANAGER LIKE ? OR A.DDAP LIKE ? OR A.XMZH LIKE ? OR A.XMXY LIKE ? OR A.XMLS LIKE ? OR A.OWNER LIKE ? OR B.NAME LIKE ?)");
			sb.append(")");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
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
			sb.append("SUBSTR(B.DDAP,0, INSTR(B.DDAP,'[',1)-1) AS DDAP,");
			sb.append("SUBSTR(B.XMZH,0, INSTR(B.XMZH,'[',1)-1) AS XMZH,");
			sb.append("SUBSTR(B.XMXY,0, INSTR(B.XMXY,'[',1)-1) AS XMXY,");
			sb.append("SUBSTR(B.XMLS,0, INSTR(B.XMLS,'[',1)-1) AS XMLS,");
			sb.append("SUBSTR(B.OWNER,0, INSTR(B.OWNER,'[',1)-1) AS OWNER,");
			sb.append("B.CREATEUSERID AS CUID");
			sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.MANAGER = ? OR A.DDAP = ? OR A.XMZH = ? OR A.XMXY = ? OR A.XMLS = ? OR A.OWNER = ? OR A.CUID = ? OR B.USERID = ?)");
			sb.append(")");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
		}
		if( xmjd!=null&&!xmjd.equals("")){
			sb.append(" AND B.xmjd LIKE ?");
			params.add("%"+xmjd.trim()+"%");
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
					String PROJECTNAME =(String)object[1];
					Date STARTDATE =(Date)object[2];
					String MANAGER =(String)object[3];
					String OWNER =(String)object[4];
					String FZJGMC =(String)object[5];
					String SSSYB =(String)object[6];
					BigDecimal A08 =(BigDecimal)object[7];
					String KHLXR =(String)object[8];
					String KHLXDH =(String)object[9];
					BigDecimal HTJE =(BigDecimal)object[10];
					BigDecimal A01 =(BigDecimal)object[11];
					String XMBZ =(String)object[12];
					map.put("INSTANCEID", INSTANCEID==null?"0":INSTANCEID.toString());
					map.put("PROJECTNAME", PROJECTNAME);
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
	public List<HashMap> getCloselist(String customername,String cyrxm,int runPageNumber,int runPageSize){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long orgroleid = user.getOrgroleid();
		String cuid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT S.INSTANCEID,B.PROJECTNAME,B.STARTDATE,B.MANAGER,B.OWNER,B.FZJGMC,B.SSSYB,B.A08,B.KHLXR,B.KHLXDH,B.HTJE,B.A01,B.XMBZ");
		sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
		sb.append(" AND B.STATUS='已完成'");
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND B.PROJECTNAME LIKE ?");
			params.add("%"+customername+"%");
		}
//		if(clbm!=null&&!clbm.equals("")){
//			sb.append(" AND B.FZJGMC LIKE ?");
//			params.add("%"+clbm+"%");
//		}
//		if(czbm!=null&&!czbm.equals("")){
//			sb.append(" AND B.GSGK LIKE ?");
//			params.add("%"+czbm+"%");
//		}
		if(cyrxm!=null&&!cyrxm.equals("")){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,instr(B.MANAGER,'[',1)+1, instr(B.MANAGER,']',1)-(instr(B.MANAGER,'[',1)+1)) AS MANAGER,");
			sb.append("SUBSTR(B.DDAP,instr(B.DDAP,'[',1)+1, instr(B.DDAP,']',1)-(instr(B.DDAP,'[',1)+1)) AS DDAP,");
			sb.append("SUBSTR(B.XMZH,instr(B.XMZH,'[',1)+1, instr(B.XMZH,']',1)-(instr(B.XMZH,'[',1)+1)) AS XMZH,");
			sb.append("SUBSTR(B.XMXY,instr(B.XMXY,'[',1)+1, instr(B.XMXY,']',1)-(instr(B.XMXY,'[',1)+1)) AS XMXY,");
			sb.append("SUBSTR(B.XMLS,instr(B.XMLS,'[',1)+1, instr(B.XMLS,']',1)-(instr(B.XMLS,'[',1)+1)) AS XMLS,");
			sb.append("SUBSTR(B.OWNER,instr(B.OWNER,'[',1)+1, instr(B.OWNER,']',1)-(instr(B.OWNER,'[',1)+1)) AS OWNER"); 
			sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.MANAGER LIKE ? OR A.DDAP LIKE ? OR A.XMZH LIKE ? OR A.XMXY LIKE ? OR A.XMLS LIKE ? OR A.OWNER LIKE ? OR B.NAME LIKE ?)");
			sb.append(")");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
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
			sb.append("SUBSTR(B.DDAP,0, INSTR(B.DDAP,'[',1)-1) AS DDAP,");
			sb.append("SUBSTR(B.XMZH,0, INSTR(B.XMZH,'[',1)-1) AS XMZH,");
			sb.append("SUBSTR(B.XMXY,0, INSTR(B.XMXY,'[',1)-1) AS XMXY,");
			sb.append("SUBSTR(B.XMLS,0, INSTR(B.XMLS,'[',1)-1) AS XMLS,");
			sb.append("SUBSTR(B.OWNER,0, INSTR(B.OWNER,'[',1)-1) AS OWNER,");
			sb.append("B.CREATEUSERID AS CUID");
			sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.MANAGER = ? OR A.DDAP = ? OR A.XMZH = ? OR A.XMXY = ? OR A.XMLS = ? OR A.OWNER = ? OR A.CUID = ? OR B.USERID = ?)");
			sb.append(")");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
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
				query.setMaxResults(pageSize1);
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
					String PROJECTNAME =(String)object[1];
					Date STARTDATE =(Date)object[2];
					String MANAGER =(String)object[3];
					String OWNER =(String)object[4];
					String FZJGMC =(String)object[5];
					String SSSYB =(String)object[6];
					BigDecimal A08 =(BigDecimal)object[7];
					String KHLXR =(String)object[8];
					String KHLXDH =(String)object[9];
					BigDecimal HTJE =(BigDecimal)object[10];
					BigDecimal A01 =(BigDecimal)object[11];
					String XMBZ =(String)object[12];
					map.put("INSTANCEID", INSTANCEID==null?"0":INSTANCEID.toString());
					map.put("PROJECTNAME", PROJECTNAME);
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
	public List<HashMap> getCloselistSize(String customername,String cyrxm){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long orgroleid = user.getOrgroleid();
		String cuid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT S.INSTANCEID,B.PROJECTNAME,B.STARTDATE,B.MANAGER,B.OWNER,B.FZJGMC,B.SSSYB,B.A08,B.KHLXR,B.KHLXDH,B.HTJE,B.A01,B.XMBZ");
		sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
		sb.append(" AND B.STATUS='已完成'");
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND B.PROJECTNAME LIKE ?");
			params.add("%"+customername+"%");
		}
//		if(clbm!=null&&!clbm.equals("")){
//			sb.append(" AND B.FZJGMC LIKE ?");
//			params.add("%"+clbm+"%");
//		}
//		if(czbm!=null&&!czbm.equals("")){
//			sb.append(" AND B.GSGK LIKE ?");
//			params.add("%"+czbm+"%");
//		}
		if(cyrxm!=null&&!cyrxm.equals("")){
			sb.append(" AND B.PROJECTNO IN(");
			sb.append("SELECT A.PROJECTNO FROM"); 
			sb.append(" (SELECT B.PROJECTNO,S.INSTANCEID,");
			sb.append("SUBSTR(B.MANAGER,instr(B.MANAGER,'[',1)+1, instr(B.MANAGER,']',1)-(instr(B.MANAGER,'[',1)+1)) AS MANAGER,");
			sb.append("SUBSTR(B.DDAP,instr(B.DDAP,'[',1)+1, instr(B.DDAP,']',1)-(instr(B.DDAP,'[',1)+1)) AS DDAP,");
			sb.append("SUBSTR(B.XMZH,instr(B.XMZH,'[',1)+1, instr(B.XMZH,']',1)-(instr(B.XMZH,'[',1)+1)) AS XMZH,");
			sb.append("SUBSTR(B.XMXY,instr(B.XMXY,'[',1)+1, instr(B.XMXY,']',1)-(instr(B.XMXY,'[',1)+1)) AS XMXY,");
			sb.append("SUBSTR(B.XMLS,instr(B.XMLS,'[',1)+1, instr(B.XMLS,']',1)-(instr(B.XMLS,'[',1)+1)) AS XMLS,");
			sb.append("SUBSTR(B.OWNER,instr(B.OWNER,'[',1)+1, instr(B.OWNER,']',1)-(instr(B.OWNER,'[',1)+1)) AS OWNER"); 
			sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.MANAGER LIKE ? OR A.DDAP LIKE ? OR A.XMZH LIKE ? OR A.XMXY LIKE ? OR A.XMLS LIKE ? OR A.OWNER LIKE ? OR B.NAME LIKE ?)");
			sb.append(")");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
			params.add("%"+cyrxm+"%");
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
			sb.append("SUBSTR(B.DDAP,0, INSTR(B.DDAP,'[',1)-1) AS DDAP,");
			sb.append("SUBSTR(B.XMZH,0, INSTR(B.XMZH,'[',1)-1) AS XMZH,");
			sb.append("SUBSTR(B.XMXY,0, INSTR(B.XMXY,'[',1)-1) AS XMXY,");
			sb.append("SUBSTR(B.XMLS,0, INSTR(B.XMLS,'[',1)-1) AS XMLS,");
			sb.append("SUBSTR(B.OWNER,0, INSTR(B.OWNER,'[',1)-1) AS OWNER,");
			sb.append("B.CREATEUSERID AS CUID");
			sb.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项表单')");
			sb.append(") A");
			sb.append(" LEFT JOIN"); 
			sb.append(" (SELECT B.USERID,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN  SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) B");
			sb.append(" ON A.INSTANCEID=B.INSTANCEID WHERE 1=1 AND (A.MANAGER = ? OR A.DDAP = ? OR A.XMZH = ? OR A.XMXY = ? OR A.XMLS = ? OR A.OWNER = ? OR A.CUID = ? OR B.USERID = ?)");
			sb.append(")");
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
			params.add(cuid);
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal INSTANCEID =(BigDecimal)object[0]; 
					String PROJECTNAME =(String)object[1];
					Date STARTDATE =(Date)object[2];
					String MANAGER =(String)object[3];
					String OWNER =(String)object[4];
					String FZJGMC =(String)object[5];
					String SSSYB =(String)object[6];
					BigDecimal A08 =(BigDecimal)object[7];
					String KHLXR =(String)object[8];
					String KHLXDH =(String)object[9];
					BigDecimal HTJE =(BigDecimal)object[10];
					BigDecimal A01 =(BigDecimal)object[11];
					String XMBZ =(String)object[12];
					map.put("INSTANCEID", INSTANCEID==null?"0":INSTANCEID.toString());
					map.put("PROJECTNAME", PROJECTNAME);
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
	
	public List<HashMap> getQtxsList(int pageSize,int pageNumber){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long ismanager = user.getIsmanager();
		String cuid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SXMC,SXZYNR,CJSJ,SPZT,LCBH,TASKID,INSTANCEID FROM BD_XP_QTSXLCB B WHERE 1=1");
		if(ismanager==1L){
			sb.append(" AND B.CREATEUSERID IN (SELECT USERID FROM ORGUSER O WHERE O.DEPARTMENTID=(SELECT DEPARTMENTID FROM ORGUSER WHERE USERID=?))");
		}else{
			sb.append(" AND B.CREATEUSERID=?");
		}
		params.add(cuid);
		sb.append(" ORDER BY B.ID DESC");
				
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List param = params;
		final String sql = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
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
					String sxmc = (String)object[0];
					String sxzynr = (String)object[1];
					Date cjsj = (Date)object[2];
					String spzt = (String)object[3];
					BigDecimal lcbh =(BigDecimal)object[4];
					BigDecimal taskid =(BigDecimal)object[5];
					BigDecimal instanceid =(BigDecimal)object[6]; 
					map.put("SXMC", sxmc);
					map.put("SXZYNR", sxzynr);
					map.put("CJSJ", cjsj==null?"":sdf.format(cjsj));
					map.put("SPZT", spzt);
					map.put("LCBH", lcbh==null?"0":lcbh.toString());
					map.put("TASKID", taskid==null?"0":taskid.toString());
					map.put("INSTANCEID", instanceid==null?"0":instanceid.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getQtxsListSize(){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long ismanager = user.getIsmanager();
		String cuid = user.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM BD_XP_QTSXLCB B WHERE 1=1");
		if(ismanager==1L){
			sb.append(" AND B.CREATEUSERID IN (SELECT USERID FROM ORGUSER O WHERE O.DEPARTMENTID=(SELECT DEPARTMENTID FROM ORGUSER WHERE USERID=?))");
		}else{
			sb.append(" AND B.CREATEUSERID=?");
		}
		params.add(cuid);
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
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap<String, Object>> getGpTaskList(String xmbh) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TYXM.ID,");
		sql.append("TYXM.JDMC,");
		sql.append("JDDATA.TBR,");
		sql.append("JDDATA.SPZT,");
		sql.append("JDDATA.FJ,");
		sql.append("JDDATA.XMBH,");
		sql.append("JDDATA.LCBH,");
		sql.append("JDDATA.TASKID,");
		sql.append("JDDATA.LCBS,");
		sql.append("JDDATA.YJINS");
		sql.append(" FROM BD_ZQB_KM_INFO TYXM");
		sql.append(" LEFT JOIN (");
			sql.append("SELECT JD.CREATEUSER AS TBR,");
			sql.append("JD.CREATEUSERID AS TBRID,");
			sql.append("JD.PROJECTNO AS XMBH,");
			sql.append("JD.XMCY AS FJ,");
			sql.append("JD.STATUS AS SPZT,");
			sql.append("JD.XMJD AS JDMC,");
			sql.append("JD.KHLXDH AS JDBH,");
			sql.append("JD.TYPENO AS LCBH,");
			sql.append("JD.CUSTOMERNO AS TASKID,");
			sql.append("JD.KHLXR AS LCBS,");
			sql.append("JD.YJINS");
			sql.append(" FROM (SELECT B.*,D.YJINS FROM BD_ZQB_XMLCXXB B LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148) D ON B.TYPENO=D.GGINS WHERE B.PROJECTNO=?) JD");
		sql.append(") JDDATA ON TYXM.ID=JDDATA.JDBH WHERE TYXM.STATE=1");
		sql.append(" ORDER BY TYXM.SORTID");
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jdmc = rs.getString("JDMC");
				String tbr = rs.getString("TBR");
				String spzt = rs.getString("SPZT");
				StringBuffer fjstr = new StringBuffer();
				String fj = rs.getString("FJ");
				if(fj!=null&&!fj.equals("")){
					List<FileUpload> files = FileUploadAPI.getInstance().getFileUploads(fj);
					for (int i = 0; i < files.size(); i++) {
						if(i==(files.size()-1)){
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName().length()>30?(files.get(i).getFileSrcName().substring(0, 30)+"..."):files.get(i).getFileSrcName()).append("</a>");
						}else{
							fjstr.append("<a href=\"uploadifyDownload.action?fileUUID=").append(files.get(i).getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(FileType.getFileIcon(files.get(i).getFileSrcName())).append("\">").append(files.get(i).getFileSrcName().length()>30?(files.get(i).getFileSrcName().substring(0, 30)+"..."):files.get(i).getFileSrcName()).append("</a>").append("<br>");
						}
					}
				}
				String prono = rs.getString("XMBH");
				String lcbh = rs.getString("LCBH")==null?"0":rs.getString("LCBH");
				String taskid = rs.getString("TASKID")==null?"0":rs.getString("TASKID");
				String lzjd="";
				if(!"0".equals(taskid)){
					lzjd=DBUTilNew.getDataStr("task_def_key_", "select s.task_def_key_ from Process_Hi_Taskinst s WHERE s.ID_='"+taskid+"' order by id_ desc ", null);
				}
				String lcbs = rs.getString("LCBS");
				String yjins = rs.getString("YJINS");
				result.put("ID", id);
				result.put("JDMC", jdmc);
				result.put("TBR", tbr);
				result.put("FJ", fjstr);
				result.put("XMBH", prono);
				result.put("LCBH", lcbh);
				result.put("TASKID", taskid);
				result.put("LCBS", lcbs);
				result.put("YJINS", yjins);
				result.put("SPZT", spzt);
				result.put("LZJD", lzjd);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap> getShowBwlList(String projectNo, int pageNumber, int pageSize, Long formid,String projectname) {
		if(projectNo==null||"".equals(projectNo)){
			return null;
		}

		OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();;
		Long isManager = orgUser.getIsmanager();
		Long orgRoleid = orgUser.getOrgroleid();
		String userid = orgUser.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT TO_CHAR(RB.SCSJ,'yyyy-MM-dd') SCSJ,RB.BWLMC,RB.SCR ,RB.INSTANCEID FROM BD_ZQB_XMBZ  RB INNER JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE FORMID=?) BIND ON RB.ID=BIND.DATAID WHERE 1=1");
		params.add(formid);
//		params.add(projectNo);
		if(orgRoleid!=5){
			if(isManager==1l){
				sb.append(" AND SCRID IN (SELECT O.USERID FROM ORGUSER O WHERE O.DEPARTMENTID IN (SELECT ID FROM ORGDEPARTMENT T START WITH T.ID=(SELECT DEPARTMENTID FROM ORGUSER O WHERE O.USERID= ?　) CONNECT BY PRIOR T.ID=T.PARENTDEPARTMENTID))");
			}else{
				sb.append(" AND SCRID = ?");
			}
			params.add(userid);
		}
		

			sb.append(" AND XMBH like ?");
			params.add("%"+projectNo+"%");
		
		/*if (projectname != null && !"".equals(projectname)) {
			sb.append(" OR PROJECTNO like ?");
			params.add("%"+projectname+"%");
		}*/
		
		sb.append(" ORDER BY RB.SCSJ  DESC");
		final String sql1 = sb.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				String cuid = UserContextUtil.getInstance().getCurrentUserId();
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
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String SCSJ = (String) object[0];
					String BWLMC = (String) object[1];
					String SCR = (String) object[2];
					BigDecimal instanceid=(BigDecimal) object[3];
					
					map.put("SCSJ", SCSJ);
					map.put("BWLMC", BWLMC);
					map.put("SCR", SCR);
					map.put("INSTANCEID", instanceid);
					l.add(map);
				}
				return l;
			}
		});
	}
}
