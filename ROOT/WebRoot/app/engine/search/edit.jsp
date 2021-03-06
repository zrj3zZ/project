<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>  
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
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
				mainFormValidator =  $("#updateSearchForm").validate({
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
		
		function doSubmit(){ 
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){ 
					return;
			}else{
				  var options = {
						error:function(){
				           alert("保存失败！");
				        },
						success:function(responseText, statusText, xhr, $form){
				           if(responseText=="success"){
				               alert("保存成功！",2);
				               api.close();
				           }
				           else if(responseText=="error"){
				              alert("保存失败！");
				           }else{
				          	 alert(responseText);
				           } 
				        }
			   	  };
				  $('#updateSearchForm').ajaxSubmit(options); 
			}
		}
		
		function close(){
			try{
				api.close();
			}catch(e){
				parent.close(); 
			}
		} 
		
	</script>
	<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
			white-space:nowrap;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
	</style>	  
</head>
<body class="easyui-layout"> 
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <s:form name="updateSearchForm" id="updateSearchForm"  action="iformSearchMap_update.action"  theme="simple">
            <table width="100%" border="0" cellspacing="4" cellpadding="5">
             <tr>
			    <td class="form_title"><font style="color:red">*</font>	条件名称:</td>
			    <td class="form_data">
			    	<s:textfield name="iformSearchMap.fieldTitle"  cssClass="{required:true}"  theme="simple"></s:textfield> 
		    	</td>
			  </tr>
			  <tr>
			    <td class="form_title">关系:</td>
			    <td class="form_data">
			    	<s:radio name="iformSearchMap.joinType" list="#{'AND':'并且','OR':'或者'}"></s:radio>
			    </td>
			  </tr>
			  <tr>
			    <td class="form_title"><font style="color:red">*</font>	条件字段:</td>
			    <td class="form_data">
			    	<s:property value="iformSearchMap.fieldName"/><s:hidden name="iformSearchMap.fieldName" cssClass="{required:true}"  theme="simple"></s:hidden>
			  </tr>
			  <tr>
			    <td class="form_title">比较:</td>
			    <td class="form_data"> 
			    	<s:radio name="iformSearchMap.compareType" cssStyle="line-height:25px" list="#{'等于':'等于','大于':'大于','小于':'小于','大于等于':'大于等于','小于等于':'小于等于','like':'包含于'}"></s:radio>
			    </td> 
			  </tr>
			   <tr>
			    <td class="form_title"><font style="color:red">*</font>	外观样式:</td>
			    <td class="form_data">
			    <input type="text" name="displayTypeName" readonly="readonly" id="displayTypeName" value="<s:property value="displayTypeName"/>"><s:hidden id="displayType" name="iformSearchMap.displayType"></s:hidden> <a href="javascript:showMenu()">选择</a>
			  </tr>
			  <tr> 
			    <td class="form_title">录入高度/宽度:</td>
			    <td class="form_data">
			    	<s:textfield name="iformSearchMap.inputHeight" cssStyle="width:60px;" cssClass="{required:true}"  theme="simple"></s:textfield>
			    	<s:textfield name="iformSearchMap.inputWidth" cssStyle="width:60px;" cssClass="{required:true}"  theme="simple"></s:textfield>
			    </td>
			  </tr>
			   <tr>
			    <td class="form_title">是否允许为空:</td>
			    <td class="form_data">
					<s:radio cssClass="{required:true}"  name="iformSearchMap.isnull" list="#{'0':'是','1':'否'}"></s:radio>
			    </td>
			  </tr>
			    <tr>
			    <td  class="form_title">参考值:</td>
			    <td class="form_data">
			    	<s:textfield name="iformSearchMap.displayEnum" cssStyle="width:260px;"  theme="simple"></s:textfield>
			    </td>
			  </tr> 
			   <tr>
			    <td class="form_title">默认值:</td>
			    <td class="form_data">
			    	<input style="width:220px;" type="text" name="iformSearchMap.fieldDefault" id ="iformSearchMap_fieldDefault" /><img style='padding-left:5px;padding-top:5px' title="选择RV参数（动态获取运行时变量）" src='iwork_img/page_code.png' border=0>
			    	</td>
			  </tr>
			  
			</table>	
			<div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef; position: absolute;height:280px;width:250px;overflow:auto;"> 
								<ul id="displayTree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
                <s:hidden name="iformSearchMap.id"></s:hidden>
                <s:hidden   name="iformSearchMap.iformId"/>
               </s:form> 
            </div>
            <div region="south" border="false" style="text-align: right; height: 40px; line-height: 30px;padding-top:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:close()">取消</a>
            </div>
</body>
</html>
