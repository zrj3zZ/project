package com.iwork.plugs.contract.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.iwork.commons.util.Number2RMB;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.contract.model.ContractBaseInfo;
import com.iwork.plugs.contract.service.IWorkContractService;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkContractAction extends ActionSupport {
	private IWorkContractService iworkContractService;
	private List<HashMap> list;
	private String itemno;//获取合同编号
	private ContractBaseInfo baseinfo;//工具类javabean对象
	private Double count;
	private Long instanceId;
	private HashMap<String,Object> hashMap;
	private String itemname;//合同名称
	private String ids;//实例
	private String dataid;//获取页面dateid
	private String pactNo;//合同编号
	private String pactName;//合同名称
	private String sum;//付款方式
	private String eventContract;//当前所选的合同种类（主、子）
	private String ContractProperties;//合同属性
	private String contractNature;//合同性质
	private int pageSize = 10; // 每页显示的条数
	private int total;
	private int pageNumber; // 当前页数
	private String formid;
	private String year;
	private String pactOwner;//合同甲方
	private String ownerNo;//甲方编号
	private String contrctDate;//合同开始日期
	private String fromDate;//合同结束日期
	private String pactParty;//合同乙方
	private String partyNo;//乙方编号
	/**
	 * 合同主界面
	 * @return
	 */
	public String getContractList(){
			int startRow = 0;
			if (pageNumber == 0) {
				pageNumber = 1;
			}
			if (pageSize == 0) {
				pageSize = 10;
			}
			if (pageNumber > 1) {
				startRow = (pageNumber - 1) * pageSize;
			}
			if(year==null){
				Date date = new Date();
				year = UtilDate.yearFormat(date);
			}
			if(itemno==null){
				itemno = "";
			}
			if(itemname==null){
				itemname = "";
			}
			list=iworkContractService.getContractList(itemno,itemname,year,pageSize,startRow);
			total = iworkContractService.getContractListSize(itemno,itemname,year);
		
		return SUCCESS;
	}
	/**
	 * 获取合同管理员
	 * @return
	 */
	public String contractindex(){
		hashMap = iworkContractService.getContractOptions();
		return SUCCESS;
	}
	public String index(){
		
		return SUCCESS;
	}
	/**
	 * 转换数字为人民币金额
	 */
	public void getNumberRMB(){
		if(count!=null && !"".equals(count)){
			ResponseUtil.write(Number2RMB.convert(count));
		}else{
			ResponseUtil.write("零元整");
		}
	}
	/**
	 *加载合同详细信息 
	 * @return
	 */
	public String showItemIndex(){
		if(itemno!=null){
			
			
		}
		return SUCCESS;
	}
	/**
	 * 加载合同基本信息
	 */
	public String showContractInstanceid(){
	   
		if(instanceId!=null){
			hashMap=iworkContractService.getDemidAndFormid();
			hashMap.put("Instanceid", instanceId);
		}
		return SUCCESS;
	}
	/**
	 * 加载子合同表单
	 */
	public void showSubContractList(){
		String html = "";
		if(itemno!=null){
			html = iworkContractService.getSubContrctListHtml(itemno);
		}
		ResponseUtil.write(html);
	}
	
	/**
	 * 合同执行计划列表
	 * @return
	 */
	public String planlist(){
		if(itemno!=null){
			//获取信息预览列表数据
			baseinfo = iworkContractService.getPlanListContractInfo(itemno);
			//获取计划列表
			list = iworkContractService.getPlanList(itemno); 
			return SUCCESS;
		}else{
			return ERROR;
		}
		
	}

	/**
	 * 获取发票计划列表数据
	 * @return
	 */
	public String Ivoiceplanlist(){
		if(itemno!=null){
			//获取信息预览列表数据
			baseinfo = iworkContractService.getIvoicePlanListContractInfo(itemno);
			//获取发票计划列表
			list = iworkContractService.getIvoicePlanList(itemno); 
			return SUCCESS;
		}else{
			return ERROR;
		}
		
	}
	/**
	 * 获取DemidAndFormid
	 */
	public void getDemidAndFormid(){
		StringBuffer json = new StringBuffer();
		HashMap hashmap = new HashMap();
		hashmap=iworkContractService.getDemidAndFormid();
		JSONObject jsonArray = JSONObject.fromObject(hashmap);
		json.append(jsonArray);
		ResponseUtil.write(json.toString());
	}
	/**
	 * 获取设置的DemidAndFormid
	 */
	public void getOptionsDemidAndFormid(){
		StringBuffer json = new StringBuffer();
		HashMap hashmap = new HashMap();
		hashmap=iworkContractService.getOptionsDemidAndFormid();
		JSONObject jsonArray = JSONObject.fromObject(hashmap);
		json.append(jsonArray);
		ResponseUtil.write(json.toString());
	}
	
	/**
	 * 获取交货计划列表数据
	 * @return
	 */
	public String deliveryPlanList(){
		if(itemno!=null){
			//获取信息预览列表数据
			baseinfo = iworkContractService.getDeliveryPlanContractInfo(itemno);
			//获取交货计划列表
			list = iworkContractService.getDeliveryPlanList(itemno); 
			return SUCCESS;
		}else{
			return ERROR;
		}
		
	}
	

	/**
	 * 获取合同执行列表数据
	 * @return
	 */
	public String contractPerformList(){
		if(itemno!=null){
			//获取信息预览列表数据
			baseinfo = iworkContractService.getContractPerformContractInfo(itemno);
			//获取合同执行列表
			list = iworkContractService.getContractPerformList(itemno); 
			return SUCCESS;
		}else{
			return ERROR;
		}
		
	}
	


	/**
	 * 获取发票执行列表数据
	 * @return
	 */
	public String ivoicePerformList(){
		if(itemno!=null){
			//获取信息预览列表数据
			baseinfo = iworkContractService.getIvoicePerformContractInfo(itemno);
			//获取发票执行列表
			list = iworkContractService.getIvoicePerformList(itemno); 
			return SUCCESS;
		}else{
			return ERROR;
		}
		
	}

	/**
	 * 获取交货执行列表数据
	 * @return
	 */
	public String deliveryPerformList(){
		if(itemno!=null){
			//获取信息预览列表数据
			baseinfo = iworkContractService.getDeliveryPerformContractInfo(itemno);
			//获取交货列表
			list = iworkContractService.getDeliveryPerformList(itemno); 
			return SUCCESS;
		}else{
			return ERROR;
		}
		
	}
	
	/**
	 * 验证开票金额
	 * @return
	 */
	public void ivoicePerformValidation(){
		if(itemno!=null){
			//获取信息预览列表数据
			baseinfo = iworkContractService.getSum(itemno,dataid);
			HashMap hashmap = new HashMap();
			hashmap.put("ticketNum", baseinfo.getTicketNum());
			hashmap.put("sumNum", baseinfo.getSumNum());
			hashmap.put("eventNum", baseinfo.getEventNum());
			JSONObject json = JSONObject.fromObject(hashmap);
			String data_str = json.toString();
			ResponseUtil.write(data_str);
			
		}
		
	}
	/**
	 * 获取最新的发票余额
	 * @return  yanglianfeng
	 */
	public void ivoicePerformUpdate(){
		if(itemno!=null){
			//获取信息预览列表数据
			baseinfo = iworkContractService.getIvoicePerformUpdate(itemno);
			HashMap hashmap = new HashMap();
			hashmap.put("ticketNum", baseinfo.getTicketNum());
			hashmap.put("sumNum", baseinfo.getSumNum());
			hashmap.put("eventNum", baseinfo.getEventNum());
			JSONObject json = JSONObject.fromObject(hashmap);
			String data_str = json.toString();
			ResponseUtil.write(data_str);
			
		}
		
	}
	
	
	
	/**
	 * 
	 * 彻底合同记录
	 * 
	 * @author 杨连峰
	 * @return
	 */
	public void cleanContract() {
		String msg = "false";
		//HttpServletRequest request = ServletActionContext.getRequest();
		// 获取页面要删除的id
		//String ids = request.getParameter("ids");
		if(ids!=null||""!=ids){
		iworkContractService.cleanContract(ids);
		msg = "succ";
		ResponseUtil.write(msg);
		}
	}

	/**
	 * 
	 * 查询合同
	 * 
	 * @author 杨连峰
	 * @return
	 */
	public String queryContract() {
		int startRow = 0;
		if (pageNumber == 0) {
			pageNumber = 1;
		}
		if (pageSize == 0) {
			pageSize = 10;
		}
		if (pageNumber > 1) {
			startRow = (pageNumber - 1) * pageSize;
		}
		if(pactNo==null){
			pactNo="";
		}
		if(pactName==null){
			pactName="";
		}
		if(sum==null){
			sum="";
		}
		if(eventContract==null){
			eventContract="";
		}
		list=iworkContractService.getQueryContract(pactNo,pactName,sum,eventContract,ContractProperties,contractNature,year,pageSize,startRow);
		total = iworkContractService.getQuerySize(pactNo, pactName, sum, eventContract,ContractProperties,contractNature,year);
		return SUCCESS;
	}
	/**
	 * 获取所有合同
	 * @author 杨连峰
	 * @return
	 */
	public String getAllContractList(){
		
		return SUCCESS;
	}
	
	/**
	 * 获取总金额、计划金额、执行金额、等
	 * @author 杨连峰
	 * @return
	 */
	public void getAccountsNum(){
		if(itemno!=null){
			//获取信息预览列表数据
			baseinfo = iworkContractService.getContractPerformContractInfo(itemno,dataid,formid);
			HashMap hashmap = new HashMap();		
			hashmap.put("sumNum", baseinfo.getSumNum());//合同总金额
			hashmap.put("planNum", baseinfo.getPlanNum());//计划收付款金额			
			hashmap.put("ticketPlanNum", baseinfo.getTicketPlanNum());//发票计划金额
			hashmap.put("realNum", baseinfo.getRealNum());//实际收付款金额
			hashmap.put("ticketNum", baseinfo.getTicketNum());//发票执行金额
			hashmap.put("accountsNum", baseinfo.getAccountsNum());//应收付款金额
			hashmap.put("eventNum", baseinfo.getEventNum());
			JSONObject json = JSONObject.fromObject(hashmap);
			String data_str = json.toString();
			ResponseUtil.write(data_str);
			
		}
		
	}
	
	/**
	 * 按照客户统计查询
	 * @author 杨连峰 2015-07-14
	 * @return
	 */
	  public String queryCustomerList(){
			
			return SUCCESS;
		}
	
   /**
	 * 
	 * 按客户统计查询
	 * 2015-07-14
	 * @author 杨连峰
	 * @return
	 */
	public String queryCustomer() {
		int startRow = 0;
		if (pageNumber == 0) {
			pageNumber = 1;
		}
		if (pageSize == 0) {
			pageSize = 10;
		}
		if (pageNumber > 1) {
			startRow = (pageNumber - 1) * pageSize;
		}
		if(pactOwner==null){
			pactOwner="";
		}
		if(ownerNo==null){
			ownerNo="";
		}
		if(contrctDate==null){
			contrctDate="";
		}
		if(fromDate==null){
			fromDate="";
		}
		
		list=iworkContractService.getqueryCustome(pactOwner,ownerNo,year,contrctDate,fromDate,pageNumber,pageSize);
		total = iworkContractService.getCustomeSize(pactOwner, ownerNo,contrctDate,fromDate,year);
		return SUCCESS;
	}
	
	
	/**
	 * 按照供应商统计查询
	 * @author 杨连峰 2015-07-14
	 * @return
	 */
	  public String querySpplierList(){
			
			return SUCCESS;
		}
	
   /**
	 * 
	 * 按供应商统计查询
	 * 2015-07-14
	 * @author 杨连峰
	 * @return
	 */
	public String querySpplier() {
		int startRow = 0;
		if (pageNumber == 0) {
			pageNumber = 1;
		}
		if (pageSize == 0) {
			pageSize = 10;
		}
		if (pageNumber > 1) {
			startRow = (pageNumber - 1) * pageSize;
		}
		if(pactParty==null){
			pactParty="";
		}
		if(partyNo==null){
			partyNo="";
		}
		if(contrctDate==null){
			contrctDate="";
		}
		if(fromDate==null){
			fromDate="";
		}
		
		list=iworkContractService.getquerySpplier(partyNo,pactParty,year,contrctDate,fromDate,pageNumber,pageSize);
		total = iworkContractService.getSpplierSize(pactParty, partyNo,contrctDate,fromDate,year);
		return SUCCESS;
	}

	/**
	 * 按照应收账款统计查询
	 * @author 杨连峰 2015-07-14
	 * @return
	 */
	public String queryRceivableList(){
			
			return SUCCESS;
		}
	
	  /**
		 * 
		 * 按照应付账款统计查询
		 * 2015-07-14
		 * @author 杨连峰
		 * @return
		 */
	public String queryRceivable() {
			int startRow = 0;
			if (pageNumber == 0) {
				pageNumber = 1;
			}
			if (pageSize == 0) {
				pageSize = 10;
			}
			if (pageNumber > 1) {
				startRow = (pageNumber - 1) * pageSize;
			}
			if(pactNo==null){
				pactNo="";
			}
			if(pactName==null){
				pactName="";
			}
			if(sum==null){
				sum="";
			}
			
			if(eventContract==null){
				eventContract="";
			}
			list=iworkContractService.getQueryRceivable(pactNo,pactName,sum,eventContract,ContractProperties,contractNature,year,pageSize,startRow);
			total = iworkContractService.getQueryRceivableSize(pactNo, pactName,sum,eventContract,ContractProperties,contractNature,year);
			return SUCCESS;
		}
	
		/**
		 * 按照应收账款统计查询
		 * @author 杨连峰 2015-07-14
		 * @return
		 */
	 public String queryPayList(){
			
		return SUCCESS;
	}

  /**
	 * 
	 * 按照应收账款统计查询
	 * 2015-07-14
	 * @author 杨连峰
	 * @return
	 */
	public String queryPay() {
		int startRow = 0;
		if (pageNumber == 0) {
			pageNumber = 1;
		}
		if (pageSize == 0) {
			pageSize = 10;
		}
		if (pageNumber > 1) {
			startRow = (pageNumber - 1) * pageSize;
		}
		if(pactNo==null){
			pactNo="";
		}
		if(pactName==null){
			pactName="";
		}
		if(sum==null){
			sum="";
		}
		
		if(eventContract==null){
			eventContract="";
		}
		list=iworkContractService.getQueryPayList(pactNo,pactName,sum,eventContract,ContractProperties,contractNature,year,pageSize,startRow);
		total = iworkContractService.getQueryPay(pactNo, pactName,sum,eventContract,ContractProperties,contractNature,year);
		return SUCCESS;
	}




	
	
	
	/**
	 * 获取合同基本信息
	 * @return
	 */
	public String getBaseContract(){
		if(itemno!=null){
			hashMap = iworkContractService.getBaseContract(itemno);
		}
		return SUCCESS;
	}
	/**
	 * 获取父合同编号
	 * @return
	 */
/*	public void getFatherpactno(){
		HashMap hashmap = new HashMap();
		hashmap = iworkContractService.getFatherpactno();
		JSONObject json = JSONObject.fromObject(hashmap);
		String data_str = json.toString();
		ResponseUtil.write(data_str);
	}*/
	/**
	 * 设置
	 * @return
	 */
	public String options(){
		hashMap = iworkContractService.getOptions();
		return SUCCESS;
	}
	/**
	 * 获取合同执行计划审批的所有金额
	 * @return
	 */
	public void getFundAmount(){
		HashMap hashmap = new HashMap();
		if(instanceId!=null){
			hashmap = iworkContractService.getFundAmount(instanceId);
			JSONObject json = JSONObject.fromObject(hashmap);
			String data_str = json.toString();
			ResponseUtil.write(data_str);
		}
		
	}
	public IWorkContractService getIworkContractService() {
		return iworkContractService;
	}

	public void setIworkContractService(IWorkContractService iworkContractService) {
		this.iworkContractService = iworkContractService;
	}


	public String getItemno() {
		return itemno;
	}


	public void setItemno(String itemno) {
		this.itemno = itemno;
	}


	

	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public List<HashMap> getList() {
		return list;
	}
	public void setList(List<HashMap> list) {
		this.list = list;
	}


	public void setBaseinfo(ContractBaseInfo baseinfo) {
		this.baseinfo = baseinfo;
	}

	public ContractBaseInfo getBaseinfo() {
		return baseinfo;
	}
	public Double getCount() {
		return count;
	}
	public void setCount(Double count) {
		this.count = count;
	}
	public Long getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
	public HashMap getHashMap() {
		return hashMap;
	}
	public void setHashMap(HashMap hashMap) {
		this.hashMap = hashMap;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getDataid() {
		return dataid;
	}
	public void setDataid(String dataid) {
		this.dataid = dataid;
	}
	public String getPactNo() {
		return pactNo;
	}
	public void setPactNo(String pactNo) {
		this.pactNo = pactNo;
	}
	public String getPactName() {
		return pactName;
	}
	public void setPactName(String pactName) {
		this.pactName = pactName;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getEventContract() {
		return eventContract;
	}
	public void setEventContract(String eventContract) {
		this.eventContract = eventContract;
	}
	public String getContractProperties() {
		return ContractProperties;
	}
	public void setContractProperties(String contractProperties) {
		ContractProperties = contractProperties;
	}
	public String getContractNature() {
		return contractNature;
	}
	public void setContractNature(String contractNature) {
		this.contractNature = contractNature;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getFormid() {
		return formid;
	}
	public void setFormid(String formid) {
		this.formid = formid;
	}

	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getPactOwner() {
		return pactOwner;
	}
	public void setPactOwner(String pactOwner) {
		this.pactOwner = pactOwner;
	}
	
	public String getContrctDate() {
		return contrctDate;
	}
	public void setContrctDate(String contrctDate) {
		this.contrctDate = contrctDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getOwnerNo() {
		return ownerNo;
	}
	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}
	public String getPactParty() {
		return pactParty;
	}
	public void setPactParty(String pactParty) {
		this.pactParty = pactParty;
	}
	public String getPartyNo() {
		return partyNo;
	}
	public void setPartyNo(String partyNo) {
		this.partyNo = partyNo;
	}

	
	
	
	
	
}
