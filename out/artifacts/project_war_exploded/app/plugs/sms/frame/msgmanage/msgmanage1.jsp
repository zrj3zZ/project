<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>短信管理信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<link rel="stylesheet" type="text/css" href="iwork_css/plugs/qbegin.css">

</head>

<body>
<form>

 <table width="100%" style="border: 1px dotted #000000" cellspacing="0" cellpadding="2"   >
    <tr> 
      <td  width="100%" height="10px" align="center"   valign="top">
      	<table width="100%" border="1"  cellspacing="0" cellpadding="0" bordercolorlight="#CCCCCC" bordercolordark="#FFFFFF">
        <tr>  <td class="font" align="center">序号</td>
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
					    
           </tr><tr ><td class="font1" colspan='11'>
      		<%=request.getAttribute("listmsg") %> </td></tr>
      	</table>
       
  </table><br />

  
</form>
</body>

</html>