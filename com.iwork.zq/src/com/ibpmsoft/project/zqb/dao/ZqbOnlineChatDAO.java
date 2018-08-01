package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.lob.SerializableClob;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;


public class ZqbOnlineChatDAO extends HibernateDaoSupport{
	public List<HashMap> getList() {
		StringBuffer sb = new StringBuffer("select org.userid userid,org.username username,org.departmentname deptname,org.orgroleid roleid,kh.zqjc zqjc,kh.zqdm zqdm,org.companyname cpname from orguser org left join ORGUSERMAP orgmap on (orgmap.orgroleid=4 or orgmap.orgroleid=5) and org.userid=orgmap.userid left join bd_zqb_kh_base kh on org.extend1=kh.customerno and org.extend2=kh.customername where (org.orgroleid=4 or org.orgroleid=5 or org.orgroleid=3) and org.id!=1");
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
				String zqjc= rs.getString("zqjc")==null?"":rs.getString("zqjc");
				String userid = rs.getString("userid");
				String username = rs.getString("username")==null?"":rs.getString("username");
				String deptname = rs.getString("deptname");
				String cpname = rs.getString("cpname");
				String zqdm = rs.getString("zqdm")==null?"":rs.getString("zqdm");
				long roleid = rs.getLong("roleid");
				map.put("ZQJC", zqjc);
				map.put("USERID", userid);
				map.put("USERNAME", username);
				map.put("DEPTNAME", deptname);
				map.put("ROLEID", roleid);
				map.put("ZQDM", zqdm);
				map.put("CPNAME", cpname);
				l.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return l;
	}

	public List<HashMap> getchatRecordList(String name) {
		StringBuffer sb = new StringBuffer("select org.userid userid,org.username username,org.departmentname deptname,org.orgroleid roleid,kh.zqjc zqjc,kh.zqdm zqdm,org.companyname cpname from orguser org left join ORGUSERMAP orgmap on (orgmap.orgroleid=4 or orgmap.orgroleid=5) and org.userid=orgmap.userid left join bd_zqb_kh_base kh on org.extend1=kh.customerno and org.extend2=kh.customername where (org.orgroleid=4 or org.orgroleid=5 or org.orgroleid=3) and org.id!=1 and org.userid=?");
		List<HashMap> l = new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, name);
			rs = ps.executeQuery();
			while(rs.next()){
				HashMap	map = new HashMap();
				String zqjc= rs.getString("zqjc")==null?"":rs.getString("zqjc");
				String userid = rs.getString("userid");
				String username = rs.getString("username");
				String deptname = rs.getString("deptname");
				String cpname = rs.getString("cpname");
				String zqdm = rs.getString("zqdm")==null?"":rs.getString("zqdm");
				long roleid = rs.getLong("roleid");
				map.put("ZQJC", zqjc);
				map.put("USERID", userid);
				map.put("USERNAME", username);
				map.put("DEPTNAME", deptname);
				map.put("ROLEID", roleid);
				map.put("ZQDM", zqdm);
				map.put("CPNAME", cpname);
				l.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return l;
	}

