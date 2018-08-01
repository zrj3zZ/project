<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script>
	//查看执行详细信息或者编辑数据
		function openPlanListItem(instanceid){
			//alert('');
			var demId = $("#demId").val();
			var formId = $("#formId").val();
			var pageUrl = "openFormPage.action?formid="+formId+"&instanceId="+instanceid+"&demId="+demId;
			   art.dialog.open(pageUrl,{
					 id:"planlistDlg",
					title:"合同执行", 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:700,
				    height:550,
					close:function(){
					   location.reload();
					}
				 });
		}
		//添加执行情况
		function addPlan(){
		var demId = $("#demId").val();
		var formId = $("#formId").val();
		var formId = $("#formId").val();
		var itemno=$("#itemno").val();
		var itemname=$("#itemname").val();
		var pageUrl = "createFormInstance.action?formid="+formId+"&demId="+demId+"&PACTNO="+itemno+"&PACTNAME="+itemname;
		  art.dialog.open(pageUrl,{
					 id:"planlistDlg",
					title:"添加合同执行情况", 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:800,
				    height:650,
					close:function(){
					   location.reload();
					}
				 });
		}
		
		   // 全选、全清功能
	   function selectAll() {
		    if ($("input[name='ckb_selectAll']").attr("checked")) {
			      $("input[name='ckb_selectAll']").attr("checked", true);
		      } else {
			      $("input[name='ckb_selectAll']").attr("checked", false);
		   }
	   }
		//删除操作
		function delcfm() {
        if (confirm("确认要删除？")) {
            cleanContract();
        
        }
    }
    //彻底删除记录
		function cleanContract(){		
		  //获取选中单选框的id值
			var counts = document.getElementsByName('ckb_selectAll');				
			var ids = "";
			if(counts.length>1){
				for(var i=0;i<counts.length;i++){
					if(counts[i].checked){
						ids = ids + counts[i].value + ",";
					}
				}
				if(ids!=""){
				//alert(ids);
				ids = ids.substring(0,ids.lastIndexOf(','));	
				var url ='iwork_contract_clean.action?ids='+ids;
		 			var option={
						type: "post",
						url: url,
						data: "",
						cache:false,	
						success: function(msg){
							if(msg=="succ"){
								alert('删除成功');
								window.location.reload();
							}else{
								alert('删除失败');
								return false;
							}
						}
			 	}
				$.ajax(option);
				}else{
				alert('请选择要删除的数据');
				return false;
				}
			}else{
				alert('请选择要删除的数据');
				return false;
			}
				
		}
    
	</script>
	<style>
		 .topCss{
			padding:5px;
		 }
		  .topCss table{
			border:1px solid #efefef;
		 }
		  .topCss tr{
			line-height:20px;
			padding:2px;
		 }
		 .topCss td{
			height:30px;
			padding:2px;
		 	padding-left:10px;
		 }
		
		 .tableList th{
			height:30px;
			padding:2px;
			padding-left:10px;
			background-color:#efefef;
			text-align:left;
		 }
		 .tableList tr{
			line-height:20px;
			padding:2px;
			border-bottom:1px solid #efefef;
		 }
		 .tableList tr:hover{
			background-color:#f6f6f6;
		 }
		 .tableList td{
			height:30px;
			padding:2px;
			padding-left:10px;
			text-align:left;
		 }
	     .addItemBtn{ 
			line-height:30px;
			text-align:left;
			padding-left:40px;
			font-size:13px;
			 font-family:微软雅黑;
			 cursor:pointer;
		}
		.addDeleBtn{ 
			line-height:30px;
			text-align:left;
			padding-left:40px;
			font-size:13px;
			 font-family:微软雅黑;
			 cursor:pointer;
		}
		.addDeleBtn span{
			background-image:url(iwork_img/but_delete.gif);
			background-repeat:no-repeat;
			background-position:20px 5px;
			padding:5px;
			padding-left:40px;
			text-align:left;
		}
		.addDeleBtn:hover span{
			border:2px solid #efefef;
			color:red;
		}
		.addItemBtn span{
			background-image:url(iwork_img/add_obj.gif);
			background-repeat:no-repeat;
			 background-position:20px 5px;
			padding:5px;
			padding-left:40px;
		}
		.addDeleBtn span{
			background-image:url(iwork_img/but_delete.gif);
			background-repeat:no-repeat;
			background-position:20px 5px;
			padding:5px;
			padding-left:50px;
			text-align:left;
		}
		.addItemBtn:hover span{
			border:1px solid #efefef;
			color:red;
		}
		.attachDiv{
			list-style-type:none;
		}
		.attachDiv li a{
			color:#0000ff;
		}
	
	</style>
