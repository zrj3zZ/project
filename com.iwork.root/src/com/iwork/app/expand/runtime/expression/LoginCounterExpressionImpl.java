package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class LoginCounterExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public LoginCounterExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 获取用户登录次数
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getLogincounter()+"";
	}
}
