package org.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name="UserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AfterUAndP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="phoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "userName", "password", "afterUAndP",
		"phoneNumber", "content" })
@XmlRootElement(name = "SMSSend")
public class SMSSend {

	@XmlElementRef(name = "UserName", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> userName;
	@XmlElementRef(name = "Password", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> password;
	@XmlElementRef(name = "AfterUAndP", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> afterUAndP;
	@XmlElementRef(name = "phoneNumber", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> phoneNumber;
	@XmlElementRef(name = "content", namespace = "http://tempuri.org/", type = JAXBElement.class)
	protected JAXBElement<String> content;

	/**
	 * Gets the value of the userName property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getUserName() {
		return userName;
	}

	/**
	 * Sets the value of the userName property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setUserName(JAXBElement<String> value) {
		this.userName = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the password property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getPassword() {
		return password;
	}

	/**
	 * Sets the value of the password property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setPassword(JAXBElement<String> value) {
		this.password = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the afterUAndP property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getAfterUAndP() {
		return afterUAndP;
	}

	/**
	 * Sets the value of the afterUAndP property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setAfterUAndP(JAXBElement<String> value) {
		this.afterUAndP = ((JAXBElement<String>) value);
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

}
