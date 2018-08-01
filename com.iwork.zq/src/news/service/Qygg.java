
package news.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Qygg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Qygg">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="qyid" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="xb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="xh" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="xm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zw" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Qygg", propOrder = {
    "id",
    "qyid",
    "xb",
    "xh",
    "xm",
    "zw"
})
public class Qygg {

    protected Integer id;
    protected Integer qyid;
    @XmlElementRef(name = "xb", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<String> xb;
    protected Integer xh;
    @XmlElementRef(name = "xm", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<String> xm;
    @XmlElementRef(name = "zw", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<String> zw;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the qyid property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getQyid() {
        return qyid;
    }

    /**
     * Sets the value of the qyid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setQyid(Integer value) {
        this.qyid = value;
    }

    /**
     * Gets the value of the xb property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getXb() {
        return xb;
    }

    /**
     * Sets the value of the xb property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setXb(JAXBElement<String> value) {
        this.xb = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the xh property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getXh() {
        return xh;
    }

    /**
     * Sets the value of the xh property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setXh(Integer value) {
        this.xh = value;
    }

    /**
     * Gets the value of the xm property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getXm() {
        return xm;
    }

    /**
     * Sets the value of the xm property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setXm(JAXBElement<String> value) {
        this.xm = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the zw property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getZw() {
        return zw;
    }

    /**
     * Sets the value of the zw property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setZw(JAXBElement<String> value) {
        this.zw = ((JAXBElement<String> ) value);
    }

}
