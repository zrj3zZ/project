package com.iwork.plugs.cms.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.km.know.model.IworkKnowQuestion;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.iwork.plugs.cms.model.IworkCmsPortlet;

/**
 * CMS内容管理数据库操作类
 * @author WeiGuangjian
 *
 */
public class CmsInfoDAO extends HibernateDaoSupport{

    public CmsInfoDAO(){
		
	}
    
    /**
     * 获取栏目所属内容列表
     * @param channelid
     * @return
     */
    public List<IworkCmsContent> getCmsList(Long portaletId){
		String sql="FROM "+IworkCmsContent.DATABASE_ENTITY+" WHERE CHANNELID=? ORDER BY ID";
		Object[] value = {portaletId};
	    List<IworkCmsContent> list =  this.getHibernateTemplate().find(sql,value); 
		return list;
	}
    /**
     * 获取内部讨论、新闻、公告列表
     * @param channelid
     * @yanglianfeng
     * @return
     */
    public List<IworkCmsContent> getCmsList(Long portaletId,String releaseman,String title,String releasedate,String enddate){
    	StringBuffer sql = new StringBuffer();
    	DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
    	sql.append("FROM "+IworkCmsContent.DATABASE_ENTITY+" WHERE CHANNELID=?");
    	UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    	if(uc!=null && !"".equals(uc)){
    		Long userType = uc.get_userModel().getUsertype();
        	if(userType != 1){
        		sql.append(" and RELEASEMAN = ?");
        	}
    	}
    	
    	if(releaseman!=null&&!"".equals(releaseman)){
    		if(d.HasInjectionData(releaseman)){
				return l;
			}
    		sql.append(" and RELEASEMAN like ?");
    	}
    	if(title!=null&&!"".equals(title)){
    		if(d.HasInjectionData(title)){
				return l;
			}
    		sql.append(" and TITLE like ?");
    	}
    	if(releasedate!=null&&!"".equals(releasedate)){
    		if(d.HasInjectionData(releasedate)){
				return l;
			}
    		sql.append(" and RELEASEDATE>= to_date(?,'yyyy-mm-dd') " );
    	}
    	if(enddate!=null&&!"".equals(enddate)){
    		if(d.HasInjectionData(enddate)){
				return l;
			}
    		sql.append(" and RELEASEDATE<= to_date(?,'yyyy-mm-dd') " );
    	}
    	String buff=sql.toString();
    	
    	Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		Query query=session.createQuery(buff);
		int i=0;
		query.setLong(i, portaletId);i++;
		if(uc!=null && !"".equals(uc)){
    		Long userType = uc.get_userModel().getUsertype();
        	if(userType != 1){
        		query.setString(i, UserContextUtil.getInstance().getCurrentUserFullName());i++;
        	}
    	}
		if(releaseman!=null&&!"".equals(releaseman)){
			query.setString(i, releaseman.toUpperCase());i++;
		}
		if(title!=null&&!"".equals(title)){
			query.setString(i, title);i++;
		}
		if(releasedate!=null&&!"".equals(releasedate)){
			query.setString(i, releasedate);i++;
		}
		if(enddate!=null&&!"".equals(enddate)){
			query.setString(i, enddate);i++;
		}
    	
    	List<IworkCmsContent> list =query.list(); 
		return list;
	}
    /**
     * 获取新闻列表
     * @param channelid
     * @return
     */
    public List<IworkCmsContent> getNewsList(Long portaletId){
		String sql="FROM "+IworkCmsContent.DATABASE_ENTITY+" WHERE CHANNELID=? and status=0 ORDER BY marktop desc,releasedate desc";
		Object[] value = {portaletId};
	    List<IworkCmsContent> list =  this.getHibernateTemplate().find(sql,value);
		return list;
	}
    
    /**
     * 获取分页栏目所属内容列表
     * @param channelid
     * @param pageSize
     * @param startRow
     * @return
     */
    public List<IworkCmsContent> getDataList(Long channelid,int pageSize,int startRow){
		String sql="FROM "+IworkCmsContent.DATABASE_ENTITY+" WHERE CHANNELID=? ORDER BY ID";
		Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		Query query=session.createQuery(sql);
		query.setLong(0, channelid);
		query.setFirstResult(startRow);
		query.setMaxResults(pageSize);
		List<IworkCmsContent> list =query.list();
		return list;
	}
    
