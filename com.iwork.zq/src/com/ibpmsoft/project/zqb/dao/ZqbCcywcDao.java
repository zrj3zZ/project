package com.ibpmsoft.project.zqb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;

public class ZqbCcywcDao extends HibernateDaoSupport{

	private final static String CN_FILENAME = "/common.properties";
	public List<HashMap<String, Object>> getCcwcList(String sql,List<String> parameter, int pageNumber, int pageSize) {
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
				Long id = rs.getLong("ID");
				String yhm = rs.getString("YHM");
				String ccsy = rs.getString("CCSY");
				String bslx = rs.getString("BSLX");
				String qssj = rs.getString("QSSJ");
				String jjsj = rs.getString("JSSJ");
				String ccmdd = rs.getString("CCMDD");
				String lcbh = rs.getString("LCBH");
				String lzjd = rs.getString("LZJD");
				String zt   = rs.getString("ZT");
				String instanceid = rs.getString("INSTANCEID");
				String rwid = rs.getString("RWID");
				Integer rm = rs.getInt("RN");
				result.put("id", id);
				result.put("roid", user.getOrgroleid());
				result.put("yhm", yhm);
				result.put("ccsy", ccsy);
				result.put("bslx", bslx);
				result.put("qssj",qssj);
				result.put("jssj", jjsj);
				result.put("ccmdd",ccmdd);
				if("审批通过".equals(zt)){
					
					result.put("zgzt",bslx);
				}else{
					result.put("zgzt","在岗");
				}
				result.put("zt",zt);
				result.put("rm", rm);
				result.put("lcbh", lcbh);
				result.put("lzjd",lzjd);
				result.put("instanceid", instanceid);
				result.put("rwid", rwid);
				result.put("curruserid", user.getUserid());
				result.put("departmentname", user.getDepartmentname());
				result.put("showbutton", yhm.equals(user.getUsername()));
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public int getCcwcListSize(String sql, List<String> parameter) {
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


}
