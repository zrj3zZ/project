<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
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
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#ifrmMain").validate({});
		mainFormValidator.resetForm();
	});
	
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	
	$(function(){
	    //查询
	    $("#search").click(function(){
	    	var valid = mainFormValidator.form(); //执行校验操作
	    	if(!valid){
	    	return false;
	    	}
	    	var sxlx = $("#sxlx").val();
	        var seachUrl = encodeURI("zqb_XPItemContentIndex.action?sxlx="+sxlx);
	        window.location.href = seachUrl;
	    });
    });
	
	function removeItemContent(instanceid){
		if(confirm("确定执行删除操作吗?")) {
			var pageUrl = "zqb_XPItemContentIndex_remove.action";
			$.post(pageUrl,{cinstanceid:instanceid},function(data){
		       			if(data=='success'){
		       				window.location.reload();
		       			}else{
		       				alert("删除失败。");
		       			} 
		     }); 
		}
	}
	
	function addItem(){
		var formid=$("#itemContentFormId").val();
		var demid=$("#itemContentDemId").val();
		var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	
	function openFormPage(instanceId,formId,demId){
		var url = 'openFormPage.action?formid='+formId+'&instanceId='+instanceId+'&demId='+demId;
		var target = "dem"+instanceId;
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url; 
	}
	
	function moveUp(){
		var checkLength=$("input[type='checkbox']:checked").length;
	    if(checkLength==0){
	         lhgdialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(checkLength>1){
	         lhgdialog.tips("选择行过多",1);
	         return;
	    }
	    var url='zqb_XPItemMoveUp.action';
	    var sxinstanceid=0;
	    $('input[name="chk_list"]:checked').each(function(){sxinstanceid=$(this).val();});
	    var formid=$("#itemContentFormId").val();
		var demid=$("#itemContentDemId").val();
	    $.post(url,{itemDemId:demid,itemFormId:formid,sxinstanceid:sxinstanceid},function(data){
	         if(data=='ok'){
	        	 $("[name = chk_list]:checkbox").attr("checked", false);
	        	 window.location.reload();
	         }
	    });
	}
	
	function moveDown(){
		var checkLength=$("input[type='checkbox']:checked").length;
	    if(checkLength==0){
	         lhgdialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(checkLength>1){
	         lhgdialog.tips("选择行过多",1);
	         return;
	    }
	    var url='zqb_XPItemMoveDown.action';
	    var sxinstanceid=0;
	    $('input[name="chk_list"]:checked').each(function(){sxinstanceid=$(this).val();});
	    var formid=$("#itemContentFormId").val();
		var demid=$("#itemContentDemId").val();
	    $.post(url,{itemDemId:demid,itemFormId:formid,sxinstanceid:sxinstanceid},function(data){
	         if(data=='ok'){
	        	 $("[name = chk_list]:checkbox").attr("checked", false);
	        	 window.location.reload();
	         }
	    });
	}
	
	function moveTop(){
		var checkLength=$("input[type='checkbox']:checked").length;
	    if(checkLength==0){
	         lhgdialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(checkLength>1){
	         lhgdialog.tips("选择行过多",1);
	         return;
	    }
	    var url='zqb_XPItemMoveTop.action';
	    var sxinstanceid=0;
	    $('input[name="chk_list"]:checked').each(function(){sxinstanceid=$(this).val();});
	    var formid=$("#itemContentFormId").val();
		var demid=$("#itemContentDemId").val();
	    $.post(url,{itemDemId:demid,itemFormId:formid,sxinstanceid:sxinstanceid},function(data){
	         if(data=='ok'){
	        	 $("[name = chk_list]:checkbox").attr("checked", false);
	        	 window.location.reload();
	         }
	    });
	}
	
	function moveBottom(){
		var checkLength=$("input[type='checkbox']:checked").length;
	    if(checkLength==0){
	         lhgdialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(checkLength>1){
	         lhgdialog.tips("选择行过多",1);
	         return;
	    }
	    var url='zqb_XPItemMoveBottom.action';
	    var sxinstanceid=0;
	    $('input[name="chk_list"]:checked').each(function(){sxinstanceid=$(this).val();});
	    var formid=$("#itemContentFormId").val();
		var demid=$("#itemContentDemId").val();
	    $.post(url,{itemDemId:demid,itemFormId:formid,sxinstanceid:sxinstanceid},function(data){
	         if(data=='ok'){
	        	 $("[name = chk_list]:checkbox").attr("checked", false);
	        	 window.location.reload();
	         }
	    });
	}
	
	function windowReload(){
		window.location.href=window.location.href;
		window.location.reload;
	}
</script>
<style type="text/css">
pre {
  	overflow: auto; 
	white-space: pre-wrap; /* css-3 */
	white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
	white-space: -pre-wrap; /* Opera 4-6 */
	white-space: -o-pre-wrap; /* Opera 7 */
	word-wrap: break-word; /* Internet Explorer 5.5+ */
}
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
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}

.cell td {
	margin: 0;
	padding: 3px 4px;
	font-size: 12px;
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
	<div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:void(0);" onclick="addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			<a href="javascript:void(0);" onclick="windowReload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<a href="javascript:void(0);" onclick="moveTop();" class="easyui-linkbutton" plain="true" iconCls="icon-top">置顶</a>
			<a href="javascript:void(0);" onclick="moveUp();" class="easyui-linkbutton" plain="true" iconCls="icon-up">上移</a>
			<a href="javascript:void(0);" onclick="moveDown();" class="easyui-linkbutton" plain="true" iconCls="icon-down">下移</a>
			<a href="javascript:void(0);" onclick="moveBottom();" class="easyui-linkbutton" plain="true" iconCls="icon-bottom">置底</a>
		</div>
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px;text-align:center;">
				<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">事项类型</td>
										<td class="searchdata"><s:select list="itemTitleList" theme="simple" headerKey="" headerValue="-空-" name="sxlx" id="sxlx" onchange="$('#search').click();"></s:select></td>
									</tr>
								</table>
							<td>
							<td valign='bottom' style='padding-bottom:5px;'><a
								id="search" class="easyui-linkbutton" icon="icon-search"
								href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef;table-layout:fixed;">
				<tr class="header">
					<td style="width:2%;">序号</td>
					<td style="width:10%;">事项名称</td>
					<td style="width:20%;">披露内容</td>
					<td style="width:5%;">操作</td>
				</tr>
				<s:iterator value="itemContentList" status="status">
					<tr class="cell">
						<td>
						<input type="checkbox" class="chk_list" name="chk_list" value="<s:property value="CINSTANCEID"/>" id="<s:property value="CINSTANCEID"/>">
						<s:property value="ROWNUM"/>
						</td>
						<td><s:property value="SXMC"/></td>
						<td class="pre"><pre><s:property value="PLNR"/></pre></td>
						<td>
							&nbsp;
							<a href="javascript:void(0)" style="color: #0000ff;" onclick="openFormPage(<s:property value='CINSTANCEID'/>,<s:property value='itemContentFormId'/>,<s:property value='itemContentDemId'/>)">
								编辑
							</a>
							&nbsp;|&nbsp;
							<a href="javascript:void(0)" style="color: #0000ff;" onclick="removeItemContent(<s:property value='CINSTANCEID'/>)">
								删除
							</a>
						</td>
					</tr>
				</s:iterator>
			</table>
			<form action="zqb_XPItemContentIndex.action" method="post" name="frmMain" id="frmMain">
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="sxlx" id="sxlx"></s:hidden>
			</form>
			<s:hidden name="itemContentFormId" id="itemContentFormId"></s:hidden>
			<s:hidden name="itemContentDemId" id="itemContentDemId"></s:hidden>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<%-- <div region="south" style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"	border="false">
		<div style="padding:5px">
			<s:if test="itemCotentTotal==0">
			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;">
				</div>
			</s:else>
		</div>
	</div> --%>
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
