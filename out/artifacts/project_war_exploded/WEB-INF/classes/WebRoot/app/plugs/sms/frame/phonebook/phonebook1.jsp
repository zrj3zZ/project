<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>短信平台-我的号码簿</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/plugs/qnumload.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript">
	$(function(){
				//加载分组树
				$('#typetree').tree({   
	                 url: 'typejson.action',  	                
	               	onClick:function(node){     		
	               		var url = 'load.action?typeid='+node.id;
	               		document.location.href=url; 
	               },
	                onLoadSuccess:function(node,data){
	               		var groupid = "<s:property value='typeid'  escapeHtml='false'/>";
	               		var tnode = $('#typetree').tree('find',groupid);
	               		if(tnode!=null){
	               			$('#typetree').tree('select',tnode.target);
	               		}
	               }
	             });
	                        
		$('#metadata_grid').datagrid({
		             //url:'qtypenums.action',   
                    //queryParams:{typeid:'<s:property value='typeid'  escapeHtml='false'/>', qname:'<s:property value='qname'  escapeHtml='false'/>' },
	             	//url:"qtypenums.action?typeid=<s:property value='typeid'  escapeHtml='false'/>&qname=<s:property value='qname'  escapeHtml='false'/>&qmobile=<s:property value='qmobile'  escapeHtml='false'/>&qattr1=<s:property value='qattr1'  escapeHtml='false'/>&qattr2=<s:property value='qattr2'  escapeHtml='false'/>&qattr3=<s:property value='qattr3'  escapeHtml='false'/>",
	             	url:"qtypenums.action?typeid=<s:property value='typeid'  escapeHtml='false'/>&qmobile=<s:property value='qmobile'  escapeHtml='false'/>&qname="+encodeURI(encodeURI('<s:property value='qname'  escapeHtml='false'/>'))+"&qattr1="+encodeURI(encodeURI('<s:property value='qattr1'  escapeHtml='false'/>'))+"&qattr2="+encodeURI(encodeURI('<s:property value='qattr2'  escapeHtml='false'/>'))+"&qattr3="+encodeURI(encodeURI('<s:property value='qattr3'  escapeHtml='false'/>')),
					loadMsg: "正在加载数据...",
					fitColumns: true,
					rownumbers:true,
					singleSelect:true,
					columns:[[
						{field:'TYPE',title:'分组',width:100},
						{field:'NAME',title:'姓名',width:80,align:'left'},
						{field:'PHONE',title:'手机号',width:120,align:'left'},
						{field:'ATTR1',title:'属性一',width:80,align:'left'},
						{field:'ATTR2',title:'属性二',width:80,align:'left'},
						{field:'ATTR3',title:'属性三',width:80,align:'left'},
						{field:'OPERATE',title:'操作',width:100}
					]]
				});		
		
	//新增分组	
		$('#typewindow').dialog({
			    title:"分组管理",
	            buttons:[{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){
						var typeadd=$('#typeaddj').val();
						if(typeadd!=""){
						document.forms[0].htype.value=typeadd;
						var url='savetype.action';			
						document.forms[0].action=url;  
						document.forms[0].method="post"
		                document.forms[0].target="hidden_frame";
		                document.forms[0].submit();
	
						$('#typewindow').dialog('close');
						alert("添加成功");
					    window.location.reload();
						}else{
						$.messager.alert('警告','分组名称不能为空!');
						}
					}
				},{
					text:'取消',
					handler:function(){
					
						$('#typewindow').dialog('close');
					}
				}]
			      });
			$('#typewindow').dialog('close');
//分组名称修改	
		$('#typeedit').dialog({
			    title:"分组修改",
	            buttons:[{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){
						var typeedit=$('#typeeditj').val();
					
						if(typeedit!=""){
						document.forms[0].htypeedit.value=typeedit;
						var url='edittype.action';			
						document.forms[0].action=url;  
						document.forms[0].method="post"
		                document.forms[0].target="hidden_frame";
		                document.forms[0].submit();	
						$('#typeedit').dialog('close');
						alert("修改分组成功");
						//parent.location.reload();   
						 window.location.reload();
						}else{
						$.messager.alert('警告','分组名称不能为空!');
						}
						
					}
					
				},{
					text:'取消',
					handler:function(){
						$('#typeedit').dialog('close');
					}
					
				}]
				
			      });
			$('#typeedit').dialog('close');
        
	//修改号码
