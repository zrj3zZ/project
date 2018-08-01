<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_js/test/demo.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
 	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default&self=true"></script>
 	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>  
 	<style>
	table.altrowstable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}
table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
.oddrowcolor{
	background-color:#d4e3e5;
}
.evenrowcolor{
	background-color:#c3dde0;
}
</style>
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
<script type="text/javascript">		
		var ndid= ${ndid};
		var dqnd= ${dqnd};			
		function addHt(xmbh){
	        var instanceid = $("#instanceid").val();
	    	var pageUrl = "ndzb_showProjectInfo_action.action?instanceid="+instanceid+"&xmbh="+xmbh+"&ndid="+ndid;
	    	art.dialog.open(pageUrl,{
					id:'Category_show',
					cover:true,
					title:'添加合同项目信息',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:680,
					cache:false,
					lock: true,
					height:480, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					close:function(){
							window.location.reload();
						}
				});
				dg.ShowDialog();					    	
	    }
		//var selectedNodes;
		$(function(){ 
			
		})
		window.onload=function(){
		
	      sum();
	     }
	     function sum(){
			//alert("1oad");
			var sum = 0 ;
			var checkeds_fkzb = $("input[id*='FKZB']");
			for(var i=0;i<checkeds_fkzb.length;i++){
				
				sum = sum + checkeds_fkzb[i].value*1;
			}
			$("#subNumTd").text(sum+"元");
		}
		function showht(htid){
		   			    
			//var pageUrl = 'xhht_loadHtInfo_action.action?htid='+htid;
			var url = 'xhht_loadHtInfo_action.action?htid='+htid;
			var target = "dem"+htid;

			var win_width = window.screen.width;
			var page = window.open(url,target,'width='+win_width+',height=800,top=10,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = url;
				
		}				
		// 删除项目
		//delete_Xhhtzjjhdsub_action.action?rowid="+row.id+"&instanceid="+instanceid
		function remove(id){
			var instanceid = $("#instanceid").val();
			var pageUrl="delete_htndmx_action.action";
	        $.post(pageUrl,{rowid:id,instanceid:instanceid},function(data){
		        if(data=='success'){
		            window.location.reload();
		        }else{
		         	alert("操作异常");
		        }
		    });
		}
		
		// 更新本月预算值
		function setFKZB(xmid,id,obj){
			var fkzb = obj.value;
		         
			// 判断是否是数字
			if(isNaN(fkzb)){
				alert("请输入数字!");
			  	obj.value = "";
			  	$("#"+obj.id).focus();
			  	return;
			}
			// 判断不能是负数
			if(fkzb<=0){
				alert("请输入正确的数字!");
			  	obj.value = "";
			  	$("#"+obj.id).focus();
			  	return;
			}
			// 判断不能大于年度付款指标
			 
			var num = fkzb;
			var instanceid = $("#instanceid").val();
			var vaUrl="ndzb_validateData_action.action";
			var vaflag = false;
	        $.get(vaUrl,{rowid:id,xmid:xmid,fkzb:num,dqnd:dqnd,ndid:ndid},function(data){
	       		
		    	if(data=='false'){		        	
			  		vaflag = true;
			  		alert("预算超过项目年度支出指标！");
			  		obj.value = "0";
			  	    $("#"+obj.id).focus();
		        }else{
		        	saveFKZB(xmid,id,obj);
		        }
		    });
		    		    													
		}
		
		
		function saveFKZB(xmid,id,obj){
			var fkzb = obj.value;
		    var num = fkzb;
			var instanceid = $("#instanceid").val();
			var pageUrl="edit_htndmx_action.action";
	        $.post(pageUrl,{rowid:id,instanceid:instanceid,fkzb:num},function(data){
	       		if(data=='ok'){
	       			window.location.reload();
	       		}
		    	if(data=='error'){
		        	alert("操作异常");
		        }
		    });
		        
		    sum();
		}
		
		
		// 付款理由
		function setfkly(id,obj){
			var fkly = obj.value;
			var instanceid = $("#instanceid").val();
			var pageUrl="xhht_fklyXhhtMx_action.action";
	        $.post(pageUrl,{rowid:id,instanceid:instanceid,fkly:fkly},function(data){
	       		 if(data=='success'){
		             // window.location.reload();
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
		
		 function showProInfo(xmid){
		 				
			var pageUrl="ndzb_loadProData_action.action";
	        $.post(pageUrl,{ndid:ndid,xmid:xmid},function(data){
	       		
	       		var proInfo = JSON.parse(data);
	       		$("#subNumTd").text(proInfo.NDFKZB+"元");
	       		$("#PRO_NDZCZB").text(proInfo.NDZCZB+"元");
	       		$("#PRO_HTZH").text(proInfo.HTZH+"元");
	       		$("#PRO_SNDZX").text(proInfo.SNDZX+"元");
	       		$("#PRO_BNDYZX").text(proInfo.BNDYZX+"元");
	       		$("#PRO_XHRC").text(proInfo.XHRC+"元");
	       		//$("#subNumTd").text(data.subNumTd+"万元");
		    });
		 
		 }
</script>
<body>
	<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;height:90px;">
		<div class="tools_nav">
			<div style="text-align:left;padding-right:10px;">				
				<!--  <a href="###" onclick="addHt()" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>	-->															 
			</div>
		 </div>
		  <div style="text-align:left;padding-right:10px;">
		 	<table width="90%" style="text-align:left;padding-right:10px;" >
		 		<tr>
		 				
		 			<td  class="td_title" >当前年度:</td>
		 			<td  class="td_data" >
		 				<s:property value="ndhtzb.ndcontent"/>年
		 			</td>		 			
		 			<td class="td_title">合同总额:</td>
		 			<td class="td_data" id="PRO_HTZH"></td>
		 			<td class="td_title" >年度支出指标:</td>
		 			<td class="td_data" id='PRO_NDZCZB'></td>
		 			<td class="td_title" >合同付款指标:</td>
		 			<td class="td_data" id='subNumTd'></td>
		 		</tr>
		 		<tr>
			 			<td class="td_title">截止上年度已执行:</td>
			 			<td class="td_data" id="PRO_SNDZX"></td>
			 			<td class="td_title">本年度已执行:</td> 
			 			<td class="td_data" id="PRO_BNDYZX"></td>
			 			<td class="td_title" >型号日常费用:</td>
		 				<td class="td_data" id="PRO_XHRC" ></td>
			 			
			 			
		 		</tr>
		 	</table>
		 </div>
		 
		<div style="margin:10px 0;">
			
		
		</div>
	</div>
	
	<!-- 页中部 -->
	<div region="center" style="padding:10px;border:0px;">
	
		<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<td></td>
				<TD style="width:100px">名称</TD>
				<TD style="width:100px">编号</TD>
				<TD style="width:100px">责任部门</TD>
				<TD style="width:100px">乙方名称</TD>
				<TD style="width:100px">年度成本支出指标</TD>
				<TD style="width:100px">合同总额</TD>
				<TD style="width:90px">截止上年底已执行</TD>
				<td style="width:60px">本年已执行</td>
				<td style="width:80px">年度付款指标</td>				
				<td style="width:60px">备注</td>
				<TD style="width:40px">操作</TD>
				
			</TR>
			<s:iterator value="list"  status="status">
				<TR class="cellTop" >													
				<td><input type='radio' name='rad_1' id='rad_1' onclick="showProInfo('<s:property value="XMBH"/>')"></td>
				<TD><s:property value="XMMC"/></TD>
				<TD><s:property value="XMBH"/></TD>
				<TD><s:property value="ZRBM"/></TD>				
				<TD><s:property value="YFDW"/></TD>
				<TD><s:property value="NDZCZB"/></TD>
				<TD><s:property value="HTZH"/></TD>
				<TD><s:property value="SNDZX"/></TD>
				<TD><s:property value="BNDYZX"/></TD>
				<TD><s:property value="NDFKZB"/></TD>								
				
				<TD><s:property value="BZ"/></TD>
				<TD><a  href="javascript:addHt('<s:property value="XMBH"/>')">添加</a></TD>				
			</TR>
			
			<s:iterator value="children"  id="inner" >
				<TR class="cell">
				<td></td>
				<TD style="text-align:left;padding-left:20px;">				
				<img src="iwork_img/arrow.png" alt="" />				
				<a href="###" onclick="showht('<s:property value="#inner.HTNUMBER"/>')"><s:property value="#inner.NAME"/></a></TD>								
				<TD><s:property value="#inner.HTNUMBER"/></TD>
				<TD><s:property value="#inner.ZRBM"/></TD>				
				<TD><s:property value="#inner.YFDW"/></TD>
				<TD><s:property value="#inner.NDZCZB"/></TD>
				<TD><s:property value="#inner.HTZH"/></TD>
				<TD><s:property value="#inner.SNDZX"/></TD>
				
				<TD><s:property value="#inner.BNDYZX"/></TD>
				<TD ><input type="text" name="FKZB" onChange="setFKZB('<s:property value="XMBH"/>',<s:property value="#inner.id"/>,this)" id="FKZB_<s:property value="#inner.id"/>" style="width:58px" value="<s:property value="#inner.BNDFKZB"/>"/></TD>								
				
				<TD><s:property value="#inner.BZ"/></TD>				
				<TD ><a  href="javascript:remove(<s:property value="#inner.id"/>)">删除</a></TD>
				
			</TR>
			</s:iterator>
			</s:iterator>
		</table>
	
	</div>				
	<s:hidden name="instanceid" id="instanceid"></s:hidden>
	<s:hidden name="ndid" id="ndid"></s:hidden>
	<s:hidden name="dqnd" id="dqnd"></s:hidden>
	</body>
</html>
