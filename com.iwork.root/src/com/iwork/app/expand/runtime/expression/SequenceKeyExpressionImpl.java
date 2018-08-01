package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.util.SequenceUtil;
import org.apache.log4j.Logger;
public class SequenceKeyExpressionImpl extends ExpressionAbst{
	private static Logger logger = Logger.getLogger(SequenceKeyExpressionImpl.class);
	private Long formid;
	
	public SequenceKeyExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		formid=model.getFormid();
	}
	
	public String expressionParse(String expression){
		int sequenceNo = 0;
        try {
          String sequenceKey = expression.substring(expression.indexOf("(#") + 3, expression.indexOf(")"));
          sequenceNo = SequenceUtil.getInstance().getSequenceIndex("iform:" + sequenceKey);
        } catch (Exception e) {logger.error(e,e);
        }
        return Integer.toString(sequenceNo);
	}
}
