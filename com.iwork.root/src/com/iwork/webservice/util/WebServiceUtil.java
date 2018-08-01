package com.iwork.webservice.util;

import java.io.CharArrayReader;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.dom4j.DocumentException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.iwork.core.util.SpringBeanUtil;
import com.iwork.webservice.constants.WebServiceConstants;
import com.iwork.webservice.model.SysWsBaseinfo;
import com.iwork.webservice.model.SysWsParams;
import com.iwork.webservice.service.WebServiceRuntimeService;
import com.iwork.webservice.service.WebServiceService;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**  
 * @Project kingoa-root-src
 * @Title WebServiceUtil.java
 * @Package com.iwork.webservice.util
 * @Description TODO
 * @author yanglei6 yanglei6@kingsoft.com,lmyanglei@gmail.com
 * @date 2013-11-27 下午6:05:17
 * @Copyright 2013 www.kingsoft.com All rights reserved.
 * @version v1.0  
 */

public class WebServiceUtil {
	private static Logger logger = Logger.getLogger(WebServiceUtil.class);
	/**
	 * 根据触发器code，获取触发器名称
	 * @param handerCode
	 * @return
	 */
	public static String getHanderName(String handerCode){
		String returnValue = "";
		
		Hashtable<String,String> hashtable = getHanderHashtable();
		
		returnValue = hashtable.get(handerCode);
		
		return returnValue;
	}
	
	/**
	 * 触发器信息
	 * @param handerCode
	 * @return
	 */
	public static Hashtable<String,String> getHanderHashtable(){
		Hashtable<String,String> hashtable = new Hashtable<String,String>();
		
		hashtable.put("1", "com.iwork.webservice.webservice.CommonWebServiceHander");
		hashtable.put("2", "com.iwork.webservice.webservice.DemoWebServiceHander");
		
		return hashtable;
	}
	
	/**
	 * 字符串是否为空
	 * @param str 字符串
	 * @return 为空返回true
	 */
	public static boolean stringIsEmpty(String str){
		return (null == str || "".equals(str));
	}
	
	/**
	 * 字符串是否不为空
	 * @param str 字符串
	 * @return 不为空返回true
	 */
	public static boolean stringIsNotEmpty(String str){
		return !stringIsEmpty(str);
	}
	
	/**
	 * 从JSONObject的list中，找到key对应的value
	 * @param listJSONObject
	 * @param key
	 * @return
	 */
	public static String getJOSNValue(List<JSONObject> listJSONObject,String key){
		String returnValue = "";
		for(int j = 0; j < listJSONObject.size(); j ++){
			JSONObject itemJson = listJSONObject.get(j);
			String tempValue = (String)itemJson.get(key);
			
			if(null != tempValue && !"".equals(tempValue)){
				returnValue = tempValue;
				break;
			}
		}
		return returnValue;
	}
	
	/**
	 * 判断字符串是否符合JSON格式
	 * @param str
	 * @return true：符合；false：不符合
	 */
	public static boolean isJSONString(String str){
		boolean returnValue = false;

		try{
			JSONObject jsonObject = JSONObject.fromObject(str);
			returnValue = true;
		}catch(Exception e){logger.error(e,e);
			returnValue = false;
		}
		
		return returnValue;
	};
	
