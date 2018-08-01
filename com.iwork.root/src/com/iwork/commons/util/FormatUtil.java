package com.iwork.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 格式转换工具
 * @author YangDayong
 *
 */
public class FormatUtil {
	private static Logger logger = Logger.getLogger(FormatUtil.class);
	public static byte[] inputStreamTobyte(InputStream is){
		 byte[] data = null;
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();  
		   int ch;  
		   try {
			while ((ch = is.read()) != -1) {  
			    bytestream.write(ch);  
			}
		   data = bytestream.toByteArray();  
		   bytestream.close();  
		   } catch (Exception e) {
				logger.error(e,e);
		   }  
		   return data; 
	}
	/**
	 * btye转换为 inputstream
	 * @param btye
	 * @return
	 */
	public static InputStream inputbyteToStream(byte[] btye){
		InputStream sbs = new ByteArrayInputStream(btye); 
		return sbs; 
	}
	
	/**
	 * key/value 的OBJECT 对象转换成MAP
	 * @param obj
	 * @return
	 */
	public static Map ConvertObjToMap(Object obj){
		  Map<String,Object> reMap = new HashMap<String,Object>();
		  if (obj == null) 
		   return null;
		  Field[] fields = obj.getClass().getDeclaredFields();
		  try {
		   for(int i=0;i<fields.length;i++){
		    try {
		     Field f = obj.getClass().getDeclaredField(fields[i].getName());
		     f.setAccessible(true);
		           Object o = f.get(obj);
		           reMap.put(fields[i].getName(), o);
		    } catch (NoSuchFieldException e) {
		     // TODO Auto-generated catch block
		     logger.error(e,e);
		    } catch (IllegalArgumentException e) {
		     // TODO Auto-generated catch block
		     logger.error(e,e);
		    } catch (IllegalAccessException e) {
		     // TODO Auto-generated catch block
		     logger.error(e,e);
		    }
		   }
		  } catch (SecurityException e) {
		   // TODO Auto-generated catch block
		   logger.error(e,e);
		  } 
		  return reMap;
		 }
}
