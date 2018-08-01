package com.iwork.commons.util;

/**
 * <p>Copyright: Copyright (c) 2002-2003</p>
 * <p>Company: JavaResearch(http://www.javaresearch.org)</p>
 * <p>最后更新日期:2003年2月25日
 * @author Cherami
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

import java.awt.*;
import javax.swing.*;
import org.apache.log4j.Logger;
/**
 * 简化提取打包在Jar或者Zip文件中的资源。
 * 此类提供一些方法更方便的从Jar或者Zip文件中得到资源。
 * @since  0.4
 */
public final class JarResource {
	private static Logger logger = Logger.getLogger(JarResource.class);
  private HashMap<String, ZipEntry> entries = new HashMap<String, ZipEntry>();
  private HashMap<String, String> names = new HashMap<String, String>();

  private String fileName;
  private ZipFile file;

  /**
   * 根据指定的文件名创建JarResource。
   * @param fileName 文件名
   * @since  0.4
   */
  public JarResource(String fileName) {
    this.fileName = fileName;
    init();
  }

  /**
   * 根据文件名得到在压缩包中的ZipEntry。
   * 可能一个文件名会有多个可能的对应项，具体对应那一项不能确定。对于这样的情况请使用全限定路径。
   * @param fileName 文件名
   * @return 对应的压缩包中的ZipEntry，不存在时返回null
   */
  private ZipEntry getEntry(String fileName) {
    ZipEntry entry = (ZipEntry) entries.get(fileName);
    if (entry == null) {
      String entryName = (String) names.get(fileName);
      if (entryName != null) {
        return (ZipEntry) entries.get(entryName);
      }
      else {
        return null;
      }
    }
    else {
      return entry;
    }

  }

  /**
   * 提取指定的文件内容并返回一个字节数组。
   * @param fileName 资源的文件名
   * @return 指定的文件内容的字节数组
   * @since  0.4
   */
  public byte[] getResource(String fileName) {
    ZipEntry entry = getEntry(fileName);
    if (entry != null) {
      try {
        InputStream inputStream = file.getInputStream(entry);
        int length = inputStream.available();
        byte contents[] = new byte[length];
        inputStream.read(contents);
        inputStream.close();
        return contents;
      }
      catch (Exception e) {logger.error(e,e);
        return null;
      }
    }
    else {
      return null;
    }
  }

  /**
   * 提取指定的文件所代表的图像。
   * @param fileName 资源的文件名
   * @return 指定的文件所代表的图像
   * @since  0.4
   */
  public Image getImage(String fileName) {
    ZipEntry entry = getEntry(fileName);
    if (entry != null) {
      StringBuffer url = new StringBuffer("jar:file:/");
      url.append(FileUtil.getUNIXfilePath(this.fileName));
      url.append("!/");
      url.append(entry.getName());
      try {
        URL fileURL = new URL(url.toString());
        return new ImageIcon(fileURL).getImage();
      }
      catch (MalformedURLException e) {logger.error(e,e);
        return null;
      }
    }
    else {
      return null;
    }
  }

  /**
   * 提取指定的文件所代表的字符串。
   * @param fileName 资源的文件名
   * @return 指定的文件所代表的字符串
   * @since  0.4
   */
  public String getString(String fileName) {
    byte contents[] = getResource(fileName);
    if (contents != null) {
      return new String(contents);
    }
    else {
      return null;
    }
  }

  /**
   * 初始化内部的资源项HashMap。
   */
  private void init() {
    try {
      file = new ZipFile(fileName);
      Enumeration<?> enumeration = file.entries();
      while (enumeration.hasMoreElements()) {
        ZipEntry entry = (ZipEntry) enumeration.nextElement();
        if (!entry.isDirectory()) {
          entries.put(entry.getName(), entry);
          names.put(FileUtil.getFileName(entry.getName()), entry.getName());
        }
      }
    }
    catch (FileNotFoundException e) {
      logger.error(e,e);
    }
    catch (Exception e) {
      logger.error(e,e);
    }
  }

}
