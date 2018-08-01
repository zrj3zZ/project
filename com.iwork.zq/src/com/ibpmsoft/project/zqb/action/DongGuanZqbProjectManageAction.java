package com.ibpmsoft.project.zqb.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.et.mvc.util.Json;
import com.ibpmsoft.project.zqb.service.DongGuanZqbProjectManageService;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class DongGuanZqbProjectManageAction extends ActionSupport {
	private String flag;
	private String dgxgzt;
	
	public String getDgxgzt() {
		return dgxgzt;
	}
	public void setDgxgzt(String dgxgzt) {
		this.dgxgzt = dgxgzt;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String index(){
		flag="false";
		dgxgzt="false";
		 UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		 String xmglbid=dongGuanZqbProjectManageService.getConfigUUID("xmglbid");
		 if(xmglbid!=null){
			 if(xmglbid.equals(uc.get_userModel().getDepartmentid().toString())){
				 flag="true";
			 }
		 }
		 String rylb=dongGuanZqbProjectManageService.getConfigUUID("dgxmxgzt");
		 if(rylb!=null && !"".equals(rylb)){
			 String[] ry=rylb.split(",");
			 for (int i = 0; i < ry.length; i++) {
				if(ry[i].equals(uc.get_userModel().getUserid())){
					dgxgzt="true";
					break;
				}
			}
		 }
		//推荐挂牌系项目demid、formid
		tjgpbasemsgdemid = dongGuanZqbProjectManageService.getConfigUUID("tjgpbasemsgdemid");
		tjgpbasemsgformid = dongGuanZqbProjectManageService.getConfigUUID("tjgpbasemsgformid");
		//定向发行项目demid、formid
		dxfxdemid = dongGuanZqbProjectManageService.getConfigUUID("dxfxdemid");
		dxfxformid = dongGuanZqbProjectManageService.getConfigUUID("dxfxformid");
		//并购重组demid、formid
		demid=Long.parseLong(dongGuanZqbProjectManageService.getConfigUUID("bgzzxmdemid")==null||dongGuanZqbProjectManageService.getConfigUUID("bgzzxmdemid").equals("")?"0":dongGuanZqbProjectManageService.getConfigUUID("bgzzxmdemid"));
		formid=Long.parseLong(dongGuanZqbProjectManageService.getConfigUUID("bgzzxmformid")==null||dongGuanZqbProjectManageService.getConfigUUID("bgzzxmformid").equals("")?"0":dongGuanZqbProjectManageService.getConfigUUID("bgzzxmformid"));
		//IPO项目demid、formid
		ipodemid = dongGuanZqbProjectManageService.getConfigUUID("ipodemid");
		ipoformid = dongGuanZqbProjectManageService.getConfigUUID("ipoformid");
		
		if (pageNumber == 0)
			pageNumber = 1;
		if (pageNumber2 == 0)
			pageNumber2 = 1;
		runList = dongGuanZqbProjectManageService.getRunList(pageSize,pageNumber,customername,sssyb,cyrName,xmlx,xmjd,ssxq,type);
		totalNum = dongGuanZqbProjectManageService.getRunListSize(customername,sssyb,cyrName,xmlx,xmjd,ssxq,type);
		
		closeList = dongGuanZqbProjectManageService.getCloseList(pageSize,pageNumber,customername,sssyb,cyrName,xmlx,xmjd,ssxq,type);
		closeNum = dongGuanZqbProjectManageService.getCloseListSize(customername,sssyb,cyrName,xmlx,xmjd,ssxq,type);
		return SUCCESS;
	}
	/**
	 * 东莞项目终止  状态那个字段只有指定人员可以录入
	 */
	public void projectUpd(){
		 dgxgzt="false";
		 UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		 String rylb=dongGuanZqbProjectManageService.getConfigUUID("dgxmxgzt");
		 if(rylb!=null && !"".equals(rylb)){
			 String[] ry=rylb.split(",");
			 for (int i = 0; i < ry.length; i++) {
				if(ry[i].equals(uc.get_userModel().getUserid())){
					dgxgzt="true";
					break;
				}
			}
		 }
		ResponseUtil.write(dgxgzt);
		
	}
	public void projectClose(){
		boolean flag = dongGuanZqbProjectManageService.projectClose(xmlx, instanceId);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	public String jdxx(){
		content = dongGuanZqbProjectManageService.getJdxx(projectno);
		return SUCCESS;
	}
	public String loadPj() {
		pjformid = dongGuanZqbProjectManageService.getConfigUUID("pjformid");
		pjdemid = dongGuanZqbProjectManageService.getConfigUUID("pjdemid");
		content = dongGuanZqbProjectManageService.loadPj(projectno);
		return SUCCESS;
	}
	public void deletePj(){
		boolean flag = DemAPI.getInstance().removeFormData(Long.parseLong(instanceId));
		String result = "error";
		if(flag){
			result="success";
		}
		ResponseUtil.write(result);
	}
	public void checkProName(){
		String result = "success";
		/* 2018-06-14 王欢
		 *  添加和修改时判断了 是否有一条记录，没有的话才能添加 
		 *  只有推荐挂牌，和首次公开发行 进行条数判断
		 * */
		boolean flag = false ; 
		if(type.equals("scssgs")||type.equals("scssgs")){
			flag = dongGuanZqbProjectManageService.checkProName(projectname,instanceid,type);
		}else{
			flag = true ;
		}
		if(!flag){
			result = "error";
		}
		ResponseUtil.write(result);
	}
	public String expPjIndex() {
		return SUCCESS;
	}
	public void expPj(){
		HttpServletResponse response = ServletActionContext.getResponse();
		dongGuanZqbProjectManageService.expPj(response,expfields,customername,sssyb,cyrName,type,ptype);
	}
	public void getCutomermsg(){
		String customermsg = dongGuanZqbProjectManageService.getCustomermsg(customerno);
		ResponseUtil.write(customermsg);
	}
	public void getCusMsgByProno(){
		String customermsg = dongGuanZqbProjectManageService.getCusMsgByProno(projectno);
		ResponseUtil.write(customermsg);
	}
	public void getCutomeUrl(){
		String customerurl = dongGuanZqbProjectManageService.getCutomeUrl(customerno);
		ResponseUtil.write(customerurl);
	}
	public void getProjectmsg(){
		String projectmsg = dongGuanZqbProjectManageService.getProjectmsg(projectno);
		ResponseUtil.write(projectmsg);
	}
	public void getIPOProjectmsg(){
		String projectmsg = dongGuanZqbProjectManageService.getIPOProjectmsg(projectno);
		ResponseUtil.write(projectmsg);
	}
	
	public String projectXmjd(){
		if(instanceId != null && Long.parseLong(instanceId) > 0){
			HashMap dataMap = DemAPI.getInstance().getFromData(Long.parseLong(instanceId), EngineConstants.SYS_INSTANCE_TYPE_DEM);
			projectname = dataMap.get("PROJECTNAME").toString();
		}
		dxfxlxformid=dongGuanZqbProjectManageService.getConfigUUID("dxfxlxformid");
		dxfxlxdemid=dongGuanZqbProjectManageService.getConfigUUID("dxfxlxdemid");
		dxfxgkformid=dongGuanZqbProjectManageService.getConfigUUID("dxfxgkformid");
		dxfxgkdemid=dongGuanZqbProjectManageService.getConfigUUID("dxfxgkdemid");
		dxfxbaformid=dongGuanZqbProjectManageService.getConfigUUID("dxfxbaformid");
		dxfxbademid=dongGuanZqbProjectManageService.getConfigUUID("dxfxbademid");
		
		dxfxjyformid=dongGuanZqbProjectManageService.getConfigUUID("dxfxjyformid");
		dxfxjydemid=dongGuanZqbProjectManageService.getConfigUUID("dxfxjydemid");
		dxfxrgformid=dongGuanZqbProjectManageService.getConfigUUID("dxfxrgformid");
		dxfxrgdemid=dongGuanZqbProjectManageService.getConfigUUID("dxfxrgdemid");
		
		dxfxfkformid=dongGuanZqbProjectManageService.getConfigUUID("dxfxfkformid");
		dxfxfkdemid=dongGuanZqbProjectManageService.getConfigUUID("dxfxfkdemid");
		dxfxjgformid=dongGuanZqbProjectManageService.getConfigUUID("dxfxjgformid");
		dxfxjgdemid=dongGuanZqbProjectManageService.getConfigUUID("dxfxjgdemid");
		dxfxzzformid=dongGuanZqbProjectManageService.getConfigUUID("dxfxzzformid");
		dxfxzzdemid=dongGuanZqbProjectManageService.getConfigUUID("dxfxzzdemid");
		
		dgqtwjformid=dongGuanZqbProjectManageService.getConfigUUID("dgqtwjformid");
		dgqtwjdemid=dongGuanZqbProjectManageService.getConfigUUID("dgqtwjdemid");
		dgxmzjuformid=dongGuanZqbProjectManageService.getConfigUUID("dgxmzjuformid");
		dgxmzjudemid=dongGuanZqbProjectManageService.getConfigUUID("dgxmzjudemid");
		
		content = dongGuanZqbProjectManageService.projectXmjdContent(projectno);
		return SUCCESS;
	}
	
	public String bgczXmjd(){
		bgzzlxformid=dongGuanZqbProjectManageService.getConfigUUID("bgzzlxformid");
		bgzzlxdemid=dongGuanZqbProjectManageService.getConfigUUID("bgzzlxdemid");
		bgzzsbformid=dongGuanZqbProjectManageService.getConfigUUID("bgzzsbformid");
		bgzzsbdemid=dongGuanZqbProjectManageService.getConfigUUID("bgzzsbdemid");
		bgzzfkformid=dongGuanZqbProjectManageService.getConfigUUID("bgzzfkformid");
		bgzzfkdemid=dongGuanZqbProjectManageService.getConfigUUID("bgzzfkdemid");
		bgzzplformid=dongGuanZqbProjectManageService.getConfigUUID("bgzzplformid");
		bgzzpldemid=dongGuanZqbProjectManageService.getConfigUUID("bgzzpldemid");
		bgzzssformid=dongGuanZqbProjectManageService.getConfigUUID("bgzzssformid");
		bgzzssdemid=dongGuanZqbProjectManageService.getConfigUUID("bgzzssdemid");
		bgzzzzformid=dongGuanZqbProjectManageService.getConfigUUID("bgzzzzformid");
		bgzzzzdemid=dongGuanZqbProjectManageService.getConfigUUID("bgzzzzdemid");
		
		bgzzggdemid=dongGuanZqbProjectManageService.getConfigUUID("bgzzggdemid");
		bgzzggformid=dongGuanZqbProjectManageService.getConfigUUID("bgzzggformid");
		bgzzgddemid=dongGuanZqbProjectManageService.getConfigUUID("bgzzgddemid");
		bgzzgdformid=dongGuanZqbProjectManageService.getConfigUUID("bgzzgdformid");
		bgzzcxdemid=dongGuanZqbProjectManageService.getConfigUUID("bgzzcxdemid");
		bgzzcxformid=dongGuanZqbProjectManageService.getConfigUUID("bgzzcxformid");
		
		dgqtwjformid=dongGuanZqbProjectManageService.getConfigUUID("dgqtwjformid");
		dgqtwjdemid=dongGuanZqbProjectManageService.getConfigUUID("dgqtwjdemid");
		dgxmzjuformid=dongGuanZqbProjectManageService.getConfigUUID("dgxmzjuformid");
		dgxmzjudemid=dongGuanZqbProjectManageService.getConfigUUID("dgxmzjudemid");
		
		jdList=dongGuanZqbProjectManageService.getTaskList(projectno);
		return SUCCESS;
	}
	
	public String ipoXmjd(){
		ipolxformid=dongGuanZqbProjectManageService.getConfigUUID("ipolxformid");
		ipolxdemid=dongGuanZqbProjectManageService.getConfigUUID("ipolxdemid");
		
		ipofademid=dongGuanZqbProjectManageService.getConfigUUID("ipofademid");
		ipofaformid=dongGuanZqbProjectManageService.getConfigUUID("ipofaformid");
		ipogddemid=dongGuanZqbProjectManageService.getConfigUUID("ipogddemid");
		ipogdformid=dongGuanZqbProjectManageService.getConfigUUID("ipogdformid");
		ipoqldemid=dongGuanZqbProjectManageService.getConfigUUID("ipoqldemid");
		ipoqlformid=dongGuanZqbProjectManageService.getConfigUUID("ipoqlformid");
		
		ipoggformid=dongGuanZqbProjectManageService.getConfigUUID("ipoggformid");
		ipoggdemid=dongGuanZqbProjectManageService.getConfigUUID("ipoggdemid");
		ipofddemid=dongGuanZqbProjectManageService.getConfigUUID("ipofddemid");
		ipofdformid=dongGuanZqbProjectManageService.getConfigUUID("ipofdformid");
		iposbdemid=dongGuanZqbProjectManageService.getConfigUUID("iposbdemid");
		iposbformid=dongGuanZqbProjectManageService.getConfigUUID("iposbformid");
		ipofkdemid=dongGuanZqbProjectManageService.getConfigUUID("ipofkdemid");
		ipofkformid=dongGuanZqbProjectManageService.getConfigUUID("ipofkformid");
		
		ipohzdemid=dongGuanZqbProjectManageService.getConfigUUID("ipohzdemid");
		ipohzformid=dongGuanZqbProjectManageService.getConfigUUID("ipohzformid");
		ipofjdemid=dongGuanZqbProjectManageService.getConfigUUID("ipofjdemid");
		ipofjformid=dongGuanZqbProjectManageService.getConfigUUID("ipofjformid");
		ipofxssdemid=dongGuanZqbProjectManageService.getConfigUUID("ipofxssdemid");
		ipofxssformid=dongGuanZqbProjectManageService.getConfigUUID("ipofxssformid");
		ipogfssdemid=dongGuanZqbProjectManageService.getConfigUUID("ipogfssdemid");
		ipogfssformid=dongGuanZqbProjectManageService.getConfigUUID("ipogfssformid");
		
		iposhdemid=dongGuanZqbProjectManageService.getConfigUUID("iposhdemid");
		iposhformid=dongGuanZqbProjectManageService.getConfigUUID("iposhformid");
		ipofxdemid=dongGuanZqbProjectManageService.getConfigUUID("ipofxdemid");
		ipofxformid=dongGuanZqbProjectManageService.getConfigUUID("ipofxformid");
		ipossdemid=dongGuanZqbProjectManageService.getConfigUUID("ipossdemid");
		ipossformid=dongGuanZqbProjectManageService.getConfigUUID("ipossformid");
		ipodddemid=dongGuanZqbProjectManageService.getConfigUUID("ipodddemid");
		ipoddformid=dongGuanZqbProjectManageService.getConfigUUID("ipoddformid");
		ipozzdemid=dongGuanZqbProjectManageService.getConfigUUID("ipozzdemid");
		ipozzformid=dongGuanZqbProjectManageService.getConfigUUID("ipozzformid");
		
		dgqtwjformid=dongGuanZqbProjectManageService.getConfigUUID("dgqtwjformid");
		dgqtwjdemid=dongGuanZqbProjectManageService.getConfigUUID("dgqtwjdemid");
		dgxmzjuformid=dongGuanZqbProjectManageService.getConfigUUID("dgxmzjuformid");
		dgxmzjudemid=dongGuanZqbProjectManageService.getConfigUUID("dgxmzjudemid");
		
		jdList=dongGuanZqbProjectManageService.getIpoList(projectno,type);
		return SUCCESS;
	}
	
	public String indexLeaveByUserid(){
		if(pageNumber==0)
			pageNumber=1;
		qjlcServer = ProcessAPI.getInstance().getProcessActDefId("QJLC");
		qjlcList = dongGuanZqbProjectManageService.getQjlcListByUserid(pageSize,pageNumber,username,departmentname,workstatus,userid);
		totalNum = dongGuanZqbProjectManageService.getQjlcListByUseridSize(username,departmentname,workstatus,userid);
		return SUCCESS;
	}
	
	public String indexLeave(){
		if(pageNumber==0)
			pageNumber=1;
		qjlcServer = ProcessAPI.getInstance().getProcessActDefId("QJLC");
		qjlcList = dongGuanZqbProjectManageService.getQjlcList(pageSize,pageNumber,username,departmentname,workstatus);
		totalNum = dongGuanZqbProjectManageService.getQjlcListSize(username,departmentname,workstatus);
		return SUCCESS;
	}
	public String sickLeaveDia(){
		return SUCCESS;
	}
	public void sickLeave(){
		boolean flag = dongGuanZqbProjectManageService.sickLeave(instanceid,sicktime);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	public void sickDel(){
		boolean flag = dongGuanZqbProjectManageService.sickDel(instanceid);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	
	private DongGuanZqbProjectManageService dongGuanZqbProjectManageService;
	private int pageNumber;
	private int pageNumber2;
	private int pageSize = 10;
	private int pageSize2 = 10;
	private int closeNum;
	private List<HashMap> closeList;
	private List<HashMap> runList;
	private int totalNum;
	private String customername;
	private String sssyb;
	private String cyrName;
	private String xmlx;
	private String xmjd;
	private Long demid;
	private Long formid;
	private String gppjdemid;
	private String gppjformid;
	private String dxfxdemid;
	private String dxfxformid;
	private String tjgpbasemsgdemid;
	private String tjgpbasemsgformid;
	private String projectno;
	private String content;
	private String instanceId;
	private String customerno;
	private String pjformid;
	private String pjdemid;
	private String projectname;
	private String dxfxlxformid;
	private String dxfxlxdemid;
	private String dxfxgkformid;
	private String dxfxgkdemid;
	private String dxfxbaformid;
	private String dxfxbademid;
	private String dxfxjgformid;
	private String dxfxjgdemid;
	private String dxfxzzformid;
	private String dxfxzzdemid;
	private String dxfxfkformid;
	private String dxfxfkdemid;
	private List<HashMap<String, Object>> jdList;
	private String bgzzlxformid;
	private String bgzzlxdemid;
	private String bgzzsbformid;
	private String bgzzsbdemid;
	private String bgzzfkformid;
	private String bgzzfkdemid;
	private String bgzzplformid;
	private String bgzzpldemid;
	private String bgzzssformid;
	private String bgzzssdemid;
	private String bgzzzzformid;
	private String bgzzzzdemid;
	private String ipodemid;
	private String ipoformid;
	private String ipolxformid;
	private String ipolxdemid;
	private String ipoggformid;
	private String ipoggdemid;
	private String ipofddemid;
	private String ipofdformid;
	private String iposbdemid;
	private String iposbformid;
	private String ipofkdemid;
	private String ipofkformid;
	private String iposhdemid;
	private String iposhformid;
	private String ipofxdemid;
	private String ipofxformid;
	private String ipossdemid;
	private String ipossformid;
	private String ipodddemid;
	private String ipoddformid;
	private String ipozzdemid;
	private String ipozzformid;
	private String type;
	private String dxfxjyformid;
	private String dxfxjydemid;
	private String dxfxrgformid;
	private String dxfxrgdemid;
	private String bgzzggdemid;
	private String bgzzggformid;
	private String bgzzgddemid;
	private String bgzzgdformid;
	private String bgzzcxdemid;
	private String bgzzcxformid;
	private String ipofademid;
	private String ipofaformid;
	private String ipogddemid;
	private String ipogdformid;
	private String ipoqldemid;
	private String ipoqlformid;
	private String ipohzdemid;
	private String ipohzformid;
	private String ipofjdemid;
	private String ipofjformid;
	private String ipofxssdemid;
	private String ipofxssformid;
	private String ipogfssdemid;
	private String ipogfssformid;
	private String ssxq;
	private String expfields;
	private String ptype;
	private String instanceid;
	private String qjlcServer;
	private List<HashMap> qjlcList;
	private String username;
	private String departmentname;
	private String workstatus;
	private String sicktime;
	private String userid;
	private String dgqtwjformid;
	private String dgqtwjdemid;
	private String dgxmzjuformid;
	private String dgxmzjudemid;
	
	public String getDgxmzjuformid() {
		return dgxmzjuformid;
	}
	public void setDgxmzjuformid(String dgxmzjuformid) {
		this.dgxmzjuformid = dgxmzjuformid;
	}
	public String getDgxmzjudemid() {
		return dgxmzjudemid;
	}
	public void setDgxmzjudemid(String dgxmzjudemid) {
		this.dgxmzjudemid = dgxmzjudemid;
	}
	public String getDgqtwjformid() {
		return dgqtwjformid;
	}
	public void setDgqtwjformid(String dgqtwjformid) {
		this.dgqtwjformid = dgqtwjformid;
	}
	public String getDgqtwjdemid() {
		return dgqtwjdemid;
	}
	public void setDgqtwjdemid(String dgqtwjdemid) {
		this.dgqtwjdemid = dgqtwjdemid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSicktime() {
		return sicktime;
	}
	public void setSicktime(String sicktime) {
		this.sicktime = sicktime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDepartmentname() {
		return departmentname;
	}
	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}
	public String getWorkstatus() {
		return workstatus;
	}
	public void setWorkstatus(String workstatus) {
		this.workstatus = workstatus;
	}
	public List<HashMap> getQjlcList() {
		return qjlcList;
	}
	public void setQjlcList(List<HashMap> qjlcList) {
		this.qjlcList = qjlcList;
	}
	public String getQjlcServer() {
		return qjlcServer;
	}
	public void setQjlcServer(String qjlcServer) {
		this.qjlcServer = qjlcServer;
	}
	public String getInstanceid() {
		return instanceid;
	}
	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}
	public String getExpfields() {
		return expfields;
	}
	public void setExpfields(String expfields) {
		this.expfields = expfields;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	public String getSsxq() {
		return ssxq;
	}
	public void setSsxq(String ssxq) {
		this.ssxq = ssxq;
	}
	public String getIpohzdemid() {
		return ipohzdemid;
	}
	public void setIpohzdemid(String ipohzdemid) {
		this.ipohzdemid = ipohzdemid;
	}
	public String getIpohzformid() {
		return ipohzformid;
	}
	public void setIpohzformid(String ipohzformid) {
		this.ipohzformid = ipohzformid;
	}
	public String getIpofjdemid() {
		return ipofjdemid;
	}
	public void setIpofjdemid(String ipofjdemid) {
		this.ipofjdemid = ipofjdemid;
	}
	public String getIpofjformid() {
		return ipofjformid;
	}
	public void setIpofjformid(String ipofjformid) {
		this.ipofjformid = ipofjformid;
	}
	public String getIpofxssdemid() {
		return ipofxssdemid;
	}
	public void setIpofxssdemid(String ipofxssdemid) {
		this.ipofxssdemid = ipofxssdemid;
	}
	public String getIpofxssformid() {
		return ipofxssformid;
	}
	public void setIpofxssformid(String ipofxssformid) {
		this.ipofxssformid = ipofxssformid;
	}
	public String getIpogfssdemid() {
		return ipogfssdemid;
	}
	public void setIpogfssdemid(String ipogfssdemid) {
		this.ipogfssdemid = ipogfssdemid;
	}
	public String getIpogfssformid() {
		return ipogfssformid;
	}
	public void setIpogfssformid(String ipogfssformid) {
		this.ipogfssformid = ipogfssformid;
	}
	public String getIpoqldemid() {
		return ipoqldemid;
	}
	public void setIpoqldemid(String ipoqldemid) {
		this.ipoqldemid = ipoqldemid;
	}
	public String getIpoqlformid() {
		return ipoqlformid;
	}
	public void setIpoqlformid(String ipoqlformid) {
		this.ipoqlformid = ipoqlformid;
	}
	public String getIpogddemid() {
		return ipogddemid;
	}
	public void setIpogddemid(String ipogddemid) {
		this.ipogddemid = ipogddemid;
	}
	public String getIpogdformid() {
		return ipogdformid;
	}
	public void setIpogdformid(String ipogdformid) {
		this.ipogdformid = ipogdformid;
	}
	public String getIpofademid() {
		return ipofademid;
	}
	public void setIpofademid(String ipofademid) {
		this.ipofademid = ipofademid;
	}
	public String getIpofaformid() {
		return ipofaformid;
	}
	public void setIpofaformid(String ipofaformid) {
		this.ipofaformid = ipofaformid;
	}
	public String getBgzzggdemid() {
		return bgzzggdemid;
	}
	public void setBgzzggdemid(String bgzzggdemid) {
		this.bgzzggdemid = bgzzggdemid;
	}
	public String getBgzzggformid() {
		return bgzzggformid;
	}
	public void setBgzzggformid(String bgzzggformid) {
		this.bgzzggformid = bgzzggformid;
	}
	public String getBgzzgddemid() {
		return bgzzgddemid;
	}
	public void setBgzzgddemid(String bgzzgddemid) {
		this.bgzzgddemid = bgzzgddemid;
	}
	public String getBgzzgdformid() {
		return bgzzgdformid;
	}
	public void setBgzzgdformid(String bgzzgdformid) {
		this.bgzzgdformid = bgzzgdformid;
	}
	public String getBgzzcxdemid() {
		return bgzzcxdemid;
	}
	public void setBgzzcxdemid(String bgzzcxdemid) {
		this.bgzzcxdemid = bgzzcxdemid;
	}
	public String getBgzzcxformid() {
		return bgzzcxformid;
	}
	public void setBgzzcxformid(String bgzzcxformid) {
		this.bgzzcxformid = bgzzcxformid;
	}
	public String getDxfxjyformid() {
		return dxfxjyformid;
	}
	public void setDxfxjyformid(String dxfxjyformid) {
		this.dxfxjyformid = dxfxjyformid;
	}
	public String getDxfxjydemid() {
		return dxfxjydemid;
	}
	public void setDxfxjydemid(String dxfxjydemid) {
		this.dxfxjydemid = dxfxjydemid;
	}
	public String getDxfxrgformid() {
		return dxfxrgformid;
	}
	public void setDxfxrgformid(String dxfxrgformid) {
		this.dxfxrgformid = dxfxrgformid;
	}
	public String getDxfxrgdemid() {
		return dxfxrgdemid;
	}
	public void setDxfxrgdemid(String dxfxrgdemid) {
		this.dxfxrgdemid = dxfxrgdemid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIpofddemid() {
		return ipofddemid;
	}
	public void setIpofddemid(String ipofddemid) {
		this.ipofddemid = ipofddemid;
	}
	public String getIpofdformid() {
		return ipofdformid;
	}
	public void setIpofdformid(String ipofdformid) {
		this.ipofdformid = ipofdformid;
	}
	public String getIposbdemid() {
		return iposbdemid;
	}
	public void setIposbdemid(String iposbdemid) {
		this.iposbdemid = iposbdemid;
	}
	public String getIposbformid() {
		return iposbformid;
	}
	public void setIposbformid(String iposbformid) {
		this.iposbformid = iposbformid;
	}
	public String getIpofkdemid() {
		return ipofkdemid;
	}
	public void setIpofkdemid(String ipofkdemid) {
		this.ipofkdemid = ipofkdemid;
	}
	public String getIpofkformid() {
		return ipofkformid;
	}
	public void setIpofkformid(String ipofkformid) {
		this.ipofkformid = ipofkformid;
	}
	public String getIposhdemid() {
		return iposhdemid;
	}
	public void setIposhdemid(String iposhdemid) {
		this.iposhdemid = iposhdemid;
	}
	public String getIposhformid() {
		return iposhformid;
	}
	public void setIposhformid(String iposhformid) {
		this.iposhformid = iposhformid;
	}
	public String getIpofxdemid() {
		return ipofxdemid;
	}
	public void setIpofxdemid(String ipofxdemid) {
		this.ipofxdemid = ipofxdemid;
	}
	public String getIpofxformid() {
		return ipofxformid;
	}
	public void setIpofxformid(String ipofxformid) {
		this.ipofxformid = ipofxformid;
	}
	public String getIpossdemid() {
		return ipossdemid;
	}
	public void setIpossdemid(String ipossdemid) {
		this.ipossdemid = ipossdemid;
	}
	public String getIpossformid() {
		return ipossformid;
	}
	public void setIpossformid(String ipossformid) {
		this.ipossformid = ipossformid;
	}
	public String getIpodddemid() {
		return ipodddemid;
	}
	public void setIpodddemid(String ipodddemid) {
		this.ipodddemid = ipodddemid;
	}
	public String getIpoddformid() {
		return ipoddformid;
	}
	public void setIpoddformid(String ipoddformid) {
		this.ipoddformid = ipoddformid;
	}
	public String getIpozzdemid() {
		return ipozzdemid;
	}
	public void setIpozzdemid(String ipozzdemid) {
		this.ipozzdemid = ipozzdemid;
	}
	public String getIpozzformid() {
		return ipozzformid;
	}
	public void setIpozzformid(String ipozzformid) {
		this.ipozzformid = ipozzformid;
	}
	public String getIpoggformid() {
		return ipoggformid;
	}
	public void setIpoggformid(String ipoggformid) {
		this.ipoggformid = ipoggformid;
	}
	public String getIpoggdemid() {
		return ipoggdemid;
	}
	public void setIpoggdemid(String ipoggdemid) {
		this.ipoggdemid = ipoggdemid;
	}
	public String getIpolxformid() {
		return ipolxformid;
	}
	public void setIpolxformid(String ipolxformid) {
		this.ipolxformid = ipolxformid;
	}
	public String getIpolxdemid() {
		return ipolxdemid;
	}
	public void setIpolxdemid(String ipolxdemid) {
		this.ipolxdemid = ipolxdemid;
	}
	public String getIpodemid() {
		return ipodemid;
	}
	public void setIpodemid(String ipodemid) {
		this.ipodemid = ipodemid;
	}
	public String getIpoformid() {
		return ipoformid;
	}
	public void setIpoformid(String ipoformid) {
		this.ipoformid = ipoformid;
	}
	public String getBgzzzzformid() {
		return bgzzzzformid;
	}
	public void setBgzzzzformid(String bgzzzzformid) {
		this.bgzzzzformid = bgzzzzformid;
	}
	public String getBgzzzzdemid() {
		return bgzzzzdemid;
	}
	public void setBgzzzzdemid(String bgzzzzdemid) {
		this.bgzzzzdemid = bgzzzzdemid;
	}
	public String getBgzzssformid() {
		return bgzzssformid;
	}
	public void setBgzzssformid(String bgzzssformid) {
		this.bgzzssformid = bgzzssformid;
	}
	public String getBgzzssdemid() {
		return bgzzssdemid;
	}
	public void setBgzzssdemid(String bgzzssdemid) {
		this.bgzzssdemid = bgzzssdemid;
	}
	public String getBgzzplformid() {
		return bgzzplformid;
	}
	public void setBgzzplformid(String bgzzplformid) {
		this.bgzzplformid = bgzzplformid;
	}
	public String getBgzzpldemid() {
		return bgzzpldemid;
	}
	public void setBgzzpldemid(String bgzzpldemid) {
		this.bgzzpldemid = bgzzpldemid;
	}
	public String getBgzzfkformid() {
		return bgzzfkformid;
	}
	public void setBgzzfkformid(String bgzzfkformid) {
		this.bgzzfkformid = bgzzfkformid;
	}
	public String getBgzzfkdemid() {
		return bgzzfkdemid;
	}
	public void setBgzzfkdemid(String bgzzfkdemid) {
		this.bgzzfkdemid = bgzzfkdemid;
	}
	public String getBgzzsbformid() {
		return bgzzsbformid;
	}
	public void setBgzzsbformid(String bgzzsbformid) {
		this.bgzzsbformid = bgzzsbformid;
	}
	public String getBgzzsbdemid() {
		return bgzzsbdemid;
	}
	public void setBgzzsbdemid(String bgzzsbdemid) {
		this.bgzzsbdemid = bgzzsbdemid;
	}
	public String getBgzzlxformid() {
		return bgzzlxformid;
	}
	public void setBgzzlxformid(String bgzzlxformid) {
		this.bgzzlxformid = bgzzlxformid;
	}
	public String getBgzzlxdemid() {
		return bgzzlxdemid;
	}
	public void setBgzzlxdemid(String bgzzlxdemid) {
		this.bgzzlxdemid = bgzzlxdemid;
	}
	public List<HashMap<String, Object>> getJdList() {
		return jdList;
	}
	public void setJdList(List<HashMap<String, Object>> jdList) {
		this.jdList = jdList;
	}
	public String getDxfxfkformid() {
		return dxfxfkformid;
	}
	public void setDxfxfkformid(String dxfxfkformid) {
		this.dxfxfkformid = dxfxfkformid;
	}
	public String getDxfxfkdemid() {
		return dxfxfkdemid;
	}
	public void setDxfxfkdemid(String dxfxfkdemid) {
		this.dxfxfkdemid = dxfxfkdemid;
	}
	public String getDxfxzzformid() {
		return dxfxzzformid;
	}
	public void setDxfxzzformid(String dxfxzzformid) {
		this.dxfxzzformid = dxfxzzformid;
	}
	public String getDxfxzzdemid() {
		return dxfxzzdemid;
	}
	public void setDxfxzzdemid(String dxfxzzdemid) {
		this.dxfxzzdemid = dxfxzzdemid;
	}
	public String getDxfxjgformid() {
		return dxfxjgformid;
	}
	public void setDxfxjgformid(String dxfxjgformid) {
		this.dxfxjgformid = dxfxjgformid;
	}
	public String getDxfxjgdemid() {
		return dxfxjgdemid;
	}
	public void setDxfxjgdemid(String dxfxjgdemid) {
		this.dxfxjgdemid = dxfxjgdemid;
	}
	public String getDxfxbaformid() {
		return dxfxbaformid;
	}
	public void setDxfxbaformid(String dxfxbaformid) {
		this.dxfxbaformid = dxfxbaformid;
	}
	public String getDxfxbademid() {
		return dxfxbademid;
	}
	public void setDxfxbademid(String dxfxbademid) {
		this.dxfxbademid = dxfxbademid;
	}
	public String getDxfxgkformid() {
		return dxfxgkformid;
	}
	public void setDxfxgkformid(String dxfxgkformid) {
		this.dxfxgkformid = dxfxgkformid;
	}
	public String getDxfxgkdemid() {
		return dxfxgkdemid;
	}
	public void setDxfxgkdemid(String dxfxgkdemid) {
		this.dxfxgkdemid = dxfxgkdemid;
	}
	public String getDxfxlxformid() {
		return dxfxlxformid;
	}
	public void setDxfxlxformid(String dxfxlxformid) {
		this.dxfxlxformid = dxfxlxformid;
	}
	public String getDxfxlxdemid() {
		return dxfxlxdemid;
	}
	public void setDxfxlxdemid(String dxfxlxdemid) {
		this.dxfxlxdemid = dxfxlxdemid;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getPjformid() {
		return pjformid;
	}
	public void setPjformid(String pjformid) {
		this.pjformid = pjformid;
	}
	public String getPjdemid() {
		return pjdemid;
	}
	public void setPjdemid(String pjdemid) {
		this.pjdemid = pjdemid;
	}
	public String getCustomerno() {
		return customerno;
	}
	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getProjectno() {
		return projectno;
	}
	public void setProjectno(String projectno) {
		this.projectno = projectno;
	}
	public String getTjgpbasemsgdemid() {
		return tjgpbasemsgdemid;
	}

	public void setTjgpbasemsgdemid(String tjgpbasemsgdemid) {
		this.tjgpbasemsgdemid = tjgpbasemsgdemid;
	}

	public String getTjgpbasemsgformid() {
		return tjgpbasemsgformid;
	}

	public void setTjgpbasemsgformid(String tjgpbasemsgformid) {
		this.tjgpbasemsgformid = tjgpbasemsgformid;
	}

	public Long getDemid() {
		return demid;
	}

	public void setDemid(Long demid) {
		this.demid = demid;
	}

	public Long getFormid() {
		return formid;
	}

	public void setFormid(Long formid) {
		this.formid = formid;
	}

	public String getGppjdemid() {
		return gppjdemid;
	}

	public void setGppjdemid(String gppjdemid) {
		this.gppjdemid = gppjdemid;
	}

	public String getGppjformid() {
		return gppjformid;
	}

	public void setGppjformid(String gppjformid) {
		this.gppjformid = gppjformid;
	}

	public String getDxfxdemid() {
		return dxfxdemid;
	}

	public void setDxfxdemid(String dxfxdemid) {
		this.dxfxdemid = dxfxdemid;
	}

	public String getDxfxformid() {
		return dxfxformid;
	}

	public void setDxfxformid(String dxfxformid) {
		this.dxfxformid = dxfxformid;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getSssyb() {
		return sssyb;
	}

	public void setSssyb(String sssyb) {
		this.sssyb = sssyb;
	}

	public String getCyrName() {
		return cyrName;
	}

	public void setCyrName(String cyrName) {
		this.cyrName = cyrName;
	}

	public String getXmjd() {
		return xmjd;
	}
	
	public void setXmjd(String xmjd) {
		this.xmjd = xmjd;
	}
	public String getXmlx() {
		return xmlx;
	}

	public void setXmlx(String xmlx) {
		this.xmlx = xmlx;
	}

	public List<HashMap> getRunList() {
		return runList;
	}

	public void setRunList(List<HashMap> runList) {
		this.runList = runList;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public List<HashMap> getCloseList() {
		return closeList;
	}

	public void setCloseList(List<HashMap> closeList) {
		this.closeList = closeList;
	}

	public int getCloseNum() {
		return closeNum;
	}

	public void setCloseNum(int closeNum) {
		this.closeNum = closeNum;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageNumber2() {
		return pageNumber2;
	}

	public void setPageNumber2(int pageNumber2) {
		this.pageNumber2 = pageNumber2;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize2() {
		return pageSize2;
	}

	public void setPageSize2(int pageSize2) {
		this.pageSize2 = pageSize2;
	}

	public void setDongGuanZqbProjectManageService(
			DongGuanZqbProjectManageService dongGuanZqbProjectManageService) {
		this.dongGuanZqbProjectManageService = dongGuanZqbProjectManageService;
	}
	private String taskTitle;
	private String entrusetUserid;
	private String entrusetTime;
	
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getEntrusetUserid() {
		return entrusetUserid;
	}
	public void setEntrusetUserid(String entrusetUserid) {
		this.entrusetUserid = entrusetUserid;
	}
	public String getEntrusetTime() {
		return entrusetTime;
	}
	public void setEntrusetTime(String entrusetTime) {
		this.entrusetTime = entrusetTime;
	}
	public void getEntrust() throws IOException{
		String description="["+entrusetUserid+"发送委托]"+taskTitle;
		List list=new ArrayList();
		
		list.add("LCBH");
		list.add("LCBS");
		list.add("STEPID");
		list.add("TASKID");
		Map params=new HashMap();
		params.put(1,description);
		params.put(2,entrusetTime);
		String sql="SELECT  HT.ID_ TASKID,HT.PROC_DEF_ID_ LCBH,HT.TASK_DEF_KEY_ STEPID,HT.EXECUTION_ID_ LCBS   FROM"
				+ "(SELECT TO_CHAR(END_TIME_,'yyyy-MM-dd HH24:mi:ss') END_TIME_1,H.*  FROM PROCESS_HI_TASKINST H) HT WHERE HT.END_TIME_1="
				+ "(SELECT MAX(TO_CHAR(END_TIME_,'yyyy-MM-dd HH24:mi:ss')) FROM PROCESS_HI_TASKINST M WHERE EXECUTION_ID_="
				+ "(SELECT EXECUTION_ID_ FROM (SELECT TO_CHAR(START_TIME_,'yyyy-MM-dd HH24:mi:ss') START_TIME_1, P.* FROM PROCESS_HI_TASKINST P) PT WHERE PT.DESCRIPTION_ =? AND PT.START_TIME_1=?))";
		List<HashMap> dataList = DBUTilNew.getDataList(list, sql, params);
		String json = Json.toJson(dataList);
		HttpServletResponse resp = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out=resp.getWriter();
        out.print(json);
        out.close();
		
	}
}
