package com.iwork.app.navigation.favorite.web.action;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.iwork.app.navigation.favorite.web.service.SysFavFrameService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 收藏夹跳转类
 * @author WeiGuangjian
 *
 */
public class SysFavFrameAction extends ActionSupport{

	private SysFavFrameService sysFavFrameService;
	protected String favlist ; 
	private String html;
	private String pstrScript;
    protected String ifunname;
    protected String ifunurl;
    protected String itarget;
    protected String imemo;
    protected String funid;
    protected String funtext;
    protected String syslist;
    protected long sys_id;
    protected String sys_name;
    protected String sys_url;
    protected String sys_target;
    protected String sys_memo;
    protected long sys_index;
    protected String type;
    protected long isys_id;
    protected long isys_index;
	
	/**
	 * 获取收藏夹列表
	 * @return
	 * @throws Exception
	 */
    
	public String favjson()throws Exception{
		String json = "";	
		json = sysFavFrameService.getSysFavMenuJson(); 
		ResponseUtil.write(json);
		return null;	
	}
	
	/**
	 * 整理收藏夹
	 * @return
	 * @throws Exception
	 */
	public String myfav()throws Exception{	
		String html = "";		
		html = sysFavFrameService.getMyFav();  		
		this.setHtml(html);
		this.setPstrScript("");
		return SUCCESS;	
	}
	
	/**
	 * 我的收藏夹系统菜单
	 * @return
	 * @throws Exception
	 */
	public String sysfav()throws Exception{
		return null;	
	}
	
	/**
	 * 保存收藏夹
	 * @return
	 * @throws Exception
	 */
	public String savemyfav()throws Exception{	
		String pstrScript = "<script>$(function(){";	
		String msg = sysFavFrameService.saveMyFav(favlist); 
		if(msg!=null&&msg.equals("success")){		
			 pstrScript +="$.messager.confirm('系统提示', '保存成功，是否继续添加？', function(r){if (!r){window.parent.$('#iwindow').window('close');}});";//弹出提示
			 pstrScript +="window.parent.reloadfav();";//刷新首页左侧收藏夹列表	 
		}else{
			 pstrScript +="$.messager.alert('系统提示', '保存失败！', 'error');";//弹出提示		 
		}
		pstrScript +="});</script>";	
		String html = "";
		html = sysFavFrameService.getMyFav();  		
		this.setHtml(html);
		this.setPstrScript(pstrScript);	
		return SUCCESS;	
	}
	
    /**
     * 保存外部链接
     * @return
     * @throws Exception
     */
	public String saveurl()throws Exception{
		sysFavFrameService.saveMyFav(favlist); 
		String pstrScript = "<script>$(function(){";			
		String msg = sysFavFrameService.saveUrl(ifunname,ifunurl,itarget,imemo); 
		if(msg.equals("success")){		
			 pstrScript +="$.messager.alert('系统提示', '保存成功！', 'info');";//弹出提示
		}else{
			 pstrScript +="$.messager.alert('系统提示', '保存失败！', 'error');";//弹出提示		 
		}
		pstrScript +="});</script>";
		String html = "";
		html = sysFavFrameService.getMyFav();  		
		this.setHtml(html);
		this.setPstrScript(pstrScript);	
		return SUCCESS;	
	}
	
	/**
	 * 添加至收藏夹
	 * @return
	 * @throws Exception
	 */
	public String addfav()throws Exception{	
		String pstrScript = "<script>$(function(){";	
		String msg = sysFavFrameService.addFav(funid,funtext); 
		 if(msg==null||msg.equals("failure")){
			 pstrScript +="window.parent.$.messager.alert('系统提示', '添加至收藏夹失败！', 'error');";//弹出提示		 
		}else if(msg.equals("success")){		
			 pstrScript +="window.parent.$.messager.alert('系统提示', '已成功添加至收藏夹！', 'info');";//弹出提示
			 pstrScript +="window.parent.reloadfav();";//刷新首页左侧收藏夹列表	 
		}else{
			 pstrScript +="window.parent.$.messager.alert('系统提示', '此项在收藏夹中已存在！', 'error');";//弹出提示	
		}
		pstrScript +="});</script>";	
		this.setPstrScript(pstrScript);	
		return SUCCESS;	
	}
	
