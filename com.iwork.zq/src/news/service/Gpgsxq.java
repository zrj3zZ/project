package news.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Gpgsxq complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Gpgsxq">
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
@XmlType(name = "Gpgsxq", propOrder = { "id","gpgsid","gsqc","gsjc","gfdm","hyfl","zyyw","zbqs","gszt","sbsj","gpsj","zzgpsj","zzgpyy","fzqs","yzbqs","rksj","zqly","sf","cs","qx","bgdz","zczj","yysr","fr","zycp","jthyfl","gfgsrq","yxgsrq","wz","dh","cz","dzyx","jylx","zssl","dm","dmdh","dsz","dszdh","zjl","gsdjh","kjssws","lssws","zssj" })
public class Gpgsxq {

	@XmlElementRef(name = "id", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<Integer> id;
	@XmlElementRef(name = "gpgsid", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<Integer> gpgsid;
	@XmlElementRef(name = "gsqc", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> gsqc;
	@XmlElementRef(name = "gsjc", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> gsjc;
	@XmlElementRef(name = "gfdm", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> gfdm;
	@XmlElementRef(name = "hyfl", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> hyfl;
	@XmlElementRef(name = "zyyw", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zyyw;
	@XmlElementRef(name = "zbqs", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zbqs;
	@XmlElementRef(name = "gszt", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> gszt;
	@XmlElementRef(name = "zzgpyy", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zzgpyy;
	@XmlElementRef(name = "fzqs", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> fzqs;
	@XmlElementRef(name = "yzbqs", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> yzbqs;
	@XmlElementRef(name = "zqly", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zqly;
	@XmlElementRef(name = "sf", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> sf;
	@XmlElementRef(name = "cs", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> cs;
	@XmlElementRef(name = "qx", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> qx;
	@XmlElementRef(name = "bgdz", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> bgdz;
	@XmlElementRef(name = "zczj", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zczj;
	@XmlElementRef(name = "yysr", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> yysr;
	@XmlElementRef(name = "fr", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> fr;
	@XmlElementRef(name = "zycp", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zycp;
	@XmlElementRef(name = "jthyfl", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> jthyfl;
	@XmlElementRef(name = "wz", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> wz;
	@XmlElementRef(name = "dh", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> dh;
	@XmlElementRef(name = "cz", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> cz;
	@XmlElementRef(name = "dzyx", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> dzyx;
	@XmlElementRef(name = "jylx", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> jylx;
	@XmlElementRef(name = "zssl", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zssl;
	@XmlElementRef(name = "dm", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> dm;
	@XmlElementRef(name = "dmdh", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> dmdh;
	@XmlElementRef(name = "dsz", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> dsz;
	@XmlElementRef(name = "dszdh", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> dszdh;
	@XmlElementRef(name = "zjl", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zjl;
	@XmlElementRef(name = "gsdjh", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> gsdjh;
	@XmlElementRef(name = "kjssws", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> kjssws;
	@XmlElementRef(name = "lssws", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> lssws;
	@XmlElementRef(name = "sbsj", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> sbsj;
	@XmlElementRef(name = "gpsj", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> gpsj;
	@XmlElementRef(name = "zzgpsj", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zzgpsj;
	@XmlElementRef(name = "rksj", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> rksj;
	@XmlElementRef(name = "gfgsrq", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> gfgsrq;
	@XmlElementRef(name = "yxgsrq", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> yxgsrq;
	@XmlElementRef(name = "zssj", namespace = "http://service.com", type = JAXBElement.class)
	protected JAXBElement<String> zssj;

	public JAXBElement<Integer> getId() {
		return id;
	}
	
	public void setId(JAXBElement<Integer> value) {
		this.id = ((JAXBElement<Integer>) value);
	}

	public JAXBElement<Integer> getGpgsid() {
		return gpgsid;
	}

	public void setGpgsid(JAXBElement<Integer> gpgsid) {
		this.gpgsid = ((JAXBElement<Integer>) gpgsid);
	}

	public JAXBElement<String> getGsqc() {
		return gsqc;
	}

	public void setGsqc(JAXBElement<String> gsqc) {
		this.gsqc = ((JAXBElement<String>) gsqc);
	}

	public JAXBElement<String> getGsjc() {
		return gsjc;
	}

	public void setGsjc(JAXBElement<String> gsjc) {
		this.gsjc = ((JAXBElement<String>) gsjc);
	}

	public JAXBElement<String> getGfdm() {
		return gfdm;
	}

	public void setGfdm(JAXBElement<String> gfdm) {
		this.gfdm = ((JAXBElement<String>) gfdm);
	}

	public JAXBElement<String> getHyfl() {
		return hyfl;
	}

	public void setHyfl(JAXBElement<String> hyfl) {
		this.hyfl = ((JAXBElement<String>) hyfl);
	}

	public JAXBElement<String> getZyyw() {
		return zyyw;
	}

	public void setZyyw(JAXBElement<String> zyyw) {
		this.zyyw = ((JAXBElement<String>) zyyw);
	}

	public JAXBElement<String> getZbqs() {
		return zbqs;
	}

	public void setZbqs(JAXBElement<String> zbqs) {
		this.zbqs = ((JAXBElement<String>) zbqs);
	}

	public JAXBElement<String> getGszt() {
		return gszt;
	}

	public void setGszt(JAXBElement<String> gszt) {
		this.gszt = ((JAXBElement<String>) gszt);
	}

	public JAXBElement<String> getZzgpyy() {
		return zzgpyy;
	}

	public void setZzgpyy(JAXBElement<String> zzgpyy) {
		this.zzgpyy = ((JAXBElement<String>) zzgpyy);
	}

	public JAXBElement<String> getFzqs() {
		return fzqs;
	}

	public void setFzqs(JAXBElement<String> fzqs) {
		this.fzqs = ((JAXBElement<String>) fzqs);
	}

	public JAXBElement<String> getYzbqs() {
		return yzbqs;
	}

	public void setYzbqs(JAXBElement<String> yzbqs) {
		this.yzbqs = ((JAXBElement<String>) yzbqs);
	}

	public JAXBElement<String> getZqly() {
		return zqly;
	}

	public void setZqly(JAXBElement<String> zqly) {
		this.zqly = ((JAXBElement<String>) zqly);
	}

	public JAXBElement<String> getSf() {
		return sf;
	}

	public void setSf(JAXBElement<String> sf) {
		this.sf = ((JAXBElement<String>) sf);
	}

	public JAXBElement<String> getCs() {
		return cs;
	}

	public void setCs(JAXBElement<String> cs) {
		this.cs = ((JAXBElement<String>) cs);
	}

	public JAXBElement<String> getQx() {
		return qx;
	}

	public void setQx(JAXBElement<String> qx) {
		this.qx = ((JAXBElement<String>) qx);
	}

	public JAXBElement<String> getBgdz() {
		return bgdz;
	}

	public void setBgdz(JAXBElement<String> bgdz) {
		this.bgdz = ((JAXBElement<String>) bgdz);
	}

	public JAXBElement<String> getZczj() {
		return zczj;
	}

	public void setZczj(JAXBElement<String> zczj) {
		this.zczj = ((JAXBElement<String>) zczj);
	}

	public JAXBElement<String> getYysr() {
		return yysr;
	}

	public void setYysr(JAXBElement<String> yysr) {
		this.yysr = ((JAXBElement<String>) yysr);
	}

	public JAXBElement<String> getFr() {
		return fr;
	}

	public void setFr(JAXBElement<String> fr) {
		this.fr = ((JAXBElement<String>) fr);
	}

	public JAXBElement<String> getZycp() {
		return zycp;
	}

	public void setZycp(JAXBElement<String> zycp) {
		this.zycp = ((JAXBElement<String>) zycp);
	}

	public JAXBElement<String> getJthyfl() {
		return jthyfl;
	}

	public void setJthyfl(JAXBElement<String> jthyfl) {
		this.jthyfl = ((JAXBElement<String>) jthyfl);
	}

	public JAXBElement<String> getWz() {
		return wz;
	}

	public void setWz(JAXBElement<String> wz) {
		this.wz = ((JAXBElement<String>) wz);
	}

	public JAXBElement<String> getDh() {
		return dh;
	}

	public void setDh(JAXBElement<String> dh) {
		this.dh = ((JAXBElement<String>) dh);
	}

	public JAXBElement<String> getCz() {
		return cz;
	}

	public void setCz(JAXBElement<String> cz) {
		this.cz = ((JAXBElement<String>) cz);
	}

	public JAXBElement<String> getDzyx() {
		return dzyx;
	}

	public void setDzyx(JAXBElement<String> dzyx) {
		this.dzyx = ((JAXBElement<String>) dzyx);
	}

	public JAXBElement<String> getJylx() {
		return jylx;
	}

	public void setJylx(JAXBElement<String> jylx) {
		this.jylx = ((JAXBElement<String>) jylx);
	}

	public JAXBElement<String> getZssl() {
		return zssl;
	}

	public void setZssl(JAXBElement<String> zssl) {
		this.zssl = ((JAXBElement<String>) zssl);
	}

	public JAXBElement<String> getDm() {
		return dm;
	}

	public void setDm(JAXBElement<String> dm) {
		this.dm = ((JAXBElement<String>) dm);
	}

	public JAXBElement<String> getDmdh() {
		return dmdh;
	}

	public void setDmdh(JAXBElement<String> dmdh) {
		this.dmdh = ((JAXBElement<String>) dmdh);
	}

	public JAXBElement<String> getDsz() {
		return dsz;
	}

	public void setDsz(JAXBElement<String> dsz) {
		this.dsz = ((JAXBElement<String>) dsz);
	}

	public JAXBElement<String> getDszdh() {
		return dszdh;
	}

	public void setDszdh(JAXBElement<String> dszdh) {
		this.dszdh = ((JAXBElement<String>) dszdh);
	}

	public JAXBElement<String> getZjl() {
		return zjl;
	}

	public void setZjl(JAXBElement<String> zjl) {
		this.zjl = ((JAXBElement<String>) zjl);
	}

	public JAXBElement<String> getGsdjh() {
		return gsdjh;
	}

	public void setGsdjh(JAXBElement<String> gsdjh) {
		this.gsdjh = ((JAXBElement<String>) gsdjh);
	}

	public JAXBElement<String> getKjssws() {
		return kjssws;
	}

	public void setKjssws(JAXBElement<String> kjssws) {
		this.kjssws = ((JAXBElement<String>) kjssws);
	}

	public JAXBElement<String> getLssws() {
		return lssws;
	}

	public void setLssws(JAXBElement<String> lssws) {
		this.lssws = ((JAXBElement<String>) lssws);
	}

	public JAXBElement<String> getSbsj() {
		return sbsj;
	}

	public void setSbsj(JAXBElement<String> sbsj) {
		this.sbsj = ((JAXBElement<String>) sbsj);
	}

	public JAXBElement<String> getGpsj() {
		return gpsj;
	}

	public void setGpsj(JAXBElement<String> gpsj) {
		this.gpsj = ((JAXBElement<String>) gpsj);
	}

	public JAXBElement<String> getZzgpsj() {
		return zzgpsj;
	}

	public void setZzgpsj(JAXBElement<String> zzgpsj) {
		this.zzgpsj = ((JAXBElement<String>) zzgpsj);
	}

	public JAXBElement<String> getRksj() {
		return rksj;
	}

	public void setRksj(JAXBElement<String> rksj) {
		this.rksj = ((JAXBElement<String>) rksj);
	}

	public JAXBElement<String> getGfgsrq() {
		return gfgsrq;
	}

	public void setGfgsrq(JAXBElement<String> gfgsrq) {
		this.gfgsrq = ((JAXBElement<String>) gfgsrq);
	}

	public JAXBElement<String> getYxgsrq() {
		return yxgsrq;
	}

	public void setYxgsrq(JAXBElement<String> yxgsrq) {
		this.yxgsrq = ((JAXBElement<String>) yxgsrq);
	}

	public JAXBElement<String> getZssj() {
		return zssj;
	}

	public void setZssj(JAXBElement<String> zssj) {
		this.zssj = ((JAXBElement<String>) zssj);
	}
}
