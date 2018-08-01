package com.iwork.commons.util;

import java.util.Vector;
import net.sourceforge.pinyin4j.PinyinHelper;  
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;  
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;  
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;  
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;  
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


/**
 * 扩展String功能
 *
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class UtilString {
    //static int count=0;

    /***/
    public String strSuper;

    /***/
    protected String lowcaseSuper;

    /**
     * Constructor a nullString of UtilString
     *
     * @preserve 声明此方法不被JOC混淆
     */
    public UtilString() {
        strSuper = new String();
        lowcaseSuper = strSuper;
    }

    public UtilString(String strInit) {
        strSuper = new String(strInit);
        lowcaseSuper = strInit.toLowerCase();
    }

    /**
     * 将字符串按strSign字串作为分割符分割字符串，分割到一个Vector
     *
     * @param
     * @return        return a Vector;
     * @preserve 声明此方法不被JOC混淆
     */
    public Vector split(String strSign) {
        int begin = 0;
        int end = 0;
        Vector vecResult = new Vector();

        if (strSign == "") {
            int i;

            for (i = 0; i < strSuper.length(); i++) {
                vecResult.add(strSuper.substring(i, i + 1));
            }

            return vecResult;
        }

        end = strSuper.indexOf(strSign);

        if (end == -1) {
            vecResult.add(strSuper);

            return vecResult;
        } else {
            while (end >= 0) {
                vecResult.add(strSuper.substring(begin, end));
                begin = end + strSign.length();
                end = strSuper.indexOf(strSign, begin);
            }

            vecResult.add(strSuper.substring(begin, strSuper.length()));

            return vecResult;
        }
    }

    /**
     * 从0位置寻找字符串strBegin在strSuper中出现的位置
     * 不区分大小写
     *
     * @param strBegin 一个将要查找的字符串
     * @return 如果找到返回字符串首位置，否则返回-1;
     * @preserve 声明此方法不被JOC混淆
     */
    public int matchOf(String strBegin) {
        return matchOf(strBegin, false);
    }

    /**
     * 从0位置寻找字符串strBegin在strSuper中出现的位置
     *
     * @param strBegin   将要查找的字符串
     * @param ignoreCase 是否区分大小写，为true时表示区分
     * @return 如果找到返回字符串首位置，否则返回-1;
     * @preserve 声明此方法不被JOC混淆
     */
    public int matchOf(String strBegin, boolean ignoreCase) {
        int pos = -1;

        if (ignoreCase) {
            pos = lowcaseSuper.indexOf(strBegin.toLowerCase());
        } else {
            pos = strSuper.indexOf(strBegin);
        }

        if (pos < 0) {
            return pos;
        } else {
            return (pos + strBegin.length());
        }
    }

    /**
     * 从末尾寻找字符串strEnd在strSuper中出现的位置
     * 不区分大小写
     *
     * @param        strEnd ?strBegin?   一个将要查找的字符串
     * @return        如果找到返回字符串首位置，否则返回-1;
     * @preserve 声明此方法不被JOC混淆
     */
    public int lastMatchOf(String strEnd) {
        return lastMatchOf(strEnd, false);
    }

    /**
     * 从0位置寻找字符串strEnd在strSuper中出现的位置
     *
     * @param strEnd     将要查找的字符串
     * @param ignoreCase 是否区分大小写，为true时表示区分
     * @return 如果找到返回字符串首位置，否则返回-1;
     * @preserve 声明此方法不被JOC混淆
     */
    public int lastMatchOf(String strEnd, boolean ignoreCase) {
        if (ignoreCase) {
            return lowcaseSuper.lastIndexOf(strEnd.toLowerCase());
        } else {
            return strSuper.lastIndexOf(strEnd);
        }
    }

    /**
     * 截取标记为strBegin与strEnd之间的字符串
     * 不区分大小写
     *
     * @param strBegin 一个头标志
     * @param strEnd   尾标志
     * @return 如果找到返回被截取的字符串值，否则返回空字符串;
     * @preserve 声明此方法不被JOC混淆
     */
    public String matchValue(String strBegin, String strEnd) {
        return matchValue(strBegin, strEnd, false);
    }

    /**
     * 截取标记为strBegin与strEnd之间的字符串
     *
     * @param strBegin   一个头标志
     * @param strEnd     尾标志
     * @param ignoreCase 是否区分大小写，为true时表示区分
     * @return 如果找到返回被截取的字符串值，否则返回空字符串;
     * @preserve 声明此方法不被JOC混淆
     */
    public String matchValue(String strBegin, String strEnd, boolean ignoreCase) {
        String strResult = new String();
        int posBegin = this.matchOf(strBegin, ignoreCase);
        int posEnd = this.lastMatchOf(strEnd, ignoreCase);

        if ((posEnd > posBegin) && (posBegin != -1)) {
            strResult = strSuper.substring(posBegin, posEnd);
        }

        return strResult;
    }

    /**
     * 将strOld替换为strNew
     *
     * @param strOld 被替换字符串
     * @param strNew 替换值
     * @preserve 声明此方法不被JOC混淆
     */
    public String replace(String strOld, String strNew) {
        return replace(strOld, strNew, false);
    }

    /**
     * 将strOld替换为strNew
     *
     * @param strOld     被替换字符串
     * @param strNew     替换值
     * @param ignoreCase 是否区分大小写，为true时表示区分
     * @preserve 声明此方法不被JOC混淆
     */
    public String replace(String strOld, String strNew, boolean ignoreCase) {
        //String strResult = new String();
        String strResult = new String();
        String strCore;

        String strReplace;

        if (ignoreCase) {
            strCore = lowcaseSuper;
            strReplace = strOld.toLowerCase();
        } else {
            strCore = strSuper;
            strReplace = strOld;
        }

        int posBegin = 0;
        int posEnd = strCore.indexOf(strReplace);

        // nothing should be replaced
        if (posEnd < 0) {
            return strSuper;
        }

        //        StringBuffer sbResult=new StringBuffer();
        //	    while (posEnd >= 0) {
        //	    	sbResult.append(strSuper.substring(posBegin, posEnd));
        //	    	sbResult.append(strNew);
        //	    	posBegin = posEnd + strReplace.length();
        //	    	posEnd = strCore.indexOf(strReplace, posBegin);
        //		}
        //	    sbResult.append(strSuper.substring(posBegin));
        //		//strResult += strSuper.substring(posBegin);
        // something should be replaced
        while (posEnd >= 0) {
            strResult += strSuper.substring(posBegin, posEnd);
            strResult += strNew;
            posBegin = posEnd + strReplace.length();
            posEnd = strCore.indexOf(strReplace, posBegin);
        }

        strResult += strSuper.substring(posBegin);

        return strResult.toString();
    }

    /**
     * 重指定开始的位置strBegin将strOld替换为strNew，strBegin是位置的标志字符串
     * 不区分大小写
     *
     * @param        strBegin 开始执行替换动作的标志字符串
     * @param        strEnd ?strOld?     替换值
     * @param        strNew 被替换字符串
     * @preserve 声明此方法不被JOC混淆
     */
    public String replace(String strBegin, String strEnd, String strNew) {
        return replace(strBegin, strEnd, strNew, false);
    }

    /**
     * 重指定开始的位置strBegin将strOld替换为strNew，strBegin是位置的标志字符串
     *
     * @param        strBegin 开始执行替换动作的标志字符串
     * @param        strEnd ?strOld?       替换值
     * @param        strNew 被替换字符串
     * @param        ignoreCase 是否区分大小写，为true时表示区分
     * @preserve 声明此方法不被JOC混淆
     */
    public String replace(String strBegin, String strEnd, String strNew,
        boolean ignoreCase) {
        int posBegin = this.matchOf(strBegin, ignoreCase);
        int posEnd = this.lastMatchOf(strEnd, ignoreCase);
        String strResult = new String();

        if ((posEnd >= posBegin) && (posBegin != -1)) {
            strResult = strSuper.substring(0, posBegin);
            strResult += strNew;
            strResult += strSuper.substring(posEnd);

            return strResult;
        } else {
            return strSuper;
        }
    }

    /**
     * @param        strBegin
     * @preserve 声明此方法不被JOC混淆
     */
    public boolean startsWith(String strBegin) {
        return startsWith(strBegin, false);
    }

    /**
     * @param        strBegin
     * @param        ignoreCase
     * @preserve 声明此方法不被JOC混淆
     */
    public boolean startsWith(String strBegin, boolean ignoreCase) {
        if (ignoreCase) {
            return lowcaseSuper.startsWith(strBegin.toLowerCase());
        } else {
            return strSuper.startsWith(strBegin);
        }
    }

    /**
     * @param        strEnd
     * @preserve 声明此方法不被JOC混淆
     */
    public boolean endsWith(String strEnd) {
        return endsWith(strEnd, false);
    }

    /**
     * @param        strEnd
     * @param        ignoreCase
     * @preserve 声明此方法不被JOC混淆
     */
    public boolean endsWith(String strEnd, boolean ignoreCase) {
        if (ignoreCase) {
            return lowcaseSuper.endsWith(strEnd.toLowerCase());
        } else {
            return strSuper.endsWith(strEnd);
        }
    }

    /**
     * 将多余字符变成..
     *
     * @param str
     * @param length
     * @return
     * @preserve 声明此方法不被JOC混淆
     */
    public static String cutString(String str, int length) {
        if (str == null) {
            return "";
        }

        if ((str.length() > length) && (length > 2)) {
            return str.substring(0, length) + ".";
        } else {
            return str;
        }
    }
    
    public static String getPingYin(String src) {  
    	  
        char[] t1 = null;  
        t1 = src.toCharArray();  
        String[] t2 = new String[t1.length];  
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();  
          
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);  
        String t4 = "";  
        int t0 = t1.length;  
        try {  
            for (int i = 0; i < t0; i++) {  
                // 判断是否为汉字字符  
                if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {  
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);  
                    t4 += t2[0];  
                } else  
                    t4 += java.lang.Character.toString(t1[i]);  
            }  
            return t4;  
        } catch (BadHanyuPinyinOutputFormatCombination e) {
        }  
        return t4;  
    }
    
   
}
