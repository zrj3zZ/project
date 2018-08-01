package com.iwork.portal.framework;

import java.util.HashMap;

import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.portal.model.IworkPortalOverall;


/**
 * 栏目接口
 * @author david
 *
 */
public interface PortletInterface {


	/**
	 * 
	 * @param me
	 * @param domainModel
	 * @param infoModel
	 * @param params
	 * @return
	 */
	public abstract String portletPage(UserContext me,IworkPortalOverall domainModel,IworkCmsPortlet infoModel,HashMap params);

}
