
$(document).ready(function(){
	$('#feedback a').click(function () {
		$('body').animate({
			scrollTop: 0
		}, 200);
		return false;
	});
	//加载新闻类型
	loadCmsColunm(0);
	//加载收藏夹
	loadFavMenuList();
	loadSubMenu(0);
	//点击加载菜单
	$('.lev').click(function(){	
		var id = $(this).attr("event-args");
		if(typeof(id)=="undefined"){
			$(".basic-list li.current").removeClass("current"); 
			//获取URL地址
			var url = $(this).attr("event-url"); 
			var menu = $(this).attr("event-menu");
			$(this).parent().addClass("current"); 
			if(typeof(url)!="undefined"){
				redirectLeftView(url,'other',menu); 
			}else{ 
				url = $(this).attr("href");
				window.location.href = url;
			}
		}else{
			var loading = $("<img alt=\"加载中...\" class=\"loadingImg\" title=\"图片加载中...\" src=\"iwork_img/login/default/loading.gif\" />");
//			$("#sub_menu_"+id).append(loading); 
			$(".basic-list li.current").removeClass("current"); 
			$(this).parent().addClass("current");  
			var sub_menu = $("#sub_menu_"+id).text();
			if(sub_menu==''){  
				$.getJSON("openjson.action",{id:id,nodeType:"SYS"},function(data){
					$("#sub_menu_"+id).val("");
					var html = '';
					$.each(data,function(idx,item){  
						html+="<li  id='submenuItem"+idx+"' onclick='openSubMenuLi("+idx+")'  event-url='"+item.pageurl+"' ><a href='javascript:void(0)' ><img src='"+item.icon+"' onerror='this.src=\"iwork_img/arrow.png\"'/>"+item.name+"</a></li>";
					}); 
					$("#sub_menu_"+id).html(html);  
					$("#sub_menu_"+id).show(); 
				});
			}
//			loading.remove();
			$("#sub_menu_"+id).slideToggle("slow"); 
		}
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
 
function loadFavMenuList(){
	 $.getJSON("favjson.action",{},function(data){
		 $.each(data,function(idx,item){ 
			 if(item.name=='常用功能'){
				 var tempList = "";
				 tempList+="<dl>";
				 	$.each(item.children,function(subIndex,subItem){ 
				 		tempList+="<dd>";
				 		tempList+="<a href=\"javascript:void(0)\" style='padding-left:15px;'><span>"+subItem.name+"</span></a>";
				 		tempList+="</dd>";  
				 	});
				 	 tempList+="</dl>";
				 $("#downMemulist").append(tempList);
			 }else if(item.name=='我的收藏夹'){
				 var tempList = "";
				 $.each(item.children,function(subIndex,subItem){ 
				 		tempList+="<dd>";
				 		tempList+="<a href=\"javascript:void(0)\"  style='padding-left:15px;'><span>"+subItem.name+"</span>&nbsp;<img src='iwork_img/star_empty.png'></a>";
				 		tempList+="</dd>";  
				 });
				 $("#favMenuList").append(tempList);
			 }
		 });
	 });
}
function loadSubMenu(){
	$(".app-list").find("li a").each(function() {
		 var id = $(this).attr("event-args");
		 $.getJSON("openjson.action",{id:id,nodeType:"SYS"},function(data){
				$("#sub_menu_"+id).val("");
				var html = '';
				$.each(data,function(idx,item){  
//					alert(item.pageurl);
					var index = item.id;
					var menukey = $("#menukey").val();
					var style='';
					if(menukey==index){
						style = " class='subleftmenu' "
					}
					html+="<li  id='submenuItem_"+index+"' "+style+" onclick='openSubMenuLi("+index+")'  event-url='"+item.pageurl+"' ><a href='javascript:void(0)' ><img src='"+item.icon+"' onerror='this.src=\"iwork_img/arrow.png\"'/>&nbsp;"+item.name+"</a></li>";
				}); 
				$("#sub_menu_"+id).html(html);  
				$("#sub_menu_"+id).show(); 
			});
	});
	
}
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
		$.dialog({ 
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
				content:pageurl,
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
function openSubMenuLi(index){
	var url = $("#submenuItem_"+index).attr("event-url");
	redirectLeftView(url,"other",index);   
}
function redirectLeftView(url,type,menu){
		url = url.replace("?","%3F");
		url = replaceAll1(url,"&","%26");
		if(typeof(menu)!="undefined"){
			var pageurl = "pageindex1.action?navurl="+url+'&tabkey='+type+'&menukey='+menu;
			window.location.href = pageurl;
		}else{
			var pageurl = "pageindex1.action?navurl="+url+'&tabkey='+type;
			window.location.href = pageurl;
		} 
	
} 
function redirectFullView(url,type){
	url = replaceAll(url,"?","%3F");
	url = url.replace(url,"&","%26");
	var pageurl = "pageindex2.action?navurl="+url+'&tabkey='+type;
	window.location.href = pageurl; 
}
function replaceAll1(url,findstr,replaceStr){
	var intArray = 10;
	 for(var i = 0; i<intArray;i++){
		 if(url.indexOf(findstr)<0){
			 break;
		 }
		 url = url.replace(findstr,replaceStr);
	 }
	return url;
}
//打开收藏夹
function arrcoll(){
		var pageUrl = "url:myfav.action";
		$.dialog({
			id:'fav_show1',
			cover:true,
			title:'整理收藏夹',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:680,
			cache:false,
			lock: true,
			height:480, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			content:pageUrl
		});
}
function exitSystem(){
	if(confirm('您确定要退出本次登录吗?')){
        $.ajax({
   			url:'logout.action', //后台处理程序
   			type:'post',         //数据发送方式
   			dataType:'json'
			});
    location.href = 'login.action';
}
}
