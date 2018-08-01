<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/tld/linkbtn.tld"%>
<%@ taglib prefix="cache"  uri="/oscache"%>

<html>
<head><title>子系统管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/org/company_list.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/org/company_list.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		$(function(){
			$("#selAll").bind("click", function () {
				if ($(this).is(":checked")) {
						$("[name=checkItem]:checkbox").each(function () {
					 		$(this).attr("checked", true);
                		});
				}else{
					 	$("[name=checkItem]:checkbox").each(function () {
					 		$(this).attr("checked", false);
                		});
				}
					 
					 
				 
            }); 
		});
 		function addStation(){
			var stationId = $('#stationId').val();
			var pageUrl = "org_station_ins_add.action?stationId="+stationId;
			 art.dialog.open(pageUrl,{
							id:'dg_addStation',  
		       				title:'新增岗位实例',
							lock:true,
							background: '#999', // 背景色
						    opacity: 0.87,	// 透明度
						    width:500,
						    height:510,
					        close:function(){
					        	window.location.reload();
					        }
						 });
		}
 		function loadStationIns(id){
			var pageUrl = "org_station_ins_load.action?id="+id;
			 art.dialog.open(pageUrl,{
							id:'dg_updateStation',  
		       				title:'更新岗位实例',
							lock:true,
							background: '#999', // 背景色
						    opacity: 0.87,	// 透明度
						    width:500,
						    height:510,
					        close:function(){
					        	window.location.reload();
					        }
						 });
		}
		function loadStationInsScope(stationId,insId){
			var pageUrl = "org_station_scope.action?stationId="+stationId+"&insId="+insId;
			 art.dialog.open(pageUrl,{
						id:'dg_updateStation',  
	       				title:'设置部门范围',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:500,
					    height:510,
				        close:function(){
				        	window.location.reload();
				        }
					 });
		}
		function loadStationInsUserScope(stationId,insId){
			var pageUrl = "org_station_user_scope.action?stationId="+stationId+"&insId="+insId;
			 art.dialog.open(pageUrl,{ 
						id:'dg_updateStation',  
	       				title:'设置人员范围',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:500,
					    height:510,
				        close:function(){
				        	window.location.reload();
				        }
					 });
		}
		function delStationIns(){
			var result = new Array();
			var i = 0;
                $("[name=checkItem]:checkbox").each(function () {
                    if ($(this).is(":checked")) {
                        result.push($(this).attr("value"));
						i++;
                    }
                });
				if(i>0){
					 art.dialog.confirm('确认是否删除当前岗位设置', function(){
									var pageurl = "org_station_ins_del.action";
									 var ids = result.join(",");
									 $.post(pageurl,{ids:ids},function(msg){ 
										 if(msg=='success'){
											 location.reload(); 
										 }else{
											 alert('权限不足，无法执行删除操作!');
										 }
										 }); 
									return;
							}); 
				}else{
				 	art.dialog.tips("请勾选删除的实例");
				}
		}
		
	</script>
</head>	
<body  class="easyui-layout">
<!-- <div class="menubackground"style="padding-left:5px;padding-top:2px;padding-bottom:2px;"> -->
<div region="north" border="false" split="false"   class="nav" id="layoutNorth">
<div class="tools_nav"> 
		<s:property value="toolbar"  escapeHtml="false"/>
		<a href="javascript:addStation();" class="easyui-linkbutton"  plain="true" iconCls="icon-add" >新增</a>
		<a href="javascript:delStationIns();" class="easyui-linkbutton"  plain="true" iconCls="icon-remove" >删除</a>
		<a href="javascript:location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
	</div>
</div>
<div region="center" border="false" split="false"   class="nav" id="layoutNorth">

	<table id="tt" cellpadding="1" cellspacing="1" class="table3" width="100%">
    <tr >
        <th  width="67" ><input type="checkbox" name="selAll" id="selAll" /></th>
        <th  width="67" >标识</th>
        <th width="251"  >岗位人员</th>
        <th width="232"  >起始时间</th>
        <th width="240"  >结束时间</th>
        <th width="169"  >状态</th>
        <th width="163"  >操作</th>
    </tr>
    <tbody> 
    <s:iterator value="station_ins_list" status="status">
        <tr >
        <td ><s:property value="#status.count"/><input type="checkbox" name="checkItem" value="<s:property value='id'/>"/></td>
        <td ><a href="javascript:loadStationIns(<s:property value="id"/>)" ><s:property value="title"/></a></td>
        <td ><s:property value="owners"/></td>
        <td ><s:property value="%{getText('{0,date,yyyy-MM-dd }',{startDate})}"/></td>
        <td><s:property value="%{getText('{0,date,yyyy-MM-dd }',{endDate})}"/></td> 
        <td>
        	<s:if test="status==0">
        		停止
        	</s:if>
        	<s:else>
        		启动
        	</s:else>
        </td> 
        <td >
        	<a class="easyui-linkbutton" plain="true" icon="icon-process-purview" href="javascript:loadStationInsScope(<s:property value="stationId"/>,<s:property value="id"/>);" >部门范围设置</a> 
        	<a  class="easyui-linkbutton" plain="true" icon="icon-org-user" href="javascript:loadStationInsUserScope(<s:property value="stationId"/>,<s:property value="id"/>);" >人员范围设置</a> 
        
        </td>
    </tr>
    </s:iterator>
   
    </tbody>
</table>
<form id="editForm" method="post">
<s:hidden  id='stationId' name='stationId'></s:hidden>
</form>
</div>
</body>
</html>
