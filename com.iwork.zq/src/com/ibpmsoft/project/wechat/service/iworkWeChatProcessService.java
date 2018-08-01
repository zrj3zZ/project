package com.ibpmsoft.project.wechat.service;

import com.ibpmsoft.project.zqb.service.ZqbAnnouncementService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.km.know.model.IworkKnowQuestion;
import com.iwork.plugs.cms.dao.CmsInfoDAO;
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
import news.service.Ssxx;
import org.apache.log4j.Logger;

public class iworkWeChatProcessService
{
  private boolean isMore = false;
  private CmsInfoDAO cmsInfoDAO;
  private final int limit = 10;
  private final int maxLength = 35;
  private static Logger logger = Logger.getLogger(iworkWeChatProcessService.class);
  public static final String CN_FILENAME = "/common.properties";
  
  public String getListrc(String userid, int pageNo, int itemNum, String nowdate) 
  {
	  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	  String date =null;
	  Calendar   cal   =   Calendar.getInstance();
	  Integer d=null;
	  if(nowdate!=null&&!"".equals(nowdate)){
		  
		  d=Integer.parseInt(nowdate);
	  }
	  cal.add(Calendar.DATE,  d); 
	  date= new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
	 
	  String yue = cal.get(Calendar.MONTH)+"";
	  String ri = cal.get(Calendar.DATE)+"";
	  String week = cal.get(Calendar.DAY_OF_WEEK)+"";
	 
    this.isMore = false;
    StringBuffer html = new StringBuffer();
    StringBuffer sql1 = new StringBuffer();
    StringBuffer sql = new StringBuffer();
    sql1.append("select distinct b.startdate,b.enddate,b.title,b.id,b.re_startdate,b.re_enddate from ");
    sql1.append(" (select distinct a.startdate,a.enddate,a.title,a.starttime,a.endtime,a.id,a.re_startdate,a.re_enddate,a.re_starttime,a.re_endtime,re_month_days,re_year_month,re_year_days,re_week_date from iwork_sch_calendar a where a.userid=?) b ");
    sql1.append(" where (b.startdate<= to_date(?,'yyyy-MM-dd')  and (b.enddate>= to_date(?,'yyyy-MM-dd') or b.enddate is null) or b.startdate is null) ");
    sql1.append(" and (b.re_startdate<= to_date(?,'yyyy-MM-dd') or b.re_startdate is null)");
    sql1.append(" and (b.re_enddate>= to_date(?,'yyyy-MM-dd') or re_enddate is null) ");
    sql1.append(" and (b.re_week_date=? or re_week_date is null) ");
    sql1.append(" and (b.re_month_days=? or re_month_days is null) ");
    sql1.append(" and ((b.re_year_month=? and b.re_year_days=?) or re_year_month is null) ");
    sql1.append(" order by b.id ");
//    分割线------------------------------------------------------------------------
    sql.append("select distinct tt.startdate,tt.enddate,tt.title,tt.id,tt.re_startdate,tt.re_enddate,tt.txrq  ");
    sql.append("  from (select s.id,s.title,"+
          "  s.re_startdate,"+
            " s.re_enddate, "+
            " 1 qs, "+
            " (select zqdm from bd_zqb_kh_base y where y.customerno = z.khbh) dm, "+
            " (select zqjc from bd_zqb_kh_base y where y.customerno = z.khbh) jc, "+
            " s.startdate, "+
            " s.enddate, "+
            " (select y.mobile from orguser y where y.userid = s.userid) ddsj, "+
           " (select y.mobile "+
              " from orguser y "+
              " where y.userid = (select zqdm "+
              "                    from bd_zqb_kh_base y "+
             "                    where y.customerno = z.khbh)) dmsj, "+
           "  s.isalert, "+
          "   ss.txrq txrq, "+
        "    s.userid "+
       " from iwork_sch_calendar s "+
       " left join bd_xp_tyrcglb t "+
      "   on to_char(s.id) = to_char(t.mainid) "+
      "  left join BD_XP_QTGGZLLC z "+
       "  on to_char(z.instanceid) = to_char(t.subid) "+
      " left join bd_xp_xpsxqtb ss "+
     "    on ss.xpsxid = s.id "+
    "  where z.id is not null "+
    " UNION "+
    "  select s.id,s.title, "+
            " s.re_startdate, "+
            " s.re_enddate, "+
            " 1 qs, "+
           " (select zqdm from bd_zqb_kh_base y where y.customerno = z.khbh) dm, "+
            " (select zqjc from bd_zqb_kh_base y where y.customerno = z.khbh) jc, "+
           " s.startdate, "+
           " s.enddate, "+
            " (select y.mobile from orguser y where y.userid = s.userid) ddsj, "+
           "  (select y.mobile "+
             "  from orguser y "+
            "  where y.userid = (select zqdm "+
          "                        from bd_zqb_kh_base y "+
         "                        where y.customerno = z.khbh)) dmsj, "+
         "   s.isalert, "+
        "    ss.txrq txrq, "+
        "    s.userid "+
       " from iwork_sch_calendar s "+
       " left join bd_xp_tyrcglb t "+
        "  on to_char(s.id) = to_char(t.mainid) "+
       " left join RCYWCB z "+
       "   on to_char(z.instanceid) = to_char(t.subid) "+
      "  left join bd_xp_xpsxqtb ss "+
      "   on ss.xpsxid = s.id "+
     "  where z.id is not null "+
    "  UNION "+
    "  select s.id,s.title, "+
           "  s.re_startdate, "+
           " s.re_enddate, "+
          "   0 qs, "+
            " '0' dm, "+
            " '0' jc, "+
           " s.startdate, "+
          "  s.enddate, "+
         "  (select y.mobile from orguser y where y.userid = s.userid) ddsj, "+
        "    '0' dmsj, "+
       "     s.isalert, "+
       "     ss.txrq txrq, "+
       "     s.userid  "+
      " from iwork_sch_calendar s "+
      " left join bd_xp_tyrcglb t "+
       "  on t.mainid = s.id "+
      " left join bd_xp_xpsxqtb ss "+
      "   on ss.xpsxid = s.id "+
      " where t.id is null) tt "+
" where (to_char(tt.startdate, 'yyyy-MM-dd') = to_char(");
    if(nowdate.equals("1")){
    sql.append("sysdate+1, ");
    }else if(nowdate.equals("2")){
    	sql.append("sysdate+2, ");
    }else{
    	sql.append("sysdate, ");
    }
    sql.append("'yyyy-MM-dd') "+
 " or to_char(tt.txrq, 'yyyy-MM-dd') = to_char(");
    
    if(nowdate.equals("1")){
        sql.append("sysdate+1, ");
        }else if(nowdate.equals("2")){
        	sql.append("sysdate+2, ");
        }else{
        	sql.append("sysdate, ");
        }
    
    sql.append("'yyyy-MM-dd')) and tt.userid=?  ");

   
    
    List<HashMap> userdata = new ArrayList();
    Connection conn = null;
    PreparedStatement stmt = null;
    PreparedStatement stmt1 = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    String startdate;
    try
    {
      conn = com.iwork.core.db.DBUtil.open();
      stmt = conn.prepareStatement(sql.toString());
      stmt1 = conn.prepareStatement(sql1.toString());
//      stmt.setString(1, nowdate);
//      stmt.setString(2, nowdate);
      stmt.setString(1, userid);
      stmt1.setString(1, userid);
      stmt1.setString(2, date);
      stmt1.setString(3, date);
      stmt1.setString(4, date);
      stmt1.setString(5, date);
      stmt1.setString(6, week);
      stmt1.setString(7, ri);
      stmt1.setString(8, yue);
      stmt1.setString(9, ri);
      rs=stmt.executeQuery();
      rs1=stmt1.executeQuery();
      while (rs.next())
      {
        HashMap<String, Object> map = new HashMap();
        startdate = rs.getObject(1) == null ? "" : sdf.format(rs.getObject(1));
        String enddate = rs.getObject(2) == null ? "" : sdf.format(rs.getObject(2));
        String title = rs.getString(3);
        
        Long id = Long.valueOf(rs.getLong(4));
        String re_startdate = rs.getObject(5) == null ? "" : sdf.format(rs.getObject(5));
        String re_enddate = rs.getObject(6) == null ? "" : sdf.format(rs.getObject(6));
        if("".equals(startdate)){
        map.put("startdate", startdate);
        map.put("enddate", enddate);
        map.put("title", title);
      
        map.put("id", id);
        map.put("re_startdate", re_startdate);
        map.put("re_enddate", re_enddate);
       
        userdata.add(map);
        }
      }
      while (rs1.next())
      {
       
       
        startdate = rs1.getObject(1) == null ? "" : sdf.format(rs1.getObject(1));
        String enddate = rs1.getObject(2) == null ? "" : sdf.format(rs1.getObject(2));
        String title = rs1.getString(3);
        
        Long id = Long.valueOf(rs1.getLong(4));
        String re_startdate = rs1.getObject(5) == null ? "" : sdf.format(rs1.getObject(5));
        String re_enddate = rs1.getObject(6) == null ? "" : sdf.format(rs1.getObject(6));
      
        HashMap<String, Object> map = new HashMap();
        map.put("startdate", startdate);
        map.put("enddate", enddate);
        map.put("title", title);
      
        map.put("id", id);
        map.put("re_startdate", re_startdate);
        map.put("re_enddate", re_enddate);
       
        userdata.add(map);
        
      }
    }
    catch (Exception e)
    {
      logger.error(e,e);
    }
    finally
    {
      com.iwork.core.db.DBUtil.close(conn, stmt, rs);
    }
    int num = itemNum;
    for (HashMap ttm : userdata)
    {
      String read_css = "";
      html.append("<a  id=\"notice_").append("\" class=\"weui_cell\" href='#'>").append("\n");
      if ((!"".equals(ttm.get("startdate"))) && (ttm.get("startdate") != null)) {
        html.append("<div class=\"memo\"><span style=\"float:right\">").append("开始时间：").append(ttm.get("startdate")).append("</span></div>");
      } else {
        html.append("<div class=\"memo\"><span style=\"float:right\">").append("开始时间：").append(ttm.get("re_startdate")).append("</span></div>");
      }
      html.append("<div href='#'>");
      
      html.append("<a href='#' onclick='loadrc(" + ttm.get("id") + ")'>");
      html.append("&nbsp;&nbsp;&nbsp;").append(ttm.get("title")).append("</a></div>").append("\n");
      html.append("</div>").append("\n");
      html.append("</a>").append("\n");
    }
    return html.toString();
  }
  
