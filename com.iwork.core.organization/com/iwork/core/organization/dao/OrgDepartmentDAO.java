package com.iwork.core.organization.dao;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.iwork.app.log.operationlog.constant.LogInfoConstants;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.cache.DepartmentCache;
import com.iwork.core.organization.factory.OrgExtendFactory;
import com.iwork.core.organization.impl.OrgExtendDeptForWeiXinImpl;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgExtendModel;

public class OrgDepartmentDAO extends HibernateDaoSupport{
	public OrgDepartmentDAO(){}
	
	
	
			
			/**
			 * 函数说明：添加信息
			 * 参数说明：对象 
			 * 返回值：
			 */
			public void addBoData(OrgDepartment model) {
				this.getHibernateTemplate().save(model);
				//======================================================================
				List<OrgExtendModel> list = OrgExtendFactory.getDeptExtendList();
				if(list!=null){
					OrgExtendDeptForWeiXinImpl qeufxi = null;
					for(OrgExtendModel orm:list){
						if (orm != null) {
							Constructor cons = null;
							try {
								cons = orm.getCons();
								if (cons != null) {
									Object[] params = {};
									qeufxi = (OrgExtendDeptForWeiXinImpl) cons.newInstance(params);
									if(qeufxi!=null){
										qeufxi.addDepartment(model);
									}
								}
							} catch (Exception e) {
								logger.error(e,e);
							}
						}
					}
				}
				//======================================================================
				DepartmentCache.getInstance().putModel(model);
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("部门名称【").append(model.getDepartmentname()).append("】,部门ID【").append(model.getId()).append("】");
				LogUtil.getInstance().addLog(model.getId(), LogInfoConstants.ORG_DEPARTMENT_ADD, log.toString());
			}

			/**
			 * 函数说明：删除信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void deleteBoData(OrgDepartment model) {
				this.getHibernateTemplate().delete(model);
				//======================================================================
				List<OrgExtendModel> list = OrgExtendFactory.getDeptExtendList();
				if(list!=null){
					OrgExtendDeptForWeiXinImpl qeufxi = null;
					for(OrgExtendModel orm:list){
						if (orm != null) {
							Constructor cons = null;
							try {
								cons = orm.getCons();
								if (cons != null) {
									Object[] params = {};
									qeufxi = (OrgExtendDeptForWeiXinImpl) cons.newInstance(params);
									if(qeufxi!=null){
										qeufxi.removeDepartment(model);
									}
								}
							} catch (Exception e) {
								logger.error(e,e);
							}
						}
					}
				}
				//======================================================================
				DepartmentCache.getInstance().removeModel(model.getId());
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("部门名称【").append(model.getDepartmentname()).append("】,部门ID【").append(model.getId()).append("】");
				LogUtil.getInstance().addLog(model.getId(), LogInfoConstants.ORG_DEPARTMENT_DEL, log.toString());
			}

			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */
			public List getAll() {
				String sql="FROM "+OrgDepartment.DATABASE_ENTITY+"  ORDER BY ORDERINDEX,ID";
				return this.getHibernateTemplate().find(sql);
			}
			
			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */
			public List getSearchList(String searchkey) {
				String sql="FROM "+OrgDepartment.DATABASE_ENTITY+" where  departmentname  like ? ORDER BY ORDERINDEX,ID";
				Object[] value = {searchkey};
				return this.getHibernateTemplate().find(sql,value); 
			}
			
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows() {
				String sql="FROM "+OrgDepartment.DATABASE_ENTITY;
				List list=this.getHibernateTemplate().find(sql);
				return list.size();
			}
			

			
			/**
			 * 函数说明：获得一条的信息
			 * 参数说明： ID
			 * 返回值：对象
			 */
			public OrgDepartment getBoData(Long id) {
				if(id==null)id = new Long(0);
				OrgDepartment model = DepartmentCache.getInstance().getModel(id+"");
				if(model==null){
					model=(OrgDepartment)this.getHibernateTemplate().get(OrgDepartment.class,id);
				}
				return model;
			}
			/**
			 * 函数说明：获得一条的信息
			 * 参数说明： ID
			 * 返回值：对象
			 */
			public OrgDepartment getModelForDepartmentNo(String departmentno) {
				StringBuffer sql = new StringBuffer();
				sql.append("FROM ").append(OrgDepartment.DATABASE_ENTITY).append(" where ").append("departmentno = ?");
				DBUtilInjection d=new DBUtilInjection();
				OrgDepartment model = null;
				if(departmentno!=null&&!"".equals(departmentno)){
					if(d.HasInjectionData(departmentno)){
						return model;
					}
				}
				Object[] value = {departmentno};
					 List<OrgDepartment>  list = this.getHibernateTemplate().find(sql.toString(),value);
					 
					 if(list!=null&&list.size()>0){
						 model = list.get(0);
					 }
				return model;
			}
			
