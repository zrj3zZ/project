<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
	padding-right: 10px;
	white-space: nowrap;
	vertical-align: middle;
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
	padding-top: 5px;
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
</script>
<script type="text/javascript">
	var api = art.dialog.open.api;
	$(document).ready(function(){
		<s:if test="null == model.id">//如果是新建，就加载流程树
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

			/*
			$('#processTree').tree({   
                url: 'processEntrust_sys_getProcessTree.action',
                checkbox:true, 
              	onClick:function(node){
              		if(node.attributes.type=='process'){ 
						if(node.checked){
							$(this).tree('uncheck', node.target);
						}else{
							$(this).tree('check', node.target);
						}
					}else if(node.attributes.type=='group'){
						$(this).tree('toggle', node.target);
					}
				}
            });*/
		</s:if>
		<s:else>//如果是浏览详细信息，就加载委托流程信息,并将所有选项设为只读
			var entrustStart = '<s:property value="entrustStart" escapeHtml="false"/>';
			var entrustEnd = '<s:property value="entrustEnd" escapeHtml="false"/>';
			$('#editForm_entrustStart').val(entrustStart);
			$('#editForm_entrustStart').attr("disabled",true);
			$('#editForm_entrustEnd').val(entrustEnd);
			$('#editForm_entrustEnd').attr("disabled",true);
			$('#editForm_model_entrusetUserid').attr("disabled",true);
			$('#editForm_model_entrusetedUserid').attr("disabled",true);
			$('#editForm_model_entrusetReason').attr("disabled",true);
			$('#editForm_processInfo').attr("disabled",true);
			$('#itemOption').attr("disabled",true);
		</s:else>
	});
	var entrustUserTemp;//定义全局变量临时存放委托人账号
	var entrustedUserTemp;//定义全局变量临时存放被委托人账号
	
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
	
	//保存
	function doSubmit(){
		getChecked();//获得要委托的流程的信息
		var entrusetUserid = $('#editForm_model_entrusetUserid').val();
		var entrusetedUserid = $('#editForm_model_entrusetedUserid').val();
		entrustUserTemp = entrusetUserid;
		entrustedUserTemp = entrusetedUserid;
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
			$('#editForm_model_entrusetUserid').val(entrustUserTemp);//检验完毕后,还原委托人与被委托人为原有的格式
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
			$('#editForm_model_entrusetUserid').val(entrustUserTemp);//保存完毕后,还原委托人与被委托人为原有的格式
			$('#editForm_model_entrusetedUserid').val(entrustedUserTemp);
			art.dialog.tips("委托成功",2);
			setTimeout('cancel();',2000);
		}else if(responseText=="error_exist"){
			$('#editForm_model_entrusetUserid').val(entrustUserTemp);//保存完毕后,还原委托人与被委托人为原有的格式
			$('#editForm_model_entrusetedUserid').val(entrustedUserTemp);
			art.dialog.tips("委托失败,与已有委托存在重复",2);
		}
	}
	//取消
	function cancel(){
		api.close();
	}
	
	//获得选中的所有需要被委托的流程的信息
	function getChecked(){
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
		if(""==start||null==start){art.dialog.tips("委托时间为必填项");flag = false}
		if(""==end||null==end){art.dialog.tips("委托时间为必填项");flag = false}
		if(""==entrusetUserid||null==entrusetUserid){art.dialog.tips("委托人为必填项");flag = false}
		if(""==entrusetedUserid||null==entrusetedUserid){art.dialog.tips("被委托人为必填项");flag = false}
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
</script>
	</head>
	<body>
		<div align="left">
		<s:form id="editForm" name="editForm" action="processEntrust_sys_save.action" theme="simple">
			<table border="0" cellspacing="5" cellpadding="0" style="margin: 15px;">
				<tr>
					<td class="td_title">委托时间:</td>
					<td class="td_title">
						<s:textfield name="entrustStart" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\\'editForm_entrustEnd\\')||\\'2050-10-01\\'}'})"><s:param name="value"><s:date name="entrustStart" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield>至<s:textfield name="entrustEnd" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\\'editForm_entrustStart\\')}',maxDate:'2050-10-01'})"><s:param name="value"><s:date name="entrustEnd" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield><span id="entrustTime" style='color:red'>*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">委托人:</td>
					<td class="td_data">
						<s:textfield name="model.entrusetUserid" theme="simple"/><s:if test="null == model.id"><a href="javascript:radio_book('','','','','','','','','editForm_model_entrusetUserid');" class="easyui-linkbutton" plain="true" iconCls="icon-add">人员地址簿</a></s:if><s:else></s:else><span id="entrustUser" style='color:red'>*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">被委托人:</td>
					<td class="td_data">
						<s:textfield name="model.entrusetedUserid" theme="simple"/><s:if test="null == model.id"><a href="javascript:radio_book('','','','','','','','','editForm_model_entrusetedUserid');" class="easyui-linkbutton" plain="true" iconCls="icon-add">人员地址簿</a></s:if><s:else></s:else><span id="entrustedUser" style='color:red'>*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">授权类型</td>
					<td class="td_data">
						<s:radio  name="model.itemOption" id="itemOption" list="#{'1':'委托办理','0':'授权代发起'}" onclick="reSetType()" listKey="key" listValue="value"></s:radio><img style='cursor:help' src="iwork_img/icon/help_blue.gif" title="如果当前类型为“委托办理”时，委托人的所有待办任务将由被委托人处理;
