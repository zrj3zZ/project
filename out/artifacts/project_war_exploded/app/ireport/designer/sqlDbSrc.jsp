<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>数据源设置</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript">
	//测试
	function test(){  
	     var dsId = $('#editForm_baseModel_dsId').val();
	     var sqlScript = $.trim($('#editForm_baseModel_sqlScript').val());     
	     $.post('ireport_designer_baseInfo_dbSourse_test.action',{dsId:dsId,sqlScript:sqlScript},function(data){
	    	
	          if(data=='ok'){
	                alert('测试成功！');
	            }
	            else{
	                alert('测试失败！');
	            }
	     
	     });
	}
	//保存
    function save(){
          var bool= validate();
          if(bool){
			 $('#editForm').submit();
        	}
          else{
          		return ;
        	}
      }
      //表单验证
      function validate(){
            var sqlScript=$.trim($('#editForm_baseModel_sqlScript').val());//不能超过800                       
            $('#editForm_baseModel_sqlScript').val(sqlScript);
                
           if(length2(sqlScript)>800){
                 alert('添加SQL代码过长!');	
                 $('#editForm_baseModel_sqlScript').focus();
                 return false;
          }       
          return true;
      }
	</script>
     <style type="text/css">
		.td_title {
		        width:15%;
				color:#004080;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				white-space:nowrap;
				vertical-align:middle;
				font-family:"微软雅黑";
				height:15px;			
			}
		.td_data{
			color:#0000FF;
			text-align:left;
			padding-left:3px;
			font-size: 12px;
			vertical-align:middle;
			word-wrap:break-word;
			word-break:break-all;
			font-weight:500;
			line-height:12px;
			padding-top:5px;
			font-family:"微软雅黑";
			height:15px;
		}
   </style>  
  </head>
  
  <body class="easyui-layout">
     <div region="center" style="padding:3px;border-top:0px;">					
					<div style="border-bottom:1px solid #efefef;margin-bottom:3px;text-align:left;padding-right:20px;backgroud-color:#efefef">
						<a href="javascript:save();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
						<a href="javascript:test();" class="easyui-linkbutton" plain="true" iconCls="icon-help">测试</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>						
					</div>
					<div>
						<s:form name="editForm" id="editForm" action="ireport_designer_baseInfo_dbSourse_save.action" theme="simple">
						<table width="100%">	
							<tr>
								<td class="td_title">添加SQL数据源：</td>
								<td class="td_data"><s:select  label="添加SQL数据源：" name="baseModel.dsId"  list="%{dbsrcList}" listKey="id" listValue="dsrcTitle"></s:select></td>
							</tr>
							<tr>
								<td class="td_title">添加SQL代码：</td>
								<td class="td_data"><s:textarea cssStyle="width:500px;height:350px;overflow:auto;" name="baseModel.sqlScript"/></td>
							</tr>	
                       </table>	
						<s:hidden name='baseModel.id'></s:hidden>
						<s:hidden name='baseModel.groupid'></s:hidden>
						<s:hidden name='baseModel.rpName'></s:hidden>
						<s:hidden name='baseModel.rpType'></s:hidden>
						<s:hidden name='baseModel.dsType'></s:hidden>
						<s:hidden name='baseModel.chartType'></s:hidden>
						<s:hidden name='baseModel.rowNum'></s:hidden>
						<s:hidden name='baseModel.condition'></s:hidden>
						<s:hidden name='baseModel.groupCount'></s:hidden>
						<s:hidden name='baseModel.status'></s:hidden>
						<s:hidden name='baseModel.memo'></s:hidden>
						<s:hidden name='baseModel.master'></s:hidden>
					    <s:hidden name='baseModel.purview'></s:hidden> 
						</s:form>
					</div>	
  </body>
</html>
