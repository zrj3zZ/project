package com.iwork.app.expand.runtime.expression;

import java.sql.Timestamp;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.util.SequenceUtil;
import org.apache.log4j.Logger;
public class SequenceYMNoExpressionImpl extends ExpressionAbst{
	private static Logger logger = Logger.getLogger(SequenceYMNoExpressionImpl.class);

	private String key;
	public SequenceYMNoExpressionImpl(ExpressionParamsModel model,String expressionValue){
		super(model,expressionValue);
        key = model.getEntityname()+model.getFormid();
	}

	public String expressionParse(String expression) {
		int sequenceNo = 0;
		String year = UtilDate.yearFormat(new Timestamp(System.currentTimeMillis()));
		String month = UtilDate.monthFormat(new Timestamp(System.currentTimeMillis()));
		key = year + month + key;
		try {
			sequenceNo = SequenceUtil.getInstance().getSequenceIndex("BPM:" + key);
		} catch (Exception e) {logger.error(e,e);
		}
		return year +"-"+ month + "-" + sequenceNo;
	}
}
