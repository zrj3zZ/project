package com.iwork.plugs.cms.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.plugs.cms.dao.CmsInfoDAO;
import com.iwork.plugs.cms.dao.CmsPortletDAO;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.util.CmsUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.eaglesearch.constant.EaglesSearchConstant;
import com.iwork.eaglesearch.factory.EaglesSearchFactory;
import com.iwork.eaglesearch.impl.EaglesSearchNewsInfoImpl;
import org.apache.log4j.Logger;
/**
 * CMS内容管理业务实现类
 * @author WeiGuangjian
 *
 */
public class CmsInfoService {
	private static Logger logger = Logger.getLogger(CmsInfoService.class);
	private CmsInfoDAO cmsInfoDAO;
	private CmsPortletDAO cmsPortletDAO;
	//鹰眼检索索引管理
	private EaglesSearchNewsInfoImpl esfdi  = null;
	/**
	 * 获取内容列表JS
	 * @param portletid
	 * @return
	 */
	public String getGridScript(String portletid,String releaseman,String title,String releasedate,String enddate){
		String xmlUTF8;
		StringBuffer script = new StringBuffer(); 
		script.append("<script>").append("\n");
		script.append("var lastsel;").append("\n");
		script.append("jQuery(\"#info_grid\").jqGrid({").append("\n");
		script.append("   	url:'cmsInfoGrid.action?portletid=").append(portletid);//获得数据URL
		if(releaseman!=null&&!"".equals(releaseman)){
			
			try {
				xmlUTF8 = URLEncoder.encode(releaseman, "UTF-8");
				script.append("&releaseman=").append(xmlUTF8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			} 
			
    	}
    	if(title!=null&&!"".equals(title)){
    		try {
				xmlUTF8 = URLEncoder.encode(title, "UTF-8");
				script.append("&title=").append(xmlUTF8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			} 
    		
    	}
    	if(releasedate!=null&&!"".equals(releasedate)){
    		script.append("&releasedate=").append(releasedate);
    	}
    	if(enddate!=null&&!"".equals(enddate)){
    		script.append("&enddate=").append(enddate);
    	}
    	script.append("',").append("\n");
		script.append("		datatype: \"json\",").append("\n");
		script.append("		mtype: \"POST\",").append("\n");
		script.append("		autowidth:true,").append("\n");
		script.append("   	colNames:").append(this.getWorkBoxColumTitle()).append(",").append("\n");//获得列标题
		script.append("   	colModel:").append(this.getWorkBoxColumModel()).append(",").append("\n");
		script.append("   	rowNum:15,").append("\n");
		script.append("   	rowList:[15,30,45,60],").append("\n");
		script.append("   	multiselect: true,").append("\n");
		script.append("   	pager: '#prowed_info_grid',").append("\n");
		script.append("   	prmNames:{rows:\"page.pageSize\",page:\"page.curPageNo\",sort:\"page.orderBy\",order:\"page.order\"},").append("\n");
		script.append("     jsonReader: {").append("\n");
		script.append("     	root: \"dataRows\",").append("\n");
		script.append("     	page: \"curPage\",").append("\n");
		script.append("     	total: \"totalPages\",").append("\n");
		script.append("     	records: \"totalRecords\",").append("\n");
		script.append("     	repeatitems: false,").append("\n");
		script.append("     	id: \"INFOID\",").append("\n");
		script.append("     	userdata: \"userdata\"").append("\n");
		script.append("    },").append("\n");
		script.append("   	sortname: 'INFOID',").append("\n");
		script.append("    	shrinkToFit:false,").append("\n");
		script.append("    	viewrecords: true,").append("\n");
		script.append("     height: \"350\"").append("\n");
//	
		script.append("});").append("\n"); 
		script.append("jQuery(\"#info_grid\").jqGrid('navGrid',\"#prowed_info_grid").append("\",{edit:false,closeOnEscape:true,add:false,del:false});").append("\n");
		script.append("</script>").append("\n");
		return script.toString();
	}
	

	/**
	 * 列表标题
	 * @return
	 */
	private String getWorkBoxColumTitle(){
		StringBuffer html = new StringBuffer();
		List<String> item = new ArrayList<String>();
		item.add("序号");
		item.add("ID");
		item.add("发布时间");
		item.add("发布人");
		item.add("文章标题");
		item.add("更新时间");
		item.add("更新人");
		item.add("状态");
		item.add("是否允许讨论");
		item.add("操作");		
		JSONArray json = JSONArray.fromObject(item);
		html.append(json);
		return html.toString();
	}
	

	/**
	 * 列表模型
	 * @return
	 */
	private String getWorkBoxColumModel(){
		StringBuffer html = new StringBuffer();
		List<Map> item = new ArrayList<Map>();
		//装载行号
		Map rownum = new HashMap();
		rownum.put("name","ROWNUM");
		rownum.put("index","1");
		rownum.put("width","45");	
		rownum.put("align","center");
		item.add(rownum);
		
		//装载ID序列
		Map id = new HashMap();
		id.put("name","INFOID");
		id.put("index","2");
		id.put("width","50");	
		id.put("align","center");
		id.put("hidden", true);
		item.add(id);
		
		//装载发布时间
		Map releasedate = new HashMap();
		releasedate.put("name", "RELEASEDATE");
		releasedate.put("index","3"); 
		releasedate.put("width", "80");
		releasedate.put("align","center");
		item.add(releasedate);
		
		//装载发布人
		Map releaseman = new HashMap();
		releaseman.put("name", "RELEASEMAN");
		releaseman.put("index","4"); 
		releaseman.put("width", "100");
		releaseman.put("align","center");
		item.add(releaseman);
		
		//装载文章标题		
		Map title = new HashMap();
		title.put("name", "TITLE");
		title.put("index","5"); 
		title.put("width", "450");
		title.put("align","left");
		item.add(title);
		
		//装载更新时间	
		Map updatedate = new HashMap();
		updatedate.put("name", "UPDATEDATE");
		updatedate.put("index","6"); 
		updatedate.put("width", "80");
		updatedate.put("align","center");
		item.add(updatedate);
		
		//装载更新人		
		Map updateman = new HashMap();
		updateman.put("name", "UPDATEMAN");
		updateman.put("index","7"); 
		updateman.put("width", "100");
		updateman.put("align","center");
		item.add(updateman);
		
		//装载状态	
		Map status = new HashMap();
		status.put("name", "STATUS");
		status.put("index","8"); 
		status.put("width", "45");
		status.put("align","center");
		item.add(status);
		
		//装载讨论类型	
		Map istalk = new HashMap();
		istalk.put("name", "ISTALK");
		istalk.put("index","9"); 
		istalk.put("width", "100");
		istalk.put("align","center");
		item.add(istalk);
		
		//装载操作	
		Map operate = new HashMap();
		operate.put("name", "OPERATE");
		operate.put("index","10"); 
		operate.put("width", "40");
		operate.put("align","center");
		item.add(operate);
		
		rownum = new HashMap();
		JSONArray json = JSONArray.fromObject(item);
		html.append(json);
		return html.toString();
	}
	
	/**
	 * 获取内容列表Json
	 * @param channelid
	 * @param page
	 * @return
	 */
	 public String getDataBoxJson(String channelid,Page page,String releaseman,String title,String releasedate,String enddate){		
		 
	    	List<IworkCmsContent> maplist = cmsInfoDAO.getCmsList(Long.parseLong(channelid),releaseman,title,releasedate,enddate);
			Map<String,Object> total = new HashMap<String,Object>();
			List<IworkCmsContent> list = null;
			int totalRecord = 0;   //总记录行数
			int totalNum = 0;   //总记录页数
			int count = 0;
			try{
				//获取总行数
				totalRecord = maplist.size();
				BigDecimal   b1   =   new   BigDecimal(totalRecord); 
				BigDecimal   b2   =   new   BigDecimal(page.getPageSize()); 
				totalNum =  b1.divide(b2,0,BigDecimal.ROUND_UP).intValue();  //计算页数  向上取整
				int startRow =  page.getPageSize()*(page.getCurPageNo()-1);
				count = startRow;
				if(maplist!=null && maplist.size()!=0){
					list = cmsInfoDAO.getDataList(Long.parseLong(channelid), page.getPageSize(), startRow,releaseman,title,releasedate,enddate);
				}else{
					list = new ArrayList<IworkCmsContent>();
				}
			}catch(Exception e){
				logger.error(e,e);
			}
			List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
			String currentUserId = UserContextUtil.getInstance().getCurrentUserId();
			StringBuffer html = new StringBuffer();
			if(list!=null && list.size()>0){
			      for(int i=0;i<list.size();i++){
			    	IworkCmsContent model = list.get(i);	
			    	String userid = model.getReleaseman();
			    	OrgUser orguser = UserContextUtil.getInstance().getOrgUserInfo(userid);
			    	if(SecurityUtil.isSuperManager()||(orguser!=null&&orguser.getUserid().equals(currentUserId))){ 
			    		Map<String,Object> item = new HashMap<String,Object>();			
						item.put("INFOID",model.getId());
//						item.put("CHANNELID",model.getChannelid());
						item.put("TITLE","<a href='###' title='查看' onclick='openCms("+model.getId()+")' style=\"text-decoration:none;\"><img src=iwork_img/but_browse.gif border='0'>&nbsp;"+model.getTitle()+"</a>");
//						item.put("BRIEFTITLE",model.getBrieftitle());
						item.put("RELEASEDATE",model.getReleasedate()==null?null:CmsUtil.dateFormat(model.getReleasedate()));			
//						item.put("RELEASEDEPT",model.getReleasedept());
						item.put("RELEASEMAN",model.getReleaseman());
//						item.put("SOURCE",model.getSource());
//						item.put("PREPICTURE",model.getPrepicture());
//						item.put("PRECONTENT",model.getPrecontent());
//						item.put("CONTENT",model.getContent());
						String istalk="";
						if(model.getIstalk()==0){
							istalk="<a href='#' title='允许，点击更改' onclick='changeTalk("+model.getId()+")'><img src=iwork_img/green.ball.gif border='0'></a>";
						}else{
							istalk="<a href='#' title='不允许，点击更改' onclick='changeTalk("+model.getId()+")'><img src=iwork_img/red.ball.gif border='0'></a>";
						}
						item.put("ISTALK",istalk);
//						item.put("ISCOPY", model.getIscopy());
//						item.put("MARKRED", model.getMarkred());
//						item.put("MARKBOLD", model.getMarkbold());
//						item.put("MARKTOP", model.getMarktop());
//						item.put("KEYWORD", model.getKeyword());
						String status="";
						if(model.getStatus()!=null&&model.getStatus().equals(new Long(0))){
							status="<a href='#' title='开启状态，点击关闭' onclick='changeStatus("+model.getId()+")'><img src=iwork_img/green.ball.gif border='0'></a>";
						}else{
							status="<a href='#' title='关闭状态，点击开启' onclick='changeStatus("+model.getId()+")'><img src=iwork_img/red.ball.gif border='0'></a>";
						}
						item.put("STATUS",status);
//						item.put("ARCHIVES",model.getArchives());
						item.put("UPDATEDATE", model.getUpdatedate()==null?null:CmsUtil.dateFormat(model.getUpdatedate()));
//						item.put("UPDATEDEPT",model.getUpdatedept());
						item.put("UPDATEMAN",model.getUpdateman());
//						item.put("BROWSE",model.getBrowse());
						
						item.put("OPERATE", "<a href='#' title='编辑' onclick='edit("+model.getId()+")'><img src=iwork_img/but_edit.gif border='0'></a>");
						count++;
						//加载序号列
						item.put("ROWNUM",count);
						rows.add(item);
			    	}
				}
			}
			
			total.put("total", list.size());
			total.put("curPage", page.getCurPageNo());
			total.put("pageSize", page.getPageSize());
			total.put("totalPages",totalNum);
			total.put("totalRecords", totalRecord);
			total.put("dataRows", rows);
			JSONArray json = JSONArray.fromObject(total);
			html.append(json);
			return html.toString();
		}
	 
	 /**
	  * 获取内容列表Json
	  * @param channelid
	  * @param page
	  * @return
	  */
	 public String getCmsInfoListJson(String channelid,int pageSize,int pageno){		
		 StringBuffer jsonHtml = new StringBuffer();
		 int startRow = pageno*pageSize;
		 List<IworkCmsContent> list =  cmsInfoDAO.getContentList(Long.parseLong(channelid), pageSize,startRow);
		 List<HashMap> datalist = new ArrayList<HashMap>();
		 for(IworkCmsContent content:list){
			 HashMap hash = new HashMap();
			 hash.put("title", content.getTitle());
			 hash.put("id", content.getId());
			 hash.put("prepicture", content.getPrepicture());
			 if(content.getPrecontent()!=null){
				 String precontent = "";
				 if(content.getPrecontent().length()>60){
					 precontent = content.getPrecontent().substring(0,60)+"...";
				 }else{
					 precontent = content.getPrecontent();
				 } 
				 hash.put("precontent", precontent);
			 }
			 hash.put("source", content.getSource());
			 hash.put("releaseman", content.getReleaseman());
			 if(content.getUpdatedate()!=null){
				 hash.put("releasedate", UtilDate.getDaysBeforeNow(content.getUpdatedate()));
			 }else{
				 hash.put("releasedate", UtilDate.getDaysBeforeNow(content.getReleasedate())); 
			 }
			 datalist.add(hash);
		 } 
		 JSONArray json = JSONArray.fromObject(datalist);
		 jsonHtml.append(json);
		 return jsonHtml.toString();
	 }
	
	 /**
		 * 获取资讯内容列表
		 * @param channelid
		 * @return
		 */
		public String getGridJson(String channelid){
			StringBuffer jsonHtml = new StringBuffer();
			List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();	
			List<IworkCmsContent> list = cmsInfoDAO.getCmsList(Long.parseLong(channelid));
				for(int i = 0;i<list.size();i++){
					IworkCmsContent model = list.get(i);
				    Map<String,Object> rows = new HashMap<String,Object>();
					rows.put("id",model.getId());
					rows.put("channelid",model.getChannelid());
					rows.put("title", model.getTitle());			
					rows.put("brieftitle",model.getBrieftitle());
					rows.put("releasedate",model.getReleasedate()==null?null:CmsUtil.dateFormat(model.getReleasedate()));
					rows.put("releasedept",model.getReleasedept());
					rows.put("releaseman",model.getReleaseman());
					rows.put("source",model.getSource());
					rows.put("prepicture",model.getPrepicture());
					rows.put("precontent",model.getPrecontent());
					rows.put("content",model.getContent());
					rows.put("istalk",model.getIstalk());
					rows.put("iscopy", model.getIscopy());
					rows.put("markred", model.getMarkred()); 
					rows.put("markbold", model.getMarkbold());
					rows.put("marktop", model.getMarktop());
					rows.put("keyword", model.getKeyword());
					rows.put("status", model.getStatus());
					rows.put("archives",model.getArchives());
					rows.put("updatedate", model.getUpdatedate()==null?null:CmsUtil.dateFormat(model.getUpdatedate()));
					rows.put("updatedept",model.getUpdatedept());
					rows.put("updateman",model.getUpdateman());
					rows.put("browse",model.getBrowse());
					item.add(rows);			    
				}	  		
			JSONArray json = JSONArray.fromObject(item);
			jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
			return jsonHtml.toString();
		}
		
		/**
		 * 删除操作
		 * @param contentid
		 */
		public void cmsInfoRemove(String contentid){
			String[] infoid=contentid.split(",");
			for(int i=0;i<infoid.length;i++){		
				long id=Long.parseLong(infoid[i]);
				IworkCmsContent model=cmsInfoDAO.getBoData(id);
				cmsInfoDAO.deleteBoData(model);
				//删除索引
				if(esfdi==null){
					esfdi  =  (EaglesSearchNewsInfoImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_INFO_INDEX);
				}
				esfdi.deleteDocument(model);
			}
		}
		
		/**
		 * 更改讨论状态
		 * @param contentid
		 */
		public void changeTalk(String contentid){
			long id=Long.parseLong(contentid);
			IworkCmsContent model=cmsInfoDAO.getBoData(id);
			if(model.getIstalk()==0){
				model.setIstalk(1L);
			}else{
				model.setIstalk(0L);
			}
			cmsInfoDAO.updateBoData(model);
		}
		
		/**
		 * 更改状态
		 * @param contentid
		 */
		public void changeStatus(String contentid){
			long id=Long.parseLong(contentid);
			if(esfdi==null){
				esfdi  =  (EaglesSearchNewsInfoImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_INFO_INDEX);
			}
			IworkCmsContent model=cmsInfoDAO.getBoData(id);
			if(model.getStatus()==0){
				model.setStatus(1L);
				esfdi.deleteDocument(model);
			}else{
				model.setStatus(0L);
				esfdi.addDocument(model);
			}
			cmsInfoDAO.updateBoData(model);
		}
		
		/**
		 * 获取当前内容Model
		 * @param contentid
		 * @return
		 */
		public IworkCmsContent getModel(String contentid){
			IworkCmsContent model= null;
			try{
				long id=Long.parseLong(contentid);
				 model=cmsInfoDAO.getBoData(id);
				 if(model!=null){
					 String content = CmsUtil.getInstance().getCmsContent(id);
						model.setContent(content); 
				 }
				
			}catch(Exception e){logger.error(e,e);}
			return model;
		} 
		
		
		/**
		 * 获取资讯所属栏目Json
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
		 * 获取资讯所属有效栏目列表
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
			List<IworkCmsPortlet> list = cmsPortletDAO.getCmsList(nowTime,group);
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
		 * 获取所属栏目列表
		 * @return
		 */
		public String getCmsChannelJson(){
			StringBuffer jsonHtml = new StringBuffer();
			List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();	
			List<IworkCmsPortlet> channelList=cmsPortletDAO.getCmsChannel();
			for(int i=0;i<channelList.size();i++){
				IworkCmsPortlet model = channelList.get(i);    
			    Map<String,Object> item = new HashMap<String,Object>();
			    item.put("id",model.getId());
			    item.put("text",model.getPortletname());
				items.add(item);			    
			}	  		
			JSONArray json = JSONArray.fromObject(items);
			jsonHtml.append(json.toString());
			return jsonHtml.toString();
		}

		/**
		 * 新发布资讯模型初始化
		 * @param channelid
		 * @return
		 */
		public IworkCmsContent initContentModel(Long channelid){
			IworkCmsContent model = new IworkCmsContent();
			model.setIscopy(new Long(1));//允许拷贝
			model.setIstalk(new Long(1));//允许评论
			model.setBrieftitle("");
			model.setBrowse("");
			model.setChannelid(channelid);
			model.setMarkbold(new Long(0));
			model.setMarkred(new Long(0));
			model.setMarktop(new Long(0));
			model.setStatus(new Long(0));
			model.setArchives("");
			return model;
		}
		/**
		 * 发布操作
		
		 */
		public boolean cmsInfoAdd(IworkCmsContent model) {
			boolean flag = false;
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			if(model!=null){
				if(model.getId()!=null){
					CmsUtil.getInstance().saveCmsContent(model.getId(),model.getContent());
					model.setContent(""); 
					model.setUpdatedate(new Date());
					model.setReleasedept(uc.get_deptModel().getDepartmentname());
					model.setReleaseman(UserContextUtil.getInstance().getCurrentUserFullName());
					model.setUpdateman(UserContextUtil.getInstance().getCurrentUserFullName());
					cmsInfoDAO.updateBoData(model);
					if(esfdi==null){ 
						esfdi  =  (EaglesSearchNewsInfoImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_INFO_INDEX);
					}
					esfdi.updateDocument(model);
					flag = true;
				}else{
					//添加动态变量
					String content = model.getContent(); 
					model.setContent("");
					model.setReleasedate(new Date());
					model.setReleasedept(uc.get_deptModel().getDepartmentname());
					model.setReleaseman(UserContextUtil.getInstance().getCurrentUserFullName());
					cmsInfoDAO.addBoData(model);
					CmsUtil.getInstance().saveCmsContent(model.getId(),content);
					if(esfdi==null){ 
						esfdi  =  (EaglesSearchNewsInfoImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_INFO_INDEX);
					}
					esfdi.addDocument(model);
					flag = true; 
				}
			}
			
			return flag;
			
		}
		
		/**
		 * 获取最新发布内容ID
		 * @return
		 */
		public String getMaxId(){
			String maxid=cmsInfoDAO.getMaxID();
			return maxid;
		}
	 
	public CmsInfoDAO getCmsInfoDAO() {
		return cmsInfoDAO;
	}
	public void setCmsInfoDAO(CmsInfoDAO cmsInfoDAO) {
		this.cmsInfoDAO = cmsInfoDAO;
	}
	public CmsPortletDAO getCmsPortletDAO() {
		return cmsPortletDAO;
	}
	public void setCmsPortletDAO(CmsPortletDAO cmsPortletDAO) {
		this.cmsPortletDAO = cmsPortletDAO;
	}
	
	
	
	
}
