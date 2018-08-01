<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" >
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
		$.ajaxSetup({
			async: false
		});
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
	    	var customername = $("#CUSTOMERNAME").val();
	    	var departmentname = $("#DEPARTMENTNAME").val();
	        var dzrqbegin = $("#DZRQBEGIN").val();
	        var dzrqend = $("#DZRQEND").val();
	        var xmlx = $("#XMLX").val();
	        var xylx = $("#XYLX").val();
	        var seachUrl = encodeURI("zqb_project_sx_accountedforindex.action?customername="+customername+"&departmentname="+departmentname+"&dzrqbegin="+dzrqbegin+"&dzrqend="+dzrqend+"&xmlx="+xmlx+"&xylx="+xylx);
	        window.location.href = seachUrl;
	    });
	    //复选/全选
        $("#chkAll").bind("click", function () {
        	$("[name =colname]:checkbox").attr("checked", this.checked);
        });
    });
		
	function edit(lcbh,lcbs,taskid){
		/* var formid = $("#formid").val();
		var demid = $("#demid").val();
		var pageUrl = "openFormPage.action?formid="+formid+"&demId="+demid+"&instanceId="+instanceid; */
		var pageUrl="loadProcessFormPage.action?actDefId="+lcbh+"&instanceId="+lcbs+"&excutionId="+lcbs+"&taskId="+taskid;
		art.dialog.open(pageUrl,{
			title:'财务入账信息',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:950,
			cache:false,
			lock: true,
			height:750, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});
	}
	function addItem(){
		/* var formid = $("#formid").val();
		var demid = $("#demid").val(); */
		//var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid;
		/* art.dialog.open(pageUrl,{
			title:'财务入账信息',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:930,
			cache:false,
			lock: true,
			height:500, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		}); */
		var cwrzServer = $("#cwrzServer").val();
		var pageUrl = 'processRuntimeStartInstance.action?actDefId='+cwrzServer;
		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = pageUrl;
	}
	
	function addAccount(customerno,kcustomername){
		/* var formid = $("#formid").val();
		var demid = $("#demid").val(); */
		//'processRuntimeStartInstance.action?actDefId='++'&CUSTOMERNAME='+encodeURI(customername)+'&CUSTOMERNO='+encodeURI(customerno)+'&JDMC='+encodeURI(jdmc);
		//var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid+"&CUSTOMERNO="+customerno+"&CUSTOMERNAME="+kcustomername+"&isDialogDisabled=1";
		/* art.dialog.open(pageUrl,{
			title:'财务入账信息',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:930,
			cache:false,
			lock: true,
			height:500, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		}); */
		var cwrzServer = $("#cwrzServer").val();
		var pageUrl = 'processRuntimeStartInstance.action?actDefId='+cwrzServer+'&CUSTOMERNAME='+encodeURI(kcustomername)+'&CUSTOMERNO='+encodeURI(customerno);
		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = pageUrl;
	}
	function delAccount(instanceid){
		$.messager.confirm('确认','确认删除?',function(result){
			if(result){
				$.post("zqb_project_sx_accountedfordel.action",{instanceid:instanceid},function(data){
					if(data=='success'){
						window.location.reload();
					}else{
						flag=false;
						alert("删除失败请重试!");
					}
				});
			}
		});
	}
	//导出
	function expkh(){
		var pageUrl = encodeURI("zqb_project_sx_accountedforexp.action");
		window.location.href = pageUrl; 
	}
	function remove(){
		$.messager.confirm('确认','确认删除?',function(result){
			if(result){
				var list = $('[name=colname]').length;
				var a=0;
				for( var n = 0; n < list; n++){
					var flag = new Boolean();
					if($('[name=colname]')[n].checked==false&&$('[name=colname]')[n].id!='chkAll'){
						a++;
						if(a==list){
							$.messager.alert('提示信息','请选择您要删除的行项目!','info');  
							return;
						}
					}
					if($('[name=colname]')[n].checked==true&&String($('[name=colname]')[n].id)!=String('chkAll')){
						var deleteUrl = "zqb_project_sx_accountedfordel.action";
						$.post(deleteUrl,{instanceid:$('[name=colname]')[n].id},function(data){
							if(data=='success'){
								window.location.reload();
							}else{
								flag=false;
								alert("删除失败请重试!");
							}
						});
					} 
					if(!flag){break;}
				}
			}
		});
	}
	
	//导出财务顾问业务监管报表
	function expCwrz(){
		var url= 'zqb_project_sx_setmonth.action';
		art.dialog.open(url,{
			title : '财务顾问业务监管报表',
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
	
	//导出项目收入汇总
	function expXmsrhz(){
		var pageUrl = "zqb_project_sx_xmsrhz.action";
		window.location.href = pageUrl; 
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
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav">
			<a class="easyui-linkbutton" plain="true" href="javascript:addItem();" iconCls="icon-add">添加</a>
			<a class="easyui-linkbutton" plain="true" href="javascript:window.location.reload();" iconCls="icon-reload">刷新</a>
			<a class="easyui-linkbutton" plain="true" href="javascript:expkh();" iconCls="icon-excel-exp">导出</a>
			<a class="easyui-linkbutton" plain="true" href="javascript:expCwrz();" iconCls="icon-excel-exp">导出财务顾问业务监管报表</a>
			<a class="easyui-linkbutton" plain="true" href="javascript:expXmsrhz();" iconCls="icon-excel-exp">导出项目收入汇总表</a>
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
										<td class="searchtitle">公司名称</td>
										<td class="searchdata">
											<input type='text' class='{maxlength:64,required:false,string:true}' style="width:280px" name='CUSTOMERNAME' id='CUSTOMERNAME' value=''></td>
										<td class="searchtitle">所属部门</td>
										<td class="searchdata">
											<input type='text' class='{maxlength:64,required:false,string:true}' style="width:180px" name='DEPARTMENTNAME' id='DEPARTMENTNAME' value=''></td>
										<td class="searchtitle">到账日期</td>
										<td class="searchdata">
											<input type='text' onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" class='{maxlength:64,required:false}' style="width:120px;" name='DZRQBEGIN' id='DZRQBEGIN' value=''>
											到
											<input type='text' onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" class='{maxlength:64,required:false}' style="width:120px;" name='DZRQEND' id='DZRQEND' value=''>
										</td>
									</tr>
									<tr>
										<td class="searchtitle">协议类型</td>
										<td class="searchdata">
											<select name='XYLX' id='XYLX'>
												<option value=''>-空-</option>
												<option value='改制融资推荐挂牌财务顾问协议'>改制融资推荐挂牌财务顾问协议</option>
												<option value='保密协议'>保密协议</option>
												<option value='定增财务顾问协议'>定增财务顾问协议</option>
												<option value='并购财务顾问协议'>并购财务顾问协议</option>
												<option value='推荐挂牌并持续督导协议书'>推荐挂牌并持续督导协议书</option>
												<option value='推荐挂牌并持续督导协议书之补充协议'>推荐挂牌并持续督导协议书之补充协议</option>
												<option value='财务顾问协议'>财务顾问协议</option>
												<option value='其他'>其他</option>
											</select>
										</td>
										<td class="searchtitle">项目类型</td>
										<td class="searchdata">
											<select name='XMLX' id='XMLX'>
												<option value=''>-空-</option>
												<option value='挂牌项目'>挂牌项目</option>
												<option value='督导项目'>督导项目</option>
												<option value='并购项目'>并购项目</option>
												<option value='定增项目'>定增项目</option>
												<option value='财务顾问'>财务顾问</option>
												<option value='其他'>其他</option>
											</select>
										</td>
										<td class="searchtitle"></td>
										<td class="searchdata">
											<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a>
										</td>
									</tr>
								</table>
							<td>
						<tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:2.5%;" class="header"><input type="checkbox" name="colname" id="chkAll" /></td>
					<td style="width:2.5%;">序号</td>
					<td style="width:5%;">月份</td>
					<td style="width:20%;">公司名称</td>
					<td style="width:10%;">协议类型</td>
					<td style="width:10%;">项目类型</td>
					<td style="width:5%;">到账金额(万元)</td>
					<td style="width:10%;">到账日期</td>
					<td style="width:10%;">部门</td>
					<td style="width:10%;">入账人</td>
					<td style="width:5%;">入账日期</td>
					<td style="width:5%;">审批状态</td>
					<td style="width:5%;">操作</td>
				</tr>
				<s:iterator value="accountedforlist" status="status">
					<tr class="cell">
						<td><input type="checkbox" id="<s:property value='INSTANCEID'/>" name="colname" value="<s:property value='INSTANCEID'/>" /></td>
						<td><s:property value="#status.count" /></td>
						<td><s:property value="MONTH" /></td>
						<td><a href="javascript:void(0)" onclick="edit('<s:property value='LCBH'/>','<s:property value='LCBS'/>','<s:property value='TASKID'/>')"><s:property value="CUSTOMERNAME" /></a></td>
						<td><s:property value="XYLX" /></td>
						<td><s:property value="XMLX" /></td>
						<td><s:property value="DZJE" /></td>
						<td><s:property value="DZRQ" /></td>
						<td><s:property value="DEPARTMENTNAME" /></td>
						<td><s:property value="LRR" /></td>
						<td><s:property value="CJSJ" /></td>
						<td><a href="javascript:void(0)" onclick="edit('<s:property value='LCBH'/>','<s:property value='LCBS'/>','<s:property value='TASKID'/>')"><s:property value="SPZT" /></a></td>
						<td>
							<a style="color:blue;" href="javascript:addAccount('<s:property value="CUSTOMERNO" />','<s:property value="KCUSTOMERNAME" />')">入账</a>&nbsp;
							<%-- |&nbsp;<a style="color:blue;" href="javascript:delAccount(<s:property value="INSTANCEID" />)">删除</a> --%>
						</td>
					</tr>
				</s:iterator>
			</table>
			<div style="display:none;">
				<%-- <s:hidden name="demid" id="demid"></s:hidden>
				<s:hidden name="formid" id="formid"></s:hidden> --%>
				<s:hidden name="cwrzServer" id="cwrzServer"></s:hidden>
			</div>
			<form action="zqb_project_sx_accountedforindex.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="customername" id="customername"></s:hidden>
				<s:hidden name="departmentname" id="departmentname"></s:hidden>
				<s:hidden name="dzrqbegin" id="dzrqbegin"></s:hidden>
				<s:hidden name="dzrqend" id="dzrqend"></s:hidden>
				<s:hidden name="xmlx" id="xmlx"></s:hidden>
				<s:hidden name="xylx" id="xylx"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south" style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		$("#CUSTOMERNAME").val($("#customername").val());
		$("#DZRQBEGIN").val($("#dzrqbegin").val());
		$("#DEPARTMENTNAME").val($("#departmentname").val());
		$("#DZRQEND").val($("#dzrqend").val());
		$("#XMLX").val($("#xmlx").val());
		$("#XYLX").val($("#xylx").val());
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