
$(document).ready(function(){
	//更多菜单
//	$('.dorp-right').hover(function() {
//		$(this).addClass("current");
//	}, function() {
//		$(this).removeClass("current");
//	});
	//加载新闻类型
	loadCmsColunm(0);
	//加载新闻列表
	$('.lev').click(function(){	
		var id = $(this).attr("event-args");
		var loading = $("<img alt=\"加载中...\" class=\"loadingImg\" title=\"图片加载中...\" src=\"iwork_img/login/default/loading.gif\" />");
		$("#sub_menu_"+id).append(loading);
		var sub_menu = $("#sub_menu_"+id).text();
		if(sub_menu==''){ 
			$.getJSON("openjson.action",{id:id,nodeType:"SYS"},function(data){
				$("#sub_menu_"+id).val("");
				$.each(data,function(idx,item){ 
					var $li = $("<li><a href=\"\" ><img src='"+item.icon+"' onerror='this.src=\"iwork_img/arrow.png\"'/>"+item.name+"</a></li>");
					$("#sub_menu_"+id).append($li);
				});
			});
		}
		loading.remove();
		$("#sub_menu_"+id).slideToggle("slow");
		return false; 
		});
        $('#diffcss ul li').click(function(){
              var curList = $("#diffcss ul li.current").attr("rel");
              var $newList = $(this);
              var listID = $newList.attr("rel"); 
              if(listID != 'kind-weibo'){
                 $('#image_insert_box').hide();
                 $('#video_insert_box').hide();
              }else{
                 $('#image_insert_box').show();
                 $('#video_insert_box').show();
              }
              if (!$(this).hasClass('current')) {
                    $('.block').each(function(){
                          if($(this).attr('rel') == listID){
                                $(this).show();
                          }else{
                                $(this).hide();
                          }
                    });
                    $(this).addClass('current').siblings().removeClass('current')
              };
        });
 })
 

 function selectCmsTab(tabIndex){
	var loading = $("<div style=\"text-align:center\"><img alt=\"加载中...\" class=\"loadingImg\" title=\"加载中...\" src=\"iwork_img/login/default/loading.gif\" /></div>");
	$("#feed-lists").html(loading);
	loadCmsColunm(tabIndex);
	loading.remove();
} 
 function loadCmsColunm(tabIndex){
	 $("#column_tab").html("");
		$.getJSON("cmsRelationAction!contentgrid.action",{channelid:1},function(data){
			$.each(data.rows,function(idx,item){ 
				if(item.portlettype==0){ 
					var $li; 
					if(tabIndex==item.id){
						 $li = $("<li class=\"current\"><span><a href=\"javascript:void(0);\" >"+item.portletname+"<i class=\"ico-arrow-down\"></i></a></span></li>");
						 loadCmsList(item.id);
					}else{
						 $li = $("<li><span><a href=\"javascript:selectCmsTab("+item.id+");\" >"+item.portletname+"<i class=\"ico-arrow-down\"></i></a></span></li>");
					} 
					$("#column_tab").append($li);
				}
			});
		});
 }
 
 function openWin(title,height,width,pageurl,location,dialogId){
		if(dialogId=='undefined')dialogId="mainDialogWin"; 
		art.dialog.open(pageurl,{ 
			   id:dialogId, 
				cover:true, 
				title:title,  
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				width:width,
				cache:false, 
				lock: true,
				esc: true,
				height:height,   
				iconTitle:false,  
				extendDrag:true,
				autoSize:true,
				close:function(){
				   location.reload();
				}
				
			});
	}
function loadCmsList(portleId){
	$.getJSON("cms_content_list.action",{portletid:portleId},function(data){
		$.each(data,function(idx,item){
				var rowItem = "<dl class=\"blog-list\">";
				if(item.prepicture!=''){
					 rowItem += "<dt class=\"face\"><a href=\"javascript:openCms("+item.id+")\"><img src=\""+item.prepicture+"\" width=\"80px\" height=\"70px\" onerror=\"this.src='iwork_img/nopic1.gif'\"></a></dt>";
				}
				rowItem+="<dd class=\"content\">";
				rowItem+="<h2>";
				rowItem+="<a href=\"javascript:openCms("+item.id+")\" target=\"_self\" class=\"name\">"+item.title+"</a>";
				rowItem+="</h2>";
				rowItem+="</dd>";
				rowItem+="<dd class=\"detail\" style=\"font-size:14px;\">";
				rowItem+=item.precontent;
				rowItem+="</dd>";
				rowItem+="<div class=\"date right\" style=\"padding-bottom:5px;\">";
				rowItem+=""+item.source+"<i class=\"vline\">&#8211;</i>"+item.releaseman+"<i class=\"vline\">&#8211;</i>"+item.releasedate+"</div>";
				rowItem+="</dl>";
				var $li = $(rowItem);
				$("#feed-lists").append($li);
		});
		
	});
}
function openCms(infoid){
    var url = "cmsOpen.action?infoid="+infoid;
    var win_width = window.screen.width-150;
    var page = window.open('form/waiting.html','_blank');
    page.location=url;
}

function redirectUrl(url){
	var pageurl = "pageindex1.action?navurl="+url;
	window.location.href = pageurl;
}
function redirectUrl2(url){
	var pageurl = "pageindex2.action?navurl="+url;
	window.location.href = pageurl;
}
function selectTopMenuBar(url){ 
	redirectUrl(url);
	var li = $(".qg-nav ul li");
	$.each(li,function(idx,item){
		var css = $(item).attr("class");
		if(css=='current'){
			$(item).attr("class","");
		}
		var href = $(item).find("a").attr('href');
		if(href.indexOf(url)>0){
			$(item).attr("class","current");
		} 
	})
	
}
function selectLeftMenuBar(url){ 
	alert(url);
}

