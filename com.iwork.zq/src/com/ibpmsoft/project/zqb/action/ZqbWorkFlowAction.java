package com.ibpmsoft.project.zqb.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import org.activiti.engine.task.Task;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.service.ZqbWorkFlowService;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbWorkFlowAction extends ActionSupport {
	private ZqbWorkFlowService zqbWorkFlowService;
	private List<HashMap> runList;
	private Long instanceid;
	private String json ;
	private String tabHtml ;
	private String title ;
	
	private int pageNumber; // 当前页数
	private int totalNum; // 总页数
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	private int pageSize = 10; // 每页条数
	
	public String index() throws Exception {
		//runList = zqbWorkFlowService.getCurrentFolwList();
		if (pageNumber == 0)
			pageNumber = 1;
		runList=zqbWorkFlowService.getCurrentFolwList(pageSize, pageNumber);
		totalNum=zqbWorkFlowService.getCurrentFolwListSize();/*runList.size();*/
		return SUCCESS; 
	} 
	public String showDesigner(){
		if(instanceid!=null){
			HashMap hash =  DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			json = zqbWorkFlowService.getFlowJson(hash);
			if(hash!=null&&hash.get("SWMS")!=null){
				title = hash.get("SWMS").toString();
			}else{
				title = "未命名";
			}
		}
		if(json==null||json.equals("")){
			json="{}";
		}
		return SUCCESS; 
	}
	
	public String flowMap(){
		
		return SUCCESS; 
	}
	
	public String showVisit(){
		if(instanceid!=null){
			HashMap hash =  DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			json = zqbWorkFlowService.getFlowJson(hash);
			if(hash!=null&&hash.get("SWMS")!=null){
				title = hash.get("SWMS").toString();
			}else{
				title = "未命名";
			}
		}
		if(json==null||json.equals("")){
			json="{}";
		}
		return SUCCESS; 
	}
	
	public void showTreeJson(){
		String json = zqbWorkFlowService.getTreeJson1();
		ResponseUtil.write(json);
	}
	
	/**
	 * 
	 */
	public void saveDesigner(){
		if(instanceid!=null&&json!=null){
			boolean flag  = zqbWorkFlowService.saveFlowJson(instanceid, json);
			if(flag){
				ResponseUtil.write(SUCCESS);
			}else{
				ResponseUtil.write(ERROR);
			}
		}else{
			ResponseUtil.write(ERROR);
		}
	}
	
	public void removeDesigner(){
		if(instanceid!=null){ 
			Long formId = DemAPI.getInstance().getFormIdByInstanceId(
					new Long(instanceid), EngineConstants.SYS_INSTANCE_TYPE_DEM);
			HashMap hash = DemAPI.getInstance().getFromData(instanceid, formId,
					EngineConstants.SYS_INSTANCE_TYPE_DEM);
			String smsContent = "";
			String sysMsgContent = "";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String value=hash.get("SXMC").toString();
			Long dataId=Long.parseLong(hash.get("ID").toString());
			if(hash!=null){
			    smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.EVENT_REMOVE_KEY, hash); 
				sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.EVENT_REMOVE_KEY, hash);
				LogUtil.getInstance().addLog(dataId, "信披基本信息维护", "删除重大事项："+value);
			}
			String customerno = "";
			if(hash.get("KHBH")!=null){
				customerno = hash.get("KHBH").toString();
			}
			String useraddress = ZQBNoticeUtil.getInstance().getDuDaoCustomer(customerno);
			String userid = UserContextUtil.getInstance().getUserId(useraddress);
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			UserContext target = UserContextUtil.getInstance().getUserContext(userid); 
			if(target!=null){
				if(!smsContent.equals("")){
					String mobile = target.get_userModel().getMobile();
					if(mobile!=null&&!mobile.equals("")){
						MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
					}
				}
				if(!sysMsgContent.equals("")){ 
						MessageAPI.getInstance().sendSysMsg(userid, "重大事项删除提醒", sysMsgContent);
				}
			}
			
			HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
			String yxid = fromData.get("YXID").toString();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(yxid));			
			if(newTaskId!=null){
				String taskId = newTaskId.getId();
				String owner = newTaskId.getOwner();
				delptask(taskId);
				boolean flag =  DemAPI.getInstance().removeFormData(instanceid);
				boolean removeProcessInstance = ProcessAPI.getInstance().removeProcessInstance(taskId, owner);
			}
			DemAPI.getInstance().removeFormData(instanceid);
			delrcyecb(yxid);
			ResponseUtil.write(SUCCESS);
		}else{
			ResponseUtil.write(ERROR);
		}
		
	}
	private void delrcyecb(String yxid){
		Connection conn = null; 
		PreparedStatement stat = null;
		String sql="DELETE FROM RCYWCB where instanceid= ?"; 
		try {
			conn = DBUtil.open(); 
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, Integer.parseInt(yxid));
			int i=stat.executeUpdate(); 
		} catch (Exception e) {
			System.out.print(e);
		}finally {
			DBUtil.close(conn, stat, null);
		}
	}
	private void delptask(String yxid){
		Connection conn = null; 
		PreparedStatement stat = null;
		String sql="DELETE FROM process_ru_task where ID_= ? "; 
		try {
			conn = DBUtil.open(); 
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, Integer.parseInt(yxid));
			int i=stat.executeUpdate(); 
		} catch (Exception e) {
		}finally {
			DBUtil.close(conn, stat, null);
		}
	}
	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public List<HashMap> getRunList() {
		return runList;
	}
	
	public void setZqbWorkFlowService(ZqbWorkFlowService zqbWorkFlowService) {
		this.zqbWorkFlowService = zqbWorkFlowService;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getTabHtml() {
		return tabHtml;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
