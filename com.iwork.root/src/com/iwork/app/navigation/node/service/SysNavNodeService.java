package com.iwork.app.navigation.node.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.app.constant.NavFilterTypeConstant;
import com.iwork.app.navigation.node.dao.SysNavNodeDAO;
import com.iwork.app.navigation.node.model.SysNavNode;
import com.iwork.app.navigation.sys.dao.SysNavSystemDAO;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.core.security.NavSecurityUtil;
import com.iwork.eaglesearch.constant.EaglesSearchConstant;
import com.iwork.eaglesearch.factory.EaglesSearchFactory;
import com.iwork.eaglesearch.impl.EaglesSearchNavigationImpl;
import com.iwork.eaglesearch.model.NavIndexModel;

public class SysNavNodeService {
	private SysNavNodeDAO sysNavNodeDAO;
	private SysNavSystemDAO sysNavSystemDAO;
	
	/**
	 * 获得单个对象
	 * @param id
	 * @return
	 */
	public SysNavNode getBoData(Long id){
		return sysNavNodeDAO.getBodata(id);
	}
	
	/**
	 * 保存单个对象
	 * @param model
	 */
	public void addBoData(SysNavNode model){
		if(model!=null){
			sysNavNodeDAO.addBoData(model);
		}
		//添加索引
		this.addIndex(model); 
	}
	
	/**
	 * 更新单个对象
	 * @param model
	 */
	public void updateBoData(SysNavNode model){
		if(model!=null){
			sysNavNodeDAO.updateBoData(model);
		}
		//更新索引
		this.updateIndex(model);
	}
	
	/**
	 * 删除单个对象
	 * @param model
	 */
	public void deleteBoData(SysNavNode model){
		sysNavNodeDAO.deleteBoData(model);
		//移除索引
		this.delIndex(model);
	}
	/**
	 * 添加索引
	 * @param model
	 */
	private void addIndex(SysNavNode snn){
		if(snn!=null){
			//判断是否有子菜单节点，如果有，则不添加索引
			List<SysNavNode> list = this.getChildListByParentId(snn.getId().toString());
			if(list!=null&&list.size()>0){
				return;
			}
			//获得子系统名称
			EaglesSearchNavigationImpl esfdi =  (EaglesSearchNavigationImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_NAV_INDEX);
			NavIndexModel model = new NavIndexModel();
			model.setId(snn.getId()+"");
			model.setIcon(snn.getNodeIcon());
			model.setTarget(snn.getNodeTarget());
			model.setTitle(snn.getNodeName());
			String path =""; //
			Long parentId = snn.getParentNodeId();
			//构建菜单全路径
			while(true){ 
				if(parentId==null||parentId.equals(new Long(0))){
					break;
				}
				SysNavNode parent_snn = this.getBoData(parentId);
				path = path+">>"+parent_snn.getNodeName();
				parentId = parent_snn.getParentNodeId();
				if(parent_snn.getSystemId()!=null&&!parent_snn.getSystemId().equals(new Long(0))){
					//获得子系统名称
					SysNavSystem sysNavSystem = sysNavSystemDAO.getBoData(parent_snn.getSystemId()+"");
					if(sysNavSystem!=null){
						path = sysNavSystem.getSysName()+path;
						break;
					}
				}
			}
			model.setContent(path);
			model.setUrl(snn.getNodeUrl());
			model.setPurviewtype(snn.getNodeType());
			esfdi.addDocument(model);
		}
		
	}
	/**
	 * 更新索引
	 * @param model
	 */ 
	private void updateIndex(SysNavNode snn){
		if(snn!=null){
			//判断是否有子菜单节点，如果有，则不添加索引
			List<SysNavNode> list = this.getChildListByParentId(snn.getId().toString());
			if(list!=null&&list.size()>0){
				return;
			}
			
			EaglesSearchNavigationImpl esfdi =  (EaglesSearchNavigationImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_NAV_INDEX);
			NavIndexModel model = new NavIndexModel();
			model.setId(snn.getId()+"");
			model.setIcon(snn.getNodeIcon());
			model.setTarget(snn.getNodeTarget());
			model.setTitle(snn.getNodeName());
			String path = ""; //
			Long parentId = snn.getParentNodeId();
			//构建菜单全路径
			while(true){ 
				if(parentId==null||parentId.equals(new Long(0))){
					break;
				}
				SysNavNode parent_snn = this.getBoData(parentId);
				path = path+">>"+parent_snn.getNodeName();
				parentId = parent_snn.getParentNodeId();
				if(parent_snn.getSystemId()!=null&&!parent_snn.getSystemId().equals(new Long(0))){
					//获得子系统名称
					SysNavSystem sysNavSystem = sysNavSystemDAO.getBoData(parent_snn.getSystemId()+"");
					if(sysNavSystem!=null){
						path = sysNavSystem.getSysName()+path;
						break;
					}
				}
			}
			model.setContent(path);
			model.setUrl(snn.getNodeUrl());
			model.setPurviewtype(snn.getNodeType());
			esfdi.updateDocument(model);
		}
		
	}
	
