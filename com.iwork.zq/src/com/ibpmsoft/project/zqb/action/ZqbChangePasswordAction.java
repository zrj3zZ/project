package com.ibpmsoft.project.zqb.action;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.login.constant.LoginConst;
import com.iwork.app.login.control.LoginContext;
import com.iwork.app.login.service.LoginService;
import com.iwork.app.persion.service.SysPersionService;
import com.iwork.commons.util.ShaSaltUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;


public class ZqbChangePasswordAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private SysPersionService sysPersionService ;
	private String DLYH;
	private String CSHMM;
	private String YHMM;
	private String loginInfo;
	
	public String index(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(uc != null){
			Long orgRoleId = uc._userModel.getOrgroleid();
			if(orgRoleId==1L||orgRoleId==5L){
				return SUCCESS;
			}else{
				return LOGIN;
			}
		}else{
			return LOGIN;
		}
	}
	public void indexqx(){
		String str="";
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(uc != null){
			Long orgRoleId = uc._userModel.getOrgroleid();
			if(orgRoleId==1L){
				str="success";
			}else{
				str="login";
			}
		}else{
			str="login";
		}
		 ResponseUtil.write(str);
	}
	/**
	 * 执行密码修改
	 * @return
	 */
	public String excutePwdUpdate() throws Exception{
		if(passwordcheck(YHMM)){
			//清空错误提示
			this.clearErrorsAndMessages();
			UserContext uc = UserContextUtil.getInstance().getUserContext(DLYH);
			OrgUser user = uc.get_userModel();		
			// 根据DLYH查询原有的用户数据
			String salt=ShaSaltUtil.getStringSalt();
			String saltNewPwd = ShaSaltUtil.getEncryptedPwd(CSHMM, salt, true);
			user.setExtend3(salt);
			user.setPassword(saltNewPwd);
			sysPersionService.updateUserInfo(user);
			this.addActionMessage("密码修改成功！");
		
		}else{
			this.addActionMessage("当前登录用户密码输入错误！");
		}
		return SUCCESS;
	}
	

	/**
	 * 执行密码匹配
	 * @return
	 */
	public boolean passwordcheck(String password) throws Exception{
		
		boolean falg = false;
		//获取当前登录人信息
		UserContext uc =  UserContextUtil.getInstance().getCurrentUserContext();
		//登录人账号
		String userid = uc._userModel.getUserid().toUpperCase();
		//获取登录人数据库信息
		UserContext target = UserContextUtil.getInstance().getUserContext(userid);
		
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request=(HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);  
		
		
		if(userid.equals(target.get_userModel().getUserid())){
			int  loginStatus;
		
			LoginContext loginContext = null;
			String SHAPwd=ShaSaltUtil.getEncryptedPwd(password,userid.toUpperCase());
		    loginContext = new LoginContext();
		    loginContext.setUid(userid.toUpperCase()); //系统统一定义大写用户ID
		    loginContext.setPwd(password);
		    loginContext.setMD5Pwd(SHAPwd);
		    LoginService loginService = (LoginService)SpringBeanUtil.getBean("loginService");
		    if(loginService!=null){
		    	loginStatus = loginService.login(loginContext);
		    	if(loginStatus==LoginConst.LOGIN_STATUS_OK){   
		    		falg = true;
		    	}
		    }
		}
		return falg;
	}
	/**
	 * 验证密码是否为默认密码
	 * @return
	 */
	public void Verificationpassword(){
		String sftz="false";
		//获取当前登录人信息
		UserContext uc =  UserContextUtil.getInstance().getCurrentUserContext();		
		//登录人账号
		String userid = uc._userModel.getUserid().toUpperCase();
		String zsPwd = uc._userModel.getPassword();
		
		//获取默认password
		String xmlPWD = SystemConfig._iworkServerConf.getUserDefaultPassword();
		String SHAPwd=ShaSaltUtil.getEncryptedPwd(xmlPWD,userid.toUpperCase());
		com.iwork.core.util.MD5 md5 = new com.iwork.core.util.MD5();
		String md5pwd = md5.getEncryptedPwd(xmlPWD);
		if(zsPwd.equals(SHAPwd) || zsPwd.equals(md5pwd))
		{			
			sftz = "true";
		}
		//使用系统默认密码登录 如果登录成功则需修改密码
		/*LoginContext loginContext = null;
		loginContext = new LoginContext();
		loginContext.setUid(userid.toUpperCase()); //系统统一定义大写用户ID
		loginContext.setPwd(mrpassword);
		loginContext.setMD5Pwd(SHAPwd);
		int  loginStatus;
		LoginService loginService = (LoginService)SpringBeanUtil.getBean("loginService");
		    if(loginService!=null){
		    	loginStatus = loginService.login(loginContext);
		    	if(loginStatus==LoginConst.LOGIN_STATUS_OK){   
		    		sftz="true";
		    	}
		    }*/
		    ResponseUtil.write(sftz);
	}
	public String getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(String loginInfo) {
		this.loginInfo = loginInfo;
	}

	public String getYHMM() {
		return YHMM;
	}

	public void setYHMM(String yHMM) {
		YHMM = yHMM;
	}

	public String getDLYH() {
		return DLYH;
	}

	public void setDLYH(String dLYH) {
		DLYH = dLYH;
	}

	public String getCSHMM() {
		return CSHMM;
	}

	public void setCSHMM(String cSHMM) {
		CSHMM = cSHMM;
	}

	public SysPersionService getSysPersionService() {
		return sysPersionService;
	}

	public void setSysPersionService(SysPersionService sysPersionService) {
		this.sysPersionService = sysPersionService;
	}

}
