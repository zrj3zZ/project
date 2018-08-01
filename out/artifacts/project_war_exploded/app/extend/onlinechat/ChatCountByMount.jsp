<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>通话记录统计</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css"href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css"href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css"href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css"href="iwork_css/engine/iformpage.css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript"src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript"src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript"src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript"src="iwork_js/jqueryjs/jquery.jqGrid.min.js"> </script>
<script type="text/javascript"src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"> </script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css"href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css"href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css"href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript"src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<script type="text/javascript">
$(function(){
    //查询
    $("#search").click(function(){
      var startime = $("#STARTIME").val();
      var endtime = $("#ENDTIME").val();
	  var seachUrl = encodeURI("zqb_onlinechat_count.action?startime="+startime+"&endtime="+endtime);
      window.location.href = seachUrl;  
    });
  });
</script>
<style type="text/css">
.searchtitle {
	text-align: right;
	padding: 5px;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: 28px;
	font-size: 12px;
	vertical-align: text-middle;
	padding-top: 2px;
}

.header td {
	height: 35px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}

.cell td {
	margin: 0;
	padding: 3px 4px;
	height: 25px;
	font-size: 12px;
	white-space: nowrap;
	word-wrap: normal;
	overflow: hidden;
	text-align: left;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #eee;
}

.cell:hover {
	background-color: #F0F0F0;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false"></div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
	<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px">
				<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
					<table width='50%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='50%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">沟通时间</td>
										<td class="searchdata">
										<input type='text' onfocus="WdatePicker();" class='{maxlength:64,required:false}' style="width:80px;" name='STARTIME' id='STARTIME' value=''>
										到
										<input type='text' onfocus="WdatePicker();" class='{maxlength:64,required:false}' style="width:80px;" name='ENDTIME' id='ENDTIME' value=''></td>
									</tr>
									<tr>
										
									</tr>
								</table>
							<td>
							<td valign='bottom' style='padding-bottom:5px;'><a
								id="search" class="easyui-linkbutton" icon="icon-search"
								href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>
			</div>
			<span style="disabled:none"> <input type="hidden"
				name="formid" value="88" id="formid" /> <input type="hidden"
				name="demId" value="21" id="demId" /> <input type="hidden"
				name="idlist" id="idlist" value='11'>

			</span>
		</form>
		<div style="padding:5px">


			<table id='iform_grid' width="50%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:25%;">持续督导专员</td>
					<td style="width:25%;">月份</td>
					<td style="width:25%;">挂牌公司沟通数量</td>
				</tr>
				<s:iterator value="countChatList" status="status">
					<tr class="cell">
						<td><s:property value="USERNAME" /></td>
						<td><s:property value="MONTH" /></td>
						<td><s:property value="SENDNUM" /></td>
					</tr>
				</s:iterator>
			</table>
			<form action="zqb_onlinechat_count.action" method=post name=frmMain id=frmMain>
				<s:hidden name="startime" id="startime"></s:hidden>
				<s:hidden name="endtime" id="endtime"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<%-- <div region="south"
		style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
		border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div> --%>
	<script type="text/javascript">
	$(function(){
		$("#STARTIME").val($("#startime").val());
		$("#ENDTIME").val($("#endtime").val());
	});
	</script>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>