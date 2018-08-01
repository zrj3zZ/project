<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信披自查反馈统计</title>
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
var api = frameElement.api, W = api.opener;
var mainFormValidator;
$().ready(function() {
	mainFormValidator =  $("#editForm").validate({});
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
function chaxun(){
		var dqrq=$("#dqrq").val();
        var zqdm = $("#ZQDM").val();
        var ggid = $("#ggid").val();
        var status = $("#STATUS").val();
        var sszjj = $("#SSZJJ").val();
        var ssbm = $("#SSBM").val();
      	var seachUrl = encodeURI("zqb_vote_goList.action?ggid="+ggid+"&zqdm="+zqdm+"&status="+status+"&sszjj="+sszjj+"&ssbm="+ssbm+"&dqrq="+dqrq);
        window.location.href = seachUrl;
      }
      
function submitMsg(pageNumber, pageSize) {
	$("#pageNumber").val(pageNumber);
	$("#pageSize").val(pageSize);
	$("#frmMain").submit();
	return;
}
function showCustomer(customerno,ggid){
	var dqrq=$("#dqrq").val();
	var url = 'zqb_vote_customerList.action?khbh='+customerno+'&ggid='+ggid+'&dqrq='+dqrq;
	var target = "_blank";
	var win_width = window.screen.width;
	var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	page.location = url;
	return;
}
function showCommunicate(customerno,ggid){
	var pageUrl = "showCommunicate.action?customerno="+customerno+"&ggid="+ggid;
	art.dialog.open(pageUrl,{ 
		title:'信披自查反馈沟通',
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999',
		rang:true,
		width:900,
		cache:false,
		lock: true,
		height:600, 
		iconTitle:false,
		extendDrag:true,
		autoSize:false,
		close:function(){
			window.location.reload();
		}
	});
}

function updateCommunicate(customerno,ggid){
	var pageUrl = "updateCommunicate.action?customerno="+customerno+"&ggid="+ggid;
	art.dialog.open(pageUrl,{ 
		title:'信披自查反馈沟通',
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999',
		rang:true,
		width:900,
		cache:false,
		lock: true,
		height:600, 
		iconTitle:false,
		extendDrag:true,
		autoSize:false,
		close:function(){
			window.location.reload();
		}
	});
}
function UnlockThis(customerno,ggid,i){
	$.post("zqb_vote_unlockthis.action", {
		customerno : customerno,
		ggid : ggid,
		lockstatus : i
		},function(data){
			window.location.reload();
	});
}
function selectAll(){
			if($("input[name='chk_list']").attr("checked")){
				$("input[name='chk_list']").attr("checked",true);
			}else{
				$("input[name='chk_list']").attr("checked",false);
			}
		}
		//锁定选中选中项
function sdxzx(ggid,j){

		var obj=document.getElementsByName('chk_list'); //选择所有name="'test'"的对象，返回数组
		var customerno='';
		for(var i=0; i<obj.length; i++){ 
			if(obj[i].checked) 
				customerno+="\'"+obj[i].value+"\'"+','; //如果选中，将value添加到变量s中
			}
		customerno = customerno.substring(0, customerno.length-1);	
		$.post("zqb_vote_lockmore.action", {
		customerno : customerno,
		ggid : ggid,
		lockstatus : j
		},function(data){
			window.location.reload();
	});
}
		
		
		
function LockThis(customerno,ggid,i){
	$.post("zqb_vote_lockthis.action", {
		customerno : customerno,
		ggid : ggid,
		lockstatus : i
		},function(data){
			window.location.reload();
	});
}

	window.onbeforeunload = function() {
		if (is_form_changed()) {
			return "您的修改内容还没有保存，您确定离开吗？";
		}
	}
	function is_form_changed() {
		var t_save = jQuery("#savebtn"); //检测页面是否要保存按钮

		if (t_save.length > 0) { //检测到保存按钮,继续检测元素是否修改
			var is_changed = false;
			jQuery("#border input, #border textarea, #border select").each(
					function() {
						var _v = jQuery(this).attr('_value');
						if (typeof (_v) == 'undefined')
							_v = '';
						if (_v != jQuery(this).val())
							is_changed = true;
					});
			return is_changed;
		}
		return false;
	}
	jQuery(document).ready(
			function() {
				jQuery("#border input, #border textarea, #border select").each(
						function() {
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
	<form action="zqb_vote_goList.action" method=post name=frmMain id=frmMain >
		<div style="padding:5px">
			<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
				<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
					<tr> 
					  <td style='padding-top:10px;padding-bottom:10px;'> 
						<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
							<tr> 
								<td class="searchtitle" style="text-align:right;">公司代码 </td> 
								<td class="searchdata">
									<input type='text' class='{maxlength:128,required:false}' style="width:100px" maxlength="128" name='ZQDM' id='ZQDM' value='<s:property value="zqdm"/>'> 
								</td>
								<td class="searchtitle" style="text-align:right;">所属证监局</td> 
								<td class="searchdata">
									<input type='text' class='{maxlength:128,required:false}' style="width:100px" maxlength="128" name='SSZJJ' id='SSZJJ' value='<s:property value="sszjj"/>'> 
								</td>
								<td class="searchtitle" style="text-align:right;">所属部门 </td> 
								<td class="searchdata">
									<input type='text' class='{maxlength:128,required:false}' style="width:100px" maxlength="128" name='SSBM' id='SSBM' value='<s:property value="ssbm"/>'> 
								</td>
								<td class="searchtitle">反馈状态 </td> 
								<td class="searchdata">
									<select name='STATUS' id='STATUS' >
									<option value=''>-空-</option>
									<option value='未回复'>未反馈</option>
									<option value='已回复'>已反馈</option>
									</select>
								</td>
							</tr> 
						</table>
					  </td>
					  <td valign='bottom' style='padding-bottom:5px;height:30px;line-hieght:30px;vertical-align:middle;' > <a id="search" class="easyui-linkbutton" onclick="chaxun()" href="javascript:void(0);" >查询</a></td>
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
							<td class="formpage_title">信披自查反馈统计</td>
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
										<table id='iform_grid'  width="100%" style="border:1px solid #efefef;">
												<tr><td colspan="7"><font color="red" size="4">未反馈客户数量：<s:property value="noFeedBackTotal"/></font></td></tr>
												<tr><td colspan="10"><div style="float:right;"><a class="easyui-linkbutton" href="javascript:sdxzx(<s:property value="ggid"/>,1)">锁定选中项</a></div></td></tr>
												<tr  class="header">
									                  <td width="5%"><input type="checkbox" name="chk_list" onclick="selectAll()">&nbsp;&nbsp;客户名称</td>
									                  <td width="5%">股票代码</td>
									                  <td width="10%">客户联系人</td>
									                  <td width="10%">客户联系电话</td>
									                  <td width="10%">客户邮箱</td>
									                  <td width="15%">所属证监局</td>
									                  <td width="15">所属部门</td>
									                  <td width="10%">沟通内容</td>
									                  <td width="5%">状态</td>
									                  <td width="5%">锁定状态</td>
									            </tr>
								                <s:iterator value="list" status="ll">
								                <tr class="cell">
								                	<s:if test="STATUS=='已反馈'&&READONLY==true">
								                		<td><font color="red"><input type="checkbox" name="chk_list" checked="checked" value="<s:property value="CUSTOMERNO"/>">&nbsp;&nbsp;<s:property value="ZQJC"/></font></td>
								                		<td><font color="red"><s:property value="ZQDM"/></font></td>
										                <td><font color="red"><s:property value="USERNAME"/></font></td>
										                <td><font color="red"><s:property value="TEL"/></font></td>
										                <td><font color="red"><s:property value="EMAIL"/></font></td>
										                <td><font color="red"><s:property value="SSZJJ"/></font></td>
										                <td><font color="red"><s:property value="SSBM"/></font></td>
										                <td title="<s:property value="GTCONTENT"/>"><font color="red"><s:if test="GTCONTENT.length()>10"><s:property value="GTCONTENT.substring(0,10)+'...'"/></s:if><s:else><s:property value="GTCONTENT"/></s:else></font></td>
								                	</s:if>
								                	<s:else>
								                	<s:if test="STATUS=='已反馈'">
								                		<td><input type="checkbox" name="chk_list" checked="checked" value="<s:property value="CUSTOMERNO"/>">&nbsp;&nbsp;<s:property value="ZQJC"/></td>
								                		</s:if>
								                		<s:else>
								                		<td><input type="checkbox" name="chk_list" value="<s:property value="CUSTOMERNO"/>">&nbsp;&nbsp;<s:property value="ZQJC"/></td>
								                		</s:else>
										                <td><s:property value="ZQDM"/></td>
										                <td><s:property value="USERNAME"/></td>
										                <td><s:property value="TEL"/></td>
										                <td><s:property value="EMAIL"/></td>
										                <td><s:property value="SSZJJ"/></font></td>
										                <td><s:property value="SSBM"/></font></td>
										                <td title="<s:property value="GTCONTENT"/>"><s:if test="GTCONTENT.length()>10"><s:property value="GTCONTENT.substring(0,10)+'...'"/></s:if><s:else><s:property value="GTCONTENT"/></s:else></td>
								                	</s:else>
									                <s:if test="STATUS=='未反馈'">
									                	<td><s:property value="STATUS"/></td>
									                	<s:if test="LOCKSTATUS==1 ">
										                	<td>
										                		<a href="javascript:UnlockThis('<s:property value="CUSTOMERNO"/>',<s:property value="ggid"/>,0);" style="color:blue;">
										                			已锁定
										                		</a>
										                	</td>
										                </s:if>
										                <s:elseif test="LOCKSTATUS!=1 ">
										                	<td>
										                		<a href="javascript:LockThis('<s:property value="CUSTOMERNO"/>',<s:property value="ggid"/>,1);" style="color:blue;">
										                			未锁定
										                		</a>
										                	</td>
										                </s:elseif>
									                	
									                </s:if>
									                <s:elseif test="STATUS=='已反馈'&&READONLY==true">
									                	<s:if test="GTSTATUS==''">
									                	<td style="text-align:center;">
									                		<a href="javascript:showCustomer('<s:property value="CUSTOMERNO"/>',<s:property value="ggid"/>);" style="color:blue;">查看信披反馈</a>
									                		<s:if test="roleid!=5&&roleid!=9">
									                			</br>&nbsp;(<a href="javascript:showCommunicate('<s:property value="CUSTOMERNO"/>',<s:property value="ggid"/>);" style="color:blue;">确认沟通</a>)
									                		</s:if>
									                	</td>
									                	</s:if>
									                	<s:else>
									                	<td style="text-align:center;">
									                		<a href="javascript:showCustomer('<s:property value="CUSTOMERNO"/>',<s:property value="ggid"/>);" style="color:blue;">查看信披反馈</a>
									                		<s:if test="roleid!=5&&roleid!=9">
									                			</br>&nbsp;(<a href="javascript:updateCommunicate('<s:property value="CUSTOMERNO"/>',<s:property value="ggid"/>);" style="color:blue;"><s:property value="GTSTATUS" /></a>)
									                		</s:if>
									                	</td>
									                	</s:else>
									                </s:elseif>
									                <s:else>
									                	<td><a href="javascript:showCustomer('<s:property value="CUSTOMERNO"/>',<s:property value="ggid"/>);" style="color:blue;"><s:property value="STATUS"/></a></td>
									                </s:else>
									                <s:if test="LOCKSTATUS==1 && STATUS=='已反馈'">
									                	<td>
									                		<a href="javascript:UnlockThis('<s:property value="CUSTOMERNO"/>',<s:property value="ggid"/>,0);" style="color:blue;">
									                			已锁定
									                		</a>
									                	</td>
									                </s:if>
									                <s:elseif test="LOCKSTATUS!=1 && STATUS=='已反馈'">
									                	<td>
									                		<a href="javascript:LockThis('<s:property value="CUSTOMERNO"/>',<s:property value="ggid"/>,1);" style="color:blue;">
									                			未锁定
									                		</a>
									                	</td>
									                </s:elseif>
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
			<input type="hidden" id="ggid" name="ggid" value="${ggid}" class="{string:true}" />
			<s:hidden name="zqdm" id="zqdm"></s:hidden>
			<s:hidden name="status" id="status"></s:hidden>
			<input type="hidden" id="dqrq" name="dqrq" value="<s:property value="dqrq"/>" />
	</div>
	<script type="text/javascript">
	$(function(){
		$("#ZQDM").val($("#zqdm").val());
		$("#STATUS").val($("#status").val()); 
	});
	</script>
</body>
</html>