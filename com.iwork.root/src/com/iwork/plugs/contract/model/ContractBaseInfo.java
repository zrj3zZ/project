package com.iwork.plugs.contract.model;

public class ContractBaseInfo {
	private String sumNum;   //合同总金额
	private Long demId;   //主数据模型ID
	private Long formId;   //表单ID
	private long planNum;   //计划收/付款金额
	private long realNum;  //实际收/付款金额
	private long ticketPlanNum;  //发票计划金额
	private long ticketNum;     //发票执行金额
	private long accountsNum;  //应收付款金额
	private Long eventNum;
	private Long InvoiceBalance;  //发票余额
	private String planCollectionString;//计划收款金额字符串
	private String invoicePlanCollectionString;//发票计划收款金额字符串
	private String actualCollectionString;//实际收款金额字符串
	private String invoicePerformCollectionString;//发票执行收款金额字符串
	private String InvoiceBalanceString;//发票余额字符串
	private String shouldString;//应收付款金额字符串
	private String currencyString;//币种
	public Long getEventNum() {
		return eventNum;
	}
	public void setEventNum(Long eventNum) {
		this.eventNum = eventNum;
	}
	public long getAccountsNum() {
		return accountsNum;
	}
	public void setAccountsNum(long accountsNum) {
		this.accountsNum = accountsNum;
	}
	public String getSumNum() {
		return sumNum;
	}
	public void setSumNum(String sumNum) {
		this.sumNum = sumNum;
	}
	public long getPlanNum() {
		return planNum;
	}
	public void setPlanNum(long planNum) {
		this.planNum = planNum;
	}
	public long getRealNum() {
		return realNum;
	}
	public void setRealNum(long realNum) {
		this.realNum = realNum;
	}
	public long getTicketPlanNum() {
		return ticketPlanNum;
	}
	public void setTicketPlanNum(long ticketPlanNum) {
		this.ticketPlanNum = ticketPlanNum;
	}
	public long getTicketNum() {
		return ticketNum;
	}
	public void setTicketNum(long ticketNum) {
		this.ticketNum = ticketNum;
	}
	public Long getDemId() {
		return demId;
	}
	public void setDemId(Long demId) {
		this.demId = demId;
	}
	public Long getFormId() {
		return formId;
	}
	public void setFormId(Long formId) {
		this.formId = formId;
	}
	public String getPlanCollectionString() {
		return planCollectionString;
	}
	public void setPlanCollectionString(String planCollectionString) {
		this.planCollectionString = planCollectionString;
	}
	public String getInvoicePlanCollectionString() {
		return invoicePlanCollectionString;
	}
	public void setInvoicePlanCollectionString(String invoicePlanCollectionString) {
		this.invoicePlanCollectionString = invoicePlanCollectionString;
	}
	public String getActualCollectionString() {
		return actualCollectionString;
	}
	public void setActualCollectionString(String actualCollectionString) {
		this.actualCollectionString = actualCollectionString;
	}
	public String getInvoicePerformCollectionString() {
		return invoicePerformCollectionString;
	}
	public void setInvoicePerformCollectionString(
			String invoicePerformCollectionString) {
		this.invoicePerformCollectionString = invoicePerformCollectionString;
	}
	public String getShouldString() {
		return shouldString;
	}
	public void setShouldString(String shouldString) {
		this.shouldString = shouldString;
	}
	public String getCurrencyString() {
		return currencyString;
	}
	public void setCurrencyString(String currencyString) {
		this.currencyString = currencyString;
	}
	public Long getInvoiceBalance() {
		return InvoiceBalance;
	}
	public void setInvoiceBalance(Long invoiceBalance) {
		InvoiceBalance = invoiceBalance;
	}
	public String getInvoiceBalanceString() {
		return InvoiceBalanceString;
	}
	public void setInvoiceBalanceString(String invoiceBalanceString) {
		InvoiceBalanceString = invoiceBalanceString;
	}
	
	
	
}
