package com.iwork.core.organization.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.core.organization.constant.GroupTypeConst;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgGroupDAO;
import com.iwork.core.organization.dao.OrgGroupSubDAO;
import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgGroup;
import com.iwork.core.organization.model.OrgGroupSub;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;

/**
 * 团队管理业务实现类
 * @author WeiGuangjian
 *
 */
public class OrgGroupService {
	
	private OrgGroupDAO orgGroupDAO;
	private OrgGroupSubDAO orgGroupSubDAO;
	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgUserDAO orgUserDAO;
	private OrgRoleDAO orgRoleDAO;
	
	/**
	 * 获取团队树一级节点
	 * @return
	 * @throws ParseException
	 */
	public String getTreeJson() throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();	
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", "0_list");
			item.put("text", "团队列表");
			item.put("iconCls", "icon-ok");
//			item.put("state", "open");
			item.put("children",this.getGroupJson());
			Map<String,Object> attributes = new HashMap<String,Object>();			
			attributes.put("url",null);
			attributes.put("target","_blank");
			attributes.put("type","list");
			item.put("attributes", attributes);	
			items.add(item);
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 获取团队树二级节点
	 * @return
	 * @throws ParseException
	 */
	public String getGroupJson() throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		//获取当前日期
		Date Time = new Date();
	  	String now=dateFormat(Time);
		Date nowTime=dateFormat(now);
		List<OrgGroup> list = orgGroupDAO.getOrgGroupList(nowTime);
			for(int i=0;i<list.size();i++){
				OrgGroup model = list.get(i);
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", model.getId()+"_group"); // changelog: type change, item_id, item_type
				item.put("id", model.getId()+"");
				item.put("text", model.getGroupName());
				item.put("iconCls", "icon-ok");
//				item.put("state", "open");
				item.put("children",this.getSubGroupJson(model.getId()));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("groupcharge",model.getGroupCharge());
				attributes.put("groupstate", model.getState());
				attributes.put("begindate",model.getBegindate());
				attributes.put("enddate", model.getEnddate());
				attributes.put("groupmemo",model.getMemo());
				attributes.put("type","group");
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 获取团队树三级节点
	 * @param groupid
	 * @return
	 * @throws ParseException 
	 */
	public String getSubGroupJson(String groupid) throws ParseException{	
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		List<OrgGroupSub> list = orgGroupSubDAO.getOrgGroupSubList(groupid);
		for(int i=0;i<list.size();i++){
			OrgGroupSub model = list.get(i);
			if(model.getItemType().equals("group")){
				String gid=model.getItemId().split("_")[0];
				if(!this.isEffectGroup(gid)){
					continue;
				}
			}else if(model.getItemType().equals("per")){
				String uid=model.getItemId().split("_")[0];
				if(!this.isEffectUser(uid)){
					continue;
				}
			}else if(model.getItemType().equals("dept")){
				String did=model.getItemId().split("_")[0];
				if(!this.isEffectDept(Long.parseLong(did))){
					continue;
				}
			}
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", model.getId()+"_sub");
			item.put("text", model.getItemName()+"("+GroupTypeConst.getInstance().getTypeName(model.getItemType())+")");
			item.put("iconCls", "icon-ok");
			Map<String,Object> attributes = new HashMap<String,Object>();
			attributes.put("groupid",model.getGroupId());
			attributes.put("itemid", model.getItemId());
			attributes.put("itemtype",GroupTypeConst.getInstance().getTypeName(model.getItemType()));
			attributes.put("type","sub");
			item.put("attributes", attributes);
			items.add(item);	
		}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 获取团队表
	 * @return
	 */
	public String getGroupGridJson(){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();	
		List<OrgGroup> list = orgGroupDAO.getOrgGroupAllList();
			for(int i = 0;i<list.size();i++){
				OrgGroup model = list.get(i);
			    Map<String,Object> rows = new HashMap<String,Object>();
				rows.put("id",model.getId());
				rows.put("groupname", model.getGroupName());
				rows.put("groupcharge", model.getGroupCharge());
				rows.put("groupstate",  model.getState());
				rows.put("begindate",  model.getBegindate()==null?null:this.dateFormat(model.getBegindate()));
				rows.put("enddate",  model.getEnddate()==null?null:this.dateFormat(model.getEnddate()));	
				rows.put("groupmemo", model.getMemo());	
			    item.add(rows);			    
			}	  		
		JSONArray json = JSONArray.fromObject(item);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	
	/**
	 * 团队增删改操作
	 * @param group_id
	 * @param group_name
	 * @param group_charge
	 * @param group_state
	 * @param begindate
	 * @param enddate
	 * @param group_memo
	 * @param type
	 * @throws ParseException
	 */
	public void saveGroup(String group_id,String group_name,String group_charge,String group_state,String begindate,String enddate,String group_memo,String type) throws ParseException{
		String groupid=group_id.split("_")[0];
		if(type.equals("edit")){
			OrgGroup model=orgGroupDAO.getBoData(groupid);
			model.setGroupName(group_name);
			model.setGroupCharge(group_charge);
			model.setBegindate(begindate.equals("")?null:dateFormat(begindate));
			model.setEnddate(enddate.equals("")?null:dateFormat(enddate));
			model.setState(group_state);
			model.setMemo(group_memo);
		    orgGroupDAO.updateBoData(model);
		}
		else if(type.equals("add")){
		OrgGroup model =new OrgGroup();
		model.setId(orgGroupDAO.getMaxID());
		model.setGroupName(group_name);
		model.setGroupCharge(group_charge);
		model.setBegindate(begindate.equals("")?null:dateFormat(begindate));
		model.setEnddate(enddate.equals("")?null:dateFormat(enddate));
		model.setState(group_state);
		model.setMemo(group_memo);
		orgGroupDAO.addBoData(model);
	    }
		else if(type.equals("remove")){
		OrgGroup model=orgGroupDAO.getBoData(groupid);
	    orgGroupDAO.deleteBoData(model);
	    List<OrgGroupSub> list=orgGroupSubDAO.getOrgGroupSubList(groupid);
		for(int i=0;i<list.size();i++){
		   OrgGroupSub itemmodel = list.get(i);
	       orgGroupSubDAO.deleteBoData(itemmodel);
		}
	    }		
	}
	
	/**
	 * 获取团队子表
	 * @param gid
	 * @return
	 * @throws ParseException 
	 */
	public String getItemGridJson(String gid) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();
		if(gid!=null&&!gid.equals("")){
		String groupid=gid.split("_")[0];
		List<OrgGroupSub> list = orgGroupSubDAO.getOrgGroupSubList(groupid);
			for(int i = 0;i<list.size();i++){
				OrgGroupSub model = list.get(i);
				if(model.getItemType().equals("group")){
					String ggid=model.getItemId().split("_")[0];
					if(!this.isEffectGroup(ggid)){
						continue;
					}
				}else if(model.getItemType().equals("per")){
					String uid=model.getItemId().split("_")[0];
					if(!this.isEffectUser(uid)){
						continue;
					}
				}else if(model.getItemType().equals("dept")){
					String did=model.getItemId().split("_")[0];
					if(!this.isEffectDept(Long.parseLong(did))){
						continue;
					}
				}
			    Map<String,Object> rows = new HashMap<String,Object>();
				rows.put("id",model.getId());
				rows.put("groupid", model.getGroupId());
				rows.put("itemid", model.getItemId());
				rows.put("itemname",  model.getItemName());
				rows.put("itemtype", GroupTypeConst.getInstance().getTypeName(model.getItemType()));
			    item.add(rows);			    
			}	
		}
		JSONArray json = JSONArray.fromObject(item);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	
	/**
	 * 增加人员获取人员树一级节点
	 * @return
	 * @throws ParseException
	 */
	public String getPersonTreeJson() throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();	
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", "0_list");
			//item.put("nodeTye","super_com");
			item.put("text", "金山软件");
			item.put("iconCls", "icon-ok");			
//			item.put("state", "open");
			item.put("children",this.getPerComTreeJson());
			Map<String,Object> attributes = new HashMap<String,Object>();	
			attributes.put("nodeTye","super_com");
			attributes.put("url",null);
			attributes.put("target","_blank");
			attributes.put("type","list");
			item.put("attributes", attributes);	
			items.add(item);
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 增加人员获取人员树二级节点
	 * @return
	 * @throws ParseException
	 */
	public String getPerComTreeJson() throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List orgcompanyList = orgCompanyDAO.getAll();
			for(int i=0;i<orgcompanyList.size();i++){
				OrgCompany model = (OrgCompany)orgcompanyList.get(i);	
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", model.getId()+"_com");
				//item.put("nodeTye", "com");
				item.put("text", model.getCompanyname());
				item.put("iconCls", "icon-ok");				
//					item.put("state", "open");
				item.put("children",this.getPerDepartmentJson(model.getId()));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("nodeTye", "com");
				attributes.put("companytype",model.getCompanytype());
				attributes.put("companyno", model.getCompanyno());
				attributes.put("companydesc",model.getCompanydesc());
				attributes.put("orderindex", model.getOrderindex());
				attributes.put("lookandfeel",model.getLookandfeel());
				attributes.put("orgadress",model.getOrgadress());
				attributes.put("post",model.getPost());
				attributes.put("email",model.getEmail());
				attributes.put("tel",model.getTel());
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 增加人员获取人员树三级节点
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	public String getPerDepartmentJson(String id) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List departmentList = orgDepartmentDAO.getTopDepartmentList(id);
			for(int i=0;i<departmentList.size();i++){
				OrgDepartment model = (OrgDepartment)departmentList.get(i);
				if(this.getDeptPerson( model.getId())){
					continue;
				}
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", model.getId()+"_dept");
				item.put("id", model.getId()+"");
				//item.put("nodeTye", "dept");
				item.put("text", model.getDepartmentname());
				item.put("iconCls", "icon-ok");				
//					item.put("state", "open");
				item.put("children",this.getPerSubDepartmentJson(model.getId()));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("nodeTye", "dept");
				attributes.put("companyid",model.getCompanyid());
				attributes.put("departmentdesc", model.getDepartmentdesc());
				attributes.put("departmentno",model.getDepartmentno());
				attributes.put("layer",model.getLayer());
				attributes.put("orderindex", model.getOrderindex());			
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 增加人员获取人员树剩余节点
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	public String getPerSubDepartmentJson(Long id) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List perlist = orgUserDAO.getActiveUserList(id);
		for(int i=0;i<perlist.size();i++){
			OrgUser model = (OrgUser)perlist.get(i);
			Map<String,Object> item = new HashMap<String,Object>();
			//item.put("id", model.getUserid()+"_per");
			item.put("id", model.getUserid()+"");
			//item.put("nodeTye", "per");
			item.put("text", model.getUsername());			
			item.put("iconCls", "icon-ok");
			Map<String,Object> attributes = new HashMap<String,Object>();	
			attributes.put("nodeTye", "per");
			attributes.put("departmentname", model.getDepartmentname());
			attributes.put("orgroleid",model.getOrgroleid());
			item.put("attributes", attributes);
			items.add(item);
		}
		List list = orgDepartmentDAO.getSubDepartmentList(id);
			for(int i=0;i<list.size();i++){
				OrgDepartment model = (OrgDepartment)list.get(i);
				if(this.getDeptPerson( model.getId())){
					continue;
				}
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", model.getId()+"_dept");
				item.put("id", model.getId()+"");
				//item.put("nodeType", "dept");
				item.put("text", model.getDepartmentname());
				item.put("iconCls", "icon-ok");
//					item.put("state", "open");
				item.put("children",this.getPerSubDepartmentJson(model.getId()));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("nodeType", "dept");
				attributes.put("companyid",model.getCompanyid());
				attributes.put("parentdepartmentid",model.getParentdepartmentid());
				attributes.put("departmentdesc", model.getDepartmentdesc());
				attributes.put("departmentno",model.getDepartmentno());
				attributes.put("layer",model.getLayer());
				attributes.put("orderindex", model.getOrderindex());			
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 保存增加的人员
	 * @param addgroupid
	 * @param addgroupstr
	 * @throws ParseException
	 */
	public void saveAddPerson(String addgroupid,String addgroupstr) throws ParseException{
		String groupid=addgroupid.split("_")[0];
		//切割字符串	
		String temp[] = addgroupstr.split(":");
		String temp2[] = temp[0].split(",");//itemid列表
		String temp3[] = temp[1].split(",");//itemtext列表
		for(int i=0;i<temp2.length;i++){
			
			//判断是否重复插入
			boolean c=false;
			List<OrgGroupSub> list = orgGroupSubDAO.getOrgGroupSubList(groupid);
			for(int j = 0;j<list.size();j++){
				OrgGroupSub model = list.get(j);
				if(model.getItemId().equals(temp2[i])){
					c=true;
					break;
				}
			}
			if(c==true){
				continue;
			}
			OrgGroupSub model =new OrgGroupSub();
			model.setId(orgGroupSubDAO.getMaxID());
			model.setGroupId(groupid);
			model.setItemId(temp2[i]);
			model.setItemName(temp3[i]);
			model.setItemType("per");
			orgGroupSubDAO.addBoData(model);	
		}
	}
	
	/**
	 * 增加部门获取部门树一级节点
	 * @return
	 * @throws ParseException
	 */
	public String getDeptTreeJson() throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();	
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", "0_list");
			item.put("text", "金山软件");
			item.put("iconCls", "icon-ok");
//			item.put("state", "open");
			item.put("children",this.getComTreeJson());
			Map<String,Object> attributes = new HashMap<String,Object>();			
			attributes.put("url",null);
			attributes.put("target","_blank");
			attributes.put("type","list");
			item.put("attributes", attributes);	
			items.add(item);
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
		
    /**
     * 增加部门获取部门树二级节点
     * @return
     * @throws ParseException
     */
	public String getComTreeJson() throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List orgcompanyList = orgCompanyDAO.getAll();
			for(int i=0;i<orgcompanyList.size();i++){
				OrgCompany model = (OrgCompany)orgcompanyList.get(i);	
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", model.getId()+"_com");
				item.put("text", model.getCompanyname());
				item.put("iconCls", "icon-ok");
//					item.put("state", "open");
				item.put("children",this.getDepartmentJson(model.getId()));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("companytype",model.getCompanytype());
				attributes.put("companyno", model.getCompanyno());
				attributes.put("companydesc",model.getCompanydesc());
				attributes.put("orderindex", model.getOrderindex());
				attributes.put("lookandfeel",model.getLookandfeel());
				attributes.put("orgadress",model.getOrgadress());
				attributes.put("post",model.getPost());
				attributes.put("email",model.getEmail());
				attributes.put("tel",model.getTel());
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 增加部门获取部门树三级节点
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	public String getDepartmentJson(String id) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List departmentList = orgDepartmentDAO.getTopDepartmentList(id);
			for(int i=0;i<departmentList.size();i++){
				OrgDepartment model = (OrgDepartment)departmentList.get(i);
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", model.getId()+"_dept");
				item.put("id", model.getId()+"");
				item.put("text", model.getDepartmentname());
				item.put("iconCls", "icon-ok");
//					item.put("state", "open");
				item.put("children",this.getSubDepartmentJson(model.getId()));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("companyid",model.getCompanyid());
				attributes.put("departmentdesc", model.getDepartmentdesc());
				attributes.put("departmentno",model.getDepartmentno());
				attributes.put("layer",model.getLayer());
				attributes.put("orderindex", model.getOrderindex());			
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 增加部门获取部门树剩余节点
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	public String getSubDepartmentJson(Long id) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List list = orgDepartmentDAO.getSubDepartmentList(id);
			for(int i=0;i<list.size();i++){
				OrgDepartment model = (OrgDepartment)list.get(i);
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", model.getId()+"_dept");
				item.put("id", model.getId()+"");
				item.put("text", model.getDepartmentname());
				item.put("iconCls", "icon-ok");
//					item.put("state", "open");
				item.put("children",this.getSubDepartmentJson(model.getId()));
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("companyid",model.getCompanyid());
				attributes.put("parentdepartmentid",model.getParentdepartmentid());
				attributes.put("departmentdesc", model.getDepartmentdesc());
				attributes.put("departmentno",model.getDepartmentno());
				attributes.put("layer",model.getLayer());
				attributes.put("orderindex", model.getOrderindex());			
				attributes.put("zoneno",model.getZoneno());
				attributes.put("zonename",model.getZonename());
				attributes.put("extend1",model.getExtend1());
				attributes.put("extend2",model.getExtend2());
				attributes.put("extend3",model.getExtend3());
				attributes.put("extend4",model.getExtend4());
				attributes.put("extend5",model.getExtend5());
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 保存增加的部门
	 * @param addgroupid
	 * @param addgroupstr
	 * @throws ParseException
	 */
	public void saveAddDept(String addgroupid,String addgroupstr) throws ParseException{
		String groupid=addgroupid.split("_")[0];
		//切割字符串	
		String temp[] = addgroupstr.split(":");
		String temp2[] = temp[0].split(",");//itemid列表
		String temp3[] = temp[1].split(",");//itemtext列表
		for(int i=0;i<temp2.length;i++){
			
			//判断是否重复插入
			boolean c=false;
			List<OrgGroupSub> list = orgGroupSubDAO.getOrgGroupSubList(groupid);
			for(int j = 0;j<list.size();j++){
				OrgGroupSub model = list.get(j);
				if(model.getItemId().equals(temp2[i])){
					c=true;
					break;
				}
			}
			if(c==true){
				continue;
			}
			OrgGroupSub model =new OrgGroupSub();
			model.setId(orgGroupSubDAO.getMaxID());
			model.setGroupId(groupid);
			model.setItemId(temp2[i]);
			model.setItemName(temp3[i]);
			model.setItemType("dept");
			orgGroupSubDAO.addBoData(model);	
		}
	}
	
	/**
	 * 增加团队获取团队树一级节点
	 * @param gid
	 * @return
	 * @throws ParseException
	 */
	public String getGroupTreeJson(String gid) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();	
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", "0_list");
			item.put("text", "团队列表");
			item.put("iconCls", "icon-ok");
//			item.put("state", "open");
			item.put("children",this.getAddGroupJson(gid));
			Map<String,Object> attributes = new HashMap<String,Object>();			
			attributes.put("url",null);
			attributes.put("target","_blank");
			attributes.put("type","list");
			item.put("attributes", attributes);	
			items.add(item);
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 增加团队获取团队树二级节点
	 * @param gid
	 * @return
	 * @throws ParseException
	 */
	public String getAddGroupJson(String gid) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		if(gid!=null&&!gid.equals("")){
		String groupid=gid.split("_")[0];
		//获取当前日期
		Date Time = new Date();
	  	String now=dateFormat(Time);
	  	Date nowTime=dateFormat(now);
		List<OrgGroup> list = orgGroupDAO.getOrgGroupList(nowTime);
			for(int i=0;i<list.size();i++){
				OrgGroup model = list.get(i);
				if(model.getId().equals(groupid)){
					continue;
				}
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", model.getId()+"_group");
				item.put("id", model.getId()+"");
				item.put("text", model.getGroupName());
				item.put("iconCls", "icon-ok");
//				item.put("state", "open");
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("groupcharge",model.getGroupCharge());
				attributes.put("groupstate", model.getState());
				attributes.put("begindate",model.getBegindate());
				attributes.put("enddate", model.getEnddate());
				attributes.put("groupmemo",model.getMemo());
				attributes.put("type","group");
				item.put("attributes", attributes);
				items.add(item);
			}
		}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 保存增加的团队
	 * @param addgroupid
	 * @param addgroupstr
	 * @throws ParseException
	 */
	public void saveAddGroup(String addgroupid,String addgroupstr) throws ParseException{
		String groupid=addgroupid.split("_")[0];
		//切割字符串	
		String temp[] = addgroupstr.split(":");
		String temp2[] = temp[0].split(",");//itemid列表
		String temp3[] = temp[1].split(",");//itemtext列表
		for(int i=0;i<temp2.length;i++){
			//去除团队列表标题
			if(temp2[i].equals("0_list")){
				continue;
			}
			//判断是否重复插入
			boolean c=false;
			List<OrgGroupSub> list = orgGroupSubDAO.getOrgGroupSubList(groupid);
			for(int j = 0;j<list.size();j++){
				OrgGroupSub model = list.get(j);
				if(model.getItemId().equals(temp2[i])){
					c=true;
					break;
				}
			}
			if(c==true){
				continue;
			}
			OrgGroupSub model =new OrgGroupSub();
			model.setId(orgGroupSubDAO.getMaxID());
			model.setGroupId(groupid);
			model.setItemId(temp2[i]);
			model.setItemName(temp3[i]);
			model.setItemType("group");
			orgGroupSubDAO.addBoData(model);	
		}
	}
	
	/**
	 * 团队子表删除操作
	 * @param id
	 * @throws ParseException
	 */
	public void itemCut(String id) throws ParseException{
		OrgGroupSub model=orgGroupSubDAO.getBoData(id);
		orgGroupSubDAO.deleteBoData(model);
	}
	
	/**
	 * 获取团队人员清单Json
	 * @param gid
	 * @return
	 */
	public String getPersonGridJson(String gid){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();
		if(gid!=null&&!gid.equals("")){
			String groupid=gid.split("_")[0];
			HashMap perlist=this.getPersonGridList(groupid,"dept");
			Iterator iter = perlist.entrySet().iterator(); 
			while (iter.hasNext()) { 
				Map.Entry entry = (Map.Entry) iter.next(); 
				OrgUser permodel = (OrgUser)entry.getValue(); 		
				Map<String,Object> rows = new HashMap<String,Object>();
				rows.put("id",permodel.getId());
				rows.put("userid", permodel.getUserid());
				rows.put("username", permodel.getUsername());
				rows.put("departmentname",  permodel.getDepartmentname());
				OrgRole rolemodel=orgRoleDAO.getBoData(permodel.getOrgroleid()+"");
				rows.put("orgrole",rolemodel.getRolename());
			    item.add(rows);	
			}
		}
		JSONArray json = JSONArray.fromObject(item);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	
	
	/**
	 * 获取团队人员清单列表
	 * @param gid
	 * @return
	 */
	public HashMap getPersonGridList(String gid,String lid){
		HashMap List = new HashMap();
		List<OrgGroupSub> list = orgGroupSubDAO.getOrgGroupSubList(gid);
			for(int i = 0;i<list.size();i++){
				OrgGroupSub model = list.get(i);
				String perid=model.getItemId().split("_")[0];
				if(model.getItemType().equals("per")){
					OrgUser permodel = orgUserDAO.getUserModel(perid);
					if(permodel!=null){
						List.put(permodel.getUserid(), permodel);
					}
				}else if(model.getItemType().equals("dept")){
					List perlist=this.getDeptPersonList(Long.parseLong(perid));
					if(perlist!=null){
						for(int j = 0;j<perlist.size();j++){
							OrgUser permodel = (OrgUser)perlist.get(j);
							List.put(permodel.getUserid(), permodel);
						}
					}
				}else if(model.getItemType().equals("group")){
					String[] ilid=lid.split(":");
					boolean f=false;
					if(ilid!=null){
						for(int k = 0;k<ilid.length;k++){
						if(perid.equals(ilid[k])){
							f=true;
							break;
						}
						}
					}
					if(f){
						continue;
					}
					HashMap perlist=this.getPersonGridList(perid,lid+":"+gid);
					Iterator iter = perlist.entrySet().iterator(); 
					while (iter.hasNext()) { 
						Map.Entry entry = (Map.Entry) iter.next(); 
						OrgUser permodel = (OrgUser)entry.getValue(); 		
						List.put(permodel.getUserid(), permodel);
					}
				}
			    		    
			}	
	return List;	
	}
	
	/**
	 * 获取团队人员列表API
	 * @param groupid
	 * @return
	 */
	public HashMap getGroupPersonList(String groupid){
		HashMap perlist=this.getPersonGridList(groupid,"dept");
		return perlist;
	}
	
	/**
	 * 判断某账户是否属于某团队API
	 * @param userid
	 * @param groupid
	 * @return
	 */
	public boolean isBelongToGroup(String userid,String groupid){
		HashMap perlist=this.getPersonGridList(groupid,"dept");
		Iterator iter = perlist.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
			OrgUser permodel = (OrgUser)entry.getValue(); 		
			if(permodel.getUserid().equals(userid)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断当前用户是否属于某团队API
	 * @param groupid
	 * @return
	 */
	public boolean isBelongToGroup(String groupid){
		UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
		String userid=userContext._userModel.getUserid();
		HashMap perlist=this.getPersonGridList(groupid,"dept");
		Iterator iter = perlist.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
			OrgUser permodel = (OrgUser)entry.getValue(); 		
			if(permodel.getUserid().equals(userid)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断部门节点下是否有人员
	 * @param id
	 * @return
	 */	
	public boolean getDeptPerson(Long id){
	boolean c=true;
	List perlist = orgUserDAO.getActiveUserList(id);
	if(perlist.size()>0){
		c=false;
	}else{
	List deptlist = orgDepartmentDAO.getSubDepartmentList(id);
	for(int i=0;i<deptlist.size();i++){
		OrgDepartment model = (OrgDepartment)deptlist.get(i);
		if(this.getDeptPerson(model.getId())==false){
			c=false;
		}
		
	}
	}
	return c;
	}
	
	/**
	 * 获取部门节点下所有人员列表
	 * @param id
	 * @return
	 */
	public List getDeptPersonList(Long id){
		ArrayList List = new ArrayList();
		List perlist = orgUserDAO.getActiveUserList(id);
	    for(int i=0;i<perlist.size();i++){
	    	OrgUser model = (OrgUser)perlist.get(i);
	    	List.add(model);
	    }
		List deptlist = orgDepartmentDAO.getSubDepartmentList(id);
		for(int i=0;i<deptlist.size();i++){
			OrgDepartment model = (OrgDepartment)deptlist.get(i);
			List sublist=this.getDeptPersonList(model.getId());
			for(int j=0;j<sublist.size();j++){
				OrgUser submodel = (OrgUser)sublist.get(j);
		    	List.add(submodel);
			}			
		}	
		return List;
	}
	
	/**
	 * 时间格式转换：Date转String
	 * @param date
	 * @return
	 */
	public String dateFormat(Date date){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str=format.format(date);
		return str;
	}
	
	/**
	 * 时间格式转换：String转Date
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public Date dateFormat(String str) throws ParseException{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date=format.parse(str);
		return date;
	}
	
	/**
	 * 判断一个团队是否是有效团队
	 * @param id
	 * @return
	 * @throws ParseException 
	 */
	public boolean isEffectGroup(String id) throws ParseException{
		//获取当前日期
		Date Time = new Date();
	  	String now=dateFormat(Time);
	  	Date nowTime=dateFormat(now);
		List<OrgGroup> list = orgGroupDAO.getOrgGroupList(nowTime);
		for(int i=0;i<list.size();i++){
			OrgGroup model = list.get(i);
			if(model.getId().equals(id)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断一个人员是否为有效人员
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	public boolean isEffectUser(String id) throws ParseException{
		//获取当前日期
		Date Time = new Date();
	  	String now=dateFormat(Time);
	  	Time=dateFormat(now);
	  	OrgUser model=orgUserDAO.getUserModel(id);
	  	if(model==null){
	  		return false;
	  	}
		if(model.getUserstate().equals(new Long(1))){
			return false;
		}
	    if(model.getStartdate().getTime()>Time.getTime()||model.getEnddate().getTime()<Time.getTime()){
	    	return false;
	    }
		return true;
	}
	
	/**
	 * 判断一个部门是否为有效部门
	 * @param id
	 * @return
	 */
	public boolean isEffectDept(Long id){
	  	OrgDepartment model=orgDepartmentDAO.getBoData(id);
	  	if(model==null){
	  		return false;
	  	}
		return true;
	}
	
	public OrgGroupDAO getOrgGroupDAO() {
		return orgGroupDAO;
	}
	public void setOrgGroupDAO(OrgGroupDAO orgGroupDAO) {
		this.orgGroupDAO = orgGroupDAO;
	}
	public OrgGroupSubDAO getOrgGroupSubDAO() {
		return orgGroupSubDAO;
	}
	public void setOrgGroupSubDAO(OrgGroupSubDAO orgGroupSubDAO) {
		this.orgGroupSubDAO = orgGroupSubDAO;
	}
	public OrgCompanyDAO getOrgCompanyDAO() {
		return orgCompanyDAO;
	}
	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}
	public OrgDepartmentDAO getOrgDepartmentDAO() {
		return orgDepartmentDAO;
	}
	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}
	public OrgUserDAO getOrgUserDAO() {
		return orgUserDAO;
	}
	public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
		this.orgUserDAO = orgUserDAO;
	}

	public OrgRoleDAO getOrgRoleDAO() {
		return orgRoleDAO;
	}

	public void setOrgRoleDAO(OrgRoleDAO orgRoleDAO) {
		this.orgRoleDAO = orgRoleDAO;
	}
		
}
