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
		function getSelectBox(){
			var sbox = $("#id");
			var sbox_str = "";
			alert(sbox.val());
			for(i=0;i<sbox.length;i++){
				if(i==0){
					sbox_str = sbox[i].value;
				}else{
					sbox_str = sbox_str + "@" +sbox[i].value ;
				}
				
			}
			alert(sbox_str);
		}
		function closeWin(){
			api.close();
		}
	</script>
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
		function add(){
			var instanceid = $("#instanceid").val();
			var chk_value =[];//定义一个数组    
            $('input[name="id"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数    
            chk_value.push($(this).val());//将选中的值添加到数组chk_value中    
            })
            
             var pageUrl="ndzb_addHt_action.action";
             var xmid_temp = chk_value.join(',');
	         //alert(xmid_temp);
	         $.post(pageUrl,{instanceid:instanceid,xmid:xmid_temp},
	         	function(data){
		         if(data=='ok'){
		             closeWin(); 
		         }else{
		         	alert("操作异常");
		         }
		    });
			//addHtform.submit();
		}
		
		function closeWin(){
			
			api.close();
		}
	</script>

</head>
<style type="text/css">
table.altrowstable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}
table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
.oddrowcolor{
	background-color:#d4e3e5;
}
.evenrowcolor{
	background-color:#c3dde0;
}
</style>
<style>
		* {
			margin:0px;
			padding:0px;
			font-size:12px;
		}
		img {
			border: 0 none;
		}
		.header td{
			font-weight:bold;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:#fafafa url('../../iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		} 
		.cellTop td{
			background:#efefef;
			padding:3px;
			text-align:left;
			
		}
		.cell:hover{
			background-color:#FAFAFA;
		}
		.cell td{
					margin:0;
					padding:3px 4px;
					white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;
					text-align:left;
				}
		.selectCheck{
			border:0px;
			text-align:right;
		}
	</style>
<body>
	
	<s:form name="addHtform"  action='add_Xhhtzjjhd_action.action'>
	<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;height:35px;">
	<div class="tools_nav">
		 <a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-ok">确定</a>
		<a href="javascript:closeWin();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">取消</a>
	</div>
	</div>
	
			
	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<TD style="width:20px"></TD>
				<TD style="width:100px">合同名称</TD>
				<TD style="width:100px">合同编号</TD>
				<TD style="width:90px">合同金额</TD>
				<!-- 
				<TD style="width:90px">合同未付金额</TD>
				<td style="width:80px">累计执行</TD>
				<td style="width:80px">年度付款指标</td>
				<td style="width:60px">本年已执行</td>
				 -->
				
			</TR>
			<s:iterator value="list"  status="status">
				<TR class="cell">
													
				<td><input type="checkbox" name="id"  id="xmid" value="<s:property value="HTBH"/>@<s:property value="XMBH"/>" ></td>
				<TD><s:property value="HTMC"/></TD>
				<TD><s:property value="HTBH"/></TD>
				<TD><s:property value="HT_XMJE"/></TD>
				<!-- 
				<TD><s:property value="HTWFKJE"/></TD>
				<TD><s:property value="LJYZX"/></TD>
				<TD><s:property value="NDFKZB"/></TD>
				<TD><s:property value="BNDYZX"/></TD>	
				-->															
			</TR>
			<s:iterator value="PJDATALIST"  id="inner" >
				<TR class="cell">
				<td></td>
				<TD style="text-align:left;padding-left:20px;">
				<input type="checkbox" name="id"  id="xmid" value="<s:property value="#inner.HTBH"/>@<s:property value="XMBH"/>" >
				<img src="iwork_img/arrow.png" alt="" />
				
				<s:property value="#inner.HTBH"/></TD>
				<TD ><s:property value="#inner.HTMC"/></TD>
				<TD ><s:property value="#inner.HT_XMJE"/></TD>
				<!-- 
				<TD ><s:property value="#inner.HTWFKJE"/></TD>
				<TD ><s:property value="#inner.LJYZX"/></TD>
				<TD ><s:property value="#inner.NDFKZB"/></TD>
				<TD ><s:property value="#inner.BNDYZX"/></TD>
				-->
				
			</TR>
			</s:iterator>
			</s:iterator>
		</table>
	<s:hidden name="instanceid" id="instanceid"></s:hidden>
	<s:hidden name="rowid" id="rowid"></s:hidden>
	</div>
	
	

	</s:form>
</body>
	
</html>
