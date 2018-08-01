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
	$.ajaxSetup({
		async: false
	});
	mainFormValidator =  $("#ifrmMain").validate({});
	mainFormValidator.resetForm();
});
	function addItem(){
		var cfjlid = $("#cfjlid").val();
		var cfjlformid = $("#cfjlformid").val();
		var pageUrl = "createFormInstance.action?formid="+cfjlformid+"&demId="+cfjlid;
		/*art.dialog.open(pageUrl,{
			title:'挂牌企业处罚记录表单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:650, 
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
	$(function(){
		$('#pp').pagination({  
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
	});
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	function edit(instanceid){
		var cfjlid = $("#cfjlid").val();
		var cfjlformid = $("#cfjlformid").val();
		var pageUrl = "openFormPage.action?formid="+cfjlformid+"&demId="+cfjlid+"&instanceId="+instanceid;
		/*art.dialog.open(pageUrl,{
			title:'挂牌企业处罚记录表单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:650, 
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
	$(function(){
		//查询
	    $("#search").click(function(){
	    	var valid = mainFormValidator.form(); //执行校验操作
	    	if(!valid){
	    		return;
	    	}
		    var zqdm = $("#ZQDM").val();
		    var zqjc = $("#ZQJC").val();
		    var fssjstart = $("#FSSJSTART").val();
		    var fssjend = $("#FSSJEND").val();
		    var cfqksm = $("#CFQKSM").val();
		    var pagesize=$("#pageSize").val();
			var pagenumber=$("#pageNumber").val();
		    var seachUrl = encodeURI("zqb_gpqycfjl_index.action?zqdm="+zqdm+"&zqjc="+zqjc+"&fssjstart="+fssjstart+"&fssjend="+fssjend+"&cfqksm="+cfqksm+"&pageSize="+pagesize+"&pageNumber="+pagenumber);
		    window.location.href = seachUrl;
	    });
	    /* $("#chkAll").bind("click", function(){
	    	$("[name =colname]:checkbox").attr("checked", this.checked);
	    }); */
	});
	function remove(insid) {
	
		  
		$.messager.confirm('确认','确认删除?',function(result){
					if(result){
					var deleteUrl = "xcjclv_del.action";
					$.post(
									deleteUrl,
									{
										instanceid : insid
									},
									function(data) {
										if (data == 'success') {
											window.location
													.reload();
										} else {
											alert(data);
										}
									});
									}
				}); 
	}
		
	function uploadItem(){
				var startdate=$("#FSSJSTART").val();
				var enddate=$("#FSSJEND").val();
				var cfqksm=$("#CFQKSM").val();
				var zqjc=$("#ZQJC").val();
				var zqdm=$("#ZQDM").val();
				var pageUrl = encodeURI("dgcfjlToExcl.action?startdate="+startdate+"&enddate="+enddate+"&cfqksm="+cfqksm+"&zqjc="+zqjc+"&zqdm="+zqdm);
				window.location.href = pageUrl;
			
			}
	</script>
