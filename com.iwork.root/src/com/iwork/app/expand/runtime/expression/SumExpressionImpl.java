package com.iwork.app.expand.runtime.expression;
import org.apache.log4j.Logger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataDAO;
import com.iwork.core.engine.metadata.model.SysEngineMetadata;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.util.SpringBeanUtil;
public class SumExpressionImpl extends ExpressionAbst {

	private static Logger logger = Logger.getLogger(SumExpressionImpl.class);
	private long _processInstanceId = 0;

	private long _formid = 0;

	public SumExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._processInstanceId = model.getInstanceid();
		this._formid = model.getFormid();
	}

	public String expressionParse(String expression) {
		SysEngineMetadataDAO metadataDAO= (SysEngineMetadataDAO)SpringBeanUtil.getBean("sysEngineMetadataDAO");
		String sumValue = "";
		String result = "";
		try { 
			expression = expression.replace("sum(", "");
			expression = expression.replace(")", "");
			expression = expression.replace("'", "");
			String[] params = expression.split(",");
			
			String tableName = "";
			String fieldName = "";
			if(params.length==2){
				tableName = params[0].toUpperCase();
				SysEngineMetadata metadata = metadataDAO.getModel(tableName);
				if(metadata!=null){
					fieldName = params[1].toUpperCase();
					StringBuffer sql = new StringBuffer();
					if(_processInstanceId == 0){
						sumValue = "0";
					}else{
						sql.append("select sum(").append(fieldName).append(") as sumValue from ").append(tableName).append(" a,SYS_ENGINE_FORM_BIND b where  a.id=b.dataid and b.instanceid="+_processInstanceId+" and b.metadataid=").append(metadata.getId());
						sumValue = DBUtil.getString(sql.toString(), "sumValue");
						if (sumValue == null || sumValue.trim().length() == 0)
							sumValue = "0";
					}
					
					DecimalFormat df=new DecimalFormat("#.00");
					df.setRoundingMode(RoundingMode.HALF_UP);
					result = (String)df.format(Double.parseDouble(sumValue));
					
				}else{
					return "#语法错误或错误的表名或字段名！";
				}
				
			}			
		} catch (Exception e) {
			logger.error(e,e);
			return "#语法错误或错误的表名或字段名！";
		}
		return result;
	}

}

