package com.iwork.km.know.dao;

import com.iwork.core.organization.model.OrgUser;
import com.iwork.km.know.model.IworkKnowAnswer;
import com.iwork.km.know.model.IworkKnowClasses;
import com.iwork.km.know.model.IworkKnowHoldoutLog;
import com.iwork.km.know.model.IworkKnowKeywordLog;
import com.iwork.km.know.model.IworkKnowQuestion;
import com.iwork.km.know.model.IworkKnowTagLog;
import com.iwork.km.know.model.IworkKnowTalk;
import com.iwork.km.know.model.IworkKnowUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class KnowDAO extends HibernateDaoSupport
{
  public List<IworkKnowClasses> getClassList(int type)
  {
    List list = new ArrayList();
    StringBuffer hql = new StringBuffer("from IworkKnowClasses ");
    hql.append(" where ctype='可用'");
    hql.append(" order by corder");
    list = getHibernateTemplate().find(hql.toString());
    return list;
  }

  public List<IworkKnowClasses> getClassList(int pageSize, int startRow)
  {
    List list = new ArrayList();
    String hql = "from IworkKnowClasses order by corder";
    Session session = getSession();
    Query query = session.createQuery(hql);
    query.setFirstResult(startRow);
    query.setMaxResults(pageSize);
    list = query.list();
    session.close();
    return list;
  }

  public IworkKnowClasses getClassModel(Long id)
  {
    IworkKnowClasses model = null;
    model = (IworkKnowClasses)getHibernateTemplate().get(IworkKnowClasses.class, id);
    return model;
  }

  public void updateBigClassModel(IworkKnowClasses model)
  {
    getHibernateTemplate().update(model);
  }

  public void saveBigClassModel(IworkKnowClasses model)
  {
    getHibernateTemplate().save(model);
  }

  public long getNextCorder()
  {
    long next = 1L;
    String hql = "SELECT MAX(corder)+1 FROM IworkKnowClasses";
    List list = getHibernateTemplate().find(hql);
    if ((list != null) && (list.size() != 0)) {
      Object obj = list.get(0);
      if (obj != null) {
        next = Long.parseLong(obj.toString());
      }
    }
    return next;
  }

  public void updateCorder(String type, Long id)
  {
    IworkKnowClasses snd1 = null;
    IworkKnowClasses snd2 = null;
    Long temp = new Long(0L);
    snd1 = getClassModel(id);
    String sql = "";
    if (type.equals("up"))
      sql = " FROM IworkKnowClasses WHERE  corder < " + snd1.getCorder() + " order by corder desc";
    else if (type.equals("down")) {
      sql = " FROM IworkKnowClasses WHERE  corder > " + snd1.getCorder() + " order by corder asc";
    }
    else if (type.equals("top"))
      sql = " FROM IworkKnowClasses WHERE  corder < " + snd1.getCorder() + " order by corder asc";
    else if (type.equals("bottom")) {
      sql = " FROM IworkKnowClasses WHERE  corder > " + snd1.getCorder() + " order by corder desc";
    }

    List list = getHibernateTemplate().find(sql);
    if ((list != null) && 
      (list.size() > 0)) {
      snd2 = (IworkKnowClasses)list.get(0);
    }

    if ((snd1 != null) && (snd2 != null))
    {
      if (snd1.getCorder() != snd2.getCorder()) {
        temp = snd1.getCorder();
        snd1.setCorder(snd2.getCorder());

        getHibernateTemplate().update(snd1);
        snd2.setCorder(temp);

        getHibernateTemplate().update(snd2);
      }
    }
  }

  public String autoGetUserInfo(String term)
  {
    List blist = new ArrayList();
    String hql = "from OrgUser where userid like ? ";
    Object[] values={"%" + term + "%"};
    List<OrgUser> list = getHibernateTemplate().find(hql,values);
    if (list != null)
      for (OrgUser model : list) {
        Map map = new HashMap();
        if (model != null) {
          map.put("userId", model.getUserid());
          map.put("userName", model.getUsername());
          blist.add(map);
        }
      }
    JSONArray json = JSONArray.fromObject(blist);
    return json.toString();
  }

  public void updateQuestionModel(IworkKnowQuestion model)
  {
    getHibernateTemplate().update(model);
  }

  public void saveQuestionModel(IworkKnowQuestion model)
  {
    getHibernateTemplate().save(model);
  }

  public List<IworkKnowQuestion> searchUnsolvedQuestionList(String keyword, Long bcid, int pageNow, int pageSize)
  {
    String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where q.qtype=0";
    if ((bcid != null) && (bcid.longValue() != 0L)) {
      hql = hql + " and c.id=" + bcid;
    }
    if ((keyword != null) && (!"".equals(keyword))) {
      hql = hql + " and ( ";
      keyword = keyword.toUpperCase();
      String[] keys = keyword.split(" ");
      for (String key : keys) {
        if ((key != null) && (!"".equals(key.trim())))
          hql = hql + " UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
      }
      hql = hql.substring(0, hql.length() - 2) + " )";
    }
    hql = hql + " order by q.qbegintime desc";
    Session session = getSession();
    Query query = session.createQuery(hql);
    
    if ((keyword != null) && (!"".equals(keyword))) {
        keyword = keyword.toUpperCase();
        String[] keys = keyword.split(" ");
        for (String key : keys) {
          if ((key != null) && (!"".equals(key.trim())))
          	query.setString(0, "%"+key+"%");
          	query.setString(1, "%"+key+"%");
          	query.setString(2, "%"+key+"%");
          	query.setString(3, "%"+key+"%");
        }
      }
    
    if ((pageNow > 0) && (pageSize > 0)) {
      query.setFirstResult((pageNow - 1) * pageSize);
      query.setMaxResults(pageSize);
    }
    List list = query.list();
    session.close();
    return list;
  }

  public List<IworkKnowQuestion> searchSolvedQuestionList(String keyword, Long bcid, int pageNow, int pageSize)
  {
    String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where q.qtype=1";
    if ((bcid != null) && (bcid.longValue() != 0L)) {
      hql = hql + " and c.id=" + bcid;
    }
    if ((keyword != null) && (!"".equals(keyword))) {
      hql = hql + " and ( ";
      keyword = keyword.toUpperCase();
      String[] keys = keyword.split(" ");
      for (String key : keys) {
        if ((key != null) && (!"".equals(key.trim())))
          hql = hql + " UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
      }
      hql = hql.substring(0, hql.length() - 2) + " )";
    }
    hql = hql + " order by q.qbegintime desc";
    Session session = getSession();
    Query query = session.createQuery(hql);
   
    if ((keyword != null) && (!"".equals(keyword))) {
        keyword = keyword.toUpperCase();
        String[] keys = keyword.split(" ");
        for (String key : keys) {
          if ((key != null) && (!"".equals(key.trim())))
        	query.setString(0, "%"+key+"%");
        	query.setString(1, "%"+key+"%");
        	query.setString(2, "%"+key+"%");
        	query.setString(3, "%"+key+"%");
        }
      }
    if ((pageNow > 0) && (pageSize > 0)) {
      query.setFirstResult((pageNow - 1) * pageSize);
      query.setMaxResults(pageSize);
    }
    List list = query.list();
    session.close();
    return list;
  }

  public List<IworkKnowQuestion> getAllQuestionList()
  {
    String sql = "FROM IworkKnowQuestion order by id";
    return getHibernateTemplate().find(sql);
  }

  /**
	 * 获取问题列表 （已解决和未解决）
	 * @param keyword
	 * @param bcid
	 * @param pageNow   
	 * @param pageSize
	 * @return
	 */
	public List<IworkKnowQuestion> searchUnsolvedQuestionListAll(String keyword,Long bcid,int pageNow,int pageSize){
		String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where (q.qtype=0 or q.qtype=1)";
		if(bcid!=null && bcid!=0){
			hql = hql+" and c.id="+bcid;
		}
		if(keyword!=null && !"".equals(keyword)){
			hql = hql+" and ( ";
			keyword = keyword.toUpperCase();
			String[] keys = keyword.split(" ");
			for(String key:keys){
				if(key==null || "".equals(key.trim()))continue;
				hql = hql+" UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
			}
			hql = hql.substring(0,hql.length()-2)+" )";
		}
		hql = hql+" order by q.qbegintime desc";
		Session session = this.getSession();	
		Query query = session.createQuery(hql);
		if(keyword!=null && !"".equals(keyword)){
			keyword = keyword.toUpperCase();
			String[] keys = keyword.split(" ");
			for(String key:keys){
				if(key==null || "".equals(key.trim()))continue;
				query.setString(0, "%"+key+"%");
				query.setString(1, "%"+key+"%");
				query.setString(2, "%"+key+"%");
				query.setString(3, "%"+key+"%");
			}
		}
		if(pageNow>0 && pageSize>0){
			query.setFirstResult((int)(pageNow-1)*pageSize);
		    query.setMaxResults(pageSize);
		}	    
		List<IworkKnowQuestion> list = query.list();
		session.close();
		return list;
	}
	
	/**
	 * 获取问题列表 （已解决和未解决）
	 * @param keyword
	 * @param bcid
	 * @param pageNow
	 * @param pageSize
	 * @return
	 */
	public List<IworkKnowQuestion> searchSolvedQuestionListAll(String keyword,Long bcid,int pageNow,int pageSize){
		String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where (q.qtype=1 or q.qtype=0)";
		if(bcid!=null && bcid!=0){
			hql = hql+" and c.id="+bcid;
		}
		if(keyword!=null && !"".equals(keyword)){
			hql = hql+" and ( ";
			keyword = keyword.toUpperCase();
			String[] keys = keyword.split(" ");
			for(String key:keys){
				if(key==null || "".equals(key.trim()))continue;
				hql = hql+" UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
			}
			hql = hql.substring(0,hql.length()-2)+" )";
		}
		hql = hql+" order by q.qbegintime desc";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		if(keyword!=null && !"".equals(keyword)){
			keyword = keyword.toUpperCase();
			String[] keys = keyword.split(" ");
			for(String key:keys){
				if(key==null || "".equals(key.trim()))continue;
				query.setString(0, "%"+key+"%");
				query.setString(1, "%"+key+"%");
				query.setString(2, "%"+key+"%");
				query.setString(3, "%"+key+"%");
			}
		}
		if(pageNow>0 && pageSize>0){
			query.setFirstResult((int)(pageNow-1)*pageSize);
		    query.setMaxResults(pageSize);
		}	    
		List<IworkKnowQuestion> list = query.list();
		session.close();
		return list;
	}
  
  public List<IworkKnowQuestion> searchMyAskQuestionList(String keyword, Long bcid, int pageNow, int pageSize, String uid)
  {
    String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where q.qtype<>2 and q.quid= ? ";
    if ((bcid != null) && (bcid.longValue() != 0L)) {
      hql = hql + " and c.id=" + bcid;
    }
    if ((keyword != null) && (!"".equals(keyword))) {
      hql = hql + " and ( ";
      keyword = keyword.toUpperCase();
      String[] keys = keyword.split(" ");
      for (String key : keys) {
        if ((key != null) && (!"".equals(key.trim())))
          hql = hql + " UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
      }
      hql = hql.substring(0, hql.length() - 2) + " )";
    }
    hql = hql + " order by q.qbegintime desc";
    Session session = getSession();
    Query query = session.createQuery(hql);
    query.setString(0, uid);
    if(keyword!=null && !"".equals(keyword)){
		keyword = keyword.toUpperCase();
		String[] keys = keyword.split(" ");
		for(String key:keys){
			if(key==null || "".equals(key.trim()))continue;
			query.setString(1, "%"+key+"%");
			query.setString(2, "%"+key+"%");
			query.setString(3, "%"+key+"%");
			query.setString(4, "%"+key+"%");
		}
	}
    if ((pageNow > 0) && (pageSize > 0)) {
      query.setFirstResult((pageNow - 1) * pageSize);
      query.setMaxResults(pageSize);
    }
    List list = query.list();
    session.close();
    return list;
  }

  public List<IworkKnowQuestion> searchMyAnswerQuestionList(String keyword, Long bcid, int pageNow, int pageSize, String uid)
  {
    String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where q.qtype<>2 and a.auid= ? ";
    if ((bcid != null) && (bcid.longValue() != 0L)) {
      hql = hql + " and c.id=" + bcid;
    }
    if ((keyword != null) && (!"".equals(keyword))) {
      hql = hql + " and ( ";
      keyword = keyword.toUpperCase();
      String[] keys = keyword.split(" ");
      for (String key : keys) {
        if ((key != null) && (!"".equals(key.trim())))
          hql = hql + " UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
      }
      hql = hql.substring(0, hql.length() - 2) + " )";
    }
    hql = hql + " order by q.qbegintime desc";
    Session session = getSession();
    Query query = session.createQuery(hql);
    query.setString(0, uid);
    if(keyword!=null && !"".equals(keyword)){
		keyword = keyword.toUpperCase();
		String[] keys = keyword.split(" ");
		for(String key:keys){
			if(key==null || "".equals(key.trim()))continue;
			query.setString(1, "%"+key+"%");
			query.setString(2, "%"+key+"%");
			query.setString(3, "%"+key+"%");
			query.setString(4, "%"+key+"%");
		}
	}
    if ((pageNow > 0) && (pageSize > 0)) {
      query.setFirstResult((pageNow - 1) * pageSize);
      query.setMaxResults(pageSize);
    }
    List list = query.list();
    session.close();
    return list;
  }

  public List<IworkKnowQuestion> searchAskMeQuestionList(String keyword, Long bcid, int pageNow, int pageSize, OrgUser user)
  {
    String myName = user.getUserid() + "[" + user.getUsername() + "]";
    String hql = "select distinct q from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where q.qtype<>2 and ( q.answerbody like '%" + myName + "%' or a.invitedman like '%" + myName + "%' )";
    if ((bcid != null) && (bcid.longValue() != 0L)) {
      hql = hql + " and c.id=" + bcid;
    }
    if ((keyword != null) && (!"".equals(keyword))) {
      hql = hql + " and ( ";
      keyword = keyword.toUpperCase();
      String[] keys = keyword.split(" ");
      for (String key : keys) {
        if ((key != null) && (!"".equals(key.trim())))
          hql = hql + " UPPER(q.qcontent) like ? or UPPER(q.qtags) like ? or UPPER(a.acontent) like ? or UPPER(c.cname) like ? or";
      }
      hql = hql.substring(0, hql.length() - 2) + " )";
    }
    hql = hql + " order by q.qbegintime desc";
    Session session = getSession();
    Query query = session.createQuery(hql);
    if(keyword!=null && !"".equals(keyword)){
  		keyword = keyword.toUpperCase();
  		String[] keys = keyword.split(" ");
  		for(String key:keys){
  			if(key==null || "".equals(key.trim()))continue;
  			query.setString(0, "%"+key+"%");
  			query.setString(1, "%"+key+"%");
  			query.setString(2, "%"+key+"%");
  			query.setString(3, "%"+key+"%");
  		}
  	}
    if ((pageNow > 0) && (pageSize > 0)) {
      query.setFirstResult((pageNow - 1) * pageSize);
      query.setMaxResults(pageSize);
    }
    List list = query.list();
    session.close();
    return list;
  }

  public IworkKnowQuestion getQuestionModel(Long qid)
  {
    IworkKnowQuestion model = null;
    String hql = "from IworkKnowQuestion q left join fetch q.iworkKnowClasses c left join fetch q.iworkKnowAnswers a where q.id=" + qid;
    Session session = getSession();
    Query query = session.createQuery(hql);
    List list = query.list();
    if ((list != null) && (list.size() > 0)) {
      model = (IworkKnowQuestion)list.get(0);
    }
    session.close();
    return model;
  }

  public Long getQuestionCountByCid(Long cid, String type)
  {
    Long count = new Long(0L);
    String hql = "select count(id) from IworkKnowQuestion where qtype<>2";
    if ("b".equals(type))
      hql = hql + " and qbigc=" + cid;
    else if ("l".equals(type)) {
      hql = hql + " and qletc=" + cid;
    }
    List list = getHibernateTemplate().find(hql);
    if ((list != null) || (list.size() > 0)) {
      count = (Long)list.get(0);
    }
    return count;
  }

  public String delBigClassAllInfo(Long bcid)
  {
    StringBuffer relateFileUUIDs = new StringBuffer();
    Session session = null;
    Transaction tx = null;
    try {
      session = getSession();
      tx = session.beginTransaction();
      IworkKnowClasses bigClass = (IworkKnowClasses)session.get(IworkKnowClasses.class, bcid);
      Set<IworkKnowQuestion> questions = bigClass.getIworkKnowQuestions();
      Set<IworkKnowAnswer> answers;
      for (IworkKnowQuestion question : questions)
        if ((question != null) && (question.getQtype().longValue() == 2L)) {
          answers = question.getIworkKnowAnswers();
          for (IworkKnowAnswer answer : answers)
            if (answer != null) {
              Set<IworkKnowHoldoutLog> holdlogs = answer.getIworkKnowHoldoutLogs();
              for (IworkKnowHoldoutLog holdlog : holdlogs) {
                if (holdlog != null)
                  session.delete(holdlog);
              }
              Set<IworkKnowTalk> talks = answer.getIworkKnowTalks();
              for (IworkKnowTalk talk : talks) {
                if (talk != null)
                  session.delete(talk);
              }
              String afileUUIDs = answer.getFileuuids();
              if ((afileUUIDs != null) && (!"".equals(afileUUIDs))) {
                if ("".equals(relateFileUUIDs.toString())) {
                  relateFileUUIDs.append(afileUUIDs);
                }
                else {
                  relateFileUUIDs.append(",").append(afileUUIDs);
                }
              }
              session.delete(answer);
            }
          String qfileUUIDs = question.getFileuuids();
          if ((qfileUUIDs != null) && (!"".equals(qfileUUIDs))) {
            if ("".equals(relateFileUUIDs.toString())) {
              relateFileUUIDs.append(qfileUUIDs);
            }
            else {
              relateFileUUIDs.append(",").append(qfileUUIDs);
            }
          }
          session.delete(question);
        }
      Set<IworkKnowTagLog> tagLogs = bigClass.getIworkKnowTagLogs();
      for (IworkKnowTagLog tagLog : tagLogs) {
        if (tagLog != null)
          session.delete(tagLog);
      }
      session.delete(bigClass);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) {
        tx.rollback();
      }
      e.printStackTrace();
    }
    finally {
      if (session != null) {
        session.close();
      }
    }
    return relateFileUUIDs.toString();
  }

  public IworkKnowTagLog getTagLog(Long cid, String tag)
  {
    IworkKnowTagLog model = null;
    String hql = "from IworkKnowTagLog where classid=" + cid + " and tag= ? ";
    Object[] values={tag};
    List list = getHibernateTemplate().find(hql,values);
    if ((list != null) && (list.size() > 0)) {
      model = (IworkKnowTagLog)list.get(0);
    }
    return model;
  }

  public void updateTagLogModel(IworkKnowTagLog model)
  {
    getHibernateTemplate().update(model);
  }

  public void saveTagLogModel(IworkKnowTagLog model)
  {
    getHibernateTemplate().save(model);
  }

  public IworkKnowUser getUserModel(String uid)
  {
    IworkKnowUser model = null;
    String hql = "from IworkKnowUser where suid= ? ";
    Object[] values ={uid};
    List list = getHibernateTemplate().find(hql,values);
    if ((list != null) && (list.size() > 0)) {
      model = (IworkKnowUser)list.get(0);
    }
    return model;
  }

  public void updateUserModel(IworkKnowUser model)
  {
    getHibernateTemplate().update(model);
  }

  public void saveUserModel(IworkKnowUser model)
  {
    getHibernateTemplate().save(model);
  }

  public Long getMyScoreByUid(String uid)
  {
    Long score = new Long(0L);
    IworkKnowUser model = getUserModel(uid);
    if (model != null) {
      score = model.getScore();
    }
    return score;
  }

  public List<IworkKnowUser> getTop10ScoreUserList()
  {
    String hql = "from IworkKnowUser order by score desc";
    Session session = getSession();
    Query query = session.createQuery(hql);
    query.setFirstResult(0);
    query.setMaxResults(10);
    List list = query.list();
    session.close();
    return list;
  }

  public IworkKnowAnswer getAnswerModel(Long id)
  {
    IworkKnowAnswer model = null;
    String hql = "from IworkKnowAnswer a left join fetch a.iworkKnowQuestion q  left join fetch a.iworkKnowHoldoutLogs h left join fetch a.iworkKnowTalks t  where a.id=" + id;
    Session session = getSession();
    Query query = session.createQuery(hql);
    List list = query.list();
    if ((list != null) && (list.size() > 0)) {
      model = (IworkKnowAnswer)list.get(0);
    }
    session.close();
    return model;
  }

  public List<IworkKnowAnswer> getAnswerList(Long qid)
  {
    String hql = "from IworkKnowAnswer a left join fetch a.iworkKnowQuestion q where q.id=" + qid + "order by a.atime desc";
    Session session = getSession();
    Query query = session.createQuery(hql);
    List list = query.list();

    session.close();
    return list;
  }

  public void saveAnswerModel(IworkKnowAnswer model)
  {
    getHibernateTemplate().save(model);
  }

  public void updateAnswerModel(IworkKnowAnswer model)
  {
    getHibernateTemplate().update(model);
  }

  public void saveHoldoutLogModel(IworkKnowHoldoutLog holdoutlog)
  {
    getHibernateTemplate().save(holdoutlog);
  }

  public List<IworkKnowTalk> getTalkList(Long aid)
  {
    String hql = "from IworkKnowTalk t left join fetch t.iworkKnowAnswer a where a.id=" + aid + "order by t.talktime desc";
    Session session = getSession();
    Query query = session.createQuery(hql);
    List list = query.list();
    session.close();
    return list;
  }

  public void saveTalkModel(IworkKnowTalk talk)
  {
    getHibernateTemplate().save(talk);
  }

  public IworkKnowTalk getTalkModel(Long tid)
  {
    IworkKnowTalk talk = (IworkKnowTalk)getHibernateTemplate().get(IworkKnowTalk.class, tid);
    return talk;
  }

  public void updateTalkModel(IworkKnowTalk talk)
  {
    getHibernateTemplate().update(talk);
  }

  public void updateKeywordLog(IworkKnowKeywordLog keywordLog)
  {
    getHibernateTemplate().update(keywordLog);
  }

  public void saveKeywordLog(IworkKnowKeywordLog keywordLog)
  {
    getHibernateTemplate().save(keywordLog);
  }

  public IworkKnowKeywordLog getKeywordLog(String keyword)
  {
    IworkKnowKeywordLog model = null;
    String hql = "from IworkKnowKeywordLog where keyword= ? ";
    Session session = getSession();
    Query query = session.createQuery(hql);
    query.setString(0, keyword);
    List list = query.list();
    if ((list != null) && (list.size() > 0)) {
      model = (IworkKnowKeywordLog)list.get(0);
    }
    session.close();
    return model;
  }
}