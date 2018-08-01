<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>无标题文档</title>
<link href="../css/public.css" rel="stylesheet" type="text/css" />
</head>

<body>

<table border="0" cellpadding="0" cellspacing="0" class="table1">
  <tr>
    <td class="comtit1ogo1">&nbsp;</td>
    <td class="comtitbg">查询条件</td>
    <td class="comtitright">&nbsp;</td>
  </tr>
</table>
<table border="0" align="center" cellpadding="0" cellspacing="0" class="table2">
  <tr>
    <td class="td3"><table width="100%" border="0" cellpadding="2" cellspacing="0">
      <tr>
        <td width=50>用户名</td>
        <td><input name="textfield2" type="text" class="true_input" /></td>
        <td width=50>出生地</td>
        <td><select name="select">
          <option>下接列表样式</option>
          <option>北京</option>
          <option>山东　</option>
          <option>上海</option>
        </select>        </td>
      </tr>
      <tr>
        <td>性别</td>
        <td><input type="checkbox" name="checkbox" value="checkbox" />
          男
            <input type="checkbox" name="checkbox2" value="checkbox" />
            女
            <input type="checkbox" name="checkbox3" value="checkbox" />
          <input type="radio" name="radiobutton" value="radiobutton" />
          <input type="radio" name="radiobutton" value="radiobutton" />
          <input type="radio" name="radiobutton" value="radiobutton" /></td>
        <td>初生日期</td>
        <td><input name="textfield232" type="text" class="no_input" value="2008-08-08" readonly="true"/></td>
      </tr>
      <tr>
        <td>邮件</td>
        <td><input name="textfield2323" type="text" class="no_input" value="111111@21sdfo.com" readonly="true"/></td>
        <td>部门</td>
        <td><select name="select2">
          <option>软件研发中心</option>
          <option>技术管理部</option>
          <option>质量部</option>
                </select></td>
      </tr>
      <tr>
        <td>备注</td>
        <td><textarea name="textarea" cols="30" rows="3" class="textarea"></textarea></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" class="table1">
  <tr>
    <td class="comtit1ogo2">&nbsp;</td>
    <td class="comtitbg">查询列表</td>
    <td class="comtitright">&nbsp;</td>
  </tr>
</table>
<table cellpadding="1" cellspacing="1" class="table3">
  <tr class="tablehead">
    <td>用户名</td>
    <td>性别</td>
    <td>电话</td>
    <td>出生日期</td>
    <td>EMAIL</td>
    <td>出生地</td>
    <td>部门</td>
    <td width=80>操作</td>
  </tr>
  <tr class="tr1"　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　>
    <td>张三李四</td>
    <td>男</td>
    <td>123456789012</td>
    <td>2008-08-08</td>
    <td>12346789＠123.com</td>
    <td>北京</td>
    <td>质量部</td>
    <td><input name="Submit" type="button" class="but_editor" id="Submit" value="" /><input name="Submit" type="button" class="btn_delete" id="Submit" value="" /><input name="Submit" type="button" class="btn_browse" id="Submit" value="" /></td>
  </tr>
  <tr class="tr1">
    <td>张三李四</td>
    <td>男</td>
    <td>123456789012</td>
    <td>2008-08-08</td>
    <td>12346789＠123.com</td>
    <td>北京</td>
    <td>质量部</td>
    <td><a href="#"><img src="images/but_edit.gif" border="0" /></a><a href="#"><img src="images/but_delete.gif" border="0" /></a><a href="#"><img src="images/but_browse.gif" border="0" /></a></td>
  </tr>
  <tr class="tr1">
    <td>张三李四</td>
    <td>男</td>
    <td>123456789012</td>
    <td>2008-08-08</td>
    <td>12346789＠123.com</td>
    <td>北京</td>
    <td>质量部</td>
    <td>&nbsp;</td>
  </tr>
  <tr class="tr2">
    <td>张三李四</td>
    <td>男</td>
    <td>123456789012</td>
    <td>2008-08-08</td>
    <td>12346789＠123.com</td>
    <td>北京</td>
    <td>质量部</td>
    <td>&nbsp;</td>
  </tr>
  <tr class="tr1">
    <td>张三李四</td>
    <td>男</td>
    <td>123456789012</td>
    <td>2008-08-08</td>
    <td>12346789＠123.com</td>
    <td>北京</td>
    <td>质量部</td>
    <td>&nbsp;</td>
  </tr>
  <tr class="tr1">
    <td>张三李四</td>
    <td>男</td>
    <td>123456789012</td>
    <td>2008-08-08</td>
    <td>12346789＠123.com</td>
    <td>北京</td>
    <td>质量部</td>
    <td>&nbsp;</td>
  </tr>
</table>
<table border="0" class="pagetable">
  <tr>
    <td align="right">结果页码:   1　2　3　4　5　上一页　下一页    　共 10 页   　共18 条记录    　翻到 　
      <input name="textfield" type="text" class="true_input" size="5" />
    第页 </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
