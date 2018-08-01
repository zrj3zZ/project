package com.iwork.app.navigation.directory.cache;

import java.util.List;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.navigation.directory.model.SysNavDirectory;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheObject;
import org.apache.log4j.Logger;
public class SysNavDirectoryCache implements CacheObject {
	private static Logger logger = Logger.getLogger(SysNavDirectoryCache.class);

	private BaseCache sysNavDirectoryCache;  
    private static SysNavDirectoryCache instance;  
    private static Object lock = new Object();
    private final String key = "SysNavDirectory";
    
    public SysNavDirectoryCache() {  
        //这个根据配置文件来，初始BaseCache而已;  
    	String time = SystemConfig._iworkServerConf.getCacheTime();
    	if(!time.equals("")){
    		sysNavDirectoryCache = new BaseCache(key,Integer.parseInt(time)); 
    	}else{
    		sysNavDirectoryCache = new BaseCache(key,1800); 
    	}
    }  
      
    public static SysNavDirectoryCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new SysNavDirectoryCache();  
                }
            }  
        }  
        return instance;  
    }  
    /**
     * 获得子系统对应目录列表
     * @param systemid
     * @return
     */
	public List<SysNavDirectory> getList() {
		List list = null;
		try {
			list = (List)sysNavDirectoryCache.get(key);
		} catch (Exception e) {logger.error(e,e);
		} 
		return list;
	}

	public void putList(List directorylist) {
		sysNavDirectoryCache.put(key, directorylist);
	}
	
	public SysNavDirectory getModel(Long id) {
		SysNavDirectory sysNavDirectory = null;
		try {
			sysNavDirectory = (SysNavDirectory)sysNavDirectoryCache.get(key+id);
		} catch (Exception e) {logger.error(e,e);
		}
		return sysNavDirectory;
	}

	/**
	 * 装载对象
	 * @param obj
	 */
	public void putModel(SysNavDirectory obj) {
		sysNavDirectoryCache.put(key+obj.getId(), obj);
	}

	public void removeAll() {
		sysNavDirectoryCache.removeAll();
	}

	public void removeList() {
		sysNavDirectoryCache.remove(key);
	}

	public void removeModel(Long id) {
		sysNavDirectoryCache.remove(key+id);

	}
	
	//重新加载cache
	public void instanceReload(){
		getInstance().removeAll();		
	}
}
