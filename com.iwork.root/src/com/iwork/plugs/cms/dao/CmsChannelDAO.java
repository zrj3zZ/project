package com.iwork.plugs.cms.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.plugs.cms.model.IworkCmsChannel;

/**
 * CMS频道管理数据库操作类
 * @author WeiGuangjian
 *
 */
public class CmsChannelDAO extends HibernateDaoSupport{

	public CmsChannelDAO(){
		
	}
	
	/**
	 * 获取有效频道列表
	 * @param now
	 * @param group
	 * @return
	 */
	public List<IworkCmsChannel> getEffectList(final Date now,final String group) {	
		Session session=getSessionFactory().openSession();
		Criteria criteria=session.createCriteria(IworkCmsChannel.class);			
		criteria.add(Restrictions.le("begindate",now));
		criteria.add(Restrictions.gt("enddate", now));
		criteria.add(Restrictions.eq("status", 0L));	
		if(group!=null){
			criteria.add(Restrictions.eq("groupname", group));
		}
		List<IworkCmsChannel> list = criteria.list();
		return list;
	}
	
	/**
	 * 获取全部频道列表
	 * @return
	 */
	public List<IworkCmsChannel> getAllList(){
		String sql="FROM "+IworkCmsChannel.DATABASE_ENTITY+" ORDER BY ID";
	    List<IworkCmsChannel> list =  this.getHibernateTemplate().find(sql);
		return list;
	}
	
	/**
	 * 获取分组频道列表
	 * @param groupname
	 * @return
	 */
	public List<IworkCmsChannel> getGroupList(String groupname){
		String sql="FROM "+IworkCmsChannel.DATABASE_ENTITY+" WHERE GROUPNAME=? ORDER BY ID";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(groupname!=null&&!"".equals(groupname)){
			if(d.HasInjectionData(groupname)){
				return l;
			}
		}
		Object[] values = {groupname};
	    List<IworkCmsChannel> list =  this.getHibernateTemplate().find(sql,values);
		return list;
	}
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public IworkCmsChannel getBoData(Long id) {
		IworkCmsChannel model = (IworkCmsChannel)this.getHibernateTemplate().get(IworkCmsChannel.class,id);			
		return model;
	}
	
	/**
	 * 更新
	 * @param model
	 */
	public void updateBoData(IworkCmsChannel model) {
		this.getHibernateTemplate().update(model);
	}
	
	/**
	 * 插入
	 * @param model
	 */
	public void addBoData(IworkCmsChannel model) {
		this.getHibernateTemplate().save(model);	
	}
	
	/**
	 * 删除
	 * @param model
	 */
	public void deleteBoData(IworkCmsChannel model) {
		this.getHibernateTemplate().delete(model);	
	}
}
