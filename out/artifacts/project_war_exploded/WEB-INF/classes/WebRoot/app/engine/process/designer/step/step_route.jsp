<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="title"/>-节点规则设置</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/process/process_step.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
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
		.node_buttonframe{width:360px; float:right;align:right}
		.node_buttonframe a{ text-decoration:none;color:#6c6c6c}
		.setup_pic{ height:89px; width:86px; background:url(iwork_img/node_icon1.jpg) no-repeat;}
        .setup_pic_font{ color:#000; font-family:Arial; font-size:10px; padding-left:35px; padding-top:50px;-webkit-text-size-adjust:none;}				 
	    .none_item{height:450px;line-height:450px;text-align:center;font-style:italic;color:#888888; font-family:宋体; font-size:18px;}
	</style>
	<script type="text/javascript">
		var api = art.dialog.open.api, W = api.opener;
		function addRoute(){
				var  prcDefId =$("#editForm_prcDefId").val();
				var actDefId = $("#editForm_actDefId").val();
				var actStepDefId  = $("#editForm_actStepDefId").val();
				var pageUrl  = "sysFlowDef_stepRoute!openAddRoute.action?prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
				this.showSendWindow(pageUrl);
		}
		function editItem(id){
				var  prcDefId =$("#editForm_prcDefId").val();
				var actDefId = $("#editForm_actDefId").val();
				var actStepDefId  = $("#editForm_actStepDefId").val();
				var pageUrl  = "sysFlowDef_stepRoute!loadform.action?id="+id+"&prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
				this.showSendWindow(pageUrl);
		}
		function delItem(id){
			art.dialog.confirm('确认删除?', function(){
			    $("#editForm_id").val(id);
               	$('form').attr('action','sysFlowDef_stepRoute!delItem.action');
               	$("#editForm").submit();
			}, function(){
			
			}); 
		}
		//打开流转处理窗口
		function showSendWindow(pageUrl){
			  art.dialog.open(pageUrl,{
			    	id:'routeStepDialog',
			    	title:'路由策略维护',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'80%',
				    height:'80%',
				    close:function(){
				    	window.location.reload(true);
				    }
				 });
		}
			//显示权限设置
	function showAuthority(id){  
	       var pageUrl =  "sysFlowDef_stepRoute!showAuthority.action?id="+id;
	         art.dialog.open(pageUrl,{
			    	id:'routeStepDialog',
			    	title:'查看权限设置',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:290,
					height:480
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
			<a href="javascript:addRoute();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加路由策略</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
		
		<s:if test="routeList==null || routeList.size()==0">
	            <div class="none_item">无设置项</div>
	     </s:if>
		
		<div class="node_box">
			<s:iterator  value="routeList" status="status">
				<div class="node_frame">
		<table border="0"  cellspacing="0" cellpadding="0" width="100%">		
				<tr >
					<td width="10%" ><div class="setup_pic"><div class="setup_pic_font"></div></div>
					</td>
					<td width="90%">
							<table border="0"  cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td class="td_title">路由类型</td>
								<td class="td_data"colspan="2">
									<s:if test="routeType==0">
										<input type="radio" disabled checked="checked"/>由办理人选择
										<input type="radio" disabled/>设置固定办理人
									</s:if>
									<s:elseif test="routeType==1">
										<input type="radio" disabled/>由办理人选择
										<input type="radio" disabled checked="checked"/>设置固定办理人
									</s:elseif>
								</td>
							</tr>
							<tr>
								<td class="td_title">路由方案</td><td class="td_data" colspan="2">
									方案<s:property value="configRouteModel.key"/> :<s:property value="configRouteModel.title"/> 
								</td>
							</tr>
							<tr>
								<td class="td_title">路由参数</td><td class="td_data" colspan="2"><s:property value="routeMemo"/></td>
							</tr>
						</table>
					</td>
					</tr>
					<tr>
					<td></td>
				    <td align="right">
							<div class="node_buttonframe">
								        <span style=" background:url(iwork_img/shield_go.png) no-repeat; padding-left:20px;margin-right:5px;"><a href='javascript:showAuthority(<s:property value="id"/>);'>查看授权</a></span>
								        <span style=" background:url(iwork_img/icon/edit.png) no-repeat; padding-left:20px;margin-right:5px;"><a href='javascript:editItem(<s:property value="id"/>);'>修改</a></span>
								        <span style=" background:url(iwork_img/icon/del.png) no-repeat; padding-left:20px;margin-right:5px;"><a href='javascript:delItem(<s:property value="id"/>);'>删除</a></span>
								        	<s:if test="#status.first">
						          				<span style=" background:url(iwork_img/icon/arrow_down_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href='sysFlowDef_stepRoute!moveDown.action?actDefId=<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>'>降低优先级</a></span>
						        			</s:if>	
						        			<s:elseif test="#status.last">
						           					 <span style=" background:url(iwork_img/icon/arrow_up_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href='sysFlowDef_stepRoute!moveUp.action?actDefId=<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>'>提高优先级</a></span>
						        			</s:elseif>	
						        			<s:else>
						           				<span style=" background:url(iwork_img/icon/arrow_up_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href='sysFlowDef_stepRoute!moveUp.action?actDefId=<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>'>提高优先级</a></span>
	                               				<span style=" background:url(iwork_img/icon/arrow_down_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href='sysFlowDef_stepRoute!moveDown.action?actDefId =<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>'>降低优先级</a></span>
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
