package com.iwork.core.organization.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.app.weixin.org.service.WeiXinOrgService;
import com.iwork.core.organization.constant.UserTypeConst;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.tools.UserContextUtil;

public class OrgDepartmentService {
	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgUserDAO orgUserDAO;
	private WeiXinOrgService winxinOrgService;
	
	/**
	 * 添加部门模型数据
	 * @param obj
	 */
	public void addBoData(OrgDepartment obj) {
		if(obj.getDepartmentstate()==null||obj.getDepartmentstate().equals("")){
			obj.setDepartmentstate("0");
		}
		orgDepartmentDAO.addBoData(obj);
		//添加部门
		
	}

	/**
	 * 删除模型数据
	 * @param model
	 * @return
	 */
	public String deleteBoData(OrgDepartment model) {
		int deptTemp = orgDepartmentDAO.getSubDepartmentSize(model.getId());
		int userTemp = orgUserDAO.getRows( model.getId());
		if(deptTemp != 0){
			return "hasDept";
		}else{
			if(userTemp != 0){
				return "hasUser";
			}else{
				orgDepartmentDAO.deleteBoData(model);
				return "deleteSuccess";
			}
		}
	}
	/**
	 * 获得部门信息
	 * @param id
	 * @return
	 */
	public OrgDepartment getBoData(Long id) {
		// TODO Auto-generated method stub
		return orgDepartmentDAO.getBoData(id);
	} 

	public List getBoDatas(int pageSize, int startRow) {
		// TODO Auto-generated method stub
		return orgDepartmentDAO.getBoDatas(pageSize, startRow);
	}

	/**
	 * 获得公司下部门列表
	 * @param companid
	 * @param parentdeptid
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List getBoDatas(int companid,int parentdeptid, int pageSize,
			int startRow) {
		// TODO Auto-generated method stub
		return orgDepartmentDAO.getBoDatas(companid,parentdeptid, pageSize, startRow);
	}

	public String getMaxID() {
		// TODO Auto-generated method stub
		return orgDepartmentDAO.getMaxID();
	}
	
	public String getMaxOrderIndex() {
		// TODO Auto-generated method stub
		return orgDepartmentDAO.getMaxOrderIndex();
	}

	public int getRows() {
		// TODO Auto-generated method stub
		return orgDepartmentDAO.getRows();
	}

	public int getRows(int companyid,Long parentdeptid) {
		// TODO Auto-generated method stub
		return orgDepartmentDAO.getRows(companyid,parentdeptid);
	}

	public List queryBoDatas(String fieldname, String value) {
		// TODO Auto-generated method stub
		return orgDepartmentDAO.queryBoDatas(fieldname, value);
	}  

	public void updateBoData(OrgDepartment obj) {
		orgDepartmentDAO.updateBoData(obj);

	}
	
	public String quickAdd(String companyid,Long parentdeptid,String departmentid,String departmentname){
		StringBuffer log = new StringBuffer();
		OrgDepartment model = orgDepartmentDAO.getModelForDepartmentNo(departmentid);
		if(model==null){
			 model = new OrgDepartment();
			model.setCompanyid(companyid);
			model.setDepartmentname(departmentname);
			model.setDepartmentno(departmentid);
			model.setParentdepartmentid(parentdeptid);
			model.setDepartmentstate("0");
			orgDepartmentDAO.addBoData(model);
			
		}else{
			log.append("部门已存在");
		}
		return log.toString();
	}
	/**
	 * 执行用户注销
	 */
	public String disable(Long id){
		String msg = "";
		UserContext context = UserContextUtil.getInstance().getCurrentUserContext();
		if(context.get_userModel().getUsertype().equals(UserTypeConst.USER_TYPE_SYSTEMUSER)){
			orgDepartmentDAO.execDisabled(id);
			msg = "success";
		}else{
			msg = "no purview";
		}
		return msg;
	}
	/**
	 * 执行用户激活
	 */
	public String doActive(Long id){
		String msg = "";
		UserContext context = UserContextUtil.getInstance().getCurrentUserContext();
		if(context.get_userModel().getUsertype().equals(UserTypeConst.USER_TYPE_SYSTEMUSER)){
			orgDepartmentDAO.execActive(id);
			msg = "success";
		}else{
			msg = "no purview";
		}
		return msg;
	}
	
