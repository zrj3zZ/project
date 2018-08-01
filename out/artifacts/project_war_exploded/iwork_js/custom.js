/*OA首页个人信息填写*/
   $(function(){
	$(".person_info_input")
		.mouseover(function(){			
			$(this).removeClass("person_info_input");
			$(this).addClass("person_info_input_hover");
				
			  
		})
		.mouseout(function(){
	   	$(this).removeClass("person_info_input_hover");
			$(this).addClass("person_info_input");
			         
		})
	  })

   $(function(){
	$(".person_info_input")
		.focus(function(){			
			$(this).removeClass("person_info_input");
			$(this).addClass("person_info_input_focus");
			if($(this).val() != "编辑个性签名") {
				gxqmOnfocus();
			}else {
				$(this).val("");
			} 
		})
		.blur(function(){
	   		$(this).removeClass("person_info_input_focus");
			$(this).addClass("person_info_input");
			if($(this).val() == "") {
				$(this).val("编辑个性签名");
			}
			gxqmOnblur();
		})
	  })

/*短信页签名*/
   $(function(){
	$(".person_info_input2")
		.mouseover(function(){			
			$(this).removeClass("person_info_input2");
			$(this).addClass("person_info_input2_hover");
				
			  
		})
		.mouseout(function(){
	   	$(this).removeClass("person_info_input2_hover");
			$(this).addClass("person_info_input2");
			         
		})
	  })

   $(function(){
	$(".person_info_input2")
		.focus(function(){			
			$(this).removeClass("person_info_input2");
			$(this).addClass("person_info_input2_focus");
			if($(this).val() != "编辑签名") {
				gxqmOnfocus();
			}else {
				$(this).val("");
			} 
		})
		.blur(function(){
	   		$(this).removeClass("person_info_input2_focus");
			$(this).addClass("person_info_input2");
			if($(this).val() == "") {
				$(this).val("编辑签名");
			}
			gxqmOnblur();
		})
	  })

/*回到首页*/
$(document).ready(function(){
	$("#back-top").hide();
	$(function () {
		$(window).scroll(function () {
			if ($(this).scrollTop() > 50) {
				$('#back-top').fadeIn(1000);
			} else {
				$('#back-top').fadeOut(1000);
			}
		});

		$('#back-top a').click(function () {
			$('body,html').animate({
				scrollTop: 0
			}, 200);
			return false;
		});
	});
});



/* 首页登录密码悬浮*/
   $(function(){
	$('#pwd')
		.focus(function(){	


			 if($(this).val()==this.defaultValue){  
                			$(this).parent('#password').removeClass("showPlaceholder"); 
		      } 
			  
		})
		.blur(function(){
	
               if($(this).val()==''){
						$(this).parent('#password').addClass("showPlaceholder");
					}
				});

     })
   
 /*下拉框的背景色定位*/

   $(function(){
	$(".txt")
		.focus(function(){			
		 $('.search_xiala').slideDown(200);
		})
		.blur(function(){
	     setTimeout("$('.search_xiala').hide()",200);
		 setTimeout('$(".current_father > li:first").addClass("current").siblings().removeClass("current")',200)
		})
	  })

 $(document).ready(function(){
	$(".current_father > li").hover(function(){
		$(this).addClass("current")		
    	.siblings().removeClass("current")		
			
	});
	$(".current_father > li").click(function(){
		$(this).removeClass("current")		
    	.siblings().removeClass("current")		
			
	});
});
/* 首页登录*/
   $(function(){
	$('input.text')
		.focus(function(){	
					$(this).removeClass("text");
			$(this).addClass("login_focus");

			 if($(this).val()==this.defaultValue){  
                  $(this).val("");   
		      } 
			  
		})
		.blur(function(){
		$(this).removeClass("login_focus");
$(this).addClass("text");
               if($(this).val()==''){
					$(this).val(this.defaultValue);
					}
				});

     })

