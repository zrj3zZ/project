package com.iwork.plugs.sysletters.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
/**
 * 站内信工具类
 * @author WangJianhui
 *
 */
public class SysLetterUtil {
	/**
	 * 剔除空对象
	 * @param obj
	 * @return
	 */
	public Object removeNull(Object obj){
		Object objNew = new Object();
		if(obj == null){
			if(obj instanceof String){
				objNew = "";
			}else if(obj instanceof Long){
				objNew = 0L;
			}else if(obj instanceof Date){
				objNew = new Date();
			}else{
				objNew = obj;
			}
		}else{
			objNew = obj;
		}
		return objNew;
	}
	/**
	 * 将字符串替换成无重复的并以','分割
	 * @param oldStr
	 * @return
	 */
	public String getNoRepeatString(String oldStr){
		String newStr = "";
		if(oldStr!=null&&!"".equals(oldStr)){
			if(oldStr.contains(",")){
				String[] strArr = oldStr.split(",");
				Set<String> set = new HashSet<String>();
				for(int i=0;i<strArr.length;i++){
					set.add(strArr[i]);
				}
				for(String str:set){
					newStr = newStr + str + ",";
				}
				newStr = newStr.substring(0, newStr.lastIndexOf(","));
			}else{
				newStr = oldStr;
			}
		}
		return newStr;
	}
	
	public static void main(String[] args){
		SysLetterUtil util = new SysLetterUtil();
		String a = "";
		String b = util.getNoRepeatString(a);
	}
}
