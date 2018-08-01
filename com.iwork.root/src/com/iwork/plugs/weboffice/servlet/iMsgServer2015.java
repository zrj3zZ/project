package com.iwork.plugs.weboffice.servlet;

import org.apache.log4j.Logger;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.DiskFileUpload;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.sdk.FileUploadAPI;
/**
 * 
 * @author 陈益特
 *
 */
public class iMsgServer2015 {
    private Hashtable<String, String> saveFormParam = new Hashtable<String, String>();  //保存form表单数据
    private Hashtable<String, String> sendFormParam = new Hashtable<String, String>();  //保存form表单数据
	private InputStream fileContentStream;
	private String fileName = "";
	private byte[] mFileBody = null;
	private boolean isLoadFile = false;
	private String sendType ="";
	private static Logger logger = Logger.getLogger(iMsgServer2015.class);
	private static final String MsgError = "404"; //设置常量404，说明没有找到对应的文档
	
	
	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	/**
	 * @throws FileUploadException 
	 * @throws Exception 
	 * @deprecated:后台类解析接口
	 * @time:2015-01-09
	 */
	public void Load(HttpServletRequest request) throws FileUploadException, IOException{
		 request.setCharacterEncoding("gb2312");
		 //DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
		 //ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
		 DefaultFileItemFactory diskFileItemFactory = new DefaultFileItemFactory();
		DiskFileUpload fileUpload = new DiskFileUpload(diskFileItemFactory);
		List fileList =  fileUpload.parseRequest(request);
		 //List<FileItem> fileList =  fileUpload.parseRequest(request);
		 //Iterator iter = fileList.iterator();
		 if (fileList != null && fileList.size() > 0) {
			    for (int i=0; i<fileList.size(); i++) {
			        FileItem item = (FileItem)fileList.get(i);
			        if(item.isFormField()) {
			        	 processFormField(item);
			        }else{
			        	processUploadedFile(item);
			        }
			    }
			}
	/*	 while (iter.hasNext()) {
		 	 FileItem item = iter.next();
			 if (item.isFormField()) {
			    processFormField(item);
			 }else {
			    processUploadedFile(item);
			 }
		 }*/
	}
   
	/**
	 * @deprecated：解析表达数据
	 * @param item:表单数据
	 * @throws UnsupportedEncodingException 
	 * @time:2015-01-09
	 */
	public void processFormField(FileItem item) throws UnsupportedEncodingException{
		String fieldName = item.getFieldName();
		String fieldValue = "";
		fieldValue = item.getString("utf-8");
		if(this.sendType.equalsIgnoreCase("JSON")){
			JSONObject json = JSONObject.fromObject(fieldValue);
			Iterator iter = json.keySet().iterator();   
			 while (iter.hasNext()) {   
			   fieldName = (String) iter.next();   
			   fieldValue = json.getString(fieldName); 
			   saveFormParam.put(fieldName, fieldValue);
			}
			 return;
		}
		saveFormParam.put(fieldName, fieldValue);
	}
	
	
	/**
	 * @deprecated：解析文档数据
	 * @param item:文档数据
	 * @throws Exception 
	 * @throws UnsupportedEncodingException
	 * @time:2015-01-09 
	 */	
	public void processUploadedFile(FileItem item) throws IOException{
		fileName = item.getName();
		if(fileName.indexOf("/")>=0){
			fileName = fileName.substring(fileName.lastIndexOf("/")+1);	
		}else if(fileName.indexOf("\\")>=0){
			fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
		}
	    fileContentStream =  item.getInputStream();
	
	}
	/**
	 * @deprecated：解析文档数据
	 * @param fieldName:参数名称
	 * @return：参数对于的值
	 * @time:2015-01-09
	 */	
	public String GetMsgByName(String fieldName){
		return saveFormParam.get(fieldName);
	}