    /**
     * 获取分页栏目所属内容列表
     * @param channelid
     * @param pageSize
     * @param startRow
     * @return
     */
    public List<IworkCmsContent> getDataList(Long channelid,int pageSize,int startRow,String releaseman,String title,String releasedate,String enddate){
    	StringBuffer sql = new StringBuffer();
    	DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
    	sql.append("FROM "+IworkCmsContent.DATABASE_ENTITY+" WHERE CHANNELID=?");
    	if(releaseman!=null&&!"".equals(releaseman)){
    		if(d.HasInjectionData(releaseman)){
				return l;
			}
    		sql.append(" and RELEASEMAN like ?");
    	}
    	if(title!=null&&!"".equals(title)){
    		if(d.HasInjectionData(title)){
				return l;
			}
    		sql.append(" and TITLE like ?");
    	}
    	if(releasedate!=null&&!"".equals(releasedate)){
    		if(d.HasInjectionData(releasedate)){
				return l;
			}
    		sql.append(" and RELEASEDATE>= to_date(?,'yyyy-mm-dd') " );
    	}
    	if(enddate!=null&&!"".equals(enddate)){
    		if(d.HasInjectionData(enddate)){
				return l;
			}
    		sql.append(" and RELEASEDATE<= to_date(?,'yyyy-mm-dd') " );
    	}
    	String buff=sql.toString();
		Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		Query query=session.createQuery(buff);
		int i=0;
		query.setLong(i, channelid);i++;
		if(releaseman!=null&&!"".equals(releaseman)){
			query.setString(i, releaseman.toUpperCase());i++;
		}
		if(title!=null&&!"".equals(title)){
			query.setString(i, title);i++;
		}
		if(releasedate!=null&&!"".equals(releasedate)){
			query.setString(i, releasedate);i++;
		}
		if(enddate!=null&&!"".equals(enddate)){
			query.setString(i, enddate);i++;
		}
		query.setFirstResult(startRow);
		query.setMaxResults(pageSize);
		List<IworkCmsContent> list =query.list();
		return list;
	}
    /**
     * 获取分页栏目所属内容列表
     * @param channelid
     * @param pageSize
     * @param startRow
     * @return
     */
    public List<IworkCmsContent> getContentList(Long channelid,int pageSize,int startRow){
    	String sql="FROM "+IworkCmsContent.DATABASE_ENTITY+" WHERE  status=0 and CHANNELID=? ORDER BY marktop desc,releasedate desc";
    	Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
    	Query query=session.createQuery(sql); 
    	query.setLong(0, channelid);
    	query.setFirstResult(startRow);
    	query.setMaxResults(pageSize);
    	List<IworkCmsContent> list =query.list();
    	return list;
    }
    
    /**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public IworkCmsContent getBoData(Long id) {
		IworkCmsContent model = (IworkCmsContent)this.getHibernateTemplate().get(IworkCmsContent.class,id);			
		return model;
	}
	
	/**
	 * 更新
	 * @param model
	 */
	public void updateBoData(IworkCmsContent model) {
		this.getHibernateTemplate().update(model);
	}
	
	/**
	 * 插入
	 * @param model
	 */
	public void addBoData(IworkCmsContent model) {
		this.getHibernateTemplate().save(model);	
	}
	
	/**
	 * 删除
	 * @param model
	 */
	public void deleteBoData(IworkCmsContent model) {
		this.getHibernateTemplate().delete(model);	
	}
	
	/**
	 * 获取最近一条数据记录ID
	 * @return
	 */
	public String getMaxID() {
		String sql="SELECT MAX(id) FROM "+IworkCmsContent.DATABASE_ENTITY;
		String noStr = null;
		List ll = (List) this.getHibernateTemplate().find(sql);
		Iterator itr = ll.iterator();
		if (itr.hasNext()) {
			Object noint = itr.next();
            if(noint == null){
    			noStr = "0";            	
            }else{
    			noStr = noint.toString();
            }
		}
		return noStr;
	}
	
