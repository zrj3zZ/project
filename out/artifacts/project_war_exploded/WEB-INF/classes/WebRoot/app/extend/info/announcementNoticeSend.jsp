<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公告统计</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
		mainFormValidator =  $("#ifrmMain").validate({
		 });
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
	});
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return;
	}
	$(function() {
		//查询
		$(document).bind('keydown', function(event) {
			if (event.keyCode == "13") {
				//禁用键盘按钮按下事件
				return false;
			}
		});
		$("#search").click(
				function() {
					var valid = mainFormValidator.form(); //执行校验操作
					if(!valid){
						return;
					}
					var tzbt = $("#TZBT").val();
					var spzt = $("#SPZT").val();
					var tzlx = $("#TZLX").val();
					var seachUrl = encodeURI("zqb_announcement_notice_send.action?tzbt=" + tzbt + "&spzt=" + spzt+ "&tzlx=" + tzlx);
					window.location.href = seachUrl;
				});
		});
	
	function addItem(){
		var pageUrl = "zqb_announcement_notice_add.action";		
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}	
		
	function showNotice(ggid){
		var pageUrl = "zqb_announcement_notice_add.action?ggid="+ggid;	
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}	
	//批量下载回复附件
	function pldownload(id,obj){
		if(obj.value==""){
			return;
		}
		var roleid="0";
		if(obj.value=="du"){
			roleid="4";
		}
		showDiv();
		var Url = "zqb_cxfjsl.action?ggid="+id+"&roleid="+roleid;
		var seachUrl = encodeURI("zqb_download_fj.action?ggid="+id+"&roleid="+roleid);
		 $.post(Url,function(data){ 
	   			if(data=='0'){
					alert("实体文件不存在!");
					hideDiv();
	   			}else{
	   				downFile(id,roleid);
	   			}
		 });
	}
	function downFile(id,roleid){
		var seachUrl = encodeURI("zqb_download_fj.action?ggid="+id+"&roleid="+roleid);
		$.ajax({
			type: "POST",
            url: seachUrl,
            success:function(date){
            	if(date!="" && date!=null){
            		window.location.href = encodeURI("zqb_download_tzgg.action?filename="+date);
            	}
            },
            complete: function () {
            	hideDiv();
            }
         });
	}
	function goReception(){
		var seachUrl = "zqb_announcement_notice_reception.action";
		window.location.href = seachUrl;
	}
	function removeNotice(id){
		 $.messager.confirm('确认','确认删除?',function(result){ 
		 	if(result){
					var deleteUrl = "deleteSendNotice.action";
					$.post(deleteUrl,{ggid:id},function(data){ 
	       			if(data=='success'){
	       				window.location.reload();
	       			}else{
	       				alert(data);
	       			}
	     		 });
				 } 
		});
	}
	function commitNotice(ggid){
		$.messager.confirm('确认','确认完成?',function(result){ 
		 	if(result){
					var updateUrl = "updateSendNotice.action";
					$.post(updateUrl,{ggid:ggid},function(data){ 
	       			if(data=='success'){
	       				window.location.reload();
	       			}else{
	       				alert(data);
	       			}
	     		 });
				 } 
		});
	}
	
	function noticeUpload(ggid){
	var pageurl = 'zqb_announcement_list_commitFile.action?ggid=' + ggid;
	var title = "资料下载";
	var height = 600;
	var width = 800;
	var dialogId = "meetFile";
	parent.parent.openWin(title, height, width, pageurl, this.location,
			dialogId);
	}
	function expWjdc(ggid){
		var pageUrl = "zqb_vote_expwjdc.action?ggid="+ggid;
		window.location.href = pageUrl; 
	}
	function senSMSToNotReply(ggid){
		$.messager.confirm('确认','确认通知未反馈人?',function(result){
			if(result){
				$.post("zqb_vote_sensmstonotreply.action",{ggid:ggid},function(data){
					art.dialog.tips("发送成功",2);
				});
			}
		});
	}
	function showDiv() {   
	    document.getElementById("bg").style.display ="block";  
	    document.getElementById("show").style.display ="block";  
	}  
	  
	function hideDiv(){  
	    document.getElementById("bg").style.display ="none";  
	    document.getElementById("show").style.display ="none";    
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
			<s:if test="tzggGrantCUD"><a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增</a></s:if>
			<a href="javascript:window.location.reload();"class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<%-- <a href="javascript:goReception();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">查看接收的通知(<s:property value="wdsize" />)</a> --%>
		</div>
	</div>
	<div region="center"
		style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px">
				<div
					style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">通知标题</td>
										<td class="searchdata"><input type='text' class='{string:true,maxlength:64}' style="width:100px;" name='TZBT' id='TZBT' value='' form-type='al_textbox'></td>
										<td class="searchtitle">通知类型</td>
										<td>
											<select name="TZLX" id="TZLX">
											<option value="" >-空-</option>
											<option value="培训通知">培训通知</option>
											<s:if test="zqServer=='hlzq' || zqServer=='dgzq'"><option value='股东质押查询'>股东质押查询</option></s:if>
											<option value="其他通知">其他通知</option>
											<option value="问卷调查">问卷调查</option>
											<s:if test="zqServer=='dgzq'"><option value='弹窗通知'>弹窗通知</option></s:if>
											</select>
										</td>
										<td class="searchtitle">状态</td>
										<td>
											<select name="SPZT" id="SPZT">
											<option value="" >-空-</option>
											<option value="未完成">未完成</option>
											<option value="完成">完成</option>
											</select>
										</td>
									</tr>
								</table>
							</td>
							<td valign='bottom' style='padding-bottom:15px;'><a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px">


			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:40%;">通知标题</td>
					<td style="width:10%;">类型</td>
					<td style="width:10%;">发送人</td>
					<td style="width:10%;">发送时间</td>
					<td style="width:20%;">最迟回复时间</td>
					<td style="width:10%;">操作</td>
				</tr>
				<s:iterator value="List" status="status">
					<tr class="cell">
						<td style="cursor:pointer;" onclick="showNotice('<s:property value='ID'/>');"><s:property value="TZBT" /></td>
						<td><s:property value="TZLX" /></td>
						<td><s:property value="FSR" /></td>
						<td><s:property value="FSSJ" /></td>
						<td><s:property value="ZCHFSJ" /></td>
						<td>
							<s:if test="FSZT!='完成'&&tzggGrantCUD">
								&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:commitNotice('<s:property value='ID'/>')">完成</a>
							</s:if>
							<s:if test="TZLX=='问卷调查'">
								&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:expWjdc('<s:property value='ID'/>')">导出</a>
							</s:if>
							<s:if test="tzggGrantCUD">
								&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:removeNotice('<s:property value='ID'/>')">删除</a>
								&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:senSMSToNotReply('<s:property value='ID'/>')">短信通知未反馈人</a>
							</s:if>
							<s:if test="TZLX=='培训通知'||TZLX=='其他通知'">
								<select onchange="pldownload('<s:property value='ID'/>',this);">
								<option value="">请选择下载内容</option>
								<option value="all">下载所有反馈资料</option>
								<option value="du">下载督导企业反馈</option>
								</select>
							</s:if>
						</td> 
					</tr>
				</s:iterator>
			</table>
			<form action="zqb_announcement_notice_send.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="tzbt" id="tzbt"></s:hidden>
				<s:hidden name="spzt" id="spzt"></s:hidden>
				<s:hidden name="tzlx" id="tzlx"></s:hidden>
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
	<script type="text/javascript">
	$(function(){
		$("#TZBT").val($("#tzbt").val());
		$("#SPZT").val($("#spzt").val());
		$("#TZLX").val($("#tzlx").val());
	});
	</script>
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