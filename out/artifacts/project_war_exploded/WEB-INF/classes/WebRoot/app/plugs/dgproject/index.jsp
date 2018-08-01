<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>项目管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="admin/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
			mainFormValidator =  $("#ifrmMain").validate({
			 });
			 mainFormValidator.resetForm();
	});
    $(function(){
		$('#pp').pagination({  
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});

		$('#bb').pagination({
			total : <s:property value="closeNum"/>,
			pageNumber : <s:property value="pageNumber2"/>,
			pageSize : <s:property value="pageSize2"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMbb(pageNumber, pageSize);
			}
		});
		$("#search").click(function(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return ;
			}
			var type = $("#type").val();
			var xmjd = $("#XMJD").val();
			var ssxq = $("#SSXQ").val();
			var customername = $("#CUSTOMERNAME").val();
			var dgzt = $("#QRDG").val();
			var xmlx = $("#XMLX").val();
			var sssyb = $("#SSSYB").val();
			if(sssyb==undefined){
				sssyb="";
			}
			var cyrname = $("#CYRNAME").val();
			var seachUrl = encodeURI("dg_zqb_project_index.action?customername="+ customername + "&xmjd=" + xmjd +"&dgzt=" + dgzt +"&sssyb="+sssyb+"&cyrName="+cyrname+"&xmlx="+xmlx+"&type="+type+"&ssxq="+ssxq);// + "&projectName="+projectName );
			window.location.href = seachUrl;
		});
	});

	
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return;
	}
	function submitMbb(pageNumber, pageSize) {
		$("#pageNumber2").val(pageNumber);
		$("#pageSize2").val(pageSize);
		$("#frmMain2").submit();
		return;
	}
	function addItem() {
		var formid = $('#tjgpbasemsgformid').val();
		var demId = $('#tjgpbasemsgdemid').val();
		var url = 'createFormInstance.action?formid=' + formid + '&demId=' + demId;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width + ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	}
	
	function addBgczItem(types){
		var formid = $('#formid').val();
		var demId = $('#demid').val();
		var type = $('#type').val();
		
		var url = 'createFormInstance.action?formid='+formid+'&demId='+demId+'&COMPANYNAME='+type+'&isHFRandHFNRdiaplsy='+types;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+ win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	}
	
	function addQtItem(type){
		var formid = $('#ipoformid').val();
		var demId = $('#ipodemid').val();
		var types = $('#type').val();
		var url = 'createFormInstance.action?formid='+formid+'&demId='+demId+'&isHFRandHFNRdiaplsy='+type+'&A01='+types;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+ win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	}
	
	function addGpfxItem() {
		var formid = $("#dxfxformid").val();
		var demId = $("#dxfxdemid").val();
		var url = 'createFormInstance.action?formid=' + formid + '&demId=' + demId;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width + ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	}
	
	function editItem(instanceId,tag,flag){
	    if(tag=='其他')
		var formid;
		var demid;
		var qt='';
		if(tag==1){
			formid = $('#tjgpbasemsgformid').val();
			demId = $('#tjgpbasemsgdemid').val();
		}else if(tag==2){
			var formid = $('#dxfxformid').val();
			var demId = $('#dxfxdemid').val();
		}else if(tag==3){
			formid = $('#formid').val();
			demId = $('#demid').val();
		}else{
			formid = $('#ipoformid').val();
			demId = $('#ipodemid').val();
		}
		var url = 'openFormPage.action?formid=' + formid + '&demId=' + demId + '&instanceId=' + instanceId+ '&dgyc=' + flag;
        if(tag=='其他'){
            var url = 'openFormPage.action?formid=' + formid + '&demId=' + demId + '&instanceId=' + instanceId+ '&dgyc=' + flag+"&isHFRandHFNRdiaplsy=xsbqt";
        }
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width + ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	}
	
	function closeItem() {
		    var flag=$("input[type='checkbox']").is(':checked');
		    if(flag){
				try{
				
					var list = $('[name=chk_list]').length;
					var a=0;
					var ErrNum = 0;	
					 for( var n = 0; n < list; n++){
						 if($('[name=chk_list]')[n].checked==true&&$('[name=chk_list]')[n].id!=String('chkAll')){
						 	var tag = $('[name=chk_list]')[n].value;
						 	
							var xmlx = $('[name=chk_list]')[n].title;
						 	var ins1id =$('[name=chk_list]')[n].id;
						 	
						 	
						 	var xmmc=$("#xmmc"+tag).val();
						 	var insid=$("#insid"+tag).val();
						 	var formid=$("#formid"+tag).val();
						 	var demid=$("#demid"+tag).val();
						 	var jdid=$("#jdid"+tag).val();
						 	var xmbh=$("#xmbh"+tag).val();
						 	var jdmc=$("#jdmc"+tag).val();
						 	var zjdmc="";
						 	if(jdmc=='1')  zjdmc='股转挂牌';
						 	else if(jdmc=='2') zjdmc='股转定增';
						 	else if(jdmc=='3') zjdmc='并购重组';
						 	else  zjdmc=jdmc;
						 	if(insid!='0'){
						 		//view-source:http://127.0.0.1:8080/openFormPage.action?formid=286&demId=163&instanceId=178139
						 		var pageUrl = encodeURI("openFormPage.action?formid="+formid+"&demId="+demid+"&instanceId="+insid+"&dggbxz=1"+"&dgxmlx="+xmlx+"&dginsid="+ins1id);
								art.dialog.open(pageUrl,{
											id:'projectItem',
											cover:true, 
											title:'',
											loadingText:'正在加载中,请稍后...',
											bgcolor:'#999',
											rang:true,
											width:1000,
											cache:false,
											lock: true,
											height:600, 
											iconTitle:false,
											extendDrag:true,
											autoSize:false,
											close:function(){
												window.location.reload();
											}
										});
						 	}else{
						 		var pageUrl = encodeURI("createFormInstance.action?formid="+formid+"&demId="+demid+"&COMPANYNAME="+xmmc+"&PROJECTNAME="+xmmc+"&GROUPID="+jdid+"&JDMC=项目终止"+"&PROJECTNO="+xmbh+"&dggbxz=1"+"&dgxmlx="+xmlx+"&dginsid="+ins1id+"&JDBH="+jdid+"&EXTEND2="+xmbh+"&XMBH="+xmbh+"&EXTEND1="+jdid);
								art.dialog.open(pageUrl,{
											id:'projectItem',
											cover:true, 
											title:'',
											loadingText:'正在加载中,请稍后...',
											bgcolor:'#999',
											rang:true,
											width:1000,
											cache:false,
											lock: true,
											height:600, 
											iconTitle:false,
											extendDrag:true,
											autoSize:false,
											close:function(){
												window.location.reload();
											}
										});
						 	}
						}
					}
				
				}catch(e){}
		    }else{
		    	$.messager.alert('提示信息','请选择您要关闭的项目!','info');
		    }
	}
	function  closeXM(){
				try{
					var list = $('[name=chk_list]').length;
					var a=0;
					var ErrNum = 0;	
					 for( var n = 0; n < list; n++){
						 if($('[name=chk_list]')[n].checked==true&&$('[name=chk_list]')[n].id!=String('chkAll')){
						 	var tag = $('[name=chk_list]')[n].title;
						 	$.post("dg_zqb_project_close.action",{instanceId:$('[name=chk_list]')[n].id,xmlx:tag},function(data){ 
				       			if(data=='success'){
				       				window.location.reload();
				       			}else{
				       				alert("项目关闭失败，请重试");
				       			}
				     		 });
						}
					}
				
				}catch(e){}
	}
	// 全选、全清功能
	function selectAll(){
		if($("input[name='chk_list']").attr("checked")){
			$("input[name='chk_list']").attr("checked",true);
		}else{
			$("input[name='chk_list']").attr("checked",false);
		}
	}
	function zrcxddItem(){
	   $.messager.confirm('确认','确认转入持续督导?',function(result){ 
		if(result){
		var list = $('[name=chk_list]').length;
		var a=0;
		var ErrNum = 0;	
		 for( var n = 0; n < list; n++){
			 if($('[name=chk_list]')[n].checked==false&&$('[name=chk_list]')[n].id!='chkAll'){
				 a++;
				 if(a==list-1){
				  	$.messager.alert('提示信息','请选择您要转入持续督导的项目!','info');  
					return;
				 	}
			 	}
			 	else if($('[name=chk_list]')[n].checked==true&&$('[name=chk_list]')[n].id!='chkAll'){
			 	{
					var cxddInstanceid=$('[name=chk_list]')[n].id;
					var tag=$('[name=chk_list]')[n].title;
					if(tag==1){
				 		var pageUrl = "dg_zqb_project_finish_item.action";
						$.post(pageUrl,{instanceid:$('[name=chk_list]')[n].id},function(data){ 
			       			if(data=='success'){
			       				window.location.reload();
			       			}else{
			       				alert("项目转入持续督导阶段失败，请重试");
			       			}
		     		 });
				 	}else if(tag==2){
				 		alert("并购重组项目不能转入持续督导阶段!");
				 	}else if(tag==3){
				 		alert("定增项目不能转入持续督导阶段!");
				 	}else{
				 		alert(tag+"项目不可转入持续督导!");
				 	}
			 	}
			}
		}}});
	}
	function expPro(){
		var type = $("#type").val();
		var pageUrl = "dg_zqb_project_expindex.action?type="+type
		art.dialog.open(pageUrl,{
			id : 'projectTask',
			cover : true,
			title : '项目导出',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1050,
			cache : false,
			lock : true,
			height : 650,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			content : pageUrl
		});
		
	}
	

	function chooseOne(chk){
		//先取得同name的chekcBox的集合物件
		var obj = document.getElementsByName("chk_list");
		for (i=0; i<obj.length; i++){
			//判斷obj集合中的i元素是否為cb，若否則表示未被點選
			if (obj[i]!=chk) obj[i].checked = false;
			//若要至少勾選一個的話，則把上面那行else拿掉，換用下面那行
			else obj[i].checked = true;
		}
	} 
	function updzt(insid,formid,demid){
 		var pageUrl = "openFormPage.action?formid="+formid+"&demId="+demid+"&instanceId="+insid;
		art.dialog.open(pageUrl,{
					id:'projectItem',
					cover:true, 
					title:'',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1000,
					cache:false,
					lock: true,
					height:600, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false
				});
	}
