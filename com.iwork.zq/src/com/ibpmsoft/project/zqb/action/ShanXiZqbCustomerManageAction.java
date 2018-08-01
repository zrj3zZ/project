package com.ibpmsoft.project.zqb.action;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.upload.util.DownloadFileUtil;
import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.service.ShanXiZqbCustomerManageService;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class ShanXiZqbCustomerManageAction extends ActionSupport{
	/**
	 * 版本
	 */
	private static final long serialVersionUID = 1L;


	
	public String index(){
		if (pageNumber == 0)
			pageNumber = 1;
		if(rqtype==null)
			rqtype=0l;
		xmlxFormId=shanXiZqbCustomerManageService.getConfig("xmlxformid");
		xmlxDemId=shanXiZqbCustomerManageService.getConfig("xmlxdemid");
		xmgpFormId=shanXiZqbCustomerManageService.getConfig("xmgpformid");
		xmgpDemId=shanXiZqbCustomerManageService.getConfig("xmgpdemid");
		xmqyFormId=shanXiZqbCustomerManageService.getConfig("xmqyformid");
		xmqyDemId=shanXiZqbCustomerManageService.getConfig("xmqydemid");
		xmggshServer = ProcessAPI.getInstance().getProcessActDefId("XMGGSH");
		xmnhshServer = ProcessAPI.getInstance().getProcessActDefId("XMNHSH");
		nhfkshServer = ProcessAPI.getInstance().getProcessActDefId("NHFKSH");
		gzfkshServer = ProcessAPI.getInstance().getProcessActDefId("GZFKJHF");
		gpdjjgdServer = ProcessAPI.getInstance().getProcessActDefId("GPDJJGD");
		customerList = shanXiZqbCustomerManageService.getCurrentCustomerList(customername,zqdm,type,zwmc,zczbbegin,zczbend,gfgsrqbegin,gfgsrqend,pageSize,pageNumber,dq);
		totalNum=shanXiZqbCustomerManageService.getCurrentCustomerListSize(customername,zqdm,type,zwmc,zczbbegin,zczbend,gfgsrqbegin,gfgsrqend,dq);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		orgroleid = uc.get_userModel().getOrgroleid();
		return SUCCESS;
	}
	
	public void delete(){
		String info= shanXiZqbCustomerManageService.deleteCustomer(instanceid);
		ResponseUtil.write(info);
	}
	
	public void checkCFXX() {
		String info = shanXiZqbCustomerManageService.checkCFXX(customername,zqdm,zqjc,instanceid);
		ResponseUtil.write(info);
	}
	
	public void getJdzl(){
		String info = shanXiZqbCustomerManageService.getJdzl(jdmc,xmlx,customerno);
		ResponseUtil.write(info);
	}
	
	public void getBgJdzl(){
		String info = shanXiZqbCustomerManageService.getBgJdzl(xmbh,jdmc,xmlx);
		ResponseUtil.write(info);
	}
	
	public void getBgZLGD(){
		String info = shanXiZqbCustomerManageService.getBgZLGD(xmbh,jdmc,xmlx);
		ResponseUtil.write(info);
	}
	
	public void getCwZLGD(){
		String info = shanXiZqbCustomerManageService.getCwZLGD(xmbh,jdmc,xmlx);
		ResponseUtil.write(info);
	}
	
	public void getCwJdzl(){
		String info = shanXiZqbCustomerManageService.getCwJdzl(xmbh,jdmc,xmlx);
		ResponseUtil.write(info);
	}
	
	public void getGuaPaiContent(){
		String info = shanXiZqbCustomerManageService.getGuaPaiContent(customerno);
		ResponseUtil.write(info);
	}
	
	public String getXmlxProject(){
		if (pageNumber == 0)
			pageNumber = 1;
		xmlxList = shanXiZqbCustomerManageService.getXmlxProjectList(pageSize,pageNumber);
		xmlxListSize=shanXiZqbCustomerManageService.getXmlxProjectListSize();
		return SUCCESS;
	}
	
	public String getXmlxCyrProject(){
		if (pageNumber == 0)
			pageNumber = 1;
		xmlxCyrList = shanXiZqbCustomerManageService.getXmlxCyrProjectList(pageSize,pageNumber);
		xmlxCyrListSize=shanXiZqbCustomerManageService.getXmlxCyrProjectListSize();
		return SUCCESS;
	}
	
	public void doExcelXMExp(){
		HttpServletResponse response = ServletActionContext.getResponse();
		shanXiZqbCustomerManageService.doExcelExpXM(response);
	}
	
	public void doExcelCYExp(){
		HttpServletResponse response = ServletActionContext.getResponse();
		shanXiZqbCustomerManageService.doExcelCYExp(response);
	}
	
	public void getCustomerAutoData(){
		String jsonstr = shanXiZqbCustomerManageService.getCustomerAutoData();
		ResponseUtil.write(jsonstr); 
	}
	
	public void expCustomer(){
		HttpServletResponse response = ServletActionContext.getResponse();
		shanXiZqbCustomerManageService.expCustomer(response);
	}




	private int pageNumber; // 当前页数
	private int totalNum; // 总页数
	private int pageSize = 10; // 每页条数
	private List<HashMap<String,Object>> customerList;
	private Long orgroleid;
	private String customername;
	private String customerno;
	private String zqdm;
	private String zqjc;
	private String type;
	private String zwmc;
	private BigDecimal zczbbegin;
	private BigDecimal zczbend;
	private String gfgsrqbegin;
	private String gfgsrqend;
	private Long instanceid;
	private String xmggshServer;//项目股改审核
	private String xmnhshServer;//项目内核审核
	private String nhfkshServer;//内核反馈审核
	private String gzfkshServer;//股转反馈审核
	private String gpdjjgdServer;//挂牌登记及归档审核
	private Long xmlxDemId;
	private Long xmlxFormId;
	private Long xmgpDemId;
	private Long xmgpFormId;
	private String dq;//所属大区
	private String jdmc;
	private List<HashMap<String,Object>> xmlxList;
	private int xmlxListSize; // 当前页数
	private List<HashMap<String,Object>> xmlxCyrList;
	private int xmlxCyrListSize; // 当前页数
	private String xmlx;
	private String xmbh;
	private Long xmqyFormId;
	private Long xmqyDemId;
	private Long rqtype;
	private String ID ;


	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}



	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public ShanXiZqbCustomerManageService getShanXiZqbCustomerManageService() {
		return shanXiZqbCustomerManageService;
	}

	public Long getRqtype() {
		return rqtype;
	}

	public void setRqtype(Long rqtype) {
		this.rqtype = rqtype;
	}

	public Long getXmqyFormId() {
		return xmqyFormId;
	}

	public void setXmqyFormId(Long xmqyFormId) {
		this.xmqyFormId = xmqyFormId;
	}

	public Long getXmqyDemId() {
		return xmqyDemId;
	}

	public void setXmqyDemId(Long xmqyDemId) {
		this.xmqyDemId = xmqyDemId;
	}

	private ShanXiZqbCustomerManageService shanXiZqbCustomerManageService;
	
	public String getNhfkshServer() {
		return nhfkshServer;
	}

	public void setNhfkshServer(String nhfkshServer) {
		this.nhfkshServer = nhfkshServer;
	}

	public String getXmnhshServer() {
		return xmnhshServer;
	}

	public void setXmnhshServer(String xmnhshServer) {
		this.xmnhshServer = xmnhshServer;
	}

	public List<HashMap<String, Object>> getXmlxCyrList() {
		return xmlxCyrList;
	}

	public void setXmlxCyrList(List<HashMap<String, Object>> xmlxCyrList) {
		this.xmlxCyrList = xmlxCyrList;
	}

	public int getXmlxCyrListSize() {
		return xmlxCyrListSize;
	}

	public void setXmlxCyrListSize(int xmlxCyrListSize) {
		this.xmlxCyrListSize = xmlxCyrListSize;
	}

	public List<HashMap<String, Object>> getXmlxList() {
		return xmlxList;
	}

	public void setXmlxList(List<HashMap<String, Object>> xmlxList) {
		this.xmlxList = xmlxList;
	}

	public int getXmlxListSize() {
		return xmlxListSize;
	}

	public void setXmlxListSize(int xmlxListSize) {
		this.xmlxListSize = xmlxListSize;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public Long getXmgpDemId() {
		return xmgpDemId;
	}

	public void setXmgpDemId(Long xmgpDemId) {
		this.xmgpDemId = xmgpDemId;
	}

	public Long getXmgpFormId() {
		return xmgpFormId;
	}

	public void setXmgpFormId(Long xmgpFormId) {
		this.xmgpFormId = xmgpFormId;
	}

	public String getJdmc() {
		return jdmc;
	}

	public void setJdmc(String jdmc) {
		this.jdmc = jdmc;
	}
	public Long getXmlxDemId() {
		return xmlxDemId;
	}

	public void setXmlxDemId(Long xmlxDemId) {
		this.xmlxDemId = xmlxDemId;
	}

	public Long getXmlxFormId() {
		return xmlxFormId;
	}

	public void setXmlxFormId(Long xmlxFormId) {
		this.xmlxFormId = xmlxFormId;
	}

	public String getXmggshServer() {
		return xmggshServer;
	}

	public void setXmggshServer(String xmggshServer) {
		this.xmggshServer = xmggshServer;
	}

	public String getZqjc() {
		return zqjc;
	}

	public void setZqjc(String zqjc) {
		this.zqjc = zqjc;
	}

	public Long getOrgroleid() {
		return orgroleid;
	}

	public void setOrgroleid(Long orgroleid) {
		this.orgroleid = orgroleid;
	}

	public List<HashMap<String, Object>> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<HashMap<String, Object>> customerList) {
		this.customerList = customerList;
	}

	public void setShanXiZqbCustomerManageService(ShanXiZqbCustomerManageService shanXiZqbCustomerManageService) {
		this.shanXiZqbCustomerManageService = shanXiZqbCustomerManageService;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getZqdm() {
		return zqdm;
	}

	public void setZqdm(String zqdm) {
		this.zqdm = zqdm;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getZwmc() {
		return zwmc;
	}

	public void setZwmc(String zwmc) {
		this.zwmc = zwmc;
	}

	public BigDecimal getZczbbegin() {
		return zczbbegin;
	}

	public void setZczbbegin(BigDecimal zczbbegin) {
		this.zczbbegin = zczbbegin;
	}

	public BigDecimal getZczbend() {
		return zczbend;
	}

	public void setZczbend(BigDecimal zczbend) {
		this.zczbend = zczbend;
	}

	public String getGfgsrqbegin() {
		return gfgsrqbegin;
	}

	public void setGfgsrqbegin(String gfgsrqbegin) {
		this.gfgsrqbegin = gfgsrqbegin;
	}

	public String getGfgsrqend() {
		return gfgsrqend;
	}

	public void setGfgsrqend(String gfgsrqend) {
		this.gfgsrqend = gfgsrqend;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public String getGzfkshServer() {
		return gzfkshServer;
	}

	public void setGzfkshServer(String gzfkshServer) {
		this.gzfkshServer = gzfkshServer;
	}

	public String getGpdjjgdServer() {
		return gpdjjgdServer;
	}

	public void setGpdjjgdServer(String gpdjjgdServer) {
		this.gpdjjgdServer = gpdjjgdServer;
	}

	public String getXmlx() {
		return xmlx;
	}

	public void setXmlx(String xmlx) {
		this.xmlx = xmlx;
	}

	public String getXmbh() {
		return xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

	public String getDq() {
		return dq;
	}

	public void setDq(String dq) {
		this.dq = dq;
	}
	
}
