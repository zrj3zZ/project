package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class UserIsRovingExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public  UserIsRovingExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 是否为游离账户
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getIsroving()+"";
	}
}
