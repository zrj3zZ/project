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
	<script>
	
	function openCreate(){
	     var pageUrl = 'iwork_contract_getdemidandformid.action';
	     $.post(pageUrl, null, function(json) {
		 var proInfo = JSON.parse(json);
		 if (proInfo!=null) {
		 	 var formid = proInfo.Formid;
		 	 var demid= proInfo.Demid;
		 	 var pactno = $("#PACTNO").val();
		 	 //var pacttitle = $("#PACTTITLE").val();
		     var target = "_blank";
		     var url="createFormInstance.action?formid="+formid+"&demId="+demid+"&FATHERPACTNO="+pactno;
			 var page = window.open(url,target,'width=1000,height=650,top=10,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	        }
	     });
	}
	
function selectcreate(){
	     var pageUrl = 'iwork_contract_getdemidandformid.action';
	     $.post(pageUrl, null, function(json) {
		 var proInfo = JSON.parse(json);
		 if (proInfo!=null) {
		 	 var formid = proInfo.Formid;
		 	 var demid= proInfo.Demid;
		 	 var instanceId = $("#instanceId").val();
		 	 var target = "_blank";
	         var pageUrl = "openFormPage.action?formid="+formid+"&instanceId="+instanceId+"&demId="+demid;
	         var page = window.open(pageUrl,target,'width=1000,height=650,top=10,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			/*art.dialog.open(pageUrl,{
			    	id:"planlistDlg",
			    	title:"基本信息修改",  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'90%',
				    height:'90%',
					close:function(){
					   location.reload();
					}
				 });*/
	        }
	     });
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
			/*background-color:#efefef;*/
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
		
             <div class="listCss">
             	<div class="tools_nav">
             		<div class="addItemBtn" onclick="javascript:selectcreate();" style="float:left;width:140px;height:10px;"><span>修改</span></div>
             		<s:if test="hashMap.PACTNATURE==1">
             			<div class="addItemBtn" onclick="javascript:openCreate();" style="float:left;width:120px;height:10px;"><span>添加子合同</span></div>
             		</s:if>
             	</div>
             	<div class="tableList">
             		<table width="100%">
           
			
			<tr>
				<td align="left">
					<table border="0" cellpadding="0" cellspacing="0" width="100%" class="ke-zeroborder">
						<tbody>
						    <tr>
				                <th  >
					                                           创建日期
				                </th>
				                <td class="" id="data_CREATENAME" width="35%">
									<s:property value="hashMap.CREATEDATE"/>
								</td>
			                </tr>
							<tr>
								<th >
									创建信息
								</th>
								<td class="" id="data_CREATENAME" width="35%">
									<span id="labelCREATEID"><s:property value="hashMap.CREATEID"/></span>[<span id="labelCREATENAME"><s:property value="hashMap.CREATENAME"/></span>]/<span id="labelCREATE_DEPT_NAME"><s:property value="hashMap.CREATE_DEPT_NAME"/></span>　
								</td>
							</tr>
							<tr>
								<th class="" id="title_PACTTITLE" width="15%">
									合同标题
								</th>
								<td class="" id="data_PACTTITLE" width="35%">
									<s:property value="hashMap.PACTTITLE"/><input type="hidden" id="PACTTITLE" value="<s:property value="hashMap.PACTTITLE"/>"/>
								</td>
								<th class="" id="title_PACTNO" width="15%">
									合同编号
								</th>
								<td class="" id="data_PACTNO" width="35%">
								    <s:property value="hashMap.PACTNO"/><input type="hidden" id="PACTNO" value="<s:property value="hashMap.PACTNO"/>"/>
								</td>
							</tr>
							<tr>
								<th class="" id="title_PACTNATURE" width="15%">
									合同属性
								</th>
								<td class="" id="data_PACTNATURE" width="35%">
									<s:if test="hashMap.PACTNATURE==1">主合同</s:if>
									<s:if test="hashMap.PACTNATURE==2">子合同</s:if>
								</td>
								<s:if test="hashMap.PACTNATURE==2">
								<th class="" id="PACTNO1" width="15%" >
									主合同编号
								</th>
								<td class="" id="PACTNO2" width="35%" >
									<s:property value="hashMap.FATHERPACTNO"/>
								</td>
								</s:if>
							</tr>
							<tr>
								<th class="" id="title_PACTTYPE" width="15%">
									合同类别
								</th>
								<td class="" id="data_PACTTYPE" width="35%">
									<s:if test="hashMap.PACTTYPE==1">收款合同</s:if>
									<s:if test="hashMap.PACTTYPE==2">付款合同</s:if>
								</td>
							</tr>
							<tr>
								<th class="" id="title_PACTOWNER" width="15%">
									合同甲方
								</th>
								<td class="" id="data_PACTOWNER" width="35%">
									<s:property value="hashMap.PACTOWNER"/>
								</td>
							</tr>
							<tr>
								<th class="" id="title_PACTPARTY" width="15%">
									合同乙方
								</th>
								<td class="" id="data_PACTPARTY" width="35%">
									<s:property value="hashMap.PACTPARTY"/>
								</td>
							</tr>
							<tr>
								<th class="" id="title_PACTSUN" width="15%">
									合同总金额
								</th>
								<td class="" id="data_PACTSUN" width="35%">
									<s:property value="hashMap.PACTSUN"/>
								</td>
								<th class="" id="title_CURRENCY" width="15%">
									币种
								</th>
								<td class="" id="data_CURRENCY" width="35%">
									<s:if test="hashMap.CURRENCY==1">人民币</s:if>
									<s:if test="hashMap.CURRENCY==2">美元</s:if>
									<s:if test="hashMap.CURRENCY==3">英镑</s:if>
									<s:if test="hashMap.CURRENCY==4">港币</s:if>
									<s:if test="hashMap.CURRENCY==5">欧元</s:if>

&nbsp;　&nbsp;汇率&nbsp;　  <s:property value="hashMap.PARITIES"/>
								</td>
							</tr>
							<tr id="itemTr_4">
								<th class="" id="title_SAYTOTAL" width="15%">
									合同金额大写
								</th>
								<td class="" id="data_SAYTOTAL" width="35%">
									<s:property value="hashMap.SAYTOTAL"/>
								</td>
								<th class="" id="title_CONVERT" width="15%">
									折算人民币金额
								</th>
								<td class="" id="data_CONVERT" width="35%">
									<s:property value="hashMap.CONVERT"/> 元
								</td>
							</tr>
							<tr id="itemTr_16">
								<th class="" id="title_CONTRACTDATE" width="15%">
									合同开始日期
								</th>
								<td class="" id="data_CONTRACTDATE" width="35%">
									<s:property value="hashMap.CONTRACTDATE"/>
								</td>
								<th class="" id="title_FROMDATE" width="15%">
									合同结束日期
								</th>
								<td class="" id="data_FROMDATE" width="35%">
									<s:property value="hashMap.FROMDATE"/>
								</td>
							</tr>
							<tr id="itemTr_18">
								<th class="" id="title_STATE" width="15%">
									合同说明
								</th>
								<td class="" id="data_STATE" width="35%">
									<s:property value="hashMap.STATE"/>
								</td>
							</tr>
							<tr>
							    <th class="" id="title_ATTACHMENT" width="15%"> 
							    	附件
							    </th>
								<td>
									<s:property value="hashMap.ATTACHMENT" escapeHtml="false"/>
								</td>
						    </tr>
						<input name="instanceId" type="hidden" id="instanceId" value="<s:property value="hashMap.instanceId"/>"/>
             		</table>
             		
             	</div>
             </div>
           
            
</body>
</html>
