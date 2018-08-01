

package com.iwork.app.login.control;
import org.apache.log4j.Logger;
import java.lang.reflect.Constructor;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.login.constant.LoginConst;
import com.iwork.commons.ClassReflect;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
/**
 * 平台登录身份验证控制
 * 
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class LoginControl{
	private static Logger logger = Logger.getLogger(LoginControl.class);
    private String userid;

    private static LoginInterface _loingAdapter;


    /**
     * 用户ID值
     */
    private String _userId;

    /**
     * 密码
     */
    private String _MD5Pwd;

    /**
     * 密码
     */
    private String _pwd;

    /**
     * 用户IP地址
     */
    private String _bIp;
    
    /**
     * 伙伴登陆扩展参数1
     */
    private String _param1;
    /**
     * 伙伴登陆扩展参数2
     */
    private String _param2;


	public String getUserId() {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * 构造一个实例
     * 
     * @param userId
     *            用户帐户名称
     * @param pwd
     *            String 口令
     * @param bIp
     *            String 用户的IP地址
     * @preserve 声明此方法不被JOC混淆
     */
    public LoginControl(LoginContext loginContext) {
        this._pwd = loginContext.getPwd();
        this._userId = loginContext.getUid();
        this._param1 = loginContext.getParam1();
        this._param2=loginContext.getParam2();
        this._bIp = loginContext.getIp();
		this._MD5Pwd = loginContext.getMD5Pwd();
    }
    /**
     * 验证用户身份
     * 
     * @return 型验证结果
     * @preserve 声明此方法不被JOC混淆
     */
    public int check() {
        //失败次数后被自动冻结
        int pauseTime = Integer.parseInt(SystemConfig._iworkServerConf.getLogincont());

            //获取登录身份识别的Adapter
            if (_loingAdapter == null)
                _loingAdapter = getLoginAdapter();
            LoginContext context = new LoginContext();
            context.setIp(this._bIp);
            context.setMD5Pwd(this._MD5Pwd);
            context.setPwd(this._pwd);
            context.setUid(this._userId);
            
            int loginStatus = _loingAdapter.login(context);
            if (loginStatus != LoginConst.LOGIN_STATUS_OK) {//调用身份验证的Adapter类接口
                userid = _loingAdapter.getUserId();
                return loginStatus; 
            } else {
            	//判断用户名是否存在  
            	UserContext uc = UserContextUtil.getInstance().getUserContext(context.getUid());
            	if(uc!=null){
            		return LoginConst.LOGIN_STATUS_OK;
            	}else{ 
            		return LoginConst.LOGIN_STATUS_USER_NOTFIND;
            	}
            }
    }

    private LoginInterface getLoginAdapter() {
        String className = SystemConfig._iworkServerConf.getLoginClassAdapter();
        Constructor _cons = null;
        Class[] parameterTypes = {};
        try {
            _cons = ClassReflect.getConstructor(className, parameterTypes);
            if (_cons != null) {
                Object[] params = {};
                return (LoginInterface) _cons.newInstance(params);
            }
        }
        catch (Exception e) {
			logger.error(e,e);
		}
        return null;
    }
}
