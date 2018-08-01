   		var subReportSaveFunction = new Array();//存放子表的保存方法名
		 var saveSubReportFlag=true;
		 $().ready(function() {
			 try{
				logInitPageElementValue();
			 }catch(e){}
				//滚动条事件  
		         $("#fpcontent").scroll(function () {
					if ($(this).scrollTop() > 150) {
						$('#back-top').fadeIn();
					} else {
						$('#back-top').fadeOut();
					}
				});
				$('#back-top a').click(function () {
					$('#fpcontent').animate({
						scrollTop: 0
					}, 100);
					return false;
				});
				//流程节点初始化脚本
				try{
				formInitEventScript();
				}catch(e){}
				var Sys = {}; 
		        var ua = navigator.userAgent.toLowerCase();
		        //判断是否是IE
		        if (window.ActiveXObject){
		        	$("#ietools").hide();
		        }else{
		       		 $("#ietools").hide();
		        }
			});