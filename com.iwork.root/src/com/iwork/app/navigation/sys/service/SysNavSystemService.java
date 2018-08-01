package com.iwork.app.navigation.sys.service;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.iwork.app.navigation.directory.dao.SysNavDirectoryDAO;
import com.iwork.app.navigation.sys.dao.SysNavSystemDAO;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.security.NavSecurityUtil;

public class SysNavSystemService {
	private static Logger logger = Logger.getLogger(SysNavSystemService.class);
	private SysNavSystemDAO sysNavSystemDAO;
	private SysNavDirectoryDAO sysNavDirectoryDAO;
	
	public void addBoData(SysNavSystem obj) {
		sysNavSystemDAO.addBoData(obj);
	}

	public void deleteBoData(String id) {
		SysNavSystem model=sysNavSystemDAO.getBoData(id);
		sysNavSystemDAO.deleteBoData(model);
	}
	
	/**
	 * 构建当前用户上下文
	 * @return
	 */
	public String getCurrentUserInfo(UserContext orguser){
    	//获得用户信息字符串
    	StringBuffer userStr = new StringBuffer();
    	userStr.append(orguser.get_userModel().getUsername()).append("[").append(orguser.get_deptModel().getDepartmentname()).append("]");
		return userStr.toString();
	}
	
	/**
	 * 获得日期字符串
	 * @return
	 */
	public String getCurrentDateStr(){
		Calendar today = Calendar.getInstance();
		  SimpleDateFormat dateformat=new SimpleDateFormat("yyyy年MM月dd日 E");
		  String date =dateformat.format(new Date());  
//		String dateStr = date+"&nbsp;&nbsp;&nbsp;农历"+LunarUtil.getLunar(today);
//		String dateStr = date+"&nbsp;&nbsp;&nbsp;农历"+LunarUtil.getLunar(today);
		return date;
	}
	/**
	 * 获得全部子系统列表
	 * @return
	 */
	public List getAll() {
		return sysNavSystemDAO.getAll();
	}
	

	
	/**
	 * 获得当前用户权限范围内子系统
	 * @return
	 */
	public List<SysNavSystem> getMyNavSystemList() {
		List<SysNavSystem> allList =sysNavSystemDAO.getAll();
		List list = new ArrayList();
		for(int i=0;i<allList.size();i++){
			SysNavSystem sysNavSystem = (SysNavSystem)allList.get(i);
			boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(sysNavSystem);
			if(flag){
				list.add(sysNavSystem);
			}
			
		}
		
		return list;
	}

	public SysNavSystem getBoData(String id) {
		// TODO Auto-generated method stub
		return sysNavSystemDAO.getBoData(id);
	} 

	public List getBoDatas(int pageSize, int startRow) {
		// TODO Auto-generated method stub
		return sysNavSystemDAO.getBoDatas(pageSize, startRow);
	}

	public List getBoDatas(String fieldname, String value, int pageSize,
			int startRow) {
		// TODO Auto-generated method stub
		return sysNavSystemDAO.getBoDatas(fieldname, value, pageSize, startRow);
	}

	public String getMaxID() {
		// TODO Auto-generated method stub
		return sysNavSystemDAO.getMaxID();
	}

	public int getRows() {
		// TODO Auto-generated method stub
		return sysNavSystemDAO.getRows();
	}

	public int getRows(String fieldname, String value) {
		// TODO Auto-generated method stub
		return sysNavSystemDAO.getRows(fieldname, value);
	}

	public List queryBoDatas(String fieldname, String value) {
		// TODO Auto-generated method stub
		return sysNavSystemDAO.queryBoDatas(fieldname, value);
	}  

	public void updateBoData(SysNavSystem obj) {
		sysNavSystemDAO.updateBoData(obj);

	}
	
	public List<HashMap> getIpaddress(String userid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select to_char(login_time,'yyyy-mm-dd hh24:mi') as logintime,ipadress as ipaddress from SYS_USER_LOGIN_LOG where id=(select min(id) from (select id,login_time,ipadress from SYS_USER_LOGIN_LOG  where LOGIN_USER=? order by id desc) where rownum<=2)");
		List<HashMap> list = new ArrayList<HashMap>();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, userid);
			rset = stmt.executeQuery();
			while (rset.next()) {
				String login_time=rset.getString("logintime");
				String ipaddress=rset.getString("ipaddress");
				HashMap map=new HashMap();
				map.put("LASTTIME", login_time);
				map.put("IPADDRESS", ipaddress);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}
	
	/**
	 * 向上移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveUp(int id){
		String type="up";
		sysNavSystemDAO.updateIndex(id, type);
	}
	
	/**
	 * 向下移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveDown(int id){
		String type="down";
		sysNavSystemDAO.updateIndex(id, type);
	}
	public void setSysNavSystemDAO(SysNavSystemDAO sysNavSystemDAO) {
		this.sysNavSystemDAO = sysNavSystemDAO;
	}

	public void setSysNavDirectoryDAO(SysNavDirectoryDAO sysNavDirectoryDAO) {
		this.sysNavDirectoryDAO = sysNavDirectoryDAO;
	}

}
