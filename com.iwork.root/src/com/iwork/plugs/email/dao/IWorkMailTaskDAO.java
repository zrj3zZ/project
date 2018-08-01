
package com.iwork.plugs.email.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SequenceUtil;
import com.iwork.plugs.email.constant.BoxTypeConst;
import com.iwork.plugs.email.model.MailTaskModel;
import com.iwork.plugs.email.model.SearchModel;

public  class IWorkMailTaskDAO  extends HibernateDaoSupport {
//	private String userId;
	private static String SEQUENCE_MAILTASK = "_MAILTASK";
	private Class<MailTaskModel> mailTaskModel;
//	private SessionFactory sessionFactory ;
//	public IWorkMailTaskDAO() {
//		super();
//		SessionFactory sessionFactory = (SessionFactory)SpringBeanUtil.getBean("sessionFactory");
//		this.setHibernateTemplate(new HibernateTemplate(sessionFactory));
//		
//	}
	
	/**
	 * 执行数据添加操作
	 * @param model
	 * @return
	 */
	public Long save(MailTaskModel model) {
			model.id = getSequenceValue();
		String mailDate = DBUtil.getDateDefaultValue();
		if (model.createTime != null) {
			mailDate = DBUtil.convertLongDate(UtilDate
					.datetimeFormat24(model.createTime));
		}
		try{
			
			this.getHibernateTemplate().save(model);
			return model.id;
		}catch(Exception e){
			logger.error(e,e);
		}
		return null;
	}
	
	/**
	 * 获取总行数
	 * @param userId
	 * @return
	 */
	public int getInstanceOfBoxRows(String userId) {
		Hashtable models = new Hashtable(); 
		List<MailTaskModel> list = null;
		StringBuffer sql = new StringBuffer();
		try {
				sql.append("FROM MailTaskModel WHERE owner =? AND  isDel = 0 ORDER BY id DESC");
				DBUtilInjection d=new DBUtilInjection();
			    
			    if(userId!=null&&!"".equals(userId)){
					   if(d.HasInjectionData(userId)){
		   				return 0;
		   			}
			    }
				Object[] value = {userId};
			return this.getHibernateTemplate().find(sql.toString(),value).size();
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
		}
		return 0;
	}
	
