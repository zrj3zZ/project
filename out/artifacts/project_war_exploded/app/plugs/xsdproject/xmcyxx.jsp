<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>项目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/excanvas.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>  	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
	<script >
	function expExcel(){
   //导入excel
	var pageUrl = "xsd_zqb_project_xmcy_doExp.action";
	$("#ifrmMain").attr("action",pageUrl); 
	$("#ifrmMain").submit();
}
	</script>
  		<style type="text/css"> 
  			
			.title{
				font-size:30px;
				text-align:center;
			}
			.grid{
				width:90%;
				margin-left:auto;
				margin-right:auto;
				border:1px solid #CCCCCC;
			}
			.grid th{
				height:22px;
				background-color:#efefef;
				padding:5px;
			}
			.grid td{
				height:22px;
				padding:5px;
				border-bottom:1px solid #efefef;
			}
  		
  		</style>
	</style>	
  </head>
    <body class="easyui-layout">
      <form name='ifrmMain' id='ifrmMain'  method="post" >
      <div region="north" border="false" > 
      
      	 <div class="title">项目及成员信息</div>
      	 
      	
      </div>
      <div region="center"  border="false" >
      
      	<table width="100%" class="grid">
      	<tr  width="100%" >
      	<td  align="right" colSpan="6">
      	       <a href="javascript:expExcel();" class="easyui-linkbutton l-btn l-btn-plain" plain="true" iconcls="icon-excel-exp">
      	       导出</a>
      	</td>
       </tr>
      	<tr>
      	       <th >序号</th>
				<th >项目名称</th>
				<th >项目负责人</th> 
				<th >项目成员</th> 
				<th >进展</th> 
				<th >最新更新日期</th>
      		</tr>
      		
      	<s:iterator value="xmcyxxList"  status="status"> 
      		<tr>
				<td ><s:property value="#status.count"/></td>
				<td ><s:property value="PROJECTNAME"/></td> 
				<td ><s:property value="OWNER"/></td>
				<td ><s:property value="NAMES"/></td>
				<td ><s:property value="XMJD"/></td>
				<td ><s:property value="GXSJ"/></td>
      		</tr> 
      	</s:iterator>	
      	</table>
		</div>
		</form>
  </body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>