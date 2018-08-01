package com.iwork.core.organization.cache;

import java.util.List;

import org.apache.log4j.Logger;

import com.iwork.app.constant.AppContextConstant;
import com.iwork.core.cache.BaseCache;
import com.iwork.core.cache.CacheConst;
import com.iwork.core.cache.CacheObject;
import com.iwork.core.db.ObjectModel;
import com.iwork.core.organization.model.OrgGroup;
	/**
	 * 单位节点模型(内存操作)<br>
	 * GroupCache对象为开发者提供读取单位模型的方法，单位对象 被声明成GroupModel的实例，并放置在一个Hashtable列表
	 * 中，如果没有特殊声明，其key值为Integer类型的序列(0,1...)
	 * 
	 * @author WeiGuangjian
	 * @preserve 声明此方法不被JOC混淆
	 * @see CacheObject
	 * @see GroupModel
	 * @see OrganizationDaoFactory
	 */
	public class GroupCache  implements CacheObject {
		private BaseCache groupCache;  
	    private static GroupCache instance;  
	    private static Object lock = new Object();  
		private static Logger logger = Logger
				.getLogger(GroupCache.class);
	    public GroupCache() {  
	        //这个根据配置文件来，初始BaseCache而已; 
	    	groupCache = new BaseCache("group",AppContextConstant.cacheLastLong); 
	    }  
	      
	    public static GroupCache getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new GroupCache();  
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
	    	groupCache.put(CacheConst.OBJ_LIST,list);
	    }
	    
	    /**
	     * 获取对象列表
	     * @return
	     */
	    public List getList(){
	    	List list;
			try {
				list = (List)groupCache.get(CacheConst.OBJ_LIST);
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
	    	groupCache.remove(CacheConst.OBJ_LIST); 
	    } 
	    
	    /**
	     * 装载有效列表对象
	     * @param now
	     * @param list
	     */
	    public void putGroupList(String now,List list){
	    	groupCache.put(now,list);
	    }
	    
	    /**
	     * 获取有效列表对象
	     * @param now
	     * @return
	     */
	    public List getGroupList(String now){
	    	List list;
			try {
				list = (List)groupCache.get(now);
			} catch (Exception e) {
				logger.error(e,e);
				return null;
			}
	    	return list;
	    }
	    
	    /**
	     * 移除有效列表对象
	     * @param now
	     */
	    public void removeGroupList(String now) {  
	        // TODO 自动生成方法存根  
	    	groupCache.remove(now); 
	    } 
	    
	    /**
	     * 装载对象
	     * @param model
	     */
	    public void putModel(ObjectModel model) {
	        // TODO 自动生成方法存根
	    	groupCache.put(model.getId(),model);
	    }  
	  
	    /**
	     * 获取对象
	     * @param id
	     * @return
	     */
	    public OrgGroup getModel(String id) {  
	        // TODO 自动生成方法存根  
	        try {  
	            return (OrgGroup) groupCache.get(id);  
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
	    	groupCache.remove(id);  
	    }  
	  
	    /**
	     * 清空
	     */
	    public void removeAll() {  
	        // TODO 自动生成方法存根  
	    	groupCache.removeAll();  
	    }  
	    
		/**
		 * 重新加载cache
		 */
		public void instanceReload(){
			getInstance().removeAll();
		}
	}