  public String getListrcMX(String userid, String title)
  {
    this.isMore = false;
    StringBuffer html = new StringBuffer();
    StringBuffer sql = new StringBuffer();
    sql.append("select * from iwork_sch_calendar a where a.userid=? and a.title=?");
    
    Map params = new HashMap();
    params.put(Integer.valueOf(1), userid);
    params.put(Integer.valueOf(2), title);
    List<HashMap> userdata = com.iwork.commons.util.DBUtil.getDataList(null, sql.toString(), params);
    for (HashMap ttm : userdata)
    {
      html.append("<a  id=\"notice_").append(ttm.get("TITLE")).append("\" class=\"weui_cell\" >").append("\n");
      html.append("<img src=\"iwork_img/readed.gif\" alt=\"\">").append("\n");
      html.append("<i class=\"item_icon\"></i>").append("\n");
      html.append("<div class=\"weui_cell_bd weui_cell_primary item_title\">").append("\n");
      html.append("</div>").append("\n");
      html.append("</a>").append("\n");
    }
    return html.toString();
  }
  
  public String getLisWXWD(int pageNo)
  {
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    StringBuffer sb = new StringBuffer();
    
    int line = 0;
    int rows = 0;
    if (this.cmsInfoDAO == null) {
      this.cmsInfoDAO = ((CmsInfoDAO)SpringBeanUtil.getBean("cmsInfoDAO"));
    }
    List<IworkKnowQuestion> questionList = null;
    questionList = this.cmsInfoDAO.searchSolvedQuestionList(null, null, pageNo, 10);
    for (IworkKnowQuestion model : questionList)
    {
      if (line >= 10) {
        break;
      }
      String content = "";
      if (model.getQcontent().length() > 35) {
        content = getSubStr(model.getQcontent(), 35);
      } else {
        content = model.getQcontent();
      }
      sb.append("<a  id=\"notice_").append("\" class=\"weui_cell\" href='#'>").append("\n");
      sb.append("<img src=\"iwork_img/readed.gif\" alt=\"\">").append("\n");
      sb.append("<i class=\"item_icon\"></i>").append("\n");
      sb.append("<div class=\"weui_cell_bd weui_cell_primary item_title\">").append("\n");
      
      sb.append("    <div href='#' onClick='openQuestionWin(").append(model.getId()).append(");").append("' title=\"").append(model.getQcontent()).append("\">").append(content).append("</div>").append("\n");
      if (model.getScore().longValue() > 0L) {
        sb.append("    <img src=\"/iwork_img/know/repo_rep.gif\" /><font color=\"#C0120B\">").append(model.getScore()).append("</font>&nbsp;&nbsp;");
      }
      sb.append("<div class=\"memo\"><span style=\"float:right\">").append("回答数：").append(model.getIworkKnowAnswers().size()).append("|").append(model.getQbegintime()).append("</span></div>\n");
      sb.append("</div>").append("\n");
      sb.append("</a>").append("\n");
      if (line == rows - 1)
      {
        line++;
        break;
      }
      line++;
    }
    sb.append("</ul>").append("\n");
    return sb.toString();
  }
  
