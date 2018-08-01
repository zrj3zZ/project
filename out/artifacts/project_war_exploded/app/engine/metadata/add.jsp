
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
	</style>	
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	var mainFormValidator;
	$().ready(function() {
			 var setting = {
				async: {
						enable: true, 
						url:"sysEngineGroup!openjson.action",
						dataType:"json"
					},
				view: {   
					dblClickExpand: false
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					//beforeClick: beforeClick,
					onClick: onClick
				}
			};
			$.fn.zTree.init($("#treeDemo"), setting);
			mainFormValidator =  $("#editForm").validate({
				debug:true
			 });
			 mainFormValidator.resetForm();
		});
		
 //==========================装载快捷键===============================//快捷键
 		jQuery(document).bind('keydown',function (evt){		
		   		 if(evt.ctrlKey&&evt.shiftKey){
				return false;
		   }else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			     this.location.reload(); return false;
		   }else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增存储
				 addMetaData(); return false;
		   }else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
				 doSubmit(); return false;
		   }		
		}); //快捷键
		
		function doSubmit(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
					return false;
				}
			var id = $("#editForm_model_id").val();
			if(id!=""){
				saveMetadata(); 
			}else{ 
				var tablename = $("#editForm_model_entityname").val();
				$.ajax({
			            type:'POST',
			            url:'sysEngineMetadata!ishaving.action?entityname='+encodeURI(tablename),
			            success:function(msg){
			            	if(msg=="HAVING"){
			            		art.dialog.tips("您要创建的存储表在系统中已经存在，建议通过“自动生成存储名”创建名称"); 
			               		return;
			            	}else{
			               		saveMetadata();
			               }
			            }
			        });
			}
			
		}
		//执行保存
		//表单提交
		function saveMetadata(){ 
            var options = {
				error:errorFunc,
				success:successFunc 
			   };
			$('#editForm').ajaxSubmit(options);
       }
      errorFunc=function(){
           art.dialog.tips("保存失败！");
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText!="0"){
               art.dialog.tips("保存成功!");
               cancel();
           }
           else if(responseText=="error"){
              art.dialog.tips("保存失败！");
           } 
      }
		//加载
		function loadTableName(){
			var groupid = $("#editForm_groupid").val();
			var entitytitle = $("#editForm_model_entitytitle").val();
			var pageurl = 'sysEngineMetadata_tablename_create.action';
			if(entitytitle!=''){
			 $.post(pageurl,{groupid:groupid,entitytitle:entitytitle},function(msg){
       				if(msg!=''){
			               		$("#editForm_model_entityname").val(msg); 
			               }else{
			               		art.dialog.tips("提取失败");
			               }
   				});
			/* 
				$.ajax({  
			            type:'POST',
			            url:pageurl,
			            success:function(msg){ 
			               if(msg!=''){
			               		$("#editForm_model_entityname").val(msg); 
			               }else{
			               		alert("提取失败");
			               }
			            }
			        }); */
			}else{
				art.dialog.tips("请填写存储标题");
			}
		}
		//关闭窗口
		function cancel(){
			api.close();
		}
		function beforeClick(treeId, treeNode) {
			var check = (treeNode && !treeNode.isParent);
			if (!check) art.dialog.tips("请选择目录...");
			return check;
		}
		
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getSelectedNodes(),
			v = "";
			var id = "";
			nodes.sort(function compare(a,b){return a.id-b.id;});
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].name + ",";
				id+= nodes[i].id + ",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			if (id.length > 0 ) id = id.substring(0, id.length-1);
			var cityObj = $("#citySel");
			cityObj.attr("value", v);
			$("#groupid").val(id); 
			hideMenu();
		}
		function showMenu() {
			var cityObj = $("#citySel"); 
			var cityOffset = $("#citySel").offset();
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
	</script>
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px;">
            <s:form name="editForm" id="editForm" action="sysEngineMetadata!save.action"  theme="simple">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0">
            		<tr style="display:none;" >
            			<td class="form_title"><span style="color:red;">*</span>模型类型:</td>
            			<td  class="form_data">  
            				<s:if test="model.entityname==null">
            					<s:radio  name="model.entitytype" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'0':'存储模型'}" value="model.entitytype" theme="simple"/>
            				</s:if> 
            				<s:else>
            					<s:if test="model.entitytype==0">
            					存储模型
            					</s:if>
            					<s:else> 
	            					视图模型
            					</s:else>	
            				</s:else>
            				
            			</td>
            		</tr>
            		<tr> 
            			<td class="form_title"><span style="color:red;">*</span>存储名称:</td>
            			<td class="form_data"><s:textfield name="model.entitytitle" cssClass="{maxlength:32,required:true}" theme="simple"/></td>
            		</tr> 
            		
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>存 储 表:</td>
            			<td class="form_data">
            				<s:if test="model.entityname==null">
            					<s:textfield name="model.entityname" theme="simple" cssClass="{maxlength:32,required:true}" /><a href="javascript:loadTableName();" style="font-family:宋体;font-size:12px;padding-left:5px;">生成存储表名</a>
            				</s:if>
            				<s:else>
            					<s:hidden name="model.entityname" theme="simple"/><s:property value="model.entityname"/>
            				</s:else>
            			</td>
            		</tr>
            		<tr>  
            			<td class="form_title"><span style="color:red;">*</span>目录分组:</td>
            			<td class="form_data">
								<input id="citySel" type="text"  disabled value="<s:property value="groupname"/>" style="width:120px;background-color:#efefef"/> 
									&nbsp;<a id="menuBtn" href="#" onclick="showMenu();">选择</a>
										<s:hidden  id="groupid"  name="model.groupid" theme="simple"/> 
						</td>
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>管 理 员:</td>
            			<td class="form_data"><s:textfield name="model.master"  cssClass="{maxlength:32,required:true}"  theme="simple"/></td>
            		</tr>
            		<tr>
            			<td class="form_title">说　　明:</td>
            			<td class="form_data"><s:textarea name="model.descirption" cols="45" rows="6"></s:textarea></td>
            		</tr>
            		
            	</table>
                <s:hidden name="model.id"></s:hidden>
                <s:hidden name="groupid"/> 
               </s:form>  
            </div> 
            <div region="south" style="border:0px;padding:10px;height:50px;">
				<fieldset  class="td_memo" style="padding:5px;color:#666">
								<legend class="td_memo_title">使用说明</legend>
								【存储模型】 用于日常数据管理维护及流程单据<br>
								【视图模型】 用于数据报表查询，数据分院<br>
								【其他说明】 数据存储自动映射系统数据表单<br>
　　　							  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据存储统一以“BD_”做为存储表开头<br>　
							</fieldset>
			</div>
           <div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef; position: absolute;height:140px;width:190px;overflow:auto;"> 
								<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul> 
							</div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;padding-top:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">取消</a>
            </div>
</body>
</html>
