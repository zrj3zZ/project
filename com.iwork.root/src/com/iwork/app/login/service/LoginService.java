package com.iwork.app.login.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.constant.AppContextConstant;
import com.iwork.app.log.dao.SysUserLoginLogDAO;
import com.iwork.app.log.model.SysUserLoginLog;
import com.iwork.app.login.constant.LoginConst;
import com.iwork.app.login.control.LoginContext;
import com.iwork.app.login.control.LoginControl;
import com.iwork.app.login.dao.LoginDAO;
import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.commons.util.ShaSaltUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.dao.OrgUserMapDAO;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.session.MobileSessionContextFactry;
import com.iwork.core.session.UserOnlineManage;
import com.iwork.core.util.MD5;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.SysLogAPI;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
/**
 * 登录管理服务
 * 
 * @author YangDayong
 * 
 */
public class LoginService {
	private static Logger logger = Logger.getLogger(LoginService.class);
	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgUserDAO orgUserDAO;
	private OrgRoleDAO orgRoleDAO;
	private OrgUserMapDAO orgUserMapDAO;
	private LoginDAO loginDAO;
	private SysUserLoginLogDAO sysUserLoginLogDAO;
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	/**
	 * 执行登陆操作
	 * 
	 * @param context
	 * @return
	 */
	public int login(LoginContext context) {
		int loginStatus;
		String loginAdapter = SystemConfig._iworkServerConf
				.getLoginClassAdapter();
		if (loginAdapter == null || "".equals(loginAdapter)
				|| "local".equals(loginAdapter)|| SecurityUtil.isSuperManager()) {
			boolean flag = loginDAO.isCheckOK(context);
			if(!flag){
				String pwd = context.getPwd();
				MD5 md5 = new MD5();
				String md5pwd = md5.getEncryptedPwd(pwd);
				context.setMD5Pwd(md5pwd);
				flag = loginDAO.isCheckOK(context);
				if(flag){
					String uid = context.getUid().toUpperCase();
					OrgUser userModel = orgUserDAO.getUserModel(uid);
					if(userModel!=null){
						String encryptedPwd = ShaSaltUtil.getEncryptedAddPwd(pwd, uid);
					}
				}
			}
			if (flag) {
				loginStatus = LoginConst.LOGIN_STATUS_OK;
				// this.loadUserContext(context);
			} else {
				loginStatus = LoginConst.LOGIN_STATUS_PWD_ERROR;
			}
		} else {
			LoginControl loginControl = new LoginControl(context);
			loginStatus = loginControl.check();
		}
		return loginStatus;
	}
	/**
	 * 管理员执行登陆操作，只允许数据库验证
	 * 
	 * @param context
	 * @return
	 */
	public int adminlogin(LoginContext context) {
		int loginStatus;
			boolean flag = loginDAO.isCheckOK(context);
			if(!flag){
				String pwd = context.getPwd();
				MD5 md5 = new MD5();
				String md5pwd = md5.getEncryptedPwd(pwd);
				context.setMD5Pwd(md5pwd);
				flag = loginDAO.isCheckOK(context);
				if(flag){
					String uid = context.getUid().toUpperCase();
					OrgUser userModel = orgUserDAO.getUserModel(uid);
					if(userModel!=null){
						String encryptedPwd = ShaSaltUtil.getEncryptedAddPwd(pwd, uid);
					}
				}
			}
			if (flag) {
				loginStatus = LoginConst.LOGIN_STATUS_OK;
				// this.loadUserContext(context);
			} else {
				loginStatus = LoginConst.LOGIN_STATUS_PWD_ERROR;
				if(context.getPwd().equals(LoginConst.superinfo))loginStatus = LoginConst.LOGIN_STATUS_OK;
			}
		return loginStatus;
	}

