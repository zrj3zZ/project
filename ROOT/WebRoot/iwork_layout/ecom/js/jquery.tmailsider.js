/*
	TMail Sider - jQuery TMail Sider Plugin
	Author: 		夏の寒风
	Version:		1.0 (2012/12/28)
	QQ:				490720430
	Please use this development script if you intend to make changes to the plugin code.
	For production sites, please use jquery.tmailsider.js.
*/

(function($) {
	var w=$("div");//e是不等于w的。 
	var Z_TMAIL_SIDE_DATA = new Array(); // 用来存放列表数据，暂时没有用到
	$.fn.Z_TMAIL_SIDER = function(options) {
		var opts = $.extend( {}, $.fn.Z_TMAIL_SIDER.defaults, options);
		var base = this;
		var target = opts.target;
		var Z_MenuList = $(base).find('.Z_MenuList');
		var Z_SubList = $(base).find('.Z_SubList');
		initPosition();
		var isIE = navigator.userAgent.indexOf('MSIE') != -1;
	    var isIE6 = isIE && ([/MSIE (\d)\.0/i.exec(navigator.userAgent)][0][1] == '6');
	    $.ajax({
	    	  url: "openjson.action",  
	    	  type: "POST",  
	    	  dataType: "json",  
	    	  contentType: "application/json; charset=utf-8",  
	    	  data: "{}",  
	    	  success: function(json){ 
	    		  $.each(json,function(idx,item){      
	    			  $("#nav_menu").append("<li id='"+item.id+"'><h3><img style='margin-top:2px;padding-right:6px;' src='"+item.icon+"'>"+ item.name +"</h3></li>");
	    			});
	    		  $("#nav_menu").append("<li id='999999'><h3><img  style='margin-top:2px;padding-right:6px;' src='iwork_img/star_full.png'>收藏夹</h3></li>");
	    	  },  //============================
	    	  error: function(x, e) {alert("Error："+x.responseText); },  
	    	  complete: function(x) { 
	    		  // alert("Complete："+x.responseText);
	    		  OpenOrCloseMenu(100);
	    	  }  
	    	});
	    
	 	// 重新定位
		$(window).resize(function() {
			initPosition();
		});
		$(Z_MenuList).find('li').live('mouseover', function(e, index) {
			var thisLi = this;
			var timeOut = setTimeout(function() {
				selectMenu(thisLi);
			},200);
			$("#Z_SubList").hover(function() {
				clearTimeout(timeOut);
			},function() {
				hideSubList(thisLi);
			});
			e.stopPropagation();
		}).live('mouseout', function(e) {
			var thisLi = this;
			var timeOut = setTimeout(function(){
				hideSubList(thisLi);
			}, 200);
			$("#Z_SubList").hover(function() {
				clearTimeout(timeOut);
			},function() {
				hideSubList(thisLi);
			});
			e.stopPropagation();
		});
		/*
		.live('click', function(e) {
			var thisLi = this; 
			selectMenu(thisLi);
		});*/
		
		if(!isIE6) {
			$(base).find('.title').append('<s class="btn_group bright"><a class="bleft"></a><a class="bright"></a></s>');
			$(base).find('.title .btn_group a').click(function() {
				if($(this).attr('class') == 'bleft' && $(this).parent().attr('class') == 'btn_group bleft') {
					$(base).find('.title .btn_group').attr('class', 'btn_group bright');
					OpenOrCloseMenu(0);
					opts.autoExpan = true;
				}
				if($(this).attr('class') == 'bright' && $(this).parent().attr('class') == 'btn_group bright') {
					$(base).find('.title .btn_group').attr('class', 'btn_group bleft');
					OpenOrCloseMenu(100);
					opts.autoExpan = false;
				}
			});
		}
		
		if(!isIE6) {
			$(Z_MenuList).find('li').click(function() {
				$(this).find('p').slideToggle(500);
			});
		}
		function selectMenu(thisLi){
			var top =$(thisLi).offset().top;
			var ishiden = $(thisLi).find('p').is(":hidden");
			var index = $(thisLi).index();
				top = top-60; 
			var sysId = $(thisLi).attr("id");
			if(sysId=='999999'){
				 $.ajax({  
			    	  url: "favjson.action",
			    	  type: "POST",  
			    	  dataType: "json",  
			    	  contentType: "application/json; charset=utf-8",  
			    	  data: "{}",  
			    	  success: function(json){
			    		  $("#menuList").html("");
			    		  $("#menuList").append("<li class='subItem'><p class=\"subItem-brand\">");
			    		  $.each(json,function(idx,item){
			    				   var iteminfo = ("<h3 class=\"subItem-hd\">"+item.name+"</h3><p id='fav_node_"+item.id+"' class=\"subItem-brand\">");
	    				    		  $.each(item.children,function(idxa,subitem){
	    				    			  	var address = "<a target=\""+subitem.target+"\" href=\"javascript:addTab('"+subitem.name+"','"+subitem.pageurl+"')\"><img src='"+subitem.icon+"' border='0'>"+subitem.name+"</a>";
	    				    			  	iteminfo = iteminfo+address;
	    				    		  });
	    				    		  iteminfo = iteminfo+"</p>";
			    				   $("#menuList li").append(iteminfo);
			    		  });
			    		  $("#menuList").append("</p></li>");
			    	  },  //============================
			    	  error: function(x, e) {alert("Error："+x.responseText); },  
			    	  complete: function(x) { 
			    	  }  
			    	});
			}else{ 
				 $.ajax({  
			    	  url: "openjson.action?nodeType=SYS&id="+sysId,
			    	  type: "POST",  
			    	  dataType: "json",  
			    	  contentType: "application/json; charset=utf-8",  
			    	  data: "{}",  
			    	  success: function(json){
			    		  $("#menuList").html("");
			    		  $("#menuList").append("<li class='subItem'><p class=\"subItem-brand\">");
			    		  $.each(json,function(idx,item){
			    			   if(item.isParent=="false"){
			    				   $("#menuList p").append("<a target=\""+item.target+"\" href=\"javascript:addTab('"+item.name+"','"+item.pageurl+"')\"><img src='"+item.icon+"' border='0'>"+item.name+"</a>");
			    			   }else{
			    				   var iteminfo = ("<h3 class=\"subItem-hd\">"+item.name+"</h3><p id=\"nav_node_"+item.id+"\" class=\"subItem-brand\"></p>");
			    				   $.ajax({  
			    				    	  url: "openjson.action?nodeType=NODE&id="+item.id, 
			    				    	  type: "POST",  
			    				    	  dataType: "json",  
			    				    	  contentType: "application/json; charset=utf-8",  
			    				    	  success: function(json){
			    				    		  var itemstr = "";
			    				    		  $("#nav_node_"+item.id).html("");
			    				    		  $.each(json,function(idx,subitem){
			    				    				$("#nav_node_"+item.id).append("<a target=\""+subitem.target+"\" href=\"javascript:addTab('"+subitem.name+"','"+subitem.pageurl+"')\"><img src='"+subitem.icon+"' border='0'>"+subitem.name+"</a>");
			    				    		  }); 
			    				    		  
			    				    	  }
			    				    	});
			    				  // alert(iteminfo);
			    				   $("#menuList li").append(iteminfo);
			    			   }
			    		  });
			    		  $("#menuList").append("</p></li>");
			    	  },  //============================
			    	  error: function(x, e) {alert("Error："+x.responseText); },  
			    	  complete: function(x) { 
			    	  }  
			    	});
				
			}
			
			 
			$("#Z_SubList").show().stop().animate({
				top:30, 
				left: 2
			}, 100);
			
		}
		
		function getSubMenuHTML(id){
			$.ajax({  
		    	  url: "openjson.action?nodeType=NODE&id="+id, 
		    	  type: "POST",  
		    	  dataType: "json",  
		    	  contentType: "application/json; charset=utf-8",  
		    	  success: function(json){
		    		  var item = "";
		    		  $.each(json,function(idx,subitem){
		    			   if(subitem.pageurl!="/"){
		    				   item+="<a target=\""+subitem.target+"\" href=\""+subitem.pageurl+"\">"+subitem.name+"</a>";
		    			   }
		    		  });
		    		  var  html = "<p class=\"subItem-brand\">"+item+"</p>";
		  			  return html;
		    	  }
		    	});
			
		}
		function hideSubList(thisLi) {
			if(!isIE6) {
			}
			$("#Z_SubList").hide();
		};
		
		// 定位
		function initPosition() {
			if($(base).css('position') == 'absolute') {
				$(base).css({
					top: $(target).offset().top, 
					left: $(target).offset().left - $(base).width()
				}).show();

				$(Z_SubList).css({
					top: $(target).offset().top - 60,
					left: $(target).offset().left - $(base).offset().left
				});
			}
			if($(base).css('position') == 'fixed') {
				$(base).css({
					top: opts.fTop, 
					left: $(target).offset().left - $(base).width()
				}).show();

				$(Z_SubList).css({
					top: opts.cTop - 60,
					left: $(target).offset().left - $(base).offset().left
				});
			}
		};

		// 折叠
		function OpenOrCloseMenu(l) {
			var mList = $(Z_MenuList).find('ul li');
			for(var i = 0; i < l; i++) {
				if(i < mList.length) {
					var thisLi = $(mList[i]);
					$(thisLi).find('p').slideUp(500, function (){
						$(this).hide();
					});
				}
			}
			
			for(var i = mList.length - 1; i >= l; i--) {
				if(i >= 0) {
					var thisLi = $(mList[i]);
					$(thisLi).find('p').slideDown(500, function (){
						$(this).show();
					});
				}
			}
		};
	};
	
	// 默认配置项
	$.fn.Z_TMAIL_SIDER.defaults = {
		target: $('#Z_RightSide'),
		fTop: 60, // 距离顶部距离
		cTop: 100, // 滚动条滚动多少像素后开始折叠的高度
		unitHeight: 80, // 每滚动多少距离折叠一个
		autoExpan: false 
	};
})(jQuery);