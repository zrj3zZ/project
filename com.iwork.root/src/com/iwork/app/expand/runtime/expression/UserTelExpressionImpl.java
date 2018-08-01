package com.iwork.app.expand.runtime.expression;


import com.iwork.core.engine.runtime.el.ExpressionAbst;import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;


public class UserTelExpressionImpl extends ExpressionAbst{

	private UserContext _me;
	public UserTelExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		this._me = model.getContext();
	}
	
	/**
	 * 获取办公室电话
	 */
	public String expressionParse(String expression){
		String tel = "";
		if(_me.get_userModel().getOfficetel()==null){
			tel = "空";
		}else{
			tel = _me.get_userModel().getOfficetel();
		}
		return tel;
	}
}