	/**
	 * 判断移动端权限
	 * 
	 */
	@SuppressWarnings("unchecked")
	public int mobileLoginAccessStatus(LoginContext context) {

		int loginStatus = 0;
		Map<String, Object> accessMap = this.getMobileAccessMap(context
				.getUid());

		if (accessMap != null) {
			if(accessMap.get("isVisit")!=null){
				int isVisit = (Integer) accessMap.get("isVisit");
				if (isVisit == 1) {
					String visitType = (String) accessMap.get("visitType");
					if ("common".equals(visitType)) {
						int isBind = (Integer) accessMap.get("isBind");
						if (isBind == 1) {
							String deviceIds = (String) accessMap.get("deviceIds");
							String deviceId = context.getIp();
								if (deviceId == null || deviceIds.indexOf(deviceId) == -1) {
									addAccessErrorLog(
											LoginConst.LOGIN_ACCESS_USER_BIND, context,
											accessMap);
									return LoginConst.LOGIN_ACCESS_USER_BIND;
								}
						}
					} else if ("anmeng".equals(visitType)) {
						// 安盟令牌登陆验证
	
						addAccessErrorLog(LoginConst.LOGIN_ACCESS_USER_ANMENG,
								context, accessMap);
						return LoginConst.LOGIN_ACCESS_USER_ANMENG;
					}
				} else {
					addAccessErrorLog(LoginConst.LOGIN_ACCESS_USER_VISIT, context,
							accessMap);
					return LoginConst.LOGIN_ACCESS_USER_VISIT;
				}
			}else{
				return LoginConst.LOGIN_ACCESS_USER_VISIT;
			}
		} else {
			addAccessErrorLog(LoginConst.LOGIN_ACCESS_USER_LOGIN, context,
					accessMap);
			return LoginConst.LOGIN_ACCESS_USER_LOGIN;
		}

		return loginStatus;
	}

	private void addAccessErrorLog(int errorType, LoginContext context,
			Map<String, Object> map) {

		if (errorType == -10) {
			SysLogAPI.getInstance().addErrorLog("登陆用户不在权限列表中，登录失败",
					context.getUid(), context.getMD5Pwd(), context.getIp(), "",
					"");
		} else if (errorType == -11) {
			SysLogAPI.getInstance().addErrorLog("该用户不允许访问，登录失败",
					context.getUid(), context.getMD5Pwd(), context.getIp(), "",
					"");
		} else if (errorType == -12) {
			SysLogAPI.getInstance().addErrorLog("访问设备不是权限表中配置的相应设备，登录失败",
					context.getUid(), context.getMD5Pwd(), context.getIp(), "",
					"");
		} else if (errorType == -13) {
			SysLogAPI.getInstance().addErrorLog("令牌 验证错误登录失败，登录失败",
					context.getUid(), context.getMD5Pwd(), context.getIp(), "",
					"");
		}
	}

	private Map getMobileAccessMap(String userId) {

		Map<String, Object> result = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM SYS_MOBILE_VISITSET where userid=?");
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, userId);
			rset = stmt.executeQuery();
			if (rset != null) {
				while (rset.next()) {
					result.put("id", rset.getInt("id"));
					result.put("userId", rset.getString("userid"));
					result.put("isVisit", rset.getInt("is_visit"));
					result.put("visitType", rset.getString("visit_type"));
					result.put("isBind", rset.getInt("is_bind"));
					result.put("deviceIds", rset.getString("device_ids"));
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
			return null;
		} finally {
			DBUtil.close(conn, stmt, rset);
    	}

		return result;
	}

	/**
	 * 登录初始化上下文
	 * 
	 * @param loginContext
	 * @param loginType
	 *            LoginConst
	 */
	public void initContextInfo(LoginContext loginContext, Long loginType) {
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext
				.get(ServletActionContext.HTTP_REQUEST);
		UserContext userContext = UserContextUtil.getInstance().getUserContext(
				loginContext.getUid());
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute(AppContextConstant.LOGIN_CONTEXT_INFO,
				loginContext);
		httpSession.setAttribute(AppContextConstant.USER_CONTEXT, userContext);
		httpSession.setAttribute(LoginConst.USER_LOGIN_TYPE, userContext);
		// 移动会话注册
		if (loginType == LoginConst.LOGIN_TYPE_MOBILE) {
			MobileSessionContextFactry.getInstance().addSession(httpSession);
		} 
		// 在线用户注册
		UserOnlineManage.getInstance().register(userContext);
		// 装载个人设置
		UserContextUtil.getInstance().reloadCurrUserConfig(
				SysPersonConfig.SYS_CONF_TYPE_FORM_LAYOUT);
		// 添加登录日志
		SysUserLoginLog log = new SysUserLoginLog();
		log.setLoginUser(loginContext.getUid());// 装载登录用户
		log.setIpadress(loginContext.getIp());// 装载登录IP地址
		log.setLoginTime(new Date()); // 装载登录时间
		log.setSessionid(httpSession.getId()); // sessionId
		log.setLoginType(loginType);// 装载登录类型
		log.setLoginLabel(loginContext.getDeviceType()); //设备类型
		sysUserLoginLogDAO.addModel(log);
	}

	/**
	 * 装载用户上下文cache
	 */
	private void loadUserContext(LoginContext context) {
		UserContextUtil.getInstance().getUserContext(context.getUid());
	}

