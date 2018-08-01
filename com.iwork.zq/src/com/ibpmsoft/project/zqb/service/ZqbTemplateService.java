package com.ibpmsoft.project.zqb.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import net.sf.json.JSONArray;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.plugs.weboffice.tools.WebOfficeTools;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.opensymphony.xwork2.ActionContext;

/**
 * 模板管理Service
 * 
 * @author
 * 
 */
public class ZqbTemplateService {
	// 模板分类UUID
	public static final String TEMPLATE_TYPE_UUID = "49c49af53bd24c1ca2bd8dd98f46c81d";
	// 模板UUDI
	public static final String TEMPLATE_UUID = "8fb1e9aba3734bb5b8f400a3dc8770ae";
	private OrgCompanyDAO orgCompanyDAO;

	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}

	/**
	 * 构建模板类别树
	 * 
	 * @param parentid
	 * @return
	 */
	public String getTemplateTreeJson(String parentid, String nodeType) {
		StringBuffer json = new StringBuffer();
		List<HashMap> root = new ArrayList<HashMap>();
		List<HashMap> subRoot = new ArrayList<HashMap>();
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		List<HashMap> subList = new ArrayList<HashMap>();
		HashMap<String, Object> rootNode = new HashMap<String, Object>();
		List<HashMap> typeList = null;

		if (parentid == null) {
			// 统一模板列表
			rootNode = new HashMap<String, Object>();
			conditionMap.put("FID", "999999");
			rootNode.put("id", "999999");
			rootNode.put("name", "信息披露");
			rootNode.put("instanceid", 999999);
			rootNode.put("open", true);
			rootNode.put("nodeType", "root");
			rootNode.put("isParent", true);
			rootNode.put("iconOpen", "iwork_img/package_add.png");
			rootNode.put("iconClose", "iwork_img/package_delete.png");
			rootNode.put("icon", "iwork_img/package.png");

			// 从模板分类列表获取记录
			typeList = DemAPI.getInstance().getList(TEMPLATE_TYPE_UUID,
					conditionMap, "ID");
			for (HashMap<String, Object> hash : typeList) {
				HashMap<String, Object> subNode = new HashMap<String, Object>();
				conditionMap.clear();
				conditionMap.put("FID", hash.get("ID").toString());
				subList.clear();
				subList = DemAPI.getInstance().getAllList(TEMPLATE_TYPE_UUID,
						conditionMap, "ID");
				subNode.put("id", hash.get("ID").toString());
				subNode.put("name", hash.get("LBMC").toString());
				subNode.put("instanceid", hash.get("INSTANCEID").toString());
				subNode.put("open", true);
				subNode.put("nodeType", "common");
				if (subList != null && subList.size() > 0) {
					subNode.put("isParent", true);
				} else {
					subNode.put("isParent", false);
				}
				subNode.put("iconOpen", "iwork_img/package_add.png");
				subNode.put("iconClose", "iwork_img/package_delete.png");
				subNode.put("icon", "iwork_img/package.png");

				subRoot.add(subNode);
			}

			rootNode.put("children", subRoot);
			root.add(rootNode);

		} else {
			subRoot = new ArrayList<HashMap>();
			conditionMap = new HashMap<String, Object>();
			subList = new ArrayList<HashMap>();
			if (parentid != null) {
				conditionMap.put("FID", parentid);
			}

			// 从模板分类列表获取记录
			typeList = DemAPI.getInstance().getList(TEMPLATE_TYPE_UUID,
					conditionMap, "ID");
			for (HashMap<String, Object> hash : typeList) {
				HashMap<String, Object> subNode = new HashMap<String, Object>();
				conditionMap.clear();
				conditionMap.put("FID", hash.get("ID").toString());
				subList.clear();
				subList = DemAPI.getInstance().getAllList(TEMPLATE_TYPE_UUID,
						conditionMap, "ID");
				subNode.put("id", hash.get("ID").toString());
				subNode.put("name", hash.get("LBMC").toString());
				subNode.put("instanceid", hash.get("INSTANCEID").toString());
				subNode.put("open", true);
				subNode.put("nodeType", "common");
				if (subList != null && subList.size() > 0) {
					subNode.put("isParent", true);
				} else {
					subNode.put("isParent", false);
				}
				subNode.put("iconOpen", "iwork_img/package_add.png");
				subNode.put("iconClose", "iwork_img/package_delete.png");
				subNode.put("icon", "iwork_img/package.png");

				subRoot.add(subNode);
			}

		}
		if (parentid == null) {
			JSONArray jsonArray = JSONArray.fromObject(root);
			json.append(jsonArray);
		} else {
			JSONArray jsonArray = JSONArray.fromObject(subRoot);
			json.append(jsonArray);
		}

		return json.toString();
	}

	/**
	 * 判断是否有子节点
	 * 
	 * @param parentid
	 * @param companyno
	 * @return
	 */
	public boolean ifHasChildren(String parentid, String companyno) {
		boolean flag = false;
		// List<OrgCompany> orgcompanyList =
		// orgCompanyDAO.getCompanyList(parentid);
		//		 
		// // 从模板分类列表获取记录
		// HashMap<String,Object> conditionMap = new HashMap<String,Object>();
		// if(parentid!=null){
		// conditionMap.put("FID", parentid);
		// }
		// List<HashMap> typeList =
		// DemAPI.getInstance().getList(TEMPLATE_TYPE_UUID, conditionMap, "ID");
		// if((orgcompanyList!=null&&orgcompanyList.size()>0)||(typeList!=null&&typeList.size()>0)){
		// flag = true;
		// }
		return flag;
	}

	/**
	 * 获取当前公司下的子模板分类
	 * 
	 * @param parentid
	 * @return
	 */
	public List<HashMap> getSubTemplateJson(String parentid) {
		List<HashMap> subList = new ArrayList<HashMap>();
		List<HashMap> rows = new ArrayList<HashMap>();

		// 从模板分类列表获取记录
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		if (parentid != null) {
			conditionMap.put("FID", parentid);
		}
		List<HashMap> typeList = DemAPI.getInstance().getList(
				TEMPLATE_TYPE_UUID, conditionMap, "ID");
		for (HashMap<String, Object> hash : typeList) {
			HashMap<String, Object> subNode = new HashMap<String, Object>();
			conditionMap.clear();
			conditionMap.put("FID", hash.get("ID").toString());
			subList.clear();
			subList = DemAPI.getInstance().getAllList(TEMPLATE_TYPE_UUID,
					conditionMap, "ID");
			subNode.put("id", hash.get("ID").toString());
			subNode.put("name", hash.get("LBMC").toString());
			subNode.put("companyno", hash.get("COMPANYNO").toString());
			subNode.put("companyname", hash.get("COMPANYNAME").toString());
			subNode.put("instanceid", hash.get("INSTANCEID").toString());
			subNode.put("open", true);
			if (subList != null && subList.size() > 0) {
				subNode.put("isParent", true);
			} else {
				subNode.put("isParent", false);
			}
			subNode.put("iconOpen", "iwork_img/package_add.png");
			subNode.put("iconClose", "iwork_img/package_delete.png");
			subNode.put("icon", "iwork_img/package.png");

			rows.add(subNode);
		}

		return rows;
	}

	/**
	 * 获取当前公司的子模板分类、子公司
	 * 
	 * @param parentid
	 * @param companyno
	 * @return
	 */
	public List<HashMap> getTreeJson(String parentid, String companyno) {
		// List<OrgCompany> orgcompanyList =
		// orgCompanyDAO.getCompanyList(parentid);
		// List<HashMap> rows = new ArrayList();
		List<HashMap> templateList = getSubTemplateJson(companyno);
		// if(orgcompanyList != null)
		// {
		// HashMap<String,Object> item;
		// Iterator<OrgCompany> iterator = orgcompanyList.iterator();
		// while(iterator.hasNext())
		// {
		// OrgCompany oc = (OrgCompany)iterator.next();
		// item = new HashMap<String,Object>();
		// item.put("id", oc.getCompanyno());
		// item.put("nodeType", "company");
		// item.put("name", oc.getCompanyname());
		// item.put("companyno", oc.getCompanyno());
		// item.put("companyname", oc.getCompanyname());
		// item.put("companytype", oc.getCompanytype());
		// item.put("parentid", oc.getParentid());
		// //item.put("pageurl", (new
		// StringBuilder("company_list.action?parentid=")).append(oc.getId()).toString());
		// item.put("iconOpen", "iwork_img/organization.gif");
		// item.put("iconClose", "iwork_img/organization.gif");
		// item.put("icon", "iwork_img/organization.gif");
		// item.put("open", true);
		// List subCompanyList = orgCompanyDAO.getCompanyList(oc.getId());
		// List subTemplateList = getSubTemplateJson(oc.getCompanyno());
		// if(subCompanyList != null && subCompanyList.size() > 0 ||
		// subTemplateList != null && subTemplateList.size() > 0){
		// item.put("children", getTreeJson(oc.getId(),oc.getCompanyno()));
		// }
		// item.put("target", "companyFrame");
		// item.put("companyId", "companyFrame");
		//	                
		// templateList.add(item);
		// }
		//
		// }
		return templateList;
	}

	/**
	 * 删除模板类别
	 * 
	 * @param instanceid
	 * @return
	 */
	public boolean deleteTemplateType(Long instanceid, String id) {
		boolean flag = false;
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("FID", id);
		List<HashMap> typeList = DemAPI.getInstance().getList(TEMPLATE_TYPE_UUID, conditionMap, null);
		// 判断类别下是否存在子分类，如果不存在则判断是否有模板
		if (typeList == null || typeList.size() == 0) {
			conditionMap.clear();
			conditionMap.put("TYPE", id);
			List<HashMap> temList = DemAPI.getInstance().getList(TEMPLATE_UUID,
					conditionMap, null);
			// 判断类别下是否有模板
			if (temList == null || temList.size() == 0) {
				HashMap hash = DemAPI.getInstance().getFromData(instanceid);
				flag = DemAPI.getInstance().removeFormData(instanceid);
				if(flag){
					Long dataId=Long.parseLong(hash.get("ID").toString());
					String value=hash.get("TEMPLATENAME")==null?"":hash.get("TEMPLATENAME").toString();
					LogUtil.getInstance().addLog(dataId, "模版管理", "删除模版："+value);
				}
			}
		}
		return flag;
	}

	/**
	 * 根据模板类型查询模板
	 * 
	 * @param id
	 * @return
	 */
	public List<HashMap> queryTemplatesByType(String id,String templatename) {
		List<HashMap> temList = new ArrayList<HashMap>();
		List<HashMap> returnList = new ArrayList<HashMap>();
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		if(id!=null&&!id.equals(""))
			conditionMap.put("TYPE", id);
		if(templatename!=null&&!templatename.equals(""))
			conditionMap.put("TEMPLATENAME", templatename);
		temList = DemAPI.getInstance().getList(TEMPLATE_UUID, conditionMap,"CREATEDATE DESC,ID DESC");
        for(HashMap map:temList){
        	String userid=map.get("CREATEUSER")!=null?map.get("CREATEUSER").toString():"";
        	String instanceid=map.get("INSTANCEID")!=null?map.get("INSTANCEID").toString():"0";
        	UserContext uc=UserContextUtil.getInstance().getUserContext(userid);
        	String username="";
			if(uc!=null){
				username = uc._userModel.getUsername();
			}
        	map.put("CREATEUSER", username);
        	map.put("INSTANCEID", instanceid);
            returnList.add(map);
        }
		return temList;
	}
	
	public List<HashMap> queryTemplatesByType() {
		List<HashMap> temList = new ArrayList<HashMap>();
		List<HashMap> returnList = new ArrayList<HashMap>();
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		temList = DemAPI.getInstance().getList(TEMPLATE_UUID, conditionMap,"CREATEDATE DESC,ID DESC");
		for(HashMap map:temList){
			String userid=map.get("CREATEUSER")!=null?map.get("CREATEUSER").toString():"";
			String instanceid=map.get("INSTANCEID")!=null?map.get("INSTANCEID").toString():"0";
			UserContext uc=UserContextUtil.getInstance().getUserContext(userid);
			String username="";
			if(uc!=null){
				username = uc._userModel.getUsername();
			}
			map.put("CREATEUSER", username);
			map.put("INSTANCEID", instanceid);
			returnList.add(map);
		}
		return temList;
	}
	
	/**
	 * 根据templatename名查寻--templatePage.jsp
	 * @param templatename
	 * @return
	 */
	public List<HashMap> queryTemplatesByTname(String templatename){
		List<HashMap> temList = new ArrayList<HashMap>();
		List<HashMap> returnList = new ArrayList<HashMap>();
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("TEMPLATENAME", templatename);
		temList = DemAPI.getInstance().getList(TEMPLATE_UUID, conditionMap,"ID");
        for(HashMap map:temList){
        	String userid=map.get("CREATEUSER")!=null?map.get("CREATEUSER").toString():"";
        	String instanceid=map.get("INSTANCEID")!=null?map.get("INSTANCEID").toString():"0";
        	UserContext uc=UserContextUtil.getInstance().getUserContext(userid);
        	String username="";
			if(uc!=null){
				username = uc._userModel.getUsername();
			}
        	map.put("CREATEUSER", username);
        	map.put("INSTANCEID", instanceid);
            returnList.add(map);
        }
        /*if(templatename.equals("")){
        	temList.removeAll(temList);
        }*/
		return temList;
	}

	/**
	 * 删除模板
	 * 
	 * @param instanceid
	 * @return
	 */
	public boolean deleteTemplate(Long instanceid, String id) {
		boolean flag = false;
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		flag = DemAPI.getInstance().removeFormData(instanceid);
		if(flag){
			String value=fromData.get("TEMPLATENAME")==null?"":fromData.get("TEMPLATENAME").toString();
			Long dataId=Long.parseLong(fromData.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "模版管理", "删除模版："+value);
		}
		return flag;
	}

	/**
	 * 构建模板类别树
	 * 
	 * @param parentid
	 * @return
	 */
	public String getTemplateTreeAllJson(String parentid, String nodeType) {
		StringBuffer json = new StringBuffer();
		List<HashMap> root = new ArrayList<HashMap>();
		List<HashMap> subRoot = new ArrayList<HashMap>();
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		List<HashMap> subList = new ArrayList<HashMap>();
		HashMap<String, Object> rootNode = new HashMap<String, Object>();
		List<HashMap> typeList = null;
		List<HashMap> tempList = new ArrayList<HashMap>();

		if (parentid == null) {
			// 集团统一模板列表
			rootNode = new HashMap<String, Object>();
			conditionMap.put("FID", "999999");
			rootNode.put("id", "999999");
			rootNode.put("name", "信息披露");
			rootNode.put("instanceid", 999999);
			rootNode.put("open", true);
			rootNode.put("nodeType", "root");
			rootNode.put("isParent", true);
			rootNode.put("iconOpen", "iwork_img/package_add.png");
			rootNode.put("iconClose", "iwork_img/package_delete.png");
			rootNode.put("icon", "iwork_img/package.png");

			// 从模板分类列表获取记录
			typeList = DemAPI.getInstance().getList(TEMPLATE_TYPE_UUID,
					conditionMap, "ID");
			for (HashMap<String, Object> hash : typeList) {
				HashMap<String, Object> subNode = new HashMap<String, Object>();
				conditionMap.clear();
				conditionMap.put("FID", hash.get("ID").toString());
				subList.clear();
				subList = DemAPI.getInstance().getAllList(TEMPLATE_TYPE_UUID,
						conditionMap, "ID");
				// 查询模板
				conditionMap.clear();
				conditionMap.put("TYPE", hash.get("ID").toString());
				tempList = DemAPI.getInstance().getAllList(TEMPLATE_UUID,
						conditionMap, "ID");

				subNode.put("id", hash.get("ID").toString());
				subNode.put("name", hash.get("LBMC").toString());

				subNode.put("instanceid", hash.get("INSTANCEID").toString());
				subNode.put("open", true);
				subNode.put("nodeType", "common");
				if ((subList != null && subList.size() > 0)
						|| (tempList != null && tempList.size() > 0)) {
					subNode.put("isParent", true);
				} else {
					subNode.put("isParent", false);
				}
				subNode.put("iconOpen", "iwork_img/package_add.png");
				subNode.put("iconClose", "iwork_img/package_delete.png");
				subNode.put("icon", "iwork_img/package.png");

				subRoot.add(subNode);
			}

			// 查询模板
			conditionMap.clear();
			/*conditionMap.put("TYPE", "999999");*/
			tempList = DemAPI.getInstance().getAllList(TEMPLATE_UUID,
					conditionMap, "ID");
			if (tempList != null && tempList.size() > 0) {
				List<HashMap> tempRoot = new ArrayList<HashMap>();
				for (HashMap<String, Object> tempMap : tempList) {
					HashMap<String, Object> tempNode = new HashMap<String, Object>();
					tempNode.put("id", tempMap.get("ID").toString());
					tempNode
							.put("name", tempMap.get("TEMPLATENAME").toString());
					tempNode.put("instanceid", tempMap.get("INSTANCEID")
							.toString());
					tempNode.put("open", false);
					tempNode.put("isParent", false);
					tempNode.put("type", "file");
					String uuid = "";
					if (tempMap.get("CONTENT") != null) {
						uuid = tempMap.get("CONTENT").toString();
					}
					tempNode.put("uuid", uuid);
					tempNode.put("iconOpen", "iwork_img/package_add.png");
					tempNode.put("iconClose", "iwork_img/package_delete.png");
					tempNode.put("icon", "iwork_img/file/word.jpg");

					subRoot.add(tempNode);
				}
			}

			rootNode.put("children", subRoot);
			root.add(rootNode);

}else{
			

			subRoot = new ArrayList<HashMap>();
			conditionMap = new HashMap<String,Object>();
			subList = new ArrayList<HashMap>();
			if(parentid!=null){
				conditionMap.put("FID", parentid);
			}
			
			// 从模板分类列表获取记录
			typeList = DemAPI.getInstance().getList(TEMPLATE_TYPE_UUID, conditionMap, "ID");
			for(HashMap<String,Object> hash:typeList){
				HashMap<String,Object> subNode = new HashMap<String,Object>();
				conditionMap.clear();
				conditionMap.put("FID", hash.get("ID").toString());
				subList.clear();
				subList = DemAPI.getInstance().getAllList(TEMPLATE_TYPE_UUID, conditionMap, "ID");
				subNode.put("id", hash.get("ID").toString());
				subNode.put("name", hash.get("LBMC").toString());
				subNode.put("instanceid", hash.get("INSTANCEID").toString());
				subNode.put("open", true);
				subNode.put("nodeType", "common");
				
				// 查询模板
				conditionMap.clear();
				conditionMap.put("TYPE", hash.get("ID").toString());
				tempList = DemAPI.getInstance().getAllList(TEMPLATE_UUID, conditionMap, "ID");
				
				if((subList!=null && subList.size()>0)||(tempList!=null && tempList.size()>0)){
					subNode.put("isParent", true);
				}else{
					subNode.put("isParent", false);
				}
				subNode.put("iconOpen", "iwork_img/package_add.png");
				subNode.put("iconClose", "iwork_img/package_delete.png");
				subNode.put("icon", "iwork_img/package.png");
				
				subRoot.add(subNode);
			}
			
			

			conditionMap.clear();
			if(parentid!=null){
				conditionMap.put("TYPE", parentid);
			}
			
			tempList = DemAPI.getInstance().getAllList(TEMPLATE_UUID, conditionMap, "ID");
			List<HashMap> tempRoot = new ArrayList<HashMap>();
			for(HashMap<String,Object> tempMap:tempList){
				HashMap<String,Object> tempNode = new HashMap<String,Object>();
				tempNode.put("id", tempMap.get("ID").toString());
				tempNode.put("name", tempMap.get("TEMPLATENAME").toString());
				tempNode.put("instanceid", tempMap.get("INSTANCEID").toString());
				tempNode.put("open", false);
				tempNode.put("isParent", false);
				tempNode.put("type", "file");
				tempNode.put("uuid", tempMap.get("CONTENT").toString());
				tempNode.put("iconOpen", "iwork_img/package_add.png");
				tempNode.put("iconClose", "iwork_img/package_delete.png");
				tempNode.put("icon", "iwork_img/file/word.jpg");
				
				subRoot.add(tempNode);
			}
		
		}

		if (parentid == null) {
			JSONArray jsonArray = JSONArray.fromObject(root);
			json.append(jsonArray);
		} else {
			JSONArray jsonArray = JSONArray.fromObject(subRoot);
			json.append(jsonArray);
		}

		return json.toString();
	}

	/**
	 * 查询模板子节点
	 * 
	 * @param parentid
	 * @param list
	 * @return
	 */
	public List<HashMap> getTemplateAllJson(String parentid, List list) {
		// 查询模板
		List<HashMap> tempList = new ArrayList<HashMap>();
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("TYPE", parentid);
		tempList = DemAPI.getInstance().getAllList(TEMPLATE_UUID, conditionMap,
				"ID");
		if (tempList != null && tempList.size() > 0) {
			List<HashMap> tempRoot = new ArrayList<HashMap>();
			for (HashMap<String, Object> tempMap : tempList) {
				HashMap<String, Object> tempNode = new HashMap<String, Object>();
				tempNode.put("id", tempMap.get("ID").toString());
				tempNode.put("name", tempMap.get("TEMPLATENAME").toString());
				tempNode
						.put("instanceid", tempMap.get("INSTANCEID").toString());
				tempNode.put("open", false);
				tempNode.put("isParent", false);
				tempNode.put("type", "file");
				String uuid = "";
				if (tempMap.get("CONTENT") != null) {
					uuid = tempMap.get("CONTENT").toString();
				}
				tempNode.put("uuid", uuid);
				tempNode.put("iconOpen", "iwork_img/package_add.png");
				tempNode.put("iconClose", "iwork_img/package_delete.png");
				tempNode.put("icon", "iwork_img/file/word.jpg");

				list.add(tempNode);
			}
		}
		return list;
	}

	/**
	 * 获取当前公司下的子模板分类
	 * 
	 * @param parentid
	 * @return
	 */
	public List<HashMap> getSubTemplateAllJson(String parentid) {
		List<HashMap> tempList = new ArrayList<HashMap>();
		List<HashMap> subList = new ArrayList<HashMap>();
		List<HashMap> rows = new ArrayList();

		// 从模板分类列表获取记录
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		if (parentid != null) {
			conditionMap.put("FID", parentid);
		}
		ArrayList<HashMap> subRoot = new ArrayList<HashMap>();
		List<HashMap> typeList = DemAPI.getInstance().getList(
				TEMPLATE_TYPE_UUID, conditionMap, "ID");
		for (HashMap<String, Object> hash : typeList) {
			HashMap<String, Object> subNode = new HashMap<String, Object>();
			conditionMap.clear();
			conditionMap.put("FID", hash.get("ID").toString());
			subList.clear();
			subList = DemAPI.getInstance().getAllList(TEMPLATE_TYPE_UUID,
					conditionMap, "ID");

			conditionMap.clear();
			conditionMap.put("TYPE", hash.get("ID").toString());
			tempList = DemAPI.getInstance().getAllList(TEMPLATE_UUID,
					conditionMap, "ID");

			subNode.put("id", hash.get("ID").toString());
			subNode.put("name", hash.get("LBMC").toString());
			subNode.put("companyno", hash.get("COMPANYNO").toString());
			subNode.put("companyname", hash.get("COMPANYNAME").toString());
			subNode.put("instanceid", hash.get("INSTANCEID").toString());
			subNode.put("open", true);
			if ((subList != null && subList.size() > 0)
					|| (tempList != null && tempList.size() > 0)) {
				subNode.put("isParent", true);
			} else {
				subNode.put("isParent", false);
			}
			subNode.put("iconOpen", "iwork_img/package_add.png");
			subNode.put("iconClose", "iwork_img/package_delete.png");
			subNode.put("icon", "iwork_img/package.png");

			rows.add(subNode);
		}

		return rows;
	}

	/**
	 * 获取当前公司下的模板分类、模板及子公司
	 * 
	 * @param parentid
	 * @param companyno
	 * @return
	 */
	public List<HashMap> getTreeAllJson(String parentid, String companyno) {
		List<HashMap> tempList = new ArrayList<HashMap>();
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		// List<OrgCompany> orgcompanyList =
		// orgCompanyDAO.getCompanyList(parentid);
		List<HashMap> rows = new ArrayList();
		List<HashMap> templateList = getSubTemplateAllJson(companyno);
		conditionMap.clear();
		conditionMap.put("TYPE", companyno);
		tempList = DemAPI.getInstance().getAllList(TEMPLATE_UUID, conditionMap,
				"ID");
		if (tempList != null && tempList.size() > 0) {
			templateList = getTemplateAllJson(companyno, templateList);
		}
		// if(orgcompanyList != null){
		// HashMap<String,Object> item;
		// Iterator<OrgCompany> iterator = orgcompanyList.iterator();
		// while(iterator.hasNext()){
		// OrgCompany oc = (OrgCompany)iterator.next();
		// item = new HashMap<String,Object>();
		// item.put("id", oc.getCompanyno());
		// item.put("nodeType", "company");
		// item.put("name", oc.getCompanyname());
		// item.put("companyno", oc.getCompanyno());
		// item.put("companyname", oc.getCompanyname());
		// item.put("companytype", oc.getCompanytype());
		// item.put("parentid", oc.getParentid());
		// //item.put("pageurl", (new
		// StringBuilder("company_list.action?parentid=")).append(oc.getId()).toString());
		// item.put("iconOpen", "iwork_img/organization.gif");
		// item.put("iconClose", "iwork_img/organization.gif");
		// item.put("icon", "iwork_img/organization.gif");
		// item.put("open", Boolean.valueOf(false));
		// List subCompanyList = orgCompanyDAO.getCompanyList(oc.getId());
		// List subTemplateList = getSubTemplateJson(oc.getCompanyno());
		//	    		
		// conditionMap.clear();
		// conditionMap.put("TYPE", oc.getCompanyno());
		// tempList = DemAPI.getInstance().getAllList(TEMPLATE_UUID,
		// conditionMap, "ID");
		// if((subCompanyList != null && subCompanyList.size() > 0) ||
		// (subTemplateList != null && subTemplateList.size() >
		// 0)||(tempList!=null && tempList.size()>0))
		// item.put("children", getTreeAllJson(oc.getId(),oc.getCompanyno()));
		// item.put("target", "companyFrame");
		// item.put("companyId", "companyFrame");
		//	                
		// templateList.add(item);
		// }

		// }

		return templateList;
	}

	/**
	 * 判断是否有子节点
	 * 
	 * @param parentid
	 * @param companyno
	 * @return
	 */
	public boolean ifHasTreeChildren(String parentid, String companyno) {
		boolean flag = false;
		// List orgcompanyList = orgCompanyDAO.getCompanyList(parentid);
		//		 
		// // 从模板分类列表获取记录
		// HashMap<String,Object> conditionMap = new HashMap<String,Object>();
		// if(parentid!=null){
		// conditionMap.put("FID", parentid);
		// }
		// List<HashMap> typeList =
		// DemAPI.getInstance().getList(TEMPLATE_TYPE_UUID, conditionMap, "ID");
		// conditionMap.clear();
		// conditionMap.put("TYPE", companyno);
		// List<HashMap> tempList =
		// DemAPI.getInstance().getAllList(TEMPLATE_UUID, conditionMap, "ID");
		// if((orgcompanyList!=null&&orgcompanyList.size()>0)||(typeList!=null&&typeList.size()>0)||(tempList!=null&&tempList.size()>0)){
		// flag = true;
		// }
		return flag;
	}

	/**
	 * 判断是否存在默认模板
	 * 
	 * @param instanceid
	 * @param hylx
	 * @param mblx
	 * @return
	 */
	public boolean validateIsDef(Long instanceid, String hylx, String mblx) {
		boolean IS_DEF = false;
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		List<HashMap> list = new ArrayList<HashMap>();
		if (hylx != null) {
			conditionMap.put("HYLX", hylx);
		}
		if (mblx != null) {
			conditionMap.put("MBLX", mblx);
		}

		list = DemAPI.getInstance().getList(TEMPLATE_UUID, conditionMap, "ID");

		if (list.size() > 0) {
			for (HashMap m : list) {
				String is_def = m.get("IS_DEF").toString();
				if ("是".equals(is_def)) {
					if (instanceid != null && !"".equals(instanceid)) {
						Long l = Long.parseLong(m.get("INSTANCEID").toString());
						if (!instanceid.equals(l)) {
							IS_DEF = true;
						}
					} else {
						IS_DEF = true;
					}
				}
			}
		}

		return IS_DEF;
	}

	public String getMb(Long instanceid) {
		//模板表单数据
				HashMap<String,Object> hashMap=DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
				//拼表单对象
				StringBuffer content=new StringBuffer();
				if(hashMap!=null&&hashMap.size()>0){
					content.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"ke-zeroborder\">");
					content.append("<tbody>");
					content.append("<tr id=\"itemTr_0\">");
					content.append("<td id=\"title_TEMPLATENAME\" class=\"td_title\" width=\"15%\">");
					content.append("<span style=\"color:red;\">*</span>模板名称");
					content.append("</td>");
					content.append("<td id=\"data_TEMPLATENAME\" class=\"td_data\" width=\"35%\">");
					//模板名称
					String templatename=hashMap.get("TEMPLATENAME")!=null?hashMap.get("TEMPLATENAME").toString():"";
					content.append("<input type=hidden name='TEMPLATENAME'  value=\""+templatename+"\">"+templatename+"&nbsp;");
					content.append("</td>");
					/*content.append("<td id=\"title_MBLX\" class=\"td_title\" width=\"15%\">");
					content.append("<span style=\"color:red;\">*</span>模板类型");
					content.append("</td>");
					content.append("<td id=\"data_MBLX\" class=\"td_data\" width=\"35%\">");
					String mblx=hashMap.get("MBLX")!=null?hashMap.get("MBLX").toString():"";
					content.append("<select   disabled=\"disabled\" style=\"background:#efefef;\" <option value=''>-空-</option>"
							+ "<option value=\"临时公告\" "+(mblx.equals("临时公告")?"selected":"")+" >临时公告</option><option value=\"定期报告\" "
							+(mblx.equals("定期报告")?"selected":"")+">定期报告</option></select>");
					content.append("</td>");*/
					content.append("<td id=\"title_IS_DEF\" class=\"td_title\" width=\"15%\">");
					content.append("<span style=\"color:red;\">*</span>是否为默认模板");
					content.append("</td>");
					content.append("<td id=\"data_IS_DEF\" class=\"td_data\" width=\"35%\">");
					//公告类型
					String is_def=hashMap.get("IS_DEF")!=null?hashMap.get("IS_DEF").toString():"";
					String displayenum=DBUtil.getString("select * from sys_engine_iform_map t where field_name='IS_DEF' AND IFORM_ID='129'","DISPLAY_ENUM");
					if(displayenum!=null&&!displayenum.equals("")){
						String[] displayArray=displayenum.split("\\|");
						for(int i=0;i<displayArray.length;i++){
							String display=displayArray[i];
							content.append("<label>");
							content.append("<input type=\"radio\"  disabled=\"disabled\" "+(is_def==null?"":
								(is_def.equals(display)?"checked=\"checked\"":""))+"/>" +
							"<input type=\"hidden\"  id='IS_DEF"+i+"' name='IS_DEF' value='"+display+"' >"+display+"</label>");
						}
					}

					content.append("&nbsp;");
					content.append("</td>");
					content.append("</tr>");
					content.append("<tr>");
					content.append("<td id=\"title_CONTENT\" class=\"td_title\">");
					content.append("提示:");
					content.append("</td>");
					content.append("<td id=\"data_CONTENT\" class=\"td_data\" colspan=\"3\">");
					content.append("如果下方的word在线编辑器显示不出来，那么可以点击<a href=\"iwork_file/BIGTXT_FILE/WordEdit.zip\" target=\"_blank\">此处</a>进行插件安装&nbsp;");
					content.append("</td>");
					content.append("</tr>");
			     	content.append("<tr id=\"txtAreaTr_1546\">");
			     	content.append("<td id=\"data_CONTENT\" class=\"td_data\" colspan=\"4\">");
			     	content.append("<div id=\"DivID\">");
			     	content.append("<script src='iwork_js/plugs/iweboffice_load.js'></script>");
			     	content.append("  <script type=\"text/javascript\">").append("\n");
			     	content.append("  $(function(){").append("\n");
			     	content.append("  	try{").append("\n");
			     	content.append("  	Load();").append("\n");
			     	content.append("  		}catch(e){").append("\n");
			     	content.append("  		}").append("\n");
			     	content.append("  	}); ").append("\n");
			     	content.append("  function Load(){").append("\n");
			     	content.append("	try{").append("\n");
			    	ActionContext actionContext = ActionContext.getContext();
			    	//获得response对象
					HttpServletRequest request=(HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);  
					   String   url=request.getScheme()+"://";   
				        url+=request.getHeader("host");   
				     String userid=UserContextUtil.getInstance().getCurrentUserId();
				    	String text=hashMap.get("CONTENT")!=null?hashMap.get("CONTENT").toString():"";
			     	content.append("	iformMain.WebOffice.WebUrl=\"").append(url).append("/weboffice/\"").append("\n");
			     	content.append("	iformMain.WebOffice.RecordID=\"").append(text).append("\"; ").append("\n");           //RecordID:本文档记录编号
					content.append("	iformMain.WebOffice.FileName=\"").append("模板正文").append("\"; ").append("\n");           //FileName:文档名称 
					content.append("	iformMain.WebOffice.Compatible=false;;").append("\n");            //FileType:文档类型  .doc  .xls  .wps
					content.append("	iformMain.WebOffice.FileType=\".doc\";").append("\n");            //FileType:文档类型  .doc  .xls  .wps
					content.append("	iformMain.WebOffice.UserName=\"").append(userid).append("\"; ").append("\n");           //UserName:操作用户名，痕迹保留需要
					content.append("	iformMain.WebOffice.EditType=\"2,1\"; ").append("\n");           //EditType:编辑类型  方式一、方式二  <参考技术文档>
					content.append("	iformMain.WebOffice.MaxFileSize = 8 * 1024; ").append("\n");           //EditType:编辑类型  方式一、方式二  <参考技术文档>
					content.append("	iformMain.WebOffice.Language=\"CH\";").append("\n");                        //Language:多语言支持显示选择   CH简体 TW繁体 EN英文
					content.append("	iformMain.WebOffice.PenColor=\"#FF0000\";").append("\n");                   //PenColor:默认批注颜色
					content.append("	iformMain.WebOffice.PenWidth=\"1\";").append("\n");                         //PenWidth:默认批注笔宽
					content.append("	iformMain.WebOffice.Print=\"1\";").append("\n");                            //Print:默认是否可以打印:1可以打印批注,0不可以打印批注
					content.append("	iformMain.WebOffice.ShowToolBar=\"0\";").append("\n");                      //ShowToolBar:是否显示工具栏:1显示,0不显示
					content.append("	iformMain.WebOffice.ShowMenu=\"0\";").append("\n");                          //控制整体菜单显示
					content.append("	iformMain.WebOffice.height=\"500\";").append("\n");                          //控制整体菜单显示
					content.append("	iformMain.WebOffice.width=\"100%\";").append("\n");
					content.append("	iformMain.WebOffice.WebRepairMode = true;").append("\n"); 
					content.append("	iformMain.WebOffice.WebOpen();").append("\n");                            //打开该文档    交互OfficeServer  调出文档OPTION="LOADFILE"    调出模板OPTION="LOADTEMPLATE"     <参考技术文档>
					content.append("	}catch(e){").append("\n");
					content.append("		alert(e);").append("\n");
					content.append("	}").append("\n");
					content.append("}").append("\n");   
					content.append("  </script>").append("\n");
					content.append("<div id=\"DivID\">").append("\n"); 
					content.append("<table width=\"100%\"  cellspacing=\"0\" cellpadding=\"0\">").append("\n");
					content.append("<tr><td>").append("\n");
					content.append(WebOfficeTools.getInstance().getWebBrowserObjHtml());
					content.append("</td></tr></table>").append("\n");
			    	content.append("</div>");
			    	content.append("<input type='hidden' name='CONTENT'  id='CONTENT'  value='"+text+"' >&nbsp;");
			    	content.append("</td>");
			    	content.append("</tr>");
			       content.append("</tbody>");
			    	content.append("</table>");

					
				}
				return content.toString();
	}
	
	public boolean batchTemplateSave(String content) {
		boolean saveFormData = false;
		StringBuffer sql= new StringBuffer();
		String COMPANYNO=DBUtil.getString("select COMPANYNO from orgcompany", "COMPANYNO");
		String COMPANYNAME=DBUtil.getString("select COMPANYNAME from orgcompany", "COMPANYNAME");
		String userid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid();
		List<FileUpload> sublist = FileUploadAPI.getInstance()
				.getFileUploads(content);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		for (FileUpload fileUpload : sublist) {
			HashMap hashmap = new HashMap();
			Long instanceid = DemAPI.getInstance()
					.newInstance(
							TEMPLATE_UUID,
							UserContextUtil.getInstance()
									.getCurrentUserContext()._userModel
									.getUserid());
			hashmap.put("formid", "129");
			hashmap.put("COMPANYNO", COMPANYNO);
			hashmap.put("COMPANYNAME", COMPANYNAME);
			hashmap.put("TEMPLATENAME", fileUpload.getFileSrcName().substring(0, fileUpload.getFileSrcName().indexOf(".")));
			hashmap.put("IS_DEF", "否");
			hashmap.put("CREATEDATE", sd.format(new Date()));
			hashmap.put("CREATEUSER", userid);
			hashmap.put("CONTENT", fileUpload.getFileId());
			hashmap.put("INSTANCEID", instanceid);
			saveFormData = DemAPI.getInstance().saveFormData(TEMPLATE_UUID,
					instanceid, hashmap, false);
		}
		return saveFormData;
	}
}
