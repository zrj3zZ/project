<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>协议资料</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/engine/iformpage.css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.jqGrid.min.js">
	
</script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js">
	
</script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css"
	href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript"
	src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>

	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>


<script type="text/javascript">
	var api= frameElement.api; W = api.opener; 
	//关闭窗口
	function closeWin(){
	     api.close();
	}
	function adddItem(){
		var formid=$("#xmzlListformid").val();
		var id=$("#xmzlListid").val();
		var projectno=$("#projectNo").val();
		var pageUrl = "url:createFormInstance.action?formid="+formid+"&demId="+id+"&projectno="+projectno;
		$.dialog({ 
			title:'协议清单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:500,
			cache:false,
			lock: true,
			height:380, 
			iconTitle:false,
			extendDrag:true,
			autoSize:true,
			content:pageUrl,
			close:function(){
				window.location.reload();
			}
		});
	}
	function editItem(instanceId){
		var formid=$("#xmzlListformid").val();
		var id=$("#xmzlListid").val();
		var pageUrl = "url:createFormInstance.action?formid="+formid+"&demId="+id+"&instanceId="+instanceId;
		$.dialog({ 
			title:'协议清单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:500,
			cache:false,
			lock: true,
			height:380, 
			iconTitle:false,
			extendDrag:true,
			autoSize:true,
			content:pageUrl,
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function deleteItem(id){
		var pageUrl = encodeURI("delatexmzllist.action?listid="+id);
		$.messager.confirm("确认","确认删除清单？",function(result){ 
		 	if(result){
		 		$.post(pageUrl,function(data){ 
	       			window.location.reload();
	     		 });
			}
		});
	}
	function selectAll(){
		if($("input[name='chk_list']").attr("checked")){
			$("input[name='chk_list']").attr("checked",true);
		}else{
			$("input[name='chk_list']").attr("checked",false);
		}
	}
	
	function saveListItem(){
		var str="";
        $("input[name='chk_list']:checkbox").each(function(){ 
            if($(this).attr("checked")){
                str += $(this).val()+","
            }
        });
        var projectno=$("#projectNo").val();
        var pageUrl = encodeURI("savaxmzllist.action?chk_list="+str+"&projectno"+projectno);
        $.post(pageUrl,function(data){
        	alert("保存成功！");
        	closeWin();
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
</style>
</head>
<body class="easyui-layout">
<div region="north" border="false" >
			<div style="float: left;">
				<a href="javascript:saveListItem();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
				<a href="javascript:adddItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
				<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			</div> 
	</div>
	<div style="clear: left;"></div>
	<div region="center"style="border:0px;background-position:top;">
		<form name='ifrmMain' id='ifrmMain'  method="post" >
	<div style="padding:5px">
		<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
</div>
	</div>
	</form>
	<s:hidden name="PROJECTNO" id="PROJECTNO"></s:hidden>
	<s:form id="editForm" name="editForm" theme="simple"></s:form>
		<form id="savaxmzlform" action="savaxmzllist.action">
			<div>
				<table id='iform_grid' width="100%" style="border:1px solid #efefef; text-align:center;">
					<tr class="header">
						<td style="width:1%;">
						<%-- <s:if test="ZT=='已删除'"><s:property value="ZT" /></s:if><input id="chkAll" type="checkbox" onclick="selectAll()" name="chk_listAll"> --%>
						选择</td>
						<td style="width:14.2%;">反馈资料清单</td>
						<td style="width:14.2%;">操作</td>
					</tr>
					<s:iterator value="XMZLlist" status="status">
						<tr class="cell">
							<td style="text-align:center;"><s:if test="ZT!='已删除'"><input id="<s:property value="ID"/>" value="<s:property value="XYMC" />" type="checkbox" name="chk_list"></s:if></td>
							<td style="text-align:center;">
							<a href="javascript:editItem('<s:property value="INSTANCEID" />');">
							<input name="XYMC" value="<s:property value="XYMC" />" readonly="readonly" disabled="disabled" style="BACKGROUND-COLOR: transparent;border: 0px;">
							</a>
							</td>
							<td style="text-align:center;"><s:if test="ZT=='已删除'"><s:property value="ZT" /></s:if><s:else><a href="javascript:deleteItem(<s:property value="ID"/>);">删除</a><s:property value="ZT" /></a></s:else></td>
						</tr>
					</s:iterator>
				</table>
					<s:hidden name="xmzlListformid" id="xmzlListformid"></s:hidden>
					<s:hidden name="xmzlListid" id="xmzlListid"></s:hidden>
					<s:hidden name="projectNo" id="projectNo"></s:hidden>
					<s:hidden name="xmzlxxid" id="xmzlxxid"></s:hidden>
					<s:hidden name="xmzlxxformid" id="xmzlxxformid"></s:hidden>
					<s:hidden name="xmzlxxckformid" id="xmzlxxckformid"></s:hidden>
					<s:hidden name="xmzlxxckid" id="xmzlxxckid"></s:hidden>
				<div id='prowed_ifrom_grid'></div>
			</div>
		</form>
	</div>
	<div region="south"
		style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
		border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
		<script type="text/javascript">
	
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