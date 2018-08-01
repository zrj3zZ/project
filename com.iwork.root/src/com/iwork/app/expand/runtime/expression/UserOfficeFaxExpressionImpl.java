package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class UserOfficeFaxExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public UserOfficeFaxExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 获取办公室传真
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getOfficefax();
	}
}
