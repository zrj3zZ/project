<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<link rel="stylesheet" type="text/css"
	href="/iwork_js/jqueryjs/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/iwork_js/jqueryjs/easyui/themes/icon.css">

<script type="text/javascript"
	src="/iwork_js/jqueryjs/jquery-3.0.7.min.js"></script>
<script type="text/javascript"
	src="/iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>


</head>
<body>
	<h2>批产项目产品目录</h2>
	<div class="demo-info">
		<div class="demo-tip icon-tip"></div>

	</div>
	<div style="margin:10px 0;">
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="add()">增加</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			onclick="edit()">修改</a> <a href="javascript:void(0)"
			class="easyui-linkbutton" onclick="save()">保存</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			onclick="cancel()">取消</a> <a href="javascript:void(0)"
			class="easyui-linkbutton" onclick="deleted()">删除</a>

	</div>
	<table id="tg" class="easyui-treegrid" title="批产项目产品目录"
		style="width:700px;height:250px"
		data-options="
				iconCls: 'icon-ok',
				rownumbers: true,
				animate: true,
				collapsible: true,
				fitColumns: true,
			url: '/pichan/typesJson.action',
			//	url: '/iwork_js/plan/pichan/treegrid_data2.json',
				idField: 'id',
				treeField: 'name',
				showFooter: false
			">
		<thead>
			<tr>
				<th data-options="field:'name',width:180,editor:'text'">名称</th>
	 <th data-options="field:'c1',width:180,editor:'text'">型号</th>
			</tr>
		</thead>
	</table>
	<script type="text/javascript">
		function formatProgress(value){
	    	if (value){
		    	var s = '<div style="width:100%;border:1px solid #ccc">' +
		    			'<div style="width:' + value + '%;background:#cc0000;color:#fff">' + value + '%' + '</div>'
		    			'</div>';
		    	return s;
	    	} else {
		    	return '';
	    	}
		}
		var editingId;
		function edit(){
	 
			if (editingId != undefined){
	
				$('#tg').treegrid('select', editingId);
				return;
			}else{
			 
		 
			}
			var row = $('#tg').treegrid('getSelected');
		
			if (row){
			 
				editingId = row.id;
			
				$('#tg').treegrid('beginEdit', editingId);
			}
		}
		function save(){
			   var t = $('#tg');
				t.treegrid('endEdit', editingId);
				var tmpid = editingId;
				editingId = undefined;
		 
	 	var row = $('#tg').treegrid("find",tmpid);
	 		jQuery.get("/pichan/typesUpdate.action?"+jQuery.param(row),"",function(data){
		datae = $.parseJSON(data);
			if (datae.result=='yes') {
	
			} else {
alert(datae.content);
			}
});
	 	 
	 
				
			//	var persons = 0;
			//	var rows = t.treegrid('getChildren');
				
		//		for(var i=0; i<rows.length; i++){
		//			var p = parseInt(rows[i].persons);
		/////			if (!isNaN(p)){
		//				persons += p;
		//			}
	//			}
		//		var frow = t.treegrid('getFooterRows')[0];
			//	frow.persons = persons;
			//	t.treegrid('reloadFooter');
			 
		}
		function cancel(){
			if (editingId != undefined){
				$('#tg').treegrid('cancelEdit', editingId);
				editingId = undefined;
			}
		}
			function deleted(){
		
	var row = $('#tg').treegrid('getSelected');
 
 
	 var params=jQuery.param(row);
			jQuery.get("/pichan/typesDelete.action?"+params,"",function(data){
			datae = $.parseJSON(data);
			 
			if (datae.result=='yes') {
		 
				$("#tg").treegrid("remove",row.id);
		 
			} else {
	alert(datae.content);
			}
			
			});
		}
					function add(){
		
	var row = $('#tg').treegrid('getSelected');

 if (row!=undefined) {
 
	jQuery.get("/pichan/typesAdd.action?pid="+row.id,"",function(data){
		datae = $.parseJSON(data);
			if (datae.result=='yes') {
			$("#tg").treegrid("insert",{after:row.id,data:{id:datae.data.id,name:datae.data.name,_parentId:row.id}});
			} else {
alert(datae.content);
			}
});
} else {
	jQuery.get("/pichan/typesAdd.action","",function(data){
			datae = $.parseJSON(data);
			if (datae.result=='yes') {
			$("#tg").treegrid("insert",{after:1,data:{id:datae.data.id,name:datae.data.name,_parentId:0}});
			} else {
alert(datae.content);
			}
	//$("#tg").treegrid("insert",{after:row.id,data:{id:-9999,name:'name33',_parentId:row.id}});
});

    //$("#tg").treegrid("insert",{before:1,data:{id:-1,name:'name-1'}});
}
 
		}
	</script>
</html>
