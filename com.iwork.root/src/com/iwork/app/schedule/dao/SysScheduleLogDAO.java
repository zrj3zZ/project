package com.iwork.app.schedule.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.schedule.log.model.SysScheduleLog;
import com.iwork.app.schedule.model.SysSchedule;
import com.iwork.commons.util.DBUtilInjection;

/**
 * 计划任务日志数据操作类
 * @author LuoChuan
 *
 */
public class SysScheduleLogDAO extends HibernateDaoSupport implements ISysScheduleLogDAO {
	
	/**
	 * 执行添加操作
	 * @param SysSchedule
	 */
	public void add(SysScheduleLog model) {
		this.getHibernateTemplate().save(model);
	}
	
	/**
	 * 根据条件获得日志
	 * @param scheduleId
	 * @param execute_time
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List getLogByCondition(String id, String date, String pageSize, String startRow) {
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		StringBuffer sqlBuff = new StringBuffer("FROM ");
		sqlBuff.append(SysScheduleLog.DATABASE_ENTITY).append(" WHERE 1=1");
		if (null != id && !"".equals(id)) {
			if(d.HasInjectionData(id)){
				return l;
			}
			sqlBuff.append(" AND SCHEDULE_ID=?");
		}
		if (null != date && !"".equals(date)) {
			if(d.HasInjectionData(date)){
				return l;
			}
			sqlBuff.append(" AND SUBSTR(EXECUTE_TIME,1,?)=?");
		}
		if (null != startRow && !"".equals(startRow)) {
			if(d.HasInjectionData(startRow)){
				return l;
			}
			
		}
		if (null != pageSize && !"".equals(pageSize)) {
			if(d.HasInjectionData(pageSize)){
				return l;
			}
			
		}
		sqlBuff.append(" ORDER BY EXECUTE_TIME DESC");
	
		Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		Query query=session.createQuery(sqlBuff.toString());
		int i = 0;
		if (null != id && !"".equals(id)) {
			query.setString(i, id);i++;
		}
		if (null != date && !"".equals(date)) {
			query.setInteger(i, date.length());i++;
			query.setString(i, date);i++;
		}
		if (null != startRow && !"".equals(startRow)) {//分页时用到
			
			query.setFirstResult(Integer.valueOf(startRow));
		}
		if (null != pageSize && !"".equals(pageSize)) {//分页时用到
			query.setMaxResults(Integer.valueOf(pageSize));
		}
		
		List list =query.list();
		return list;
//		List list = this.getHibernateTemplate().find(sqlBuff.toString());
//		return list;
	}
	
	/**
	 * 接触计划任务锁
	 * @param id
	 * @return boolean
	 */
	public boolean unlock(String id) {
		
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		String lockSql = " UPDATE " + SysSchedule.DATABASE_ENTITY +" SET TASK_LOCK='1' where ID=?";
		Query sql = session.createQuery(lockSql);
		sql.setString(0, id);
		int r = sql.executeUpdate();
		tx.commit();
		session.close();
		
		return true;
	}
	
	/**
	 * 执行删除日志操作
	 * @param id		计划任务ID
	 * @return int		删除记录的条数
	 */
	public int delete(String id) {
		Session session=getSessionFactory().openSession();
		Transaction tx=session.beginTransaction();
		Query sql = session.createQuery("delete from "+SysScheduleLog.DATABASE_ENTITY+" where SCHEDULE_ID=?");
		sql.setString(0, id);
		int r = sql.executeUpdate();
		tx.commit();
		session.close();
		return r;
	}
}
