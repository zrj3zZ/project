package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;

public class XcjclvDao extends HibernateDaoSupport{
	private static Logger logger = Logger.getLogger(XcjclvDao.class);

	/**
	 * 培训记录
	 * @param zqdmxs
	 * @param zqjcxs
	 * @param startdate
	 * @param enddate
	 * @param xcjcry
	 * @param xcjclx
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List<HashMap> getPxjlList(String zqdmxs,String zqjcxs,Date startdate,Date enddate,String xcjcry,String xcjclx,String pxry,int pageSize,int pageNumber){
		String formid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='培训记录'","FORMID");
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		sql.append("  select t.bankaccount zqdm,t.customername zqjc,  t.nsr, t.id, BINDTABLE.Instanceid,  t.swdjh, t.userid,  to_char(t.cjsj,'yyyy-MM-dd') cjsj   from BD_ZQB_KHXXGL t ");
		sql.append("    inner join SYS_ENGINE_FORM_BIND BINDTABLE on BINDTABLE.Dataid=t.id  and BINDTABLE.INSTANCEID is not null  and BINDTABLE.formid = ?  and BINDTABLE.metadataid = 220 and t.customerno=1 ");
		parameter.add(formid);
		if (zqdmxs != null && !zqdmxs.equals("")) {
			sql.append(" and t.bankaccount like ? ");
			parameter.add("%"+zqdmxs+"%");
		}
		if (zqjcxs != null && !zqjcxs.equals("")) {
			sql.append("and t.customername like ? ");
			parameter.add("%"+zqjcxs+"%");
		}
		if (startdate != null && !startdate.equals("")) {
			sql.append("and t.cjsj >= ? ");
			parameter.add(startdate);
		}
		if (enddate != null && !enddate.equals("")) {
			sql.append("and t.cjsj <= ? ");
			parameter.add(enddate);
		}
		if (xcjcry != null && !xcjcry.equals("")) {
			sql.append("and t.nsr like ? ");
			parameter.add("%"+xcjcry+"%");
		}
		if (pxry != null && !pxry.equals("")) {
			sql.append("and t.userid like ? ");
			parameter.add("%"+pxry+"%");
		}
		if (xcjclx != null && !xcjclx.equals("")) {
			sql.append("and t.swdjh = ? ");
			parameter.add(xcjclx);
		}
		sql.append(" order by t.cjsj desc  ");
		final String sql1 = sql.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List<String> list = parameter;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setFirstResult(startRow1);
				List<HashMap> l = new ArrayList<HashMap>();
				query.setMaxResults(pageSize1);
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
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String zqdm = (String) object[0];
					String zqjc = (String)object[1];
					String cxry= (String)object[2];
					String pxlx = (String)object[5];
					String pxry = (String)object[6];
					BigDecimal  tid=(BigDecimal )object[3];
					BigDecimal  insId=(BigDecimal )object[4];
					String cjsj = (String)object[7];
					map.put("zqdm", zqdm);
					map.put("zqjc", zqjc);
					map.put("cxry", cxry);
					map.put("pxlx", pxlx);
					map.put("pxry", pxry);
					map.put("tid", tid);
					map.put("insId", insId.toString());
					map.put("cjsj", cjsj);
					l.add(map);
				}
				return l;
			}
		});
	}
	/**
	 * 培训记录
	 * @param zqdmxs
	 * @param zqjcxs
	 * @param startdate
	 * @param enddate
	 * @param xcjcry
	 * @param xcjclx
	 * @return
	 */
	public List<HashMap> getPxjlListSize(String zqdmxs,String zqjcxs,String startdate,String enddate,String xcjcry,String xcjclx,String pxry){
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		int count=0;
		sql.append("  select t.bankaccount zqdm,t.customername zqjc,t.bankname sj,t.swdjdz ry,t.nsr from BD_ZQB_KHXXGL t   where 1=1 and t.customerno=1 ");
		if (zqdmxs != null && !zqdmxs.equals("")) {
			sql.append(" and t.bankaccount like ? ");
			parameter.add("%"+zqdmxs+"%");
		}
		if (zqjcxs != null && !zqjcxs.equals("")) {
			sql.append("and t.customername like ? ");
			parameter.add("%"+zqjcxs+"%");
		}
		if (startdate != null && !startdate.equals("")) {
			sql.append("and to_char(t.cjsj,'yyyy-MM-dd') >= ? ");
			parameter.add(startdate);
		}
		if (enddate != null && !enddate.equals("")) {
			sql.append("and to_char(t.cjsj,'yyyy-MM-dd') <= ? ");
			parameter.add(enddate);
		}
		if (xcjcry != null && !xcjcry.equals("")) {
			sql.append("and t.nsr like ? ");
			parameter.add("%"+xcjcry+"%");
		}
		if (pxry != null && !pxry.equals("")) {
			sql.append("and t.userid like ? ");
			parameter.add("%"+pxry+"%");
		}
		if (xcjclx != null && !xcjclx.equals("")) {
			sql.append("and t.swdjh = ? ");
			parameter.add(xcjclx);
		}
		sql.append(" order by t.cjsj desc  ");
		List<HashMap> dataList=new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int j=0;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for(int i=0;i<parameter.size();i++){
				ps.setString(i+1, parameter.get(i));
			}
			rs = ps.executeQuery();
			int i=1;
			while (rs.next()) {
				HashMap<String,Object> map = new HashMap<String,Object>();
				
				map.put("XH", i++);
				
				
				dataList.add(map);
				
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	/**
	 * 现场检查记录
	 * @param zqdmxs
	 * @param zqjcxs
	 * @param startdate
	 * @param enddate
	 * @param xcjcry
	 * @param xcjclx
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List<HashMap> getXcjcList(String zqdmxs,String zqjcxs,Date startdate,Date enddate,String xcjcry,String xcjclx,int pageSize,int pageNumber){
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		sql.append("  select t.bankaccount zqdm,t.customername zqjc,to_char(t.bankname,'yyyy-MM-dd') sj,t.swdjdz ry,t.nsr,   t.id,   BINDTABLE.Instanceid,t.swdjh   from BD_ZQB_KHXXGL t ");
		sql.append("    inner join SYS_ENGINE_FORM_BIND BINDTABLE on BINDTABLE.Dataid=t.id  and BINDTABLE.INSTANCEID is not null  and BINDTABLE.formid = 209  and BINDTABLE.metadataid = 220 and t.customerno=0 ");
		if (startdate != null && !startdate.equals("")) {
			sql.append("and t.bankname >= ? ");
			//parameter.add(startdate);
		}
		if (enddate != null && !enddate.equals("")) {
			sql.append("and t.bankname <= ? ");
			//parameter.add(enddate);
		}
		if (zqdmxs != null && !zqdmxs.equals("")) {
			sql.append(" and t.bankaccount like ? ");
			parameter.add("%"+zqdmxs+"%");
		}
		if (zqjcxs != null && !zqjcxs.equals("")) {
			sql.append("and t.customername like ? ");
			parameter.add("%"+zqjcxs+"%");
		}
		
		if (xcjcry != null && !xcjcry.equals("")) {
			sql.append("and t.swdjdz like ? ");
			parameter.add("%"+xcjcry+"%");
		}
		if (xcjclx != null && !xcjclx.equals("")) {
			sql.append("and t.nsr = ? ");
			parameter.add(xcjclx);
		}
		sql.append(" order by t.bankname desc  ");
		final String sql1 = sql.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List<String> list = parameter;
		final Date sdate=startdate;
		final Date edate=enddate;	
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				int n=0;
				if (sdate != null && !sdate.equals("")) {
					query.setParameter(n, sdate);n++;
				}
				if (edate != null && !edate.equals("")) {
					query.setParameter(n, edate);n++;
				}
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i)!=null && !"".equals(list.get(i).toString())){
						String params=list.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i+n, list.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String zqdm = (String) object[0];
					String zqjc = (String)object[1];
					String sj = (String)object[2];
					String ry = (String)object[3];
					String nsr = (String)object[4];
					BigDecimal  tid=(BigDecimal )object[5];
					BigDecimal  insId=(BigDecimal )object[6];
					String swdjh = (String)object[7];
					map.put("zqdm", zqdm);
					map.put("zqjc", zqjc);
					map.put("sj", sj);
					map.put("ry", ry);
					map.put("nsr", nsr);
					map.put("tid", tid);
					map.put("insId", insId.toString());
					map.put("swdjh", swdjh);
					l.add(map);
				}
				return l;
			}
		});
	}
	/**
	 * 现场检查记录
	 * @param zqdmxs
	 * @param zqjcxs
	 * @param startdate
	 * @param enddate
	 * @param xcjcry
	 * @param xcjclx
	 * @return
	 */
	public List<HashMap> getXcjcListSize(String zqdmxs,String zqjcxs,String startdate,String enddate,String xcjcry,String xcjclx){
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		int count=0;
		sql.append("  select t.bankaccount zqdm,t.customername zqjc,t.bankname sj,t.swdjdz ry,t.nsr from BD_ZQB_KHXXGL t   where 1=1 and t.customerno=0 ");
		if (zqdmxs != null && !zqdmxs.equals("")) {
			sql.append("and t.bankaccount like ? ");
			parameter.add("%"+zqdmxs+"%");
		}
		if (zqjcxs != null && !zqjcxs.equals("")) {
			sql.append("and t.customername like ? ");
			parameter.add("%"+zqjcxs+"%");
		}
		if (startdate != null && !startdate.equals("")) {
			sql.append("and to_char(t.bankname,'yyyy-MM-dd') >= ? ");
			parameter.add(startdate);
		}
		if (enddate != null && !enddate.equals("")) {
			sql.append("and to_char(t.bankname,'yyyy-MM-dd') <= ? ");
			parameter.add(enddate);
		}
		if (xcjcry != null && !xcjcry.equals("")) {
			sql.append("and t.swdjdz like ? ");
			parameter.add("%"+xcjcry+"%");
		}
		if (xcjclx != null && !xcjclx.equals("")) {
			sql.append("and t.nsr = ? ");
			parameter.add(xcjclx);
		}
		sql.append(" order by t.bankname desc  ");
		List<HashMap> dataList=new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int j=0;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for(int i=0;i<parameter.size();i++){
				ps.setString(i+1, parameter.get(i));
			}
			rs = ps.executeQuery();
			int i=1;
			while (rs.next()) {
				HashMap<String,Object> map = new HashMap<String,Object>();
				
				map.put("XH", i++);
				
				
				dataList.add(map);
				
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		XcjclvDao.logger = logger;
	}

}
