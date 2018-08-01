<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>持续督导分派</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
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
	$(function() {
		$('#pp').pagination({
			total : <s:property value="totalNum"/>,
			pageNumber : <s:property value="pageNumber"/>,
			pageSize : <s:property value="pageSize"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMsg(pageNumber, pageSize);
			}
		});
	});

	function addProject(customername, customerno, username, tel) {
		var formid = 91;
		var demId = 22;
		var url = 'createFormInstance.action?formid=' + formid + '&demId='
				+ demId + '&CUSTOMERNAME=' + encodeURI(customername)
				+ '&CUSTOMERNO=' + encodeURI(customerno) + '&KHLXR='
				+ encodeURI(username) + '&KHLXDH=' + tel;
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

	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return;
	}
	$(function() {
		//查询
		$("#search").click(
				function() {
					var valid = mainFormValidator.form(); //执行校验操作
					if(!valid){
						return false;
					}
					var khbh = $("#KHBH").val();
					var khmc = $("#KHMC").val();
					var cxddzy = $("#CXDDZY").val();
					var zzcxdd = $("#ZZCXDD").val();
					var zqdm = $("#GPDM").val();
					var rule = $("#rule").val();
					var col = $("#col").val();
					var seachUrl = encodeURI("loadFP.action?khmc=" + khmc
							+ "&khbh=" + khbh + "&cxddzy=" + cxddzy + "&zzcxdd=" + zzcxdd + "&zqdm=" + zqdm + "&rule=" + rule + "&col=" + col);
					window.location.href = seachUrl;
				});
		$("#chkAll").bind("click", function() {
			$("[name =colname]:checkbox").attr("checked", this.checked);
		});

	});

	function updateFP(instanceid,khfzr) {
		var ispurview = $("#ISPURVIEW").val();
		var pageUrl = "";		
		if (ispurview == 'true') {
			pageUrl = "openFormPage.action?formid=114&demId=50&instanceId="
					+ instanceid;
		}
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
	function addItem() {
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
				//window.top.frames["deskframe"].location.reload();
			}
		});

	}

	function remove() {
		$.messager.confirm('确认','确认删除?',function(result) {
			if (result) {
				var list = $('[name=colname]').length;var a = 0;
				for (var n = 0; n < list; n++) {
					if ($('[name=colname]')[n].checked == false && $('[name=colname]')[n].id != 'chkAll') {
						a++;
						if (a === list) {
							$.messager.alert('提示信息','请选择您要删除的行项目!', 'info');
							return;
						}
					}
					if ($('[name=colname]')[n].checked == true && String($('[name=colname]')[n].id) != String('chkAll')) {
						var deleteUrl = "deleteFP.action";
						$.post(deleteUrl,{instanceid : $('[name=colname]')[n].id},function(data) {
							if (data == 'success') {
								window.location.reload();
							} else {
								alert(data);
							}
						});
					}
				}
			}
		});
	}
	
	function batch(){
		var khbh="";
		if ($('[name=colname]:checked').length === 0) {
			$.messager.alert('提示信息',
					'请选择您要修改的记录!', 'info');
			return;
		}
		var list = $('[name=colname]:checked').each(function (){
			var length=$("#"+$(this).val()+"").parent().parent().children("td").eq(2).text();
			khbh+=length+",";
		});
			var pageUrl ;
			pageUrl = "openFormPage.action?formid=155&demId=70&KHBH="+khbh;
			/* window.top.showUrl(pageUrl,'关联会议','iwork_img/desk/msg_b.png','mainFrame'); */
			
			art.dialog.open(pageUrl,{ 
					id:'Category_show',
					cover:true, 
					title:'编辑公告',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1100,
					cache:false,
					lock: true,
					height:600, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					close : function() {
						window.location.reload();
					}
				});
	}
	function OrderCol(col){
		var col_ = $("#col").val();
		var rule = $("#rule").val();
		if(col_!=col)
			rule="";
		if(rule==null||rule==""){
			rule="1";
		}else if(rule!=null&&rule!=""&&rule=="1"){
			rule="2";
		}else if(rule!=null&&rule!=""&&rule=="2"){
			rule="";
		}
		var valid = mainFormValidator.form(); //执行校验操作
		if(!valid){
			return false;
		}
		var khbh = $("#KHBH").val();
		var khmc = $("#KHMC").val();
		var cxddzy = $("#CXDDZY").val();
		var zzcxdd = $("#ZZCXDD").val();
		var zqdm = $("#GPDM").val();
		var seachUrl = encodeURI("loadFP.action?khmc=" + khmc + "&khbh=" + khbh + "&cxddzy=" + cxddzy + "&zzcxdd=" + zzcxdd + "&zqdm=" + zqdm + "&rule=" + rule + "&col=" + col);
		window.location.href = seachUrl;
	}
	$(function(){
		var col = $("#col").val();
		var rule = $("#rule").val();
		if(rule==1){
			$("#"+col).html("↑");
		}else if(rule==2){
			$("#"+col).html("↓");
		}else{
			$("#"+col).html("");
		}
	});
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

