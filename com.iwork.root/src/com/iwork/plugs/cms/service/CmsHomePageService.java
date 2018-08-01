package com.iwork.plugs.cms.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import news.common.HelloWebService;

import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.service.ZqbAnnouncementService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.navigation.web.service.SysNavFrameService;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.km.know.model.IworkKnowQuestion;
import com.iwork.plugs.cms.dao.CmsInfoDAO;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.util.CmsCommentUtil;
import com.iwork.plugs.cms.util.CmsUtil;
import com.iwork.plugs.email.model.MailTaskModel;
import com.iwork.plugs.email.service.RecIWorkMailClientService;
import com.iwork.process.desk.handle.model.TodoTaskModel;
import com.iwork.process.desk.handle.service.ProcessDeskService;
/**
 * 首页Service
 * 处理首页邮件及待办异步加载
 * @author zouyalei
 *
 */
public class CmsHomePageService {

	private static Logger logger = Logger.getLogger(CmsHomePageService.class);
	private RecIWorkMailClientService recIWorkMailClientService;
	private ProcessDeskService processDeskService;
	private SysNavFrameService SysNavFrameService;
	private CmsInfoDAO cmsInfoDAO;
	 
	private final int limit=10;//最多显示条数
	private final int maxLength = 35;//最大长度
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	
	
