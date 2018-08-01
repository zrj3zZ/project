package com.iwork.plugs.rss.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.rss.model.BdInfRssdyjlb;

public class IworkPlugsRssDao extends HibernateDaoSupport {

	public IworkPlugsRssDao() {

	}

	/**
	 * 获得页面信息
	 * 
	 * @param uid
	 * @return
	 */
	public List<BdInfRssdyjlb> getIndexList(String uid) {
		String sql = "From BdInfRssdyjlb where createuser=? order by id";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
        if ((uid != null) && (!"".equals(uid))) {
        	if(d.HasInjectionData(uid)){
				return lis;
			}
         
        }
		Object[] values = {uid};
		return this.getHibernateTemplate().find(sql,values);
	}

	/**
	 * 获得用户配置信息
	 * 
	 * @param uid
	 * @return
	 */
	public String getUserProfile(String uid) {
		String sql = "select VALUE from SYS_PERSON_CONFIG where USERID = ? and TYPE = 'RSS'";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((uid != null) && (!"".equals(uid))) {
        	if(d.HasInjectionData(uid)){
				return "";
			}
          
        }
		Map params = new HashMap();
		params.put(1, uid);
		return com.iwork.commons.util.DBUtil.getDataStr("VALUE", sql, params);
	}

	public void updateUserProfile(String uid, String value) {
		String sql = "update SYS_PERSON_CONFIG set VALUE = ? where USERID = ? and TYPE = 'RSS'";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((uid != null) && (!"".equals(uid.trim()))) {
        	if(d.HasInjectionData(uid)){
				return;
			}
         
        }
	    if ((value != null) && (!"".equals(value.trim()))) {
        	if(d.HasInjectionData(value)){
				return;
			}
         
        }
		Map params = new HashMap();
		params.put(1, value);
		params.put(2, uid);
		com.iwork.commons.util.DBUtil.update(sql, params);
	}

	public int getIdSequcence() {
		String sql = "select max(ID) as id from BD_INF_RSSDYJLB";
		return DBUtil.getInt(sql, "id") + 1;
	}

	/**
	 * 增加RSS订阅
	 * 
	 * @param model
	 */
	public void addRssSubscription(BdInfRssdyjlb model) {
		this.getHibernateTemplate().save(model);
	}

	/**
	 * 删除RSS订阅
	 * 
	 * @param id
	 * @return
	 */
	public int deleteRssSubscription(String id) {
		String uid = UserContextUtil.getInstance().getCurrentUserId();
		String sql = "delete from BD_INF_RSSDYJLB where ID = ? and CREATEUSER = ?";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((id != null) && (!"".equals(id))) {
        	if(d.HasInjectionData(id)){
				return 0;
			}
         
        }
		Map params = new HashMap();
		params.put(1, Integer.parseInt(id));
		params.put(2, uid);
		return com.iwork.commons.util.DBUtil.update(sql, params);
	}

	/**
	 * 重置RSS订阅
	 * 
	 * @return
	 */
	public int resetRssSubscription() {
		String uid = UserContextUtil.getInstance().getCurrentUserId();
		String sql = "delete from BD_INF_RSSDYJLB where CREATEUSER = ?";
		Map params = new HashMap();
		params.put(1, uid);
		return com.iwork.commons.util.DBUtil.update(sql, params);
	}

}
