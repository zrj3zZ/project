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
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style>
		.topHead{
			height:40px;
			background:#fff;
			border-bottom:3px solid #677e9b;
		}
		.maintitle{
			font-size:25px;
			font-family:黑体;
			font-werght:bold;
			padding:8px;
			color:#304d79;
		}
		.maintitle span{
			font-size:26px;
			color:#304d79;
			padding:2px;
		}
		.maintitle span span{
			color:red;
		}
		.tablist{
			list-style-type:none;
			 padding:0px;
			 margin-left:10px;
			 margin-top:5px;
			  border-top:1px solid #ccc;
		}
		.tablist li{
			 line-height:30px;
			 border-bottom:1px solid #ccc;
			 border-right:1px solid #ccc;
			 border-left:1px solid #ccc;
			 padding-left:15px;
			 font-family:微软雅黑;
			  background:#f7f7f7;
			  color:#666;
			  font-weight:bold;
		}
		.tablist li:hover{
			   background:#fff;
			   color:#333;
			 cursor:pointer;
		}
		.tablist .current{
			  background:#fff;
			   color:#333;
			 border-right:1px solid #fff;
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
		}
		.addItemBtn:hover span{
			border:1px solid #efefef;
			color:red;
		}
	</style>
	<script>
	$(function(){
		//setYear();
		 var year=new Date().getFullYear(); 
		$("select").append("<option value="+(year-4)+">"+(year-4)+"</option><option value="+(year-3)+">"+(year-3)+"</option><option value="+(year-2)+">"+(year-2)+"</option><option value="+(year-1)+">"+(year-1)+"</option><option value="+year+">"+year+"</option><option value="+(year+1)+">"+(year+1)+"</option>");
		$('.tablist li').click(function(obj){
			 $(".tablist li").each(function(item){
				    $(this).removeClass("current");
			  });
			 $(this).addClass("current");
			  var label = $(this).attr('id');
			 if(label=='contract_list'){
				var year = $('select').val();
				$("#ifm").attr("src","iwork_contract_ContractList.action?year="+year);
			 }else if(label=='query_list'){
			    // setYear();
			     var year = $('select').val();
			    $("#ifm").attr("src","iwork_contract_queryList.action?year="+year);
			 }else if(label=='query_customer'){//按客户统计查询
			     var year = $('select').val();
			    $("#ifm").attr("src","iwork_customer_queryList.action?year="+year);
			 }else if(label=='query_supplier'){//按供应商统计查询
			     var year = $('select').val();
			    $("#ifm").attr("src","iwork_supplier_queryList.action?year="+year);
			 }else if(label=='query_receivable'){//应收账款统计查询
			     var year = $('select').val();
			    $("#ifm").attr("src","iwork_query_receivableList.action?year="+year);
			 }else if(label=='query_pay'){//应付账款统计查询
			     var year = $('select').val();
			    $("#ifm").attr("src","iwork_query_payList.action?year="+year);
			 } else if(label=='options'){
			 	$("#ifm").attr("src","iwork_contract_options.action");
			 }
	    });
	});
	/*function setYear(){
		var myDate = new Date();
		var myDateYear = myDate.getFullYear();
		if(myDateYear!=''&&myDateYear=='2015'){
			$('#CURRENCY')[0].selectedIndex = 6;
		}else if(myDateYear!=null&&myDateYear=='2016'){
			$('#CURRENCY')[0].selectedIndex = 2;
		}else if(myDateYear!=null&&myDateYear=='2017'){
			$('#CURRENCY')[0].selectedIndex = 3;
		}else if(myDateYear!=null&&myDateYear=='2018'){
			$('#CURRENCY')[0].selectedIndex = 4;
		}else if(myDateYear!=null&&myDateYear=='2019'){
			$('#CURRENCY')[0].selectedIndex = 5;
		}
	}*/
	function openCreate(){
	     var pageUrl = 'iwork_contract_getdemidandformid.action';
	     $.post(pageUrl, null, function(json) {
		 var proInfo = JSON.parse(json);
		 if (proInfo!=null) {
		 	 var formid = proInfo.Formid;
		 	 var demid= proInfo.Demid;
		     var target = "_blank";
			 var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid;
			 art.dialog.open(pageUrl,{
			    	id:"planlistDlg",
			    	title:"新建合同", 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'90%',
				    height:'90%',
					close:function(){
						location.reload();
					}
				 });
	        }
	     });
	}
	function doSearch(){
		var itemno = $("#itemno").val();
		var itemname = $("#itemname").val();
		 var year = $('select').val();
		$("#ifm").attr("src","iwork_contract_ContractList.action?year="+year+"&itemno="+itemno+"&itemname="+itemname);
	}
	function yearselectlist(){
	  var year = $('select').val();
		$("#ifm").attr("src","iwork_contract_ContractList.action?year="+year);
		$(".tablist li").removeClass("current");
		$(".tablist li:nth-child(1)").addClass("current");
	}
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false"  style="overflow:hidden;" > 
		 	<div class="topHead">
		 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		 			<tr>
		 				<td>
		 					<div class="maintitle">合同管理</div>
		 				</td>
		 				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		 				<td style="text-align:right">
		 				年份：
			 				<select  onchange="yearselectlist();" name='CURRENCY'  id='CURRENCY' class=required  >
			 				   <option value=''>-空-</option>
	                        </select>
		 				</td>
		 				<td style="text-align:right;padding-right:10px;">
		 					<div style="float:right">
		 					<span class="search_btn" id="search_btn">
		 					合同编号：<input  type="text" name="itemno" id="itemno"  />
							 合同名称： <input  type="text" name="itemname" id="itemname" />
							  <input  onclick="doSearch();" type="button" class="search_button" value="查询"/> 
							  </span>
							</div>
		 				</td>
		 			</tr>
		 		</table>
		 	</div>
        </div>
		 <div region="west"  class="leftDiv" border="false" style="border-right:1px solid #fff;background-color:#efefef;width:200px;padding-top:10px;">
		 		<div class="addItemBtn" onclick="javascript:openCreate();" ><span>新建合同</span></div>
		 		<ul class="tablist">
		 			<li class="current" id="contract_list">合同首页</li>
		 			<li id="query_list">合同查询</li>
		 			<li id="query_customer">按客户统计查询</li>
		 			<li id="query_supplier">按供应商统计查询</li>
		 			<li id="query_receivable">应收账款统计查询</li>
		 			<li id="query_pay">应付账款统计查询</li>
		 			<s:if test="hashMap.IS_SUB==1">
		 			<li id="options">设置</li>
		 			</s:if>
		 		</ul>
        </div>  
            <div region="center" border="false" s	tyle="background: #fff; border-top:1px solid #efefef;">
            	 <iframe width='100%' height='99%' src ="iwork_contract_ContractList.action?" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="ifm" name="ifm"\>
            </div>
            <s:hidden id="recNum" name="recNum"></s:hidden>
            <s:hidden id="draftNum" name="draftNum"></s:hidden>
            <s:hidden id="issupport" name="issupport"></s:hidden>
</body>
</html>
