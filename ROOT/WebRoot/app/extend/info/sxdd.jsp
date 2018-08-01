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
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/> 
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
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
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#frmMain").validate({});
		mainFormValidator.resetForm();
	});
	$(function(){
		var s=getBeforeDate(5);
		$("#STARTDATE").val(s);
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
			var startdate = $("#STARTDATE").val();
			var enddate = $("#ENDDATE").val();
			var sxmc=$("#SXMC").val();
			var seachUrl = encodeURI("searchZyddsx.action?sxmc="+sxmc+"&startdate="+startdate+"&enddate="+enddate);
			window.location.href = seachUrl;
		});
	});
	function getBeforeDate(n){
		var n = n;
		var d = new Date();
		var year = d.getFullYear();
		var mon=d.getMonth()+1;
		var day=d.getDate();
		if(day <= n){
			if(mon>1) {
			   mon=mon-1;
			}else {
				year = year-1;
				mon = 12;
			}
		}
		d.setDate(d.getDate()-n);
		year = d.getFullYear();
		mon=d.getMonth()+1;
		day=d.getDate();
		var s = year+"-"+(mon<10?("0"+mon):mon)+"-"+(day<10?("0"+day):day);
	    return s;
	}
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
	function fkzlDdsj(id,instanceid,excutionId,taskId){
		var pageUrl ;
		pageUrl = encodeURI("loadProcessFormPage.action?actDefId=ZYDDSXLC:1:81304&instanceId="+ instanceid +"&excutionId="+excutionId+"&taskId="+taskId);
		art.dialog.open(pageUrl,{ 
			id:'Category_show',
			cover:true, 
			title:'反馈资料',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1000,
			cache:false,
			lock: true,
			height:480, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false
		});
	}
	// 编辑模板
	function editDdsj(id,instanceid){
		var pageUrl ;
		pageUrl = "openFormPage.action?formid=142&demId=62&instanceId="+instanceid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		return;
	}
	// 查看模板
	function readDdsj(id,instanceid,lcbh,yxid,rwid){
	 if(rwid!=""&&rwid!=null){
		var pageUrl = "loadProcessFormPage.action?actDefId="+lcbh+"&excutionId="+yxid+"&instanceId="+instanceid+"&taskId="+rwid;
			art.dialog.open(pageUrl,{ 
						id:'Category_show',
						cover:true, 
						title:'查看重要督导事项',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:600, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false
					});
			}else{
			alert("当前事项并未下发董秘，请到审批页面进行后续操作！");
		}
	}
	function readLc(lcbh,lcbs,yxid,rwid,prcid,stepid){
			var pageUrl = "processInstanceMornitor.action?actDefId="+lcbh
			+"&actStepDefId="+stepid+"&prcDefId="+prcid+"&taskId="+rwid+"&instanceId="+lcbs+"&excutionId="+yxid;
			art.dialog.open(pageUrl,{ 
				id:'Category_show',
				cover:true, 
				title:'流程跟踪',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:480, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false
			});
		
	}
	// 删除公告
	function removeAnnouncement(id,instanceid){
		if(confirm("确定要删除模板吗？")){
			var pageUrl="zqb_announcement_delete.action";
			$.post(pageUrl,{id:id,instanceid:instanceid},function(data){
		         if(data=='success'){
		             window.location.reload();
		         }else{
		         	 alert("删除失败!");
		         }
		    });
		}
	}
	// 新增重要督导事项f
	function addDdsx(){
		var pageUrl = "processRuntimeStartInstance.action?actDefId=ZYDDSXLC:1:81304";
		/*art.dialog.open(pageUrl,{ 
			id:'Category_show',
			cover:true, 
			title:'重要督导事项表单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1000,
			cache:false,
			lock: true,
			height:700, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});*/
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	function xftz(){
		$.messager.confirm('确认','确认下发?',function(result){ 
			if(result){
				$('input[name=chk_list]').each(//遍历每个checkbox对象
				function(){
					var obj_check = $(this).attr('checked');//检查是否被选中
					if(obj_check){
						var id= $(this).val();//获取复选框的值
						$.post('sendddMail.action',{instanceid:id},//调用ajax方法
						function(data)
						{
							if(data=="success"){
							 alert("下发成功！");
							}else{
							  alert("下发失败！");
							}
						});
					}
				});
			}
		});
	}
	//删除记录
	function deleteDdsj(instanceid){
	    $.messager.confirm('确认','确认删除?',function(result){ 
	           if(result){
		  			$.post('deleteDdsj.action',{instanceid:instanceid},//调用ajax方法
			       function(data)
			       {
				       if(data=="success"){
				         alert("删除成功");
				           window.location.reload();
				       }else{
				         alert("删除失败");
				       }
				        
		
			       });
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
						<a id="addDdsx" href="javascript:addDdsx();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增信息</a>
						</label>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
						<label id="operationButton">
					</td>
				</tr>
			</table>
		 </div>
	</div>
<div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
<form action="searchZyddsx.action" method=post name='frmMain' id='frmMain' >
<div style="padding:5px">
<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
<tr>
<td style='padding-top:10px;padding-bottom:10px;'> 
<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
<tr> 
<td style="text-align: right;">事项名称 </td> 
<td>
<input type='text' class = '{maxlength:128,required:false,string:true}'  name='SXMC' id='SXMC'  value='' > 
</td> 
<td id="title_STARTDATE" style="text-align: right;"><span style="color:red;"></span>最迟反馈时间</td>
<td id="data_STARTDATE" >
<input type='text' onfocus="WdatePicker()" class = "{required:true}"  style="width:100px" name='STARTDATE' id='STARTDATE'  value="" >
至 <input type='text' onfocus="WdatePicker()" class = "{required:true}" onchange="checkRQ()" style="width:100px" name='ENDDATE' id='ENDDATE'
value='<%= new SimpleDateFormat("yyyy-MM-dd").format(new Date())%>' ></td>
</tr> 
</table> 
<td> 
<td valign='bottom' style='padding-bottom:5px;'> <a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);" >查询</a></td>
<tr> 
</table> 
</div>
	</div>
	<span style="disabled:none">
	<input type="hidden" name="formid" value="88" id="formid"/> 
	<input type="hidden" name="demId" value="21" id="demId"/>
	<input type = "hidden" name="idlist" id="idlist" value='11'>
	<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
	<s:hidden name="pageSize" id="pageSize"></s:hidden>
	<s:hidden name="totalNum" id="totalNum"></s:hidden>
	</span>
	</form>
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<TD style="width:20px"><input type="checkbox" name="chk_list" onclick="selectAll()"></TD>
				<TD style="width:150px">事项名称</TD>
				<TD style="width:100px">最迟反馈时间</TD>
				<TD style="width:60px">反馈状态</TD>
				<TD style="width:60px">审批状态</TD>
				 <TD style="width:100px">操作</TD> 
			</TR>
			<s:iterator value="list"  status="status">
			<TR class="cell">
				<TD><input type="checkbox" name="chk_list" value="<s:property value="INSTANCEID"/>"></TD>
				<TD><a href="javascript:readDdsj('<s:property value="ID"/>','<s:property value="INSTANCEID"/>','<s:property value="LCBH"/>','<s:property value="YXID"/>','<s:property value="RWID"/>')"><s:property value="SXMC"/></a></TD>
				<TD><s:property value="ZCFKSJ"/></TD>
				<TD><s:property value="FKZT"/></TD>
				<TD><a href="javascript:readLc('<s:property value="LCBH"/>',
				<s:property value="LCBS"/>,'<s:property value="YXID"/>',
				'<s:property value="RWID"/>','<s:property value="PRCID"/>','<s:property value="STEPID"/>')"><s:property value="SPZT"/></a></TD>
				<TD>
					<a href="javascript:deleteDdsj(<s:property value="INSTANCEID"/>)">删除</a> 
				</TD>
				
			</TR>
			</s:iterator>
		</table>
    </div>
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
		<div style = "padding:5px">
			<s:if test="totalNum==0">
			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
	<s:hidden name="khbh" id="khbh"></s:hidden>
	<s:hidden name="khmc" id="khmc"></s:hidden>
	<s:hidden name="roleid" id="roleid"></s:hidden>
	<s:hidden name="sxmc" id="sxmc"></s:hidden>
<script type="text/javascript">
$(function(){
	$("#SXMC").val($("#sxmc").val());
});
</script>
</body>
</html>
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