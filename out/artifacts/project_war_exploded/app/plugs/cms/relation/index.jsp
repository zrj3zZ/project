<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/selectListTools.js"></script>
<script type="text/javascript">
	$(function(){
			
			//加载栏目表
		   	$('#contentgrid').datagrid({
            	url:"cmsRelationAction!contentgrid.action?channelid=<s:property value='channelid' escapeHtml='false'/>",
			    loadMsg: "数据加载中，请稍后...",
			    fitColumns: false,
			    singleSelect:true,
			    nowrap:false,
			    rownumbers:true,
			    idField: 'id',
			    columns:[[
					{field:'portletname',title:'栏目名称',width:80},
					{field:'groupname',title:'分组名称',width:80},
					{field:'manager',title:'管理员',width:120},
					{field:'verifytype',title:'验证类型',width:60,
					formatter:function(value){
					    	if (value==0){						    	
						    	return '匿名访问';
					    	} else {
						    	return '登录验证';
					    	}
					  }
					},
					{field:'portlettype',title:'类型',width:60,
					formatter:function(value){
					    	if (value==0){						    	
						    	return '资讯';
					    	} else if(value==1) {
						    	return '外部链接';
					    	} else{
					    	    return '接口';
					    	}
					  }				
					},
					{field:'begindate',title:'有效期开始',width:80},
					{field:'enddate',title:'有效期结束',width:80},	
					{field:'status',title:'状态',width:50,
					formatter:function(value){
					    	if (value==0){						    	
						    	return '开启';
					    	} else {
						    	return '关闭';
					    	}
					    }
					},
					{field:'operate',title:'操作',width:80}
				]]
		    });		
		    
		    //添加栏目对话框
		    $('#addPortletDialog').dialog({
	            title:'频道栏目管理',
	            modal:true,
	            resizable:true,
				buttons:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
					    savePortlet(document.all.to);
					}
				},{
					text:'刷新',
					iconCls:'icon-reload',
					handler:function(){					
						$('#addPortlet').tree('reload');		
					}
				}]

			});
			
			//加载栏目树
			$('#addPortlet').tree({
			//	checkbox: true,
				url: 'cmsRelationAction!portlettree.action',
				 onDblClick:function(node){
                    if(node.state=='closed'){
               			$(this).tree('toggle', node.target);   			
               		}else if(node.state=='open'){
               			$(this).tree('toggle', node.target);
               		}else{       
               			moveSelect(node,document.all.to);
               	    }
               	    }  
			});
		    
			  //隐藏 
	       $('#addPortletDialog').dialog('close');  
	});	
	
	//保存操作
	function savePortlet(oSourceSel){
	   var s = '';
	   for(var i=0; i<oSourceSel.options.length; i++){	
		  if (s != '') s += ',';
		  s += oSourceSel.options[i].value;
	   }		
	   document.forms[0].addstr.value=s;
	   document.forms[0].channelid.value='<s:property value='channelid' escapeHtml='false'/>';				
	   var url='cmsAddPortlet.action';	
       document.forms[0].action=url;
       document.forms[0].submit();
	   return false; 
	}

	//删除
	function remove(){
		//根据表
		var row = $('#contentgrid').datagrid('getSelected');
		if (row){
		    $.messager.confirm('系统提示','确认删除?',function(result){  
		    if(result){
				var index = $('#contentgrid').datagrid('getRowIndex', row);
				$('#contentgrid').datagrid('deleteRow', index);			   
				document.forms[0].contentid.value=row.contentid==null?'':row.contentid;
				document.forms[0].channelid.value='<s:property value='channelid' escapeHtml='false'/>';
				var url='cmsRemovePortlet.action';	
			    document.forms[0].action=url;
			    document.forms[0].submit();
			 	return false; 
		 	}
		 	})
		}
	}	
	
</script>

<script type="text/javascript" src="iwork_js/plugs/cmsrelationaction.js"></script>

</head>
<body class="easyui-layout">
    <!-- TOP区 -->
	<div region="north" border="false" class="tools_nav"  style="background-color:#efefef;padding:0px;overflow:no">
	<div style="padding:2px;">
		<a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
    <a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>	
	</div>
	</div>
    <!-- 内容区 -->
	<div region="center" style="padding:3px;">
		<table id="contentgrid" style="margin:2px;">
		</table>
	</div>
	<!-- 添加对话框 -->
    <div id="addPortletDialog" icon="icon-edit"  style="width:500px;height:350px;padding-top:10px;background: #fafafa;">			      
       <table width='100%' border='0'>
		<tr>	
		<td width='20'>&nbsp; 	
	    </td>
		<td width='200' valgin='top' align='center'>
			<div style='border:1px #CCCCCC solid;;width:150px;height:250px;OVERFLOW-X:auto;OVERFLOW:scroll;text-align:left;'>
				<ul id="addPortlet"></ul>
			</div>
		</td>
		<td width='20' align='center'> 
		    <a href='#' title='添加' onClick="addSelect();"><img src=iwork_img/e_forward.gif border='0'></a><br><br>
		    <a href='#' title='删除' onClick="deleteSelect(document.all.to);"><img src=iwork_img/e_back.gif border='0'></a>
		</td>
	    <td width='200' valgin='top' align='center'>
	     	<s:property value='html'  escapeHtml='false'/>
	    </td>
	    <td>&nbsp; 
	    </td>	
	    </tr>
	  </table>        
    </div>
	<form id="contentform" method="post">
	<input type='hidden' id='addstr' name='addstr'>
	<input type='hidden' id='contentid' name='contentid'>
    <input type='hidden' id='channelid' name='channelid'>
    </form>	 
</body>
</html>
