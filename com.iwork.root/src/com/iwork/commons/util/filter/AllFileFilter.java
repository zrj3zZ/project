package com.iwork.commons.util.filter;

/**
 * <p>Copyright: Copyright (c) 2002-2003</p>
 * <p>Company: JavaResearch(http://www.javaresearch.org)</p>
 * <p>鏈�鍚庢洿鏂版棩鏈�:2003骞�2鏈�12鏃�
 * @author Cherami
 */

import java.io.*;

/**
 * 鍏ㄩ儴鏂囦欢杩囨护鍣ㄣ��
 * @since  0.1
 */

public class AllFileFilter
    extends javax.swing.filechooser.FileFilter {
  /**
   * 鍒ゆ柇鎸囧畾鐨勬枃浠舵槸鍚﹀彲浠ヨ鎺ュ彈銆�
   * @param file 闇�瑕佸垽鏂殑鏂囦欢
   * @return 鍦ㄤ换浣曟儏鍐甸兘杩斿洖true銆�
   * @since  0.1
   */
  public boolean accept(File file) {
    return true;
  }

  /**
   * 杩斿洖杩囨护鍣ㄧ殑鎻忚堪瀛楃涓层��
   * @return 杩囨护鍣ㄧ殑鎻忚堪瀛楃涓测�淎ll Files鈥�
   * @since  0.1
   */
  public String getDescription() {
    return "All Files";
  }
}
