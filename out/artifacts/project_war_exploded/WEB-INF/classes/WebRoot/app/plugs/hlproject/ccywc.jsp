<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
 <base target="_self">
  
    <title>档案归档</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css"/>
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.min.js"  charset="utf-8" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"  ></script>
    <link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
    <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
    var mainFormValidator;
    $().ready(function() {
		mainFormValidator =  $("#ifrmMaincc").validate({});
		mainFormValidator.resetForm();
	});
	 function addItem(){
		var thxmlc=$("#cclc").val();
		var pageUrl = "processRuntimeStartInstance.action?actDefId="+thxmlc;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		
		}
	function addItemT(){
		var thxmlc=$("#wclc").val();
		var pageUrl = "processRuntimeStartInstance.action?actDefId="+thxmlc;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		
		}
	
	/* function setKpOpenFormPage(instanceId,formId,demId){
		var url = 'openFormPage.action?formid='+formId+'&instanceId='+instanceId+'&demId='+demId;
		var target = "dem"+instanceId;
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url; 
	}
	 */
	/* function setKhOpenFormPage(instanceId,formId,demId){
		var url = 'openFormPage.action?formid='+formId+'&instanceId='+instanceId+'&demId='+demId;
		var target = "dem"+instanceId;
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url; 
	} */
	
	/* function setKpState(instanceid,demId,dataid){
		if(confirm("确认状态调整为已开票吗？调整后，状态将不能修改。")){
			var pageUrl = "zqb_sqkp_setState.action";
			$.post(pageUrl,{kpinstanceid:instanceid,kpdataId:dataid,kpid:demId},function(data){ 
		       			if(data=='success'){
		       				window.location.reload();
		       			}else{
		       				alert("状态更新失败。");
		       			} 
		     }); 
		}
	} */
	
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	$(function(){
		//分页
		$('#ppt').pagination({
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
		
		//查询
	    $("#search").click(function(){
	    	var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return;
			}
	    	$("#ifrmMaincc").submit();
	    });
	    
	    $(document).bind('keyup', function(event) {
			if (event.keyCode == "13") {
				//回车执行查询
				$("#ifrmMaincc").submit();
			}
		});
    });
	 function showInfo(lcbh,steptid,lcinstanceId,taskik){
		if(lcbh==""||steptid==""||lcinstanceId==""||taskik==""){
		
			alert("请在待办流程中查找所选内容！");
			
		} else{
			var pageUrl = "loadProcessFormPage.action?actDefId="+lcbh+"&actStepDefId="+steptid+"&instanceId="+lcinstanceId+"&excutionId="+lcinstanceId+"&taskId="+taskik;
			art.dialog.open(pageUrl,{
						id:'projectItem',
						cover:true, 
						title:'',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
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
	}
	 function ccywcbyuserid(username){
		 
		 var url = 'getccywcbyuserid.action?yhm='+encodeURI(username);
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = url;
		 
		 
		 
		 
	 }
	function removeda(instanceid){
		if(confirm("确定执行删除操作吗?")) {
			var pageUrl = "delCc.action";
			$.post(pageUrl,{ccid:instanceid},function(data){
						
		       			if(data=='success'){
		       				alert("删除成功。");
		       				window.location.reload();
		       			}else{
		       				
		       				alert("删除失败。");
		       			} 
		     }); 
		}
	}
	function showNotice(ggid){
	
		var pageUrl = "damx.action?ccid="+ggid;	
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
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

.cell td {
	margin: 0;
	padding: 3px 4px;
	height: 25px;
	font-size: 12px;
	white-space: nowrap;
	word-wrap: normal;
	overflow: hidden;
	text-align: left;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #eee;
}

.cell:hover {
	background-color: #F0F0F0;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:addItemT();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增外出流程</a>
			<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增出差流程</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMaincc' id='ifrmMaincc' method="post">
			<div style="padding:5px;text-align:center;">
				<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle" style="text-align: center;" width="25%" >出差/外出事由&nbsp;&nbsp;
											<input type='text' class = '{string:true,maxlength:64,required:false}'   name='ccsy' id='ccsy' value="<s:property value="ccsy" />">
										</td>
										<td class="searchtitle"  width="25%" style="text-align: center;">办事类型&nbsp;&nbsp;
											<select name='bslx' id='bslx' style="width:100px" >
												<option value=''>-空-</option>
												<option  <s:if test='bslx=="出差"'>selected</s:if> >出差</option>
												<option  <s:if test='bslx=="外出"'>selected</s:if> >外出</option>
											</select>
										</td> 
										<td class="searchtitle"  width="30%" style="text-align: center;">归档状态 &nbsp;&nbsp;
											<s:if test='zt=="起草"'>
											<select name='zt' id='zt' style="width:100px">
												<option value=''>-空-</option>
												<option value="起草" selected>起草</option>
												<option value="审批中" >审批中</option>
												<option value="驳回" >驳回</option>
												<option value="审批通过" >审批通过</option>
											</select>
											</s:if>
											<s:elseif test='zt=="审批中"'>
												<select name='zt' id='zt' style="width:100px" >
												<option value=''>-空-</option>
													<option value="起草" >起草</option>
												<option value="审批中" selected>审批中</option>
												<option value="驳回" >驳回</option>
												<option value="审批通过" >审批通过</option>
												</select>
											</s:elseif>
											<s:elseif test='zt=="驳回"'>
												<select name='zt' id='zt' style="width:100px" >
												<option value=''>-空-</option>
													<option value="起草" >起草</option>
												<option value="审批中" >审批中</option>
												<option value="驳回" selected>驳回</option>
												<option value="审批通过" >审批通过</option>
												</select>
											</s:elseif>
											<s:elseif test='zt=="审批通过"'>
												<select name='zt' id='zt' style="width:100px" >
												<option value=''>-空-</option>
													<option value="起草" >起草</option>
												<option value="审批中" >审批中</option>
												<option value="驳回" >驳回</option>
												<option value="审批通过" selected>审批通过</option>
												</select>
											</s:elseif>
											<s:else>
												<select name='zt' id='zt' style="width:100px">
													<option value=''>-空-</option>
													<option  >起草</option>
													<option >审批中</option>
													<option >驳回</option>
													<option >审批通过</option>
												</select>
											</s:else>
										</td>
									</tr>
								</table>
							</td>
							<td valign='bottom' style='padding-bottom:5px;'><a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a></td>
						</tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td align="center" style="width:4%;">序号
					<input type="hidden" id="cclc" name="cclc" value="<s:property value="cclc"/>"/>
					<input type="hidden" id="wclc" name="wclc" value="<s:property value="wclc"/>"/>
					</td>
					<td style="width:15%;">出差/外出人</td>
					<td style="width:15%;">出差/外出事由</td>
					<td style="width:15%;">开始时间</td>
					<td style="width:15%;">结束时间</td>
					<td style="width:15%;">出差目的地</td>
					<td style="width:5%;">办事类型</td>
					<td style="width:5%;">审批状态</td>
					<td  align="center" style="width:5%;">操作</td>
				</tr>
				<s:iterator value="list" status="status">
					<tr class="cell" style="text-align: center;">
						<td style="text-align: center;"><s:property value="rm"/>
						<!-- <input type="hidden" id="" name="" value=""> -->
						</td>
						<td style="text-align: center;"><a href="#" onclick="javascript:ccywcbyuserid('<s:property value="yhm" />')"><s:property value="yhm" /><a></td>
						<td style="text-align: center;">
							<a href="javascript:showInfo('<s:property value="lcbh"/>','<s:property value="lzjd"/>','<s:property value="instanceid"/>','<s:property value="rwid"/>')">
								<s:property value="ccsy"/>
							</a>
						</td>
						<td style="text-align: center;">
						<%-- <a href="javascript:void(0)"  onclick="showNotice('<s:property value='id'/>');" > --%>
						<s:property value="qssj" />
						</a>
						</td>
						<td style="text-align: center;"><s:property value="jssj" /></td>
						<td style="text-align: center;"><s:property value="ccmdd" /></td>
						<td style="text-align: center;"><s:property value="bslx" /></td>
						<td style="text-align: center;"><s:property value="zgzt" /></td>
						<td  aria-describedby="iform_grid__OPERATE" title="操作" style="text-align: center;" role="gridcell">
						<s:if test="zt!=null&&zt!='审批通过'&&zt!='审批中'&&(departmentname=='综合管理部'||curruserid=='YANGGUANG'||curruserid=='LVHONGZHEN'||showbutton==true)">
							<a href="javascript:void(0)"  onclick="removeda(<s:property value='id'/>)">删除 </a>
						</s:if>
						<s:else>
							暂无
						</s:else>
							
						</td>
					</tr>
				</s:iterator>
			</table>
			<div region="south" style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"	border="false">
				<div style="padding:5px">
					<s:if test="totalNum==0">
					</s:if>
					<s:else>
						<div id="ppt" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
					</s:else>
				</div>
			</div>
			<s:hidden name="kpformid" id="kpformid"></s:hidden>
			<s:hidden name="kpid" id="kpid"></s:hidden>
			<form action="ccywc.action" method="post" name="frmMain" id="frmMain">
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="ccsy" id="ccsy"></s:hidden>
				<s:hidden name="zt" id="zt"></s:hidden>
				<s:hidden name="bslx" id="bslx"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
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