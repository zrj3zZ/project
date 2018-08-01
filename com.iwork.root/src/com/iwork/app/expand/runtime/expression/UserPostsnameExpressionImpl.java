package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class UserPostsnameExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public UserPostsnameExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 获取岗位名称
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getPostsname();
	}
}
