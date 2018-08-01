package com.iwork.core.organization.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.app.log.operationlog.constant.LogInfoConstants;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.AddressBookUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgStationDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.dao.OrgUserMapDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgStation;
import com.iwork.core.organization.model.OrgStationIns;
import com.iwork.core.organization.model.OrgStationInsItem;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;

public class OrgStationService {
	private OrgStationDAO orgStationDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgCompanyDAO orgCompanyDAO;
	private OrgUserDAO orgUserDAO;
	private OrgUserMapDAO orgUserMapDAO;
	
	
	
	/**
	 * 删除岗位
	 * @param id
	 * @return
	 */
	public boolean deleteStation(Long id){
		boolean flag = false;
		if(SecurityUtil.getInstance().isSuperManager()){
			OrgStation os = orgStationDAO.getModel(id);
			if(os!=null){
				 List<OrgStationInsItem>  list = orgStationDAO.getItemList(id, null,null);
				 for(OrgStationInsItem osii:list){
					 orgStationDAO.deleteStationeItem(osii);
				 }
				 //删除实例
				 List<OrgStationIns> inslist = orgStationDAO.getStationInsList(id);
				 for(OrgStationIns osi:inslist){
					 orgStationDAO.delStationIns(osi);
				 }
				 orgStationDAO.delStation(os);
				 flag = true;
				//添加审计日志
					StringBuffer log = new StringBuffer();
					log.append("岗位名称【").append(os.getStationName()).append("】,岗位ID【").append(os.getId()).append("】");
					LogUtil.getInstance().addLog(os.getId(), LogInfoConstants.ORG_STATION_DEL, log.toString());
			}
		}
		return flag;
	}
	/**
	 * 删除岗位
	 * @param id
	 * @return
	 */
	public boolean deleteStationIns(String ids){
		StringBuffer log = new StringBuffer();
		
		boolean flag = false;
		if(SecurityUtil.getInstance().isSuperManager()){
			String[] idlist = ids.split(",");
			for(String tmp:idlist){
				//删除实例
				if(tmp.trim().equals("")){
					continue;
				} 
				Long id = Long.parseLong(tmp);
				OrgStationIns osi = orgStationDAO.getStationInsModel(id);
				if(osi!=null){
					List<OrgStationInsItem>  list = orgStationDAO.getItemList(osi.getStationId(),id, null);
					for(OrgStationInsItem osii:list){
						orgStationDAO.deleteStationeItem(osii);
					}
					orgStationDAO.delStationIns(osi);
					log.append("【").append(osi.getTitle()).append(",").append(osi.getId()).append("】");
				}
			}
			//添加审计日志
			LogUtil.getInstance().addLog(null, LogInfoConstants.ORG_STATION_INS_DEL, log.toString());
			flag = true;
		}
		return flag;
	}
	/**
	 * 保存范围设置
	 * @param stationId
	 * @param insId
	 * @param companylist
	 * @param deptlist
	 * @return
	 */
	public String stationScopeItemSave(Long stationId,Long insId,String companylist,String deptlist){
		
		String msg = "";

		 List<OrgStationInsItem> clist = orgStationDAO.getItemList(null,insId,"com");
		 List<OrgStationInsItem> dlist = orgStationDAO.getItemList(null,insId,"dept");
		 //比对company
		 if(companylist!=null&&!companylist.equals("")){
			StringBuffer log = new StringBuffer();
			log.append("【组织】[岗位ID:").append(stationId).append("][岗位实例ID:").append(insId).append("]");
			 String[] cl = companylist.split(",");
			 for(String id:cl){
				 if(id.trim().equals(""))continue;
				 boolean isadd = true;
				 if(clist==null){
					 isadd = true;
				 }else{
					 for(OrgStationInsItem item:clist){
						 if(item.getVal().equals(id.toString())){
							 isadd = false;
							 break;
						 }
					 }
				 }
				 if(isadd){
					 if(id.trim().equals("")){continue;}
					 OrgStationInsItem model = new OrgStationInsItem();
					 model.setOrgtype("com");
					 model.setStationId(stationId);
					 model.setStationInsId(insId);
					 model.setVal(id);
					 orgStationDAO.saveStationeItem(model); 
					 log.append("[").append(model.getVal()).append("]");
				 }
			 }
				LogUtil.getInstance().addLog(insId, LogInfoConstants.ORG_STATION_INS_ITEM_ADD, log.toString());
		 }
		 //
		 if(clist!=null&&clist.size()>0){
			 StringBuffer log = new StringBuffer();
			log.append("【组织】[岗位ID:").append(stationId).append("][岗位实例ID:").append(insId).append("]");
			 for(OrgStationInsItem osii:clist){
				 boolean isdel = false;
				 if(companylist==null){
					 isdel = true;
				 }else{
					 String[] cl = companylist.split(",");
					 for(String id:cl){
						 if(id.equals(osii.getVal())){
							 isdel = false;
							 break;
						 }
					 }
				 }
				 if(isdel){
					 orgStationDAO.deleteStationeItem(osii);
					 log.append("[").append(osii.getVal()).append("]");
				 }
			 }
			 LogUtil.getInstance().addLog(insId, LogInfoConstants.ORG_STATION_INS_ITEM_DEL, log.toString());
		 }
		 
		 if(deptlist!=null&&!deptlist.equals("")){
			 StringBuffer log = new StringBuffer();
			log.append("【部门】[岗位ID:").append(stationId).append("][岗位实例ID:").append(insId).append("]");
			 String[] cl = deptlist.split(",");
			 for(String id:cl){
				 if(id.trim().equals(""))continue;
				 boolean isadd = true;
				 if(dlist==null){
					 isadd = true;
				 }else{
					 for(OrgStationInsItem item:dlist){
						 if(item.getVal().equals(id.toString())){
							 isadd = false;
							 break;
						 }
					 }
				 }
				 if(isadd){
					 OrgStationInsItem model = new OrgStationInsItem();
					 model.setOrgtype("dept");
					 model.setStationId(stationId);
					 model.setStationInsId(insId);
					 model.setVal(id);
					 orgStationDAO.saveStationeItem(model); 
					 log.append("[").append(model.getVal()).append("]");
					 msg = "success";
				 }
			 }
			 LogUtil.getInstance().addLog(insId, LogInfoConstants.ORG_STATION_INS_ITEM_ADD, log.toString());
		 }
		 //
		 if(dlist!=null&&dlist.size()>0){
			 StringBuffer log = new StringBuffer();
				log.append("【部门】[岗位ID:").append(stationId).append("][岗位实例ID:").append(insId).append("]");
			 for(OrgStationInsItem osii:dlist){
				 boolean isdel = true;
				 if(deptlist==null){
					 isdel = true;
				 }else{
					 String[] cl = deptlist.split(",");
					 for(String id:cl){
						 if(id.equals(osii.getVal())){
							 isdel = false;
							 break;
						 }
					 }
				 }
				 if(isdel){
					 orgStationDAO.deleteStationeItem(osii);
					 log.append("[").append(osii.getVal()).append("]");
				 }
			 }
			 LogUtil.getInstance().addLog(insId, LogInfoConstants.ORG_STATION_INS_ITEM_DEL, log.toString());
		 }
		 msg = "success";
		return msg;
	}
	/**
	 * 保存范围设置
	 * @param stationId
	 * @param insId
	 * @param companylist
	 * @param deptlist
	 * @return
	 */
	public String stationScopeItemSave(Long stationId,Long insId,String userlist){
		StringBuffer msg = new StringBuffer();
		List<OrgStationInsItem> dlist = orgStationDAO.getItemList(null,insId,"user");
		if(userlist!=null&&!userlist.equals("")){
			 StringBuffer log = new StringBuffer();
				log.append("【用户】[岗位ID:").append(stationId).append("][岗位实例ID:").append(insId).append("]");
			String[] cl = userlist.split(",");
			for(String id:cl){
				if(id.trim().equals(""))continue;
				boolean isadd = true;
				if(dlist==null&&dlist.size()==0){
					isadd = true;
				}else{
					for(OrgStationInsItem item:dlist){
						if(item.getVal().equals(id.toString())){
							isadd = false;
							break;
						}
					}
				}
				if(isadd){
					OrgStationInsItem model = new OrgStationInsItem();
					model.setOrgtype("user");
					model.setStationId(stationId);
					model.setStationInsId(insId);
					model.setVal(id);
					List datalist = orgStationDAO.getItemList(stationId, null, "user", id);
					if(datalist==null||datalist.size()==0){
						orgStationDAO.saveStationeItem(model); 
						log.append("[").append(id).append("]");
					}else{
						UserContext uc = UserContextUtil.getInstance().getUserContext(id);
						msg.append(uc.get_userModel().getUsername());
					}
					
				}
			}
			 LogUtil.getInstance().addLog(insId, LogInfoConstants.ORG_STATION_INS_ITEM_ADD, log.toString());
		}
		//
		if(dlist!=null&&dlist.size()>0){
			StringBuffer log = new StringBuffer();
			log.append("【用户】[岗位ID:").append(stationId).append("][岗位实例ID:").append(insId).append("]");
			for(OrgStationInsItem osii:dlist){
				boolean isdel = true;
				if(userlist==null||"".equals(userlist)){
					isdel = true;
				}else{
					String[] cl = userlist.split(",");
					for(String id:cl){
						if(id.equals(osii.getVal())){
							isdel = false;
							break;
						}
					}
				}
				if(isdel){
					orgStationDAO.deleteStationeItem(osii);
					log.append("[").append(osii.getVal()).append("]");
				}
			}
			 LogUtil.getInstance().addLog(insId, LogInfoConstants.ORG_STATION_INS_ITEM_DEL, log.toString());
		}
		if(msg.toString()==""){
			msg.append("success");
		}
		return msg.toString();
	}
	
