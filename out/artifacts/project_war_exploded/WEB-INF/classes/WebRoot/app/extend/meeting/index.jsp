<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>项目管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	$(function(){
		$.post("zqb_announcement_roleid.action", function(data) {
	        $("#roleid").val(data);
	      });
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
		$("#editForm").submit();
		return ;
	}
	function dofilterYear(obj) {
		
		$("#year").val(obj);
		$("#editForm").attr("action", "zqb_meeting_index.action");
		$("#editForm").submit();
	}
	function dofilterMonth(obj) {
		
		$("#month").val(obj);
	
		$("#editForm").attr("action", "zqb_meeting_index.action");
		$("#editForm").submit();
	}
	function dofilterMeet(obj) {
		$("#meettype").val(obj);
		$("#editForm").attr("action", "zqb_meeting_index.action");
		$("#editForm").submit();
	}
	function dofilterGroup(obj) {

		$("#grouptype").val(obj);
		$("#editForm").attr("action", "zqb_meeting_index.action");
		$("#editForm").submit();
	}
	function dofilterStatus(obj) {
		$("#status").val(obj);
		$("#editForm").attr("action", "zqb_meeting_index.action");
		$("#editForm").submit();
	}
	function addItem() {
		var formid = 96;
		var demId = 26;
		var customerno = $("#customerno").val();
		var customername = $("#customername").val();
		var type=$("#type").val();
		if (customerno == ""||type=="root") {
			alert("未选择公司，无法添加计划");
			return;
		}
		var pageUrl = 'createFormInstance.action?formid=' + formid
				+ '&demId=' + demId + '&CUSTOMERNO=' + encodeURI(customerno)+'&CUSTOMERNAME='+encodeURI(customername);
		var title = "";
		var height = 580;
		var width = 900;
		var dialogId = "projectItem";
		//parent.parent.openWin(title, height, width, pageurl, this.location,dialogId);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+width+',height=600,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		return;
	}

	function addShspsm(id) {
		var customerno = $("#customerno").val();
		var customername = $("#customername").val();
		var type=$("#type").val();
		if (customerno == ""||type=="root") {
			alert("未选择公司，无法添加三会审批说明");
			return;
		}
		var pageurl = "openFormPage.action?formid=147&instanceId=" + id
		+ "&demId=63&CUSTOMERNO="+encodeURI(customerno)+"&CUSTOMERNAME="+encodeURI(customername);
		var title = "";
		var height = 580;
		var width = 900;
		var dialogId = "projectItem";
		parent.parent.openWin(title, height, width, pageurl, this.location,
				dialogId);
		return;
	}
	function showItem(id) {
		var title = "";
		var height = 580;
		var width = 900;
		var pageUrl = "openFormPage.action?formid=96&instanceId=" + id
				+ "&demId=26";
		var dialogId = "meetItem";
		//parent.parent.openWin(title, height, width, pageurl, this.location,dialogId);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+width+',height=600,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	function showVisit(customerno, id) {
		var title = "已召开会议信息";
		var height = 580;
		var width = 900;
		var pageurl = "loadConveneMeeting.action?instanceid=" + id
				+ '&customerno=' + encodeURI(customerno);
		var dialogId = "meet";
		parent.parent.openWin(title, height, width, pageurl, null, dialogId);
	}

	function closeItem(instanceid, projectNo) {
		if (confirm('确定取消当前会议计划吗？')) {
			var pageUrl = "zqb_meeting_remove_item.action?instanceid="
					+ instanceid ;
			$.post(pageUrl, {}, function(data) {
				if (data == 'success') {
					window.location.reload();
				} else {
					alert("取消当前会议信息失败");
				}
			});
		}
	}
	function setFinish(instanceid) {
		var title = "设置会议已召开";
		var height = 200;
		var width = 400;
		var pageurl = "zqb_meeting_edit.action?instanceid=" + instanceid;
		var dialogId = "meetEdit";
		parent.parent.openWin(title, height, width, pageurl, this.location,
				dialogId);
	}
	function setRetime(instanceid) {
		var title = "会议延期时间";
		var height = 200;
		var width = 400;
		var pageurl = "zqb_meeting_meetRetime.action?instanceid="
				+ instanceid;
		var dialogId = "meetRetime";
		parent.parent.openWin(title, height, width, pageurl, this.location,
				dialogId);
	}

	function uploadifyDialog(dialogId, fieldName, divId, sizeLimit, multi,
			fileExt, fileDesc) {
		if (dialogId == null || dialogId == "" || fieldName == null
				|| fieldName == "" || divId == null || divId == "") {
			alert('参数不正确');
			return false;
		}
		var pageurl = 'showUploadifyPage.action?parentColId=' + fieldName
				+ '&parentDivId=' + divId + '&sizeLimit=' + sizeLimit
				+ '&multi=' + multi + '&fileExt=' + fileExt + '&fileDesc='
				+ fileDesc;
		var title = "设置会议延期时间";
		var height = 600;
		var width = 800;
		var dialogId = "meetRetime";
		parent.parent.openWin(title, height, width, pageurl, this.location,
				dialogId);
		return false;
	}

	function showUploadifyPageFJ() {
		uploadifyDialog('FJ', 'FJ', 'DIVFJ', '', 'true', '', '');
	}
	function selCustomerItem() {
		var item = $("#selCustomer").val();
		$("#customerno").val(item);
		$("#editForm").attr("action", "zqb_meeting_index.action");
		$("#editForm").submit();
	}
	function commitFilePage(customerno, instanceid, isOwner) {
		var pageUrl = 'zqb_meeting_commitFile.action?customerno='
				+ encodeURI(customerno) + '&instanceid=' + instanceid + '&isOwner='
				+ isOwner;
		var title = "提交会议资料";
		var height = 600;
		var width = 800;
		var dialogId = "meetFile";
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+width+',height=600,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	function returnFileList(customerno, instanceid) {
		// 判断反馈资料清单是否已提交  
		var pageUrl = "zqb_meeting_checkReturnList.action";
		$.post(pageUrl, {
			customerno : customerno,
			instanceid : instanceid
		}, function(data) {
			if (data == 'null') {
				returnFilePage("add", customerno, instanceid, null);
			} else {
				if (data != "") {
					returnFilePage("edit", customerno, instanceid, data);
				}
			}
		});

	}
	function returnFilePage(type, customerno, instanceid, data) {
		var pageUrl = "";
		var formid = 117;
		var demId = 39;
		if (type == "add") {
			pageUrl = 'createFormInstance.action?formid=' + formid
					+ '&demId=' + demId + '&KHBH=' + customerno + '&HYBH='
					+ instanceid;
		} else {
			pageUrl = 'openFormPage.action?formid=' + formid + '&demId='
					+ demId + '&instanceId=' + data;
		}

		var title = "";
		var height = 580;
		var width = 660;
		var dialogId = "projectItem";
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+width+',height=600,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		/* parent.parent.openWin(title, height, width, pageurl, this.location,
				dialogId); */
	}
	
	function addAnnouncement(meetname,id){
		var zqdmxs =$("#zqdmxs").val();
		var zqjcxs =$("#zqjcxs").val();
		var khbh =$("#customerno").val() ;
		if(khbh==null || khbh==''||khbh=="undefined"||khbh=="0"){
		    $.messager.alert('提示信息','请选择挂牌公司!','info');  
			return;
		}
		var dmggsplc=$("#dmggsplc").val();
		var ggsplc=$("#ggsplc").val();
		var roleid= $("#roleid").val();
		var pageUrl ;
		if(roleid=="3"){
		   pageUrl = "processRuntimeStartInstance.action?actDefId="+dmggsplc+"&KHBH="+encodeURI(khbh)+"&MEETNAME="+encodeURI(meetname)+"&MEETID="+id+"&ZQJCXS="+encodeURI(zqjcxs)+"&ZQDMXS="+zqdmxs;
		}else{
		    pageUrl = "processRuntimeStartInstance.action?actDefId="+ggsplc+"&KHBH="+encodeURI(khbh)+"&MEETNAME="+encodeURI(meetname)+"&MEETID="+id+"&ZQJCXS="+encodeURI(zqjcxs)+"&ZQDMXS="+zqdmxs;
		}
		var title = "新增公告";
		var height = 600;
		var width = 900;
		var dialogId = "projectItem";
		parent.parent.openWin(title, height, width, pageUrl, this.location,
				dialogId);
		return;
		
	}
	 function uploadItem(customerno){
		 var seachUrl ="toExcl.action?customerno="+customerno;
		window.location.href = seachUrl;
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
			height:30px;
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
					padding:3px 4px;
					white-space:nowrap;
					word-wrap:normal;
					text-align:left;
					overflow:hidden;
					text-decoration:none;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
				}
				
