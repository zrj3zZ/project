package com.iwork.app.expand.runtime.expression;

import com.iwork.commons.util.UUIDUtil;
import com.iwork.core.engine.runtime.el.ExpressionAbst;import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;


public class UUIDExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public UUIDExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	public String expressionParse(String expression){
		
		return UUIDUtil.getUUID();
	}
}
