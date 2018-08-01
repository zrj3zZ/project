package com.iwork.webservice.interceptor;

import java.io.InputStream;
import java.util.Hashtable;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import com.iwork.webservice.constants.WebServiceConstants;
import com.iwork.webservice.model.SysWsBaseinfo;
import com.iwork.webservice.util.WebServiceUtil;

public class UserPWDInInterceptor extends AbstractPhaseInterceptor<Message> {

	//private WebServiceService webServiceService;
	//private WebServiceRuntimeService webServiceRuntimeService;
	
	public UserPWDInInterceptor() {
		super(Phase.PRE_PROTOCOL);

		//webServiceService = (WebServiceService)SpringBeanUtil.getBean("webServiceService");
		//webServiceRuntimeService = (WebServiceRuntimeService)SpringBeanUtil.getBean("webServiceRuntimeService");
	}

	public void handleMessage(Message message) throws Fault {
		// 判断soap内容的正确与否
		Hashtable<String,String> hashtable = WebServiceUtil.getDataHashtable(WebServiceUtil.getWebServiceMessageContent(message));
		if(null == hashtable){
			throw new Fault(new IllegalAccessException(WebServiceConstants.CONST_MESSAGE_ERROR_101));
		}
				
		String host = WebServiceUtil.getWebServiceHost(message);
		
		String url = WebServiceUtil.getWebServiceURL(message);
		
		// soap内容
		String content = ((InputStream) message.getContent(InputStream.class)).toString();

		// 根据传输数据，获取对此webservice的相关配置
		SysWsBaseinfo sysWsBaseinfo = WebServiceUtil.getSysWsBaseinfo(message);
		if(null == sysWsBaseinfo || 0 == sysWsBaseinfo.getId()){
			throw new Fault(new IllegalAccessException(WebServiceConstants.CONST_MESSAGE_ERROR_102));
		}
				
		// 是否需要用户名密码校验
		boolean isNeedCheck = false;
		String checkType = sysWsBaseinfo.getCheckType();
		if(null != checkType && !"".equals(checkType)){
			String[] checkTypeArr = checkType.split(",");
			for(String item:checkTypeArr){
				if(WebServiceConstants.WS_CHECK_TYPE_PWD.equals(item)){
					isNeedCheck = true;
					break;
				}
			}
		}else{
			
		}
		
		if(isNeedCheck){
			// 需要校验
			// 传来的用户名密码
			String userName = hashtable.get(WebServiceConstants.KEY_USER_NAME);
			userName = WebServiceUtil.notNull(userName);
			String userPwd = hashtable.get(WebServiceConstants.KEY_USER_PWD);
			userPwd = WebServiceUtil.notNull(userPwd);
			
			// 校验用户名密码
			String usernameStr = sysWsBaseinfo.getUsername();
			if(null == usernameStr){
				usernameStr = "";
			};
			String passwordStr = sysWsBaseinfo.getPassword();
			if(null == passwordStr){
				passwordStr = "";
			};
			
			String[] usernameStrArr = usernameStr.split(",");
			String[] passwordStrArr = passwordStr.split(",");
			
			boolean validation = false;
			for(int i = 0; i < usernameStrArr.length; i++){
				if(userName.equals(usernameStrArr[i])
						&& userPwd.equals(passwordStrArr[i])){
					
					validation = true;
					break;
				}
			}
			
			if(!validation){
				throw new Fault(new IllegalAccessException(WebServiceConstants.CONST_MESSAGE_ERROR_105+" 用户名:" + userName + " 密码:" + userPwd));
			}
		}	
	}
}
