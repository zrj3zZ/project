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
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript"
    src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
	
	<script>
	
	$(function(){
		
		$('.tablist li').click(function(obj){
			 $(".tablist li").each(function(item){
				    $(this).removeClass("current");
			  });
			 $(this).addClass("current");
	    });
	    trace();
	});
	function trace(){
	      //var pageUrl="iwork_contract_ContractList.action";
					//window.location.href = pageUrl;
	}
	function expand(id){
		$("#expandBtn"+id).hide();
		$("#unExpandBtn"+id).show();
		var pageurl ="iwork_contract_showsublist.action";
		var submitdata = {itemno:id};
		  $.post(pageurl,submitdata,function(data)
		  {
			   var item = $("#subItem"+id).show();
				$("#subItem"+id).find("td").html(data);
		   });  
		
	} 
	function unExpand(id){
			    	$("#subItem"+id).hide();
			    	$("#expandBtn"+id).show();
			    	$("#unExpandBtn"+id).hide();
	}
	function doSearch(){ 
	       //获取合同年份
	         var year = $("#year").val();
	         //获取合同甲方
			 var pactParty = $("#pactParty").val();
			 //获取合同编号
             var partyNo = $("#partyNo").val();
             //获取合同签订日期
              var contrctDate = $("#contrctDate").val();
             //获取合同截止日期
              var fromDate = $("#fromDate").val();
		     window.location.href="iwork_supplier_query.action?pactParty="+pactParty+"&partyNo="+partyNo+"&year="+year+"&contrctDate="+contrctDate+"&fromDate="+fromDate;    
    }
	 //查看基本信息或者编辑数据
		function openPlanListItem(instanceid){
			//alert('');
			var demId = $("#demId").val();
			var formId = $("#formId").val();
			var pageUrl = "openFormPage.action?formid="+formId+"&instanceId="+instanceid+"&demId="+demId;
			//alert(pageUrl);
			art.dialog.open(pageUrl,{ 
				   id:"planlistDlg",
					cover:true, 
					title:"合同执行",  
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					width:600,
					cache:false, 
					lock: true,
					esc: true,
					height:550,
					iconTitle:false,  
					extendDrag:true,
					autoSize:true,
					close:function(){
					   location.reload();
					}
					
				});
		}
	
	$(document).ready(function(){  
	     $("input[name='sum']").get(0).checked=true;  
       //$("input[@type=radio][name=sum][@value=1]").attr("checked",true); 
         $("input[name='radiobutton']").get(0).checked=true;
       //$("input[@type=radio][name=radiobutton][@value=1]").attr("checked",true);  
});  
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
			font-size:14px;
			 font-family:微软雅黑;
			 cursor:pointer;
		}
		.addItemBtn span{
			background-image:url(iwork_img/add_obj.gif);
			background-repeat:no-repeat;
			background-position:20px 5px;
			padding:5px;
			padding-left:40px;
			text-align:left;
		}
		.addItemBtn:hover span{
			border:1px solid #efefef;
			color:red;
		}
	  
	</style>
</head>
<body >
<form name='ifrmMain' id='ifrmMain' method="post" >
<div class="topCss">
           <fieldset width="100%">
            <legend>查询</legend>    
            		<table width="100%">
            			<tr>
            				<td>供应商编号</td>
            				<td><input type="text" id="partyNo" value="<s:property value="partyNo"/>"/></td>  
            				<td>供应商名称</td>
           			        <td><input type="text" id="pactParty" value="<s:property value="pactParty"/>"/> </td> 
           			    </tr> 
           			    <tr> 
           			        <td>开始日期</td>
           			       <td><input id="contrctDate" class="Wdate" name="contrctDate" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'fromDate\')||\'2020-10-01\'}'})"/></td>
           			        <td>结束日期</td>
           			        <td><input id="fromDate" class="Wdate" name="fromDate"  type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'contrctDate\')}',maxDate:'2020-10-01'})"/></td>
            			 <td valign='bottom' style='padding-bottom:5px;'> <a id="btnEp" class="easyui-linkbutton" icon="icon-search" href="javascript: doSearch();" >查询</a></td> </tr>
                </table>         
       </fieldset>
</form>		         
             <div class="listCss">
             	<div class="tableList">
	                <table width="100%">
             			<tr>            		 		
            		      <th style="width:2%"></th>
	            	      <th style="width:15%">供应商编号</th>	            	      
	            	      <th style="width:20%">供应商名称</th>
	            	      <th style="width:10%">已签合同数量</th>
	            	      <th style="width:10%">合同总金额</th>
	            	      <th style="width:10%">已付款金额</th>
	            	      <th style="width:10%">未付款金额</th>
            	        </tr>           			
             		<s:iterator value="list">            			
             		 <tr>
             			<td><a id="expandBtn<s:property value="PACTNO"/>" href="#" onclick="expand('<s:property value="PACTNO"/>')">
	            		</a>
	            		<a id="unExpandBtn<s:property value="PACTNO"/>" style="display:none" href="#" onclick="unExpand('<s:property value="PACTNO"/>')">
	            		<img src="iwork_img/unExpand.gif" border="0"></a></td>
	            		<td><s:property value="PARTYNO"/></td>           		
	            		<td><s:property value="PACTPARTY"/></td>	
	            		<td><s:property value="COUNTS"/></td>
	            		<td><s:property value="PACTSUNS"/></td>
	            		<td><s:property value="PAYAMOUNT"/></td>
	            		<td><s:property value="NOPAY"/></td>	            		
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
            <s:hidden name="year" id="year"></s:hidden>
            </s:form>
           <div region="south" border="false" style="height: 32px;">
			<div id="pp"
				style="background: url('../iwork_img/engine/tools_nav_bg.jpg') repeat-x; border: 1px solid #ccc;"></div>
		   </div> 
</body>
</html>
