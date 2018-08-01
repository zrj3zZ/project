<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>项目管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css"
	href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="admin/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css"
	href="iwork_themes/easyui/gray/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.validate.js"></script>

<script type="text/javascript"
	src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
	mainFormValidator =  $("#ifrmMain").validate({});
	mainFormValidator.resetForm();
});
 $(function(){
	 var ymid=${ymid};
	 $('#mainFrameTab').tabs('select',ymid);
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
		$('#dd').pagination({
			total : <s:property value="finishNum"/>,
			pageNumber : <s:property value="pageNumber1"/>,
			pageSize : <s:property value="pageSize1"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMdd(pageNumber, pageSize);
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
		$('#xm').pagination({
			total : <s:property value="xmNum"/>,
			pageNumber : <s:property value="pageNumberXM"/>,
			pageSize : <s:property value="pageSizeXM"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitXM(pageNumber, pageSize);
			}
		});
		$("#search").click(function(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}
			var xmjd = $("#XMJD").val();
			var customername = $("#CUSTOMERNAME").val();
			var dgzt = $("#QRDG").val();
			var sssyb = $("#SSSYB").val();
			var cyrname = $("#CYRNAME").val();
			var seachUrl = encodeURI("xsd_zqb_project_indexNew.action?customername="+ customername
					+ "&xmjd=" + xmjd +"&dgzt=" + dgzt+"&sssyb="+sssyb+"&cyrName="+cyrname);// + "&projectName="+projectName );
			window.location.href = seachUrl;
		});
	});

	
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return;
	}
	function submitMdd(pageNumber, pageSize) {
		$("#pageNumber1").val(pageNumber);
		$("#pageSize1").val(pageSize);
		$("#frmMain1").submit();
		return;
	}
	function submitXM(pageNumber, pageSize) {
		$("#pageNumberXM").val(pageNumber);
		$("#pageSizeXM").val(pageSize);
		$("#frmMainXM").submit();
		return;
	}
	function submitMbb(pageNumber, pageSize) {
		$("#pageNumber2").val(pageNumber);
		$("#pageSize2").val(pageSize);
		$("#frmMain2").submit();
		return;
	}

	function addItem() {
		var xmsplcServer = $("#xmsplcServer").val();
		var url = 'processRuntimeStartInstance.action?actDefId='+xmsplcServer;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window
				.open(
						'form/loader_frame.html',
						target,
						'width='
								+ win_width
								+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	}

	/* function search() {
		var xmjd = $("#XMJD").val();
		var customername = $("#CUSTOMERNAME").val();
		var dgzt = $("#QRDG").val();
		var sssyb = $("#SSSYB").val();
		var cyrname = $("#CYRNAME").val();
		var seachUrl = encodeURI("xsd_zqb_project_indexNew.action?customername="+ customername
				+ "&xmjd=" + xmjd +"&dgzt=" + dgzt+"&sssyb="+sssyb+"&cyrName="+cyrname);// + "&projectName="+projectName );
		window.location.href = seachUrl;
		return;
	} */

	function showInfo(id) {
		var title = "";
		var height = 580;
		var width = 900;
		var pageurl = "loadVisitPage.action?formid=91&demId=22&instanceId="
				+ id;
		var dialogId = "projectItem";
		parent.openWin(title, height, width, pageurl, this.location, dialogId);
	}

	function showItem(id) {
		var title = "";
		var height = 580;
		var width = 900;
		var pageurl = "xsd_zqb_project_item.action?projectNo=" + id;
		var dialogId = "projectItem";
		parent.openWin(title, height, width, pageurl, this.location, dialogId);
	}

	function editItem1(instanceid, projectNo) {
		var formid = 91;
		var demId = 22;
		var url = 'openFormPage.action?instanceId=' + instanceid + '&formid='
				+ formid + '&demId=' + demId+'&PROJECTNO='+projectNo;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window
				.open(
						'form/loader_frame.html',
						target,
						'width='
								+ win_width
								+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function editItem2(lcbh,lcbs,taskid) {
		var url="loadProcessFormPage.action?actDefId="+ lcbh + "&instanceId="+ lcbs + "&excutionId="+ lcbs +"&taskId="+taskid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window
				.open(
						'form/loader_frame.html',
						target,
						'width='
								+ win_width
								+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	function closeItem() {
			try{
				 $.messager.confirm('确认','确认要关闭?',function(result){ 
					 	if(result){
					var list = $('[name=chk_list]').length;
					var a=0;
					var ErrNum = 0;	
					 for( var n = 0; n < list; n++){
						 if($('[name=chk_list]')[n].checked==false&&$('[name=chk_list]')[n].id!='chkAll'){
							 a++;
							 if(a==list-1){
							  	$.messager.alert('提示信息','请选择您要关闭的项目!','info');  
								return;
							 	}
						 	}
						 	if($('[name=chk_list]')[n].checked==true&&$('[name=chk_list]')[n].id!=String('chkAll')){
						 	{
						 		var pageUrl = "xsd_zqb_project_closeitem.action";
							$.post(pageUrl,{instanceid:$('[name=chk_list]')[n].id},function(data){ 
				       			if(data=='success'){
				       				window.location.reload();
				       			}else{
				       				alert("项目关闭失败，请重试");
				       			}
				     		 });
							
						 	}
						}
					}
				}});
				}catch(e){}
	}
	function getXmJCy() {
		var title = "";
		var height = 580;
		var width = 900;
		var pageurl = "xsd_zqb_project_xmcy.action";
		var dialogId = "xmcyItem";
		parent.openWin(title, height, width, pageurl, this.location, dialogId);
	}
	function removeProject(instanceid, projectNo) {
		if (confirm('确定删除当前项目吗？')) {
			var pageUrl = "xsd_zqb_project_removeProject.action?instanceid="
					+ instanceid + "&projectNo=" + projectNo;
			$.post(pageUrl, {}, function(data) {
				if (data == 'success') {
					window.location.reload();
				} else {
					alert("删除项目失败");
					;
				}
			});
		}
	}
	function removeCxddProject(instanceid, projectNo) {
		if (confirm('确定删除当前项目吗？')) {
			var pageUrl = "xsd_zqb_project_removeCxddProject.action?instanceid="
					+ instanceid + "&projectNo=" + projectNo;
			$.post(pageUrl, {}, function(data) {
				if (data == 'success') {
					window.location.reload();
				} else {
					alert(data);
				}
			});
		}
	}
	function editPJ(instanceid,projectno,groupid){	
	 	var formid = 150;
		var demId = 65;
		var url;
	    if(instanceid!='0'){
	      url= 'openFormPage.action?formid='
				+ formid + '&demId=' + demId+"&instanceId="+instanceid+"&PROJECTNO="+projectno+"&GROUPID="+groupid;
	    }else{
	      url = 'createFormInstance.action?formid='
				+ formid + '&demId=' + demId+"&PROJECTNO="+projectno+"&GROUPID="+groupid;
	    }
		var target = "_blank";
		var win_width = window.screen.width;
		art.dialog.open(url,{
			title : '评价表单',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 580,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			close : function() {
				window.location.reload();
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
		/* var pageUrl = "createFormInstance.action?formid=114&demId=50";
		art.dialog.open(pageUrl,{
			title : '持续督导分派表单',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 580,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			close : function() {
				window.location.reload();
			}
		}); */
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
				 		var pageUrl = "xsd_zqb_project_finish_item.action";
						$.post(pageUrl,{instanceid:$('[name=chk_list]')[n].id},function(data){ 
			       			if(data=='success'){
			       				$.messager.confirm('确认','是否分派持续督导专员?',function(result){
			       					if(result){
			       						$.post("zqb_cxdd_check.action",{instanceid:cxddInstanceid},function(data){ 
			    			       			if(data=='success'){
		    			       						var pageUrl = "createFormInstance.action?formid=114&demId=50";
		    			       						art.dialog.open(pageUrl,{
		    			       							title : '持续督导分派表单',
		    			       							loadingText : '正在加载中,请稍后...',
		    			       							bgcolor : '#999',
		    			       							rang : true,
		    			       							width : 1100,
		    			       							cache : false,
		    			       							lock : true,
		    			       							height : 580,
		    			       							iconTitle : false,
		    			       							extendDrag : true,
		    			       							autoSize : false,
		    			       							close : function() {
		    			       								window.location.reload();
		    			       							}
		    			       						});
			    			       			}else{
			    			       				var pageUrl = "openFormPage.action?formid=114&demId=50&instanceId="+data;
			    			       				art.dialog.open(pageUrl,{
	    			       							title : '持续督导分派表单',
	    			       							loadingText : '正在加载中,请稍后...',
	    			       							bgcolor : '#999',
	    			       							rang : true,
	    			       							width : 1100,
	    			       							cache : false,
	    			       							lock : true,
	    			       							height : 580,
	    			       							iconTitle : false,
	    			       							extendDrag : true,
	    			       							autoSize : false,
	    			       							close : function() {
	    			       								window.location.reload();
	    			       							}
	    			       						});
			    			       			}
			    		     		 });
			       						/* var pageUrl = "createFormInstance.action?formid=114&demId=50";
			       						art.dialog.open(pageUrl,{
			       							title : '持续督导分派表单',
			       							loadingText : '正在加载中,请稍后...',
			       							bgcolor : '#999',
			       							rang : true,
			       							width : 1100,
			       							cache : false,
			       							lock : true,
			       							height : 580,
			       							iconTitle : false,
			       							extendDrag : true,
			       							autoSize : false,
			       							content : pageUrl,
			       							close : function() {
			       								window.location.reload();
			       							}
			       						}); */
			       					}else{
			       						window.location.reload();
			       					}
			       				});
			       			}else{
			       				alert("项目转入持续督导阶段失败，请重试");
			       			}
		     		 });
				 	}
				}
			}}});
		}
	function expPro(){
		var url;
	      url= 'xsd_zqb_project_exppro_index.action';
		art.dialog.open(url,{
			title : '项目信息汇总',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 600,
			iconTitle : false,
			extendDrag : true,
			autoSize : false
		});
	}
	
	function dgCommit(){
		   $.messager.confirm('确认','是否确认提交底稿?',function(result){ 
			 	if(result){
			var list = $('[name=chk_list]').length;
			var a=0;
			var ErrNum = 0;	
			var temp=new Array();
			 for( var n = 0; n < list; n++){
				 if($('[name=chk_list]')[n].checked==false&&$('[name=chk_list]')[n].id!='chkAll'){
					 a++;
					 if(a==list-1){
					  	$.messager.alert('提示信息','请选择您要确认底稿的项目!','info');  
						return;
					 }
				 }else if($('[name=chk_list]')[n].checked==true&&$('[name=chk_list]')[n].id!='chkAll'){
					temp.push($('[name=chk_list]')[n].id);
				}
			}
			temp=temp.join(',');
			var pageUrl = "xsd_zqb_project_finish_dg.action";
			$.post(pageUrl,{temp:temp},function(data){ 
	       		if(data=='success'){
	       			window.location.reload();
	       		}else{
	       			alert("项目转入持续督导阶段失败，请重试");
	       		}
	     	});
			}
		});
		}
	function readLc(lcbh,stepid,prcid,rwid,yxid,lcbs){
		var pageUrl = "processInstanceMornitor.action?actDefId="+lcbh
		+"&actStepDefId="+stepid+"&prcDefId="+prcid+"&taskId="+rwid+"&instanceId="+lcbs+"&excutionId="+yxid;
		art.dialog.open(pageUrl,{ 
					id:'Category_show',
					cover:true, 
					title:'流程跟踪',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1000,
					cache:false,
					lock: true,
					height:580, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false
				});
	}
	
	function showDaily(projectno){
		var url = 'xsd_zqb_project_showDaily.action?projectNo='+projectno;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	}
</script>
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
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:addItem();" class="easyui-linkbutton"
				plain="true" iconCls="icon-add">添加项目</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton"
				plain="true" iconCls="icon-reload">刷新</a>
			<a href="javascript:zrcxddItem();" class="easyui-linkbutton"
				plain="true" iconCls="icon-add">转入持续督导阶段</a>
			<s:if test="ISVIEWDG==true">
			<a href="javascript:dgCommit();" class="easyui-linkbutton"
				plain="true" iconCls="icon-add">确认底稿</a>
			</s:if>
			<a href='javascript:closeItem();' class="easyui-linkbutton"
				plain="true" iconCls="icon-remove">关闭项目</a>
			<a href="javascript:expPro();" class="easyui-linkbutton"
				plain="true" iconCls="icon-excel-exp">导出</a>
		</div>
		<div style="padding:5px">
			<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
			<form id="ifrmMain" name="ifrmMain">
				<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tbody>
						<tr>
							<td style="padding-top:10px;padding-bottom:10px;">
								<table width="100%" cellspacing="0" cellpadding="0" border="0">
									<tbody>
										<tr>
											<td class="searchtitle" style="text-align:right;">客户名称</td>
											<td class="searchdata"><input id="CUSTOMERNAME" class="{maxlength:128,required:false}" type="text" name="CUSTOMERNAME" style="width:100px;" value=""></td>
											<td class="searchtitle" style="text-align:right;">项目阶段</td>
											<td class="searchdata"><input id="XMJD" class="{maxlength:128,required:false}" type="text" name="XMJD" style="width:100px;" value=""></td>
											<s:if test="IsViewSearch==true">
											<td class="searchtitle" style="text-align:right;">承做部门</td>
											<td class="searchdata"><input id="SSSYB" class="{maxlength:128,required:false,string:true}" type="text" name="SSSYB" style="width:100px;" value="${sssyb}"></td>
											<td class="searchtitle" style="text-align:right;">参与人姓名</td>
											<td class="searchdata"><input id="CYRNAME" class="{maxlength:128,required:false,string:true}" type="text" name="CYRNAME" style="width:100px;" value="${cyrName}"></td>
											</s:if>
											<s:if test="ISVIEWDG==true">
											<td class="searchtitle" style="text-align:right;">底稿状态</td>
											<td class="searchdata">
												<select   name='QRDG'  id='QRDG' ><option value=''>-未选择-</option>
												<option value='1'>已完成</option>
												<option value='2'>未完成</option>
												</select>
											</td>
											</s:if>
										</tr>
									</tbody>
								</table>
							</td>
							<td valign="bottom" style="padding-bottom:5px;"><a
								id="search" class="easyui-linkbutton l-btn"
								href="javascript:search();" icon="icon-search">查询</a></td>
						</tr>
					</tbody>
				</table>
				</form>
			</div>
		</div>
	</div>
	<div region="center" border="false">
		<div id="mainFrameTab" style="border:0px" class="easyui-tabs"
			fit="true">
			<div title="正在执行的项目" border="false" style="border:0px"
				iconCls="icon-search" cache="false">
				<div class="itemList">
					<table width="100%">
						<tr class="header" style="border:1px solid #efefef">
							<td style="text-align:left;width:20px;"><input id="chkAll"
								type="checkbox" name="chk_list" onclick="selectAll()"></td>
							<td style="text-align: left;">客户名称</td>
							<td style="text-align: left;">现场负责人</td>
							<td style="text-align: left;">项目金额（万元）</td>
							<td style="text-align: left;">未收金额（万元）</td>
							<td style="text-align: left;">阶段实收金额（万元）</td>
							<td style="text-align: left;">项目阶段</td>
							<td style="text-align: left;">项目审批状态</td>
							<td style="text-align: left;">所属事业部</td>
							<td style="text-align: left;">评价</td>
							<td style="text-align: left;">项目日报</td>
						</tr>
						<s:iterator value="runList" status="ll">
							<tr class="content">


								<s:if
									test="#ll.index-1<0||PROJECTNO!=runList[#ll.index-1].PROJECTNO">
									<TD style="text-align:left;width:20px;"
										rowspan="<s:property value="NUM"/>"><input
										type="checkbox" name="chk_list"
										id="<s:property value="INSTANCEID"/>"></TD>
									<s:if test="ISSTEPID==true&&XMSPZT=='审批通过'">
									<td
										onclick="editItem1(<s:property value="INSTANCEID"/>,'<s:property value="PROJECTNO"/>')"
										rowspan="<s:property value="NUM"/>"><s:property
											value="CUSTOMERNAME" /> <s:if test="ISYD==false">
											<img src="../../../iwork_img/new0.gif" width="20" height="10" />
										</s:if></td>
									</s:if>
									<s:else>
									<td
										onclick="editItem2('<s:property value="LCBH"/>',<s:property value="LCBS"/>,<s:property value="TASKID"/>)"
										rowspan="<s:property value="NUM"/>"><s:property
											value="CUSTOMERNAME" /> <s:if test="ISYD==false">
											<img src="../../../iwork_img/new0.gif" width="20" height="10" />
										</s:if></td>
									</s:else>
									
									<td rowspan="<s:property value="NUM"/>"><s:property
											value="MANAGER" /></td>
									<td rowspan="<s:property value="NUM"/>"><s:property
											value="HTJE" /></td>
									<td rowspan="<s:property value="NUM"/>"><s:property
											value="WSJE" /></td>
								</s:if>
								<td><s:property value="SSJE" /></td>
								<td><s:property value="RWJD" /></td>
								<td><a
									href="javascript:readLc('<s:property value="LCBH"/>','<s:property value="STEPID"/>',<s:property value="PRCID"/>,<s:property value="TASKID"/>,<s:property value="LCBS"/>,<s:property value="LCBS"/>)"><s:property value="XMSPZT" /></a></td>
								<td><s:property value="JDFZR" /></td>
								<td><a
									href="javascript:editPJ('<s:property value="PJINSID"/>','<s:property value="PROJECTNO"/>','<s:property value="GROUPID"/>')"><s:property
											value="PJ" /></a></td>
								<%-- <s:if test="STEPID=='usertask5'&&(XMSPZT!='驳回'||XMSPZT!='审批通过')">
								<td><a
									href="javascript:editItem1(<s:property value="INSTANCEID"/>,'<s:property value="PROJECTNO"/>')">相关问题</a></td>
									</s:if>
									<s:else>
								<td><a
									href="javascript:editItem2('<s:property value="LCBH"/>',<s:property value="LCBS"/>,<s:property value="TASKID"/>)">相关问题</a></td>
									</s:else> --%>
								<s:if
									test="#ll.index-1<0||PROJECTNO!=runList[#ll.index-1].PROJECTNO">
								<td rowspan="<s:property value="NUM"/>" ><a
									href="javascript:showDaily('<s:property value="PROJECTNO"/>')">查看项目日报</a></td>
								</s:if>
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
					<form action="xsd_zqb_project_indexNew.action" method=post name=frmMain
						id=frmMain>
						<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
						<s:hidden name="pageSize" id="pageSize"></s:hidden>
						<s:hidden name="projectName" id="projectName"></s:hidden>
						<s:hidden name="xmjd" id="xmjd"></s:hidden>
						<s:hidden name="customername" id="customername"></s:hidden>
						<s:hidden name="dgzt" id="dgzt"></s:hidden>
						<s:hidden name="ymid" id="ymid" value="0"></s:hidden>
					</form>
				</div>
			</div>

			<div title="持续督导项目" border="false" iconCls="icon-search"
				cache="false">
				<div class="itemList">
					<table width="100%">
						<tr class="header" style="border:1px solid #efefef">
							<td style="text-align: left;">客户名称</td>
							<td style="text-align: left;">现场负责人</td>
							<td style="text-align: left;">项目金额（万元）</td>
							<td style="text-align: left;">未收金额（万元）</td>
							<td style="text-align: left;">阶段实收金额（万元）</td>
							<td style="text-align: left;">项目阶段</td>
							<td style="text-align: left;">项目审批状态</td>
							<td style="text-align: left;">所属事业部</td>
							<td style="text-align: left;">评价</td>
							<td style="text-align: left;">相关问题</td>
						</tr>
						<s:iterator value="finishList" status="ll">
							<tr class="content">
								<s:if
									test="#ll.index-1<0||PROJECTNO!=finishList[#ll.index-1].PROJECTNO">
									<td
										onclick="editItem1(<s:property value="INSTANCEID"/>,'<s:property value="PROJECTNO"/>')"
										rowspan="<s:property value="NUM"/>"><s:property
											value="CUSTOMERNAME" /> <s:if test="ISYD==false">
											<img src="../../../iwork_img/new0.gif" width="20" height="10" />
										</s:if></td>
									<td rowspan="<s:property value="NUM"/>"><s:property
											value="MANAGER" /></td>
									<td rowspan="<s:property value="NUM"/>"><s:property
											value="HTJE" /></td>
									<td rowspan="<s:property value="NUM"/>"><s:property
											value="WSJE" /></td>
								</s:if>

								<td><s:property value="SSJE" /></td>
								<td><s:property value="RWJD" /></td>
								<td><a
									href="javascript:readLc('<s:property value="LCBH"/>','<s:property value="STEPID"/>',<s:property value="PRCID"/>,<s:property value="TASKID"/>,<s:property value="LCBS"/>,<s:property value="LCBS"/>)"><s:property value="XMSPZT" /></a></td>
								<td><s:property value="JDFZR" /></td>
								<td><a
									href="javascript:editPJ('<s:property value="PJINSID"/>','<s:property value="PROJECTNO"/>','<s:property value="GROUPID"/>')"><s:property
											value="PJ" /></a></td>
								<s:if test="STEPID=='usertask5'&&(XMSPZT!='驳回'||XMSPZT!='审批通过')">
								<td><a
									href="javascript:editItem1(<s:property value="INSTANCEID"/>,'<s:property value="PROJECTNO"/>')">相关问题</a></td>
									</s:if>
									<s:else>
								<td><a
									href="javascript:editItem2('<s:property value="LCBH"/>',<s:property value="LCBS"/>,<s:property value="TASKID"/>)">相关问题</a></td>
									</s:else>

							</tr>
						</s:iterator>
					</table>
					<div style="padding:5px">
						<s:if test="finishNum==0">
						</s:if>
						<s:else>
							<div id="dd"
								style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
						</s:else>
					</div>
					<form action="xsd_zqb_project_indexNew.action" method=post name=frmMain1
						id=frmMain1>
						<s:hidden name="pageNumber1" id="pageNumber1"></s:hidden>
						<s:hidden name="pageSize1" id="pageSize1"></s:hidden>
						<s:hidden name="projectName" id="projectName"></s:hidden>
						<s:hidden name="xmjd" id="xmjd"></s:hidden>
						<s:hidden name="customername" id="customername"></s:hidden>
						<s:hidden name="startDate" id="startDate"></s:hidden>
						<s:hidden name="dgzt" id="dgzt"></s:hidden>
						<s:hidden name="ymid" id="ymid" value="1"></s:hidden>
					</form>
				</div>
			</div>
			<div title="已关闭项目" border="false" iconCls="icon-search" cache="false">
				<div class="itemList">
					<table width="100%">
						<tr class="header" style="border:1px solid #efefef">
							<td style="text-align: left;">客户名称</td>
							<td style="text-align: left;">现场负责人</td>
							<td style="text-align: left;">项目金额（万元）</td>
							<td style="text-align: left;">未收金额（万元）</td>
							<td style="text-align: left;">阶段实收金额（万元）</td>
							<td style="text-align: left;">项目阶段</td>
							<td style="text-align: left;">项目审批状态</td>
							<td style="text-align: left;">所属事业部</td>
						</tr>
						<s:iterator value="closeList" status="ll">
							<tr class="content">
								<s:if
									test="#ll.index-1<0||PROJECTNO!=closeList[#ll.index-1].PROJECTNO">
									<td
										onclick="editItem1(<s:property value="INSTANCEID"/>,'<s:property value="PROJECTNO"/>')"
										rowspan="<s:property value="NUM"/>"><s:property
											value="CUSTOMERNAME" /> <s:if test="ISYD==false">
											<img src="../../../iwork_img/new0.gif" width="20" height="10" />
										</s:if></td>
									<td rowspan="<s:property value="NUM"/>"><s:property
											value="MANAGER" /></td>
									<td rowspan="<s:property value="NUM"/>"><s:property
											value="HTJE" /></td>
									<td rowspan="<s:property value="NUM"/>"><s:property
											value="WSJE" /></td>
								</s:if>
								<td><s:property value="SSJE" /></td>
								<td><s:property value="RWJD" /></td>
								<td><a
									href="javascript:readLc('<s:property value="LCBH"/>','<s:property value="STEPID"/>',<s:property value="PRCID"/>,<s:property value="TASKID"/>,<s:property value="LCBS"/>,<s:property value="LCBS"/>)"><s:property value="XMSPZT" /></a></td>
								<td><s:property value="JDFZR" /></td>
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
					<form action="xsd_zqb_project_indexNew.action" method=post name=frmMain2
						id=frmMain2>
						<s:hidden name="pageNumber2" id="pageNumber2"></s:hidden>
						<s:hidden name="pageSize2" id="pageSize2"></s:hidden>
						<s:hidden name="projectName" id="projectName"></s:hidden>
						<s:hidden name="xmjd" id="xmjd"></s:hidden>
						<s:hidden name="customername" id="customername"></s:hidden>
						<s:hidden name="startDate" id="startDate"></s:hidden>
						<s:hidden name="dgzt" id="dgzt"></s:hidden>
						<s:hidden name="ymid" id="ymid" value="2"></s:hidden>
					</form>
				</div>
			</div>
			<!-- <div title="项目及成员信息" border="false" iconCls="icon-search"
				cache="false">
				<iframe id="mainFrame" name="mainFrame" scrolling="no"
					frameborder="0" src="xsd_zqb_project_getXM.action"
					style="width:100%;height:100%;"></iframe>
			</div> -->
		</div>
	</div>
	<s:hidden name="xmsplcServer"></s:hidden>
<script type="text/javascript">
	$(function(){
		$("#QRDG").attr("value",$("#dgzt").val()); 
	});
	</script>
	<script type="text/javascript">
	$(function(){
		$("#CUSTOMERNAME").val($("#customername").val());
		$("#XMJD").val($("#xmjd").val());
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