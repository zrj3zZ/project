<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>上传资料下载</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"> </script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
<script type="text/javascript" src="iwork_js/json.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	function downloadFile(fileuuid) {
		var downUrl = "uploadifyDownload.action?fileUUID="+fileuuid;
		window.location.href = downUrl; 
	}
	function downZip() {
		var fileuuidarray=document.getElementsByName('fileuuid'); 
		var fileuuidlist='';
		for(var i=0; i<fileuuidarray.length; i++){
			if(fileuuidarray[i].checked){
				fileuuidlist+=fileuuidarray[i].value+'\\\\';
			}
		}
		var filename=parent.document.getElementById("filename").value;
		var downUrl = encodeURI("zqb_file_downloadZip.action?filename="+filename+"+"+"&fileuuidlist="+fileuuidlist);
		if(fileuuidlist!=''){
			window.location.href = downUrl;	
		}else{
			alert("未勾选文件!");
		}
	}
	$(function(){
		$("#chkAll").bind("click", function () {
	    	$("[name =fileuuid]:checkbox").attr("checked", this.checked);
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
		$("#frmMain").submit();
		return ;
	}
</script>
<style type="text/css">
.header td{
			/* font-weight:bold; */
			font-size:12px;
			padding:8px;
			white-space:nowrap;
			padding-left:5px;
			background:#fafafa url('../../iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		}
</style>
</head>
<body>
	<div style="width:85%;padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<div style="padding:5px">
			<table id='iform_grid' style="border:1px solid #efefef;width:75%;">
				<button onclick="downZip();" style="float: left;margin-left: 70%;">打包下载</button>
				<tr class="header">
					<td style="width:2.5%;" align="center"><input type="checkbox" name="colname" id="chkAll" /></td>
					<td style="width:19.5%;" align="center">公司名称</td>
					<td style="width:19.5%;" align="center">模块名称</td>
					<td style="width:19.5%;" align="center">模块细分</td>
					<td style="width:19.5%;" align="center">文件名称</td>
					<td style="width:19.5%;" align="center">下载</td>
				</tr>
				<s:iterator value="downlist" status="status">
					<tr class="cell">
						<td align="center"><input name="fileuuid" type="checkbox" value="<s:property value="FILEUUID" />//<s:property value="FILE_SRC_NAME" />//<s:property value="GNMK" />/-/<s:property value="CUSTOMERNAME" />/-/<s:property value="XFMK" />"/></td>
						<td align="center"><s:property value="CUSTOMERNAME" /></td>
						<td align="center"><s:property value="GN" /></td> 
						<td align="center"><s:property value="XFMK" /></td>
						<td align="center"><s:property value="FILE_SRC_NAME" /></td>
						<td align="center"><button onclick="downloadFile('<s:property value="FILEUUID" />');">下载</button></td>
					</tr>
				</s:iterator>
			</table>

			<form action="zqb_file_download.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="gnmk" id="gnmk"></s:hidden>
				<s:hidden name="gsmc" id="gsmc"></s:hidden>
				<s:hidden name="xfmk" id="xfmk"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false">
		<div style="padding:5px">
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
