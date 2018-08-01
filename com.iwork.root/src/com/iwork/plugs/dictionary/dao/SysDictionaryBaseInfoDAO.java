package com.iwork.plugs.dictionary.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.plugs.dictionary.model.SysDictionaryBaseinfo;

public class SysDictionaryBaseInfoDAO extends HibernateDaoSupport{
	/**
	 * 获得查询条件字段列表
	 * @param reportId
	 * @return
	 */
	public List<SysDictionaryBaseinfo> getList(Long formId){
		List<SysDictionaryBaseinfo> list = null;
		if(formId!=null){
			String sql = "FROM SysDictionaryBaseinfo WHERE groupid = ?";
			Object[] values = {formId};
			list = this.getHibernateTemplate().find(sql,values);
		}
		return list;
	}
	/**
	 * 获得查询条件字段列表
	 * @param reportId
	 * @return
	 */
	public List<SysDictionaryBaseinfo> getListByType(Long formId,Long dicType){
		List<SysDictionaryBaseinfo> list = null;
		if(formId!=null){
			String sql = "FROM SysDictionaryBaseinfo WHERE groupid = ? and dicType=?";
			Object[] values = {formId,dicType};
			list = this.getHibernateTemplate().find(sql,values);  
		}
		return list;
	}
	/**
	 * 获得单条数据
	 * @param id
	 * @return
	 */
	public SysDictionaryBaseinfo getModel(Long id){
		SysDictionaryBaseinfo model = (SysDictionaryBaseinfo) this.getHibernateTemplate().get(SysDictionaryBaseinfo.class, id);
		return model;
	}
	/**
	 * 获得单条数据
	 * @param id
	 * @return
	 */
	public SysDictionaryBaseinfo getModel(String uuid){
		SysDictionaryBaseinfo model = null;
		if(uuid!=null){
			DBUtilInjection d=new DBUtilInjection();
		  
			String sql = "FROM SysDictionaryBaseinfo WHERE uuid = ?";
			if(d.HasInjectionData(uuid)){
				return model;
			}
			Object[] values = {uuid};
			List<SysDictionaryBaseinfo> list = this.getHibernateTemplate().find(sql,values);  
			if(list!=null&&list.size()>0){
				model = list.get(0);
			}
		}
		return model;
	}
	
	/**
	 * 更新
	 * @param model
	 */
	public void updateModel(SysDictionaryBaseinfo model){
		this.getHibernateTemplate().update(model);
	}
	/**
	 * 插入
	 * @param model
	 */
	public void addModel(SysDictionaryBaseinfo model) {
		this.getHibernateTemplate().save(model);
	}
	/**
	 * 删除
	 * @param model
	 */
	public void del(SysDictionaryBaseinfo model){
		this.getHibernateTemplate().delete(model);
	}
	
    /**
	 * 得到最大的orderindex
	 * @return
	 */
   public long getMaxOrderIndex(){
	    long max=1L;
	    String hql="SELECT MAX(orderIndex)+1 FROM SysDictionaryBaseinfo";
	    List list=this.getHibernateTemplate().find(hql);
	    if(list!=null&&list.size()!=0){
	    	Object obj = list.get(0);
	    	if(obj!=null){
	    		max=Long.parseLong(obj.toString());
	    	}
	    	
	    }
	    return max;
   }
   /**
    * 删除数据字典
    * @param id
    */
   public void delDic(Long id){
	   Session session = this.getSession();
	   Transaction ts = null;
	   try{
		   ts = session.beginTransaction();
		   String hql = "delete from SysDictionaryBaseinfo where id=?";
		   Query query = session.createQuery(hql);
		   query.setLong(0, id);
		   query.executeUpdate();
		   
		   hql = "delete from SysDictionaryCondition where id=?";
		   query = session.createQuery(hql);
		   query.setLong(0, id);
		   query.executeUpdate();
		   
		   hql = "delete from SysDictionaryShowfield where id=?";
		   query = session.createQuery(hql);
		   query.setLong(0, id);
		   query.executeUpdate();
		   ts.commit();
		   ts = null;
	   }catch(Exception e){
		   logger.error(e,e);
		   if(ts!=null){
			   ts.rollback();
		   }
	   }finally{
		   session.close();
	   }
	   
   }
  
}
