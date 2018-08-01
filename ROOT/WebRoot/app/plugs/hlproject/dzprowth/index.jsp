<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>项目管理</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="admin/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"></script>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#ifrmMain").validate({});
		mainFormValidator.resetForm();
		$("input[name='chk_list']").attr("checked",false);
	});
	
	function submitRunPj(pageNumber,pageSize){
		$("#runPageNumber").val(pageNumber);
		$("#runPageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	
	function submitClosePj(pageNumber,pageSize){
		$("#closePageNumber").val(pageNumber);
		$("#closePageSize").val(pageSize);
		$("#frmMain2").submit();
		return ;
	}
	
	$(function(){
		 var ymid=${ymid};
		 $('#mainFrameTab').tabs('select',ymid);
		 
		//分页
		$('#pp').pagination({
		    total:<s:property value="runTotalNum"/>,  
		    pageNumber:<s:property value="runPageNumber"/>,
		    pageSize:<s:property value="runPageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitRunPj(pageNumber,pageSize);
		    }
		});
		
		//分页
		$('#bb').pagination({
		    total:<s:property value="closeTotalNum"/>,  
		    pageNumber:<s:property value="closePageNumber"/>,
		    pageSize:<s:property value="closePageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitClosePj(pageNumber,pageSize);
		    }
		});
		
		//查询
	    $("#search").click(function(){
	    	var valid = mainFormValidator.form(); //执行校验操作
	    	if(!valid){
	    		return false;
	    	}
	        var customername = $("#CUSTOMERNAME").val();
	        var clbm = $("#CLBM").val();
	        var czbm = $("#CZBM").val();
	        var cyrxm = $("#CYRXM").val();
	        var fxzt = $("#FXZT").val();
	        var seachUrl = encodeURI("hl_zqb_dzwthIndex.action?customername="+customername+"&clbm="+clbm+"&cyrxm="+cyrxm+"&fxzt="+fxzt);
	        window.location.href = seachUrl;
	    });
	});
	
	function addItem(formid,demId) {
		var url = 'createFormInstance.action?formid=' + formid + '&demId=' + demId;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	}

	function editItem(instanceid,formid,demId) {
		var url = 'openFormPage.action?instanceId=' + instanceid + '&formid=' + formid + '&demId=' + demId;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function closeItem() {
		try{
			$.messager.confirm('确认','确认要关闭?',function(result){
				if(result){
					var temp=new Array();
					$("input[name='chk_list']:checked").each(function(){
						if($(this).val()!=""){
							temp.push($(this).val());
						}
					});
					temp=temp.join(',');
					if(temp!=""){
						var pageUrl = "hl_zqb_dzwthmoreclose.action";
						$.post(pageUrl,{instanceIdStr:temp},function(data){
			       			if(data=='success'){
			       				window.location.reload();
			       			}else{
			       				alert(data+"定增(200人以内)项目关闭失败，请重试");
			       			}
			     		 });
					}else{
						alert("请勾选要关闭的项目！");
					}
				}
			});
		}catch(e){
			
		}
	}
	
	// 全选、全清功能
	function selectAll(){
		if($("input[name='chk_list']").attr("checked")){
			$("input[name='chk_list']").attr("checked",true);
		}else{
			$("input[name='chk_list']").attr("checked",false);
		}
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
			<a href="javascript:addItem(<s:property value="formid"/>,<s:property value="demId"/>);" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加项目</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<a href='javascript:closeItem();' class="easyui-linkbutton" plain="true" iconCls="icon-remove">关闭项目</a>
			<s:if test="cuid=='NEEQMANAGER'">
			<a href='javascript:clearItem();' class="easyui-linkbutton" plain="true" iconCls="icon-no">清空数据</a>
			</s:if>
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
												<input id="CUSTOMERNAME" class="{maxlength:128,required:false,string:true}" type="text" value="${customername}" name="JYF" style="width:100px">
											</td>
											 <td class="searchtitle" style="text-align:right;">项目承揽人</td>
											<td class="searchdata">
												<input id="CLBM" class="{maxlength:64,required:false,string:true}" type="text" value="${clbm}" name="CLBM" style="width:100px">
											</td>
											<!--<td class="searchtitle" style="text-align:right;">承揽部门</td>
											<td class="searchdata">
												<input id="CZBM" class="{maxlength:128,required:false,string:true}" type="text" name="CZBM" style="width:100px;" value="${czbm}">
											</td>-->
											<td class="searchtitle" style="text-align:right;">参与人姓名</td>
											<td class="searchdata">
												<input id="CYRXM" class="{maxlength:128,required:false,string:true}" type="text" name="CYRXM" style="width:100px;" value="${cyrxm}">
											</td>
											<td class="searchtitle" style="text-align:right;">项目阶段</td>
											<td class="searchdata">
												<input id="FXZT" class="{maxlength:128,required:false,string:true}" type="text" name="FXZT" style="width:100px;" value="${fxzt}">
											</td>
										</tr>
									</tbody>
								</table>
							</td>
							<td></td>
							<td valign="bottom" style="padding-bottom:5px;">
								<a id="search" class="easyui-linkbutton l-btn" icon="icon-search">查询</a>
							</td>
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
							<td style="text-align:left;width:20px;">
								<input id="chkAll" type="checkbox" name="chk_list" onclick="selectAll();" value="">
							</td>
							<td style="text-align: left;">客户名称</td>
							<td style="text-align: left;">项目负责人</td>
							<td style="text-align: left;">业务部门负责人</td>
							<td style="text-align: left;">项目承揽人</td>
							<td style="text-align: left;">项目阶段</td>
						</tr>
						<s:iterator value="runList" status="ll">
							<tr class="content">
								<td style="text-align:left;width:20px;">
									<input type="checkbox" name="chk_list" id="<s:property value="INSTANCEID"/>" value="<s:property value="INSTANCEID"/>">
								</td>
								<td style="cursor:pointer;" onclick="editItem(<s:property value="INSTANCEID"/>,<s:property value="formid"/>,<s:property value="demId"/>)" >
									<s:property value="CUSTOMERNAME" />
								</td>
								<td><s:property value="MANAGER" /></td>
								<td><s:property value="OWNER" /></td>
								<td><s:property value="CLBM" /></td>
								<td><s:property value="FXZT" /></td>
							</tr>
						</s:iterator>
					</table>
					<div style="padding:5px">
						<s:if test="runTotalNum==0">
						</s:if>
						<s:else>
							<div id="pp"
								style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
						</s:else>
					</div>
					<form action="hl_zqb_dzwthIndex.action" method="post" name="frmMain" id="frmMain">
						<s:hidden name="runPageNumber" id="runPageNumber"></s:hidden>
						<s:hidden name="runPageSize" id="runPageSize"></s:hidden>
						<s:hidden name="cryxm" id="cryxm"></s:hidden>
						<s:hidden name="czbm" id="czbm"></s:hidden>
						<s:hidden name="clbm" id="clbm"></s:hidden>
						<s:hidden name="customername" id="customername"></s:hidden>
						<s:hidden name="ymid" id="ymid" value="0"></s:hidden>
					</form>
				</div>
			</div>
			<div title="已关闭项目" border="false" iconCls="icon-search" cache="false">
				<div class="itemList">
					<table width="100%">
						<tr class="header" style="border:1px solid #efefef">
							<td style="text-align: left;">客户名称</td>
							<td style="text-align: left;">项目负责人</td>
							<td style="text-align: left;">项目承揽人</td>
							<td style="text-align: left;">承做部门</td>
						</tr>
						<s:iterator value="closeList" status="ll">
							<tr class="content">
								<td style="cursor:pointer;" onclick="editItem(<s:property value="INSTANCEID"/>,<s:property value="formid"/>,<s:property value="demId"/>)" >
									<s:property value="CUSTOMERNAME" />
								</td>
								<td><s:property value="MANAGER" /></td>
								<td><s:property value="OWNER" /></td>
								<td><s:property value="CLBM" /></td>
								<td><s:property value="FXZT" /></td>
							</tr>
						</s:iterator>
					</table>
					<div style="padding:5px">
						<s:if test="closeTotalNum==0">
						</s:if>
						<s:else>
							<div id="bb"
								style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
						</s:else>
					</div>
					<form action="hl_zqb_dzwthIndex.action" method="post" name="frmMain2" id="frmMain2">
						<s:hidden name="closePageNumber" id="closePageNumber"></s:hidden>
						<s:hidden name="closePageSize" id="closePageSize"></s:hidden>
						<s:hidden name="cryxm" id="cryxm"></s:hidden>
						<s:hidden name="czbm" id="czbm"></s:hidden>
						<s:hidden name="clbm" id="clbm"></s:hidden>
						<s:hidden name="customername" id="customername"></s:hidden>
						<s:hidden name="ymid" id="ymid" value="1"></s:hidden>
					</form>
				</div>
			</div>
		</div>
	</div>
	<s:hidden id="formid" name="formid"></s:hidden>
	<s:hidden id="demId" name="demId"></s:hidden>
	<s:hidden id="cuid" name="cuid"></s:hidden>
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
        function clearItem(){
       	
			$.messager.confirm('确认','确认要清空数据吗?',function(result){
				if(result){
						var cuid=$("#cuid").val();
						var pageUrl="delhldz.action";
						$.post(pageUrl,{cuid:cuid},function(data){
						
		       			if(data=='success'){
		       				alert("删除成功。");
		       				window.location.reload();
		       			}else{
		       				
		       				alert("删除失败。");
		       			} 
		     }); 
			}
		
       });
       
       } 
</script>