<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
 <base target="_self">
  
    <title>项目管理</title>
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
    function addItem(){
    	var customerno = $("#customerno").val();
    	var customername = $("#customername").val();
			var type=$("#type").val();
		if (customerno == ""||type=="root") {
				alert("未选择公司，无法添加信息披露事项");
				return ;
			}
			
			var formid = 116;
			var demId = 38; 
				var pageUrl =  'createFormInstance.action?formid='+formid+'&demId='+demId+'&KHBH='+customerno+"&KHMC="+encodeURI(customername); 
				var target = "_blank";
	    		var win_width = window.screen.width;
	    		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	    		page.location = pageUrl;
		}
	    function showInfo(instanceId){
		var formid = 116;
		var demId = 38; 
		var pageUrl = 'openFormPage.action?formid='+formid+'&demId='+demId+'&instanceId='+instanceId; 
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		}
		
		function showTalk(instanceid){
		 
			var title = "";
			 var pageurl = "zqb_event_talk.action?instanceid="+instanceid;
			 var dialogId = "meetRetime"; 
			 art.dialog.open(pageurl,{ 
						id:'projectItem',
						cover:true, 
						title:'留言板',
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
    
		}
			function closeItem(instanceid){
			if(confirm("确定执行删除操作吗?")) {
				var pageUrl = "zqb_workflow_remove.action";
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
  </head> 
    <body >
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
    <div>
    	 <s:if test="readonly!=true">
         	<a href="javascript:addItem();" id="addEvent" class="easyui-linkbutton" plain="true" iconCls="icon-add">申报重大事项</a>
         	</s:if>
         
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
	</div>		
	
<table width="100%" WIDTH="100%" style="border:1px solid #efefef" cellpadding="0" cellspacing="0"> 
      		<tr  class="header">
      			<td width="35%">事项名称</td>
      			<td  width="25%">事项类型</td> 
      			<td width="20%">申报日期</td>
      			<td width="20%">操作</td>
      		</tr>
      		<s:iterator value="runList">
      				<tr  class="cell">
      					<td width="35%" ><a href="javascript:showInfo(<s:property value="INSTANCEID"/>)"><s:property value="SXMC"/></a>  
      					<s:if test="TALKNUM==0">
      					<img src="../../../iwork_img/new0.gif" width="20" height="10" /> 
      					</s:if>
      					 </td>
      					<td width="25%"><s:property value="SXLX"/></td>
      					 <td width="20%"><s:property value="SBRQ" /></td> 
      					<%-- <td width="150px"><s:property value="GLGSMC"/></td> --%>
      					<td width="20%">      					
      					
      					<a href="javascript:showTalk('<s:property value="INSTANCEID"/>')">留言板
      					<s:if test="TALKNUM>0">
      					(<s:property value="TALKNUM"/>)
      					</s:if>
      					</a>&nbsp;&nbsp;
      					<a href="javascript:closeItem(<s:property value="INSTANCEID"/>)">删除</a>
      					</td>
      				</tr> 
      		</s:iterator>
      		</table>
      		
       <s:form id="editForm" name="editForm" action="zqb_event_index_content.action">
       <s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
  	 <s:hidden id="customerno" name="customerno"></s:hidden>
  	  <s:hidden id="customername" name="customername"></s:hidden>
  	<s:hidden id="year" name="year"></s:hidden>
  	<s:hidden id="grouptype" name="grouptype"></s:hidden>
  	<s:hidden id="meettype" name="meettype"></s:hidden>
  	<s:hidden id="status" name="status"></s:hidden>
  	<s:hidden id="type" name="type"></s:hidden>
   </s:form>
  </div>
  
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
	<div style = "padding:5px">
			<s:if test="totalNum==0">
			
			</s:if><s:else>
			<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
			</div>
	</div>
  </body>
</html>