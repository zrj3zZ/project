package com.iwork.commons.util;

import java.net.URLDecoder;
import java.security.MessageDigest;
import org.apache.log4j.Logger;
/**
 * 字符编码转换
 * @author David.Yang
 *
 */
public class PubEcoding
{	private static Logger logger = Logger.getLogger(PubEcoding.class);

	public static String LOGIN_OBJ = "IWORK-LOGIN-FORM";
	
    public PubEcoding()
    {
    }

    public static String ISO2GB(String sSrc)
    {
        String sRet = "";
        try
        {
            sRet = new String(sSrc.getBytes("ISO8859_1"), "GB2312");
        }
        catch(Exception e)
        {
            logger.error(e,e);
        }
        return sRet;
    }

    public static String UTF2ISO(String sSrc)
    {
        String sRet = "";
        try
        {
            sRet = new String(sSrc.getBytes("UTF-8"), "ISO8859_1");
        }
        catch(Exception e)
        {
            logger.error(e,e);
        }
        return sRet;
    }

    public static String UTF2GB(String sSrc)
    {
        String sRet = "";
        try
        {
            sRet = new String(sSrc.getBytes("UTF-8"), "GB2312");
        }
        catch(Exception e)
        {
            logger.error(e,e);
        }
        return sRet;
    }

    public static String ISO2UTF8(String sSrc)
    {
        String sRet = "";
        try
        {
            sRet = new String(sSrc.getBytes("ISO8859_1"), "UTF-8");
        }
        catch(Exception e)
        {
            logger.error(e,e);
        }
        return sRet;
    }

    public static String GB2ISO(String sSrc)
    {
        String sRet = "";
        try
        {
            sRet = new String(sSrc.getBytes("GB2312"), "ISO8859_1");
        }
        catch(Exception e)
        {
            logger.error(e,e);
        }
        return sRet;
    }

    public static String GB2UTF8(String sSrc)
    {
        String sRet = "";
        try
        {
            sRet = new String(sSrc.getBytes("GB2312"), "UTF-8");
        }
        catch(Exception e)
        {
            logger.error(e,e);
        }
        return sRet;
    }



    public static String TokenOf(String sSrc, int n, char cDelimiter)
    {
        int nLen = sSrc.length();
        int nIndex = 0;
        String sRet = new String("");
        if(sSrc == null || nLen == 0 || n < 0)
            return sRet;
        for(int i = 0; i < nLen; i++)
        {
            if(cDelimiter == sSrc.charAt(i))
            {
                nIndex++;
                continue;
            }
            if(nIndex > n)
                break;
            if(nIndex == n)
                sRet = sRet + sSrc.charAt(i);
        }

        return sRet;
    }

    public static String GetStrFromTo(String sSrc, String sFrom, String sTo)
    {
        int nFrom = 0;
        int nTo = 0;
        String sRet = "";
        nFrom = sSrc.indexOf(sFrom, nTo);
        if(nFrom == -1)
            return sRet;
        nTo = sSrc.indexOf(sTo, nFrom + sFrom.length());
        if(nTo == -1)
        {
            return sRet;
        } else
        {
            sRet = sSrc.substring(nFrom + sFrom.length(), nTo);
            return sRet;
        }
    }

    public static String[] split(String sSrc, String sDst)
    {
        int n = 0;
        int nPos = 0;
        int nLast = 0;
        String sRet[] = null;
        if(sSrc == null || sSrc.equals(""))
        {
            sRet = new String[1];
            sRet[0] = "";
            return sRet;
        }
        if(sDst == null || sDst.equals(""))
        {
            sRet = new String[1];
            sRet[0] = sSrc;
            return sRet;
        }
        while((nPos = sSrc.indexOf(sDst, nPos)) != -1) 
        {
            nPos++;
            n++;
        }
        if(n == 0)
        {
            sRet = new String[1];
            sRet[0] = sSrc;
            return sRet;
        }
        sRet = new String[n + 1];
        nPos = 0;
        n = 0;
        for(; (nPos = sSrc.indexOf(sDst, nPos)) != -1; nPos++)
        {
            sRet[n] = sSrc.substring(nLast, nPos);
            nLast = nPos + sDst.length();
            n++;
        }

        sRet[n] = sSrc.substring(nLast);
        return sRet;
    }



//    public static void setRowsPerPage(Command cmd, int nRows)
//    {
//        if(cmd == null)
//            return;
//        PageControl pc = cmd.pageControl;
//        if(pc.currentPage < 1)
//            pc.currentPage = 1;
//        if(nRows < 1)
//            nRows = 20;
//        int curPage = pc.currentPage;
//        pc.fromRow = (curPage - 1) * nRows + 1;
//        pc.toRow = (pc.fromRow + nRows) - 1;
//    }

