<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <title>流程中心</title>
    <meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

<meta name="description" content="Write an awesome description for your new site here. You can edit this line in _config.yml. It will appear in your document head meta (for Google search results) and in your feed.xml site description.
">
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
    	var url = "wechat_process_finish.action";
    	redirectUrl(url);
    }
    function notice(){
    	var url = "wechat_process_notice.action";
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
        <div class="weui_navbar_item weui_bar_item_on">
          待办任务(<s:property value="taskNum"/>)
        </div>
        <div class="weui_navbar_item" onclick="finish()">
          已办任务
        </div>
        <div class="weui_navbar_item"  onclick="notice()">
          通知/抄送(<s:property value="noticeNum"/>)
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
      var loading = false;
      $(document.body).infinite().on("infinite", function() {
        if(loading) return;
        $("#tips").show();
        loading = true;
        setTimeout(function() {
        page++;
        showlist(page);
          loading = false;
        }, 500);
      });
      
      function showlist(page){
        var searchKey = "";
        var itemNum = pagesize*(page-1);
      	var url = "wechat_process_taskHtml.action"; 
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
				$("#tip").hide();
			}
		});  
      }
      showlist(page);
    </script>
  </body>
</html>
