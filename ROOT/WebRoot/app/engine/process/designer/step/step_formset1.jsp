<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="../iwork_js/lhgdialog/lhgcore.min.js" ></script>
	<script type="text/javascript" src="../iwork_js/lhgdialog/lhgdialog.min.js" ></script>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="iwork_js/metadata_index.js" charset="gb2312"></script>
	<style type="text/css">
		.item{
			border-bottom:1px dotted #ccc;
			white-space:nowrap;
			vertical-align:top;
			font-size: 12px;
		}
		.item a{
			font-size: 12px;
			word-wrap:break-word;
			word-break:break-all;
			margin-right:10px;
		}
		.td_title {
				color:#004080;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				white-space:nowrap;
				vertical-align:middle;
				font-weight:700;
				vertical-align:top;
				padding-top:5px;
			}
		.td_data{
			padding-top:3px;
			color:#0000FF;
			text-align:left;
			padding-left:3px;
			font-size: 12px;
			vertical-align:top;
			word-wrap:break-word;
			word-break:break-all;
			font-weight:500;
		}
		.icon-title{
			background:url('icons/page.png') no-repeat;
		}
	</style>
	<script type="text/javascript">
	
		//添加表单设置页面
		function addForm(){
				window.frames["sysStepform"].document.location = "sysFlowDef_stepFormSet!openAddForm.action?prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value="actStepDefId"/>";
				$('#formWinDiv').show();
				$('#formwindow').window('open');
		}
		//打开编辑表单设置页面
		function editItem(id){
				var dg = new J.dialog({
					id:id,
					cover:true, 
					bgcolor:'#999',
					rang:true,
					title:'编辑表单设置',
					link:true,
					width:500,
					height:350,
					iconTitle:false,
					autoSize:true,
					btnBar:false,
					page:"sysFlowDef_stepFormSet!loadform.action?id="+id+"&prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value='actStepDefId'/>"
				});
				//window.frames["sysStepform"].document.location = "sysFlowDef_stepFormSet!loadform.action?id="+id+"&prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value="actStepDefId"/>";
				//$('#formWinDiv').show();
				 dg.ShowDialog();
		}
		//打开授权页面
		function showFormMap(id){
				$('#formwindow').window({
					title:'字段权限设置',
					width:550,
					icon:"icon-title",
					content:"<iframe id='sysStepform' name='sysStepform'  width='515' style='border:0px solid #ccc;padding:3px;' height='330' frameborder=0  scrolling=auto  marginheight=0 marginwidth=0 border='0' ></iframe>"
				})
				window.frames["sysStepform"].document.location = "sysFlowDef_stepFormSet!showFormMap.action?id="+id+"&prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value="actStepDefId"/>";
				$('#formWinDiv').show();
				$('#formwindow').window('open');
		}
		//删除表单项目
		function delItem(id){
			 $.messager.confirm('确认','确认删除?',function(result){  
					 	if(result){
		                	$("#editForm_id").val(id);
		                	$('form').attr('action','sysFlowDef_stepFormSet!delItem.action');
		                	$("#editForm").submit();
	                    }
	            });
		}
	</script> 
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
		<s:property value="stepToolbar" escapeHtml="false"/>
	</div>
    <!-- 导航区 -->
	<div region="center" style="padding:3px;border:0px">
		<div style="padding:2px;background:#efefef;border-bottom:1px solid #efefef">
			<a href="javascript:addForm();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加表单绑定</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-search">预览</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
		<table border="0"  cellspacing="0" cellpadding="0"  width="100%">
		<s:iterator  value="formList" status="status">
				<tr >
					<td class="item"><img src="iwork_img/documents48.png" border="0"  style="border:1px solid #efefef;padding:3px;padding-left:13px;margin-right:13px;margin-top:13px;"></td>
					<td width="100%" class="item">
							<table border="0"  cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td class="td_title" width="15%">表单绑定类型：</td><td class="td_data" width="85%">
									<s:if test="bindType==0">
										<input type="radio" disabled checked="checked"/><b>绑定表单</b>
										<input type="radio" disabled/>绑定URL连接
									</s:if>
									<s:elseif test="bindType==1">
										<input type="radio" disabled/>绑定表单
										<input type="radio" disabled checked="checked"/><b>绑定URL连接</b>
									</s:elseif>
								</td>
							</tr>
							<tr>
								<td class="td_title">表单名称：</td><td class="td_data">
									<img src="iwork_img/page.png" border="0"  ><s:property value="formname_t"/>
									<!-- 判断是否为默认表单 -->
									<s:if test="isDef==1">
										【默认】
									</s:if>
								</td>
							</tr>
							<tr>
								<td class="td_title">表单设置：</td>
								<td class="td_data">
									<s:if test="isModify==1">
										<input type="checkbox" name="isModify" disabled checked>
									</s:if>
									<s:else>
										<input type="checkbox" name="isModify" disabled>
									</s:else>
									是否允许编辑
								</td>
							</tr>
							
						</table>
					</td>
					<td width="5%" class="item">
							<table   cellspacing="0" cellpadding="0" width="100%">
			        				<tr><td  class="item" style="text-align:right">
			        				<s:if test="bindType==0">
			        					<a href="javascript:showFormMap(<s:property value='id'/>);"><img src='iwork_img/shield_go.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>设置表单域字段权限</a>
			        				</s:if>
					        			<a href="javascript:editItem(<s:property value='id'/>);"><img src='iwork_img/note_edit.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>修改</a>
					        			<a href="javascript:delItem(<s:property value="id"/>);"><img src="iwork_img/del3.gif" style="border:0px solid #efefef;margin-right:3px;margin-top:3px;">删除</a>
					        			
			        				</td></tr>
			        				<tr><td 	>
					        			&nbsp;
			        				</td></tr>
			        				<tr><td  class="item"  style="text-align:right">调整优先级：
					        			<img title='移动到最前' src='iwork_img/icon/arrow_first.gif' border='0'/>
					        			<img title='向上移动' src='iwork_img/icon/arrow_up_blue.gif' border='0'/>
					        			<img title='向下移动' src='iwork_img/icon/arrow_down_blue.gif' border='0'/>
					        			<img  title='移动到最后' src='iwork_img/icon/arrow_last.gif' border='0'/>
			        				</td></tr>
			        			</table>
					</td>
				</tr>
			</s:iterator>
		</table>
		<s:form name="editForm" id="editForm">
			        	<s:hidden name="actDefId"/>
		                <s:hidden name="actStepDefId"/>
		                <s:hidden name="prcDefId"/>
			        	<s:hidden name="id"></s:hidden>
			        </s:form>
	</div>
	
	<!--添加分类窗口-->
		<div id="formWinDiv"  style="display:none">
	    <div id="formwindow" class="easyui-window" title="表单管理" modal="true" closed="true" collapsible="false" minimizable="true"
	        maximizable="false" icon="icon-title"  style="width: 515px; height: 400px; padding: 0px;
	        background: #fafafa;">
	        	<iframe id="sysStepform" name="sysStepform" width="480" style="border:0px solid #ccc;padding:3px;" height="330" frameborder=0  scrolling=auto  marginheight=0 marginwidth=0 border="0" ></iframe>
	        </div>
    	</div>
</body>
</html>
