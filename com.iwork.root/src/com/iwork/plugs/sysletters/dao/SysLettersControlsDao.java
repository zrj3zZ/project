package com.iwork.plugs.sysletters.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.plugs.syscalendar.util.SysCalendarUtil;
import com.iwork.plugs.sysletters.constants.SysLettersConstants;
import com.iwork.plugs.sysletters.model.SysLettersContent;
import com.iwork.plugs.sysletters.model.SysLettersDetailInfo;
import com.iwork.plugs.sysletters.model.SysLettersDetailReply;
import com.iwork.plugs.sysletters.util.SysLetterUtil;
/**
 * 站内信DAO层
 * @author WangJianhui
 *
 */
public class SysLettersControlsDao extends HibernateDaoSupport{
	
	/**
	 * 改变标记状态
	 * @param id
	 * @param letterId
	 * @param receiveUserId
	 * @param owenerId
	 * @return
	 */
	public int changeLetterFlag(Long letterId,String receiveUserId,String ownerId,String flag){
		int count = 0 ;
		if(letterId!=null&&receiveUserId!=null&&!"".equals(receiveUserId)&&ownerId!=null&&!"".equals(ownerId)){
			String updateSql = "update SysLettersDetailInfo set checkStatus=? where letterId=? and receiveUserId=? and ownerId=?";
			DBUtilInjection d=new DBUtilInjection();
		   
		    if ((receiveUserId != null) && (!"".equals(receiveUserId))) {
	        	if(d.HasInjectionData(receiveUserId)){
    				return count;
    			}
	         
	        }
		    if ((ownerId != null) && (!"".equals(ownerId))) {
	        	if(d.HasInjectionData(ownerId)){
    				return count;
    			}
	         
	        }
		    if ((flag != null) && (!"".equals(flag))) {
	        	if(d.HasInjectionData(flag)){
    				return count;
    			}
	         
	        }
			Object[] values = {flag,letterId,receiveUserId,ownerId};
			count = this.getHibernateTemplate().bulkUpdate(updateSql,values);
		}
		return count;
	}
	/**
	 * 批量处理标记状态
	 * @param letterIds
	 * @param receiveUserId
	 * @param ownerId
	 * @param flag
	 * @return
	 */
	public int changeLetterMoreFlag(List<Long> letterIds,String receiveUserId,String ownerId,String flag){
		int count = 0 ;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		if(letterIds!=null&&letterIds.size()>0&&receiveUserId!=null&&!"".equals(receiveUserId)&&ownerId!=null&&!"".equals(ownerId)){
			DBUtilInjection d=new DBUtilInjection();
		   
		  
	        	if(d.HasInjectionData(receiveUserId)){
    				return count;
    			}
	         
	       
		 
	        	if(d.HasInjectionData(ownerId)){
    				return count;
    			}
	         
	       
			Query query = session.createQuery(SysLettersConstants.SQL_UPDATE_FLAG);
			query.setParameterList("letterIds", letterIds);
			query.setString("flag", flag);
			query.setString("receiveUserId", receiveUserId);
			query.setString("ownerId", receiveUserId);
			count = query.executeUpdate();
		}
		tx.commit();
		session.close();
		return count;
	}
	/**
	 * 查看站内信详细信息
	 * @param letterId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SysLettersContent getLettersContent(Long letterId ,String receiveUserId){
		SysLettersContent sysModel = new SysLettersContent();
		String sql = "from SysLettersContent where id=?";
		Object[] values = {letterId};
		List<SysLettersContent> list = this.getHibernateTemplate().find(sql,values);
		if(list.size()>0&&list!=null){
			sysModel = list.get(0);
		}
	
		return sysModel;
	}
	/**
	 * 查询详细回复信息
	 * @param letterId
	 * @param receiveUserId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysLettersDetailInfo> getLettersReply(Long letterId ,String receiveUserId){
		String sql1 = "select u from SysLettersDetailInfo u left join u.syslettersdetailreply where u.letterId=? and u.ownerId=? order by u.ts desc";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    	if(receiveUserId!=null&&!"".equals(receiveUserId))
        	if(d.HasInjectionData(receiveUserId)){
				return lis;
			}
		Object[] values = {letterId,receiveUserId};
		List<SysLettersDetailInfo> listDtail  = this.getHibernateTemplate().find(sql1,values);
		return listDtail;
	}
	
	/**
	 * 查询是否有新消息记录
	 */
	public Long isExitsLettersReply(Long letterId ,String receiveUserId,Long beforeId){
		String sql1 = "select max(u.id) from SysLettersDetailInfo u left join u.syslettersdetailreply where u.letterId=? and u.ownerId=? and u.sentUserId<>? and u.id>?";
		DBUtilInjection d=new DBUtilInjection();
	    
	    	if(receiveUserId!=null&&!"".equals(receiveUserId))
        	if(d.HasInjectionData(receiveUserId)){
				return 0L;
			}
		Object[] values = {letterId,receiveUserId,receiveUserId,beforeId};
		List listDtail  = this.getHibernateTemplate().find(sql1,values);
		
		Long id = 0L;
		if(listDtail!=null&&listDtail.size()>0){
			id = (Long) listDtail.get(0);
		}else{
			id = null;
		}
		return id;
	}
	/**
	 * 初始化最后的回复ID
	 */
	public Long getLastLettersReply(Long letterId ,String receiveUserId){
		String sql1 = "select max(u.id) from SysLettersDetailInfo u left join u.syslettersdetailreply where u.letterId=? and u.ownerId=? and u.sentUserId<>?";
		DBUtilInjection d=new DBUtilInjection();
	     
	    	if(receiveUserId!=null&&!"".equals(receiveUserId))
        	if(d.HasInjectionData(receiveUserId)){
				return 0L;
			}
		Object[] values = {letterId,receiveUserId,receiveUserId};
		List listDtail  = this.getHibernateTemplate().find(sql1,values);
		
		Long id = 0L;
		if(listDtail!=null&&listDtail.size()>0){
			id = (Long) listDtail.get(0);
		}else{
			id = null;
		}
		return id;
	}
	/**
	 * 查询基本信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysLettersDetailInfo> list(String userId){
		List<SysLettersDetailInfo> list = new ArrayList<SysLettersDetailInfo>();
		SysLetterUtil util = new SysLetterUtil();
		String sql = SysLettersConstants.SQL_LIST;
		DBUtilInjection d=new DBUtilInjection();
	     
    	if(userId!=null&&!"".equals(userId))
    	if(d.HasInjectionData(userId)){
			return list;
		}
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery(sql);
		query.setString("receiveUserId", userId);
		query.setString("ownerId", userId);
		List<SysLettersDetailInfo> listR = query.list();
		List<Long> listId = new ArrayList<Long>(); 
		List<SysLettersDetailInfo> listCheck = new ArrayList<SysLettersDetailInfo>();
		Iterator iterator = listR.iterator();
		while(iterator.hasNext()){
		         Object[] p = (Object[])iterator.next();
		         SysLettersDetailInfo detailInfo = new SysLettersDetailInfo();
		         SysLettersContent contentInfo = new SysLettersContent();
		         detailInfo.setId((Long)( p[0]));
		         detailInfo.setLetterId((Long)(p[1]));
		         detailInfo.setReceiveUserId((String)util.removeNull(p[2]));
		         detailInfo.setReceiveUserName((String)util.removeNull(p[3]));
		         //detailInfo.setSentUserId((String)util.removeNull(p[4]));
		         //detailInfo.setSentUserName((String)util.removeNull(p[5]));
		         detailInfo.setCreateUserName((String)util.removeNull(p[6]));
		         contentInfo.setLetterTitle((String)util.removeNull(p[4]));
		         detailInfo.setSysletterscontent(contentInfo);
		         listId.add(detailInfo.getId());
		         //detailInfo.getSysletterscontent().setLetterTitle((String)util.removeNull(p[7]));
		         list.add(detailInfo);
		   }
		//对check_status值进行赋值操作
		if(listId!=null&&listId.size()>0){
			sql = SysLettersConstants.SQL_BY_ID;
			Query query_id = session.createQuery(sql);
			query_id.setParameterList("listId",listId);
			listCheck =  query_id.list();
			if(listCheck.size()>0&&listCheck!=null){
				for(int i=0;i<list.size();i++){
					for(int j=0;j<listCheck.size();j++){
						if(list.get(i).getId().equals(listCheck.get(j).getId())){
							list.get(i).setCheckStatus(listCheck.get(j).getCheckStatus());
							list.get(i).setTs((listCheck.get(j).getTs()));
							listCheck.remove(j);
							j = j-1;
							break;
						}
					}
				}
			}
		}
		tx.commit();
		session.close();
		return list;
	}
	/**
	 * 创建站内信
	 * @param syslettersdetailinfo
	 * @param sysletterscontent
	 */
	public void createLetter(SysLettersDetailInfo syslettersdetailinfo,SysLettersContent sysletterscontent,String[] ReceiveIds,String[] ReceiveNames){
		this.getHibernateTemplate().saveOrUpdate(sysletterscontent);
		Long id = sysletterscontent.getId();
		DBUtilInjection d=new DBUtilInjection();
		if(id!=null){
			syslettersdetailinfo.setLetterId(id);
			SysLettersDetailReply syslettersdetailreply = new SysLettersDetailReply();
			for(int i=0;i<ReceiveIds.length;i++){
				//每发送一条信息时候根据接收人数量插入相应数量的记录,用于邮件分开处理
				//1)创建消息人根据发送N个人插入N条记录
				if(ReceiveNames[i]!=null&&!"".equals(ReceiveNames[i]))
			    	if(d.HasInjectionData(ReceiveNames[i])){
						return;
					}
				if(ReceiveIds[i]!=null&&!"".equals(ReceiveIds[i]))
			    	if(d.HasInjectionData(ReceiveIds[i])){
						return;
					}
				syslettersdetailinfo.setOwnerId(syslettersdetailinfo.getCreateUserId());
				syslettersdetailinfo.setReceiveUserName(ReceiveNames[i]);
				syslettersdetailinfo.setReceiveUserId(ReceiveIds[i]);
				this.getHibernateTemplate().save(syslettersdetailinfo);
				//插入发送信息到回复表
				syslettersdetailreply.setDetailDataId(syslettersdetailinfo.getId());
				syslettersdetailreply.setReplyContent(sysletterscontent.getLetterContent());
				syslettersdetailreply.setLetterId(sysletterscontent.getId());
				syslettersdetailreply.setUserId(sysletterscontent.getCreateUserId());
				syslettersdetailreply.setTs(sysletterscontent.getTs());
				this.getHibernateTemplate().save(syslettersdetailreply);
				//2)接收人信息插入
				syslettersdetailinfo.setOwnerId(ReceiveIds[i]);
				syslettersdetailinfo.setReceiveUserId(ReceiveIds[i]);
				syslettersdetailinfo.setReceiveUserName(ReceiveNames[i]);
				this.getHibernateTemplate().save(syslettersdetailinfo);
				//插入发送信息到回复表
				syslettersdetailreply.setDetailDataId(syslettersdetailinfo.getId());
				syslettersdetailreply.setReplyContent(sysletterscontent.getLetterContent());
				syslettersdetailreply.setLetterId(sysletterscontent.getId());
				syslettersdetailreply.setUserId(ReceiveIds[i]);
				syslettersdetailreply.setTs(sysletterscontent.getTs());
				this.getHibernateTemplate().save(syslettersdetailreply);
			}
		}
	}
	/**
	 * 创建回复站内信,用于多人发送
	 * @param syslettersdetailinfo
	 * @param ReceiveIds
	 * @param ReceiveNames
	 */
	public void createLetterReply(SysLettersDetailInfo syslettersdetailinfo,String[] ReceiveIds,String[] ReceiveNames,String content){
		DBUtilInjection d=new DBUtilInjection();
		SysLettersDetailReply syslettersDetailReply = new SysLettersDetailReply();
		SysCalendarUtil util = new SysCalendarUtil();
		if(ReceiveIds.length>0&&ReceiveIds!=null){
			Session session = getHibernateTemplate().getSessionFactory().openSession();
			Transaction tx = session.beginTransaction();
			for(int i=0;i<ReceiveIds.length;i++){
				//每发送一条信息时候根据接收人数量插入相应数量的记录,用于邮件分开处理
				//1） 创建消息人根据发送N个人插入N条记录
				if(ReceiveNames[i]!=null&&!"".equals(ReceiveNames[i]))
			    	if(d.HasInjectionData(ReceiveNames[i])){
						return;
					}
				if(ReceiveIds[i]!=null&&!"".equals(ReceiveIds[i]))
			    	if(d.HasInjectionData(ReceiveIds[i])){
						return;
					}
				if(content!=null&&!"".equals(content))
			    	if(d.HasInjectionData(content)){
						return;
					}
				syslettersdetailinfo.setOwnerId(syslettersdetailinfo.getSentUserId());
				syslettersdetailinfo.setReceiveUserId(ReceiveIds[i]);
				syslettersdetailinfo.setReceiveUserName(ReceiveNames[i]);
				syslettersdetailinfo.setCheckStatus(SysLettersConstants.CHECKSTATUS_TURE);//自己发送的消息默认为已读
				//插入发送人消息
				session.save(syslettersdetailinfo);
				//this.getHibernateTemplate().save(syslettersdetailinfo);
				Long detaiInfoId = syslettersdetailinfo.getId();
				syslettersDetailReply.setDetailDataId(detaiInfoId);
				syslettersDetailReply.setLetterId(syslettersdetailinfo.getLetterId());
				syslettersDetailReply.setReplyContent(content);
				syslettersDetailReply.setUserId(syslettersdetailinfo.getReceiveUserId());
				syslettersDetailReply.setTs(util.getTimeStamp());
				//插入发送人消息内容
				session.save(syslettersDetailReply);
				//this.getHibernateTemplate().save(syslettersDetailReply);
				//清除缓存
				session.flush();
				session.clear();
				//当发送人给自己回复的时候,只插入一条即可
				if(!syslettersdetailinfo.getSentUserId().equals(syslettersdetailinfo.getReceiveUserId())){
					
					//2）接收人信息插入
					syslettersdetailinfo.setOwnerId(ReceiveIds[i]);
					syslettersdetailinfo.setReceiveUserId(ReceiveIds[i]);
					syslettersdetailinfo.setReceiveUserName(ReceiveNames[i]);
					syslettersdetailinfo.setCheckStatus(SysLettersConstants.CHECKSTATUS_FALSE);//发送给别人的消息默认为未读
					//插入接收人信息
					session.save(syslettersdetailinfo);
					//this.getHibernateTemplate().save(syslettersdetailinfo);
					Long detaiInfoId1 = syslettersdetailinfo.getId();
					syslettersDetailReply.setDetailDataId(detaiInfoId1);
					syslettersDetailReply.setLetterId(syslettersdetailinfo.getLetterId());
					syslettersDetailReply.setReplyContent(content);
					syslettersDetailReply.setUserId(syslettersdetailinfo.getReceiveUserId());
					syslettersDetailReply.setTs(util.getTimeStamp());
					//插入接收人信息内容
					session.save(syslettersDetailReply);
					//清除缓存
					session.flush();
					session.clear();
					//this.getHibernateTemplate().save(syslettersDetailReply);
				}
			}
			tx.commit();
			session.close();
		}
	}
	/**
	 * 用于现有模式，一对一聊天
	 * @param syslettersdetailinfo
	 * @param ReceiveIds
	 * @param ReceiveNames
	 * @param content
	 */
	public void createLetterReply_single(SysLettersDetailInfo syslettersdetailinfo,String ReceiveIds,String ReceiveNames,String content){
			SysLettersDetailReply syslettersDetailReply = new SysLettersDetailReply();
			SysCalendarUtil util = new SysCalendarUtil();
			DBUtilInjection d=new DBUtilInjection();
			if(ReceiveIds!=null){
				Session session = getHibernateTemplate().getSessionFactory().openSession();
				Transaction tx = session.beginTransaction();
				if(ReceiveNames!=null&&!"".equals(ReceiveNames))
			    	if(d.HasInjectionData(ReceiveNames)){
						return;
					}
				if(ReceiveIds!=null&&!"".equals(ReceiveIds))
			    	if(d.HasInjectionData(ReceiveIds)){
						return;
					}
				if(content!=null&&!"".equals(content))
			    	if(d.HasInjectionData(content)){
						return;
					}
				//每发送一条信息时候根据接收人数量插入相应数量的记录,用于邮件分开处理
				//1） 创建消息人根据发送N个人插入N条记录
				syslettersdetailinfo.setOwnerId(syslettersdetailinfo.getSentUserId());
				syslettersdetailinfo.setReceiveUserId(ReceiveIds);
				syslettersdetailinfo.setReceiveUserName(ReceiveNames);
				syslettersdetailinfo.setCheckStatus(SysLettersConstants.CHECKSTATUS_TURE);//自己发送的消息默认为已读
				//插入发送人消息
				session.save(syslettersdetailinfo);
				//this.getHibernateTemplate().save(syslettersdetailinfo);
				Long detaiInfoId = syslettersdetailinfo.getId();
				syslettersDetailReply.setDetailDataId(detaiInfoId);
				syslettersDetailReply.setLetterId(syslettersdetailinfo.getLetterId());
				syslettersDetailReply.setReplyContent(content);
				syslettersDetailReply.setUserId(syslettersdetailinfo.getReceiveUserId());
				syslettersDetailReply.setTs(util.getTimeStamp());
				//插入发送人消息内容
				session.save(syslettersDetailReply);
				//清除缓存
				session.flush();
				session.clear();
				//当发送人给自己回复的时候,只插入一条即可
				if(!syslettersdetailinfo.getSentUserId().equals(syslettersdetailinfo.getReceiveUserId())){
					//2）接收人信息插入
					syslettersdetailinfo.setOwnerId(ReceiveIds);
					syslettersdetailinfo.setReceiveUserId(ReceiveIds);
					syslettersdetailinfo.setReceiveUserName(ReceiveNames);
					syslettersdetailinfo.setCheckStatus(SysLettersConstants.CHECKSTATUS_FALSE);//发送给别人的消息默认为未读
					//插入接收人信息
					session.save(syslettersdetailinfo);
					Long detaiInfoId1 = syslettersdetailinfo.getId();
					syslettersDetailReply.setDetailDataId(detaiInfoId1);
					syslettersDetailReply.setLetterId(syslettersdetailinfo.getLetterId());
					syslettersDetailReply.setReplyContent(content);
					syslettersDetailReply.setUserId(syslettersdetailinfo.getReceiveUserId());
					syslettersDetailReply.setTs(util.getTimeStamp());
					//插入接收人信息内容
					session.save(syslettersDetailReply);
					//清除缓存
					session.flush();
					session.clear();
				}
				tx.commit();
				session.close();
			}
	}
	/**
	 * 删除回复内容
	 * @param id
	 * @return
	 */
	public int delLetterReply(Long id){
		int count = 0;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Query query_reply = session.createQuery(SysLettersConstants.SQL_DEL_REPLY);
		Query query_reply_content = session.createQuery(SysLettersConstants.SQL_DEL_REPLY_CONTENT);
		query_reply.setLong("id", id);
		query_reply_content.setLong("detailDataId", id);
		int t_count = query_reply.executeUpdate();
		int c_count = 0;
		if(t_count>0){
			c_count = query_reply_content.executeUpdate();
		}
		count = t_count + c_count;
		tx.commit();
		session.close();
		return count;
	}
	/**
	 * 
	 * @param letterIds
	 * @param userId
	 * @return
	 */
	public int delLettersList(List<Long> letterIds,String userId){
		int count = 0 ;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Query query_detail = session.createQuery(SysLettersConstants.SQL_DEL_LIST_DETAIL);
		Query query_reply = session.createQuery(SysLettersConstants.SQL_DEL_LIST_REPLY);
		DBUtilInjection d=new DBUtilInjection();
		 if ((userId != null) && (!"".equals(userId))) {
	 	if(d.HasInjectionData(userId)){
			return count;
		}
		 }
    
  
	    
		query_detail.setParameterList("letterIds", letterIds);
		query_detail.setString("ownerId", userId);
		query_reply.setParameterList("letterIds", letterIds);
		query_reply.setString("userId", userId);
		int t_count = query_detail.executeUpdate();
		int c_count = 0;
		if(t_count>0){
			c_count = query_reply.executeUpdate();
		}
		count = t_count + c_count;
		tx.commit();
		session.close();
		return count;
	}
}
