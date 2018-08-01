package com.ibpmsoft.project.zqb.upload;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;

import com.iwork.commons.util.StringUtil;
import com.iwork.core.upload.model.FileUpload;
public class FileUploadUtil extends HttpServlet {
	
	 //记录各个文件头信息及对应的文件类型
    public static Map<String, String> mFileTypes = new HashMap<String, String>();

    static {
      
        mFileTypes.put("text/plain",".txt");
        mFileTypes.put("application/vnd.ms-excel", ".xls");
        mFileTypes.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");
        mFileTypes.put("application/msword", ".doc");
        mFileTypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");
        mFileTypes.put("application/pdf", ".pdf");
        mFileTypes.put("application/vnd.ms-powerpoint", ".ppt");
        mFileTypes.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx");
        mFileTypes.put("application/zip", ".zip");
        mFileTypes.put("application/x-rar", ".rar");
        mFileTypes.put("image/bmp", ".bmp");    
        mFileTypes.put("image/jpeg", ".jpg");
        mFileTypes.put("image/jpeg", ".jpeg");
        mFileTypes.put("image/png", ".png");
        mFileTypes.put("image/gif", ".gif");
        mFileTypes.put("application/xml", ".xml");
    }
    /**
     * 文件上传
     * 
     * @param request
     * @return infos 
     * @throws Exception 
     */
    public String uploadFile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	String fileName = req.getParameter("name");
        String infos = "true";
        //验证文件名是否含有非法字符
        infos = this.validateFields(fileName);
      
        if (infos.equals("true")) {
        	//验证后缀名
        	infos  = this.initFields(fileName);
        }
        return infos;
    }
    public String uploadname(String fileName ) throws Exception {
        String infos = "true";
        //验证文件名是否含有非法字符
        infos = this.validateFields(fileName);
      
        if (infos.equals("true")) {
        	//验证后缀名
        	infos  = this.initFields(fileName);
        }
        return infos;
    }
    /**
     * 验证文件名是否含有非法字符
     * 
     * @param request
     */
    private String validateFields(String fileName) {
        String errorInfo = "true";
       
        if(StringUtil.mathcer("[\\/:*?\"<>|]",fileName)){
        	errorInfo="文件名含有特殊字符";
		}
        if(fileName.equals("crossdomain.xml")){
        	errorInfo="非法文件";
        }
      /*  int count=0;
        count=fileName.toLowerCase().indexOf("0x00");
        if(count!=-1){
        	errorInfo="文件名含有特殊字符";
        }*/
        return errorInfo;
    }

    /**
     * 验证文件后缀名
     * 
     * @param request
     * @param maxSize
     * @return
     */
    private String initFields(String fileName) {
    	String errorInfo = "true";
    	String str="";
    	int pos = 0;
    	int count=0;
	    pos = fileName.indexOf(".");
	    if(pos != -1)
	    	str= fileName.substring(pos + 1, fileName.length());
	    str="."+str.toLowerCase();
        String[] errorType = {".txt",".xls",".xlsx",".doc",".docx",".pdf",".ppt",".pptx",".zip",".rar",".bmp",".jpg",".jpeg",".png",".gif",".xml"};
        for (int i = 0; i < errorType.length; i++) {
			if(str.equals(errorType[i])){
				count++;
				break;
			}
		}
        if(count==0){
        	errorInfo="文件格式不正确";
        }
        return errorInfo;
    }
    /**
     * 验证mime类型
     * @param filemodel
     * @param files
     * @return
     * @throws IOException
     * @throws FileUploadException
     */
    public String initFields(FileUpload filemodel,File files) throws IOException, FileUploadException {
    	String errorInfo = "true";
    	String type = null;
    	String attachName=filemodel.getFileSaveName();
    	String filename=filemodel.getFileUrl();
    	Path path = Paths.get(filename);
    	
		try {
			type = Files.probeContentType(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 String sname = attachName.substring(attachName.lastIndexOf("."));
		 String bname = mFileTypes.get(type);
		if(!bname.equals(sname)){
			 File file=files;
	         if(file.exists()){
	        	 file.delete();
	         }
			errorInfo="非法文件";
		}
		return errorInfo;
    }
	public String delFile(File files) {
		File file = files;
		if (file.exists()) {
			file.delete();
		}
		return null;
	}
}
