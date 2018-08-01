<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link href="iwork_css/eagles_searcher.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
			.header td{
			font-weight:bold; 
			font-size:12px;
			padding:3px;
			white-space:nowrap; 
			padding-left:5px;
			background:#fafafa url('../iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #fff; 
			border-right:1px dotted #ccc;
		}
		.cell td{
					margin:0;
					padding:3px 4px;
					font-size:12px;
					overflow:hidden;
					text-align:left;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
				}

	</style>
	<script type="text/javascript">
		function rebuild(typeKey){ 
			art.dialog.confirm('重新构建索引需要很长时间，构建过程中会导致系统性能下降！是否执行索引构建？', function(){
			var url = "eaglesSearch_reBuildIndex.action";
			   $.post(url,{indexType:typeKey},function(data){
	         				if(data=='success'){
								art.dialog.tips('索引构建成功!',2); 
	         				}else{ 
	         					art.dialog.tips('索引构建失败!',2); 
	         				}
	    				}); 
			}, function(){
			    art.dialog.tips('操作取消');
			});
		}
	</script>
</head> 
<body >
<!-- TOP区 -->
	<div style="padding:10px;text-align:center;border:0px">
				<table width="90%" cellspacing="0" cellpadding="0" border="0" style="border:1px solid #efefef">
					<tr class="header">
						<td>序号</td>
						<td>索引名称</td>
						<td>索引键值</td>
						<td>操作</td>
					</tr>
					<s:iterator value="typeList" status="status">
						<tr class="cell">
							<td class="td_data"><s:property value="id"/></td>
							<td><s:property value="title"/></td>
							<td><s:property value="esType"/></td>
							<td><a href="javascript:rebuild('<s:property value="esType"/>');">重新构建索引</a></td> 
						</tr>
					</s:iterator>
				</table>
	</div>
</body>
</html>
