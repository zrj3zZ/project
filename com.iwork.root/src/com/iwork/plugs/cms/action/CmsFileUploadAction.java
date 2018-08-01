package com.iwork.plugs.cms.action;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.upload.FileUploadUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.plugs.cms.util.CmsFileUploadUtil;
import com.opensymphony.xwork2.ActionSupport;

public class CmsFileUploadAction extends ActionSupport {

	  private File uploadify;
	  private String uploadifyFileName;
	  private FileUploadService uploadifyService;
	
	public String upload() throws Exception {
		FileUpload model = CmsFileUploadUtil.getInstance().saveKnowFile(uploadify,
				uploadifyFileName);
		HttpServletResponse response = ServletActionContext.getResponse();
		FileUploadUtil file = new FileUploadUtil();
		String flag = file.uploadname(uploadifyFileName);
		if (!flag.equals("true")) {
			file.delFile(uploadify);
			response.getWriter().print("{flag:false}");
			return null;
		}
		response.setCharacterEncoding("utf-8");
		if (model != null) {
			String uuid = model.getFileId();
			String url = model.getFileUrl();
			response.getWriter().print(
					"{flag:true,uuid:'" + uuid + "',url:'" + url + "'}");
		} else {
			response.getWriter().print("{flag:false}");
			throw new Exception(uploadifyFileName + "上传失败");
		}
		return null;
	}

	public File getUploadify() {
		return uploadify;
	}

	public void setUploadify(File uploadify) {
		this.uploadify = uploadify;
	}

	public String getUploadifyFileName() {
		return uploadifyFileName;
	}

	public void setUploadifyFileName(String uploadifyFileName) {
		this.uploadifyFileName = uploadifyFileName;
	}

	public FileUploadService getUploadifyService() {
		return uploadifyService;
	}

	public void setUploadifyService(FileUploadService uploadifyService) {
		this.uploadifyService = uploadifyService;
	}
}
