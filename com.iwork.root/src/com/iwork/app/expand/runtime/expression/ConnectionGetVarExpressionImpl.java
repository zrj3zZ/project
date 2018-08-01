package com.iwork.app.expand.runtime.expression;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iwork.connection.ConnectionAPI;
import com.iwork.connection.model.SysConnBaseinfo;
import com.iwork.connection.service.ConnectionRuntimeService;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.sdk.EngineAPI;

public class ConnectionGetVarExpressionImpl extends ExpressionAbst {

	private UserContext _me;
	private static ConnectionRuntimeService connectionRuntimeService;

  
	/**
	 * 获得数据集成参数
	 * @param model
	 * @param expressionValue
	 */

	public ConnectionGetVarExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
	}
 
	public String expressionParse(String expression) {
		String value = ""; 
		//获取UUID
		String uuid = ""; 
		//获取显示字段
		String fieldName = "";
		Pattern ptn = Pattern.compile("\'(.*?)\'");
        Matcher m = ptn.matcher(expression);
        if(m.find()) {
        	uuid =  m.group(1);
        }
        if(m.find()) {
        	fieldName =  m.group(1);
        }
        if(!uuid.equals("")&&!fieldName.equals("")){
        	if(connectionRuntimeService==null){
    			connectionRuntimeService = (ConnectionRuntimeService)SpringBeanUtil.getBean("connectionRuntimeService");		
    		}
        	//获取基础模型
        	SysConnBaseinfo model = connectionRuntimeService.getConnectionDesignService().getConnectionDesignDAO().getModel(uuid);
        	if(model!=null){
        		HashMap hash = new EngineAPI().getFromData(this.getInstanceid(),this.getFormId(), this.getParamModel().getEngineType());
        		List<HashMap> resultList = ConnectionAPI.getInstance().getList(model, hash);
        		if(resultList!=null){
        			for(HashMap item:resultList){
        				Object obj = item.get(fieldName);
            			if(obj!=null){
            				value = obj.toString();
            				break;
            			} 
        			}
        			
        		}
        	}
        }
		return value;
	}
}
