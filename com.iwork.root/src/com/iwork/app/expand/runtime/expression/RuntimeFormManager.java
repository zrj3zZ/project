package com.iwork.app.expand.runtime.expression;

import com.iwork.commons.util.UtilString;
import com.iwork.core.engine.runtime.el.ExpressionFactory;
import com.iwork.core.engine.runtime.el.ExpressionInterface;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;

public class RuntimeFormManager {
	public String convertMacrosValue(ExpressionParamsModel model,String defaultValue)
	{
		String i;
		String sign = "_MACROS%C1%F5%BD%F0%D6%F9MACROS_";
		String formula = defaultValue;
		formula = (new UtilString(formula)).replace("\\+", sign);
		String formulas[] = new String[1];
		if (formula.indexOf("+") == -1)
		{
			formulas[0] = formula;
		} else
		{
			UtilString us = new UtilString(formula);
			formulas = formula.split("\\+");
		}
		StringBuffer value = new StringBuffer();
		String result;
		if (defaultValue.length() > 18 && defaultValue.toLowerCase().substring(0, 18).equals("${getdatabasevalue("))
		{
			result = getFormulaValue(model,defaultValue);
			result = (new UtilString(result)).replace(sign, "+");
			return result;
		}
		for (int j = 0; j < formulas.length; j++)
			value.append(formulas[j].indexOf("$") == -1 ? formulas[j] : getFormulaValue(model,formulas[j]));

		i = value.toString();
		i = (new UtilString(i)).replace(sign, "+");
		return i; 
	}
	private String getFormulaValue(ExpressionParamsModel model,String formula)
	{
		String oldFormula = formula;
		formula = formula.toLowerCase();
		ExpressionInterface expression = ExpressionFactory.getExpressionInstance(model, formula);
		String result = expression.expressionParse(formula);
		if (formula.equals(result))
			return oldFormula;
		else
			return result;
	}
}
