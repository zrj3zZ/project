package com.iwork.core.organization.action;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.iwork.app.log.operationlog.constant.LogInfoConstants;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.UUIDUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgStation;
import com.iwork.core.organization.model.OrgStationIns;
import com.iwork.core.organization.service.OrgStationService;
import com.iwork.core.organization.service.OrgStationTreeService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.core.util.SequenceUtil;
import com.opensymphony.xwork2.ActionSupport;

public class OrgStationAction extends ActionSupport {
	private static final Logger logger = LoggerFactory.getLogger(OrgStationAction.class);
	private OrgStationService orgStationService;
	private OrgStationTreeService orgStationTreeService;
	private OrgStation model;
	private OrgStationIns osi;
	private String stationHtml = "";
	private Long id;
	private Long stationId;
	private Long insId;
	private String companylist = "";
	private String deptlist = "";
	private String userlist = "";
	private Long status;
	private String nodeType;
	private List<OrgStationIns> station_ins_list;
	private List<OrgStation> station_list;
	private String companyid;
	private String companyId;
	private String inputName;
	private String inputTitle;
	private String ids;
	/**
	 * 岗位路由设置
	 * @return
	 */
	public String index(){
		stationHtml = orgStationService.getStationList();
		
		return SUCCESS;
	}
	
	/**
	 * 新增岗位窗口
	 * @return
	 */
	public String addStationDlg(){
		
		return SUCCESS;
	}
	/**
	 * 加载岗位窗口
	 * @return
	 */
	public String loadStationDlg(){
		if(id!=null){
			model = orgStationService.getOrgStationDAO().getModel(id);
		}
		return SUCCESS;
	}
	/**
	 * 保存岗位
	 * @return
	 */
	public void saveStation(){ 
		if(model!=null){
			if(model.getId()==null){
				model.setUuid(UUIDUtil.getUUID());
				orgStationService.getOrgStationDAO().saveStation(model);
				ResponseUtil.write("add");
			}else{
				orgStationService.getOrgStationDAO().updateStation(model);
				ResponseUtil.write("update");
			}
		
		}else{
			
		}
	}
	/**
	 * 删除岗位
	 * @return
	 */
	public void deleteStation(){ 
		if(id!=null){
			boolean flag = orgStationService.deleteStation(id);
			if(flag){
				ResponseUtil.write(SUCCESS);
			}else{
				ResponseUtil.write(ERROR);
			}
		}
	}
	/**
	 * 删除岗位
	 * @return
	 */
	public void deleteStationIns(){ 
		if(ids!=null){
			boolean flag = orgStationService.deleteStationIns(ids);
			if(flag){
				ResponseUtil.write(SUCCESS);
			}else{
				ResponseUtil.write(ERROR);
			}
		}
	}
	
	
	
	/**
	 * 加载岗位路由列表
	 * @return
	 */
	public String loadStationInsList(){
		if(stationId!=null){
			station_ins_list = 	orgStationService.getOrgStationDAO().getStationInsList(stationId);
		}
		return SUCCESS;
	}
	/**
	 * 新增岗位窗口
	 * @return
	 */
	public String addStationInsDlg(){
		osi = new OrgStationIns();
		osi.setStatus(new Long(1));
		osi.setStationId(stationId);
		int orderindex = SequenceUtil.getInstance().getSequenceIndex("STATION_INS");
		osi.setOrderindex(new Long(orderindex));
		return SUCCESS;
	}
	
	/**
	 * 加载岗位窗口
	 * @return
	 */
	public String loadStationInsDlg(){
		if(id!=null){
			osi = orgStationService.getOrgStationDAO().getStationInsModel(id);
		}
		return SUCCESS;
	}
	/**
	 * 保存岗位
	 * @return
	 */
	public void saveStationIns(){ 
		if(osi!=null){
			if(osi.getId()==null){
				orgStationService.getOrgStationDAO().saveStationIns(osi);
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("岗位名称【").append(osi.getTitle()).append("】,岗位ID【").append(osi.getId()).append("】");
				LogUtil.getInstance().addLog(model.getId(), LogInfoConstants.ORG_STATION_INS_ADD, log.toString());
				ResponseUtil.write(osi.getId()+"");
			}else{
				orgStationService.getOrgStationDAO().updateStationIns(osi);
				//添加审计日志
				StringBuffer log = new StringBuffer();
				log.append("岗位名称【").append(osi.getTitle()).append("】,岗位ID【").append(osi.getId()).append("】");
				LogUtil.getInstance().addLog(model.getId(), LogInfoConstants.ORG_STATION_INS_ADD, log.toString());
				ResponseUtil.write(osi.getId()+"");
			}
		}
	}
	
