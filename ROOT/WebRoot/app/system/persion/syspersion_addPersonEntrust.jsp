<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css"	href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css"	href="iwork_css/jquerycss/icon.css">
<linkrel="stylesheet" type="text/css"  href="iwork_css/reset.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 

<script type="text/javascript"	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<style type="text/css">
.td_title {
	color: #004080;
	font-size: 12px;
	text-align: right;
	letter-spacing: 0.1em;
	padding-right:3px;
	white-space: nowrap;
	vertical-align: middle;
	height: 15px;
}

.td_data {
	color: #0000FF;
	text-align: left;
	padding-left: 3px;
	font-size: 12px;
	vertical-align: middle;
	word-wrap: break-word;
	word-break: break-all;
	font-weight: 500;
	line-height: 12px;
	padding-top: 2px;
	height: 15px;
}
.textinput {
	padding: 5px;
	border: 1px solid #ABADB3;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
</style>
<script type="text/javascript">
	var entrustUserTemp;//定义全局变量临时存放委托人账号
	var entrustedUserTemp;//定义全局变量临时存放被委托人账号
	var api = art.dialog.open.api, W = api.opener;
	//保存
	function doSubmit(){
		getChecked();//检测数据的有效性
		var entrusetUserid = $('#editForm_model_entrusetUserid').val();
		var entrusetedUserid = $('#editForm_model_entrusetedUserid').val();
		entrustUserTemp = entrusetUserid;
		entrustedUserTemp=entrusetedUserid;
		var s1 = entrusetUserid.split('[');
		var s2 = entrusetedUserid.split('[');
		$('#editForm_model_entrusetUserid').val(s1[0]);//将USERID过滤出来,数据库中仅存放USERID不包含中文姓名
		$('#editForm_model_entrusetedUserid').val(s2[0]);
		if(checkValue()){
			var options = {
				error:errorFunc,
				success:showResponse 
			};
			$('#editForm').ajaxSubmit(options);
		}else{
			$('#editForm_model_entrusetUserid').val(entrustUserTemp);//检验不合格,还原委托人与被委托人为原有的格式
			$('#editForm_model_entrusetedUserid').val(entrustedUserTemp);
		}
	}
	errorFunc = function(){
		$('#editForm_model_entrusetUserid').val(entrustUserTemp);//保存完毕后,还原委托人与被委托人为原有的格式
		$('#editForm_model_entrusetedUserid').val(entrustedUserTemp);
		art.dialog.tips("委托失败，返回值异常",2);
	}
	showResponse = function(responseText, statusText, xhr, $form){
		if(responseText=="save_success"){
			$('#editForm_model_entrusetUserid').val(entrustUserTemp);
			$('#editForm_model_entrusetedUserid').val(entrustedUserTemp);
			art.dialog.tips("委托任务已经发送到被委托人等待“接受委托确认”",2);
			cancel();
		}else if(responseText=="error_exist"){
			$('#editForm_model_entrusetUserid').val(entrustUserTemp);
			$('#editForm_model_entrusetedUserid').val(entrustedUserTemp);
			art.dialog.tips("委托失败，存在已经委托的流程",2);
		}
	}
	//取消
	function cancel(){
		api.close();
	}
	$(document).ready(function(){
		//预加载委托人为当前用户，且为只读状态
		$('#editForm_model_entrusetUserid').val('<s:property value="userid" escapeHtml="false"/>');
		$('#editForm_model_entrusetUserid').attr('readonly','readonly');
			var setting = {
					check: {
						enable: true
					},
					view: {
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:"processEntrust_sys_getProcessTree.action",
						dataType:"json",  
						autoParam:["id","nodeType"]
					},callback: {
						onClick:onClickItem
					}
				};
			$.fn.zTree.init($("#processTree"), setting);
	});
	
	function onClickItem(event, treeId, treeNode, clickFlag){
		if(treeNode.isParent){
						var zTree = $.fn.zTree.getZTreeObj("processTree");
				 		zTree.expandNode(treeNode, true, null, null, true);
		}else{ 
		
					var zTree = $.fn.zTree.getZTreeObj("processTree");
			 		if(!treeNode.checked){
			 			zTree.checkNode(treeNode, true, true, clickFlag);
			 		}else{
		 				zTree.checkNode(treeNode, false, true, false);
			 		}
		}
	}
	
	//获得选中的所有需要被委托的流程的信息
	function getChecked(){
		//var nodes = $('#processTree').tree('getChecked');
		var s = '';
		var zTree = $.fn.zTree.getZTreeObj("processTree");
		var nodes = zTree.getCheckedNodes(true);
		var str = "";  
		for(var i=0;i<nodes.length;i++){
			var type = nodes[i].type;
			if(type=='group')continue;
			else if(type=='process'){
				if( s!= ''){
					s += '_';
				}
				s += nodes[i].name+'~'+nodes[i].actDefId+'~'+nodes[i].proDefId;
			}
		}
		$('#editForm_processInfo').val(s);
	}
	//检验数据的有效性
	function checkValue(){
		var flag = true;
		var start = $('#editForm_entrustStart').val();
		var end = $('#editForm_entrustEnd').val();
		var entrusetUserid = $('#editForm_model_entrusetUserid').val();
		var entrusetedUserid = $('#editForm_model_entrusetedUserid').val();
		var entrustInfo = $('#editForm_processInfo').val();
		var entrusetReason = $('#entrusetReason').val();
		var itemOption = $('input[name="model.itemOption"]:checked').val();
		if(""==start||null==start){art.dialog.tips("委托起始为必填项");flag = false}
		if(""==end||null==end){art.dialog.tips("委托结束时间为必填项");flag = false}
		if(""==itemOption||null==itemOption){art.dialog.tips("委托类型为必填项");flag = false}
		if(""==entrusetUserid||null==entrusetUserid){art.dialog.tips("委托人为必填项");flag = false}
		if(""==entrusetedUserid||null==entrusetedUserid){art.dialog.tips("被委托人为必填项");flag = false}
		if(""==entrusetReason||null==entrusetReason){art.dialog.tips("委托原因为必填项");flag = false}
		if(document.getElementById('editForm_model_isentrusetall0').checked){
			if(""==entrustInfo||null==entrustInfo){art.dialog.tips("请选择需要委托的流程");flag = false}
		}
		if(""!=entrusetUserid&&""!=entrusetedUserid&&entrusetUserid==entrusetedUserid){art.dialog.tips("委托人与被委托人不能为同一人");flag = false}
		return flag;
	}
	//显示流程树
	function displayProcessTree(obj){
		if(obj.value==1){
			$('#p_Tree').hide();
			$('#p_Show').show();
		}else{
			$('#p_Show').hide();
			$('#p_Tree').show();
		}
	}
	function reSetType(){
		var itemOption = $('input[name="model.itemOption"]:checked').val();
		var setting = {
					check: {
						enable: true
					},
					view: {
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:"processEntrust_sys_getProcessTree.action?itemOption="+itemOption,
						dataType:"json",  
						autoParam:["id","nodeType"]
					},callback: {
						onClick:onClickItem
					}
				};
			$.fn.zTree.init($("#processTree"), setting);
			$('#editForm_processInfo').val(''); 
	}
	function showTips(){
		art.dialog.tips("如果当前类型为“委托办理”时，委托人的所有待办任务将由被委托人处理;<br/>如果当前类型为“授权发起”时，被委托人可代替委托人填报单据并发起流程。",2);
	}
</script>
	</head>
	<body>
		<div align="left">
		<s:form id="editForm" name="editForm" action="processEntrust_sys_save.action" theme="simple">
			<table border="0" cellspacing="5" cellpadding="0" style="margin:5px;">
					<tr>
					<td class="td_title">授权委托人</td>
					<td class="td_data"><input type="text" name="entrust" value="<s:property value="userid" escapeHtml="false"/>" disabled="true"><s:textfield name="model.entrusetUserid" theme="simple" cssStyle="display:none"/>
					</td>
				</tr>
				<tr>
					<td class="td_title">被授权委托人</td>
					<td class="td_data">
						<s:textfield name="model.entrusetedUserid" theme="simple"/><a href="javascript:radio_book('false','false','','','','','','','editForm_model_entrusetedUserid');" class="easyui-linkbutton" plain="true" iconCls="icon-add">地址簿</a><span id="entrustedUser" style='color:red'>*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">授权时间</td>
					<td class="td_data" style="text-align:left">
						<s:textfield name="entrustStart" theme="simple" cssStyle="width:120px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\\'editForm_entrustEnd\\')||\\'2050-10-01\\'}'})"><s:param name="value"><s:date name="entrustStart" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield>&nbsp;至&nbsp;<s:textfield name="entrustEnd" theme="simple" cssStyle="width:120px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\\'editForm_entrustStart\\')}',maxDate:'2050-10-01'})"><s:param name="value"><s:date name="entrustEnd" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield><span id="entrustTime" style='color:red'>*</span>
					</td>
				</tr>
		
				
				<tr>
					<td class="td_title">授权类型</td>
					<td class="td_data">
						<s:radio id="itemOption"  name="model.itemOption" list="#{'1':'委托办理','0':'授权代发起'}" onclick="reSetType()" listKey="key" listValue="value"></s:radio><a href="javascript:showTips()"><img style='cursor:pointer;' src="iwork_img/icon/help_blue.gif" title="如果当前类型为“委托办理”时，委托人的所有待办任务将由被委托人处理;
如果当前类型为“授权发起”时，被委托人可代替委托人填报单据并发起流程。"/></a><span id="entrustType" style='color:red'>*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">原因</td>
					<td class="td_data">
						<s:textarea id="entrusetReason" name="model.entrusetReason" theme="simple" cssStyle="resize:none;width:330px;height:40px;"/><span id="entrustTime" style='color:red'>*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">是否委托全部流程</td>
					<td class="td_data">
						<s:radio name="model.isentrusetall" list="#{'1':'是','0':'否'}" onclick="javascript:displayProcessTree(this);" listKey="key" listValue="value"/>
					</td>
				</tr>
				<tr id="process-Tree">
					<td class="td_title" style="vertical-align: top;">委托列表:</td>
					<td class="td_data">
						<div id="p_Tree" class="textinput" style="height:150px;width:320px;overflow:auto;display:none;">
							<ul id="processTree" class="ztree"></ul>
						</div>
						<div id="p_Show" class="textinput" style="height:150px;width:320px;overflow:auto;background-color:#EEEEEE;">
						</div>
					</td>
				</tr>
				<tr style="display:none;">
					<td class="td_title">委托流程</td>
					<td class="td_data">
						<s:textarea name="processInfo" theme="simple" cssStyle="resize:none;width:330px;height:60px;"/>
					</td>
				</tr>
			</table>
			<s:hidden name="model.id" />
			<s:hidden name="model.entrusetStatus" />
			<div region="south" border="false"	style="text-align: right; height: 30px; line-height: 30px; padding-top: 5px; padding-right: 15px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok"	href="javascript:doSubmit();"> 保存</a>
				<a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel"	href="javascript:cancel()">关闭</a>
			</div>
		</s:form>
		</div>
	</body>
</html>
