package com.iwork.app.expand.runtime.expression;
import org.apache.log4j.Logger;
import java.util.Calendar;
import java.util.Date;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;

public class DateAddFormExpressionImpl extends ExpressionAbst {
	private static Logger logger = Logger.getLogger(DateAddFormExpressionImpl.class);
	private ExpressionParamsModel model;
	public DateAddFormExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
	}

	public String expressionParse(String expression) {
		String datepart = expression.substring(expression.indexOf("(") + 1, expression.indexOf(",")).toUpperCase();
		String num = expression.substring(expression.indexOf(",") + 1, expression.indexOf(",", expression.indexOf(",")+1)).toUpperCase();
		String date = expression.substring(expression.indexOf(",",expression.indexOf(",")+1) + 1, expression.lastIndexOf(")")).toUpperCase();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(UtilDate.getTimes(date)));
		
		int step=0;
		try{
			step=Integer.parseInt(num);
		}catch(Exception e){
			logger.error(e,e);
			return "${dateAdd()}第2个参数不是合法的数值类型!";
		}
		
		if(datepart.trim().toLowerCase().equals("year")){
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + step);	
		}
		if(datepart.trim().toLowerCase().equals("month")){
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + step);	
		}
		if(datepart.trim().toLowerCase().equals("week")){
			calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) + step);	
		}
		if(datepart.trim().toLowerCase().equals("day")){
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + step);	
		}
		return UtilDate.dateFormat(calendar.getTime());
	}
	

}



