package com.iwork.core.organization.cache;

import java.util.List;
import com.iwork.app.constant.AppContextConstant;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheObject;
import com.iwork.core.organization.model.OrgUserMap;

/**
 * 
 * 单位节点模型(内存操作)<br>
 * UserMapCache对象为开发者提供读取单位模型的方法，单位对象 被声明成CompanyModel的实例，并放置在一个Hashtable列表
 * 中，如果没有特殊声明，其key值为Integer类型的序列(0,1...)
 * 
 * @author David.Yang
 * @preserve 声明此方法不被JOC混淆
 * @see CacheObject
 * @see CompanyModel
 * @see OrganizationDaoFactory
 */
public class UserMapCache  implements CacheObject {
	private BaseCache userMapCache;  
    private static UserMapCache instance;  
    private static Object lock = new Object();
    public UserMapCache() {  
        //这个根据配置文件来，初始BaseCache而已; 
    		userMapCache = new BaseCache("userMapCache",AppContextConstant.cacheLastLong); 
    }  
    
    public static UserMapCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new UserMapCache();  
                }
            }  
        }  
        return instance;  
    }  
    /**
     * 装载列表对象
     * @param list
     */
    public void putList(String userid,List list){
    	userMapCache.put(userid,list);
    }
    /**
     * 获取对象列表
     * @return
     */
    public List<OrgUserMap> getList(String userid){
    	List list;
		try {
			list = (List)userMapCache.get(userid);
		} catch (Exception e) {
			return null;
		}
    	return list;
    }
    /**
     * 移除对象列表
     */
    public void removeList(String userid) {  
        // TODO 自动生成方法存根  
    	userMapCache.remove(userid); 
    }
    public void removeAll(){
    	userMapCache.removeAll();
    }
	//重新加载cache
	public void instanceReload(){
		getInstance().removeAll();
	}
}