<script type="text/javascript">
	var zqdm;
    //预加载
    jQuery(function($) {
    	zqdm = $("#ZQDM").val();
        //给txtbox绑定键盘事件--键盘松开事件
        $("#ZQDM").bind("keyup", function() {
            var currentValue = $(this).val();
            if (currentValue != zqdm) {
            	zqdm = currentValue;
                TxtChange();
            }
        });
    });
    //目标选择框文本发生更改时
    function TxtChange() {
    	var html = "<option value=''>-空-</option>";
    	if(zqdm != null && zqdm != ''){
    		$.post("zqb_gpqycfjl_get_zqdmzqjclist.action",{zqdm:zqdm},function(data){
    			if(data == null || data == ''){
    				document.getElementById("selectzqdm").style.display="none";
    			}else{
    				var values = data.split(",");
        		    for (var i = 0; i < values.length; i++) {
        		    	if(values[i] != null && values[i] != ''){
        		    		var valuessub = values[i].split("-");
        		    		html+="<option value='"+valuessub[0]+"'>"+valuessub[1]+"</option>";
        		    	}
    				}
        		    $("#selectzqdm").html(html);
        		    document.getElementById("selectzqdm").style.display="block";
    			}
    		});
		}           
    }
    function setzqdm() {
    	$("#ZQDM").val($("#selectzqdm").val());
    	$("#ZQJC").val($("#selectzqdm").find("option:selected").text());
    	document.getElementById("selectzqdm").style.display="none";
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
			<!-- <a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a> -->
			<a href="javascript:uploadItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">导出</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>

		</div>
	</div>
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
										<td class="searchtitle">证券代码</td>
										<td class="searchdata" style="width:100px;">
											<input type='text' class='{string:true,maxlength:6,required:false}' style="width:100px;" name='ZQDM' id='ZQDM' value='' onchange="getzqmdzqjcList();">
										</td>
										<td><select onchange='setzqdm();' id="selectzqdm" name="selectzqdm" style="width:100px;display:none;height:23px;"></select></td>
										<td class="searchtitle">证券简称</td>
										<td class="searchdata"><input type='text' class='{string:true,maxlength:12,required:false}' style="width:100px" name='ZQJC' id='ZQJC' value=''></td>
										<td class="searchtitle">处罚时间</td>
								<td class="searchdata">
									<input type='text' onfocus="var ENDDATE=$dp.$('FSSJEND');WdatePicker({onpicked:function(){ENDDATE.focus();},maxDate:'#F{$dp.$D(\'FSSJEND\')}'})" class='{maxlength:64,required:false}' style="width:80px;" name='FSSJSTART' id='FSSJSTART' value=''>
									到
									<input type='text' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'FSSJSTART\')}'})" class='{maxlength:64,required:false}' style="width:80px;" name='FSSJEND' id='FSSJEND' value=''>
								</td>
										<td class="searchtitle">处罚情况说明</td>
										<td class="searchdata"><input type='text' class='{string:true,maxlength:64,required:false}' style="width:250px" name='CFQKSM' id='CFQKSM' value=''></td>
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
		</form>
		<div style="padding:5px">
			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">
					<!-- <td style="width:5%;" class="header"><input type="checkbox" name="colname" id="chkAll" /></td> -->
					<td style="width:5%;">序号</td>
					<td style="width:10%;cursor:pointer;" onclick="OrderGPSJ();">证券代码</td>
					<td style="width:15%;cursor:pointer;" onclick="OrderZQDM();">证券简称</td>
					<td style="width:15%;">发生时间</td>
					<td style="width:50%;">处罚情况说明</td>
					<td style="width:15%;text-align: center;">操作</td>
				</tr>
				<s:iterator value="compantRecordList" status="status">
					<tr class="cell">
						<%-- <td style="width:05%;"><input type="checkbox" id="<s:property value=''/>" name="colname" value="<s:property value=''/>" /></td> --%>
						<td><s:property value="#status.count" /></td>
						<td><s:property value="ZQDM" /></td>
						<td><s:property value="ZQJC" /></td>
						<td><s:property value="FSSJ" /></td>
						<s:if test="CFQKSM.length()>=70">
							<td title="<s:property value="CFQKSM" />"><s:property value="%{CFQKSM.substring(0,70)}" />...</td>
						</s:if>
						<s:else>
							<td><s:property value="CFQKSM" /></td>
						</s:else>
						<td style="text-align: center;">
							<a href="javascript:remove('<s:property value="INSTANCEID"/>');" style="color:blue;"><u>删除</u></a>
							&nbsp;|&nbsp;
							
							<a href="javascript:edit('<s:property value="INSTANCEID"/>')" style="color:blue;"><u>查看详情</u></a>
						</td>
					</tr>
				</s:iterator>
			</table>

			<form action="zqb_gpqycfjl_index.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="cfjlid" id="cfjlid"></s:hidden>
				<s:hidden name="cfjlformid" id="cfjlformid"></s:hidden>
				<s:hidden name="zqdm" id="zqdm"></s:hidden>
				<s:hidden name="zqjc" id="zqjc"></s:hidden>
				<s:hidden name="fssjstart" id="fssjstart"></s:hidden>
				<s:hidden name="fssjend" id="fssjend"></s:hidden>
				<s:hidden name="cfqksm" id="cfqksm"></s:hidden>
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
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		$("#ZQDM").val($("#zqdm").val());
		$("#ZQJC").val($("#zqjc").val());
		$("#FSSJSTART").val($("#fssjstart").val());
		$("#FSSJEND").val($("#fssjend").val());
		$("#CFQKSM").val($("#cfqksm").val());
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
						alert("str:"+tmp+"s:"+str);
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>