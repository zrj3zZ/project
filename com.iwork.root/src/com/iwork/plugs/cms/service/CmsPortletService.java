package com.iwork.plugs.cms.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.plugs.cms.dao.CmsInfoDAO;
import com.iwork.plugs.cms.dao.CmsPortletDAO;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.util.CmsUtil;
import com.iwork.core.security.SecurityUtil;

/**
 * CMS栏目管理业务实现类
 * @author WeiGuangjian
 *
 */
public class CmsPortletService {

	private CmsPortletDAO cmsPortletDAO;
	private CmsInfoDAO cmsInfoDAO;

	/**
	 * 获取导航树
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
			item.put("id", model.toString()+"_group");
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
	 * 获取有效栏目列表
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
				boolean flag = SecurityUtil.isSuperManager();
				boolean flag2= CmsUtil.isManager(model.getManager());
				if(!flag&&!flag2){	
				   continue;
				}
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", model.getId()+"_channel");
				item.put("text", model.getPortletname());
				item.put("iconCls", "icon-ok");
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("portletkey",model.getPortletkey());
				attributes.put("groupname",model.getGroupname());
				attributes.put("manager",model.getManager());
				attributes.put("template",model.getTemplate());
				attributes.put("morelink",model.getMorelink());
				attributes.put("linktarget",model.getLinktarget());
				attributes.put("verifytype",model.getVerifytype());
				attributes.put("browse",model.getBrowse());
				attributes.put("portlettype",model.getPortlettype());
				attributes.put("param",model.getParam());			
				attributes.put("begindate", model.getBegindate()==null?null:CmsUtil.dateFormat(model.getBegindate()));
				attributes.put("enddate",  model.getEnddate()==null?null:CmsUtil.dateFormat(model.getEnddate()));
				attributes.put("status", model.getStatus());
				attributes.put("memo",model.getMemo());	
				attributes.put("prows",model.getProws());
				attributes.put("pwidth",model.getPwidth());
				attributes.put("pheight",model.getPheight());
				attributes.put("isborder",model.getIsborder());	
				attributes.put("istitle",model.getIstitle());	
				attributes.put("type","portlet");
				item.put("attributes", attributes);
				items.add(item);
			}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 获取所有栏目列表
	 * @return
	 */
	public String getGridJson(String groupname){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();
		List<IworkCmsPortlet> list;
			if(groupname==null||groupname.equals("")){
				list=cmsPortletDAO.getAllList();
			}else{
				list=cmsPortletDAO.getGroupList(groupname);
			}		
			for(int i = 0;i<list.size();i++){
				IworkCmsPortlet model = list.get(i);
				boolean flag = SecurityUtil.isSuperManager();
				boolean flag2= CmsUtil.isManager(model.getManager());
				if(!flag&&!flag2){	
				   continue;
				}
			    Map<String,Object> rows = new HashMap<String,Object>();
				rows.put("id",model.getId());
				rows.put("portletkey",model.getPortletkey());
				rows.put("portletname", model.getPortletname());			
				rows.put("groupname",model.getGroupname());
				rows.put("manager",model.getManager());
				rows.put("template",model.getTemplate());
				rows.put("morelink",model.getMorelink());
				rows.put("linktarget",model.getLinktarget());
				rows.put("verifytype",model.getVerifytype());
				rows.put("browse",model.getBrowse());
				rows.put("portlettype",model.getPortlettype());
				rows.put("param",model.getParam());
				rows.put("begindate",model.getBegindate()==null?null:CmsUtil.dateFormat(model.getBegindate()));
				rows.put("enddate", model.getEnddate()==null?null:CmsUtil.dateFormat(model.getEnddate()));
				rows.put("status", model.getStatus());
				rows.put("memo",model.getMemo());
				rows.put("prows",model.getProws());
				rows.put("pwidth",model.getPwidth());
				rows.put("pheight",model.getPheight());
				rows.put("isborder",model.getIsborder()); 
				rows.put("istitle",model.getIstitle());
				if(model.getOrderindex()==null||model.getOrderindex()==0){
					rows.put("orderindex","");
				}else{
					rows.put("orderindex",model.getOrderindex());
				}								
				if(model.getPortlettype()==0){
				rows.put("operate","<a href='#' title='设置' onclick=\"Set("+model.getId()+");\"><img src=iwork_img/btn_rwfp.gif border='0'></a> <a href='#' title='内容管理' onclick=\"openTab('"+model.getPortletname()+"内容管理','cmsInfoAction!index.action?portletid="+model.getId()+"','');\"><img src=iwork_img/but_edit.gif border='0'></a>");
				}else{
                rows.put("operate","<a href='#' title='设置' onclick=\"Set("+model.getId()+");\"><img src=iwork_img/btn_rwfp.gif border='0'></a>"); 
				}
				item.add(rows);			    
			}	  		
		JSONArray json = JSONArray.fromObject(item);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	
	/**
	 * 获取分组下拉列表
	 * @return
	 */
	public String getComboboxJson(){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
		List groupList=cmsPortletDAO.getGroup();
		for(int i=0;i<groupList.size();i++){
			Object model = groupList.get(i);    
		    Map<String,Object> item = new HashMap<String,Object>();	 
		    item.put("text",model.toString());
			items.add(item);			    
		}	  		
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json.toString());
		return jsonHtml.toString();
	}
	
