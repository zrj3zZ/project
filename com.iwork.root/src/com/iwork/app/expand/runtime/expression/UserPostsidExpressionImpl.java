package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class UserPostsidExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public UserPostsidExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 获取岗位编号
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getPostsid();
	}
}
