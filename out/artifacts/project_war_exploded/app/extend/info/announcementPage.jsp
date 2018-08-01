<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<title>公告呈报</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/> 
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/common.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script> 
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"></script>
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
      //查询事件
       $("#search").click(function(){
   	   var valid = mainFormValidator.form(); //执行校验操作
   	   if(!valid){
   	   return false;
   	   }
        var noticeName = $("#NOTICENAME").val();
        var startdate = $("#STARTDATE").val();
        var enddate = $("#ENDDATE").val();
        var khbh=$("#khbh").val();
        var zqdm=$("#ZQDM").val();
        var zqjc=$("#ZQJC").val();
        var noticetype=$("#NOTICETYPE").val();
        var bzlx=$("#BZLX").val();
        var spzt=$("#SPZT").val();
        var dq=$("#DQ").val();
        var companyno=$("#COMPANYNO").val();
      	var pageUrl="isCustomer.action";
      	if(noticeName==""&&startdate==""&&enddate==""&&zqdm==""&&zqjc==""&&noticetype==""&&spzt==""){
      		khbh="";
      	}
     	$.post(pageUrl,{zqjc:zqjc,zqdm:zqdm},function(data){
	         if(data=='success'){
	        	 var seachUrl = encodeURI("zqb_nnouncement_search.action?spzt="+spzt+"&khbh="+khbh+"&noticename="+noticeName+"&startdate="+startdate+"&enddate="+enddate+"&zqdm="+zqdm+"&zqjc="+zqjc+"&noticetype="+noticetype+"&bzlx="+bzlx+"&dq="+dq+"&companyno="+companyno);
	             window.location.href = seachUrl;
	         }else{
	         	 alert("公司名称有误或不是你所督导的客户");         	 
	         }
	   });
      });
  	});
		function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
		// 全选、全清功能
		function selectAll(){
			if($("input[name='chk_list']").attr("checked")){
				$("input[name='chk_list']").attr("checked",true);
			}else{
				$("input[name='chk_list']").attr("checked",false);
			}
		}
		// 编辑模板
		function editAnnouncement(lcbh,lcbs,yxid,rwid){
			var pageUrl;
			 pageUrl = "loadProcessFormPage.action?actDefId="+lcbh+"&instanceId="+lcbs+"&excutionId="+yxid+"&taskId="+rwid;
			 art.dialog.open(pageUrl,{
				id:'Category_show',
				cover:true, 
				title:'编辑公告',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,			
				close:function(){
					 location.reload();
				}
			});
		}

		// 编辑模板
		function editGg(instanceid){
			
		var pageUrl ;
		pageUrl = "openFormPage.action?formid=154&demId=69&instanceId="+instanceid.split(".")[0];
		//window.top.showUrl(pageUrl,'关联会议','iwork_img/desk/msg_b.png','mainFrame');
	/* 	art.dialog.open(pageUrl,{
			id:'Category_show',
			cover:true, 
			title:'关联会议',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1000,
			cache:false,
			lock: true,
			height:580, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,				
			close:function(){
				 location.reload();
			}
		}); */
		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = pageUrl;	
		}
		// 查看模板
		function readAnnouncement(id,instanceid){
			var zqdmxs =$("#zqdmxs").val();
			var zqjcxs =$("#zqjcxs").val();
			var pageUrl = "loadPage.action?instanceId="+instanceid+"&zqjcxs="+encodeURI(zqjcxs)+"&zqdmxs="+zqdmxs;
			var target = "_blank";
    		var win_width = window.screen.width;
    		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
    		page.location = pageUrl;
			/* art.dialog.open(pageUrl,{
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
		}
		
		// 删除公告
		function removeAnnouncement(id,instanceid){
			if(confirm("确定要删除公告吗？")){
				var pageUrl="zqb_announcement_delete.action";
				$.post(pageUrl,{id:id,instanceid:instanceid},function(data){
			         if(data=='success'){
			             window.location.reload();
			             //window.top.frames["deskframe"].location.reload();
			         }else{
			         	 alert("删除失败!");
			         }
			    });
			}
		}
		// 新增公告
		function addAnnouncement(){
			var khbh =$("#khbh").val() ;
			var khmc =$("#khmc").val() ;
			var zqdmxs =$("#zqdmxs").val()==""?$("#zqdm").val():$("#zqdmxs").val();
			var zqjcxs =$("#zqjcxs").val()==""?$("#zqjc").val():$("#zqjcxs").val();
			if(khbh==null || khbh==''||khbh=="undefined"||khbh=="0"||zqdmxs==null||zqdmxs==''||zqdmxs=="undefined"||zqjcxs==null||zqjcxs==''||zqjcxs=="undefined"){
			    $.messager.alert('提示信息','请选择挂牌公司!','info');  
				return;
			}
			if(khmc==null || khmc==''||khmc=="undefined"||khmc=="0"||zqdmxs==null||zqdmxs==''||zqdmxs=="undefined"||zqjcxs==null||zqjcxs==''||zqjcxs=="undefined"){
			    $.messager.alert('提示信息','请选择挂牌公司!','info');  
				return;
			}
			var roleid= $("#roleid").val();
			var ggsplc=$("#ggsplc").val();
			var dmggsplc=$("#dmggsplc").val();
			var pageUrl ;
			if(roleid=="3"){
			   pageUrl = encodeURI("processRuntimeStartInstance.action?actDefId="+dmggsplc+"&KHBH="+khbh+"&KHMC="+khmc+"&ZQJCXS="+zqjcxs+"&ZQDMXS="+zqdmxs);
			}else{
			    pageUrl = encodeURI("processRuntimeStartInstance.action?actDefId="+ggsplc+"&KHBH="+khbh+"&KHMC="+khmc+"&ZQJCXS="+zqjcxs+"&ZQDMXS="+zqdmxs);//CXDDYFQLC:1:73608
			}
			var target = "_blank";
	   		var win_width = window.screen.width;
	   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	   		page.location = pageUrl;			
		}
		
		function readLc(lcbh,lcbs,yxid,rwid,prcid,stepid){
			var pageUrl = "processInstanceMornitor.action?actDefId="+lcbh
			+"&actStepDefId="+stepid+"&prcDefId="+prcid+"&taskId="+rwid+"&instanceId="+lcbs+"&excutionId="+yxid;
			art.dialog.open(pageUrl,{ 
				id:'Category_show',
				cover:true, 
				title:'流程跟踪',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,						
				close:function(){
					 location.reload();
				}
			});
		}
		function readLsbb(noticetext){
			var pageUrl = "readLsbb.action?noticetext="+encodeURI(noticetext);
			art.dialog.open(pageUrl,{
						id:'Category_show',
						cover:true, 
						title:'历史版本',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:580, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false,						
						close:function(){
							 location.reload();
						}
					});
		}
		// 下载模板
		function downloadTemplate(id,fjid){
			var url = 'uploadifyDownload.action?fileUUID='+fjid;
			window.location.href = url;
			
		}
		
		function getPageUrl(id,instanceid){
			var pageUrl=null;
			$.ajaxSetup({
		        async: false
		    });
			$.ajax({
		        url : 'zqb_announcement_getsmjinsid.action',
		        async : false,
		        type : "POST",
		        data: {
		        	ggid:id
	            },
		        dataType : "json",
		        success : function(data) {
		        	if(data!=null){
			        	 pageUrl ="openFormPage.action?formid=148&demId=64&instanceId="+data;
			         }else{
						pageUrl ="createFormInstance.action?formid=148&demId=64&GGID="+id+"&GGINS="+instanceid;
			         }
		        }  
		    });
			return pageUrl;
		}
		
		function addSmj(id,instanceid){
			var pageUrl = getPageUrl(id,instanceid);
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;
		}
		function readSmj(instanceid){
		   var pageUrl ="loadVisitPage.action?formid=148&demId=64&instanceId="+instanceid;
		   art.dialog.open(pageUrl,{
				id:'Category_show',
				cover:true, 
				title:'查看确认扫描件',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,						
				close:function(){
					 location.reload();
				}
			});
		}
		function editSmj(instanceid){
		   var pageUrl ="openFormPage.action?formid=148&demId=64&instanceId="+instanceid;
		   var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;
		}
		function getContentUrl(id,instanceid){
			var pageUrl=null;
			$.ajaxSetup({
		        async: false
		    });
			$.ajax({
		        url : 'zqb_announcement_getsmjinsid.action',
		        async : false,
		        type : "POST",
		        data: {
		        	ggid:id
	            },
		        dataType : "json",
		        success : function(data) {
		        	if(data!=null){
			        	 pageUrl ="openFormPage.action?formid=148&demId=64&instanceId="+data+"&isHFRandHFNRdiaplsy=1";
			         }else{
						pageUrl ="createFormInstance.action?formid=148&demId=64&GGID="+id+"&GGINS="+instanceid+"&isHFRandHFNRdiaplsy=1";
			         }
		        }  
		    });
			return pageUrl;
		}
		function editContent(instanceid){
			var pageUrl ="openFormPage.action?formid=148&demId=64&instanceId="+instanceid+"&isHFRandHFNRdiaplsy=1";
			art.dialog.open(pageUrl,{
				id:'iformPjrz',
				cover:true, 
				title:'意见',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:600, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,						
				close:function(){
					 location.reload();
				}
			});
			/* var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl; */
		}
		function addContent(id,instanceid){
			var pageUrl = getContentUrl(id,instanceid);
			art.dialog.open(pageUrl,{
				id:'iformPjrz',
				cover:true, 
				title:'意见',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:600, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,						
				close:function(){
					 location.reload();
				}
			});
			/* var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl; */
		}
		function impExcel(){
			//导入excel
				var pageUrl = "zqb_announcement_impPage.action?formid=133&demId=60";
				var dg =art.dialog.open(pageUrl,{
					id:'ExcelImpDialog',  
					cover:true,
					title:"数据导入",
					width:500,
					height:350,
					loadingText:'正在加载中,请稍后...', 
					bgcolor:'#999',
					rang:true, 
					lock: true,
					iconTitle:false,
					extendDrag:true, 
					autoSize:false,
					resize:false,					
				});
				dg.ShowDialog();
			}
		
		function getUrl(ggid){
			var url = null;
			$.ajaxSetup({
		        async: false
		    });
			$.ajax({
		        url : 'zqb_announcement_getgzid.action',
		        async : false,
		        type : "POST",
		        data: {
		        	ggid:ggid
	            },
		        dataType : "json",
		        success : function(data) {
		        	if(data!=null){
			        	url = "zqb_nnouncement_dxyjtzr.action?gzyjid="+data;
			         }else{
						var gzxtyjdemid =$("#gzxtyjdemid").val();
						var gzxtyjformid =$("#gzxtyjformid").val();
						var khbh =$("#khbh").val();
						url ="createFormInstance.action?formid="+gzxtyjformid+"&demId="+gzxtyjdemid+"&GGID="+ggid+"&KHBH="+khbh;
			         }
		        }  
		    });
			return url;
		}
		function dealGzyj(ggid){
			var url = getUrl(ggid);
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = url;
		}
		function dealGzyjHF(gzyjid){
			var url = "zqb_nnouncement_dxyjtzr.action?gzyjid="+gzyjid;
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = url;
		}
		function exoNotice(){
			var noticeName = $("#NOTICENAME").val();
	        var startdate = $("#STARTDATE").val();
	        var enddate = $("#ENDDATE").val();
	        var khbh=$("#khbh").val();
	        var zqdm=$("#ZQDM").val();
	        var zqjc=$("#ZQJC").val();
	        var noticetype=$("#NOTICETYPE").val();
	        var bzlx=$("#BZLX").val();
	        var spzt=$("#SPZT").val();
	        var dq=$("#DQ").val();
	        var companyno=$("#COMPANYNO").val();
	      	var pageUrl="isCustomer.action";
	      	if(noticeName==""&&startdate==""&&enddate==""&&zqdm==""&&zqjc==""&&noticetype==""&&spzt==""){
	      		khbh="";
	      	}
			var pageUrl = encodeURI("zqb_nnouncement_expnotice.action?spzt="+spzt+"&khbh="+khbh+"&noticename="+noticeName+"&startdate="+startdate+"&enddate="+enddate+"&zqdm="+zqdm+"&zqjc="+zqjc+"&noticetype="+noticetype+"&bzlx="+bzlx+"&dq="+dq+"&companyno="+companyno);
			window.location.href = pageUrl; 
		}
	</script>
	<style type="text/css">
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
					/* white-space:nowrap;
					word-wrap:normal;
					overflow:hidden; */
					text-align:left;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
					text-overflow:ellipsis; 
				}
		.selectCheck{
			border:0px;
			text-align:right;
		}
	</style>
