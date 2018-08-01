package com.iwork.sdk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.conf.SystemConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.log4j.Logger;
public class ConfigurationAPI {
	private static ConfigurationAPI instance;
	private static Configuration configuration = null;
	private static Object lock = new Object();
	private static Logger logger = Logger.getLogger(ConfigurationAPI.class);
	@SuppressWarnings("deprecation")
	public ConfigurationAPI(){
		configuration = new Configuration();
		configuration.setDefaultEncoding("UTF-8");
	}
	
	public static ConfigurationAPI getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ConfigurationAPI();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 生成word文档
	 * @param response
	 * @param dataMap 模版中所涉及到的参数
	 * @param templateName 模版名称
	 * @return
	 */
	public String createWord(HttpServletResponse response,HashMap dataMap,String templateName) {
		String savePath="";
		try {
			String rootPath =  ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getFormFilePath().replace("/",File.separator));
			Date data=new Date();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
			String format = sd.format(data);
			rootPath = rootPath.replace("/",File.separator)+File.separator+format;
			String uuid =  UUID.randomUUID().toString().replaceAll("-", ""); 
			File dirFile = new File(rootPath);
			if(!dirFile.isDirectory()){
				dirFile.mkdirs();
			}
			String newFileName=uuid+"."+"doc";
			savePath = rootPath+File.separator+newFileName;
			File targetFile = new File(savePath);
			try {
				configuration.setClassForTemplateLoading(this.getClass(),"template"); // FTL文件所存在的位置
				Template template = configuration.getTemplate(templateName);
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
				template.process(dataMap, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				logger.error(e,e);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		return savePath;
	}

}
