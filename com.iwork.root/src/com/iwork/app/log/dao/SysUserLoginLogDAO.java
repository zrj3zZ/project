package com.iwork.app.log.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.iwork.app.log.model.SysUserLoginLog;
import com.iwork.commons.util.DBUtilInjection;
/**
 * 操作日志数据操作类
 * @author LuoChuan
 *
 */
public class SysUserLoginLogDAO extends HibernateDaoSupport  {
	 
	/**
	 * 添加系统登录日志
	 * @param model
	 */
	public Long addModel(SysUserLoginLog model) {
		Long id = null;
		try{
			this.getHibernateTemplate().save(model);
			id = model.getId();
		}catch(Exception e){logger.error(e,e);}
		return id;
		
	}
	
	/**
	 * 获得指定日期的登录次数
	 * @param date
	 * @return
	 */
	public int getLoginCount(Date startDate,Date endDate){
		String sql = "from SysUserLoginLog where loginTime<=? and loginTime>=? ";
		final String sql1=sql; 
		final Date startDate1=startDate; 
		final Date endDate1=endDate; 
		return this.getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(org.hibernate.Session session)
			throws HibernateException, SQLException {
				// TODO 自动生成方法存根 
				Query query=session.createQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List l=new ArrayList();
				if(startDate1!=null&&!"".equals(startDate1)){
					if(d.HasInjectionData(startDate1.toString())){
						return l;
					}
					query.setDate(0,startDate1);  
				}else{
					query.setDate(0,startDate1);  
				}
				if(endDate1!=null&&!"".equals(endDate1)){
					if(d.HasInjectionData(endDate1.toString())){
						return l;
					}
					query.setDate(1,endDate1); 
				}else{
					query.setDate(1,endDate1);  
				}
					query.setDate(1,endDate1);
				return query.list();
			}
		}).size();
	}
	
	
	public List<SysUserLoginLog> searchLog(String userId){
		String sql = "from SysUserLoginLog where login_user=?";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(userId!=null&&!"".equals(userId)){
			if(d.HasInjectionData(userId)){
				return l;
			}
		}
		Object[] value = {userId};
		List<SysUserLoginLog> list = this.getHibernateTemplate().find(sql,value);
		return list;
	}
	
	public List<SysUserLoginLog> searchMobileLog(String userId){

		String sql = "from SysUserLoginLog where login_type=2 and login_user=?";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(userId!=null&&!"".equals(userId)){
			if(d.HasInjectionData(userId)){
				return l;
			}
		}
		Object[] value = {userId};
		List<SysUserLoginLog> list = this.getHibernateTemplate().find(sql,value);
		return list;
	}
	
	public List<SysUserLoginLog> searchMobileLogGroupByIp(String userId){

		String sql = "from SysUserLoginLog where login_type=2 and login_user=? group by ipaddress";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(userId!=null&&!"".equals(userId)){
			if(d.HasInjectionData(userId)){
				return l;
			}
		}
		Object[] value = {userId};
		List<SysUserLoginLog> list = this.getHibernateTemplate().find(sql,value);
		return list;
	}
	
}
