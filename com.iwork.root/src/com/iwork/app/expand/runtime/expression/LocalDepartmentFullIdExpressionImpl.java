package com.iwork.app.expand.runtime.expression;

import java.util.Stack;

import com.iwork.core.engine.runtime.el.ExpressionAbst;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.organization.cache.DepartmentCache;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.util.SpringBeanUtil;

public class LocalDepartmentFullIdExpressionImpl extends ExpressionAbst {

	private UserContext _me;
	private StringBuffer deptpath = new StringBuffer();	
	private Stack stk=new Stack();

/**
 * 获取部门全路径id
 * @param model
 * @param expressionValue
 */
	public LocalDepartmentFullIdExpressionImpl(ExpressionParamsModel model, String expressionValue) {
		super(model, expressionValue);
		this._me = model.getContext();
	}

	public String expressionParse(String expression) {
		OrgDepartment deptModel = _me.get_deptModel();

		this.getDeptFullPath(deptModel);
		
		while(!stk.empty()){
			deptpath.append(stk.pop()).append("/");
			}
		String deptfullpath=deptpath.toString();//去掉末尾的“/”
		int size=deptfullpath.length();
		if(deptfullpath.substring(size-1, size).equals("/"))
			deptfullpath=deptfullpath.substring(0,size-1);
		return deptfullpath;
	}
	
	private void getDeptFullPath(OrgDepartment model){
		OrgDepartment parentModel = DepartmentCache.getInstance().getModel(model.getParentdepartmentid()+"");
		if(parentModel==null){
			OrgDepartmentDAO deptdao = (OrgDepartmentDAO)SpringBeanUtil.getBean("orgDepartmentDAO");
			parentModel  = deptdao.getBoData(model.getParentdepartmentid());
			if(parentModel!=null){
			DepartmentCache.getInstance().putModel(parentModel);
			}
		}
		
		if(parentModel!=null){
			stk.push(parentModel.getId());
			getDeptFullPath(parentModel);
		}
	}
}

