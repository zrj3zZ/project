package com.iwork.plugs.email.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.commons.util.AddressBookUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.email.dao.IWorkMailGroupDAO;
import com.iwork.plugs.email.model.IworkMailGroupItem;
import com.iwork.plugs.email.model.IworkMailGrouplist;

public class IWorkMailGroupService {
	private IWorkMailGroupDAO iWorkMailGroupDAO;


	/**
	 * 获得分组列表json
	 * @return
	 */
	public String getGroupListJson(){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> root = new ArrayList<Map<String,Object>>();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		 List<IworkMailGrouplist>  list = iWorkMailGroupDAO.getGroupList(userid);
		 if(list!=null && list.size()>0){
			 for(IworkMailGrouplist img:list){
				 Map<String,Object> item = new HashMap<String,Object>();
					item.put("id", img.getId());
					item.put("name", img.getGroupTitle());
					item.put("open", "false"); 
					item.put("nodeType", "diy");  
					item.put("iconOpen", "iwork_img/ztree/diy/1_open.png");
					item.put("iconClose", "iwork_img/ztree/diy/1_close.png");
					List<Map<String,Object>> subNode = new ArrayList<Map<String,Object>>();
					List<IworkMailGroupItem>  itemlist = iWorkMailGroupDAO.getGroupItemList(img.getId());
					if(itemlist!=null && itemlist.size()>0){
						for(IworkMailGroupItem imgi:itemlist){
							OrgUser model =  UserContextUtil.getInstance().getOrgUserInfo(imgi.getUserid());
							if(model!=null && !"".equals(model)){
								Map<String, Object> subitem = new HashMap<String, Object>();
								subitem.put("id", model.getUserid()); 
								subitem.put("name",model.getUsername());
								if(model.getIsmanager()==null||model.getIsmanager().equals("0")){
									subitem.put("icon", "iwork_img/user_business_boss.png");  
								}else{
									subitem.put("icon", "iwork_img/user.png"); 
								} 
								subitem.put("userName", model.getUsername());
								subitem.put("userId", model.getUserid()); 
								String useraddress = AddressBookUtil.generateUid(model.getUserid(),model.getUsername());
								subitem.put("useraddress", useraddress); 
								subitem.put("userno", model.getUserno());
								subitem.put("deptname", model.getDepartmentname()); 
								subitem.put("deptId", model.getDepartmentid());
								subitem.put("orgroleid",model.getOrgroleid());
								subNode.add(subitem);  
							}
						}
					}
					item.put("children", subNode);
					root.add(item);
			 }
		 }
		 JSONArray json = null;
		 json = JSONArray.fromObject(root);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public boolean groupRemove(Long id){
		boolean ispurview = true;
		boolean flag = false;
		String userid = UserContextUtil.getInstance().getCurrentUserId();
			IworkMailGrouplist model = iWorkMailGroupDAO.getGroupListModel(id);
			if(model!=null){
				if(model.getUserid().equals(userid)){
					ispurview = true;
				}
				if(ispurview){
					List<IworkMailGroupItem> list = iWorkMailGroupDAO.getGroupItemList(id);
					if(list.size()>0){
						ispurview = false;
					}
				}
				if(ispurview){
					iWorkMailGroupDAO.removeGroupListModel(model);
					flag = true;
				}
			}
		return flag;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public boolean groupSubRemove(Long id){
		boolean ispurview = false;
		boolean flag = false;
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		IworkMailGroupItem imgi = iWorkMailGroupDAO.getGroupItemModel(id);
		if(imgi!=null){
			IworkMailGrouplist model = iWorkMailGroupDAO.getGroupListModel(imgi.getPid());
			if(model!=null){
				if(model.getUserid().equals(userid)){
					ispurview = true;
				}
			}
			if(ispurview){
				iWorkMailGroupDAO.removeGroupItemModel(imgi);
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 获得分组列表
	 * @return
	 */
	public String getGroupList(){
		StringBuffer html = new StringBuffer();
		html.append("<ul id=\"itemlist\" class=\"tablist\">").append("\n");
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		List<IworkMailGrouplist>  list = iWorkMailGroupDAO.getGroupList(userid);
		if(list!=null&&list.size()>0){
			for(IworkMailGrouplist item:list){
				html.append("<li id=\"").append(item.getId()).append("\" ><img src=\"iwork_img/report_user.png\"/>").append(item.getGroupTitle()).append("</li>").append("\n");
			}
		}else{
			html.append("<li>空</li>").append("\n");
		}
		html.append("</ul>").append("\n");
		return html.toString();
	}
	/**
	 * 获得分组列表
	 * @return
	 */
	public String getGroupSubList(Long id){
		StringBuffer html = new StringBuffer();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		List<IworkMailGroupItem>  list = iWorkMailGroupDAO.getGroupItemList(id);
		if(list!=null&&list.size()>0){
			for(IworkMailGroupItem item:list){
				 UserContext uc =  UserContextUtil.getInstance().getUserContext(item.getUserid());
				html.append("<tr id=\"tr_").append(item.getId()).append("\">").append("\n");
				html.append("<td>").append(uc.get_userModel().getUsername()).append("</td>");
				html.append("<td>").append(uc.get_userModel().getUserid()).append("</td>");
				html.append("<td>").append(uc.get_userModel().getDepartmentname()).append("</td>");
				html.append("<td>").append(uc.get_userModel().getCompanyname()).append("</td>");
				html.append("<td>").append("<a href=\"javascript:remove(").append(item.getId()).append(")\">删除</a>").append("</td>");
				html.append("</tr>").append("\n");
			}
		}else{
			html.append("<tr>").append("\n");
			html.append("<td colspan=\"4\">未发现</td>");
			html.append("</tr>").append("\n");
		}
		
		html.append("<tr>").append("\n");
		html.append("<td colspan=\"4\" class=\"addBtn\"><a href=\"javascript:editGroup(").append(id).append(")\">添加组织成员至分组</a></td>");
		html.append("</tr>").append("\n");
		return html.toString();
	}
	/**
	 * 保存自定义分组列表
	 * @param title
	 * @param desc
	 * @param userlist
	 * @return
	 */
	public String saveGroupList(Long id,String title,String desc,String userlist){
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		IworkMailGrouplist group = new IworkMailGrouplist();
		group.setGroupTitle(title);
		group.setGroupDesc(desc);
		group.setUserid(userid);
		if(id==null){
			group = iWorkMailGroupDAO.saveGroupList(group);
		}else{
			group.setId(id);
			group = iWorkMailGroupDAO.updateGroupList(group);
		}
		if(group.getId()!=null){
			String[] users = userlist.split(",");
			List<String> list = new ArrayList();
			for(String user:users){
				String user_id = UserContextUtil.getInstance().getUserId(user);
				if(user_id!=null){
					if(list.contains(user_id)){
						continue;
					}
					IworkMailGroupItem imgi = new IworkMailGroupItem();
					imgi.setPid(group.getId());
					imgi.setUserid(user_id);
					iWorkMailGroupDAO.saveGroupItem(imgi);
					list.add(user_id);
				}
				
			}
		}
		
		return "";
	}
	
	
	public void setiWorkMailGroupDAO(IWorkMailGroupDAO iWorkMailGroupDAO) {
		this.iWorkMailGroupDAO = iWorkMailGroupDAO;
	}

	public IWorkMailGroupDAO getiWorkMailGroupDAO() {
		return iWorkMailGroupDAO;
	}
	
	
}
