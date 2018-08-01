package news.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Comnotice complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Comnotice">
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
@XmlType(name = "Comnotice", propOrder = { "id","gsdm","noticename","noticetype","noticedate","noticepublishdate","noticeurl","adddate"})
public class Comnotice {

	@XmlElementRef(name = "id", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<Integer> id;
	@XmlElementRef(name = "gsdm", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<Integer> gsdm;
	@XmlElementRef(name = "noticename", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> noticename;
	@XmlElementRef(name = "noticetype", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> noticetype;
	@XmlElementRef(name = "noticedate", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> noticedate;
	@XmlElementRef(name = "noticepublishdate", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> noticepublishdate;
	@XmlElementRef(name = "noticeurl", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> noticeurl;
	@XmlElementRef(name = "adddate", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> adddate;

	public JAXBElement<Integer> getId() {
		return id;
	}
	
	public void setId(JAXBElement<Integer> value) {
		this.id = ((JAXBElement<Integer>) value);
	}

	public JAXBElement<Integer> getGsdm() {
		return gsdm;
	}

	public void setGsdm(JAXBElement<Integer> value) {
		this.gsdm = ((JAXBElement<Integer>) value);
	}

	public JAXBElement<String> getNoticename() {
		return noticename;
	}

	public void setNoticename(JAXBElement<String> value) {
		this.noticename = ((JAXBElement<String>) value);
	}

	public JAXBElement<String> getNoticetype() {
		return noticetype;
	}

	public void setNoticetype(JAXBElement<String> value) {
		this.noticetype = ((JAXBElement<String>) value);
	}

	public JAXBElement<String> getNoticedate() {
		return noticedate;
	}

	public void setNoticedate(JAXBElement<String> value) {
		this.noticedate = ((JAXBElement<String>) value);
	}

	public JAXBElement<String> getNoticepublishdate() {
		return noticepublishdate;
	}

	public void setNoticepublishdate(JAXBElement<String> value) {
		this.noticepublishdate = ((JAXBElement<String>) value);
	}

	public JAXBElement<String> getNoticeurl() {
		return noticeurl;
	}

	public void setNoticeurl(JAXBElement<String> value) {
		this.noticeurl = ((JAXBElement<String>) value);
	}

	public JAXBElement<String> getAdddate() {
		return adddate;
	}

	public void setAdddate(JAXBElement<String> value) {
		this.adddate = ((JAXBElement<String>) value);
	}
	
	
}
