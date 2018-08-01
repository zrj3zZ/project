<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
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
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
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
					white-space:nowrap;
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
	<script type="text/javascript">
	var api = frameElement.api, W = api.opener;
	$(function() {
		$("#mainFrameTab").tabs({});
	});
		//新增预约
		function addAppoint(){
			var oDiv=document.getElementById("divAddAppoint");
			oDiv.style.display="block";			
		}
		//关闭预约添加页面
		function closeAppoint(){
			var oDiv=document.getElementById("divAddAppoint");
			oDiv.style.display="none";			
		}		
		// 保存预约
		function EditAppiont(setDate){
			var CorpCode =$("#corpCode").val() ;
			var sxid = $("#SXID").val();			
			var khbh = $("#customerno").val();
			var cxdd = $("#cxdd").val();
			if(!confirm("确认在 【"+setDate+"】  预约吗？"))
			{
				return;
			}			
			$.post('SaveAppoint.action',{customerno:khbh,SXID:sxid,startDate:setDate,cxdd:cxdd,corpCode:CorpCode},//调用ajax方法
		       function(data)
		       {
			       if(data=="success"){
			         alert("保存成功");
			       /*   window.close();
			         window.location.reload(); */
			         api.close();
			       }else{
			         alert(data);
			       }			
		       });			
		}
	</script>
</head> 
<body class="easyui-layout">
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
		<form action="getCanAppoint.action" method=post name=frmMain id=frmMain >
			<span style="disabled:none">
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="totalNum" id="totalNum"></s:hidden>
				<s:hidden name="customerno" id="customerno"></s:hidden>
				<s:hidden name="cxdd" id="cxdd"></s:hidden>			
				<s:hidden name="SXID" id="SXID"></s:hidden>
				<s:hidden name="corpCode" id="corpCode"></s:hidden>
			</span>		
		</form>
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">				
				<TD style="width:100px">日期</TD>
				<TD style="width:80px">星期</TD>
				<TD style="width:100px">可预约操作</TD>
			</TR>
			<s:iterator value="runList"  status="status">
			<TR class="cell">				
				<TD><s:property value="YYDATE"/></TD>
				<TD><s:property value="XQ"/></TD>
				<TD><a href="javascript:void(0);" onclick="javascript:EditAppiont('<s:property value="YYDATE"/>');"><s:property value="SYSL"/></a></TD>
			</TR>
			</s:iterator>
		</table>
    </div>
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