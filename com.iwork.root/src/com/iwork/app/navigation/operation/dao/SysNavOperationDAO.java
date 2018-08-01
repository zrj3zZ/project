package com.iwork.app.navigation.operation.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.navigation.operation.model.SysNavOperation;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;

public class SysNavOperationDAO extends HibernateDaoSupport implements ISysNavOperationDAO{
	public SysNavOperationDAO(){}
			
	/**
	 * 函数说明：添加信息
	 * 参数说明：对象 
	 * 返回值：
	 */
	public void addBoData(SysNavOperation model) {
		this.getHibernateTemplate().save(model);
	}

	/**
	 * 函数说明：删除信息
	 * 参数说明： 对象
	 * 返回值：
	 */
	public void deleteBoData(SysNavOperation model) {
		this.getHibernateTemplate().delete(model);
	}

	/**
	 * 函数说明：获得所有的信息
	 * 参数说明： 
	 * 返回值：信息的集合
	 */
	public List getAll() {
		String sql="FROM "+SysNavOperation.DATABASE_ENTITY+" ORDER BY ORDERINDEX";
		return this.getHibernateTemplate().find(sql);
	}
	
	/**
	 * 函数说明：获得总行数
	 * 参数说明： 
	 * 返回值：总行数
	 */
	public int getRows() {
		String sql="FROM "+SysNavOperation.DATABASE_ENTITY;
		List list=this.getHibernateTemplate().find(sql);
		return list.size();
	}
	

	/**
	 * 函数说明：获得一条的信息
	 * 参数说明： ID
	 * 返回值：对象
	 */
	public SysNavOperation getBoData(String id) {
		if(id==null)id = "0";
		return (SysNavOperation)this.getHibernateTemplate().get(SysNavOperation.class,id);
	}

