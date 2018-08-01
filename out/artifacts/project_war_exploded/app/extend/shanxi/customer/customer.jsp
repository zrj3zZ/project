<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"> </script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"> </script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"></script>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
	    $.ajaxSetup({async: false});
		mainFormValidator =  $("#ifrmMain").validate({});
		mainFormValidator.resetForm();
	});
	
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	
	$(function(){
		//分页
		$('#pp').pagination({
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
	    //查询
	    $("#search").click(function(){
	    	var valid = mainFormValidator.form(); //执行校验操作
	    	if(!valid){
	    		return false;
	    	}
	        var customerName = $("#CUSTOMERNAME").val();
	        var zqdm = $("#ZQDM").val();
	        var type = $("#TYPE").val();
	        var zwmc = $("#ZWMC").val();
	        var zczbbegin = $("#ZCZBBEGIN").val();
	        var zczbend = $("#ZCZBEND").val();
	        var gfgsrqbegin = $("#GFGSRQBEGIN").val();
	        var gfgsrqend = $("#GFGSRQEND").val();
	        var dq = $("#ZCQX").val();	       
	        var seachUrl = encodeURI("sx_loadCustomer.action?customername="+customerName+"&zqdm="+zqdm+"&type="+type+"&zwmc="+zwmc+"&zczbbegin="+zczbbegin+"&zczbend="+zczbend+"&gfgsrqbegin="+gfgsrqbegin+"&gfgsrqend="+gfgsrqend+"&dq="+dq);
	        window.location.href = seachUrl;
	    });
	    //复选/全选
        $("#chkAll").bind("click", function () {
        	$("[name =colname]:checkbox").attr("checked", this.checked);
        });
    });
		
	function edit(instanceid){
		var pageUrl = "openFormPage.action?formid=88&demId=21&instanceId="+instanceid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
		
	function addItem(){
		var pageUrl = "createFormInstance.action?formid=88&demId=21";
   		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = pageUrl;
	}
		
	function remove(){
		$.messager.confirm('确认','确认删除?',function(result){
			if(result){
				var list = $('[name=colname]').length;
				var a=0;
				for( var n = 0; n < list; n++){
					var flag = new Boolean();
					if($('[name=colname]')[n].checked==true&&String($('[name=colname]')[n].id)!=String('chkAll')){
						var deleteUrl = "sx_deleteCustomer.action";
						$.post(deleteUrl,{instanceid:$('[name=colname]')[n].id},function(data){ 
							if(data=='success'){
								window.location.reload();
							}else{
			       				flag=false;
			       				alert(data);
							}
						});
					} 
					if(!flag){break;}
				}
			}
		});
	}
	
	function addGugai(actDefId,customername,customerno,jdmc){
		var result=checkXmjd(customerno,"XMGG");
		var instanceId = result.instanceId;
		var url = "";
		if(instanceId==0){
   			url = 'processRuntimeStartInstance.action?actDefId='+actDefId+'&YGSMC='+encodeURI(customername)+'&CUSTOMERNO='+encodeURI(customerno)+"&JDMC="+encodeURI(jdmc);
		}else{
			var taskid = result.taskid;
			url="loadProcessFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+instanceId+"&taskId="+taskid+"&JDMC="+encodeURI(jdmc);
		}
   		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = url;
   		return;
	}
	
	function editGugai(lcbh,lcbs,taskid,jdmc) {
		var url="loadProcessFormPage.action?actDefId="+lcbh+"&instanceId="+lcbs+"&excutionId="+lcbs+"&taskId="+taskid+"&JDMC="+encodeURI(jdmc);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function addFormPage(zcqx,jlnjlr,formId,demId,customerno,customername,jdmc){
		var result=checkXmjd(customerno,"XMLX");
		var instanceId = result.instanceId;
		var url = "";
		if(instanceId==0){
			url = 'createFormInstance.action?formid=' + formId + '&demId=' + demId + '&CUSTOMERNO=' + customerno + '&PROJECTNAME=' + encodeURI(customername) + '&CUSTOMERNAME=' + encodeURI(customername) + '&GSGK=' + encodeURI(zcqx) + '&XMYS=' + encodeURI(jlnjlr)+'&JDMC='+encodeURI(jdmc);
		}else{
			url = 'openFormPage.action?instanceId=' + instanceId + '&formid='+ formId + '&demId=' + demId + '&CUSTOMERNO=' + customerno + '&GSGK=' + encodeURI(zcqx) + '&XMYS=' + encodeURI(jlnjlr)+'&JDMC='+encodeURI(jdmc);
		}
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function editFormPage(zcqx,jlnjlr,instanceId,formId,demId,customerno,jdmc){
		var url = 'openFormPage.action?instanceId=' + instanceId + '&formid='+ formId + '&demId=' + demId + '&CUSTOMERNO=' + customerno+'&JDMC='+encodeURI(jdmc);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function checkXmjd(customerno,jdmc){
		var result=null;
		$.ajaxSetup({
	        async: false
	    });
		$.ajax({
	        url : 'sx_zqb_checkXMJD.action',
	        async : false,
	        type : "POST",
	        data: {
	        	customerno: customerno,
		    	jdmc:jdmc
            },
	        dataType : "json",
	        success : function(data) {
	        	result=data;
	        }  
	    });
	    return result;
	}
	
	function addXmnh(actDefId,customername,customerno,jdmc){
		var result=checkXmjd(customerno,"XMNH");
		var instanceId = result.instanceId;
		var url = "";
		if(instanceId==0){
   			url = 'processRuntimeStartInstance.action?actDefId='+actDefId+'&CUSTOMERNAME='+encodeURI(customername)+'&CUSTOMERNO='+encodeURI(customerno)+'&JDMC='+encodeURI(jdmc);
		}else{
			var taskid = result.taskid;
			url="loadProcessFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+instanceId+"&taskId="+taskid+'&JDMC='+encodeURI(jdmc);
		}
   		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = url;
   		return;
	}
	
	function editXmnh(lcbh,lcbs,taskid,jdmc) {
		var url="loadProcessFormPage.action?actDefId="+lcbh+"&instanceId="+lcbs+"&excutionId="+lcbs+"&taskId="+taskid+'&JDMC='+encodeURI(jdmc);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function addNhfk(actDefId,customername,customerno,jdmc){
		var result=checkXmjd(customerno,"NHFK");
		var instanceId = result.instanceId;
		var url = "";
		if(instanceId==0){
   			url = 'processRuntimeStartInstance.action?actDefId='+actDefId+'&CUSTOMERNAME='+encodeURI(customername)+'&CUSTOMERNO='+encodeURI(customerno)+"&JDMC="+encodeURI(jdmc);
		}else{
			var taskid = result.taskid;
			url="loadProcessFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+instanceId+"&taskId="+taskid+"&JDMC="+encodeURI(jdmc);
		}
   		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = url;
   		return;
	}
	
	function editNhfk(lcbh,lcbs,taskid,jdmc) {
		var url="loadProcessFormPage.action?actDefId="+lcbh+"&instanceId="+lcbs+"&excutionId="+lcbs+"&taskId="+taskid+"&JDMC="+encodeURI(jdmc);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function addGzfkjhf(actDefId,customername,customerno,jdmc){
		var result=checkXmjd(customerno,"GZFKJHF");
		var instanceId = result.instanceId;
		var url = "";
		if(instanceId==0){
   			url = 'processRuntimeStartInstance.action?actDefId='+actDefId+'&CUSTOMERNAME='+encodeURI(customername)+'&CUSTOMERNO='+encodeURI(customerno)+"&JDMC="+encodeURI(jdmc);
		}else{
			var taskid = result.taskid;
			url="loadProcessFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+instanceId+"&taskId="+taskid+"&JDMC="+encodeURI(jdmc);
		}
   		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = url;
   		return;
	}
	
	function editGzfkjhf(lcbh,lcbs,taskid,jdmc) {
		var url="loadProcessFormPage.action?actDefId="+lcbh+"&instanceId="+lcbs+"&excutionId="+lcbs+"&taskId="+taskid+"&JDMC="+encodeURI(jdmc);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function addGuapai(actDefId,customername,customerno,jdmc){
		var result=checkXmjd(customerno,"GPDJJGD");
		var instanceId = result.instanceId;
		var url = "";
		if(instanceId==0){
   			url = 'processRuntimeStartInstance.action?actDefId='+actDefId+'&CUSTOMERNAME='+encodeURI(customername)+'&CUSTOMERNO='+encodeURI(customerno)+"&JDMC="+encodeURI(jdmc);
		}else{
			var taskid = result.taskid;
			url="loadProcessFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+instanceId+"&taskId="+taskid+"&JDMC="+encodeURI(jdmc);
		}
   		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = url;
   		return;
	}
	
	function editGuapai(lcbh,lcbs,taskid,jdmc) {
		var url="loadProcessFormPage.action?actDefId="+lcbh+"&instanceId="+lcbs+"&excutionId="+lcbs+"&taskId="+taskid+"&JDMC="+encodeURI(jdmc);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	//导出
	function expkh(){
		var pageUrl = "sx_expCustomer.action";
		window.location.href = pageUrl; 
	}
	
	function addProcessFormPage(instanceId, projectNo) {
		var authority='false';
		$.ajax({
	        url : 'sx_zqb_commitAuthority.action',
	        async : false,
	        type : "POST",
	        data: {
	        	projectNo:projectNo,
	        	xmlx:'推荐挂牌项目',
	        	jdmc:'项目立项'
            },
	        dataType : "json",
	        success : function(data) {
	        	if(data!=null){
		        	authority=data.authority;
	        	}else{
	        		authority='false';
	        	}
	        }  
	    });
		if(authority=='false'){
			alert("您不是项目负责人,请联系项目负责人提交项目!");
    		return;
		}else{
			var url="";
			var win_width = window.screen.width;
			var flag = new Boolean();
				$.ajax({
			        url : 'sx_zqb_xmlx_commit.action',
			        async : false,
			        type : "POST",
			        data: {
			        	instanceId:instanceId,
			        	projectNo:projectNo
	                },
			        dataType : "json",
			        success : function(data) {
			        	if(data!=null){
			        		flag=true;
				        	var executionId=data.executionId;
							var taskid=data.taskid;
							var actDefId=data.actDefId;
							var instanceId=data.instanceId;
							url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+executionId+'&taskId='+taskid;
			        	}else{
			        		flag=false;
			        	}
			        }  
			    });
				if(flag){
					var target = "_blank";
					var win_width = window.screen.width;
					var page = window.open('form/loader_frame.html',target,'width='+win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
					page.location = url;
				}
		}
	}
	
	function editProcessFormPage(lcbh,lcbs,taskid) {
		var url="loadProcessFormPage.action?actDefId="+lcbh+"&instanceId="+lcbs+"&excutionId="+lcbs+"&taskId="+taskid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
</script>
<style type="text/css">
.searchtitle {
	text-align: right;
	padding: 5px;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: 28px;
	font-size: 12px;
	vertical-align: text-middle;
	padding-top: 2px;
}

.header td {
	height: 35px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}

.cell td {
	margin: 0;
	padding: 3px 4px;
	height: 25px;
	font-size: 12px;
	white-space: nowrap;
	word-wrap: normal;
	overflow: hidden;
	text-align: left;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #eee;
}

.cell:hover {
	background-color: #F0F0F0;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav">
			<s:if test="orgroleid != 3">
				<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
				<a href="javascript:expkh();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">导出</a>
				<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
				<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			</s:if>
		</div>
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top;">
		<s:if test="orgroleid != 3">
			<form name='ifrmMain' id='ifrmMain' method="post">
				<div style="padding:5px;text-align:center;">
					<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">					
						<table width='95%' border='0' cellpadding='0' cellspacing='0'>
							<tr>
								<td class="searchtitle">客户名称</td>
								<td class="searchdata">
									<input type='text' class='{maxlength:128,required:false,string:true}' style="width:100px" name='CUSTOMERNAME' id='CUSTOMERNAME' value='${customername }'>
								</td>
								<td class="searchtitle">股票代码</td>
								<td class="searchdata">
									<input type='text' class='{maxlength:64,required:false,string:true}' style="width:100px" name='ZQDM' id='ZQDM' value='${zqdm}'>
								</td>
								<td class="searchtitle">成立日期</td>
								<td class="searchdata"><input type='text'
									onfocus="WdatePicker();"
									class='{maxlength:64,required:false}' style="width:80px;"
									name='GFGSRQBEGIN' id='GFGSRQBEGIN' value='${gfgsrqbegin}'> 到 <input
									type='text' onfocus="WdatePicker();"
									class='{maxlength:64,required:false}' style="width:80px;"
									name='GFGSRQEND' id='GFGSRQEND' value='${gfgsrqend}'></td>
								<td class="searchtitle">所属大区</td>
								<td class="searchdata">
									<input type='text' class='{maxlength:10,required:false,string:true}' style="width:100px" name='ZCQX' id='ZCQX' value='${dq}'>
								</td>
							</tr>
							<tr>
								<td class="searchtitle">注册类型</td>
								<td class="searchdata"><input type='text'
									class='{maxlength:64,required:false,string:true}'
									style="width:100px" name='TYPE' id='TYPE' value='${type}'></td>
								<td class="searchtitle">住所地</td>
								<td class="searchdata"><input type='text'
									class='{maxlength:64,required:false,string:true}'
									style="width:100px" name='ZWMC' id='ZWMC' value='${zwmc}'></td>
								<td class="searchtitle">注册资本</td>
								<td class="searchdata"><input type='text'
									class='{maxlength:8,required:false,number:true}'
									style="width:60px;" name='ZCZBBEGIN' id='ZCZBBEGIN' value='${zczbbegin}'>
									到 <input type='text'
									class='{maxlength:8,required:false,number:true}'
									style="width:60px;" name='ZCZBEND' id='ZCZBEND' value='${zczbend}'>万元</td>									
								<td>
									<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">
										查询
									</a>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</form>
		</s:if>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:5%;" class="header"><input type="checkbox" name="colname" id="chkAll" /></td>
					<td style="width:5%;">序号</td>
					<td style="width:20%;">客户名称</td>
					<td style="width:5%;">证券简称(公司简称)</td>
					<td style="width:5%;">证券代码(股票代码)</td>
					<td style="width:10%;">所属行业</td>
					<td style="width:10%;">住所地</td>
					<td style="width:5%;">注册类型</td>
					<td style="width:10%;">注册资本(万元)</td>
					<td style="width:10%;">成立日期</td>
					<td style="width:10%;">项目阶段</td>
					<td style="width:10%;">阶段审批状态</td>
				</tr>
				<s:iterator value="customerList" status="status">
					<tr class="cell">
						<s:if
							test="#status.index-1<0||CUSTOMERNO!=customerList[#status.index-1].CUSTOMERNO">
							<td rowspan="<s:property value="CNUM"/>">
								<input
								type="checkbox" id="<s:property value='INSTANCEID'/>"
								name="colname" value="<s:property value='INSTANCEID'/>" />
							</td>
							<td rowspan="<s:property value="CNUM"/>">
								<s:property value="RNUM" />
							</td>
							<td rowspan="<s:property value="CNUM"/>">
								<a href="javascript:void(0)" onclick="edit(<s:property value='INSTANCEID'/>)">
									<s:property value="CUSTOMERNAME" />
								</a>
							</td>
							<td rowspan="<s:property value="CNUM"/>">
								<s:property value="ZQJC" />
							</td>
							<td rowspan="<s:property value="CNUM"/>">
								<s:property value="ZQDM" />
							</td>
							<td rowspan="<s:property value="CNUM"/>">
								<s:property value="SSHY" />
							</td>
							<td rowspan="<s:property value="CNUM"/>">
								<s:property value="ZWMC" />
							</td>
							<td rowspan="<s:property value="CNUM"/>">
								<s:property value="TYPE" />
							</td>
							<td rowspan="<s:property value="CNUM"/>">
								<s:property value="ZCZB" />
							</td>
							<td rowspan="<s:property value="CNUM"/>">
								<s:property value="GFGSRQ" />
							</td>
						</s:if>
						<s:if test="JDMC=='项目立项'">
							<s:if test="PJID==0">
								<td><a href="javascript:void(0)" onclick="addFormPage('<s:property value='ZCQX'/>','<s:property value='JLNJLR'/>','<s:property value='xmlxFormId'/>','<s:property value='xmlxDemId'/>','<s:property value='CUSTOMERNO'/>','<s:property value='CUSTOMERNAME'/>','<s:property value="JDMC" />')"><s:property value="JDMC" /></a></td>
							</s:if>
							<s:else>
								<td><a href="javascript:void(0)" onclick="editFormPage('<s:property value='ZCQX'/>','<s:property value='JLNJLR'/>',<s:property value='PJINSID'/>,'<s:property value='xmlxFormId'/>','<s:property value='xmlxDemId'/>','<s:property value='CUSTOMERNO'/>','<s:property value="JDMC" />')"><s:property value="JDMC" /></a></td>
							</s:else>
							<s:if test="XMLXSPZT!=null&&XMLXSPZT!='审批通过'">
								<s:if test="XMLXSPZT.indexOf('驳回')>0||XMLXSPZT=='驳回'">
									<td><a href="javascript:void(0)" onclick="editProcessFormPage('<s:property value='XMLXLCBH'/>',<s:property value='XMLXLCBS'/>,<s:property value='XMLXLCTASKID'/>)"><s:property value="XMLXSPZT" /></a></td>
								</s:if>
								<s:elseif test="XMLXSPZT=='提交审批'&&PJID!=0">
									<td><a href="javascript:void(0)" onclick="addProcessFormPage(<s:property value='PJINSID'/>,'<s:property value='PJNO'/>')"><s:property value="XMLXSPZT" /></a></td>
								</s:elseif>
								<s:elseif test="XMLXSPZT=='提交审批'&&PJID==0">
									<td>未提交</td>
								</s:elseif>
								<s:elseif test="XMLXSPZT=='起草'">
									<td><a href="javascript:void(0)" onclick="editProcessFormPage('<s:property value='XMLXLCBH'/>',<s:property value='XMLXLCBS'/>,<s:property value='XMLXLCTASKID'/>)">申报中</a></td>
								</s:elseif>
								<s:else>
									<td><a href="javascript:void(0)" onclick="editProcessFormPage('<s:property value='XMLXLCBH'/>',<s:property value='XMLXLCBS'/>,<s:property value='XMLXLCTASKID'/>)"><s:property value="XMLXSPZT" />审批中</a></td>
								</s:else>
							</s:if>
							<s:else>
								<td style="cursor:pointer;" onclick="editProcessFormPage('<s:property value='XMLXLCBH'/>',<s:property value='XMLXLCBS'/>,<s:property value='XMLXLCTASKID'/>)"><s:property value="XMLXSPZT" /></td>
							</s:else>
						</s:if>
						<s:elseif test="JDMC=='股改'">
							<s:if test="GGLCTASKID==0">
								<td><a href="javascript:void(0)" onclick="addGugai('<s:property value='xmggshServer'/>','<s:property value='CUSTOMERNAME'/>','<s:property value='CUSTOMERNO'/>','<s:property value='JDMC'/>')"><s:property value="JDMC" /></a></td>
							</s:if>
							<s:else>
								<td><a href="javascript:void(0)" onclick="editGugai('<s:property value='GGLCBH'/>',<s:property value='GGLCBS'/>,<s:property value='GGLCTASKID'/>,'<s:property value='JDMC'/>')"><s:property value="JDMC" /></a></td>
							</s:else>
							<s:if test="GGLCSPZT!=null&&GGLCSPZT!='审批通过'">
								<s:if test="GGLCSPZT.indexOf('驳回')>0||GGLCSPZT=='驳回'">
									<td><s:property value="GGLCSPZT" /></td>
								</s:if>
								<s:elseif test="GGLCSPZT=='起草'">
									<td>申报中</td>
								</s:elseif>
								<s:else>
									<td><s:property value="GGLCSPZT" />审批中</td>
								</s:else>
							</s:if>
							<s:elseif test="GGLCSPZT!=null&&GGLCSPZT=='审批通过'">
								<td style="cursor:pointer;" onclick="editGugai('<s:property value='GGLCBH'/>',<s:property value='GGLCBS'/>,<s:property value='GGLCTASKID'/>)"><s:property value="GGLCSPZT" /></td>
							</s:elseif>
							<s:else>
								<td>未提交</td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='申报审核'">
							<s:if test="XMNHLCTASKID==0">
								<td><a href="javascript:void(0)" onclick="addXmnh('<s:property value='xmnhshServer'/>','<s:property value='CUSTOMERNAME'/>','<s:property value='CUSTOMERNO'/>','<s:property value="JDMC" />')"><s:property value="JDMC" /></a></td>
							</s:if>
							<s:else>
								<td><a href="javascript:void(0)" onclick="editXmnh('<s:property value='XMNHLCBH'/>',<s:property value='XMNHLCBS'/>,<s:property value='XMNHLCTASKID'/>),'<s:property value="JDMC" />'"><s:property value="JDMC" /></a></td>
							</s:else>
							<s:if test="XMNHLCSPZT!=null&&XMNHLCSPZT!='审批通过'">
								<s:if test="XMNHLCSPZT.indexOf('驳回')>0||XMNHLCSPZT=='驳回'">
									<td><s:property value="XMNHLCSPZT" /></td>
								</s:if>
								<s:elseif test="XMNHLCSPZT=='起草'">
									<td>申报中</td>
								</s:elseif>
								<s:else>
									<td><s:property value="XMNHLCSPZT" />审批中</td>
								</s:else>
							</s:if>
							<s:elseif test="XMNHLCSPZT!=null&&XMNHLCSPZT=='审批通过'">
								<td style="cursor:pointer;" onclick="editXmnh('<s:property value='XMNHLCBH'/>',<s:property value='XMNHLCBS'/>,<s:property value='XMNHLCTASKID'/>)"><s:property value="XMNHLCSPZT" /></td>
							</s:elseif>
							<s:else>
								<td>未提交</td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='内核反馈及回复'">
							<s:if test="NHFKLCTASKID==0">
								<td><a href="javascript:void(0)" onclick="addNhfk('<s:property value='nhfkshServer'/>','<s:property value='CUSTOMERNAME'/>','<s:property value='CUSTOMERNO'/>','<s:property value="JDMC" />')"><s:property value="JDMC" /></a></td>
							</s:if>
							<s:else>
								<td><a href="javascript:void(0)" onclick="editNhfk('<s:property value='NHFKLCBH'/>','<s:property value='NHFKLCBS'/>','<s:property value='NHFKLCTASKID'/>','<s:property value="JDMC" />')"><s:property value="JDMC" /></a></td>
							</s:else>
							<s:if test="NHFKLCSPZT!=null&&NHFKLCSPZT!='审批通过'">
								<s:if test="NHFKLCSPZT.indexOf('驳回')>0||NHFKLCSPZT=='驳回'">
									<td><s:property value="NHFKLCSPZT" /></td>
								</s:if>
								<s:elseif test="NHFKLCSPZT=='起草'">
									<td>申报中</td>
								</s:elseif>
								<s:else>
									<td><s:property value="NHFKLCSPZT" />审批中</td>
								</s:else>
							</s:if>
							<s:elseif test="NHFKLCSPZT!=null&&NHFKLCSPZT=='审批通过'">
								<td style="cursor:pointer;" onclick="editNhfk('<s:property value='NHFKLCBH'/>',<s:property value='NHFKLCBS'/>,<s:property value='NHFKLCTASKID'/>)"><s:property value="NHFKLCSPZT" /></td>
							</s:elseif>
							<s:else>
								<td>未提交</td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='股转反馈及回复'">
							<s:if test="GZFKLCTASKID==0">
								<td><a href="javascript:void(0)" onclick="addGzfkjhf('<s:property value='gzfkshServer'/>','<s:property value='CUSTOMERNAME'/>','<s:property value='CUSTOMERNO'/>','<s:property value="JDMC" />')"><s:property value="JDMC" /></a></td>
							</s:if>
							<s:else>
								<td><a href="javascript:void(0)" onclick="editGzfkjhf('<s:property value='GZFKLCBH'/>',<s:property value='GZFKLCBS'/>,<s:property value='GZFKLCTASKID'/>,'<s:property value="JDMC" />')"><s:property value="JDMC" /></a></td>
							</s:else>
							<s:if test="GZFKLCSPZT!=null&&GZFKLCSPZT!='审批通过'">
								<s:if test="GZFKLCSPZT.indexOf('驳回')>0||GZFKLCSPZT=='驳回'">
									<td><s:property value="GZFKLCSPZT" /></td>
								</s:if>
								<s:elseif test="GZFKLCSPZT=='起草'">
									<td>申报中</td>
								</s:elseif>
								<s:else>
									<td><s:property value="GZFKLCSPZT" />审批中</td>
								</s:else>
							</s:if>
							<s:elseif test="GZFKLCSPZT!=null&&GZFKLCSPZT=='审批通过'">
								<td style="cursor:pointer;" onclick="editGzfkjhf('<s:property value='GZFKLCBH'/>',<s:property value='GZFKLCBS'/>,<s:property value='GZFKLCTASKID'/>)"><s:property value="GZFKLCSPZT" /></td>
							</s:elseif>
							<s:else>
								<td>未提交</td>
							</s:else>
						</s:elseif>
						<s:elseif test="JDMC=='挂牌登记及归档'">
							<s:if test="GPDJJGDLCTASKID==0">
								<td><a href="javascript:void(0)" onclick="addGuapai('<s:property value='gpdjjgdServer'/>','<s:property value='CUSTOMERNAME'/>','<s:property value='CUSTOMERNO'/>','<s:property value="JDMC" />')"><s:property value="JDMC" /></a></td>
							</s:if>
							<s:else>
								<td><a href="javascript:void(0)" onclick="editGuapai('<s:property value='GPDJJGDLCBH'/>',<s:property value='GPDJJGDLCBS'/>,<s:property value='GPDJJGDLCTASKID'/>,'<s:property value="JDMC" />')"><s:property value="JDMC" /></a></td>
							</s:else>
							<s:if test="GPDJJGDLCSPZT!=null&&GPDJJGDLCSPZT!='审批通过'">
								<s:if test="GPDJJGDLCSPZT.indexOf('驳回')>0||GPDJJGDLCSPZT=='驳回'">
									<td><s:property value="GPDJJGDLCSPZT" /></td>
								</s:if>
								<s:elseif test="GPDJJGDLCSPZT=='起草'">
									<td>申报中</td>
								</s:elseif>
								<s:else>
									<td><s:property value="GPDJJGDLCSPZT" />审批中</td>
								</s:else>
							</s:if>
							<s:elseif test="GPDJJGDLCSPZT!=null&&GPDJJGDLCSPZT=='审批通过'">
								<td style="cursor:pointer;" onclick="editGuapai('<s:property value='GPDJJGDLCBH'/>',<s:property value='GPDJJGDLCBS'/>,<s:property value='GPDJJGDLCTASKID'/>)"><s:property value="GPDJJGDLCSPZT" /></td>
							</s:elseif>
							<s:else>
								<td>未提交</td>
							</s:else>
						</s:elseif>
					</tr>
				</s:iterator>
			</table>

			<form action="sx_loadCustomer.action" method="post" name="frmMain" id="frmMain">
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="customerno" id="customerno"></s:hidden>
				<s:hidden name="zqdm" id="zqdm"></s:hidden>
				<s:hidden name="zqjc" id="zqjc"></s:hidden>
				<s:hidden name="customername" id="customername"></s:hidden>
				<s:hidden name="type" id="type"></s:hidden>
				<s:hidden name="zwmc" id="zwmc"></s:hidden>
				<s:hidden name="zczbbegin" id="zczbbegin"></s:hidden>
				<s:hidden name="zczbend" id="zczbend"></s:hidden>
				<s:hidden name="gfgsrqbegin" id="gfgsrqbegin"></s:hidden>
				<s:hidden name="gfgsrqend" id="gfgsrqend"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south"
		style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
		border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>