package com.iwork.app.navigation.sys.cache;

import java.util.List;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheConst;
import com.iwork.core.cache.CacheObject;
import com.iwork.core.db.ObjectModel;
public class SysNavSystemCache implements CacheObject {
	private BaseCache sysNavSystemCache;  
    private static SysNavSystemCache instance;  
    private static Object lock = new Object();

    public SysNavSystemCache() {  
        //这个根据配置文件来，初始BaseCache而已;  
    	String time = SystemConfig._iworkServerConf.getCacheTime();
    	if(!time.equals("")){
    		sysNavSystemCache = new BaseCache("SysNavSystem",Integer.parseInt(time)); 
    	}else{
    		sysNavSystemCache = new BaseCache("SysNavSystem",1800); 
    	}
    }  
      
    public static SysNavSystemCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new SysNavSystemCache();  
                }
            }  
        }  
        return instance;  
    }  
  
	public List getList() {
		List list;
		try {
			list = (List)sysNavSystemCache.get(CacheConst.OBJ_LIST);
		} catch (Exception e) {
			return null;
		}
    	return list;
	}

	public SysNavSystem getModel(String id) {
		 // TODO 自动生成方法存根  
        try {  
            return (SysNavSystem) sysNavSystemCache.get(id);  
        } catch (Exception e) {
            return null;  
        } 
	}

	//重新加载cache
		public void instanceReload(){
			getInstance().removeAll();
		}

	public void putList(List list) {
		sysNavSystemCache.put(CacheConst.OBJ_LIST,list);
	}

	public void putModel(ObjectModel obj) {
		// TODO 自动生成方法存根
		sysNavSystemCache.put(obj.getId(),obj);
	}

	public void removeAll() {
		sysNavSystemCache.removeAll();  
	}

	public void removeList() {
		sysNavSystemCache.remove(CacheConst.OBJ_LIST);
	}

	public void removeModel(String id) {
		sysNavSystemCache.remove(id);
		
	}

}
