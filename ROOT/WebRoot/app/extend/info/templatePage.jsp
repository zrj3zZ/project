<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>模板</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
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
			mainFormValidator =  $("#editForm").validate({});
			mainFormValidator.resetForm();
			$("#search").click(function(){
				var valid = mainFormValidator.form(); //执行校验操作
				if(!valid){
				return ;
				}
				var templatename = $("#templatename").val();
				var seachUrl = encodeURI("zqb_template_list.action?templatename="+ templatename);// + "&projectName="+projectName );
				window.location.href = seachUrl;
			});
		});
		// 全选、全清功能
		function selectAll(){
			if($("input[name='chk_list']").attr("checked")){
				$("input[name='chk_list']").attr("checked",true);
			}else{
				$("input[name='chk_list']").attr("checked",false);
			}
		}
		// 编辑模板
		function editTemplate(id,instanceid){
			var pageUrl = "openFormPage.action?formid=129&demId=52&instanceId="+instanceid;
			art.dialog.open(pageUrl,{ 
						id:'Category_show',
						cover:true, 
						title:'编辑模板',
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
		// 查看模板
		function readTemplate(id,instanceid){
			var pageUrl = "templateloadPage.action?instanceid="+instanceid;
			art.dialog.open(pageUrl,{ 
						id:'Category_show',
						cover:true, 
						title:'查看模板',
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
		// 下载模板
		function downloadTemplate(id,fjid){
			//alert(fjid);
			var url = 'uploadifyDownload.action?fileUUID='+fjid;
			window.location.href = url;
			
		}
		//批量下载模板
		function downloadall(){
			var fjid = new Array();
			var insid = new Array();
			var obj=document.getElementsByName("chk_list");//选择所有name="'chk_list'"的对象，返回数组
				//取到对象数组后，我们来循环检测它是不是被选中
				for(var i=0; i<obj.length; i++){ 
					if(obj[i].checked&&obj[i].value!=null&&obj[i].value!=''&&obj[i].id!=null&&obj[i].id!=''){
						fjid.push(obj[i].value);
						insid.push(obj[i].id);
					}
				}
			var fjids = fjid.join(",");
			var insids = insid.join(",");
			if(!fjids==""){
				//执行下载
				var url = "";
				if(fjids.length>2000){
					showDiv();
					downFile(insids,0);
					//url = "batch_uploadifyDownload.action?instanceids="+insids;	
				}else{
					showDiv();
					downFile(fjids,1);
				//	url = "batch_uploadifyDownload.action?fileUUID="+fjids;
				}
				//window.location.href = url;
			}else{
				alert("请选择需要下载的模板！");
			}
		}
		function downFile(id,flag){
			var seachUrl = "";
			if(flag==0){
				 seachUrl = encodeURI("batch_uploadifyDownload.action?instanceids="+id);
			
			}else{
				
				 seachUrl = encodeURI("batch_uploadifyDownload.action?fileUUID="+id);
			}
		//	var seachUrl = encodeURI("zqb_download_xpfj.action?ggid="+id);
		//	 window.location.href = seachUrl;
			$.ajax({
				type: "POST",
	            url: seachUrl,
	            success:function(date){
	            	if(date!="" && date!=null){
	            		window.location.href = encodeURI("zqb_download_ggmb.action?filename="+date);
	            	}
	            },
	            complete: function () {
	            	hideDiv();
	            }
	         });
			
		}
		// 删除模板
		function removeTemplate(id,instanceid){
			if(confirm("确定要删除模板吗？")){
				var pageUrl="zqb_template_delete.action";
				$.post(pageUrl,{id:id,instanceid:instanceid},function(data){
			         if(data=='success'){
			             window.location.reload();
			         }else{
			         	 alert("删除失败!");
			         }
			    });
			}
		}
		
		function addTemplate(){
				var pageUrl = "createFormInstance.action?formid=129&demId=52";
				art.dialog.open(pageUrl,{ 
						id:'Category_show',
						cover:true, 
						title:'新增模板',
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
		
		function batchTemplateType(){
				var pageUrl = "zqb_template_batch.action";
				var target = "_blank";
		   		var win_width = window.screen.width;
		   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		   		page.location = pageUrl;
				/* art.dialog.open(pageUrl,{ 
						id:'Category_show',
						cover:true, 
						title:'新增模板',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:550, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false
					}); */
		}
		function showDiv() {   
		    document.getElementById("bg").style.display ="block";  
		    document.getElementById("show").style.display ="block";  
		}  
		  
		function hideDiv(){  
		    document.getElementById("bg").style.display ="none";  
		    document.getElementById("show").style.display ="none";    
		}  

	</script>
</head> 
<body class="easyui-layout">
  <div class="container_div"><!-- 遮罩 -->  
        <div id="bg"></div>  
         
           
               <div id="show" style="display:none;position:absolute;z-index:9999;height:30px;width:200px;top:30%;left:50%;
	margin-left:-150px;text-align:center;border: solid 2px #86a5ad">
		<i class="fa fa-spinner fa-spin"></i>文件打包中，请稍候......
	</div>    
                
           
          
    </div> 
	<div region="north" style="height:50px;font-size:20px;font-family:黑体;padding:5px;border-bottom:1px solid #efefef;" border="false" >
		<div class="tools_nav">
			<table width="100%"  border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td> 
						<!-- <a id="addTemplateType" href="javascript:addTemplateType();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增模板分类</a> -->
						<label id="operationButton">
						<!-- <a id="editTemplateType" href="javascript:editTemplateType();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">编辑模板分类</a> -->
						<s:if test="flag==1">
							<!-- <a id="addTemplate" href="javascript:addTemplate();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增模板</a> -->
							<a id="batchTemplateType" href="javascript:batchTemplateType();" class="easyui-linkbutton" plain="true" iconCls="icon-add">批量插入</a>
						</s:if>
						<!-- <a id="delTemplateType" href="javascript:deleteTemplateType();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除模板分类</a> -->
						<a id="batchTemplateType" href="javascript:downloadall();" class="easyui-linkbutton" plain="true" iconCls="icon-add">批量下载</a>
						</label>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
    <!-- 按templatename查询 -->
    <div style="padding:5px">
			<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
				<form id="editForm" name="editForm">
					<table width="100%" cellspacing="0" cellpadding="0" border="0">
						<tbody>
							<tr>
								<td style="padding-top:10px;padding-bottom:10px;">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tbody>
											<tr>
												<td class="searchtitle" style="text-align:right;">模板名称</td>
												<td class="searchdata"><input id="templatename"
													class="{maxlength:128,required:false,string:true}" type="text"
													value="<s:property
											value="templatename" />"
													name="templatename" style="width:100px"></td>
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
				</form>
			</div>
		</div>
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<TD style="width:20px"><input type="checkbox" name="chk_list" onclick="selectAll()"></TD>
				<TD style="width:150px">模板名称</TD>
				<TD style="width:100px">是否为默认模板</TD>
				<TD style="width:60px">创建日期</TD>
				<TD style="width:60px">创建人</TD>
				<TD style="width:100px">操作</TD>
			</TR>
			<s:iterator value="list"  status="status">
			<TR class="cell">
				<TD><input type="checkbox" name="chk_list" id="<s:property value="INSTANCEID"/>" value="<s:property value="CONTENT"/>"></TD>
				<TD><a href="javascript:downloadTemplate(<s:property value="ID"/>,'<s:property value="CONTENT"/>')"><s:property value="TEMPLATENAME"/></a></TD>
				<TD><s:property value="IS_DEF"/></TD>
				<TD><s:property value="CREATEDATE"/></TD>
				<TD><s:property value="CREATEUSER"/></TD>
				<TD>
				<%-- 	<s:if test="flag==1">
						<a href="javascript:editTemplate(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)">编辑</a> |
					</s:if> --%>
						<a href="javascript:downloadTemplate(<s:property value="ID"/>,'<s:property value="CONTENT"/>')">下载</a>
					<s:if test="flag==1"> 
						|&nbsp;<a href="javascript:removeTemplate(<s:property value="ID"/>,<s:property value="INSTANCEID"/>)">删除</a>
					</s:if>
				</TD>
			</TR>
			</s:iterator>
		</table>
    </div>
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
	</div>
	<s:hidden name="companyno" id="companyno"></s:hidden>
	<s:hidden name="companyname" id="companyname"></s:hidden>
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