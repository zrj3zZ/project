package com.iwork.plugs.resoucebook.action;

import java.util.Date;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.resoucebook.model.IworkRmSpace;
import com.iwork.plugs.resoucebook.model.IworkRmWeb;
import com.iwork.plugs.resoucebook.service.ResourceBookService;
import com.opensymphony.xwork2.ActionSupport;

public class ResourceBookAction extends ActionSupport {
	public ResourceBookService resourceBookService;
	private Long spaceId;
	private String resouceid;
	private String resoucename;
	private String spaceName;
	private String info;
	private String html;
	private String cycle;
	private Date startdate;
	private IworkRmWeb model;
	/**
	 * 空间预定页面
	 * @return
	 */
	public String spaceIndex(){
		if(spaceId!=null){
			IworkRmSpace model = resourceBookService.getSpaceManageDao().getSpaceModel(spaceId);
			if(model.getCycle()==null){
				model.setCycle(new Long(7));
			}
			cycle= resourceBookService.getCycleStr(model.getCycle().intValue());
			spaceName = resourceBookService.getSpaceManageDao().getSpaceName(spaceId);
			info = resourceBookService.getSpaceManageDao().getSpaceMemo(spaceId);
			html = resourceBookService.spaceIndex(model,null); 
			if(startdate==null){
				startdate = new Date();
			}
		} 
		return SUCCESS;
	}
	/**
	 * 打开直接预定页面
	 * @return
	 */
	public String showReservePage(){
		model = new IworkRmWeb();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		model.setSpaceid(spaceId);
		model.setUserid(uc.get_userModel().getUserid());
		model.setUsername(uc.get_userModel().getUsername());
		model.setResouceid(resouceid); 
		model.setResouce(resoucename);
		model.setStatus(new Long(1));
		model.setSpacename(spaceName);
		return SUCCESS;
	}
	/**
	 * 保存预定
	 * @return
	 */
	public void saveReserve(){
		String msg = "";
		if(model!=null){
			msg = resourceBookService.executeAddRMWeb(model);
		}
		ResponseUtil.write(msg);
	}
	/**
	 * 下一预定周期
	 * @return
	 */
	public String nextCycle(){
		if(spaceId!=null&&startdate!=null){
			IworkRmSpace model = resourceBookService.getSpaceManageDao().getSpaceModel(spaceId);
			if(model.getCycle()==null){
				model.setCycle(new Long(7));
			}
			cycle= resourceBookService.getCycleStr(model.getCycle().intValue());
			info = resourceBookService.getSpaceManageDao().getSpaceMemo(spaceId);
			startdate = resourceBookService.getNextDate(model.getCycle().intValue(), startdate);
			html = resourceBookService.spaceIndex(model,startdate);  
			
		} 
		return SUCCESS;
	}
	/**
	 * 上一预定周期
	 * @return
	 */
	public String previousCycle(){
		if(spaceId!=null&&startdate!=null){
			IworkRmSpace model = resourceBookService.getSpaceManageDao().getSpaceModel(spaceId);
			if(model.getCycle()==null){
				model.setCycle(new Long(7));
			}
			cycle= resourceBookService.getCycleStr(model.getCycle().intValue());
			info = resourceBookService.getSpaceManageDao().getSpaceMemo(spaceId);
			startdate = resourceBookService.getPreviousDate(model.getCycle().intValue(), startdate);
			html = resourceBookService.spaceIndex(model,startdate);  
		} 
		return SUCCESS;
	}
	
	public ResourceBookService getResourceBookService() {
		return resourceBookService;
	}
	public void setResourceBookService(ResourceBookService resourceBookService) {
		this.resourceBookService = resourceBookService;
	}

	public Long getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public IworkRmWeb getModel() {
		return model;
	}

	public void setModel(IworkRmWeb model) {
		this.model = model;
	}

	public String getResouceid() {
		return resouceid;
	}

	public void setResouceid(String resouceid) {
		this.resouceid = resouceid;
	}

	public String getResoucename() {
		return resoucename;
	}

	public void setResoucename(String resoucename) {
		this.resoucename = resoucename;
	}

	
	
	
}
