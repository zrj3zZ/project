package com.iwork.core.organization.cache;


import com.iwork.app.constant.AppContextConstant;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheObject;
import com.iwork.core.organization.model.OrgRole;
/**
 * 
 * @author David.Yang
 * @preserve 声明此方法不被JOC混淆
 * @see CacheObject
 * @see CompanyModel
 * @see OrganizationDaoFactory
 */
public class RoleCache  implements CacheObject {
	private BaseCache roleCache;  
    private static RoleCache instance;  
    private static Object lock = new Object();
    public RoleCache() {  
        //这个根据配置文件来，初始BaseCache而已; 
    		roleCache = new BaseCache("roleCache",AppContextConstant.cacheLastLong); 
    }  
    
    public static RoleCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new RoleCache();  
                }
            }  
        }  
        return instance;  
    } 
    
    /**
     * 装载对象
     * @param OrgUser
     */
    public void putModel(OrgRole orgRole) {
        // TODO 自动生成方法存根
    	roleCache.put(orgRole.getId(),orgRole);
    }  
  
    public void removeModel(String roleid) {  
        // TODO 自动生成方法存根  
    	roleCache.remove(roleid);  
    }  
  
    public OrgRole getModel(String roleid) {  
        // TODO 自动生成方法存根  
        try {  
            return (OrgRole) roleCache.get(roleid);  
        } catch (Exception e) {
            return null;  
        }  
    }
    public void removeAll(){
    	roleCache.removeAll();
    }
	//重新加载cache
	public void instanceReload(){
		getInstance().removeAll();
	}
}
