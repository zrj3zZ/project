package news.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.service package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _QysjkZczj_QNAME = new QName(
			"http://service.com", "zczj");
	private final static QName _QysjkZbqs_QNAME = new QName(
			"http://service.com", "zbqs");
	private final static QName _QysjkSf_QNAME = new QName("http://service.com",
			"sf");
	private final static QName _QysjkGfdm_QNAME = new QName(
			"http://service.com", "gfdm");
	private final static QName _QysjkFr_QNAME = new QName("http://service.com",
			"fr");
	private final static QName _QysjkZyyw_QNAME = new QName(
			"http://service.com", "zyyw");
	private final static QName _QysjkGsqc_QNAME = new QName(
			"http://service.com", "gsqc");
	private final static QName _QysjkQyjc_QNAME = new QName(
			"http://service.com", "qyjc");
	private final static QName _QyjbxxGpdm_QNAME = new QName(
			"http://service.com", "gpdm");
	private final static QName _QyjbxxYysr_QNAME = new QName(
			"http://service.com", "yysr");
	private final static QName _QyjbxxSshy_QNAME = new QName(
			"http://service.com", "sshy");
	private final static QName _QyjbxxSzd_QNAME = new QName(
			"http://service.com", "szd");
	private final static QName _SsxxHref_QNAME = new QName(
			"http://service.com", "href");
	private final static QName _SsxxTitle_QNAME = new QName(
			"http://service.com", "title");
	private final static QName _SsxxId_QNAME = new QName("http://service.com",
			"id");
	private final static QName _SsxxContent_QNAME = new QName(
			"http://service.com", "content");
	private final static QName _SsxxTimes_QNAME = new QName(
			"http://service.com", "times");
	private final static QName _ZjjgName_QNAME = new QName(
			"http://service.com", "name");
	private final static QName _CxddGsmc_QNAME = new QName(
			"http://service.com", "gsmc");
	private final static QName _CxddType_QNAME = new QName(
			"http://service.com", "type");
	private final static QName _CxddSource_QNAME = new QName(
			"http://service.com", "source");
	private final static QName _CxddNature_QNAME = new QName(
			"http://service.com", "nature");
	private final static QName _QyggXb_QNAME = new QName("http://service.com",
			"xb");
	private final static QName _QyggZw_QNAME = new QName("http://service.com",
			"zw");
	private final static QName _QyggXm_QNAME = new QName("http://service.com",
			"xm");
	private final static QName _QytopGdxm_QNAME = new QName(
			"http://service.com", "gdxm");
	private final static QName _QytopCgbl_QNAME = new QName(
			"http://service.com", "cgbl");
	private final static QName _QytopGfxz_QNAME = new QName(
			"http://service.com", "gfxz");
	
	
	private final static QName Comnotice_id_QNAME = new QName("http://service.com", "id");
	@XmlElementDecl(namespace = "http://service.com", name = "id", scope = Comnotice.class)
	public JAXBElement<Integer> createComnoticeid(Integer value) {return new JAXBElement<Integer>(Comnotice_id_QNAME, Integer.class,Gpgsxq.class, value);}
	
	private final static QName Comnotice_gsdm_QNAME = new QName("http://service.com", "gsdm");
	@XmlElementDecl(namespace = "http://service.com", name = "gsdm", scope = Comnotice.class)
	public JAXBElement<Integer> createComnoticegsdm(Integer value) {return new JAXBElement<Integer>(Comnotice_gsdm_QNAME, Integer.class,Gpgsxq.class, value);}
	
	private final static QName Comnotice_noticename_QNAME = new QName("http://service.com", "noticename");
	@XmlElementDecl(namespace = "http://service.com", name = "noticename", scope = Comnotice.class)
	public JAXBElement<String> createComnoticenoticename(String value) {return new JAXBElement<String>(Comnotice_noticename_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName Comnotice_noticetype_QNAME = new QName("http://service.com", "noticetype");
	@XmlElementDecl(namespace = "http://service.com", name = "noticetype", scope = Comnotice.class)
	public JAXBElement<String> createComnoticenoticetype(String value) {return new JAXBElement<String>(Comnotice_noticetype_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName Comnotice_noticedate_QNAME = new QName("http://service.com", "noticedate");
	@XmlElementDecl(namespace = "http://service.com", name = "noticedate", scope = Comnotice.class)
	public JAXBElement<String> createComnoticenoticedate(String value) {return new JAXBElement<String>(Comnotice_noticedate_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName Comnotice_noticepublishdate_QNAME = new QName("http://service.com", "noticepublishdate");
	@XmlElementDecl(namespace = "http://service.com", name = "noticepublishdate", scope = Comnotice.class)
	public JAXBElement<String> createComnoticenoticepublishdate(String value) {return new JAXBElement<String>(Comnotice_noticepublishdate_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName Comnotice_noticeurl_QNAME = new QName("http://service.com", "noticeurl");
	@XmlElementDecl(namespace = "http://service.com", name = "noticeurl", scope = Comnotice.class)
	public JAXBElement<String> createComnoticenoticeurl(String value) {return new JAXBElement<String>(Comnotice_noticeurl_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName Comnotice_adddate_QNAME = new QName("http://service.com", "adddate");
	@XmlElementDecl(namespace = "http://service.com", name = "adddate", scope = Comnotice.class)
	public JAXBElement<String> createComnoticeadddate(String value) {return new JAXBElement<String>(Comnotice_adddate_QNAME, String.class,Gpgsxq.class, value);}
	
	
	private final static QName _id_QNAME = new QName("http://service.com", "id");
	@XmlElementDecl(namespace = "http://service.com", name = "id", scope = Gpgsxq.class)
	public JAXBElement<Integer> createGpgsxqid(Integer value) {return new JAXBElement<Integer>(_id_QNAME, Integer.class,Gpgsxq.class, value);}
	
	private final static QName _gpgsid_QNAME = new QName("http://service.com", "gpgsid");
	@XmlElementDecl(namespace = "http://service.com", name = "gpgsid", scope = Gpgsxq.class)
	public JAXBElement<Integer> createGpgsxqgpgsid(Integer value) {return new JAXBElement<Integer>(_gpgsid_QNAME, Integer.class,Gpgsxq.class, value);}
	
	private final static QName _gsqc_QNAME = new QName("http://service.com", "gsqc");
	@XmlElementDecl(namespace = "http://service.com", name = "gsqc", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqgsqc(String value) {return new JAXBElement<String>(_gsqc_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _gsjc_QNAME = new QName("http://service.com", "gsjc");
	@XmlElementDecl(namespace = "http://service.com", name = "gsjc", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqgsjc(String value) {return new JAXBElement<String>(_gsjc_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _gfdm_QNAME = new QName("http://service.com", "gfdm");
	@XmlElementDecl(namespace = "http://service.com", name = "gfdm", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqgfdm(String value) {return new JAXBElement<String>(_gfdm_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _hyfl_QNAME = new QName("http://service.com", "hyfl");
	@XmlElementDecl(namespace = "http://service.com", name = "hyfl", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqhyfl(String value) {return new JAXBElement<String>(_hyfl_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _zyyw_QNAME = new QName("http://service.com", "zyyw");
	@XmlElementDecl(namespace = "http://service.com", name = "zyyw", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqzyyw(String value) {return new JAXBElement<String>(_zyyw_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _zbqs_QNAME = new QName("http://service.com", "zbqs");
	@XmlElementDecl(namespace = "http://service.com", name = "zbqs", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqzbqs(String value) {return new JAXBElement<String>(_zbqs_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _gszt_QNAME = new QName("http://service.com", "gszt");
	@XmlElementDecl(namespace = "http://service.com", name = "gszt", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqgszt(String value) {return new JAXBElement<String>(_gszt_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _sbsj_QNAME = new QName("http://service.com", "sbsj");
	@XmlElementDecl(namespace = "http://service.com", name = "sbsj", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqsbsj(String value) {return new JAXBElement<String>(_sbsj_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _gpsj_QNAME = new QName("http://service.com", "gpsj");
	@XmlElementDecl(namespace = "http://service.com", name = "gpsj", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqgpsj(String value) {return new JAXBElement<String>(_gpsj_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _zzgpsj_QNAME = new QName("http://service.com", "zzgpsj");
	@XmlElementDecl(namespace = "http://service.com", name = "zzgpsj", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqzzgpsj(String value) {return new JAXBElement<String>(_zzgpsj_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _zzgpyy_QNAME = new QName("http://service.com", "zzgpyy");
	@XmlElementDecl(namespace = "http://service.com", name = "zzgpyy", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqzzgpyy(String value) {return new JAXBElement<String>(_zzgpyy_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _fzqs_QNAME = new QName("http://service.com", "fzqs");
	@XmlElementDecl(namespace = "http://service.com", name = "fzqs", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqfzqs(String value) {return new JAXBElement<String>(_fzqs_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _yzbqs_QNAME = new QName("http://service.com", "yzbqs");
	@XmlElementDecl(namespace = "http://service.com", name = "yzbqs", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqyzbqs(String value) {return new JAXBElement<String>(_yzbqs_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _rksj_QNAME = new QName("http://service.com", "rksj");
	@XmlElementDecl(namespace = "http://service.com", name = "rksj", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqrksj(String value) {return new JAXBElement<String>(_rksj_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _zqly_QNAME = new QName("http://service.com", "zqly");
	@XmlElementDecl(namespace = "http://service.com", name = "zqly", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqzqly(String value) {return new JAXBElement<String>(_zqly_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _sf_QNAME = new QName("http://service.com", "sf");
	@XmlElementDecl(namespace = "http://service.com", name = "sf", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqsf(String value) {return new JAXBElement<String>(_sf_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _cs_QNAME = new QName("http://service.com", "cs");
	@XmlElementDecl(namespace = "http://service.com", name = "cs", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqcs(String value) {return new JAXBElement<String>(_cs_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _qx_QNAME = new QName("http://service.com", "qx");
	@XmlElementDecl(namespace = "http://service.com", name = "qx", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqqx(String value) {return new JAXBElement<String>(_qx_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _bgdz_QNAME = new QName("http://service.com", "bgdz");
	@XmlElementDecl(namespace = "http://service.com", name = "bgdz", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqbgdz(String value) {return new JAXBElement<String>(_bgdz_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _zczj_QNAME = new QName("http://service.com", "zczj");
	@XmlElementDecl(namespace = "http://service.com", name = "zczj", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqzczj(String value) {return new JAXBElement<String>(_zczj_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _yysr_QNAME = new QName("http://service.com", "yysr");
	@XmlElementDecl(namespace = "http://service.com", name = "yysr", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqyysr(String value) {return new JAXBElement<String>(_yysr_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _fr_QNAME = new QName("http://service.com", "fr");
	@XmlElementDecl(namespace = "http://service.com", name = "fr", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqfr(String value) {return new JAXBElement<String>(_fr_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _zycp_QNAME = new QName("http://service.com", "zycp");
	@XmlElementDecl(namespace = "http://service.com", name = "zycp", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqzycp(String value) {return new JAXBElement<String>(_zycp_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _jthyfl_QNAME = new QName("http://service.com", "jthyfl");
	@XmlElementDecl(namespace = "http://service.com", name = "jthyfl", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqjthyfl(String value) {return new JAXBElement<String>(_jthyfl_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _gfgsrq_QNAME = new QName("http://service.com", "gfgsrq");
	@XmlElementDecl(namespace = "http://service.com", name = "gfgsrq", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqgfgsrq(String value) {return new JAXBElement<String>(_gfgsrq_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _yxgsrq_QNAME = new QName("http://service.com", "yxgsrq");
	@XmlElementDecl(namespace = "http://service.com", name = "yxgsrq", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqyxgsrq(String value) {return new JAXBElement<String>(_yxgsrq_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _wz_QNAME = new QName("http://service.com", "wz");
	@XmlElementDecl(namespace = "http://service.com", name = "wz", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqwz(String value) {return new JAXBElement<String>(_wz_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _dh_QNAME = new QName("http://service.com", "dh");
	@XmlElementDecl(namespace = "http://service.com", name = "dh", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqdh(String value) {return new JAXBElement<String>(_dh_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _cz_QNAME = new QName("http://service.com", "cz");
	@XmlElementDecl(namespace = "http://service.com", name = "cz", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqcz(String value) {return new JAXBElement<String>(_cz_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _dzyx_QNAME = new QName("http://service.com", "dzyx");
	@XmlElementDecl(namespace = "http://service.com", name = "dzyx", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqdzyx(String value) {return new JAXBElement<String>(_dzyx_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _jylx_QNAME = new QName("http://service.com", "jylx");
	@XmlElementDecl(namespace = "http://service.com", name = "jylx", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqjylx(String value) {return new JAXBElement<String>(_jylx_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _zssl_QNAME = new QName("http://service.com", "zssl");
	@XmlElementDecl(namespace = "http://service.com", name = "zssl", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqzssl(String value) {return new JAXBElement<String>(_zssl_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _dm_QNAME = new QName("http://service.com", "dm");
	@XmlElementDecl(namespace = "http://service.com", name = "dm", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqdm(String value) {return new JAXBElement<String>(_dm_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _dmdh_QNAME = new QName("http://service.com", "dmdh");
	@XmlElementDecl(namespace = "http://service.com", name = "dmdh", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqdmdh(String value) {return new JAXBElement<String>(_dmdh_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _dsz_QNAME = new QName("http://service.com", "dsz");
	@XmlElementDecl(namespace = "http://service.com", name = "dsz", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqdsz(String value) {return new JAXBElement<String>(_dsz_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _dszdh_QNAME = new QName("http://service.com", "dszdh");
	@XmlElementDecl(namespace = "http://service.com", name = "dszdh", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqdszdh(String value) {return new JAXBElement<String>(_dszdh_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _zjl_QNAME = new QName("http://service.com", "zjl");
	@XmlElementDecl(namespace = "http://service.com", name = "zjl", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqzjl(String value) {return new JAXBElement<String>(_zjl_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _gsdjh_QNAME = new QName("http://service.com", "gsdjh");
	@XmlElementDecl(namespace = "http://service.com", name = "gsdjh", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqgsdjh(String value) {return new JAXBElement<String>(_gsdjh_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _kjssws_QNAME = new QName("http://service.com", "kjssws");
	@XmlElementDecl(namespace = "http://service.com", name = "kjssws", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqkjssws(String value) {return new JAXBElement<String>(_kjssws_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _lssws_QNAME = new QName("http://service.com", "lssws");
	@XmlElementDecl(namespace = "http://service.com", name = "lssws", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqlssws(String value) {return new JAXBElement<String>(_lssws_QNAME, String.class,Gpgsxq.class, value);}
	
	private final static QName _zssj_QNAME = new QName("http://service.com", "zssj");
	@XmlElementDecl(namespace = "http://service.com", name = "zssj", scope = Gpgsxq.class)
	public JAXBElement<String> createGpgsxqzssj(String value) {return new JAXBElement<String>(_zssj_QNAME, String.class,Gpgsxq.class, value);}
	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.service
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link GetListCxdd2 }
	 * 
	 */
	public GetListCxdd2 createGetListCxdd2() {
		return new GetListCxdd2();
	}

	/**
	 * Create an instance of {@link ArrayOfQytop }
	 * 
	 */
	public ArrayOfQytop createArrayOfQytop() {
		return new ArrayOfQytop();
	}

	/**
	 * Create an instance of {@link GetQygg }
	 * 
	 */
	public GetQygg createGetQygg() {
		return new GetQygg();
	}

	/**
	 * Create an instance of {@link ArrayOfQysjk }
	 * 
	 */
	public ArrayOfQysjk createArrayOfQysjk() {
		return new ArrayOfQysjk();
	}

	/**
	 * Create an instance of {@link GetListCxdd1 }
	 * 
	 */
	public GetListCxdd1 createGetListCxdd1() {
		return new GetListCxdd1();
	}

	/**
	 * Create an instance of {@link Qysjk }
	 * 
	 */
	public Qysjk createQysjk() {
		return new Qysjk();
	}

	/**
	 * Create an instance of {@link Zjjg }
	 * 
	 */
	public Zjjg createZjjg() {
		return new Zjjg();
	}

	/**
	 * Create an instance of {@link Qytop }
	 * 
	 */
	public Qytop createQytop() {
		return new Qytop();
	}

	/**
	 * Create an instance of {@link GetListZjjgResponse }
	 * 
	 */
	public GetListZjjgResponse createGetListZjjgResponse() {
		return new GetListZjjgResponse();
	}

	/**
	 * Create an instance of {@link GetQyjbxxResponse }
	 * 
	 */
	public GetQyjbxxResponse createGetQyjbxxResponse() {
		return new GetQyjbxxResponse();
	}

	/**
	 * Create an instance of {@link GetQytop }
	 * 
	 */
	public GetQytop createGetQytop() {
		return new GetQytop();
	}

	/**
	 * Create an instance of {@link ArrayOfSsxx }
	 * 
	 */
	public ArrayOfSsxx createArrayOfSsxx() {
		return new ArrayOfSsxx();
	}

	/**
	 * Create an instance of {@link GetListResponse }
	 * 
	 */
	public GetListResponse createGetListResponse() {
		return new GetListResponse();
	}

	/**
	 * Create an instance of {@link GetListZjjg }
	 * 
	 */
	public GetListZjjg createGetListZjjg() {
		return new GetListZjjg();
	}

	/**
	 * Create an instance of {@link GetZjjgById }
	 * 
	 */
	public GetZjjgById createGetZjjgById() {
		return new GetZjjgById();
	}

	/**
	 * Create an instance of {@link GetListCxdd1Response }
	 * 
	 */
	public GetListCxdd1Response createGetListCxdd1Response() {
		return new GetListCxdd1Response();
	}

	/**
	 * Create an instance of {@link QysjkCountResponse }
	 * 
	 */
	public QysjkCountResponse createQysjkCountResponse() {
		return new QysjkCountResponse();
	}

	/**
	 * Create an instance of {@link Qyjbxx }
	 * 
	 */
	public Qyjbxx createQyjbxx() {
		return new Qyjbxx();
	}

	/**
	 * Create an instance of {@link ArrayOfQyjbxx }
	 * 
	 */
	public ArrayOfQyjbxx createArrayOfQyjbxx() {
		return new ArrayOfQyjbxx();
	}

	/**
	 * Create an instance of {@link QysjkCount }
	 * 
	 */
	public QysjkCount createQysjkCount() {
		return new QysjkCount();
	}

	/**
	 * Create an instance of {@link GetZjjgByIdResponse }
	 * 
	 */
	public GetZjjgByIdResponse createGetZjjgByIdResponse() {
		return new GetZjjgByIdResponse();
	}

	/**
	 * Create an instance of {@link GetQyjbxx }
	 * 
	 */
	public GetQyjbxx createGetQyjbxx() {
		return new GetQyjbxx();
	}

	/**
	 * Create an instance of {@link ArrayOfQygg }
	 * 
	 */
	public ArrayOfQygg createArrayOfQygg() {
		return new ArrayOfQygg();
	}

	/**
	 * Create an instance of {@link Ssxx }
	 * 
	 */
	public Ssxx createSsxx() {
		return new Ssxx();
	}

	/**
	 * Create an instance of {@link CxddCount1 }
	 * 
	 */
	public CxddCount1 createCxddCount1() {
		return new CxddCount1();
	}

	/**
	 * Create an instance of {@link Qygg }
	 * 
	 */
	public Qygg createQygg() {
		return new Qygg();
	}

	/**
	 * Create an instance of {@link GetQysjkResponse }
	 * 
	 */
	public GetQysjkResponse createGetQysjkResponse() {
		return new GetQysjkResponse();
	}

	/**
	 * Create an instance of {@link GetCxddById }
	 * 
	 */
	public GetCxddById createGetCxddById() {
		return new GetCxddById();
	}

	/**
	 * Create an instance of {@link GetQysjk }
	 * 
	 */
	public GetQysjk createGetQysjk() {
		return new GetQysjk();
	}

	/**
	 * Create an instance of {@link GetListCxdd2Response }
	 * 
	 */
	public GetListCxdd2Response createGetListCxdd2Response() {
		return new GetListCxdd2Response();
	}

	/**
	 * Create an instance of {@link ZjjgCountResponse }
	 * 
	 */
	public ZjjgCountResponse createZjjgCountResponse() {
		return new ZjjgCountResponse();
	}

	/**
	 * Create an instance of {@link GetSsxxById }
	 * 
	 */
	public GetSsxxById createGetSsxxById() {
		return new GetSsxxById();
	}

	/**
	 * Create an instance of {@link GetQysjk1 }
	 * 
	 */
	public GetQysjk1 createGetQysjk1() {
		return new GetQysjk1();
	}

	/**
	 * Create an instance of {@link GetSsxxByIdResponse }
	 * 
	 */
	public GetSsxxByIdResponse createGetSsxxByIdResponse() {
		return new GetSsxxByIdResponse();
	}

	/**
	 * Create an instance of {@link ArrayOfCxdd }
	 * 
	 */
	public ArrayOfCxdd createArrayOfCxdd() {
		return new ArrayOfCxdd();
	}
	
	/**
	 * Create an instance of {@link ArrayOfGpgsxq }
	 * 
	 */
	public ArrayOfComnotice createArrayOfComnotice() {
		return new ArrayOfComnotice();
	}
	
	/**
	 * Create an instance of {@link ArrayOfGpgsxq }
	 * 
	 */
	public ArrayOfGpgsxq createArrayOfGpgsxq() {
		return new ArrayOfGpgsxq();
	}

	/**
	 * Create an instance of {@link CxddCount1Response }
	 * 
	 */
	public CxddCount1Response createCxddCount1Response() {
		return new CxddCount1Response();
	}

	/**
	 * Create an instance of {@link ZjjgCount }
	 * 
	 */
	public ZjjgCount createZjjgCount() {
		return new ZjjgCount();
	}

	/**
	 * Create an instance of {@link GetList }
	 * 
	 */
	public GetList createGetList() {
		return new GetList();
	}

	/**
	 * Create an instance of {@link SsxxCountResponse }
	 * 
	 */
	public SsxxCountResponse createSsxxCountResponse() {
		return new SsxxCountResponse();
	}

	/**
	 * Create an instance of {@link GetCxddByIdResponse }
	 * 
	 */
	public GetCxddByIdResponse createGetCxddByIdResponse() {
		return new GetCxddByIdResponse();
	}

	/**
	 * Create an instance of {@link GetQytopResponse }
	 * 
	 */
	public GetQytopResponse createGetQytopResponse() {
		return new GetQytopResponse();
	}

	/**
	 * Create an instance of {@link CxddCountResponse }
	 * 
	 */
	public CxddCountResponse createCxddCountResponse() {
		return new CxddCountResponse();
	}

	/**
	 * Create an instance of {@link CxddCount }
	 * 
	 */
	public CxddCount createCxddCount() {
		return new CxddCount();
	}

	/**
	 * Create an instance of {@link GetQyggResponse }
	 * 
	 */
	public GetQyggResponse createGetQyggResponse() {
		return new GetQyggResponse();
	}

	/**
	 * Create an instance of {@link GetListCxdd }
	 * 
	 */
	public GetListCxdd createGetListCxdd() {
		return new GetListCxdd();
	}

	/**
	 * Create an instance of {@link SsxxCount }
	 * 
	 */
	public SsxxCount createSsxxCount() {
		return new SsxxCount();
	}

	/**
	 * Create an instance of {@link GetListCxddResponse }
	 * 
	 */
	public GetListCxddResponse createGetListCxddResponse() {
		return new GetListCxddResponse();
	}

	/**
	 * Create an instance of {@link GetQysjk1Response }
	 * 
	 */
	public GetQysjk1Response createGetQysjk1Response() {
		return new GetQysjk1Response();
	}

	/**
	 * Create an instance of {@link ArrayOfZjjg }
	 * 
	 */
	public ArrayOfZjjg createArrayOfZjjg() {
		return new ArrayOfZjjg();
	}

	/**
	 * Create an instance of {@link Cxdd }
	 * 
	 */
	public Cxdd createCxdd() {
		return new Cxdd();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "zczj", scope = Qysjk.class)
	public JAXBElement<Double> createQysjkZczj(Double value) {
		return new JAXBElement<Double>(_QysjkZczj_QNAME, Double.class,
				Qysjk.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "zbqs", scope = Qysjk.class)
	public JAXBElement<String> createQysjkZbqs(String value) {
		return new JAXBElement<String>(_QysjkZbqs_QNAME, String.class,
				Qysjk.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "sf", scope = Qysjk.class)
	public JAXBElement<String> createQysjkSf(String value) {
		return new JAXBElement<String>(_QysjkSf_QNAME, String.class,
				Qysjk.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "gfdm", scope = Qysjk.class)
	public JAXBElement<String> createQysjkGfdm(String value) {
		return new JAXBElement<String>(_QysjkGfdm_QNAME, String.class,
				Qysjk.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "fr", scope = Qysjk.class)
	public JAXBElement<String> createQysjkFr(String value) {
		return new JAXBElement<String>(_QysjkFr_QNAME, String.class,
				Qysjk.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "zyyw", scope = Qysjk.class)
	public JAXBElement<String> createQysjkZyyw(String value) {
		return new JAXBElement<String>(_QysjkZyyw_QNAME, String.class,
				Qysjk.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "gsqc", scope = Qysjk.class)
	public JAXBElement<String> createQysjkGsqc(String value) {
		return new JAXBElement<String>(_QysjkGsqc_QNAME, String.class,
				Qysjk.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "qyjc", scope = Qysjk.class)
	public JAXBElement<String> createQysjkQyjc(String value) {
		return new JAXBElement<String>(_QysjkQyjc_QNAME, String.class,
				Qysjk.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "zczj", scope = Qyjbxx.class)
	public JAXBElement<Double> createQyjbxxZczj(Double value) {
		return new JAXBElement<Double>(_QysjkZczj_QNAME, Double.class,
				Qyjbxx.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "zbqs", scope = Qyjbxx.class)
	public JAXBElement<String> createQyjbxxZbqs(String value) {
		return new JAXBElement<String>(_QysjkZbqs_QNAME, String.class,
				Qyjbxx.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "gpdm", scope = Qyjbxx.class)
	public JAXBElement<String> createQyjbxxGpdm(String value) {
		return new JAXBElement<String>(_QyjbxxGpdm_QNAME, String.class,
				Qyjbxx.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "yysr", scope = Qyjbxx.class)
	public JAXBElement<Double> createQyjbxxYysr(Double value) {
		return new JAXBElement<Double>(_QyjbxxYysr_QNAME, Double.class,
				Qyjbxx.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "sshy", scope = Qyjbxx.class)
	public JAXBElement<String> createQyjbxxSshy(String value) {
		return new JAXBElement<String>(_QyjbxxSshy_QNAME, String.class,
				Qyjbxx.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "szd", scope = Qyjbxx.class)
	public JAXBElement<String> createQyjbxxSzd(String value) {
		return new JAXBElement<String>(_QyjbxxSzd_QNAME, String.class,
				Qyjbxx.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "href", scope = Ssxx.class)
	public JAXBElement<String> createSsxxHref(String value) {
		return new JAXBElement<String>(_SsxxHref_QNAME, String.class,
				Ssxx.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "title", scope = Ssxx.class)
	public JAXBElement<String> createSsxxTitle(String value) {
		return new JAXBElement<String>(_SsxxTitle_QNAME, String.class,
				Ssxx.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "id", scope = Ssxx.class)
	public JAXBElement<Integer> createSsxxId(Integer value) {
		return new JAXBElement<Integer>(_SsxxId_QNAME, Integer.class,
				Ssxx.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "content", scope = Ssxx.class)
	public JAXBElement<String> createSsxxContent(String value) {
		return new JAXBElement<String>(_SsxxContent_QNAME, String.class,
				Ssxx.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "times", scope = Ssxx.class)
	public JAXBElement<String> createSsxxTimes(String value) {
		return new JAXBElement<String>(_SsxxTimes_QNAME, String.class,
				Ssxx.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "href", scope = Zjjg.class)
	public JAXBElement<String> createZjjgHref(String value) {
		return new JAXBElement<String>(_SsxxHref_QNAME, String.class,
				Zjjg.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "id", scope = Zjjg.class)
	public JAXBElement<Integer> createZjjgId(Integer value) {
		return new JAXBElement<Integer>(_SsxxId_QNAME, Integer.class,
				Zjjg.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "name", scope = Zjjg.class)
	public JAXBElement<String> createZjjgName(String value) {
		return new JAXBElement<String>(_ZjjgName_QNAME, String.class,
				Zjjg.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "gfdm", scope = Cxdd.class)
	public JAXBElement<String> createCxddGfdm(String value) {
		return new JAXBElement<String>(_QysjkGfdm_QNAME, String.class,
				Cxdd.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "gsmc", scope = Cxdd.class)
	public JAXBElement<String> createCxddGsmc(String value) {
		return new JAXBElement<String>(_CxddGsmc_QNAME, String.class,
				Cxdd.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "href", scope = Cxdd.class)
	public JAXBElement<String> createCxddHref(String value) {
		return new JAXBElement<String>(_SsxxHref_QNAME, String.class,
				Cxdd.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "type", scope = Cxdd.class)
	public JAXBElement<String> createCxddType(String value) {
		return new JAXBElement<String>(_CxddType_QNAME, String.class,
				Cxdd.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "title", scope = Cxdd.class)
	public JAXBElement<String> createCxddTitle(String value) {
		return new JAXBElement<String>(_SsxxTitle_QNAME, String.class,
				Cxdd.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "id", scope = Cxdd.class)
	public JAXBElement<Integer> createCxddId(Integer value) {
		return new JAXBElement<Integer>(_SsxxId_QNAME, Integer.class,
				Cxdd.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "content", scope = Cxdd.class)
	public JAXBElement<String> createCxddContent(String value) {
		return new JAXBElement<String>(_SsxxContent_QNAME, String.class,
				Cxdd.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "times", scope = Cxdd.class)
	public JAXBElement<String> createCxddTimes(String value) {
		return new JAXBElement<String>(_SsxxTimes_QNAME, String.class,
				Cxdd.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "source", scope = Cxdd.class)
	public JAXBElement<String> createCxddSource(String value) {
		return new JAXBElement<String>(_CxddSource_QNAME, String.class,
				Cxdd.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "nature", scope = Cxdd.class)
	public JAXBElement<String> createCxddNature(String value) {
		return new JAXBElement<String>(_CxddNature_QNAME, String.class,
				Cxdd.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "xb", scope = Qygg.class)
	public JAXBElement<String> createQyggXb(String value) {
		return new JAXBElement<String>(_QyggXb_QNAME, String.class, Qygg.class,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "zw", scope = Qygg.class)
	public JAXBElement<String> createQyggZw(String value) {
		return new JAXBElement<String>(_QyggZw_QNAME, String.class, Qygg.class,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "xm", scope = Qygg.class)
	public JAXBElement<String> createQyggXm(String value) {
		return new JAXBElement<String>(_QyggXm_QNAME, String.class, Qygg.class,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "gdxm", scope = Qytop.class)
	public JAXBElement<String> createQytopGdxm(String value) {
		return new JAXBElement<String>(_QytopGdxm_QNAME, String.class,
				Qytop.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "cgbl", scope = Qytop.class)
	public JAXBElement<String> createQytopCgbl(String value) {
		return new JAXBElement<String>(_QytopCgbl_QNAME, String.class,
				Qytop.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.com", name = "gfxz", scope = Qytop.class)
	public JAXBElement<String> createQytopGfxz(String value) {
		return new JAXBElement<String>(_QytopGfxz_QNAME, String.class,
				Qytop.class, value);
	}
}
