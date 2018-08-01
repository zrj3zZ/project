//新增序列
function add(){
		var pageUrl =  "know_bigclass_add.action";
		 art.dialog.open(pageUrl,{
			 id:'bigclass_edit',
			 title:'新增分类',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:450,
				height:250,
				close:function(){
			 		window.location.reload();
		 		}
			 });
}
//编辑序列
function edit(id){
		var pageUrl =  "know_bigclass_edit.action?bcid="+id;
		art.dialog.open(pageUrl,{
			 id:'bigclass_edit',
			 title:'编辑分类',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:450,
				height:250,
				close:function(){
			 		window.location.reload();
		 		} 
			 });
		 
}
//启动
function start_confirm(id){
        art.dialog.confirm('确定启用该分类？', function(){ 
              location.href="know_bigclass_start.action?bcid="+id;
         }, function(){ 
  
         });
}
//停止
function stop_confirm(id){
        art.dialog.confirm('确定停用该分类？', function(){ 
              location.href="know_bigclass_stop.action?bcid="+id;
         }, function(){ 
  
         });
}
//查看分类专家
function showExperts(id){
        var pageUrl =  "know_bigclass_showExperts.action?bcid="+id;
        art.dialog.open(pageUrl,{
			 id:'bigclass_edit',
				title:'专家分类',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:280,
				height:480
			 });
}
//删除 
function delConfirm(id){
	   $.post('know_bigclass_isQuestionExist.action',{bcid:id},function(data){
	   		  if(data=='false'){
	   		       art.dialog.confirm('确定删除该分类？', function(){ 
              		   location.href="know_bigclass_delete.action?bcid="+id;
         			}, function(){});
	   		  }//该分类下不存在问题，可删除
	   		  else{
	   		       art.dialog.tips("该分类下有提问，不能被删除！",2);
	   		  }
	   });
}
