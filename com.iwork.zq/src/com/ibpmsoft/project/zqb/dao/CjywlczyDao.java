package com.ibpmsoft.project.zqb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;

public class CjywlczyDao extends HibernateDaoSupport{	
	private static Logger logger = Logger.getLogger(CjywlczyDao.class);
	public List<HashMap> getCjList(String sxmc, int pageSize, int pageNumber) {
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		sql.append("    select t.sxmc,t.sygz,t.plyq,t2.bznr,t3.plbc  from BD_XP_XPSXB t left join  (select to_char(wm_concat(s.bznr)) as bznr ,s.sxid  ");
		sql.append("    from bd_xp_xpsxbz s group by s.sxid) t2 on  to_char(t2.sxid)=to_char(t.id） left join  (select to_char(wm_concat(z.plbc)) as plbc ,z.sxid ");
		sql.append("  from bd_xp_xpsxbc z group by z.sxid) t3 on to_char(t3.sxid)=to_char(t.id) where 1=1 ");
		if (sxmc != null && !sxmc.equals("")) {
			sql.append("and t.sxmc like ? ");
			parameter.add("%"+sxmc+"%");
		}
		sql.append(" order by t.id desc ");
		final String sql1 = sql.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List<String> list = parameter;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
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
					String sxmcs = (String) object[0];
					String sygz = (String)object[1];
					String plyq = (String)object[2];
					String bsbz = (String)object[3];
					String bcwj = (String)object[4];
					if(bsbz!=null && "".equals(bsbz)){
						bsbz=bsbz.replaceAll(",", "<br/>");
					}
					if(bcwj!=null && "".equals(bcwj)){
						bcwj=bcwj.replaceAll(",", "<br/>");
					}
					map.put("sxmc", sxmcs);
					map.put("sygz", sygz);
					map.put("plyq", plyq);
					map.put("bsbz", bsbz);
					map.put("bcwj", bcwj);
				
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getCjListSize(String sxmc) {
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		int count=0;
		sql.append("    select t.sxmc,t.sygz,t.plyq,t2.bznr,t3.plbc  from BD_XP_XPSXB t left join  (select to_char(wm_concat(s.bznr)) as bznr ,s.sxid  ");
		sql.append("    from bd_xp_xpsxbz s group by s.sxid) t2 on  to_char(t2.sxid)=to_char(t.id） left join  (select to_char(wm_concat(z.plbc)) as plbc ,z.sxid ");
		sql.append("  from bd_xp_xpsxbc z group by z.sxid) t3 on to_char(t3.sxid)=to_char(t.id) where 1=1 ");
		if (sxmc != null && !sxmc.equals("")) {
			sql.append("and t.sxmc like ? ");
			parameter.add("%"+sxmc+"%");
		}
		sql.append(" order by t.id desc ");
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
				String sxmcs = rs.getString("sxmc");
				
				map.put("sxmc", sxmcs);
				
				dataList.add(map);
				
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
}
