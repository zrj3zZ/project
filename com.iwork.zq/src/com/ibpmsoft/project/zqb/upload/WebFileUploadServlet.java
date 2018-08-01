package com.ibpmsoft.project.zqb.upload;



import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.StringUtil;
import com.iwork.core.upload.model.FileUpload;

public class WebFileUploadServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(WebFileUploadServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doGet(request, response);
		doPost(request, response);

	}

	@SuppressWarnings("resource")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			JSONObject json = new JSONObject();
			String savePath =  this.getServletConfig().getServletContext()
					.getRealPath(SystemConfig._fileServerConf.getFormFilePath().replace("/", File.separator));

			String realPath=savePath;
			 Date d = new Date();  
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");  
			  String dateNowStr = sdf.format(d);  
			  savePath=savePath+ "\\" + dateNowStr + "\\";
			String action = request.getParameter("action");
			String exts = request.getParameter("exts");
			
			
			String fileName = request.getParameter("filenames");
			//文件名验证
			FileUpload filemodel = null;
			
			
			if (action.equals("mergeChunks")) {
				boolean success = true;
				String illegalChar = "";
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
								illegalChar+=(fileName+"包含非法字符:");
							}
							illegalChar+=nakedNameArr[i];
						}
					}
					success=false;
					//illegalChar+=fileName+":包含非法字符";
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
					try {
						
						
						// 合并文件
						// 需要合并的文件的目录标记
						String fileMd5 = request.getParameter("fileMd5");
			
						// 读取目录里的所有文件
						File f = new File(savePath + "/" + fileMd5);
						File[] fileArray = f.listFiles(new FileFilter() {
							// 排除目录只要文件
							public boolean accept(File pathname) {
								if (pathname.isDirectory()) {
									return false;
								}
								return true;
							}
						});
						if(fileArray!=null && fileArray.length>0){
//						json.put("url", url);
							// 转成集合，便于排序
							List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
							Collections.sort(fileList, new Comparator<File>() {
								public int compare(File o1, File o2) {
									if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2
											.getName())) {
										return -1;
									}
									return 1;
								}
							});
							// UUID.randomUUID().toString()-->随机名
							File outputFile = new File(savePath + "/" + fileMd5 + "."+exts);
							// 创建文件
							outputFile.createNewFile();
							// 输出流
							FileChannel outChnnel = new FileOutputStream(outputFile).getChannel();
							// 合并
							FileChannel inChannel;
							for (File file : fileList) {
								inChannel = new FileInputStream(file).getChannel();
								inChannel.transferTo(0, inChannel.size(), outChnnel);
								inChannel.close();
								// 删除分片
								file.delete();
							}
							outChnnel.close();
							// 清除文件夹
							File tempFile = new File(savePath + "/" + fileMd5);
							if (tempFile.isDirectory() && tempFile.exists()) {
								tempFile.delete();
							}
							IoUtil.getFile(fileName, realPath,fileMd5);
							json.put("success", "true");
							json.put("uuid", fileMd5);
							json.put("fileName", fileName);
							try {
								filemodel = IoUtil.getFileUpload(fileMd5);
							} catch (Exception e) {
								json.put("success", "false");
								json.put("illegalChar", fileName+"上传异常，请重试");
								logger.error(e,e);
							}
							if (filemodel != null) {
								String url = filemodel.getFileUrl();
								json.put("url", url);
							}
						}else{
							json.put("success", "wjyc");
							json.put("uuid", fileMd5);
							json.put("fileName", fileName);
							json.put("illegalChar", fileName+"上传异常，请重试");
						}
						
					} catch (JSONException e) {
						try {
							json.put("success", "false");
						} catch (Exception e1) {
							logger.error(e1,e1);
						}
					}
				}else{
					try {
						json.put("success", "fname");
						json.put("illegalChar", illegalChar);
					} catch (Exception e) {
						logger.error(e,e);
					}
				}
				
			} else if (action.equals("checkChunk")) {
				
				try {
					// 检查当前分块是否上传成功
					String fileMd5 = request.getParameter("fileMd5");
					String chunk = request.getParameter("chunk");
					String chunkSize = request.getParameter("chunkSize");

					File checkFile = new File(savePath + "/" + fileMd5 + "/" + chunk);

					response.setContentType("text/html;charset=utf-8");
					// 检查文件是否存在，且大小是否一致
					if (checkFile.exists()
							&& checkFile.length() == Integer.parseInt(chunkSize)) {
						// 上传过
						response.getWriter().write("{\"ifExist\":1}");
					} else {
						// 没有上传过
						response.getWriter().write("{\"ifExist\":0}");
					}
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
			writer.write(json.toString());
			IoUtil.close(writer);
		} catch (Exception e) {
			logger.error(e,e);
		}
	}

}