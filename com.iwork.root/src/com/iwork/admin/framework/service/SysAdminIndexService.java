package com.iwork.admin.framework.service;

import java.util.List;
import com.iwork.app.navigation.directory.model.SysNavDirectory;
import com.iwork.app.navigation.directory.service.SysNavDirectoryService;
import com.iwork.app.navigation.function.model.Sysnavfunction;
import com.iwork.app.navigation.function.service.SysNavFunctionService;

public class SysAdminIndexService {
	private SysNavDirectoryService sysNavDirectoryService;
	private SysNavFunctionService sysNavFunctionService;
	/**
	 * 获得导航菜单
	 * @return
	 */
	public String getToolbarHtml(){
		StringBuffer html = new StringBuffer();
		StringBuffer menu = new StringBuffer();
		List<SysNavDirectory> list = sysNavDirectoryService.getDirectoryList();
		for(SysNavDirectory model:list){
			List<Sysnavfunction> sublist = sysNavFunctionService.getFunctionList(model.getId());
			if(sublist!=null&&sublist.size()>0){
				html.append("<a href=\"#\" class=\"easyui-menubutton\" data-options=\"menu:'#menu").append(model.getId()).append("' ");
				html.append("\">").append(model.getDirectoryName()).append("</a>").append("\n");
				StringBuffer temp = new StringBuffer();
				temp.append("<div id=\"menu").append(model.getId()).append("\" style=\"width:150px;\">").append("\n");
				for(Sysnavfunction function:sublist){
					temp.append("<div ");
					String icon = "";
					if(function.getFunctionIcon()!=null&&!function.getFunctionIcon().equals("")){
						icon = function.getFunctionIcon();
						temp.append("data-options=\"iconCls:'").append(function.getFunctionIcon()).append("'\"");
					}else{
						icon = "icon-sysbtn";
						temp.append("data-options=\"iconCls:'icon-sysbtn'\"");
					}
					temp.append(" onclick=\"showUrl('").append(function.getFunctionUrl()).append("','").append(function.getFunctionName()).append("','").append(icon).append("','").append(function.getFunctionTarget()).append("');\">").append(function.getFunctionName()).append("</div>").append("\n");
					temp.append("<div class=\"menu-sep\"></div>").append("\n");
				}
				temp.append("</div>").append("\n");
				menu.append(temp); 
			}else{
				html.append("<a href=\"javascript:showUrl('").append(model.getDirectoryUrl()).append("','").append(model.getDirectoryName()).append("','','").append(model.getDirectoryTarget()).append("')\" class=\"easyui-linkbutton\"  data-options=\"plain:true\" >").append(model.getDirectoryName()).append("</a>");
			}
		}
		html.append("<div style='display:none'>").append(menu).append("</div>");
		return html.toString(); 
	}
	
	public String getPortletsHtml(){
		StringBuffer html = new StringBuffer();
		//获得系统设置布局列表
		String layout = "";
		
		if(layout!=null&&!layout.equals("")){
			
			
		}
		StringBuffer column1 = new StringBuffer();
		
		StringBuffer column2 = new StringBuffer();
		StringBuffer column3 = new StringBuffer();
		
		return html.toString();
	}
	
	public SysNavDirectoryService getSysNavDirectoryService() {
		return sysNavDirectoryService;
	}
	public void setSysNavDirectoryService(
			SysNavDirectoryService sysNavDirectoryService) {
		this.sysNavDirectoryService = sysNavDirectoryService;
	}
	public SysNavFunctionService getSysNavFunctionService() {
		return sysNavFunctionService;
	}
	public void setSysNavFunctionService(SysNavFunctionService sysNavFunctionService) {
		this.sysNavFunctionService = sysNavFunctionService;
	}
	
	
}
