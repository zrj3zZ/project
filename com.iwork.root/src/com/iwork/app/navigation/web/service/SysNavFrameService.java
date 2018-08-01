package com.iwork.app.navigation.web.service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.message.sysmsg.dao.SysMessageDAO;
import com.iwork.app.message.sysmsg.service.SysMessageService;
import com.iwork.app.navigation.directory.dao.SysNavDirectoryDAO;
import com.iwork.app.navigation.function.dao.SysNavFunctionDAO;
import com.iwork.app.navigation.node.dao.SysNavNodeDAO;
import com.iwork.app.navigation.node.model.SysNavNode;
import com.iwork.app.navigation.sys.dao.SysNavSystemDAO;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.app.navigation.web.dao.SysNavFrameDAO;
import com.iwork.app.navigation.web.model.LeftMenuModel;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.NavSecurityUtil;
import com.iwork.core.util.SequenceUtil;
import com.iwork.plugs.bgyp.model.ShopCarModel;
import com.iwork.plugs.email.service.RecIWorkMailClientService;
import com.iwork.process.desk.handle.service.ProcessDeskManagementService;
import com.iwork.sdk.ProcessAPI;

import org.apache.log4j.Logger;
public class SysNavFrameService {
	private final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private static Logger logger = Logger.getLogger(SysNavFrameService.class);
	private SysNavFunctionDAO sysNavFunctionDAO;
	private SysNavSystemDAO sysNavSystemDAO;
	private SysNavDirectoryDAO sysNavDirectoryDAO;
	private SysNavFrameDAO sysNavFrameDAO;
	private SysMessageDAO sysMessageDAO;
	private SysNavNodeDAO sysNavNodeDAO;
	
