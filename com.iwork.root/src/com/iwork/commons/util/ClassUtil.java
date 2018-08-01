package com.iwork.commons.util;

/**
 * <p>Copyright: Copyright (c) 2002-2003</p>
 * <p>Company: JavaResearch(http://www.javaresearch.org)</p>
 * <p>最后更新日期:2003年3月1日
 * @author Cherami
 */
import java.lang.reflect.*;
import java.util.*;
import java.io.*;
import org.apache.log4j.Logger;
/**
 * 涉及类的方法等的工具类。
 * @since  0.4
 */

public class ClassUtil {
	private static Logger logger = Logger.getLogger(ClassUtil.class);
  /**
   * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
   */
  private ClassUtil() {
  }

  /**
   * 根据类名得到该类所有的pulbic成员方法的方法名。
   * @param className 类名
   * @return 所有的pulbic成员方法的方法名数组
   * @since  0.4
   */
  public static String[] getMethods(String className) {
    String methodNames[];
    try {
      Class<?> c = Class.forName(className);
      Method methods[] = c.getMethods();
      methodNames = new String[methods.length];
      for (int i = 0; i < methods.length; i++) {
        methodNames[i] = methods[i].toString();
      }
      return methodNames;
    }

    catch (ClassNotFoundException e) {
      logger.error(e,e);
      return null;
    }
  }

  /**
   * 根据类名得到该类所申明的方法的方法名。
   * @param className 类名
   * @return 所申明的方法的方法名数组
   * @since  0.4
   */
  public static String[] getDeclaredMethods(String className) {
    String methodNames[];
    try {
      Class<?> c = Class.forName(className);
      Method methods[] = c.getDeclaredMethods();
      methodNames = new String[methods.length];
      for (int i = 0; i < methods.length; i++) {
        methodNames[i] = methods[i].toString();
      }
      return methodNames;
    }

    catch (ClassNotFoundException e) {
      logger.error(e,e);
      return null;
    }
  }

  /**
   * 判断一个对象数组是否是同质数组。
   * 这里的同质是严格的同质，即他们的实际类类型必须完全相同。
   * @param objects 要比较的对象数组
   * @return 所有元素的类类型完全相同时返回true，如果数组中只有一个元素时也返回true，否则返回false
   * @since  0.5
   */
  public static boolean isSameClassType(Object[] objects) {
    if (objects.length == 1) {
      return true;
    }
    Class<? extends Object> c = objects[0].getClass();
    for (int i = 1; i < objects.length; i++) {
      if (!c.equals(objects[i].getClass())) {
        return false;
      }
    }
    return true;
  }

  /**
   * 判断一个对象数组是否是同质数组且是指定的类类型。
   * 这里的同质是严格的同质，即他们的实际类类型必须完全相同。
   * @param objects 要比较的对象数组
   * @param c 类类型
   * @return 所有元素的类类型和c完全相同时返回true，如果数组中只有一个元素时也返回true，否则返回false
   * @since  0.5
   */
  public static boolean isSameClassType(Object[] objects, Class<?> c) {
    if (objects.length == 1) {
      return true;
    }
    for (int i = 1; i < objects.length; i++) {
      if (!c.equals(objects[i].getClass())) {
        return false;
      }
    }
    return true;
  }

  /**
   * 判断child是否是c的一个子类。
   * @param c 父类
   * @param child 要判断的可能的子类
   * @return child是c的子类的时候返回true，其他所有情况下都返回false
   * @since  0.5
   */
  public static boolean isSubclass(Class<?> c, Class<?> child) {
    try {
      if (c.isInstance(child.newInstance())) {
        return true;
      }
      return false;
    }
   /* catch (IllegalAccessException e) {logger.error(e,e);
      return false;
    }
    catch (InstantiationException e) {logger.error(e,e);
      return false;
    }*/
    catch (Exception e) {logger.error(e,e);
      return false;
    }
  }
  /**
   * 得到JDK1.4所有的类名集合，只包含org,java,javax包及其子包下的类的类名。
   * 每个元素的值只是类名，没有包名以及扩展名java。
   * @return 类名的集合
   * @since  0.5
   */
  @SuppressWarnings("finally")
public static TreeSet<?> getAllClassname() {
    TreeSet<?> classnameSet=new TreeSet<Object>();
    try {
      InputStream istream = ClassUtil.class.getResourceAsStream("allclassname.treeset");
      ObjectInputStream p = new ObjectInputStream(istream);
      classnameSet = (TreeSet<?>)p.readObject();
      istream.close();
    }
    catch (Exception e) {
      logger.error(e,e);
    }
    finally {
      return classnameSet;
    }
  }

}