$('#hiddiv').dialog({
			    title:"保存设置"
			    
			 		       
			});
			  
        $('#hiddiv').dialog('close');


});

function removeItem(id){
	$.ajax({
		type: 'POST',
		url: 'delnumj.action',
		data: {cid:id},
		dataType: 'text',
		success: function(data,status){
		 window.location.reload();
		}
	});
}
</script>

<script type="text/javascript" src="iwork_js/plugs/qnumload.js"></script>

</head>
<body class="easyui-layout">

<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
	<div class="tools_nav">
		<a href="javascript:addType();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增分组</a>
		<a href="javascript:removetype();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
		<a href="javascript:edittype();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">修改分组</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		
	</div>
	</div>
	<div region="west"  split="false" border="false"  style="width:100px;padding:0px;overflow:hidden; background-color:#fff;border-top:1px solid #F9FAFD">
		<div title="我的菜单" icon="icon-reload" closable="false" style="overflow:auto;padding:5px;">
						 <ul id="typetree">
						 </ul> 
					</div>
    </div>
     
    <div region="center" style="padding:3px;border:1px solid #efefef">
     姓&nbsp;名：<input type=text class="easyui-validatebox"  size="10" name='name' value='<s:property value='qname'/>' id='name'>　
     手机号：<input type=text class="easyui-validatebox" size="10" name='mobilenum' value='<s:property value='qmobile'/>' id='mobilenum'>
	&nbsp;&nbsp;属性一：<input type=text class="easyui-validatebox" size="10"  name='extend1' value='<s:property value='extend1'/>' id='extend1'>　
	<br/><br/>属性二：<input type=text class="easyui-validatebox"  size="10" name='extend2' value='<s:property value='extend2'/>' id='extend2'>	
	&nbsp;属性三：<input type=text class="easyui-validatebox"  size="10" name='extend3' value='<s:property value='extend3'/>' id='extend3'>
	&nbsp;&nbsp;<input type=button onclick="addNumj()"  value="新增" >&nbsp;&nbsp;<input type=button onclick="qNumj()" value="查询">
			<br /><br />
			<table id="metadata_grid" style="margin:500px;border:1px solid #efefef"></table>
	</div>
	<!--号码簿修改窗口-->
	<div id='hiddiv' style="padding:5px;width:300px;height:220px;">
<table></table></div> 

   <div id="typewindow" style="padding: 5px; width: 200px; height: 150px;">
   组名：<input type=text class="easyui-validatebox" required="true" missingMessage="必填" name=addtypej id=typeaddj size=10 >
   </div>
    <div id="typeedit" style="padding: 5px; width: 200px; height: 150px;">
   修改后组名：<input type=text class="easyui-validatebox" required="true" missingMessage="必填" name=typeeditj id=typeeditj size=10 >
   </div>
  <s:form>
   <input type=hidden name=htype id=htype>
   <input type=hidden name=htypeedit id=htypeedit>
   <input type=hidden name=htypeeditid id=htypeeditid>
   <!-- 号码簿新增号码或查询的各个属性 -->
    <input type=hidden name=htypee id=htypee>
   <input type=hidden name=hmobile id=hmobile>
   <input type=hidden name=hname id=hname>
   <input type=hidden name=hattr1 id=hattr1>
   <input type=hidden name=hattr2 id=hattr2>
   <input type=hidden name=hattr3 id=hattr3>
   <!-- 删除号码 -->
   <input type=hidden name=hcid2 id=hcid2>
   <!-- 修改号码的各个属性 -->
    <input type=hidden name=hcid11 id=hcid11>
    <input type=hidden name=htypee1 id=htypee1>
   <input type=hidden name=hmobile1 id=hmobile1>
   <input type=hidden name=hname1 id=hname1>
   <input type=hidden name=hattr11 id=hattr11>
   <input type=hidden name=hattr21 id=hattr21>
   <input type=hidden name=hattr31 id=hattr31>
     <iframe name='hidden_frame' id="hidden_frame" width= "0"  height= "0" style="VISIBILITY: hidden">       
   </iframe>
   </s:form>
  
</body>
</html>
