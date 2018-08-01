package com.ibpmsoft.project.zqb.upload;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.StringUtil;
import com.iwork.core.upload.model.FileUpload;
public class FormDataServlet extends HttpServlet
{
	private static Logger logger = Logger.getLogger(FormDataServlet.class);
	private static final long serialVersionUID = -1905516389350395696L;
	static final String FILE_FIELD = "file";
	public static final int BUFFER_LENGTH = 10485760;
	static final int MAX_FILE_SIZE = 104857600;
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter)==null?"":config.get(parameter);
		}
		return result;
	}
  public void init()
    throws ServletException
  {
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
	  String a="";
	  resp.setContentType("text/html;charset=UTF-8");
    doOptions(req, resp);
    String illegalChar = "";
    req.setCharacterEncoding("utf8");

    PrintWriter writer = resp.getWriter();

    boolean isMultipart = ServletFileUpload.isMultipartContent(req);
    if (!isMultipart) {
      writer.println("ERROR: It's not Multipart form.");
      return;
    }
    JSONObject json = new JSONObject();
    long start = 0L;
    HashMap hashMap=new HashMap();
    boolean success = true;
    String message = "";
	String names ="";
    ServletFileUpload upload = new ServletFileUpload();
    InputStream in = null;
    String token = null;
    try {
    
      FileItemIterator iter = upload.getItemIterator(req);
      while (iter.hasNext()) {
        FileItemStream item = iter.next();
        String name = item.getFieldName();
        in = item.openStream();
        if (item.isFormField()) {
          String value = Streams.asString(in);
          if ("token".equals(name)) {
            token = value;
          }
        } else {
          String fileName = item.getName();
          names=fileName;
          FileUploadUtil file = new FileUploadUtil();          
          String flag=null;
          try
          {
          flag = file.uploadname(fileName);
          }catch(Exception e){}
          if(!flag.equals("true")){
        	  success=false;
          }
          int countStr = StringUtil.countStr(fileName,".");
	  	  if(countStr>=2){
	  		illegalChar+=(fileName+":包含多个.");
	  		  success=false;
	      }
          if(StringUtil.mathcer("[@$%\\/]",fileName)){
        	  success=false;
	      }
          if(fileName!=null){
  			if(!StringUtil.validata(FileUtil.getFileExt(fileName))){
  				illegalChar+=(fileName+":不支持的文件格式");
  				success=false;
  			}
  		  }else{
  			    success=false;
  		  }
          if(success){
        	  FileUpload filemodel = null;
        	  String realPath = this.getServletConfig().getServletContext().getRealPath(SystemConfig._fileServerConf.getFormFilePath().replace("/",File.separator));
        	 a=realPath;
        	  try {
				hashMap = IoUtil.streaming(in, token, fileName,realPath);
				  start=(Long) hashMap.get("length");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }
        }
      }
    } catch (FileUploadException fne) {
      success = false;
      message = "Error: " + fne.getLocalizedMessage();
    } finally {
      try {
        if (success)
          json.put("start", start);
        if (hashMap != null&&!hashMap.isEmpty()) {
        	 Date d=new Date();
			    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");//转换格式
				String zqserver = getConfigUUID("zqServer")==null?"":getConfigUUID("zqServer");
				 String fileurl=a+File.separator+sdf.format(d)+File.separator+ hashMap.get("url").toString().split("\\\\")[ hashMap.get("url").toString().split("\\\\").length-1];
				if(zqserver.equals("gxzq")){
					Fileupload fileupload= new Fileupload();
					fileupload.fileScann(names, fileurl);
				}
	          String uuid = hashMap.get("uuid").toString();
	          String url = hashMap.get("url").toString();
	          json.put("flag", true);
	          json.put("uuid", uuid);
	          json.put("url", url);
	    }else{
	    	json.put("flag", false);
	    }
        json.put("success", success);
        json.put("message", message);
        json.put("illegal", illegalChar);
      } catch (JSONException e) {
      }
      try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			logger.error(e,e);
		}
      writer.write(json.toString());
      IoUtil.close(in);
      IoUtil.close(writer);
    }
  }

  public void destroy()
  {
    super.destroy();
  }
}
