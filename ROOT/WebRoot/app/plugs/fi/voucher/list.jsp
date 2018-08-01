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
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	 <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script>
	$(function(){
	$('.itemTr').click(function(obj){
		var id = $(this).attr("id");
		$("#item_"+id).attr("checked",'true');
	});
		$('.tablist li').click(function(obj){
			 $(".tablist li").each(function(item){
				    $(this).removeClass("current");
			  });
			 $(this).addClass("current");
	    });
	    trace();
	});
	function allPrpos ( obj ) { 
	  // 用来保存所有的属性名称和值 
	  var props = "" ; 
	  // 开始遍历 
	  for ( var p in obj ){ // 方法 
	  if ( typeof ( obj [ p ]) == " function " ){ obj [ p ]() ; 
	  } else { // p 为属性名称，obj[p]为对应属性的值 
	  props += p + " = " + obj [ p ] + " /t " ; 
	  } } // 最后显示所有的属性 
	  alert ( props ) ;
	}
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
	         //获取合同编号
			 var pactNo = $("#pactNo").val();
			 //获取合同名称
             var pactName = $("#pactName").val();
             //获取当前所选的付款方式
             var sum= $("input[name='sum']:checked").val();
             // alert(sum);
             //获取当前所选的合同
             var eventContract= $("input[name='radiobutton']:checked").val();
             //alert(eventContract);
             //获取合同属性的值
             var ContractProperties= $("#sel").val();            
             //获取合同性质的值
             var contractNature= $("#sele").val();            
		     window.location.href="iwork_contract_query.action?pactNo="+pactNo+"&pactName="+pactName+"&sum="+sum+"&eventContract="+eventContract+"&ContractProperties="+ContractProperties+"&contractNature="+contractNature+"&year="+year;   
    }

	 //查看基本信息或者编辑数据
		function openItem(instanceid){
			//alert('');
			var demId = $("#demId").val();
			var formId = $("#formId").val();
			var pageUrl = "openFormPage.action?formid="+formId+"&instanceId="+instanceid+"&demId="+demId;
			//alert(pageUrl);
			 art.dialog.open(pageUrl,{
			    	id:"planlistDlg",
			    	title:"凭证", 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:1000,
				    height:650,
				   close:function(){
						location.reload();
					}
				 });
		}
	    function validation(){	   
	      
	      var c= $("input[name='sum']:checked").val();      
	      var d= $("input[name='radiobutton']:checked").val();		           	      
		    if(c==undefined||d==undefined){
			alert("请选择合同类别");
			return false;
		}else{
		   doSearch();
		}
	}
	
	
	$(function(){
		$("#pp").pagination({
			    total:<s:property value="numSize"/>, 
			    pageNumber:1,
			    pageSize:15,
			    onSelectPage:function(pageNumber, pageSize){
			    	var pageUrl="iwork_fi_voucherList.action?pageSize="+pageSize+"&pageNumber="+pageNumber;
					window.location.href = pageUrl;
				}
			});
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
			height:25px;
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
			cursor: pointer;
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
<body class="easyui-layout">
		 <div region="north" border="false"  style="overflow:hidden;" > 
		      <div class="tools_nav">
		 		 <a  href="#" style="margin-left:1px;margin-right:1px"  onclick='saveForm()' text='Ctrl+s' class="easyui-linkbutton" plain="true" iconCls="icon-process-trancation">执行过账</a>
		 	 </div>
		 	 <form name='ifrmMain' id='ifrmMain' method="post" >
			           <fieldset width="100%">
			            <legend>查询</legend>    
			            		<table width="100%">
			            			<tr>
			            				<td>记账日期从</td>
			            				<td><input type='text' onfocus="WdatePicker()" class = "{required:false}"  style="width:100px" name='QJQSRQ' id='QJQSRQ'  value='' >&nbsp;到&nbsp; <input type='text' onfocus="WdatePicker()" class = "{required:false}" onchange="getQJGJXSS()" style="width:100px" name='QJJSRQ' id='QJJSRQ'  value='' ></td>
			            				<td>凭证编号</td>
			            				<td><input type="text" id="VoucherNo" value=""/> </td>
			            				<td>记账用户</td>
			            				<td><input type="text" id="userId" value=""/> </td>         				
			            			</tr>
			            			<tr>
			            				
			            				<td>状态</td>
			            				<td>
			                               <input type="radio" name="radiobutton" id="radiobutton1" value="1"/><label for="radiobutton1">已过账</label>
			<input type="radio" name="radiobutton" id="radiobutton2" value="2"/><label for="radiobutton2">未过账</label>
			            			    </td>
			            			    <td>单据编号</td>
			            			    <td>
			            			  <input type="text" id="pageno" value=""/>
			                            </td>
			                            <td>ERP凭证编号</td>
			            			    <td>
			            			    <input type="text" id="erp_voucher" value=""/>
			                            </td>
			            			 <td valign='bottom' style='padding-bottom:5px;'> <a id="btnEp" class="easyui-linkbutton" icon="icon-search" href="javascript:validation();" >查询</a></td> </tr>
			                </table>         
			       </fieldset>
			</form>	
		</div>
<div region="center" border="false" 	tyle="background: #fff; border-top:1px solid #efefef;">
             <div class="listCss">
             	<div class="tableList">
	                <table width="100%">
             			<tr>            		 		
            		      <th style="width:2%"><input type="checkbox" name="checkbox" value="checkbox1"></th>
	            	      <th style="width:15%">记账日期</th>	            	      
	            	      <th style="width:15%">记账用户</th>
	            	      <th style="width:15%">单据编号</th>
	            	      <th style="width:15%">凭证编号</th>
	            	      <th style="width:15%">ERP凭证编号</th>
	            	       <th style="width:15%">操作</th>	   
	            	      <th style="width:15%">状态</th>
	            	               	      
            	        </tr>           			
             		    <s:iterator value="list" id="column" status="u">
             		       <tr class="itemTr" id="<s:property value="INSTANCEID"/>">     
             		    	 <td ><input type="checkbox" name="ITEM" value="<s:property value="INSTANCEID"/>"  id="item_<s:property value="INSTANCEID"/>"/></td>
             		    	 <td ><s:property value="VDATE"/></td>
             		    	 <td><s:property value="USERID"/></td>
             		    	 <td><s:property value="VNO"/></td>
             		    	 <td><s:property value="ERPNO"/></td>
             		    	 <td>&nbsp;</td>
             		    	 <td><a href="javascript:openItem(<s:property value="INSTANCEID"/>);">编辑</a></td>
             		    	 <td>&nbsp;</td>
             		    	</tr>    
             		    </s:iterator>       			
             		</table>
             </div>
             </div>
             <s:hidden name="demId" id="demId"></s:hidden>
             <s:hidden name="formId" id="formId"></s:hidden>
	</div>
	
         <div region="south" border="false" style="height: 32px;">
			<div id="pp" style="background: url('../iwork_img/engine/tools_nav_bg.jpg') repeat-x; border: 1px solid #ccc;"></div>
  		 </div> 
</body>
</html>
