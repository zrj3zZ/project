package com.iwork.plugs.sms.action;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.iwork.commons.util.PinYinUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.sms.bean.PhonebookMst;
import com.iwork.plugs.sms.bean.PhonebookgroupMst;
import com.iwork.plugs.sms.service.PhonebookService;
import com.iwork.sdk.DemAPI;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
public class PhonebookAction extends ActionSupport {
	private static Logger logger = Logger.getLogger(PhonebookAction.class);
	private String type1;// 分组的名称
	private long mobilenum;
	private String name;
	private String extend1;
	private String extend2;
	private String extend3;
	private int id;
	private PhonebookService phonebookService;
	private String edittype1;
	// 号码簿修改号码
	private String cid;
	private String nameedit;
	private String mobilenumedit;
	private String extend1edit;
	private String extend2edit;
	private String extend3edit;
	private String typeedit;
	// 号码簿修改分组
	private String editgroupid;
	private String editgroupname;
//号码簿删除号码
	private String cid2;
	//jquery
	private String typeid;
	
	private String pstrScript;//号码簿增加分组的时候分组重复的返回值
	//private String typeaddj;
	
	private String htype;
	private String htypeedit;
	private String htypeeditid;
	//新增号码(jquery)
	private String htypee;
	private String hname;
	private String hmobile;
	private String hattr1;
	private String hattr2;
	private String hattr3;
	//删除号码
	private String hcid2;
	//查询号码(jquery)
	private String qname;
	private String qmobile;
	private String qattr1;
	private String qattr2;
	private String qattr3;
	//修改号码保存(jquery)
	private String hcid11;
	private String htypee1;
	private String hname1;
	private String hmobile1;
	private String hattr11;
	private String hattr21;
	private String hattr31;
	private int pageNumber;
	private int pageSize=10;
	private int totalNum;
	private List<HashMap> list;
	private List<HashMap> list1;
	private Long instanceid;
	private String zqdm;
	private String gsmc;
	private String leibie;
	private Long dhbfzFormid;
	private Long dhbfzDemId;
	private String zb;
	private String filename;
	private String model;
 	
