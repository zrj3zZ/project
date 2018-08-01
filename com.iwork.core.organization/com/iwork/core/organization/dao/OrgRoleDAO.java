package com.iwork.core.organization.dao;

import java.sql.ResultSet;
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
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.cache.RoleCache;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgRoleGroup;

public class OrgRoleDAO extends HibernateDaoSupport{
	public OrgRoleDAO(){}
			
			/**
			 * 函数说明：添加信息
			 * 参数说明：对象 
			 * 返回值：
			 */
			public void addBoData(OrgRole model) {
				this.getHibernateTemplate().save(model);
				RoleCache.getInstance().putModel(model);
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("角色名称【").append(model.getRolename()).append("】,角色ID【").append(model.getId()).append("】");
				LogUtil.getInstance().addLog(Long.parseLong(model.getId()), LogInfoConstants.ORG_ROLE_ADD, log.toString());
			}

			/**
			 * 函数说明：删除信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void deleteBoData(OrgRole model) {
				this.getHibernateTemplate().delete(model);
				RoleCache.getInstance().removeModel(model.getId());
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("角色名称【").append(model.getRolename()).append("】,角色ID【").append(model.getId()).append("】");
				LogUtil.getInstance().addLog(Long.parseLong(model.getId()), LogInfoConstants.ORG_ROLE_DEL, log.toString());
			}

			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */
			public List getAll() {
				String sql="FROM OrgRole ORDER BY ORDERINDEX,ID";
				return this.getHibernateTemplate().find(sql);
			}
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows() {
				String sql="FROM OrgRole ORDER BY ORDERINDEX,ID ";
				List list=this.getHibernateTemplate().find(sql);
				return list.size();
			}
			/**
			 * 函数说明：获得组对应的角色列表
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public List<OrgRole> getRoleList(Long groupid) {
				List<OrgRole> list=new ArrayList<OrgRole>();
				StringBuffer sql= new StringBuffer("FROM OrgRole ");
				if(groupid!=null){
					sql.append(" where groupid = ?");
				}
				sql.append(" ORDER BY ORDERINDEX,ID ");
				if(groupid!=null){
					Object[] value = {groupid.toString()};
					list=this.getHibernateTemplate().find(sql.toString(),value);
				}else{
					list=this.getHibernateTemplate().find(sql.toString());
				}
				return list;
			}
			
			/**
			 * 获得角色分组列表
			 * @return
			 */
			public List<OrgRoleGroup> getRoleGroupList(){
				String sql="FROM OrgRoleGroup ORDER BY id "; 
				List<OrgRoleGroup> list = this.getHibernateTemplate().find(sql);
				return list; 
				
			}
			 
			/**
			 * 获得角色分组列表
			 * @return
			 */
			public OrgRoleGroup getRoleGroupModel(Long groupId){
				OrgRoleGroup model = (OrgRoleGroup)this.getHibernateTemplate().get(OrgRoleGroup.class,groupId);
				return model;
				
			}
			/**
			 * 保存分组信息
			 * @param model
			 */
			public void saveGroup(OrgRoleGroup model){
				if(model!=null){
					if(model.getId()==null){
						this.getHibernateTemplate().save(model);
					}else{
						this.getHibernateTemplate().update(model);
					}
				}
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("角色组【").append(model.getGroupName()).append("】,ID【").append(model.getId()).append("】");
				LogUtil.getInstance().addLog(model.getId(), LogInfoConstants.ORG_ROLEGROUP_ADD, log.toString());
			}
			/**
			 * 删除分组信息
			 * @param model
			 */
			public void delGroup(OrgRoleGroup model){
				if(model!=null){
						this.getHibernateTemplate().delete(model);
						//添加审计日志
						StringBuffer log = new StringBuffer();
						log.append("角色组【").append(model.getGroupName()).append("】,角色组ID【").append(model.getId()).append("】");
						LogUtil.getInstance().addLog(model.getId(), LogInfoConstants.ORG_ROLEGROUP_DEL, log.toString());
				}
			}
	
			/**
			 * 函数说明：获得一条的信息
			 * 参数说明： ID
			 * 返回值：对象
			 */
			public OrgRole getBoData(String id) {
				if(id==null||id.equals("null"))id = "0";
				OrgRole model = (OrgRole)RoleCache.getInstance().getModel(id);
				if(model==null){
					model = (OrgRole)this.getHibernateTemplate().get(OrgRole.class,id);
				}
				return model;
			}