	/**
	 * 函数说明：获得最大ID
	 * 参数说明： 
	 * 返回值：最大ID
	 */
	public String getMaxID() {
		String date=UtilDate.getNowdate();
		String sql="SELECT MAX(ID)+1 FROM "+SysNavOperation.DATABASE_ENTITY;
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
	public void updateBoData(SysNavOperation pd) {
		this.getHibernateTemplate().update(pd);
	}

	/**
	 * 函数说明：查询信息
	 * 参数说明： 集合
	 * 返回值：
	 */
	public List queryBoDatas(String fieldname,String value) {
		String sql="FROM "+SysNavOperation.DATABASE_ENTITY+"  where ? like ? ORDER BY ORDERINDEX";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(fieldname!=null&&!"".equals(fieldname)){
			if(d.HasInjectionData(fieldname)){
				return l;
			}
		}
		if(fieldname!=null&&!"".equals(fieldname)){
			if(d.HasInjectionData(fieldname)){
				return l;
			}
		}
		Object[] values = {fieldname,"%"+value+"%"};
		return this.getHibernateTemplate().find(sql,values);
	}
	
	/**
	 * 获得功能模块列表
	 * @param directoryid
	 * @return
	 */
	public List getSubFunctionList(String directoryid) {
		String sql="FROM "+SysNavOperation.DATABASE_ENTITY+"  where DIRECTORY_ID = ? ORDER BY ORDERINDEX";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(directoryid!=null&&!"".equals(directoryid)){
			if(d.HasInjectionData(directoryid)){
				return l;
			}
		}
		Object[] value = {directoryid};
		return this.getHibernateTemplate().find(sql,value);
	}
	
	/**
	 * 函数说明：获得总行数
	 * 参数说明： 
	 * 返回值：总行数
	 */
	public int getRows(String ptype,String pid) {
		String sql="";
		if(ptype==null||ptype.equals("")||pid==null||pid.equals(""))
			return 0;
		else	
			sql="FROM "+SysNavOperation.DATABASE_ENTITY+" where ptype=? and pid= ?";
		List list=null;
		try {
			DBUtilInjection d=new DBUtilInjection();
		
			if(ptype!=null&&!"".equals(ptype)){
				if(d.HasInjectionData(ptype)){
					return 0;
				}
			}
			if(pid!=null&&!"".equals(pid)){
				if(d.HasInjectionData(pid)){
					return 0;
				}
			}
			Object[] value = {ptype,pid};
			list = this.getHibernateTemplate().find(sql,value);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
		if(list==null){
			return 0;
		}else{
			return list.size();
		}
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
				Query query=session.createQuery("FROM "+SysNavOperation.DATABASE_ENTITY+" ORDER BY ORDERINDEX");
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
	public List getBoDatas(String ptype,String pid,int pageSize, int startRow) {
		final int pageSize1=pageSize;
		final int startRow1=startRow;
		String sql="";
		
		if(ptype==null||ptype.equals("")||pid==null||pid.equals(""))
			//sql="FROM "+SysNavOperation.DATABASE_ENTITY+" where 1=2 ORDER BY ORDERINDEX";
			return null;
		else
			
			sql="FROM "+SysNavOperation.DATABASE_ENTITY+" where ptype=? and pid= ? ORDER BY ORDERINDEX";
		
		final String sql1=sql;
		final String final_ptype = ptype;
		final String final_pid = pid;
		return this.getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				// TODO 自动生成方法存根
				Query query=session.createQuery(sql1);
				if(final_ptype==null||final_ptype.equals("")||final_pid==null||final_pid.equals("")){}else{
					DBUtilInjection d=new DBUtilInjection();
					List l=new ArrayList();
					if(final_ptype!=null&&!"".equals(final_ptype)){
						if(d.HasInjectionData(final_ptype)){
							return l;
						}
					}
					if(final_pid!=null&&!"".equals(final_pid)){
						if(d.HasInjectionData(final_pid)){
							return l;
						}
					}
					query.setString(0, final_ptype);
					query.setString(1, final_pid);
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				return query.list();
			}
		});
	}
	/**
	 * 函数说明：获得指定子系统或模块的操作列表
	 * 参数说明： 集合
	 * 返回值：
	 */
	public List<SysNavOperation> getOperationList(String ptype,String pid) {
		String sql="";
		if(ptype==null||ptype.equals("")||pid==null||pid.equals("")){
			//sql="FROM "+SysNavOperation.DATABASE_ENTITY+" where 1=2 ORDER BY ORDERINDEX";
			return null;
		}else{
			sql="FROM "+SysNavOperation.DATABASE_ENTITY+" where ptype=? and pid=? ORDER BY ORDERINDEX";
			DBUtilInjection d=new DBUtilInjection();
			List l=new ArrayList();
			if(ptype!=null&&!"".equals(ptype)){
				if(d.HasInjectionData(ptype)){
					return l;
				}
			}
			if(pid!=null&&!"".equals(pid)){
				if(d.HasInjectionData(pid)){
					return l;
				}
			}
			Object[] value = {ptype,pid};
			return this.getHibernateTemplate().find(sql,value);
		}
	}
	
	/**
	 * 调整模块排序
	 * @param orderindex
	 * @return
	 */
	public int updateIndex(int id,String type){
		int index=0;
		SysNavOperation snd1 = null;
		SysNavOperation snd2 = null;;
		String temp = "";
		String sql = "FROM "+SysNavOperation.DATABASE_ENTITY+" WHERE ID =?";
		Object[] value = {id};
		List downlist=this.getHibernateTemplate().find(sql,value);
		if(downlist!=null){
			if(downlist.size()>0){
				snd1 = (SysNavOperation)downlist.get(0);
			}
		}
		if(type.equals("up"))
			sql = "FROM "+SysNavOperation.DATABASE_ENTITY+" WHERE orderindex <? order by orderindex desc";
		else
			sql = "FROM "+SysNavOperation.DATABASE_ENTITY+" WHERE orderindex >? order by orderindex asc";
		Object[] values = {snd1.getOrderindex()};
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(snd1.getOrderindex()!=null&&!"".equals(snd1.getOrderindex())){
			if(d.HasInjectionData(snd1.getOrderindex())){
				return index;
			}
		}
		
		List uplist=this.getHibernateTemplate().find(sql,values);
			if(uplist!=null){
				if(uplist.size()>0){
					snd2 = (SysNavOperation)uplist.get(0);
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
}
