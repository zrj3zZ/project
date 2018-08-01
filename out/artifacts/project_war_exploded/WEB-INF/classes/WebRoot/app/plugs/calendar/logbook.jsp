<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">	
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>	
<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
<script type="text/javascript" src="iwork_js/message/sysmsgpage.js"></script>
<script type="text/javascript" src="iwork_js/message/sysmsglist.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=blue"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
<script type="text/javascript">  
var api,W;
try{
	api=  art.dialog.open.api;
	W = api.opener;	
}catch(e){}
var mainFormValidator;
$().ready(function() {
	mainFormValidator =  $("#editForm").validate({});
	mainFormValidator.resetForm();
});
$(document).ready(function(){
	$('#pp').pagination({  
		total:<s:property value="totalNum"/>,  
		pageNumber:<s:property value="pageNumber"/>,
		pageSize:<s:property value="pageSize"/>,
		onSelectPage:function(pageNumber, pageSize){
		submitMsg(pageNumber,pageSize);
		}
	});
	$("#search").click(function(){
		var valid = mainFormValidator.form(); //执行校验操作
		if(!valid){
			return ;
		}
		var bm=$("#BM").val();
		var name = $("#XM").val(); 
		
		var startdate = $("#STARTDATE").val(); 
		var enddate = $("#ENDDATE").val(); 
		$("#search_btn").toggleClass("search_btn_onclick");
		/* if(name==''&&startdate==''&&enddate==''){
			var url = "logbook.action";
			$('#editForm').attr('action',url);
			$('#editForm').submit(); 
		}else{
			var url = "logbook.action?username="+name+"&startDate="+startdate+"&endDate="+enddate;
			$('#editForm').attr('action',url);
			$('#editForm').submit(); 
		} */
		var seachUrl = encodeURI("logbook.action?username="+name+"&depname="+bm);
		window.location.href = seachUrl;
	});
	
		

	
	
	
	
})
 $(function(){
	var orgroleid= $("#orgroleid").val();
	if(orgroleid!=5){
		$("#qxxs").hide();
		$("#qxsd").hide();
	}
	$.ajax({
		type:'POST',
		url:"gzrzxzq.action",
		dataType:"json",
		success:function(data){
			
			var $sel = $("#XM");
			
			
			 var XM=$("#xmname").val(); 
			
			 $sel.append("<option value=''>---请选择---</option>");
			 if(data!=""){
				 
			
		 	 for(var i = 0;i<data.length;i++){  
		   
			   
			    if(XM==data[i].username){
				    $sel.append("<option value='"+data[i].username+"' selected>"+data[i].username+"</option>");
			    }else{
			    	
			    $sel.append("<option value='"+data[i].username+"'>"+data[i].username+"</option>");
			    }

			
				}
			 }
		}
	});
	 $.ajax({
		type:"POST",
		url:"gzrzxzqBM.action",
		dataType:"json",
		success:function(data){
			var $sel = $("#BM");  
			
			  var BM=$("#bmname").val(); 
			  
			  $sel.append("<option value=''>------请选择-------</option>");
			  if(data!=""){
				  
			 
		 	 for(var i = 0;i<data.length;i++){  
		 		 if(BM==data[i].departmentname){
		 			$sel.append("<option value='"+data[i].departmentname+"'   selected>"+data[i].departmentname+"</option>");
				    }else{
				    	
				    	$sel.append("<option value='"+data[i].departmentname+"'>"+data[i].departmentname+"</option>");
				    }
			   
			   
			
			
				}
			  }
		}
	}); 
	
	
});
function submitMsg(pageNumber,pageSize){
	$("#pageNumber").val(pageNumber);
	$("#pageSize").val(pageSize);
	$("#frmMain").submit();
	return ;
}

function expExcel(){
	//导入excel
	var bm=$("#BM").val();
	var name = $("#XM").val(); 
	var startdate = $("#STARTDATE").val(); 
	var enddate = $("#ENDDATE").val(); 
	
	var pageUrl =  encodeURI("doExcelExp.action?username="+name+"&depname="+bm);
	$('#frm').attr('action',pageUrl);
	$('#frm').submit();
	return; 
}
var status = 1; 

