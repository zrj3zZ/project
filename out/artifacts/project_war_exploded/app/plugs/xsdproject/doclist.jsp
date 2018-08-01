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
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
    <script type="text/javascript">
    var mainFormValidator;
    $().ready(function() {
    	mainFormValidator =  $("#editForm").validate({});
    	mainFormValidator.resetForm();
    });
    function doSearch(){
    	var valid = mainFormValidator.form(); //执行校验操作
		if(!valid){
			return false;
		}
    var searchkey = $("#searchkey").val();
        // if(searchkey==""){
    	//	alert("请输入关键字");
    	//	$("#searchKey").focus();
    	//	return false;
    	//}
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
			border-bottom:1px solid #efefef;
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
			background:transparent url(iwork_img/application_view_list.gif) no-repeat scroll 0px 3px;
		}
		.grid{
			
		}
		.grid th{
			
			 padding:5px;
			 text-align:left;
			 padding-left:20px;
			 font-siz:12px;
			 font-weight:500;
			 border-bottom:1px solid #efefef;
		}
		.grid tr:hover{
			 
		}
		.grid td{
			 background-color:#fff;
			 padding:2px;
			 text-align:left;
			 padding-left:10px;
		}
		.docItem tr{
			line-height:25px;
		}	
		.docItem tr:hover{
			background-color:#FAFAFA; 
		}
		.docItem td a{
			text-decoration:none; 
		}	
	</style>	
  </head>
    <body class="easyui-layout">
     <div region="north" border="false" style="height:80px;vertical-align:bottom">
     <table width="100%">
     	<tr>
     		<td>
     		<span style="font-size:16px;font-family:黑体;"><img style="width:60px" src="iwork_img/archive.gif">项目文件</span>
     		</td>
     		<td  style="vertical-align:bottom">
     		<s:form name="editForm" id="editForm" action="xsd_zqb_project_doc_search.action" theme="simple">
     		<INPUT type="text" name="searchkey" id="searchkey" class="{string:true}" value="${searchkey}"/>&nbsp;&nbsp;<INPUT type="button" value="搜索" name="searchBtn"  onclick="doSearch()"/>
     		</s:form>
     		</td>
     	</tr>
     </table>
      	
      </div>
      <div region="center"  border="false" >
      <div style="padding:10px;">
      		<table class="grid" width="100%" border="0" cellpadding="0" cellspacing="0">  
      		<s:iterator value="doclist"> 
      				<tr> 
      					<th class="itemicon"><%-- <s:property value="projectNo"/>&nbsp;&nbsp;&nbsp; --%> 
      					<s:property value="projectName"/>&nbsp;&nbsp;&nbsp;
      					<s:property value="taskName"/></th>
      				<s:iterator value="list">
	      				<tr> 
	      					<td colspan="3" style="padding-left:20px"> 
	      						<table width="100%" class="docItem">
	      							<tr> 
	      								<td><s:property value="prepare2" escapeHtml="false"/>&nbsp;<a href="<s:property value="fileUrl" escapeHtml="false"/>" target="_blank"><s:property value="fileSrcName"/></a></td>
	      								<td style="text-align:right"><s:property value="uploadTime"/></td>
	      							</tr>
	      						</table> 
	      					</td>
	      				</tr> 
      				</s:iterator>
      		</s:iterator>
      		</table>
      </div>
	</div>
  
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