	/**
	 * 获得未解决的知道列表
	 * @param keyword
	 * @param bcid
	 * @param pageNow
	 * @param pageSize
	 * @return
	 */
	public List<IworkKnowQuestion> searchUnsolvedQuestionList(String keyword, Long bcid, int pageNow, int pageSize){
	    String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where q.qtype=0";
	    if ((bcid != null) && (bcid.longValue() != 0L)) {
	      hql = hql + " and c.id=?";
	    }
	    DBUtilInjection d=new DBUtilInjection();
		List lis=new ArrayList();
	    if ((keyword != null) && (!"".equals(keyword))){
	      hql = hql + " and ( ";
	      keyword = keyword.toUpperCase();
	      String[] keys = keyword.split(" ");
	      String[] arrayOfString1;
	      int j = (arrayOfString1 = keys).length;
	      for (int i = 0; i < j; i++){
	        String key = arrayOfString1[i];
	        if ((key != null) && (!"".equals(key.trim()))) {
	        
	    			if(d.HasInjectionData(key)){
	    				return lis;
	    			}
	    		
	          hql = hql + " UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
	        }
	      }
	      hql = hql.substring(0, hql.length() - 2) + " )";
	    }
	    hql = hql + " order by q.qbegintime desc";
	    Session session = getSession();
	    Query query = session.createQuery(hql);
	    
	    int i=0;
	    if((bcid != null) && (bcid.longValue() != 0L)){
	    	query.setLong(i,  bcid);
	    	i++;
	    }
	    if ((keyword != null) && (!"".equals(keyword))){
	    	keyword = keyword.toUpperCase();
	    	String[] keys = keyword.split(" ");
	    	String[] arrayOfString1;
	    	int j = (arrayOfString1 = keys).length;
	    	for (int l = 0; l < j; l++){
	    		String key = arrayOfString1[l];
	    		if ((key != null) && (!"".equals(key.trim()))) {
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    		}
	    	}
	    }
	    
	    
	    if ((pageNow > 0) && (pageSize > 0))
	    {
	      query.setFirstResult((pageNow - 1) * pageSize);
	      query.setMaxResults(pageSize);
	    }
	    Object list = query.list();
	    session.close();
	    return (List<IworkKnowQuestion>)list;
	  }
	  
	/**
	 * 获得已解决的知道列表
	 * @param keyword
	 * @param bcid
	 * @param pageNow
	 * @param pageSize
	 * @return
	 */
	  public List<IworkKnowQuestion> searchSolvedQuestionList(String keyword, Long bcid, int pageNow, int pageSize)
	  {
		DBUtilInjection d=new DBUtilInjection();
		List lis=new ArrayList(); 
	    String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where q.qtype=1";
	    if ((bcid != null) && (bcid.longValue() != 0L)) {
	      hql = hql + " and c.id=?";
	    }
	    if ((keyword != null) && (!"".equals(keyword)))
	    {
	      hql = hql + " and ( ";
	      keyword = keyword.toUpperCase();
	      String[] keys = keyword.split(" ");
	      String[] arrayOfString1;
	      int j = (arrayOfString1 = keys).length;
	      for (int i = 0; i < j; i++)
	      {
	        String key = arrayOfString1[i];
	        if ((key != null) && (!"".equals(key.trim()))) {
	        	if(d.HasInjectionData(key)){
    				return lis;
    			}
	          hql = hql + " UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
	        }
	      }
	      hql = hql.substring(0, hql.length() - 2) + " )";
	    }
	    hql = hql + " order by q.qbegintime desc";
	    Session session = getSession();
	    Query query = session.createQuery(hql);
	    
	    
	    int i=0;
	    if((bcid != null) && (bcid.longValue() != 0L)){
	    	query.setLong(i,  bcid);
	    	i++;
	    }
	    if ((keyword != null) && (!"".equals(keyword))){
	    	keyword = keyword.toUpperCase();
	    	String[] keys = keyword.split(" ");
	    	String[] arrayOfString1;
	    	int j = (arrayOfString1 = keys).length;
	    	for (int l = 0; l < j; l++){
	    		String key = arrayOfString1[l];
	    		if ((key != null) && (!"".equals(key.trim()))) {
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    		}
	    	}
	    }
	    
	    
	    if ((pageNow > 0) && (pageSize > 0))
	    {
	      query.setFirstResult((pageNow - 1) * pageSize);
	      query.setMaxResults(pageSize);
	    }
	    Object list = query.list();
	    session.close();
	    return (List<IworkKnowQuestion>)list;
	  }
	  
