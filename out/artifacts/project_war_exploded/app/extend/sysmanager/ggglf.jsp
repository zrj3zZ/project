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
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
mainFormValidator =  $("#ifrmMain").validate({
 });
 mainFormValidator.resetForm();
});
	
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
	        var khbh = $("#khbh").val();
	        var gsmc = $("#GSMC").val();
	        var gslx = $("#GSLX").val();
	        var zch = $("#ZCH").val();
	        var zcdz = $("#ZCDZ").val();
	        var seachUrl = encodeURI("zqb_glf_gg_getglf.action?khbh="+khbh+"&gsmc="+gsmc+"&gslx="+gslx+"&zch="+zch+"&zcdz="+zcdz);
	        window.location.href = seachUrl;
	    });
    });
		
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
	<div region="north" border="false"></div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px">
				<div
					style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">公司名称</td>
										<td class="searchdata"><input type='text' class='{maxlength:128,required:false,string:true}' style="width:100px" name='GSMC' id='GSMC' value=''></td>
										<td class="searchtitle">公司类型</td>
										<td class="searchdata">
											<select name='GSLX' id='GSLX'>
													<option value=''>-空-</option>
													<option value='分公司'>分公司</option>
													<option value='子公司'>子公司</option>
													<option value='全资/控股子公司'>全资/控股子公司</option>
											</select>
										</td>
									</tr>
									<tr>
										<td class="searchtitle">注册号</td>
										<td class="searchdata"><input type='text' class='{maxlength:64,required:false,string:true}' style="width:100px" name='ZCH' id='ZCH' value=''></td>
										<td class="searchtitle">注册地址</td>
										<td class="searchdata"><input type='text' class='{maxlength:64,required:false,string:true}' style="width:100px" name='ZCDZ' id='ZCDZ' value=''></td>
									</tr>
								</table>
							<td>
							<td valign='bottom' style='padding-bottom:5px;'><a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px">


			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:5%;" class="header"><input type="checkbox" name="colname" id="chkAll" /></td>
					<td style="width:5%;">序号</td>
					<td style="width:20%;">公司名称</td>
					<td style="width:5%;">公司类型</td>
					<td style="width:5%;">注册号</td>
					<td style="width:10%;">注册地址</td>
					<td style="width:5%;">注册资本</td>
					<td style="width:10%;">法人代表</td>
					<td style="width:15%;">关联关系</td>
					<td style="width:10%;">生产经营范围</td>
					<!-- <td style="width:20%;">操作</td> -->
				</tr>
				<s:iterator value="glflist" status="status">
					<tr class="cell">
						<td style="width:05%;"><input type="checkbox" id="<s:property value='INSTANCEID'/>" name="colname" value="<s:property value='INSTANCEID'/>" /></td>
						<td><s:property value="#status.count" /></td>
						<td><s:property value="GSMC" /></td>
						<td><s:property value="GSLX" /></td>
						<td><s:property value="ZCH" /></td>
						<td><s:property value="ZCDZ" /></td>
						<td><s:property value="ZCZB" /></td>
						<td><s:property value="FRDB" /></td>
						<td><s:property value="GLGX" /></td>
						<td><s:property value="SCJYFW" /></td>
						<!-- <td>查看详情</td> -->
					</tr>
				</s:iterator>
			</table>

			<form action="zqb_glf_gg_getglf.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="khbh" id="khbh"></s:hidden>
				<s:hidden name="gsmc" id="gsmc"></s:hidden>
				<s:hidden name="gslx" id="gslx"></s:hidden>
				<s:hidden name="zch" id="zch"></s:hidden>
				<s:hidden name="zcdz" id="zcdz"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
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
	</div>
	<script type="text/javascript">
	$(function(){
        $("#GSMC").val($("#gsmc").val());
        $("#GSLX").val($("#gslx").val());
        $("#ZCH").val($("#zch").val());
        $("#ZCDZ").val($("#zcdz").val());
	});
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