package com.iwork.app.expand.runtime.expression;
import java.sql.Timestamp;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;

public class DayExpressionImpl extends ExpressionAbst {

	public DayExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
	}

	public String expressionParse(String expression) {
		return UtilDate.dayFormat(new Timestamp(System.currentTimeMillis()));
	}
}
 