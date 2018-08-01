

package com.iwork.commons.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;


/**
 * 数值格式化工具
 * 
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class UtilNumber {

	/**
	 * 保留小数位数
	 * 
	 * @param num
	 *            原始数值
	 * @param pointNumber
	 *            要保留的小数位数
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static double fixPoint(double num, int pointNumber) {
		// //幂
		String tmp = Double.toString(num);

		// NumberFormat format = NumberFormat.getNumberInstance();
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(pointNumber);
		String f = format.format(num);
		f = new UtilString(f).replace(",", "");
		return Double.parseDouble(f);
	}

	// add by wangwq 增加对字符串型的数字的格式化
	public static String fixPoint(String num, int pointNumber) {
		// //幂
		if (num != null && num.trim().length() > 0 && num.indexOf(".") != -1) {
			if (pointNumber < 0) {
				return null;
			}
			BigDecimal b = new BigDecimal(num);
			BigDecimal one = new BigDecimal("1");
			BigDecimal b3 = b.divide(one, pointNumber, BigDecimal.ROUND_HALF_UP);
			String tmp = b3.toPlainString();
			String tmpHead=tmp.substring(0, tmp.indexOf("."));
			String tmpEnd=tmp.substring(tmp.indexOf("."), tmp.length());
			double tmpD=Double.parseDouble("0"+tmpEnd);
			String result = tmpHead + String.valueOf(tmpD).substring(1);
			return result;
		} else {
			return num;
		}
	}

	/**
	 * 将一个数值格式化成固定长度的串，不足以0补齐
	 * 
	 * @param num
	 * @param length
	 * @return
	 * @author jack
	 */
	public static String fillLength(int num, int length) {
		String var = Integer.toString(num);
		String tmp = var;
		for (int i = var.length(); i < length; i++) {
			tmp = "0" + tmp;
		}
		return tmp;
	}

	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	/** */
	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	public static String add(String v1, String v2) {
		if (v1 != null && v1.trim().length() > 0 && v2 != null && v2.trim().length() > 0) {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			BigDecimal b3 = b1.add(b2);
			return b3.toPlainString();
		} else {
			return v1;
		}
	}

	/** */
	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	
	public static String sub(String v1, String v2) {
		if (v1 != null && v1.trim().length() > 0 && v2 != null && v2.trim().length() > 0) {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			BigDecimal b3 = b1.subtract(b2);
			return b3.toPlainString();
		} else {
			return v1;
		}
	}

	/** */
	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	public static String mul(String v1, String v2) {
		if (v1 != null && v1.trim().length() > 0 && v2 != null && v2.trim().length() > 0) {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			BigDecimal b3 = b1.multiply(b2);
			return b3.toPlainString();
		} else {
			return v1;
		}

	}

	/** */
	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/** */
	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static String div(String v1, String v2, int scale) {
		BigDecimal b1 = new BigDecimal((v1));
		BigDecimal b2 = new BigDecimal((v2));
		BigDecimal b3=b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
		return b3.toPlainString();
	}

	/** */
	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static String round(String v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal((v));
		BigDecimal one = new BigDecimal("1");
		BigDecimal b3=b.divide(one, scale, BigDecimal.ROUND_HALF_UP);
		return b3.toPlainString();
	}



	public static String getMoneyTypeNumber(String number) {
		if(number==null||number.trim().length()==0)
			return number;
		DecimalFormat currencyFormat =new DecimalFormat();
		double number2=Double.parseDouble(number);
		String value=currencyFormat.format(number2);
		return value;
	}

	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
    public static boolean bIsNumber(String str)
    {
        if(str == null || str.trim().equals(""))
        {
            return false;
        } else
        {
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        }
    }
    
}