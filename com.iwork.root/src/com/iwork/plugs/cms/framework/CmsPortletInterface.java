package com.iwork.plugs.cms.framework;

import java.util.HashMap;

import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.cms.model.IworkCmsPortlet;


/**
 * 栏目接口
 * @author david
 *
 */
public interface CmsPortletInterface {


	/**
	 * 
	 * @param me
	 * @param domainModel
	 * @param infoModel
	 * @param params
	 * @return
	 */
	public abstract String portletPage(UserContext me,DomainModel domainModel,IworkCmsPortlet infoModel,HashMap params);
	/**
	 * 
	 * @param me
	 * @param domainModel
	 * @param infoModel
	 * @param params
	 * @return
	 */
	public abstract String portletWeiXinPage(UserContext me,DomainModel domainModel,IworkCmsPortlet infoModel,HashMap params);
}
