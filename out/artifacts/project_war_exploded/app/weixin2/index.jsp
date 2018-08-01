<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>首页</title>
    <link rel="stylesheet" href="app/weixin2/style/weui.css"/>
    <link rel="stylesheet" href="app/weixin2/example.css"/>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
	<script  type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
    <script type="text/javascript">
    	<s:property value="initWeiXinScript" escapeHtml="false"/>
		function screenTwoCode(){
			wx.scanQRCode({
			    desc: '扫描流程单据号',
			    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
			    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
			    success: function (res) {
				    var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果 
				    	$.post('iwork_doScanCode.action',{type:'fbc',codestr:result}, function (text, status) {
						       if(text.indexOf('url:')>-1){ 
						       		var pageUrl = text.replace('url:','');
						       		selectUrl(pageUrl);
						       }else{
						       		alert('条码/二维码非流程相关，请确认扫描的二维码是否正确!');
						       }
				      		});
				}
			});
		}
		function selectUrl(url){
			window.location.href = url;
		}
		function createChart(){
				
		}
    </script>
</head>
<body ontouchstart>
    <div class="container" id="container">

    </div>
    <script type="text/html" id="tpl_home">
<div class="hd"  style="background-image:url('app/weixin2/images/banner01.jpg')">
    <h1 class="page_title"><s:property value="systemTitle"/></h1>
    <p class="page_desc">
<s:property value="systemTitle"/></p>
</div>
<div class="bd" >
    <div class="weui_grids">
<a href="weixin_processLaunchCenter.action" class="weui_grid">
            <div class="weui_grid_icon">
                <i class="icon icon_article"></i>
            </div>
            <p class="weui_grid_label">
                发起中心
            </p>
        </a>
<a href="weixin_processdeskIndex.action" class="weui_grid">
            <div class="weui_grid_icon">
                <i class="icon icon_article"></i>
            </div>
            <p class="weui_grid_label">
                流程审批(<s:property value="todoNum"/>)
            </p>
        </a>
 <a href="weixin_reportCenter.action" class="weui_grid">
            <div class="weui_grid_icon">
                <i class="icon icon_toast"></i>
            </div>
            <p class="weui_grid_label">
                报表查询
            </p>
        </a>
        <a href="javascript:screenTwoCode()" class="weui_grid">
            <div class="weui_grid_icon">
                <i class="icon icon_button"></i>
            </div>
            <p class="weui_grid_label">
                二维码扫码
            </p>
        </a>
       
       <!--
        <a href="concur_qyhh_index.action" class="weui_grid">
            <div class="weui_grid_icon">
                <i class="icon icon_dialog"></i>
            </div>
            <p class="weui_grid_label">
                企业会话
            </p>
        </a>
       -->
        <a href="concur_djcx_index.action" class="weui_grid">
            <div class="weui_grid_icon">
                <i class="icon icon_search_bar"></i>
            </div>
            <p class="weui_grid_label">
                鹰眼检索
            </p>
        </a>

<a href="weixin_cms_index.action" class="weui_grid">
            <div class="weui_grid_icon">
                <i class="icon icon_search_bar"></i>
            </div>
            <p class="weui_grid_label">
                信息资讯
            </p>
        </a>
 <a href="weixin_km_index.action" class="weui_grid">
            <div class="weui_grid_icon">
                <i class="icon icon_cell"></i>
            </div>
            <p class="weui_grid_label">
                知识库
            </p>
        </a>
<a href="weixin_account.action" class="weui_grid">
            <div class="weui_grid_icon">
                <i class="icon icon_cell"></i>
            </div>
            <p class="weui_grid_label">
                个人设置
            </p>
        </a>
 <a href="weixin_km_index.action" class="weui_grid">
            <div class="weui_grid_icon">
                <i class="icon icon_actionSheet"></i>
            </div>
            <p class="weui_grid_label">
                更多
            </p>
        </a>
    </div>
</div>
</script>
    
    <script src="app/weixin2/zepto.min.js"></script>
    <script src="app/weixin2/router.min.js"></script>
    <script src="app/weixin2/example.js"></script>
</body>
</html>
