package com.iwork.core.organization.tools;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.constant.AppContextConstant;
import com.iwork.app.login.control.LoginContext;
import com.iwork.app.persion.dao.SysPersonDAO;
import com.iwork.app.persion.model.SysPersonConfig;

import com.iwork.core.organization.constant.UserStateConst;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.dao.OrgUserMapDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.session.MobileSessionContextFactry;
import com.iwork.core.util.SpringBeanUtil;
import com.opensymphony.xwork2.ActionContext;

public class UserContextUtil {
	private static Logger logger = Logger.getLogger(UserContextUtil.class);
	private static UserContextUtil instance;  
	/**
	 * 访客（游离账户）
	 */
	public static final String FREE_ACCOUNT = null;//"GUEST";
    private static Object lock = new Object();  
    private OrgUserDAO orgUserDAO;
    private OrgCompanyDAO orgCompanyDAO;
    private OrgUserMapDAO orgUserMapDAO;
    private SysPersonDAO sysPersonDAO;
    private OrgDepartmentDAO orgDepartmentDAO;
    private OrgRoleDAO orgRoleDAO ; 
	public static UserContextUtil getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new UserContextUtil();  
                }
            }
        }
        return instance;
	}
	
	
	
	/**
	 * 获得当前用户上下文
	 * @return
	 */
	public UserContext getCurrentUserContext(){
		UserContext userContext = this.getCurrentUserContext(null);
		return userContext;
	}
	/**
	 * 获得当前用户上下文
	 * @return
	 */
	public UserContext getCurrentUserContext(String sessionId){
		UserContext userContext = null;
		HttpSession httpSession;
		ActionContext actionContext = ActionContext.getContext();
		if(actionContext!=null){
			if(sessionId==null){
				HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
				httpSession = request.getSession(); 
			}else{
				 httpSession = MobileSessionContextFactry.getInstance().getSession(sessionId);
			}
			try{
				userContext = (UserContext)httpSession.getAttribute(AppContextConstant.USER_CONTEXT);
			}catch(Exception e){
				HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
				httpSession = request.getSession();
				userContext = (UserContext)httpSession.getAttribute(AppContextConstant.USER_CONTEXT);
				logger.error(e,e);
			}
			if(userContext==null){
				LoginContext loginContext = (LoginContext)httpSession.getAttribute(AppContextConstant.LOGIN_CONTEXT_INFO);
				if(loginContext!=null){
					userContext =  this.initContextUserContext(loginContext.getUid());
					
				}//xlj update 2017年4月1日10:14:46 GUEST用户不用
				/*else{
					userContext = new UserContext();
					OrgUser guestUserModel = new OrgUser();
					guestUserModel.setUserid(FREE_ACCOUNT);
					guestUserModel.setUsername("访客");
					userContext.set_userModel(guestUserModel);
				}*/
				if(userContext!=null){
					httpSession.setAttribute(AppContextConstant.USER_CONTEXT, userContext);
				}
			}
		}
		return userContext;
	}
	
	/**
	 * 获得当前用户配置
	 * @param configType
	 * @return
	 */
	public SysPersonConfig getCurrentUserConfig(String configType){
		SysPersonConfig personConf = null;
		//获取当前用户会话中得参数数据
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		HashMap<String,SysPersonConfig> userConfigMap = (HashMap)request.getSession().getAttribute(AppContextConstant.USER_CONFIG);
		if(userConfigMap==null){
			userConfigMap = new HashMap();
		}else{
			personConf = (SysPersonConfig)userConfigMap.get(configType); 
			
		}
		
		//判断外观属性设置
		if(personConf==null||personConf.getUserid()==null||!personConf.getUserid().equals(UserContextUtil.getInstance().getCurrentUserId())){
			SysPersonDAO sysPersonDAO = (SysPersonDAO)SpringBeanUtil.getBean("sysPersonDAO");
			String[] str = {configType};
			List<SysPersonConfig> list = sysPersonDAO.getUserConfigList(this.getCurrentUserId(),str);
			if(list!=null){
				for(SysPersonConfig config:list){
					personConf = config;
					break;
				}
				if(personConf!=null){
					if(personConf.getUserid()!=null&&personConf.getUserid().equals(UserContextUtil.getInstance().getCurrentUserId())){
						userConfigMap.put(configType, personConf);
						//转载session会话参数
						request.getSession().setAttribute(AppContextConstant.USER_CONFIG, userConfigMap);
					}
				}
			}
		}
		return personConf;
	}
	/**
	 * 获得指定用户配置
	 * @param configType
	 * @return
	 */
	public SysPersonConfig getUserConfig(String userid,String configType){
		SysPersonConfig personConf = null;
		  if(sysPersonDAO==null){
				 sysPersonDAO = (SysPersonDAO)SpringBeanUtil.getBean("sysPersonDAO");
		  }
		  personConf = sysPersonDAO.getUserConfig(userid, configType);
		return personConf;
	}
	/**
	 * 获得当前用户配置
	 * @param configType
	 * @return
	 */
	public void setUserConfig(String userid,SysPersonConfig config){
		  if(sysPersonDAO==null){
				 sysPersonDAO = (SysPersonDAO)SpringBeanUtil.getBean("sysPersonDAO");
		  }
		  if(config!=null){
			  config.setUserid(userid);
			  if(config.getId()==null){
				  sysPersonDAO.addBoData(config);
			  }else{
				  sysPersonDAO.updateBoData(config);
			  }
		  }
	}
	/**
	 * 获得当前用户配置
	 * @param configType
	 * @return
	 */
	public void setCurrentUserConfig(String configType,SysPersonConfig config){
		if(sysPersonDAO==null){
			sysPersonDAO = (SysPersonDAO)SpringBeanUtil.getBean("sysPersonDAO");
		}
		if(config!=null){
			SysPersonConfig spc = sysPersonDAO.getUserConfig(config.getUserid() , configType);
			if(spc!=null)config.setId(spc.getId());
			if(config.getId()==null){
				sysPersonDAO.addBoData(config);
			}else{
				sysPersonDAO.updateBoData(config);
			}
			
			SysPersonConfig personConf = null;
			//获取当前用户会话中得参数数据
			ActionContext actionContext = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
			HashMap<String,SysPersonConfig> userConfigMap = (HashMap)request.getSession().getAttribute(AppContextConstant.USER_CONFIG);
			if(userConfigMap==null){
				userConfigMap = new HashMap();
			}
			userConfigMap.put(configType, config);
			request.getSession().setAttribute(AppContextConstant.USER_CONFIG, userConfigMap);
		}
	}
	/**
	 * 重新加载用户配置
	 * @param configType
	 */
	public void reloadCurrUserConfig(String configType){
		reloadCurrUserConfig(configType,null);
	}
	/**
	 * 重新加载用户配置
	 * @param configType
	 * @param sessionId
	 */
	public void reloadCurrUserConfig(String configType,String sessionId){
		SysPersonConfig personConf = null;
		HttpSession httpSession = null;
		if(sessionId==null){
			ActionContext actionContext = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
			httpSession = request.getSession();
		}else{
			httpSession = MobileSessionContextFactry.getInstance().getSession(sessionId);
		}
		//获取当前用户会话中得参数数据
		HashMap<String,SysPersonConfig> userConfigMap = (HashMap)httpSession.getAttribute(AppContextConstant.USER_CONFIG);
		if(userConfigMap==null){
			userConfigMap = new HashMap();
		}
			personConf = userConfigMap.get(configType);
			if(personConf!=null){
				//判断配置信息是否存在，如果存在，则重新装载
				SysPersonDAO sysPersonDAO = (SysPersonDAO)SpringBeanUtil.getBean("sysPersonDAO");
				String[] str = {configType};
				List<SysPersonConfig> list = sysPersonDAO.getUserConfigList(this.getCurrentUserId(),str);
				if(list!=null){
					personConf = list.get(0);
					if(personConf!=null){
						userConfigMap.put(configType, personConf);
						//转载session会话参数
						httpSession.setAttribute(AppContextConstant.USER_CONFIG, userConfigMap);
					}
				}
			}
	}
	
	/**
	 * 获得当前用户ID
	 * USERID
	 * @return
	 */
	public String getCurrentUserFullName(){
		String userid =  getCurrentUserId();
		return this.getFullUserAddress(userid);
	}
	
	/**
	 * 获得当前用户ID
	 * USERID
	 * @return
	 */
	public String getCurrentUserId(){
		HttpSession httpSession = null;
		LoginContext loginContext = null;
		ActionContext actionContext = ActionContext.getContext();
		if(actionContext!=null){
			HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
			httpSession = request.getSession();
			if(httpSession!=null){
				loginContext = (LoginContext)httpSession.getAttribute(AppContextConstant.LOGIN_CONTEXT_INFO);
			}
			if(loginContext!=null){
				return loginContext.getUid();
			}
			else{
				return FREE_ACCOUNT;
			}
		}
		else{
			return FREE_ACCOUNT;
		}		
	}
	
	/**
	 * 获取当前用户登陆系统的设备类型
	 * @return
	 */
	public String getCurrentUserLoginDeviceType(){
		HttpSession httpSession = null;
		LoginContext loginContext = null;
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		httpSession = request.getSession();
		if(httpSession!=null){
			loginContext = (LoginContext)httpSession.getAttribute(AppContextConstant.LOGIN_CONTEXT_INFO);
		}
		if(loginContext!=null){ 
			return loginContext.getDeviceType();
		}else{
			return LoginContext.LOGIN_DEVICE_TYPE_WEB;
		}
	}
	
	/**
	 * 获得指定用户上下文信息
	 * @param userId
	 * @return
	 */
	public UserContext getUserContext(String userId){
		if(userId!=null){
			userId = userId.toUpperCase();
			return this.initContextUserContext(userId);
		}else{
			return null;
		}
		
	}
	/**
	 * 获得指定用户信息
	 * @param userId
	 * @return
	 */
	public OrgUser getOrgUserInfo(String address){
		OrgUser model = null;
		address = address.toUpperCase();
		if(address.indexOf("[")>0&&address.indexOf("]")>0){
			String userId = address.substring(0,address.indexOf("[")); 
			UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
			if(uc!=null){
				model = uc._userModel;
			}
			
		}else{
			UserContext uc = UserContextUtil.getInstance().getUserContext(address);
			if(uc!=null){
				model = uc._userModel;
			}
		}
		return model;
	}
	/**
	 * 获得完整用户账号地址
	 * @param  address 可传入格式为:userId[姓名] 或userId
	 * @return
	 */
	public String getFullUserAddress(String address){
		String returnAddress = null;
		if(address!=null&&!address.equals("")){
			address = address.toUpperCase();
			if(address.indexOf("[")>0&&address.indexOf("]")>0){
				String userId = address.substring(0,address.indexOf("[")); 
				UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
				if(uc!=null){
					returnAddress = uc._userModel.getUserid()+"["+uc._userModel.getUsername()+"]";
				}
				
			}else{
				UserContext uc = UserContextUtil.getInstance().getUserContext(address);
				if(uc!=null){
					returnAddress = uc._userModel.getUserid()+"["+uc._userModel.getUsername()+"]";
				}
			}
		}
		return returnAddress;
	}
	/**
	 * 获得用户账号地址
	 * @param address 可传入格式为:userId[姓名] 或userId
	 * @return
	 */
	public String getUserId(String address){
		String returnAddress = null;
		address = address.toUpperCase();
		if(address.indexOf("[")>0&&address.indexOf("]")>0){
			String userId = address.substring(0,address.indexOf("["));
			UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
			if(uc!=null){
				returnAddress = uc._userModel.getUserid();
			}
			
		}else{
			UserContext uc = this.getUserContext(address);
			if(uc!=null){
				returnAddress = uc._userModel.getUserid();
			}
		}
		return returnAddress;
	}
	/**
	 * 获得用户账号地址
	 * @param address address 可传入格式为:姓名<userId> 或userId
	 * @return
	 */
	public String getUserName(String address){
		String returnAddress = null;
		address = address.toUpperCase();
		if(address.indexOf("[")>0&&address.indexOf("]")>0){
			String userId = address.substring(0,address.indexOf("["));
			UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
			if(uc!=null){
				returnAddress = uc._userModel.getUsername();
			}
			
		}else{
			UserContext uc = UserContextUtil.getInstance().getUserContext(address);
			if(uc!=null){
				returnAddress = uc._userModel.getUsername();
			}
		}
		return returnAddress;
	}

	
	/**
	 * 获得用户姓名信息
	 * @param address address 可传入格式为:姓名<userId> 或userId
	 * @return
	 */
	public String getUserName(String[] address){
		StringBuffer returnAddress =null;
		if(address!=null){
			 returnAddress = new StringBuffer();;
			for(int i = 0;i<address.length;i++){
				String item = address[0];
				if(item!=null){
					 String temp = this.getUserName(item);
						if(temp!=null){
							returnAddress.append(temp);
						}
				}
			}
		}
		return returnAddress.toString();
	}
	
	/**
	 * 获得用户姓名信息
	 * @param address address 可传入格式为:userId[姓名] 或userId
	 * @return
	 */
	public String[] getUserId(String[] address){
		String[] userIds = null;
		if(address!=null){
			userIds = new String[address.length];
			for(int i = 0;i<address.length;i++){
				String item = address[i].toUpperCase();
				if(item!=null){
					String temp = this.getUserId(item);
					if(temp!=null){
						userIds[i]=temp;
					}else{
						userIds[i] = null;
					}
				}
			}
		}
		return userIds;
	}
	
	 /**
     * 初始化游离账户上下文
     * @param loginContext
     */
    /*private UserContext initGuestUserContext(){
    	String userid = "GUEST"; //游离账户
    	UserContext uc = null;
    	//初始化用户模型
    	OrgUser model = UserCache.getInstance().getModel(userid);
    	if(model==null){
//    		OrgUserDAO  userDao = new OrgUserDAO();
//    		model = orgUserDAO.getUserModel(userid);
    		if(orgUserDAO==null){
    			orgUserDAO = (OrgUserDAO)SpringBeanUtil.getBean("orgUserDAO");
    		}
    		if(orgUserDAO!=null){
    			model = orgUserDAO.getUserModel(userid);	
    		}
    		if(model!=null){
    			if(model.getUserstate().equals(UserStateConst.ORGUSER_STATE_DISABLED)){
        			String username = model.getUsername()+"[已注销]";
        			model.setUsername(username);
        			UserCache.getInstance().putModel(model);
        		}
    		}
    	}
    	if(model!=null){
    		uc = new UserContext();
        	uc.set_userModel(model);
    		if(model!=null){
    			//初始化兼职模型
        		List<OrgUserMap> mapList = UserMapCache.getInstance().getList(userid);
        		if(mapList==null){
        			orgUserMapDAO = (OrgUserMapDAO)SpringBeanUtil.getBean("orgUserMapDAO");
        			mapList = orgUserMapDAO.getOrgUserMapList(userid);
        			if(mapList!=null)UserMapCache.getInstance().putList(userid, mapList);
        		}
        		uc.set_userMapList(mapList);
        		
        		//初始化角色模型
        		OrgRole orgRole = RoleUserCache.getInstance().getModel(userid);
        		if(orgRole==null){
        			if(orgRoleDAO==null)orgRoleDAO = (OrgRoleDAO)SpringBeanUtil.getBean("orgRoleDAO");
        			
        			orgRole = orgRoleDAO.getBoData(model.getOrgroleid()+"");
        			if(orgRole!=null)RoleUserCache.getInstance().putModel(userid, orgRole);
        		}
        		uc.set_orgRole(orgRole);
        		//部门模型
        		OrgDepartment orgDepartment = DepartmentCache.getInstance().getModel(model.getDepartmentid()+"");
        		if(orgDepartment==null){
        			if(orgDepartmentDAO==null)orgDepartmentDAO = (OrgDepartmentDAO)SpringBeanUtil.getBean("orgDepartmentDAO");
        			orgDepartment = orgDepartmentDAO.getBoData(model.getDepartmentid());
        			if(orgDepartment!=null)DepartmentCache.getInstance().putModel(orgDepartment);
        		}
        		uc.set_deptModel(orgDepartment);
        		
        		//获取用户部门全路径列表
    			List<OrgDepartment> parentDeptList = new ArrayList();
    			Long parentid = orgDepartment.getParentdepartmentid();
    			while(true){
    				if(parentid!=null){
    					//判断缓存中的对象
    					OrgDepartment dept = DepartmentCache.getInstance().getModel(parentid+"");
    					if(dept==null){
    						if(orgDepartmentDAO==null)orgDepartmentDAO = (OrgDepartmentDAO)SpringBeanUtil.getBean("orgDepartmentDAO");
    						dept = orgDepartmentDAO.getBoData(parentid);
    						if(dept==null){
    							break;
    						}else{
    							parentDeptList.add(dept); 
    							//重新给父id赋值后，进入下一次循环
    							parentid = dept.getParentdepartmentid();
    							continue;  
    						}
    					}else{
    						parentDeptList.add(dept); 
							//重新给父id赋值后，进入下一次循环
							parentid = dept.getParentdepartmentid();
							continue;  
    					}
    				}else{
    					break;
    				}
    			}
    			uc.set_parentDeptList(parentDeptList);
        	
        		//组织单元模型
        		OrgCompany orgCompany = CompanyCache.getInstance().getModel(orgDepartment.getCompanyid());
        		if(orgCompany==null){
        			if(orgCompanyDAO==null)orgCompanyDAO = (OrgCompanyDAO)SpringBeanUtil.getBean("orgCompanyDAO");
        			orgCompany = orgCompanyDAO.getBoData(orgDepartment.getCompanyid());
        			if(orgCompany!=null)CompanyCache.getInstance().putModel(orgCompany);
        		}
        		uc.set_companyModel(orgCompany);
    		}
    	}
		return uc;
    }*/
    /**
     * 初始化用户上下文
     * @param loginContext
     */
    private UserContext initContextUserContext(String userid){
    	UserContext uc = null;
    	
    	//格式转换
    	//初始化用户模型
		if(orgUserDAO==null) orgUserDAO = (OrgUserDAO)SpringBeanUtil.getBean("orgUserDAO");
		OrgUser model = orgUserDAO.getUserModel(userid);
		if(model!=null){
			if(model.getUserstate().equals(UserStateConst.ORGUSER_STATE_DISABLED)){
				String username = model.getUsername()+"[已注销]";
			}
		}
    	if(model!=null){
    		uc = new UserContext();
    		uc.set_userModel(model);
    		if(model!=null){
    			//初始化兼职模型
    				if(orgUserMapDAO==null)orgUserMapDAO = (OrgUserMapDAO)SpringBeanUtil.getBean("orgUserMapDAO");
    				List<OrgUserMap> mapList = orgUserMapDAO.getOrgUserMapList(userid);
    				uc.set_userMapList(mapList);
    			//初始化角色模型
    			
				if(orgRoleDAO==null) orgRoleDAO = (OrgRoleDAO)SpringBeanUtil.getBean("orgRoleDAO");
				OrgRole orgRole  = orgRoleDAO.getBoData(model.getOrgroleid()+"");
    			uc.set_orgRole(orgRole);
    			//部门模型
    			if(orgDepartmentDAO==null)orgDepartmentDAO = (OrgDepartmentDAO)SpringBeanUtil.getBean("orgDepartmentDAO");
    				OrgDepartment orgDepartment = orgDepartmentDAO.getBoData(model.getDepartmentid());
    			uc.set_deptModel(orgDepartment);
    			
    			//初始化用户部门全路径列表
    			List<OrgDepartment> parentDeptList = new ArrayList();
    			Long parentid = orgDepartment.getParentdepartmentid();
    			while(true){
    				if(parentid!=null){
    					//判断缓存中的对象
    					if(orgDepartmentDAO==null)orgDepartmentDAO = (OrgDepartmentDAO)SpringBeanUtil.getBean("orgDepartmentDAO");
    						OrgDepartment dept = orgDepartmentDAO.getBoData(parentid);
    						if(dept==null){
    							break;
    						}else{
    							parentDeptList.add(dept); 
    							//重新给父id赋值后，进入下一次循环
    							parentid = dept.getParentdepartmentid();
    							continue;  
    						}
    				}else{
    					break;
    				}
    			}
    			uc.set_parentDeptList(parentDeptList);
    			
    			//组织单元模型
				if(orgCompanyDAO==null) orgCompanyDAO = (OrgCompanyDAO)SpringBeanUtil.getBean("orgCompanyDAO");
				OrgCompany orgCompany = orgCompanyDAO.getBoData(orgDepartment.getCompanyid());
    			uc.set_companyModel(orgCompany);
    		}
    	}
    	return uc;
    }
    
    /**
	 * 过滤注销用户
	 * @param orgUser
	 * @return
	 */
	public String filterDisabledUser(String userId){
		UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
        if(uc!=null){
       	 if(uc._userModel!=null){
       		 if(UserStateConst.ORGUSER_STATE_ACTIVE.equals(uc._userModel.getUserstate())){
       			 return userId;
       		 }
       	 }
        }
		return null;
	}
	
	/**
	 * 过滤注销用户列表
	 * @param userList
	 * @return
	 */
	public List<OrgUser> filterDisabledUsers(List<OrgUser> userList){
		List<OrgUser> list = new ArrayList<OrgUser>();
		if(userList!=null){
	        for(OrgUser model:userList){
	             if(model!=null){
	            		 if(UserStateConst.ORGUSER_STATE_ACTIVE.equals(model.getUserstate())){
	            			 list.add(model);
	            		 }
	             }
	             
	        }
		}
		
		return list;
	}
	
	/**
	 * 检查指定用户发送地址是否合法
	 * @param address
	 * @return
	 */
	public boolean checkAddress(String[] addressList){
		boolean flag = true;
		if(addressList!=null){
			for(String address:addressList){
				if("".equals(address)){
					flag = false;
					break;
				}
				String userId = this.getUserId(address.toUpperCase());
				if(userId!=null){
					UserContext uc = this.getUserContext(userId.toUpperCase());
					if(uc==null){
						flag = false;
						break;
					}
					if(UserStateConst.ORGUSER_STATE_DISABLED.equals(uc._userModel.getUserstate())){
						flag = false;
						break;
					}
				}else{
					flag = false;
					break;
				}
			}
		}else{
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 检查指定用户发送地址是否合法
	 * @param address
	 * @return
	 */
	public boolean checkAddress(String address){
		boolean flag = true;
		if(address!=null){
				if("".equals(address)){
					flag = false;
				}
				String userId = this.getUserId(address.toUpperCase());
				if(userId!=null){
					UserContext uc = this.getUserContext(userId);
					if(uc==null){
						flag = false;
					}
					if(UserStateConst.ORGUSER_STATE_DISABLED.equals(uc._userModel.getUserstate())){
						flag = false;
					}
				}else{ 
					flag = false;
				}
				
		}else{
			flag = false;
		}
		return flag;
	}
}
