package com.htsc.soa.custmanage.v10.investmentcourtaccounttype;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for InvestmentCourtAccountType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="InvestmentCourtAccountType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="officeAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="regState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rowId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contactName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mainBusiness" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mainProduct" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="superiority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="annualReceivable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="netProfit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="profitModel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contactMobilePhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contactEmai" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="history" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listingDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="foundedDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="limitedFoundedDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="securitiesCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="regAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="industry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="englishName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registeredCapital" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="freightTermsInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="websiteAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="legalContact" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mainPhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mainFaxNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="regCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="majorityShareholder" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ratio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listedCompanyFlag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvestmentCourtAccountType", propOrder = { "officeAddress",
		"regState", "rowId", "accountName", "contactName", "mainBusiness",
		"mainProduct", "superiority", "annualReceivable", "netProfit",
		"profitModel", "contactMobilePhone", "contactEmai", "history",
		"listingDate", "foundedDate", "limitedFoundedDate", "comments",
		"description", "securitiesCode", "regAddress", "shortName", "industry",
		"englishName", "registeredCapital", "freightTermsInfo",
		"websiteAddress", "legalContact", "mainPhoneNumber", "mainFaxNumber",
		"regCity", "postalCode", "majorityShareholder", "ratio",
		"listedCompanyFlag" })
public class InvestmentCourtAccountType {

	protected String officeAddress;
	protected String regState;
	protected String rowId;
	protected String accountName;
	protected String contactName;
	protected String mainBusiness;
	protected String mainProduct;
	protected String superiority;
	protected String annualReceivable;
	protected String netProfit;
	protected String profitModel;
	protected String contactMobilePhone;
	protected String contactEmai;
	protected String history;
	protected String listingDate;
	protected String foundedDate;
	protected String limitedFoundedDate;
	protected String comments;
	protected String description;
	protected String securitiesCode;
	protected String regAddress;
	protected String shortName;
	protected String industry;
	protected String englishName;
	protected String registeredCapital;
	protected String freightTermsInfo;
	protected String websiteAddress;
	protected String legalContact;
	protected String mainPhoneNumber;
	protected String mainFaxNumber;
	protected String regCity;
	protected String postalCode;
	protected String majorityShareholder;
	protected String ratio;
	protected String listedCompanyFlag;

