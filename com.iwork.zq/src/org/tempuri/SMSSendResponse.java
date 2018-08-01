package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="SMSSendResult" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "smsSendResult" })
@XmlRootElement(name = "SMSSendResponse")
public class SMSSendResponse {

	@XmlElement(name = "SMSSendResult")
	protected Integer smsSendResult;

	/**
	 * Gets the value of the smsSendResult property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getSMSSendResult() {
		return smsSendResult;
	}

	/**
	 * Sets the value of the smsSendResult property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setSMSSendResult(Integer value) {
		this.smsSendResult = value;
	}

}
