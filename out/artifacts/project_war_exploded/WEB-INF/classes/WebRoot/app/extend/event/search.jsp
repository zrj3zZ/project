<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>项目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript">
   		function dofilterYear(obj){
   			$("#year").val(obj);
   			$("#editForm").attr("action","zqb_meeting_index.action");
   			$("#editForm").submit();
   		}
   		function dofilterMeet(obj){
   			$("#meettype").val(obj);
   			$("#editForm").attr("action","zqb_meeting_index.action");
   			$("#editForm").submit();
   		}
   		function dofilterGroup(obj){

   			$("#grouptype").val(obj);
   			$("#editForm").attr("action","zqb_meeting_index.action");
   			$("#editForm").submit();
   		}
   		function dofilterStatus(obj){
   			$("#status").val(obj);
   			$("#editForm").attr("action","zqb_meeting_index.action");
   			$("#editForm").submit();
   		}
    	function addItem(){
			var formid = 116;
			var demId = 38; 
			var customerno = $("#customerno").val();
			if(customerno==""){
				alert("未发现客户编号，无法添加信息披露事项");
				return false;
			}
			var pageurl = 'createFormInstance.action?formid='+formid+'&demId='+demId+'&KHBH='+customerno; 
			 var title = "";
			 var height=580; 
			 var width = 900;
			 var dialogId = "projectItem";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
			return;    
		}
		
		function showItem(id){
			 var title = "";
			 var height=580;
			 var width = 900;
			 var pageurl = "openFormPage.action?formid=96&instanceId="+id+"&demId=26";
			 var dialogId = "meetItem";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
		}
		
		function closeItem(instanceid,projectNo){
			if(confirm('确定关闭当前项目吗？')){
				var pageUrl = "zqb_project_remove_item.action?instanceid="+instanceid+"&projectNo="+projectNo+"&taskid=0"; 
				$.post(pageUrl,{},function(data){ 
	       			if(data=='success'){
	       				this.location.reload();
	       			}else{
	       				alert("关闭项目失败");;
	       			}
	     		});
     		}
		}
		function setFinish(instanceid){
			var title = "设置会议已召开";
			 var height=300;
			 var width = 400;
			 var pageurl = "zqb_meeting_edit.action?instanceid="+instanceid;
			 var dialogId = "meetEdit";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
		}
		function setRetime(instanceid){
			var title = "设置会议延期时间";
			 var height=300;
			 var width = 400;
			 var pageurl = "zqb_meeting_meetRetime.action?instanceid="+instanceid;
			 var dialogId = "meetRetime";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
		}
		
		function uploadifyDialog(dialogId,fieldName,divId,sizeLimit,multi,fileExt,fileDesc){
		if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
			alert('参数不正确');
			return false;
		}
		var pageurl = 'showUploadifyPage.action?parentColId='+fieldName+'&parentDivId='+divId+'&sizeLimit='+sizeLimit+'&multi='+multi+'&fileExt='+fileExt+'&fileDesc='+fileDesc;
			var title = "设置会议延期时间";
			 var height=600;
			 var width = 800;
			 var dialogId = "meetRetime";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
		return false;
	}
	
		function showUploadifyPageFJ(){
			uploadifyDialog('FJ','FJ','DIVFJ','','true','','');
		}
		function selCustomerItem(){
			var item = $("#selCustomer").val();
			//$("#customerno").val(item); 
			$("#editForm").attr("action","zqb_meeting_index.action?customerno="+item);
			$("#editForm").submit();
		}
    </script>
    <style type="text/css">
		 body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
		.groupTitle{
			font-family:黑体;
			font-size:12px;
			text-align:left;
			color:#666;
			height-line:20px;
			padding:5px;
			padding-left:15px;
		}
		.itemList{  
			font-family:宋体;
			font-size:12px;
			height:200px;
			padding-left:15px;
		}
		.itemList td{
			list-style:none;
			height:20px;
			padding:2px;
			padding-left:20px;
			
		}
		.itemList tr:hover{
			color:#0000ff;
			cursor:pointer;
		}
		.itemList  td{
			font-size:12px;
		}
		
		.itemicon{
			padding-left:25px;
			background:transparent url(iwork_img/pin.png) no-repeat scroll 0px 3px;
		}
		.selectBar{
			border:1px solid #efefef;
			margin:5px;
			background:#FDFDFD;
		}
		.selectBar td{
			vertical-align:middle;
			height:20px;
		}
		
		.selectBar td linkbtn{
			color:#0000FF;
			text-decoration: none;
		}
	</style>	
  </head> 
    <body class="easyui-layout">
  
      <div region="north" border="false" >
      	<div class="tools_nav">
         	<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">申报重大事项</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
        </div>
        <fieldset class="selectBar">
      		<legend>筛选</legend>
      		<s:if test="customerList!=null&&customerList.size()>0">
	      		<div>
	      			<s:select list="customerList" name="selCustomer" value="%{customerno}"  id="selCustomer" onchange="selCustomerItem()"  headerKey=""  headerValue="-----------------选择客户----------------" listKey="KHBH" listValue="KHMC" ></s:select>
	      		</div>
      		</s:if>
      		<div>
      		<s:property value="filterbar"  escapeHtml="false"/>
      		</div>
      	</fieldset>
      </div>
      <div region="center"  border="false" >
      	<div  class="itemList" id="itemList">
      		<table width="100%"> 
      		<s:iterator value="runList">
      				<tr>
      					<td class="itemicon" ><s:property value="SXMC"/> </td>
      					<td ><s:property value="SXLX"/></td>
      					<td ><s:date name="PLANTIME" format="yyyy-MM-dd"/></td>
      					<td ><s:property value="GLGSMC"/></td>
      					<td>
      	
      					
      					<a href="javascript:setFinish('<s:property value="INSTANCEID"/>')"></a>&nbsp;&nbsp;
      					<a href="javascript:setRetime('<s:property value="INSTANCEID"/>')">留言</a>&nbsp;&nbsp;
      					<a href="javascript:closeItem(<s:property value="INSTANCEID"/>,'<s:property value="ID"/>')">取消</a>
      						
      						
      						
      					
      					
      					</td>
      				</tr> 
      		</s:iterator>
      		</table>
      	</div>
	 <s:form id="editForm" name="editForm">
  	 <s:hidden id="customerno" name="customerno"></s:hidden>
  	<s:hidden id="year" name="year"></s:hidden>
  	<s:hidden id="grouptype" name="grouptype"></s:hidden>
  	<s:hidden id="meettype" name="meettype"></s:hidden>
  	<s:hidden id="status" name="status"></s:hidden>
   </s:form>
  </div>
 
  </body>
</html>
		
		
		
		
		
		
		
		
		
		
		
		
		