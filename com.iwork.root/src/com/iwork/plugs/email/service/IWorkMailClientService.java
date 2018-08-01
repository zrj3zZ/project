	package com.iwork.plugs.email.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONArray;

import com.iwork.commons.FileType;
import com.iwork.commons.util.UtilDate;
import com.iwork.commons.util.UtilString;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgDepartmentService;
import com.iwork.core.organization.service.OrgUserService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.HtmlRegexpUtil;
import com.iwork.eaglesearch.constant.EaglesSearchConstant;
import com.iwork.eaglesearch.factory.EaglesSearchFactory;
import com.iwork.plugs.email.constant.BoxTypeConst;
import com.iwork.plugs.email.constant.MailStatusConst;
import com.iwork.plugs.email.constant.MailTaskStatusConst;
import com.iwork.plugs.email.dao.IWorkMailDAO;
import com.iwork.plugs.email.dao.IWorkMailOwnerDAO;
import com.iwork.plugs.email.dao.IWorkMailTaskDAO;
import com.iwork.plugs.email.fullsearch.EaglesSearchEmailImpl;
import com.iwork.plugs.email.fullsearch.SearchIndexUtil;
import com.iwork.plugs.email.model.DataModel;
import com.iwork.plugs.email.model.EmailIndexModel;
import com.iwork.plugs.email.model.MailModel;
import com.iwork.plugs.email.model.MailOwnerModel;
import com.iwork.plugs.email.model.MailTaskModel;
import com.iwork.plugs.email.model.SearchModel;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.MessageAPI;

public class IWorkMailClientService {

	private IWorkMailDAO iWorkMailDAO;
	public IWorkMailTaskDAO iWorkMailTaskDAO;
	private IWorkMailOwnerDAO iWorkMailOwnerDAO;
	private OrgUserService orgUserService;
	private OrgDepartmentService orgDepartmentService;

