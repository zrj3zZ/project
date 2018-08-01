<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>  
<script type="text/javascript">
  //全局变量
	 function saveBaseInfo(){
          var bool= validate();
          if(bool){
             var options = {
				error:function(){
         			  lhgdialog.tips("保存失败！",2);
     		 	},
				success:function(responseText, statusText, xhr, $form){
		           if(responseText=="ok"){
		              lhgdialog.tips("保存成功！",2); 
		              setTimeout('cancel();',1000);
		           }
    			  } 
			   };
			 $('#editForm').ajaxSubmit(options);
        	}
          else{
          		return ;
         }
      }
            //表单验证
      function validate(){
          return true;
      }
      //关闭窗口
      function cancel(){
          api.close();
      }
</script>
<style> 
		<!--
			#header { background:#6cf;}
			#title { height:20px; background:#EFEFEF; border-bottom:1px solid #990000; font:12px; font-family:宋体; padding-left:5px; padding-top:5px;}
			#baseframe { margin:0px;background:#FFFFFF;}
			#baseinfo {background:#FFFFFF; padding:5px;font:12px; font-family:宋体;}
			.toobar{
				 border-bottom:1px solid #990000; 
				 padding-bottom:5px; 
			}
			/*只读数据样式*/
			.readonly_data {
				vertical-align:bottom;
				font-size: 12px;
				line-height: 20px;
				color: #888888;
				padding-right:10px;
				border-bottom:1px #999999 dotted;
				font-family:"宋体";
				line-height:15px;
			}
			.table_form{
				font-family:"宋体";
				font-size: 12px;
			}
			/*数据字段标题样式*/
			.td_title {
			color:#004080;
				line-height: 23px;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				padding-left:10px;
				white-space:nowrap;
				border-bottom:1px #999999 thick;
				vertical-align:middle;
				font-family:"宋体";
			}
			/*数据字段内容样式*/
			.td_data {
				color:#0000FF;
				line-height: 23px;
				text-align: left;
				padding-left: 3px;
				font-size: 12px;
				font-family:"宋体";
				border-bottom:1px #999999 dotted;
				vertical-align:middle;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
				
			}
		-->
</style>
</head>
<body class="easyui-layout">
	<div region="center" style="padding:0px;border:0px;">
		<div style="border-bottom:1px solid #efefef;margin-bottom:0px;float:left;width:100%">
		       <a href="javascript:saveBaseInfo();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>				
		</div> 
		<div id="baseframe" style="border-bottom:1px solid #efefef;margin-bottom:0px;float:left;width:100%">
			<s:form name="editForm" id="editForm" action="ireport_designer_baseInfo_save.action" theme="simple">
				<table class = "table_form">
					<tr>
							<td class = "td_title">报表名称:</td>
							<td class = "td_data"><s:textfield  name="baseModel.rpName" theme="simple"></s:textfield></td>
					</tr>
					<tr>
							<td class = "td_title">显示行数:</td>
							<td class = "td_data"><s:textfield  name="baseModel.rowNum" cssStyle="width:30px" theme="simple"/></td>
					</tr>
					<tr>
							<td class = "td_title">是否使用查询条件:</td>
							<td class = "td_data"><s:radio  name="baseModel.isCondition"  listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" value="baseModel.isCondition" theme="simple"/></td>
					</tr>
					<tr>
							<td class = "td_title">查询条件显示列数:</td>
							<td class = "td_data"><s:radio  name="baseModel.groupCount"  listKey="key" listValue="value"  list="#{'1':'一列','2':'二列','3':'三列'}" value="baseModel.groupCount" theme="simple"/></td>
					</tr>
					<tr>
							<td class = "td_title">报表状态:</td>
							<td class = "readonly_data"><s:radio  name="baseModel.status"  listKey="key" listValue="value"  list="#{'1':'开启','0':'关闭'}" value="baseModel.status" theme="simple"/></td>
					</tr>
					<tr>
							<td class = "td_title">分组目录:</td>
							<td class = "td_data"><s:textfield  name="baseModel.groupid" cssStyle="width:80px" theme="simple"/></td>
					</tr>
					<tr>
							<td class = "td_title">唯一识别码:</td>
							<td class = "td_data"><s:property value="baseModel.uuid"/><s:hidden  name="baseModel.uuid" cssStyle="width:80px" theme="simple"/></td>
					</tr>
					<tr>
							<td class = "td_title">管理员:</td>
							<td class = "td_data"><s:textfield  name="baseModel.master" cssStyle="width:300px" theme="simple"/></td>
					</tr>
					
					<tr>
							<td class = "td_title">报表描述:</td>
							<td class = "td_data"><s:textarea name="baseModel.memo" cssStyle="width:500px;height:100px" theme="simple"/></td>
					</tr>
					<s:hidden name="baseModel.id"></s:hidden> 
					<s:hidden name="baseModel.rpType"></s:hidden> 
					<s:hidden name="baseModel.chartType"></s:hidden> 
					<s:hidden name="baseModel.dsId"></s:hidden> 
					<s:hidden name="baseModel.sqlScript"></s:hidden> 
				</table>
			</s:form>
		</div>
	</div>
</body>
</html>