	private  ProcessDeskManagementService processDeskManagementService;
	private  SysMessageService sysMessageService;
	private RecIWorkMailClientService reciWorkMailClientService;
	
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}
	
	/**
	 * 获得top菜单HTML
	 * @param key
	 * @return
	 */
	public String getTopMenuHtml(String key){
		if(key==null){
			key = "main";
		}
		StringBuffer html = new StringBuffer();
		List<TopMenuModel> list = this.buildMenuData();
		for(TopMenuModel model:list){
			String isCurrent = "";
			if(model.getKey().equals(key)){
				isCurrent = " class=\"current\" ";
			}
			html.append("<li ").append(isCurrent).append(">").append("\n");
			if(model.getOpenType()==null){
				html.append("<a href=\"").append(model.getUrl()).append("\"  class=\"q-nav\"><span>").append(model.getTitle()).append("</span></a></li> ").append("\n");
			}else if(model.getOpenType().equals(TopMenuModel.LAYOUT_OPENTYPE_LEFTMENU)){
				html.append("<a href=\"javascript:redirectLeftView('").append(model.getUrl()).append("','").append(model.getKey()).append("')\"  class=\"q-nav\"><span>").append(model.getTitle()).append("</span></a></li> ").append("\n");
			}else{
				html.append("<a href=\"javascript:redirectFullView('").append(model.getUrl()).append("','").append(model.getKey()).append("')\"  class=\"q-nav\"><span>").append(model.getTitle()).append("</span></a></li> ").append("\n");
			} 
			html.append("</li >").append("\n"); 
		}
		return html.toString();
	}
	/**
	 * 获得top菜单HTML
	 * @param key
	 * @return
	 */
	public String getLeftMenuHtml(String key,List<SysNavSystem> list){
		if(key==null){
			key = "main";
		}
		StringBuffer html = new StringBuffer();
		html.append("<div class=\"mod-sub-nav\">").append("\n"); 
		html.append("<ul class=\"basic-list\">").append("\n"); 
		List<LeftMenuModel>  topList = this.buildLeftData();
		for(LeftMenuModel model:topList){
			String isCurrent = "";
			if(model.getKey().equals(key)){ 
				isCurrent = " class=\"current\" ";
			}
			if(model.getOpenType().equals(LeftMenuModel.LAYOUT_OPENTYPE_LEFTMENU)){
				html.append("<li ").append(isCurrent).append("><a href=\"javascript:void(0)\" event-menu=\"").append(model.getKey()).append("\" event-url=\"").append(model.getUrl()).append("\" class=\"lev\"><i class=\"").append(model.getClassName()).append("\"></i>").append(model.getTitle()).append("</a></li>").append("\n");
			}else{
				html.append("<li ").append(isCurrent).append("><a href=\"").append(model.getUrl()).append("\" event-menu=\"").append(model.getKey()).append("\" class=\"lev\"><i class=\"").append(model.getClassName()).append("\"></i>").append(model.getTitle()).append("</a></li>").append("\n");
			}
		}

		html.append("</ul>").append("\n");
		html.append("</div>  ").append("\n");
		
		html.append("<!--应用-->").append("\n");
		html.append("<div class=\"mod-app\"> ").append("\n");
		html.append("<fieldset class=\"P-line\"><legend class=\"P-txt\">应用</legend></fieldset>").append("\n");
		html.append("<ul class=\"app-list\">").append("\n");
		
		for(SysNavSystem model:list){
			String isCurrent = "";
			String tmpKey = key;
			isCurrent = " class=\"current\" ";
			html.append("<li ").append(isCurrent).append(">").append("\n");
			html.append(" <a href=\"#\" class=\"lev\" event-args=\"").append(model.getId()).append("\" id='app_system_").append(model.getId()).append("'>").append("\n");
			html.append("<div class=\"icon-pic icon-app-task\"><img src=\"").append(model.getSysIcon()).append("\" width='24' onerror=\"this.src='iwork_img/sysjump.gif'\">").append(model.getSysName()).append("<span id=\"load_icon").append(model.getId()).append("\"></span></div>").append("\n");
			html.append(" </a>").append("\n");
			html.append(" <ul  id=\"sub_menu_").append(model.getId()).append("\" style=\"display:none\" class=\"app-sub-list\"></ul>").append("\n");
			html.append("</li>").append("\n");
			
		}
		html.append(" </ul>").append("\n");
		html.append(" </div>").append("\n");
		return html.toString();
	}
	
	/**
	 * 获得当前用户权限范围内子系统
	 * @return
	 */
	public List<SysNavSystem> getMyNavSystemList() {
		List<SysNavSystem> allList =sysNavSystemDAO.getAll();
		List list = new ArrayList();
		for(int i=0;i<allList.size();i++){
			SysNavSystem sysNavSystem = (SysNavSystem)allList.get(i);
				boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(sysNavSystem);
				if(flag){
					list.add(sysNavSystem);
				}
		}
		return list;
	}
	
	
	
	public String getNavToolBar(){
		StringBuffer html = new StringBuffer();
		StringBuffer subMenu = new StringBuffer();
		List<SysNavSystem> allList = this.getMyNavSystemList();
		for(SysNavSystem system:allList){
			html.append("<a href=\"#\" class=\"easyui-menubutton\" data-options=\"menu:'#menu").append(system.getId()).append("'");
			String sysicon = system.getSysIcon();
			if(sysicon==null||(sysicon!=null&&sysicon.indexOf(".")>0)){
				sysicon = "icon-lightning";
			}
			if(sysicon!=null&&!sysicon.equals("/")){
				html.append(",iconCls:'").append(sysicon).append("'");
			}
			html.append("\">").append(system.getSysName()).append("</a>").append("\n");
			subMenu.append(this.getSubMenuHtml(system.getId())); 
		} 
		
		html.append(subMenu);
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
			//第一级循环
			List<SysNavNode> list  = sysNavNodeDAO.getChildListBySystemId(systemId);
		if(list!=null&&list.size()>0){
			html.append("<div id=\"menu").append(systemId).append("\" style=\"width:150px;\">\n");
			int num = 0;
			for(SysNavNode node:list){
				boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(node);
				if(!flag){//无当前模块权限，系统自动过滤
					continue;
				}
				num++;
				//第二级循环
				List<SysNavNode> sublist = sysNavNodeDAO.getChildListByParentId(node.getId().toString());
				StringBuffer subHtml = new StringBuffer();
				int count = 0;
				if(sublist!=null&&sublist.size()>0){
					subHtml.append("<div class=\"menu-sep\"></div>\n");
					subHtml.append("<div>\n");
					subHtml.append("<span>").append(node.getNodeName()).append("</span>\n");
					subHtml.append("<div>\n");
					for(SysNavNode subNode:sublist){
						boolean subflag = NavSecurityUtil.getInstance().isCheckSecurity(subNode);
						if(!subflag){//无当前模块权限，系统自动过滤
							continue;
						}
						count++;
						//第三级循环
						List<SysNavNode> sublist2 = sysNavNodeDAO.getChildListByParentId(subNode.getId().toString());
						int subCount = 0;
						StringBuffer subHtml2 = new StringBuffer();
						if(sublist2!=null&&sublist2.size()>0){
							subHtml2.append("<div class=\"menu-sep\"></div>\n");
							subHtml2.append("\t<div>\n");
							subHtml2.append("\t<span>").append(subNode.getNodeName()).append("</span>\n");
							subHtml2.append("\t<div>\n");
							for(SysNavNode subNode2:sublist2){
								boolean sub2flag = NavSecurityUtil.getInstance().isCheckSecurity(subNode2);
								if(!sub2flag){//无当前模块权限，系统自动过滤
									continue;
								}
								subCount++;
								String icon = "";
								if(subNode2.getNodeIcon()!=null&&!subNode2.getNodeIcon().equals("/")){
									icon = subNode2.getNodeIcon();
								}
								subHtml2.append("<div data-options=\"iconCls:'").append(icon).append("'\" onclick=\"showUrl('").append(subNode2.getNodeUrl()).append("','").append(subNode2.getNodeName()).append("','").append(icon).append("','mainFrame');\">").append(subNode2.getNodeName()).append("</div>\n");
							}
							subHtml2.append("\t</div>\n");
							subHtml2.append("\t</div>\n");
						}
						if(subCount>0){ 
							subHtml.append(subHtml2);
						}else{
							if(count>1){
								subHtml.append("<div class=\"menu-sep\"></div>\n");
							}
							String icon = "";
							if(subNode.getNodeIcon()!=null&&!subNode.getNodeIcon().equals("/")){
								icon = subNode.getNodeIcon();
							}
							subHtml.append("<div data-options=\"iconCls:'").append(icon).append("'\" onclick=\"showUrl('").append(subNode.getNodeUrl()).append("','").append(subNode.getNodeName()).append("','").append(icon).append("','mainFrame');\">").append(subNode.getNodeName()).append("</div>\n");
						} 
					}
					subHtml.append("</div>\n");
					subHtml.append("</div>\n");
				}
				if(count>0){
					html.append(subHtml);
				}else{
					if(num>1){
						html.append("<div class=\"menu-sep\"></div>\n");
					}
					String icon = "";
					if(node.getNodeIcon()!=null&&!node.getNodeIcon().equals("/")){
						icon = node.getNodeIcon();
					}
					html.append("<div data-options=\"iconCls:'").append(icon).append("'\" onclick=\"showUrl('").append(node.getNodeUrl()).append("','").append(node.getNodeName()).append("','").append(icon).append("','mainFrame');\">").append(node.getNodeName()).append("</div>\n");
				}
			}
			html.append("</div>\n");
		}
			
		return html.toString();
	}
	
	
	
	
	/**
	 * 
	 * @return
	 */
	private List<TopMenuModel> buildMenuData(){
		List<TopMenuModel> list = new ArrayList();
		TopMenuModel model = new TopMenuModel();
			model.setKey("main");
			model.setTitle("首页");
			model.setUrl("mainAction.action");
//			model.setOpenType(TopMenuModel.LAYOUT_OPENTYPE_LEFTMENU);
		list.add(model);
			model = new TopMenuModel();
			model.setKey("process");
			model.setTitle("流程中心");
			model.setUrl("process_desk_index.action");
			model.setOpenType(TopMenuModel.LAYOUT_OPENTYPE_LEFTMENU);
		list.add(model);
			model = new TopMenuModel();
			model.setKey("report");
			model.setTitle("报表中心");
			model.setUrl("ireport_rt_showcenter.action");
			model.setOpenType(TopMenuModel.LAYOUT_OPENTYPE_FULL);
		list.add(model);
			model = new TopMenuModel();
			model.setKey("kmdoc");
			model.setTitle("知识库");
			model.setUrl("kmdoc_Index.action");
			model.setOpenType(TopMenuModel.LAYOUT_OPENTYPE_FULL);
		list.add(model);
			model = new TopMenuModel();
			model.setKey("ask");
			model.setTitle("问答");
			model.setUrl("know_index.action");
			model.setOpenType(TopMenuModel.LAYOUT_OPENTYPE_FULL);
		list.add(model);
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<LeftMenuModel> buildLeftData(){ 
		List<LeftMenuModel> list = new ArrayList();
		LeftMenuModel model = new LeftMenuModel();
		model.setKey("main");
		model.setTitle("首页");
		model.setUrl("mainAction.action");
		model.setClassName("aicon-home");
		model.setOpenType(LeftMenuModel.LAYOUT_OPENTYPE_HOME);
//			model.setOpenType(TopMenuModel.LAYOUT_OPENTYPE_LEFTMENU);
		list.add(model);
		model = new LeftMenuModel();
		model.setKey("sysmessage");
		model.setTitle("系统消息");
		model.setUrl("sysmsg_index.action");
		model.setClassName("aicon-email");
		model.setOpenType(LeftMenuModel.LAYOUT_OPENTYPE_LEFTMENU);
		list.add(model);
//		model = new LeftMenuModel();
//		model.setKey("report");
//		model.setTitle("鹰眼检索");
//		model.setClassName("aicon-star");
//		model.setUrl("eaglesSearch_Index.action");
//		model.setOpenType(LeftMenuModel.LAYOUT_OPENTYPE_LEFTMENU);
//		list.add(model);
		model = new LeftMenuModel();
		model.setKey("report");
		model.setTitle("日程管理");
		model.setClassName("aicon-star");
		model.setUrl("schCalendarAction.action");
		model.setOpenType(LeftMenuModel.LAYOUT_OPENTYPE_LEFTMENU);
		list.add(model);
//		model = new TopMenuModel();
//		model.setKey("kmdoc");
//		model.setTitle("知识库");
//		model.setOpenType(TopMenuModel.LAYOUT_OPENTYPE_FULL);
//		model.setUrl("kmdoc_Index.action");
//		list.add(model);
//		model = new TopMenuModel();
//		model.setKey("ask");
//		model.setTitle("问答");
//		model.setOpenType(TopMenuModel.LAYOUT_OPENTYPE_FULL);
//		model.setUrl("know_index.action");
//		list.add(model);
		return list;
	}
	
	/**
	 * 获得待办事宜|系统消息个数
	 * @return
	 */
	public String showTipsInfo(String type){ 
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		int count = 0;
		if(type.equals("workflow")){
			int taskNum = processDeskManagementService.getProcessTaskCount(userId);
			int noticeNum = processDeskManagementService.getProcessRuntimeCcDAO().getUserCCListCount(userId);
			count = taskNum+noticeNum;
		}else if(type.equals("sysmsg")){
			count = sysMessageService.getUnreadCount(userId);
		}
		StringBuffer msgJson = new StringBuffer("");
        JSONObject jsonObject = new JSONObject();  
        jsonObject.put("count", count);
        jsonObject.put("type",type);
        msgJson.append(jsonObject);
        return msgJson.toString();
	}
	/**
	 * 获得待办事宜|系统消息个数
	 * @return
	 */
	public String showNumInfo(){ 
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		int count = 0;
		JSONArray jsonList = new JSONArray();
			int taskNum = processDeskManagementService.getProcessTaskCount(userId);
			int noticeNum = processDeskManagementService.getProcessRuntimeCcDAO().getUserCCListCount(userId);
			count = taskNum+noticeNum;
			JSONObject taskObject = new JSONObject();  
			taskObject.put("count", count);
			taskObject.put("type","process");
			jsonList.add(taskObject); 
			//获取消息格式
			int msgNum= sysMessageService.getUnreadCount(userId);
			StringBuffer msgJson = new StringBuffer("");
			JSONObject jsonObject = new JSONObject();  
			jsonObject.put("count", msgNum);
			jsonObject.put("type","sysmsg");
			jsonList.add(jsonObject); 
			msgJson.append(jsonList);
		return msgJson.toString();
	}
	/**
	 * 获得待办事宜列表
	 * @return
	 */
	public String getTodolistTop10(){
		String json = "";
		Page page = new Page();
		page.setCurPageNo(0);
		page.setPageSize(10);
		json = processDeskManagementService.getTodoListJson(page, null, null); 
		return json; 
	}
	
	public String getMsgList(){
		StringBuffer jsonHtml = new StringBuffer();
		//系统消息
		int msgNum= sysMessageService.getUnreadCount(UserContextUtil.getInstance().getCurrentUserId());
		int noticeSize = processDeskManagementService.getNoticeListJsonSize();
		HashMap<String,Object> hash = new HashMap<String,Object>();
		hash.put("SYSMSG", msgNum);
		hash.put("PROCESSMSG", noticeSize);
		hash.put("TOTAL", msgNum+noticeSize);
		JSONArray json = JSONArray.fromObject(hash);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public String getEmailList(){
		String json = "";
		// 查询未读邮件 
		json = reciWorkMailClientService.getRecUnReadEmail(UserContextUtil.getInstance().getCurrentUserId(), 10, 0);
		return json;
	}
	
	/**
	 * 执行查询操作
	 * @param searchkey
	 * @return
	 */
	public String doNodeSearchJSON(String searchkey){
		StringBuffer html = new StringBuffer();
		List<SysNavNode> list = sysNavNodeDAO.getSearchList(searchkey);
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		int num = 0;
		for(SysNavNode snd:list){
			boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(snd);
			if(!flag){//无当前模块权限，系统自动过滤
				continue;
			}
			//有子目录的不显示
			int count = sysNavNodeDAO.getChildListByParentId(snd.getId().toString()).size();
			if(count>0){continue;}
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", snd.getId());
			item.put("name",snd.getNodeName());
			item.put("iconOpen", "iwork_img/images/folder-open.gif");
			item.put("iconClose", "iwork_img/ztree/diy/40.png");
			if(snd.getNodeIcon()!=null&&snd.getNodeIcon().trim().toUpperCase().indexOf("IWORK_IMG")>=0){
				item.put("icon", snd.getNodeIcon());
			}else{
				item.put("icon", "iwork_img/icon/AIM.png");
			}
			
			item.put("isParent", "false");
			item.put("target",snd.getNodeTarget());
			item.put("pageurl",snd.getNodeUrl()); 
			item.put("nodeType", "NODE"); 
			num++;
			rows.add(item);
		}
		if(num==0){
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("name","检索为空，点击返回导航"); 
			item.put("icon", "iwork_img/nondynamic.gif");
			rows.add(item);
		}
		JSONArray json = JSONArray.fromObject(rows);
		html.append(json);
		return html.toString();
	}
	
	/**
	 * 获得权限范围内，导航校验
	 * @param nodeId
	 * @param isPurview
	 * @return
	 */
	public String getTreeSysJson(String nodeId){
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
					//用户对导航菜单进行授权时，将有权限的树节点设置复选框为“勾选”
					boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(sns);
					if(!flag){//无当前模块权限，系统自动过滤
						continue;
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
					item.put("iconClose", "iwork_img/ztree/diy/40.png");
					if(sns.getSysIcon()!=null&&"".equals(sns.getSysIcon().trim())){
						item.put("icon", sns.getSysIcon());
					}else{
						item.put("icon", "iwork_img/icon/AIM.png");
					}
					if(sns.getSysIsHidden()!=null){
						//默认隐藏
						if(sns.getSysIsHidden().equals("0")){
							item.put("isHidden", "true");
						}else if(sns.getSysIsHidden().equals("1")){
							item.put("isHidden", "false");
						}else if(sns.getSysIsHidden().equals("2")){
							
							item.put("open", true);
							item.put("children", this.getTreeNodeJson(sns.getId(), "SYS"));
						}
					}
					item.put("nodeType", "SYS");
					item.put("target",sns.getSysTarget());
					item.put("pageurl",sns.getSysUrl()); 
					rows.add(item); 
				}
			}
			JSONArray json = JSONArray.fromObject(rows);
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
	public String getTreeNodeJson(String nodeId,String nodeType){
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
				item.put("iconClose", "iwork_img/ztree/diy/40.png");
				if(snd.getNodeIcon()!=null&&snd.getNodeIcon().trim().toUpperCase().indexOf("IWORK_IMG")>=0){
					item.put("icon", snd.getNodeIcon());
				}else{
					item.put("icon", "iwork_img/icon/AIM.png");
				}
				//用户对导航菜单进行授权时，将有权限的树节点设置复选框为“勾选”
				boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(snd);
				if(!flag){//无当前模块权限，系统自动过滤
					continue;
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
	
	

	//=================================END========================================================
	
	/**
	 * 获得未读系统消息个数
	 * @param userid
	 * @return
	 */
	public int getUnReadCount(String userid){
		return sysMessageDAO.getUnReadMsgRow(userid);
	}
	/**
	 * 获得待办事宜个数
	 * @param userid
	 * @return
	 */
	public int getWorkflowCount(String userid){
		return processDeskManagementService.getProcessTaskCount(userid);
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

	public void setSysNavSystemDAO(SysNavSystemDAO sysNavSystemDAO) {
		this.sysNavSystemDAO = sysNavSystemDAO;
	}

	public void setSysNavDirectoryDAO(SysNavDirectoryDAO sysNavDirectoryDAO) {
		this.sysNavDirectoryDAO = sysNavDirectoryDAO;
	}

	public void setSysNavFunctionDAO(SysNavFunctionDAO sysNavFunctionDAO) {
		this.sysNavFunctionDAO = sysNavFunctionDAO;
	}
	public void setSysNavFrameDAO(SysNavFrameDAO sysNavFrameDAO) {
		this.sysNavFrameDAO = sysNavFrameDAO;
	}

	public void setSysMessageDAO(SysMessageDAO sysMessageDAO) {
		this.sysMessageDAO = sysMessageDAO;
	}

	public ProcessDeskManagementService getProcessDeskManagementService() {
		return processDeskManagementService;
	}

	public void setProcessDeskManagementService(
			ProcessDeskManagementService processDeskManagementService) {
		this.processDeskManagementService = processDeskManagementService;
	}

	public SysMessageService getSysMessageService() {
		return sysMessageService;
	}
	public void setSysMessageService(SysMessageService sysMessageService) {
		this.sysMessageService = sysMessageService;
	}

	public void setSysNavNodeDAO(SysNavNodeDAO sysNavNodeDAO) {
		this.sysNavNodeDAO = sysNavNodeDAO;
	}

	public void setReciWorkMailClientService(
			RecIWorkMailClientService reciWorkMailClientService) {
		this.reciWorkMailClientService = reciWorkMailClientService;
	}
	
	public int getSysCount() {
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		int count = 0;
		count = sysMessageService.getUnreadCount(userId);
		return count;
	}
	
	public int getDbCount() {
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		int count = 0;
		int taskNum = processDeskManagementService.getProcessTaskCount(userId);
		int noticeNum = processDeskManagementService.getProcessRuntimeCcDAO().getUserCCListCount(userId);
		count = taskNum+noticeNum;
		return count;
	}

	/**获取近3天要开的未处理的会议和今天“未处理”的事项信息
	 * @return
	 */
	public int getFspCount() {
		int count=0;
		count=sysNavFrameDAO.getFspCount();
		return count;
	}

	public boolean isDm() {
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			Long orgRoleId = uc.get_userModel().getOrgroleid();
			if (orgRoleId!=null&&orgRoleId.equals(new Long(3))) {
				flag = true;
			}
		}
		return flag;
	}
	
	public boolean getIsSuperMan() {
		String roleTyle = "";
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			Long orgRoleId = uc.get_userModel().getOrgroleid();
			if (orgRoleId!=null&&(orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9)))) {
				flag = true;
			}
		}
		return flag;
	}

	/**获取会议信息
	 * @return
	 */
	public List<HashMap<String, Object>> getHyfsp(int pageNumber,int pageSize) {
		int start = (pageNumber-1)*pageSize;
		int end=pageNumber*pageSize;
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
		// 场外看到所有的，持续督导看到自己分管的
				String userId = UserContextUtil.getInstance().getCurrentUserId();
				String username = UserContextUtil.getInstance().getCurrentUserFullName();		
				// 判断角色
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				StringBuffer sb = new StringBuffer();
				if(uc==null) return null;
				Long orgRoleId = uc.get_userModel().getOrgroleid();			
				if (uc != null) {
					if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {// 场外或质控部负责人,看到所有
						sb.append("  select * from (select BOTABLE.id,meetname,customername,to_char(plantime,'yyyy-MM-dd HH24:mi') plantime,status,rownum num,BINDTABLE.INSTANCEID");
						sb.append(" from BD_MEET_PLAN BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=96 and BINDTABLE.metadataid=109 ");
						sb.append("  where to_char(plantime, 'yyyyMMDD') >= to_char(sysdate, 'yyyyMMDD')");
						sb.append("     and to_char(plantime, 'yyyyMMDD') <=");
						sb.append("      to_char(sysdate+2, 'yyyyMMDD') and ( ycl  is null or ycl='0') ) where num>?  and num<=? order by plantime");
						
					}else if(orgRoleId.equals(new Long(3)))//董秘登陆看自己的
					{						
						sb.append(" select * from (select BOTABLE.id,meetname,customername,to_char(plantime,'yyyy-MM-dd HH24:mi') plantime,status,rownum num,BINDTABLE.INSTANCEID");
						sb.append(" from BD_MEET_PLAN BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=96 and BINDTABLE.metadataid=109 ");
						sb.append("  where to_char(plantime, 'yyyyMMDD') >= to_char(sysdate, 'yyyyMMDD')");
						sb.append("     and to_char(plantime, 'yyyyMMDD') <=");
						sb.append("      to_char(sysdate+2, 'yyyyMMDD')");
						sb.append("   and  customerno=?  and ( ycl  is null or ycl='0')  ) where num>?  and num<=? order by plantime");
					}
					else
					{// 其他人员,看分派给自己的
						sb.append(" select * from (select BOTABLE.id,meetname,customername,to_char(plantime,'yyyy-MM-dd HH24:mi') plantime,status,rownum num,BINDTABLE.INSTANCEID");
						sb.append(" from BD_MEET_PLAN BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=96 and BINDTABLE.metadataid=109 ");
						sb.append("  where to_char(plantime, 'yyyyMMDD') >= to_char(sysdate, 'yyyyMMDD')");
						sb.append("     and to_char(plantime, 'yyyyMMDD') <=");
						sb.append("      to_char(sysdate+2, 'yyyyMMDD')");
						sb.append("   and  customerno in(select khbh from BD_MDM_KHQXGLB where khfzr=? or zzcxdd=? or fhspr=? or zzspr=? or ggfbr=?) and ( ycl  is null or ycl='0')  ) where num>?  and num<=? order by plantime");
					
					}
				}
				Connection conn=null;
				ResultSet rs=null;
				PreparedStatement stmt=null;
				try {
					conn=DBUtil.open();
					 stmt=conn.prepareStatement(sb.toString());
					 if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {// 场外或质控部负责人,看到所有
					
						stmt.setInt(1, start);
						stmt.setInt(2, end);
					 }else if(orgRoleId.equals(new Long(3)))//董秘登陆看自己的
						{
						stmt.setString(1,uc.get_userModel().getExtend1());
					 	stmt.setInt(2, start);
						stmt.setInt(3, end);
						}
					 else{
							stmt.setString(1, username);
							stmt.setString(2, username);
							stmt.setString(3, username);
							stmt.setString(4, username);
							stmt.setString(5, username);
							stmt.setInt(6, start);
							stmt.setInt(7, end);
					 }
					rs=stmt.executeQuery();
				    while(rs.next()){
				    	String meetname=rs.getString("MEETNAME");
				    	String csname=rs.getString("CUSTOMERNAME");
				    	String plantime=rs.getString("PLANTIME");
				    	String  status=rs.getString("STATUS");
				    	int  instanceid=rs.getInt("INSTANCEID");
				    	Long id=rs.getLong("ID");
				    	HashMap<String,Object> h=new HashMap<String,Object>();
				    	h.put("MEETNAME", meetname);
				    	h.put("CUSTOMERNAME", csname);
				    	h.put("PLANTIME", plantime);
				    	h.put("STATUS", status);
				    	h.put("INSTANCEID", instanceid);
				    	h.put("ID", id);
				    	list.add(h);
				    }
				} catch (Exception e) {
					logger.error(e,e);
				}finally {
					DBUtil.close(conn, stmt, rs);
		    	}

		return list;
	}

	/**获取重大事项信息
	 * @return
	 */
	public List<HashMap<String, Object>> getZyddfsp(int pageNumber,int pageSize) {
		int start = (pageNumber-1)*pageSize;
		int end=pageNumber*pageSize;
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
		// 场外看到所有的，持续督导看到自己分管的
	
		return list;
	}
	/**
	 *  日程提醒完成事件
	 * @param type
	 */
	public boolean addRcwc(Long type){
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		String username=user.getUserid()+"["+user.getUsername()+"]";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString =sdf.format(date);  
		int id = SequenceUtil.getInstance().getSequenceIndex("rctx_sequence");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO bd_xp_xpsxqtb (ID, CREATEUSER,CUSTOMERNO,XPSXID) VALUES (rctx_sequence.nextval,?,?,?) ");
		Connection conn = null; 
		PreparedStatement stat = null;
		int i =0;
		try {
			conn = DBUtil.open(); 
			stat = conn.prepareStatement(sql.toString());
			//stat.setInt(1, id);
			stat.setString(1, username);
			stat.setString(2, dateString);
			stat.setString(3, type.toString());
			i=stat.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		}finally {
			DBUtil.close(conn, stat, null);
		}
		if(i>0){
			flag = true;
		}
		return flag;
	}
	/**
	 * 周期日程提醒完成事件
	 * @param type
	 */
	public boolean updRcwc(Long type,String sj){
		boolean flag = false;
		Connection conn = null;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		try {
			conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall("{call proc_updRx(?,?,?)}");
			cstmt.setString(1, user.getUserid());
			cstmt.setInt(2, type.intValue());
			cstmt.setString(3, sj);
			cstmt.execute();  
		} catch (Exception e) {
			
		}finally{
			DBUtil.close(conn, null, null);
		}
		
		return flag;
	}
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public boolean delRctx(String id){
		boolean flag = false;
		Connection conn = null;
		try {
			conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall("{call proc_delRctx(?)}");
			cstmt.setString(1, id);
			cstmt.execute();  
		} catch (Exception e) {
			logger.error(e,e);
		}
		finally{DBUtil.close(conn, null, null);}
		return flag;
	}
	public List<HashMap<String, Object>> getGzscrx(int pageNumber,int pageSize) {
		//List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
		//List<HashMap<String, Object>> list1=new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> list2=new ArrayList<HashMap<String, Object>>();
		//list1=this.getRctxZq(pageNumber, pageSize);
		list2=this.getRctx(pageNumber, pageSize);
		//list1.addAll(list2);
		return list2;
	}
	public List<HashMap<String, Object>> getGzscrxSize() {
	
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString =sdf.format(date);  
	
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>(); 
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		StringBuffer sb = new StringBuffer();
		sb.append("  select * from ( ");
		sb.append("     select p.*,rownum rn from (    ");
		sb.append("   select * from (  ");
		sb.append("    select s.id, s.title, s.id tid,(select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm,to_char(s.startdate,'yyyy-MM-dd')  sj   ,1 flag,s.remark ");
		sb.append("  FROM  iwork_sch_calendar s LEFT JOIN bd_xp_tyrcglb t  ON to_char(S.ID) = to_char(T.MAINID) LEFT JOIN BD_XP_QTGGZLLC r ON to_char(R.INSTANCEID) = to_char(T.Subid)    WHERE s.userid= ? and s.STARTDATE <= TO_DATE(?,'yyyy-MM-dd')  ");
		sb.append("  and (select count(*) from bd_xp_xpsxqtb where XPSXID = s.id) = 0  and t.subid is not null and r.khbh is not null    and(title not like '%待归还' and title not like '%审批中' and title not like '%已审批'  ");
		sb.append("    and title not like '%已驳回' and title not like '%已归档')   ");
		sb.append("   UNION   ");
		sb.append("  select s.id, s.title, s.id tid,(select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm,to_char(s.startdate,'yyyy-MM-dd') sj    ,1 flag ,s.remark  ");
		sb.append("    FROM  iwork_sch_calendar s LEFT JOIN bd_xp_tyrcglb t  ON S.ID=T.MAINID LEFT JOIN rcywcb r ON to_char(R.INSTANCEID) = to_char(T.Subid)     WHERE s.userid= ? and s.STARTDATE <= TO_DATE(?,'yyyy-MM-dd')   ");
		sb.append("  and (select count(*) from bd_xp_xpsxqtb where XPSXID = s.id) = 0  and t.subid is not null and r.khbh is not null     and(title not like '%待归还' and title not like '%审批中' and title not like '%已审批'  ");
		sb.append("     and title not like '%已驳回' and title not like '%已归档') )  ");
		sb.append("   UNION ");
		sb.append("    select * from (  ");
		sb.append("     select s.id,t.title ,t.id tid,(select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm,to_char(s.txrq,'yyyy-MM-dd')  sj  ,2 flag,t.remark");
		sb.append("    from bd_xp_xpsxqtb s LEFT JOIN iwork_sch_calendar T ON to_char(S.XPSXID)=to_char(T.ID) LEFT JOIN bd_xp_tyrcglb Z ON to_char(Z.MAINID)=to_char(T.ID) LEFT JOIN BD_XP_QTGGZLLC R ON to_char(R.INSTANCEID)=to_char(Z.SUBID) ");
		sb.append("       WHERE s.createuserid = ? and  s.TXRQ<=TO_DATE( ? , 'yyyy-MM-dd') AND s.CUSTOMERNO IS NULL AND Z.SUBID IS NOT NULL and r.khbh IS NOT NULL   ");
		sb.append("         UNION    ");
		sb.append("  select s.id,t.title ,t.id tid,(select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm ,to_char(s.txrq,'yyyy-MM-dd')  sj ,2 flag,t.remark ");
		sb.append("   from bd_xp_xpsxqtb s LEFT JOIN iwork_sch_calendar T ON to_char(S.XPSXID)=to_char(T.ID) LEFT JOIN bd_xp_tyrcglb Z ON to_char(Z.MAINID)=to_char(T.ID) LEFT JOIN rcywcb R ON to_char(R.INSTANCEID)=to_char(Z.SUBID)  ");
		sb.append("       WHERE s.createuserid = ?  and s.TXRQ<=TO_DATE(?, 'yyyy-MM-dd') AND s.CUSTOMERNO IS NULL AND Z.SUBID IS NOT NULL and r.khbh IS NOT NULL ) ) p )  order by tid desc ");
				Connection conn=null;
				ResultSet rs=null;
				PreparedStatement stmt=null;
				try {
					 conn=DBUtil.open();
					 stmt=conn.prepareStatement(sb.toString());
					 stmt.setString(1, userId);
					 stmt.setString(2, dateString);
					 stmt.setString(3, userId);
					 stmt.setString(4, dateString);
					 stmt.setString(5, userId);
					 stmt.setString(6, dateString);
					 stmt.setString(7, userId);
					 stmt.setString(8, dateString);
					
					 rs=stmt.executeQuery();
				    while(rs.next()){
				    	String id=rs.getString("ID");
				    	
				    	HashMap<String,Object> h=new HashMap<String,Object>();
				    	h.put("id", id);
				    	
				    	list.add(h);
				    }
				} catch (Exception e) {
					logger.error(e,e);
				}finally {
					DBUtil.close(conn, stmt, rs);
		    	}

		return list;
	}
	public List<HashMap<String, Object>> getshowRclbSize() {
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
		StringBuffer sb = new StringBuffer();
		sb.append("   select * from ( ");
		sb.append("   select id,subid,cjsj,khbh,title,noticename,zqjc,zqdm,noticesq,x,userid,username, rownum rm from ( ");
		sb.append("   select id,subid,cjsj,khbh,title,noticename,zqjc,zqdm,noticesq,x,userid,username from (");
		sb.append(" select s.id id,to_char(t.cjsj,'yyyy-MM-dd') cjsj,t.subid subid,r.khbh khbh,s.title title,r.noticename noticename, ");
		sb.append("  (select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm,r.noticesq noticesq,1 x  ,s.userid userid ");
		sb.append("  ,(select p.username from orguser p where p.userid=s.userid) username ");
		sb.append(" from iwork_sch_calendar s left join  bd_xp_tyrcglb t on to_char( s.id)= to_char(t.mainid) left join BD_XP_QTGGZLLC r on to_char(r.instanceid)=to_char(t.subid) where r.khbh is not null  ");
		if(user.getOrgroleid()!=5){
			sb.append("  and  t.createuserid= ? ");
		}
		sb.append(" UNION ");
		sb.append(" select s.id id,to_char(t.cjsj,'yyyy-MM-dd') cjsj,t.subid subid,r.khbh khbh,s.title title,r.sxmc noticename ,");
		sb.append("  (select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm,r.noticesq noticesq,2 x ,s.userid userid ");
		sb.append("  ,(select p.username from orguser p where p.userid=s.userid) username ");
		sb.append(" from iwork_sch_calendar s left join  bd_xp_tyrcglb t on to_char( s.id)= to_char(t.mainid) left join rcywcb r on to_char(r.instanceid)=to_char(t.subid) where r.khbh is not null ");
		if(user.getOrgroleid()!=5){
			sb.append("  and  t.createuserid= ? ");
		}
		sb.append("     ) order by cjsj desc,id desc ))    ");
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement stmt=null;
		try {
			 conn=DBUtil.open();
			 stmt=conn.prepareStatement(sb.toString());
			 if(user.getOrgroleid()!=5){
				 stmt.setString(1, userId);
				 stmt.setString(2, userId);
				
			 }
			 rs=stmt.executeQuery();
		    while(rs.next()){
		    	String id=rs.getString("id");
		    	String cjsj=rs.getString("cjsj");
		    	String zqjc=rs.getString("zqjc");
		    	String zqdm=rs.getString("zqdm");
		    	String title=rs.getString("title");
		    	String noticename=rs.getString("noticename");
		    	String subid=rs.getString("subid");
		    	String userid=rs.getString("userid");
		    	String username=rs.getString("username");
		    	int x =rs.getInt("x");
		    	String noticesq=rs.getString("noticesq");
		    	HashMap<String,Object> h=new HashMap<String,Object>();
		    	if(x==1){
		    		HashMap<String,Object> getList=getCsQt(noticesq);
		    		h.put("lcbh", getList.get("lcbh"));
			    	h.put("stepid", getList.get("stepid"));
			    	h.put("taskid", getList.get("taskid"));
		    	}else if(x==2){
		    		HashMap<String,Object> getList=getCsTwo(noticesq);
		    		h.put("lcbh", getList.get("lcbh"));
			    	h.put("stepid", getList.get("stepid"));
			    	h.put("taskid", getList.get("taskid"));
		    	}
		    	if(userid.equals(user.getUserid())){
		    		h.put("del", 0);
		    	}else{
		    		h.put("del", 1);
		    	}
		    	h.put("id", id);
		    	h.put("cjsj", cjsj);
		    	h.put("zqjc", zqjc);
		    	h.put("zqdm", zqdm);
		    	h.put("title", title);
		    	h.put("noticename", noticename);
		    	h.put("subid", subid);
		    	h.put("username", username);
		    	list.add(h);
		    }
		} catch (Exception e) {
			logger.error(e,e);
		}finally {
			DBUtil.close(conn, stmt, rs);
    	}
		return list;
	}
	/**
	 * 日程新菜单显示
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public List<HashMap<String, Object>> getshowRclb(int pageNumber,int pageSize) {
		int start = (pageNumber-1)*pageSize;
		int end = pageNumber*pageSize;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
		StringBuffer sb = new StringBuffer();
		sb.append("   select * from ( ");
		sb.append("   select id,subid,cjsj,khbh,title,noticename,zqjc,zqdm,noticesq,x,userid,username, rownum rm from ( ");
		sb.append("   select id,subid,cjsj,khbh,title,noticename,zqjc,zqdm,noticesq,x,userid,username from (");
		sb.append(" select s.id id,to_char(t.cjsj,'yyyy-MM-dd') cjsj,t.subid subid,r.khbh khbh,s.title title,r.noticename noticename, ");
		sb.append("  (select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm,r.noticesq noticesq,1 x  ,s.userid userid ");
		sb.append("  ,(select p.username from orguser p where p.userid=s.userid) username ");
		sb.append(" from iwork_sch_calendar s left join  bd_xp_tyrcglb t on to_char( s.id)= to_char(t.mainid) left join BD_XP_QTGGZLLC r on to_char(r.instanceid)=to_char(t.subid) where r.khbh is not null  ");
		if(user.getOrgroleid()!=5){
			sb.append("  and  t.createuserid= ? ");
		}
		sb.append(" UNION ");
		sb.append(" select s.id id,to_char(t.cjsj,'yyyy-MM-dd') cjsj,t.subid subid,r.khbh khbh,s.title title,r.sxmc noticename ,");
		sb.append("  (select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm,r.noticesq noticesq,2 x ,s.userid userid ");
		sb.append("  ,(select p.username from orguser p where p.userid=s.userid) username ");
		sb.append(" from iwork_sch_calendar s left join  bd_xp_tyrcglb t on to_char( s.id)= to_char(t.mainid) left join rcywcb r on to_char(r.instanceid)=to_char(t.subid) where r.khbh is not null ");
		if(user.getOrgroleid()!=5){
			sb.append("  and  t.createuserid= ? ");
		}
		sb.append("     ) order by cjsj desc,id desc ))  WHERE rm>? AND rm<=?  ");
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement stmt=null;
		try {
			 conn=DBUtil.open();
			 stmt=conn.prepareStatement(sb.toString());
			 if(user.getOrgroleid()!=5){
				 stmt.setString(1, userId);
				 stmt.setString(2, userId);
				 stmt.setInt(3, start);
				 stmt.setInt(4, end);
			 }else{
				 stmt.setInt(1, start);
				 stmt.setInt(2, end);
			 }
			 rs=stmt.executeQuery();
		    while(rs.next()){
		    	String id=rs.getString("id");
		    	String cjsj=rs.getString("cjsj");
		    	String zqjc=rs.getString("zqjc");
		    	String zqdm=rs.getString("zqdm");
		    	String title=rs.getString("title");
		    	String noticename=rs.getString("noticename");
		    	String subid=rs.getString("subid");
		    	String userid=rs.getString("userid");
		    	String username=rs.getString("username");
		    	int x =rs.getInt("x");
		    	String noticesq=rs.getString("noticesq");
		    	HashMap<String,Object> h=new HashMap<String,Object>();
		    	if(x==1){
		    		HashMap<String,Object> getList=getCsQt(noticesq);
		    		h.put("lcbh", getList.get("lcbh"));
			    	h.put("stepid", getList.get("stepid"));
			    	h.put("taskid", getList.get("taskid"));
		    	}else if(x==2){
		    		HashMap<String,Object> getList=getCsTwo(noticesq);
		    		h.put("lcbh", getList.get("lcbh"));
			    	h.put("stepid", getList.get("stepid"));
			    	h.put("taskid", getList.get("taskid"));
		    	}
		    	if(userid.equals(user.getUserid())){
		    		h.put("del", 0);
		    	}else{
		    		h.put("del", 1);
		    	}
		    	h.put("id", id);
		    	h.put("cjsj", cjsj);
		    	h.put("zqjc", zqjc);
		    	h.put("zqdm", zqdm);
		    	h.put("title", title);
		    	h.put("noticename", noticename);
		    	h.put("subid", subid);
		    	h.put("username", username);
		    	list.add(h);
		    }
		} catch (Exception e) {
			logger.error(e,e);
		}finally {
			DBUtil.close(conn, stmt, rs);
    	}
		return list;
	}
	public HashMap<String,Object> getCsTwo(String noticesq) {
		StringBuffer sb = new StringBuffer();
		sb.append("  select s.lcbh,s.stepid,s.taskid from bd_xp_rcywcbzsj s where s.noticesq=? ");
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement stmt=null;
		HashMap<String,Object> h=new HashMap<String,Object>();
		try {
			 conn=DBUtil.open();
			 stmt=conn.prepareStatement(sb.toString());
			 stmt.setString(1, noticesq);
			 rs=stmt.executeQuery();
		    while(rs.next()){
		    	String lcbh=rs.getString("lcbh");
		    	String stepid=rs.getString("stepid");
		    	String yxid=rs.getString("taskid");
		    	h.put("lcbh", lcbh);
		    	h.put("stepid", stepid);
		    	h.put("taskid", yxid);
		    }
		} catch (Exception e) {
			logger.error(e,e);
		}finally {
			DBUtil.close(conn, stmt, rs);
    	}
		return h;
		
	}
	public HashMap<String,Object> getCsQt(String noticesq) {
		StringBuffer sb = new StringBuffer();
		sb.append("  select s.lcbh,s.stepid,s.rwid from BD_MEET_QTGGZL s where s.noticesq=? ");
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement stmt=null;
		HashMap<String,Object> h=new HashMap<String,Object>();
		try {
			 conn=DBUtil.open();
			 stmt=conn.prepareStatement(sb.toString());
			 stmt.setString(1, noticesq);
			 rs=stmt.executeQuery();
		    while(rs.next()){
		    	String lcbh=rs.getString("lcbh");
		    	String stepid=rs.getString("stepid");
		    	String yxid=rs.getString("rwid");
		    	h.put("lcbh", lcbh);
		    	h.put("stepid", stepid);
		    	h.put("taskid", yxid);
		    }
		} catch (Exception e) {
			logger.error(e,e);
		}finally {
			DBUtil.close(conn, stmt, rs);
    	}
		return h;
		
	}
	/**
	 * 取得周期提醒的日期
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public List<HashMap<String, Object>> getRctxZq(int pageNumber,int pageSize) {
		int start = (pageNumber-1)*pageSize;
		int end = pageNumber*pageSize;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString =sdf.format(date);  
	
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
				String userId = UserContextUtil.getInstance().getCurrentUserId();
				StringBuffer sb = new StringBuffer();
				sb.append(" select * from (  ");
				sb.append("   select s.id,t.title ,t.id tid,t.remark,(select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm, ROWNUM  RNUM   ");
				sb.append("  from bd_xp_xpsxqtb s LEFT JOIN iwork_sch_calendar T ON to_char(S.XPSXID)=to_char(T.ID) LEFT JOIN bd_xp_tyrcglb Z ON to_char(Z.MAINID)=to_char(T.ID) LEFT JOIN BD_XP_QTGGZLLC R ON to_char(R.INSTANCEID)=to_char(Z.SUBID)   ");
				sb.append("  WHERE s.createuserid= ? AND s.TXRQ<=TO_DATE( ? , 'yyyy-MM-dd') AND s.CUSTOMERNO IS NULL AND Z.SUBID IS NOT NULL and r.khbh IS NOT NULL   ");
				sb.append("   UNION  ");
				sb.append("   select s.id,t.title ,t.id tid,t.remark,(select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm, ROWNUM  RNUM   ");
				sb.append("   from bd_xp_xpsxqtb s LEFT JOIN iwork_sch_calendar T ON to_char(S.XPSXID)=to_char(T.ID) LEFT JOIN bd_xp_tyrcglb Z ON to_char(Z.MAINID)=to_char(T.ID) LEFT JOIN rcywcb R ON to_char(R.INSTANCEID)=to_char(Z.SUBID)  ");
				sb.append("  WHERE s.createuserid= ? AND s.TXRQ<=TO_DATE( ? , 'yyyy-MM-dd') AND s.CUSTOMERNO IS NULL AND Z.SUBID IS NOT NULL and r.khbh IS NOT NULL  ");
				
				sb.append("    )  WHERE RNUM > ? AND RNUM <= ? order by id desc ");
				Connection conn=null;
				ResultSet rs=null;
				PreparedStatement stmt=null;
				try {
					 conn=DBUtil.open();
					 stmt=conn.prepareStatement(sb.toString());
					 stmt.setString(1, userId);
					 stmt.setString(2, dateString);
					 stmt.setString(3, userId);
					 stmt.setString(4, dateString);
					 stmt.setInt(5, start);
					 stmt.setInt(6, end);
					 rs=stmt.executeQuery();
				    while(rs.next()){
				    	String id=rs.getString("ID");
				    	String title=rs.getString("TITLE");
				    	String zqjc=rs.getString("zqjc");
				    	String zqdm=rs.getString("zqdm");
				    	String tid=rs.getString("tid");
				    	String remark=rs.getString("remark");
				    	HashMap<String,Object> h=new HashMap<String,Object>();
				    	h.put("id", id);
				    	h.put("title", title);
				    	h.put("tid", tid);
				    	h.put("zqjc", zqjc);
				    	h.put("zqdm", zqdm);
				    	h.put("flag", "0");
				    	h.put("remark", remark);
				    	list.add(h);
				    }
				} catch (Exception e) {
					logger.error(e,e);
				}finally {
					DBUtil.close(conn, stmt, rs);
		    	}

		return list;
	}
	public List<HashMap<String, Object>> getfddRctx(int pageNumber,int pageSize) {
		int start = (pageNumber-1)*pageSize;
		int end = pageNumber*pageSize;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString =sdf.format(date);  
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
				String userId = UserContextUtil.getInstance().getCurrentUserId();
				StringBuffer sb = new StringBuffer();
				sb.append("  select z.* from ( select s.title,s.startdate from iwork_sch_calendar s where s.userid='"+user.getUserid()+"' and s.startdate is not null order by s.startdate desc    ) z where rownum > ? AND rownum <= ? ");
				
				Connection conn=null;
				ResultSet rs=null;
				PreparedStatement stmt=null;
				try {
					 conn=DBUtil.open();
					 stmt=conn.prepareStatement(sb.toString());
				
					 stmt.setInt(1, start);
					 stmt.setInt(2, end);
					 rs=stmt.executeQuery();
				    while(rs.next()){
				    	String title=rs.getString("TITLE");
				    	String sdate=rs.getString("STARTDATE");
				    
				    	HashMap<String,Object> h=new HashMap<String,Object>();
				    	h.put("title", title);
				    	h.put("sdate", sdate);
				    	
				    	list.add(h);
				    }
				} catch (Exception e) {
					logger.error(e,e);
				}finally {
					DBUtil.close(conn, stmt, rs);
		    	}

		return list;
	}
	/**
	 * 取得日程提醒
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public List<HashMap<String, Object>> getRctx(int pageNumber,int pageSize) {
		int start = (pageNumber-1)*pageSize;
		int end = pageNumber*pageSize;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString =sdf.format(date);  
	
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
				String userId = UserContextUtil.getInstance().getCurrentUserId();
				StringBuffer sb = new StringBuffer();
				sb.append("  select * from ( ");
				sb.append("     select p.*,rownum rn from (    ");
				sb.append("   select * from (  ");
				sb.append("    select s.id, s.title, s.id tid,(select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm,to_char(s.startdate,'yyyy-MM-dd')  sj   ,1 flag,s.remark ");
				sb.append("  FROM  iwork_sch_calendar s LEFT JOIN bd_xp_tyrcglb t  ON to_char(S.ID)=to_char(T.MAINID) LEFT JOIN BD_XP_QTGGZLLC r ON to_char(R.INSTANCEID)=to_char(T.Subid)    WHERE s.userid= ? and s.STARTDATE <= TO_DATE(?,'yyyy-MM-dd')  ");
				sb.append("  and (select count(*) from bd_xp_xpsxqtb where XPSXID = s.id) = 0  and t.subid is not null and r.khbh is not null    and(title not like '%待归还' and title not like '%审批中' and title not like '%已审批'  ");
				sb.append("    and title not like '%已驳回' and title not like '%已归档')   ");
				sb.append("   UNION   ");
				sb.append("  select s.id, s.title, s.id tid,(select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm,to_char(s.startdate,'yyyy-MM-dd') sj    ,1 flag ,s.remark  ");
				sb.append("    FROM  iwork_sch_calendar s LEFT JOIN bd_xp_tyrcglb t  ON to_char(S.ID)=to_char(T.MAINID) LEFT JOIN rcywcb r ON to_char(R.INSTANCEID)=to_char(T.Subid)     WHERE s.userid= ? and s.STARTDATE <= TO_DATE(?,'yyyy-MM-dd')   ");
				sb.append("  and (select count(*) from bd_xp_xpsxqtb where XPSXID = s.id) = 0  and t.subid is not null and r.khbh is not null     and(title not like '%待归还' and title not like '%审批中' and title not like '%已审批'  ");
				sb.append("     and title not like '%已驳回' and title not like '%已归档') )  ");
				sb.append("   UNION ");
				sb.append("    select * from (  ");
				sb.append("     select s.id,t.title ,t.id tid,(select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm,to_char(s.txrq,'yyyy-MM-dd')  sj  ,2 flag,t.remark");
				sb.append("    from bd_xp_xpsxqtb s LEFT JOIN iwork_sch_calendar T ON to_char(S.XPSXID)=to_char(T.ID) LEFT JOIN bd_xp_tyrcglb Z ON to_char(Z.MAINID)=to_char(T.ID) LEFT JOIN BD_XP_QTGGZLLC R ON to_char(R.INSTANCEID)=to_char(Z.SUBID) ");
				sb.append("       WHERE s.createuserid = ? and  s.TXRQ<=TO_DATE( ? , 'yyyy-MM-dd') AND s.CUSTOMERNO IS NULL AND Z.SUBID IS NOT NULL and r.khbh IS NOT NULL   ");
				sb.append("         UNION    ");
				sb.append("  select s.id,t.title ,t.id tid,(select y.zqjc from bd_zqb_kh_base y where y.customerno=r.khbh) zqjc,(select y.zqdm from bd_zqb_kh_base y where y.customerno=r.khbh) zqdm ,to_char(s.txrq,'yyyy-MM-dd')  sj ,2 flag,t.remark ");
				sb.append("   from bd_xp_xpsxqtb s LEFT JOIN iwork_sch_calendar T ON to_char(S.XPSXID)=to_char(T.ID) LEFT JOIN bd_xp_tyrcglb Z ON to_char(Z.MAINID)=to_char(T.ID) LEFT JOIN rcywcb R ON to_char(R.INSTANCEID)=to_char(Z.SUBID)  ");
				sb.append("       WHERE s.createuserid = ?  and s.TXRQ<=TO_DATE(?, 'yyyy-MM-dd') AND s.CUSTOMERNO IS NULL AND Z.SUBID IS NOT NULL and r.khbh IS NOT NULL ) ) p ) where rn> ? and rn<= ? order by tid desc ");
				Connection conn=null;
				ResultSet rs=null;
				PreparedStatement stmt=null;
				try {
					 conn=DBUtil.open();
					 stmt=conn.prepareStatement(sb.toString());
					 stmt.setString(1, userId);
					 stmt.setString(2, dateString);
					 stmt.setString(3, userId);
					 stmt.setString(4, dateString);
					 stmt.setString(5, userId);
					 stmt.setString(6, dateString);
					 stmt.setString(7, userId);
					 stmt.setString(8, dateString);
					 stmt.setInt(9, start);
					 stmt.setInt(10, end);
					 rs=stmt.executeQuery();
				    while(rs.next()){
				    	String id=rs.getString("ID");
				    	String title=rs.getString("TITLE");
				    	String tid=rs.getString("TID");
				    	String zqjc=rs.getString("ZQJC");
				    	String zqdm=rs.getString("ZQDM");
				    	String sj=rs.getString("sj");
				    	String flag=rs.getString("flag");
				    	String remark=rs.getString("remark");
				    	HashMap<String,Object> h=new HashMap<String,Object>();
				    	h.put("id", id);
				    	h.put("flag", flag);
				    	h.put("title", title);
				    	h.put("flag", flag);
				    	h.put("zqjc", zqjc);
				    	h.put("zqdm", zqdm);
				    	h.put("tid", tid);
				    	h.put("sj", sj);
				    	h.put("remark", remark);
				    	list.add(h);
				    }
				} catch (Exception e) {
					logger.error(e,e);
				}finally {
					DBUtil.close(conn, stmt, rs);
		    	}

		return list;
	}
	/**获取工作审查信息
	 * @return
	 */
	public List<HashMap<String, Object>> getGzsc(int pageNumber,int pageSize) {
		int start = (pageNumber-1)*pageSize;
		int end = pageNumber*pageSize;
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
				String userId = UserContextUtil.getInstance().getCurrentUserId();
				SimpleDateFormat sdf;
				StringBuffer sb = new StringBuffer();
				sb.append("SELECT INSTANCEID,FSRID,FSR,TZBT,TZNR,FSSJ,FKZT,CID,DID,USERID,NAME,PHONE,EMAIL,EINS,JSZT FROM (SELECT C.INSTANCEID,C.FSRID,C.FSR,C.TZBT,C.TZNR,C.FSSJ,C.ID CID,D.ID DID,D.USERID,D.NAME,D.FKZT,D.PHONE,D.EMAIL,E.INSTANCEID EINS,ROWNUM RNUM,C.JSZT FROM (SELECT INSTANCEID,B.ID,B.FSRID,B.FSR,B.TZBT,B.TZNR,B.FSSJ  , B.JSZT FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查')) A LEFT JOIN BD_XP_GZSC B ON A.DATAID = B.ID) C LEFT JOIN (SELECT A.INSTANCEID,B.ID,B.USERID,B.NAME,B.PHONE,B.EMAIL,B.FKZT FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查事项反馈人表表单')) A LEFT JOIN BD_XP_GZSCSXFKRB B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID AND D.ID IS NOT NULL AND C.ID IS NOT NULL LEFT JOIN (SELECT INSTANCEID,B.DID,B.HFR,ORG.USERNAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查回复')) A LEFT JOIN BD_XP_GZBCHF B ON A.DATAID = B.ID LEFT JOIN ORGUSER ORG ON B.HFR=ORG.USERID) E ON D.ID=E.DID WHERE USERID=?) WHERE RNUM>?  AND RNUM<=? ORDER BY FSSJ DESC");
				Connection conn=null;
				ResultSet rs=null;
				PreparedStatement stmt=null;
				try {
					 conn=DBUtil.open();
					 stmt=conn.prepareStatement(sb.toString());
					 stmt.setString(1, userId);
					 stmt.setInt(2, start);
					 stmt.setInt(3, end);
					 rs=stmt.executeQuery();
				    while(rs.next()){
				    	sdf = new SimpleDateFormat("yyyy-MM-dd");
				    	BigDecimal instanceid=rs.getBigDecimal("INSTANCEID");
				    	String tzbt=rs.getString("TZBT");
				    	String fsr=rs.getString("FSR");
				    	String tznr=rs.getString("TZNR");
				    	BigDecimal cid=rs.getBigDecimal("CID");
				    	BigDecimal did=rs.getBigDecimal("DID");
				    	String fkzt=rs.getString("FKZT");
				    	String eins=rs.getString("EINS");
				    	String fssj=rs.getString("FSSJ");
				    	String jszt=rs.getString("JSZT");
				    	HashMap<String,Object> h=new HashMap<String,Object>();
				    	h.put("INSTANCEID", instanceid==null?"":instanceid.toString());
				    	h.put("TZBT", tzbt);
				    	h.put("FSR", fsr);
				    	h.put("TZNR", tznr);
				    	h.put("CID", cid==null?"":cid.toString());
				    	h.put("DID", did==null?"":did.toString());
				    	h.put("FKZT", fkzt);
				    	h.put("EINS", eins);
				    	h.put("FSSJ", fssj);
				    	h.put("JSZT", jszt);
				    	list.add(h);
				    }
				} catch (Exception e) {
					logger.error(e,e);
				}finally {
					DBUtil.close(conn, stmt, rs);
		    	}

		return list;
	}
	public int getHyCount() {
		int num=0;		
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();	
				// 判断角色
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				if (uc == null) { return num;} //xlj 漏洞扫描 2018年5月15日11:08:39
				// 场外看到所有的，持续督导看到自己分管的
				String userId = UserContextUtil.getInstance().getCurrentUserId();
				String username = UserContextUtil.getInstance().getCurrentUserFullName();	
				StringBuffer sb = new StringBuffer();
				Long orgRoleId = uc.get_userModel().getOrgroleid();			
				
					if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {// 场外或质控部负责人,看到所有
						sb.append("  select * from (select BOTABLE.id,meetname,customername,to_char(plantime,'yyyy-MM-dd HH24:mi') plantime,status,rownum num,BINDTABLE.INSTANCEID");
						sb.append(" from BD_MEET_PLAN BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=96 and BINDTABLE.metadataid=109 ");
						sb.append("  where to_char(plantime, 'yyyyMMDD') >= to_char(sysdate, 'yyyyMMDD')");
						sb.append("     and to_char(plantime, 'yyyyMMDD') <=");
						sb.append("      to_char(sysdate+2, 'yyyyMMDD') and ( ycl  is null or ycl='0') )  order by plantime");
						
					}else if(orgRoleId.equals(new Long(3)))//董秘登陆看自己的
					{						
						sb.append(" select * from (select BOTABLE.id,meetname,customername,to_char(plantime,'yyyy-MM-dd HH24:mi') plantime,status,rownum num,BINDTABLE.INSTANCEID");
						sb.append(" from BD_MEET_PLAN BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=96 and BINDTABLE.metadataid=109 ");
						sb.append("  where to_char(plantime, 'yyyyMMDD') >= to_char(sysdate, 'yyyyMMDD')");
						sb.append("     and to_char(plantime, 'yyyyMMDD') <=");
						sb.append("      to_char(sysdate+2, 'yyyyMMDD')");
						sb.append("   and  customerno=?  and ( ycl  is null or ycl='0')  )  order by plantime");
					}
					else
					{// 其他人员,看分派给自己的
						sb.append(" select * from (select BOTABLE.id,meetname,customername,to_char(plantime,'yyyy-MM-dd HH24:mi') plantime,status,rownum num,BINDTABLE.INSTANCEID");
						sb.append(" from BD_MEET_PLAN BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=96 and BINDTABLE.metadataid=109 ");
						sb.append("  where to_char(plantime, 'yyyyMMDD') >= to_char(sysdate, 'yyyyMMDD')");
						sb.append("     and to_char(plantime, 'yyyyMMDD') <=");
						sb.append("      to_char(sysdate+2, 'yyyyMMDD')");
						sb.append("   and  customerno in(select khbh from BD_MDM_KHQXGLB where khfzr=? or zzcxdd=? or fhspr=? or zzspr=? or ggfbr=?) and ( ycl  is null or ycl='0')  ) order by plantime");
					
					}
				
				Connection conn=null;
				ResultSet rs=null;
				PreparedStatement stmt=null;
				try {
					conn=DBUtil.open();
					 stmt=conn.prepareStatement(sb.toString());
					 if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {// 场外或质控部负责人,看到所有
						
						
					 }else if(orgRoleId.equals(new Long(3)))//董秘登陆看自己的
						{
						stmt.setString(1,uc.get_userModel().getExtend1());
					 	
						}
					 else{
						 stmt.setString(1, username);
							stmt.setString(2, username);
							stmt.setString(3, username);
							stmt.setString(4, username);
							stmt.setString(5, username);
					 }
					rs=stmt.executeQuery();
				    while(rs.next()){
				    	
				    	Long id=rs.getLong("ID");
				    	HashMap<String,Object> h=new HashMap<String,Object>();
				    
				    	h.put("ID", id);
				    	list.add(h);
				    }
				} catch (Exception e) {
					logger.error(e,e);
				}finally {
					DBUtil.close(conn, stmt, rs);
		    	}
			if(list!=null && list.size()>0){
				num=list.size();
			}
		return num;
	}

	public int getSxCount() {
		int num=0;
		return num;
	}
	
	public int getGzscCount() {
		int i=0;
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
		// 场外看到所有的，持续督导看到自己分管的
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(*) NUM FROM (SELECT C.INSTANCEID,C.FSRID,C.FSR,C.TZBT,C.TZNR,C.FSSJ,C.ID CID,D.ID DID,D.USERID,D.NAME,D.FKZT,D.PHONE,D.EMAIL,E.INSTANCEID EINS,ROWNUM RNUM FROM (SELECT INSTANCEID,B.ID,B.FSRID,B.FSR,B.TZBT,B.TZNR,B.FSSJ FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查')) A LEFT JOIN BD_XP_GZSC B ON A.DATAID = B.ID) C LEFT JOIN (SELECT A.INSTANCEID,B.ID,B.USERID,B.NAME,B.PHONE,B.EMAIL,B.FKZT FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查事项反馈人表表单')) A LEFT JOIN BD_XP_GZSCSXFKRB B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID AND D.ID IS NOT NULL AND C.ID IS NOT NULL LEFT JOIN (SELECT INSTANCEID,B.DID,B.HFR,ORG.USERNAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查回复')) A LEFT JOIN BD_XP_GZBCHF B ON A.DATAID = B.ID LEFT JOIN ORGUSER ORG ON B.HFR=ORG.USERID) E ON D.ID=E.DID) WHERE  USERID=?");
		Connection conn=DBUtil.open();
		ResultSet rs=null;
		PreparedStatement stmt=null;
		try {
			stmt=conn.prepareStatement(sb.toString());
			stmt.setString(1, userId);
			rs=stmt.executeQuery();
			if(rs.next()){
				i=rs.getInt("num");
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally {
			DBUtil.close(conn, stmt, rs);
		}
		return i;
	}

	public int getXpxxCount() {
		int i=0;
		String currentUserFullName = UserContextUtil.getInstance().getCurrentUserFullName();
		boolean isSuperMan = getIsSuperMan();
		boolean dm = isDm();
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql=new StringBuffer("select count(1) num from (select CUSTOMERNAME from (");
		if(!dm){
			sql.append(" select NFILE.customername from ( ");
			sql.append(" select kh.customername,xpbc.xpfile FILEID from ");
			if(dm){
				String extend1 = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1();
				sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB WHERE KHBH=?)");
				params.add(extend1);
			}else if(isSuperMan){
				sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB)");
			}else{
				sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB WHERE KHFZR=? OR ZZCXDD=? OR FHSPR=? OR ZZSPR=? OR GGFBR=?)");
				params.add(currentUserFullName);
				params.add(currentUserFullName);
				params.add(currentUserFullName);
				params.add(currentUserFullName);
				params.add(currentUserFullName);
			}
			sql.append(" cx inner join (select customerno,customername from bd_zqb_kh_base where status='有效') kh on cx.khbh=kh.customerno inner join (SELECT ID,CUSTOMERNO,REGEXP_SUBSTR(XPFILE ,'[^,]+',1,l) XPFILE FROM (SELECT  XPQT.ID,XPQT.CUSTOMERNO,BCWJ.XPFILE FROM BD_XP_XPSXQTB XPQT INNER JOIN BD_XP_XPSXBCWJB BCWJ ON XPQT.ID=BCWJ.XPSXQTID) R,(SELECT LEVEL l FROM DUAL CONNECT BY LEVEL<=100) B  WHERE l <=LENGTH(XPFILE) - LENGTH(REPLACE(XPFILE,','))+1) XPBC on cx.khbh=xpbc.customerno LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(select ID from sys_engine_metadata WHERE ENTITYNAME='BD_XP_XPSXQTB')) BIND ON XPBC.ID=BIND.DATAID");
			sql.append(" union all");
			sql.append(" select kh.customername,GGSMFILE.SMJ FILEID from ");
			if(dm){
				String extend1 = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1();
				sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB WHERE KHBH=?)");
				params.add(extend1);
			}else if(isSuperMan){
				sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB)");
			}else{
				sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB WHERE KHFZR=? OR ZZCXDD=? OR FHSPR=? OR ZZSPR=? OR GGFBR=?)");
				params.add(currentUserFullName);
				params.add(currentUserFullName);
				params.add(currentUserFullName);
				params.add(currentUserFullName);
				params.add(currentUserFullName);
			}
			sql.append(" cx inner join (select customerno,customername from bd_zqb_kh_base where status='有效') kh on cx.khbh=kh.customerno inner join (SELECT ID,KHBH,REGEXP_SUBSTR(SMJ ,'[^,]+',1,l) SMJ FROM (SELECT GG.ID,GG.KHBH,GGSM.SMJ FROM BD_MEET_QTGGZL GG INNER JOIN BD_XP_GGSMJ GGSM ON GG.ID=GGSM.GGID) R,(SELECT LEVEL l FROM DUAL CONNECT BY LEVEL<=100) B WHERE l <=LENGTH(SMJ) - LENGTH(REPLACE(SMJ,','))+1) GGSMFILE ON CX.KHBH=GGSMFILE.KHBH LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(select ID from sys_engine_metadata WHERE ENTITYNAME='BD_MEET_QTGGZL')) BIND ON GGSMFILE.ID=BIND.DATAID");
			sql.append(" ) NFILE INNER JOIN (select FILE_ID from sys_upload_file where UPLOAD_TIME>=to_char(sysdate-15,'yyyy-mm-dd') AND UPLOAD_TIME<=to_char(sysdate+1,'yyyy-mm-dd')) SFILE ON NFILE.FILEID=SFILE.FILE_ID");
			sql.append( " union all");
		}
		sql.append(" select kh.customername from ");
		if(dm){
			String extend1 = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1();
			sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB WHERE KHBH=?)");
			params.add(extend1);
		}else if(isSuperMan){
			sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB)");
		}else{
			sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB WHERE KHFZR=? OR ZZCXDD=? OR FHSPR=? OR ZZSPR=? OR GGFBR=?)");
			params.add(currentUserFullName);
			params.add(currentUserFullName);
			params.add(currentUserFullName);
			params.add(currentUserFullName);
			params.add(currentUserFullName);
		}
		sql.append(" cx inner join (select customerno,customername from bd_zqb_kh_base where status='有效') kh on cx.khbh=kh.customerno inner join (SELECT GG.ID,GG.KHBH,GZ.ID GZID,CASE WHEN GZYJHFSJ is null THEN GZYJLRSJ WHEN GZYJHFSJ is not null THEN GZYJHFSJ ELSE null END TIME FROM BD_MEET_QTGGZL GG INNER JOIN (SELECT GZYJ.ID,GZYJ.GGID,GZYJ.KHBH,GZYJ.GZYJLRSJ,YJHF.GZYJHFSJ FROM BD_XP_GZXTYJB GZYJ LEFT JOIN BD_XP_GZXTYJHFB YJHF ON GZYJ.ID=YJHF.GZYJID WHERE (to_char(GZYJ.GZYJLRSJ,'yyyy-mm-dd')>=to_char(sysdate-15,'yyyy-mm-dd') AND to_char(GZYJ.GZYJLRSJ,'yyyy-mm-dd')<=to_char(sysdate,'yyyy-mm-dd')) or (to_char(YJHF.GZYJHFSJ,'yyyy-mm-dd')>=to_char(sysdate-15,'yyyy-mm-dd') AND to_char(YJHF.GZYJHFSJ,'yyyy-mm-dd')<=to_char(sysdate,'yyyy-mm-dd'))) GZ ON GG.ID=GZ.GGID) GGGZYJ ON CX.KHBH=GGGZYJ.KHBH LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(select ID from sys_engine_metadata WHERE ENTITYNAME='BD_MEET_QTGGZL')) BIND ON GGGZYJ.ID=BIND.DATAID))");
		Connection conn=DBUtil.open();
		ResultSet rs=null;
		PreparedStatement stmt=null;
		try {
			 stmt=conn.prepareStatement(sql.toString());
			 for (int j = 0; j < params.size(); j++) {
				stmt.setObject(j+1, params.get(j));
			}
			 rs=stmt.executeQuery();
			 if(rs.next()){
			    	i=rs.getInt("NUM");
			    }
		} catch (Exception e) {
			logger.error(e,e);
		}finally {
			DBUtil.close(conn, stmt, rs);
    	}
		return i;
	}

	public List<HashMap<String, Object>> getXpxxList(int pageNumber, int pageSize) {
		int start = (pageNumber-1)*pageSize;
		int end = pageNumber*pageSize;
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
		String currentUserFullName = UserContextUtil.getInstance().getCurrentUserFullName();
		boolean isSuperMan = getIsSuperMan();
		boolean dm = isDm();
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql=new StringBuffer("SELECT TYPE,TYPENAME,CUSTOMERNAME,CUSTOMERNO,ZQJC,ZQDM,ID,TITLE,FILEID,FILE_SRC_NAME,TIME,INSTANCEID FROM (SELECT TYPE,TYPENAME,CUSTOMERNAME,CUSTOMERNO,ZQJC,ZQDM,ID,TITLE,FILEID,FILE_SRC_NAME,TIME,INSTANCEID,ROWNUM RNUM FROM (SELECT TYPE,TYPENAME,CUSTOMERNAME,CUSTOMERNO,ZQJC,ZQDM,ID,TITLE,FILEID,FILE_SRC_NAME,TIME,INSTANCEID FROM (");
		if(!dm){
			//xlj update 2017年6月14日09:24:23  该表已废弃不用
			sql.append( "SELECT NFILE.TYPE,NFILE.TYPENAME,NFILE.CUSTOMERNAME,NFILE.CUSTOMERNO,NFILE.ZQJC,NFILE.ZQDM,NFILE.ID,NFILE.TITLE,NFILE.FILEID,SFILE.FILE_SRC_NAME,SFILE.UPLOAD_TIME TIME,NFILE.INSTANCEID FROM (");
			//sql.append( " SELECT 2 TYPE,'备查文件' TYPENAME,KH.CUSTOMERNAME,KH.CUSTOMERNO,KH.ZQJC,KH.ZQDM,XPBC.ID,XPBC.XPSXNAME TITLE,XPBC.XPFILE FILEID,BIND.INSTANCEID FROM "+cxdd+" CX INNER JOIN (SELECT ZQJC,ZQDM,CUSTOMERNO,CUSTOMERNAME FROM BD_ZQB_KH_BASE WHERE STATUS='有效') KH ON CX.KHBH=KH.CUSTOMERNO INNER JOIN (SELECT ID,XPSXNAME,CUSTOMERNO,REGEXP_SUBSTR(XPFILE ,'[^,]+',1,L) XPFILE FROM (SELECT  XPQT.XPSXNAME,XPQT.ID,XPQT.CUSTOMERNO,BCWJ.XPFILE FROM BD_XP_XPSXQTB XPQT INNER JOIN BD_XP_XPSXBCWJB BCWJ ON XPQT.ID=BCWJ.XPSXQTID) R,(SELECT LEVEL L FROM DUAL CONNECT BY LEVEL<=100) B  WHERE L <=LENGTH(XPFILE) - LENGTH(REPLACE(XPFILE,','))+1) XPBC ON CX.KHBH=XPBC.CUSTOMERNO LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XPSXQTB')) BIND ON XPBC.ID=BIND.DATAID ");
			//sql.append( " UNION ALL");
			sql.append( " SELECT 3 TYPE,'扫描件' TYPENAME,KH.CUSTOMERNAME,KH.CUSTOMERNO,KH.ZQJC,KH.ZQDM,GGSMFILE.ID,GGSMFILE.NOTICENAME TITLE,GGSMFILE.SMJ FILEID,BIND.INSTANCEID FROM ");
			if(dm){
				String extend1 = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1();
				sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB WHERE KHBH=?)");
				params.add(extend1);
			}else if(isSuperMan){
				sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB)");
			}else{
				sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB WHERE KHFZR=? OR ZZCXDD=? OR FHSPR=? OR ZZSPR=? OR GGFBR=?)");
				params.add(currentUserFullName);
				params.add(currentUserFullName);
				params.add(currentUserFullName);
				params.add(currentUserFullName);
				params.add(currentUserFullName);
			}
			sql.append(" CX INNER JOIN (SELECT ZQJC,ZQDM,CUSTOMERNO,CUSTOMERNAME FROM BD_ZQB_KH_BASE WHERE STATUS='有效') KH ON CX.KHBH=KH.CUSTOMERNO INNER JOIN (SELECT ID,NOTICENAME,KHBH,REGEXP_SUBSTR(SMJ ,'[^,]+',1,L) SMJ FROM (SELECT GG.ID,GG.NOTICENAME,GG.KHBH,GGSM.SMJ FROM BD_MEET_QTGGZL GG INNER JOIN BD_XP_GGSMJ GGSM ON GG.ID=GGSM.GGID) R,(SELECT LEVEL L FROM DUAL CONNECT BY LEVEL<=100) B WHERE L <=LENGTH(SMJ) - LENGTH(REPLACE(SMJ,','))+1) GGSMFILE ON CX.KHBH=GGSMFILE.KHBH LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_MEET_QTGGZL')) BIND ON GGSMFILE.ID=BIND.DATAID) NFILE ");
			sql.append( " INNER JOIN ");
			sql.append( " (SELECT FILE_ID,FILE_SRC_NAME,UPLOAD_TIME FROM SYS_UPLOAD_FILE WHERE UPLOAD_TIME>=TO_CHAR(sysdate-15,'yyyy-mm-dd') AND UPLOAD_TIME<=TO_CHAR(sysdate+1,'yyyy-mm-dd')) SFILE ON NFILE.FILEID=SFILE.FILE_ID");
			sql.append( " UNION ALL");
		}
		sql.append(" SELECT 1 TYPE,'股转意见' TYPENAME,KH.CUSTOMERNAME,KH.CUSTOMERNO,KH.ZQJC,KH.ZQDM,GGGZYJ.ID,GGGZYJ.NOTICENAME TITLE,TO_CHAR(GGGZYJ.GZID) FILEID,'' FILE_SRC_NAME,TO_CHAR(GGGZYJ.TIME,'yyyy-mm-dd hh24:mi:ss') TIME,BIND.INSTANCEID FROM ");
		if(dm){
			String extend1 = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1();
			sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB WHERE KHBH=?)");
			params.add(extend1);
		}else if(isSuperMan){
			sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB)");
		}else{
			sql.append("(SELECT KHBH FROM BD_MDM_KHQXGLB WHERE KHFZR=? OR ZZCXDD=? OR FHSPR=? OR ZZSPR=? OR GGFBR=?)");
			params.add(currentUserFullName);
			params.add(currentUserFullName);
			params.add(currentUserFullName);
			params.add(currentUserFullName);
			params.add(currentUserFullName);
		}
		sql.append(" CX INNER JOIN (SELECT ZQJC,ZQDM,CUSTOMERNO,CUSTOMERNAME FROM BD_ZQB_KH_BASE WHERE STATUS='有效') KH ON CX.KHBH=KH.CUSTOMERNO INNER JOIN (SELECT GG.ID,GG.NOTICENAME,GG.KHBH,GZ.ID GZID,CASE WHEN GZYJHFSJ IS NULL THEN GZYJLRSJ WHEN GZYJHFSJ IS NOT NULL THEN GZYJHFSJ ELSE NULL END TIME FROM BD_MEET_QTGGZL GG INNER JOIN (SELECT GZYJ.ID,GZYJ.GGID,GZYJ.KHBH,GZYJ.GZYJLRSJ,YJHF.GZYJHFSJ FROM BD_XP_GZXTYJB GZYJ LEFT JOIN BD_XP_GZXTYJHFB YJHF ON GZYJ.ID=YJHF.GZYJID WHERE (TO_CHAR(GZYJ.GZYJLRSJ,'yyyy-mm-dd')>=TO_CHAR(sysdate-15,'yyyy-mm-dd') AND TO_CHAR(GZYJ.GZYJLRSJ,'yyyy-mm-dd')<=TO_CHAR(sysdate,'yyyy-mm-dd')) OR (TO_CHAR(YJHF.GZYJHFSJ,'yyyy-mm-dd')>=TO_CHAR(sysdate-15,'yyyy-mm-dd') AND TO_CHAR(YJHF.GZYJHFSJ,'yyyy-mm-dd')<=TO_CHAR(sysdate,'yyyy-mm-dd'))) GZ ON GG.ID=GZ.GGID) GGGZYJ ON CX.KHBH=GGGZYJ.KHBH LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_MEET_QTGGZL')) BIND ON GGGZYJ.ID=BIND.DATAID");
		sql.append(" ) ORDER BY TIME DESC,TYPE)) WHERE RNUM>? AND RNUM<=?");
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement stmt=null;
		try {
			conn=DBUtil.open();
			 stmt=conn.prepareStatement(sql.toString());
			 int k=1;
			 for (int j = 0; j < params.size(); j++) {
					stmt.setObject(k, params.get(j));k++;
			}
			 stmt.setInt(k, start);k++;
			 stmt.setInt(k, end);k++;
			 rs=stmt.executeQuery();
			 while(rs.next()){
		    	String type=rs.getString("TYPE");
		    	String typename=rs.getString("TYPENAME");
		    	String customername=rs.getString("CUSTOMERNAME");
		    	String customerno=rs.getString("CUSTOMERNO");
		    	String zqdm=rs.getString("ZQDM");
		    	String zqjc=rs.getString("ZQJC");
		    	String title=rs.getString("TITLE");
		    	String fileid=rs.getString("FILEID");
		    	String filesrcname=rs.getString("FILE_SRC_NAME");
		    	String time=rs.getString("TIME");
		    	BigDecimal id=rs.getBigDecimal("ID");
		    	BigDecimal instanceid=rs.getBigDecimal("INSTANCEID");
		    	HashMap<String,Object> h=new HashMap<String,Object>();
		    	h.put("TYPE", type);
		    	h.put("TYPENAME", typename);
		    	h.put("CUSTOMERNAME", customername);
		    	h.put("CUSTOMERNO", customerno);
		    	h.put("ZQJC", zqjc);
		    	h.put("ZQDM", zqdm);
		    	h.put("TITLE", title);
		    	h.put("FILEID", fileid);
		    	h.put("FILESRCNAME", filesrcname);
		    	h.put("TIME", time);
		    	h.put("ID", id);
		    	h.put("INSTANCEID", instanceid);
		    	list.add(h);
		    }
		} catch (Exception e) {
			logger.error(e,e);
		}finally {
			DBUtil.close(conn, stmt, rs);
    	}
		return list;
	}
}
