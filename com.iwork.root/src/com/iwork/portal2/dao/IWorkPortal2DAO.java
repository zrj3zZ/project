package com.iwork.portal2.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.portal.model.IworkPortalOverall;
import com.iwork.portal.model.IworkPortalUserItem;

public class IWorkPortal2DAO extends HibernateDaoSupport{
	
	public IWorkPortal2DAO(){}
	
	/**
	 * 获得指定用户门户列表
	 * @param userid
	 * @param columnIndex
	 * @return
	 */
	public List<IworkPortalUserItem> getUserItemAllList(String userid){
		String sql = "From IworkPortalUserItem where userid= ?  order by orderIndex";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
	    if ((userid != null) && (!"".equals(userid))) {
        	if(d.HasInjectionData(userid)){
				return lis;
			}
          
        }
		Object[] values = {userid};
		return this.getHibernateTemplate().find(sql,values);  
	}
	/**
	 * 获得指定用户门户列表
	 * @param userid
	 * @param columnIndex
	 * @return
	 */
	public List<IworkPortalUserItem> getUserItemAllList(String userid ,Long groupindex){
		String sql = "From IworkPortalUserItem where userid= ? and groupindex = "+groupindex+" order by orderIndex";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
	    if ((userid != null) && (!"".equals(userid))) {
        	if(d.HasInjectionData(userid)){
				return lis;
			}
          
        }
		Object[] values = {userid};
		return this.getHibernateTemplate().find(sql,values);   
	}
	
	/**
	 * 获得portlet
	 * @param id
	 * @return 
	 */
	public IworkPortalUserItem getPortlet(Long id){
		return (IworkPortalUserItem)this.getHibernateTemplate().get(IworkPortalUserItem.class, id);
	}
	/**
	 * 保存portletlet
	 * @param model
	 * @return
	 */
	public IworkPortalUserItem savePortlet(IworkPortalUserItem model){
		this.getHibernateTemplate().save(model);
		return model;
	}
	/**
	 * 更新portletlet
	 * @param model
	 * @return
	 */
	public IworkPortalUserItem updatePortlet(IworkPortalUserItem model){
		this.getHibernateTemplate().update(model);
		return model;
	}
	
	/**
	 * 更新portletlet
	 * @param model
	 * @return
	 */
	public void removePortlet(IworkPortalUserItem model){
		this.getHibernateTemplate().delete(model);
	}
	
	
	/**
	 * 移除指定用户的portlet
	 * @param userid
	 * @return
	 */
	public boolean removeUserPortletList(String userid){
		boolean flag = false;
		Map params = new HashMap();
		params.put(1, userid);
		String sql = "delete from IWORK_PORTAL_USER_ITEM where userid = ? ";
		int i = DBUTilNew.update(sql,params);
		if(i>0){ 
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 获得门户组件列表
	 * @return
	 */
	public List<IworkPortalOverall> getOverAllList(String groupname){
		Object[] values = null;
		String sql =  "FROM IworkPortalOverall ORDER BY ID";
		if(groupname==null){
			sql = "FROM IworkPortalOverall ORDER BY ID";
			values = new Object[0];
		}else{
			sql =  "FROM IworkPortalOverall where groupName = ? ORDER BY ID";
			values = new Object[1];
			DBUtilInjection d=new DBUtilInjection();
			List lis=new ArrayList();
			
				if(d.HasInjectionData(groupname)){
					return lis;
				}
			values[0] = groupname;
	          
	        
		}
		return this.getHibernateTemplate().find(sql,values); 
	}
	/**
	 * 获得门户组件模型
	 * @return
	 */
	public IworkPortalOverall getOverAllModel(Long id){
		return (IworkPortalOverall)this.getHibernateTemplate().get(IworkPortalOverall.class, id);
	}
	
	/**
	 * 保存通用门户组件
	 * @param model
	 * @return
	 */
	public IworkPortalOverall saveOverAll(IworkPortalOverall model){
		 this.getHibernateTemplate().save(model);
		 return model;
	}
	/**
	 * 移除通用门户组件
	 * @param model
	 * @return
	 */
	public void removeOverAll(IworkPortalOverall model){
		this.getHibernateTemplate().delete(model); 
	}
	/**
	 * 保存通用门户组件
	 * @param model
	 * @return
	 */
	public IworkPortalOverall updateOverAll(IworkPortalOverall model){
		this.getHibernateTemplate().update(model);
		return model;
	}
		
}
