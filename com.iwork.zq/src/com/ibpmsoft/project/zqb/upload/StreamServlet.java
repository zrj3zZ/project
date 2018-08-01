package com.ibpmsoft.project.zqb.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.StringUtil;
import com.iwork.core.upload.model.FileUpload;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.upload.IoUtil;
import com.ibpmsoft.project.zqb.util.ConfigUtil;

public class StreamServlet extends HttpServlet {
	private static final long serialVersionUID = -8619685235661387895L;
	static final int BUFFER_LENGTH = 10240;
	static final String START_FIELD = "start";
	public static final String CONTENT_RANGE_HEADER = "content-range";
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	//private static Logger logger = Logger.getLogger(StreamServlet.class);
	public void init() throws ServletException {
	}
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter)==null?"":config.get(parameter);
		}
		return result;
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doOptions(req, resp);
		String illegalChar = "";
		boolean success = true;
		String fileName = req.getParameter("name");
		int countStr = StringUtil.countStr(fileName,".");
		if(countStr>=2){
			illegalChar+=(fileName+":包含多个.");
			success=false;
		}
		if(StringUtil.mathcer("[@$%\\/]",fileName)){
			int lastPoint = fileName.lastIndexOf(".");
			String nakedName = fileName.substring(0, lastPoint);
			String[] nakedNameArr = nakedName.split("");
			for (int i = 0; i < nakedNameArr.length; i++) {
				if("[@$%\\/]".contains(nakedNameArr[i])){
					if(illegalChar.equals("")){
						illegalChar+=(fileName+":");
					}
					illegalChar+=nakedNameArr[i];
				}
			}
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
		int lastIndexOf = fileName.lastIndexOf(".");
	       if(fileName.substring(0,lastIndexOf).length()>60){
				
				illegalChar+=(fileName+":文件名长度不能超过60");
				success=false;
			
		    }
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		JSONObject json = new JSONObject();
		if(success){
			String token = req.getParameter("token");
			String size = req.getParameter("size");
			long start = 0L;
			String message = "";
			try {
				String realPath = this
						.getServletConfig()
						.getServletContext()
						.getRealPath(
								SystemConfig._fileServerConf.getFormFilePath()
								.replace("/", File.separator));
				File f = IoUtil.getTokenedFile(token, realPath);
				start = f.length();
				if ((token.endsWith("_0")) && ("0".equals(size)) && (0L == start)) {
					f.renameTo(IoUtil.getFile(fileName, realPath));
				}
			} catch (FileNotFoundException fne) {
				message = "Error: " + fne.getMessage();
				success = false;
			} finally {
				try {
					if (success)
						json.put("start", start);
					json.put("success", success);
					json.put("message", message);
				} catch (JSONException e) {
				}
				writer.write(json.toString());
				IoUtil.close(writer);
			}
		}else{
			try {
				json.put("success", success);
				json.put("message", "1");
				json.put("illegal", illegalChar);
			} catch (JSONException e) {
			}
			writer.write(json.toString());
			IoUtil.close(writer);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doOptions(req, resp);
		String illegalChar = "";
		boolean success = true;
		String fileName = req.getParameter("name");
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
		int lastIndexOf = fileName.lastIndexOf(".");
	    if(fileName.substring(0,lastIndexOf).length()>60){
				
				illegalChar+=(fileName+":文件名长度不能超过60");
				success=false;
			
		    }
		if(success){
			String token = req.getParameter("token");
			Range range = IoUtil.parseRange(req);
			OutputStream out = null;
			InputStream content = null;
			PrintWriter writer = resp.getWriter();
			JSONObject json = new JSONObject();
			long start = 0L;
			String message = "";
			FileUpload filemodel = null;
			String realPath = this
					.getServletConfig()
					.getServletContext()
					.getRealPath(
							SystemConfig._fileServerConf.getFormFilePath().replace(
									"/", File.separator));
			File f = IoUtil.getTokenedFile(token, realPath);
			try {
				if (f.length() != range.getFrom()) {
					throw new StreamException(
							StreamException.ERROR_FILE_RANGE_START);
				}
				//xlj tomcat服务端是win8.1，ie11时会出现文件上传失败的错误，可以用注释的代码解决。此处先不考虑该问题，暂时注掉
				/*try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					logger.error(e);
				}*/
				out = new FileOutputStream(f, true);
				content = req.getInputStream();
				int read = 0;
				byte[] bytes = new byte[10240];
				while ((read = content.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				start = f.length();
			} catch (StreamException se) {
				File dst;
				success = StreamException.ERROR_FILE_RANGE_START == se.getCode();
				message = "Code: " + se.getCode();
			} catch (FileNotFoundException fne) {
				File dst;
				message = "Code: " + StreamException.ERROR_FILE_NOT_EXIST;
				success = false;
			} catch (IOException io) {
				File dst;
				message = "IO Error: " + io.getMessage();
				success = false;
			} finally {
				File dst;
				IoUtil.close(out);
				IoUtil.close(content);
				try {
					if (range.getSize() == start) {
						dst = IoUtil.getFile(fileName, realPath);
						dst.delete();
						f.renameTo(dst);
						String name = dst.getName().substring(0,dst.getName().lastIndexOf("."));
						filemodel = IoUtil.getFileUpload(name);
						if (Configurations.isDeleteFinished()){
							dst.delete();
						}
					}
					if (success){
						json.put("start", start);
					}
					if (filemodel != null) {
						String uuid = filemodel.getFileId();
						String url = filemodel.getFileUrl();
						json.put("flag", true);
						json.put("uuid", uuid);
						json.put("url", url);
					}
					json.put("success", success);
					json.put("message", message);
				} catch (Exception e) {
					//logger.error(e);
				}
				Date d=new Date();
			    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");//转换格式
				String zqserver = getConfigUUID("zqServer")==null?"":getConfigUUID("zqServer");
				 String fileurl=realPath+File.separator+sdf.format(d)+File.separator+filemodel.getFileSaveName();
				if(zqserver.equals("gxzq")){
					Fileupload fileupload= new Fileupload();
					fileupload.fileScann(fileName, fileurl);
				}
				writer.write(json.toString());
				IoUtil.close(writer);
			}
		}else{
			PrintWriter writer = resp.getWriter();
			JSONObject json = new JSONObject();
			try {
				json.put("flag", false);
				json.put("success", success);
				json.put("message", "1");
				json.put("illegal", illegalChar);
			} catch (JSONException e) {
				//logger.error(e);
			}
			writer.write(json.toString());
			IoUtil.close(writer);
		}
		
	}

	protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setHeader("Access-Control-Allow-Headers",
				"Content-Range,Content-Type");
		resp.setHeader("Access-Control-Allow-Origin",
				Configurations.getCrossOrigins());
		resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
	}

	public void destroy() {
		super.destroy();
	}
}