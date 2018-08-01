package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class DepartmentCompanyIdExpressionImpl extends ExpressionAbst {

	private UserContext _me;


	/**
	 * 获取组织单位id
	 * @param model
	 * @param expressionValue
	 */

	public DepartmentCompanyIdExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
	}

	public String expressionParse(String expression) {
		return _me.get_deptModel().getCompanyid();
	}
}
