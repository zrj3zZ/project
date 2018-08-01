package com.ibpmsoft.project.zqb.action;

import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;  
import org.apache.struts2.ServletActionContext;  
import com.ibpmsoft.project.zqb.service.ZqbFileCompareService;
import com.iwork.core.util.ResponseUtil;
import com.iwork.km.know.service.KnowService;
import com.opensymphony.xwork2.ActionSupport; 

public class ZqbFileCompareAction extends ActionSupport {
	
	private Logger Log = Logger.getLogger(ZqbFileCompareAction.class);
	private ZqbFileCompareService zqbFileCompareService;
	private KnowService knowService;
	private static final String FilePath = "D:\\";  
	private String FILETEXT;
	private String filename;
	private String senduser;
	private String nickname;
	private String chatName;
	private String startdate; 
	private String enddate;
	private String content;
	private String sendname;
	private String companyjc;
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getChatName() {
		return chatName;
	}

	public void setChatName(String chatName) {
		this.chatName = chatName;
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

	public String getSendname() {
		return sendname;
	}

	public String getCompanyjc() {
		return companyjc;
	}

	public void setCompanyjc(String companyjc) {
		this.companyjc = companyjc;
	}

	public void setSendname(String sendname) {
		this.sendname = sendname;
	}

	private static final long serialVersionUID = -8694640030455344419L;

	public String impKnowIndex(){
		return SUCCESS;
	}
	
	public void impKnow() throws Exception {
		zqbFileCompareService.impKnow(filename);
	}
	
	public void expKnow(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbFileCompareService.expKnow(response);
	}
	public void expkhmc(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbFileCompareService.expkhmc(response);
	}
	public void expLTJL(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbFileCompareService.expLTJL(response,senduser,nickname, chatName,startdate, enddate, content,sendname,companyjc);
	}
	public void expDelete(){
		Integer num = 0;
		num = zqbFileCompareService.expDelete(senduser,nickname, chatName,startdate, enddate, content,sendname,companyjc);
		if(num > 0){
			ResponseUtil.write("删除成功");
		}else{
			ResponseUtil.write("删除失败");
		}
	}
	
	public String fileCompareList(){
		return SUCCESS;
	}
	
	public String fileifromlist(){
		return SUCCESS;
	}
	
	public String fileCompareGo(){
		return SUCCESS;
	}
  
	public void fileCompare(){
		String content=zqbFileCompareService.fileCompare(FILETEXT);
		ResponseUtil.write(content);
	}
	
	public void readPhoneBook(){
		String content=zqbFileCompareService.fileCompare(FILETEXT);
		ResponseUtil.write(content);
	}
	
	public KnowService getKnowService() {
		return knowService;
	}

	public void setKnowService(KnowService knowService) {
		this.knowService = knowService;
	}
	
	public ZqbFileCompareService getZqbFileCompareService() {
		return zqbFileCompareService;
	}

	public void setZqbFileCompareService(ZqbFileCompareService zqbFileCompareService) {
		this.zqbFileCompareService = zqbFileCompareService;
	}
  
    public String getFILETEXT() {
		return FILETEXT;
	}

	public void setFILETEXT(String fILETEXT) {
		FILETEXT = fILETEXT;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSenduser() {
		return senduser;
	}

	public void setSenduser(String senduser) {
		this.senduser = senduser;
	}
		
}  
