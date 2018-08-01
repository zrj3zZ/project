package com.ibpmsoft.project.zqb.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.ibpmsoft.project.zqb.service.ZqbCustomerManageService;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;

public class ZqbCustomerManageAction extends ActionSupport{
	/**
	 * 版本
	 */
	private static final long serialVersionUID = 1L;

	public ZqbCustomerManageService getZqbCustomerManageService() {
		return zqbCustomerManageService;
	}

	public void setZqbCustomerManageService(
			ZqbCustomerManageService zqbCustomerManageService) {
		this.zqbCustomerManageService = zqbCustomerManageService;
	}

	private ZqbCustomerManageService zqbCustomerManageService;
	private List<HashMap> customerList;
	private String customername ;
	private Long instanceid;
	private int pageNumber; // 当前页数
	private int totalNum; // 总页数
	private int pageSize = 10; // 每页条数
	private String khmc;
	private String xmsplcServer;
	private boolean showProject;
	private String projectno;
	private String filename;
	private String commonstr;
	private String customerno ;
	private String type ;
	private String zwmc ;
	private BigDecimal jlr;
	private String gpsj;
	private String zczbbegin;
	private String zczbend;
	private String gfgsrqbegin;
	private String gfgsrqend;
	private String status ;
	private String ygp;
	private String zrfs;
	private String cxddbg;
	private String orderbyzqdm="0";
	private String orderbygpsj="0";
	private String orderbygpzt="0";
	private Long orgroleid;
	private String username;
	private String dm;
	private String khbh;
	private String zqdm;

	private String gpsjBEGIN;//挂牌时间开始
	private String gpsjEND;//挂牌时间截止
	private String extend4;//所属证监局
	private String innovation;//创新层
	private String zcqx;//所属大区或者所属部门
	private String sshy;//所属行业
	private String classification;//分类
	public String getCommonstr() {
		return commonstr;
	}

	public String getOrderbygpzt() {
		return orderbygpzt;
	}

	public void setOrderbygpzt(String orderbygpzt) {
		this.orderbygpzt = orderbygpzt;
	}

