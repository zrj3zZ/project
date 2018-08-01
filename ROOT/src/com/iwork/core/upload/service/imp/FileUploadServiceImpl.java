package com.iwork.core.upload.service.imp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.StringUtil;
import com.iwork.core.upload.dao.FileUploadDAO;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.core.upload.util.FileUploadUtil;

public class FileUploadServiceImpl implements FileUploadService {	
	private FileUploadUtil fileUploadUtil;
	private FileUploadDAO uploadifyDAO;

	public FileUploadDAO getUploadifyDAO() {
		return uploadifyDAO;
	}

	public void setUploadifyDAO(FileUploadDAO uploadifyDAO) {
		this.uploadifyDAO = uploadifyDAO;
	}
	
	/**
	 * 保存上传文件
	 */
	public FileUpload save(File uploadify,String uploadifyFileName) {
		int countStr = StringUtil.countStr(uploadifyFileName,".");
		if(countStr>=2){
			return null;
		}
		if(StringUtil.mathcer("[@$%\\/]",uploadifyFileName)){
			return null;
		}
		if(uploadifyFileName!=null){
			if(!StringUtil.validata(FileUtil.getFileExt(uploadifyFileName))){
				return null;
			}
		}else{
			return null;
		}
		FileUpload fileModel = fileUploadUtil.upload(uploadify, uploadifyFileName);
		FileUpload fu =uploadifyDAO.save(fileModel);
		if(fu==null){
			fileUploadUtil.deleteFile(fileModel);
		} 
		return fu;
	}
	
	/**
	 * 保存上传文件
	 */
	public FileUpload saveForPath(File uploadify,String filePath,String uuid) {
		FileUpload fileModel = fileUploadUtil.uploadForPath(uploadify, filePath, uuid);
		FileUpload model = uploadifyDAO.getFileUpload(FileUpload.class, uuid);
		if(model!=null){
			fileModel.setFileId(model.getFileId());
			uploadifyDAO.update(fileModel);   
		}else{
			FileUpload fu =uploadifyDAO.save(fileModel);
			if(fu==null){ 
				fileUploadUtil.deleteFile(fileModel);
			}
		}
		return fileModel;
	}
	
	/**
	 * 保存上传文件
	 */
	public FileUpload save(File uploadify,String uploadifyName,String uuid) {
		FileUpload fileModel = fileUploadUtil.upload(uploadify, uploadifyName, uuid);
		FileUpload model = uploadifyDAO.getFileUpload(FileUpload.class, uuid); 
		if(model!=null){ 
			model.setUploadTime(fileModel.getUploadTime());
			uploadifyDAO.update(model);   
		}else{
			FileUpload fu =uploadifyDAO.save(fileModel);
			if(fu==null){ 
				fileUploadUtil.deleteFile(fileModel);
			}
		}
		return fileModel;
	}
	/**
     * 保存文件到km_file/know
     * @param uploadify
     * @param uploadifyFileName
     * @return
     */
    public FileUpload saveKnowFile(File uploadify,String uploadifyFileName){
    	FileUpload fileModel = fileUploadUtil.KmKnowUpload(uploadify, uploadifyFileName);
		FileUpload fu =uploadifyDAO.save(fileModel);
		if(fu==null){
			fileUploadUtil.deleteFile(fileModel);
		} 
		return fu;
    }
    /**
     * 保存文件到km_file/document
     */
    public FileUpload saveDocumentFile(File uploadify,String uploadifyFileName){
    	FileUpload fileModel = fileUploadUtil.KmDocumentUpload(uploadify, uploadifyFileName);
		FileUpload fu =uploadifyDAO.save(fileModel);
		if(fu==null){
			fileUploadUtil.deleteFile(fileModel);
		} 
		return fu;
    }

