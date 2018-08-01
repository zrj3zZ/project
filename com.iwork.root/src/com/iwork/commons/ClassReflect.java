

package com.iwork.commons;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class ClassReflect {

	/**
	 * 
	 * @param className
	 * @param parameterTypes
	 * @param initargs
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @preserve 声明此方法不被JOC混淆
	 */
	static public Object getInstance(String className, Class[] parameterTypes, Object[] initargs) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, InstantiationException, IllegalAccessException {
		return getInstance(getConstructor(className, parameterTypes), initargs);
	}

	/**
	 * 从一个构造器对象加载一个类实例
	 * 
	 * @param cons
	 *            Constructor 一个类的构造器
	 * @param initargs
	 *            Object[] 构造方法初始化参数的值
	 * @return Object
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @preserve 声明此方法不被JOC混淆
	 */
	static public Object getInstance(Constructor cons, Object[] initargs) throws IllegalArgumentException, InvocationTargetException, InstantiationException, IllegalAccessException {

		Object obj = null;
		obj = cons.newInstance(initargs);
		return obj;
	}

	/**
	 * 获取个类的构造器
	 * 
	 * @param className
	 *            String 类名称
	 * @param parameterTypes
	 *            Class[] 构造方法的参数类型
	 * @return Constructor
	 * @throws ClassNotFoundException
	 *             构造方法的参数值
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @preserve 声明此方法不被JOC混淆
	 */
	static public Constructor getConstructor(String className, Class[] parameterTypes) throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		Constructor con = null;
		Class reflectClass = Class.forName(className);
		con = reflectClass.getConstructor(parameterTypes);
		return con;
	}
}
