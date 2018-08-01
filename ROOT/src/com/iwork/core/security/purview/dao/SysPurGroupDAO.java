package com.iwork.core.security.purview.dao;

import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.security.purview.model.SysPurGroup;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SysPurGroupDAO extends HibernateDaoSupport
{
  public List<SysPurGroup> search(String searchKey)
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" FROM ").append(SysPurGroup.DATABASE_ENTITY).append(" WHERE groupname like :searchkey  ORDER BY ORDERINDEX");
    final String sql1 = sql.toString();
    final String searchkey = "%" + searchKey + "%";
    List list = getHibernateTemplate().executeFind(new HibernateCallback()
    {
      public Object doInHibernate(Session session) throws HibernateException, SQLException
      {
        Query query = session.createQuery(sql1);
        query.setString("searchkey", searchkey);
        return query.list();
      }
    });
    return list;
  }

  public void addBoData(SysPurGroup model)
  {
    getHibernateTemplate().save(model);
    StringBuffer log = new StringBuffer();
    log.append("权限组ID:").append(model.getId()).append(",权限组名称:").append(model.getGroupname());
    LogUtil.getInstance().addLog(model.getId(), "【权限组新增】新增了一个的权限组", log.toString());
  }

  public void deleteBoData(SysPurGroup model)
  {
    getHibernateTemplate().delete(model);
    StringBuffer log = new StringBuffer();
    log.append("权限组ID:").append(model.getId()).append(",权限组名称:").append(model.getGroupname());
    LogUtil.getInstance().addLog(model.getId(), "【权限组删除】删除了一个的权限组", log.toString());
  }

  public List<String> getCategoryList()
  {
    List list = new ArrayList();
    String sql = "select distinct CATEGORYNAME from SysPurGroup order by CATEGORYNAME";
    Connection conn = DBUtil.open();
    Statement stat = null;
    ResultSet rest = null;
    try {
      stat = conn.createStatement();
      rest = stat.executeQuery(sql);
      while (rest.next()) {
        String temp = rest.getString("CATEGORYNAME");
        if (temp != null)
          list.add(temp);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    } finally {
      DBUtil.close(conn, stat, rest);
    }

    return list;
  }

  public List getAll()
  {
    String sql = "FROM " + SysPurGroup.DATABASE_ENTITY + " ORDER BY ORDERINDEX";
    return getHibernateTemplate().find(sql);
  }

  public int getRows()
  {
    String sql = "FROM " + SysPurGroup.DATABASE_ENTITY;
    List list = getHibernateTemplate().find(sql);
    return list.size();
  }

  public SysPurGroup getBoData(Long id)
  {
    if (id == null) id = new Long(0L);
    return (SysPurGroup)getHibernateTemplate().get(SysPurGroup.class, id);
  }

  public String getMaxID()
  {
    String date = UtilDate.getNowdate();
    String sql = "SELECT MAX(id)+1 FROM " + SysPurGroup.DATABASE_ENTITY;
    String noStr = null;
    List ll = getHibernateTemplate().find(sql);
    Iterator itr = ll.iterator();
    if (itr.hasNext()) {
      Object noint = itr.next();
      if (noint == null)
        noStr = "1";
      else {
        noStr = noint.toString();
      }
    }

    if (noStr.length() == 1)
      noStr = "000" + noStr;
    else if (noStr.length() == 2)
      noStr = "00" + noStr;
    else if (noStr.length() == 3)
      noStr = "0" + noStr;
    else {
      noStr = noStr;
    }
    return noStr;
  }

  public void updateBoData(SysPurGroup pd)
  {
    getHibernateTemplate().update(pd);
    StringBuffer log = new StringBuffer();
    log.append("权限组ID:").append(pd.getId()).append(",权限组名称:").append(pd.getGroupname());
    LogUtil.getInstance().addLog(pd.getId(), "【权限组更新】更新了一个的权限组", log.toString());
  }

  public List getBoDatas(int pageSize, int startRow) throws HibernateException
  {
    final int pageSize1 = pageSize;
    final int startRow1 = startRow;
    return getHibernateTemplate().executeFind(new HibernateCallback()
    {
      public List doInHibernate(Session session) throws HibernateException, SQLException {
        Query query = session.createQuery(" FROM " + SysPurGroup.DATABASE_ENTITY + " ORDER BY ORDERINDEX");
        query.setFirstResult(startRow1);
        query.setMaxResults(pageSize1);
        return query.list();
      }
    });
  }

  public List<SysPurGroup> getSysPurGroupByCategory(String categoryname) {
    String sql = "";
    Object[] values = null;
    List l =new ArrayList();
    if (categoryname == null) {
      sql = "FROM " + SysPurGroup.DATABASE_ENTITY;
      sql = sql + " where 1=1 ";
    } else {
      sql = "FROM " + SysPurGroup.DATABASE_ENTITY;
      sql = sql + " WHERE categoryname = ? ";
      l.add(categoryname);
    }
    sql = sql + "  ORDER BY ORDERINDEX";
    values = new Object[l.size()];
    for (int i = 0; i < l.size(); i++) {
		values[i]=l.get(i);
	}
    return getHibernateTemplate().find(sql,values);
  }

  public int updateIndex(int id, String type)
  {
    int index = 0;
    SysPurGroup sns1 = null;
    SysPurGroup sns2 = null;
    String temp = "";
    String sql = "FROM " + SysPurGroup.DATABASE_ENTITY + " WHERE ID =" + id;
    List downlist = getHibernateTemplate().find(sql);
    if ((downlist != null) && 
      (downlist.size() > 0)) {
      sns1 = (SysPurGroup)downlist.get(0);
    }

    if (type.equals("up"))
      sql = "FROM " + SysPurGroup.DATABASE_ENTITY + " WHERE orderindex <" + sns1.getOrderindex() + " order by orderindex desc";
    else
      sql = "FROM " + SysPurGroup.DATABASE_ENTITY + " WHERE orderindex >" + sns1.getOrderindex() + " order by orderindex asc";
    List uplist = getHibernateTemplate().find(sql);
    if ((uplist != null) && 
      (uplist.size() > 0)) {
      sns2 = (SysPurGroup)uplist.get(0);
    }

    if ((sns1 != null) && (sns2 != null)) {
      temp = sns1.getOrderindex();
      sns1.setOrderindex(sns2.getOrderindex());

      updateBoData(sns1);
      sns2.setOrderindex(temp);
      updateBoData(sns2);
    }
    return index;
  }
}