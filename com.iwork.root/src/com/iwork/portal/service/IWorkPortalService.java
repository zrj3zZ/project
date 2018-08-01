package com.iwork.portal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.commons.util.sort.ImprovedQuickSort;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SequenceUtil;
import com.iwork.portal.constans.PortalConstant;
import com.iwork.portal.dao.IWorkPortalDAO;
import com.iwork.portal.model.IworkPortalOverall;
import com.iwork.portal.model.IworkPortalUserItem;

public class IWorkPortalService {
	private IWorkPortalDAO iworkPortalDAO;
	private int count = 0;
	
	/**
	 * 获得个人门户列HTML
	 * @param index
	 * @return
	 */
	public String getColumnHTML(){
		StringBuffer html = new StringBuffer();
		StringBuffer column1 = new StringBuffer();
		StringBuffer column2 = new StringBuffer();
		StringBuffer column3 = new StringBuffer();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		 List<IworkPortalUserItem> list = iworkPortalDAO.getUserItemAllList(userid);
		 column1.append("<ul id=\"column_1\" class=\"column\">  ").append("\n");
		 column2.append("<ul id=\"column_2\" class=\"column\">  ").append("\n");
		 column3.append("<ul id=\"column_3\" class=\"column\">  ").append("\n");
		 boolean cloumn1flag = false;
		 boolean cloumn2flag = false;
		 boolean cloumn3flag = false;
		 
		 for(IworkPortalUserItem model:list){
			 IworkPortalOverall overallModel =  iworkPortalDAO.getOverAllModel(model.getPortletId());
			 if(overallModel==null){
				 iworkPortalDAO.removePortlet(model);
				 continue;}
			 Long height = model.getHeight(); 
			 String link = overallModel.getUrlLink();
			 String style = "";
			 if(height==null)height = new Long(300); 
			 //获得列号
			 Long groupIndex = model.getGroupindex();
			 if(groupIndex==null){
				 if(!cloumn1flag){
					 groupIndex = new Long(1); 
					 cloumn1flag =true;
				 }else if(!cloumn2flag){
					 groupIndex = new Long(2); 
					 cloumn2flag =true;
				 }else if(!cloumn3flag){
					 groupIndex = new Long(3); 
					 cloumn3flag =true;
				 }else if(cloumn1flag&&cloumn2flag&&cloumn3flag){
					  cloumn1flag = true;
					  cloumn2flag = false;
					  cloumn3flag = false;
					  groupIndex = new Long(1); 
				 } 
				 //系统自动分配列后，更新至用户portlet库
				 model.setGroupindex(groupIndex);
				 iworkPortalDAO.updatePortlet(model);
			 }
			 //获得栏目样式
			 if(model.getStyle()!=null){
				 style = model.getStyle();
			 }else{
				 style = "widget color-yellow";
			 }
			 if(groupIndex.equals(new Long(1))){
				 column1.append("<li class=\"widget ").append(style).append("\" id=\"widget_").append(model.getId()).append("\">  ").append("\n");
				 column1.append("                <div class=\"widget-head\">").append("\n");
				 column1.append("                    <h3>").append(model.getTitle()).append("</h3>").append("\n");
				 column1.append("                </div>").append("\n");
				 column1.append("                <div class=\"widget-content\"").append(" style=\"height:").append(height).append("px\"").append(" >").append("\n");
				 column1.append("                 <iframe frameborder=\"no\"  style=\"border:0px;\" src=\"").append(link).append("\" width=\"100%\" height=\"100%\" border=\"0\"></iframe>").append("\n");
				 column1.append("                </div>").append("\n");
				 column1.append("            </li>").append("\n");
			 }else if(groupIndex.equals(new Long(2))){
				
				 column2.append("<li class=\"widget ").append(style).append("\" id=\"widget_").append(model.getId()).append("\">  ").append("\n");
				 column2.append("                <div class=\"widget-head\">").append("\n");
				 column2.append("                    <h3>").append(model.getTitle()).append("</h3>").append("\n");
				 column2.append("                </div>").append("\n");
				 column2.append("                <div class=\"widget-content\"").append(" style=\"height:").append(height).append("px\"").append(" >").append("\n");
				 column2.append("                 <iframe frameborder=\"no\"  style=\"border:0px;\" src=\"").append(link).append("\" width=\"100%\" height=\"100%\" border=\"0\"></iframe>").append("\n");
				 column2.append("                </div>").append("\n");
				 column2.append("            </li>").append("\n"); 
				
			 }else if(groupIndex.equals(new Long(3))){ 
				 column3.append("<li class=\"widget ").append(style).append("\" id=\"widget_").append(model.getId()).append("\">  ").append("\n");
				 column3.append("                <div class=\"widget-head\">").append("\n");
				 column3.append("                    <h3>").append(model.getTitle()).append("</h3>").append("\n");
				 column3.append("                </div>").append("\n");
				 column3.append("                <div class=\"widget-content\"").append(" style=\"height:").append(height).append("px\"").append(" >").append("\n");
				 column3.append("                 <iframe frameborder=\"no\"  style=\"border:0px;\" src=\"").append(link).append("\" width=\"100%\" height=\"100%\" border=\"0\"></iframe>").append("\n");
				 column3.append("                </div>").append("\n");
				 column3.append("            </li>").append("\n"); 
			 }
		 }  
		 column1.append(" </ul>").append("\n"); 
		 column2.append(" </ul>").append("\n"); 
		 column3.append(" </ul>").append("\n"); 
		 html.append(column1).append(column2).append(column3);
		return html.toString();
	}
	/**
	 * 移动portlet
	 * @return
	 */
	public boolean movePortlet(Long groupId,Long portletid,String orderlist){
		boolean isSameGroup = false; //判断是否在同一个组别
		//加载移动的portlet
		IworkPortalUserItem model = iworkPortalDAO.getPortlet(portletid);
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		List<IworkPortalUserItem> groupList = iworkPortalDAO.getUserItemAllList(userid,groupId);
		String[] orderItemList = orderlist.split(",");
		
		//判断是否在同一个组别
		if(model.getGroupindex().equals(groupId)){
			//获取排序列表
			int[] idlist = new int[groupList.size()];
			for(int i=0;i<groupList.size();i++){
				IworkPortalUserItem temp =groupList.get(i);
				if(temp.getOrderIndex()==null||temp.getOrderIndex().equals(new Long(0))){
					temp.setOrderIndex(temp.getId());
				}
				idlist[i] = temp.getOrderIndex().intValue();
			} 
			ImprovedQuickSort.getInstance().sort(idlist); 
			for(int i=0;i<orderItemList.length;i++){ 
				IworkPortalUserItem item = iworkPortalDAO.getPortlet(Long.parseLong(orderItemList[i].trim()));
				item.setOrderIndex(new Long(idlist[i]));
				iworkPortalDAO.updatePortlet(item); 
			}
		}else{
			int[] idlist = new int[groupList.size()+1];
			for(int i=0;i<groupList.size();i++){
				IworkPortalUserItem temp =groupList.get(i);
				if(temp.getOrderIndex()==null||temp.getOrderIndex().equals(new Long(0))){
					temp.setOrderIndex(temp.getId());
				}
				idlist[i] = temp.getOrderIndex().intValue();
			} 
			if(model.getOrderIndex()==null||model.getOrderIndex().equals(new Long(0))){
				model.setOrderIndex(model.getId()); 
			} 
			idlist[groupList.size()] = model.getOrderIndex().intValue(); 
			ImprovedQuickSort.getInstance().sort(idlist); 
			for(int i=0;i<orderItemList.length;i++){ 
				IworkPortalUserItem item = iworkPortalDAO.getPortlet(Long.parseLong(orderItemList[i].trim()));
				item.setGroupindex(groupId); 
				item.setOrderIndex(new Long(idlist[i]));
				iworkPortalDAO.updatePortlet(item);  
			}
		}
		return true;
	}
	/**
	 * 移动portlet
	 * @return
	 */
	public boolean updatePortlet(Long portletid,String title,String style){
		boolean isSameGroup = false; //判断是否在同一个组别
		//加载移动的portlet
		IworkPortalUserItem model = iworkPortalDAO.getPortlet(portletid);
		if(!title.equals("")){
			model.setTitle(title);
		}
		if(!style.equals("")){
			model.setStyle(style);
		}
		iworkPortalDAO.updatePortlet(model);
		return true;
	}
	
