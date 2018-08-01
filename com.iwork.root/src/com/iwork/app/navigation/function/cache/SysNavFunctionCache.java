package com.iwork.app.navigation.function.cache;

import java.util.List;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.navigation.function.model.Sysnavfunction;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheObject;
import org.apache.log4j.Logger;
public class SysNavFunctionCache implements CacheObject {
	private BaseCache SysNavFunctionCache;  
    private static SysNavFunctionCache instance;  
    private static Object lock = new Object();
    private final String key = "SysNavFunction";
    private static Logger logger = Logger.getLogger(SysNavFunctionCache.class);

    public SysNavFunctionCache() {  
        //这个根据配置文件来，初始BaseCache而已;  
    	String time = SystemConfig._iworkServerConf.getCacheTime();
    	if(!time.equals("")){
    		SysNavFunctionCache = new BaseCache(key,Integer.parseInt(time)); 
    	}else{
    		SysNavFunctionCache = new BaseCache(key,1800); 
    	}
    }   
      
    public static SysNavFunctionCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new SysNavFunctionCache();  
                }
            }  
        }  
        return instance;  
    }  
    /**
     * 获得子目录对应模块菜单列表
     * @param systemid
     * @return
     */
	public List<Sysnavfunction> getList(Long directoryid) {
		List list = null;
		try {
			list = (List)SysNavFunctionCache.get("fun_list_"+directoryid);
		} catch (Exception e) {logger.error(e,e);
		}
		return list;
	}

	/**
	 * 装载子目录对应的模块菜单列表
	 * @param directoryid
	 * @param directorylist
	 */
	public void putList(Long directoryid ,List functionlist) {
		SysNavFunctionCache.put("fun_list_"+directoryid, functionlist);
	}
	
	/**
	 * 获得模块菜单模型
	 * @param id
	 * @return
	 */
	public Sysnavfunction getModel(Long id) {
		Sysnavfunction objModel = null;
		try {
			objModel = (Sysnavfunction)SysNavFunctionCache.get(key+id);
		} catch (Exception e) {logger.error(e,e);
		}
		return objModel;
	}

	/**
	 * 装载对象
	 * @param obj
	 */
	public void putModel(Sysnavfunction obj) {
		SysNavFunctionCache.put(key+obj.getId(), obj);
	}

	/**
	 * 移除全部
	 */
	public void removeAll() {
		SysNavFunctionCache.removeAll();
	}

	/**
	 * 移除列表
	 * @param directory
	 */
	public void removeList(Long directory) {
		SysNavFunctionCache.remove("fun_list_"+directory);
	}

	/**
	 * 移除对象
	 * @param id
	 */
	public void removeModel(Long id) {
		SysNavFunctionCache.remove(key+id);

	}
	//重新加载cache
		public void instanceReload(){
			getInstance().removeAll();
		}

}
