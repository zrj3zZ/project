package com.iwork.app.expand.runtime.expression;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.util.SequenceUtil;
import org.apache.log4j.Logger;
public class SequenceNoExpressionImpl extends ExpressionAbst {
	private static Logger logger = Logger.getLogger(SequenceNoExpressionImpl.class);
	private long instanceid;
	public SequenceNoExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
        instanceid=model.getInstanceid();
	}

	public String expressionParse(String expression) {
		int sequenceNo = 0;
		try {
			sequenceNo = SequenceUtil.getInstance().getSequenceIndex("BPM:" + instanceid);
		} catch (Exception e) {logger.error(e,e);
		}
		return Integer.toString(sequenceNo);
	}
}
