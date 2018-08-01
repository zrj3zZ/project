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
		
		// 预算名称
		function setysmc(id,obj){
			var ysmc = obj.value;
			var instanceid = $("#instanceid").val();
			if($("#YSMC_"+id).val()=='0'){
				alert("请选择正确的预算项！");
				return;
			}
			var pageUrl="sanbu_plan_bmfy_updateA2Ysmc.action";
	        $.post(pageUrl,{id:id,instanceid:instanceid,ysmc:ysmc},function(data){
	       		 if(data=='success'){
		             // window.location.reload();
		         }else{
		         	alert("操作异常");
		         }
		    });
		}
		
		// 资金来源
		function setzjly(id,obj){
			var zjly = obj.value;
			var instanceid = $("#instanceid").val();
			if($("#ZJLY_"+id).val()=='0'){
				alert("请选择正确的资金来源！");
				return;
			}
			var pageUrl="sanbu_plan_bmfy_updateA2Zjly.action";
	        $.post(pageUrl,{id:id,instanceid:instanceid,zjly:zjly},function(data){
	       		 if(data=='success'){
		             // window.location.reload();
		         }else{
		         	alert("操作异常");
		         }
		    });
		}
		
		// 驳回
		function bohui(id){
			// 打开意见页面，输入驳回意见，更新到日志表中
			var instanceid = $("#instanceid").val();
			var pageUrl = "sanbu_plan_bmfy_openA2Opinion.action?instanceid="+instanceid+"&id="+id;
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
		
		// 更新本月预算值
		function setValue(id,obj){
			var byys = obj.value;
			// 判断是否是数字
			if(isNaN(byys)){
				alert("请输入数字!");
			  	obj.value = "";
			  	$("#"+obj.id).focus();
			  	return;
			}
			// 判断不能是负数
			if(byys<=0){
				alert("请输入正确的数字!");
			  	obj.value = "";
			  	$("#"+obj.id).focus();
			  	return;
			}
			// 判断不能大于可用余额
			var kyye = $("#KYYE_"+id).val();
			if(Number(kyye)<Number(byys)){
				alert("本月预算不能大于可用余额!");
			  	obj.value = "";
			  	$("#"+obj.id).focus();
			  	return;
			}
			var num = byys;
			var instanceid = $("#instanceid").val();
			var pageUrl="sanbu_plan_bmfy_updateA2Byys.action";
	        $.post(pageUrl,{id:id,instanceid:instanceid,byys:byys},function(data){
	       		 $("#subNumTd").html(data);
		    	if(data=='error'){
		        	alert("操作异常");
		        }
		    });
		}
		
		// 提交审批
		function submit(){
			var instanceid = $("#instanceid").val();
			var checkeds = $("input:checked");
			var ids = [];
			for(var i=0;i<checkeds.length;i++){
				if(checkeds[i].value!='on'){
					ids.push(checkeds[i].value);
				}
				if($("#BYYS_"+checkeds[i].value).val()==''){
					alert("提交审批项目中本月预算未赋值！");
					return;
				}
			}
			if(ids.length==0){
				alert("请选择您要提交的项目！");
				return;
			}
			var pageUrl="sanbu_plan_bmfy_auditA2.action?instanceid="+instanceid+"&ids="+ids.join(',');
			$.post(pageUrl,{},function(data){
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
		
		// 流程中判断是否有未填写的内容
		function vaData(){
			var checkeds_YSMC = $("select[id*='YSMC']");
			for(var i=0;i<checkeds_YSMC.length;i++){
				if(checkeds_YSMC[i].value=='0'){
					alert("提交审批项目中预算名称未选择！");
					checkeds_YSMC[i].focus();
					return false;
				}
			}
			var checkeds_BYYS = $("input[id*='BYYS']");
			for(var i=0;i<checkeds_BYYS.length;i++){
				if(checkeds_BYYS[i].value==""){
					alert("提交审批项目中本月未预算填写！");
					checkeds_BYYS[i].focus();
					return false;
				}
			}
			return true;
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
		<div class="tools_nav">
			<div style="text-align:left;padding-right:10px;">
			<table width="100%">
				<TR>
					
				</TR>
			</table>
			</div>
		 </div>
		 <div >
		 	<table width="90%" style="margin:auto">
		 		<tr>
		 			<td  class="td_title" >预算月份</td>
		 			<td  class="td_data" >
		 				<s:property value="mainData.YSYF"/>月
		 			</td>
		 			<td class="td_title">编报部门</td>
		 			<td class="td_data" ><s:property value="mainData.BBR"/>/<s:property value="mainData.BBBM"/></td>
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
				<TD style="width:120px">预算名称</TD>
				<TD style="width:120px">期间费用年度预算额</TD>
				<TD style="width:120px">期间费用可用余额</TD>
				<TD style="width:100px">本月预算</TD>
				<TD style="width:100px">备注</TD>
				<TD style="width:40px">操作</TD>
			</TR>
			<s:iterator value="list"  status="status">
			<TR class="cell">
				<TD style="text-align:center"><s:property value="#status.count"/></TD>
				<TD><s:property value="YSMC"/></TD>
				<TD><s:property value="NDYSE"/></TD>
				<TD><input type="hidden" name="KYYE" id="KYYE_<s:property value="ID"/>" value="<s:property value="KYYE"/>"/><s:property value="KYYE"/></TD>
				<s:if test="BBZT=='A2'">
					<TD><input type="text" name="BYYS" onChange="setValue(<s:property value="ID"/>,this)" id="BYYS_<s:property value="ID"/>" style="width:95px" value="<s:property value="BYYS"/>"/></TD>
				</s:if>
				<s:else>
					<TD><s:property value="BYYS"/></TD>
				</s:else>
				<TD style="display:none"><s:property value="BBZT"/></TD>
				<TD><s:property value="BZ"/></TD>
				<s:if test="BBZT=='A2'">
					<TD><a  href="javascript:bohui(<s:property value="ID"/>)">驳回</a></TD>
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
