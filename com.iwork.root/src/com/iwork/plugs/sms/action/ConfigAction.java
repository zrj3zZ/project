package com.iwork.plugs.sms.action;

import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.iwork.plugs.sms.service.ConfigService;
import com.iwork.plugs.sms.util.MsgConst;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
public class ConfigAction {
	private static Logger logger = Logger.getLogger(ConfigAction.class);
	MsgConst msgconst=new MsgConst();
	public ConfigService configService;
	private String typename1;
	private String parameterid;
	private String parameter;
	private String selectvalue;
	private String cid;
	private String id;//弹出修改小页面时
	private String keychange;
	private String valuechange;
	
	private String type;
	private String returnvalue2;
	public String getReturnvalue2() {
		return returnvalue2;
	}
	public void setReturnvalue2(String returnvalue2) {
		this.returnvalue2 = returnvalue2;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKeychange() {
		return keychange;
	}
	public void setKeychange(String keychange) {
		this.keychange = keychange;
	}
	public String getValuechange() {
		return valuechange;
	}
	public void setValuechange(String valuechange) {
		this.valuechange = valuechange;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public ConfigService getConfigService() {
		return configService;
	}
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
	public String getParameterid() {
		return parameterid;
	}
	public void setParameterid(String parameterid) {
		this.parameterid = parameterid;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getTypename1() {
		return typename1;
	}
	public void setTypename1(String typename1) {
		this.typename1 = typename1;
	}
	public String getSelectvalue() {
		return selectvalue;
	}
	public void setSelectvalue(String selectvalue) {
		this.selectvalue = selectvalue;
	}
	
	
	/**
	 * 链接系统设置
	 * @return
	 */
	public String loginconfig(){
		String type=configService.configall();
		Map request=(Map)ActionContext.getContext().get("request");
		request.put("type",type);
		request.put("srcd", "qconfig1.action");//先查(下拉框第一个的类型)
		request.put("returnvalue2", "");
		return "configadd";
	}
	/**
	 * 查询系统设置内容(下拉框第一个的类型)
	 * @return
	 */
	public String qconfig1(){
		String type1="DEFAULT_CHANNEL";
		String typelist=configService.qtypelist(type1);
		Map request=(Map)ActionContext.getContext().get("request");
		request.put("type1list",typelist);
		request.put("returnvalue", "");//增加参数时重复性返回值
		return "configaddxinxi";
	}
	/**
	 * 查询系统设置内容
	 * @return
	 */
	public String queryconfig(){
		String type2=this.getSelectvalue();
		String typelist=configService.qtypelist(type2);
		Map request=(Map)ActionContext.getContext().get("request");
		request.put("type1list",typelist);
		request.put("returnvalue", "");//增加参数时重复性返回值
		return "configaddxinxi";
	
	}
	/**
	 * 增加系统设置
	 * @return
	 */
	public String addconfig(){
		String type=this.getTypename1();
		String parid=this.getParameterid();
		String par=this.getParameter();
		String rvalue=configService.addconfig(type,parid,par);//重复性检查，有重复则返回值，无重复则返回""
		
		String typelist=configService.qtypelist(type);
		Map request=(Map)ActionContext.getContext().get("request");
		request.put("returnvalue", rvalue);
		request.put("type1list",typelist);
		return "configaddxinxi";
	}
/**
 * 删除系统设置条目
 * @return
 */	
	public String delnumc(){
		String cid=this.getCid();
		//String cid2 = ServletActionContext.getRequest().getParameter("cid");
		String type=configService.delnumc(cid);//删除条目
		
		String typelist=configService.qtypelist(type);
		Map request=(Map)ActionContext.getContext().get("request");
		request.put("type1list",typelist);
		request.put("returnvalue", "");
		return "configaddxinxi";
	}
	/**
	 * 系统设置修改修改号码弹出的小页面
	 * 
	 * @param id
	 * @param userid
	 * @return
	 */
	public void dataeditc() {
		StringBuffer sb = new StringBuffer();

		String sql = "from ConfigMst where cid='"+ id + "'";//ajax传过来的id
		Hashtable ht=configService.qdelnum(id);//根据id查询修改之前的类型，key和value
		String oldtype=(String)ht.get("type");
		String oldtypeshow = oldtype + "_NAME";
		String oldtype1 = msgconst.hsdb.get(oldtypeshow);
		String oldkey = (String)ht.get("key");
		String oldvalue =(String) ht.get("value");
		sb.append("<table width=98% align=center border=0 cellspacing=0 cellpadding=0>");
		sb.append("<tr><td>[<a  href='###' onclick=\"datasave('"
				+ id + "')\">保存</a>]<br>");
		sb.append("</td></tr></table>\n");
		sb.append("<br><table width=98% align=center border=0 cellspacing=0 cellpadding=0>");
		sb.append("<tr><td >类别：</td><td align=left>")
				.append("<input type='text' name='typechange' id=\"typechange\"  maxlength=10 size=10 value='"
								+ oldtype1 + "' disabled></td></tr>");
		sb
				.append("<tr><td >参数ID：</td><td align=left>")
				.append(
						"<input type='text' name='keychange' id=\"keychange\"  maxlength=10 size=10 value='"
								+ oldkey + "'></td></tr>");
		sb
				.append("<tr><td >参数值：</td><td align=left>")
				.append(
						"<input type='text' name='valuechange' id=\"valuechange\" class ='actionsoftInput'  maxlength=30 size=30 value='"
								+ oldvalue + "'></td></tr>");

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
	/**
	 *保存修改的条目
	 * @return
	 */
	public String saveeditnumc(){
		String cid=this.getCid();
		String keyedit=this.getKeychange();
		String valueedit=this.getValuechange();
		Hashtable ht=configService.qdelnum(cid);//根据cid查类型
		String type=(String)ht.get("type");
		String oldkey=(String)ht.get("key");
		String oldvalue=(String)ht.get("value");
		//重复性检查
       String rvalue=configService.editconfig(cid,type,oldkey,oldvalue,keyedit,valueedit);//重复性检查，有重复则返回值，无重复则返回""
		
      // String type3=configService.configall2(type);//下拉框中type被选中
		

       String typelist=configService.qtypelist(type);
		Map request=(Map)ActionContext.getContext().get("request");
		request.put("type1list",typelist);
		request.put("returnvalue", rvalue);
		return "configaddxinxi";
	}
	
}
