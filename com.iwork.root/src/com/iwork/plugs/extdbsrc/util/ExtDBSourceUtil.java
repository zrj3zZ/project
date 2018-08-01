package com.iwork.plugs.extdbsrc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.log4j.Logger;
import com.iwork.core.db.DBUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.extdbsrc.dao.ExtDBSrcDao;
import com.iwork.plugs.extdbsrc.model.SysExdbsrcCenter;

public class ExtDBSourceUtil {
	private static Logger logger = Logger.getLogger(ExtDBSourceUtil.class);

	private static ExtDBSourceUtil instance;  
    private static Object lock = new Object();  
	public static ExtDBSourceUtil getInstance(){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new ExtDBSourceUtil();  
	                }
	            }  
	    return instance;
	}
	private ExtDBSrcDao extDBSrcDao;
	/**
	 * 打开外部数据源链接
	 * @param model
	 * @return
	 */
	public Connection open(SysExdbsrcCenter model){
		Connection cn = null;
		if(model !=null){
			String className = model.getDriverName();
			String url = model.getDsrcUrl();
			String username = model.getUsername();
			String pwd = model.getPassword();
			if(className!=null&&url!=null){
				try{
					Class.forName(className); 
					java.util.Properties info = new java.util.Properties();  
					info.put ("user",username);  
					info.put ("password",pwd);  
					info.put ("resultsetMetaDataOptions","1");  
					cn = DriverManager.getConnection(url,info);
				}catch(Exception e){logger.error(e,e);}
				finally{
					DBUtil.close(cn, null, null);
				}
			}
		}
		return cn;
	}
	
	/**
	 * 关闭数据源
	 * @param conn
	 * @return
	 */
	public boolean close(Connection conn){
		boolean flag = false;
		if(conn!=null){
			try{
				conn.close();
				flag = true;
			}catch(Exception e){logger.error(e,e);}
		}
		return flag;
	}
	
	/**
	 * 获得扩展外部数据源模型
	 * @param uuid
	 * @return
	 */
	public SysExdbsrcCenter getExdbModel(String uuid){
		SysExdbsrcCenter model = null;
		if(extDBSrcDao==null){
			extDBSrcDao = (ExtDBSrcDao)SpringBeanUtil.getBean("extDBSrcDao");
		}
		model = extDBSrcDao.getModelForUUID(uuid);
		return model;
	}

	/**
	 * 获得扩展外部数据源模型
	 * @param uuid
	 * @return
	 */
	public SysExdbsrcCenter getExdbModelForName(String name){
		SysExdbsrcCenter model = null;
		if(extDBSrcDao==null){
			extDBSrcDao = (ExtDBSrcDao)SpringBeanUtil.getBean("extDBSrcDao");
		}
		model = extDBSrcDao.getModelForName(name);
		return model;
	}
	
	public void setExtDBSrcDao(ExtDBSrcDao extDBSrcDao) {
		this.extDBSrcDao = extDBSrcDao;
	}
	
	
}
