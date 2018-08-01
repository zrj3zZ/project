package com.iwork.webservice.cache;

import com.iwork.app.constant.AppContextConstant;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheObject;

/**
 * WebServiceModelCache<br>
 * 
 * @author lmyanglei@gmail.com
 * @see WebServiceModelCache
 */
public class WebServiceModelCache  implements CacheObject {
	private BaseCache cache;  
    private static WebServiceModelCache instance;  
    private static Object lock = new Object();  
      
    public WebServiceModelCache() {  
    	cache = new BaseCache("WebServiceModelCache",AppContextConstant.cacheLastLong); 
    }  
      
    public static WebServiceModelCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new WebServiceModelCache();  
                }
            }  
        }  
        return instance;  
    }  

    /**
     * 装载对象
     * @param 
     */
    public void put(String key,Object obj) {
        cache.put(key,obj);
    }

    public Object get(String key) {  
        try {  
            return cache.get(key);  
        } catch (Exception e) {
            return null;  
        }  
    }  
  
    public void remove(String key) {  
        cache.remove(key);  
    } 
    
    public void removeAll() {  
        cache.removeAll();  
    }
    
	//重新加载cache
	public void instanceReload(){
		getInstance().removeAll();
		System.out.println("刷新【WebService管理-模型】缓存......................................OK");
	}
}
