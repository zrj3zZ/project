<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>文档基本信息</title>
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
			padding-top:5px;
			font-family:"宋体";
			border-bottom:1px solid #efefef;
			line-height:20px;
		}
    </style>
  </head>
  
  <body class="easyui-layout">
  <div region="south"  >
	  <div class="tools_nav">
	  	<s:property value="doc_menubtn" escapeHtml="false"/>
	  </div>
  </div>
  <div region="center"  style="padding-left:20px;"> 
         <table border="0" cellspacing="0" cellpadding="0" width="80%">
             <tr>
                 <td class="td_title">文件名称:</td><td class="td_data"><s:property value='model.docName'/></td>
             </tr>
             <tr>
                 <td class="td_title">路径:</td><td class="td_data"><s:property value='model.address'/></td>
             </tr>
             <tr>
                 <td class="td_title">关键词:</td><td class="td_data"><s:property value='model.keyword'/></td>
             </tr>
             <tr>
                 <td class="td_title">有效期</td><td class="td_data"><s:property value='model.startDate'/>&nbsp;到&nbsp;<s:property value='model.endDate'/></td>
             </tr>
             <tr>
                 <td class="td_title">发布人:</td><td class="td_data"><s:property value='model.createUserName'/></td>
             </tr>
             <tr>
                 <td class="td_title">版本:</td><td class="td_data">V<s:property value='model.version'/>.0</td>
             </tr>
             <tr>
                 <td class="td_title">文档描述:</td><td class="td_data"><s:property value='model.memo'/></td>
             </tr>
       </table> 
          <s:hidden name="model.id" id="proprety"></s:hidden>
          <s:hidden name="model.directoryid"></s:hidden>
          </div>
  </body>
</html>