	/**
	 * Gets the value of the officeAddress property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOfficeAddress() {
		return officeAddress;
	}

	/**
	 * Sets the value of the officeAddress property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOfficeAddress(String value) {
		this.officeAddress = value;
	}

	/**
	 * Gets the value of the regState property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRegState() {
		return regState;
	}

	/**
	 * Sets the value of the regState property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRegState(String value) {
		this.regState = value;
	}

	/**
	 * Gets the value of the rowId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRowId() {
		return rowId;
	}

	/**
	 * Sets the value of the rowId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRowId(String value) {
		this.rowId = value;
	}

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
	 * Gets the value of the contactName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * Sets the value of the contactName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setContactName(String value) {
		this.contactName = value;
	}

	/**
	 * Gets the value of the mainBusiness property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMainBusiness() {
		return mainBusiness;
	}

	/**
	 * Sets the value of the mainBusiness property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMainBusiness(String value) {
		this.mainBusiness = value;
	}

	/**
	 * Gets the value of the mainProduct property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMainProduct() {
		return mainProduct;
	}

	/**
	 * Sets the value of the mainProduct property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMainProduct(String value) {
		this.mainProduct = value;
	}

	/**
	 * Gets the value of the superiority property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSuperiority() {
		return superiority;
	}

	/**
	 * Sets the value of the superiority property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSuperiority(String value) {
		this.superiority = value;
	}

	/**
	 * Gets the value of the annualReceivable property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAnnualReceivable() {
		return annualReceivable;
	}

	/**
	 * Sets the value of the annualReceivable property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAnnualReceivable(String value) {
		this.annualReceivable = value;
	}

	/**
	 * Gets the value of the netProfit property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNetProfit() {
		return netProfit;
	}

	/**
	 * Sets the value of the netProfit property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNetProfit(String value) {
		this.netProfit = value;
	}

	/**
	 * Gets the value of the profitModel property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProfitModel() {
		return profitModel;
	}

	/**
	 * Sets the value of the profitModel property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setProfitModel(String value) {
		this.profitModel = value;
	}

	/**
	 * Gets the value of the contactMobilePhone property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getContactMobilePhone() {
		return contactMobilePhone;
	}

	/**
	 * Sets the value of the contactMobilePhone property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setContactMobilePhone(String value) {
		this.contactMobilePhone = value;
	}

	/**
	 * Gets the value of the contactEmai property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getContactEmai() {
		return contactEmai;
	}

	/**
	 * Sets the value of the contactEmai property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setContactEmai(String value) {
		this.contactEmai = value;
	}

	/**
	 * Gets the value of the history property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getHistory() {
		return history;
	}

	/**
	 * Sets the value of the history property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setHistory(String value) {
		this.history = value;
	}

	/**
	 * Gets the value of the listingDate property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getListingDate() {
		return listingDate;
	}

	/**
	 * Sets the value of the listingDate property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setListingDate(String value) {
		this.listingDate = value;
	}

	/**
	 * Gets the value of the foundedDate property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFoundedDate() {
		return foundedDate;
	}

	/**
	 * Sets the value of the foundedDate property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFoundedDate(String value) {
		this.foundedDate = value;
	}

	/**
	 * Gets the value of the limitedFoundedDate property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLimitedFoundedDate() {
		return limitedFoundedDate;
	}

	/**
	 * Sets the value of the limitedFoundedDate property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLimitedFoundedDate(String value) {
		this.limitedFoundedDate = value;
	}

	/**
	 * Gets the value of the comments property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Sets the value of the comments property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setComments(String value) {
		this.comments = value;
	}

	/**
	 * Gets the value of the description property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescription(String value) {
		this.description = value;
	}

	/**
	 * Gets the value of the securitiesCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSecuritiesCode() {
		return securitiesCode;
	}

	/**
	 * Sets the value of the securitiesCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSecuritiesCode(String value) {
		this.securitiesCode = value;
	}

	/**
	 * Gets the value of the regAddress property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRegAddress() {
		return regAddress;
	}

	/**
	 * Sets the value of the regAddress property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRegAddress(String value) {
		this.regAddress = value;
	}

	/**
	 * Gets the value of the shortName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Sets the value of the shortName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShortName(String value) {
		this.shortName = value;
	}

	/**
	 * Gets the value of the industry property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIndustry() {
		return industry;
	}

	/**
	 * Sets the value of the industry property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIndustry(String value) {
		this.industry = value;
	}

	/**
	 * Gets the value of the englishName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEnglishName() {
		return englishName;
	}

	/**
	 * Sets the value of the englishName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEnglishName(String value) {
		this.englishName = value;
	}

	/**
	 * Gets the value of the registeredCapital property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRegisteredCapital() {
		return registeredCapital;
	}

	/**
	 * Sets the value of the registeredCapital property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRegisteredCapital(String value) {
		this.registeredCapital = value;
	}

	/**
	 * Gets the value of the freightTermsInfo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFreightTermsInfo() {
		return freightTermsInfo;
	}

	/**
	 * Sets the value of the freightTermsInfo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFreightTermsInfo(String value) {
		this.freightTermsInfo = value;
	}

	/**
	 * Gets the value of the websiteAddress property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getWebsiteAddress() {
		return websiteAddress;
	}

	/**
	 * Sets the value of the websiteAddress property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setWebsiteAddress(String value) {
		this.websiteAddress = value;
	}

	/**
	 * Gets the value of the legalContact property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLegalContact() {
		return legalContact;
	}

	/**
	 * Sets the value of the legalContact property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLegalContact(String value) {
		this.legalContact = value;
	}

	/**
	 * Gets the value of the mainPhoneNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMainPhoneNumber() {
		return mainPhoneNumber;
	}

	/**
	 * Sets the value of the mainPhoneNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMainPhoneNumber(String value) {
		this.mainPhoneNumber = value;
	}

	/**
	 * Gets the value of the mainFaxNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMainFaxNumber() {
		return mainFaxNumber;
	}

	/**
	 * Sets the value of the mainFaxNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMainFaxNumber(String value) {
		this.mainFaxNumber = value;
	}

	/**
	 * Gets the value of the regCity property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRegCity() {
		return regCity;
	}

	/**
	 * Sets the value of the regCity property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRegCity(String value) {
		this.regCity = value;
	}

	/**
	 * Gets the value of the postalCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * Sets the value of the postalCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPostalCode(String value) {
		this.postalCode = value;
	}

	/**
	 * Gets the value of the majorityShareholder property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMajorityShareholder() {
		return majorityShareholder;
	}

	/**
	 * Sets the value of the majorityShareholder property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMajorityShareholder(String value) {
		this.majorityShareholder = value;
	}

	/**
	 * Gets the value of the ratio property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRatio() {
		return ratio;
	}

	/**
	 * Sets the value of the ratio property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRatio(String value) {
		this.ratio = value;
	}

	/**
	 * Gets the value of the listedCompanyFlag property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getListedCompanyFlag() {
		return listedCompanyFlag;
	}

	/**
	 * Sets the value of the listedCompanyFlag property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setListedCompanyFlag(String value) {
		this.listedCompanyFlag = value;
	}

}
