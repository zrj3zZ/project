package com.ibpmsoft.project.zqb.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
/**
 * �����ļ�����
 * @author duqq
 *
 */
public class ConfigUtil {
	private static Logger logger = Logger.getLogger(ConfigUtil.class);
	private static Properties config = null;
	public  static  void init(String filename) {
		InputStream in = ConfigUtil.class.getResourceAsStream(
				filename);
		config = new Properties();
		try {
			config.load(in);
			in.close();
		} catch (Exception e) {
			logger.error(e,e);
		}
	} // ���key��ȡvalue

	public static String readValue(String filename,String key) {
		// 
		init(filename);
		Properties props = new Properties();
		try {
			String value = config.getProperty(key);
			return value;
		} catch (Exception e) {
			logger.error(e,e);

			return null;
		}
	}

	// ��ȡproperties��ȫ����Ϣ
	public static Map<String, String> readAllProperties(String filename) {
		init(filename);
		Map<String, String> map =new HashMap<String, String>();
		try {
			Enumeration en = config.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String Property = config.getProperty(key);
				map.put(key, Property);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		return map;
	}
	//更新propertie
	public static void updateProperties(String filename,String key ,String value) {
		try {
			Properties prop = new Properties();
			InputStream in = new  FileInputStream(filename);
			prop.load(in);
			in.close();
			OutputStream out = new FileOutputStream(filename);
			prop.setProperty(key, value);
			prop.store(out, "Update '" + key + "'+ '"+value);
			out.close();
		} catch (Exception e) {
			logger.error(e,e);
		}
	}
}