<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>项目相关问题回复</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css" />
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>

<style type="text/css">
.memoTitle {
	font-size: 14px;
	padding: 5px;
	color: #666;
}

.memoTitle a {
	font-size: 12px;
	padding: 5px;
}

.TD_TITLE {
	padding: 5px;
	width: 200px;
	background-color: #efefef;
	text-align: right;
}

.TD_DATA {
	padding: 5px;
	padding-left: 15px;
	padding-right: 30px;
	background-color: #fff;
	width: 500px;
	text-align: left;
	border-bottom: 1px solid #efefef;
}

.header td {
	height: 30px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}

.cell:hover {
	background-color: #F0F0F0;
}

.cell td {
	margin: 0;
	padding: 3px 4px;
	white-space: nowrap;
	word-wrap: normal;
	overflow: hidden;
	text-align: left;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #ccc;
}

.selectCheck {
	border: 0px;
	text-align: right;
}
</style>
	<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#frmMain").validate({});
		mainFormValidator.resetForm();
	});

		$(function() {
			$('#pp').pagination({
				total : <s:property value="totalNum"/>,
				pageNumber : <s:property value="pageNumber"/>,
				pageSize : <s:property value="pageSize"/>,
				onSelectPage : function(pageNumber, pageSize) {
					submitMsg(pageNumber, pageSize);
				}
			});
			//查询事件
			$("#search").click(
					function() {
						var projectName = $("#projectName").val();
						var question = $("#question").val();
						var seachUrl = encodeURI("xsd_zqb_project_xgwthf.action?projectName="
								+ projectName + "&question=" + question);
						window.location.href = seachUrl;
					});
		});
		function submitMsg(pageNumber, pageSize) {
			$("#pageNumber").val(pageNumber);
			$("#pageSize").val(pageSize);
			$("#frmMain").submit();
			return;
		}
		function showQuestion(title, taskid, questionid,xmbh) {
			var pageUrl = "xsd_zqb_project_question.action?projectNo="
					+ xmbh + "&taskid=" + taskid + "&questionId="
					+ questionid;
			art.dialog.open(pageUrl,{
				id : 'projectTask',
				cover : true,
				title : "[问题反馈]" + title,
				loadingText : '正在加载中,请稍后...',
				bgcolor : '#999',
				rang : true,
				width : 750,
				cache : false,
				lock : true,
				height : 650,
				iconTitle : false,
				extendDrag : true,
				autoSize : false
			});
		}
		
		function showXm(xmbh,instanceid,xmmc) {
			var pageUrl = "loadVisitPage.action?instanceId="+instanceid+"&formid=91&demId=22&PROJECTNO="+xmbh+"&PROJECTNAME="+xmmc;
			art.dialog.open(pageUrl,{
				id : 'projectTask',
				cover : true,
				title : "查看项目",
				loadingText : '正在加载中,请稍后...',
				bgcolor : '#999',
				rang : true,
				width : 900,
				cache : false,
				lock : true,
				height : 700,
				iconTitle : false,
				extendDrag : true,
				autoSize : false
			});
		}
	</script>
</head>
<body class="easyui-layout">

	<div region="center" border="false" align="center"
		style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
		<form action="xsd_zqb_project_xgwthf.action" method=post name="frmMain"
			id="frmMain">
			<div style="padding:5px">
				<div
					style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle" style="text-align:right;">项目名称</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:128,required:false,string:true}' style="width:100px"
											name='projectName' id='projectName' value=''></td>
										<td id="title_wtgjz" class="searchtitle" width="400">问题关键字
											<input type='text' class='{maxlength:128,required:false,string:true}'
											style="width:100px" name='question' id='question' value=''>
										</td>

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
 <s:hidden name="pageNumber" id="pageNumber"></s:hidden>
					<s:hidden name="pageSize" id="pageSize"></s:hidden>
					<s:hidden name="totalNum" id="totalNum"></s:hidden>
		</form>

		<table WIDTH="100%" style="border:1px solid #efefef">
			<TR class="header">
				<TD style="width:150px">项目名称</TD>
				<TD style="width:100px">阶段名称</TD>
				<TD style="width:60px">提问人</TD>
				<TD style="width:120px">问题</TD>
				<TD style="width:100px">操作</TD>
			</TR>
			<s:iterator value="list" status="status">
				<TR class="cell">
					<TD><s:property value="XMMC" /></a></TD>
					<TD><s:property value="TASKNAME" /></TD>
					<TD><s:property value="USERNAME" /></TD>
					<TD><s:property value="QUESTION" /></TD>
					<TD><a
						href="javascript:showQuestion('<s:property value='TASKNAME'/>','<s:property value='TASKNO'/>','<s:property value='ID'/>',
						'<s:property value='XMBH'/>')">回复</a>|<a
						href="javascript:showXm('<s:property value='XMBH'/>','<s:property value='XMID'/>','<s:property value='XMMC'/>')">查看项目信息</a></TD>

				</TR>
			</s:iterator>
		</table>
		<div>
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		
	</div>

	</div>
	

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