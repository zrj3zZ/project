<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>编辑</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
     <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>   
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>  
    <script type="text/javascript">
      //全局变量
     var api = frameElement.api, W = api.opener; 
     	var mainFormValidator;
	$().ready(function() {
			 var setting = {
				async:{
						enable: true, 
						url:"sysEngineIFormMap_loadDisplayTypeTree.action",
						dataType:"json"
					},
				data: {
					simpleData: {
						enable: true
					} 
				}, 
				callback: {
					onClick: onClick
				} 
			};
			$.fn.zTree.init($("#displayTree"), setting);
				mainFormValidator =  $("#editForm").validate({
				debug:false 
			 });  
			 mainFormValidator.resetForm();
		});
		function onClick(e, treeId, treeNode) {
			var displayTree = $.fn.zTree.getZTreeObj("displayTree");
			var nodes = displayTree.getSelectedNodes();
			if(treeNode.isParent){
		 		displayTree.expandNode(treeNode, true, null, null, true);
			}else{
				v = "";
				var id = "";
				nodes.sort(function compare(a,b){return a.id-b.id;});
				for (var i=0, l=nodes.length; i<l; i++) {
					v += nodes[i].name + ",";
					id+= nodes[i].id + ",";
				}
				if (v.length > 0 ) v = v.substring(0, v.length-1);
				if (id.length > 0 ) id = id.substring(0, id.length-1);
				var cityObj = $("#displayTypeName");
				cityObj.attr("value", v);
				$("#displayType").val(id); 
				hideMenu();
			}
		}
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
           W.lhgdialog.tips("保存失败！",2);
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              W.lhgdialog.tips("保存成功！",2);
              setTimeout('cancel();',1000);
           }
      }
            //表单验证
      function validate(){
            var fieldName=$.trim($('#editForm_model_fieldName').val());//不能为空，不能超过32
          var fieldTitle=$.trim($('#editForm_model_fieldTitle').val());//不能为空，不能超过100
                  var fieldDisplay=$.trim($('#displayType').val());//不能为空           
                   var fieldParams=$.trim($('#editForm_model_fieldParams').val());//不能超过500
            var extendsParams=$.trim($('#editForm_model_extendsParams').val());//不能超过500
               var fieldDef=$.trim($('#editForm_model_fieldDef').val());//不能超过100
          $('#editForm_model_fieldName').val(fieldName);
          $('#editForm_model_fieldTitle').val(fieldTitle);

          $('#editForm_model_fieldParams').val(fieldParams);
          $('#editForm_model_extendsParams').val(extendsParams);
          $('#editForm_model_fieldDef').val(fieldDef);
          
          if(fieldName==""){
              W.lhgdialog.tips("字段ID不能为空！",2);
               $('#editForm_model_fieldName').focus();
              return false;
          }
          if(fieldTitle==""){
              W.lhgdialog.tips("字段名称不能为空！",2);
              $('#editForm_model_fieldTitle').focus();
              return false;             
          }
           if(fieldDisplay==""){
              W.lhgdialog.tips("请选择外观！",2);
              $('#editForm_model_fieldDisplay').focus();
              return false;             
          }
           if(length2(fieldName)>32){
                 W.lhgdialog.tips('字段ID过长!',2);
                 $('#editForm_model_fieldName').focus();
                 return false;
          }
          if(length2(fieldTitle)>100){
                 W.lhgdialog.tips('字段名称过长!',2);
                 $('#editForm_model_fieldTitle').focus();
                 return false;
          }
          if(length2(fieldParams)>500){
                 W.lhgdialog.tips('参考值过长!',2);
                 $('#editForm_model_fieldParams').focus();
                 return false;
          }
          if(length2(extendsParams)>500){
                 W.lhgdialog.tips('扩展参数过长!',2);
                 $('#editForm_model_extendsParams').focus();
                 return false;
          } 
          if(length2(fieldDef)>100){
                 W.lhgdialog.tips('默认值过长!',2);
                 $('#editForm_model_fieldDef').focus();
                 return false;
          }         
          return true;
      }
      //================================================
		function showMenu() {
				var cityObj = $("#displayTypeName"); 
				var cityOffset = $("#displayTypeName").offset();
				$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
				$("body").bind("mousedown", onBodyDown);
		}
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		} 
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}
		
      //关闭窗口
      function cancel(){
          api.close();
      }
      </script>
       <style type="text/css">
		.td_title {
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
      <div region="center" border="false" style="padding:3px;margin-bottom:5px;overflow-y:auto;">
           <s:form name="editForm" id="editForm" action="ireport_designer_condition_save.action" theme="simple">
               <table width="100%" border="0" cellspacing="0" cellpadding="0">
                   <tr>
                      <td class="td_title">字段ID：</td><td class="td_data"><s:textfield readonly="true" cssStyle="background-color:#cdcdcd;width:200px;" name="model.fieldName"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">字段名称：</td><td class="td_data"><s:textfield cssStyle="width:200px;" name="model.fieldTitle"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">外观：</td><td class="td_data">
                       <input type="text" name="displayTypeName" readonly="readonly" id="displayTypeName" value="<s:property value="displayTypeName"/>"><s:hidden id="displayType" name="model.fieldDisplay"></s:hidden> <a href="javascript:showMenu()">选择</a>
					  &nbsp;<span style='color:red'>*</span>
					  </td>
                   </tr>
                    <tr>
                      <td class="td_title">比较：</td><td class="td_data">
                      <s:select label="比较：" name="model.comparison"  list="%{comparisonTypeList}" listKey="typeKey" listValue="typeValue">
                      </s:select>&nbsp;<span style='color:red'>*</span>
					  </td>
                   </tr>
                   <tr> 
                      <td class="td_title">参考值：</td><td class="td_data"><s:textfield cssStyle="width:300px;" name="model.fieldParams"/></td>
                   </tr>
                   <tr>
                      <td class="td_title">扩展参数：</td><td class="td_data"><s:textfield cssStyle="width:300px;" name="model.extendsParams"/></td>
                   </tr>
                   <tr>
                      <td class="td_title">默认值：</td><td class="td_data"><s:textfield  cssStyle="width:300px;" name="model.fieldDef"/></td>
                   </tr>
               </table>
               <div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef; position: absolute;height:180px;width:250px;overflow:auto;"> 
								<ul id="displayTree" class="ztree" style="margin-top:0; width:160px;height:100px;"></ul> 
				</div>
              
               <s:hidden name="model.id"/>
               <s:hidden name="model.ireportId"/>
               <s:hidden name="model.orderIndex"/>
           </s:form>
      </div>
      
      <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
           <a class="easyui-linkbutton" href="javascript:save();" iconCls="icon-save">保存</a>
           <a class="easyui-linkbutton" href="javascript:cancel();" iconCls="icon-cancel">取消</a>
      </div>
      
  </body>
</html>
