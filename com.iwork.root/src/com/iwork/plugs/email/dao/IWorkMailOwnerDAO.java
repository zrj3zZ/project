package com.iwork.plugs.email.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.iwork.plugs.cms.model.IworkCmsAppkm;
import com.iwork.plugs.email.constant.BoxTypeConst;
import com.iwork.plugs.email.model.MailOwnerModel;
import com.iwork.plugs.email.model.SearchModel;

public class IWorkMailOwnerDAO extends HibernateDaoSupport {

	private static String SEQUENCE_MAILTASK = "_MAILOWNER";

	private Class<MailOwnerModel> mailOwnerModel;

	 /**
	  * 查询所有收件箱中邮件信息
	  **/
	   public List<MailOwnerModel> doSearchList(SearchModel sm){
		   List params = new ArrayList();
		   StringBuffer hql = new StringBuffer("From MailOwnerModel where owner=? ");
		   params.add(UserContextUtil.getInstance().getCurrentUserId());
		   if(sm.getFolderid()!=null&&sm.getFolderid().equals("4")){
			   hql.append(" and isArchives = 0");
		   }else{
			   hql.append(" and isArchives = 1");
		   }
		   
		   if(sm.getRecever()!=null){ 
			   hql.append(" and mailTo like ?");
			   params.add("%"+sm.getRecever()+"%");
		   }
		   if(sm.getKeyword()!=null){
			   hql.append(" and title like ?");
			   params.add("%"+sm.getKeyword()+"%");
		   }
			final String sql1=hql.toString();
			final List param = params;
			List<MailOwnerModel> list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
				public Object doInHibernate(org.hibernate.Session session)
						throws HibernateException, SQLException {
					// TODO 自动生成方法存根
					Query query=session.createQuery(sql1);
					for (int i = 0; i < param.size(); i++) {
						query.setParameter(i, param.get(i));
					} 
					return query.list();
				}
			});
		   return list;
	}

	/**
	 * 执行数查询当前用户所有发件信息 杨连峰
	 * 
	 * 
	 * @param model
	 * @return
	 */
	public List<MailOwnerModel> getSendList(String userName) {
		String sql = "FROM MailOwnerModel where owner=? and isDel=? and isArchives=? order by id desc";
		Object[] value = {userName, BoxTypeConst.IS_DEL_NO ,BoxTypeConst.IS_ARCHIVES_YES};
		List<MailOwnerModel> list = this.getHibernateTemplate().find(sql,value);
		
		return list;
	}

	/**
	 * 执行数查询当前用户所有发件信息 杨连峰
	 * 
	 * 
	 * @param model
	 * @return
	 */
	public MailOwnerModel getSendListById(long id) {
		String sql = "FROM MailOwnerModel where id=?";
		MailOwnerModel mailOwnerModel = new MailOwnerModel();
		Object[] value = {id};
		List<MailOwnerModel> list = this.getHibernateTemplate().find(sql,value);
		for (MailOwnerModel ownerModel : list) {
					
			mailOwnerModel = ownerModel;
		}
		return mailOwnerModel;
	}

	/**
	 * 获取标星邮件的记录条数
	 * @author wanglei
	 */
	public int getMailStarListSize(String userId){
		int size = 0;
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			//根据查找收件箱，发件箱数据的总条数
			String sql = "select a.id,a.name,a.title,a.time from ( select o.id as id,o.owner as name,o.title as title,o.create_time as time from iwork_mail_owner o where o.owner=? and o.is_star='1'  union all select t.id as id,t.mail_form as name ,t.title as title,t.create_time as time from iwork_mail_task t where t.owner=? and t.is_star='1') a ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, userId);
			rset = ps.executeQuery();
			while(rset.next())    
			{    
				size++;    
			}  
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rset);
		}
    	return size;
	}
	/**
	 * 获取收件箱发件箱里面所有标星邮件
	 * @author wanglei
	 */
	public List<HashMap> getAllStarList(String userName,int pageSize,int pageNumber) {
		List<HashMap> allStarList = new ArrayList<HashMap>();
		int startrow = (pageNumber-1)*pageSize;
		int endrow = pageSize*pageNumber;
		HashMap starMap =null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			//查找收件箱，发件箱里面所有标星的信息
			String sql = "select a.id,a.name,a.title,a.time,a.bindid,a.type,a.isstar,a.isread from ( select o.id as id,o.owner as name,o.title as title,o.create_time as time,o.bind_id as bindId,-1 as type,o.is_star as isstar,1 as isread from iwork_mail_owner o where o.owner=? and o.is_star='1' union all select t.id as id,t.mail_form as name ,t.title as title,t.create_time as time,t.bind_id as bindId,-2 as type,t.is_star as isstar,t.is_read as isread from iwork_mail_task t where t.owner=? and t.is_star='1') a order by a.time DESC";
			String finalSql = "select * from(select A.*, rownum rn from ("+sql+") A where rownum <= ?) where rn > ?";
			ps = conn.prepareStatement(finalSql);
			ps.setString(1, userName);
			ps.setString(2, userName);
			ps.setInt(3, endrow);
			ps.setInt(4, startrow);
			rset = ps.executeQuery();
			while(rset.next()){
				starMap = new HashMap();
				starMap.put("id", rset.getLong("id"));
				starMap.put("owner", rset.getString("name"));
				starMap.put("isstar", rset.getString("isstar"));
				starMap.put("isread", rset.getString("isread"));
				//截取字符串
				String str=rset.getString("title");
				if(str.length()>40){
					String title=str.substring(0,40)+"...";
					starMap.put("title", title);
				}else{
					starMap.put("title", rset.getString("title"));
				}
				//对取出时间做格式化
				if(rset.getString("time")!=null){
					Date createTime=UtilDate.StringToDate(rset.getString("time"),"yyyy-MM-dd HH:mm:ss");
					starMap.put("createTime", createTime);
				}else{
					starMap.put("createTime", rset.getDate("time"));
				}
				starMap.put("bindid", rset.getLong("bindid"));
				starMap.put("titles", rset.getString("title"));
				starMap.put("type", rset.getString("type"));
				allStarList.add(starMap);  
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rset);
		}
		return allStarList;
	
	}


	/**
	 * 执行数据添加操作 杨连峰
	 * 
	 * 
	 * @param model
	 * @return
	 */
	public void add(IworkCmsAppkm model) {
		model.toString();
		this.getHibernateTemplate().save(model);
	}
	
	
	
	public Long save(MailOwnerModel mom) {
		
		mom.id = getSequenceValue();
	String mailDate = DBUtil.getDateDefaultValue();
	if (mom.createTime != null) {
		mailDate = DBUtil.convertLongDate(UtilDate
				.datetimeFormat24(mom.createTime));
	}
	try{
		this.getHibernateTemplate().save(mom);
		return mom.id;
	}catch(Exception e){
		logger.error(e,e);
	}
	return null;
}

	

	public void delEamil(MailOwnerModel model) {
		this.getHibernateTemplate().delete(model);

	}

	/**

	 * 
	 * 分页获取发件箱当前总行数 杨连峰
	 * 
	 * @param userId

	 * @return
	 */
	public int countSendEail(String userId) {

		int size = 0;
		// 拼接hql语句
		String sql = "FROM MailOwnerModel where owner=? and isDel=? and isArchives=? order by id desc";
		Object[] value = {userId,BoxTypeConst.IS_DEL_NO,BoxTypeConst.IS_ARCHIVES_YES};
		List<MailOwnerModel> mailDelList = this.getHibernateTemplate().find(sql,value);
		size = mailDelList.size();
		return size;
	}

	/**
	 * 分页获取发件箱数据列表 杨连峰
	 * 
	 * @param boxType
	 * @param userId
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List<MailOwnerModel> getSendListEmails(String userId, int pageSize,
			int startRow) {
		String sql = "FROM MailOwnerModel where owner=? and isDel=? and isArchives=? order by id desc";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((userId != null) && (!"".equals(userId))) {
        	if(d.HasInjectionData(userId)){
				return lis;
			}
         
        }
		// 获取session对象
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(sql);
		query.setString(0, userId);
		query.setLong(1, BoxTypeConst.IS_DEL_NO);
		query.setLong(2, BoxTypeConst.IS_ARCHIVES_YES);
		query.setFirstResult(startRow);
		query.setMaxResults(pageSize);
		// 获取发件箱当前用户已发邮件集合
		List<MailOwnerModel> list = query.list();
		session.close();
		return list;
	}

	/**
	 * 分页获取发件箱数据列表 杨连峰
	 * 
	 * @param boxType
	 * @param userId
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List<MailOwnerModel> getSendListEmails(String userId) {
		String sql = "FROM MailOwnerModel where owner=? and isDel=? and isArchives=? order by id desc";
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
		query.setLong(2, BoxTypeConst.IS_ARCHIVES_YES);
		// 获取发件箱当前用户已发邮件集合
		List<MailOwnerModel> list = query.list();
		session.close();
		return list;
	}

	// 以下为草稿箱的实现功能
	/**
	 * 获草稿箱的数据列表 杨连峰
	 */
	public List<MailOwnerModel> getDraftList(String userName) {
		String sql = "FROM MailOwnerModel where owner=? and isDel=? and isArchives=? order by id desc";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((userName != null) && (!"".equals(userName))) {
        	if(d.HasInjectionData(userName)){
				return lis;
			}
         
        }
		Object[] value = {userName,BoxTypeConst.IS_DEL_NO,BoxTypeConst.IS_ARCHIVES_NO};
		List<MailOwnerModel> list = this.getHibernateTemplate().find(sql,value);

		return list;
	}
	  /**
	    * 按id查询相应的邮件信息
	    * @author 杨连峰
	    * */
	public MailOwnerModel searchDraftEmail(Long id){
		   String sql="FROM MailOwnerModel where id=?";
		   MailOwnerModel mailOwnerModel = new MailOwnerModel();
		   //按id查找邮件箱中的当前记录数的所有属性值
		   Object[] value = {id};
		    List<MailOwnerModel> list =  this.getHibernateTemplate().find(sql,value);
		    for(MailOwnerModel Model: list){
		    	mailOwnerModel = Model;
		    }
			return mailOwnerModel;
		   
		}
	
	/**
	 * 获取草稿箱当前总记录数 杨连峰
	 * 
	 * @param userId

	 * @return
	 */
	public int countDraftEail(String userId) {

		int size = 0;
		// 拼接hql语句
		String sql = "FROM MailOwnerModel where owner=? and isDel=? and isArchives=? order by id desc";
		DBUtilInjection d=new DBUtilInjection();
	   
	    if ((userId != null) && (!"".equals(userId))) {
        	if(d.HasInjectionData(userId)){
				return size;
			}
	    }
		Object[] value = {userId,BoxTypeConst.IS_DEL_NO,BoxTypeConst.IS_ARCHIVES_NO};
		List<MailOwnerModel> mailDelList = this.getHibernateTemplate().find(sql,value);
		size = mailDelList.size();
		return size;
	}

	/**
	 * 分页获取草稿箱数据列表 杨连峰
	 * 
	 * @param boxType
	 * @param userId
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List<MailOwnerModel> getDraftListEmails(String userId, int pageSize,
			int startRow) {
		String sql = "FROM MailOwnerModel where owner=? and isDel=? and isArchives=? order by id desc";
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
		query.setLong(2, BoxTypeConst.IS_ARCHIVES_NO);
		query.setFirstResult(startRow);
		query.setMaxResults(pageSize);
		// 获取发件箱当前用户已发邮件集合
		List<MailOwnerModel> list = query.list();
		session.close();

		return list;
	}
	

	public Long getSequenceValue() {
		int i = SequenceUtil.getInstance().getSequenceIndex(SEQUENCE_MAILTASK);
		return new Long(i);
	}

	/**
	 * 查询标星的邮件
	 */

	public List<MailOwnerModel> getStaeList(String userName) {
		String sql = "FROM MailOwnerModel WHERE OWNER=? and isStar=1 and isDel=0";
		DBUtilInjection d=new DBUtilInjection();
		List lis=new ArrayList();   
	    if ((userName != null) && (!"".equals(userName))) {
        	if(d.HasInjectionData(userName)){
				return lis;
			}
	    }
		Object[] value = {userName};
		List<MailOwnerModel> list = this.getHibernateTemplate().find(sql,value);

		return list;
	}

	/**
	 * 更新
	 * 
	 * @param model
	 */
	public void updateMailOwnerModel(MailOwnerModel model) {
		this.getHibernateTemplate().update(model);
	}

	/**
	 * 
	 * 查询所有
	 * @param model
	 */
	public List<MailOwnerModel> getAllList(){
		
		String sql="FROM MailOwnerModel";
		List<MailOwnerModel> list=this.getHibernateTemplate().find(sql);
		return list;
	}
	
	/**
	 * 查询发件箱模型
	 * @param bindid
	 * @return
	 */
	public MailOwnerModel getOwnerModel(Long bindid){
		MailOwnerModel model = null;
		String sql="FROM MailOwnerModel WHERE bindId = ?";
		Object[] value = {bindid};
		List<MailOwnerModel> list=this.getHibernateTemplate().find(sql,value);
		if(list!=null&&list.size()>0){
			model = list.get(0);
		}
		return model;
	}
	
	
}
