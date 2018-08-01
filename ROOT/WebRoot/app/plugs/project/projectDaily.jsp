<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目日报</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<script type="text/javascript" src="iwork_js/commons.js"></script>
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
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
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
		//查询事件
	    $("#search").click(function(){
	    	var createuser=$("#CREATEUSER").val();
	    	var projectno = $("#PROJECTNO").val();
	        var startdate = $("#STARTDATE").val();
	        var enddate = $("#ENDDATE").val();
	      	var seachUrl = encodeURI("zqb_project_showDaily.action?startdate="+startdate+"&enddate="+enddate+"&XMBH="+projectno+"&createuser="+createuser);
	        window.location.href = seachUrl;
	      });
	});
	function submitMsg(pageNumber, pageSize) {
		$("#pageSize").val(pageSize);
		$("#pageNumber").val(pageNumber);
		$("#frmMain").submit();
		return;
	}
	function showVote(ggid){
		var pageUrl = "zqb_vote_goList.action?ggid="+ggid;
		art.dialog.open(pageUrl,{
			title:'自查反馈统计',
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
		});
	}
	
	function addItem(){
		var formid=$("#formid").val();
		var id=$("#xmrbid").val();
		var projectno=$("#projectNo").val();
		var projectname=$("#projectname").val();	
			//+"&PROJECTNO="+projectno
		var pageUrl =encodeURI( "createFormInstance.action?formid="+formid+"&demId="+id+"&projectNo="+projectno+"&projectname="+projectname);
		art.dialog.open(pageUrl,{
			title:'项目日志表单',
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
	
	function editDaily(instanceid){
		var formid=$("#formid").val();
		var id=$("#xmrbid").val();
		var projectno=$("#projectNo").val(); 
		var pageUrl = "openFormPage.action?formid="+formid+"&demId="+id+"&instanceId=" + instanceid+"&projectNo="+projectno;
		art.dialog.open(pageUrl,{
			title:'项目日志表单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:680, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});
	}
	function addContent(instanceid){
			if(instanceid=='0'||instanceid==0||instanceid==null||instanceid==''){
				art.dialog.tips("尚无流程信息,请先添加!",1);
				return;
			}
			var pageUrl = "createFormInstance.action?formid=148&demId=64&GGINS="+instanceid+"&isHFRandHFNRdiaplsy=1";
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
		}
	function removeDaily(instanceid) {
		if (confirm('确定删除此项目日志吗？')) {
			var pageUrl = "zqb_project_removeDaily.action?instanceid=" + instanceid ;
			$.post(pageUrl, {}, function(data) {
				if (data == 'success') {
					window.location.reload();
				} else {
					alert("删除项目日志失败");
				}
			});
		}
	}
	
	function checkDaily(instanceid){
		var pageUrl = "zqb_project_checkDaily.action?instanceid="+instanceid;
		art.dialog.open(pageUrl,{
			title:'查看项目日志',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:680, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){}
		});
	}
	
	function expPro(){
		var totle = <s:property value="totalNum"/>;
		if(totle==0){
			alert("无项目日志!");
			return;
		}
		var projectno=$("#projectNo").val();
      	
       
		var pageUrl = encodeURI("zqb_project_daily.action?projectNo="+projectno);
		$("#editForm").attr("action", pageUrl);
		$("#editForm").submit();
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
.projectname{
	cursor:pointer;
}
.projectname:hover {
	color:blue;
}
</style>
</head>
<body class="easyui-layout">
<div region="north" border="false" >
			<!--<div  class="tools_nav">
				<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
				<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				<a href="javascript:expPro();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a>
			</div> -->
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
	<form name='ifrmMain' id='ifrmMain'  method="post" >
	<div style="padding:5px">
		<div style="padding:0px;margin-bottom:5px;background:white;">
			<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
				<tr> 
					<td style='padding-top:10px;padding-bottom:10px;'> 
						<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
							<tr>
								<!-- <td class= "searchtitle">填报人</td>
								 <td class= "searchdata"><input type='text'  class = "{maxlength:128,required:false,string:true}"  style="width:100px" name='CREATEUSER' id='CREATEUSER' /></td>
								<td class= "searchtitle">项目名称</td>
								<td class= "searchdata"><input type='text'  class = "{maxlength:128,required:false,string:true}"  style="width:100px" name='PROJECTNO' id='PROJECTNO' /></td>
								<td class= "searchtitle">日期 </td> 
								<td class= "searchdata">
									<input type='text' onfocus="WdatePicker()" class = "{required:true}"  style="width:100px" name='STARTDATE' id='STARTDATE'  value='' >
									到
									<input type='text' onfocus="WdatePicker()" class = "{required:true}" onchange="checkRQ()" style="width:100px" name='ENDDATE' id='ENDDATE'  value='' >  -->
									<div  class="tools_nav">
				 <a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
				<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				 <a href="javascript:expPro();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a> 
			    </div> 
								</td> 
							</tr> 
						</table>
					</td> 
					 <!--  <td></td>
					<td valign='bottom' style='padding-bottom:5px;'> <a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);" >查询</a></td> -->
				</tr> 
			</table> 
		</div>
	</div>
	</form>
	<s:form id="editForm" name="editForm" theme="simple"></s:form>
		<div style="padding:5px">
			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">				
					
					<td style="width:5%;">填报人</td>
					<td style="width:5%;">日期</td>
					<td style="width:5%;">项目阶段</td>
					<td style="width:15%;">工作内容</td>
					<td style="width:25%;">进度说明</td>
					<td style="width:20%;">备注说明</td>
					<td style="width:15%;">操作</td>
				</tr>
				<s:iterator value="list" status="status">
					<tr class="cell">					
						<%-- <td class="projectname" onclick="checkDaily(<s:property value='INSTANCEID'/>)"><s:property value="PROJECTNO"/>
						
						</td> --%>
						
						<td><s:property value="CREATEUSER"/></td>
						<td><s:property value="PROJECTDATE"/></td>	
						<td><s:property value="TRACKING" /></td>
						<td title="<s:property value="PROGRESS"/>"><s:if test="PROGRESS.length() > 25"><s:property value="PROGRESS.substring(0,25)"/>...</s:if><s:else><s:property value="PROGRESS"/></s:else></td>
						<td title="<s:property value="USERNAME"/>"><s:if test="USERNAME.length() > 25"><s:property value="USERNAME.substring(0,25)"/>...</s:if><s:else><s:property value="USERNAME"/></s:else></td>
						<td title="<s:property value="TEL"/>"><s:if test="TEL.length() > 25"><s:property value="TEL.substring(0,25)"/>...</s:if><s:else><s:property value="TEL"/></s:else></td>
						<td>
							<s:if test="SHOWBUTTON">
							&nbsp;<a href="javascript:void(0)" onclick="editDaily(<s:property value='INSTANCEID'/>)">编辑</a>
							&nbsp;|
							&nbsp;<a href="javascript:void(0)" onclick="removeDaily(<s:property value='INSTANCEID'/>)">删除</a>
							</s:if>		
 							<s:if test="orgroleid==5">
							<s:if test="SHOWBUTTON">
							&nbsp;|
							</s:if>	
							<s:if test="YJINS==null||YJINS==''">
							<a href="javascript:void(0)" onclick="addContent(<s:property value='INSTANCEID'/>)">提意见</a>
							</s:if>
							<s:else>
								<a href="javascript:void(0)" onclick="editContent(<s:property value='YJINS'/>)">提意见</a>
							</s:else>
							
							
							</s:if> 
							
						</td> 
					</tr>
				</s:iterator>
			</table>
			<form action="zqb_project_showDaily.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="projectNo" id="projectNo"></s:hidden>
				<s:hidden name="startdate" id="startdate"></s:hidden>
				<s:hidden name="enddate" id="enddate"></s:hidden>
				<s:hidden name="createuser" id="createuser"></s:hidden>
				
			</form>
				<s:hidden name="formid" id="formid"></s:hidden>
				<s:hidden name="xmrbid" id="xmrbid"></s:hidden>
				<s:hidden name="PROJECTNO" id="PROJECTNO"></s:hidden>
				<s:hidden name="projectname" id="projectname"></s:hidden>
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
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
		<script type="text/javascript">
	$(function(){
		$("#STARTDATE").val($("#startdate").val());
		$("#ENDDATE").val($("#enddate").val());
		$("#CREATEUSER").val($("#createuser").val());
		$("#PROJECTNO").val($("#projectNo").val());	
	});
	</script>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
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