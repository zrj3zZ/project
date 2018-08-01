

package com.iwork.commons;

/**
 * 字符编码定义
 * 
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class Encoding {

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int GB2312 = 0;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int GBK = 1;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int HZ = 2;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int BIG5 = 3;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int CNS11643 = 4;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int UTF8 = 5;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int UNICODE = 6;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int UNICODET = 7;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int UNICODES = 8;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int ISO2022CN = 9;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int ISO2022CN_CNS = 10;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int ISO2022CN_GB = 11;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int ASCII = 12;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int OTHER = 13;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public final static int TOTALTYPES = 14;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String[] javaname;

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public Encoding() {
		javaname = new String[TOTALTYPES];

		// Assign encoding names
		javaname[GB2312] = "GB2312";
		javaname[HZ] = "ASCII"; // What to put here? Sun doesn't support HZ
		javaname[GBK] = "GBK";
		javaname[ISO2022CN_GB] = "ISO2022CN_GB";
		javaname[BIG5] = "BIG5";
		javaname[CNS11643] = "EUC-TW";
		javaname[ISO2022CN_CNS] = "ISO2022CN_CNS";
		javaname[ISO2022CN] = "ISO2022CN";
		javaname[UTF8] = "UTF8";
		javaname[UNICODE] = "Unicode";
		javaname[UNICODET] = "Unicode";
		javaname[UNICODES] = "Unicode";
		javaname[ASCII] = "ASCII";
		javaname[OTHER] = "ISO8859_1";
	}

}