	/**
	 * 系统收藏夹维护
	 * @return
	 * @throws Exception
	 */
	public String consysfav()throws Exception{
		return SUCCESS;	
	}
	
	/**
	 * 加载系统收藏夹数据
	 * @return
	 * @throws Exception
	 */
	public String confav()throws Exception{
		String json = "";	
		json = sysFavFrameService.getSysFav(); 
		HttpServletRequest request = ServletActionContext.getRequest();	
		request.setAttribute("sysfavJson", json);
		return SUCCESS;	
	}
	
	/**
	 * 保存系统收藏夹
	 * @return
	 * @throws Exception
	 */
	public String savesysfav()throws Exception{	
		sysFavFrameService.saveSysFav(sys_id,sys_name,sys_url,sys_target,sys_memo,sys_index,type); 
		return SUCCESS;	
	}
	
	/**
	 * 排序
	 * @return
	 * @throws Exception
	 */
	public String move()throws Exception{
		sysFavFrameService.move(sys_id,sys_index,isys_id,isys_index);	
		return SUCCESS;	
    }
	
	public SysFavFrameService getSysFavFrameService() {
		return sysFavFrameService;
	}
	
	public void setSysFavFrameService(SysFavFrameService sysFavFrameService) {
		this.sysFavFrameService = sysFavFrameService;
	}

	public String getFavlist() {
		return favlist;
	}

	public void setFavlist(String favlist) {
		this.favlist = favlist;
	}
	
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getPstrScript() {
		return pstrScript;
	}

	public void setPstrScript(String pstrScript) {
		this.pstrScript = pstrScript;
	}

	public String getIfunname() {
		return ifunname;
	}

	public void setIfunname(String ifunname) {
		this.ifunname = ifunname;
	}

	public String getIfunurl() {
		return ifunurl;
	}

	public void setIfunurl(String ifunurl) {
		this.ifunurl = ifunurl;
	}

	public String getItarget() {
		return itarget;
	}

	public void setItarget(String itarget) {
		this.itarget = itarget;
	}

	public String getImemo() {
		return imemo;
	}

	public void setImemo(String imemo) {
		this.imemo = imemo;
	}

	public String getFunid() {
		return funid;
	}

	public void setFunid(String funid) {
		this.funid = funid;
	}

	public String getFuntext() {
		return funtext;
	}

	public void setFuntext(String funtext) {
		this.funtext = funtext;
	}

	public String getSyslist() {
		return syslist;
	}

	public void setSyslist(String syslist) {
		this.syslist = syslist;
	}

	public String getSys_name() {
		return sys_name;
	}

	public void setSys_name(String sys_name) {
		this.sys_name = sys_name;
	}

	public String getSys_url() {
		return sys_url;
	}

	public void setSys_url(String sys_url) {
		this.sys_url = sys_url;
	}

	public String getSys_target() {
		return sys_target;
	}

	public void setSys_target(String sys_target) {
		this.sys_target = sys_target;
	}

	public String getSys_memo() {
		return sys_memo;
	}

	public void setSys_memo(String sys_memo) {
		this.sys_memo = sys_memo;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSys_id() {
		return sys_id;
	}

	public void setSys_id(long sys_id) {
		this.sys_id = sys_id;
	}

	public long getSys_index() {
		return sys_index;
	}

	public void setSys_index(long sys_index) {
		this.sys_index = sys_index;
	}

	public long getIsys_id() {
		return isys_id;
	}

	public void setIsys_id(long isys_id) {
		this.isys_id = isys_id;
	}

	public long getIsys_index() {
		return isys_index;
	}

	public void setIsys_index(long isys_index) {
		this.isys_index = isys_index;
	}

	
}
