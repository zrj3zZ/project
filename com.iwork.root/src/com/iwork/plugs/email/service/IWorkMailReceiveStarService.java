package com.iwork.plugs.email.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.commons.util.UtilCode;
import com.iwork.commons.util.UtilDate;
import com.iwork.plugs.email.constant.BoxTypeConst;
import com.iwork.plugs.email.dao.IWorkMailDAO;
import com.iwork.plugs.email.dao.IWorkMailDelDAO;
import com.iwork.plugs.email.dao.IWorkMailOwnerDAO;
import com.iwork.plugs.email.dao.IWorkMailTaskDAO;
import com.iwork.plugs.email.model.MailDelModel;
import com.iwork.plugs.email.model.MailOwnerModel;
import com.iwork.plugs.email.model.MailTaskModel;

public class IWorkMailReceiveStarService {
	
	private IWorkMailDAO iWorkMailDAO;
	private IWorkMailTaskDAO iWorkMailTaskDAO;
	private IWorkMailOwnerDAO iWorkMailOwnerDAO;
	private IWorkMailDelDAO iWorkMailDelDAO;
	/**
	 * 查询标星邮件
	 */
	public List<HashMap> getStaeMailOwnerModelList(String userName,int pageSize,int pageNumber){
		List<HashMap> list=new ArrayList<HashMap>();
		if(userName!=null){
			list=iWorkMailOwnerDAO.getAllStarList(userName,pageSize,pageNumber);
		}
		return list;
	}
	/**
	 * 单击是否标星
	 * @author wanglei
	 */
	public void clickStar(Long id,Long emailType,Long isstar){
		if(emailType.equals(BoxTypeConst.TYPE_SEND)){
			if(isstar.equals(BoxTypeConst.IS_STAR_YES)){
				MailOwnerModel mailOwnerModel=iWorkMailOwnerDAO.getSendListById(id);
				mailOwnerModel.setIsStar(BoxTypeConst.IS_STAR_NO);
				iWorkMailOwnerDAO.updateMailOwnerModel(mailOwnerModel);
			}
			if(isstar.equals(BoxTypeConst.IS_STAR_NO)){
				MailOwnerModel mailOwnerModel=iWorkMailOwnerDAO.getSendListById(id);
				mailOwnerModel.setIsStar(BoxTypeConst.IS_STAR_YES);
				iWorkMailOwnerDAO.updateMailOwnerModel(mailOwnerModel);
			}
		}
		if(emailType.equals(BoxTypeConst.TYPE_TRANSACT)){
			if(isstar.equals(BoxTypeConst.IS_STAR_YES)){
				MailTaskModel mailTaskModel=iWorkMailTaskDAO.findTaskById(id);
				mailTaskModel.setIsStar(BoxTypeConst.IS_STAR_NO);
				iWorkMailTaskDAO.updateMailTask(mailTaskModel);
			}
			if(isstar.equals(BoxTypeConst.IS_STAR_NO)){
				MailTaskModel mailTaskModel=iWorkMailTaskDAO.findTaskById(id);
				mailTaskModel.setIsStar(BoxTypeConst.IS_STAR_YES);
				iWorkMailTaskDAO.updateMailTask(mailTaskModel);
			}
		}
	}
	/**
	 * 获取标星邮件的数量
	 * @author wanglei
	 */
	public int getMailStarListSize(String userId){ 
		return iWorkMailOwnerDAO.getMailStarListSize(userId);
	}
	/**
	 * 根据ID查找对应的对象(MailOwnerModel)
	 * @author wanglei
	 */
	public MailOwnerModel findOwnerByIdStar(String letterIds){
		MailOwnerModel mailOwnerModel=null;
		if(letterIds!=null&&!letterIds.equals("")){
			Long id=Long.parseLong(letterIds);
			mailOwnerModel=iWorkMailOwnerDAO.getSendListById(id);
		}
		return mailOwnerModel;
	}
	/**
	 * 根据ID查找对应的对象(mailTaskModel)
	 * @author wanglei
	 */
	public MailTaskModel findTaskByIdStar(String letterIds){
		MailTaskModel mailTaskModel=null;
		if(letterIds!=null&&!letterIds.equals("")){
			Long id=Long.parseLong(letterIds);
			mailTaskModel=iWorkMailTaskDAO.findTaskById(id);
		}
		return mailTaskModel;
	}
	/**
	 * 标星
	 * @author wanglei
	 */
	public boolean restarStar(String letterIds,String zhuangtais,String userName){
		boolean fage=false;
		if(letterIds!=null&&zhuangtais!=null){
			String[] strArr=letterIds.split(",");
			String[] zhuangtai=zhuangtais.split(",");
			for(int i=0;i<strArr.length;i++){
				String userId=UtilCode.convert2UTF8(zhuangtai[i].toString());
				//判断是否为MailOwnerModel表中数据
				if(userId.equals(userName)){
					Long id=Long.parseLong(strArr[i]);
					MailOwnerModel mailOwnerModel=iWorkMailOwnerDAO.getSendListById(id);
					mailOwnerModel.setIsStar(BoxTypeConst.IS_STAR_YES);
					iWorkMailOwnerDAO.updateMailOwnerModel(mailOwnerModel);
				}
				//判断是否为MailOwnerModel表中数据
				if(!userId.equals(userName)&&!userId.equals("0")){
					Long id=Long.parseLong(strArr[i]);
					MailTaskModel mailTaskModel=iWorkMailTaskDAO.findTaskById(id);
					mailTaskModel.setIsStar(BoxTypeConst.IS_STAR_YES);
					iWorkMailTaskDAO.updateMailTask(mailTaskModel);
				}
				
			}
		}
		return fage;
	}
	/**
	 * 取消标星功能
	 * @author wanglei
	 */
	public boolean cancelStar(String letterIds,String zhuangtais,String userName){
		boolean fage=false;
		if(letterIds!=null&&zhuangtais!=null){
			String[] strArr=letterIds.split(",");
			String[] zhuangtai=zhuangtais.split(",");
			for(int i=0;i<strArr.length;i++){
				String userId=UtilCode.convert2UTF8(zhuangtai[i].toString());
				//判断是否为MailOwnerModel表中数据
				if(userId.equals(userName)){
					Long id=Long.parseLong(strArr[i]);
					MailOwnerModel mailOwnerModel=iWorkMailOwnerDAO.getSendListById(id);
					mailOwnerModel.setIsStar(BoxTypeConst.IS_STAR_NO);
					iWorkMailOwnerDAO.updateMailOwnerModel(mailOwnerModel);
				}
				//判断是否为MailOwnerModel表中数据
				if(!userId.equals(userName)&&!userId.equals("0")){
					Long id=Long.parseLong(strArr[i]);
					MailTaskModel mailTaskModel=iWorkMailTaskDAO.findTaskById(id);
					mailTaskModel.setIsStar(BoxTypeConst.IS_STAR_NO);
					iWorkMailTaskDAO.updateMailTask(mailTaskModel);
				}
				
			}
		}
		return fage;
	}
	/**
	 * 真的删除标星邮箱的信息
	 * @author wanglei
	 */
	public boolean reallyDelete(String letterIds,String zhuangtais,String userName){
		boolean fage=false;
		if(letterIds!=null&&zhuangtais!=null){
			String[] strArr=letterIds.split(",");
			String[] zhuangtai=zhuangtais.split(",");
			for(int i=0;i<strArr.length;i++){
				//判断是否为MailOwnerModel表中数据
				String userId=UtilCode.convert2UTF8(zhuangtai[i].toString());
				if(userId.equals(userName)){
					Long id=Long.parseLong(strArr[i]);
					MailOwnerModel mailOwnerModel=iWorkMailOwnerDAO.getSendListById(id);
					iWorkMailOwnerDAO.delEamil(mailOwnerModel);
				}
				//判断是否为MailOwnerModel表中数据
				if(!userId.equals(userName)&&!userId.equals("0")){
					Long id=Long.parseLong(strArr[i]);
					MailTaskModel mailTaskModel=iWorkMailTaskDAO.findTaskById(id);
					iWorkMailTaskDAO.deleteMailTask(mailTaskModel);
				}
			}
		}
		return fage;
	}
	/**
	 * 
	 * @author wanglei
	 */
	/**
	 * 删除标星邮箱的信息
	 * @author wanglei
	 */
	public void deleteStar(String letterIds,String zhuangtais,String userName){
		
		if(letterIds!=null&&zhuangtais!=null){
			String[] strArr=letterIds.split(",");
			String[] zhuangtai=zhuangtais.split(",");
			for(int i=0;i<strArr.length;i++){
				String userId=UtilCode.convert2UTF8(zhuangtai[i].toString());
				//判断是否为MailOwnerModel表中数据
				if(userId.equals(userName)){
					Long id=Long.parseLong(strArr[i]);
					MailOwnerModel mailOwnerModel=iWorkMailOwnerDAO.getSendListById(id);
					mailOwnerModel.setIsDel(BoxTypeConst.IS_DEL_YES);
					iWorkMailOwnerDAO.updateMailOwnerModel(mailOwnerModel);
					//执行插入删除记录表中操作
					Long blindid=mailOwnerModel.getBindId();
					//获取当前时间
					String currentDate=UtilDate.getNowDatetime();
					//获取当前人
					String owner=mailOwnerModel.getOwner();
					//获取邮件id
	          		Long emailId=mailOwnerModel.getId();
					//获取邮件的类型
					String emailType=String.valueOf(BoxTypeConst.TYPE_SEND);
					MailDelModel mailDelModel=new MailDelModel();
					mailDelModel.setBindId(blindid);
					mailDelModel.setType(emailType);
					mailDelModel.setCreateTime(UtilDate.StringToDate(currentDate, "yyyy-MM-dd HH:mm:ss"));
					mailDelModel.setOwner(owner);
					mailDelModel.setTaskId(emailId);
					iWorkMailDelDAO.addBoData(mailDelModel);
				}
				//判断是否为MailOwnerModel表中数据
				if(!userId.equals(userName)&&!userId.equals("0")){
					Long id=Long.parseLong(strArr[i]);
					MailTaskModel mailTaskModel=iWorkMailTaskDAO.findTaskById(id);
					mailTaskModel.setIsDel(BoxTypeConst.IS_DEL_YES);
					iWorkMailTaskDAO.updateMailTask(mailTaskModel);
					//执行插入删除记录表中操作
					Long blindid=mailTaskModel.getBindId();
					//获取当前时间
					String currentDate=UtilDate.getNowDatetime();
					//获取当前人
					String owner=mailTaskModel.getOwner();
					//获取邮件id
	          		Long emailId=mailTaskModel.getId();
					//获取邮件的类型
					String emailType=String.valueOf(BoxTypeConst.TYPE_TRANSACT);
					MailDelModel mailDelModel=new MailDelModel();
					mailDelModel.setBindId(blindid);
					mailDelModel.setType(emailType);
					mailDelModel.setCreateTime(UtilDate.StringToDate(currentDate, "yyyy-MM-dd HH:mm:ss"));
					mailDelModel.setOwner(owner);
					mailDelModel.setTaskId(emailId);
					iWorkMailDelDAO.addBoData(mailDelModel);
				}
				
			}
		}
		
		
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
	public IWorkMailOwnerDAO getiWorkMailOwnerDAO() {
		return iWorkMailOwnerDAO;
	}
	public void setiWorkMailOwnerDAO(IWorkMailOwnerDAO iWorkMailOwnerDAO) {
		this.iWorkMailOwnerDAO = iWorkMailOwnerDAO;
	}
	public IWorkMailDelDAO getiWorkMailDelDAO() {
		return iWorkMailDelDAO;
	}
	public void setiWorkMailDelDAO(IWorkMailDelDAO iWorkMailDelDAO) {
		this.iWorkMailDelDAO = iWorkMailDelDAO;
	}
	
	
}
