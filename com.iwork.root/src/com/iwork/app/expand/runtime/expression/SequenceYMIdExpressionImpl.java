package com.iwork.app.expand.runtime.expression;

import java.sql.Timestamp;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.util.SequenceUtil;
import org.apache.log4j.Logger;
public class SequenceYMIdExpressionImpl extends ExpressionAbst{
	private static Logger logger = Logger.getLogger(SequenceYMIdExpressionImpl.class);

	private String key;
	public SequenceYMIdExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
		key = model.getEntityname()+model.getFormid();
	}

	public String expressionParse(String expression) {
		int sequenceNo = 0;
		try {
			sequenceNo = SequenceUtil.getInstance().getSequenceIndex("BPM:" + key);
		} catch (Exception e) {logger.error(e,e);
		}
		return UtilDate.yearFormat(new Timestamp(System.currentTimeMillis())) +"-"+ UtilDate.monthFormat(new Timestamp(System.currentTimeMillis())) +"-"+sequenceNo;
	}
}

