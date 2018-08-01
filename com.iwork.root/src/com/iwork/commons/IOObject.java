

package com.iwork.commons;

import java.io.*;
import org.apache.log4j.Logger;
/**
 * 将可序列化的类写入到一个文件或读出创建一个对象实例
 * 
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class IOObject {
	private static Logger logger = Logger.getLogger(IOObject.class);

	/**
	 * 写一个序列化的对象到指定文件
	 * 
	 * @param obj
	 *            可序列化的类
	 * @param filename
	 *            要保存的文件名称
	 * @throws IOObjectException
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static void writeObject(Object obj, String filename) throws IOObjectException {
		try {
			FileOutputStream ostream = new FileOutputStream(filename);
			ObjectOutputStream p = new ObjectOutputStream(ostream);
			p.writeObject(obj);
			p.flush();
			ostream.close();
		} catch (Exception e) {
			logger.error(e,e);
		}
	}

	/**
	 * 读一个序列化文件，并实例化成一个实例对象
	 * 
	 * @param filename
	 *            被序列化后的文件名称
	 * @return Object a Object Instance
	 * @throws IOObjectException
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static Object readObject(String filename) throws IOObjectException {
		Object obj = null;
		try {
			File f = new File(filename);
			if (!f.exists()) {

			} else {
				FileInputStream istream = new FileInputStream(f);
				ObjectInputStream p = new ObjectInputStream(istream);
				obj = p.readObject();
				istream.close();
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		return obj;
	}
}
