package com.iwork.plugs.cms.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.plugs.cms.dao.CmsChannelDAO;
import com.iwork.plugs.cms.dao.CmsRelationDAO;
import com.iwork.plugs.cms.model.IworkCmsChannel;
import com.iwork.plugs.cms.util.CmsUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;

/**
 * CMS频道管理业务实现类
 * @author WeiGuangjian
 *
 */
public class CmsChannelService {

	private CmsChannelDAO cmsChannelDAO;
	private CmsRelationDAO cmsRelationDAO;

	/**
	 * 获取频道树
	 * @return
	 * @throws ParseException
	 */
	public String getTreeJson() throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();	
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", "频道_group");
			item.put("text", "频道");
			item.put("iconCls", "icon-ok");
			item.put("state", "open");
			item.put("children",this.getChannelJson("频道"));
			Map<String,Object> attributes = new HashMap<String,Object>();			
			attributes.put("url",null);
			attributes.put("target","_blank");
			attributes.put("type","group");
			item.put("attributes", attributes);	
			items.add(item);
			Map<String,Object> item2 = new HashMap<String,Object>();
			item2.put("id", "专题_group");
			item2.put("text", "专题");
			item2.put("iconCls", "icon-ok");
			item2.put("state", "open");
			item2.put("children",this.getChannelJson("专题"));
			Map<String,Object> attributes2 = new HashMap<String,Object>();			
			attributes2.put("url",null);
			attributes2.put("target","_blank");
			attributes2.put("type","group");
			item2.put("attributes", attributes2);	
			items.add(item2);
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 获取有效频道列表
	 * @param group
	 * @return
	 * @throws ParseException
	 */
	public String getChannelJson(String group) throws ParseException{
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		//获取当前日期
		Date Time = new Date();
	  	String now=CmsUtil.dateFormat(Time);
		Date nowTime=CmsUtil.dateFormat(now);
		List<IworkCmsChannel> list = cmsChannelDAO.getEffectList(nowTime,group);
			for(int i=0;i<list.size();i++){
				IworkCmsChannel model = list.get(i);
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				boolean flag = SecurityUtil.isSuperManager();
				boolean flag2= CmsUtil.isManager(model.getManager());
				if(!flag&&!flag2){	
				   continue;
				}
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", model.getId()+"_channel");
				item.put("text", model.getChannelname());
				item.put("iconCls", "icon-ok");
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("groupname",model.getGroupname());
				attributes.put("manager",model.getManager());
				attributes.put("template",model.getTemplate());
				attributes.put("verifytype",model.getVerifytype());
				attributes.put("browse",model.getBrowse());
				attributes.put("begindate", model.getBegindate()==null?null:CmsUtil.dateFormat(model.getBegindate()));
				attributes.put("enddate",  model.getEnddate()==null?null:CmsUtil.dateFormat(model.getEnddate()));
				attributes.put("status", model.getStatus());
				attributes.put("memo",model.getMemo());	
				attributes.put("type","channel");
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 获取频道表
	 * @return
	 */
	public String getGridJson(String groupname){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();	
		List<IworkCmsChannel> list;	
			if(groupname==null||groupname.equals("")){
				list=cmsChannelDAO.getAllList();
			}else{
				list=cmsChannelDAO.getGroupList(groupname);
			}	
			for(int i = 0;i<list.size();i++){
				IworkCmsChannel model = list.get(i);
				boolean flag = SecurityUtil.isSuperManager();
				boolean flag2= CmsUtil.isManager(model.getManager());
				if(!flag&&!flag2){	
				   continue;
				}
			    Map<String,Object> rows = new HashMap<String,Object>();
				rows.put("id",model.getId());
				rows.put("channelname", model.getChannelname());			
				rows.put("groupname",model.getGroupname());
				rows.put("manager",model.getManager());
				rows.put("template",model.getTemplate());
				rows.put("verifytype",model.getVerifytype());
				rows.put("browse",model.getBrowse());
				rows.put("begindate",model.getBegindate()==null?null:CmsUtil.dateFormat(model.getBegindate()));
				rows.put("enddate", model.getEnddate()==null?null:CmsUtil.dateFormat(model.getEnddate()));
				rows.put("status", model.getStatus());
				rows.put("memo",model.getMemo());
				rows.put("operate","<a href='#' title='设置' onclick=\"Set();\"><img src=iwork_img/btn_rwfp.gif border='0'></a> <a href='#' title='栏目管理' onclick=\"window.parent.addTab('"+model.getChannelname()+"栏目管理','cmsRelationAction!index.action?channelid="+model.getId()+"','');\"><img src=iwork_img/but_edit.gif border='0'></a>");
				item.add(rows);			    
			}	  		
		JSONArray json = JSONArray.fromObject(item);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	
	/**
	 * 频道增删改
	 * @param channelid
	 * @param channelname
	 * @param groupname
	 * @param manager
	 * @param template
	 * @param verifytype
	 * @param browse
	 * @param begindate
	 * @param enddate
	 * @param status
	 * @param memo
	 * @param type
	 * @throws ParseException
	 */
	public String cmsChannelEdit(String channelid,String channelname,String groupname,String manager,String template,long verifytype,String browse,String begindate,String enddate,long status,String memo,String type) throws ParseException{
		String msg="";
		if(type.equals("edit")){
			long id=Long.parseLong(channelid.split("_")[0]);
			IworkCmsChannel model=cmsChannelDAO.getBoData(id);
			model.setChannelname(channelname);
			model.setGroupname(groupname);
			model.setManager(manager);
			model.setTemplate(template);
			model.setVerifytype(verifytype);
			model.setBrowse(browse);
			model.setBegindate(begindate.equals("")?null:CmsUtil.dateFormat(begindate));
			model.setEnddate(enddate.equals("")?null:CmsUtil.dateFormat(enddate));
			model.setStatus(status);
			model.setMemo(memo);
			cmsChannelDAO.updateBoData(model);
		}
		else if(type.equals("add")){	
			IworkCmsChannel model =new IworkCmsChannel();
			model.setChannelname(channelname);
			model.setGroupname(groupname);
			model.setManager(manager);
			model.setTemplate(template);
			model.setVerifytype(verifytype);
			model.setBrowse(browse);
			model.setBegindate(begindate.equals("")?null:CmsUtil.dateFormat(begindate));
			model.setEnddate(enddate.equals("")?null:CmsUtil.dateFormat(enddate));
			model.setStatus(status);
			model.setMemo(memo);
			cmsChannelDAO.addBoData(model);
	    }
		else if(type.equals("remove")){
			long id=Long.parseLong(channelid.split("_")[0]);
			List list=cmsRelationDAO.getCmsList(Long.parseLong(channelid.split("_")[0]));
			if(list.size()==0){
			IworkCmsChannel model=cmsChannelDAO.getBoData(id);
			cmsChannelDAO.deleteBoData(model);
			}else{
				msg="failure";
			}
	    }		
		
		return msg;
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
