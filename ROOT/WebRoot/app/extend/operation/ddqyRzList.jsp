<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公告统计</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
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
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
	mainFormValidator =  $("#ifrmMain").validate({});
	mainFormValidator.resetForm();
});
	$(function() {
		$('#pp').pagination({
			total : <s:property value="totalNum"/>,
			pageNumber : <s:property value="pageNumber"/>,
			pageSize : <s:property value="pageSize"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMsg(pageNumber, pageSize);
			}
		});
});
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#ifrmMain").submit();
		return;
	}
	$(function() {
		//查询
		$(document).bind('keydown', function(event) {
			if (event.keyCode == "13") {
				//禁用键盘按钮按下事件
				return false;
			}
		});
		$("#search").click(
				function() {
					var valid = mainFormValidator.form(); //执行校验操作
					if(!valid){
						return false;
					}
					var wgfxnr = $("#wgfxnr").val();
					var gsdm = $("#gsdm").val();
					var gsjc = $("#gsjc").val();
					var seachUrl = encodeURI("qywgfxjl.action?wgfxnr=" + wgfxnr + "&gsdm=" + gsdm+ "&gsjc=" + gsjc);
					var wgfxlx=$('#wgfxlx option:selected').val();
					if(wgfxlx!=null){
						seachUrl = encodeURI("qywgfxjl.action?wgfxnr=" + wgfxnr + "&gsdm=" + gsdm+ "&gsjc=" + gsjc+ "&wgfxlx=" + wgfxlx);
					}
					
					window.location.href = seachUrl;
		});
		var flag=document.getElementById("flags").value;
		//alert(flag);
	});

		

	
/* 	function updXcjcs(tid){
		var formid = $("#rzformid").val();
		var demid = $("#rzdemid").val();
		var pageUrl = "openFormPage.action?formid="+formid+"&instanceId="+tid+"&demId="+demid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
			
	} */
	function updXcjc(tid,flag){
		var formid = $("#rzformid").val();
		var demid = $("#rzdemid").val();
		var pageUrl = "openFormPage.action?formid="+formid+"&instanceId="+tid+"&demId="+demid+"&EXTEND2="+flag;
		var target = "_blank";
		var win_width = window.screen.width;
		//var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		//page.location = pageUrl;
			
		
		
		 var winObj =  window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		 winObj.location = pageUrl; 
		 var loop = setInterval(function() {       
		        if(winObj.closed) {      
		            clearInterval(loop);      
		            //alert('closed');      
		            parent.location.reload();   
		        }      
		    }, 1); 
	}
	
	

	function remove(tid) {
		if(confirm("确认删除该入账信息吗？")){
			var deleteUrl = "xcjclv_del.action";
			$.post(deleteUrl, {
				instanceid : tid
			}, function(data) {
				if (data == 'success') {
					window.location.reload();
				} else {
					alert(data);
				}
			});
		}
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
	
<form name='ifrmMain' id='ifrmMain' method="post">
<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="rzformid" id="rzformid"></s:hidden>
				<s:hidden name="rzdemid" id="rzdemid"></s:hidden>
				<s:hidden name="flags" id="flags"></s:hidden>
</form>
		
		<div style="padding:5px">

	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header" style="text-align: center;">
				<TD style="width:26%">入账人</TD>
				<TD style="width:27%">入账（万元）</TD>
				<TD style="width:27%">入账日期</TD>
				<TD style="width:20%">操作</TD>
			</TR>
			<s:iterator value="list"  status="status">
				<TR class="cell" >
				<TD style="text-align: center;"><s:property value="lrr"/>[<s:property value="xylx"/>]</TD>
				<TD style="text-align: center;"><s:property value="dzje"/></TD>
				<TD style="text-align: center;"><s:property value="dzrq"/></TD>
				<TD style="text-align: center;" id="bj">
					<s:if test="flags==0">
						<s:if test="flag==0">
						
							 <a href="javascript:updXcjc('<s:property value="insId"/>','1');" >编辑</a> 
							&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="javascript:remove('<s:property value="insId"/>');" >删除</a> 
						</s:if>
						<s:if test="flag==1">
							 <a href="#" >编辑</a> 
							&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="#" >删除</a> 
						</s:if>
					</s:if>
					<s:if test="flags==1">
						<a href="javascript:updXcjc('<s:property value="insId"/>','0');" >查看</a> 
					</s:if>
				</TD>
			</TR>
			</s:iterator>
		</table>
			
				</TD>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
		border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div></div>
	
</body>
</html>
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