package com.htsc.soa.service.queryinvestmentcourtaccount;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * com.htsc.soa.service.queryinvestmentcourtaccount package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * com.htsc.soa.service.queryinvestmentcourtaccount
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of
	 * {@link QueryInvestmentCourtAccountResp.ResponseBody }
	 * 
	 */
	public QueryInvestmentCourtAccountResp.ResponseBody createQueryInvestmentCourtAccountRespResponseBody() {
		return new QueryInvestmentCourtAccountResp.ResponseBody();
	}

	/**
	 * Create an instance of {@link QueryInvestmentCourtAccountReq }
	 * 
	 */
	public QueryInvestmentCourtAccountReq createQueryInvestmentCourtAccountReq() {
		return new QueryInvestmentCourtAccountReq();
	}

	/**
	 * Create an instance of {@link QueryInvestmentCourtAccountReq.RequestBody }
	 * 
	 */
	public QueryInvestmentCourtAccountReq.RequestBody createQueryInvestmentCourtAccountReqRequestBody() {
		return new QueryInvestmentCourtAccountReq.RequestBody();
	}

	/**
	 * Create an instance of {@link QueryInvestmentCourtAccountResp }
	 * 
	 */
	public QueryInvestmentCourtAccountResp createQueryInvestmentCourtAccountResp() {
		return new QueryInvestmentCourtAccountResp();
	}

}
