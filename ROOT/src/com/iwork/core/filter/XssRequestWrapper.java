package com.iwork.core.filter;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import com.iwork.commons.util.DBUtilInjection;
public class XssRequestWrapper extends HttpServletRequestWrapper {  
    
    private static Policy policy = null;  
      
    static{  
        String path =XssRequestWrapper.class.getClassLoader().getResource("antisamy-anythinggoes.xml").getFile();  
        if(path.startsWith("file")){  
            path = path.substring(6);  
        }  
         try {  
            policy = Policy.getInstance(path);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public XssRequestWrapper(HttpServletRequest request) {  
        super(request);  
    }  
      
    @SuppressWarnings("rawtypes")  
    public Map<String,String[]> getParameterMap(){  
        Map<String,String[]> request_map = super.getParameterMap();  
        Iterator iterator = request_map.entrySet().iterator();  
        while(iterator.hasNext()){  
            Map.Entry me = (Map.Entry)iterator.next();  
            String[] values = (String[])me.getValue();  
            for(int i = 0 ; i < values.length ; i++){  
                values[i] = xssClean(values[i]);  
            }  
        }  
        return request_map;  
    }  
     public String[] getParameterValues(String paramString)  
      {  
        String[] arrayOfString1 = super.getParameterValues(paramString);  
        if (arrayOfString1 == null)  
          return null;  
        int i = arrayOfString1.length;  
        String[] arrayOfString2 = new String[i];  
        for (int j = 0; j < i; j++)  
          arrayOfString2[j] = xssClean(arrayOfString1[j]);  
        return arrayOfString2;  
      }  
  
      public String getParameter(String paramString)  
      {  
        String str = super.getParameter(paramString);  
        if (str == null)  
          return null;  
        return xssClean(str);  
      }  
  
      public String getHeader(String paramString)  
      {  
        String str = super.getHeader(paramString);  
        if (str == null)  
          return null;  
        return xssClean(str);  
      }  
        
        
    private String xssClean(String value) {   
    	if(value == null) return null;
    	DBUtilInjection d=new DBUtilInjection();
    	if(d.HasInjectionData(value)){
    		 value ="";
    	}
    	//xlj 增加判断条件，优化速度，保留换行符
    	if(value.equals("")) return value;
    	String strNEw = value.replace(".", "");
    	Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(strNEw);
        if(isNum.matches() ){
        	return value;
        }
        value = value.replaceAll("(?i)placeholder", "");
        value = value.replaceAll("(?i)prompt", "");
        value = value.replace("\n", "%5Cn");
        value = value.replace("<", "%3c").replace(">", "%3e");
        value = value.replaceAll("(?i)src=", "");
        value = value.replaceAll("(?i)response.write", "");
        value = value.replaceAll("(?i)nslookup", "");
        value = value.replaceAll("(?i)waitfor", "");
        value = value.replaceAll("(?i)print", "");
//        value = value.replaceAll("../", "");
        value = value.replaceAll("'", "");
    //    value = value.replaceAll("\"", "");
        
        AntiSamy antiSamy = new AntiSamy();  
        try {           
            final CleanResults cr = antiSamy.scan(value, policy);
            String str = cr.getCleanHTML().toString();
            //安全的HTML输出
            return str.replaceAll("&quot;", "\"").replace("%5Cn","\n");
        } catch (Exception e) {  
            value = null;
        } 
        return value;  
    }
    
    public static String xssCleans(String value){
    	AntiSamy antiSamy = new AntiSamy();
    	try {
			final CleanResults cr = antiSamy.scan(value,policy);
			String str = StringEscapeUtils.unescapeHtml(cr.getCleanHTML());
			str = str.replaceAll(antiSamy.scan("&nbsp;",policy).getCleanHTML(), "");
			return str;
		} catch (ScanException e) {
			e.printStackTrace();
		} catch (PolicyException e) {
			e.printStackTrace();
		}
    	return value;
    }
}