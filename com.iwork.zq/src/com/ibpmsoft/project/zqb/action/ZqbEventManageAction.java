package com.ibpmsoft.project.zqb.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.sf.json.JSONArray;
import com.ibpmsoft.project.zqb.service.ZqbEventManageService;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.meeting.util.UtilDate;
import com.iwork.sdk.DemAPI;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbEventManageAction extends ActionSupport {
	private ZqbEventManageService zqbEventManageService;
	private List<HashMap> runList;
	private List<HashMap> finishList;
	private List<HashMap> talkList;
	private List<HashMap> customerList;
	private HashMap hash;
	private String projectNo;
	private String json;
	private int pageNumber; // 当前页数
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	private int totalNum; // 总页数
	private int pageSize = 10; // 每页条数
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	private Long id;
	private Long instanceid;
	private Long taskid;
	private String startDate;
	private String endDate;
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

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	private String jydxmc;	 
	private String type;
	public String getJydxmc() {
		return jydxmc;
	}

	public void setJydxmc(String jydxmc) {
		this.jydxmc = jydxmc;
	}

	public String getJydxbh() {
		return jydxbh;
	}

	public void setJydxbh(String jydxbh) {
		this.jydxbh = jydxbh;
	}

	public String index(){
		if(year==0)year = UtilDate.getYear(new Date());
		if(status==null){status="全部";}
		if(meettype==null){meettype="全部";}
		if(grouptype==null){grouptype="全部";}
		//获取客户列表
//		customerList = zqbEventManageService.getCurrentCustomerList();
//		if(customerList==null||customerList.size()==0){
//			OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
////			if(orgUser.getUsertype().equals(new Long(2))){
//				customerno = orgUser.getExtend1();
////			} 
//		}
		if(customerno!=null){
			runList = zqbEventManageService.getMeetRunList(customerno,year, meettype, grouptype, status,pageSize,pageNumber);
			totalNum=zqbEventManageService.getEventRunListSize(customerno, pageSize,pageNumber);
//			filterbar = zqbEventManageService.getMeetFilterBar(year, meettype, grouptype, status);
		}
		readonly=false;
//		String userid=UserContextUtil.getInstance().getCurrentUserId();
//		if(!zqbEventManageService.getChartBarLabelListFlag(userid)){
//			readonly=true;
//		}
		return SUCCESS; 
	}

	public void search(){
		String json = zqbEventManageService.geGslx(jydxbh,jydxmc);
		ResponseUtil.write(json);
	}
	
	/**
	 * 讨论页面
	 * @return
	 */
	public String talkIndex(){
		if(instanceid!=null){
			userid = UserContextUtil.getInstance().getCurrentUserId();
			username = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUsername();
			hash = DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			talkList = zqbEventManageService.getTalkList(instanceid);
		}
		return SUCCESS;
	}
	
	public void talkCommit(){
		if(content!=null&&instanceid!=null){ 
			zqbEventManageService.saveTalk(instanceid+"", content);
		}
		
	}
	
	public String charIndex(){
		typeBarData = zqbEventManageService.getChartBarData();
		List<String> list =zqbEventManageService.getChartBarLabelList();
		typeBarLabel = JSONArray.fromObject(list).toString();
		return SUCCESS;
	}
	public String gridIndex(){
		
		return SUCCESS;
	}
	
	public String itemIndex(){
		
		return SUCCESS;
	}
	/**
	 * 编辑会议状态
	 * @return
	 */
	public String meetEdit(){
		if(instanceid!=null){
			opreatorType = "finish";
			hash = zqbEventManageService.getMeetingMap(instanceid);
		}
		return SUCCESS;
	}
	
	/**
	 * 编辑会议状态
	 * @return
	 */
	public String meetRetime(){
		if(instanceid!=null){
			opreatorType = "retime";
			hash = zqbEventManageService.getMeetingMap(instanceid);
		}
		return SUCCESS;
	}
	
	public void setStatus(){
		if(opreatorType!=null&&instanceid!=null&&meettime!=null){
			boolean flag = zqbEventManageService.setStatus(opreatorType, instanceid, meettime);
			if(flag){
				ResponseUtil.write(SUCCESS);
			}else{
				ResponseUtil.write(ERROR);
			}
		}
	}
	/**
	 * 移除任务
	 */
	public void removeItem(){
		
		
	}
	
	/**
	 * 获取会议召开次数
	 */
	public void getMeetingCount(){
		if(customerno!=null&&meettype!=null&&grouptype!=null){
			String json = zqbEventManageService.getMeetingCount(customerno, jc,meettype, grouptype);
			ResponseUtil.write(json);
		}
		
	}
	/**
	 * 获取树
	 */
	public void getEventTreeJson(){
		 superman = zqbEventManageService.getIsSuperMan();
			String json = zqbEventManageService.getEventTreeJson(superman);
			ResponseUtil.write(json);
		
	}

	public List<HashMap> getRunList() {
		return runList;
	}

	public List<HashMap> getFinishList() {
		return finishList;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public HashMap getHash() {
		return hash;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Long getTaskid() {
		return taskid;
	}

	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}
	public boolean isReadonly() {
		return readonly;
	}
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String getTypePieData() {
		return typePieData;
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

	public String getTypePieData2() {
		return typePieData2;
	}

	public String getTypePieData3() {
		return typePieData3;
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
	public String getFilterbar() {
		return filterbar;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public void setJc(int jc) {
		this.jc = jc;
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

	public List<HashMap> getCustomerList() {
		return customerList;
	}

	public void setZqbEventManageService(ZqbEventManageService zqbEventManageService) {
		this.zqbEventManageService = zqbEventManageService;
	}

	public List<HashMap> getTalkList() {
		return talkList;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String indexEvent(){
		if(year==0)year = UtilDate.getYear(new Date());
		if(status==null){status="全部";}
		if(meettype==null){meettype="全部";}
		if(grouptype==null){grouptype="全部";}
		//获取客户列表
//		customerList = zqbEventManageService.getCurrentCustomerList();
//		if(customerList==null||customerList.size()==0){
//			OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
////			if(orgUser.getUsertype().equals(new Long(2))){
//				customerno = orgUser.getExtend1();
////			} 
//		} 
		if(customerno!=null){
			if (pageNumber == 0)
				pageNumber = 1;
			runList = zqbEventManageService.getMeetRunList(customerno,year, meettype, grouptype, status,pageSize, pageNumber);
			totalNum=zqbEventManageService.getEventRunListSize(customerno, pageSize,pageNumber);
//			filterbar = zqbEventManageService.getMeetFilterBar(year, meettype, grouptype, status);
		}
		readonly=false;
//		String userid=UserContextUtil.getInstance().getCurrentUserId();
//		if(!zqbEventManageService.getChartBarLabelListFlag(userid)){
//			readonly=true;
//		}
		return SUCCESS;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getTotalNum() {
		return totalNum;
	}
	
	public void updateClZdsxFlag(){
		String msg= zqbEventManageService.updateClFlag(instanceid,id);
		ResponseUtil.write(msg);

	}
	public void updateGzsc(){
		String msg= zqbEventManageService.updateGzsc(cid,did);
		ResponseUtil.write(msg);

	}
}
