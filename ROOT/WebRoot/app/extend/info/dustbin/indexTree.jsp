<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>WBS</title>
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
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
	
	<style type="text/css">
		.memoTitle{
			font-size:14px;
			padding:5px;
			color:#666;
		}
		.memoTitle a{
			font-size:12px;
			padding:5px;
		}
	</style>
	<script type="text/javascript">
		//加载导航树  
		$(function(){
			initTree();
		});
		
		function initTree(){
			var setting = {
						async: {
							enable: true, 
							url:"zqb_event_eventJSON.action",  
							dataType:"json",
							autoParam:["id","nodeType"]
						},
						callback: {
							onClick:onClick
						} 
					};
				$.fn.zTree.init($("#dustbinTree"), setting);//加载导航树 
		}
		
		//点击事件
		function onClick(event, treeId, treeNode, clickFlag){
			var khbh = treeNode.khbh;
			var khmc = treeNode.khmc;
			var zqdm = treeNode.zqdm;
			var zqjc = treeNode.zqjc;
			//var pageUrl = "sysDem_DataList.action?demUUID=1dfe958166a347188339af1337e25fb7&KHBH="+khbh+"&init_params=KHBH="+khbh+";KHMC="+encodeURI(khmc);
			var pageUrl = encodeURI("zqb_dustbin_index.action?khbh="+khbh+"&khmc="+khmc+"&zqdmxs="+zqdm+"&zqjcxs="+zqjc);
			$("#mainFrame").attr("src",pageUrl);
		}
		
		//判断是否显示各编辑按钮
		function showOperateButton(nodeType){
			$("#operationButton").show();
			if(nodeType=="root"){
				$("#operationButton").hide();
			}
		//	if(nodeType=="company"){
		//		$("#editTemplateType").hide();
		//		$("#delTemplateType").hide();
		//	}
		}
		
		//全部展开
	    function expandAll() {
			var zTree = $.fn.zTree.getZTreeObj("announcementTree");
				zTree.expandAll(true);
		}

	   	//全部折叠
		function unExpandAll() {
			var zTree = $.fn.zTree.getZTreeObj("announcementTree");
			zTree.expandAll(false);
		}
		
		
	</script>
</head> 
<body class="easyui-layout">

	<div region="west" style="width:250px;background-color:#efefef" border="false" >
		<ul id="dustbinTree" class="ztree"></ul>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;">
    	<iframe id="mainFrame" name="mainFrame" scrolling="no" frameborder="0"    style="width:100%;height:100%;"></iframe> 
    </div>
  
		<s:hidden name="khbh" id="khbh"></s:hidden>
	<s:hidden name="name" id="name"></s:hidden>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>