	public List<HashMap> getOnlineRecordList(String nickname, String chatName,String startdate, String enddate, String content,String sendname,String companyjc) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT A.ID ID,A.USERNAME USERNAME,TO_CHAR(A.DATATIME,'yyyy-MM-dd HH24:mi:ss') DATATIME,A.CONTENT CONTENT,CHATRECORDNAME CHATRECORDNAME,SENDNAME SENDNAME FROM (SELECT * FROM BD_GE_LTJL  WHERE (USERNAME=? OR SENDNAME=? OR SENDNAME='all') ");
		params.add(nickname);
		params.add(chatName);
		if(content != null &&!"".equals(content)){
			sb.append(" AND regexp_like(regexp_replace(content,'<img.*?>',''),'<a.*>.*?.*</a>') or (regexp_like(regexp_replace(content,'<img.*?>',''),'[^<a]') and regexp_replace(content,'<img.*?>','') like ?) ");
			params.add(content);
			params.add("%"+content+"%");
		}
		String nowdate = UtilDate.getNowdate();
		if (startdate != null && !"".equals(startdate)) {
			sb.append(" AND TO_CHAR(DATATIME,'yyyy-MM-dd') >= ? ");
			params.add(startdate);
		}
		if (enddate != null && !"".equals(enddate)) {
			sb.append(" AND TO_CHAR(DATATIME,'yyyy-MM-dd') <= ? ");
			params.add(enddate);
		}
		if(sendname != null && !sendname.equals("") && !sendname.equals("all")){
			sb.append("AND SENDNAME=?");
			params.add(sendname);
		}
		if(companyjc != null && !companyjc.equals("") && !companyjc.equals("null") && !companyjc.equals("all")){
			sb.append("AND SENDNAME LIKE ?");
			params.add("%"+companyjc+"%");
		}
		sb.append(" ORDER BY ID DESC) A");
		
		if((enddate == null || "".equals(enddate))&&(startdate == null || "".equals(startdate))){
			sb.append("  iNNER JOIN (SELECT TO_CHAR(MAX(DATATIME),'yyyy-MM-dd') SJ FROM BD_GE_LTJL WHERE (USERNAME=? OR SENDNAME=? OR SENDNAME='all')");
			params.add(nickname);
			params.add(chatName);
			sb.append(" ) B ON TO_CHAR(A.DATATIME,'yyyy-MM-dd')=B.SJ ");
		}
		
		sb.append(" ORDER BY ID");
		String gslx = "";
		final String sql=sb.toString();
		final List param = params;
		List<HashMap> l = new ArrayList<HashMap>();
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
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				char[] buffer = null; 
				for (Object[] object : list) {
					map = new HashMap();
					Integer id=object[0]==null?0:Integer.valueOf(object[0].toString());
					String username=(String) object[1];
					String datatime=(String) object[2];
					SerializableClob sc = (SerializableClob)object[3];         
					try {             
					//根据CLOB长度创建字符数组     
					buffer = new char[(int)sc.length()];             
					//获取CLOB的字符流Reader,并将内容读入到字符数组中     
					sc.getCharacterStream().read(buffer); 
					} catch (Exception e) { 
					logger.error(e,e); 
					}
					//转换为字符串
					String content = String.valueOf(buffer);
					String CHATRECORDNAME=(String) object[4];
					String sendname=(String) object[5];
					map.put("ID", id);
					map.put("USERNAME", username);
					map.put("DATATIME", datatime);
					map.put("CONTENT", content);
					map.put("CHATRECORDNAME",CHATRECORDNAME);
					map.put("SENDNAME", sendname);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getonlineChatCount(String name,String startime,String endtime) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long orguserid = 0l;
		if(uc != null){
			orguserid = uc.get_userModel().getOrgroleid();
		}
		/*StringBuffer sb = new StringBuffer("SELECT UUID,USERNAME,MONTH,SUM(SENDNUM) SENDNUM FROM (SELECT substr(USERNAME,1,instr(USERNAME,'[',-1)-1) UUID,USERNAME,COUNT(SENDNAME) SENDNUM,TO_CHAR(DATATIME,'yyyy-MM') MONTH FROM BD_GE_LTJL INNER JOIN (select USERID from orguser WHERE ORGROLEID <> 3");
		if(orguserid == 4l){
			sb.append(" AND ORGROLEID <> 5");
		}
		sb.append(") A ON substr(USERNAME,1,instr(USERNAME,'[',-1)-1) = USERID WHERE");
		if(startime!=null && !startime.equals("") && endtime!=null && !endtime.equals("")){
			sb.append(" (TO_CHAR(DATATIME,'yyyy-MM-dd') BETWEEN '"+startime+"' AND '"+endtime+"') AND");
		}else if(endtime!=null && !endtime.equals("")){
			sb.append(" (TO_CHAR(DATATIME,'yyyy-MM-dd') BETWEEN '1900-01-01' AND '"+endtime+"') AND");
		}else if(startime!=null && !startime.equals("")){
			sb.append(" (TO_CHAR(DATATIME,'yyyy-MM-dd') BETWEEN '"+startime+"' AND '2099-12-31') AND");
		}
		sb.append(" SENDNAME != 'all' AND SENDNAME LIKE '%(%' GROUP BY USERNAME,TO_CHAR(DATATIME,'yyyy-MM'),substr(USERNAME,1,instr(USERNAME,'[',-1)-1) UNION ALL SELECT DISTINCT substr(USERNAME,1,instr(USERNAME,'[',-1)-1) UUID,USERNAME,SENDNUM,TO_CHAR(DATATIME,'yyyy-MM') MONTH FROM BD_GE_LTJL INNER JOIN (select USERID from orguser WHERE ORGROLEID <> 3");
		if(orguserid == 4l){
			sb.append(" AND ORGROLEID <> 5");
		}	
		sb.append(") A ON substr(USERNAME,1,instr(USERNAME,'[',-1)-1) = USERID LEFT JOIN (SELECT COUNT(ID) SENDNUM FROM BD_MDM_KHQXGLB WHERE KHFZR IS NOT NULL) A ON 1=1  WHERE SENDNAME = 'all' ");
		if(startime!=null && !startime.equals("") && endtime!=null && !endtime.equals("")){
			sb.append(" AND (TO_CHAR(DATATIME,'yyyy-MM-dd') BETWEEN '"+startime+"' AND '"+endtime+"')");
		}else if(endtime!=null && !endtime.equals("")){
			sb.append(" AND (TO_CHAR(DATATIME,'yyyy-MM-dd') BETWEEN '1900-01-01' AND '"+endtime+"')");
		}else if(startime!=null && !startime.equals("")){
			sb.append(" AND (TO_CHAR(DATATIME,'yyyy-MM-dd') BETWEEN '"+startime+"' AND '2099-12-31')");
		}
		sb.append(") C"); 
		if(orguserid == 4l){
			sb.append(" WHERE USERNAME='"+name+"'");
		}else if(orguserid == 5l){
			sb.append(" WHERE USERNAME!='"+name+"'");
		}
		sb.append(" GROUP BY UUID,USERNAME,MONTH");*/
		StringBuffer sb = new StringBuffer(" SELECT substr(USERNAME,1,instr(USERNAME,'[',-1)-1) UUID,USERNAME,TO_CHAR(DATATIME,'yyyy-MM') MONTH,COUNT(DISTINCT SENDNAME) SENDNUM FROM BD_GE_LTJL INNER JOIN (SELECT USERID FROM ORGUSER WHERE ORGROLEID <> 3 ");
		if(orguserid == 5l){
			sb.append("AND ORGROLEID <> 5 ");
		}
		sb.append(") A ON substr(USERNAME,1,instr(USERNAME,'[',-1)-1) = USERID WHERE SENDNAME != 'all' AND SENDNAME LIKE '%(%'");
		if(startime!=null && !startime.equals("")){
			sb.append(" AND TO_CHAR(DATATIME,'yyyy-MM-dd') >=?");
		}
		if(endtime!=null && !endtime.equals("")){
			sb.append(" AND TO_CHAR(DATATIME,'yyyy-MM-dd') <=?");
		}
		if(orguserid == 4l){
			sb.append(" AND substr(USERNAME,1,instr(USERNAME,'[',-1)-1)=?");
		}
		sb.append(" group by USERNAME,TO_CHAR(DATATIME,'yyyy-MM'),substr(USERNAME,1,instr(USERNAME,'[',-1)-1)");
		List<HashMap> l = new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			int i = 1;
			if(startime!=null && !startime.equals("")){
				ps.setString(i, startime);i++;
			}
			if(endtime!=null && !endtime.equals("")){
				ps.setString(i, endtime);i++;
			}
			if(orguserid == 4l){
				ps.setString(i, name);i++;
			}
			rs = ps.executeQuery();
			while(rs.next()){
				HashMap	map = new HashMap();
				String USERNAME= rs.getString("USERNAME")==null?"":rs.getString("USERNAME");
				String MONTH = rs.getString("MONTH");
				String SENDNUM = rs.getString("SENDNUM");
				map.put("USERNAME", USERNAME);
				map.put("MONTH", MONTH);
				map.put("SENDNUM", SENDNUM);
				l.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return l;
	}
	/**
	 * 挂牌企业处罚记录主方法
	 * @param zqdm 证券代码
	 * @param zqjc 证券简称
	 * @param fssjstart 发生开始时间
	 * @param fssjend   发生结束时间
	 * @param cfqksm    处罚情况说明
	 * @param pageSize  页码
	 * @param pageNow   当前页
	 * @return
	 */
	public List<HashMap> zqbGpqycfjlIndex(String zqjc,String zqdm,String customername,String fssjstart,String fssjend,String cfqksm,int pageSize, int pageNow){
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(" select f.ID,f.CREATEUSER,f.CUSTOMERNAME,f.FSSJ,f.CFQKSM,f.XGZL_1,f.INSTANCEID,f.DATAID,e.zqjc,e.zqdm from (SELECT D.ID,D.CREATEUSER,D.CUSTOMERNAME,D.FSSJ,D.CFQKSM,D.XGZL_1,D.INSTANCEID,D.DATAID FROM (SELECT INSTANCEID,DATAID FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_GPQYCFJL')) A ) C LEFT JOIN (SELECT A.INSTANCEID,DATAID,B.ID,B.CREATEUSER,B.CUSTOMERNAME,B.FSSJ,B.CFQKSM,B.XGZL_1 FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_GPQYCFJL')) A LEFT JOIN BD_XP_GPQYCFJL B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID WHERE 1=1");
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND UPPER(CUSTOMERNAME) LIKE ?");
			params.add("%"+customername.toUpperCase()+"%");
		}
		if(fssjstart!=null&&!fssjstart.equals("")){
			sb.append(" AND FSSJ >= TO_DATE(?, 'YYYY-MM-DD')");
			params.add(fssjstart);
		}
		if(fssjend!=null&&!fssjend.equals("")){
			sb.append(" AND FSSJ <= TO_DATE(?, 'YYYY-MM-DD')");
			params.add(fssjend);
		}
		if(cfqksm!=null&&!cfqksm.equals("")){
			sb.append(" AND LTRIM(RTRIM(CFQKSM)) LIKE ? ");
			params.add("%"+cfqksm+"%");
		}
		sb.append(") F left join BD_ZQB_KH_BASE E  on e.customername= f.customername where 1=1 ");
		if(zqjc!=null&&!zqjc.equals("")){
			sb.append(" AND e.zqjc LIKE ? ");
			params.add("%"+zqjc+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND e.zqdm LIKE ? ");
			params.add("%"+zqdm+"%");
		}
		sb.append(" ORDER BY f.FSSJ DESC");
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
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal) object[0];
					String CREATEUSER = (String) object[1];
					String customername = (String) object[2];
					String FSSJ = object[3]==null?"":sdf.format((Date) object[3]);
					String CFQKSM = (String) object[4];
					String XGZL_1 = (String) object[5];
					BigDecimal INSTANCEID = (BigDecimal) object[6];
					BigDecimal DATAID = (BigDecimal) object[7];
					String ZQJC = (String) object[8];
					String ZQDM = (String) object[9];
					map.put("ID", ID == null ? "0" : ID.toString());
					map.put("CREATEUSER", CREATEUSER == null ? "" : CREATEUSER);
					map.put("CUSTOMERNAME", customername == null ? "" : customername);
					map.put("FSSJ", FSSJ == null ? "" : FSSJ);
					map.put("CFQKSM", CFQKSM == null ? "" : CFQKSM);
					map.put("XGZL_1", XGZL_1 == null ? "" : XGZL_1);
					map.put("INSTANCEID", INSTANCEID == null ? "0" : INSTANCEID.toString());
					map.put("DATAID", DATAID == null ? "0" : DATAID.toString());
					map.put("ZQJC", ZQJC == null ? "您输入的公司名称有误，请核实！" : ZQJC);
					map.put("ZQDM", ZQDM == null ? "" : ZQDM);
					l.add(map);
				}
				return l;
			}
		});
	}
	public String zqbGpqycfjlGetZqdmzqjclist(String zqdm){
		StringBuffer zqdmzqjc = new StringBuffer();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT ZQDM,ZQJC FROM (SELECT BD.ZQDM,BD.ZQJC FROM ORGUSER ORG LEFT JOIN BD_ZQB_KH_BASE BD ON ORG.EXTEND1= BD.CUSTOMERNO WHERE ORG.ORGROLEID='3' AND  BD.STATUS='有效' AND BD.YGP='已挂牌' AND BD.ZQJC IS NOT NULL AND BD.ZQDM IS NOT NULL AND BD.ZQDM LIKE ?)");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, zqdm+"%");
			rs = ps.executeQuery();
			while(rs.next()){
				zqdmzqjc.append(rs.getString(1)+"-");
				zqdmzqjc.append(rs.getString(2)+",");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		
		return zqdmzqjc.toString();
	}
	public String zqbGpqycfjlAddZqdmzqjclist(String zqdm){
		StringBuffer zqdmzqjc = new StringBuffer();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT ZQDM,ZQJC FROM (SELECT BD.ZQDM,BD.ZQJC FROM ORGUSER ORG LEFT JOIN BD_ZQB_KH_BASE BD ON ORG.EXTEND1= BD.CUSTOMERNO WHERE ORG.ORGROLEID='3' AND  BD.STATUS='有效' AND BD.YGP='已挂牌' AND BD.ZQJC IS NOT NULL AND BD.ZQDM IS NOT NULL AND BD.ZQDM LIKE ?)");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, zqdm+"%");
			rs = ps.executeQuery();
			while(rs.next()){
				zqdmzqjc.append(rs.getString(1)+"-");
				zqdmzqjc.append(rs.getString(2)+",");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		
		return zqdmzqjc.toString();
	}
	public String getAllCompany(){
		StringBuffer allcompany = new StringBuffer();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT ORG.DEPARTMENTNAME,BD.ZQJC FROM ORGUSER ORG LEFT JOIN BD_ZQB_KH_BASE BD ON ORG.EXTEND1=BD.CUSTOMERNO WHERE BD.ZQJC IS NOT NULL");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				allcompany.append(rs.getString(1)+"-");
				allcompany.append(rs.getString(2)+",");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		
		return allcompany.toString();
	}
	public List<HashMap> getDgcfjlDc(String fssjstart, String fssjend, String cfqksm,String zqjc,String zqdm){
//		final int pageSize1 = pageSize;
//		final int startRow1 = (pageNow - 1) * pageSize;
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(" select f.CREATEUSER,f.CUSTOMERNAME,f.FSSJ,f.CFQKSM,f.DATAID,e.zqjc,e.zqdm from (SELECT D.ID,D.CREATEUSER,D.CUSTOMERNAME,D.FSSJ,D.CFQKSM,D.XGZL_1,D.INSTANCEID,D.DATAID FROM (SELECT INSTANCEID,DATAID FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_GPQYCFJL')) A ) C LEFT JOIN (SELECT A.INSTANCEID,DATAID,B.ID,B.CREATEUSER,B.CUSTOMERNAME,B.FSSJ,B.CFQKSM,B.XGZL_1 FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_GPQYCFJL')) A LEFT JOIN BD_XP_GPQYCFJL B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID WHERE 1=1");
		
		if(fssjstart!=null&&!fssjstart.equals("")){
			sb.append(" AND FSSJ >= TO_DATE(?, 'YYYY-MM-DD')");
			params.add(fssjstart);
		}
		if(fssjend!=null&&!fssjend.equals("")){
			sb.append(" AND FSSJ <= TO_DATE(?, 'YYYY-MM-DD')");
			params.add(fssjend);
		}
		if(cfqksm!=null&&!cfqksm.equals("")){
			sb.append(" AND CFQKSM LIKE ? ");
			params.add("%"+cfqksm+"%");
		}
		sb.append(") F left join BD_ZQB_KH_BASE E  on e.customername= f.customername where 1=1 ");
		if(zqjc!=null&&!zqjc.equals("")){
			sb.append(" AND e.zqjc LIKE ? ");
			params.add("%"+zqjc+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND e.zqdm LIKE ? ");
			params.add("%"+zqdm+"%");
		}
		sb.append(" ORDER BY f.FSSJ DESC");
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
//				query.setFirstResult(startRow1);
//				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				for (Object[] object : list) {
					map = new HashMap();
					
					String CREATEUSER = (String) object[0];
					String customername = (String) object[1];	
					String FSSJ = object[3]==null?"":sdf.format((Date) object[2]);
					String CFQKSM = (String) object[3];
					
					BigDecimal DATAID = (BigDecimal) object[4];
					String ZQJC = (String) object[5];
					String ZQDM = (String) object[6];
					
					map.put("CREATEUSER", CREATEUSER == null ? "" : CREATEUSER);
					map.put("CUSTOMERNAME", customername == null ? "" : customername);
					map.put("FSSJ", FSSJ == null ? "" : FSSJ);
					map.put("CFQKSM", CFQKSM == null ? "" : CFQKSM);
				
					map.put("DATAID", DATAID == null ? "0" : DATAID.toString());
					map.put("ZQJC", ZQJC == null ? "您输入的公司名称有误，请核实！" : ZQJC);
					map.put("ZQDM", ZQDM == null ? "" : ZQDM);
					l.add(map);
				}
				return l;
			}
		});
		
	}
}
 