	public void phoneBookcheckZB(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = "";
		if(uc != null){
			userid = uc.get_userModel().getUserid();
		}
		String info = "";
		StringBuffer sql = new StringBuffer("SELECT ZB FROM BD_GE_PHONEGROUP WHERE ZB=? AND CREATEUSER=?");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, zb);
			ps.setString(2, userid);
			rs = ps.executeQuery();
			while(rs.next()){
				info="当前用户已存在该分组!";
				break;
			}
			ResponseUtil.write(info);
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
	}
	/**
	 * 
	 * @return
	 */
	public String index(){
		String uuid = "5a47f41adc764690ae7f8258730e1618";
		//获取通讯录分组表单的DEMID和FORMID
		String idandformid = DBUtil.getString("SELECT ID||','||FORMID IDANDFORMID FROM SYS_DEM_ENGINE WHERE TITLE='通讯录分组表单'", "IDANDFORMID");
		dhbfzDemId = Long.parseLong(idandformid.split(",")[0]);
		dhbfzFormid = Long.parseLong(idandformid.split(",")[1]);
		HashMap conditionMap = new HashMap();
		conditionMap.put("USERID",UserContextUtil.getInstance().getCurrentUserId());
		list = DemAPI.getInstance().getList(uuid, conditionMap, "NAME");
		for(HashMap hash:list){
			String name = hash.get("NAME").toString();
			String pinyin = PinYinUtil.zh_CnToPinyinHeadParser(name);
			hash.put("PINYIN", pinyin);
			String zb = hash.get("ZB")==null?"":hash.get("ZB").toString();
			String[] zbarr = zb.split(",");
			String zbcorrect = "";
			for (int i = 0; i < zbarr.length; i++) {
				if(zbarr[i]!=null&&!zbarr[i].equals("")){
					if(i==zbarr.length-1){
						zbcorrect+=zbarr[i];
					}else{
						zbcorrect+=(zbarr[i]+",");
					}
				}
			}
			hash.put("ZB", zbcorrect);
		}
		return SUCCESS;
	}
	
	public String address(){
		if (pageNumber == 0)
			pageNumber = 1;
		HashMap data=phonebookService.getPhoneList(pageNumber,pageSize,zqdm,gsmc,leibie);
		list = (List<HashMap>)data.get("LIST");
		totalNum= (Integer)data.get("TOTALNUM");
		return SUCCESS;
	}
	
	public String phoneBook_exp(){
		HttpServletResponse response = ServletActionContext.getResponse();
		phonebookService.doExcelExp(response);
		return null;
	}

	public void removeItem(){
		String msg = "ok";
		if(instanceid!=null){
			DemAPI.getInstance().removeFormData(instanceid);
		}
		ResponseUtil.write(msg);
	}
	
	public String impPhoneBook(){
		return SUCCESS;
	}
	public void doImpPhoneBook() throws Exception{
		phonebookService.doImpPhoneBook(filename);
	}
	public void expPnoneBook(){
		HttpServletResponse response = ServletActionContext.getResponse();
		phonebookService.expPnoneBook(response,model);
	}
	
	/**
	 * 获取分组列表(jquery)
	 * @return
	 * @throws Exception
	 */
    
	public String typejson()throws Exception{
		String json = "";	
		json = phonebookService.getTypeTreeJson(); 
		HttpServletRequest request = ServletActionContext.getRequest();	
		request.setAttribute("PhonebookType", json);
		return "phonebookjson";	
	}
	public String load(){
		this.setTypeid(typeid);
		this.setQmobile("");
		this.setQname("");
		this.setQattr1("");
		this.setQattr2("");
		this.setQattr3("");
		return "phonebook1";
	}
	/**
	 * 根据分组查询号码(jquery)
	 * @return
	 */
	public  String qtypenums(){
		String typeid=this.getTypeid();
		String type="";
		if(!typeid.equals("")){
		 type=phonebookService.querydbtype(typeid);//根据分组id得到分组名称
		}	
		String qname=this.getQname();
		String qattr1=this.getQattr1()==null?"":this.getQattr1();
		String qattr2=this.getQattr2()==null?"":this.getQattr2();
		String qattr3=this.getQattr3()==null?"":this.getQattr3();
		String qname1="";
		String qattr11="";
		String qattr21="";
		String qattr31="";
		try{
			qname1=URLDecoder.decode(qname,"UTF-8");
			qattr11=URLDecoder.decode(qattr1,"UTF-8");
			qattr21=URLDecoder.decode(qattr2,"UTF-8");
			qattr31=URLDecoder.decode(qattr3,"UTF-8");
		}catch(Exception e){logger.error(e,e);}
		String qmobile=this.getQmobile()==null?"":this.getQmobile();		
		String json = "";	
		json = phonebookService.getNumsTreeJson(type,qname1,qmobile,qattr11,qattr21,qattr31); 
		HttpServletRequest request = ServletActionContext.getRequest();	
		request.setAttribute("typenums", json);
		
		return "phonebooknumsjson";
	}
	/**
	 * 刚开始链接我的号码簿界面(jquery)
	 * 
	 * @return
	 */
	public String loginaddnum() {
//		String typehtml = phonebookService.querytype();
//		Map request = (Map) ActionContext.getContext().get("request");
//		request.put("phonebooktype", typehtml);
		return "phonebookadd";
	}
	/**
	 * 号码簿分组删除(jquery)
	 * 
	 * @return
	 */
	public String removetype() {
		String cid = ServletActionContext.getRequest().getParameter("cid");
		phonebookService.deltype(cid);
		return "phonebook1";
	}
	/**
	 * 分组的保存及返回提示(jquery)
	 * @return
	 */
	public String savetype(){
		String pstrScript = "<script>$(function(){";	
		String typeaddj=this.getHtype();
		String msg  = phonebookService.checkrepeattype(typeaddj);
		if(msg.equals("")){	
				PhonebookgroupMst pm = new PhonebookgroupMst();				
				pm.setGroupname(typeaddj);				
				pm.setUserid("test");
				phonebookService.addtype(pm);//分组加入数据库
			 pstrScript +="window.parent.$.messager.alert('系统提示', '已成功添加分组！', 'info');";//弹出提示
		}else{
			 pstrScript +="window.parent.$.messager.alert('系统提示', '此分组已存在！', 'error');";//弹出提示	
		}
		pstrScript +="});</script>";	
		this.setPstrScript(pstrScript);	
		return "addtypej";	
	}
	/**
	 * 分组修改的保存及返回提示(jquery)
	 * @return
	 */
	public String edittype(){
		String pstrScript = "<script>$(function(){";	
		String typeeditj=this.getHtypeedit();
		String typeid=this.getHtypeeditid();
		String msg  = phonebookService.checkrepeattype2(typeid,typeeditj);
		if(msg.equals("")){	
			phonebookService.addtypeedit(typeid, typeeditj);//分组加入数据库
			 pstrScript +="window.parent.$.messager.alert('系统提示', '已成功修改分组！', 'info');";//弹出提示
		}else{
			 pstrScript +="window.parent.$.messager.alert('系统提示', '此分组已存在！', 'error');";//弹出提示	
		}
		pstrScript +="});</script>";	
		this.setPstrScript(pstrScript);	
		return "addtypej";	
	}
	/**
	 * phonebook新增号码,新增前号码重复性检查(jquery)
	 */
	public String addnumj() {
		long repeatnum = Long.valueOf(this.getHmobile());
		String repeattypeid = this.getHtypeeditid();
		String repeattype=phonebookService.querydbtype(repeattypeid);
		String pstrScript = "<script>$(function(){";	
		String msg  = phonebookService.checkrepeatnum(repeatnum,repeattype);
		if(msg.equals("")){	
			PhonebookMst pm = new PhonebookMst();				
			pm.setUserid("test");
			pm.setName(this.getHname());
			pm.setGroupname(repeattype);
			pm.setMobilenum(repeatnum );
			pm.setAttr1(this.getHattr1());
			pm.setAttr2(this.getHattr2());
			pm.setAttr3(this.getHattr3());
			phonebookService.adddb(pm);//新增号码加入数据库
			 pstrScript +="window.parent.$.messager.alert('系统提示', '已成功添加号码！', 'info');";//弹出提示
		}else{
			 pstrScript +="window.parent.$.messager.alert('系统提示', '此号码已存在！', 'error');";//弹出提示	
		}
		pstrScript +="});</script>";	
		this.setPstrScript(pstrScript);	
		return "addtypej";	
	}
	/**
	 * 号码簿删除号码(jquery)
	 * 
	 * @return
	 */
	public String delnumj() {
		//String cid = ServletActionContext.getRequest().getParameter("cid");
		phonebookService.delnum(cid);
		return "phonebook1";
	}
	
	/**
	 * 号码簿修改号码保存（jquery）
	 * @return
	 */
	public String saveeditnumj() {
		String cid = this.getHcid11();
		String nameedit = this.getHname1();
		String mobileedit = this.getHmobile1();
		long mobilen = Long.valueOf(mobileedit);
		String typeedit = this.getHtypee1();
		String extend1edit = this.getHattr11();
		String extend2edit = this.getHattr21();
		String extend3edit = this.getHattr31();
		String pstrScript = "<script>$(function(){";
		// 号码重复性检查
		String msg  = phonebookService.checkrepeatnum2(cid, mobilen,typeedit);
		if(msg.equals("")){	
			phonebookService.addeditnum(cid, nameedit, mobileedit, extend1edit,
					extend2edit, extend3edit, typeedit);//修改号码加入数据库
			 pstrScript +="window.parent.$.messager.alert('系统提示', '已成功修改号码！', 'info');";//弹出提示
		}else{
			 pstrScript +="window.parent.$.messager.alert('系统提示', '此号码已存在！', 'error');";//弹出提示	
		}
		pstrScript +="});</script>";	
		this.setPstrScript(pstrScript);	
		return "addtypej";	
	}
	/**
	 * 查询号码（jquery）
	 * @return
	 */
	public String qnumload(){
		String type=this.getHtypeeditid();
		String mobile=this.getHmobile();
		String name=this.getHname();
		String attr1=this.getHattr1();
		String attr2=this.getHattr2();
		String attr3=this.getHattr3();
		this.setTypeid(type);
		this.setQmobile(mobile);
		this.setQname(name);
		this.setQattr1(attr1);
		this.setQattr2(attr2);
		this.setQattr3(attr3);
		return "phonebook1";
	}
	
	
