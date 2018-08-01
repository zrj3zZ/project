package com.iwork.plugs.weboffice.action;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.UUIDUtil;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.plugs.weboffice.service.IWorkPlugsWebOfficeService;
import com.iwork.sdk.FileUploadAPI;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;

public class IWorkPlugsWebOfficeAction extends ActionSupport {
	private static Logger logger = Logger.getLogger(IWorkPlugsWebOfficeAction.class);
	public IWorkPlugsWebOfficeService iWorkPlugsWebOfficeService;
	private String fileType;
	private String edittype;
	private String editTypeStr;
	private String fileUUID;
	private String fileName;
	private String userId;
	private String t;
	private String url;
	private FileUpload fileUpload;
	
	public String documentEdit(){
		ActionContext actionContext = ActionContext.getContext();
    	//获得response对象
		HttpServletRequest request=(HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);  
		      url=request.getScheme()+"://"+request.getHeader("host")+"/weboffice2015/";   
			if(fileUUID!=null&&edittype!=null){
				if(edittype.equals("w")){
					editTypeStr = "1";
				}else if(edittype.equals("r")){
					editTypeStr = "0";
				}
				 fileUpload = FileUploadAPI.getInstance().getFileUpload(fileUUID); 
				 if(fileUpload!=null){
					 fileName = fileUpload.getFileSrcName();
				 }
			}else{
				if(fileUUID==null){
					fileUUID = UUIDUtil.getUUID();
				}
			}
			if(fileType==null)fileType=".doc";
			if(fileType.equals("docx")){
				 fileType = "doc";
			 }else if(fileType.equals("xlsx")){
				 fileType = "xls";
			 }else if(fileType.equals("pptx")){
				 fileType = "ppt";
			 }else{
				 fileType = "doc";
			 }
			 if(fileType!=null&&(fileType.indexOf(".")<0)){
				 fileType = "."+fileType;
			 }
			 
			userId = UserContextUtil.getInstance().getCurrentUserId();
		return SUCCESS;
	}
	
	public String saveDocument(){
		ActionContext actionContext = ActionContext.getContext();
		//获得response对象
		HttpServletRequest request=(HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);  
		url=request.getScheme()+"://"+request.getHeader("host")+"/weboffice2015/OfficeServer2015";   
		if(fileUUID!=null&&edittype!=null){
			if(edittype.equals("w")){
				editTypeStr = "1,1";
			}else if(edittype.equals("r")){
				editTypeStr = "0,4";
			}
			fileUpload = FileUploadAPI.getInstance().getFileUpload(fileUUID); 
			if(fileUpload!=null){
				fileName = fileUpload.getFileSrcName();
				fileType =  FileUtil.getFileExt(fileUpload.getFileSrcName());
				if(fileType.equals("docx")){
					fileType = "doc";
				}else if(fileType.equals("xlsx")){
					fileType = "xls";
				}else if(fileType.equals("pptx")){
					fileType = "ppt";
				}
				if(fileType!=null&&(fileType.indexOf(".")<0)){
					fileType = "."+fileType;
				}
			}
			
		}else{
			fileUUID = UUIDUtil.getUUID();
		}
		userId = UserContextUtil.getInstance().getCurrentUserId();
		return SUCCESS;
	}

	public void index(){
		ActionContext actionContext = ActionContext.getContext();
    	//获得response对象
		HttpServletRequest request=(HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);  	
		try {
			request.setCharacterEncoding("gb2312"); 
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
    	HttpServletResponse response=(HttpServletResponse) actionContext.get(ServletActionContext.HTTP_RESPONSE);  	
    	iWorkPlugsWebOfficeService.executeRun(request, response);
	}
	
	
	public void doSave(){
		
	}
	
	/**
	 * office在线编辑
	 * @return
	 */ 
	public String officeOnline(){
		ActionContext actionContext = ActionContext.getContext();
    	//获得response对象
		HttpServletRequest request=(HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);  
		      url=request.getScheme()+"://";   
	        url+=request.getHeader("host");   
			if(fileUUID!=null&&edittype!=null){
				if(edittype.equals("w")){
					editTypeStr = "1,1";
				}else if(edittype.equals("r")){
					editTypeStr = "0,4";
				}
				 fileUpload = FileUploadAPI.getInstance().getFileUpload(fileUUID); 
				 if(fileUpload!=null){
					 fileName = fileUpload.getFileSrcName();
					 fileType =  FileUtil.getFileExt(fileUpload.getFileSrcName());
					 if(fileType.equals("docx")){
						 fileType = "doc";
					 }else if(fileType.equals("xlsx")){
						 fileType = "xls";
					 }else if(fileType.equals("pptx")){
						 fileType = "ppt";
					 }
					 if(fileType!=null&&(fileType.indexOf(".")<0)){
						 fileType = "."+fileType;
					 }
				 }
				 userId = UserContextUtil.getInstance().getCurrentUserId();
			}
		return SUCCESS;
	}
	
	public IWorkPlugsWebOfficeService getiWorkPlugsWebOfficeService() {
		return iWorkPlugsWebOfficeService;
	}
	public void setiWorkPlugsWebOfficeService(
			IWorkPlugsWebOfficeService iWorkPlugsWebOfficeService) {
		this.iWorkPlugsWebOfficeService = iWorkPlugsWebOfficeService;
	}
	public String getEdittype() {
		return edittype;
	}
	public void setEdittype(String edittype) { 
		this.edittype = edittype;
	}
	public String getFileUUID() {
		return fileUUID;
	}
	public void setFileUUID(String fileUUID) {
		this.fileUUID = fileUUID;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getEditTypeStr() {
		return editTypeStr;
	}
	public void setEditTypeStr(String editTypeStr) {
		this.editTypeStr = editTypeStr;
	}
	public FileUpload getFileUpload() {
		return fileUpload;
	}
	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
