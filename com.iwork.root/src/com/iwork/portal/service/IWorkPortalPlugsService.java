package com.iwork.portal.service;

import java.lang.reflect.Constructor;
import com.iwork.commons.ClassReflect;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.cms.constans.CmsConstans;
import com.iwork.plugs.cms.dao.CmsPortletDAO;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.CmsPortletModel;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import org.apache.log4j.Logger;

public class IWorkPortalPlugsService {
	private static Logger logger = Logger.getLogger(IWorkPortalPlugsService.class);
	private CmsPortletDAO cmsPortletDAO;
	/**
	 * 获得首页新闻及资讯信息
	 * @return
	 */
	public String getCmsTopNews(Long portletid){
		String html = "";
		IworkCmsPortlet cmsPortletModel = cmsPortletDAO.getBoData(portletid);
		if(cmsPortletModel!=null){
			CmsPortletModel infoModel = new CmsPortletModel();
						Long a=cmsPortletModel.getPortlettype();
						if(cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE1)){//当栏目类型为资讯类栏目时
							infoModel.setInfoClass(CmsConstans.CMS_PORTLET_INPLAMENTS1);									
						}else if(cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE2)){//当栏目类型为链接类栏目时
							infoModel.setInfoClass(CmsConstans.CMS_PORTLET_INPLAMENTS2);				
						}else if(cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE3)){//当栏目类型为接口类栏目时							
							infoModel.setInfoClass(CmsConstans.CMS_PORTLET_INPLAMENTS3);			
						} 
						cmsPortletModel.setIsborder(new Long(1));
						cmsPortletModel.setIstitle(new Long(1));
						Constructor cons = null;
						Class[] parameterTypes = {}; 
						try { 
							cons = ClassReflect.getConstructor(infoModel.getInfoClass(), parameterTypes);
							if (cons != null) {
								Object[] params = {};
								infoModel.setKsPortalInfoObject((CmsPortletInterface) cons.newInstance(params));
							}	
						} catch (Exception e) {
							logger.error(e,e);
						} 
						CmsPortletInterface portlet=infoModel.getKsPortalInfoObject();
						//装载内容 
						html=  portlet.portletPage(UserContextUtil.getInstance().getCurrentUserContext(), null, cmsPortletModel, null); 
		} 
		return html;
	}
	
	public String myCalendarList(){
		
		return "";
	}
	
	
	public CmsPortletDAO getCmsPortletDAO() {
		return cmsPortletDAO;
	}
	public void setCmsPortletDAO(CmsPortletDAO cmsPortletDAO) {
		this.cmsPortletDAO = cmsPortletDAO;
	}
}
