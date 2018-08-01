package com.iwork.app.expand.runtime.expression;

import java.sql.Timestamp;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.runtime.el.ExpressionAbst;import com.iwork.core.engine.runtime.el.ExpressionParamsModel;


/**
 * @author highsun
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 * @preserve 声明此方法不被JOC混淆
 */
public class MonthExpressionImpl extends ExpressionAbst {

	public MonthExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);

	}

	public String expressionParse(String expression) {
		return UtilDate.monthFormat(new Timestamp(System.currentTimeMillis()));
	}
}
