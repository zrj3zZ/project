<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#test').datagrid({
				title:'',
				iconCls:'icon-save',
				width:800,
				height:350,
				nowrap: false,
				striped: true,
				url:'showBulletin.action',
				sortName: 'ACTION_ID',
				sortOrder: 'desc',
				idField:'ACTION_ID',
				fit:true,
				frozenColumns:[[
	                {field:'ck',checkbox:true},
	                {title:'ACTION_ID',field:'ACTION_ID',width:80,sortable:true}
				]],
				columns:[[
			        {title:'Base Information',colspan:3},
					{field:'opt',title:'Operation',width:100,align:'center', rowspan:2,
						formatter:function(value,rec){
							return '<span style="color:red">Edit Delete</span>';
						}
					},
					{field:'FORM_PATH',title:'FORM_PATH',width:100,align:'center', rowspan:2},
					{field:'QUERY_PATH',title:'QUERY_PATH',width:100,align:'center', rowspan:2}
				],[
					{field:'TABLE_NAME',title:'TABLE_NAME',width:120},
					{field:'CLASS_NAME',title:'CLASS_NAME',width:120,rowspan:2,sortable:true},
					{field:'PK_NAME',title:'PK_NAME',width:150,rowspan:2}
				]],
				pagination:true,
				rownumbers:true,
				singleSelect:false,
				toolbar:[{
					text:'Add',
					iconCls:'icon-add',
					handler:function(){
						alert('add')
					}
				},{
					text:'Cut',
					iconCls:'icon-cut',
					handler:function(){
						alert('cut')
					}
				},'-',{
					text:'Save',
					iconCls:'icon-save',
					handler:function(){
						alert('save')
					}
				}]
			});
		}
		
		
		);
		function resize(){
			$('#test').datagrid({
				title: 'New Title',
				striped: true,
				width: 800,
				queryParams:{
					p:'param test',
					name:'My Name'
				}
			});
		}
		function getSelected(){
			var selected = $('#test').datagrid('getSelected');
			alert(selected.code+":"+selected.name);
		}
		function getSelections(){
			var ids = [];
			var rows = $('#test').datagrid('getSelections');
			for(var i=0;i<rows.length;i++){
				ids.push(rows[i].code);
			}
			alert(ids.join(':'))
		}
		function clearSelections(){
			$('#test').datagrid('clearSelections');
		}
		function selectRow(){
			$('#test').datagrid('selectRow',2);
		}
		function selectRecord(){
			$('#test').datagrid('selectRecord','002');
		}
		function unselectRow(){
			$('#test').datagrid('unselectRow',2);
		}
	</script>
	<form id="testForm" method="post">
		<table class="x-table">
			<tr class="table-tr">
				<td>name</td>
				<td><input type="text" name="name" id="idName"></td>
				<td>date</td>
				<td><input type="text" name="date" id="idDate"></td>
			</tr>
			<tr>
				<td colspan="4">
					<input type="button" name="btn">
				</td>
			</tr>
		</table>
		<table id="test"></table>
	</form>