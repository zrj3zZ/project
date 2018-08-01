<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html >
<head>
    <meta charset="UTF-8">
    <title>提示</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
    <link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
    <script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>

    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/webuploader.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  charset="utf-8"  > </script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
    <script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
</head>
<body>
<script type="text/javascript">
    function uploadifyDialog(){
        var XMQY_ID = window.parent.$("#XMQY_ID").val();
        if(XMQY_ID==undefined || XMQY_ID =="" ){
            alert("目录为空:请选择目录");
            return;
        }
        var DAJYLCB_ID=window.parent.$("#dgcdID").val();
        if(DAJYLCB_ID==undefined || DAJYLCB_ID==""){
            alert("项目编号为空:无法上传");
            return;
        }
        if(DAJYLCB_ID.concat(".")){
            var i = DAJYLCB_ID.indexOf(".");
            DAJYLCB_ID = DAJYLCB_ID.substr(0, i);
        }
        var url = 'UploadifyDGCDFile.action?XMQY_ID='+XMQY_ID+'&DAJYLCB_ID='+DAJYLCB_ID;
        art.dialog.open(url,{
            id:"DGCD",
            title: '上传附件',
            pading: 0,
            lock: true,
            width: 650,
            height: 500,
            close:function(){
                window.location.reload();
                this.focus();
            }
        });
        return ;
    }
</script>
<style>

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
</body>
        <div style="margin-left: 6px;padding-top:5px;display: inline-block">
            <div><button onclick="showUploadifyPageXGZL();return false;">附件上传</button></div>
        </div>
        <div style="display: inline-block">
            <div><button onclick="DownloadZip();return false;">下载选中内容</button></div>
        </div>
        <div style="display: inline-block">
            <div><button onclick="DeleteFile();return false;">删除选中内容</button></div>
        </div>
<div style="padding:5px;text-align:center;">
    <table id='iform_grid' width="100%" style="border:1px solid #efefef" >
        <hidden id="XMQY_ID" value="<s:property value='XMQY_ID'/>"></hidden>
        <tr class="header" >
            <td style="width:5%;" class="header"><input type="checkbox"  name="colname" id="chkAll" /></td>
            <td style="text-align: left;width:5%;">序号</td>
            <td style="text-align: left;width:45%;">文件名称</td>
            <td style="text-align: left;width:15%;">审核状态</td>
            <td style="text-align: left;width:15%;">更新人</td>
            <td style="text-align: left;width:15%;">更新时间</td>
            <td style="text-align: left;width:15%;">更新操作</td>
        </tr>
        <s:iterator value="DGCDFileList" status="status">
            <tr class="cell">
                <td style="width:05%;"><input type="checkbox" id="" name="colname" value="<s:property value='departmentname'/>" /></td>
                <td style="cursor:pointer;">
                    <s:property value="%{#status.index+1}"/>
                </td>
                <s:if test="FILE_SRC_NAME.length()<=15">
                    <td><s:property value="FILE_SRC_NAME" /></td>
                </s:if>
                <s:if test="FILE_SRC_NAME.length()>15">
                    <td><s:property value="FILE_SRC_NAME.substring(0,20)+'...'" /></td>
                </s:if>
                <td>
                    <s:if test="SPZT == 'SPTG'.toString() ">
                        审核通过
                    </s:if>
                    <s:if test="SPZT == 'SPZ'.toString() ">
                        审核中
                    </s:if>
                    <s:if test="SPZT != 'SPTG'.toString() && SPZT != 'SPZ'.toString() ">
                        未审核
                    </s:if>
                </td>
                <td><s:property value="USERNAME" /></td>
                <td>
                    <s:property value="cjsj" />
                </td>
                <td>

                    <a href="http://localhost:8080/downloadFileDGCD.action?uuid=<s:property value='departmentname'/>"
                       style="color:blue;"><u>下载</u></a>
                    <s:if test="SaveOrUpdate=='update'.toString()">
                        <a href="javascript:DeleteOrDownload('<s:property value='departmentname'/>','delete')"
                           style="color:red;"><u>删除</u></a>
                    </s:if>
                </td>
            </tr>
        </s:iterator>
    </table>
    <script>
        function showUploadifyPageXGZL(){
            uploadifyDialog();
        }
        //复选/全选
        $("#chkAll").bind("click", function () {
            $("[name =colname]:checkbox").attr("checked", this.checked);
        });
        //下载打包文件
        function  DownloadZip(){
            debugger;
            var XMQY_ID = window.parent.$("#XMQY_ID").val();
            if(XMQY_ID==undefined || XMQY_ID =="" ){
                alert("目录为空:请选择目录");
                return;
            }
            var str;
            var TVGoods=$("input[name='colname']:checkbox:checked").each(function(){
                str+=$(this).val()+","
            })
            str=str.substr(0,str.length-1);
            window.location.href="http://localhost:8080/downloadFileDGCD.action?uuid="+str+"&XMQY_ID="+XMQY_ID;
        }
        //删除选中文件
        function  DeleteFile(){
            debugger;
            var str;
            var TVGoods=$("input[name='colname']:checkbox:checked").each(function(){
                str+=$(this).val()+","
            })
            str=str.substr(0,str.length-1);
            var actionUrl="sx_deleteDGCDFile.action";
            $.ajax({
                url:actionUrl,
                type:"post",
                async: false,
                data:{"uuid":str},
                success:function(data){
                    var parse = JSON.parse(data);
                    if(parse.success==undefined){
                        alert(parse.fial);
                    }else{
                        alert(parse.success+","+parse.fail);
                    }
                    window.location.reload();
                },
                error:function(data){
                    var parse = JSON.parse(data);
                    alert(parse.fial);
                    window.location.reload();
                }
            });
        }
        function  DeleteOrDownload(uuid,flag) {
            var actionUrl;
            if(flag==="delete"){
                if(!confirm("确认删除")){
                    return false;
                }
                actionUrl="sx_deleteDGCDFile.action"
            }else {
                actionUrl="sx_downloadDGCDFile.action"
            }
            $.ajax({
                url:actionUrl,
                type:"post",
                async: false,
                data:{"uuid":uuid},
                success:function(data){
                    alert("运行正确");
                    window.location.reload();
                },
                error:function(e){
                    alert("运行错误");
                    window.location.reload();
                }
            });
        }
        <!--这个是刷新rightiframe页面-->
        function  selectFile(XMQY_ID) {
            var SaveOrUpdate = window.parent.$("#SaveOrUpdate").val();
            if(SaveOrUpdate==undefined || SaveOrUpdate =="" ){
                alert("获取权限失败");
                return;
            }
            var XMQY_ID = window.parent.$("#XMQY_ID").val();
            if(XMQY_ID==undefined || XMQY_ID =="" ){
                alert("目录为空:请选择目录");
                return;
            }
            var DAJYLCB_ID=window.parent.$("#dgcdID").val();
            if(DAJYLCB_ID==undefined){
                alert("项目编号为空:获取失败");
                return;
            }
            window.parent.$("#rightIframe").attr("src","http://localhost:8080/sx_dgcdIframe.action?XMQY_ID="+XMQY_ID+"&DAJYLCB_ID="+DAJYLCB_ID+"&SaveOrUpdate="+SaveOrUpdate);
        }
    </script>
</div>
</html>