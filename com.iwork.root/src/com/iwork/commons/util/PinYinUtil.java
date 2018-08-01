package com.iwork.commons.util;

import net.sourceforge.pinyin4j.PinyinHelper;

public class PinYinUtil {
	
	/**
	 * 中文转换为全拼
	 * @param zh_cnStr
	 * @return
	 */
	public static String zh_CnToPinyinParser(String zh_cnStr){
		String convert = "";  
        for (int j = 0; j < zh_cnStr.length(); j++) {  
            //提取每一个汉字 ！Char能存储汉字 这个是Java基础哦。  
            char word = zh_cnStr.charAt(j);  
            // 提取汉字的首字母  
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
            if (pinyinArray != null) {  
                //如果是汉字能提取当前首字母  
                convert += pinyinArray[0].charAt(0);  
            } else {
                //如果不是汉字 非汉字类型 英语类型 不用转换  
                convert += word;  
            }  
        }  
        return convert; 
	}
	/**
	 * 中文转换为拼音首字母
	 * @param zh_cnStr
	 * @return
	 */
	public static String zh_CnToPinyinHeadParser(String zh_cnStr){
		String convert = ""; 
		for (int j = 0; j < zh_cnStr.length(); j++) { 
		      char word = zh_cnStr.charAt(j); 
		      String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word); 
		      if (pinyinArray != null) { 
		      convert += pinyinArray[0].charAt(0); 
		      }else { 
		      convert += word; 
		      } 
		} 
		return convert.toUpperCase(); 
	}

}
