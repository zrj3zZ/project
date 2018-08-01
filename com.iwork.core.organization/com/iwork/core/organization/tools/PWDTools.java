package com.iwork.core.organization.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;
public class PWDTools {
	private static Logger logger = Logger.getLogger(PWDTools.class);
	 /**
     * 生成密码.
     * @param count 密码位数
     * @param letters 是否包含字符
     * @param numbers 是否包含数字
     * @return String password
     */
    public static String getPassword(int count, boolean letters, boolean numbers) {
        return org.apache.commons.lang.RandomStringUtils.random(count, letters, numbers);
    }
    /**
     * 生成字符数字混合的密码.
     * @param count 密码位数
     * @return String password
     */
    private static String getPassword(int count) {
        return getPassword(count, true, true);
    }
    /**
     * 生成纯数字密码.
     * @param count 密码位数
     * @return String password
     */
    public static String getPasswordOfNumber(int count) {
        return getPassword(count, false, true);
    }
    /**
     * 生成纯字符密码.
     * @param count 密码位数
     * @return String password
     */
    public static String getPasswordOfCharacter(int count) {
        return getPassword(count, true, false);
    }
    /**
     * 生成3DES密钥.
     * @param key_byte seed key
     * @throws Exception
     * @return javax.crypto.SecretKey Generated DES key
     */
    public static javax.crypto.SecretKey genDESKey(byte[] key_byte) throws Exception {
        SecretKey k = new SecretKeySpec(key_byte, "DESede");
        return k;
    }
    /**
     * 3DES 解密(byte[]).
     * @param key SecretKey
     * @param crypt byte[]
     * @throws Exception
     * @return byte[]
     */
    public static byte[] desDecrypt(javax.crypto.SecretKey key, byte[] crypt) throws Exception {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DESede");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(crypt);
    }
    /**
     * 3DES 解密(String).
     * @param key SecretKey
     * @param crypt byte[]
     * @throws Exception
     * @return byte[]
     */
    public static String desDecrypt(javax.crypto.SecretKey key, String crypt) throws Exception {
        return new String(desDecrypt(key, crypt.getBytes()));
    }
    /**
     * 3DES加密(byte[]).
     * @param key SecretKey
     * @param src byte[]
     * @throws Exception
     * @return byte[]
     */
    public static byte[] desEncrypt(javax.crypto.SecretKey key, byte[] src) throws Exception {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DESede");
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(src);
    }
    /**
     * 3DES加密(String).
     * @param key SecretKey
     * @param src byte[]
     * @throws Exception
     * @return byte[]
     */
    public static String desEncrypt(javax.crypto.SecretKey key, String src) throws Exception {
        return new String(desEncrypt(key, src.getBytes()));
    }
    /**
     * MD5 摘要计算(byte[]).
     * @param src byte[]
     * @throws Exception
     * @return byte[] 16 bit digest
     */
    public static byte[] md5Digest(byte[] src) throws Exception {
        java.security.MessageDigest alg = java.security.MessageDigest.getInstance("MD5");
        // MD5 is 16 bit message digest
        return alg.digest(src);
    }
    /**
     * MD5 摘要计算(String).
     * @param src String
     * @throws Exception
     * @return String
     */
    public static String md5Digest(String src) throws Exception {
        return new String(md5Digest(src.getBytes()));
    }
    /**
     * BASE64 编码.
     * @param src String inputed string
     * @return String returned string
     */
    public static String base64Encode(String src) {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        return encoder.encode(src.getBytes());
    }
    /**
     * BASE64 编码(byte[]).
     * @param src byte[] inputed string
     * @return String returned string
     */
    public static String base64Encode(byte[] src) {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        return encoder.encode(src);
    }
    /**
     * BASE64 解码.
     * @param src String inputed string
     * @return String returned string
     */
    public static String base64Decode(String src) {
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            return new String(decoder.decodeBuffer(src));
        } catch (Exception e) {
        	logger.error(e,e);
            return null;
        }
    }
    /**
     * BASE64 解码(to byte[]).
     * @param src String inputed string
     * @return String returned string
     */
    public static byte[] base64DecodeToBytes(String src) {
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            return decoder.decodeBuffer(src);
        } catch (Exception e) {
        	logger.error(e,e);
            return null;
        }
    }
    /**
     * 对给定字符进行 URL 编码GB2312.
     * @param src String
     * @return String
     */
    public static String urlEncode(String src) {
        return urlEncode(src, "GB2312");
    }
    /**
     * 对给定字符进行 URL 解码GB2312
     * @param value 解码前的字符串
     * @return 解码后的字符串
     */
    public static String urlDecode(String value) {
        return urlDecode(value, "GB2312");
    }
    /**
     * 对给定字符进行 URL 编码.
     * @param src String
     * @param coder 字符编码格式（GB2312/GBK）
     * @return String
     */
    public static String urlEncode(String src, String coder) {
        try {
            src = java.net.URLEncoder.encode(src, coder);
            return src;
        } catch (Exception e) {
        	logger.error(e,e);
        }
        return src;
    }
    /**
     * 对给定字符进行 URL 解码
     * @param value 解码前的字符串
     * @param coder 字符编码格式（GB2312/GBK）
     * @return 解码后的字符串
     */
    public static String urlDecode(String value, String coder) {
        try {
            return java.net.URLDecoder.decode(value, coder);
        } catch (Exception e) {
        	logger.error(e,e);
        }
        return value;
    }
    /**
     * 执行给定url
     * @param urlString 给定的url
     * @return 返回值
     */
    public static String executeURL(String urlString) throws Exception {
        StringBuffer document = new StringBuffer();
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null)
            document.append(line + "/n");
        reader.close();
        
        return document.toString();
    }
}
