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
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.iwork.plugs.sms.bean.ChannelMst;
import com.iwork.plugs.sms.bean.LogMst;
import com.iwork.plugs.sms.bean.MsgMst;
import com.iwork.plugs.sms.bean.SpMst;
import com.iwork.plugs.sms.service.MsgmanageService;

public class MsgmanageDao extends HibernateDaoSupport {
	MsgmanageService msgmanageService = new MsgmanageService();

	/**
	 * 函数说明：获得总行数 参数说明： 返回值：总行数
	 */
	public int getRows(String userid) {
		String sql = "";
		sql = " FROM " + MsgMst.DATABASE_ENTITY;
		List list = this.getHibernateTemplate().find(sql);
		return list.size();
	}
	/**
	 * 函数说明：获得总行数 参数说明： 返回值：总行数
	 */
	public int getRows2(String sender,String supplier,String chanel,
			String mobilenum,String keywords,String status, String begintime,String endtime, String batch) {

		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(MsgMst.class);
		if (sender != null && !"".equals(sender)) {			
			criteria.add(Restrictions.like("userid", "%"+sender+"%" ));
		}

		if (supplier != null && !"".equals(supplier)) {
			criteria.add(Restrictions.like("msgsp", "%" + supplier + "%"));
		}
		if (batch != null && !"".equals(batch)) {
			criteria.add(Restrictions.like("batchnum", "%" + batch + "%"));
		}
		if (chanel != null && !"".equals(chanel)) {
			criteria.add(Restrictions.eq("channelid",  supplier ));
		}
		if (mobilenum != null && !"".equals(mobilenum)) {
			criteria.add(Restrictions.like("mobilenum", "%" + mobilenum + "%"));
		}
		if (keywords != null && !"".equals(keywords)) {
			criteria.add(Restrictions.like("content", "%" + keywords + "%"));
		}
		if (status != null && !"".equals(status)) {
			criteria.add(Restrictions.eq("status", Integer.parseInt(status)));
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
			criteria.add(Restrictions.gt("submittime", date1));
			criteria.add(Restrictions.le("submittime", date2));
		}
		List list = criteria.list();
			session.close();
		return list.size();
	}
	/**
	 * 写入日志
	 * 
	 * @param lm
	 */
	public void savelog(LogMst lm) {
		this.getHibernateTemplate().save(lm);

	}