	/**
	 * 获得导航菜单树脚本
	 * @return
	 */
	public String getNavTreeScript(){
		
		StringBuffer js = new StringBuffer();
		js.append("<script>").append("\n");
		js.append("foldersTree = gFld(\"部门管理维护\", \"department_list.action\",  \"diffFolder.gif\", \"diffFolder-0.gif\");").append("\n");
		List orgcompanyList = orgCompanyDAO.getAll();
		if(orgcompanyList!=null){
			for(int i=0;i<orgcompanyList.size();i++){
				OrgCompany oc = (OrgCompany)orgcompanyList.get(i);
				js.append(this.getCompanyScript(oc.getId(), oc.getCompanyname(), "department_list.action?companyId="+oc.getId()+"&parentdeptid=0&layer=0", "foldersTree","company_"));
				List departmentList = orgDepartmentDAO.getTopDepartmentList(oc.getId());
				//获得首级部门列表
				for(int j=0;j<departmentList.size();j++){
					OrgDepartment topod = (OrgDepartment)departmentList.get(j);
					List list = orgDepartmentDAO.getSubDepartmentList(topod.getId());
					String subsizestr = "";
					if(list.size()>0){   //如果子部门为0，则不做显示
						subsizestr = "["+list.size()+"]";
					}
					js.append(this.getFolderScript(topod.getId(), topod.getDepartmentname()+subsizestr, "department_list.action?companyId="+topod.getCompanyid()+"&layer=1&parentdeptid="+topod.getId(), "company_"+topod.getCompanyid(),"department_"));
					js.append(this.getSubDeptScript(list));
				}
			}
		}
		js.append("initializeDocument();").append("\n"); 
		js.append("</script>").append("\n");
		return js.toString();
	}
	
