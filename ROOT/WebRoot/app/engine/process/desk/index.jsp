<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>流程处理中心</title>
<link href="iwork_css/engine/process_desk_center_operate_log.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/process_center.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
<script type="text/javascript" src="iwork_js/process/process_desk.js"></script> 
<script type="text/javascript" src="iwork_js/scrollpagination.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script>
var pageNo = 1;
var itemNum = 0;
var bl = false;
$(document).ready(function(){
		$('#contentTable').scrollPagination({ 
			contentPage: 'process_desk_showlist.action', // the url you are fetching the results
			contentData: {pageNo :function(){return pageNo++},itemNum :function(){return itemNum},searchKey:function(){return $("#searchKey").val()}}, // these are the variables you can pass to the request, for example: children().size() to know which page you are
			scrollTarget: $(window), // who gonna scroll? in this example, the full window
			heightOffset: 15, // it gonna request when scroll is 10 pixels before the page ends
			beforeLoad: function(){ 
				$('#loading').fadeIn(); 
				$(".moreBtnTr").each(function(){
				    	$(this).remove();
				  });
			}, 
			afterLoad: function(elementsLoaded,data){
				 $('#loading').fadeOut(); 
				  $(elementsLoaded).fadeInWithDelay();  
				 if(data==""){
				 	 $('#contentTable').stopScrollPagination();  
					art.dialog.tips("您没有更多记录了",2); 
				 }
				 itemNum = $("td.row3:last").html();
			}
		});
		// code for fade in element by element
		$.fn.fadeInWithDelay = function(){
			var delay = 0;
			return this.each(function(){
				$(this).delay(delay).animate({opacity:1}, 200);
				delay += 100;
			});
		};
		/* $(".list-selected").click(
				function(){
					$(".list-selected").hide();
					$(".icon").show();
					window.location="processManagement_batch_index.action";
				});	    */
	});
	 function getMoreItem(){
			requireItem();
	}
	
	//请求某天的HTML
	function requireItem(){
		$('#loading').fadeIn();	
		 itemNum = $("td.row3:last").html();
		 var searchKey = $("#searchKey").val();
		var url = "process_desk_showlist.action";
		$.post(url, {
	    	pageNo : pageNo++,
	    	itemNum :itemNum,
	    	searchKey :searchKey
		}, function(rText) {
			$('#loading').fadeOut();
			if(rText==""){
				art.dialog.tips("您没有更多记录了",2);
			}else{
				$(".moreBtnTr").each(function(){
				   $(this).remove();
				  });
				$("#contentTable").append(rText); 
			}
		});  
	}
	 
	
</script>
<style>
	.loading {
		background:#BDEBEE;
		color:#303030;
		font-size:20px;
		padding:5px 10px;
		text-align:center;
		width:450px;
		margin:0px auto;
		display:none;
		border-radius: 5px;
	}
	.table-tr-notice td{
	  color:red;
	}
	.moreBtn{
		text-align:center;
	}
	.moreBtn a{
		padding:10px;
		font-size:14px;
		font-family:微软雅黑;
	}
	.top_toolbar{
		height:2px;
	}
</style>
</head>
<body >
	<div>
				  	<div class="process_header">
						  <div class="process_head_tab_box"> 
						 	 <a class="process_head_tab_active" title="显示所有待审批流程未读的抄送任务或通知" href="process_desk_index.action"><s:text name="process.desk.todo"/><span id="taskNum"></span></a>
						     <a class="process_head_tab"  title="显示当前用户已办理，但未最终归档的流程任务"  href="process_desk_finish.action"><s:text name="process.desk.history"/></a>  
						     <a class="process_head_tab"  title="显示所有接收的抄送及流程通知任务"  href="process_desk_notice.action"> <s:text name="process.desk.notice"/><span id="NoticeNum"></span></a>
						      <a class="process_head_tab"  title="显示当前用户已办理，所有办理过的任务历史及审批意见"  href="process_desk_history.action"><s:text name="process.desk.dolog"/></a>
						     <!--  <a class="process_head_tab"  title="显示所有当前用户发起的流程单据跟踪查询"  href="process_desk_createlog.action">我发起的流程</a> -->
						    <!--    <a class="process_head_tab" href="process_desk_notice.action"> 已归档流程</a>-->
						      </div>
						      <div style="padding-top:3px;">
						      </div>
						  <div class="process_head_tab_right">
						    <form id="search" class="search">
						      <input name="searchKey" id="searchKey" value="<s:property value="searchKey"/>" class="txt" type="text">
						      </input>
						      <button class="search-submit" data-ca="search-btn"></button>
						    </form>
						  </div>
						  <div class="switch-view">
							<a href="processManagement_batch_index.action" class="list-selected"  title="切换到缩略图模式"></a>
								<!-- <a href="processManagement_batch_index.action" class="icon" title="切换到列表模式" style="display:none;border:0px;"></a> -->
						 </div>
					</div>
			<div >
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr class="wr-table-hd-inner" >
			        <td class="row3" style="width:30px;"><s:text name="process.desk.title.num"/></td>
			        <td class="row1"><s:text name="process.desk.title.from"/></td>
			        <td colspan="2" ><s:text name="process.desk.title.tasktitle"/></td>
			        <td class="row2"><s:text name="process.desk.title.recivetime"/></td>
			        <td class="row1"><s:text name="process.desk.title.timeline"/></td>
			      </tr>
			      <tbody  id="contentTable" >
			      
			      </tbody>
			  </table>
			 
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			    <tr>
			      <td width="100%">
			      	<div class="loading" id="loading">正在加载...</div>
    			  </td>
			    </tr>
			  </table>
			</div>
		</div>
	
</body>
</html>
<script>
showTaskCount();
</script>