package com.iwork.plugs.email.cache;

import com.iwork.core.cache.BaseCache;
import org.apache.log4j.Logger;
public class EmailCache {
	private static Logger logger = Logger.getLogger(EmailCache.class);

	private BaseCache bigTxtCache;  
    private static EmailCache instance;  
    private static Object lock = new Object();
    private final String key = "EmailCache";
    
    public EmailCache() {  
        bigTxtCache = new BaseCache(key,43200); 
    }  
    public static EmailCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new EmailCache();  
                }
            }  
        }  
        return instance;  
    }  
   

    public void putBigTxt(String key,String content){
    	bigTxtCache.put(key, content);
    }
    
    /**
     * 获取大文本数据
     * @param key
     * @return
     */
    public String getBigTxt(String key){
    	String content = null;
    	Object obj=null;
		try {
			obj = bigTxtCache.get(key);
		} catch (Exception e) {logger.error(e,e);
		}
    	if(obj!=null){
    		content = obj.toString();
    	}
    	return content;
    }
    public void remove(String key){
    	bigTxtCache.remove(key);
    }
    
	/**
	 * 移除全部
	 */
	public void removeAll() {
		bigTxtCache.removeAll();
	}

	public void instanceReload() {
		// TODO Auto-generated method stub
		getInstance().removeAll(); 
	}
}