	/**
	 * 解析webservice客户端传来的数据，并以Hashtable格式返回
	 * 注意：这里只解析第一层和第二层，不解析>2的各层数据
	 * 
	 * @param content
	 * @return
	 */
	public static Hashtable getDataHashtable(String content){
		Hashtable<String,Object> hashtable = new Hashtable<String,Object>();
		
		// 获取参数
		org.dom4j.Document dom = null;
		try{
			dom = org.dom4j.DocumentHelper.parseText(content);
		
			org.dom4j.Element root = dom.getRootElement();
			org.dom4j.tree.DefaultElement body = (org.dom4j.tree.DefaultElement)root.element("Body");
			List<org.dom4j.tree.DefaultElement> subBodyElementList = body.elements();
			
			// 传来的是否是json数据
			boolean isJSON = false;
			for(org.dom4j.tree.DefaultElement methodElement:subBodyElementList){
				List subMethodElementList = methodElement.elements();
				
				boolean isFound = false;
				if(WebServiceConstants.WS_COMMON_WEBSERVICE_EXECUTE_NUM == subMethodElementList.size()){
					for(Object dataElement:subMethodElementList){
						String text = ((org.dom4j.tree.DefaultElement)dataElement).getText();
						isJSON = WebServiceUtil.isJSONString(text);
						if(isJSON){
							isFound = true;
							break;
						}
					}
					if(isFound){
						break;
					}
				}
				
			}
			
			if(isJSON){
				// 解析json数据
				//List<JSONObject> listJSONObject = new ArrayList<JSONObject>();
				for(org.dom4j.tree.DefaultElement methodElement:subBodyElementList){
					List subMethodElementList = methodElement.elements();
					for(Object dataElement:subMethodElementList){
						String name = ((org.dom4j.tree.DefaultElement)dataElement).getName();
						String text = ((org.dom4j.tree.DefaultElement)dataElement).getText();
	
						if("arg0".equals(name)){
							// 用户名
							hashtable.put(WebServiceConstants.KEY_USER_NAME, text);
						}else if("arg1".equals(name)){
							// 密码
							hashtable.put(WebServiceConstants.KEY_USER_PWD,text);
						}else if("arg2".equals(name)){
							// UUID
							hashtable.put(WebServiceConstants.KEY_UUID, text);
						}else if("arg3".equals(name)){
							// 客户端配置参数内容
							JSONObject jsonObject = JSONObject.fromObject(text);
							//listJSONObject.add(jsonObject);
							
							Iterator  keyIterator = jsonObject.keys();
							while (keyIterator.hasNext()) {
				                String key = (String) keyIterator.next();  
				                String value = jsonObject.getString(key);
	
				                // 是否有第二层
				                if(isJSONString(value)){
				                	
				                	Hashtable<String,String> hashtableSec = new Hashtable<String,String>();
				                	
				                	JSONObject jsonObjectSec = JSONObject.fromObject(value);
									
									Iterator  keyIteratorSec = jsonObjectSec.keys();
									while (keyIteratorSec.hasNext()) {  
						                String keySec = (String) keyIteratorSec.next();  
						                String valueSec = jsonObjectSec.getString(keySec);
	
						                hashtableSec.put(keySec, valueSec);
						            }
									
									hashtable.put(key, hashtableSec);
				                }else{
				                	hashtable.put(key, value);
				                }
				            }
						}
						
						/*
						if(WebServiceConstants.KEY_USER_NAME.equals(name)){
							// 用户名
							hashtable.put(name, text);
						}else if(WebServiceConstants.KEY_USER_PWD.equals(name)){
							// 密码
							hashtable.put(name, text);
						}else if(WebServiceConstants.KEY_UUID.equals(name)){
							// UUID
							hashtable.put(name, text);
						}else if(WebServiceConstants.KEY_PARAM_CONTENT.equals(name)){
							// 客户端配置参数内容
							JSONObject jsonObject = JSONObject.fromObject(text);
							//listJSONObject.add(jsonObject);
							
							Iterator  keyIterator = jsonObject.keys();
							while (keyIterator.hasNext()) {
				                String key = (String) keyIterator.next();  
				                String value = jsonObject.getString(key);
	
				                // 是否有第二层
				                if(isJSONString(value)){
				                	
				                	Hashtable<String,String> hashtableSec = new Hashtable<String,String>();
				                	
				                	JSONObject jsonObjectSec = JSONObject.fromObject(value);
									
									Iterator  keyIteratorSec = jsonObjectSec.keys();
									while (keyIteratorSec.hasNext()) {  
						                String keySec = (String) keyIteratorSec.next();  
						                String valueSec = jsonObjectSec.getString(keySec);
	
						                hashtableSec.put(keySec, valueSec);
						            }
									
									hashtable.put(key, hashtableSec);
				                }else{
				                	hashtable.put(key, value);
				                }
				            }
						}*/
					}
				}
			}else{
				// 解析XML数据
				for(org.dom4j.tree.DefaultElement methodElement:subBodyElementList){
					List subMethodElementList = methodElement.elements();
					for(Object dataElement:subMethodElementList){
						String name = ((org.dom4j.tree.DefaultElement)dataElement).getName();
						String text = ((org.dom4j.tree.DefaultElement)dataElement).getText();
						
						if("arg0".equals(name)){
							// 用户名
							hashtable.put(WebServiceConstants.KEY_USER_NAME, text);
						}else if("arg1".equals(name)){
							// 密码
							hashtable.put(WebServiceConstants.KEY_USER_PWD,text);
						}else if("arg2".equals(name)){
							// UUID
							hashtable.put(WebServiceConstants.KEY_UUID, text);
						}else if("arg3".equals(name)){
							// 客户端配置参数内容
							org.dom4j.Document contentDocument = null;
							try {
								contentDocument = org.dom4j.DocumentHelper.parseText(text);
							} catch (DocumentException e) {
								logger.error(e,e);
							}
							if(null != contentDocument){
								
								// 获取参数
								try {
									org.dom4j.Element contentRoot = contentDocument.getRootElement();
									List<org.dom4j.tree.DefaultElement> listContentElement = contentRoot.elements();
									
									for(Object realDataElement:listContentElement){
										String nameData = ((org.dom4j.tree.DefaultElement)realDataElement).getName();
										String textData = ((org.dom4j.tree.DefaultElement)realDataElement).getText();
	
										List contentElementList = ((org.dom4j.tree.DefaultElement)realDataElement).elements();
										if(null != contentElementList && contentElementList.size() > 0){
											Hashtable<String,String> hashtableSec = new Hashtable<String,String>();
						                	
											for(Object dataElementSec:contentElementList){
												String keySec = ((org.dom4j.tree.DefaultElement)dataElementSec).getName();
												String valueSec = ((org.dom4j.tree.DefaultElement)dataElementSec).getText();
												
												hashtableSec.put(keySec, valueSec);
											}
											
											hashtable.put(nameData, hashtableSec);
										}else{
											hashtable.put(nameData, textData);
										}
									}
									
								} catch (Exception e) {
									logger.error(e,e);
								}
							}
						}
					}
				}
			}

		}catch(Exception e){
			hashtable = null;			
			logger.error(e,e);
		}
		
		return hashtable;
	};
	
