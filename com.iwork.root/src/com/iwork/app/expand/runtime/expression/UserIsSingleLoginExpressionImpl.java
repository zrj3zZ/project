package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class UserIsSingleLoginExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public  UserIsSingleLoginExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 是否控制单点登录
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getIssinglelogin()+"";
	}
}
