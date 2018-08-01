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
    <link rel="stylesheet" href="mobile/assets/lib/weui.min.css">
    <link rel="stylesheet" href="mobile/assets/css/jquery-weui.css">
    <link rel="stylesheet" href="mobile/assets/demos/css/demos.css">
	<script src="mobile/assets/jweixin-1.1.0.js"></script>
    <script type="text/javascript">
    	<s:property value="initWeiXinScript" escapeHtml="false"/>
		function run2Code(){
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
			 <header class='demos-header'  style="background-image:url('app/weixin2/images/banner01.jpg')">
			      <h1 class="demos-title"><s:property value="systemShortTitle"/></h1>
			      <p class='demos-sub-title'><s:property value="systemTitle"/></p>
			    </header>
    <div class="weui_grids">
<a href="wechat_process_todo.action" class="weui_grid">
            <div class="weui_grid_icon">
               <img src="mobile/assets/images/icon_nav_toast.png" alt="">
            </div>
            <p class="weui_grid_label">
                流程审批(<s:property value="taskNum"/>)
            </p>
        </a>

 <a href="weixin_opricheng_account.action" class="weui_grid">
            <div class="weui_grid_icon">
                <img src="mobile/assets/images/report3.png" alt="">
            </div>
            <p class="weui_grid_label">
               日程提醒
            </p>
        </a>
      
       
       <!--
  <a href="javascript:run2Code()" class="weui_grid">
            <div class="weui_grid_icon">
                 <img src="mobile/assets/images/icon_nav_button.png" alt="">
            </div>
            <p class="weui_grid_label">
                二维码扫码
            </p>
        </a>
<a href="wechat_process_launch.action" class="weui_grid">
            <div class="weui_grid_icon">
               <img src="mobile/assets/images/icon_nav_article.png" alt="">
            </div>
            <p class="weui_grid_label">
                发起中心
            </p>
        </a>
        <a href="concur_qyhh_index.action" class="weui_grid">
            <div class="weui_grid_icon">
                <i class="icon icon_dialog"></i>
            </div>
            <p class="weui_grid_label">
                企业会话
            </p>
        </a>
       
        <a href="concur_djcx_index.action" class="weui_grid">
            <div class="weui_grid_icon">
                 <img src="mobile/assets/images/icon_nav_search_bar.png" alt="">
            </div>
            <p class="weui_grid_label">
                鹰眼检索
            </p>
        </a>   weixin_TZGG_account-->

  <a href="weixin_cms_index.action" class="weui_grid">
            <div class="weui_grid_icon">
               <img src="mobile/assets/images/report2.png" alt="">
            </div>
            <p class="weui_grid_label">
                信息资讯
            </p>
        </a> 
      
<!--  <a href="weixin_km_index.action" class="weui_grid">
            <div class="weui_grid_icon">
                  <img src="mobile/assets/images/icon_nav_panel.png" alt="">
            </div>
            <p class="weui_grid_label">
                知识库
            </p>
        </a> -->
<!-- <a href="wechat_sysmsg_index.action" class="weui_grid">
            <div class="weui_grid_icon">
             <img src="mobile/assets/images/icon_nav_dialog.png" alt="">
            </div>
            <p class="weui_grid_label">
                系统消息
            </p>
        </a> -->
 <a href="weixin_person_index.action" class="weui_grid">
            <div class="weui_grid_icon">
           <img src="mobile/assets/images/icon_nav_actionSheet.png" alt="">
            </div>
            <p class="weui_grid_label">
                个人设置
            </p>
        </a>
    </div>
</div>
    
 <script src="mobile/assets/lib/jquery-2.1.4.js"></script>
<script src="mobile/assets/lib/fastclick.js"></script>
<script>
  $(function() {
    FastClick.attach(document.body);
  });
</script>
<script src="mobile/assets/js/jquery-weui.js"></script>
</body>
</html>
