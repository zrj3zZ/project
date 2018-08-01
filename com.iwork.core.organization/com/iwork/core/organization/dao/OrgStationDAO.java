package com.iwork.core.organization.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.log.operationlog.constant.LogInfoConstants;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgStation;
import com.iwork.core.organization.model.OrgStationIns;
import com.iwork.core.organization.model.OrgStationInsItem;

public class OrgStationDAO extends HibernateDaoSupport{
	
	
	/**
	 * 当前部门对应的岗位范围实例
	 * @param stationId
	 * @param deptlist
	 * @return
	 */
	public List<OrgStationInsItem> getOrgStationInsByDepartment(Long stationId,List<OrgDepartment> deptlist ){
		StringBuffer sql = new StringBuffer();
		List params = new ArrayList();
		sql.append("From OrgStationInsItem Where orgtype='dept' and  stationId =?");
		params.add(stationId);
		if(deptlist!=null&&deptlist.size()>0){
			sql.append(" and ( 1=2 ");
			for(OrgDepartment dept:deptlist){
				sql.append(" or val=?");
				params.add(dept.getId());
			}
			sql.append(" )");
		}
		Object[] values = new Object[params.size()];
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		for (int i = 0; i < params.size(); i++) {
			
				   
				
			
			values[i] = params.get(i);
			if(values[i]!=null&&!"".equals(values[i])){
				if(d.HasInjectionData(values[i].toString())){
					return l;
				}
			}
		}
		
		List<OrgStationInsItem> list = this.getHibernateTemplate().find(sql.toString(),values);
		sql.append(" order by id desc");
		return list;
	}
	
	/**
	 * 当前部门对应的岗位范围实例
	 * @param stationId
	 * @param deptlist
	 * @return
	 */
	public List<OrgStationInsItem> getOrgStationInsByUserId(Long stationId,String userid ){
		StringBuffer sql = new StringBuffer();
		sql.append("From OrgStationInsItem Where orgtype='user' and  stationId =? and val=?");
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(userid!=null&&!"".equals(userid)){
			if(d.HasInjectionData(userid)){
				return l;
			}
		}
		Object[] values = {stationId,userid};
		List<OrgStationInsItem> list = this.getHibernateTemplate().find(sql.toString(),values);
		return list;
	}
	
	/**
	 * 保存范围
	 * @param model
	 */
	public void saveStationeItem(OrgStationInsItem model){
		this.getHibernateTemplate().save(model);
	}
	/**
	 * 删除范围
	 * @param model
	 */
	public void deleteStationeItem(OrgStationInsItem model){
		this.getHibernateTemplate().delete(model);
	}
	
	/**
	 * 获得范围模型
	 * @param id
	 * @return
	 */
	public OrgStationInsItem getStationInsItemModel(Long id){
		OrgStationInsItem model = (OrgStationInsItem)this.getHibernateTemplate().get(OrgStationInsItem.class, id);
		return model;
	}
	
	/**
	 * 获得范围列表
	 * @param insId
	 * @param type
	 * @return
	 */
	public List<OrgStationInsItem> getItemList(Long stationId,Long insId,String type){
		StringBuffer sql = new StringBuffer();
		List params = new ArrayList();
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(type!=null&&!"".equals(type)){
			if(d.HasInjectionData(type)){
				return l;
			}
		}
		sql.append("From OrgStationInsItem Where 1=1 ");
		if(type!=null){
			sql.append(" and orgtype = ?");
			params.add(type);
		}
		if(insId!=null){
			sql.append(" and stationInsId = ?");
			params.add(insId);
		}
		if(stationId!=null){
			sql.append(" and stationId = ?");
			params.add(stationId);
		}
		Object[] values = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			values[i] = params.get(i);
		}
		List<OrgStationInsItem> list = this.getHibernateTemplate().find(sql.toString(),values);
		return list;
	}
	/**
	 * 获得范围列表
	 * @param insId
	 * @param type
	 * @return
	 */
	public List<OrgStationInsItem> getItemList(Long stationId,Long insId,String type,String value){
		StringBuffer sql = new StringBuffer();
		List params = new ArrayList();
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(type!=null&&!"".equals(type)){
			if(d.HasInjectionData(type)){
				return l;
			}
		}
		if(value!=null&&!"".equals(value)){
			if(d.HasInjectionData(value)){
				return l;
			}
		}
		sql.append("From OrgStationInsItem Where 1=1 ");
		if(type!=null){
			sql.append(" and orgtype = ?");
			params.add(type);
		}
		if(insId!=null){
			sql.append(" and stationInsId = ?");
			params.add(insId);
		}
		if(stationId!=null){
			sql.append(" and stationId = ?");
			params.add(stationId);
		}
		if(value!=null){
			sql.append(" and val = ?");
			params.add(value);
		}
		Object[] values = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			values[i] = params.get(i);
		}
		List<OrgStationInsItem> list = this.getHibernateTemplate().find(sql.toString(),values);
		return list;
	}
	
	/**
	 * 获得全部岗位列表
	 * @return
	 */
	public List<OrgStation> getAllList(){
		String sql = "From OrgStation ORDER BY id";
		List<OrgStation> list = this.getHibernateTemplate().find(sql);
		return list;
	}
	
	
	public List<OrgStationIns> getStationInsList(Long stationId){
		StringBuffer sql = new StringBuffer();
		sql.append("From OrgStationIns WHERE stationId = ? ORDER BY id");
		Object[] values = {stationId};
		List<OrgStationIns> list = this.getHibernateTemplate().find(sql.toString(),values);
		return list;
	}
	public OrgStation getModel(Long id){
		OrgStation model = (OrgStation)this.getHibernateTemplate().get(OrgStation.class, id);
		return model;
	}
	public void saveStation(OrgStation model){
		if(model!=null){
			this.getHibernateTemplate().save(model);
			//添加审计日志
			StringBuffer log = new StringBuffer();
			log.append("岗位名称【").append(model.getStationName()).append("】,岗位ID【").append(model.getId()).append("】");
			LogUtil.getInstance().addLog(model.getId(), LogInfoConstants.ORG_STATION_ADD, log.toString());
		}
		
	}
	public void updateStation(OrgStation model){
		if(model!=null){
			this.getHibernateTemplate().update(model);
		}
		//添加审计日志
		StringBuffer log = new StringBuffer();
		log.append("岗位名称【").append(model.getStationName()).append("】,岗位ID【").append(model.getId()).append("】");
		LogUtil.getInstance().addLog(model.getId(), LogInfoConstants.ORG_STATION_UPDATE, log.toString());
	}
	public void delStation(OrgStation model){
		this.getHibernateTemplate().delete(model);
	}
	
	
	
	public OrgStationIns getStationInsModel(Long id){
		OrgStationIns model = (OrgStationIns)this.getHibernateTemplate().get(OrgStationIns.class, id);
		return model;
	}
	public void saveStationIns(OrgStationIns model){
		this.getHibernateTemplate().save(model);
	}
	public void updateStationIns(OrgStationIns model){
		this.getHibernateTemplate().update(model);
	}
	public void delStationIns(OrgStationIns model){
		this.getHibernateTemplate().delete(model);
	}
}
