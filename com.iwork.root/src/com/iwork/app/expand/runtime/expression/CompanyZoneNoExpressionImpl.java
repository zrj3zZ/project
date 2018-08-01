package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class CompanyZoneNoExpressionImpl extends ExpressionAbst {

	private UserContext _me;


	/**
	 * 获取组织地区编号
	 * @param model
	 * @param expressionValue
	 */

	public CompanyZoneNoExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
	}

	public String expressionParse(String expression) {
		String zoneno= _me.get_companyModel().getZoneno();
		if(zoneno==null)
			zoneno="null";
		return zoneno;
	}
}
