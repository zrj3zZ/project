<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>短信平台-我的号码簿</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script language="javascript" src="iwork_js/commons.js"></script> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"  > </script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
			mainFormValidator =  $("#editForm").validate({
			 });
			 mainFormValidator.resetForm();
		});
	$(function(){
		$('#pp').pagination({  
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
		$(document).bind('keydown', function(event) {
			if (event.keyCode == "13") {
				//禁用回车按下事件
				return false;
			}
		});
		$(document).bind('keyup', function(event) {
			if (event.keyCode == "13") {
				//回车执行查询
				doSearch();
			}
		});
	});
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	function doSearch(){
		var valid = mainFormValidator.form(); //执行校验操作
		if(!valid){
			return false;
		}
		var pageUrl = "phoneBook_index_address.action";
		$("#editForm").attr("action",pageUrl); 
		$("#editForm").submit();
	}
	function expExcel(){
		   //导入excel
			var pageUrl = "phoneBook_index_exp.action";
			$("#editForm").attr("action",pageUrl); 
			$("#editForm").submit();
		}
	</script>
<style type="text/css">
		 body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
		.memoTitle{
			font-size:14px;
			padding:5px;
			color:#666;
		}
		.memoTitle a{
			font-size:12px;
			padding:5px;
		}
		.TD_TITLE{
			padding:5px;
			width:200px;
			background-color:#efefef;
			text-align:right;
			
		}
		.TD_DATA{
			padding:5px;
			padding-left:15px;
			padding-right:30px;
			background-color:#fff;
			width:500px;
			text-align:left;
			border-bottom:1px solid #efefef;
		}
		.header td{
			height:35px;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
			border-top:1px dotted #ccc;
			border-right:1px solid #eee;
		} 
		.cell:hover{
			background-color:#F0F0F0;
		}
		.cell td{
					margin:0;
					font-size:12px;
					padding:3px 4px;
					white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;
					text-decoration:none;
					text-align:left;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
				}
		.selectCheck{
			border:0px;
			text-align:right;
		}
		.searchtitle{
			text-align:right;
			padding:5px;
		}
	</style>
<script type="text/javascript" src="iwork_js/plugs/qnumload.js"></script>

</head>
<body class="easyui-layout">
<div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
<form id="editForm" method="post" action="/phoneBook_index_address.action" name="editForm">
<div style="float: none;padding-left: 5px;"><a href="phoneBook_index.action">号码簿</a>|通讯录</div>
<div style="padding:5px;clear: both;">
<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
<table width="100%" cellspacing="0" cellpadding="0" border="0">
<tbody>
<tr>
<td style="padding-top:10px;padding-bottom:10px;">
<table width="100%" cellspacing="0" cellpadding="0" border="0">
<tbody>
<tr>
<td class="searchtitle">股票代码</td>
<td class="searchdata"><input id="zqdm" type="text" value="<s:property value="zqdm"/>" class='{string:true}' name="zqdm"></td>
<td class="searchtitle">公司名称</td>
<td class="searchdata"><input id="gsmc" type="text" value="<s:property value="gsmc"/>" class='{string:true}' name="gsmc"></td>
<td class="searchtitle">类别</td>
<td class="searchdata">
<s:select list="{'客户联系人','项目负责人','持续督导专员','专职督导人员'}" theme="simple" headerKey="" headerValue="-空-" name="leibie" id="ss"></s:select>
</td>
</tr>
</tbody>
</table>
</td>
<td> </td>
<td valign="bottom" style="padding-bottom:5px;">
<a class="easyui-linkbutton l-btn" icon="icon-search" onclick="doSearch()" name="searchBtn">查询</a>
<a href="javascript:expExcel();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a>
</td>
</tr>
<tr> </tr>
</tbody>
</table>
</div>
</div>
</form>
<table width="80%" style="border:1px solid #efefef" cellpadding="0" cellspacing="0"> 
      		<tr class="header">
      			<td>挂牌公司</td>
      		    <td>类别</td>
      		    <td>姓名</td>
      		    <td>电话</td>
      		    <td>邮箱</td>
      		    <td>董秘姓名</td>
      		    <td>董秘电话</td>
      		    <td>董秘邮箱</td>
      		</tr>
      		<!-- CUSTOMERNAME,USERNAME,MOBILE,EMAIL,ROLENAME,ZQDM -->
      		<s:iterator value="list" status="ll">
	      		<tr class="cell">
		      		<s:if test="#ll.index-1<0||CUSTOMERNAME!=list[#ll.index-1].CUSTOMERNAME">
			      		<td rowspan="<s:property value="COUNT"/>" style="width:400px">
		    					<s:property value="ZQDM"/><br>
		    					<s:property value="CUSTOMERNAME"/>
		    			</td>
		      		</s:if>
	   				<td>
	   					<s:property value="ROLENAME"/>
	   				</td>
	   				<td>
	   					<s:property value="USERNAME"/>
	   				</td>
	   				<td>
	   					<s:property value="MOBILE"/>
	   				</td>
	   				<td>
	   					<s:property value="EMAIL"/>
	   				</td>
	   				<s:if test="#ll.index-1<0||CUSTOMERNAME!=list[#ll.index-1].CUSTOMERNAME">
			      		<td rowspan="<s:property value="COUNT"/>">
		    					<s:property value="EXTEND1"/>
		    			</td>
			      		<td rowspan="<s:property value="COUNT"/>">
		    					<s:property value="EXTEND2"/>
		    			</td>
		    			<td rowspan="<s:property value="COUNT"/>">
		    					<s:property value="EXTEND3"/>
		    			</td>
		      		</s:if>
      			</tr>
      		</s:iterator>
      		</table>
			<form action="phoneBook_index_address.action" method=post name=frmMain id=frmMain >
			<s:hidden name="leibie" id="leibie"></s:hidden>
			<s:hidden name="zqdm" id="zqdm"></s:hidden>
			<s:hidden name="gsmc" id="gsmc"></s:hidden>
			<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
		</form>	
  </div>
</form>	
	<div  region="south"  style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-left:10px;" border="false"  >
		<div style = "padding:2px">
<s:if test="totalNum==0">

</s:if><s:else>
<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
</s:else>
</div>
</div>
</body>
</html>
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