<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
    
    <title>自定义按钮管理中心</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
    	<link rel="stylesheet" type="text/css" href="iwork_css/process/process_step.css"> 
    <script type="text/javascript" src="iwork_js/commons.js"></script>        
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <style type="text/css">
		/*节点设置*/
		.node_body{ background-color:#f6f9fb;width:100%;}
		.node_nav{  height:32px; padding-left:20px; line-height:32px;}
		.node_box{  background:url(iwork_img/node_frame_page.jpg) repeat-x ; }
		.node_frame{ padding:15px; clear:both; line-height:30px; color:#6c6c6c; background-color:#f1f8fc; width:100%\9; border-bottom:1px solid #838383;}
		.node_line{  height:2px;  background:url(iwork_img/horizontal.jpg) repeat-x; width:100%; }
		.node_buttonframe{width:360px; float:right;align:right}
		.node_buttonframe a{ text-decoration:none;color:#6c6c6c}
		.setup_pic{ height:89px; width:86px; background:url(iwork_img/node_icon8.jpg) no-repeat;}
.setup_pic_font{ color:#000; font-family:Arial; font-size:10px; padding-left:35px; padding-top:50px;-webkit-text-size-adjust:none;}
.none_item{height:460px;line-height:460px;text-align:center;font-style:italic;color:#888888; font-family:宋体; font-size:18px;}
	</style>
    
	<script type="text/javascript">
	//全局变量
	var api = art.dialog.open.api, W = api.opener;
	//添加
	function addDefButton(){
	       var actDefId =$('#actDefId').val();
	       var actStepDefId =$('#actStepDefId').val();
	        var prcDefId =$('#prcDefId').val();
			 var pageUrl =  "sysFlowDef_stepDIYBtn_add.action?prcDefId="+prcDefId+"&actDefId="+encodeURI(actDefId)+"&actStepDefId="+encodeURI(actStepDefId);
			     art.dialog.open(pageUrl,{
			    	id:'db_add',
			    	title:'添加自定义按钮',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:550,
					height:400,
					close:function(){
					    location.reload();
					}
				 });
	}
	//编辑
	function editDefButton(id){
			 var pageUrl =  "sysFlowDef_stepDIYBtn_edit.action?id="+id;
			 art.dialog.open(pageUrl,{
			    	id:'db_add',
			    	title:'添加自定义按钮',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:550,
					height:400,
					close:function(){
					    location.reload();
					}
				 });
	}
	//显示权限设置
	function showAuthority(id){  
	       var pageUrl =  "sysFlowDef_stepDIYBtn_show.action?id="+id;
	        art.dialog.open(pageUrl,{
			    	id:'db_add',
			    	title:'查看权限设置',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				   width:290,
						height:480
				 });
	}
	//删除
	function delDefButton(id){
	     if(confirm('确定删除？')){
	            $.post('sysFlowDef_stepDIYBtn_del.action',{id:id},function(data){
	           		 if(data=="ok"){
	                	location.reload();
	            	}
	             });
	      }    
	}
	//下移
	function moveDown(id){
	     $.post('sysFlowDef_stepDIYBtn_moveDown.action',{id:id},function(data){
	            if(data=="ok"){
	                location.reload();
	            }
	     });	     
	}
   //上移
   function moveUp(id){
        $.post('sysFlowDef_stepDIYBtn_moveUp.action',{id:id},function(data){
	            if(data=="ok"){
	                location.reload();
	            }
	     });
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
    <!-- 维护区 -->  
    <div region="center" style="padding:0px;border:0px">
        <div class="tools_nav">
		      <a href="javascript:addDefButton();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加自定义按钮</a>
		      <a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
         </div>
	    
	     <s:if test="list==null || list.size()==0">
	            <div class="none_item">无设置项</div>
	     </s:if>
	     
	    <div class="node_box">    
	       <s:iterator value="list" status="sta">
	          <div class="node_frame">
	              <table border="0"  cellspacing="0" cellpadding="0"  width="100%">	        
	                 <tr> 
	                  <td width="10%"><div class="setup_pic"><div class="setup_pic_font"></div></div></td>
	                  <td width="95%">
							<table border="0"  cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td style="padding-left:10px;padding-right:15px;width:25%;text-align:right">按钮名称：</td>
								<td colspan="2" style="width:75%">
									<s:property value="buttonName"/>								
								</td>
							</tr> 
							<tr>
								<td style="padding-left:10px;padding-right:15px;width:25%;text-align:right">用途描述：</td>
								<td colspan="2" style="width:75%">
									<s:property value="useDesc"/>								
								</td>
							</tr>
							<tr>
								<td style="padding-left:10px;padding-right:15px;width:25%;text-align:right">链接：</td>
								<td colspan="2" style="width:75%">
									<s:property value="url"/>
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
								        <span style=" background:url(iwork_img/icon/edit.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:editDefButton(<s:property value='id'/>);">修改</a></span>
								        <span style=" background:url(iwork_img/icon/del.png) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:delDefButton(<s:property value='id'/>);">删除</a></span>
								        	<s:if test="#sta.first">
						          				<span style=" background:url(iwork_img/icon/arrow_down_blue.gif) no-repeat; padding-left:20px;margin-right:5px;"><a href="javascript:moveDown(<s:property value='id'/>);">降低优先级</a></span>
						        			</s:if>	
						        			<s:elseif test="#sta.last">
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
	  
	    <s:hidden name="actDefId" theme="simple"/> 
	    <s:hidden name="actStepDefId" theme="simple"/>
	    <s:hidden name="prcDefId" theme="simple"/> 
    </div>
  </div>  
  </body>
</html>