	/**
	 * 删除上传文件
	 */
	public boolean deleteFileUpload(Class clazz, Serializable id) {
		boolean flag = false;
		FileUpload model = uploadifyDAO.getFileUpload(clazz, id);
		if(model!=null){
			flag = fileUploadUtil.deleteFile(model);
			if(flag){
				uploadifyDAO.deleteFileUpload(model);
			}
		}
		return flag;
	}
    /**
     * 
     */
	public boolean deleteAll(Collection entities) {
		return uploadifyDAO.deleteAll(entities);
	}
	/**
	 * 
	 */
	public boolean deleteAll(Class clazz, String ids){
		boolean flag = true;
		String[] idArray = ids.split(",");
		for(String id:idArray){
			if(id==null || "".equals(id))continue;
			flag = this.deleteFileUpload(FileUpload.class, id);
			if(flag==false)break;
		}
		return flag;
	}
	/**
	 * 
	 */
	public FileUpload getFileUpload(Class clazz, Serializable id) {
		return uploadifyDAO.getFileUpload(clazz, id);
	}
	/**
	 * 
	 */
	public List<FileUpload> getFileUploads(Class clazz, String ids) {
		// TODO Auto-generated method stub
		List<FileUpload> list = new ArrayList<FileUpload> ();
		if(ids!=null){
			String[] idArray = ids.split(",");
			for(String id:idArray){
				if(id==null || "".equals(id)) continue;
				FileUpload file = uploadifyDAO.getFileUpload(FileUpload.class, id);
				if(file!=null){
					list.add(file);
				}				
			}
		}
		return list;
	}
    /**
     * @return 
     * 
     */
	public boolean downLoadFile(Class clazz, Serializable fileUUID,HttpServletResponse response) {
		if(fileUUID!=null&&!fileUUID.equals("")){
			 String match="^[a-zA-Z0-9]{4,10}-[a-zA-Z0-9]{4,10}-[a-zA-Z0-9]{4,10}-[a-zA-Z0-9]{4,10}-[a-zA-Z0-9]{4,20}$|(^[a-zA-Z0-9]{32,50}$)";
			  boolean matches=fileUUID.toString().matches(match);
			  if(!matches){
					return false;
			  }
			BufferedOutputStream bos = null;  
			FileInputStream fis = null; 
			FileUpload model = this.getFileUpload(FileUpload.class, fileUUID);
			if(model==null) return false;
			String fileUrl=model.getFileUrl();
			if(fileUrl!=null&&!"".equals(fileUrl)){
				try {
					String fileName=model.getFileSrcName();
					try{
						fis = new FileInputStream(fileUrl);
					}catch(Exception e){}
					
					if(fis==null){
						 fileUrl = ServletActionContext.getServletContext().getRealPath(fileUrl.replaceAll("\\.\\.", ""));
						 fis = new FileInputStream(fileUrl);
					}
					
//					String fileUrl = model.getFileUrl();  
					 // 添加头信息，为"文件下载/另存为"对话框指定默认文件名 
					String disposition = "attachment;filename=\"" + UploadFileNameCodingUtil.StringEncoding(fileName) +"\"";
					// 指定返回的是一个不能被客户端读取的流，必须被下载   
					response.setContentType("application/octet-stream;charset=UTF-8");
					response.setHeader("Content-disposition", disposition);  
					bos = new BufferedOutputStream(response.getOutputStream());
					 byte[] buffer = new byte[fis.available()];
					 fis.read(buffer);
//					byte[] buffer = new byte[FileUploadUtil.BUFFER_SIZE]; 
//			        int offset = 0;
//			        while ((offset = fis.read(buffer, 0, buffer.length)) != -1) {
					    bos.write(buffer);  
//					}
					bos.flush();
				} catch (Exception e) {
					
				} finally {
					if (fis != null) {
						try {
							fis.close();
						} catch (Exception e) {
						}
					}
					if (bos != null) {
						try {
							bos.close();
						} catch (Exception e) {
						}
					}
				}  
			}
		}
		return true;
	}
	public FileUploadUtil getFileUploadUtil() {
		return fileUploadUtil;
	}
	public void setFileUploadUtil(FileUploadUtil fileUploadUtil) {
		this.fileUploadUtil = fileUploadUtil;
	}


}