			/**
			 * 函数说明：执行部门激活/注销操作,如果当前状态为激活，则执行后注销，反之也然
			 * 参数说明： ID
			 * 返回值：对象
			 */
			public void execDisabled(Long id) {
				OrgDepartment orgDepartment = (OrgDepartment)this.getHibernateTemplate().get(OrgDepartment.class,id);
					orgDepartment.setDepartmentstate("1");
				this.getHibernateTemplate().update(orgDepartment);
				//======================================================================
				List<OrgExtendModel> list = OrgExtendFactory.getDeptExtendList();
				if(list!=null){
					OrgExtendDeptForWeiXinImpl qeufxi = null;
					for(OrgExtendModel orm:list){
						if (orm != null) {
							Constructor cons = null;
							try {
								cons = orm.getCons();
								if (cons != null) {
									Object[] params = {};
									qeufxi = (OrgExtendDeptForWeiXinImpl) cons.newInstance(params);
									if(qeufxi!=null){
										qeufxi.updateDepartment(orgDepartment);
									}
								}
							} catch (Exception e) {
								logger.error(e,e);
							}
						}
					}
				}
				DepartmentCache.getInstance().putModel(orgDepartment);
				//======================================================================
			}
			/**
			 * 执行激活操作
			 * @param id
			 */
			public void execActive(Long id) {
				OrgDepartment orgDepartment = (OrgDepartment)this.getHibernateTemplate().get(OrgDepartment.class,id);
				orgDepartment.setDepartmentstate("0");
				this.getHibernateTemplate().update(orgDepartment);
				//======================================================================
				List<OrgExtendModel> list = OrgExtendFactory.getDeptExtendList();
				if(list!=null){
					OrgExtendDeptForWeiXinImpl qeufxi = null;
					for(OrgExtendModel orm:list){
						if (orm != null) {
							Constructor cons = null;
							try {
								cons = orm.getCons();
								if (cons != null) {
									Object[] params = {};
									qeufxi = (OrgExtendDeptForWeiXinImpl) cons.newInstance(params);
									if(qeufxi!=null){
										qeufxi.updateDepartment(orgDepartment);
									}
								}
							} catch (Exception e) {
								logger.error(e,e);
							}
						}
					}
				}
				DepartmentCache.getInstance().putModel(orgDepartment);
				//======================================================================
			}

