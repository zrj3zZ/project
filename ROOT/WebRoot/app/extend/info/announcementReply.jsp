<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
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
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
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
		//从上级页面获取通知类型
		var tzlx =window.parent.document.getElementById("NOTICETYPE").value;
		if(tzlx!="培训通知"){
			$("#qdan").hide();
			$("#ydrstd").hide();
			$("#sdrstd").hide();
		}
		if(tzlx!="股东质押查询"){
			$("#sfcxtd").hide();
		}
		$(document).bind('keyup', function(event) {
			if (event.keyCode == "13") {
				return false; 
			}
		})
		$(document).bind('keydown', function(event) {
			if (event.keyCode == "13") {
				return false; 
			}
		})
	});
	
	function setCheck(){
		$("[name=chk_list]:checkbox").attr("checked", $("#chkAll").is(':checked'));
	}

	function addProject(customername, customerno, username, tel) {
		var formid = 91;
		var demId = 22;
		var url = 'createFormInstance.action?formid=' + formid + '&demId=' + demId + '&CUSTOMERNAME=' + encodeURI(customername) + '&CUSTOMERNO=' + encodeURI(customerno) + '&KHLXR=' + encodeURI(username) + '&KHLXDH=' + tel + '&PROJECTNAME=' + encodeURI(customername);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width + ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
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
		$("#search").click(function() {
			document.getElementById("roleid").value="";
			var status = $("#STATUS").val();
			var ggid=$("#ggid").val();
			var seachUrl = encodeURI("announcement_notice_list.action?status=" + status+"&ggid="+ggid);
			
			window.location.href = seachUrl;
		});
		$("#searchMycpy").click(function() {
			document.getElementById("roleid").value="flag";
			var status = $("#STATUS").val();
			var ggid=$("#ggid").val();
			var seachUrl = encodeURI("announcement_notice_list.action?status=" + status+"&ggid="+ggid+"&roleid=flag");
			window.location.href = seachUrl;
		});
	});

	function edit(instanceid) {
		var pageUrl = "openFormPage.action?formid=88&demId=21&instanceId=" + instanceid;
		art.dialog.open(pageUrl,{
			title : '客户信息维护表单',
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
		var pageUrl = "createFormInstance.action?formid=88&demId=21";
		art.dialog.open(pageUrl,{
			title : '客户信息维护表单',
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
	function Signin() {
		var tzggid = $("#ggid").val();
		var pageUrl = "Signin.action?ggid="+tzggid;
		art.dialog.open(pageUrl,{
			title : '已回复签到页面',
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
	function showReply(instanceid,ggid,userid){
			if(instanceid==0){
				alert("该公司还没有回复。");
				return ;
			}
			var pageurl="";
			var title = "";
			var height = 580;
			var width = 900;
			//从上级页面获取通知类型
			var tzlx =window.parent.document.getElementById("NOTICETYPE").value;
			 $.ajax({
					type: "POST",
					url: "zqb_announcement_notice_read.action",
					data: {ggid:ggid},
					success: function(data){
						updckqk(userid,ggid);
						if(tzlx=="培训通知"){
							pageurl = "loadVisitPage.action?instanceId=" + instanceid+"&formid="+data.split(",")[0]+"&demId="+data.split(",")[1];
							art.dialog.open(pageurl,{
								title : '培训通知',
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
							
						}else if(tzlx=="问卷调查"){
							pageurl = 'zqb_vote_wjdc.action?ggid='+ggid+'&instanceid='+instanceid;
							art.dialog.open(pageurl,{
								title : '问卷调查',
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
							pageurl = "showReply.action?instanceId=" + instanceid+"&ggid="+ggid;
							art.dialog.open(pageurl,{
								title : '其他通知',
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
					}
			});
	}
	function updckqk(userId,ggid){
		$.ajax({
			type: "POST",
			url: "zqb_announcement_dfyy.action",
			data: {ggid:ggid,userId:userId}
		});
	}
	function deleteReply(id,nameid,name){
		 $.messager.confirm('确认','确认删除?',function(result){ 
		 	if(result){
		 			var hfr = $("#hfr",parent.document).val();
		 			var tzggid = $("#ggid").val();
		 			var hfrsub = hfr.split(",");
		 			for (var i = 0; i < hfrsub.length; i++) {
						if(hfrsub[i].indexOf(name) >= 0){
							hfrsub.splice(i,1);//从下标为i的元素开始，连续删除1个元素
					        i--;
						}
					}
		 			var hfrstr = hfrsub.join(",");
		 			$("#hfr",parent.document).val(hfrstr);
		 			$("#"+name+"").remove();
					var deleteUrl = "deleteReply.action";
					$.post(deleteUrl,{ggid:id,tzggid:tzggid,hfr:hfrstr},function(data){ 
	       			if(data=='success'){
	       				window.location.reload();
	       			}else{
	       				alert(data);
	       			}
	     		 });
				 } 
     });
     }
	function deleteReplyAll(){
		 $.messager.confirm('确认','确认全部删除?',function(result){ 		 	
		 	if(result){
		 			var obj = document.getElementsByName("chk_list");
					var s1=new Array();
					var s2=new Array();
					var s3=new Array();
					//取到对象数组后，我们来循环检测它是不是被选中
					var jj=0;
					for(var j=0; j<obj.length; j++){ 
						if(obj[j].checked){
							var a=obj[j].value;
							var b=a.split(",");
							if(b.length==3){
						 		var hfr = $("#hfr",parent.document).val();
			 					var tzggid = $("#ggid").val();
			 					var hfrsub = hfr.split(",");
			 					for (var i = 0; i < hfrsub.length; i++) {
									if(hfrsub[i].indexOf(b[2]) >= 0){
									hfrsub.splice(i,1);//从下标为i的元素开始，连续删除1个元素
						       		 i--;
									}
								}
			 					var hfrstr = hfrsub.join(",");
			 					$("#hfr",parent.document).val(hfrstr);
			 					$("#"+b[2]+"").remove();
							}
				 			var hfrstr = hfrsub.join(",");
				 			$("#hfr",parent.document).val(hfrstr);
				 			$("#"+b[2]+"").remove();	
				 			s1[jj]=b[0];
				 			s2[jj]=tzggid;
				 			s3[jj]=hfrstr;
				 			jj++;
						}
					}	
					if(s1==""){alert("请选择要删除的内容。");return;}
					var deleteUrl = "deleteReplyAll.action";
					var s11=s1.join(",");
					var s22=s2.join(",");
					var s33=s3.join(",");
					$.post(deleteUrl,{ggid:s11,tzggids:s22,hfr:s33},function(data){ 
	       			if(data=='success'){
	       				window.location.reload();
	       			}else{
	       				alert(data);
	       			}
	     		 });
		  } 
     });
     }
	function setMemo(id){
		var memo = $("#"+id).val();
		$.post("updateMemo.action",{id:id,memo:memo},function(data){ 
			if(data=='success'){
				window.location.reload();
			}else{
				alert(data);
			}
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
</style>
</head>
<body class="easyui-layout">
	<div region="center"
		style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post">
				<div
					style="border:1px solid #ccc;background:#FFFFEE;">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:5px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">状态</td>
										<td class="searchdata"><select name='STATUS' id='STATUS'>
												<option	value=''>-空-</option>
												<option value='未回复'>未回复</option>
												<option value='已回复'>已回复</option>
										</select></td>
									</tr>
								</table>
							<td>
							<td valign='bottom' style='padding-bottom:2px;'>
								<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a>
								<a id="searchMycpy" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询我督导的公司</a>
								
								<a id="searchMycpy" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);"  onclick="deleteReplyAll();">全部删除</a>
								
								<span id ="qdan">
									<a id="search" class="easyui-linkbutton"  style="float:right;" href="javascript:Signin();">签到</a>									
								</span>
							</td>
						<tr>
					</table>
			</div>
		</form>
		<div style="display:block;width:300px;float:left;color: red;">注意:只要通知挂牌企业,就通知相应的初审人员</div><div align="right" style="padding-right: 5px;width:500px;float: right;"><%-- <s:property value="${tzcount}" /><s:property value="${whfcount}" /> --%><%-- <input type="text" value="${tzcount}"/> --%>共通知人数：<s:property value="tzcount" /> 未回复人数：<s:property value="whfcount" /></div>
		<div style="padding:5px">
			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">
					<td class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_rn" style="width: 3%;">
								<div id="jqgh_dictionary_list_grid_rn"><input type="checkbox" name="chk_lista" id="chkAll" onclick="setCheck();">
										<span style="display:none" class="s-ico">
												<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
												<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
										</span>
								</div>
					</td>
					<td style="width:7%;">公司代码</td>
					<td style="width:14%;">部门/公司名称</td>
					<td style="width:14%;">姓名</td>
					<td style="width:7%;">手机号</td>
					<td style="width:7%;">已阅</td>
					<td style="width:7%;">状态</td>
					<td style="width:7%;">回复时间</td>
					<td style="width:7%;" id="sfcxtd">是否查询</td>
					<td style="width:9%;">备注</td>
					<td><s:if test="tzggGrantCUD"><td style="width:7%;">操作</td></s:if></td>
					<td style="width:7%;" id="ydrstd">应到人数</td>
					<td style="width:7%;" id="sdrstd">实到人数</td>
				</tr>
				<s:iterator value="list" status="status">
					<tr class="cell">
						<td><input type="checkbox" value="<s:property value='ID' />,<s:property value='GSDM' />[<s:property value='XM' />],<s:property value='XM' />" name="chk_list"></td>
						<td><a href="javascript:void(0)" style="color:blue;" onclick="showReply(<s:property value='INSTANCEID'/>,<s:property value='ggid'/>,'<s:property value='USERID'/>')"><s:property value="GSDM" /></a></td>
						<td><a href="javascript:void(0)" style="color:blue;" onclick="showReply(<s:property value='INSTANCEID'/>,<s:property value='ggid'/>,'<s:property value='USERID'/>')"><s:property value="GSMC" /></a></td>
						<td><s:property value="XM" /></td>
						<td><s:property value="SJH" /></td>
						<td><s:property value="SFCK" /></td>
						<td><s:property value="STATUS" /></td>
						<td class="mailicon">
						<s:if test="XGZL!=null&&XGZL!=''">
								<img src=iwork_img/attach.png>
								</s:if>
						<s:property value="HFSJ" />
						</td>
						<s:if test="TZLX=='股东质押查询'">
						<td id="sfcxtd1"><s:property value="EXTEND1" /></td>
						</s:if>
						<td><input placeholder="备注信息" type='text' class='{maxlength:128,required:false}' style="width:100px" id="<s:property value='ID'/>" name="MEMO" value="${MEMO}" <s:if test="tzggGrantCUD">onblur="setMemo(<s:property value='ID'/>);"</s:if>></td>
						<s:if test="tzggGrantCUD">
						<td>
							<a href="javascript:deleteReply('<s:property value="ID" />','<s:property value="GSDM" />'+'['+'<s:property value="XM" />'+']','<s:property value="XM" />')" style="color:blue;">
								<u>删除</u>
							</a>
						</td>
						</s:if>
						<s:if test="TZLX=='培训通知'">
						<td id="ydrstd1"><s:property value="YDRS" /></td>
						<td id="sdrstd1"><s:property value="SDRS" /></td>
						</s:if>
						
					</tr>
				</s:iterator>
			</table>

			<form action="announcement_notice_list.action" method=post name=frmMain
				id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="status" id="status"></s:hidden>
				<s:hidden name="roleid" id="roleid" ></s:hidden>
				<input type="hidden" name="ggid" id="ggid" value="${ggid}"/>
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
	</div>
	<script type="text/javascript">
		$(function() {
			$("#STATUS").attr("value", $("#status").val());
		});
	</script>
</body>
</html>