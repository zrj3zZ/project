package com.iwork.commons.util;

/**
 * <p>Copyright: Copyright (c) 2002-2003</p>
 * <p>Company: JavaResearch(http://www.javaresearch.org)</p>
 * <p>最后更新日期:2003年3月11日
 * @author Cherami
 */

/**
 * boolean类型相关内容的操作工具类，提供常见的boolean类型操作需要的方法。
 * @since  0.5
 */

public class BooleanUtil {

  /**
   * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
   */
  private BooleanUtil() {
  }

  /**
   * 将整型的数转换为boolean数组，如果对应的位的值为1则将数组中的对应的元素赋值为true，否则为false。
   * 例如value为5，则得到一个长度为32，只有0和2两个元素的值为true的数组。
   * @param value 值
   * @return 转换后的boolean数组
   * @since  0.5
   */
  public static boolean[] convertToArray(int value) {
    int length = 32;
    boolean[] result = new boolean[length];
    int mark = 1;
    for (int i = 0; i < length; i++) {
      if ( (mark & value) != 0) {
        result[i] = true;
      }
      else {
        result[i] = false;
      }
      mark = mark << 1;
    }
    return result;
  }

  /**
   * 将字节型的数转换为boolean数组，如果对应的位的值为1则将数组中的对应的元素赋值为true，否则为false。
   * 例如value为5，则得到一个长度为8，只有0和2两个元素的值为true的数组。
   * @param value 值
   * @return 转换后的boolean数组
   * @since  0.5
   */
  public static boolean[] convertToArray(byte value) {
    int length = 8;
    boolean[] result = new boolean[length];
    int mark = 1;
    for (int i = 0; i < length; i++) {
      if ( (mark & value) != 0) {
        result[i] = true;
      }
      else {
        result[i] = false;
      }
      mark = mark << 1;
    }
    return result;
  }

  /**
   * 将长整型的数转换为boolean数组，如果对应的位的值为1则将数组中的对应的元素赋值为true，否则为false。
   * 例如value为5，则得到一个长度为64，只有0和2两个元素的值为true的数组。
   * @param value 值
   * @return 转换后的boolean数组
   * @since  0.5
   */
  public static boolean[] convertToArray(long value) {
    int length = 64;
    boolean[] result = new boolean[length];
    long mark = 1;
    for (int i = 0; i < length; i++) {
      if ( (mark & value) != 0) {
        result[i] = true;
      }
      else {
        result[i] = false;
      }
      mark = mark << 1;
    }
    return result;
  }

  /**
   * 将短节型的数转换为boolean数组，如果对应的位的值为1则将数组中的对应的元素赋值为true，否则为false。
   * 例如value为5，则得到一个长度为16，只有0和2两个元素的值为true的数组。
   * @param value 值
   * @return 转换后的boolean数组
   * @since  0.5
   */
  public static boolean[] convertToArray(short value) {
    int length = 16;
    boolean[] result = new boolean[length];
    long mark = 1;
    for (int i = 0; i < length; i++) {
      if ( (mark & value) != 0) {
        result[i] = true;
      }
      else {
        result[i] = false;
      }
      mark = mark << 1;
    }
    return result;
  }

  /**
   * 将boolean数组转换为一个整型值，数组的长度如果大于32，多余的部分被忽略。
   * 例如一个长度为8的数组，只有0和3两个元素的值为true，将返回值9。
   * @param values boolean数组
   * @return 转换后的整型值
   * @since  0.5
   */
  public static int convertToInt(boolean[] values) {
    int length = 8;
    int value = 0;
    int mark = 1;
    for (int i = 0; i < length; i++) {
      if (values[i] == true) {
        value = value + mark;
      }
      mark = mark << 1;
    }
    return value;
  }

  /**
   * 将boolean数组转换为一个字节型值，数组的长度如果大于8，多余的部分被忽略。
   * 例如一个长度为8的数组，只有0和3两个元素的值为true，将返回值9。
   * @param values boolean数组
   * @return 转换后的字节型值
   * @since  0.5
   */
  public static byte convertToByte(boolean[] values) {
    int length = 8;
    byte value = 0;
    byte mark = 1;
    for (int i = 0; i < length; i++) {
      if (values[i] == true) {
        value = (byte) (value + mark);
      }
      mark = (byte) (mark << 1);
    }
    return value;
  }

  /**
   * 将boolean数组转换为一个短整型值，数组的长度如果大于16，多余的部分被忽略。
   * 例如一个长度为8的数组，只有0和3两个元素的值为true，将返回值9。
   * @param values boolean数组
   * @return 转换后的短整型值
   * @since  0.5
   */
  public static short convertToShort(boolean[] values) {
    int length = 16;
    short value = 0;
    short mark = 1;
    for (int i = 0; i < length; i++) {
      if (values[i] == true) {
        value = (short) (value + mark);
      }
      mark = (short) (mark << 1);
    }
    return value;
  }

  /**
   * 将boolean数组转换为一个长整型值，数组的长度如果大于64，多余的部分被忽略。
   * 例如一个长度为8的数组，只有0和3两个元素的值为true，将返回值9。
   * @param values boolean数组
   * @return 转换后的长整型值
   * @since  0.5
   */
  public static long convertToLong(boolean[] values) {
    int length = 64;
    long value = 0;
    long mark = 1;
    for (int i = 0; i < length; i++) {
      if (values[i] == true) {
        value = value + mark;
      }
      mark = mark << 1;
    }
    return value;
  }
}