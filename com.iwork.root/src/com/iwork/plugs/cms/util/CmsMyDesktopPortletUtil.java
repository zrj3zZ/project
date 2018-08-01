package com.iwork.plugs.cms.util;
import org.apache.log4j.Logger;

import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import news.common.HelloWebService;

import com.ibpmsoft.project.zqb.service.ZqbAnnouncementService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.eaglesearch.model.EaglesSearchConfModel;
import com.iwork.eaglesearch.service.EaglesSearchService;
import com.iwork.plugs.cms.dao.CmsAppkmDAO;
import com.iwork.plugs.cms.dao.CmsInfoDAO;
import com.iwork.plugs.cms.dao.CmsVideoDAO;
import com.iwork.plugs.cms.model.IworkCmsAppkm;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.iwork.plugs.cms.model.IworkCmsVideo;
import com.iwork.plugs.rss.dao.IworkPlugsRssDao;
import com.iwork.plugs.rss.model.BdInfRssdyjlb;
import com.iwork.plugs.rss.util.IworkPlugsRssUtil;
import com.iwork.plugs.weather.bean.CmsWeatherModel;
import com.iwork.plugs.weather.util.WeatherUtil;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * 我的桌面工具类
 * @author liuyiyang
 *
 */
public class CmsMyDesktopPortletUtil {
	private static Logger logger = Logger.getLogger(CmsMyDesktopPortletUtil.class);
	private static CmsMyDesktopPortletUtil instance = null;
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件

	private CmsMyDesktopPortletUtil() {
	}

	public static CmsMyDesktopPortletUtil getInstance() {
		if (instance == null) {
			instance = new CmsMyDesktopPortletUtil();
		}
		return instance;
	}

	// 行业新闻 1 培训 2新闻公告0 焦点图5 本周寿星6
	/**
	 * 获得系统栏目HTML
	 */
	public String getCommonShowStr(Long showId, int pageSize, int pageNo) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser us = uc.get_userModel();
		StringBuffer html = new StringBuffer();
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		Long hfxxbformid = Long.parseLong(config.get("hfxxbformid")) ;
		Long hfxxbdemid = Long.parseLong(config.get("hfxxbdemid"));
		