//	/**
//	 * phonebook新增号码,新增前号码重复性检查
//	 */
//	public String addnum() {
//		PhonebookMst pm = new PhonebookMst();
//		long repeatnum = this.getMobilenum();
//		String repeattype = this.getType1();
//		String repeatvalue = phonebookService.checkrepeatnum(repeatnum,
//				repeattype);
//		if (repeatvalue.equals("")) {
//			pm.setUserid("test");
//			pm.setName(this.getName());
//			pm.setGroupname(this.getType1());
//			pm.setMobilenum(this.getMobilenum());
//			pm.setAttr1(this.getExtend1());
//			pm.setAttr2(this.getExtend2());
//			pm.setAttr3(this.getExtend3());
//			phonebookService.adddb(pm);
//			String html = phonebookService.querydb("", "", "", "", "", "");
//			String typehtml = phonebookService.querytype();
//			Map request = (Map) ActionContext.getContext().get("request");
//			request.put("phonebook1", html);
//			request.put("phonebooktype", typehtml);
//		} else {
//			String typehtml = phonebookService.querytype();
//			Map request = (Map) ActionContext.getContext().get("request");
//			request.put("repeatnum", "此号码已经存在");
//			request.put("phonebooktype", typehtml);
//
//		}
//
//		return "phonebookadd";
//	}