</style>
</head>
<body class="easyui-layout">

	<div region="center" border="false">
		<div style = "padding:10px">
			<s:if test="readonly!=true">
				<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加会议计划</a>
				<a href="javascript:addShspsm(<s:property value="shenPi"/>);" class="easyui-linkbutton" plain="true" iconCls="icon-add">三会审批说明</a>
			</s:if>
			<s:if test="flag==0">
				<a href="javascript:uploadItem('<s:property value="customerno"/>');" class="easyui-linkbutton" plain="true" iconCls="icon-add">导出</a>
			</s:if>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
		<div style = "padding-left:10px;">
			<table width="100%" cellpadding="0" cellspacing="0" style="border:1px solid #efefef">
				<tr  class="header">
					<td>会议名称</td>
					<td>创建时间</td>
					<td>会议类型</td>
					<td>召开日期</td>
					<td>会议状态</td>
					<td>操作</td>
				</tr>
				<s:iterator value="runList">
					<tr  class="cell">
						<s:if test="STATUS=='未召开'">
							
							<td class="itemicon" onclick="showItem('<s:property value="INSTANCEID"/>')"><s:property value="MEETNAME" /></td>
						</s:if>
						<s:else>
							<td class="itemicon" onclick="showVisit('<s:property value="CUSTOMERNO"/>','<s:property value="INSTANCEID"/>')"><s:property value="MEETNAME" /></td>
						</s:else>
						<td><s:property value="JHCJSJ" /></td>
						<td><s:property value="MEETTYPE" /></td>
						<td><s:property value="PLANTIME" /></td>
						<td><s:property value="STATUS" /></td>
						<td>
							<s:if test="STATUS=='已召开'">
									<s:if test="yys==4 || yys==5">
										<a href="javascript:closeItem(<s:property value="INSTANCEID"/>,'<s:property value="ID"/>')">取消</a>		
									</s:if>
										
							</s:if>
							<s:else>
								
								<a href="javascript:setFinish('<s:property value="INSTANCEID"/>')">已召开</a>&nbsp;&nbsp;
	      						<a href="javascript:setRetime('<s:property value="INSTANCEID"/>')">延期召开</a>&nbsp;&nbsp;
	      						<a href="javascript:closeItem(<s:property value="INSTANCEID"/>,'<s:property value="ID"/>')">取消</a>
	      						
	      					</s:else>
	      					
      					</td>
					</tr>
				</s:iterator>
			</table>
			</div>
		</div>
		<s:form id="editForm" name="editForm" action="zqb_meeting_index.action">
			<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
			<s:hidden id="customerno" name="customerno"></s:hidden>
			<s:hidden id="customername" name="customername"></s:hidden>
			<s:hidden id="year" name="year"></s:hidden>
			<s:hidden id="month" name="month"></s:hidden>
			<s:hidden id="grouptype" name="grouptype"></s:hidden>
			<s:hidden id="meettype" name="meettype"></s:hidden>
			<s:hidden id="status" name="status"></s:hidden>
			<s:hidden id="type" name="type"></s:hidden>
			<s:hidden id="roleid" name="roleid"></s:hidden>
			<s:hidden name="ggsplc" id="ggsplc"></s:hidden>
			<s:hidden name="zqjcxs" id="zqjcxs"></s:hidden>
			<s:hidden name="zqdmxs" id="zqdmxs"></s:hidden>
		<s:hidden name="dmggsplc" id="dmggsplc"></s:hidden>
		</s:form>
	<div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
	<div style = "padding:5px">
			<s:if test="totalNum==0">
			
			</s:if><s:else>
			<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
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