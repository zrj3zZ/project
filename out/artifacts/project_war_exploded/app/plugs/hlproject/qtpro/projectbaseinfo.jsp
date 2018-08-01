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
	function addFormPage(demid,formid,jdbh){
		var insId=$("#instanceId", window.parent.document).val();
		if(insId==0){
			art.dialog.tips("请先保存主表!",1);
			return;
		}
		var xmbh = $("#xmbh").val();
		var jdmc = $("#JDMC").val();
		var url = "createFormInstance.action?formid="+formid+"&demId="+demid+"&XMBH="+xmbh+"&JDBH="+jdbh+'&JDMC='+encodeURI(jdmc);
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
	
	function editFormPage(demid,formid,instanceid,jdmc) {
		var url = "openFormPage.action?instanceId="+instanceid+"&formid="+formid+"&demId="+demid+'&JDMC='+encodeURI(jdmc);
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
					<td style="width:15%;">呈报人</td>
					<td style="width:45%;">资料内容</td>
					<td style="width:5%;"<s:if test="orgroleid==5"> align="center" colspan="2"</s:if>>操作</td>
				</tr>
				<s:iterator value="taskList">
					<tr class="cell">
						<td style="width:20%;"><s:property value="JDMC" /></td>
						<td style="width:15%;"><s:property value="LXTBR" /></td>
						<td style="width:45%;"><s:property value="LXFJ" escapeHtml="false"/></td>
						<s:if test="JDMC=='初步尽调'">
							<s:if test="INSTANCEID==0">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addFormPage(<s:property value='cbjddemid'/>,<s:property value='cbjdformid'/>,<s:property value='ID'/>)">添加</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editFormPage(<s:property value='cbjddemid'/>,<s:property value='cbjdformid'/>,<s:property value='INSTANCEID'/>,'<s:property value='JDMC'/>')">查看</a></td>
							</s:else>
						</s:if>
						<s:elseif test="JDMC=='项目立项'">
							<s:if test="INSTANCEID==0">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addFormPage(<s:property value='xmxldemid'/>,<s:property value='xmxlformid'/>,<s:property value='ID'/>)">添加</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editFormPage(<s:property value='xmxldemid'/>,<s:property value='xmxlformid'/>,<s:property value='INSTANCEID'/>,'<s:property value='JDMC'/>')">查看</a></td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='工作进度汇报'">
							<s:if test="INSTANCEID==0">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addFormPage(<s:property value='gzjdhbdemid'/>,<s:property value='gzjdhbformid'/>,<s:property value='ID'/>)">添加</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editFormPage(<s:property value='gzjdhbdemid'/>,<s:property value='gzjdhbformid'/>,<s:property value='INSTANCEID'/>,'<s:property value='JDMC'/>')">查看</a></td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='资料归档'">
							<s:if test="INSTANCEID==0">
								<td style="width:5%;"><a href="javascript:void(0)" onclick="addFormPage(<s:property value='zlgddemid'/>,<s:property value='zlgdformid'/>,<s:property value='ID'/>)">添加</a></td>
							</s:if>
							<s:else>
								<td style="width:5%;"><a href="javascript:void(0)" onclick="editFormPage(<s:property value='zlgddemid'/>,<s:property value='zlgdformid'/>,<s:property value='INSTANCEID'/>,'<s:property value='JDMC'/>')">查看</a></td>
							</s:else>
						</s:elseif>
							<s:if test="orgroleid==5">
							
								<td style="width:5%;">
									<s:if test="YJINS==null||YJINS==''">
										<a href="javascript:void(0)" onclick="addContent(<s:property value='INSTANCEID'/>)">提意见</a>
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
			<s:hidden name="xmxldemid" id="xmxldemid"></s:hidden>
			<s:hidden name="xmxlformid" id="xmxlformid"></s:hidden>
			<s:hidden name="gzjdhbdemid" id="gzjdhbdemid"></s:hidden>
			<s:hidden name="gzjdhbformid" id="gzjdhbformid"></s:hidden>
			<s:hidden name="zlgddemid" id="zlgddemid"></s:hidden>
			<s:hidden name="zlgdformid" id="zlgdformid"></s:hidden>
			<s:hidden name="cbjddemid" id="cbjddemid"></s:hidden>
			<s:hidden name="cbjdformid" id="cbjdformid"></s:hidden>
		</div>
	</div>
</body>
</html>