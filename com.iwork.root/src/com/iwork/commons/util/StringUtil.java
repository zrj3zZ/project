package com.iwork.commons.util;

/**
 * <p>Copyright: Copyright (c) 2002-2003</p>
 * <p>Company: JavaResearch(http://www.javaresearch.org)</p>
 * <p>最后更新日期:2003年2月19日
 * @author Cherami
 */

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.iwork.commons.util.FileUtil;

/**
 * 此类中封装一些常用的字符串操作。 所有方法都是静态方法，不需要生成此类的实例， 为避免生成此类的实例，构造方法被申明为private类型的。
 * 
 * @since 0.1
 */
public class StringUtil {
	private static Logger logger = Logger.getLogger(StringUtil.class);

	/***/
	public String strSuper;

	/***/
	protected String lowcaseSuper;

	/**
	 * 允许上传的文件类型^.*?\.(txt|xls|xlsx|doc|docx|pdf|ppt|pptx|zip|rar|bmp|jpg|jpeg|png|gif|xml)$
	 */
	private static String FILETYPEREGEX = "^.*?(txt|xls|xlsx|doc|docx|pdf|ppt|pptx|zip|rar|bmp|jpg|jpeg|png|gif|xml)$";

	/**
	 * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
	 */
	private StringUtil() {
	}

	/**
	 * 此方法将给出的字符串source使用delim划分为单词数组。
	 * 
	 * @param source
	 *            需要进行划分的原字符串
	 * @param delim
	 *            单词的分隔字符串
	 * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组，
	 *         如果delim为null则使用逗号作为分隔字符串。
	 * @since 0.1
	 */
	public static String[] split(String source, String delim) {
		String[] wordLists;
		if (source == null) {
			wordLists = new String[1];
			wordLists[0] = source;
			return wordLists;
		}
		if (delim == null) {
			delim = ",";
		}
		StringTokenizer st = new StringTokenizer(source, delim);
		int total = st.countTokens();
		wordLists = new String[total];
		for (int i = 0; i < total; i++) {
			wordLists[i] = st.nextToken();
		}
		return wordLists;
	}

	/**
	 * 此方法将给出的字符串source使用delim划分为单词数组。
	 * 
	 * @param source
	 *            需要进行划分的原字符串
	 * @param delim
	 *            单词的分隔字符
	 * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组。
	 * @since 0.2
	 */
	public static String[] split(String source, char delim) {
		return split(source, String.valueOf(delim));
	}

	/**
	 * 此方法将给出的字符串source使用逗号划分为单词数组。
	 * 
	 * @param source
	 *            需要进行划分的原字符串
	 * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组。
	 * @since 0.1
	 */
	public static String[] split(String source) {
		return split(source, ",");
	}

	/**
	 * 循环打印字符串数组。 字符串数组的各元素间以指定字符分隔，如果字符串中已经包含指定字符则在字符串的两端加上双引号。
	 * 
	 * @param strings
	 *            字符串数组
	 * @param delim
	 *            分隔符
	 * @param out
	 *            打印到的输出流
	 * @since 0.4
	 */
	public static void printStrings(String[] strings, String delim,
			OutputStream out) {
		try {
			if (strings != null) {
				int length = strings.length - 1;
				for (int i = 0; i < length; i++) {
					if (strings[i] != null) {
						if (strings[i].indexOf(delim) > -1) {
							out.write(("\"" + strings[i] + "\"" + delim)
									.getBytes());
						} else {
							out.write((strings[i] + delim).getBytes());
						}
					} else {
						out.write("null".getBytes());
					}
				}
				if (strings[length] != null) {
					if (strings[length].indexOf(delim) > -1) {
						out.write(("\"" + strings[length] + "\"").getBytes());
					} else {
						out.write(strings[length].getBytes());
					}
				} else {
					out.write("null".getBytes());
				}
			} else {
				out.write("null".getBytes());
			}
			out.write(FileUtil.LINE_SEPARATOR.getBytes());
		} catch (Exception e) {
			logger.error(e,e);
		}
	}

	/**
	 * 循环打印字符串数组到标准输出。 字符串数组的各元素间以指定字符分隔，如果字符串中已经包含指定字符则在字符串的两端加上双引号。
	 * 
	 * @param strings
	 *            字符串数组
	 * @param delim
	 *            分隔符
	 * @since 0.4
	 */
	public static void printStrings(String[] strings, String delim) {
		printStrings(strings, delim, System.out);
	}

	/**
	 * 循环打印字符串数组。 字符串数组的各元素间以逗号分隔，如果字符串中已经包含逗号则在字符串的两端加上双引号。
	 * 
	 * @param strings
	 *            字符串数组
	 * @param out
	 *            打印到的输出流
	 * @since 0.2
	 */
	public static void printStrings(String[] strings, OutputStream out) {
		printStrings(strings, ",", out);
	}