	/**
	 * 删除索引
	 * @param model
	 */
	private void delIndex(SysNavNode snn){
		if(snn!=null){
			EaglesSearchNavigationImpl esfdi =  (EaglesSearchNavigationImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_NAV_INDEX);
			esfdi.delDocuemnt(snn.getId()+"");
		}
	}
	
	/**
	 * 获得最大的ID
	 * @return
	 */
	public String getMaxId(){
		return sysNavNodeDAO.getMaxId();
	}
	
	/**
	 * 获得最大的OrderIndex
	 * @return
	 */
	public String getMaxOrderIndex(){
		return sysNavNodeDAO.getMaxOrderIndex();
	}
	
	
	/**
	 * 获得所有的子节点的LIST
	 * @return
	 */
	public List<SysNavNode> getAll(){
		return sysNavNodeDAO.getAll();
	}
	
	/**
	 * 获得父节点下的子节点的LIST
	 * @return
	 */
	public List<SysNavNode> getChildListByParentId(String parentNodeId){
		return sysNavNodeDAO.getChildListByParentId(parentNodeId);
	}
	
	/**
	 * 判断父节点下面是否存在子节点
	 * @param parentNodeId
	 * @return
	 */
	public boolean IsChildExsitsInParent(String parentNodeId){
		List<SysNavNode> list = sysNavNodeDAO.getChildListByParentId(parentNodeId);
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获得子系统下的子节点的LIST
	 * @param systemId
	 * @return
	 */
	public List<SysNavNode> getChildListBySystemId(String systemId){
		return sysNavNodeDAO.getChildListBySystemId(systemId);
	}
	
	/**
	 * 判断子系统下面是否存在子节点
	 * @param systemId
	 * @return
	 */
	public boolean IsChildExsitsInSystem(String systemId){
		List<SysNavNode> list = sysNavNodeDAO.getChildListBySystemId(systemId);
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 向上移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveUp(int id){
		String type="up";
		sysNavNodeDAO.updateIndex(id, type);
	}
	
	/**
	 * 向下移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveDown(int id){
		String type="down";
		sysNavNodeDAO.updateIndex(id, type);
	}
	
	/**
	 * 是否进行权限校验
	 * @param nodeId
	 * @param isPurview
	 * @return
	 */
	public String getSys_FunTreeJson(String nodeId,Long filterType){
		StringBuffer html = new StringBuffer();
		String operTree = "";
		if(nodeId.equals("0")){
			List systemList =  sysNavSystemDAO.getAll();
			List<Map<String,Object>> rootList = new ArrayList<Map<String,Object>>();
			Map<String,Object> root = new HashMap<String,Object>();
			root.put("id", "0");
			root.put("name","导航菜单目录");
			root.put("isParent", "true");
			root.put("open", "true");
			//判断权限
			root.put("iconOpen", "iwork_img/images/folder-open.gif");
			root.put("iconClose", "iwork_img/images/folder.gif");
			root.put("nodeType", "ROOT"); 
			
			List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
			if(systemList!=null){
				for(int i=0;i<systemList.size();i++){//加载一级节点
					if(systemList.get(i)==null)continue;
					SysNavSystem sns = (SysNavSystem)systemList.get(i);
					Map<String,Object> item = new HashMap<String,Object>();
					//用户对导航菜单进行授权时，将有权限的树节点设置复选框为“勾选”
					if(filterType!=null&&filterType.equals(NavFilterTypeConstant.NAV_FILTER_PURVIEW)){
						boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(sns);
						if(flag){
							item.put("checked",true);
						}else{
							item.put("checked",false);
						}
					}else if(filterType!=null&&filterType.equals(NavFilterTypeConstant.NAV_FILTER_NAV)){
						boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(sns);
						if(!flag){
							continue; 
						}
					}
					item.put("id", sns.getId());
					item.put("name", sns.getSysName());
					int count = sysNavNodeDAO.getChildListBySystemId(sns.getId()).size();
					if(count>0){
						item.put("isParent", "true");
					}else{
						item.put("isParent", "false");
					}
					//判断权限
					item.put("iconOpen", "iwork_img/images/folder-open.gif");
					item.put("iconClose", "iwork_img/images/folder.gif");
					item.put("nodeType", "SYS");
					item.put("target",sns.getSysTarget());
					item.put("pageurl",sns.getSysUrl()); 
					rows.add(item); 
				}
			}
			root.put("children", rows);
			rootList.add(root); 
			JSONArray json = JSONArray.fromObject(rootList);
			html.append(json);
		}
		return html.toString();
	}
	/**
	 * 获得功能模块JSON
	 * @param nodeId 节点ID
	 * @param nodeType 节点类型  
	 * @param isPurview  是否进行权限判断
	 * @return
	 */
	public String getNode_FunTreeJson(String nodeId,String nodeType,Long filterType){
		StringBuffer html = new StringBuffer();
		List<SysNavNode> list = null;
		if(nodeType.equals("SYS")){
			list = sysNavNodeDAO.getChildListBySystemId(nodeId);
		}else{
			 list = sysNavNodeDAO.getChildListByParentId(nodeId);
		}
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		if(list!=null){
			for(int i=0;i<list.size();i++){
				if(list.get(i)==null)continue;
				SysNavNode snd=(SysNavNode)list.get(i);
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", snd.getId());
				item.put("name",snd.getNodeName());
				item.put("iconOpen", "iwork_img/images/folder-open.gif");
				item.put("iconClose", "iwork_img/images/folder.gif");
				item.put("icon", "iwork_img/domain.gif");
				//用户对导航菜单进行授权时，将有权限的树节点设置复选框为“勾选”
				if(filterType!=null&&filterType.equals(NavFilterTypeConstant.NAV_FILTER_PURVIEW)){
					boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(snd);
					if(flag){
						item.put("checked",true);
					}else{
						item.put("checked",false);
					}
				}else if(filterType!=null&&filterType.equals(NavFilterTypeConstant.NAV_FILTER_NAV)){
					boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(snd);
					if(!flag){
						continue;
					}
				}
				int count = sysNavNodeDAO.getChildListByParentId(snd.getId().toString()).size();
				if(count>0){ 
					item.put("isParent", "true");
				}else{
					item.put("isParent", "false");
				}
				item.put("target",snd.getNodeTarget());
				item.put("pageurl",snd.getNodeUrl()); 
				item.put("nodeType", "NODE"); 
				rows.add(item);  
			}
		}
		JSONArray json = JSONArray.fromObject(rows);
		html.append(json);
		return html.toString();
	}
//===========================POJO====================================
	public SysNavNodeDAO getSysNavNodeDAO() {
		return sysNavNodeDAO;
	}

	public void setSysNavNodeDAO(SysNavNodeDAO sysNavNodeDAO) {
		this.sysNavNodeDAO = sysNavNodeDAO;
	}

	public SysNavSystemDAO getSysNavSystemDAO() {
		return sysNavSystemDAO;
	}

	public void setSysNavSystemDAO(SysNavSystemDAO sysNavSystemDAO) {
		this.sysNavSystemDAO = sysNavSystemDAO;
	}
	
}