/*填写提示*/
$(function(){
    var x = 10;  
	var y = 30;
	$(".tooltip").mouseover(function(e){
       	this.myTitle = $(this).attr("tooltip");
	    var tooltip = "<div id='tooltip3'>"+ this.myTitle +"<\/div>"; 
		$("body").append(tooltip);	//把它追加到文档中
		$("#tooltip3")
			.css({
				"top": (e.pageY+y) + "px",
				"left": (e.pageX+x)  + "px"
			}).delay(300).fadeIn();	 
    }).mouseout(function(){		
		$(this).attr("tooltip",this.myTitle);
		$("#tooltip3").remove();  
    }).mousemove(function(e){
		$("#tooltip3")
			.css({
				"top": (e.pageY+y) + "px",
				"left": (e.pageX+x)  + "px"
			});
	});
})
 
 /*流程中心排序箭头*/
 $(function(){
    $(".orange_down_arrow").click(function(){
		
		 $(this).toggleClass( 'orange_up_arrow' )
	})
})
/*橙色按钮*/

$(function(){
	$('a.button').hover(
        function () {
                $(this).stop(true, true).animate({
                        backgroundColor: '#fa7a20'
                   
                },200)
        },

        function () {
                $(this).stop(true, true).animate({
                        backgroundColor: '#fdb98a'
                },200)
        });
				
 	$('a.button,a.organge_btn,a.login_btn_a').mousedown(

        function () {
                $(this).stop(true, true).animate({
                        backgroundColor: '#db5a00'
                   
                },10)
 
       
        });
 	$('a.button,a.organge_btn,a.login_btn_a').mouseup(

        function () {
                $(this).stop(true, true).animate({
                        backgroundColor: '#fa7a20'
                },10)
        });
	
	/* input+按钮的鼠标滑过效果
    
	     * --------------------------------------------------------------------------------*/

 /* input+按钮的鼠标滑过效果
    
	     * --------------------------------------------------------------------------------*/

        $('div.input_plusbutton-form a.submit, div.search-form a.submit,div.short_input_plusbutton-form a.submit').hover(
		
		function () {
                $(this).stop(true, true).animate({
                        backgroundColor: '#fa7a20'
                },200)
        },

        function () {
                $(this).stop(true, true).animate({
                        backgroundColor: '#ebebeb'
                },200)
        });



});
/*流程组高度*/
 
$(document).ready(function(){

  var h = $(window).height();
   $(".lcz").css("height",h);


	})
	
$(window).resize(function(){
var h = $(window).height();
 $(".lcz").css("height",h);


	})
/*经理工资查询高度*/

$(document).ready(function(){

  var h = $(window).height()-175;
   $(".jlgzcx").css("height",h);


	})
	
$(window).resize(function(){
var h = $(window).height()-175;
 $(".jlgzcx").css("height",h);


	})	
/*工资查询高度*/

$(document).ready(function(){

  var h = $(window).height()-178;
   $(".gzcx").css("height",h);


	})
	
$(window).resize(function(){
var h = $(window).height()-178;
 $(".gzcx").css("height",h);


	})	

/*办公用品申领*/

$(document).ready(function(){

  var h = $(window).height()-40;
   $(".bbyp_right").css("height",h);
  var h2 = h-262;
   $(".office_tools").css("height",h2);
	})
	
$(window).resize(function(){
var h = $(window).height()-40;
   $(".bbyp_right").css("height",h);
   var h2 = h-262;
   $(".office_tools").css("height",h2);

	})	


/*考勤自助高度*/

$(document).ready(function(){

  var h = $(window).height()-150;
   $(".kaoqinzz").css("height",h);


	})
	
$(window).resize(function(){
var h = $(window).height()-150;
 $(".kaoqinzz").css("height",h);


	})	

/*首页预定会议室高度*/

$(document).ready(function(){

  var h = $(window).height()-136;
   $(".chrome_hack").css("height",h);


	})
	
$(window).resize(function(){
var h = $(window).height()-136;
 $(".chrome_hack").css("height",h);


	})	

/*首页iframe高度*/

$(document).ready(function(){

  var h = $(window).height()-47;
   $(".home_body_left").css("height",h);
   $(".home_body_main").css("height",h);

	})
	
$(window).resize(function(){
var h = $(window).height()-47;
 $(".home_body_left").css("height",h);
$(".home_body_main").css("height",h);

	})	

/*地址簿的select高度*/

$(document).ready(function(){

  var h = $(window).height()-20;
   $(".zyxz_select").css("height",h);

	})
	
