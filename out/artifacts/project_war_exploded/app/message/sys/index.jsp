<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/message/sysmessageaction.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js" ></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />	
	<script type="text/javascript" src="iwork_js/jquery.form.js" ></script>
	<script type="text/javascript" src="iwork_js/message/sysmessageaction.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
</head>
<script type="text/javascript">

//添加
function sendSysMsg(){
	  var pageUrl = "sysmsg_mge_add.action"; 
	  	art.dialog.open(pageUrl,{
						id:'cmsWinDiv',
						title:'发送系统消息',   
						lock:true,
						background: '#999', // 背景色
						opacity: 0.87,	// 透明度
						width:700,
						height:550
					 });
}
	
</script>
<body class="easyui-layout" style="overflow-y:scroll">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:auto;">
	<div style="padding:2px;background:#efefef;border-bottom:1px solid #efefef">
		<a href="javascript:sendSysMsg();" class="easyui-linkbutton" plain="true" iconCls="icon-add">发送系统消息</a>
		<a href="javascript:refresh();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>		
		
	</div>
		<table  width="98%" style="border:1px solid #A4BED4;">
					<thead>
						<tr class="header">
							<td   nowrap="nowrap">序号</td>
							<td   nowrap="nowrap">消息标题<br></td>
							<td   nowrap="nowrap">消息内容<br></td>
							<td   nowrap="nowrap">接收范围<br></td>
							<td   nowrap="nowrap">消息类型<br></td>							
							<td   nowrap="nowrap">优先级<br></td>
							<td   nowrap="nowrap">发送时间<br></td>
							<td   nowrap="nowrap">发送状态<br></td>
							
						</tr>
						</thead>
						<tbody>
						<s:iterator value="logListBean.list" status="status">
							<tr class="cell">								
								<td  style="text-align:center" width='2%'><s:property value="#status.count"/></td>
								<td width='10%'><s:property value="title"/></td>
								<td width='30%'><s:property value="content"/></td>
								<td width='20%'><s:property value="rcvRange"/></td>
								<td style="text-align:center">
									<s:if test="type==0">
										系统消息
									</s:if>
									<s:elseif test="type==1">
										生日提醒
									</s:elseif>
									<s:elseif test="type==2">
										流程提醒
									</s:elseif>
									<s:elseif test="type==3">
										会议通知
									</s:elseif>
								</td>
								<td style="text-align:center">
									<s:if test="priority==-1">
										低
									</s:if>
									<s:elseif test="priority==0">
										中
									</s:elseif>
									<s:elseif test="priority==1">
										高
									</s:elseif>									
								</td>
								<td width='10%' nowrap><s:property value="sendDate"/></td>
								<td style="text-align:center">
									<s:if test="status==1">
										成功
									</s:if>
									<s:else>
										失败
									</s:else>
								</td>
							</tr>
						 </s:iterator>
						 <s:if test="%{logListBean.totalRows > 0}">
                			<tr><td colspan="8" align="right">
                				共<s:property value="logListBean.totalRows"/> 条记录
      							共<s:property value="logListBean.totalPages"/> 页
        						当前第<s:property value="logListBean.currentPage"/>页<br/>
        
        					<s:if test="%{logListBean.currentPage == 1}">
          						 第一页 上一页
        					</s:if>
        					<s:else>
            					<a href="sysMessageAction!index.action?currLogPage=1">第一页</a>
            					<a href="sysMessageAction!index.action?currLogPage=<s:property value="%{logListBean.currentPage-1}"/>">上一页</a>
        					</s:else>
        					<s:if test="%{logListBean.currentPage != logListBean.totalPages}">
            					<a href="sysMessageAction!index.action?currLogPage=<s:property value="%{logListBean.currentPage+1}"/>">下一页</a>
            					<a href="sysMessageAction!index.action?currLogPage=<s:property value="logListBean.totalPages"/>">最后一页</a>
        					</s:if>
        					<s:else>
            					下一页 最后一页
       						</s:else> 
       					</td></tr>	
       					</s:if>
						
						</tbody>
						
			</table>
	</div>
</body>
</html>