	/**
	 * 解析webservice客户端传来的数据，并以Hashtable格式返回
	 * 注意：这里只解析第一层和第二层，不解析>2的各层数据
	 * 
	 * @param content JSON格式
	 * @return
	 */
	public static Hashtable getDataHashtableByJSON(String content){
		Hashtable<String,Object> hashtable = new Hashtable<String,Object>();
		
		String text = content;
		JSONObject jsonObject = JSONObject.fromObject(text);
		
		Iterator  keyIterator = jsonObject.keys();
		while (keyIterator.hasNext()) {  
            String key = (String) keyIterator.next();  
            String value = jsonObject.getString(key);

            // 是否有第二层
            if(isJSONString(value)){
            	
            	Hashtable<String,String> hashtableSec = new Hashtable<String,String>();
            	
            	JSONObject jsonObjectSec = JSONObject.fromObject(value);
				
				Iterator  keyIteratorSec = jsonObjectSec.keys();
				while (keyIteratorSec.hasNext()) {  
	                String keySec = (String) keyIteratorSec.next();  
	                String valueSec = jsonObjectSec.getString(keySec);

	                hashtableSec.put(keySec, valueSec);
	            }
				
				hashtable.put(key, hashtableSec);
            }else{
            	hashtable.put(key, value);
            }
        }  
		
		return hashtable;
	};
	
