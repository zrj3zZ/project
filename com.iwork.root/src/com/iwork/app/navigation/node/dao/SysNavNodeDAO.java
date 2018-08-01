package com.iwork.app.navigation.node.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.navigation.node.cache.SysNavNodeCache;
import com.iwork.app.navigation.node.model.SysNavNode;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UUIDUtil;
import com.iwork.core.security.NavSecurityUtil;
/**
 * 子系统对应目录管理表数据库操作类
 *
 */
public class SysNavNodeDAO extends HibernateDaoSupport{

	/**
	 * 添加一个对象
	 * @param model
	 */
	public void addBoData(SysNavNode model){
		if(model!=null){
			if(model.getNodeUuid()==null||"".equals(model.getNodeUuid())){
				model.setNodeUuid(UUIDUtil.getUUID());
			}
			this.getHibernateTemplate().save(model);
			//装载cache
			SysNavNodeCache.getInstance().putModel(model);
			SysNavNodeCache.getInstance().removeList(String.valueOf(model.getParentNodeId()));
			SysNavNodeCache.getInstance().removeList(String.valueOf(model.getId()));
		}
	}
	
	/**
	 * 删除一个对象
	 * @param model
	 */
	public void deleteBoData(SysNavNode model){
		this.getHibernateTemplate().delete(model);
		SysNavNodeCache.getInstance().removeModel(String.valueOf(model.getId()));
		SysNavNodeCache.getInstance().removeList(String.valueOf(model.getParentNodeId()));
		SysNavNodeCache.getInstance().removeList(String.valueOf(model.getId()));
	}
	
	/**
	 * 函数说明：修改信息
	 * 参数说明： 对象
	 * 返回值：
	 */
	public void updateBoData(SysNavNode model) {
		if(model!=null){
			SysNavNode temp = this.getBodata(model.getId());
			SysNavNodeCache.getInstance().removeList(String.valueOf(temp.getParentNodeId()));
			if(model.getNodeUuid()==null||"".equals(model.getNodeUuid())){
				model.setNodeUuid(UUIDUtil.getUUID());
			}
			this.getHibernateTemplate().update(model); 
		}
		
		//装载cache
		//SysNavNodeCache.getInstance().putModel(model);
		//重新装载LIST cache 
		SysNavNodeCache.getInstance().removeList(String.valueOf(model.getParentNodeId()));
		SysNavNodeCache.getInstance().removeList(String.valueOf(model.getId()));
		SysNavNodeCache.getInstance().removeModel(String.valueOf(model.getId()));
	}
	

	
	/**
	 * 获得所有的信息
	 * @return 所有信息的集合
	 * 访问量较低不做cache装载
	 */
	public List<SysNavNode> getAll(){
		String sql="FROM "+SysNavNode.DATABASE_ENTITY+" ORDER BY ORDERINDEX";
		return this.getHibernateTemplate().find(sql);
	}
	
	/**
	 * 获一条信息
	 * @param id
	 * @return
	 */
	public SysNavNode getBodata(Long id){
		SysNavNode sysNavNode = null;
		if(id==null)id=new Long(0);
		try{
			sysNavNode = (SysNavNode)SysNavNodeCache.getInstance().getModel(id.toString());
			if(sysNavNode!=null){
				return sysNavNode;
			}else{
				return (SysNavNode)this.getHibernateTemplate().get(SysNavNode.class,id);
			}
		}catch(RuntimeException e){logger.error(e,e);
			return (SysNavNode)this.getHibernateTemplate().get(SysNavNode.class,id);
		}
	}
	
	/**
	 * 获得最大ID
	 * @return
	 */
	public String getMaxId(){
		String sql="SELECT MAX(ID)+1 FROM "+SysNavNode.DATABASE_ENTITY;
		List list = this.getHibernateTemplate().find(sql);
		Integer maxId = (Integer)list.get(0);
		return maxId.toString();
	}
	
	/**
	 * 获得最大ID
	 * @return
	 */
	public String getMaxOrderIndex(){
		String sql="SELECT MAX(ORDERINDEX)+1 FROM "+SysNavNode.DATABASE_ENTITY;
		List list = this.getHibernateTemplate().find(sql);
		Integer orderindex = (Integer)list.get(0);
		return orderindex.toString();
	}
	
