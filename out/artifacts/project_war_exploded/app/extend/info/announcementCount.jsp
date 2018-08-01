<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公告统计</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
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
				var khmc = $("#KHMC").val();
				var ggzy = $("#GGZY").val();
				var spzt = $("#SPZT").val();
				var noticename = $("#NOTICENAME").val();
				var gpdm = $("#GPDM").val();
				var noticetype = $("#NOTICETYPE").val();
				var startdate = $("#STARTDATE").val();
				var enddate = $("#ENDDATE").val();
				var seachUrl = encodeURI("zqb_announcement_count.action?khmc="
						+ khmc
						+ "&ggzy="
						+ ggzy
						+ "&spzt="
						+ spzt
						+ "&noticename="
						+ noticename
						+ "&zqdm="
						+ gpdm
						+ "&startdate="
						+ startdate
						+ "&enddate=" + enddate+"&noticetype="+noticetype);
				window.location.href = seachUrl;
			});
	});
	// 查看公告标题公告详情
	function readAnnouncement(id, instanceid,zqdm,zqjc) {
		var pageUrl = "loadPage.action?instanceId=" + instanceid+"&zqjcxs="+encodeURI(zqjc)+"&zqdmxs="+zqdm;
		art.dialog.open(pageUrl,{
			id : 'Category_show',
			cover : true,
			title : '查看公告',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1000,
			cache : false,
			lock : true,
			height : 480,
			iconTitle : false,
			extendDrag : true,
			autoSize : false
		});
	}
	//点击公司简称查看客户详情
	function edit(instanceid){
			var pageUrl = "openFormPage.action?formid=88&demId=21&instanceId="+instanceid;
			art.dialog.open(pageUrl,{ 
				title:'客户信息维护表单',
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
	function expExcel() {
		//导入excel
		var pageUrl = "zqb_announcement_exp.action";
		$("#ifrmMain").attr("action", pageUrl);
		$("#ifrmMain").submit();
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

.cell td {
	margin: 0;
	padding: 3px 4px;
	font-size: 12px;
	word-wrap: break-word;
	overflow: hidden;
	text-align: left;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #eee;
}

.cell:hover {
	background-color: #F0F0F0;
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
#iform_grid {
table-layout: fixed;
 }


#iform_grid td {
overflow: hidden;
white-space: nowrap;
text-overflow: ellipsis;
}
.tools_nav {
	background:url(../iwork_img/engine/tools_nav_bg.jpg) repeat-x;
	height:34px;
	line-height:30px;
	padding-left:20px;
	vertical-align:middle;
	padding-top:2px;
	padding-bottom:2px; 
	text-align:left;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:window.location.reload();"
				class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<a href="javascript:expExcel();" class="easyui-linkbutton"
				plain="true" iconCls="icon-excel-exp">导出</a>
		</div>
	</div>
	<div region="center"
		style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px">
				<div
					style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">公司简称</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:200,required:false,string:true}' style="width:100px;"
											name='KHMC' id='KHMC' value='' form-type='al_textbox'></td>
										<td class="searchtitle">股票代码</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:200,required:false,string:true}' placeholder='支持逗号分割'
											style="width:100px;" name='GPDM' id='GPDM' value=''
											form-type='al_textbox'></td>
										<td class="searchtitle" style="text-align:right;">公告类型</td>
										<td class="searchdata">
										<select name='NOTICETYPE' id='NOTICETYPE'>
										<option value=''>-空-</option>
										<option value='临时报告'>临时报告</option>
										<option value='定期报告'>定期报告</option>
										</select></td>
										<td class="searchtitle">公告名称</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:32,required:false,string:true}' style="width:100px;"
											name='NOTICENAME' id='NOTICENAME' value=''
											form-type='al_textbox'></td>
										<td class="searchtitle">公告摘要</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:32,required:false,string:true}' style="width:100px;"
											name='GGZY' id='GGZY' value='' form-type='al_textbox'>
										</td>
										<td class="searchtitle">审批状态</td>
										<td><select name="SPZT" id="SPZT">
												<option value="">-空-</option>
												<option value="审批中">审批中</option>
												<option value="审批通过">审批通过</option>
										</select></td>
										<td id="title_STARTDATE" class="searchtitle" width="400">公告时间
											<input type='text' onfocus="WdatePicker()"
											style="width:100px" name='STARTDATE'
											id='STARTDATE' value=''> 到 <input type='text'
											onfocus="WdatePicker()"
											onchange="checkRQ()" style="width:100px" name='ENDDATE'
											id='ENDDATE' value=''>
										</td>
									</tr>
								</table>
							</td>
							<td valign='bottom' style='padding-bottom:15px;'><a
								id="search" class="easyui-linkbutton" icon="icon-search"
								href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>

			</div>
			<span style="disabled:none"> <input type="hidden"
				name="formid" value="114" id="formid" /> <input type="hidden"
				name="demId" value="50" id="demId" /> <input type="hidden"
				name="init_params" value="" id="init_params" /> <input
				type="hidden" name="idlist" id="idlist" value='11'>

			</span>
		</form>
		<div style="padding:5px">
			<table id='iform_grid'
				style="TABLE-LAYOUT: fixed;border:1px solid #efefef" border="1"
				cellspacing="0" cellpadding="0" width="100%">
				<tbody>
					<tr class="header">
						<td style="width:30px;">序号</td>
						<td style="width:60px;">公告类型</td>
						<td style="width:60px;">股票代码</td>
						<td style="width:80px;">客户名称</td>
						<td style="width:80px;">公告编号</td>
						<td style="width:350px;">公告名称</td>
						<td style="width:80px;">公告日期</td>
						<td style="width:80px;">审批状态</td>
						<td style="width:80px;">公告评分</td>
						<td>公告摘要</td>
					</tr>
					<s:iterator value="List" status="status">
						<tr class="cell">
							<td style="width:30px;"><s:property value="XL" /></td>
							<td style="width:60px;"><s:property value="NOTICETYPE" /></td>
							<td style="width:60px;"><s:property value="ZQDM" /></td>
							<td style="width:80px;"><a href="javascript:void(0)"
								onclick="edit(<s:property value='CusIns'/>)"><s:property
										value="ZQJC" /></a></td>
							<td style="width:80px;"><s:property value="NOTICENO" /></td>
							<td style="width:350px;" title="<s:property value="NOTICENAME" />"><a
								href="javascript:readAnnouncement(<s:property value="ID"/>,<s:property value="INSTANCEID"/>,'<s:property value="ZQDM"/>','<s:property value="ZQJC"/>')"><s:property
										value="NOTICENAME" /></a></td>
							<td style="width:80px;"><s:property value="NOTICEDATE" /></td>
							<td style="width:80px;"><s:property value="SPZT" /></td>
							<td style="width:80px;"><s:property value="GGDF" /></td>
							<td id="MYTD" title="<s:property value="GGZY" />"><s:property value="GGZY"/></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
			<form action="zqb_announcement_count.action" method=post name=frmMain
				id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="ggzy" id="ggzy"></s:hidden>
				<s:hidden name="khmc" id="khmc"></s:hidden>
				<s:hidden name="noticename" id="noticename"></s:hidden>
				<s:hidden name="spzt" id="spzt"></s:hidden>
				<s:hidden name="zqdm" id="zqdm"></s:hidden>
				<s:hidden name="startdate" id="startdate"></s:hidden>
				<s:hidden name="enddate" id="enddate"></s:hidden>
				<s:hidden name="noticetype" id="noticetype"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south"
		style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
		border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
		<script type="text/javascript">
			$(function() {
				$("#KHMC").val($("#khmc").val());
				$("#GGZY").val($("#ggzy").val());
				$("#SPZT").val($("#spzt").val());
				$("#NOTICENAME").val($("#noticename").val());
				$("#GPDM").val($("#zqdm").val());
				$("#STARTDATE").val($("#startdate").val());
				$("#ENDDATE").val($("#enddate").val());
				$("#NOTICETYPE").attr("value",$("#noticetype").val()); 
			});
		</script>
</body>
</html>
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