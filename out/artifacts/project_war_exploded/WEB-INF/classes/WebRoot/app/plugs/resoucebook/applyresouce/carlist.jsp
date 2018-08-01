<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
		<title>车辆预定</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">		
		
	   <link rel="stylesheet" type="text/css" href="iwork_css/plugs/getcarlist.css">
       <script type="text/javascript" src="iwork_js/plugs/getcarlist.js"></script>

</head>
<body>
<div id="nav"><div class='tit2'>
<span id="url_route" name="url_route"> </span></div></div>
<!--  div id=actionsoftPopupWin class=actionsoftPopupWinCSS></div>-->
<script type="text/javascript" src="app/plugs/resoucebook/js/popup.js"></script> 
  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td>
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><table width="760" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td  height="21" class="actionsoftTitle">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td> </td>
                      <td align=left valign="bottom"><span class="STYLE2"><%=request.getAttribute("spacename") %></span></td>
                      <td align="right" valign="bottom"> <span class="STYLE1"><%=request.getAttribute("memo") %></span></td>
                    </tr>
                  </table></td>
              </tr>
              <tr>
                <td height="5" bgcolor="#D6E2EB"></td>
              </tr>
			  <tr>
                <td height="15"></td>
              </tr>
              <tr>
                <td align="center"><div class="main"  align="left">
                  	<%=request.getAttribute("carlist") %>
                </div></td>

            </table></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td>
    </tr>
  </table>
  <!-- 车辆申请 -->
 <div id="caradd" style="padding: 15px; width: 450px; height: 350px;">
 <s:form>
 <table class="font">
 <tr><td nowrap align=right>
 资源编号/类型：</td><td nowrap><input type=text  name=carid id=carid size=4 disabled>  
 /<input type=text name=carname id=carname size=8 class="easyui-validatebox" style="font-size: 12px" disabled>
 </td><td nowrap>&nbsp;准乘人数：</td><td><input type=text name=carpara3 id=carpara3 disabled size=4></td></tr>
 <tr><td align=right>车牌号码：</td><td><input type=text name=carpara1 id=carpara1 disabled size=10></td>
 <td> &nbsp;车辆颜色：</td><td><input type=text name=carpara2 id=carpara2 disabled size=8></td></tr>
 <tr><td  align=right>
 开始日期：</td><td><input type=text class='easyui-datebox' editable='false' name=cardate1 id=cardate1 style='width:85px;' >  
 </td><td> &nbsp;开始时间：</td><td><input type=text name=carhour1 id=carhour1 size=2 class="easyui-validatebox" style="font-size: 12px">时/<input type=text name=carmin1 id=carmin1 size=2 class="easyui-validatebox" style="font-size: 12px" value="00">分  
 </td></tr>
 <tr><td  align=right>
 结束日期：</td><td><input type=text  name=cardate2 id=cardate2 class='easyui-datebox' editable='false' style='width:85px;'>  
 </td><td>&nbsp;结束时间：</td><td><input type=text name=carhour2 id=carhour2 size=2 class="easyui-validatebox" style="font-size: 12px">时/<input type=text name=carmin2 id=carmin2 size=2 class="easyui-validatebox" style="font-size: 12px" value="00">分  
 </td></tr>
 <tr><td valign="top" align=right>备注：
</td><td colspan='3'><textarea cols=35 rows=4 name="carmemo" id="carmemo"
style="font-size: 12px; overflow: auto"></textarea>
</td></tr></table>
 <input type=hidden name=hid id=hid><input type=hidden name=hname id=hname>
</s:form>
  </div>
</body>
</html>
