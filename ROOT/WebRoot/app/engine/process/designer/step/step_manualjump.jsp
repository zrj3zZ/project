﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="title"/>-人工跳转</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/process/process_step.css">
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
	<script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
	<script type="text/javascript" src="iwork_js/metadata_index.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		.td_title {
				padding-left:5px;
				padding-right:10px;
				width:20%;
				text-align:right;
			}
		.td_data{
			    width:80%;
		}
		/*节点设置*/
		.node_body{ background-color:#f6f9fb;width:100%;}
		.node_nav{  height:32px; padding-left:20px; line-height:32px;}
		.node_box{  background:url(iwork_img/node_frame_page.jpg) repeat-x ; }
		.node_frame{ padding:15px; clear:both; line-height:30px; color:#6c6c6c; background-color:#f1f8fc;width:100%\9; border-bottom:1px solid #838383; }
		.node_line{  height:2px;  background:url(iwork_img/horizontal.jpg) repeat-x; width:100%; }
		.node_buttonframe{width:450px; float:right;align:right}
		.node_buttonframe a{ text-decoration:none;color:#6c6c6c}
		.setup_pic{ height:89px; width:86px; background:url(iwork_img/node_icon3.jpg) no-repeat;}
        .setup_pic_font{ color:#000; font-family:Arial;font-weight:bold; font-size:12px; text-align:right;padding-right:2px; padding-top:60px;-webkit-text-size-adjust:none;}	
	    .none_item{height:450px;line-height:450px;text-align:center;font-style:italic;color:#888888; font-family:宋体; font-size:18px;}
	</style>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
		//添加表单设置页面
		function addManualJump(){
				var  prcDefId =$("#editForm_prcDefId").val();
				var actDefId = $("#editForm_actDefId").val();
				var actStepDefId  = $("#editForm_actStepDefId").val();
				var pageUrl = "sysFlowDef_stepManualJump!openAddManualJump.action?prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
				this.showSendWindow(pageUrl);
		}
		//打开编辑表单设置页面
		function editItem(id){
				var  prcDefId =$("#editForm_prcDefId").val();
				var actDefId = $("#editForm_actDefId").val();
				var actStepDefId  = $("#editForm_actStepDefId").val();
				var pageUrl = "sysFlowDef_stepManualJump!loadform.action?id="+id+"&prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
				this.showSendWindow(pageUrl);
		}
		//打开授权页面
		function showFormMap(id){
				var  prcDefId =$("#editForm_prcDefId").val();
				var actDefId = $("#editForm_actDefId").val();
				var actStepDefId  = $("#editForm_actStepDefId").val();
				var pageUrl = "sysFlowDef_stepManualJump!showFormMap.action?id="+id+"&prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
				this.showSendWindow(pageUrl);
		}
		//删除表单项目
		function delItem(id){
			art.dialog.confirm('确认删除?', function(){
			    $("#editForm_id").val(id);
               	$('form').attr('action','sysFlowDef_stepManualJump!delItem.action');
               	$("#editForm").submit();
			}, function(){
			
			}); 
		}
		//打开流转处理窗口
		function showSendWindow(pageUrl){
			 art.dialog.open(pageUrl,{
			    	id:'stepManualJump',
					title:'人工跳转管理维护',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:400,
					close:function(){
					    location.reload();
					}
				 });
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
			<a href="javascript:addManualJump();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加人工跳转设置</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
		
		<s:if test="manualJumpList==null || manualJumpList.size()==0">
	            <div class="none_item">无设置项</div>
	     </s:if>
		
		<div class="node_box">
			<s:iterator  value="manualJumpList" status="status">
				<div class="node_frame">
		<table border="0"  cellspacing="0" cellpadding="0"  width="100%">
		
				<tr >
					<td width="15%">
						<s:if test="trigerType=='JAVA'">
						<div class="setup_pic"><div class="setup_pic_font">JAVA</div></div>
						</s:if>
						<s:elseif test="trigerType=='SQL'">
						<div class="setup_pic"><div class="setup_pic_font">SQL</div></div>
						</s:elseif>
						<s:elseif test="trigerType=='JavaScript'">
						<div class="setup_pic"><div class="setup_pic_font">JScript</div></div>
						</s:elseif>
						<s:else>
						<div class="setup_pic"><div class="setup_pic_font"></div></div>
						</s:else>
					</td>
					<td width="90%">
							<table border="0"  cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td class="td_title">跳转菜单名称：</td>
								<td colspan="2" class="td_data">
									<s:property value="mjName"/>
								</td>
							</tr>
							<tr>
								<td class="td_title" >执行动作：</td>
								<td class="td_data" colspan="2">
									<s:property value="mjTypeName_t"/>
								</td>
							</tr>
							<s:if test="isTriger==1">
							<tr>
								<td class="td_title">触发器类型：</td><td class="td_data" colspan="2">
									<s:if test="trigerType=='JAVA'">
										<input type="radio" disabled checked="checked"/><b>JAVA</b>
										<input type="radio" disabled/>SQL
										<input type="radio" disabled/>JavaScript
									</s:if>
									<s:elseif test="trigerType=='SQL'">
										<input type="radio" disabled />JAVA
										<input type="radio" disabled checked="checked"/><b>SQL</b>
										<input type="radio" disabled/>JavaScript
									</s:elseif>
									<s:elseif test="trigerType=='JavaScript'">
										<input type="radio" disabled />JAVA
										<input type="radio" disabled />SQL
										<input type="radio" disabled  checked="checked"/><b>JavaScript</b>
									</s:elseif>
								</td>
							</tr>
							<tr>
								<td class="td_title" >脚本类路径：</td><td class="td_data" colspan="2">
									<s:property value="trigerMap"/>
								</td>
							</tr>
							</s:if>
							<s:if test="isRemind==1">
							<tr>
								<td class="td_title" >通知类型：</td><td class="td_data" colspan="2">
								    <s:if test="msgSysmsg==1">
								        <input type="checkbox" checked disabled value="checkbox">
								    </s:if>
								     <s:else>
							            <input type="checkbox"  disabled value="checkbox">
							         </s:else>系统消息
								    <s:if test="msgEmail==1">
								    	<input type="checkbox" checked disabled value="checkbox">
							      	</s:if>
							      	<s:else>
							          	<input type="checkbox"  disabled value="checkbox">
							      	</s:else>邮件
								    <s:if test="msgIm==1">
								    	<input type="checkbox" checked disabled value="checkbox">
							      	</s:if>
							      	<s:else>
							         	<input type="checkbox"  disabled value="checkbox">
							      	</s:else>即时通讯
								    <s:if test="msgSms==1">
								     	<input type="checkbox" checked disabled value="checkbox">
							      	</s:if>
							      	<s:else>
							          	<input type="checkbox"  disabled value="checkbox">
							      	</s:else>短信									
								</td>
							</tr>
							<tr>
								<td class="td_title" >通知人：</td><td class="td_data" colspan="2">
									<s:property value="msgUsers"/>
								</td>
							</tr>
							</s:if>
						</table>
					</td>
					</tr>
					<tr>
					<td></td>
				    <td align="right">
							<div class="node_buttonframe">
			        				<s:if test="bindType==0">
			        					<span style=" background:url(iwork_img/shield_go.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:showFormMap(<s:property value='id'/>);">设置表单域字段权限</a></span>
			        				</s:if>		
					        			<span style=" background:url(iwork_img/icon/edit.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:editItem(<s:property value='id'/>);">修改</a></span>
					        			<span style=" background:url(iwork_img/icon/del.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:delItem(<s:property value="id"/>);">删除</a></span>		
			        			
			        				    <s:if test="#status.first">
											<span style=" background:url(iwork_img/icon/arrow_down_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="sysFlowDef_stepManualJump!moveDown.action?actDefId =<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>">降低优先级</a></span>
										</s:if>
			        					<s:elseif test="#status.last">
					        				<span style=" background:url(iwork_img/icon/arrow_up_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="sysFlowDef_stepManualJump!moveUp.action?actDefId=<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>">提高优先级</a></span>
										</s:elseif>										
										<s:else>
												 <span style=" background:url(iwork_img/icon/arrow_up_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="sysFlowDef_stepManualJump!moveUp.action?actDefId =<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>">提高优先级</a></span>
												 <span style=" background:url(iwork_img/icon/arrow_down_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="sysFlowDef_stepManualJump!moveDown.action?actDefId =<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>">降低优先级</a></span>
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
