package com.iwork.core.organization.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import com.iwork.commons.util.UtilNumber;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.tools.UserContextUtil;

public class OrgCompanyService {
	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	
	
	/**
	 * 获取树节点json
	 * @return
	 */
	public String getTreeJson(String parentid){ 
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		if(parentid==null){
			parentid = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getId();
			OrgCompany oc = orgCompanyDAO.getBoData(parentid);
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id",oc.getId()); 
			item.put("nodeType","company");
			item.put("name", oc.getCompanyname());
			item.put("companyno", oc.getCompanyno());
			item.put("companyname", oc.getCompanyname()); 
			item.put("companytype", oc.getCompanytype());
			item.put("parentid", oc.getParentid());
			item.put("pageurl","company_list.action?parentid="+oc.getId());
			item.put("iconOpen", "iwork_img/organization.gif");
			item.put("iconClose", "iwork_img/organization.gif"); 
			item.put("icon", "iwork_img/organization.gif"); 
			item.put("open", true);
			List<OrgCompany> subCompanyList =  orgCompanyDAO.getCompanyList(oc.getId(),null);
			if(subCompanyList!=null&&subCompanyList.size()>0){
				item.put("children", this.getTreeJson(oc.getId())); 
			}
			item.put("target", "companyFrame");
			item.put("companyId", "companyFrame");
			rows.add(item);
		}else{
			List<OrgCompany> orgcompanyList = orgCompanyDAO.getCompanyList(parentid,null);
			if(orgcompanyList!=null){
				for(OrgCompany oc:orgcompanyList){
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("id",oc.getId()); 
					item.put("nodeType","company");
					item.put("name", oc.getCompanyname());
					item.put("companyno", oc.getCompanyno());
					item.put("companyname", oc.getCompanyname()); 
					item.put("companytype", oc.getCompanytype());
					item.put("parentid", oc.getParentid());
					item.put("pageurl","company_list.action?parentid="+oc.getId());
					item.put("iconOpen", "iwork_img/organization.gif");
					item.put("iconClose", "iwork_img/organization.gif"); 
					item.put("icon", "iwork_img/organization.gif"); 
					item.put("open", true);
					List<OrgCompany> subCompanyList =  orgCompanyDAO.getCompanyList(oc.getId(),null);
					if(subCompanyList!=null&&subCompanyList.size()>0){
						item.put("children", this.getTreeJson(oc.getId())); 
					}
					item.put("target", "companyFrame");
					item.put("companyId", "companyFrame");
					rows.add(item);
				}
			}
		}
		JSONArray json = null;
		StringBuffer html = new StringBuffer();
		if(parentid==null||parentid.equals("999999")){
			Map<String,Object> root = new HashMap<String,Object>();
			root.put("id","-1"); 
			root.put("nodeType","root");
			root.put("name","组织机构");
			root.put("iconOpen", "iwork_img/logohome.gif");
			root.put("iconClose", "iwork_img/logohome.gif"); 
			root.put("open", true);
			root.put("children",rows); 
			json = JSONArray.fromObject(root);
		}else{
			json = JSONArray.fromObject(rows);
		}
		html.append(json);
		return html.toString();
	}
	
	/**
	 * 获取上市或者非上市公司树节点json
	 * @return
	 */
	public String getSSorFSSTreeJson(String parentid,String companyno,String type){
		if(companyno==null){
			companyno = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getCompanyno();
		}
		if(parentid==null){
			if(companyno.length()<5){
				parentid = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getId();
			}else{
				parentid = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getParentid();
			}
		} 
		StringBuffer html = new StringBuffer();
		List<OrgCompany> orgcompanyList = orgCompanyDAO.getSSorFSSCompanyList(parentid,companyno,type);
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		if(orgcompanyList!=null){
			for(OrgCompany oc:orgcompanyList){
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id",oc.getId()); 
				item.put("nodeType","company");
				item.put("name", oc.getCompanyname());
				item.put("companyno", oc.getCompanyno());
				item.put("companyname", oc.getCompanyname());
				item.put("companytype", oc.getCompanytype());
				item.put("parentid", oc.getParentid());
				item.put("pageurl","company_list.action?parentid="+oc.getId());
				item.put("iconOpen", "iwork_img/organization.gif");
				item.put("iconClose", "iwork_img/organization.gif"); 
				item.put("icon", "iwork_img/organization.gif"); 
				item.put("open", true);
				List<OrgCompany> subCompanyList =  orgCompanyDAO.getSSorFSSCompanyList(oc.getId(),oc.getCompanyno(),type);
				if(subCompanyList!=null&&subCompanyList.size()>0){
					item.put("children", this.getSSorFSSTreeJson(oc.getId(),oc.getCompanyno(),type)); 
				}
				item.put("target", "companyFrame");
				item.put("companyId", "companyFrame");
				rows.add(item);
			}
		}
		JSONArray json = JSONArray.fromObject(rows);
		html.append(json);
		return html.toString();
	}
	
