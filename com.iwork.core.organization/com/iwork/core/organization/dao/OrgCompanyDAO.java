package com.iwork.core.organization.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.log.operationlog.constant.LogInfoConstants;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.cache.CompanyCache;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.tools.UserContextUtil;

public class OrgCompanyDAO extends HibernateDaoSupport{
	public OrgCompanyDAO(){}
			
			/**
			 * 函数说明：添加信息
			 * 参数说明：对象 
			 * 返回值：
			 */
			public void addBoData(OrgCompany model) {
				this.getHibernateTemplate().save(model);
				CompanyCache.getInstance().putModel(model);
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("组织名称【").append(model.getCompanyname()).append("】,组织ID【").append(model.getId()).append("】");
				LogUtil.getInstance().addLog(Long.parseLong(model.getId()), LogInfoConstants.ORG_COMPANY_ADD, log.toString());
			}

			/**
			 * 函数说明：删除信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void deleteBoData(OrgCompany model) {
				if(model==null)return;
				this.getHibernateTemplate().delete(model);
				CompanyCache.getInstance().removeModel(model.getId());
				CompanyCache.getInstance().removeList();
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("组织名称【").append(model.getCompanyname()).append("】,组织ID【").append(model.getId()).append("】");
				LogUtil.getInstance().addLog(Long.parseLong(model.getId()), LogInfoConstants.ORG_COMPANY_DEL, log.toString());
			}

			/**
			 * 获得列表
			 * @return
			 */
			public List<OrgCompany> getCompanyList(String parentid,String searchKey){
				StringBuffer sql= new StringBuffer();
				List l = new ArrayList();
				List list=new ArrayList();
				Object[] values = null;
				sql.append("FROM ").append(OrgCompany.DATABASE_ENTITY);
				if(parentid.equals("999999")){ 
					sql.append(" where  parentid is null");
				} else if(parentid == null){
					sql.append(" where  parentid is null");
				}else{
					sql.append(" where parentid = ?");
					l.add(parentid);
				}
				if(searchKey!=null){
					sql.append(" and (companyno like ? or companyname like ?)");
					l.add("%"+searchKey+"%");
					l.add("%"+searchKey+"%");
				}
				sql.append(" ORDER BY COMPANYNAME,ORDERINDEX,ID");
				values = new Object[l.size()];
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < values.length; i++) {
					if(values[i]!=null && !"".equals(values[i].toString())){
						String params=values[i].toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return list;
						}
						}
					values[i]=l.get(i);
				}
				
				 list = this.getHibernateTemplate().find(sql.toString(),values);
				return  list; 
			}
			/**
			 * 获得列表
			 * @return
			 */
			public List<OrgCompany> getCompanyList(String parentid){
				StringBuffer sql= new StringBuffer();
				List l = new ArrayList();
				List list = new ArrayList();
				Object[] values = null;
				sql.append("FROM ").append(OrgCompany.DATABASE_ENTITY);
				if(parentid.equals("999999")){ 
					sql.append(" where  parentid is null");
				} else if(parentid == null){
					sql.append(" where  parentid is null");
				}else{
					sql.append(" where parentid = ?");
					l.add(parentid);
				}
				sql.append(" ORDER BY COMPANYNAME,ORDERINDEX,ID");
				values = new Object[l.size()];
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < values.length; i++) {
					if(values[i]!=null && !"".equals(values[i].toString())){
						String params=values[i].toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return list;
						}
						}
					values[i]=l.get(i);
				}
				 list = this.getHibernateTemplate().find(sql.toString(),values);
				return  list; 
			}
			
			/**
			 * 获得列表
			 * @return
			 */
			public List<OrgCompany> getCompanyListByID(String ID){
				List list=new ArrayList();
				StringBuffer sql= new StringBuffer();
				sql.append("FROM ").append(OrgCompany.DATABASE_ENTITY);
				sql.append(" where id = ?");
				sql.append(" ORDER BY COMPANYNAME,ORDERINDEX,ID");
				DBUtilInjection d=new DBUtilInjection();
				
				if(ID!=null&&!"".equals(ID)){
					if(d.HasInjectionData(ID)){
						return list;
					}
				}
				Object[] values = {ID};
				 list = this.getHibernateTemplate().find(sql.toString(),values);
				return  list; 
			}
			
			/**
			 * 获得上市或非上市公司列表
			 * @return
			 */
			public List<OrgCompany> getSSorFSSCompanyList(String parentid,String companyno,String type){
				StringBuffer sql= new StringBuffer();
				List l = new ArrayList();
				List lis = new ArrayList();
				Object[] values = null;
				sql.append("FROM ").append(OrgCompany.DATABASE_ENTITY);
				if(parentid.equals("999999")){ 
					sql.append(" where  parentid is null");
				} else if(parentid == null){
					sql.append(" where  parentid is null");
				}else{
					sql.append(" where parentid = ?");
					l.add(parentid);
				}
				if(companyno!=null && companyno.length()>2){
					sql.append(" and length(companyno)>4 ");
					if("SS".equals(type)){
						sql.append(" and companytype = '5' ");
					}else if("FSS".equals(type)){
						sql.append(" and companytype <> '5' ");
					}
				}
				
				sql.append(" ORDER BY ORDERINDEX,ID");
				values = new Object[l.size()];
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < values.length; i++) {
					if(values[i]!=null && !"".equals(values[i].toString())){
						String params=values[i].toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return lis;
						}
						}
					values[i]=l.get(i);
				}
				List list = this.getHibernateTemplate().find(sql.toString(),values);
				return  list; 
			}
			
			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */
			public List getAll() {
				List list = CompanyCache.getInstance().getList();
				if(list!=null){
					return list;
				}
				String sql="FROM OrgCompany ORDER BY ORDERINDEX,ID";
				list = this.getHibernateTemplate().find(sql);
				CompanyCache.getInstance().putList(list);////装载cache
				return  list;
			}
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows() {
				List list = CompanyCache.getInstance().getList();
				if(list!=null){
					return list.size();
				}else{
					String sql="FROM OrgCompany ORDER BY ORDERINDEX,ID";
					 list=this.getHibernateTemplate().find(sql);
					 //装载cache
					 CompanyCache.getInstance().putList(list);
				}
				return list.size();
				
			}
			
			/**
			 * 获得组织模型
			 * @param extend1
			 * @return
			 */
			public OrgCompany getCompanyModelForExtend1(String extend1){
				StringBuffer sql= new StringBuffer();
				List l = new ArrayList();
				Object[] values = null;
				OrgCompany model = null;
				sql.append("FROM ").append(OrgCompany.DATABASE_ENTITY);
				if(extend1!=null){ 
					sql.append(" where  extend1 =?");
					l.add(extend1);
				}
				sql.append(" ORDER BY ID");
				values = new Object[l.size()];
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < values.length; i++) {
					if(values[i]!=null && !"".equals(values[i].toString())){
						String params=values[i].toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return model;
						}
						}
					values[i]=l.get(i);
				}
				List<OrgCompany> list = this.getHibernateTemplate().find(sql.toString(),values);
				if(list!=null&&list.size()>0){
					model = list.get(0);
				}
				return model;
			}
			

			/**
			 * 函数说明：获得一条的信息
			 * 参数说明： ID
			 * 返回值：对象
			 */
			public OrgCompany getBoData(String id) {
				if(id==null)id = "0";
				//从cache中获取
				OrgCompany orgcompany = CompanyCache.getInstance().getModel(id);
				if(orgcompany!=null){
					return orgcompany;
				}else{
					  orgcompany = (OrgCompany)this.getHibernateTemplate().get(OrgCompany.class,id);
					  if(orgcompany!=null)
						  CompanyCache.getInstance().putModel(orgcompany);  //装载CACHE
					 return orgcompany;
				}
			}

			/**
			 * 函数说明：获得最大ID
			 * 参数说明： 
			 * 返回值：最大ID
			 */
			public String getMaxID() {
				String date=UtilDate.getNowdate();
				String sql="SELECT MAX(id)+1 FROM "+OrgCompany.DATABASE_ENTITY;
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
			public void updateBoData(OrgCompany pd) {
				this.getHibernateTemplate().update(pd);
				CompanyCache.getInstance().putModel(pd);  //装载CACHE
				
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("组织名称【").append(pd.getCompanyname()).append("】,组织ID【").append(pd.getId()).append("】");
				LogUtil.getInstance().addLog(Long.parseLong(pd.getId()), LogInfoConstants.ORG_COMPANY_UPDATE, log.toString());
			}

			/**
			 * 函数说明：查询信息
			 * 参数说明： 集合
			 * 返回值：
			 */
			public List queryBoDatas(String fieldname,String value) {
				String sql="FROM OrgCompany  where ? like ? ORDER BY ORDERINDEX,ID";
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
				Object[] values = null;
				if(fieldname==null||fieldname.equals("")||value==null||value.equals("")){
					sql=" FROM OrgCompany ORDER BY ORDERINDEX,ID";
					values = new Object[0];
				}else{
					sql=" FROM OrgCompany where ? like ? ORDER BY ORDERINDEX,ID";
					values = new Object[2];
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
					values[0]= fieldname;
					values[1]= "%"+value+"%";
					
				}
				List list=this.getHibernateTemplate().find(sql,values);
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
						Query query=session.createQuery(" FROM OrgCompany ORDER BY ORDERINDEX,ID");
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
			public List<OrgCompany> getListForCompanyno(String companyno) {
				String sql="";
				if(companyno!=null){
					sql=" FROM OrgCompany where companyno like ? ORDER BY ORDERINDEX,ID";
				}
				final String sql1=sql;
				final String final_companyno = companyno;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						if(final_companyno!=null){
							DBUtilInjection d=new DBUtilInjection();
							List l=new ArrayList();
							if(final_companyno!=null&&!"".equals(final_companyno)){
								if(d.HasInjectionData(final_companyno)){
									return l;
								}
							}
							query.setString(0, final_companyno);
						}
//						query.setFirstResult(startRow1);
//						query.setMaxResults(pageSize1);
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
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				String companyno = uc.get_companyModel().getCompanyno();
				
				// sql=" FROM OrgCompany ORDER BY ORDERINDEX,ID";
				if(queryName==null||queryName.equals("")||queryValue==null||queryValue.equals(""))
					sql=" FROM OrgCompany where companyno  like  ? ORDER BY ORDERINDEX,ID";
				else
					sql=" FROM OrgCompany where ? like ? and companyno  like ? ORDER BY ORDERINDEX,ID";
				
				final String sql1=sql;
				final String final_companyno = companyno;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						DBUtilInjection d=new DBUtilInjection();
						List l=new ArrayList();
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						if(queryName==null||queryName.equals("")||queryValue==null||queryValue.equals("")){
							if(final_companyno!=null&&!"".equals(final_companyno)){
								if(d.HasInjectionData(final_companyno)){
									return l;
								}
							}
							query.setString(0, final_companyno);
						}else{
							
								if(d.HasInjectionData(queryName)){
									return l;
								}
								if(d.HasInjectionData(queryValue)){
									return l;
								}
							
								if(final_companyno!=null&&!"".equals(final_companyno)){
									if(d.HasInjectionData(final_companyno)){
										return l;
									}
								}
							query.setString(0, queryName);
							
							query.setString(1, queryValue);
							query.setString(2, final_companyno);
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
			public List<OrgCompany> getBoDatas(String parentid,int pageSize, int startRow) {
				final int pageSize1=pageSize;
				final int startRow1=startRow;
				String sql="";
				if(parentid==null)
					sql=" FROM OrgCompany where parentid is null ORDER BY ORDERINDEX,ID";
				else
					sql=" FROM OrgCompany where parentid  =  ? ORDER BY ORDERINDEX,ID";
				
				final String sql1=sql;
				final String final_parentid = parentid;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						DBUtilInjection d=new DBUtilInjection();
						List l=new ArrayList();
						if(final_parentid==null){
							
						}else{
							
								if(d.HasInjectionData(final_parentid)){
									return l;
								}
							
							query.setString(0, final_parentid);
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
			public void updateIndex(int id,String type){
				//添加审计日志
				StringBuffer log = new StringBuffer();
				int index=0;
				OrgCompany snd1 = null;
				OrgCompany snd2 = null;;
				String temp = "";
				String sql = "FROM OrgCompany WHERE ID = ? ";
				
				Object[] value={id};
				List downlist=this.getHibernateTemplate().find(sql,value);
				if(downlist!=null){
					if(downlist.size()>0){
						snd1 = (OrgCompany)downlist.get(0);
						if(snd1!=null){
							log.append("组织名称【").append(snd1.getCompanyname()).append("】,组织ID【").append(snd1.getId()).append("】");
						}
					}
				}
				Object[] values = null;
				if(type.equals("up")){
						sql = "FROM OrgCompany WHERE orderindex < ? order by orderindex desc";
						log.append("调整顺序【向上】");
						values=new Object[1];
						values[0]=snd1.getOrderindex();
				}else{
						sql = "FROM OrgCompany WHERE orderindex > ? order by orderindex asc";
						log.append("调整顺序【向下】");
						values=new Object[1];
						values[0]=snd1.getOrderindex();
				}
				List uplist=this.getHibernateTemplate().find(sql,values);
					if(uplist!=null){
						if(uplist.size()>0){
							snd2 = (OrgCompany)uplist.get(0);
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
					LogUtil.getInstance().addLog(new Long(id), LogInfoConstants.ORG_COMPANY_ORDER, log.toString());
			}
}
