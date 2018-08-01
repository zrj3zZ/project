package com.iwork.app.expand.runtime.expression;


import org.apache.log4j.Logger;
import com.iwork.commons.util.UtilString;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class GetDatabaseValueExpressionImpl extends ExpressionAbst {

	private static Logger logger = Logger.getLogger(GetDatabaseValueExpressionImpl.class);
	private long _processInstanceId = 0;
	private UserContext _me;

	public GetDatabaseValueExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._processInstanceId = model.getInstanceid();
		this._me = model.getContext();
	}

	public String expressionParse(String expression) {
	

		// 该功能可以获得指定表中字段值

		java.sql.Connection conn = DBUtil.open();
		java.sql.PreparedStatement stmt = null;
		java.sql.ResultSet rset = null;
		
		try { 
			expression = expression.trim();			
			String sql=expression.substring(expression.indexOf("('") + 2, expression.length() - 2);			
			sql= new UtilString(sql).replace("_MACROS%C1%F5%BD%F0%D6%F9MACROS_".toLowerCase(), "+");
			stmt = conn.prepareStatement(sql);
			rset = stmt.executeQuery();
			if (rset.next()) {
				String value = rset.getString(1);
				if (value == null)
					value = "";
				return value;
			}
			return "";
		} catch (Exception e) {logger.error(e,e);
			return "";
		} finally {
			DBUtil.close(conn, stmt, rset);
    	}
	}
}