	  /**
	   * 获取问我的问题列表
	   * @param keyword
	   * @param bcid
	   * @param pageNow
	   * @param pageSize
	   * @param user
	   * @return
	   */
	  public List<IworkKnowQuestion> searchAskMeQuestionList(String keyword, Long bcid, int pageNow, int pageSize, OrgUser user)
	  {
		DBUtilInjection d=new DBUtilInjection();
		List lis=new ArrayList(); 
	    String myName = user.getUserid() + "[" + user.getUsername() + "]";
	    String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where q.qtype<>2 and ( q.answerbody like ? or a.invitedman like ? )";
	    if ((bcid != null) && (bcid.longValue() != 0L)) {
	      hql = hql + " and c.id=?";
	    }
	    if ((keyword != null) && (!"".equals(keyword)))
	    {
	      hql = hql + " and ( ";
	      keyword = keyword.toUpperCase();
	      String[] keys = keyword.split(" ");
	      String[] arrayOfString1;
	      int j = (arrayOfString1 = keys).length;
	      for (int i = 0; i < j; i++)
	      {
	        String key = arrayOfString1[i];
	        if ((key != null) && (!"".equals(key.trim()))) {
	        	if(d.HasInjectionData(key)){
    				return lis;
    			}
	          hql = hql + " UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
	        }
	      }
	      hql = hql.substring(0, hql.length() - 2) + " )";
	    }
	    hql = hql + " order by q.qbegintime desc";
	    Session session = getSession();
	    Query query = session.createQuery(hql);
	    
	    
	    int i=0;
	    query.setString(i, myName);i++;
	    query.setString(i, myName);i++;
	    if((bcid != null) && (bcid.longValue() != 0L)){
	    	query.setLong(i,  bcid);
	    	i++;
	    }
	    if ((keyword != null) && (!"".equals(keyword))){
	    	keyword = keyword.toUpperCase();
	    	String[] keys = keyword.split(" ");
	    	String[] arrayOfString1;
	    	int j = (arrayOfString1 = keys).length;
	    	for (int l = 0; l < j; l++){
	    		String key = arrayOfString1[l];
	    		if ((key != null) && (!"".equals(key.trim()))) {
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    		}
	    	}
	    }
	    
	    
	    if ((pageNow > 0) && (pageSize > 0))
	    {
	      query.setFirstResult((pageNow - 1) * pageSize);
	      query.setMaxResults(pageSize);
	    }
	    Object list = query.list();
	    session.close();
	    return (List<IworkKnowQuestion>)list;
	  }
	  
	  /**
	   * 获得我的提问列表
	   * @param keyword
	   * @param bcid
	   * @param pageNow
	   * @param pageSize
	   * @param uid
	   * @return
	   */
	  public List<IworkKnowQuestion> searchMyAskQuestionList(String keyword, Long bcid, int pageNow, int pageSize, String uid)
	  { 
		DBUtilInjection d=new DBUtilInjection();
		List lis=new ArrayList(); 
	    String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where q.qtype<>2 and q.quid=?";
	    if ((bcid != null) && (bcid.longValue() != 0L)) {
	      hql = hql + " and c.id=?";
	    }
	    if ((keyword != null) && (!"".equals(keyword)))
	    {
	      hql = hql + " and ( ";
	      keyword = keyword.toUpperCase();
	      String[] keys = keyword.split(" ");
	      String[] arrayOfString1;
	      int j = (arrayOfString1 = keys).length;
	      for (int i = 0; i < j; i++)
	      {
	        String key = arrayOfString1[i];
	        if ((key != null) && (!"".equals(key.trim()))) {
	        	if(d.HasInjectionData(key)){
    				return lis;
    			}
	          hql = hql + " UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
	        }
	      }
	      hql = hql.substring(0, hql.length() - 2) + " )";
	    }
	    hql = hql + " order by q.qbegintime desc";
	    Session session = getSession();
	    Query query = session.createQuery(hql);
	    
	    
	    int i=0;
	    query.setString(i, uid);i++;
	    if((bcid != null) && (bcid.longValue() != 0L)){
	    	query.setLong(i,  bcid);
	    	i++;
	    }
	    if ((keyword != null) && (!"".equals(keyword))){
	    	keyword = keyword.toUpperCase();
	    	String[] keys = keyword.split(" ");
	    	String[] arrayOfString1;
	    	int j = (arrayOfString1 = keys).length;
	    	for (int l = 0; l < j; l++){
	    		String key = arrayOfString1[l];
	    		if ((key != null) && (!"".equals(key.trim()))) {
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    		}
	    	}
	    }
	    
	    
	    if ((pageNow > 0) && (pageSize > 0))
	    {
	      query.setFirstResult((pageNow - 1) * pageSize);
	      query.setMaxResults(pageSize);
	    }
	    Object list = query.list();
	    session.close();
	    return (List<IworkKnowQuestion>)list;
	  }
	  
