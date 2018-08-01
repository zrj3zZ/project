package com.iwork.plugs.cms.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.plugs.cms.model.IworkCmsComment;

public class CmsCommentDao extends HibernateDaoSupport {

	/**
	 * 获取当前新闻的全部评论列表
	 * 
	 * @return
	 */
	public List<IworkCmsComment> getAllList(String infoid) {
		String sql = "FROM " + IworkCmsComment.DATABASE_ENTITY + " where contentid=? order by talktime desc";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(infoid!=null&&!"".equals(infoid)){
			if(d.HasInjectionData(infoid)){
				return l;
			}
		}
		Object[] values = {infoid};
		List<IworkCmsComment> list = this.getHibernateTemplate().find(sql,values);
		return list;
	}

	/**
	 * 更新
	 * 
	 * @param model
	 */
	public void updateBoData(IworkCmsComment model) {
		this.getHibernateTemplate().update(model);
	}

	/**
	 * 插入
	 * 
	 * @param model
	 */
	public void addBoData(IworkCmsComment model) {
		this.getHibernateTemplate().save(model);
	}

	/**
	 * 删除
	 * 
	 * @param model
	 */
	public void deleteBoData(IworkCmsComment model) {
		this.getHibernateTemplate().delete(model);
	}

	/**
	 * 查询
	 * 
	 * @param id
	 * @return
	 */
	public IworkCmsComment getOneDataById(Long id) {

		String sql = "FROM " + IworkCmsComment.DATABASE_ENTITY + " where id=? order by id desc";
		Object[] values = {id};
		List<IworkCmsComment> list = this.getHibernateTemplate().find(sql,values);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
