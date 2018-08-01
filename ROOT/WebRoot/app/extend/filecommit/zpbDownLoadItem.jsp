<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>持续督导分派</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
    <script language="javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
    <script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
    <link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
    <script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
    <script type="text/javascript" src="iwork_js/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
        var mainFormValidator;
        $().ready(function() {
            mainFormValidator =  $("#ifrmMain").validate({
            });
        });
        $(function() {
            $('#pp').pagination({
                total : <s:property value="totalNum"/>,
                pageNumber : <s:property value="pageNumber"/>,
                pageSize : <s:property value="pageSize"/>,
                onSelectPage : function(pageNumber, pageSize) {
                    submitMsg(pageNumber, pageSize);
                }
            });
        });
        function submitMsg(pageNumber, pageSize) {
            $("#pageNumber").val(pageNumber);
            $("#pageSize").val(pageSize);
            $("#frmMain").submit();
            return;
        }
        function isRealNum(val){
            // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
            if(val === "" || val ==null){
                return false;
            }
            if(!isNaN(val)){
                return true;
            }else{
                return false;
            }
        }
        $(function() {
            $("#chkAll").bind("click", function() {
                $("[name =colname]:checkbox").attr("checked", this.checked);
            });

        });

        function remove() {
            $.messager.confirm('确认','确认删除?',function(result) {
                if (result) {
                    var name="";
                    var namesss="";
                    obj = document.getElementsByName("colname");
                    for(k in obj){
                        if(obj[k].checked){
                            if(obj[k].value!='sb'){
                                if(name==""){
                                    name=obj[k].value;
                                    namesss=document.getElementById("id"+obj[k].value).value;
                                }else{
                                    name=name+","+obj[k].value;
                                    namesss=namesss+","+document.getElementById("id"+obj[k].value).value;
                                }

                            }

                        }

                    }

                    if(name!=""){
                        var downUrl = encodeURI("delfj.action?chkvalue="+name+"&customername="+namesss);
                        $.post(downUrl,function(data){
                            if(data==0){
                                alert("删除成功！");
                                window.location.reload();
                            }
                        });
                    }
                }
            });
        }
function dowmwj(wjm){
    var downUrl = encodeURI("zqb_filedowm.action?filename="+wjm);
    window.location.href = downUrl;
}
    </script>
    <style type="text/css">
        .searchtitle {
            text-align: right;
            padding: 5px;
        }

        .ui-jqgrid tr.jqgrow td {
            white-space: normal !important;
            height: 28px;
            font-size: 12px;
            vertical-align: text-middle;
            padding-top: 2px;
        }

        .cell td{
            margin:0;
            padding:3px 4px;
            height:25px;
            font-size:12px;
            white-space:nowrap;
            word-wrap:normal;
            overflow:hidden;
            text-align:left;
            border-bottom:1px dotted #eee;
            border-top:1px dotted #fff;
            border-right:1px dotted #eee;
        }
        .cell:hover{
            background-color:#F0F0F0;
        }
        .header td{
            height:35px;
            font-size:12px;
            padding:3px;
            white-space:nowrap;
            padding-left:5px;
            background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
            border-top:1px dotted #ccc;
            border-right:1px solid #eee;
        }
    </style>
</head>
<body class="easyui-layout">
<div region="north" border="false">
    <div class="tools_nav">

        <a href="javascript:window.location.reload();"
           class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
        <a href="javascript:remove();" class="easyui-linkbutton"
           plain="true" iconCls="icon-remove">删除</a>

    </div>
</div>
<div  region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">

    <div style="padding:5px">
        <table id='iform_grid' width="100%" style="border:1px solid #efefef">
            <tr class="header">
                <td style="width:5%;"><input
                        type="checkbox" name="colname" id="chkAll"  value="sb"/></td>
                <td style="width:20%;cursor:pointer;" >公司名称<span id="j"></span></td>
                <td style="width:20%;">查询条件</td>
                <td style="width:10%;">压缩人</td>
                <td style="width:10%;">压缩时间</td>
                <td style="width:35%;">压缩包名称</td>
            </tr>
            <s:iterator value="downlist" status="status">
                <tr class="cell">
                    <td><input type="checkbox" id="<s:property value='id'/>" name="colname" value="<s:property value='id'/>" /></td>
                    <td><s:property value="xsgsmc" /></td>
                    <td><s:property value="cxtj" /></td>
                    <td><s:property value="dbrname" /></td>
                    <td><s:property value="dbsj" />
                        <input type="hidden" id="id<s:property value='id'/>" value="<s:property value="ysbmc" />">
                    </td>
                    <td><a href="#" onclick="dowmwj('<s:property value="ysbmc" />')" style="color: blue"><s:property value="ysbmc" /></a></td>
                </tr>
            </s:iterator>
        </table>


    </div>
</div>
<div region="south"
     style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-left:10px;"
     border="false">
    <div>
        <s:if test="totalNum==0">

        </s:if>
        <s:else>
            <div id="pp"
                 style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
        </s:else>
    </div>
    <script type="text/javascript">
        $(function(){
            $("#KHMC").val($("#khmc").val());
            $("#KHBH").val($("#khbh").val());
            $("#ZZCXDD").val($("#zcxxdd").val());
            $("#CXDDZY").val($("#cxddzy").val());
            $("#GPDM").val($("#zqdm").val());
        });
    </script>
</body>
</html>
<script language="JavaScript">
    jQuery.validator.addMethod("string", function(value, element) {
        var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
        var patrn=/[`~!#$%^&*+<>?"{};'[\]]/im;
        if(patrn.test(value)){
        }else{
            var flag = false;
            var tmp = value.toLowerCase();
            for(var i=0;i<sqlstr.length;i++){
                var str = sqlstr[i];
                if(tmp.indexOf(str)>-1){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                return "success";
            }
        }
    }, "包含非法字符!");
</script>