<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.iwork.core.organization.context.UserContext"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>The Calendar API Detail Page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<style type="text/css">
        html,body
        {
            height: 100%;
            margin: 0px;
        }
        .left
        {
            float: left;
            height: 100%;
            width: 20%;
        }
        .right
        {
            float: right;
            height: 100%;
            width: 80%;
        }   

		#function{
  			color:#698B22;  
		}
		#notice{
  			color:red;  
		}
</style>     
    </style>
	<script type="text/javascript">
</script>

  </head>
<form action="" method=post name=frmMain >
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="3">
    	<table width="100%" height="39" border="0" align="center" cellpadding="0" cellspacing="0">
      	  <tr>
        	<td width="51%" align="right" valign="top" style="margin:10px;">	
        		<fieldset style="padding:8px;width:90%;border:1px solid #ACBCC9; margin-left:10px; margin-right:10px;line-height:2.0; text-align:left;" >
        			<legend style="vertical-align:middle; text-align:left">
        				<img src="iwork_img/rszz/online.gif" width="16" height="16" border="0">API使用介绍
        			</legend>
        			<table width="734" height="459" border="0">
  						<tr>
    						<td width="167" height="39" align="center">API名称</td>
    						<td width="557" align="center">使用介绍及方法</td>
  						</tr>
  						<tr>
   							<td class ="td_title"><p >判断指定用户的指定日期是否为工作日</p></td>
    						<td class ="td_data"><span id="function">SysCalendarAPI.getInstance().isWorkDay(String userId,Date date),</span>参数分别是用户的ID和指定的日期。return:返回类型 int,返回 1为工作日,0为非工作日，当返回<span id="notice">-111111111</span>,则为错误代码,可能造成的原因是日历已经过期,或者日历已经关闭。例：SysCalendarAPI.getInstance().isWorkDay(&quot;ZHANGSAN&quot;,new Date());Date格式为:yyyy-MM-dd</td>
  						</tr>
  						<tr>
    						<td class ="td_title"><p >判断两个时间区间的工作天数</p></td>
    						<td class ="td_data"><span id="function">SysCalendarAPI.getInstance().betweenStartAndEndDays(Date startDate,Date endDate,String userId)</span>,参数分别代表开始日期、结束日期、人员ID。return:返回类型 int,若返回<span id="notice">-111111111</span>则为错误代码,没有查到系统日历,原因为日历已经过期,或者日历已经关闭。Date格式为:yyyy-MM-dd</td>
  						</tr>
 						<tr>
    						<td height="56" class ="td_title"><p >根据起始日期，及工作日天数获取结束日期</p></td>
    						<td class ="td_data"><span id="function">SysCalendarAPI.getInstance().getEndDateByStartDayAndDays(Date startDate,int days,String userId)</span>,参数分别代表开始日期、工作天数、人员ID。return:返回类型Date，若返回<span id="notice">null</span>,则为错误代码,没有查到系统日历,原因为日历已经过期,或者日历已经关闭。Date格式为:yyyy-MM-dd</td>
  						</tr>
  						<tr>
   						    <td height="73" class ="td_title"><p >根据结束日期及工作日天数获取开始日期</p></td>
    						<td class ="td_data"><span id="function">SysCalendarAPI.getInstance().getStartDateByEndDayAndDays(Date endDate,int days,String userId)</span>,参数分别代表结束日期、工作天数、人员ID。return:返回类型Date,若若返回<span id="notice">null</span>,则为错误代码,没有查到系统日历,原因为日历已经过期,或者日历已经关闭。Date格式为:yyyy-MM-dd</td>
 						</tr>
  						<tr>
    						<td height="57" class ="td_title"><p >判断当前时间是否为指定用户的工作时间</p></td>
    						<td class ="td_data"><span id="function">SysCalendarAPI.getInstance().isWorkTime(String userId)</span>,参数代表人员ID。return:返回类型 int,返回0不包含在内,返回1包含在内,返回<span id="notice">-111111111</span>表示没有找到日历,日历被关闭,或是过期。</td>
  						</tr>
  						<tr>
    						<td height="52" class ="td_title"><p >判断指定时间是否为指定用户的工作时间</p></td>
    						<td class ="td_data"><span id="function">SysCalendarAPI.getInstance().isWorkTime(String userId,String _time)</span>,参数分别代表人员ID、带有时间的日期。return: 返回类型int,返回0不包含在内,返回1包含在内,返回<span id="notice">-111111111</span>为错误代码,表示没有找到日历,日历被关闭,或是过期._time格式为：yyyy-MM-dd HH:mm:ss </td>
  						</tr>
					</table>
	     		</fieldset>
	   		</td>
   	 	</tr>
    	
     </table>
 	</td>
   </tr>
  </table>  
<!--</div>

			
--></form>
  
</html>
