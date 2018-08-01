package com.iwork.app.navigation.function.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.navigation.function.cache.SysNavFunctionCache;
import com.iwork.app.navigation.function.model.Sysnavfunction;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import org.apache.log4j.Logger;
public class SysNavFunctionDAO extends HibernateDaoSupport {
	private static Logger logger = Logger.getLogger(SysNavFunctionDAO.class);

	public SysNavFunctionDAO(){}
			
			/**
			 * 函数说明：添加信息
			 * 参数说明：对象 
			 * 返回值：
			 */
			public void addBoData(Sysnavfunction model) {
				this.getHibernateTemplate().save(model);
				SysNavFunctionCache.getInstance().putModel(model);
				SysNavFunctionCache.getInstance().removeList(model.getDirectoryId());
				SysNavFunctionCache.getInstance().removeList(model.getId());
			}

			/**
			 * 函数说明：删除信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void deleteBoData(Sysnavfunction model) {
				this.getHibernateTemplate().delete(model);
				SysNavFunctionCache.getInstance().removeModel(model.getId());
				SysNavFunctionCache.getInstance().removeList(model.getDirectoryId());
			}

			/**
			 * 函数说明：获得所有的信息 
			 * 参数说明： 
			 * 返回值：信息的集合
			 * 访问量较低不做cache装载（性能优化）
			 */
			public List getAll() {
				String sql="FROM Sysnavfunction ORDER BY orderindex ";  
				return this.getHibernateTemplate().find(sql);
			}
			
			/**
			 * 函数说明：获得总行数 
			 * 参数说明： 
			 * 返回值：总行数
			 * 访问量较低不做cache装载（性能优化）
			 */ 
			public int getRows() {
				String sql="FROM Sysnavfunction";
				List list=this.getHibernateTemplate().find(sql);
				return list.size();
			}
			

			/**
			 * 函数说明：获得一条的信息
			 * 参数说明： ID
			 * 返回值：对象
			 */
			public Sysnavfunction getBoData(Long id) {
				Sysnavfunction sysNavFunction = null;
				if(id==null)id = new Long(0);
				try {
					sysNavFunction = (Sysnavfunction)SysNavFunctionCache.getInstance().getModel(id);
					if(sysNavFunction!=null){
						return sysNavFunction;
					}else{
						return (Sysnavfunction)this.getHibernateTemplate().get(Sysnavfunction.class,id);
					}
				} catch (RuntimeException e) {logger.error(e,e);
					return (Sysnavfunction)this.getHibernateTemplate().get(Sysnavfunction.class,id);
				}
				
				
			}

			/**
			 * 函数说明：获得最大ID
			 * 参数说明： 
			 * 返回值：最大ID
			 */
			public String getMaxID() {
				String date=UtilDate.getNowdate();
				String sql="SELECT MAX(ID)+1 FROM Sysnavfunction ";
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
			public void updateBoData(Sysnavfunction model) {
				this.getHibernateTemplate().update(model);
				SysNavFunctionCache.getInstance().removeAll();
			}

			/**
			 * 获得功能模块列表
			 * @param directoryid
			 * @return
			 */
			public List getSubFunctionList(Long directoryid) {
				
				List list = null;
				try {
					list = SysNavFunctionCache.getInstance().getList(directoryid);
					if(list==null){
						String sql="FROM Sysnavfunction  where directoryId = "+directoryid+" ORDER BY orderindex";
						list = this.getHibernateTemplate().find(sql);
						SysNavFunctionCache.getInstance().putList(directoryid, list);
					}
				} catch (RuntimeException e) {logger.error(e,e);
				}
				
				return list;
			}
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows(Long directoryId) {
				String sql="FROM Sysnavfunction where directoryId = "+directoryId;
				List list=this.getHibernateTemplate().find(sql);				
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
						Query query=session.createQuery("FROM Sysnavfunction ORDER BY orderindex");
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
			public List<Sysnavfunction> getFunctionList(Long directoryId) {
				String sql = "FROM Sysnavfunction where directoryId = "+directoryId+" order by orderIndex";
				return this.getHibernateTemplate().find(sql); 
			}
			
			
			/**
			 * 调整模块排序
			 * @param orderindex
			 * @return
			 */
			public int updateIndex(int id,String type){
				int index=0;
				Sysnavfunction snd1 = null;
				Sysnavfunction snd2 = null;;
				Long temp ;
				String sql = "FROM Sysnavfunction WHERE ID ="+id;
				List downlist=this.getHibernateTemplate().find(sql);
				if(downlist!=null){
					if(downlist.size()>0){
						snd1 = (Sysnavfunction)downlist.get(0);
					}
				}
				if(type.equals("up"))
					sql = "FROM Sysnavfunction WHERE directoryId="+snd1.getDirectoryId()+" and orderindex <"+snd1.getOrderindex()+" order by orderindex desc";
				else
					sql = "FROM Sysnavfunction WHERE directoryId="+snd1.getDirectoryId()+" and orderindex >"+snd1.getOrderindex()+" order by orderindex asc";
				List uplist=this.getHibernateTemplate().find(sql); 
					if(uplist!=null){
						if(uplist.size()>0){
							snd2 = (Sysnavfunction)uplist.get(0);
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
			 * 获取第三层系统菜单中的我的收藏夹列表
			 * @param tem
			 * @return
			 */
			public List getFunList(String tem){							
				String sql ="from Sysnavfunction where id=  ? ";
				DBUtilInjection d=new DBUtilInjection();
				List l=new ArrayList();
				if(tem!=null&&!"".equals(tem)){
					if(d.HasInjectionData(tem)){
						return l;
					}
				}
				Object[] values = {tem};
				List funlist = this.getHibernateTemplate().find(sql,values);
				return funlist;
			}
}
