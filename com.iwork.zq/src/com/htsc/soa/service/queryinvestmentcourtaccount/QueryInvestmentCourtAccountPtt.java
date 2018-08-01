package com.htsc.soa.service.queryinvestmentcourtaccount;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "QueryInvestmentCourtAccount_ptt", targetNamespace = "http://soa.htsc.com/service/QueryInvestmentCourtAccount")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface QueryInvestmentCourtAccountPtt {

	/**
	 * 
	 * @param requestPart
	 * @return returns com.htsc.soa.service.queryinvestmentcourtaccount.
	 *         QueryInvestmentCourtAccountResp
	 */
	@WebMethod(operationName = "CrmQueryInvestmentCourtAccount", action = "http://soa.htsc.com/service/QueryInvestmentCourtAccount/CrmQueryInvestmentCourtAccount")
	@WebResult(name = "QueryInvestmentCourtAccountResp", targetNamespace = "http://soa.htsc.com/service/QueryInvestmentCourtAccount", partName = "ResponsePart")
	public QueryInvestmentCourtAccountResp crmQueryInvestmentCourtAccount(
			@WebParam(name = "QueryInvestmentCourtAccountReq", targetNamespace = "http://soa.htsc.com/service/QueryInvestmentCourtAccount", partName = "RequestPart") QueryInvestmentCourtAccountReq requestPart);

}