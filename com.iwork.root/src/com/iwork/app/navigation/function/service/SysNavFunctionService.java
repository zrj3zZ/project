package com.iwork.app.navigation.function.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.app.navigation.directory.dao.SysNavDirectoryDAO;
import com.iwork.app.navigation.directory.model.SysNavDirectory;
import com.iwork.app.navigation.function.dao.SysNavFunctionDAO;
import com.iwork.app.navigation.function.model.Sysnavfunction;
import com.iwork.app.navigation.sys.dao.SysNavSystemDAO;
import com.iwork.app.navigation.sys.model.SysNavSystem;

public class SysNavFunctionService {
	private SysNavFunctionDAO sysNavFunctionDAO;
	private SysNavSystemDAO sysNavSystemDAO;
	private SysNavDirectoryDAO sysNavDirectoryDAO;
	
	public void addBoData(Sysnavfunction obj) {
		sysNavFunctionDAO.addBoData(obj);
	} 

	public void deleteBoData(Long id) {
		Sysnavfunction model=sysNavFunctionDAO.getBoData(id);
		sysNavFunctionDAO.deleteBoData(model);
	}

	public List getAll() {
		// TODO Auto-generated method stub
		return sysNavFunctionDAO.getAll();
	}

	public Sysnavfunction getBoData(Long id) {
		// TODO Auto-generated method stub
		return sysNavFunctionDAO.getBoData(id);
	} 

	public List getBoDatas(int pageSize, int startRow) {
		// TODO Auto-generated method stub
		return sysNavFunctionDAO.getBoDatas(pageSize, startRow);
	}

	public List<Sysnavfunction> getFunctionList(Long directoryId) {
		// TODO Auto-generated method stub
		return sysNavFunctionDAO.getFunctionList(directoryId);
	}

	public String getMaxID() {
		// TODO Auto-generated method stub
		return sysNavFunctionDAO.getMaxID();
	}

	public int getRows(Long directoryId) {
		// TODO Auto-generated method stub
		return sysNavFunctionDAO.getRows(directoryId);
	}

	public void updateBoData(Sysnavfunction obj) {
		sysNavFunctionDAO.updateBoData(obj);

	}

	public void setSysNavFunctionDAO(SysNavFunctionDAO sysNavFunctionDAO) {
		this.sysNavFunctionDAO = sysNavFunctionDAO;
	}
	
	
	
	/**
	 * 加载异步树的Sys节点
	 * @return
	 */
	public String getSys_FunTreeJson(String nodeId){
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
					item.put("iconCls","icon-ftv2folderopen");
					Map<String,Object> attributes = new HashMap<String,Object>();
					//装载导航信息
					attributes.put("url","directory_list.action?queryName=SYSTEM_ID&queryValue="+sns.getId());
					attributes.put("target","navlistFrame");
					attributes.put("type", "sys");
					item.put("attributes", attributes);
					rows.add(item);
				}
			}
			JSONArray json = JSONArray.fromObject(rows);
			html.append(json);
			operTree = "[{\"id\":\"0\",\"text\":\"导航模块\",\"iconCls\":\"icon-diffFolder\",\"attributes\":{\"target\":\"navlistFrame\",\"type\":\"\",\"url\":\"sysindex.action\"},\"children\":"+html.toString()+"}]";
		}
		return operTree;
	}
	/**
	 * 加载异步树的Dir节点
	 * @param nodeId
	 * @return
	 */
	public String getDir_FunTreeJson(String nodeId){
		StringBuffer html = new StringBuffer();
		List list = sysNavDirectoryDAO.getDirectoryList();
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		if(list!=null){
			for(int i=0;i<list.size();i++){
				if(list.get(i)==null)continue;
				SysNavDirectory snd=(SysNavDirectory)list.get(i);
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", snd.getId());
				item.put("text",snd.getDirectoryName());
				int count = sysNavFunctionDAO.getRows(snd.getId());
				if(count>0){
					item.put("state", "closed");
				}
				item.put("iconCls","icon-ftv2folderopen");
				Map<String,Object> attributes = new HashMap<String,Object>();
				//装载导航信息
				attributes.put("url","function_list.action?queryName=DIRECTORY_ID&queryValue="+snd.getId());
				attributes.put("target","navlistFrame");
				attributes.put("type", "dir");
				item.put("attributes", attributes);
				rows.add(item);
			}
		}
		JSONArray json = JSONArray.fromObject(rows);
		html.append(json);
		return html.toString();
	}
	/**
	 * 加载异步树的Fun节点
	 * @param nodeId
	 * @return
	 */
	public String getFun_FunTreeJson(Long directory){
		StringBuffer html = new StringBuffer();
		List list = sysNavFunctionDAO.getSubFunctionList(directory);
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		if(list!=null){
			for(int i=0;i<list.size();i++){
				if(list.get(i)==null)continue;
				Sysnavfunction snf = (Sysnavfunction)list.get(i);
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", snf.getDirectoryId());
				item.put("text",snf.getFunctionName());
				item.put("iconCls","icon-ftv2link");
				Map<String,Object> attributes = new HashMap<String,Object>();
				//装载导航信息
				attributes.put("url",snf.getId());
				attributes.put("target","navlistFrame");
				attributes.put("type","fun");
				item.put("attributes", attributes);
				rows.add(item);
			}
		}
		JSONArray json = JSONArray.fromObject(rows);
		html.append(json);
		return html.toString();
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
		js.append(key).append(id).append(" = insFld(").append(parentkey).append(", gFld (\"").append(name).append("\", \"").append(url).append("\", \"ftv2folderopen.gif\", \"ftv2folderclosed.gif\"));").append("\n");
		
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
		sysNavFunctionDAO.updateIndex(id, type);
	}
	
	/**
	 * 向下移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveDown(int id){
		String type="down";
		sysNavFunctionDAO.updateIndex(id, type);
	}
	public void setSysNavSystemDAO(SysNavSystemDAO sysNavSystemDAO) {
		this.sysNavSystemDAO = sysNavSystemDAO;
	}

	public void setSysNavDirectoryDAO(SysNavDirectoryDAO sysNavDirectoryDAO) {
		this.sysNavDirectoryDAO = sysNavDirectoryDAO;
	}
	
}
