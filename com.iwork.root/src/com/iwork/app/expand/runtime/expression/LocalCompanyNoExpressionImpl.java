package com.iwork.app.expand.runtime.expression;


import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class LocalCompanyNoExpressionImpl extends ExpressionAbst {

	private UserContext _me;

	public LocalCompanyNoExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
	}

	public String expressionParse(String expression) {
		return _me.get_companyModel().getCompanyno();
	}
}
