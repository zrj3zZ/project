package com.iwork.app.mobile.process.create.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.core.constant.SysConst;
import com.iwork.core.engine.group.dao.SysEngineGroupDAO;
import com.iwork.core.engine.group.model.SysEngineGroup;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.security.purview.service.SysPurviewProcessService;
import com.iwork.process.definition.deployment.dao.ProcessDeploymentDAO;
import com.iwork.process.definition.deployment.model.ProcessDefDeploy;
import com.iwork.process.definition.flow.dao.ProcessDefMapDAO;
import com.iwork.process.definition.flow.model.ProcessDefMap;
import com.iwork.process.deployment.dao.SysProcessDefinitionDAO;

/**
 * 移动端流程发起中心
 * @author chenM
 *
 */
public class SyncMobileProcessLaunchCenterService {
	private static final long serialVersionUID = 1L;
	private SysEngineGroupDAO sysEngineGroupDAO;   //流程分组模型
	private SysPurviewProcessService sysPurviewProcessService;
	private ProcessDeploymentDAO processDeploymentDAO;
	private SysProcessDefinitionDAO sysProcessDefinitionDAO;
	private ProcessDefMapDAO processDefMapDAO;
	public String getMobileProcessLaunchCenterHtml(){
		StringBuffer content = new StringBuffer();
		StringBuffer html = new StringBuffer();
		//获得首级分组列表
		List<String> securityList = sysPurviewProcessService.getCurrUserProcessList();
  		List<SysEngineGroup> list = sysEngineGroupDAO.getSysEngineGroupList(new Long(0));
//		list = delNoSubItem(list);
		int count = 0;
		if (list != null) {
			int size = list.size();
			for(int i=0;i<size;i++){
					SysEngineGroup model = list.get(i);
					String itemcontent = getProcessItemContent(model.getId(),securityList);
					if(itemcontent!=null&&!itemcontent.equals("")){
						html.append("<li data-role=\"list-divider\">");
						html.append(model.getGroupname());
						html.append("</li>");
						html.append(itemcontent);
						count++;
					}else{
						continue;
					}
			} 
			if(count==0){
				html.append("<div style='color:#333333;font-size:14px;'><img border='0' src='iwork_img/nondynamic.gif'>未发现权限范围内发起的流程</div>");
			}else{
				html.append(content);
			}
		}
		
		return html.toString();
	}
	/**
	 * 获得单条的HTML页面 
	 * @param model
	 * @return
	 */
	public String getItemHtml(SysEngineGroup model,List<String> securityList){
		StringBuffer html = new StringBuffer();
		String itemcontent = getProcessItemContent(model.getId(),securityList);
		if(itemcontent.equals("")){
			return "";
		}else{
			html.append("<li data-role=\"list-divider\">")
			
			.append(model.getGroupname()).append("</div>"); 
			html.append("		<div class=\"widget-content\">");
			html.append("<ul>\n");
			
			html.append(itemcontent);
			html.append("</ul>\n");
			html.append("</div></li>");
			return html.toString();
		}
		
	}
	/**
	 * 获得流程发起中心栏目报表列表
	 * 
	 * @param groupId
	 * @return
	 */
	private String getProcessItemContent(Long parentGroupId,List<String> securityList) {
		StringBuffer html = new StringBuffer();
		List<SysEngineGroup> groupList = sysEngineGroupDAO.getSysEngineGroupList(parentGroupId);
		if(groupList!=null&&groupList.size()>0){
			for(SysEngineGroup groupmodel:groupList){
				html.append(this.getProcessItemContent(groupmodel.getId(),securityList));
			}
		}
		
		List<ProcessDefDeploy> list = processDeploymentDAO.getDeployRunListbyGroupId(parentGroupId);
		boolean flag = SecurityUtil.isSuperManager();
		for (ProcessDefDeploy model : list) { 
			ProcessDefMap mapModel = processDefMapDAO.getProcessDefMapModel(model.getActDefId()); 
			if(mapModel!=null&&mapModel.getIsCcenter().equals(SysConst.off)){
				continue;
			}
			//判断流程发起权限
			if(securityList.contains(model.getActDefId())||flag){ 
				html.append( 
				"<li><a href='javascript:startProcess(\"")
				.append(model.getActDefId()).append("\",\"")
				.append(model.getTitle()).append("\")'>")
				.append(model.getTitle()).append("</a></li>");
			}
		}
		
		return html.toString();
	}
	/**
	 * 流程发起中心的JSON
	 * @return
	 */
	public String getMobileProcessLaunchCenterJson() {
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<SysEngineGroup> list = sysEngineGroupDAO.getSysEngineGroupList(new Long(0));
		int size = list.size();
		if(null!=list&&size>0){
			for (int i = 0; i < size; i++) {
				SysEngineGroup model = list.get(i);
				if(null==model)continue;
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("id", model.getId());
				item.put("master", model.getMaster());
				item.put("parentid", model.getParentid());
				item.put("groupmemo", model.getGroupmemo());
				item.put("groupname", model.getGroupname());
				item.put("processItemContent",getProcessItemContent(model.getId()));
				items.add(item);
			}
			JSONArray json = JSONArray.fromObject(items);
			jsonHtml.append(json);
			return jsonHtml.toString();
		}else{
			return null;
		}
	}
	
