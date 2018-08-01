package com.iwork.plugs.sms.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.plugs.sms.bean.LogMst;

public class AddlogDao extends HibernateDaoSupport{
public void add(LogMst lm){
	this.getHibernateTemplate().save(lm);
}
}