	/**
	 * 移除portlet
	 * @return
	 */
	public boolean removePortlet(Long id){
		boolean flag = false;
		if(id!=null){
			IworkPortalUserItem model = iworkPortalDAO.getPortlet(id);
			if(model!=null){
				iworkPortalDAO.removePortlet(model);
				flag = true;
			}
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
			
			String[] idlist = ids.split(",");
			if(idlist!=null&&idlist.length>0){
				//移除之前的门户设置
				String userid = UserContextUtil.getInstance().getCurrentUserId();
				//iworkPortalDAO.removeUserPortletList(userid); 
				for(String id:idlist){
					if("".equals(id.trim()))continue;
					IworkPortalOverall ipoa = iworkPortalDAO.getOverAllModel(Long.parseLong(id));
					if(ipoa==null){continue;}
					IworkPortalUserItem model = new IworkPortalUserItem();
					model.setCreatedate(new Date()); 
					model.setHeight(ipoa.getHeight());
					model.setTitleIsshow(new Long(1));
					model.setTitle(ipoa.getTitle());
					model.setPortletId(ipoa.getId());
					model.setUserid(userid);
					int sequenceno = SequenceUtil.getInstance().getSequenceIndex("userDesk");
					model.setOrderIndex(new Long(sequenceno));
					iworkPortalDAO.savePortlet(model); 
				} 
				flag = true;
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
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		String[] list = PortalConstant.overallGroupList;
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		 List<IworkPortalUserItem> selectlist = iworkPortalDAO.getUserItemAllList(userid);
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
					hash.put("children", this.getSubOverAllJson(userid,groupname,selectlist));
					rows.add(hash);
			}
		} 
		JSONArray json = JSONArray.fromObject(rows);
		html.append(json); 
		return html.toString();
	} 
	private String getSubOverAllJson(String userid,String groupname,List<IworkPortalUserItem> selectlist){
		StringBuffer html = new StringBuffer();
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		List<IworkPortalOverall> list = iworkPortalDAO.getOverAllList(groupname);
		for(IworkPortalOverall model:list){
			HashMap hash = new HashMap();
			hash.put("id",model.getId()); 
			hash.put("name",model.getTitle()); 
			
			if(selectlist!=null){ 
				for(IworkPortalUserItem ipui :selectlist){
					if(ipui.getPortletId().equals(model.getId())){
						hash.put("chkDisabled", true); 
						hash.put("checked", true);  
						break;
					}
				}
			}
			
			hash.put("type","overall");
			hash.put("icon","iwork_img/shield.png");
			rows.add(hash);
		}
		JSONArray json = JSONArray.fromObject(rows);
		html.append(json); 
		return html.toString();
	}
	