	/**
	 * 范围
	 * @return
	 */
	public String scopeIndex(){
		stationHtml = orgStationService.getSelectContent(insId, "dept");
		return SUCCESS;
	}
	/**
	 * 用户范围
	 * @return
	 */
	public String scopeUserIndex(){
		stationHtml = orgStationService.getSelectContent(insId, "user");
		return SUCCESS;
	}
	
	/**
	 * 获得岗位作用范围json --部门
	 */
	public void showStationScopeJson(){
			String json = "";
			if(companyid==null || "".equals(companyid)){
				companyid = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getId();
			}
			if(nodeType!=null){ 
				if(id!=null&&nodeType.equals("dept")){
					json = orgStationService.getDeptJson(new Long(id),insId);
				}else{
					json = orgStationService.getCompanyNodeJson(companyid,insId,nodeType,false); 
				} 
			}else{ 
					Long dept = null;
					if(nodeType==null){
						nodeType = "com";
						json = orgStationService.getCompanyNodeJson(companyid,insId,nodeType,true);
					}
					 
			}
			ResponseUtil.write(json); 
	}
	
	
	/**
	 * 获得岗位作用范围json   --人员
	 */
	public void showStationScopeUserJson(){
		String json = "";
		if(companyid==null || "".equals(companyid)){
			companyid = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getId();
		}
		if(nodeType != null){ 
			if(id!=null && nodeType.equals("dept")){
				json = orgStationTreeService.getDeptAndUserJson(insId,new Long(id));
			}else{
				json = orgStationTreeService.getCompanyNodeJson(id+"",nodeType,false); 
			}
		}else{ 
			Long dept = null;
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				nodeType = "com";
				json = orgStationTreeService.getCompanyNodeJson(companyid,nodeType,true); 
		}  
		ResponseUtil.write(json);
		
	}
	
	/**
	 * 获得岗位作用范围json
	 */
	public void stationScopeItemSave(){
		if(stationId!=null&&insId!=null){
			String json = orgStationService.stationScopeItemSave(stationId, insId, companylist, deptlist);
			ResponseUtil.write(json); 
		}
	}
	
	/**
	 * 获得岗位作用范围json
	 */
	public void stationScopeUserItemSave(){
		if(stationId!=null&&insId!=null){
			String json = orgStationService.stationScopeItemSave(stationId, insId, userlist);
			ResponseUtil.write(json); 
		}
	}
	
	/**
	 * 岗位地址簿
	 * @return
	 */
	public String orgStationBook(){
		station_list = orgStationService.getOrgStationDAO().getAllList();
		return SUCCESS;
	}
	public OrgStationService getOrgStationService() {
		return orgStationService;
	}
	public void setOrgStationService(OrgStationService orgStationService) {
		this.orgStationService = orgStationService;
	}
	public String getStationHtml() {
		return stationHtml;
	}
	public void setStationHtml(String stationHtml) {
		this.stationHtml = stationHtml;
	}

	public OrgStation getModel() {
		return model;
	}

	public void setModel(OrgStation model) {
		this.model = model;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<OrgStationIns> getStation_ins_list() {
		return station_ins_list;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setOsi(OrgStationIns osi) {
		this.osi = osi;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public OrgStationIns getOsi() {
		return osi;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public Long getInsId() {
		return insId;
	}

	public void setInsId(Long insId) {
		this.insId = insId;
	}

	public String getCompanylist() {
		return companylist;
	}

	public void setCompanylist(String companylist) {
		this.companylist = companylist;
	}

	public String getDeptlist() {
		return deptlist;
	}

	public void setDeptlist(String deptlist) {
		this.deptlist = deptlist;
	}

	public List<OrgStation> getStation_list() {
		return station_list;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public String getInputTitle() {
		return inputTitle;
	}

	public void setInputTitle(String inputTitle) {
		this.inputTitle = inputTitle;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public void setOrgStationTreeService(OrgStationTreeService orgStationTreeService) {
		this.orgStationTreeService = orgStationTreeService;
	}

	public String getUserlist() {
		return userlist;
	}

	public void setUserlist(String userlist) {
		this.userlist = userlist;
	}
	
}
