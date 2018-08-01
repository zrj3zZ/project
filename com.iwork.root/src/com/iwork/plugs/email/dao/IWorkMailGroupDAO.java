

package com.iwork.plugs.email.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.plugs.email.model.IworkMailGroupItem;
import com.iwork.plugs.email.model.IworkMailGrouplist;
import com.iwork.plugs.email.model.MailModel;

public  class IWorkMailGroupDAO extends HibernateDaoSupport{
	private static final String SEQUENCE_MAILBOX = "_MAILBOX";
	private Class<MailModel> entityClass;
	
	
	/**
	 * 获得分组列表
	 * @param userid
	 * @return
	 */
	public List<IworkMailGrouplist> getGroupList(String userid){
		 String sql="FROM IworkMailGrouplist where userid=?";
		 DBUtilInjection d=new DBUtilInjection();
		 List lis=new ArrayList(); 
		 if ((userid != null) && (!"".equals(userid))) {
	        	if(d.HasInjectionData(userid)){
 				return lis;
 			}
	         
	        }
		   //按id查找邮件箱中的当前记录数的所有属性值
		 Object[] values = {userid};
		    List<IworkMailGrouplist> list =  this.getHibernateTemplate().find(sql,values);
		return list;
	}
	/**
	 * 获得分组
	 * @param id
	 * @return
	 */
	public IworkMailGrouplist getGroupListModel(Long id){
		IworkMailGrouplist model =  (IworkMailGrouplist)this.getHibernateTemplate().get(IworkMailGrouplist.class, id);
		return model;
	}
	/**
	 * 获得分组下人员
	 * @param id
	 * @return
	 */
	public IworkMailGroupItem getGroupItemModel(Long id){
		IworkMailGroupItem model =  (IworkMailGroupItem)this.getHibernateTemplate().get(IworkMailGroupItem.class, id);
		return model;
	}
	/**
	 * 移除成员
	 * @param model
	 */
	public void removeGroupItemModel(IworkMailGroupItem model){
		this.getHibernateTemplate().delete(model);
	}
	/**
	 * 移除分组
	 * @param model
	 */
	public void removeGroupListModel(IworkMailGrouplist model){
		this.getHibernateTemplate().delete(model);
	}
	/**
	 * 获得分组下人员列表
	 * @param id
	 * @return
	 */
	public List<IworkMailGroupItem> getGroupItemList(Long id){
		 String sql="FROM IworkMailGroupItem where pid=?";
		   //按id查找邮件箱中的当前记录数的所有属性值
		 Object[] values = {id};
		    List<IworkMailGroupItem> list =  this.getHibernateTemplate().find(sql,values);
		return list;
	}
	/**
	 * 
	 * @param model
	 * @return
	 */
	public IworkMailGrouplist saveGroupList(IworkMailGrouplist model){
	
		this.getHibernateTemplate().save(model);
		return model;
	}
	/**
	 * 
	 * @param model
	 * @return
	 */
	public IworkMailGrouplist updateGroupList(IworkMailGrouplist model){
		this.getHibernateTemplate().update(model);
		return model;
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	public IworkMailGroupItem saveGroupItem(IworkMailGroupItem model){
		this.getHibernateTemplate().save(model);
		return model;
	}
	

}
