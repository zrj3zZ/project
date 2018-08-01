package com.iwork.app.scancode.factory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.iwork.app.conf.ServerConfigParser;
import com.iwork.app.scancode.interceptor.ScanCodeInterface;
import com.iwork.app.scancode.model.ScanCodeConsModel;
import com.iwork.commons.ClassReflect;
import org.apache.log4j.Logger;
public class ScanCodeFactory {
	private static Logger logger = Logger.getLogger(ScanCodeFactory.class);
	private static HashMap _scanCodeList = null;

	private static HashMap _scanCodeSortList = null;
	static {
		loadScanCode();
	}
	/**
	 * 获得二维码处理对象
	 * @return
	 */
	public static ScanCodeInterface getScanCodeImpl(String type) {
		ScanCodeInterface qrCode = null;
		ScanCodeConsModel qrCodeModel = (ScanCodeConsModel) _scanCodeList.get(type);
		if (qrCodeModel != null) {
			Constructor cons = null;
			try {
				cons = qrCodeModel.getCons();
				if (cons != null) {
					Object[] params = {};
					qrCode = (ScanCodeInterface) cons.newInstance(params);
					return qrCode;
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
		}
		return qrCode;
	}
	
	/**
	 * 
	 */
	public static void reloadScanCode() {
		loadScanCode();
	}
	/**
	 * 
	 * @return
	 */
	public static HashMap getScanCodeList() {
		// loadExpression();
		return _scanCodeList;
	}

	/**
	 * @return
	 */
	public static HashMap getScanCodeSortList() {
		return _scanCodeSortList;
	}

	private static void loadScanCode() {
		_scanCodeList = new LinkedHashMap(); 
		_scanCodeSortList = new LinkedHashMap();
		Class[] parameterTypes = {};
		String xml = "scancode-config.xml";
		String web_inf_Path=new File(ServerConfigParser.class.getResource("/").getPath()).getParent();
        web_inf_Path = web_inf_Path.replace("%20", " ");//替换空格
        File file = new File(web_inf_Path+File.separator+xml);
		SAXReader saxreader = new SAXReader();
		Document doc = DocumentFactory.getInstance().createDocument();
		try {
			doc = saxreader.read(file);
			Iterator it = doc.getRootElement().elementIterator();
			it = doc.getRootElement().elementIterator("scancode-item");
			int i = 0;
			while (it.hasNext()) {
				Element element = (Element) it.next();
				if (element.getName().equals("scancode-item")) {
					Iterator iit = element.elementIterator();
					ScanCodeConsModel qrCodeModel = new ScanCodeConsModel();
					while (iit.hasNext()) {
						Element ielement = (Element) iit.next();
						if (ielement.getName().equals("id"))
							qrCodeModel.setId(ielement.getText());
						if (ielement.getName().equals("title"))
							qrCodeModel.setTitle(ielement.getText());
						if (ielement.getName().equals("implements-class"))
							qrCodeModel.setClassName(ielement.getText());
						if (ielement.getName().equals("type"))
							qrCodeModel.setEsType(ielement.getText());
						if (ielement.getName().equals("memo"))
							qrCodeModel.setMemo(ielement.getText());
					}
					// 建立反射
					Constructor cons = null;
					try {
						cons = ClassReflect.getConstructor(qrCodeModel.getClassName(), parameterTypes);
					} catch (Exception e) {
						logger.error(e,e);
					}
					// 设置构造组件的构造函数对象
					qrCodeModel.setCons(cons);
					if (cons != null && !qrCodeModel.getClassName().equals("")) {
						_scanCodeList.put(qrCodeModel.getEsType(), qrCodeModel);
						_scanCodeSortList.put(new Integer(_scanCodeList.size()), qrCodeModel);
					} 
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
	}

}