	/**
	 * 循环打印字符串数组到系统标准输出流System.out。 字符串数组的各元素间以逗号分隔，如果字符串中已经包含逗号则在字符串的两端加上双引号。
	 * 
	 * @param strings
	 *            字符串数组
	 * @since 0.2
	 */
	public static void printStrings(String[] strings) {
		printStrings(strings, ",", System.out);
	}

	/**
	 * 将字符串中的变量使用values数组中的内容进行替换。 替换的过程是不进行嵌套的，即如果替换的内容中包含变量表达式时不会替换。
	 * 
	 * @param prefix
	 *            变量前缀字符串
	 * @param source
	 *            带参数的原字符串
	 * @param values
	 *            替换用的字符串数组
	 * @return 替换后的字符串。 如果前缀为null则使用“%”作为前缀；
	 *         如果source或者values为null或者values的长度为0则返回source；
	 *         如果values的长度大于参数的个数，多余的值将被忽略；
	 *         如果values的长度小于参数的个数，则后面的所有参数都使用最后一个值进行替换。
	 * @since 0.2
	 */
	public static String getReplaceString(String prefix, String source,
			String[] values) {
		String result = source;
		if (source == null || values == null || values.length < 1) {
			return source;
		}
		if (prefix == null) {
			prefix = "%";
		}

		for (int i = 0; i < values.length; i++) {
			String argument = prefix + Integer.toString(i + 1);
			int index = result.indexOf(argument);
			if (index != -1) {
				String temp = result.substring(0, index);
				if (i < values.length) {
					temp += values[i];
				} else {
					temp += values[values.length - 1];
				}
				temp += result.substring(index + 2);
				result = temp;
			}
		}
		return result;
	}

	/**
	 * 将字符串中的变量（以“%”为前导后接数字）使用values数组中的内容进行替换。
	 * 替换的过程是不进行嵌套的，即如果替换的内容中包含变量表达式时不会替换。
	 * 
	 * @param source
	 *            带参数的原字符串
	 * @param values
	 *            替换用的字符串数组
	 * @return 替换后的字符串
	 * @since 0.1
	 */
	public static String getReplaceString(String source, String[] values) {
		return getReplaceString("%", source, values);
	}

