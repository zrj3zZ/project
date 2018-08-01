package com.iwork.core.server.root;

import java.lang.reflect.Constructor;
import org.apache.log4j.Logger;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.constant.AppContextConstant;
import com.iwork.app.login.action.LoginAction;
import com.iwork.app.login.constant.LoginConst;
import com.iwork.app.login.control.LoginContext;
import com.iwork.app.login.service.LoginService;
import com.iwork.commons.ClassReflect;
import com.iwork.commons.util.UtilNumber;
import com.iwork.core.constant.GlobalField;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.server.ge.control.sso.SSOAdapterInterface;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.definition.deployment.action.ProcessDesignerAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
/**
 * 登陆验证拦截器
 * @author Administrator
 *
 */
public class IWorkInterceptor implements Interceptor {
	private static Logger logger = Logger.getLogger(IWorkInterceptor.class);
	protected String locale;
	private LoginService loginService;
	public void destroy() {
		// TODO Auto-generated method stub

	}
	public void init() {
		// TODO Auto-generated method stub

	}
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		 HttpServletRequest request=ServletActionContext.getRequest();
		 //xlj 漏洞扫描 2018年5月15日11:23:41
		 /*HttpSession hsession = request.getSession(true);
		    if (hsession.isNew() ||request ==null || request.getHeader("user-agent") == null) {
		       //session无效，在这里进行页面跳转，返回到登录页面
		    	return Action.LOGIN;
		    }*/
		String userAgent = request.getHeader("user-agent").toLowerCase();
		if(userAgent.indexOf("android")!=-1 || userAgent.indexOf("ipad")!=-1 || userAgent.indexOf("iphone")!=-1){
			return actionInvocation.invoke();
		}
		boolean ssoCheck = false; 
		 Object action = actionInvocation.getAction();  
		 String actionName = actionInvocation.getProxy().getActionName();
	        boolean isCheckLogin = false;  //判断是否已经做完校验
	    	//SSO自动登陆
	        if (action instanceof LoginAction) {   // 对LoginAction不做该项拦截
	        	boolean flag = true;
//	        	if(actionName.equals("ssologin")&&ssoAdapter.equals("on")){
//	        		flag = this.checkSSOLogin(action);
//	        		isCheckLogin = true;
//		    	} 
	        	if(flag){
	        		return actionInvocation.invoke(); 
	        	}else{
	        		return Action.LOGIN;
	        	}
	        }else if (action instanceof ProcessDesignerAction) {   // 对LoginAction不做该项拦截
	        	return actionInvocation.invoke(); 
	        }
	        
	     boolean isCheck = false;
        // 验证 session  
        Map session = actionInvocation.getInvocationContext().getSession();
       
        if(session.get(AppContextConstant.LOGIN_CONTEXT_INFO)==null){
        	if(isCheckLogin){
        		return Action.LOGIN;
        	}else{
        		ssoCheck = this.checkSSOLogin(action); 
        		
            	if(ssoCheck){
            		isCheck = true;
            	}else{
            		return Action.LOGIN;
            	}  
        	}
        	
        }
        
