<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信披自查反馈</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/iwork/oaknow.css"/>
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  charset="utf-8"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"  ></script>
	<script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" charset="utf-8">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#editForm").validate({});
		mainFormValidator.resetForm();
	}); 		
		var flag = false;
		$(document).ready(function() {
			window.onbeforeunload = null;
			if($("#lockStatusForToolsNav").val()!='0'){
				$("#save").hide();
			}

			var ggid = $("#ggid").val();
			var khbh = $("#khbh").val();
			$.post("zqb_vote_getwjdccontent.action?ggid="+ggid+"&khbh="+khbh, function (data) {
				var dataJson = eval("(" + data + ")");
				$("#BZ1").bind({focus:function(){hideTip(this,dataJson[0].BZ1)},blur:function(){showTip(this,dataJson[0].BZ1)}}).attr("placeholder",dataJson[0].BZ1);
				$("#BZ2").bind({focus:function(){hideTip(this,dataJson[0].BZ2)},blur:function(){showTip(this,dataJson[0].BZ2)}}).attr("placeholder",dataJson[0].BZ2);
				$("#BZ3").bind({focus:function(){hideTip(this,dataJson[0].BZ3)},blur:function(){showTip(this,dataJson[0].BZ3)}}).attr("placeholder",dataJson[0].BZ3);
				$("#BZ4").bind({focus:function(){hideTip(this,dataJson[0].BZ4)},blur:function(){showTip(this,dataJson[0].BZ4)}}).attr("placeholder",dataJson[0].BZ4);
				$("#BZ5").bind({focus:function(){hideTip(this,dataJson[0].BZ5)},blur:function(){showTip(this,dataJson[0].BZ5)}}).attr("placeholder",dataJson[0].BZ5);
				$("#BZ6").bind({focus:function(){hideTip(this,dataJson[0].BZ6)},blur:function(){showTip(this,dataJson[0].BZ6)}}).attr("placeholder",dataJson[0].BZ6);
				$("#BZ7").bind({focus:function(){hideTip(this,dataJson[0].BZ7)},blur:function(){showTip(this,dataJson[0].BZ7)}}).attr("placeholder",dataJson[0].BZ7);
				$("#BZ8").bind({focus:function(){hideTip(this,dataJson[0].BZ8)},blur:function(){showTip(this,dataJson[0].BZ8)}}).attr("placeholder",dataJson[0].BZ8);
				$("#BZ9").bind({focus:function(){hideTip(this,dataJson[0].BZ9)},blur:function(){showTip(this,dataJson[0].BZ9)}}).attr("placeholder",dataJson[0].BZ9);
				$("#BZ10").bind({focus:function(){hideTip(this,dataJson[0].BZ10)},blur:function(){showTip(this,dataJson[0].BZ10)}}).attr("placeholder",dataJson[0].BZ10);
				$("#BZ11").bind({focus:function(){hideTip(this,dataJson[0].BZ11)},blur:function(){showTip(this,dataJson[0].BZ11)}}).attr("placeholder",dataJson[0].BZ11);
				$("#BZ12").bind({focus:function(){hideTip(this,dataJson[0].BZ12)},blur:function(){showTip(this,dataJson[0].BZ12)}}).attr("placeholder",dataJson[0].BZ12);
				$("#BZ13").bind({focus:function(){hideTip(this,dataJson[0].BZ13)},blur:function(){showTip(this,dataJson[0].BZ13)}}).attr("placeholder",dataJson[0].BZ13);
				$("#BZ14").bind({focus:function(){hideTip(this,dataJson[0].BZ14)},blur:function(){showTip(this,dataJson[0].BZ14)}}).attr("placeholder",dataJson[0].BZ14);
				$("#BZ15").bind({focus:function(){hideTip(this,dataJson[0].BZ15)},blur:function(){showTip(this,dataJson[0].BZ15)}}).attr("placeholder",dataJson[0].BZ15);
				$("#BZ16").bind({focus:function(){hideTip(this,dataJson[0].BZ16)},blur:function(){showTip(this,dataJson[0].BZ16)}}).attr("placeholder",dataJson[0].BZ16);
				$("#BZ17").bind({focus:function(){hideTip(this,dataJson[0].BZ17)},blur:function(){showTip(this,dataJson[0].BZ17)}}).attr("placeholder",dataJson[0].BZ17);
				$("#BZ18").bind({focus:function(){hideTip(this,dataJson[0].BZ18)},blur:function(){showTip(this,dataJson[0].BZ18)}}).attr("placeholder",dataJson[0].BZ18);
				$("#BZ19").bind({focus:function(){hideTip(this,dataJson[0].BZ19)},blur:function(){showTip(this,dataJson[0].BZ19)}}).attr("placeholder",dataJson[0].BZ19);
				$("#BZ20").bind({focus:function(){hideTip(this,dataJson[0].BZ20)},blur:function(){showTip(this,dataJson[0].BZ20)}}).attr("placeholder",dataJson[0].BZ20);
				$("#BZ21").bind({focus:function(){hideTip(this,dataJson[0].BZ21)},blur:function(){showTip(this,dataJson[0].BZ21)}}).attr("placeholder",dataJson[0].BZ21);
				$("#BZ22").bind({focus:function(){hideTip(this,dataJson[0].BZ22)},blur:function(){showTip(this,dataJson[0].BZ22)}}).attr("placeholder",dataJson[0].BZ22);
				$("#BZ23").bind({focus:function(){hideTip(this,dataJson[0].BZ23)},blur:function(){showTip(this,dataJson[0].BZ23)}}).attr("placeholder",dataJson[0].BZ23);
				$("#BZ24").bind({focus:function(){hideTip(this,dataJson[0].BZ24)},blur:function(){showTip(this,dataJson[0].BZ24)}}).attr("placeholder",dataJson[0].BZ24);
				$("#BZ25").bind({focus:function(){hideTip(this,dataJson[0].BZ25)},blur:function(){showTip(this,dataJson[0].BZ25)}}).attr("placeholder",dataJson[0].BZ25);
				$("#BZ26").bind({focus:function(){hideTip(this,dataJson[0].BZ26)},blur:function(){showTip(this,dataJson[0].BZ26)}}).attr("placeholder",dataJson[0].BZ26);
				$("#BZ27").bind({focus:function(){hideTip(this,dataJson[0].BZ27)},blur:function(){showTip(this,dataJson[0].BZ27)}}).attr("placeholder",dataJson[0].BZ27);
				$("#BZ28").bind({focus:function(){hideTip(this,dataJson[0].BZ28)},blur:function(){showTip(this,dataJson[0].BZ28)}}).attr("placeholder",dataJson[0].BZ28);
				$("#BZ29").bind({focus:function(){hideTip(this,dataJson[0].BZ29)},blur:function(){showTip(this,dataJson[0].BZ29)}}).attr("placeholder",dataJson[0].BZ29);
				$("#BZ30").bind({focus:function(){hideTip(this,dataJson[0].BZ30)},blur:function(){showTip(this,dataJson[0].BZ30)}}).attr("placeholder",dataJson[0].BZ30);
				$("#BZ31").bind({focus:function(){hideTip(this,dataJson[0].BZ31)},blur:function(){showTip(this,dataJson[0].BZ31)}}).attr("placeholder",dataJson[0].BZ31);
				$("#BZ32").bind({focus:function(){hideTip(this,dataJson[0].BZ32)},blur:function(){showTip(this,dataJson[0].BZ32)}}).attr("placeholder",dataJson[0].BZ32);
				$("#BZ33").bind({focus:function(){hideTip(this,dataJson[0].BZ33)},blur:function(){showTip(this,dataJson[0].BZ33)}}).attr("placeholder",dataJson[0].BZ33);
				$("#BZ34").bind({focus:function(){hideTip(this,dataJson[0].BZ34)},blur:function(){showTip(this,dataJson[0].BZ34)}}).attr("placeholder",dataJson[0].BZ34);
				$("#BZ35").bind({focus:function(){hideTip(this,dataJson[0].BZ35)},blur:function(){showTip(this,dataJson[0].BZ35)}}).attr("placeholder",dataJson[0].BZ35);
				$("#BZ36").bind({focus:function(){hideTip(this,dataJson[0].BZ36)},blur:function(){showTip(this,dataJson[0].BZ36)}}).attr("placeholder",dataJson[0].BZ36);
				$("#BZ37").bind({focus:function(){hideTip(this,dataJson[0].BZ37)},blur:function(){showTip(this,dataJson[0].BZ37)}}).attr("placeholder",dataJson[0].BZ37);
				$("#BZ38").bind({focus:function(){hideTip(this,dataJson[0].BZ38)},blur:function(){showTip(this,dataJson[0].BZ38)}}).attr("placeholder",dataJson[0].BZ38);
				$("#BZ39").bind({focus:function(){hideTip(this,dataJson[0].BZ39)},blur:function(){showTip(this,dataJson[0].BZ39)}}).attr("placeholder",dataJson[0].BZ39);
				$("#BZ40").bind({focus:function(){hideTip(this,dataJson[0].BZ40)},blur:function(){showTip(this,dataJson[0].BZ40)}}).attr("placeholder",dataJson[0].BZ40);
				$("#BZ41").bind({focus:function(){hideTip(this,dataJson[0].BZ41)},blur:function(){showTip(this,dataJson[0].BZ41)}}).attr("placeholder",dataJson[0].BZ41);
				$("#BZ42").bind({focus:function(){hideTip(this,dataJson[0].BZ42)},blur:function(){showTip(this,dataJson[0].BZ42)}}).attr("placeholder",dataJson[0].BZ42);
				$("#BZ43").bind({focus:function(){hideTip(this,dataJson[0].BZ43)},blur:function(){showTip(this,dataJson[0].BZ43)}}).attr("placeholder",dataJson[0].BZ43);
				$("#BZ44").bind({focus:function(){hideTip(this,dataJson[0].BZ44)},blur:function(){showTip(this,dataJson[0].BZ44)}}).attr("placeholder",dataJson[0].BZ44);
				$("#BZ45").bind({focus:function(){hideTip(this,dataJson[0].BZ45)},blur:function(){showTip(this,dataJson[0].BZ45)}}).attr("placeholder",dataJson[0].BZ45);
				$("#BZ46").bind({focus:function(){hideTip(this,dataJson[0].BZ46)},blur:function(){showTip(this,dataJson[0].BZ46)}}).attr("placeholder",dataJson[0].BZ46);
				$("#BZ47").bind({focus:function(){hideTip(this,dataJson[0].BZ47)},blur:function(){showTip(this,dataJson[0].BZ47)}}).attr("placeholder",dataJson[0].BZ47);
				$("#BZ48").bind({focus:function(){hideTip(this,dataJson[0].BZ48)},blur:function(){showTip(this,dataJson[0].BZ48)}}).attr("placeholder",dataJson[0].BZ48);
				$("#BZ49").bind({focus:function(){hideTip(this,dataJson[0].BZ49)},blur:function(){showTip(this,dataJson[0].BZ49)}}).attr("placeholder",dataJson[0].BZ49);
				$("#BZ50").bind({focus:function(){hideTip(this,dataJson[0].BZ50)},blur:function(){showTip(this,dataJson[0].BZ50)}}).attr("placeholder",dataJson[0].BZ50);
				setTip($("#BZ1"),dataJson[0].BZ1);setTip($("#BZ26"),dataJson[0].BZ26);
				setTip($("#BZ2"),dataJson[0].BZ2);setTip($("#BZ27"),dataJson[0].BZ27);
				setTip($("#BZ3"),dataJson[0].BZ3);setTip($("#BZ28"),dataJson[0].BZ28);
				setTip($("#BZ4"),dataJson[0].BZ4);setTip($("#BZ29"),dataJson[0].BZ29);
				setTip($("#BZ5"),dataJson[0].BZ5);setTip($("#BZ30"),dataJson[0].BZ30);
				setTip($("#BZ6"),dataJson[0].BZ6);setTip($("#BZ31"),dataJson[0].BZ31);
				setTip($("#BZ7"),dataJson[0].BZ7);setTip($("#BZ32"),dataJson[0].BZ32);
				setTip($("#BZ8"),dataJson[0].BZ8);setTip($("#BZ33"),dataJson[0].BZ33);
				setTip($("#BZ9"),dataJson[0].BZ9);setTip($("#BZ34"),dataJson[0].BZ34);
				setTip($("#BZ10"),dataJson[0].BZ10);setTip($("#BZ35"),dataJson[0].BZ35);
				setTip($("#BZ11"),dataJson[0].BZ11);setTip($("#BZ36"),dataJson[0].BZ36);
				setTip($("#BZ12"),dataJson[0].BZ12);setTip($("#BZ37"),dataJson[0].BZ37);
				setTip($("#BZ13"),dataJson[0].BZ13);setTip($("#BZ38"),dataJson[0].BZ38);
				setTip($("#BZ14"),dataJson[0].BZ14);setTip($("#BZ39"),dataJson[0].BZ39);
				setTip($("#BZ15"),dataJson[0].BZ15);setTip($("#BZ40"),dataJson[0].BZ40);
				setTip($("#BZ16"),dataJson[0].BZ16);setTip($("#BZ41"),dataJson[0].BZ41);
				setTip($("#BZ17"),dataJson[0].BZ17);setTip($("#BZ42"),dataJson[0].BZ42);
				setTip($("#BZ18"),dataJson[0].BZ18);setTip($("#BZ43"),dataJson[0].BZ43);
				setTip($("#BZ19"),dataJson[0].BZ19);setTip($("#BZ44"),dataJson[0].BZ44);
				setTip($("#BZ20"),dataJson[0].BZ20);setTip($("#BZ45"),dataJson[0].BZ45);
				setTip($("#BZ21"),dataJson[0].BZ21);setTip($("#BZ46"),dataJson[0].BZ46);
				setTip($("#BZ22"),dataJson[0].BZ22);setTip($("#BZ47"),dataJson[0].BZ47);
				setTip($("#BZ23"),dataJson[0].BZ23);setTip($("#BZ48"),dataJson[0].BZ48);
				setTip($("#BZ24"),dataJson[0].BZ24);setTip($("#BZ49"),dataJson[0].BZ49);
				setTip($("#BZ25"),dataJson[0].BZ25);setTip($("#BZ50"),dataJson[0].BZ50);
			});
			
			$.post("zqb_vote_gettzggdate.action?ggid="+ggid,function (data) {
				var dataJson = eval("(" + data + ")");
				var html = "填写"+dataJson[0].THISMON+"月实际发生内容 "+dataJson[0].NEXTMON+"月预计发生内容";
				var html_ = "填写上周实际发生内容，本周预计发生内容";
				$("#memotd").html(dataJson[0].DD=='01'?html:html_);
			});
		});
		function setTip(obj,txt){
			var html = obj.val();
			if(html==null||html==''||typeof(html)=='undefined'||html=='null'){
				if(typeof(txt)!='undefined') obj.val(txt);
				else    obj.val('');
			}
		}
		function hideTip(obj,txt){
			var html = obj.value;
			if(html==txt){
				obj.value="";
			}
		}
		function showTip(obj,txt){
			var html = obj.value;
			if(html==null||html==''){
				if(typeof(txt)!='undefined') obj.value=txt;
				else obj.value='';
			}
		}
		function saveForm() {
			for(var i=1;i<=50;i++){
				var o1 = "#BZ"+i;
				if($(o1).attr("placeholder")==$(o1).val()){
					$(o1).html("");
					$(o1).val("");
					$(o1).innerHTML="";
				}
			}
			var ggid = $("#ggid").val();
			var khbh = $("#khbh").val();
			var roleid = $("#roleid").val();
			var bz;
			var pz;
			var url = "zqb_vote_updateXpzcfk.action?CUSTOMERNO=" + khbh + "&TZGGID=" + ggid;
			for (var i = 1; i <= $("#forSize").val(); i++) {
				var as = $("input[name='AS" + i + "']:checked").val();
				var pl = $("input[name='PL" + i + "']:checked").val();
				var yjxzsffs = $("input[name='YJXYSFFS" + i + "']:checked").val();
				if(roleid=='3'){
					bz = $("#BZ" + i).val();
					pz = $("#PZ" + i).html();
				}else{
					//bz = $("#BZ" + i).html();
					bz = $("#BZ" + i).val();
					pz = $("#PZ" + i).val();
					
				}
				if (as != '' && as != undefined) {
					url += ("&AS" + i + "=" + encodeURIComponent(as));
				}
				if (pl != '' && pl != undefined) {
					url += ("&PL" + i + "=" + encodeURIComponent(pl));
				}
				if (bz != '' && typeof(bz) != "undefined") {
					url += ("&BZ" + i + "=" + encodeURIComponent(bz));
				}
				if (pz != '' && typeof(pz) != "undefined") {
					url += ("&PZ" + i + "=" + encodeURIComponent(pz));
				}
				if (yjxzsffs != '' && yjxzsffs != undefined) {
					url += ("&YJXYSFFS" + i + "=" + encodeURIComponent(yjxzsffs));
				}
				
			}
			url += ("&forSize=" + $("#forSize").val());
			var encodeUrl = url;
			$.post(encodeUrl,function(data){
				art.dialog.tips("保存成功!",2);
				window.location.reload();
			});
		}
		function cancel(){
			window.onbeforeunload = null;
			if(typeof(api) =="undefined"){
				window.close();
			}else{ 
				api.close(); 
			}
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
	height: 25px;
	font-size: 12px;
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
.content div{
	word-break:break-all;
	white-space:normal;
	width:860px;
	color:red;
}
.breakword{
	word-wrap:break-word;
	word-break:break-all;
	white-space: yes;
}
</style>
</head>
<body class="easyui-layout">
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
	<div class="tools_nav" id="tools_nav">
		<span>
			<a id="save" plain="true" class="easyui-linkbutton l-btn l-btn-plain" onclick="saveForm()" href="#">
				<span class="l-btn-left">
					<span class="l-btn-text icon-save" style="padding-left: 20px;">保存</span>
				</span>
			</a>	
			<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:this.location.reload();">
				<span class="l-btn-left">
					<span class="l-btn-text icon-reload" style="padding-left: 20px;">刷新</span>
				</span>
			</a>
			<!-- <a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-print">打印</a> -->
			<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:cancel();" id="close">
				<span class="l-btn-left">
					<span class="l-btn-text icon-cancel" style="padding-left: 20px;">关闭</span>
				</span>
			</a>
		</span> 
		<span style="float:right">
		<!-- <a href="#" onclick='setMenuLayout()'>布局设置</a>&nbsp;&nbsp; -->
		</span>
	</div>
		<s:form id="editForm" name="editForm" theme="simple">
			<!--表单参数-->
			<div id="border">
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
					cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td class="formpage_title">信披自查反馈</td>
						</tr>
						<tr>
							<td id="help" align="right"><br /></td>
						</tr>
						<tr>
							<td class="line" align="right"><br /></td>
						</tr>
						<tr>
							<td align="left">
								<table class="ke-zeroborder" border="0" cellpadding="0"
									cellspacing="0" width="100%">
									<tbody>
										<table id='iform_grid'  width="100%" style="border:1px solid #efefef">
						                	<tr class="cell">
						                		<td><font size="3">
						                		<table width="100%" border="0"><tr>
						                		<td>客户信息:</td>
						                		<td><s:property value="zqdm"/></td>
						                		<td><s:property value="zqjc"/></td>
						                		<td><s:property value="customername"/></td>
						                		<td>填报人：<s:property value="username"/></td>
						                		</tr></table>
						                		 </font></td>
						                	</tr>
						                	<tr>
												<td id="memotd" colspan='2' style='font-size: 16px;text-align: right;'></td>
											</tr>
						                	<s:if test="zqServer=='xnzq'">
											<tr class="cell">
												<td style="font-size: 16px;" colspan="1">第一部分：<s:if test="zqServer=='hlzq'">注意事项</s:if><s:else>重点提示内容</s:else></td>
									        </tr>
											<s:iterator value="zxlist" status="ll">
							                	<s:if test="#ll.index-1<0||CUSTOMERNO!=list[#ll.index-1].CUSTOMERNO">
									                	<tr class="cell">
									                		<td class="breakword"><s:property value="QUESTION"/></td>
									                	</tr>
									                	<tr class="cell">
									                		<td><font color="red">
									                		<s:if test='zxlist[#ll.index].ANSWER=="是"'>是否知悉：
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
									                		</s:if>
									                		<s:if test='zxlist[#ll.index].ANSWER=="否"'>是否知悉：
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
									                		</s:if>
										                	</font>
									                	</tr>
									                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
									                		<tr class="cell">
									                			<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
									                		</tr>
									                	</s:if>
									                	<s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
									                		<tr class="cell">
									                			<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
									                		</tr>
									                	</s:elseif>
									                	<s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
									                		<tr class="cell">
									                			<td class="content">批注：<div id="PZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
									                		</tr>
									                	</s:if>
									                	<s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
									                		<tr class="cell">
									                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
									                		</tr>
									                	</s:elseif>
												</s:if>
												<s:else>
								                	<tr class="cell">
								                		<td class="breakword"><s:property	value="QUESTION"/></td>
								                	</tr>
								                	<tr class="cell">
								                		<td>
									                		<font color="red">
									                			<s:if test='zxlist[#ll.index].ANSWER=="是"'>是否知悉：
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
										                		</s:if>
										                		<s:if test='zxlist[#ll.index].ANSWER=="否"'>是否知悉：
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
										                		</s:if>
															</font>
														</td>
								                	</tr>
								                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
									                	<tr class="cell">
									                		<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
									                	</tr>
									                </s:if>
									                <s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
									                	<tr class="cell">
									                		<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
									                	</tr>
									                </s:elseif>
									                <s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
									                	<tr class="cell">
									                		<td class="content">批注：<div id="PZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
									                	</tr>
									                </s:if>
									                <s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
									                		<tr class="cell">
									                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
									                		</tr>
									                </s:elseif>
												</s:else>
											</s:iterator>
											<s:iterator value="zxfeedBackList" status="ll">
							                <s:if test="#ll.index-1<0||CUSTOMERNO!=list[#ll.index-1].CUSTOMERNO">
							                	<tr class="cell">
							                		<td class="breakword"><s:property	value="QUESTION"/></td>
							                	</tr>
							                	<tr class="cell">
							                		<td>
							                			<s:if test='zxfeedBackList[#ll.index].ANSWER=="是"'>是否知悉：
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
								                		</s:if>
								                		<s:if test='zxfeedBackList[#ll.index].ANSWER=="否"'>是否知悉：
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
								                		</s:if>
													</td>
							                	</tr>
							                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
							                		<tr class="cell">
							                			<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
							                		</tr>
							                	</s:if>
							                	<s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
							                		<tr class="cell">
							                			<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
							                		</tr>
							                	</s:elseif>
							                	<s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
							                		<tr class="cell">
							                			<td class="content">批注：<div id="BZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
							                		</tr>
							                	</s:if>
							                	<s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
							                		<tr class="cell">
							                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
							                		</tr>
							                	</s:elseif>
							                </s:if>
							                <s:else>
						                	<tr class="cell">
						                		<td class="breakword"><s:property	value="QUESTION"/></td>
						                	</tr>
						                	<tr class="cell">
						                		<td>
						                			<s:if test='zxfeedBackList[#ll.index].ANSWER=="是"'>是否知悉：
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
							                		</s:if>
							                		<s:if test='zxfeedBackList[#ll.index].ANSWER=="否"'>是否知悉：
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
							                		</s:if>
												</td>
						                	</tr>
						                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
						                		<tr class="cell">
						                			<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
						                		</tr>
						                	</s:if>
						                	<s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
						                		<tr class="cell">
						                			<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
						                		</tr>
						                	</s:elseif>
						                	<s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
						                		<tr class="cell">
						                			<td class="content">批注：<div id="BZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
						                		</tr>
						                	</s:if>
						                	<s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
						                		<tr class="cell">
						                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
						                		</tr>
						                	</s:elseif>
							                </s:else>
											</s:iterator>
											</s:if>
											
											
											
						                	<s:if test="zqServer=='hlzq'||zqServer=='xnzq'">
											<tr class="cell">
												<td style="font-size: 16px;" colspan="1">第<s:if test="zqServer=='xnzq'">二</s:if><s:else>一</s:else>部分：<s:if test="zqServer=='hlzq'">日常督导</s:if><s:else>主要事项</s:else></td>
											</tr>
						                	</s:if>
							                <s:iterator value="list" status="ll">
							                	<s:if test="#ll.index-1<0||CUSTOMERNO!=list[#ll.index-1].CUSTOMERNO">
									                	<tr class="cell">
									                		<td class="breakword"><s:property value="QUESTION"/></td>
									                	</tr>
									                	<tr class="cell">
									                		<td><font color="red">
									                		<s:if test='list[#ll.index].ANSWER=="是"'>是否发生：
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
									                		</s:if>
									                		<s:if test='list[#ll.index].ANSWER=="否"'>是否发生：
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
									                		</s:if>
									                		&nbsp;&nbsp;&nbsp;&nbsp;
									                		<s:if test='list[#ll.index].PL=="是"'>是否披露：
									                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
									                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
									                		</s:if>
									                		<s:if test='list[#ll.index].PL=="否"'>是否披露：
									                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
									                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
										                	</s:if>
										                	&nbsp;&nbsp;&nbsp;&nbsp;
										                <s:if test='list[#ll.index].YJXYSFFS=="是"'><s:property value="yjzdms"/>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
										                </s:if>
										                <s:if test='list[#ll.index].YJXYSFFS=="否"'><s:property value="yjzdms"/>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
											            </s:if>
										                	</font></td>
									                	</tr>
									                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
									                		<tr class="cell">
									                			<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
									                		</tr>
									                	</s:if>
									                	<s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
									                		<tr class="cell">
									                			<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
									                		</tr>
									                	</s:elseif>
									                	<s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
									                		<tr class="cell">
									                			<td class="content">批注：<div id="PZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
									                		</tr>
									                	</s:if>
									                	<s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
									                		<tr class="cell">
									                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
									                		</tr>
									                	</s:elseif>
												</s:if>
												<s:else>
								                	<tr class="cell">
								                		<td class="breakword"><s:property	value="QUESTION"/></td>
								                	</tr>
								                	<tr class="cell">
								                		<td>
									                		<font color="red">
									                			<s:if test='list[#ll.index].ANSWER=="是"'>是否发生：
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
										                		</s:if>
										                		<s:if test='list[#ll.index].ANSWER=="否"'>是否发生：
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
										                		</s:if>
										                		&nbsp;&nbsp;&nbsp;&nbsp;
										                		<s:if test='list[#ll.index].PL=="是"'>是否披露：
										                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
										                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
										                		</s:if>
										                		<s:if test='list[#ll.index].PL=="否"'>是否披露：
										                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
										                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
										                		</s:if>
										                		&nbsp;&nbsp;&nbsp;&nbsp;
										                <s:if test='list[#ll.index].YJXYSFFS=="是"'><s:property value="yjzdms"/>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
										                </s:if>
										                <s:if test='list[#ll.index].YJXYSFFS=="否"'><s:property value="yjzdms"/>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
											            </s:if>
															</font>
														</td>
								                	</tr>
								                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
									                	<tr class="cell">
									                		<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
									                	</tr>
									                </s:if>
									                <s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
									                	<tr class="cell">
									                		<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
									                	</tr>
									                </s:elseif>
									                <s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
									                	<tr class="cell">
									                		<td class="content">批注：<div id="PZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
									                	</tr>
									                </s:if>
									                <s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
									                		<tr class="cell">
									                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
									                		</tr>
									                </s:elseif>
												</s:else>
											</s:iterator>
							                <s:iterator value="feedBackList" status="ll">
							                <s:if test="#ll.index-1<0||CUSTOMERNO!=list[#ll.index-1].CUSTOMERNO">
							                	<tr class="cell">
							                		<td class="breakword"><s:property	value="QUESTION"/></td>
							                	</tr>
							                	<tr class="cell">
							                		<td>
							                			<s:if test='feedBackList[#ll.index].ANSWER=="是"'>是否发生：
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
								                		</s:if>
								                		<s:if test='feedBackList[#ll.index].ANSWER=="否"'>是否发生：
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
								                		</s:if>
								                		&nbsp;&nbsp;&nbsp;&nbsp;
							                			<s:if test='feedBackList[#ll.index].PL=="是"'>是否披露：
								                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
								                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
								                		</s:if>
								                		<s:if test='feedBackList[#ll.index].PL=="否"'>是否披露：
								                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
								                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
								                		</s:if>
								                		&nbsp;&nbsp;&nbsp;&nbsp;
										                <s:if test='feedBackList[#ll.index].YJXYSFFS=="是"'><s:property value="yjzdms"/>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
										                </s:if>
										                <s:if test='feedBackList[#ll.index].YJXYSFFS=="否"'><s:property value="yjzdms"/>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
											            </s:if>
													</td>
							                	</tr>
							                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
							                		<tr class="cell">
							                			<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
							                		</tr>
							                	</s:if>
							                	<s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
							                		<tr class="cell">
							                			<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
							                		</tr>
							                	</s:elseif>
							                	<s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
							                		<tr class="cell">
							                			<td class="content">批注：<div id="BZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
							                		</tr>
							                	</s:if>
							                	<s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
							                		<tr class="cell">
							                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
							                		</tr>
							                	</s:elseif>
							                </s:if>
							                <s:else>
						                	<tr class="cell">
						                		<td class="breakword"><s:property	value="QUESTION"/></td>
						                	</tr>
						                	<tr class="cell">
						                		<td>
						                			<s:if test='feedBackList[#ll.index].ANSWER=="是"'>是否发生：
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
							                		</s:if>
							                		<s:if test='feedBackList[#ll.index].ANSWER=="否"'>是否发生：
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
							                		</s:if>
							                		&nbsp;&nbsp;&nbsp;&nbsp;
						                			<s:if test='feedBackList[#ll.index].PL=="是"'>是否披露：
							                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
							                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
							                		</s:if>
							                		<s:if test='feedBackList[#ll.index].PL=="否"'>是否披露：
							                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
							                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
							                		</s:if>
							                		&nbsp;&nbsp;&nbsp;&nbsp;
										                <s:if test='feedBackList[#ll.index].YJXYSFFS=="是"'><s:property value="yjzdms"/>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
										                </s:if>
										                <s:if test='feedBackList[#ll.index].YJXYSFFS=="否"'><s:property value="yjzdms"/>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
										                <label><input type="radio" name="YJXYSFFS<s:property value="COUNT"/>" id="YJXYSFFS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
											            </s:if>
												</td>
						                	</tr>
						                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
						                		<tr class="cell">
						                			<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
						                		</tr>
						                	</s:if>
						                	<s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
						                		<tr class="cell">
						                			<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
						                		</tr>
						                	</s:elseif>
						                	<s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
						                		<tr class="cell">
						                			<td class="content">批注：<div id="BZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
						                		</tr>
						                	</s:if>
						                	<s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
						                		<tr class="cell">
						                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
						                		</tr>
						                	</s:elseif>
							                </s:else>
											</s:iterator>
											
											
											<s:if test="zqServer=='hlzq'">
											<tr class="cell">
												<td style="font-size: 16px;" colspan="1">第二部分：<s:if test="zqServer=='hlzq'">注意事项</s:if><s:else>重点提示内容</s:else></td>
									        </tr>
											<s:iterator value="zxlist" status="ll">
							                	<s:if test="#ll.index-1<0||CUSTOMERNO!=list[#ll.index-1].CUSTOMERNO">
									                	<tr class="cell">
									                		<td class="breakword"><s:property value="QUESTION"/></td>
									                	</tr>
									                	<tr class="cell">
									                		<td><font color="red">
									                		<s:if test='zxlist[#ll.index].ANSWER=="是"'>是否知悉：
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
									                		</s:if>
									                		<s:if test='zxlist[#ll.index].ANSWER=="否"'>是否知悉：
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
									                		</s:if>
										                	</font>
									                	</tr>
									                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
									                		<tr class="cell">
									                			<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
									                		</tr>
									                	</s:if>
									                	<s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
									                		<tr class="cell">
									                			<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
									                		</tr>
									                	</s:elseif>
									                	<s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
									                		<tr class="cell">
									                			<td class="content">批注：<div id="PZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
									                		</tr>
									                	</s:if>
									                	<s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
									                		<tr class="cell">
									                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
									                		</tr>
									                	</s:elseif>
												</s:if>
												<s:else>
								                	<tr class="cell">
								                		<td class="breakword"><s:property	value="QUESTION"/></td>
								                	</tr>
								                	<tr class="cell">
								                		<td>
									                		<font color="red">
									                			<s:if test='zxlist[#ll.index].ANSWER=="是"'>是否知悉：
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
										                		</s:if>
										                		<s:if test='zxlist[#ll.index].ANSWER=="否"'>是否知悉：
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
										                		</s:if>
															</font>
														</td>
								                	</tr>
								                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
									                	<tr class="cell">
									                		<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
									                	</tr>
									                </s:if>
									                <s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
									                	<tr class="cell">
									                		<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
									                	</tr>
									                </s:elseif>
									                <s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
									                	<tr class="cell">
									                		<td class="content">批注：<div id="PZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
									                	</tr>
									                </s:if>
									                <s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
									                		<tr class="cell">
									                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
									                		</tr>
									                </s:elseif>
												</s:else>
											</s:iterator>
											<s:iterator value="zxfeedBackList" status="ll">
							                <s:if test="#ll.index-1<0||CUSTOMERNO!=list[#ll.index-1].CUSTOMERNO">
							                	<tr class="cell">
							                		<td class="breakword"><s:property	value="QUESTION"/></td>
							                	</tr>
							                	<tr class="cell">
							                		<td>
							                			<s:if test='zxfeedBackList[#ll.index].ANSWER=="是"'>是否知悉：
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
								                		</s:if>
								                		<s:if test='zxfeedBackList[#ll.index].ANSWER=="否"'>是否知悉：
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
								                		</s:if>
													</td>
							                	</tr>
							                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
							                		<tr class="cell">
							                			<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
							                		</tr>
							                	</s:if>
							                	<s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
							                		<tr class="cell">
							                			<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
							                		</tr>
							                	</s:elseif>
							                	<s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
							                		<tr class="cell">
							                			<td class="content">批注：<div id="BZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
							                		</tr>
							                	</s:if>
							                	<s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
							                		<tr class="cell">
							                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
							                		</tr>
							                	</s:elseif>
							                </s:if>
							                <s:else>
						                	<tr class="cell">
						                		<td class="breakword"><s:property	value="QUESTION"/></td>
						                	</tr>
						                	<tr class="cell">
						                		<td>
						                			<s:if test='zxfeedBackList[#ll.index].ANSWER=="是"'>是否知悉：
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>是</label>
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />否</label>
							                		</s:if>
							                		<s:if test='zxfeedBackList[#ll.index].ANSWER=="否"'>是否知悉：
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> />是</label>
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="ORGUSERROLRID!=3 || LOCKSTATUS!=0">disabled="disabled"</s:if> checked="checked"/>否</label>
							                		</s:if>
												</td>
						                	</tr>
						                	<s:if test="(ORGUSERROLRID!=3 && BZ!='') ||  (LOCKSTATUS==1 && BZ!='')">
						                		<tr class="cell">
						                			<td class="content">情况说明：<div  color="red"><s:property value="BZ"/></div> <input type="hidden" id="BZ<s:property value="COUNT"/>" value="<s:property value="BZ"/>" /></td>
						                		</tr>
						                	</s:if>
						                	<s:elseif test="ORGUSERROLRID==3 && LOCKSTATUS!=1">
						                		<tr class="cell">
						                			<td class="content">情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
						                		</tr>
						                	</s:elseif>
						                	<s:if test="(ORGUSERROLRID==3 && PZ!='') ||  (LOCKSTATUS==1 && PZ!='')">
						                		<tr class="cell">
						                			<td class="content">批注：<div id="BZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
						                		</tr>
						                	</s:if>
						                	<s:elseif test="ORGUSERROLRID!=3 && LOCKSTATUS!=1">
						                		<tr class="cell">
						                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:1536,required:false} " style="width:750px;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
						                		</tr>
						                	</s:elseif>
							                </s:else>
											</s:iterator>
											</s:if>
										</table>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<input type="hidden" id="ggid" name="ggid" value="${ggid}" class="{string:true}" />
			<input type="hidden" id="khbh" name="khbh" value="${khbh}" class="{string:true}"/>
			<input type="hidden" id="forSize" name="forSize" value="50" class="{string:true}"/>
			<input type="hidden" id="lockStatusForToolsNav" name="lockStatusForToolsNav" value="${lockStatusForToolsNav}" class="{string:true}"/>
			<input type="hidden" id="roleid" name="roleid" value="${roleid}" class="{string:true}"/>
		</s:form>
	</div>
</body>
</html>