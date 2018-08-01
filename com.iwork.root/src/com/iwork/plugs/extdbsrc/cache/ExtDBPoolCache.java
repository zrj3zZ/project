package com.iwork.plugs.extdbsrc.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.cache.BaseCache;
import com.iwork.process.definition.step.cache.ProcessStepCache;

public class ExtDBPoolCache {
	private static final Logger logger = LoggerFactory.getLogger(ProcessStepCache.class);
	private BaseCache extDBPoolCache;  
    private static ExtDBPoolCache instance;  
    private static Object lock = new Object();
    private final String key = "ExtDBPoolCache";
    public ExtDBPoolCache() {  
        //这个根据配置文件来，初始BaseCache而已;  
    	String time = SystemConfig._iworkServerConf.getCacheTime();
    	if(!time.equals("")){
    		extDBPoolCache = new BaseCache(key,Integer.parseInt(time)); 
    	}else{
    		extDBPoolCache = new BaseCache(key,1800); 
    	}
    }  
    public static ExtDBPoolCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new ExtDBPoolCache();  
                }
            }  
        }  
        return instance;  
    }
    
}
