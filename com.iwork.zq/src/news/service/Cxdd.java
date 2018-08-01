package news.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Cxdd complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Cxdd">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gfdm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gsmc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nature" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="times" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cxdd", propOrder = { "content", "gfdm", "gsmc", "href", "id",
		"nature", "source", "times", "title", "type" })
public class Cxdd {

	@XmlElementRef(name = "content", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> content;
	@XmlElementRef(name = "gfdm", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> gfdm;
	@XmlElementRef(name = "gsmc", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> gsmc;
	@XmlElementRef(name = "href", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> href;
	@XmlElementRef(name = "id", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<Integer> id;
	@XmlElementRef(name = "nature", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> nature;
	@XmlElementRef(name = "source", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> source;
	@XmlElementRef(name = "times", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> times;
	@XmlElementRef(name = "title", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> title;
	@XmlElementRef(name = "type", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> type;

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
	 * Gets the value of the gsmc property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getGsmc() {
		return gsmc;
	}

	/**
	 * Sets the value of the gsmc property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setGsmc(JAXBElement<String> value) {
		this.gsmc = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the href property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getHref() {
		return href;
	}

	/**
	 * Sets the value of the href property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setHref(JAXBElement<String> value) {
		this.href = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link Integer }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<Integer> getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link Integer }
	 *            {@code >}
	 * 
	 */
	public void setId(JAXBElement<Integer> value) {
		this.id = ((JAXBElement<Integer>) value);
	}

	/**
	 * Gets the value of the nature property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getNature() {
		return nature;
	}

	/**
	 * Sets the value of the nature property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setNature(JAXBElement<String> value) {
		this.nature = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the source property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getSource() {
		return source;
	}

	/**
	 * Sets the value of the source property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setSource(JAXBElement<String> value) {
		this.source = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the times property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getTimes() {
		return times;
	}

	/**
	 * Sets the value of the times property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setTimes(JAXBElement<String> value) {
		this.times = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the title property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getTitle() {
		return title;
	}

	/**
	 * Sets the value of the title property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setTitle(JAXBElement<String> value) {
		this.title = ((JAXBElement<String>) value);
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String }
	 *         {@code >}
	 * 
	 */
	public JAXBElement<String> getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String }
	 *            {@code >}
	 * 
	 */
	public void setType(JAXBElement<String> value) {
		this.type = ((JAXBElement<String>) value);
	}

}