</script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="iwork_js/plugs/gantt2/jquery-ui-1.8.4.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.position.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.autocomplete.js"></script>
<style type="text/css">
.header td {
	height: 30px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}

.content td {
	border: 1px solid #efefef;
}
</style>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

.groupTitle {
	font-family: 黑体;
	font-size: 12px;
	text-align: left;
	color: #666;
	height-line: 20px;
	padding: 5px;
	padding-left: 15px;
	border-bottom: 1px solid #efefef;
}

.itemList {
	font-family: 宋体;
	font-size: 12px;
	height: 200px;
	padding-left: 15px;
}

.itemList td {
	list-style: none;
	height: 31px;
	padding: 2px;
	padding-left: 5px;
}

.itemList tr:hover {
	color: #0000ff;
	cursor: pointer;
}

.itemList  td {
	font-size: 12px;
}

.itemicon {
	padding-left: 25px;
	background: transparent url(iwork_img/application_view_list.gif)
		no-repeat scroll 0px 3px;
}

.gridTitle {
	padding-left: 25px;
	height: 20px;
	font-size: 14px;
	font-family: 黑体;
	background: transparent url(iwork_img/table_multiple.png) no-repeat
		scroll 5px 1px;
}

.grid {
	padding: 5px;
	vertical-align: top;
}

