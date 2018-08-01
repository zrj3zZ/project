package com.iwork.core.organization.cache;


import java.util.List;
import com.iwork.app.constant.AppContextConstant;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheConst;
import com.iwork.core.cache.CacheObject;
import com.iwork.core.db.ObjectModel;
import com.iwork.core.organization.model.OrgCompany;

/**
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
public class CompanyCache  implements CacheObject {
	private BaseCache companyCache;  
    private static CompanyCache instance;  
    private static Object lock = new Object();
    public CompanyCache() {  
        //这个根据配置文件来，初始BaseCache而已; 
    		companyCache = new BaseCache("company",AppContextConstant.cacheLastLong); 
    }  
      
    public static CompanyCache getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new CompanyCache();  
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
    	 companyCache.put(CacheConst.OBJ_LIST,list);
    }
    /**
     * 获取对象列表
     * @return
     */
    public List getList(){
    	List list;
		try {
			list = (List)companyCache.get(CacheConst.OBJ_LIST);
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
        companyCache.remove(CacheConst.OBJ_LIST); 
    } 
    /**
     * 装载对象
     * @param orgCompany
     */
    public void putModel(ObjectModel orgCompany) {
        // TODO 自动生成方法存根
        companyCache.put(orgCompany.getId(),orgCompany);
    }  
  
    public void removeModel(String id) {  
        // TODO 自动生成方法存根  
        companyCache.remove(id);  
    }  
  
    public OrgCompany getModel(String id) {  
        // TODO 自动生成方法存根  
        try {  
            return (OrgCompany) companyCache.get(id);  
        } catch (Exception e) {
            return null;  
        }  
    }  
  
    public void removeAll() {  
        // TODO 自动生成方法存根  
        companyCache.removeAll();  
    }  
	//重新加载cache
	public void instanceReload(){
		getInstance().removeAll();
	}
}
