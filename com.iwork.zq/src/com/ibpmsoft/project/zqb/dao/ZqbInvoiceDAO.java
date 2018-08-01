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

public class ZqbInvoiceDAO extends HibernateDaoSupport {

	public List<HashMap<String, Object>> getKhList(String sql,List<String> parameter, int pageNumber, int pageSize) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber * pageSize;
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, "%"+parameter.get(i).toString().toUpperCase()+"%");
			}
			ps.setInt(index++, startRow);
			ps.setInt(index++, endRow);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				Long instanceid = rs.getLong("INSTANCEID");
				String customerno = rs.getString("CUSTOMERNO");
				String customername = rs.getString("CUSTOMERNAME");
				String bankname = rs.getString("BANKNAME");
				String bankaccount = rs.getString("BANKACCOUNT");
				Integer rownum = rs.getInt("RM");
				result.put("ID", id);
				result.put("INSTANCEID", instanceid);
				result.put("CUSTOMERNO", customerno);
				result.put("CUSTOMERNAME", customername);
				result.put("BANKNAME", bankname);
				result.put("BANKACCOUNT", bankaccount);
				result.put("ROWNUM", rownum);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public int getKhListSize(String sql, List<String> parameter) {
		int count=0;
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, "%"+parameter.get(i).toString().toUpperCase()+"%");
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("cnum");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}
	
	public List<HashMap<String, Object>> getKpList(String sql,List<String> parameter, int pageNumber, int pageSize) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int startRow = (pageNumber - 1) * pageSize;
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
				Long id = rs.getLong("id");
				String customername = rs.getString("customername");
				String customerno = rs.getString("customerno");
				String state = rs.getString("state");
				String nsrlx = rs.getString("nsrlx");
				String sqrq = rs.getString("sqrq");
				Integer rm = rs.getInt("rn");
				result.put("id", id);
				result.put("roid", user.getOrgroleid());
				result.put("customername", customername);
				result.put("customerno", customerno);
				result.put("state", state);
				result.put("nsrlx", nsrlx);
				result.put("sqrq", sqrq);
				result.put("rm", rm);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap<String, Object>> getUpd(String sql,List<String> parameter) {
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
				Long id = rs.getLong("id");
				String customername = rs.getString("customername");
				String customerno = rs.getString("customerno");
				String state = rs.getString("state");
				String nsrlx = rs.getString("nsrlx");
				String sqrq = rs.getString("sqrq");
				String memo = rs.getString("memo");
				result.put("id", id);
				result.put("customername", customername);
				result.put("customerno", customerno);
				result.put("state", state);
				result.put("nsrlx", nsrlx);
				result.put("sqrq", sqrq);
				result.put("memo", memo);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public int getKpListSize(String sql, List<String> parameter) {
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
				Long id = rs.getLong("id");
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
	/**
	 * 导出工作底稿归档
	 * @param customername
	 * @param customerno
	 * @param state
	 * @return
	 */
	public List<HashMap> getDdggDc(String customername,String customerno,String state){
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		sql.append("  select s.customername,s.customerno,to_char(s.sqrq,'yyyy-MM-dd'),s.nsrlx,s.state from BD_ZQB_SQKP s where 1 = 1 ");
		if (customername != null && !customername.equals("")) {
			sql.append(" and s.customername like ? ");
			parameter.add("%"+customername+"%");
		}
		if (customerno != null && !customerno.equals("")) {
			sql.append("and s.customerno like ? ");
			parameter.add("%"+customerno+"%");
		}
	
		if (state != null && !state.equals("")) {
			sql.append("and s.state = ? ");
			parameter.add(state);
		}
		sql.append(" order by s.sqrq desc  ");
		final String sql1 = sql.toString();
		
		final List<String> list = parameter;
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
					String customername = (String) object[0];
					String customerno = (String)object[1];
					String sqrq = (String)object[2];
					String nsrlx = (String)object[3];
					String state = (String)object[4];
					map.put("customername", customername);
					map.put("customerno", customerno==null?"":customerno);
					map.put("sqrq", sqrq);
					map.put("nsrlx", nsrlx);
					map.put("state", state);
					
					l.add(map);
				}
				return l;
			}
		});
	}
}
