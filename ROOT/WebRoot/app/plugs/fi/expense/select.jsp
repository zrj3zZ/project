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
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
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
	<style type="text/css">
		.form_title{  
			font-family:宋体;
			font-size:12px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
	</style>	
	<script type="text/javascript">
	var mainFormValidator;
	var api = frameElement.api, W = api.opener;	
	$().ready(function() {
			mainFormValidator =  $("#editForm").validate({
				debug:true
			 });
			 mainFormValidator.resetForm();
		});
 //==========================装载快捷键===============================//快捷键
 		jQuery(document).bind('keydown',function (evt){		
		   		
		}); //快捷键
		function doSubmit(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
					return;
			}
			var actProcDefId = $("#actDefId").val();
			var url = "processRuntimeStartInstance.action?actDefId=" + actProcDefId;
			openDialog(url);
		}
		//添加和编辑窗口
	function openDialog(url){
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
		function close(){
			api.close();
		} 
	</script>
</head>
<body class="easyui-layout">
		 
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <s:form name="editForm" id="editForm" action="sysDem_save.action"  theme="simple">
            		<table>
            			<tr id="itemTr_0">
								<td class="td_title" width="15%">
									申请人
								</td>
								<td class="td_data" colspan="3">
									超级管理员<input type='hidden' name='SQR'  id='SQR'  value='超级管理员' >&nbsp;[ADMIN<input type='hidden' name='SQRZH'  id='SQRZH'  value='ADMIN' >] /&nbsp;KS集团/KS信息技术部<input type='hidden' name='SQRBMMC'  id='SQRBMMC'  value='KS集团/KS信息技术部' >&nbsp;　
								</td>
							</tr>
							<tr id="itemTr_1">
								<td class="td_title" width="15%">
									报销人
								</td>
								<td class="td_data" width="35%">
									<input type='hidden'null name='BXRXM'  id='BXRXM'  value='' ><div style=" white-space:nowrap;vertical-align:bottom;"><input type='text' readonly  name='BXRZH' class = '{maxlength:32,required:false}'  id='BXRZH' value=''  style="width:100px;margin-right:5px;background-color:#efefef" ><a id='radioBtnhtml' href="###" title="单选地址薄"  onclick="radio_book('','','','','','','','','BXRZH');" class="easyui-linkbutton" plain="true" iconCls="icon-radiobook"></a><input type='hidden'null name='BXRYGBH'  id='BXRYGBH'  value='' >
								</td>
								<td class="td_title" width="15%">
									所属公司
								</td>
								<td class="td_data" width="35%">
									<input type='text' class = '{maxlength:64,required:false}'  style="width:100px" name='SSGSBM' id='SSGSBM'  value='' >
								</td>
							</tr>
							<tr id="itemTr_1">
								<td class="td_title" width="15%">
									费用类别
								</td>
								<td class="td_data" width="35%">
									<input type='hidden'null name='FYLBBM'  id='FYLBBM'  value='' ><input type='text' class = '{maxlength:32,required:false}'  style="width:100px" name='FYLBMC' id='FYLBMC'  value='' >
								</td>
								<td class="td_title" width="15%">
									报销部门
								</td>
								<td class="td_data" width="35%">
									<input type='hidden'null name='BXBMID'  id='BXBMID'  value='' ><input type='text' readonly  name='BXBM' ' id='BXBM' value=''  style="width:100px" ><a href="###" onclick="dept_book('','','','','','','','','BXBM');" title="部门地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-deptbook"></a><br />
								</td>
							</tr>
							<tr id="itemTr_2">
								<td class="td_title" width="15%">
									预算类型
								</td>
								<td class="td_data" colspan="3" width="35%">
									<input type=radio   id='YSLX0' name='YSLX' value='个人预算' checked="checked"><label  id="lbl_YSLX0"  for="YSLX0">个人预算</label>&nbsp;
<input type=radio  id='YSLX1' name='YSLX' value='部门预算' ><label  id="lbl_YSLX1" for="YSLX1">部门预算</label>&nbsp;

								</td>
							</tr>
							<tr id="itemTr_21">
								<td class="td_title" width="15%">
									结算方式
								</td>
								<td class="td_data" colspan="3" width="35%">
									<input type=radio  id='JSFSBM0' name='JSFSBM' value='现金' ><label  id="lbl_JSFSBM0" for="JSFSBM0">现金</label>&nbsp;
<input type=radio   id='JSFSBM1' name='JSFSBM' value='银行代发' checked="checked"><label  id="lbl_JSFSBM1"  for="JSFSBM1">银行代发</label>&nbsp;
<input type=radio  id='JSFSBM2' name='JSFSBM' value='电汇' ><label  id="lbl_JSFSBM2" for="JSFSBM2">电汇</label>&nbsp;
<input type=radio  id='JSFSBM3' name='JSFSBM' value='支票' ><label  id="lbl_JSFSBM3" for="JSFSBM3">支票</label>&nbsp;
<input type=radio  id='JSFSBM4' name='JSFSBM' value='等额冲账' ><label  id="lbl_JSFSBM4" for="JSFSBM4">等额冲账</label>&nbsp;
<input type='hidden'null name='JSFSMC'  id='JSFSMC'  value='' >
								</td>
							</tr>
							<tr id="itemTr_22">
								<td class="td_title" width="15%">
									货币类型
								</td>
								<td class="td_data" colspan="3" width="35%">
									<select   name='HBLX'  id='HBLX' ><option value=''>-空-</option>
<option value="CNY" selected >人民币</option>
<option value="USD">美元</option>
<option value="EUR">欧元</option>
</select>
								</td>
							</tr>
							<tr id="itemTr_3">
								<td class="td_title" id="title_SQR" width="15%">
									收款方
								</td>
								<td class="td_data" id="data_SQR" width="35%">
									<input type=radio   id='JSF0' name='JSF' value='个人' checked="checked"><label  id="lbl_JSF0"  for="JSF0">个人</label>&nbsp;
<input type=radio  id='JSF1' name='JSF' value='公司' ><label  id="lbl_JSF1" for="JSF1">公司</label>&nbsp;

								</td>
								<td class="td_title" id="title_SQRBMMC" width="15%">
									附单据张数
								</td>
								<td class="td_data" id="data_SQRBMMC" width="35%">
									<input type='text' class = '{maxlength:32,required:false}'  style="width:50px" name='DJZS' id='DJZS'  value='' >
								</td>
							</tr>
							<tr id="itemTr_4">
								<td class="td_title" id="title_SQR" width="15%">
									收款单位
								</td>
								<td class="td_data" id="data_SQR" width="35%">
									<input type='text' class = '{maxlength:60,required:false}'  style="width:100px" name='SKDW' id='SKDW'  value='' >
								</td>
								<td id="title_SQRBMMC" colspan="2">
									<table width="100%">
										<tbody>
											<tr>
												<td class="td_title">
													开户行
												</td>
												<td class="td_data">
													<input type='text' class = '{maxlength:64,required:false}'  style="width:100px" name='KHX' id='KHX'  value='' >
												</td>
												<td class="td_title">
													账号
												</td>
												<td class="td_data">
													<input type='text' class = '{maxlength:64,required:false}'  style="width:100px" name='YXZH' id='YXZH'  value='' >
												</td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
							<tr id="itemTr_221">
								<td class="td_title" width="15%">
									报销事由
								</td>
								<td class="td_data" colspan="3" width="35%">
									<textarea  class="{maxlength:512,required:false} "  name='BXSY' id='BXSY'  style="width:672px;height:50px;"  ></textarea>　
								</td>
							</tr>
            			
            		</table>
            		<s:hidden name="actDefId" id="actDefId"></s:hidden>
            </s:form>  
            </div> 
        <div region="south" border="false" > 
		 		<div style="padding:5px;text-align:right">
		 			<a class="easyui-linkbutton" icon="icon-ok"  plain="false"  href="javascript:doSubmit();" >发起</a> 
                <a  class="easyui-linkbutton" icon="icon-cancel"  plain="false"  href="javascript:close();">取消</a>
		 		</div>
        </div>  
</body>
</html>
