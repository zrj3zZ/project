package com.iwork.plugs.dictionary.util;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iwork.core.engine.runtime.el.ExpressionFactory;
import com.iwork.core.engine.runtime.el.ExpressionInterface;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.engine.runtime.tools.RuntimeELUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;

public class DictionaryUtil {
	private static DictionaryUtil instance;  
    private static Object lock = new Object();  
	 public static DictionaryUtil getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new DictionaryUtil();  
	                }
	            }  
	        }  
	        return instance;  
	 }
	 
	/**
	 * 将宏变量转换成默认的值
	 * 
	 * @param defaultValue
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public String convertMacrosValue(String sql,Map map) {
		Pattern ptn = Pattern.compile("\\{(.*?)\\}");  
        Matcher m = ptn.matcher(sql);
        while (m.find()) {
           String label =  m.group(1);
           String text = "";
           Object obj = null;
           if(map!=null){
        	   obj = map.get(label);
           }
           if(obj!=null){
        	   if(obj instanceof BigDecimal){
        		   BigDecimal tmp = (BigDecimal)obj; 
        		   text = tmp.toString();
        	   }else if(obj instanceof String){
        		   text = obj.toString();
        	   }else if(obj instanceof String[]){
        		  String[] tmp = (String[])obj;
        		  if(tmp!=null&&tmp.length>0){
        			  text = tmp[0];
        		  }
        	   }else{
        		   text = obj.toString();
        	   }
           }
           if(!text.equals("")){
        	   sql = sql.replace("${"+label+"}", text); 
           }else{
        	   sql = sql.replace("${"+label+"}", ""); 
           }
        } 
       
        //动态运行时参数匹配
        Pattern ptn1 = Pattern.compile("%(.*?)%");  
        Matcher m1 = ptn1.matcher(sql);
        if (m1.find()) {
        	ExpressionParamsModel model = new ExpressionParamsModel();
     		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
     		model.setContext(uc);
     		//=======装载流程参数========
     		//获取动态变量
     		sql = RuntimeELUtil.getInstance().convertMacrosValue(sql,model);
        } 
		return sql;
	}
	public String getDirectaryEnumLabel(String enumStr,Map map){
		String value="";
		if(enumStr!=null){
			String enumField = enumStr;
			enumField = enumField.replace("${", "").replace("}","");
			Object obj = map.get(enumField);
			if(obj!=null){
				if(obj instanceof String[]){
					String[] o = (String[])obj;
					value = o[0];
				}else if(obj instanceof String){
					value = obj.toString();
				}
			}
		}
		return value;
	}
	/**
	 * 获得RV参数的值
	 * @param label
	 * @param model
	 * @return
	 */
	public String getVaribleValue(String label,ExpressionParamsModel model) {
		String src_label = label;
		//label = label.toLowerCase();
		ExpressionInterface expression = ExpressionFactory.getExpressionInstance(model, label);
		// 获得公式的解析
		String result = expression.expressionParse(label);
		if (label.equals(result)) {// 没有公式被执行
			return src_label;
		} else {
			return result;
		}
	}
}
