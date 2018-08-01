package com.iwork.app.navigation.operation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.app.navigation.directory.dao.SysNavDirectoryDAO;
import com.iwork.app.navigation.function.dao.SysNavFunctionDAO;
import com.iwork.app.navigation.operation.dao.SysNavOperationDAO;
import com.iwork.app.navigation.operation.model.SysNavOperation;
import com.iwork.app.navigation.sys.dao.SysNavSystemDAO;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.commons.constant.SecurityConstant;

public class SysNavOperationService {
	private SysNavOperationDAO sysNavOperationDAO;
	private SysNavFunctionDAO sysNavFunctionDAO;
	private SysNavSystemDAO sysNavSystemDAO;
	private SysNavDirectoryDAO sysNavDirectoryDAO;
	
	public void addBoData(SysNavOperation obj) {
		sysNavOperationDAO.addBoData(obj);
	} 

	public void deleteBoData(String id) {
		SysNavOperation model=sysNavOperationDAO.getBoData(id);
		sysNavOperationDAO.deleteBoData(model);
	}

	public List getAll() {
		// TODO Auto-generated method stub
		return sysNavOperationDAO.getAll();
	}

	public SysNavOperation getBoData(String id) {
		// TODO Auto-generated method stub
		return sysNavOperationDAO.getBoData(id);
	} 

	public List getBoDatas(int pageSize, int startRow) {
		// TODO Auto-generated method stub
		return sysNavOperationDAO.getBoDatas(pageSize, startRow);
	}

	public List getBoDatas(String fieldname, String value, int pageSize,
			int startRow) {
		// TODO Auto-generated method stub
		return sysNavOperationDAO.getBoDatas(fieldname, value, pageSize, startRow);
	}

	public String getMaxID() {
		// TODO Auto-generated method stub
		return sysNavOperationDAO.getMaxID();
	}

	public int getRows() {
		// TODO Auto-generated method stub
		return sysNavOperationDAO.getRows();
	}

	public int getRows(String fieldname, String value) {
		// TODO Auto-generated method stub
		return sysNavOperationDAO.getRows(fieldname, value);
	}

	public List queryBoDatas(String fieldname, String value) {
		// TODO Auto-generated method stub
		return sysNavOperationDAO.queryBoDatas(fieldname, value);
	}  

	public void updateBoData(SysNavOperation obj) {
		sysNavOperationDAO.updateBoData(obj);

	}

	public void setSysNavFunctionDAO(SysNavFunctionDAO sysNavFunctionDAO) {
		this.sysNavFunctionDAO = sysNavFunctionDAO;
	}
	
	
	
	/**
	 * 加载异步树的Sys节点
	 * @return
	 */
	public String getSysOperTreeJson(String nodeId){
		StringBuffer html = new StringBuffer();
		String operTree = "";
		if(nodeId.equals("0")){
			List systemList =  sysNavSystemDAO.getAll();
			List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
			if(systemList!=null){
				for(int i=0;i<systemList.size();i++){//加载一级节点
					if(systemList.get(i)==null)continue;
					SysNavSystem sns = (SysNavSystem)systemList.get(i);
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("id", sns.getId());
					item.put("text", sns.getSysName());
					int count = sysNavDirectoryDAO.getRows("SYSTEM_ID", sns.getId());
					if(count>0){
						item.put("state", "closed");
					}
					item.put("iconCls","icon-macfloder");
					Map<String,Object> attributes = new HashMap<String,Object>();
					//装载导航信息
					attributes.put("url","operation_list.action?ptype="+SecurityConstant.NAVTYPE1+"&&pid="+sns.getId());
					attributes.put("target", "navOPeration_listFrame");
					attributes.put("type", "sys");
					item.put("attributes", attributes);
					rows.add(item);
				}
			}
			JSONArray json = JSONArray.fromObject(rows);
			html.append(json);
			operTree = "[{\"id\":\"0\",\"text\":\"导航模块\",\"iconCls\":\"icon-diffFolder\",\"children\":"+html.toString()+"}]";
		}
		return operTree;
	}
	
	
	
	/**
	 * 获得中间节点脚本
	 * @param id
	 * @param name
	 * @param url
	 * @param parentkey
	 * @return
	 */
	private String getFolderScript(String id,String name,String url,String parentkey,String key){
		StringBuffer js = new StringBuffer();
		js.append(key).append(id).append(" = insFld(").append(parentkey).append(", gFld (\"").append(name).append("\", \"").append(url).append("\", \"macfloder.gif\", \"macfloder.gif\"));").append("\n");
		
		return js.toString();
	}
	
	/**
	 * 获得叶子节点
	 * @param id
	 * @param name
	 * @param url
	 * @param parentkey
	 * @param key
	 * @return
	 */
	private String getEndNodeScript(String id,String name,String url,String parentkey,String key){
		StringBuffer js = new StringBuffer();
		js.append(key).append(id).append(" = insFld(").append(parentkey).append(", gLnk(\"0\",\"").append(name).append("\", \"").append(url).append("\", \"ftv2link.gif\"));").append("\n");
		return js.toString();
	}
	/**
	 * 向上移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveUp(int id){
		String type="up";
		sysNavOperationDAO.updateIndex(id, type);
	}
	
	/**
	 * 向下移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveDown(int id){
		String type="down";
		sysNavOperationDAO.updateIndex(id, type);
	}
	public void setSysNavSystemDAO(SysNavSystemDAO sysNavSystemDAO) {
		this.sysNavSystemDAO = sysNavSystemDAO;
	}

	public void setSysNavDirectoryDAO(SysNavDirectoryDAO sysNavDirectoryDAO) {
		this.sysNavDirectoryDAO = sysNavDirectoryDAO;
	}

	public void setSysNavOperationDAO(SysNavOperationDAO sysNavOperationDAO) {
		this.sysNavOperationDAO = sysNavOperationDAO;
	}
}