	public Long getFirstChannelid(){
		Long id = new Long(0);
		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		List<IworkCmsPortlet> cmsPortlet = cmsInfoDAO.getOpenPortletList();
		if(cmsPortlet!=null&&cmsPortlet.size()>0){
			id = cmsPortlet.get(0).getId();
		}else{
			id = 9999L;
		}
		return id;
	}
	/**
	 * 初始化  董秘行业动态tab
	 * @return
	 */
	public String getHydtTab(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("<li class=\"active\">\n<a data-toggle=\"tab\" href=\"#TZGG\" channelid=\"0\" onclick=\"changeHomeTabdm(0)\">市场新闻</a>\n</li>\n");
		sb.append("<li >\n<a data-toggle=\"tab\" href=\"#cxddxw\" channelid=\"14\" onclick=\"changeHomeTabdm(14)\">企业舆情</a>\n</li>\n");
		return sb.toString();
	}
	/**
	 * 获得信息咨询tab
	 * @return
	 */
	public String getHomeTab(){
		StringBuffer sb = new StringBuffer();
		int i = 0;
		
		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		List<IworkCmsPortlet> cmsPortlet = cmsInfoDAO.getOpenPortletList();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		/*if(user.getOrgroleid()==5){
			if(cmsPortlet!=null&&cmsPortlet.size()>0){
				for (IworkCmsPortlet model : cmsPortlet) {
					sb.append("  <li class=\"active\">").append("\n");
					sb.append("    <a data-toggle=\"tab\" href=\"#").append(model.getPortletkey()).append("\" channelid=\"").append(model.getId()).append("\" onclick=\"changeHomeTab(").append(model.getId()).append(")\" >").append(model.getPortletname()).append("</a>").append("\n");
					sb.append("  </li>").append("\n");
					i++;
				}
			}
			sb.append("<li >\n<a data-toggle=\"tab\" href=\"#cxddxw\" channelid=\"14\" onclick=\"changeHomeTab(14)\">企业舆情</a>\n</li>\n");
			sb.append("<li >\n<a data-toggle=\"tab\" href=\"#NewsInfo\" channelid=\"17\" onclick=\"changeHomeTab(17)\">通知公告</a>\n</li>\n");
		} else{*/
		if(user.getOrgroleid()==3){
			if(cmsPortlet!=null&&cmsPortlet.size()>0){
				for (IworkCmsPortlet model : cmsPortlet) {
					if(i==0){
						sb.append("  <li class=\"active\">").append("\n");
					}else{
						sb.append("  <li>").append("\n");
					}
					sb.append("    <a data-toggle=\"tab\" href=\"#").append(model.getPortletkey()).append("\" channelid=\"").append(model.getId()).append("\" onclick=\"changeHomeTab(").append(model.getId()).append(")\" >").append(model.getPortletname()).append("</a>").append("\n");
					sb.append("  </li>").append("\n");
					i++;
				}
			}
			
			sb.append("<li >\n<a data-toggle=\"tab\" href=\"#finish\" onclick=\"changeDBSY(1)\">会议计划(<font color=\"red\"><span id=\"hycount\"></span></font>)</a>\n</li>\n");
		}else{
			sb.append("<li class=\"active\">\n<a data-toggle=\"tab\" href=\"#question\" onclick=\"changeDBSY(2)\">部门通知(<font color=\"red\"><span id=\"gzsccount\"></span></font>)</a>\n</li>\n");
			sb.append("<li >\n<a data-toggle=\"tab\" href=\"#finish\" onclick=\"changeDBSY(1)\">会议计划(<font color=\"red\"><span id=\"hycount\"></span></font>)</a>\n</li>\n");
		}
			
		/*}*/
		return sb.toString();
	}
	/**
	 * 初始化滚动信息
	 * @return
	 */
	public String showGdtDiv1(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		StringBuffer sb = new StringBuffer();
		List list=new ArrayList();
		
		list.add("id");
		list.add("tzbt");
		list.add("fssj");
		list.add("tzlx");
		String sql="select s.id,s.tzbt,s.fssj from bd_xp_tzggb s,s.tzlx left join bd_xp_hfqkb t on s.id=t.ggid where t.userid='"+uc.get_userModel().getUserid()+"' and t.status='未回复' order by s.fssj desc";
		List<HashMap> dataList = DBUTilNew.getDataList(list, sql, null);
		sb.append(" <ul> ");
		int tag = 0;
		if(dataList!=null && dataList.size()>0){
			for (int i = 0; i < dataList.size(); i++) {
				String tzlx = dataList.get(i).get("tzlx")==null?"":dataList.get(i).get("tzlx").toString();
				String tzbt =  dataList.get(i).get("tzbt").toString();
				tag = tzbt.startsWith("持续督导日常工作反馈(")?1:tzbt.startsWith("挂牌公司重大事项信息披露自查反馈表(")?2:3;
				sb.append(" <li class=\"notice_active_ch\">");
				sb.append(" <span>"+dataList.get(i).get("tzbt")+"</span>     <em>"+dataList.get(i).get("fssj")+"</em>");
				sb.append(" </li >");
			}
		}
		sb.append(" </ul> ");
		return sb.toString();
	}
	/*public String showFDMGdtDiv(){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long roleId=user.getOrgroleid();
		int pagesize=10;
		if(roleId==3) pagesize=3;
		StringBuffer sb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		int line = 0;
		int rows = 0;
		sql.append("<ul class=\"scroll\" >").append("\n");
		//sb.append("<table width=\"99%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed;\">").append("\n");
		
		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		
		// 根据type待办事宜
		List<HashMap<String, Object>> questionList = null;
		if(SysNavFrameService==null)
			SysNavFrameService = (SysNavFrameService)SpringBeanUtil.getBean("sysNavFrameService");
		
		questionList = SysNavFrameService.getGzsc(1,100);
		String gzscformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查'", "FORMID");
		String gzscdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查'", "ID");
		String gzschfformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查回复'", "FORMID");
		String gzschfdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查回复'", "ID");
		if(questionList.size()>0){
			
			for(HashMap<String, Object> model : questionList) {
				if(model.get("JSZT")!=null && model.get("JSZT").equals("是")){
					sb.append("    <li  class=\"notice_active_ch\" >");
					sb.append("	<a  href=\"javascript:updateGzsc('"+gzscformid+"','"+gzscdemid+"','"+model.get("CID")+"','"+model.get("DID")+"','"+model.get("INSTANCEID")+"')\">"+model.get("TZBT")+" &nbsp;&nbsp; <lable style=\"font-size:12px\">"+model.get("FSSJ").toString().substring(0,10)+"</lable></a>\n");
					sb.append("   </li> ");
					if (line == rows - 1) {
						line++;
						break;
					}
					line++;
				}
			}
		}
		sb.append("</ul>").append("\n");
		if(line!=0){
			sql.append(" <li class=\"notice_active_ch\" style=\"text-align:right;width:150px;\">重要通知：");
			sql.append(" </li>");
			sql.append(sb);
		}else sql.append(sb);
		
			
		return sql.toString();
	}*/
    public String showFDMGdtDiv(){
        OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
        Long roleId=user.getOrgroleid();
        int pagesize=10;
        if(roleId==3) pagesize=3;
        StringBuffer sb = new StringBuffer();
        StringBuffer sql = new StringBuffer();
        int line = 0;
        int rows = 0;

        if (cmsInfoDAO == null) {
            cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
        }

        // 根据type待办事宜
        List<HashMap<String, Object>> questionList = null;
        if(SysNavFrameService==null)
            SysNavFrameService = (SysNavFrameService)SpringBeanUtil.getBean("sysNavFrameService");

        questionList = SysNavFrameService.getGzsc(1,100);
        String gzscformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查'", "FORMID");
        String gzscdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查'", "ID");
        String gzschfformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查回复'", "FORMID");
        String gzschfdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查回复'", "ID");
        if(questionList.size()>0){

            for(HashMap<String, Object> model : questionList) {
                if(model.get("JSZT")!=null && model.get("JSZT").equals("是")){
                    sb.append("  <span style=\"margin-right:100px;\"> ");
                    String insid=DBUTilNew.getDataStr("instanceid","select instanceid from sys_engine_form_bind where formid="+gzschfformid+" and dataid=(select id from BD_XP_GZBCHF where cid="+model.get("CID")+")",null);

                   sb.append("	<a  href=\"javascript:updateGzsc1('"+gzschfformid+"','"+gzschfdemid+"','"+model.get("CID")+"','"+model.get("DID")+"',"+insid+")\">"+model.get("TZBT")+" &nbsp;&nbsp; <lable style=\"font-size:12px\">"+model.get("FSSJ").toString().substring(0,10)+"</lable></a>\n");

                   // sb.append("	<a  href=\"javascript:updateGzsc('"+gzscformid+"','"+gzscdemid+"','"+model.get("CID")+"','"+model.get("DID")+"','"+model.get("INSTANCEID")+"')\">"+model.get("TZBT")+" &nbsp;&nbsp; <lable style=\"font-size:12px\">"+model.get("FSSJ").toString().substring(0,10)+"</lable></a>\n");

                    sb.append("   </span> ");
                    if (line == rows - 1) {
                        line++;
                        break;
                    }
                    line++;
                }
            }
        }
        if(line!=0){
            sql.append(" <span  style=\"text-align:right;width:150px;\">重要通知：");
            sql.append(" </span>");
            sql.append(sb);
        }else sql.append(sb);


        return sql.toString();
    }
	/*public String showGdtDiv(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser us = uc.get_userModel();
		StringBuffer sb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		Long wjformid = Long.parseLong(config.get("wjformid")) ;
		Long wjdemid = Long.parseLong(config.get("wjdemid"));
		Long hfxxbformid = Long.parseLong(config.get("hfxxbformid")) ;
		Long hfxxbdemid = Long.parseLong(config.get("hfxxbdemid"));
		sql.append(" <ul  class=\"scroll\">   ");
		
		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		// 查询公告信息
		List<IworkCmsContent> cmsInfoList;
		ZqbAnnouncementService zqbAnnouncementService=null;
		if(zqbAnnouncementService==null){
			zqbAnnouncementService = (ZqbAnnouncementService) SpringBeanUtil.getBean("zqbAnnouncementService");
		 }
		List<HashMap> data = zqbAnnouncementService.getsygdt();
		if( config.get("zqServer").equals("hlzq")){
			com.ibpmsoft.project.zqb.service.ZqbProjectManageService zqbProjectManageService=null;
			if(zqbProjectManageService==null){
				zqbProjectManageService = (com.ibpmsoft.project.zqb.service.ZqbProjectManageService) SpringBeanUtil.getBean("zqbProjectManageService");
			 }
			List<HashMap> data2 = config.get("zqServer").equals("hlzq")?zqbProjectManageService.showDailyListForIndex(null, 1, 10, null, null, zqbProjectManageService.getConfig("xmrbformid"), null):new ArrayList();
			data.addAll(data2);
		}
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		String khbh = null;
		if(user.getOrgroleid()==3l){
			khbh = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1().toUpperCase();
		}
		int tag = 0;
		
		for (HashMap hashMap : data) {
			if( hashMap.get("EXTEND5")!=null && hashMap.get("EXTEND5").equals("是") ){
			String tzbt = hashMap.get("TZBT").toString();
			String tzlx = hashMap.get("TZLX")==null?"":hashMap.get("TZLX").toString();
			tag = tzbt.startsWith("持续督导日常工作反馈(")?1:tzbt.startsWith("挂牌公司重大事项信息披露自查反馈表(")?2:3;
			sb.append(" <li class=\"notice_active_ch\">");
			if(tag==1){
				//sb.append(" <span>"+tzbt+"</span>     <em>"+hashMap.get("FSSJ")+"</em>");// onClick='showCxddfkgzxx(").append(hashMap.get("ID")).append(");'
				sb.append(" <span>");
				sb.append("<a href=\"javascript:addCustomer('").append(hashMap.get("TZNR")).append("','").append(hashMap.get("ID")).append("','").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" title='").append(tzbt).append("'>").append("").append(tzbt+"&nbsp;&nbsp; <lable style=\"font-size:12px\">"+hashMap.get("FSSJ")+"</lable>").append("</a>").append("\n");
				sb.append("</span>     ");
			}else if(tag==2){
				//sb.append(" <span>"+tzbt+"</span>     <em>"+hashMap.get("FSSJ")+"</em>");// onClick='showCxddfkgzxx(").append(hashMap.get("ID")).append(");'
				sb.append(" <span>");
				sb.append("<a href=\"#\" onClick='showCustomer(\"").append(khbh).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("\");' title='").append(tzbt).append("'>").append(tzbt+"&nbsp;&nbsp; <lable style=\"font-size:12px\">"+hashMap.get("FSSJ")+"</lable>").append("</a>").append("\n");
				sb.append(" </span>    ");
			}else if(tzlx.equals("问卷调查")){
				//sb.append(" <span>"+tzbt+"</span>     <em>"+hashMap.get("FSSJ")+"</em>");// onClick='showCxddfkgzxx(").append(hashMap.get("ID")).append(");'
				sb.append(" <span>");
					sb.append("<a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"")
					.append(wjformid).append("\",\"")
					.append(wjdemid).append("\",\"")
					.append(hashMap.get("INSTANCEID")).append("\",\"")
					.append(hashMap.get("ID")).append("\",\"")
					.append(hashMap.get("HFID")).append("\")' title='")
					.append(tzbt).append("'>").append("").append(tzbt+" &nbsp;&nbsp; <lable style=\"font-size:12px\">"+hashMap.get("FSSJ")+"</lable>").append("</a>");
					sb.append("</span>    ");
			}else if(tzlx.equals("项目日志")){
				//sb.append(" <span>"+tzbt+"</span>     <em>"+hashMap.get("FSSJ")+"</em>");// onClick='showCxddfkgzxx(").append(hashMap.get("ID")).append(");'
				sb.append(" <span>");
				sb.append("<a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='checkDaily(").append(hashMap.get("SFTZ").toString()).append(")' title='").append(tzbt).append("'>").append("").append(tzbt+" &nbsp;&nbsp; <lable style=\"font-size:12px\">"+hashMap.get("FSSJ")+"</lable> ").append("</a>");
				sb.append("</span>    ");
			}else{
				sb.append(" <span>");
				sb.append("<a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt+"&nbsp;&nbsp; <lable style=\"font-size:12px\">"+hashMap.get("FSSJ")+"</lable>").append("</a>");
				sb.append("</span>     ");
				// onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>");
			}
			sb.append(" </li> ");
			}
		}
		sb.append(" <ul> ");
		if(tag!=0){
			sql.append(" <li class=\"notice_active_ch\" style=\"text-align:right;width:150px;\">重要通知：");
			sql.append(" </li>");
			sql.append(sb);
		}else{
			sql.append(sb);
		}
		return sql.toString();
	}*/
	public String showGdtDiv(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser us = uc.get_userModel();
		StringBuffer sb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		Long wjformid = Long.parseLong(config.get("wjformid")) ;
		Long wjdemid = Long.parseLong(config.get("wjdemid"));
		Long hfxxbformid = Long.parseLong(config.get("hfxxbformid")) ;
		Long hfxxbdemid = Long.parseLong(config.get("hfxxbdemid"));

		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		// 查询公告信息
		List<IworkCmsContent> cmsInfoList;
		ZqbAnnouncementService zqbAnnouncementService=null;
		if(zqbAnnouncementService==null){
			zqbAnnouncementService = (ZqbAnnouncementService) SpringBeanUtil.getBean("zqbAnnouncementService");
		}
		List<HashMap> data = zqbAnnouncementService.getsygdt();
		if( config.get("zqServer").equals("hlzq")){
			com.ibpmsoft.project.zqb.service.ZqbProjectManageService zqbProjectManageService=null;
			if(zqbProjectManageService==null){
				zqbProjectManageService = (com.ibpmsoft.project.zqb.service.ZqbProjectManageService) SpringBeanUtil.getBean("zqbProjectManageService");
			}
			List<HashMap> data2 = config.get("zqServer").equals("hlzq")?zqbProjectManageService.showDailyListForIndex(null, 1, 10, null, null, zqbProjectManageService.getConfig("xmrbformid"), null):new ArrayList();
			data.addAll(data2);
		}
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		String khbh = null;
		if(user.getOrgroleid()==3l){
			khbh = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1().toUpperCase();
		}
		int tag = 0;

		for (HashMap hashMap : data) {
			if( hashMap.get("EXTEND5")!=null && hashMap.get("EXTEND5").equals("是") ){
				String tzbt = hashMap.get("TZBT").toString();
				String tzlx = hashMap.get("TZLX")==null?"":hashMap.get("TZLX").toString();
				tag = tzbt.startsWith("持续督导日常工作反馈(")?1:tzbt.startsWith("挂牌公司重大事项信息披露自查反馈表(")?2:3;
				if(tag==1){
					sb.append(" <span style=\"margin-right:100px;\">");
					sb.append("<a href=\"javascript:addCustomer('").append(hashMap.get("TZNR")).append("','").append(hashMap.get("ID")).append("','").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" title='").append(tzbt).append("'>").append("").append(tzbt+"&nbsp;&nbsp; <lable style=\"font-size:12px\">"+hashMap.get("FSSJ")+"</lable>").append("</a>").append("\n");
					sb.append("</span>     ");
				}else if(tag==2){
					sb.append(" <span style=\"margin-right:100px;\">");
					sb.append("<a href=\"#\" onClick='showCustomer(\"").append(khbh).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("\");' title='").append(tzbt).append("'>").append(tzbt+"&nbsp;&nbsp; <lable style=\"font-size:12px\">"+hashMap.get("FSSJ")+"</lable>").append("</a>").append("\n");
					sb.append(" </span>    ");
				}else if(tzlx.equals("问卷调查")){
					sb.append(" <span style=\"margin-right:100px;\">");
					sb.append("<a href = '#' style=\"display:inline-block;\" onclick='addTalk(\"")
							.append(wjformid).append("\",\"")
							.append(wjdemid).append("\",\"")
							.append(hashMap.get("INSTANCEID")).append("\",\"")
							.append(hashMap.get("ID")).append("\",\"")
							.append(hashMap.get("HFID")).append("\")' title='")
							.append(tzbt).append("'>").append("").append(tzbt+" &nbsp;&nbsp; <lable style=\"font-size:12px\">"+hashMap.get("FSSJ")+"</lable>").append("</a>");
					sb.append("</span>    ");
				}else if(tzlx.equals("项目日志")){
					sb.append(" <span style=\"margin-right:100px;\">");
					sb.append("<a href = '#' style=\"display:inline-block;\" onclick='checkDaily(").append(hashMap.get("SFTZ").toString()).append(")' title='").append(tzbt).append("'>").append("").append(tzbt+" &nbsp;&nbsp; <lable style=\"font-size:12px\">"+hashMap.get("FSSJ")+"</lable> ").append("</a>");
					sb.append("</span>    ");
				}else{
					sb.append(" <span style=\"margin-right:100px;\">");
					sb.append("<a href = '#' style=\"display:inline-block;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt+"&nbsp;&nbsp; <lable style=\"font-size:12px\">"+hashMap.get("FSSJ")+"</lable>").append("</a>");
					sb.append("</span>     ");
				}
			}
		}
		if(tag!=0){
			sql.append(" <span style=\"width:150px;\">重要通知：");
			sql.append(" </span>");
			sql.append(sb);
		}else{
			sql.append(sb);
		}
		return sql.toString();
	}
	/**
	 * 获取公告信息
	 * @return
	 */
	public String getHomeInfoHtml(long id){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser us = uc.get_userModel();
		Long roId=us.getOrgroleid();
		StringBuffer sb = new StringBuffer();
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		Long hfxxbformid = Long.parseLong(config.get("hfxxbformid")) ;
		Long hfxxbdemid = Long.parseLong(config.get("hfxxbdemid"));
		
		Long wjformid = Long.parseLong(config.get("wjformid")) ;
		Long wjdemid = Long.parseLong(config.get("wjdemid"));
		int pagesize=3;
	//	if(roId==3) pagesize=3;
		int line = 0;
		int rows = 0;
		String tab="tab1";
		if(id==17l){
			 tab="tab9";
		}
		sb.append("<table id=\""+tab+"\" width=\"100%\" class=\"table\" style=\"font-size:15px;\" >").append("\n");
	
		
		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		
		// 查询公告信息
		List<IworkCmsContent> cmsInfoList;
		if(id==17l){
			sb.append("  <col width=\"50%\" />").append("\n");
			sb.append("  <col width=\"10%\" />").append("\n");
			sb.append("  <col width=\"10%\" />").append("\n");
			sb.append("  <col width=\"10%\" />").append("\n");
			sb.append("  <col width=\"20%\" />").append("\n");
			ZqbAnnouncementService zqbAnnouncementService=null;
			if(zqbAnnouncementService==null){
				zqbAnnouncementService = (ZqbAnnouncementService) SpringBeanUtil.getBean("zqbAnnouncementService");
			 }
			List<HashMap> data = zqbAnnouncementService.getTZGGReceptionList(null,null,1,pagesize);
			if( config.get("zqServer").equals("hlzq")){
				com.ibpmsoft.project.zqb.service.ZqbProjectManageService zqbProjectManageService=null;
				if(zqbProjectManageService==null){
					zqbProjectManageService = (com.ibpmsoft.project.zqb.service.ZqbProjectManageService) SpringBeanUtil.getBean("zqbProjectManageService");
				 }
				List<HashMap> data2 = config.get("zqServer").equals("hlzq")?zqbProjectManageService.showDailyListForIndex(null, 1, 10, null, null, zqbProjectManageService.getConfig("xmrbformid"), null):new ArrayList();
				data.addAll(data2);
			}
			
			OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
			String khbh = null;
			
			if(user.getOrgroleid()==3l){
				khbh = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1().toUpperCase();
			}
			int tag = 0;
			for (HashMap hashMap : data) {
				String sqls="select extend2,status from Bd_Xp_Hfqkb where ggid= ? and userid= ? ";
				Map params = new HashMap<Integer,HashMap>();
				params.put(1, hashMap.get("ID"));
				params.put(2, user.getUserid());
				List lables = new ArrayList();
				lables.add("extend2");
				lables.add("status");
		
				List mobilelist = com.iwork.commons.util.DBUtil.getDataList(lables, sqls, params);
				String extend2="";
				String status="";
				if(mobilelist.size()>0){
					extend2 = ((HashMap)mobilelist.get(0)).get("extend2")==null?"":((HashMap)mobilelist.get(0)).get("extend2").toString();
					status =((HashMap)mobilelist.get(0)).get("status").toString();
				}
				String tzbt = hashMap.get("TZBT").toString();
				String tzlx = hashMap.get("TZLX")==null?"":hashMap.get("TZLX").toString();
				tag = tzbt.startsWith("持续督导日常工作反馈(")?1:tzbt.startsWith("挂牌公司重大事项信息披露自查反馈表(")?2:3;
				sb.append("  <tr>").append("\n");
				if(hashMap.get("JSZT").equals("已回复")&&(hashMap.get("STATUS")==null||hashMap.get("STATUS").equals(""))){
					if(tag==1){
						sb.append(" <td><a href=\"#\" onClick='showCxddfkgzxx(").append(hashMap.get("ID")).append(");' title='").append(tzbt).append("'>").append(tzbt).append("</a></td>").append("\n");
						sb.append("<td></td><td>已反馈</td><td></td>");
					}else if(tag==2){
						sb.append("<td><a href=\"#\" onClick='showCustomer(\"").append(khbh).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("\");' title='").append(tzbt).append("'>").append(tzbt).append("</a></td>").append("\n");
						sb.append("<td><a href=\"javascript:expWord('").append(khbh).append("',").append(hashMap.get("ID")).append(",'").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" >导出</a></td>");
						sb.append("<td>已反馈</td><td>(<a href=\"javascript:showCommunicate('").append(khbh).append("',").append(hashMap.get("ID")).append(")\" style=\"color:blue;\">确认沟通</a>)</td>");
					}else if(tzlx.equals("问卷调查")){
						if(hashMap.get("JSZT")!=null&&!hashMap.get("JSZT").equals("")&&hashMap.get("JSZT").toString().equals("已回复")){
							sb.append("<td><a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='showWJDC(\"").append(hashMap.get("ID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
							if(!status.equals("未回复")){
								sb.append("<td></td><td>"+status+"</td>");
								if(extend2!=""){
									sb.append("<td>("+extend2+"已阅)</td>");
								}else sb.append("<td></td>");
							}else{
								sb.append("<td></td><td></td><td></td>");
							}	
						
							sb.append("\n");
						}else{
							sb.append("<td><a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"")
							.append(wjformid).append("\",\"")
							.append(wjdemid).append("\",\"")
							.append(hashMap.get("INSTANCEID")).append("\",\"")
							.append(hashMap.get("ID")).append("\",\"")
							.append(hashMap.get("HFID")).append("\")' title='")
							.append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
							if(!status.equals("未回复")){
								sb.append("<td></td><td>"+status+"</td>");
								if(extend2!=""){
									sb.append("<td>("+extend2+"已阅)</td>");
								}else sb.append("<td></td>");
							}else{
								sb.append("<td></td><td></td><td></td>");
							}
							sb.append("\n");
						}
					}else{
						sb.append("<td><a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
						if(!status.equals("未回复")){
							sb.append("<td></td><td>"+status+"</td>");
							if(extend2!=""){
								sb.append("<td>("+extend2+"已阅)</td>");
							}else sb.append("<td></td>");
						}else{
							sb.append("<td></td><td></td><td></td>");
						}	
					
						sb.append("\n");
					}
				}else if(hashMap.get("JSZT").equals("已回复")&&(hashMap.get("STATUS")!=null||hashMap.get("STATUS").equals("已确认沟通"))){
					if(tag==1){
						sb.append("<td><a href=\"#\" onClick='showCxddfkgzxx(").append(hashMap.get("ID")).append(");' title='").append(tzbt).append("'>").append(tzbt).append("</a></td>").append("\n");
						sb.append("<td></td><td>已反馈</td><td></td>");
					}else if(tag==2){
						sb.append("<td><a href=\"#\" onClick='showCustomer(\"").append(khbh).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("\");' title='").append(tzbt).append("'>").append(tzbt).append("</a></td>").append("\n");
						sb.append("<td><a href=\"javascript:expWord('").append(khbh).append("',").append(hashMap.get("ID")).append(",'").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" >导出</a></td>");
						sb.append("<td>已反馈</td><td>(<a href=\"javascript:updateCommunicate('").append(khbh).append("',").append(hashMap.get("ID")).append(")\" style=\"color:blue;\">已确认沟通</a>)</td>");
					}else if(tzlx.equals("问卷调查")){
						if(hashMap.get("JSZT")!=null&&!hashMap.get("JSZT").equals("")&&hashMap.get("JSZT").toString().equals("已回复")){
							sb.append("<td><a href = '#' onclick='showWJDC(\"").append(hashMap.get("ID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td><td></td><td></td><td></td>").append("\n");
						}else{
							sb.append("<td><a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"")
							.append(wjformid).append("\",\"")
							.append(wjdemid).append("\",\"")
							.append(hashMap.get("INSTANCEID")).append("\",\"")
							.append(hashMap.get("ID")).append("\",\"")
							.append(hashMap.get("HFID")).append("\")' title='")
							.append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
							if(!status.equals("未回复")){
								sb.append("<td></td><td>"+status+"</td>");
								if(extend2!=""){
									sb.append("<td>("+extend2+"已阅)</td>");
								}else sb.append("<td></td>");
							}else{
								sb.append("<td></td><td></td><td></td>");
							}	
							sb.append("\n");
						}
					}else{
						sb.append("<td><a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
						if(!status.equals("未回复")){
							sb.append("<td></td><td>"+status+"</td>");
							if(extend2!=""){
								sb.append("<td>("+extend2+"已阅)</td>");
							}else sb.append("<td></td>");
						}else{
							sb.append("<td></td><td></td><td></td>");
						}	
						sb.append("\n");
					}
				}else{
					if(tag==1){
						sb.append("<td><a href=\"javascript:addCustomer('").append(hashMap.get("TZNR")).append("','").append(hashMap.get("ID")).append("','").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>").append("\n");
						sb.append("<td></td><td>未反馈</td><td></td>");
					}else if(tag==2){
						sb.append("<td><a href=\"#\" onClick='showCustomer(\"").append(khbh).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("\");' title='").append(tzbt).append("'>").append(tzbt).append("</a></td>").append("\n");
						sb.append("<td><a href=\"javascript:expWord('").append(khbh).append("',").append(hashMap.get("ID")).append(",'").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" style=\"color:blue;\">导出</a></td>");
						sb.append("<td>未反馈</td><td></td>");
					}else if(tzlx.equals("问卷调查")){
						if(hashMap.get("JSZT")!=null&&!hashMap.get("JSZT").equals("")&&hashMap.get("JSZT").toString().equals("已回复")){
							sb.append("<td><a href = '#' onclick='showWJDC(\"").append(hashMap.get("ID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a><td><td></td><td></td><td></td>").append("\n");
						}else{
							sb.append("<td><a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"")
							.append(wjformid).append("\",\"")
							.append(wjdemid).append("\",\"")
							.append(hashMap.get("INSTANCEID")).append("\",\"")
							.append(hashMap.get("ID")).append("\",\"")
							.append(hashMap.get("HFID")).append("\")' title='")
							.append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
							if(!status.equals("未回复")){
								sb.append("<td></td><td>"+status+"</td>");
								if(extend2!=""){
									sb.append("<td>("+extend2+"已阅)</td>");
								}else sb.append("<td></td>");
							}else{
								sb.append("<td></td><td></td><td></td>");
							}	
							sb.append("\n");
						}
					}else if(tzlx.equals("项目日志")){
						sb.append("<td><a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='checkDaily(").append(hashMap.get("SFTZ").toString()).append(")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td><td></td><td></td><td></td>");
						sb.append("\n");
					}else{
						sb.append("<td><a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
						if(!status.equals("未回复")){
							sb.append("<td></td><td>"+status+"</td>");
							if(extend2!=""){
								sb.append("<td>("+extend2+"已阅)</td>");
							}else sb.append("<td></td>");
						}else{
							sb.append("<td></td><td></td><td></td>");
						}	
						sb.append("\n");
					}
				}
				sb.append("  <td>").append("\n");
				sb.append("    <span style=\"float:right\">").append(hashMap.get("FSSJ")).append("</span>").append("\n");
				sb.append("  </td>").append("\n");
				sb.append("  </tr>").append("\n");
			}
		}else if(id==14l){
			sb.append("  <col width=\"100%\" />").append("\n");
			/*sb.append("  <col width=\"10%\" />").append("\n");
			sb.append("  <col width=\"10%\" />").append("\n");
			sb.append("  <col width=\"10%\" />").append("\n");
			sb.append("  <col width=\"20%\" />").append("\n");*/
			SimpleDateFormat sd = new SimpleDateFormat("yy-MM-dd HH:mm");
			Long roleId = us.getOrgroleid();
			StringBuffer sql=new StringBuffer();
			if(roleId==3){
				String gsmc = us.getDepartmentname();
				String customerno = us.getExtend1();
				SimpleDateFormat sdb = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MONTH, -1);
				String times = sdb.format(calendar.getTime());
				sql.append(" TIMES>='").append(times).append("'");
				String zqdm = getDMZqdm(customerno, gsmc);
				if(zqdm!=null&&!zqdm.equals("")){
					sql.append(" AND TYPE=").append(zqdm).append("");
				}
			}else{
				SimpleDateFormat sdb = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MONTH, -1);
				String times = sdb.format(calendar.getTime());
				sql.append(" TIMES>='").append(times).append("'");
				
				String cuid = us.getUserid();
				Long roleid = us.getOrgroleid();
				String zqdm = getCustomerZqdm(cuid, null, roleid);
				if(zqdm!=null&&!zqdm.equals("")){
					sql.append(" AND TYPE IN(").append(zqdm).append(")");
				}
			}
			try {
				List<news.service.Cxdd> data = HelloWebService.getCxdd(1, pagesize, sql.toString());
				String title;
				String title_;
				String href;
				String times;
				String gsmc;
				String gfdm;
				for (news.service.Cxdd cxdd : data) {
					sb.append("  <tr>").append("\n");
					title = cxdd.getTitle().getValue().replaceAll("<span class=\"c_keywordcolor\" style=\"color:red\">|</span>", "");
					title = (title.length()>=20?title.substring(0, 20):title)+"...";
					href = cxdd.getHref().getValue();
					times = cxdd.getTimes().getValue().substring(0, 16);
					gsmc = cxdd.getGsmc().getValue();
					gfdm = cxdd.getGfdm().getValue();
					sb.append("  <td>\n");
					sb.append("    <a target=\"_blank\" href='").append(href).append("'\" title='").append(title).append("'>").append(title.toString()).append("</a>\n");
					sb.append("    <span style=\"float:right;width:140px;\">").append(times).append("</span>\n");
					sb.append("    <span style=\"float:right;width:80px;\">").append(gsmc).append("</span>\n");
					sb.append("    <span style=\"float:right;width:80px;\">").append(gfdm).append("</span>\n");
					sb.append("  </td>\n");
					sb.append("  </tr>").append("\n");
				}
			} catch (Exception e) {
			}
		}else if(id==0l){
			sb.append("  <col width=\"80%\" />").append("\n");
			sb.append("  <col width=\"20%\" />").append("\n");
			try {
				List<news.service.Ssxx> Ssxxlist =HelloWebService.getSsxx(1,pagesize);
				String href;
				String title;
				String times;
				for (news.service.Ssxx ssxx : Ssxxlist) {
					sb.append("  <tr>").append("\n");
					href = ssxx.getHref().getValue();
					times = ssxx.getTimes().getValue().substring(0, 16);
					title = ssxx.getTitle().getValue();
					//title = (title.length()>=20?title.substring(0, 20):title)+"...";
					sb.append("  <td>").append("\n");
					sb.append("    <a target=\"_blank\" href='").append(href).append("'\" title='").append(title).append("'>").append("").append(title);
					sb.append("    </a></td>").append("\n");
					sb.append("    <td> <span style=\"float:right\">").append(times).append("</span>").append("\n");
					sb.append("  </td>").append("\n");
					sb.append("  </tr>").append("\n");
				}
			} catch (Exception e) {
			}
		}else{
			cmsInfoList = cmsInfoDAO.getNewsList(id);
			if(cmsInfoList != null && cmsInfoList.size()>0){
				for(IworkCmsContent model : cmsInfoList) {
					if(line>=limit)
						break;
					// 如果状态为空，默认为不发布
					if (model.getStatus() == null){
						model.setStatus(new Long(0));
					}
					// 判断当前用户的浏览权限
					if (!CmsUtil.getContentSecurityList(model.getBrowse())) {
						continue;
					}
					String releaseDate = null;
					if (model.getReleasedate() != null) {
						releaseDate = CmsUtil.dateShortFormat(model.getReleasedate());
					}
					
					int commentCount = CmsCommentUtil.getInstance().getCommentCount(model.getId() + "");
					
					if (model.getBrieftitle().equals("") || model.getBrieftitle() == null) {
						
						String fulltitle = CmsUtil.getCmsTitle(model);
						
						String title = "";
						if(fulltitle.length()>maxLength){
							title = getSubStr(fulltitle, maxLength);
						}else{
							title = fulltitle;
						}
						
						sb.append("  <li style=\"line-height:30px;\">").append("\n");
						sb.append("    <i class=\"ace-icon fa fa-angle-right bigger-110\"></i>").append("\n");
						sb.append("    <a href='#' onClick='openCms(").append(model.getId()).append(")'").append("\" title='").append(model.getTitle()).append("'>").append("").append(title);
						if (commentCount > 0) {
							sb.append(    "&nbsp;(" + commentCount + ")");
						}
						sb.append("        <img src = \"iwork_img/new.gif\"/>").append("\n");
						sb.append("    </a>").append("\n");
						sb.append("    <span style=\"float:right\">").append(releaseDate).append("</span>").append("\n");
						sb.append("  </li>").append("\n");
					} else {
						String fulltitle = CmsUtil.getCmsBriefTitle(model);
						String title = "";
						if(fulltitle.length()>maxLength){
							title = getSubStr(fulltitle, maxLength);
						}else{
							title = fulltitle;
						}
						
						sb.append("  <li>").append("\n");
						sb.append("    <i class=\"ace-icon fa fa-angle-right bigger-110\"></i>").append("\n");
						sb.append("    <a href='#' onClick='openCms(").append(model.getId()).append(")'").append("\" title='").append(model.getTitle()).append("'>").append("").append(title);
						if (commentCount > 0) {
							sb.append(    "&nbsp;(" + commentCount + ")");
						}
						sb.append("        <img src = \"iwork_img/new.gif\"/>").append("\n");
						sb.append("    </a>").append("\n");
						sb.append("    <span style=\"float:right\">").append(releaseDate).append("</span>").append("\n");
						sb.append("  </li>").append("\n");
					}
					if (line == rows - 1) {
						line++;
						break;
					}
					line++;
				}
			}
		}
		if(id==17l){
			sb.append("  <tr>").append("\n");
			sb.append(" <td></td><td><td/><td></td> <td>").append("\n");
			sb.append("    <a style=\"float:right;cursor:pointer;font-size:15px;\" onclick=\"openMoreHtml(").append(id).append(")\" >").append(">>更多").append("</a>").append("\n");
			sb.append("  </td>").append("\n");		
			sb.append("  </tr>").append("\n");
		}else if(id==0l){
			sb.append("  <tr>").append("\n");
			sb.append(" <td></td> <td>").append("\n");
			sb.append("    <a style=\"float:right;cursor:pointer;font-size:15px;\" onclick=\"openMoreHtml(").append(id).append(")\" >").append(">>更多").append("</a>").append("\n");
			sb.append("  </td>").append("\n");		
			sb.append("  </tr>").append("\n");
		}else if(id==14l){
			sb.append("  <tr>").append("\n");
			sb.append("  <td>").append("\n");
			sb.append("    <a style=\"float:right;cursor:pointer;font-size:15px;\" onclick=\"openMoreHtml(").append(id).append(")\" >").append(">>更多").append("</a>").append("\n");
			sb.append("  </td>").append("\n");		
			sb.append("  </tr>").append("\n");
		}
		sb.append("</table>").append("\n");
		
		return sb.toString();
	}
	
	public String getDMZqdm(String khbh,String gsmc){
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql = new StringBuffer("SELECT ZQDM FROM BD_ZQB_KH_BASE WHERE ");
		if(gsmc!=null && !"".equals(gsmc)){
			sql.append(" (CUSTOMERNAME LIKE ? OR ZQJC LIKE ?) AND ");
			parameter.add("%"+gsmc+"%");
			parameter.add("%"+gsmc+"%");
		}
		sql.append(" CUSTOMERNO=?");
		parameter.add(khbh);
		int index=1;
		StringBuffer result = new StringBuffer();
		String zqdm = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				zqdm = rs.getString("ZQDM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		if(zqdm!=null&&!zqdm.trim().equals("")){
			String[] split = zqdm.split(",");
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {  
					result.append("'").append(split[i]).append("'"); 
				}else if((i%999)==0 && i>0){  
					result.append("'").append(split[i]).append("') OR TYPE IN ("); 
				}else{
					result.append("'").append(split[i]).append("',");  
				}  
			}
		}
		return result.toString();
	}
	public String getCustomerZqdm(String userid,String gsmc,Long roleid){
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql = new StringBuffer("SELECT TO_CHAR(WM_CONCAT(ZQDM)) ZQDM FROM (SELECT BASE.ZQDM ZQDM FROM (SELECT KHBH FROM BD_MDM_KHQXGLB WHERE 1=1 ");
		if(gsmc!=null && !"".equals(gsmc)){
			sql.append(" AND KHMC LIKE ? ");
			parameter.add("%"+gsmc+"%");
		}
		if(roleid!=5){
			sql.append(" AND (SUBSTR(KHFZR,0, INSTR(KHFZR,'[',1)-1)=? OR SUBSTR(ZZCXDD,0, INSTR(ZZCXDD,'[',1)-1)=? OR SUBSTR(FHSPR,0, INSTR(FHSPR,'[',1)-1)=? OR SUBSTR(ZZSPR,0, INSTR(ZZSPR,'[',1)-1)=? OR SUBSTR(GGFBR,0, INSTR(GGFBR,'[',1)-1)=?)");
			parameter.add(userid);
			parameter.add(userid);
			parameter.add(userid);
			parameter.add(userid);
			parameter.add(userid);
		}
		sql.append(" ) CXDD INNER JOIN (SELECT CUSTOMERNO,ZQDM FROM BD_ZQB_KH_BASE ");
		if(gsmc!=null && !"".equals(gsmc)){
			sql.append(" WHERE CUSTOMERNAME LIKE ? OR ZQJC LIKE ?");
			parameter.add("%"+gsmc+"%");
			parameter.add("%"+gsmc+"%");
		}
		sql.append(" ) BASE ON CXDD.KHBH=BASE.CUSTOMERNO)");
		int index=1;
		StringBuffer result = new StringBuffer();
		String zqdm = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				zqdm = rs.getString("ZQDM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		if(zqdm!=null&&!zqdm.trim().equals("")){
			String[] split = zqdm.split(",");
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {  
					result.append("'").append(split[i]).append("'"); 
				}else if((i%999)==0 && i>0){  
					result.append("'").append(split[i]).append("') OR TYPE IN ("); 
				}else{
					result.append("'").append(split[i]).append("',");  
				}  
			}
		}
		return result.toString();
	}
	
	/**
	 * 获取知道信息
	 * @return
	 */
	public String getKnowInfoHtml(long type){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		StringBuffer sb = new StringBuffer();
		
		int line = 0;
		int rows = 0;
		
		sb.append("<table width=\"99%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed;\">").append("\n");
		sb.append("  <tr>").append("\n");
		sb.append("    <col width=\"20%\" />").append("\n");
		sb.append("    <col width=\"55%\" />").append("\n");
		sb.append("    <col width=\"10%\" />").append("\n");
		sb.append("    <col width=\"15%\" />").append("\n");
		sb.append("    <td  style=\"line-height:24px; padding: 0px;\" ><font color=\"#333333\" style=\"font-weight:bold;\">分类</font></td>").append("\n");
		sb.append("    <td ><font color=\"#333333\" style=\"font-weight:bold;\">问题</font></td>").append("\n");
		sb.append("    <td ><font color=\"#333333\" style=\"font-weight:bold;\">回答数</font></td>").append("\n");
		sb.append("    <td align='center'><font color=\"#333333\" style=\"font-weight:bold;\">提问时间</font></td>").append("\n");
		
		sb.append("  </tr>").append("\n");
		
		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		
		// 根据type查询知道信息
		List<IworkKnowQuestion> questionList = null;
		if (type == 0L){//待解决问题
			questionList = cmsInfoDAO.searchUnsolvedQuestionList(null, null, 1, 10);
		}else if(type == 1L){//已解决问题
			questionList = cmsInfoDAO.searchSolvedQuestionList(null, null, 1, 10);
		}else if(type == 2L){//我的提问
			questionList = cmsInfoDAO.searchMyAskQuestionList(null, null, 1, 10, user.getUserid());
		}else if(type == 3L){//我的回答
			questionList = cmsInfoDAO.searchMyAnswerQuestionList(null, null, 1, 10, user.getUserid());
		}else if(type == 4L){//问我的问题
			questionList = cmsInfoDAO.searchAskMeQuestionList(null, null, 1, 10, user);
		}
		//List<IworkCmsContent> cmsInfoList = cmsInfoDAO.getNewsList(new Long(0));
		
		//if(cmsInfoList != null && cmsInfoList.size()>0){
			for(IworkKnowQuestion model : questionList) {
				if(line>=limit)
					break;
				String content = "";
				if(model.getQcontent().length()>maxLength){
					content = getSubStr(model.getQcontent(), maxLength);
				}else{
					content = model.getQcontent();
				}
				//style=\"line-height:22px;\"
				sb.append("  <tr onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\">").append("\n");
				sb.append("    <td style=\"color:#056ea4;\">").append("    [").append(model.getIworkKnowClasses().getCname()).append("]").append("</td>").append("\n");
				sb.append("    <td class=\"span_con\">\n");
				if(model.getScore()>0){
					sb.append("    <img src=\"/iwork_img/know/repo_rep.gif\" /><font color=\"#C0120B\">").append(model.getScore()).append("</font>&nbsp;&nbsp;");
				}
				sb.append("    <a href='#' onClick='openQuestionWin(").append(model.getId()).append(");").append("' title=\"").append(model.getQcontent()).append("\">").append(content).append("</a>").append("\n");
				sb.append("    </td>\n");
				sb.append("    <td >").append(model.getIworkKnowAnswers().size()).append("</td>\n");
				sb.append("    <td  align='right'>").append(this.getShowTime(model.getQbegintime())).append("</td>\n");
				
				sb.append("  </tr>").append("\n");
				
				
				if (line == rows - 1) {
					line++;
					break;
				}
				line++;
			}
		//}
		sb.append("  <tr>").append("\n");
		sb.append("    <td></td><td></td><td></td><td><a style=\"float:right;cursor:pointer;font-size:15px;\" onclick=\"openMoreKnow()\">>>更多</a></td>").append("\n");
		sb.append("  </tr>").append("\n");
		sb.append("</table>").append("\n");
		return sb.toString();
	}
	/**
	 *  日程提醒完成事件
	 * @param type
	 */
	public void addRcwc(Long type){
		SysNavFrameService.addRcwc(type);
	}
	/**
	 * 周期日程提醒完成事件
	 * @param type
	 */
	public void updRcwc(Long type,String sj){
		SysNavFrameService.updRcwc(type,sj);
	}
	/**
	 * 取得非督导的日程
	 * @return
	 */
	public String getfddRctx(){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		StringBuffer sb = new StringBuffer();
		
		int line = 0;
		int rows = 0;
		sb.append("<table width=\"99%\" id=\"tab4\"  class=\"table \" style=\"font-size:15px;\" >").append("\n");
		sb.append("  <tr>").append("\n");
		
		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		
		
		List<HashMap<String, Object>> questionList = null;
	
		questionList = SysNavFrameService.getfddRctx(1,3);
		sb.append("    <col width=\"60%\" />").append("\n");
		sb.append("    <col width=\"25%\" />").append("\n");
		
		sb.append("    <col width=\"15%\" />").append("\n");
	
	
		sb.append("  </tr>").append("\n");
		if(questionList.size()>0){
			for(HashMap<String, Object> model : questionList) {
				
					sb.append("  <tr onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\">").append("\n");
					sb.append("    <td style=\"font-size:15px;color:#056ea4;\">").append(model.get("title")).append("</td>").append("\n");
					sb.append("    <td style=\"font-size:15px;color:#056ea4;\">").append(model.get("sdate").toString().substring(0, 10)).append("</td>").append("\n");
					sb.append("    <td ></td>").append("\n");
					sb.append("  </tr>").append("\n");
					
					if (line == rows - 1) {
						line++;
						break;
					}
					line++;
				}
		}
		
		sb.append("  <tr>").append("\n");
		sb.append("    <td></td>   <td></td><td><a style=\"float:right;cursor:pointer;font-size:15px;\" href=\"schCalendarAction.action\">>>更多</a></td>").append("\n");
		sb.append("  </tr>").append("\n");
		sb.append("</table>").append("\n");
		return sb.toString();
	}
	
	/**
	 * 取得日程提醒
	 * @return
	 */
	public String getRctx(){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		StringBuffer sb = new StringBuffer();
		
		int line = 0;
		int rows = 0;
		sb.append("<table width=\"99%\" id=\"tab4\"  class=\"table \" style=\"font-size:15px;\" >").append("\n");
		sb.append("  <tr>").append("\n");
		
		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		
		
		List<HashMap<String, Object>> questionList = null;
	
		questionList = SysNavFrameService.getRctx(1,3);
		sb.append("    <col width=\"60%\" />").append("\n");
		sb.append("    <col width=\"15%\" />").append("\n");
		sb.append("    <col width=\"15%\" />").append("\n");
		sb.append("    <col width=\"10%\" />").append("\n");
	
	
		sb.append("  </tr>").append("\n");
		if(questionList.size()>0){
			for(HashMap<String, Object> model : questionList) {
				
					sb.append("  <tr onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\">").append("\n");
					sb.append("    <td style=\"font-size:15px;color:#056ea4;\"><a href=\"javascript:addAdvance('"+model.get("tid")+"')\">").append(model.get("title")).append("--"+model.get("sj")+"</a></td>").append("\n");
					sb.append("		<td style=\"font-size:15px;\">"+model.get("zqjc")+"</td>\n");
					sb.append("		<td style=\"font-size:15px;\">"+model.get("zqdm")+"</td>\n");
					sb.append("		<td style=\"font-size:15px;\"><a href=\"javascript:rctxWc('"+model.get("id")+"','"+model.get("tid")+"','"+model.get("flag")+"','"+model.get("sj")+"')\">确认</a> </td>\n");
				
					sb.append("  </tr>").append("\n");
					
					if (line == rows - 1) {
						line++;
						break;
					}
					line++;
				}
		}
		
		sb.append("  <tr>").append("\n");
		sb.append("    <td></td> <td></td>  <td></td><td><a style=\"float:right;cursor:pointer;font-size:15px;\" href=\"rx_index.action\">>>更多</a></td>").append("\n");
		sb.append("  </tr>").append("\n");
		sb.append("</table>").append("\n");
		return sb.toString();
	}
	
	/**
	 * 取得问答内容
	 * @return
	 */
	public String getWd(){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		StringBuffer sb = new StringBuffer();
		

		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		List<IworkKnowQuestion> list = cmsInfoDAO.searchSolvedQuestionList("",0L,1,9);
		
		
		
		
		sb.append("<table width=\"99%\" id=\"tab8\" class=\"table\" style=\"font-size:15px;\" >").append("\n");
		for (IworkKnowQuestion lists : list) {
			
			sb.append("  <tr>").append("\n");
			sb.append("  <td>  <a target=\"_blank\" style=\"cursor: pointer;\" onclick='").append("openQuestionWin("+lists.getId()+")").append("'\" title='").append(lists.getQcontent()).append("'>").append(lists.getQcontent());
			sb.append("    </a></td>").append("\n");
			sb.append("  </tr>").append("\n");
		}
	
		sb.append("  <tr>").append("\n");
		sb.append("  <td>   <a style=\"float:right;cursor:pointer;font-size:15px;\" onclick=\"openMoreWd(").append("").append(")\" >").append(">>更多").append("</a></td>").append("\n");
		sb.append("  </tr>").append("\n");
		sb.append("</table>").append("\n");
		return sb.toString();
	}
	/**
	 * 获取待办事宜
	 * @return
	 */
	public String getDBSYHtml(long type){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long roleId=user.getOrgroleid();
		int pagesize=10;
		if(roleId==3) pagesize=3;
		StringBuffer sb = new StringBuffer();
		int line = 0;
		int rows = 0;
		sb.append("<table width=\"99%\"  id=\"tab3\" class=\"table\" style=\"font-size:15px;\" >").append("\n");
		//sb.append("<table width=\"99%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed;\">").append("\n");
		sb.append("  <tr>").append("\n");
		
		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		
		// 根据type待办事宜
		List<HashMap<String, Object>> questionList = null;
		if(SysNavFrameService==null)
			SysNavFrameService = (SysNavFrameService)SpringBeanUtil.getBean("sysNavFrameService");
		if (type == 0L){//披露信息更新
				questionList = SysNavFrameService.getXpxxList(1,10);
				sb.append("    <col width=\"15%\" />").append("\n");
				sb.append("    <col width=\"15%\" />").append("\n");
				sb.append("    <col width=\"15%\" />").append("\n");
				sb.append("    <col width=\"15%\" />").append("\n");
				sb.append("    <col width=\"20%\" />").append("\n");
				sb.append("    <col width=\"20%\" />").append("\n");
	
				sb.append("    <td  style=\"line-height:24px;font-size:15px;padding: 0px;\" ><font color=\"#333333\" style=\"font-weight:bold;\">公司代码</font></td>").append("\n");
				sb.append("    <td ><font color=\"#333333\" style=\"font-size:15px;font-weight:bold;\">公司简称</font></td>").append("\n");
				sb.append("    <td ><font color=\"#333333\" style=\"font-size:15px;font-weight:bold;\">更新文件信息</font></td>").append("\n");
				sb.append("  </tr>").append("\n");
				if(questionList.size()>0){
					for(HashMap<String, Object> model : questionList) {
						sb.append("  <tr onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\">").append("\n");
						sb.append("    <td style=\"font-size:15px;color:#056ea4;\">").append("    [").append(model.get("ZQDM")).append("]").append("</td>").append("\n");
						sb.append("    <td style=\"font-size:15px;\" class=\"span_con\">\n").append(model.get("ZQJC"));
						sb.append("    </td>\n");
						sb.append("    <td style=\"font-size:15px;\" class=\"span_con\">\n").append(model.get("TYPENAME"));
						sb.append("    </td>\n");
						sb.append("    <td style=\"font-size:15px;\">").append(model.get("TITLE")).append("</td>\n");
						if(model.get("FILESRCNAME")==null){
							sb.append("				<td style=\"font-size:15px;\"><a href=\"javascript:dealGzyjHF('"+model.get("FILEID").toString()+"')\">查看</a> </td>\n");
						}else{
							sb.append("				<td style=\"font-size:15px;\"><a href=\"javascript:downloadTemplate('"+model.get("FILEID")+"')\">"+model.get("FILESRCNAME")+"</a></td>\n");
						}
						
							sb.append("    <td style=\"font-size:15px;\" align='right'>").append(this.getShowTime(model.get("TIME").toString())).append("</td>\n");
							sb.append("  </tr>").append("\n");
						
						
						if (line == rows - 1) {
							line++;
							break;
						}
						line++;
					}
				}
			sb.append("  <tr>").append("\n");
			sb.append("    <td></td> <td></td> <td></td><td></td><td></td><td><a style=\"float:right;cursor:pointer;font-size:15px;\" href=\"fsp_zydd_index.action\">>>更多</a></td>").append("\n");
			sb.append("  </tr>").append("\n");
			sb.append("</table>").append("\n");
		}else if(type == 1L){//会议计划
				questionList = SysNavFrameService.getHyfsp(1,3);
				sb.append("    <col width=\"30%\" />").append("\n");
				sb.append("    <col width=\"30%\" />").append("\n");
				sb.append("    <col width=\"20%\" />").append("\n");
				sb.append("    <col width=\"10%\" />").append("\n");
				sb.append("    <col width=\"10%\" />").append("\n");
	
				/*sb.append("    <td  style=\"line-height:24px;padding: 0px;\" ><font color=\"#333333\" style=\"font-weight:bold;font-size:15px;\">公司名称</font></td>").append("\n");
				sb.append("    <td ><font color=\"#333333\" style=\"font-weight:bold;font-size:15px;\">会议名称</font></td>").append("\n");
				sb.append("    <td ><font color=\"#333333\" style=\"font-weight:bold;font-size:15px;\">会议时间</font></td>").append("\n");
				sb.append("    <td ><font color=\"#333333\" style=\"font-weight:bold;font-size:15px;\">会议状态</font></td>").append("\n");
				sb.append("    <td align='center'><font color=\"#333333\" style=\"font-weight:bold;font-size:15px;\">操作</font></td>").append("\n");*/
				sb.append("  </tr>").append("\n");
				if(questionList.size()>0){
						for(HashMap<String, Object> model : questionList) {
							sb.append("  <tr onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\">").append("\n");
							sb.append("    <td style=\"font-size:15px;color:#056ea4;\" onclick=\"checkGlhy("+model.get("ID")+")\" >").append(model.get("CUSTOMERNAME")).append("</td>").append("\n");
							sb.append("    <td style=\"font-size:15px;\" class=\"span_con\">\n").append(model.get("MEETNAME"));
							sb.append("    </td>\n");
							sb.append("    <td style=\"font-size:15px;\" class=\"span_con\">\n").append(model.get("PLANTIME"));
							sb.append("    </td>\n");
							sb.append("    <td  style=\"font-size:15px;\">").append(model.get("STATUS")).append("</td>\n");
							sb.append("				<td style=\"font-size:15px;\" align='center'><a href=\"javascript:updateClflag('"+model.get("INSTANCEID")+"','"+model.get("ID")+"')\">完成</a> </td>\n");
							sb.append("  </tr>").append("\n");
							
							
							if (line == rows - 1) {
								line++;
								break;
							}
							line++;
						}
				}
				sb.append("  <tr>").append("\n");
				sb.append("    <td></td> <td></td> <td></td><td></td><td><a style=\"float:right;cursor:pointer;font-size:15px;\" href=\"fsp_hy_index.action\">>>更多</a></td>").append("\n");
				sb.append("  </tr>").append("\n");
				sb.append("</table>").append("\n");
		}else if(type == 2L){//工作备查
				questionList = SysNavFrameService.getGzsc(1,3);
				String gzscformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查'", "FORMID");
				String gzscdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查'", "ID");
				String gzschfformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查回复'", "FORMID");
				String gzschfdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查回复'", "ID");
				sb.append("    <col width=\"60%\" />").append("\n");
				sb.append("    <col width=\"25%\" />").append("\n");
				sb.append("    <col width=\"15%\" />").append("\n");
				
	
			/*	sb.append("    <td  style=\"padding-left: 10px;\" ><font color=\"#333333\" style=\"font-weight:bold;font-size:15px;\">标题</font></td>").append("\n");
				sb.append("    <td ><font color=\"#333333\" style=\"font-weight:bold;font-size:15px;\">通知日期</font></td>").append("\n");
				sb.append("    <td align='center'><font color=\"#333333\" style=\"font-weight:bold;font-size:15px;\">操作</font></td>").append("\n");*/
				sb.append("  </tr>").append("\n");
				if(questionList.size()>0){
					for(HashMap<String, Object> model : questionList) {
						
							sb.append("  <tr onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\">").append("\n");
							sb.append("    <td style=\"font-size:15px;color:#056ea4;\">").append(model.get("TZBT")).append("</td>").append("\n");
							sb.append("    </td>\n");
							sb.append("    <td style=\"font-size:15px;\" class=\"span_con\">\n").append(model.get("FSSJ").toString().substring(0,10));
							sb.append("    </td>\n");
							
							if(model.get("FKZT").toString().equals("1")){
								sb.append("				<td style=\"font-size:15px;\" align='left'><a href=\"javascript:updateGzsc('"+gzscformid+"','"+gzscdemid+"','"+model.get("CID")+"','"+model.get("DID")+"','"+model.get("INSTANCEID")+"')\">查看</a>&nbsp;|&nbsp;<a href=\"javascript:checkGzschf('"+gzschfformid+"','"+gzschfdemid+"','"+model.get("EINS")+"')\">已反馈</a></td>\n");
							}else{
								sb.append("				<td style=\"font-size:15px;\" align='left'><a href=\"javascript:updateGzsc('"+gzscformid+"','"+gzscdemid+"','"+model.get("CID")+"','"+model.get("DID")+"','"+model.get("INSTANCEID")+"')\">查看</a>&nbsp;|&nbsp;<a href=\"javascript:addGzschf('"+gzschfformid+"','"+gzschfdemid+"','"+model.get("CID")+"','"+model.get("DID")+"')\">回复</a></td>\n");
							}
							sb.append("  </tr>").append("\n");
							
							if (line == rows - 1) {
								line++;
								break;
							}
							line++;
						}
				}
				sb.append("  <tr>").append("\n");
				sb.append("    <td></td>  <td></td><td><a style=\"float:right;cursor:pointer;font-size:15px;\" href=\"fsp_gzsc_index.action\">>>更多</a></td>").append("\n");
				sb.append("  </tr>").append("\n");
				sb.append("</table>").append("\n");
			}
		return sb.toString();
	}
	
	public String getShowTime(String createTime)
	  {
	    String time = "";
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try
	    {
	      Date date = new Date();
	      String today = sdf.format(date);
	      today = today + " 00:00:00";
	      Date jintian = sdf1.parse(today);
	      if (sdf1.parse(createTime).before(jintian)) {
	        time = createTime.substring(0, 10);
	      } else {
	        time = "今日" + createTime.substring(11, 16);
	      }
	    }
	    catch (Exception e)
	    {
	      logger.error(e,e);
	    }
	    return time;
	  }
	/**
	 * 获取Email
	 * @return
	 */
	public String getEmailHtml(){
		
		StringBuffer sb = new StringBuffer();
		
		// 获取当前登录用户ID
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		
		if (recIWorkMailClientService == null) {
			recIWorkMailClientService = (RecIWorkMailClientService) SpringBeanUtil.getBean("reciWorkMailClientService");
		}
		
		// 查询当前登陆用户状态为"0"的收件箱邮件
		List<MailTaskModel> list = recIWorkMailClientService.queryPageMails(userId, 10, 0, 0, 9, -2);
		
		// 装载返回EmailHtml
		
		sb.append("<ul class='item_task'>").append("\n");
		
		// 定义是否存在更多记录
		boolean isMore = false;
		
		for(int i=0; i<list.size(); i++){
			
			MailTaskModel mail = list.get(i);
			if(mail!=null && !"".equals(mail)){
				if(mail.getIsRead()==0){
					sb.append("<li><a style=\"font-weight:bold;\" href=\"###\" onclick=\"openEmailDetailInfo(").append(mail.getBindId()).append(",").append(mail.getId()).append(",'").append(mail.getTitle()).append("',").append(mail.getMailBox()).append(");").append("\" title=\"").append(mail.getTitle()).append("\" target=\"_self\" >").append(mail.getTitle()).append("<img src = \"iwork_img/new.gif\"/>").append("</a><span style='float:right'>").append(CmsUtil.dateShortFormat(mail.getCreateTime())).append("</span>").append("\n");
					sb.append("</li>").append("\n");
				}else{
					sb.append("<li><a style=\"color:black;\" href=\"###\" onclick=\"openEmailDetailInfo(").append(mail.getBindId()).append(",").append(mail.getId()).append(",'").append(mail.getTitle()).append("',").append(mail.getMailBox()).append(");").append("\" title=\"").append(mail.getTitle()).append("\" target=\"_self\" >").append(mail.getTitle()).append("</a><span style='float:right'>").append(CmsUtil.dateShortFormat(mail.getCreateTime())).append("</span>").append("\n");
					sb.append("</li>").append("\n"); 
				}
			}
			
			if(i>=10){
				isMore = true;
				break;
			}
		}
		
		if(isMore){
			sb.append("<li><a  style=\"float:right\" target=\"_self\" href=\"iwork_email_index.action\"").append("\" target=\"_blank\" >查看更多</a>").append("\n");
		}
		sb.append("</ul>").append("\n");
		
		return sb.toString();
	}
	
	/**
	 * 获取待办事项
	 * @return
	 */
	public String getTodoHtml(){
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<ul class='item_task'>").append("\n");
		if (processDeskService == null) {
			processDeskService = (ProcessDeskService) SpringBeanUtil.getBean("processDeskService");
		}
		List<TodoTaskModel> list = processDeskService.getTaskList(null);
		boolean isMore = false;
		for (int i = 0; i < list.size(); i++) {
			TodoTaskModel model = list.get(i);
			if(model.getIsRead()!=null&&model.getIsRead().equals(new Long(0))){
				if(model.getTaskType().equals("Notice")){
					sb.append("<li><a style=\"color:black;\" href=\"###\" onclick=\"openNoticePage('").append("").append("','").append(model.getActDefId()).append("',").append(model.getInstanceId()).append(",").append(model.getExcutionId()).append(",").append(model.getTaskId()).append(");").append("\" target=\"_self\" >").append("<img border=\"0\" src=\"iwork_img/min_form.gif\"/>&nbsp;").append(model.getTitle().split("-")[0]).append("</a><span style='float:right'>").append(CmsUtil.dateShortFormat(model.getCreateDate())).append("</span>").append("\n");
					sb.append("</li>").append("\n");
				}else if(model.getTaskType().equals("Signs")){
					sb.append("<li><a style=\"color:black;\" href=\"###\"  onclick=\"openSignsPage('").append(model.getTitle()).append("','").append(model.getActDefId()).append("',").append(model.getInstanceId()).append(",").append(model.getExcutionId()).append(",").append(model.getTaskId()).append(");").append("\" title=\"").append(model.getTitle()).append("\" target=\"_self\" >").append("").append(model.getTitle()).append("</a><span style='float:right'>").append(CmsUtil.dateShortFormat(model.getCreateDate())).append("</span>").append("\n");
					sb.append("</li>").append("\n");
				}else{
					sb.append("<li><a style=\"color:black;\" href=\"###\"  onclick=\"openTaskPage('").append(model.getTitle()).append("','").append(model.getActDefId()).append("',").append(model.getInstanceId()).append(",").append(model.getExcutionId()).append(",").append(model.getTaskId()).append(");").append("\" title=\"").append(model.getTitle()).append("\" target=\"_self\" >").append("").append(model.getTitle()).append("</a><span style='float:right'>").append(CmsUtil.dateShortFormat(model.getCreateDate())).append("</span>").append("\n");
					sb.append("</li>").append("\n");
				}
			}else{
				if(model.getTaskType().equals("Notice")){
					sb.append("<li><a href=\"###\"  onclick=\"openNoticePage('").append("").append("','").append(model.getActDefId()).append("',").append(model.getInstanceId()).append(",").append(model.getExcutionId()).append(",").append(model.getTaskId()).append(");").append("\" target=\"_self\" ><b>").append("").append(model.getTitle().split("-")[0]).append("</b></a><span style='float:right'>").append(UtilDate.getDaysBeforeNow(model.getCreateDate())).append("</span>").append("\n");
					sb.append("</li>").append("\n");
				}else if(model.getTaskType().equals("Signs")){
					sb.append("<li><a  href=\"###\"  onclick=\"openSignsPage('").append(model.getTitle()).append("','").append(model.getActDefId()).append("',").append(model.getInstanceId()).append(",").append(model.getExcutionId()).append(",").append(model.getTaskId()).append(");").append("\" title=\"").append(model.getTitle()).append("\" target=\"_self\" ><b>").append(model.getTitle()).append("</b></a><span style='float:right'>").append(CmsUtil.dateShortFormat(model.getCreateDate())).append("</span>").append("\n");
					sb.append("</li>").append("\n");
				}else{
					sb.append("<li><a  href=\"###\"  onclick=\"openTaskPage('").append(model.getTitle()).append("','").append(model.getActDefId()).append("',").append(model.getInstanceId()).append(",").append(model.getExcutionId()).append(",").append(model.getTaskId()).append(");").append("\" title=\"").append(model.getTitle()).append("\" target=\"_self\" ><b>").append(model.getTitle()).append("</b></a><span style='float:right'>").append(CmsUtil.dateShortFormat(model.getCreateDate())).append("</span>").append("\n");
					sb.append("</li>").append("\n");
				}
			}
			if(i>=10){
				isMore = true;
				break;
			}
		}
		
		sb.append("</ul>").append("\n");
		return sb.toString();
	}
	public String getGovHtml(){
		StringBuffer html = new StringBuffer();
		List<HashMap> list = new ArrayList<HashMap>();
		
		//当前用户账号
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		StringBuffer sql = new StringBuffer("select * from (select * from BD_GOV_GWXFJLB g where g.swzt='").append(userId).append("' and g.xflx='员工'");
		sql.append(" union all ");
		sql.append(" select * from BD_GOV_GWXFJLB g where g.xflx='部门' and g.swzt in (");
		sql.append(" select distinct d.id from orgdepartment d ");
		sql.append(" start with d.id=(select u.departmentid from orguser u where u.userid='").append(userId).append("')");
		sql.append(" connect by  d.id =prior d.parentdepartmentid)) where rownum<=10");
		Connection con= null;
		ResultSet rs = null;
		Statement stmt = null;
		try{		
			con = DBUtil.open();
			stmt = con.createStatement(); 
			rs = DBUtil.executeQuery(con, stmt, sql.toString());
		    while(rs.next()){
		    	HashMap map = new HashMap();
		    	long ID = rs.getLong("ID");
		    	String GWBT = rs.getString("GWBT");
		    	String XFLX = rs.getString("XFLX");
		    	String WZ = rs.getString("WZ");
		    	String JJCD = rs.getString("JJCD");
		    	String MJ = rs.getString("MJ");
		    	String SFYY = rs.getString("SFYY");
		    	String BZ = rs.getString("BZ");
		    	String SWZT = rs.getString("SWZT");
		    	String GWFJ = rs.getString("GWFJ");
		    	String FWBM = rs.getString("FWBM");
		    	String URL = rs.getString("URL");
		    	String SWZTMC = rs.getString("SWZTMC");
		    	map.put("ID", ID);
		    	map.put("GWBT", GWBT);
		    	map.put("XFLX", XFLX);
		    	map.put("WZ", WZ);
		    	map.put("JJCD", JJCD);
		    	map.put("MJ", MJ);
		    	map.put("SFYY", SFYY);
		    	map.put("BZ", BZ);
		    	map.put("SWZT", SWZT);
		    	map.put("GWFJ", GWFJ);
		    	map.put("FWBM", FWBM);
		    	map.put("URL", URL);
		    	map.put("SWZTMC", SWZTMC);
		    	list.add(map);
		    }   
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			DBUtil.close(con, stmt, rs);
		}		
	    html.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed;\">").append("\n");
	    html.append("  <tr>").append("\n");
	    html.append("    <td class=\"title\" style=\"width:5%;\">&nbsp;</td>").append("\n");
	    html.append("    <td class=\"title\" style=\"width:44%;\">公文标题</td>").append("\n");
	    html.append("    <td class=\"title\" style=\"width:12%;\">发文部门</td>").append("\n");
	    html.append("    <td class=\"title\" style=\"width:15%;\">收文主体</td>").append("\n");
	    html.append("    <td class=\"title\" style=\"width:12%;\">紧急程度</td>").append("\n");
	    html.append("    <td class=\"title\" style=\"width:12%;\" >公文密级</td>").append("\n");
	    html.append("  </tr>").append("\n");
	    
	    int num = 0;
	    num++;
	    for (HashMap map : list){
	    	if(num>=10){
	    		break;
	    	}
	    	String title = "";
	    	String url = "";
	    	if(map.get("GWBT")!=null){
		    	if(map.get("GWBT").toString().length()>maxLength){
		    		title = getSubStr(map.get("GWBT").toString(), maxLength);
		    	}else{
		    		title=map.get("GWBT").toString();
		    	}
	    	}
	    	
	    	html.append("<tr class=\"mailItem\" onclick=\"javascript:openGw(").append(Long.valueOf(map.get("ID").toString())).append(",'").append(map.get("URL").toString()).append("','").append(map.get("SFYY").toString()).append("')\">");
	    	html.append("<td class=\"mailicon\" align='center'>").append("\n");
	    	if("已阅".equals(map.get("SFYY").toString())){
	    		html.append("    <img src=\"/iwork_img/readed.gif\" />").append("\n");
	    		//html.append("<input name=\"isOpen").append(map.get("ID")).append("\" type=\"button\" class=\"read_icon\" disabled/>").append("\n");
	    	}
	    	if("未阅".equals(map.get("SFYY").toString())){
	    		html.append("    <img src=\"/iwork_img/MailUnread.gif\" />").append("\n");
	    		//html.append("<input name=\"isOpen").append(map.get("ID")).append("\" type=\"button\" class=\"unread_icon\" disabled/>").append("\n");
	    	}
	    	html.append("</td>").append("\n");
	    	html.append("<td  class=\"mailUser\" ><span title=\"").append(map.get("GWBT").toString()).append("\">").append(title).append("</span></td>").append("\n");
			html.append("<td class=\"mailTitle\" >").append(map.get("FWBM").toString()).append("</td>").append("\n");
			html.append("<td class=\"mailTitle\" >").append(map.get("SWZTMC").toString()).append("</td>").append("\n");
			html.append("<td class=\"mailTitle\" >").append(map.get("JJCD").toString()).append("</td>").append("\n");
			html.append("<td class=\"mailTitle\" >").append(map.get("MJ").toString()).append("</td>").append("\n");
			
			html.append("</tr>").append("\n");
	    	
			num++;
	    }
	    html.append("  <tr>").append("\n");
	    html.append("    <td></td><td></td><td></td><td></td><td></td><td><a style=\"float:right;cursor:pointer;font-size:15px;\" onclick=\"openMoreGov()\">>>更多</a></td>").append("\n");
	    html.append("  </tr>").append("\n");
	    html.append("</table>").append("\n");
	    return html.toString();
	}
	
	/**
	 * 获取待办流程
	 * @return
	 */
	public String getListHtml(String searchKey, int pageNo, int itemNum){
		ProcessDeskService processDeskService = (ProcessDeskService) SpringBeanUtil.getBean("processDeskService");
	    StringBuffer html = new StringBuffer();
	    html.append("<table width=\"99%\" id=\"tab2\" class=\"table \" style=\"font-size:15px;\" >").append("\n");
	    html.append("  <col width=\"15%\" />").append("\n");
	    html.append("  <col width=\"57%\" />").append("\n");
	    html.append("  <col width=\"19%\" />").append("\n");
	    html.append("  <col width=\"8%\" />").append("\n");
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long roldId=user.getOrgroleid();
	    List<TodoTaskModel> list = processDeskService.getTaskListPage(searchKey, 1, itemNum);
	    int num = itemNum;
	    num++;
	    for (TodoTaskModel ttm : list){
	    	
	    		 if(num>3){
	 	    		break;
	 	    	}
	    	
	    	//文字内容
	    	String title = "";
	    	//超过指定长度，内容截取
	    	if(ttm.getTitle().length()>maxLength){
	    		title = this.getSubStr(ttm.getTitle(), maxLength);
	    	}else{
	    		title = ttm.getTitle();
	    	}
	    	//是否已读 0:未读 1:已读
	    	Long isRead = ttm.getIsRead();
			if (ttm.getIsRead().equals(new Long(1L))){
			    if (ttm.getTaskType().equals("Notice")){
					html.append("<tr  onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\" id=\"notice_").append(ttm.getTaskId()).append("\">").append("\n");
					html.append("  <td  style=\"font-size:15px;\">").append(ttm.getOwnerName()).append("</td>").append("\n");
					if(isRead==0)
						html.append("  <td  class=\"span_con\" style=\"cursor:pointer;color:#056ea4;font-size:15px;\" onclick=\"openNoticePage();\" title=\"").append(ttm.getTitle()).append("\">").append(title).append("</td>").append("\n");
					else  						
						html.append("  <td  class=\"span_con\" style=\"cursor:pointer;color:#056ea4;font-size:15px;font-weight:bold;\" onclick=\"openNoticePage();\" title=\"").append(ttm.getTitle()).append("\">").append(ttm.getTitle()).append("</td>").append("\n");
					html.append("  <td >").append(datetimeFormat(ttm.getCreateDate())).append("</td>").append("\n");
					html.append("  <td style=\"margin:right;font-size:15px;\" align='right'>").append(ttm.getLongTime()).append("</td>").append("\n");
					html.append("</tr> ");
			    }else if (ttm.getTaskType().equals("Signs")){
					html.append("<tr  onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\" id=\"signs_").append(ttm.getTaskId()).append("\">").append("\n");
					html.append("  <td  style=\"font-size:15px;\">").append(ttm.getOwnerName()).append("</td>").append("\n");
					if(isRead==0)
						html.append("  <td  class=\"span_con\" style=\"cursor:pointer;color:#056ea4;font-size:15px;\"  title=\"").append(ttm.getTitle()).append("\" onclick=\"openSignsPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(title).append("</font></td>").append("\n");
					else
						html.append("  <td  class=\"span_con\" style=\"cursor:pointer;color:#056ea4;font-size:15px;font-weight:bold;\"  title=\"").append(ttm.getTitle()).append("\" onclick=\"openSignsPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(title).append("</font></td>").append("\n");
					html.append("  <td >").append(datetimeFormat(ttm.getCreateDate())).append("</td>").append("\n");
					html.append("  <td style=\"margin:right;font-size:15px;\" align='right'>").append(ttm.getLongTime()).append("</td>").append("\n");
					html.append("</tr> ").append("\n");
			    }else{
			    	html.append("<tr  onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\" id=\"task_").append(ttm.getTaskId()).append("\">").append("\n");
			    	html.append("  <td  style=\"font-size:15px;\">").append(ttm.getOwnerName()).append("</td>").append("\n");
			    	if(isRead==0)
			    		html.append("  <td  class=\"span_con\" style=\"cursor:pointer;color:#056ea4;font-size:15px;\" title=\"").append(ttm.getTitle()).append("\" onclick=\"openTaskPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(title).append("</font></td>").append("\n");
			    	else
			    		html.append("  <td  class=\"span_con\" style=\"cursor:pointer;color:#056ea4;font-size:15px;font-weight:bold;\" title=\"").append(ttm.getTitle()).append("\" onclick=\"openTaskPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(title).append("</font></td>").append("\n");
					html.append("  <td >").append(datetimeFormat(ttm.getCreateDate())).append("</td>").append("\n");
					html.append("  <td style=\"margin:right;font-size:15px;\" align='right'>").append(ttm.getLongTime()).append("</td>").append("\n");
					html.append("</tr> ").append("\n");
			    }
			}else if (ttm.getTaskType().equals("Signs")){
				html.append("<tr  onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\" class=\"table-tr-notice\" id=\"signs_").append(ttm.getTaskId()).append("\">").append("\n");
				html.append("  <td  style=\"font-size:15px;\">").append(ttm.getOwnerName()).append("</td>").append("\n");
				if(isRead==0)
					html.append("  <td class=\"span_con\"  style=\"cursor:pointer;font-size:15px;color:#056ea4;\" title=\"").append(ttm.getTitle()).append("\" onclick=\"openSignsPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(title).append("</font></td>").append("\n");
				else
					html.append("  <td class=\"span_con\"  style=\"cursor:pointer;font-size:15px;color:#056ea4;font-weight:bold;\" title=\"").append(ttm.getTitle()).append("\" onclick=\"openSignsPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(title).append("</font></td>").append("\n");
				html.append("  <td >").append(datetimeFormat(ttm.getCreateDate())).append("</td>").append("\n");
				html.append("  <td style=\"margin:right;font-size:15px;\" align='right'>").append(ttm.getLongTime()).append("</td>").append("\n");
				html.append("</tr> ").append("\n");
			  }else{
				  html.append("<tr  onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\" id=\"task_").append(ttm.getTaskId()).append("\">").append("\n");
				  html.append("  <td  style=\"font-size:15px;\">").append(ttm.getOwnerName()).append("</td>").append("\n");
				  if(isRead.equals(new Long(0)))
					  html.append("  <td class=\"span_con\"  style=\"cursor:pointer;font-size:15px;color:#056ea4;\" title=\"").append(ttm.getTitle()).append("\" onclick=\"openTaskPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(title).append("</font></td>").append("\n");
				  else
					  html.append("  <td class=\"span_con\"  style=\"cursor:pointer;font-size:15px;color:#056ea4;font-weight:bold;\" title=\"").append(ttm.getTitle()).append("\" onclick=\"openTaskPage('").append(ttm.getTitle()).append("','").append(ttm.getActDefId()).append("',").append(ttm.getInstanceId()).append(",").append(ttm.getExcutionId()).append(",").append(ttm.getTaskId()).append(");\"><font class=\"black_font\">").append(title).append("</font></td>").append("\n");
				  html.append("  <td >").append(datetimeFormat(ttm.getCreateDate())).append("</td>").append("\n");
				  html.append("  <td style=\"margin:right;font-size:15px;\" align='right'>").append(ttm.getLongTime()).append("</td>").append("\n");
				  html.append("</tr> ").append("\n");
			  }
			  num++;
	    }
	    html.append("  <tr>").append("\n");
	    html.append("    <td></td><td></td><td colspan='2'><a style=\"float:right;cursor:pointer;font-size:15px;\" onclick=\"openMoreTodo()\">>>更多</a></td>").append("\n");
	    html.append("  </tr>").append("\n");
	    html.append("</table>").append("\n");
	    return html.toString();
	  }
	  
	  /**
	 * 获得日期时间
	 * 
	 * @param date
	 * @return 日期格式yyyy-MM-dd HH:mm:ss(old:yyyy-MM-dd hh:mm:ss a)
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String datetimeFormat(Date date) {
		if(date==null)return "";
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return datetimeFormat.format(date);
	}
	/**
	 * 截取字符串设定长度
	 * @param str
	 * @param cutCount 设定长度,字节数
	 * @return
	 */
	public String getSubStr(String str, int cutCount) {
       if (str == null)
           return "";

       String resultStr = "";
       char[] ch = str.toCharArray();
       int count = ch.length;
       int strBLen = str.getBytes().length;
       int temp = 0;
       for (int i = 0; i < count; i++) {
           resultStr += ch[i];
           temp = resultStr.getBytes().length;
           if (temp >= cutCount && temp < strBLen) {
               resultStr += "...";
               break;
           }
       }
       return resultStr;
	}
	
	/**
	 * 获得常用资料
	 * @return
	 */
	public String getAppkmHtml(){
		StringBuffer html = new StringBuffer();
//		CmsAppkmDAO cmsAppkmDAO = (CmsAppkmDAO) SpringBeanUtil
//				.getBean("cmsAppkmDAO");
//		List<IworkCmsAppkm> cmsAppkmList = cmsAppkmDAO.getList();
		
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		List<String> lables = new ArrayList<String>();lables.add("TITLE");lables.add("URL");lables.add("TYPE");
		Map params = new HashMap();params.put(1, user.getOrgroleid()==3l?"非券商":"券商");
		List<HashMap> cmsAppkmList = com.iwork.commons.util.DBUtil.getDataList(lables, "SELECT TITLE,URL,TYPE FROM BD_GE_KJYYMKB WHERE TYPE=? ORDER BY ID", params);
		
	    html.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed;\">").append("\n");
	    html.append("  <col width=\"50%\" />").append("\n");
	    html.append("  <col width=\"50%\" />").append("\n");
	    int num = 0;
	    num++;
	    for (HashMap ttm : cmsAppkmList){
	    	if(num>20){
	    		break;
	    	}
	    	String title = "";
	    	String url = "";
	    	if(ttm.get("TITLE").toString().length()>maxLength){
	    		title = getSubStr(ttm.get("TITLE").toString(), maxLength);
	    	}else{
	    		title = ttm.get("TITLE").toString();
	    	}
	    	if(ttm.get("URL").toString().length()>maxLength){
	    		url = getSubStr(ttm.get("URL").toString(), maxLength);
	    	}else{
	    		url = ttm.get("URL").toString();
	    	}
	    	
	    	if(num%2==1){
	    		html.append("<tr >");
	    	}
	    	
	    	html.append("<td class=\"text-overflow\" title=\"").append(ttm.get("TITLE").toString()).append("\">");
			html.append("<a style=\"font-size:15px;\" href=\"").append(ttm.get("URL").toString()).append("\" target=\"_blank\" title=\"").append(ttm.get("URL").toString()).append("\">");
			html.append(title);
			html.append("</a>");
			html.append("</td>");
			
			if(num%2==0){
				html.append("</tr>");
			}
	    	
			num++;
	    }
	    html.append("  <tr>").append("\n");
	    html.append("    <td></td><td><a style=\"float:right;cursor:pointer;font-size:15px;\" onclick=\"openMoreAppkm()\">>>更多</a></td>").append("\n");
	    html.append("  </tr>").append("\n");
	    html.append("</table>").append("\n");
	    return html.toString();
	  }
	
	public String getAppkmShowStr(int pageSize, int pageNo){

		StringBuffer html = new StringBuffer();
//		CmsAppkmDAO cmsAppkmDAO = (CmsAppkmDAO) SpringBeanUtil
//				.getBean("cmsAppkmDAO");
//		List<IworkCmsAppkm> cmsAppkmList = cmsAppkmDAO.getList();
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		List<String> lables = new ArrayList<String>();lables.add("TITLE");lables.add("URL");lables.add("TYPE");
		Map params = new HashMap();params.put(1, user.getOrgroleid()==3l?"非券商":"券商");
		List<HashMap> cmsAppkmList = com.iwork.commons.util.DBUtil.getDataList(lables, "SELECT TITLE,URL,TYPE FROM BD_GE_KJYYMKB WHERE TYPE=? ORDER BY ID", params);
		int line = 0;
		boolean flag = false;

		for (HashMap model : cmsAppkmList) {

			if (line >= (pageNo - 1) * pageSize) {
				if (line < pageNo * pageSize)
					flag = true;
			}

			if (flag) {
				//style=\"line-height:22px;\"
				html.append("<tr ><td class=\"text-overflow\">");
				html.append("<a href=\"").append(model.get("URL").toString()).append("\" target=\"_blank\" title=\"").append(model.get("URL").toString()).append("\">");
				html.append(model.get("TITLE").toString());
				html.append("</a>");
				html.append("</td>")
						.append("<td  class=\"data\">")
						.append("<a href=\"").append(model.get("URL").toString()).append("\" target=\"_blank\" >")
						.append(model.get("URL").toString()).append("</a>").append("</td>");
				html.append("</tr>");
			}

			line++;
			flag = false;
		}

		return html.toString();
	}
	
	public int getAppkmCount() {

//		CmsAppkmDAO cmsAppkmDAO = (CmsAppkmDAO) SpringBeanUtil
//		.getBean("cmsAppkmDAO");
//		List<IworkCmsAppkm> cmsAppkmList = cmsAppkmDAO.getList();

		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		List<String> lables = new ArrayList<String>();lables.add("TITLE");lables.add("URL");lables.add("TYPE");
		Map params = new HashMap();params.put(1, user.getOrgroleid()==3l?"非券商":"券商");
		List<HashMap> cmsAppkmList = com.iwork.commons.util.DBUtil.getDataList(lables, "SELECT TITLE,URL,TYPE FROM BD_GE_KJYYMKB WHERE TYPE=? ORDER BY ID", params);

		if (cmsAppkmList != null && cmsAppkmList.size() > 0) {
			return cmsAppkmList.size();
		}

		return 0;
	}
	public SysNavFrameService getSysNavFrameService() {
		return SysNavFrameService;
	}
	public void setSysNavFrameService(SysNavFrameService sysNavFrameService) {
		SysNavFrameService = sysNavFrameService;
	}
	
}
