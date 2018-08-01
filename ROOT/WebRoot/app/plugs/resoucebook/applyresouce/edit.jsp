<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>编辑</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    
    <style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			width:150px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
	</style>	
    <script type="text/javascript">
      //全局变量
     var api = art.dialog.open.api, W = api.opener; 
     var mainFormValidator;
     $().ready(function() {
    		 mainFormValidator =  $("#editForm").validate({
				debug:false
			 });
			 mainFormValidator.resetForm();	
	 });
	 
    //保存
    function save(){
    	var startTime = $("#d5221").val();  
        var endTIime =  $("#d5222").val(); 
        if(startTime > endTIime){  
            art.dialog.tips("结束时间小于开始时间！");
            $("#"+endTIimeID).val(""); 
            return false;  
          } 
   			 var valid = mainFormValidator.form(); //执行校验操作
				if(!valid){
					return;
				}
             var options = {
				error:errorFunc,
				success:successFunc 
			   };
			 $('#editForm').ajaxSubmit(options);
      }
      errorFunc=function(){
          alert("保存失败！");
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="success"){
               alert("保存成功!");
              closeDialog();
           }else if(responseText=="isadd"){
             	alert("此时间段已被预定,请重新选择!");
           }
      }
	    //关闭窗口
	   function closeDialog(){
	   		api.close(); 
	   }
      </script>
  </head>
    <body class="easyui-layout">
      <div region="center" border="false" style="padding:3px;">
		 <s:form name="editForm" id= "editForm" theme="simple" action="resbook_runtime_save">
				<table class="font"  cellspacing="10">
					<tr> 
						<td align=right  class="form_title">
							资源ID/资源名称：
							</td><td class="form_data"><s:property value="model.resouceid"/>/<s:property value="model.resouce"/>
						</td>
					</tr>
					<tr>
						<td align=right  class="form_title">
							预定人帐号/姓名：
							</td><td  class="form_data"><s:property value="model.userid"/>[<s:property value="model.username"/>]
						</td>
					</tr>					
					<tr>
						<td align=right  class="form_title">
							预定开始/结束日期：
							</td><td  class="form_data">
							<s:textfield name="model.begintime" id="d5221" onfocus="WdatePicker('{sShowWeek:true}')" cssClass = "{dateISO:true,required:false}" cssStyle="width:100px;"></s:textfield>&nbsp;/&nbsp;
							<s:textfield name="model.endtime"   id="d5222"   onfocus="WdatePicker()" cssClass = "{dateISO:true,required:false}" cssStyle="width:100px;"></s:textfield>
						</td>   
					</tr> 
					<tr> 
						<td valign="top" align=right  class="form_title">
							事　　由：
							</td><td  class="form_data">
							<s:textarea name="model.memo"  cssClass = "{required:false}" cssStyle="width:250px;height:100px;"></s:textarea>
						</td>
					</tr>
				</table>
			
		 	<s:hidden name="model.spaceid"></s:hidden>
		 	<s:hidden name="model.spacename"></s:hidden>
		 	<s:hidden name="model.userid"></s:hidden>
		 	<s:hidden name="model.username"></s:hidden>
		 	<s:hidden name="model.resouceid"></s:hidden>
		 	<s:hidden name="model.resouce"></s:hidden>
		 	<s:hidden name="model.id"></s:hidden>
		 	<s:hidden name="model.status"></s:hidden>
		 	</s:form>
	</div>
      <div region="south" border="false" style="text-align: right; height: 40px; line-height: 30px;padding-top:5px;padding-right:15px;border-top:1px solid #efefef">
           <a class="easyui-linkbutton" href="javascript:save();" iconCls="icon-save">保存</a>
           <a class="easyui-linkbutton" href="javascript:closeDialog();" iconCls="icon-cancel">取消</a>
      </div>
      
  </body>
</html>
		