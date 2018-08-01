<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/plugs/dictionary_runtime.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 
<script type="text/javascript">
var  api = art.dialog.open.api; 
  //全局变量
  	var mainFormValidator;
  	var rowNum;
	$().ready(function() {
		document.onkeydown = function(e){ 
		    var ev = document.all ? window.event : e;
		    if(ev.keyCode==13) {
		    	doSubmit();
		     }
		}
		var lastsel;
			<s:if test="isAutoShow==1">
				var pageUrl = 'sys_dictionary_runtime_showDataJson.action?dictionaryId=<s:property value="dictionaryId"/>';
			</s:if>
			 <s:else>
			 	var pageUrl = ''; 
			 </s:else>
				jQuery("#dictionary_list_grid").jqGrid({
					url:pageUrl,
					colNames:<s:property value="colNames"  escapeHtml="false"/>, 
					colModel:<s:property value="colModel"  escapeHtml="false"/>,
					rowNum:15,
					rowList:[15,30,50],
					loadui:'block',
					rownumbers:true,
					multiselect: true, 
					sortname: 'ID',
					viewrecords: true,
					resizable:false,
					datatype: "json",
					mtype: "POST",
					height:340,
					shrinkToFit:true,
					sortorder: "desc",
					pager: '#dictionary_list_prowed',
					prmNames:{rows:"page.pageSize",page:"page.curPageNo",sort:"page.orderBy",order:"page.order"},
					jsonReader: {
						root: "dataRows",
						page: "curPage",
						total: "totalPages",
						records: "totalRecords",
						repeatitems: false,
						id: "id",
						userdata: "userdata"
					}, 
					serializeGridData:function(postData){
						<s:property value="initFieldScript"  escapeHtml="false"/>
						return postData; 
					}
				});
				jQuery("#dictionary_list_grid").jqGrid('navGrid',"#dictionary_list_prowed",{edit:false,closeOnEscape:false,add:false,del:false,search:false});
	});
	
		function doSubmit(){
			var rowNums = $('#dictionary_list_grid').jqGrid('getGridParam', 'selarrrow');
			if(rowNums.length>0){ 
					insertData(rowNums);   
			}else{
				alert('请选择要插入的记录!');
			}
		}
		
		function doSearch(){
			<s:property value="searchscript"  escapeHtml="false"/>
		}
		//执行数据插入
		function insertData(rowNums){
			<s:property value="insertscript"  escapeHtml="false"/> //插入数据脚本 
			cancel();  
		}
		 function editItem(formId,instanceId,demId){
	 			var url = 'openFormPage.action?formid='+formId+'&instanceId='+instanceId+'&demId='+demId;
	 			var target = "dem"+instanceId; 
	 			var win_width = window.screen.width;
	 			var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	 			
	 	}
		 function addItem(formId,demId){
			 	var url = 'createFormInstance.action?formid='+formId+'&demId='+demId;
	 			var target = "_blank";
	 			var win_width = window.screen.width;
	 			var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		 }
     //关闭窗口
		function cancel(){
			api.close();
		}
</script>
<style> 
		<!--
			#header { background:#6cf;}
			#title { height:20px; background:#EFEFEF; border-bottom:1px solid #990000; font:12px; font-family:宋体; padding-left:5px; padding-top:5px;}
			#baseframe { margin:0px;background:#FFFFFF;}
			#baseinfo {background:#FFFFFF; padding:5px;font:12px; font-family:宋体;}
			.toobar{ 
				 border-bottom:1px solid #990000; 
				 padding-bottom:5px; 
			}
			/*只读数据样式*/
			.readonly_data {
				vertical-align:bottom;
				font-size: 12px;
				line-height: 20px;
				color: #888888;
				padding-right:10px;
				border-bottom:1px #999999 dotted;
				font-family:"宋体";
				line-height:15px;
			}
			.table_form{
				font-family:"宋体";
				font-size: 12px;
			}
			/*数据字段标题样式*/
			.td_title {
			color:#004080;
				line-height: 23px;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				padding-left:10px;
				white-space:nowrap;
				border-bottom:1px #999999 thick;
				vertical-align:middle;
				font-family:"宋体";
			}
			
			/*数据字段内容样式*/
			.td_data {
				color:#0000FF;
				line-height: 23px;
				text-align: left;
				padding-left: 3px;
				font-size: 12px;
				font-family:"宋体";
				border-bottom:1px #999999 dotted;
				vertical-align:middle;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
			}
			.dict_type{
				float:right;
			}
		-->
</style>
</head>
<body class="easyui-layout">
<s:property value="toolbarHtml" escapeHtml="false"/> 
	<div region="center" style="padding:0px;padding:5px;border:0px;overflow-y:hidden;">
			<div style="padding:0px;border:0px solid #ccc;margin-bottom:5px;"> 
		        	<table width="100%" cellpadding='0' cellspacing='0'>
		        		<tr>
		        			<td width="95%"> 
		        			<s:form name="editForm" id="editForm" action="sys_dictionary_runtime_showDataJson.action" theme="simple">
		        					<s:property value="conditionsHtml" escapeHtml="false"/>
		        					<s:hidden name="dictionaryId"></s:hidden> 
		        			 </s:form>
		        			</td>
		        			<td width="5%" style="vertical-align:bottom;padding:3px;">
								<a href="javascript:doSearch()" class="easyui-linkbutton" plain="false" iconCls="icon-search"></a>
							</td>
		        		</tr> 
		        	</table>
		       </div>
				<div id="baseframe">
					
						<table id='dictionary_list_grid'></table> 
						<div id='dictionary_list_prowed'></div> 
					
				</div>
				
	</div>
	<!-- 
	<div region="south" border="false"  style="text-align:left;height:40px;overflow-x:hidden;overflow-y:hidden;background-color:#efefef;border-top:1px solid #666;"> 
 			<div style="text-align:right;border-left:5px;padding-top:5px;padding-right:20px;" >
 				<table width="100%"  cellpadding='0' cellspacing='0'>
 					<tr>
 						<td  style="text-align:left;padding-left:10px;"><s:property value="dictionaryName"  escapeHtml="false"/>【多选】</td>
 						<td  style="text-align:right;">
	 						<a href="javascript:doSubmit()" class="easyui-linkbutton" plain="false" iconCls="icon-ok">确认选择</a>
							<a href="javascript:cancel();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">取消</a>
 						</td>
 					</tr>
 				</table>
 				
		        	
			</div>			
		 </div> -->
</body>
</html>
