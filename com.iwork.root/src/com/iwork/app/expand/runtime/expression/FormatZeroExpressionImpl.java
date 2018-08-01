package com.iwork.app.expand.runtime.expression;

import com.iwork.commons.util.UtilString;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class FormatZeroExpressionImpl extends ExpressionAbst {
	private UserContext _me;
	private ExpressionParamsModel model;

	public FormatZeroExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
	}

	public String expressionParse(String expression) {		

		expression = expression.trim();
		
		String value = expression.substring(expression.indexOf(",") + 1, expression.lastIndexOf(")")).toLowerCase();
		if(value.length()==0)return value;
		
		String strLength = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(",")).toLowerCase();
		if(strLength.trim().length()==0)return value;
		int maxLength=Integer.parseInt(strLength);
		
		value = new UtilString(value).replace("_MACROS%C1%F5%BD%F0%D6%F9MACROS_".toLowerCase(), "+");
		
		String zero="";
		for(int i=value.length();i<maxLength;i++){
			zero+="0";
		}
		return zero+value;
	}
	
	

}

