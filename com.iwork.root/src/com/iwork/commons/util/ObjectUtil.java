package com.iwork.commons.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 处理对象操作
 * @author YangDayong
 *
 */
public class ObjectUtil {
	/**
	 * 无论对象为数组、还是list集合或字符串，统一返回第一个字符串文本
	 * @param obj
	 * @return
	 */
	public static String getString(Object obj){
		String str = "";
		if(obj!=null){
			if(obj instanceof String){
				str = obj.toString();
			}else if(obj instanceof String[]){
				String[] tmp = (String[])obj;
				str = tmp[0];
			}else if(obj instanceof Date){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = (Date)obj; 
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(d);
				int hour = calendar.get(Calendar.HOUR);   
				int minute = calendar.get(Calendar.MINUTE);   
				int seconds =calendar.get(Calendar.SECOND);   
				if(hour==0&&minute==0&&seconds==0){
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				}  
				str = sdf.format(obj); 
			}else if(obj instanceof Long){
				str = obj.toString(); 
			}else{
				str = obj.toString();
			}
	   }
		return str;
	}
}