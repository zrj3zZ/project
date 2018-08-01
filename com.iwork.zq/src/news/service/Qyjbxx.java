
package news.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Qyjbxx complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Qyjbxx">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gpdm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gprq" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="qyid" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="sshy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="szd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="yysr" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="zbqs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zczj" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Qyjbxx", propOrder = {
    "gpdm",
    "gprq",
    "id",
    "qyid",
    "sshy",
    "szd",
    "yysr",
    "zbqs",
    "zczj"
})
public class Qyjbxx {

    @XmlElementRef(name = "gpdm", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<String> gpdm;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar gprq;
    protected Integer id;
    protected Integer qyid;
    @XmlElementRef(name = "sshy", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<String> sshy;
    @XmlElementRef(name = "szd", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<String> szd;
    @XmlElementRef(name = "yysr", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<Double> yysr;
    @XmlElementRef(name = "zbqs", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<String> zbqs;
    @XmlElementRef(name = "zczj", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<Double> zczj;

    /**
     * Gets the value of the gpdm property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGpdm() {
        return gpdm;
    }

    /**
     * Sets the value of the gpdm property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGpdm(JAXBElement<String> value) {
        this.gpdm = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the gprq property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getGprq() {
        return gprq;
    }

    /**
     * Sets the value of the gprq property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGprq(XMLGregorianCalendar value) {
        this.gprq = value;
    }

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
     * Gets the value of the sshy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSshy() {
        return sshy;
    }

    /**
     * Sets the value of the sshy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSshy(JAXBElement<String> value) {
        this.sshy = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the szd property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSzd() {
        return szd;
    }

    /**
     * Sets the value of the szd property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSzd(JAXBElement<String> value) {
        this.szd = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the yysr property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public JAXBElement<Double> getYysr() {
        return yysr;
    }

    /**
     * Sets the value of the yysr property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public void setYysr(JAXBElement<Double> value) {
        this.yysr = ((JAXBElement<Double> ) value);
    }

    /**
     * Gets the value of the zbqs property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getZbqs() {
        return zbqs;
    }

    /**
     * Sets the value of the zbqs property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setZbqs(JAXBElement<String> value) {
        this.zbqs = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the zczj property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public JAXBElement<Double> getZczj() {
        return zczj;
    }

    /**
     * Sets the value of the zczj property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public void setZczj(JAXBElement<Double> value) {
        this.zczj = ((JAXBElement<Double> ) value);
    }

}
