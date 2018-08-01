package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class RoleTypeExpressionImpl extends ExpressionAbst {

	private UserContext _me;

/**
 * 获取角色类型
 * @param model
 * @param expressionValue
 */
	public RoleTypeExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
	}

	public String expressionParse(String expression) {
		return _me.get_orgRole().getRoletype();
	}
}