	/**
	 * 解析webservice客户端传来的数据，并以Hashtable格式返回
	 * 注意：这里只解析第一层和第二层，不解析>2的各层数据
	 * 
	 * @param content XML格式
	 * @return
	 */
	public static Hashtable getDataHashtableByXML(String content){
		Hashtable<String,Object> hashtable = new Hashtable<String,Object>();
		
		// 获取参数
		org.dom4j.Document contentDocument = null;
		try{
			contentDocument = org.dom4j.DocumentHelper.parseText(content);
			
			org.dom4j.Element contentRoot = contentDocument.getRootElement();
			List<org.dom4j.tree.DefaultElement> listContentElement = contentRoot.elements();
			
			for(Object realDataElement:listContentElement){
				String keyData = ((org.dom4j.tree.DefaultElement)realDataElement).getName();
				String valueData = ((org.dom4j.tree.DefaultElement)realDataElement).getText();

				List contentElementList = ((org.dom4j.tree.DefaultElement)realDataElement).elements();
				if(null != contentElementList && contentElementList.size() > 0){
					Hashtable<String,String> hashtableSec = new Hashtable<String,String>();
                	
					for(Object dataElementSec:contentElementList){
						String keySec = ((org.dom4j.tree.DefaultElement)dataElementSec).getName();
						String valueSec = ((org.dom4j.tree.DefaultElement)dataElementSec).getText();
						
						hashtableSec.put(keySec, valueSec);
					}
					
					hashtable.put(keyData, hashtableSec);
				}else{
					hashtable.put(keyData, valueData);
				}
			}
			
			/*dom = org.dom4j.DocumentHelper.parseText(content);
			
			org.dom4j.Element root = dom.getRootElement();
			org.dom4j.tree.DefaultElement body = (org.dom4j.tree.DefaultElement)root.element("Body");
			List<org.dom4j.tree.DefaultElement> listBodyElement = body.elements();
			
			// 解析XML数据
			for(org.dom4j.tree.DefaultElement bodyElement:listBodyElement){
				List methodElementList = bodyElement.elements();
				for(Object dataElement:methodElementList){
					String key = ((org.dom4j.tree.DefaultElement)dataElement).getName();
					String value = ((org.dom4j.tree.DefaultElement)dataElement).getText();
					
					List contentElementList = ((org.dom4j.tree.DefaultElement)dataElement).elements();
					if(null != contentElementList && contentElementList.size() > 0){
						Hashtable<String,String> hashtableSec = new Hashtable<String,String>();
	                	
						for(Object dataElementSec:contentElementList){
							String keySec = ((org.dom4j.tree.DefaultElement)dataElementSec).getName();
							String valueSec = ((org.dom4j.tree.DefaultElement)dataElementSec).getText();
							
							hashtableSec.put(keySec, valueSec);
						}
						
						hashtable.put(key, hashtableSec);
					}else{
						hashtable.put(key, value);
					}
				}
			}*/
		}catch(Exception e){
			hashtable = null;
			logger.error(e,e);
		}
		
		return hashtable;
	};
	
	/**
	 * 根据传输数据，获取对此webservice的相关配置
	 * @param message
	 * @return
	 */
	public static SysWsBaseinfo getSysWsBaseinfo(Message message){
		WebServiceService webServiceService = (WebServiceService)SpringBeanUtil.getBean("webServiceService");
		WebServiceRuntimeService webServiceRuntimeService = (WebServiceRuntimeService)SpringBeanUtil.getBean("webServiceRuntimeService");
	
		// soap内容
		String content = getWebServiceMessageContent(message);
		
		// 解析数据
		Hashtable<String,String> hashtable = WebServiceUtil.getDataHashtable(content);

		SysWsBaseinfo sysWsBaseinfo = null;
		
		if(null != hashtable){
			// 取webservice的相关配置
			boolean isCommonWebService = false;// 是否是调用的CommonWebService
			String host = "";// 服务器IP和端口，例如：127.0.0.1:8080
			String url = "";// 服务名，例如：/services/common
			String UUID = "";// webservice管理，UUID
			HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
			url = (String) message.get(org.apache.cxf.message.Message.REQUEST_URI);
			Map<String, List<String>> reqHeaders = CastUtils.cast((Map<?,?>)message.get(Message.PROTOCOL_HEADERS)); 
			List<String> hostList = (List<String>)reqHeaders.get("host");
			if(null != hostList && hostList.size() > 0){
				host = hostList.get(0);
			}
			
			if(WebServiceConstants.COMMON_WEB_SERVICE_URL.equals(url)){
				isCommonWebService = true;
				UUID = hashtable.get(WebServiceConstants.KEY_UUID);
			}

			// webservice相关信息
			List<SysWsBaseinfo> list = new ArrayList<SysWsBaseinfo>();
			sysWsBaseinfo = new SysWsBaseinfo();
			if(isCommonWebService){
				sysWsBaseinfo = (SysWsBaseinfo)webServiceService.getModelByUUID(UUID);
			}else{
				sysWsBaseinfo = (SysWsBaseinfo)webServiceService.getModelByURI(url);
			}
		}
		
		return sysWsBaseinfo;
	}
	
