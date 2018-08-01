
function add(){
	var pageUrl = "process_route_add.action";
	art.dialog.open(pageUrl,{
		id:'routeBaseWinDiv',
		title:'新建精确路由管理模型',  
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:550
	 });
}