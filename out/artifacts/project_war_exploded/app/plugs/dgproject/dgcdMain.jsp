<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <s:if test="SaveOrUpdate=='save'.toString()"><title>底稿存档添加表单</title></s:if>
    <s:if test="SaveOrUpdate=='update'.toString()"><title>底稿存档修改表单</title></s:if>
    <s:if test="SaveOrUpdate=='see'.toString()"><title>底稿存档查看表单</title></s:if>
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/webuploader.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js" charset="utf-8"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js" charset="utf-8"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js" charset="utf-8"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js" charset="utf-8"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js" charset="utf-8"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript" src="iwork_js/engine/iformpage.js" charset="utf-8"></script>
    <link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
    <style>
    	.ztree li span.button.add {
			background-position:-144px 0; 
			vertical-align:top; 
			*vertical-align:middle;
			margin-right: 3px;
			margin-left: 3px;
		}
		.ztree li span.button.insert {
			background-position:-127px -48px; 
			vertical-align:top; 
			*vertical-align:middle;
			margin-right: 3px;
			margin-left: 3px;
		}
		
		.ztree li span.button.edit {
			margin-right: 3px;
			margin-left: 3px;
		}
		
		.ztree li span.button.remove {
			margin-right: 3px;
			margin-left: 3px;
		}
        #All {
            margin-left: 16%;
        }

        #left {
            height: 700px;
            width: 30%;
            border: 1px solid #0f0f0f;
            float:left;
            display:inline;
        }

        #right {
            height: 700px;
            width: 41%;
            border: 1px solid #0f0f0f;
            float:left;
            display:inline;
        }
        .top {
            margin-left: 16%;
            height: 60px;
            width: 71%;
            float:left;
            display:inline;
        }

        .none_i {
            display: none;
        }
        .choisePro{
            display:none;
        }

    </style>
    <script type="text/javascript">
        function aa(){
            debugger;
            var url = "dg_dgcdZreeExcelImp.action?COMPANYNAME=<s:property value='COMPANYNAME'/>";
            debugger;
            art.dialog.open(url,{
                id:"DGCD",
                title: '上传附件',
                pading: 0,
                lock: true,
                width: 650,
                height: 500,
                close:function(){
                    this.focus();
                }
            });
            return ;
        }
    </script>
    <script>
        //select的onchange事件
        function SelectionType() {
                var choise = $("#choise").val();
                switch (choise) {
                    case "新三板业务---推荐挂牌":
                        showPro(1);
                        break;
                    case "新三板业务---定增":
                        showPro(2);
                        break;
                    case "新三板业务---并购重组":
                        showPro(3);
                        break;
                    case "新三板业务---其他":
                        showPro(4);
                        break;
                    case "上市公司业务---首次公开发行股票":
                        showPro(5);
                        break;
                    case "上市公司业务---再融资":
                        showPro(6);
                        break;
                    case "上市公司业务---并购重组":
                        showPro(7);
                        break;
                    case "上市公司业务---其他":
                        showPro(8);
                        break;
                    case "债券业务---公司债":
                        showPro(9);
                        break;
                    case "债券业务---企业债":
                        showPro(10);
                        break;
                    case "债券业务---可交换债":
                        showPro(11);
                        break;
                    case "债券业务---其他":
                        showPro(12);
                        break;
                }
            }
        //作用:根据不同的项目类型显示不同的选择按钮
        function showPro(s) {
                console.log(s);
                $("#PROJECTNO").val("");
                $("#PROJECTNAME").val("");
                for (var i = 1; i <= 12; i++) {
                    if ($("#a" + i).hasClass("choisePro")) {
                        if (i === s) $("#a" + i).removeClass("choisePro");
                    } else {
                        if (i != s) $("#a" + i).addClass("choisePro")
                    }
                }
            }
        //作用添加加载界面的时候默认是1,select默认也是第一个
        $(document).ready(function(){
            showPro(1);
        })

        function openTzggcx(s){
            var pageUrl="sx_dgcdInsert_selectPro.action?pageSize=20&ProID="+s;
            art.dialog.open(pageUrl,{
                title:'通知人员选择页面',
                loadingText:'正在加载中,请稍后...',
                bgcolor:'#999',
                rang:true,
                width:1200,
                cache:false,
                lock: true,
                height:600,
                iconTitle:false,
                extendDrag:true,
                autoSize:false,
                close:function(){

                }
            });
        }
    </script>
