package com.iwork.webservice.interceptor;

import java.util.Hashtable;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import com.iwork.core.util.SpringBeanUtil;
import com.iwork.webservice.constants.WebServiceConstants;
import com.iwork.webservice.model.SysWsBaseinfo;
import com.iwork.webservice.service.WebServiceRuntimeService;
import com.iwork.webservice.service.WebServiceService;
import com.iwork.webservice.util.WebServiceUtil;

public class HeaderUserPWDInInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

	public HeaderUserPWDInInterceptor() {
		super(Phase.PRE_PROTOCOL);
	}

	public void handleMessage(SoapMessage message) throws Fault {

		WebServiceService webServiceService = (WebServiceService)SpringBeanUtil.getBean("webServiceService");
		WebServiceRuntimeService webServiceRuntimeService = (WebServiceRuntimeService)SpringBeanUtil.getBean("webServiceRuntimeService");
	
		Hashtable headerHashtable = WebServiceUtil.getSoapHeaderHashtable(message);
		
		String UUID = (String)headerHashtable.get(WebServiceConstants.KEY_UUID);
		if(WebServiceUtil.stringIsEmpty(UUID)){
			throw new Fault(new IllegalAccessException(WebServiceConstants.CONST_MESSAGE_ERROR_110));
		}
		
		SysWsBaseinfo sysWsBaseinfo = (SysWsBaseinfo)webServiceService.getModelByUUID(UUID);
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
		}

		// 需要校验
		if(isNeedCheck){
			String userName = (String)headerHashtable.get(WebServiceConstants.KEY_USER_NAME);
			if(WebServiceUtil.stringIsEmpty(userName)){
				throw new Fault(new IllegalAccessException(WebServiceConstants.CONST_MESSAGE_ERROR_111));
			}
			
			String userPwd = (String)headerHashtable.get(WebServiceConstants.KEY_USER_PWD);
			if(WebServiceUtil.stringIsEmpty(userPwd)){
				throw new Fault(new IllegalAccessException(WebServiceConstants.CONST_MESSAGE_ERROR_112));
			}
			
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