	/**
	 * 获得分组列表
	 * @return
	 */
	public String getTabList(String groupname){
		StringBuffer html = new StringBuffer(); 
		html.append("			<dl class=\"demos-nav\"> ").append("\n");
		if(groupname==null){
			html.append("				<dd><a class=\"selected\" href=\"pt_overall_index.action\" target=\"_self\">全部</a></dd> ").append("\n");
		}else{
			html.append("				<dd><a  href=\"pt_overall_index.action\" target=\"_self\">全部</a></dd> ").append("\n");
		}
		for(String temp:PortalConstant.overallGroupList){
			if(groupname!=null&&temp.equals(groupname)){
				html.append("				<dd><a class=\"selected\"  href=\"pt_overall_index.action?groupname=").append(temp).append("\"  target=\"_self\">").append(temp).append("</a></dd> ").append("\n");
			}else{
				html.append("				<dd><a href=\"pt_overall_index.action?groupname=").append(temp).append("\"  target=\"_self\">").append(temp).append("</a></dd> ").append("\n");
			}
		} 
		html.append("			</dl> ").append("\n");
		return html.toString();
	}
	
	public boolean saveOverAllModel(IworkPortalOverall  model){
		boolean flag = false;
		if(model!=null){
			if(model.getId()==null){
				iworkPortalDAO.saveOverAll(model);
			}else{
				iworkPortalDAO.updateOverAll(model);
			}
		}
		return flag;
	}
	/**
	 * 移除门户组件
	 * @param id
	 */
	public boolean removeOverAllModel(Long  id){
		boolean flag = true;
		//判断是否允许删除此栏目
		if(flag){
			IworkPortalOverall model = iworkPortalDAO.getOverAllModel(id);
			iworkPortalDAO.removeOverAll(model);
		}
		return flag;
	}
	

	public IWorkPortalDAO getIworkPortalDAO() {
		return iworkPortalDAO;
	}

	public void setIworkPortalDAO(IWorkPortalDAO iworkPortalDAO) {
		this.iworkPortalDAO = iworkPortalDAO;
	}
	
	
	
	
	
	
	
	
	
	
}