	/**
	 * 获得子部门及部门下用户
	 * @param deptId
	 * @return
	 */
	public String getDeptsAndUserJson(List<Long> deptIds){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> list = new ArrayList();
		if(deptIds!=null&&deptIds.size()>0){
			if(deptIds.size()==1){
				//获得部门列表
				List<OrgDepartment> deptlist = orgDepartmentDAO.getSubDepartmentList(deptIds.get(0));
				for(OrgDepartment dept:deptlist){
					List<OrgDepartment> templist = orgDepartmentDAO.getSubDepartmentList(dept.getId());
					boolean isLeaf = true;
					if(templist!=null&&templist.size()>0){
						isLeaf = false;
					}
					Map<String,Object> childrenItem = buildTreeDeptNode(null,dept,isLeaf); 
					if(childrenItem!=null){
						list.add(childrenItem);
					}
				}
			}else{
				for(Long deptId:deptIds){
					OrgDepartment dept = orgDepartmentDAO.getBoData(deptId);
					boolean isLeaf = true;
					Map<String,Object> childrenItem = buildTreeDeptNode(null,dept,isLeaf); 
					if(childrenItem!=null){
						list.add(childrenItem);
					}
				}
			}
		}
		JSONArray json = JSONArray.fromObject(list); 
		jsonHtml.append(json); 
		return jsonHtml.toString();
	}
	
