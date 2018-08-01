package com.ibpmsoft.project.zqb.upload;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.FileUtil;
import com.iwork.core.upload.dao.FileUploadDAO;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.util.FileUploadUtil;
import com.iwork.core.util.SpringBeanUtil;

public class IoUtil
{
  static final Pattern RANGE_PATTERN = Pattern.compile("bytes \\d+-\\d+/\\d+");
  private static FileUploadDAO fileUploadDao;
  public static File getFile(String filename, String path) throws IOException {
		if ((filename == null) || (filename.isEmpty()))
			return null;
		FileUpload fileModel = null;
		String extName = "";//扩展名
		String newFileName= "";//新文件名
		Date data = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		String format = sd.format(data);
		String rootPath = path + File.separator + format;
		String uuid =  UUID.randomUUID().toString().replaceAll("-", ""); 
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		File dirFile = new File(rootPath);
		if(!dirFile.isDirectory()){
			dirFile.mkdirs();
		}
		if(filename!=null){
			extName = FileUtil.getFileExt(filename);
		}
		newFileName=uuid+"."+extName;
		String filePath = SystemConfig._fileServerConf.getFormFilePath().replaceAll("\\.\\.", "") + File.separator+format + File.separator + newFileName; 
		filePath = filePath.replace("\\","/");
		String savePath = rootPath+File.separator+newFileName;
		File f = new File(savePath);
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		if (!f.exists()) {
			f.createNewFile();
		}
		fileModel = new FileUpload();
		fileModel.setFileId(uuid); 
		fileModel.setFileUrl(filePath);
		fileModel.setFileSaveName(newFileName);
		fileModel.setFileSrcName(filename);
		fileModel.setUploadTime(sf.format(new Date()));
		FileUploadDAO uploadifyDAO = (FileUploadDAO) SpringBeanUtil.getBean("uploadifyDAO");
		FileUpload fu =uploadifyDAO.save(fileModel);
		if(fu==null){
			FileUploadUtil fileUploadUtil=new FileUploadUtil();
			fileUploadUtil.deleteFile(fileModel);
		}
		return f;
	}
  public static File getFile(String filename, String path,String uuid) throws IOException {
		if ((filename == null) || (filename.isEmpty()))
			return null;
		FileUpload fileModel = null;
		String extName = "";//扩展名
		String newFileName= "";//新文件名
		Date data = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		String format = sd.format(data);
		String rootPath = path + File.separator + format;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		File dirFile = new File(rootPath);
		if(!dirFile.isDirectory()){
			dirFile.mkdirs();
		}
		if(filename!=null){
			extName = FileUtil.getFileExt(filename);
		}
		newFileName=uuid+"."+extName;
		String filePath = SystemConfig._fileServerConf.getFormFilePath().replaceAll("\\.\\.", "") + File.separator+format + File.separator + newFileName; 
		filePath = filePath.replace("\\","/");
		String savePath = rootPath+File.separator+newFileName;
		File f = new File(savePath);
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		if (!f.exists()) {
			f.createNewFile();
		}
		fileModel = new FileUpload();
		fileModel.setFileId(uuid); 
		fileModel.setFileUrl(filePath);
		fileModel.setFileSaveName(newFileName);
		fileModel.setFileSrcName(filename);
		fileModel.setUploadTime(sf.format(new Date()));
		FileUploadDAO uploadifyDAO = (FileUploadDAO) SpringBeanUtil.getBean("uploadifyDAO");
		FileUpload fu =uploadifyDAO.save(fileModel);
		if(fu==null){
			FileUploadUtil fileUploadUtil=new FileUploadUtil();
			fileUploadUtil.deleteFile(fileModel);
		}
		return f;
	}
  
  public static FileUpload getFileUpload(Serializable id) {
		if(fileUploadDao==null){
			fileUploadDao = (FileUploadDAO)SpringBeanUtil.getBean("uploadifyDAO");
		}
		return fileUploadDao.getFileUpload(FileUpload.class, id);
	}

