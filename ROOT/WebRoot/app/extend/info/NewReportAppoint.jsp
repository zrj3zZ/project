<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>定期报告预约情况</title>
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
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<style type="text/css">
.memoTitle {
	font-size: 14px;
	padding: 5px;
	color: #666;
}

.memoTitle a {
	font-size: 12px;
	padding: 5px;
}

.TD_TITLE {
	padding: 5px;
	width: 200px;
	background-color: #efefef;
	text-align: right;
}

.TD_DATA {
	padding: 5px;
	padding-left: 15px;
	padding-right: 30px;
	background-color: #fff;
	width: 500px;
	text-align: left;
	border-bottom: 1px solid #efefef;
}

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

.cell:hover {
	background-color: #F0F0F0;
}

.cell td {
	margin: 0;
	padding: 3px 4px;
	white-space: nowrap;
	word-wrap: normal;
	overflow: hidden;
	text-align: left;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #ccc;
}

.selectCheck {
	border: 0px;
	text-align: right;
}
</style>
<script type="text/javascript">
	$(function() {
		$('#pp').pagination({
			total : <s:property value="totalNum"/>,
			pageNumber : <s:property value="pageNumber"/>,
			pageSize : <s:property value="pageSize"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMsg(pageNumber, pageSize);
			}
		});
		//查询事件
		$("#search").click(function() {
			var sxid = $("#sxInfo").val();
			var customerno = $("#customerno").val();
			var yyrq = $("#YYRQ").val();
			var seachUrl = encodeURI("newAppointInfo.action?customerno=" + customerno + "&yyrq=" + yyrq + "&eventID=" + sxid);
			window.location.href = seachUrl;
		});
	});
	
	var addTimer = function () {
        var list = [],
            interval;
        return function (id, time) {
            if (!interval)
                interval = setInterval(go, 1000);
            list.push({ ele: document.getElementById(id), time: time });
        }     
    
        function go() {
            for (var i = 0; i < list.length; i++) {     
                list[i].ele.innerHTML = getTimerString(list[i].time ? list[i].time -= 1 : 0);
                if (!list[i].time){
                	this.location.reload();
                    list.splice(i--, 1);
                }
            }
        }     
    
        function getTimerString(time) {     
                d = Math.floor(time / 86400),     
                h = Math.floor((time % 86400) / 3600),     
                m = Math.floor(((time % 86400) % 3600) / 60),     
                s = Math.floor(((time % 86400) % 3600) % 60);     
            if (time>0)     
                return d + " 天" + h + " 小时" + m + " 分" + s + " 秒";       
            else return "";
        }     
    } ();
	
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return;
	}
	
	// 全选、全清功能
	function selectAll() {
		if ($("input[name='chk_All']").attr(
				"checked")) {
			$("input[name='chk_list']").attr(
					"checked", true);
		} else {
			$("input[name='chk_list']").attr(
					"checked", false);
		}
	}
	
	//新增预约
	function addAppoint(yysxid,customerno) {
		var pageUrl = "url:getNewCanAppoint.action?customerno=" + customerno + "&SXID=" + yysxid;
		$.dialog({
			title : '预约信息维护',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 800,
			cache : false,
			lock : true,
			height : 500,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			content : pageUrl,
			close : function() {
				window.location.reload();
			}
		});
	}

	//删除预约日期
	function DeleteYY(id) {
		if(!confirm("确定要删除该预约吗？")){return;}
		$.post('NewDeleteAppoint.action', {projectNo:id},
			function(data) {
				if (data == "success") {
					alert("删除成功");
					window.location.reload();
				} else {
					alert("删除失败，请重试");
				}
			}
		);
	}
	
	//检查开始日期截止 日期
	function checkRQ() {
		var start = $("#STARTDATE").val();
		var end = $("#ENDDATE").val();
		if (start != "" && end != "") {
			if (end < start) {
				alert("结束日期不能早于开始日期，请重新输入！");
				$("#ENDDATE").val("");
			}
			return;
		}
	}

	function selectNum() {
		var url = "url:selectNum.action";
		$.dialog({
			id : 'kmDialog',
			cover : true,
			title : '预约设置',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			height : 550,
			cache : false,
			lock : true,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			content : url
		});
	}

	//xlj start
	//更新预约日期
	function UpdateDate() {
		var ids = $("#hid").val();
		var yyDate = $("#editDATE").val();
		if (ids != "") {
			$.post('UPDATEAppoint.action', {
				projectNo : ids,
				startDate : yyDate
			},//调用ajax方法
			function(data) {
				if (data == "success") {
					alert("保存成功");
					window.location.reload();
				} else {
					alert("保存失败，请重试");
				}
			});
		}
	}

	function EditDate(yyid,e) {
		var scrollX = document.documentElement.scrollLeft
				|| document.body.scrollLeft;
		var scrollY = document.documentElement.scrollTop
				|| document.body.scrollTop;
		var x = e.pageX || e.clientX + scrollX;
		var y = e.pageY || e.clientY + scrollY;
		$("#hid").val(yyid);
		var div = document.getElementById("divEditDate");//显示		
		div.style.display = "block";
	
		div.style.top = y + "px";
		div.style.left = (x - 150) + "px";
		div.style.height = "50px";
		div.style.width = "150px";
		div.style.position = "absolute";
	}
	
	function editClose() {
		document.getElementById("divEditDate").style.display = "none";
	}
	//xlj end

	function SetProhibitive() {
		url = "url:appointmentAdd.action"
		var dg = $.dialog({
			id : 'dg_select',
			title : '预约设置',
			resize : false,
			iconTitle : false,
			content : url,
			width : 1100,
			height : 600,
			max : false
		});
	}
	
	function edit(instanceid,yyid,khbh){
		var yyformid = $("#yyformid").val();
		var yydemid = $("#yydemid").val();
		var pageUrl = "openFormPage.action?formid="+yyformid+"&demId="+yydemid+"&instanceId="+instanceid;//+"&YYID="+yyid+"&KHBH="+khbh;;
		art.dialog.open(pageUrl,{ 
			title:'编辑附件',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:800,
			cache:false,
			lock: true,
			height:600, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			/* close:function(){
				window.location.reload();
			} */
		 
		});
	}
	
	function addItem(yyid, khbh) {
		if (yyid == null || yyid == "") {
			alert("请先预约事项!");
			return;
		}
		var yyformid = $("#yyformid").val();
		var yydemid = $("#yydemid").val();
		var pageUrl = "url:createFormInstance.action?formid=" + yyformid + "&demId=" + yydemid + "&YYID=" + yyid + "&KHBH=" + khbh;
		$.dialog({
			title : '上传附件',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 800,
			cache : false,
			lock : true,
			height : 600,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			content : pageUrl,
			close : function() {
				window.location.reload();
			}
		});
	}
