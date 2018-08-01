<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信披自查反馈</title>
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
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	$(function() {
		$('#pp').pagination({
			total : <s:property value="totalNum"/>,
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
		$("#frmMain").submit();
		return;
	}
	//批量下载当前信披自查反馈附件
	function pldownload(id){
		showDiv();
		var Url = "zqb_vote_cxxpfjsl.action?ggid="+id;
		var seachUrl = encodeURI("zqb_download_xpfj.action?ggid="+id);
		 $.post(Url,function(data){ 
	   			if(data=='0'){
					alert("实体文件不存在!");
					hideDiv();
	   			}else{
	   				
	   				downFile(id);
	   			}
		 });
	}
	function downExcel(id){
		var seachUrl = encodeURI("zqb_downCheck_xpfj.action?ggid="+id);
		 $.post(seachUrl,function(data){ 
	   			if(data=='0'){
					alert("该问卷无人回答！");
	   			}else{
	   				 var downUrl =encodeURI("zqb_downExcel_xpfj.action?ggid="+id);
	   				window.location.href = downUrl;
	   			}
		 });
	}
	function downFile(id){
		
		var seachUrl = encodeURI("zqb_download_xpfj.action?ggid="+id);
	//	 window.location.href = seachUrl;
		$.ajax({
			type: "POST",
            url: seachUrl,
            success:function(date){
            	if(date!="" && date!=null){
            		window.location.href = encodeURI("zqb_download.action?filename="+date);
            	}
            },
            complete: function () {
            	hideDiv();
            }
         });
		
	}
function showVote(ggid,dqrq){
		var pageUrl = "zqb_vote_goList.action?ggid="+ggid+"&dqrq="+dqrq;
		art.dialog.open(pageUrl,{
			title:'信披自查反馈统计',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
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
var isIe = (document.all) ? true : false;

//创建渐变的背景层
function showBackDIV() {
	var bWidth = parseInt(document.body.clientWidth);//parseInt(document.body.scrollWidth);
	var bHeight = parseInt(document.body.clientHeight);//parseInt(document.body.scrollHeight);

	var back = document.createElement("div");
	back.id = "back";
	var styleStr = "top:0px;left:0px;position:absolute;z-index:9998;background:gray;width:" + bWidth + "px;height:" + bHeight + "px;";
	styleStr += (isIe) ? "filter:alpha(opacity=0);" : "opacity:0;";
	back.style.cssText = styleStr;
	document.body.appendChild(back);
	showBackground(back, 20);
}

//让背景渐渐变暗
function showBackground(obj, endInt) {
	if (isIe) {
		obj.filters.alpha.opacity += 1;
		if (obj.filters.alpha.opacity < endInt) {
			setTimeout( function() {
				showBackground(obj, endInt)
			}, 1);	//1秒
		}
	} else {
		var al = parseFloat(obj.style.opacity);
		al += 0.01;
		obj.style.opacity = al;
		if (al < (endInt / 100)) {
			setTimeout( function() {
				showBackground(obj, endInt)
			}, 1);
		}
	}
}    
function showDiv() {   
    document.getElementById("bg").style.display ="block";  
    document.getElementById("show").style.display ="block";  
}  
  
function hideDiv(){  
    document.getElementById("bg").style.display ="none";  
    document.getElementById("show").style.display ="none";    
}

function previewPaper(){
	var url = 'zqb_vote_previewpaper.action';
	var target = "_blank";
	var win_width = window.screen.width-50;
	var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	page.document.location = url;
	return;
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

.cell td{
			margin:0;
			padding:3px 4px;
			height:25px;
			font-size:12px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #eee;
		}
		.cell:hover{
			background-color:#F0F0F0;
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
		    #bg{   
        display: none;   
        position: absolute;   
        top: 0%;   
        left: 0%;   
        width: 100%;   
        height: 100%;   
        background-color: black;   
        z-index:1001;   
        -moz-opacity: 0.3;   
        opacity:.30;   
        filter: alpha(opacity=30);  
    }  
      
  
</style>
</head>
<body class="easyui-layout">
    <div class="container_div"><!-- 遮罩 -->  
		<div id="bg"></div>  
		<div id="show" style="display:none;position:absolute;z-index:9999;height:30px;width:200px;top:30%;left:50%;margin-left:-150px;text-align:center;border: solid 2px #86a5ad">
			<i class="fa fa-spinner fa-spin"></i>文件打包中，请稍候......
		</div>    
    </div>  
	<div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:previewPaper();" class="easyui-linkbutton" plain="true" iconCls="icon-process-addsign">预览最新问卷</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<div region="center"
		style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<div style="padding:5px">
			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:50%;">通知标题</td>
					<td style="width:10%;">发送人</td>
					<td style="width:10%;">发送时间</td>
					<td style="width:20%;">最迟回复时间</td>
					<td style="width:10%;">操作</td>
				</tr>
				<s:iterator value="list" status="status">
					<tr class="cell">
						<td><a href="javascript:showVote('<s:property value='ID'/>','<s:property value='DQRQ'/>')"><s:property value="TZBT" /></a></td>
						<td><s:property value="FSR" /></td>
						<td><s:property value="FSSJ" /></td>
						<td><s:property value="ZCHFSJ" /></td>
						<td>
						<a onclick="downExcel('<s:property value='ID'/>');" href="javascript:void(0);">导出答案</a>&nbsp;|&nbsp;
						<a onclick="pldownload('<s:property value='ID'/>');" href="javascript:void(0);">下载回复附件</a></td>
						</td> 
					</tr>
				</s:iterator>
			</table>
			<form action="zqb_vote_list.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south"
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
</body>
</html>