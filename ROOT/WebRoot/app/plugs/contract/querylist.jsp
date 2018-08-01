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
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	
	<script>							
	$(function(){
	   //var sum=<s:property value="sum"/>;	
	    // var sum= <s:property value="pageNumber"/>;
	   //  alert(sum);
		$("#pp").pagination({
			    total:<s:property value="total"/>, 
			    pageNumber:<s:property value="pageNumber"/>,
			   // sum:<s:property value="sum"/>;
			    //eventContract:<s:property value="eventContract"/>;
			    pageSize:<s:property value="pageSize"/>,
			    onSelectPage:function(pageNumber, pageSize){
			    	var pageUrl="iwork_contract_query.action?pageSize="+pageSize+"&pageNumber="+pageNumber+"&sum="+<s:property value="sum"/>+"&eventContract="+<s:property value="eventContract"/>+"&pactNo="+$("#pactNo").val()+"&pactName="+$("#pactName").val()
			    	                                            +"&ContractProperties="+$("#sel").val()+"&contractNature="+$("#sele").val();
					window.location.href = pageUrl;
				}
			});
		
	  });
	  	
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
	         //获取合同编号
			 var pactNo = $("#pactNo").val();
			 //获取合同名称
             var pactName = $("#pactName").val();
             //获取当前所选的付款方式
             var sum= $("input[name='sum']:checked").val();
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
            				<td>合同编号</td>
            				<td><input type="text" id="pactNo"  required="required" value="<s:property value="pactNo"/>"/> </td>
            				<td>合同名称</td>
            				<td><input type="text" id="pactName" oclick="javascript:pactNo();" required="required" value="<s:property value="pactName"/>"/> </td>
            				<td>合同类别</td>
            				<td>           				
                               <s:radio name="sum" id="sum"  list="#{'1':'收款','2':'付款'}" listKey="key" theme="simple" value="sum"></s:radio>                         
                            </td>            				
            			</tr>
            			<tr>
            				
            				<td>合同类别</td>            				
            				<td>
                               <s:radio name="radiobutton"  id="radiobutton" list="%{#{'1':'主合','2':'子合同'}}" theme="simple" value="eventContract"></s:radio>           				
            			    </td>         			    
            			   <td>合同属性</td>
            			    <td>
            			   <select id="sel" onChange="chg()">
            			    <s:if test="ContractProperties==''">
                              <option value="">-空-</option>
                              <option value="1">外协研制合同</option>
                            </s:if> 
                            <s:if test="ContractProperties==1">                            
                              <option value="1">外协研制合同</option>
                              <option value="">-空-</option>
                            </s:if> 
                            </select>
                            </td>
                            <td>合同性质</td>
            			    <td>
            			   <select id="sele" onChange="chg()">
            			      <s:if test="contractNature==''">
                              <option value="">-空-</option>
                              <option value="1">集团合同</option>
                              </s:if> 
                              <s:if test="contractNature==1">                            
                              <option value="1">集团合同</option>
                              <option value="">-空-</option>
                              </s:if>
                            </select>
                            </td>
            			 <td valign='bottom' style='padding-bottom:5px;'> <a id="btnEp" class="easyui-linkbutton" href="javascript:validation();" icon="icon-search" oclick="javascript:doSearch();" >查询</a></td> </tr>
                </table>         
       </fieldset>
</form>		
         
             <div class="listCss">
             	<div class="tableList">
	                <table width="100%">
             			<tr>            		 		
            		      <th style="width:2%"></th>
	            	      <th style="width:15%">合同编号</th>	            	      
	            	      <th style="width:30%">合同名称</th>
	            	      <th style="width:10%">合同类型</th>
	            	      <th style="width:10%">负责人</th>
	            	      <th style="width:10%">合同总金额</th>
	            	      <th style="width:10%">合同截止日期</th>
            	        </tr>           			
             		<s:iterator value="list">           		
             		 <tr>            		 
             			<td><a id="expandBtn<s:property value="PACTNO"/>" href="#" onclick="expand('<s:property value="PACTNO"/>')">
	            		</a>
	            		<a id="unExpandBtn<s:property value="PACTNO"/>" style="display:none" href="#" onclick="unExpand('<s:property value="PACTNO"/>')">
	            		<img src="iwork_img/unExpand.gif" border="0"></a></td>
	            		<td><a href="iwork_contract_basecontractquery.action?instanceId=<s:property value="INSTANCEID"/>&itemno=<s:property value="PACTNO"/>" target="_self"><s:property value="PACTNO"/></a></td>           		
	            		<td><s:property value="PACTTITLE"/></td>	
	            		<td><s:property value="CONTACTTYPE"/></td>
	            		<td><s:property value="ADMIN"/></td>
	            		<td><s:property value="PACTSUN"/></td>
	            		<td><s:property value="FROMDATE"/></td>
             		</tr>             		            		
             		</s:iterator>             		
             		</table>
             </div>
             </div>
             <s:if test='total==0'>          
             	<p style="display:block; width:100%; background-color:#f6f6f6; text-align:center; font-size:15px;">沒有符合查询条件的合同  </p>                 		
             </s:if>	
            <s:form name="editForm" theme="simple">
            <s:hidden name="baseinfo.demId" id="demId"></s:hidden>
            <s:hidden name="baseinfo.formId" id="formId"></s:hidden>
            <s:hidden name="itemname" id="itemname"></s:hidden>
            <s:hidden name="itemno" id="itemno"></s:hidden>
            </s:form>
           <div region="south" border="false" style="height: 32px;">
			<div id="pp"
				style="background: url('../iwork_img/engine/tools_nav_bg.jpg') repeat-x; border: 1px solid #ccc;"></div>
		</div> 
</body>
</html>
