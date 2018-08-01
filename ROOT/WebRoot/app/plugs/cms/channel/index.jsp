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
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js" charset="gb2312"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){ 
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 添加频道
					add(); return false;
				}
		  else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
					save(); return false;
				}		
}); //快捷键
	</script>
<script type="text/javascript">
	$(function(){
	
	        //加载导航树
		    $('#channeltree').tree({   
                url: 'cmsChannelAction!openjson.action',  
                onSelect:function(node){
	                if(node.attributes.type=='channel'){
	                	$('#channelgrid').datagrid('selectRecord',node.id.split('_')[0]);   
	              	}
                }, 
                onClick:function(node){
	                if(node.attributes.type=='group'){
	                	
	                	var pageurl =  "cmsChannelAction!index.action?gname="+encodeURI(node.text); 
	                 document.location.href = pageurl; 
	              	}
                }, 
              	onDblClick:function(node){
              	if(node.attributes.type=='group'){
              	$(this).tree('toggle', node.target);   
              	}else{
              	document.forms[0].channelid.value=node.id==null?'':node.id;
				document.forms[0].channelname.value=node.text==null?'':node.text;		
				if(node.attributes.groupname!=null){
                  var ridaolen=document.forms[0].groupname.length; 
                  for(var i=0;i<ridaolen;i++){
                    if(node.attributes.groupname==document.forms[0].groupname[i].value){
                      document.forms[0].groupname[i].checked=true;
                    }
                  }
				}
			    document.forms[0].manager.value=node.attributes.manager==null?'':node.attributes.manager;
			    document.forms[0].template.value=node.attributes.template==null?'':node.attributes.template;
			    if(node.attributes.verifytype!=null){
                  var ridaolen=document.forms[0].verifytype.length;
                  for(var i=0;i<ridaolen;i++){
                    if(node.attributes.verifytype==document.forms[0].verifytype[i].value){
                      document.forms[0].verifytype[i].checked=true;
                    }
                  }
				}
			    document.forms[0].browse.value=node.attributes.browse==null?'':node.attributes.browse;
			    $('#begindate').datebox('setValue',node.attributes.begindate==null?'':node.attributes.begindate);
			    $('#enddate').datebox('setValue',node.attributes.enddate==null?'':node.attributes.enddate);
			    if(node.attributes.status!=null){
                  var ridaolen=document.forms[0].status.length;
                  for(var i=0;i<ridaolen;i++){
                    if(node.attributes.status==document.forms[0].status[i].value){
                      document.forms[0].status[i].checked=true;
                    }
                  }
				}
				document.forms[0].memo.value=node.attributes.memo==null?'':node.attributes.memo;	
				document.forms[0].type.value='edit';
				$('#channeldialog').dialog('open'); 
              	}
              	},
           	    onLoadSuccess:function(node,data){
            	var groupId = "<s:property value='gname' escapeHtml='false'/>"+"_group";
                var tnode = $('#channeltree').tree('find',groupId);
                if(tnode!=null){
                $('#channeltree').tree('select',tnode.target);
                }      		
               }
            });
			
			//加载频道表
		   	$('#channelgrid').datagrid({
            	url:"cmsChannelAction!channelgrid.action?gname="+encodeURI(encodeURI('<s:property value='gname' escapeHtml='false'/>')),
			    loadMsg: "数据加载中，请稍后...",
			    fitColumns: true,
			    singleSelect:true,
			    nowrap:false,
			    rownumbers:true,
			    idField: 'id',
			    columns:[[
					{field:'channelname',title:'频道名称',width:100},
					{field:'manager',title:'管理员',width:100},
					{field:'verifytype',title:'验证类型',width:100,
					formatter:function(value){
					    	if (value==0){						    	
						    	return '匿名访问';
					    	} else {
						    	return '登录验证';
					    	}
					  }
					},
					{field:'begindate',title:'有效期开始',width:100},
					{field:'enddate',title:'有效期结束',width:100},	
					{field:'status',title:'状态',width:100,
					formatter:function(value){
					    	if (value==0){						    	
						    	return '开启';
					    	} else {
						    	return '关闭';
					    	}
					    }
					},
					{field:'operate',title:'操作',width:100}			
				]],
				onDblClickRow:function(){
				var row = $('#channelgrid').datagrid('getSelected');
				document.forms[0].channelid.value=row.id==null?'':row.id;
				document.forms[0].channelname.value=row.channelname==null?'':row.channelname;		
				if(row.groupname!=null){
                  var ridaolen=document.forms[0].groupname.length; 
                  for(var i=0;i<ridaolen;i++){
                    if(row.groupname==document.forms[0].groupname[i].value){
                      document.forms[0].groupname[i].checked=true;
                    }
                  }
				}
			    document.forms[0].manager.value=row.manager==null?'':row.manager;
			    document.forms[0].template.value=row.template==null?'':row.template;
			    if(row.verifytype!=null){
                  var ridaolen=document.forms[0].verifytype.length;
                  for(var i=0;i<ridaolen;i++){
                    if(row.verifytype==document.forms[0].verifytype[i].value){
                      document.forms[0].verifytype[i].checked=true;
                    }
                  }
				}
			    document.forms[0].browse.value=row.browse==null?'':row.browse;
			    $('#begindate').datebox('setValue',row.begindate==null?'':row.begindate);
			    $('#enddate').datebox('setValue',row.enddate==null?'':row.enddate);
			    if(row.status!=null){
                  var ridaolen=document.forms[0].status.length;
                  for(var i=0;i<ridaolen;i++){
                    if(row.status==document.forms[0].status[i].value){
                      document.forms[0].status[i].checked=true;
                    }
                  }
				}
				document.forms[0].memo.value=row.memo==null?'':row.memo;	
				document.forms[0].type.value='edit';
				document.forms[0].gname.value='<s:property value='gname' escapeHtml='false'/>';
				$('#channeldialog').dialog('open'); 
			    }
		    });
			
		   //权限设置
		   var m='<s:property value='flag' escapeHtml='false'/>';
		   if(m=='false'){
		   $('#addchannel').linkbutton('disable');
		   $('#removechannel').linkbutton('disable');
		   document.forms[0].manager.disabled=true;
		   }
		   
		   //隐藏
		   $('#channeldialog').dialog('close');
			
			
	});	
	
	//设置
	function Set(){
		var row = $('#channelgrid').datagrid('getSelected');
		document.forms[0].channelid.value=row.id==null?'':row.id;
		document.forms[0].channelname.value=row.channelname==null?'':row.channelname;		
		if(row.groupname!=null){
	               var ridaolen=document.forms[0].groupname.length; 
	               for(var i=0;i<ridaolen;i++){
	                 if(row.groupname==document.forms[0].groupname[i].value){
	                   document.forms[0].groupname[i].checked=true;
	                 }
	               }
		}
	    document.forms[0].manager.value=row.manager==null?'':row.manager;
	    document.forms[0].template.value=row.template==null?'':row.template;
	    if(row.verifytype!=null){
	               var ridaolen=document.forms[0].verifytype.length;
	               for(var i=0;i<ridaolen;i++){
	                 if(row.verifytype==document.forms[0].verifytype[i].value){
	                   document.forms[0].verifytype[i].checked=true;
	                 }
	               }
		}
	    document.forms[0].browse.value=row.browse==null?'':row.browse;
	    $('#begindate').datebox('setValue',row.begindate==null?'':row.begindate);
	    $('#enddate').datebox('setValue',row.enddate==null?'':row.enddate);
	    if(row.status!=null){
	               var ridaolen=document.forms[0].status.length;
	               for(var i=0;i<ridaolen;i++){
	                 if(row.status==document.forms[0].status[i].value){
	                   document.forms[0].status[i].checked=true;
	                 }
	               }
		}
		document.forms[0].memo.value=row.memo==null?'':row.memo;	
		document.forms[0].type.value='edit';
		document.forms[0].gname.value='<s:property value='gname' escapeHtml='false'/>';
		$('#channeldialog').dialog('open'); 
	}
	
	//添加
	function add(){
	  document.forms[0].channelname.value='';		
      document.forms[0].groupname[0].checked=true;           
	  document.forms[0].manager.value='';
	  document.forms[0].template.value='';			    
      document.forms[0].verifytype[0].checked=true;       
	  document.forms[0].browse.value='';
	  $('#begindate').datebox('setValue','');
	  $('#enddate').datebox('setValue','');	
      document.forms[0].status[0].checked=true;
	  document.forms[0].memo.value='';	
	  document.forms[0].type.value='add';
	  document.forms[0].gname.value='<s:property value='gname' escapeHtml='false'/>';
	  $('#channeldialog').dialog('open');
	}
	
	//删除
	function remove(){
		//根据表
		var row = $('#channelgrid').datagrid('getSelected');
		if (row){
		    $.messager.confirm('系统提示','确认删除?',function(result){  
		    if(result){
				var index = $('#channelgrid').datagrid('getRowIndex', row);
				$('#channelgrid').datagrid('deleteRow', index);
				document.forms[0].channelid.value=row.id==null?'':row.id;
				document.forms[0].type.value='remove';
				document.forms[0].gname.value='<s:property value='gname' escapeHtml='false'/>';
				var url='cmsChannelEdit.action';	
			    document.forms[0].action=url;
			    document.forms[0].submit();
			 	return false; 
		 	}
		 	})
		}
	}
