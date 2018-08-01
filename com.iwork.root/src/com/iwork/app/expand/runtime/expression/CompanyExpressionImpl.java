package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class CompanyExpressionImpl extends ExpressionAbst {

	private UserContext _me;
	/**
	 * 获取公司名称
	 * @param model
	 * @param expressionValue
	 */
	public CompanyExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
	}

	public String expressionParse(String expression) {
		return _me.get_companyModel().getCompanyname();
}
}
