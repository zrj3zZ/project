package com.iwork.app.navigation.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.navigation.function.model.Sysnavfunction;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;

public class SysNavFrameDAO extends HibernateDaoSupport implements
		ISysNavFrameDAO {
	public SysNavFrameDAO() {
	}

	/**
	 * 函数说明：添加信息 参数说明：对象 返回值：
	 */
	public void addBoData(Sysnavfunction model) {
		this.getHibernateTemplate().save(model);
	}

	/**
	 * 函数说明：删除信息 参数说明： 对象 返回值：
	 */
	public void deleteBoData(Sysnavfunction model) {
		this.getHibernateTemplate().delete(model);
	}

	/**
	 * 函数说明：获得所有的信息 参数说明： 返回值：信息的集合
	 */
	public List getAll() {
		String sql = "FROM SysNavFunction ORDER BY ORDERINDEX";
		return this.getHibernateTemplate().find(sql);
	}

	/**
	 * 函数说明：获得总行数 参数说明： 返回值：总行数
	 */
	public int getRows() {
		String sql = "FROM SysNavFunction";
		List list = this.getHibernateTemplate().find(sql);
		return list.size();
	}

	/**
	 * 函数说明：获得一条的信息 参数说明： ID 返回值：对象
	 */
	public Sysnavfunction getBoData(String id) {
		if (id == null){
			id = "0";
		}else{
			DBUtilInjection d=new DBUtilInjection();
			
			if(id!=null&&!"".equals(id)){
				if(d.HasInjectionData(id)){
					return null;
				}
			}
		}
		return (Sysnavfunction) this.getHibernateTemplate().get(
				Sysnavfunction.class, id);
	}

	/**
	 * 函数说明：获得最大ID 参数说明： 返回值：最大ID
	 */
	public String getMaxID() {
		String date = UtilDate.getNowdate();
		String sql = "SELECT MAX(ID)+1 FROM SysNavFunction ";
		String noStr = null;
		List ll = (List) this.getHibernateTemplate().find(sql);
		Iterator itr = ll.iterator();
		if (itr.hasNext()) {
			Object noint = itr.next();
			if (noint == null) {
				noStr = "1";
			} else {
				noStr = noint.toString();
			}
		}

		if (noStr.length() == 1) {
			noStr = "000" + noStr;
		} else if (noStr.length() == 2) {
			noStr = "00" + noStr;
		} else if (noStr.length() == 3) {
			noStr = "0" + noStr;
		} else {
			noStr = noStr;
		}
		return noStr;
	}

	/**
	 * 函数说明：修改信息 参数说明： 对象 返回值：
	 */
	public void updateBoData(Sysnavfunction pd) {
		this.getHibernateTemplate().update(pd);
	}

	/**
	 * 函数说明：查询信息 参数说明： 集合 返回值：
	 */
	public List queryBoDatas(String fieldname, String value) {
		String sql = "FROM SysNavFunction  where ? like ? ORDER BY ORDERINDEX";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(fieldname!=null&&!"".equals(fieldname)){
			if(d.HasInjectionData(fieldname)){
				return l;
			}
		}
		if(value!=null&&!"".equals(value)){
			if(d.HasInjectionData(value)){
				return l;
			}
		}
		Object[] values = {fieldname,"%" + value + "%"};
		return this.getHibernateTemplate().find(sql,values);
	}

	/**
	 * 函数说明：获得总行数 参数说明： 返回值：总行数
	 */
	public int getRows(String fieldname, String value) {
		String sql = "";
		List list = new ArrayList();
		if (fieldname == null || fieldname.equals("") || value == null || value.equals("")){
			sql = " FROM SysNavFunction ";
			list = this.getHibernateTemplate().find(sql);
		}else{
			sql = "FROM SysNavFunction where ? = ?";
			DBUtilInjection d=new DBUtilInjection();
			
			if(fieldname!=null&&!"".equals(fieldname)){
				if(d.HasInjectionData(fieldname)){
					return 0;
				}
			}
			if(value!=null&&!"".equals(value)){
				if(d.HasInjectionData(value)){
					return 0;
				}
			}
			Object[] values = {fieldname,value}; 
			list = this.getHibernateTemplate().find(sql,values);
		}

		return list.size();
	}

	/**
	 * 函数说明：获得所有的信息 参数说明： 返回值：信息的集合
	 */
	public List getBoDatas(int pageSize, int startRow)
			throws HibernateException {
		final int pageSize1 = pageSize;
		final int startRow1 = startRow;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {

			public List doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session
						.createQuery("FROM SysNavFunction ORDER BY ORDERINDEX");
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				return query.list();
			}
		});

	}

	/**
	 * 函数说明：查询信息 参数说明： 集合 返回值：
	 */
	public List getBoDatas(String fieldname, String value, int pageSize,
			int startRow) {
		final int pageSize1 = pageSize;
		final int startRow1 = startRow;
		final String queryName = fieldname;
		final String queryValue = value;
		String sql = "";

		if (queryName == null || queryName.equals("") || queryValue == null || queryValue.equals(""))
			sql = "FROM SysNavFunction ORDER BY ORDERINDEX";
		else
			sql = "FROM SysNavFunction where ? = ? ORDER BY ORDERINDEX";

		final String sql1 = sql;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				// TODO 自动生成方法存根
				Query query = session.createQuery(sql1);
				if (queryName == null || queryName.equals("") || queryValue == null || queryValue.equals("")){}else{
					DBUtilInjection d=new DBUtilInjection();
					List l=new ArrayList();
					
						if(d.HasInjectionData(queryName)){
							return l;
						}
					
					
						if(d.HasInjectionData(queryName)){
							return l;
						}
					
					query.setString(0, queryName);
					query.setString(1, queryValue);
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				return query.list();
			}
		});
	}

	/**
	 * 获取近3天要开的未处理的会议和今天“未处理”的事项信息
	 * 
	 * @return
	 */
	public int getFspCount() {
		int num=0;

		// 场外看到所有的，持续督导看到自己分管的，董秘看到自己公司的
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		String username = UserContextUtil.getInstance().getCurrentUserFullName();
		// 判断角色
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		StringBuffer sb = new StringBuffer();
		Long orgRoleId = uc.get_userModel().getOrgroleid();
		if (uc != null) {
			if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {// 场外或质控部负责人,看到所有
				sb.append("select count(*) num");
				sb.append(" from (select id");
				sb.append(" from BD_MEET_PLAN");
				sb.append("  where to_char(plantime, 'yyyyMMDD') >= to_char(sysdate, 'yyyyMMDD')");
				sb.append("     and to_char(plantime, 'yyyyMMDD') <=");
				sb.append("      to_char(sysdate + 2, 'yyyyMMDD')  and ( ycl  is null or ycl='0') ");
				/*sb.append("  union");
				sb.append("   select id");
				sb.append("    from BD_XP_BASEINFO");
				sb.append("    where to_char(sbrq, 'yyyyMMDD') = to_char(sysdate, 'yyyyMMDD')   and ( ycl  is null or ycl='0')) ");*/
			}else {// 其他人员,看分派给自己的
				sb.append("select count(*) num ");
				sb.append(" from (select id");
				sb.append(" from BD_MEET_PLAN");
				sb.append("  where to_char(plantime, 'yyyyMMDD') >= to_char(sysdate, 'yyyyMMDD')");
				sb.append("     and to_char(plantime, 'yyyyMMDD') <=");
				sb.append("      to_char(sysdate + 2, 'yyyyMMDD')");
				sb.append("   and  customerno in(select khbh from BD_MDM_KHQXGLB where"
						+ " khfzr=? or zzcxdd=? or fhspr=? or zzspr=? "
								+ "or ggfbr=?)  and ( ycl  is null or ycl='0') ");
				/*sb.append("  union");
				sb.append("   select id");
				sb.append("    from BD_XP_BASEINFO");
				sb.append("    where to_char(sbrq, 'yyyyMMDD') = to_char(sysdate, 'yyyyMMDD')");
				sb.append("   and  khbh in(select khbh from BD_MDM_KHQXGLB where"
						+ " khfzr=? or zzcxdd=? or fhspr=? or zzspr=? "
								+ "or ggfbr=?)  and ( ycl  is null or ycl='0')");
				sb.append( ")");*/
			}
		}
		Connection conn=DBUtil.open();
		ResultSet rs=null;
		PreparedStatement stmt=null;
		try {
			 stmt=conn.prepareStatement(sb.toString());
			 if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {
				 
			 }else{
				 for (int i = 1; i <= 10; i++) {
					stmt.setString(i, username);
				}
			 }
			 rs=stmt.executeQuery();
		    if(rs.next()){
		    	num=rs.getInt("num");
		    }
		} catch (Exception e) {
			logger.error(e,e);
		}finally {
			DBUtil.close(conn, stmt, rs);
    	}

		return num;
	}
}
