package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class CostCenterIdExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public CostCenterIdExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 获取成本中心编号
	 */
	public String expressionParse(String expression){
		return _me.get_userModel().getCostcenterid();
	}
}
