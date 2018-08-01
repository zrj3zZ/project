package com.iwork.core.upload.util;

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

import com.ibm.icu.util.Calendar;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.FileUtil;
import com.iwork.core.upload.model.FileUpload;
import org.apache.log4j.Logger;

/**
 * 文件上传
 * @author YangDayong
 *
 */
public class FileUploadUtil {
	private static Logger logger = Logger.getLogger(FileUploadUtil.class);
	public static final int BUFFER_SIZE = 16 * 1024;
	
	/**
	 * 存放在other文件夹
	 * @param uploadify
	 * @param uploadifyFileName
	 * @return
	 */
	public FileUpload uploadForPath(File uploadify,String fileDirFullPath,String uuid){
		FileUpload fileModel = null;
		  String extName = "";//扩展名
		  String newFileName= "";//新文件名
		  if(uuid==null){ 
			  uuid =  UUID.randomUUID().toString().replaceAll("-", ""); 
		  }
		  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		  File dirFile = new File(fileDirFullPath); 
		  if(!dirFile.isDirectory()){
			  dirFile.mkdirs();
		  }
		  if(uploadify!=null){
			   extName = FileUtil.getFileExt(uploadify.getName());
		  }
		  newFileName=uuid+"."+extName;
		  String fileFullPath = fileDirFullPath+File.separator+newFileName;
		  File targetFile = new File(fileFullPath);
		  if(targetFile!=null){
			  boolean flag = this.copy(uploadify, targetFile);
			  if(flag){
				  
				  fileModel = new FileUpload();
				  fileModel.setFileId(uuid); 
				  fileModel.setFileUrl(fileFullPath);
				  fileModel.setFileSaveName(newFileName);
				  fileModel.setFileSrcName(uploadify.getName());
				  fileModel.setUploadTime(sf.format(new Date()));
			  }
		  }
		  return fileModel;
	}
	/**
	 * 存放在other文件夹
	 * @param uploadify
	 * @param uploadifyFileName
	 * @return
	 */
	public FileUpload upload(File uploadify,String uploadifyFileName,String uuid){
		FileUpload fileModel = null;
		String extName = "";//扩展名
		String newFileName= "";//新文件名
		Date data=new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		String format = sd.format(data);
		String rootPath =  ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getFormFilePath().replace("/",File.separator));
		rootPath = rootPath.replace("/",File.separator)+File.separator+format;
		if(uuid==null){ 
			uuid =  UUID.randomUUID().toString().replaceAll("-", ""); 
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		File dirFile = new File(rootPath);
		if(!dirFile.isDirectory()){
			dirFile.mkdirs();
		}
		if(uploadifyFileName!=null){
			extName = FileUtil.getFileExt(uploadifyFileName);
		}
		newFileName=uuid+"."+extName;
		String filePath = SystemConfig._fileServerConf.getFormFilePath().replaceAll("\\.\\.", "") + File.separator+format + File.separator + newFileName; 
		filePath = filePath.replace("/",File.separator);
		String savePath = rootPath+File.separator+newFileName; 
		File targetFile = new File(savePath);
		boolean flag = this.copy(uploadify, targetFile);
		if(flag){
			
			fileModel = new FileUpload();
			fileModel.setFileId(uuid); 
			fileModel.setFileUrl(filePath);
			fileModel.setFileSaveName(newFileName);
			fileModel.setFileSrcName(uploadifyFileName);
			fileModel.setUploadTime(sf.format(new Date()));
		}
		return fileModel;
	}
	/**
	 * 存放在other文件夹
	 * @param uploadify
	 * @param uploadifyFileName
	 * @return
	 */
	public FileUpload upload(File uploadify,String uploadifyFileName){
		return this.upload(uploadify, uploadifyFileName, null);
	}
	
