package com.htsc.soa.basesoapobject.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OtherAttributeType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OtherAttributeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AttributeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AttributeValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OtherAttributeType", propOrder = { "attributeName",
		"attributeValue" })
public class OtherAttributeType {

	@XmlElement(name = "AttributeName", required = true)
	protected String attributeName;
	@XmlElement(name = "AttributeValue", required = true)
	protected String attributeValue;

	/**
	 * Gets the value of the attributeName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Sets the value of the attributeName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAttributeName(String value) {
		this.attributeName = value;
	}

	/**
	 * Gets the value of the attributeValue property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * Sets the value of the attributeValue property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAttributeValue(String value) {
		this.attributeValue = value;
	}

}
