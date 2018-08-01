<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <title>日程提醒</title>
    <meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

<meta name="description" content="Write an awesome description for your new site here. You can edit this line in _config.yml. It will appear in your document head meta (for Google search results) and in your feed.xml site description.
">

<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.6.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery-ui-1.8.16.custom.js"></script>
<script type="text/javascript" src="iwork_js/fullcalendar/fullcalendar/fullcalendar.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=blue"></script>

<link rel="stylesheet" type="text/css" href="iwork_css/plugs/schcalendaraction.css" />
<link rel="stylesheet" href="mobile/assets/lib/weui.min.css">
<link rel="stylesheet" href="mobile/assets/css/jquery-weui.css">
<style type="text/css">
	.memo{
		font-size:12px;
		color:#666;
		padding-left:20px;
		background-image:url(iwork_img/user.png);
		background-repeat:no-repeat;
	}
	.item_title{
		font-size:16px;
		font-weight:normal;
		padding-left:5px;
	}
</style>

<script type="text/javascript">
	function loadrc(id){
	
	var pageUrl =encodeURI("editSchCalendar.action?id="+id);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
    function openNoticePage(title,actDefId,instanceId,excutionId,taskId,dataid){
	var url = "wechat_process_notice.action";
	redirectUrl(url);
}
function openSignsPage(title,actDefId,instanceId,excutionId,taskId){
	var url = 'loadSignsPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
	redirectUrl(url); 
}
function openTaskPage(title,actDefId,instanceId,excutionId,taskId){
	var url = 'wechat_pr_formpage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
	redirectUrl(url);
}
    function finish(){
    	var url = "weixin_optomRC_account.action";
    	redirectUrl(url);
    }
    function toRC(){
    	var url = "weixin_opricheng_account.action";
    	redirectUrl(url);
    }
    function newrc(){
    	var url = "wechat_new_notice.action";
    	redirectUrl(url);
    }
    
	function redirectUrl(url){
		this.location.href = url;
	}
</script>
  </head>

  <body ontouchstart>
     <div class="weui_tab">
      <div class="weui_navbar">
        <div class="weui_navbar_item"  onclick="toRC()">
        今天日程
        </div>
        <div class="weui_navbar_item" onclick="finish()">
          明天日程
        </div>
        <div class="weui_navbar_item weui_bar_item_on" >
         后天日程
        </div>
         <div class="weui_navbar_item"  onclick="newrc()">
        新建日程
        </div>
      </div>
      <div class="weui_tab_bd">
			<div  id="list" class="weui_cells weui_cells_access ">
				
		     </div>
      </div>
    </div>
    <div id="tip" class="weui-infinite-scroll" >
      <div  class="infinite-preloader"></div>
      正在加载
    </div>
    
    <script src="mobile/assets/lib/jquery-2.1.4.js"></script>
<script src="mobile/assets/lib/fastclick.js"></script>
<script>
  $(function() {
    FastClick.attach(document.body);
  });
</script>
<script src="mobile/assets/js/jquery-weui.js"></script>
 <script>
 	var page = 1;
 	var pagesize = 15;
    
      
      function showlist(page){
        var searchKey = "";
        var itemNum = pagesize*(page-1);
      	var url = "wechat_DaytomRC_notice.action"; 
      	$.post(url, {
	    	pageNo : page++,
	    	itemNum :itemNum,
	    	searchKey :searchKey
		}, function(rText) {
			$('#loading').fadeOut();
			if(rText==""){
				var loading = true;
				$("#tip").text("已到达底部");
			}else{
				$("#list").append(rText);
				$("#tip").text("");
			}
		});  
      }
      showlist(page);
    </script>
  </body>
</html>
