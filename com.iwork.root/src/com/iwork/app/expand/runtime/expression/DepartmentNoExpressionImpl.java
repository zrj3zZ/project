package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;
public class DepartmentNoExpressionImpl extends ExpressionAbst {

	private UserContext _me;
	private Long _activityInstanceId ;

	public DepartmentNoExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
		this._activityInstanceId = model.getInstanceid();
	}

	public String expressionParse(String expression) {
		return _me.get_deptModel().getDepartmentno();
	}
}
