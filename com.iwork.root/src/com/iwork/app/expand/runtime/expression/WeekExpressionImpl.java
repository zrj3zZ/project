package com.iwork.app.expand.runtime.expression;


import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.runtime.el.ExpressionAbst;import com.iwork.core.engine.runtime.el.ExpressionParamsModel;

public class WeekExpressionImpl extends ExpressionAbst {

	public WeekExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
	}

	public String expressionParse(String expression) {
		return Integer.toString(UtilDate.getWeekOfYear());
	}
}