	/**
	 * 获得子部门及部门下用户
	 * @param deptId
	 * @return
	 */
	public String getDeptJson(Long deptId,Long insId){
		 List<OrgStationInsItem> dlist = orgStationDAO.getItemList(null,insId,"dept");
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> list = new ArrayList();
		//获得部门列表
		List<OrgDepartment> deptlist = orgDepartmentDAO.getSubDepartmentList(deptId);
		for(OrgDepartment dept:deptlist){
			List<OrgDepartment> templist = orgDepartmentDAO.getSubDepartmentList(dept.getId());
			boolean isLeaf = true;
			if(templist!=null&&templist.size()>0){
				isLeaf = false;
			}
			Map<String,Object> childrenItem = buildTreeDeptNode(dlist,dept,isLeaf); 
			if(childrenItem!=null){
				list.add(childrenItem);
			}
		}
		JSONArray json = JSONArray.fromObject(list); 
		jsonHtml.append(json); 
		return jsonHtml.toString();
	}
	
	/**
	 * 构建部门节点
	 * @param list
	 * @param dept
	 * @return
	 */
	private Map<String,Object> buildTreeDeptNode(List<OrgStationInsItem> list,OrgDepartment dept,boolean isLeaf){
		boolean isCheck = false;
		if(list!=null&&list.size()>0){
			for(OrgStationInsItem osii:list){
				if(osii.getVal().equals(dept.getId().toString())){
					isCheck = true;
					break;
				}
			}
		}
		Map<String,Object> childrenItem = new HashMap<String,Object>();
		childrenItem.put("id", dept.getId());
		childrenItem.put("name", dept.getDepartmentname());
		childrenItem.put("open", "true"); 
		childrenItem.put("nodeType", "dept");
		if(isLeaf){
			childrenItem.put("icon", "iwork_img/model.gif");
			childrenItem.put("isParent", false);
		}else{
			childrenItem.put("isParent", true);
		}
		if(isCheck){
			childrenItem.put("checked",true);
		}
		childrenItem.put("nodeId", dept.getId());
		childrenItem.put("companyid",dept.getCompanyid());
		childrenItem.put("departmentdesc", dept.getDepartmentdesc());
		childrenItem.put("departmentno",dept.getDepartmentno());
		childrenItem.put("layer",dept.getLayer());
		childrenItem.put("orderindex", dept.getOrderindex());			
		childrenItem.put("zoneno",dept.getZoneno()); 
		childrenItem.put("zonename",dept.getZonename());
		childrenItem.put("extend1",dept.getExtend1());
		childrenItem.put("extend2",dept.getExtend2());
		childrenItem.put("extend3",dept.getExtend3());
		childrenItem.put("extend4",dept.getExtend4());
		childrenItem.put("extend5",dept.getExtend5()); 
		return childrenItem; 
	}
	/**
	 * 获得岗位列表
	 * @return
	 */
	public String getStationList(){
//		orgStationDAO.
		StringBuffer html = new StringBuffer();
		List<OrgStation> list = orgStationDAO.getAllList();
		html.append("<ul class=\"tablist\">");
		for(OrgStation os:list){
			html.append("<li id=\"").append(os.getId()).append("\">").append(os.getStationName()).append("<span class=\"delbtn\" onclick=\"deleteItem(").append(os.getId()).append(")\"><img src=\"iwork_img/close.gif\"/></span>").append("</li>");
		}
		html.append("</ul>");
		return html.toString();
	}
	
	
	/**
	 * 组织结构树初始化时，只显示公司
	 * @return
	 */
	public String getCompanyNodeJson(String id, Long insId,String nodeType, boolean flag){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> root = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		List<OrgCompany> orgCompanyList = new ArrayList<OrgCompany>();
		 List<OrgStationInsItem> clist = orgStationDAO.getItemList(null,insId,"com");
		 List<OrgStationInsItem> dlist = orgStationDAO.getItemList(null,insId,"dept");
		// 构建当前登录所属公司的子公司
		if(nodeType!=null && nodeType.equals("com")){
			orgCompanyList = orgCompanyDAO.getCompanyList(id);
			if(orgCompanyList!=null && orgCompanyList.size()>0){
				for(OrgCompany model : orgCompanyList){
					Map<String,Object> item = new HashMap<String,Object>();
					item = this.buildTreeCompanyNode(model);
					
					List<OrgCompany> subCompanyList =  orgCompanyDAO.getCompanyList(model.getId());
					
					if((subCompanyList!=null&&subCompanyList.size()>0)){
						item.put("children", this.getCompanyNodeJson(model.getId(),insId,nodeType, false)); 
					}
					List departmentList = orgDepartmentDAO.getTopDepartmentList(model.getId());
					if(departmentList!=null&&departmentList.size()>0){
						item.put("isParent", true); 
					}
					items.add(item);
				}
			}
		}
		
		// 构建部门
		List departmentList = null;
		if(nodeType != null && nodeType.equals("com")){ 
			departmentList = orgDepartmentDAO.getTopDepartmentList(id);
		}else{
			departmentList = orgDepartmentDAO.getSubDepartmentList(Long.parseLong(id)); 
		} 
		if(departmentList != null){
			for(int j=0;j<departmentList.size();j++){
				Map<String,Object> subItem = new HashMap<String,Object>();
				OrgDepartment dept = (OrgDepartment)departmentList.get(j);
				
				List<OrgDepartment> templist = orgDepartmentDAO.getSubDepartmentList(dept.getId());
				boolean isLeaf = true;
				if(templist!=null&&templist.size()>0){
					isLeaf = false;
				}
				//装载部门列表 
				subItem = buildTreeDeptNode(dlist,dept,isLeaf);
				items.add(subItem);
			}
		}
		
		// 构建根节点
		JSONArray json = null;
		if(flag){
			Map<String,Object> item = new HashMap<String,Object>();
			OrgCompany  orgCompany = orgCompanyDAO.getBoData(id);
			if(orgCompany!=null){
				item = this.buildTreeCompanyNode(orgCompany);
				item.put("children", items);
				root.add(item);
				json = JSONArray.fromObject(root);
			}
		}else{
			json = JSONArray.fromObject(items);
		}
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 组织结构树初始化时，只显示公司
	 * @return
	 */
	public String getTopUserTreeNodeJson(String id, Long insId,String nodeType, boolean flag){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> root = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		List<OrgCompany> orgCompanyList = new ArrayList<OrgCompany>();
		// 构建当前登录所属公司的子公司
		if(nodeType!=null && nodeType.equals("com")){
			orgCompanyList = orgCompanyDAO.getCompanyList(id);
			if(orgCompanyList!=null && orgCompanyList.size()>0){
				for(OrgCompany model : orgCompanyList){
					Map<String,Object> item = new HashMap<String,Object>();
					item = this.buildTreeCompanyNode(model);
					
					List<OrgCompany> subCompanyList =  orgCompanyDAO.getCompanyList(model.getId());
					
					if((subCompanyList!=null&&subCompanyList.size()>0)){
						item.put("children", this.getCompanyNodeJson(model.getId(),insId,nodeType, false)); 
					}
					List departmentList = orgDepartmentDAO.getTopDepartmentList(model.getId());
					if(departmentList!=null&&departmentList.size()>0){
						item.put("isParent", true); 
					}
					items.add(item);
				}
			}
		}
		
		// 构建部门
		List departmentList = null;
		if(nodeType != null && nodeType.equals("com")){ 
			departmentList = orgDepartmentDAO.getTopDepartmentList(id);
		}else{
			departmentList = orgDepartmentDAO.getSubDepartmentList(Long.parseLong(id)); 
		} 
		if(departmentList != null){
			for(int j=0;j<departmentList.size();j++){
				OrgDepartment dept = (OrgDepartment)departmentList.get(j);
				
				List<OrgDepartment> templist = orgDepartmentDAO.getSubDepartmentList(dept.getId());
				
				boolean isLeaf = true;
				if(templist!=null&&templist.size()>0){
					isLeaf = false;
				}
				//装载部门列表 
				Map<String,Object> childrenItem = new HashMap();
				if(isLeaf){
					childrenItem.put("icon", "iwork_img/model.gif");
					childrenItem.put("isParent", false);
				}else{
					childrenItem.put("isParent", true);
				}
			
				childrenItem.put("id", dept.getId());
				childrenItem.put("name", dept.getDepartmentname());
				childrenItem.put("open", true); 
				childrenItem.put("nocheck", true);
				items.add(childrenItem);
			}
		}
		
		// 构建根节点
		JSONArray json = null;
		if(flag){
			Map<String,Object> item = new HashMap<String,Object>();
			OrgCompany  orgCompany = orgCompanyDAO.getBoData(id);
			if(orgCompany!=null){
				item = this.buildTreeCompanyNode(orgCompany);
				item.put("children", items);
				root.add(item);
				json = JSONArray.fromObject(root);
			}
		}else{
			json = JSONArray.fromObject(items);
		}
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	/**
	 * 
	 * Description:构建公司节点
	 * Params:参数描述
	 * Return:Map<String,Object>
	 *
	 * @author:zouyalei
	 * @date  :2015-7-24下午02:59:17
	 */
	public Map<String,Object> buildTreeCompanyNode(OrgCompany model){
		Map<String,Object> item = new HashMap<String,Object>();
		item.put("id", model.getId());
		item.put("name", model.getCompanyname());
		item.put("open", "true"); 
		item.put("nodeType", "com");  
		item.put("iconOpen", "iwork_img/ztree/diy/1_open.png");
		item.put("iconClose", "iwork_img/ztree/diy/1_close.png");
		item.put("companyid",model.getId());
		item.put("companytype",model.getCompanytype());
		item.put("companyno", model.getCompanyno());
		item.put("companydesc",model.getCompanydesc());
		item.put("orderindex", model.getOrderindex());
		item.put("lookandfeel",model.getLookandfeel());
		item.put("orgadress",model.getOrgadress());
		item.put("post",model.getPost());
		item.put("email",model.getEmail());
		item.put("tel",model.getTel());
		item.put("zoneno",model.getZoneno());
		item.put("zonename",model.getZonename());
		item.put("extend1",model.getExtend1());
		item.put("extend2",model.getExtend2());
		item.put("extend3",model.getExtend3());
		item.put("extend4",model.getExtend4());
		item.put("extend5",model.getExtend5()); 
		
		return item;
	}
	/**
	 * 构建人员节点
	 * @param list
	 * @param orguser
	 * @return
	 */
	private Map<String, Object> buildTreeUserNode(OrgUser model,List<OrgStationInsItem> list){
		boolean isCheck = false;
		if(list!=null&&list.size()>0){
			for(OrgStationInsItem osii:list){
				if(osii.getVal().equals(model.getUserid())){
					isCheck = true;
					break;
				}
			}
		}
		
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("id", model.getUserid()); 
		item.put("name",model.getUsername());
		if(model.getIsmanager().equals(new Long(1))){
			item.put("icon", "iwork_img/user_business_boss.png");  
		}else if(model.getUsertype()!=null&&model.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示
			item.put("icon", "iwork_img/user.png"); 
			//item.put("icon", "iwork_img/user_silhouette.png"); 
		}
		String useraddress = AddressBookUtil.generateUid(model.getUserid(),model.getUsername());
		//判断权限
		//=============================================================================================
		if(isCheck){
			item.put("checked", true);
		}
		//=============================================================================================
		item.put("nodeType", "user");
		item.put("userName", model.getUsername());
		item.put("userId", model.getUserid()); 
		item.put("useraddress",useraddress); 
		item.put("userno", model.getUserno());
		item.put("deptname", model.getDepartmentname()); 
		item.put("deptId", model.getDepartmentid());
		item.put("orgroleid",model.getOrgroleid());
		return item;
	}
	
	public String getSelectContent( Long insId,String type){
		StringBuffer content = new StringBuffer();
		List<OrgStationInsItem> dlist = orgStationDAO.getItemList(null,insId,type);
		for(OrgStationInsItem osii:dlist){
			String orgtype = osii.getOrgtype();
			if(orgtype.equals("user")){
				String userid = osii.getVal();
				UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
				content.append(uc.get_userModel().getUsername()).append("[").append(uc.get_deptModel().getDepartmentname()).append("]").append("&nbsp;");
			}else if(orgtype.equals("dept")){
				String deptid = osii.getVal();
				OrgDepartment od = orgDepartmentDAO.getBoData(Long.parseLong(deptid));
				if(od!=null){
					content.append(od.getDepartmentname()).append("[").append(od.getId()).append("]").append("&nbsp;");
				}
			}
			
		}
		return content.toString();
	}
	public OrgStationDAO getOrgStationDAO() {
		return orgStationDAO;
	}
	public void setOrgStationDAO(OrgStationDAO orgStationDAO) {
		this.orgStationDAO = orgStationDAO;
	}

	public OrgDepartmentDAO getOrgDepartmentDAO() {
		return orgDepartmentDAO;
	}

	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}

	public OrgCompanyDAO getOrgCompanyDAO() {
		return orgCompanyDAO;
	}

	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}
	public OrgUserDAO getOrgUserDAO() {
		return orgUserDAO;
	}
	public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
		this.orgUserDAO = orgUserDAO;
	}
	public OrgUserMapDAO getOrgUserMapDAO() {
		return orgUserMapDAO;
	}
	public void setOrgUserMapDAO(OrgUserMapDAO orgUserMapDAO) {
		this.orgUserMapDAO = orgUserMapDAO;
	}
	
	
}
