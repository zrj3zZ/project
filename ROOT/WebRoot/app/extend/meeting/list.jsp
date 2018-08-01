<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>项目管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
<link rel="stylesheet" type="text/css"
	href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript">
	function dofilterYear(obj) {
		$("#year").val(obj);
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
		if (customerno == "") {
			alert("未发现客户编号，无法添加计划");
			return false;
		}
		var pageurl = 'createFormInstance.action?formid=' + formid
				+ '&demId=' + demId + '&CUSTOMERNO=' + customerno;
		var title = "";
		var height = 580;
		var width = 900;
		var dialogId = "projectItem";
		parent.openWin(title, height, width, pageurl, this.location, dialogId);
		return;
	}

	function showItem(id) {
		var title = "";
		var height = 580;
		var width = 900;
		var pageurl = "openFormPage.action?formid=96&instanceId=" + id
				+ "&demId=26";
		var dialogId = "meetItem";
		parent.openWin(title, height, width, pageurl, this.location, dialogId);
	}
	function showVisit(customerno, id) {
		var title = "已召开会议信息";
		var height = 580;
		var width = 900;
		var pageurl = "loadConveneMeeting.action?instanceid=" + id
				+ '&customerno=' + customerno;
		var dialogId = "meet";
		parent.parent.openWin(title, height, width, pageurl, null, dialogId);
	}
	function closeItem(instanceid, projectNo) {
		if (confirm('确定关闭当前项目吗？')) {
			var pageUrl = "zqb_project_remove_item.action?instanceid="
					+ instanceid + "&projectNo=" + projectNo + "&taskid=0";
			$.post(pageUrl, {}, function(data) {
				if (data == 'success') {
					this.location.reload();
				} else {
					alert("关闭项目失败");
					;
				}
			});
		}
	}
	function setFinish(instanceid) {
		var title = "设置会议已召开";
		var height = 300;
		var width = 400;
		var pageurl = "zqb_meeting_edit.action?instanceid=" + instanceid;
		var dialogId = "meetEdit";
		parent.openWin(title, height, width, pageurl, this.location, dialogId);
	}
	function setRetime(instanceid) {
		var title = "设置会议延期时间";
		var height = 300;
		var width = 400;
		var pageurl = "zqb_meeting_meetRetime.action?instanceid="
				+ instanceid;
		var dialogId = "meetRetime";
		parent.openWin(title, height, width, pageurl, this.location, dialogId);
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
		parent.openWin(title, height, width, pageurl, this.location, dialogId);
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
		var pageurl = 'zqb_meeting_commitFile.action?customerno='
				+ customerno + '&instanceid=' + instanceid + '&isOwner='
				+ isOwner;
		var title = "提交会议资料";
		var height = 600;
		var width = 800;
		var dialogId = "meetFile";
		parent.openWin(title, height, width, pageurl, this.location, dialogId);
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
		var pageurl = "";
		var formid = 117;
		var demId = 39;
		if (type == "add") {
			pageurl = 'createFormInstance.action?formid=' + formid
					+ '&demId=' + demId + '&KHBH=' + customerno + '&HYBH='
					+ instanceid;
		} else {
			pageurl = 'openFormPage.action?formid=' + formid + '&demId='
					+ demId + '&instanceId=' + data;
		}

		var title = "";
		var height = 580;
		var width = 660;
		var dialogId = "projectItem";
		parent.openWin(title, height, width, pageurl, this.location, dialogId);
	}
</script>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

.groupTitle {
	font-family: 黑体;
	font-size: 12px;
	text-align: left;
	color: #666;
	height-line: 20px;
	padding: 5px;
	padding-left: 15px;
}

.itemList {
	font-family: 宋体;
	font-size: 12px;
	height: 200px;
	padding-left: 15px;
}

.itemList td {
	list-style: none;
	height: 20px;
	padding: 2px;
	padding-left: 20px;
}

.itemList tr:hover {
	color: #0000ff;
	cursor: pointer;
}

.itemList  td {
	font-size: 12px;
}

.itemicon {
	padding-left: 25px;
	background: transparent url(iwork_img/pin.png) no-repeat scroll 0px 3px;
}

