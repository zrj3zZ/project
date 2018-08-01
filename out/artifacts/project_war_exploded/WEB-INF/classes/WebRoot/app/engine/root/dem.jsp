<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformsearch_index.css">
	<script type="text/javascript" src="iwork_js/engine/iformsearch_index.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style>
		.tbLable{
			width:90%;
			border:1px solid #efefef;
			margin-left:auto;
			margin-right:auto;
		}
		.tbLable th{
			height:30px;
			background-color:#ffffee;
			border-bottom:1px solid #ccc;
		}
		.tbLable td{ 
			font-size:12px;
			padding:3px;
			height:25px;
			text-align:left;
		}  
		.tbLable tr:hover{
			background-color:#efefef;
		}
	</style>
<script type="text/javascript" >
	function clearData(demid){
		var width=500;
		var height=300;
		
		var content = "确认删除吗？删除该数据可导致系统无法使用";
		art.dialog({
	    id: 'testID',
	    content:content,
	    button: [
	        {
	             name: '确定删除',
	            callback: function () {
	                  this.content('开始执行清理操作 ...').time(2);
		               doClear(demid);
		                return false;
	            },
	            focus: true
	        },
	        {
	             name: '取消删除',
	            callback: function () {
	                 this.close();
	            }
	        }
	    ]
	});
		  
	}
	function doClear(demid){
	 		$.post('system_dem_doclear.action',{demid:demid},function(data)
            {
		    	if(data=='success'){
		    		 alert("清理完毕");
		    		 window.location.reload();
		    	}else{
		    		 alert("清理异常请稍后");
		    	}
		  });  
	}
</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="height:70px;border:1px solid #efefef;font-size:20px;font-family:微软雅黑">
	<img src="iwork_img/gear3.gif" style="width:50px"/>主数据数据初始化 
   </div>
 
	<div region="center" style="padding:5px;border:0px;height:300px;text-align:center;"> 
			<table class="tbLable" width="100%"> 
				<tr>     
					<th width="100">序号</th> 
					<th>标题</th>
					<th>实例数</th>
					<th>操作</th>
				</tr>
				<s:iterator value="list" status="status">
				<tr>    
					<td><s:property value="#status.count"/></td> 
					<td><img src="iwork_img/icon/dem.png" alt="主数据维护"/>&nbsp;<s:property value="TITLE"/></td>
					<td style="font-size:16px;color:red"><s:property value="SIZE"/></td>
					<td><a href="javascript:clearData(<s:property value="DEMID"/>)">清空</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:showInfolist(<s:property value="DEMID"/>)">查看明细</a></td>
				</tr>
				</s:iterator>
				
			</table>
	</div>
</body> 
</html>
