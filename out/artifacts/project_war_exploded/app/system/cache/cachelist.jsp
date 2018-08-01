<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>缓存管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>	
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/system/cachelist.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	 function failResponse(){
      	 art.dialog.tips("发送失败",3);
			return true; 
	   }
	   function successResponse(responseText, statusText, xhr, $form){
			var arr=responseText;
			art.dialog.tips("刷新成功",1);
			return true;
		}
		function execute(key){
			$("#key").val(key);
			var submitOption = {
				    	error :failResponse,
				    	success:successResponse
			    	}; 
       		$('form').attr('action','reloadCacheList.action');
       		$('form').ajaxSubmit(submitOption);
		}
</script>
<body style="padding:5px;">
<table cellpadding="1" cellspacing="1" class="table3" >
	<thead>
    <tr  class="tablehead" >
        <th  width="39">序号</th>
        <th width="279"  >缓存名称</th> 
        <th width="232" >键值</th>
        <th width="240"  >操作</th>
    </tr>
    </thead>
    <tbody>
<s:iterator value="list" status="status">
	<tr class="RowData2" >
        <td><s:property value="#status.count"/></td>
        <td><s:property value="cacheName"/></td>
        <td><s:property value="key"/></td>
        <td><a href="javascript:execute('<s:property value="key"/>');"><img src="iwork_img/refresh2.gif" border="0"/>清除缓存</a></td>
    </tr>
</s:iterator>
</tbody>
</table>
<s:form action="reloadCacheList.action">
	<input type="hidden" name="key" id="key"  value="">
</s:form>
</body>
</html>
