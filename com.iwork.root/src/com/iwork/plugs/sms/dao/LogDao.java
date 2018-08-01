package com.iwork.plugs.sms.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.UtilDate;
import com.iwork.plugs.sms.bean.ConfigMst;
import com.iwork.plugs.sms.bean.LogMst;
import com.iwork.plugs.sms.service.LogService;

public class LogDao extends HibernateDaoSupport {
	LogService logService = new LogService();

	/**
	 * 函数说明：获得总行数 参数说明： 返回值：总行数
	 */
	public int getRows() {
		String sql = "";
		
		sql = " FROM " + LogMst.DATABASE_ENTITY;
		List list = this.getHibernateTemplate().find(sql);
		return list.size();
	}

	/**
	 * 函数说明：获得总行数 参数说明： 返回值：总行数
	 */
	public int getRows2(String sender, String keywords, String begintime,
			String endtime, List type_list) {

		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(LogMst.class);
		if (sender != null && !"".equals(sender)) {
			String sendergroup[] = sender.split(" ");
			criteria.add(Restrictions.in("userid", sendergroup ));// 单人查询

			//criteria.add(Restrictions.like("userid", "%" + sender + "%"));// 单人查询
		}

		if (type_list.size() > 0) {
			String ss[]=new String[30];
			for(int i=0;i<type_list.size();i++){
				ss[i]=type_list.get(i).toString();
			}
			criteria.add(Restrictions.in("logtype", ss));// 单人查询

		}

		if (keywords != null && !"".equals(keywords)) {
			criteria.add(Restrictions.like("value", "%" + keywords + "%"));// 单人查询
		}
		if (begintime != null && !"".equals(begintime)) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date1 = null;
			Date date2 = null;
			try {
				date1 = format.parse(begintime); // Wed sep 26 00:00:00 CST
													// 2007
				date2 = format.parse(endtime);
			} catch (ParseException e) {
				logger.error(e,e);
			}
			criteria.add(Restrictions.gt("logtime", date1));
			criteria.add(Restrictions.le("logtime", date2));
		}