</head>
<body style="width: 100%">
<!--三个if标签:根据传的SaveOrUpdate不同显示顶部的页面:1.添加;2,修改;3,查看-->
<s:if test="SaveOrUpdate=='save'.toString()">
    <form id="formDGCD">
        <input type="text" style="display:none" id="SaveOrUpdate" name="SaveOrUpdate" value="<s:property value='SaveOrUpdate'/>">
        <table style="margin-left: 30px;">
            <tr style="">
                <td style="padding-left: 270px;padding-top: 20px;">
                    <label for="COMPANYNAME">项目类型:</label>
                    <select name='COMPANYNAME' id="choise" onchange="SelectionType()" style="height: 30px;">
                        <option value="新三板业务---推荐挂牌" <s:if test="COMPANYNAME == '新三板业务---推荐挂牌'.toString() ">selected</s:if>>
                            新三板业务---推荐挂牌
                        </option>
                        <option value="新三板业务---定增" <s:if test="COMPANYNAME == '新三板业务---定增'.toString()">selected</s:if>>
                            新三板业务---定增
                        </option>
                        <option value="新三板业务---并购重组" <s:if test="COMPANYNAME == '新三板业务---并购重组'.toString() ">selected</s:if>>
                            新三板业务---并购重组
                        </option>
                        <option value="新三板业务---其他" <s:if test="COMPANYNAME == '新三板业务---其他'.toString() ">selected</s:if>>
                            新三板业务---其他
                        </option>
                        <option value="上市公司业务---首次公开发行股票" <s:if test="COMPANYNAME == '上市公司业务---首次公开发行股票'.toString() ">selected</s:if>>
                            上市公司业务---首次公开发行股票
                        </option>
                        <option value="上市公司业务---再融资" <s:if test="COMPANYNAME == '上市公司业务---再融资'.toString() ">selected</s:if>>
                            上市公司业务---再融资
                        </option>
                        <option value="上市公司业务---并购重组" <s:if test="COMPANYNAME == '上市公司业务---并购重组'.toString() ">selected</s:if>>
                            上市公司业务---并购重组
                        </option>
                        <option value="上市公司业务---其他" <s:if test="COMPANYNAME == '上市公司业务---其他'.toString() ">selected</s:if>>
                            上市公司业务---其他
                        </option>
                        <option value="债券业务---公司债" <s:if test="COMPANYNAME == '债券业务---公司债'.toString() ">selected</s:if>>
                            债券业务---公司债
                        </option>
                        <option value="债券业务---企业债" <s:if test="COMPANYNAME == '债券业务---企业债'.toString() ">selected</s:if>>
                            债券业务---企业债
                        </option>
                        <option value="债券业务---可交换债" <s:if test="COMPANYNAME == '债券业务---可交换债'.toString() ">selected</s:if>>
                            债券业务---可交换债
                        </option>
                        <option value="债券业务---其他" <s:if test="COMPANYNAME == '债券业务---其他'.toString() ">selected</s:if>>
                            债券业务---其他
                        </option>
                    </select>
                </td>
                <td class="searchdata" style="padding-top: 20px;">
                    <label for="DAMC">项目名称:</label>
                    <input id="PROJECTNAME" disabled type="text" value="<s:property value='DAMC'/>" name="PROJECTNAME" style="height:30px;width:240px">
                    <input type="text" disabled style="width:100px;display:none"   name="PROJECTNO"  id="PROJECTNO">
                </td>
                <td id="a1"  style="padding-top: 20px;">
                    <!--新三板---项目基本情况查询-->
                    <a href="javascript:openTzggcx(0);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td id="a2" style="padding-top: 20px;">
                    <!--新三板---定向增发项目-->
                    <a href="javascript:openTzggcx(1);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td id="a3" style="padding-top: 20px;">
                    <!--新三板--并购重组项目-->
                    <a href="javascript:openTzggcx(2);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td id="a4" style="padding-top: 20px;">
                    <!--新三板--其他-->
                    <a href="javascript:openTzggcx(3);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td id="a5" style="padding-top: 20px;">
                    <!--上市公司--首次公开发行-->
                    <a href="javascript:openTzggcx(4);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td id="a6" style="padding-top: 20px;">
                    <!--上市公司--再融资-->
                    <a href="javascript:openTzggcx(5);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td id="a7" style="padding-top: 20px;">
                    <!--上市公司--并购重组-->
                    <a href="javascript:openTzggcx(6);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td id="a8" style="padding-top: 20px;">
                    <!--上市公司--其他-->
                    <a href="javascript:openTzggcx(7);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td id="a9" style="padding-top: 20px;">
                    <!--债券业务--公司债-->
                    <a href="javascript:openTzggcx(8);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td id="a10" style="padding-top: 20px;">
                    <!--债券业务--企业债-->
                    <a href="javascript:openTzggcx(9);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td id="a11" style="padding-top: 20px;">
                    <!--债券业务全部--可交换债-->
                    <a href="javascript:openTzggcx(10);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td id="a12" style="padding-top: 20px;">
                    <!--债券业务--其他-->
                    <a href="javascript:openTzggcx(11);"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a>
                </td>
                <td style="padding-top: 20px;">
                    <button onclick="after('sx_dgcdInsert.action')" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</button>
                    <button onclick="pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</button></td>
                </td>

            </tr>
        </table>
    </form>
