package com.iwork.webservice.interceptor;

import java.util.Hashtable;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import com.iwork.webservice.constants.WebServiceConstants;
import com.iwork.webservice.model.SysWsBaseinfo;
import com.iwork.webservice.util.WebServiceUtil;

/**
 * IP地址拦截器
 * 可在filter.xml文件中配置允许和拒绝访问的IP地址
 * @author Sunshine
 *
 */
public class IpAddressInInterceptor extends AbstractPhaseInterceptor<Message> {

	public IpAddressInInterceptor() {
		super(Phase.RECEIVE);
	}

	public void handleMessage(Message message) throws Fault {
		// 判断soap内容的正确与否
		Hashtable<String,String> hashtable = WebServiceUtil.getDataHashtable(WebServiceUtil.getWebServiceMessageContent(message));
		if(null == hashtable){
			throw new Fault(new IllegalAccessException(WebServiceConstants.CONST_MESSAGE_ERROR_101));
		}
		
		// 取客户端IP地址
		String remoteAddr = WebServiceUtil.getWebServiceRemoteAddr(message);
		
		// 根据传输数据，获取对此webservice的相关配置
		SysWsBaseinfo sysWsBaseinfo = WebServiceUtil.getSysWsBaseinfo(message);
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
			// 需要IP校验
			// 先处理拒绝访问的地址
			String forbidIp = sysWsBaseinfo.getForbidIp();
			if(null == forbidIp){
				forbidIp = "";
			}
			String[] forbidIpArr = forbidIp.split(",");
			for (String item : forbidIpArr) {
				if (remoteAddr.equals(item)) {
					throw new Fault(new IllegalAccessException(remoteAddr + "："+WebServiceConstants.CONST_MESSAGE_ERROR_104));
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
					if (item.equals(remoteAddr)) {
						contains = true;
						break;
					}
				}
				if (!contains) {
					throw new Fault(new IllegalAccessException(remoteAddr + "："+WebServiceConstants.CONST_MESSAGE_ERROR_103));
				}
			}
		}
	}
}

