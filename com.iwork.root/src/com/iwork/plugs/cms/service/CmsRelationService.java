package com.iwork.plugs.cms.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.plugs.cms.dao.CmsChannelDAO;
import com.iwork.plugs.cms.dao.CmsPortletDAO;
import com.iwork.plugs.cms.dao.CmsRelationDAO;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.model.IworkCmsRelation;
import com.iwork.plugs.cms.util.CmsUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import org.apache.log4j.Logger;
/**
 * CMS频道与栏目关系业务实现类
 * @author WeiGuangjian
 *
 */
public class CmsRelationService {
	private static Logger logger = Logger.getLogger(CmsRelationService.class);

	private CmsRelationDAO cmsRelationDAO;
	private CmsPortletDAO cmsPortletDAO;
	private CmsChannelDAO cmsChannelDAO;
	
	/**
	 * 获取当前频道栏目列表
	 * @param channelid
	 * @return
	 */
	public String getGridJson(String channelid){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();
		Long cid=0L;
		try {
			cid=Long.parseLong(channelid);
		} catch (Exception e) {logger.error(e,e);
			return jsonHtml.toString();
		}
		List<IworkCmsRelation> list = cmsRelationDAO.getCmsList(cid);
			for(int i = 0;i<list.size();i++){
				IworkCmsRelation cmsModel = list.get(i);
				IworkCmsPortlet model=cmsPortletDAO.getBoData(cmsModel.getPortletid());
				if(model==null)continue;
			    Map<String,Object> rows = new HashMap<String,Object>();
			    rows.put("contentid", cmsModel.getId());
			    rows.put("channelid", cmsModel.getChannelid());
			    rows.put("id",model.getId());
				rows.put("portletkey",model.getPortletkey());
				rows.put("portletname", model.getPortletname());			
				rows.put("groupname",model.getGroupname());
				rows.put("manager",model.getManager());
				rows.put("template",model.getTemplate());
				rows.put("verifytype",model.getVerifytype());
				rows.put("browse",model.getBrowse());
				rows.put("portlettype",model.getPortlettype());
				rows.put("param",model.getParam());
				rows.put("begindate",model.getBegindate()==null?null:CmsUtil.dateFormat(model.getBegindate()));
				rows.put("enddate", model.getEnddate()==null?null:CmsUtil.dateFormat(model.getEnddate()));
				rows.put("status", model.getStatus());
				rows.put("memo",model.getMemo());
				if(model.getPortlettype()==0){
					boolean flag = SecurityUtil.isSuperManager();
					boolean flag2= CmsUtil.isManager(model.getManager());
					if(flag||flag2){	
				        rows.put("operate","<a href='#' title='内容管理' onclick=\"window.parent.addTab('"+model.getPortletname()+"内容管理','cmsInfoAction!index.action?portletid="+model.getId()+"','');\"><img src=iwork_img/but_edit.gif border='0'></a>");
					}
				}
				item.add(rows);			    
			}	  		
		JSONArray json = JSONArray.fromObject(item);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	/**
	 * 获取当前频道栏目列表
	 * @param channelid
	 * @return
	 */
	public String showRunListJson(String channelid){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();	
		List<IworkCmsRelation> list = cmsRelationDAO.getCmsList(Long.parseLong(channelid));
		for(int i = 0;i<list.size();i++){
			IworkCmsRelation cmsModel = list.get(i);
			IworkCmsPortlet model=cmsPortletDAO.getBoData(cmsModel.getPortletid());
			if(model==null)continue;
			Map<String,Object> rows = new HashMap<String,Object>();
			rows.put("contentid", cmsModel.getId());
			rows.put("channelid", cmsModel.getChannelid());
			rows.put("id",model.getId());
			rows.put("portletkey",model.getPortletkey());
			rows.put("portletname", model.getPortletname());			
			rows.put("groupname",model.getGroupname());
			rows.put("manager",model.getManager());
			rows.put("template",model.getTemplate());
			rows.put("verifytype",model.getVerifytype());
			rows.put("browse",model.getBrowse());
			rows.put("portlettype",model.getPortlettype());
			rows.put("param",model.getParam());
			rows.put("begindate",model.getBegindate()==null?null:CmsUtil.dateFormat(model.getBegindate()));
			rows.put("enddate", model.getEnddate()==null?null:CmsUtil.dateFormat(model.getEnddate()));
			rows.put("status", model.getStatus());
			rows.put("memo",model.getMemo());
			if(model.getPortlettype()==0){
				boolean flag = SecurityUtil.isSuperManager();
				boolean flag2= CmsUtil.isManager(model.getManager());
				if(flag||flag2){	
					rows.put("operate","<a href='#' title='内容管理' onclick=\"window.parent.addTab('"+model.getPortletname()+"内容管理','cmsInfoAction!index.action?portletid="+model.getId()+"','');\"><img src=iwork_img/but_edit.gif border='0'></a>");
				}
			}
			item.add(rows);			    
		}	  		
		JSONArray json = JSONArray.fromObject(item);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}

	/**
	 * 获取已选栏目列表
	 * @param channelid
	 * @return
	 */
	public String getPortletList(String channelid){		
		StringBuffer portletlist = new StringBuffer();
		//我的收藏夹
		portletlist.append("<select name='to' size='10' multiple style='width:150px;height:250px;' ondblclick='deleteSelect(document.all.to);'>");	
		UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
		List<IworkCmsRelation> list = cmsRelationDAO.getCmsList(Long.parseLong(channelid));
		for(int i=0;i<list.size();i++){
			IworkCmsRelation cmsModel = list.get(i);
			portletlist.append("<option value='"+cmsModel.getPortletid()+"_channel'>");
			IworkCmsPortlet model=cmsPortletDAO.getBoData(cmsModel.getPortletid());
			if(model!=null){
				portletlist.append(model.getPortletname()).append("</option>\n");
			}
		}
		portletlist.append("</select>");	
		return portletlist.toString();
	}
	
	/**
	 * 获取栏目树一级菜单
	 * @return
	 * @throws ParseException
	 */
	public String getTreeJson() throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();	
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		List groupList=cmsPortletDAO.getGroup();
		for(int i=0;i<groupList.size();i++){
			Object model = groupList.get(i);         			
		    Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", i+"_group");
			item.put("text", model.toString());
			item.put("iconCls", "icon-ok");
			item.put("state", "open");
			item.put("children",this.getPortletJson(model.toString()));
			Map<String,Object> attributes = new HashMap<String,Object>();			
			attributes.put("url",null);
			attributes.put("target","_blank");
			attributes.put("type","group");
			item.put("attributes", attributes);	
			items.add(item);	
		}			
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 获取栏目树二级菜单
	 * @param group
	 * @return
	 * @throws ParseException
	 */
	public String getPortletJson(String group) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		//获取当前日期
		Date Time = new Date();
	  	String now=CmsUtil.dateFormat(Time);
		Date nowTime=CmsUtil.dateFormat(now);
		List<IworkCmsPortlet> list = cmsPortletDAO.getEffectList(nowTime,group);
			for(int i=0;i<list.size();i++){
				IworkCmsPortlet model = list.get(i);
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", model.getId()+"_channel");
				item.put("text", model.getPortletname());
				item.put("iconCls", "icon-ok");
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("portletkey",model.getPortletkey());
				attributes.put("groupname",model.getGroupname());
				attributes.put("manager",model.getManager());
				attributes.put("template",model.getTemplate());
				attributes.put("verifytype",model.getVerifytype());
				attributes.put("browse",model.getBrowse());
				attributes.put("portlettype",model.getPortlettype());
				attributes.put("param",model.getParam());			
				attributes.put("begindate", model.getBegindate()==null?null:CmsUtil.dateFormat(model.getBegindate()));
				attributes.put("enddate",  model.getEnddate()==null?null:CmsUtil.dateFormat(model.getEnddate()));
				attributes.put("status", model.getStatus());
				attributes.put("memo",model.getMemo());	
				attributes.put("type","portlet");
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 添加操作
	 * @param channelid
	 * @param addstr
	 * @throws ParseException
	 */
	public void cmsAddPortlet(String channelid,String addstr) throws ParseException{

		//删除所有列表
		List<IworkCmsRelation> cmslist =cmsRelationDAO.getCmsList(Long.parseLong(channelid));
		for(int i = 0;i<cmslist.size();i++){
			IworkCmsRelation model = cmslist.get(i);
			cmsRelationDAO.deleteBoData(model);
		}
		//切割字符串	
		String temp[] = addstr.split(",");
		for(int i=0;i<temp.length;i++){
			String  portletid= temp[i].split("_")[0];
			//判断是否重复插入
			boolean c=false;
			List<IworkCmsRelation> list = cmsRelationDAO.getCmsList(Long.parseLong(channelid));
			for(int j = 0;j<list.size();j++){
				IworkCmsRelation model = list.get(j);
				if(model.getPortletid().equals(portletid)){
					c=true;
					break;
				}
			}
			if(c==true){
				continue;
			}
			//插入数据库
			IworkCmsRelation model =new IworkCmsRelation();
			model.setChannelid(Long.parseLong(channelid));
			model.setPortletid(Long.parseLong(portletid));		
			cmsRelationDAO.addBoData(model);	
		}
	}
	
	/**
	 * 删除操作
	 * @param contentid
	 * @throws ParseException
	 */
	public void cmsRemovePortlet(String contentid) throws ParseException{
		   long id=Long.parseLong(contentid);
		   IworkCmsRelation model=cmsRelationDAO.getBoData(id);	
		   cmsRelationDAO.deleteBoData(model);	
	}
	
	public CmsRelationDAO getCmsRelationDAO() {
		return cmsRelationDAO;
	}

	public void setCmsRelationDAO(CmsRelationDAO cmsRelationDAO) {
		this.cmsRelationDAO = cmsRelationDAO;
	}

	public CmsPortletDAO getCmsPortletDAO() {
		return cmsPortletDAO;
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
	
	
}
