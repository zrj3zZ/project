package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class UserStartDateExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public UserStartDateExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 获取用户有效期的起始日期
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getStartdate().toString();
	}
}
