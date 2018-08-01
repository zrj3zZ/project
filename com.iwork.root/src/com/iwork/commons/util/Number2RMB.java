

package com.iwork.commons.util;

/**
 * 数值格式化工具
 * 
 * @author tianjinwei
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class Number2RMB {

	private static String HanDigiStr[] = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	private static String HanDiviStr[] = new String[] { "", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟" };
	
	/**
	 * @param NumStr
	 * @return
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String convertRMB(String NumStr) {
		// 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
		String RMBStr = "";
		boolean lastZero = false;
		boolean hasValue = false; // 亿、万进位前有数值标记
		boolean blZero=false;//亿、万位为零的标志，
		int len, n;
		len = NumStr.length();
		if (len > 15)
			return "数值过大!";
		for (int i = len - 1; i >= 0; i--) {
			if (NumStr.charAt(len - i - 1) == ' ')
				continue;
			n = NumStr.charAt(len - i - 1) - '0';
			if (n < 0 || n > 9)
				return "输入含非数字字符!";

			if (n != 0) {
				if (lastZero)
					RMBStr += HanDigiStr[0]; // 若干零后若跟非零值，只显示一个零
				// 除了亿万前的零不带到后面
				// if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) ) //
				// 如十进位前有零也不发壹音用此行
				//if (!(n == 1 && (i % 4) == 1 && i == len - 1)) // 十进位处于第一位不发壹音
				if((i%4==3)&& blZero)  //亿位为0，万位非0需要读零
					RMBStr +=HanDigiStr[0];
				RMBStr += HanDigiStr[n];
				RMBStr += HanDiviStr[i]; // 非零值后加进位，个位为空
				hasValue = true; // 置万进位前有值标记

			} else {
				
				if ((i % 8) == 0 || ((i % 8) == 4 && hasValue)) // 亿万之间必须有非零值方显示万
					RMBStr += HanDiviStr[i]; // “亿”或“万”			
			}
			if (i % 8 == 0)
				hasValue = false; // 万进位前有值标记逢亿复位
			lastZero = (n == 0) && (i % 4 != 0);
			blZero=(n == 0) && (i % 4 == 0);
		}

		if (RMBStr.length() == 0)
			return HanDigiStr[0]; // 输入空字符或"0"，返回"零"
		return RMBStr;
	}

	public static String convert(double var) {
		String SignStr = "";
		String TailStr = "";
		
		long fraction, integer;
		int yuan,jiao, fen;

		if (var < 0) {
			var = -var;
			SignStr = "负";
		}
		if (var > 99999999999999.999 || var < -99999999999999.999)
			return "数值位数过大!";
		// 四舍五入到分
		long temp = Math.round(var * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;
		String tempStr=String.valueOf(integer);
		yuan=tempStr.charAt(tempStr.length()-1)-'0';
		
//		if (jiao == 0 && fen == 0) {
//			TailStr = "整";		
//		}else{
//			TailStr = HanDigiStr[jiao];
//			if (jiao != 0)
//				TailStr += "角";
//			if (integer == 0 && jiao == 0) // 零元后不写零几分
//				TailStr = "";
//			if (fen != 0)
//				TailStr += HanDigiStr[fen] + "分";
//		}
/*@2006-7-11
 * @Danielxyd
 * 
 */    
		

		    if (jiao == 0 && fen == 0)            //  1.00   壹元整
			    TailStr = "整";		
		    if (jiao != 0 && fen == 0){           //   1.10   壹元壹角整
		    	if(yuan==0){
		    		TailStr = "零"+HanDigiStr[jiao]+ "角整";  //0.10  零壹角整
		    	}else{
		    	TailStr = HanDigiStr[jiao]+ "角整";}}			    
		    if (jiao == 0 && fen != 0)          //   1.01   壹元零壹分		    
		    	TailStr ="零"+ HanDigiStr[fen] + "分";
		    if (jiao != 0 && fen != 0){         //    1.11   壹元壹角壹分
		    	if(yuan==0){
		    		TailStr = "零"+HanDigiStr[jiao]+ "角" + HanDigiStr[fen] + "分"; //0.11 零壹角壹分
		    	}else{		    		
				TailStr = HanDigiStr[jiao]+ "角" + HanDigiStr[fen] + "分";}}                
	       	
        
		// 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
		// if( !integer ) return SignStr+TailStr;

		return SignStr + convertRMB(String.valueOf(integer)) + "元" + TailStr;
	}

}// END class 
