<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="title"/>-督办规则设置</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/process/process_step.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
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
		.setup_pic{ height:89px; width:86px; background:url(iwork_img/node_icon5.jpg) no-repeat;}
		.setup_pic_font{ color:#000; font-family:Arial; font-size:10px; padding-left:35px; padding-top:50px;-webkit-text-size-adjust:none;}		
	    .none_item{height:450px;line-height:450px;text-align:center;font-style:italic;color:#888888; font-family:宋体; font-size:18px;}
	</style>
	<script type="text/javascript">
	//全局变量
var api = art.dialog.open.api, W = api.opener;
		function add(){
				var  prcDefId =$("#editForm_prcDefId").val();
				var actDefId = $("#editForm_actDefId").val();
				var actStepDefId  = $("#editForm_actStepDefId").val();
				var pageUrl  = "sysFlowDef_stepSupervise!openAddSupervise.action?prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
				this.showSendWindow(pageUrl);
		}
		function editItem(id){
				var  prcDefId =$("#editForm_prcDefId").val();
				var actDefId = $("#editForm_actDefId").val();
				var actStepDefId  = $("#editForm_actStepDefId").val();
				var pageUrl  = "sysFlowDef_stepSupervise!loadform.action?id="+id+"&prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
				this.showSendWindow(pageUrl);
		}
		function delItem(id){
			art.dialog.confirm('确认删除?', function(){
			    $("#editForm_id").val(id);
               	$('form').attr('action','sysFlowDef_stepSupervise!delItem.action');
               	$("#editForm").submit();
			}, function(){
			
			}); 
		}
		//打开流转处理窗口
		function showSendWindow(pageUrl){
			//添加和编辑窗口
		 	 art.dialog.open(pageUrl,{
			    	id:'openStepDefWinDiv',
			    	title:'任务督办维护',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:410,
						close:function(){
						   location.reload();
						}
				 });
		}
			//显示权限设置
	function showAuthority(id){  
	       var pageUrl =  "sysFlowDef_stepSupervise!showAuthority.action?id="+id;
	        art.dialog.open(pageUrl,{
			    	id:'openStepDefWinDiv',
			    	title:'查看权限设置',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				   width:290,
				    height:480,
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
			<a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
		<div class="node_box">
			<s:iterator  value="superviseList" status="status">
				<div class="node_frame">
		<table border="0"  cellspacing="0" cellpadding="0" width="100%">		
				<tr >
					<td width="10%"><div class="setup_pic"><div class="setup_pic_font"></div></div></td>
					<td width="90%">
							<table border="0"  cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td class="td_title">计时方式:</td>
								<td class="td_data">
									<s:if test="srType==0">
										按任务接收时间计算
									</s:if>
									<s:elseif test="srType==1">
									    按任务打开时间计算
									</s:elseif>
								</td>
							</tr>
							<tr>
								<td class="td_title">触发条件:</td>
								<td class="td_data">
									<s:if test="srCondition==0">
										超过合理办理时间
									</s:if>
									<s:elseif test="srCondition==1">
									    超过预警办理时间
									</s:elseif>
									&nbsp;&nbsp;&nbsp;延时<s:property value="daleyTime"/>小时办理
								</td>
							</tr>
							<tr>
								<td class="td_title">督办动作:</td>
								<td class="td_data">
									<s:if test="srAction==0">
										通知发起人
									</s:if>
									<s:elseif test="srAction==1">
									    通知当前办理人
									</s:elseif>
									<s:elseif test="srAction==2">
									    继续向下办理
									</s:elseif>
									<s:elseif test="srAction==3">
									    返回上一办理人
									</s:elseif>
									<s:elseif test="srAction==4">
									    触发一个事件
									</s:elseif>
								</td>
							</tr>
							<s:if test="srAction==4">
							      <tr><td class="td_title">触发事件:</td>
								  <td class="td_data"><s:property value="triggerEvent"/></td></tr>		
							</s:if>
							<s:else>   
							      <tr><td class="td_title">通知类型:</td>
							      <td class="td_data">
							      <s:if test="%{remindType!=null&&remindType.indexOf('_sysmsg')!=-1}">
							         <input type="checkbox" checked disabled value="checkbox">
							      </s:if>
							      <s:else>
							          <input type="checkbox"  disabled value="checkbox">
							      </s:else>系统消息
							      <s:if test="%{remindType!=null&&remindType.indexOf(' _email')!=-1}">
							          <input type="checkbox" checked disabled value="checkbox">
							      </s:if>
							      <s:else>
							          <input type="checkbox"  disabled value="checkbox">
							      </s:else>邮件
							      <s:if test="%{remindType!=null&&remindType.indexOf(' _weixin')!=-1}">
							          <input type="checkbox" checked disabled value="checkbox">
							      </s:if>
							      <s:else>
							         <input type="checkbox"  disabled value="checkbox">
							      </s:else>微信
							      <s:if test="%{remindType!=null&&remindType.indexOf('_sms')!=-1}">
							          <input type="checkbox" checked disabled value="checkbox">
							      </s:if>
							      <s:else>
							          <input type="checkbox"  disabled value="checkbox">
							      </s:else>短信
							     </td>
							     </tr>	    
							</s:else>
						</table>
					</td>
					</tr>
					<tr>
					<td></td>
				    <td align="right">
							<div class="node_buttonframe">
								       <!--  <span style=" background:url(iwork_img/shield_go.png) no-repeat; padding-left:20px;margin-right:5px;"><a href='javascript:showAuthority(<s:property value="id"/>);'>查看授权</a></span> -->
								        <span style=" background:url(iwork_img/icon/edit.png) no-repeat; padding-left:20px;margin-right:5px;"><a href='javascript:editItem(<s:property value="id"/>);'>修改</a></span>
								        <span style=" background:url(iwork_img/icon/del.png) no-repeat; padding-left:20px;margin-right:5px;"><a href='javascript:delItem(<s:property value="id"/>);'>删除</a></span>
								        	<s:if test="#status.first">
						          				<span style=" background:url(iwork_img/icon/arrow_down_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href='sysFlowDef_stepSupervise!moveDown.action?actDefId =<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>'>降低优先级</a></span>
						        			</s:if>	
						        			<s:elseif test="#status.last">
						           					 <span style=" background:url(iwork_img/icon/arrow_up_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href='sysFlowDef_stepSupervise!moveUp.action?actDefId=<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>'>提高优先级</a></span>
						        			</s:elseif>	
						        			<s:else>
						           				<span style=" background:url(iwork_img/icon/arrow_up_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href='sysFlowDef_stepSupervise!moveUp.action?actDefId=<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>'>提高优先级</a></span>
	                               				<span style=" background:url(iwork_img/icon/arrow_down_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href='sysFlowDef_stepSupervise!moveDown.action?actDefId =<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&prcDefId=<s:property value="prcDefId"/>&id=<s:property value="id"/>'>降低优先级</a></span>
						        			</s:else>
							</div>					        							        			
					</td>
				</tr>
		</table>
		</div>
		</s:iterator>
		            <s:form name="editForm" id="editForm" theme="simple">
			        	<s:hidden name="actDefId"/>
		                <s:hidden name="actStepDefId"/>
		                <s:hidden name="prcDefId"/>
			        	<s:hidden name="id"/>
			       </s:form>  
	</div>
</div>	
</body>
</html>
