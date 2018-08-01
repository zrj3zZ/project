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
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
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
		$("#search").click(
				function() {
					var valid = mainFormValidator.form(); //执行校验操作
					if(!valid){
						return false;
					}
					var khmc=$("#khmc").val();
					var khbh = $("#khbh").val();
					var nmxx = $("#NMXX").val();
					var nmxxStart = $("#NMXXSTART").val();
					var nmxxEnd = $("#NMXXEND").val();
					var seachUrl = encodeURI("zqb_nmsx_list.action?nmxx=" + nmxx + "&nmxxStart=" + nmxxStart + "&nmxxEnd=" + nmxxEnd+"&khbh=" + khbh + "&khmc=" + khmc);
					$.post("checkdate.action", { startdate: nmxxStart,enddate:nmxxEnd }, function (data) {
						if(data=='error1'){
							alert("起始时间不能大于截止时间！");
						}else{
							window.location.href = seachUrl;
						}
					});
					
				});
	});
	
	function addItem(){
		var khbh = $("#khbh").val();
    	var khmc = $("#khmc").val();
		if (khbh=="0"||khbh==""||khbh==null||khbh=="undefined"||khmc==""||khmc==null||khmc=="undefined"){
			alert("未选择公司，无法添加内幕信息");
			return ;
		}
			var formid = $("#formid").val();
			var demId = $("#id").val(); 
			var pageUrl =  encodeURI('createFormInstance.action?formid='+formid+'&demId='+demId+'&KHBH='+khbh+'&KHMC='+khmc); 
			/*art.dialog.open(pageUrl,{ 
				id:'Category_show',
				cover:true,
				title:'新增内幕信息',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:900,
				cache:false,
				lock: true,
				height:700, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,				
				close:function(){
                window.location.reload();
                }
			});*/
			var target = "_blank";
    		var win_width = window.screen.width;
    		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
    		page.location = pageUrl;
	}	
		
	function editNmxx(instanceid){
		var str=instanceid.split(".")[0];
		var formid=$("#formid").val();
		var demId=$("#id").val();
		var pageUrl = "openFormPage.action?formid="+formid+"&demId="+demId+"&instanceId="+str;
		/*art.dialog.open(pageUrl,{ 
			title:'修改内幕信息',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:580, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,			
			close:function(){
				window.location.reload();
			}
		});*/
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	
	function removeNmxx(instanceid){
		var str=instanceid.split(".")[0];
		if(confirm("确定执行删除操作吗?")) {
			var pageUrl = "zqb_nmxx_remove.action";
			$.post(pageUrl,{instanceid:str},function(data){ 
		       			if(data=='success'){
		       				window.location.reload();
		       			}else{
		       				alert("删除失败");;
		       			} 
		     }); 
		}
	}
	
	function showNotice(ggid){
		var pageUrl = "zqb_announcement_notice_add.action?ggid="+ggid;
		/*art.dialog.open(pageUrl,{
			title:'查看公告',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:780, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,			
			close:function(){
				window.location.reload();
			}
		});*/
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}	
		
	function goReception(){
		var seachUrl = "zqb_announcement_notice_reception.action";
		window.location.href = seachUrl;
	}
	
	function removeNotice(instanceid){
		 $.messager.confirm('确认','确定执行删除操作吗?',function(result){ 
		 	if(result){
					var deleteUrl = "zqb_nmxx_remove.action";
					$.post(deleteUrl,{instanceid:instanceid},function(data){ 
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
	
	function uploadNmxx(instanceid){
		var pageUrl="zqb_nmxx_upload.action";
		$.post(pageUrl,{instanceid:instanceid},function(data){
			if(data!='error'){
				var seachUrl = encodeURI("uploadifyDownload.action?fileUUID="+data);
	            window.location.href = seachUrl;
			}
   		});
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
	<div region="north" border="false">
		<div class="tools_nav">
		<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增</a>
			<a href="javascript:window.location.reload();"
				class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
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
										<td class="searchtitle">内幕信息</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:200,required:false,string:true}'
											style="width:100px;" name='NMXX' id='NMXX' value='<s:property value="nmxx" />'
											form-type='al_textbox'></td>
										<td id="title_STARTDATE" class="searchtitle" width="400">起始时间
											<input type='text' onfocus="var ENDDATE=$dp.$('NMXXEND');WdatePicker({onpicked:function(){ENDDATE.focus();},maxDate:'#F{$dp.$D(\'NMXXEND\')}'})"
											style="width:100px" name='NMXXSTART'
											id='NMXXSTART' value='<s:property value="nmxxStart" />'> 截止时间 <input type='text'
											onfocus="WdatePicker({minDate:'#F{$dp.$D(\'NMXXSTART\')}'})"
											onchange="checkRQ()" style="width:100px" name='NMXXEND'
											id='NMXXEND' value='<s:property value="nmxxEnd" />'>
										</td>
									</tr>
								</table>
							</td>
							<td valign='bottom' style='padding-bottom:15px;'><a
								id="search" class="easyui-linkbutton" icon="icon-search"
								href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px">


			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:5%;">序号</td>
					<td style="width:50%;">内幕事项</td>
					<td style="width:15%;">内幕时间</td>
					<td style="width:20%;">附件</td>
					<td style="width:10%;">操作</td>
				</tr>
				<s:iterator value="List" status="status">
					<tr class="cell">
						<td><s:property value="#status.count"/></td>
						<td><s:property value="NMSX" /></td>
						<td><s:property value="NMSJ" /></td>
						<td><s:if test="NMSXZL==true"></s:if><s:else><a href="javascript:uploadNmxx('<s:property value='INSTANCEID'/>')">下载</a></s:else></td>
						<td><a href="javascript:editNmxx('<s:property value='INSTANCEID'/>')">编辑</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:removeNmxx('<s:property value='INSTANCEID'/>')">删除</a>
						</td> 
					</tr>
				</s:iterator>
			</table>
			<form action="zqb_nmsx_list.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden id="khmc" name="khmc"></s:hidden>
  	  			<s:hidden id="khbh" name="khbh"></s:hidden>
  	  			<s:hidden id="formid" name="formid"></s:hidden>
  	  			<s:hidden id="id" name="id"></s:hidden>
  	  			<s:hidden id="nmxxStart" name="nmxxStart"></s:hidden>
  	  			<s:hidden id="nmxxEnd" name="nmxxEnd"></s:hidden>
  	  			<s:hidden id="nmxx" name="nmxx"></s:hidden>
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
	<%-- <script type="text/javascript">
	$(function(){
		$("#TZBT").val($("#tzbt").val());
		$("#SPZT").val($("#spzt").val());
	});
	</script> --%>
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