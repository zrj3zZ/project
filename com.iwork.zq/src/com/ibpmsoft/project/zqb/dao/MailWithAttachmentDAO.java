package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibpmsoft.project.zqb.common.ZQBRoleConstants;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.sms.bean.MailLog;

public class MailWithAttachmentDAO extends HibernateDaoSupport {
	public void addBoData(MailLog model) {
		this.getHibernateTemplate().save(model);
	}
	
	public List getDept(Long companyid,Long deptId) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long orgRoleId = uc.get_userModel().getOrgroleid();
		String userid = uc.get_userModel().getUserid();
		boolean flag = false;
		List<String> roleList = new ArrayList<String>();
		List<OrgUserMap> orgUserMapList = uc.get_userMapList();
		for (OrgUserMap orgUserMap : orgUserMapList) {
			roleList.add(orgUserMap.getOrgroleid());
		}
		if(roleList.contains(ZQBRoleConstants.ISPURVIEW_ROLE_ID_DUDAO)&&!roleList.contains(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG)){//非场外角色兼职督导角色
			flag=true;
		}
		List dataList=new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT DISTINCT * FROM (SELECT DPT.ID ID,DPT.DEPARTMENTNAME DEPARTMENTNAME,ORDERINDEX FROM ORGDEPARTMENT DPT INNER JOIN (SELECT DEPARTMENTID,EMAIL,USERID FROM ORGUSER WHERE EMAIL IS NOT NULL AND USERSTATE=0  AND SYSDATE<ENDDATE");
		if((orgRoleId==4l||flag)&&deptId==51l){
			sql.append(" AND EXTEND1 IN(SELECT KHBH FROM BD_MDM_KHQXGLB WHERE SUBSTR(KHFZR,0,INSTR(KHFZR,'[',1)-1)=? OR SUBSTR(ZZCXDD,0,INSTR(ZZCXDD,'[',1)-1)=? OR SUBSTR(FHSPR,0,INSTR(FHSPR,'[',1)-1)=? OR SUBSTR(ZZSPR,0,INSTR(ZZSPR,'[',1)-1)=? OR SUBSTR(CWSCBFZR2,0,INSTR(CWSCBFZR2,'[',1)-1)=? OR SUBSTR(CWSCBFZR3,0,INSTR(CWSCBFZR3,'[',1)-1)=? OR SUBSTR(FBBWJSHR,0,INSTR(FBBWJSHR,'[',1)-1)=? OR SUBSTR(GGFBR,0,INSTR(GGFBR,'[',1)-1)=? OR SUBSTR(qynbrysh,0,INSTR(qynbrysh,'[',1)-1)=?)");
		}
		sql.append(") ORG ON DPT.ID=ORG.DEPARTMENTID WHERE COMPANYID=? AND DEPARTMENTSTATE=0 AND PARENTDEPARTMENTID = ? ORDER BY ORDERINDEX,ID)");
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			if((orgRoleId==4l||flag)&&deptId==51l){
				ps.setString(1, userid);
				ps.setString(2, userid);
				ps.setString(3, userid);
				ps.setString(4, userid);
				ps.setString(5, userid);
				ps.setString(6, userid);
				ps.setString(7, userid);
				ps.setString(8, userid);
				ps.setString(9, userid);
				ps.setLong(10, companyid);
				ps.setLong(11, deptId);
			}else{
				ps.setLong(1, companyid);
				ps.setLong(2, deptId);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String departmentname = rs.getString("DEPARTMENTNAME");
				result.put("DEPTID", id);
				result.put("DEPTNAME", departmentname);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public List<HashMap> getGroupDepartmentid(String id) {
		if(id.endsWith(",")){
			id = id.substring(0, id.length()-1);
		}
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT D.ID,D.DEPARTMENTNAME,D.COMPANYID,D.PARENTDEPARTMENTID,D.DEPARTMENTDESC,D.DEPARTMENTNO,D.LAYER,D.ORDERINDEX,D.ZONENO,D.ZONENAME,D.EXTEND1,D.EXTEND2,D.EXTEND3,D.EXTEND4,D.EXTEND5,D.DEPARTMENTSTATE FROM ORGDEPARTMENT D WHERE 1=1");
		if(id!=null&&!id.equals("")){
			sb.append(" AND D.PARENTDEPARTMENTID IN(");
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==(ids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
			}
			sb.append(")");
		}
		final String sql1 = sb.toString();
		final String final_id = id;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				if(final_id!=null&&!final_id.equals("")){
					String[] ids = final_id.split(",");
					for (int i = 0; i < ids.length; i++) {
						query.setInteger(i, Integer.parseInt(ids[i]));
					}
				}
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					BigDecimal parentdepartmentid = (BigDecimal) object[3];
					map.put("ID", id);
					map.put("PARENTDEPARTMENTID", parentdepartmentid);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getGroupData(String id,String id2) {
		if(id.endsWith(",")){
			id = id.substring(0, id.length()-1);
		}
		if(id2.endsWith(",")){
			id2 = id2.substring(0, id2.length()-1);
		}
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ORG.ID,ORG.USERID,ORG.USERNAME,ORG.EMAIL FROM ORGUSER ORG WHERE 1=1 AND ORG.EMAIL IS NOT NULL AND ORG.USERSTATE=0  AND SYSDATE<ORG.ENDDATE AND ORG.USERID!='NEEQMANAGER'");
		if(id!=null&&!id.equals("")){
			sb.append(" AND ORG.DEPARTMENTID IN(");
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==(ids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
			}
			sb.append(")");
		}
		if(id2!=null&&!id2.equals("")){
			sb.append(" AND ORG.ID IN(");
			String[] ids = id2.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==(ids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
			}
			sb.append(")");
		}
		final String sql1 = sb.toString();
		final String final_id = id;
		final String final_id2 = id2;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int j = 0;
				if(final_id!=null&&!final_id.equals("")){
					String[] ids = final_id.split(",");
					for (int i = 0; i < ids.length; i++) {
						query.setInteger(j, Integer.parseInt(ids[i]));j++;
					}
				}
				if(final_id2!=null&&!final_id2.equals("")){
					String[] ids = final_id2.split(",");
					for (int i = 0; i < ids.length; i++) {
						query.setInteger(j, Integer.parseInt(ids[i]));j++;
					}
				}
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String userid = (String) object[1];
					String username = (String) object[2];
					String email = (String) object[3];
					map.put("ID", id);
					map.put("USERID", userid);
					map.put("USERNAME", username);
					map.put("EMAIL", email);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getGroupEmails(String userid) {
		StringBuffer sb = new StringBuffer();
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
	    if ((userid != null) && (!"".equals(userid))) {
        	if(d.HasInjectionData(userid)){
				return lis;
			}
         
        }
		sb.append("SELECT NAME,EMAIL FROM BD_GE_PHONEBOOK WHERE USERID=?");
		final String sql1 = sb.toString();
		final String final_userid = userid;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setString(0, final_userid);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String username = (String) object[0];
					String email = (String) object[1];
					map.put("USERNAME", username);
					map.put("EMAIL", email);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<String> getEmailNum(String content, String[] sjrarr) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT EMAIL FROM IWORK_MAIL_LOG WHERE EMAIL IN(");
		for (int i = 0; i < sjrarr.length; i++) {
			if(i==(sjrarr.length-1)){
				sql.append("?");
			}else{
				sql.append("?,");
			}
			params.add(sjrarr[i].indexOf("<")!=-1?sjrarr[i].substring(sjrarr[i].indexOf("<") + 1,sjrarr[i].indexOf(">")):sjrarr[i]);
		}
		sql.append(") AND CONTENT = ?");
		params.add(content);
		sql.append(" AND CEIL((SYSDATE - SENDTIME) * 24 * 60 * 60) < 300");
		
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i+1, params.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				String email = rs.getString("EMAIL");
				list.add(email);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return list;
	}
}
