package com.iwork.core.upload.action;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.upload.FileUploadUtil;
import com.ibpmsoft.project.zqb.upload.Fileupload;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.service.FileUploadService;
import com.opensymphony.xwork2.ActionSupport;

public class UploadifyAction extends ActionSupport
{
  private File uploadify;
  private String uploadifyFileName;
  private String fileUUID;
  private String url;
  private String srcFileName;
  private FileUploadService uploadifyService;
  private String parentColId;
  private String parentDivId;
  private String sizeLimit = "";
  private String multi = "true";
  private String fileExt = "";
  private String fileDesc = "";

  public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
  public String baseUpload()
  {
    return "success";
  }
  public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter)==null?"":config.get(parameter);
		}
		return result;
	}
  public String doBaseUpload()
    throws Exception
  {
	  HttpServletResponse response = ServletActionContext.getResponse();
	  response.setContentType("text/html;charset=UTF-8");
	  String flags="success";
    if (this.uploadify != null) {
      FileUpload model = this.uploadifyService.save(this.uploadify, this.uploadifyFileName);
      FileUploadUtil file = new FileUploadUtil();
      String flag=file.uploadname(uploadifyFileName);
      if(!flag.equals("true")){
    	  file.delFile(uploadify);
    	  response.getWriter().print("<script>alert('上传失败！');history.go(-1);</script>");
    	  return null;
      }
      Date d=new Date();
      SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");//转换格式
  	  String fileurl=ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getFormFilePath())+File.separator+sdf.format(d)+File.separator+model.getFileSaveName();
      String zqserver = getConfigUUID("zqServer")==null?"":getConfigUUID("zqServer");
		if(zqserver.equals("gxzq")){
			Fileupload fileupload= new Fileupload();
			fileupload.fileScann(model.getFileSrcName(),fileurl);
		}
      if (model != null) {
        this.srcFileName = model.getFileSrcName();
        this.fileUUID = model.getFileId();
        this.url = model.getFileUrl();
      }
    }

    return flags;
  }

  public String upload()
    throws Exception
  {
	  HttpServletResponse response = ServletActionContext.getResponse();
    FileUpload model = this.uploadifyService.save(this.uploadify, this.uploadifyFileName);
    FileUploadUtil file = new FileUploadUtil();
    String flag=file.uploadname(uploadifyFileName);
    if(!flag.equals("true")){
  	  file.delFile(uploadify);
  	  response.getWriter().print("{flag:false}");
  	  return null;
    }
   
    response.setCharacterEncoding("utf-8");
    if (model != null) {
      String uuid = model.getFileId();
      String url = model.getFileUrl();
      response.getWriter().print("{flag:true,uuid:'" + uuid + "',url:'" + url + "'}");
    } else {
      response.getWriter().print("{flag:false}");
      throw new Exception(this.uploadifyFileName + "上传失败");
    }
    return null;
  }

  public String remove()
    throws Exception
  {
    HttpServletResponse response = ServletActionContext.getResponse();
    response.setCharacterEncoding("utf-8");
    if ((this.fileUUID != null) && (!this.fileUUID.equals(""))) {
      this.uploadifyService.deleteFileUpload(FileUpload.class, this.fileUUID);
      response.getWriter().print("{flag:true}");
    } else {
      response.getWriter().print("{flag:false}");
    }
    return null;
  }

  public String download()
  {
	  Serializable sfileUUID = fileUUID;
    this.uploadifyService.downLoadFile(FileUpload.class, sfileUUID, ServletActionContext.getResponse());
    return null;
  }
  public String showUploadifyPage() {
    return "success";
  }
  /**
   * Web Uploader插件
   * @return
   */
  public String showWebUploaderPage() {
	    return "success";
	  }
  public FileUploadService getUploadifyService() {
    return this.uploadifyService;
  }

  public void setUploadifyService(FileUploadService uploadifyService) {
    this.uploadifyService = uploadifyService;
  }

  public String getFileUUID() {
    return this.fileUUID;
  }

  public void setFileUUID(String fileUUID) {
    this.fileUUID = fileUUID;
  }

  public File getUploadify() {
    return this.uploadify;
  }

  public void setUploadify(File uploadify) {
    this.uploadify = uploadify;
  }

  public String getUploadifyFileName() {
    return this.uploadifyFileName;
  }

  public void setUploadifyFileName(String uploadifyFileName) {
    this.uploadifyFileName = uploadifyFileName;
  }

  public String getParentColId() {
    return this.parentColId;
  }

  public void setParentColId(String parentColId) {
    this.parentColId = parentColId;
  }

  public String getParentDivId() {
    return this.parentDivId;
  }

  public void setParentDivId(String parentDivId) {
    this.parentDivId = parentDivId;
  }

  public String getSizeLimit() {
    return this.sizeLimit;
  }

  public void setSizeLimit(String sizeLimit) {
    this.sizeLimit = sizeLimit;
  }

  public String getMulti() {
    return this.multi;
  }

  public void setMulti(String multi) {
    this.multi = multi;
  }

  public String getFileExt() {
    return this.fileExt;
  }

  public void setFileExt(String fileExt) {
    this.fileExt = fileExt;
  }

  public String getFileDesc() {
    return this.fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getSrcFileName() {
    return this.srcFileName;
  }
}