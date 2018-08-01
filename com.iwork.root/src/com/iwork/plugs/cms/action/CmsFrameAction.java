package com.iwork.plugs.cms.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.service.ZqbAnnouncementService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.cms.service.CmsFrameService;
import com.iwork.plugs.cms.util.CmsUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * CMS频道显示
 * 
 * @author WeiGuangjian
 * 
 */
public class CmsFrameAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private CmsFrameService CmsFrameService;
	private Long channelid;
	private String channelContent;
	private String columnName;
	private int columnPageNum;
	private String systemname;
	private String version;
	private final static String CN_FILENAME = "/common.properties";
	public String getVersion(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 获取CMS频道主页
	 * 
	 * @return
	 */
	public String index() {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		
		if (uc == null) { return ERROR;} //xlj 漏洞扫描 2018年5月15日11:08:39
		systemname = SystemConfig._iworkServerConf.getTitle();
		version=getVersion("version");
		if (channelid== null||channelid.equals(new Long(0))) {
			this.setChannelid(CmsFrameService.getTopChannelId());
		}
		channelContent = CmsFrameService.getChannelPage(channelid);

		if(this.getColumnName() != null){
			this.setColumnPageNum(CmsUtil.getColumnPageShowNum(columnName));
		}else{
			this.setColumnPageNum(0);
		}
		ZqbAnnouncementService zqbAnnouncementService=null;
		if(zqbAnnouncementService==null){
			zqbAnnouncementService = (ZqbAnnouncementService) SpringBeanUtil.getBean("zqbAnnouncementService");
		 }
		int size=0;
		OrgUser user = uc.get_userModel();
		 if(user.getOrgroleid()==3){
			 if(zqbAnnouncementService.getsygdt()!=null && zqbAnnouncementService.getsygdt().size()>0){
				    int n=1;
				    List<HashMap> data = zqbAnnouncementService.getsygdt();
				    for (HashMap hashMap : data) {
				    	if( hashMap.get("EXTEND5")!=null && hashMap.get("EXTEND5").equals("是") ){
				    		n++;
				    	}
				    }
					size=(n+1)*420;
				}
			 request.setAttribute("size", size);
			return "opendm";
		}else{
			if(CmsFrameService.getGzsc()!=null && CmsFrameService.getGzsc().size()>0){
				  int n=1;
				List<HashMap<String, Object>> questionList =CmsFrameService.getGzsc();
				for(HashMap<String, Object> model : questionList) {
					if(model.get("JSZT")!=null && model.get("JSZT").equals("是")){
						n++;
					}
				}
				size=(n+1)*420;
			}
			request.setAttribute("size", size);
			return SUCCESS;
		}
		
	}

	public CmsFrameService getCmsFrameService() {
		return CmsFrameService;
	}

	public void setCmsFrameService(CmsFrameService cmsFrameService) {
		CmsFrameService = cmsFrameService;
	}

	public String getSystemname() {
		return systemname;
	}

	public void setSystemname(String systemname) {
		this.systemname = systemname;
	}

	public Long getChannelid() {
		return channelid;
	}

	public void setChannelid(Long channelid) {
		this.channelid = channelid;
	}

	public String getChannelContent() {
		return channelContent;
	}

	public void setChannelContent(String channelContent) {
		this.channelContent = channelContent;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getColumnPageNum() {
		return columnPageNum;
	}

	public void setColumnPageNum(int columnPageNum) {
		this.columnPageNum = columnPageNum;
	}

}
