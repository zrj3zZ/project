package com.iwork.commons.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.OutputStream;
import com.iwork.commons.util.filter.AllFileFilter;
import org.apache.log4j.Logger;

public class FileUtil {
	private static Logger logger = Logger.getLogger(FileUtil.class);
	public static final int BUFFER_SIZE = 16 * 1024;
	 /**
	   * 换行符。
	   * <b>由于在设计的时候忘记final变量是会经过预编译优化的，因此定义为final变量就没有跨平台的能力了。因此在0.5beta版中取消了final声明。</b>
	   * @since  0.12
	   */
	  public static String LINE_SEPARATOR = System.getProperty(
	      "line.separator");
	  /**
	   * 文件分隔符。
	   * <b>由于在设计的时候忘记final变量是会经过预编译优化的，因此定义为final变量就没有跨平台的能力了。因此在0.5beta版中取消了final声明。</b>
	   * @since  0.12
	   */
	  public static String FILE_SEPARATOR = System.getProperty(
	      "file.separator");
	  /**
	   * 路径分隔符。
	   * <b>由于在设计的时候忘记final变量是会经过预编译优化的，因此定义为final变量就没有跨平台的能力了。因此在0.5beta版中取消了final声明。</b>
	   * @since  0.12
	   */
	  public static String PATH_SEPARATOR = System.getProperty(
	      "path.separator");
	  
	  
	  public static void byte2File(byte[] buf, String filePath, String fileName)
		{
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = null;
			try
			{
				File dir = new File(filePath);
				if (!dir.exists() && dir.isDirectory())
				{
					dir.mkdirs();
				}
				file = new File(filePath + File.separator + fileName);
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				bos.write(buf);
			}
			catch (Exception e)
			{
				logger.error(e,e);
			}
			finally
			{
				if (bos != null)
				{
					try
					{
						bos.close();
					}
					catch (Exception e)
					{
						logger.error(e,e);
					}
				}
				if (fos != null)
				{
					try
					{
						fos.close();
					}
					catch (Exception e)
					{
						logger.error(e,e);
					}
				}
			}
		}
	/**
	 * 文件创建写入
	 * @param path   文件路径+文件名称
	 * @param content  写入正文
	 * @return
	 */
	  public  static boolean writeFile(String path,String content){
			boolean flag = false;
		      try {
		    	  File f = new File(path);
		    	  File parent = f.getParentFile(); 
		    	  if(parent!=null&&!parent.exists()){ 
		    		  parent.mkdirs(); 
		    	  } 
		       if (!f.exists()) {
			     f.createNewFile();
		       }
		       	BufferedWriter utput = new BufferedWriter(new FileWriter(f));
		       	utput.write(content);
		       	utput.close();
		       	flag = true;
		      } catch (Exception e) {
		    	  logger.error(e,e);
		      }
			return flag;
		}
	  /**
	   * 文件创建写入
	   * @param path   文件路径+文件名称
	   * @param content  写入正文
	   * @return
	   */
	  public  static boolean writeFile(String path,File srcFile){
		  boolean flag = false;
		  InputStream in = null;
			OutputStream out = null;
		  try {
			  File f = new File(path); 
			  File parent = f.getParentFile(); 
			  if(parent!=null&&!parent.exists()){ 
				  parent.mkdirs(); 
			  } 
			  if (!f.exists()) {
				  f.createNewFile();
			  } 
			    in = new BufferedInputStream(new FileInputStream(srcFile), BUFFER_SIZE);
			   out = new BufferedOutputStream(new FileOutputStream(f),BUFFER_SIZE);
			  	byte[] buffer = new byte[BUFFER_SIZE];
		        int offset = 0;
		        while ((offset = in.read(buffer, 0, buffer.length)) != -1) {
		            out.write(buffer, 0, offset);
		        } 
				out.flush();
				in.close();
				out.close();
			  flag = true;
		  } catch (Exception e) {
			  logger.error(e,e);
		  }
		  return flag;
	  }
	
