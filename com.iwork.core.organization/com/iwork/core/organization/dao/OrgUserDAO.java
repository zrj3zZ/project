package com.iwork.core.organization.dao;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.log.operationlog.constant.LogInfoConstants;
import com.iwork.app.log.util.LogUtil;
import com.iwork.app.weixin.org.service.WeiXinOrgService;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.cache.UserCache;
import com.iwork.core.organization.factory.OrgExtendFactory;
import com.iwork.core.organization.impl.OrgExtendUserForWeiXinImpl;
import com.iwork.core.organization.model.OrgExtendModel;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgUser;

public class OrgUserDAO extends HibernateDaoSupport{
	public   Session session = null;  
	private WeiXinOrgService weiXinOrgService;
	public  HibernateTemplate hibernateTemplate;
	public OrgUserDAO(){}	
			/**
			 * 函数说明：添加信息
			 * 参数说明：对象 
			 * 返回值： 
			 */
			public void addBoData(OrgUser model) {
				if(model.getOrderindex()==null){
					model.setOrderindex(model.getId());
				}
				this.getHibernateTemplate().save(model);
				UserCache.getInstance().putModel(model);
				List<OrgExtendModel> list = OrgExtendFactory.getUserExtendList();
				if(list!=null){
					OrgExtendUserForWeiXinImpl qeufxi = null;
					for(OrgExtendModel orm:list){
						if (orm != null) {
							Constructor cons = null;
							try {
								cons = orm.getCons();
								if (cons != null) {
									Object[] params = {};
									qeufxi = (OrgExtendUserForWeiXinImpl) cons.newInstance(params);
									if(qeufxi!=null){
										qeufxi.addOrgUser(model);
									}
								}
							} catch (Exception e) {
								logger.error(e,e);
							}
						}
					}
				}
				UserCache.getInstance().putModel(model); 
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("用户名称【").append(model.getUsername()).append("】,用户账号【").append(model.getUserid()).append("】");
				LogUtil.getInstance().addLog(model.getId(), LogInfoConstants.ORG_USER_ADD, log.toString());
			}