	/**
	 * 获取树视图
	 * @param companyId
	 * @param parentid
	 * @param type
	 * @return
	 */
	public String getTreeJson(String companyId,String parentid,String type,boolean isRoot){
		StringBuffer html = new StringBuffer();
		OrgCompany  orgCompany = orgCompanyDAO.getBoData(companyId);
		List<Map<String,Object>> root = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		if(type!=null&&type.equals("company")){
			List<OrgCompany> orgcompanyList = orgCompanyDAO.getCompanyList(companyId);
			if(orgcompanyList!=null){
				for(OrgCompany oc:orgcompanyList){
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("id",oc.getId()); 
					item.put("nodeType","company");
					item.put("name", oc.getCompanyname());
					//item.put("pageurl","company_list.action?parentid="+oc.getId());
					item.put("pageurl","department_list.action?companyId="+oc.getId()+"&parentdeptid=0");
					item.put("iconOpen", "iwork_img/organization.gif"); 
					item.put("iconClose", "iwork_img/organization.gif"); 
					item.put("icon", "iwork_img/organization.gif"); 
					item.put("open", false);
					item.put("async", false); 
					List<OrgCompany> subCompanyList =  orgCompanyDAO.getCompanyList(oc.getId());
					
					if((subCompanyList!=null&&subCompanyList.size()>0)){
						item.put("children", this.getTreeJson(oc.getId(),oc.getId(),type,false)); 
					}
					List departmentList = orgDepartmentDAO.getTopDepartmentList(oc.getId());
					if(departmentList!=null&&departmentList.size()>0){
						item.put("isParent", true); 
					} 
					item.put("target", "companyFrame");  
					item.put("companyId", oc.getId()); 
					rows.add(item);
				}
			}
		}
		
		//获取部门列表
		List departmentList = null;
		if(type!=null&&type.equals("company")){ 
			departmentList = orgDepartmentDAO.getTopDepartmentList(companyId);
		}else{
			departmentList = orgDepartmentDAO.getSubDepartmentList(Long.parseLong(parentid)); 
		} 
		if(departmentList != null){  
			for(int j=0;j<departmentList.size();j++){
				
				if(departmentList.get(j)==null)continue;
				OrgDepartment od = (OrgDepartment)departmentList.get(j);
				Map<String,Object> subItem = new HashMap<String,Object>();
				subItem.put("id", od.getId());
				int subDepartmentSize = orgDepartmentDAO.getSubDepartmentSize(od.getId());
				if(subDepartmentSize>0){
					subItem.put("isParent","true"); 
					subItem.put("name", od.getDepartmentname()+"["+subDepartmentSize+"]");
				}else{
					subItem.put("name", od.getDepartmentname());
				}
				subItem.put("fullname", od.getDepartmentname());
				subItem.put("async", true); 
				subItem.put("companyId",companyId);
				subItem.put("companyName",orgCompany.getCompanyname());
				subItem.put("nodeType","dept");
				subItem.put("iconOpen", "iwork_img/images/folder-open.gif");
				subItem.put("iconClose", "iwork_img/images/folder.gif");
				subItem.put("icon", "iwork_img/images/folder-open.gif");
				subItem.put("pageurl","department_list.action?companyId="+companyId+"&parentdeptid="+od.getId());
				subItem.put("target", "departmenFrame");
				rows.add(subItem); 
			}
		}
		
		JSONArray json = null;
		if(isRoot){
			Map<String,Object> item = new HashMap<String,Object>();
			if(orgCompany!=null){
				item.put("id",companyId);
				item.put("nodeType","company");
				item.put("name", orgCompany.getCompanyname());
				item.put("fullname", orgCompany.getCompanyname());
				item.put("companyId",companyId); 
				item.put("pageurl","department_list.action?companyId="+orgCompany.getId()+"&parentdeptid=0"); 
				item.put("iconOpen", "iwork_img/organization.gif"); 
				item.put("iconClose", "iwork_img/organization.gif"); 
				item.put("icon", "iwork_img/organization.gif"); 
				item.put("open", true);
				item.put("children", rows);
				root.add(item);
				json = JSONArray.fromObject(root); 
			}
		}else{
			json = JSONArray.fromObject(rows); 
		}
		html.append(json);
		return html.toString();
	}
	
