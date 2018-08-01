package com.iwork.app.navigation.sys.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.navigation.sys.cache.SysNavSystemCache;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;

public class SysNavSystemDAO extends HibernateDaoSupport implements ISysNavSystemDAO{
	public SysNavSystemDAO(){}
			
			/**
			 * 函数说明：添加信息
			 * 参数说明：对象 
			 * 返回值：
			 */
			public void addBoData(SysNavSystem model) {
				this.getHibernateTemplate().save(model);
				SysNavSystemCache.getInstance().putModel(model);//装载cache
				SysNavSystemCache.getInstance().removeList();
			}

			/**
			 * 函数说明：删除信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void deleteBoData(SysNavSystem model) {
				this.getHibernateTemplate().delete(model);
				SysNavSystemCache.getInstance().removeModel(model.getId());
				SysNavSystemCache.getInstance().removeList();
			}

			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */
			public List getAll() {
				List list = SysNavSystemCache.getInstance().getList();
				if(list!=null){
					return list;
				}else{
					String sql="FROM "+SysNavSystem.DATABASE_ENTITY+" ORDER BY ORDERINDEX";
					list =  this.getHibernateTemplate().find(sql);
					SysNavSystemCache.getInstance().putList(list);
					return list;
				}
			}
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows() {
				List list = SysNavSystemCache.getInstance().getList();
				if(list!=null){
					return list.size();
				}else{
					String sql="FROM "+SysNavSystem.DATABASE_ENTITY+" ORDER BY ORDERINDEX";
					list =  this.getHibernateTemplate().find(sql);
					SysNavSystemCache.getInstance().putList(list);
					return list.size();
				}
			}
			

			/**
			 * 函数说明：获得一条的信息
			 * 参数说明： ID
			 * 返回值：对象
			 */
			public SysNavSystem getBoData(String id) {
				SysNavSystem sysNavSystem = null;
				try {
					DBUtilInjection d=new DBUtilInjection();
					
					
					if(id!=null&&!"".equals(id)){
						if(d.HasInjectionData(id)){
							return sysNavSystem;
						}
					}
					sysNavSystem = (SysNavSystem)SysNavSystemCache.getInstance().getModel(id);
					if(sysNavSystem==null){
						sysNavSystem =  (SysNavSystem)this.getHibernateTemplate().get(SysNavSystem.class,id);
						if(sysNavSystem!=null){
							SysNavSystemCache.getInstance().putModel(sysNavSystem);//装载cache
						}
					}
				} catch (Exception e) {
					logger.error(e,e);
				}
				return sysNavSystem;
			}

			/**
			 * 函数说明：获得最大ID
			 * 参数说明： 
			 * 返回值：最大ID
			 */
			public String getMaxID() {
				String date=UtilDate.getNowdate();
				String sql="SELECT MAX(id)+1 FROM "+SysNavSystem.DATABASE_ENTITY;
				String noStr = null;
				List ll = (List) this.getHibernateTemplate().find(sql);
				Iterator itr = ll.iterator();
				if (itr.hasNext()) {
					Object noint = itr.next();
		            if(noint == null){
		    			noStr = "1";            	
		            }else{
		    			noStr = noint.toString();
		            }
				}
				
				if(noStr.length()==1){
					noStr="000"+noStr;
				}else if(noStr.length()==2){
					noStr="00"+noStr;
				}else if(noStr.length()==3){
					noStr="0"+noStr;
				}else{
					noStr=noStr;
				}
				return noStr;
			}