			/**
			 * 函数说明：获得最大ID
			 * 参数说明： 
			 * 返回值：最大ID
			 */
			public String getMaxID() {
				String date=UtilDate.getNowdate();
				String sql="SELECT MAX(id)+1 FROM OrgRole ";
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
			public void updateBoData(OrgRole pd) {
				this.getHibernateTemplate().update(pd);
				RoleCache.getInstance().putModel(pd);
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("角色名称【").append(pd.getRolename()).append("】,角色ID【").append(pd.getId()).append("】");
				LogUtil.getInstance().addLog(Long.parseLong(pd.getId()), LogInfoConstants.ORG_ROLE_UPDATE, log.toString());
			}

			/**
			 * 函数说明：查询信息
			 * 参数说明： 集合
			 * 返回值：
			 */
			public List queryBoDatas(String fieldname,String value) {
				String sql="FROM OrgRole  where ? like ? ORDER BY ORDERINDEX,ID";
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
				Object[] values = {fieldname,value};
				return this.getHibernateTemplate().find(sql,values);
			}
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows(String fieldname,String value) {
				List list=new ArrayList();
				String sql="";
				if(fieldname==null||fieldname.equals("")||value==null||value.equals(""))
					sql=" FROM OrgRole ORDER BY ORDERINDEX,ID ";
				else	
					sql=" FROM OrgRole where ? like ? ORDER BY ORDERINDEX,ID ";
				
				if(fieldname==null||fieldname.equals("")||value==null||value.equals("")){
					list=this.getHibernateTemplate().find(sql);
				}else{
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
						Query query=session.createQuery(" FROM OrgRole ORDER BY ORDERINDEX,ID");
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
			public List getBoDatas(String fieldname,String value,int pageSize, int startRow) {
				final int pageSize1=pageSize;
				final int startRow1=startRow;
				final String queryName=fieldname;
				final String queryValue=value;
				String sql="";
				
				if(queryName==null||queryName.equals("")||queryValue==null||queryValue.equals(""))
					sql=" FROM OrgRole ORDER BY ORDERINDEX,ID ";
				else
					sql="FROM OrgRole where ? like ? ORDER BY ORDERINDEX,ID";
				
				final String sql1=sql;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						if(queryName==null||queryName.equals("")||queryValue==null||queryValue.equals("")){
							
						}else{
							DBUtilInjection d=new DBUtilInjection();
							List list=new ArrayList();
							if(queryName!=null&&!"".equals(queryName)){
								if(d.HasInjectionData(queryName)){
									return list;
								}
							}
							if(queryValue!=null&&!"".equals(queryValue)){
								if(d.HasInjectionData(queryValue)){
									return 0;
								}
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
			 * get all role type
			 */
			public List<OrgRoleGroup> getRoleTypeList() {
				java.sql.Connection conn = null;
		        java.sql.Statement stmt = null;
		        java.sql.ResultSet rs = null;
		        List list = new ArrayList();
		        try {
		        	conn = DBUtil.open();
	                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,  
	                        ResultSet.CONCUR_READ_ONLY);
	                String sql="select ID,GROUP_NAME,MASTER,MEMO from org_role_group ";
	                rs = stmt.executeQuery(sql);
	                if (rs != null) {
	                	while (rs.next()) {
	                		OrgRoleGroup org = new OrgRoleGroup();
	                		org.setGroupName(rs.getString("GROUP_NAME"));
	                		org.setId(rs.getLong("ID"));
	                		org.setMaster(rs.getString("MASTER"));
	                		org.setMemo(rs.getString("MEMO"));
	                		list.add(org);
	                	}
	                }
		        } catch (Exception e) {
		        	logger.error(e,e);
		        } finally {
		        	DBUtil.close(conn, stmt, rs);
		        }			
				return list;
			}
			/**
			 * 调整模块排序
			 * @param id
			 * @param type
			 */
			public void updateIndex(int id,String type){
				OrgRole snd1 = null;
				OrgRole snd2 = null;
				String temp = null;
				String sql = " FROM OrgRole where id = ?";
				Object[] value = {id};
				List downlist = this.getHibernateTemplate().find(sql,value);
				if(downlist!=null){
					if(downlist.size()>0){
						snd1 = (OrgRole) downlist.get(0);
					}
				}
				if(type.equals("up")){
					sql = " FROM OrgRole WHERE orderindex < ? order by orderindex desc";
				}else{
					sql = " FROM OrgRole WHERE orderindex > ? order by orderindex asc";
				}
				DBUtilInjection d=new DBUtilInjection();
				List uplist=null;
				if(snd1.getOrderindex()!=null&&!"".equals(snd1.getOrderindex())){
					if(d.HasInjectionData(snd1.getOrderindex())){
						return;
					}
				}
				
				Object[] values = {snd1.getOrderindex()};
				uplist = this.getHibernateTemplate().find(sql,values);
				if(uplist!=null){
					if(uplist.size()>0){
						snd2 = (OrgRole) uplist.get(0);
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
			}
}