	public String getTreeJson(String companyId,String parentid,String type,boolean isRoot,String searchOrg){
		StringBuffer html = new StringBuffer();
		OrgCompany  orgCompany = orgCompanyDAO.getBoData(companyId);
		List<Map<String,Object>> root = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		
		
		//获取部门列表
		List departmentList =departmentList = orgDepartmentDAO.getDept(searchOrg); 
		if(departmentList != null){  
			for(int j=0;j<departmentList.size();j++){
				
				if(departmentList.get(j)==null)continue;
				OrgDepartment od = (OrgDepartment)departmentList.get(j);
				Map<String,Object> subItem = new HashMap<String,Object>();
				subItem.put("id", od.getId());
				int subDepartmentSize = orgDepartmentDAO.getSubDepartmentSize(od.getId());
				if(subDepartmentSize>0){
					subItem.put("isParent","true"); 
					subItem.put("name", od.getDepartmentname()+"["+subDepartmentSize+"]");
				}else{
					subItem.put("name", od.getDepartmentname());
				}
				subItem.put("fullname", od.getDepartmentname());
				subItem.put("async", true); 
				subItem.put("companyId",companyId);
				subItem.put("companyName",orgCompany.getCompanyname());
				subItem.put("nodeType","dept");
				subItem.put("iconOpen", "iwork_img/images/folder-open.gif");
				subItem.put("iconClose", "iwork_img/images/folder.gif");
				subItem.put("icon", "iwork_img/images/folder-open.gif");
				subItem.put("pageurl","department_list.action?companyId="+companyId+"&parentdeptid="+od.getId());
				subItem.put("target", "departmenFrame");
				rows.add(subItem); 
			}
		}
		
		JSONArray json = null;
		if(isRoot){
			Map<String,Object> item = new HashMap<String,Object>();
			if(orgCompany!=null){
				item.put("id",companyId);
				item.put("nodeType","company");
				item.put("name", orgCompany.getCompanyname());
				item.put("fullname", orgCompany.getCompanyname());
				item.put("companyId",companyId); 
				item.put("pageurl","department_list.action?companyId="+orgCompany.getId()+"&parentdeptid=0"); 
				item.put("iconOpen", "iwork_img/organization.gif"); 
				item.put("iconClose", "iwork_img/organization.gif"); 
				item.put("icon", "iwork_img/organization.gif"); 
				item.put("open", true);
				item.put("children", rows);
				root.add(item);
				json = JSONArray.fromObject(root); 
			}
		}else{
			json = JSONArray.fromObject(rows); 
		}
		html.append(json);
		return html.toString();
	}
	/**
	 * 加载导航树的Json
	 * @return
	 */
	public String getNode(Long parentdeptid){
		StringBuffer html = new StringBuffer();
		if(parentdeptid.equals(new Long(0))){
			List orgcompanyList = orgCompanyDAO.getAll(); 
			List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
			if(orgcompanyList!=null){
				for(int i=0;i<orgcompanyList.size();i++){
					//加载公司节点
					if(orgcompanyList.get(i)==null)continue;
					OrgCompany oc = (OrgCompany)orgcompanyList.get(i);
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("id",oc.getId());
					item.put("nodeType","company");
					item.put("name", oc.getCompanyname());
					item.put("pageurl","department_list.action?companyId="+oc.getId()+"&parentdeptid=0");
					item.put("iconOpen", "iwork_img/organization.gif");
					item.put("iconClose", "iwork_img/organization.gif");
					item.put("open", true);
					item.put("target", "departmenFrame");
					item.put("companyId", "departmenFrame");
					//加载公司的首级部门节点 
					StringBuffer subHtml = new StringBuffer();
					List departmentList = orgDepartmentDAO.getTopDepartmentList(oc.getId());
					List<Map<String,Object>> subRows = new ArrayList<Map<String,Object>>();
					if(departmentList != null){
						for(int j=0;j<departmentList.size();j++){
							if(departmentList.get(j)==null)continue;
							OrgDepartment od = (OrgDepartment)departmentList.get(j);
							Map<String,Object> subItem = new HashMap<String,Object>();
							subItem.put("id", od.getId());
							int subDepartmentSize = orgDepartmentDAO.getSubDepartmentSize(od.getId());
							if(subDepartmentSize>0){
								subItem.put("isParent","true"); 
								subItem.put("name", od.getDepartmentname()+"["+subDepartmentSize+"]");
							}else{
								subItem.put("name", od.getDepartmentname());
							}
							subItem.put("companyId",oc.getId());
							subItem.put("nodeType","dept");
							subItem.put("iconOpen", "iwork_img/images/folder-open.gif");
							subItem.put("iconClose", "iwork_img/images/folder.gif");
							subItem.put("icon", "iwork_img/images/folder-open.gif");
							subItem.put("pageurl","department_list.action?companyId="+oc.getId()+"&parentdeptid="+od.getId());
							subItem.put("target", "departmenFrame");
							subRows.add(subItem);
						}
					}
					JSONArray subjson = JSONArray.fromObject(subRows);
					subHtml.append(subjson);
					item.put("children",subHtml.toString());
					rows.add(item);
				}
			}
			JSONArray json = JSONArray.fromObject(rows);
			html.append(json);
		}else{
			List subDeptList = orgDepartmentDAO.getSubDepartmentList(parentdeptid);
			List<Map<String,Object>> subRows = new ArrayList<Map<String,Object>>();
			if(subDeptList != null){
				for(int i=0;i<subDeptList.size();i++){
					if(subDeptList.get(i)==null)continue;
					OrgDepartment od = (OrgDepartment)subDeptList.get(i);
					Map<String,Object> subItem = new HashMap<String,Object>();
					subItem.put("id", od.getId());
					subItem.put("iconOpen", "iwork_img/images/folder-open.gif");
					subItem.put("icon", "iwork_img/images/folder-open.gif");
					subItem.put("iconClose", "iwork_img/images/folder.gif");
					subItem.put("companyId",od.getCompanyid());
					subItem.put("nodeType","dept");
					int subDepartmentSize = orgDepartmentDAO.getSubDepartmentSize(od.getId());
					if(subDepartmentSize>0){
						subItem.put("isParent","true"); 
						subItem.put("name", od.getDepartmentname()+"["+subDepartmentSize+"]");
					}else{
						subItem.put("name", od.getDepartmentname());
					}
					subItem.put("pageurl","department_list.action?companyId="+od.getCompanyid()+"&parentdeptid="+od.getId());
					subItem.put("target", "departmenFrame");
					subRows.add(subItem);
				}
			}
			JSONArray json = JSONArray.fromObject(subRows);
			html.append(json);
		}
 		return html.toString();
	}
	/**
	 * 获得子部门脚本,循环递归
	 * @return
	 */
//	private String getSubDeptScript(OrgDepartment deptmodel){
	private String getSubDeptScript(List list){
		StringBuffer js = new StringBuffer();
		int layer = 0;
		
//		if(deptmodel!=null){
			if(list==null)return "";
			for(int i=0;i<list.size();i++){
				OrgDepartment model = (OrgDepartment)list.get(i);
//				if(!"".equals(deptmodel.getLayer())&&deptmodel.getLayer()!=null){
//					layer = Integer.parseInt(model.getLayer());
					layer++;
					List nextlist = orgDepartmentDAO.getSubDepartmentList(model.getId());
					String subsizestr = "";
					if(nextlist.size()>0){   //如果子部门为0，则不做显示
						subsizestr = "["+nextlist.size()+"]";
					}
					String url = "department_list.action?companyId="+model.getCompanyid()+"&parentdeptid="+model.getId()+"&layer="+layer;
					js.append("department_").append(model.getId()).append(" = insFld(").append("department_").append(model.getParentdepartmentid()).append(", gFld (\"").append(model.getDepartmentname()+subsizestr).append("\", \"").append(url).append("\", \"orgDept.gif\", \"orgDept.gif\"));").append("\n");
//				}
				//循环调用
				js.append(this.getSubDeptScript(nextlist));
			}
//		}
		return js.toString();
	}
	/**
	 * 获得中间节点脚本
	 * @param id
	 * @param name
	 * @param url
	 * @param parentkey
	 * @return
	 */
	private String getCompanyScript(String id,String name,String url,String parentkey,String key){
		StringBuffer js = new StringBuffer();
		js.append(key).append(id).append(" = insFld(").append(parentkey).append(", gFld (\"").append(name).append("\", \"").append(url).append("\", \"mac_open.gif\", \"mac_closed.gif\"));").append("\n");
		return js.toString();
	}
	
