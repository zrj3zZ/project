
function createProcess(actDefId){
		var url = "processRuntimeStartInstance.action?actDefId=" + actDefId;
		openDialog('',url); 
	} 
	
	//添加和编辑窗口
	function openDialog(title,url){
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	//	page.location = url;
	}

function doResize() { 
			var ss = getPageSize(); 
			$("#task_list_grid").jqGrid('setGridWidth', ss.WinW-220).jqGrid('setGridHeight', ss.WinH-185); 
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