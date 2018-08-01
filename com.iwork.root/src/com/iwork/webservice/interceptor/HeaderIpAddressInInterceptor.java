package com.iwork.webservice.interceptor;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import com.iwork.core.util.SpringBeanUtil;
import com.iwork.webservice.constants.WebServiceConstants;
import com.iwork.webservice.model.SysWsBaseinfo;
import com.iwork.webservice.service.WebServiceRuntimeService;
import com.iwork.webservice.service.WebServiceService;
import com.iwork.webservice.util.WebServiceUtil;

public class HeaderIpAddressInInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

	public HeaderIpAddressInInterceptor() {
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
		
		// 是否需要IP校验
		boolean isNeedCheckIp = false;
		String checkType = sysWsBaseinfo.getCheckType();
		if(null != checkType && !"".equals(checkType)){
			String[] checkTypeArr = checkType.split(",");
			for(String item:checkTypeArr){
				if(WebServiceConstants.WS_CHECK_TYPE_IP.equals(item)){
					isNeedCheckIp = true;
					break;
				}
			}
		}

		if(isNeedCheckIp){
			HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
			
			// 取客户端IP地址
			String ipAddress = "";
			if(request!=null){
				ipAddress = request.getRemoteAddr(); 
			}
			
			// 先处理拒绝访问的地址
			String forbidIp = sysWsBaseinfo.getForbidIp();
			if(null == forbidIp){
				forbidIp = "";
			}
			String[] forbidIpArr = forbidIp.split(",");
			for (String item : forbidIpArr) {
				if (ipAddress.equals(item)) {
					throw new Fault(new IllegalAccessException(ipAddress + "："+WebServiceConstants.CONST_MESSAGE_ERROR_104));
				}
			}
			
			// 如果允许访问的集合非空，继续处理，否则认为全部IP地址均合法
			String permitIp = sysWsBaseinfo.getPermitIp();
			if(null == permitIp){
				permitIp = "";
			}
			String[] permitIpArr = permitIp.split(",");
			if (permitIpArr.length > 0) {
				boolean contains = false;
				for (String item : permitIpArr) {
					if (item.equals(ipAddress)) {
						contains = true;
						break;
					}
				}
				if (!contains) {
					throw new Fault(new IllegalAccessException(ipAddress + "："+WebServiceConstants.CONST_MESSAGE_ERROR_103));
				}
			}
		}
	}
}