	/**
	 * 上传文件 存放在KM_FILE文件夹
	 * @param uploadify
	 * @param uploadifyFileName
	 * @param path
	 * @return 
	 */
	public FileUpload kmUpload(File uploadify,String uploadifyFileName){
		FileUpload fileModel = null;
		String path = ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getKmFilePath().replaceAll("\\.\\.", ""));
		String extName = "";//扩展名
		String newFileName= "";//新文件名
		String rootPath = path.replaceAll("\\.\\.", "");
		String filePath = rootPath+File.separator+uploadifyFileName;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		File dirFile = new File(rootPath);
		if(!dirFile.isDirectory()){
			dirFile.mkdirs();
		}
		if(uploadifyFileName.lastIndexOf(".")>=0){
			extName = uploadifyFileName.substring(uploadifyFileName.lastIndexOf("."));
		}
		newFileName=UUID.randomUUID().toString().replaceAll("-", "")+extName;
		String savePath = path+newFileName; 
		File targetFile = new File(filePath);
		boolean flag = this.copy(uploadify, targetFile);
		if(flag){
			fileModel = new FileUpload();
			fileModel.setFileId(UUID.randomUUID().toString().replaceAll("-", ""));
			fileModel.setFileUrl(savePath); 
			fileModel.setFileSaveName(newFileName); 
			fileModel.setFileSrcName(uploadifyFileName);
			fileModel.setUploadTime(sf.format(new Date())); 
		}
		return fileModel;
	}
	
	/**
	 * 上传文件 存放在KM_FILE文件夹
	 * @param uploadify
	 * @param uploadifyFileName
	 * @param path
	 * @return 
	 */
	public FileUpload emailUpload(File uploadify,String uploadifyFileName){
		FileUpload fileModel = null;
		StringBuffer pathname = new StringBuffer();
		String path = ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getEmailFilePath().replaceAll("\\.\\.", ""));
		String extName = "";//扩展名
		String newFileName= "";//新文件名 
		String rootPath = path.replaceAll("\\.\\.", "");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		File dirFile = new File(rootPath);
		if(!dirFile.isDirectory()){
			dirFile.mkdirs();
		}
		if(uploadifyFileName.lastIndexOf(".")>=0){
			extName = uploadifyFileName.substring(uploadifyFileName.lastIndexOf("."));
		}
		//添加路径
		pathname.append(path);
		//添加年份
		Calendar now = Calendar.getInstance();
		pathname.append(File.separator).append(now.get(Calendar.YEAR)).append(File.separator).append((now.get(Calendar.MONTH) + 1)).append(File.separator).append(now.get(Calendar.DAY_OF_MONTH)).append(File.separator).append("ATTACH").append(File.separator);
		//添加文件名
		pathname.append(UUID.randomUUID().toString().replaceAll("-", "")).append(extName);
		String savePath = pathname.toString(); 
		File targetFile = new File(savePath);
		boolean flag = this.copy(uploadify, targetFile);
		if(flag){
			fileModel = new FileUpload();
			fileModel.setFileId(UUID.randomUUID().toString().replaceAll("-", ""));
			fileModel.setFileUrl(savePath); 
			fileModel.setFileSaveName(newFileName); 
			fileModel.setFileSrcName(uploadifyFileName);
			fileModel.setUploadTime(sf.format(new Date())); 
		}
		return fileModel;
	}
	
	/**
	 * 存放在KM_FILE/KNOW文件夹
	 * @param uploadify
	 * @param uploadifyFileName
	 * @return
	 */
	public FileUpload KmKnowUpload(File uploadify,String uploadifyFileName){
		FileUpload fileModel = null;
		  String extName = "";//扩展名
		  String newFileName= "";//新文件名
		  
		  String rootPath = ServletActionContext.getServletContext().getRealPath((SystemConfig._fileServerConf.getKmFilePath()+File.separator+"KNOW").replaceAll("\\.\\.", ""));
		 
		  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		  File dirFile = new File(rootPath);
		  if(!dirFile.isDirectory()){
			  dirFile.mkdirs();
		  }
		  if(uploadifyFileName.lastIndexOf(".")>=0){
			   extName = uploadifyFileName.substring(uploadifyFileName.lastIndexOf("."));
		  }
		  newFileName=UUID.randomUUID().toString().replaceAll("-", "")+extName;
		  String filePath = rootPath + File.separator + newFileName; 
		  String savePath = SystemConfig._fileServerConf.getKmFilePath() + File.separator+"KNOW"+File.separator +newFileName;
		  File targetFile = new File(filePath);
		  boolean flag = this.copy(uploadify, targetFile);
		  if(flag){
			  fileModel = new FileUpload();
			  fileModel.setFileId(UUID.randomUUID().toString().replaceAll("-", ""));
			  fileModel.setFileUrl(savePath);
			  fileModel.setFileSaveName(newFileName);
			  fileModel.setFileSrcName(uploadifyFileName);
			  fileModel.setUploadTime(sf.format(new Date()));
		  }
		  return fileModel;
	}
	/**
	 * 保存文件到km_file/DOCUMENT
	 * @param uploadify
	 * @param uploadifyFileName
	 * @return
	 */
	public FileUpload KmDocumentUpload(File uploadify,String uploadifyFileName){
		FileUpload fileModel = null;
		  String extName = "";//扩展名
		  String newFileName= "";//新文件名
		  
		  String rootPath = ServletActionContext.getServletContext().getRealPath((SystemConfig._fileServerConf.getKmFilePath()+File.separator+"DOCUMENT").replaceAll("\\.\\.", ""));
		 
		  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		  File dirFile = new File(rootPath);
		  if(!dirFile.isDirectory()){
			  dirFile.mkdirs();
		  }
		  if(uploadifyFileName.lastIndexOf(".")>=0){
			   extName = uploadifyFileName.substring(uploadifyFileName.lastIndexOf("."));
		  }
		  newFileName=UUID.randomUUID().toString().replaceAll("-", "")+extName;
		  String filePath = rootPath + File.separator + newFileName; 
		  String savePath = SystemConfig._fileServerConf.getKmFilePath() + File.separator+"DOCUMENT"+File.separator +newFileName;
		  File targetFile = new File(filePath);
		  boolean flag = this.copy(uploadify, targetFile);
		  if(flag){
			  fileModel = new FileUpload();
			  fileModel.setFileId(UUID.randomUUID().toString().replaceAll("-", ""));
			  fileModel.setFileUrl(savePath);
			  fileModel.setFileSaveName(newFileName);
			  fileModel.setFileSrcName(uploadifyFileName);
			  fileModel.setUploadTime(sf.format(new Date()));
		  }
		  return fileModel;
	}
	/**
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	private boolean copy(File src, File dst) {
		String path = dst.getParent();
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst),BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
	        int offset = 0;
	        while ((offset = in.read(buffer, 0, buffer.length)) != -1) {
	            out.write(buffer, 0, offset);
	        }
			out.flush();
		} catch (Exception e) {
			logger.error(e,e); 
			return false;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
		return true;
	} 
	/**
	 * 删除文件
	 * @param model
	 * @return
	 */
	public boolean deleteFile(FileUpload model){
		if(model!=null){
			String rootPath = ServletActionContext.getServletContext().getRealPath("");
			String fileUrl = model.getFileUrl();
			if(fileUrl!=null){
				String savePath ="";
				File file = new  File(fileUrl);
				if(file!=null&&file.isFile()&&file.exists()){
					file.delete(); 
				}else{
					savePath = rootPath+File.separator+fileUrl.replaceAll("\\.\\.", "");
					 file=new File(savePath);  
				    if(file.exists()&&file.isFile()){  
				        file.delete();
				    }
				}
				
			}
		}
	    return true;
	}
}
