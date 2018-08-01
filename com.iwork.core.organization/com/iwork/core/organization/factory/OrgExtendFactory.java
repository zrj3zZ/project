package com.iwork.core.organization.factory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.apache.log4j.Logger;
import com.iwork.app.conf.ServerConfigParser;
import com.iwork.commons.ClassReflect;
import com.iwork.core.organization.model.OrgExtendModel;
import com.iwork.core.organization.model.OrgUser;

public class OrgExtendFactory {
	private static List<OrgExtendModel> _userExtendList = null;
	private static Logger logger = Logger.getLogger(OrgExtendFactory.class);
	private static List<OrgExtendModel> _deptExtendList = null;

	static {
		loadOrgExtend();
	}
	
	public static String executeOrgUser(OrgUser model){
		StringBuffer log = new StringBuffer();
		
		
		return log.toString();
	}
	
	private static void loadOrgExtend() {
		_userExtendList = new ArrayList(); 
		
		_deptExtendList = new ArrayList(); 
		
		Class[] parameterTypes = {};
		String xml = "org-config.xml";
		String web_inf_Path=new File(ServerConfigParser.class.getResource("/").getPath()).getParent();
        web_inf_Path = web_inf_Path.replace("%20", " ");//替换空格
        File file = new File(web_inf_Path+File.separator+xml);
		SAXReader saxreader = new SAXReader();
		Document doc = DocumentFactory.getInstance().createDocument();
		try {
			doc = saxreader.read(file);
			Iterator it = doc.getRootElement().elementIterator();
			it = doc.getRootElement().elementIterator("user-item");
			int i = 0;
			while (it.hasNext()) {
				Element element = (Element) it.next();
				if (element.getName().equals("user-item")) {
					Iterator iit = element.elementIterator();
					OrgExtendModel qrCodeModel = new OrgExtendModel();
					while (iit.hasNext()) {
						Element ielement = (Element) iit.next();
						if (ielement.getName().equals("key"))
							qrCodeModel.setId(ielement.getText());
						if (ielement.getName().equals("title"))
							qrCodeModel.setTitle(ielement.getText());
						if (ielement.getName().equals("implements-class"))
							qrCodeModel.setClassName(ielement.getText());
						if (ielement.getName().equals("type"))
							qrCodeModel.setType(ielement.getText());
						if (ielement.getName().equals("memo"))
							qrCodeModel.setMemo(ielement.getText());
					}
					// 建立反射
					Constructor cons = null;
					try {
						cons = ClassReflect.getConstructor(qrCodeModel.getClassName(), parameterTypes);
					} catch (ClassNotFoundException ce) {						
						logger.error("ERROR:>>未定义的二维码引擎配置！[" + qrCodeModel.getClassName() + "]");					
					} catch (NoSuchMethodException ne) {						
						logger.error("ERROR:>>二维码引擎配置！[" + qrCodeModel.getClassName() + "]构造方法不匹配!");						
					} catch (Exception e) {
						logger.error(e,e);
					}
					// 设置构造组件的构造函数对象
					qrCodeModel.setCons(cons);
					if (cons != null && !qrCodeModel.getClassName().equals("")) {
						_userExtendList.add(qrCodeModel);
					} 
				}
			}
			it = doc.getRootElement().elementIterator("department-item");
			while (it.hasNext()) {
				Element element = (Element) it.next();
				if (element.getName().equals("department-item")) {
					Iterator iit = element.elementIterator();
					OrgExtendModel qrCodeModel = new OrgExtendModel();
					while (iit.hasNext()) {
						Element ielement = (Element) iit.next();
						if (ielement.getName().equals("key"))
							qrCodeModel.setId(ielement.getText());
						if (ielement.getName().equals("title"))
							qrCodeModel.setTitle(ielement.getText());
						if (ielement.getName().equals("implements-class"))
							qrCodeModel.setClassName(ielement.getText());
						if (ielement.getName().equals("type"))
							qrCodeModel.setType(ielement.getText());
						if (ielement.getName().equals("memo"))
							qrCodeModel.setMemo(ielement.getText());
					} 
					// 建立反射
					Constructor cons = null;
					try {
						cons = ClassReflect.getConstructor(qrCodeModel.getClassName(), parameterTypes);
					} catch (ClassNotFoundException ce) {						
						logger.error("ERROR:>>未定义的二维码引擎配置！[" + qrCodeModel.getClassName() + "]");
					} catch (NoSuchMethodException ne) {						
						logger.error("ERROR:>>二维码引擎配置！[" + qrCodeModel.getClassName() + "]构造方法不匹配!");
					} catch (Exception e) {
						logger.error(e,e);
					}
					// 设置构造组件的构造函数对象
					qrCodeModel.setCons(cons);
					if (cons != null && !qrCodeModel.getClassName().equals("")) {
						_deptExtendList.add(qrCodeModel);
					} 
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
	}

	public static List<OrgExtendModel> getUserExtendList() {
		return _userExtendList;
	}

	public static List<OrgExtendModel> getDeptExtendList() {
		return _deptExtendList;
	}

}
