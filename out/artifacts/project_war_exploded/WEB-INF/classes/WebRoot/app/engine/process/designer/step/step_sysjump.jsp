<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="title"/>-系统规则设置</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/process/process_step.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		/*节点设置*/
		.node_body{ background-color:#f6f9fb;width:100%;}
		.node_nav{  height:32px; padding-left:20px; line-height:32px;}
		.node_box{  background:url(iwork_img/node_frame_page.jpg) repeat-x ; }
		.node_frame{ padding:15px; clear:both; line-height:30px; color:#6c6c6c; background-color:#f1f8fc;width:100%\9; border-bottom:1px solid #838383; }
		.node_line{  height:2px;  background:url(iwork_img/horizontal.jpg) repeat-x; width:100%; }
		.node_buttonframe{width:360px; float:right;align:right;border-top:1px solid #efefef}
		.node_buttonframe a{ text-decoration:none;color:#6c6c6c}
		.setup_pic{ height:89px; width:86px; background:url(iwork_img/node_icon4.jpg) no-repeat;}
		.setup_pic_font{ color:#000; font-family:Arial; font-size:10px; padding-left:35px; padding-top:50px;-webkit-text-size-adjust:none;}
		.none_item{height:450px;line-height:450px;text-align:center;font-style:italic;color:#888888; font-family:宋体; font-size:18px;}		
	</style>
	<script type="text/javascript">
	    var api = art.dialog.open.api, W = api.opener;
	    //增加
		function addSystemJump(){  
		     var  prcDefId =$("#editForm_prcDefId").val();
		     var actDefId = $("#editForm_actDefId").val();
	         var actStepDefId  = $("#editForm_actStepDefId").val();	
		    var pageUrl= "sysFlowDef_stepSysJump_add.action?prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
		     art.dialog.open(pageUrl,{
						id:'lg_add',
						title:'规则设置',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:650,
					    height:510,
					    close:function(){
						    location.reload();
						}
					 });
	}
	//修改
	function editItem(id){
		     var  prcDefId =$("#editForm_prcDefId").val();
		     var actDefId = $("#editForm_actDefId").val();
	         var actStepDefId  = $("#editForm_actStepDefId").val();	
	        var pageUrl="sysFlowDef_stepSysJump_edit.action?id="+id+"&prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
	         art.dialog.open(pageUrl,{
						id:'lg_add',
						title:'规则设置',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:650,
					    height:510,
					    close:function(){
						    location.reload();
						}
					 });
	}
	//删除
	function delItem(id){
		 art.dialog.confirm('确定删除？',function(){
	           				$.post('sysFlowDef_stepSysJump_delItem.action',{id:id},function(data){
	           				   if(data=="ok"){
	           				      location.reload();
	           				   }
	           				   else if(data=="error"){
	           				      art.dialog.tips("删除失败！",2);
	           				   }
	           				});
	      },function(){}); 
	}
	//查看授权
	function showAuthority(id){  
	       var pageUrl =  "sysFlowDef_stepSysJump_show.action?id="+id;
	       art.dialog.open(pageUrl,{
						id:'db_show',
						title:'查看权限设置',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:500,
					    height:380
					 });
	}
	//下移
	function moveDown(id){
	     var  prcDefId =$("#editForm_prcDefId").val();
		 var actDefId = $("#editForm_actDefId").val();
	     var actStepDefId  = $("#editForm_actStepDefId").val();	
	     $.post('sysFlowDef_stepSysJump_moveDown.action',{id:id,prcDefId:prcDefId,actDefId:actDefId,actStepDefId:actStepDefId},function(data){
	            if(data=="ok"){
	                location.reload();
	            }
	     });	     
	}
   //上移
   function moveUp(id){
	     var  prcDefId =$("#editForm_prcDefId").val();
		 var actDefId = $("#editForm_actDefId").val();
	     var actStepDefId  = $("#editForm_actStepDefId").val();	   
        $.post('sysFlowDef_stepSysJump_moveUp.action',{id:id,prcDefId:prcDefId,actDefId:actDefId,actStepDefId:actStepDefId},function(data){
	            if(data=="ok"){
	                location.reload();
	            }
	     });
   }
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<s:if test="stepToolbar!=\"\"">
	<div region="west" border="false" style="width:180px;padding:3px;border-right:1px solid #efefef">
		<s:property value="stepToolbar" escapeHtml="false"/>
	</div>
	</s:if>
	<div region="north" border="false" style="height:40px">
		<div class="stepTitle">
			<s:property value="title"/><img src="iwork_img/gear.gif" style="float:right;height:25px" alt="节点设置"/>
		</div>
	</div>
	<div region="center" style="padding:0px;border:0px">
					<div class="tools_nav">
						<a href="javascript:addSystemJump();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加系统规则</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
					</div>
					<s:if test="sysJumpList==null || sysJumpList.size()==0">   
	            		<div class="none_item">无设置项</div>
	     			</s:if>
					
					<div class="node_box">
						<s:iterator value="sysJumpList" status="status">
							<div class="node_frame">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="10%" ><div class="setup_pic"><div class="setup_pic_font"></div></div></td>
										<td width="95%" >
											<table  width="100%" border="0" cellspacing="0" cellpadding="0">
												 <tr>
											        <td  style="padding-left:10px;padding-right:15px;width:15%;text-align:right">规则表达式:</td>
											        <td colspan="2" style="width:85%">
											        <s:property value="sjExpression"/>
											       </td>
											      </tr>
												 <tr>
											        <td  style="padding-left:10px;padding-right:15px;width:15%;text-align:right">动作设置:</td>
											        <td colspan="2" style="padding-left:10px;width:85%">
											        				
											        				<s:if test="isJump==1">
									        							<input type="checkbox" checked disabled value="checkbox">
									        						</s:if>
									        						<s:else>
									        							<input type="checkbox"  disabled value="checkbox">
									        						</s:else>
							        								是否跳转
							        								
									        						<s:if test="isNextuser==1">
										        							<input type="checkbox" checked disabled value="checkbox"> 
										        						</s:if>
										        						<s:else>
										        							<input type="checkbox"  disabled value="checkbox">
										        						</s:else>
								        								 是否指定下一办理人
								        								<s:if test="isRemind==1">
										        							<input type="checkbox" checked disabled value="checkbox">
										        						</s:if>
										        						<s:else>
										        							<input type="checkbox"  disabled value="checkbox"> 
										        						</s:else>
										        						是否发送通知
											        </td>
											      </tr>
											      <tr>
											        <td></td>
											        <td colspan="2">
							        								<s:if test="isJump==1">
									        							[跳转节点]【<s:property value="targetStep"/>】
									        						</s:if>
								        								<s:if test="isNextuser==1">
										        							[指定下一办理人为]【<s:property value="nextUser"/>】<br/>
										        						</s:if>
											        </td>
											      </tr>
											      <tr>
											        <td></td>
											        <td colspan="2">
							        								<s:if test="isRemind==1">
									        							[通知用户为]【<s:property value="msgUsers"/>】<br/>
									        							[通知类型]<s:if test="msgSysmsg==1">
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
									        						</s:if>
											        </td>
											      </tr>
											     
											</table>
										</td>
									</tr>
									<tr>
							        <td></td>
							        <td align="right">
								        <div class="node_buttonframe">
								        <span style=" background:url(iwork_img/shield_go.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:showAuthority(<s:property value='id'/>);">查看授权</a></span>
								        <span style=" background:url(iwork_img/icon/edit.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:editItem(<s:property value='id'/>);">修改</a></span>
								        <span style=" background:url(iwork_img/icon/del.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:delItem(<s:property value='id'/>);">删除</a></span>
								        	<s:if test="#status.first">
						          				<span style=" background:url(iwork_img/icon/arrow_down_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:moveDown(<s:property value='id'/>);">降低优先级</a></span>
						        			</s:if>	
						        			<s:elseif test="#status.last">
						           					 <span style=" background:url(iwork_img/icon/arrow_up_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:moveUp(<s:property value='id'/>);">提高优先级</a></span>
						        			</s:elseif>	
						        			<s:else>
						           				<span style=" background:url(iwork_img/icon/arrow_up_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:moveUp(<s:property value='id'/>);">提高优先级</a></span>
	                               				<span style=" background:url(iwork_img/icon/arrow_down_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:moveDown(<s:property value='id'/>);">降低优先级</a></span>
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
