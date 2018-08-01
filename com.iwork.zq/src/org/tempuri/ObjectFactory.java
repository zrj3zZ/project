package org.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.datacontract.schemas._2004._07.smswcfservice.CompositeType;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.tempuri package.
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

	private final static QName _GetDataResponseGetDataResult_QNAME = new QName(
			"http://tempuri.org/", "GetDataResult");
	private final static QName _GetDataUsingDataContractComposite_QNAME = new QName(
			"http://tempuri.org/", "composite");
	private final static QName _SMSSendPassword_QNAME = new QName(
			"http://tempuri.org/", "Password");
	private final static QName _SMSSendPhoneNumber_QNAME = new QName(
			"http://tempuri.org/", "phoneNumber");
	private final static QName _SMSSendContent_QNAME = new QName(
			"http://tempuri.org/", "content");
	private final static QName _SMSSendAfterUAndP_QNAME = new QName(
			"http://tempuri.org/", "AfterUAndP");
	private final static QName _SMSSendUserName_QNAME = new QName(
			"http://tempuri.org/", "UserName");
	private final static QName _GetDataUsingDataContractResponseGetDataUsingDataContractResult_QNAME = new QName(
			"http://tempuri.org/", "GetDataUsingDataContractResult");
	private final static QName _SPSendIps_QNAME = new QName(
			"http://tempuri.org/", "ips");
	private final static QName _SPSendPwd_QNAME = new QName(
			"http://tempuri.org/", "pwd");
	private final static QName _SPSendSn_QNAME = new QName(
			"http://tempuri.org/", "sn");
	private final static QName _SPSendCode_QNAME = new QName(
			"http://tempuri.org/", "Code");
	private final static QName _SPSendAfterMMAndCode_QNAME = new QName(
			"http://tempuri.org/", "AfterMMAndCode");
	private final static QName _SPSendResponseSPSendResult_QNAME = new QName(
			"http://tempuri.org/", "SPSendResult");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: org.tempuri
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link GetDataResponse }
	 * 
	 */
	public GetDataResponse createGetDataResponse() {
		return new GetDataResponse();
	}

	/**
	 * Create an instance of {@link GetDataUsingDataContract }
	 * 
	 */
	public GetDataUsingDataContract createGetDataUsingDataContract() {
		return new GetDataUsingDataContract();
	}

	/**
	 * Create an instance of {@link GetData }
	 * 
	 */
	public GetData createGetData() {
		return new GetData();
	}

	/**
	 * Create an instance of {@link SMSSend }
	 * 
	 */
	public SMSSend createSMSSend() {
		return new SMSSend();
	}

	/**
	 * Create an instance of {@link GetDataUsingDataContractResponse }
	 * 
	 */
	public GetDataUsingDataContractResponse createGetDataUsingDataContractResponse() {
		return new GetDataUsingDataContractResponse();
	}

	/**
	 * Create an instance of {@link SPSend }
	 * 
	 */
	public SPSend createSPSend() {
		return new SPSend();
	}

	/**
	 * Create an instance of {@link SMSSendResponse }
	 * 
	 */
	public SMSSendResponse createSMSSendResponse() {
		return new SMSSendResponse();
	}

	/**
	 * Create an instance of {@link SPSendResponse }
	 * 
	 */
	public SPSendResponse createSPSendResponse() {
		return new SPSendResponse();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "GetDataResult", scope = GetDataResponse.class)
	public JAXBElement<String> createGetDataResponseGetDataResult(String value) {
		return new JAXBElement<String>(_GetDataResponseGetDataResult_QNAME,
				String.class, GetDataResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link CompositeType }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "composite", scope = GetDataUsingDataContract.class)
	public JAXBElement<CompositeType> createGetDataUsingDataContractComposite(
			CompositeType value) {
		return new JAXBElement<CompositeType>(
				_GetDataUsingDataContractComposite_QNAME, CompositeType.class,
				GetDataUsingDataContract.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "Password", scope = SMSSend.class)
	public JAXBElement<String> createSMSSendPassword(String value) {
		return new JAXBElement<String>(_SMSSendPassword_QNAME, String.class,
				SMSSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "phoneNumber", scope = SMSSend.class)
	public JAXBElement<String> createSMSSendPhoneNumber(String value) {
		return new JAXBElement<String>(_SMSSendPhoneNumber_QNAME, String.class,
				SMSSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "content", scope = SMSSend.class)
	public JAXBElement<String> createSMSSendContent(String value) {
		return new JAXBElement<String>(_SMSSendContent_QNAME, String.class,
				SMSSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "AfterUAndP", scope = SMSSend.class)
	public JAXBElement<String> createSMSSendAfterUAndP(String value) {
		return new JAXBElement<String>(_SMSSendAfterUAndP_QNAME, String.class,
				SMSSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "UserName", scope = SMSSend.class)
	public JAXBElement<String> createSMSSendUserName(String value) {
		return new JAXBElement<String>(_SMSSendUserName_QNAME, String.class,
				SMSSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link CompositeType }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "GetDataUsingDataContractResult", scope = GetDataUsingDataContractResponse.class)
	public JAXBElement<CompositeType> createGetDataUsingDataContractResponseGetDataUsingDataContractResult(
			CompositeType value) {
		return new JAXBElement<CompositeType>(
				_GetDataUsingDataContractResponseGetDataUsingDataContractResult_QNAME,
				CompositeType.class, GetDataUsingDataContractResponse.class,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "ips", scope = SPSend.class)
	public JAXBElement<String> createSPSendIps(String value) {
		return new JAXBElement<String>(_SPSendIps_QNAME, String.class,
				SPSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "phoneNumber", scope = SPSend.class)
	public JAXBElement<String> createSPSendPhoneNumber(String value) {
		return new JAXBElement<String>(_SMSSendPhoneNumber_QNAME, String.class,
				SPSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = SPSend.class)
	public JAXBElement<String> createSPSendPwd(String value) {
		return new JAXBElement<String>(_SPSendPwd_QNAME, String.class,
				SPSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "sn", scope = SPSend.class)
	public JAXBElement<String> createSPSendSn(String value) {
		return new JAXBElement<String>(_SPSendSn_QNAME, String.class,
				SPSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "content", scope = SPSend.class)
	public JAXBElement<String> createSPSendContent(String value) {
		return new JAXBElement<String>(_SMSSendContent_QNAME, String.class,
				SPSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "Code", scope = SPSend.class)
	public JAXBElement<String> createSPSendCode(String value) {
		return new JAXBElement<String>(_SPSendCode_QNAME, String.class,
				SPSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "AfterMMAndCode", scope = SPSend.class)
	public JAXBElement<String> createSPSendAfterMMAndCode(String value) {
		return new JAXBElement<String>(_SPSendAfterMMAndCode_QNAME,
				String.class, SPSend.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "SPSendResult", scope = SPSendResponse.class)
	public JAXBElement<String> createSPSendResponseSPSendResult(String value) {
		return new JAXBElement<String>(_SPSendResponseSPSendResult_QNAME,
				String.class, SPSendResponse.class, value);
	}

}
