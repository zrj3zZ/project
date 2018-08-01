package com.ibpmsoft.project.zqb.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.app.conf.SystemConfig;


public class WebuploaderServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(WebuploaderServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload sfu = new ServletFileUpload(factory);
			sfu.setHeaderEncoding("utf-8");
			 Date d = new Date();  
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");  
			  String dateNowStr = sdf.format(d);  
			
			String savePath = this.getServletConfig().getServletContext()
					.getRealPath("");
			String folad = "uploads";
			savePath = savePath + "\\" + folad + "\\";
			savePath = this.getServletConfig().getServletContext()
					.getRealPath(SystemConfig._fileServerConf.getFormFilePath().replace("/", File.separator));
			savePath=savePath+ "\\" + dateNowStr + "\\";
			String fileMd5 = null;
			String chunk = null;

			try {
				List<FileItem> items = sfu.parseRequest(request);
			
				for (FileItem item : items) {
					if (item.isFormField()) {
						String fieldName = item.getFieldName();
						if (fieldName.equals("fileMd5")) {
							fileMd5 = item.getString("utf-8");
						}
						if (fieldName.equals("chunk")) {
							chunk = item.getString("utf-8");
						}
					} else {
						File file = new File(savePath + "/" + fileMd5);
						if (!file.exists()) {
							file.mkdir();
						}
						File chunkFile = new File(savePath + "/" + fileMd5 + "/"
								+ chunk);
						FileUtils.copyInputStreamToFile(item.getInputStream(),
								chunkFile);
					}
				}

			} catch (Exception  e) {
			    logger.error(e,e);
			}
		} catch (Exception e) {
			 logger.error(e,e);
		}
	
	}
}