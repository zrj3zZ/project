<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>定期报告预约情况</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"> </script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"> </script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
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
       		var kflb = $("#CorpCode").val();            
            var seachUrl = encodeURI("czkflb.action?KFLB="+kflb);
            window.location.href = seachUrl;    
            $.post(seachUrl,{},function(data){});
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
		if($("input[name='chk_All']").attr("checked")){
			$("input[name='chk_list']").attr("checked",true);
		}else{
			$("input[name='chk_list']").attr("checked",false);
		}
	}
	//新增扣分类别
	function addAppoint(){
		var pageUrl = "xinzengkflb.action?";
		/*art.dialog.open(pageUrl,{
			title:'新增扣分类别',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:800,
			cache:false,
			lock: true,
			height:500, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});*/
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+600+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	
	//删除扣分类别
	function DeleteYY(){
		var list = $('[name=chk_list]').length;
		var a = 0;
		var ids = "";		
		for (var n = 0; n < list; n++) {
			if ($('[name=chk_list]')[n].checked == true) {
				a++;
				var delId = $('[name=chk_list]')[n].id;
				if(delId != 0){ids += delId + ",";}				
				}
			}
		if (a == 0) {
			$.messager.alert('提示信息','请选择要删除的扣分类别!', 'info');
			return;				
		}
		$.post('deletekflb.action',{kflbid:ids},//调用ajax方法
       function(data)
       {
	         alert("删除成功");
	         $("input[name='chk_list']").attr("checked",false);
	         window.location.reload();		
       });
	}
	//修改扣分类别
	function updatekflb(updatekflb){
		var pageUrl = "cxupdatekflb.action?kflbid="+updatekflb;
		/*art.dialog.open(pageUrl,{
			title:'修改扣分类别',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:800,
			cache:false,
			lock: true,
			height:500, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});*/
		
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+600+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	
</script>
</head> 
<body class="easyui-layout">
<div region="north" style="height:50px;font-size:20px;font-family:黑体;padding:5px;border-bottom:1px solid #efefef;" border="false" >
  <div class="tools_nav">
			<table width="100%"  border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>						
						<label id="operationButton">
						<a id="addAppoint" href="javascript:void();" onclick="addAppoint();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增类别</a>
						&nbsp;<!-- xlj -->
						<a id="DeleteYY" href="javascript:DeleteYY();" class="easyui-linkbutton" plain="true" iconCls="icon-add">删除类别</a>
					
						&nbsp;
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>						
					</td>
				</tr>
			</table>
		 </div>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
	<form action="czkflb.action" method=post name=frmMain id=frmMain >
	<div style="padding:5px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
	<table border='0' cellpadding='0' cellspacing='0'> 
		<tr> 
		  <td class="searchtitle">扣分类别： 
		   <input type='text' class = '{maxlength:6,required:false}'  style="width:100px" name='CorpCode' id='CorpCode' value="<s:property value="KFLB"/>"/>			
			</td>	
		  	<%-- <td class="searchtitle" align="left" <s:if test="roleCid==3">style="display:none;"</s:if>>&nbsp;&nbsp;督导老师：
		  	<select name="ddls" width="150px" id="ddls">
			  	<c:forEach var="ddlsConfig" items="${cxddList}">
	               <option value="${ddlsConfig.USERID}" <c:if test="${ddlsConfig.USERID==cxdd}">selected="true"</c:if>>${ddlsConfig.UNAME}</option>
	            </c:forEach>
		  	</select></td> --%>
		  <td class="searchtitle" align="left">&nbsp;&nbsp;<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a></td>
		</tr> 
	</table>
	</div>					
	<s:hidden name="ID" id="ID"></s:hidden>
	</form>    
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<TD style="width:20px"><input type="checkbox" name="chk_All" onclick="selectAll()"></TD>
				<TD style="width:150px">扣分类别</TD>
				<TD style="width:150px">分数</TD>
				<TD style="width:100px">创建人账号</TD>				
				<TD style="width:150px">创建时间</TD>
			</TR>
			<s:iterator value="runList"  status="status">
			<TR class="cell">
				<TD><input type="checkbox" name="chk_list" id="<s:property value="ID"/>"></TD>
				<TD><a href="javascript:updatekflb(<s:property value="ID"/>);"><s:property value="KFLB"/></a></TD>
				<TD><s:property value="FS"/></TD>
				<TD><s:property value="CJZH"/></TD>
				<TD><s:property value="CHSJ"/></TD>
			</TR>
			</s:iterator>
		</table>
    </div>
    <div region="south"
		style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
		border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
	<s:hidden name="userID" id="userID"></s:hidden>
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