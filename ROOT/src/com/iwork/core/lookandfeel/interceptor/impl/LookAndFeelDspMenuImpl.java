
package com.iwork.core.lookandfeel.interceptor.impl;

import java.util.List;
import java.util.Map;
import com.iwork.app.navigation.node.dao.SysNavNodeDAO;
import com.iwork.app.navigation.node.model.SysNavNode;
import com.iwork.app.navigation.sys.dao.SysNavSystemDAO;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.app.navigation.sys.service.SysNavSystemService;
import com.iwork.core.lookandfeel.interceptor.LookAndFeelAbst;
import com.iwork.core.lookandfeel.model.LookAndFeel;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.NavSecurityUtil;
import com.iwork.core.util.SpringBeanUtil;

public class LookAndFeelDspMenuImpl extends LookAndFeelAbst{
	private String title = "";
	private SysNavNodeDAO sysNavNodeDAO;
	private SysNavSystemDAO sysNavSystemDAO;
	private SysNavSystemService sysNavSystemService; 
	private int count = 0;
	public LookAndFeelDspMenuImpl(UserContext uc,Map params, String key) {
		super(uc,params, key);
	}
 
	public String getActionPath() {
		String actionPath = null;
			LookAndFeel model = this.getModel();
			if(model!=null){
				actionPath = model.getWelcome();
			}
			return actionPath;
	}
	
	public String getTopMenuHtml( List<SysNavSystem>  list){
		return getNavToolBar(list);
	}
	/**
	 * 获得当前人显示信息内容
	 */
	public String getCurrentUserInfoHtml(){
		StringBuffer html = new StringBuffer();
		UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
		if(userContext!=null){
			html.append("<img class=\"nav-user-photo\"  onerror=\"this.src='iwork_img/nopic.gif'\"  src='iwork_file/USER_PHOTO/").append(userContext.get_userModel().getUserid()).append(".jpg'/>");
			html.append(userContext.get_userModel().getUsername());
		} 
		return html.toString();
	}
	
	
	public String getLeftMenuHtml(String systemid){
		if(sysNavSystemService==null){
			sysNavSystemService = (SysNavSystemService)SpringBeanUtil.getBean("sysNavSystemService");
		}
		List<SysNavSystem> list = sysNavSystemService.getMyNavSystemList();
		StringBuffer html = new StringBuffer();
		StringBuffer menu = new StringBuffer();
		StringBuffer content = new StringBuffer();
//		String message = MessageResourceFactory.getInstance().getMessage("portal.tab.Index.title"); 
		menu.append("<ul class=\"menu-item\">").append("\n");
		content.append("<div class=\"menu-cont\" style=\"display:none;top:241px;\">");
		 int num = 0;
		for(int i=0;i<list.size();i++){
			SysNavSystem system = list.get(i);
			String icon = "";
			String subMenu =  getSubMenuHtml(system.getId()); 
			if(system.getSysIcon()!=null&&system.getSysIcon().equals("/")){
				icon = "fa-desktop";
			}else{
				icon = system.getSysIcon();
			}
			if(subMenu.equals("")){
				menu.append("<li><a  href=\"javascript:setRedirect('sys_").append(system.getId()).append("','").append(system.getSysUrl()).append("','").append(system.getSysName()).append("')\"  >");
				menu.append("<i class=\"menu-icon fa ").append(icon).append("\" ></i>").append("\n");
				menu.append(system.getSysName()).append("</a></li>\n");
			}else{

				menu.append("<li><a  href=\"###\"  >");
				menu.append("<i class=\"menu-icon fa ").append(icon).append("\" ></i>").append("\n");
				menu.append(system.getSysName()).append("</a></li>\n");
			}
			
			if(system.getSysIcon()!=null&&system.getSysIcon().equals("/")){
				icon = "fa-desktop";
			}else{
				icon = system.getSysIcon();
			}
			if(subMenu.equals("")){
				content.append("<div class=\"menu-cont-list\" style=\"display:none;\">").append("\n");
				content.append("<ul>").append("\n");
				content.append(subMenu);
				content.append("</ul>").append("\n");
				content.append("</div>").append("\n");
			}else{
				content.append("<div class=\"menu-cont-list submenu\" style=\"display:none;\">").append("\n");
				content.append("<ul>").append("\n");
				content.append(subMenu);
				content.append("</ul>").append("\n");
				content.append("</div>").append("\n");
			}
//			content.append(buildGroupDiv(system));
		}
		content.append("</div>");
		menu.append("</ul>").append("\n");
		html.append(menu);
		html.append(content);
		return html.toString();	
	}
	
