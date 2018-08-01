<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>WBS</title>
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
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
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
	
function submitMsg(pageNumber,pageSize){
	$("#pageNumber").val(pageNumber);
	$("#pageSize").val(pageSize);
	$("#frmMain").submit();
	return ;
}
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
        var sxmc=$("#sxmc").val();
      
      	var seachUrl = encodeURI("cjywlczy.action?sxmc="+sxmc);
           window.location.href = seachUrl;
      });
  	});



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
/* .cell:hover{
	background-color:#F0F0F0;
} */
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
					
						</label>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
					</td>
				</tr>
			</table>
		 </div>
	</div>
    <div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
	<form action="cjywlczy.action" method="post" name="frmMain" id="frmMain" >
	<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
	 <div style="padding:5px">
			<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
					<table width="100%" cellspacing="0" cellpadding="0" border="0">
						<tbody>
							<tr>
								<td style="padding-top:10px;padding-bottom:10px;">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tbody>
											<tr>
												<td class="searchtitle" style="text-align:right;">事项名称</td>
												<td class="searchdata"><input id="sxmc"
													class="{maxlength:128,required:false,string:true}" type="text"
													value="<s:property
											value="sxmc" />"
													name="sxmc" style="width:100px"></td>
											</tr>
										</tbody>
									</table>
								</td>
								<td></td>
								<td valign="bottom" style="padding-bottom:5px;"><a
									id="search" class="easyui-linkbutton l-btn"
									href="javascript:void(0);" icon="icon-search">查询</a></td>
							</tr>
							<tr>
							</tr>
						</tbody>
					</table>
			</div>
		</div>
		<span style="disabled:none">
			<s:hidden name="noticedatestart" id="noticedatestart"></s:hidden>
			<s:hidden name="noticedateend" id="noticedateend"></s:hidden>
		</span>
	</form>
    
    	<table width="100%" style="border:1px solid #efefef;">
			<TR  class="header">
				<!-- <TD style="width:2px"><input type="checkbox" name="chk_list" onclick="selectAll()"></TD> -->
				<TD style="width:15%;">事项名称</TD>
				<TD style="width:20%;">适应规则</TD>
				<TD style="width:20%;">披露要求</TD>
				<TD style="width:25%;">办事步骤</TD>
				<TD style="width:20%;">备查文件</TD>
			</TR>
			<s:iterator value="list"  status="status">
				<TR  class="cell" >
					<TD><s:property value="sxmc" escapeHtml="false"/></TD>
					<TD><s:property value="sygz" escapeHtml="false"/></TD>
					<TD><s:property value="plyq" escapeHtml="false"/></TD>
					<TD><s:property value="bsbz" escapeHtml="false"/>  </TD>
					<TD><s:property value="bcwj" escapeHtml="false"/></TD>
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