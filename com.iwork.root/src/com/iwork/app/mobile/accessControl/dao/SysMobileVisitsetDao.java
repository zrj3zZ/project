package com.iwork.app.mobile.accessControl.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.mobile.accessControl.model.SysMobileVisitset;
import com.iwork.commons.util.DBUtilInjection;

public class SysMobileVisitsetDao extends HibernateDaoSupport {

	/**
	 * 保存
	 */
	public void save(SysMobileVisitset model) {
		this.getHibernateTemplate().save(model);
	} 

	/**
	 * 保存
	 */
	public void update(SysMobileVisitset model) {
		this.getHibernateTemplate().update(model);
	}

	/**
	 * 查找By Userid
	 */
	
	public SysMobileVisitset findModelByUserId(String userId) {
		DBUtilInjection d=new DBUtilInjection();
		
		if(userId!=null&&!"".equals(userId)){
			if(d.HasInjectionData(userId)){
				return null;
			}
		}
		String sql = "from SysMobileVisitset where userid=?";
		Object[] values = {userId};
		List<SysMobileVisitset> list = this.getHibernateTemplate().find(sql,values);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new SysMobileVisitset();
	}

	/**
	 * 查找All
	 */
	public List<SysMobileVisitset> findAllModel() {
		String sql = "from SysMobileVisitset";
		List<SysMobileVisitset> list = this.getHibernateTemplate().find(sql);
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}
}
