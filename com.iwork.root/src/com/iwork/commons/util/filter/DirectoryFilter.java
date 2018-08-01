package com.iwork.commons.util.filter;

/**
 * <p>Copyright: Copyright (c) 2002-2003</p>
 * <p>Company: JavaResearch(http://www.javaresearch.org)</p>
 * <p>鏈�鍚庢洿鏂版棩鏈�:2003骞�2鏈�12鏃�
 * @author Cherami
 */

import java.io.*;

/**
 * 鐩綍杩囨护鍣ㄣ��
 * @since  0.1
 */

public class DirectoryFilter
    extends javax.swing.filechooser.FileFilter {
  /**
   * 鍒ゆ柇鎸囧畾鐨勬枃浠舵槸鍚﹀彲浠ヨ鎺ュ彈銆�
   * @param file 闇�瑕佸垽鏂殑鏂囦欢
   * @return 鏂囦欢鏄竴涓洰褰曟椂杩斿洖true锛屽惁鍒欒繑鍥瀎alse銆�
   * @since  0.1
   */
  public boolean accept(File file) {
    return file.isDirectory();
  }

  /**
   * 杩斿洖杩囨护鍣ㄧ殑鎻忚堪瀛楃涓层��
   * @return 杩囨护鍣ㄧ殑鎻忚堪瀛楃涓测�淒irectories鈥濄��
   * @since  0.1
   */
  public String getDescription() {
    return "Directories";
  }
}
