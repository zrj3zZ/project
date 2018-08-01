<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>视频管理维护</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/plugs/video/video_edit.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					add(); return false;
				}
		   else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
					doSubmit(); return false;
				}		
}); //快捷键
	</script>
</head>
<body class="easyui-layout">
<div region="north" border="false" style="padding:0px;overflow:no">
  <div style="padding:2px;background:#efefef;border-bottom:1px solid #efefef"> <a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a> <a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a> </div>
</div>
<div region="center" style="padding:3px;border:0px">
  <table id="videogrid">
  </table>
</div>
<!-- 编辑窗口 -->
<div id="idialog" icon="icon-ok"  style="width:350px;height:200px;padding-top:10px;background: #fafafa;">
  <form id="iform" method="post">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td align='right'><label for="title">视频名称：</label></td>
        <td><input class="easyui-validatebox" required="true" validType="maxLength[64]" type="text" id="title" name="title">
          </input>
          &nbsp;<span style='color:red'>*</span></td>
      </tr>
      <tr>
        <td align='right'><label for="videofile">播放视频文件名：</label></td>
        <td><input class="easyui-validatebox" required="true" validType="maxLength[256]" type="text" id="videofile" name="videofile">
          </input>
          &nbsp;<span style='color:red'>*</span></td>
      </tr>
      <tr>
        <td align='right'><label for="picfile">预览图片文件名：</label></td>
        <td><input class="easyui-validatebox" required="true" validType="maxLength[256]" type="text" id="picfile" name="picfile">
          </input>
          &nbsp;<span style='color:red'>*</span></td>
      </tr>
      <tr>
        <td align='right'><label for="description">视频描述：</label></td>
        <td><input class="easyui-validatebox" validType="maxLength[100]" type="text" id="description" name="description">
          </input></td>
      </tr>
    </table>
    <input type='hidden' id='type' name='type'>
    <input type='hidden' id='id' name='id'>
    <input type='hidden' id='uploader' name='uploader'>
    <input type='hidden' id='uploadtime' name='uploadtime'>
  </form>
</div>
</body>
</html>