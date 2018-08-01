package com.iwork.plugs.hr.org.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.model.SysDemEngine;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.hr.org.constant.OrgConstants;
import com.iwork.sdk.DemAPI;



public class IWorkHrLegalOrganizationService {
	
	/**
	 * 获得指定用户法人组织结构信息
	 * @param userid
	 * @return
	 */
	public String getUserLegalOrgInfo(String userid){
		String json = "";
		HashMap conditionMap = new HashMap();
		conditionMap.put("USERID", userid);
		List<HashMap> list = DemAPI.getInstance().getList(OrgConstants.ORG_LEGAL_USER_UUID, conditionMap, null);
		if(list!=null&&list.size()>0){
				JSONArray jsonArray = JSONArray.fromObject(list); 
				json = jsonArray.toString();
		}
		return json;
	}
	
	/**
	 * 添加用户
	 * @param deptno
	 * @param userids
	 * @return
	 */
	public void addUser(Long deptInstanceId,String deptno,String userids){
		String[] useridlist = userids.split(",");
		String deptname,companyno,companname;
		HashMap deptdata = DemAPI.getInstance().getFromData(deptInstanceId, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		
		SysDemEngine model = DemAPI.getInstance().getDemModel(OrgConstants.ORG_LEGAL_USER_UUID);
		if(model!=null){
			for(String uid:useridlist){
				if(uid.trim().equals("")){
					continue;
				}
				UserContext uc = UserContextUtil.getInstance().getUserContext(uid.trim());
				if(uc!=null){
					Long instanceId = DemAPI.getInstance().newInstance(model.getId(),UserContextUtil.getInstance().getCurrentUserId());
					HashMap hashdata = new HashMap();
					hashdata.put("USERNO", uc.get_userModel().getUserno());
					hashdata.put("USERNAME", uc.get_userModel().getUsername());
					hashdata.put("USERID", uid);
					if(deptdata!=null){
						hashdata.put("LEGAL_DEPT_NO", deptdata.get("DEPARTMENTID"));
						hashdata.put("LEGAL_DEPT_NAME", deptdata.get("DEPARTMENTNAME"));
						hashdata.put("LEGAL_COMY_NO", deptdata.get("LEGAL_COMY_NO"));
						hashdata.put("LEGAL_COMY_NAME", deptdata.get("LEGAL_COMY_NAME"));
					}
					 DemAPI.getInstance().saveFormData(OrgConstants.ORG_LEGAL_USER_UUID, instanceId, hashdata, false);
				} 
			}
		}
	}
	public String getTreeJson(){
		StringBuffer jsonHtml = new StringBuffer();
		List<HashMap> list = DemAPI.getInstance().getList(OrgConstants.ORG_LEGAL_COMPANY_UUID, null, null);
		List<Map<String,Object>> root = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		JSONArray json = null;
		for(HashMap map:list){
			Map<String,Object> item = new HashMap<String,Object>();
			if(map.get("ID")==null||map.get("CODE")==null||map.get("NAME")==null){
				continue;
			}
			item.put("id", "legal_org_"+map.get("ID"));
			item.put("DATAID", map.get("ID"));
			item.put("name", map.get("NAME")); 
			item.put("COMPANY_NO", map.get("CODE"));
			item.put("COMPANY_NAME", map.get("NAME")); 
			item.put("INSTANCEID", map.get("INSTANCEID"));
			item.put("open", true);  
			item.put("children",this.loadDeptJson(map.get("CODE").toString(),"0"));
			item.put("icon", "iwork_img/icon/organisation.png");
			item.put("iconOpen", "iwork_img/icon/organisation.png"); 
			item.put("iconClose", "iwork_img/icon/organisation.png");
			item.put("type","company"); 
			items.add(item); 
		}
//		Map<String,Object> node = new HashMap<String,Object>();
//		node.put("id", 999999);
//		node.put("name", "法人组织结构");
//		node.put("iconOpen", "iwork_img/icon/organisation.png"); 
//		node.put("iconClose", "iwork_img/icon/organisation.png");
//		node.put("children",items);
//		root.add(node);
		json = JSONArray.fromObject(items); 
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	private List<Map<String,Object>> loadDeptJson(String code,String deptno){
		List<Map<String,Object>> items = new ArrayList();
		
		HashMap hash = new HashMap();
		if(deptno!=null){
			hash.put("PARENTID", deptno);  
		}
		hash.put("LEGAL_COMY_NO", code);
		List<HashMap> list = DemAPI.getInstance().getList(OrgConstants.ORG_LEGAL_DEPT_UUID, hash, null);
		for(HashMap map:list){
			Map<String,Object> item = new HashMap<String,Object>();
			if(map.get("ID")==null||map.get("DEPARTMENTID")==null||map.get("DEPARTMENTNAME")==null){
				continue;
			}
			item.put("id", "legal_user_"+map.get("ID"));
			item.put("DATAID", map.get("ID"));
			item.put("name", map.get("DEPARTMENTNAME"));
			item.put("DEPT_NAME", map.get("DEPARTMENTNAME"));
			item.put("DEPT_NO", map.get("DEPARTMENTID"));
			item.put("COMPANY_NAME", map.get("LEGAL_COMY_NAME"));
			item.put("COMPANY_NO", map.get("LEGAL_COMY_NO"));
			if(deptno==null){
				item.put("PARENTID",""); 
			}else{
				item.put("PARENTID",deptno); 
			}
			
			item.put("INSTANCEID", map.get("INSTANCEID"));
			item.put("open", true);  
			item.put("children",this.loadDeptJson(code,map.get("DEPARTMENTID").toString())); 
			item.put("icon", "iwork_img/km/treeimg/ftv2folderopen02.gif");  
			item.put("iconOpen", "iwork_img/km/treeimg/ftv2folderopen02.gif");  
			item.put("iconClose", "iwork_img/km/treeimg/ftv2link01.gif");
			item.put("type","dept"); 
			items.add(item); 
		} 
		return items;
	}
	
	public boolean removeuser(Long instanceId){
		boolean flag = DemAPI.getInstance().removeFormData(instanceId);
		return flag;
	}
}