			/**
			 * 函数说明：删除信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void deleteBoData(OrgUser model) {
				this.getHibernateTemplate().delete(model);
				
				UserCache.getInstance().removeModel(model.getUserid());
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("用户名称【").append(model.getUsername()).append("】,用户账号【").append(model.getUserid()).append("】");
				LogUtil.getInstance().addLog(model.getId(), LogInfoConstants.ORG_USER_DEL, log.toString());
			}

			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */
			public List<OrgUser> getAll() {
				String sql="FROM OrgUser ORDER BY ORDERINDEX,ID ";
				return this.getHibernateTemplate().find(sql);
			}
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明： 
			 * 返回值：总行数
			 */
			public int getRows() {
				String sql="FROM OrgUser ORDER BY ORDERINDEX,ID ";
				List list=this.getHibernateTemplate().find(sql);
				return list.size();
			}
			
			
			/**
			 * 函数说明：根据用户ID获得一条的信息
			 * 参数说明： ID
			 * 返回值：对象
			 */
			public OrgUser getModel(String id) {
				String sql="FROM OrgUser where id=? ORDER BY ORDERINDEX,ID ";
				final String sql1=sql.toString();
				final String id1=id;
				List<OrgUser> list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						query.setString(0,id1); 
						return query.list();
					}
				});
				
				if(list!=null&&list.size()>0){
					OrgUser orgUser = (OrgUser)list.get(0);
					return orgUser;
				}else{
					return null;
				}
			}
			
			/**
			 * 函数说明：根据用户帐号获得一条的信息
			 * 参数说明： userid
			 * 返回值：对象
			 */
			public OrgUser getUserModel(String userid) {
				OrgUser orgUser = UserCache.getInstance().getModel(userid);
				DBUtilInjection d=new DBUtilInjection();
				
				if(userid!=null&&!"".equals(userid)){
					if(d.HasInjectionData(userid)){
						return null;
					}
				}
				if(orgUser==null){
					String sql="FROM OrgUser where userid=? ORDER BY ORDERINDEX,ID ";
					final String sql1=sql.toString();
					final String f_userid=userid;
					List<OrgUser> list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
						public Object doInHibernate(org.hibernate.Session session)
								throws HibernateException, SQLException {
							// TODO 自动生成方法存根
							Query query=session.createQuery(sql1);
							
							query.setString(0,f_userid); 
							return query.list();
						}
					});
					
					if(list!=null&&list.size()>0){
						orgUser = (OrgUser)list.get(0);
						return orgUser;
					}else{
						return null;
					}
				}
				return orgUser;
			}
			
			/**
			 * 函数说明：根据用户帐号获得一条的信息
			 * 参数说明： userid
			 * 返回值：对象
			 */
			public List<Object[]> getUserModelAndOrgRole(String userid) {
				List<Object[]> list = new ArrayList<Object[]>();
				DBUtilInjection d=new DBUtilInjection();
				
				if(userid!=null&&!"".equals(userid)){
					if(d.HasInjectionData(userid)){
						return list;
					}
				}
				String sql="FROM OrgUser as orguser,OrgRole as role where orguser.orgroleid=role.id AND userid=? ORDER BY orguser.orderindex,orguser.id ";
				final String sql1=sql.toString();
				final String f_userid=userid;
				List<Object[]> listObj = this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						query.setString(0,f_userid); 
						return query.list();
					}
				});
				if(listObj.get(0)!=null&&listObj.get(0).length>0){
					list.add(listObj.get(0));
					return list;
				}else{
					return list;
				}
			}
			
			/**
			 * 执行用户注销操作
			 * 如果当前状态为激活，则执行后注销，反之也然
			 * @param userid
			 */
			public void execDisabled(String userid){
				String sql="FROM OrgUser where  userid=? ORDER BY ORDERINDEX,ID ";
				final String sql1=sql.toString();
				final String f_userid=userid;
				DBUtilInjection d=new DBUtilInjection();
				
				if(userid!=null&&!"".equals(userid)){
					if(d.HasInjectionData(userid)){
						return;
					}
				}
				List<OrgUser> list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						query.setString(0,f_userid); 
						return query.list();
					}
				});
				if(list!=null&&list.size()>0){
					OrgUser orgUser = (OrgUser)list.get(0);
					orgUser.setUserstate(new Long(1));
					this.getHibernateTemplate().update(orgUser);
					//
					List<OrgExtendModel> extendlist = OrgExtendFactory.getUserExtendList();
					if(extendlist!=null){
						OrgExtendUserForWeiXinImpl qeufxi = null;
						for(OrgExtendModel orm:extendlist){
							if (orm != null) {
								Constructor cons = null;
								try {
									cons = orm.getCons();
									if (cons != null) {
										Object[] params = {};
										qeufxi = (OrgExtendUserForWeiXinImpl) cons.newInstance(params);
										if(qeufxi!=null){
											qeufxi.addOrgUser(orgUser);
										}
									}
								} catch (Exception e) {
									logger.error(e,e);
								}
							}
						}
					}
					UserCache.getInstance().putModel(orgUser);
					//添加审计日志
					StringBuffer log = new StringBuffer();
					log.append("用户名称【").append(orgUser.getUsername()).append("】,用户账号【").append(orgUser.getUserid()).append("】");
					LogUtil.getInstance().addLog(orgUser.getId(), LogInfoConstants.ORG_USER_DISABLE, log.toString());
				}
			}
			/**
			 * 执行用户激活操作
			 * 如果当前状态为激活，则执行后注销，反之也然
			 * @param userid
			 */
			public void execActivating(String userid){
				String sql="FROM OrgUser where userid=? ORDER BY ORDERINDEX,ID ";
				DBUtilInjection d=new DBUtilInjection();
				
				if(userid!=null&&!"".equals(userid)){
					if(d.HasInjectionData(userid)){
						return;
					}
				}
				final String sql1=sql.toString();
				final String f_userid=userid;
				List<OrgUser> list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						query.setString(0,f_userid); 
						return query.list();
					}
				});
				if(list!=null&&list.size()>0){
					OrgUser orgUser = (OrgUser)list.get(0);
					orgUser.setUserstate(new Long(0));
					this.getHibernateTemplate().update(orgUser);
					List<OrgExtendModel> extendlist = OrgExtendFactory.getUserExtendList();
					if(extendlist!=null){
						OrgExtendUserForWeiXinImpl qeufxi = null;
						for(OrgExtendModel orm:extendlist){
							if (orm != null) {
								Constructor cons = null;
								try {
									cons = orm.getCons();
									if (cons != null) {
										Object[] params = {};
										qeufxi = (OrgExtendUserForWeiXinImpl) cons.newInstance(params);
										if(qeufxi!=null){
											qeufxi.addOrgUser(orgUser);
										}
									}
								} catch (Exception e) {
									logger.error(e,e);
								}
							}
						}
					}
					UserCache.getInstance().putModel(orgUser);
					//添加审计日志
					StringBuffer log = new StringBuffer();
					log.append("用户名称【").append(orgUser.getUsername()).append("】,用户账号【").append(orgUser.getUserid()).append("】");
					LogUtil.getInstance().addLog(orgUser.getId(), LogInfoConstants.ORG_USER_ACTIVE, log.toString());
				}
			}
			
			/**
			 * 函数说明：获得最大ID
			 * 参数说明： 
			 * 返回值：最大ID
			 */
			public String getMaxID() {
				String date=UtilDate.getNowdate();
				String sql="SELECT MAX(id)+1 FROM OrgUser ";
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
			 * 函数说明：获得最大ORDERINDEX
			 * 参数说明： 
			 * 返回值：最大orderindex
			 */
			public String getMaxOrderIndex(){
				String date=UtilDate.getNowdate();
				String sql="SELECT MAX(ORDERINDEX)+1 FROM OrgUser";
				String noStr = null;
				List ll = (List) this.getHibernateTemplate().find(sql);
				Iterator itr = ll.iterator();
				if (itr.hasNext()) {
					Object noint = itr.next();
					noStr = noint==null?"1":noint.toString();
				}
				return noStr;
			}

			/**
			 * 函数说明：修改信息
			 * 参数说明： 对象
			 * 返回值：
			 */
			public void updateBoData(OrgUser orgUser) {
				if(orgUser.getOrgroleid()==null){
					orgUser.setOrgroleid(orgUser.getId());
				}
				this.getHibernateTemplate().update(orgUser);
				
				/*//执行扩展
				List<OrgExtendModel> list = OrgExtendFactory.getUserExtendList();
				if(list!=null){
					OrgExtendUserForWeiXinImpl qeufxi = null;
					for(OrgExtendModel orm:list){
						if (orm != null) {
							Constructor cons = null;
							try {
								cons = orm.getCons();
								if (cons != null) {
									Object[] params = {};
									qeufxi = (OrgExtendUserForWeiXinImpl) cons.newInstance(params);
									if(qeufxi!=null){
										qeufxi.updateOrgUser(orgUser);
									}
								}
							} catch (Exception e) {
								logger.error(e,e);
							}
						}
					}
				}*/
				UserCache.getInstance().putModel(orgUser);
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("用户名称【").append(orgUser.getUsername()).append("】,用户账号【").append(orgUser.getUserid()).append("】");
				LogUtil.getInstance().addLog(orgUser.getId(), LogInfoConstants.ORG_USER_UPDATE, log.toString());
			}

			
			
			/**
			 * 函数说明：获得总行数
			 * 参数说明：部门编号
			 * 返回值：总行数
			 */
			public int getRows(Long departmentid) {
				String sql="";	
					sql=" FROM OrgUser WHERE DEPARTMENTID = ? ORDER BY ORDERINDEX,ID";
				Object[] value = {departmentid};
				List list=this.getHibernateTemplate().find(sql,value);
				return list.size();
			}
			
			/**
			 * 执行查询操作
			 * @param departmentId
			 * @param searchkey
			 * @return
			 */
			public List<OrgUser> doSearch(Long departmentId,String searchkey){
				List<OrgUser> list = new ArrayList<OrgUser>();
				DBUtilInjection d=new DBUtilInjection();
				
				if(searchkey!=null&&!"".equals(searchkey)){
					if(d.HasInjectionData(searchkey)){
						return list;
					}
				}
				StringBuffer sql= new StringBuffer();
				sql.append(" FROM OrgUser WHERE (userid like ? OR username  like ?)") ;
				if(departmentId!=null){
					sql.append(" and departmentid=?");
				}
				sql.append(" order by userid");
				Object[] value1 = {"%"+searchkey+"%","%"+searchkey+"%"};
				Object[] value2 = {"%"+searchkey+"%","%"+searchkey+"%",departmentId};
				if(departmentId!=null){
					list = this.getHibernateTemplate().find(sql.toString(),value2);
				}else{
					list = this.getHibernateTemplate().find(sql.toString(),value1);
				}
				//List<OrgUser> list = this.getHibernateTemplate().find(sql.toString());
				return list;
				
			}
			
			/**
			 * 执行查询操作
			 * @param departmentId
			 * @param searchkey
			 * @return
			 */
			public List doSearchNew(Long departmentId,String searchkey){
				List<OrgUser> list = new ArrayList<OrgUser>();
				StringBuffer sql= new StringBuffer();
				DBUtilInjection d=new DBUtilInjection();
				
				if(searchkey!=null&&!"".equals(searchkey)){
					if(d.HasInjectionData(searchkey)){
						return list;
					}
				}
				sql.append(" FROM OrgUser as orguser,OrgRole as role WHERE (userid like ? OR username  like ?)") ;
				if(departmentId!=null){
					sql.append(" and departmentid=?");
				}
				sql.append(" and orguser.orgroleid=role.id order by userid");
				Object[] value1 = {"%"+searchkey+"%","%"+searchkey+"%"};
				Object[] value2 = {"%"+searchkey+"%","%"+searchkey+"%",departmentId};
				if(departmentId!=null){
					list = this.getHibernateTemplate().find(sql.toString(),value2);
				}else{
					list = this.getHibernateTemplate().find(sql.toString(),value1);
				}
				//List list = this.getHibernateTemplate().find(sql.toString());
				return list;
			}
			
			/**
			 * 函数说明：获得所有的信息
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List getOrgUserList(int pageSize, int startRow) throws HibernateException {
				final int pageSize1=pageSize;
				final int startRow1=startRow;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public List doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
						Query query=session.createQuery(" FROM OrgUser ORDER BY ORDERINDEX,ID");
			             query.setFirstResult(startRow1);
			             query.setMaxResults(pageSize1);
			             return query.list();
					}
				});
					
			}
			
			/**
			 * 函数说明：获得指定部门ID下得全部用户
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List<OrgUser> getDeptAllUserList(Long departmentid) throws HibernateException {
				String sql=" FROM OrgUser WHERE DEPARTMENTID = ? ORDER BY ORDERINDEX,ID";
				Object[] value = {departmentid};
				return this.getHibernateTemplate().find(sql,value);
					
			}
			
			/**
			 * 函数说明：获得指定部门ID下得全部用户
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List<OrgUser> getDeptAllUserAndRoleList(Long departmentid) throws HibernateException {
				String sql=" FROM OrgUser as ouser,OrgRole as role WHERE ouser.orgroleid=role.id AND ouser.departmentid = ? ORDER BY ouser.orderindex,ouser.id";
				Object[] value = {departmentid};
				return this.getHibernateTemplate().find(sql,value);
				
			}
			/**
			 *获得指定组织json
			 **/
			public List<HashMap> getCompanyActiveUserList(String companyno,String searchKey) {
				StringBuffer sql= new StringBuffer();
				searchKey = searchKey.toUpperCase();
				sql.append("select c.companyname,c.companyno,d.companyid,d.departmentname,d.departmentno,u.userid,u.username,u.userno from orgcompany c,orgdepartment d,orguser  u where c.id = d.companyid and d.id = u.departmentid and u.userstate=0 and u.usertype = 0  and u.enddate>sysdate");
				sql.append(" and c.companyno  like ? ");
				sql.append("  and ( u.userid like ? or u.username like ?)");
				List list  = new ArrayList();
				Connection conn = DBUtil.open();
				PreparedStatement stmt = null;
				ResultSet rset = null;
				try{
					stmt = conn.prepareStatement(sql.toString());
					stmt.setString(1, companyno+"%");
					stmt.setString(2, "%"+searchKey+"%");
					stmt.setString(3, "%"+searchKey+"%");
					rset = stmt.executeQuery();
					while(rset.next()){
						String companyname = rset.getString("companyname");
						if(companyname==null){
							companyname = "";
						} 
						String departmentname = rset.getString("departmentname");
						if(departmentname==null){
							departmentname = "";
						}
						String userid = rset.getString("userid");
						if(userid==null){
							userid = "";
						}
						String username = rset.getString("username");
						if(username==null){
							username = "";
						}
						HashMap hash = new HashMap();
						hash.put("companyname", companyname);
						hash.put("departmentname", departmentname);
						hash.put("userid", userid);
						hash.put("username", username);
						list.add(hash);
					}
				}catch(Exception e){logger.error(e,e);
				}finally{
					DBUtil.close(conn, stmt, rset);
				} 
				return list;
			}
			/**
			 * 获得激活用户列表
			 * @param departmentid
			 * @return
			 */
			public List<OrgUser> getUserListByDeptRole(Long departmentid,List<OrgRole> roleIds) {
				StringBuffer sql= new StringBuffer();
				sql.append("FROM OrgUser where DEPARTMENTID = ? and  USERSTATE = 0 ");
				StringBuffer roleCondition = new StringBuffer();
				boolean isCondition = false;
				roleCondition.append(" and  (1=2 "); 
				for(OrgRole roleModel:roleIds){
						roleCondition.append(" or orgroleid =?");
						isCondition = true;
				}
				roleCondition.append(")");
				if(isCondition){
					sql.append(roleCondition);
				}
				sql.append(" and  STARTDATE<=? and ENDDATE>? ORDER BY priority DESC,ORDERINDEX,ID");
				final String sql1=sql.toString();
				final List<OrgRole> final_roleIds = roleIds;
				final Long final_departmentid = departmentid;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						int i=0;
						query.setLong(i, final_departmentid);i++;
						for (int j = 0; j < final_roleIds.size(); j++) {
							query.setString(i, final_roleIds.get(j).getId());i++;
						}
						query.setDate(i,Calendar.getInstance().getTime());i++;
						query.setDate(i,Calendar.getInstance().getTime());i++;
						return query.list();
					}
				});
			}
			/**
			 * 获得激活用户列表
			 * @param departmentid
			 * @return
			 */
			public List<OrgUser> getActiveUserList(Long companyid,Long departmentid) {
				StringBuffer sql= new StringBuffer();
				sql.append("FROM OrgUser where DEPARTMENTID = ? and  USERSTATE = 0 ");
				if(companyid!=null){
					sql.append("and companyid=?");
				}
				sql.append(" and  STARTDATE<=? and ENDDATE>? ORDER BY ORDERINDEX,ID");
				final String sql1=sql.toString();
				final Long final_departmentid = departmentid;
				final Long final_companyid = companyid;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						int i=0;
						query.setLong(i, final_departmentid);i++;
						if(final_companyid!=null){
							query.setLong(i, final_companyid);i++;
						}
						query.setDate(i,Calendar.getInstance().getTime());i++;
						query.setDate(i,Calendar.getInstance().getTime());i++;
						return query.list();
					}
				});
			}
			
			/**20180104
			 * 获得激活用户列表
			 * @param departmentid
			 * @return
			 */
			public List<OrgUser> getActiveUserLists(Long departmentid,String searchOrg) {
				Long companyid=null; 
				List<OrgUser> list=new ArrayList<OrgUser>();
				StringBuffer sql= new StringBuffer();
				DBUtilInjection d=new DBUtilInjection();
				
				if(searchOrg!=null&&!"".equals(searchOrg)){
					if(d.HasInjectionData(searchOrg)){
						return list;
					}
				}
				sql.append("FROM OrgUser where DEPARTMENTID = ? and  USERSTATE = 0 ");
				if(companyid!=null){
					sql.append("and companyid=?");
				}
				if(searchOrg!=null && !"".equals(searchOrg)){
					sql.append(" and ( username like ? or userid like ? ) ");
				}
				sql.append(" and  STARTDATE<=? and ENDDATE>? ORDER BY ORDERINDEX,ID");
				final String sql1=sql.toString();
				final Long final_departmentid = departmentid;
				final Long final_companyid = companyid;
				final String final_searchOrg = searchOrg;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						int i=0;
						query.setLong(i, final_departmentid);i++;
						if(final_companyid!=null){
							query.setLong(i, final_companyid);i++;
						}
						if(final_searchOrg!=null && !"".equals(final_searchOrg)){
							query.setString(i, "%"+final_searchOrg+"%"); i++;
							query.setString(i, "%"+final_searchOrg+"%"); i++;
						}
						query.setDate(i,Calendar.getInstance().getTime());i++;
						query.setDate(i,Calendar.getInstance().getTime());i++;
						return query.list();
					}
				});
			}
			/**
			 * 获得激活用户列表
			 * @param departmentid
			 * @return
			 */
			public List<OrgUser> getActiveUserList(Long departmentid) {
				return this.getActiveUserList(null, departmentid);
			}
			
			/**
			 * 获得激活用户个数
			 * @param departmentid
			 * @return
			 */
			public int getActiveUserCount() {
				StringBuffer sql= new StringBuffer();
				sql.append("FROM OrgUser where  USERSTATE = 0  and  STARTDATE<=? and ENDDATE>? ORDER BY ORDERINDEX,ID");
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
				}).size();
			}
			
			/**
			 * 获得查询用户列表
			 * @param fieldname
			 * @param value
			 * @param pageSize
			 * @param startRow
			 * @return
			 */
			public List getSearchUserLists(String fieldname,String value,int pageSize, int startRow) {
				final int pageSize1=pageSize;
				final int startRow1=startRow;
				final String queryName=fieldname;
				final String queryValue=value;
				DBUtilInjection d=new DBUtilInjection();
				List l=new ArrayList();
				if(fieldname!=null&&!"".equals(fieldname)){
					if(d.HasInjectionData(fieldname)){
						return l;
					}
				}
				String sql="";
				
				if(queryName==null||queryName.equals("")||queryValue==null||queryValue.equals(""))
					sql=" FROM OrgUser ORDER BY ORDERINDEX,ID";
				else
					sql="FROM OrgUser where ? like ? ORDER BY ORDERINDEX,ID";
				
				final String sql1=sql;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						if(queryName==null||queryName.equals("")||queryValue==null||queryValue.equals("")){}else{
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
			 * 根据部门获得查询用户列表
			 * @param departmentId
			 * @param searchKey
			 * @return
			 */
			public List<OrgUser> getSearchUserLists(Long departmentId,String searchKey) {
				DBUtilInjection d=new DBUtilInjection();
				List<OrgUser> l=new ArrayList<OrgUser>();
				if(searchKey!=null&&!"".equals(searchKey)){
					if(d.HasInjectionData(searchKey)){
						return l;
					}
				}
				String	sql= "";
				if(departmentId!=null&&!departmentId.equals(new Long(0))){
					sql="FROM OrgUser where userstate = 0 and departmentid = ? and ( userid like ? or username like ? or  departmentname like ?) ORDER BY ORDERINDEX,ID";
				}else{
					sql="FROM OrgUser where  userstate = 0 and ( userid like ? or username like ? or  departmentname like ?) ORDER BY ORDERINDEX,ID";
				} 
				final String sql1=sql;
				final Long final_departmentId = departmentId;
				final String final_searchKey = searchKey;
				return this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
						Query query=session.createQuery(sql1);
						if(final_departmentId!=null&&!final_departmentId.equals(new Long(0))){
							query.setLong(0, final_departmentId);
							query.setString(1, "%"+final_searchKey+"%");
							query.setString(2, "%"+final_searchKey+"%");
							query.setString(3, "%"+final_searchKey+"%");
						}else{
							query.setString(0, "%"+final_searchKey+"%");
							query.setString(1, "%"+final_searchKey+"%");
							query.setString(2, "%"+final_searchKey+"%");
						}
						
						return query.list(); 
					} 
				});
			}
			
			private void initOrderIndex(){
				String sql=" FROM OrgUser where orderindex is null";
				List<OrgUser> list =  this.getHibernateTemplate().find(sql);
				for(OrgUser user:list){
					user.setOrderindex(user.getId());
					this.updateBoData(user);
				}
			}
			
			/**
			 * 调整模块排序（置顶）
			 * @param departmentid
			 * @param id
			 * @param type
			 */
			public void updateTop(Long departmentid,int id){
				OrgUser snd1 =null;
				OrgUser snd2 =null;
				Long temp = null; 
				OrgUser ou = this.getModel(id+"");
				if(ou!=null){
					Long orderIndex = ou.getOrderindex();
					StringBuffer sql= new StringBuffer();
					sql.append(" FROM OrgUser where departmentid = ? and orderindex < ? order by orderindex");
					Object[] value = {departmentid,orderIndex};
					List<OrgUser> list = this.getHibernateTemplate().find(sql.toString(),value);
					int num = 0;
					Long tmpNum = null;
					for(OrgUser model:list){
						if(num==0){
							tmpNum = ou.getOrderindex();
							ou.setOrderindex(model.getOrderindex());
							this.updateBoData(ou);
						}
						OrgUser next = null;
						try{
						next = list.get(num+1);
						}catch(Exception e){logger.error(e,e);}
						if(next!=null){
							model.setOrderindex(next.getOrderindex());
						}else{
							model.setOrderindex(tmpNum);	
						}
						this.updateBoData(model);
						num++;
						//this.updateBoData(model);
					}
				}
				
			}
			/**
			 * 调整模块排序（置顶）
			 * @param departmentid
			 * @param id
			 * @param type
			 */
			public void updateBottem(Long departmentid,int id){
				OrgUser snd1 =null;
				OrgUser snd2 =null;
				Long temp = null; 
				OrgUser ou = this.getModel(id+"");
				if(ou!=null){
					Long orderIndex = ou.getOrderindex();
					StringBuffer sql= new StringBuffer();
					sql.append(" FROM OrgUser where departmentid = ? and orderindex > ? order by orderindex desc");
					Object[] value = {departmentid,orderIndex};
					List<OrgUser> list = this.getHibernateTemplate().find(sql.toString(),value);
					int num = 0;
					Long tmpNum = null;
					for(OrgUser model:list){
						if(num==0){
							tmpNum = ou.getOrderindex();
							ou.setOrderindex(model.getOrderindex());
							this.updateBoData(ou);
						}
						OrgUser next = null;
						try{
							next = list.get(num+1);
						}catch(Exception e){logger.error(e,e);}
						if(next!=null){
							model.setOrderindex(next.getOrderindex());
						}else{
							model.setOrderindex(tmpNum);	
						}
						this.updateBoData(model);
						num++;
						//this.updateBoData(model);
					}
				}
				
			}
			
			/**
			 * 调整模块排序
			 * @param departmentid
			 * @param id
			 * @param type
			 */
			public void updateIndex(Long departmentid,int id,String type){
				OrgUser snd1 =null;
				OrgUser snd2 =null;
				Long temp = null; 
				String sql=" FROM OrgUser where id = ?";
				Object[] value = {id};
				List downlist = this.getHibernateTemplate().find(sql,value);
				if(downlist!=null){
					if(downlist.size()>0){
						snd1 = (OrgUser) downlist.get(0);
						if(snd1!=null&&departmentid==null){
							departmentid = snd1.getDepartmentid();
						}
					} 
				}
				
				if(snd1!=null&&snd1.getOrderindex()==null){
					snd1.setOrderindex(snd1.getId());
					initOrderIndex();
				}
				Object[] values;
				DBUtilInjection d=new DBUtilInjection();
				if(type.equals("up")){
					if(departmentid==0){
						sql = " FROM OrgUser WHERE (departmentid IS NULL or DEPARTMENTID = 0) AND ORDERINDEX <? ORDER BY ORDERINDEX desc";
						values = new Object[1];
						values[0] = snd1.getOrderindex();
						
						if(values[0]!=null&&!"".equals(values[0])){
							if(d.HasInjectionData(values[0].toString())){
								return;
							}
						}
					}else{
						sql = " FROM OrgUser WHERE departmentid = ?  and orderindex < ? order by orderindex desc";
						values = new Object[2];
						values[0] = departmentid;
						values[1] = snd1.getOrderindex();
						if(values[0]!=null&&!"".equals(values[0])){
							if(d.HasInjectionData(values[0].toString())){
								return;
							}
						}
						if(values[1]!=null&&!"".equals(values[1])){
							if(d.HasInjectionData(values[1].toString())){
								return;
							}
						}
					}
				}else{
					if(departmentid==0){
						sql = " FROM OrgUser WHERE (departmentid IS NULL or DEPARTMENTID = 0) AND ORDERINDEX >? ORDER BY ORDERINDEX asc";
						values = new Object[1];
						values[0] = snd1.getOrderindex();
						if(values[0]!=null&&!"".equals(values[0])){
							if(d.HasInjectionData(values[0].toString())){
								return;
							}
						}
					
					}else{
						sql = " FROM OrgUser WHERE departmentid = ?  and orderindex > ? order by orderindex asc";
						values = new Object[2];
						values[0] = departmentid;
						values[1] = snd1.getOrderindex();
						if(values[0]!=null&&!"".equals(values[0])){
							if(d.HasInjectionData(values[0].toString())){
								return;
							}
						}
						if(values[1]!=null&&!"".equals(values[1])){
							if(d.HasInjectionData(values[1].toString())){
								return;
							}
						}
					}
				}
				List uplist=this.getHibernateTemplate().find(sql,values);
				if(uplist!=null){
					if(uplist.size()>0){
						snd2 = (OrgUser) uplist.get(0);
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
			/**
			 * 获得部门负责人
			 * @param deptId
			 * @return
			 */
			public List<OrgUser> getDeptManager(Long deptId){
				StringBuffer sql = new StringBuffer();
				sql.append("FROM OrgUser where departmentid =? and ismanager=1 and  userstate = 0 ORDER BY ORDERINDEX,ID");
				Object[] value = {deptId};
				return this.getHibernateTemplate().find(sql.toString(),value);
			}
			
			/**
			 * 查询角色对应用户列表
			 * @param roleId
			 * @return
			 */
			public List<OrgUser> getUserListByRole(String roleId) {
				String sql=" FROM OrgUser  where orgroleid =?  and  userstate = 0  ORDER BY priority DESC,ORDERINDEX,ID";
				DBUtilInjection d=new DBUtilInjection();
				List<OrgUser> l=new ArrayList<OrgUser>();
				if(roleId!=null&&!"".equals(roleId)){
					if(d.HasInjectionData(roleId)){
						return l;
					}
				}
				final String f_roleId = roleId;
				final String sql1 = sql;
				List<OrgUser> list = this.getHibernateTemplate().executeFind(new HibernateCallback(){
					public Object doInHibernate(org.hibernate.Session session)
							throws HibernateException, SQLException {
						// TODO 自动生成方法存根
						Query query=session.createQuery(sql1);
						query.setString(0,f_roleId); 
						return query.list();
					}
				});
//				return this.getHibernateTemplate().find(sql);
				return list;
			}
			
			/**
			 * 查询用户列表：查询条件自定义
			 * @param condition
			 * @return
			 */
			/*public List<OrgUser> getUserListByConditon(String condition){
			
				return this.getHibernateTemplate().find(sql);
			}*/
			
			
			/**
			 * 函数说明：获得指定部门ID下得全部用户(手机号不为空)
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List<OrgUser> getDeptAllUserPhoneList(Long departmentid) throws HibernateException {
				String sql=" FROM OrgUser WHERE MOBILE IS NOT NULL AND DEPARTMENTID = ? AND  USERSTATE=0 AND SYSDATE<ENDDATE ORDER BY ORDERINDEX,ID";
				Object[] value = {departmentid};
				return this.getHibernateTemplate().find(sql,value);
					
			}
			
			/**
			 * 函数说明：获得指定部门ID下得全部用户(邮箱不为空)
			 * 参数说明： 
			 * 返回值：信息的集合
			 */ 
			public List<OrgUser> getDeptAllUserEmailList(Long departmentid) throws HibernateException {
				String sql=" FROM OrgUser WHERE EMAIL IS NOT NULL AND DEPARTMENTID = ? ORDER BY ORDERINDEX,ID";
				Object[] value = {departmentid};
				return this.getHibernateTemplate().find(sql,value);
				
			}
			public WeiXinOrgService getWeiXinOrgService() {
				return weiXinOrgService;
			}
			public void setWeiXinOrgService(WeiXinOrgService weiXinOrgService) {
				this.weiXinOrgService = weiXinOrgService;
			}
			
}
