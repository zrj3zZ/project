package com.iwork.core.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import net.sf.json.JSONObject;
import com.iwork.app.constant.AppContextConstant;
import com.iwork.app.login.control.LoginContext;
import com.iwork.commons.util.JsonUtil;
import com.iwork.commons.util.PurviewCommonUtil;
import com.iwork.core.organization.cache.DepartmentCache;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.service.OrgGroupService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.purview.dao.SysPurviewOrgDAO;
import com.iwork.core.security.purview.dao.SysRolePurviewDAO;
import com.iwork.core.security.purview.model.SecurityModel;
import com.iwork.core.security.purview.model.SysPurviewOrg;
import com.iwork.core.security.purview.model.SysPurviewRoleModel;
import com.iwork.core.security.purview.model.Syspurviewschema;
import com.iwork.core.util.SpringBeanUtil;
import com.opensymphony.xwork2.ActionContext;

public class SecurityUtil {
	 private static Object lock = new Object(); 
		public static final String COMPANY_PREFIX = "COMPANY";   //组织
		public static final String DEPARTMENT_PREFIX = "DEPT";  //部门
		public static final String ROLE_PREFIX = "ROLE";   //角色
		public static final String GROUP_PREFIX = "GROUP";  //组
		public static final String USER_PREFIX = "USER";   //
		public static final String supermanager = "NEEQMANAGER";
		
	
		Stack<Integer> deptList = new Stack<Integer>();
	    private static SecurityUtil instance;  
	    private OrgGroupService groupService;
	    private OrgUserDAO orgUserDAO;
	    private OrgCompanyDAO orgCompanyDAO;
	    private OrgDepartmentDAO orgDepartmentDAO;
	    private OrgRoleDAO orgRoleDAO;
	    public static SecurityUtil getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new SecurityUtil();  
	                }
	            }  
	        }  
	        return instance;  
	    } 
	public SecurityUtil(){
		if(groupService==null){
			groupService = (OrgGroupService)SpringBeanUtil.getBean("orgGroupService");
		}
		if(orgUserDAO==null){
			orgUserDAO = (OrgUserDAO)SpringBeanUtil.getBean("orgUserDAO");
		}
	}
	/**
	 * 判断当前人是否是超级管理员
	 * @return
	 */
	public static boolean isSuperManager(){
		LoginContext lc = (LoginContext)ActionContext.getContext().getSession().get(AppContextConstant.LOGIN_CONTEXT_INFO);
		boolean flag = false;
		if(lc!=null&&lc.getUid().equals(supermanager)){
			flag = true;
		}
		return flag;
	}
	/**
	 * 判断当前人是否是超级管理员
	 * @return
	 */
	public static boolean isSuperManager(String userId){
		boolean flag = false;
		if(userId.equals(supermanager)){
			flag = true;
		}
		return flag;
	}
	/**
	 * 检查当前权限组里面是否包含指定角色
	 * @return
	 */
	public static boolean isPurviewRoleCheck(List list,int id){
		boolean flag = false;
		for(int i=0;i<list.size();i++){
			SysPurviewRoleModel model =(SysPurviewRoleModel)list.get(i);
			if(id==model.getRoleid()){
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 检查当前权限组里面是否包含指定模块
	 * @return
	 */
	public static boolean isPurviewSchemaCheck(List<Syspurviewschema> list,Long id){
		boolean flag = false;
		if(list!=null&&id!=null){
			for(int i=0;i<list.size();i++){
				Syspurviewschema model =(Syspurviewschema)list.get(i);
				if(id.equals(model.getNavid())){
					flag = true;
				}
			}
		}
		
		return flag;
	}
	/**
	 * 检查当前权限组里面是否包含指定操作模型
	 * @return
	 */
	public static boolean isPurviewOperationCheck(List list,Long id){
		boolean flag = false;
		if(list!=null&&id!=null){
			for(int i=0;i<list.size();i++){
				Syspurviewschema model =(Syspurviewschema)list.get(i);
				if(id.equals(model.getNavid())){
					flag = true;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 获取指定用户的全部权限列表（包含用户、部门、角色对应的权限列表）
	 * @param context
	 * @return
	 */
	public List<Long> getUserPurviewAllList(UserContext context){
		List<Long> list = new ArrayList();
		//根据部门过去当前可用流程列表
		List<SysPurviewOrg> tmplist = this.getDeptPurviewList(String.valueOf(context.get_userModel().getDepartmentid()));
		 if(tmplist!=null){
             Iterator it= tmplist.iterator();
             while(it.hasNext()){
            	 SysPurviewOrg model =  (SysPurviewOrg)it.next();
                 list.add(model.getPurviewid());
             }
         }  
		//根据用户过去当前可用流程列表
		 tmplist = this.getUserPurviewList(context.get_userModel().getUserid());
		 for(SysPurviewOrg model:tmplist){
			  if(!list.contains(model)){
                  list.add(model.getPurviewid());
			  }
		 } 
		//根据角色过去当前可用流程列表
		 List<SysPurviewRoleModel> rolelist = this.getRolePurviewList(String.valueOf(context.get_userModel().getOrgroleid()));
		 for(SysPurviewRoleModel model:rolelist){
			 boolean flag = false;
			 for(Long purviewId:list){
				 if(purviewId.equals(model.getPurviewid())){
					 flag  = true ;
					 break;
				 }
			 }
			  if(!flag){
                 list.add(model.getPurviewid());
			  }
		 }
		 //根据兼任角色过去当前可用流程列表
		 List<OrgUserMap> orgUserMaplist =   context.get_userMapList();
		 if(orgUserMaplist!=null&&orgUserMaplist.size()>0){
			 
			 for(OrgUserMap oup:orgUserMaplist){
				 //兼任部门
				 List<SysPurviewOrg> otherDeptList = this.getDeptPurviewList(String.valueOf(oup.getDepartmentid()));
				 if(otherDeptList!=null&&otherDeptList.size()>0){
					 for(SysPurviewOrg model:otherDeptList){
//		            	 SysPurviewOrg model =  (SysPurviewOrg)it.next();
//		                 list.add(model.getPurviewid());
		            	 boolean flag = false;
						 for(Long purviewId:list){
							 if(purviewId.equals(model.getPurviewid())){
								 flag  = true ;
								 break; 
							 }
						 }
						 if(!flag){
							 list.add(model.getPurviewid());
						 }
		             }
		         }  
				//兼任角色
				 List<SysPurviewRoleModel> otherrolelist = this.getRolePurviewList(String.valueOf(oup.getOrgroleid()));
				 for(SysPurviewRoleModel model:otherrolelist){
					 boolean flag = false;
					 for(Long purviewId:list){
						 if(purviewId.equals(model.getPurviewid())){
							 flag  = true ;
							 break;
						 }
					 }
					 if(!flag){
						 list.add(model.getPurviewid());
					 }
				 }
			 }
		 }
		 
		 return list;
	}
	
	/**
	 * 获得角色对应的权限列表
	 * @param userContext
	 * @return
	 */
	public List<SysPurviewRoleModel>  getRolePurviewList(String roleId){
		//获取角色对应权限列表
    	SysRolePurviewDAO sysRolePurviewDAO = new SysRolePurviewDAO();
    	List<SysPurviewRoleModel> purview_list = sysRolePurviewDAO.getRolePurViewList(roleId);
		return purview_list;
	}
	
	/**
	 * 获得部门对应的权限列表
	 * @param deptid
	 * @return
	 */
	public List<SysPurviewOrg> getDeptPurviewList(String deptid){
		SysPurviewOrgDAO dao = (SysPurviewOrgDAO)SpringBeanUtil.getBean("sysPurviewOrgDAO");
		List<SysPurviewOrg> list = dao.getOrgPurviewList("dept", deptid);
		return list;
	}
	
	/**
	 * 获得用户对应的权限列表
	 * @param userid
	 * @return
	 */
	public List<SysPurviewOrg> getUserPurviewList(String userid){
		SysPurviewOrgDAO dao = (SysPurviewOrgDAO)SpringBeanUtil.getBean("sysPurviewOrgDAO");
		List<SysPurviewOrg> list = dao.getOrgPurviewList("user", userid);
		return list;
	}
	
	/**
	 * 获取当前权限code对应的用户列表
	 * @param securityCode
	 * @return
	 */
	public List<SecurityModel> getUserList(String securityCode){
		List<SecurityModel> rlist = new ArrayList();
		JSONObject json = JsonUtil.strToJson(securityCode); 
		if (securityCode == null || securityCode.trim().equals("")) {
			return null; 
		}else if(json==null){
			String[] userlist = securityCode.split(",");
			if(userlist!=null&&userlist.length>0){
				for(int i=0;i<userlist.length;i++){
					String useritem = userlist[i].trim().toUpperCase();
					if(!"".equals(useritem)){
						OrgUser tmpUser = UserContextUtil.getInstance().getOrgUserInfo(useritem);
						if(tmpUser!=null){
							SecurityModel model = new SecurityModel();
							model.setType(SecurityModel.ORG_TYPE_USER);
							model.setModel(tmpUser);
							rlist.add(model); 
						}
					}
				}
			}
		}else{
			//判断单位
	        if(json.containsKey(PurviewCommonUtil.COMPANY_PREFIX)){
				List list = (List)json.get(PurviewCommonUtil.COMPANY_PREFIX);
				for(int i=0;i<list.size();i++){
					Map map = (Map)list.get(i);
					String id = (String)map.get("id");
					if(id!=null){
						if(orgCompanyDAO==null){
							orgCompanyDAO = (OrgCompanyDAO)SpringBeanUtil.getBean("orgCompanyDAO");
						}
						OrgCompany oc = orgCompanyDAO.getBoData(id);
						if(oc!=null){
							SecurityModel model = new SecurityModel();
							model.setType(SecurityModel.ORG_TYPE_COMPANY);
							model.setModel(oc);
							rlist.add(model);
						}
					}
				}
			}
	        if(json.containsKey(PurviewCommonUtil.DEPARTMENT_PREFIX)){
	        	//获取当前用户部门列表
				List list = (List)json.get(PurviewCommonUtil.DEPARTMENT_PREFIX);
				for(int i=0;i<list.size();i++){
					Map map = (Map)list.get(i);
					String id = (String)map.get("id");
					if(id!=null){
						if(orgDepartmentDAO==null){
							orgDepartmentDAO = (OrgDepartmentDAO)SpringBeanUtil.getBean("orgDepartmentDAO");
						}
						OrgDepartment od = orgDepartmentDAO.getBoData(Long.parseLong(id));
						if(od!=null){
							SecurityModel model = new SecurityModel();
							model.setType(SecurityModel.ORG_TYPE_DEPARTMENT);
							model.setModel(od);
							rlist.add(model);
						}
					}else{
						continue;  
					}
				}
			}
	        //判断角色权限
	        if(json.containsKey(PurviewCommonUtil.ROLE_PREFIX)){
				List list = (List)json.get(PurviewCommonUtil.ROLE_PREFIX);
				for(int i=0;i<list.size();i++){
					Map map = (Map)list.get(i);
					String id = (String)map.get("id");
					if(id!=null){
						if(orgRoleDAO==null){
							orgRoleDAO = (OrgRoleDAO)SpringBeanUtil.getBean("orgRoleDAO");
						}
						OrgRole od = orgRoleDAO.getBoData(id);
						if(od!=null){
							SecurityModel model = new SecurityModel();
							model.setType(SecurityModel.ORG_TYPE_ROLE);
							model.setModel(od);
							rlist.add(model);
						}
					}
				}
			}
	       //根据用户ID判断权限
	        if(json.containsKey(PurviewCommonUtil.USER_PREFIX)){
				List list = (List)json.get(PurviewCommonUtil.USER_PREFIX);
				for(int i=0;i<list.size();i++){
					Map map = (Map)list.get(i);
					String id = (String)map.get("id");
					if(id!=null){
						OrgUser tmpUser = UserContextUtil.getInstance().getOrgUserInfo(id);
						if(tmpUser!=null){
							SecurityModel model = new SecurityModel();
							model.setType(SecurityModel.ORG_TYPE_USER);
							model.setModel(tmpUser);
							rlist.add(model);
						}
					}
				}
				
			}
		}
		return rlist;
	}
	/**
	 * 判断指定用户，是否在指定权限编码中的权限
	 * @param userid   判断用户
	 * @param securityCode  权限编码
	 * @return
	 */
	public boolean checkUserSecurity(String userid,String securityCode){
		boolean flag = false;
		//指定用户如果是超级管理员
		if(isSuperManager(userid)){
			flag = true;
			return flag; 
		}
		if (securityCode == null || securityCode.trim().equals("")) {
			return flag; 
		}
		JSONObject json = JsonUtil.strToJson(securityCode); 
		if (securityCode == null || securityCode.trim().equals("")) {
			return flag; 
		}else if(json==null){
			String[] userlist = securityCode.split(",");
			if(userlist!=null&&userlist.length>0){
				for(int i=0;i<userlist.length;i++){
					String useritem = userlist[i].trim().toUpperCase();
					if(!"".equals(useritem)){
						String tmpUser = UserContextUtil.getInstance().getUserId(useritem);
						if(tmpUser!=null&&tmpUser.equals(userid)){
							flag = true;
							break;
						}
					}
				}
				return flag;  
			}
		}else{
		UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		//判断单位
        if(json.containsKey(PurviewCommonUtil.COMPANY_PREFIX)){
			List list = (List)json.get(PurviewCommonUtil.COMPANY_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String id = (String)map.get("id");
				if(id!=null){
					if(uc.get_companyModel().getId().equals(id.trim())){
						flag = true;
						break;
					}
				}
			}
			if(flag)return flag;  //如果权限已满足，将不再继续判断
		}
        if(json.containsKey(PurviewCommonUtil.DEPARTMENT_PREFIX)){
        	//获取当前用户部门列表
			List list = (List)json.get(PurviewCommonUtil.DEPARTMENT_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String id = (String)map.get("id");
				long l = Long.parseLong(id);
				if(id!=null){
					//判断当前部门
					if(uc.get_deptModel().getId()==l){
						flag = true;
						break;
					} 
					//判断父部门列表
					List<OrgDepartment> deptlist =  uc.get_parentDeptList();
					for(OrgDepartment model:deptlist){ 
						if(model.getId()==l){
							flag = true;
							break;
						}  
					}
					if(flag)break; 
				}else{
					continue;  
				}
			}
			if(flag)return flag;  //如果权限已满足，将不再继续判断
		}
        //判断角色权限
        if(json.containsKey(PurviewCommonUtil.ROLE_PREFIX)){
			List list = (List)json.get(PurviewCommonUtil.ROLE_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String id = (String)map.get("id");
				if(id!=null){
					//判断岗位角色
					if(uc.get_orgRole().getId().equals(id.trim())){
						flag = true;
						break;
					}
					
					//判断兼职角色
					List<OrgUserMap> usermapList= uc.get_userMapList();
					if(usermapList!=null){
						for(OrgUserMap oum:usermapList){
							if(oum.getOrgroleid().equals(id.trim())){
								flag = true; 
								break;
							}
						}
						if(flag) break; 
					}
				}
			}
			if(flag)return flag;  //如果权限已满足，将不再继续判断
		}
        
       //根据用户ID判断权限
        if(json.containsKey(PurviewCommonUtil.USER_PREFIX)){
			List list = (List)json.get(PurviewCommonUtil.USER_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String id = (String)map.get("id");
				if(id!=null){
					if(uc.get_userModel().getUserid().equals(id.trim())){
						flag = true; 
						break; 
					}
				}
			}
			
		}
        
        //**************************暂不支持团队*****************************************************
        /*if(json.containsKey(PurviewCommonUtil.GROUP_PREFIX)){
			List list = (List)json.get(PurviewCommonUtil.GROUP_PREFIX);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String name = (String)map.get("name");
			}
		}**/
		}
		return flag;
	}
	
//	/**
//	 * 获取当前用户中所有的权限组
//	 * @param userContext
//	 * @return
//	 */
//	public List getRoleMapPurviewList(){
//		
//		ArrayList purviewidList = new ArrayList();
//		
//    	
//    	SysPurviewSchemaDAO sysPurviewSchemaDAO = new SysPurviewSchemaDAO();
//    	//获得兼任角色对应的权限列表
//    	List<OrgUserMap> userMapList = userContext._userMapList;
//    	if(userMapList!=null){
//    		for(int i=0;i<userMapList.size();i++){
//    			OrgUserMap orgUserMap = (OrgUserMap)userMapList.get(i);
//    			List tempList = sysRolePurviewDAO.getRolePurViewList(orgUserMap.getOrgroleid());
//    			 for(int j=0;j<tempList.size();j++){
//    				 purview_list.add(tempList.get(j));
//    			 }
//    		}
//    	}
//    	
//    	//获取部门对应的权限列表
//    	Stack deptlist = this.getCurrParentDeptList(userContext._deptModel);
//    	
//    	//获取
//		
//		
//		return null;
//	}
	
	
	/**
	 * 获得指定用户对应的所有父部门节点
	 * @param orgdept
	 * @return
	 */
	public Stack getCurrParentDeptList(OrgDepartment orgdept){
		Stack<Integer> deptList = new Stack<Integer>();
		deptList.push(orgdept.getId().intValue());
		Long parentid = new Long(0);
		while(true){
			orgdept = DepartmentCache.getInstance().getModel(orgdept.getParentdepartmentid()+"");
			if(orgdept==null){
				OrgDepartmentDAO orgDepartmentDAO = (OrgDepartmentDAO)SpringBeanUtil.getBean("orgDepartmentDAO");
				if(!"".equals(parentid))
				orgdept = orgDepartmentDAO.getBoData(parentid);
				if(orgdept!=null){
					parentid = orgdept.getParentdepartmentid();
					deptList.push(orgdept.getId().intValue());
				}else{
					break;
				}
			}else{
				parentid = orgdept.getParentdepartmentid();
				deptList.push(orgdept.getId().intValue());
			}
		}
		
		return deptList;
	}
	
	
	

		
}
