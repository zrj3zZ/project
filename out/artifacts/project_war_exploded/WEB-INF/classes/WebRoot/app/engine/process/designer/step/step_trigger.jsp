<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="title"/>-触发器设置</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/process/process_step.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
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
		.setup_pic1{ height:89px; width:86px; background:url(iwork_img/node_icon6.jpg) no-repeat;}
		.setup_pic2{ height:89px; width:86px; background:url(iwork_img/node_icon7.jpg) no-repeat;}
.setup_pic_font{ color:#000; font-family:Arial; font-size:10px; padding-left:35px; padding-top:50px;-webkit-text-size-adjust:none;}		
	.none_item{height:450px;line-height:450px;text-align:center;font-style:italic;color:#888888; font-family:宋体; font-size:18px;}
	</style>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
		function addTriggerItem(){
				var  prcDefId =$("#editForm_prcDefId").val();
				var actDefId = $("#editForm_actDefId").val();
				var actStepDefId  = $("#editForm_actStepDefId").val();		
				var pageUrl = "sysFlowDef_stepTrigger_add.action?prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
				art.dialog.open(pageUrl,{
					id:'StepTriggerWinDiv',
					title:'节点触发器设置',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				   height:280,
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
				var pageUrl = "sysFlowDef_stepTrigger_edit.action?id="+id+"&prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
				art.dialog.open(pageUrl,{
					id:'StepTriggerWinDiv',
					title:'节点触发器设置',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				   height:280,
						close:function(){
						    location.reload();
						}
				 });
		}
		//删除表单项目
		function delItem(id){
		    art.dialog.confirm('确定删除？',function(){
	           				$.post('sysFlowDef_stepTrigger_delItem.action',{id:id},function(data){
	           				   if(data=="ok"){
	           				      location.reload();
	           				   }
	           				});
	      },function(){}); 
		}
	</script>	
</head>
<body class="easyui-layout">
  <div region="west" border="false" style="width:180px;padding:3px;border-right:1px solid #efefef">
		<s:property value="stepToolbar" escapeHtml="false"/>
	</div>
	<div region="north" border="false" style="height:40px">
		<div class="stepTitle">
			<s:property value="title"/><img src="iwork_img/gear.gif" style="float:right;height:25px" alt="节点设置"/>
		</div>
	</div>
	<div region="center" style="padding:0px;border:0px;">
	<div class="tools_nav">
			<a href="javascript:addTriggerItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加触发器</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
    </div>
	
	<s:if test="modelList==null || modelList.size()==0">
	            <div class="none_item">无设置项</div>
	 </s:if>
		<div class="node_box">
			<s:iterator  value="modelList" status="status">
				<div class="node_frame">	       
				<table border="0"  cellspacing="0" cellpadding="0"  width="100%">
				<tr >
					<td width="10%">
					<s:if test="trigerType==0">
					<div class="setup_pic1"><div class="setup_pic_font"></div></div>
					</s:if>
					<s:elseif test="trigerType==1">
					<div class="setup_pic2"><div class="setup_pic_font"></div></div>
					</s:elseif>
					</td>
					<td width="90%">
							<table border="0"  cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td class="td_title">触发器类型：</td>
								<td class="td_data">
									<s:if test="trigerType==0">
										<input type="radio" disabled checked="checked"/>JAVA触发器 
										<input type="radio" disabled/>WebService触发器 
									</s:if>
									<s:elseif test="trigerType==1">
										<input type="radio" disabled/>JAVA触发器 
										<input type="radio" disabled checked="checked"/>WebService触发器 
									</s:elseif>
								</td>
							</tr>
							<tr>
								<td class="td_title">事件类型：</td>
								<td class="td_data">
									<s:property value="eventTypeName_t"/>
								</td>
							</tr>
							<tr>
								<td class="td_title">参考值：</td>
								<td class="td_data">
									<s:property value="eventParam"/>
								</td>
							</tr>
							<tr>
								<td class="td_title">业务描述：</td>
								<td class="td_data">
									<s:property value="eventMemo"/>
								</td>
							</tr>
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
					         <span style=" background:url(iwork_img/icon/del.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:delItem(<s:property value='id'/>);">删除</a></span>					                  
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
