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

//东莞底稿调阅 
public class DongGuanZqbDgdyDao extends HibernateDaoSupport {
	private static Logger logger = Logger.getLogger(DongGuanZqbDgdyDao.class);

	public List<HashMap> getDgdyList(Date startdate, Date enddate,String status, String dyrId, int pageSize, int pageNumber) {
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		sql.append("select sy,TO_CHAR(sqsj,'yyyy-MM-dd') sqsj,TO_CHAR(yjghsj,'yyyy-MM-dd') yjghsj,dyrxm,spzt,lzwz,lzjd,rwid,TO_CHAR(ghsj,'yyyy-MM-dd') ghsj,lcbh, INSTANCEID,lcbs,DYZLNR,D.YJINS from BD_ZQB_DGDYLCB t LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148) D ON T.INSTANCEID=D.GGINS where 1=1 ");
		if (startdate != null && !startdate.equals("")) {
			sql.append("and sqsj >= ? ");
		}
		if (enddate != null && !enddate.equals("")) {
			sql.append("and sqsj <= ? ");
		}
		if (status != null && !status.equals("")) {
			sql.append("and spzt = ? ");
			parameter.add(status);
		}
		if (dyrId != null && !dyrId.equals("")) {
			sql.append("and dyrXm like ? ");
			parameter.add("%"+dyrId+"%");
		}
		sql.append(" order by ghsj desc,yjghsj ");
		final String sql1 = sql.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List<String> list = parameter;
		final Date start = startdate;
		final Date endate=enddate;
		/*final String statu=status;
		final String dyrIds=dyrId;*/
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				int n=0;
				if(start!=null){
					
					query.setParameter(n, start);
					n++;
				}
				if(endate!=null){
					
					query.setParameter(n, endate);
					n++;
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
					String sy = (String) object[0];
					String sqsj = (String)object[1];
					String yjghsj = (String)object[2];
					String dyrxm = (String)object[3];
					String spzt = (String)object[4];
					String lzwz = (String)object[5];
					String lzjd = (String)object[6];
					String rwid = (String)object[7];
					String ghsj = (String)object[8];
					String lcbh=(String)object[9];
					String INSTANCEID=(String) object[10].toString();
					String lcbs=(String)object[11];
					String DYZLNR=(String)object[12];
					BigDecimal yjins=(BigDecimal)object[13];
					if(ghsj!=null &&  !"".equals(ghsj)){
						map.put("ghzt", "已归还");
					}else{
						map.put("ghzt", "未归还");
					}
					map.put("sy", sy);
					map.put("sqsj", sqsj);
					map.put("yjghsj", yjghsj);
					map.put("dyrxm", dyrxm);
					map.put("spzt", spzt);
					map.put("lzwz", lzwz);
					map.put("lzjd", lzjd);
					map.put("lcbh", lcbh);
					map.put("INSTANCEID", INSTANCEID);
					map.put("rwid", rwid);
					map.put("lcbs", lcbs);
					map.put("DYZLNR", DYZLNR);
					map.put("YJINS", yjins);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getDgdyListSize(String startdate, String enddate,String status, String dyrId) {
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		int count=0;
		sql.append(" select sy,TO_CHAR(sqsj,'yyyy-MM-dd') sqsj,TO_CHAR(yjghsj,'yyyy-MM-dd') yjghsj,dyrxm,spzt,lzwz,lzjd,rwid,ghsj from BD_ZQB_DGDYLCB t  where 1=1 ");
		if (startdate != null && !startdate.equals("")) {
			sql.append("and TO_CHAR(sqsj,'yyyy-MM-dd')  >= ? ");
			parameter.add(startdate);
		}
		if (enddate != null && !enddate.equals("")) {
			sql.append("and TO_CHAR(sqsj,'yyyy-MM-dd') <= ? ");
			parameter.add(enddate);
		}
		if (status != null && !status.equals("")) {
			sql.append("and spzt = ? ");
			parameter.add(status);
		}
		if (dyrId != null && !dyrId.equals("")) {
			sql.append("and dyrXm like ? ");
			parameter.add("%"+dyrId+"%");
		}
		sql.append(" order by ghsj,yjghsj desc ");
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
				String sy = rs.getString("sy");
				String sqsj = rs.getString("sqsj");
				String yjghsj = rs.getString("yjghsj");
				String dyrxm = rs.getString("dyrxm");
				String spzt = rs.getString("spzt");
				String lzjd = rs.getString("lzjd");
				String rwid=rs.getString("rwid");
				map.put("XH", i++);
				map.put("sy", sy);
				map.put("sqsj", sqsj);
				map.put("yjghsj", yjghsj);
				map.put("dyrxm", dyrxm);
				map.put("spzt", spzt);
				map.put("lzjd", lzjd);
				map.put("rwid", rwid);
				
				dataList.add(map);
				
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public List<HashMap> getDgdyDc(Date startdate, Date enddate,String status, String dyrId) {
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		sql.append("   select s.dyzlnr,to_char(s.sqsj,'yyyy-MM-dd'),to_char(s.yjghsj,'yyyy-MM-dd'),s.dyrxm,s.sy,s.spzt from BD_ZQB_DGDYLCB s where 1=1");
		if (startdate != null && !startdate.equals("")) {
			sql.append("and sqsj >= ? ");
			parameter.add(startdate);
		}
		if (enddate != null && !enddate.equals("")) {
			sql.append("and sqsj <= ? ");
			parameter.add(enddate);
		}
		if (status != null && !status.equals("")) {
			sql.append("and spzt = ? ");
			parameter.add(status);
		}
		if (dyrId != null && !dyrId.equals("")) {
			sql.append("and dyrXm like ? ");
			parameter.add("%"+dyrId+"%");
		}
		sql.append(" order by ghsj desc,yjghsj ");
		final String sql1 = sql.toString();
		
		final List<String> list = parameter;
		/*final Date start = startdate;
		final Date endate=enddate;
		final String statu=status;
		final String dyrIds=dyrId;*/
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
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String dyzlnr = (String) object[0];
					String sqsj = (String)object[1];
					String yjghsj = (String)object[2];
					String dyrxm = (String)object[3];
					String sy = (String)object[4];
					String spzt=(String)object[5];
					
					map.put("dyzlnr", dyzlnr==null?"":dyzlnr);
					map.put("sqsj", sqsj);
					map.put("yjghsj", yjghsj);
					map.put("dyrxm", dyrxm);
					map.put("sy", sy==null?"":sy);
					map.put("spzt", spzt==null?"未归还":spzt);
					
					l.add(map);
				}
				return l;
			}
		});
	}
	
}
