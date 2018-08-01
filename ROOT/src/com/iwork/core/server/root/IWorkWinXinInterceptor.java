package com.iwork.core.server.root;

import java.util.Map;

import org.apache.log4j.Logger;
import com.iwork.app.constant.AppContextConstant;
import com.iwork.app.login.constant.LoginConst;
import com.iwork.app.login.control.LoginContext;
import com.iwork.app.login.service.LoginService;
import com.iwork.app.weixin.core.pojo.UserCodeModel;
import com.iwork.app.weixin.org.action.WeiXinOrgAction;
import com.iwork.app.weixin.org.dao.WeiXinOrgDAO;
import com.iwork.commons.util.ObjectUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.wechat.util.WeixinUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class IWorkWinXinInterceptor implements Interceptor {
	private Logger logger=Logger.getLogger(IWorkUnLoginInterceptor.class);
	public static LoginService loginService;
	public void destroy() {
		// TODO Auto-generated method stub
	} 
	public void init() {
		// TODO Auto-generated method stub

	} 
	public String intercept(ActionInvocation actionInvocation) throws Exception {
//		 return actionInvocation.invoke(); 
		   Map map = actionInvocation.getInvocationContext().getParameters();
		 Map session = actionInvocation.getInvocationContext().getSession();
		 Object action = actionInvocation.getAction();
		 if (action instanceof WeiXinOrgAction) {   // 对LoginAction不做该项拦截
	            return actionInvocation.invoke(); 
	     }
		
		 LoginContext loginContext = null;
		 try{
			 loginContext = (LoginContext) session.get(AppContextConstant.LOGIN_CONTEXT_INFO);  
		 }catch(Exception e){}
         if (loginContext!= null) {  
            // 存在的情况下进行后续操作。  
            return actionInvocation.invoke();  
         } else {  
        	 if(map.get("code")!=null){
    			 Object code = map.get("code");
    			 String str = ObjectUtil.getString(code);
    			 UserCodeModel model = WeixinUtil.getInstance().getUserIdForCode(str);
    			 if(model!=null){
    				 if(loginService==null){
    					 loginService = (LoginService)SpringBeanUtil.getBean("loginService");
    				 }
    				 WeiXinOrgDAO orgWinXinDAO = new WeiXinOrgDAO();
    				 String userid = orgWinXinDAO.getOrgUserId(model.getUserid());
    				 if(userid==null||userid.equals("")){
    					 userid = model.getUserid().toUpperCase();
    				 }
    				 loginContext = new LoginContext();
    			        loginContext.setUid(userid); //系统统一定义大写用户ID
    			        loginContext.setIp(model.getDeviceid());//装载IP
    			        loginContext.setParam1(model.getDeviceid());
    			      //设置当前登陆用户为管理员登陆
    			        loginContext.setLoginType(LoginConst.LOGIN_TYPE_MOBILE);  
    			        loginContext.setDeviceType(LoginContext.LOGIN_DEVICE_TYPE_WEIXIN);
    				 //初始化上下文
    				 loginService.initContextInfo(loginContext,LoginConst.LOGIN_TYPE_MOBILE);
    				 return actionInvocation.invoke(); 
    			 }else{
    				 return Action.ERROR;
    			 }
    		 }else{
    			 return Action.ERROR;
    		 }
  
         }
	
	       
	}
}
