<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title> 
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" /> 
<script type="text/javascript">
	$(function(){
		    //加载导航树
		    $('#grouptree').tree({   
                url: 'orgGroupAction!openjson.action',  
              	onClick:function(node){
	              	if(node.attributes.type=="list"){
	              	 $('#itempanel').panel('close');
	                 $('#grouppanel').panel('open');
	              	}else if(node.attributes.type=="group"){
	              	 $('#groupgrid').datagrid('unselectAll');
	                 $('#grouppanel').panel('close');
	                 $('#itempanel').panel('open');
	                 document.location.href = "orgGroupAction!index.action?gid="+ node.id;      
	              	}
              	},
                onLoadSuccess:function(node,data){
	               	 var groupId = "<s:property value='gid'  escapeHtml='false'/>";
	                 var tnode = $('#grouptree').tree('find',groupId);
		             if(tnode!=null){
			             $('#grouptree').tree('select',tnode.target);
			             $('#groupgrid').datagrid('unselectAll');
			             $('#grouppanel').panel('close');
			             $('#itempanel').panel('open');
		             }      		
                }
            });
            
                 
            //加载团队表
            $('#groupgrid').datagrid({
            	url:"orgGroupAction!groupgrid.action",
			    loadMsg: "数据加载中，请稍后...",
			    fitColumns: true,
			    singleSelect:true,
			    nowrap:false,
			    rownumbers:true,
			    idField: 'id',
			    columns:[[
					{field:'groupname',title:'团队名称',width:100},
					{field:'groupcharge',title:'团队负责人',width:80},
					{field:'groupstate',title:'状态',width:80},
					{field:'begindate',title:'有效期开始',width:100},
					{field:'enddate',title:'有效期结束',width:100},
					{field:'groupmemo',title:'备注',width:100}
				]],
				onDblClickRow:function(){
				var row = $('#groupgrid').datagrid('getSelected');
				document.forms[0].group_id.value=row.id==null?'':row.id;
				document.forms[0].group_name.value=row.groupname==null?'':row.groupname;
			    document.forms[0].group_charge.value=row.groupcharge==null?'':row.groupcharge;
			    $('#group_state').combobox('setValue',row.groupstate==null?'':row.groupstate);
			    $('#begindate').datebox('setValue',row.begindate==null?'':row.begindate);
			    $('#enddate').datebox('setValue',row.enddate==null?'':row.enddate);
				document.forms[0].group_memo.value=row.groupmemo==null?'':row.groupmemo;	
				document.forms[0].type.value='edit';
				$('#groupdialog').dialog('open'); 
			    }
		    });
		    //加载子项表
            $('#itemgrid').datagrid({
            	url:"orgGroupAction!itemgrid.action?gid=<s:property value='gid' escapeHtml='false'/>",
			    loadMsg: "数据加载中，请稍后...",
			    fitColumns: true,
			    singleSelect:true,
			    nowrap:false,
			    rownumbers:true,
			    idField: 'id',
			    columns:[[
					{field:'itemname',title:'组织组成',width:100},
					{field:'itemtype',title:'类型',width:100}	
				]],
				toolbar:[{
					id:'personadd',
					text:'添加人员',
					iconCls:'icon-add',
					handler:function(){				 
                      $('#addpersondialog').dialog('open');  		 	 
					}
				},'-',{
					id:'deptadd',
					text:'添加部门',
					iconCls:'icon-add',
					handler:function(){					 
                    $('#adddeptdialog').dialog('open');
					}
				},'-',{
				    id:'groupadd',
					text:'添加团队',
					iconCls:'icon-add',
					handler:function(){	
			     	$('#addgroupdialog').dialog('open');
					}
				},'-',{
					id:'itemcut',
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){							        
						var row = $('#itemgrid').datagrid('getSelected');
						if (row){
						    $.messager.confirm('确认','确认删除?',function(result){  
							    if(result){
									var index = $('#itemgrid').datagrid('getRowIndex', row);
									$('#itemgrid').datagrid('deleteRow', index);
									document.forms[0].item_id.value=row.id==null?'':row.id;
									document.forms[0].addgroupid.value='<s:property value='gid' escapeHtml='false'/>';		
									var url='itemcut.action';	
								    document.forms[0].action=url;
								    document.forms[0].submit();
								 	return false; 
							 	}
						 	});
						}
					}
				},'-',{
					id:'itemview',
					text:'查看团队人员清单',
					iconCls:'icon-search',
					handler:function(){		        
					   $('#persondialog').dialog('open');
					}
				}]
		    });
		   			
			//加载子表添加团队对话框
	       $('#addgroupdialog').dialog({
	            title:'添加团队',
	            modal:true,
	            resizable:true,
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
					var nodes = $('#addgroup').tree('getChecked');
					if(nodes==""||nodes==null){
					 $.messager.alert('系统提示','请做出选择!','info'); 
		 			 return false;
				    }
					var s = '';
					for(var i=0; i<nodes.length; i++){
						if (s != '') s += ',';
						s += nodes[i].id;
					}
					var t = '';
					for(var i=0; i<nodes.length; i++){
						if (t != '') t += ',';
						t += nodes[i].text;
					}
					var r=s+':'+t;
					document.forms[0].addgroupstr.value=r;
					document.forms[0].addgroupid.value='<s:property value='gid' escapeHtml='false'/>';				
					var url='saveAddGroup.action';	
			        document.forms[0].action=url;
			        document.forms[0].submit();
			 		return false; 
					}
				},{
					text:'刷新',
					iconCls:'icon-reload',
					handler:function(){
						var node = $('#addgroup').tree('getSelected');
						if (node){
							$('#addgroup').tree('reload', node.target);
						} else {
							$('#addgroup').tree('reload');
						}
					}
				}]

			});
			
			//子表加载团队树
			$('#addgroup').tree({
				checkbox: true,
				url: 'orgGroupAction!grouptree.action?gid=<s:property value='gid' escapeHtml='false'/>',
				onClick:function(node){
					$(this).tree('toggle', node.target);
				}
			});
			
			//加载子表添加部门对话框
	       $('#adddeptdialog').dialog({
	            title:'添加部门',
	            modal:true,
	            resizable:true,
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
					var nodes = $('#adddept').tree('getChecked');
					if(nodes==""||nodes==null){
					 $.messager.alert('系统提示','请做出选择!','info'); 
		 			 return false;
				    }
					var s = '';
					var t = '';
					for(var i=0; i<nodes.length; i++){
					var nid=nodes[i].id.split('_')[1];
					if(nid=='list'||nid=='com'){
					    continue;
					}
					var f=false;
					var p = $('#adddept').tree('getParent',nodes[i].target);
					var pid=p.id.split('_')[1];
					if(pid=='dept'){	
					for(var j=0; j<nodes.length; j++){
					if(p.id==nodes[j].id){
					f=true;
					break;
					}			
					}
					}				
					if(f==true){
					continue;
					}	
					if (s != '') s += ',';
					s += nodes[i].id;
					if (t != '') t += ',';
					t += nodes[i].text;
					}			
					var r=s+':'+t;
				    document.forms[0].addgroupstr.value=r;
					document.forms[0].addgroupid.value='<s:property value='gid' escapeHtml='false'/>';				
					var url='saveAddDept.action';	
			        document.forms[0].action=url;
			        document.forms[0].submit();
			 		return false; 
					}
				},{
					text:'刷新',
					iconCls:'icon-reload',
					handler:function(){
						var node = $('#adddept').tree('getSelected');
						if (node){
							$('#adddept').tree('reload', node.target);
						} else {
							$('#adddept').tree('reload');
						}
					}
				}]

			});
			
			//子表加载部门树
			$('#adddept').tree({
				checkbox: true,
				url: 'orgGroupAction!depttree.action',
				onClick:function(node){
					$(this).tree('toggle', node.target);
				}
			});
			
			//加载子表添加人员对话框
	       $('#addpersondialog').dialog({
	            title:'添加人员',
	            modal:true,
	            resizable:true,
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
					var nodes = $('#addperson').tree('getChecked');
					if(nodes==""||nodes==null){
					 $.messager.alert('系统提示','请做出选择!','info'); 
		 			 return false;
				    }
					var s = '';
					var t = '';
					for(var i=0; i<nodes.length; i++){
					var nodeType = nodes[i].attributes.nodeTye;
					if (nodeType != 'per'){
						
						continue;
					}
					if (s != '') s += ',';
					s += nodes[i].id;
					if (t != '') t += ',';
					t += nodes[i].text;
					}			
					var r=s+':'+t;
				    document.forms[0].addgroupstr.value=r;
				    
					document.forms[0].addgroupid.value='<s:property value='gid' escapeHtml='false'/>';				
					var url='saveAddPerson.action';	
			        document.forms[0].action=url;
			        document.forms[0].submit();
			 		return false; 
					}
				},{
					text:'刷新',
					iconCls:'icon-reload',
					handler:function(){
						var node = $('#addperson').tree('getSelected');
						if (node){
							$('#addperson').tree('reload', node.target);
						} else {
							$('#addperson').tree('reload');
						}
					}
				}]

			});
			
			//子表加载人员树
			$('#addperson').tree({
				checkbox: true,
				url: 'orgGroupAction!persontree.action',
				onClick:function(node){
					$(this).tree('toggle', node.target);
				}
			});
			
			//加载子表添加团队对话框
	       $('#persondialog').dialog({
	            title:'团队人员清单',
	            resizable:true,
	            modal:true
			});
			
			//加载团队人员清单表
            $('#persongrid').datagrid({
            	url:'orgGroupAction!persongrid.action?gid=<s:property value='gid' escapeHtml='false'/>',
			    loadMsg: "数据加载中，请稍后...",
			    fitColumns: true,
			    nowrap:false,
			    rownumbers:true,
			    columns:[[
					{field:'userid',title:'ID',width:100},
					{field:'username',title:'姓名',width:100},
					{field:'departmentname',title:'部门名称',width:100},
					{field:'orgrole',title:'角色',width:100}
				]]
		    });
			
	        //隐藏 
	       $('#groupdialog').dialog('close');  
	       $('#addgroupdialog').dialog('close');
	       $('#adddeptdialog').dialog('close');
	       $('#addpersondialog').dialog('close');
	       $('#persondialog').dialog('close');
	       
	        //日期格式转换
		   $('#begindate').datebox({
			    formatter: function(date){ return date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();}
			   
		   });			
		   $('#enddate').datebox({
			    formatter: function(date){ return date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();}
			  
		   });		
			
	});	
