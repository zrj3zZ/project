package com.iwork.admin.dashboard.service;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.iwork.admin.dashboard.DashboardFactory;
import com.iwork.admin.dashboard.SysConsoleDashboardInterface;
import com.iwork.admin.framework.model.DashboardModel;
import com.iwork.app.persion.constant.SysManagerConstant;
import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.core.organization.tools.UserContextUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SysDashboardService {
	private static String[] column = {"column_1","column_2","column_3"};
	public String getPortletsHtml(){
		StringBuffer html = new StringBuffer();
		String layout = "";
		//获得系统设置布局列表
		SysPersonConfig config = UserContextUtil.getInstance().getCurrentUserConfig(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT);
		if(config!=null){ 
			layout = config.getValue();
		}
//		layout = "[{\"column_1\":[\"memoryMonitor\",\"messageBoard\",\"modleMonitor\",\"userMonitor\"]},{\"column_2\":[\"processTaskMonitor\",\"userOnline\"]},{\"column_3\":[]}]";
		StringBuffer columnStr1 = new StringBuffer();
		StringBuffer columnStr2 = new StringBuffer();
		StringBuffer columnStr3 = new StringBuffer();
		try{ 
		if(layout!=null&&!layout.equals("")){
			JSONArray list = JSONArray.fromObject(layout);
			Iterator iterator = list.iterator();
			while(iterator.hasNext()){
				JSONObject jsonObject = (JSONObject)iterator.next();
				JSONArray column1 = null;
				try{
				column1 = jsonObject.getJSONArray("column_1");
				columnStr1.append("<ul id=\"column_1\" class=\"column\">  ").append("\n");
				}catch(Exception e){}
				
				if(column1!=null){
					Iterator itr = column1.iterator();
					while(itr.hasNext()){
						String columnlist1 = (String)itr.next();
						DashboardModel model = DashboardFactory.getModel(columnlist1);
						if(model==null)continue;
						 //获得栏目样式
						String style = "widget color-yellow";
						columnStr1.append("<li class=\"widget ").append(style).append("\" id=\"widget_").append(model.getKey()).append("\">  ").append("\n");
						columnStr1.append("                <div class=\"widget-head\">").append("\n");
						columnStr1.append("                    <h3>").append(model.getTitle()).append("</h3>").append("\n");
						columnStr1.append("                </div>").append("\n");
						columnStr1.append("                <div class=\"widget-content\"");
						if(model.getHeight()==null||model.getHeight().equals("auto")){
							
						}else{
							columnStr1.append(" style=\"height:").append(model.getHeight()).append("px\"");
						}
						columnStr1.append(" >").append("\n");
						
						columnStr1.append(this.getPortletContent(model));
						columnStr1.append("                </div>").append("\n");
						columnStr1.append("            </li>").append("\n");
					}
				}
				JSONArray column2 = null;
				try{
				column2 = 	jsonObject.getJSONArray("column_2");
				columnStr2.append("<ul id=\"column_2\" class=\"column\">  ").append("\n");
				}catch(Exception e){}
				
				if(column2!=null){
					Iterator itr2 = column2.iterator();
					while(itr2.hasNext()){
						String columnlist1 = (String)itr2.next();
						DashboardModel model = DashboardFactory.getModel(columnlist1);
						if(model==null)continue;
						 //获得栏目样式
						String style = "widget color-yellow";
						columnStr2.append("<li class=\"widget ").append(style).append("\" id=\"widget_").append(model.getKey()).append("\">  ").append("\n");
						columnStr2.append("                <div class=\"widget-head\">").append("\n");
						columnStr2.append("                    <h3>").append(model.getTitle()).append("</h3>").append("\n");
						columnStr2.append("                </div>").append("\n");
						columnStr2.append("                <div class=\"widget-content\"");
						if(model.getHeight()==null||model.getHeight().equals("auto")){
							
						}else{
							columnStr2.append(" style=\"height:").append(model.getHeight()).append("px\"");
						}
						columnStr2.append(" >").append("\n");
						columnStr2.append(this.getPortletContent(model));
						columnStr2.append("                </div>").append("\n");
						columnStr2.append("            </li>").append("\n");
					}
				}
				
				JSONArray column3 = null;
				try{
					column3 = 	jsonObject.getJSONArray("column_3");
					columnStr3.append("<ul id=\"column_3\" class=\"column\">  ").append("\n");
				}catch(Exception e){}
				
				if(column3!=null){
					Iterator itr3 = column3.iterator();
					while(itr3.hasNext()){
						String columnlist1 = (String)itr3.next();
						DashboardModel model = DashboardFactory.getModel(columnlist1);
						if(model==null)continue;
						 //获得栏目样式
						String style = "widget color-yellow";
						columnStr3.append("<li class=\"widget ").append(style).append("\" id=\"widget_").append(model.getKey()).append("\">  ").append("\n");
						columnStr3.append("                <div class=\"widget-head\">").append("\n");
						columnStr3.append("                    <h3>").append(model.getTitle()).append("</h3>").append("\n");
						columnStr3.append("                </div>").append("\n");
						columnStr3.append("                <div class=\"widget-content\"");
						if(model.getHeight()==null||model.getHeight().equals("auto")){
							
						}else{
							columnStr3.append(" style=\"height:").append(model.getHeight()).append("px\"");
						}
						columnStr3.append(" >").append("\n");
						columnStr3.append(this.getPortletContent(model));
						columnStr3.append("                </div>").append("\n");
						columnStr3.append("            </li>").append("\n");
					}
				}
			}
			 columnStr1.append(" </ul>").append("\n"); 
			 columnStr2.append(" </ul>").append("\n"); 
			 columnStr3.append(" </ul>").append("\n"); 
		}
		}catch(Exception e){
			
		}
		html.append(columnStr1).append(columnStr2).append(columnStr3);
		return html.toString();
	}
	/**
	 * 获得portlet正文
	 * @param model
	 * @return
	 */
	private String getPortletContent(DashboardModel model){
		String content = "";
		SysConsoleDashboardInterface templateEngine = null;
		if (model != null) {
			Constructor cons = null;
			try {
				cons = model.getCons();
				if (cons != null) {
					Object[] params = {};
					templateEngine = (SysConsoleDashboardInterface) cons.newInstance(params);
				}
			} catch (Exception e) {
				
			}
		}
		
		if(templateEngine!=null)
			content = templateEngine.getContent();
		
		if(content==null){
			content = "未发现数据";
		}
		return content;
	}
	
	
	/**
	 * 移动portlet
	 * @return
	 */
	public boolean movePortlet(String orderlist){
		boolean flag = false;
		SysPersonConfig config = UserContextUtil.getInstance().getCurrentUserConfig(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT);
		if(config!=null){
			if(config.getValue().equals(orderlist)){
				flag = true;
			}else{
				config.setValue(orderlist);
				UserContextUtil.getInstance().setCurrentUserConfig(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT, config);
			}
		}else{
			 config = new SysPersonConfig();
			 config.setType(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT);
			 config.setUserid(UserContextUtil.getInstance().getCurrentUserId());
			 config.setValue(orderlist);
			 UserContextUtil.getInstance().setCurrentUserConfig(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT, config);
		}
		return flag;
	}
	/**
	 * 移动portlet
	 * @return
	 */
	public boolean updatePortlet(String portletid,String title,String style){
		
		return true;
	}
	
	/**
	 * 移除portlet
	 * @return
	 */
	public boolean removePortlet(String id,String orderlist){
		boolean flag = false;
		JSONArray list = JSONArray.fromObject(orderlist);
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			JSONObject jobj = (JSONObject)iterator.next();
			for(String temp:column){
				JSONArray jsonArray = null;
				try{
					jsonArray = jobj.getJSONArray(temp);
				}catch(Exception e){}
				if(jsonArray!=null){
					Iterator subite = jsonArray.iterator();
					while(subite.hasNext()){
						String str = (String)subite.next();
						if(str.equals(id)){
							subite.remove();
							break;
						}
					}
				}
			}
		}
		String newOderlist = list.toString();
		SysPersonConfig config = UserContextUtil.getInstance().getCurrentUserConfig(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT);
		if(config!=null){
			config.setValue(newOderlist);
			 UserContextUtil.getInstance().setCurrentUserConfig(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT, config);
			 flag = true;
		}
		return flag;
	}
	/**
	 * 设置portlet列表
	 * @param ids
	 */
	public boolean setPortletList(String ids){
		boolean flag = false;
		if(ids!=null){
			String orderlist = "";
			SysPersonConfig config = UserContextUtil.getInstance().getCurrentUserConfig(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT);
			if(config!=null){
				orderlist = config.getValue();
			}
			if(orderlist.equals("")){
				HashMap root = new HashMap();
				String[]  id = ids.split(",");
				List<String> list1 = new ArrayList();
				List<String> list2 = new ArrayList();
				List<String> list3 = new ArrayList();
				for(int i=0;i<id.length;i++){
					String tmp = id[i];
					if(!tmp.equals("")){
						list1.add(tmp);
					}
					i++;
					if(i>=id.length)break;
					tmp = id[i];
					if(!tmp.equals("")){
						list2.add(tmp);
					}
					i++;
					if(i>=id.length)break;
					tmp = id[i];
					if(!tmp.equals("")){
						list3.add(tmp);
					}
				}
				root.put("column_1", list1);
				root.put("column_2", list2);
				root.put("column_3", list3);
				JSONArray json = JSONArray.fromObject(root);
				orderlist = json.toString();
				 config = new SysPersonConfig();
				 config.setType(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT);
				 config.setUserid(UserContextUtil.getInstance().getCurrentUserId());
				 config.setValue(orderlist);
				 UserContextUtil.getInstance().setCurrentUserConfig(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT, config);
			}else{
				JSONArray jsonlist = JSONArray.fromObject(orderlist);
				Iterator iterator = jsonlist.iterator();
				String[]  idlist = ids.split(",");
				String[] column = {"column_1","column_2","column_3"};
				while(iterator.hasNext()){
					JSONObject jobj = (JSONObject)iterator.next();
					try{
					JSONArray jsonArray = jobj.getJSONArray("column_1");
					for(String id:idlist){
						jsonArray.add(id);
					}
					}catch(Exception e){}
					
				}
				String setlist = jsonlist.toString();
				config.setValue(setlist);
				 UserContextUtil.getInstance().setCurrentUserConfig(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT, config);
			}
			
			
		}
		return flag;
	}
	
	/**
	 * 获得当前用户的通用门户组件树JSON
	 * @return
	 */
	public String showOverAllTreeJSON(){
		//获取指定用户的权限组列表
		StringBuffer html = new StringBuffer();
		String orderlist = "";
		SysPersonConfig config = UserContextUtil.getInstance().getCurrentUserConfig(SysManagerConstant.ADMIN_DASHBOARD_LAYOUT);
		if(config!=null){
			orderlist = config.getValue();
		}
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		List<String> list = DashboardFactory.getGroupList();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		 List<DashboardModel> selectlist = DashboardFactory.getSortList();
		int count = 0;
		for(String groupname:list){
			HashMap hash = new HashMap();
			count++;
			if(groupname!=null){
					hash.put("id", count+"_group");
					hash.put("type", "group");
					hash.put("open", true); 
					hash.put("nocheck", true); 
					hash.put("name", groupname);
					hash.put("children", this.getSubOverAllJson(userid,groupname,selectlist,orderlist));
					rows.add(hash);
			}
		} 
		JSONArray json = JSONArray.fromObject(rows);
		html.append(json); 
		return html.toString();
	} 
	
	private String getSubOverAllJson(String userid,String groupname,List<DashboardModel> selectlist,String orderlist){
		StringBuffer html = new StringBuffer();
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		for(DashboardModel model:selectlist){
			if(model.getGroupName()!=null&&model.getGroupName().equals(groupname)){
				HashMap hash = new HashMap();
				hash.put("id",model.getKey());
				//判断当前排序列表中是否包含了当前节点
				if(this.checkInList(model.getKey(), orderlist)){
					hash.put("chkDisabled", true); 
					hash.put("checked", true);  
				}
				hash.put("name",model.getTitle()); 
				hash.put("type","overall");
				hash.put("icon","iwork_img/shield.png");
				rows.add(hash);
			}
		}
		JSONArray json = JSONArray.fromObject(rows);
		html.append(json); 
		return html.toString();
	}
	/**
	 * 指定的key（键值）是否在orderlist列表中
	 * @param key
	 * @param orderlist
	 * @return
	 */
	private boolean checkInList(String key,String orderlist){
		boolean flag = false;
		try{
		JSONArray jsonlist = JSONArray.fromObject(orderlist);
		Iterator iterator = jsonlist.iterator();
		String[] column = {"column_1","column_2","column_3"};
		while(iterator.hasNext()){
			JSONObject jobj = (JSONObject)iterator.next();
			for(String id:column){
				JSONArray jsonArray = null;
				try{
				jsonArray = jobj.getJSONArray(id);
				}catch(Exception e){}
				if(jsonArray!=null){
					Iterator subite = jsonArray.iterator();
					while(subite.hasNext()){
						String str = (String)subite.next();
						if(str.equals(key)){
							flag = true;
							break;
						}
					}
				}
			}
		}}catch(Exception e){}
		return flag;
	}
}
