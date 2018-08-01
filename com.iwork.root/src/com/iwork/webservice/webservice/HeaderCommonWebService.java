package com.iwork.webservice.webservice;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import com.iwork.commons.util.UUIDUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.webservice.cache.WebServiceCacheManager;
import com.iwork.webservice.constants.WebServiceConstants;
import com.iwork.webservice.model.SysWsBaseinfo;
import com.iwork.webservice.model.SysWsParams;
import com.iwork.webservice.model.SysWsRuLog;
import com.iwork.webservice.model.SysWsRuParams;
import com.iwork.webservice.service.WebServiceRuntimeService;
import com.iwork.webservice.service.WebServiceService;
import com.iwork.webservice.util.WebServiceUtil;
import org.apache.log4j.Logger;


/**
 * 通用WebService
 * @author lmyanglei@gmail.com
 *
 */
	@WebService
	public class HeaderCommonWebService{ 
		private static Logger logger = Logger.getLogger(HeaderCommonWebService.class);
		private WebServiceService webServiceService;
		private WebServiceRuntimeService webServiceRuntimeService;
		
		private StringBuffer logInfo;

		@WebMethod
		public	String execute(String contentParam) {
			WebServiceContext context = new WebServiceContextImpl();
			MessageContext ctx = context.getMessageContext();
			HttpServletRequest request = (HttpServletRequest)ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
			String IP = request.getRemoteAddr();
			StringBuffer URL = request.getRequestURL();
			String URI = request.getRequestURI();
			
			//  取完整的soap内容
			Hashtable headerHashtable = new Hashtable<String,String>();
			WrappedMessageContext wmctx = (WrappedMessageContext)ctx;
			MessageImpl messageImpl = (MessageImpl)wmctx.getWrappedMessage();
			java.io.InputStream inputStream = messageImpl.getContent(java.io.InputStream.class);
			String content = inputStream.toString();
			
			org.dom4j.Document contentDocument = null;
			try {
				contentDocument = org.dom4j.DocumentHelper.parseText(content);
				
				Element contentRoot = contentDocument.getRootElement();
				List<Element> rootSubElememts = contentRoot.elements();
				for(Element rootSubEmelent:rootSubElememts){
					String name = rootSubEmelent.getName();
					if("Header".equals(name)){
						List<Element> headerSubElememts = rootSubEmelent.elements();
						for(Element headerSubEmelent:headerSubElememts){
							String key = headerSubEmelent.getName();
							String value = headerSubEmelent.getTextTrim();
							headerHashtable.put(key, value);
						}
					}
				}
				
				if(null == headerHashtable || headerHashtable.size() == 0){
					logInfo.append(WebServiceConstants.CONST_MESSAGE_ERROR_113);
					return logInfo.toString();
				}
			} catch (DocumentException e) {
				logger.error(e,e);
				logInfo.append(WebServiceConstants.CONST_MESSAGE_ERROR_113);
				return logInfo.toString();
			}
			
			
			// 业务处理
			webServiceService = (WebServiceService)SpringBeanUtil.getBean("webServiceService");
			webServiceRuntimeService = (WebServiceRuntimeService)SpringBeanUtil.getBean("webServiceRuntimeService");
			
			logInfo = new StringBuffer(); 
			long beginTime = System.currentTimeMillis();
			
			boolean isJSON = WebServiceUtil.isJSONString(contentParam);

			Hashtable dataHashtable = new Hashtable();
			if(isJSON){
				dataHashtable = WebServiceUtil.getDataHashtableByJSON(contentParam);
			}else{
				dataHashtable = WebServiceUtil.getDataHashtableByXML(contentParam);
			}
			
			if(null == dataHashtable){
				logInfo.append(WebServiceConstants.CONST_MESSAGE_ERROR_106);
				return logInfo.toString();
			}
			
//			dataHashtable.put(WebServiceConstants.KEY_USER_NAME, userNameParam);
//			dataHashtable.put(WebServiceConstants.KEY_USER_PWD, userPwdParam);
//			dataHashtable.put(WebServiceConstants.KEY_UUID, UUIDParam);
			
			// CommonHander
			// execute
			String UUID = (String)headerHashtable.get(WebServiceConstants.KEY_UUID);
			if(WebServiceUtil.stringIsEmpty(UUID)){
				logInfo.append(WebServiceConstants.CONST_MESSAGE_ERROR_102);
				return logInfo.toString();
			}
			
			SysWsBaseinfo sysWsBaseinfo =(SysWsBaseinfo)webServiceService.getModelByUUID(UUID);
			
			if(null == sysWsBaseinfo){
				logInfo.append(WebServiceConstants.CONST_MESSAGE_ERROR_102);
				return logInfo.toString();
			}
			
			if(0 == sysWsBaseinfo.getStatus()){
				logInfo.append(WebServiceConstants.CONST_MESSAGE_ERROR_109);
				return logInfo.toString();
			}
			
			StringBuffer cachekey = new StringBuffer("");
			
			// 记录输入日志
			String checkParam = "";
			
			int status = WebServiceConstants.SUCCESS;
			SysWsRuLog log = new SysWsRuLog();
			log.setPid(sysWsBaseinfo.getId());
			log.setUuid(UUIDUtil.getUUID());
			log.setCreatedate(new Date());
			log.setStatus(status);
			webServiceRuntimeService.saveOrUpdateLogInfo(log);
			
			List<SysWsParams> inputParamList = webServiceService.getInputParamsList(sysWsBaseinfo.getId());
			for(int i = 0 ; i < inputParamList.size(); i++){
				SysWsParams param = inputParamList.get(i);
				
				if(WebServiceConstants.WS_FIELD_TYPE_STRUCTURE.equals(param.getType())){
					
					List<SysWsParams> inputParamListSec = webServiceService.getInputParamsList(param.getId());
					Hashtable dataHashtableSec = (Hashtable)dataHashtable.get(param.getName());
					
					// 校验参数
					if(null != param && "1".equals(param.getRequired()) ){
						if(null == dataHashtableSec || dataHashtableSec.size() == 0){
							checkParam += param.getName() + ";";
						}
					}
					
					for(int j = 0 ; j < inputParamListSec.size(); j++){
						SysWsParams paramSec = inputParamListSec.get(j);
						
						SysWsRuParams ruParam = new SysWsRuParams();
						ruParam.setInOrOut(WebServiceConstants.WS_PARAMS_TYPE_INPUT);
						ruParam.setLogId(log.getId());
						ruParam.setUuid(UUIDUtil.getUUID());
						ruParam.setTitle(paramSec.getTitle());
						ruParam.setName(paramSec.getName());
						ruParam.setType(paramSec.getType());
						
						// 客户端传来的值
						String value = (String)dataHashtableSec.get(paramSec.getName());
						ruParam.setValue(value);
						
						// 校验参数
						checkParam += WebServiceUtil.checkParam(paramSec, value);
						
						webServiceRuntimeService.saveLogParams(ruParam);
						
						// cache参数
						cachekey.append(ruParam.getName()).append(ruParam.getValue());
					}
				}else{
					SysWsRuParams ruParam = new SysWsRuParams();
					ruParam.setInOrOut(WebServiceConstants.WS_PARAMS_TYPE_INPUT);
					ruParam.setLogId(log.getId());
					ruParam.setUuid(UUIDUtil.getUUID());
					ruParam.setTitle(param.getTitle());
					ruParam.setName(param.getName());
					ruParam.setType(param.getType());
					
					// 客户端传来的值
					String value = (String)dataHashtable.get(param.getName());
					ruParam.setValue(value);
					
					// 校验参数
					checkParam += WebServiceUtil.checkParam(param, value);
					
					webServiceRuntimeService.saveLogParams(ruParam);
					
					// cache参数
					cachekey.append(ruParam.getName()).append(ruParam.getValue());
				}
			}
			
			if(WebServiceUtil.stringIsNotEmpty(checkParam)){
				checkParam = WebServiceConstants.CONST_MESSAGE_ERROR_108 + checkParam;
				logInfo.append(checkParam);
				log.setLoginfo(logInfo.toString());
				
				status = WebServiceConstants.ERROR;
				log.setStatus(status);
				
				webServiceRuntimeService.saveOrUpdateLogInfo(log);
				
				return logInfo.toString();
			}
			
			
			// 处理业务
			String result = null;
			
			// 读取缓存
			if(null != sysWsBaseinfo && WebServiceConstants.YES == sysWsBaseinfo.getIsCache()){
				result = (String)WebServiceCacheManager.getInstance(sysWsBaseinfo.getUuid(), sysWsBaseinfo.getCacheTime()).getOutputParams(cachekey.toString());
			}
			
			if(null == result){
				// 没有缓存则执行业务处理
				// 调用触发器
				String handerName = sysWsBaseinfo.getName();
				if(WebServiceUtil.stringIsEmpty(handerName)){
					logInfo.append(WebServiceConstants.CONST_MESSAGE_ERROR_107); 
					log.setLoginfo(logInfo.toString());
					
					status = WebServiceConstants.ERROR;
					log.setStatus(status);
					
					webServiceRuntimeService.saveOrUpdateLogInfo(log);
					
					return logInfo.toString();
				}

				Class handerClass = null;
				try {
					handerClass = Class.forName(handerName);
				} catch (ClassNotFoundException e) {
					logger.error(e,e);
				}
				
				WebServiceHanderAbstract hander;				
				try {
					hander = (WebServiceHanderAbstract)handerClass.newInstance();				
					result = hander.execute(contentParam);
				} catch (Exception e) {
					logger.error(e,e);
					
					logInfo.append(WebServiceConstants.CONST_MESSAGE_ERROR_107+contentParam);
					log.setLoginfo(logInfo.toString());
					
					status = WebServiceConstants.ERROR;
					log.setStatus(status);
					webServiceRuntimeService.saveOrUpdateLogInfo(log);
					
					return logInfo.toString();
					
				}// 处理完成
				
				// 装载缓存
				if(null != sysWsBaseinfo && WebServiceConstants.YES == sysWsBaseinfo.getIsCache()){
					WebServiceCacheManager.getInstance(sysWsBaseinfo.getUuid(), sysWsBaseinfo.getCacheTime()).putInputParams(cachekey.toString(),result);
				}
			}
			
			// 记录输出日志
			long endTime = System.currentTimeMillis();
			log.setShowtime(endTime - beginTime);
			status = WebServiceConstants.SUCCESS;
			log.setStatus(status);
			webServiceRuntimeService.saveOrUpdateLogInfo(log);
			
			// 记录输出日志
			if(WebServiceUtil.stringIsEmpty(result)){
				return result;
			}
			
			dataHashtable = new Hashtable();
			isJSON = WebServiceUtil.isJSONString(result);
			if(isJSON){
				dataHashtable = WebServiceUtil.getDataHashtableByJSON(result);
			}else{
				dataHashtable = WebServiceUtil.getDataHashtableByXML(result);
			}
			
			log.setPid(sysWsBaseinfo.getId());
			log.setUuid(UUIDUtil.getUUID());
			log.setCreatedate(new Date());
			log.setStatus(status);
			webServiceRuntimeService.saveOrUpdateLogInfo(log);
			
			List<SysWsParams> outputParamList = webServiceService.getOutputParamsList(sysWsBaseinfo.getId());
			for(int i = 0 ; i < outputParamList.size(); i++){
				SysWsParams param = outputParamList.get(i);
				
				if(WebServiceConstants.WS_FIELD_TYPE_STRUCTURE.equals(param.getType())){
					List<SysWsParams> paramListSec = webServiceService.getOutputParamsList(param.getId());
					Hashtable dataHashtableSec = (Hashtable)dataHashtable.get(param.getName());
					for(int j = 0 ; j < paramListSec.size(); j++){
						SysWsParams paramSec = paramListSec.get(j);
						
						SysWsRuParams ruParam = new SysWsRuParams();
						ruParam.setInOrOut(WebServiceConstants.WS_PARAMS_TYPE_OUTPUT);
						ruParam.setLogId(log.getId());
						ruParam.setUuid(UUIDUtil.getUUID());
						ruParam.setTitle(paramSec.getTitle());
						ruParam.setName(paramSec.getName());
						ruParam.setType(paramSec.getType());
						
						// 客户端传来的值
						String value = (String)dataHashtableSec.get(paramSec.getName());
						ruParam.setValue(value);
						
						webServiceRuntimeService.saveLogParams(ruParam);
					}
				}else{
					SysWsRuParams ruParam = new SysWsRuParams();
					ruParam.setInOrOut(WebServiceConstants.WS_PARAMS_TYPE_OUTPUT);
					ruParam.setLogId(log.getId());
					ruParam.setUuid(UUIDUtil.getUUID());
					ruParam.setTitle(param.getTitle());
					ruParam.setName(param.getName());
					ruParam.setType(param.getType());
					
					// 客户端传来的值
					String value = (String)dataHashtable.get(param.getName());
					ruParam.setValue(value);					
					webServiceRuntimeService.saveLogParams(ruParam);
				}
			}
			
			return result;
			
		}		
	}