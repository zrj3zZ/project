package com.iwork.commons.util;

import com.iwork.commons.PropertyComparable;

/**
 * 排序工具类，提供常见的需要排序但是又没有实现Comparable接口的类。
 * 此类也提供一些创建的排序算法的范例。
 * @since  0.5
 */
public class CompareUtil {
 
  private CompareUtil() {
  }

  /**
   * 比较两个数字。
   * 这个方法取Number的double值进行比较，因此只有在两个数严格相等的情况下才返回0，
   * 又可能因为Java中Double型和Float的精度不同而导致两个相等的数不返回0。
   * @param o1 第一个数字
   * @param o2 第二个数字
   * @return 第一个数字大于第二个返回1，等于返回0，否则返回－1
   * @since  0.5
   */
  public static int compare(Number o1, Number o2) {
    double n1 = o1.doubleValue();
    double n2 = o2.doubleValue();
    if (n1 < n2) {
      return -1;
    }
    else if (n1 > n2) {
      return 1;
    }
    else {
      return 0;
    }
  }

  /**
   * 比较两个布尔型值。
   * 如果两个的值不同，true被认为等于1，而false等于0。
   * @param o1 第一个值
   * @param o2 第二个值
   * @return 第一个值大于第二个返回1，等于返回0，否则返回-1
   * @since  0.5
   */
  public static int compare(Boolean o1, Boolean o2) {
    boolean b1 = o1.booleanValue();
    boolean b2 = o2.booleanValue();
    if (b1 == b2) {
      return 0;
    }
    else if (b1) {
      return 1;
    }
    else {
      return -1;
    }
  }

  /**
   * 冒泡排序法排序。
   * @param objects 排序对象
   * @param isAscent 排序顺序
   * @return 排序后应该有的索引数组
   * @since  0.5
   */
  @SuppressWarnings("unchecked")
public static int[] n2sort(@SuppressWarnings("rawtypes") Comparable[] objects, boolean isAscent) {
    int length = objects.length;
    int[] indexes = ArrayUtil.getInitedIntArray(length);
    for (int i = 0; i < length; i++) {
      for (int j = i + 1; j < length; j++) {
        if (isAscent == true) {
          if (objects[indexes[i]].compareTo(objects[indexes[j]]) > 0) {
            swap(indexes, i, j);
          }
        }
        else {
          if (objects[indexes[i]].compareTo(objects[indexes[j]]) < 0) {
            swap(indexes, i, j);
          }
        }
      }
    }
    return indexes;
  }

  /**
   * 冒泡排序法排序。
   * @param objects 排序对象
   * @param key 排序关键字
   * @param isAscent 排序顺序
   * @return 排序后应该有的索引数组
   * @since  0.5
   */
  public static int[] n2sort(PropertyComparable[] objects, int key,
                             boolean isAscent) {
    int length = objects.length;
    int[] indexes = ArrayUtil.getInitedIntArray(length);
    for (int i = 0; i < length; i++) {
      for (int j = i + 1; j < length; j++) {
        if (isAscent == true) {
          if (objects[indexes[i]].compareTo(objects[indexes[j]], key) > 0) {
            swap(indexes, i, j);
          }
        }
        else {
          if (objects[indexes[i]].compareTo(objects[indexes[j]], key) < 0) {
            swap(indexes, i, j);
          }
        }
      }
    }
    return indexes;
  }
  /**
   * 交换两个元素的值。
   * @param indexes 原索引数组
   * @param i 第一行
   * @param j 第二行
   */
  private static void swap(int[] indexes, int i, int j) {
    int tmp = indexes[i];
    indexes[i] = indexes[j];
    indexes[j] = tmp;
  }

}