</s:if>
<s:if test="SaveOrUpdate=='update'.toString()">
    <form id="formDGCD">
        <s:iterator value="DGCDList" status="status">
            <!-- 这里hidden有错误 -->
            <!--dgcdID: 保存底稿存档项目的id ; SaveOrUpdate: 保存添加or修改or查看这三个的状态 ; DABH : 项目的编号 : 这里用于 点击目录来显示对应的文件列表-->
            <input type="text" id="dgcdID" style="display:none" name="dgcdID" value="<s:property value='ID'/>">
            <input type="text" style="display:none" id="SaveOrUpdate" name="SaveOrUpdate" value="<s:property value='SaveOrUpdate'/>">
            <input id="DABH" type="text" value="<s:property value='DABH'/>" name="DABH" style="width:100px;display:none">
            <table  class="top" style="">
                <tr class="header">
                    <td style="padding-top: 20px;">
                        <label for="COMPANYNAME">项目类型:</label>
                        <select name='COMPANYNAME' id='COMPANYNAME'>
                            <option value="新三板业务---推荐挂牌"
                                    <s:if test="COMPANYNAME == '新三板业务---推荐挂牌'.toString() ">selected</s:if> >新三板业务---推荐挂牌
                            </option>
                            <option value="新三板业务---定增"
                                    <s:if test="COMPANYNAME == '新三板业务---定增'.toString()">selected</s:if> >新三板业务---定增
                            </option>
                            <option value="新三板业务---并购重组"
                                    <s:if test="COMPANYNAME == '新三板业务---并购重组'.toString() ">selected</s:if>>新三板业务---并购重组
                            </option>
                            <option value="新三板业务---其他"
                                    <s:if test="COMPANYNAME == '新三板业务---其他'.toString() ">selected</s:if>>新三板业务---其他
                            </option>
                            <option value="上市公司业务---首次公开发行股票"
                                    <s:if test="COMPANYNAME == '上市公司业务---首次公开发行股票'.toString() ">selected</s:if>>
                                上市公司业务---首次公开发行股票
                            </option>
                            <option value="上市公司业务---再融资"
                                    <s:if test="COMPANYNAME == '上市公司业务---再融资'.toString() ">selected</s:if>>上市公司业务---再融资
                            </option>
                            <option value="上市公司业务---并购重组"
                                    <s:if test="COMPANYNAME == '上市公司业务---并购重组'.toString() ">selected</s:if>>
                                上市公司业务---并购重组
                            </option>
                            <option value="上市公司业务---其他"
                                    <s:if test="COMPANYNAME == '上市公司业务---其他'.toString() ">selected</s:if>>上市公司业务---其他
                            </option>
                            <option value="债券业务---公司债"
                                    <s:if test="COMPANYNAME == '债券业务---公司债'.toString() ">selected</s:if>>债券业务---公司债
                            </option>
                            <option value="债券业务---企业债"
                                    <s:if test="COMPANYNAME == '债券业务---企业债'.toString() ">selected</s:if>>债券业务---企业债
                            </option>
                            <option value="债券业务---可交换债"
                                    <s:if test="COMPANYNAME == '债券业务---可交换债'.toString() ">selected</s:if>>债券业务---可交换债
                            </option>
                            <option value="债券业务---其他"
                                    <s:if test="COMPANYNAME == '债券业务---其他'.toString() ">selected</s:if>>债券业务---其他
                            </option>
                        </select>
                    </td>
                    <td class="searchdata" style="padding-top: 20px;">
                        <label for="DAMC">项目名称:</label>
                        <input id="DAMC" disabled type="text" value="<s:property value='DAMC'/>" name="DAMC"  style="height:30px;width:240px">
                    </td>
                    <td class="searchdata" style="padding-left: 10px;padding-top: 20px;">
                        <label for="GHSJ">填报时间:</label>
                        <label style="width:81px;height: 30px" name="GHSJ" id="GHSJ" value="<s:property value='GHSJ'/>"></label>
                        <input type="text" onfocus="WdatePicker()" style="width:100px;display: none" name="GHSJ" id="GHSJ" value="<s:property value='GHSJ'/>">
                        <label style="width:81px;height: 30px" ><s:property value='GHSJ'/></label>
                        <label for="JYRMC">填报人姓名:</label>
                        <label style="width:48px;height: 30px"><s:property value='JYRMC'/></label>
                        <input id="JYRMC" type="text" value="<s:property value='JYRMC'/>" name="JYRMC" style="width:100px;display: none">
                    </td>
                    <td  style="padding-top:20px">
                        <button onclick="after('sx_dgcdUpdateDGCD.action')" class="easyui-linkbutton" iconCls="icon-save">
                            保存
                        </button>
                        <button onclick="pageClose();" class="easyui-linkbutton" plain="true"
                                iconCls="icon-cancel">关闭</button>
                        <a onclick="aa()"  class="easyui-linkbutton" >上传目录</a>
                    </td>
                </tr>
            </table>
        </s:iterator>
    </form>
