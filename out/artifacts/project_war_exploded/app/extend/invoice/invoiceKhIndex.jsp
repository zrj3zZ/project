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
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#ifrmMain").validate({});
		mainFormValidator.resetForm();
	});
	
	function addSqkp(customerno,customername,formid,demId){
		var url = 'createFormInstance.action?formid='+formid+'&demId='+demId+'&CUSTOMERNAME='+encodeURI(customername)+'&CUSTOMERNO='+encodeURI(customerno);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	}
	
	function addItem(){
		var khformid=$("#khformid").val();
		var khid=$("#khid").val();
		var pageUrl = "createFormInstance.action?formid="+khformid+"&demId="+khid;
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
	
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	$(function(){
		//分页
		$('#pp').pagination({
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
		
	    //查询
	    $("#search").click(function(){
	    	var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}
	    	$("#ifrmMain").submit();
	    });
	    
	    $(document).bind('keyup', function(event) {
			if (event.keyCode == "13") {
				var valid = mainFormValidator.form(); //执行校验操作
				if(!valid){
					return false;
				}
				//回车执行查询
				$("#ifrmMain").submit();
			}
		});
    });
	
	function removekh(instanceid){
		if(confirm("确定执行删除操作吗?")) {
			var pageUrl = "zqb_kpxx_remove.action";
			$.post(pageUrl,{khinstanceid:instanceid},function(data){ 
		       			if(data=='success'){
		       				window.location.reload();
		       			}else{
		       				alert("删除失败,该客户下存在开票信息。");
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
	<div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<a href="iwork_file/BIGTXT_FILE/东吴证券增值税专用发票参考模板.docx" class="easyui-linkbutton" plain="true" iconCls="icon-process-claim">一般纳税人信息参考模板</a>
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
										<td class="searchtitle">客户名称</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:128,required:false,string:true}' style="width:100px"
											name='customername' id='customername' value='${customername}'></td>
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
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:2%;">序号</td>
					<td style="width:20%;">客户名称</td>
					<td style="width:20%;">开户银行名称</td>
					<td style="width:20%;">开户银行帐号</td>
					<td style="width:10%;">申请开票</td>
					<td style="width:5%;">操作</td>
				</tr>
				<s:iterator value="invoiceKhList" status="status">
					<tr class="cell">
						<td><s:property value="ROWNUM"/></td>
						<td>
							<s:property value="CUSTOMERNAME"/>
						</td>
						<td><s:property value="BANKNAME" /></td>
						<td><s:property value="BANKACCOUNT" /></td>
						<td aria-describedby="iform_grid__OPERATE" title="申请开票" style="" role="gridcell">
							<a href="javascript:void(0)" style="color: #0000ff;" onclick="addSqkp('<s:property value='CUSTOMERNO'/>','<s:property value='CUSTOMERNAME'/>',<s:property value='kpformid'/>,<s:property value='kpid'/>)">
								申请开票
							</a>
						</td>
						<td aria-describedby="iform_grid__OPERATE" title="编辑" style="" role="gridcell">
							&nbsp;
							<a href="javascript:void(0)" style="color: #0000ff;" onclick="openFormPage(<s:property value='INSTANCEID'/>,<s:property value='khformid'/>,<s:property value='khid'/>)">
								编辑
							</a>
							&nbsp;|&nbsp;
							<a href="javascript:void(0)" style="color: #0000ff;" onclick="removekh(<s:property value='INSTANCEID'/>)">
								删除
							</a>
						</td>
					</tr>
				</s:iterator>
			</table>
			<s:hidden name="khformid" id="khformid"></s:hidden>
			<s:hidden name="khid" id="khid"></s:hidden>
			<form action="zqb_kpxx_index.action" method="post" name="frmMain" id="frmMain">
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="customername" id="customername"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south" style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"	border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">
			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;">
				</div>
			</s:else>
		</div>
	</div>
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