$(window).resize(function(){
var h = $(window).height()-20;
$(".zyxz_select").css("height",h);

	})	

 /* 数据字典*/
   $(function(){
	$("#input_focus")
		.focus(function(){			
			 if($(this).val() == "搜索"){  
                  $(this).val("");   
		      } 
			  
		})
		.blur(function(){
			 if ($(this).val() == '') {
                $(this).val("搜索");
             }

		})
		.change(function(){
			$('.search_btn3').attr('class','search_btn2')
			
			})
			
    .keyup(function(){  
		   if(event.keyCode == 13){    
		      $('.search_btn3').attr('class','search_btn2')
			  }   
		 })
     })
	 
       	
  
  $(function(){
		$("#search_btn").click(function(){
			$('.search_btn2').attr('class','search_btn3')
              $("#input_focus").val("搜索"); 

			if($("#input_focus").val()!=("#input_focus").defaultValue){  
                 $("#input_focus").val() ==("#input_focus").defaultValue
		      } 
     
		})		
    })
 
 
  /* 地址簿*/
   $(function(){
	$("#inquiries_input_focus")
		.focus(function(){			
			 if($(this).val() == "搜索"){  
                  $(this).val("");   
		      } 
			  
		})
		.blur(function(){
			 if ($(this).val() == '') {
                $(this).val("搜索");
             }

		})

     })
 
 /*使用说明*/
 $(function(){
    $(".use_exp").click(function(){
		
		 $(this).toggleClass( 'use_exp_b' ),
         $(this).next('.use_exp_box').slideToggle()
	})
})

/*iframe浏览器高度自适应效果*/

$(document).ready(function(){
  var h = $(window).height()-60;
   $(".iframe").css("height",h);
	});
	
$(window).resize(function(){
var h = $(window).height()-60;
  $(".iframe").css("height",h);
	});
	

$(document).ready(function(){
  var h = $(window).height()-72;
   $(".process_iframe").css("height",h);
	});
	
$(window).resize(function(){
var h = $(window).height()-72;
  $(".process_iframe").css("height",h);
	});
		
		




/*inputfocus效果*/
    $(function(){
		$(":input[type=text],.textarea_normal,.textarea_normal_b").focus(function(){
			  $(this).addClass("focus");
			  /**
			  if($(this).val() ==this.defaultValue){  
                  $(this).val("");           
			  } */
		}).blur(function(){
			 $(this).removeClass("focus");
			 /*
			 if ($(this).val() == '') {
                $(this).val(this.defaultValue);
             }*/
		});
    })
	

/*更多隐藏效果*/
   
 $('.more_info').hide();

 
$(function(){
	$('.show_arrow_more').toggle(
	
	function(){
		  $('.more_info').fadeIn();
		  $('.show_arrow_more').attr('class','hide_arrow_more'); 
		},
		
		function(){
		  $('.more_info').fadeOut();
		$('.hide_arrow_more').attr('class','show_arrow_more'); 
		});	

	})
        /* NOTIFICATION 点击隐藏的顶部banner
         * --------------------------------------------------------------------------------*/

        $('.notification-button a').hover(

        function () {
                $(this).stop(true, true).animate({
                        backgroundColor: '#fe8a02'
                })
        },

        function () {
                $(this).stop(true, true).animate({
                        backgroundColor: '#322e39'
                })
        });

        // If you wanna make notification bar opened simply comment this two lines
        $('.notification').hide();
       

        // Trigger open/close notification bar on click
        $('.notification-button a').toggle(function () {
                $('.notification').slideDown();
				 $('.bottom_arrow').attr('class','up_arrow'); 
				},
              function () {
                $('.notification').slideUp();
				 $('.up_arrow').attr('class','bottom_arrow');  
            
			  });

        /* SOCIAL ICONS
         * --------------------------------------------------------------------------------*/

        $('ul.social-icons li a.icon.social').hover(function () {
                $(this).stop(true, true).addClass('active', 600);
        },

        function () {
                $(this).stop(true, true).removeClass('active', 600);
        });

        /* SCROLL TO TOP 滚到顶部
         * --------------------------------------------------------------------------------*/

        $('.scrollup').hover(function () {
                $(this).stop(true, true).animate({
                        background: '-21px 21px'
                })
        },

        function () {
                $(this).stop(true, true).animate({
                        background: '0px 0px'
                })
        });

        $(window).scroll(function () {
                if ($(this).scrollTop() > 100) {
                        $('.scrollup').fadeIn();
                }
                else {
                        $('.scrollup').fadeOut();
                }
        });

        $('.scrollup').click(function () {
                $("html, body").animate({
                        scrollTop: 0
                }, 600);
                return false;
        });



