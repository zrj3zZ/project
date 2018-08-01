package com.ibpmsoft.project.zqb.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.sf.json.JSONArray;
import com.ibpmsoft.project.zqb.service.ZqbOperationManageService;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.meeting.util.UtilDate;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbOperationManageAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ZqbOperationManageService zqbOperationManageService;
	private List<HashMap> runList;
	private List<HashMap> finishList;
	private List<HashMap> talkList;
	private List<HashMap> customerList;
	private HashMap hash;
	private String projectNo;
	private String json;
	private int pageNumber; // 当前页数
	private String zInstanceId;
	public String getzInstanceId() {
		return zInstanceId;
	}

	public void setzInstanceId(String zInstanceId) {
		this.zInstanceId = zInstanceId;
	}

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

	private String rcywcblc;
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
	
	public String getNoticename() {
		return noticename;
	}

	public void setNoticename(String noticename) {
		this.noticename = noticename;
	}

	public String getSpzt() {
		return spzt;
	}

	public void setSpzt(String spzt) {
		this.spzt = spzt;
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
	public Long getOrgRoleId() {
		return orgRoleId;
	}

	public void setOrgRoleId(Long orgRoleId) {
		this.orgRoleId = orgRoleId;
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
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		orgRoleId = uc._userModel.getOrgroleid();
		return SUCCESS; 
	}

	public void search(){
		String json = zqbOperationManageService.geGslx(jydxbh,jydxmc);
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
			talkList = zqbOperationManageService.getTalkList(instanceid);
		}
		return SUCCESS;
	}
	
	public void talkCommit(){
		if(content!=null&&instanceid!=null){ 
			zqbOperationManageService.saveTalk(instanceid+"", content);
		}
		
	}
	
	public String charIndex(){
		typeBarData = zqbOperationManageService.getChartBarData();
		List<String> list =zqbOperationManageService.getChartBarLabelList();
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
			hash = zqbOperationManageService.getMeetingMap(instanceid);
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
			hash = zqbOperationManageService.getMeetingMap(instanceid);
		}
		return SUCCESS;
	}
	
	public void setStatus(){
		if(opreatorType!=null&&instanceid!=null&&meettime!=null){
			boolean flag = zqbOperationManageService.setStatus(opreatorType, instanceid, meettime);
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
			String json = zqbOperationManageService.getMeetingCount(customerno, jc,meettype, grouptype);
			ResponseUtil.write(json);
		}
		
	}
	/**
	 * 获取树
	 */
	public void getEventTreeJson (){
		 superman = zqbOperationManageService.getIsSuperMan();
			String json = zqbOperationManageService.getEventTreeJson(superman);
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

	public String getRcywcblc() {
		return rcywcblc;
	}

	public void setRcywcblc(String rcywcblc) {
		this.rcywcblc = rcywcblc;
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

	public void setZqbOperationManageService(ZqbOperationManageService zqbOperationManageService) {
		this.zqbOperationManageService = zqbOperationManageService;
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
		rcywcblc = ProcessAPI.getInstance().getProcessActDefId("RCYWCB");
		if(year==0)year = UtilDate.getYear(new Date());
		if(status==null){status="全部";}
		if(meettype==null){meettype="全部";}
		if(grouptype==null){grouptype="全部";}
		//获取当前登录用户角色id
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		orgRoleId = uc._userModel.getOrgroleid();
		if(orgRoleId==3l)
			customerno=uc._userModel.getExtend1();
		String userid = uc._userModel.getUserid();
		if (pageNumber == 0)
			pageNumber = 1;
		runList = zqbOperationManageService.getMeetRunList(pageSize, pageNumber,userid,customerno,orgRoleId,zqdm,zqjc,startdate,enddate,noticename,spzt);
		totalNum=zqbOperationManageService.getEventRunListSize(userid,customerno,orgRoleId,zqdm,zqjc,startdate,enddate,noticename,spzt);
		readonly=false;
		return SUCCESS;
	}
/**
 * 获取呈报明细
 * @param type
 */
	public String getYWCBMX() {
		// 编号、名称、类型、日期、附件、正文
		content = ZqbOperationManageService.getYWCBMX(instanceid);
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
		String msg= zqbOperationManageService.updateClFlag(instanceid,id);
		ResponseUtil.write(msg);

	}
	public void updateGzsc(){
		String msg= zqbOperationManageService.updateGzsc(cid,did);
		ResponseUtil.write(msg);

	}
}