  public static File getTokenedFile(String key, String path)
			throws IOException {
		if ((key == null) || (key.isEmpty())) {
			return null;
		}
		Date data = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		String format = sd.format(data);
		File f = new File(path + File.separator + format + File.separator + key);
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		if (!f.exists()) {
			f.createNewFile();
		}
		return f;
	}

  public static void storeToken(String key) throws IOException {
    if ((key == null) || (key.isEmpty())) {
      return;
    }
    File f = new File(Configurations.getFileRepository() + File.separator + key);
    if (!f.getParentFile().exists())
      f.getParentFile().mkdirs();
    if (!f.exists())
      f.createNewFile();
  }

  public static void close(Closeable stream)
  {
    try
    {
      if (stream != null)
        stream.close();
    }
    catch (IOException e)
    {
    }
  }

  public static Range parseRange(HttpServletRequest req)
    throws IOException
  {
    String range = req.getHeader("content-range");
    Matcher m = RANGE_PATTERN.matcher(range);
    if (m.find()) {
      range = m.group().replace("bytes ", "");
      String[] rangeSize = range.split("/");
      String[] fromTo = rangeSize[0].split("-");

      long from = Long.parseLong(fromTo[0]);
      long to = Long.parseLong(fromTo[1]);
      long size = Long.parseLong(rangeSize[1]);

      return new Range(from, to, size);
    }
    throw new IOException("Illegal Access!");
  }

  public static HashMap streaming(InputStream in, String key, String fileName,
			String path) throws IOException {
		OutputStream out = null;
		File fc = getTokenedFile(key, path);
		try {
			out = new FileOutputStream(fc);

			int read = 0;
			byte[] bytes = new byte[10485760];
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
		} finally {
			close(out);
		}

		File dst;
		FileUpload fileModel = null;
		String extName = "";//扩展名
		String newFileName= "";//新文件名
		Date data = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		String format = sd.format(data);
		String rootPath = path + File.separator + format;
		String uuid =  UUID.randomUUID().toString().replaceAll("-", ""); 
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		File dirFile = new File(rootPath);
		if(!dirFile.isDirectory()){
			dirFile.mkdirs();
		}
		if(fileName!=null){
			extName = FileUtil.getFileExt(fileName);
		}
		newFileName=uuid+"."+extName;
		String filePath = SystemConfig._fileServerConf.getFormFilePath().replaceAll("\\.\\.", "") + File.separator+format + File.separator + newFileName; 
		filePath = filePath.replace("/",File.separator);
		String savePath = rootPath+File.separator+newFileName;
		File f = new File(savePath);
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		if (!f.exists()) {
			f.createNewFile();
		}
		fileModel = new FileUpload();
		fileModel.setFileId(uuid); 
		fileModel.setFileUrl(filePath);
		fileModel.setFileSaveName(newFileName);
		fileModel.setFileSrcName(fileName);
		fileModel.setUploadTime(sf.format(new Date()));
		FileUploadDAO uploadifyDAO = (FileUploadDAO) SpringBeanUtil.getBean("uploadifyDAO");
		FileUpload fu =uploadifyDAO.save(fileModel);
		if(fu==null){
			FileUploadUtil fileUploadUtil=new FileUploadUtil();
			fileUploadUtil.deleteFile(fileModel);
		}
		dst=f;
		dst.delete();
		fc.renameTo(dst);
		long length = getFiles(newFileName,path).length();
		HashMap hashMap=new HashMap();
		hashMap.put("uuid", uuid);
		hashMap.put("url", filePath);
		hashMap.put("length", length);
/*
		long length = 0;

		if (Configurations.isDeleteFinished()) {
			dst.delete();
		}*/

		return hashMap;
	}
  
  public static File getFiles(String filename,String path)
		    throws IOException
		  {
		    if ((filename == null) || (filename.isEmpty()))
		      return null;
		    Date data = new Date();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
			String format = sd.format(data);
			String rootPath = path + File.separator + format;
		    String name = filename.replaceAll("/", Matcher.quoteReplacement(File.separator));
		    File f = new File(rootPath + File.separator + name);
		    if (!f.getParentFile().exists())
		      f.getParentFile().mkdirs();
		    if (!f.exists()) {
		      f.createNewFile();
		    }
		    return f;
		  }
}
