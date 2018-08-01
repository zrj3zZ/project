package com.iwork.commons.util;

import java.util.regex.Pattern;

public class DBUtilInjection {
	 public boolean HasInjectionData(String inputData)
     {
         if ("".equals(inputData) || inputData==null)
             return false;

         //里面定义恶意字符集合
         //验证inputData是否包含恶意集合Regex.IsMatch(inputData.toLowerCase(), GetRegexString())
         // Pattern pattern = Pattern.compile("^[A-Za-z]+$"); 
         //pattern.matcher(start).matches();
         Pattern pattern = Pattern.compile(GetRegexString()); 
         if (pattern.matcher(inputData.toLowerCase()).matches())
         {
             return true;
         }
         else
         {
             return false;
         }
     }
	/* public static void main(String[] args) {
		 DBUtilInjection D =new DBUtilInjection();
		 if( D.HasInjectionData(" ch ")){
			 System.out.println("找到了");
		 }else{
			 System.out.println("没找到");
		 }
		
	 }*/
     /// <summary>
     /// 获取正则表达式
     /// </summary>
     /// <returns></returns>
     private static String GetRegexString()
     {
         //构造SQL的注入关键字符
    	 String[] strBadChar =
         {
             "(select)",
             "(\\sfrom\\s)",
             "(insert\\s)",
             "(delete\\s)",
             "(update\\s)",
             "(drop\\s)",
             "(truncate\\s)",
             "(exec\\s)",
             "(count\\()",
             "(declare\\s)",
             "(\\sasc)",
             "(mid\\()",
             "(char\\()",
             "(net user)",
             "(xp_cmdshell)",
             "(add\\s)",
             "(exec master.dbo.xp_cmdshell)",
             "(net localgroup administrators)",
             "(chr\\()",
             "(modify\\s)",
             "(alter\\s)",
             "(create\\s)",
             "(union\\s)",
             "(grant\\s)",
             "(declare\\s)",
             "(master\\s)",
             "(database\\s)",
             "(tablespace\\s)",
             "(when\\s)"
            
         };

         //构造正则表达式
         String str_Regex = ".*(";
         for (int i = 0; i < strBadChar.length - 1; i++)
         {
             str_Regex += strBadChar[i] + "|";
         }
         str_Regex += strBadChar[strBadChar.length - 1] + ").*";

         return str_Regex;
     }
}
