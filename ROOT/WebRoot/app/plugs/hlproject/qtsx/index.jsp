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
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"> </script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		$.ajaxSetup({async: false});
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
	    	var customerName = $("#CUSTOMERNAME").val();//客户名称
			var zqdm = $("#ZQDM").val();//股票代码
	        var seachUrl = encodeURI("hl_zqb_othermatters_index.action?customername="+customerName+"&zqdm="+zqdm);
	        window.location.href = seachUrl;
	    });
	    //复选/全选
        $("#chkAll").bind("click", function () {
        	$("[name =colname]:checkbox").attr("checked", this.checked);
        });
    });
		
		function editQtsx(instanceId,excutionId,taskId){
			var hlqtlcsplcServer = $("#hlqtlcsplcServer").val();
			var pageUrl = "loadProcessFormPage.action?actDefId="+hlqtlcsplcServer+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId
    		var target = "_blank";
    		var win_width = window.screen.width;
    		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
    		page.location = pageUrl;
		}
		function addQtsx(){			
			var hlqtlcsplcServer = $("#hlqtlcsplcServer").val();
			var pageUrl = "processRuntimeStartInstance.action?actDefId="+hlqtlcsplcServer;
    		var target = "_blank";
    		var win_width = window.screen.width;
    		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
    		page.location = pageUrl;
		}
		/* function remove(){
			$.messager.confirm('确认','确认删除?',function(result){ 
				if(result){
					var list = $('[name=colname]').length;
					var a=0;
					for( var n = 0; n < list; n++){
						var flag = new Boolean();
						if($('[name=colname]')[n].checked==false&&$('[name=colname]')[n].id!='chkAll'){
							a++;
							if(a==list){
								$.messager.alert('提示信息','请选择您要删除的行项目!','info');  
								return;
							}
						}
						//console.log(typeof ($('[name=colname]')[n].id)!=String('chkAll')+","+typeof ($('[name=colname]')[n].id));
						if($('[name=colname]')[n].checked==true&&String($('[name=colname]')[n].id)!=String('chkAll')){
							var deleteUrl = "deleteCustomer.action";
							$.post(deleteUrl,{instanceid:$('[name=colname]')[n].id},function(data){ 
								if(data=='success'){
									window.location.reload();
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
		} */
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
			<a href="javascript:addQtsx();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			<!-- <a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a> -->
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<%-- <form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px;text-align:center;">
				<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">事项名称</td>
										<td class="searchdata">
											<input type='text' class='{maxlength:128,required:false,string:true}' style="width:100px" name='CUSTOMERNAME' id='CUSTOMERNAME' value=''>
										</td>
										<td class="searchtitle">事项主要内容</td>
										<td class="searchdata">
											<input type='text' class='{maxlength:64,required:false,string:true}' style="width:100px" name='ZQDM' id='ZQDM' value=''>
										</td>
									</tr>
								</table>
							<td>
							<td valign='bottom' style='padding-bottom:5px;'>
								<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a>
							</td>
						<tr>
					</table>
				</div>
			</div>
			<span style="disabled:none">
				<input type="hidden" name="formid" value="88" id="formid" />
				<input type="hidden" name="demId" value="21" id="demId" />
				<input type="hidden" name="idlist" id="idlist" value='11'>
			</span>
		</form> --%>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:5%;" class="header"><input type="checkbox" name="colname" id="chkAll" /></td>
					<td style="width:5%;">序号</td>
					<td style="width:35%;">事项名称</td>
					<td style="width:35%;">事项主要内容</td>
					<td style="width:10%;">创建时间</td>
					<td style="width:5%;">审批状态</td>
					<td style="width:5%;">操作</td>
				</tr>
				<s:iterator value="list" status="status">
					<tr class="cell">
						<td style="width:05%;"><input type="checkbox"　id="<s:property value='INSTANCEID'/>" name="colname"　value="<s:property value='INSTANCEID'/>" /></td>
						<td><s:property value="#status.count" /></td>
						<td>
						<s:if test="SXMC.length() > 60">
							<s:property value="SXMC.substring(0,60)"/>...
						</s:if>
						<s:else>
							<s:property value="SXMC"/>
						</s:else>
						</td>
						<td>
						<s:if test="SXZYNR.length() > 60">
							<s:property value="SXZYNR.substring(0,60)"/>...
						</s:if>
						<s:else>
							<s:property value="SXZYNR"/>
						</s:else>
						</td>
						<td><s:property value="CJSJ" /></td>
						<td><s:property value="SPZT" /></td>
						<td>
							<a href="javascript:editQtsx('<s:property value='INSTANCEID'/>','<s:property value='LCBH'/>','<s:property value='TASKID'/>')"　style="color:blue;"><u>查看</u></a>
						</td>
					</tr>
				</s:iterator>
			</table>

			<form action="hl_zqb_othermatters_index.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="customerno" id="customerno"></s:hidden>
				<s:hidden name="zqdm" id="zqdm"></s:hidden>
			</form>
			<s:hidden name="hlqtlcsplcServer" id="hlqtlcsplcServer"></s:hidden>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south"
		style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
		border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">
			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		$("#CUSTOMERNAME").val($("#customername").val());
		$("#ZQDM").val($("#zqdm").val());
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