<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysengineiformmap.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	 <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	 <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	 <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var mainFormValidator;
	var api = art.dialog.open.api, W = api.opener;
	$().ready(function() {
			 var setting = {
				async: {
						enable: true, 
						url:"sysEngineIForm!openjson.action?showtype=2",
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
			$.fn.zTree.init($("#formTree"), setting);
			mainFormValidator =  $("#eidtSubForm").validate({
				debug:true
			 });
			 validator.resetForm();
		});
		//执行提交动作
		function doSubmit(){
			var valid = mainFormValidator.form(); //执行校验操作
				if(!valid){
					return false;
				}
			   var options = {
					error:errorFunc,
					success:successFunc 
			   };
			   
			   $('#eidtSubForm').ajaxSubmit(options);
		}
	      errorFunc=function(){
	           alert("保存失败！");
	      }
	      successFunc=function(responseText, statusText, xhr, $form){
	           if(responseText!="0"){
	                alert("保存成功！");
	                close();
	           }
	           else if(responseText=="error"){
	              alert("保存失败！");
	           } 
	      }
	      
		function onClick(e, treeId, treeNode) {
			var formTree = $.fn.zTree.getZTreeObj("formTree");
			if(treeNode.isParent||treeNode.type=='group'){
		 		formTree.expandNode(treeNode, true, null, null, true); 
			}else{ 
				var nodes = formTree.getSelectedNodes();
				v = "";
				var id = "";
				nodes.sort(function compare(a,b){return a.id-b.id;});
				for (var i=0, l=nodes.length; i<l; i++) {
					v += nodes[i].name + ",";
					id+= nodes[i].id + ",";
				}
				if (v.length > 0 ) v = v.substring(0, v.length-1);
				if (id.length > 0 ) id = id.substring(0, id.length-1);
				var subformname = $("#subformname");
				subformname.attr("value", v); 
				$("#eidtSubForm_subForm_title").val(v); 
				$("#eidtSubForm_subForm_subformid").val(id);
				$("#eidtSubForm_subForm_key").val("");  
				hideMenu();
			}
		}
		//================================================
		function showMenu() {
				var cityObj = $("#subformname"); 
				var cityOffset = $("#subformname").offset();
				$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
				$("body").bind("mousedown", onBodyDown);
				return false;
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
		//自动生成表单键值
		function loadFormKey(){
			var subForm_title = $("#eidtSubForm_subForm_title").val();
			var formid = $("#subForm_iformId").val();
			var pageurl = 'sysEngineSubform_loadformkey.action';
			if(subForm_title!=''){ 
				$.post(pageurl,{formid:formid,title:subForm_title},function(msg){ 
			               if(msg!=''){
			               		$("#subtablekey").val(msg); 
			               }else{
			               		alert("提取失败");
			               }
			            }
			      );
			}else{
				alert("请填写子表单标题");
			}
			return false;
		}
		function close(){
			api.close(); 
		}
		
		function isResizeChange(){
		
			var val=$('input:radio[name="subForm.isResize"]:checked').val();
			
			if(val==1){
				$("#tr_height").show();
				$("#subForm_height").rules("add",{maxlength:32,required:true});
			}else{
				$("#tr_height").hide();
				$("#subForm_height").rules("remove","required");
			}
		}
		function isAutoWidth(){
			 var val=$('input:radio[name="subForm.autowidth"]:checked').val();
			 
			if(val==1){
				$("#tr_gridWidth").show();
				$("#subForm_gridwidth").rules("add",{maxlength:32,required:true});
			}else{
				$("#tr_gridWidth").hide();
				$("#subForm_gridwidth").rules("remove","required");
			}
		}
		
		function showInfo(){
			var val= obj.value;
			alert(val);
		}
	</script>
	<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
		.td_memo{
			text-align:left; 
			padding:12px;
			font-family:"宋体";
			border:1px solid #666;
			color:#666;
			font-size:12px;
		} 
		.td_memo_title{
			color:red;
		}
		
	</style>
</head>
<body class="easyui-layout">
						 <div region="center" border="false" style="padding: 10px;border-bottom:1px solid #ccc;">
				            	 <s:form name="eidtSubForm" id="eidtSubForm"  action="sysEngineSubform_save.action"  theme="simple">
						            <table width="100%" border="0" cellspacing="4" cellpadding="5"> 
									  <tr>
									    <td class="form_title"><span style="color:red;">*</span>关联子表:</td>
									    <td class="form_data">
									    	<input  id="subformname"  name="subformname" value="<s:property value='title' escapeHtml='false'/>" cssClass="{maxlength:32,required:true}" disabled style="readonly;background-color:#efefef;color:#666"/>&nbsp;<a id="menuBtn" href="#" onclick="showMenu(); ">选择</a>
									    	<s:hidden name="subForm.subformid" ></s:hidden>
									    </td>
									  </tr>
									  <tr>
									    <td class="form_title"><span style="color:red;">*</span>子表标题:</td>
									    <td class="form_data">
									    	<s:textfield name="subForm.title"  cssClass="{maxlength:1024,required:true}" ></s:textfield>
									  	</td>
									  </tr>
									  <tr>
									    <td class="form_title"><span style="color:red;">*</span>键值:</td>
									    <td class="form_data">
									    		<s:textfield name="subForm.subtablekey" id="subtablekey" cssClass="{maxlength:32,required:true}" ></s:textfield>&nbsp;<a id="menuBtn" href="#" onclick="loadFormKey();">自动生成</a><br/>
									    </td>
									  </tr>
									  
									  <tr>
									    <td class="form_title"><span style="color:red;">*</span>子表类型:</td>
									    <td class="form_data">
									    		<s:radio  name="subForm.type" onclick="showInfo(this)" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'0':'编辑行项目','1':'视窗行项目','2':'模版表单','3':'树形行项目'}" value="subForm.type" theme="simple"/>
									    </td> 
									  </tr>
									   <tr id="jsonInterFace" style="display:none">
									    <td class="form_title"><span style="color:red;">*</span>JSON扩展接口:</td>
									    <td class="form_data">
									    	<s:textfield cssStyle="width:300px" name="subForm.interfaceClass"></s:textfield>
									    </td> 
									  </tr>
									  <tr>
									    <td  class="form_title"><span style="color:red;">*</span>是否固定行项目高度:</td>
									    <td  class="form_data">
									    	<s:radio  name="subForm.isResize" cssStyle="border:0px;" onclick="isResizeChange()"  cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" value="subForm.isResize" theme="simple"/>
									    </td> 
									  </tr>
									  <tr style="display:none" id="tr_height">
									    <td  class="form_title"><span style="color:red;">*</span>高度:</td>
									    	<td class="form_data">
									    		<s:textfield name="subForm.height" cssStyle="width:60px;" id="subForm_height"></s:textfield>
									    	</td>
									  </tr>
									  <tr>
									    <td  class="form_title"><span style="color:red;">*</span>是否固定行项目宽度:</td>
									    	<td  class="form_data">
												<s:radio  name="subForm.autowidth" cssStyle="border:0px;" onclick="isAutoWidth()" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" value="subForm.autowidth" theme="simple"/>
								    		</td>
									  </tr>
									  <s:if test="subForm.autowidth==1">
									  	<tr id="tr_gridWidth">
									    <td  class="form_title"><span style="color:red;">*</span>宽度:</td>
									    <td class="form_data"><s:textfield name="subForm.gridwidth" cssStyle="width:60px;"  id="subForm_gridwidth"></s:textfield></td>
									  </tr>
									  </s:if>
									  <s:else>
									  <tr style="display:none" id="tr_gridWidth">
									    <td  class="form_title"><span style="color:red;">*</span>宽度:</td>
									    <td class="form_data"><s:textfield name="subForm.gridwidth" cssStyle="width:60px;"  id="subForm_gridwidth"></s:textfield></td>
									  </tr>
									  </s:else>
									   <tr id="tr_gridDictionary"> 
										    <td  class="form_title">默认排序字段:</td>
										    <td class="form_data" >
										    <s:textfield name="subForm.orderColumn" cssStyle="width:60px;"  id="orderColumn"/>
										    <s:radio list="#{'ASC':'升序','DESC':'降序'}" name="subForm.orderType" theme="simple"/>
										    </td>
										  </tr> 
										  
									  <tr>
									  	<td colspan="2">
									  	<fieldset  class="td_memo">
										<legend class="td_memo_title">子表操作按钮设置</legend>
								 		<table width="100%"  cellspacing="4" cellpadding="5"> 
								 			<tr id="tr_excel"> 
										    <td  class="form_title">EXCEL导入按钮:</td>
										    <td class="form_data"><s:radio list="#{'1':'是','0':'否'}" name="subForm.excelImp" theme="simple"/></td>
										  
										    <td  class="form_title">EXCEL导出按钮:</td>
										    <td class="form_data"><s:radio list="#{'1':'是','0':'否'}" name="subForm.excelExp" theme="simple"/></td>
										  </tr>  
										  <tr id="tr_copy"> 
										    <td  class="form_title">是否允许行复制:</td>
										    <td class="form_data"><s:radio list="#{'1':'是','0':'否'}" name="subForm.isCopy" theme="simple"/></td>
										  
										    <td  class="form_title">是否显示新增按钮:</td>
										    <td class="form_data"><s:radio list="#{'1':'是','0':'否'}" name="subForm.isAdd" theme="simple"/></td>
										  </tr>   
										  <tr id="tr_del"> 
										    <td  class="form_title">是否显示删除按钮:</td>
										    <td class="form_data"><s:radio list="#{'1':'是','0':'否'}" name="subForm.isDel" theme="simple"/></td>
										  
										    <td  class="form_title">是否显示保存按钮:</td>
										    <td class="form_data"><s:radio list="#{'1':'是','0':'否'}" name="subForm.isSave" theme="simple"/></td>
										  </tr>   
										 
										  <tr id="tr_gridDictionary"> 
										    <td  class="form_title">数据字典:</td>
										    <td class="form_data" colspan="3"><s:select name="subForm.dictionaryId"  listKey="id" listValue="dicName" headerKey="0"  headerValue="---请选择数据字典---"   list="%{dictionaryList}" theme="simple"></s:select></td>
										  </tr> 
								 		</table>
										</fieldset>
									  	 
									  	</td>
									  </tr>
									  
									  <tr style="display:none"> 
									    <td  class="form_title"><span style="color:red;">*</span>锁定列:</td>
									    <td class="form_data">
									    	<s:select  name="subForm.fixedrow" list="#{1:'一列',2:'二列',3:'三列',4:'四列'}" cssStyle="width:80px"></s:select>
									    </td>
									  </tr>
									</table>	
						                <input  type="hidden" id = "subForm_iformId" name="subForm.iformId" value="<s:property value='formid' escapeHtml='false'/>">
						                <s:hidden name="subForm.id"></s:hidden>
						               </s:form> 
    					</div>
    					 <div region="south" border="false" style="text-align: right; height: 40px; line-height: 30px;padding-top:5px;">
			                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
			                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:close()">取消</a>
			            </div>
			            <div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #666; height:280px;width:250px;overflow:auto;position: absolute;"> 
								<ul id="formTree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
</body>
</html>
<script>
	isResizeChange();
	isAutoWidth();
</script>