package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.service.ZqbOnlineChatService;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.db.DBUtil;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbOnlineChatAction extends ActionSupport {
	private String sendUserName;
	private String chatRecordName;
	private List<HashMap> sendUserList;
	private String onlineName;
	private ZqbOnlineChatService zqbOnlineChatService;
	private String nickname;
	private String chatName;
	private String dataId;
	private String startdate;
	private String enddate;
	private String content;
	private List<HashMap> countChatList;
	private String startime;
	private String endtime;
	private List<HashMap> allName;
	private String sendname;
	private String companyjc;
	private List<HashMap> compantRecordList;
	private String cfjlid;
	private String cfjlformid;
	//--------------
	private String customername;
	private String zqdm;
	private String zqjc;
	private String fssjstart;
	private String fssjend;
	private String cfqksm;
	private int totalNum;
	private int pageNumber;
	private int pageSize = 10;
	private int pageNow;
	//--------------
	
	public String getCfjlid() {
		return cfjlid;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
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

	public String getFssjstart() {
		return fssjstart;
	}

	public void setFssjstart(String fssjstart) {
		this.fssjstart = fssjstart;
	}

	public String getFssjend() {
		return fssjend;
	}

	public void setFssjend(String fssjend) {
		this.fssjend = fssjend;
	}

	public String getCfqksm() {
		return cfqksm;
	}

	public void setCfqksm(String cfqksm) {
		this.cfqksm = cfqksm;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNow() {
		return pageNow;
	}

	public void setPageNow(Integer pageNow) {
		this.pageNow = pageNow;
	}

	public void setCfjlid(String cfjlid) {
		this.cfjlid = cfjlid;
	}

	public String getCfjlformid() {
		return cfjlformid;
	}

	public void setCfjlformid(String cfjlformid) {
		this.cfjlformid = cfjlformid;
	}

	public List<HashMap> getCompantRecordList() {
		return compantRecordList;
	}

	public void setCompantRecordList(List<HashMap> compantRecordList) {
		this.compantRecordList = compantRecordList;
	}

	public String getSendname() {
		return sendname;
	}

	public void setSendname(String sendname) {
		this.sendname = sendname;
	}

	public String getCompanyjc() {
		return companyjc;
	}

	public void setCompanyjc(String companyjc) {
		this.companyjc = companyjc;
	}

	public List<HashMap> getAllName() {
		return allName;
	}

	public void setAllName(List<HashMap> allName) {
		this.allName = allName;
	}

	public String getStartime() {
		return startime;
	}

	public void setStartime(String startime) {
		this.startime = startime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public List<HashMap> getCountChatList() {
		return countChatList;
	}

	public void setCountChatList(List<HashMap> countChatList) {
		this.countChatList = countChatList;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getChatName() {
		return chatName;
	}

	public void setChatName(String chatName) {
		this.chatName = chatName;
	}

	public String getChatRecordName() {
		return chatRecordName;
	}

	public void setChatRecordName(String chatRecordName) {
		this.chatRecordName = chatRecordName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public ZqbOnlineChatService getZqbOnlineChatService() {
		return zqbOnlineChatService;
	}

	public void setZqbOnlineChatService(ZqbOnlineChatService zqbOnlineChatService) {
		this.zqbOnlineChatService = zqbOnlineChatService;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}
	
	public List<HashMap> getSendUserList() {
		return sendUserList;
	}

	public void setSendUserList(List<HashMap> sendUserList) {
		this.sendUserList = sendUserList;
	}

	public void setOnlineName(String onlineName) {
		this.onlineName = onlineName;
	}

	public String index(){
		sendUserName=zqbOnlineChatService.getUserName();
		sendUserList=zqbOnlineChatService.getSendUserList();
		chatRecordName=zqbOnlineChatService.chatRecordName();
		return SUCCESS;
	}
	
	public void getOnlineName(){
		String msg = zqbOnlineChatService.getOnlineNameList(onlineName);
		ResponseUtil.write(msg);
	}
	
	public void getOnlineRecordContent(){
		String chatRecordContent=zqbOnlineChatService.getOnlineRecordList(nickname,chatName,startdate,enddate,content,sendname,companyjc);
		ResponseUtil.write(chatRecordContent);
	}
	
	public String getOnlineRecord(){
		allName = zqbOnlineChatService.getAllName();
		return SUCCESS;
	}
	public String onlineChatCount(){
		countChatList=zqbOnlineChatService.getonlineChatCount(startime,endtime);
		return SUCCESS;
	}
	public void dgcfjlToExcl(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbOnlineChatService.thxmexcfjlportexcl(response,startdate,enddate,cfqksm,zqjc,zqdm);
	}
	/**
	 增加一个功能：挂牌企业处罚记录 zqbGpqycfjlIndex
	一、查询条件：
		1证券代码（输入时可联想出公司简称进行选择么？） 、
		2证券简称、
		3发生开始时间（默认是当日，可选择）、
		4发生截止时间（默认是当日，可选择）、
		5处罚情况说明。
	二、挂牌企业处罚记录列表：证券代码 、证券简称、处罚情况说明、发生时间。
		按日期倒序，查询条件：证券代码 、证券简称、处罚情况说明、发生时间，带分页
	三、添加内容
		证券代码 （输入时可联想出公司简称进行选择么？）、证券简称、处罚情况说明、发生时间，处罚情况说明、相关资料（附件） ，填报人（隐藏，但记入表中）
	*/
	public String zqbGpqycfjlIndex(){
		//可编辑的表单的demid、formid
		cfjlid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE where title='挂牌企业处罚记录表单'", "ID");
		cfjlformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE where title='挂牌企业处罚记录表单'", "FORMID");
		if (pageNumber == 0)
			pageNumber = 1;
		compantRecordList = zqbOnlineChatService.zqbGpqycfjlIndex(zqjc,zqdm,customername,fssjstart,fssjend,cfqksm,pageSize,pageNumber);
		//totalNum=compantRecordList.size();
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) TOTALNUM FROM (SELECT INSTANCEID,DATAID FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_GPQYCFJL')) A ) C LEFT JOIN (SELECT A.INSTANCEID,DATAID,B.ID,B.CREATEUSER,B.ZQDM,B.ZQJC,B.FSSJ,B.CFQKSM,B.XGZL_1,b.customername FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_GPQYCFJL')) A LEFT JOIN BD_XP_GPQYCFJL B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID left join BD_ZQB_KH_BASE E  on e.customername= d.customername where 1=1 ");
		Map params = new HashMap();
		int n = 1 ;
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND UPPER(CUSTOMERNAME) LIKE ? ");
			params.put(n, "%"+customername.toUpperCase()+"%");
			n++;
		}
		if(fssjstart!=null&&!fssjstart.equals("")){
			sb.append(" AND FSSJ >= TO_DATE( ? , 'YYYY-MM-DD')");
			params.put(n, fssjstart);
			n++;
		}
		if(fssjend!=null&&!fssjend.equals("")){
			sb.append(" AND FSSJ <= TO_DATE( ? , 'YYYY-MM-DD')");
			params.put(n, fssjend);
			n++;
		}
		if(cfqksm!=null&&!cfqksm.equals("")){
			sb.append(" AND LTRIM(RTRIM(CFQKSM)) LIKE ? ");
			params.put(n, "%"+cfqksm+"%");
			n++;
		}
		if(zqjc!=null&&!zqjc.equals("")){
			sb.append(" AND e.zqjc LIKE ? ");
			params.put(n, "%"+zqjc+"%");
			n++;
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND e.zqdm LIKE ? ");
			params.put(n, "%"+zqdm+"%");
			n++;
		}
		totalNum = DBUTilNew.getInt("TOTALNUM",sb.toString(),params);
		return SUCCESS;
	}
	public void zqbGpqycfjlGetZqdmzqjclist(){
		String zqdmzqjc = zqbOnlineChatService.zqbGpqycfjlGetZqdmzqjclist(zqdm);
		ResponseUtil.write(zqdmzqjc);
	}
	public void zqbGpqycfjlAddZqdmzqjclist(){
		String zqdmzqjc = zqbOnlineChatService.zqbGpqycfjlAddZqdmzqjclist(zqdm);
		ResponseUtil.write(zqdmzqjc);
	}
	public void getAllCompany(){
		String allcompany = zqbOnlineChatService.getAllCompany();
		ResponseUtil.write(allcompany);
	}
}
