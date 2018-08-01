package com.iwork.core.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.iwork.commons.util.AddressBookUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.dao.OrgUserMapDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.km.document.dao.KMDirectoryDAO;
import com.iwork.km.document.dao.KMDocDAO;
import com.iwork.km.document.dao.KMPurViewDAO;
import com.iwork.km.document.model.IworkKmDirectory;
import com.iwork.km.document.model.IworkKmDoc;
import com.iwork.km.document.model.IworkKmPurview;
import com.iwork.km.document.model.IworkKmPurviewItem;
import com.iwork.km.document.service.KMFavService;

public class ZKMPurViewService {
	private FileUploadService uploadifyService;
	private KMDirectoryDAO kmDirectoryDAO;
	private KMDocDAO kmDocDAO;
	private KMFavService kmFavService;
	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgUserDAO orgUserDAO; 
	private OrgUserMapDAO orgUserMapDAO;
	private KMPurViewDAO kmPurViewDAO;
	
	/**
	 * 组织结构树初始化时，只显示公司
	 * @param purviewGroup
	 * @param purviewType 
	 * @param type 
	 * @param id
	 * @return
	 */
	public String getCompanyNodeJson(String purviewGroup,Long purviewType,Long type,Long id,String searchOrg){
		StringBuffer jsonHtml = new StringBuffer();	
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		JSONArray jsona=new JSONArray();
		//未传入部门ID,则显示全部信息
			List orgcompanyList = orgCompanyDAO.getAll();  
			for(int i=0;i<orgcompanyList.size();i++){
				OrgCompany model = (OrgCompany)orgcompanyList.get(i);	
				Map<String,Object> item = new HashMap<String,Object>();
				JSONObject jsonObj = new JSONObject();
				item.put("id", model.getId());
				item.put("name", model.getCompanyname());
				item.put("open", "true"); 
				item.put("nodeType", "com");  
				item.put("iconOpen", "iwork_img/ztree/diy/1_open.png");
				item.put("iconClose", "iwork_img/ztree/diy/1_close.png");	
				jsonObj.put("id", model.getId());
				jsonObj.put("name", model.getCompanyname());
				jsonObj.put("open", "true"); 
				jsonObj.put("nodeType", "com");  
				jsonObj.put("iconOpen", "iwork_img/ztree/diy/1_open.png");
				jsonObj.put("iconClose", "iwork_img/ztree/diy/1_close.png");	
				List departmentList = orgDepartmentDAO.getTopDepartmentList(model.getId());
				List<Map<String,Object>> childrenItems = new ArrayList<Map<String,Object>>();
				JSONArray jsonb=new JSONArray();
				for(int j=0;j<departmentList.size();j++){
					OrgDepartment dept = (OrgDepartment)departmentList.get(j);
					
					//装载部门列表 
					Map<String,Object> map = buildTreeDeptNode(dept,purviewGroup,purviewType,type,id,searchOrg);
					if(map!=null){
						childrenItems.add(map);
						jsonb.add(map);
					}
				}
				if(childrenItems!=null&&childrenItems.size()>0){ 
					JSONArray childrenJson = JSONArray.fromObject(childrenItems);
					item.put("children", childrenJson.toString()); 
					jsonObj.put("children", jsonb);
				}
				items.add(item);
				jsona.add(jsonObj);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsona.toString();
	} 
	
	/**
	 * 构建部门节点
	 * @param dept
	 * @param purviewGroup  授权分类：  文件夹授权|知识授权
	 * @param purviewType    按部门授权 0  按人员授权 1
	 * @param type   0：使用授权 1：管理授权
	 * @param pid
	 * @return
	 */
	private Map<String,Object> buildTreeDeptNode(OrgDepartment dept,String purviewGroup,Long purviewType,Long type,Long pid,String searchOrg){
		boolean  flag = false;
		if(purviewType.equals(new Long(1))){
			Map params = new HashMap();
			List lables = new ArrayList();
			int n=1;
			StringBuffer sql=new StringBuffer();
			sql.append(" select s.* from orguser s left join orgdepartment t on s.departmentid=t.id where  1=1 ");
			if(!"".equals(searchOrg) && searchOrg!=null){
				sql.append(" and  (s.username like ? or s.userid like ?) ");
				params.put(n, "%"+searchOrg+"%");n++;
				params.put(n, "%"+searchOrg+"%");n++;
			}
			if(dept.getId()!=0 && !dept.getId().equals(new Long(0))){
				sql.append(" and s.departmentid in (select z.id from orgdepartment z start with z.id= ?  connect by prior z.id=z.PARENTDEPARTMENTID) ");
				params.put(n, dept.getId());
			}else{
				return null;
			}
			
			List lists = DBUTilNew.getDataList(lables, sql.toString(), params);
			//List<OrgDepartment> deptlists = orgDepartmentDAO.getSubDepartmentList(dept.getId());
			if(lists!=null&&lists.size()>0){
				
			}else{
				return null;
			}
		}
		Map<String,Object> childrenItem = new HashMap<String,Object>();
		childrenItem.put("id", dept.getId());
		childrenItem.put("name", dept.getDepartmentname());
		childrenItem.put("open", "true");  
		//判断是否为末级部门
		List<OrgDepartment> deptlist = orgDepartmentDAO.getSubDepartmentList(dept.getId());
		if(purviewType.equals(new Long(0))){
			if(deptlist!=null&&deptlist.size()>0){
				childrenItem.put("isParent", true);
			}else{
				childrenItem.put("isParent", false);
			}
			//判断是否有读取权限
			flag  = isDeptPurview(dept,purviewGroup,purviewType,type,pid);  
			if(flag){
				childrenItem.put("checked", true);
			}
		}else{
			childrenItem.put("isParent", true); 
			childrenItem.put("nocheck", true);
		}
		childrenItem.put("nodeType", "dept");
		childrenItem.put("icon", "iwork_img/ztree/diy/40.png");
		childrenItem.put("companyid",dept.getCompanyid());
		childrenItem.put("departmentdesc", dept.getDepartmentdesc());
		childrenItem.put("departmentno",dept.getDepartmentno());
//		childrenItem.put("layer",dept.getLayer());
//		childrenItem.put("orderindex", dept.getOrderindex());			
//		childrenItem.put("zoneno",dept.getZoneno()); 
//		childrenItem.put("zonename",dept.getZonename());
//		childrenItem.put("extend1",dept.getExtend1());
//		childrenItem.put("extend2",dept.getExtend2());
//		childrenItem.put("extend3",dept.getExtend3());
//		childrenItem.put("extend4",dept.getExtend4());
//		childrenItem.put("extend5",dept.getExtend5()); 
		return childrenItem; 
	}
	
	/**
	 * 获得子部门及部门下用户
	 * @param deptId
	 * @return
	 */
	public String getDeptAndUserJson(Long deptId,String purviewGroup,Long purviewType,Long type,Long pid,String searchOrg){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String, Object>> list = new ArrayList();
		List<OrgDepartment> deptlist = new ArrayList();
		if(purviewType==1){
			deptlist = orgDepartmentDAO.getSubDepartmentLists(deptId,null);
		}else{
			deptlist = orgDepartmentDAO.getSubDepartmentLists(deptId,searchOrg);
		}
		//获得部门列表
		
		for(OrgDepartment dept:deptlist){
			Map<String,Object> childrenItem = buildTreeDeptNode(dept,purviewGroup,purviewType,type,pid,searchOrg); 
			if(childrenItem!=null){
				list.add(childrenItem);
			}
		}
		//判断授权类型：是否为按人员授权
		if(purviewType.equals(new Long(1))){ 
					List<OrgUser> currentUserList = new ArrayList();//当前部门用户列表（用于去除兼职列表中的重复用户）
					//获得用户列表
					List<OrgUser> userList = orgUserDAO.getActiveUserLists(deptId,searchOrg);
					for(OrgUser model:userList){
						if(model.getUsertype()!=null&&model.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示 
							Map<String, Object> item = buildTreeUserNode(model,purviewGroup,purviewType,type,pid);
							if(item!=null){
								list.add(item);
								currentUserList.add(model);
							}
						}
					}
					if("".equals(searchOrg) || searchOrg==null){
					//获得行政兼职用户列表
						List<OrgUserMap> userMapList = orgUserMapDAO.getOrgUserMap_DeptId(deptId.intValue(), OrgUserMap.USER_TYPE_ORG);
						for(OrgUserMap oum:userMapList){
							String userid = oum.getUserid();  //获取用户帐号
							if(userid==null)continue;
							//获取用户信息
							OrgUser model = orgUserDAO.getUserModel(userid);
							if(model!=null){
								Long userstatus = model.getUserstate();
								if(userstatus.equals(new Long(0))){//判断用户状态是否正常
									//判断是否已经添加过了
									if(currentUserList.contains(model)){ 
										continue;  //判断是否重复添加用户
									}else if(model.getUsertype()!=null&&model.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示
										Map<String, Object> item = new HashMap<String, Object>();
										item.put("id", model.getUserid()); 
										item.put("name",model.getUsername());
										if(oum.getIsmanager()==null||oum.getIsmanager().equals("0")){
											item.put("icon", "iwork_img/user_business_boss.png");  
										}else{
											item.put("icon", "iwork_img/user.png"); 
										} 
										item.put("nodeType", "user");
										item.put("userName", model.getUsername());
										item.put("userId", model.getUserid()); 
										String useraddress = AddressBookUtil.generateUid(model.getUserid(),model.getUsername());
										//判断权限
										//=============================================================================================
										//判断是否有读取权限
										boolean flag  = isUserPurview(model,purviewGroup,purviewType,type,pid);  
										if(flag){
											item.put("checked", true);
										}
										//=============================================================================================
												
										item.put("useraddress", useraddress); 
										item.put("userno", model.getUserno());
										item.put("deptname", oum.getDepartmentname()); 
										item.put("deptId", oum.getDepartmentid());
										item.put("orgroleid",oum.getOrgroleid());
										list.add(item);  
										currentUserList.add(model);  //装载到当前用户列表
									}else{ 
										continue;
									}
								}
							}
						}
					}
		}
		JSONArray json = JSONArray.fromObject(list); 
		jsonHtml.append(json); 
		return jsonHtml.toString();
	}
	
	/**
	 * 构建人员节点
	 * @param list
	 * @param orguser
	 * @return
	 */
	private Map<String, Object> buildTreeUserNode(OrgUser model,String purviewGroup,Long purviewType,Long type,Long pid){
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
		boolean flag  = isUserPurview(model,purviewGroup,purviewType,type,pid);  
		if(flag){
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
	/**
	 * @param purviewGroup  授权分类：  文件夹授权|知识授权
	 * @param purviewType    按部门授权 0  按人员授权 1
	 * @param ids  授权ID
	 * @param type   /管理授权 1 只读授权 0
	 * @return
	 */
	public void addPurview(String purviewGroup,Long purviewType,String ids,Long type,Long PId){
		String[] idlist = ids.split(",");
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		IworkKmPurview model =  kmPurViewDAO.getPurview(purviewGroup, purviewType, PId);
		if(model==null){
			model = new IworkKmPurview();
			model.setPurType(purviewType);
			model.setType(purviewGroup);
			model.setPUser(userid);
			model.setPTime(new Date());
			model.setPId(PId); 
			model = kmPurViewDAO.addPurview(model); 
		}else if(model!=null){
			model.setPurType(purviewType);
			model.setType(purviewGroup);
			model.setPUser(userid);
			model.setPTime(new Date());
			model.setPId(PId); 
			model = kmPurViewDAO.updatePurview(model);
		}
		if(model!=null){ 
			//清空已选列表
			kmPurViewDAO.deletePurviewItem(model.getId(),type);
			//添加新的权限列表
			for(String id:idlist){
				if("".equals(id))continue;
				IworkKmPurviewItem item = new IworkKmPurviewItem();
				item.setPurId(model.getId());
				item.setType(type);
				item.setPurview(id);
				kmPurViewDAO.addPurviewItem(item);
			}
		}
		
	}
	/**
	 * 判断指定授权id 是否进行过权限设置，如未进行权限设置，则默认对该文件夹有权限
	 * @param purviewGroup ‘folder’则表示在jqGrid中显示文件夹下内容，‘doc’则表示在jqGrid中只显示该文件      
	 * @param pid 授权ID
	 * @return
	 */
	public boolean isSetPurview(String purviewGroup,Long pid){
		return kmPurViewDAO.isSetPurview(pid,purviewGroup);
	}
	/**
	 * 判断指定部门是否有浏览权限
	 * @param dept
	 * @param purviewGroup   //‘folder’则表示在jqGrid中显示文件夹下内容，‘doc’则表示在jqGrid中只显示该文件      
	 * @param purviewType  //0 按部门授权 ,1 按人员授权
	 * @param pid
	 * @return  0:无权限  1：只读权限  2：管理权限
	 */
	public boolean isDeptPurview(OrgDepartment dept,String purviewGroup,Long purviewType,Long type,Long pid){
		boolean flag = false;
		IworkKmPurview model = kmPurViewDAO.getPurview(purviewGroup, purviewType, pid); 
		if(model==null){
			flag = false;
		}else{
			//获取指定权限列表
			 List<IworkKmPurviewItem> list = kmPurViewDAO.getPurviewItemList(model.getId(),type);
			 for(IworkKmPurviewItem ikpi:list){
				 if(ikpi.getPurview()==null)continue;
				 
				 if(ikpi.getPurview().equals(dept.getId().toString())){
					 flag = true;
					 break;
				 }
			 }
			
		}
		return flag; 
	}
	/**
	 * 判断指定部门是否有浏览权限
	 * @param dept
	 * @param purviewGroup   //‘folder’则表示在jqGrid中显示文件夹下内容，‘doc’则表示在jqGrid中只显示该文件      
	 * @param purviewType  //0 按部门授权 ,1 按人员授权
	 * @param pid
	 * @return  0:无权限  1：只读权限  2：管理权限
	 */
	public boolean isUserPurview(OrgUser orguser,String purviewGroup,Long purviewType,Long type,Long pid){
		boolean flag = false;
		IworkKmPurview model = kmPurViewDAO.getPurview(purviewGroup, purviewType, pid);
		if(model==null){
			flag = false;
		}else{
			//获取指定权限列表
			List<IworkKmPurviewItem> list = kmPurViewDAO.getPurviewItemList(model.getId(),type);
			for(IworkKmPurviewItem ikpi:list){
				if(ikpi.getPurview()==null)continue;
				if(ikpi.getPurview().equals(orguser.getUserid())){
					flag = true;
					break;
				}
			}
			
		}
		return flag; 
	}
	
	/**
	 * 获得已授权列表
	 * @param purviewGroup
	 * @param pid
	 * @return
	 */
	public String getPurviewList(String purviewGroup,Long pid){
		StringBuffer html = new StringBuffer();
		html.append("<table width=\"50%\" border=\"0\" style=\"border:1px solid #999;line-height:20px\"  cellspacing=\"0\" cellpadding=\"0\">").append("\n");
		 html.append("<tr style='background:#efefef'>").append("\n");
		 html.append("<th align=\"right\">").append("授权单元").append("</th>").append("\n");
		 html.append("<th>").append("授权类型").append("</th>").append("\n");
		 html.append("</tr>").append("\n");
		//获取按部门授权
		IworkKmPurview model = kmPurViewDAO.getPurview(purviewGroup, new Long(0), pid);
		if(model!=null){
			//获取指定权限列表
			 List<IworkKmPurviewItem> list = kmPurViewDAO.getPurviewItemList(model.getId());
			 for(IworkKmPurviewItem ikpi:list){
				 html.append("<tr >").append("\n");
				 Long type = ikpi.getType();
				 String deptid = ikpi.getPurview();
				 OrgDepartment dept = orgDepartmentDAO.getBoData(Long.parseLong(deptid));
				 if(dept!=null){
					 html.append("<td style=\"font-family:宋体;font-size:12px;text-align:right;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">").append(dept.getDepartmentname()).append("</td>").append("\n");
					 if(type.equals(new Long(0))){
						 html.append("<td style=\"font-family:宋体;font-size:12px;text-align:center;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">只读权限</td>").append("\n"); 
					 }else{
						 html.append("<td style=\"font-family:宋体;font-size:12px;text-align:center;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">管理权限</td>").append("\n");
					 }
				 }
				 html.append("</tr>").append("\n");
			 }
		}
		
		//获取按人员授权
		 model = kmPurViewDAO.getPurview(purviewGroup, new Long(1), pid);
		 if(model!=null){
			 List<IworkKmPurviewItem> list = kmPurViewDAO.getPurviewItemList(model.getId());
			 for(IworkKmPurviewItem ikpi:list){
				 html.append("<tr >").append("\n");
				 Long type = ikpi.getType();
				 String userId = ikpi.getPurview();
				 UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
				 if(uc!=null){
					 OrgUser orguser = uc.get_userModel();
					 String str = orguser.getUsername()+"["+orguser.getUserid()+"]";
					 html.append("<td style=\"font-family:宋体;font-size:12px;text-align:right;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">").append(str).append("</td>").append("\n");
					 if(type.equals(new Long(0))){
						 html.append("<td style=\"font-family:宋体;font-size:12px;text-align:center;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">只读权限</td>").append("\n"); 
					 }else{
						 html.append("<td style=\"font-family:宋体;font-size:12px;text-align:center;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">管理权限</td>").append("\n");
					 } 
				 }
				 html.append("</tr>").append("\n");
			 }
		 } 
		 html.append("</table>").append("\n");
		return html.toString();
	}
	/**
	 * 加载权限操作按钮
	 * @return
	 */
	public String loadPurviewBtn(Long id,String purviewGroup){
		StringBuffer html = new StringBuffer();
		OrgDepartment dept = UserContextUtil.getInstance().getCurrentUserContext()._deptModel;//获取当前部门ID
		OrgUser orguser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();//获取当前用户信息
		//==============================管理权限过滤=================================================
		boolean flag = false;
		String createUser = "ADMIN";
		if(purviewGroup.equals("doc")){
			IworkKmDoc model = kmDocDAO.getDocModel(id);
			if(model!=null){
				createUser = model.getCreateUser();
			}
		}else if(purviewGroup.equals("folder")){
			IworkKmDirectory ikd = kmDirectoryDAO.getDirectoryModel(id);
			if(ikd!=null){
				createUser = ikd.getCreateuser();
			}
		} 
		  //判断是否有管理权限
		if(orguser.getUserid().equals(createUser)||SecurityUtil.isSuperManager()){
			flag = true;
		}else{
			flag = isDeptPurview(dept, purviewGroup, new Long(0),  new Long(1),id);
			if(!flag){
				flag =  isUserPurview(orguser, purviewGroup, new Long(1),  new Long(1),id);  //判断是否管理权限 
			}
		}
		if(flag){
			html.append("<div class=\"tools_nav\">");
			html.append("<a href=\"#\"  plain=\"true\" class=\"easyui-linkbutton\" iconCls=\"icon-add\" onclick=\"javascript:addPurview(").append(id).append(",'").append(purviewGroup).append("',0);\" title=\"授权用户阅读文档\">浏览授权</a>\n");
			html.append("<a href=\"#\"  plain=\"true\" class=\"easyui-linkbutton\" iconCls=\"icon-add\" onclick=\"javascript:addPurview(").append(id).append(",'").append(purviewGroup).append("',1);\" title=\"授权用户管理维护\">管理授权</a>\n");
			html.append("</div>");
		}
		//==============================管理权限过滤=================================================
		return html.toString();
	}
//===========================================================//	
	public void setUploadifyService(FileUploadService uploadifyService) {
		this.uploadifyService = uploadifyService;
	}
	public void setKmDirectoryDAO(KMDirectoryDAO kmDirectoryDAO) {
		this.kmDirectoryDAO = kmDirectoryDAO;
	}
	public void setKmDocDAO(KMDocDAO kmDocDAO) {
		this.kmDocDAO = kmDocDAO;
	}
	public void setKmFavService(KMFavService kmFavService) {
		this.kmFavService = kmFavService;
	}
	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}
	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}
	public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
		this.orgUserDAO = orgUserDAO;
	}
	public void setOrgUserMapDAO(OrgUserMapDAO orgUserMapDAO) {
		this.orgUserMapDAO = orgUserMapDAO;
	}
	public void setKmPurViewDAO(KMPurViewDAO kmPurViewDAO) {
		this.kmPurViewDAO = kmPurViewDAO;
	}
	public KMPurViewDAO getKmPurViewDAO() {
		return kmPurViewDAO;
	}
	
	
}