	/**
	 * 全文检索
	 * @param sm
	 * @return
	 */
	public DataModel fullsearch(SearchModel sm,int startRow,int pageSize){
		DataModel dm = new DataModel();
		StringBuffer html = new StringBuffer();
		EaglesSearchEmailImpl esfdi =  (EaglesSearchEmailImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_EMAIL_INDEX);
		List<EmailIndexModel> list = esfdi.searcher(sm);
		if(list!=null){
			int count = 0;
			int num = pageSize;
			for(EmailIndexModel model:list){
				count++;
				if(count<=startRow){
					continue;
				}
				if(num==0){
					break;
				}
				if(model.getId()==null)continue;
				MailModel mm = iWorkMailDAO.getMailModelById(model.getMailid());
				if(mm!=null){
					String star ="";
	   			 StringBuffer icon = new StringBuffer();
	   			 String isread = "";
	   			 String content = "";
	   			 String sender = "";
	   			 icon.append("<input type=\"button\" class=\"read_icon\" disabled/>");
	   			 if(mm.get_attachments()==null||mm.get_attachments().equals("")){
	   				 
	   			}else{
	   				icon.append("<input type=\"button\" class=\"attach\" disabled/>");
	   			}
	   			 if(mm!=null){
	   				 content = mm.get_content();
	   				 if(content!=null){
	   					 String tmp = HtmlRegexpUtil.filterHtml(content.replace("\t","").replace("\n","").replace("\r",""));
	   					 if(tmp.length()>30){
	   						 content = tmp.substring(0,30);
	   					 }else{
	   						 content = tmp;
	   					 }
	   				 }
	   			 }
	   			 String fullpath = mm.get_mailFrom();
	   			String  userid = UserContextUtil.getInstance().getUserId(mm.get_mailFrom());
	   			if(userid!=null){
	   				UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
	   				if(uc!=null){
	   					sender = uc.get_userModel().getUsername();
	   					fullpath = uc.get_deptModel().getDepartmentname()+"/"+mm.get_mailFrom();
	   				}
	   			}else{
	   				sender = mm.get_mailFrom();
	   			}
					 html.append("<tr class=\"mailItem ").append(isread).append("\" id=\"item_").append(model.getId()).append("\"  name=\"receiveMail\">").append("\n");
					 html.append("	<td ><input type=\"checkbox\" name=\"ckb_selectAll\"; value=\"").append(model.getId()).append("\"/></td>").append("\n");
					 html.append("	<td  class=\"mailicon\" >").append(icon).append("</td>").append("\n");
					 html.append("	<td  class=\"mailUser\"  onclick=\"showDetailInfo('").append(mm.get_id()).append("','").append(model.getId()).append("','").append(model.getTitle()).append("','')\" ><span title=\"").append(fullpath).append("\">").append(sender).append("</span></td>").append("\n");
					 html.append("	<td class=\"mailTitle\"  onclick=\"showDetailInfo('").append(mm.get_id()).append("','").append(model.getId()).append("','").append(model.getTitle()).append("','')\" >").append(model.getTitle()).append("-<span>").append(content).append("</span></td>").append("\n");
					 html.append("	<td class=\"mailTitle\" >").append("\n"); 
					 html.append(UtilDate.datetimeFormat24(mm.get_createDate()));
					 html.append("	</td>").append("\n");
					 html.append("	<td class=\"mailicon\">").append("\n");
					 html.append(star).append("\n");
					 html.append("	</td>").append("\n");
					 html.append("</tr>").append("\n");
					 // count++;
					 num--;
				}
				
			}
		}
		dm.setHtml(html.toString());
		dm.setTotalNum(list==null?0:list.size());
		return dm;
	}
	/**
	 * 查询列表
	 * @param sm
	 * @return
	 */
	public String getSearchList(SearchModel sm){
		List list = new ArrayList();
		 StringBuffer html = new StringBuffer();
		//判断查询结构
		if(sm.getFolderid()!=null&&sm.getFolderid().equals("1")){
			List<MailTaskModel> taskModelList = iWorkMailTaskDAO.doSearchList(sm);
	    	 if(taskModelList!=null&&taskModelList.size()>0){
	    		 for(MailTaskModel model:taskModelList){
	    			 String star ="";
	    			 StringBuffer icon = new StringBuffer();
	    			 String isread = "";
	    			 String content = "";
	    			 String sender = "";
	    			 if(model.getIsStar()!=null&&model.getIsStar().equals(new Long(1))){
	    				 star = " <img id=\"bflag\" onclick=\"javascript:clickSetStar('"+model.getId()+"');\" src=\"iwork_img/star_empty.png\"/>";
	    			 }else{
	    				 star = " <img id=\"bflag\" onclick=\"javascript:clickSetStar('"+model.getId()+"');\" src=\"iwork_img/star_full.png\"/>";
	    			 }
	    			 if(model.getIsRead()!=null&&model.getIsRead().equals(new Long(0))){
	    				 isread ="unread"; 
	    				 icon.append("<input type=\"button\" class=\"unread_icon\" disabled/>");
	    				//判断是否有附件
	    			 }else{
	    				 icon.append("<input type=\"button\" class=\"read_icon\" disabled/>");
	    			 }
	    			 if(model.getAttachment()==null||model.getAttachment().equals("")){
	    				 
	    			}else{
	    				icon.append("<input type=\"button\" class=\"attach\" disabled/>");
	    			}
	    			 MailModel mm =  iWorkMailDAO.getMailModelById(model.getBindId());
	    			 if(mm!=null){
	    				 content = mm.get_content();
	    				 if(content!=null){
	    					 String tmp = HtmlRegexpUtil.filterHtml(content.replace("\t","").replace("\n","").replace("\r",""));
	    					 if(tmp.length()>30){
	    						 content = tmp.substring(0,30);
	    					 }else{
	    						 content = tmp;
	    					 }
	    				 }
	    			 }
	    			 String fullpath = model.getMailFrom();
	    			String  userid = UserContextUtil.getInstance().getUserId(model.getMailFrom());
	    			if(userid!=null){
	    				UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
	    				if(uc!=null){
	    					sender = uc.get_userModel().getUsername();
	    					fullpath = uc.get_deptModel().getDepartmentname()+"/"+model.getMailFrom();
	    				}
	    			}else{
	    				sender = model.getMailFrom();
	    			}
					 html.append("<tr class=\"mailItem ").append(isread).append("\" id=\"item_").append(model.getId()).append("\"  name=\"receiveMail\">").append("\n");
					 html.append("	<td ><input type=\"checkbox\" name=\"ckb_selectAll\"; value=\"").append(model.getId()).append("\"/></td>").append("\n");
					 html.append("	<td  class=\"mailicon\" >").append(icon).append("</td>").append("\n");
					 html.append("	<td  class=\"mailUser\"  onclick=\"showDetailInfo('").append(model.getBindId()).append("','").append(model.getId()).append("','").append(model.getTitle()).append("','").append(model.getMailBox()).append("')\" ><span title=\"").append(fullpath).append("\">").append(sender).append("</span></td>").append("\n");
					 html.append("	<td class=\"mailTitle\"  onclick=\"showDetailInfo('").append(model.getBindId()).append("','").append(model.getId()).append("','").append(model.getTitle()).append("','").append(model.getMailBox()).append("')\" >").append(model.getTitle()).append("-<span>").append(content).append("</span></td>").append("\n");
					 html.append("	<td class=\"mailTitle\" >").append("\n"); 
					 html.append(UtilDate.datetimeFormat(model.getCreateTime(),"yyyy-MM-dd hh:mm"));
					 html.append("	</td>").append("\n");
					 html.append("	<td class=\"mailicon\">").append("\n");
					 html.append(star).append("\n");
					 html.append("	</td>").append("\n");
					 html.append("</tr>").append("\n");
	    		 }
	    	 }else{
	    		 html.append("<tr><td colspan=\"6\">未发现您的内部邮件!").append("</td></tr>");
	    	 }
		}else{
			 List<MailOwnerModel> ownerModelList = iWorkMailOwnerDAO.doSearchList(sm);
			 if(ownerModelList!=null&&ownerModelList.size()>0){
	    		 for(MailOwnerModel model:ownerModelList){
	    			 String star ="";
	    			 StringBuffer icon = new StringBuffer();
	    			 String isread = "";
	    			 String content = "";
	    			 String sender = "";
	    			 if(model.getIsStar()!=null&&model.getIsStar().equals(new Long(1))){
	    				 star = " <img id=\"bflag\" onclick=\"javascript:clickSetStar('"+model.getId()+"');\" src=\"iwork_img/star_empty.png\"/>";
	    			 }else{
	    				 star = " <img id=\"bflag\" onclick=\"javascript:clickSetStar('"+model.getId()+"');\" src=\"iwork_img/star_full.png\"/>";
	    			 }
	    			 
	    			 icon.append("<input type=\"button\" class=\"read_icon\" disabled/>");
	    			 MailModel mm =  iWorkMailDAO.getMailModelById(model.getBindId());
	    			 if(mm!=null){
	    				 content = mm.get_content();
	    				 if(content!=null){
	    					 String tmp = HtmlRegexpUtil.filterHtml(content.replace("\t","").replace("\n","").replace("\r",""));
	    					 if(tmp.length()>30){
	    						 content = tmp.substring(0,30);
	    					 }else{
	    						 content = tmp;
	    					 }
	    				 }
	    			 }
	    			 String fullpath = model.getOwner();
	    			String  userid = UserContextUtil.getInstance().getUserId(model.getOwner());
	    			if(userid!=null){
	    				UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
	    				if(uc!=null){
	    					sender = uc.get_userModel().getUsername();
	    					fullpath = uc.get_deptModel().getDepartmentname()+"/"+model.getOwner();
	    				}
	    			}else{
	    				sender = model.getMailTo();
	    			}
					 html.append("<tr class=\"mailItem ").append(isread).append("\" id=\"item_").append(model.getId()).append("\"  name=\"receiveMail\">").append("\n");
					 html.append("	<td ><input type=\"checkbox\" name=\"ckb_selectAll\"; value=\"").append(model.getId()).append("\"/></td>").append("\n");
					 html.append("	<td  class=\"mailicon\" >").append(icon).append("</td>").append("\n");
					 html.append("	<td  class=\"mailUser\"  onClick=\"javascript:showDetailInfo('").append(model.getBindId()).append("','").append(model.getId()).append("','").append(model.getTitle()).append("','").append(model.getMailBox()).append("');\" ><span title=\"").append(fullpath).append("\">").append(sender).append("</span></td>").append("\n");
					 html.append("	<td class=\"mailTitle\"  onClick=\"javascript:showDetailInfo('").append(model.getBindId()).append("','").append(model.getId()).append("','").append(model.getTitle()).append("','").append(model.getMailBox()).append("');\" >").append(model.getTitle()).append("-<span>").append(content).append("</span></td>").append("\n");
					 html.append("	<td class=\"mailTitle\" >").append("\n");
					 html.append(UtilDate.getDaysBeforeNow(model.getCreateTime()));
					 html.append("	</td>").append("\n");
					 html.append("	<td class=\"mailicon\">").append("\n");
					 html.append(star).append("\n");
					 html.append("	</td>").append("\n");
					 html.append("</tr>").append("\n");
	    		 }
	    	 }else{
	    		 html.append("<tr><td colspan=\"6\">未发现您的内部邮件!").append("</td></tr>");
	    	 }
		}
		 return html.toString();
	}
	
