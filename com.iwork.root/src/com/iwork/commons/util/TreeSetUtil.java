package com.iwork.commons.util;
/**
 * <p>Copyright: Copyright (c) 2002-2003</p>
 * <p>Company: JavaResearch(http://www.javaresearch.org)</p>
 * <p>最后更新日期:2003年3月26日
 * @author Cherami
 */
import java.util.*;

/**
 * 此类中封装一些常用的TreeSet操作方法。
 * 所有方法都是静态方法，不需要生成此类的实例，
 * 为避免生成此类的实例，构造方法被申明为private类型的。
 * @since  0.5
 */

public class TreeSetUtil {
  /**
   * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
   */
  private TreeSetUtil() {
  }
  /**
   * 将数组转换为TreeSet。
   * @param array 数组
   * @return 转换后的TreeSet，如果数组为null则返回null
   * @since  0.5
   */
  public static TreeSet<Object> ArrayToTreeSet(Object[] array) {
    if (array != null) {
      TreeSet<Object> treeSet = new TreeSet<Object>();
      for (int i = 0; i < array.length; i++) {
        treeSet.add(array[i]);
      }
      return treeSet;
    }
    else {
      return null;
    }
  }
}