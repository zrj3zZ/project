
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>IWORK综合应用管理系统</title>
			<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
			<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
			<link rel="stylesheet" type="text/css"	href="iwork_css/jquerycss/default/easyui.css">
			<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
			<link rel="stylesheet" type="text/css"	href="iwork_css/jquerycss/zTreeStyle.css">
			<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.metadata.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.validate.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.form.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
			<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
			<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js" ></script>
			<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
			<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>					
	<style>
				.topHead {
					background: #fff;
					border-bottom: 3px solid #677e9b;
				}
				
				.title {
					background: #f2f4f6;
					border-bottom: 1px solid #c6c9ca;
					border-right: 1px solid #c6c9ca;
					padding: 2px;
					padding-left: 5px;
					font-family: 微软雅黑;
				}
				
				.title_icon {
					background: url(iwork_img/email/mail227195.png) 112px -18px no-repeat;
					width: 26px;
					height: 16px;
				}
				
				.unread_icon {
					border: none;
					padding: 0;
					margin: 0;
					background: url(iwork_img/email/mail227195.png) 0px -82px no-repeat;
					width: 26px;
					height: 16px;
					margin-left: 8px;
				}
				
				.maintitle {
					font-size: 14px;
					font-family: 微软雅黑;
					font-werght: bold;
					padding: 3px;
					color: #304d79;
					border-bottom: 1px solid #efefef;
				}
				
				.maintitle span {
					color: #666;
					font-size: 12px;
					padding: 2px;
				}
				
				.mailItem {
					height: 30px;
					border-bottom: 1px solid #efefef;
				}
				
				.mailItem td {
					font-family: 微软雅黑;
				}
				
				.mailItem .mailTitle span {
					color: #a0a0a0;
					font-weight: 500;
				}
				
				.mailItem:hover {
					height: 30px;
					background: #f3f3f3;
					cursor: pointer;
				}
				
				.unread {
					font-weight: bold;
				}
	</style>
<script type="text/javascript">
	  $(function(){
	  	var total = "("+$("#total").val()+")";
		parent.$("#total").text(total);
		$("#pp").pagination({
			    total:<s:property value="total"/>, 
			    pageNumber:<s:property value="pageNumber"/>,
			    pageSize:<s:property value="pageSize"/>,
			    onSelectPage:function(pageNumber, pageSize){
			    	var pageUrl="iwork_mail_draft_index.action?pageSize="+pageSize+"&pageNumber="+pageNumber;
					window.location.href = pageUrl;
				}
			});
	  });
		
		//彻底删除草稿箱邮件记录
	function cleanEmail(){		
		  	//获取选中单选框的id值
			var counts = document.getElementsByName('ckb_selectAll');				
			var ids = "";
			if(counts.length>1){
				for(var i=0;i<counts.length;i++){
					if(counts[i].checked){
						ids = ids + counts[i].value + ",";
					}
				}
			}
			if(ids!=""){
					if(confirm("确认要删除？")){
						ids = ids.substring(0,ids.lastIndexOf(','));
						var url ='iwork_mail_write_cleanDraft.action?id='+ids;
		 				var option={
							type: "post",
							url: url,
							data: "",
							cache:false,	
							success: function(msg){
								if(msg=="succ"){
									art.dialog.tips('删除成功');
									window.location.reload();
								}else{
									art.dialog.tips('删除失败');
									return false;
								}
							}
					 	}
						$.ajax(option);
					}
				}else{
						art.dialog.tips('未选中任何邮件');
						return false;
					}
		}
 // 全选、全清功能
	function selectAll() {
		if ($("input[name='ckb_selectAll']").attr("checked")) {
			$("input[name='ckb_selectAll']").attr("checked", true);
		} else {
			$("input[name='ckb_selectAll']").attr("checked", false);
		}
	}
	//双击邮件时触发的事件
	 function showDetailInfo(id,title){
      		if(title.length>5){
      			title = title.substring(0,5)+"...";
      		}
      		var pageurl="iwork_mail_write.action?id="+id;
      		parent.addTab(title,pageurl,""); 
      	}
       
 
</script>	
	</head>
	<body class="easyui-layout">
		<div region="north" border="false">
			
			<div class="tools_nav" style="padding-left: 2px;">
				<!--<input type="button" name="removeBtn" onclick="javascript:deleteEmail();" value="删除" />-->
				<input type="button" name="removeBtn" onclick="javascript:cleanEmail();" value="彻底删除" />
				
			</div>
		</div>
		<div region="center" border="false"
			style="background: #fff; border: 1px solid #fff">
			<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td class="title" style="width: 15px;">
						<input type="checkbox" id="ckb_selectAll" onclick="selectAll()" 
							name="ckb_selectAll"
							value="0"
							title="选中/取消选中" />
					</td>
					
					<td class="title" style="width: 30px;">
					
					</td>
					
					<td class="title" style="width: 120px;">
						收件人
					</td>
					<td class="title">
						主题
					</td>
					<td class="title" style="width: 120px;">
						时间
					</td>
					<td class="title" style="width: 20px;">
						
					</td>
				</tr>
				<s:iterator value="ownerlist" status="status">

					<tr class="mailItem" name="sendMail">
						<td>
							<input name ="ckb_selectAll" type="checkbox" value="<s:property value="id"/>"
								title="选中/取消选中" />
						</td>
						<td class="mailicon">
							<img id="bflag" src="iwork_img/sysletter/mail_have_readed.png"/>

						</td>
						<td class="mailUser"  onClick="javascript:showDetailInfo('<s:property value="id"/>','<s:property value="title"/>');" >
							<s:property value="mailTo" />
						</td>
						<td  onClick="javascript:showDetailInfo('<s:property value="id"/>','<s:property value="title"/>');"  class="mailTitle" title="<s:property value="subTitle"/>">
							<s:property value="title" />
							<span> </span>
						</td>
						<td class="mailtime">
							<s:property
								value="%{getText('{0,date,yyyy-MM-dd HH:mm}',{createTime})}" />
						</td>
						<td class="mailicon"></td>
					</tr>
				</s:iterator>
				<s:if test="ownerlist==null||ownerlist.size()==0"> 
		 					<tr class="mailItem ">
		 						<td colspan="5" style="text-align:center">【空】</td>
		 						<td class="mailicon">&nbsp;</td>
		 					</tr>
		 			</s:if>

			</table>
		</div>
		</div>
		<!--<div region="south" border="false" style="height: 32px;">
			<div id="pp" style="background: #efefef; text-align: right; border: 1px solid #ccc;"></div>
		</div>
		--><div region="south" border="false" style="height: 32px;">
		<div id="pp" style="background: url('../iwork_img/engine/tools_nav_bg.jpg') repeat-x;border:1px solid #ccc;"></div>
	</div>
	<s:hidden id="total" name="total"></s:hidden>
	</body>
</html>