</script>
<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键

		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return;
		     }  
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					 addGroup(); return;
				}
		  else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
					save(); return;
				}			
}); //快捷键

	</script>
<script type="text/javascript" src="iwork_js/org/orggroupaction.js"></script>

</head>
<body class="easyui-layout">
    <!-- TOP区 -->
	<div region="north" border="false" >
	<div style="padding:2px;" class="tools_nav">
		<a href="javascript:addGroup();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新建团队</a>
		<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除团队</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
		
	</div>
	</div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="background-color:F2FAFD;width:200px;padding:0px;overflow:auto;border-right:1px solid #efefef">	
	    <ul id="grouptree">
		</ul> 
    </div>
    <!-- 内容区 -->
	<div region="center" style="padding:0px;border:0px;">
	<div id="grouppanel" class="easyui-panel" border="false" >
		<table id="groupgrid" style="margin:2px;">
		</table>
	</div>
	<div id="itempanel" class="easyui-panel" border="false" closed='true'>
		<table id="itemgrid" style="margin:2px;">
	    </table>		
	</div>
	</div>
	<!-- 编辑窗口 -->
    <div id="groupdialog" icon="icon-edit" class="easyui-dialog" title="编辑" style="width:500px;height:420px;padding-top:10px;background: #fafafa;" buttons="#dlg-buttons">			
      <form id="groupform" method="post">
        <table width="100%" border="0" cellpadding="3" cellspacing="10">
        <tr>
	    <td align='right'><label for="group_name">团队名称：</label></td>
        <td><input class="easyui-validatebox" type="text" id="group_name" name="group_name" required="true"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'><label for="group_charge">团队负责人：</label></td>
        <td><input class="easyui-validatebox" type="text" id="group_charge" name="group_charge"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'><label for="group_state">状态：</label></td>
        <td>
		<input type="radio" name="group_state" id="group_state_open" value="开启"  checked="checked" style="border:0px;"/><label for="group_state_open">开启</label>
		<input type="radio" name="group_state" id="group_state_close" value="关闭" style="border:0px;"/><label for="group_state_close">关闭</label>
		<span style='color:red'>*</span>
        </td>
        </tr>
        <tr>
	    <td align='right'><label for="begindate">有效期开始：</label></td>
        <td><input class="easyui-datebox"  editable='false' id="begindate" name="begindate" style="width:153px;"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'><label for="enddate">有效期结束：</label></td>
        <td><input class="easyui-datebox"  editable='false' id="enddate" name="enddate" style="width:153px;" ></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'><label for="group_memo">备注：</label></td>
        <td><textarea id="group_memo" name="group_memo" style="width:300px;height:80px"></textarea></td>
        </tr>
        </table>
        <input type='hidden' id='type' name='type'>
        <input type='hidden' id='group_id' name='group_id'>
        <input type='hidden' id='addgroupstr' name='addgroupstr'>
        <input type='hidden' id='addgroupid' name='addgroupid'>
        <input type='hidden' id='item_id' name='item_id'>
       </form>	 
      </div>
      <div id="dlg-buttons">
		<a  href="javascript:save();" class="easyui-linkbutton" iconCls="icon-save">保存</a>
		<a href="javascript:cancel();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
	 </div>
	 
    <!-- 添加团队窗口 -->
    <div id="addgroupdialog" icon="icon-edit"  style="width:400px;height:450px;padding-top:10px;background: #fafafa;">			
        <ul id="addgroup"></ul>
    </div>
    <!-- 添加部门窗口 -->
    <div id="adddeptdialog" icon="icon-edit"  style="width:300px;height:400px;padding-top:10px;background: #fafafa;">			
        <ul id="adddept"></ul>
    </div>
    <!-- 添加人员窗口 -->
    <div id="addpersondialog" icon="icon-edit"  style="width:300px;height:400px;padding-top:10px;background: #fafafa;">			
        <ul id="addperson"></ul>
    </div>
    
     <!-- 团队人员清单窗口 -->
    <div id="persondialog" icon="icon-edit"  style="width:500px;height:240px;padding:2px;background: #fafafa;">			
        <table id="persongrid" style="margin:2px;">
		</table>
    </div>
</body>
</html>