</s:if>
<s:if test="SaveOrUpdate=='see'.toString()">
    <form id="formDGCD">
        <s:iterator value="DGCDList" status="status">
            <!-- 这里hidden有问题 -->
            <input type="text" id="dgcdID" style="display:none" name="dgcdID" value="<s:property value='ID'/>">
            <input type="text" style="display:none" id="SaveOrUpdate" name="SaveOrUpdate" value="<s:property value='SaveOrUpdate'/>">
            <!--项目编号-->
            <input id="DABH" type="text" value="<s:property value='DABH'/>" name="DABH" style="width:100px;display: none">
            <table class="top" >
                <tr>
                    <td style="padding-top: 20px;">
                        <label for="COMPANYNAME">项目类型:</label>
                        <select id='COMPANYNAME' disabled>
                            <option value="新三板业务---推荐挂牌"
                                    <s:if test="COMPANYNAME == '新三板业务---推荐挂牌'.toString() ">selected</s:if> >新三板业务---推荐挂牌
                            </option>
                            <option value="新三板业务---定增"
                                    <s:if test="COMPANYNAME == '新三板业务---定增'.toString()">selected</s:if> >新三板业务---定增
                            </option>`
                            <option value="新三板业务---并购重组"
                                    <s:if test="COMPANYNAME == '新三板业务---并购重组'.toString() ">selected</s:if>>新三板业务---并购重组
                            </option>
                            <option value="新三板业务---其他"
                                    <s:if test="COMPANYNAME == '新三板业务---其他'.toString() ">selected</s:if>>新三板业务---其他
                            </option>
                            <option value="上市公司业务---首次公开发行股票"
                                    <s:if test="COMPANYNAME == '上市公司业务---首次公开发行股票'.toString() ">selected</s:if>>
                                上市公司业务---首次公开发行股票
                            </option>
                            <option value="上市公司业务---再融资"
                                    <s:if test="COMPANYNAME == '上市公司业务---再融资'.toString() ">selected</s:if>>上市公司业务---再融资
                            </option>
                            <option value="上市公司业务---并购重组"
                                    <s:if test="COMPANYNAME == '上市公司业务---并购重组'.toString() ">selected</s:if>>
                                上市公司业务---并购重组
                            </option>
                            <option value="上市公司业务---其他"
                                    <s:if test="COMPANYNAME == '上市公司业务---其他'.toString() ">selected</s:if>>上市公司业务---其他
                            </option>
                            <option value="债券业务---公司债"
                                    <s:if test="COMPANYNAME == '债券业务---公司债'.toString() ">selected</s:if>>债券业务---公司债
                            </option>
                            <option value="债券业务---企业债"
                                    <s:if test="COMPANYNAME == '债券业务---企业债'.toString() ">selected</s:if>>债券业务---企业债
                            </option>
                            <option value="债券业务---可交换债"
                                    <s:if test="COMPANYNAME == '债券业务---可交换债'.toString() ">selected</s:if>>债券业务---可交换债
                            </option>
                            <option value="债券业务---其他"
                                    <s:if test="COMPANYNAME == '债券业务---其他'.toString() ">selected</s:if>>债券业务---其他
                            </option>
                        </select>
                    </td>
                    <td class="searchdata" style="padding-top: 20px;">
                        <label for="DAMC">项目名称:</label>
                        <input id="DAMC" disabled type="text" value="<s:property value='DAMC'/>" style="height:30px;width:240px">
                    </td>
                    <td class="searchdata" style="padding-left: 10px;padding-top: 20px;">
                        <label for="GHSJ">填报时间:</label>
                        <label style="width:81px;height: 30px" name="GHSJ" id="GHSJ" value="<s:property value='GHSJ'/>"></label>
                        <input type="text" onfocus="WdatePicker()" style="width:100px;display: none" name="GHSJ" id="GHSJ" value="<s:property value='GHSJ'/>">
                        <label style="width:81px;height: 30px" ><s:property value='GHSJ'/></label>
                        <label for="JYRMC">填报人姓名:</label>
                        <label style="width:48px;height: 30px"><s:property value='JYRMC'/></label>
                        <input id="JYRMC" type="text" value="<s:property value='JYRMC'/>" name="JYRMC" style="width:100px;display: none">
                    </td>
                    <td style="	position:absolute;top:28px;left:1600px;">
                        <button onclick="pageClose();" class="easyui-linkbutton" plain="true"
                                iconCls="icon-cancel">关闭</button></td>
                    </td>
                </tr>
            </table>
        </s:iterator>
    </form>
</s:if>
<input type="hidden" id="XMQY_ID">
<s:if test="SaveOrUpdate!='save'.toString()">
    <div id="All" class="" style="width: 100%;">
        <div id="left" >
            <s:if test="JYXS !=  'Y'.toString() ">
                <iframe id="leftIframe" frameborder="1" height="100%" width="100%"
                        style="overflow-x:scroll;overflow-y:scroll" src="http://localhost:8080/add_dgcdZtree.action?COMPANYNAME=<s:property value='COMPANYNAME'/>">
                </iframe>
            </s:if>
            <s:if test="JYXS ==  'Y'.toString() ">
                <iframe id="leftIframe" frameborder="1" height="100%" width="100%"
                        style="overflow-x:scroll;overflow-y:scroll" src="http://localhost:8080/dg_dgcdShowProject.action?dgcdID=<s:property value='dgcdID'/>&JYXS=<s:property value='JYXS'/>">
                </iframe>
            </s:if>
        </div>
        <s:if test="SaveOrUpdate=='update'.toString()">
        <div id="right"  >
            <iframe id="rightIframe" frameborder="1" height="100%" width="100%"
                    style="overflow-x:scroll;overflow-y:scroll" src="http://localhost:8080/sx_dgcdIframe.action?SaveOrUpdate">
            </iframe>
        </div>
        </s:if>
        <s:if test="SaveOrUpdate=='see'.toString()">
            <div id="right" >
                <iframe id="rightIframe" frameborder="1" height="100%" width="100%"
                        style="overflow-x:scroll;overflow-y:scroll" src="http://localhost:8080/sx_dgcdIframe.action">
                </iframe>
            </div>
        </s:if>
    </div>
</s:if>
</body>
<script>
    function saveOrUpdateF(action) {
        var data = getFormJson(document.getElementById("formDGCD"));
        //这个后台传到前台接收的是一个小数,通过下面转化一下
        if (data["SaveOrUpdate"] == "update") {
            var i = data["dgcdID"].indexOf(".");
            data["dgcdID"] = data["dgcdID"].substr(0, i);
        }else{
            var name=$("#PROJECTNAME").val();
            var ID=$("#PROJECTNO").val();
            data.DAMC=name;
            data.DABH=ID;
        }
        $.ajax({
            url: action,
            type: "post",
            data: data,
            async:false,
            success: function (data) {
                if(data!=undefined){
                    var parse = JSON.parse(data);
                    //if(parse.data.dgcdID.indexOf(".")!=-1){
                    //    var i = ID.indexOf(".");
                    //    var pageUrl = "go_DGCDUpdate.action?dgcdID=" + parse.data.dgcdID.substr(0, i) + "&SaveOrUpdate=update&COMPANYNAME="+parse.data.COMPANYNAME;
                    //}else {
                    //    var pageUrl = "go_DGCDUpdate.action?dgcdID=" + parse.data.dgcdID+ "&SaveOrUpdate=update&COMPANYNAME="+parse.data.COMPANYNAME;
                    //}
                    //alert(pageUrl);
                    //window.open (pageUrl,"_self");
                    window.opener.skipUpdate(parse.data.dgcdID,parse.data.COMPANYNAME);
                    return;
                }else{
                    window.location.reload();
                }
            },
            error: function (e) {
                alert("运行错误");
            }
        });
    }

    function after(action) {
        //$("#All").removeClass("none_i");
        if (action === 'sx_dgcdInsert.action') {
            if ($("#PROJECTNAME").val() === "" || $("#PROJECTNAME").val() === undefined) {
                alert("项目名称不可为空");
                return;
            }
        } else {
            if ($("#DAMC").val() === "" || $("#DAMC").val() === undefined) {
                alert("项目名称不可为空");
                return;
            }
        }
        saveOrUpdateF(action);
    }

    function getFormJson(frm) {
        var o = {};
        var a = $(frm).serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    }
</script>
</html>