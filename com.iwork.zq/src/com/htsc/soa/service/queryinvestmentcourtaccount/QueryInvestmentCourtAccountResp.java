package com.htsc.soa.service.queryinvestmentcourtaccount;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.htsc.soa.basesoapobject.v1.ResponseHeaderType;
import com.htsc.soa.custmanage.v10.investmentcourtaccounttype.InvestmentCourtAccountType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResponseHeader" type="{http://soa.htsc.com/BaseSoapObject/v1}ResponseHeaderType"/>
 *         &lt;element name="ResponseBody">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="investmentCourtAccount" type="{http://soa.htsc.com/CustManage/v10/InvestmentCourtAccountType}InvestmentCourtAccountType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "responseHeader", "responseBody" })
@XmlRootElement(name = "QueryInvestmentCourtAccountResp")
public class QueryInvestmentCourtAccountResp {

	@XmlElement(name = "ResponseHeader", required = true)
	protected ResponseHeaderType responseHeader;
	@XmlElement(name = "ResponseBody", required = true)
	protected QueryInvestmentCourtAccountResp.ResponseBody responseBody;

	/**
	 * Gets the value of the responseHeader property.
	 * 
	 * @return possible object is {@link ResponseHeaderType }
	 * 
	 */
	public ResponseHeaderType getResponseHeader() {
		return responseHeader;
	}

	/**
	 * Sets the value of the responseHeader property.
	 * 
	 * @param value
	 *            allowed object is {@link ResponseHeaderType }
	 * 
	 */
	public void setResponseHeader(ResponseHeaderType value) {
		this.responseHeader = value;
	}

	/**
	 * Gets the value of the responseBody property.
	 * 
	 * @return possible object is
	 *         {@link QueryInvestmentCourtAccountResp.ResponseBody }
	 * 
	 */
	public QueryInvestmentCourtAccountResp.ResponseBody getResponseBody() {
		return responseBody;
	}

	/**
	 * Sets the value of the responseBody property.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link QueryInvestmentCourtAccountResp.ResponseBody }
	 * 
	 */
	public void setResponseBody(
			QueryInvestmentCourtAccountResp.ResponseBody value) {
		this.responseBody = value;
	}

	/**
	 * <p>
	 * Java class for anonymous complex type.
	 * 
	 * <p>
	 * The following schema fragment specifies the expected content contained
	 * within this class.
	 * 
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="investmentCourtAccount" type="{http://soa.htsc.com/CustManage/v10/InvestmentCourtAccountType}InvestmentCourtAccountType" maxOccurs="unbounded" minOccurs="0"/>
	 *       &lt;/sequence>
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "investmentCourtAccount" })
	public static class ResponseBody {

		protected List<InvestmentCourtAccountType> investmentCourtAccount;

		/**
		 * Gets the value of the investmentCourtAccount property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the investmentCourtAccount property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getInvestmentCourtAccount().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link InvestmentCourtAccountType }
		 * 
		 * 
		 */
		public List<InvestmentCourtAccountType> getInvestmentCourtAccount() {
			if (investmentCourtAccount == null) {
				investmentCourtAccount = new ArrayList<InvestmentCourtAccountType>();
			}
			return this.investmentCourtAccount;
		}

	}

}
