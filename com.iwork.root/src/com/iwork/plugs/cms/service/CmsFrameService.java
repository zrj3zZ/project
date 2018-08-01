package com.iwork.plugs.cms.service;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.iwork.commons.ClassReflect;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.IWorkTemplateEngineUtil;
import com.iwork.plugs.cms.constans.CmsConstans;
import com.iwork.plugs.cms.dao.CmsChannelDAO;
import com.iwork.plugs.cms.dao.CmsInfoDAO;
import com.iwork.plugs.cms.dao.CmsPortletDAO;
import com.iwork.plugs.cms.dao.CmsRelationDAO;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.CmsPortletModel;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsChannel;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.model.IworkCmsRelation;
import com.iwork.plugs.cms.util.CmsUtil;

/**
 * CMS频道管理业务实现类
 * @author WeiGuangjian
 *
 */
public class CmsFrameService {	
	private static Logger logger = Logger.getLogger(CmsFrameService.class);
	private CmsChannelDAO cmsChannelDAO;
	private CmsRelationDAO cmsRelationDAO;
	private CmsInfoDAO cmsInfoDAO;
	private CmsPortletDAO cmsPortletDAO;

	
	public List<HashMap<String, Object>> getGzsc() {
		List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
				String userId = UserContextUtil.getInstance().getCurrentUserId();
				SimpleDateFormat sdf;
				StringBuffer sb = new StringBuffer();
				sb.append("SELECT INSTANCEID,FSRID,FSR,TZBT,TZNR,FSSJ,FKZT,CID,DID,USERID,NAME,PHONE,EMAIL,EINS FROM (SELECT C.INSTANCEID,C.FSRID,C.FSR,C.TZBT,C.TZNR,C.FSSJ,C.ID CID,D.ID DID,D.USERID,D.NAME,D.FKZT,D.PHONE,D.EMAIL,E.INSTANCEID EINS,ROWNUM RNUM FROM (SELECT INSTANCEID,B.ID,B.FSRID,B.FSR,B.TZBT,B.TZNR,B.FSSJ FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查')) A LEFT JOIN BD_XP_GZSC B ON A.DATAID = B.ID) C LEFT JOIN (SELECT A.INSTANCEID,B.ID,B.USERID,B.NAME,B.PHONE,B.EMAIL,B.FKZT FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查事项反馈人表表单')) A LEFT JOIN BD_XP_GZSCSXFKRB B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID AND D.ID IS NOT NULL AND C.ID IS NOT NULL LEFT JOIN (SELECT INSTANCEID,B.DID,B.HFR,ORG.USERNAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查回复')) A LEFT JOIN BD_XP_GZBCHF B ON A.DATAID = B.ID LEFT JOIN ORGUSER ORG ON B.HFR=ORG.USERID) E ON D.ID=E.DID WHERE USERID=?)  ORDER BY FSSJ DESC");
				Connection conn=null;
				ResultSet rs=null;
				PreparedStatement stmt=null;
				try {
					 conn=DBUtil.open();
					 stmt=conn.prepareStatement(sb.toString());
					 stmt.setString(1, userId);
					
					 rs=stmt.executeQuery();
				    while(rs.next()){
				    	sdf = new SimpleDateFormat("yyyy-MM-dd");
				    	BigDecimal instanceid=rs.getBigDecimal("INSTANCEID");
				    	HashMap<String,Object> h=new HashMap<String,Object>();
				    	h.put("INSTANCEID", instanceid);
				    	list.add(h);
				    }
				} catch (Exception e) {
					logger.error(e,e);
				}finally {
					DBUtil.close(conn, stmt, rs);
		    	}
		return list;
	}
	/**
	 * 获得第一个频道
	 * @return
	 */
	public Long getTopChannelId(){
		Long channelId = null;
		List<IworkCmsChannel> list = cmsChannelDAO.getGroupList("频道");
		if(list!=null&&list.size()>0){
			IworkCmsChannel model = list.get(0);
			if(model!=null){
				channelId = model.getId();
			}
		}
		return channelId;
	}

