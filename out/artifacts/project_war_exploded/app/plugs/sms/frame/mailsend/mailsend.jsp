<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>mailform.html</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
<script type="text/javascript" src="iwork_js/engine/mailIformpage.js"></script>
<script type="text/javascript" src="iwork_js/json.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="iwork_js/plugs/gantt2/jquery-ui-1.8.4.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.position.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.autocomplete.js"></script>
<link rel='stylesheet' type='text/css' href='iwork_js/plugs/gantt2/jquery-ui-1.8.4.css'>
<link rel='stylesheet' type='text/css' href='iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css'>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(
			function() {
				mainFormValidator = $("#iformMain")
						.validate({});
				mainFormValidator.resetForm();
	});
	function checkEmail(str){
	    var re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
		return re.test(str);
	}
	function sendMailMethod() {
		var valid = mainFormValidator.form(); //执行校验操作
		if (!valid) {
			return false;
		}
		var sjr = $("#sjr").val();
		var content = $("#body").val();
		var title = $("#subject").val();
		var filearr = $("#fjsc").nextAll("div");
		var file = "";
		/*邮箱合法性验证开始*/
		var sjrright="";
		var sjrwrong = "以下邮箱格式错误:\n";
		var sjrarr = sjr.split(/;|,/);
		var mail = "";var leftlt;var rightlt;
		for(var i=0;i<sjrarr.length;i++){
			if(sjrarr[i]!=null&&sjrarr[i]!=""){
				leftlt = sjrarr[i].indexOf("<");
				rightlt = sjrarr[i].indexOf(">");
				if(leftlt>-1&&rightlt>-1){
					mail = sjrarr[i].substring(leftlt+1,rightlt);
				}else{
					mail = sjrarr[i];
				}
				if(checkEmail(mail)){
					sjrright+=(sjrarr[i]+";");
				}else{
					sjrwrong+=(sjrarr[i]+"\n");
				}
			}
		}
		/*邮箱合法性验证结束*/
		if (filearr.length !== 0) {
			for (var i = 0; i < filearr.length; i++) {
				if($(filearr[i]).attr("id")!=null&&$(filearr[i]).attr("id")!=""){
					file = file + $(filearr[i]).attr("id") + ";";
				}
			}
		}
		if (content == "") {
			alert("发送内容不能为空!");
			return;
		}
		if (sjr == "") {
			alert("收件人不能为空！");
			return;
		}
		//var url="messagesend.action?content11="+content11+"&phone11="+phone11;
		var url = "mailsend.action";
		$.post(url, {
			sjr : sjrright,
			content : content,
			title : title,
			file : file
		}, function(data) {
			var str = "";
			var msg = data.msg;
			var errForbid = data.errForbid;
			if("以下邮箱格式错误:\n"!=sjrwrong){
				str+=(msg+"\n"+sjrwrong);
			}else{
				str+=(msg);
			}
			if(errForbid.length>0){
				  str += "\n其中五分钟内重复发送的邮箱如下(重复未发送)：";
				  var result=errForbid.split(",");
				  for(var i=0;i<result.length;i++){
					  str += "\n";
				      str += result[i];
				  }
			  }
			alert(str);
			return;
		},"json");
	}
	function showItem(cid) {
		var url = "zqb_mail_getXX.action?id=" + cid;
		$("#mainFrame").attr("src", url);
	
	}
</script>
<script type="text/javascript">
$(function() {
	$("#sjr").autocomplete({
		source:function(request,response) {
			$.ajax({
				url:"zqb_customer_userassociate.action",
				dataType:"json",
				data:{
					commonstr:$("#sjr").val().split(";")[$("#sjr").val().split(";").length-1]//用最后一个输入的用户名或部门名做查询条件
				},
				success: function(data) {
					var arr = $("#sjr").val().split(";");
					var emails = "";
					for(var i=0;i<arr.length-1;i++){
						emails+=(arr[i]+";");
					}
					response($.map(data, function(item) {
						return {
							label:item.USERNAME+'--'+item.DEPARTMENTNAME,
							value:uniq(emails+item.USERNAME+'<'+item.EMAIL+'>;')
						}
					}));
				}
			});
		},
		select:function(event,ui){},
		minLength : 1,
		minChars : 0,
		matchContains : true,
		scrollHeight : 660,
		scroll : true
	});
});
function uniq(str){
	var arr = str.split(";");
	arr = arr.uniquel();
	var emails = "";
	for(var i=0;i<arr.length-1;i++){
		emails+=(arr[i]+";");
	}
	return emails;
}
Array.prototype.uniquel = function(){
	var res = [this[0]];
	for(var i = 1; i < this.length; i++){
		var repeat = false;
		for(var j = 0; j < res.length; j++){
			if(this[i] == res[j]){
				repeat = true;
				break;
			}
		}
		if(!repeat){
			res.push(this[i]);
		}
	}
	return res;
}
</script>
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
	height: 20px;
	padding: 2px;
	padding-left: 20px;
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
	border: 1px solid #ccc;
}