</head>
<body >
			
            <div class="topCss">
            <fieldset>
                  <legend>信息预览</legend> 
            		<table width="100%">
            			<tr>
            				<td>合同总金额（<s:property value="baseinfo.currencyString"/>）:<s:property value="baseinfo.sumNum"/></td>
            				<td></td>
            				<td><s:property value="baseinfo.planCollectionString"/>（<s:property value="baseinfo.currencyString"/>）:<s:property value="baseinfo.planNum"/></td>
            				<td> </td>
            				<td><s:property value="baseinfo.invoicePlanCollectionString"/>（<s:property value="baseinfo.currencyString"/>）:<s:property value="baseinfo.ticketPlanNum"/></td>
            				<td></td>
            			</tr>
            			<tr>
            				<td></td>
            				<td></td>
            				<td><s:property value="baseinfo.actualCollectionString"/>（<s:property value="baseinfo.currencyString"/>）:<s:property value="baseinfo.realNum"/></td>
            				<td></td>
            				<td><s:property value="baseinfo.invoicePerformCollectionString"/>（<s:property value="baseinfo.currencyString"/>）:<s:property value="baseinfo.ticketNum"/></td>
            				<td></td>
            			</tr>
            			<tr>
            				<td></td>
            				<td></td>
            				<td><s:property value="baseinfo.shouldString"/>（<s:property value="baseinfo.currencyString"/>）:<s:property value="baseinfo.accountsNum"/></td>
            				<td></td>
            				<td><s:property value="baseinfo.InvoiceBalanceString"/>（<s:property value="baseinfo.currencyString"/>）:<s:property value="baseinfo.InvoiceBalance"/></td>
            				<td></td>
            			</tr>
            		</table>
            	 </fieldset>
            </div>
             <div class="listCss">
             	<div class="tools_nav">
             		<div class="addItemBtn" onclick="javascript:addPlan();" style="float:left;width:140px;height:10px;"><span>添加执行情况</span></div>
             		<div class="addDeleBtn" onclick="javascript:delcfm();" style="float:left;width:100px;height:10px;"><span>删除</span></div>
             	</div>
             	<div class="tableList">
             		<table width="100%">
             			<tr>
             			<th><input type="checkbox" id="ckb_selectAll" onclick="selectAll()"
							name="ckb_selectAll" value="0" title="选中/取消选中" /></th>
             				<th>款项名称</th>
             				<th><s:property value="baseinfo.actualCollectionString"/>（<s:property value="baseinfo.currencyString"/>）</th>
             				<th>收/付款日期</th>
             				<th>附件</th>
             				<th>备注</th>
             				<th>操作</th>
             			</tr>
             			<s:iterator value="list">
             				<tr>
             				<td>
							<input name="ckb_selectAll" type="checkbox"
								value="<s:property value="INSTANCEID"/>" title="选中/取消选中" /></td>
             				<td><s:property value="FUNDSNAME"/></td>
             				<td><s:property value="AMOUNT"/></td>
             				<td><s:property value="RECEIVEDATE"/></td>
             				<td><s:property value="ATTACHMENT" escapeHtml="false"/></td>
             				<td><s:property value="MEMO"/></td>
             				<td><a href="###" onclick="openPlanListItem(<s:property value="INSTANCEID"/>)">维护</a></td>
             			</tr>
             			</s:iterator>
             			
             		</table>
             		
             	</div>
             </div>
            <s:form name="editForm" theme="simple">
            <s:hidden name="baseinfo.demId" id="demId"></s:hidden>
            <s:hidden name="baseinfo.formId" id="formId"></s:hidden>
            <s:hidden name="itemname" id="itemname"></s:hidden>
            <s:hidden name="itemno" id="itemno"></s:hidden>
            </s:form>
            
</body>
</html>
<script>
	showSysTips();
</script>

