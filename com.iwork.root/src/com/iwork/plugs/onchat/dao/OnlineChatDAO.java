package com.iwork.plugs.onchat.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.plugs.onchat.model.OnlineChat;

public class OnlineChatDAO extends HibernateDaoSupport {

	/**
	 * 插入
	 * 
	 * @param model
	 */
	public void addBoData(OnlineChat model) {
		this.getHibernateTemplate().save(model);
	}

}