	/**
	 * 删除指定目录下的所有文件
	 * @param folderPath 目录路径
	 * @return true:删除成功 false:删除失败
	 */
	public static boolean delAllFile(String folderPath) {
		boolean flag = false;
		File file = new File(folderPath);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (folderPath.endsWith(File.separator)) {
				temp = new File(folderPath + tempList[i]);
			} else {
				temp = new File(folderPath + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(folderPath + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(folderPath + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 删除指定文件
	 * @param filePath 指定文件的路径
	 * @return true:删除成功 false:删除失败
	 */
	public static boolean delFile(String filePath) {
		boolean flag = false;
		File file = new File(filePath);
		if (!file.exists()) {
			return flag;
		}
		flag = (new File(filePath)).delete();
		return flag;
	}

	/**
	 * 删除指定文件夹(包括文件夹下的所有文件)
	 * @param folderPath 指定文件夹路径
	 * @return true:删除成功 false:删除失败
	 */
	public static boolean delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			logger.error(e,e);
			return false;
		}
		return true;
	}
	
	
 	/**
 	 * 读取文本文件的内容
 	 * @param curfile   文本文件路径
 	 * @return 返回文件内容
 	 */
 	public static String readFile(String curfile)
 	{
			
		    try
		    {
		    File f = new File(curfile);
	 		  if (!f.exists()) return null;
	 		  FileReader cf=new FileReader(curfile);
			  BufferedReader is = new BufferedReader(cf);
			  String filecontent = ""; 			  
			  String str = is.readLine();
			  while (str != null){
			  	filecontent += str;
		    	str = is.readLine();
		    	if (str != null)
		    		filecontent += "\n";
			  }
			  is.close();
			  cf.close();
			  return filecontent; 
		    }
		    catch (Exception e)
		    {
		    	logger.error(e,e);
		      return null;
		    }
 		
 	}
 	
	/**
	 * 取指定文件的扩展名
	 * @param filePathName  文件路径
	 * @return 扩展名
	 */
	public static String getFileExt(String filePathName)
	{
	    int pos = 0;
	    pos = filePathName.lastIndexOf('.');
	    if(pos != -1)
	        return filePathName.substring(pos + 1, filePathName.length());
	    else
	        return "";
		
	}
	/**
	 * 获得驶入文件名称及路径，返回文件名
	 * @param strFileName
	 * @return
	 */
    public static String getFileName(String strFileName)
    {
        if(strFileName == null)
            return "";
        if(strFileName.length() == 0)
            return "";
        int nLen = strFileName.length();
        for(int i = nLen - 1; i >= 0; i--)
            if(strFileName.charAt(i) == '\\')
                return strFileName.substring(i + 1);

        return strFileName;
    }
	  /**
	   * 将文件名中的类型部分去掉。
	   * @param filename 文件名
	   * @return 去掉类型部分的结果
	   * @since  0.5
	   */
	  public static String trimType(String filename) {
	    int index = filename.lastIndexOf(".");
	    if (index != -1) {
	      return filename.substring(0, index);
	    }
	    else {
	      return filename;
	    }
	  }
	
	/**
	 * 拷贝文件到指定目录
	 * @param srcPath 源文件路径
	 * @param destPath 目标文件路径
	 * @return true:拷贝成功 false:拷贝失败
	 */
	  public static boolean copyFile(String srcPath,String destPath){
			boolean flag = false;
			File src = new File(srcPath);
			File dst = new File(destPath);
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
		        int offset = 0;
		        while ((offset = in.read(buffer, 0, buffer.length)) != -1) {
		            out.write(buffer, 0, offset);
		        }
				out.flush();
				flag = true;
			} catch (Exception e) {
				logger.error(e,e); 
				return false;
			} finally {
				if (null != in) {
					try {
						in.close();
					} catch (Exception e) {
						logger.error(e,e);
					}
				}
				if (null != out) {
					try {
						out.close();
					} catch (Exception e) {
						logger.error(e,e);
				}
				}
			} 
			return flag;
		}
	/**
	 * 拷贝文件到指定目录
	 * @param srcPath 源文件路径
	 * @param destPath 目标文件路径
	 * @return true:拷贝成功 false:拷贝失败
	 */
	  public static boolean copyFile(File src,File dst){
			boolean flag = false;
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
		        int offset = 0;
		        while ((offset = in.read(buffer, 0, buffer.length)) != -1) {
		            out.write(buffer, 0, offset);
		        }
				out.flush();
				flag = true;
			} catch (Exception e) {
				logger.error(e,e); 
				return false;
			} finally {
				if (null != in) {
					try {
						in.close();
					} catch (Exception e) {
						logger.error(e,e);
					}
				}
				if (null != out) {
					try {
						out.close();
					} catch (Exception e) {
						logger.error(e,e);
				}
				}
			} 
			return flag;
		}
	
	  /**
	   * 修改文件的最后访问时间。
	   * 如果文件不存在则创建该文件。
	   * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	   * @param file 需要修改最后访问时间的文件。
	   * @since  0.1
	   */
	  public static void touch(File file) {
		    long currentTime = System.currentTimeMillis();
		    if (!file.exists()) {
		      try {
		        file.createNewFile();
		      }
		      catch (Exception e) {
		        logger.error(e,e);
		      }
		    }
		    boolean result = file.setLastModified(currentTime);
		  }

	  /**
	   * 修改文件的最后访问时间。
	   * 如果文件不存在则创建该文件。
	   * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	   * @param fileName 需要修改最后访问时间的文件的文件名。
	   * @since  0.1
	   */
	  public static void touch(String fileName) {
	    File file = new File(fileName);
	    touch(file);
	  }

	  /**
	   * 修改文件的最后访问时间。
	   * 如果文件不存在则创建该文件。
	   * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	   * @param files 需要修改最后访问时间的文件数组。
	   * @since  0.1
	   */
	  public static void touch(File[] files) {
	    for (int i = 0; i < files.length; i++) {
	      touch(files[i]);
	    }
	  }

	  /**
	   * 修改文件的最后访问时间。
	   * 如果文件不存在则创建该文件。
	   * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	   * @param fileNames 需要修改最后访问时间的文件名数组。
	   * @since  0.1
	   */
	  public static void touch(String[] fileNames) {
	    File[] files = new File[fileNames.length];
	    for (int i = 0; i < fileNames.length; i++) {
	      files[i] = new File(fileNames[i]);
	    }
	    touch(files);
	  }

	  /**
	   * 列出目录中的所有内容，包括其子目录中的内容。
	   * @param fileName 要列出的目录的目录名
	   * @return 目录内容的文件数组。
	   * @since  0.1
	   */
	  public static File[] listAll(String fileName) {
	    return listAll(new File(fileName));
	  }

	  /**
	   * 列出目录中的所有内容，包括其子目录中的内容。
	   * @param file 要列出的目录
	   * @return 目录内容的文件数组。
	   * @since  0.1
	   */
	  public static File[] listAll(File file) {
	    ArrayList<File> list = new ArrayList<File>();
	    File[] files;
	    if (!file.exists() || file.isFile()) {
	      return null;
	    }
	    list(list, file, new AllFileFilter());
	    list.remove(file);
	    files = new File[list.size()];
	    list.toArray(files);
	    return files; 
	  }

	  /**
	   * 列出目录中的所有内容，包括其子目录中的内容。
	   * @param file 要列出的目录
	   * @param filter 过滤器
	   * @return 目录内容的文件数组。
	   * @since  0.1
	   */
	  public static File[] listAll(File file,
	                               javax.swing.filechooser.FileFilter filter) {
	    ArrayList<File> list = new ArrayList<File>();
	    File[] files;
	    if (!file.exists() || file.isFile()) {
	      return null;
	    }
	    list(list, file, filter);
	    files = new File[list.size()];
	    list.toArray(files);
	    return files;
	  }

	  /**
	   * 将目录中的内容添加到列表。
	   * @param list 文件列表
	   * @param filter 过滤器
	   * @param file 目录
	   */
	  private static void list(ArrayList<File> list, File file,
	                           javax.swing.filechooser.FileFilter filter) {
	    if (filter.accept(file)) {
	      list.add(file);
	      if (file.isFile()) {
	        return;
	      }
	    }
	    if (file.isDirectory()) {
	      File files[] = file.listFiles();
	      for (int i = 0; i < files.length; i++) {
	        list(list, files[i], filter);
	      }
	    }

	  }
	  /**
	   * 从文件名得到UNIX风格的文件绝对路径。
	   * @param fileName 文件名
	   * @return 对应的UNIX风格的文件路径
	   * @since  0.4
	   * @see #toUNIXpath(String filePath) toUNIXpath
	   */
	  public static String getUNIXfilePath(String fileName) {
	    File file = new File(fileName);
	    return toUNIXpath(file.getAbsolutePath());
	  }
	  
	  /**
	   * 将DOS/Windows格式的路径转换为UNIX/Linux格式的路径。
	   * 其实就是将路径中的"\"全部换为"/"，因为在某些情况下我们转换为这种方式比较方便，
	   * 某中程度上说"/"比"\"更适合作为路径分隔符，而且DOS/Windows也将它当作路径分隔符。
	   * @param filePath 转换前的路径
	   * @return 转换后的路径
	   * @since  0.4
	   */
	  private static String toUNIXpath(String filePath) {
	    return filePath.replace('\\', '/');
	  }
}
