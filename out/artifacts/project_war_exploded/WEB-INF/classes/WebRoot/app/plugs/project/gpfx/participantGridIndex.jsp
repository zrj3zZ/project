<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>项目参与人统计</title>
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
	var pageUrl = "zqb_gpfx_project_participant_Excel_DoExp.action";
	$("#ifrmMain").attr("action",pageUrl); 
	$("#ifrmMain").submit();
}
	</script>
  		<style type="text/css">
  			.grid {
  			 
			  padding:5px;
			  vertical-align:top;
			}
  			.grid table{
  				width:100%;
  				border:1px solid #efefef;
  			}
  			.grid th{
  				
  				padding:5px;
  				font-size:12px;
  				font-weight:500;
  				height:20px;
  				background-color:#ffffee;
  				border-bottom:1px solid #ccc;
  			}
  			.grid tr:hover{
  				background-color:#efefef;
  			}
  			.grid td{
  			
  				padding:5px;
  				line-height:16px;
  				
  			}
  			.gridTitle{
  				padding-left:25px;
  				height:20px;
  				font-size:14px;
  				font-family:黑体;
  				background:transparent url(iwork_img/table_multiple.png) no-repeat scroll 5px 1px;
  			}
  		</style>
	</style>	
  </head>
    <body class="easyui-layout">
   
  
      <div region="north" border="false" >
      	<div class="tools_nav">
      	<span style="float:right;padding-right:10px"><a href="zqb_gpfx_project_participantBarChart.action">图表</a>|表格</span>
      	</div>
      	 
      </div>
      <div region="center"  border="false" >
      <form name='ifrmMain' id='ifrmMain'  method="post" >
      
      	<table width="50%" >	 	     		
      		<tr>
      			<td  class="gridTitle" >按项目参与人统计</td>
      			<td align="right">
      			<a href="javascript:expExcel();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a>
      			</td>
      			
      				
      		</tr>
      		<tr>
      			<td class="grid" colspan="2"><s:property value="typePieData" escapeHtml="false"/></td>
      		</tr>
      		
      	</table>
      	</form>
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