		Long wjformid = Long.parseLong(config.get("wjformid")) ;
		Long wjdemid = Long.parseLong(config.get("wjdemid"));
		if(showId==17l){
			OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
			String khbh = null;
			if(user.getOrgroleid()==3l){
				khbh = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1().toUpperCase();
			}
			ZqbAnnouncementService zqbAnnouncementService=null;
			if(zqbAnnouncementService==null){
				zqbAnnouncementService = (ZqbAnnouncementService) SpringBeanUtil.getBean("zqbAnnouncementService");
			 }
			List<HashMap> data = zqbAnnouncementService.getTZGGReceptionList(null,null,pageNo,pageSize);
			if( config.get("zqServer").equals("hlzq")){				
				com.ibpmsoft.project.zqb.service.ZqbProjectManageService zqbProjectManageService=null;
				if(zqbProjectManageService==null){
					zqbProjectManageService = (com.ibpmsoft.project.zqb.service.ZqbProjectManageService) SpringBeanUtil.getBean("zqbProjectManageService");
				 }
				List<HashMap> data2 = config.get("zqServer").equals("hlzq")?zqbProjectManageService.showDailyListForIndex(null, pageNo, pageSize, null, null, zqbProjectManageService.getConfig("xmrbformid"), null):new ArrayList();
				data.addAll(data2);
			}
			int tag=0;
			//拼接更多页面
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
				if(mobilelist.size()==1){
					extend2 = ((HashMap)mobilelist.get(0)).get("extend2")==null?"":((HashMap)mobilelist.get(0)).get("extend2").toString();
					status =((HashMap)mobilelist.get(0)).get("status").toString();
				}
				
				String tzbt = hashMap.get("TZBT").toString();
				String tzlx = hashMap.get("TZLX")==null?"":hashMap.get("TZLX").toString();
				tag = tzbt.startsWith("持续督导日常工作反馈(")?1:tzbt.startsWith("挂牌公司重大事项信息披露自查反馈表(")?2:3;
//				html.append("  <li style=\"display:block;height:30px;line-height:30px;min-width:1024px;width:auto;\">").append("\n");
//				html.append("    <i class=\"ace-icon fa fa-angle-right bigger-110\"></i>").append("\n");
				
				html.append("<tr><td class=\"text-overflow\">");
				if(hashMap.get("JSZT").equals("已回复")&&(hashMap.get("STATUS")==null||hashMap.get("STATUS").equals(""))){
					if(tag==1){
						html.append("<a href=\"#\" onClick='showCxddfkgzxx(").append(hashMap.get("ID")).append(");' title='").append(tzbt).append("'>").append(tzbt).append("</a>").append("\n");
						html.append("&nbsp;已反馈");
					}else if(tag==2){
						html.append("<a href=\"#\" onClick='showCustomer(\"").append(khbh).append("\",").append(hashMap.get("ID")).append(","+hashMap.get("FSSJ").toString().substring(8, 10)+");' title='").append(tzbt).append("'>").append(tzbt).append("</a>").append("\n");
						html.append("<a href=\"javascript:expWord('").append(khbh).append("',").append(hashMap.get("ID")).append(",'").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" style=\"color:blue;padding-left:40px;\">导出</a>");
						html.append("&nbsp;已反馈&nbsp;(<a href=\"javascript:showCommunicate('").append(khbh).append("',").append(hashMap.get("ID")).append(")\" style=\"color:blue;\">确认沟通</a>)");
					}else{
						if(hashMap.get("JSZT")!=null&&!hashMap.get("JSZT").equals("")&&hashMap.get("JSZT").toString().equals("已回复")){
							if("问卷调查".equals(tzlx)){
								html.append("<a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='showWJDC(\"").append(hashMap.get("ID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>");
								if(!status.equals("未回复")){
									html.append("<span style=\"color:black;padding-left:75px;\">"+status+"</span>");
									if(extend2!=""){
										html.append("&nbsp;("+extend2+"已阅)");
									}
								}	
								html.append("\n");
							}else{
								html.append("<a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")'\" title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>");
								if(!status.equals("未回复")){
									html.append("<span style=\"color:black;padding-left:75px;\">"+status+"</span>");
									if(extend2!=""){
										html.append("&nbsp;("+extend2+"已阅)");
									}
								}	
								html.append("\n");
							}
						}else{
							html.append("<a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")'\" title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>");
							if(!status.equals("未回复")){
								html.append("<span style=\"color:black;padding-left:50px;\">"+status+"</span>");
								if(extend2!=""){
									html.append("&nbsp;("+extend2+"已阅)");
								}
							}	
							html.append("\n");
						}
					}
				}else if(hashMap.get("JSZT").equals("已回复")&&(hashMap.get("STATUS")!=null||hashMap.get("STATUS").equals("已确认沟通"))){
					if(tag==1){
						html.append("<a href=\"#\" onClick='showCxddfkgzxx(").append(hashMap.get("ID")).append(");' title='").append(tzbt).append("'>").append(tzbt).append("</a>").append("\n");
						html.append("&nbsp;已反馈");
					}else if(tag==2){
						html.append("<a href=\"#\" onClick='showCustomer(\"").append(khbh).append("\",").append(hashMap.get("ID")).append(","+hashMap.get("FSSJ").toString().substring(8, 10)+");' title='").append(tzbt).append("'>").append(tzbt).append("</a>").append("\n");
						html.append("<a href=\"javascript:expWord('").append(khbh).append("',").append(hashMap.get("ID")).append(",'").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" style=\"color:blue;padding-left:40px;\">导出</a>");
						html.append("&nbsp;已反馈&nbsp;(<a href=\"javascript:updateCommunicate('").append(khbh).append("',").append(hashMap.get("ID")).append(")\" style=\"color:blue;\">已确认沟通</a>)");
					}else{
						html.append("<a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")'\" title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>");
						if(!status.equals("未回复")){
							html.append("<span style=\"color:black;padding-left:50px;\">"+status+"</span>");
							if(extend2!=""){
								html.append("&nbsp;("+extend2+"已阅)");
							}
						}	
						html.append("\n");
					}
				}else{
					if(tag==1){
						html.append("<a href=\"javascript:addCustomer('").append(hashMap.get("TZNR")).append("','").append(hashMap.get("ID")).append("')\" title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>").append("\n");
						html.append("&nbsp;未反馈");
					}else if(tag==2){
						html.append("<a href=\"#\" onClick='showCustomer(\"").append(khbh).append("\",").append(hashMap.get("ID")).append(","+hashMap.get("FSSJ").toString().substring(8, 10)+");' title='").append(tzbt).append("'>").append(tzbt).append("</a>").append("\n");
						html.append("<a href=\"javascript:expWord('").append(khbh).append("',").append(hashMap.get("ID")).append(",'").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" style=\"color:blue;padding-left:40px;\">导出</a>");
						html.append("&nbsp;未反馈");
					}else if(tzlx.equals("项目日志")){
						html.append("<a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='checkDaily(").append(hashMap.get("SFTZ").toString()).append(")' title='").append(tzbt).append("'>").append("").append(tzbt.length()>64?tzbt.substring(0, 32):tzbt).append("</a>");
						html.append("\n");
					}else if(tzlx.equals("问卷调查")){
						html.append("<a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(wjformid).append("\",\"").append(wjdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")'\" title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>");
						if(!status.equals("未回复")){
							html.append("<span style=\"color:black;padding-left:50px;\">"+status+"</span>");
							if(extend2!=""){
								html.append("&nbsp;("+extend2+"已阅)");
							}
						}	
						html.append("\n");
					}else{
						html.append("<a href = '#' style=\"display:inline-block;width:301.91px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")'\" title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>");
						if(!status.equals("未回复")){
							html.append("<span style=\"color:black;padding-left:50px;\">"+status+"</span>");
							if(extend2!=""){
								html.append("&nbsp;("+extend2+"已阅)");
							}
						}	
						html.append("\n");
					}
				}
				html.append("    <span style=\"float:right\">").append(hashMap.get("ZCHFSJ")).append("</span>").append("\n");
				html.append("</td><td  class=\"data\"></td></tr>");
			}
		}else if(showId==14l){
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
				List<news.service.Cxdd> data = HelloWebService.getCxdd(pageNo, pageSize, sql.toString());
				for (news.service.Cxdd cxdd : data) {
					html.append("<tr><td class=\"text-overflow\">");
					html.append("    <a target=\"_blank\" href='").append(cxdd.getHref().getValue()).append("'\" title='").append(cxdd.getTitle().getValue()).append("'>").append("").append(cxdd.getTitle().getValue());
					html.append("    <span style=\"float:right;width:100px;\">").append(cxdd.getTimes().getValue().substring(0, 16)).append("</span>").append("\n");
					html.append("    <span style=\"float:right;width:100px;\">").append(cxdd.getGsmc().getValue()).append("</span>").append("\n");
					html.append("    <span style=\"float:right;width:100px;\">").append(cxdd.getGfdm().getValue()).append("</span>").append("\n");
					html.append("</a></td><td  class=\"data\"></td></tr>");
				}
			} catch (Exception e) {
			}
		}else if(showId==0l){
			try {
				List<news.service.Ssxx> Ssxxlist =HelloWebService.getSsxx(pageNo,pageSize);
				int count=(pageNo-1)*pageSize+1;
				for (news.service.Ssxx ssxx : Ssxxlist) {
					html.append("<tr><td class=\"text-overflow\">");
					html.append("    <a target=\"_blank\" href='").append(ssxx.getHref().getValue()).append("'\" title='").append(ssxx.getTitle().getValue()).append("'>").append("").append(count+"、"+ssxx.getTitle().getValue());
					html.append("</a>");
					html.append("    <span style=\"float:right\">").append(ssxx.getTimes().getValue().substring(0, 16)).append("</span>").append("\n");
					html.append("</td><td  class=\"data\"></td></tr>");
					count++;
				}
			} catch (Exception e) {
			}
		}else{
			CmsInfoDAO cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
			List<IworkCmsContent> cmsInfoList = cmsInfoDAO.getNewsList(showId);
			int line = 0;
			boolean flag = false;
			
			for (IworkCmsContent model : cmsInfoList) {
				
				if (line >= (pageNo - 1) * pageSize) {
					if (line < pageNo * pageSize)
						flag = true;
				}
				
				if (model.getStatus() == null)
					model.setStatus(new Long(0)); // 如果状态为空，默认为不发布
				if (!CmsUtil.getContentSecurityList(model.getBrowse())) {
					continue;
				}
				String releaseDate = null;
				if (model.getReleasedate() != null) {
					releaseDate = CmsUtil.dateShortFormat(model.getReleasedate());
				}
				String resultDate = UtilDate.dateFormat(new Date());
				String newImg = "";
				if (releaseDate != null && resultDate != null) {
					if (CmsUtil.getSubtractionDate(model.getReleasedate(),
							new Date()) <= 5) {
						newImg = "&nbsp;(新)";
					}
				}
				if (flag) {
					html.append("<tr><td class=\"text-overflow\">");
					
					String fulltitle = CmsUtil.getCmsTitle(model);
					html.append(
							"<a href='#' onClick='openCms(" + model.getId()
							+ ")' title='")
							.append(model.getTitle() + newImg).append("'>")
							.append("·").append(fulltitle).append(newImg);
					
					int commentCount = CmsCommentUtil.getInstance()
							.getCommentCount(model.getId() + "");
					if (commentCount > 0) {
						html.append("&nbsp;&nbsp;<font color=\"#999999\">评论"
								+ commentCount + "次</font>");
					}
					
					html.append("</a></td><td  class=\"data\">").append(releaseDate).append("</td></tr>");
				}
				
				line++;
				flag = false;
			}
		}

		return html.toString();

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
		if(roleid!=5&&roleid!=6&&roleid!=7){
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
	 * 拼装前需要加入的HTML代码
	 * @param sb
	 * @return
	 */
	private StringBuffer moreStringBufferBeforeAppend(StringBuffer sb) {

		sb.append("<tr><td>")
				.append("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"2\" border=\"0\">")
				.append("<tbody><tr>").append("<td width=\"46%\">");
		return sb;
	}

	/**
	 * 拼装后需要加入的HTML代码
	 * @param sb
	 * @return
	 */
	private StringBuffer moreStringBufferAfterAppend(StringBuffer sb) {

		sb.append("<tr><td class=\"text-overflow\">");
		return sb;
	}

	/**
	 * 默认10条记录
	 * @param showId
	 * @return
	 */
	public String getCommonShowStr(Long showId) {
		int pageSize = 10;
		return getCommonShowStr(showId, pageSize, 1);
	}

	/**
	 * 获得天气栏目HTML
	 * @param cityName 城市名称
	 * @return
	 */
	public String getWeatherShowStr(String cityName) {
		StringBuffer html = new StringBuffer();
		String sql = "SELECT template FROM IWORK_CMS_PORTLET where portletkey = 'Stock'";
		cityName = DBUtil.getString(sql, "template");
		if (cityName == null) {
			cityName = "北京";
		}
		CmsWeatherModel model = WeatherUtil.getInstance().getCmsWeatherModelByCityName(cityName);
		html.append("<table style=\"width:100%\">");
		html.append("<tr><td colspan='2'>").append(model.getCity()).append("<td></tr>\n");
		html.append("<tr><td class='weatherTitle'>").append("今日温度：").append("</td><td class='weatherData'>").append(model.getHighest()).append("<td></tr>\n");
		html.append("<tr><td class='weatherTitle'>").append("天气情况：").append("</td><td class='weatherData'>").append(model.getZwx_s()).append("<td></tr>\n");
		html.append("<tr><td class='weatherTitle'>").append("穿衣说明：").append("</td><td class='weatherData'>").append(model.getChy_shuoming()).append("<td></tr>\n");
		html.append("<tr><td class='weatherTitle'>").append("活动建议：").append("</td><td class='weatherData'>").append(model.getSsd_s()).append("<td></tr>\n");
		html.append("<tr><td class='weatherTitle'>").append("洗车注意：").append("</td><td class='weatherData'>").append(model.getXcz_s()).append("<td></tr>\n");
		html.append("</table>"); 
		return html.toString();
	}
	
	/**
	 * 获取视频栏目HTML
	 * @return
	 */
	public String getVideoShowStr() {

		String name = "default.flv";
		StringBuffer sb = new StringBuffer();
		Date timeNow = null;
		CmsVideoDAO cmsVideoDAO = (CmsVideoDAO) SpringBeanUtil
				.getBean("cmsVideoDAO");
		List<IworkCmsVideo> list = cmsVideoDAO.getVideo(name);
		if (list == null || list.size() == 0) {
			sb.append("未发现视频");
		} else {
			try {
				for (int i = 0; i < list.size(); i++) {
					IworkCmsVideo model = list.get(i);
					String title = model.getTitle();
					String picFile = model.getPicfile();
					Date uploadTime = model.getUploadtime();
					if (title == null || "".equals(title)) {
						title = "空标题";
					}
					String video_path = "../iwork_video/" + name;
					String pic_path = "../iwork_video/" + picFile;

					Date date = new Date();
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String now = df.format(date);
					try {
						timeNow = df.parse(now);
					} catch (Exception e) {
						logger.error(e,e);
					}
					sb.append("<div>").append("\n");
					sb.append(
							"	<div id='container'><a href='http://www.macromedia.com/go/getflashplayer'>Get the Flash Player</a></div>")
							.append("\n");
					sb.append(
							"	<script type='text/javascript' src='../iwork_js/plugs/video/swfobject.js'></script>")
							.append("\n");
					sb.append("	<script type='text/javascript'>").append("\n");
					sb.append(
							"		var s1 = new SWFObject('../iwork_video/player.swf','ply','100%','100%','9','#FFFFFF');")
							.append("\n");
					sb.append("		s1.addParam('allowfullscreen','true');")
							.append("\n");
					sb.append("		s1.addParam('wmode','opaque');").append("\n");
					sb.append("		s1.addParam('allowscriptaccess','always');")
							.append("\n");
					sb.append("		s1.addParam('flashvars','file=")
							.append(video_path)
							.append("&image=" + pic_path + "');").append("\n");
					sb.append("		s1.write('container');").append("\n");
					sb.append("	</script>").append("\n");
					sb.append("</div>").append("\n");
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取常用资料HTML
	 * @return
	 */
	public String getAppkmShowStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"lft_u_cnt\">").append("\n");
		CmsAppkmDAO cmsAppkmDAO = (CmsAppkmDAO) SpringBeanUtil
				.getBean("cmsAppkmDAO");
		List<IworkCmsAppkm> list = cmsAppkmDAO.getList();
		for (int i = 0; i < list.size(); i++) {
			IworkCmsAppkm model = list.get(i);
			String url = model.getUrl();
			sb.append("<span class=\"Item\">");
			sb.append("<a href=\"").append(url)
					.append("\" target=\"_blank\" >").append(model.getTitle())
					.append("</a>").append("\n");
			sb.append("</span>");
		}
		sb.append("</div>").append("\n");
		return sb.toString();
	}

	/**
	 * 根据用户ID获取RSS内容
	 * @param userId
	 * @return
	 */
	public String getKsBlogShowStr(String userId) {

		int line = 10;
		String url = "http://blog.sina.com.cn/rss/1298306070.xml";
		String result = "";

		String sql = "SELECT template FROM IWORK_CMS_PORTLET where portletkey = 'ksBlog'";
		String showKeyWord = DBUtil.getString(sql, "template");
		if ("blog".equals(showKeyWord.toLowerCase())) {
			result = indexFeedReader(line, url);
		} else {
			result = getRssList(userId, showKeyWord);
		}

		return result;
	}

	/**
	 * 获得订阅消息列表
	 * 
	 * @param uid
	 * @return
	 */
	public String getRssList(String uid, String chooseShowType) {
		StringBuffer sb = new StringBuffer();
		IworkPlugsRssDao rssInformationDao = (IworkPlugsRssDao) SpringBeanUtil
				.getBean("rssInformationDao");
		List<BdInfRssdyjlb> list = rssInformationDao.getIndexList(uid);
		String[] showKeyWords = chooseShowType.split("-");
		if (showKeyWords.length > 1) {
			chooseShowType = showKeyWords[1];
		} else {
			return "";
		}
		if (list == null || list.size() == 0) {
			sb.append("您还没有添加消息订阅");
		} else {
			try {
				for (int i = 0; i < list.size(); i++) {
					BdInfRssdyjlb model = list.get(i);
					int line = Integer.parseInt(model.getLinesize().toString());
					String type = model.getType();
					String keyWord = model.getKeyword();
					String url = model.getRssurl();

					// 非RSS订阅方式，获取URL --搜索引擎+关键字
					if (!type.equals("RSS")) {
						if (type.equals("BAIDU")) {
							keyWord = keyWord.replaceAll("\\s+", "+");
						}
						url = (String) IworkPlugsRssUtil.hash.get(type)
								+ keyWord;
					}

					if (model.getTitle().equals(chooseShowType)) {
						sb.append(indexFeedReader(line, url));
					}
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取RSS订阅信息XML并解析
	 * 
	 * @param line
	 * @param url
	 * @return
	 */
	private static String indexFeedReader(int line, String url) {
		if (line > 10) {
			line = 10;// 默认显示行数
		}
		StringBuffer sb = new StringBuffer();
		try {
			URLConnection feedUrl = new URL(url).openConnection();
			feedUrl.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			List list = feed.getEntries();
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			sb.append("<ul class=\"item_ul\">\n");
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (i == line) {
						break;
					} else {
						SyndEntry entry = (SyndEntry) list.get(i);
						String author = entry.getAuthor();
						Date date = entry.getPublishedDate();
						if (date == null) {
							sb.append("<li>");
							sb.append("<a href='").append(entry.getLink())
									.append("' target='_blank' title='")
									.append(entry.getTitle()).append("'>")
									.append(entry.getTitle()).append("</a>");
							sb.append("<span class=\"time\" title=\"")
									.append(entry.getAuthor()).append("\">")
									.append(entry.getAuthor())
									.append("</span>");
							sb.append("</li>");
						} else if (author == null || "".equals(author.trim())) {
							sb.append("<li>\n");
							sb.append("<a href='").append(entry.getLink())
									.append("' target='_blank' title='")
									.append(entry.getTitle()).append("'>")
									.append(entry.getTitle()).append("</a>");
							sb.append("<span class=\"time\" >")
									.append(sdf.format(entry.getPublishedDate()))
									.append("&nbsp;</span>\n");
							sb.append("</li>\n");
						} else {
							sb.append("<li>\n");
							sb.append("<a href='").append(entry.getLink())
									.append("' target='_blank' title='")
									.append(entry.getTitle()).append("'>")
									.append(entry.getTitle()).append("</a>");
							sb.append("<span class=\"time\" >")
									.append(sdf.format(entry.getPublishedDate()))
									.append("&nbsp;</span>\n");
							sb.append("</li>\n");
						}
					}
				}
			} else {
				sb.append("<li>没有数据</li>");
			}
			sb.append("</ul>");
		} catch (Exception e) {
			sb.append("<br><br><div align=center><font color=gray>读RSS数据时失败</font></div><br><br>");
			logger.error(e,e);
		}
		return sb.toString();
	}

	/**
	 * 轮播图栏目HTML
	 * @return
	 */
	public String getPicShowStr(String showType,Long portaletId) {

		String picWidth = "300px";
		if("myDesktop".equals(showType)){
			//picWidth = "100%";
		}
		
		CmsInfoDAO cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil
				.getBean("cmsInfoDAO");
		List<IworkCmsContent> maplist = null;
		if(portaletId==null||portaletId.equals(new Long(0))){
			 maplist = cmsInfoDAO.getCmsList(0l);
		}else{
			 maplist = cmsInfoDAO.getCmsList(portaletId);
		} 
		StringBuffer sb = new StringBuffer();
		sb.append("<div id=\"KinSlideshow\" style=\"visibility:hidden;\">");

		int flagCount = 0;

		for (int i = 0; i < maplist.size(); i++) {
			IworkCmsContent iworkCmsContent = maplist.get(i);
			String picUrl = iworkCmsContent.getPrepicture();
			if (picUrl != null && picUrl.trim().length() > 10
					&& iworkCmsContent.getStatus() == 0) {
				// cmsOpen.action?infoid=8
				sb.append("<a href=\"cmsOpen.action?infoid="
						+ iworkCmsContent.getId()
						+ "\" target=\"_blank\"><img onerror=\"javascript:this.src='../iwork_img/nopic1.gif'\" src=\"../" + picUrl
						+ "\" width=\"" + picWidth + "\" height=\"215\" alt=\""
						+ iworkCmsContent.getBrieftitle() + "\" /></a>");
				flagCount++;
			}
			if (flagCount == 6) {
				break;
			}
		}
		sb.append("</div>");

		return sb.toString();
	}

	/**
	 * 获取系统栏目总数
	 * @param showId
	 * @return
	 */
	public int getCommonTypeCount(Long showId) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser us = uc.get_userModel();
		CmsInfoDAO cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		if(showId==17l){
			ZqbAnnouncementService zqbAnnouncementService=null;
			if(zqbAnnouncementService==null){
				zqbAnnouncementService = (ZqbAnnouncementService) SpringBeanUtil.getBean("zqbAnnouncementService");
			}
			int num = zqbAnnouncementService.getTZGGReceptionListSize(null, null);
			if (num > 0) {
				return num;
			}
		}else if(showId==14l){
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
			int num = HelloWebService.getCxddCount(sql.toString());
			if (num > 0) {
				return num;
			}
		}else if(showId==0l){
			int num = HelloWebService.getSsxxCount();
			if (num > 0) {
				return num;
			}
		}else{
			List<IworkCmsContent> cmsInfoList = cmsInfoDAO.getNewsList(showId);
	
			if (cmsInfoList != null && cmsInfoList.size() > 0) {
				return cmsInfoList.size();
			}
		}
		return 0;
	}

	/**
	 * 从数据库查询更多时显示的标题
	 * @param portletId 栏目ID
	 * @return
	 */
	public String getMoreTitleStr(long portletId) {

		String sql = "SELECT * FROM IWORK_CMS_PORTLET where id=" + portletId;
		String result = DBUtil.getString(sql, "PORTLETNAME");
		if (result != null) {
			return result;
		} else {
			return "";
		}
	}

	/**
	 * 获得首页查询类型HTML
	 * @return
	 */
	public String getSearchTypeHtml() {

		StringBuffer sb = new StringBuffer();
		List<EaglesSearchConfModel> typeList = new ArrayList<EaglesSearchConfModel>();
		EaglesSearchService eaglesSearchService = (EaglesSearchService) SpringBeanUtil
				.getBean("eaglesSearchService");
		typeList = eaglesSearchService.getEaglesSearchTabList();
		String defHtml = "<li><a onclick=\"showTag('ALL')\" href=\"javascript:void(0)\" id=\"tag_ALL\">全部</a></li>";
		sb.append(defHtml);
		for (EaglesSearchConfModel model : typeList) {

			sb.append("<li><a onclick=\"showTag('" + model.getEsType()
					+ "')\" href=\"javascript:void(0)\" id=\"tag_"
					+ model.getEsType() + "\">" + model.getTitle()
					+ "</a></li>");

		}

		return sb.toString();
	}

}
