<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.iwork.core.organization.context.UserContext" %>
<%@ page import="com.iwork.core.organization.tools.UserContextUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
	String userId = "";
	UserContext me =  UserContextUtil.getInstance().getCurrentUserContext();
	userId = me.get_userModel().getUserid();
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    
    <title>My JSP 'sysletterslist.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.js"></script>
	<link type="text/css" rel="stylesheet" href="iwork_css/syscalendar/sys_btn.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
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
			/*数据字段内容样式*/
			.td_data {
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
				font-weight:bold;
			}
		.button{
			width:100px;
			font-size:16px;
			font-family:黑体;
			padding:2px;
		}
		a:link { 
			font-size: 12px; 
			color: #0000FF; 
			text-decoration: none; 
		} 
		a:visited { 
			font-size: 12px; 
			color: #0000FF; 
			text-decoration: none; 
		} 
		a:hover { 
			font-size: 12px; 
			color: #0000FF; 
			text-decoration: underline; 
		} 
	</style>
	<script type="text/javascript">
		
		function subForm(){
			var form = document.getElementById('form');
			form.src='iwork_sys_letter_list.action';
			form.submit();
		}
		//打开发送站内信界面
		function openCreate(obj){
			var url ="iwork_sys_letter_open_sent.action";
			var target = getNewTarget();
			var page = window.open('form/waiting.html',target,'width='+screen.width-50+',height=800,top=150,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
			
		}
		
		//更改已读未读状态
		function changeCheck(obj,objThis){
			var form = document.getElementById('form');
			var objTr = objThis.parentNode.parentNode;
			var obj1 = objTr.getElementsByTagName("td");
			//获得数据ID
			var letterId = obj1[8].innerText;
			var id = obj1[9].innerText;
			var receiveUserId = obj1[10].innerText;
			var url = 'iwork_sys_letter_change_flag.action?letterId='+letterId+'&id='+id+'&receiveUserId='+receiveUserId+'&flag='+obj;
			
				var params = $('#form').serialize();
		 		var option={
					type: "post",
					url: url,
					data: params,
					cache:false,	
					success: function(msg){
						if(msg=="succ"){
							alert('标记成功');
							window.location.reload();
						}else{
							
							alert('标记失败');
							return false;
						}
					}
			 }
			$.ajax(option);
		}
		
		//查看站内信
		function lookFor(objThis){
			var objTr = objThis.parentNode.parentNode;
			var obj1 = objTr.getElementsByTagName("td");
			//获得数据ID
			var flag = $(obj1[11]).text();
			var letterId = $(obj1[8]).text();
			//var letterId = obj1[8].textContent;
			var id = $(obj1[9]).text();
			var receiveUserId = $(obj1[10]).text();
			//window.location.href='iwork_sys_letter_look_for.action?letterId='+letterId+'&id='+id+'&receiveUserId='+receiveUserId+'&flag='+flag;
			//url_0用于多人发送,暂时取消
			//var url_0 = 'iwork_sys_letter_look_for.action?letterId='+letterId+'&id='+id+'&receiveUserId='+receiveUserId+'&flag='+flag;
			var url = "iwork_sys_letter_look_for_reply_page.action?letterId="+letterId+"&flag="+flag;
			var target = getNewTarget();
			var page = window.open('form/waiting.html',target,'width='+(screen.width-100)+',height=600,top=50,left=50,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
		}
		//收取站内信
		function refresh(){
			window.location.reload();
		}
		//删除站内信
		function delLetter(){
			var checks = document.getElementsByName('letterId');
			var letterIds = "";
			if(checks.length>0){
				for(var i=0;i<checks.length;i++){
					if(checks[i].checked){
						letterIds = letterIds + checks[i].value + ",";
					}
				}
				letterIds = letterIds.substring(0,letterIds.lastIndexOf(','));
				if(letterIds == ''){
					alert('请选择要删除的站内信!');
					return false;
				}else{
					var url = 'iwork_sys_letter_delete.action?letterIds='+letterIds;
		 			var option={
						type: "post",
						url: url,
						data: "",
						cache:false,	
						success: function(msg){
							if(msg=="succ"){
								alert('删除成功');
								window.location.reload();
							}else{
								alert('删除失败');
								return false;
							}
						}
			 		}
					$.ajax(option);
				}
			}else{
				alert('请选择要删除的数据');
				return false;
			}
				
		}
		
		function selectAll(){
			var all = document.getElementById('all');
			var letterIds = document.getElementsByName('letterId');
			if(all.checked){
				for(var i=0;i<letterIds.length;i++){
					letterIds[i].checked = true;
				}
			}else{
				for(var j=0;j<letterIds.length;j++){
					letterIds[j].checked = false;
				}
			}
		}
		
		//标记为已读或未读状态
		function haveRead(flag){
			var letterIds = document.getElementsByName('letterId');
			var letterId = "";
			var receiveUserId = "";
			for(var i=0;i<letterIds.length;i++){
				if(letterIds[i].checked){
					letterId = letterId + letterIds[i].value+',';
				}
			}
			letterId = letterId.substring(0,letterId.lastIndexOf(','));
			if(letterId==''){
				alert('请选择要标记的站内信!');
				return false;
			}else{
				var url = 'iwork_sys_letter_change_flag.action?letterId='+letterId+'&flag='+flag;
				var params = $('#form').serialize();
		 		var option={
					type: "post",
					url: url,
					data: params,
					cache:false,	
					success: function(msg){
						if(msg=="succ"){
							alert('标记成功');
							window.location.reload();
						}else{
							alert('标记失败');
							return false;
						}
					}
			 }
			$.ajax(option);
		   }
		}
		
	</script>
  </head>
  
  <body> 
    <s:form action="iwork_sys_letter_list.action" id="form" name="form">
    	<table>
    		<tr>
    			<td><input class="btn1" type="button" onclick="javascript:openCreate();" value="发送站内信"/></td>
    			<td><input class="btn1" type="button" onclick="javascript:refresh();" value="收取站内信"/></td>
    			<td><input class="btn1" type="button" onclick="javascript:delLetter();" value="删除站内信"/></td>
    			<td><input class="btn1" type="button" onclick="javascript:haveRead('1');" value="标记为已读"/></td>
    			<td><input class="btn1" type="button" onclick="javascript:haveRead('0');" value="标记为未读"/></td>
    		</tr>
    	</table>
    	<table  width='90%'>
  			<tr>
    			<td background='iwork_img/rszz/report-bg-blue2.gif' height="25" colspan="10">站内信列表</td>
  			</tr>
 			<tr>
    			<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="4%" ><div align="center"><input type="checkbox" id="all" onclick="selectAll();"/></div></td>
   				<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="4%" align="center">标识</td>
   				<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="8%" align="center">原始发信人</td>
   				<!--  <td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="8%" align="center">发信人</td>-->
    			<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="9%" align="center">收信人</td>
    			<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="30%" align="center">标题</td>
    			<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="7%" align="center">已读/未读</td>
    			<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="14%" align="center">最后回复时间</td>
    			<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width="18%" align="center" style="display: none">编辑</td>
    			<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' style="display: none">little_id</td>
    			<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' style="display: none">id</td>
  			</tr>
  			<s:iterator value="queryLettersList" status="letters">
  			<tr>
  			    <!-- 注意更改此td下的顺序或增加td内容时,要更改lookFor()和changeCheck()的js方法,由于是按照文本位置来获取当前行的值 -->
  				<td class="td_data1" width="37"  align="center"><div align="center"><input name="letterId" type="checkbox" value="<s:property value="letterId"/>"/></div></td>
   				<td class="td_data1" width="25" align="center"><s:if test="checkStatus==1"><img id="bflag" src="iwork_img/sysletter/mail_have_readed.png"/></s:if><s:else><img id="bflag" src="iwork_img/sysletter/mail_no_readed.png"/></s:else></td>
   				<td class="td_data1" width="99" align="center"><s:property value="createUserName"/></td>
   				<!-- <td class="td_data1" width="99" align="center"><s:property value="sentUserName"/></td> -->
    			<td class="td_data1" width="82" align="center"><s:property value="receiveUserName"/></td>
    			<s:if test="checkStatus==1">
    				<td class="td_data1" width="221" align="center"><a href="####" onclick="javascript:lookFor(this);"><s:property value="sysletterscontent.letterTitle"/></a></td>
    			</s:if>
    			<s:else>
    				<td class="td_data" width="221" align="center"><a href="####" onclick="javascript:lookFor(this);"><s:property value="sysletterscontent.letterTitle"/></a></td>
    			</s:else>
    			<td class="td_data1" width="80" align="center"><s:if test="checkStatus==1">已读</s:if><s:else>未读</s:else></td>
    			<td class="td_data1" width="120" align="center"><s:property value="ts"/></td>
    			<td class="td_data1" width="150" style="display: none">
    			<input class="btn4" type="button" onclick="javascript:lookFor(this);" value="查看" />&nbsp;
    			<s:if test="checkStatus==1"><input class="btn1" type="button" onclick="javascript:changeCheck(<s:property value='checkStatus'/>,this);" value="标记未读"/></s:if>
    			<s:else><input class="btn1" type="button" onclick="javascript:changeCheck(<s:property value='checkStatus'/>,this);" value="标记已读"/></s:else>
    			</td>
    			<td class="td_data1" style="display: none"><s:property value="letterId"/></td>
    			<td class="td_data1" style="display: none"><s:property value="id"/></td>
    			<td class="td_data1" style="display: none"><s:property value='receiveUserId'/>
    			<input type="text" id="bflag_value" name="bflag_value" value="<s:property value='checkStatus'/>"/>
    			</td>
    			<td style="display: none"><s:property value='checkStatus'/></td>
  			</tr>
  			</s:iterator>
</table>
    </s:form>
  </body>
</html>
