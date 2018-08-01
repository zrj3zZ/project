<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>IWORK综合应用管理系统</title> 
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>     		
	<script type="text/javascript"> 
	var selectedNodes;
		$(function(){ 
			
		})

		// 驳回
		function bohui(id,instanceid){
			// 打开意见页面，输入驳回意见，更新到日志表中
			// var instanceid = $("#instanceid").val();
			var pageUrl = "sanbu_plan_bmfy_openA6Opinion.action?instanceid="+instanceid+"&id="+id;
			art.dialog.open(pageUrl,{ 
						id:'Category_show', 
						cover:true,
						title:'驳回意见',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:580,
						cache:false,
						lock:true,
						height:400, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:false,
						close:function(){
							window.location.reload();
						}
					});
			
		}
		
		//送审计划单
		function audit(){
			var instanceid = $("#instanceid").val();
			var checkeds = $("input:checked");
			var ids = [];
			for(var i=0;i<checkeds.length;i++){
				if(checkeds[i].value!='on'){
					ids.push(checkeds[i].value);
				}
			}
			if(ids.length==0){
				alert("请选择您要提交的项目！");
				return;
			}
			var url = "sanbu_plan_bmfy_auditA6.action?instanceid="+instanceid+"&ids="+ids.join(',');
			$.post(url,{},function(data){
	       		 if(data=='success'){
		             window.location.reload();
		         }else{
		         	alert("操作异常");
		         }
		    });					
		}
		
		// 全选、全清功能
		function selectAll(){
			if($("input[name='chk_list']").attr("checked")){
				$("input[name='chk_list']").attr("checked",true);
			}else{
				$("input[name='chk_list']").attr("checked",false);
			}
		}
		
	</script>
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
		.cell:hover{
			background-color:#F0F0F0;
		}
		.cell td{
					margin:0;
					padding:3px 4px;
					white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;
					text-align:left;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
				}
		.selectCheck{
			border:0px;
			text-align:right;
		}
	</style>
	
	
</head>
<body class="easyui-layout"> 
<!-- TOP区 -->
	<div >
		 <div >
		 	<table width="90%" style="margin:auto">
		 		<tr>
		 			<td  class="td_title" >预算月份</td>
		 			<td  class="td_data" >
		 				<s:property value="mainData.YSYF"/>月
		 			</td>
		 			<td class="td_title">本月预算总计</td>
		 			<td class="td_data" id="subNumTd"><s:property value="mainData.BYYSZJ"/></td>
		 			<td class="td_title">单位:</td>
		 			<td class="td_data" >元</td>
		 		</tr>
		 	</table>
		 </div>
	
		<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<TD style="width:20px">序号</TD>
				<TD style="width:100px">部门名称</TD>
				<TD style="width:120px">预算名称</TD>
				<!--  <TD style="width:120px">资金来源</TD> -->
				<TD style="width:120px">期间费用年度预算额</TD>
				<TD style="width:120px">期间费用可用余额</TD>
				<TD style="width:100px">本月预算</TD>
				<TD style="width:100px">备注</TD>
				<TD style="width:40px">操作</TD>
			</TR>
			<s:iterator value="list"  status="status">
				<TR class="cell">
				<TD style="text-align:center"><s:property value="#status.count"/></TD>
				<TD><s:property value="CJBMMC"/></TD>
				<TD><s:property value="YSMC"/></TD>
				<!--  <TD><s:property value="ZJLY"/></TD> -->
				<TD><s:property value="NDYSE"/></TD>
				<TD><s:property value="KYYE"/></TD>
				<TD><s:property value="BYYS"/></TD>
				<TD><s:property value="BZ"/></TD>
				<TD style="display:none"><s:property value="BBZT"/></TD>
				<s:if test="BBZT=='A6'">
					<TD><a href="javascript:bohui(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)">驳回</a></TD>
				</s:if>
				<s:else>
					<TD></TD>
				</s:else>
			</TR>
			</s:iterator>
		</table>
		<s:hidden name="instanceid" id="instanceid"></s:hidden>
	</div>
</body>
</html>