	public static String checkParam(SysWsParams param,String str){
		String returnValue = "";
		
		if(null != param && "1".equals(param.getRequired()) ){
			if(WebServiceUtil.stringIsEmpty(str)){
				returnValue += param.getName() + ";";
			}
		}
		
		if(WebServiceConstants.WS_FIELD_TYPE_NUM.equals(param.getType())){
			try{
				Integer.parseInt(str);
			}catch(Exception e){
				returnValue += param.getName() + ";";
				logger.error(e,e);
			}
		}else if(WebServiceConstants.WS_FIELD_TYPE_DATE.equals(param.getType())){
			try{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sdf.parse(str);
			}catch(Exception e){
				returnValue += param.getName() + ";";
				logger.error(e,e);
			}
		}
		
		return returnValue;
	}
	
	/**
	 * 服务器IP和端口，例如：127.0.0.1:8080
	 * @param message
	 * @return
	 */
	public static String getWebServiceHost(Message message){
		String host = "";// 服务器IP和端口，例如：127.0.0.1:8080
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		Map<String, List<String>> reqHeaders = CastUtils.cast((Map<?,?>)message.get(Message.PROTOCOL_HEADERS)); 
		List<String> hostList = (List<String>)reqHeaders.get("host");
		if(null != hostList && hostList.size() > 0){
			host = hostList.get(0);
		}
		
		return host;
	}
	
	/**
	 * 客户端IP，例如：192.168.5.6
	 * @param message
	 * @return
	 */
	public static String getWebServiceRemoteAddr(Message message){
		String remoteAddr = "";// 客户端IP，例如：192.168.5.6
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		remoteAddr = request.getRemoteAddr(); 

		return remoteAddr;
	}
	
	
	
	/**
	 * 服务名，例如：/services/common
	 * @param message
	 * @return
	 */
	public static String getWebServiceURL(Message message){
		String url = "";// 服务名，例如：/services/common
		HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
		url = (String) message.get(org.apache.cxf.message.Message.REQUEST_URI);
		
		return url;
	}
	
	/**
	 * Message中，参数的内容，soap内容
	 * @param message
	 * @return
	 */
	public static String getWebServiceMessageContent(Message message){
		return ((InputStream) message.getContent(InputStream.class)).toString();
	}
	
	// 暂时未用
	public Element getRootElement(SoapMessage message){
		Element root = null;
		
		SAAJInInterceptor saa = new SAAJInInterceptor();

		SOAPMessage mess = message.getContent(SOAPMessage.class);
		if (mess == null) {
			saa.handleMessage(message);
			mess = message.getContent(SOAPMessage.class);
		}
		try {
			SOAPPart soapPart = mess.getSOAPPart();

			Source source = soapPart.getContent();

			Node node = null;

			Document doc = null;

			DocumentBuilderFactory builder = DocumentBuilderFactory
					.newInstance();

			DocumentBuilder document = builder.newDocumentBuilder();

			if (source instanceof DOMSource) {

				node = ((DOMSource) source).getNode();

			} else if (source instanceof SAXSource) {

				InputSource inSource = ((SAXSource) source).getInputSource();

				doc = document.parse(inSource);

			}

			NodeList list1 = node.getChildNodes();

			// 得到Document的根
			root = (Element) list1.item(0);

		} catch (Exception e) {
			logger.error(e,e);
		}
		
		return root;
	}
	
