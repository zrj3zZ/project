<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>权限</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript">
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
            var purview=$.trim($('#editForm_baseModel_purview').val());//不能超过500                       
            $('#editForm_baseModel_purview').val(purview);
                
           if(length2(purview)>500){
                 lhgdialog.tips('报表权限过长!',2);
                 $('#editForm_baseModel_purview').focus();
                 return false;
          }       
          return true;
      }
	//权限地址簿
		function openAuthorityBook(obj){
			var code = obj.value;	
			var url = "authorityAddressBookAction!index.action?code=" + code;
			window.showModalDialog(url, obj, 'dialogWidth:650px;dialogHeight:535px;help:no;resizable:no;status:no;location:no');
		} 
	</script>

  </head>
  
  <body class="easyui-layout">
     <div region="center" style="padding:3px;border-top:0px;">					
					<div style="border-bottom:1px solid #efefef;margin-bottom:3px;text-align:left;padding-right:20px;backgroud-color:#efefef">
						<a href="javascript:save();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>						
					</div>
					<div>
						<s:form name="editForm" id="editForm" action="ireport_designer_baseInfo_Purview_save.action" theme="simple">	
						<div>报表权限：</div>
						<div>
						  <s:textarea readonly="true" cssStyle="width:500px;height:350px;overflow:auto;" name="baseModel.purview"/>
                          <a class="easyui-linkbutton" href="javascript:openAuthorityBook(document.getElementById('editForm_baseModel_purview'));" iconCls="icon-add" plain="true">权限地址簿</a>
                       </div>			
						<s:hidden name='baseModel.id'></s:hidden>
						<s:hidden name='baseModel.groupid'></s:hidden>
						<s:hidden name='baseModel.rpName'></s:hidden>
						<s:hidden name='baseModel.rpType'></s:hidden>
						<s:hidden name='baseModel.dsType'></s:hidden>
						<s:hidden name='baseModel.dsId'></s:hidden>
						<s:hidden name='baseModel.sqlScript'></s:hidden>
						<s:hidden name='baseModel.chartType'></s:hidden>
						<s:hidden name='baseModel.rowNum'></s:hidden>
						<s:hidden name='baseModel.condition'></s:hidden>
						<s:hidden name='baseModel.groupCount'></s:hidden>
						<s:hidden name='baseModel.status'></s:hidden>
						<s:hidden name='baseModel.memo'></s:hidden>
						<s:hidden name='baseModel.master'></s:hidden>
					<!-- 	<s:hidden name='baseModel.purview'></s:hidden>   -->
						</s:form>
					</div>	
  </body>
</html>