//保存
function save(){				 
					 if(!$('#iform').form('validate')){
					        
					     }
					 else{
					    document.forms[0].manager.disabled=false;
					   var url='cmsChannelEdit.action';	
			           document.forms[0].action=url;
			           document.forms[0].submit();
			 		   return false;					 
					 }
					  
			}
//取消
function cancel(){
			$('#channeldialog').dialog('close');
		 }
//
$.extend($.fn.validatebox.defaults.rules, {
    maxLength: {
        validator: function(value, param){
            var cArr = value.match(/[^\x00-\xff]/ig);
            var length = value.length + (cArr == null ? 0 : cArr.length);  
            return length <= param[0];
        },
        message: '输入值过长'
    }
});		 			
					
</script>
<s:property value='pstrScript'  escapeHtml='false'/>
</head> 
<body class="easyui-layout">
    <!-- TOP区 -->
	<div region="north" border="false"  style="padding:0px;overflow:no">
	<div style="padding:2px;"  class="tools_nav">
		<a href="javascript:add();" class="easyui-linkbutton" id='addchannel' plain="true" iconCls="icon-add">添加频道</a>
		<a href="javascript:remove();" class="easyui-linkbutton" id='removechannel' plain="true" iconCls="icon-remove">删除频道</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
	</div>
	</div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="width:200px;border-right:1px solid #efefef;padding:0px;overflow:hidden;">	
	    <ul id="channeltree">
		</ul> 
    </div>
    <!-- 内容区 -->
	<div region="center" style="padding:3px;border:0px;">
		<table id="channelgrid" style="margin:2px;">
		</table>
	</div>
	<!-- 编辑窗口 -->
    <div id="channeldialog" icon="icon-edit" class="easyui-dialog" title="频道信息维护" style="width:650px;height:450px;padding-top:10px;background: #fafafa;" buttons="#dlg-buttons">			
      <form id="channelform" method="post">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
	    <td align='right'>频道名称：</td>
        <td><input class="easyui-validatebox" type="text" id="channelname" name="channelname" required="true" validType="maxLength[64]"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>分组：</td>
        <td><input type="radio" checked id="groupname" name="groupname" value='频道'>频道<input type="radio" id="groupname" name="groupname" value='专题'>专题&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>管理员：</td>
        <td><input class="easyui-validatebox" type="text" id="manager" name="manager" validType="maxLength[256]"></input></td>
        </tr>       
        <tr>
	    <td align='right'>模版文件：</td>
        <td><input class="easyui-validatebox" type="text" id="template" name="template" required="true" validType="maxLength[64]"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>验证类型：</td>
        <td><input type="radio" checked id="verifytype" name="verifytype" value='0'>匿名访问<input type="radio" id="verifytype" name="verifytype" value='1'>登录验证&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>浏览权限：</td>
        <td><input class="easyui-validatebox" type="text" id="browse" name="browse" validType="maxLength[512]"></input></td>
        </tr>
        <tr>
	    <td align='right'>有效期：</td>
        <td><input class="easyui-datebox"  editable='false' id="begindate" name="begindate" style="width:100px;" required="true"></input>&nbsp;至&nbsp;<input class="easyui-datebox"  editable='false' id="enddate" name="enddate" style="width:100px;" required="true"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>  
        <tr>
	    <td align='right'>状态：</td>
        <td><input type="radio" checked id="status" name="status" value='0'>开启<input type="radio" id="status" name="status" value='1'>关闭&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>备注：</td>
        <td><textarea id="memo" class="easyui-validatebox" name="memo" style="height:60px;width:155px;" validType="maxLength[300]"></textarea></td>
        </tr>
        </table>
        <input type='hidden' id='channelid' name='channelid'>
        <input type='hidden' id='type' name='type'>
        <input type='hidden' id='gname' name='gname'>
       </form>	 
      <div>
      <div id="dlg-buttons">
		<a  href="javascript:save();" class="easyui-linkbutton" iconCls="icon-save">保存</a>
		<a href="javascript:cancel();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
	 </div>
</body>
</html>
