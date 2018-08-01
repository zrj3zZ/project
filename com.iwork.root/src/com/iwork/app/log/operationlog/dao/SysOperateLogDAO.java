package com.iwork.app.log.operationlog.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.log.operationlog.model.SysOperateLog;
import com.iwork.commons.util.DBUtilInjection;

/**
 * 用户操作记录数据库操作类
 * @author chenM
 *
 */
public class SysOperateLogDAO extends HibernateDaoSupport{
	/**
	 * 删除用户
	 * @return
	 */
	public boolean batchDeleteLog(List<SysOperateLog> list){
		Session session = getHibernateTemplate().getSessionFactory().openSession();
    	Transaction tx = session.beginTransaction();
    	try{
	    	if(null!=list&&list.size()>0){
	    		for(int i=0;i<list.size();i++){
	    			SysOperateLog model = list.get(i);
	    			if(null==model){continue;}
	    			session.delete(model);
	    			if(i%100==0){
						session.flush();
						session.clear();
					}
	    		}
	    		tx.commit();
	    	}
	    	return true;
    	}catch(HibernateException   e){
			tx.rollback();
			logger.error(e,e);
			return false;
		}finally{
			session.close();
		}
	}
	
	/**
	 * 获得指定用户的指定操作的以及指定时间最近的一条记录
	 * @param userid
	 * @param logtype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SysOperateLog getUserLatestSpecOperateLog(Date specDate,String userid,String logtype){
		Session session=getSessionFactory().openSession();
		Criteria criteria=session.createCriteria(SysOperateLog.class);
		criteria.add(Restrictions.eq("userid", userid));
		criteria.add(Restrictions.eq("logtype", logtype));
		criteria.add(Restrictions.le("createdate", specDate));
		criteria.addOrder(Order.desc("indexid"));
		List<SysOperateLog> list = criteria.list();
		session.close();
		if(null!=list&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	
	/**
	 * 获得指定用的某项操作的最大INDEXID
	 * @param userid
	 * @param logtype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getUserSpecOperateMaxIndexId(String userid,String logtype){
		String sql="SELECT MAX(indexid)+1 FROM SysOperateLog ";
		String noStr = null;
		List<String> list = this.getHibernateTemplate().find(sql);
		Iterator<String> itr = list.iterator();
		if (itr.hasNext()) {
			Object obj = itr.next();
            if(null==obj){
    			noStr = "1";            	
            }else{
    			noStr = obj.toString();
            }
		}
		return noStr;
	}
	
	/**
	 * 获取指定用户的全部记录
	 * @param userid
	 * @param logtype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysOperateLog> getUserOperateList(String userid){
		String sql="FROM SysOperateLog WHERE USERID=? ORDER BY indexid DESC";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(userid!=null&&!"".equals(userid)){
			if(d.HasInjectionData(userid)){
				return l;
			}
		}
		Object[] values = {userid};
		List<SysOperateLog> list =  this.getHibernateTemplate().find(sql,values);
		return list;
	}
	
	/**
	 * 获取指定用户的某项操作的全部记录
	 * @param userid
	 * @param logtype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysOperateLog> getUserSpecOperateList(String userid,String logtype){
		String sql="FROM "+SysOperateLog.DATABASE_ENTITY+" WHERE USERID=? AND LOGTYPE=? ORDER BY indexid DESC";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(userid!=null&&!"".equals(userid)){
			if(d.HasInjectionData(userid)){
				return l;
			}
		}
		if(logtype!=null&&!"".equals(logtype)){
			if(d.HasInjectionData(logtype)){
				return l;
			}
		}
		Object[] values = {userid,logtype};
		List<SysOperateLog> list =  this.getHibernateTemplate().find(sql,values);
		return list;
	}
	
	/**
	 * 获得时间段内指定用户的某项操作记录
	 * @param startdate
	 * @param enddate
	 * @param userid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysOperateLog> getPeriodList(Date startdate,Date enddate,String userid,String logtype){
		Session session=getSessionFactory().openSession();
		
		Criteria criteria=session.createCriteria(SysOperateLog.class);
		criteria.add(Restrictions.eq("userid", userid));
		criteria.add(Restrictions.eq("logtype", logtype));
		criteria.add(Restrictions.between("createdate", startdate, enddate));
		criteria.addOrder(Order.desc("indexid"));
		List<SysOperateLog> list = criteria.list();
		session.close();
		return list;
	}
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public SysOperateLog getBoData(Long id){
		SysOperateLog model = (SysOperateLog) this.getHibernateTemplate().get(SysOperateLog.class, id);
		return model;
	}
	/**
	 * 更新
	 * @param model
	 */
	public void updateBoData(SysOperateLog model){
		this.getHibernateTemplate().update(model);
	}
	/**
	 * 插入
	 * @param model
	 */
	public void addBoData(SysOperateLog model) {
		this.getHibernateTemplate().save(model);
	}
	/**
	 * 删除
	 * @param model
	 */
	public void deleteBoData(SysOperateLog model) {
		this.getHibernateTemplate().delete(model);
	}
}
