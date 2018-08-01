package com.ibpmsoft.project.zqb.expression;

import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.context.UserContext;

public class FormMeetJCExpressionImpl  extends ExpressionAbst {
	private UserContext _me;
	private Long _activityInstanceId ;

	public FormMeetJCExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
		this._activityInstanceId = model.getInstanceid();
	}

	public String expressionParse(String expression) {
		
		int num = this.getMeetingNum();
		if(num==0){
			num=1;
		}
		return num+"";
	}
	
	private int getMeetingNum(){
		//获取当前部门编号及名称
				String deptno = _me.get_deptModel().getDepartmentno();
				String deptname = _me.get_deptModel().getDepartmentname();
				
				StringBuffer sql = new StringBuffer();
				sql.append("select jc from bd_meet_plan where customerno = '").append(deptno).append("' order by year desc, jc desc");
		return DBUtil.getInt(sql.toString(),"JC");
	}
} 
