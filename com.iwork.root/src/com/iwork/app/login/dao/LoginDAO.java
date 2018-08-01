package com.iwork.app.login.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.iwork.app.login.control.LoginContext;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgUser;

public class LoginDAO extends HibernateDaoSupport {
	
	/**
	 * 登陆验证
	 * @return
	 */
	public boolean isCheckOK(LoginContext context){
		boolean flag = false;
		if(context==null)return flag;
		if(context.getUid()==null||"".equals(context.getUid()))return flag;
		if(context.getMD5Pwd()==null||"".equals(context.getMD5Pwd()));
		StringBuffer sql= new StringBuffer();
		sql.append("FROM OrgUser WHERE userid=? and password=? and USERSTATE = 0 ");
		final String sql1=sql.toString();
		final LoginContext context1=context;
		List<OrgUser> list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				// TODO 自动生成方法存根
				Query query=session.createQuery(sql1);
				query.setString(0,context1.getUid());
				query.setString(1,context1.getMD5Pwd());
				return query.list();
			}
		});
		//获取当前日期
		Calendar current_cal = Calendar.getInstance();
		if(list==null||list.isEmpty())return flag;
		if(list.size()>0){
			flag = true;
			OrgUser model = list.get(0);
			if(model.getStartdate()!=null){ 
				Calendar start_cal = Calendar.getInstance();
				start_cal.setTime(model.getStartdate());
				if (start_cal.after(current_cal)){
					flag = false;
				}
			}
			if(model.getEnddate()!=null){
				Calendar end_cal = Calendar.getInstance();
				end_cal.setTime(model.getEnddate());
				if (end_cal.before(current_cal)){
					flag = false;
				}
			}
			
		}
		
		return flag;
	}
//	
//	/**
//	 * 登陆验证
//	 * @return
//	 */
//	public boolean isCheckOK(LoginContext context){
//		boolean flag = false;
//		if(context==null)return flag;
//		if(context.getUid()==null||"".equals(context.getUid()))return flag;
//		if(context.getMD5Pwd()==null||"".equals(context.getMD5Pwd()));
//		StringBuffer sql= new StringBuffer();
//		sql.append("FROM OrgUser WHERE userid='").append(context.getUid()).append("' and password='").append(context.getMD5Pwd()).append("'  and USERSTATE = 0 ");
//		List<OrgUser> list = this.getHibernateTemplate().find(sql.toString());
//		//获取当前日期
//		Calendar current_cal = Calendar.getInstance();
//		if(list==null)return flag;
//		if(list.size()>0){
//			flag = true;
//			OrgUser model = list.get(0);
//			if(model.getStartdate()!=null){ 
//				Calendar start_cal = Calendar.getInstance();
//				start_cal.setTime(model.getStartdate());
//				if (start_cal.after(current_cal)){
//					flag = false;
//				}
//			}
//			if(model.getEnddate()!=null){
//				Calendar end_cal = Calendar.getInstance();
//				end_cal.setTime(model.getEnddate());
//				if (end_cal.before(current_cal)){
//					flag = false;
//				}
//			}
//			
//		}
//		
//		return flag;
//	}

	public boolean isCheckOK(final String phone,final String smsNum, LoginContext context) {
		boolean flag = false;
		if(context==null)return flag;
		if(context.getUid()==null||"".equals(context.getUid()))return flag;
		int count=0;
		StringBuffer sql= new StringBuffer();
		sql.append("select MOBILE,YZM from BD_GE_YZMDL WHERE MOBILE = ? and YZM = ?");
		DBUtilInjection d=new DBUtilInjection();
		
		if(phone!=null&&!"".equals(phone)){
			if(d.HasInjectionData(phone)){
				return flag;
			}
		}
		if(smsNum!=null&&!"".equals(smsNum)){
			if(d.HasInjectionData(smsNum)){
				return flag;
			}
		}
		final String sql2=sql.toString();
		List list = this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql2);
				query.setString(0, phone);
				query.setString(1, smsNum);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				for (Object[] object : list) {
					map = new HashMap();
					String mobile = (String) object[0];
					String yzm = (String) object[1];
					map.put("MOBILE", mobile);
					map.put("YZM", yzm);
					l.add(map);
				}
				return l;
			}
		});
		//获取当前日期
		if(list==null)return flag;
		if(list.size()>0){
			flag = true;
			StringBuffer sqlMobile = new StringBuffer();
			sqlMobile.append("select count(*) as count from orguser where mobile=?");
			Connection conn = DBUtil.open();
			PreparedStatement stmt = null;
			ResultSet rset = null;
			try {
				HashMap map;
				stmt =conn.prepareStatement(sqlMobile.toString());
				stmt.setString(1, phone);
				rset = stmt.executeQuery();
				while(rset.next()){
					map = new HashMap();
					count=rset.getInt("count");
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				DBUtil.close(conn, stmt, rset);
			}
			if(count==0){
				flag=false;
			}else{
				StringBuffer sqlTest= new StringBuffer();
				
				sqlTest.append("FROM OrgUser WHERE userid=? and password=? and USERSTATE = 0 ");
				final String sql1=sqlTest.toString();
				final LoginContext context1=context;
				List<OrgUser> list2 = this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						
						query.setString(0,context1.getUid());
						query.setString(1,context1.getMD5Pwd());
						return query.list();
					}
				});
				//获取当前日期
				Calendar current_cal = Calendar.getInstance();
				if(list2==null)return flag;
				if(list2.size()>0){
					flag = true;
					OrgUser model = list2.get(0);
					if(model.getStartdate()!=null){ 
						Calendar start_cal = Calendar.getInstance();
						start_cal.setTime(model.getStartdate());
						if (start_cal.after(current_cal)){
							flag = false;
						}
					}
					if(model.getEnddate()!=null){
						Calendar end_cal = Calendar.getInstance();
						end_cal.setTime(model.getEnddate());
						if (end_cal.before(current_cal)){
							flag = false;
						}
					}
					
				}
			}
		}
		return flag;
	}
}
