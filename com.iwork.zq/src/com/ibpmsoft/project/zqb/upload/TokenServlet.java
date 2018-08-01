package com.ibpmsoft.project.zqb.upload;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.StringUtil;

public class TokenServlet extends HttpServlet
{
  private static final long serialVersionUID = 2650340991003623753L;
  static final String FILE_NAME_FIELD = "name";
  static final String FILE_SIZE_FIELD = "size";
  static final String TOKEN_FIELD = "token";
  static final String SERVER_FIELD = "server";
  static final String SUCCESS = "success";
  static final String MESSAGE = "message";

  public void init()
    throws ServletException
  {
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    String name = req.getParameter("name");
    String illegalChar = "";
    String fileName = req.getParameter("name");
    boolean success=true;
    FileUploadUtil file = new FileUploadUtil();
	String flag=file.uploadFile(req, resp);
	if(!flag.equals("true")){
		success=false;
	}
    int countStr = StringUtil.countStr(name,".");
	if(countStr>=2){
		illegalChar+=(fileName+":包含多个.");
		success=false;
	}
	if(StringUtil.mathcer("[@$%\\/]",name)){
		success=false;
	}
	if(name!=null){
		if(!StringUtil.validata(FileUtil.getFileExt(name))){
			illegalChar+=(fileName+":不支持的文件格式");
			success=false;
		}
	}else{
		success=false;
	}
	int lastIndexOf = fileName.lastIndexOf(".");
    if(fileName.substring(0,lastIndexOf).length()>60){
			
			illegalChar+=(fileName+":文件名长度不能超过60");
			success=false;
		
	    }
	if(success){
		String size = req.getParameter("size");
		String token=TokenUtil.getNewToken(name, size);
		PrintWriter writer = resp.getWriter();
		JSONObject json = new JSONObject();
		try {
			json.put("token", token);
			if (Configurations.isCrossed())
				json.put("server", Configurations.getCrossServer());
			json.put("success", true);
			json.put("message", "");
		}
		catch (JSONException e)
		{
		}
		writer.write(json.toString());
	}else{
		PrintWriter writer = resp.getWriter();
		String token = TokenUtil.generateToken(name, "0");
		JSONObject json = new JSONObject();
		try {
			json.put("token", token);
			if (Configurations.isCrossed())
				json.put("server", Configurations.getCrossServer());
			json.put("success", false);
			json.put("message", "");
			json.put("illegal", illegalChar);
		}
		catch (JSONException e)
		{
		}
		writer.write(json.toString());
	}
  }

  protected void doHead(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    super.doHead(req, resp);
  }

  public void destroy()
  {
    super.destroy();
  }
}