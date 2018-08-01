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
    $(function(){
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
	//新增流程
    function addItem(){
		var thxmlc=$("#thxmlc").val();
		var pageUrl = "processRuntimeStartInstance.action?actDefId="+thxmlc;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		/* art.dialog.open(pageUrl,{
			id:'Category_show',
			cover:true,
			title:'新增日常业务呈报',
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
		        window.location.reload();
		     }
			
		}); */
		}
		//根据条件查询内容
		function selectthxm(){
			var xmmc = $("#XMMC").val();
			var startdate = $("#STARTDATE").val();
			var enddate = $("#ENDDATE").val();
			var gjjd = $("#gjjd").find("option:selected").val();
			var scbk = $("#scbk").find("option:selected").val();
			var seachUrl = encodeURI("openthxm.action?xmmc="+xmmc+"&startdate=" + startdate+"&enddate=" + enddate+"&gjjd=" + gjjd+"&scbk=" + scbk);
			window.location.href = seachUrl;
		}
		//导出当前显示数据
		function toExcl(){
			var xmmc = $("#XMMC").val();
			var startdate = $("#STARTDATE").val();
			var enddate = $("#ENDDATE").val();
			var gjjd = $("#gjjd").find("option:selected").val();
			var scbk = $("#scbk").find("option:selected").val();
			var seachUrl =encodeURI("exportexcl.action?xmmc="+xmmc+"&startdate=" + startdate+"&enddate=" + enddate+"&gjjd=" + gjjd+"&scbk=" + scbk);
			window.location.href = seachUrl;
		}
	//显示已提交的流程页面
    function showInfo(lcbh,steptid,lcinstanceId,taskik){
			var pageUrl = "loadProcessFormPage.action?actDefId="+lcbh+"&actStepDefId="+steptid+"&instanceId="+lcinstanceId+"&excutionId="+lcinstanceId+"&taskId="+taskik;
			art.dialog.open(pageUrl,{
						id:'projectItem',
						cover:true, 
						title:'',
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
                           window.location.reload();
                           }
					});
    
			// parent.openWin(title,height,width,pageurl,null,dialogId);
		}				//processInstanceMornitor
		//查看流程流转信息
	    function readLc(lcbh,lcbs,yxid,rwid,prcid,stepid){
	    	var lcbsArr = lcbs.split(".");
	    	
			var pageUrl = "processInstanceMornitor.action?actDefId="+lcbh
			+"&actStepDefId="+stepid+"&prcDefId="+prcid+"&taskId="+rwid+"&instanceId="+lcbsArr[0]+"&excutionId="+lcbsArr[0];

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

			function closeItem(instanceid){
			if(confirm("确定执行删除操作吗?")) {
				var pageUrl = "removeexport.action";
				$.post(pageUrl,{instanceid:instanceid},function(data){ 
			       			if(data=='success'){
			       				window.location.reload();
			       			}else{
			       				alert("删除失败");;
			       			} 
			     }); 
			}
			
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
					font-size:12px;
					padding:3px 4px;
					white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;
					text-decoration:none;
					text-align:left;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
				}
		.selectCheck{
			border:0px;
			text-align:right;
		}
		
	</style>	
	<style>  
  .ellipsis_div{  
    overflow:hidden;  
    text-overflow:ellipsis;  
    white-space:nowrap;  
    
  }   
  </style>  
  </head> 
    <body >
   <div region="north" style="height:50px;font-size:20px;font-family:黑体;padding:5px;border-bottom:1px solid #efefef;" border="false" >
   	
	  <div class="tools_nav">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td> 
					<label id="operationButton">
						<a id="addAnnouncement" href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增融资需求流程</a>
					</label>
					<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
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
		<td class="searchtitle" style="padding-left:10px;">项目名称</td> 
		<td class= "searchdata"><input type='text' class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='XMMC' id='XMMC' value='<s:property value="xmmc"/>' ></td> 
		<td class="searchtitle">流程发起日期 </td> 
		<td id="title_STARTDATE" class="searchdata">
			<input type='text' onfocus="var ENDDATE=$dp.$('ENDDATE');WdatePicker({onpicked:function(){ENDDATE.focus();},maxDate:'#F{$dp.$D(\'ENDDATE\')}'})"  style="width:100px" name='STARTDATE' id='STARTDATE'  value='<s:property value="startdate"/>' >
			&nbsp;&nbsp;到&nbsp;&nbsp;<input type='text' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'STARTDATE\')}'})" onchange="checkRQ()" style="width:100px" name='ENDDATE' id='ENDDATE'  value='<s:property value="enddate"/>' >
		</td>
		<td class="searchtitle">项目跟进阶段</td>
		<td class="searchdata">
			<s:if test='gjjd=="已签融资服务协议"'>
				<select name='gjjd' id='gjjd'>
					<option value=''>-空-</option>
					<option value='已签融资服务协议' selected>已签融资服务协议</option>
					<option value='未签融资服务协议'>未签融资服务协议</option>
				</select>
			</s:if>
			<s:elseif test='gjjd=="未签融资服务协议"'>
				<select name='gjjd' id='gjjd'>
					<option value=''>-空-</option>
					<option value='已签融资服务协议' >已签融资服务协议</option>
					<option value='未签融资服务协议' selected>未签融资服务协议</option>
				</select>
			</s:elseif>
			<s:else>
				<select name='gjjd' id='gjjd'>
					<option value=''>-空-</option>
					<option value='已签融资服务协议'>已签融资服务协议</option>
					<option value='未签融资服务协议'>未签融资服务协议</option>
				</select>
			</s:else>
		</td>
		<td class="searchResult">项目所处板块</td>
		<td class="searchdata">
			<s:if test='scbk=="全国股转系统项目"'>
				<select name='scbk' id='scbk'>
				<option value=''>-空-</option>
				<option value='全国股转系统项目' selected>全国股转系统项目</option>
				<option value='PRE-IPO项目'>PRE-IPO项目</option>
				<option value='其他'>其他</option>
			</select>
			</s:if>
			<s:elseif test='scbk=="PRE-IPO项目"'>
				<select name='scbk' id='scbk'>
				<option value=''>-空-</option>
				<option value='全国股转系统项目'>全国股转系统项目</option>
				<option value='PRE-IPO项目' selected>PRE-IPO项目</option>
				<option value='其他'>其他</option>
			</select>
			</s:elseif>
			<s:elseif test='scbk=="其他"'>
				<select name='scbk' id='scbk'>
				<option value=''>-空-</option>
				<option value='全国股转系统项目'>全国股转系统项目</option>
				<option value='PRE-IPO项目'>PRE-IPO项目</option>
				<option value='其他' selected>其他</option>
			</select>
			</s:elseif>
			<s:else>
				<select name='scbk' id='scbk'>
				<option value=''>-空-</option>
				<option value='全国股转系统项目'>全国股转系统项目</option>
				<option value='PRE-IPO项目'>PRE-IPO项目</option>
				<option value='其他'>其他</option>
			</select>
			</s:else>
			
		</td>
	</tr>
	<tr>
		
		
	</tr>
