package com.iwork.plugs.sms.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.sms.bean.PageInfoModel;
import com.iwork.plugs.sms.service.MsgService;
import com.iwork.sdk.DemAPI;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
public class MessageSendAction extends ActionSupport {
	private static Logger logger = Logger.getLogger(MessageSendAction.class);
	private String content;
	private String phone;
	private MsgService msgService;
	private String id;
	private String hidden_nums;
	private String phonebook_nums;
	private String returnvalue;
	private String rev;
	private String unitprice;
	private String msglen;
	private String html;
	// 号码簿选号
	private String type1;
	private String name;
	private String mobilenum;
	private String extend1;
	private String extend2;
	private String extend3;

	private String tvalue;

	private String oldcontent;
	private String oldphone;

	private String content1;
	private String searchkey;   //获取查询键值
	private String phone1;
	private PageInfoModel model;
	private String content11;//发送传参数
	private String phone11;
	private String content12;//号码簿选号传参数
	private String phone12;
	private String contents;//发送全部挂牌公司短信文本
	private boolean isview;//属性是用来判断是否要显示发送挂牌公司按钮
	private String phones;//该属性是用来存储获取到的手机号码
	private String groupname;//该属性是用来保存组名的
	private List<HashMap<String,Object>> list;
	private String is360;
	private String nodeType;
	
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getIs360() {
		return is360;
	}
	public void setIs360(String is360) {
		this.is360 = is360;
	}
	public boolean isCWorDD(){
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			Long orgRoleId = uc.get_userModel().getOrgroleid();
			if (orgRoleId.equals(new Long(5))||orgRoleId.equals(new Long(4))) {
				flag = true;
			}
			if(!flag){
				List<OrgUserMap> usermaplist=uc.get_userMapList();
				if(usermaplist.size()>0){
					for(OrgUserMap oum:usermaplist){
						String rolejrid=oum.getOrgroleid();
						if(rolejrid.equals("4")){
							flag = true;
							break;
						}
					}
				}
			}
		}
		return flag;
	}
	public boolean setsView(){
		boolean flag = false;
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		UserContext target = UserContextUtil.getInstance().getUserContext(userid);
    	String deptname = "";
    	if(target != null){
    		deptname = target.get_userModel().getDepartmentname();
    		if(deptname.equals("督导管理部") || deptname.equals("场外市场部")){
    			flag = true;
    		}
    	}
    	return flag;
	}
	public String loginmsg() {
		/*HttpServletRequest request = ServletActionContext.getRequest();
		String agent=request.getHeader("User-Agent").toLowerCase();
		if(getBrowserName(agent).equals("others")){
			is360="<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Frameset//EN' 'http://www.w3.org/TR/html4/frameset.dtd'>";
		}else{
			is360="<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>";
		}*/
		isview = isCWorDD();
		String breturnvalue = this.getRev();
		if(breturnvalue!=null)
		breturnvalue=this.getStr(breturnvalue);
		String bcontent = this.getContent();// MsgConst.DEFAULT_CONTENT;
		if(bcontent!=null)
		bcontent = this.getStr(bcontent);
		String bphone = this.getPhone();// MsgConst.DEFAULT_NUM;
		if(bphone!=null)
		bphone = this.getStr(bphone);
		String unitprice = "0.065";// 数据库取短信单价
		String msglen = "64";
		PageInfoModel pm = this.getModel();
		if (pm == null) {
			pm = new PageInfoModel();
		}
		pm.setUnitprice(unitprice);
		pm.setMsglen(msglen);
		if (bphone != null && bphone != "NULL") {
			pm.setBphone(bphone);
		} else {
			pm.setBphone("");
		}
		if (bcontent != null && bcontent != "NULL") {
			pm.setBcontent(bcontent);
		} else {
			pm.setBcontent("");
		}
		if(breturnvalue!=null&&breturnvalue!="NULL"){
			pm.setBv(breturnvalue);
		}else{
			pm.setBv("");
		}
		this.setModel(pm);
		list=msgService.getList();
		return SUCCESS;
	}

