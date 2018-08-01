package com.iwork.core.organization.cache;

import com.iwork.app.constant.AppContextConstant;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheObject;
import com.iwork.core.organization.model.OrgRole;
import org.apache.log4j.Logger;
/**
 * 
 * @author David.Yang
 * @preserve 声明此方法不被JOC混淆
 * @see CacheObject
 * @see CompanyModel
 * @see OrganizationDaoFactory
 */
public class RoleUserCache  implements CacheObject {
	private BaseCache roleUserCache;  
    private static RoleUserCache instance;  
    private static Object lock = new Object();  
    private static Logger logger = Logger.getLogger(RoleUserCache.class);
    public RoleUserCache() {  
        //这个根据配置文件来，初始BaseCache而已; 
    		roleUserCache = new BaseCache("roleUserCache",AppContextConstant.cacheLastLong); 
    }  
    
    public static RoleUserCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new RoleUserCache();  
                }
            }  
        }  
        return instance;  
    } 
    
    /**
     * 装载对象
     * @param OrgUser
     */
    public void putModel(String userid,OrgRole orgRole) {
        // TODO 自动生成方法存根
    	roleUserCache.put(userid,orgRole);
    }  
  
    public void removeModel(String userid) {  
        // TODO 自动生成方法存根  
    	roleUserCache.remove(userid);  
    }  
  
    public OrgRole getModel(String userid) {  
        // TODO 自动生成方法存根  
        try {  
            return (OrgRole) roleUserCache.get(userid);  
        } catch (Exception e) {
        	logger.error(e,e);
            return null;  
        }  
    }
    public void removeAll(){
    	roleUserCache.removeAll();
    }
	//重新加载cache
	public void instanceReload(){
		getInstance().removeAll();
	}
}
