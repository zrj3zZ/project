package com.iwork.app.mobile.accessControl.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.iwork.app.mobile.accessControl.bean.AccessBindModel;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.db.DBUtil;
public class AccessControlUtil {
	private static Logger logger = Logger.getLogger(AccessControlUtil.class);
	private static AccessControlUtil instance = null;

	private AccessControlUtil() {
	}

	public static AccessControlUtil getInstance() {
		if (instance == null) {
			instance = new AccessControlUtil();
		}
		return instance;
	}

	public List<AccessBindModel> getUserBindModelList(String userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) cc, t.ipadress FROM SYS_USER_LOGIN_LOG t where login_user=? and login_type =2 group by ipadress");
		List<AccessBindModel> list = new ArrayList<AccessBindModel>();
		Connection conn =null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, userId.toUpperCase());
			rset = stmt.executeQuery();
			if (rset != null) {
				while (rset.next()) {
					AccessBindModel model = new AccessBindModel();
					model.setUserId(userId);
					model.setBindId(rset.getString("ipadress"));
					
					String phoneType = this.getUserLoginType(rset
							.getString("ipadress"));
					
					if(phoneType != null){
						model.setPhoneType(phoneType);
					}else{
						model.setPhoneType("型号未知");
					}
					
					list.add(model);
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
			return null;
		} finally {
			DBUtil.close(conn, stmt, rset);
    	}

		return list;
	}

	public String getUserLastLoginTime(String userId) {

		String[] result = {};
		String sql = "SELECT * FROM SYS_USER_LOGIN_LOG where login_user=? and login_type=2 order by login_time desc";
		Map params = new HashMap();
		params.put(1, userId);
		Timestamp lastTime = com.iwork.commons.util.DBUtil.getTimestamp("LOGIN_TIME",sql,params);
		//Timestamp lastTime = DBUtil.getTimestamp(sql, "login_time");

		if (lastTime != null) {
			result = lastTime.toString().split(".");
			if (result.length > 0) {
				return result[0];
			} else {
				return lastTime.toString();
			}
		}
		return "";
	}

	public String countUserLoginNum(String userId) {

		String sql = "SELECT count(*) cc FROM SYS_USER_LOGIN_LOG where login_user=? and login_type=2 ";
		Map params = new HashMap();
		params.put(1, userId);
		int loginCount = DBUTilNew.getInt("cc",sql,params);
		return loginCount + "次";
	}

	public String getUserVisitsetData(String userId) {

		String sql = "SELECT * FROM SYS_MOBILE_VISITSET where userid=?";
		Map params = new HashMap();
		params.put(1, userId);
		return com.iwork.commons.util.DBUtil.getDataStr("USERID", sql, params);
	}

	public String getUserLoginType(String bindId) {

		String sql = "SELECT login_label FROM SYS_USER_LOGIN_LOG where ipadress=?";
		Map params = new HashMap();
		params.put(1, bindId);
		return com.iwork.commons.util.DBUtil.getDataStr("login_label", sql, params);
	}
}
