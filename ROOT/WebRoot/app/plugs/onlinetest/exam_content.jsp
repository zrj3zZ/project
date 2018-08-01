<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<title>在线考试</title>
		<link rel="stylesheet" href="iwork_css/common.css" type="text/css">
		<link rel="stylesheet" href="iwork_css/plugs/exam.css"
			type="text/css">
		<link rel="stylesheet" href="iwork_css/jquerycss/zTreeStyle.css"
			type="text/css">
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>

		<script type="text/javascript">
	var setting = {
			view: {
				dblClickExpand: dblClickExpand
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeClick: beforeClick
			}
		};

		function dblClickExpand(treeId, treeNode) {
			return treeNode.level > 0;
		}

		$(document).ready(function(){
			var examNo = $("#examNo").val();
			$.post("examTreeJson.action",{examNo:examNo},function(data){
				var zNodes = data;
				$.fn.zTree.init($("#tree"), setting, zNodes);
				nextExam(1);
			},"json");
		});
		
		var log, className = "dark";
		
		function beforeClick(treeId, treeNode, clickFlag) {
			className = (className === "dark" ? "":"dark");
			return (treeNode.click != false);
		}
		
		var falg = true;
		
		function openOneExam(examId,countNo){	
			if(falg){
				answerQuestion();
			}
			var instanceId = $("#instanceId").val();
			$.post("openOneExam.action",{examNo:examId, countNo:countNo, instanceId:instanceId},function(data){
				var strs = new Array();
				strs = data.split("||");
				var examText = strs[0];
				$("#examText").html(examText);
				$("#countNo").val(countNo);
				falg = true;
			});
		}
		
		function nextExam(countNo){
		
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var selectedNodes = treeObj.getSelectedNodes();
			if(selectedNodes.length > 0 && countNo == 1){
				if(selectedNodes[0].countNo == 1){
					alert("已经是第一题了。");
				}
			}	
			
			
			var testNoStr = "试题" + countNo;
			var nodes = treeObj.getNodesByParamFuzzy("name", testNoStr, null);
			if (nodes.length > 0) {
				if(falg){
					answerQuestion();
					falg = false;
				}
				treeObj.selectNode(nodes[0],false);
				openOneExam(nodes[0].examId,countNo);
			}else{
				testNoStr = "试题" + (countNo - 1);
				nodes = treeObj.getNodesByParamFuzzy("name", testNoStr, null);
				if (nodes.length > 0) {
					answerQuestion();
				}
				alert("已经是最后一题了。");
			}
		}
		
		function answerQuestion(){
		
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getSelectedNodes();
			if(nodes.length > 0){
				var answerStr = $("input:radio:checked").val();
				if(answerStr == undefined){
					answerStr = "";
					$('input[type="checkbox"]:checked').each(
						function() {
							answerStr = answerStr + $(this).val() + "||";
						}
					);
				}
				
				if(answerStr != ""){
					var instanceId = $("#instanceId").val();
					var examNo = $("#examNo").val();
					var examId = nodes[0].examId;
					var countNo = nodes[0].countNo;
					$.post("answerQuestion.action",{
						countNo:countNo, 
						answerStr:answerStr, 
						instanceId:instanceId,
						examNo:examNo,
						examId:examId
					});
				}
			}else{
				var testNoStr = "试题" + 1;
				var oneNodes = treeObj.getNodesByParamFuzzy("name", testNoStr, null);
				if (oneNodes.length > 0) {
					treeObj.selectNode(oneNodes[0],false);
					openOneExam(oneNodes[0].examId,oneNodes[0].countNo);
				}
			}
		}
		
		function examSubmit(){
			
			var instanceId = $("#instanceId").val();
			var examNo = $("#examNo").val();
			
			$.post("examExecuteSubmit.action",{instanceId:instanceId, examNo:examNo},function(data){
				alert(data);
				location.reload();
			});
		}
</script>
	</head>
	<body>
		<div class="exam_bg1">
			<div class="exam_bg2">
				<div class="exam_head">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;
							</td>
							<td colspan="2"
								style="text-align: center; font-size: 24px; font-family: 微软雅黑; padding-bottom: 3px;">
								试卷标题 ：
								<s:property value='examTitle' />
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;
							</td>
							<td style="font-size: 12px;">
								考试人：
								<s:property value='userInfo' />
							</td>
							<td>
								<s:property value='paperSubmitBtn' escapeHtml="false" />
							</td>
						</tr>
					</table>
				</div>
				<div class="exam_main">
					<div></div>
					<div class="exam_main_tree">
						<div class="exam_main_tree_left">
							<ul id="tree" class="ztree"></ul>
						</div>
						<div class="exam_main_treefg"></div>
						<div style="clear: both"></div>
					</div>
					<div class="exam_main_main">
						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							style="font-size: 14px;line-height: 40px;" id="examText">
	
						</table>
					</div>
				</div>
			</div>
		</div>
		<s:hidden name = "instanceId" />
		<s:hidden name = "examNo" />
	</body>
</html>