			/**
			 * 函数说明：获得最大ID
			 * 参数说明： 
			 * 返回值：最大ID
			 */
			public String getMaxID() {
				String date=UtilDate.getNowdate();
				String sql="SELECT MAX(id)+1 FROM "+OrgDepartment.DATABASE_ENTITY;
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
			 * 函数说明：获得最大OrderIndex
			 * 参数说明： 
			 * 返回值：最大OrderIndex
			 */
			public String getMaxOrderIndex() {
				String date=UtilDate.getNowdate();
				String sql="SELECT MAX(ORDERINDEX)+1 FROM "+OrgDepartment.DATABASE_ENTITY;
				String noStr = null;
				List ll = (List) this.getHibernateTemplate().find(sql);
				Iterator itr = ll.iterator();
				if (itr.hasNext()) {
					Object noint = itr.next();
					noStr = noint == null?"1":noint.toString();
				}
				return noStr;
			}

			/**
			 * 函数说明：修改信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void updateBoData(OrgDepartment pd) {
				this.getHibernateTemplate().update(pd);
				//======================================================================
				List<OrgExtendModel> list = OrgExtendFactory.getDeptExtendList();
				if(list!=null){
					OrgExtendDeptForWeiXinImpl qeufxi = null;
					for(OrgExtendModel orm:list){
						if (orm != null) {
							Constructor cons = null;
							try {
								cons = orm.getCons();
								if (cons != null) {
									Object[] params = {};
									qeufxi = (OrgExtendDeptForWeiXinImpl) cons.newInstance(params);
									if(qeufxi!=null){
										qeufxi.updateDepartment(pd);
									}
								}
							} catch (Exception e) {
								logger.error(e,e);
							}
						}
					}
				}
				//======================================================================
				DepartmentCache.getInstance().putModel(pd);
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("部门名称【").append(pd.getDepartmentname()).append("】,部门ID【").append(pd.getId()).append("】");
				LogUtil.getInstance().addLog(pd.getId(), LogInfoConstants.ORG_DEPARTMENT_UPDATE, log.toString());
			}

			/**
			 * 函数说明：查询信息
			 * 参数说明： 集合
			 * 返回值：
			 */
			public List queryBoDatas(String fieldname,String value) {
				String sql="FROM "+OrgDepartment.DATABASE_ENTITY+"  where ? like ?  ORDER BY ORDERINDEX,ID";
				DBUtilInjection d=new DBUtilInjection();
				List l =new ArrayList();
				
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
				Object[] valuearr = {fieldname,value};
				return this.getHibernateTemplate().find(sql,valuearr);
			}
			
			




			/**
			 * 获得首级部门列表
			 * @param parentid
			 * @return
			 */
			public List getTopDepartmentList(String companyid){ 
				String sql = "FROM "+OrgDepartment.DATABASE_ENTITY+" where COMPANYID = ? and (PARENTDEPARTMENTID is null or PARENTDEPARTMENTID='' or PARENTDEPARTMENTID=0) AND departmentstate=0   ORDER BY ORDERINDEX,ID";
				DBUtilInjection d=new DBUtilInjection();
				List l =new ArrayList();
				
				if(companyid!=null&&!"".equals(companyid)){
					if(d.HasInjectionData(companyid)){
						return l;
					}
				}
				Object[] value = {companyid};
				return this.getHibernateTemplate().find(sql,value);
			}
			
			/**
			 * 获得子部门列表
			 * @param parentid
			 * @return
			 */
			public List<OrgDepartment> getSubDepartmentList(Long companyid,Long parentid){
				StringBuffer sql =new StringBuffer();
				sql.append("FROM ").append(OrgDepartment.DATABASE_ENTITY).append(" where ");
				if(parentid==0||parentid.equals(new Long(0))){
					sql.append("(PARENTDEPARTMENTID = ? or PARENTDEPARTMENTID is null )");
				}else{
					sql.append("PARENTDEPARTMENTID = ?");
				}
				
				if(companyid!=null){
					sql.append(" AND companyid=?");
				}
				sql.append(" AND departmentstate=0  ORDER BY ORDERINDEX,ID");
				
				
				Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		    	Query query=session.createQuery(sql.toString()); 
		    	query.setLong(0, parentid);
		    	if(companyid!=null){
		    		query.setLong(1, companyid);
		    	}
		    	List<OrgDepartment> list =query.list();
				
				return list;
			}
			/**20180104
			 * 获得子部门列表
			 * @param parentid
			 * @return
			 */
			public List<OrgDepartment> getSubDepartmentLists(Long parentid,String searchOrg){
				Long companyid=null;
				StringBuffer sql =new StringBuffer();
				sql.append("FROM ").append(OrgDepartment.DATABASE_ENTITY).append(" where ");
				if(parentid==0||parentid.equals(new Long(0))){
					sql.append("(PARENTDEPARTMENTID = ? or PARENTDEPARTMENTID is null )");
				}else{
					sql.append("PARENTDEPARTMENTID = ?");
				}
				
				if(companyid!=null){
					sql.append(" AND companyid=?");
				}
				if(searchOrg!=null && !"".equals(searchOrg)){
					sql.append(" AND departmentname like ?");
				}
				sql.append(" AND departmentstate=0  ORDER BY ORDERINDEX,ID");
				
				
				Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		    	Query query=session.createQuery(sql.toString()); 
		    	query.setLong(0, parentid);
		    	int n=1;
		    	if(companyid!=null){
		    		query.setLong(n, companyid);
		    		n++;
		    	}
		    	if(searchOrg!=null && !"".equals(searchOrg)){
					query.setString(n, "%"+searchOrg+"%");
				}
		    	List<OrgDepartment> list =query.list();
				
				return list;
			}

			/**
			 * 获得子部门列表
			 * @param parentid
			 * @return
			 */
			public List<OrgDepartment> getSubDepartmentList(Long parentid){
				return this.getSubDepartmentList(null, parentid);
			}
			/**
			 * 查询部门
			 * @param searchOrg
			 * @return
			 */
			public List<OrgDepartment> getDept(String searchOrg){
				return this.getDept(null, searchOrg);
			}
			public List<OrgDepartment> getDept(Long companyid,String searchOrg){
				StringBuffer sql =new StringBuffer();
				sql.append("FROM ").append(OrgDepartment.DATABASE_ENTITY).append(" where 1=1 ");
				
				
				if(searchOrg!=null){
					DBUtilInjection d=new DBUtilInjection();
					List l =new ArrayList();
					
					if(searchOrg!=null&&!"".equals(searchOrg)){
						if(d.HasInjectionData(searchOrg)){
							return l;
						}
					}
					sql.append(" AND departmentname like ?");
				}
				sql.append(" AND departmentstate=0  ORDER BY ORDERINDEX,ID");
				
				
				Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		    	Query query=session.createQuery(sql.toString()); 
		    	
		    	query.setString(0, "%"+searchOrg+"%");
		    	
		    	List<OrgDepartment> list =query.list();
				
				return list;
			}
			/**
			 * 获得子部门列表
			 * @param parentid
			 * @return
			 */
			public int getSubDepartmentSize(Long parentid){
				String sql = "FROM "+OrgDepartment.DATABASE_ENTITY+" where PARENTDEPARTMENTID = ? AND departmentstate=0 ";
				Object[] value = {parentid};
				List list = this.getHibernateTemplate().find(sql,value);
				return list.size();
			}
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows(int companyid,Long parentdeptid) {
				String sql="";
				if(parentdeptid==0)
					sql=" FROM "+OrgDepartment.DATABASE_ENTITY+" where COMPANYID=? AND (PARENTDEPARTMENTID IS NULL or PARENTDEPARTMENTID = 0)";
				else
					sql="FROM "+OrgDepartment.DATABASE_ENTITY+" where COMPANYID=? AND PARENTDEPARTMENTID = ?";
				
				
				Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		    	Query query=session.createQuery(sql.toString()); 
		    	if(parentdeptid==0){
		    		query.setInteger(0, companyid);
		    	}else{
		    		query.setInteger(0, companyid);
		    		query.setLong(1, parentdeptid);
		    	}
		    	List list =query.list();
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
						Query query=session.createQuery(" FROM "+OrgDepartment.DATABASE_ENTITY+"  ORDER BY ORDERINDEX,ID");
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
			public List getBoDatas(int companyid,int parentdeptid,int pageSize, int startRow) {
				final int pageSize1=pageSize;
				final int startRow1=startRow;
				String sql="";
				if(parentdeptid==0)
					sql=" FROM "+OrgDepartment.DATABASE_ENTITY+" where COMPANYID=? AND (PARENTDEPARTMENTID IS NULL or PARENTDEPARTMENTID = 0)  ORDER BY ORDERINDEX,ID";
				else
					sql="FROM "+OrgDepartment.DATABASE_ENTITY+" where COMPANYID=? AND PARENTDEPARTMENTID = ?  ORDER BY ORDERINDEX,ID";
				
				final String sql1=sql;
				final int final_companyid = companyid;
				final int final_parentdeptid = parentdeptid;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						if(final_parentdeptid==0){
							query.setInteger(0, final_companyid);
						}else{
							query.setInteger(0, final_companyid);
							query.setInteger(1, final_parentdeptid);
						}
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
			public List getAllSubData(int companyid,int parentdeptid) {
				String sql="";
				if(parentdeptid==0)
					sql=" FROM "+OrgDepartment.DATABASE_ENTITY+" where COMPANYID=? AND (PARENTDEPARTMENTID IS NULL or PARENTDEPARTMENTID = 0)  ORDER BY ORDERINDEX,ID";
				else
					sql="FROM "+OrgDepartment.DATABASE_ENTITY+" where COMPANYID=? AND PARENTDEPARTMENTID = ?  ORDER BY ORDERINDEX,ID";
				
				Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		    	Query query=session.createQuery(sql.toString()); 
		    	if(parentdeptid==0){
		    		query.setInteger(0, companyid);
		    	}else{
		    		query.setInteger(0, companyid);
		    		query.setLong(1, parentdeptid);
		    	}
		    	List list =query.list();
				
				return list;//this.getHibernateTemplate().find(sql);
			}
			/**
			 * 调整模块排序
			 * @param orderindex
			 * @return
			 */
			public void updateIndex(int companyid,int parentdeptid,int id,String type){
				
				StringBuffer log = new StringBuffer();
				//判断当前orderindex 有空值的，进行初始化
				List<OrgDepartment> orderindexNullList = getOrderIndexIsNull();
				if(orderindexNullList!=null&&orderindexNullList.size()>0){
					for(OrgDepartment model:orderindexNullList){
						model.setOrderindex(model.getId().toString());
						this.updateBoData(model);
					}
				}
				int index=0;
				OrgDepartment snd1 = null;
				OrgDepartment snd2 = null;;
				String temp = "";
				String sql = "FROM "+OrgDepartment.DATABASE_ENTITY+" WHERE ID =?";
				Object[] value = {id};
				List downlist=this.getHibernateTemplate().find(sql,value);
				if(downlist!=null){
					if(downlist.size()>0){
						snd1 = (OrgDepartment)downlist.get(0);
						if(snd1!=null){
							log.append("部门名称【").append(snd1.getDepartmentname()).append("】,部门ID【").append(snd1.getId()).append("】");
						}
					}
				}
				if(type.equals("up")){
					if(parentdeptid==0)
						sql=" FROM "+OrgDepartment.DATABASE_ENTITY+" where companyid=? AND parentdepartmentid = 0 and orderindex <? order by orderindex desc";
					else
						sql = "FROM "+OrgDepartment.DATABASE_ENTITY+" WHERE companyid=? AND parentdepartmentid = ? and orderindex <? order by orderindex desc";
					log.append("调整顺序【向上】");
				}else{
					if(parentdeptid==0)
						sql = "FROM "+OrgDepartment.DATABASE_ENTITY+" WHERE  COMPANYID=? AND (PARENTDEPARTMENTID IS NULL or PARENTDEPARTMENTID = 0) and orderindex >? order by orderindex asc";
					else
						sql = "FROM "+OrgDepartment.DATABASE_ENTITY+" WHERE COMPANYID=? AND PARENTDEPARTMENTID = ? and orderindex >? order by orderindex asc";
					log.append("调整顺序【向下】");
				}
				
				
				
				Session session = this.getHibernateTemplate().getSessionFactory().openSession(); 
		    	Query query=session.createQuery(sql.toString()); 
		    	if(type.equals("up")){
					if(parentdeptid==0){
						query.setInteger(0, companyid);
						query.setString(1, snd1.getOrderindex());
					}else{
						query.setInteger(0, companyid);
						query.setInteger(1, parentdeptid);
						query.setString(2, snd1.getOrderindex());
					}
				}else{
					if(parentdeptid==0){
						query.setInteger(0, companyid);
						query.setString(1, snd1.getOrderindex());
					}else{
						query.setInteger(0, companyid);
						query.setInteger(1, parentdeptid);
						query.setString(2, snd1.getOrderindex());
					}
				}
		    	List uplist=query.list();
				
				
				
				//List uplist=this.getHibernateTemplate().find(sql);
					if(uplist!=null){
						if(uplist.size()>0){
							snd2 = (OrgDepartment)uplist.get(0);
						}
					}
					if(snd1!=null&&snd2!=null){
						temp = snd1.getOrderindex();
						snd1.setOrderindex(snd2.getOrderindex());
						//执行更新动作
						this.updateBoData(snd1);
						//=============================执行扩展操作=========================================
						List<OrgExtendModel> list = OrgExtendFactory.getDeptExtendList();
						if(list!=null){
							OrgExtendDeptForWeiXinImpl qeufxi = null;
							for(OrgExtendModel orm:list){
								if (orm != null) {
									Constructor cons = null;
									try {
										cons = orm.getCons();
										if (cons != null) {
											Object[] params = {};
											qeufxi = (OrgExtendDeptForWeiXinImpl) cons.newInstance(params);
											if(qeufxi!=null){
												qeufxi.updateDepartment(snd1);
											}
										}
									} catch (Exception e) {
										logger.error(e,e);
									}
								}
							}
						}
						//======================================================================
						snd2.setOrderindex(temp);
						this.updateBoData(snd2);
						//=============================执行扩展操作=========================================
						if(list!=null){
							OrgExtendDeptForWeiXinImpl qeufxi = null;
							for(OrgExtendModel orm:list){
								if (orm != null) {
									Constructor cons = null;
									try {
										cons = orm.getCons();
										if (cons != null) {
											Object[] params = {};
											qeufxi = (OrgExtendDeptForWeiXinImpl) cons.newInstance(params);
											if(qeufxi!=null){
												qeufxi.updateDepartment(snd2);
											}
										}
									} catch (Exception e) {
										logger.error(e,e);
									}
								}
							}
						}
						//======================================================================
					}
					//添加审计日志
					LogUtil.getInstance().addLog(new Long(id), LogInfoConstants.ORG_DEPARTMENT_ORDER, log.toString());
			}
			
			private List getOrderIndexIsNull(){
				String sql="FROM "+OrgDepartment.DATABASE_ENTITY+" where ORDERINDEX is null ORDER BY ID";
				return this.getHibernateTemplate().find(sql);
			}
}
