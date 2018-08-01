package com.ibpmsoft.project.zqb.sx.action;

import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.sx.service.ZqbProjectSxService;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.DemAPI;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbProjectSxAction extends ActionSupport {
	private ZqbProjectSxService zqbProjectSxService;
	
	private String projectno;
	private List<HashMap> sxQusansList;
	private String demid;
	private String formid;
	private String xmbz;
	private String question;
	private String content;
	private String commonstr;
	private String qins;
	private String ains;
	private String date;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCommonstr() {
		return commonstr;
	}
	public void setCommonstr(String commonstr) {
		this.commonstr = commonstr;
	}
	public String getQins() {
		return qins;
	}
	public void setQins(String qins) {
		this.qins = qins;
	}
	public String getAins() {
		return ains;
	}
	public void setAins(String ains) {
		this.ains = ains;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getXmbz() {
		return xmbz;
	}
	public void setXmbz(String xmbz) {
		this.xmbz = xmbz;
	}
	public String getDemid() {
		return demid;
	}
	public void setDemid(String demid) {
		this.demid = demid;
	}
	public String getFormid() {
		return formid;
	}
	public void setFormid(String formid) {
		this.formid = formid;
	}
	public List<HashMap> getSxQusansList() {
		return sxQusansList;
	}
	public void setSxQusansList(List<HashMap> sxQusansList) {
		this.sxQusansList = sxQusansList;
	}
	public String getProjectno() {
		return projectno;
	}
	public void setProjectno(String projectno) {
		this.projectno = projectno;
	}

	public ZqbProjectSxService getZqbProjectSxService() {
		return zqbProjectSxService;
	}
	public void setZqbProjectSxService(ZqbProjectSxService zqbProjectSxService) {
		this.zqbProjectSxService = zqbProjectSxService;
	}

	public String indexSxQusans(){
		String demid$formid = DBUtil.getString("SELECT ID||'@'||FORMID AS DEMID$FORMID FROM SYS_DEM_ENGINE WHERE TITLE='问题反馈表单'", "DEMID$FORMID");
		demid=demid$formid.split("@")[0];
		formid=demid$formid.split("@")[1];
		sxQusansList = zqbProjectSxService.getSxQusans(projectno);
		return SUCCESS;
	}
	public String qusAnsAdd(){
		return SUCCESS;
	}
	public void qusAnsDel(){
		boolean flag = zqbProjectSxService.qusAnsDel(commonstr);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("删除失败,请重试!");
		}
	}
	public void qusAnsSave(){
		zqbProjectSxService.qusAnsSave(projectno,question,content,date);
	}
	public String qusAnsEdit(){
		String[] qinsains = commonstr.split(",");
		qins = qinsains[0];
		ains = qinsains[1];
		HashMap questionmap = DemAPI.getInstance().getFromData(Long.parseLong(qins));
		HashMap contentmap = DemAPI.getInstance().getFromData(Long.parseLong(ains));
		question = questionmap.get("QUESTION")==null?"":questionmap.get("QUESTION").toString();
		date = questionmap.get("CREATEDATE")==null?"":questionmap.get("CREATEDATE").toString().substring(0, 10);
		content = contentmap.get("CONTENT")==null?"":contentmap.get("CONTENT").toString();
		return SUCCESS;
	}
	public void qusAnsUpdate(){
		zqbProjectSxService.qusAnsUpdate(qins,ains,question,content,date);
	}
	public void saveBeforeUseridUsername(){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String userid = user.getUserid();
		String username = user.getUsername();
		ResponseUtil.write(userid+"&"+username);
	}
	public void industryMsgAssociate(){
		String jsonstr=zqbProjectSxService.industryMsgAssociate(xmbz);
		ResponseUtil.write(jsonstr); 
	}
	
}
