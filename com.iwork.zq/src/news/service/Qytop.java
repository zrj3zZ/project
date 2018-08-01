
package news.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Qytop complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Qytop">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cgbl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cgs" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="gdxm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gfxz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="qyid" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="xh" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Qytop", propOrder = {
    "cgbl",
    "cgs",
    "gdxm",
    "gfxz",
    "id",
    "qyid",
    "xh"
})
public class Qytop {

    @XmlElementRef(name = "cgbl", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<String> cgbl;
    protected Integer cgs;
    @XmlElementRef(name = "gdxm", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<String> gdxm;
    @XmlElementRef(name = "gfxz", namespace = "http://service.com", type = JAXBElement.class)
    protected JAXBElement<String> gfxz;
    protected Integer id;
    protected Integer qyid;
    protected Integer xh;

    /**
     * Gets the value of the cgbl property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCgbl() {
        return cgbl;
    }

    /**
     * Sets the value of the cgbl property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCgbl(JAXBElement<String> value) {
        this.cgbl = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the cgs property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCgs() {
        return cgs;
    }

    /**
     * Sets the value of the cgs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCgs(Integer value) {
        this.cgs = value;
    }

    /**
     * Gets the value of the gdxm property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGdxm() {
        return gdxm;
    }

    /**
     * Sets the value of the gdxm property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGdxm(JAXBElement<String> value) {
        this.gdxm = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the gfxz property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGfxz() {
        return gfxz;
    }

    /**
     * Sets the value of the gfxz property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGfxz(JAXBElement<String> value) {
        this.gfxz = ((JAXBElement<String> ) value);
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

}
