package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class PortalModelExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public  PortalModelExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 获取门户模版
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getPortalmodel();
	}
}
