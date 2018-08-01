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
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
$(function(){
	$("#chkAll").bind("click", function () {
    	$("[name =colname]:checkbox").attr("checked", this.checked);
    });
});
function addSubBond(){
	var instanceId = parent.document.getElementById("instanceId").value;
	if(instanceId==0){
		alert("请先保存主表信息!");
		return;
	}
	var dataid = parent.document.getElementById("dataid").value;
	var demId = $("#demId").val();
	var formid = $("#formid").val();
	var pageUrl = "url:createFormInstance.action?formid="+formid+"&demId="+demId+"&ZQINSID="+instanceId+"&DJZQID="+dataid;
	art.dialog.open(pageUrl,{
		title : '添加付息信息',
		loadingText : '正在加载中,请稍后...',
		bgcolor : '#999',
		rang : true,
		width : 1000,
		cache : false,
		lock : true,
		height : 420,
		iconTitle : false,
		extendDrag : true,
		autoSize : false,
		close : function() {
			window.location.reload();
		}
	});
}
function editSubBond(qinsains){
	var demId = $("#demId").val();
	var formid = $("#formid").val();
	var pageUrl = "url:openFormPage.action?formid="+formid+"&demId="+demId+"&instanceId="+qinsains;
	art.dialog.open(pageUrl,{
		title : '编辑付息信息',
		loadingText : '正在加载中,请稍后...',
		bgcolor : '#999',
		rang : true,
		width : 1000,
		cache : false,
		lock : true,
		height : 420,
		iconTitle : false,
		extendDrag : true,
		autoSize : false,
		close : function() {
			window.location.reload();
		}
	});
}
function delSubBond(){
	$.messager.confirm('确认','确认删除?',function(result){
		if(result){
			var list = $('[name=colname]').length;
			var a=0;
			for( var n = 0; n < list; n++){
				var flag = new Boolean();
				if($('[name=colname]')[n].checked==false&&$('[name=colname]')[n].id!='chkAll'){
					a++;
					if(a==list-1){
						alert('请选择您要删除的行信息!');
						return;
					}
				}
				if($('[name=colname]')[n].checked==true&&String($('[name=colname]')[n].id)!=String('chkAll')){
					var deleteUrl = "zqb_bond_delsub.action";
					$.post(deleteUrl,{instanceid:$('[name=colname]')[n].id},function(data){
						if(data=='success'){
							parent.window.location.reload();
		       			}else{
		       				flag=false;
		       				alert(data);
		       			}
					});
				} 
				if(!flag){break;}
			}
		}
	});
}
</script>
<style type="text/css">
td{
	color:#004080;
}
.tools_n {
	height:34px;
	line-height:30px;
	padding-left:5px;
	vertical-align:middle;
	padding-top:2px;
	padding-bottom:2px; 
	text-align:left;
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
td {
	line-height: 30px;
	padding-left: 3px;
	font-size: 12px;
	border-bottom:1px #efefef dotted;
	vertical-align:middle;
	word-wrap:break-word;
	word-break:break-all;
	font-weight:100;
	line-height:15px;
	padding-top:5px;
	text-align:left;
	
}
</style>
</head>
<body>
		<div class="tools_nav">
			<a href="javascript:addSubBond();" class="easyui-linkbutton l-btn l-btn-plain" plain="true" iconcls="icon-add">新增</a>
			<a href="javascript:delSubBond();" class="easyui-linkbutton l-btn l-btn-plain" plain="true" iconcls="icon-cancel">删除</a>
		</div>
		<table width="100%" cellspacing="0px">
		<tr class="header">
			<td width="3%" style="text-align:left;">
				<input type="checkbox" name="colname" id="chkAll" />
			</td>
			<td width="15%" style="border-left:1px dashed #CCCCCC;text-align:left;">
				付息时间
			</td>
			<td width="15%" style="border-left:1px dashed #CCCCCC;text-align:left;">
				偿还债券本金比例
			</td>
			<td width="19%" style="border-left:1px dashed #CCCCCC;text-align:left;">
				兑付付息公告与通知
			</td>
			<td width="19%" style="border-left:1px dashed #CCCCCC;text-align:left;">
				交易所证券兑付付息
			</td>
			<td width="19%" style="border-left:1px dashed #CCCCCC;text-align:left;">
				关于企业债付息兑付资金落实情况回执
			</td>
			<td width="27%" style="border-left:1px dashed #CCCCCC;text-align:left;">
				划款时间
			</td>
		</tr>
		<s:iterator value="list" status="status">
		<tr>
			<td width="3%" style="border-top:1px dashed #CCCCCC;;text-align:left;">
				<input type="checkbox" name="colname" id="<s:property value="INSTANCEID"/>" value="<s:property value="INSTANCEID"/>"/>
			</td>
			<td title="<s:property value='FXSJ'/>" width="15%" style="border-left:1px dashed #CCCCCC;;border-top:1px dashed #CCCCCC;;text-align:left;color:#0000FF;">
				<a href="javascript:editSubBond('<s:property value="INSTANCEID"/>');">
					<s:property value="FXSJ"/>
				</a>
			</td>
			<td title="<s:property value='CHZQBJDYBL'/>" width="15%" style="border-left:1px dashed #CCCCCC;;border-top:1px dashed #CCCCCC;;text-align:left;color:#0000FF;">
				<a href="javascript:editSubBond('<s:property value="INSTANCEID"/>');">
					<s:property value="CHZQBJDYBL"/>
				</a>
			</td>
			<td title="<s:property value='DFFXGGYTZ'/>" width="19%" style="border-left:1px dashed #CCCCCC;;border-top:1px dashed #CCCCCC;;text-align:left;color:#0000FF;">
				<a href="javascript:editSubBond('<s:property value="INSTANCEID"/>');">
					<s:property value="DFFXGGYTZ"/>
				</a>
			</td>
			<td title="<s:property value='JYSZQDFFX'/>" width="19%" style="border-left:1px dashed #CCCCCC;;border-top:1px dashed #CCCCCC;;text-align:left;color:#0000FF;">
				<a href="javascript:editSubBond('<s:property value="INSTANCEID"/>');">
					<s:property value="JYSZQDFFX"/>
				</a>
			</td>
			<td title="<s:property value='ZJLSQKHZ'/>" width="19%" style="border-left:1px dashed #CCCCCC;;border-top:1px dashed #CCCCCC;;text-align:left;color:#0000FF;">
				<a href="javascript:editSubBond('<s:property value="INSTANCEID"/>');">
					<s:property value="ZJLSQKHZ"/>
				</a>
			</td>
			<td title="<s:property value='HKSJ'/>" width="27%" style="border-left:1px dashed #CCCCCC;;border-top:1px dashed #CCCCCC;;text-align:left;color:#0000FF;">
				<a href="javascript:editSubBond('<s:property value="INSTANCEID"/>');">
					<s:property value="HKSJ"/>
				</a>
			</td>
		</tr>
		</s:iterator>
		</table>
		<div style="display:none;">
			<s:hidden name="formid" id="formid"></s:hidden>
			<s:hidden name="demId" id="demId"></s:hidden>
		</div>
</body>
</html>