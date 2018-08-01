package com.iwork.app.expand.runtime.expression;

import java.util.HashMap;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.constant.ProcessTaskConstant;
public class ProcessFormNoExpressionImpl extends ExpressionAbst {
	private UserContext _me;
	private Long _activityInstanceId ;

	public ProcessFormNoExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
		this._activityInstanceId = model.getInstanceid();
	}

	public String expressionParse(String expression) {
		ExpressionParamsModel model = this.getParamModel();
		HashMap hash = model.getTaskParam();
		String formNo = "";
		if(hash!=null){
			formNo = (String)hash.get(ProcessTaskConstant.PROCESS_TASK_FORMNO);
		}
		return formNo;
	}
}
