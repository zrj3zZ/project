package com.iwork.plugs.email.util;

import java.io.File;

import org.apache.struts2.ServletActionContext;

import com.ibm.icu.util.Calendar;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.FileUtil;
import com.iwork.plugs.email.cache.EmailCache;
import org.apache.log4j.Logger;
public class EmailContentUtil {
	private static Logger logger = Logger.getLogger(EmailContentUtil.class);

	 private static Object lock = new Object();  
	 private static EmailContentUtil instance; 
	 public static EmailContentUtil getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new EmailContentUtil();  
	                }
	            }  
	        }  
	        return instance;  
	 }
	 
	 /**
	  * 
	  * @param key
	  * @param text
	  * @return
	  */
	 public String saveText(String key,String content){
		 boolean flag = true;
		 String path = "";
		 try{
			StringBuffer pathname = new StringBuffer();
			String rootPath = ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getEmailFilePath().replaceAll("\\.\\.", ""))+File.separator;
			Calendar now = Calendar.getInstance();
			 String fileName = key;
			pathname.append(rootPath).append(now.get(Calendar.YEAR)).append(File.separator).append((now.get(Calendar.MONTH) + 1)).append(File.separator).append(now.get(Calendar.DAY_OF_MONTH)).append(File.separator).append("CONTENT");
			pathname.append(File.separator).append(fileName);
			 FileUtil.delFile(pathname.toString());
			 FileUtil.writeFile(pathname.toString(), content);
			 EmailCache.getInstance().putBigTxt(key, content);
			 path = pathname.toString();
		 }catch(Exception e){logger.error(e,e);
			 flag =false;
		 }
		 return path;
	 }
	 
	 /**
	  * 根据类型及存储key获取文本内容
	  * @param type
	  * @param key
	  * @return
	  */
	 public String getText(String key,String path){
		 //判断缓存中是否存在
		String content =  EmailCache.getInstance().getBigTxt(key);
		if(content==null||content.equals("")){
			 String fileName = key;
			 content = FileUtil.readFile(path);
			 if(content==null){
				 content = "";
				 EmailCache.getInstance().putBigTxt(key, content);
			 }else{
				 EmailCache.getInstance().putBigTxt(key, content);
			 }
		}
		 return content;
	 }
	 /**
	  * 根据类型及存储key获取文本内容
	  * @param type
	  * @param key
	  * @return
	  */
	 public void removeText(String key,String path){
		 	//判断缓存中是否存在
			 FileUtil.delFile(path);
			 EmailCache.getInstance().remove(key);
	 }
   
}
