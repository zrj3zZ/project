package com.iwork.core.organization.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.cache.UserMapCache;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgUserMap;

public class OrgUserMapDAO extends HibernateDaoSupport{
	public OrgUserMapDAO(){}
			
			/**
			 * 函数说明：添加信息
			 * 参数说明：对象 
			 * 返回值： 
			 */
			public void addBoData(OrgUserMap model) {
				if(model!=null&&model.getUserid()!=null){
					UserMapCache.getInstance().removeList(model.getUserid());
				}
				this.getHibernateTemplate().save(model);
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("用户账号【").append(model.getUserid()).append("】,角色名称/ID【").append(model.getOrgrolename()).append(",").append(model.getOrgroleid()).append("】,部门名称/ID【").append(model.getUserid()).append("").append(model.getDepartmentid()).append("】");
				LogUtil.getInstance().addLog(Long.parseLong(model.getId()), LogInfoConstants.ORG_USERMAP_ADD, log.toString());
			}

			/**
			 * 函数说明：删除信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void deleteBoData(OrgUserMap model) {
				if(model!=null&&model.getUserid()!=null){
					UserMapCache.getInstance().removeList(model.getUserid());
				}
				this.getHibernateTemplate().delete(model);
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("用户账号【").append(model.getUserid()).append("】,角色名称/ID【").append(model.getOrgrolename()).append(",").append(model.getOrgroleid()).append("】,部门名称/ID【").append(model.getUserid()).append("").append(model.getDepartmentid()).append("】");
				LogUtil.getInstance().addLog(Long.parseLong(model.getId()), LogInfoConstants.ORG_USERMAP_DEL, log.toString());
			}

			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */
			public List getAll() {
				String sql="FROM OrgUserMap ORDER BY ID";
				return this.getHibernateTemplate().find(sql);
			}
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows() {
				String sql="FROM OrgUserMap ORDER BY ID ";
				List list=this.getHibernateTemplate().find(sql);
				return list.size();
			}
			
			/**
			 * 函数说明：获得置顶用户的兼任用户角色
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List<OrgUserMap> getOrgUserMapList(String userid){
				List<OrgUserMap> list = UserMapCache.getInstance().getList(userid);
				DBUtilInjection d=new DBUtilInjection();
				List<OrgUserMap> l=new ArrayList<OrgUserMap>();
				if(userid!=null&&!"".equals(userid)){
					if(d.HasInjectionData(userid)){
						return l;
					}
				}
				if(list==null){
					String sql=" FROM OrgUserMap WHERE userid =? ORDER BY ID";
					final String sql1=sql.toString();
					final String f_userid=userid;
					 list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
						public Object doInHibernate(org.hibernate.Session session)
								throws HibernateException, SQLException {
							// TODO 自动生成方法存根
							Query query=session.createQuery(sql1);
							query.setString(0,f_userid); 
							return query.list();
						}
					});
				}
				return list;
					
			}
			/**
			 * 函数说明：根据用户帐号获得一条的信息
			 * 参数说明： ID
			 * 返回值：对象
			 */
			public OrgUserMap getUserModel(String id) {
				DBUtilInjection d=new DBUtilInjection();
				
				if(id!=null&&!"".equals(id)){
					if(d.HasInjectionData(id)){
						return null;
					}
				}
				return (OrgUserMap)this.getHibernateTemplate().get(OrgUserMap.class,id);
			}
			
