package com.iwork.plugs.cms.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.core.db.DBUtil;
import com.iwork.plugs.cms.model.IworkCmsAppkm;

public class CmsAppkmDAO extends HibernateDaoSupport {

	public List<IworkCmsAppkm> getList() {
		String sql = "from IworkCmsAppkm order by sequence";
		List<IworkCmsAppkm> list = this.getHibernateTemplate().find(sql);
		return list;
	}

	public int getIdSequcence() { 
		String sql = "select max(ID) as id from iwork_cms_appkm";
		return DBUtil.getInt(sql, "id") + 1;
	}
	
	public int getSequence() {
		String sql = "select max(sequence) as sequcence from iwork_cms_appkm";
		return DBUtil.getInt(sql, "sequcence") + 1;
	}

	public void addBoData(IworkCmsAppkm model) {
		this.getHibernateTemplate().save(model);
	}

	public void deleteBoData(IworkCmsAppkm model) {
		this.getHibernateTemplate().delete(model);
	}

	public void updateBoData(IworkCmsAppkm model) {
		this.getHibernateTemplate().update(model);
	}

}
