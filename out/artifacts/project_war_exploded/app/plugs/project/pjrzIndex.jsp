<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
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
	    	var customername = $("#CUSTOMERNAME").val();
	    	var rzrq = $("#RZRQ").val();
	    	var xmlx = $("#XMLX").val();
	    	var ordertype = $("#ORDERTYPE").val();
			var seachUrl = encodeURI("zqb_project_getPjSettlementSheet.action?customername="+ customername+"&rzrq="+rzrq+"&xmlx="+xmlx+"&ordertype="+ordertype);
			window.location.href = seachUrl;
	    });
	});
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return;
	}
	
	function setRzje(formid,demId,customerno,customername){
		var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demId+"&CUSTOMERNO="+customerno+"&CUSTOMERNAME="+customername;
		seturl = encodeURI(pageUrl);
		art.dialog.open(seturl,{
			id:'iformPjrz',
			title:'项目结算信息管理',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:580, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function expProXmjsSptg(){
		var url= 'zqb_project_setmonth.action?status=1';
		art.dialog.open(url,{
			title : '项目结算信息管理',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 600,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function expProXmjsWsp(){
		var url= 'zqb_project_setmonth.action?status=0';
		art.dialog.open(url,{
			title : '项目结算信息管理',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 600,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function expProYwhz(){
		var url= 'zqb_project_ywhzsetmonth.action';
		art.dialog.open(url,{
			title : '推荐业务收入情况汇总',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 600,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function expProYdqy(){
		var url= 'zqb_project_ydqysetmonth.action';
		art.dialog.open(url,{
			title : '月度新签约项目统计',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 600,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function expProJsfc(){
		var url= 'zqb_project_jsfcsetmonth.action';
		art.dialog.open(url,{
			title : '定增项目的结算分成单',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 600,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function addItem(){
		var formid = $("#xmjsformid").val();
		var demid = $("#xmjsid").val();
		var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid;
		art.dialog.open(pageUrl,{
			id:'iformPjrz',
			title:'项目结算信息管理',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:580, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});
		
	}
	function edit(instanceid){
		var formid = $("#xmjsformid").val();
		var demid = $("#xmjsid").val();
		var pageUrl = "openFormPage.action?formid="+formid+"&demId="+demid+"&instanceId="+instanceid;
		art.dialog.open(pageUrl,{
			title:'项目结算信息管理',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:580, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});
	}
	function deleteformdata(instanceid) {
		$.messager.confirm('确认','确认删除?',function(result){
			if(result){
				var deleteUrl = encodeURI("zqb_project_deleteformdate.action?instanceid="+ instanceid);
				$.post(deleteUrl,function(data){
					if(data=='success'){
		   				window.location.reload();
		   			}else{
		   				alert(data);
		   			}
				});
			}
		});
	}
	
	function expProWorddz(instanceid) {
		/* $.messager.confirm('确认','确认导出项目结算单?',function(result){
			if(result){ */
				var pageUrl = "zqb_project_expproworddz.action?instanceid="+instanceid;
				$("#ifrmMain").attr("action", pageUrl);
				$("#ifrmMain").submit();
		/* 	}
		}); */
	}
	function expProWord(instanceid) {
		/* $.messager.confirm('确认','确认导出项目结算单?',function(result){
			if(result){ */
				var pageUrl = "zqb_project_expproword.action?instanceid="+instanceid;
				$("#ifrmMain").attr("action", pageUrl);
				$("#ifrmMain").submit();
		/* 	}
		}); */
	}
	
	function getLog(){
		var url = 'zqb_pjLogList.action';
		var target = "_blank";
		var win_width = window.screen.width-50;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes,location=no');
		page.location=url;
		page.document.title = title; 
	}
	
	function setLocking(instanceid){
		var deleteUrl = "pjSetLocking.action";
		$.post(deleteUrl,{instanceid:instanceid},function(data){
      			if(data=='success'){
      				window.location.reload();
      			}else{
      				alert(data);
      			}
		});
	}
	
	function cleanLocking(instanceid){
		var deleteUrl = "pjCleanLocking.action";
		$.post(deleteUrl,{instanceid:instanceid},function(data){
      			if(data=='success'){
      				window.location.reload();
      			}else{
      				alert(data);
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
.icon-lock{
	background:url('../../iwork_img/km/icon-lock.png') no-repeat;
}
.icon-unlock{
	background:url('../../iwork_img/km/icon-unlock.png') no-repeat;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<a href="javascript:expProXmjsSptg();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出挂牌已审批结算</a>
			<a href="javascript:expProXmjsWsp();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出挂牌未审批结算</a>
			<a href="javascript:expProYwhz();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出推荐业务收入情况汇总表</a>
			<a href="javascript:expProYdqy();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出月度新签约项目统计表</a>
			<a href="javascript:expProJsfc();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出定增项目结算分成单</a>
			<a href="javascript:getLog();" class="easyui-linkbutton" style="float: right" plain="true" iconCls="icon-search">项目日志</a>
		</div>
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px;text-align:center;">
				<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">客户名称</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:128,required:false,string:true}' style="width:100px"
											name='CUSTOMERNAME' id='CUSTOMERNAME' value='${customername}'></td>
										
										<td class="searchtitle">项目类型</td>
										<td class="searchdata">
											<select name='XMLX' id='XMLX'>
													<option value=''>-空-</option>
													<option value='挂牌'>挂牌</option>
													<option value='定增'>定增</option>
													<option value='并购'>并购</option>
													<option value='督导'>督导</option>
													<option value='做市'>做市</option>
													<option value='其他'>其他</option>
												</select></td>
											</td>
										
										<td class="searchtitle">入账日期</td>
										<td class="searchdata"><input type='text' onfocus="WdatePicker({dateFmt:'yyyy-MM'});"
												class='{maxlength:64,required:false}' style="width:100px"
												name='RZRQ' id='RZRQ' value='${rzrq}'></td>
										<td class="searchtitle">排序方式</td>
										<td class="searchdata">
												<select name='ORDERTYPE' id='ORDERTYPE'>
													<option value=''>入账时间</option>
													<option value='2'>分配信息录入时间</option>
													<option value='4'>审批通过时间</option>
												</select></td>
									</tr>
								</table>
							<td>
							<td valign='bottom' style='padding-bottom:5px;'><a
								id="search" class="easyui-linkbutton" icon="icon-search"
								href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px">
			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:5%;">序号</td>
					<td style="width:30%;">客户名称</td>
					<td style="width:15%;">项目类型</td>
					<td style="width:7%;">税后金额(元)</td>
					<td style="width:6%;">入账金额(万元)</td>
					<td style="width:6%;">入账日期</td>
					<td style="width:7%;">审批状态</td>
					<td style="width:15%;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作</td>
				</tr>
				<s:iterator value="list" status="status">
					<tr class="cell">
						<td><s:property value="#status.count" /></td>
						<td><s:property value="CUSTOMERNAME" /></td>
						<td><s:property value="XMLX" /></td>
						<td><s:property value="SHJE" /></td>
						<td><s:property value="RZJE" /></td>
						<td><s:if test="RZRQ=='1900-01-01'"></s:if><s:else><s:property value="RZRQ" /></s:else></td>
						<s:if test="ZBSPZT=='未审批'">
							<td><s:property value="ZBSPZT" /></td>
						</s:if>
						<s:elseif test="ZBSPZT=='未关联'">
							<s:if test="XMLX=='定增'">
								<td><a href="javascript:edit(<s:property value="INSTANCEID"/>)"><s:property value="ZBSPZT" /></a></td>
							</s:if>
							<s:else>
								<td><s:property value="ZBSPZT" /></td>
							</s:else>
						</s:elseif>
						<s:else>
							<td><a href="javascript:readLc('<s:property value="ZBLCBH"/>','<s:property value="ZBSTEPID"/>',<s:property value="ZBPDFID"/>,<s:property value="ZBTASKID"/>,<s:property value="ZBLCBS"/>,<s:property value="ZBLCBS"/>)"><s:property value="ZBSPZT" /></a></td>
						</s:else>
						<td>
							<a href="javascript:setRzje(<s:property value="xmjsformid"/>,<s:property value="xmjsid"/>,'<s:property value="CUSTOMERNO"/>','<s:property value="CUSTOMERNAME"/>')" style="color:blue;"><u>入账</u></a>
							<s:if test="INSTANCEID!=NULL">&nbsp;
							|&nbsp;<a href="javascript:deleteformdata('<s:property value="INSTANCEID"/>')" style="color:blue;"><u>删除</u></a>&nbsp;
								<s:if test="XMLX=='挂牌'||XMLX=='定增'">
								|&nbsp;<a href="javascript:expProWord<s:if test="XMLX=='定增'">dz</s:if>(<s:property value="INSTANCEID"/><s:if test="XMLX=='定增'">,'<s:property value="PROJECTNO"/>'</s:if>)" style="color:blue;"><u>导出项目结算单</u></a>
								</s:if>
							</s:if>
							<s:if test="(LOCKED==1||LOCKED==2)&&PJINSTANCEID!=null">
								&nbsp;|&nbsp;<a href="javascript:void(0);" onclick="cleanLocking(<s:property value='PJINSTANCEID'/>)" class="easyui-linkbutton" plain="true" iconCls="icon-unlock">解锁</a>
							</s:if>
							<s:if test="LOCKED==0&&PJINSTANCEID!=null&&XMLX=='挂牌'">
								&nbsp;|&nbsp;<a href="javascript:void(0);" onclick="setLocking(<s:property value='PJINSTANCEID'/>)" class="easyui-linkbutton" plain="true" iconCls="icon-lock">锁定</a>
							</s:if>
						</td>
					</tr>
				</s:iterator>
			</table>

			<form action="zqb_project_getPjSettlementSheet.action" method="post" name="frmMain"	id="frmMain">
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="customername" id="customername"></s:hidden>
				<s:hidden name="xmjsformid" id="xmjsformid"></s:hidden>
				<s:hidden name="xmjsid" id="xmjsid"></s:hidden>
				<s:hidden name="rzrq" id="rzrq"></s:hidden>
				<s:hidden name="xmlx" id="xmlx"></s:hidden>
				<s:hidden name="ordertype" id="ordertype"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south"
		style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-left:10px;"
		border="false">
		<div style="padding:1px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		$("#ORDERTYPE").attr("value",$("#ordertype").val());
		$("#XMLX").attr("value",$("#xmlx").val());
	});
	</script>
</body>
</html>

<script language="JavaScript">
	jQuery.validator.addMethod("string", function(value, element) {
		var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
		var patrn=/[“”`~!#$%^&*+<>?"{},;'[]（）—。[\]]/im;
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
