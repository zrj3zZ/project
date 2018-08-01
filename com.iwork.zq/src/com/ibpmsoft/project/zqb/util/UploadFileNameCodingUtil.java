package com.ibpmsoft.project.zqb.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;
import org.apache.log4j.Logger;
public class UploadFileNameCodingUtil {
	private final static String IE9="MSIE 9.0";
    private final static String IE8="MSIE 8.0";
    private final static String IE7="MSIE 7.0";
    private final static String IE6="MSIE 6.0";
    private final static String MAXTHON="Maxthon";
    private final static String QQ="QQBrowser";  
    private final static String GREEN="GreenBrowser";  
    private final static String SE360="360SE";  
    private final static String FIREFOX="Firefox";  
    private final static String OPERA="Opera";  
    private final static String CHROME="Chrome";  
    private final static String SAFARI="Safari";  
    private static Logger logger = Logger.getLogger(UploadFileNameCodingUtil.class);
	public static boolean regex(String regex,String str){
	    Pattern p =Pattern.compile(regex,Pattern.MULTILINE);
	    Matcher m=p.matcher(str);
	    return m.find();  
	}	
	 /** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块 */
	public static final String US_ASCII = "US-ASCII";

	 /** ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1 */
	public static final String ISO_8859_1 = "ISO-8859-1";

	 /** 8 位 UCS 转换格式 */
	 public static final String UTF_8 = "UTF-8";

	 /** 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序 */
	 public static final String UTF_16BE = "UTF-16BE";

	 /** 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序 */
	 public static final String UTF_16LE = "UTF-16LE";

	 /** 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识 */
	 public static final String UTF_16 = "UTF-16";

	 /** 中文超大字符集 */
	 public static final String GBK = "GBK";

	 /**
	  * 将字符编码转换成US-ASCII码
	  */
	 public static String toASCII(String str) throws UnsupportedEncodingException{
	  return changeCharset(str, US_ASCII);
	 }
	 /**
	  * 将字符编码转换成ISO-8859-1码
	  */
	 public static String toISO_8859_1(String str) throws UnsupportedEncodingException{
	  return changeCharset(str, ISO_8859_1);
	 }
	 /**
	  * 将字符编码转换成UTF-8码
	  */
	 public static String toUTF_8(String str) throws UnsupportedEncodingException{
	  return changeCharset(str, UTF_8);
	 }
	 /**
	  * 将字符编码转换成UTF-16BE码
	  */
	 public static String toUTF_16BE(String str) throws UnsupportedEncodingException{
	  return changeCharset(str, UTF_16BE);
	 }
	 /**
	  * 将字符编码转换成UTF-16LE码
	  */
	 public static String toUTF_16LE(String str) throws UnsupportedEncodingException{
	  return changeCharset(str, UTF_16LE);
	 }
	 /**
	  * 将字符编码转换成UTF-16码
	  */
	 public static String toUTF_16(String str) throws UnsupportedEncodingException{
	  return changeCharset(str, UTF_16);
	 }
	 /**
	  * 将字符编码转换成GBK码
	  */
	 public static String toGBK(String str) throws UnsupportedEncodingException{
	  return changeCharset(str, GBK);
	 }
	 
	 /**
	  * 字符串编码转换的实现方法
	  * @param str  待转换编码的字符串
	  * @param newCharset 目标编码
	  * @return
	  * @throws UnsupportedEncodingException
	  */
	 public static String changeCharset(String str, String newCharset){
	  if (str != null) {
 		  return getBrowserName(ServletActionContext.getRequest().getHeader("USER-AGENT"),str,newCharset);
	  }
	  return null;
	 }
	 /**
	  * 字符串编码转换的实现方法
	  * @param str  待转换编码的字符串
	  * @param oldCharset 原编码
	  * @param newCharset 目标编码
	  * @return
	  * @throws UnsupportedEncodingException
	  */
	 public static String changeCharset(String str, String oldCharset, String newCharset)
	   throws UnsupportedEncodingException {
	  if (str != null) {
	   //用旧的字符编码解码字符串。解码可能会出现异常。
	   byte[] bs = str.getBytes(oldCharset);
	   //用新的字符编码生成字符串
	   return new String(bs, newCharset);
	  }
	  return null;
	 }
	 
	 public static String StringEncoding(String str){
		 String string="";
		 try {
			 string = toISO_8859_1(str);
		} catch (UnsupportedEncodingException e) {
			try {
				string = toUTF_8(str);
			} catch (UnsupportedEncodingException e1) {
				return str;
			}
		}
		 return string;
	 }
	 
	 public static String getBrowserName(String agent, String str, String newCharset) {
		//用默认字符编码解码字符串。
		byte[] bs;
		try {
			if(regex(OPERA, agent)){
				bs = str.getBytes("UTF-8");
			} else if(regex(CHROME, agent)){
				bs = str.getBytes("UTF-8");
			}else if(regex(FIREFOX, agent)){
				bs = str.getBytes("UTF-8");
			}else if(regex(SAFARI, agent)){
				bs = str.getBytes("UTF-8");
			}else if(regex(SE360, agent)){
				bs = str.getBytes("GB2312");
			}else if(regex(GREEN,agent)){
				bs = str.getBytes("UTF-8");
			}else if(regex(QQ,agent)){
				bs = str.getBytes("UTF-8");
	        }else if(regex(MAXTHON, agent)){
	        	bs = str.getBytes("UTF-8");
	        }else if(regex(IE9,agent)){
	        	bs = str.getBytes("GB2312");
	        }else if(regex(IE8,agent)){
	        	bs = str.getBytes("GB2312");
	        }else if(regex(IE7,agent)){
	        	bs = str.getBytes("GB2312");
	        }else if(regex(IE6,agent)){
	        	bs = str.getBytes("GB2312");
	        }else{
	        	str=new String(str.getBytes("GBK"), "ISO-8859-1");
	        	bs = str.getBytes("ISO-8859-1");
	        }
		//用新的字符编码生成字符串
		return new String(bs, newCharset);
		} catch (UnsupportedEncodingException e) {
			logger.error(e,e);
		}
		return null;
	}
}
