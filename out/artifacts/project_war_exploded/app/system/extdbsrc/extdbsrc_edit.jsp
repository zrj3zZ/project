<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>新增外部数据源</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
    <link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
    //全局变量
    var api = art.dialog.open.api, W = api.opener;
      //==========================装载快捷键===============================//快捷键
		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
			         save(); return false;
		     }
       }); //快捷键
     
      //保存
    function save(){
          var bool= validate();
          if(bool){
             var options = {
				error:errorFunc,
				success:successFunc 
			   };

			 $('#editForm').ajaxSubmit(options);
        	}
          else{
          		return ;
        	}
      }
      errorFunc=function(){
           art.dialog.tips("保存失败！",2);
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              art.dialog.tips("保存成功！",2);
              setTimeout('close();',1000);
           }
      }
      //表单验证
      function validate(){
          var dsrcTitle=$.trim($('#editForm_model_dsrcTitle').val());//不能为空，不能超过64
          var dsrcType=$.trim($('#editForm_model_dsrcType').val());//不能不选         
          var dsrcUrl=$.trim($('#editForm_model_dsrcUrl').val());//不能为空，不能超过256,格式与带出来的格式相同
          var username=$.trim($('#editForm_model_username').val());//不能为空，不能超过64，ajax验证？
          var password=$.trim($('#editForm_model_password').val());//不能为空，不能超过64，ajax验证？
          $('#editForm_model_dsrcTitle').val(dsrcTitle);
          $('#editForm_model_dsrcUrl').val(dsrcUrl);
          $('#editForm_model_username').val(username);
          $('#editForm_model_password').val(password);
          
          if(dsrcTitle==""){
              art.dialog.tips("数据源标题不能为空！",2);
               $('#editForm_model_dsrcTitle').focus();
              return false;
          }
          if(dsrcType=="请选择"){
              art.dialog.tips("请选择数据源模板！",2);
              $('#editForm_model_dsrcType').focus();
              return false;             
          }
          if(dsrcUrl==""){
              art.dialog.tips("请填写链接地址！",2);
              $('#editForm_model_dsrcUrl').focus();
              return false;              
          }
          if(username==""){
              art.dialog.tips("请输入用户名！",2);
              $('#editForm_model_username').focus();
              return false;              
          }
          if(password==""){
              art.dialog.tips("请输入密码！",2);
              $('#editForm_model_password').focus();
              return false;              
          }
           if(length2(dsrcTitle)>64){
                 art.dialog.tips('数据源标题过长!',2);
                 $('#editForm_model_dsrcTitle').focus();
                 return false;
          }
          if(length2(dsrcUrl)>256){
                 art.dialog.tips('链接地址过长!',2);
                 $('#editForm_model_dsrcUrl').focus();
                 return false;
          }
          if(length2(username)>64){
                 art.dialog.tips('用户名过长!',2);
                 $('#editForm_model_username').focus();
                 return false;
          }
          if(length2(password)>64){
                 art.dialog.tips('密码过长!',2);
                 $('#editForm_model_password').focus();
                 return false;
          }
          //这里差dsrcUrl格式的验证          
          return true;
      }
      //关闭窗口
      function close(){
          api.close();
      } 
      //链接测试
      function testCon(){
       if(validate()){
          $.post('sys_extdbsrc_testCon.action',$('#editForm').serialize(),function(data){
                if(data=="0"){
                   art.dialog.tips("链接成功！",2);
                }
                else if(data=="1"){
                   art.dialog.tips("加载驱动失败,驱动名称有误！",2);
                }
                else if(data=="2"){
                   art.dialog.tips("注册驱动失败，lib目录缺少相应驱动包！",2);
                }
                else if(data=="3"){
                   art.dialog.tips("链接失败，用户名或密码有误！",2);
                }
                else if(data=="4"){
                   art.dialog.tips("链接失败，链接地址有误或服务器未开启！",2);
                }
          });
         }
        else{
           return ;
        }   
      }
      //根据数据源模板，自动选择驱动名
      function changeDriName(){
           var dsrcType=$('#editForm_model_dsrcType').val();
           if(dsrcType=="请选择"){
               $('#editForm_model_driverName').val(''); 
               $('#editForm_model_dsrcUrl').val('');
               $('#editForm_model_driverName').focus();              
           } 
           if(dsrcType=="Oracle (Thin driver)"){
               $('#editForm_model_driverName').val('oracle.jdbc.driver.OracleDriver'); 
               $('#editForm_model_dsrcUrl').val("jdbc:oracle:thin:@<server>[:<1521>]:<database_name>");
               $('#editForm_model_dsrcUrl').focus();
           }
           else if(dsrcType=="Microsoft SQL Server2000"){
               $('#editForm_model_driverName').val('com.microsoft.jdbc.sqlserver.SQLServerDriver'); 
               $('#editForm_model_dsrcUrl').val('jdbc:microsoft:sqlserver://<server_name>:<1433>;[databaseName=<dbname>]');
               $('#editForm_model_dsrcUrl').focus();
           }
           else if(dsrcType=="Microsoft SQL Server2005/2008"){
               $('#editForm_model_driverName').val('com.microsoft.sqlserver.jdbc.SQLServerDriver'); 
               $('#editForm_model_dsrcUrl').val('jdbc:sqlserver://<server_name>:<1433>;[databaseName=<dbname>]');
               $('#editForm_model_dsrcUrl').focus();
           }
           else if(dsrcType=="MySQL Connector/J"){
               $('#editForm_model_driverName').val('com.mysql.jdbc.Driver'); 
               $('#editForm_model_dsrcUrl').val('jdbc:mysql://<hostname>[<:3306>]/<dbname>');
               $('#editForm_model_dsrcUrl').focus();
           }
                      
      }
    </script>
    <style type="text/css">
      .td_title{width:23%;}
    </style>
  </head>
  
  <body class="easyui-layout">
      <div regin="center" border="false" style="padding:3px;margin-bottom:5px;overflow-y:auto;">
           <s:form name="editForm" id="editForm" action="sys_extdbsrc_save.action" theme="simple">
               <table width="95%" cellpadding="5">
                   <tr>
                      <td class="td_title">数据源标题：</td><td class="td_data"><s:textfield cssStyle="width:150px;" name="model.dsrcTitle"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">数据源模板：</td><td class="td_data"><s:select onchange="changeDriName();" cssStyle="width:300px;" label="数据源模板：" name="model.dsrcType" list="{'请选择','Oracle (Thin driver)','Microsoft SQL Server2000','Microsoft SQL Server2005/2008','MySQL Connector/J'}"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">驱动名称：</td><td class="td_data"><s:textfield readonly="true" cssStyle="width:300px;background-color:#efefef;" name="model.driverName"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">链接地址：</td><td class="td_data"><s:textfield cssStyle="width:380px;" name="model.dsrcUrl"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">用户名：</td><td class="td_data"><s:textfield cssStyle="width:150px;" name="model.username"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">密码：</td><td class="td_data"><s:password cssStyle="width:150px;" name="model.password"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
               </table>
               
               <s:hidden name="model.id"/>
           </s:form>
      </div>
      <div regin="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-right:20px;margin-top:45px;">
           <a class="easyui-linkbutton" href="javascript:testCon();" iconCls="icon-search">链接测试</a>
           <a class="easyui-linkbutton" href="javascript:save();" iconCls="icon-save">保存</a>
      </div>
  </body>
</html>