</table>
<td> 
<td valign='bottom' style='padding-bottom:5px;'> <a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);" onclick="selectthxm()">查询</a></td>
<td valign='bottom' style='padding-bottom:5px;'> <a id="search" class="easyui-linkbutton"  href="javascript:void(0);" onclick="toExcl()">导出当前数据</a></td>
<tr> 
</table> 
</div>
	</div>
	</form>
	<table width="100%" WIDTH="100%" style="border:1px solid #efefef" cellpadding="0" cellspacing="0"> 
      		<tr  class="header">
      			<td width="6%">流程发起日期</td>
      			<td width="12%">资本市场部经办人</td>
      			<td width="11%">项目名称</td> 
      			<td width="11%">跟进阶段</td>
      			<td width="11%">所处板块</td>
      			<td width="19%">融资需求描述</td>
      			<td width="19%">项目综合评价</td>
      			<td width="5%">项目状态</td>
      			<td width="5%">&nbsp;&nbsp;操作&nbsp;&nbsp;</td>
      		</tr>
      		<s:iterator value="list">
      				<tr  class="cell">
      					<td width="6%"><s:property value="FQRQ"/></td>
      					<td width="12%"><s:property value="ZBSCBJBR" /></td>
      					<td width="11%" ><a href="javascript:showInfo('<s:property value="LCBH"/>','<s:property value="STEPTID"/>','<s:property value="LCINSTANCEID"/>','<s:property value="TASKID"/>')"><s:property value="XMMC"/></a>  </td>
      					 <td width="11%"><s:property value="XMGJJD" /></td> 
      					 <td width="11%"><s:property value="XMSCBK" /></td> 
      					 <td width="19%" ><div title=" <s:property value="RZXQMS" />" class="ellipsis_div" style="width:250px;background-color:#FFFFFF;"><s:property value="RZXQMS" /></div></td> 
      					  <td width="19%" ><div title=" <s:property value="extend1" />" class="ellipsis_div" style="width:250px;background-color:#FFFFFF;"> <s:property value="extend1" /></div></td>
      					 <td width="5%"><a href="javascript:readLc('<s:property value="LCBH"/>','<s:property value="YXID"/>','<s:property value="YXID"/>','<s:property value="TASKID"/>','<s:property value="PRCID"/>','<s:property value="STEPTID"/>')"><s:property value="SPZT"/></a>  </td> 
      					<%-- <td width="5%"><s:if test="isFBR==1">&nbsp;&nbsp;暂无&nbsp;&nbsp;</s:if><s:else><a href="javascript:closeItem(<s:property value="INSTANCEID"/>)">&nbsp;&nbsp;删除&nbsp;&nbsp;</a></s:else></td> --%>
      					<td width="5%"><a href="javascript:closeItem(<s:property value="INSTANCEID"/>)">&nbsp;&nbsp;删除&nbsp;&nbsp;</a></td>
      				</tr> 
      		</s:iterator>
      		</table>
      		
       <s:form id="editForm" name="editForm" action="openthxm.action">
			<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
			<s:hidden name="xmmc" id="xmmc"></s:hidden>
			<s:hidden name="startdate" id="startdate"></s:hidden>
			<s:hidden name="enddate" id="enddate"></s:hidden>
			<s:hidden name="gjjd" id="gjjd"></s:hidden>
			<s:hidden name="scbk" id="scbk"></s:hidden>

   </s:form>
  </div>
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
	<div style = "padding:5px">
			<s:if test="totalNum==0">
			
			</s:if><s:else>
			<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
			</div>
			<s:hidden name="thxmlc" id="thxmlc"></s:hidden>
	</div>
  </body>
</html>