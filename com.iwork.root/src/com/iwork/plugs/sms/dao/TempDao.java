package com.iwork.plugs.sms.dao;

import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.plugs.sms.bean.TempMst;



public class TempDao extends HibernateDaoSupport{
    /**
     * 临时短信,数据提交到数据库
     * @param mm
     */
	public void adddb( TempMst mm ){
		Session ss = getSessionFactory().openSession();
		Transaction tx = ss.beginTransaction();
		ss.save(mm);
		tx.commit();
	}
}