public String getBrowserName(String agent){
	if(agent.indexOf("qqbrowser")>0){
		return "qqbrowser";
	}else if(agent.indexOf("opera")>0){
		return "opera";
	}else if(agent.indexOf("firefox")>0){
		return "firefox";
	}else if(agent.indexOf("webkit")>0){
		return "webkit";
	}else if(agent.indexOf("msie")>0 && agent.indexOf("rv:11")>0){
		return "ie";
	}else if(agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0){
		return "ie11";
	}else{
		return "others";
	}
}
	
	public void messagesend() {
		String info = msgService.handleMsg(content, phone);
		ResponseUtil.write(info);
	}
	
	public void messageSendBeforeContentCheck(){
		String contentCheckResult = msgService.messageSendBeforeContentCheck(content);
		ResponseUtil.write(contentCheckResult);
	}
 
	/**
	 * 号码簿选号
	 * 
	 * @return
	 */
	public String phonebookSelectnum() {
		String content = this.getContent12();
		String phoneNum = this.getPhone12();
		phoneNum = this.getStr(phoneNum);
		content = this.getStr(content);
		PageInfoModel pm = this.getModel();
		if (pm == null) {
			pm = new PageInfoModel();
		}
		pm.setBcontent(content);
		pm.setBphone(phoneNum);
		this.setModel(pm);
		return SUCCESS;
	}

	public void showPhoneBookJson(){
		String json = msgService.showPhoneBookJson(searchkey);
		ResponseUtil.write(json);
		
	}
	
	public void showPhoneBookJsonTree(){
		String json = msgService.showPhoneBookJson(searchkey);
		ResponseUtil.write(json);
	}
	
	public void showPhoneBookJsonTreeNew(){
		String json = "";
		if(nodeType!=null){ 
			if(id!=null&&nodeType.equals("dept")){
				json = msgService.getDeptAndUserJson(new Long(id));
			}  
		}else{ 
			json = msgService.getDeptAndUserJson(new Long(0));
		}
		ResponseUtil.write(json); 
	}
	
	public void justPhoneBookJsonTree(){
		Map<String, Object> list = new HashMap<String, Object>();
		String departmentname = msgService.getdepartmentJson();
		list.put("departmentname", departmentname);
		String company = msgService.getcompanyJson();
		list.put("company", company);
		String changwai = msgService.getchangwaiJson();
		list.put("changwai", changwai);
		String zhongjie = msgService.getzhongjieJson();
		list.put("zhongjie", zhongjie);
		String yewududao = msgService.getyewududaoJson();
		list.put("yewududao", yewududao);
		String yewufazhan = msgService.getyewufazhanJson();
		list.put("yewufazhan", yewufazhan);
		ResponseUtil.write(departmentname);
	}
	public void getphonegroup(){
		String zb = msgService.getphonegroup();
		ResponseUtil.write(zb);
	}
	public void delphonegroup(){
		
		DemAPI demapi = DemAPI.getInstance();
		String zb = demapi.getFromData(Long.parseLong(id)).get("ZB").toString();
		
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		
		int num = DBUtil.getInt("SELECT COUNT(*) NUM FROM (SELECT ID,NAME,TEL,TITLE,EMAIL,COMPANY,USERID,REGEXP_SUBSTR(ZB, '[^,]+', 1, RN) ZB FROM BD_GE_PHONEBOOK, (SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(ZB, '[^,]+', 1, RN) IS NOT NULL) WHERE USERID='"+userid+"' AND ZB='"+zb+"'", "NUM");
		
		if(num<1){
			demapi.removeFormData(Long.parseLong(id));
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write(zb+"分组下存在联系人!");
		}
	}
	/*public void getphonegrouplist(){
		String zbdetail = msgService.getphonegrouplist(groupname);
		ResponseUtil.write(zbdetail);
	}*/
	public void getallphone(){
		StringBuffer jsonHtml = new StringBuffer();
		List<HashMap> root = msgService.getallphone();
		JSONArray json = JSONArray.fromObject(root);
		jsonHtml.append(json);
		ResponseUtil.write(jsonHtml.toString());
	}
	
	public void getGroupValue(){
		StringBuffer jsonHtml = msgService.getGroupValue(id);
		ResponseUtil.write(jsonHtml.toString());
	}
	
	public void getcompanyphonelist(){
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String useridname = userModel.getUserid() + "[" + userModel.getUsername() + "]";
		if(useridname != null){
			if(UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getOrgroleid()==5){
				phones = msgService.getcompanyphonelistcw();
			}else{
				phones = msgService.getcompanyphonelist(useridname);
			}
			//下面这段注释的代码是用来发送给所有挂牌公司短信的
			/*if(phones != null&&contents != null){
				msgService.handleMsg(contents, phones);
			}*/
			ResponseUtil.write(phones);
		}
	}
	/**
	 * 号码簿选号 查询号码
	 * 
	 * @return
	 */
	public String qselectnum() {
		return SUCCESS;
	}

	public String selectednum() {
		String tvalue = this.getTvalue();// 新选的号码cid
		String phoneNum = this.getPhone1();
		String content = this.getContent1();
		content = this.getStr(content);
		phoneNum = this.getStr(phoneNum);
		if (phoneNum == null)
			phoneNum = "";
		StringBuffer nums = new StringBuffer();
		String ret = msgService.getNumsFromPhonebook(tvalue, phoneNum, nums);
		PageInfoModel pm = this.getModel();
		if (pm == null) {
			pm = new PageInfoModel();
		}
		String numss = nums.toString();
		pm.setBv(ret);//返回值
		pm.setBphone(numss);
		pm.setBcontent(content);
		this.setModel(pm);
		return SUCCESS;
	}
	
	public String getXX(){
		html=msgService.getHtml(id);
		return SUCCESS;
	}
	
	public String getPhones() {
		return phones;
	}
	
	public void setPhones(String phones) {
		this.phones = phones;
	}
	
	public boolean isIsview() {
		return isview;
	}

	public void setIsview(boolean isview) {
		this.isview = isview;
	}

	public String getOldcontent() {
		return oldcontent;
	}

	public void setOldcontent(String oldcontent) {
		this.oldcontent = oldcontent;
	}

	public String getOldphone() {
		return oldphone;
	}

	public void setOldphone(String oldphone) {
		this.oldphone = oldphone;
	}

	public String getTvalue() {
		return tvalue;
	}

	public void setTvalue(String tvalue) {
		this.tvalue = tvalue;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobilenum() {
		return mobilenum;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public void setMobilenum(String mobilenum) {
		this.mobilenum = mobilenum;
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

	public String getHidden_nums() {
		return hidden_nums;
	}

	public void setHidden_nums(String hidden_nums) {
		this.hidden_nums = hidden_nums;
	}

	public String getPhonebook_nums() {
		return phonebook_nums;
	}

	public void setPhonebook_nums(String phonebook_nums) {
		this.phonebook_nums = phonebook_nums;
	}

	public String getReturnvalue() {
		return returnvalue;
	}

	public void setReturnvalue(String returnvalue) {
		this.returnvalue = returnvalue;
	}

	public String getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(String unitprice) {
		this.unitprice = unitprice;
	}

	public String getMsglen() {
		return msglen;
	}

	public void setMsglen(String msglen) {
		this.msglen = msglen;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public MsgService getMsgService() {
		return msgService;
	}

	public void setMsgService(MsgService msgService) {
		this.msgService = msgService;
	}

	public PageInfoModel getModel() {
		return model;
	}

	public void setModel(PageInfoModel model) {
		this.model = model;
	}

	public String getContent1() {
		return content1;
	}

	public void setContent1(String content1) {
		this.content1 = content1;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getGroupname() {
		return groupname;
	}
	
	public void setGroupname(String groupname) {
		this.groupname = groupname;
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

	public String getStr2(String str) {

		if (null != str && !str.equals("")) {
			try {
				// 对JS中的中文参数进行解码
				str = java.net.URLDecoder.decode(str, "GBK");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			}
		}
		return str;
	}
	
	public String getRev() {
		return rev;
	}

	public void setRev(String rev) {
		this.rev = rev;
	}

	public String getContent11() {
		return content11;
	}

	public void setContent11(String content11) {
		this.content11 = content11;
	}

	public String getPhone11() {
		return phone11;
	}

	public void setPhone11(String phone11) {
		this.phone11 = phone11;
	}

	public String getContent12() {
		return content12;
	}

	public void setContent12(String content12) {
		this.content12 = content12;
	}

	public String getPhone12() {
		return phone12;
	}

	public void setPhone12(String phone12) {
		this.phone12 = phone12;
	}

	public String getSearchkey() {
		return searchkey;
	}

	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}
	public List<HashMap<String, Object>> getList() {
		return list;
	}
	public void setList(List<HashMap<String, Object>> list) {
		this.list = list;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
}
