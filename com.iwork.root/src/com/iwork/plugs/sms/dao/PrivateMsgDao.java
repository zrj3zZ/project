package com.iwork.plugs.sms.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.plugs.sms.bean.ConfigMst;
import com.iwork.plugs.sms.bean.LimitMst;
import com.iwork.plugs.sms.bean.MsgMst;
import com.iwork.plugs.sms.service.PrivateMsgService;

public class PrivateMsgDao extends HibernateDaoSupport{
	PrivateMsgService privateMsgService=new PrivateMsgService();
	
	
	/**
	 * 函数说明：获得总行数 参数说明： 返回值：总行数
	 */
	public int getRows() {
		String sql = "";
		sql = " FROM " + MsgMst.DATABASE_ENTITY;
		List list = this.getHibernateTemplate().find(sql);
		return list.size();
	}
	/**
	 * 函数说明：获得总行数 参数说明： 返回值：总行数
	 */
	public int getRows2(String mobilenum,String keywords,String status,String begintime,String endtime,String batch) {
		Session session=getSessionFactory().openSession();
	      Criteria criteria=session.createCriteria(MsgMst.class);
	      if (mobilenum != null && !"".equals(mobilenum)) {
				criteria.add(Restrictions.like("mobilenum","%"+mobilenum+"%"));
	      }
	      if (keywords != null && !"".equals(keywords)) {
				criteria.add(Restrictions.like("content","%"+keywords+"%"));
	      }
	      if (status != null && !"".equals(status)) {
	    	  int statusi=Integer.parseInt(status);
				criteria.add(Restrictions.eq("status",statusi));
	      }
	      if (begintime != null && !"".equals(begintime)) {
	    	  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      
	    	              Date date1 = null;
	    	              Date date2=null;
	    	             try {    
	    	               date1 = format.parse(begintime);  // Wed sep 26 00:00:00 CST 2007    
	    	               date2 = format.parse(endtime);
	    	             } catch (ParseException e) {    
	    	                 logger.error(e,e);    
	    	           }    
			criteria.add(Restrictions.gt("submittime",date1));
			criteria.add(Restrictions.le("submittime",date2));
	      }
	      if (batch != null && !"".equals(batch)) {
				criteria.add(Restrictions.like("batchnum","%"+batch+"%"));
		      }
			List list = criteria.list();
			session.close();
		return list.size();
	}
	/**
	 * 查询短信列表（jquery）
	 * @param mobilenum
	 * @param keywords
	 * @param status
	 * @param begintime
	 * @param endtime
	 * @param batch
	 * @return
	 */
	public List getMsgNums(int pageSize,int startRow,String mobilenum,String keywords,String status,String begintime,String endtime,String batch){
		final int pageSize1=pageSize;
		final int startRow1=startRow;
		Session session=getSessionFactory().openSession();
	      Criteria criteria=session.createCriteria(MsgMst.class);
	      if (mobilenum != null && !"".equals(mobilenum)) {
				criteria.add(Restrictions.like("mobilenum","%"+mobilenum+"%"));
	      }
	      if (keywords != null && !"".equals(keywords)) {
				criteria.add(Restrictions.like("content","%"+keywords+"%"));
	      }
	      if (status != null && !"".equals(status)) {
	    	  int statusi=Integer.parseInt(status);
				criteria.add(Restrictions.eq("status",statusi));
	      }
	      if (begintime != null && !"".equals(begintime)) {
	    	  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 	    	              Date date1 = null;
	    	              Date date2=null;
	    	             try {    
	    	               date1 = format.parse(begintime);  // Wed sep 26 00:00:00 CST 2007    
	    	               date2 = format.parse(endtime);
	    	             } catch (ParseException e) {    
	    	                 logger.error(e,e);    
	    	           }    
			criteria.add(Restrictions.gt("submittime",date1));
			criteria.add(Restrictions.le("submittime",date2));
	      }
	      if (batch != null && !"".equals(batch)) {
				criteria.add(Restrictions.like("batchnum","%"+batch+"%"));
		      }
	        criteria.setFirstResult(startRow1);//分页
			criteria.setMaxResults(pageSize1);
			List list = criteria.list();
			session.close();
			return list;
	}
	/**
	 * 查询短信列表
	 * @param mobilenum
	 * @param keywords
	 * @param status
	 * @param begintime
	 * @param endtime
	 * @param batch
	 * @return
	 */
	public String queryPMsg(int pageSize,int startRow,String mobilenum,String keywords,String status,String begintime,String endtime,String batch){
		final int pageSize1=pageSize;
		final int startRow1=startRow;
		ArrayList listhtml = new ArrayList();
			String html="";
			
		      Session session=getSessionFactory().openSession();
		      Criteria criteria=session.createCriteria(MsgMst.class);
		      if (mobilenum != null && !"".equals(mobilenum)) {
					criteria.add(Restrictions.like("mobilenum",mobilenum));
		      }
		      if (keywords != null && !"".equals(keywords)) {
					criteria.add(Restrictions.like("content",keywords));
		      }
		      if (status != null && !"".equals(status)) {
					criteria.add(Restrictions.eq("status",status));
		      }
		      if (begintime != null && !"".equals(begintime)) {
		    	  DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh24:mi:ss");         
		    	              Date date1 = null;
		    	              Date date2=null;
		    	             try {    
		    	               date1 = format.parse(begintime);  // Wed sep 26 00:00:00 CST 2007    
		    	               date2 = format.parse(endtime);
		    	             } catch (ParseException e) {    
		    	                 logger.error(e,e);    
		    	           }    
				criteria.add(Restrictions.gt("submittime",date1));
				criteria.add(Restrictions.le("submittime",date2));
		      }
		      if (batch != null && !"".equals(batch)) {
					criteria.add(Restrictions.like("batchnum",batch));
			      }
		        criteria.setFirstResult(startRow1);//分页
				criteria.setMaxResults(pageSize1);
				List list = criteria.list();
				session.close();
		    	  for(int i=0;i<list.size();i++){
		    		  MsgMst mm=(MsgMst)list.get(i);
		    		  Hashtable ht = new Hashtable();
		    		  String batchnumdb=mm.getBatchnum()==null?"":mm.getBatchnum();
		    		  String statusdb=String.valueOf(mm.getStatus());
		    		  String submittimedb=mm.getSubmittime().toString();
		    		  String sendtimedb=mm.getSendtime().toString();
		    		  String mobilenumdb=mm.getMobilenum()==null?"":mm.getMobilenum();
		    		  String contentdb=mm.getContent()==null?"":mm.getContent();
		    		  if (statusdb.equals("")) {
		    	          ht.put("STATUS", "");
		    	        } else {// 从系统参数数据库表里取相应的状态
		    	          String statusshow = this.getStatusdb(statusdb);
		    	          ht.put("STATUS", statusshow);
		    	        }
		    	        if (submittimedb != null && !"".equals(submittimedb)) {
		    	          String submittime1 = submittimedb.substring(0, 10);
		    	          String submittime2 = submittimedb.substring(11, 19);
		    	          ht.put("SUBMITTIME1", submittime1);
		    	          ht.put("SUBMITTIME2", submittime2);
		    	        } else {
		    	          ht.put("SUBMITTIME1", "");
		    	          ht.put("SUBMITTIME2", "");
		    	        }
		    	        if (sendtimedb != null && !"".equals(sendtimedb)) {
		    	          String sendtime1 = sendtimedb.substring(0, 10);
		    	          String sendtime2 = sendtimedb.substring(11, 19);
		    	          ht.put("SENDTIME1", sendtime1);
		    	          ht.put("SENDTIME2", sendtime2);
		    	        } else {
		    	          ht.put("SENDTIME1", "");
		    	          ht.put("SENDTIME2", "");
		    	        }
		    	        ht.put("MOBILENUM", mobilenumdb);
		    	        ht.put("CONTENT", contentdb.replace("\n","<br>"));
		    	        if (batchnumdb != null && !"".equals(batchnumdb)) {
		    	          String batchnums[] = batchnumdb.split("-");
		    	          String batchnum1 = batchnums[0] + "-";
		    	          String batchnum2 = batchnumdb.substring(batchnums[0].length() + 1,
		    	              batchnumdb.length());
		    	          ht.put("BATCHNUM1", batchnum1);
		    	          ht.put("BATCHNUM2", batchnum2);
		    	        } else {
		    	          ht.put("BATCHNUM1", "");
		    	          ht.put("BATCHNUM2", "");
		    	        }
		    	        listhtml.add(ht);
		    	      }		
		    	html= privateMsgService.getprimsgList(listhtml); 
		      
	return html;
	}
	/**
	 * 数据库按状态取值
	 * @param statusdb
	 * @return
	 */
	public String getStatusdb(String statusdb){
		Session ss=getSessionFactory().openSession();
		String valued="";
		try{
			Query q=ss.createQuery("from ConfigMst where TYPE='MSG_STATUS' and ckey=?");
			q.setString(0, statusdb);
			ArrayList list=(ArrayList)q.list();
			for(int i=0;i<list.size();i++){
				ConfigMst sm=(ConfigMst)list.get(i);
				valued=sm.getValue()==null?"":sm.getValue();
			}
		}catch(Exception e){ 
			logger.error(e,e);
		}finally{
			ss.close();
		}
		return valued;
	}
	/**
	 * 个人短信查询余额等显示
	 * @param userid
	 * @return
	 */
	public String queryuserdata1(String userid) {
	    String html = "";// StringBuffer html = new StringBuffer();
	    int leavemsg=this.qleavemsg(userid);
	    int totalmsg=this.qtotalmsg(userid);
	    int successmsg=this.qsuccess(userid);
	    int failuremsg=totalmsg-successmsg;
	    double priceall=this.qpriceall(userid);
	    double priceallend = Math.ceil(priceall * 100) / 100;
	     html = "剩余短信额度 " + leavemsg + " 条。<br />累计发送短信 " + totalmsg + " 条，成功 "
	          + successmsg + " 条，不成功 " + failuremsg + " 条。";

	    return html;
	  }
/**
 * 剩余短信条数
 * @param userid
 * @return
 */
public int qleavemsg(String userid){
	Session ss=getSessionFactory().openSession();
	int leaveval=0;
	try{
		Query q=ss.createQuery("from LimitMst where userid=?");
		q.setString(0, userid);
		ArrayList list=(ArrayList)q.list();
		for(int i=0;i<list.size();i++){
			LimitMst lm=(LimitMst)list.get(i);
			leaveval=lm.getSmsLimit();
		}
		
	}catch(Exception e){
		logger.error(e,e);
	}finally{
		ss.close();
	}
	return leaveval;
}
/**
 * 查询已发送的全部短信数量
 * @param userid
 * @return
 */
public int qtotalmsg(String userid){
	Session ss=getSessionFactory().openSession();
	int totalval=0;
	try{
		Query qq=ss.createQuery("from MsgMst where userid=?");
		qq.setString(0, userid);
		ArrayList list=(ArrayList)qq.list();
		totalval=list.size();
	}catch(Exception e){
		logger.error(e,e);
	}finally{
		ss.close();
	}
	return totalval;
}
/**
 * 成功短信数量
 * @param userid
 * @return
 */
public int qsuccess(String userid){
	Session ss=getSessionFactory().openSession();
	int successval=0;
	try{
		Query qq=ss.createQuery("from MsgMst where userid=? and status='2'");
		qq.setString(0, userid);
		ArrayList list=(ArrayList)qq.list();
		successval=list.size();
	}catch(Exception e){
		logger.error(e,e);
	}finally{
		ss.close();
	}
	return successval;
}
/**
 * 查询短信成功数量，并计算费用
 * @param userid
 * @return
 */
public double qpriceall(String userid){
	Session ss=getSessionFactory().openSession();
	double priceall = 0;
	try{
		Query qq=ss.createQuery("from MsgMst where userid=? and status='2'");
		qq.setString(0, userid);
		ArrayList list=(ArrayList)qq.list();
		for(int i=0;i<list.size();i++){
			MsgMst mm=(MsgMst)list.get(i);
			double unitprice=mm.getPrice();
			priceall += unitprice;
		}
	}catch(Exception e){
		logger.error(e,e);
	}finally{
		ss.close();
	}
	return priceall;
}
/**
 * 数据库取状态
 * @return
 */
public String querystatuses(){
	Session ss=getSessionFactory().openSession();
	String html="";
	ArrayList list1=new ArrayList();
	try{
		Query qq=ss.createQuery("from ConfigMst where TYPE='MSG_STATUS'");
		ArrayList list=(ArrayList)qq.list();
		for(int i=0;i<list.size();i++){
			Hashtable hs = new Hashtable();
			ConfigMst cm=(ConfigMst)list.get(i);
			String key=cm.getCkey()==null?"":cm.getCkey();
			String value=cm.getValue()==null?"":cm.getValue();
			hs.put("KEY", key);
			hs.put("VALUE", value);
			list1.add(hs);
		}
	}catch(Exception e){
		logger.error(e,e);
	}finally{
		ss.close();
	}
	html=privateMsgService.getstatuslist(list1);
		return html;
}	

/**
 * 数据库取状态
 * @return
 */
public String querystatuses2(){
	Session ss=getSessionFactory().openSession();
	String html="";
	ArrayList list1=new ArrayList();
	try{
		Query qq=ss.createQuery("from ConfigMst where TYPE='MSG_STATUS'");
		ArrayList list=(ArrayList)qq.list();
		for(int i=0;i<list.size();i++){
			Hashtable hs = new Hashtable();
			ConfigMst cm=(ConfigMst)list.get(i);
			String key=cm.getCkey()==null?"":cm.getCkey();
			String value=cm.getValue()==null?"":cm.getValue();
			hs.put("KEY", key);
			hs.put("VALUE", value);
			list1.add(hs);
		}
	}catch(Exception e){
		logger.error(e,e);
	}finally{
		ss.close();
	}
	html=privateMsgService.getstatuslist2(list1);
		return html;
}	
/**
 * 数据库取状态
 * @return
 */
public String querystatuses3(String status){
	Session ss=getSessionFactory().openSession();
	String html="";
	ArrayList list1=new ArrayList();
	try{
		Query qq=ss.createQuery("from ConfigMst where TYPE='MSG_STATUS'");
		ArrayList list=(ArrayList)qq.list();
		for(int i=0;i<list.size();i++){
			Hashtable hs = new Hashtable();
			ConfigMst cm=(ConfigMst)list.get(i);
			String key=cm.getCkey()==null?"":cm.getCkey();
			String value=cm.getValue()==null?"":cm.getValue();
			hs.put("KEY", key);
			hs.put("VALUE", value);
			list1.add(hs);
		}
	}catch(Exception e){
		logger.error(e,e);
	}finally{
		ss.close();
	}
	html=privateMsgService.getstatuslist3(list1,status);
		return html;
}	

}

