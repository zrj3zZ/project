<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>权限</title>
    
     <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	
	<script type="text/javascript">

	var setting = {
	                data:{
	                    key:{
	                        name:"name"
	                    }
	                },
					view: {
						selectedMulti: false,
						dblClickExpand: false
					},
					async: {
						enable: true,
						url:"sysEngineIForm!openjson.action",
						dataType:"json"
					},
					callback: {
					    beforeClick: beforeClick,
						onClick: onClick,
						onAsyncSuccess: onAsyncSuccess
					}
				};
	function beforeClick(treeId, treeNode){  
	  				if(treeNode.type=='group'){
	  				    lhgdialog.tips("选择组无效！",1);
	  				    return false;
	  				} 
						return true;
	         	 }			
	function onClick(event, treeId, treeNode){
	                    if(treeNode.type=='iform'){
	               			 var text=treeNode.name;
	               		     var formObj = $("#formSel");
			                 formObj.html(text); 
			                 var id=treeNode.id;
			                 var hidObj = $('#editForm_baseModel_dsId');
			                 hidObj.attr("value", id);
	               		}
	             }
	function onAsyncSuccess(event, treeId, treeNode, msg){ 
	                    var tree = $.fn.zTree.getZTreeObj('formTree');
	                    tree.expandAll(true);	                    
	                    var groupid = "<s:property value='baseModel.dsId'  escapeHtml='false'/>";
	                    var node=tree.getNodesByParam('id',groupid,null);
	               		for(i=0;i<node.length;i++){
				    		if(node[i].type=='iform'){
				    			  var text=node[i].name;
	               		          var formObj = $("#formSel");
			                      formObj.html(text);
				    		      tree.selectNode(node[i]);
				    		  }
						}
	             }				
  $(document).ready(function(){ 
			$.fn.zTree.init($("#formTree"), setting);
		});		
	//保存
    function save(){
           var oldDsId = $('#editForm_oldDsId').val();
           var dsId = $('#editForm_baseModel_dsId').val();
           var bool= validate();
          if(bool){
              if(oldDsId!=''){
              		if(dsId!=oldDsId){
                  		lhgdialog.confirm('确定清空查询条件设置，显示条件设置，<br/>排序字段设置中相关设置？',function(){
                  			$('#editForm').submit();
                  		});
              		}
              		else{
              		    lhgdialog.tips("未变更表单数据源！",1);
              		}
            	}
             else{
                 $('#editForm').submit();
             }			 
        	}
      }
      //表单验证
      function validate(){
          var dsId = $('#editForm_baseModel_dsId').val();
          if(dsId==""){
               lhgdialog.tips("请选择表单数据源！",1);
              return false;
          }
          return true;
      } 
	</script>
     <style type="text/css">
		.top {
				color:#004080;
				font-size: 12px;
				text-align: left;
				letter-spacing: 0.1em;
				padding-left:10px;
				white-space:nowrap;
				vertical-align:middle;
				font-family:"微软雅黑";		
			}
		.menuContent{	
		    margin-top:10px;		
			margin-left:100px;
			height:300px;
		}
		.bottom{
				color:#004080;
				font-size: 12px;
				text-align: left;
				letter-spacing: 0.1em;
				padding-left:10px;
				white-space:nowrap;
				vertical-align:middle;
				font-family:"微软雅黑";	    
		}
   </style>  
  </head>
  
  <body class="easyui-layout">
     <div region="center" style="padding:3px;border-top:0px;width:100%">					
					<div style="border-bottom:1px solid #efefef;margin-bottom:3px;text-align:left;padding-right:20px;backgroud-color:#efefef">
						<a href="javascript:save();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>						
					</div>
					<div>
						<s:form name="editForm" id="editForm" action="ireport_designer_baseInfo_dbSourse_save.action" theme="simple">
						<div class="top">请选择表单数据源：</div>	
						
						<div id="menuContent" class="menuContent" style="height:400px;overflow:auto;border-bottom:1px solid #efefef;">
	                    	<ul id="formTree" class="ztree" style="margin-top:0; width:160px;"></ul>
                        </div>	
                        
                        <div class="bottom">您当前选择的表单数据源为：<span style="font-size:13px;font-weight:bold;font-style:oblique;" id="formSel"></span></div>                       
						<s:hidden  name="baseModel.dsId"/>
						<s:hidden  name="oldDsId" value='%{baseModel.dsId}'/>
									
						<s:hidden name='baseModel.id'></s:hidden>
						<s:hidden name='baseModel.groupid'></s:hidden>
						<s:hidden name='baseModel.rpName'></s:hidden>
						<s:hidden name='baseModel.rpType'></s:hidden>
						<s:hidden name='baseModel.dsType'></s:hidden>  
						<s:hidden name='baseModel.sqlScript'></s:hidden>
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
	</div>				
  </body>
</html>
