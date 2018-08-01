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
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
		mainFormValidator =  $("#ifrmMain").validate({
		 });
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
		$("#frmMain").submit();
		return;
	}
	$(function() {
		//查询
		$("#search").click(
				function() {
					var valid = mainFormValidator.form(); //执行校验操作
					if(!valid){
						return false;
					}
					var tzbt = $("#TZBT").val();
					var spzt = $("#SPZT").val();
					var seachUrl = encodeURI("zqb_announcement_gzsc.action?tzbt="+tzbt+"&spzt="+spzt);
					window.location.href = seachUrl;
				});
	});
	
	function addItem(){
		var gzscformid = $("#gzscformid").val();
		var gzscdemid = $("#gzscdemid").val();
		var pageUrl = "createFormInstance.action?formid="+gzscformid+"&demId="+gzscdemid;
		/*art.dialog.open(pageUrl,{ 
			title:'工作备查',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:580, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});*/
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}	
	function delGZSC(cid){
		if(confirm("确定执行删除操作吗?")) {
			var pageUrl = "delGZSC.action";
			$.post(pageUrl,{id:cid},function(data){ 
		       			if(data=='success'){
		       				window.location.reload();
		       			}else{
		       				alert("删除失败");
		       			} 
		     }); 
		}
	}
	function showGZSC(instanceid){
		var gzscformid = $("#gzscformid").val();
		var gzscdemid = $("#gzscdemid").val();
		var pageUrl = "openFormPage.action?formid="+gzscformid+"&demId="+gzscdemid+"&instanceId="+instanceid+"&isDialogDisabled=0"+"&isHFRandHFNRdiaplsy=1";
		/* art.dialog.open(pageUrl,{
			title:'工作备查查看',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:580, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		}); */
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	function showHF(cid){
		var pageUrl = "zqb_announcement_checkHF.action?id="+cid;
		art.dialog.open(pageUrl,{
			title:'部门通知已回复人员',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:850,
			cache:false,
			lock: false,
			height:450, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			zindex:100,
			stack:false,
			position:'absolute',
			close:function(){
				window.location.reload();
			}
		});
	}
	function xftz(){
		var chk_value =[]; 
			$('input[name="chk_list"]:checked').each(function(){ 
			chk_value.push($(this).val()); 
		}); 
		if(chk_value.length>0){
			$.messager.confirm('确认','确认通知反馈人员?',function(result){
				if(result){
					$('input[name=chk_list]').each(function(){
						var obj_check = $(this).attr('checked');
						if(obj_check){
							var id= $(this).val();
							$.post('fsp_gzsc_sendddmail.action',{gzscinstanceid:id},
							function(data){
								if(data=="success"){
									alert("下发成功！");
								}
								if(data=="该记录没有事项反馈人员！"){
									alert(data)
								}
							});
						}
					});
				}
			});
		}else{
			alert("未勾选项!");
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
	<div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<a id="addDdsx" href="javascript:xftz();" class="easyui-linkbutton" plain="true" iconCls="icon-add">下发短信通知</a>
		</div>
	</div>
	<div region="center"style="padding-left:0px;padding-right:0px;border:0px;background-position:top;">
		<form name='ifrmMain' id='ifrmMain' method="post" style="display:none;">
			<div style="padding:5px">
				<div
					style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">反馈状态</td>
										<td>
											<select name="SPZT" id="SPZT">
											<option value="" >-空-</option>
											<option value="0">未完成</option>
											<option value="1">完成</option>
											</select>
										</td>
									</tr>
								</table>
							</td>
							<td valign='bottom' style='padding-bottom:15px;'><a
								id="search" class="easyui-linkbutton" icon="icon-search"
								href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px">


			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:5px"><input style="display:none;" type="checkbox" name="chk_list" onclick="selectAll()"></td>
					<td style="width:20%;">通知标题</td>
					<td style="width:45%;">通知内容</td>
					<td style="width:25%;">发送时间</td>
					<td style="width:5%;">操作</td>
				</tr>
				<s:iterator value="list" status="status">
					<tr class="cell">
						<td style="width:5px"><input type="checkbox" name="chk_list" value="<s:property value="INSTANCEID"/>"></td>
						<td style="width:20%;"><s:property value="TZBT" /></td>
						<td style="width:45%;">
								<s:if test="TZNR.length() > 60">
									<s:property value="TZNR.substring(0,60)"/>...
								</s:if>
								<s:else>
									<s:property value="TZNR"/>
								</s:else>
						</td>
						<td style="width:15%;"><s:property value="FSSJ" /></td>
						<td style="width:15%;">
							<a href="javascript:showGZSC('<s:property value='INSTANCEID'/>')">查看</a>&nbsp;|&nbsp;
							<a href="javascript:showHF('<s:property value='CID'/>')">查看回复情况</a>&nbsp;|&nbsp;
							<a href="javascript:delGZSC('<s:property value='CID'/>')">删除</a>
							<%-- &nbsp;&nbsp;&nbsp;&nbsp;
							<a href="javascript:noticeUpload('<s:property value='ID'/>')">资料下载</a>
							<s:if test="FSZT!='完成'">&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="javascript:commitNotice('<s:property value='ID'/>')">完成</a>
							</s:if>&nbsp;&nbsp;&nbsp;&nbsp; --%>
							<%-- <a href="javascript:removeNotice('<s:property value='ID'/>')">删除</a> --%>
						</td> 
					</tr>
				</s:iterator>
			</table>
			<form action="zqb_announcement_gzsc.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="tzbt" id="tzbt"></s:hidden>
				<s:hidden name="spzt" id="spzt"></s:hidden>
				<s:hidden name="gzscformid" id="gzscformid"></s:hidden>
				<s:hidden name="gzscdemid" id="gzscdemid"></s:hidden>
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
	<script type="text/javascript">
	$(function(){
		$("#TZBT").val($("#tzbt").val());
		$("#SPZT").val($("#spzt").val());
	});
	</script>
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