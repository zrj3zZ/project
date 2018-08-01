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
    $.ajax({
        url: 'sx_zqb_setLxxxReadonly.action',
        async: false,
        type: "POST",
        data: {
            projectno: $("#projectno").val()
        },
        dataType: "json",
        success: function (data) {
            if (data != null) {
                var readonly = data.readonly;
                if(readonly==true){
                    $("#tools_nav").css('display','none');
                }
                
            }
        }
    });
});
function addQus(){
	var instanceId = parent.document.getElementById("instanceId").value;
	if(instanceId==0){
		alert("请先保存项目信息!");
		return;
	}
	var projectno = $("#projectno").val();
	var pageUrl = "zqb_project_sx_qusansadd.action?projectno="+projectno;
	art.dialog.open(pageUrl,{
		title:'添加问题及解决方案',
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999',
		rang:true,
		width:800,
		cache:false,
		lock: true,
		height:520, 
		iconTitle:false,
		extendDrag:true,
		autoSize:false,
		content:pageUrl,
		close:function(){
			window.location.reload();
		}
	});
}
function editQus(qinsains){
	var pageUrl = "zqb_project_sx_qusansedit.action?commonstr="+qinsains;
	art.dialog.open(pageUrl,{
		title:'编辑问题及解决方案',
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999',
		rang:true,
		width:800,
		cache:false,
		lock: true,
		height:520, 
		iconTitle:false,
		extendDrag:true,
		autoSize:false,
		content:pageUrl,
		close:function(){
			window.location.reload();
		}
	});
}
function delQus(){
		if(confirm('确认删除?')){
			var list = $('[name=colname]').length;
			var a=0;
			for( var n = 0; n < list; n++){
				var flag = new Boolean();
				if($('[name=colname]')[n].checked==false&&$('[name=colname]')[n].id!='chkAll'){
					a++;
					if(a==list-1){
						alert('请选择您要删除的行项目!');
						return;
					}
				}
				if($('[name=colname]')[n].checked==true&&String($('[name=colname]')[n].id)!=String('chkAll')){
					var deleteUrl = "zqb_project_sx_qusansdel.action";
					$.post(deleteUrl,{commonstr:$('[name=colname]')[n].id},function(data){
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
		<div id="tools_nav" class="tools_nav">
			<a href="javascript:addQus();" class="easyui-linkbutton l-btn l-btn-plain" plain="true" iconcls="icon-add">新增</a>
			<a href="javascript:delQus();" class="easyui-linkbutton l-btn l-btn-plain" plain="true" iconcls="icon-cancel">删除</a>
		</div>
		<table width="100%" cellspacing="0px">
		<tr class="header">
			<td width="3%" style="text-align:left;">
				<input type="checkbox" name="colname" id="chkAll" />
			</td>
			<td width="14%" style="border-left:1px dashed #CCCCCC;text-align:left;">
				日期
			</td>
			<td width="41%" style="border-left:1px dashed #CCCCCC;text-align:left;">
				事项描述
			</td>
			<td width="42%" style="border-left:1px dashed #CCCCCC;text-align:left;">
				处理结果
			</td>
		</tr>
		<s:iterator value="sxQusansList" status="status">
		<tr>
			<td width="3%" style="border-top:1px dashed #CCCCCC;;text-align:left;">
				<input type="checkbox" name="colname" id="<s:property value="QINS"/>#<s:property value="AINS"/>" value="<s:property value="QINS"/>#<s:property value="AINS"/>"/>
			</td>
			<td title="<s:property value='CREATEDATE'/>" width="14%" style="border-left:1px dashed #CCCCCC;;border-top:1px dashed #CCCCCC;;text-align:left;color:#0000FF;">
				<s:property value='CREATEDATE'/>
			</td>
			<td title="<s:property value='QUESTION'/>" width="41%" style="border-left:1px dashed #CCCCCC;;border-top:1px dashed #CCCCCC;;text-align:left;color:#0000FF;">
				<%-- <s:property value='QUESTION'/> --%>
				<a href="javascript:editQus('<s:property value="QINS"/>,<s:property value="AINS"/>');">
				<s:if test="QUESTION.length() > 30">
					<s:property value="QUESTION.substring(0,30)"/>
				</s:if>
				<s:else>
					<s:property value="QUESTION"/>
				</s:else>
				</a>
			</td>
			<td title="<s:property value='CONTENT'/>" width="42%" style="border-left:1px dashed #CCCCCC;;border-top:1px dashed #CCCCCC;;text-align:left;color:#0000FF;">
				<%-- <s:property value='CONTENT'/> --%>
				<s:if test="CONTENT.length() > 30">
					<s:property value="CONTENT.substring(0,30)"/>
				</s:if>
				<s:else>
					<s:property value="CONTENT"/>
				</s:else>
			</td>
		</tr>
		</s:iterator>
		</table>
		<div style="display:none;">
			<s:hidden name="projectno" id="projectno"></s:hidden>
			<s:hidden name="formid" id="formid"></s:hidden>
			<s:hidden name="demid" id="demid"></s:hidden>
		</div>
</body>
</html>