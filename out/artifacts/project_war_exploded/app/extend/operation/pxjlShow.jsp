<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>公告呈报</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/> 
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/common.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script> 
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
	mainFormValidator =  $("#frmMain").validate({
	 });
	 mainFormValidator.resetForm();
	});
	$(function() {
		$("#mainFrameTab").tabs({});
	});
	$(function(){
	 
        $('#pp').pagination({  
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
      //查询事件
       $("#search").click(function(){
   	   var valid = mainFormValidator.form(); //执行校验操作
   	   if(!valid){
   	   return false;
   	   }
   	var pxry = $("#pxry").val();
        var zqdmxs = $("#ZQDM").val();
        var zqjcxs = $("#ZQJC").val();
        var startdate = $("#startdate").val();
        var enddate=$("#enddate").val();
        var xcjcry=$("#xcjcry").val();
        var xcjclx=$("#xcjclx").val();
       
      	var pageUrl="pxjl_search.action";
      	 var seachUrl = encodeURI("pxjl_search.action?zqdmxs="+zqdmxs+"&zqjcxs="+zqjcxs+"&startdate="+startdate+"&enddate="+enddate+"&xcjcry="+xcjcry+"&xcjclx="+xcjclx+"&pxry="+pxry);
         window.location.href = seachUrl;
   
      });
  	});
		function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
		// 全选、全清功能
		function selectAll(){
			if($("input[name='chk_list']").attr("checked")){
				$("input[name='chk_list']").attr("checked",true);
			}else{
				$("input[name='chk_list']").attr("checked",false);
			}
		}
		

	
	
	
		// 新增公告
		function addAnnouncement(){
			
			var zqdmxs =$("#ZQDM").val();
			var zqjcxs =$("#ZQJC").val();
			
			
			if(zqdmxs==null || zqjcxs==''){
			    $.messager.alert('提示信息','请选择挂牌公司!','info');  
				return;
			}
		
			var formid = $("#formid").val();
			var demid = $("#demid").val();
			var pageUrl = encodeURI("createFormInstance.action?formid="+formid+"&demId="+demid+"&BANKACCOUNT="+zqdmxs+"&CUSTOMERNAME="+zqjcxs);
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;			
		}
		
		function uploadItem(){
			var pageNumber=$("#pageNumber").val();
			var pageSize=$("#pageSize").val();
			  var zqdmxs = $("#ZQDM").val();
		        var zqjcxs = $("#ZQJC").val();
		        var startdate = $("#startdate").val();
		        var enddate=$("#enddate").val();
		        var xcjcry=$("#xcjcry").val();
		        var xcjclx=$("#xcjclx").val();
		        var pxry = $("#pxry").val();
			 var seachUrl =encodeURI("pxjllToExcl.action?zqdmxs="+zqdmxs+"&zqjcxs="+zqjcxs+"&startdate="+startdate+"&enddate="+enddate+"&xcjcry="+xcjcry+"&xcjclx="+xcjclx+ "&pageNumber=" + pageNumber+ "&pageSize=" + pageSize+ "&pxry=" + pxry);
			window.location.href = seachUrl;
			
		 }
	
		
		function remove() {
			/* var obj = document.getElementsByName("chk_list");
			check_val = [];
			for (k in obj) {
				if (obj[k].checked)
					check_val.push(obj[k].value);
			}
			if (check_val.length == 0 || check_val[0] == '0') {
				alert("请选择您要删除的行项目!");
			} else {
				var deleteUrl = "xcjclv_del.action";
				$.post(deleteUrl, {
					mlist : check_val
				}, function(data) {
					if (data == 'success') {
						window.location.reload();
					} else {
						alert(data);
					}
				});
			} */
			$.messager
			.confirm(
					'确认',
					'确认删除?',
					function(result) {
						 var obj = document.getElementsByName("chk_list");
						check_val = [];
						for(var i=0;i<obj.length;i++){
							if (obj[i].checked)
								check_val.push(obj[i].value);
						}
						
						if (check_val.length == 0 ) {
							$.messager.alert('提示信息',
									'请选择您要删除的行项目!', 'info');
							return;
						}
						if (result) {
							var list = $('[name=chk_list]').length;
							var a = 0;

							for (var n = 0; n < list; n++) {
								if ($('[name=chk_list]')[n].checked == false
										&& $('[name=chk_list]')[n].id != 'chkAll') {
									a++;
									
								}
								if ($('[name=chk_list]')[n].checked == true
										&& String($('[name=chk_list]')[n].id) != String('chkAll')) {
									var deleteUrl = "xcjclv_del.action";
									$
											.post(
													deleteUrl,
													{
														instanceid : $('[name=chk_list]')[n].id
													},
													function(data) {
														if (data == 'success') {
															window.location
																	.reload();
														} else {
															alert(data);
														}
													});
								}
							}
						}
					});
		}
		function updXcjc(tid){
			//+"&BANKACCOUNT="+zqdmxs+"&CUSTOMERNAME="+zqjcxs
			var formid = $("#formid").val();
			var demid = $("#demid").val();
			var pageUrl = "openFormPage.action?formid="+formid+"&instanceId="+tid+"&demId="+demid;
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;
				
		}
	</script>
	<style type="text/css">
		.memoTitle{
			font-size:14px;
			padding:5px;
			color:#666;
		}
		.memoTitle a{
			font-size:12px;
			padding:5px;
		}
		.TD_TITLE{
			padding:5px;
			width:200px;
			background-color:#efefef;
			text-align:right;
			
		}
		.TD_DATA{
			padding:5px;
			padding-left:15px;
			padding-right:30px;
			background-color:#fff;
			width:500px;
			text-align:left;
			border-bottom:1px solid #efefef;
		}
		 .header td{
			height:30px;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
			border-top:1px dotted #ccc;
			border-right:1px solid #eee;
		} 
		.cell:hover{
			background-color:#F0F0F0;
		}
		.cell td{
					margin:0;
					padding:3px 4px;
					word-wrap:normal;
					overflow:hidden;
					text-align:left;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
				}
		.selectCheck{
			border:0px;
			text-align:right;
		}
	</style>
</head> 
<body class="easyui-layout">
<div region="north" style="height:50px;font-size:20px;font-family:黑体;padding:5px;border-bottom:1px solid #efefef;" border="false" >
  <div class="tools_nav">
	<table width="100%"  border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td> 
				<label id="operationButton">
					<a id="addAnnouncement" href="javascript:addAnnouncement();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增培训记录</a>
					<a id="deljclv" href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-add">删除</a>
					<a href="javascript:uploadItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">导出</a>
				</label>
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				<%-- <a iconcls="icon-excel-imp" plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:impExcel();"><span class="l-btn-left"><span class="l-btn-text icon-excel-imp" style="padding-left: 20px;">导入</span></span></a> --%>
			</td>
		</tr>
	</table>
		 </div>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
	<form action="pxjl_search.action" method="post" name="frmMain" id="frmMain" >
	<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
	<div style="padding:5px">
		<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
<tr> 
  <td style='padding-top:10px;padding-bottom:10px;'> 
<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
	<tr> 
		<td class="searchtitle" style="padding-left:10px;">证券简称</td> 
		<td class= "searchdata"><input type='text'  class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='ZQJC' id='ZQJC' value='<s:property value="zqjcxs"/>' ></td> 
		<td class="searchtitle">证券代码 </td> 
		<td class= "searchdata"><input type='text'  class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='ZQDM' id='ZQDM' value='<s:property value="zqdmxs"/>' ></td> 
		<td class="searchtitle">培训时间 </td> 
		<td id="title_STARTDATE" class="searchdata">
			<input type='text' onfocus="WdatePicker()"  style="width:100px" name='startdate' id='startdate'  value='<s:property value="startdate"/>' >
			&nbsp;&nbsp;到&nbsp;&nbsp;<input type='text' onfocus="WdatePicker()" onchange="checkRQ()" style="width:80px" name='enddate' id='enddate'  value='<s:property value="enddate"/>' >
		</td>
	</tr>
	<tr>
		<td class="searchtitle" style="padding-left:10px;">参训人员 </td> 
		<td class= "searchdata"><input type='text' class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='xcjcry' id='xcjcry' value='<s:property value="xcjcry"/>' ></td>
		<td class="searchtitle" >培训人员 </td> 
		<td class= "searchdata"><input type='text' class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='pxry' id='pxry' value='<s:property value="pxry"/>' ></td>
		<td class="searchtitle">培训类型</td>
		<td class="searchdata">
			<select name='xcjclx' id='xcjclx'>
				<option value=''>-空-</option>
				<option value='现场培训' <s:if test="xcjclx=='现场培训'">selected</s:if> >现场培训</option>
				<option value='视频培训' <s:if test="xcjclx=='视频培训'">selected</s:if> >视频培训</option>
				<option value='电话培训' <s:if test="xcjclx=='电话培训'">selected</s:if> >电话培训</option>
				<option value='其他方式' <s:if test="xcjclx=='其他方式'">selected</s:if> >其他方式</option>
				
			</select>
		</td>
		<td class="searchResult"></td>
		<td class="searchdata">
			
		</td>
	</tr>
</table>
<td> 
<td valign='bottom' style='padding-bottom:5px;'> <a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);" >查询</a></td>
<tr> 
</table> 
</div>

	</div>
		<span style="disabled:none">
		 	<s:hidden name="formid" id="formid"></s:hidden>
			<s:hidden name="demid" id="demid"></s:hidden>
		
		
			<s:hidden name="sj" id="sj"></s:hidden>
		
			<s:hidden name="ry" id="ry"></s:hidden>
			<s:hidden name="nsr" id="nsr"></s:hidden>
		<s:hidden name="zqdmxs" id="zqdmxs"></s:hidden>
			<s:hidden name="zqjcxs" id="zqjcxs"></s:hidden>
		</span>
	</form>
    
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header" style="text-align: center;">
				<TD style="width:4%"><input type="checkbox"  name="chk_list" id="chkAll" onclick="selectAll()" value="0"></TD>
				<TD style="width:12%">证券代码</TD>
				<TD style="width:12%">证券简称</TD>
				<TD style="width:12%">培训时间</TD>
				<TD style="width:20%">培训人员</TD>
				<TD style="width:20%">参训人员</TD>
				<TD style="width:20%">培训类型</TD>
			</TR>
			<s:iterator value="list"  status="status">
			<TR class="cell" >
				<TD style="text-align: center;"><input type="checkbox" name="chk_list" value="<s:property value="insId"/>" id="<s:property value="insId"/>"></TD>
				
				<TD style="text-align: center;"><s:property value="zqdm"/></TD>
				<TD style="text-align: center;"><a href="javascript:updXcjc('<s:property value="insId"/>')"><s:property value="zqjc"/></a></TD>
				<TD style="text-align: center;"><s:property value="cjsj"/></TD>
				<TD style="text-align: center;"><s:property value="pxry"/></TD>
				<TD style="text-align: center;"><s:property value="cxry"/></TD>
				<TD style="text-align: center;"><s:property value="pxlx"/></TD>
				</TD>
				 <%-- <a href="javascript:readAnnouncement(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)"><s:property value="NOTICENAME"/></a> --%>
			</TR>
			</s:iterator>
		</table>
					
		
    </div>
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
	<div style = "padding:5px">
			<s:if test="totalNum==0">
			
			</s:if><s:else>
			<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
			</div>
	</div>

</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
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