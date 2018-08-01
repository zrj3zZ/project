package com.iwork.webservice.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.webservice.cache.WebServiceCacheManager;
import com.iwork.webservice.cache.WebServiceModelCache;
import com.iwork.webservice.constants.WebServiceConstants;
import com.iwork.webservice.model.SysWsBaseinfo;
import com.iwork.webservice.model.SysWsParams;

public class WebServiceDAO  extends HibernateDaoSupport{
	
	public WebServiceDAO(){
		
	}

	/**
	 * 根据ID获取模型
	 * @param id
	 * @return
	 */
	public SysWsBaseinfo getModel(int id){ 
		String cacheKey = "getModelint"+id;
		
		SysWsBaseinfo model = (SysWsBaseinfo) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == model){
			model = (SysWsBaseinfo)this.getHibernateTemplate().get(SysWsBaseinfo.class, id);
			
			// 缓存
			if(null != model){
				WebServiceModelCache.getInstance().put(cacheKey, model);
			}
		}
		
		return model;
	}
	

	/**
	 * 根据UUID获取模型
	 * @param uuid
	 * @return
	 */
	public SysWsBaseinfo getModel(String uuid){
		String cacheKey = "getModelString"+uuid;
		
		SysWsBaseinfo model = (SysWsBaseinfo) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == model){
			String sql = "From SysWsBaseinfo where uuid = ?";
			DBUtilInjection d=new DBUtilInjection();
		   
		    if ((uuid != null) && (!"".equals(uuid))) {
	        	if(d.HasInjectionData(uuid)){
    				return model;
    			}
	          
	        }
			Object[] values = {uuid};
			List<SysWsBaseinfo> list = this.getHibernateTemplate().find(sql,values);
			if(list!=null&&list.size()>0){
				model = list.get(0);
			}
			
			// 缓存
			if(null != model){
				WebServiceModelCache.getInstance().put(cacheKey, model);
			}
		}
		
		return model;
	}
	
	/**
	 * 保存
	 * @param model
	 */
	public void saveBaseInfo(SysWsBaseinfo model){
		this.getHibernateTemplate().save(model);

		// 清空缓存
		removeAllCache(model);
	}
	
	/**
	 * 更新
	 * @param model
	 */
	public void updateBaseInfo(SysWsBaseinfo model){
		this.getHibernateTemplate().update(model);
		
		// 清空缓存
		removeAllCache(model);
	}
	
	/**
	 * 清空Model相关的缓存
	 * 
	 * @param model
	 */
	private void removeAllCache(SysWsBaseinfo model){
		// 清空Model缓存
		WebServiceModelCache.getInstance().removeAll();
		
		// 清空Runtime缓存
		WebServiceCacheManager.getInstance(model.getUuid(), model.getCacheTime()).removeAll();
	};
	
	/**
	 * 删除
	 * @param model
	 */
	public void delete(SysWsBaseinfo model){
		this.getHibernateTemplate().delete(model);
		
		// 清空缓存
		WebServiceModelCache.getInstance().removeAll();
	}
	
	/**
	 * 根据groupId，获取模型列表
	 * @param groupId
	 * @return
	 */
	public List<SysWsBaseinfo> getList(Long groupId){
		String cacheKey = "getListLong"+groupId;
		
		List<SysWsBaseinfo> list = (List<SysWsBaseinfo>) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == list){
			String sql = "From SysWsBaseinfo where groupId = ?";
			Object[] values = {groupId};
			list = this.getHibernateTemplate().find(sql,values);
			
			// 缓存
			if(null != list){
				WebServiceModelCache.getInstance().put(cacheKey, list);
			}
		}
		return list;
	}

	/**
	 * 保存参数模型
	 * @param model
	 */
	public void saveParam(SysWsParams model){
		model.setOrderIndex(this.getMaxParamsOrderIndex());
		this.getHibernateTemplate().save(model);
		
		// 清空缓存
		WebServiceModelCache.getInstance().removeAll();
	}
	
	/**
	 * 保存参数模型
	 * @param model
	 */
	public void deleteParam(SysWsParams model){
		this.getHibernateTemplate().delete(model);
		
		// 清空缓存
		WebServiceModelCache.getInstance().removeAll();
	}
	
	/**
	 * 更新参数模型
	 * @param model
	 */
	public void updateParam(SysWsParams model){
		this.getHibernateTemplate().update(model);
		
		// 清空缓存
		WebServiceModelCache.getInstance().removeAll();
	}
	
	/**
	 * 获取参数模型列表
	 * @param type 输入参数/输出参数
	 * @param bid 基础模型ID
	 * @return
	 */
	public List<SysWsParams> getWsParamsList(String inorout,int pid){
		String cacheKey = "getWsParamsListStringint"+inorout+pid;
		List<SysWsParams> list = (List<SysWsParams>) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == list){
			String sql = "From SysWsParams where inorout=? and pid = ? order by orderIndex";
			DBUtilInjection d=new DBUtilInjection();
		    List lis=new ArrayList(); 
		    if ((inorout != null) && (!"".equals(inorout))) {
	        	if(d.HasInjectionData(inorout)){
    				return lis;
    			}
	          
	        }
			Object[] values = {inorout,pid};
			list = this.getHibernateTemplate().find(sql,values);
			
			// 缓存
			if(null != list){
				WebServiceModelCache.getInstance().put(cacheKey, list);
			}
		}
		return list;
	}
	
	/**
	 * 获取输入参数模型列表
	 * @param bid 基础模型ID
	 * @return
	 */
	public List<SysWsParams> getInputParamsList(int pid){
		String cacheKey = "getInputParamsListint"+pid;
		List<SysWsParams> list = (List<SysWsParams>) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == list){
			String sql = "From SysWsParams where inorout=? and pid = ? order by orderIndex";
			Object[] values = {WebServiceConstants.WS_PARAMS_TYPE_INPUT,pid};
			list = this.getHibernateTemplate().find(sql);
			
			// 缓存
			if(null != list){
				WebServiceModelCache.getInstance().put(cacheKey, list);
			}
		}
		return list;
	}
	
	/**
	 * 获取输出参数模型列表
	 * @param bid 基础模型ID
	 * @return
	 */
	public List<SysWsParams> getOutputParamsList(int pid){
		String cacheKey = "getOutputParamsListint"+pid;
		List<SysWsParams> list = (List<SysWsParams>) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == list){
			String sql = "From SysWsParams where inorout=? and pid = ? order by orderIndex";
			Object[] values = {WebServiceConstants.WS_PARAMS_TYPE_OUTPUT,pid};
			list = this.getHibernateTemplate().find(sql);
			
			// 缓存
			if(null != list){
				WebServiceModelCache.getInstance().put(cacheKey, list);
			}
		}
		return list;
	}
	
	/**
	 * 获取参数模型列表
	 * @param type 输入参数/输出参数
	 * @param bid 基础模型ID
	 * @return
	 */
	public List<SysWsParams> getWsParamsAllList(int pid){
		String cacheKey = "getWsParamsAllListint"+pid;
		List<SysWsParams> list = (List<SysWsParams>) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == list){
			String sql = "From SysWsParams where pid = ?";
			Object[] values = {pid};
			list = this.getHibernateTemplate().find(sql,values);
			// 缓存
			if(null != list){
				WebServiceModelCache.getInstance().put(cacheKey, list);
			}
		}
		return list;
	}

	/**
	 * 获得参数对象
	 * @param id
	 * @return
	 */
	public SysWsParams getParamModel(int id){
		String cacheKey = "getParamModelint"+id;
		SysWsParams model = (SysWsParams) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == model){
			model = (SysWsParams)this.getHibernateTemplate().get(SysWsParams.class, id);

			// 缓存
			if(null != model){
				WebServiceModelCache.getInstance().put(cacheKey, model);
			}
		}
		return model;
	}
	
	/**
	 * 得到最大的orderindex
	 * 
	 * @return
	 */
	public int getMaxParamsOrderIndex() {
		int max = 0;
		String hql = "SELECT MAX(orderIndex)+1 FROM SysWsParams";
		List list = this.getHibernateTemplate().find(hql);
		if (list != null && list.size() != 0) {
			Object obj = list.get(0);
			if (obj != null) {
				max = Integer.parseInt(obj.toString());
			}
		}
		return max;
	}

	/**
	 * 调整排序
	 * 
	 * @param type
	 *            'up'表示上移，'down'表示下移
	 * @param ireportId
	 * @param id
	 */
	public void updateOrderIndex(String type, int pid, String inorout, int id) {
		SysWsParams snd1 = null;
		SysWsParams snd2 = null;
		int temp = 0;
		DBUtilInjection d=new DBUtilInjection();
		   
	    if ((inorout != null) && (!"".equals(inorout))) {
        	if(d.HasInjectionData(inorout)){
				return;
			}
          
        }
		snd1 = this.getParamModel(id);
		String sql = "";
		Object[] values = null;
		if (type.equals("up")) {
			sql = " FROM SysWsParams WHERE  pid = ? and inorout = ? and orderIndex < ? order by orderIndex desc";
		} else if (type.equals("down")) {
			sql = " FROM SysWsParams WHERE  pid = ? and inorout = ? and orderIndex > ? order by orderIndex asc";
		} else if (type.equals("top")) {
			sql = " FROM SysWsParams WHERE  pid = ? and inorout = ? and orderIndex < ? order by orderIndex asc";
		} else if (type.equals("bottom")) {
			sql = " FROM SysWsParams WHERE  pid = ? and inorout = ? and orderIndex > ? order by orderIndex desc";
		}
		values = new Object[3];
		values[0] = pid;
		values[1] = inorout;
		values[2] = snd1.getOrderIndex();
		List<SysWsParams> list = this.getHibernateTemplate().find(sql,values);
		if (list != null) {
			if (list.size() > 0) {
				snd2 = (SysWsParams) list.get(0);
			}
		}
		if (snd1 != null && snd2 != null) {
			// 如果排序编号未错乱，正常调整排序
			if (snd1.getOrderIndex() != snd2.getOrderIndex()) {
				temp = snd1.getOrderIndex();
				snd1.setOrderIndex(snd2.getOrderIndex());
				// 执行更新动作
				this.getHibernateTemplate().update(snd1);
				snd2.setOrderIndex(temp);
				// 执行更新动作
				this.getHibernateTemplate().update(snd2);
			}
		}
		
		// 清空缓存
		removeAllCache(getModel(pid));
	}

	/**
	 * 根据URI获取模型列表
	 * @param URI
	 * @return
	 */
	public List<SysWsBaseinfo> getModelListByURI(String URI) {
		
		String cacheKey = "getModelListByURIString"+URI;
		List<SysWsBaseinfo> list = (List<SysWsBaseinfo>) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == list){
			String hql = "From SysWsBaseinfo where url like ?";
			DBUtilInjection d=new DBUtilInjection();
			   
		    if ((URI != null) && (!"".equals(URI))) {
	        	if(d.HasInjectionData(URI)){
					return list;
				}
	          
	        }
			Object[] values = {"%" + URI + "%"};
			list = this.getHibernateTemplate().find(hql,values);

			// 缓存
			if(null != list){
				WebServiceModelCache.getInstance().put(cacheKey, list);
			}
		}
		return list;
	}
	
	/**
	 * 根据UUID，获取对此webservice的配置信息
	 * @param UUID
	 * @return
	 */
	public List<SysWsBaseinfo> getModelListByUUID(String UUID){
		
		String cacheKey = "getModelListByUUIDString"+UUID;
		
		List<SysWsBaseinfo> list = (List<SysWsBaseinfo>) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == list){
			String hql = "From SysWsBaseinfo where UUID = ?";
			DBUtilInjection d=new DBUtilInjection();
			   
		    if ((UUID != null) && (!"".equals(UUID))) {
	        	if(d.HasInjectionData(UUID)){
					return list;
				}
	          
	        }
			Object[] values = {UUID};
			list = this.getHibernateTemplate().find(hql,values);

			// 缓存
			if(null != list){
				WebServiceModelCache.getInstance().put(cacheKey, list);
			}
		}
		return list;
	};
	
	/**
	 * 根据UUID，获取WebService的配置信息
	 * @param UUID
	 * @return
	 */
	public SysWsBaseinfo getModelByUUID(String UUID){
		String cacheKey = "getModelByUUIDString"+UUID;
		
		SysWsBaseinfo model = (SysWsBaseinfo) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == model){
			List<SysWsBaseinfo> list = getModelListByUUID(UUID);
			if(null != list && list.size()> 0){
				model = list.get(0);
			}

			// 缓存
			if(null != model){
				WebServiceModelCache.getInstance().put(cacheKey, model);
			}
		}
		return model;
	};
	
	/**
	 * 根据URI，获取WebService的配置信息
	 * @param URI
	 * @return
	 */
	public SysWsBaseinfo getModelByURI(String URI){
		
		String cacheKey = "getModelByURIString"+URI;
		
		SysWsBaseinfo model = (SysWsBaseinfo) WebServiceModelCache.getInstance().get(cacheKey);
		
		if(null == model){
			List<SysWsBaseinfo> list = getModelListByURI(URI);
			if(null != list && list.size()> 0){
				model = list.get(0);
			}

			// 缓存
			if(null != model){
				WebServiceModelCache.getInstance().put(cacheKey, model);
			}
		}
		return model;
	};
	
}