	/**
	 * 
	 * @param searchKey
	 * @return
	 */
	public String showAddressJson(String searchKey){
		String companyId = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getCompanyno();
		List<HashMap> list  = orgUserService.getOrgUserDAO().getCompanyActiveUserList(companyId,searchKey);
		StringBuffer jsonHtml = new StringBuffer();
		List jsonlist = new ArrayList();
		for(HashMap orguser:list){ 
			Map mapList = new HashMap();
			StringBuffer showInfo = new StringBuffer();
			StringBuffer useraddress = new StringBuffer();
			useraddress.append(orguser.get("userid")).append("[").append(orguser.get("username")).append("]");
			showInfo.append(orguser.get("username")).append("[").append(orguser.get("companyname")).append("/").append(orguser.get("departmentname")).append("]");
			mapList.put("name",showInfo.toString());
			mapList.put("to",useraddress.toString());
			mapList.put("username",orguser.get("username"));
			mapList.put("type","user");
			jsonlist.add(mapList);
		}
		List<OrgDepartment> deptlist = orgDepartmentService.getOrgDepartmentDAO().queryBoDatas("departmentname", searchKey);
		for(OrgDepartment model:deptlist){
			Map mapList = new HashMap();
			StringBuffer showInfo = new StringBuffer();
			showInfo.append(model.getDepartmentname()).append("[").append(model.getId()).append("]");
			mapList.put("name",showInfo.toString());
			mapList.put("to","dept:"+model.getId());
			mapList.put("username",model.getDepartmentname());
			mapList.put("type","dept");
			jsonlist.add(mapList);
		}
		JSONArray json = JSONArray.fromObject(jsonlist);
		jsonHtml.append(json);
		String result = jsonHtml.toString();
		return result;
	}

