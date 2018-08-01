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
		var ipolxdemid = $("#ipolxdemid").val();
		var ipolxformid = $("#ipolxformid").val();
		
		var ipofademid = $("#ipofademid").val();
		var ipofaformid = $("#ipofaformid").val();
		var ipobgdemid = $("#ipobgdemid").val();
		var ipobgformid = $("#ipobgformid").val();
		var ipogddemid = $("#ipogddemid").val();
		var ipogdformid = $("#ipogdformid").val();
		var ipoqldemid = $("#ipoqldemid").val();
		var ipoqlformid = $("#ipoqlformid").val();
		
		var ipoggdemid = $("#ipoggdemid").val();
		var ipoggformid = $("#ipoggformid").val();
		var ipofddemid = $("#ipofddemid").val();
		var ipofdformid = $("#ipofdformid").val();
		var iposbdemid = $("#iposbdemid").val();
		var iposbformid = $("#iposbformid").val();
		var ipofkdemid = $("#ipofkdemid").val();
		var ipofkformid = $("#ipofkformid").val();
		
		var ipohzdemid = $("#ipohzdemid").val();
		var ipohzformid = $("#ipohzformid").val();
		var ipofjdemid = $("#ipofjdemid").val();
		var ipofjformid = $("#ipofjformid").val();
		var ipofxssdemid = $("#ipofxssdemid").val();
		var ipofxssformid = $("#ipofxssformid").val();
		var ipogfssdemid = $("#ipogfssdemid").val();
		var ipogfssformid = $("#ipogfssformid").val();
		
		var iposhdemid = $("#iposhdemid").val();
		var iposhformid = $("#iposhformid").val();
		var ipofxdemid = $("#ipofxdemid").val();
		var ipofxformid = $("#ipofxformid").val();
		var ipossdemid = $("#ipossdemid").val();
		var ipossformid = $("#ipossformid").val();
		var ipodddemid = $("#ipodddemid").val();
		var ipoddformid = $("#ipoddformid").val();
		var ipozzdemid = $("#ipozzdemid").val();
		var ipozzformid = $("#ipozzformid").val();
		
		var dgqtwjformid = $("#dgqtwjformid").val();
		var dgqtwjdemid = $("#dgqtwjdemid").val();
		var dgxmzjuformid = $("#dgxmzjuformid").val();
		var dgxmzjudemid = $("#dgxmzjudemid").val();
		
		
		if(jdmc=='立项'){
			demid=ipolxdemid;
			formid=ipolxformid;
		}else if(jdmc=='公告发行方案'){
			demid=ipofademid;
			formid=ipofaformid;
		}else if(jdmc=='公告并购重组方案'){
			demid=ipobgdemid;
			formid=ipobgformid;
		}else if(jdmc=='公告方案'){
			demid=ipofademid;
			formid=ipofaformid;
		}else if(jdmc=='股东大会决议'){
			demid=ipogddemid;
			formid=ipogdformid;
		}else if(jdmc=='权力机构决议'){
			demid=ipoqldemid;
			formid=ipoqlformid;
		}else if(jdmc=='股改'){
			demid=ipoggdemid;
			formid=ipoggformid;
		}else if(jdmc=='辅导'){
			demid=ipofddemid;
			formid=ipofdformid;
		}else if(jdmc=='上会'){
			demid=iposhdemid;
			formid=iposhformid;
		}else if(jdmc=='发行'){
			demid=ipofxdemid;
			formid=ipofxformid;
		}else if(jdmc=='上市'){
			demid=ipossdemid;
			formid=ipossformid;
		}else if(jdmc=='申报'){
			demid=iposbdemid;
			formid=iposbformid;
		}else if(jdmc=='反馈'){
			demid=ipofkdemid;
			formid=ipofkformid;
		}else if(jdmc=='核准'){
			demid=ipohzdemid;
			formid=ipohzformid;
		}else if(jdmc=='封卷'){
			demid=ipofjdemid;
			formid=ipofjformid;
		}else if(jdmc=='发行实施'){
			demid=ipofxssdemid;
			formid=ipofxssformid;
		}else if(jdmc=='实施'){
			demid=ipofxssdemid;
			formid=ipofxssformid;
		}else if(jdmc=='新增股份上市'){
			demid=ipogfssdemid;
			formid=ipogfssformid;
		}else if(jdmc=='持续督导期间'){
			demid=ipodddemid;
			formid=ipoddformid;
		}else if(jdmc=='项目终止'){
			demid=ipozzdemid;
			formid=ipozzformid;
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
		var pageUrl = encodeURI("createFormInstance.action?formid="+formid+"&demId="+ demid + "&XMBH=" + projectno + "&JDBH=" + jdbh+"&JDMC="+jdmc + "&PROJECTNAME=" + projectname);
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
		var ipolxdemid = $("#ipolxdemid").val();
		var ipolxformid = $("#ipolxformid").val();
		
		var ipofademid = $("#ipofademid").val();
		var ipofaformid = $("#ipofaformid").val();
		var ipogfdemid = $("#ipogfdemid").val();
		var ipogfformid = $("#ipogfformid").val();
		var ipogddemid = $("#ipogddemid").val();
		var ipogdformid = $("#ipogdformid").val();
		var ipoqldemid = $("#ipoqldemid").val();
		var ipoqlformid = $("#ipoqlformid").val();
		
		var ipoggdemid = $("#ipoggdemid").val();
		var ipoggformid = $("#ipoggformid").val();
		var ipofddemid = $("#ipofddemid").val();
		var ipofdformid = $("#ipofdformid").val();
		var iposbdemid = $("#iposbdemid").val();
		var iposbformid = $("#iposbformid").val();
		var ipofkdemid = $("#ipofkdemid").val();
		var ipofkformid = $("#ipofkformid").val();
		
		var ipohzdemid = $("#ipohzdemid").val();
		var ipohzformid = $("#ipohzformid").val();
		var ipofjdemid = $("#ipofjdemid").val();
		var ipofjformid = $("#ipofjformid").val();
		var ipofxssdemid = $("#ipofxssdemid").val();
		var ipofxssformid = $("#ipofxssformid").val();
		var ipogfssdemid = $("#ipogfssdemid").val();
		var ipogfssformid = $("#ipogfssformid").val();
		
		var iposhdemid = $("#iposhdemid").val();
		var iposhformid = $("#iposhformid").val();
		var ipofxdemid = $("#ipofxdemid").val();
		var ipofxformid = $("#ipofxformid").val();
		var ipossdemid = $("#ipossdemid").val();
		var ipossformid = $("#ipossformid").val();
		var ipodddemid = $("#ipodddemid").val();
		var ipoddformid = $("#ipoddformid").val();
		var ipozzdemid = $("#ipozzdemid").val();
		var ipozzformid = $("#ipozzformid").val();
		
		var dgqtwjformid = $("#dgqtwjformid").val();
		var dgqtwjdemid = $("#dgqtwjdemid").val();
		var dgxmzjuformid = $("#dgxmzjuformid").val();
		var dgxmzjudemid = $("#dgxmzjudemid").val();
		
		
		if(jdmc=='立项'){
			demid=ipolxdemid;
			formid=ipolxformid;
		}else if(jdmc=='公告发行方案'){
			demid=ipofademid;
			formid=ipofaformid;
		}else if(jdmc=='公告并购重组方案'){
			demid=ipobgdemid;
			formid=ipobgformid;
		}else if(jdmc=='公告方案'){
			demid=ipofademid;
			formid=ipofaformid;
		}else if(jdmc=='股东大会决议'){
			demid=ipogddemid;
			formid=ipogdformid;
		}else if(jdmc=='权力机构决议'){
			demid=ipoqldemid;
			formid=ipoqlformid;
		}else if(jdmc=='股改'){
			demid=ipoggdemid;
			formid=ipoggformid;
		}else if(jdmc=='辅导'){
			demid=ipofddemid;
			formid=ipofdformid;
		}else if(jdmc=='上会'){
			demid=iposhdemid;
			formid=iposhformid;
		}else if(jdmc=='发行'){
			demid=ipofxdemid;
			formid=ipofxformid;
		}else if(jdmc=='上市'){
			demid=ipossdemid;
			formid=ipossformid;
		}else if(jdmc=='申报'){
			demid=iposbdemid;
			formid=iposbformid;
		}else if(jdmc=='反馈'){
			demid=ipofkdemid;
			formid=ipofkformid;
		}else if(jdmc=='核准'){
			demid=ipohzdemid;
			formid=ipohzformid;
		}else if(jdmc=='封卷'){
			demid=ipofjdemid;
			formid=ipofjformid;
		}else if(jdmc=='发行实施'){
			demid=ipofxssdemid;
			formid=ipofxssformid;
		}else if(jdmc=='实施'){
			demid=ipofxssdemid;
			formid=ipofxssformid;
		}else if(jdmc=='新增股份上市'){
			demid=ipogfssdemid;
			formid=ipogfssformid;
		}else if(jdmc=='持续督导期间'){
			demid=ipodddemid;
			formid=ipoddformid;
		}else if(jdmc=='项目终止'){
			demid=ipozzdemid;
			formid=ipozzformid;
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
						<td style="width:10%;"><s:property value="CJSJ" /></td>
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
<s:hidden name="ipolxdemid"></s:hidden>
<s:hidden name="ipolxformid"></s:hidden>

<s:hidden name="ipofademid"></s:hidden>
<s:hidden name="ipofaformid"></s:hidden>
<s:hidden name="ipobgdemid"></s:hidden>
<s:hidden name="ipobgformid"></s:hidden>
<s:hidden name="ipogddemid"></s:hidden>
<s:hidden name="ipogdformid"></s:hidden>
<s:hidden name="ipoqldemid"></s:hidden>
<s:hidden name="ipoqlformid"></s:hidden>

<s:hidden name="ipoggdemid"></s:hidden>
<s:hidden name="ipoggformid"></s:hidden>
<s:hidden name="ipofddemid"></s:hidden>
<s:hidden name="ipofdformid"></s:hidden>
<s:hidden name="iposbdemid"></s:hidden>
<s:hidden name="iposbformid"></s:hidden>
<s:hidden name="ipofkdemid"></s:hidden>
<s:hidden name="ipofkformid"></s:hidden>

<s:hidden name="ipohzdemid"></s:hidden>
<s:hidden name="ipohzformid"></s:hidden>
<s:hidden name="ipofjdemid"></s:hidden>
<s:hidden name="ipofjformid"></s:hidden>
<s:hidden name="ipofxssdemid"></s:hidden>
<s:hidden name="ipofxssformid"></s:hidden>
<s:hidden name="ipogfssdemid"></s:hidden>
<s:hidden name="ipogfssformid"></s:hidden>

<s:hidden name="iposhdemid"></s:hidden>
<s:hidden name="iposhformid"></s:hidden>
<s:hidden name="ipofxdemid"></s:hidden>
<s:hidden name="ipofxformid"></s:hidden>
<s:hidden name="ipossdemid"></s:hidden>
<s:hidden name="ipossformid"></s:hidden>
<s:hidden name="ipodddemid"></s:hidden>
<s:hidden name="ipoddformid"></s:hidden>
<s:hidden name="ipozzdemid"></s:hidden>
<s:hidden name="ipozzformid"></s:hidden>
<s:hidden name="projectno"></s:hidden>
<s:hidden name="projectname"></s:hidden>
<s:hidden name="instanceId"></s:hidden>

<s:hidden name="dgqtwjformid"></s:hidden>
<s:hidden name="dgqtwjdemid"></s:hidden>
<s:hidden name="dgxmzjuformid"></s:hidden>
<s:hidden name="dgxmzjudemid"></s:hidden>
</body>
</html>