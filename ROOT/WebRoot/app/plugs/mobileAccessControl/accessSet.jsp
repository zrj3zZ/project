<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
		<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		<script type="text/javascript">		
		function submitAccessSet(){

			var isVisit = $('input:radio[name=isVisit]:checked').val();
			var visitType = $('input:radio[name=visitType]:checked').val();
			var isBind = $('input:radio[name=isBind]:checked').val();
			var deviceIds = $('#deviceIds').val();
			var userId = $("#userId").val();
			
			$.post("submitUserAccess.action",
				{userId:userId, isVisit:isVisit, visitType:visitType, isBind:isBind, deviceIds:deviceIds},
				function(data){
					alert(data);
				});	
		}
		
		function openLogGrid(){
			var pageUrl = "userMobileBindIndex.action?userId=" + $("#userId").val();
			art.dialog.open(pageUrl,{
				id:'accessBaseWinDiv',
				cover:true,
				title:'登陆日志',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:520,
				cache:false,
				lock: true,
				esc: true,
				height:350,
				iconTitle:false,
				extendDrag:true,
				autoSize:true
			});
		}
		
		</script>
</head>
	<body class="easyui-layout">
	
	<div region="west"  split="false" border="false"  style="height:100%;width:430px;padding-left:5px;overflow:hidden; border-top:1px solid #F9FAFD">
		<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="tree_top"></td>
			</tr>
			<tr>
				<td class="tree_main">
					<table>
						<tr>
							<td>
								是否允许登陆:
							</td>
							<td>
								<label><input type="radio" name="isVisit" value="1"/> 是</label>
								<label><input type="radio" name="isVisit" id="isVisit2" value="0"/> 否</label>
							</td>
						</tr>
						<tr>
							<td>
								登录方式:
							</td>
							<td>
								<label><input type="radio" name="visitType" value="common"/> 普通登陆</label>
								<label><input type="radio" name="visitType" value="anmeng"/> 令牌登陆</label>
							</td>
						</tr>
						<tr>
							<td>
								是否绑定设备:
							</td>
							<td>
								<label><input type="radio" name="isBind" value="1"/> 是</label>
								<label><input type="radio" name="isBind" value="0"/> 否</label>
							</td>
						</tr>
						<tr>
							<td>
								绑定设备:
							</td>
							<td>
								<textarea id="deviceIds"  style="width: 210px; height: 153px;" ></textarea> 
								<a href="javascript:openLogGrid();" class="easyui-linkbutton" plain="true">从登陆日志中选取</a>
							</td>
						</tr>
						<tr>
							<td>
								最后登录时间:
							</td>
							<td>
								<s:property value='userLoginTime' escapeHtml='false' />
							</td>
						</tr>
						<tr>
							<td>
								最后登录次数:
							</td>
							<td>
								<s:property value='userLoginCount' escapeHtml='false' />
							</td>
						</tr>
						<tr>
							<td>
								
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
					</table>
					<div style="padding:5px;text-align:right; ">
						<a href="javascript:submitAccessSet();" class="easyui-linkbutton"  iconCls="icon-ok">确定</a>
					</div>
				</td>
			</tr>
			<tr>
				<td class="tree_bottom"></td>
			</tr>
		</table>
    </div>
	<s:hidden name="userId" id="userId" />
	
<script type="text/javascript">
	var isVisit = "<s:property value='mobileVisitset.isVisit' escapeHtml='false' />";
	var visitType = "<s:property value='mobileVisitset.visitType' escapeHtml='false' />";
	var isBind = "<s:property value='mobileVisitset.isBind' escapeHtml='false' />";
	var deviceIds = "<s:property value='mobileVisitset.deviceIds' escapeHtml='false' />";
	var opeTime = "<s:property value='mobileVisitset.opeTime' escapeHtml='false' />";
	
	if($("#userId").val().length > 0){
		
		$('input:radio[name=isVisit]').each(function(){
			if(isVisit == $(this).val()){
				$(this).attr("checked","checked");
			}
		});
		
		$('input:radio[name=visitType]').each(function(){
			if(visitType == $(this).val()){
				$(this).attr("checked","checked");
			}
		});
		
		$('input:radio[name=isBind]').each(function(){
			if(isBind == $(this).val()){
				$(this).attr("checked","checked");
			}
		});
			
		$("#deviceIds").html(deviceIds);
	}
</script>
</body>
</html>