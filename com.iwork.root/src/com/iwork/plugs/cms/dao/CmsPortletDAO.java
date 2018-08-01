package com.iwork.plugs.cms.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.plugs.cms.model.IworkCmsPortlet;



/**
 * CMS栏目管理数据库操作类
 * @author WeiGuangjian
 *
 */
public class CmsPortletDAO extends HibernateDaoSupport{
 
    public CmsPortletDAO(){
		
	}
    
    /**
     * 获取分组列表
     * @return
     */
    public List getGroup(){
    	String sql="SELECT groupname FROM "+IworkCmsPortlet.DATABASE_ENTITY+" GROUP BY groupname";
	    List list = this.getHibernateTemplate().find(sql);
		return list;  	
    }
    
    /**
     * 获取有效栏目列表
     * @param now
     * @param group
     * @return
     */
    public List<IworkCmsPortlet> getEffectList(final Date now,final String group) {	
		Session session=getSessionFactory().openSession();
		Criteria criteria=session.createCriteria(IworkCmsPortlet.class);			
		criteria.add(Restrictions.le("begindate",now));
		criteria.add(Restrictions.gt("enddate", now));
		criteria.add(Restrictions.eq("status", 0L));	
		criteria.add(Restrictions.eq("groupname", group));
		List<IworkCmsPortlet> list = criteria.list();
		return list;
	}
    
    /**
     * 获取资讯所属栏目列表
     * @param now
     * @param group
     * @return
     */
    public List<IworkCmsPortlet> getCmsList(final Date now,final String group) {	
		Session session=getSessionFactory().openSession();
		Criteria criteria=session.createCriteria(IworkCmsPortlet.class);			
		criteria.add(Restrictions.le("begindate",now));
		criteria.add(Restrictions.gt("enddate", now));
		criteria.add(Restrictions.eq("status", 0L));	
		criteria.add(Restrictions.eq("groupname", group));
		criteria.add(Restrictions.eq("portlettype", 0L));
		List<IworkCmsPortlet> list = criteria.list();
		return list;
	}
    
    /**
     * 获取所有栏目列表
     * @return
     */
    public List<IworkCmsPortlet> getAllList(){
		String sql="FROM "+IworkCmsPortlet.DATABASE_ENTITY+" ORDER BY ID";
	    List<IworkCmsPortlet> list =  this.getHibernateTemplate().find(sql);
		return list;
	}
    
    /**
     * 获取分组栏目列表
     * @param groupname
     * @return
     */
    public List<IworkCmsPortlet> getGroupList(String groupname){
		String sql="FROM "+IworkCmsPortlet.DATABASE_ENTITY+" where GROUPNAME= ?  ORDER BY ID";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((groupname != null) && (!"".equals(groupname))) {
        	if(d.HasInjectionData(groupname)){
				return lis;
			}
        
        }
		Object[] values = {groupname};
	    List<IworkCmsPortlet> list =  this.getHibernateTemplate().find(sql,values);
		return list;
	}
    
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public IworkCmsPortlet getBoData(Long id) {
		IworkCmsPortlet model = (IworkCmsPortlet)this.getHibernateTemplate().get(IworkCmsPortlet.class,id);			
		return model;
	}
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public IworkCmsPortlet getPortletModel(String key) {
		IworkCmsPortlet model = null;
		String sql=" FROM "+IworkCmsPortlet.DATABASE_ENTITY +" where portletkey=?";
		DBUtilInjection d=new DBUtilInjection();
	   
	    if ((key != null) && (!"".equals(key))) {
        	if(d.HasInjectionData(key)){
				return model;
			}
        
        }
		Object[] values = {key};
	    List<IworkCmsPortlet> list = this.getHibernateTemplate().find(sql,values);
		
		if(list!=null&&list.size()>0){
			model = list.get(0);
		}
		return model;
	}
	
	/**
	 * 根据id获取单条数据
	 * @param id
	 * @return
	 */
	public IworkCmsPortlet getPortleByIdtModel(String id) {
		IworkCmsPortlet model = null;
		String sql=" FROM "+IworkCmsPortlet.DATABASE_ENTITY +" where ID=?";
		DBUtilInjection d=new DBUtilInjection();
		   
	    if ((id != null) && (!"".equals(id))) {
        	if(d.HasInjectionData(id)){
				return model;
			}
        
        }
		Object[] values = {id};
	    List<IworkCmsPortlet> list = this.getHibernateTemplate().find(sql,values);
		
		if(list!=null&&list.size()>0){
			model = list.get(0);
			if(model.getBegindate()!=null && model.getEnddate()!=null){
				Date begindate=UtilDate.StringToDate(model.getBegindate().toString(),"yyyy-MM-dd");
				model.setBegindate(begindate);
				Date enddate=UtilDate.StringToDate(model.getEnddate().toString(),"yyyy-MM-dd");
				model.setEnddate(enddate);
			}
		}
		return model;
	}
	/**
	 * 更新
	 * @param model
	 */
	public void updateBoData(IworkCmsPortlet model) {
		this.getHibernateTemplate().update(model);
	}
	
	/**
	 * 插入
	 * @param model
	 */
	public void addBoData(IworkCmsPortlet model) {
		this.getHibernateTemplate().save(model);	
	}
	
	/**
	 * 删除
	 * @param model
	 */
	public void deleteBoData(IworkCmsPortlet model) {
		this.getHibernateTemplate().delete(model);	
	}
	
	/**
	 * 获取当前栏目KEY值
	 * @return
	 */
	public List getKey(){
    	String sql="SELECT portletkey FROM "+IworkCmsPortlet.DATABASE_ENTITY;
	    List list = this.getHibernateTemplate().find(sql);
		return list;  	
	}
	
	/**
	 * 获取资讯栏目列表
	 * @return
	 */
	public List<IworkCmsPortlet> getCmsChannel(){
    	String sql="FROM "+IworkCmsPortlet.DATABASE_ENTITY+" WHERE PORTLETTYPE=0";
	    List<IworkCmsPortlet> list = this.getHibernateTemplate().find(sql);
		return list;  	
	}
	
}