</head> 
<body class="easyui-layout">
<div region="north" style="height:50px;font-size:20px;font-family:黑体;padding:5px;border-bottom:1px solid #efefef;" border="false" >
  <div class="tools_nav">
	<table width="100%"  border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td> 
				<label id="operationButton">
					<a id="addAnnouncement" href="javascript:addAnnouncement();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增公告</a>
				</label>
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				<a href="javascript:exoNotice();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">导出</a>
				<%-- <a iconcls="icon-excel-imp" plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:impExcel();"><span class="l-btn-left"><span class="l-btn-text icon-excel-imp" style="padding-left: 20px;">导入</span></span></a> --%>
			</td>
		</tr>
	</table>
		 </div>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
	<form action="zqb_nnouncement_search.action" method="post" name="frmMain" id="frmMain" >
	<div style="padding:5px">
		<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
<tr> 
  <td style='padding-top:10px;padding-bottom:10px;'> 
<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
	<tr> 
		<td class="searchtitle" style="padding-left:10px;">公司简称</td> 
		<td class= "searchdata"><input type='text' <s:if test="roleid==3">readonly="readonly"</s:if> class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='ZQJC' id='ZQJC' value='' ></td> 
		<td class="searchtitle">公司代码 </td> 
		<td class= "searchdata"><input type='text' <s:if test="roleid==3">readonly="readonly"</s:if> class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='ZQDM' id='ZQDM' value='' ></td> 
		<td class="searchtitle">公告时间 </td> 
		<td id="title_STARTDATE" class="searchdata">
			<input type='text' onfocus="var ENDDATE=$dp.$('ENDDATE');WdatePicker({onpicked:function(){ENDDATE.focus();},maxDate:'#F{$dp.$D(\'ENDDATE\')}'})"  style="width:100px" name='STARTDATE' id='STARTDATE'  value='' >
			&nbsp;&nbsp;到&nbsp;&nbsp;<input type='text' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'STARTDATE\')}'})" onchange="checkRQ()" style="width:80px" name='ENDDATE' id='ENDDATE'  value='' >
		</td>
	</tr>
	<tr>
		<td class="searchtitle" style="padding-left:10px;">公告名称 </td> 
		<td class= "searchdata"><input type='text' class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='NOTICENAME' id='NOTICENAME' value='' ></td>
		<td class="searchtitle">公告类型</td>
		<td class="searchdata">
			<select name='NOTICETYPE' id='NOTICETYPE'>
				<option value=''>-空-</option>
				<option value='临时报告'>临时报告</option>
				<option value='定期报告'>定期报告</option>
				<s:if test="roleid!=3">
				<option value='券商公告'>券商公告</option>
				<option value='风险提示性公告'>风险提示性公告</option>
				</s:if>
			</select>
		</td>
		<td class="searchResult">审批结果</td>
		<td class="searchdata">
			<select name='SPZT' id='SPZT'>
				<option value=''>-空-</option>
				<option value='未提交'>未提交</option>
				<option value='审批中'>审批中</option>
				<option value='审批通过'>审批通过</option>
				<option value='驳回'>驳回</option>
			</select>
			&nbsp;所属大区&nbsp;<input type='text' class = '{maxlength:10,required:false,string:true}'  style="width:80px" name='DQ' id='DQ' value='' ></td>
		</td>
	</tr>
	<tr> 
		<td class="searchtitle" style="padding-left:10px;">股东大会日期</td> 
		<td class= "searchdata"><input type='text' onfocus="WdatePicker()"  style="width:100px" name='COMPANYNO' id='COMPANYNO'  value='' ></td> 
		<td class="searchtitle"> </td> 
		<td class= "searchdata"></td> 
		<td class="searchtitle"> </td> 
		<td class="searchdata"></td>
	</tr>
