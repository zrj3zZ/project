package com.iwork.plugs.contract.schedule;

import java.util.HashMap;
import java.util.List;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.email.dao.IWorkMailDAO;
import com.iwork.plugs.email.dao.IWorkMailOwnerDAO;
import com.iwork.plugs.email.dao.IWorkMailTaskDAO;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;

public class EnforceContractSchedule  implements IWorkScheduleInterface {
	/**
	 * 合同执行计划提醒
	 */
	public static final String  CONTRACTOR_PLAN_UUID = "525e3a9555494ef5a052c0386a17802c";
	public IWorkMailTaskDAO iWorkMailTaskDAO;
	private IWorkMailOwnerDAO iWorkMailOwnerDAO;
	private IWorkMailDAO iWorkMailDAO;
	
	public boolean executeAfter() throws ScheduleException {
		System.out.println("合同执行计划提醒定时任务结束*****");
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		System.out.println("合同执行计划提醒定时任务开始*****");
		return true;
	}
	 /**
     *合同执行计划提醒流程发起 
     *
     * **/
	public boolean executeOn() throws ScheduleException {
		IWorkMailDAO dao = new IWorkMailDAO();
		dao = (IWorkMailDAO)SpringBeanUtil.getBean("iWorkMailDAO");
		List<HashMap> list = DemAPI.getInstance().getAllList(CONTRACTOR_PLAN_UUID, null, null);
		for(HashMap<String,Object> map : list){
			String userId = map.get("CREATEID")==null?"":map.get("CREATEID").toString();//创建人ID
			String userName = map.get("CREATENAME")==null?"":map.get("CREATENAME").toString();//创建人姓名
			String pactNo = map.get("PACTNO")==null?"":map.get("PACTNO").toString();//合同编号
			String fundsName = map.get("FUNDSNAME")==null?"":map.get("FUNDSNAME").toString();//款项名称
			String amount = map.get("AMOUNT")==null?"":map.get("AMOUNT").toString();//款项金额
			String renindDate = map.get("RENINDDATE")==null?"":map.get("RENINDDATE").toString();//提醒日期
			String instanceId = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
			UserContext user = UserContextUtil.getInstance().getUserContext(userId);
			String _to = UserContextUtil.getInstance().getFullUserAddress(userId);
			if(renindDate.equals(UtilDate.getNowdate())){
				MessageAPI.getInstance().sendSysMsg(userId, "【合同管理】合同计划提醒","合同【"+pactNo+"】中，款项金额为【"+amount+"】的【"+fundsName+"】，预计于【"+renindDate+"】执行，请联系财务人员，确认合同款是否到位！", "loadVisitPage.action?formid=100&instanceId="+instanceId+"&demId=26");
				MessageAPI.getInstance().sendSysMail("【合同管理】", user.get_userModel().getEmail(), "【合同管理】合同计划提醒", "合同【"+pactNo+"】中，款项金额为【"+amount+"】的【"+fundsName+"】，预计于【"+renindDate+"】执行，请联系财务人员，确认合同款是否到位！");
//				MailModel model = new MailModel();
//				model.set_to(_to);
//				model.set_content("合同【"+pactNo+"】中，款项金额为【"+amount+"】的【"+fundsName+"】，预计于【"+renindDate+"】执行，请联系财务人员，确认合同款是否到位！");
//				model.set_createUser("ADMIN");
//				model.set_createDate(Calendar.getInstance().getTime());
//				model.set_mailFrom(UserContextUtil.getInstance().getFullUserAddress("ADMIN"));
//				model.set_title("【合同管理】合同计划提醒");
//				
//				dao.save(model);
//				//添加发件箱
//				MailOwnerModel mom = new MailOwnerModel();
//				mom.setTitle(model._title);
//				mom.setBindId(model._id);
//				mom.setMailTo(model._to);
//				mom.setIsDel(new Long(0));
//				mom.setIsArchives(new Long(1));
//				mom.setIsImportant(model._isImportant);
//				mom.setCreateTime(Calendar.getInstance().getTime());
//				mom.setOwner(userId);
//				mom.setMailBox(BoxTypeConst.TYPE_SEND);
//				dao.save(mom);
//				//保存发件箱索引
//				SearchIndexUtil.getInstance().addDocIndex(mom.getId(),UserContextUtil.getInstance().getCurrentUserId(), BoxTypeConst.TYPE_SEND, model);
			}
		}
		return true;
	}
//	/**
//	 * 发送邮件
//	 * @param model 
//	 * @return
//	 */
//	public String sendEmail(MailModel model,String Ownerid){
//
//		if(model!=null){
//			HashMap innerAccount = new HashMap();
//			UserContext sender = UserContextUtil.getInstance().getCurrentUserContext();
//			if(model.get_createUser()==null||"".equals(model.get_createUser())){
//				model._createUser = sender.get_userModel().getUserid();
//				model._createDate = Calendar.getInstance().getTime();
//				model._mailFrom = UserContextUtil.getInstance().getCurrentUserFullName();
//			}
//			List<String> to_list = new ArrayList();
//			if(model.get_to()!=null){
//				String[] to = model.get_to().split(",");
//				for(int i=0;to.length>i;i++){
//					String userid = UserContextUtil.getInstance().getUserId(to[i]);
//					if(userid!=null){
//						to_list.add(userid);
//					}
//				}
//			}
//			String content = model.get_content();
//			boolean flag = iWorkMailDAO.save(model); 
//			if(!flag)return MailStatusConst.DB_CONNECT_ERROR;
//			model.set_content(content);
//			//添加发件箱
//			MailOwnerModel mom = new MailOwnerModel();
//			mom.setTitle(model._title);
//			mom.setBindId(model._id);
//			mom.setMailTo(model._to);
//			mom.setIsDel(new Long(0));
//			mom.setIsArchives(new Long(1));
//			mom.setIsImportant(model._isImportant);
//			mom.setCreateTime(Calendar.getInstance().getTime());
//			mom.setOwner(sender._userModel.getUserid());
//			mom.setMailBox(BoxTypeConst.TYPE_SEND);
//			//如果是从存稿箱点进来的发送就把存稿箱里面数据状态更改
//			if(Ownerid!=null && !"".equals(Ownerid)){
//				Long id = new Long(Ownerid);
//				mom.setId(id);
//				iWorkMailOwnerDAO.updateMailOwnerModel(mom);
//				SearchIndexUtil.getInstance().addDocIndex(id, UserContextUtil.getInstance().getCurrentUserId(), BoxTypeConst.TYPE_SEND, model);
//			}else{
//				iWorkMailOwnerDAO.save(mom);
//				//保存发件箱索引
//				SearchIndexUtil.getInstance().addDocIndex(mom.getId(),UserContextUtil.getInstance().getCurrentUserId(), BoxTypeConst.TYPE_SEND, model);
//			}
//					String mailAccount=(String)innerAccount.get(new Integer(i));
//					OrgUser ou = UserContextUtil.getInstance().getOrgUserInfo(mailAccount);
//					if(ou==null){
//						continue;
//					}
//					//收件箱
//					MailTaskModel sendTaskModel = new MailTaskModel();
//					sendTaskModel.owner = ou.getUserid();
//					sendTaskModel.mailFrom = model._mailFrom;
//					sendTaskModel.mailTo = model.get_to();
//					sendTaskModel.bindId = model._id;
//					if(model._attachments == null || model._attachments.equals("")){
//						sendTaskModel.isArchives = new Long(1);
//					} 
//					sendTaskModel.isImportant = model._isImportant;
//					sendTaskModel.createTime=Calendar.getInstance().getTime();
//					sendTaskModel.mailBox = BoxTypeConst.TYPE_TRANSACT;
//					sendTaskModel.title = model._title;
//					sendTaskModel.setIsDel(new Long(0));
//					sendTaskModel.setIsImportant(model._isImportant);
//					sendTaskModel.setIsRead(new Long(0));
//					Long s = iWorkMailTaskDAO.save(sendTaskModel);
//					SearchIndexUtil.getInstance().addDocIndex(sendTaskModel.getId(),ou.getUserid(), BoxTypeConst.TYPE_SEND, model);
//					if (s ==null) {
//						return MailStatusConst.SYSTEM_ERROR;
//					}
//			return MailStatusConst.OK;
//		}
//		return MailStatusConst.OK;
//	}

	public IWorkMailTaskDAO getiWorkMailTaskDAO() {
		return iWorkMailTaskDAO;
	}

	public void setiWorkMailTaskDAO(IWorkMailTaskDAO iWorkMailTaskDAO) {
		this.iWorkMailTaskDAO = iWorkMailTaskDAO;
	}

	public IWorkMailOwnerDAO getiWorkMailOwnerDAO() {
		return iWorkMailOwnerDAO;
	}

	public void setiWorkMailOwnerDAO(IWorkMailOwnerDAO iWorkMailOwnerDAO) {
		this.iWorkMailOwnerDAO = iWorkMailOwnerDAO;
	}

	public IWorkMailDAO getiWorkMailDAO() {
		return iWorkMailDAO;
	}

	public void setiWorkMailDAO(IWorkMailDAO iWorkMailDAO) {
		this.iWorkMailDAO = iWorkMailDAO;
	}

}