		List list = criteria.list();
		return list.size();
	}

	/**
	 * 日志，数据提交到数据库
	 * 
	 * @param mm
	 */
	public void adddb(LogMst lm) {
		Session ss = getSessionFactory().openSession();
		Transaction tx = ss.beginTransaction();
		ss.save(lm);
		tx.commit();
	}

	/*
	 * 查询日志并显示(jquery)
	 */
	public List getLogj(int pageSize,int startRow,String sender, String keywords, String begintime,
			String endtime, ArrayList type_list) {
		final int pageSize1=pageSize;
		final int startRow1=startRow;
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(LogMst.class);
		if (sender != null && !"".equals(sender)) {
			String sendergroup[] = sender.split(" ");
			criteria.add(Restrictions.in("userid", sendergroup ));// 单人查询
		//criteria.add(Restrictions.like("userid", "%" + sender + "%"));// 单人查询
		}

		if (type_list.size() > 0) {
			String ss[]=new String[30];
			for(int i=0;i<type_list.size();i++){
				ss[i]=type_list.get(i).toString();
			}
			criteria.add(Restrictions.in("logtype", ss));// 单人查询

		}

		if (keywords != null && !"".equals(keywords)) {
			criteria.add(Restrictions.like("value", "%" + keywords + "%"));// 单人查询
		}
		if (begintime != null && !"".equals(begintime)) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date1 = null;
			Date date2 = null;
			try {
				date1 = format.parse(begintime); // Wed sep 26 00:00:00 CST
													// 2007
				date2 = format.parse(endtime);
			} catch (ParseException e) {
				logger.error(e,e);
			}
			criteria.add(Restrictions.gt("logtime", date1));
			criteria.add(Restrictions.le("logtime", date2));
		}
		criteria.setFirstResult(startRow1);
		criteria.setMaxResults(pageSize1);
		List list = criteria.list();
		return list;
	}

	/**
	 * 查询日志类型
	 * 
	 * @param sql
	 * @return
	 */

	public String gettypev(String sql,List<Object> params) {
		Session ss = getSessionFactory().openSession();
		String tyvalue = "";
		try {
			Query qq = ss.createQuery(sql);
			for (int i = 0; i < params.size(); i++) {
				qq.setParameter(i, params.get(i));
			}
			ArrayList list = (ArrayList) qq.list();
			for (int j = 0; j < list.size(); j++) {
				ConfigMst cm = (ConfigMst) list.get(j);
				tyvalue = cm.getValue() == null ? "" : cm.getValue();
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		return tyvalue;
	}

	/*
	 * 查询日志并显示
	 */
	public String getLog(String sender, String keywords, String begintime,
			String endtime, ArrayList type_list) {
		StringBuffer where = new StringBuffer();
		ArrayList list1 = new ArrayList();
		String html = "";
		where.append(" where 1='1'");
		if (sender != null && !"".equals(sender)) {
			where.append(" and (");
			String sendergroup[] = sender.split(" ");
			for (int i = 0; i < sendergroup.length; i++) {
				String senderg[] = sendergroup[i].split("<");
				String senderid = senderg[0];
				where.append("userid=? or ");
			}
			where.append(" 1='2')");
		}

		if (type_list.size() > 0) {
			String s = "";

			for (int i = 0; i < type_list.size(); i++) {
				if (s.length() == 0) {
					s = "logtype=?";
				} else {
					s += " or logtype=?";
				}
			}
			if (s.length() > 0) {
				where.append(" and (");
				where.append(s);
				where.append(") ");
			}
		}

		if (keywords != null && !"".equals(keywords)) {
			where.append(" and value LIKE ?");
		}
		if (begintime != null && !"".equals(begintime)) {
			where.append(" and logtime>=?");
		}
		if (endtime != null && !"".equals(endtime)) {
			where.append(" and logtime<=?");
		}
		Session ss = getSessionFactory().openSession();
		try {
			String sql = "from LogMst " + where.toString();
			final String final_sql=sql.toString();
			final String final_sender = sender;
			final ArrayList final_type_list = type_list;
			final String final_keywords = keywords;
			final String final_begintime=begintime;
			final String final_endtime=endtime;
			List list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
				public Object doInHibernate(org.hibernate.Session session)
				throws HibernateException, SQLException {
					
					Query query=session.createQuery(final_sql);
					int num = 0;
					if (final_sender != null && !"".equals(final_sender)) {
						String sendergroup[] = final_sender.split(" ");
						for (int i = 0; i < sendergroup.length; i++) {
							String senderg[] = sendergroup[i].split("<");
							String senderid = senderg[0];
							query.setString(num, senderid);num++;
						}
					}
					if (final_type_list.size() > 0) {
						for (int i = 0; i < final_type_list.size(); i++) {
							query.setString(num, final_type_list.get(i).toString());num++;
						}
					}
					if(final_keywords != null && !"".equals(final_keywords)){
						query.setString(num, final_keywords);num++;
					}
					if (null != final_begintime && !"".equals(final_begintime)){ 
						query.setDate(num,UtilDate.StringToDate(final_begintime, "yyyy-MM-dd hh:mm:ss"));num++;
					}
					if (null != final_endtime && !"".equals(final_endtime)){ 
						query.setDate(num,UtilDate.StringToDate(final_endtime, "yyyy-MM-dd hh:mm:ss"));num++;
					}
					return query.list();
				}
			});
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = new Hashtable();
				LogMst lm = (LogMst) list.get(i);
				String userid = lm.getUserid() == null ? "" : lm.getUserid();
				String value = lm.getValue() == null ? "" : lm.getValue();
				String logtime = lm.getLogtime().toLocaleString();
				if (logtime == null) {
					logtime = "";
				} else {
					logtime = logtime.substring(0, logtime.length());
				}
				String logtype = String.valueOf(lm.getLogtype()) == null ? "" : String.valueOf(lm.getLogtype());
				if (logtype.equals("")) {
					ht.put("checktype", "");
				} else {
					String sql1 = " from ConfigMst where type='LOG_TYPE' and CKey=?";
					List<Object> params = new ArrayList<Object>();
					params.add(logtype);
					String typevalue = gettypev(sql1,params);
					ht.put("checktype", typevalue);
				}
				if (logtime != null && !"".equals(logtime)) {
					ht.put("logtime", logtime);
				} else {
					ht.put("logtime", "");
				}
				
				ht.put("userid", userid);
				ht.put("value", value);
				list1.add(ht);
			}
			html = logService.getlogList(list1);

		} catch (Exception e) {
			logger.error(e,e);
		}

		return html;
	}

}