	 /**
	  * 查询所有收件箱中邮件信息
	  **/
	   public List<MailTaskModel> doSearchList(SearchModel sm){
		   DBUtilInjection d=new DBUtilInjection();
		    List lis=new ArrayList(); 
		   StringBuffer hql = new StringBuffer("From MailTaskModel where owner=?  ");
		   if(sm.getSender()!=null&&!"".equals(sm.getSender())){
			   if(d.HasInjectionData(sm.getSender())){
   				return lis;
   			}
			   hql.append(" and mailFrom like ?");
		   }
		   if(sm.getRecever()!=null&&!"".equals(sm.getRecever())){
			   
				   if(d.HasInjectionData(sm.getRecever())){
	   				return lis;
				   }
			   hql.append(" and recevier like ?");
		   }
		   if(sm.getKeyword()!=null){
			   if(d.HasInjectionData(sm.getRecever())){
	   				return lis;
				   }
			   hql.append(" and title like ?");
		   }
			final String sql1=hql.toString();
			final String f_owner=UserContextUtil.getInstance().getCurrentUserId();
			final SearchModel sm_ = sm;
			List<MailTaskModel> list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
				public Object doInHibernate(org.hibernate.Session session)
						throws HibernateException, SQLException {
					// TODO 自动生成方法存根
					Query query=session.createQuery(sql1);
					int i = 0;
					query.setString(i,f_owner);i++; 
					if(sm_.getSender()!=null&&!"".equals(sm_.getSender())){
						query.setString(i, "%"+sm_.getSender()+"%");i++;
					}
					if(sm_.getRecever()!=null&&!"".equals(sm_.getRecever())){
						query.setString(i, "%"+sm_.getRecever()+"%");i++;
					}
					if(sm_.getKeyword()!=null){
						query.setString(i, "%"+sm_.getKeyword()+"%");i++;
					}
					return query.list();
				}
			});
		   return list;
	}
	   /**
	    * 查询所有收件箱中邮件信息
	    * @author wenpengyu
	    * */
	   public List<MailTaskModel> findall(String username){
		   //   Hashtable models = new Hashtable(); 
		   String hql = "From MailTaskModel where owner = ? and isDel = 0 ";
		   Object[] value = {username};
		   return this.getHibernateTemplate().find(hql,value);
	   }
	 
	   /**
	    * 更新收件箱中邮件的信息
	    * @author wenpengyu
	    * */
	   public void updateRecEmail(MailTaskModel model){
		   
		   
		   this.getHibernateTemplate().update(model);
		   
		   
	   }

	   //查询所有数据
	public List<MailTaskModel> searchAll() {
		String hql = "From MailTaskModel";
		return this.getHibernateTemplate().find(hql);

	}
	   
	   
	   /**
	    *按照id查找邮件信息
	    * @author wenpengyu
	    * */
		public MailTaskModel getMailTaskModelById(long id){
			String sql="FROM MailTaskModel where id=?";
			MailTaskModel Model = new MailTaskModel();
			Object[] value = {id};
		    List<MailTaskModel> list =  this.getHibernateTemplate().find(sql,value);
		    for(MailTaskModel taskModel: list){
		    	Model = taskModel;
		    }
			return Model;
		}
		
		/**
		 *按照id查找邮件信息
		 * @author wenpengyu
		 * */
		public MailTaskModel getMailTaskModelById(String userid,long id){
			String sql="FROM MailTaskModel where  id=? and owner like ?"; 
			MailTaskModel Model = new MailTaskModel();
			DBUtilInjection d=new DBUtilInjection();
		    List lis=new ArrayList();
		    if ((userid != null) && (!"".equals(userid))) {
	        	if(d.HasInjectionData(userid)){
    				return Model;
    			}
	          
	        }
			Object[] value = {id,userid};
			List<MailTaskModel> list =  this.getHibernateTemplate().find(sql,value);
			for(MailTaskModel taskModel: list){
				Model = taskModel;
			}
			return Model;
		}
		   /**
		    *删除邮件信息
		    * @author wenpengyu
		    * */
		public void deleteEmail(MailTaskModel model){
			DBUtilInjection d=new DBUtilInjection();
		    List lis=new ArrayList(); 
		    
			this.getHibernateTemplate().delete(model);
			
		}

	public Long getSequenceValue() {
		int i = SequenceUtil.getInstance().getSequenceIndex(SEQUENCE_MAILTASK);
		return new Long(i);
	}
	
	/**
	 * 更新
	 * @param model
	 */
	public void updateMailTask(MailTaskModel model) {
		this.getHibernateTemplate().update(model);
	}
	
	/**
	 * 插入
	 * @param model
	 */
	public void addMailTask(MailTaskModel model) {
		this.getHibernateTemplate().save(model);	
	}
	
	/**
	 * 删除
	 * @param model
	 */
	public void deleteMailTask(MailTaskModel model) {
		
		this.getHibernateTemplate().delete(model);	
	}
	
	/**
	 * 查询所有
	 * @param model
	 */
	public List<MailTaskModel> getAllMailTaskModel(String userName){
		String sql="FROM MailTaskModel WHERE OWNER=? AND mailbox = ? AND isDel = 0 ORDER BY ID";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((userName != null) && (!"".equals(userName))) {
        	if(d.HasInjectionData(userName)){
				return lis;
			}
         
        }
		Object[] value = {userName,BoxTypeConst.TYPE_TRANSACT};
	    List<MailTaskModel> list =  this.getHibernateTemplate().find(sql,value);
		return list;
	}
	
	/**
	 * 
	 * 获取发件箱当前总行数
	 * 
	 * */
	public int countReceiveEail(String userId) {

		int size = 0;
		// 拼接hql语句
		String sql = "FROM MailTaskModel where owner=? and isDel=? order by id desc";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((userId != null) && (!"".equals(userId))) {
        	if(d.HasInjectionData(userId)){
				return size;
			}
         
        }
		Object[] value = {userId,BoxTypeConst.IS_DEL_NO};
		List<MailTaskModel> mailDelList = this.getHibernateTemplate().find(sql,value);
		size = mailDelList.size();
		return size;
	}
	
	/**
	 * 获取当前用户收件箱中未读邮件总数
	 * 
	 * @param boxType
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int countRecUnReadEmail(String userId) {

		int size = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("FROM MailTaskModel where 1=1 and owner=? and isDel=? and isRead=? and mailBox=? order by id desc");
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((userId != null) && (!"".equals(userId))) {
        	if(d.HasInjectionData(userId)){
				return size;
			}
         
        }
		Object[] value = {userId,BoxTypeConst.IS_DEL_NO,BoxTypeConst.IS_READ_NO,BoxTypeConst.TYPE_TRANSACT};
		List<MailTaskModel> mailDelList = this.getHibernateTemplate().find(sb.toString(),value);
		size = mailDelList.size();
		return size;
	}
	
	/**
	 * 获取当前用户收件箱中未读邮件总数
	 * 
	 * @param boxType
	 * @param userId
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MailTaskModel> getRecUnReadEmail(String userId, int pageSize,int startRow) {
		StringBuffer sb = new StringBuffer();
		sb.append("FROM MailTaskModel where 1=1 and owner=? and isDel=? and isRead=? and mailBox=? order by id desc");
		// 获取session对象
		DBUtilInjection d=new DBUtilInjection();
	    List l=new ArrayList(); 
		 if ((userId != null) && (!"".equals(userId))) {
	        	if(d.HasInjectionData(userId)){
 				return l;
 			}
	         
	        }
		
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(sb.toString());
		query.setString(0, userId);
		query.setLong(1, BoxTypeConst.IS_DEL_NO);
		query.setLong(2, BoxTypeConst.IS_READ_NO);
		query.setLong(3, BoxTypeConst.TYPE_TRANSACT);
		query.setFirstResult(startRow);
		query.setMaxResults(pageSize);
		List<MailTaskModel> list = query.list();
		session.close();
		return list;
	}
	
	/**
	 * 获取发件箱数据列表 
	 * 
	 * @param boxType
	 * @param userId
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List<MailTaskModel> getReceiveListEmails(String userId, int pageSize,
			int startRow) {
		String sql = "FROM MailTaskModel where owner=? and isDel=? order by id desc";
		// 获取session对象
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(sql);
		query.setString(0, userId);
		query.setLong(1, BoxTypeConst.IS_DEL_NO);
		query.setFirstResult(startRow);
		query.setMaxResults(pageSize);
		// 获取发件箱当前用户已发邮件集合
		List<MailTaskModel> list = query.list();
		session.close();
		return list;
	}
	public List<MailTaskModel> getReceiveListEmails(String userId) {
		String sql = "FROM MailTaskModel where owner=? and isDel=? order by id desc";
		// 获取session对象
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((userId != null) && (!"".equals(userId))) {
        	if(d.HasInjectionData(userId)){
				return lis;
			}
         
        }
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(sql);
		query.setString(0, userId);
		query.setLong(1, BoxTypeConst.IS_DEL_NO);
		// 获取发件箱当前用户已发邮件集合
		List<MailTaskModel> list = query.list();
		session.close();
		return list;
	}
	
	
	
	/**
	 * 根据ID获取信息
	 */
	public MailTaskModel findTaskById(Long id){
			String sql="from MailTaskModel where id=?";
			MailTaskModel mailTaskModel = new MailTaskModel();
			Object[] value = {id};
		    List<MailTaskModel> list =  this.getHibernateTemplate().find(sql,value);
		    for(MailTaskModel Model: list){
		    	mailTaskModel = Model;
		    }
			return mailTaskModel;
	}
	
	
	
	/**
	 * 根据ID获取信息
	 */
	public List<MailTaskModel> getMailTaskList(Long mailId){
		String sql="From MailTaskModel where bindId=? order by ID";
		Object[] value = {mailId};
		List<MailTaskModel> list = this.getHibernateTemplate().find(sql,value);
		return list;
	}
	
	/**
	 * 根据ID及用户id获取信息
	 * zouyalei 2016-03-16
	 */
	@SuppressWarnings("unchecked")
	public Long getIsRead(Long mailId,String userid){
		String sql="From MailTaskModel where bindId=? and owner = ?";
		MailTaskModel mailTaskModel = new MailTaskModel();
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
	    if ((userid != null) && (!"".equals(userid))) {
        	if(d.HasInjectionData(userid)){
				return 0L;
			}
         
        }
		Object[] value = {mailId,userid};
		List<MailTaskModel> list =  this.getHibernateTemplate().find(sql,value);
		if(list!=null && list.size()>0){
			mailTaskModel = (MailTaskModel)list.get(0);
		}
		return mailTaskModel.getIsRead();
	}
	
	/**
	 * 查询标星邮件
	 */
	public List<MailTaskModel> getStaeList(String userName){
		String sql="FROM MailTaskModel WHERE OWNER=? and isStar=1 and isDel=0";
		Object[] value = {userName};
		List<MailTaskModel> list =  this.getHibernateTemplate().find(sql,value);
		return list;
	}
	
	
	/**
	 * 获取页面收件箱邮件
	 * @param userId
	 * @param pageSize
	 * @param startRow
	 * @param isDel
	 * @param isRead
	 * @param mailBox
	 * @return
	 */
	public List<MailTaskModel> getPageEmails(String userId,int pageSize,int startRow,int isDel,int isRead,int mailBox){
		StringBuffer sb = new StringBuffer();
		sb.append("FROM MailTaskModel where 1=1 and owner=? and isDel=?");
		if(isRead==0 || isRead==1){
			sb.append(" and isRead=?");
		}
		sb.append(" and mailBox=? order by id desc");
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((userId != null) && (!"".equals(userId))) {
        	if(d.HasInjectionData(userId)){
				return lis;
			}
          
        }
		// 获取session对象
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(sb.toString());
		
		int i=0;
		query.setString(i, userId);i++;
		query.setInteger(i, isDel);i++;
		if(isRead==0 || isRead==1){
			query.setInteger(i, isRead);i++;
		}
		query.setInteger(i, mailBox);i++;
		
		query.setFirstResult(startRow);
		query.setMaxResults(pageSize);
		// 获取发件箱当前用户已发邮件集合
		List<MailTaskModel> list = query.list();
		session.close();
		return list;
	}
}
