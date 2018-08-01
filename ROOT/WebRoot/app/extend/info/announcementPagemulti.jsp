<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">


	$(function() {
		initTreeTo();
	});
	function initTreeTo() {
		var khbh = $("#khbh").val();
		var setting = {
			async : {
				enable : true,
				url : "zqb_nnouncement_usermulti_ztree.action?khbh="+khbh,
				dataType : "json",
				autoParam : [ "id", "nodeType", "companyId"]
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				onClick : onClick
			}
		};
		$.fn.zTree.init($("#phoneTree"), setting);
	}
	function onClick(event, treeId, treeNode, clickFlag) {
		if(treeNode.userid!=""){
			var dxyjtzr = parent.document.getElementById("DXYJTZR");
			var dxyjtzrer = "<div id='"+treeNode.userid+"'><span class='selItem'>"+treeNode.name+"<img id='"+treeNode.userid+"O' onclick='del(this)' src='iwork_img/close.gif' border='0'></span></div>";
			if(dxyjtzr.value!=""){
				var array = dxyjtzr.value.split(",");
				if(array.length<4){
					if(dxyjtzr.value.indexOf(treeNode.userid+",")==-1){
						dxyjtzr.value+=(treeNode.userid+",");
						parent.document.getElementById("DXYJTZRDIV").innerHTML+=dxyjtzrer;
					}
				}else{
					alert("最多允许选三人!");
				}
			}else{
				dxyjtzr.value+=(treeNode.userid+",");
				parent.document.getElementById("DXYJTZRDIV").innerHTML=dxyjtzrer;
			}
		}
	}
</script>
</head>
<body>
	<input value="<% out.print(request.getParameter("khbh"));%>" id="khbh" type="hidden" class="{string:true}"/>
	<div region="west" style="width:100%;height:100%;float: left;border:0px;padding:3px; padding:0px; line-height: 20px; overflow: auto; border="false">
		<ul id="phoneTree" class="ztree"></ul>
	</div>
</body>
</html>
