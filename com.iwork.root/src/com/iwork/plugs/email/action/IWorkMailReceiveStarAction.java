package com.iwork.plugs.email.action;

import java.util.HashMap;
import java.util.List;

import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.email.model.MailOwnerModel;
import com.iwork.plugs.email.model.MailTaskModel;
import com.iwork.plugs.email.service.IWorkMailReceiveStarService;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkMailReceiveStarAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	private IWorkMailReceiveStarService iWorkMailReceiveStarService;
	private List<HashMap> listMap;
	private int pageNumber; // 当前页数
	private long total;// 总条数
	private int pageSize; //每页显示条数
	private MailTaskModel mailTaskModel;
	private MailOwnerModel mailOwnerModel;
	private String letterIds;
	private String zhuangtais;
	private Long id; //主表ID
	private Long emailType; //主表类型
	private Long isstar;//是否标星
	private Long isread;//是否已读
	/**
	 * 获取标星邮件

	 * @author wanglei
	 */
	public String staeList(){
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		// 查询总条数
		total = iWorkMailReceiveStarService.getMailStarListSize(userId);

		if(pageNumber == 0){
			pageNumber = 1;
		}
		if(pageSize == 0){
			pageSize = 10;
		}
		// 根据分页查询记录
		listMap=iWorkMailReceiveStarService.getStaeMailOwnerModelList(userId,pageSize,pageNumber);

		return SUCCESS;
	}
	/**
	 * 标星
	 * @author wanglei
	 */
	public void restarStar(){
		String msg = "false";
		String userName=UserContextUtil.getInstance().getCurrentUserId();
		if(letterIds!=null&&zhuangtais!=null){
			iWorkMailReceiveStarService.restarStar(letterIds, zhuangtais, userName);
			msg = "succ";
		}
		ResponseUtil.write(msg);
	}
	/**
	 * 取消标星
	 * @author wanglei
	 */
	public void cancelStar(){
		String msg = "false";
		String userName=UserContextUtil.getInstance().getCurrentUserId();
		if(letterIds!=null&&zhuangtais!=null){
			iWorkMailReceiveStarService.cancelStar(letterIds, zhuangtais, userName);
			msg = "succ";
		}
		ResponseUtil.write(msg);
	}
	/**
	 * 虚拟删除功能
	 * @author wanglei
	 */
	public void deleteStar(){
		String msg = "false";
		String userName=UserContextUtil.getInstance().getCurrentUserId();
		if(letterIds!=null&&zhuangtais!=null){
			iWorkMailReceiveStarService.deleteStar(letterIds, zhuangtais,userName);
			msg = "succ";
		}
		ResponseUtil.write(msg);
	}
	/**
	 * 真的删除
	 * @author wanglei
	 */
	public void reallyDelete(){
		String msg = "false";
		String userName=UserContextUtil.getInstance().getCurrentUserId();
		if(letterIds!=null&&zhuangtais!=null){
			iWorkMailReceiveStarService.reallyDelete(letterIds, zhuangtais, userName);
			msg = "succ";
		}
		ResponseUtil.write(msg);
	}
	/**
	 * 查找
	 * @param iWorkMailReceiveStarService
	 */
	public String findByIdStar(){
		String userName=UserContextUtil.getInstance().getCurrentUserId();
		if(zhuangtais!=null&&zhuangtais.equals(userName)){
			mailOwnerModel=iWorkMailReceiveStarService.findOwnerByIdStar(letterIds);
			
		}
		if(zhuangtais!=null&&!zhuangtais.equals(userName)){
			mailTaskModel=iWorkMailReceiveStarService.findTaskByIdStar(letterIds);
		}
		return SUCCESS;
	}
	/**
	 * 单击是否标星
	 * @author wanglei
	 * @param iWorkMailReceiveStarService
	 */
	public void clickStar(){
		String msg = "false";
		iWorkMailReceiveStarService.clickStar(id,emailType,isstar);
		msg = "succ";
		ResponseUtil.write(msg);
	}
	public void setiWorkMailReceiveStarService(
			IWorkMailReceiveStarService iWorkMailReceiveStarService) {
		this.iWorkMailReceiveStarService = iWorkMailReceiveStarService;
	}

	public List<HashMap> getListMap() {
		return listMap;
	}
	public void setListMap(List<HashMap> listMap) {
		this.listMap = listMap;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getLetterIds() {
		return letterIds;
	}
	public void setLetterIds(String letterIds) {
		this.letterIds = letterIds;
	}
	public String getZhuangtais() {
		return zhuangtais;
	}
	public void setZhuangtais(String zhuangtais) {
		this.zhuangtais = zhuangtais;
	}
	public MailTaskModel getMailTaskModel() {
		return mailTaskModel;
	}
	public void setMailTaskModel(MailTaskModel mailTaskModel) {
		this.mailTaskModel = mailTaskModel;
	}
	public MailOwnerModel getMailOwnerModel() {
		return mailOwnerModel;
	}
	public void setMailOwnerModel(MailOwnerModel mailOwnerModel) {
		this.mailOwnerModel = mailOwnerModel;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEmailType() {
		return emailType;
	}
	public void setEmailType(Long emailType) {
		this.emailType = emailType;
	}
	public Long getIsstar() {
		return isstar;
	}
	public void setIsstar(Long isstar) {
		this.isstar = isstar;
	}
	public Long getIsread() {
		return isread;
	}
	public void setIsread(Long isread) {
		this.isread = isread;
	}
	
	
}