	  /**
	   * 获得我的回答列表
	   * @param keyword
	   * @param bcid
	   * @param pageNow
	   * @param pageSize
	   * @param uid
	   * @return
	   */
	  public List<IworkKnowQuestion> searchMyAnswerQuestionList(String keyword, Long bcid, int pageNow, int pageSize, String uid)
	  {
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where q.qtype<>2 and a.auid=?";
	    if ((bcid != null) && (bcid.longValue() != 0L)) {
	      hql = hql + " and c.id=?";
	    }
	    if ((keyword != null) && (!"".equals(keyword)))
	    {
	      hql = hql + " and ( ";
	      keyword = keyword.toUpperCase();
	      String[] keys = keyword.split(" ");
	      String[] arrayOfString1;
	      int j = (arrayOfString1 = keys).length;
	      for (int i = 0; i < j; i++)
	      {
	        String key = arrayOfString1[i];
	        if ((key != null) && (!"".equals(key))) {
	        	if(d.HasInjectionData(key)){
    				return lis;
    			}
	          hql = hql + " UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
	        }
	      }
	      hql = hql.substring(0, hql.length() - 2) + " )";
	    }
	    hql = hql + " order by q.qbegintime desc";
	    Session session = getSession();
	    Query query = session.createQuery(hql);
	    
	    
	    int i=0;
	    query.setString(i, uid);i++;
	    if((bcid != null) && (bcid.longValue() != 0L)){
	    	query.setLong(i,  bcid);
	    	i++;
	    }
	    if ((keyword != null) && (!"".equals(keyword))){
	    	keyword = keyword.toUpperCase();
	    	String[] keys = keyword.split(" ");
	    	String[] arrayOfString1;
	    	int j = (arrayOfString1 = keys).length;
	    	for (int l = 0; l < j; l++){
	    		String key = arrayOfString1[l];
	    		if ((key != null) && (!"".equals(key.trim()))) {
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    			query.setString(i, key);i++;
	    		}
	    	}
	    }
	    
	    
	    if ((pageNow > 0) && (pageSize > 0))
	    {
	      query.setFirstResult((pageNow - 1) * pageSize);
	      query.setMaxResults(pageSize);
	    }
	    Object list = query.list();
	    session.close();
	    return (List<IworkKnowQuestion>)list;
	  }
	  
	  /**
	   * 获得所有开启的咨询频道
	   * @return
	   */
	  public List<IworkCmsPortlet> getOpenPortletList(){
		  UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		  if(uc == null) return null;
			OrgUser user = uc.get_userModel();
			String sql="";
			if(user.getOrgroleid()==5){
				  sql="FROM "+IworkCmsPortlet.DATABASE_ENTITY+" WHERE portletname='市场新闻' ORDER BY id desc";
			}else{
				  sql="FROM "+IworkCmsPortlet.DATABASE_ENTITY+" WHERE portlettype=0 and status=0 ORDER BY id desc";
			}
		 
	    	Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
	    	Query query=session.createQuery(sql); 
	    	List<IworkCmsPortlet> list =query.list();
	    	return list;
	  }
}
