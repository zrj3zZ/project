package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class DepartmentNameExpressionImpl extends ExpressionAbst {

	private UserContext _me;
/**
 * 获取部门名称
 */

	private Long _activityInstanceId ;

	public DepartmentNameExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
		this._activityInstanceId = model.getInstanceid();
	}

	public String expressionParse(String expression) {
		return _me.get_deptModel().getDepartmentname();
	}
}