  public String getLisXXWXWD(String title)
  {
    StringBuffer sb = new StringBuffer();
    StringBuffer sql = new StringBuffer();
    
    int line = 0;
    int rows = 0;
    
    sql.append("select a.qcontent,a.quname,a.qbegintime, a.score,a.id from Iwork_Know_Question a where a.qcontent like ?");
    
    List<HashMap> userdata = new ArrayList();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String qcontent;
    try
    {
      conn = com.iwork.core.db.DBUtil.open();
      stmt = conn.prepareStatement(sql.toString());
      stmt.setString(1, "%" + title + "%");
      rs = stmt.executeQuery();
      while (rs.next())
      {
        HashMap<String, Object> map = new HashMap();
        qcontent = rs.getString(1);
        String quname = rs.getString(2);
        String qbegintime = rs.getString(3);
        String score = rs.getString(4);
        String id = rs.getString(5);
        map.put("qcontent", qcontent);
        map.put("quname", quname);
        map.put("qbegintime", qbegintime);
        map.put("score", score);
        map.put("id", id);
        userdata.add(map);
      }
    }
    catch (Exception e)
    {
      logger.error(e,e);
    }
    finally
    {
      com.iwork.core.db.DBUtil.close(conn, stmt, rs);
    }
    for (HashMap model : userdata)
    {
      if (line >= 10) {
        break;
      }
      String content = "";
      if (model.get("qcontent").toString().length() > 35) {
        content = getSubStr(model.get("qcontent").toString(), 35);
      } else {
        content = model.get("qcontent").toString();
      }
      sb.append("<a  id=\"notice_").append("\" class=\"weui_cell\" href='#'>").append("\n");
      sb.append("<img src=\"iwork_img/readed.gif\" alt=\"\">").append("\n");
      sb.append("<i class=\"item_icon\"></i>").append("\n");
      sb.append("<div class=\"weui_cell_bd weui_cell_primary item_title\">").append("\n");
      
      sb.append("    <div href='#' onClick='openQuestionWin(").append(model.get("id")).append(");").append("' title=\"").append(model.get("qcontent").toString()).append("\">").append(content).append("</div>").append("\n");
      if (!model.get("score").equals("0")) {
        sb.append("    <img src=\"/iwork_img/know/repo_rep.gif\" /><font color=\"#C0120B\">").append(model.get("score")).append("</font>&nbsp;&nbsp;");
      }
      sb.append("<div class=\"memo\"><span style=\"float:right\">").append("提问人：").append(model.get("quname").toString()).append("|").append(model.get("qbegintime")).append("</span></div>\n");
      sb.append("</div>").append("\n");
      sb.append("</a>").append("\n");
      if (line == rows - 1)
      {
        line++;
        break;
      }
      line++;
    }
    return sb.toString();
  }
  
