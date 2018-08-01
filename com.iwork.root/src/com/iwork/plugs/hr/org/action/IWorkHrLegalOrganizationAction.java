package com.iwork.plugs.hr.org.action;

import java.util.HashMap;
import java.util.List;

import com.iwork.core.engine.dem.model.SysDemEngine;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.hr.org.constant.OrgConstants;
import com.iwork.plugs.hr.org.service.IWorkHrLegalOrganizationService;
import com.iwork.sdk.DemAPI;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkHrLegalOrganizationAction  extends ActionSupport{

	private IWorkHrLegalOrganizationService iWorkHrLegalOrganizationService;
	private Long company_demid;
	private Long dept_demid;
	private Long user_demid;
	private Long company_formid;
	private Long dept_formid;
	private Long instanceid;
	private String companyno;
	private String deptno;
	private String userids;
	private String userid;
	private Long user_formid;
	private List<HashMap> deptlist;
	private List<HashMap> userlist;
	
	/**
	 * 法人组织结构
	 * @return
	 */
	public String index(){
		SysDemEngine model = DemAPI.getInstance().getDemModel(OrgConstants.ORG_LEGAL_COMPANY_UUID);
		if(model!=null){
			company_demid = model.getId();
			company_formid = model.getFormid();
		}
		 model = DemAPI.getInstance().getDemModel(OrgConstants.ORG_LEGAL_DEPT_UUID);
		if(model!=null){
			dept_demid = model.getId();
			dept_formid = model.getFormid();
		}
		 model = DemAPI.getInstance().getDemModel(OrgConstants.ORG_LEGAL_USER_UUID);
		if(model!=null){
			user_demid = model.getId(); 
			user_formid = model.getFormid();
		}
		return SUCCESS;
	}
	
	public String showuser(){
		
		return SUCCESS;
	}
	
	public void adduser(){ 
		if(instanceid!=null&&deptno!=null&&userids!=null){
			iWorkHrLegalOrganizationService.addUser(instanceid, deptno, userids);
		} 
		ResponseUtil.write(SUCCESS);
	}
	public void removeuser(){ 
		if(instanceid!=null){
			iWorkHrLegalOrganizationService.removeuser(instanceid);
		}  
		ResponseUtil.write(SUCCESS);
	}
	
	public String list(){ 
		if(companyno!=null&&deptno!=null){
			HashMap conditionMap = new HashMap();
			conditionMap.put("PARENTID", deptno);
			conditionMap.put("LEGAL_COMY_NO", companyno);
			deptlist = DemAPI.getInstance().getList(OrgConstants.ORG_LEGAL_DEPT_UUID, conditionMap, null);
			
			HashMap conditionMap2 = new HashMap();
			conditionMap2.put("LEGAL_DEPT_NO", deptno);
			conditionMap2.put("LEGAL_COMY_NO", companyno); 
			userlist = DemAPI.getInstance().getList(OrgConstants.ORG_LEGAL_USER_UUID, conditionMap2, null);
		}
		return SUCCESS;
	}
	
	public void treejson(){
		String json = iWorkHrLegalOrganizationService.getTreeJson();
		ResponseUtil.write(json);
	}
	
	/**
	 * 获得法人组织信息
	 */
	public void getUserLegalOrgInfo(){
		if(userid!=null){ 
			String json =iWorkHrLegalOrganizationService.getUserLegalOrgInfo(userid);
			ResponseUtil.write(json);
		}
	}
	
	
	public void setIWorkHrLegalOrganizationService(
			IWorkHrLegalOrganizationService iWorkHrLegalOrganizationService) {
		this.iWorkHrLegalOrganizationService = iWorkHrLegalOrganizationService;
	}
	public Long getCompany_demid() {
		return company_demid;
	}
	public Long getDept_demid() {
		return dept_demid;
	}
	public Long getUser_demid() {
		return user_demid;
	}
	public Long getCompany_formid() {
		return company_formid;
	}
	public Long getDept_formid() {
		return dept_formid;
	}
	public Long getUser_formid() {
		return user_formid;
	}
	public void setiWorkHrLegalOrganizationService(
			IWorkHrLegalOrganizationService iWorkHrLegalOrganizationService) {
		this.iWorkHrLegalOrganizationService = iWorkHrLegalOrganizationService;
	}
 
	public List<HashMap> getDeptlist() {
		return deptlist;
	}

	public List<HashMap> getUserlist() {
		return userlist;
	}

	public String getCompanyno() {
		return companyno;
	}

	public void setCompanyno(String companyno) {
		this.companyno = companyno;
	}

	public String getDeptno() {
		return deptno;
	}

	public void setDeptno(String deptno) {
		this.deptno = deptno;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public String getUserids() {
		return userids;
	}

	public void setUserids(String userids) {
		this.userids = userids;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
}