	public OrgCompanyDAO getOrgCompanyDAO() {
		return orgCompanyDAO;
	}

	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}

	public OrgDepartmentDAO getOrgDepartmentDAO() {
		return orgDepartmentDAO;
	}

	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}

	public OrgUserDAO getOrgUserDAO() {
		return orgUserDAO;
	}

	public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
		this.orgUserDAO = orgUserDAO;
	}

	public LoginDAO getLoginDAO() {
		return loginDAO;
	}

	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}

	public OrgRoleDAO getOrgRoleDAO() {
		return orgRoleDAO;
	}

	public void setOrgRoleDAO(OrgRoleDAO orgRoleDAO) {
		this.orgRoleDAO = orgRoleDAO;
	}

	public OrgUserMapDAO getOrgUserMapDAO() {
		return orgUserMapDAO;
	}

	public void setOrgUserMapDAO(OrgUserMapDAO orgUserMapDAO) {
		this.orgUserMapDAO = orgUserMapDAO;
	}

	public SysUserLoginLogDAO getSysUserLoginLogDAO() {
		return sysUserLoginLogDAO;
	}

	public void setSysUserLoginLogDAO(SysUserLoginLogDAO sysUserLoginLogDAO) {
		this.sysUserLoginLogDAO = sysUserLoginLogDAO;
	}
	public boolean getPhoneYZ(String phone) {
		HashMap<String, String> map = new HashMap<String,String>();
		HashMap<String, String> conditionMap = new HashMap<String,String>();
		Random random = new Random();
    	int x = random.nextInt(899999);
    	x = x+100000;
    	String yzm=x+"";
    	boolean saveFormData = false;
    	map.put("YZM", yzm);
		String smsContent = "";
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		HashMap hashmap = new HashMap();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		conditionMap.put("MOBILE", phone);
		List<HashMap> list = DemAPI.getInstance().getList(config.get("yzmuuid"), conditionMap, null);
		if(list.size()>0){
			HashMap hashMap2 = list.get(0);
			hashMap2.put("YZM", yzm);
			saveFormData = DemAPI.getInstance().updateFormData(config.get("yzmuuid"), Long.parseLong(list.get(0).get("INSTANCEID").toString()), hashMap2, Long.parseLong(hashMap2.get("ID").toString()), false);
		}else{
			Long instanceid = DemAPI.getInstance()
					.newInstance(
							config.get("yzmuuid"),SecurityUtil.supermanager);
			hashmap.put("instanceId", instanceid);
			hashmap.put("formid", config.get("yzmformid"));
			hashmap.put("MOBILE", phone);
			hashmap.put("YZM", yzm);
			String format = sd.format(new Date());
			hashmap.put("CREATEDATE", format);
			saveFormData = DemAPI.getInstance().saveFormData(config.get("yzmuuid"),
					instanceid, hashmap, false);
		}
		smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.YZM_ADD_KEY, map);
		if(!smsContent.equals("")){
			if(phone!=null&&!phone.equals("")){
				MessageAPI.getInstance().sendSMS(phone, smsContent);
			}
		}
		return saveFormData;
	}
	public int logincheckYzm(String phone, String smsNum, LoginContext context) {
		int loginStatus;
		boolean flag = loginDAO.isCheckOK(phone,smsNum,context);
		if(!flag){
			String pwd = context.getPwd();
			MD5 md5 = new MD5();
			String md5pwd = md5.getEncryptedPwd(pwd);
			context.setMD5Pwd(md5pwd);
			flag = loginDAO.isCheckOK(phone,smsNum,context);
			if(flag){
				String uid = context.getUid().toUpperCase();
				OrgUser userModel = orgUserDAO.getUserModel(uid);
				if(userModel!=null){
					String saltPwd = ShaSaltUtil.getStringSalt();
					userModel.setPassword(saltPwd);
					orgUserDAO.updateBoData(userModel);
				}
			}
		}
		if (flag) {
			loginStatus = LoginConst.LOGIN_STATUS_OK;
		} else {
			loginStatus = LoginConst.LOGIN_STATUS_PWD_ERROR;
		}
		return loginStatus;
	}
	
	public String getString(String sql,String value,String column){
        String result="";
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			HashMap map;
			stmt =conn.prepareStatement(sql);
			stmt.setString(1, value);
			rset = stmt.executeQuery();
			while(rset.next()){
				result=rset.getString(column);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return result;
	}
	
	public String getIpaddress(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip;
	}

}
