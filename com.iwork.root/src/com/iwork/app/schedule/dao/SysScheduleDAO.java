package com.iwork.app.schedule.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.schedule.model.SysSchedule;
import com.iwork.commons.util.DBUtilInjection;

/**
 * 系统计划任务数据操作类
 * @author LuoChuan
 *
 */
public class SysScheduleDAO extends HibernateDaoSupport implements ISysScheduleDAO {

	/**
	 * 获得所有的计划任务
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List getAllData() {

		String status = null;
		List list = this.getListByStatus(status);
		return list;
	}
	
	/**
	 * 根据Id，获得计划任务
	 * @param String isDisabled
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List getListByStatus(String isDisabled) {
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer("FROM ").append(SysSchedule.DATABASE_ENTITY).append(" WHERE 1=1");
		if (null != isDisabled && !"".equals(isDisabled)) {
			sql.append(" AND ISDISABLED = ?");
		}
		sql.append(" ORDER BY ADD_TIME");
		if(null != isDisabled && !"".equals(isDisabled)){
			DBUtilInjection d=new DBUtilInjection();
			
			if(d.HasInjectionData(isDisabled)){
				return list;
			}
			Object[] value = {isDisabled};
			list = this.getHibernateTemplate().find(sql.toString(),value);
		}else{
			list = this.getHibernateTemplate().find(sql.toString());
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List getListById(String id) {
		String sql = "FROM "+SysSchedule.DATABASE_ENTITY+" WHERE ID=?";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(id!=null&&!"".equals(id)){
			if(d.HasInjectionData(id)){
				return l;
			}
		}
		Object[] value = {id};
		List list = this.getHibernateTemplate().find(sql,value);
		return list;
	}
	
	/**
	 * 根据状态，获得计划任务
	 * @param String 
	 * @return List
	 */
	
	/**
	 * 执行更新操作
	 * @param id				计划任务ID 	
	 * @param planName			计划任务名称
	 * @param planPri			计划任务优先级
	 * @param classz			计划任务执行类
	 * @param usefulLife_start	有效期开始日期
	 * @param usefulLife_end	有限期结束日期
	 * @param planDesc			计划任务描述
	 * @param repeatNum			失败补偿次数
	 * @param rule_type			执行频率
	 * @param execute_time		执行时间
	 * @param execute_poin		执行点
	 * @param intervalMinutes	频率为AWS启动时的间隔时间
	 * @return int				更新记录的条数
	 */
	public int update(String id, String planName, String planPri,
					  String classz, String usefulLife_start, String usefulLife_end,
					  String planDesc, String repeatNum, String rule_type,
					  String execute_time, String execute_point, String intervalMinutes, String status) {
		
		StringBuffer strBuff = new StringBuffer("update " + SysSchedule.DATABASE_ENTITY+"  set id=?");
		if (null != planName && !"".equals(planName)) {
			strBuff.append(" , planname=?");
		}
		if (null != planPri && !"".equals(planPri)) {
			strBuff.append(" , planpri=?");
		}
		if (null != classz && !"".equals(classz)) {
			strBuff.append(" , classz=?");
		}
		if (null != usefulLife_start && !"".equals(usefulLife_start)) {
			strBuff.append(" , usefullife_start=?");
		}
		if (null != usefulLife_end && !"".equals(usefulLife_end)) {
			strBuff.append(" , usefullife_end=?");
		}
		if (null != planDesc && !"".equals(planDesc)) {
			strBuff.append(" , plandesc=?");
		}
		if (null != repeatNum && !"".equals(repeatNum)) {
			strBuff.append(" , repeatnum=?");
		}
		if (null != rule_type && !"".equals(rule_type)) {
			strBuff.append(" , rule_type=?");
		}
		if (null != execute_time && !"".equals(execute_time)) {
			strBuff.append(" , execute_time=?");
		}
		if (null != execute_point && !"".equals(execute_point)) {
			strBuff.append(" , execute_point=?");
		}
		if (null != intervalMinutes && !"".equals(intervalMinutes)) {
			strBuff.append(" , intervalminutes=?");
		}
		if (null != status && !"".equals(status)) {
			strBuff.append(" , isdisabled=?");
		}
		strBuff.append(" where id=?");
		
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Query sql = session.createQuery(strBuff.toString());
		int i=0;
		sql.setString(i, id);i++;
		if (null != planName && !"".equals(planName)) {
			sql.setString(i, planName);i++;
		}
		if (null != planPri && !"".equals(planPri)) {
			sql.setString(i, planPri);i++;
		}
		if (null != classz && !"".equals(classz)) {
			sql.setString(i, classz);i++;
		}
		if (null != usefulLife_start && !"".equals(usefulLife_start)) {
			sql.setString(i, usefulLife_start);i++;
		}
		if (null != usefulLife_end && !"".equals(usefulLife_end)) {
			sql.setString(i, usefulLife_end);i++;
		}
		if (null != planDesc && !"".equals(planDesc)) {
			sql.setString(i, planDesc);i++;
		}
		if (null != repeatNum && !"".equals(repeatNum)) {
			sql.setString(i, repeatNum);i++;
		}
		if (null != rule_type && !"".equals(rule_type)) {
			sql.setString(i, rule_type);i++;
		}
		if (null != execute_time && !"".equals(execute_time)) {
			sql.setString(i, execute_time);i++;
		}
		if (null != execute_point && !"".equals(execute_point)) {
			sql.setString(i, execute_point);i++;
		}
		if (null != intervalMinutes && !"".equals(intervalMinutes)) {
			sql.setString(i, intervalMinutes);i++;
		}
		if (null != status && !"".equals(status)) {
			sql.setString(i, status);i++;
		}
		sql.setString(i, id);i++;
		int r = sql.executeUpdate();
		tx.commit();
		session.close();
		return r;
	}
	
	/**
	 * 执行删除操作
	 * @param id		计划任务ID
	 * @return int		删除记录的条数
	 */
	public int delete(String id) {
		Session session=getSessionFactory().openSession();
		Transaction tx=session.beginTransaction();
		Query sql = session.createQuery("delete from "+SysSchedule.DATABASE_ENTITY+" where ID=?");
		sql.setString(0, id);
		int r = sql.executeUpdate();
		tx.commit();
		session.close();
		return r;
	}
	
	/**
	 * 执行添加操作
	 * @param SysSchedule
	 */
	public void add(SysSchedule model) {
		this.getHibernateTemplate().save(model);
	}

}