	/**
	 * 获得父节点下所有子节点的信息
	 * @param parentNodeId
	 * @return
	 */
	public List<SysNavNode> getChildListByParentId(String parentNodeId){
		List<SysNavNode> list = null;
		String sql = "FROM "+SysNavNode.DATABASE_ENTITY+" where PARENT_NODE_ID = ? ORDER BY ORDERINDEX";
		try{
			list = SysNavNodeCache.getInstance().getList(parentNodeId);
			List<SysNavNode> resultList=new ArrayList<SysNavNode>();
			for (SysNavNode sysNavNode : list) {
				boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(sysNavNode);
				if(flag){
					resultList.add(sysNavNode);
				}
			}			
			return resultList;			
		}catch(RuntimeException e){
			DBUtilInjection d=new DBUtilInjection();
			List l=new ArrayList();
			if(parentNodeId!=null&&!"".equals(parentNodeId)){
				if(d.HasInjectionData(parentNodeId)){
					return l;
				}
			}
			Object[] values = {parentNodeId};
			list=this.getHibernateTemplate().find(sql,values);
			SysNavNodeCache.getInstance().putList(parentNodeId, list);
			return list;
		}
	}
	
	
	/**
	 * 获得系统节点下的所有子节点信息
	 * @param systemId
	 * @return
	 * 访问量较低不做Cache装载
	 */
	public List<SysNavNode> getSearchList(String searchkey){
		String sql = "FROM "+SysNavNode.DATABASE_ENTITY+" where nodeName like ? OR nodeDesc like ? ORDER BY ORDERINDEX";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(searchkey!=null&&!"".equals(searchkey)){
			if(d.HasInjectionData(searchkey)){
				return l;
			}
		}
		Object[] values = {"%"+searchkey+"%","%"+searchkey+"%"};
		List<SysNavNode> list = this.getHibernateTemplate().find(sql);
		return list;
	}
	/**
	 * 获得系统节点下的所有子节点信息
	 * @param systemId
	 * @return
	 * 访问量较低不做Cache装载
	 */
	public List<SysNavNode> getChildListBySystemId(String systemId){
		String sql = "FROM "+SysNavNode.DATABASE_ENTITY+" where SYSTEM_ID = ? ORDER BY ORDERINDEX";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(systemId!=null&&!"".equals(systemId)){
			if(d.HasInjectionData(systemId)){
				return l;
			}
		}
		Object[] values = {systemId};
		List<SysNavNode> list = this.getHibernateTemplate().find(sql,values);
		return list;
	}
	/**
	 * 获得系统节点下的所有子节点信息
	 * @param systemId
	 * @return
	 * 访问量较低不做Cache装载
	 */
	public int getChildCountBySystemId(String systemId){
		String sql = "FROM "+SysNavNode.DATABASE_ENTITY+" where SYSTEM_ID = ? ORDER BY ORDERINDEX";
		DBUtilInjection d=new DBUtilInjection();
		if(systemId!=null&&!"".equals(systemId)){
			if(d.HasInjectionData(systemId)){
				return 0;
			}
		}
		Object[] values = {systemId};
		List<SysNavNode> list = this.getHibernateTemplate().find(sql,values);
		return list.size();
	}
	
	/**
	 * 调整模块排序
	 * @param orderindex
	 * @return
	 */
	public void updateIndex(int id,String type){
		SysNavNode snd1 = null;
		SysNavNode snd2 = null;;
		Long temp = null;
		String sql = "FROM "+SysNavNode.DATABASE_ENTITY+" WHERE ID =?";
		Object[] values = {id};
		List downlist=this.getHibernateTemplate().find(sql,values);
		if(downlist!=null){
			if(downlist.size()>0){
				snd1 = (SysNavNode)downlist.get(0);
			}
		}
		Object[] value = new Object[2];;
		if(snd1.getParentNodeId()!=null){
			if(type.equals("up")){
				sql = "FROM "+SysNavNode.DATABASE_ENTITY+" WHERE PARENT_NODE_ID=? and orderindex < ? order by orderindex desc";
				value[0]=snd1.getParentNodeId();
				value[1]=snd1.getOrderindex();
				
			}else{
				sql = "FROM "+SysNavNode.DATABASE_ENTITY+" WHERE PARENT_NODE_ID=? and orderindex > ? order by orderindex asc";
				value[0]=snd1.getParentNodeId();
				value[1]=snd1.getOrderindex();
			}
		}
		if(snd1.getSystemId()!=null){
			if(type.equals("up")){
				sql = "FROM "+SysNavNode.DATABASE_ENTITY+" WHERE SYSTEM_ID=? and orderindex < ? order by orderindex desc";
				value[0]=snd1.getSystemId();
				value[1]=snd1.getOrderindex();
			}else{
				sql = "FROM "+SysNavNode.DATABASE_ENTITY+" WHERE SYSTEM_ID=? and orderindex > ? order by orderindex asc";
				value[0]=snd1.getSystemId();
				value[1]=snd1.getOrderindex();
			}
		}
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(value[0]!=null&&!"".equals(value[0])){
			if(d.HasInjectionData(value[0].toString())){
				return;
			}
		}
		if(value[1]!=null&&!"".equals(value[1])){
			if(d.HasInjectionData(value[1].toString())){
				return;
			}
		}
		List uplist=this.getHibernateTemplate().find(sql,value);
			if(uplist!=null){
				if(uplist.size()>0){
					snd2 = (SysNavNode)uplist.get(0);
				}
			}
			if(snd1!=null&&snd2!=null){
				temp = snd1.getOrderindex();
				snd1.setOrderindex(snd2.getOrderindex());
				//执行更新动作
				this.updateBoData(snd1);
				snd2.setOrderindex(temp);
				this.updateBoData(snd2);
			}
	}
	
	
}
