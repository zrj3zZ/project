
package news.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfQyjbxx complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfQyjbxx">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Qyjbxx" type="{http://service.com}Qyjbxx" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfQyjbxx", propOrder = {
    "qyjbxx"
})
public class ArrayOfQyjbxx {

    @XmlElement(name = "Qyjbxx", nillable = true)
    protected List<Qyjbxx> qyjbxx;

    /**
     * Gets the value of the qyjbxx property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the qyjbxx property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQyjbxx().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Qyjbxx }
     * 
     * 
     */
    public List<Qyjbxx> getQyjbxx() {
        if (qyjbxx == null) {
            qyjbxx = new ArrayList<Qyjbxx>();
        }
        return this.qyjbxx;
    }

}
