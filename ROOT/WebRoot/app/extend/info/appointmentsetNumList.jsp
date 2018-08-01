<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预约事项</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css" />
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js""></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js""></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/engine/iformpage.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"> </script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"> </script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var api,W;
	try{
		api=  art.dialog.open.api;
		W = api.opener;	
	}catch(e){} 
			$(function(){
			        $('#ppt').pagination({  
					    total:<s:property value="totalNum"/>,  
					    pageNumber:<s:property value="pageNumber"/>,
					    pageSize:<s:property value="pageSize"/>,
					    onSelectPage:function(pageNumber, pageSize){
					    	submitMsg(pageNumber,pageSize);
					    }
					});
			});
			function submitMsg(pageNumber,pageSize){
				$("#pageNumber").val(pageNumber);
				$("#pageSize").val(pageSize);
				$("#ifrmMain").submit();
				return ;
			}
			function SetNum(){
				var url="appointmentSetNum.action"
				var obj = new Object();
				obj.win = window;
		        
				art.dialog.open(url,{
					id:"yysxDialog",
					title: '预约设置',
					pading: 0,
					lock: true,
					width:600,
		            height:500, 
					top:'25%',
					left:'50%',
					data:obj,
					close : function() {
						setfocus();
				} 
				});
			}
			
			function editYYS(sxid,szsid){
				var url="updateappointmentSetNum.action?sxid="+sxid+"&yysx="+szsid;
				var obj = new Object();
				obj.win = window;
		        
				art.dialog.open(url,{
					id:"yysxDialog",
					title: '预约设置',
					pading: 0,
					lock: true,
					width:600,
		            height:300, 
					top:'25%',
					left:'50%',
					data:obj,
					close : function() {
						setfocus();
				} 
				});
			}
			function deleteYYSX(sxid){
				 $.messager.confirm('确认','确认删除?',function(result){ 
				 	if(result){
							var deleteUrl = "deleteYYSX.action";
							$.post(deleteUrl,{sxid:sxid},function(data){ 
			       			if(data=='success'){
			       				alert("删除成功！");
			       				/* setTimeout('setfocus()', 1000); */
			       				window.location.reload();
			       			}else{
			       				alert(data);
			       			}
			     		 });
		       }
			});
			}
			function setfocus(){
				/* api.zindex().focus(); */
				window.location.reload();
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
</head>
<body class="easyui-layout">
<div region="north" style="height:50px;font-size:20px;font-family:黑体;padding:5px;border-bottom:1px solid #efefef;" border="false" >
  <div class="tools_nav">
			<table width="100%"  border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td> 						
						<label id="operationButton">
						<a id="btnEp" class="easyui-linkbutton" icon="icon-add" plain="true" href="#" onclick="SetNum();">新增可预约事项</a>						
						</label>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload" id="reloadButton">刷新</a>
					</td>
					<td style="font-size:12px;color:red;" align="left">如预约事项列表中已有相应的的预约事项，只需设置每日最大预约数量，挂牌企业即可看到并参与预约</td>
				</tr>				
			</table>
		 </div>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
	<form action="selectNum.action" method="post" name="ifrmMain" id="ifrmMain" >
			<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
			<s:hidden name="totalNum" id="totalNum"></s:hidden>
		</form>	
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<TD style="width:150px">事项描述</TD>
				<TD style="width:100px">开始日期</TD>
				<TD style="width:60px">截止日期</TD>
				<TD style="width:60px">每日最大预约数</TD>
				<TD style="width:200px" align="center">选择已有预约事项</TD>
			</TR>
			<s:iterator value="runList"  status="status">
			<TR class="cell">
				<TD><s:property value="SXMS"/></TD>
				<TD><s:property value="KSRQ"/></TD>
				<TD><s:property value="JZRQ"/></TD>
			    <TD><s:property value="SZS"/></TD>
				<TD style="text-align:center;"><a href="javascript:editYYS(<s:property value="SXID"/>,<s:if test="SZSID==null">0</s:if><s:else><s:property value="SZSID"/></s:else>)">设置我的每日最大预约数</a>
				&nbsp;
				<a href="javascript:deleteYYSX(<s:property value="SXID"/>,<s:if test="SZSID==null">0</s:if><s:else><s:property value="SZSID"/></s:else>)">删除</a>
				</TD>
			</TR>
			</s:iterator>
		</table>
    </div>
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
			<s:if test="totalNum==0">
			
			</s:if><s:else> 
			<div id="ppt"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			 </s:else> 
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