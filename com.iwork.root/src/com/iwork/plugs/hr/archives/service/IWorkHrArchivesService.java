package com.iwork.plugs.hr.archives.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.hr.archives.model.StaffPageModel;
import com.iwork.plugs.hr.constants.IWorkHrConstants;
import com.iwork.plugs.hr.staff.IWorkHrStaffFactory;
import com.iwork.plugs.hr.staff.IWorkHrStaffInterface;
import com.iwork.plugs.hr.staff.model.ConfigModel;

public class IWorkHrArchivesService {
	
	public String showOverAllTreeJSON(){
	//获取指定用户的权限组列表
	StringBuffer html = new StringBuffer();
	List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
	String userid = UserContextUtil.getInstance().getCurrentUserId();
	List<ConfigModel> list = IWorkHrStaffFactory.get_staffSortList();
	int count = 0;
	SysPersonConfig spc = UserContextUtil.getInstance().getCurrentUserConfig(IWorkHrConstants.HR_STAFF_SELF_USERCONFIG);
	String[] setlist = null;
	if(spc!=null){
		String str = spc.getValue();
		if(str!=null){
			setlist = str.split(",");
		}
	}
	for(ConfigModel model:list){
		HashMap hash = new HashMap();
		count++;
		if(model!=null){
				hash.put("id", count+"_group");
				hash.put("key",model.getKey());
				hash.put("icon", "iwork_img/tab_add.png"); 
				hash.put("open", true); 
				if(model.getIsDefault()!=null&&model.getIsDefault().equals("true")){
					hash.put("name", model.getTitle()+"[默认]");
				}else{
					hash.put("name", model.getTitle());
				} 
				if(setlist!=null){
					boolean ischeck = false;
					for(String key:setlist){
						if(key.equals(model.getKey())){
							ischeck = true;
							break;
						}
					}
					if(ischeck){
						hash.put("checked", true);  
					}
				}else{
					if(model.getIsDefault()!=null&&model.getIsDefault().equals("true")){
						hash.put("chkDisabled", true); 
					}
					
				}
				rows.add(hash);
		}
	} 
	JSONArray json = JSONArray.fromObject(rows);
	html.append(json); 
	return html.toString();
} 
//private String getSubOverAllJson(String userid,String groupname,List<IworkPortalUserItem> selectlist){
//	StringBuffer html = new StringBuffer();
//	List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
//	List<IworkPortalOverall> list = iworkPortalDAO.getOverAllList(groupname);
//	for(IworkPortalOverall model:list){
//		HashMap hash = new HashMap();
//		hash.put("id",model.getId()); 
//		hash.put("name",model.getTitle()); 
//		
//		if(selectlist!=null){ 
//			for(IworkPortalUserItem ipui :selectlist){
//				if(ipui.getPortletId().equals(model.getId())){
//					hash.put("chkDisabled", true); 
//					hash.put("checked", true);  
//					break;
//				}
//			}
//		}
//		
//		hash.put("type","overall");
//		hash.put("icon","iwork_img/shield.png");
//		rows.add(hash);
//	}
//	JSONArray json = JSONArray.fromObject(rows);
//	html.append(json); 
//	return html.toString();
//}
	/**
	 * 获取菜单列表
	 * @param tabkey
	 * @return
	 */
	public StaffPageModel getStaffModel(String tabkey){
		StaffPageModel spm = new StaffPageModel();
		StringBuffer html = new StringBuffer();
		html.append("<ul class=\"tablist\">").append("\n");
		SysPersonConfig spc = UserContextUtil.getInstance().getCurrentUserConfig(IWorkHrConstants.HR_STAFF_SELF_USERCONFIG);
		String[] setlist = null;
		boolean isDefault = true;
		if(spc!=null){
			String str = spc.getValue();
			if(str!=null){
				setlist = str.split(",");
				isDefault = false;
			}
		} 
		List<ConfigModel> list = IWorkHrStaffFactory.get_staffSortList();
		for(ConfigModel model:list){
			if(tabkey==null){
				tabkey = model.getKey();
			}
			boolean isShow = false;
			if(!isDefault){
				for(String setkey:setlist){
					if(setkey.equals(model.getKey())){
						isShow = true;
						break;
					}
				}
			}else{
				if(model.getIsDefault()!=null&&model.getIsDefault().equals("true")){
					isShow = true;
				}
			}
			if(isShow){
				if(tabkey.equals(model.getKey())){
					html.append("<li class=\"current\" id=").append(model.getKey()).append(">").append(model.getTitle()).append("</li>").append("\n");
				}else{
					html.append("<li id=").append(model.getKey()).append(">").append(model.getTitle()).append("</li>").append("\n");
				}
			}
		}
		html.append("</ul>").append("\n");
		spm.setToolbar(html.toString());
		IWorkHrStaffInterface cm = IWorkHrStaffFactory.getStaffInstance(UserContextUtil.getInstance().getCurrentUserContext(),tabkey);
		spm.setBtnHtml(cm.getBtnHtml());
		spm.setContent(cm.getContent());
		spm.setScript(cm.getScript());
		return spm;
	}
}
