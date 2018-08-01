package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;


public class UserExtend9InforExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public UserExtend9InforExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();

	}
	/**
	 * 获取扩展字段9
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getExtend9();
	}
}
