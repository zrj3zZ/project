package com.iwork.app.login.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.security.interfaces.RSAPrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.ServletActionContext;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.CookieUtils;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.constant.AppContextConstant;
import com.iwork.app.login.cache.LoginCache;
import com.iwork.app.login.constant.LoginConst;
import com.iwork.app.login.control.LoginContext;
import com.iwork.app.login.service.LoginService;
import com.iwork.app.navigation.web.model.TopMenuModel;
import com.iwork.commons.AbstractJSONAction;
import com.iwork.commons.DESEncoder;
import com.iwork.commons.util.RSAUtils;
import com.iwork.commons.util.ShaSaltUtil;
import com.iwork.core.constant.GlobalField;
import com.iwork.core.organization.constant.UserTypeConst;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgCompanyService;
import com.iwork.core.organization.service.OrgDepartmentService;
import com.iwork.core.organization.service.OrgUserService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.session.UserOnlineManage;
import com.iwork.core.util.MD5;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.MessageAPI;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.opensymphony.xwork2.ActionContext;

public class LoginAction extends AbstractJSONAction {
	
	protected OrgCompanyService orgCompanyService;
	protected OrgDepartmentService orgdepartmentService;
	protected OrgUserService orgUserService;
	protected String userid;
	protected String username;
	protected String schma;
	protected String txtPWD;
	protected String ip;
	protected String param1;
	protected String loginInfo = "";
	protected String loginInfo2 = "";
	protected String param2;
    protected LoginService loginService;
    protected UserContext userContext;
    protected String rememberName;
	protected String rememberPwd;
	protected String autoLogin;
	protected ByteArrayInputStream inputStream;
	protected String identifyCode;//验证码
	protected List<UserContext> userOnlineList;//验证码
	protected String isShowYZMorNot;
	protected String ticket;
	protected String locale;
	protected String deviceId;
	protected String deviceType;
	protected String redirectURL;
	protected String phone;
	protected String smsNum;
	protected String loginMode;
	protected String loginModeTest;
	protected String useridlocal;
	protected String passwordlocal;
	protected String identifyCodelocal;	
	protected String schmalocal;
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	/**
	 * 生成验证码
	 * @return
	 * @throws Exception 
	 */
	public String showidentifyCode() throws Exception{
		//在内存中创建图片的大小
		int width=60,height=20;
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);  
		 // 获取图形上下文  
        Graphics g = image.getGraphics();  
        // 生成随机类  
        Random random = new Random();  
        // 设定背景色  
        g.setColor(getRandColor(200, 250));  
        g.fillRect(0, 0, width, height); 
        // 设定字体  
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));  
        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到  
        g.setColor(getRandColor(160, 200));  
        for (int i = 0; i < 155; i++) {  
            int x = random.nextInt(width);  
            int y = random.nextInt(height);  
            int xl = random.nextInt(12);  
            int yl = random.nextInt(12);  
            g.drawLine(x, y, x + xl, y + yl);  
        }  
        // 取随机产生的认证码(4位数字)  
        String sRand = "";  
        for (int i = 0; i < 4; i++) {  
            String rand = String.valueOf(random.nextInt(10));  
            sRand += rand;  
            // 将认证码显示到图象中  
            g.setColor(new Color(20 + random.nextInt(110), 20 + random  
                    .nextInt(110), 20 + random.nextInt(110)));  
            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成  
            g.drawString(rand, 13 * i + 6, 16);  
        }  
        // 将认证码存入SESSION  
        ActionContext.getContext().getSession().put("rand", sRand);  
        // 图象生效  
        g.dispose();
        ByteArrayOutputStream output = new ByteArrayOutputStream();  
        ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);  
        ImageIO.write(image, "JPEG", imageOut);  
        imageOut.close();  
        ByteArrayInputStream input = new ByteArrayInputStream(output  
                .toByteArray());  
        this.setInputStream(input);
		return SUCCESS;
	}
    /* 
     * 给定范围获得随机颜色的工具方法
     */  
    private Color getRandColor(int fc, int bc) {  
        Random random = new Random();  
        if (fc > 255)  
            fc = 255;  
        if (bc > 255)  
            bc = 255;  
        int r = fc + random.nextInt(bc - fc);  
        int g = fc + random.nextInt(bc - fc);  
        int b = fc + random.nextInt(bc - fc);  
        return new Color(r, g, b);  
    }
    
    /**
     * 加载登录
     * @return
     */
    public String login(){
    	String ssoAdapter = SystemConfig._ssoLoginConf.getSsoMode();
//    	//SSO自动登陆
    	if(ssoAdapter.equals("on")){
    		String userid = UserContextUtil.getInstance().getCurrentUserId();
    		if(!userid.equals(UserContextUtil.FREE_ACCOUNT)){
    			return "ssologin"; 
    		}else{
    			loginInfo = "单点登录链接失败，请输入用户名及密码或联系系统管理员!";
    			return SUCCESS;
    		}
    	}else{
    		return SUCCESS;
    	}
    }
    public String ssologin(){    	
    	return SUCCESS;
    }
	/**
	 * 登录验证
	 * @return
	 */
    public String dologin() throws Exception{    	
    	if(userid == null || txtPWD ==null){return ERROR;}//xlj 漏洞扫描 2018年5月15日10:53:51
    	if(userid.length()>256){
    		loginInfo = "用户名过长，最多256个字符!";
    		return ERROR;
    	}
    	if(checkLoginInfo(userid)||checkLoginInfoPWD(txtPWD)||checkLoginInfo(isShowYZMorNot) ){//|| checkLoginInfo(identifyCode)
    		loginInfo = "登录信息包含非法字符!";
    		return ERROR;
    	}
    	
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request=(HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);  
		boolean isHttps = SystemConfig._iworkServerConf.getIsHttps()==null?false:SystemConfig._iworkServerConf.getIsHttps().equals("on");
		Cookie schmalocalObjCookie= CookieUtils.getCookie(request, "schmalocal");
		schmalocal = schmalocalObjCookie==null?"":schmalocalObjCookie.getValue();
		Cookie useridlocalObjCookie= CookieUtils.getCookie(request, "useridlocal");
		useridlocal = useridlocalObjCookie==null?"":useridlocalObjCookie.getValue();
		Cookie passwordlocalObjCookie= CookieUtils.getCookie(request, "passwordlocal");
		passwordlocal = passwordlocalObjCookie==null?"":passwordlocalObjCookie.getValue();
		String isRSA = SystemConfig._iworkServerConf.getIsRSA();
		String p = request.getParameter("password");
		if(isRSA!=null&&isRSA.equals("on")){
			RSAPrivateKey privateKey = (RSAPrivateKey)request.getSession().getAttribute("RSAPrivateKey");
			try {
				p = RSAUtils.decryptByPrivateKey(p, privateKey);
			} catch (Exception e) {
			}
		}
		if("1".equals(isShowYZMorNot) && (!(request.getParameter("userid").equals(useridlocal)) || !(p.equals(passwordlocal)))){
			if(schma==null||schma.equals("")){
				loginInfo = "验证码不能为空";
			}else{
				if(schmalocal==null&&schma!=null){
					loginInfo = "验证码错误";
				}else{
					loginInfo = "请检查输入的用户名或密码是否正确!";
				}
			}
			return ERROR;
		}else{
			if("1".equals(isShowYZMorNot)){
				if((schmalocal != null && !schmalocal.equals("")) && (schma != null && !schmalocal.equals("")) && schmalocal.equals(schma)){
					/*删除验证码cookie*/
					HttpServletResponse response=(HttpServletResponse) actionContext.get(ServletActionContext.HTTP_RESPONSE);
					CookieUtils.addCookie(request, response, "schmalocal", null, 0, null,isHttps);
					CookieUtils.addCookie(request, response, "useridlocal", null, 0, null,isHttps);
					CookieUtils.addCookie(request, response, "passwordlocal", null, 0, null,isHttps);
					/*执行登陆*/
					return chooselogin();
				}else{
					if(!schmalocal.equals(schma)){
						loginInfo = "验证码错误";
					}else if(schmalocal!=null){
						loginInfo = "验证码错误";
					}else if(schma == null || schma.equals("")){
						loginInfo = "验证码不能为空";
					}
					return ERROR;
				}
			}else{
				return chooselogin();
			}
		}
    }
    /**
     * 执行实际登录
     * @return
     */
	private String chooselogin() {
		if(userid.length()>256){
    		loginInfo = "用户名过长，最多256个字符!";
    		return ERROR;
    	}
		UserContext target = UserContextUtil.getInstance().getUserContext(userid);
		if(target != null){
			username = target.get_userModel().getUsername();
			userid = userid.toUpperCase();
		}
		boolean isHttps = SystemConfig._iworkServerConf.getIsHttps()==null?false:SystemConfig._iworkServerConf.getIsHttps().equals("on");
		HashMap hashMap = LoginCache.getInstance().getHashMap(userid);
		if(hashMap!=null){
			int unlockCount = Integer.parseInt(hashMap.get("UnlockCount").toString());
			int pauseTime = Integer.parseInt(SystemConfig._iworkServerConf.getLogincont());
			if(pauseTime==unlockCount){
				loginInfo = "系统帐户已锁定，请通知系统管理员解锁";
				return ERROR;
			}
		}
		//登录时给登录次数赋值
		TopMenuModel.user_Login_Count=1;
		loginInfo="";//登录时清空提示信息
		boolean flag = true; 
		int loginStatus;
		ActionContext actionContext = ActionContext.getContext();
		//获得response对象
		HttpServletResponse response=(HttpServletResponse) actionContext.get(ServletActionContext.HTTP_RESPONSE);  	
		//获得request对象
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		//保存帐号Cookie
		//保存密码Cookie
		String isRSA = SystemConfig._iworkServerConf.getIsRSA();
		if(isRSA!=null&&isRSA.equals("on")){
			RSAPrivateKey privateKey = (RSAPrivateKey)request.getSession().getAttribute("RSAPrivateKey");
			try {
				txtPWD = RSAUtils.decryptByPrivateKey(txtPWD, privateKey);
				userid = RSAUtils.decryptByPrivateKey(userid, privateKey).toUpperCase();
			} catch (Exception e) {
                loginerrlog("用户名或密码错误",userid.toUpperCase());
				loginInfo = "登录异常，请检查输入的用户名或密码是否正确！";
				HashMap map = LoginCache.getInstance().getHashMap(userid);
				if(map==null||map.isEmpty()){
					map=new HashMap();
					map.put("UnlockCount", 1);
				}else{
					map.put("UnlockCount", Integer.parseInt(map.get("UnlockCount").toString())+1);
				}
				LoginCache.getInstance().putHashMap(userid, map);
				return ERROR;
			}
		}
		/*String match="(?=^.{8,}$)[-\\da-zA-Z`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]*((\\d+[a-zA-Z]+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+)|(\\d+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+[a-zA-Z]+)|([a-zA-Z]+\\d+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+)|([a-zA-Z]+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+\\d+)|([-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+\\d+[a-zA-Z]+)|([-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+[a-zA-Z]+\\d+))[-\\da-zA-Z`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]*";
		boolean matches=txtPWD.replace(" ", "").matches(match);
		Pattern p = Pattern.compile("[0-9A-Za-z_]*");
		if(!p.matcher(userid.replace(" ", "")).matches() || !matches){
			loginInfo = "登录异常，请检查输入的用户名或密码是否正确！";
			return ERROR;
		 }*/
		if (rememberPwd != null&& rememberPwd.endsWith("true")) {
			CookieUtils.addCookie(request, response, "name", userid, 24 * 3600, null,isHttps);
			CookieUtils.addCookie(request, response, "pwd", txtPWD, 24 * 3600, null,isHttps);
			CookieUtils.addCookie(request, response, "checkPwd", "true", 24 * 3600, null,isHttps);
		}else{//删除密码Cookie
			CookieUtils.addCookie(request, response, "name", null, 24 * 3600, null,isHttps);
			CookieUtils.addCookie(request, response, "pwd", null, 0, null,isHttps);
			CookieUtils.addCookie(request, response, "checkPwd", null, 24 * 3600, null,isHttps);
		}
		//判断自动登录
		if (autoLogin != null&& autoLogin.endsWith("true")) {
			CookieUtils.addCookie(request, response, "autoLogin", autoLogin, 24 * 3600, null,isHttps);
		}else{
			CookieUtils.addCookie(request, response, "autoLogin", null, 24 * 3600, null,isHttps);
		}
		hashMap = LoginCache.getInstance().getHashMap(userid);
		String ipaddress = loginService.getIpaddress(request);; //获取客户端IP地址
		boolean ipMatch=false;
		if(target!=null){
			String roleId = target.get_orgRole().getId();
			Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
			String ip = config.get("IP");
			if((ip!=null&&!"".equals(ip))&&!"3".equals(roleId)){
				String[] split = ip.split(";");
				for (String ipValue : split) {
					String setIp = ipValue.substring(0, ipValue.indexOf(".",ipValue.indexOf(".",ipValue.indexOf(".")+1)+1));
					String getIp = ipaddress.substring(0, ipaddress.indexOf(".",ipaddress.indexOf(".",ipaddress.indexOf(".")+1)+1 ));
					if((getIp!=null&&!"".equals(getIp))&&!setIp.equals(getIp)){
						ipMatch=true;
					}else if ((getIp!=null&&!"".equals(getIp))&&setIp.equals(getIp)){
						ipMatch=false;
						break;
					}
				}
			}
		}
		String md5pwd = "";
		LoginContext loginContext = null;
		if(ipMatch){
			flag = false;
    		loginInfo = "券商内部人员请使用内部局域网登录系统！";
		}else if(userid!=null&&!"".equals(userid)&&txtPWD!=null&&!"".equals(txtPWD)){
			//执行MD5加密
			/*MD5 md5 = new MD5();
			md5pwd = md5.getEncryptedPwd(password);*/
			//执行SHA-256+SALT加密
			String SHAPwd=ShaSaltUtil.getEncryptedPwd(txtPWD,userid.toUpperCase());
		    loginContext = new LoginContext();
		    loginContext.setUid(userid.toUpperCase()); //系统统一定义大写用户ID
		    loginContext.setPwd(txtPWD);
		    loginContext.setMD5Pwd(SHAPwd);
		    loginContext.setIp(ipaddress);//装载IP
		    loginContext.setParam1(param1);
		    //E10ADC3949BA59ABBE56E057F20F883E
		  //设置当前登录用户为管理员登录
		    loginContext.setLoginType(LoginConst.LOGIN_TYPE_WEB);  
		    loginContext.setDeviceType(LoginContext.LOGIN_DEVICE_TYPE_WEB);
		  //执行登录动作
		    boolean isIdentifyCode = false;
		    
		    //判断是否开启验证码登录
		    String loginVerify = SystemConfig._iworkServerConf.getLoginVerify();
		    if(loginVerify.equals("on")){
		    	String userIdentifyCode = this.getIdentifyCode();
		    	String sysIdentifyCode = (String)ActionContext.getContext().getSession().get("rand");
		    	if(null==userIdentifyCode||"".equals(userIdentifyCode)){
		    		flag = false;
		    		loginInfo = "请输入验证码!";
		    	}else{
		    		if(!userIdentifyCode.equals(sysIdentifyCode)){
		    			flag = false;
		        		loginInfo = "验证码输入错误!";
                        loginerrlog(loginInfo,userid.toUpperCase());
		    		}else{
		    			isIdentifyCode = true;
		    		}
		    	}
		    }else{
		    	isIdentifyCode = true;
		    }
			
			if(isIdentifyCode){
				 HttpSession session = request.getSession(false);
				 if(session!=null){
					 session.invalidate();
					 Cookie[] cookies = request.getCookies();//获取cookies
					 if(cookies != null){
						 for (int i = 0; i < cookies.length; i++) {
							 if(cookies[i].getName().equals("JSESSIONID")){
								 cookies[i].setMaxAge(0);//让cookie过期
								 break;
							 }
						}
					 }
				 }
				loginStatus = loginService.login(loginContext);
				session = request.getSession(true);
		        if(loginStatus==LoginConst.LOGIN_STATUS_OK){   //登录成功
		        	HashMap map = LoginCache.getInstance().getHashMap(userid);
		        	if(map==null||map.isEmpty()){
		        		map=new HashMap();
		        		map.put("UnlockCount", 0);
		        	}else{
		        		map.put("UnlockCount", 0);
		        	}
		        	LoginCache.getInstance().putHashMap(userid, map);
		        	flag = true; 
		        	//
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
		        	if(sessiontime!=null){
			        	if(!sessiontime.equals("")){
			        		try {
								request.getSession().setMaxInactiveInterval(Integer.parseInt(sessiontime));
								//登录成功后初始化上下文
								loginService.initContextInfo(loginContext,LoginConst.LOGIN_TYPE_WEB);
							} catch (Exception e) {
								
							}
			        	}
		        	}
		        }else if(loginStatus==LoginConst.LOGIN_STATUS_DATABASE_ERROR){  
		        	flag = false;
		        	loginInfo = "数据库链接错误，导致登录失败，请通知系统管理员";
		        }else if(loginStatus==LoginConst.LOGIN_STATUS_DISENABLE){  
		        	flag = false;
		        	loginInfo = "系统帐户已锁定，请通知系统管理员解锁";
		        }else if(loginStatus==LoginConst.LOGIN_STATUS_PWD_ERROR||loginStatus==LoginConst.LOGIN_STATUS_USER_NOTFIND){  
		        	flag = false;
                    loginerrlog("用户名或密码错误",userid.toUpperCase());
		        	loginInfo = "登录异常，请检查输入的用户名或密码是否正确！";
		        	HashMap map = LoginCache.getInstance().getHashMap(userid);
		        	if(map==null||map.isEmpty()){
		        		map=new HashMap();
		        		map.put("UnlockCount", 1);
		        	}else{
		        		map.put("UnlockCount", Integer.parseInt(map.get("UnlockCount").toString())+1);
		        	}
		        	LoginCache.getInstance().putHashMap(userid, map);
		        }else if(loginStatus==LoginConst.LOGIN_STATUS_USER_NOTFIND){  
		        	flag = false;
		        	loginInfo = "未发现访问的用户，请重试";
		        }else if(loginStatus==LoginConst.LOGIN_STATUS_INNER_ERROR){  
		        	flag = false;
		        	loginInfo = "内部错误，请通知管理员";
		        }else{
                    loginerrlog("用户名或密码错误",userid.toUpperCase());
		        	loginInfo = "登录异常，请检查输入的用户名或密码是否正确！";
		        }
			}
		}else if(phone!=null&&!"".equals(phone)){
		    loginContext = new LoginContext();
			loginContext.setIp(ipaddress);//装载IP
		    loginContext.setParam1(param1);
		  //设置当前登录用户为管理员登录
		    loginContext.setLoginType(LoginConst.LOGIN_TYPE_WEB);  
		    loginContext.setDeviceType(LoginContext.LOGIN_DEVICE_TYPE_WEB);
		    String userid=loginService.getString("select USERID from orguser where mobile = ?",phone, "USERID");
		    String password=loginService.getString("select PASSWORD from orguser where mobile = ?",phone, "PASSWORD");
		    loginContext.setUid(userid.toUpperCase()); //系统统一定义大写用户ID
		    loginContext.setMD5Pwd(password);
			boolean isIdentifyCode = false;
			if(SystemConfig._iworkServerConf.getLoginVerify().equals("on")){
		    	String userIdentifyCode = this.getIdentifyCode();
		    	String sysIdentifyCode = (String)ActionContext.getContext().getSession().get("rand");
		    	if(null==userIdentifyCode||"".equals(userIdentifyCode)){
		    		flag = false;
		    		loginInfo2 = "请输入验证码!";
		    	}else{
		    		if(!userIdentifyCode.equals(sysIdentifyCode)){
		    			flag = false;
		    			loginInfo2 = "验证码输入错误!";
                        loginerrlog(loginInfo,userid.toUpperCase());
		    		}else{
		    			isIdentifyCode = true;
		    		}
		    	}
		    }else{
		    	isIdentifyCode = true;
		    }
			if(isIdentifyCode){
				loginStatus = loginService.logincheckYzm(phone,smsNum,loginContext);
				if(loginStatus==LoginConst.LOGIN_STATUS_OK){   //登录成功
					HashMap map = LoginCache.getInstance().getHashMap(phone);
		        	if(map==null||map.isEmpty()){
		        		map=new HashMap();
		        		map.put("UnlockCount", 0);
		        	}else{
		        		map.put("UnlockCount", 0);
		        	}
		        	LoginCache.getInstance().putHashMap(userid, map);
		        	flag = true; 
		        	//
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
		        	if(sessiontime!=null){
			        	if(!sessiontime.equals("")){
			        		try {
								request.getSession().setMaxInactiveInterval(Integer.parseInt(sessiontime));
								//登录成功后初始化上下文
								loginService.initContextInfo(loginContext,LoginConst.LOGIN_TYPE_WEB);
							} catch (Exception e) {
								
							}
			        	}
		        	}
		        }else if(loginStatus==LoginConst.LOGIN_STATUS_DATABASE_ERROR){  
		        	flag = false;
		        	loginInfo2 = "数据库链接错误，导致登录失败，请通知系统管理员";
		        }else if(loginStatus==LoginConst.LOGIN_STATUS_DISENABLE){  
		        	flag = false;
		        	loginInfo2 = "系统帐户已锁定，请通知系统管理员解锁";
		        }else if(loginStatus==LoginConst.LOGIN_STATUS_PWD_ERROR||loginStatus==LoginConst.LOGIN_STATUS_USER_NOTFIND){  
		        	flag = false;
		        	loginInfo2 = "请核对信息，验证码错误或已失效，请重新获取验证码！";
		        	HashMap map = LoginCache.getInstance().getHashMap(phone);
		        	if(map==null||map.isEmpty()){
		        		map=new HashMap();
		        		map.put("UnlockCount", 1);
		        	}else{
		        		map.put("UnlockCount", Integer.parseInt(map.get("UnlockCount").toString())+1);
		        	}
		        	LoginCache.getInstance().putHashMap(userid, map);
		        }else if(loginStatus==LoginConst.LOGIN_STATUS_USER_NOTFIND){  
		        	flag = false;
		        	loginInfo2 = "未发现访问的用户，请重试";
		        }else if(loginStatus==LoginConst.LOGIN_STATUS_INNER_ERROR){  
		        	flag = false;
		        	loginInfo2 = "内部错误，请通知管理员";
		        }else{
                    loginerrlog("用户名或密码错误",userid.toUpperCase());
		        	loginInfo2 = "登录异常，请检查输入的用户名或密码是否正确！";
		        }
			}
		}else{
			flag = false;
			loginInfo2 = "用户名或密码异常!";
		}
		if(flag){
			try {
				Object pre_path = ActionContext.getContext().getSession().get(GlobalField.PRE_PATH);
				if(pre_path!=null){
					redirectURL = pre_path.toString();
					 ActionContext.getContext().getSession().remove(GlobalField.PRE_PATH);
					return "redirectURL";
				}
			} catch (Exception e) {
			}
			return SUCCESS;
		}
		else {
			return ERROR;
		}
	}  
     
    /**
     * 移动端登录验证
     * @return
     */
    public String mobileLogin(){
    	loginInfo="";//登录时清空提示信息
    	if(null!=userid&&txtPWD!=null){
    		boolean flag = true;
    		String testenry = "y";
    		int loginStatus;
    		ActionContext actionContext = ActionContext.getContext();
    		//获得response对象 
    		HttpServletResponse response=(HttpServletResponse) actionContext.get(ServletActionContext.HTTP_RESPONSE);  	
    		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
    		String ipaddress = request.getRemoteAddr(); //获取设备ID
    		String decrUerId = null; //解密后的用户名
    		String decrPwd = null;//解密后的密码
    		try {
    			String key = "ibpmsoft";
    			String ecrTest = DESEncoder.encryptDES(testenry, key);
    			userid = userid.replace(" ","+");
    			 decrUerId = DESEncoder.decryptDES(userid,key);
				 decrPwd = DESEncoder.decryptDES(txtPWD,key);
			} catch (Exception e1) {
				flag = false;
    			loginInfo = "ERROR-0504";//内部错误，请通知管理员
    			return loginInfo;
			}
    		
    		String md5pwd = "";
    		MD5 md5 = new MD5();
    		md5pwd = md5.getEncryptedPwd(decrPwd);
			String SHAPwd=ShaSaltUtil.getEncryptedPwd(decrPwd,userid.toUpperCase());
    		LoginContext loginContext = new LoginContext();
    		loginContext.setUid(decrUerId.toUpperCase()); //系统统一定义大写用户ID
    		loginContext.setPwd(decrPwd);
    		loginContext.setMD5Pwd(SHAPwd);
    		loginContext.setIp(deviceId);//装载IP
    		loginContext.setParam1(param1);
    		 loginContext.setLoginType(LoginConst.LOGIN_TYPE_MOBILE); 
    		loginContext.setDeviceType(deviceType);
    		//执行登录动作
    		loginStatus = loginService.login(loginContext);
    		if(loginStatus==LoginConst.LOGIN_STATUS_OK){   //登录成功
    			flag = true; 
    			String sessiontime = SystemConfig._iworkServerConf.getSessionTime();
    			if(sessiontime!=null){
    				if(!sessiontime.equals("")){ 
    					try {
    						request.getSession().setMaxInactiveInterval(Integer.parseInt(sessiontime));
    						//登录成功后初始化上下文 
    						loginService.initContextInfo(loginContext,LoginConst.LOGIN_TYPE_MOBILE);
    					} catch (Exception e) {
    						
    					}
    				}
    			}
    		}else if(loginStatus==LoginConst.LOGIN_STATUS_DATABASE_ERROR){  
    			flag = false;
    			loginInfo = "ERROR-0500";//数据库链接错误，导致登录失败，请通知系统管理员
    		}else if(loginStatus==LoginConst.LOGIN_STATUS_DISENABLE){  
    			flag = false;
    			loginInfo = "ERROR-0501";//系统帐户已锁定，请通知系统管理员解锁
    		}else if(loginStatus==LoginConst.LOGIN_STATUS_PWD_ERROR||loginStatus==LoginConst.LOGIN_STATUS_USER_NOTFIND){  
    			flag = false;
                loginerrlog("用户名或密码错误",userid.toUpperCase());
    			loginInfo = "ERROR-0502";//登录异常，请检查输入的用户名或密码是否正确;
    		}else if(loginStatus==LoginConst.LOGIN_STATUS_USER_NOTFIND){  
    			flag = false;
    			loginInfo = "ERROR-0503";//未发现访问的用户，请重试
    		}else if(loginStatus==LoginConst.LOGIN_STATUS_INNER_ERROR){  
    			flag = false;
    			loginInfo = "ERROR-0504";//内部错误，请通知管理员
    		}else{
    			loginInfo = "ERROR-0502";
    		}
    		
			if (loginStatus == LoginConst.LOGIN_STATUS_OK) { // 登录成功后判断权限
				int loginAccessStatus = loginService
						.mobileLoginAccessStatus(loginContext);
				if (loginAccessStatus == LoginConst.LOGIN_ACCESS_USER_VISIT) {
					flag = false;
					loginInfo = "ERROR-0601";// 禁止该用户访问
				} else if (loginAccessStatus == LoginConst.LOGIN_ACCESS_USER_ANMENG) {
					flag = false;
					loginInfo = "ERROR-0602";// 令牌错误
				} else if (loginAccessStatus == LoginConst.LOGIN_ACCESS_USER_BIND) {
					flag = false;
					loginInfo = "ERROR-0603";// 非绑定设备，禁止登录
				}
			}
    		if(flag&&loginContext!=null){
    			if(deviceType!=null&&deviceType.equals("android")){
    				ResponseUtil.writeTextUTF8(request.getSession().getId());
    			}else{
    				OrgUser model = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
        			if(model!=null){
        				JSONArray json = JSONArray.fromObject(model);
            			StringBuffer tmp = new StringBuffer("");
            			tmp.append(json);
            			ResponseUtil.writeTextUTF8(tmp.toString().substring(1,tmp.toString().length()-1));
        			}
    			}
    			
    		}else{
    			ResponseUtil.writeTextUTF8(loginInfo);
    		}
    	}else{
    		ResponseUtil.writeTextUTF8("");
    	}
    	return null;
    }
    
    /**
     * 获得在线用户列表
     * @return
     */
    public String showUserOnlineList(){
    	userOnlineList = UserOnlineManage.getInstance().getOnLineList();
    	return SUCCESS;
    }
    
    
    /**
     * 登录管理员控制台
     * @return
     */
    public String loginadmin(){
    	if(userid!=null&&!"".equals(userid)){
    		if(userid.length()>20){
        		loginInfo = "用户名过长，最多20个字符!";
        		return ERROR;
        	}
    	
    	loginInfo="";//登录时清空提示信息
    	boolean flag = true;
    	int loginStatus; 
    	ActionContext actionContext = ActionContext.getContext();
    	//获得response对象
    	HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
    	HttpServletResponse response=(HttpServletResponse) actionContext.get(ServletActionContext.HTTP_RESPONSE);
    	boolean isHttps = SystemConfig._iworkServerConf.getIsHttps()==null?false:SystemConfig._iworkServerConf.getIsHttps().equals("on");
    	
    	String isRSA = SystemConfig._iworkServerConf.getIsRSA();
		if(isRSA!=null&&isRSA.equals("on")){
			RSAPrivateKey privateKey = (RSAPrivateKey)request.getSession().getAttribute("RSAPrivateKey");
			try {
				txtPWD = RSAUtils.decryptByPrivateKey(txtPWD, privateKey);
			} catch (Exception e) {
                loginerrlog("用户名或密码错误",userid.toUpperCase());
				loginInfo = "登录异常，请检查输入的用户名或密码是否正确！";
				HashMap map = LoginCache.getInstance().getHashMap(userid);
				if(map==null||map.isEmpty()){
					map=new HashMap();
					map.put("UnlockCount", 1);
				}else{
					map.put("UnlockCount", Integer.parseInt(map.get("UnlockCount").toString())+1);
				}
				LoginCache.getInstance().putHashMap(userid, map);
				return ERROR;
			}
		}
    	
        String ipaddress = request.getRemoteAddr(); //获取客户端IP地址
        
        String md5pwd = "";
    	/*MD5 md5 = new MD5();
		md5pwd = md5.getEncryptedPwd(password);*/ 
		String SHAPwd=ShaSaltUtil.getEncryptedPwd(txtPWD,userid.toUpperCase());
        LoginContext loginContext = new LoginContext();
        loginContext.setUid(userid.toUpperCase()); //系统统一定义大写用户ID
        loginContext.setPwd(txtPWD);
        loginContext.setMD5Pwd(SHAPwd);
        loginContext.setIp(ipaddress);//装载IP
        loginContext.setParam1(param1);
        loginContext.setParam2(param2);
        loginContext.setDeviceType(LoginContext.LOGIN_DEVICE_TYPE_WEB); 
        //设置当前登录用户为管理员登录
        loginContext.setLoginType(LoginConst.LOGIN_TYPE_ADMIN); 
	        
        //执行登录动作
        String userIdentifyCode = this.getIdentifyCode();
    	String sysIdentifyCode = (String)actionContext.getSession().get("rand");
    	if(null==userIdentifyCode||"".equals(userIdentifyCode)){
    		flag = false;
    		loginInfo = "请输入验证码!";
    	}else{
    		if(!userIdentifyCode.equals(sysIdentifyCode)){
    			flag = false;
        		loginInfo = "验证码输入错误!";
                loginerrlog(loginInfo,userid.toUpperCase());
    		}else{
    			//判断管理员登录帐号是否为管理帐号
    			UserContext uc = UserContextUtil.getInstance().getUserContext(loginContext.getUid());
    			if(uc==null){
                    loginerrlog("用户名或密码错误",userid.toUpperCase());
    				loginInfo = "登录异常，请检查输入的用户名或密码是否正确！";
    				return ERROR; 
    			}else if(!uc.get_userModel().getUsertype().equals(UserTypeConst.USER_TYPE_SYSTEMUSER)){
    				loginInfo = "当前的用户类型无法登录后台管理";
    				return ERROR; 
    			}
    	        loginStatus = loginService.adminlogin(loginContext);
    	        if(loginStatus==LoginConst.LOGIN_STATUS_OK){   //登录成功
    	        	flag = true; 
    	        	String sessiontime = SystemConfig._iworkServerConf.getSessionTime();
    	        	if(sessiontime!=null){
    		        	if(!sessiontime.equals("")){
    		        		try {
    							request.getSession().setMaxInactiveInterval(Integer.parseInt(sessiontime));
    							//登录成功后初始化上下文
    							loginService.initContextInfo(loginContext,LoginConst.LOGIN_TYPE_ADMIN);
    						} catch (Exception e) {
    							
    						}
    		        	}
    	        	}
    	        }else if(loginStatus==LoginConst.LOGIN_STATUS_DATABASE_ERROR){  
    	        	flag = false;
    	        	loginInfo = "数据库链接错误，导致登录失败，请通知系统管理员";
    	        }else if(loginStatus==LoginConst.LOGIN_STATUS_DISENABLE){  
    	        	flag = false;
    	        	loginInfo = "系统帐户已锁定，请通知系统管理员解锁";
    	        }else if(loginStatus==LoginConst.LOGIN_STATUS_PWD_ERROR||loginStatus==LoginConst.LOGIN_STATUS_USER_NOTFIND){  
    	        	flag = false;
                    loginerrlog("用户名或密码错误",userid.toUpperCase());
    	        	loginInfo = "登录异常，请检查输入的用户名或密码是否正确！";
    	        }else if(loginStatus==LoginConst.LOGIN_STATUS_USER_NOTFIND){  
    	        	flag = false;
    	        	loginInfo = "未发现访问的用户，请重试";
    	        }else if(loginStatus==LoginConst.LOGIN_STATUS_INNER_ERROR){  
    	        	flag = false;
    	        	loginInfo = "内部错误，请通知管理员";
    	        }else{
                    loginerrlog("用户名或密码错误",userid.toUpperCase());
    	        	loginInfo = "登录异常，请检查输入的用户名或密码是否正确！";
    	        }
    	        //ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
    	        //ctx.getBean("dataSource");
    		}
    	}
    	
    	if(flag){
    			if (rememberyhm != null&& rememberyhm.endsWith("true")) {
        			if(remembermm!=null&&remembermm.endsWith("true")){
        			CookieUtils.addCookie(request, response, "yhm", userid.toUpperCase(), 24 * 3600, null,isHttps);
        			CookieUtils.addCookie(request, response, "mm", txtPWD, 24 * 3600, null,isHttps);
        			CookieUtils.addCookie(request, response, "checkyhm", "true", 24 * 3600, null,isHttps);
        			CookieUtils.addCookie(request, response, "checkmm", "true", 24 * 3600, null,isHttps);
        			}else{
        			CookieUtils.addCookie(request, response, "yhm", userid.toUpperCase(), 24 * 3600, null,isHttps);
        			CookieUtils.addCookie(request, response, "mm", null, 0, null,isHttps);
            		CookieUtils.addCookie(request, response, "checkyhm", "true", 24 * 3600, null,isHttps);	
            		CookieUtils.addCookie(request, response, "checkmm", null, 24 * 3600, null,isHttps);	
        			}
        		}else{//删除密码Cookie
        			CookieUtils.addCookie(request, response, "yhm", null, 24 * 3600, null,isHttps);
        			CookieUtils.addCookie(request, response, "mm", null, 0, null,isHttps);
        			CookieUtils.addCookie(request, response, "checkyhm", null, 24 * 3600, null,isHttps);
        			CookieUtils.addCookie(request, response, "checkmm", null, 24 * 3600, null,isHttps);
        		}
    			
    			
    			
    		
    		
    		return SUCCESS;
    	}
    	else return ERROR;
    	}else{
    		return ERROR;
    	}
    }
    private String remembermm;
    private String rememberyhm;
    
    public String getRemembermm() {
		return remembermm;
	}
	public void setRemembermm(String remembermm) {
		this.remembermm = remembermm;
	}
	public String getRememberyhm() {
		return rememberyhm;
	}
	public void setRememberyhm(String rememberyhm) {
		this.rememberyhm = rememberyhm;
	}
	/**
     * 退出系统
     * @return
     */
    public String  exit(){
    	ActionContext actionContext = ActionContext.getContext();
    	HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
    	UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    	if(uc!=null){
    		UserOnlineManage.getInstance().remove(uc);
    	}
    	
    	request.getSession().removeAttribute(AppContextConstant.LOGIN_CONTEXT_INFO);
    	return SUCCESS; 
    }
    
    private static boolean checkLoginInfo(String info) {
    	if(info==null||info.equals("")){
    		return true;
    	}else{
    		String regEx = " and | exec | count | chr | mid | master | or | truncate | char | declare | join |insert |select |delete |update |create |drop ";
    		//Pattern pattern = Pattern.compile(regEx);
    		// 忽略大小写的写法
    		Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
    		Matcher matcher = pattern.matcher(info.trim());
    		// 字符串是否与正则表达式相匹配
    		
    		String regEx2="[`“”~!#$^%&*,+<>?）\\]\\[（—\"{};']";
    		Pattern pattern2 = Pattern.compile(regEx2, Pattern.CASE_INSENSITIVE);
    		// 字符串是否与正则表达式相匹配
    		Matcher matcher2 = pattern2.matcher(info.trim());
    		
    		int n = 0;
    		if(matcher.find()){
    			n++;
    		}
    		if(matcher2.find()){
    			n++;
    		}
    		if(n==0){
    			return false;
    		}else{
    			return true;
    		}
    	}
	}
    
    /*
     * 专门是给密码验证使用，因为密码验证可以有特殊符号
     */
    private static boolean checkLoginInfoPWD(String info) {
    	if(info==null||info.equals("")){
    		return true;
    	}else{
    		String regEx = " and | exec | count | chr | mid | master | or | truncate | char | declare | join |insert |select |delete |update |create |drop ";
    		//Pattern pattern = Pattern.compile(regEx);
    		// 忽略大小写的写法
    		Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
    		Matcher matcher = pattern.matcher(info.trim());
    		// 字符串是否与正则表达式相匹配
    		try {
				info = URLDecoder.decode(info,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
    		String regEx2="[&<>]";
    		Pattern pattern2 = Pattern.compile(regEx2, Pattern.CASE_INSENSITIVE);
    		// 字符串是否与正则表达式相匹配
    		Matcher matcher2 = pattern2.matcher(StringEscapeUtils.unescapeHtml(info.trim()));
    		
    		int n = 0;
    		if(matcher.find()){
    			n++;
    		}
    		if(matcher2.find()){
    			n++;
    		}
    		if(n==0){
    			return false;
    		}else{
    			return true;
    		}
    	}
	}
    
    /**
     * 这个检索方法用于获取登录方式为 1 时的验证码获取
     */
    public void getmobileyzm(){
    	if(checkLoginInfo(userid)||checkLoginInfoPWD(txtPWD)||checkLoginInfo(identifyCode)||checkLoginInfo(isShowYZMorNot)){
    		ResponseUtil.write(responseWrite("登录信息包含非法字符!",true));
    		return;
    	}
    	
		String loginVerify = SystemConfig._iworkServerConf.getLoginVerify();
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		HttpServletResponse response=(HttpServletResponse) actionContext.get(ServletActionContext.HTTP_RESPONSE);
		boolean isIdentifyCode = false;
		
		/**
		 * 判断是否开启图片验证码,开启做判断,未开启isIdentifyCode赋值true
		 * 判断验证码是否正确,正确isIdentifyCode赋值true,否则返回错误信息
		 */
		////1、写txt
		BufferedOutputStream out = openStream(request);
		
		if(loginVerify.equals("on")){
			byte a[] = (userid+"获取手机验证码"+";传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"判断是否开启图片验证功能(开启)\r\n").getBytes() ;
			streamWrite(out, a);
			String userIdentifyCode = request.getParameter("identifyCode");
			//获取session中的图片验证码值
			String sysIdentifyCode = (String)ActionContext.getContext().getSession().get("rand");
			//图片验证码判定
			if(null==userIdentifyCode||"".equals(userIdentifyCode)){
				byte b[] = (userid+"获取手机验证码"+";传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"判断图片验证码是否为空(为空)\r\n\r\n").getBytes() ;
				streamWrite(out, b);
				closeSream(out);
				ResponseUtil.write(responseWrite("请输入验证码!",true));
				return;
			}else{
				if(!userIdentifyCode.equals(sysIdentifyCode)){
					byte b[] = (userid+"获取手机验证码"+";传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"判断图片验证码是否正确(不正确)\r\n\r\n").getBytes() ;
					streamWrite(out, b);
					closeSream(out);
                    loginerrlog(loginInfo,userid);
					ResponseUtil.write(responseWrite("验证码输入错误!",true));
					return;
				}else{
					isIdentifyCode = true;
				}
			}
		}else{
			byte b[] = (userid+"获取手机验证码"+",传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"判断是否开启图片验证功能(未开启)\r\n").getBytes() ;
			streamWrite(out, b);
			isIdentifyCode = true;
		}
		/**
		 * 判断userid,username是否为空,为空返回错误信息 
		 */
		if(isIdentifyCode&&txtPWD!=null&&!txtPWD.equals("")&&userid!=null&&!userid.equals("")){
			byte b[] = (userid+"获取手机验证码"+",传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"判断用户名、密码是否为空(不为空)\r\n").getBytes() ;
			streamWrite(out, b);
			//判断是否开启手机验证码
			String isShowYZM = ConfigUtil.readAllProperties(CN_FILENAME).get("isShowYZM");
			
			byte c[] = (userid+"获取手机验证码"+",传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"获取MD5、SHA密码加密值\r\n").getBytes() ;
			streamWrite(out, c);
			//获取不通加密方式下的密码值
			String isRSA = SystemConfig._iworkServerConf.getIsRSA();
			if(isRSA!=null&&isRSA.equals("on")){
				byte d[] = (userid+"获取手机验证码"+",传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"判断RSA密码加密是否开启(开启)\r\n").getBytes() ;
				streamWrite(out, d);
				RSAPrivateKey privateKey = (RSAPrivateKey)request.getSession().getAttribute("RSAPrivateKey");
				try {
					txtPWD = RSAUtils.decryptByPrivateKey(txtPWD, privateKey);
					userid = RSAUtils.decryptByPrivateKey(userid, privateKey);
					txtPWD = URLDecoder.decode(txtPWD, "UTF-8");
				} catch (Exception e) {
				}
			}
			MD5 md5 = new MD5();
			Map<String, String> map = new HashMap<String, String>();
			String md5pwd = md5.getEncryptedPwd(txtPWD);
			String SHAPwd=ShaSaltUtil.getEncryptedPwd(txtPWD,userid.toUpperCase());
			
			//查询手机号做短信发送
			Map params = new HashMap<Integer,HashMap>();
			params.put(1, userid.toUpperCase());
			params.put(2, SHAPwd);
			params.put(3, md5pwd);
			
			List lables = new ArrayList();
			lables.add("MOBILE");
		
			String sql = "SELECT MOBILE FROM ORGUSER WHERE USERID=? and (PASSWORD=? OR PASSWORD=?)";
			
			String mobile = "";
			List mobilelist = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
			if(mobilelist.size()==1){
				mobile = ((HashMap)mobilelist.get(0)).get("MOBILE")==null?"":((HashMap)mobilelist.get(0)).get("MOBILE").toString();
			}
			//mobile = DBUtil.getString("SELECT MOBILE FROM ORGUSER WHERE USERID='"+userid.toUpperCase()+"' and (PASSWORD='"+SHAPwd+"' OR PASSWORD='"+md5pwd+"')" , "MOBILE");
			if("".equals(mobile)||mobile==null){
				byte d[] = (userid+"获取手机验证码"+",传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"查询手机号做短信发送(手机号为空)\r\n\r\n").getBytes() ;
				streamWrite(out, d);
				closeSream(out);
				ResponseUtil.write(responseWrite("验证码发送失败，请联系管理员!",true));
				return;
			}else{
				byte d[] = (userid+"获取手机验证码"+",传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"查询手机号做短信发送(手机号不为空)\r\n").getBytes() ;
				streamWrite(out, d);
			}
			//获取4未随机验证码
			RandomWordGenerator bs = new RandomWordGenerator("1234567890");
			schmalocal = bs.getWord(4);
			//存储Cookie 60s有效,用作登陆判定
			if(isShowYZM.equals("1")){
				byte d[] = (userid+"获取手机验证码"+",传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"判断是否开启手机验证码(开启),并存储USERID、USERNAME到Cookie\r\n").getBytes() ;
				streamWrite(out, d);
				boolean isHttps = SystemConfig._iworkServerConf.getIsHttps()==null?false:SystemConfig._iworkServerConf.getIsHttps().equals("on");
				CookieUtils.addCookie(request, response, "schmalocal", schmalocal, 60, null, isHttps);
				CookieUtils.addCookie(request, response, "useridlocal", userid, 60, null, isHttps);
				CookieUtils.addCookie(request, response, "passwordlocal", txtPWD, 60, null, isHttps);
			}
			//判断存储的验证码Cookie是否失效
			if(mobile != null && !mobile.equals("") && schmalocal != ""){
				//根据不同环境调用不同的短信发送方式
				byte d[] = (userid+"获取手机验证码"+",传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"验证码发送成功\r\n\r\n").getBytes() ;
				streamWrite(out, d);
				closeSream(out);
				MessageAPI.getInstance().sendSMS(userid,mobile, "手机验证码:"+schmalocal+",请妥善保管!【淘璞易】");
				ResponseUtil.write(responseWrite("验证码发送成功",true));
			}
		}else if(userid==null||userid.equals("")){
			byte d[] = (userid+"获取手机验证码"+",传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"判断用户名、密码是否为空(用户名为空)\r\n\r\n").getBytes() ;
			streamWrite(out, d);
			closeSream(out);
			ResponseUtil.write(responseWrite("请输入用户名!",true));
			return;
		}else if(txtPWD==null||txtPWD.equals("")){
			byte d[] = (userid+"获取手机验证码"+",传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"判断用户名、密码是否为空(密码为空)\r\n\r\n").getBytes() ;
			streamWrite(out, d);
			closeSream(out);
			ResponseUtil.write(responseWrite("请输入密码!",true));
			return;
		}else{
			byte d[] = (userid+"获取手机验证码"+",传递参数说明:"+"登陆ID:"+userid+",密码:"+txtPWD+",是否开启手机验证码:"+isShowYZMorNot+",图片验证码:"+identifyCode+";系统时间:"+getSystemtime()+";执行步骤说明:"+"判断用户名、密码是否为空(都为空)\r\n\r\n").getBytes() ;
			streamWrite(out, d);
			closeSream(out);
			ResponseUtil.write(responseWrite("请输入用户名和密码!",true));    			
		}
		closeSream(out);
    }
    private BufferedOutputStream openStream(HttpServletRequest request){
    	BufferedOutputStream out = null;
    	String isMobileLog = ConfigUtil.readAllProperties(CN_FILENAME).get("isMobileLog");
    	if(isMobileLog==null||(isMobileLog!=null&&!isMobileLog.equals("0"))){
    	String rootpath = request.getSession().getServletContext().getRealPath("/");
		File file = new File(rootpath+"\\iwork_log");
		file.mkdirs();
		file = new File(rootpath+"\\iwork_log\\getmobileyzm.txt");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (Exception e) {
				
			}
		}
		try{
			out= new BufferedOutputStream(new FileOutputStream(file, true));
		} catch (Exception e) {
			
		}
    	}
		return out;
    }
	private String getSystemtime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	private void streamWrite(BufferedOutputStream out, byte[] b) {
		try {
			if(out!=null)
				out.write(b);
		} catch (Exception e) {
			
		}
	}
	private void closeSream(BufferedOutputStream out) {
		try {
			if(out!=null)
				out.close();
		} catch (Exception e) {

		}
	}

    private  String getIpAdrress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if(index != -1){
                return XFor.substring(0,index);
            }else{
                return XFor;
            }
        }
        XFor = Xip;
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }
    private void loginerrlog(String type,String userid){
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response=ServletActionContext.getResponse();
        String url=request.getServletContext().getRealPath("/");
        File file=new File(url+"/"+"loginERR");
        if(!file.exists()){
            file.mkdirs();
        }
        String separator = File.separator;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String wjm=df.format(new Date());
        File execlFile=new File(url+separator+"loginERR"+separator+wjm+".xls");
        String filePath=url+separator+"loginERR/";
        if(!execlFile.exists()){
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("登录异常日志");
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow((int) 0);

            HSSFFont headfont = wb.createFont();
            headfont.setFontName("宋体");
            headfont.setFontHeightInPoints((short) 11);// 字体大小
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            style.setBorderBottom((short) 1);
            style.setBorderLeft((short) 1);
            style.setBorderRight((short) 1);
            style.setBorderTop((short) 1);
            style.setWrapText(true);
            HSSFCellStyle style2 = wb.createCellStyle();
            style2.setBorderBottom((short) 1);
            style2.setBorderLeft((short) 1);
            style2.setBorderRight((short) 1);
            style2.setBorderTop((short) 1);
            style2.setWrapText(true);
            style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            HSSFFont font2 = wb.createFont();
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font2);
            // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
            List<HashMap> list = new ArrayList<HashMap>();
            List<HashMap> list1 = new ArrayList<HashMap>();
            int m=0;
            int z=0;
            HSSFRow row1 = sheet.createRow((int) m++);
            HSSFCell cell1 = row1.createCell((short) z++);cell1.setCellValue("登录账号");cell1.setCellStyle(style);
            cell1 = row1.createCell((short) z++);cell1.setCellValue("IP");cell1.setCellStyle(style);
            cell1 = row1.createCell((short) z++);cell1.setCellValue("时间");cell1.setCellStyle(style);
            cell1 = row1.createCell((short) z++);cell1.setCellValue("出错原因");cell1.setCellStyle(style);
            z=0;
            row1 = sheet.createRow((int) 1);
            HSSFCell cell2 = row1.createCell((short) z++);
            try {
                cell2.setCellValue(URLDecoder.decode(userid ,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            cell2 = row1.createCell((short) z++);cell2.setCellValue(getIpAdrress(request));
            cell2 = row1.createCell((short) z++);cell2.setCellValue(sdf.format(new Date()));
            cell2 = row1.createCell((short) z++);cell2.setCellValue(type);

            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 6000);
            sheet.setColumnWidth(2, 6000);
            sheet.setColumnWidth(3, 7000);

            File fst = new File(filePath);
            if(!fst.exists()){
                fst.mkdirs();
            }
            File fs = new File(filePath+wjm+".xls");
            FileOutputStream out=null;
            try {
                out = new FileOutputStream(fs);
                wb.write(out);
            } catch (Exception e) {
            }finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            try {
                FileInputStream fs=new FileInputStream(filePath+wjm+".xls");  //获取d://test.xls
                POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息
                HSSFWorkbook wb=new HSSFWorkbook(ps);
                HSSFSheet sheet=wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表
                HSSFRow row=sheet.getRow(0);  //获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值

                HSSFCellStyle style2 = wb.createCellStyle();
                style2.setBorderBottom((short) 1);
                style2.setBorderLeft((short) 1);
                style2.setBorderRight((short) 1);
                style2.setBorderTop((short) 1);
                style2.setWrapText(true);
                style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

                FileOutputStream out=new FileOutputStream(filePath+wjm+".xls");  //向d://test.xls中写数据
                row=sheet.createRow((short)(sheet.getLastRowNum()+1)); //在现有行号后追加数据
                row.createCell(0).setCellValue(java.net.URLDecoder.decode(userid ,"UTF-8")); //设置第一个（从0开始）单元格的数据
                row.createCell(1).setCellValue(getIpAdrress(request)); //设置第二个（从0开始）单元格的数据
                row.createCell(2).setCellValue(sdf.format(new Date())); //设置第二个（从0开始）单元格的数据
                row.createCell(3).setCellValue(type); //设置第二个（从0开始）单元格的数据




                out.flush();
                wb.write(out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	public String responseWrite(String info,boolean flag){
		HashMap returnMap = new HashMap();
		returnMap.put("INFO", info);
		returnMap.put("flag", flag);
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
    
//========================POJO==============================================
	public OrgCompanyService getOrgCompanyService() {
		return orgCompanyService;
	}


	public void setOrgCompanyService(OrgCompanyService orgCompanyService) {
		this.orgCompanyService = orgCompanyService;
	}


	public OrgDepartmentService getOrgdepartmentService() {
		return orgdepartmentService;
	}


	public void setOrgdepartmentService(OrgDepartmentService orgdepartmentService) {
		this.orgdepartmentService = orgdepartmentService;
	}


	public OrgUserService getOrgUserService() {
		return orgUserService;
	}


	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTxtPWD() {
		return txtPWD;
	}
	public void setTxtPWD(String txtPWD) {
		this.txtPWD = txtPWD;
	}
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getParam1() {
		return param1;
	}


	public void setParam1(String param1) {
		this.param1 = param1;
	}


	public String getParam2() {
		return param2;
	}


	public void setParam2(String param2) {
		this.param2 = param2;
	}


	public LoginService getLoginService() {
		return loginService;
	}


	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}


	public String getLoginInfo() {
		return loginInfo;
	}


	public void setLoginInfo(String loginInfo) {
		this.loginInfo = loginInfo;
	}
	public UserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}
	public String getRememberName() {
		return rememberName;
	}
	public void setRememberName(String rememberName) {
		this.rememberName = rememberName;
	}
	public String getRememberPwd() {
		return rememberPwd;
	}
	public void setRememberPwd(String rememberPwd) {
		this.rememberPwd = rememberPwd;
	}
	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getIdentifyCode() {
		return identifyCode;
	}
	public void setIdentifyCode(String identifyCode) {
		this.identifyCode = identifyCode;
	}
	public List<UserContext> getUserOnlineList() {
		return userOnlineList;
	}
	public void setUserOnlineList(List<UserContext> userOnlineList) {
		this.userOnlineList = userOnlineList;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getRedirectURL() {
		return redirectURL;
	}
	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}
	public String getAutoLogin() {
		return autoLogin;
	}
	public void setAutoLogin(String autoLogin) {
		this.autoLogin = autoLogin;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSmsNum() {
		return smsNum;
	}
	public void setSmsNum(String smsNum) {
		this.smsNum = smsNum;
	}
	public String getLoginMode() {
		return loginMode;
	}
	public void setLoginMode(String loginMode) {
		this.loginMode = loginMode;
	}
	public String getLoginModeTest() {
		return loginModeTest;
	}
	public void setLoginModeTest(String loginModeTest) {
		this.loginModeTest = loginMode;
	}
	public String getLoginInfo2() {
		return loginInfo2;
	}
	public void setLoginInfo2(String loginInfo2) {
		this.loginInfo2 = loginInfo2;
	}
	public String getIsShowYZMorNot() {
		return isShowYZMorNot;
	}
	public void setIsShowYZMorNot(String isShowYZMorNot) {
		this.isShowYZMorNot = isShowYZMorNot;
	}
	public String getSchma() {
		return schma;
	}
	public void setSchma(String schma) {
		this.schma = schma;
	}
	public String getUseridlocal() {
		return useridlocal;
	}
	public void setUseridlocal(String useridlocal) {
		this.useridlocal = useridlocal;
	}
	public String getPasswordlocal() {
		return passwordlocal;
	}
	public void setPasswordlocal(String passwordlocal) {
		this.passwordlocal = passwordlocal;
	}
	public String getIdentifyCodelocal() {
		return identifyCodelocal;
	}
	public void setIdentifyCodelocal(String identifyCodelocal) {
		this.identifyCodelocal = identifyCodelocal;
	}
	public String getSchmalocal() {
		return schmalocal;
	}
	public void setSchmalocal(String schmalocal) {
		this.schmalocal = schmalocal;
	}
	
	
}