	/**
	 *快速添加组织单位信息 
	 * @return
	 */
	public String quickAddCompany(String parentid,String companyName,String companyCode,String CompanyType){
		StringBuffer log = new StringBuffer();
		//判断当前组织是否存在
		OrgCompany model = orgCompanyDAO.getCompanyModelForExtend1(companyCode);
		if(model==null){
			 model = new OrgCompany();
			 String id = getMaxID();
			 model.setId(id);
			 model.setCompanyname(companyName);
			 model.setExtend1(companyCode);
			 if(CompanyType==null){
				 CompanyType = "2";
			 }
			 model.setCompanytype(CompanyType);
			 model.setParentid(parentid);
			 //获取父节点的组织
			 OrgCompany parentModel = orgCompanyDAO.getBoData(parentid);
			 String parentCompanyNo = "";
			 if(parentModel!=null){
				 parentCompanyNo =  parentModel.getCompanyno();
			 }
			 //获取父节点当前组织列表,判断当前组织序号
			List<OrgCompany> list =  orgCompanyDAO.getCompanyList(parentid,null);
			int sequnceNo = 1;
			if(list!=null&&list.size()>0){
				sequnceNo  = sequnceNo+list.size();
			}
			String num = UtilNumber.fillLength(sequnceNo, 3);
			String companyno = parentCompanyNo+num;
			 model.setCompanyno(companyno);
			 model.setLookandfeel("def");
			 orgCompanyDAO.addBoData(model);
		}else{
			log.append("组织模型已存在");
		}
		return log.toString();
	}
	
	/**
	 * 保存组织
	 * @param model
	 * @return
	 */
	public String saveCompany(OrgCompany model){
		String msg = "success"; 
//		if(!CheckInfo.getInstance().isCheckCompany(model)){
//			msg = "companyName error"; 
//		}else{ 
			OrgCompany orgModel=getBoData(model.getId());
			if(orgModel == null){
				addBoData(model);
				msg = "add";
			}else{
				updateBoData(model);
				msg = "update";
			}
		return msg;
	}
	private void addBoData(OrgCompany obj) {
		orgCompanyDAO.addBoData(obj);
	}

	public void deleteBoData(String id) {
		OrgCompany model=orgCompanyDAO.getBoData(id);
		orgCompanyDAO.deleteBoData(model);
	}

	public List getAll() {
		// TODO Auto-generated method stub
		return orgCompanyDAO.getAll();
	}

	public OrgCompany getBoData(String id) {
		// TODO Auto-generated method stub
		return orgCompanyDAO.getBoData(id);
	} 

	public List getBoDatas(int pageSize, int startRow) {
		// TODO Auto-generated method stub
		return orgCompanyDAO.getBoDatas(pageSize, startRow);
	}

	public List getBoDatas(String parentid,int pageSize,
			int startRow) {
		// TODO Auto-generated method stub
		return orgCompanyDAO.getBoDatas( parentid, pageSize, startRow);
	}

	public String getMaxID() {
		// TODO Auto-generated method stub
		return orgCompanyDAO.getMaxID();
	}

	public int getRows() {
		// TODO Auto-generated method stub
		return orgCompanyDAO.getRows();
	}

	public int getRows(String fieldname, String value) {
		// TODO Auto-generated method stub
		return orgCompanyDAO.getRows(fieldname, value);
	}

	public List queryBoDatas(String fieldname, String value) {
		// TODO Auto-generated method stub
		return orgCompanyDAO.queryBoDatas(fieldname, value);
	}  

	private void updateBoData(OrgCompany obj) {
		orgCompanyDAO.updateBoData(obj);

	}
	/**
	 * 向上移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveUp(int id){
		String type="up";
		orgCompanyDAO.updateIndex(id, type);
	}
	
	/**
	 * 向下移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveDown(int id){
		String type="down";
		orgCompanyDAO.updateIndex(id, type);
	}
	
	/**
	 * 是否存在部门
	 * @param companyid
	 * @return
	 */
	public boolean hasDepartment(String companyid){
		List list = orgDepartmentDAO.getTopDepartmentList(companyid);
		int temp = list.size();
		if(temp==0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 执行组织查询
	 * @param key
	 * @return
	 */
	public String search(String key){
		StringBuffer html = new StringBuffer();
		List<OrgCompany> list = orgCompanyDAO.getBoDatas("companyname",key, 20, 0);
		List searchlist = new ArrayList();
		for(OrgCompany model:list){
			HashMap hash = new HashMap();
			hash.put("companyno",model.getCompanyno());
			hash.put("companyname", model.getCompanyname());
			searchlist.add(hash);
		}
		JSONArray json = JSONArray.fromObject(searchlist);
		html.append(json);
		return html.toString();
		
	}
	
	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}
	
	public OrgCompanyDAO getOrgCompanyDAO() {
		return orgCompanyDAO;
	}

	public OrgDepartmentDAO getOrgDepartmentDAO() {
		return orgDepartmentDAO;
	}

	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}

	
}