.grid tr:hover {
	background-color: #efefef;
}

.grid td {
	padding: 5px;
	line-height: 16px;
}
.ui-autocomplete {
    max-height: 500px;
    max-width: 500px;
    overflow-y: auto;
    /* 防止水平滚动条 */
    overflow-x: hidden;
}
</style>
</head>

<body class="easyui-layout">
	<div region="center" border="false">
		<div id="mainFrameTab" style="height:100%;border:0px" class="easyui-tabs" fit="true">
			<div title="发送邮件" border="false" style="height:100%;border:0px" cache="false" class="easyui-layout">
				<div region="west" style="width:250px;height:100%;background-color:#efefef" border="false">
					<ul id="mailTree" class="ztree"></ul>
				</div>
				<div region="center" align="left" style="width:500px;margin:5px;" border="false">
					<form id="iformMain" name="iformMain" method="post" action='saveIform.action'>
						<table width="100%">
							<tr>
								<td width="5%" valign="top"><div align="left">收件人</div></td>
								<td width="40%" align="left">
									<div>
										<div class="ui-widget">
										<textarea name="sjr" cols="40" rows="5" id="sjr" style="width: 500px;" class="{string:true}"></textarea>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td width="5%" valign="top"><div align="left">主题</div></td>
								<td width="40%" align="left">
									<div>
										<input name="subject" type="text" class='{string:true,maxlength:200}' id="subject" style="width: 500px;">
									</div>
								</td>
							</tr>
							<tr height="50px">
								<td width="5%" valign="top"><div align="left">邮件内容</div></td>
								<td width="40%" align="left">
									<div>
										<textarea name="body" rows="20" id="body" style="width: 500px;" class='{maxlength:4000}'></textarea>
									</div>
								</td>
							</tr>
							<tr height="100px">
								<td width="5%" valign="top"><div align="left">添加附件</div></td>
								<td width="40%" align="left" valign="top">
									<div id='DIVYJFS'>
										<button id="fjsc" onclick="showUploadifyPage();return false;">附件上传</button>
										<input type="button" name="sendButton" id="sendButton"
											value="发送" onclick="sendMailMethod()" />
										<div>
											<input type=hidden id='YJFS' class='{maxlength:2048}' name='YJFS' value='' />
										</div>
										<script>
											function showUploadifyPage() {
												mainFormAlertFlag = false;
												saveSubReportFlag = false;
												var valid = mainFormValidator.form();
												if (!valid) {
													return false;
												}
												mainFormAlertFlag = false;
												saveSubReportFlag = false;
												uploadifyDialog('YJFS','YJFS','DIVYJFS','', 'true','', '');
											}
										</script>
									</div>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
			<div title="邮件日志" border="false" style="height:100%;border:0px" cache="false" class="easyui-layout">
				<div region="west" style="width:250px;height:100%;background-color:#efefef" border="false">
					<table class="grid">
						<tr>
							<th width="25%">序号</th>
							<th width="50%">收件人(邮箱)</th>
							<th width="25%">状态</th>
						</tr>
						<s:iterator value="list" status="ll">
							<tr>
								<td><s:property value="ID" /></td>
								<td><a href="javascript:showItem('<s:property value="ID"/>')"><s:property value="NAME" />( <s:property value="EMAIL" /> )</a></td>
								<td><s:property value="STATUS" /></td>
							</tr>
						</s:iterator>
					</table>
				</div>
				<div region="center" align="left" style="width:500px;height:100%;margin:5px;" border="false">
					<iframe id="mainFrame" name="mainFrame" scrolling="no" frameborder="0" style="width:100%;height:100%;"></iframe>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
<script language="JavaScript">
	jQuery.validator.addMethod("string", function(value, element) {
		var sqlstr = [ " and ", " exec ", " count ", " chr ", " mid ",
				" master ", " or ", " truncate ", " char ", " declare ",
				" join ", "insert ", "select ", "delete ", "update ",
				"create ", "drop " ]
		var patrn = /[`~!#$%^&*+?"{},'[\]]/im;
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