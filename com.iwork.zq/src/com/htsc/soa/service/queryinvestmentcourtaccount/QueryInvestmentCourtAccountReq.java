package com.htsc.soa.service.queryinvestmentcourtaccount;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.htsc.soa.basesoapobject.v1.RequestHeaderType;

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
 *         &lt;element name="RequestHeader" type="{http://soa.htsc.com/BaseSoapObject/v1}RequestHeaderType"/>
 *         &lt;element name="RequestBody">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="startRowNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "", propOrder = { "requestHeader", "requestBody" })
@XmlRootElement(name = "QueryInvestmentCourtAccountReq")
public class QueryInvestmentCourtAccountReq {

	@XmlElement(name = "RequestHeader", required = true)
	protected RequestHeaderType requestHeader;
	@XmlElement(name = "RequestBody", required = true)
	protected QueryInvestmentCourtAccountReq.RequestBody requestBody;

	/**
	 * Gets the value of the requestHeader property.
	 * 
	 * @return possible object is {@link RequestHeaderType }
	 * 
	 */
	public RequestHeaderType getRequestHeader() {
		return requestHeader;
	}

	/**
	 * Sets the value of the requestHeader property.
	 * 
	 * @param value
	 *            allowed object is {@link RequestHeaderType }
	 * 
	 */
	public void setRequestHeader(RequestHeaderType value) {
		this.requestHeader = value;
	}

	/**
	 * Gets the value of the requestBody property.
	 * 
	 * @return possible object is
	 *         {@link QueryInvestmentCourtAccountReq.RequestBody }
	 * 
	 */
	public QueryInvestmentCourtAccountReq.RequestBody getRequestBody() {
		return requestBody;
	}

	/**
	 * Sets the value of the requestBody property.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link QueryInvestmentCourtAccountReq.RequestBody }
	 * 
	 */
	public void setRequestBody(QueryInvestmentCourtAccountReq.RequestBody value) {
		this.requestBody = value;
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
	 *         &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="startRowNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *       &lt;/sequence>
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "accountName", "startRowNum" })
	public static class RequestBody {

		@XmlElement(required = true)
		protected String accountName;
		@XmlElement(required = true)
		protected String startRowNum;

		/**
		 * Gets the value of the accountName property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getAccountName() {
			return accountName;
		}

		/**
		 * Sets the value of the accountName property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setAccountName(String value) {
			this.accountName = value;
		}

		/**
		 * Gets the value of the startRowNum property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getStartRowNum() {
			return startRowNum;
		}

		/**
		 * Sets the value of the startRowNum property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setStartRowNum(String value) {
			this.startRowNum = value;
		}

	}

}