	/**
	 * 栏目增改
	 * @param portletid
	 * @param portletkey
	 * @param portletname
	 * @param groupname
	 * @param manager
	 * @param template
	 * @param verifytype
	 * @param browse
	 * @param portlettype
	 * @param param
	 * @param begindate
	 * @param enddate
	 * @param status
	 * @param memo
	 * @param isborder 
	 * @param pheight 
	 * @param pwidth 
	 * @param prows 
	 * @param type
	 * @throws ParseException
	 */
	public void cmsPortletEdit(String portletid, String portletkey,
			String portletname, String groupname, String manager,
			String template,String morelink, String linktarget, long verifytype, String browse, long portlettype,
			String param, String begindate, String enddate, long status,
			String memo,long prows, long pwidth, long pheight, long isborder,  long istitle,String type,long orderindex) throws ParseException {
		// TODO Auto-generated method stub
		
		if(type.equals("edit")){
			long id=Long.parseLong(portletid.split("_")[0]);
			IworkCmsPortlet model=cmsPortletDAO.getBoData(id);
			model.setPortletkey(portletkey);
			model.setPortletname(portletname);
			model.setGroupname(groupname);
			model.setManager(manager);
			model.setTemplate(template);
			model.setMorelink(morelink);
			model.setLinktarget(linktarget);
			model.setVerifytype(verifytype);
			model.setBrowse(browse);
			model.setPortlettype(portlettype);
			model.setParam(param);
			model.setBegindate(begindate.equals("")?null:CmsUtil.dateFormat(begindate));
			model.setEnddate(enddate.equals("")?null:CmsUtil.dateFormat(enddate));
			model.setStatus(status);
			model.setMemo(memo);
			model.setProws(prows);
			model.setPwidth(pwidth);
			model.setPheight(pheight);
			model.setIsborder(isborder);
			model.setIstitle(istitle);
			//if(orderindex!=0){
		    model.setOrderindex(orderindex);
			//}	
			cmsPortletDAO.updateBoData(model);
		}
		else if(type.equals("add")){	
			IworkCmsPortlet model =new IworkCmsPortlet();
			model.setPortletkey(portletkey);
			model.setPortletname(portletname);
			model.setGroupname(groupname);
			model.setManager(manager);
			model.setTemplate(template);
			model.setMorelink(morelink);
			model.setLinktarget(linktarget);
			model.setVerifytype(verifytype);
			model.setBrowse(browse);
			model.setPortlettype(portlettype);
			model.setParam(param);
			model.setBegindate(begindate.equals("")?null:CmsUtil.dateFormat(begindate));
			model.setEnddate(enddate.equals("")?null:CmsUtil.dateFormat(enddate));
			model.setStatus(status);
			model.setMemo(memo);
			model.setProws(prows);
			model.setPwidth(pwidth);
			model.setPheight(pheight);
			model.setIsborder(isborder);
			model.setIstitle(istitle);
			if(orderindex!=0){
				model.setOrderindex(orderindex);
			}	
			cmsPortletDAO.addBoData(model);
	    }
		
	}	
	
	/** 
	 * 栏目删除
	 * @param portletid
	 * @return
	 */
	public String cmsPortletRemove(String portletid){
		String msg="";
		long id=Long.parseLong(portletid.split("_")[0]);
		List list=cmsInfoDAO.getCmsList(Long.parseLong(portletid.split("_")[0]));
		if(list.size()==0){
			IworkCmsPortlet model=cmsPortletDAO.getBoData(id);
			cmsPortletDAO.deleteBoData(model);
		}else{
			msg="failure";
		}		
		return msg;
	}
	

	/**
	 * 键值校验
	 * @param key
	 * @param type
	 * @param portletid
	 * @return
	 */
	public String getKeyVal(String key,String type,String portletid){
		String msg="";
		if(type.equals("add")){
		List keyList=cmsPortletDAO.getKey();
		for(int i=0;i<keyList.size();i++){
			Object model = keyList.get(i);
			if(model.toString().equals(key)){
				msg="此键值已存在！";
			}
		}
		}else if(type.equals("edit")){
			long id=Long.parseLong(portletid.split("_")[0]);
			IworkCmsPortlet model=cmsPortletDAO.getBoData(id);
			List keyList=cmsPortletDAO.getKey();
			for(int i=0;i<keyList.size();i++){
				Object keymodel = keyList.get(i);
				if(keymodel.toString().equals(key)&&!key.equals(model.getPortletkey())){
					msg="此键值已存在！";
				}
			}
		}
		return msg;
	}
	
	public CmsPortletDAO getCmsPortletDAO() {
		return cmsPortletDAO;
	}

	public void setCmsPortletDAO(CmsPortletDAO cmsPortletDAO) {
		this.cmsPortletDAO = cmsPortletDAO;
	}

	public CmsInfoDAO getCmsInfoDAO() {
		return cmsInfoDAO;
	}

	public void setCmsInfoDAO(CmsInfoDAO cmsInfoDAO) {
		this.cmsInfoDAO = cmsInfoDAO;
	}

	

	


	
}
