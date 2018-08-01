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
	$(function(){
	   $.post("zqb_announcement_roleid.action", function(data) {
        $("#roleid").val(data);
      });
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
        var noticeName = $("#NOTICENAME").val();
        var startdate = $("#STARTDATE").val();
        var enddate = $("#ENDDATE").val();
        var khbh=$("#khbh").val();
        var zqdm=$("#ZQDM").val();
        var zqjc=$("#ZQJC").val();
        var bzlx=$("#BZLX").val();
        var noticetype=$("#NOTICETYPE").val();
      	var pageUrl="isCustomer.action";
     	$.post(pageUrl,{zqjc:zqjc,zqdm:zqdm},function(data){
	         if(data=='success'){
	        	 var seachUrl = encodeURI("zqb_dustbin_index.action?khbh="+khbh+"&noticename="+noticeName+"&startdate="+startdate+"&enddate="+enddate+"&zqdm="+zqdm+"&zqjc="+zqjc+"&noticetype="+noticetype+"&bzlx="+bzlx);
	             window.location.href = seachUrl;
	         }else{
	         	 alert("公司名称有误或不是你所督导的客户");
	             window.location.href = "zqb_dustbin_index.action";
	         	 
	         }
	   });
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

		// 查看模板
		function readAnnouncement(id,instanceid){
			var zqdmxs =$("#zqdmxs").val();
			var zqjcxs =$("#zqjcxs").val();
			var pageUrl = "loadPage.action?instanceId="+instanceid+"&zqjcxs="+encodeURI(zqjcxs)+"&zqdmxs="+zqdmxs;
			art.dialog.open(pageUrl,{
				id:'Category_show',
				cover:true, 
				title:'查看公告',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,						
				close:function(){
					 location.reload();
				}
			});
		}
		
		// 删除公告
		function removeAnnouncement(id,instanceid){
			if(confirm("确定要彻底删除公告吗？")){
				var pageUrl="zqb_dustbin_delete.action";
				$.post(pageUrl,{id:id,instanceid:instanceid},function(data){
			         if(data=='success'){
			             window.location.reload();
			             //window.top.frames["deskframe"].location.reload();
			         }else{
			         	 alert("删除失败!");
			         }
			    });
			}
		}
		
		// 删除公告
		function restoreAnnouncement(instanceid){
			if(confirm("确定要还原此公告吗？")){
				var pageUrl="zqb_dustbin_restore.action";
				$.post(pageUrl,{instanceid:instanceid},function(data){
			         if(data=='success'){
			             window.location.reload();
			             //window.top.frames["deskframe"].location.reload();
			         }else{
			         	 alert("还原失败!");
			         }
			    });
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
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,						
				close:function(){
					 location.reload();
				}
			});
		}
		function readLsbb(noticetext){
			var pageUrl = "readLsbb.action?noticetext="+encodeURI(noticetext);
			art.dialog.open(pageUrl,{
				id:'Category_show',
				cover:true, 
				title:'历史版本',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,						
				close:function(){
					 location.reload();
				}
			});
		}
		// 下载模板
		function downloadTemplate(id,fjid){
			var url = 'uploadifyDownload.action?fileUUID='+fjid;
			window.location.href = url;
			
		}

		function readSmj(instanceid){
		   var pageUrl ="loadVisitPage.action?formid=148&demId=64&instanceId="+instanceid;
		   art.dialog.open(pageUrl,{
				id:'Category_show',
				cover:true, 
				title:'查看确认扫描件',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,						
				close:function(){
					 location.reload();
				}
			});
		}
		
		function impExcel(){
			//导入excel
				var pageUrl = "zqb_announcement_impPage.action?formid=133&demId=60";
				art.dialog.open(pageUrl,{
					id:'ExcelImpDialog',  
					cover:true,
					title:"数据导入",
					width:500,
					height:350,
					loadingText:'正在加载中,请稍后...', 
					bgcolor:'#999',
					rang:true, 
					lock: true,
					iconTitle:false,
					extendDrag:true, 
					autoSize:false,
					resize:false					
				});
				dg.ShowDialog();
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
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
					</td>
				</tr>
			</table>
		 </div>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
	<form action="zqb_dustbin_index.action" method="post" name="frmMain" id="frmMain" >
	<div style="padding:5px">
		<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
<tr> 
  <td style='padding-top:10px;padding-bottom:10px;'> 
<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
	<tr> 
		<s:if test="roleid==3"></s:if>
		<s:else>
			<td class="searchtitle">公司简称</td> 
			<td class= "searchdata"><input type='text' class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='ZQJC' id='ZQJC' value='' ></td> 
			<td class="searchtitle">公司代码 </td> 
			<td class= "searchdata"><input type='text' class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='ZQDM' id='ZQDM' value='' ></td> 
		</s:else>
		<!-- <td class="searchtitle">信披事项</td>
		<td class="searchdata"><input type='text' class='{maxlength:128,required:false,string:true}' style="width:100px" name='BZLX' id='BZLX' value=''></td> -->
	</tr>
	<tr>
		<td class="searchtitle">公告名称 </td> 
		<td class= "searchdata"><input type='text' class = '{maxlength:128,required:false,string:true}'  style="width:100px" name='NOTICENAME' id='NOTICENAME' value='' ></td>
		<td class="searchtitle">公告类型</td>
		<td class="searchdata">
			<select name='NOTICETYPE' id='NOTICETYPE'>
				<option value=''>-空-</option>
				<option value='临时报告'>临时报告</option>
				<option value='定期报告'>定期报告</option>
				<s:if test="roleid!=3">
				<option value='券商公告'>券商公告</option>
				<option value='风险提示性公告'>风险提示性公告</option>
				</s:if>
			</select>
		</td>
		<td class="searchtitle">公告时间 </td> 
		<td id="title_STARTDATE" class="searchdata">
			<input type='text' onfocus="var ENDDATE=$dp.$('ENDDATE');WdatePicker({onpicked:function(){ENDDATE.focus();},maxDate:'#F{$dp.$D(\'ENDDATE\')}'})"  style="width:100px" name='STARTDATE' id='STARTDATE'  value='' >
			到<input type='text' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'STARTDATE\')}'})" onchange="checkRQ()" style="width:100px" name='ENDDATE' id='ENDDATE'  value='' >
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
		 	<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
			<s:hidden name="totalNum" id="totalNum"></s:hidden>
			<s:hidden name="khbh" id="khbh"></s:hidden>
			<s:hidden name="roleid" id="roleid"></s:hidden>
			<s:hidden name="noticename" id="noticename"></s:hidden>
			<s:hidden name="startdate" id="startdate"></s:hidden>
			<s:hidden name="enddate" id="enddate"></s:hidden>
			<s:hidden name="zqjc" id="zqjc"></s:hidden>
			<s:hidden name="zqdm" id="zqdm"></s:hidden>
			<s:hidden name="bzlx" id="bzlx"></s:hidden>
			<s:hidden name="zqjcxs" id="zqjcxs"></s:hidden>
			<s:hidden name="zqdmxs" id="zqdmxs"></s:hidden>
			<s:hidden name="noticetype" id="noticetype"></s:hidden>
		</span>
	</form>
    
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<!-- <TD style="width:30px">信披事项</TD> -->
				<TD style="width:8%">公告类型</TD>
				<TD style="width:8%">公司简称</TD>
				<TD style="width:35%">公告名称</TD>
				<TD style="width:8%">提交人</TD>
				<TD style="width:8%">公告日期</TD>
				<TD style="width:8%">审批结果</TD>
				<!-- <TD style="width:60px">历史版本</TD> -->
				<TD style="width:25%">操作</TD>
			</TR>
			<s:iterator value="list"  status="status">
			<TR class="cell">
				<%-- <TD><s:property value="BZLX"/></TD> --%>
				<TD><s:property value="NOTICETYPE"/></TD>
				<TD><s:property value="ZQJCXS"/></TD>
				<TD><a href="javascript:readAnnouncement(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)"><s:property value="NOTICENAME"/></a></TD>
				<TD><s:property value="CREATENAME"/></TD>
				<TD><s:property value="NOTICEDATE"/></TD>
				<TD><a href="javascript:readLc('<s:property value="LCBH"/>',<s:property value="LCBS"/>,'<s:property value="YXID"/>','<s:property value="RWID"/>','<s:property value="PRCID"/>','<s:property value="STEPID"/>')"><s:property value="SPZT"/></a></TD>
			    <%-- <TD><a href="javascript:readLsbb('<s:property value="NOTICEFILE"/>')">历史版本</a></TD> --%>
			    
				<TD>
				<s:if test="roleid!=3||(roleid==3&&roleid==SCROLEID)||SCROLEID==null">
				<a href="javascript:restoreAnnouncement(<s:property value="INSTANCEID"/>)">恢复</a> | <a href="javascript:removeAnnouncement(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)">彻底删除</a>
				</s:if>
				 <s:if test="ISSMJINS">
				  | <a href="javascript:readSmj('<s:property value="SMJINS"/>')">查看归档的扫描件</a>
				 </s:if>
				</TD>
				 
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
		<s:hidden name="ggsplc" id="ggsplc"></s:hidden>
		<s:hidden name="dmggsplc" id="dmggsplc"></s:hidden>
	<script type="text/javascript">
	$(function(){
		$("#NOTICENAME").val($("#noticename").val());
		$("#STARTDATE").val($("#startdate").val());
		$("#ENDDATE").val($("#enddate").val());
		$("#ZQJC").val($("#zqjc").val());
		$("#BZLX").val($("#bzlx").val());
		$("#ZQDM").val($("#zqdm").val());
		$("#NOTICETYPE").attr("value",$("#noticetype").val()); 
	});
	</script>
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