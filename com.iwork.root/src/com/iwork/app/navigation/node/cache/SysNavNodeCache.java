package com.iwork.app.navigation.node.cache;

import java.util.List;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.navigation.node.model.SysNavNode;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheObject;
public class SysNavNodeCache implements CacheObject {
	private BaseCache sysNavNodeCache;  
    private static SysNavNodeCache instance;  
    private static Object lock = new Object();
    private final String key = "SysNavNode";

    public SysNavNodeCache(){
    	//这个根据配置文件来，初始BaseCache而已;
    	String time = SystemConfig._iworkServerConf.getCacheTime();
    	if(!time.equals("")){
    		sysNavNodeCache = new BaseCache(key,Integer.parseInt(time)); 
    	}else{
    		sysNavNodeCache = new BaseCache(key,1800); 
    	}
    }
    
    /**
     * 获得一个cache实例
     * @return
     */
    public static SysNavNodeCache getInstance(){
    	 if (instance == null){  
             synchronized( lock ){  
                 if (instance == null){  
                     instance = new SysNavNodeCache();  
                 }
             }  
         }
    	 return instance;
    }
    
    /**
	 * 装载父节点对应子节点菜单列表
	 * @param directoryid
	 * @param directorylist
	 */
	public void putList(String parentNodeId ,List parentNodeIdList) {
		sysNavNodeCache.put(parentNodeId, parentNodeIdList);
	}
	
    /**
     * 获得父节点对应子节点菜单列表
     * @param parentNodeId
     * @return
     */
    public List<SysNavNode> getList(String parentNodeId){
    	List list = null;
    	try{
    		list = (List)sysNavNodeCache.get(parentNodeId);
    	}catch(Exception e){
    	}
    	return list;
    }
    
    /**
	 * 移除列表
	 * @param parentNodeId
	 */
	public void removeList(String parentNodeId) {
		sysNavNodeCache.remove(parentNodeId);
	}
    
	/**
	 * 装载对象
	 * @param obj
	 */
	public void putModel(SysNavNode obj) {
		sysNavNodeCache.put(key+obj.getId(), obj);
	}
	
    /**
	 * 获得节点菜单模型
	 * @param id
	 * @return
	 */
	public SysNavNode getModel(String id) {
		SysNavNode objModel = null;
		try {
			objModel = (SysNavNode)sysNavNodeCache.get(key+id);
		} catch (Exception e) {
			return null;
		}
		return objModel;
	}
	
	/**
	 * 移除对象
	 * @param id
	 */
	public void removeModel(String id) {
		sysNavNodeCache.remove(key+id);

	}
	
	/**
	 * 移除全部
	 */
	public void removeAll() {
		sysNavNodeCache.removeAll();
	}

	//重新加载cache
		public void instanceReload(){
			getInstance().removeAll();
		}
}