	private String buildGroupDiv(SysNavSystem system ){
		StringBuffer html = new StringBuffer();
		
		
		
		
		return html.toString();
	}
	public String getNavToolBar( List<SysNavSystem>  list ){
		StringBuffer html = new StringBuffer();
		return html.toString();
	}
	/**
	 * 获得子菜单
	 * 默认读取4级子菜单，不进行递归循环
	 * @param systemId
	 * @param nodeId
	 * @return
	 */
	private String getSubMenuHtml(String systemId){
		StringBuffer html = new StringBuffer();
		if(sysNavNodeDAO==null){
			sysNavNodeDAO = (SysNavNodeDAO)SpringBeanUtil.getBean("sysNavNodeDAO");
		}
			//第一级循环
			List<SysNavNode> list  = sysNavNodeDAO.getChildListBySystemId(systemId);
		if(list!=null&&list.size()>0){
			int num = 0;
			//第二级循环
			for(SysNavNode node:list){
				String icon = "fa-caret-right";
				if(node.getNodeIcon()!=null&&node.getNodeIcon().equals("/")){
					icon = "fa-caret-right";
				}else{
					icon = node.getNodeIcon();
				}
				int count = 0;
				StringBuffer subHtml = new StringBuffer();
				boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(node);
				if(!flag){//无当前模块权限，系统自动过滤
					continue;
				}
				
				List<SysNavNode> sublist = sysNavNodeDAO.getChildListByParentId(node.getId().toString());
				html.append("<li>").append("\n");  
				
				html.append("<h3><a  href=\"javascript:setRedirect('nd_").append(node.getId()).append("','").append(node.getNodeUrl()).append("','").append(node.getNodeName()).append("')\" >");
				
				html.append(node.getNodeName()).append("</a></h3>").append("\n");  
				html.append("<div class=\"menu-list-link\">");
				if(sublist!=null&&sublist.size()>0){
					for(int i=0;i<sublist.size();i++){
						SysNavNode subNode = sublist.get(i);
						StringBuffer subHtml3 = new StringBuffer();
						boolean subflag = NavSecurityUtil.getInstance().isCheckSecurity(subNode);
						if(!subflag){//无当前模块权限，系统自动过滤
							continue;
						}
						count++;
						//第三级循环
						List<SysNavNode> sublist2 = sysNavNodeDAO.getChildListByParentId(subNode.getId().toString());
						int subCount = 0;
						if(sublist2!=null&&sublist2.size()>0){
							for(SysNavNode subNode2:sublist2){
								boolean sub2flag = NavSecurityUtil.getInstance().isCheckSecurity(subNode2);
								if(!sub2flag){//无当前模块权限，系统自动过滤
									continue;
								} 
								subCount++; 
								subHtml3.append("<a  href=\"javascript:setRedirect('nd_").append(subNode2.getId()).append("','").append(subNode2.getNodeUrl()).append("','").append(subNode2.getNodeName()).append("')\"  >").append("\n");
								subHtml3.append("\t\t<i class=\"menu-icon fa fa-caret-right\"></i>\n");
								subHtml3.append(subNode2.getNodeName()).append("\n");
								subHtml3.append("</a>");
								subHtml3.append("<span class=\"long-string\">|</span>").append("\n");
							}
						} 
						if(subCount>0){
							html.append("<a  href=\"javascript:setRedirect('nd_").append(subNode.getId()).append("','").append(subNode.getNodeUrl()).append("','").append(subNode.getNodeName()).append("')\"  >").append("\n");
							html.append("<i class=\"menu-icon fa fa-caret-right\"></i>\n");
							html.append(subNode.getNodeName()).append("\n"); 
							html.append("</a>"); 
							if(i <sublist.size()-1){
								html.append("<span class=\"long-string\">|</span>").append("\n");
							}

						}else{ 
							String icon2 = "";
							html.append("<a  href=\"javascript:setRedirect('nd_").append(subNode.getId()).append("','").append(subNode.getNodeUrl()).append("','").append(subNode.getNodeName()).append("')\"  >").append("\n");
							html.append("<i class=\"menu-icon fa fa-caret-right\"></i>\n");
							html.append(subNode.getNodeName()).append("\n"); 
							html.append("</a>"); 
							if(i <sublist.size()-1){
								html.append("<span class=\"long-string\">|</span>").append("\n");
							}
						}
					} 
				//	subHtml.append("</li>\n");
				}
				
				html.append("</div>").append("\n");  
				html.append("</li>").append("\n");  
				
//				
//				if(count>0){
//					
//					
//					
//					
//					
//				}else{
//					
//					html.append("<li>").append("\n");  
//					html.append("<h3><a  href=\"javascript:setRedirect('nd_").append(node.getId()).append("','").append(node.getNodeUrl()).append("','").append(node.getNodeName()).append("')\" >").append(node.getNodeName()).append("</a></h3>").append("\n");  
//					html.append("<div class=\"menu-list-link\"></div>").append("\n");  
//					html.append("</li>").append("\n");  
//				}
				
			}
		}
			
		return html.toString();
	}
}
