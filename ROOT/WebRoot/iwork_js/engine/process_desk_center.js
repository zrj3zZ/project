function showTaskCount(){
				 $.post('processManagement_showTaskNoticeJson.action',{},function(data){
					 var dataObj=eval("("+data+")");
					 var sumNum = 0;
					 $.each(dataObj,function(idx,item){
						 if(idx=="taskNum"){
							 if(item!=0){
								 $("#num0").html("("+item+")");
								 sumNum+=item; 
							 }else{
								 $("#num0").html("");
							 }
						 }
						 if(idx=="noticeNum"){
							 if(item!=0){
								 $("#num1").html("("+item+")");
								 sumNum+=item;
							 }else{
								 $("#num1").html("");
							 }
						 }
					 }) 
					$("#workflow", window.parent.document).html("("+sumNum+")"); 
				 }); 
		}
		function doResize() { 
			var ss = getPageSize(); 
			$("#task_list_grid").jqGrid('setGridWidth', ss.WinW-20).jqGrid('setGridHeight', ss.WinH-185); 
		} 
		function getPageSize() { 
			var winW, winH; 
			if(window.innerHeight) {// all except IE 
			winW = window.innerWidth; 
			winH = window.innerHeight; 
			} else if (document.documentElement && document.documentElement.clientHeight) {// IE 6 Strict Mode 
			winW = document.documentElement.clientWidth; 
			winH = document.documentElement.clientHeight; 
			} else if (document.body) { // other 
				winW = document.body.clientWidth; 
				winH = document.body.clientHeight; 
			}  // for small pages with total size less then the viewport  
			return {WinW:winW, WinH:winH}; 
		}