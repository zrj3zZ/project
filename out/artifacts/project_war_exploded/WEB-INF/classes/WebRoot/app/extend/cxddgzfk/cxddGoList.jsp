<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>持续督导工作反馈统计</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/iwork/oaknow.css"/>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  charset="utf-8"  > </script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"  ></script>
<script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">

$(function() {
	$("#search").click(function(){
		var mainFormValidator;
		$().ready(function() {
			mainFormValidator =  $("#editForm").validate({});
			mainFormValidator.resetForm();
		});
        var status = $("#STATUS").val();
        var ggid = $("#ggid").val();
      	var seachUrl = encodeURI("zqb_cxdd_goList.action?status="+status+"&ggid="+ggid);
        window.location.href = seachUrl;
      });
});
function showCxddfkgzxx(userid,ggid){
	var pageUrl = "zqb_cxdd_customerListsub.action?ggid="+ggid+"&hfr="+userid;
	art.dialog.open(pageUrl,{
		title:'持续督导日常工作反馈',
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999',
		rang:true,
		width:1100,
		cache:false,
		lock: true,
		height:800, 
		iconTitle:false,
		extendDrag:true,
		autoSize:false,
		close:function(){
			window.location.reload();
		}
	});
}

function UnlockThis(hfr,ggid,i){
	$.post("zqb_cxdd_unlockthis.action", {
		hfr : hfr,
		ggid : ggid,
		lockstatus : i
		},function(data){
			window.location.reload();
	});
}
function LockThis(hfr,ggid,i){
	$.post("zqb_cxdd_lockthis.action", {
		hfr : hfr,
		ggid : ggid,
		lockstatus : i
		},function(data){
			window.location.reload();
	});
}

window.onbeforeunload = function() {
	if(is_form_changed()) {
	return "您的修改内容还没有保存，您确定离开吗？";
	}
	}
	function is_form_changed() {
	var t_save = jQuery("#savebtn"); //检测页面是否要保存按钮

	if(t_save.length>0) { //检测到保存按钮,继续检测元素是否修改
	var is_changed = false;
	jQuery("#border input, #border textarea, #border select").each(function() {
	var _v = jQuery(this).attr('_value');
	if(typeof(_v) == 'undefined') _v = '';
	if(_v != jQuery(this).val()) is_changed = true;
	});
	return is_changed;
	}
	return false;
	}
	jQuery(document).ready(function(){
	jQuery("#border input, #border textarea, #border select").each(function() {
	jQuery(this).attr('_value', jQuery(this).val());
	});
	}); 
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
	<div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
	<form action="zqb_cxdd_goList.action" method=post name=frmMain id=frmMain >
		<div style="padding:5px">
			<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
				<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
					<tr> 
					  <td style='padding-top:10px;padding-bottom:10px;'> 
						<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
							<tr> 
								<!-- <td class="searchtitle" style="text-align:right;">公司代码 </td> 
								<td class="searchdata">
									<input type='text' class='{maxlength:128,required:false}' style="width:100px" name='ZQDM' id='ZQDM' value=''> 
								</td> -->
								<td class="searchtitle">回复状态 </td> 
								<td class="searchdata">
									<select name='STATUS' id='STATUS' >
									<option value=''>-空-</option>
									<option value='未回复'>未回复</option>
									<option value='已回复'>已回复</option>
									</select>
								</td>
							</tr> 
						</table>
					  </td>
					  <td valign='bottom' style='padding-bottom:5px;'> <a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);" >查询</a></td>
					</tr>
				</table> 
			</div>
		</div>
	</form>
		<s:form id="editForm" name="editForm" theme="simple"></s:form>
			<!--表单参数-->
			<div id="border">
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
					cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td class="formpage_title">持续督导工作反馈</td>
						</tr>
						<tr>
							<td id="help" align="right"><br /></td>
						</tr>
						<tr>
							<td class="line" align="right"><br /></td>
						</tr>
						<tr>
							<td align="left">
								<table class="ke-zeroborder" border="0" cellpadding="0"
									cellspacing="0" width="100%">
									<tbody>
										<table id='iform_grid'  width="100%" style="border:1px solid #efefef;table-layout:fixed;">
												<tr><td colspan="4"><font color="red" size="4">未反馈用户数量：<s:property value="noFeedBackTotal"/></font></td></tr>
												<tr  class="header">
									                  <td>回复人</td>
									                  <td>回复时间</td>
									                  <td>状态</td>
									                  <td>锁定状态</td>
									            </tr>
								                <s:iterator value="list" status="ll">
								                <tr class="cell">
								                	<td><s:property value="XM"/></td>
									                <td><s:property value="HFSJ"/></td>
									                <td style="text-align:center;">
										                <s:if test="STATUS=='已回复'">
										                	<a href="javascript:showCxddfkgzxx('<s:property value="USERID"/>',<s:property value="ggid"/>);" style="color:blue;"><s:property value="STATUS"/></a>
										                </s:if>
										                <s:else>
										                	<s:property value="STATUS"/>
										                </s:else>
								                	</td>
									                <td>
										                <s:if test="STATUS=='已回复'">
										                	<s:if test="LOCKSTATUS==1">
										                		<a href="javascript:UnlockThis('<s:property value="USERID"/>',<s:property value="ggid"/>,0);" style="color:blue;">
										                			已锁定
										                		</a>
										                	</s:if>
										                	<s:else>
										                		<a href="javascript:LockThis('<s:property value="USERID"/>',<s:property value="ggid"/>,1);" style="color:blue;">
										                			未锁定
										                		</a>
										                	</s:else>
										                </s:if>
										                <s:else>
										                </s:else>
								                	</td>
									            </tr>
												</s:iterator>
											</table>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<input type="hidden" id="ggid" name="ggid" value="${ggid}" class="{string:true}"/>
			<s:hidden name="zqdm" id="zqdm"></s:hidden>
			<s:hidden name="status" id="status"></s:hidden>
	</div>
	<script type="text/javascript">
	$(function(){
		$("#ZQDM").val($("#zqdm").val());
		$("#STATUS").val($("#status").val()); 
	});
	</script>
</body>
</html>
<script language="JavaScript">
	jQuery.validator.addMethod("string", function(value, element) {
		var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
		var patrn=/[“”`~!#$%^&*+<>?"{},;'[]（）—。[\]]/im;
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
