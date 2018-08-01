package com.iwork.core.organization.cache;

import java.util.List;

import com.iwork.app.constant.AppContextConstant;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheConst;
import com.iwork.core.cache.CacheObject;
import com.iwork.core.db.ObjectModel;
import com.iwork.core.organization.model.OrgGroupSub;
import org.apache.log4j.Logger;
	/**
	 * 单位节点模型(内存操作)<br>
	 * GroupSubCache对象为开发者提供读取单位模型的方法，单位对象 被声明成GroupSubModel的实例，并放置在一个Hashtable列表
	 * 中，如果没有特殊声明，其key值为Integer类型的序列(0,1...)
	 * 
	 * @author WeiGuangjian
	 * @preserve 声明此方法不被JOC混淆
	 * @see CacheObject
	 * @see GroupSubModel
	 * @see OrganizationDaoFactory
	 */
	public class GroupSubCache  implements CacheObject {
		private BaseCache groupSubCache;  
	    private static GroupSubCache instance;  
	    private static Object lock = new Object();  
	    private static Logger logger = Logger.getLogger(GroupSubCache.class);
	    public GroupSubCache() {  
	        //这个根据配置文件来，初始BaseCache而已; 
	    	groupSubCache = new BaseCache("groupsub",AppContextConstant.cacheLastLong); 
	    }  
	      
	    public static GroupSubCache getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new GroupSubCache();  
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
	    	groupSubCache.put(CacheConst.OBJ_LIST,list);
	    }
	    
	    /**
	     * 获取对象列表
	     * @return
	     */
	    public List getList(){
	    	List list;
			try {
				list = (List)groupSubCache.get(CacheConst.OBJ_LIST);
			} catch (Exception e) {
				logger.error(e,e);
				return null;
			}
	    	return list;
	    }
	    
	    /**
	     * 移除对象列表
	     */
	    public void removeList() {  
	        // TODO 自动生成方法存根  
	    	groupSubCache.remove(CacheConst.OBJ_LIST); 
	    } 
	    
	    /**
	     * 装载团队列表对象
	     * @param id
	     * @param list
	     */
	    public void putGroupSubList(String id,List list){
	    	groupSubCache.put(id,list);
	    }
	    
	    /**
	     * 获取团队列表对象
	     * @param now
	     * @return
	     */
	    public List getGroupSubList(String id){
	    	List list;
			try {
				list = (List)groupSubCache.get(id);
			} catch (Exception e) {
				logger.error(e,e);
				return null;
			}
	    	return list;
	    }
	    
	    /**
	     * 移除团队列表对象
	     * @param now
	     */
	    public void removeGroupSubList(String id) {  
	        // TODO 自动生成方法存根  
	    	groupSubCache.remove(id); 
	    } 
	    
	    
	    /**
	     * 装载对象
	     * @param orgGroup
	     */
	    public void putModel(ObjectModel orgGroup) {
	        // TODO 自动生成方法存根
	    	groupSubCache.put(orgGroup.getId(),orgGroup);
	    }  
	  
	   /**
	    * 获取对象
	    * @param id
	    * @return
	    */
	    public OrgGroupSub getModel(String id) {  
	        // TODO 自动生成方法存根  
	        try {  
	            return (OrgGroupSub) groupSubCache.get(id);  
	        } catch (Exception e) {  
	        	logger.error(e,e);
	            return null;  
	        }  
	    }  
	  
	    /**
	     * 移除对象
	     * @param id
	     */
	    public void removeModel(String id) {  
	        // TODO 自动生成方法存根  
	    	groupSubCache.remove(id);  
	    }  
	    
	    /**
	     * 清空
	     */
	    public void removeAll() {  
	        // TODO 自动生成方法存根  
	    	groupSubCache.removeAll();  
	    }  
	    
		/**
		 * 重新加载cache
		 */
		public void instanceReload(){
			getInstance().removeAll();			
		}
	}

