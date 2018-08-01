package com.iwork.app.expand.runtime.expression;

import java.util.Date;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.util.SequenceUtil;
import org.apache.log4j.Logger;
public class MonthSequenceKeyExpressionImpl extends ExpressionAbst{
	private static Logger logger = Logger.getLogger(MonthSequenceKeyExpressionImpl.class);
	public MonthSequenceKeyExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
	}
	/**
	 * 每月份给出一个序列编号
	 */
	public String expressionParse(String expression){
		int sequenceNo = 0;
        try {
          String sequenceKey =UtilDate.getYear(new Date())+"-"+UtilDate.getMonth(new Date())+ expression.substring(expression.indexOf("(#") + 2, expression.indexOf(")"));
          sequenceNo = SequenceUtil.getInstance().getSequenceIndex("BPM:" + sequenceKey);
        } catch (Exception e) {
        	logger.error(e,e);
        }
        return Integer.toString(sequenceNo);
	}
}
