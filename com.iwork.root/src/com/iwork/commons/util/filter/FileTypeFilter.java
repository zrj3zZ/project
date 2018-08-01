package com.iwork.commons.util.filter;

/**
 * <p>Copyright: Copyright (c) 2002-2003</p>
 * <p>Company: JavaResearch(http://www.javaresearch.org)</p>
 * <p>鏈�鍚庢洿鏂版棩鏈�:2003骞�3鏈�25鏃�
 * @author Cherami
 */

import java.io.*;

import com.iwork.commons.util.StringUtil;


/**
 * 鎵╁睍鍚嶆枃浠惰繃婊ゅ櫒銆�
 * @since  0.4
 */

public class FileTypeFilter
    extends javax.swing.filechooser.FileFilter {
  String[] suffixList;
  String description;
  boolean caseSensitive = false;
  /**
   * 浣跨敤鎸囧畾鐨勬墿灞曞悕鏋勯�犱竴涓狥ileTypeFilter銆�
   * @param suffix 鎵╁睍鍚�
   * @since  0.5
   */
  public FileTypeFilter(String suffix) {
    suffixList = new String[1];
    suffixList[0]=suffix;
  }
  /**
   * 浣跨敤鎸囧畾鐨勬墿灞曞悕鏁扮粍鏋勯�犱竴涓狥ileTypeFilter銆�
   * @param suffixList 鎵╁睍鍚嶆暟缁�
   * @since  0.4
   */
  public FileTypeFilter(String[] suffixList) {
    this.suffixList = suffixList;
  }

  /**
   * 浣跨敤鎸囧畾鐨勬墿灞曞悕鏁扮粍鏋勯�犱竴涓狥ileTypeFilter銆�
   * @param suffixList 鎵╁睍鍚嶆暟缁�
   * @param description 杩囨护鍣ㄧ殑鎻忚堪鏂囨湰
   * @since  0.4
   */
  public FileTypeFilter(String[] suffixList, String description) {
    this.suffixList = suffixList;
    this.description = description;
  }

  /**
   * 浣跨敤鎸囧畾鐨勬墿灞曞悕鏁扮粍鍜屾槸鍚﹀ぇ灏忓啓鏁忔劅鏋勯�犱竴涓狥ileTypeFilter銆�
   * @param suffixList 鎵╁睍鍚嶆暟缁�
   * @param caseSensitive 鏄惁澶у皬鍐欐晱鎰�
   * @since  0.4
   */
  public FileTypeFilter(String[] suffixList, boolean caseSensitive) {
    this.suffixList = suffixList;
    this.caseSensitive = caseSensitive;
  }

  /**
   * 浣跨敤鎸囧畾鐨勬墿灞曞悕鏁扮粍鍜屾槸鍚﹀ぇ灏忓啓鏁忔劅鏋勯�犱竴涓狥ileTypeFilter銆�
   * @param suffixList 鎵╁睍鍚嶆暟缁�
   * @param caseSensitive 鏄惁澶у皬鍐欐晱鎰�
   * @param description 杩囨护鍣ㄧ殑鎻忚堪鏂囨湰
   * @since  0.4
   */
  public FileTypeFilter(String[] suffixList, boolean caseSensitive,
                        String description) {
    this.suffixList = suffixList;
    this.caseSensitive = caseSensitive;
    this.description = description;
  }

  /**
   * 璁剧疆鏄惁澶у皬鍐欐晱鎰熴��
   * @param caseSensitive 鏄惁澶у皬鍐欐晱鎰�
   * @since  0.4
   */
  public void setCaseSensitive(boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  /**
   * 鏄惁澶у皬鍐欐晱鎰�
   * @return 澶у皬鍐欐晱鎰熸椂杩斿洖true锛屽惁鍒欒繑鍥瀎alse
   * @since  0.4
   */
  public boolean getCaseSensitive() {
    return caseSensitive;
  }

  /**
   * 鍒ゆ柇鎸囧畾鐨勬枃浠舵槸鍚﹀彲浠ヨ鎺ュ彈銆�
   * @param file 闇�瑕佸垽鏂殑鏂囦欢
   * @return 鎵╁睍鍚嶇鍚堟寚瀹氱殑瑕佹眰鏃惰繑鍥瀟rue锛屽惁鍒欒繑鍥瀎alse銆�
   * @since  0.4
   */
  public boolean accept(File file) {
    String filename = file.getName();
    int dot = filename.lastIndexOf(".");
    if (dot == -1) {
      return false;
    }
    return StringUtil.contains(suffixList, filename.substring(dot + 1),
                               caseSensitive);
  }

  /**
   * 杩斿洖杩囨护鍣ㄧ殑鎻忚堪瀛楃涓层��
   * @return 鎵�鏈夋墿灞曞悕鐨勫垪琛�
   * @since  0.4
   */
  public String getDescription() {
    if (description != null) {
      return description;
    }
    return StringUtil.combineStringArray(suffixList, "/");
  }
}
