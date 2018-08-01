<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"> </script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"> </script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"></script>
<script type="text/javascript">
	function addProcessFormPage(actDefId,xmbh,jdKey,jdbh,jdmc){
		var insId=$("#INSTANCEID", window.parent.document).val();
		if(insId!=''){
			var result=checkXmjd(xmbh,jdKey);
			var instanceId = result.instanceId;
			var url = "";
			if(instanceId==0){
	   			url = 'processRuntimeStartInstance.action?actDefId='+actDefId+'&XMBH='+encodeURI(xmbh)+'&JDBH='+encodeURI(jdbh)+'&JDMC='+encodeURI(jdmc);
			}else{
				var taskid = result.taskid;
				url="loadProcessFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+instanceId+"&taskId="+taskid+"&JDMC="+encodeURI(jdmc);
			}
	   		var target = "_blank";
	   		var win_width = window.screen.width;
	   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	   		page.location = url;
		}else{
			alert("请先保存项目信息。");
		}
   		return;
	}
	
	function editProcessFormPage(lcbh,lcbs,taskid,jdmc) {
		var url="loadProcessFormPage.action?actDefId="+lcbh+"&instanceId="+lcbs+"&excutionId="+lcbs+"&taskId="+taskid+"&JDMC="+encodeURI(jdmc);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function checkXmjd(xmbh,jdmc){
		var result=null;
		$.ajaxSetup({
	        async: false
	    });
		$.ajax({
	        url : 'sx_zqb_CwCheckXMJD.action',
	        async : false,
	        type : "POST",
	        data: {
	        	xmbh:xmbh,
		    	jdmc:jdmc
            },
	        dataType : "json",
	        success : function(data) {
	        	result=data;
	        }  
	    });
	    return result;
	}
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
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
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
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top;">
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:20%;">阶段名称</td>
					<td style="width:10%;">呈报人</td>
					<td style="width:40%;">资料内容</td>
					<td style="width:10%;">状态</td>
					<td style="width:5%;">操作</td>
				</tr>
				<s:iterator value="taskList">
					<tr class="cell">
						<td style="width:20%;"><s:property value="JDMC" /></td>
						<td style="width:10%;"><s:property value="LXTBR" /></td>
						<td style="width:40%;"><s:property value="LXFJ" escapeHtml="false"/></td>
						<td style="width:10%;"><s:property value="SPZT" /></td>
						<s:if test="JDMC=='项目立项'">
							<s:if test="LCBS==0">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addProcessFormPage('<s:property value='ybcwXmlxServer'/>','<s:property value='XMBH'/>','CWXMLX',<s:property value='ID'/>,'<s:property value='JDMC'/>')">呈报</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editProcessFormPage('<s:property value='LCBH'/>',<s:property value='LCBS'/>,<s:property value='TASKID'/>,'<s:property value='JDMC'/>')">查看</a></td>
							</s:else>
						</s:if>
						<s:elseif test="JDMC=='工作进度汇报'">
							<s:if test="LCBS==0">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addProcessFormPage('<s:property value='ybcwGzjdhbServer'/>','<s:property value='XMBH'/>','CWGZJDHB',<s:property value='ID'/>,'<s:property value='JDMC'/>')">呈报</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editProcessFormPage('<s:property value='LCBH'/>',<s:property value='LCBS'/>,<s:property value='TASKID'/>,'<s:property value='JDMC'/>')">查看</a></td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='资料归档'">
							<s:if test="LCBS==0">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addProcessFormPage('<s:property value='ybcwZlgdServer'/>','<s:property value='XMBH'/>','CWZLGD',<s:property value='ID'/>,'<s:property value='JDMC'/>')">呈报</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editProcessFormPage('<s:property value='LCBH'/>',<s:property value='LCBS'/>,<s:property value='TASKID'/>,'<s:property value='JDMC'/>')">查看</a></td>
							</s:else>
						</s:elseif>
					</tr>
				</s:iterator>
			</table>
		</div>
	</div>
</body>
</html>