<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>   
   	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
   	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
   	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_model.css"> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	
	<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
		$(function(){
	             var setting = {
					view: {
						selectedMulti: false
					},
					async: {
						enable: true,
						url:"ireport_designer_showTreeJson.action",
						dataType:"json",
						autoParam:["id"]
					},
					callback: {
						onClick: onClick
					}
				};
	             $.fn.zTree.init($("#iReportTree"), setting);
		})
		  function onClick(event, treeId, treeNode){
			  var zTree = $.fn.zTree.getZTreeObj("iReportTree");
                if(treeNode.type=='ireport'){
           			var reportType = treeNode.reportType;
           			var chartType = treeNode.chartType; 
           			openReport(treeNode.id, reportType, chartType);
           		}
           		else if(treeNode.type=='group'){ 
           			zTree.expandNode(treeNode, true, null, null, true);
           			 $("#groupid").val(treeNode.id);
					submitList(treeNode.id,0); 
           		}  
	     }
		//提交显示列表
		function submitList(id,type){   
			var url = "ireport_designer_showList.action?groupid="+id;
			window.frames["iReportListFrame"].location.href=url;
		}
		
		function openReport(reportId, reportType, chartType){
			if(reportId==''){
				alert("您选择的报表不正确");
				return false;
			}
			var pageUrl = 'url:ireport_designer_frameset.action?reportId='+reportId+'&reportType='+reportType+'&chartType='+chartType;
			openDialog(pageUrl);
		}
		function addReport(){
			var groupid = $("#groupid").val();
			if(groupid==''){
				alert("请选择左侧的分组导航");
				//return false;
			}else{
				var pageUrl = 'url:ireport_designer_newReport.action?groupid='+groupid;
				openDialog(pageUrl);
			}
		}
		
		function openDialog(pageUrl){
			art.dialog.open(pageUrl,{
						id:'openStepDefWinDiv',
						cover:true,
						title:'新增报表',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:900,
						cache:false,
						lock: true,
						height:500, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:true
					});
	
		}	 
		function remove(){
		      var id = $('#ireportId').val();
			  var groupid = $("#groupid").val();
		      if(id==""){
		      	alert('请选择要移除的报表!');
		      	return;
		      }
		      if(confirm("确定删除？") == true){
		            $.post('ireport_designer_delReport.action',{reportId:id,groupid:groupid},function(data){
		                   if(data=="ok"){
		                         location.reload();
		                   }
		            });
		      };
		}
		
		// iframe高度自适应
		function dyniframesize(down) { 
			var pTar = null; 
			if (document.getElementById){ 
				pTar = document.getElementById(down); 
			} else{ 
				eval('pTar = ' + down + ';'); 
			} 
			if (pTar && !window.opera){ 
				pTar.style.display="block" 
				if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){ 
					pTar.height = pTar.contentDocument.body.offsetHeight +20; 
					pTar.width = pTar.contentDocument.body.scrollWidth; 
				} else if (pTar.Document && pTar.Document.body.scrollHeight){ 
					pTar.height = pTar.Document.body.scrollHeight; 
					pTar.width = pTar.Document.body.scrollWidth; 
				} 
			} 
		}
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
	<div class="tools_nav">
		<a href="javascript:addReport();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加报表模型</a>
		<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		
	</div>
	</div>
	<div region="west"  split="false" border="false"  style="width:230px;padding-left:5px;overflow:auto; border-top:1px solid #F9FAFD">
				<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tree_top"></td>
					</tr>
					<tr>
						<td class="tree_main">
							<ul id="iReportTree" class="ztree"></ul>
						</td>
					</tr>
					<tr>
						<td class="tree_bottom"></td>
					</tr>
				</table>
    </div>
	<div region="center" style="padding:3px;border:0px;">
				<iframe id="iReportListFrame" name="iReportListFrame"  src="sysEngineIReport_Help.action" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="auto" allowtransparency="yes"  onload="javascript:dyniframesize('iReportListFrame');" width="100%" ></iframe>
			<s:form name="editForm" id="editForm" theme="simple">
				<s:hidden name="groupid" id="groupid"></s:hidden>
				<s:hidden name="ireportId" id="ireportId" value=""></s:hidden>
			</s:form>
	</div>
</body>
</html>
