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
     function todo(){
    	var url = "wechat_process_todo.action";
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
        <div class="weui_navbar_item " onclick="todo()">
          待办任务(<s:property value="taskNum"/>)
        </div>
        <div class="weui_navbar_item weui_bar_item_on"  onclick="finish()">
          已办任务
        </div>
        <div class="weui_navbar_item"  onclick="notice()">
          通知/抄送(<s:property value="noticeNum"/>)
        </div>
      </div>
    </div>
    <div class="weui_panel weui_panel_access">
      <div class="weui_panel_hd" style="padding-top:40px;padding-left:0px;padding-right:0px;">
      	 <div class="weui_search_bar" id="search_bar">
      <form class="weui_search_outer">
        <div class="weui_search_inner">
          <i class="weui_icon_search"></i>
          <input type="search" name="searchKey" class="weui_search_input" style="color:#000" id="searchKey" placeholder="搜索" required/>
          <a href="javascript:" class="weui_icon_clear" id="search_clear"></a>
        </div>
        <label for="search_input" class="weui_search_text" id="search_text">
          <i class="weui_icon_search"></i>
          <span>搜索</span>
        </label>
      </form>
      <a href="javascript:" class="weui_search_cancel" id="search_cancel">取消</a>
    </div>
      </div>
      <div class="weui_panel_bd">
      	 <div  id="list" class="weui_cells weui_cells_access "></div>
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
 	var pagesize = 10;
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
        var searchKey = $("#searchKey").val();
        var itemNum = pagesize*(page-1);
      	var url = "wechat_process_finishHtml.action"; 
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
