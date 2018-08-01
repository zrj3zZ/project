<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
		<title>短信平台-短信日志查询</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">		
		
		 <link rel="stylesheet" type="text/css" href="iwork_css/plugs/loginuserj.css">
		
		</head>
<body>
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
	<div style="margin-left:10px;margin-top:10px;margin-bottom:5px;width:97%;">
  <table class="font"><tr><td>
发送人：<input type=text class='actionsoftInput' width='5' id='sender' name='sender'  value='<s:property value='hoprator'  escapeHtml='false'/>'>
</td><td>
开通日期：<input type=text  name='startdate' id='startdate' width='5'  value='<s:property value='hstartd'  escapeHtml='false'/>' class='easyui-datebox' editable='false'>
至 <input type=text  name='enddate' id='enddate' width='5' class='easyui-datebox' editable='false'  value='<s:property value='hendd'  escapeHtml='false'/>'>
</td><td>
	<input type=button value='查 询'  onClick="userQuery()" name='buttonquery'  border='0'>
</td></tr></table>
</div>

<div region="center" style="padding:3px;">
		<table id="metadata_grid" style="margin:2px;"></table>
	</div>
	<div id="typeedit" style="padding: 5px; width: 400px; height: 200px;">
	<table class="font" width=100% align=center border=0 cellspacing=0 cellpadding=0>
        <tr><td>移动默认通道:</td><td align='left'><select name='cmchannel' id='cmchannel'><option value='0' selected>不修改</option>
        <option value='1' >1-天润融通</option></select>
        </td></tr>
		<tr><td>联通默认通道:</td><td align='left'><select name='cuchannel' id='cuchannel'><option value='0' selected>不修改</option>
        <option value='1' >1-天润融通</option></select></td></tr>
		<tr><td>电信默认通道:</td><td align='left'><select name='ctchannel' id='ctchannel'><option value='0' selected>不修改</option>
        <option value='1' >1-天润融通</option></select></td></tr>
	    <tr><td>短信额度:</td><td align='left'><select name='typechange' id='typechange'><option value='0' selected>不修改</option><option value='add'>增加</option><option value='minus'>减少</option></select>&nbsp; &nbsp;&nbsp;数量:<input type=text size='6' name='countchange' id='countchange' value='0'>条</td></tr>
	    </table>
   </div>
<s:form>
  <!--查询用户-->
  <input type=hidden name=hoprator id=hoprator>
  <input type=hidden name=hstartd id=hstartd>
  <input type=hidden name=hendd id=hendd>
 <!-- 用户设置 -->
 <input type=hidden name=hcid  id=hcid>
 <input type=hidden name=hmchannel  id=hmchannel>
 <input type=hidden name=huchannel id=huchannel>
 <input type=hidden name=htchannel id=htchannel>
 <input type=hidden name=htype id=htype>
 <input type=hidden name=hcount id=hcount>
</s:form>
</body>
<script>
$(function(){	                        
		$('#metadata_grid').datagrid({
	             	url:"quserj.action?hoprator=<s:property value='hoprator'  escapeHtml='false'/>&hstartd=<s:property value='hstartd'  escapeHtml='false'/>&hstarth=<s:property value='hstarth'  escapeHtml='false'/>&hstartm=<s:property value='hstartm'  escapeHtml='false'/>&hendd=<s:property value='hendd'  escapeHtml='false'/>&hendh=<s:property value='hendh'  escapeHtml='false'/>",
					loadMsg: "正在加载数据...",
					fitColumns: true,
					rownumbers:true,
					singleSelect:true,
					columns:[[
						{field:'sender',title:'发送人',width:70,align:'center'},
						{field:'time',title:'开通日期',width:150,align:'center'},
						{field:'operate',title:'设置',width:150,align:'center'}
						
					]]
				});	
		
//用户设置	
		$('#typeedit').dialog({
			    title:"用户设置",
	            buttons:[{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){
						var cmc=$('#cmchannel').val();
						var ctc=$('#ctchannel').val();
						var cuc=$('#cuchannel').val();
						var typec=$('#typechange').val();
						var count=$('#countchange').val();
						if(typec=='0'){
						$.messager.alert('警告','请选择增加或减少！');
						return false;
						}
						document.forms[0].hmchannel.value=cmc;
						document.forms[0].htchannel.value=ctc;
			            document.forms[0].huchannel.value=cuc;
						document.forms[0].htype.value=typec;
						document.forms[0].hcount.value=count;
						var url='saveuser.action';			
						document.forms[0].action=url;  
						document.forms[0].method="post";
		              //  document.forms[0].target="hidden_frame";
		                document.forms[0].submit();
	
						$('#typeedit').dialog('close');
						
					}
				},{
					text:'取消',
					handler:function(){
						$('#typeedit').dialog('close');
					}
				}]
			      });
			      $('#typeedit').dialog('close');
			      });	
</script>

<script type="text/javascript" src="iwork_js/plugs/loginuserj.js"></script>

</html>