	/**
	 * 获取总行数
	 * @param boxType
	 * @param userId
	 * @return
	 */
	public int getMsgLogRow(String userId) {
		return iWorkMailTaskDAO.getInstanceOfBoxRows(userId);
	}
	
	
	/**
	 * 
	 * @param boxType
	 * @param userId
	 * @param pageSize
	 * @param currLogPage
	 * @return
	 */
//	public List<MailTaskModel> getReceiveList(String userId,int pageSize, int currLogPage){
//		int startRow = 0;
//		if(currLogPage>1){
//			startRow = pageSize*currLogPage;
//		}
//		return iWorkMailTaskDAO.getInstanceOfBoxRows(userId);
//	}
	
	
	/**
	 * 发送邮件
	 * @param model 
	 * @return
	 */
	public String sendEmail(MailModel model,String Ownerid){

		if(model!=null){
			HashMap innerAccount = new HashMap();
			UserContext sender = UserContextUtil.getInstance().getCurrentUserContext();
			if(model.get_createUser()==null||"".equals(model.get_createUser())){
				model._createUser = sender.get_userModel().getUserid();
				model._createDate = Calendar.getInstance().getTime();
				model._mailFrom = UserContextUtil.getInstance().getCurrentUserFullName();
			}
			String returnCode = ""; 
			//
			
			String mail_to = model.get_to();
			returnCode = getAddressList(innerAccount, model._to, "TO");
			if (!returnCode.equals(MailStatusConst.OK)) {
				return returnCode;
			}
			// 获取邮件抄送
			model._cc = model._cc.trim();
			returnCode = getAddressList(innerAccount, model._cc, "CC");
			if (!returnCode.equals(MailStatusConst.OK)) {
				return returnCode;
			}
			String to_="";
			List<String> to_list = new ArrayList();
			List<String> cc_list = new ArrayList();
			if(model.get_to()!=null){
				String[] to = model.get_to().split(",");
				for(int i=0;to.length>i;i++){
					String userid = UserContextUtil.getInstance().getUserId(to[i]);
					if(userid!=null){
						to_list.add(userid);
					}
				}
			}
			
			String cc_=" ";
			if(model.get_cc()!=null){
				String[] cc = model.get_cc().split(",");
				for(String ccuserid:cc){
					String userid = UserContextUtil.getInstance().getUserId(ccuserid);
					if(userid!=null){
						cc_list.add(userid);
					}
				}
			}
			String content = model.get_content();
			boolean flag = iWorkMailDAO.save(model); 
			if(!flag)return MailStatusConst.DB_CONNECT_ERROR;
			//获取密送
//			model._bcc = model._bcc.trim();
//			returnCode=getAddressList(innerAccount,model._bcc,"BCC");
//			if(!returnCode.equals(MailStatusConst.OK)){
//				return returnCode;
//			}
			model.set_content(content);
			//添加发件箱
			MailOwnerModel mom = new MailOwnerModel();
			mom.setTitle(model._title);
			mom.setBindId(model._id);
			mom.setMailTo(model._to);
			mom.setIsDel(new Long(0));
			mom.setIsArchives(new Long(1));
			mom.setIsImportant(model._isImportant);
			mom.setCreateTime(Calendar.getInstance().getTime());
			mom.setOwner(sender._userModel.getUserid());
			mom.setMailBox(BoxTypeConst.TYPE_SEND);
			//如果是从存稿箱点进来的发送就把存稿箱里面数据状态更改
			if(Ownerid!=null && !"".equals(Ownerid)){
				Long id = new Long(Ownerid);
				mom.setId(id);
				iWorkMailOwnerDAO.updateMailOwnerModel(mom);
				SearchIndexUtil.getInstance().addDocIndex(id, UserContextUtil.getInstance().getCurrentUserId(), BoxTypeConst.TYPE_SEND, model);
//				SearchIndexUtil.getInstance().addDocIndex(id, UserContextUtil.getInstance().getCurrentUserId(), BoxTypeConst.TYPE_SEND, model.get_mailFrom(), mail_to, model._cc, model.get_title(), content, model.get_attachments());
			}else{
				iWorkMailOwnerDAO.save(mom);
				//保存发件箱索引
				SearchIndexUtil.getInstance().addDocIndex(mom.getId(),UserContextUtil.getInstance().getCurrentUserId(), BoxTypeConst.TYPE_SEND, model);
			}
			//if(ownerId==null)return MailStatusConst.DB_CONNECT_ERROR;
			if(innerAccount!=null && innerAccount.size()>0){
				for (int i = 0; i < innerAccount.size(); i++) {
					String mailAccount=(String)innerAccount.get(new Integer(i));
					OrgUser ou = UserContextUtil.getInstance().getOrgUserInfo(mailAccount);
					if(ou==null){
						continue;
					}
					//收件箱
					MailTaskModel sendTaskModel = new MailTaskModel();
					sendTaskModel.owner = ou.getUserid();
					sendTaskModel.mailFrom = model._mailFrom;
					sendTaskModel.mailTo = model.get_to();
					sendTaskModel.bindId = model._id;
					if(model._attachments == null || model._attachments.equals("")){
						sendTaskModel.isArchives = new Long(1);
					} 
					sendTaskModel.isImportant = model._isImportant;
					sendTaskModel.createTime=Calendar.getInstance().getTime();
					sendTaskModel.mailBox = BoxTypeConst.TYPE_TRANSACT;
					sendTaskModel.title = model._title;
					sendTaskModel.setIsDel(new Long(0));
					sendTaskModel.setIsImportant(model._isImportant);
					sendTaskModel.setIsRead(new Long(0));
					Long s = iWorkMailTaskDAO.save(sendTaskModel);
					SearchIndexUtil.getInstance().addDocIndex(sendTaskModel.getId(),ou.getUserid(), BoxTypeConst.TYPE_SEND, model);
					if (s ==null) {
						return MailStatusConst.SYSTEM_ERROR;
					}
				}
			}
			return MailStatusConst.OK;
		}
		return MailStatusConst.OK;
	}
	/**
	 * 转发邮件
	 * @param model 
	 * @return
	 */
	public String resendEmail(MailTaskModel model){

		if(model!=null){
			HashMap innerAccount = new HashMap();
			UserContext sender = UserContextUtil.getInstance().getCurrentUserContext();
			if(model.getOwner()==null||"".equals(model.getOwner())){
				model.owner = sender.get_userModel().getUserid();
				model.createTime = Calendar.getInstance().getTime();
				model.mailFrom= UserContextUtil.getInstance().getCurrentUserFullName();
			}
			String returnCode = "";
			//
			Long flag = iWorkMailTaskDAO.save(model); 
			if(flag!=0)return MailStatusConst.DB_CONNECT_ERROR;
			
			//获取账户列表
			String mail_to = model.getMailTo();
			returnCode=getAddressList(innerAccount,model.mailTo,"TO");
			if(!returnCode.equals(MailStatusConst.OK)){
				// 删除已添加数据
				return returnCode;
			}
			//获取邮件抄送
//			model. = model._cc.trim();
//			returnCode=getAddressList(innerAccount,model._cc,"CC");
//			if(!returnCode.equals(MailStatusConst.OK)){
//				return returnCode;
//			}
			//获取密送
//			model._bcc = model._bcc.trim();
//			returnCode=getAddressList(innerAccount,model._bcc,"BCC");
//			if(!returnCode.equals(MailStatusConst.OK)){
//				return returnCode;
//			}
			//添加发件箱
//			MailOwnerModel mom = new MailOwnerModel();
//			mom.setTitle(model._title);
//			mom.setBindId(model._id);
//
//			mom.setMailTo(model._to);
//			mom.setIsDel(new Long(0));
//			mom.setIsImportant(model._isImportant);
//			mom.setCreateTime(Calendar.getInstance().getTime());
//			mom.setOwner(sender._userModel.getUserid());
//			mom.setMailBox(BoxTypeConst.TYPE_SEND);
//			Long ownerId = iWorkMailOwnerDAO.save(mom);
//			//if(ownerId==null)return MailStatusConst.DB_CONNECT_ERROR;
			if(innerAccount!=null && innerAccount.size()>0){
				for (int i = 0; i < innerAccount.size(); i++) {
					String mailAccount=(String)innerAccount.get(new Integer(i));
					OrgUser ou = UserContextUtil.getInstance().getOrgUserInfo(mailAccount);
					if(ou==null){
						continue;
					}
					
					//邮件信息表
					MailModel Model = new MailModel();
					Model.set_createUser(ou.getUserid());
					Model.set_mailFrom( model.mailFrom);
					Model.set_to(model.mailTo);
					Model.set_relatedId(model.id);
					Model.set_isImportant(model.isImportant);
					Model.set_createDate(Calendar.getInstance().getTime());
					Model.set_mailType(BoxTypeConst.TYPE_TRANSACT);
					Model.set_title(model.title);
					Model.set_isImportant(model.isImportant);
					boolean s = iWorkMailDAO.save(Model);
					if (s ==false) {
						return MailStatusConst.SYSTEM_ERROR;
					}
				}
			}
			return MailStatusConst.OK;
		}
		return "";
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * @param innerAccount
	 * @param wwwAccount
	 * @param list
	 * @param addressType
	 * @return
	 */
	public String getAddressList(HashMap innerAccount,String list,String addressType){
		list = list.trim();
		Vector tmpList=new UtilString(list).split(",");
		for(int i=0;i<tmpList.size();i++){
			String mailAccount=(String)tmpList.get(i);
			mailAccount=mailAccount.trim();
			if(mailAccount.length()==0)continue;
				if(UserContextUtil.getInstance().checkAddress(mailAccount)){
						innerAccount.put(new Integer(innerAccount.size()), mailAccount);
				}else{
					return MailStatusConst.ADDRESS_ERROR;
				}
		}
		return MailStatusConst.OK;
	}

	
	

	/**
	 * 获取收件箱内的信息List
	 * @author wenpengyu
	 * */
	public List<MailTaskModel> getReceiveBoxInfo(String username){
		
		return iWorkMailTaskDAO.findall(username);
		
	}

	

	/**
	 * 获取所有已发邮件
	 * @author 杨连峰
	 * @param model 
	 * @return
	 */
	public List<MailOwnerModel> getSendList(String userName){
		
		return iWorkMailOwnerDAO.getSendList(userName);
		
	}
	
	

	/**
	 * 获取收件箱内每条信息的详细内容
	 * @author wenpengyu
	 * */
	
	public List<MailModel> findReceiveEmailById(long id){
		 MailTaskModel model = new MailTaskModel();
		// model.setId(id);
		 model.setIsRe(MailTaskStatusConst.isRe);
		// iWorkMailDAO.update(model);
		 List list =  iWorkMailDAO.findById(id);	
		   
			return list;
		}

	/**
	 * 设置邮件为已读邮件
	 * @param taskid
	 * @return
	 */
	public boolean setMailRead(Long taskid){
		boolean flag = false;
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		MailTaskModel taskmodel = iWorkMailTaskDAO.getMailTaskModelById(userid,taskid);
		if(taskmodel!=null){
			if(taskmodel.getIsRead()!=null&&taskmodel.getIsRead().equals(new Long(1))){
				
				flag = true;
			}else{
				taskmodel.setIsRead(new Long(1));
				taskmodel.setReadTime(new Date());
				iWorkMailTaskDAO.updateMailTask(taskmodel);
				flag = true;
			}
		}
		return flag;
		
	}

	
	/**
	 * 更新邮件信息
	 * 
	 * */
	public void updateRecEmail(MailTaskModel model){
		
		iWorkMailTaskDAO.updateRecEmail(model);
		
		
	}
	/**
	 * 收件箱邮件删除
	 */	
	public boolean deleteReEmail(String ids){
		boolean flag = false;
		MailTaskModel model;
		if(ids!=null||"".equals(ids)){
			String str[] = ids.split(",");
			for(int i = 0;i<str.length;i++){
				if(str[i].equals("0")){
					
				}else{
			    Long id = Long.parseLong(str[i]);
				model = iWorkMailTaskDAO.getMailTaskModelById(id);
				model.setIsDel(new Long(1));
				iWorkMailTaskDAO.updateRecEmail(model);
				//删除索引
				SearchIndexUtil.getInstance().removeDocIndex(id,BoxTypeConst.TYPE_TRANSACT);
				flag = true;
				}
			}
		}
		return flag;
	}
	/**
	 * 收件箱邮件彻底删除
	 */	
	public boolean deleteAllReEmail(String ids){
		boolean flag = false;
		MailTaskModel model;
		if(ids!=null||"".equals(ids)){
			String str[] = ids.split(",");
			for(int i = 0;i<str.length;i++){
				if(str[i].equals("0")){
					
				}else{
			    Long id = Long.parseLong(str[i]);
				model = iWorkMailTaskDAO.getMailTaskModelById(id);
				model.setIsStar(new Long(0));
				iWorkMailTaskDAO.updateRecEmail(model);
				SearchIndexUtil.getInstance().removeDocIndex(id,BoxTypeConst.TYPE_TRANSACT);
				flag = true;
				}
			}
		}
		return flag;
	}
	/**
	 * 收件箱邮件标星
	 */	
	public boolean setRecStar(String ids){
		boolean flag = false;
		MailTaskModel model;
		if(ids!=null||"".equals(ids)){
			String str[] = ids.split(",");
			for(int i = 0;i<str.length;i++){
				if(str[i].equals("0")){
					
				}else{
			    Long id = Long.parseLong(str[i]);
				model = iWorkMailTaskDAO.getMailTaskModelById(id);
				model.setIsStar(new Long(1));
				iWorkMailTaskDAO.updateRecEmail(model);
				flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * 取消收件箱邮件标星
	 */	
	public boolean cancelSetRecStar(String ids){
		boolean flag = false;
		MailTaskModel model;
		if(ids!=null||"".equals(ids)){
			String str[] = ids.split(",");
			for(int i = 0;i<str.length;i++){
				if(str[i].equals("0")){
					
				}else{
			    Long id = Long.parseLong(str[i]);
				model = iWorkMailTaskDAO.getMailTaskModelById(id);
				model.setIsStar(new Long(0));
				iWorkMailTaskDAO.updateRecEmail(model);
				flag = true;
				}
			}
		}
		return flag;
	}
	/**
	 * 写信保存草稿箱
	 * @return 
	 * 
	 * */
	public String saveToUnsend(MailModel model){
		if(model!=null){
			HashMap innerAccount = new HashMap();
			UserContext sender = UserContextUtil.getInstance().getCurrentUserContext();
			if(model.get_createUser()==null||"".equals(model.get_createUser())){
				model._createUser = sender.get_userModel().getUserid();
				model._createDate = Calendar.getInstance().getTime();
				model._mailFrom = UserContextUtil.getInstance().getCurrentUserFullName();
			}
			String returnCode = ""; 
			//
			boolean flag = iWorkMailDAO.save(model); 
			if(!flag)return MailStatusConst.DB_CONNECT_ERROR;
			
			//获取账户列表
			String mail_to = model.get_to();
			returnCode=getAddressList(innerAccount,model._to,"TO");
			if(!returnCode.equals(MailStatusConst.OK)){
				return returnCode;
			}
			//获取邮件抄送
			model._cc = model._cc.trim();
			returnCode=getAddressList(innerAccount,model._cc,"CC");
			if(!returnCode.equals(MailStatusConst.OK)){
				return returnCode;
			}
			//获取密送
//			model._bcc = model._bcc.trim();
//			returnCode=getAddressList(innerAccount,model._bcc,"BCC");
//			if(!returnCode.equals(MailStatusConst.OK)){
//				return returnCode;
//			}
			//添加发送任务表
			MailOwnerModel mom = new MailOwnerModel();
			mom.setTitle(model._title);
			mom.setBindId(model._id);
			//mom.setMailFrom(model._mailFrom);
			mom.setMailTo(model._to);
			mom.setIsDel(new Long(0));
			mom.setIsArchives(new Long(0));
			mom.setIsImportant(model._isImportant);
			mom.setCreateTime(Calendar.getInstance().getTime());
			mom.setOwner(sender._userModel.getUserid());
			mom.setMailBox(BoxTypeConst.TYPE_SEND);
			
			Long ownerId = iWorkMailOwnerDAO.save(mom);
			if(ownerId==null)return MailStatusConst.DB_CONNECT_ERROR;
			if(innerAccount!=null && innerAccount.size()>0){
				for (int i = 0; i < innerAccount.size(); i++) {
					String mailAccount=(String)innerAccount.get(new Integer(i));
					OrgUser ou = UserContextUtil.getInstance().getOrgUserInfo(mailAccount);
					if(ou==null){
						continue;
					}
		}
	}
		}
		return  MailStatusConst.OK;
	}
	/**
	 * 获得附件标题
	 * @param model
	 * @return
	 */
	public String buildAttachTipHtml(MailModel model){
		StringBuffer html = new StringBuffer();
		if(model!=null&&model.get_attachments()!=null){
			html.append("<Div>");
			String attchstr = model.get_attachments();
			String[] attchlist = attchstr.split(",");
			FileUpload fu = null;
			int count = 0;
			for(String uuid:attchlist){
				if(uuid.trim().equals("")){
					continue;
				}
				if(fu==null){
					 fu = FileUploadAPI.getInstance().getFileUpload(uuid);
				}
				 count++;
			}
			if(fu!=null){
				String icon = "iwork_img/attach.png";
				if(fu.getFileSrcName()!=null){
					icon = FileType.getFileIcon(fu.getFileSrcName());
				}
				html.append("<span class=\"tipfileNum\">").append(count).append("</span> 个 <span class=\"tipfileName\">( <a href=\"javascript:redirectDiv()\"> <img src=\"").append(icon).append("\"/>").append(fu.getFileSrcName()).append("</a>");
				if(count>1){
					html.append("..."); 
				} 
				html.append(" )</span>");
			}
			
			html.append("</div>");
		}
		return html.toString(); 
	}
	/**
	 * 获得邮件重要程度
	 * @param mailId
	 * @return
	 */
	public String getEmailImportantHtml(Long mailId){
		String isImportant = "";
		MailOwnerModel model = iWorkMailOwnerDAO.getOwnerModel(mailId);
		if(model!=null){
			if(model.getIsImportant()!=null){
   			 if(model.getIsImportant().equals(new Long(1))){
   				 isImportant = "[重要邮件]";
   			 }else if(model.getIsImportant().equals(new Long(2))){
   					 isImportant = "[非常重要]";
   			}
			 }
		}
		return isImportant;
	}
	
	
	public String buildAttachHtml(MailModel model){
		StringBuffer html = new StringBuffer();
		StringBuffer title = new StringBuffer();
		StringBuffer body = new StringBuffer();
		if(model!=null&&model.get_attachments()!=null){
			String attchstr = model.get_attachments();
			String[] attchlist = attchstr.split(",");
			FileUpload fu = null;
			int count = 0;
			for(String uuid:attchlist){
				if(uuid.trim().equals("")){
					continue;
				}
				fu = FileUploadAPI.getInstance().getFileUpload(uuid);
				if(fu!=null){
					String icon = "iwork_img/attach.png";
					if(fu.getFileSrcName()!=null){
						icon = FileType.getFileIcon(fu.getFileSrcName());
					}
					body.append("<div class=\"attachItem\"><img src=\"").append(icon).append("\"/>&nbsp;&nbsp;").append(fu.getFileSrcName()).append("&nbsp;&nbsp;&nbsp;&nbsp;<a target=\"_blank\" href=\"iwork_email_download.action?fileUUID=").append(fu.getFileId()).append("\">下载</a></div>\n");
					count++;
				}
			}
			title.append("<div class=\"attchTitle\">附件( ").append(count).append("个 )</div>\n");
			html.append(title);
			html.append(body);
		}
		return html.toString(); 
	}
	/**
	 * 获取当前登录用户收件箱条数
	 */
	public int getTotalRecNum(){
		int recNum = 0;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userId = uc.get_userModel().getUserid();
		recNum = iWorkMailTaskDAO.countReceiveEail(userId);
		return recNum;
	}
	
	/**
	 * 获取当前登录用户草稿箱条数
	 * @return
	 */
	public int getTotalDraftNum(){
		int draft = 0;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userId = uc.get_userModel().getUserid();
		draft = iWorkMailOwnerDAO.countDraftEail(userId);
		return draft;
	}
	/**
	 * 发送状态
	 * @param mailId
	 * @return
	 */
	public String showSendStatus(Long mailId){
		StringBuffer html = new StringBuffer();
		html.append("<table width=\"100%\" class=\"mailStatus\">").append("\n");
		html.append("<tr>").append("\n");
		html.append("<th>").append("标记").append("</th>").append("\n");
		html.append("<th>").append("收件人").append("</th>").append("\n");
		html.append("<th>").append("收件人部门").append("</th>").append("\n");
		html.append("<th>").append("状态").append("</th>").append("\n");
		html.append("<th>").append("阅读时间").append("</th>").append("\n");
		html.append("</tr>").append("\n");
		List<MailTaskModel> list = iWorkMailTaskDAO.getMailTaskList(mailId);
		for(MailTaskModel model:list){
			String useraddress = "";
			String deptName = "";
			UserContext uc = UserContextUtil.getInstance().getUserContext(model.getOwner());
			if(uc != null && !"".equals(uc)){
				OrgUser user = uc.get_userModel();
				if(user!=null && !"".equals(user)){
					useraddress = user.getUserid().trim()+'['+user.getUsername().trim()+']';
					deptName = user.getCompanyname()+'/'+user.getDepartmentname();
				}
			}
			String status = "";
			if(model.getIsRead()!=null&&model.getIsRead().equals(new Long(1))){
				status = "已读";
			}else{
				status = "未读";
			}
			if(model.getIsDel()!=null&&model.getIsDel().equals(new Long(1))){
				status = "已删除";
			}
			html.append("<tr>").append("\n");
			if(model.getIsRead()!=null&&model.getIsRead().equals(new Long(1))){
				html.append("<td>");
				html.append("  <img src=\"iwork_img/readed.gif\" />");
				html.append("</td>").append("\n");
			}else{
				html.append("<td>");
				html.append("  <img src=\"iwork_img/unread.gif\" />");
				html.append("</td>").append("\n");
			}
			html.append("<td>").append(useraddress).append("</td>").append("\n");
			html.append("<td>").append(deptName).append("</td>").append("\n");
			if(model.getIsRead()!=null&&model.getIsRead().equals(new Long(1))){
				html.append("<td>").append(status).append("</td>").append("\n");
				html.append("<td>").append(UtilDate.datetimeFormat24(model.getReadTime())).append("</td>").append("\n");
			}else{
				html.append("<td>").append(status).append("</td>").append("\n");
				html.append("<td>").append("</td>").append("\n");
			}
			
			html.append("</tr>").append("\n");
		}
		/**
		if(isUndo){
			html.append("<tr>").append("\n");
			html.append("<td><a href=\"javascript:mailUndo();\"><img src=\"iwork_img/arrow_undo.png\">邮件撤销</a></td>").append("\n");
			html.append("<td colspan='4'></td>").append("\n");
			html.append("</tr>").append("\n");
			html.append("</table>").append("\n");
		}*/
		return html.toString();
	}
	
	
	/**
	 * 发送状态
	 * @param mailId
	 * @return
	 */
	public String mailUndo(Long mailId){
		String msg = "success";
		boolean isPurView = false;
		boolean isUndo = true;
		 
		String username = UserContextUtil.getInstance().getCurrentUserFullName();
		MailModel mm = iWorkMailDAO.getMailModelById(mailId);
		if(mm!=null&&mm.get_createUser()!=null&&mm.get_createUser().equals(UserContextUtil.getInstance().getCurrentUserId())){
			msg = "nopurview";
			isPurView = true;
		}
		if(isPurView){
			List<MailTaskModel> list = iWorkMailTaskDAO.getMailTaskList(mailId);
			for(MailTaskModel model:list){
				if(model.getIsRead()!=null&&model.getIsRead().equals(new Long(1))){
					isUndo = isUndo && false;
					msg = "read";
				}else{
					String owner = model.getOwner();
					//执行删除操作
					iWorkMailTaskDAO.deleteEmail(model);
					//发送系统消息
					String title = "【邮件撤销提示】";
					StringBuffer content = new StringBuffer();
					content.append("").append(username).append("[").append(UtilDate.getNowDatetime()).append("]撤销一封发给您的邮件!");
					MessageAPI.getInstance().sendSysMsg(owner, title, content.toString());
				}
			}
			if(isUndo){
				//将当前邮件撤销只草稿箱
				MailOwnerModel model = iWorkMailOwnerDAO.getOwnerModel(mailId);
				if(model!=null){
					model.setIsArchives(new Long(0));
					iWorkMailOwnerDAO.updateMailOwnerModel(model);
				}
				msg = "success";
			}
		}
		return msg;
	}
	
	/**
	 * 获取邮件是否读取标识
	 * @return
	 */
	public Long getIsRead(Long mailId,String userid){
		return iWorkMailTaskDAO.getIsRead(mailId, userid);
	}
	
	
	public IWorkMailDAO getiWorkMailDAO() {
		return iWorkMailDAO;
	}
	public void setiWorkMailDAO(IWorkMailDAO iWorkMailDAO) {
		this.iWorkMailDAO = iWorkMailDAO;
	}
	public IWorkMailTaskDAO getiWorkMailTaskDAO() {
		return iWorkMailTaskDAO;
	}
	public void setiWorkMailTaskDAO(IWorkMailTaskDAO iWorkMailTaskDAO) {
		this.iWorkMailTaskDAO = iWorkMailTaskDAO;
	}
	public void setiWorkMailOwnerDAO(IWorkMailOwnerDAO iWorkMailOwnerDAO) {
		this.iWorkMailOwnerDAO = iWorkMailOwnerDAO;
	}

	public OrgUserService getOrgUserService() {
		return orgUserService;
	}

	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}

	public OrgDepartmentService getOrgDepartmentService() {
		return orgDepartmentService;
	}
	public void setOrgDepartmentService(OrgDepartmentService orgDepartmentService) {
		this.orgDepartmentService = orgDepartmentService;
	}
	
}