	/**
	 * 字符串数组中是否包含指定的字符串。
	 * 
	 * @param strings
	 *            字符串数组
	 * @param string
	 *            字符串
	 * @param caseSensitive
	 *            是否大小写敏感
	 * @return 包含时返回true，否则返回false
	 * @since 0.4
	 */
	public static boolean contains(String[] strings, String string,
			boolean caseSensitive) {
		for (int i = 0; i < strings.length; i++) {
			if (caseSensitive == true) {
				if (strings[i].equals(string)) {
					return true;
				}
			} else {
				if (strings[i].equalsIgnoreCase(string)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 字符串数组中是否包含指定的字符串。大小写敏感。
	 * 
	 * @param strings
	 *            字符串数组
	 * @param string
	 *            字符串
	 * @return 包含时返回true，否则返回false
	 * @since 0.4
	 */
	public static boolean contains(String[] strings, String string) {
		return contains(strings, string, true);
	}

	/**
	 * 不区分大小写判定字符串数组中是否包含指定的字符串。
	 * 
	 * @param strings
	 *            字符串数组
	 * @param string
	 *            字符串
	 * @return 包含时返回true，否则返回false
	 * @since 0.4
	 */
	public static boolean containsIgnoreCase(String[] strings, String string) {
		return contains(strings, string, false);
	}

	/**
	 * 将字符串数组使用指定的分隔符合并成一个字符串。
	 * 
	 * @param array
	 *            字符串数组
	 * @param delim
	 *            分隔符，为null的时候使用""作为分隔符（即没有分隔符）
	 * @return 合并后的字符串
	 * @since 0.4
	 */
	public static String combineStringArray(String[] array, String delim) {
		int length = array.length - 1;
		if (delim == null) {
			delim = "";
		}
		StringBuffer result = new StringBuffer(length * 8);
		for (int i = 0; i < length; i++) {
			result.append(array[i]);
			result.append(delim);
		}
		result.append(array[length]);
		return result.toString();
	}

	/**
	 * 截取标记为strBegin与strEnd之间的字符串 不区分大小写
	 * 
	 * @param strBegin
	 *            一个头标志
	 * @param strEnd
	 *            尾标志
	 * @return 如果找到返回被截取的字符串值，否则返回空字符串;
	 * @preserve 声明此方法不被JOC混淆
	 */
	public String matchValue(String strBegin, String strEnd) {
		return matchValue(strBegin, strEnd, false);
	}

	/**
	 * 截取标记为strBegin与strEnd之间的字符串
	 * 
	 * @param strBegin
	 *            一个头标志
	 * @param strEnd
	 *            尾标志
	 * @param ignoreCase
	 *            是否区分大小写，为true时表示区分
	 * @return 如果找到返回被截取的字符串值，否则返回空字符串;
	 * @preserve 声明此方法不被JOC混淆
	 */
	public String matchValue(String strBegin, String strEnd, boolean ignoreCase) {
		String strResult = new String();
		int posBegin = this.matchOf(strBegin, ignoreCase);
		int posEnd = this.lastMatchOf(strEnd, ignoreCase);

		if ((posEnd > posBegin) && (posBegin != -1)) {
			strResult = strSuper.substring(posBegin, posEnd);
		}

		return strResult;
	}

	/**
	 * 将strOld替换为strNew
	 * 
	 * @param strOld
	 *            被替换字符串
	 * @param strNew
	 *            替换值
	 * @preserve 声明此方法不被JOC混淆
	 */
	public String replace(String strOld, String strNew) {
		return replace(strOld, strNew, false);
	}

	/**
	 * 将strOld替换为strNew
	 * 
	 * @param strOld
	 *            被替换字符串
	 * @param strNew
	 *            替换值
	 * @param ignoreCase
	 *            是否区分大小写，为true时表示区分
	 * @preserve 声明此方法不被JOC混淆
	 */
	public String replace(String strOld, String strNew, boolean ignoreCase) {
		// String strResult = new String();
		String strResult = new String();
		String strCore;

		String strReplace;

		if (ignoreCase) {
			strCore = lowcaseSuper;
			strReplace = strOld.toLowerCase();
		} else {
			strCore = strSuper;
			strReplace = strOld;
		}

		int posBegin = 0;
		int posEnd = strCore.indexOf(strReplace);

		// nothing should be replaced
		if (posEnd < 0) {
			return strSuper;
		}

		while (posEnd >= 0) {
			strResult += strSuper.substring(posBegin, posEnd);
			strResult += strNew;
			posBegin = posEnd + strReplace.length();
			posEnd = strCore.indexOf(strReplace, posBegin);
		}

		strResult += strSuper.substring(posBegin);

		return strResult.toString();
	}

	/**
	 * 从0位置寻找字符串strEnd在strSuper中出现的位置
	 * 
	 * @param strEnd
	 *            将要查找的字符串
	 * @param ignoreCase
	 *            是否区分大小写，为true时表示区分
	 * @return 如果找到返回字符串首位置，否则返回-1;
	 * @preserve 声明此方法不被JOC混淆
	 */
	private int lastMatchOf(String strEnd, boolean ignoreCase) {
		if (ignoreCase) {
			return lowcaseSuper.lastIndexOf(strEnd.toLowerCase());
		} else {
			return strSuper.lastIndexOf(strEnd);
		}
	}

	/**
	 * 从0位置寻找字符串strBegin在strSuper中出现的位置
	 * 
	 * @param strBegin
	 *            将要查找的字符串
	 * @param ignoreCase
	 *            是否区分大小写，为true时表示区分
	 * @return 如果找到返回字符串首位置，否则返回-1;
	 * @preserve 声明此方法不被JOC混淆
	 */
	private int matchOf(String strBegin, boolean ignoreCase) {
		int pos = -1;

		if (ignoreCase) {
			pos = lowcaseSuper.indexOf(strBegin.toLowerCase());
		} else {
			pos = strSuper.indexOf(strBegin);
		}

		if (pos < 0) {
			return pos;
		} else {
			return (pos + strBegin.length());
		}
	}

	/**
	 * 判断str1中包含str2的个数
	 * 
	 * @param str1
	 * @param str2
	 * @return counter
	 */
	public static int countStr(String str1, String str2) {
		int counter = 0;
		if (str1.indexOf(str2) == -1) {
			return 0;
		} else if (str1.indexOf(str2) != -1) {
			counter++;
			counter += countStr(str1.substring(str1.indexOf(str2) + str2.length()), str2);
			return counter;
		}
		return 0;
	}

	public static boolean mathcer(String regex, String str) {
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(str);
		return matcher.find();
	}

	// 判断是否为允许上传的文件类型
	public static boolean validata(String fileExt) {
		Pattern p = Pattern.compile(FILETYPEREGEX);
		Matcher matcher = p.matcher(fileExt.toLowerCase());
		return matcher.find();
	}
}