	/**
	 * 获得频道页面
	 * @return
	 */
	public String getChannelPage(Long channelid){
		Map root = new HashMap();
		IworkCmsChannel channelModel = cmsChannelDAO.getBoData(channelid);
		if(channelModel==null){
			return "<div style='text-align:center;color:red;'>此频道不存在!</div>";
		}
		if(!CmsUtil.getContentSecurityList(channelModel.getBrowse())){
    		return "<div style='text-align:center;color:red;'>您无权查看此频道!</div>";
    	}
		if(!CmsUtil.getEffect(channelModel.getBegindate(),channelModel.getEnddate())){
			return "<div style='text-align:center;color:red;'>此频道已失效!</div>";
		}
		if(channelModel.getStatus()==1){
			return "<div style='text-align:center;color:red;'>此频道已关闭!</div>";
		}
		//获取当前频道对应的栏目关系列表
		List<IworkCmsRelation> relationList = cmsRelationDAO.getCmsList(channelid);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		DomainModel domodel = new DomainModel();
		HashMap param=new HashMap();
		//循环关系列表
		for(IworkCmsRelation relationModel:relationList){
			CmsPortletModel infoModel = new CmsPortletModel();
			Long portletid = relationModel.getPortletid();
			IworkCmsPortlet cmsPortletModel = cmsPortletDAO.getBoData(portletid);
			//判断类型
			if(cmsPortletModel!=null){
//				if(CmsUtil.getContentSecurityList(cmsPortletModel.getBrowse())){
//					if(CmsUtil.getEffect(cmsPortletModel.getBegindate(),cmsPortletModel.getEnddate())){
//						if(cmsPortletModel.getStatus()==0){			
							Long a=cmsPortletModel.getPortlettype();
							if(cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE1)){//当栏目类型为资讯类栏目时
								infoModel.setInfoClass(CmsConstans.CMS_PORTLET_INPLAMENTS1);									
							}else if(cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE2)){//当栏目类型为链接类栏目时
								infoModel.setInfoClass(CmsConstans.CMS_PORTLET_INPLAMENTS2);				
							}else if(cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE3)){//当栏目类型为接口类栏目时							
								infoModel.setInfoClass(CmsConstans.CMS_PORTLET_INPLAMENTS3);			
							} 
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
							if(portlet!=null){
								//装载内容 
								String content = "";
								try{
									content = portlet.portletPage(uc, domodel, cmsPortletModel, param);
								}catch(Exception e){
									logger.error(e,e);
								}finally{ 
									cons = null;
									parameterTypes = null;
									portlet = null;
								}  
								root.put(cmsPortletModel.getPortletkey(), content);
								
							} 
	
			}
		}
		String content = "";
		try{
			content =  IWorkTemplateEngineUtil.getInstance().process(channelModel.getTemplate(),root,"");
		}catch(Exception e){logger.error(e,e);
			
		}
		return content;	
		
	}
	 
	public void setCmsInfoDAO(CmsInfoDAO cmsInfoDAO) {
		this.cmsInfoDAO = cmsInfoDAO;
	}

	public void setCmsPortletDAO(CmsPortletDAO cmsPortletDAO) {
		this.cmsPortletDAO = cmsPortletDAO;
	}

	public CmsChannelDAO getCmsChannelDAO() {
		return cmsChannelDAO;
	}

	public void setCmsChannelDAO(CmsChannelDAO cmsChannelDAO) {
		this.cmsChannelDAO = cmsChannelDAO;
	}

	public CmsRelationDAO getCmsRelationDAO() {
		return cmsRelationDAO;
	}

	public void setCmsRelationDAO(CmsRelationDAO cmsRelationDAO) {
		this.cmsRelationDAO = cmsRelationDAO;
	}
	
	
	
}
