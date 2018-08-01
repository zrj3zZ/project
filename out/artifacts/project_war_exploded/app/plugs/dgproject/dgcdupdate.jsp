<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>底稿存档修改表单</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
    <link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/webuploader.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  charset="utf-8"  > </script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
    <link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
    <style>
        table{
            width:800px;
        }
        td{
            width:180px;
            padding-top: 20px;
        }
        label{
            width: 80px;
        }
        #All{
            margin-left: 299px;

        }
        #left {
            float: left;
            height: 700px;
            width: 500px;
            border: 1px solid #0f0f0f;
        }
        #right {
            float: left;
            height: 700px;
            width: 860px;
            border: 1px solid #0f0f0f;
        }

    </style>
</head>
<body>
<form action="sx_dgcdUpdateDGCD.action" id="dgcdup" method="post">
    <s:iterator value="DGCDList" status="status">
        <hidden name="ID" value="<s:property value='ID'/>"></hidden>
        <table style="margin-left: 30px;">
            <tr>
                <td class="searchdata">
                    <label for="DAMC" >项目名称:</label>
                    <input id="DAMC" class="{maxlength:128,required:false,string:true}" type="text" value="<s:property value='DAMC'/>" name="DAMC" style="width:100px">
                </td>
                <td class="searchdata">
                    <label for="DABH">项目编号:</label>
                    <input id="DABH" class="{maxlength:128,required:false,string:true}" type="text" value="<s:property value='DABH'/>" name="DABH" style="width:100px">
                </td>
                <td style="width: 220px">
                    <label for="COMPANYNAME">项目类型:</label>
                    <select   name='COMPANYNAME' id='COMPANYNAME' >
                        <option value="新三板业务---推荐挂牌" <s:if test="COMPANYNAME == '新三板业务---推荐挂牌'.toString() ">selected</s:if> >新三板业务---推荐挂牌</option>
                        <option value="新三板业务---定增" <s:if test="COMPANYNAME == '新三板业务---定增'.toString()">selected</s:if> >新三板业务---定增</option>
                        <option value="新三板业务---并购重组"  <s:if test="COMPANYNAME == '新三板业务---并购重组'.toString() ">selected</s:if>>新三板业务---并购重组</option>
                        <option value="新三板业务---其他"  <s:if test="COMPANYNAME == '新三板业务---其他'.toString() ">selected</s:if>>新三板业务---其他</option>
                        <option value="上市公司业务---首次公开发行股票"  <s:if test="COMPANYNAME == '上市公司业务---首次公开发行股票'.toString() ">selected</s:if>>上市公司业务---首次公开发行股票</option>
                        <option value="上市公司业务---再融资"  <s:if test="COMPANYNAME == '上市公司业务---再融资'.toString() ">selected</s:if>>上市公司业务---再融资</option>
                        <option value="上市公司业务---并购重组"  <s:if test="COMPANYNAME == '上市公司业务---并购重组'.toString() ">selected</s:if>>上市公司业务---并购重组</option>
                        <option value="上市公司业务---其他"  <s:if test="COMPANYNAME == '上市公司业务---其他'.toString() ">selected</s:if>>上市公司业务---其他</option>
                        <option value="债券业务---公司债"  <s:if test="COMPANYNAME == '债券业务---公司债'.toString() ">selected</s:if>>债券业务---公司债</option>
                        <option value="债券业务---企业债"  <s:if test="COMPANYNAME == '债券业务---企业债'.toString() ">selected</s:if>>债券业务---企业债</option>
                        <option value="债券业务---可交换债"  <s:if test="COMPANYNAME == '债券业务---可交换债'.toString() ">selected</s:if>>债券业务---可交换债</option>
                        <option value="债券业务---其他"  <s:if test="COMPANYNAME == '债券业务---其他'.toString() ">selected</s:if>>债券业务---其他</option>
                    </select>
                </td>
            </tr>
            <tr style="">
                <td class="searchdata">
                    <label for="JYSY">锁定人账号</label>
                    <input id="JYSY" class="{maxlength:128,required:false,string:true}" type="text" value="<s:property value='JYSY'/>" name="JYSY" style="width:100px">
                </td>
                <td class="searchdata">
                    <label for="JCSJ">锁定时间</label>
                    <input id="JCSJ" class="{maxlength:128,required:false,string:true}" type="text" value="<s:property value='JCSJ'/>" name="JCSJ" style="width:100px">
                </td>
                <td>
                    <label for="JYXS">是否锁定</label>
                    <select  style="width:200px" name='JYXS' id='JYXS' >
                        <option value="已锁定" <s:if test="JYXS == 'Y'.toString() ">selected</s:if> >已锁定</option>
                        <option value="未锁定" <s:if test="JYXS == 'N'">selected</s:if> >未锁定</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="searchdata">
                    <label for="GHSJ">填报时间</label>
                    <input id="GHSJ" class="{maxlength:128,required:false,string:true}" type="text" value="<s:property value='GHSJ'/>" name="GHSJ" style="width:100px">
                </td>
                <td>
                    <label for="JYRMC">填报人姓名</label>
                    <input id="JYRMC" class="{maxlength:128,required:false,string:true}" type="text" value="<s:property value='JYRMC'/>" name="JYRMC" style="width:100px">
                </td>
            </tr>
            <tr >
                <a onclick="save()" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
            </tr>
        </table>
    </s:iterator>
</form>
<script>
    function save(){
        $("#dgcdup").submit();
        //window.close();
    }
</script>
</body>
</html>