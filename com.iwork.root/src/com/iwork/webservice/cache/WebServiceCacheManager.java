package com.iwork.webservice.cache;



import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheObject;

public class WebServiceCacheManager implements CacheObject  {
	private BaseCache webServiceBaseCache;  
    private static WebServiceCacheManager instance;   
    private static Object lock = new Object();
    public WebServiceCacheManager(String uuid,int hour) {  
        //这个根据配置文件来，初始BaseCache而已;   
    	webServiceBaseCache = WebServiceBaseCache.getInstance().getCache(uuid);
    	if(webServiceBaseCache==null){
    		webServiceBaseCache = new BaseCache(uuid,hour*600*6); 
    		 WebServiceBaseCache.getInstance().putCache(uuid, this.webServiceBaseCache);
    	}
    } 
    public static WebServiceCacheManager getInstance(String uuid,int hour){  
         instance = new WebServiceCacheManager(uuid,hour);  
         return instance;  
    }
    
    /**
     * 装载输入参数及对象
     * @param key
     * @param obj
     */
    public void putInputParams(String key,Object obj){
    	webServiceBaseCache.put(key, obj);
    }
    
    /**
     * 获得输出参数
     * @param key
     * @return
     */
    public Object getOutputParams(String key){
    	Object returnValue = null;
    	try {
    		returnValue = webServiceBaseCache.get(key);
		} catch (Exception e) {
			
		}
    	return returnValue;
    }   
   

    //重新加载cache
  	public void instanceReload(){
  		
  	}
  	
  	public void removeAll(){
    	webServiceBaseCache.removeAll();
    }
}
