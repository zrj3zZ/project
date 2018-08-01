<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<META http-equiv="Last-Modified" content="Sat, 10 Nov 1997 09:08:07 GMT">
<title>流程处理中心</title>
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/process_center.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/message/sysmsgpage.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css"/>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/process/process_desk.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script>
	$(function() {
		$('#pp').pagination({
			total : <s:property value="sxcount"/>,
			pageNumber : <s:property value="pageNumber"/>,
			pageSize : <s:property value="pageSize"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMsg(pageNumber, pageSize);
			}
		});
	});
	
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#editForm").submit();
		return;
	}

	function reloadPage() {
		window.location.reload();
	}
	
	function showItem(id) {
		var title = "";
		var height = 580;
		var width = 900;
		var formid = 116;
		var demId = 38;
		var pageurl = 'loadVisitPage.action?formid=' + formid + '&demId=' + demId + '&instanceId=' + id;
		var dialogId = "meetItem";
		parent.parent.openWin(title, height, width,pageurl, this.location, dialogId);
	}
	
	function updateClflag(instanceid, id) {
		$.messager.confirm('确认', '确认更新为“已处理”?', function(result) {
			if (result) {
				var updateUrl = "updateClZdsxFlag.action";
				$.post(updateUrl, {instanceid : instanceid,id : id}, function(data) {
					alert(data);
					window.location.reload();
				});
			}
		});
	}
	
	function checkXpsx(id) {
		var pageUrl = "zqb_xpsxt_check.action?id="+id;
		art.dialog.open(pageUrl,{ 
			title:'信披事项表单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:950,
			cache:false,
			lock: true,
			height:600, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function downloadTemplate(fjid){
		var url = 'uploadifyDownload.action?fileUUID='+fjid;
		window.location.href = url;
	}
	
	function cheackAnncement(instanceid,zqdm,zqjc){
		/* var pageUrl = "loadPage.action?instanceId="+instanceid+"&zqjcxs="+encodeURI(zqjc)+"&zqdmxs="+zqdm;
		art.dialog.open(pageUrl,{
				id:'Category_show',
				cover:true, 
				title:'查看公告',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false
		}); */
		var zqdmxs =$("#zqdmxs").val();
		var zqjcxs =$("#zqjcxs").val();
		var pageUrl = "loadPage.action?instanceId="+instanceid+"&zqjcxs="+encodeURI(zqjcxs)+"&zqdmxs="+zqdmxs;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	
	function dealGzyjHF(gzyjid){
		var url = "zqb_nnouncement_dxyjtzr.action?gzyjid="+gzyjid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
</script>
<style>
.header td {
	height: 30px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}

.content td {
	border: 1px solid #efefef;
	align: center;
	valign: middle;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false" split="false" style="height:35px;" id="layoutNorth">
		<div class="process_header">
			<div class="process_head_tab_box">
				<a class="process_head_tab_active" href="fsp_zydd_index.action">披露信息更新(<font color="red"><s:property value="sxcount"/></font>)</a>
				<s:if test="isdm==false">
					<a class="process_head_tab" title="显示所有会议计划未处理的事宜" href="fsp_hy_index.action">会议计划(<font color="red"><s:property value="hycount" /></font>)</a>
					<a class="process_head_tab" title="显示所有部门通知未处理的事宜" href="fsp_gzsc_index.action">部门通知(<font color="red"><s:property value="gzsccount" /></font>)</a>
				</s:if>
			</div>
			<div style="padding-top:3px;"></div>
		</div>
	</div>
	<div region="center" id="layoutCenter" style="overflow:auto">
		<div class="wr_table">
			<table border="0" cellspacing="0" cellpadding="0">
				<thead>
					<tr class="header" style="border:1px solid #efefef">
						<td align="center"><font style="color:#000000;">公司代码</font></td>
						<td align="center"><font style="color:#000000;">公司简称</font></td>
						<td align="left" colspan="4"><font style="color:#000000;">更新文件信息</font></td>
					</tr>
				</thead>
				<s:iterator value="list" status="st">
					<tbody>
						<tr class="content" height="36px">
							<td align="center">
								<s:property value="ZQDM" />
							</td>
							<td align="center">
								<s:property value="ZQJC" />
							</td>
							<td align="center">
								<s:property value="TYPENAME"/>
							</td>
							<s:if test="TYPE==1">
								<td align="left">
									<a href="javascript:cheackAnncement(<s:property value="INSTANCEID"/>,'<s:property value="ZQDM"/>','<s:property value="ZQJC"/>')"><s:property value="TITLE"/></a>
								</td>
							</s:if>
							<s:elseif test="TYPE==2">
								<td align="left">
									<a href="javascript:checkXpsx(<s:property value="ID"/>)"><s:property value="TITLE"/></a>
								</td>
							</s:elseif>
							<s:elseif test="TYPE==3">
								<td align="left">
									<a href="javascript:cheackAnncement(<s:property value="INSTANCEID"/>,'<s:property value="ZQDM"/>','<s:property value="ZQJC"/>')"><s:property value="TITLE"/></a>
								</td>
							</s:elseif>
							<s:if test="TYPE==1">
								<td align="left">
									<a href="javascript:dealGzyjHF('<s:property value="FILEID"/>')">查看</a>
								</td>
							</s:if>
							<s:else>
								<td align="left">
									<a href="javascript:downloadTemplate('<s:property value="FILEID"/>')"><s:property value="FILESRCNAME"/></a>
								</td>
							</s:else>
							<td align="center">
								<s:property value="TIME"/>
							</td>
						</tr>
					</tbody>
				</s:iterator>
			</table>
		</div>
	</div>
	<s:form id="editForm" name="editForm" action="fsp_zydd_index.action">
		<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
		<s:hidden name="pageSize" id="pageSize"></s:hidden>
	</s:form>
	<div region="south"  border="false" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-left:10px;">
		<div>
			<s:if test="sxcount>0">
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:if>
		</div>
	</div>
</body>
</html>
<script>
	showTaskCount();
</script>