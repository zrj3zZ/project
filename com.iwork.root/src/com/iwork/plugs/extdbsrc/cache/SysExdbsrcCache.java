package com.iwork.plugs.extdbsrc.cache;


import org.apache.log4j.Logger;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheObject;
import com.iwork.plugs.extdbsrc.model.SysExdbsrcCenter;

public class SysExdbsrcCache  implements CacheObject{
	private static Logger logger = Logger.getLogger(SysExdbsrcCache.class);

	private BaseCache sysExdbsrcCache;  
    private static SysExdbsrcCache instance;  
    private static Object lock = new Object();
    private final String key = "SysExdbsrcCache";
    
    public SysExdbsrcCache() {  
        //这个根据配置文件来，初始BaseCache而已;  
    	String time = SystemConfig._iworkServerConf.getCacheTime();
    	if(!time.equals("")){
    		sysExdbsrcCache = new BaseCache(key,Integer.parseInt(time)); 
    	}else{
    		sysExdbsrcCache = new BaseCache(key,1800); 
    	}
    }  
    public static SysExdbsrcCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new SysExdbsrcCache();  
                }
            }  
        }  
        return instance;  
    }
    
    /**
     * 装载外部数据源
     * @param model
     */
    public void putModel(SysExdbsrcCenter model){
    	sysExdbsrcCache.put(model.getId()+"", model);
    	
    }
    
    /**
     * 获取外部数据源
     * @param id
     * @return
     */
    public SysExdbsrcCenter getModel(Long id){
    	SysExdbsrcCenter model = null;
    	try{
    		 model = (SysExdbsrcCenter)sysExdbsrcCache.get(id+"");
    	}catch(Exception e){logger.error(e,e);}
    	return model;
    }
    /**
     * 移除缓存
     * @param id
     */
    public void remove(Long id){
    	sysExdbsrcCache.remove(id+"");
    }
    public void removeAll(){
    	sysExdbsrcCache.removeAll();
    }
  //重新加载cache
  	public void instanceReload(){
  		getInstance().removeAll();  		
  	}
}
