<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
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
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	function addFormPage(actDefId,xmbh,jdKey,jdbh,jdmc){
		var insId=$("#instanceId", window.parent.document).val();
		if(insId==0){
			art.dialog.tips("请先保存主表!",1);
			return;
		}
		var xmbh = $("#xmbh").val();
		var projectname = $("#projectname").val();
		var url = 'processRuntimeStartInstance.action?actDefId='+actDefId+'&PROJECTNO='+encodeURI(xmbh)+'&GROUPID='+encodeURI(jdbh)+'&JDMC='+encodeURI(jdmc)+'&PROJECTNAME='+encodeURI(projectname);
		art.dialog.open(url,{
			title:'添加',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1000,
			cache:false,
			lock: true,
			height:700, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});	
	}
	
	function editFormPage(lcbh,lcbs,taskid,jdmc,lzjd) {
		var url="loadProcessFormPage.action?actDefId="+lcbs+"&instanceId="+lcbh+"&excutionId="+lcbh+"&taskId="+taskid+"&JDMC="+encodeURI(jdmc)+"&actStepDefId="+lzjd;
		art.dialog.open(url,{
			title:'编辑',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1000,
			cache:false,
			lock: true,
			height:700, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});	
	}
	
	function addPage(demid,formid,jdbh,jdmc){
		var insId=$("#instanceId", window.parent.document).val();
		if(insId==0){
			art.dialog.tips("请先保存主表!",1);
			return;
		}
		var xmbh = $("#xmbh").val();
		var projectname = $("#projectname").val();

		var url = 'createFormInstance.action?formid='+formid+'&demId='+demid+'&PROJECTNO='+xmbh+'&GROUPID='+jdbh+'&JDMC='+encodeURI(jdmc)+'&PROJECTNAME='+encodeURI(projectname);
		art.dialog.open(url,{
			title:'添加',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1000,
			cache:false,
			lock: true,
			height:700, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});	
	}
	
	function editPage(demid,formid,ins) {
		var url="openFormPage.action?instanceId="+ins+"&formid="+formid+"&demId="+demid;
		art.dialog.open(url,{
			title:'编辑',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1000,
			cache:false,
			lock: true,
			height:700, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
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
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
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
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top;">
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:20%;">阶段名称</td>
					<td style="width:10%;">呈报人</td>
					<td style="width:40%;">资料内容</td>
					<td style="width:10%;">状态</td>
					<td style="width:5%;"<s:if test="orgroleid==5"> align="center" colspan="2"</s:if>>操作</td>
				</tr>
				<s:iterator value="taskList">
					<tr class="cell">
						<td style="width:20%;"><s:property value="JDMC" /></td>
						<td style="width:10%;"><s:property value="CREATEUSER" /></td>
						<td style="width:40%;"><s:property value="FJSTR" escapeHtml="false"/></td>
						<td style="width:10%;"><s:property value="SPZT" /></td>
						<s:if test="JDMC=='初步尽调'">
							<s:if test="LCBH==null||LCBH==''">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addPage(<s:property value='cbjddemid'/>,<s:property value='cbjdformid'/>,<s:property value='ID'/>,'<s:property value='JDMC'/>')">添加</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editPage(<s:property value='cbjddemid'/>,<s:property value='cbjdformid'/>,<s:property value='LCBH'/>)">查看</a></td>
							</s:else>
						</s:if>
						<s:elseif test="JDMC=='项目立项'">
							<s:if test="SPZT==null||SPZT==''">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addFormPage('<s:property value='hldzxmlxlcServer'/>','<s:property value='XMBH'/>','CWGZJDHB',<s:property value='ID'/>,'<s:property value='JDMC'/>')">呈报</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editFormPage('<s:property value='LCBH'/>','<s:property value='LCBS'/>',<s:property value='TASKID'/>,'<s:property value='JDMC'/>','<s:property value='LZJD'/>')">查看</a></td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='股票发行方案'">
							<s:if test="SPZT==null||SPZT==''">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addFormPage('<s:property value='hldzgpfxlcServer'/>','<s:property value='XMBH'/>','CWGZJDHB',<s:property value='ID'/>,'<s:property value='JDMC'/>')">呈报</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editFormPage('<s:property value='LCBH'/>','<s:property value='LCBS'/>',<s:property value='TASKID'/>,'<s:property value='JDMC'/>','<s:property value='LZJD'/>')">查看</a></td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='一阶段股转反馈问题'">
							<s:if test="SPZT==null||SPZT==''">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addFormPage('<s:property value='hldzyjgwlcServer'/>','<s:property value='XMBH'/>','CWGZJDHB',<s:property value='ID'/>,'<s:property value='JDMC'/>')">呈报</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editFormPage('<s:property value='LCBH'/>','<s:property value='LCBS'/>',<s:property value='TASKID'/>,'<s:property value='JDMC'/>','<s:property value='LZJD'/>')">查看</a></td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='一阶段股转反馈回复'">
							<s:if test="SPZT==null||SPZT==''">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addFormPage('<s:property value='hldzyjghlcServer'/>','<s:property value='XMBH'/>','CWGZJDHB',<s:property value='ID'/>,'<s:property value='JDMC'/>')">呈报</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editFormPage('<s:property value='LCBH'/>','<s:property value='LCBS'/>',<s:property value='TASKID'/>,'<s:property value='JDMC'/>','<s:property value='LZJD'/>')">查看</a></td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='申报资料（备案）'">
							<s:if test="SPZT==null||SPZT==''">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addFormPage('<s:property value='hldzsbzllcServer'/>','<s:property value='XMBH'/>','CWGZJDHB',<s:property value='ID'/>,'<s:property value='JDMC'/>')">呈报</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editFormPage('<s:property value='LCBH'/>','<s:property value='LCBS'/>',<s:property value='TASKID'/>,'<s:property value='JDMC'/>','<s:property value='LZJD'/>')">查看</a></td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='二阶段股转反馈问题'">
							<s:if test="SPZT==null||SPZT==''">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addFormPage('<s:property value='hldzejgwlcServer'/>','<s:property value='XMBH'/>','CWGZJDHB',<s:property value='ID'/>,'<s:property value='JDMC'/>')">呈报</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editFormPage('<s:property value='LCBH'/>','<s:property value='LCBS'/>',<s:property value='TASKID'/>,'<s:property value='JDMC'/>','<s:property value='LZJD'/>')">查看</a></td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='二阶段股转反馈回复'">
							<s:if test="SPZT==null||SPZT==''">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addFormPage('<s:property value='hldzejghlcServer'/>','<s:property value='XMBH'/>','CWGZJDHB',<s:property value='ID'/>,'<s:property value='JDMC'/>')">呈报</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editFormPage('<s:property value='LCBH'/>','<s:property value='LCBS'/>',<s:property value='TASKID'/>,'<s:property value='JDMC'/>','<s:property value='LZJD'/>')">查看</a></td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='资料归档（需确认是否发行成功的状态）'">
							<s:if test="LCBH==null||LCBH==''">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addPage(<s:property value='zlgddemid'/>,<s:property value='zlgdformid'/>,<s:property value='ID'/>,'<s:property value='JDMC'/>')">添加</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editPage(<s:property value='zlgddemid'/>,<s:property value='zlgdformid'/>,<s:property value='LCBH'/>)">查看</a></td>
							</s:else>
						</s:elseif>
						<!-- 流程提意见 -->
						<s:if test="orgroleid==5">
							
								<td style="width:5%;">
									<s:if test="YJINS==null||YJINS==''">
										<a href="javascript:void(0)" onclick="addContent(<s:property value='LCBH'/>)">提意见</a>
									</s:if>
									<s:else>
										<a href="javascript:void(0)" onclick="editContent(<s:property value='YJINS'/>)">提意见</a>
									</s:else>
								</td>
							
						</s:if>
					</tr>
				</s:iterator>
			</table>
		</div>
		<div style="display:none;">
			<s:hidden name="xmbh" id="xmbh"></s:hidden>
			<s:hidden name="projectname" id="projectname"></s:hidden>
			<s:hidden name="zlgddemid" id="zlgddemid"></s:hidden>
			<s:hidden name="zlgdformid" id="zlgdformid"></s:hidden>
			<s:hidden name="cbjddemid" id="cbjddemid"></s:hidden>
			<s:hidden name="cbjdformid" id="cbjdformid"></s:hidden>
		</div>
	</div>
</body>
</html>