	/**
	 * 数据库里取通道
	 * 
	 * @return
	 */
	public String getchannelall() {
		ArrayList list = new ArrayList();
		String html = "";
		// String sql = "select c.CHANNELID,s.NAME from BO_MSG_CHANNEL
		// c,BO_MSG_SP s where c.channelid=s.spid";
		Session ss = getSessionFactory().openSession();
		try {
			Query qq = ss.createQuery("from ChannelMst");
			ArrayList list1 = (ArrayList) qq.list();
			for (int i = 0; i < list1.size(); i++) {
				Hashtable ht = new Hashtable();
				ChannelMst cm = (ChannelMst) list1.get(i);
				String channelid = String.valueOf(cm.getChannelid());// long转换为String
				String description = cm.getDescription();
				String idname = channelid + "-" + description;
				ht.put("CHANNELID", channelid);
				ht.put("IDNAME", idname);
				list.add(ht);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		html = msgmanageService.getsplist(list);
		return html;
	}

	/**
	 * 修改通道从数据库里取得
	 * 
	 * @return
	 */

	public String getchannelall2() {
		ArrayList list = new ArrayList();
		String html = "";
		// String sql = "select c.CHANNELID,s.NAME from BO_MSG_CHANNEL
		// c,BO_MSG_SP s where c.channelid=s.spid";
		Session ss = getSessionFactory().openSession();
		try {
			Query qq = ss.createQuery("from ChannelMst");
			ArrayList list1 = (ArrayList) qq.list();
			for (int i = 0; i < list1.size(); i++) {
				Hashtable ht = new Hashtable();
				ChannelMst cm = (ChannelMst) list1.get(i);
				String channelid = String.valueOf(cm.getChannelid());// long转换为String
				String description = cm.getDescription();
				String idname = channelid + "-" + description;
				ht.put("CID", channelid);
				ht.put("IDNAME", idname);
				list.add(ht);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		html = msgmanageService.getsplist2(list);
		return html;
	}

	/**
	 * 数据库里取供应商
	 * 
	 * @return
	 */
	public String getspall() {
		ArrayList list = new ArrayList();
		String html = "";
		Session ss = getSessionFactory().openSession();
		try {
			Query qq = ss.createQuery("from SpMst");
			ArrayList list1 = (ArrayList) qq.list();
			for (int i = 0; i < list1.size(); i++) {
				Hashtable hs = new Hashtable();
				SpMst sm = (SpMst) list1.get(i);
				String dbspid = String.valueOf(sm.getSpid());
				String dbspname = sm.getName() == null ? "" : sm.getName();
				hs.put("SPID", dbspid);
				hs.put("NAME", dbspname);
				list.add(hs);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		html = msgmanageService.getspnamelist(list);
		return html.toString();
	}

	/**
	 * 短信管理查询
	 * 
	 * @param sender
	 * @param supplier
	 * @param chanel
	 * @param mobilenum
	 * @param keywords
	 * @param status
	 * @param begintime
	 * @param endtime
	 * @param batch
	 * @return
	 */
	public String getQuery(int pageSize, int startRow, String sender,
			String supplier, String chanel, String mobilenum, String keywords,
			String status, String begintime, String endtime, String batch) {
		final int pageSize1=pageSize;
		final int startRow1=startRow;
		ArrayList list=new ArrayList();
		String html="";
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(MsgMst.class);
		if (sender != null && !"".equals(sender)) {			
			criteria.add(Restrictions.like("userid", "%"+sender+"%" ));
		}

		if (supplier != null && !"".equals(supplier)) {
			criteria.add(Restrictions.like("msgsp", "%" + supplier + "%"));
		}
		if (batch != null && !"".equals(batch)) {
			criteria.add(Restrictions.like("batchnum", "%" + batch + "%"));
		}
		if (chanel != null && !"".equals(chanel)) {
			criteria.add(Restrictions.eq("channelid",  supplier ));
		}
		if (mobilenum != null && !"".equals(mobilenum)) {
			criteria.add(Restrictions.like("mobilenum", "%" + mobilenum + "%"));// 单人查询
		}
		if (keywords != null && !"".equals(keywords)) {
			criteria.add(Restrictions.like("content", "%" + keywords + "%"));// 单人查询
		}
		if (status != null && !"".equals(status)) {
			criteria.add(Restrictions.eq("status", Integer.parseInt(status)));// 单人查询
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
			criteria.add(Restrictions.gt("submittime", date1));
			criteria.add(Restrictions.le("submittime", date2));
			
		}
		criteria.setFirstResult(startRow1);
		criteria.setMaxResults(pageSize1);
		List list1 = criteria.list();
		
		
		
			for (int i = 0; i < list1.size(); i++) {
				Hashtable ht = new Hashtable();
				MsgMst mm = (MsgMst) list1.get(i);
				int id = mm.getCid();
				String dbbatchnum = mm.getBatchnum() == null ? "" : mm
						.getBatchnum();
				String dbsubmittime = mm.getSubmittime().toString();
				String dbsendtime = mm.getSendtime().toString();
				String dbsender = mm.getUserid() == null ? "" : mm.getUserid();
				String dbmobilenum = mm.getMobilenum() == null ? "" : mm
						.getMobilenum();
				String dbcontent = mm.getContent() == null ? "" : mm
						.getContent();
				String dbchanel = String.valueOf(mm.getChannelid()) == null ? ""
						: String.valueOf(mm.getChannelid());
				String dbsupplier = String.valueOf(mm.getMsgsp()) == null ? ""
						: String.valueOf(mm.getMsgsp());
				String dbstatus = String.valueOf(mm.getStatus()) == null ? ""
						: String.valueOf(mm.getStatus());
				String dbpathname = mm.getPathname() == null ? "" : mm
						.getPathname();
				ht.put("ID", id);
				if (dbpathname != null && !"".equals(dbpathname)
						&& dbpathname.length() > 25) {
					String dbpathname1 = dbpathname.substring(0, 24);
					String dbpathname2 = dbpathname.substring(24, dbpathname
							.length());
					ht.put("PATHNAME1", dbpathname1);
					ht.put("PATHNAME2", dbpathname2);
				} else {
					ht.put("PATHNAME1", dbpathname);
					ht.put("PATHNAME2", "");
				}

				if (dbbatchnum != null && !"".equals(dbbatchnum)) {
					String batchnums[] = dbbatchnum.split("-");
					String batchnum1 = batchnums[0] + "-";
					String batchnum2 = dbbatchnum.substring(batchnums[0]
							.length() + 1, dbbatchnum.length());
					ht.put("BATCHNUM1", batchnum1);
					ht.put("BATCHNUM2", batchnum2);
				} else {
					ht.put("BATCHNUM1", "");
					ht.put("BATCHNUM2", "");
				}
				if (dbsubmittime != null && !"".equals(dbsubmittime)) {
					ht.put("SUBMITTIME1", dbsubmittime.substring(0, 10));
					ht.put("SUBMITTIME2", dbsubmittime.substring(11, 19));
				} else {
					ht.put("SUBMITTIME1", "");
					ht.put("SUBMITTIME2", "");
				}
				if (dbsendtime != null && !"".equals(dbsendtime)) {
					ht.put("SENDTIME1", dbsendtime.substring(0, 10));
					ht.put("SENDTIME2", dbsendtime.substring(11, 19));
				} else {
					ht.put("SENDTIME1", "");
					ht.put("SENDTIME2", "");
				}

				ht.put("USER1", dbsender);
				ht.put("DEPARTMENT", "");

				ht.put("MOBILENUM", dbmobilenum);
				ht.put("CONTENT", dbcontent.replace("\n", "<br>"));

				String sqlchanelid = "select description from ChannelMst where channelid=?";
				List<Object> params = new ArrayList<Object>();
				params.add(dbchanel);
				String channelshow = dbchanel + "-"
						+ this.getcahennelvlue(sqlchanelid,params);
				ht.put("CHANNELID", channelshow);

				String Sqlsupplier = "select name from SpMst where spid=?";
				params.clear();
				params.add(dbsupplier);
				String msgspname = this.getcahennelvlue(Sqlsupplier,params);
				ht.put("MSGSP", msgspname);
				String sqlstatus = "select value from ConfigMst where type='MSG_STATUS' and key=?";
				params.clear();
				params.add(dbstatus);
				String statusshow = this.getcahennelvlue(sqlstatus,params);
				ht.put("STATUS", statusshow);
				list.add(ht);
			}
			session.close();
			html = msgmanageService.getList(list);
		return html;
	}
	/**
	 * 函数说明：查询列表(分页)
	 * 返回值：
	 */
	public List getMsgListDatas(String sql,int pageSize, int startRow) {
		final int pageSize1=pageSize;
		final int startRow1=startRow;

		final String sql1=sql;
		return this.getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				// TODO 自动生成方法存根
				Query query=session.createQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				return query.list();
			}
		});
	}
	/**
	 * 根据sql语句返回一个值 根据id查通道的value等
	 * 
	 * @param sql
	 * @return
	 */
	public String getcahennelvlue(String sql,List<Object> params) {
		Session ss = getSessionFactory().openSession();
		String valuer = "";
		try {
			Query qq = ss.createQuery(sql);
			for (int i = 0; i < params.size(); i++) {
				qq.setParameter(i, params.get(i));
			}
			List list = qq.list();
			for (int i = 0; i < list.size(); i++) {
				valuer = (String) list.get(i);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return valuer;
	}

	/**
	 * 查询修改短信通道或状态前的短信通道和状态 MsgMst
	 * 
	 * @param oldsql
	 * @return
	 */
	public Hashtable getoldmsg(String oldsql,List<Object> params) {
		Hashtable ht = new Hashtable();
		Session ss = getSessionFactory().openSession();
		try {
			Query qq = ss.createQuery(oldsql);
			for (int i = 0; i < params.size(); i++) {
				qq.setParameter(i, params.get(i));
			}
			ArrayList list = (ArrayList) qq.list();
			for (int i = 0; i < list.size(); i++) {
				MsgMst mm = (MsgMst) list.get(i);
				// String userid=mm.getUserid()==null?"":mm.getUserid();
				String oldstatus = String.valueOf(mm.getStatus()) == null ? ""
						: String.valueOf(mm.getStatus());
				String oldchanel = String.valueOf(mm.getChannelid()) == null ? ""
						: String.valueOf(mm.getChannelid());
				ht.put("oldstatus", oldstatus);
				ht.put("oldchanel", oldchanel);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}

		return ht;
	}

	/**
	 * 执行修改
	 * 
	 * @param sql
	 */
	public void update(String sql,List<Object> params) {
		Session ss = getSessionFactory().openSession();
		Transaction tx = ss.beginTransaction();
		Query qq = ss.createQuery(sql);
		for (int i = 0; i < params.size(); i++) {
			qq.setParameter(i, params.get(i));
		}
		int ret = qq.executeUpdate();
		tx.commit();
		ss.close();
	}

	/**
	 * 重发短信的时候要重新查询出内容等
	 * 
	 * @param sql
	 * @return
	 */
	public Hashtable getmsgagain(String sql,List<Object> params) {
		Hashtable ht = new Hashtable();
		Session ss = getSessionFactory().openSession();
		try {
			Query qq = ss.createQuery(sql);
			for (int i = 0; i < params.size(); i++) {
				qq.setParameter(i, params.get(i));
			}
			ArrayList list = (ArrayList) qq.list();
			for (int i = 0; i < list.size(); i++) {
				MsgMst mm = (MsgMst) list.get(i);
				// String userid=mm.getUserid()==null?"":mm.getUserid();
				String contentagain = mm.getContent() == null ? "" : mm
						.getContent();
				String mobilenumagain = mm.getMobilenum() == null ? "" : mm
						.getMobilenum();
				String useridagain = mm.getUserid() == null ? "" : mm
						.getUserid();
				String batchnum = mm.getBatchnum() == null ? "" : mm
						.getBatchnum();
				ht.put("contentagain", contentagain);
				ht.put("mobilenumagain", mobilenumagain);
				ht.put("useridagain", useridagain);
				ht.put("batchnum", batchnum);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}

		return ht;
	}

	/**
	 * 得到用户限额
	 * 
	 * @param sql
	 * @return
	 */
	public String getlimit(String sql) {
		Session ss = getSessionFactory().openSession();
		String limit = "";
		try {
			Query qq = ss.createQuery(sql);
			List list = qq.list();
			for (int i = 0; i < list.size(); i++) {
				limit = (String) list.get(i);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return limit;
	}

	/**
	 * 短信 写入通道
	 * 
	 * @param sql
	 */
	public void doupdate1(String sql,List<Object> params) {
		Session ss = getSessionFactory().openSession();
		Transaction tx = ss.beginTransaction();
		Query qq = ss.createQuery(sql);
		for (int i = 0; i < params.size(); i++) {
			qq.setParameter(i, params.get(i));
		}
		int ret = qq.executeUpdate();
		tx.commit();
		ss.close();
	}

	/**
	 * 查询用户通道
	 * 
	 * @param sql
	 * @return
	 */
	public int qchannel(String sql,List<Object> params) {
		Session ss = getSessionFactory().openSession();
		int rv = 0;
		try {
			Query q = ss.createQuery(sql);
			for (int i = 0; i < params.size(); i++) {
				q.setParameter(i, params.get(i));
			}
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

	/**
	 * 查询用户通道
	 * 
	 * @param sql
	 * @return
	 */
	public String qchannel2(String sql) {
		Session ss = getSessionFactory().openSession();
		String rv = "";
		try {
			Query q = ss.createQuery(sql);
			List list = q.list();
			for (int i = 0; i < list.size(); i++) {
				rv = (String) list.get(i);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			ss.close();
		}
		return rv;
	}

	public Hashtable qmsg1(String sql,String batchnum,String mobileNum) {
		Hashtable ht = new Hashtable();
		Session ss = getSessionFactory().openSession();
		try {
			Query q = ss.createQuery(sql);
			q.setString(0, batchnum);
			q.setString(1, mobileNum);
			ArrayList list = (ArrayList) q.list();
			for (int i = 0; i < list.size(); i++) {
				MsgMst mm = (MsgMst) list.get(i);
				String content = mm.getContent() == null ? "" : mm.getContent();
				String price = Double.toString(mm.getPrice());
				String billinglen = String.valueOf(mm.getBillinglen());
				ht.put("content", content);
				ht.put("price", price);
				ht.put("billinglen", billinglen);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return ht;
	}

	/**
	 * 短信状态修改成待发后重发的时候取此条短信信息
	 * 
	 * @param sql
	 * @return select channelid,msgsp,sign ,billinglen ,price ,submittime
	 */
	public Hashtable qoldmsg(String sql,String batchnum,String mobilenum,String content) {
		Hashtable ht = new Hashtable();
		Session ss = getSessionFactory().openSession();
		try {
			Query q = ss.createQuery(sql);
			q.setString(0, batchnum);
			q.setString(1, mobilenum);
			q.setString(2, content);
			ArrayList list = (ArrayList) q.list();
			for (int i = 0; i < list.size(); i++) {
				MsgMst mm = (MsgMst) list.get(i);
				String channelid = String.valueOf(mm.getChannelid());
				String msgsp = String.valueOf(mm.getMsgsp());
				String billinglen = String.valueOf(mm.getBillinglen());
				String price = Double.toString(mm.getPrice());
				String submit = mm.getSubmittime().toString();
				String sign = mm.getSign();
				ht.put("channelid", channelid);
				ht.put("price", price);
				ht.put("billinglen", billinglen);
				ht.put("msgsp", msgsp);
				ht.put("submit", submit);
				ht.put("sign", sign);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}

		return ht;
	}

	public void savemsg(MsgMst mm) {
		// Session ss = getSessionFactory().openSession();
		// Transaction tx = ss.beginTransaction();
		// ss.save(pm);
		// tx.commit();
		// ss.close();
		this.getHibernateTemplate().save(mm);
	}

	/**
	 * 分页
	 * 
	 * @param sql
	 * @return
	 */
	public int getRownum(String sql) {
		Session ss = getSessionFactory().openSession();
		int rv = 0;
		try {
			Query q = ss.createQuery(sql);
			List list = q.list();
			rv = list.size();
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			ss.close();
		}
		return rv;

	}
}
