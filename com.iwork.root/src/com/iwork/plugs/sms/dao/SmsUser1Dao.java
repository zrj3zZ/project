package com.iwork.plugs.sms.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.plugs.sms.bean.LimitMst;
import com.iwork.plugs.sms.bean.LogMst;

public class SmsUser1Dao extends HibernateDaoSupport {

	public List getUserNums(String sender, String begintime, String endtime) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(LimitMst.class);
		if (sender != null && !"".equals(sender)) {
			criteria.add(Restrictions.like("userid", "%" + sender + "%"));// 单人查询
		}
		if (begintime != null && !"".equals(begintime)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = null;
			try {
				date1 = format.parse(begintime); // Wed sep 26 00:00:00 CST
			} catch (ParseException e) {
				logger.error(e,e);
			}
			criteria.add(Restrictions.gt("submittime", date1));
		}
		if (endtime != null && !"".equals(endtime)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date2 = null;
			try {
				date2 = format.parse(endtime);
			} catch (ParseException e) {
				logger.error(e,e);
			}			
			criteria.add(Restrictions.le("submittime", date2));
		}
		List list = criteria.list();
		session.close();
		return list;
	}

	public void savec(String cid, String type, String count1) {
		StringBuffer sblog = new StringBuffer();
		int limitold = this.getlimit(cid);
		int countchange = Integer.parseInt(count1);
		if (type != null && !"".equals(type)) {
			int count = 0;
			if (type.equals("add")) {
				count = limitold + countchange;
				sblog.append("短信额度增加" + countchange + "条，" + limitold + " -> "
						+ count);
			} else if (limitold < countchange) {
				count = 0;
				sblog.append("短信额度减少" + limitold + "条，" + limitold + " -> 0");
			} else {
				count = limitold - countchange;
				sblog.append("短信额度减少" + countchange + "条，" + limitold + " -> "
						+ count);
			}
			Session ss = getSessionFactory().openSession();
			Transaction tx = ss.beginTransaction();
			Query q = ss.createQuery("update LimitMst set limit=? where cid=? ");
			q.setInteger(0, count);
			q.setString(1, cid);
			int ret = q.executeUpdate();
			tx.commit();
			ss.close();
			// 加入日志
			String logvalue = "修改的用户名为" + "test" + ",原来的通道为1,1,1.设置后的通道为1,1,1;"
					+ sblog.toString() + ".";
			LogMst lm = new LogMst();
			Date date = new Date();
			lm.setLogtime(date);
			lm.setLogtype("2");// LOG_TYPE_USER_MANAGE = 2
			lm.setUserid("test");
			lm.setValue(logvalue);
			this.savelog(lm);
		}
	}

	public void savelog(LogMst lm) {
		this.getHibernateTemplate().save(lm);
	}

	public int getlimit(String cid) {
		String sql = "select limit from LimitMst where cid=?";
		Session ss = getSessionFactory().openSession();
		int rv = 0;
		try {
			Query q = ss.createQuery(sql);
			q.setString(0, cid);
			List list = q.list();
			for (int i = 0; i < list.size(); i++) {
				rv = (Integer) list.get(i);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			ss.close();
		}
		return rv;
	}
}
