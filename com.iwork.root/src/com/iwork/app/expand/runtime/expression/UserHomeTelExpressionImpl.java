package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class UserHomeTelExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public UserHomeTelExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 获取家庭电话
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getHometel();
	}
}