			/**
			 * 函数说明：修改信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void updateBoData(SysNavSystem pd) {
				this.getHibernateTemplate().update(pd);
				SysNavSystemCache.getInstance().putModel(pd);
				SysNavSystemCache.getInstance().removeList();
			}

			/**
			 * 函数说明：查询信息
			 * 参数说明： 集合
			 * 返回值：
			 */
			public List queryBoDatas(String fieldname,String value) {
				String sql="FROM "+SysNavSystem.DATABASE_ENTITY+"  where ? like ? ORDER BY ORDERINDEX";
				DBUtilInjection d=new DBUtilInjection();
				List l=new ArrayList();
				if(fieldname!=null&&!"".equals(fieldname)){
					if(d.HasInjectionData(fieldname)){
						return l;
					}
				}
				if(value!=null&&!"".equals(value)){
					if(d.HasInjectionData(value)){
						return l;
					}
				}
				Object[] values = {fieldname,"%"+value+"%"};
				return this.getHibernateTemplate().find(sql,values);
			}
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows(String fieldname,String value) {
				String sql="";
				List list = new ArrayList();
				if(fieldname==null||fieldname.equals("")||value==null||value.equals("")){
					sql=" FROM "+SysNavSystem.DATABASE_ENTITY+" ORDER BY ID DESC";
					list=this.getHibernateTemplate().find(sql);
				}else{	
					sql=" FROM "+SysNavSystem.DATABASE_ENTITY+" where ? like ? ORDER BY ORDERINDEX DESC";
					DBUtilInjection d=new DBUtilInjection();
					
					if(fieldname!=null&&!"".equals(fieldname)){
						if(d.HasInjectionData(fieldname)){
							return 0;
						}
					}
					if(value!=null&&!"".equals(value)){
						if(d.HasInjectionData(value)){
							return 0;
						}
					}
					Object[] values = {fieldname,"%"+value+"%"};
					list=this.getHibernateTemplate().find(sql,values);
				}
//				List list=this.getHibernateTemplate().find(sql);
				return list.size();
			}
			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List getBoDatas(int pageSize, int startRow) throws HibernateException {
				final int pageSize1=pageSize;
				final int startRow1=startRow;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public List doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
						Query query=session.createQuery(" FROM "+SysNavSystem.DATABASE_ENTITY+" ORDER BY ORDERINDEX");
			             query.setFirstResult(startRow1);
			             query.setMaxResults(pageSize1);
			             return query.list();
					}
				});
					
			}
			/**
			 * 函数说明：查询信息
			 * 参数说明： 集合
			 * 返回值：
			 */
			public List getBoDatas(String fieldname,String value,int pageSize, int startRow) {
				final int pageSize1=pageSize;
				final int startRow1=startRow;
				final String queryName=fieldname;
				final String queryValue=value;
				String sql="";
				
				if(queryName==null||queryName.equals("")||queryValue==null||queryValue.equals(""))
					sql=" FROM "+SysNavSystem.DATABASE_ENTITY+" ORDER BY ORDERINDEX";
				else
					sql="FROM "+SysNavSystem.DATABASE_ENTITY+" where ? like ? ORDER BY ORDERINDEX";
				
				final String sql1=sql;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						if(queryName==null||queryName.equals("")||queryValue==null||queryValue.equals("")){}else{
							DBUtilInjection d=new DBUtilInjection();
							List l=new ArrayList();
							if(queryName!=null&&!"".equals(queryName)){
								if(d.HasInjectionData(queryName)){
									return l;
								}
							}
							if(queryValue!=null&&!"".equals(queryValue)){
								if(d.HasInjectionData(queryValue)){
									return l;
								}
							}
							query.setString(0, queryName);
							query.setString(1, "%"+queryValue+"%");
						}
						query.setFirstResult(startRow1);
						query.setMaxResults(pageSize1);
						return query.list();
					}
				});
			}
			
			
			/**
			 * 调整模块排序
			 * @param orderindex
			 * @return
			 */
			public int updateIndex(int id,String type){
				int index=0;
				SysNavSystem sns1 = null;
				SysNavSystem sns2 = null;;
				String temp = "";
				String sql = "FROM "+SysNavSystem.DATABASE_ENTITY+" WHERE ID =?";
				Object[] value = {id};
				List downlist=this.getHibernateTemplate().find(sql,value);
				if(downlist!=null){
					if(downlist.size()>0){
						sns1 = (SysNavSystem)downlist.get(0);
					}
				}
				if(type.equals("up"))
					sql = "FROM "+SysNavSystem.DATABASE_ENTITY+" WHERE orderindex <? order by orderindex desc";
				else
					sql = "FROM "+SysNavSystem.DATABASE_ENTITY+" WHERE orderindex >? order by orderindex asc";
				Object[] values = {sns1.getOrderindex()};
				DBUtilInjection d=new DBUtilInjection();
				List l=new ArrayList();
				if(sns1.getOrderindex()!=null&&!"".equals(sns1.getOrderindex())){
					if(d.HasInjectionData(sns1.getOrderindex())){
						return index;
					}
				}
				List uplist=this.getHibernateTemplate().find(sql,values);
					if(uplist!=null){
						if(uplist.size()>0){
							sns2 = (SysNavSystem)uplist.get(0);
						}
					}
					if(sns1!=null&&sns2!=null){
						temp = sns1.getOrderindex();
						sns1.setOrderindex(sns2.getOrderindex());
						//执行更新动作
						this.updateBoData(sns1);
						sns2.setOrderindex(temp);
						this.updateBoData(sns2);
					}
				return index;
			}
			
			/**
			 * 获取第一层系统菜单中的我的收藏夹列表
			 * @param tem
			 * @return
			 */
			public List getSysList(String tem){							
				String sql ="from "+SysNavSystem.DATABASE_ENTITY+" where id=?";
				DBUtilInjection d=new DBUtilInjection();
				List l=new ArrayList();
				if(tem!=null&&!"".equals(tem)){
					if(d.HasInjectionData(tem)){
						return l;
					}
				}
				Object[] value = {tem};
				List syslist = this.getHibernateTemplate().find(sql,value);
				return syslist;
			}
}
