function confirm1(id){
	art.dialog.confirm('权限组执行删除操作后，授权信息将一并删除，是否确认删除?', function(){
        	var obj = {"id":id};
        	$.post("purgroup_delete.action",obj,function(data){
 	           if(data=="success"){ 
                 	art.dialog.tips("移除成功");
                 	document.location.reload();
                 	api.close();
             	}else{
             		art.dialog.tips("移除成功");
             	}
 	    	});
            }, function(){ 
              
        });
	}
//添加	
function addInfo(){
	var pageUrl = "purgroup_add.action";
	art.dialog.open(pageUrl,{
		id:'dg_addInfo',  
	    title:'权限组维护',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:500,
	    height:300,
	    close:function(){
		location.reload(true);
	}
	 });
   }
//修改   
function editInfo(id){
	var pageUrl = 'purgroup_edit.action?id='+id;
	art.dialog.open(pageUrl,{
		id:'dg_addInfo',  
	    title:'权限组维护',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:500,
	    height:300,
	    close:function(){
			location.reload(true);
		}
	 });
}
//授权模块
function  moduleAuthority(id){
	var pageUrl = 'openpurview_web.action?id='+id;
	art.dialog.open(pageUrl,{
		id:'dg_addInfo',  
	    title:'权限组维护',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
	 });
}
//授权操作
function  operationAuthority(id){
	var pageUrl = 'openpurviewToOperation_web.action?id='+id;
	art.dialog.open(pageUrl,{
		id:'dg_addInfo',  
	    title:'授权操作',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
	 });
}
//授权流程
function  processAuthority(id){
	var pageUrl = 'openPurviewProcessIndex.action?purviewid='+id;
	art.dialog.open(pageUrl,{
		id:'dg_addInfo',  
		title:'权限组授权流程',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
	 });
} 
//授权数据维护
function  demAuthority(id){
	var pageUrl = 'openPurviewDemIndex.action?purviewid='+id;
	art.dialog.open(pageUrl,{
		id:'dg_addInfo',  
		title:'权限组授权数据维护管理',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
	 });
	
}
//授权数据维护
function  reportAuthority(id){
	var pageUrl = 'openPurviewReportIndex.action?purviewid='+id;
	art.dialog.open(pageUrl,{
		id:'dg_addInfo',  
		title:'权限组授权报表权限',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:'90%',
	    height:'90%'
	 });
}
//授权角色
 function  roleAuthority(id){
	 var pageUrl = 'openpurviewToRole_web.action?id='+id;
		art.dialog.open(pageUrl,{
			id:'dg_addInfo',  
			 title:'权限组授权角色',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:'90%',
		    height:'90%'
		 });
} 

//授权用户
 function  userAuthority(id){
	 var pageUrl = 'openPurviewUserIndex.action?purviewid='+id;
	 art.dialog.open(pageUrl,{
			id:'dg_addInfo',  
			title:'权限组授权用户',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:'90%',
		    height:'90%'
		 });
}	
//授权部门
 function  deptAuthority(id){
	 var pageUrl = 'openPurviewDeptIndex.action?purviewid='+id;
	 art.dialog.open(pageUrl,{
			id:'dg_addInfo',  
			title:'权限组授权部门',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:'90%',
		    height:'90%'
		 });
 }
//查询回车事件
 function enterKey(){
 	if (window.event.keyCode==13){
 		 doSearch();
 		return;
 	}
 }
 function doSearch(){
 	var searchTxt = $("#searchTxt").val();
 	if(searchTxt==""){
 		alert("请输入查询关键字");
 		return;
 	}else{ 
 		searchTxt = searchTxt.toUpperCase();
 		var url = "purgroup_index_search.action?searchkey="+encodeURI(searchTxt);
 		$('#editForm').attr('action',url); 
 		$('#editForm').attr('target',"_self");
 		$('#editForm').submit(); 
 	}
 }