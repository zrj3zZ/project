//新增序列
function addSequence(){
		var pageUrl =  "sequence_add.action";
		art.dialog.open(pageUrl,{
			id:'sequence_add',
			title:'新增序列',
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
//编辑序列
function editSequence(id){
		var pageUrl =  "sequence_edit.action?id="+id;
		art.dialog.open(pageUrl,{
			id:'sequence_add',
			title:'编辑序列',
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
function delConfirm(id){
	art.dialog.confirm('确定删除？', function(){ 
              location.href="sequence_delete.action?id="+id;
         }, function(){ 
  
         });
}
