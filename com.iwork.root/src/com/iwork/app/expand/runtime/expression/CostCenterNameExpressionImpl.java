package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class CostCenterNameExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public CostCenterNameExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 获取成本中心名称
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getCostcentername();
	}
}