</table>
<td> 
<td valign='bottom' style='padding-bottom:5px;'> <a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);" >查询</a></td>
<tr> 
</table> 
</div>

	</div>
		<span style="disabled:none">
			<input type="hidden" name="formid" value="88" id="formid"/> 
			<input type="hidden" name="demId" value="21" id="demId"/>
			<input type = "hidden" name="idlist" id="idlist" value='11'>
		 	<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
			<s:hidden name="totalNum" id="totalNum"></s:hidden>
			<s:hidden name="khbh" id="khbh"></s:hidden>
			<s:hidden name="khmc" id="khmc"></s:hidden>
			<s:hidden name="roleid" id="roleid"></s:hidden>
			<s:hidden name="noticename" id="noticename"></s:hidden>
			<s:hidden name="startdate" id="startdate"></s:hidden>
			<s:hidden name="enddate" id="enddate"></s:hidden>
			<s:hidden name="zqjc" id="zqjc"></s:hidden>
			<s:hidden name="bzlx" id="bzlx"></s:hidden>
			<s:hidden name="zqdm" id="zqdm"></s:hidden>
			<s:hidden name="zqjcxs" id="zqjcxs"></s:hidden>
			<s:hidden name="zqdmxs" id="zqdmxs"></s:hidden>
			<s:hidden name="noticetype" id="noticetype"></s:hidden>
			<s:hidden name="gzxtyjdemid" id="gzxtyjdemid"></s:hidden>
			<s:hidden name="gzxtyjformid" id="gzxtyjformid"></s:hidden>
			<s:hidden name="spzt" id="spzt"></s:hidden>
			<s:hidden name="dq" id="dq"></s:hidden>
			<s:hidden name="companyno" id="companyno"></s:hidden>
		</span>
	</form>
    
    	<table WIDTH="100%" style="border:1px solid #efefef;table-layout: fixed;">
			<TR  class="header">
				<!--<TD style="width:4%"><input type="checkbox" name="chk_list" onclick="selectAll()"></TD>
				 <TD style="width:30px">信披事项</TD> -->
				<TD style="width:5%">公告类型</TD>
				<TD style="width:5%">公告编号</TD>
				<TD style="width:5%">公司简称</TD>
				<TD style="width:30%">公告名称</TD>
				<TD style="width:5%">提交人</TD>
				<TD style="width:15%">会议名称</TD>
				<TD style="width:5%">公告日期</TD>
				<TD style="width:9%">股东大会日期</TD>
				<TD style="width:6%">审批结果</TD>
				<!-- <TD style="width:60px">历史版本</TD> -->
				<!-- <TD style="width:10px">公告得分</TD> -->
				<TD style="width:5%">备查文件</TD>
				<TD style="width:5%">已披露</TD>
				<TD style="width:15%">操作</TD>
			</TR>
			<s:iterator value="list"  status="status">
			<TR class="cell">
				<%--<TD><input type="checkbox" name="chk_list" value="<s:property value="ID"/>"></TD>
				 <TD title="<s:property value="BZLX"/>">
					<s:if test="BZLX.length()>6"><s:property value="BZLX.substring(0,6)"/>. . . </s:if>
					<s:else><s:property value="BZLX"/></s:else>
				</TD> --%>
				<TD style="white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;"> <s:property value="NOTICETYPE"/></TD>
				<TD style="white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;"><s:property value="NOTICENO"/></TD>
				<TD style="white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;"><s:property value="ZQJCXS"/></TD>
				<TD style="white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;" title="<s:property value="NOTICENAME"/>"><a href="javascript:readAnnouncement(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)"><s:property value="NOTICENAME"/></a></TD>
				<TD style="white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;"><s:property value="CREATENAME"/></TD>
				<TD style="white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;" title="<s:property value="MEETNAME"/>"><s:property value="MEETNAME"/></TD>
				<TD style="white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;"><s:property value="NOTICEDATE"/></TD>
				<TD><s:property value="COMPANYNO"/></TD>
				<TD><a href="javascript:readLc('<s:property value="LCBH"/>',
				<s:property value="LCBS"/>,'<s:property value="YXID"/>',
				'<s:property value="RWID"/>','<s:property value="PRCID"/>','<s:property value="STEPID"/>')"><s:property value="SPZT"/></a></TD>
			   <%--   <TD>
			     <s:if test="SPZT=='审批通过'">
			     <a href="javascript:readLsbb('<s:property value="NOTICEFILE"/>')">历史版本</a>
			     </s:if>
			     </TD>  --%>
			    <%-- <TD><s:property value="GGDF"/></TD> --%>
			    <TD>
			    <s:if test="SPZT=='审批通过'">
					<%-- <s:if test="roleid==3">
						<s:if test="ISSMJINS">
							<a href="javascript:editSmj('<s:property value="SMJINS"/>')"><s:if test="NUM>0">（<s:property value="NUM"/>）</s:if><s:else>上传</s:else></a>
						</s:if>
						<s:else>
							<a href="javascript:addSmj(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)">上传</a>
						</s:else>
					</s:if>
					<s:if test="roleid!=3">
						<a href="javascript:readSmj('<s:property value="SMJINS"/>')"><s:if test="NUM>0">（<s:property value="NUM"/>）</s:if><s:else>无</s:else></a>
					</s:if> --%>
					<s:if test="ISSMJINS">
						<a href="javascript:editSmj('<s:property value="SMJINS"/>')"><s:if test="NUM>0">（<s:property value="NUM"/>）</s:if><s:else>上传</s:else></a>
					</s:if>
					<s:else>
						<a href="javascript:addSmj(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)">上传</a>
					</s:else>
				</s:if>
			    </TD>
			    <td style="cursor:pointer;" title='<s:property value='GGNAME' escapeHtml="false"/>'><s:property value="ONUM"/></td>
				<TD >
				<a href="javascript:editGg('<s:property value="INSTANCEID"/>')">关联会议</a>
				<%-- |<a href="javascript:downloadTemplate(<s:property value="ID"/>,'<s:property value="NOTICETEXT"/>')">下载</a> --%>
				
				<s:if test="roleid!=3">
				|  <a href="javascript:removeAnnouncement('<s:property value="ID"/>',<s:property value="INSTANCEID"/>)">删除</a>
				</s:if>
				 <%-- <s:if test="ISSMJINS">
				  | <a href="javascript:readSmj('<s:property value="SMJINS"/>')">查看归档的扫描件</a>
				 </s:if>
				<s:if test="ISTJ">
				 <s:if test="ISSMJINS">
				 | <a href="javascript:editSmj('<s:property value="SMJINS"/>')">编辑归档的扫描件</a>
				 </s:if>
				 <s:else>
				 |  <a href="javascript:addSmj(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)">提交归档的扫描件</a>
				 </s:else>
				</s:if> --%>
				<s:if test="SPZT=='审批通过'">
					<s:if test="roleid==3">
						<s:if test="GZYJID!=null">
							| <a href="javascript:dealGzyjHF('<s:property value="GZYJID"/>')">处理股转意见</a>
						</s:if>
					</s:if>
					<s:if test="roleid!=3">
						<s:if test="GZYJID==null">
							| <a href="javascript:dealGzyj('<s:property value="ID"/>')">处理股转意见</a>
						</s:if>
						<s:else>
							| <a href="javascript:dealGzyjHF('<s:property value="GZYJID"/>')">处理股转意见</a>
						</s:else>
					</s:if>
				</s:if>
				<s:if test="orgroleid==5">
				 | <s:if test="ISSMJINS">
						<a href="javascript:editContent('<s:property value="SMJINS"/>')">提意见</a>
					</s:if>
					<s:else>
						<a href="javascript:addContent(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)">提意见</a>
					</s:else>
				</s:if>
				</TD>
				 
			</TR>
			</s:iterator>
		</table>
					
		
    </div>
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
	<div style = "padding:5px">
			<s:if test="totalNum==0">
			
			</s:if><s:else>
			<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
			</div>
	</div>
		<s:hidden name="ggsplc" id="ggsplc"></s:hidden>
		<s:hidden name="dmggsplc" id="dmggsplc"></s:hidden>
	<script type="text/javascript">
	$(function(){
		$("#NOTICENAME").val($("#noticename").val());
		$("#STARTDATE").val($("#startdate").val());
		$("#DQ").val($("#dq").val());
		$("#ENDDATE").val($("#enddate").val());
		$("#ZQJC").val($("#zqjcxs").val()==""||$("#zqjcxs").val()=="undefined"?$("#zqjc").val():$("#zqjcxs").val());
		$("#BZLX").val($("#bzlx").val());
		$("#ZQDM").val($("#zqdmxs").val()==""||$("#zqdmxs").val()=="undefined"?$("#zqdm").val():$("#zqdmxs").val());
		$("#NOTICETYPE").attr("value",$("#noticetype").val()); 
		$("#SPZT").attr("value",$("#spzt").val());
		$("#COMPANYNO").val($("#companyno").val());
	});
	</script>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{};'[\]]/im;
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