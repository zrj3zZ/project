package com.iwork.app.navigation.favorite.sysfav.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.iwork.app.navigation.favorite.sysfav.bean.Sysfavsystem;
/**
 * 系统收藏夹数据库操作类
 * @author WeiGuangjian
 *
 */
public class SysFavSystemDAO extends HibernateDaoSupport{
	public SysFavSystemDAO(){}
			
			/**
			 * 获得所有的信息
			 * @return
			 */
			public List getAll() {			 
					String sql="FROM "+Sysfavsystem.DATABASE_ENTITY+" ORDER BY SYS_INDEX";
				    List list =  this.getHibernateTemplate().find(sql);
					return list;
			}
			
			/**
			 * 执行更新操作
			 * @param sys_id
			 * @param sys_name
			 * @param sys_url
			 * @param sys_target
			 * @param sys_memo
			 * @param sys_index
			 * @return
			 */
			public int update(long sys_id,String sys_name,String sys_url,String sys_target,String sys_memo,long sys_index){
				Session session=getSessionFactory().openSession();
				Transaction tx=session.beginTransaction();
				Query sql = session.createQuery("update "+Sysfavsystem.DATABASE_ENTITY+" set sys_name=?, sys_url=?, sys_target=?, sys_memo=?,sys_index=? where ID=?");
				sql.setString(0, sys_name);
				sql.setString(1, sys_url);
				sql.setString(2, sys_target);
				sql.setString(3, sys_memo);
				sql.setLong(4, sys_index);
				sql.setLong(5, sys_id);
				int r = sql.executeUpdate();
				tx.commit();
				session.close();
				return r;
			}
			
			/**
			 * 执行插入操作
			 * @param model
			 */
			public void addBoData(Sysfavsystem model) {
				this.getHibernateTemplate().save(model);	
			}
			
			/**
			 * 执行删除操作
			 * @param id
			 * @return
			 */
			public int delete(long id){			
				Session session=getSessionFactory().openSession();
				Transaction tx=session.beginTransaction();
				Query sql = session.createQuery("delete from "+Sysfavsystem.DATABASE_ENTITY+" where ID=?");
				sql.setLong(0, id);
				int r = sql.executeUpdate();
				tx.commit();
				session.close();
				return r;
			}
			
			/**
			 * 获取排序最大值
			 * @return
			 */
			public long getMaxIndex() {
				String sql="SELECT MAX(sysIndex) FROM "+Sysfavsystem.DATABASE_ENTITY;
				long noStr =0;
				List ll = (List) this.getHibernateTemplate().find(sql);
				Iterator itr = ll.iterator();
				if (itr.hasNext()) {
					Object noint = itr.next();         
		    			noStr = noint.hashCode();            
				}				
				return noStr;
			}
			
			/**
			 * 获取排序最小值
			 * @return
			 */
			public long getMinIndex() {
				String sql="SELECT MIN(sysIndex) FROM "+Sysfavsystem.DATABASE_ENTITY;
				long noStr =0;
				List ll = (List) this.getHibernateTemplate().find(sql);
				Iterator itr = ll.iterator();
				if (itr.hasNext()) {
					Object noint = itr.next();         
		    			noStr = noint.hashCode();            
				}				
				return noStr;
			}
			
			/**
			 * 排序更新1
			 * @param sys_id
			 * @param isys_index
			 * @return
			 */
			public long move(long sys_id,long isys_index) {
				Session session=getSessionFactory().openSession();
				Transaction tx=session.beginTransaction();
				Query sql = session.createQuery("update "+Sysfavsystem.DATABASE_ENTITY+" set sys_index=? where ID=?");
				sql.setLong(0, isys_index);
				sql.setLong(1, sys_id);
				int r = sql.executeUpdate();
				tx.commit();
				session.close();
				return r;
			}
			
			/**
			 * 排序更新2
			 * @param sys_index
			 * @param isys_id
			 * @return
			 */
			public long imove(long sys_index,long isys_id) {
				Session session=getSessionFactory().openSession();
				Transaction tx=session.beginTransaction();
				Query sql = session.createQuery("update "+Sysfavsystem.DATABASE_ENTITY+" set sys_index=? where ID=?");
				sql.setLong(0, sys_index);
				sql.setLong(1, isys_id);
				int r = sql.executeUpdate();
				tx.commit();
				session.close();
				return r;
			}
			
}