	/**
	 * 清除所有SetMsgByName所有内容
	 * @time:2015-01-09
	 */
	public void MsgTextClear(){
		saveFormParam.clear();
	}
	
	
	public byte[] MsgFileBody() throws IOException{
		 mFileBody = null;
		 isLoadFile = false;
		 ByteArrayOutputStream output = new ByteArrayOutputStream();
		 byte[] buffer = new byte[4096];
		 int n = 0;
		 while (-1 != (n = fileContentStream.read(buffer))) {
		        output.write(buffer, 0, n);
		 }
	    mFileBody = output.toByteArray();
		return mFileBody;
	}
	
	
	/** 
     * 把字节数组保存为一个文件 
     *  
     * @param b 
     * @param outputFile 
     * @return 
     */  
    public  boolean MsgFileSave(String outputFile,String mRecordID,String filename) {  
    	 boolean mResult = false;
    	 try {
    		
    		 byte[] docFileBody  =  this.MsgFileBody();
             this.MsgTextClear();    
             if(docFileBody!=null){ 
		 	       BufferedOutputStream stream = null;
		 	      FileOutputStream fstream = null;
		 	       File file = null;
		 	      String f = SystemConfig._fileServerConf.getPath().replace("/",File.separator);
		 	      String filefullpath =outputFile;
		 	       try{ 
		 	    	    file = new File(filefullpath); 
		 	    	    fstream = new FileOutputStream(file);
		 	    	   stream = new BufferedOutputStream(fstream);
		 	    	  stream.write(docFileBody);
		 	    	 stream.flush();
		 	       }catch(Exception e){
		 	    	   logger.error(e,e);
		 	       }finally{
		 	    	   if(stream!=null){
		 	    		  try {
							stream.close();
						} catch (Exception e) {logger.error(e,e);
						}
		 	    	   }
		 	    	   if(fstream!=null){
		 	    		   try {
		 	    			  fstream.close();
		 	    		   } catch (Exception e) {logger.error(e,e);
		 	    		   }
		 	    	   }
		 	       }
		 	       if(file!=null){
		 	    	  file = new File(filefullpath);
		 	    	  if(file!=null){
		 	    		  String fileDirfullpath = SystemConfig._fileServerConf.getPath().replace("/",File.separator);
		 	    		 FileUploadAPI.getInstance().saveForPath(file, fileDirfullpath,mRecordID);   
		 	    	  }
		 	    	  
		 	    	 mResult = true;
		 	       }
	    	 }
    	 } catch (Exception e) {
 			logger.error(e,e);
 			return false;
 		}  
    	 return mResult;
    	
    }  
	

    
    public boolean MsgFileLoad(String mFilePath,String mRecordID) throws IOException{
    	 FileUpload fileUpload = FileUploadAPI.getInstance().getFileUpload(mRecordID); 
	   	  if(fileUpload!=null){
	   		 String fileurl =  fileUpload.getFileUrl();
	   		 String filepath = mFilePath+File.separator+fileurl;
	   		File file = new File(filepath);
	    	if(file.exists()){
	    	fileContentStream = new FileInputStream(file);
	    	MsgFileBody();
	    	}else{
	    		mFileBody = new byte[0];
	    	}
	    	isLoadFile = true;
	   	  }
    	return true;
    }
    
    
    /**
     * @deprecated:将文件的二进制数据设置到信息包中
     * @param response
     * @throws IOException
     */
    public void Send(HttpServletResponse response) throws IOException{
    	try{
	    	if(isLoadFile){
	    		    if(mFileBody.length != 0){
			    		response.setCharacterEncoding("utf-8");
				   		response.setContentType("application/x-msdownload;charset=utf-8");
				   		response.setContentLength(mFileBody.length);
				   	    response.setHeader("Content-Disposition","attachment;filename=");
				   	    response.getOutputStream().write(mFileBody,0,mFileBody.length);
	    		    }else{
	    		    	response.setHeader("MsgError",iMsgServer2015.MsgError); 
	    		    }
	    	}    	
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
    	}catch(Exception e){
    		logger.error(e,e);
    	}
    } 	
}
