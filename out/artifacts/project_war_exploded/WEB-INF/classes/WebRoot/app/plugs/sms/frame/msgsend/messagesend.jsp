<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<html>
<head>
<title>短信发送</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/common.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
mainFormValidator =  $("#messagesend").validate({});
 mainFormValidator.resetForm();
});
	$(function() {
		initTreeTo();
		postGetAll();
	});
	function initTreeTo() {
		var setting = {
			async: {
                enable:true,
                url: "showPhoneBookJsonTreeNew.action",
                dataType:"json",
                autoParam:[ "id", "nodeType"]
	        },
	        data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				onClick : onClick
			}
		};
		$.fn.zTree.init($("#phoneTree"), setting);//加载导航树
	}
	
	function onClick(event, treeId, treeNode, clickFlag) {
		var dxfsr = $("#phone").val();
		if (treeNode.phone != undefined) {
			var strs = new Array();
			strs = dxfsr.split(";"); 
			for (var i = 0; i < strs.length; i++) {
				var s = strs[i];
				if (s == treeNode.phone + "[" + treeNode.name + "]") {
					return;
				}
			}
			$("#phone").val(dxfsr + treeNode.phone + "[" + treeNode.name + "];");
		}
	}
	function postGetAll() {
		var setting = {
			async : {
				enable : true,
				url : "getallphone.action",
				dataType : "json",
				autoParam : [ "id", "nodeType", "companyId"]
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				onClick : setVlaueClick
			}
		};
		$.fn.zTree.init($("#justphoneTree"), setting);
	}
	function setVlaueClick(event, treeId, treeNode, clickFlag) {
		var dxfsr = $("#phone").val();
		if(treeNode.value!=undefined&&treeNode.value!=""){
			var strs = new Array();
			strs = dxfsr.split(";"); 
			for (var i = 0; i < strs.length; i++) {
				var s = strs[i];
				if ((s+";") == treeNode.value) {
					return;
				}
			}
			$("#phone").val(dxfsr + treeNode.value);
		}
	}

	function getGroupValue(id){
		var dxfsr = $("#phone").val();
		var stra = new Array();
		stra = dxfsr.split(";");
		
		var url = "getgroupvalue.action";
		$.post(url,{id:id}, function(data) {
			var strb = new Array();
			strb = data.split(";");
			var flag = false;
			
			for (var i = 0; i < strb.length; i++) {
				flag = false;
				var b = strb[i];
				for (var j = 0; j < stra.length; j++) {
					var a = stra[j];
					if(a==b){
						flag=true;
					}
				}
				if(!flag){
					$("#phone").val($("#phone").val() + b + ";");
					flag=false;
					stra = $("#phone").val().split(";");
				}
			}
		});
	}

	//选择当前用户在审批流程中审批的角色的用户电话号码
	function loadInfo() {
		$.post("getcompanyphonelist.action", function(data) {
			$("#phone").val(data);
		});
	}
	
	function showPhoneNum(dialogName) {
		var obj = new Object();
		var pageurl = "url:phonebookSelectnum.action";
		obj.dialogName = dialogName;
		art.dialog.open(pageurl,{
			id : "multbook",
			cover : true,
			title : "加载电话号码簿",
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			width : 350,
			cache : false,
			lock : true,
			esc : true,
			height : 480,
			iconTitle : false,
			extendDrag : true,
			autoSize : true,
			data : obj
		});
	}
	
	function showItem(cid) {
		var url = "zqb_phone_getXX.action?id=" + cid;
		$("#mainFrame").attr("src", url);
	}