  public String getTZGG(int pageNo)
  {
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    OrgUser us = uc.get_userModel();
    StringBuffer sb = new StringBuffer();
    Map<String, String> config = ConfigUtil.readAllProperties("/common.properties");
    Long hfxxbformid = Long.valueOf(Long.parseLong((String)config.get("hfxxbformid")));
    Long hfxxbdemid = Long.valueOf(Long.parseLong((String)config.get("hfxxbdemid")));
    
    Long wjformid = Long.valueOf(Long.parseLong((String)config.get("wjformid")));
    Long wjdemid = Long.valueOf(Long.parseLong((String)config.get("wjdemid")));
    int line = 0;
    int rows = 0;
    if (this.cmsInfoDAO == null) {
      this.cmsInfoDAO = ((CmsInfoDAO)SpringBeanUtil.getBean("cmsInfoDAO"));
    }
    ZqbAnnouncementService zqbAnnouncementService = null;
    if (zqbAnnouncementService == null) {
      zqbAnnouncementService = (ZqbAnnouncementService)SpringBeanUtil.getBean("zqbAnnouncementService");
    }
    List<HashMap> data = zqbAnnouncementService.getTZGGReceptionList(null, null, pageNo, 10);
    OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
    String khbh = null;
    if (user.getOrgroleid().longValue() == 3L) {
      khbh = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getExtend1().toUpperCase();
    }
    int tag = 0;
    for (HashMap hashMap : data)
    {
      String sqls = "select extend2,status from Bd_Xp_Hfqkb where ggid= ? and userid= ? ";
      Map params = new HashMap();
      params.put(Integer.valueOf(1), hashMap.get("ID"));
      params.put(Integer.valueOf(2), user.getUserid());
      List lables = new ArrayList();
      lables.add("extend2");
      lables.add("status");
      
      List mobilelist = com.iwork.commons.util.DBUtil.getDataList(lables, sqls, params);
      String extend2 = "";
      String status = "";
      if (mobilelist.size() > 0)
      {
        extend2 = ((HashMap)mobilelist.get(0)).get("extend2") == null ? "" : ((HashMap)mobilelist.get(0)).get("extend2").toString();
        status = ((HashMap)mobilelist.get(0)).get("status").toString();
      }
      String tzbt = hashMap.get("TZBT") == null ? "" : hashMap.get("TZBT").toString();
      String tzlx = hashMap.get("TZLX") == null ? "" : hashMap.get("TZLX").toString();
      tag = tzbt.startsWith("持续督导日常工作反馈(") ? 1 : tzbt.startsWith("挂牌公司重大事项信息披露自查反馈表(") ? 2 : 3;
      sb.append("<table width=\"100%\" border=\"1\" bordercolor=\"#a0c6e5\" frame=below rules=none><tr><td>");
      sb.append("<a  id=\"notice_").append("\" class=\"weui_cell\" href='#'>").append("\n");
      if((hashMap.get("JSZT").equals("已回复")) && ((hashMap.get("STATUS") == null) || (hashMap.get("STATUS").equals("")))||"已回复".equals(status)){
      sb.append("<img src=\"iwork_img/read.gif\" alt=\"\" />");
      }else{
    	  sb.append("<img src=\"iwork_img/unread.gif\" alt=\"\" />");  
      }//<i class=\"item_icon\"></i>
     
      sb.append("</td><td><span style=\"padding-left:4px;float:right;\">").append(hashMap.get("FSSJ").toString().substring(0,11)).append("</span> ").append("</td></tr>");
      sb.append("<tr>");
      if ((hashMap.get("JSZT").equals("已回复")) && ((hashMap.get("STATUS") == null) || (hashMap.get("STATUS").equals(""))))
      {
        if (tag == 1)
        {
          sb.append("<td colspan=\"2\"><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href=\"#\" onClick='showCxddfkgzxx(").append(hashMap.get("ID")).append(");' title='").append(tzbt).append("'>").append(tzbt).append("</a>").append("");
          sb.append("<span style=\"color:black;float:right;font-size:15px;font-weight:300;\">已反馈</span></td><td>");
        }
        else if (tag == 2)
        {
          sb.append("<td colspan=\"2\"><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href=\"#\" onClick='showCustomer(\"").append(khbh).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("\");' title='").append(tzbt).append("'>").append(tzbt).append("</a>");
          //sb.append("\n");
          sb.append("<a href=\"javascript:expWord('").append(khbh).append("',").append(hashMap.get("ID")).append(")\" style=\"color:blue;padding-left:40px;\"></a>");
          //&nbsp;(<a href=\"javascript:showCommunicate('").append(khbh).append("',").append(hashMap.get("ID")).append(")\" style=\"color:blue;\">确认沟通</a>)
          sb.append("<span style=\"color:black;float:right;font-size:15px;font-weight:300;\">已反馈</span></td><td>");
        }
        else if (tzlx.equals("问卷调查"))
        {
          if ((hashMap.get("JSZT") != null) && (!hashMap.get("JSZT").equals("")) && (hashMap.get("JSZT").toString().equals("已回复")))
          {
            sb.append("<td ><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href = '#' style=\"display:block;width:190px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='showWJDC(\"").append(hashMap.get("ID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
            if (!status.equals("未回复"))
            {
              sb.append("<td><span style=\"color:black;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
//              if (extend2 != "") {
//                sb.append("&nbsp;(" + extend2 + "已阅)");
//              }
            }
            else
            {
              sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
            }
            //sb.append("\n");
          }
          else
          {
            sb.append("<td ><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href = '#' style=\"display:block;width:190px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(wjformid).append("\",\"").append(wjdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
            if (!status.equals("未回复"))
            {
              sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
//              if (extend2 != "") {
//                sb.append("&nbsp;(" + extend2 + "已阅)");
//              }
            }
            else
            {
              sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
            }
            //sb.append("\n");
          }
        }
        else
        {
          sb.append("<td ><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href = '#' style=\"display:block;width:190px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
          if (!status.equals("未回复"))
          {
            sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
//            if (extend2 != "") {
//              sb.append("&nbsp;(" + extend2 + "已阅)");
//            }
          }
          else
          {
            sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
          }
          //sb.append("\n");
        }
      }
      else if ((hashMap.get("JSZT").equals("已回复")) && ((hashMap.get("STATUS") != null) || (hashMap.get("STATUS").equals("已确认沟通"))))
      {
        if (tag == 1)
        {
          sb.append("<td colspan=\"2\"><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href=\"#\" onClick='showCxddfkgzxx(").append(hashMap.get("ID")).append(");' title='").append(tzbt).append("'>").append(tzbt).append("</a>").append("</td>");
          sb.append("<td><span style=\"color:black;float:right;font-size:15px;font-weight:300;\">已反馈</span></td><td>");
        }
        else if (tag == 2)
        {
          sb.append("<td colspan=\"2\"><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href=\"#\" onClick='showCustomer(\"").append(khbh).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("\");' title='").append(tzbt).append("'>").append(tzbt).append("</a>");
          //sb.append("\n");
          sb.append("<a href=\"javascript:expWord('").append(khbh).append("',").append(hashMap.get("ID")).append(",'").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" style=\"color:blue;padding-left:40px;\"></a>");
          //&nbsp;(<a href=\"javascript:updateCommunicate('").append(khbh).append("',").append(hashMap.get("ID")).append(")\" style=\"color:blue;\">已确认沟通</a>)
          sb.append("<span style=\"color:black;float:right;font-size:15px;font-weight:300;\">&nbsp;已反馈</span></td><td>");
        }
        else if (tzlx.equals("问卷调查"))
        {
          if ((hashMap.get("JSZT") != null) && (!hashMap.get("JSZT").equals("")) && (hashMap.get("JSZT").toString().equals("已回复")))
          {
            sb.append("<td ><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href = '#' onclick='showWJDC(\"").append(hashMap.get("ID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>").append("</td>");
          }
          else
          {
            sb.append("<td ><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href = '#' style=\"display:block;width:190px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(wjformid).append("\",\"").append(wjdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
            if (!status.equals("未回复"))
            {
              sb.append("<td><span style=\"color:black;padding-left:4px;font-size:15px;font-weight:300;\">" + status + "</span>");
//              if (extend2 != "") {
//                sb.append("&nbsp;(" + extend2 + "已阅)");
//              }
            }
            else
            {
              sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
            }
            //sb.append("\n");
          }
        }
        else
        {
          sb.append("<td ><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href = '#' style=\"display:block;width:190px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
          if (!status.equals("未回复"))
          {
            sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;\">" + status + "</span>");
//            if (extend2 != "") {
//              sb.append("&nbsp;(" + extend2 + "已阅)");
//            }
          }
          else
          {
            sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
          }
         // sb.append("\n");
        }
      }
      else if (tag == 1)
      {
        sb.append("<td colspan=\"2\"><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href=\"javascript:addCustomer('").append(hashMap.get("TZNR")).append("','").append(hashMap.get("ID")).append("','").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>").append("\n");
        sb.append("<span style=\"color:black;float:right;font-size:15px;\">未反馈</span></td><td>");
      }
      else if (tag == 2)
      {
        sb.append("<td colspan=\"2\"><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href=\"javascript:addCustomer('").append(hashMap.get("TZNR")).append("','").append(hashMap.get("ID")).append("','").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\" title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>");
        //sb.append("\n");
        sb.append("<a href=\"javascript:void(0)\" style=\"color:blue;padding-left:40px;\"></a>");
        sb.append("<a style=\"\" href=\"javascript:expWord('',").append(hashMap.get("ID")).append(",'").append(hashMap.get("FSSJ").toString().substring(8, 10)).append("')\"></a><span style=\"color:black;float:right;font-size:15px;font-weight:300;"
        		+ "\">未反馈</span></td><td>");
      }
      else if (tzlx.equals("问卷调查"))
      {
        if ((hashMap.get("JSZT") != null) && (!hashMap.get("JSZT").equals("")) && (hashMap.get("JSZT").toString().equals("已回复")))
        {
          sb.append("<td><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href = '#' onclick='showWJDC(\"").append(hashMap.get("ID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a>").append("</td>");
        }
        else
        {
          sb.append("<td><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href = '#' style=\"display:block;width:190px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(wjformid).append("\",\"").append(wjdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
          if (!status.equals("未回复"))
          {
            sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
//            if (extend2 != "") {
//              sb.append("&nbsp;(" + extend2 + "已阅)");
//            }
          }
          else
          {
            sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
          }
        //  sb.append("\n");
        }
      }
      else
      {
        sb.append("<td ><div class=\"weui_cell_bd weui_cell_primary item_title\">").append("<a href = '#' style=\"display:block;width:190px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;\" onclick='addTalk(\"").append(hfxxbformid).append("\",\"").append(hfxxbdemid).append("\",\"").append(hashMap.get("INSTANCEID")).append("\",\"").append(hashMap.get("ID")).append("\",\"").append(hashMap.get("HFID")).append("\")' title='").append(tzbt).append("'>").append("").append(tzbt).append("</a></td>");
        if (!status.equals("未回复"))
        {//padding-left:100px;
          sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
//          if (extend2 != "") {
//            sb.append("&nbsp;(" + extend2 + "已阅)");
//          }
        }
        else
        { 
        	sb.append("<td><span style=\"color:black;padding-left:4px;float:right;font-size:15px;font-weight:300;\">" + status + "</span>");
        }
       // sb.append("\n");
      }
      //sb.append(" <span style=\"float:right;\">").append(hashMap.get("FSSJ")).append("</span> ").append("\n");
      sb.append("  </div>");
      sb.append("  </li>").append("</td></tr></table>");
    }
    return sb.toString();
  }
  
  public String getQYYQ(int paramInt)
  {
    throw new Error("Unresolved compilation problems: \n\tDuplicate local variable times\n\tDuplicate local variable times\n\tDuplicate local variable times\n");
  }
  
  public String getSCXW(int pageNo)
  {
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    OrgUser us = uc.get_userModel();
    StringBuffer sb = new StringBuffer();
    
    int line = 0;
    int rows = 0;
    if (this.cmsInfoDAO == null) {
      this.cmsInfoDAO = ((CmsInfoDAO)SpringBeanUtil.getBean("cmsInfoDAO"));
    }
    try
    {
      List<Ssxx> Ssxxlist = HelloWebService.getSsxx(pageNo, 10);
      for (Ssxx ssxx : Ssxxlist)
      {
        String href = (String)ssxx.getHref().getValue();
        String times = ((String)ssxx.getTimes().getValue()).substring(0, 16);
        String title = (String)ssxx.getTitle().getValue();
        title = (title.length() >= 20 ? title.substring(0, 20) : title) + "...";
        
        sb.append("<a  id=\"notice_").append("\" class=\"weui_cell\" href='#'>").append("\n");
        sb.append("<img src=\"iwork_img/readed.gif\" alt=\"\">").append("\n");
        sb.append("<i class=\"item_icon\"></i>").append("\n");
        sb.append("<div class=\"weui_cell_bd weui_cell_primary item_title\">").append("\n");
        
        sb.append("<div onClick=openscxw(\"").append(href + "\"").append(") title=\"").append(title).append("\">").append(title).append("</div>").append("\n");
        sb.append("<div class=\"memo\"><span style=\"float:right\">").append("日期：").append(times).append("</span></div>\n");
        sb.append("</div>").append("\n");
        sb.append("</a>").append("\n");
      }
    }
    catch (Exception e)
    {
      logger.error(e,e);
    }
    return sb.toString();
  }
  
  public String getSubStr(String str, int cutCount)
  {
    if (str == null) {
      return "";
    }
    String resultStr = "";
    char[] ch = str.toCharArray();
    int count = ch.length;
    int strBLen = str.getBytes().length;
    int temp = 0;
    for (int i = 0; i < count; i++)
    {
      resultStr = resultStr + ch[i];
      temp = resultStr.getBytes().length;
      if ((temp >= cutCount) && (temp < strBLen))
      {
        resultStr = resultStr + "...";
        break;
      }
    }
    return resultStr;
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
}