.grid table {
	width: 100%;
	border: 1px solid #efefef;
}

.grid th {
	padding: 5px;
	font-size: 12px;
	font-weight: 500;
	height: 20px;
	background-color: #ffffee;
	border-bottom: 1px solid #ccc;
}

.grid tr:hover {
	background-color: #efefef;
}

.grid td {
	padding: 5px;
	line-height: 16px;
}
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
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
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
			<s:if test="type=='xsb'">
				<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">推荐挂牌</a>
				<a href="javascript:addGpfxItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">定增</a>
				<a href="javascript:addBgczItem('xsbcc');" class="easyui-linkbutton" plain="true" iconCls="icon-add">并购重组</a>			
				<a href="javascript:addQtItem('xsbqt');" class="easyui-linkbutton" plain="true" iconCls="icon-add">其他</a>			
			</s:if>
			<s:elseif test="type=='ssgs'">
				<a href="javascript:addQtItem('sc');" class="easyui-linkbutton" plain="true" iconCls="icon-add">首次公开发行股票</a>
				<a href="javascript:addQtItem('zrz');" class="easyui-linkbutton" plain="true" iconCls="icon-add">再融资</a>
				<a href="javascript:addBgczItem('ssgscc');" class="easyui-linkbutton" plain="true" iconCls="icon-add">并购重组</a>			
				<a href="javascript:addQtItem('ssgsqt');" class="easyui-linkbutton" plain="true" iconCls="icon-add">其他</a>
			</s:elseif>
			<s:elseif test="type=='zq'">
				<a href="javascript:addQtItem('gsz');" class="easyui-linkbutton" plain="true" iconCls="icon-add">公司债</a>
				<a href="javascript:addQtItem('qyz');" class="easyui-linkbutton" plain="true" iconCls="icon-add">企业债</a>
				<a href="javascript:addQtItem('kjhz');" class="easyui-linkbutton" plain="true" iconCls="icon-add">可交换债</a>			
				<a href="javascript:addQtItem('zqqt');" class="easyui-linkbutton" plain="true" iconCls="icon-add">其他</a>
			</s:elseif>
			
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<s:if test="flag=='true'">
				<a href='javascript:closeItem();' class="easyui-linkbutton" plain="true" iconCls="icon-remove">关闭项目</a>
			</s:if>
			
			<a href="javascript:expPro();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">项目基本情况汇总导出</a>
			<!-- <a href="javascript:zrcxddItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">转入持续督导阶段</a>
			<a href="javascript:expProCXDD();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出持续督导的项目</a> -->			
		</div>
		<div style="padding:5px">
			<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
				<form id="ifrmMain" name="ifrmMain">
				<table width="99%" cellspacing="0" cellpadding="0" border="0">
					<tbody>
						<tr>
							<td style="padding-top:10px;padding-bottom:10px;">
								<table width="100%" cellspacing="0" cellpadding="0" border="0" id="aa">
									<tbody>
										<tr>
											<td class="searchtitle" style="text-align:right;">客户名称</td>
											<td class="searchdata"><input id="CUSTOMERNAME" class="{maxlength:128,required:false,string:true}" type="text" name="CUSTOMERNAME" style="width:100px;" value=""></td>
											<td class="searchtitle" style="text-align:right;">承做部门</td>
											<td class="searchdata"><input id="SSSYB" class="{maxlength:128,required:false,string:true}" type="text" name="SSSYB" style="width:100px;" value="${sssyb}"></td>
											<td class="searchtitle" style="text-align:right;">参与人姓名</td>
											<td class="searchdata"><input id="CYRNAME" class="{maxlength:128,required:false,string:true}" type="text" name="CYRNAME" style="width:100px;" value="${cyrName}"></td>
											<td class="searchtitle" style="text-align:right;">项目类型</td>
											<td class="searchdata">
												<select name='XMLX' id='XMLX' ><option value=''>-未选择-</option>
												<s:if test="type=='xsb'">
													<option value='1'>股转挂牌</option>
													<option value='2'>股转定增</option>
													<option value='3'>并购重组</option>
													<option value="其他">其他</option>
												</s:if>
												<s:elseif test="type=='ssgs'">
													<option value="IPO">IPO</option>
													<option value="改制">改制</option>
													<option value="辅导">辅导</option>
													<option value="增发">增发</option>
													<option value="配股">配股</option>
													<option value="非公开发行">非公开发行</option>
													<option value="可转债">可转债</option>
													<option value='3'>并购重组</option>
													<option value="其他">其他</option>
												</s:elseif>
												<s:elseif test="type=='zq'">
													<option value="公司债">公司债</option>
													<option value="企业债">企业债</option>
													<option value="可交换债">可交换债</option>
													<option value="其他">其他</option>
												</s:elseif>
												</select>
											</td>
										</tr>
										<tr>
											<td class="searchtitle" style="text-align:right;">项目进展</td>
											<td class="searchdata"><input id="XMJD" class="{maxlength:128,required:false,string:true}" type="text" name="XMJD" style="width:100px;" value=""></td>
											<td class="searchtitle" style="text-align:right;">所属辖区</td>
											<td class="searchdata"><input id="SSXQ" class="{maxlength:128,required:false,string:true}" type="text" name="SSXQ" style="width:100px;" value=""></td>
											<!-- <td class="searchtitle" style="text-align:right;">项目底稿归档情况</td>
											<td class="searchdata"><input id="" class="{maxlength:128,required:false,string:true}" type="text" name="" style="width:100px;" value=""></td> -->
										</tr>
									</tbody>
								</table>
							</td>
							<td valign="bottom" style="padding-bottom:5px;">
								<a id="search" class="easyui-linkbutton l-btn" href="javascript:search();" icon="icon-search">查询</a>
							</td>
						</tr>
					</tbody>
				</table>
				</form>
			</div>
		</div>
	</div>
	<div region="center" border="false">
		<div id="mainFrameTab" style="border:0px" class="easyui-tabs" fit="true">
			<div title="正在执行的项目" border="false" style="border:0px"
				iconCls="icon-search" cache="false">
				<div class="itemList">
					<table width="100%">
						<tr class="header" style="border:1px solid #efefef">
							<td style="text-align:left;width:20px;"></td>
							<td style="text-align: left;">客户名称</td>
							<td style="text-align: left;">项目类型</td>
							<td style="text-align: left;">项目负责人</td>
							<td style="text-align: left;">承做时间</td>
							<td style="text-align: left;">进展阶段</td>
							<td style="text-align: left;">承做部门</td>
						</tr>
						<s:iterator value="runList" status="ll">
							<tr class="content">
								<TD style="text-align:left;width:20px;" rowspan="<s:property value="NUM"/>">
									<input title="<s:property value="TAG" />" type="checkbox" name="chk_list" value="<s:property value="js"/>" onclick="chooseOne(this);" id="<s:property value="INSTANCEID"/>">
									<input type="hidden" value="<s:property value="xmmc"/>" id="xmmc<s:property value="js"/>" />
									<input type="hidden" value="<s:property value="insid"/>"  id="insid<s:property value="js"/>" />
									<input type="hidden" value="<s:property value="formid"/>"   id="formid<s:property value="js"/>" />
									<input type="hidden" value="<s:property value="demid"/>" id="demid<s:property value="js"/>" />
									<input type="hidden" value="<s:property value="jdid"/>"  id="jdid<s:property value="js"/>" />
									<input type="hidden" value="<s:property value="TAG"/>"  id="jdmc<s:property value="js"/>" />
									<input type="hidden" value="<s:property value="xmbh"/>"  id="xmbh<s:property value="js"/>" />
								</TD>
								<td onclick="editItem(<s:property value="INSTANCEID"/>,'<s:property value="TAG"/>','1')">
									<s:property value="CUSTOMERNAME" />
								</td>
								<td>
									<s:if test="TAG==1">
									股转挂牌
									</s:if>
									<s:elseif test="TAG==2">
									股转定增
									</s:elseif>
									<s:elseif test="TAG==3">
									并购重组
									</s:elseif>
									<s:else>
									<s:property value="TAG" />
									</s:else>
								</td>
								<td><s:property value="MANAGER" /></td>
								<td><s:property value="SDATE" /></td>
								<td><s:property value="XMJD" /></td>
								<td><s:property value="CZBM" /></td>
							</tr>
						</s:iterator>
					</table>
					<div style="padding:5px">
						<s:if test="totalNum==0">
						</s:if>
						<s:else>
							<div id="pp"
								style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
						</s:else>
					</div>
					<form action="dg_zqb_project_index.action" method=post name=frmMain id=frmMain>
						<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
						<s:hidden name="pageSize" id="pageSize"></s:hidden>
						<s:hidden name="projectName" id="projectName"></s:hidden>
						<s:hidden name="xmjd" id="xmjd"></s:hidden>
						<s:hidden name="ssxq" id="ssxq"></s:hidden>
						<s:hidden name="customername" id="customername"></s:hidden>
						<s:hidden name="dgzt" id="dgzt"></s:hidden>
						<s:hidden name="ymid" id="ymid" value="0"></s:hidden>
						<s:hidden name="sssyb" id="sssyb"></s:hidden>
						<s:hidden name="cyrName" id="cyrName"></s:hidden>
						<s:hidden name="xmlx" id="xmlx"></s:hidden>
						
						<s:hidden name="tjgpbasemsgdemid" id="tjgpbasemsgdemid"></s:hidden>
						<s:hidden name="tjgpbasemsgformid" id="tjgpbasemsgformid"></s:hidden>
						<s:hidden name="dxfxformid" id="dxfxformid"></s:hidden>
						<s:hidden name="dxfxdemid" id="dxfxdemid"></s:hidden>
						<s:hidden name="demid"></s:hidden>
						<s:hidden name="formid"></s:hidden>
						<s:hidden name="ipodemid"></s:hidden>
						<s:hidden name="ipoformid"></s:hidden>
						<s:hidden name="type"></s:hidden>
					</form>
					<s:form id="editForm" name="editForm" theme="simple"></s:form>
				</div>
			</div>

			<div title="已关闭项目" border="false" iconCls="icon-search" cache="false">
				<div class="itemList">
					<table width="100%">
						<tr class="header" style="border:1px solid #efefef">
							<td style="text-align: left;">项目名称</td>
							<td style="text-align: left;">项目类型</td>
							<td style="text-align: left;">项目负责人</td>
							<td style="text-align: left;">承做部门</td>
							<td style="text-align: left;">承做时间</td>
							<td style="text-align: left;">入库时间</td>
							<td style="text-align: left;">入库原因</td>
							<td style="text-align: left;">主要问题</td>
							<td style="text-align: left;">当前状态</td>
							<s:if test="dgxgzt=='true'">
								<td style="text-align: left;">操作</td>
							</s:if>
							
						</tr>
						<s:iterator value="closeList" status="ll">
							<tr class="content">
								<td onclick="editItem(<s:property value="INSTANCEID"/>,'<s:property value="TAG"/>','2')">
									<s:property value="CUSTOMERNAME" />
								</td>
								<td>
									<s:if test="TAG==1">
									股转挂牌
									</s:if>
									<s:elseif test="TAG==2">
									股转定增
									</s:elseif>
									<s:elseif test="TAG==3">
									并购重组
									</s:elseif>
									<s:else>
									<s:property value="TAG" />
									</s:else>
								</td>
								<td><s:property value="MANAGER" /></td>
								<td><s:property value="CZBM" /></td>
								<td><s:property value="CZSJ" /></td>
								<td><s:property value="ZZSJ" /></td>
								<td><s:property value="ZZYY" /></td>
								<td><s:property value="EXT3" /></td>
								<td><s:property value="EXT4" /></td>
								
									<s:if test="dgxgzt=='true'">
									<td>
										<a href="javascript:updzt(<s:property value="insid" />,<s:property value="formid" />,<s:property value="demid" />)" style="color:blue;"><u>修改状态</u></a>
									</td>
									</s:if>
								
							</tr>
						</s:iterator>
					</table>
					<div style="padding:5px">
						<s:if test="closeNum==0">
						</s:if>
						<s:else>
							<div id="bb"
								style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
						</s:else>
					</div>
					<form action="dg_zqb_project_index.action" method=post name=frmMain2 id=frmMain2>
						<s:hidden name="pageNumber2" id="pageNumber2"></s:hidden>
						<s:hidden name="pageSize2" id="pageSize2"></s:hidden>
						<s:hidden name="projectName" id="projectName"></s:hidden>
						<s:hidden name="xmjd" id="xmjd"></s:hidden>
						<s:hidden name="ssxq" id="ssxq"></s:hidden>
						<s:hidden name="customername" id="customername"></s:hidden>
						<s:hidden name="dgzt" id="dgzt"></s:hidden>
						<s:hidden name="ymid" id="ymid" value="0"></s:hidden>
						<s:hidden name="sssyb" id="sssyb"></s:hidden>
						<s:hidden name="cyrName" id="cyrName"></s:hidden>
						<s:hidden name="xmlx" id="xmlx"></s:hidden>
						
						<s:hidden name="tjgpbasemsgdemid" id="tjgpbasemsgdemid"></s:hidden>
						<s:hidden name="tjgpbasemsgformid" id="tjgpbasemsgformid"></s:hidden>
						<s:hidden name="dxfxformid" id="dxfxformid"></s:hidden>
						<s:hidden name="dxfxdemid" id="dxfxdemid"></s:hidden>
						<s:hidden name="demid"></s:hidden>
						<s:hidden name="formid"></s:hidden>
						<s:hidden name="ipodemid"></s:hidden>
						<s:hidden name="ipoformid"></s:hidden>
						<s:hidden name="type"></s:hidden>
					</form>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript">
	$(function(){
		$("#QRDG").attr("value",$("#dgzt").val()); 
	});
	</script>
	<script type="text/javascript">
	$(function(){
		$("#CUSTOMERNAME").val($("#customername").val());
		$("#XMJD").val($("#xmjd").val());
		$("#XMLX").val($("#xmlx").val());
		$("#SSXQ").val($("#ssxq").val());
	});
	</script>
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