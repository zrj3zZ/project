package com.iwork.commons;

/**
 * <p>Copyright: Copyright (c) 2002-2003</p>
 * <p>Company: JavaResearch(http://www.javaresearch.org)</p>
 * <p>最后更新日期:2003年3月4日
 * @author Cherami
 */
/**
 * 属性比较接口。推出这个主要是弥补Comparable只能有一种比较结果的不足。
 * 实现此接口可以根据比较索引在不同的情况下得到不同的结果。
 * @since  0.5
 */

public interface PropertyComparable
    extends Comparable<Object> {
  /**
   * 根据比较关键字进行比较
   * @param o 要比较的另一个对象，一般也应该实现此接口
   * @param key 属性索引
   * @return 根据比较索引进行比较的结果，大于时返回值大于0，相等时返回0，小于时返回值小于0
   */
  public int compareTo(Object o, int key);
}
