package com.iwork.plugs.hr.staff;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.iwork.app.conf.ServerConfigParser;
import com.iwork.commons.ClassReflect;
import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.hr.staff.model.ConfigModel;
import org.apache.log4j.Logger;

public class IWorkHrStaffFactory {
	private static Logger logger = Logger.getLogger(IWorkHrStaffFactory.class);
	private static Hashtable<Object,ConfigModel> _staffList = null;
	private static List<ConfigModel> _staffSortList = null;
	static {
		loadStaff();
	}

	/**
	 * 根据UI组件类型返回其UI组件实例
	 * 
	 * @param UIType
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public static IWorkHrStaffInterface getStaffInstance(UserContext uc,String key) {
		if(_staffList==null){
			loadStaff();
		}
		if(key!=null){
			ConfigModel configModel = (ConfigModel) _staffList.get(key);
			if (configModel != null) {
				Constructor cons = null;
				IWorkHrStaffInterface staff;
				try {
					cons = configModel.getCons();
					if (cons != null) {
						Object[] params = {uc,configModel};
						staff = (IWorkHrStaffInterface) cons.newInstance(params);
						return staff;
					}				
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
		//默认路由策略
		return null;
	}
	
	/**
	 * 根据组件的KEY值，获取组件对象
	 * @param compKey
	 * @return
	 */
	public static ConfigModel getModel(String staffKey){
		List tempList = _staffSortList;
		ConfigModel model = null;
		if(tempList!=null&&staffKey!=null&&!"".equals(staffKey)){
			for(int i=0;i<tempList.size();i++){
				ConfigModel temp =(ConfigModel)tempList.get(i);
				if(temp!=null&&temp.getKey().equals(staffKey)){
					model = temp;
					break;
				}
			}
		}
		return model;
	}

	/**
	 * 重新加载路由策略
	 * 
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public static void reloadStaff() {
		loadStaff();
	}
	
	/**
	 * 加载iform-components.xml
	 */
	private static void loadStaff() {
		_staffList = new Hashtable();
		_staffSortList = new ArrayList();
		Class[] parameterTypes = {UserContext.class,ConfigModel.class};
		String xml = "iwork-emp-self-service-config.xml";
		String web_inf_Path=new File(ServerConfigParser.class.getResource("/").getPath()).getParent();
        web_inf_Path = web_inf_Path.replace("%20", " ");//替换空格
       File file = new File(web_inf_Path+File.separator+xml);
		SAXReader saxreader = new SAXReader();
		Document doc = DocumentFactory.getInstance().createDocument();
		try {
			doc = saxreader.read(file);
			Iterator it = doc.getRootElement().elementIterator();
			Hashtable hs = new Hashtable();
			it = doc.getRootElement().elementIterator("action");
			int i = 0;
			while (it.hasNext()) { 
				Element element = (Element) it.next();
				if (element.getName().equals("action")) {
					Iterator iit = element.elementIterator();
					ConfigModel configModel = new ConfigModel();
					while (iit.hasNext()) {
						Element ielement = (Element) iit.next();
						if (ielement.getName().equals("key")){
							if(ielement.getText()!=null){
								try{
									configModel.setKey(ielement.getText());
								}catch(Exception e){
									logger.error(e,e);
									configModel.setKey(null);
								}
							}
							
						}
						if (ielement.getName().equals("name"))
							configModel.setTitle(ielement.getText());
						if (ielement.getName().equals("interface-class"))
							configModel.setInterfaceClass(ielement.getText());
						if (ielement.getName().equals("implements-class"))
							configModel.setImplementsClass(ielement.getText());
						if (ielement.getName().equals("url"))
							configModel.setUrl(ielement.getText());
						if (ielement.getName().equals("desc"))
							configModel.setDesc(ielement.getText());
					}
					// 建立反射
					Constructor cons = null;
					try {
						cons = ClassReflect.getConstructor(configModel.getImplementsClass(), parameterTypes);	
					} catch (Exception e) {
						logger.error(e,e);
					}
					// 设置构造组件的构造函数对象
					configModel.setCons(cons);
					_staffList.put(configModel.getKey(), configModel);
					_staffSortList.add(configModel);
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
	}

	public static List get_staffSortList() {
		return _staffSortList;
	}


	public static void set_staffSortList(List sortList) {
		_staffSortList = sortList;
	}
	
	
}