	public String getElementText(Element root,String tagName){
		String returnValue = "";
		
		NodeList list = root.getElementsByTagName(tagName);
		//遍历page元素
		
		// 获取第一个元素
		if(null != list && list.getLength() > 0){
			Element element = (Element)list.item(0);
			returnValue = element.getTextContent();
		}
		
		return returnValue;
	}

	/**  
     * 字符串转换成org.w3c.dom.Document  
     *   
     * @param XMLStr
     * @return  
     */  
    public static org.w3c.dom.Document convertString2Dom(String XMLStr){  
   	 org.w3c.dom.Document dom = new org.apache.xerces.dom.DocumentImpl();
        try {  
       	 org.xml.sax.InputSource source = new org.xml.sax.InputSource(new CharArrayReader(XMLStr  
                    .toCharArray()));  
       	 javax.xml.parsers.DocumentBuilderFactory docBuilderFactory = javax.xml.parsers.DocumentBuilderFactory  
                    .newInstance();  
       	 javax.xml.parsers.DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();  
            dom = docBuilder.parse(source);  
        } catch (Exception e) { 
        	logger.error(e,e);
            dom = null;  
        }
        
        return dom;
    }  
    
    /**   
     *   org.w3c.dom.Document   ->   org.dom4j.Document   
     *   @param   doc   Document(org.w3c.dom.Document)   
     *   @return   Document   
     */    
	public static org.dom4j.Document convertDom2Dom4j(org.w3c.dom.Document doc)
			throws Exception {
		if (doc == null) {
			return (null);
		}
		org.dom4j.io.DOMReader xmlReader = new org.dom4j.io.DOMReader();
		return (org.dom4j.Document) (xmlReader.read(doc));
	} 
  
   /**   
     *   org.dom4j.Document   ->   org.w3c.dom.Document   
     *   @param   doc   Document(org.dom4j.Document)   
     *   @throws   Exception   
     *   @return   Document   
     */    
   public   static   org.w3c.dom.Document   convertDom4j2Dom(org.dom4j.Document   doc)   throws   Exception   {    
       if   (doc   ==   null)   {    
           return   (null);    
       }    
       java.io.StringReader   reader   =   new   java.io.StringReader(doc.asXML());    
       org.xml.sax.InputSource   source   =   new   org.xml.sax.InputSource(reader);    
       javax.xml.parsers.DocumentBuilderFactory   documentBuilderFactory   =    
               javax.xml.parsers.DocumentBuilderFactory.newInstance();    
       javax.xml.parsers.DocumentBuilder   documentBuilder   =   documentBuilderFactory.    
               newDocumentBuilder();    
       return   (org.w3c.dom.Document)(documentBuilder.parse(source));    
   } 
   
	/**
	 * 从WebService的URL中解析出URL
	 * 例如：
	 * URL = http://localhost/services/common?wsdl
	 * 则
	 * URI = /services/common
	 * @param url
	 * @return
	 */
	public static String getURIByURL(String URL) {
		String URI = "";
		if(null != URL && !"".equals(URL)){
			URL = URL.toLowerCase();
			URI = URL.substring(URL.indexOf("/services"), URL.indexOf("?wsdl"));
		}
		return URI;
	}
	
	/**
	 * 返回非null
	 * 
	 * @param param
	 * @return
	 */
	public static String notNull(String param) {
		return param == null ? "" : param.trim();
	}
	
	/**
	 * 取WebService的header信息
	 * @param message
	 * @return
	 */
	public static Hashtable getSoapHeaderHashtable(SoapMessage message){
		Hashtable<String ,String> hashtable= new Hashtable<String ,String>();
		
		List<Header> headers=message.getHeaders();  
        for(Header item:headers){
        	hashtable.put(item.getName().toString(), ((Element)item.getObject()).getTextContent());
        }
        
		return hashtable;
	};
}
