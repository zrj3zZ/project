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
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
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
	    	var zhuhy = $("#ZHUHY").val();
	    	var zihy = $("#ZIHY").val();
	        var seachUrl = encodeURI("zqb_industrymsg_manegement.action?zhuhy="+zhuhy+"&zihy="+zihy);
	        window.location.href = seachUrl;
	    });
	    //复选\全选
	    $("#chkAll").bind("click", function () {
        	$("[name =chk_list]:checkbox").attr("checked", this.checked);
        });
    });
	
	function removeIndustrymsg(instanceid){
		if(confirm("确定执行删除操作吗?")) {
			var pageUrl = "zqb_industrymsg_remove.action";
			$.post(pageUrl,{instanceid:instanceid},function(data){
				if(data=='success'){
		       		window.location.reload();
		       	}else{
		       		alert("删除失败。");
		       	} 
		     }); 
		}
	}
	
	function removeIndustrymsgs(){
		$.messager.confirm('确认','确认删除?',function(result){ 
			if(result){
				var list = $('[name=chk_list]').length;
				var a=0;
				for( var n = 0; n < list; n++){
					var flag = new Boolean();
					if($('[name=chk_list]')[n].checked==false&&$('[name=chk_list]')[n].id!='chkAll'){
						a++;
						if(a==list){
							alert("未勾选任何项!");
							return;
						}
					}
					if($('[name=chk_list]')[n].checked==true&&String($('[name=chk_list]')[n].id)!=String('chkAll')){
						var deleteUrl = "zqb_industrymsg_remove.action";
						$.post(deleteUrl,{instanceid:$('[name=chk_list]')[n].id},function(data){ 
							if(data=='success'){
								window.location.reload();
							}else{
								flag=false;
								alert(data);
							}
						});
					}
					if(!flag){break;}
				}
			}
		});
	}
	
	function editIndustrymsg(instanceid){
		var formid = $("#formid").val();
		var demId = $("#demId").val();
		var pageUrl = "openFormPage.action?formid="+formid+"&demId="+demId+"&instanceId="+instanceid;
		art.dialog.open(pageUrl,{ 
			title:'行业信息维护表单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:900,
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
	function addIndustrymsg(){
		var formid = $("#formid").val();
		var demId = $("#demId").val();
		var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demId;
		art.dialog.open(pageUrl,{  
			title:'行业信息维护表单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:900,
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
	
	//导入
	function impIndustrymsg(){
		//var pageUrl = "url:zqb_imp_know_index.action";
		var pageUrl = "zqb_imp_industrymsg_index.action";
		art.dialog.open(pageUrl,{  
				id:'Category_show',
				cover:true, 
				title:'导入行业信息',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:900,
				cache:false,
				lock: true,
				height:550, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false
			});
	}
	
	function windowReload(){
		window.location.href=window.location.href;
		window.location.reload;
	}
</script>
<style type="text/css">
pre {
  	overflow: auto;
	white-space: pre-wrap; /* css-3 */
	white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
	white-space: -pre-wrap; /* Opera 4-6 */
	white-space: -o-pre-wrap; /* Opera 7 */
	word-wrap: break-word; /* Internet Explorer 5.5+ */
}
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
	font-size: 12px;
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
			<a href="javascript:addIndustrymsg();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			<a href="javascript:impIndustrymsg();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">导入</a>
			<a href="javascript:removeIndustrymsgs();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
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
										<td class="searchtitle">主行业</td>
										<td class="searchdata">
											<input type='text' class='{maxlength:256,required:false,string:true}' style="width:40%;" name='ZHUHY' id='ZHUHY' value=''>
										</td>
										<td class="searchtitle">子行业</td>
										<td class="searchdata">
											<input type='text' class='{maxlength:256,required:false,string:true}' style="width:40%;" name='ZIHY' id='ZIHY' value=''>
										</td>
									</tr>
								</table>
							<td>
							<td valign='bottom' style='padding-bottom:5px;'><a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef;table-layout:fixed;">
				<tr class="header">
					<td style="width:2%;"><input type="checkbox" name="colname" id="chkAll" /></td>
					<td style="width:10%;">主行业</td>
					<td style="width:20%;">子行业</td>
					<td style="width:5%;">操作</td>
				</tr>
				<s:iterator value="list" status="status">
					<tr class="cell">
						<td>
							<input type="checkbox" class="chk_list" name="chk_list" value="<s:property value="INSTANCEID"/>" id="<s:property value="INSTANCEID"/>">
						</td>
						<td><s:property value="ZHUHY"/></td>
						<td><s:property value="ZIHY"/></td>
						<td>
							&nbsp;
							<a href="javascript:void(0)" style="color: #0000ff;" onclick="editIndustrymsg(<s:property value='INSTANCEID'/>)">编辑</a>
							&nbsp;|&nbsp;
							<a href="javascript:void(0)" style="color: #0000ff;" onclick="removeIndustrymsg(<s:property value='INSTANCEID'/>)">删除</a>
						</td>
					</tr>
				</s:iterator>
			</table>
			<form action="zqb_industrymsg_manegement.action" method="post" name="frmMain" id="frmMain">
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="zhuhy" id="zhuhy"></s:hidden>
				<s:hidden name="zihy" id="zihy"></s:hidden>
			</form>
			<s:hidden name="formid" id="formid"></s:hidden>
			<s:hidden name="demId" id="demId"></s:hidden>
		</div>
	</div>
	<div region="south" style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"	border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
</body>
</html>
<script type="text/javascript">
	$(function(){
		$("#ZHUHY").val($("#zhuhy").val());
		$("#ZIHY").val($("#zihy").val());
	});
	</script>
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