        LoginContext loginContext = (LoginContext) session.get(AppContextConstant.LOGIN_CONTEXT_INFO);  
        if (loginContext!=null&&loginContext.getUid()!= null) {  
            // 存在的情况下进行后续操作。  
        	isCheck = true;
        } else {
        	
            // 否则终止后续操作，返回LOGIN  
        	ssoCheck = this.checkSSOLogin(action);
        	if(ssoCheck){
        		isCheck = true;
        	}else{
        		return Action.LOGIN;
        	}
        }
        if(isCheck){
        	String method = ServletActionContext.getRequest().getMethod();
//        	if(method.equals("POST")){
//        		
//        	}else{
//        		Map map=actionInvocation.getInvocationContext().getParameters();  
//                Set keys = map.keySet();  
//                          Iterator iters = keys.iterator();  
//                        while(iters.hasNext()){  
//                            Object key = iters.next();  
//                            if(sqlValidate(key.toString())){
//                             	 return "nosecurity";
//                            }
//                            Object value = map.get(key);  
//                            if(value instanceof String){
//                                String keyStr = ObjectUtil.getString(key);
//                                String valStr = ObjectUtil.getString(value);
//                                if(sqlValidate(valStr)){
//                               	 return "nosecurity";
//                                }
//                            }else if(value instanceof String[]){
//                            	String[] values = (String[])value;
//                            	for(String str:values){
//                            		if(sqlValidate(str)){
//                                      	 return "nosecurity";
//                                       }
//                            	}
//                            }
////                        
//                        } 
//        	}
        	return actionInvocation.invoke();
        }else{
        	return Action.LOGIN; 
        }
	}
	
	protected static boolean sqlValidate(String str) {  
        str = str.toLowerCase();//统一转为小写  
        boolean isNumber = false;
        try{
        	isNumber = UtilNumber.bIsNumber(str);
        }catch(Exception e){} 
        if(isNumber){
        	return false;   
        }
        String badStr = "'| and |join|exec |\\|;|,| |execute|0x0d|0x0a|<|>|/*|*/|@|$|%|\"|()|+|,|insert|select|delete|create|drop|update| count|drop |*|%| chr| mid| master|truncate |" +  
                " char|declare | sitename|net user| xp_cmdshell| or |+ |like'| and |exec |execute |insert |create |drop |" +  
                " table | from | grant | use | group_concat |column_name|" +  
                "information_schema.columns|table_schema | union |where |select |delete |update | order|by | count|*|" +  
                " chr| mid| master| truncate| char| declare| or | like| //| /|#";//过滤掉的sql关键字，可以手动添加  
        String[] badStrs = badStr.split("\\|");  
        for (int i = 0; i < badStrs.length; i++) {  
            if (str.indexOf(badStrs[i]) >= 0) {  
                return true;  
            }else if(str.indexOf("|") >= 0){
            	 return true;  
            }
        }  
        return false;  
    }  
	 /** 
     * @Description: 遍历参数数组里面的数据，取出空格 
     * @param params 
     * @return 
     */  
    private String[] transfer(String[] params){  
        for(int i=0;i<params.length;i++){  
            if(StringUtils.isEmpty(params[i]))continue;  
            params[i]=params[i].trim();  
        }  
        return params;  
    }  
	
	/**
	 * 检查统一身份认证信息
	 * @return
	 */
	private boolean checkSSOLogin(Object action){
		boolean flag = false;
		 HttpServletRequest request=ServletActionContext.getRequest();
		 ActionContext actionContext = ActionContext.getContext();
		 String mode = SystemConfig._ssoLoginConf.getSsoMode();
		 String classpath =  SystemConfig._ssoLoginConf.getSsoClassAdapter();
		 if(classpath!=null){
			 if(classpath.equals("local")||mode.equals("off")){
				 flag = false;
			 }else{
				 SSOAdapterInterface sso =  this.getSSOAdapter();
				 boolean checkFlag = sso.check(request, action);
				 if(checkFlag){
					 String userId = sso.getCheckUserId(request, action);
					 if(userId!=null&&userId!=""){
						 UserContext uc = UserContextUtil.getInstance().getUserContext(userId.toUpperCase());
						 request.getSession().setAttribute("CASLOGIN","success");
			            	if(uc!=null){
							 if(loginService==null){
								 loginService =  (LoginService)SpringBeanUtil.getBean("loginService"); 
							 }
								  Locale l = Locale.getDefault();     
			    	                if(locale==null){     
			    	                	locale = SystemConfig._iworkServerConf.getDefaultLocale();   
			    	                }
			    	                if (locale.equals("zh_CN")) {     
			    	                   l = new Locale("zh", "CN");     
			    	                }else if (locale.equals("en_US")) {      
			    	                   l = new Locale("en", "US");      
			    	                }
			    	                actionContext.setLocale(l); 
			    	                ServletActionContext.getRequest().getSession().setAttribute("WW_TRANS_I18N_LOCALE", l); 
			    	        	String sessiontime = SystemConfig._iworkServerConf.getSessionTime();
			    	        	LoginContext loginContext = new LoginContext(); 
			    	        	  String ipaddress = request.getLocalAddr(); //获取客户端IP地址
			    	  	        loginContext.setUid(userId.toUpperCase()); //系统统一定义大写用户ID 
			    	  	        loginContext.setPwd(null);
			    	  	        loginContext.setMD5Pwd(null); 
			    	  	        loginContext.setIp(ipaddress);//装载IP
			    	  	      //设置当前登陆用户为管理员登陆
			    	  	        loginContext.setLoginType(LoginConst.LOGIN_TYPE_WEB);  
			    	  	        loginContext.setDeviceType(LoginContext.LOGIN_DEVICE_TYPE_WEB);
			    	        	if(sessiontime!=null){
			    		        	if(!sessiontime.equals("")){
			    		        		try {
			    							request.getSession().setMaxInactiveInterval(Integer.parseInt(sessiontime));
			    							//登录成功后初始化上下文
			    							loginService.initContextInfo(loginContext,LoginConst.LOGIN_TYPE_WEB);
			    							flag = true;
			    						} catch (Exception e) {
			    							System.out.println("【系统异常】登录时设置SESSION失败!");
			    						}
			    		        	}
			    	        	}
								  
							 }
					 }else{ 
						 flag = false;
					 }
				 }
			 }
		 }
		return flag;
	} 
	
	/**
	 * 获取登陆适配器
	 * @return
	 */
	 private SSOAdapterInterface getSSOAdapter() {
	        String className = SystemConfig._ssoLoginConf.getSsoClassAdapter();
	        Constructor _cons = null;
	        Class[] parameterTypes = {};
	        try {  
	            _cons = ClassReflect.getConstructor(className, parameterTypes);
	            if (_cons != null) {
	                Object[] params = {};
	                return (SSOAdapterInterface) _cons.newInstance(params);
	            } 
	   /*     } catch (ClassNotFoundException ce) {
	            System.err.println("AWS ClassLoader>>在getLoginAdapter中,类[" + className + "]没有找到!");
	            clogger.error(e,e);
	        } catch (NoSuchMethodException ne) {
	            System.err.println("AWS ClassLoader>>在getLoginAdapter中,类[" + className + "]构造方法不匹配!");
	            nlogger.error(e,e);
	        } catch (InstantiationException ie) {
	            System.err.println("AWS ClassLoader>>在getLoginAdapter中,类[" + className + "]构造实例时出错!");
	            ilogger.error(e,e);
	        } catch (java.lang.IllegalAccessException le) {
	            System.err.println("AWS ClassLoader>>在getLoginAdapter中,类[" + className + "]抛出IllegalAccessException!");
	            llogger.error(e,e);
	        } catch (java.lang.reflect.InvocationTargetException invoke) {
	            System.err.println("AWS ClassLoader>>在getLoginAdapter中,类[" + className + "]抛出InvocationTargetException!");
	            invoklogger.error(e,e);*/
	        }catch (Exception e) {
				logger.error(e,e);
			}
	        return null;
	    }
	
	private void putPerPath(){
		 HttpServletRequest request=ServletActionContext.getRequest();
			String path=request.getRequestURI();
			String actionPath=path;
			//访问服务器所带有的参数信息
			String queryInfo=request.getQueryString();
			if(queryInfo!=null&&(!queryInfo.equals(""))){
				actionPath=actionPath+"?"+queryInfo;
			}
			ActionContext.getContext().getSession().put(GlobalField.PRE_PATH, actionPath);
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}

}