</script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"> </script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
<script type="text/javascript" src="iwork_js/json.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="iwork_js/plugs/gantt2/jquery-ui-1.8.4.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.position.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.autocomplete.js"></script>
<link rel='stylesheet' type='text/css' href='iwork_js/plugs/gantt2/jquery-ui-1.8.4.css'>
<link rel='stylesheet' type='text/css' href='iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css'>
<script type="text/javascript">
var phoneflg = true;
var contentflg = true;
$(function() {
	$("#phone").autocomplete({
		source:function(request,response) {
			$.ajax({
				url:"zqb_customer_userassociate.action",
				dataType:"json",
				data:{
					commonstr:$("#phone").val().split(";")[$("#phone").val().split(";").length-1]//用最后一个输入的用户名或部门名做查询条件
				},
				success: function(data) {
					var arr = $("#phone").val().split(";");
					var cellphone = "";
					for(var i=0;i<arr.length-1;i++){
						cellphone+=(arr[i]+";");
					}
					response($.map(data, function(item) {
						return {
							label:item.USERNAME+'--'+item.DEPARTMENTNAME,
							value:uniq(cellphone+item.MOBILE+'['+item.USERNAME+'];')
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
	var phonelen = 0;
	$("#phone").change(function(){
		phonelen = strlen($(this).val());
		if(phonelen>4000){
			phoneflg = false;
			$("#phonelable").html("请输入一个长度最多是 4000 的字符串");
		}else{
			phoneflg = true;
			$("#phonelable").html("");
		}
	});
	var contentlen = 0;
	
	$("#content").change(function(){
		contentlen = strlen($(this).val());
		if(contentlen>240){
			contentflg = false;
			$("#contentlable").html("请输入一个长度最多是 240 的字符串");
		}else{
			contentflg = true;
			$("#contentlable").html("");
		}
	});
});

function strlen(str){  
	var len = 0;
	for (var i=0; i<str.length; i++) {
		var c = str.charCodeAt(i);
		//单字节加1
		if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {
			len++;
		}else {
			len+=2;   
		}   
	}   
	return len;  
}

function uniq(str){
	var arr = str.split(";");
	arr = arr.uniquel();
	var phones = "";
	for(var i=0;i<arr.length-1;i++){
		phones+=(arr[i]+";");
	}
	return phones;
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
form.ifromMain label.error, label.error {
	/* remove the next line when you have trouble in IE6 with labels in list */
	color: red;
	font-style: italic
}
.ui-autocomplete {
    max-height: 500px;
    max-width: 495px;
    overflow-y: auto;
    /* 防止水平滚动条 */
    overflow-x: hidden;
  }
</style>
</head>
<body class="easyui-layout">
	<!--center-->
	<div region="center" border="false">
		<div id="mainFrameTab" style="height:100%;border:0px;width:auto;" class="easyui-tabs" fit="true">
			<div title="发送短信" border="false" style="height:700px;width:99%;border:0px" cache="false" class="easyui-layout">
				<div id="nav">
					<div class='tit2'>
						<span id="url_route" name="url_route"> </span>
					</div>
				</div>
				<div region="west" style="width:250px;height:100%;overflow:auto;background-color:#efefef;float:left;border:0px;padding:0px;line-height:20px;" border="false">
					<ul id="phoneTree" class="ztree"></ul>
				</div>
				<div region="east" style="width:250px;height:100%;overflow:auto;background-color:#efefef;float:right;border:0px;padding:0px;line-height:20px;" border="false">
					<ul id="justphoneTree" class="ztree"></ul>
				</div>
				<div region="center" style='width:750px;float:left;margin:0;padding:0;margin-top:20px;' border="false">
					<form name="messagesend" id="messagesend" method="post">
						<div style='width:500px;float:left;margin:0;padding:0;margin-left:5px;'>
							<div id='up' style='width:100%;'>
								<div style='text-align:left;width:100%;font-weight:bold;'>
									接收号码：<img src='/app/plugs/sms/images/help0.png'
										title='1.接收人姓名写在手机号后，用方括号括起。如不使用通配符%name%，可省略。&#13;&#13;示例：&#13;13811111111[张三]，13611111111 &#10;&#13;2.多个手机号用逗号或分号间隔。&#13;&#13; 示例：&#13;13811111111,13611111111,13511111111' />
								</div>
								<div><!-- 督导管理部     场外市场部 -->
									<div class="ui-widget">
										<textarea rows='5' style='width:100%;font-size:12px;line-height:normal;' id='phone' name='phone' class="{string:true}"></textarea>
										<label id="phonelable" style="display:block;color: red;font-style: italic;"></label>
									</div>
								</div>
							</div>
							<div id='down' style='width:100%;margin-top:7px;'>
								<div style='width:100%;'>
									<div id='up_title_left' style='text-align:left;float:left;width:50%;font-weight:bold;'> 短信内容：<img src='/app/plugs/sms/images/help0.png' title='1. 短信内容中可包含以下通配符：&#13;%name% 姓名&#13;%attr1% 属性一（仅限号码簿中手机号）&#13;%attr2% 属性二（仅限号码簿中手机号）&#13;%attr3% 属性三（仅限号码簿中手机号）' /> </div>
									<div id='up_title_right' style='float:right;width:50%;text-align:right;'></div>
								</div>
								<div style='width:100%;'>
									<textarea rows='13' class="{string:true}" style='width:100%;float:clear;font-size:12px;line-height:normal;' id='content' name='content'></textarea>
									<label id="contentlable" style="display:block;color: red;font-style: italic;"></label>
								</div>
								<div style='float:right;'>
									<input type='text' readonly=true id='content_len' style='color:red;border:none;text-align:right;width:250px;' />
								</div>
								<!-- <div style='float:left;text-align:left;'><input type=button value='我的号码簿'  onclick="showPhoneNum()"/></div> -->
								<div style='width:100%;text-align:center;margin-top:5px;'>
									<input type='text' readonly=true id='phone_count'
										style='color:#f05050;border:none;text-align:right;width:370px;' />
								</div>
								<div style='margin-top:10px;margin-bottom:10px;width:100%;text-align:center;'>
									<center>
										<input type=button onclick="sendmsg()" value='发送短信' />
										<!-- <span>按钮只能督导角色、场外角色能看</span> -->
										<s:if test="isview!=false">
										&nbsp;&nbsp; <input type=button onclick="loadInfo();" value='选择挂牌公司' />
										</s:if>
									</center>
								</div>
							</div>
						</div>
						<!--/center-->
						<div style='width:230px;float:left;font-size:12px;margin-left:10px;border: 1px solid #CCCCCC;margin-top:18px;margin-bottom:0px;text-align:left;background-color:#FDFFE1;color:#5050ff'>
							<!--808090'-->
							<div style='margin-left:5px;margin-top:5px;margin-bottom:5px;width:95%;boder:1px dashed #ccc;'>
								<div style='color:#808090;font-weight:bold;margin-bottom:5px;'>
									使用说明：</div>
								1. 本功能不得用于群发广告短信及各种违法违纪内容。<br /> 2. 如短信额度不足，请使用"短信使用权限申请"流程申请充值。<br />
								3. 每次短信内容最大长度240字符。汉字算2个字符，字母、数字均算1个字符。<br /> 4. 多个号码中间以分号或逗号间隔。<br />
							</div>
							<div style='margin-left:5px;margin-right:0px;margin-top:10px;margin-bottom:5px;width:95%;boder:1px dashed #ccc;'>
								<div style='color:#808090;font-weight:bold;margin-bottom:0px;'>
									通配符使用说明：</div>
								1. 在短信内容中可插入通配符，用于在短信中带出接收人姓名等内容。可用的通配符：<br /> %name%：接收人姓名；<br />
								2. 示例：<br /> 短信内容：%name%，你好！<br /> 接收号码：13800138000[张三]<br />
								实际发送：张三，你好！<br />
								<!--span style='color:#5050ff;font-weight:bold;margin-left:10px;'>%name%</span>：接收人姓名，可写在号码后，用方括号（[&nbsp;&nbsp;]）括起。选号时会自动带出。<br />
								<span style='color:#5050ff;font-weight:bold;margin-left:10px;'>%attr1%</span>、<span style='color:#5050ff;font-weight:bold;'>%attr2%</span>、<span style='font-weight:bold;color:#5050ff'>%attr3%</span>：属性一、二、三，仅支持号码簿中的号码。<br />
								3. 示例：<br />
								<span style='font-weight:bold;margin-left:10px;'>短信内容</span>：%name%，你好！<br />
								<span style='font-weight:bold;margin-left:10px;'>接收号码</span>：13800138000[张三]<br />
								<span style='font-weight:bold;margin-left:10px;'>实际发送</span>：张三，你好！-->
							</div>
						</div>
				</div>
				</form>
				<!--/center-->
			</div>
			<div title="短信日志" border="false" style="height:100%;border:0px" cache="false" class="easyui-layout">
				<div region="west" style="width:250px;height:100%;background-color:#efefef" border="false">
					<table class="grid">
						<tr>
							<th width="20%" style="text-align: left;">序号</th>
							<th width="50%">收件人(手机号)</th>
							<th width="30%">状态</th>
						</tr>
						<s:iterator value="list" status="ll">
							<tr>
								<td><s:property value="#ll.count" /></td>
								<td><a href="javascript:showItem('<s:property value="ID"/>')"><s:property value="NAME" />(<s:property value="MOBILE" />)</a></td>
								<td><s:property value="STATUS" /></td>
							</tr>
						</s:iterator>
					</table>
				</div>
				<div region="center" align="left"
					style="width:500px;height:100%;margin:5px;" border="false">
					<iframe id="mainFrame" name="mainFrame" scrolling="no" frameborder="0" style="width:100%;height:100%;"></iframe>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="iwork_js/plugs/messagesend.js"></script>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{}']/im;
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