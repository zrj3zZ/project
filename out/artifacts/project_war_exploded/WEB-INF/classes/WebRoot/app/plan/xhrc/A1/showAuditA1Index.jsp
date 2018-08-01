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
		
		function add(){
			var instanceid = $("#instanceid").val();
			var pageUrl = "sanbu_plan_xhrc_dic.action?instanceid="+instanceid;
			art.dialog.open(pageUrl,{ 
						id:'Category_show', 
						cover:true,
						title:'选择项目',
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
		
		// 删除项目
		function remove(id){
		var instanceid = $("#instanceid").val();
			 var pageUrl="sanbu_plan_xhrc_remove.action";
	         $.post(pageUrl,{id:id,instanceid:instanceid},function(data){
		         if(data=='success'){
		             window.location.reload();
		         }else{
		         	alert("操作异常");
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
			var pageUrl="sanbu_plan_xhrc_updateByys.action";
	        $.post(pageUrl,{id:id,instanceid:instanceid,byys:byys},function(data){
	       		 $("#subNumTd").html(data);
		    	if(data=='error'){
		        	alert("操作异常");
		        }
		    });
		}
		
		// 付款理由
		function setfkly(id,obj){
			var fkly = obj.value;
			var instanceid = $("#instanceid").val();
			if($("#FKLY_"+id).val()=='0'){
				alert("请选择正确的付款理由！");
				return;
			}
			var pageUrl="sanbu_plan_xhrc_updateFkly.action";
	        $.post(pageUrl,{id:id,instanceid:instanceid,fkly:fkly},function(data){
	       		 if(data=='success'){
		             // window.location.reload();
		         }else{
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
				if($("#FKLY_"+checkeds[i].value).val()=='0'){
					alert("提交审批项目中付款理由未选择！");
					return;
				}
			}
			if(ids.length==0){
				alert("请选择您要提交的项目！");
				return;
			}
			var pageUrl="sanbu_plan_xhrc_auditA1.action?instanceid="+instanceid+"&ids="+ids.join(',');
			$.post(pageUrl,{},function(data){
				alert(data);
				window.location.reload();
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
		<div class="tools_nav">
			<div style="text-align:left;padding-right:10px;">
			<table width="100%">
				<TR>
					<td>
						<s:if test="mainData.STATUS==mainData.QDBB">
							<a href="###" onclick="add()" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加预算</a>
							<a href="###" onclick="submit()" class="easyui-linkbutton" plain="true" iconCls="icon-process-trans">提交审批</a>
						</s:if>
					</td>
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
				<TD style="width:30px"><input type="checkbox" name="chk_list" onclick="selectAll()"></TD>
				<TD style="width:100px">项目名称</TD>
				<TD style="width:100px">项目编号</TD>
				<TD style="width:100px">年度支出成本指标</TD>
				<TD style="width:80px">可用余额</TD>
				<td style="width:100px">截至上月已支出</TD>
				<td style="width:80px">备注</td>
				<td style="width:80px">本月预算</td>
				<td style="width:80px">付款理由</td>
				<TD style="width:50px">操作</TD>
			</TR>
			<s:iterator value="list"  status="status">
				<TR class="cell">
				<s:if test="BBZT=='A1'">
					<TD><input type="checkbox" name="chk_list" value="<s:property value="ID"/>"></TD>
				</s:if>
				<s:else>
					<TD></TD>
				</s:else>
				<TD><s:property value="XMMC"/></TD>
				<TD><s:property value="XMBH"/></TD>
				<TD><s:property value="NDZCCBZB"/></TD>
				<TD><input type="hidden" name="KYYE" id="KYYE_<s:property value="ID"/>" value="<s:property value="KYYE"/>"/><s:property value="KYYE"/></TD>
				<TD><s:property value="SYYZC"/></TD>
				<TD><s:property value="BZ"/></TD>
				<s:if test="BBZT=='A1'">
					<TD><input type="text" name="BYYS" onChange="setValue(<s:property value="ID"/>,this)" id="BYYS_<s:property value="ID"/>" style="width:75px" value="<s:property value="BYYS"/>"/></TD>
				</s:if>
				<s:else>
					<TD><s:property value="BYYS"/></TD>
				</s:else>
				<TD style="display:none"><s:property value="BBZT"/></TD>
				
				<s:if test="BBZT=='A1'">
					<TD><s:select list="fklyList" name="FKLY" id="FKLY_%{ID}" theme="simple" onChange="setfkly('%{ID}',this)" listKey="FKLY" listValue="FKLY" value="FKLY" headerKey="0" headerValue="请选择"></s:select></TD>
				</s:if>
				<s:else>
					<TD><s:property value="FKLY"/></TD>
				</s:else>
					
				<s:if test="BBZT=='A1'">
					<TD><a  href="javascript:remove(<s:property value="ID"/>)">删除</a></TD>
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