    private static String UTF8toGBKChar(String str)
    {
        int n = 0;
        String sRet = "";
        try
        {
            n = Integer.parseInt(str, 16);
            char c = (char)n;
            sRet = sRet + c;
        }
        catch(NumberFormatException e)
        {logger.error(e,e);
            sRet = "&#x" + str + ";";
        }
        return sRet;
    }

    private static byte[] getHexInt(String hexString)
    {
        byte byteArray[] = new byte[2];
        int val = Integer.parseInt(hexString, 16);
        byteArray[0] = (byte)(val >>> 8 & 0xff);
        byteArray[1] = (byte)(val >>> 0 & 0xff);
        return byteArray;
    }

    public static String UTF8toGBK(String strUTF8)
    {
        if(strUTF8 == null || strUTF8.equals(""))
            return strUTF8;
        String sRet = "";
        String utf8 = "";
        boolean bIsUTF8 = false;
        boolean bBreak = false;
        int i = 0;
        int nLast = 0;
        for(int nPos = 0; (nPos = strUTF8.indexOf("&#x", nPos)) != -1;)
        {
            sRet = sRet + strUTF8.substring(nLast, nPos);
            utf8 = "";
            bIsUTF8 = false;
            bBreak = false;
            for(i = nPos + 3; i < strUTF8.length(); i++)
            {
                char c = strUTF8.charAt(i);
                switch(c)
                {
                case 48: // '0'
                case 49: // '1'
                case 50: // '2'
                case 51: // '3'
                case 52: // '4'
                case 53: // '5'
                case 54: // '6'
                case 55: // '7'
                case 56: // '8'
                case 57: // '9'
                case 65: // 'A'
                case 66: // 'B'
                case 67: // 'C'
                case 68: // 'D'
                case 69: // 'E'
                case 70: // 'F'
                case 97: // 'a'
                case 98: // 'b'
                case 99: // 'c'
                case 100: // 'd'
                case 101: // 'e'
                case 102: // 'f'
                    utf8 = utf8 + strUTF8.charAt(i);
                    break;

                case 59: // ';'
                    sRet = sRet + UTF8toGBKChar(utf8);
                    bIsUTF8 = true;
                    bBreak = true;
                    break;

                case 38: // '&'
                    i--;
                    bBreak = true;
                    break;

                case 39: // '\''
                case 40: // '('
                case 41: // ')'
                case 42: // '*'
                case 43: // '+'
                case 44: // ','
                case 45: // '-'
                case 46: // '.'
                case 47: // '/'
                case 58: // ':'
                case 60: // '<'
                case 61: // '='
                case 62: // '>'
                case 63: // ?
                case 64: // '@'
                case 71: // 'G'
                case 72: // 'H'
                case 73: // 'I'
                case 74: // 'J'
                case 75: // 'K'
                case 76: // 'L'
                case 77: // 'M'
                case 78: // 'N'
                case 79: // 'O'
                case 80: // 'P'
                case 81: // 'Q'
                case 82: // 'R'
                case 83: // 'S'
                case 84: // 'T'
                case 85: // 'U'
                case 86: // 'V'
                case 87: // 'W'
                case 88: // 'X'
                case 89: // 'Y'
                case 90: // 'Z'
                case 91: // '['
                case 92: // '\\'
                case 93: // ']'
                case 94: // '^'
                case 95: // '_'
                case 96: // '`'
                default:
                    bBreak = true;
                    break;
                }
                if(bBreak)
                    break;
            }

            if(!bIsUTF8)
                sRet = sRet + strUTF8.substring(nPos, i + 1);
            nLast = nPos = i + 1;
        }

        sRet = sRet + strUTF8.substring(nLast);
        return sRet;
    }

    public static byte[] md5(byte input[])
    {
        byte digest[] = null;
        try
        {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            alg.update(input);
            digest = alg.digest();
        }
        catch(Exception e)
        {
            logger.error(e,e);
            return digest;
        }
        return digest;
    }

    public static String byte2hex(byte b[])
    {
        String hs = "";
        String stmp = "";
        for(int n = 0; n < b.length; n++)
        {
            stmp = Integer.toHexString(b[n] & 0xff);
            if(stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if(n < b.length - 1)
                hs = hs + ":";
        }

        return hs.toUpperCase();
    }

    public static String byte2hexEx(byte b[])
    {
        String hs = "";
        String stmp = "";
        for(int n = 0; n < b.length; n++)
        {
            stmp = Integer.toHexString(b[n] & 0xff);
            if(stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }

        return hs;
    }

    public static void main(String agr[])
        throws Exception
    {
        String str = "yyyy\u5E74MM\u6708dd\u65E5";
        str = URLDecoder.decode("%E6%B5%8B+%E8%AF%95a%20?aa", "UTF-8");
    }
}
