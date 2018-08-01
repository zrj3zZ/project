<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
<title>项目管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="admin/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"></script>
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
		    total:<s:property value="runNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
		$('#bb').pagination({
			total : <s:property value="closeNum"/>,
			pageNumber : <s:property value="pageNumber1"/>,
			pageSize : <s:property value="pageSize1"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMbb(pageNumber, pageSize);
			}
		});
		$("#search").click(function(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return ;
			}
			var customername = $("#CUSTOMERNAME").val();
			var sshy = $("#SSHY").val();
			var czbm = $("#CZBM").val();
			var cyrname = $("#CYRNAME").val();
			var seachUrl = encodeURI("zqb_gpfxproject_sx_index.action?customername="+ customername + "&sshy=" + sshy +"&czbm="+czbm+"&cyrname="+cyrname);
			window.location.href = seachUrl;
		});
		//复选/全选
        $("#chkAll").bind("click", function () {
        	$("[name =colname]:checkbox").attr("checked", this.checked);
        });
	});

	
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return;
	}
	function submitMbb(pageNumber, pageSize) {
		$("#pageNumber1").val(pageNumber);
		$("#pageSize1").val(pageSize);
		$("#frmMain1").submit();
		return;
	}

	function addItem() {
		var formid=$("#formid").val();
		var demid=$("#demid").val();
		var url = 'createFormInstance.action?formid=' + formid + '&demId=' + demid;
		/* art.dialog.open(url,{
			title:'股票发行项目信息',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:800, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		}); */
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width + ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	}
	
	function editItem(instanceid) {
		var formid=$("#formid").val();
		var demid=$("#demid").val();
		var url = 'openFormPage.action?instanceId=' + instanceid + '&formid=' + formid + '&demId=' + demid;
		/* art.dialog.open(url,{
			title:'股票发行项目信息',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:800, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		}); */
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}

	function closeItem() {
		
		var list = $('[name=colname]').length;
		var a=0;
		var instanceids = new Array();	
		for(var n = 0; n < list; n++){
			if($('[name=colname]')[n].checked==false&&$('[name=colname]')[n].id!='chkAll'){
				a++;
				if(a==list-1){
					$.messager.alert('提示信息','请选择您要关闭的项目!','info');  
					return;
				}
			}
			if($('[name=colname]')[n].checked==true&&$('[name=colname]')[n].id!=String('chkAll')){
				instanceids.push($('[name=colname]')[n].id);
			}
		}
		
		var pageUrl = "zqb_gpfxproject_sx_closedialog.action?instanceids="+instanceids.join(",");
		art.dialog.open(pageUrl,{
			title:'股票发行项目关闭原因',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:670,
			cache:false,
			lock: true,
			height:530, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});
		/* try{
			$.messager.confirm('确认','确认要关闭?',function(result){ 
				if(result){
					var list = $('[name=colname]').length;
					var a=0;
					var ErrNum = 0;	
					for(var n = 0; n < list; n++){
						if($('[name=colname]')[n].checked==false&&$('[name=colname]')[n].id!='chkAll'){
							a++;
							if(a==list-1){
								$.messager.alert('提示信息','请选择您要关闭的项目!','info');  
								return;
							}
						}
						if($('[name=colname]')[n].checked==true&&$('[name=colname]')[n].id!=String('chkAll')){{
							var pageUrl = "zqb_gpfxproject_sx_closeitem.action";
							$.post(pageUrl,{instanceid:$('[name=colname]')[n].id},function(data){ 
								if(data=='success'){
									window.location.reload();
								}else{
									alert("项目关闭失败，请重试");
								}
							});
						}
						}
					}
				}
			});
		}catch(e){} */
	}
	function setCloseReason(instanceid){
		var pageUrl = "zqb_gpfxproject_sx_setclosereason.action?instanceid="+instanceid;
		art.dialog.open(pageUrl,{
			title:'股票发行项信息',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1000,
			cache:false,
			lock: true,
			height:800, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});
	}
	function expFixedReserve(){
		var pageUrl= 'zqb_gpfxproject_sx_expfixedreserveindex.action';
		art.dialog.open(pageUrl,{
			title : '企业定增储备项目',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 600,
			cache : false,
			lock : true,
			height : 340,
			iconTitle : false,
			extendDrag : true,
			autoSize : false
		});
	}
	function expIssueObject(){
		var pageUrl = encodeURI("zqb_gpfxproject_sx_expissueobject.action");
		window.location.href = pageUrl; 
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
			<a class="easyui-linkbutton" plain="true" href="javascript:addItem();" iconCls="icon-add">添加项目</a>
			<a class="easyui-linkbutton" plain="true" href="javascript:this.location.reload();" iconCls="icon-reload">刷新</a>
			<a class="easyui-linkbutton" plain="true" href='javascript:closeItem();' iconCls="icon-remove">关闭项目</a>
			<a class="easyui-linkbutton" plain="true" href="javascript:expFixedReserve();" iconCls="icon-excel-exp">导出企业定增储备项目</a>
			<a class="easyui-linkbutton" plain="true" href="javascript:expIssueObject();" iconCls="icon-excel-exp">导出新增定增项目的发行对象</a>
		</div>
		<div style="padding:5px">
			<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
			<form id="ifrmMain" name="ifrmMain">
				<table width="99%" cellspacing="0" cellpadding="0" border="0">
					<tbody>
						<tr>
							<td style="padding-top:10px;padding-bottom:10px;">
								<table width="100%" cellspacing="0" cellpadding="0" border="0">
									<tbody>
										<tr>
											<td class="searchtitle" style="text-align:right;">客户名称</td>
											<td class="searchdata">
												<input id=CUSTOMERNAME class="{maxlength:128,required:false,string:true}" type="text" value="<s:property value="customername" />" name="CUSTOMERNAME" style="width:100px">
											</td>
											<td class="searchtitle" style="text-align:right;">所属行业</td>
											<td class="searchdata">
												<input id="SSHY" class="{maxlength:64,required:false,string:true}" type="text" value="<s:property value="sshy" />" name="SSHY" style="width:100px">
											</td>
											<td class="searchtitle" style="text-align:right;">承做部门</td>
											<td class="searchdata">
												<input id="CZBM" class="{maxlength:128,required:false,string:true}" type="text" value="<s:property value="czbm" />" name="CZBM" style="width:100px;">
											</td>
											<td class="searchtitle" style="text-align:right;">参与人姓名</td>
											<td class="searchdata">
												<input id="CYRNAME" class="{maxlength:128,required:false,string:true}" type="text" value="<s:property value="czbm" />" name="CYRNAME" style="width:100px;">
											</td>
										</tr>
									</tbody>
								</table>
							</td>
							<td></td>
							<td valign="bottom" style="padding-bottom:5px;"><a id="search" class="easyui-linkbutton l-btn" href="javascript:search();" icon="icon-search">查询</a></td>
						</tr>
						<tr>
						</tr>
					</tbody>
				</table>
			</form>
			</div>
		</div>
	</div>
	<div region="center" border="false">
		<div id="mainFrameTab" style="border:0px" class="easyui-tabs" fit="true">
			<div title="正在执行的项目" border="false" style="border:0px" iconCls="icon-search" cache="false">
				<div class="itemList">
					<table width="100%">
						<tr class="header" style="border:1px solid #efefef">
							<td style="text-align:left;width:20px;"><input id="chkAll" type="checkbox" name="colname"></td>
							<td style="text-align:left;">项目负责人</td>
							<td style="text-align:left;">客户名称</td>
							<td style="text-align:left;">证券代码</td>
							<td style="text-align:left;">证券简称</td>
							<td style="text-align:left;">企业类型</td>
							<td style="text-align:left;">所属行业</td>
							<td style="text-align:left;">拟发行股数(万元)</td>
							<td style="text-align:left;">拟募集资金金额(万元)</td>
						</tr>
						<s:iterator value="runlist" status="ll">
							<tr class="content">
								<td style="width:05%;"><input type="checkbox" id="<s:property value='INSTANCEID'/>" name="colname" value="<s:property value='INSTANCEID'/>" /></td>
								<td><s:property value="MANAGER"/></td>
								<td><a href="javascript:editItem(<s:property value='INSTANCEID'/>)"><s:property value="CUSTOMERNAME"/></a></td>
								<td><s:property value="ZQDM"/></td>
								<td><s:property value="ZQJC"/></td>
								<td><s:property value="ZRFS"/></td>
								<td><s:property value="SSHY"/></td>
								<td><s:property value="GPFXSL"/></td>
								<td><s:property value="MJZJZE"/></td>
							</tr>
						</s:iterator>
					</table>
					<div style="padding:5px">
						<s:if test="runNum==0">
						</s:if>
						<s:else>
							<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
						</s:else>
					</div>
					<form action="zqb_gpfxproject_sx_index.action" method=post name=frmMain id=frmMain>
						<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
						<s:hidden name="pageSize" id="pageSize"></s:hidden>
						<s:hidden name="customername" id="customername"></s:hidden>
						<s:hidden name="sshy" id="sshy"></s:hidden>
						<s:hidden name="czbm" id="czbm"></s:hidden>
						<s:hidden name="cyrname" id="cyrname"></s:hidden>
					</form>
				</div>
			</div>
			<div title="已关闭项目" border="false" iconCls="icon-search" cache="false">
				<div class="itemList">
					<table width="100%">
						<tr class="header" style="border:1px solid #efefef">
							<td style="text-align:left;">项目负责人</td>
							<td style="text-align:left;">客户名称</td>
							<td style="text-align:left;">证券代码</td>
							<td style="text-align:left;">证券简称</td>
							<td style="text-align:left;">企业类型</td>
							<td style="text-align:left;">所属行业</td>
							<td style="text-align:left;">项目减少原因</td>
							<td style="text-align:left;">备注</td>
						</tr>
						<s:iterator value="closelist" status="ll">
							<tr class="content">
								<td><s:property value="MANAGER"/></td>
								<td><a href="javascript:setCloseReason(<s:property value="INSTANCEID"/>)"><s:property value="CUSTOMERNAME"/></a></td>
								<td><s:property value="ZQDM"/></td>
								<td><s:property value="ZQJC"/></td>
								<td><s:property value="ZRFS"/></td>
								<td><s:property value="SSHY"/></td>
								<td><s:property value="JSYY"/></td>
								<td><s:property value="MEMO"/></td>
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
					<form action="zqb_gpfxproject_sx_index.action" method=post name=frmMain1 id=frmMain1>
						<s:hidden name="pageNumber1" id="pageNumber1"></s:hidden>
						<s:hidden name="pageSize1" id="pageSize1"></s:hidden>
						<s:hidden name="customername" id="customername"></s:hidden>
						<s:hidden name="sshy" id="sshy"></s:hidden>
						<s:hidden name="czbm" id="czbm"></s:hidden>
						<s:hidden name="cyrname" id="cyrname"></s:hidden>
					</form>
				</div>
			</div>
		</div>
		<div style="display:none;">
			<s:hidden name="demid" id="demid"></s:hidden>
			<s:hidden name="formid" id="formid"></s:hidden>
		</div>
	</div>
<script type="text/javascript">
	$(function(){
		$("#CUSTOMERNAME").val($("#customername").val());
		$("#SSHY").val($("#sshy").val());
		$("#CZBM").val($("#czbm").val());
		$("#CYRNAME").val($("#cyrname").val());
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