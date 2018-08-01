<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
 <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
<title>单选字典</title> 
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/plugs/dictionary_runtime.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 
	
<script type="text/javascript">
var api = art.dialog.open.api, W = api.opener;
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
					multiselect: false,
					sortname: 'ID',
					viewrecords: true,
					resizable:false,
					datatype: "json",
					mtype: "POST",
					height:350, 
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
					},
					onSelectRow: function(rowid,status,e){
				    	rowNum = rowid;
					},
				    ondblClickRow: function(id,iRow,iCol,e){
				    	insertData(id);
					},gridComplete:function(){
						showSysTips();
					}
				}); 
				jQuery("#dictionary_list_grid").jqGrid('navGrid',"#dictionary_list_prowed",{edit:false,closeOnEscape:false,add:false,del:false,search:false});
	});
	
		function extend(des, src, override){
		   if(src instanceof Array){
		       for(var i = 0, len = src.length; i < len; i++)
		            extend(des, src[i], override);
		   }
		   for( var i in src){
		       if(override || !(i in des)){
		           des[i] = src[i];
		       }
		   } 
		   return des;
		}
		function doSubmit(){
			if(rowNum!=null&&rowNum.length>0){
					insertData(rowNum);  
			}else{
				alert('请选择要插入的记录!');
			}
		} 
		function doSearch(){
			<s:property value="searchscript"  escapeHtml="false"/>
		}
		//执行数据插入
		function insertData(iRow){
			var rd = jQuery("#dictionary_list_grid").jqGrid('getRowData',iRow);
			var dictionaryId = $("#dictionaryId").val();
			try{
					var origin = artDialog.open.origin; 
				    origin.execDictionarySel(dictionaryId,rd);
		    }catch(e){}
		    
			var parentForm = origin.document.forms[0]; 
			<s:property value="insertscript"  escapeHtml="false"/>
			cancel(); 
		}
		//装载数据对象
			function rebackData(inputName,value){
				if(inputName!=""){
					try{
						var origin = artDialog.open.origin; 
						var input = origin.document.getElementById(inputName);
						input.value = value;
					}catch(e){
						
					}
				}
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
 			<s:property value="conditionsHtml" escapeHtml="false"/>
				<div id="baseframe">
						<table id='dictionary_list_grid'></table> 
						<div id='dictionary_list_prowed'></div> 
				</div>
				<input type='hidden' name='dictionaryId'  id='dictionaryId'  value='<s:property value="dictionaryId"/>' >
	</div>
	<!-- 
	<div region="south" border="false"  style="text-align:left;height:40px;overflow-x:hidden;overflow-y:auto;background-color:#efefef;border-top:1px solid #666;"> 
 			<div style="text-align:right;border-left:5px;padding-top:5px;padding-right:20px;" >
 				<table width="100%"  cellpadding='0' cellspacing='0'>
 					<tr>
 						<td  style="text-align:left;padding-left:10px;"><s:property value="dictionaryName"  escapeHtml="false"/></td>
 						<td  style="text-align:right;">
	 						<a href="javascript:doSubmit()" class="easyui-linkbutton" plain="false" iconCls="icon-ok">确认选择</a>
							<a href="javascript:cancel();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">取消</a>
 						</td>
 					</tr>
 				</table>
 				
		        	
			</div>			
		 </div>   -->
</body>
</html>
