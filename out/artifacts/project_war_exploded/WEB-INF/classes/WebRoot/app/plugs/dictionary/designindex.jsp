<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>   
   	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
   	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
   	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>	
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
	var api = art.dialog.open.api, W = api.opener;
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
				return false;
		   	}
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			    his.location.reload(); return false;
		   } 
		   else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增存储
					 addMetaData(); return false;
				}
		  else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
					doSubmit(); return false;
		  }		
	}); //快捷键
	
	function addDictionary(){
		var groupid = $("#groupid").val();
		if(groupid==''){
			alert('请选择分组ID');
			return;
		}
		var pageUrl = 'sys_dictionary_design_create.action?groupid='+groupid;
		openDialog(pageUrl);
	}
	//编辑基本信息
	function eidtBaseInfo(dicId){
		if(dicId==''){
			alert("您选择的数据选择器不正确");
			return false; 
		}
		var pageUrl = 'sys_dictionary_design_openframe.action?dictionaryId='+dicId+'&dicType=1';
		openDialog(pageUrl,800,500);
	}
	//编辑查询条件
	function openCondition(dicId){
		if(dicId==''){
			alert("您选择的数据选择器不正确");
			return false; 
		}
		var pageUrl = 'sys_dictionary_condition.action?dictionaryId='+dicId;
		openDialog(pageUrl);
	}
	//编辑查询条件
	function openTarget(dicId){
		if(dicId==''){
			alert("您选择的数据选择器不正确");
			return false; 
		}
		var pageUrl = 'sys_dictionary_condition.action?dictionaryId='+dicId;
		openDialog(pageUrl);
	}
	
	function openDialog(pageUrl,width,height){
		if(width==null){
			width = 600;
		}
		if(height==null){
			height= 450;
		}
		art.dialog.open(pageUrl,{
			    	id:'openStepDefWinDiv',
			    	title:'新增选择器',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:width,
				    height:height
				 });
	}
	
	function del(id){
		if(confirm("确认删除当前字典模型吗？")){
			var pageurl = "sys_dictionary_design_del.action?id="+id;
			$.ajax({ 
	            type:'POST',
	            url:pageurl,
	            success:function(msg){ 
	            	  if(msg=="success"){
	                  	alert("移除成功！");
	                  	reload();
	                  } 
	                  else if(responseText=="error"){
	                     alert("移除失败！");
	                  } 
	            }
	        });
		}
		
	}
	function reload(){
  		this.location.reload();
  	}
  	function selectItem(){
  		var text = $("#uuidDiv").html();
		$.copy(text); 	
  	}
	</script>
	<style type="text/css"> 
		.uuid{
			color:red;
			padding-left:10px;
		}
	</style>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
	<div class="tools_nav">
		<a href="javascript:addDictionary();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加选择器</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
	</div>
	</div>
    <!-- 导航区 -->
	<div region="center" style="padding:3px;border:0px;">
			<s:if test="list==null || list.size()==0">
	            <div class="none_item"><img src="iwork_img/report_magnify.png" border="0"> 无字典模型</div>
	 	</s:if> 
	 	<table width="100%"   border="0" cellpadding="0" cellspacing="0">
			<s:iterator  value="list" status="status">
				<tr>
						<td  class="right_left" style="padding-left:5px;"  width="98%">
					   		 <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
					      <tr>
					        <td style="width:100px"  class="right_center">
					        	 <table>
			        	 			<s:if test="dicType==1">
											<tr><td><img src="iwork_img/engine/editonline.png" alt="单选数据字典" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">[单选]</td></tr>	
										</s:if>
			        	 			<s:elseif test="dicType==2">
											<tr><td><img src="iwork_img/engine/editonline.png" alt="复选数据字典" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">[复选]</td></tr>	
										</s:elseif>
			        	 			<s:elseif test="dicType==3">
											<tr><td><img src="iwork_img/engine/editonline.png" alt="行项目数据字典" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">[行项目]</td></tr>	
										</s:elseif>
								</table>
					        </td>
					         <td width="1%"  class="right_center" ><img src="iwork_img/engine/vertical.jpg" width="1" height="85" /></td>
					        <td class="right_center" width="90%"> 
					           		 <table  border="0" cellspacing="0" cellpadding="0" style="width:100%;height:100%">
					           		 	<tr>
					           		 		<td class="data_title"><a href="javascript:del(<s:property value="id"/>);"><img src="iwork_img/engine/bin_del.png" style="margin-left:5px;margin-right:5px;" border="0" /></a></td>
					           		 	</tr> 
					           		 	<tr>
					           		 		<td class="title"> 
												[<s:property value="id"/>]<s:property value="dicName"/><span id="uuidDiv" class="uuid" onclick="selectItem()">[<s:property value="uuid"/>]</span>
					           		 		</td>
					           		 	</tr>
					           		 	<tr>
					           		 		<td class="title">
					           		 			<s:property value="memo"/>
					           		 		</td>
					           		 	</tr>
					           		 	<td style="padding-bottom:5px">
					           		 			<!--  <span class="button2" onMouseOver="this.className='button2_hover'"   onclick="openTarget(<s:property value="id"/>);"  onmouseout="this.className='button2'" onmousedown="this.className='button2_click'" onmouseup="this.className='button2'">选择设置</span>
					           		 			 <span class="button2" onMouseOver="this.className='button2_hover'"   onclick="openCondition(<s:property value="id"/>);"  onmouseout="this.className='button2'" onmousedown="this.className='button2_click'" onmouseup="this.className='button2'">查询条件</span> -->
					           		 			 <a  href="#"    onclick="eidtBaseInfo(<s:property value="id"/>);"   class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">选择器设置</a>
					           		 			 
					           		 	</td>
					           		 </table> 
					          </td>
					        <td ><div class="right_right"></div></td>
					      </tr>
					    </table>
					    </td>
						</tr>
			</s:iterator>
		</table>
			<s:hidden name="groupid" id="groupid"></s:hidden>
	</div>
</body>
</html>
