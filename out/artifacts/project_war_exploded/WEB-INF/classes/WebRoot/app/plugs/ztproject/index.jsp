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
    <link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css"/>
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.min.js"  charset="utf-8" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
    <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#frmMain").validate({});
		mainFormValidator.resetForm();
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
				return;
			}
	        var khbh = $("#customerno").val();
	        var type = $("#type").val();
	        var customername = $("#customername").val();
	        var zqjc = $("#ZQJC").val();
	        var zqdm = $("#ZQDM").val();
	        var noticename = "";
	        var startdate = $("#STARTDATE").val();
	        var enddate = $("#ENDDATE").val();
	        var spzt = $("#SPZT").val();
	     	var seachUrl = encodeURI("ztnbcb.action?type="+type+"&customername="+customername+"&zqjc="+zqjc+"&zqdm="+zqdm+"&noticename="+noticename+"&startdate="+startdate+"&enddate="+enddate+"&spzt="+spzt);
	        window.location.href = seachUrl;
	      });
	});
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#editForm").submit();
		return ;
	}
    function addItem(){
		var dmggsplc=$("#nbcblc").val();
		var pageUrl = "processRuntimeStartInstance.action?actDefId="+dmggsplc;
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
    function showInfo(lcbh,steptid,lcinstanceId,taskik){
		if(lcbh==""||steptid==""||lcinstanceId==""||taskik==""){
		
			alert("请在待办流程中查找所选内容！");
			
		} else{
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
		}
	}
	    function readLc(lcbh,lcbs,yxid,rwid,prcid,stepid){
			var pageUrl = "processInstanceMornitor.action?actDefId="+lcbh+"&actStepDefId="+stepid+"&prcDefId="+prcid+"&taskId="+rwid.split(".")[0]+"&instanceId="+lcbs.split(".")[0]+"&excutionId="+yxid;
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
				autoSize:false
			});
		}
		function showTalk(instanceid){
			var title = "";
			var pageUrl = "zqb_event_talk.action?instanceid="+instanceid;
			var dialogId = "meetRetime"; 
			art.dialog.open(pageUrl,{
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
				var pageUrl = "ztnbcbremove.action";
				$.post(pageUrl,{instanceid:instanceid},function(data){ 
			       			if(data=='success'){
			       				window.location.reload();
			       			}else{
			       				alert("删除失败，请从待办中删除");;
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
		.searchtitle{
			padding:5px;
			padding-right:15px;
			text-align:right;
			width:100px;
			
		}
		.searchdata{
			padding-left:5px;
		}
	</style>	
  </head> 
    <body >
    <div region="north" style="height:50px;font-size:20px;font-family:黑体;padding:5px;border-bottom:1px solid #efefef;" border="false" >
	  	<div class="tools_nav">
			<table width="100%"  border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td> 
						<s:if test="readonly!=true">
							<a href="javascript:addItem();" id="addEvent" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增内部呈报业务</a>
						</s:if>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
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
									<td class= "searchdata"><input type='text' <s:if test="orgRoleId==3">readonly="readonly"</s:if> class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='ZQJC' id='ZQJC' value='' ></td> 
									<td class="searchtitle">证券代码 </td> 
									<td class= "searchdata"><input type='text' <s:if test="orgRoleId==3">readonly="readonly"</s:if> class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='ZQDM' id='ZQDM' value='' ></td> 
									<td class="searchtitle">申请时间 </td> 
									<td id="title_STARTDATE" class="searchdata">
										<input type='text' onfocus="WdatePicker()"  style="width:100px" name='STARTDATE' id='STARTDATE'  value='' >
										&nbsp;&nbsp;到&nbsp;&nbsp;<input type='text' onfocus="WdatePicker()" onchange="checkRQ()" style="width:80px" name='ENDDATE' id='ENDDATE'  value='' >
									</td>
								</tr>
								<tr>
									<!-- <td class="searchtitle" style="padding-left:10px;">事项名称 </td> 
									<td class= "searchdata"><input type='text' class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='NOTICENAME' id='NOTICENAME' value='' ></td> -->
									<td class="searchtitle">审批结果</td>
									<td class="searchdata">
										<select name='SPZT' id='SPZT'>
											<option value=''>-空-</option>
											<option value='起草'>起草</option>
											<option value='审批中'>审批中</option>
											<option value='审批通过'>审批通过</option>
											<option value='驳回'>驳回</option>
										</select>
									</td>
									<td class="searchtitle"></td>
									<td class="searchdata"></td>
									<td class="searchtitle"></td>
									<td class="searchdata"></td>
								</tr>
							</table>
						</td> 
						<td valign='bottom' style='padding-bottom:5px;'> <a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);" >查询</a></td>
					</tr>
				</table> 
			</div>
		</div>
	</form>
		<table width="100%" WIDTH="100%" style="border:1px solid #efefef" cellpadding="0" cellspacing="0"> 
      		<tr  class="header">
      			<td width="10%">公司简称</td>
      			<td  width="10%">证券代码</td>
      			<td  width="20%">提交人</td> 
      			<td width="20%">呈报日期</td>
      			<td width="20%">审批状态</td>
      		<s:if test="orgRoleId!=3">
      			<td width="20%">操作</td>
      		</s:if>
      		</tr>
      		<s:iterator value="runList">
      				<tr  class="cell">
      					<td width="10%" ><a href="javascript:showInfo('<s:property value="NODE2SFKF"/>','<s:property value="NODE1SKP"/>','<s:property value="INSTANCEID"/>','<s:property value="NODE4SFKF"/>')""><s:property value="ZQJC"/></a>  
      					<s:if test="TALKNUM==0">
      					</s:if>
      					 </td>
      					 <td width="10%"><s:property value="NODE2"/></td>
      					<td width="20%"><s:property value="CREATENAME"/></td>
      					 <td width="20%"><s:property value="CREATEDATE" /></td> 
      					 <td width="20%"> <s:property value="SPZT"/> </td> 
      					 <%--<td width="20%"><s:property value="SBRQ" /></td> --%>
      					<%-- <td width="150px"><s:property value="GLGSMC"/></td> --%>
      					<s:if test="SPZT!='审批通过'">
	      					<td width="20%">      					
	      					<a href="javascript:closeItem(<s:property value="INSTANCEID"/>)">删除</a>
	      					</td>
	      				</s:if>
      				</tr> 
      		</s:iterator>
      	</table>
      		
		<s:form id="editForm" name="editForm" action="ztnbcb.action">
			<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
			<s:hidden name="customerno" id="customerno"></s:hidden>
			<s:hidden name="customername" id="customername"></s:hidden>
			
			<s:hidden name="zqjc" id="zqjc"></s:hidden>
			<s:hidden name="zqdm" id="zqdm"></s:hidden>
		
			<s:hidden name="startdate" id="startdate"></s:hidden>
			<s:hidden name="enddate" id="enddate"></s:hidden>
			<s:hidden name="spzt" id="spzt"></s:hidden>
		</s:form>
		<s:hidden name="nbcblc" id="nbcblc"></s:hidden>
	</div>
  
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
		<div style = "padding:5px">
			<s:if test="totalNum==0">
			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		$("#ZQJC").val($("#zqjc").val());
		$("#ZQDM").val($("#zqdm").val());
		$("#NOTICENAME").val($("#noticename").val());
		$("#STARTDATE").val($("#startdate").val());
		$("#ENDDATE").val($("#enddate").val());
		$("#SPZT").attr("value",$("#spzt").val());
	});
	</script> 
  </body>
</html>
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