package com.iwork.commons.util;

/**
 * <p>Copyright: Copyright (c) 2002-2003</p>
 * <p>Company: JavaResearch(http://www.javaresearch.org)</p>
 * <p>最后更新日期:2003年3月11日
 * @author Cherami
 */

/**
 * 数组操作工具类，提供常见的数组操作需要的方法。
 * @since  0.5
 */

public class ArrayUtil {
  /**
   * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
   */
  private ArrayUtil() {
  }

  /**
   * 得到初始化好的int数组。
   * @param length 数组长度
   * @return 初始化后的int数组，各个元素的值和其索引值相等。
   * @since  0.5
   */
  public static int[] getInitedIntArray(int length) {
    int[] indexes = new int[length];
    for (int i = 0; i < length; i++) {
      indexes[i] = i;
    }
    return indexes;
  }

  /**
   * 得到初始化好的int数组。
   * @param length 数组长度
   * @param value 初始值
   * @return 初始化后的int数组，各个元素的值都等于指定的value。
   * @since  0.5
   */
  public static int[] getInitedIntArray(int length, int value) {
    int[] indexes = new int[length];
    for (int i = 0; i < length; i++) {
      indexes[i] = value;
    }
    return indexes;
  }

  /**
   * 得到初始化好的boolean数组。
   * @param length 数组长度
   * @param value 初始值
   * @return 初始化后的boolean数组，各个元素的值都等于value。
   * @since  0.5
   */
  public static boolean[] getInitedBooleanArray(int length, boolean value) {
    boolean[] indexes = new boolean[length];
    for (int i = 0; i < length; i++) {
      indexes[i] = value;
    }
    return indexes;
  }

  /**
   * 得到指定的对象在对象数组中的索引。
   * @param objects 对象数组
   * @param object 对象
   * @return 对象在对象数组中的位置，不存在于数组中时返回-1
   * @since  0.5
   */
  public static int indexOf(Object[] objects, Object object) {
    for (int i = 0; i < objects.length; i++) {
      if (objects[i].equals(object)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * 将原数组的值拷贝到目标数组。
   * 目标数组的大小必须大于等于原数组，一般应该是大小相等，如果目标数组的大小较大则大于的部分保留原值。
   * @param orginalArray 原数组
   * @param targetArray 目标数组
   * @since  0.5
   */
  public static void copyArrayValue(int[] orginalArray, int[] targetArray) {
    for (int i = 0; i < orginalArray.length; i++) {
      targetArray[i] = orginalArray[i];
    }
  }

  /**
   * 将数组中的值移位。
   * 移动的方式是将指定位置以后的每个元素依次往前移动一位，指定位置的值移到最后。
   * @param array 数组
   * @param index 位置
   */
  public static void shiftArray(int[] array, int index) {
    int temp = array[index];
    int length = array.length - 1;
    for (int i = index; i < length; i++) {
      array[i] = array[i + 1];
    }
    array[length] = temp;
  }

  /**
   * 打印数组的值。
   * 数组前后为一个"["和"]"，中间用逗号分隔。
   * @param array 数组
   */
  public static void printArray(int[] array) {
    int length = array.length - 1;
    StringBuffer buffer = new StringBuffer(length * 5);
    buffer.append("[");
    for (int i = 0; i < length; i++) {
      buffer.append(array[i]);
      buffer.append(",");
    }
    buffer.append(array[length] + "]");
  }

  /**
   * 将指定的值插入到数组中的指定位置。
   * 数组最后一个元素的值将被丢弃。
   * @param array
   * @param index
   * @param value
   */
  public static void insertValueToArray(int[] array, int index, int value) {
    int length = array.length - 1;
    for (int i = length; i > index; i--) {
      array[i] = array[i - 1];
    }
    array[index] = value;
  }
}