.selectBar {
	border: 1px solid #efefef;
	margin: 5px;
	background: #FDFDFD;
	padding: 20px
}

.selectBar td {
	vertical-align: middle;
	height: 20px;
}

.selectBar td linkbtn {
	color: #0000FF;
	text-decoration: none;
}

.listTitle td {
	background-color: #efefef;
	font-weight: bold;
	color: #666;
}
</style>
</head>
<body class="easyui-layout">

	<div region="north" border="false"></div>
	<div region="center" border="false" style="overflow:hidden">
		<div id="mainFrameTab" style="border:0px" class="easyui-tabs"
			fit="true">
		<!--  	<div title="最近一个月的会议情况" border="false" style="border:0px"
				iconCls="icon-search" cache="false">
				<div class="tools_nav">
					<!-- 
	         	<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加会议计划</a>
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a> 
				</div>
				<div class="itemList" id="itemList">
					<table width="100%" cellpadding="0" cellspacing="0">
						<tr class="listTitle">
							<td>会议名称</td>
							<td>会议类型</td>
							<td>召开日期</td>
							<td>会议状态</td>
							<td>操作</td>
						</tr>
						<s:iterator value="runList">
							<tr>
								<s:if test="STATUS=='未召开'">
									<td class="itemicon"
										onclick="showItem('<s:property value="INSTANCEID"/>')"><s:property
											value="MEETNAME" /> (<s:property value="CUSTOMERNAME" />)</td>
								</s:if>
								<s:else>
									<td class="itemicon"
										onclick="showVisit('<s:property value="CUSTOMERNO"/>','<s:property value="INSTANCEID"/>')"><s:property
											value="MEETNAME" /> (<s:property value="CUSTOMERNAME" />)</td>
								</s:else>
								<td><s:property value="MEETTYPE" /></td>
								<td><s:date name="PLANTIME" format="yyyy-MM-dd HH:mm:ss" /></td>
								<td><s:property value="STATUS" /></td>
								<td><s:if test="STATUS=='已召开'">
										<s:if test="customerList!=null&&customerList.size()>0">
											<a
												href="javascript:returnFileList('<s:property value="CUSTOMERNO"/>',<s:property value="INSTANCEID"/>)">反馈资料清单</a>&nbsp;&nbsp;
      							 <a
												href="javascript:commitFilePage('<s:property value="CUSTOMERNO"/>',<s:property value="INSTANCEID"/>,false)">查看资料</a>&nbsp;&nbsp;
      							
      						 </s:if>
										<s:else>
											<a
												href="javascript:commitFilePage('<s:property value="CUSTOMERNO"/>',<s:property value="INSTANCEID"/>,true)">提交资料</a>&nbsp;&nbsp;
      						 </s:else>
									</s:if> <s:else>
										<a
											href="javascript:setFinish('<s:property value="INSTANCEID"/>')">已召开</a>&nbsp;&nbsp;
      					<a
											href="javascript:setRetime('<s:property value="INSTANCEID"/>')">延期召开</a>&nbsp;&nbsp;
      					<a
											href="javascript:closeItem(<s:property value="INSTANCEID"/>,'<s:property value="ID"/>')">取消</a>
									</s:else></td>
							</tr>
						</s:iterator>
					</table>
				</div>
				<s:form id="editForm" name="editForm">
					<s:hidden id="customerno" name="customerno"></s:hidden>
					<s:hidden id="year" name="year"></s:hidden>
					<s:hidden id="grouptype" name="grouptype"></s:hidden>
					<s:hidden id="meettype" name="meettype"></s:hidden>
					<s:hidden id="status" name="status"></s:hidden>
				</s:form>
			</div> -->
			<s:if test="customerList!=null&&customerList.size()>0">
				<div title="按客户查询" border="false" iconCls="icon-search"
					cache="false">
					<iframe src="zqb_meeting_index.action" scrolling="auto"
						frameborder="0" style="width:100%;height:100%;"></iframe>
				</div>
			</s:if>
			<s:else>
				<div title="全部会议" border="false" iconCls="icon-search" cache="false">

					<iframe src="zqb_meeting_index.action" scrolling="auto"
						frameborder="0" style="width:100%;height:100%;"></iframe>
				</div>
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