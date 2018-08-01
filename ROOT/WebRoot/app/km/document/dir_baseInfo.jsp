<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>文件夹基本信息</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/km/km_baseinfo.css">
      <style type="text/css">
    	.td_title {
				color:#000;
				letter-spacing: 0.1em;
				padding-right:10px;
				white-space:nowrap;
				vertical-align:middle;
				font-family:黑体;
				font-size:14px;
				text-align:right;
				width:100px;
				line-height:20px;
	}
.td_data{
			color:#0000FF; 
			text-align:left;
			padding-left:3px;
			font-size: 12px;
			vertical-align:middle;
			word-wrap:break-word;
			word-break:break-all;
			font-weight:500;
			line-height:20px;
			padding-top:5px;
			font-family:"宋体";
			border-bottom:1px solid #efefef;
		}
    </style>
  </head>
  
  <body class="easyui-layout">
  <div region="south"  >
	 <s:property value="menuBtn" escapeHtml="false"/>
  </div>
  <div region="center"  style="padding-left:20px;"> 
           <table border="0" cellspacing="0" cellpadding="0" width="80%">
             <tr> 
                 <td class="td_title">文件夹名称:</td><td class="td_data"><s:property value='model.directoryname'/></td>
             </tr>
             <tr>
                 <td class="td_title">路径:</td><td class="td_data"><s:property value='model.address'/></td>
             </tr>
             <tr>
                 <td class="td_title">文件个数:</td><td class="td_data"><s:property value='model.fileCount'/>个</td>
             </tr>
             <tr>
                 <td class="td_title">管理员:</td><td class="td_data"><s:property value='model.createUserName'/></td>
             </tr>
             <tr>
                 <td class="td_title">文件夹描述:</td><td class="td_data"><s:property value='model.memo'/></td>
             </tr>
       </table> 
          <s:hidden name="model.id"></s:hidden>
          <s:hidden name="model.parentid"></s:hidden>
          </div>
  </body>
</html>
