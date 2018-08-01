package com.iwork.plugs.cms.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.conf.SystemConfig;
import com.iwork.core.upload.dao.FileUploadDAO;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.SpringBeanUtil;
import org.apache.log4j.Logger;
public class CmsFileUploadUtil {

	private static CmsFileUploadUtil instance = null;
	private static Logger logger = Logger.getLogger(CmsFileUploadUtil.class);
	private CmsFileUploadUtil() {
	}

	public static CmsFileUploadUtil getInstance() {
		if (instance == null) {
			instance = new CmsFileUploadUtil();
		}
		return instance;
	}

	public FileUpload saveKnowFile(File uploadify, String uploadifyFileName) {
		FileUpload fileModel = this.KmKnowUpload(uploadify, uploadifyFileName);
		FileUploadDAO uploadifyDAO = (FileUploadDAO)SpringBeanUtil.getBean("uploadifyDAO");
		FileUpload fu = uploadifyDAO.save(fileModel);
		if (fu == null) {
			this.deleteFile(fileModel);
		}
		return fu;
	}

	public FileUpload KmKnowUpload(File uploadify, String uploadifyFileName) {
		FileUpload fileModel = null;
		String extName = "";
		String newFileName = "";
		String cmsFilePath = SystemConfig._fileServerConf.getCmsFilePath();
		String rootPath = ServletActionContext.getServletContext().getRealPath(
				(cmsFilePath +"/PHOTO"));

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		File dirFile = new File(rootPath);
		if (!dirFile.isDirectory()) {
			dirFile.mkdirs();
		}
		if (uploadifyFileName.lastIndexOf(".") >= 0) {
			extName = uploadifyFileName.substring(uploadifyFileName
					.lastIndexOf("."));
		}
		newFileName = UUID.randomUUID().toString().replaceAll("-", "")
				+ extName;
		String filePath = rootPath +"/"+ newFileName; 
		String savePath = SystemConfig._fileServerConf.getCmsFilePath()
				+ "/PHOTO/"+newFileName; 
		File targetFile = new File(filePath);
		boolean flag = copy(uploadify, targetFile);
		if (flag) {
			fileModel = new FileUpload();
			fileModel.setFileId(UUID.randomUUID().toString()
					.replaceAll("-", ""));
			fileModel.setFileUrl(savePath);
			fileModel.setFileSaveName(newFileName);
			fileModel.setFileSrcName(uploadifyFileName);
			fileModel.setUploadTime(sf.format(new Date()));
		}
		return fileModel;
	}

	private boolean copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), 16384);
			out = new BufferedOutputStream(new FileOutputStream(dst), 16384);
			byte[] buffer = new byte[16384];
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}
			out.flush();
		} catch (Exception e) {
			logger.error(e,e);
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
		return true;
	}

	public boolean deleteFile(FileUpload model) {
		if (model != null) {
			String rootPath = ServletActionContext.getServletContext()
					.getRealPath("");
			String fileUrl = model.getFileUrl();
			if (fileUrl != null) {
				String savePath = rootPath + fileUrl.replaceAll("\\.\\.", "");
				File file = new File(savePath);
				if ((file.exists()) && (file.isFile())) {
					file.delete();
				}
			}
		}
		return true;
	}

}
