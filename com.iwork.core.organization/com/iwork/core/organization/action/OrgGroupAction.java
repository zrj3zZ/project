package com.iwork.core.organization.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.iwork.commons.util.PurviewCommonUtil;
import com.iwork.core.organization.service.OrgGroupService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 团队管理跳转类
 * @author WeiGuangjian
 *
 */
public class OrgGroupAction extends ActionSupport {
	
	private OrgGroupService orgGroupService;
	private String group_id;
	private String group_name;
	private String group_charge;
	private String group_state;
	private String begindate;
	private String enddate;
	private String group_memo;
	private String type;
	private String gid;
	private String addgroupid;
	private String addgroupstr;
	private String item_id;
	
	/**
	 * 主页
	 * @return
	 * @throws Exception
	 */
	public String index() throws Exception {
		//this.setGid(gid);
		PurviewCommonUtil t = PurviewCommonUtil.getInstance();
		boolean b = t.checkUserInPurview("admin", "GROUP:{0001}");
		return "index";
	}
	
	/**
	 * 获取有效团队树
	 * @return
	 * @throws Exception
	 */
	public String openjson() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = orgGroupService.getTreeJson();
		request.setAttribute("OrgGroup", json);
		return "openjson";
	}
	
	/**
	 * 获取全部团队表
	 * @return
	 * @throws Exception
	 */
	public void groupgrid() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = orgGroupService.getGroupGridJson();
		ResponseUtil.write(json);
	}
	
	/**
	 * 团队增删改操作
	 * @return
	 * @throws Exception
	 */
	public String saveGroup()throws Exception{	
		orgGroupService.saveGroup(group_id,group_name,group_charge,group_state,begindate,enddate,group_memo,type); 
		this.setGid("");
		return "success";	
	}
	
	/**
	 * 团队子表
	 * @return
	 * @throws Exception
	 */
	public void itemgrid() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();	
		json = orgGroupService.getItemGridJson(gid);
		ResponseUtil.write(json);
	}
	
	/**
	 * 增加人员获取人员树
	 * @return
	 * @throws Exception
	 */	
	public String persontree()throws Exception{	
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = orgGroupService.getPersonTreeJson();
		request.setAttribute("OrgGroup", json);
		return "openjson";
	}
	
	/**
	 * 保存增加的人员
	 * @return
	 * @throws Exception
	 */
	public String saveAddPerson()throws Exception{	
		orgGroupService.saveAddPerson(addgroupid,addgroupstr); 
		this.setGid(addgroupid);
		return "success";	
	}
	
	/**
	 * 增加部门获取部门树
	 * @return
	 * @throws Exception
	 */
	public String depttree()throws Exception{	
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = orgGroupService.getDeptTreeJson();
		request.setAttribute("OrgGroup", json);
		return "openjson";
	}
	
	/**
	 * 保存增加的部门
	 * @return
	 * @throws Exception
	 */
	public String saveAddDept()throws Exception{	
		orgGroupService.saveAddDept(addgroupid,addgroupstr); 
		this.setGid(addgroupid);
		return "success";	
	}
	
	/**
	 * 增加团队获取团队树
	 * @return
	 * @throws Exception
	 */
	public String grouptree() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = orgGroupService.getGroupTreeJson(gid);
		request.setAttribute("OrgGroup", json);
		return "openjson";
	}
	
	/**
	 * 保存增加的团队
	 * @return
	 * @throws Exception
	 */
	public String saveAddGroup()throws Exception{	
		orgGroupService.saveAddGroup(addgroupid,addgroupstr); 
		this.setGid(addgroupid);
		return "success";	
	}
	
	/**
	 * 团队子表删除操作
	 * @return
	 * @throws Exception
	 */
	public String itemcut()throws Exception{	
		orgGroupService.itemCut(item_id); 
		this.setGid(addgroupid);
		return "success";	
	}
	
	/**
	 * 获取团队人员详单
	 * @return
	 * @throws Exception
	 */
	public String persongrid() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = orgGroupService.getPersonGridJson(gid);
		request.setAttribute("OrgGroup", json);
		return "openjson";
	}
	
	public OrgGroupService getOrgGroupService() {
		return orgGroupService;
	}

	public void setOrgGroupService(OrgGroupService orgGroupService) {
		this.orgGroupService = orgGroupService;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getGroup_charge() {
		return group_charge;
	}

	public void setGroup_charge(String group_charge) {
		this.group_charge = group_charge;
	}

	public String getGroup_state() {
		return group_state;
	}

	public void setGroup_state(String group_state) {
		this.group_state = group_state;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getGroup_memo() {
		return group_memo;
	}

	public void setGroup_memo(String group_memo) {
		this.group_memo = group_memo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddgroupid() {
		return addgroupid;
	}

	public void setAddgroupid(String addgroupid) {
		this.addgroupid = addgroupid;
	}

	public String getAddgroupstr() {
		return addgroupstr;
	}

	public void setAddgroupstr(String addgroupstr) {
		this.addgroupstr = addgroupstr;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}	
}
