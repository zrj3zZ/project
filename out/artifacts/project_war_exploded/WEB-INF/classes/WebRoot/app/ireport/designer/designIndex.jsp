<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" /> 
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	
	<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增存储
					 addMetaData(); return false;
				}
		  else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
					doSubmit(); return false;
				}		
}); //快捷键
	</script>
	<script type="text/javascript">
	$(function(){
				//加载导航树
				$('#iReportTree').tree({   
	                 url: 'ireport_designer_showTreeJson.action',
	               	onClick:function(node){
	               		if(node.attributes.type=='ireport'){
	               			var url = 'ireport_designer_index.action?metadataid='+node.id;
							window.parent.addTab(node.text,url,'');
	               		}else if(node.attributes.type=='group'){
	               			document.location.href = "ireport_designer_index.action?groupid="+node.id;
	               		}
	               },
	               onLoadSuccess:function(node,data){
	               		var groupid = "<s:property value='groupid'  escapeHtml='false'/>";
	               		var tnode = $('#metadatatree').tree('find',groupid);
	               		if(tnode!=null){
	               			$('#metadatatree').tree('select',tnode.target);
	               		}
	               }
	             });
	             $('#report_grid').datagrid({
	             	url:"ireport_designer_index.action?groupid=<s:property value='groupid'  escapeHtml='false'/>",
					loadMsg: "正在加载数据...",
					fitColumns: true,
					singleSelect:true,
					columns:[[
						{field:'ID',title:'序号',width:30},
						{field:'REPORTNAME',title:'报表名称',width:100},
						{field:'REPORTTYPE',title:'类型',width:80,align:'left'},
						{field:'STATUS',title:'状态',width:80,align:'left'},
						{field:'MASTER',title:'管理员',width:150}
					]],
					idField:'ID',
					onDblClickRow:function(rowIndex){
					var row = $('#report_grid').datagrid('getSelected');
					var url = 'sysEngineMetadataMap!index.action?metadataid='+row.ID;
					}
				});
				
				$('#editForm').form({  
				    onSubmit:function(){  
				        return $(this).form('validate');  
				    },  
				    success:function(data){  
				        alert(data);  
				    }  
				});
				
		})
	function addReport(){
		var groupid = $("#groupid").val();
		if(groupid==''){
			lhgdialog.tips("请选择左侧的分组导航",2);
			return false;
		}
		var pageUrl = 'ireport_designer_newReport.action?groupid='+groupid;
		art.dialog.open(pageUrl,{
					id:'openStepDefWinDiv',
					cover:true,
					title:'新增报表',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1000,
					cache:false,
					lock: true,
					height:500, 
					iconTitle:false, 
					extendDrag:true,
					autoSize:true
				});
			dg.ShowDialog();
	}	
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
	<div style="padding:2px;border-bottom:1px solid #efefef">
		<a href="javascript:addReport();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加报表模型</a>
		<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		
	</div>
	</div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false" title="报表导航"  style="width:200px;padding:0px;overflow:hidden; background-color:F2FAFD;border-top:1px solid #F9FAFD">
		<div title="我的菜单" icon="icon-reload" closable="false" style="overflow:auto;padding:0px;">
						 <ul id="iReportTree">
						 </ul> 
					</div>
    </div>
	<div region="center" style="padding:3px;">
			<table id="report_grid" style="margin:2px;"></table>
			<s:hidden name="groupid" id="groupid"></s:hidden>
	</div>
</body>
</html>