//	/**
//	 * 查询全部号码
//	 * 
//	 * @return
//	 */
//	public String querynum() {
//		// String ctypename = this.getType1();
//		// String cname = this.getName();
//		// long cmobilenum = this.getMobilenum();
//		// String s = String.valueOf(cmobilenum);
//		// String cattr1 = this.getExtend1();
//		// String cattr2 = this.getExtend2();
//		// String cattr3 = this.getExtend3();
//		String html = phonebookService.querydb("", "", "", "", "", "");
//		Map request = (Map) ActionContext.getContext().get("request");
//		request.put("phonebook1", html);
//		return "phonebookxinxi";
//	}

//	/**
//	 * 查询部分号码
//	 * 
//	 * @return
//	 */
//	public String querynumbutton() {
//		String ctypename = this.getType1();
//		String cname = this.getName();
//		long cmobilenum = this.getMobilenum();
//		String scmobilenum = String.valueOf(cmobilenum).equals("0") ? ""
//				: String.valueOf(cmobilenum);
//		String cattr1 = this.getExtend1();
//		String cattr2 = this.getExtend2();
//		String cattr3 = this.getExtend3();
//		String html = phonebookService.querydb(ctypename, cname, scmobilenum,
//				cattr1, cattr2, cattr3);
//		String typehtml = phonebookService.querytype();
//		Map request = (Map) ActionContext.getContext().get("request");
//		request.put("phonebook1", html);
//		request.put("phonebooktype", typehtml);
//
//		return "phonebookxinxi";
//	}

//	/**
//	 * 增加分组,分组的重复性检查
//	 * 
//	 * @return
//	 */
//	public String addtype() {
//		String type = this.getEdittype1();
//		String repeattypev = phonebookService.checkrepeattype(type);
//		if (repeattypev.equals("")) {
//			PhonebookgroupMst pm = new PhonebookgroupMst();
//			// int a=pm.getCid()+1;
//			pm.setGroupname(type);
//			// pm.setGroupid(a);
//			pm.setUserid("test");
//			phonebookService.addtype(pm);
//			String html = phonebookService.querydb("", "", "", "", "", "");
//			String typehtml = phonebookService.querytype();
//			Map request = (Map) ActionContext.getContext().get("request");
//			request.put("phonebook1", html);
//			request.put("phonebooktype", typehtml);
//			return "typeset";
//		} else {
//			String html = phonebookService.querydb("", "", "", "", "", "");
//			String typehtml = phonebookService.querytype();
//			Map request = (Map) ActionContext.getContext().get("request");
//			request.put("phonebook1", html);
//			request.put("phonebooktype", typehtml);
//			request.put("repeattypev", "此分组已经存在");
//			return "typeset";
//		}
//	}

