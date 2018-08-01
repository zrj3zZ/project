package org.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

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
 *         &lt;element name="sn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="phoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AfterMMAndCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="ips" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sn", "pwd", "phoneNumber", "content",
		"code", "afterMMAndCode", "datetime", "ips" })
@XmlRootElement(name = "SPSend")
public class SPSend {

	@XmlElementRef(name = "sn", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> sn;
	@XmlElementRef(name = "pwd", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> pwd;
	@XmlElementRef(name = "phoneNumber", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> phoneNumber;
	@XmlElementRef(name = "content", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> content;
	@XmlElementRef(name = "Code", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> code;
	@XmlElementRef(name = "AfterMMAndCode", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> afterMMAndCode;
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar datetime;
	@XmlElementRef(name = "ips", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> ips;

	/**
	 * Gets the value of the sn property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getSn() {
		return sn;
	}

	/**
	 * Sets the value of the sn property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setSn(JAXBElement<String> value) {
		this.sn = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the pwd property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getPwd() {
		return pwd;
	}

	/**
	 * Sets the value of the pwd property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setPwd(JAXBElement<String> value) {
		this.pwd = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the phoneNumber property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the value of the phoneNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setPhoneNumber(JAXBElement<String> value) {
		this.phoneNumber = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the content property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getContent() {
		return content;
	}

	/**
	 * Sets the value of the content property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setContent(JAXBElement<String> value) {
		this.content = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the code property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getCode() {
		return code;
	}

	/**
	 * Sets the value of the code property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setCode(JAXBElement<String> value) {
		this.code = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the afterMMAndCode property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getAfterMMAndCode() {
		return afterMMAndCode;
	}

	/**
	 * Sets the value of the afterMMAndCode property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setAfterMMAndCode(JAXBElement<String> value) {
		this.afterMMAndCode = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the datetime property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDatetime() {
		return datetime;
	}

	/**
	 * Sets the value of the datetime property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDatetime(XMLGregorianCalendar value) {
		this.datetime = value;
	}

	/**
	 * Gets the value of the ips property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getIps() {
		return ips;
	}

	/**
	 * Sets the value of the ips property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setIps(JAXBElement<String> value) {
		this.ips = ((JAXBElement<String>) value);
	}

}
