

package com.iwork.commons.util;

import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
/**
 * 字符编码工具
 * 
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class UtilCode {
	private static Logger logger = Logger.getLogger(UtilCode.class);
	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public UtilCode() {
	}

	/**
	 * This method uses a naive String to byte interpretation, it simply gets
	 * each char of the String and calls it a byte.
	 * </p>
	 * <p>
	 * Since we should be dealing with Base64 encoded Strings that is a
	 * reasonable assumption.
	 * </p>
	 * <p>
	 * <h4>End of data</h4>
	 * We don't try to stop the converion when we find the "=" end of data
	 * padding char. We simply add zero bytes to the unencode buffer.
	 * </p>
	 */
	public static int decodeSize;

	/**
	 * decode a Base 64 encoded String to data
	 * 
	 * @param encoded
	 *            Base64 Sting to convert
	 * @return byte[] data
	 */
	public static byte[] decodeBase64(String encoded) {
		int maxturns;
		int strNum = 0, zeroNum = 0;
		maxturns = encoded.length();
		// work out how long to loop for.
		/*
		 * if(encoded.length()%3==0) maxturns=encoded.length(); else
		 * maxturns=encoded.length()+(3-(encoded.length()%3));
		 */
		// tells us whether to include the char in the unencode
		boolean skip;
		// the unencode buffer

		byte[] decodeStr = new byte[(encoded.length() / 4 + 1) * 3];
		byte[] unenc = new byte[4];
		byte b;
		for (int i = 0, j = 0; i < maxturns; i++) {
			skip = false;
			// get the byte to convert or 0
			if (i < encoded.length()) {
				b = (byte) encoded.charAt(i);
			} else {
				b = 0;
				// test and convert first capital letters, lowercase, digits
				// then '+' and '/'
			}
			if (b >= 65 && b < 91) {
				unenc[j] = (byte) (b - 65);
			} else if (b >= 97 && b < 123) {
				unenc[j] = (byte) (b - 71);
			} else if (b >= 48 && b < 58) {
				unenc[j] = (byte) (b + 4);
			} else if (b == '+') {
				unenc[j] = 62;
			} else if (b == '/') {
				unenc[j] = 63;
				// if we find "=" then data has finished, we're not really
				// dealing with this now
			} else if (b == '=') {
				unenc[j] = 0;
				zeroNum++;
			} else {
				// char c=(char)b;
				if (b == '\n' || b == '\r' || b == ' ' || b == '\t') {
					skip = true;
				} else {

					// could throw an exception here? it's input we don't
					// understand.
					;
				}
			}
			// once the array has boiled convert the bytes back into chars
			if (!skip && ++j == 4) {
				// shift the 6 bit bytes into a single 4 octet word
				int res = ((unenc[0] << 18) & 0xFC0000) + ((unenc[1] << 12) & 0x3F000) + ((unenc[2] << 6) & 0xFC0) + (unenc[3] & 0x3F);
				byte c;
				int k = 16;
				// shift each octet down to read it as char and add to
				// StringBuffer
				while (k >= 0) {
					c = (byte) (res >> k);
					decodeStr[strNum++] = c;
					// if ( c > 0 )
					// sb.append((char)c);
					k -= 8;
				}
				if (zeroNum != 0) {
					strNum -= zeroNum;
					zeroNum = 0;
				}
				// reset j and the unencode buffer
				j = 0;
				unenc[0] = 0;
				unenc[1] = 0;
				unenc[2] = 0;
				unenc[3] = 0;
			}
		}
		decodeSize = strNum;
		byte[] retByte = new byte[strNum];
		for (int j = 0; j < strNum; j++) {
			retByte[j] = decodeStr[j];
		}
		return retByte;

	}
	 /** 
     * 把unicode转化为中文 
     * @param str 
     * @return 
     */  
    public static String unicodeToChinese(String str) {  
        if (str.indexOf("\\u") == -1 || str == null || "".equals(str.trim())) {/*若不是unicode，则直接返回*/  
            return str.replaceAll("\\\\ ", " ");//删掉英文中的\,such as "default\ value1"  
            /* 主要是针对 zk 中的国际化问题 */  
        }  
        StringBuffer sb = new StringBuffer();  
        if(!str.startsWith("\\u")){/*若开头不是unicode，如“abc\u4e2d\u56fd” */  
            int index=str.indexOf("\\u");  
            sb.append(str.substring(0, index));  
            str=str.substring(index);  
        }  
        if (str.endsWith(":")) /*如“\u4e2d\u56fd：” */{  
            str = str.substring(0, str.length() - 1);  
        }  
        String[] chs = str.trim().split("\\\\u");  
          
        for (int i = 0; i < chs.length; i++) {  
            String ch = chs[i].trim();  
            if (ch != null && !"".equals(ch)) {  
  
                sb.append((char) Integer.parseInt(ch.substring(0, 4), 16));  
                if (ch.length() > 4) {  
                    sb.append(ch.substring(4));  
                }  
            }  
        }  
        return sb.toString();  
    }  
	/**
	 * GBK转换为unicode字符编码
	 * @param gbString
	 * @return
	 */
	 public static String convertGBK2Unicode(final String gbString) {  
	        char[] utfBytes = gbString.toCharArray();  
	        String unicodeBytes = "";  
	        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {  
	            String hexB = Integer.toHexString(utfBytes[byteIndex]);  
	            if (hexB.length() <= 2) {  
	                hexB = "00" + hexB;  
	            }  
	            unicodeBytes = unicodeBytes + "\\u" + hexB;  
	        }  
	        return unicodeBytes;  
	  } 
	 
	/**
	 * encode plain data to a base 64 string
	 * 
	 * @param plain
	 *            the data to convert.
	 * @return the encoded text
	 * @param plainLength
	 */
	public static String encodeBase64(byte[] plain, int plainLength) {
		// if(plain.length()>76)
		// return null;
		int maxturns, line = 0;
		StringBuffer sb = new StringBuffer();
		// the encode buffer
		byte[] enc = new byte[3];
		boolean end = false;
		for (int i = 0, j = 0; !end; i++) {
			// char _ch=plain.charAt(i);
			if (i == plainLength - 1) {
				end = true;
			}
			enc[j++] = plain[i];
			if (j == 3 || end) {
				int res;
				// this is a bit inefficient at the end point
				// worth it for the small decrease in code size?
				res = ((enc[0] << 16) & 0xFF0000) + ((enc[1] << 8) & 0xFF00) + (enc[2] & 0xFF);
				int b;
				int lowestbit = 18 - (j * 6);
				for (int toshift = 18; toshift >= lowestbit; toshift -= 6) {
					b = res >> toshift;
					b &= 63;
					if (b >= 0 && b < 26) {
						sb.append((char) (b + 65));
					}
					if (b >= 26 && b < 52) {
						sb.append((char) (b + 71));
					}
					if (b >= 52 && b < 62) {
						sb.append((char) (b - 4));
					}
					if (b == 62) {
						sb.append('+');
					}
					if (b == 63) {
						sb.append('/');
					}
					if ((sb.length() - line) % 76 == 0) {
						sb.append('\n');
						line += 1;
					}

				}
				if (end) {
					if (j == 1) {
						sb.append("==");
					}
					if (j == 2) {
						sb.append('=');
					}
				}
				enc[0] = 0;
				enc[1] = 0;
				enc[2] = 0;
				j = 0;
			}
		}
		return sb.toString();
	}

	/**
	 * decode a Quoted-Printable String to data see RFC2045 for QP format
	 * 
	 * @param plain:
	 *            Quoted-Printable Sting to convert
	 * @return byte[] data
	 */
	public static byte[] decodeQP(String plain) {
		int i, tmp, index = 0, dec = 20, dig = 20;
		byte[] b = new byte[3];
		byte[] decode = new byte[plain.length()];

		for (i = 0; i < plain.length(); i++) {
			b[0] = (byte) plain.charAt(i);
			if (b[0] == '=') {
				if ((i + 1) < plain.length()) {
					b[1] = (byte) plain.charAt(i + 1);
				} else {
					b[1] = 0;
				}
				if ((i + 2) < plain.length()) {
					b[2] = (byte) plain.charAt(i + 2);
				} else {
					b[2] = 0;
				}
				if (b[1] >= '0' && b[1] <= '9') {
					dec = b[1] - 48;
				} else if (b[1] >= 'A' && b[1] <= 'F') {
					dec = b[1] - 55;
				} else if (b[1] >= 'a' && b[1] <= 'f') {
					dec = b[1] - 87;
				} else {
					dec = 20;
				}
				if (dec != 20) {
					if (b[2] >= '0' && b[2] <= '9') {
						dig = b[2] - 48;
					} else if (b[2] >= 'A' && b[2] <= 'F') {
						dig = b[2] - 55;
					} else if (b[2] >= 'a' && b[2] <= 'f') {
						dig = b[2] - 87;
					} else {
						dig = 20;
					}
					if (dig != 20) {
						tmp = dec * 16 + dig;
						decode[index++] = (byte) (tmp & 0xFF);
						i += 2;
						dig = 20;
						dec = 20;
						continue;
					} else {
						decode[index++] = (byte) dec;
						i += 1;
						continue;
					}
				} else {
					if (b[1] == '\r') {
						if (b[2] == '\n') {
							i += 2;
						} else {
							i++;
						}
						continue;
					}
					if (b[1] == '\n') {
						i++;
						continue;
					}
				}
			}
			decode[index++] = b[0];
		}

		decodeSize = index;
		byte[] retByte = new byte[index];
		for (int j = 0; j < index; j++) {
			retByte[j] = decode[j];
		}
		return retByte;
	}

	/**
	 * decode a HZ String to GB String
	 * 
	 * @param hzstring:
	 *            HZ Sting to convert
	 * @return GB String
	 */
	public static String hz2gb(String hzstring) {
		byte[] hzbytes = new byte[2];
		byte[] gbchar = new byte[2];
		int byteindex = 0;
		StringBuffer gbstring = new StringBuffer();

		// Convert to look like equivalent Unicode of GB
		for (byteindex = 0; byteindex < hzstring.length(); byteindex++) {
			if (hzstring.charAt(byteindex) == 0x7e) {
				if (hzstring.charAt(byteindex + 1) == 0x7b) {
					byteindex += 2;
					while (byteindex < hzstring.length()) {
						if (hzstring.charAt(byteindex) == 0x7e && hzstring.charAt(byteindex + 1) == 0x7d) {
							byteindex++;
							break;
						} else if (hzstring.charAt(byteindex) == 0x0a || hzstring.charAt(byteindex) == 0x0d) {
							gbstring.append((char) hzstring.charAt(byteindex));
							break;
						}
						gbchar[0] = (byte) (hzstring.charAt(byteindex) + 0x80);
						gbchar[1] = (byte) (hzstring.charAt(byteindex + 1) + 0x80);
						try {
							gbstring.append(new String(gbchar));
						} catch (Exception e) {
							logger.error(e,e);
						}
						byteindex += 2;
					}
				} else if (hzstring.charAt(byteindex + 1) == 0x7e) { // ~~
					// becomes
					// ~
					gbstring.append('~');
				} else { // false alarm
					gbstring.append((char) hzstring.charAt(byteindex));
				}
			} else {
				gbstring.append((char) hzstring.charAt(byteindex));
			}
		}
		return gbstring.toString();
	}

	/**
	 * decode encoded String to GB String
	 * 
	 * @param plain:
	 *            String to convert
	 * @return String decode String like: =?chartype?encodetype?encodedString?=
	 *         ~{HZ-String~} chartype: gb2312,iso-8809-1,etc; encodetype:
	 *         B(Base64), Q(QP), etc notes: if plain contains characters not
	 *         us-ascii, these characters will be converted according to the
	 *         platform's default character encoding. So, if you're not sure
	 *         about what plain contains, just use next function: byte[]
	 *         decode(byte[] plain);
	 */
	public static String chartype;

	/***/
	public static String encodetype;

	/**
	 * AWF平台默认的编码格式
	 * 
	 * @param plain
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String decode(String plain) {
		return plain;
	}

	/**
	 * @param plain 
	 */
	public static byte[] decode(byte[] plain) {
		int i, k, seg = 0;
		int pos, next = 0, lastpos = 0, tmppos;
		int begin, middle, end;
		int bufLength = 0;
		byte[] bytetemp;
		byte[] bufByte = new byte[plain.length];
		String code = "";

		boolean valid = true, endofstr = false;

		for (i = 0; i < plain.length && endofstr == false; i++) {
			begin = byteIndexOf(plain, "=?", i);
			if (begin == -1) {
				for (; i < plain.length; i++, bufLength++) {
					bufByte[bufLength] = plain[i]; // gbstring.append(plain.substring(i));
				}
				endofstr = true;
				continue;
			}
			if (begin > i) {
				for (int j = i; j < begin; j++, bufLength++) { // gbsring.append(plain.substring(i,begin));
					bufByte[bufLength] = plain[j];
				}
			}
			begin += 2;
			if ((middle = byteIndexOf(plain, "?", begin)) == -1) {
				valid = false;
			} else {
				chartype = new String(plain, begin, middle - begin); // plain.substring(begin,middle);
				if ((end = byteIndexOf(plain, "?", ++middle)) == -1) {
					valid = false;
				} else {
					encodetype = new String(plain, middle, end - middle); // plain.substring(middle,end);
					if ((next = byteIndexOf(plain, "=?", ++end)) == -1) {
						next = plain.length;
					}
					if ((pos = byteIndexOf(plain, "?=", end)) == -1) {
						valid = false;
					} else if ((pos - 1) > next) {
						for (int j = begin - 2; j < next; j++, bufLength++) { // gbstring.append(plain.substring(begin-2,next));
							bufByte[bufLength] = plain[j];
						}
						i = next - 1;
						continue;
					} else {
						lastpos = pos;
						while (pos != -1 && pos < next) {
							lastpos = pos;
							tmppos = pos + 1;
							pos = byteIndexOf(plain, "?=", tmppos);
						}
						code = new String(plain, end, lastpos - end); // plain.substring(end,lastpos);
					}
				}
			}
			if (valid == false) {
				for (int j = begin - 2; j < plain.length; j++, bufLength++) { // gbstring.append(plain.substring(begin));
					bufByte[bufLength] = plain[j];
				}
				endofstr = true;
			} else {
				if (encodetype.equalsIgnoreCase("B")) {
					bytetemp = decodeBase64(code);
					for (int j = 0; j < decodeSize; j++, bufLength++) { // gbstring.append(new
						// String(bytetemp));
						bufByte[bufLength] = bytetemp[j];
					}
					i = lastpos + 1;
				} else if (encodetype.equalsIgnoreCase("Q")) {
					bytetemp = decodeQP(code);
					for (int j = 0; j < decodeSize; j++, bufLength++) { // gbstring.append(new
						// String(bytetemp));
						bufByte[bufLength] = bytetemp[j];
					}
					i = lastpos + 1;
				} else { // unknown encodingtype
					for (int j = begin - 2; j < next; j++, bufLength++) { // gbstring.append(plain.substring(begin-2,next));
						bufByte[bufLength] = plain[j];
					}
					i = next - 1;
				}
				valid = true;
			}
		}
		byte[] gbByte = new byte[bufLength];
		// for (int j=0; j<bufLength; j++) gbByte[j] = bufByte[j];
		// abc ~{9XSZNRCG~} ~{NRCG@4K5~}
		int start = 0;
		int gbNum = 0;

		while (true) {
			if ((begin = byteIndexOf(bufByte, "~{", start)) != -1 && (end = byteIndexOf(bufByte, "~}", begin + 2)) != -1) {
				for (int j = start; j < begin; j++, gbNum++) {
					gbByte[gbNum] = bufByte[j];
				}
				String tempstr = hz2gb(new String(bufByte, begin, end + 2 - begin));
				try {
					//xingbotao GB2312-->GBK
					byte[] tempbyte = tempstr.getBytes("GBK");
					for (int j = 0; j < tempbyte.length; j++, gbNum++) {
						gbByte[gbNum] = tempbyte[j];
					}
				} catch (Exception e) {
					logger.error(e,e);
				}
				start = end + 2;
			} else {
				for (int j = start; j < bufLength; j++, gbNum++) {
					gbByte[gbNum] = bufByte[j];
				}
				break;
			}
		}
		decodeSize = gbNum;
		byte[] retByte = new byte[gbNum];
		for (int j = 0; j < gbNum; j++) {
			retByte[j] = gbByte[j];
		}
		return retByte;
	}

	/**
	 * encoding-->GBK
	 * 
	 * @param str
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String convert2GBK(String encoding, String str) {
		try {
			byte[] b = str.getBytes(encoding);
			return new String(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			logger.error(e,e);
			return null;
		}
	}

	/**
	 * encoding-->iso-8859_1
	 * 
	 * @param str
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String convert2ISO_8859_1(String encoding, String str) {
		try {
			byte[] b = str.getBytes(encoding);
			return new String(b, "iso_8859_1");
		} catch (UnsupportedEncodingException e) {
			logger.error(e,e);
			return null;
		}
	}
	/**
	 * encoding-->iso-8859_1
	 * 
	 * @param str
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String convert2UTF8(String str) {
//		String temp  =  "";
//		try {
//			//temp = new String(str.getBytes("iso8859-1"),"UTF-8"); 
//		} catch (UnsupportedEncodingException e) {
//			logger.error(e,e);
//			return null;
//		}
		return str;
	}

	private static int byteIndexOf(byte[] buf, String str, int begin) {
		int strlen, j;
		byte[] strByte = str.getBytes();
		strlen = strByte.length;
		if (begin >= buf.length) {
			return -1;
		}
		for (int i = begin; i < buf.length; i++) {
			if (buf[i] == strByte[0]) {
				if ((i + strlen - 1) < buf.length) {
					for (j = 1; j < strlen; j++) {
						if (buf[i + j] == strByte[j]) {
							continue;
						} else {
							break;
						}
					}
					if (j == strlen) {
						return i;
					} else {
						continue;
					}
				} else {
					return -1;
				}
			}
		}
		return -1;
	}

}