	/**
	 * 流程发起中心的一级目录
	 * 
	 * @param groupId
	 * @return
	 */
	private String getProcessItemContent(Long parentGroupId) {
		StringBuffer jsonHtml = new StringBuffer();
		List<ProcessDefDeploy> list = processDeploymentDAO.getDeployRunListbyGroupId(parentGroupId);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (ProcessDefDeploy model : list) {
			if(null==model)continue;
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("actDefId",model.getActDefId());
			item.put("deployI",model.getDeployId());
			item.put("groupid",model.getGroupid());
			item.put("id",model.getId());
			item.put("memo",model.getMemo());
			item.put("orderIndex",model.getOrderIndex());
			item.put("prcHelp",model.getPrcHelp());
			item.put("prcKey",model.getPrcKey());
			item.put("status",model.getStatus());
			item.put("title",model.getTitle());
			item.put("uploadDate",model.getUploadDate());
			item.put("uploader",model.getUploader());
			item.put("version",model.getVersion());
			item.put("versionType",model.getVersionType());
			items.add(item);
		}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public SysEngineGroupDAO getSysEngineGroupDAO() {
		return sysEngineGroupDAO;
	}
	public void setSysEngineGroupDAO(SysEngineGroupDAO sysEngineGroupDAO) {
		this.sysEngineGroupDAO = sysEngineGroupDAO;
	}
	public ProcessDeploymentDAO getProcessDeploymentDAO() {
		return processDeploymentDAO;
	}
	public void setProcessDeploymentDAO(ProcessDeploymentDAO processDeploymentDAO) {
		this.processDeploymentDAO = processDeploymentDAO;
	}

	public void setProcessDefMapDAO(ProcessDefMapDAO processDefMapDAO) {
		this.processDefMapDAO = processDefMapDAO;
	}
	public SysProcessDefinitionDAO getSysProcessDefinitionDAO() {
		return sysProcessDefinitionDAO;
	}

	public void setSysProcessDefinitionDAO(
			SysProcessDefinitionDAO sysProcessDefinitionDAO) {
		this.sysProcessDefinitionDAO = sysProcessDefinitionDAO;
	}

	public SysPurviewProcessService getSysPurviewProcessService() {
		return sysPurviewProcessService;
	}

	public void setSysPurviewProcessService(
			SysPurviewProcessService sysPurviewProcessService) {
		this.sysPurviewProcessService = sysPurviewProcessService;
	}
	
}
