<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title>目录管理维护</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
<link rel="stylesheet" type="text/css" href="iwork_css/km/km_edit.css"> 
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
<script type="text/javascript">
 var api = art.dialog.open.api, W = api.opener;
 var mainFormValidator;
 $().ready(function() {
 	window.alert = function(str) {
 		return ;
 }
 mainFormValidator =  $("#editForm").validate({
  });
  mainFormValidator.resetForm();
 });
	//表单提交
		document.onkeydown=function(event){
            var e = event || window.event || arguments.callee.caller.arguments[0];
             if(e && e.keyCode==13){ // enter 键
                doSubmit();
            }
        }; 
		function doSubmit(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}
			if($('#directoryname').val()==''){
				art.dialog.tips("目录名称不能为空");
				$('#directoryname').foucs();
			}
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
           if(responseText!=""){
           	var directoryid = responseText;
             art.dialog.tips("保存成功！");
             var title = $('#directoryname').val();
             var newNode = {id:directoryid,name:title,isParent:true,directoryid:directoryid,type:'DIR'}; 
             //获得树节点 
             var origin = artDialog.open.origin; 
             origin.sycnAddFolder(newNode);
             	
               var id = $('#editForm_model_id').val();
               var dfParentid = $('#defaultParentid').val();
               var refreshGridId = dfParentid;
      		   if(id==null || id==''){
      		   		art.dialog.data('model_id', id);
      		   		art.dialog.data('dfParentid', dfParentid);
      		   }else{     	
      		    	var nowParentid = $('#editForm_model_parentid').val();
      		   		art.dialog.data('model_id', id);
      		   		art.dialog.data('dfParentid', dfParentid);
      		   		art.dialog.data('nowParentid', nowParentid);
      		   }          
              api.close();
           }
           else if(responseText=="error"){
             art.dialog.tips("保存失败！");
           } 
      }
        
		//执行退出
		function cancel(){
			api.close();
		}	
	  function getAddress(treeNode){
	        var address=treeNode.name;
	        while (treeNode.getParentNode()!=null){
				      address = treeNode.getParentNode().name+">>"+address;
				      treeNode = treeNode.getParentNode();
				  }
		    return address;
	  }		
      function showMenu() {
			var AddrObj = $("#docAddr");
			var AddrOffset = $("#docAddr").offset();
			$("#menuContent").css({left:AddrOffset.left + "px", top:AddrOffset.top + AddrObj.outerHeight() + "px"}).slideDown("fast");
           
			$("body").bind("mousedown", onBodyDown);
		}
	  function onBodyDown(event) {
			if (!(event.target.id == "docAddr" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}
     function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
	
	var expandRootFlag = false;  //展开‘根目录’标志位(这是一个全局变量)	
	$(document).ready(function(){	   
		 	var id = $('#editForm_model_id').val(); 
        	if(id==null || id==''){
      	  		$('#docAddr').css('background-color','#efefef');
      	  		$("#docAddr").removeAttr("onclick");    
      	  		$('#altAddr').css('display','none');
        	}//新增
        	else{ 
	    			var setting = {
							view: {
								dblClickExpand: false,
								selectedMulti:false
							},
							async: {
								enable: true, 
								url:"km_dir_change_address.action",
								dataType:"json",
								autoParam:["directoryid"],
								otherParam: ["id", id]
							}, 
							callback: {
								onClick: onClick,
								onAsyncSuccess: onAsyncSuccess
							}
					};
				  expandRootFlag = true;
	              $.fn.zTree.init($("#treeMenu"), setting);	
        	}//编辑
		});	
    //树事件		
    function onClick(e, treeId, treeNode) {
			var AddrObj = $("#docAddr");
			AddrObj.attr("value", getAddress(treeNode));
			$('#editForm_model_parentid').val(treeNode.id);
			hideMenu();
		}
	//树事件	
	function onAsyncSuccess(event, treeId, treeNode, msg){						 
	                    var zTree = $.fn.zTree.getZTreeObj("treeMenu");                  
	                    if(expandRootFlag){
	                         var node=zTree.getNodeByParam('id',0);        	   		
	   					     zTree.expandNode(node,true);
	                         expandRootFlag = false;
	                    }
	                    var parentid = '<s:property value="model.parentid"/>';  
	                    var node=zTree.getNodeByParam('id',parentid,null);
	                    if(node!=null){
				    	     zTree.selectNode(node);
	                    }	               		
	             }		
</script>  
</head>
<body class="easyui-layout">
<div region="center" border="false" style="padding:3px;margin-bottom:5px;overflow-y:auto;">
<s:form name="editForm" id="editForm" action="km_dir_save.action" theme="simple">

    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
        <td class='td_title'>目录名称</td>
        <td class='td_data'>
        	<s:textfield  theme="simple" cssClass="txtInput {string:true}" id = "directoryname" name="model.directoryname"/><span style="color:red">*</span>
        </td>
        </tr>
         <tr>
                      <td class="td_title">文档路径:</td>
                      <td class="td_data">
                           <s:textfield name="model.address" id="docAddr" readonly="true" onclick="showMenu();" cssStyle="width:200px;" /><a id="altAddr" class="easyui-linkbutton {string:true}" href="javascript:showMenu();" plain="true" iconCls="icon-add">更改路径</a>&nbsp;<span style='color:red'>*</span>
                           <div id="menuContent" class="menuContent" style="display:none; position: absolute;">
								<ul id="treeMenu" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
						   </div>
                      </td>
        </tr>
        <tr>
        <td class='td_title'>模块描述</td>
        <td class='td_data'>
			<s:textarea  theme="simple" cssStyle="width:240px;height:100px;"  id = "memo"  cssClass="txtInput {string:true}" name="model.memo"></s:textarea>
		</td>
        </tr>
    </table>
         				
   <s:hidden   name="model.id"/>
   <s:hidden   name="model.parentid" />
   <s:hidden   name="model.createuser" />
   <s:hidden   name="model.createdate" />
   <s:hidden   name="model.orderIndex" />
   <s:hidden   id='defaultParentid' name="defaultParentid" value='%{model.parentid}'/>
      
</s:form>
</div>
<div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
	<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >保存</a> 
	<a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel();">关闭</a>
</div>
</body>
</html>
<script language="JavaScript">
	jQuery.validator.addMethod("string", function(value, element) {
		var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
		var patrn=/[“”`~!#$%^&*+<>?"{},;'[]（）—。[\]]/im;
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

