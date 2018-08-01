
package news.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfSsxx complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSsxx">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Ssxx" type="{http://service.com}Ssxx" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSsxx", propOrder = {
    "ssxx"
})
public class ArrayOfSsxx {

    @XmlElement(name = "Ssxx", nillable = true)
    protected List<Ssxx> ssxx;

    /**
     * Gets the value of the ssxx property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ssxx property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSsxx().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ssxx }
     * 
     * 
     */
    public List<Ssxx> getSsxx() {
        if (ssxx == null) {
            ssxx = new ArrayList<Ssxx>();
        }
        return this.ssxx;
    }

}
