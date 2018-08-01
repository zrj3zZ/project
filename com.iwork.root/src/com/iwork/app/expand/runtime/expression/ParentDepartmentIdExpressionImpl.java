package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class ParentDepartmentIdExpressionImpl extends ExpressionAbst {

	private UserContext _me;


	/**
	 * 获取上级部门id
	 * @param model
	 * @param expressionValue
	 */

	public ParentDepartmentIdExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
	}

	public String expressionParse(String expression) {
		return _me.get_deptModel().getParentdepartmentid()+"";
	}
}
