package com.ibpmsoft.project.zqb.dao;

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

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;

public class HualongZqbCdDAO extends HibernateDaoSupport{
	private final static String CN_FILENAME = "/common.properties";
	//private static Logger logger = Logger.getLogger(ShanXiZqbBgProjectManageService.class);
	
	public List<HashMap<String, Object>> getHldaList(String sql,List<String> parameter, int pageNumber, int pageSize) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int startRow = (pageSize - 1) * pageNumber;
		int endRow = pageNumber * pageSize;
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i));
			}
			ps.setInt(index++, startRow);
			ps.setInt(index++, endRow);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String damc = rs.getString("DAMC");
				String dalx = rs.getString("DALX");
				String gdwz = rs.getString("GDWZ");
				String dabh = rs.getString("DABH");
				String dajs = rs.getString("DAJS");
				String gdfj = rs.getString("DAFJ");
				String lcbh = rs.getString("LCBH");
				String lzjd = rs.getString("LZJD");
				String zt = rs.getString("ZT");
				String instanceid = rs.getString("INSTANCEID");
				String rwid = rs.getString("RWID");
				Integer rm = rs.getInt("RN");
				String yjins = rs.getString("YJINS");
				result.put("id", id);
				result.put("roid", user.getOrgroleid());
				result.put("damc", damc);
				result.put("dalx", dalx);
				result.put("gdwz", gdwz);
				result.put("dabh",dabh);
				result.put("dajs", dajs);
				result.put("gdfj",gdfj);
				result.put("rm", rm);
				result.put("lcbh", lcbh);
				result.put("lzjd",lzjd);
				result.put("zt",zt);
				result.put("instanceid", instanceid);
				result.put("rwid", rwid);
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
	public int getHldaListSize(String sql, List<String> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int count=0;
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				result.put("id", id);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList.size();
	}
	public List<HashMap<String, Object>> getUpd(String sql, List<String> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(1, parameter.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				
				String damc = rs.getString("DAMC");
				String dalx = rs.getString("DALX");
				String gdwz = rs.getString("GDWZ");
				String dabh = rs.getString("DABH");
				String dajs = rs.getString("DAJS");
				String gdfj = rs.getString("DAFJ");
				String yhid = rs.getString("YHID");
				String zt = rs.getString("ZT");
				String sqsj = rs.getString("SQSJ");
				String gdsj = rs.getString("GDSJ");
				result.put("yhid", yhid);
				result.put("damc", damc);
				result.put("dalx", dalx);
				result.put("gdwz", gdwz);
				result.put("dabh", dabh);
				result.put("dajs", dajs);
				result.put("gdfj", gdfj);
				result.put("gdfj", gdfj);
				result.put("zt", zt);
				result.put("sqsj", sqsj);
				result.put("gdsj", gdsj);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap<String, Object>> getGdOutList(String sql, List<String> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i));
			}
			
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String damc = rs.getString("DAMC");
				String dalx = rs.getString("DALX");
				String gdwz = rs.getString("GDWZ");
				String dabh = rs.getString("DABH");
				String dajs = rs.getString("DAJS");
				String gdfj = rs.getString("DAFJ");
				String lcbh = rs.getString("LCBH");
				String lzjd = rs.getString("LZJD");
				String zt = rs.getString("ZT");
				String instanceid = rs.getString("INSTANCEID");
				String rwid = rs.getString("RWID");
				Integer rm = rs.getInt("RN");
				result.put("id", id);
				result.put("roid", user.getOrgroleid());
				result.put("damc", damc);
				result.put("dalx", dalx);
				result.put("gdwz", gdwz);
				result.put("dabh",dabh);
				result.put("dajs", dajs);
				result.put("gdfj",gdfj);
				result.put("rm", rm);
				result.put("lcbh", lcbh);
				result.put("lzjd",lzjd);
				result.put("zt",zt);
				result.put("instanceid", instanceid);
				result.put("rwid", rwid);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List getDgdyOutList(String startdate, String enddate, String status, String dyrId) {
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		sql.append(" select sy,TO_CHAR(sqsj,'yyyy-MM-dd') sqsj,TO_CHAR(yjghsj,'yyyy-MM-dd') yjghsj,dyrxm,spzt,lzwz,lzjd,rwid,TO_CHAR(ghsj,'yyyy-MM-dd') ghsj,lcbh, INSTANCEID,lcbs,DYZLNR from BD_ZQB_DGDYLCB t  where 1=1 ");
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
					l.add(map);
				}
				return l;
			}
		});
	}

}
