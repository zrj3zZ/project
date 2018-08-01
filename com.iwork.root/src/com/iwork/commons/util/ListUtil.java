package com.iwork.commons.util;

/**
 * <p>Copyright: Copyright (c) 2002-2003</p>
 * <p>Company: JavaResearch(http://www.javaresearch.org)</p>
 * <p>最后更新日期:2003年3月4日
 * @author Cherami
 */

import java.util.*;

/**
 * 此类中封装一些常用的List操作方法。
 * 所有方法都是静态方法，不需要生成此类的实例，
 * 为避免生成此类的实例，构造方法被申明为private类型的。
 * @since  0.5
 */

public class ListUtil {
  /**
   * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
   */
  private ListUtil() {
  }

  /**
   * 将数组转换为一个List，实际上是一个ArrayList。
   * @param array 原数组
   * @return 原数组不为null的时候返回包含数组内容的ArrayList，否则返回null
   * @since  0.5
   */
  public static List<Object> ArrayToList(Object[] array) {
    if (array != null) {
      ArrayList<Object> list = new ArrayList<Object>(array.length);
      for (int i = 0; i < array.length; i++) {
        list.add(array[i]);
      }
      return list;
    }
    else {
      return null;
    }
  }

  /**
   * 将数组中的内容全部添加到列表中。
   * @param array 数组
   * @param list 列表
   * @since  0.5
   */
  public static void addArrayToList(Object[] array, List<Object> list) {
    if (array == null || list == null || array.length == 0) {
      return;
    }
    for (int i = 0; i < array.length; i++) {
      list.add(array[i]);
    }
  }

  /**
   * 将数组中的内容全部添加到列表中。
   * @param array 数组
   * @param list 列表
   * @param start 开始位置
   * @since  0.5
   */
  public static void addArrayToList(Object[] array, List<Object> list, int start) {
    if (array == null || list == null || array.length == 0) {
      return;
    }
    for (int i = 0; i < array.length; i++) {
      list.add(start + i, array[i]);
    }
  }

  /**
   * 移动列表中的元素
   * @param list 列表
   * @param start 移动的元素的开始索引
   * @param end 移动的元素的最后索引，不包括这个
   * @param to 移动到的位置
   * @since  0.5
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
public static void moveElements(List<?> list, int start, int end, int to) {
    List subList = new ArrayList(list.subList(start, end));
    list.removeAll(subList);
    list.addAll(to, subList);
  }
  
  /**
   * 去除list中重复数据
   * 
   * @param list 
   * @param cleanPrimaryKey 是否去掉主键后在比较
   * @param primaryKey 主键
   * @return
   */

  public static List<HashMap> cleanList(List<HashMap> list,boolean cleanPrimaryKey,String primaryKey){
	  List<HashMap> resultList=new ArrayList<HashMap>();
	  HashMap<String, HashMap> msp = new HashMap<String, HashMap>();
	  for(int i = list.size()-1 ; i>=0; i--){
          HashMap map = list.get(i);
          if(cleanPrimaryKey){
        	  map.remove(primaryKey);
          }
          resultList.add(map);
      }
	  resultList=new ArrayList<HashMap>(new HashSet(resultList));
	  return resultList;
  }
}