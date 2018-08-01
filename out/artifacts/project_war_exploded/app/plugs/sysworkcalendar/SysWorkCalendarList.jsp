<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="java.text.SimpleDateFormat"%>
<%
SimpleDateFormat  sdf=new  SimpleDateFormat("yyyy-MM-dd");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>系统日历</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"></script>
	<script type="text/javascript" src="iwork_js/pformpage.js"></script>
	<script type="text/javascript" src="iwork_js/json.js"></script>
	<link type="text/css" rel="stylesheet" href="iwork_css/syscalendar/sys_btn.css"/>
	<style type="text/css">
		.td_title1 {
					line-height: 30px;
					font-size: 12px;
					text-align: center;
					letter-spacing: 0.1em;
					padding-right:10px;
					white-space:nowrap;
					border-bottom:1px #999999 thick;
					vertical-align:middle;
					font-family:"宋体";
			}
			/*数据字段内容样式*/
			.td_data1 {
				color:#0000FF;
				line-height: 23px;
				text-align: center;
				padding-left: 3px;
				font-size: 12px;
				font-family:"宋体";
				border-bottom:1px #999999 dotted;
				vertical-align:middle;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
				color:0000FF; 
			}
		.button{
			width:100px;
			font-size:16px;
			font-family:黑体;
			padding:2px;
		}
	</style>
  </head>
  <script type="text/javascript">
		function selectAll(pobj){
		  var obj = document.form.elements;
		  if(pobj.checked==true){
				
			for (var i=0;i<obj.length;i++){
				if (obj[i].name == "id"){
					obj[i].checked = true;
				}
			}
		  }else{
		  	for (var i=0;i<obj.length;i++){
				if (obj[i].name == "id"){
				   obj[i].checked = false;
				}
			}
		  }
		}
		//获取列值
		function openCreate(obj){
			//parent.window.location.reload();
			//var url = "app/plugs/sysworkcalendar/SysWorkCalendarBaseInfo.jsp?isModify=1";
			var url ="iwork_sys_calendar_create.action";
			var target = getNewTarget();
			var page = window.open('form/waiting.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
			
		}
		
		function openEdit(id){	
			//获得数据ID
		    var url = 'iwork_sys_calendar_show.action?id='+id;
			var target = getNewTarget();
			var page = window.open(url,target,'width='+(screen.width-100)+',height=800,top=50,left=50,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
			
		}
	   
	   function refreshTo(){
		var form = document.getElementById('form');
		form.action = "iwork_sys_calendar.action";
		form.submit();
	   }
		
	//刪除操作	
	function del_sure(){
		var chkvalue = "";
 		var chk = document.getElementsByName("id");
 		for (var i = 0; i < chk.length; i++) {
  			if (chk[i].checked) {
   				chkvalue = chkvalue + "," + chk[i].value.toString() + "";
  			}
 		}
 		if(chkvalue!=""){
 			chkvalue = chkvalue.substring(1);
 			chkvalue = chkvalue.toString();
 		}
		var gnl=confirm("确定要删除?刪除后无法恢复!");
		if (gnl==true){
			var option={
				type: "post",
				url: "iwork_sys_calendar_delete.action?check_id="+chkvalue,
				data: "",
				cache:false,
				success: function(msg){
					if(msg=='succ'){
						alert('删除成功');
						window.location.href=window.location.href;
					}else{
						alert('删除失败');
						window.location.href=window.location.href;
					}
				}
			}
		$.ajax(option);
  			return true;
		}else{
  			return false;
		}
	}
	</script>
  <body>
  <form action="iwork_sys_calendar.action" name="form" id="form" method="post">
 	<div  width='95%' align="center">
    <table width='95%' >
    	<tr >
    		<td>
    		<input  type="button" onclick="openCreate(this);return false;" value="创建日历"/>
    		<input  type="button" onclick="javascript:del_sure();"value="删除"/>
    		</td>
    	</tr>
    </table>
    </div>
    <div width='95%' >
	<table width='95%' style='WORD-WRAP: break-word;word-break:break-all' align='center'  cellpadding= '0'   cellspacing= '0' frame="box">
	<tr height = '25'>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='5%' align='center'>
		<input type="checkbox" onclick="javascript:selectAll(this);">
		</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='5%' align='center'>序号</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>日历名称</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='30%' align='center'>适用范围描述</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>日历有效期</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>状态</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>日历类型</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>操作</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center' style="display: none">数据ID</td>
	</tr>
	<s:iterator  value="queryCalendarList" status="calendar">
	<tr height = '25'>
		<td class="td_title1"   width='5%' align='center'>
		<input type="checkbox" name="id" value ="${id }">
		</td>
		<td class="td_data1" ><s:property value='#calendar.count'/></td>
		<td class="td_data1" ><s:property value='calendarName'/></td>
		<td class="td_data1" ><s:property value='describ'/></td>
		<td class="td_data1" ><s:date name='expDateFrom'  format="yyyy-MM-dd"/>至<s:date name='expDateTo'  format="yyyy-MM-dd" /></td>
		<td class="td_data1" ><s:if test="status==1">开启</s:if><s:else>关闭</s:else></td>
		<td class="td_data1" ><s:if test="calendarType==1">普通日历</s:if><s:else>系统默认日历</s:else></td>
		<td class="td_data1" ><a href="#" onclick="openEdit('<s:property value='id'/>');return false;">编辑</a></td>
		<td style="display:none"><s:property value='id'/></td>
	</tr>
	</s:iterator>
</table>
 </div>
</form>
  </body>
</html>
