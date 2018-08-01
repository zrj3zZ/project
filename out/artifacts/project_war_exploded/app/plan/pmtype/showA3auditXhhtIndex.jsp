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
		function showht(htid){
		   			    
			//var pageUrl = 'xhht_loadHtInfo_action.action?htid='+htid;
			var url = 'xhht_loadHtInfo_action.action?htid='+htid;
			var target = "dem"+htid;

			var win_width = window.screen.width;
			var page = window.open(url,target,'width='+win_width+',height=800,top=10,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = url;
				
		}	
			
		function vaData(){		    		    		   
			var checkeds_FKLY = $("input[id*='FKLY']");
			for(var i=0;i<checkeds_FKLY.length;i++){
				if(checkeds_FKLY[i].value==""){
					alert("提交审批项目中付款理由未填写！");
					checkeds_FKLY[i].focus();
					return false;
				}
			}
			var checkeds_BYYS = $("input[id*='BYYS']");
			for(var i=0;i<checkeds_BYYS.length;i++){
				if(checkeds_BYYS[i].value==""){
					alert("提交审批项目中本月未填写！");
					checkeds_BYYS[i].focus();
					return false;
				}
			}
			return true;
		}
		// 删除项目
		//delete_Xhhtzjjhdsub_action.action?rowid="+row.id+"&instanceid="+instanceid
		function remove(id){
			var instanceid = $("#instanceid").val();
			var pageUrl="delete_Xhhtzjjhdsub_action.action";
	        $.post(pageUrl,{rowid:id,instanceid:instanceid},function(data){
		        if(data=='ok'){
		            window.location.reload();
		        }else{
		         	alert("操作异常");
		        }
		    });
		}
		function backAudit(id,instanceid){
			
			//alert(instanceid);
			var pageUrl = "zg_loadA3Opinion_action.action?instanceid="+instanceid+"&rowid="+id+"&shjd=A3";
			art.dialog.open(pageUrl,{ 
						id:'Category_show', 
						cover:true,
						title:'审核意见',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:350,
						cache:false,
						lock:true,
						height:250, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:false,
						close:function(){
							window.location.reload();
						}
					});
		
		}
		// 更新本月预算值
		function setbyys(id,instanceid,obj){
			var byys = obj.value;
			
			// alert(byys);
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
			// 判断不能大于年度付款指标
			
			var num = byys;
			
			var pageUrl="xhht_byysXhhtMx_action.action";
	        $.post(pageUrl,{rowid:id,instanceid:instanceid,num:num},function(data){
	       		$("#subNumTd").html(data);
		    	if(data=='error'){
		        	alert("操作异常");
		        }
		    });
		}
		
		// 付款理由
		function setfkly(id,instanceid,obj){
			var fkly = obj.value;
			
			var pageUrl="xhht_fklyXhhtMx_action.action";
	        $.post(pageUrl,{rowid:id,instanceid:instanceid,fkly:fkly},function(data){
	       		 if(data=='success'){
		             // window.location.reload();
		         }else{
		         	alert("操作异常");
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
				if($("#ZJLY"+checkeds[i].value).val()==''){
					alert("提交审批项目中付款理由未填写！");
					return;
				}
				if($("#BYYS_"+checkeds[i].value).val()==''){
					alert("提交审批项目中本月预算未赋值！");
					return;
				}
				if($("#FKLY_"+checkeds[i].value).val()==''){
					alert("提交审批项目中付款理由未填写！");
					return;
				}
			}
			//if(ids.length==0){
			//	alert("请选择您要提交的项目！");
			//	return;
			//}
			var url = "edit_Xhhtzjjhd_action.action?instanceid="+instanceid;
			$.post(url,{},function(data){
	       		 if(data=='ok'){
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
		 			<td class="td_data" id="subNumTd"><s:property value="sumNum"/></td>
		 			<td class="td_title">单位:</td>
		 			<td class="td_data" >万元</td>
		 		</tr>
		 	</table>
		 </div>
	
		<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<TD style="width:20px"></TD>
				<TD style="width:100px">名称</TD>
				<TD style="width:100px">编号</TD>
				<TD style="width:90px">金额</TD>
				<TD style="width:90px">合同未付金额</TD>
				<td style="width:80px">累计执行</TD>
				<td style="width:80px">执行比例</TD>
				<td style="width:80px">年度付款指标</td>
				<td style="width:60px">本年已执行</td>
				<td style="width:60px">本月预算</td>
				<td style="width:60px">付款理由</td>
				<TD style="width:40px">操作</TD>
				
			</TR>
			<s:iterator value="list"  status="status">
				<TR class="cellTop">
				
					
				
				<td></td>
				<TD><s:property value="XMMC"/></TD>
				<TD><s:property value="XMBH"/></TD>
				
				<TD><s:property value="HTZJE"/></TD>
				<TD><s:property value="HTWFKJE"/></TD>
				<TD><s:property value="LJZXJE"/></TD>
				<TD><s:property value="LJZX_RATE"/>%</TD>
				<TD><s:property value="NDFKZB"/></TD>
				<TD><s:property value="BYYZX"/></TD>
				
				
				<TD><s:property value="BYYS"/></TD>
				
				<TD style="display:none"><s:property value="BBZT"/></TD>
				
				
					<TD><s:property value="FKLY"/></TD>
				
					
				
					<TD></TD>
				
			</TR>
			<s:iterator value="children"  id="inner" >
				<TR class="cell">
				<td></td>
				<TD style="text-align:left;padding-left:20px;">
				
				<img src="iwork_img/arrow.png" alt="" />
				
				<s:property value="#inner.XMMC"/></TD>
				<TD ><s:property value="#inner.XMBH"/></TD>
				<TD ><s:property value="#inner.HTZJE"/></TD>
				<TD ><s:property value="#inner.HTWFKJE"/></TD>
				<TD ><s:property value="#inner.LJZXJE"/></TD>
				<TD ><s:property value="#inner.NDFKZB"/></TD>
				<TD ><s:property value="#inner.BYYZX"/></TD>
				
				
				<TD ><input type="text" name="BYYS" onChange="setbyys(<s:property value="#inner.id"/>,<s:property value="#inner.INSTANCEID"/>,this)" id="BYYS_<s:property value="#inner.id"/>" style="width:58px" value="<s:property value="#inner.BYYS"/>"/></TD>
				
				
				<TD style="display:none"><s:property value="#inner.BBZT"/></TD>
				
				<s:if test="#inner.BBZT=='A3'">
					<TD><s:select list="fklyList" name="FKLY" id="FKLY_%{#inner.id}" theme="simple" onChange="setfkly('%{#inner.id}','%{#inner.INSTANCEID}',this)" listKey="FKLY" listValue="FKLY" value="#inner.FKLY" headerKey="0" headerValue="请选择"></s:select></TD>
				</s:if>
				
					
				<s:if test="#inner.BBZT=='A3'">
					<TD ><a  href="javascript:backAudit(<s:property value="#inner.id"/>,,<s:property value="#inner.INSTANCEID"/>)">驳回</a></TD>
				</s:if>
				<s:else>
					<TD ></TD>
				</s:else>
				<TD ><a  href="javascript:showAuditInfo(${instanceid},<s:property value="#inner.id"/>)">查看</a></TD>
			</TR>
			</s:iterator>
			</s:iterator>
		</table>
		
	</div>
</body>
</html>
