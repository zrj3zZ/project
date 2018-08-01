package com.iwork.admin.dashboard;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.iwork.admin.framework.model.DashboardModel;
import com.iwork.app.conf.ServerConfigParser;
import com.iwork.commons.ClassReflect;
import com.iwork.core.cache.CacheFactory;
import com.iwork.core.cache.CacheObject; 

public class DashboardFactory {
	private static Logger logger = Logger.getLogger(CacheFactory.class);  
	private static Hashtable<Object,DashboardModel> _list = null;
	private static List<DashboardModel> _sortList = null;
	static {
		loadDashboard();
	}

	/**
	 * 根据UI组件类型返回其UI组件实例
	 * 
	 * @param UIType
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public static CacheObject getInstance(String key) {
		if(_list==null){
			loadDashboard();
		}
		if(key!=null){
			DashboardModel dashboardModel = (DashboardModel) _list.get(key);
			if (dashboardModel != null) {
				Constructor cons = null;
				CacheObject cache;
				try {
					cons = dashboardModel.getCons();
					if (cons != null) { 
						cache = (CacheObject) cons.newInstance();
						return cache;
					}
				} catch (InstantiationException ie) {		
					logger.error(ie);
				} catch (java.lang.IllegalAccessException le) {		
					logger.error(le);
				} catch (java.lang.reflect.InvocationTargetException invoke) {		
					logger.error(invoke);
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
		return null;
	}
	
	/**
	 * 获得分组列表
	 * @return
	 */
	public static List<String> getGroupList(){
		List<String> grouplist = new ArrayList();
		if(_sortList!=null){
			for(DashboardModel model:_sortList){
				if(model.getGroupName()!=null&&!model.getGroupName().equals("")){
					if(grouplist.contains(model.getGroupName())){
						continue;
					}else{
						grouplist.add(model.getGroupName());
					}
				}
			}
		}
		return grouplist;
	}
	
	/**
	 * 根据组件的KEY值，获取组件对象
	 * @param compKey
	 * @return
	 */
	public static DashboardModel getModel(String cacheKey){
		List tempList = _sortList;
		DashboardModel model = null; 
		if(tempList!=null&&cacheKey!=null&&!"".equals(cacheKey)){
			for(int i=0;i<tempList.size();i++){
				DashboardModel temp =(DashboardModel)tempList.get(i);
				if(temp!=null&&temp.getKey().equals(cacheKey)){
					model = temp;
					break;
				}
			}
		}
		return model;
	}

	
	/**
	 * 重新加载后台首页面板设置
	 * 
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public static void reloadDashboard() {
		loadDashboard();
	}
	
	/**
	 * 加载ibpm-dashboard-config.xml
	 */
	private static void loadDashboard() {
		_list = new Hashtable();
		_sortList = new ArrayList();
		Class[] parameterTypes = {};
		String xml = "ibpm-dashboard-config.xml";
		String web_inf_Path=new File(ServerConfigParser.class.getResource("/").getPath()).getParent();
        web_inf_Path = web_inf_Path.replace("%20", " ");//替换空格
       File file = new File(web_inf_Path+File.separator+xml);
		SAXReader saxreader = new SAXReader();
		Document doc = DocumentFactory.getInstance().createDocument();
		try {
			doc = saxreader.read(file);
			Iterator it = doc.getRootElement().elementIterator();
			Hashtable hs = new Hashtable();
			it = doc.getRootElement().elementIterator("portlet");
			int i = 0;
			while (it.hasNext()) { 
				Element element = (Element) it.next();
				if (element.getName().equals("portlet")) {
					Iterator iit = element.elementIterator();
					DashboardModel dashboardModel = new DashboardModel();
					while (iit.hasNext()) {
						Element ielement = (Element) iit.next();
						if (ielement.getName().equals("portlet-key")){
							if(ielement.getText()!=null){
								try{
									dashboardModel.setKey(ielement.getText());
								}catch(Exception e){
									logger.error(e,e);
									dashboardModel.setKey("");
								}
							}
						}
						if (ielement.getName().equals("portlet-title"))
							dashboardModel.setTitle(ielement.getText());
						if (ielement.getName().equals("implements-class"))
							dashboardModel.setClassName(ielement.getText());
						if (ielement.getName().equals("interface-class"))
							dashboardModel.setInterfaceName(ielement.getText());
						if (ielement.getName().equals("desc"))
							dashboardModel.setDesc(ielement.getText());
						if (ielement.getName().equals("portlet-group"))
							dashboardModel.setGroupName(ielement.getText());
						if (ielement.getName().equals("height"))
							dashboardModel.setHeight(ielement.getText());
					}
					// 建立反射
					Constructor cons = null;
					try {
						cons = ClassReflect.getConstructor(dashboardModel.getClassName(), parameterTypes);
					} catch (ClassNotFoundException ce) {
						logger.error(ce);
					} catch (NoSuchMethodException ne) {
						logger.error(ne);
					} catch (Exception e) { 
						logger.error(e,e);
					}
					// 设置构造组件的构造函数对象
					dashboardModel.setCons(cons);
					_list.put(dashboardModel.getKey(), dashboardModel);
					_sortList.add(dashboardModel);
				}
			}
		} catch (Exception e) {logger.error(e,e);
		}
	}


	public static List getSortList() {
		return _sortList;
	}

	public static void setSortList(List sortList) {
		_sortList = sortList;
	}
}
