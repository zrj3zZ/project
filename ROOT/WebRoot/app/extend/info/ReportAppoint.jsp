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
        	var sxid = $("#sxInfo").val();            
            if(sxid == null){            	
            	alert("没有可预约事项，如需预约请联系督导老师。");
            	return;
            }
       		var customerno=$("#customerno").val(); 
    		var CorpCode = $.trim($("#CorpCode").val());
            var startdate = $("#STARTDATE").val();
            var enddate = $("#ENDDATE").val();
            var cxdd = $("#CXDD").val();
            var seachUrl = encodeURI("AppointInfo.action?customerno="+customerno
    	        +"&corpCode="+CorpCode +"&startDate="+startdate+"&endDate="+enddate+"&eventID="+sxid+"&cxdd="+cxdd);
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
		if($("input[name='chk_All']").attr("checked")){
			$("input[name='chk_list']").attr("checked",true);
		}else{
			$("input[name='chk_list']").attr("checked",false);
		}
	}
	//新增预约
	function addAppoint(){
		var sxid = $("#eventID").val();		
		var customerno = $("#customerno").val();
		if(sxid == "")
		{
			if(customerno == "")
			{
				alert("没有可以预约的事项，请创建预约事项。");
			}
			else alert("没有可以预约的事项，请联系督导老师添加可预约事项。");
			return;
		}
		var CorpCode = $.trim($("#CorpCode").val());		
		if(CorpCode == "")
		{
			alert("请输入公司代码");	
			return;
		}
		$("#CorpCode").val(CorpCode);
		var pageUrl = "getCanAppoint.action?corpCode="+CorpCode+"&SXID="+sxid;
		art.dialog.open(pageUrl,{
			title:'预约信息维护',
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
		});
	}
	
	//删除预约日期
	function DeleteYY(){
		var list = $('[name=chk_list]').length;
		var a = 0;
		var ids = "";		
		for (var n = 0; n < list; n++) {
			if ($('[name=chk_list]')[n].checked == true) {
				a++;
				var delId = $('[name=chk_list]')[n].id;
				if(delId != 0){ids = delId + ",";}				
				}
			}
		if (a == 0) {
			$.messager.alert('提示信息','请选择要删除的预约信息!', 'info');
			return;				
		}
		if(ids == ""){alert("选择的信息中没有预约信息，不需要删除。");return;}
		$.post('DeleteAppoint.action',{projectNo:ids},//调用ajax方法
       function(data)
       {
	       if(data=="success"){
	         alert("删除成功");
	         window.location.reload();
	       }else{
	         alert("删除失败，请重试");
	       }			
       });
	}
	//检查开始日期截止 日期
	function checkRQ(){
		var start=$("#STARTDATE").val();
		var end = $("#ENDDATE").val();
		if(start != "" && end != "")
		{
	  		if(end<start){
		  		alert("结束日期不能早于开始日期，请重新输入！");
		  		$("#ENDDATE").val("");
		  		}
		  	return;		  
	  	}
  }	

	function selectNum(){
		
		var url="selectNum.action";
		art.dialog.open(url,{
	 		id:'kmDialog',
	    	cover:true,
			title:'预约设置',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			height:550, 
			cache:false,
			lock: true,
			iconTitle:false,
			extendDrag:true,
			autoSize:false
			
	});
	}
	
	//xlj start
	//更新预约日期
	function UpdateDate(){
		var ids = $("#hid").val();
		var yyDate = $("#editDATE").val();
		var khbh = $("#customerno").val();
		var cxdd = $("#cxdd").val();
		var CorpCode = $("#corpCode").val();
		var sxid = $("#eventID").val();		
		if(ids == "")
		{
			$.post('SaveAppoint.action',{customerno:khbh,SXID:sxid,startDate:yyDate,cxdd:cxdd,corpCode:CorpCode},//调用ajax方法
	       function(data)
	       {
		       if(data=="success"){
		         alert("保存成功");
		          window.location.reload();
		       }else{
		         alert("保存失败，请重试");
		       }			
	       });
		}
		else
		{
			$.post('UPDATEAppoint.action',{projectNo:ids,startDate:yyDate},//调用ajax方法
	       function(data)
	       {
		       if(data=="success"){
		         alert("保存成功");
		          window.location.reload();
		       }else{
		         alert("保存失败，请重试");
		       }			
	       });
       }
	}
	
	function EditDate(yyid,e,zqdm,cxdd,khbh)
	{
        var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
        var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
        var x = e.pageX || e.clientX + scrollX;
        var y = e.pageY || e.clientY + scrollY;        
		$("#hid").val(yyid);
		$("#customerno").val(khbh);
		$("#cxdd").val(cxdd);
		$("#corpCode").val(zqdm);
		var div = document.getElementById("divEditDate");//显示		
		div.style.display="block";		
        	
		div.style.top= y+"px";
		div.style.left = (x-150)+"px";
		div.style.height = "50px";
		div.style.width = "150px";
		div.style.position = "absolute";
	}
	function editClose(){document.getElementById("divEditDate").style.display="none";	}
	//xlj end
	
	function SetProhibitive(){
		url="appointmentAdd.action"
		art.dialog.open(url,{
            id:'dg_select',  
            title:'预约设置',
            resize : false,
            iconTitle:false, 
            content:url,
            width:1100,
            height:600, 
            max:false
		});
	}
	function edit(instanceid,yyid,khbh){
		var yyformid = $("#yyformid").val();
		var yydemid = $("#yydemid").val();
		var pageUrl = "openFormPage.action?formid="+yyformid+"&demId="+yydemid+"&instanceId="+instanceid;//+"&YYID="+yyid+"&KHBH="+khbh;;
		art.dialog.open(pageUrl,{ 
			title:'编辑附件',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:800,
			cache:false,
			lock: true,
			height:600, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false
		}); 
	}
	function addItem(yyid,khbh){
		if(yyid==null||yyid==""){
			alert("请先预约事项!");
			return;
		}
		var yyformid = $("#yyformid").val();
		var yydemid = $("#yydemid").val();
		var pageUrl = "createFormInstance.action?formid="+yyformid+"&demId="+yydemid+"&YYID="+yyid+"&KHBH="+khbh;
		art.dialog.open(pageUrl,{ 
			title:'上传附件',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:800,
			cache:false,
			lock: true,
			height:600, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});
		
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
						<a id="addAppoint" href="javascript:void();" onclick="addAppoint();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增预约</a>
						&nbsp;<!-- xlj -->
						<a id="DeleteYY" href="javascript:DeleteYY();" class="easyui-linkbutton" plain="true" iconCls="icon-add">删除预约</a>
						&nbsp;
						<s:if test="roleCid==3"></s:if><s:else>						
						<a id="SetProhibitive" href="javascript:SetProhibitive();" class="easyui-linkbutton" plain="true" iconCls="icon-add">设置休息日</a>
						&nbsp;
						<s:if test="roleCid!=3">
						<a id="SetNum" href="javascript:selectNum();" class="easyui-linkbutton" plain="true" iconCls="icon-add">创建可预约事项</a>
						</s:if>
						</label>
						</s:else>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>						
					</td>
				</tr>
			</table>
		 </div>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
	<form action="AppointInfo.action" method=post name=frmMain id=frmMain >
	<div style="padding:5px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
	<table border='0' cellpadding='0' cellspacing='0'> 
		<tr> 
		  <td class="searchtitle">公司代码： 
		   <input type='text' maxlength="6"  style="width:100px" name='CorpCode' id='CorpCode' <s:if test="roleCid==3">disabled="disabled"</s:if> value='<s:property value="corpCode" />' /> 		  
预约日期：
				<input type='text' onfocus="WdatePicker()" style="width:100px" name='STARTDATE' id='STARTDATE' value='<s:property value="startDate" />'/>
				<input type='text' onfocus="WdatePicker()" style="width:100px" name='ENDDATE' id='ENDDATE' value='<s:property value="endDate" />'/>				
			</td>	
		   <td class="searchtitle" align="left">督导老师：
		   <input type='text'  maxlength="30" style="width:100px" name='CXDD' id='CXDD' value='<s:property value="cxdd" />' <s:if test="roleCid==5">disabled="disabled"</s:if>/> 
		  	</td>
		  <td class="searchtitle">&nbsp;&nbsp;事项描述：
		  <select name="sxms" style="width:400px" id="sxInfo">
		   	<c:forEach var="sxmsConfig" items="${sxmsList}">		   	
               <option value="${sxmsConfig.ID}" <c:if test="${sxmsConfig.ID==eventID}">selected="true"</c:if>>${sxmsConfig.SXMS}</option>
            </c:forEach>
		  </select>
		  </td>
		  <td class="searchtitle" align="left">&nbsp;&nbsp;<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a></td>
		</tr> 
	</table>
	</div>					
	<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
	<s:hidden name="pageSize" id="pageSize"></s:hidden>
	<s:hidden name="totalNum" id="totalNum"></s:hidden>
	<s:hidden name="customerno" id="customerno"></s:hidden>	
	<s:hidden name="cxdd" id="cxdd"></s:hidden>
	<s:hidden name="corpCode" id="corpCode"></s:hidden>
	<s:hidden name="eventID" id="eventID"></s:hidden>
	<s:hidden name="yyformid" id="yyformid"></s:hidden>
	<s:hidden name="yydemid" id="yydemid"></s:hidden>
	<!-- xlj start -->    
    <input type="hidden" id="hid" name="hid"/>
    <div align="center" id="divEditDate" name="divEditDate" style="z-index:99999;display:none;">
    	<table width="100%">
			<tr>
				<td align="left"><a id="btnEp" class="easyui-linkbutton"
					icon="icon-save" plain="true" href="javascript:UpdateDate();">保存</a>
					<a href="javascript:editClose();" class="easyui-linkbutton"
					plain="true" iconCls="icon-cancel">关闭</a></td>
				<td style="text-align:right;padding-right:10px"></td>
			</tr>
			<tr>
				<td align="center" colspan="2">请选择预约日期：<input type='text' onfocus="WdatePicker()" class="{required:true}"  style="width:100px" name='editDATE' id='editDATE'/></td>
			</tr>
		</table>
    </div>    
    <!-- xlj end -->
	</form>    
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<TD style="width:20px"><input type="checkbox" name="chk_All" onclick="selectAll()"></TD>
				<TD style="width:150px">持续督导</TD>
				<TD style="width:150px" id="tdsx">事项名称</TD>
				<TD style="width:80px">公司代码</TD>
				<TD style="width:80px">公司简称</TD>				
				<TD style="width:150px">可预约日期</TD>
				<TD style="width:60px">预约日期</TD>		
				<TD style="display:none;" width="120px">变更预约日期</TD>
				<TD style="width:60px">附件</TD>
			</TR>
			<s:iterator value="runList"  status="status">
			<TR class="cell">
				<TD><input type="checkbox" name="chk_list" id="<s:property value="ID"/>"></TD>
				<TD><s:property value="KHFZR"/></TD>
				<TD><s:property value="SXMS"/></TD>
				<TD><s:property value="ZQDM"/></TD>
				<TD><s:property value="ZQJC"/></TD>
				<TD><s:property value="YYSJ"/></TD>
				<TD><s:property value="YYRQ"/></TD>		
				<TD style="display:none;"><a href="javascript:void(0);" onclick="javascript:EditDate('<s:property value="ID"/>',event,'<s:property value="ZQDM"/>','<s:property value="KHFZR"/>','<s:property value="KHBH"/>');">变更预约日期</a></TD>
				<s:if test="INSTANCEID != ''">
					<TD>
						<a href="javascript:void(0);" onclick="javascript:edit('<s:property value="INSTANCEID"/>','<s:property value="ID"/>','<s:property value="KHBH"/>');">
							<s:if test="FJ==0">
								上传附件
							</s:if>
							<s:else>
								查看附件
							</s:else>
						</a>
					</TD>
				</s:if>
				<s:else>
					<TD><a href="javascript:void(0);" onclick="javascript:addItem('<s:property value="ID"/>','<s:property value="KHBH"/>');">上传附件</a></TD>
				</s:else>
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