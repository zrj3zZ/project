package com.iwork.plugs.cms.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.plugs.cms.model.IworkCmsVideo;

public class CmsVideoDAO extends HibernateDaoSupport {

	public List<IworkCmsVideo> getVideo(String videoName) {
		String sql = "From IworkCmsVideo where VIDEOFILE = ?";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
		if ((videoName != null) && (!"".equals(videoName.trim()))) {
        	if(d.HasInjectionData(videoName)){
				return lis;
			}
         
        }
		Object[] values = {videoName};
		return this.getHibernateTemplate().find(sql,values);
	}

	public List<IworkCmsVideo> getVideoList(int beginRow, int pageSize) {
		String hql = "From IworkCmsVideo order by id desc";
		return this.getHibernateTemplate().find(hql);
	}

	public List getList() {
		String sql = "From IworkCmsVideo order by id desc";
		List list = this.getHibernateTemplate().find(sql);
		return list;
	}

	public int getTotalNumberOfPages() {
		String sql = "select count(*) as count from iwork_cms_video";
		return DBUtil.getInt(sql, "count");
	}

	public void addBoData(IworkCmsVideo model) {
		this.getHibernateTemplate().save(model);
	}

	public void deleteBoData(IworkCmsVideo model) {
		this.getHibernateTemplate().delete(model);
	}

	public void updateBoData(IworkCmsVideo model) {
		this.getHibernateTemplate().update(model);
	}

	public int getIdSequcence() {
		String sql = "select max(ID) as id from iwork_cms_video";
		return DBUtil.getInt(sql, "id") + 1;
	}

}
