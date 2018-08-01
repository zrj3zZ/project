package com.iwork.commons.util;
import java.text.DecimalFormat;
import org.apache.log4j.Logger;
import java.text.NumberFormat;
import java.util.Locale;
public class MoneyUtil {
	private static Logger logger = Logger.getLogger(MoneyUtil.class);
	/**
	 * 获得指定格式
	 * @param str"##,###,###,###,##0.00"
	 * @return
	 */
	public static String getDecimalFormat(String str){ 
		String format = "##,###,###,###,##0.00";
		if(str.indexOf(".")>1){
			int length = str.substring(str.indexOf(".")).length();
			if(length>2){
				StringBuffer tmp = new StringBuffer("##,###,###,###,##0.");
				length = length -1 ;
				for(int i=0;i<length;i++){
					tmp.append("0");
				}
				format = tmp.toString();
			} 
		}
		DecimalFormat   fmt   =   new   DecimalFormat(format);    
        String outStr = null;  
        double d;  
        try {  
            d = Double.parseDouble(str);  
            outStr = fmt.format(d);  
        } catch (Exception e) { logger.error(e,e); 
        }  
        return outStr;  
    }
	
	/**
	 * 获得人民币
	 * @param str
	 * @return
	 */
	public static String getCurrency(String str) { 
		 String outStr = "";  
		if(str!=null&&!"".equals(str)){
			 NumberFormat n = NumberFormat.getCurrencyInstance(Locale.CHINA);
		        n.setMaximumFractionDigits(2);
		        double d;  
		       
		        try {  
		             d = Double.parseDouble(str);  
		             outStr = n.format(d);  
		        } catch (Exception e) {  
		            logger.error(e,e);  
		        }  
		}
        return outStr;  
    }  
	/**
	 * 获得货币
	 * @param str
	 * @param locale
	 * @return
	 */
	public static String getCurrency(String str, java.util.Locale locale) { 
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.CHINA);
		n.setMaximumFractionDigits(2);
		double d;  
		String outStr = null;  
		try {  
			d = Double.parseDouble(str);  
			outStr = n.format(d);  
		} catch (Exception e) {  
			logger.error(e,e);  
		}  
		return outStr;  
	}  
}
