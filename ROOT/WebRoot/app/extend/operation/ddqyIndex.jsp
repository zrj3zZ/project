<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" >
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
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">

	
	
	
	var mainFormValidator;
	$().ready(function() {
	mainFormValidator =  $("#frmMain").validate({
	 });
	 mainFormValidator.resetForm();
	});
	$(function() {
		$("#mainFrameTab").tabs({});
	});
	$(function(){
	 
        $('#pp').pagination({  
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
      //查询事件
       $("#search").click(function(){
   	   var valid = mainFormValidator.form(); //执行校验操作
   	   if(!valid){
   	   return false;
   	   }
     
         var khmc = $("#khmc").val();
			var kssj = $("#kssj").val();
			var jssj = $("#jssj").val();
			var seachUrl = encodeURI("ddListShow.action?khmc=" + khmc + "&kssj=" + kssj+ "&jssj=" + jssj);
			
			
			window.location.href = seachUrl;
   
      });
  	});
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	function addItem(){
		var formid = $("#formid").val();
		var demid = $("#demid").val();
		var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid+"&EXTEND4=2";
		//var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;		
	}	
		
	function addItemrz(zbid){
		var formid = $("#rzformid").val();
		var demid = $("#rzdemid").val();
		
		var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid+"&DEPARTMENTID="+zbid;
		//var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}	
	
	function updXcjc(tid,EXTEND4){
		
		//+"&BANKACCOUNT="+zqdmxs+"&CUSTOMERNAME="+zqjcxs
		var formid = $("#formid").val();
		var demid = $("#demid").val();
		var pageUrl = "openFormPage.action?formid="+formid+"&instanceId="+tid+"&demId="+demid+"&EXTEND4="+EXTEND4;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
			
	}
	
	function uploadItem(){
		var pageNumber=$("#pageNumber").val();
		var pageSize=$("#pageSize").val(pageSize);
		 var khmc = $("#khmc").val();
			var kssj = $("#kssj").val();
			var jssj = $("#jssj").val();
		 var seachUrl =encodeURI("ddToExcl.action?khmc=" + khmc + "&kssj=" + kssj+ "&jssj=" + jssj + "&pageNumber=" + pageNumber+ "&pageSize=" + pageSize);
		window.location.href = seachUrl;
		
	 }
	function updjy(zid){
		if(confirm("解约后将不能继续入账和编辑，确认要解约吗？")){
			var pageUrl = "toJy.action?rzid="+zid;
			var target = "_blank";
			var win_width = window.screen.width/2;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=500,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;
		}
	
	}
	function yjy(){
		alert("已解约，请点击客户名称查看详细信息！");
	}
	function uploadXxzcMb(){
		 var seachUrl ="ddqyMbupload.action";
		window.location.href = seachUrl;
	}
	function ddqyDr(){
		
		var pageUrl = "ddqyDr.action";
		art.dialog.open(pageUrl,{
			id:'ExcelImpDialog',
			title:"数据导入",
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:500,
		    height:300
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
		<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增</a>
		
	<!-- 	<a id="deljclv" href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-add">删除</a> -->
	<a plain="true" class="easyui-linkbutton l-btn l-btn-plain"  href="javascript:uploadXxzcMb();">
					<span class="l-btn-text icon-add" style="padding-left: 20px;">模板下载</span>
				</a>
		<a plain="true" class="easyui-linkbutton l-btn l-btn-plain"  href="javascript:ddqyDr();">
					<span class="l-btn-text icon-add" style="padding-left: 20px;">导入</span>
				</a>
		<a href="javascript:uploadItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">导出</a>
		<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<div region="center"
		style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form action="ddListShow.action" method="post" name="frmMain" id="frmMain" >
	<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
			<div style="padding:5px">
				<div
					style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">客户名称</td>
										<td class="searchdata"><input type='text'
											class='{string:true}'
											style="width:150px;" name='khmc' id='khmc' value='<s:property value="khmc"/>'
											form-type='al_textbox'></td>
										<td class="searchtitle">签约日期</td>
										<td>
											<input type='text' onfocus="WdatePicker()"  style="width:100px" name='kssj' id='kssj'  value='<s:property value="kssj"/>' >
			&nbsp;&nbsp;到&nbsp;&nbsp;<input type='text' onfocus="WdatePicker()"  style="width:100px" name='jssj' id='jssj'  value='<s:property value="jssj"/>' >
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
<span style="disabled:none">
		 	<s:hidden name="formid" id="formid"></s:hidden>
			<s:hidden name="demid" id="demid"></s:hidden>
			<s:hidden name="rzformid" id="rzformid"></s:hidden>
			<s:hidden name="rzdemid" id="rzdemid"></s:hidden>
		
		
</span>
	<table id='iform_grid' width="100%" style="border:1px solid #efefef">
			<TR  class="header" style="text-align: center;">
				<TD style="width:10%">客户名称</TD>
				<TD style="width:10%">签约日期</TD>
				<TD style="width:20%">付款节点</TD>
				<TD style="width:10%">跟进人</TD>
				<TD style="width:10%">已入账（万元）</TD>
				<TD style="width:10%">最后入账时间</TD>
				<TD style="width:10%">操作
				
				</TD>
			</TR>
			<s:iterator value="list"  status="status">
				<TR class="cell" >
				
				<TD style="text-align: center;" ><a href="javascript:updXcjc('<s:property value="insId"/>','1');" ><s:property value="zbkhmc"/></a></TD>
				<TD style="text-align: center;"><s:property value="qyrq"/></TD>
				<TD style="text-align: center;"><s:property value="fkjd"/></TD>
				<TD style="text-align: center;"><s:property value="gjrxm"/></TD>
				<TD style="text-align: center;"><s:property value="zrzje"/></TD>
				<TD style="text-align: center;"><s:property value="zhsj"/>  </TD>
				<TD style="text-align: center;">
					<s:if test="jy==1">
						<a href="#" onclick="yjy();" style="color: black" >编辑</a>
						&nbsp;&nbsp;&nbsp;&nbsp;  
						<a href="#" onclick="yjy();" style="color: black">入账</a>
						&nbsp;&nbsp;&nbsp;&nbsp;  
						<a href="#"  onclick="yjy();" style="color: black">解约</a>
					</s:if>
					<s:else>
						<a href="javascript:updXcjc('<s:property value="insId"/>','0');" style="color: blue" >编辑</a>
						&nbsp;&nbsp;&nbsp;&nbsp;  
						<a href="javascript:addItemrz('<s:property value="zbid"/>');" style="color: blue"  >入账</a>
						&nbsp;&nbsp;&nbsp;&nbsp;  
						<a href="javascript:updjy('<s:property value="zbid"/>');" style="color: blue"  >解约</a>
					</s:else>
				</TD>
			</TR>
			
			</s:iterator>
		</table>
			<%-- <form action="frmMainaa.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="tzbt" id="tzbt"></s:hidden>
				<s:hidden name="spzt" id="spzt"></s:hidden>
				<s:hidden name="tzlx" id="tzlx"></s:hidden>
			</form> --%>
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
		</div></div>
	
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