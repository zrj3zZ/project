<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="title"/>-表单设置</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/process/process_step.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
	<script type="text/javascript" src="iwork_js/metadata_index.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		.td_title {
				padding-left:5px;
				padding-right:10px;
				width:15%;
				text-align:right;
			}
		.td_data{
			    width:85%;
		}
		/*节点设置*/
		.node_body{ background-color:#f6f9fb;width:100%;}
		.node_nav{  height:32px; padding-left:20px; line-height:32px;}
		.node_box{  background:url(iwork_img/node_frame_page.jpg) repeat-x ; }
		.node_frame{ padding:15px; clear:both; line-height:30px; color:#6c6c6c; background-color:#f1f8fc;width:100%\9; border-bottom:1px solid #838383; }
		.node_line{  height:2px;  background:url(iwork_img/horizontal.jpg) repeat-x; width:100%; }
		.node_buttonframe{width:450px; float:right;align:right}
		.node_buttonframe a{ text-decoration:none;color:#6c6c6c}
		.setup_pic{ height:89px; width:86px; background:url(iwork_img/node_icon2.jpg) no-repeat;}
        .setup_pic_font{ color:#000; font-family:Arial; font-size:10px; padding-left:35px; padding-top:50px;-webkit-text-size-adjust:none;}			
	    .none_item{height:450px;line-height:450px;text-align:center;font-style:italic;color:#888888; font-family:宋体; font-size:18px;}
	</style>
	<script type="text/javascript">
	  var api = art.dialog.open.api, W = api.opener;
		//添加表单设置页面
		function addForm(){
		var  prcDefId =$("#editForm_prcDefId").val();
		var actDefId = $("#editForm_actDefId").val();
		var actStepDefId  = $("#editForm_actStepDefId").val();
		var pageUrl = "sysFlowDef_stepFormSet!openAddForm.action?prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
		   art.dialog.open(pageUrl,{
			    	id:'formMapSetWinDiv',
					title:'表单域设置',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'80%',
				    height:'80%',
					close:function(){
					    location.reload();
					}
				 });
		}
		//打开编辑表单设置页面
		function editItem(id){
			var  prcDefId =$("#editForm_prcDefId").val();
			var actDefId = $("#editForm_actDefId").val();
			var actStepDefId  = $("#editForm_actStepDefId").val();
			var pageUrl = "sysFlowDef_stepFormSet!loadform.action?id="+id+"&prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
			  art.dialog.open(pageUrl,{
			    	id:'formMapSetWinDiv',
					title:'表单域设置',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'80%',
				    height:'80%',
					close:function(){
					    location.reload();
					}
				 });
		}
		//打开授权页面
		function showFormMap(id){
			var  prcDefId =$("#editForm_prcDefId").val();
			var actDefId = $("#editForm_actDefId").val();
			var actStepDefId  = $("#editForm_actStepDefId").val();
			var pageUrl = "sysFlowDef_stepFormMap_load.action?formId="+id+"&prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
			  art.dialog.open(pageUrl,{
			    	id:'formMapSetWinDiv',
					title:'表单域权限设置',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'80%',
				    height:'80%',
					close:function(){
					    location.reload();
					}
				 });
		}
		//显示权限设置
	function showAuthority(id){  
	       var pageUrl =  "sysFlowDef_stepFormSet!showAuthority.action?id="+id;
	         art.dialog.open(pageUrl,{
			    	id:'db_show',
					title:'查看权限设置',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:290,
					height:480
				 });
				 
	}
		//删除表单项目
		function delItem(id){
			art.dialog.confirm('你确定要删除当前表单吗？', function(){
			    $("#editForm_id").val(id);
               	$('form').attr('action','sysFlowDef_stepFormSet!delItem.action');
               	$("#editForm").submit();
			}, function(){
			
			});
		}
		
		//移动位置
		function moveItem(id,type){
			if(type=='top'){
				$('form').attr('action','sysFlowDef_stepFormSet!moveTop.action');
			}else if(type=='up'){
				$('form').attr('action','sysFlowDef_stepFormSet!moveUp.action');
			}else if(type=='down'){
				$('form').attr('action','sysFlowDef_stepFormSet!moveDown.action');
			}else if(type=='bottom'){
				$('form').attr('action','sysFlowDef_stepFormSet!moveBottom.action');
			}
			$("#editForm_id").val(id);
			$("#editForm").submit();
		}
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="west" border="false" style="width:180px;padding:3px;border-right:1px solid #efefef">
		<s:property value="stepToolbar" escapeHtml="false"/>
	</div>
	<div region="north" border="false" style="height:40px">
		<div class="stepTitle">
			<s:property value="title"/><img src="iwork_img/gear.gif" style="float:right;height:25px" alt="节点设置"/>
		</div>
	</div>
    <!-- 导航区 -->
	<div region="center" style="padding:0px;border:0px">
		<div class="tools_nav">
			<a href="javascript:addForm();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加表单绑定</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
		
		<s:if test="formList==null || formList.size()==0">
	            <div class="none_item">无设置项</div>
	     </s:if>
		
		<div class="node_box">
			<s:iterator  value="formList" status="status">
				<div class="node_frame">
		<table border="0"  cellspacing="0" cellpadding="0"  width="100%">		
				<tr >
					<td width="15%"><div class="setup_pic"><div class="setup_pic_font"></div></div></td>
					<td width="90%">
							<table border="0"  cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td class="td_title">表单名称：</td>
								<td colspan="2" class="td_data">
									<img src="iwork_img/page.png" border="0"  >
									<s:property value="formname"/>
									<!-- 判断是否为默认表单 -->
									<s:if test="isDef==1">
										【默认】
									</s:if>
								</td>
							</tr> 
							<tr>
								<td class="td_title">表单绑定类型：</td>
								<td colspan="2" class="td_data">
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
					</tr>
					<tr>
					<td></td>
				    <td align="right">
							<div class="node_buttonframe">	
							<a  href="javascript:editItem(<s:property value='id'/>);" class="easyui-linkbutton" plain="false" iconCls="icon-edit">属性</a>
							<!-- <span style=" background:url(iwork_img/icon/edit.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:editItem(<s:property value='id'/>);">修改</a></span> -->
					        			<s:if test="bindType==0" > 
				        						<a  href="javascript:showFormMap(<s:property value='id'/>);" class="easyui-linkbutton" plain="false" >域权限设置</a>
				        						<!-- <span style=" background:url(iwork_img/shield_go.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:showFormMap(<s:property value='id'/>);">表单域权限设置</a></span> -->
			        					</s:if>
			        					<a  href="javascript:showAuthority(<s:property value='id'/>);"class="easyui-linkbutton" plain="false" >表单显示权限</a>
			        					<!-- <span style=" background:url(iwork_img/shield_go.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:showAuthority(<s:property value='id'/>);">查看权限设置</a></span>	-->
			        					<a  href="javascript:delItem(<s:property value="id"/>);" class="easyui-linkbutton" plain="false" iconCls="icon-remove">删除</a>
					        			<!--<span style=" background:url(iwork_img/icon/del.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:delItem(<s:property value="id"/>);">删除</a></span>-->
					     
					      		       <s:if test="#status.first">
					       					<span style=" background:url(iwork_img/icon/arrow_down_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:moveItem(<s:property value='id'/>,'down');">向下</a></span>
					                    </s:if>	
					                    <s:elseif test="#status.last">
					                    	 <span style=" background:url(iwork_img/icon/arrow_up_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:moveItem(<s:property value='id'/>,'up');">向上</a></span>
					                    </s:elseif>
					                    <s:else>
					        				 <span style=" background:url(iwork_img/icon/arrow_up_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:moveItem(<s:property value='id'/>,'up');">向上</a></span>
					        				<span style=" background:url(iwork_img/icon/arrow_down_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:moveItem(<s:property value='id'/>,'down');">向下</a></span>
					        			</s:else>	
					        </div> 							       
					</td>
				</tr>
		</table>
		</div>
		</s:iterator>
		<s:form name="editForm" id="editForm">
			        	<s:hidden name="actDefId"/>
		                <s:hidden name="actStepDefId"/>
		                <s:hidden name="prcDefId"/>
			        	<s:hidden name="id"></s:hidden>
			        </s:form>
	</div>
</div>
</body>
</html>
