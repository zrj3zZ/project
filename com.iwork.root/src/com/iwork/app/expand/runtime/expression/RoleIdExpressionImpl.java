package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class RoleIdExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public RoleIdExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me=model.getContext();
	}
	/**
	 * 获取角色id
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getOrgroleid()+"";
	}
}
