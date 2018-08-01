    function doSearch(){
			if(document.all.searchValue.value=="")
			{	
				alert("请输入查询关键字!");
			}else{
				window.location.href="sysindex.action?queryName="+document.all.searchName.value+"&&queryValue="+document.all.searchValue.value;
		 	}
		}
		function confirm1(id){
			 art.dialog.confirm('确认删除?', function(r){
				if (r){										
					location.href="system_delete.action?id="+id;
					return false;
				}
			});
		}
		function edit(id){
			
			var pageUrl = "system_edit.action?id="+id;
			 art.dialog.open(pageUrl,{
				 id:'system_edit',
					title:'编辑',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510,
				    close:function(){
				 		location.reload();
			 		}
			 });
		}
		function add(){
			
			var pageUrl = "system_add.action";
			 art.dialog.open(pageUrl,{
					id:'addressDialog', 
					title:'增加',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510,
				    close:function(){
				 		location.reload();
			 		}
			 });
			 
		}
		//该段js位于</table>与</body>之间
GB_myShow = function(caption, url, /* optional */ height, width, callback_fn) {
    var options = {
        caption: caption,
        height: height || 500,
        width: width || 500,
        fullscreen: false,
        show_loading: false,
        callback_fn: callback_fn
    }
    var win = new GB_Window(options);
    return win.show(url);
}		