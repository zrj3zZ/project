package com.iwork.app.navigation.favorite.myfav.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.navigation.favorite.myfav.bean.Sysfavfunction;
import com.iwork.commons.util.DBUtilInjection;

/**
 * 我的收藏夹数据库操作类
 * @author WeiGuangjian
 *
 */
public class SysFavFunctionDAO extends HibernateDaoSupport{
	public SysFavFunctionDAO(){}
			
			/**
			 * 获得当前用户的信息
			 * @param uid
			 * @return
			 */
			public List getUserAll(String uid) {			 
					String sql="FROM "+Sysfavfunction.DATABASE_ENTITY+" WHERE USER_ID=? ORDER BY FUN_INDEX";
					DBUtilInjection d=new DBUtilInjection();
					List l=new ArrayList();
					if(uid!=null&&!"".equals(uid)){
						if(d.HasInjectionData(uid)){
							return l;
						}
					}
					Object[] value = {uid};
				    List list = this.getHibernateTemplate().find(sql,value);
					return list;
			}
			
			/**
			 * 删除当前用户的信息
			 * @param uid
			 * @return
			 */
			public int delMyFav(String uid,String List){			
				Session session=getSessionFactory().openSession();
				Transaction tx=session.beginTransaction();
				StringBuffer sb = new StringBuffer();
				sb.append("delete from "+Sysfavfunction.DATABASE_ENTITY+" where USER_ID=? and FUN_ID<>'0' or ( FUN_RNAME not in (");
				List param = new ArrayList();
				String[] fun_rnames = List.split(",");
				for (int i = 0; i < fun_rnames.length; i++) {
					if(i==(fun_rnames.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					param.add(fun_rnames[i].replaceAll("'", ""));;
				}
				sb.append(") and FUN_ID='0')");
				Query sql = session.createQuery(sb.toString());
				for (int i = 0; i < fun_rnames.length; i++) {
					sql.setParameter(i, fun_rnames[i]);
				}
				int r = sql.executeUpdate();
				tx.commit();
				session.close();
				return r;
			}
		
            /**
             * 更新外部链接
             * @param temp
             * @param i
             * @param uid
             * @return
             */
			public int updateIndex(String temp,int i,String uid){
				Session session=getSessionFactory().openSession();
				Transaction tx=session.beginTransaction();
				Query sql = session.createQuery("update "+Sysfavfunction.DATABASE_ENTITY+" set FUN_INDEX=? where USER_ID=? and FUN_ID='0' and FUN_RNAME=?");
				sql.setInteger(0, i);
				sql.setString(1, uid);
				sql.setString(2, temp);
				int r = sql.executeUpdate();
				tx.commit();
				session.close();
				return r;
			}
			
			/**
			 * 执行插入操作
			 * @param model
			 */
			public void addBoData(Sysfavfunction model) {
				this.getHibernateTemplate().save(model);	
			}
			
			/**
			 * 获取排序最大值
			 * @return
			 */
			public long getMaxIndex() {
				String sql="SELECT MAX(FUN_INDEX)+1 FROM "+Sysfavfunction.DATABASE_ENTITY;
				long noStr =0;
				List ll = (List) this.getHibernateTemplate().find(sql);
				Iterator itr = ll.iterator();
				if (itr.hasNext()) {
					Object noint = itr.next();         
		    			noStr = noint.hashCode();            
				}				
				return noStr;
			}
								
}
