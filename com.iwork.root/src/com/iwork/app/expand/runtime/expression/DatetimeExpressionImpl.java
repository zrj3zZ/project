package com.iwork.app.expand.runtime.expression;

import java.sql.Timestamp;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;

public class DatetimeExpressionImpl extends ExpressionAbst {

	public DatetimeExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
	}

	public String expressionParse(String expression) {
		return UtilDate.datetimeFormat(new Timestamp(System.currentTimeMillis()));
	}
}
