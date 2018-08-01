package news.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for Qysjk complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Qysjk">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gfdm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gprq" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="gsqc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="qyid" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="qyjc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zbqs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zczj" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="zyyw" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Qysjk", propOrder = { "fr", "gfdm", "gprq", "gsqc", "id",
		"qyid", "qyjc", "sf", "zbqs", "zczj", "zyyw" })
public class Qysjk {

	@XmlElementRef(name = "fr", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> fr;
	@XmlElementRef(name = "gfdm", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> gfdm;
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar gprq;
	@XmlElementRef(name = "gsqc", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> gsqc;
	protected Integer id;
	protected Integer qyid;
	@XmlElementRef(name = "qyjc", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> qyjc;
	@XmlElementRef(name = "sf", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> sf;
	@XmlElementRef(name = "zbqs", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zbqs;
	@XmlElementRef(name = "zczj", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<Double> zczj;
	@XmlElementRef(name = "zyyw", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zyyw;

	/**
	 * Gets the value of the fr property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getFr() {
		return fr;
	}

	/**
	 * Sets the value of the fr property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setFr(JAXBElement<String> value) {
		this.fr = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the gfdm property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getGfdm() {
		return gfdm;
	}

	/**
	 * Sets the value of the gfdm property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setGfdm(JAXBElement<String> value) {
		this.gfdm = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the gprq property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getGprq() {
		return gprq;
	}

	/**
	 * Sets the value of the gprq property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setGprq(XMLGregorianCalendar value) {
		this.gprq = value;
	}

	/**
	 * Gets the value of the gsqc property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getGsqc() {
		return gsqc;
	}

	/**
	 * Sets the value of the gsqc property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setGsqc(JAXBElement<String> value) {
		this.gsqc = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setId(Integer value) {
		this.id = value;
	}

	/**
	 * Gets the value of the qyid property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getQyid() {
		return qyid;
	}

	/**
	 * Sets the value of the qyid property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setQyid(Integer value) {
		this.qyid = value;
	}

	/**
	 * Gets the value of the qyjc property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getQyjc() {
		return qyjc;
	}

	/**
	 * Sets the value of the qyjc property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setQyjc(JAXBElement<String> value) {
		this.qyjc = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the sf property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getSf() {
		return sf;
	}

	/**
	 * Sets the value of the sf property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setSf(JAXBElement<String> value) {
		this.sf = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the zbqs property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getZbqs() {
		return zbqs;
	}

	/**
	 * Sets the value of the zbqs property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setZbqs(JAXBElement<String> value) {
		this.zbqs = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the zczj property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link Double }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<Double> getZczj() {
		return zczj;
	}

	/**
	 * Sets the value of the zczj property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link Double }
	 *            {@code >}
	 * 
	 */
	public void setZczj(JAXBElement<Double> value) {
		this.zczj = ((JAXBElement<Double>) value);
	}

	/**
	 * Gets the value of the zyyw property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getZyyw() {
		return zyyw;
	}

	/**
	 * Sets the value of the zyyw property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setZyyw(JAXBElement<String> value) {
		this.zyyw = ((JAXBElement<String>) value);
	}

}
