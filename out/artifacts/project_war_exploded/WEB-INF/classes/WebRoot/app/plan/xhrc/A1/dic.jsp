<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	
	<script type="text/javascript">
	var api = frameElement.api, W = api.opener;	
	$(document).ready(function(){
		$('#pj_grid').datagrid({
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onDblClickRow:function(rowIndex){
					var row = $('#pj_grid').datagrid('getSelected');
				}
			});
		});
		
		function doSetting(){
			var rows = $('#pj_grid').datagrid('getSelections');
				 		var ids = [];
					 		for(var i=0;i<rows.length;i++){
							ids.push(rows[i].INSTANCEID);
						}
			var instanceid = $("#instanceid").val();
	         var pageUrl="sanbu_plan_xhrc_doSetting.action?instanceid="+instanceid+"&ids="+ids.join(',');
	         $.post(pageUrl,{},function(data){
		         if(data=='success'){
		             closeWin(); 
		         }else{
		         	alert("操作异常");
		         }
		    });
	         
		}
		function closeWin(){
			api.close();
		}
	</script>
</head>
<body class="easyui-layout"> 
<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;">
<div class="tools_nav">
		<a href="javascript:doSetting();" class="easyui-linkbutton" plain="true" iconCls="icon-ok">确定</a>
								<a href="javascript:closeWin();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">取消</a>
	</div>
	</div>
<div region="center" border="false" style="padding:0px;padding-left:10px;overflow:no;scrolling:no;border-bottom:1px;">
			<table id="pj_grid"  style = "margin:auto;height:450px;width:550px;border:1px solid #fff" iconCls="icon-edit" singleSelect="false"	idField="ID" url="sanbu_plan_xhrc_dicJson.action?instanceid=${instanceid}" } >
									<thead> 
										<tr> 
											 <th field="CK" checkbox=true></th>
											<th field="ID" width="40">ID</th>
											<th field="INSTANCEID" width="40" ></th>
											<th field="XMMC" width="150" >项目名称</th>
											<th field="XMBH" width="100" align="left" >项目编号</th>
											<th field="SSLX" width="100" align="left" >所属领域</th>
											<th field="YZZT" width="60" align="center" >研制状态</th>
										</tr>
									</thead>
						</table>

	<s:hidden name="instanceid" id="instanceid"></s:hidden>
	</div>
	
</body>
</html>
