package com.iwork.plugs.sms.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;





public class Util {
	private static Logger logger = Logger.getLogger(Util.class);
	/**
	 * 将短信密码编码
	 * @param msg
	 * @return
	 */
	public String encode(String msg){
		if(msg!=null&&!"".equals(msg.trim())){
			try {
				return URLEncoder.encode(msg, "gb2312");
			} catch (UnsupportedEncodingException e) {
				logger.error(e,e);
			}
		}
		return "";
	}

	/**
	 * 解码信息内容
	 * @param msg
	 * @return
	 */
	public String decode(String msg){
		if(msg!=null&&!"".equals(msg.trim())){
			try {
				return URLDecoder.decode(msg, "gb2312");
			} catch (UnsupportedEncodingException e) {
				logger.error(e,e);
			}
		}
		return "";
	}
	/**
	 * 读手机发送短信的帐号属性配置文件
	 * @return
	 */
	public String getMsgServerProperty(String key){
		Properties prop = new Properties();
		String value = "";
		try{
			String currpath = new File("").getAbsolutePath()+File.separator+"plugs"+File.separator+"xcgl.properties";
			InputStream in = new FileInputStream(new File(currpath));
			if(in != null){
				prop.load(in);
				value = prop.getProperty(key);
			}
		}catch(Exception e){
			logger.error(e,e);
		}
		return value;
	}
	
	/**
	 * 
	 * 
	 */
	public static final HashMap<String, String> msgplat= new HashMap<String, String>() {
		{
			put("FUNCTION_LY ", "TRRT");
			put("FUNCTION_TXLC ", "TRRT");
			put("FUNCTION_TXCX", "TRRT");
			put("FUNCTION_PS", "TRRT");
			put("FUNCTION_MS", "TRRT");
			put("FUNCTION_LCSPH", "TRRT");
			
			put("SERVER_NAME_TRRT ", "jinshan");
			put("SERVER_PWD_TRRT ", "kingsoft2010");
			put("SERVER_NAME_DSF", "kingsoft1");
			put("SERVER_PWD_DSF", "bc01da238911e14dd1301ccc647b7b4b");
			put("FUNCTION_MS", "TRRT");
			put("SERVER_EXTEND1_DSF", "0");
		}
	};
}
