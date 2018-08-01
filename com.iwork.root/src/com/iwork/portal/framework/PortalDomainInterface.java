package com.iwork.portal.framework;

import com.iwork.core.organization.context.UserContext;
import com.iwork.portal.model.IworkPortalOverall;

/**
 * CMS栏目对象接口（例如人员、项目）
 * @author David.yang
 *
 */
public interface PortalDomainInterface {

	/**
	 * 获取一个domain的Index页面
	 * 
	 * @param me
	 * @param domainModel
	 * @return
	 * @author David.yang
	 */
	public abstract String portletPage(UserContext me,IworkPortalOverall domainModel);

	/**
	 * 获取栏目标题
	 * 
	 * @param me
	 * @param domainModel
	 * @param domainValue
	 * @return
	 * @author David.yang
	 */
	public abstract String getTitle(UserContext me,IworkPortalOverall domainModel,String domainValue);
	/**
	 * 获取“更多”连接
	 * 
	 * @param me
	 * @param domainModel
	 * @param domainValue
	 * @return
	 * @author David.yang
	 */
	public abstract String getMoreLink(UserContext me,IworkPortalOverall domainModel,String domainValue);
}