</script>
</head>
<body class="easyui-layout">
	<div region="north" style="height:50px;font-size:20px;font-family:黑体;padding:5px;border-bottom:1px solid #efefef;" border="false">
		<div class="tools_nav">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<a href="javascript:this.location.reload();"
						class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div region="center" border="false" align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
		<form action="AppointInfo.action" method=post name=frmMain id=frmMain>
			<div style="padding:5px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
				<table border='0' cellpadding='0' cellspacing='0'>
					<tr>
						<td class="searchtitle">							
							<input type='hidden' onfocus="WdatePicker()" class="{required:true}" style="width:100px" 
							name='YYRQ' id='YYRQ' value='${yyrq}' />
						</td>
						<td class="searchtitle">&nbsp;&nbsp;事项描述：
							<select name="sxms" style="width:400px" id="sxInfo">
								<option value="">-空-</option>
								<c:forEach var="sxmsConfig" items="${sxmsList}">
									<option value="${sxmsConfig.ID}" <c:if test="${sxmsConfig.ID==eventID}">selected="true"</c:if>>${sxmsConfig.SXMS}</option>
								</c:forEach>
						</select>
						</td>
						<td class="searchtitle" align="left">&nbsp;&nbsp;
							<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a>
						</td>
					</tr>
				</table>
			</div>
			<s:hidden name="customerno" id="customerno"></s:hidden>
			<s:hidden name="corpCode" id="corpCode"></s:hidden>
			<s:hidden name="eventID" id="eventID"></s:hidden>
			<s:hidden name="yyformid" id="yyformid"></s:hidden>
			<s:hidden name="yydemid" id="yydemid"></s:hidden>
			<!-- xlj start -->
			<input type="hidden" id="hid" name="hid" />
			<div align="center" id="divEditDate" name="divEditDate"
				style="z-index:99999;display:none;">
				<table width="100%">
					<tr>
						<td align="left"><a id="btnEp" class="easyui-linkbutton"
							icon="icon-save" plain="true" href="javascript:UpdateDate();">保存</a>
							<a href="javascript:editClose();" class="easyui-linkbutton"
							plain="true" iconCls="icon-cancel">关闭</a></td>
						<td style="text-align:right;padding-right:10px"></td>
					</tr>
					<tr>
						<td align="center" colspan="2">请选择预约日期：<input type='text'
							onfocus="WdatePicker()" class="{required:true}"
							style="width:100px" name='editDATE' id='editDATE' /></td>
					</tr>
				</table>
			</div>
			<!-- xlj end -->
		</form>
		<table WIDTH="100%" style="border:1px solid #efefef">
			<tr class="header">
				<td style="width:150px">事项名称</td>
				<td style="width:150px">负责人</td>
				<td style="width:150px">可预约日期</td>
				<td style="width:150px">预约倒计时</td>
				<td style="width:60px">已预约日期</td>
				<td style="width:60px">操作</td>
			</tr>
			<s:iterator value="runList" status="status">
				<tr class="cell">
					<td><s:property value="SXMS" /></td>
					<td><s:property value="KHFZR" /></td>
					<td><s:property value="YYSJFW" /></td>
					<td><span id="<s:property value='CLNAME'/>"></span></td>
					<s:if test="DJS>0">
						<script type="text/javascript">
								addTimer('<s:property value='CLNAME'/>',<s:property value='DJS'/>);
						</script>
					</s:if>
					<td><s:property value="YYRQ" /></td>
					<td>
						<s:if test="SFGQ==0">
							<s:if test="(YYXXID==0||YYXXID==null)&&SFYY==1&&0>=DJS">
								<a href="javascript:void(0);" onclick="javascript:addAppoint(<s:property value="YYSXID"/>,'<s:property value="CUSTOMERNO"/>');">预约</a>&nbsp;&nbsp;
							</s:if>
							<s:elseif test="YYXXID!=null&&YYXXID!=''">
								<s:if test="FJINSTANCEID != ''">
									<a href="javascript:void(0);" onclick="javascript:edit('<s:property value="FJINSTANCEID"/>',<s:property value="YYXXID"/>,'<s:property value="CUSTOMERNO"/>');">
										<s:if test="FJ==null||FJ==''">
											上传附件
										</s:if>
										<s:else>
											查看附件
										</s:else>
									</a>
								</s:if>
								<s:else>
									<a href="javascript:void(0);" onclick="javascript:addItem(<s:property value="YYXXID"/>,'<s:property value="CUSTOMERNO"/>');">上传附件</a>
								</s:else>
								<a href="javascript:void(0);" onclick="javascript:DeleteYY(<s:property value="YYXXID"/>);">删除预约</a>
							</s:elseif>
						</s:if>
					</td>
				</TR>
			</s:iterator>
		</table>
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
	</div>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript">
	jQuery.validator.addMethod("string", function(value, element) {
		var sqlstr = [ " and ", " exec ", " count ", " chr ", " mid ",
				" master ", " or ", " truncate ", " char ", " declare ",
				" join ", "insert ", "select ", "delete ", "update ",
				"create ", "drop " ]
		var patrn = /[`~!#$%^&*+<>?"{},;'[\]]/im;
		if (patrn.test(value)) {
		} else {
			var flag = false;
			var tmp = value.toLowerCase();
			for (var i = 0; i < sqlstr.length; i++) {
				var str = sqlstr[i];
				if (tmp.indexOf(str) > -1) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				return "success";
			}
		}
	}, "包含非法字符!");
</script>