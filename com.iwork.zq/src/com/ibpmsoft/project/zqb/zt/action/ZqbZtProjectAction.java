package com.ibpmsoft.project.zqb.zt.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.activiti.engine.task.Task;
import com.ibpmsoft.project.zqb.zt.service.ZqbZtProjectService;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbZtProjectAction extends ActionSupport{
	
	
	private String nbcblc;
	private Long id;
	private Long instanceid;
	private Long taskid;
	private String typePieData;
	private String typePieData2;
	private String typePieData3;
	private String typeBarData;
	private String typeBarLabel;
	private boolean readonly;
	private boolean superman;
	private int year; 
	private String meettype;
	private String status;
	private String grouptype;
	private String content;
	private String filterbar;
	private String userid;
	private String username;
	private String customerno;
	private String opreatorType;
	private Date meettime;
	private int jc;
	private String jydxbh;
	private String customername;
	private String cid;
	private String did;
	private Long orgRoleId;
	private String zqdm;
	private String zqjc;
	private String noticename;
	private String startdate;
	private String enddate;
	private String spzt;
	private List<HashMap> runList;
	private List<HashMap> finishList;
	private List<HashMap> talkList;
	private List<HashMap> customerList;
	private HashMap hash;
	private String projectNo;
	private String json;
	private int pageNumber; // 当前页数
	private String zInstanceId;
	private int totalNum; // 总页数
	private int pageSize = 10; // 每页条数
	private ZqbZtProjectService zqbZtProjectService;
	public String index(){
		nbcblc = ProcessAPI.getInstance().getProcessActDefId("NBCBLC");
		
		//获取当前登录用户角色id
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		orgRoleId = uc._userModel.getOrgroleid();
		if(orgRoleId==3l)
			customerno=uc._userModel.getExtend1();
		String userid = uc._userModel.getUserid();
		if (pageNumber == 0)
			pageNumber = 1;
		runList = zqbZtProjectService.getMeetRunList(pageSize, pageNumber,userid,customerno,orgRoleId,zqdm,zqjc,startdate,enddate,noticename,spzt);
		totalNum=zqbZtProjectService.getEventRunListSize(userid,customerno,orgRoleId,zqdm,zqjc,startdate,enddate,noticename,spzt);
		readonly=false;
		return SUCCESS;
	}
//	public String indexEvent(){
//		nbcblc = ProcessAPI.getInstance().getProcessActDefId("NBCBLC");
//		if(year==0)year = UtilDate.getYear(new Date());
//		if(status==null){status="全部";}
//		if(meettype==null){meettype="全部";}
//		if(grouptype==null){grouptype="全部";}
//		//获取当前登录用户角色id
//		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
//		orgRoleId = uc._userModel.getOrgroleid();
//		if(orgRoleId==3l)
//			customerno=uc._userModel.getExtend1();
//		String userid = uc._userModel.getUserid();
//		if (pageNumber == 0)
//			pageNumber = 1;
//		runList = zqbZtProjectService.getMeetRunList(pageSize, pageNumber,userid,customerno,orgRoleId,zqdm,zqjc,startdate,enddate,noticename,spzt);
//		totalNum=zqbZtProjectService.getEventRunListSize(userid,customerno,orgRoleId,zqdm,zqjc,startdate,enddate,noticename,spzt);
//		readonly=false;
//		return SUCCESS;
//	}
	
	
	public void removeZtNbcb(){
		if(instanceid!=null){ 
			
			HashMap fromData=ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if(fromData!=null){
				
				String spzt = fromData.get("NODE9SFKF")==null?"":fromData.get("NODE9SFKF").toString();
					String yxid = fromData.get("NODE4SFKF")==null?"":fromData.get("NODE4SFKF").toString();
					String owner = fromData.get("NODE7SFKF")==null?"":fromData.get("NODE7SFKF").toString();
					Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(yxid));			
					if(!"".equals(yxid)){
						
//						if(newTaskId!=null){
//							String taskId = newTaskId.getId();
//							String owner = newTaskId.getOwner();
							
							delptask(yxid);
							boolean removeFormData = ProcessAPI.getInstance().removeFormData(instanceid);
							boolean removeProcessInstance = ProcessAPI.getInstance().removeProcessInstance(yxid, owner);
							
							if(removeFormData){
								ResponseUtil.write(SUCCESS);
							}
//						}
					}
			
			}
		
			
		}else{
			ResponseUtil.write(ERROR);
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
//	public void removeZtNbcb(){
//		if(instanceid!=null){ 
//		HashMap fromData=ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
//		String spzt = fromData.get("NODE9SFKF")==null?"":fromData.get("NODE9SFKF").toString();
//		if("未提交".equals(spzt)||"审批通过".equals(spzt)){
//	
//		
//			String  flag = zqbZtProjectService.removeZtNbcb(instanceid);
//			ResponseUtil.write(flag);
//		}
//		}
//	}
	public String getNbcblc() {
		return nbcblc;
	}
	public void setNbcblc(String nbcblc) {
		this.nbcblc = nbcblc;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getInstanceid() {
		return instanceid;
	}
	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}
	public Long getTaskid() {
		return taskid;
	}
	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}
	public String getTypePieData() {
		return typePieData;
	}
	public void setTypePieData(String typePieData) {
		this.typePieData = typePieData;
	}
	public String getTypePieData2() {
		return typePieData2;
	}
	public void setTypePieData2(String typePieData2) {
		this.typePieData2 = typePieData2;
	}
	public String getTypePieData3() {
		return typePieData3;
	}
	public void setTypePieData3(String typePieData3) {
		this.typePieData3 = typePieData3;
	}
	public String getTypeBarData() {
		return typeBarData;
	}
	public void setTypeBarData(String typeBarData) {
		this.typeBarData = typeBarData;
	}
	public String getTypeBarLabel() {
		return typeBarLabel;
	}
	public void setTypeBarLabel(String typeBarLabel) {
		this.typeBarLabel = typeBarLabel;
	}
	public boolean isReadonly() {
		return readonly;
	}
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	public boolean isSuperman() {
		return superman;
	}
	public void setSuperman(boolean superman) {
		this.superman = superman;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getMeettype() {
		return meettype;
	}
	public void setMeettype(String meettype) {
		this.meettype = meettype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGrouptype() {
		return grouptype;
	}
	public void setGrouptype(String grouptype) {
		this.grouptype = grouptype;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFilterbar() {
		return filterbar;
	}
	public void setFilterbar(String filterbar) {
		this.filterbar = filterbar;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCustomerno() {
		return customerno;
	}
	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}
	public String getOpreatorType() {
		return opreatorType;
	}
	public void setOpreatorType(String opreatorType) {
		this.opreatorType = opreatorType;
	}
	public Date getMeettime() {
		return meettime;
	}
	public void setMeettime(Date meettime) {
		this.meettime = meettime;
	}
	public int getJc() {
		return jc;
	}
	public void setJc(int jc) {
		this.jc = jc;
	}
	public String getJydxbh() {
		return jydxbh;
	}
	public void setJydxbh(String jydxbh) {
		this.jydxbh = jydxbh;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public Long getOrgRoleId() {
		return orgRoleId;
	}
	public void setOrgRoleId(Long orgRoleId) {
		this.orgRoleId = orgRoleId;
	}
	public String getZqdm() {
		return zqdm;
	}
	public void setZqdm(String zqdm) {
		this.zqdm = zqdm;
	}
	public String getZqjc() {
		return zqjc;
	}
	public void setZqjc(String zqjc) {
		this.zqjc = zqjc;
	}
	public String getNoticename() {
		return noticename;
	}
	public void setNoticename(String noticename) {
		this.noticename = noticename;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getSpzt() {
		return spzt;
	}
	public void setSpzt(String spzt) {
		this.spzt = spzt;
	}
	public List<HashMap> getRunList() {
		return runList;
	}
	public void setRunList(List<HashMap> runList) {
		this.runList = runList;
	}
	public List<HashMap> getFinishList() {
		return finishList;
	}
	public void setFinishList(List<HashMap> finishList) {
		this.finishList = finishList;
	}
	public List<HashMap> getTalkList() {
		return talkList;
	}
	public void setTalkList(List<HashMap> talkList) {
		this.talkList = talkList;
	}
	public List<HashMap> getCustomerList() {
		return customerList;
	}
	public void setCustomerList(List<HashMap> customerList) {
		this.customerList = customerList;
	}
	public HashMap getHash() {
		return hash;
	}
	public void setHash(HashMap hash) {
		this.hash = hash;
	}
	public String getProjectNo() {
		return projectNo;
	}
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getzInstanceId() {
		return zInstanceId;
	}
	public void setzInstanceId(String zInstanceId) {
		this.zInstanceId = zInstanceId;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public ZqbZtProjectService getZqbZtProjectService() {
		return zqbZtProjectService;
	}
	public void setZqbZtProjectService(ZqbZtProjectService zqbZtProjectService) {
		this.zqbZtProjectService = zqbZtProjectService;
	}
	
}