	public void setCommonstr(String commonstr) {
		this.commonstr = commonstr;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getProjectno() {
		return projectno;
	}

	public void setProjectno(String projectno) {
		this.projectno = projectno;
	}

	public boolean isShowProject() {
		return showProject;
	}

	public void setShowProject(boolean showProject) {
		this.showProject = showProject;
	}

	public String getXmsplcServer() {
		return xmsplcServer;
	}

	public void setXmsplcServer(String xmsplcServer) {
		this.xmsplcServer = xmsplcServer;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}
	
	public String getZqdm() {
		return zqdm;
	}

	public void setZqdm(String zqdm) {
		this.zqdm = zqdm;
	}

	public String getZqjc() {
		return zqjc;
	}

	public void setZqjc(String zqjc) {
		this.zqjc = zqjc;
	}

	private String zqjc;
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getZwmc() {
		return zwmc;
	}

	public void setZwmc(String zwmc) {
		this.zwmc = zwmc;
	}
	
	public String getGpsj() {
		return gpsj;
	}

	public void setGpsj(String gpsj) {
		this.gpsj = gpsj;
	}
	
	public String getGfgsrqbegin() {
		return gfgsrqbegin;
	}

	public void setGfgsrqbegin(String gfgsrqbegin) {
		this.gfgsrqbegin = gfgsrqbegin;
	}

	public String getGfgsrqend() {
		return gfgsrqend;
	}

	public void setGfgsrqend(String gfgsrqend) {
		this.gfgsrqend = gfgsrqend;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getOrgroleid() {
		return orgroleid;
	}

	public void setOrgroleid(Long orgroleid) {
		this.orgroleid = orgroleid;
	}

	public String getCxddbg() {
		return cxddbg;
	}

	public void setCxddbg(String cxddbg) {
		this.cxddbg = cxddbg;
	}

	public String getZrfs() {
		return zrfs;
	}

	public void setZrfs(String zrfs) {
		this.zrfs = zrfs;
	}

	public String getOrderbyzqdm() {
		return orderbyzqdm;
	}

	public void setOrderbyzqdm(String orderbyzqdm) {
		this.orderbyzqdm = orderbyzqdm;
	}

	public String getOrderbygpsj() {
		return orderbygpsj;
	}

	public void setOrderbygpsj(String orderbygpsj) {
		this.orderbygpsj = orderbygpsj;
	}

	public String getYgp() {
		return ygp;
	}

	public void setYgp(String ygp) {
		this.ygp = ygp;
	}

	public List<HashMap> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<HashMap> customerList) {
		this.customerList = customerList;
	}
	
	public BigDecimal getJlr() {
		return jlr;
	}

	public void setJlr(BigDecimal jlr) {
		this.jlr = jlr;
	}

	public String getZczbbegin() {
		return zczbbegin;
	}

	public void setZczbbegin(String zczbbegin) {
		this.zczbbegin = zczbbegin;
	}

	public String getZczbend() {
		return zczbend;
	}

	public void setZczbend(String zczbend) {
		this.zczbend = zczbend;
	}

	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

	public String getDm() {
		return dm;
	}

	public void setDm(String dm) {
		this.dm = dm;
	}
	public String getGpsjBEGIN() {
		return gpsjBEGIN;
	}

	public void setGpsjBEGIN(String gpsjBEGIN) {
		this.gpsjBEGIN = gpsjBEGIN;
	}

	public String getGpsjEND() {
		return gpsjEND;
	}

	public void setGpsjEND(String gpsjEND) {
		this.gpsjEND = gpsjEND;
	}

	public String getExtend4() {
		return extend4;
	}

	public void setExtend4(String extend4) {
		this.extend4 = extend4;
	}

	public String getInnovation() {
		return innovation;
	}

	public void setInnovation(String innovation) {
		this.innovation = innovation;
	}

	public String getZcqx() {
		return zcqx;
	}

	public void setZcqx(String zcqx) {
		this.zcqx = zcqx;
	}

	public String getSshy() {
		return sshy;
	}

	public void setSshy(String sshy) {
		this.sshy = sshy;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}	
	public void getdm() {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String user = uc.get_userModel().getUserid() + "[" + uc.get_userModel().getUsername() + "]";
		if(user.equals(dm)){
			ResponseUtil.write("error");
		}else{
			ResponseUtil.write("succes");
		}
	}
    public void expkhmc(){
        HashMap<String,Integer> params = zqbCustomerManageService.getCusParams();
        HttpServletResponse response = ServletActionContext.getResponse();
        zqbCustomerManageService.expkhmc(response,username,customername,zqdm,zrfs,cxddbg,status,
                type,zwmc,jlr,gpsjBEGIN,zczbbegin,zczbend,gfgsrqbegin,gfgsrqend,ygp
                ,extend4,zcqx,sshy,gpsjEND,innovation,classification,params);
    }
	public String index(){		
		if (pageNumber == 0)
			pageNumber = 1;
		xmsplcServer = ProcessAPI.getInstance().getProcessActDefId("XMSPLC");
		HashMap<String,Integer> params = zqbCustomerManageService.getCusParams();
		
		customerList = zqbCustomerManageService.getCurrentCustomerList(username,customername,zqdm,zrfs,cxddbg,status,
				type,zwmc,jlr,gpsjBEGIN,zczbbegin,zczbend,gfgsrqbegin,gfgsrqend,pageSize,pageNumber,orderbygpsj,orderbyzqdm,ygp
				,extend4,zcqx,sshy,gpsjEND,innovation,classification,params,orderbygpzt);
		
		totalNum=zqbCustomerManageService.getCurrentCustomerListSize(username,customername,zqdm,zrfs,cxddbg,status,
				type,zwmc,jlr,gpsjBEGIN,zczbbegin,zczbend,gfgsrqbegin,gfgsrqend,ygp
				,extend4,zcqx,sshy,gpsjEND,innovation,classification,params);		
		showProject=zqbCustomerManageService.getIsShowProject();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		orgroleid = uc.get_userModel().getOrgroleid();
		return SUCCESS;
	}
	public void delete(){
		String info= zqbCustomerManageService.deleteCustomer(instanceid);
			ResponseUtil.write(info); 
	}
	public String impCustomerIndex(){
		return SUCCESS;
	}
	public String impCustomerFile(){
		return SUCCESS;
	}
	public void impCustomer() throws Exception {
		zqbCustomerManageService.impCustomer(filename);
	}
	public void checkCFXX(){
		//xlj-综合吴尧和周瑞金的方法，删除ZqbAnnouncementAction中的方法customernameYZ，以及struts-zq.xml中的对应内容。
		String info= "";
		if(khmc.length()>80 || khmc.length()<4){
			info = "公司名称长度限制为4-80位！";
			ResponseUtil.write(info);
			return;
		}
		Pattern p = Pattern.compile("[0-9A-Za-z\u4e00-\u9fa5\\（\\）]*");
		 if(!p.matcher(khmc).matches()){
			 info = "公司名称只能为数字、字母、汉字和（）";
			 ResponseUtil.write(info);
			 return;
		 }		
		info=zqbCustomerManageService.checkCFXX(khmc,zqdm,zqjc,instanceid);
		ResponseUtil.write(info); 
	}
	public void Associate(){
		String jsonstr=zqbCustomerManageService.Associate(khmc);
		ResponseUtil.write(jsonstr); 
	}
	public void UserAssociate(){
		String jsonstr=zqbCustomerManageService.UserAssociate(commonstr);
		ResponseUtil.write(jsonstr); 
	}
	public void checkYGP(){
		Map params = new HashMap();
		params.put(1,customerno);
		String 	name=DBUTilNew.getDataStr("YGP", "SELECT YGP FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO= ? ", params);
		ResponseUtil.write(name);
	}
	public void checkXMCY_ZJJG(){
		Map params = new HashMap();
		params.put(1,customerno);
		params.put(2,projectno);
		params.put(3,customerno);
		params.put(4,projectno);
		String projectname=DBUTilNew.getDataStr("PROJECTNAME", "SELECT PROJECTNAME FROM BD_ZQB_PJ_BASE WHERE CUSTOMERNO= ? AND PROJECTNO= ?  UNION SELECT PROJECTNAME FROM BD_ZQB_XMLCXXB WHERE CUSTOMERNO= ?  AND PROJECTNO= ? ", params);
		if(projectname!=null&&!projectname.equals("")){
			params = new HashMap();
			params.put(1,customerno);
			String zjjg =DBUTilNew.getDataStr("PROJECTNO","SELECT C.PROJECTNO FROM (SELECT INSTANCEID,DATAID PROJECTID,B.CUSTOMERNO,B.LCBS,B.PROJECTNO FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT METADATAID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目管理表单')) A LEFT JOIN BD_ZQB_PJ_BASE B ON A.DATAID = B.ID ) C INNER JOIN (SELECT A.INSTANCEID,DATAID,B.ZJJGMC BZJJGMC,B.LXR BLXR,B.LXDH BLXDH,B.LXYX BLXYX FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT METADATAID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目中介机构')) A LEFT JOIN BD_ZQB_XMZJJG B ON A.DATAID = B.ID) D ON C.LCBS = D.INSTANCEID WHERE D.BZJJGMC IS NOT NULL AND D.BLXR IS NOT NULL AND C.CUSTOMERNO= ? ", params);
			String xmcy =DBUTilNew.getDataStr("PROJECTNO","SELECT C.PROJECTNO FROM (SELECT INSTANCEID,DATAID PROJECTID,B.CUSTOMERNO,B.LCBS,B.PROJECTNO FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT METADATAID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目管理表单')) A LEFT JOIN BD_ZQB_PJ_BASE B ON A.DATAID = B.ID ) C INNER JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME BNAME,B.POSITION BPOSITION,B.TEL BTEL,B.EMAIL BEMAIL,B.MEMO BMEMO,B.PHONE BPHONE,B.USERID BUSERID FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT METADATAID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')) A LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) D ON C.LCBS = D.INSTANCEID WHERE D.BNAME IS NOT NULL AND D.BUSERID IS NOT NULL AND C.CUSTOMERNO= ?", params);
			if((zjjg.equals("")||zjjg==null)&&(xmcy.equals("")||xmcy==null)){
				ResponseUtil.write("00");
			}
			if((!zjjg.equals("")&&zjjg!=null)&&(xmcy.equals("")||xmcy==null)){
				ResponseUtil.write("10");
			}
			if((zjjg.equals("")||zjjg==null)&&(!xmcy.equals("")&&xmcy!=null)){
				ResponseUtil.write("01");
			}
			if((!zjjg.equals("")&&zjjg!=null)&&(!xmcy.equals("")&&xmcy!=null)){
				ResponseUtil.write("11");
			}
		}
	}

	
}
