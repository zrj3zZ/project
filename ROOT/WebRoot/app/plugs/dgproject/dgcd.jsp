<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <title>IWORK综合应用管理系统</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/public.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
    <link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
    <script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
    <script type="text/javascript" src="iwork_js/bindclick.js"></script>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
        function submitMsg(pageNumber, pageSize) {
            $("#pageNumber").val(pageNumber);
            $("#pageSize").val(pageSize);
            $("#frmMain").submit();
            return;
        }
        $(function () {
            //分页
            $('#pp').pagination({
                total:<s:property value="totalNum"/>,
                pageNumber:<s:property value="pageNumber"/>,
                pageSize:<s:property value="pageSize"/>,
                onSelectPage: function (pageNumber, pageSize) {
                    submitMsg(pageNumber, pageSize);
                }
            });
            //查询
            $("#search").click(function () {
                var DAMC = $("#DAMC").val();
                var type = $("#TYPE").val();
                var pageNumber = $("#pageNumber").val();
                var pageSize = $("#pageSize").val();
                var seachUrl = encodeURI("sx_dgcd.action?DAMC=" + DAMC + "&type=" + type + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize);
                window.location.href = seachUrl;
            });
            //复选/全选
            $("#chkAll").bind("click", function () {
                $("[name =colname]:checkbox").attr("checked", this.checked);
            });
        });
        //改变项目类型或者项目名称重置本页变为1
        function searchSize(){
            $("#pageNumber").val(1);
        }
        function addItem() {
            var pageUrl = "sx_dgcdAdd.action?SaveOrUpdate=save";
            var target = "_blank";
            var win_width = window.screen.width;
            page = window.open(pageUrl, target, 'width=' + win_width + ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
            page.location = pageUrl;
        }
        function  skipUpdate(ID,COMPANYNAME){
            page.close();
            updateProject(ID,COMPANYNAME);
            //page.location = url;
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

        .header td {
            height: 35px;
            font-size: 12px;
            padding: 3px;
            white-space: nowrap;
            padding-left: 5px;
            background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
            border-top: 1px dotted #ccc;
            border-right: 1px solid #eee;
        }

        .cell td {
            margin: 0;
            padding: 3px 4px;
            height: 25px;
            font-size: 12px;
            white-space: nowrap;
            word-wrap: normal;
            overflow: hidden;
            text-align: left;
            border-bottom: 1px dotted #eee;
            border-top: 1px dotted #fff;
            border-right: 1px dotted #eee;
        }

        .cell:hover {
            background-color: #F0F0F0;
        }
    </style>
</head>
<body class="easyui-layout">
<div region="north" border="false">
    <div class="tools_nav">
        <a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
        <a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
    </div>
</div>
<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
    <s:if test="orgroleid != 3">
        <form name='ifrmMain' id='ifrmMain' method="post">
            <div style="padding:5px;text-align:center;">
                <div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
                    <table width='100%' border='0' cellpadding='0' cellspacing='0'>
                        <tr>
                            <td style='padding-top:10px;padding-bottom:10px;'>
                                <table width='100%' border='0' cellpadding='0' cellspacing='0'>
                                    <tr>
                                        <td class="searchtitle">项目类型</td>
                                        <td class="searchdata">
                                            <select style="width:200px" name='TYPE' id='TYPE' onchange="searchSize()">
                                                <option value="">所有的类型</option>
                                                <option value="新三板业务---推荐挂牌"
                                                        <s:if test="type == '新三板业务---推荐挂牌'.toString() ">selected</s:if> >
                                                    新三板业务---推荐挂牌
                                                </option>
                                                <option value="新三板业务---定增"
                                                        <s:if test="type == '新三板业务---定增'">selected</s:if> >新三板业务---定增
                                                </option>
                                                <option value="新三板业务---并购重组"
                                                        <s:if test="type == '新三板业务---并购重组'.toString() ">selected</s:if>>
                                                    新三板业务---并购重组
                                                </option>
                                                <option value="新三板业务---其他"
                                                        <s:if test="type == '新三板业务---其他'.toString() ">selected</s:if>>
                                                    新三板业务---其他
                                                </option>
                                                <option value="上市公司业务---首次公开发行股票"
                                                        <s:if test="type == '上市公司业务---首次公开发行股票'.toString() ">selected</s:if>>
                                                    上市公司业务---首次公开发行股票
                                                </option>
                                                <option value="上市公司业务---再融资"
                                                        <s:if test="type == '上市公司业务---再融资'.toString() ">selected</s:if>>
                                                    上市公司业务---再融资
                                                </option>
                                                <option value="上市公司业务---并购重组"
                                                        <s:if test="type == '上市公司业务---并购重组'.toString() ">selected</s:if>>
                                                    上市公司业务---并购重组
                                                </option>
                                                <option value="上市公司业务---其他"
                                                        <s:if test="type == '上市公司业务---其他'.toString() ">selected</s:if>>
                                                    上市公司业务---其他
                                                </option>
                                                <option value="债券业务---公司债"
                                                        <s:if test="type == '债券业务---公司债'.toString() ">selected</s:if>>
                                                    债券业务---公司债
                                                </option>
                                                <option value="债券业务---企业债"
                                                        <s:if test="type == '债券业务---企业债'.toString() ">selected</s:if>>
                                                    债券业务---企业债
                                                </option>
                                                <option value="债券业务---可交换债"
                                                        <s:if test="type == '债券业务---可交换债'.toString() ">selected</s:if>>
                                                    债券业务---可交换债
                                                </option>
                                                <option value="债券业务---其他"
                                                        <s:if test="type == '债券业务---其他'.toString() ">selected</s:if>>
                                                    债券业务---其他
                                                </option>
                                            </select>
                                        </td>
                                        <td class="searchtitle">项目名称</td>
                                        <td class="searchdata"><input type='text' onchange="searchSize()"
                                                                      class='{maxlength:128,required:false,string:true}'
                                                                      style="width:100px"
                                                                      name='DAMC' id='DAMC'
                                                                      value="<s:property value='DAMC'/>"></td>
                                    </tr>
                                </table>
                            <td>
                            <td valign='bottom' style='padding-bottom:5px;'><a
                                    id="search" class="easyui-linkbutton" icon="icon-search"
                                    href="javascript:void(0);">查询</a></td>
                        <tr>
                    </table>
                </div>
            </div>
            <span style="disabled:none">
			</span>

        </form>
    </s:if>
    <div style="padding:5px;text-align:center;">

        <table id='iform_grid' width="99%" style="border:1px solid #efefef">
            <tr class="header">
                <!--<td style="width:5%;" class="header"><input type="checkbox" name="colname" id="chkAll" /></td>-->
                <td style="text-align: left;">项目类型</td>
                <td style="text-align: left;">项目名称</td>
                <td style="text-align: left;">状态</td>
                <td style="text-align: left;">操作</td>
            </tr>
            <s:iterator value="DGCDList" status="status">
                <tr class="cell">
                    <!--<td style="width:05%;"><input type="checkbox" id="" name="colname" value="" /></td>-->
                    <td>
                        <s:property value="COMPANYNAME"/>
                    </td>
                    <td style="cursor:pointer;">
                        <s:if test="JYXS !=  'Y'.toString() ">
                            <a href="javascript:seeProject('<s:property value="ID"/>','<s:property value="COMPANYNAME"/>','N')"
                               style="color:blue;"><s:property value="DAMC"/></a>
                        </s:if>
                        <s:if test="JYXS == 'Y'.toString() ">
                            <a href="javascript:seeProject('<s:property value="ID"/>','<s:property value="COMPANYNAME"/>','Y')"
                               style="color:blue;"><s:property value="DAMC"/></a>
                        </s:if>
                    </td>
                    <td>
                        <s:if test="JYXS=='Y'.toString() "><label style="color: red">已锁定</label></s:if>
                        <s:if test="JYXS != 'Y'.toString()">
                            未锁定
                        </s:if>
                        &nbsp;&nbsp;&nbsp;
                        <a href="javascript:Delete('sx_deleteDGCD.action','<s:property value="ID"/>')"
                           style="color:blue;"><u>共有文件:<s:property value="sum"/></u></a>&nbsp;&nbsp;&nbsp;
                        <a href="javascript:Delete('sx_deleteDGCD.action','<s:property value="ID"/>')"
                           style="color:blue;"><u>未提交文件:<s:property value="WSH"/></u></a>&nbsp;&nbsp;&nbsp;
                        <a href="javascript:Delete('sx_deleteDGCD.action','<s:property value="ID"/>')"
                           style="color:blue;"><u>审批通过:<s:property value="SHTG"/></u></a>&nbsp;&nbsp;&nbsp;
                        <a href="javascript:Delete('sx_deleteDGCD.action','<s:property value="ID"/>')"
                           style="color:blue;"><u>审批中:<s:property value="SHZ"/></u></a>

                    </td>
                    <td>
                        <!--<s:if test="JYXS!= 'Y'.toString() "></s:if>:1-->
                            <a href="javascript:updateProject('<s:property value="ID"/>','<s:property value="COMPANYNAME"/>')"
                               style="color:blue;"><u>修改项目</u></a>
                            <!--<a href="javascript:downloadDGCDFileForDGCDID('downloadDGCDFileForDGCDID.action','<s:property value="ID"/>')"
                               style="color:blue;"><u>下载</u></a>-->
                            <a href="javascript:Delete('sx_deleteDGCD.action','<s:property value="ID"/>')"
                               style="color:blue;"><u>删除</u></a>
                            <s:if test="lockDGCD == 'YES'.toString() ">
                                <s:if test="JYXS !=  'Y'.toString() ">
                                    <a href="javascript:LockDGCD('sx_lockDGCD.action','<s:property value="ID"/>','<s:property value="JYXS"/>','<s:property value="COMPANYNAME"/>')"
                                       style="color:red;"><u>锁定目录</u>
                                    </a>
                                </s:if>
                            </s:if>
                        <!--1-->
                        <!--<s:if test="JYXS== 'Y'.toString() ">-->
                            <!--<label style="color: red">已锁定</label>-->
                            <s:if test="lockDGCD ==  'YES'.toString() ">
                                <a href="javascript:LockDGCD('sx_lockDGCD.action','<s:property value="ID"/>' , '<s:property value="JYXS"/>','<s:property value="COMPANYNAME"/>')"
                                   style="color:blue;"><u>解锁目录</u>
                                </a>
                            </s:if>
                        <!--</s:if>-->
                    </td>
                </tr>
            </s:iterator>
        </table>
        <form action="sx_dgcd.action" method=post name=frmMain
              id=frmMain>
            <s:hidden name="pageNumber" id="pageNumber"></s:hidden>
            <s:hidden name="pageSize" id="pageSize"></s:hidden>
            <s:hidden name="DAMC"></s:hidden>
            <s:hidden name="type"></s:hidden>
        </form>
        <div id='prowed_ifrom_grid'></div>
    </div>
</div>
<div region="south"
     style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
     border="false">
    <div style="padding:5px">
        <div id="pp"
             style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
    </div>
</div>
</body>
</html>
<script>
    function updateProject(ID,COMPANYNAME) {
        if(ID.indexOf(".")!=-1){
            var i = ID.indexOf(".");
            var pageUrl = "go_DGCDUpdate.action?dgcdID=" + ID.substr(0, i) + "&SaveOrUpdate=update&COMPANYNAME="+COMPANYNAME;
        }else {
            var pageUrl = "go_DGCDUpdate.action?dgcdID=" + ID+ "&SaveOrUpdate=update&COMPANYNAME="+COMPANYNAME;
        }
        var target = "_blank";
        var win_width = window.screen.width;
        var ipage = window.open(pageUrl, target, 'width=' + win_width + ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
        ipage.location = pageUrl;
    }
    function seeProject(ID,COMPANYNAME,lock) {
        var i = ID.indexOf(".");
        var pageUrl = "go_DGCDUpdate.action?dgcdID=" + ID.substr(0, i) + "&SaveOrUpdate=see&COMPANYNAME="+COMPANYNAME+"&JYXS="+lock;
        var target = "_blank";
        var win_width = window.screen.width;
        var page = window.open(pageUrl, target, 'width=' + win_width + ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
        page.location = pageUrl;
    }

    function Delete(action, ID) {
        if(!confirm("确认删除")){
            return;
        }
        ID = ID.substr(0, ID.indexOf("."));
        $.ajax({
            url: action,
            type: "post",
            data: {"dgcdID": ID},
            success: function (data) {
                alert("运行正确");
                window.location.reload();
            },
            error: function (e) {
                alert("运行错误");
                window.location.reload();
            }
        });
    }

    function downloadDGCDFileForDGCDID(action, ID) {
        if(ID.concat("."))ID = ID.substr(0, ID.indexOf("."));
        window.location.href="http://localhost:8080/"+action+"?dgcdID="+ID;
        // $.ajax({
        //     url: action,
        //     type: "post",
        //     data: {"dgcdID": ID},
        //     success: function (data) {
        //     },
        //     error: function (e) {
        //         alert("下载文件错误");
        //     }
        // });
    }
    function  LockDGCD(action,ID,JYXS,COMPANYNAME) {
        debugger;
        //Y代表锁定
        //N代表未锁定
        ID=ID.substr(0,ID.indexOf("."));
        if(JYXS!="Y"){
            JYXS="Y";
            if(!confirm("确认锁定")){
                return;
            }
        }else{
            JYXS="N";
            if(!confirm("确认解锁")){
                return;
            }
        }
        $.ajax({
            url:action,
            type:"post",
            data:{"dgcdID":ID,"JYXS":JYXS,"COMPANYNAME":COMPANYNAME},
            success:function(data){
                window.location.reload();
            },
            error:function(e){
                alert("解锁错误");
                window.location.reload();
            }
        });
    }
</script>
