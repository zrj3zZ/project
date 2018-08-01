//新增服务模型
function service_add(){
	var pageUrl = "sysService_add.action";
	art.dialog.open(pageUrl,{
		id:'addressDialog', 
		 title:'服务管理维护',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:500,
        height:280,
        close:function (){
             window.location.reload();
        }
	 });
}
//编辑
function service_edit(id){
	var pageUrl = 'sysService_edit.action?id='+id;
	art.dialog.open(pageUrl,{
		id:'addressDialog', 
		 title:'服务管理维护',
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:500,
        height:280,
        close:function (){
             window.location.reload();
        }
	 });
}
//删除
function del_confirm(id){
		art.dialog.confirm('确定删除？', function(){ 
              location.href="sysService_delete.action?id="+id;
         }, function(){ 
  
         });
}
//启动
function start_confirm(id){
		art.dialog.confirm('确定启动该服务？', function(){ 
              location.href="sysService_start.action?id="+id;
         }, function(){ 
  
         });
}
//停止
function stop_confirm(id){
	art.dialog.confirm('确定停止该服务？', function(){ 
              location.href="sysService_stop.action?id="+id;
         }, function(){ 
  
         });
}