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
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	function addProcessFormPage(jdbh,jdmc){
		var demid;
		var formid;
		var bgzzlxdemid = $("#bgzzlxdemid").val();
		var bgzzlxformid = $("#bgzzlxformid").val();
		
		var bgzzggdemid = $("#bgzzggdemid").val();
		var bgzzggformid = $("#bgzzggformid").val();
		var bgzzgddemid = $("#bgzzgddemid").val();
		var bgzzgdformid = $("#bgzzgdformid").val();
		
		var bgzzsbdemid = $("#bgzzsbdemid").val();
		var bgzzsbformid = $("#bgzzsbformid").val();
		var bgzzfkdemid = $("#bgzzfkdemid").val();
		var bgzzfkformid = $("#bgzzfkformid").val();
		var bgzzpldemid = $("#bgzzpldemid").val();
		var bgzzplformid = $("#bgzzplformid").val();
		var bgzzssdemid = $("#bgzzssdemid").val();
		var bgzzssformid = $("#bgzzssformid").val();
		
		var bgzzcxdemid = $("#bgzzcxdemid").val();
		var bgzzcxformid = $("#bgzzcxformid").val();
		
		var bgzzzzdemid = $("#bgzzzzdemid").val();
		var bgzzzzformid = $("#bgzzzzformid").val();
		
		var dgqtwjformid = $("#dgqtwjformid").val();
		var dgqtwjdemid = $("#dgqtwjdemid").val();
		var dgxmzjuformid = $("#dgxmzjuformid").val();
		var dgxmzjudemid = $("#dgxmzjudemid").val();
		
		if(jdmc=='立项'){
			demid=bgzzlxdemid;
			formid=bgzzlxformid;
		}else if(jdmc=='公告并购重组方案'){
			demid=bgzzggdemid;
			formid=bgzzggformid;
		}else if(jdmc=='股东大会决议'){
			demid=bgzzgddemid;
			formid=bgzzgdformid;
		}else if(jdmc=='申报'){
			demid=bgzzsbdemid;
			formid=bgzzsbformid;
		}else if(jdmc=='反馈'){
			demid=bgzzfkdemid;
			formid=bgzzfkformid;
		}else if(jdmc=='补充披露'){
			demid=bgzzpldemid;
			formid=bgzzplformid;
		}else if(jdmc=='实施情况'){
			demid=bgzzssdemid;
			formid=bgzzssformid;
		}else if(jdmc=='持续督导期间'){
			demid=bgzzcxdemid;
			formid=bgzzcxformid;
		}else if(jdmc=='项目终止'){
			demid=bgzzzzdemid;
			formid=bgzzzzformid;
		}else if(jdmc=='项目组工作记录'){
			formid=dgxmzjuformid;
			demid=dgxmzjudemid;
		}else if(jdmc=='其他文件'){
			formid=dgqtwjformid;
			demid=dgqtwjdemid;
		}
		var instanceid=$("#instanceId").val();
		var projectno = $("#projectno").val();
		var projectname = $("#projectname").val();
		if (instanceid == "0") {
			alert("请先保存项目信息!");
			return;
		}
		if(jdmc==null||jdmc==''){
			alert("阶段配置信息错误!");
			return;
		}
		var pageUrl = encodeURI("createFormInstance.action?formid="+formid+"&demId="+ demid + "&XMBH=" + projectno + "&JDBH=" + jdbh+"&JDMC="+jdmc + "&COMPANYNAME=" + projectname);
		if(jdmc=='项目组工作记录' || jdmc=='其他文件' ){
			pageUrl = encodeURI("createFormInstance.action?formid="+formid+"&demId="+demid+"&PROJECTNO=" + projectno + "&PROJECTNAME=" + projectname +  "&GROUPID=" + jdbh);
		}
		/* var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl; */
		art.dialog.open(pageUrl,{
			id : 'projectTask',
			cover : true,
			title : '项目阶段添加',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1000,
			cache : false,
			lock : true,
			height : 600,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			content : pageUrl,
			close : function() {
				window.location.reload();
			}
		});
	}
	
	function editProcessFormPage(instanceid,jdmc) {
		var demid;
		var formid;
		var bgzzlxdemid = $("#bgzzlxdemid").val();
		var bgzzlxformid = $("#bgzzlxformid").val();
		
		var bgzzggdemid = $("#bgzzggdemid").val();
		var bgzzggformid = $("#bgzzggformid").val();
		var bgzzgddemid = $("#bgzzgddemid").val();
		var bgzzgdformid = $("#bgzzgdformid").val();
		
		var bgzzsbdemid = $("#bgzzsbdemid").val();
		var bgzzsbformid = $("#bgzzsbformid").val();
		var bgzzfkdemid = $("#bgzzfkdemid").val();
		var bgzzfkformid = $("#bgzzfkformid").val();
		var bgzzpldemid = $("#bgzzpldemid").val();
		var bgzzplformid = $("#bgzzplformid").val();
		var bgzzssdemid = $("#bgzzssdemid").val();
		var bgzzssformid = $("#bgzzssformid").val();
		
		var bgzzcxdemid = $("#bgzzcxdemid").val();
		var bgzzcxformid = $("#bgzzcxformid").val();
		
		var bgzzzzdemid = $("#bgzzzzdemid").val();
		var bgzzzzformid = $("#bgzzzzformid").val();
		
		var dgqtwjformid = $("#dgqtwjformid").val();
		var dgqtwjdemid = $("#dgqtwjdemid").val();
		var dgxmzjuformid = $("#dgxmzjuformid").val();
		var dgxmzjudemid = $("#dgxmzjudemid").val();
		
		if(jdmc=='立项'){
			demid=bgzzlxdemid;
			formid=bgzzlxformid;
		}else if(jdmc=='公告并购重组方案'){
			demid=bgzzggdemid;
			formid=bgzzggformid;
		}else if(jdmc=='股东大会决议'){
			demid=bgzzgddemid;
			formid=bgzzgdformid;
		}else if(jdmc=='申报'){
			demid=bgzzsbdemid;
			formid=bgzzsbformid;
		}else if(jdmc=='反馈'){
			demid=bgzzfkdemid;
			formid=bgzzfkformid;
		}else if(jdmc=='补充披露'){
			demid=bgzzpldemid;
			formid=bgzzplformid;
		}else if(jdmc=='实施情况'){
			demid=bgzzssdemid;
			formid=bgzzssformid;
		}else if(jdmc=='持续督导期间'){
			demid=bgzzcxdemid;
			formid=bgzzcxformid;
		}else if(jdmc=='项目终止'){
			demid=bgzzzzdemid;
			formid=bgzzzzformid;
		}else if(jdmc=='项目组工作记录'){
			formid=dgxmzjuformid;
			demid=dgxmzjudemid;
		}else if(jdmc=='其他文件'){
			formid=dgqtwjformid;
			demid=dgqtwjdemid;
		}
		var pageUrl = encodeURI("openFormPage.action?formid="+formid+"&demId="+demid+"&instanceId="+instanceid);
		/* var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl; */
		art.dialog.open(pageUrl,{
			id : 'projectTask',
			cover : true,
			title : '项目阶段编辑',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1000,
			cache : false,
			lock : true,
			height : 600,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			content : pageUrl,
			close : function() {
				window.location.reload();
			}
		});
	}
	
	function checkXmjd(xmbh,jdmc){
		var result=null;
		$.ajaxSetup({
	        async: false
	    });
		$.ajax({
	        url : 'sx_zqb_BgCheckXMJD.action',
	        async : false,
	        type : "POST",
	        data: {
	        	xmbh:xmbh,
		    	jdmc:jdmc
            },
	        dataType : "json",
	        success : function(data) {
	        	result=data;
	        }  
	    });
	    return result;
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
					<td style="width:10%;">填报人</td>
					<td style="width:10%;">日期</td>
					<td style="width:40%;">阶段资料</td>
					<td style="width:5%;">操作</td>
				</tr>
				<s:iterator value="jdList">
					<tr class="cell">
						<td style="width:20%;"><s:property value="JDMC" /></td>
						<td style="width:10%;"><s:property value="TBR" /></td>
						<td style="width:10%;"><s:property value="TBSJ" /></td>
						<td style="width:40%;"><s:property value="FJ" escapeHtml="false"/></td>
						<s:if test="INSTANCEID==0">
							<td style="width:5%;"><a href="javascript:void(0)" onclick="addProcessFormPage(<s:property value='ID'/>,'<s:property value='JDMC'/>')">上传</a></td>
						</s:if>
						<s:else>
							<td style="width:5%;"><a href="javascript:void(0)" onclick="editProcessFormPage(<s:property value='INSTANCEID'/>,'<s:property value='JDMC'/>')">编辑</a></td>
						</s:else>
					</tr>
				</s:iterator>
			</table>
		</div>
	</div>
<s:hidden name="bgzzlxdemid"></s:hidden>
<s:hidden name="bgzzlxformid"></s:hidden>

<s:hidden name="bgzzggdemid"></s:hidden>
<s:hidden name="bgzzggformid"></s:hidden>
<s:hidden name="bgzzgddemid"></s:hidden>
<s:hidden name="bgzzgdformid"></s:hidden>

<s:hidden name="bgzzsbdemid"></s:hidden>
<s:hidden name="bgzzsbformid"></s:hidden>
<s:hidden name="bgzzfkdemid"></s:hidden>
<s:hidden name="bgzzfkformid"></s:hidden>
<s:hidden name="bgzzpldemid"></s:hidden>
<s:hidden name="bgzzplformid"></s:hidden>
<s:hidden name="bgzzssformid"></s:hidden>
<s:hidden name="bgzzssdemid"></s:hidden>

<s:hidden name="bgzzcxdemid"></s:hidden>
<s:hidden name="bgzzcxformid"></s:hidden>

<s:hidden name="bgzzzzformid"></s:hidden>
<s:hidden name="bgzzzzdemid"></s:hidden>
<s:hidden name="projectno"></s:hidden>
<s:hidden name="projectname"></s:hidden>
<s:hidden name="instanceId"></s:hidden>

<s:hidden name="dgqtwjformid"></s:hidden>
<s:hidden name="dgqtwjdemid"></s:hidden>
<s:hidden name="dgxmzjuformid"></s:hidden>
<s:hidden name="dgxmzjudemid"></s:hidden>
</body>
</html>