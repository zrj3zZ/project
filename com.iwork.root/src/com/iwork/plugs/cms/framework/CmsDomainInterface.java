package com.iwork.plugs.cms.framework;

import com.iwork.core.organization.context.UserContext;

/**
 * CMS栏目对象接口（例如人员、项目）
 * @author David.yang
 *
 */
public interface CmsDomainInterface {

	/**
	 * 获取一个domain的Index页面
	 * 
	 * @param me
	 * @param domainModel
	 * @return
	 * @author David.yang
	 */
	public abstract String portletPage(UserContext me,DomainModel domainModel);

	/**
	 * 获取栏目标题
	 * 
	 * @param me
	 * @param domainModel
	 * @param domainValue
	 * @return
	 * @author David.yang
	 */
	public abstract String getTitle(UserContext me,DomainModel domainModel,String domainValue);
	/**
	 * 获取“更多”连接
	 * 
	 * @param me
	 * @param domainModel
	 * @param domainValue
	 * @return
	 * @author David.yang
	 */
	public abstract String getMoreLink(UserContext me,DomainModel domainModel,String domainValue);
}
