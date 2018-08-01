package com.iwork.core.organization.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.cache.GroupCache;
import com.iwork.core.organization.model.OrgGroup;

/**
 * 团队管理主表操作类
 * @author WeiGuangjian
 *
 */
public class OrgGroupDAO extends HibernateDaoSupport{

	/**
	 * 获取有效团队
	 * @param now
	 * @return
	 */
	public List getOrgGroupList(final Date now) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				// TODO 自动生成方法存根
				DBUtilInjection d=new DBUtilInjection();
				List l=new ArrayList();
				if(now!=null&&!"".equals(now)){
					if(d.HasInjectionData(now.toString())){
						return l;
					}
				}
				Criteria criteria=session.createCriteria(OrgGroup.class);
				criteria.add(Restrictions.le("begindate",now));
				criteria.add(Restrictions.gt("enddate", now));
				criteria.add(Restrictions.eq("state", "开启"));
				List list = criteria.list();
				GroupCache.getInstance().putGroupList(now.toString(),list);//装载cache
				return list;
			}
		});
	}
	
	/**
	 * 获取所有团队
	 * @return
	 */
	public List getOrgGroupAllList() {	
		String sql="FROM "+OrgGroup.DATABASE_ENTITY+" ORDER BY ID";
	    List list =  this.getHibernateTemplate().find(sql);
	    GroupCache.getInstance().putList(list);//装载cache
		return list;
    }
	
	/**
	 * 获取一条数据
	 * @param id
	 * @return
	 */
	public OrgGroup getBoData(String id) {
		if(id==null)id = "0";
		//从cache中获取
		OrgGroup orggroup = GroupCache.getInstance().getModel(id);
		if(orggroup!=null){
			return orggroup;
		}else{
		     orggroup = (OrgGroup)this.getHibernateTemplate().get(OrgGroup.class,id);
			 GroupCache.getInstance().putModel(orggroup);  //装载CACHE
			 return orggroup;
		}
	}
	
    /**
     * 更新操作
     * @param model
     */
	public void updateBoData(OrgGroup model) {
		this.getHibernateTemplate().update(model);
		GroupCache.getInstance().putModel(model);  //装载CACHE
	}
	
	/**
	 * 插入操作
	 * @param model
	 */
	public void addBoData(OrgGroup model) {
		this.getHibernateTemplate().save(model);	
		GroupCache.getInstance().putModel(model); //装载CACHE
	}
		
	/**
	 * 删除操作
	 * @param model
	 */
	public void deleteBoData(OrgGroup model) {
		if(model==null)return;
		this.getHibernateTemplate().delete(model);
		GroupCache.getInstance().removeModel(model.getId());
	}
	
	/**
	 * 获取ID最大值
	 * @return
	 */
	public String getMaxID() {
		String date=UtilDate.getNowdate();
		String sql="SELECT MAX(id)+1 FROM "+OrgGroup.DATABASE_ENTITY;
		String noStr = null;
		List ll = (List) this.getHibernateTemplate().find(sql);
		Iterator itr = ll.iterator();
		if (itr.hasNext()) {
			Object noint = itr.next();
            if(noint == null){
    			noStr = "1";            	
            }else{
    			noStr = noint.toString();
            }
		}
		
		if(noStr.length()==1){
			noStr="000"+noStr;
		}else if(noStr.length()==2){
			noStr="00"+noStr;
		}else if(noStr.length()==3){
			noStr="0"+noStr;
		}else{
			noStr=noStr;
		}
		return noStr;
	}
	
}
