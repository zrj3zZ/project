<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>系统消息</title>
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
	 <link rel="stylesheet" href="mobile/assets/lib/weui.min.css">
    <link rel="stylesheet" href="mobile/assets/css/jquery-weui.css">
    <link rel="stylesheet" href="mobile/assets/demos/css/demos.css">
	<script src="mobile/assets/jweixin-1.1.0.js"></script>
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

<body>
<div data-role="page" class="type-interior"> 
	<div data-role="header" class="ui-body-d ui-body"　style="overflow:hidden;" data-position="fixed">
		<div class="weui_navbar">
        <div class="weui_navbar_item weui_bar_item_on">
          全部
        </div>
        <div class="weui_navbar_item">
          未读
        </div>
        <div class="weui_navbar_item">
          已读
        </div>
      </div>
	<div data-role="content"> 
	<div class="weui_cells weui_cells_access">
		<s:iterator value="list" >
        <a class="weui_cell" href="<s:property value="url"/>">
          <div class="weui_cell_hd"><img src="iwork_img/sysmsg/sys_msg_icon_noread.png" alt="" width="20"></div>
          <div class="weui_cell_bd weui_cell_primary">
          	
          </div>
          <div class="weui_cell_ft"><s:property value="title"/></div>
        </a>
        
</s:iterator>
</div>
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
	</div><!-- /footer -->
	<div data-role="footer"  data-position="fixed" style="padding-left:10px;" >
		   <s:property value="transButton" escapeHtml="false"/>
	    </div><!-- /navbar -->  
	</div><!-- /content --> 
</body>
</html>
