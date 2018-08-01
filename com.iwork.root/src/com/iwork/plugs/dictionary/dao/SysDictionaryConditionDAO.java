package com.iwork.plugs.dictionary.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.plugs.dictionary.model.SysDictionaryCondition;

public class SysDictionaryConditionDAO extends HibernateDaoSupport{
	/**
	 * 获得查询条件字段列表
	 * @param reportId
	 * @return
	 */
	public List<SysDictionaryCondition> getListForDSType(Long dictionaryId,Long dsType){   	
		List<SysDictionaryCondition> list = null;
		if(dictionaryId!=null){
			String sql = "FROM SysDictionaryCondition WHERE dictionaryId = "+dictionaryId+" and dsType = "+dsType+" order by orderIndex";
			list = this.getHibernateTemplate().find(sql);
		}
		return list;
	}
	/**
	 * 获得查询条件字段列表
	 * @param reportId
	 * @return
	 */
	public List<SysDictionaryCondition> getList(Long dictionaryId){   	
		List<SysDictionaryCondition> list = null;
		if(dictionaryId!=null){
			String sql = "FROM SysDictionaryCondition WHERE dictionaryId = "+dictionaryId+" order by orderIndex";
			list = this.getHibernateTemplate().find(sql);
		}
		return list;
	}
	/**
	 * 获得单条数据
	 * @param id
	 * @return
	 */
	public SysDictionaryCondition getModel(Long id){
		SysDictionaryCondition model = (SysDictionaryCondition) this.getHibernateTemplate().get(SysDictionaryCondition.class, id);
		return model;
	}
	/**
	 * 获得单条数据
	 * @param dictionaryId
	 * @param fieldName
	 * @return
	 */
	public SysDictionaryCondition getModel(Long dictionaryId,String fieldName){
		SysDictionaryCondition model = null;
		if(dictionaryId!=null){
			String sql = "FROM SysDictionaryCondition WHERE dictionaryId = "+dictionaryId+" and fieldName= ?  order by orderIndex";
			DBUtilInjection d=new DBUtilInjection();
		    List lis=new ArrayList(); 
		    if ((fieldName != null) && (!"".equals(fieldName))) {
	        	if(d.HasInjectionData(fieldName)){
    				return model;
    			}
	         
	        }
			Object[] values = {fieldName};
			List<SysDictionaryCondition> list = this.getHibernateTemplate().find(sql,values);
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
	public void updateModel(SysDictionaryCondition model){
		this.getHibernateTemplate().update(model);
	}
	/**
	 * 插入
	 * @param model
	 */
	public void addModel(SysDictionaryCondition model) {
		this.getHibernateTemplate().save(model);
	}
	/**
	 * 删除
	 * @param model
	 */
	public void del(SysDictionaryCondition model){
		this.getHibernateTemplate().delete(model);
	}
	
    /**
	 * 得到最大的orderindex
	 * @return
	 */
   public long getMaxOrderIndex(){
	    long max=1L;
	    String hql="SELECT MAX(orderIndex)+1 FROM SysDictionaryCondition";
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
    * 调整排序
    * @param type 'up'表示上移，'down'表示下移
    * @param ireportId
    * @param id
    */
	public void updateOrderIndex(String type,Long dictionaryId,Long id){
		SysDictionaryCondition snd1 = null;
		SysDictionaryCondition snd2 = null;;
		Long temp = new Long(0);
		snd1 = this.getModel(id);
		String sql = "";
		if(type.equals("up")){
			sql = " FROM SysDictionaryCondition WHERE  dictionaryId = "+dictionaryId+"  and orderIndex < "+snd1.getOrderIndex()+" order by orderIndex desc";
		}else if(type.equals("down")){
			sql = " FROM SysDictionaryCondition WHERE  dictionaryId = "+dictionaryId+"  and orderIndex > "+snd1.getOrderIndex()+" order by orderIndex asc";
		}
		else if(type.equals("top")){
			sql = " FROM SysDictionaryCondition WHERE  dictionaryId = "+dictionaryId+"  and orderIndex < "+snd1.getOrderIndex()+" order by orderIndex asc";
		}else if(type.equals("bottom")){
			sql = " FROM SysDictionaryCondition WHERE  dictionaryId = "+dictionaryId+"  and orderIndex > "+snd1.getOrderIndex()+" order by orderIndex desc";
		}
		
		List<SysDictionaryCondition> list=this.getHibernateTemplate().find(sql);
		if(list!=null){
			if(list.size()>0){
					snd2 = (SysDictionaryCondition)list.get(0);
			}
		}
		if(snd1!=null&&snd2!=null){ 
			//如果排序编号未错乱，正常调整排序
			if( snd1.getOrderIndex()!=snd2.getOrderIndex()){
				temp = snd1.getOrderIndex();
				snd1.setOrderIndex(snd2.getOrderIndex());
				//执行更新动作
				this.getHibernateTemplate().update(snd1);
				snd2.setOrderIndex(temp);
				//执行更新动作
				this.getHibernateTemplate().update(snd2);
			}
		}
	}
	/**
	 * 
	 * @param reportId
	 * @return 该报表下查询条件设置下已选字段的字段ID
	 */
	public List<String> getFeildsChossen(Long dictionaryId){
		List<String> list = new ArrayList<String>();
		if(dictionaryId!=null){
			String hql = "select fieldName from SysDictionaryCondition where dictionaryId="+dictionaryId+" order by orderIndex";
			list = this.getHibernateTemplate().find(hql); 
		}		
		return list;
	}
	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	public SysDictionaryCondition findModel(Long dictionaryId,String fieldName){
		List<SysDictionaryCondition> list = new ArrayList<SysDictionaryCondition>();
		if(dictionaryId!=null&fieldName!=null){
			String hql = "from SysDictionaryCondition where dictionaryId="+dictionaryId+" and fieldName= ? ";
			DBUtilInjection d=new DBUtilInjection();
		    List lis=new ArrayList(); 
		    if ((fieldName != null) && (!"".equals(fieldName))) {
	        	if(d.HasInjectionData(fieldName)){
    				return null;
    			}
	         
	        }
			Object[] values = {fieldName};
			list = this.getHibernateTemplate().find(hql,values); 
		}
		if(list!=null&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
		
	}
  
}
