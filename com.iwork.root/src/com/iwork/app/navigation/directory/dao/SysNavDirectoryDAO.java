package com.iwork.app.navigation.directory.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.navigation.directory.cache.SysNavDirectoryCache;
import com.iwork.app.navigation.directory.model.SysNavDirectory;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;

public class SysNavDirectoryDAO extends HibernateDaoSupport {
	
	public SysNavDirectoryDAO(){}	
			/**
			 * 函数说明：添加信息
			 * 参数说明：对象 
			 * 返回值：
			 */
			public void addBoData(SysNavDirectory model) {
				this.getHibernateTemplate().save(model);
				
				//装载cache
				SysNavDirectoryCache.getInstance().putModel(model);
				//重新加载LIST cache
				SysNavDirectoryCache.getInstance().removeList();
				this.getDirectoryList();
			}

			/**
			 * 函数说明：删除信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void deleteBoData(SysNavDirectory model) {
				this.getHibernateTemplate().delete(model);
				SysNavDirectoryCache.getInstance().removeModel(model.getId());
				//重新加载LIST cache
				SysNavDirectoryCache.getInstance().removeList();
				this.getDirectoryList();
			}

			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */
			public List<SysNavDirectory> getAll() {
				String sql="FROM "+SysNavDirectory.DATABASE_ENTITY+" ORDER BY ORDERINDEX";
				return this.getHibernateTemplate().find(sql);
			}
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows() {
				String sql="FROM "+SysNavDirectory.DATABASE_ENTITY;
				List list=this.getHibernateTemplate().find(sql);
				return list.size();
			}
			

			/**
			 * 函数说明：获得一条的信息
			 * 参数说明： ID
			 * 返回值：对象
			 */
			public SysNavDirectory getBoData(Long id) {
				SysNavDirectory sysNavDirectory = null;
				if(id==null)id = new Long(0);
				try {
					sysNavDirectory = (SysNavDirectory)SysNavDirectoryCache.getInstance().getModel(id);
					if(sysNavDirectory!=null){
						return sysNavDirectory;
					}else{
						return (SysNavDirectory)this.getHibernateTemplate().get(SysNavDirectory.class,id);
					}
				} catch (RuntimeException e) {logger.error(e,e);
					return (SysNavDirectory)this.getHibernateTemplate().get(SysNavDirectory.class,id);
				}
				
				
			}

			/**
			 * 函数说明：获得最大ID
			 * 参数说明： 
			 * 返回值：最大ID
			 */
			public String getMaxID() {
				String date=UtilDate.getNowdate();
				String sql="SELECT MAX(ID)+1 FROM "+SysNavDirectory.DATABASE_ENTITY;
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
			public void updateBoData(SysNavDirectory pd) {
				this.getHibernateTemplate().update(pd);
				//装载cache
				SysNavDirectoryCache.getInstance().putModel(pd);
				//重新加载LIST cache
				SysNavDirectoryCache.getInstance().removeList();
				this.getDirectoryList();
			}

			
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows(String fieldname,String value) {
				String sql="";
				List list=new ArrayList();
				if(fieldname==null||fieldname.equals("")||value==null||value.equals("")){
					sql=" FROM "+SysNavDirectory.DATABASE_ENTITY;
					list=this.getHibernateTemplate().find(sql);
				}else{	
					sql="FROM "+SysNavDirectory.DATABASE_ENTITY+" where ? = ?";
					DBUtilInjection d=new DBUtilInjection();
					List l=new ArrayList();
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
					Object[] values = {fieldname,value};
					list=this.getHibernateTemplate().find(sql,values);
				}
				
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
						Query query=session.createQuery("FROM "+SysNavDirectory.DATABASE_ENTITY+" ORDER BY ORDERINDEX");
			             query.setFirstResult(startRow1);
			             query.setMaxResults(pageSize1);
			             return query.list();
					}
				});
					
			}
			/**
			 * 函数说明：获得子系统下所有目录信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List getDirectoryList() throws HibernateException {
				List list = SysNavDirectoryCache.getInstance().getList();
				if(list!=null){
					return list;
				}else{
					String sql="";
					sql="FROM "+SysNavDirectory.DATABASE_ENTITY+" ORDER BY ORDERINDEX";
					list=this.getHibernateTemplate().find(sql);
					return list;
				}
					
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
					sql="FROM "+SysNavDirectory.DATABASE_ENTITY+" ORDER BY ORDERINDEX";
				else
					sql="FROM "+SysNavDirectory.DATABASE_ENTITY+" where ? = ? ORDER BY ORDERINDEX";
				
				final String sql1=sql;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						if(queryName==null||queryName.equals("")||queryValue==null||queryValue.equals("")){}else{
							DBUtilInjection d=new DBUtilInjection();
							List l=new ArrayList();
							
								if(d.HasInjectionData(queryName)){
									return l;
								}
							
							
								if(d.HasInjectionData(queryValue)){
									return l;
								}
							
							query.setString(0, queryName);
							query.setString(1, queryValue);
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
				SysNavDirectory snd1 = null;
				SysNavDirectory snd2 = null;;
				Long temp ;
				String sql = "FROM "+SysNavDirectory.DATABASE_ENTITY+" WHERE ID =?";
				Object[] value = {id};
				List downlist=this.getHibernateTemplate().find(sql,value);
				if(downlist!=null){
					if(downlist.size()>0){
						snd1 = (SysNavDirectory)downlist.get(0);
					}
				}
				if(type.equals("up"))
					sql = "FROM "+SysNavDirectory.DATABASE_ENTITY+" WHERE   orderindex < ? order by orderindex desc";
				else
					sql = "FROM "+SysNavDirectory.DATABASE_ENTITY+" WHERE   orderindex > ? order by orderindex asc";
				Object[] values = {snd1.getOrderindex()};
				DBUtilInjection d=new DBUtilInjection();
				
				if(snd1.getOrderindex()!=null&&!"".equals(snd1.getOrderindex())){
					if(d.HasInjectionData(snd1.getOrderindex().toString())){
						return 0;
					}
				}
				
				List uplist=this.getHibernateTemplate().find(sql,values);
					if(uplist!=null){
						if(uplist.size()>0){
							snd2 = (SysNavDirectory)uplist.get(0);
						}
					}
					if(snd1!=null&&snd2!=null){
						temp = snd1.getOrderindex();
						snd1.setOrderindex(snd2.getOrderindex());
						//执行更新动作
						this.updateBoData(snd1);
						snd2.setOrderindex(temp);
						this.updateBoData(snd2);
					}
				return index;
			}
			
			/**
			 * 获取第二层系统菜单中的我的收藏夹列表
			 * @param tem
			 * @return
			 */
			public List getDirList(String tem){							
				String sql ="from "+SysNavDirectory.DATABASE_ENTITY+" where id=?";
				DBUtilInjection d=new DBUtilInjection();
				List l=new ArrayList();
				if(tem!=null&&!"".equals(tem)){
					if(d.HasInjectionData(tem)){
						return l;
					}
				}
				Object[] value = {tem};
				List dirlist = this.getHibernateTemplate().find(sql,value);
				return dirlist;
			}
}