			/**
			 * 根据角色ID，获取对应的兼任职位人员列表
			 * @param orgroleid
			 * @return
			 */
			public List<OrgUserMap> getUserMapList(String orgroleid){
				String sql=" FROM OrgUserMap WHERE orgroleid =? ORDER BY ID";
				DBUtilInjection d=new DBUtilInjection();
				List<OrgUserMap> l=new ArrayList<OrgUserMap>();
				if(orgroleid!=null&&!"".equals(orgroleid)){
					if(d.HasInjectionData(orgroleid)){
						return l;
					}
				}
				final String sql1=sql.toString();
				final String f_orgroleid=orgroleid;
				List<OrgUserMap> list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						query.setString(0,f_orgroleid); 
						return query.list();
					}
				});
				return list;
			}

			/**
			 * 函数说明：获得最大ID
			 * 参数说明： 
			 * 返回值：最大ID
			 */
			public String getMaxID() {
				String date=UtilDate.getNowdate();
				String sql="SELECT MAX(id)+1 FROM OrgUserMap ";
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
				return noStr;
			}

			/**
			 * 函数说明：修改信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void updateBoData(OrgUserMap model) {
				if(model!=null&&model.getUserid()!=null){
					UserMapCache.getInstance().removeList(model.getUserid());
				}
				this.getHibernateTemplate().update(model);
			}

			/**
			 * 函数说明：查询信息
			 * 参数说明： 集合
			 * 返回值：
			 */
			public List queryBoDatas(String fieldname,String value) {
				String sql="FROM OrgUserMap  where ? like ? ORDER BY ID";
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
			public int getRows(String userid) {
				String sql="";	
					sql=" FROM OrgUserMap WHERE userid =? ORDER BY ID ";
					DBUtilInjection d=new DBUtilInjection();
					
					if(userid!=null&&!"".equals(userid)){
						if(d.HasInjectionData(userid)){
							return 0;
						}
					}
					final String sql1=sql.toString();
					final String f_userid=userid;
					List<OrgUserMap> list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
						public Object doInHibernate(org.hibernate.Session session)
								throws HibernateException, SQLException {
							// TODO 自动生成方法存根
							Query query=session.createQuery(sql1);
							query.setString(0,f_userid); 
							return query.list();
						}
					});
					
				return list.size();
			}
			
			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List getOrgUserMapList(int pageSize, int startRow) throws HibernateException {
				final int pageSize1=pageSize;
				final int startRow1=startRow;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public List doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
						Query query=session.createQuery(" FROM OrgUserMap ORDER BY ID");
			             query.setFirstResult(startRow1);
			             query.setMaxResults(pageSize1);
			             return query.list();
					}
				});
					
			}
			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List getOrgUserMapList(String userid,int pageSize, int startRow) throws HibernateException {		
				StringBuffer sql2 = new StringBuffer();
				sql2.append("select t.* from(select rownum num,u.* from(");
				sql2.append("select u.*,c.companyname,c.companyno from OrgUserMap u,OrgDepartment d,OrgCompany c where 1=1");
				sql2.append(" and u.userid = ?");
				sql2.append(" and u.departmentid=d.id and d.companyid=c.id order by u.id");
				sql2.append(") u where rownum<=?) t where t.num>?");
		        List list  = new ArrayList();
				Connection conn = DBUtil.open();
				PreparedStatement stmt = null;
				ResultSet rset = null;
				try{
					stmt = conn.prepareStatement(sql2.toString());
					stmt.setString(1, userid);
					stmt.setInt(2, startRow+pageSize);
					stmt.setInt(3, startRow);
					rset = stmt.executeQuery();
					while(rset.next()){
						Long id = rset.getLong("ID");
						String userId = rset.getString("USERID");
						if(userId==null){
							userId = "";
						}
						Long departmentid = rset.getLong("DEPARTMENTID");
						
						String departmentname = rset.getString("DEPARTMENTNAME");
						if(departmentname==null){
							departmentname = "";
						} 
						
						String orgroleid = rset.getString("ORGROLEID");
						if(orgroleid==null){
							orgroleid = "";
						}
						String orgrolename = rset.getString("ORGROLENAME");
						if(orgrolename==null){
							orgrolename = "";
						} 
						Long ismanager = rset.getLong("ISMANAGER");
						String companyname = rset.getString("COMPANYNAME");
						if(companyname==null){
							companyname = "";
						} 
						String companyno = rset.getString("COMPANYNO");
						if(companyno==null){
							companyno = "";
						}
						String usermaptype = rset.getString("USERMAPTYPE");
						if(usermaptype==null){
							usermaptype = "";
						}
						
						HashMap hash = new HashMap();
						hash.put("id", id);
						hash.put("userid", userId);
						hash.put("departmentid", departmentid);
						hash.put("departmentname", departmentname);
						hash.put("orgroleid", orgroleid);
						hash.put("orgrolename", orgrolename);
						hash.put("ismanager", ismanager);
						hash.put("companyname", companyname);
						hash.put("companyno", companyno);
						hash.put("usermaptype", usermaptype);
						
						list.add(hash);
					}
				}catch(Exception e){
					logger.error(e,e);
				}finally{
					DBUtil.close(conn, stmt, rset);
				} 
				return list;
			}
			
			/**
			 * 函数说明：获得兼职和角色所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List getOrgUserMapAndOrgUserRoleList(String userid,int pageSize, int startRow) throws HibernateException {
				final int pageSize1=pageSize;
				final int startRow1=startRow;
				String sql="FROM OrgUserMap as userMap,OrgRole as role WHERE userMap.orgroleid=role.id AND userMap.userid =? ORDER BY userMap.id";
				DBUtilInjection d=new DBUtilInjection();
				List l=new ArrayList();
				if(userid!=null&&!"".equals(userid)){
					if(d.HasInjectionData(userid)){
						return l;
					}
				}
				final String sql1=sql;
				final String f_userid=userid;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public List doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						Query query=session.createQuery(sql1);
						query.setString(0,f_userid); 
						query.setFirstResult(startRow1);
						query.setMaxResults(pageSize1);
						List list = query.list();
						return query.list();
					}
				});
				
			}
			
			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List getUserMapListByDeptRole(Long deptId,List<OrgRole> arrayList){
				StringBuffer sql= new StringBuffer();
				sql.append(" FROM OrgUserMap WHERE departmentid = ?");
				StringBuffer roleCondition = new StringBuffer();
				roleCondition.append(" id = -1 ");
				boolean isCondition = false;
				if(arrayList!=null&&arrayList.size()>0){
					for(OrgRole role:arrayList){
						roleCondition.append(" or orgroleid = ?");
						isCondition = true;
					}
				} 
				if(isCondition){
					sql.append(" and (").append(roleCondition).append(") order by ID");
				}
				final String sql1=sql.toString();
				final Long final_deptId = deptId;
				final List<OrgRole> final_arrayList = arrayList;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						int i=0;
						query.setLong(i, final_deptId);i++;
						if(final_arrayList!=null&&final_arrayList.size()>0){
							for(OrgRole role:final_arrayList){
								query.setString(i, role.getId());i++;
							}
						}
						return query.list();
					}
				});
				
			}
			
			
			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List getOrgUserMapListForUser(String userid){
				String sql=" FROM OrgUserMap WHERE userid =? ORDER BY ID";
				DBUtilInjection d=new DBUtilInjection();
				List l=new ArrayList();
				if(userid!=null&&!"".equals(userid)){
					if(d.HasInjectionData(userid)){
						return l;
					}
				}
				Object[] value = {userid};
				return this.getHibernateTemplate().find(sql,value);
				
			}
			
			/**
			 * 函数说明：查询信息
			 * 参数说明： 集合
			 * 返回值：
			 */
			public List getSearchUserLists(String fieldname,String value,int pageSize, int startRow) {
				final int pageSize1=pageSize;
				final int startRow1=startRow;
				final String queryName=fieldname;
				final String queryValue=value;
				String sql="";
				
				if(queryName==null||queryName.equals("")||queryValue==null||queryValue.equals(""))
					sql=" FROM OrgUserMap ORDER BY ID ";
				else
					sql="FROM OrgUserMap where ? like ? ORDER BY ID";
				
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
			 * 获得指定部门下存在的用户兼职列表
			 * @param departmentid
			 * @return
			 * @throws HibernateException
			 */
			public List<OrgUserMap> getOrgUserMap_DeptId(Long departmentid) throws HibernateException{
				String sql=" FROM OrgUserMap WHERE DEPARTMENTID = ? AND USERMAPTYPE = 0 ORDER BY ID";
				Object[] value = {departmentid};
				return this.getHibernateTemplate().find(sql,value);
			}
			
			/**
			 * 获得指定部门下存在的用户兼职列表
			 * @param departmentid
			 * @return
			 * @throws HibernateException
			 */
			public List getOrgUserMapAndOrgRoleDeptId(Long departmentid) throws HibernateException{
				String sql=" FROM OrgUserMap as usermap,OrgRole as role WHERE usermap.orgroleid=role.id AND usermap.departmentid = ? AND usermap.usermaptype = 0 ORDER BY usermap.id";
				Object[] value = {departmentid};
				return this.getHibernateTemplate().find(sql,value);
			}
			
			/**
			 * @param departmentid
			 * @param type OrgUserMap.USER_TYPE_ORG 组织兼任 ，USER_TYPE_SYS 系统兼任
			 * @return
			 * @throws HibernateException 
			 */  
			public List<OrgUserMap> getOrgUserMap_DeptId(int departmentid,String type) throws HibernateException{
				String sql=" FROM OrgUserMap WHERE DEPARTMENTID = ? AND USERMAPTYPE = ? ORDER BY ID";
				DBUtilInjection d=new DBUtilInjection();
				List<OrgUserMap> l=new ArrayList<OrgUserMap>();
				if(type!=null&&!"".equals(type)){
					if(d.HasInjectionData(type)){
						return l;
					}
				}
				Object[] value = {departmentid,type};
				return this.getHibernateTemplate().find(sql,value); 
			} 
			 
}
