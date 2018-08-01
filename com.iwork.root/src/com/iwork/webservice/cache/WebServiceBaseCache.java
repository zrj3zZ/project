package com.iwork.webservice.cache;


import org.apache.log4j.Logger;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheObject;

public class WebServiceBaseCache implements CacheObject  {
	private static Logger logger = Logger.getLogger(WebServiceBaseCache.class);

	private BaseCache webServiceBaseCache;  
    private static WebServiceBaseCache instance;   
    private static Object lock = new Object();
    public WebServiceBaseCache() {  
        //设置基础缓存时间24小时
    	webServiceBaseCache = new BaseCache("webServiceBaseCache",3600*24); 
    }
    public static WebServiceBaseCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new WebServiceBaseCache();  
                }
            }  
        }  
        return instance;  
    }
    
    /**
     * 装载cache
     * @param uuid
     * @param cache
     */
    public void putCache(String uuid,BaseCache cache){
    	webServiceBaseCache.put(uuid, cache);
    }
    
    /**
     * 获取cache
     * @param uuid
     * @return
     */
    public BaseCache getCache(String uuid){
    	BaseCache model = null;
		try {
			model = (BaseCache)webServiceBaseCache.get(uuid);
		} catch (Exception e) {logger.error(e,e);
		}
    	return model;
    }
    
    public void removeAll(){
    	webServiceBaseCache.removeAll();
    }
	//重新加载cache
	public void instanceReload(){
		getInstance().removeAll();
		System.out.println("刷新【WebService管理-运行时】缓存......................................OK");
	}
}
