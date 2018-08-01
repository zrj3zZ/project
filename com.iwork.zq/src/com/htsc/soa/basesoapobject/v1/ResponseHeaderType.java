package com.htsc.soa.basesoapobject.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ResponseHeaderType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReqNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ServiceId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BusinessSvcCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BusinessSvcMsg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BusinessSvcStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrgId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EmpId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SysId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UserToken" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TranTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExtHead" type="{http://soa.htsc.com/BaseSoapObject/v1}OtherAttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseHeaderType", propOrder = { "reqNo", "serviceId",
		"businessSvcCode", "businessSvcMsg", "businessSvcStatus", "orgId",
		"empId", "sysId", "userToken", "tranTime", "extHead" })
public class ResponseHeaderType {

	@XmlElement(name = "ReqNo", required = true)
	protected String reqNo;
	@XmlElement(name = "ServiceId", required = true)
	protected String serviceId;
	@XmlElement(name = "BusinessSvcCode", required = true)
	protected String businessSvcCode;
	@XmlElement(name = "BusinessSvcMsg", required = true)
	protected String businessSvcMsg;
	@XmlElement(name = "BusinessSvcStatus", required = true)
	protected String businessSvcStatus;
	@XmlElement(name = "OrgId", required = true)
	protected String orgId;
	@XmlElement(name = "EmpId", required = true)
	protected String empId;
	@XmlElement(name = "SysId", required = true)
	protected String sysId;
	@XmlElement(name = "UserToken", required = true)
	protected String userToken;
	@XmlElement(name = "TranTime", required = true)
	protected String tranTime;
	@XmlElement(name = "ExtHead")
	protected List<OtherAttributeType> extHead;

	/**
	 * Gets the value of the reqNo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getReqNo() {
		return reqNo;
	}

	/**
	 * Sets the value of the reqNo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setReqNo(String value) {
		this.reqNo = value;
	}

	/**
	 * Gets the value of the serviceId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * Sets the value of the serviceId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setServiceId(String value) {
		this.serviceId = value;
	}

	/**
	 * Gets the value of the businessSvcCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBusinessSvcCode() {
		return businessSvcCode;
	}

	/**
	 * Sets the value of the businessSvcCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBusinessSvcCode(String value) {
		this.businessSvcCode = value;
	}

	/**
	 * Gets the value of the businessSvcMsg property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBusinessSvcMsg() {
		return businessSvcMsg;
	}

	/**
	 * Sets the value of the businessSvcMsg property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBusinessSvcMsg(String value) {
		this.businessSvcMsg = value;
	}

	/**
	 * Gets the value of the businessSvcStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBusinessSvcStatus() {
		return businessSvcStatus;
	}

	/**
	 * Sets the value of the businessSvcStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBusinessSvcStatus(String value) {
		this.businessSvcStatus = value;
	}

	/**
	 * Gets the value of the orgId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * Sets the value of the orgId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrgId(String value) {
		this.orgId = value;
	}

	/**
	 * Gets the value of the empId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEmpId() {
		return empId;
	}

	/**
	 * Sets the value of the empId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEmpId(String value) {
		this.empId = value;
	}

	/**
	 * Gets the value of the sysId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSysId() {
		return sysId;
	}

	/**
	 * Sets the value of the sysId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSysId(String value) {
		this.sysId = value;
	}

	/**
	 * Gets the value of the userToken property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserToken() {
		return userToken;
	}

	/**
	 * Sets the value of the userToken property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserToken(String value) {
		this.userToken = value;
	}

	/**
	 * Gets the value of the tranTime property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTranTime() {
		return tranTime;
	}

	/**
	 * Sets the value of the tranTime property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTranTime(String value) {
		this.tranTime = value;
	}

	/**
	 * Gets the value of the extHead property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the extHead property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getExtHead().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OtherAttributeType }
	 * 
	 * 
	 */
	public List<OtherAttributeType> getExtHead() {
		if (extHead == null) {
			extHead = new ArrayList<OtherAttributeType>();
		}
		return this.extHead;
	}

}
