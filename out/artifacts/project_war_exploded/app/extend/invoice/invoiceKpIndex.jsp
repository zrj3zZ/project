<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
 <base target="_self">
  
    <title>日常业务呈报</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css"/>
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.min.js"  charset="utf-8" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"  ></script>
    <link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
    <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">

	
	function addItem(){
		var kpformid=$("#kpformid").val();
		var kpid=$("#kpid").val();
		var pageUrl = "createFormInstance.action?formid="+kpformid+"&demId="+kpid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	
	function setKpOpenFormPage(instanceId,formId,demId){
		var url = 'openFormPage.action?formid='+formId+'&instanceId='+instanceId+'&demId='+demId;
		var target = "dem"+instanceId;
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url; 
	}
	
	function setKhOpenFormPage(instanceId,formId,demId){
		var url = 'openFormPage.action?formid='+formId+'&instanceId='+instanceId+'&demId='+demId;
		var target = "dem"+instanceId;
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url; 
	}
	
	function setKpState(instanceid,demId,dataid){
		if(confirm("确认状态调整为已开票吗？调整后，状态将不能修改。")){
			var pageUrl = "zqb_sqkp_setState.action";
			$.post(pageUrl,{kpinstanceid:instanceid,kpdataId:dataid,kpid:demId},function(data){ 
		       			if(data=='success'){
		       				window.location.reload();
		       			}else{
		       				alert("状态更新失败。");
		       			} 
		     }); 
		}
	}
	
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	$(function(){
		//分页
		$('#pp').pagination({
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
		
		//查询
	    $("#search").click(function(){
	    	$("#ifrmMain").submit();
	    });
	    
	    $(document).bind('keyup', function(event) {
			if (event.keyCode == "13") {
				//回车执行查询
				$("#ifrmMain").submit();
			}
		});
    });

	function removekp(instanceid){
		if(confirm("确定执行删除操作吗?")) {
			var pageUrl = "zqb_del_index.action";
			$.post(pageUrl,{khid:instanceid},function(data){ 
		       			if(data=='success'){
		       				alert("删除成功。");
		       				window.location.reload();
		       			}else{
		       				alert("删除失败。");
		       			} 
		     }); 
		}
	}
	function showNotice(ggid){
		var pageUrl = "zqb_upd_index.action?khid="+ggid;	
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}	
	function uploadItem(){
		var customername = $("#customername").val();
	    var customerno = $("#customerno").val();
	    var state = $("#state").val();
		var seachUrl =encodeURI("dggdToExcl.action?customername="+customername+"&customerno="+customerno+"&state="+state);
		window.location.href = seachUrl;
	}
	function downloadTmp(){
		var pageUrl = "dggdDr.action";
		art.dialog.open(pageUrl,{
			id:'ExcelImpDialog',
			title:"数据导入",
			lock:true,
			background: '#999', // zxksDr
		    opacity: 0.87,	
		    width:500,
		    height:300,
		    close:function(){
		    	if(location!=null){
		    		location.reload();
		    	}
		    }
		 });
	}
	function uploadXxzcMb(){
		 var seachUrl ="dggdMbupload.action";
		window.location.href = seachUrl;
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
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
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
	<div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			<a href="javascript:uploadXxzcMb();" class="easyui-linkbutton" plain="true" iconCls="icon-add">下载模板</a>
			<a href="javascript:downloadTmp();" class="easyui-linkbutton" plain="true" iconCls="icon-add">导入</a>
			<a href="javascript:uploadItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">导出</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			
		</div>
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px;text-align:center;">
				<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										
		<td class="searchtitle" style="text-align: center;" width="35%" >项目名称 &nbsp;&nbsp;<input type='text' class = '{maxlength:64,required:false,string:true}'   name='customername' id='customername'  value='<s:property value="customername"/>' >
		
	
		<td class="searchtitle"  width="35%" style="text-align: center;">归档人员 &nbsp;&nbsp;<input type='text' class = '{maxlength:64,required:false,string:true}'  style="width:100px" name='customerno' id='customerno' value='<s:property value="customerno"/>' ></td> 
		
		<td class="searchtitle"  width="30%" style="text-align: center;">归档状态 &nbsp;&nbsp;<s:if test='state=="未归档"'>
				<select name='state' id='state' style="width:100px">
					<option value=''>-空-</option>
					<option value='已归档' >已归档</option>
					<option value='延期归档' >延期归档</option>
					<option value='未归档' selected >未归档</option>
				</select>
			</s:if>
			<s:elseif test='state=="已归档"'>
				<select name='state' id='state' style="width:100px" >
					<option value=''>-空-</option>
					<option value='已归档' selected>已归档</option>
					<option value='延期归档' >延时归档</option>
					<option value='未归档' >未归档</option>
				</select>
			</s:elseif>
			<s:elseif test='state=="延时归档"'>
				<select name='state' id='state' style="width:100px" >
					<option value=''>-空-</option>
					<option value='已归档' >已归档</option>
					<option value='延期归档' selected>延期归档</option>
					<option value='未归档' >未归档</option>
				</select>
			</s:elseif>
			<s:else>
				<select name='state' id='state' style="width:100px">
					<option value=''>-空-</option>
					<option value='已归档' >已归档</option>
					<option value='延期归档' >延期归档</option>
					<option value='未归档' >未归档</option>
				</select>
			</s:else></td>
	
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
		</form>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td align="center" style="width:2%;">序号</td>
					<td style="width:15%;">项目名称</td>
					<td style="width:15%;">归档人员</td>
					<td style="width:15%;">归档状态</td>
					<td style="width:10%;">归档位置</td>
					<td style="width:5%;">归档时间</td>
					
					<td  align="center" style="width:5%;">操作</td>
				</tr>
				<s:iterator value="invoiceKpList" status="status">
					<tr class="cell">
						<td style="text-align: center;"><s:property value="rm"/></td>
						<td>
							<a href="javascript:void(0)"  onclick="showNotice('<s:property value='id'/>');" >
								<s:property value="customername"/>
							</a>
						</td>
						<td><s:property value="customerno" /></td>
							<td><s:property value="sqrq" /></td>
						
						<td><s:property value="nsrlx" /></td>
					<td><s:property value="state" /></td>
			
						
						<td  aria-describedby="iform_grid__OPERATE" title="操作" style="text-align: center;" role="gridcell">
						<s:if test='roid==5'>
							<a href="javascript:void(0)"  onclick="removekp(<s:property value='id'/>)">删除 </a>
						</s:if>
						<s:else>
							暂无
						</s:else>
							
						</td>
					</tr>
				</s:iterator>
			</table>
				<div region="south" style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"	border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">
			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;">
				</div>
			</s:else>
		</div>
	</div>
			<s:hidden name="kpformid" id="kpformid"></s:hidden>
			<s:hidden name="kpid" id="kpid"></s:hidden>
			<form action="zqb_sqkp_index.action" method="post" name="frmMain" id="frmMain">
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="customername" id="customername"></s:hidden>
				<s:hidden name="customerno" id="customerno"></s:hidden>
				<s:hidden name="state" id="state"></s:hidden>
			</form>
		
			<div id='prowed_ifrom_grid'></div>
		</div>
		
	</div>
	
</body>
</html>

