package com.iwork.plugs.cms.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.plugs.cms.model.IworkCmsRelation;

/**
 * CMS频道与栏目关系数据库操作类
 * @author WeiGuangjian
 *
 */
public class CmsRelationDAO extends HibernateDaoSupport{

    public CmsRelationDAO(){
		
	}
    
    /**
     * 获取频道下栏目列表
     * @param channelid
     * @return
     */
    public List<IworkCmsRelation> getCmsList(Long channelid){
		String sql="FROM "+IworkCmsRelation.DATABASE_ENTITY+" WHERE CHANNELID="+channelid+" ORDER BY ID";
	    List<IworkCmsRelation> list =  this.getHibernateTemplate().find(sql);
		return list;
	}
    /**
     * 获取频道下栏目列表
     * @param channelid
     * @return
     */
    public List<IworkCmsRelation> getCmsRunList(Long channelid){
    	String sql="FROM "+IworkCmsRelation.DATABASE_ENTITY+" WHERE CHANNELID="+channelid+" ORDER BY ID";
    	List<IworkCmsRelation> list =  this.getHibernateTemplate().find(sql);
    	return list;
    }
    
    /**
	 * 插入
	 * @param model
	 */
	public void addBoData(IworkCmsRelation model) {
		this.getHibernateTemplate().save(model);	
	}
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public IworkCmsRelation getBoData(Long id) {
		IworkCmsRelation model = (IworkCmsRelation)this.getHibernateTemplate().get(IworkCmsRelation.class,id);			
		return model;
	}
	
	/**
	 * 删除
	 * @param model
	 */
	public void deleteBoData(IworkCmsRelation model) {
		this.getHibernateTemplate().delete(model);	
	}
}
