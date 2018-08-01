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

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;


public class QywgfxjlDao extends HibernateDaoSupport{
	private static Logger logger = Logger.getLogger(QywgfxjlDao.class);
	/**
	 * 
	 * @param gsdm
	 * @param gsjc
	 * @param wgfxnr
	 * @param wgfxlx
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List<HashMap> getQyList(String khbh, String khmc, String gsdm,String gsjc,String wgfxnr,String wgfxlx, int pageSize, int pageNumber) {
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		sql.append("   select s.ycl,s.tjdsh,s.sxmc,to_char(s.sbrq,'yyyy-MM-dd'),s.sxgy_1,s.sxgy, BINDTABLE.Instanceid from bd_xp_baseinfo s ");
		sql.append("  inner join SYS_ENGINE_FORM_BIND BINDTABLE on BINDTABLE.Dataid = s.id    and BINDTABLE.INSTANCEID is not null  and BINDTABLE.formid = 116 where 1=1 ");
		if (khbh != null && !khbh.equals("")) {
			sql.append("and s.khbh = ? ");
			parameter.add(khbh);
		}
		if (khmc != null && !khmc.equals("")) {
			sql.append("and s.khmc = ? ");
			parameter.add(khmc);
		}
		if (gsdm != null && !gsdm.equals("")) {
			sql.append("and s.ycl like ? ");
			parameter.add("%"+gsdm+"%");
		}
		if (gsjc != null && !gsjc.equals("")) {
			sql.append("and s.tjdsh like ? ");
			parameter.add("%"+gsjc+"%");
		}
		if (wgfxnr != null && !wgfxnr.equals("")) {
			sql.append("and s.sxgy like ? ");
			parameter.add("%"+wgfxnr+"%");
		}
		if (wgfxlx != null && !wgfxlx.equals("")) {
			sql.append("and s.sxlx = ? ");
			parameter.add(wgfxlx);
		}
		sql.append(" order by s.tjgddh desc ");
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
					String ycl = (String) object[0];
					String tjdsh = (String)object[1];
					String sxmc = (String)object[2];
					String sbrq=(String)object[3];
					String sxgy_1 = (String)object[4];
					String sxgy = (String)object[5];
					BigDecimal insId = (BigDecimal)object[6];
					map.put("ycl", ycl);
					map.put("tjdsh", tjdsh);
					map.put("sxmc", sxmc);
					map.put("sbrq", sbrq);
					map.put("sxgy_1", sxgy_1);
					map.put("sxgy", sxgy);
					map.put("insId", insId.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getQyListSize(String khbh, String khmc, String gsdm,String gsjc,String wgfxnr,String wgfxlx) {
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		int count=0;
		sql.append("   select s.ycl,s.tjdsh,s.sxmc,s.sbrq,s.sxgy_1,s.sxgy from bd_xp_baseinfo s where 1=1 ");
		if (khbh != null && !khbh.equals("")) {
			sql.append("and s.khbh = ? ");
			parameter.add(khbh);
		}
		if (khmc != null && !khmc.equals("")) {
			sql.append("and s.khmc = ? ");
			parameter.add(khmc);
		}
		if (gsdm != null && !gsdm.equals("")) {
			sql.append("and s.ycl like ? ");
			parameter.add("%"+gsdm+"%");
		}
		if (gsjc != null && !gsjc.equals("")) {
			sql.append("and s.tjdsh like ? ");
			parameter.add("%"+gsjc+"%");
		}
		if (wgfxnr != null && !wgfxnr.equals("")) {
			sql.append("and s.sxgy like ? ");
			parameter.add("%"+wgfxnr+"%");
		}
		if (wgfxlx != null && !wgfxlx.equals("")) {
			sql.append("and s.sxlx = ? ");
			parameter.add(wgfxlx);
		}
		sql.append(" order by s.tjgddh desc ");
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
				String sxmcs = rs.getString("ycl");
				
				map.put("ycl", sxmcs);
				
				dataList.add(map);
				
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap<String, String>> getCustomerAutoDataList(String sql,List<String> parameter){
		List<HashMap<String,String>> dataList=new ArrayList<HashMap<String,String>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for(int i=0;i<parameter.size();i++){
				ps.setString(i+1, parameter.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,String> result = new HashMap<String,String>();
				String zqjc = rs.getString("zqjc");
				String zqdm = rs.getString("zqdm");
				result.put("zqjc", zqjc==null?"":zqjc);
				result.put("zqdm", zqdm==null?"":zqdm);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap<String, String>> getqyxxList(String sql,List<String> parameter){
		List<HashMap<String,String>> dataList=new ArrayList<HashMap<String,String>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=sdf.format( new  Date());
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for(int i=0;i<parameter.size();i++){
				ps.setString(i+1, parameter.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,String> result = new HashMap<String,String>();
				String sxlx = rs.getString("sxlx");
				String sxgy_1 = rs.getString("sxlxs");
				result.put("sxlx", sxlx==null?"":sxlx);
				result.put("sxgy_1", sxgy_1==null?"":sxgy_1);
				result.put("userid",user.getUserid() );
				result.put("username", user.getUsername());
				result.put("time",time);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
public List<HashMap> getMeetExcl(){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select s.ycl,s.tjdsh,to_char(s.sbrq,'yyyy-MM-dd') sbrq,s.sxmc,s.sxgy_1,s.sxgy from bd_xp_baseinfo s ");
		
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String ycl  = (String) object[0];
					String tjjsh = (String) object[1];
					String sbrq = (String) object[2];
					String sxmc = (String) object[3];
					String sxgy_1 = (String) object[4];
					String sxgy = (String) object[5];
					
					map.put("ycl", ycl);
					map.put("tjjsh", tjjsh);
					map.put("sbrq", sbrq);
					map.put("sxmc", sxmc);
					map.put("sxgy_1", sxgy_1);
					map.put("sxgy", sxgy==null?"":sxgy);
					l.add(map);
				}
				return l;
			}
		});
	}
}