//查询回车事件
function enterKey(){
	if (window.event.keyCode==13){
		doSearch();
		return ;
	}
} 
$("#XM").focus(function(){
	$.post('getUserName.action',null,
		function(data){//执行成功之后的回调函数                        
			$('#info').html(data);                  
		}
	);
});
  function edityj(id,text,userid,num){
  	if(num=='0'){
  		alert("暂无工作日志！");
  	}else{
  		var pageUrl =encodeURI("edityijian.action?id="+id+"&text="+text+"&orgroleid="+userid);
  		var target = "_blank";
  		var iTop = (window.screen.availHeight - 30 - 100) / 2; 
  	    var iLeft = (window.screen.availWidth - 10 - 100) / 2; 
  		var page = window.open('form/loader_frame.html',target,'width='+600+',height=300,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
  		page.location = pageUrl;
  	}
  }      
function checkRQ(){
	if($("#ENDDATE").val()<$("#STARTDATE").val()){
		alert("项目结束时间不能早于开始时间，请重新输入！");
		$("#ENDDATE").val("");
		return;
	}
}
function checkLog(id,isallday,userid,title,js,starttime,je,endtime,isalert,alerttime,issharing,remark,re_startdate,re_enddate,re_starttime,re_endtime,re_mode,re_day_interval,re_week_date,re_month_days,re_year_month,re_year_days){
	var obj = new Object();
	obj.editForm_iworkSchCalendar_id = id;
	obj.editForm_iworkSchCalendar_isallday = isallday;
	obj.editForm_iworkSchCalendar_userid = userid;
	obj.editForm_iworkSchCalendar_title = title;
	obj.editForm_iworkSchCalendar_startdate = js;
	obj.editForm_iworkSchCalendar_starttime = starttime;
	obj.editForm_iworkSchCalendar_enddate = je;
	obj.editForm_iworkSchCalendar_endtime = endtime;
	obj.editForm_iworkSchCalendar_isalert = isalert;
	obj.editForm_iworkSchCalendar_alerttime = alerttime;
	obj.editForm_iworkSchCalendar_issharing = issharing;
	obj.editForm_iworkSchCalendar_remark = remark;
	obj.editForm_iworkSchCalendar_reStartdate = re_startdate;
	obj.editForm_iworkSchCalendar_reEnddate = re_enddate;
	obj.editForm_iworkSchCalendar_reStarttime = re_starttime;
	obj.editForm_iworkSchCalendar_reEndtime = re_endtime;
	obj.editForm_iworkSchCalendar_reMode = re_mode;
	obj.editForm_iworkSchCalendar_reDayInterval = re_day_interval;
	obj.editForm_iworkSchCalendar_reWeekDate = re_week_date;
	obj.editForm_iworkSchCalendar_reMonthDays = re_month_days;
	obj.editForm_iworkSchCalendar_reYearMonth = re_year_month;
	obj.editForm_iworkSchCalendar_reYearDays = re_year_days;
	var dg = $.dialog({
		id:'dg_eventClick',
		title:'编辑日程',
		resize : false,
		iconTitle:false,
		content:"url:editSchCalendar.action?id="+id,
		max:false,
		data:obj,
		close:function(){
			window.location.reload();
		}
	});
}
function Dictionary(){
	var pageUrl = "gzrzxzq.action";
	art.dialog.open(pageUrl,{
		title:'数据选择',
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999',
		rang:true,
		width:800,
		cache:false,
		lock: true,
		stack:true,
		zIndex:10,
		top:100,
		height:550,
		iconTitle:false,
		drag:false,
		autoSize:true,
		close:function(){
			//window.location.reload();
		}
	});
}
function DictionaryBM(){
	var pageUrl = "gzrzxzqBM.action";
	art.dialog.open(pageUrl,{
		title:'数据选择',
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999',
		rang:true,
		width:800,
		cache:false,
		lock: true,
		stack:true,
		zIndex:10,
		top:100,
		height:550,
		iconTitle:false,
		drag:false,
		autoSize:true,
		close:function(){
			//window.location.reload();
		}
	});
}
function openOpinion(userid,num){
	if(num=='0'){
		alert("暂无工作日志！");
	}else{
		var url = 'logbookXx.action?userid='+encodeURI(userid);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
}
	</script>
<style type="text/css">
.searchtitle {
	text-align: right;
	padding: 5px;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: 28px;
	font-size: 12px;
	vertical-align: text-middle;
	padding-top: 2px;
}

.header td {
	height: 35px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
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

.cell:hover {
	background-color: #F0F0F0;
}

</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav"></div>
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top;width:100%;">
		<form id="editForm" name="editForm" method="post">
			<div style="padding:5px;border:1px solid #ccc;margin-bottom:5px;margin-left:5px;margin-right:5px;background:#FFFFEE;">
				<table width="100%" border="0" cellpadding="0" cellspacing="0"> 
					<tbody>
						<tr> 
							<td style="padding-top:10px;padding-bottom:10px;"> 
								<table width="100%" border="0" cellpadding="0" cellspacing="0"> 
									<tbody>
										<tr id="qxsd"> 
											<td class="searchtitle" style="text-align:right">姓名  </td> 
											<td class="searchdata"> 
												<select id="XM" name='XM'></select>
												<%-- <input type="text" class="{string:true}" maxlength="128" style="width:100px" name="XM" id="XM" value="<s:property value="username"/>" >
												<a id="selfdefinedic" class="easyui-linkbutton l-btn l-btn-plain" iconcls="icon-dictionary" plain="true" style="margin-left:5px;" href="javascript:Dictionary();"></a> --%>
											</td> 
											
											<td class="searchtitle" style="text-align:right">部门  </td> 
											<td class="searchdata"> 
											<select id="BM"></select>
												<%-- <input type="text" class="{string:true}" maxlength="128" style="width:100px" name="BM" id="BM" value="<s:property value="depname"/>" >
												<a id="selfdefinedic" class="easyui-linkbutton l-btn l-btn-plain" iconcls="icon-dictionary" plain="true" style="margin-left:5px;" href="javascript:DictionaryBM();"></a> --%>
											</td>
										</tr> 
									</tbody>
								</table>
							</td>
							<td valign="bottom" style="padding-bottom:5px;"  id="qxxs"> 
								<a id="search" class="easyui-linkbutton l-btn" icon="icon-search" href="javascript:void(0);"><span ><span><span>查询</span></span></span></a>
							</td>
							<td valign="bottom" style="padding-bottom:5px;padding-left:10px;">
								<a href="javascript:expExcel();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a>
							</td>
						</tr>
					<tr> 
					</tr>
					</tbody>
				</table> 
			</div>
		</form>
		<div style="padding:1%;text-align: center;width:98%;">
			<table id='iform_grid' style="border:1px solid #efefef;width:100%;">
				<tr  class="header">
					<td align="center" width="8%">时间</td>
					<td align="center" width="6%">姓名</td>
					<td align="center" width="9%">部门</td>
					<td align="center" width="27%">工作内容</td>
					<td align="center" width="3%">完成状态</td>
					<td align="center" width="20%">完成情况</td>
					
					<td align="center" width="20%">备注</td>
					<td align="center" width="5%">意见</td>
					
					
					
				</tr>
				<s:iterator value="list" status="ll" >
				<tr  class="cell">  
						<td><s:property value="startdate"/></td>
						<td />
					<a href="javascript:void(0)" style="color: #0000ff;" onclick="openOpinion('<s:property value="userid"/>','<s:property value="num"/>')">	<s:property value="username"/>( <s:property value="num"/>)</a>
						</td>
						<td><s:property value="depname"/></td>
						<td style="text-align: left;" <s:if test="isclick==1"> onclick='checkLog("<s:property value="id"/>","<s:property value="isallday"/>","<s:property value="userid"/>","<s:property value="title"/>","<s:property value="js"/>","<s:property value="starttime"/>","<s:property value="je"/>","<s:property value="endtime"/>","<s:property value="isalert"/>","<s:property value="alerttime"/>","<s:property value="issharing"/>","<s:property value="remark"/>","<s:property value="re_startdate"/>","<s:property value="re_enddate"/>","<s:property value="re_starttime"/>","<s:property value="re_endtime"/>","<s:property value="re_mode"/>","<s:property value="re_day_interval"/>","<s:property value="re_week_date"/>","<s:property value="re_month_days"/>","<s:property value="re_year_month"/>","<s:property value="re_year_days"/>");' style="cursor:pointer;color:blue;"</s:if>><s:property value="title"/></td>
						<td style="text-align: center;"><s:property value="wczt"/></td>
						<td style="text-align: left;"><s:property value="wcqk"/></td>
						<td style="text-align: left;"><s:property value="remark"/></td>
						
						<%-- <s:if test="orgroleid==5">
						<td style="text-align: left;"><a href="#" onclick='edityj("<s:property value="id"/>","<s:property value="extends1"/>","<s:property value="orgroleid"/>")'  >提意见</a></td>
						</s:if><s:elseif test="extends1==null">
						<td style="text-align: left;"></td>
						</s:elseif><s:else>
						<td style="text-align: left;"><a href="#" onclick='edityj("<s:property value="id"/>","<s:property value="extends1"/>","<s:property value="orgroleid"/>")'  >查看意见</a></td>
						</s:else> --%>
			
			
						<td style="text-align: center;">
						<s:if test="orgroleid==5">
						<a href="#" onclick='edityj("<s:property value="id"/>","<s:property value="extends1"/>","<s:property value="orgroleid"/>","<s:property value="num"/>")'  >提意见</a>
						</s:if><s:elseif test="extends1==null">
						</s:elseif><s:else>
						<a href="#" onclick='edityj("<s:property value="id"/>","<s:property value="extends1"/>","<s:property value="orgroleid"/>","<s:property value="num"/>")'  >查看意见</a>
						</s:else>
						</td>
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
			<form action="logbook.action" method=post name=frmMain id=frmMain >
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="username" id="username"></s:hidden>
				<s:hidden name="startDate" id="startDate"></s:hidden>
				<s:hidden name="endDate" id="endDate"></s:hidden>
				<s:hidden name="xmname" id="xmname"></s:hidden>
				<s:hidden name="orgroleid" id="orgroleid"></s:hidden>
				<s:hidden name="bmname" id="bmname"></s:hidden>
			</form>
			<form action="doExcelExp.action" method=post name=frm id=frm >
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<%-- <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false">
		<div style = "padding:5px">
			<s:if test="totalNum==0">
				暂无工作日志
			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div> --%>
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
						alert("str:"+tmp+"s:"+str);
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>