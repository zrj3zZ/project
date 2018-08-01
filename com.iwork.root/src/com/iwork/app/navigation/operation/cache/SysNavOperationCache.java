package com.iwork.app.navigation.operation.cache;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.navigation.operation.model.SysNavOperation;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheObject;
import org.apache.log4j.Logger;
public class SysNavOperationCache implements CacheObject {
	private BaseCache sysNavOperationCache;  
    private static SysNavOperationCache instance;  
    private static Object lock = new Object();
    private final String key = "SysNavOperationCache";
    private static Logger logger = Logger.getLogger(SysNavOperationCache.class);

    public SysNavOperationCache() {  
        //这个根据配置文件来，初始BaseCache而已;  
    	String time = SystemConfig._iworkServerConf.getCacheTime();
    	if(!time.equals("")){
    		sysNavOperationCache = new BaseCache(key,Integer.parseInt(time)); 
    	}else{
    		sysNavOperationCache = new BaseCache(key,1800); 
    	}
    }  
      
    public static SysNavOperationCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new SysNavOperationCache();  
                }
            }  
        }  
        return instance;  
    }  
   
	/**
	 * 装载子目录对应的模块菜单列表
	 * @param directoryid
	 * @param directorylist
	 */
	public void putModel(String oid ,SysNavOperation sysNavOperation) {
		sysNavOperationCache.put(key+oid, sysNavOperation);
	}
	
	/**
	 * 获得模块菜单模型
	 * @param id
	 * @return
	 */
	public SysNavOperation getModel(String oid) {
		SysNavOperation objModel = null;
		try {
			objModel = (SysNavOperation)sysNavOperationCache.get(key+oid);
		} catch (Exception e) {logger.error(e,e);
		}
		return objModel;
	}

	/**
	 * 装载对象
	 * @param obj
	 */
	public void putModel(SysNavOperation sysNavOperation) {
		sysNavOperationCache.put(key+sysNavOperation.getId(), sysNavOperation);
	}

	/**
	 * 移除全部
	 */
	public void removeAll() {
		sysNavOperationCache.removeAll();
	}

	/**
	 * 移除列表
	 * @param directory
	 */
	public void removeList(String directory) {
		sysNavOperationCache.remove(directory);
	}

	/**
	 * 移除对象
	 * @param id
	 */
	public void removeModel(String id) {
		sysNavOperationCache.remove(key+id);

	}
	//重新加载cache
		public void instanceReload(){
			getInstance().removeAll();
		}
}
