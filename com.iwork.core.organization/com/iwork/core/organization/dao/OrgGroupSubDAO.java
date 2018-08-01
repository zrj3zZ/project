package com.iwork.core.organization.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.cache.GroupSubCache;
import com.iwork.core.organization.model.OrgGroupSub;

/**
 * 团队管理子表操作类
 * @author WeiGuangjian
 *
 */
public class OrgGroupSubDAO extends HibernateDaoSupport{

	/**
	 * 获取某团队信息
	 * @param groupid
	 * @return
	 */
	public List getOrgGroupSubList(String groupid) {	
		String sql="FROM "+OrgGroupSub.DATABASE_ENTITY+" WHERE groupId=? ORDER BY ID";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(groupid!=null&&!"".equals(groupid)){
			if(d.HasInjectionData(groupid)){
				return l;
			}
		}
		Object[] values = {groupid};
	    List list =  this.getHibernateTemplate().find(sql,values);
	    GroupSubCache.getInstance().putGroupSubList(groupid,list);//装载cache
		return list;
    }
	
	/**
	 * 获取一条数据
	 * @param id
	 * @return
	 */
	public OrgGroupSub getBoData(String id) {
		if(id==null)id = "0";
		//从cache中获取
		OrgGroupSub orggroupsub = GroupSubCache.getInstance().getModel(id);
		if(orggroupsub!=null){
			return orggroupsub;
		}else{
		    orggroupsub = (OrgGroupSub)this.getHibernateTemplate().get(OrgGroupSub.class,id);
			GroupSubCache.getInstance().putModel(orggroupsub);  //装载CACHE
			 return orggroupsub;
		}
	}
	
	/**
	 * 插入操作
	 * @param model
	 */
	public void addBoData(OrgGroupSub model) {
		this.getHibernateTemplate().save(model);	
		GroupSubCache.getInstance().putModel(model);
	}
	
	/**
	 * 删除操作
	 * @param model
	 */
	public void deleteBoData(OrgGroupSub model) {
		if(model==null)return;
		this.getHibernateTemplate().delete(model);
		GroupSubCache.getInstance().removeModel(model.getId());
	}
	
	/**
	 * 获取ID最大值
	 * @return
	 */
	public String getMaxID() {
		String date=UtilDate.getNowdate();
		String sql="SELECT MAX(id)+1 FROM "+OrgGroupSub.DATABASE_ENTITY;
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
