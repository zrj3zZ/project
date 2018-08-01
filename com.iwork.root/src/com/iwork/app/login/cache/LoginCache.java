package com.iwork.app.login.cache;

import java.util.HashMap;
import java.util.List;
import com.iwork.app.constant.AppContextConstant;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheConst;
import com.iwork.core.cache.CacheObject;
import com.iwork.core.organization.model.OrgUser;
/**
 * 
 * 单位节点模型(内存操作)<br>
 * CompanyCache对象为开发者提供读取单位模型的方法，单位对象 被声明成CompanyModel的实例，并放置在一个Hashtable列表
 * 中，如果没有特殊声明，其key值为Integer类型的序列(0,1...)
 * 
 * @author David.Yang
 * @preserve 声明此方法不被JOC混淆
 * @see CacheObject
 * @see CompanyModel
 * @see OrganizationDaoFactory
 */
public class LoginCache  implements CacheObject {
	private BaseCache loginCache;  
    private static LoginCache instance;
    private static HashMap<String,Integer> map=new HashMap<String,Integer>();
    private static Object lock = new Object();
      
    public LoginCache() {  
        //这个根据配置文件来，初始BaseCache而已; 
    	loginCache = new BaseCache("loginCache",AppContextConstant.cacheLastLong); 
    }  
    
    public static LoginCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new LoginCache();  
                }
            }  
        }  
        return instance;  
    }  
  
    /**
     * 装载列表对象
     * @param list
     */
    public void putList(List list){
    	loginCache.put(CacheConst.OBJ_LIST,list);
    }
    /**
     * 获取对象列表
     * @return
     */
    public List getList(){
    	List list;
		try {
			list = (List)loginCache.get(CacheConst.OBJ_LIST);
		} catch (Exception e) {
			return null;
		}
    	return list;
    }
    /**
     * 移除对象列表
     */
    public void removeList() {  
        // TODO 自动生成方法存根  
    	loginCache.remove(CacheConst.OBJ_LIST); 
    } 
    /**
     * 装载对象
     * @param OrgUser
     */
    public void putModel(OrgUser orgUser) {
        // TODO 自动生成方法存根
    	loginCache.put(orgUser.getUserid(),orgUser);
    }
    
    public void putHashMap(String userid,HashMap map) {
    	// TODO 自动生成方法存根
    	loginCache.put(userid,map);
    }  
    
    public HashMap getHashMap(String userid) {  
        // TODO 自动生成方法存根  
        try {  
            return (HashMap) loginCache.get(userid);  
        } catch (Exception e) {
            return null;  
        }  
    }
  
    public void removeModel(String userid) {  
        // TODO 自动生成方法存根  
    	loginCache.remove(userid);
    }  
    
    public void removeHashMap(String userid) {  
        // TODO 自动生成方法存根  
    	loginCache.remove(userid);  
    }  
  
    public OrgUser getModel(String userid) {  
        // TODO 自动生成方法存根  
        try {  
            return (OrgUser) loginCache.get(userid);  
        } catch (Exception e) {
            return null;  
        }  
    }
    
    public void removeAll(){
    	loginCache.removeAll();
    }
	//重新加载cache
	public void instanceReload(){
		getInstance().removeAll();
	}
}
