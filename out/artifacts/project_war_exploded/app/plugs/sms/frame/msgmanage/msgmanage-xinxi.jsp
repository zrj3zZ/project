<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>短信管理信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<link rel="stylesheet" type="text/css" href="iwork_css/plugs/msgmanage.css">

</head>

<body>
<form method="post">
<div id="win"></div>
  <table width="100%" style="border: 1px dotted #000000" cellspacing="0" cellpadding="2" >
    <tr> 
      <td  width="100%" height="10px" align="center"   valign="top">
      	<table  width="100%" border="1"  cellspacing="0" cellpadding="0" bordercolorlight="#CCCCCC" bordercolordark="#FFFFFF">
        <tr>  <td class="font" ><div align="center" >序号</div></td>
						<td class="font" nowrap><div align="center" >发送批号</div></td>
          
            <td class="font"  ><div align="center">提交时间</div></td>
						<td class="font"  nowrap><div align="center">发送时间</div></td>
							<td class="font"  nowrap><div align="center">状态</div></td>       
			      <td class="font" nowrap><div align="center" >提交人</div></td>
            <td class="font"  nowrap><div align="center">接收号码</div></td>
						<td class="font"  ><div align="center">短信内容</div></td>
						<td class="font"  ><div align="center">提交部门</div></td>
						<td class="font"  nowrap><div align="center">通道</div></td>
			      <td class="font"  nowrap><div align="center">短信提供商</div></td> 
					    
           </tr>
			    <s:property value="listmsg"  escapeHtml="false"/>
			    <tr align="right">
			    	<td colspan="11" class="font2">
			    		共<s:property value="totalRows"/>行&nbsp;
			    		第<s:property value="currentPage"/>页&nbsp;
			    		共<s:property value="pager.getTotalPages()"/>页&nbsp;
			    		<a href="javascript:query2('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">首页</a>
			    		<a href="javascript:query2('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">上一页</a>
						<a href="javascript:query2('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">下一页</a>
                        <a href="javascript:query2('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">尾页</a>

			    	</td>
			    </tr>	
			   
			</table>
  </table><br />
  <table class="font" ><tr><td>
  更改为状态：<select name="updatestatus" id="updatestatus"><option value="" selected>不修改</option>
    <option value='2'>成功</option> <option value='1'>待发</option> <option value='6'>失败</option>
	</select>
	</td><td>通道:<%=request.getAttribute("updatesp") %>
	</td><td><input type=button value="批量修改记录" class="font"   onClick="mu()" name='buttonquery'  border='0'>
	</td></tr></table>
   <input type=hidden name=tvalue>
   <input type=hidden name=senderd value='<%= request.getAttribute("senderd")%>' >
   <input type=hidden name=supplierd value='<%=request.getAttribute("supplierd") %>' >
   <input type=hidden name=channeld value='<%=request.getAttribute("channeld") %>' >
   <input type=hidden name=mobilenumd value='<%=request.getAttribute("mobilenumd") %>' >
   <input type=hidden name=keywordsd value='<%=request.getAttribute("keywordsd") %>' >
   <input type=hidden name=statusd value='<%=request.getAttribute("statusd") %>' >
   <input type=hidden name=begintimed value='<%=request.getAttribute("begintimed") %>' >
   <input type=hidden name=endtimed value='<%=request.getAttribute("endtimed") %>' >
   <input type=hidden name=batchd value='<%=request.getAttribute("batchd") %>' >
   
   <input type=hidden name=returnvalue id='returnvalueupdate' value='<#breturnvalueupdate>'>
</form>
</body>

<script type="text/javascript" src="iwork_js/plugs/msgmanage.js"></script>

</html>