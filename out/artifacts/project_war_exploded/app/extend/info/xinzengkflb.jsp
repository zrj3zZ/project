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
	$(function() {
		$("#mainFrameTab").tabs({});
	});			
		// 保存扣分类别
		function EditAppiont(){
			var kflb =$("#KFLB").val();
			if(kflb==""){
				alert("请填写扣分类别！");
				return ;
			}
			var CJZH =$("#CJZH").val() ;	
			var fs =$("#FS").val() ;	
			kflb = encodeURI(kflb);
			var pageurl = "Savekflb.action?KFLB="+kflb+"&CJZH="+CJZH+"&FS="+fs;	
			$.post(pageurl,{},function(data){
			 if(data=="success"){
			         alert("保存成功");
			         window.close();
			         window.location.reload();
			         }else{
			         alert("新增出错，请联系管理员！");
			         }
		       });			
		}
		function editClose(){
		window.close();
		}
	</script>
</head> 
<body class="easyui-layout">
    <div id="border">
	<table border="0" cellpadding="0" cellspacing="0" style="margin-bottom:5px;" width="100%" class="ke-zeroborder">
		<tbody>
		<div  border="false" >
		<div class="tools_nav">
				<a  href="#" style="margin-left:1px;margin-right:1px"  onclick='EditAppiont()' text='Ctrl+s' class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
					<a href="javascript:editClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
		</div>
		</div>
		</tbody>

						<tbody>
							<tr id="itemTr_0">
								<td class="td_title" id="title_kflb" width="15%">
									扣分类别
								</td>
								<td class="td_data" id="data_kflb" width="35%">
									<input type='text' class = '{maxlength:128,required:true,string:true}'  style="width:300px;" name='KFLB' id='KFLB'  value='' form-type='al_textbox'><font style="color:red;padding-left:5px">*</font>&nbsp;　
								</td>
							</tr>
							<tr id="itemTr_1">
								<s:hidden name="ID" id="ID"></s:hidden>
								<td class="td_title" id="title_kflb" width="15%">
									对应分数
								</td>
								<td class="td_data" id="data_kflb" width="35%">
									<input type='text' class = '{maxlength:128,required:true,string:true}'  style="width:300px;" name='FS' id='FS'  value='' form-type='al_textbox'><font style="color:red;padding-left:5px">*</font>　&nbsp;　
								</td>
							</tr>
							<tr id="itemTr_18">
								<td class="td_title" id="title_CJZH" width="15%">
									创建账号
								</td>
								<td class="td_data" id="data_CJZH" width="35%">
									<input type='text' class = '{maxlength:128,required:true,string:true}'  readonly="true" style="width:300px"  name='CJZH' id='CJZH'  value='<s:property value="userID" />' >&nbsp;
								</td>
								
							</tr>
							<tr id="itemTr_2">
								<td class="td_title" id="title_kflb" width="15%" cosplan='3'>
									<font style="color:red;padding-left:5px">注：总分10分！</font>
								</td>
							</tr>
					</tbody>
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