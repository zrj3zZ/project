package com.iwork.core.server.servicemanager.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;  

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.server.servicemanager.model.Sysservice;;

public class SysServiceManagerDAO extends HibernateDaoSupport implements ISysServiceManagerDAO{
	public SysServiceManagerDAO(){}
			/**
			 * 函数说明：添加信息
			 * 参数说明：对象 
			 * 返回值：  
			 */ 
			public void addBoData(Sysservice model) {
				this.getHibernateTemplate().save(model);
			}

			/**
			 * 函数说明：删除信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void deleteBoData(Sysservice model) {
				this.getHibernateTemplate().delete(model);
			}

			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */
			public List getAll() {
				String sql="FROM Sysservice ORDER BY ORDERINDEX";
				return this.getHibernateTemplate().find(sql);
			}
			/**
			 * 获得有效的服务列表
			 * @return
			 */
			public List<Sysservice> getActiveService(){
				StringBuffer sql= new StringBuffer();
				sql.append("FROM Sysservice where   startdate<=? and enddate>? ORDER BY ORDERINDEX,ID");
				final String sql1=sql.toString();
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						query.setDate(0,Calendar.getInstance().getTime());
						query.setDate(1,Calendar.getInstance().getTime());
						return query.list();
					}
				});
			}
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows() {
				String sql=" FROM Sysservice";
				List list=this.getHibernateTemplate().find(sql);
				return list.size();
			} 
			

			/**
			 * 函数说明：获得一条的信息
			 * 参数说明： ID
			 * 返回值：对象
			 */
			public Sysservice getBoData(Long id) {
				if(id==null)id = new Long(0);
				return (Sysservice)this.getHibernateTemplate().get(Sysservice.class,id);
			}

			/**
			 * 函数说明：获得最大ID
			 * 参数说明： 
			 * 返回值：最大ID
			 */
			public Long getMaxID() {
				String date=UtilDate.getNowdate();
				String sql="SELECT MAX(id)+1 FROM Sysservice";
				Long noStr = null;
				List ll = (List) this.getHibernateTemplate().find(sql);
				Iterator itr = ll.iterator();
				if (itr.hasNext()) {
					Long noint = (Long)itr.next();
		            if(noint == null){
		    			noStr = new Long(1);;            	
		            }else{
		    			noStr = noint;
		            }
				}
				return noStr;
			}
			/**
			 * 获得排序号
			 * @return
			 */
		  public String getOrderIndex(){
			  String orderindex="1";
			  String sql="SELECT MAX(orderindex)+1 FROM Sysservice";
			  List list = (List) this.getHibernateTemplate().find(sql);
			  if(list.size()>0){
				  if(list.get(0)!=null){
					  orderindex=list.get(0).toString();
				  }
			  }
			 return orderindex; 
		  }	

			/**
			 * 函数说明：修改信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void updateBoData(Sysservice pd) {
				this.getHibernateTemplate().update(pd);
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
						Query query=session.createQuery(" FROM Sysservice ORDER BY ORDERINDEX");
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
			public int updateIndex(Long id,String type){
				int index=0;
				Sysservice sns1 = null;
				Sysservice sns2 = null;;
				Long temp = new Long(0);
				String sql = "FROM Sysservice WHERE ID =?";
				Object[] value = {id};
				List downlist=this.getHibernateTemplate().find(sql,value);
				if(downlist!=null){
					if(downlist.size()>0){
						sns1 = (Sysservice)downlist.get(0);
					}
				}
				if(type.equals("up"))
					sql = "FROM Sysservice WHERE orderindex <? order by orderindex desc";
				else
					sql = "FROM Sysservice WHERE orderindex >? order by orderindex asc";
				Object[] values = {sns1.getOrderindex()};
				DBUtilInjection d=new DBUtilInjection();
				
				if(sns1.getOrderindex()!=null&&!"".equals(sns1.getOrderindex())){
					if(d.HasInjectionData(sns1.getOrderindex().toString())){
						return index;
					}
				}
				List uplist=this.getHibernateTemplate().find(sql);
					if(uplist!=null){
						if(uplist.size()>0){
							sns2 = (Sysservice)uplist.get(0);
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
}