	/**
	 * 获得中间节点脚本
	 * @param id
	 * @param name
	 * @param url
	 * @param parentkey
	 * @return
	 */
	private String getFolderScript(Long id,String name,String url,String parentkey,String key){
		StringBuffer js = new StringBuffer();
		js.append(key).append(id).append(" = insFld(").append(parentkey).append(", gFld (\"").append(name).append("\", \"").append(url).append("\", \"orgDept.gif\", \"orgDept.gif\"));").append("\n");
		
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
	
	
	/**
	 * 获得部门字典脚本
	 * @return
	 */
	public String getShowDeptDictionaryScript(String dictionarytype,String valueType){
		StringBuffer js = new StringBuffer();
		if(dictionarytype.equals("radio")){
			// 默认全路径
			js.append("<script language='JavaScript'>\n try{\nd = new dTree('d');\nd.add(0,-1,'部门树&nbsp;&nbsp;');\n");
			List orgcompanyList = orgCompanyDAO.getAll();
			StringBuffer sb = new StringBuffer();
			int treeId = 0, rootId = 0;
			//无上级部门选项
			//js.append("d.add(").append(treeId).append(",").append(rootId).append(",\"").append("[无上级部门]").append("\",\"\",\"\",\"mainFrame\");\n");
				for (int companyP = 0; companyP < orgcompanyList.size(); companyP++) {
					OrgCompany companyModel = (OrgCompany) orgcompanyList.get(new Integer(companyP));
					if (companyModel != null) {
						treeId++;
						js.append("d.add(").append(treeId).append(",").append(rootId).append(",\"").append(companyModel.getCompanyname()).append("\",\"\",\"\",\"mainFrame\");\n");
						// loop root department
						int departmentRootId = treeId;
						List rootDepartmentList = orgDepartmentDAO.getTopDepartmentList(companyModel.getId());
						for (int rootDepartmentP = 0; rootDepartmentP < rootDepartmentList.size(); rootDepartmentP++) {
							OrgDepartment rootDepartmentModel = (OrgDepartment) rootDepartmentList.get(new Integer(rootDepartmentP));
							treeId++;
							String selectValue = "";
							// ID全路径|名称全路径
//							if (valueType.toUpperCase().equals(DepartmentConst.DEPARTMENT_TREE_TYPE1)) {
//								selectValue = DepartmentCache.getFullID(rootDepartmentModel._id) + "|" + DepartmentCache.getFullName(rootDepartmentModel._id);
//							} else {
								selectValue = rootDepartmentModel.getId() + "|" + rootDepartmentModel.getDepartmentname();
//							}
							js.append("d.add(").append(treeId).append(",").append(departmentRootId).append(",\"").append("<input ondblclick=selectOne(); type=radio name=departmentId value=").append(selectValue).append(">").append(rootDepartmentModel.getDepartmentname()).append("[").append(rootDepartmentModel.getId()).append("]").append("\",\"\",\"\",\"mainFrame\");\n");
							// loop sub department
							treeId = putDepartmentTree(valueType, js, rootDepartmentModel, treeId);
						}
					}
				}
				js.append("}catch(e){}\ndocument.write(d);\n</script>");
		}
		return js.toString();
	}
	
	// 组装组织结构树,展开每个一级部门下的子部门
	private int putDepartmentTree(String valueType, StringBuffer js, OrgDepartment rootModel, int treeId) {
		int parentRootId = treeId;
		List departmentList = orgDepartmentDAO.getSubDepartmentList(rootModel.getId());
		for (int i = 0; i < departmentList.size(); i++) {
			treeId++;
			OrgDepartment departmentModel = (OrgDepartment) departmentList.get(new Integer(i));
			String selectValue = "";
			// ID全路径|名称全路径
//			if (valueType.toUpperCase().equals(MetaDataMapConst.MAP_DISPLAY_DEPRT_TREE2)) {
//				selectValue = departmentModel._departmentFullIdOfCache + "|" + departmentModel._departmentFullNameOfCache;
//			} else {
				selectValue = departmentModel.getId() + "|" + departmentModel.getDepartmentname();
//			}
			js.append("d.add(").append(treeId).append(",").append(parentRootId).append(",\"").append("<input ondblclick=selectOne(); type=radio name=departmentId value=").append(selectValue).append(">").append(departmentModel.getDepartmentname()).append("[").append(departmentModel.getId()).append("]").append("\",\"\",\"\",\"mainFrame\");\n");
			List tmp = orgDepartmentDAO.getSubDepartmentList(departmentModel.getId());
			if (tmp.size() > 0)
				treeId = putDepartmentTree(valueType, js, departmentModel, treeId);
		}
		return treeId;
	}
	
	/**
	 * 向上移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveUp(int companyid,int parentdeptid,int id){
		String type="up";
		orgDepartmentDAO.updateIndex(companyid,parentdeptid,id, type);
	}
	
	/**
	 * 向下移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveDown(int companyid,int parentdeptid,int id){
		String type="down";
		orgDepartmentDAO.updateIndex(companyid,parentdeptid,id, type);
	}
	
	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}

	public OrgDepartmentDAO getOrgDepartmentDAO() {
		return orgDepartmentDAO;
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

	public WeiXinOrgService getWinxinOrgService() {
		return winxinOrgService;
	}

	public void setWinxinOrgService(WeiXinOrgService winxinOrgService) {
		this.winxinOrgService = winxinOrgService;
	}
	
}
