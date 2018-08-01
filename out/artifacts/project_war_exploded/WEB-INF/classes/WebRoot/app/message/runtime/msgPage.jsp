<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/sys_procdef.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
    <link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="iwork_js/message/sysmsgpage.js"></script>
	<script type="text/javascript" src="iwork_js/message/sysmsglist.js"></script>
	<script type="text/javascript">
	$(function(){
		$('#pp').pagination({  
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
	});
	
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
	}
	function filterStatus(status){
		if(status==-1){
			$("#status").val("");
			$("#type").val("");
		}else{
			$("#status").val(status);
			
		}
		submitMsg(0,10);
	}
	function filterType(type){
		$("#type").val(type);
		submitMsg(0,10);
	}
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no;height:33px;border-bottom:1px solid #efefef;">
			<div class="report_top"> 
			  <ul class="sub_tab">
			  <s:if test="status=='' || status==null">
			    <li><a href="javascript:filterStatus(-1)" class="sub_tab_selected" style="color:#000">全部</a></li>
			  </s:if>
			  <s:else>
			  	 <li><a href="javascript:filterStatus(-1)" >全部</a></li> 
			  </s:else>
			  
			    <li>|</li>
			    <s:if test="status!='' && status==0">
			    	<li><a href="javascript:filterStatus(0)" class="sub_tab_selected"  style="color:#000" >未读</a></li>
			    </s:if>
			    <s:else> 
			     	<li><a href="javascript:filterStatus(0)" >未读</a></li>
			    </s:else>
			  </ul>
			  <ul class="sub_tab"  style="float:right">
			    <li><a href="javascript:setAllRead()" >全部标记已阅</a></li>
			    <li>|</li>
			    <li><a href="javascript:setAllDel()" >删除所有消息</a></li>
			  </ul>
			</div>
	</div>
<div region="center" style="border:0px;padding-top:5px;" id="messagelistDiv" >
<s:iterator value="list" >
<div class="news_center" id="div_item_<s:property value="id"/>">
  <div class="news_center_title">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td style="width:85px;">
	        <s:if test="type==0">
	       		  <a href="#" style="color:#0382cf">【系统消息】</a>
	        </s:if>
	        <s:elseif test="type==1">
	        	  <a href="#" style="color:#0382cf">【生日提醒】</a>
	        </s:elseif>
	        <s:elseif test="type==2">
	        	  <a href="#" style="color:#0382cf">【流程通知】</a>
	        </s:elseif>
	        <s:elseif test="type==3">
	        	  <a href="#" style="color:#0382cf">【流程通知】</a>
	        </s:elseif>
	        <s:else> 
	          <a href="#" style="color:#0382cf">【系统消息】</a>
	        </s:else>
        </td>
        <td><a href="#" style="color:#6a6868"><strong><s:property value="title"/></strong></a></td>
        <td class="msgDate"> <s:property value="sendDate"/></td>
        <td><img align='top' style="cursor:hand" alt="删除消息" src='iwork_img/sysmsg/msg_close.gif' onClick="deleteit(<s:property value="id"/>)"></td>
      </tr>
    </table>
  </div>
  <div class="news_center_main">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td style="width:80px;text-align:center">
         <s:if test="status==0">
        		<img align="middle" id="icon_<s:property value="id"/>"  src="iwork_img/sysmsg/sys_msg_icon_noread.png" />
        	</s:if>
        	<s:else>
        	<img align="middle"  src="iwork_img/sysmsg/sys_msg_icon_read.png" />
        	</s:else>
        </td>
        <td class="msgContent">
        	<s:property value="content" escapeHtml="false"/>
        </td>
      </tr>
      <tr>
      	<td colspan="2" class="msgLink"> 
      	<s:if test="status==0">
      		<a id="readBtn_<s:property value="id"/>" href="###" onclick="readit(<s:property value="id"/>);"><img alt="" src="iwork_img/ok.gif">标记已阅</a>
      	</s:if>
      	&nbsp;
      	&nbsp;
      	<s:if test="url!=null&&url!=''">
      		<a href="<s:property value="url"/>" target='_blank'><img border="0" src="iwork_img/link.png"/>相关链接</a>
      	</s:if>
      	</td>
      </tr>
    </table>
  </div>
</div>
</s:iterator>
<div style = "padding:5px">
<s:if test="totalNum==0">
	暂无系统消息
</s:if><s:else>
<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
</s:else>
</div>
			<form action="sysmsg_index.action" method=post name=frmMain id=frmMain>
			<s:hidden name="status" id="status"></s:hidden>
			<s:hidden name="type" id="type"></s:hidden> 
			<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
		</form>	
	</div>
</body>
</html>
