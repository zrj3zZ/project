

package com.iwork.commons;

import java.lang.reflect.*;
import java.util.Date;
import java.util.Map;

import com.iwork.commons.util.UtilDate;

/**
 * 调试类信息
 * 
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class ClassInfo {
	
	/**
	 * 获取当前线程的调用堆栈调试信息
	 * @return
	 * @author jack
	 */
	public static String getThreadStackTraces(){
		String value="";
		Map activeThreads = Thread.getAllStackTraces();// 当前活动的线程
		StackTraceElement[] stacks = (StackTraceElement[]) activeThreads.get(Thread.currentThread());
		if (stacks != null) {
			for (int ii = 0; ii < stacks.length; ii++) {
				if (stacks[ii].getLineNumber() <= 0)
					value += stacks[ii].getClassName() + "." + stacks[ii].getMethodName() + "()\n";
				else
					value += stacks[ii].getClassName() + "." + stacks[ii].getMethodName() + "()第" + stacks[ii].getLineNumber() + "行\n";
			}
			value += "\n 记录时间：" + UtilDate.datetimeFormat(new Date());
		}
		activeThreads = null;
		return value;
	}

	/**
	 * 
	 * @param obj
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static void printClass(Object obj) {
		if (obj == null) {
			return;
		}
		Class c = obj.getClass();
		Field fields[] = null;
		fields = c.getFields();
		for (int i = 0; i < fields.length; i++) {
			Class classType = fields[i].getType();
			Object o = null;
			try {
				o = fields[i].get(obj);
			} catch (java.lang.IllegalAccessException e) {
			}
		}
	}

	private static String parseClassName(String sName) {
		int iPointPos = sName.lastIndexOf(".");
		int iAtPos = sName.indexOf("@");
		if ((iPointPos == -1) || (iAtPos == -1)) {
			return sName;
		}
		sName = sName.substring(iPointPos + 1, iAtPos);
		return sName;
	}

	public static void main(String[] args) {
		printClass("aaa");
		printClass("aaac");
	}

}
