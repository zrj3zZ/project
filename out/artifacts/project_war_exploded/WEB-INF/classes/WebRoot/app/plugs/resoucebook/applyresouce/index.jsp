<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
		<title>车辆预定</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_css/plugs/getcarlist.css">
       <script type="text/javascript" src="iwork_js/plugs/resourcebook_runtime_index.js" ></script>
       <script language="javascript" src="iwork_js/commons.js"></script> 
	   <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
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
                <td  height="21" >
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td> </td>
                      <td align=left valign="bottom" style="font-size:18px;font-family:黑体"><s:property value="spaceName"/></td>
                       <td align="right" valign="bottom"> <span class="STYLE1"><s:property value="info"/></span></td>
                      <td align="right" valign="bottom"><a href="javascript:previous()" style="font-size:12px;"><img alt="查看前一周期预定信息" src="iwork_img/icon/arrow_left_small.gif" border="0">向前<s:property value="cycle"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:next()" style="font-size:12px;">向后<s:property value="cycle"/><img alt="查看后一周期预定信息" src="iwork_img/icon/arrow_right_small.gif" border="0"></a></td>
                     
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
                  	<s:property value="html"  escapeHtml="false"/> 
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
 <s:form name="editForm" id="editForm" theme="simple">
	<s:hidden name="startdate"></s:hidden>
	<s:hidden name="spaceId"></s:hidden>
	<s:hidden name="spaceName"></s:hidden>
</s:form>
</body>
</html>