如果当前类型为“授权发起”时，被委托人可代替委托人填报单据并发起流程。"/><span id="entrustType" style='color:red'>*</span>
					</td>
				</tr>
				<tr>
					<td class="td_title">是否委托全部流程:</td>
					<td class="td_data">
						<s:radio name="model.isentrusetall" list="#{'1':'是','0':'否'}" onclick="javascript:displayProcessTree(this);" listKey="key" listValue="value"/>
					</td>
				</tr>
				<tr>
					<td class="td_title">委托原因:</td>
					<td class="td_data">
						<s:textarea name="model.entrusetReason" theme="simple" cssStyle="resize:none;width:330px;height:40px;"/>
					</td>
				</tr>
				<s:if test="null == model.id">
					<tr id="process-Tree">
						<td class="td_title" style="vertical-align: top;">委托列表:</td>
						<td class="td_data">
							<div id="p_Tree" class="textinput" style="height:230px;width:320px;overflow:auto;display:none;">
								<ul id="processTree" class="ztree"></ul>
							</div>
							<div id="p_Show" class="textinput" style="height:230px;width:320px;overflow:auto;background-color:#EEEEEE;">
							</div>
						</td>
					</tr>
					<tr style="display:none;">
						<td class="td_title">委托流程:</td>
						<td class="td_data">
							<s:textarea name="processInfo" theme="simple" cssStyle="resize:none;width:330px;height:60px;"/>
						</td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td class="td_title">委托流程:</td>
						<td class="td_data">
							<s:textarea name="processInfo" theme="simple" cssStyle="resize:none;width:330px;height:60px;"/>
						</td>
					</tr>
				</s:else>
			</table>
			<s:hidden name="model.id" />
			<s:hidden name="model.entrusetStatus" />
			<div region="south" border="false"	style="text-align: right; height: 30px; line-height: 30px; padding-top: 5px; padding-right: 15px;">
				<s:if test="null == model.id">
					<a id="btnEp" class="easyui-linkbutton" icon="icon-ok"	href="javascript:doSubmit();"> 保存</a>
					<a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel"	href="javascript:cancel()">关闭</a>
				</s:if>
				<s:else>
					<a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel"	href="javascript:cancel()">关闭</a>
				</s:else>
			</div>
		</s:form>
		</div>
	</body>
</html>