.cell td{
			margin:0;
			padding:3px 4px;
			height:25px;
			font-size:12px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #eee;
		}
		.cell:hover{
			background-color:#F0F0F0;
		}
 .header td{
			height:35px;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
			border-top:1px dotted #ccc;
			border-right:1px solid #eee;
		} 
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav">
			<s:if test="ISPURVIEW">
				<a href="javascript:addItem();" class="easyui-linkbutton"
					plain="true" iconCls="icon-add">新增信息</a>
				<a href="javascript:remove();" class="easyui-linkbutton"
					plain="true" iconCls="icon-remove">删除</a>
				<a href="javascript:batch();" class="easyui-linkbutton"
					plain="true" iconCls="icon-add">批量设置</a>
			</s:if>
			<a href="javascript:window.location.reload();"
				class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>

		</div>
	</div>
	<div  region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px">
				<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:98%">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<!-- <td class="searchtitle">客户编号</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:32,required:false}'
											style="width:100px;" name='KHBH' id='KHBH' value=''
											form-type='al_textbox'></td> -->
										<td class="searchtitle">客户名称</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:200,required:false,string:true}'
											style="width:100px;" name='KHMC' id='KHMC' value='<s:property value="khmc"/>'
											form-type='al_textbox'></td>
										<td class="searchtitle">股票代码</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:200,required:false,string:true}'
											placeholder='支持逗号分割' style="width:100px;" name='GPDM' id='GPDM' value='<s:property value="zqdm"/>'
											form-type='al_textbox'></td>
										<td class="searchtitle">专职持续督导</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:32,required:false,string:true}' style="width:100px;"
											name='ZZCXDD' id='ZZCXDD' value='<s:property value="zzcxdd"/>' form-type='al_textbox'>
										</td>
										<td class="searchtitle">持续督导专员</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:32,required:false,string:true}' style="width:100px;"
											name='CXDDZY' id='CXDDZY' value='<s:property value="cxddzy"/>' form-type='al_textbox'>
										</td>

<s:property value=""/>
									</tr>
								</table>
							</td>
							<td valign='bottom' style='padding-bottom:5px;'><a
								id="search" class="easyui-linkbutton" icon="icon-search"
								href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>

			</div>
			<span style="disabled:none"> <input type="hidden"
				name="formid" value="114" id="formid" /> <input type="hidden"
				name="demId" value="50" id="demId" /> <input type="hidden"
				name="init_params" value="" id="init_params" /> <input type="hidden"
				name="idlist" id="idlist" value='11'>
			</span>
		</form>
		<div style="padding:5px">
			<table id='iform_grid' width="98%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:5%;"><input
						type="checkbox" name="colname" id="chkAll" /></td>
					<td style="width:5%;cursor:pointer;" onclick="OrderCol('j');">股票代码<span id="j"></span></td>
					<td style="width:5%;display:none;">客户编号</td>
					<td style="width:15%;">客户名称</td>
					<td style="width:7%;cursor:pointer;" onclick="OrderCol('a');">企业内部人员审核<span id="a"></span></td>
					<td style="width:7%;cursor:pointer;" onclick="OrderCol('b');">持续督导专员<span id="b"></span></td>
					<td style="width:7%;cursor:pointer;" onclick="OrderCol('c');">投行审核人员<span id="c"></span></td>
					<td style="width:7%;cursor:pointer;" onclick="OrderCol('d');">专职持续督导<span id="d"></span></td>
					<td style="width:7%;cursor:pointer;" onclick="OrderCol('e');">质控部负责人<span id="e"></span></td>
					<td style="width:7%;cursor:pointer;" onclick="OrderCol('f');">场外市场部负责人1<span id="f"></span></td>
					<td style="width:7%;cursor:pointer;" onclick="OrderCol('g');">场外市场部负责人2<span id="g"></span></td>
					<td style="width:7%;cursor:pointer;" onclick="OrderCol('h');">发布版文件审核人<span id="h"></span></td>
					<td style="width:7%;cursor:pointer;" onclick="OrderCol('i');">公告发布人<span id="i"></span></td>
					
					<td style="width:5%;">操作</td>
				</tr>
				<s:iterator value="List" status="status">
					<tr class="cell">
						<td><input type="checkbox" id="<s:property value='INSTANCEID'/>" name="colname" value="<s:property value='INSTANCEID'/>" /></td>
						<td><s:property value="ZQDM" /></td>
						<td style="display:none;"><s:property value="KHBH" /></td>
						<td><s:property value="KHMC" /></td>
						<td><s:property value="QYNBRYSH" /></td>
						<td><s:property value="KHFZR" /></td>
						<td><s:property value="ZZCXDD" /></td>
						<td><s:property value="FHSPR" /></td>
						<td><s:property value="ZZSPR" /></td> 
						<td><s:property value="CWSCBFZR2" /></td>
						<td><s:property value="CWSCBFZR3" /></td>
						<td><s:property value="FBBWJSHR" /></td>
						<td><s:property value="GGFBR" /></td>
						
						<td><a
							href="javascript:updateFP('<s:property value="INSTANCEID"/>','<s:property value="KHFZR"/>')"
							style="color:blue;"><u>查看详情</u></a></td>
					</tr>
				</s:iterator>
			</table>

			<form action="loadFP.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="khbh" id="khbh"></s:hidden>
				<s:hidden name="khmc" id="khmc"></s:hidden>
				<s:hidden name="qynbrysh" id="qynbrysh"></s:hidden>
				<s:hidden name="cxddzy" id="cxddzy"></s:hidden>
				<s:hidden name="zzcxdd" id="zcxxdd"></s:hidden>
				<s:hidden name="zqdm" id="zqdm"></s:hidden>
				<s:hidden name="ISPURVIEW" id="ISPURVIEW"></s:hidden>
				<s:hidden name="lc" id="lc"></s:hidden>
				<s:hidden name="col" id="col"></s:hidden>
				<s:hidden name="rule" id="rule"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south"
		style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-left:10px;"
		border="false">
		<div>
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
					<script type="text/javascript">
	$(function(){
		$("#KHMC").val($("#khmc").val());
		$("#KHBH").val($("#khbh").val());
		$("#ZZCXDD").val($("#zcxxdd").val());
		$("#CXDDZY").val($("#cxddzy").val());
		$("#GPDM").val($("#zqdm").val());
	});
	</script>
</body>
</html>
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{};'[\]]/im;
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