//	/**
//	 * 查询所有的分组(未分组除外)并显示
//	 * 
//	 * @return
//	 */
//	public String querytypeall() {
//		String typehtml = phonebookService.querytypeall();
//		Map request = (Map) ActionContext.getContext().get("request");
//		request.put("phonebooktype", typehtml);
//		return "typexinxi";
//	}

	/**
	 * 号码簿修改号码弹出的小页面
	 * 
	 * @param id
	 * @param userid
	 * @return
	 */
	public void dataedit() {
		// HttpServletRequest request = ServletActionContext.getRequest();
		// String cid = request.getParameter("id");
		// Hashtable<String, String> hashTags = new Hashtable<String, String>();
		StringBuffer sb = new StringBuffer();
		PhonebookMst mod = phonebookService.getPhonebookMst(id);
		String type1name = mod.getGroupname();
		String typeall = phonebookService.querytype(type1name);
		sb
				.append("<table width=98% align=center border=0 cellspacing=0 cellpadding=0>");
		sb.append("<tr><td>[<a  href='###' onclick=\"datasave('" + id
				+ "')\">保存</a>]<br>");
		sb.append("</td></tr></table>\n");
		sb
				.append("<br><table width=98% align=center border=0 cellspacing=0 cellpadding=0>");
		sb
				.append("<tr><td >姓名：</td><td align=left>")
				.append(
						"<input type='text' name='nameedit' id=\"nameedit\"   maxlength=30 size=30 value='"
								+ mod.getName() + "'></td></tr>");
		sb
				.append("<tr><td >手机号：</td><td align=left>")
				.append(
						"<input type='text' name='mobilenumedit' id=\"mobilenumedit\"    maxlength=30 size=30 value='"
								+ mod.getMobilenum() + "'></td></tr>");
		sb.append("<tr><td >分组：</td><td align=left>").append(
				typeall + "</td></tr>");
		sb
				.append("<tr><td >属性一：</td><td align=left>")
				.append(
						"<input type='text' name='extend1edit' id=\"extend1edit\"  maxlength=30 size=30 value='"
								+ mod.getAttr1() + "'></td></tr>");
		sb
				.append("<tr><td >属性二：</td><td align=left>")
				.append(
						"<input type='text' name='extend2edit' id=\"extend2edit\"    maxlength=30 size=30 value='"
								+ mod.getAttr2() + "'></td></tr>");
		sb
				.append("<tr><td >属性三：</td><td align=left>")
				.append(
						"<input type='text' name='extend3edit' id=\"extend3edit\"   maxlength=30 size=30 value='"
								+ mod.getAttr3() + "'></td></tr>");
		sb.append("</table>\n");

		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("gb2312");
			response.setContentType("text/html;charset=gb2312");
			response.getWriter().print(sb.toString());
		} catch (Exception e) {
			logger.error(e,e);
		}
	}

	public String getEditgroupid() {
		return editgroupid;
	}

	public void setEditgroupid(String editgroupid) {
		this.editgroupid = editgroupid;
	}

	public String getEditgroupname() {
		return editgroupname;
	}

	public void setEditgroupname(String editgroupname) {
		this.editgroupname = editgroupname;
	}

	public String getNameedit() {
		return nameedit;
	}

	public void setNameedit(String nameedit) {
		this.nameedit = nameedit;
	}

	public String getMobilenumedit() {
		return mobilenumedit;
	}

	public void setMobilenumedit(String mobilenumedit) {
		this.mobilenumedit = mobilenumedit;
	}

	public String getExtend1edit() {
		return extend1edit;
	}

	public void setExtend1edit(String extend1edit) {
		this.extend1edit = extend1edit;
	}

	public String getExtend2edit() {
		return extend2edit;
	}

	public void setExtend2edit(String extend2edit) {
		this.extend2edit = extend2edit;
	}

	public String getExtend3edit() {
		return extend3edit;
	}

	public void setExtend3edit(String extend3edit) {
		this.extend3edit = extend3edit;
	}

	public String getTypeedit() {
		return typeedit;
	}

	public void setTypeedit(String typeedit) {
		this.typeedit = typeedit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEdittype1() {
		return edittype1;
	}

	public void setEdittype1(String edittype1) {
		this.edittype1 = edittype1;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public long getMobilenum() {
		return mobilenum;
	}

	public void setMobilenum(long mobilenum) {
		this.mobilenum = mobilenum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtend1() {
		return extend1;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}

	public String getExtend2() {
		return extend2;
	}

	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}

	public String getExtend3() {
		return extend3;
	}

	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public PhonebookService getPhonebookService() {
		return phonebookService;
	}

	public void setPhonebookService(PhonebookService phonebookService) {
		this.phonebookService = phonebookService;
	}

	public String typeset() {

		return SUCCESS;
	}

	public String getCid2() {
		return cid2;
	}

	public void setCid2(String cid2) {
		this.cid2 = cid2;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getPstrScript() {
		return pstrScript;
	}
	public void setPstrScript(String pstrScript) {
		this.pstrScript = pstrScript;
	}
	public String getHtype() {
		return htype;
	}
	public void setHtype(String htype) {
		this.htype = htype;
	}
	public String getHtypeedit() {
		return htypeedit;
	}
	public void setHtypeedit(String htypeedit) {
		this.htypeedit = htypeedit;
	}
	public String getHtypeeditid() {
		return htypeeditid;
	}
	public void setHtypeeditid(String htypeeditid) {
		this.htypeeditid = htypeeditid;
	}
	public String getHname() {
		return hname;
	}
	public void setHname(String hname) {
		this.hname = hname;
	}
	public String getHmobile() {
		return hmobile;
	}
	public void setHmobile(String hmobile) {
		this.hmobile = hmobile;
	}
	public String getHattr1() {
		return hattr1;
	}
	public void setHattr1(String hattr1) {
		this.hattr1 = hattr1;
	}
	public String getHattr2() {
		return hattr2;
	}
	public void setHattr2(String hattr2) {
		this.hattr2 = hattr2;
	}
	public String getHattr3() {
		return hattr3;
	}
	public void setHattr3(String hattr3) {
		this.hattr3 = hattr3;
	}
	public String getHcid2() {
		return hcid2;
	}
	public void setHcid2(String hcid2) {
		this.hcid2 = hcid2;
	}
	public String getHtypee() {
		return htypee;
	}
	public void setHtypee(String htypee) {
		this.htypee = htypee;
	}
	public String getQname() {
		return qname;
	}
	public void setQname(String qname) {
		this.qname = qname;
	}
	public String getQmobile() {
		return qmobile;
	}
	public void setQmobile(String qmobile) {
		this.qmobile = qmobile;
	}
	public String getQattr1() {
		return qattr1;
	}
	public void setQattr1(String qattr1) {
		this.qattr1 = qattr1;
	}
	public String getQattr2() {
		return qattr2;
	}
	public void setQattr2(String qattr2) {
		this.qattr2 = qattr2;
	}
	public String getQattr3() {
		return qattr3;
	}
	public void setQattr3(String qattr3) {
		this.qattr3 = qattr3;
	}
	public String getStr(String str) {
		try {
			String temp_p = str;
			byte[] temp_t = temp_p.getBytes("ISO8859-1");
			String temp = new String(temp_t);
			return temp;
		} catch (Exception e) {logger.error(e,e);
		}
		return "NULL";
	}
	public String getHtypee1() {
		return htypee1;
	}
	public void setHtypee1(String htypee1) {
		this.htypee1 = htypee1;
	}
	public String getHname1() {
		return hname1;
	}
	public void setHname1(String hname1) {
		this.hname1 = hname1;
	}
	public String getHmobile1() {
		return hmobile1;
	}
	public void setHmobile1(String hmobile1) {
		this.hmobile1 = hmobile1;
	}
	public String getHattr11() {
		return hattr11;
	}
	public void setHattr11(String hattr11) {
		this.hattr11 = hattr11;
	}
	public String getHattr21() {
		return hattr21;
	}
	public void setHattr21(String hattr21) {
		this.hattr21 = hattr21;
	}
	public String getHattr31() {
		return hattr31;
	}
	public void setHattr31(String hattr31) {
		this.hattr31 = hattr31;
	}
	public String getHcid11() {
		return hcid11;
	}
	public void setHcid11(String hcid11) {
		this.hcid11 = hcid11;
	}


	public List<HashMap> getList() {
		return list;
	}


	public void setList(List<HashMap> list) {
		this.list = list;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public List<HashMap> getList1() {
		return list1;
	}

	public void setList1(List<HashMap> list1) {
		this.list1 = list1;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	
	public String getZqdm() {
		return zqdm;
	}

	public void setZqdm(String zqdm) {
		this.zqdm = zqdm;
	}

	public String getGsmc() {
		return gsmc;
	}

	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
	}
	
	public String getLeibie() {
		return leibie;
	}

	public void setLeibie(String leibie) {
		this.leibie = leibie;
	}

	public Long getDhbfzFormid() {
		return dhbfzFormid;
	}

	public void setDhbfzFormid(Long dhbfzFormid) {
		this.dhbfzFormid = dhbfzFormid;
	}

	public Long getDhbfzDemId() {
		return dhbfzDemId;
	}

	public void setDhbfzDemId(Long dhbfzDemId) {
		this.dhbfzDemId = dhbfzDemId;
	}
	
	public String getZb() {
		return zb;
	}
	public void setZb(String zb) {
		this.zb = zb;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
}
