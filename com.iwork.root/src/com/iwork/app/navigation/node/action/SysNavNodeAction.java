package com.iwork.app.navigation.node.action;

import java.util.List;

import com.iwork.app.constant.NavFilterTypeConstant;
import com.iwork.app.navigation.node.model.SysNavNode;
import com.iwork.app.navigation.node.service.SysNavNodeService;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.app.navigation.sys.service.SysNavSystemService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class SysNavNodeAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private SysNavNode model;
	private String id;
	private String systemId;
	private String parentNodeId;
	private List<SysNavNode> childenList;//子节点的LIST
	private SysNavNodeService sysNavNodeService;
	private SysNavSystemService sysNavSystemService;
	private String nodeType;
	private String searchkey;
	private String nodeId;
	private String parentNodeName;//上级目录名称
	
	/**
	 * 加载导航模块首页
	 * @return
	 * @throws Exception
	 */
	public String index() throws Exception{
		return SUCCESS;
	}
	
	/**
	 * 加载导航树
	 * @return
	 * @throws Exception
	 */
	public String showTree() throws Exception{
		return SUCCESS;
	}
	
	/**
	 * 加载导航树JSON
	 * @return
	 * @throws Exception
	 */
	public String showTree_Json() throws Exception{
		String JsonData = "";
		nodeId = id; 
		if(nodeType==null)this.setNodeType("");
		if(null==nodeId||"".equals(nodeId))this.setNodeId("0");
		 
		if("SYS".equals(nodeType)||"NODE".equals(nodeType)){
			JsonData = sysNavNodeService.getNode_FunTreeJson(nodeId,nodeType,NavFilterTypeConstant.NAV_FILTER_PURVIEW);
		}else{
			JsonData = sysNavNodeService.getSys_FunTreeJson(nodeId,NavFilterTypeConstant.NAV_FILTER_PURVIEW);
		} 
		ResponseUtil.write(JsonData);
		this.setNodeId("");
		this.setNodeType("");
		return null; 
	}
	/**
	 * 获得子节点的LIST
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		//获得子系统下的子节点的LIST
		if(systemId!=null&&!"".equals(systemId)&&!"null".equals(systemId)){
			List<SysNavNode> sys_list = sysNavNodeService.getChildListBySystemId(systemId);
			this.setChildenList(sys_list);
		}else if(parentNodeId!=null&&!"".equals(parentNodeId)&&!"null".equals(parentNodeId)){//获得普通节点下的子节点的LIST
				List<SysNavNode> node_List = sysNavNodeService.getChildListByParentId(parentNodeId);
				this.setChildenList(node_List);
		}
		return SUCCESS;
	}
	
	/**
	 * 添加一个子节点
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception{
		SysNavNode temp = new SysNavNode();
		if(systemId!=null&&!"".equals(systemId)){
			temp.setSystemId(Long.parseLong(systemId));
			SysNavSystem sysModel = sysNavSystemService.getBoData(systemId);
			this.setParentNodeName(sysModel.getSysName());
		}
		if(parentNodeId!=null&&!"".equals(parentNodeId)){
			temp.setParentNodeId(Long.parseLong(parentNodeId));
			SysNavNode nodeModel = sysNavNodeService.getBoData(Long.parseLong(parentNodeId));
			this.setParentNodeName(nodeModel.getNodeName());
		}
		temp.setOrderindex(Long.parseLong(sysNavNodeService.getMaxId()));
		this.setModel(temp);
		return SUCCESS;
	}
	
	/**
	 * 保存子系统信息
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		Long idTemp = this.getModel().getId();
		if(idTemp == null){
			sysNavNodeService.addBoData(this.getModel());
		}else{
			sysNavNodeService.updateBoData(this.getModel());
		}
 	    return SUCCESS;
	}
	
	/**
	 * 编辑加载一个子节点
	 * @return
	 * @throws Exception
	 */
	public String load() throws Exception{
		String id = this.getId();
		SysNavNode node = sysNavNodeService.getBoData(Long.parseLong(id));
		//加载上级目录名称
		if(node!=null){
			if(node.getSystemId()!=null){
				SysNavSystem sysModel = sysNavSystemService.getBoData(node.getSystemId().toString());
				this.setParentNodeName(sysModel.getSysName());
			}else if(node.getParentNodeId()!=null){
				SysNavNode parentNode = sysNavNodeService.getBoData(node.getParentNodeId());
				this.setParentNodeName(parentNode.getNodeName());
			}else{
				this.setParentNodeName("");
			}
			this.setModel(node);
		}
		return SUCCESS;
	}
	
	/**
	 * 删除一个子节点
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception{
		String id = this.getId();
		SysNavNode node = sysNavNodeService.getBoData(Long.parseLong(id));
		if(node!=null){
			sysNavNodeService.deleteBoData(node);
			//为页面跳转做准备
			if(node.getParentNodeId()!=null){
				this.setParentNodeId(node.getParentNodeId().toString());
			}
			if(node.getSystemId()!=null){
				this.setSystemId(node.getSystemId().toString());
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 向上移动
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception{
		if(this.getId()!=null&&!"".equals(this.getId())){
			int id = Integer.parseInt(this.getId());
			sysNavNodeService.moveUp(id);
			SysNavNode model = sysNavNodeService.getBoData(new Long(id));
			if(model!=null){
				this.setParentNodeId(model.getParentNodeId()+"");
				this.setSystemId(model.getSystemId()+"");
			}
			
			
		}
		return SUCCESS;
	}
	
	/**
	 * 向下移动
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception{
		if(this.getId()!=null&&!"".equals(this.getId())){
			int id = Integer.parseInt(this.getId());
			sysNavNodeService.moveDown(id);
			SysNavNode model = sysNavNodeService.getBoData(new Long(id));
			if(model!=null){
				this.setParentNodeId(model.getParentNodeId()+"");
				this.setSystemId(model.getSystemId()+"");
			}
		}
		return SUCCESS;
	}
	
	//====================================POJO========================================
	public SysNavNode getModel() {
		return model;
	}

	public void setModel(SysNavNode model) {
		this.model = model;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public List<SysNavNode> getChildenList() {
		return childenList;
	}

	public void setChildenList(List<SysNavNode> childenList) {
		this.childenList = childenList;
	}

	public SysNavNodeService getSysNavNodeService() {
		return sysNavNodeService;
	}

	public void setSysNavNodeService(SysNavNodeService sysNavNodeService) {
		this.sysNavNodeService = sysNavNodeService;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getParentNodeName() {
		return parentNodeName;
	}

	public void setParentNodeName(String parentNodeName) {
		this.parentNodeName = parentNodeName;
	}

	public SysNavSystemService getSysNavSystemService() {
		return sysNavSystemService;
	}

	public void setSysNavSystemService(SysNavSystemService sysNavSystemService) {
		this.sysNavSystemService = sysNavSystemService;
	}

	public String getSearchkey() {
		return searchkey;
	}
	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}
}
