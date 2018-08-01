//新增空间		
function addBase(){
	var spaceId = $("#spaceId").val(); 
	var spacename = $("#spacename").val(); 
	var pageUrl = 'resbook_base_add.action?spaceId='+spaceId+'&spacename='+encodeURI(spacename);
	art.dialog.open(pageUrl,{
				id:'addBaseWinDiv',
				cover:true,
				title:'添加资源预定',  
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:500,
				cache:false, 
				lock: true,
				esc: true,
				height:400,  
				iconTitle:false, 
				extendDrag:true,
				autoSize:true
			});
}

//新增空间		
function editBase(id){ 
	var pageUrl = 'url:resbook_base_edit.action?id='+id;
	art.dialog.open(pageUrl,{
		id:'editBaseWinDiv',
		cover:true,
		title:'修改预定资源信息',  
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999',
		rang:true,
		width:500,
		cache:false, 
		lock: true, 
		esc: true,
		height:400,  
		iconTitle:false, 
		extendDrag:true,
		autoSize:true
	});
}
//删除空间
function remove(){
	var rows = $('#metadata_grid').datagrid('getSelections');
	if(rows.length==0){
	   $.messager.alert('警告','请选择要删除的信息！');
	   }else{
		   if(confirm("确认删除当前模型吗?")){ 
                	var idsremove = [];
					var rows = $('#metadata_grid').datagrid('getSelections');		
					for(var i=0;i<rows.length;i++){
						idsremove.push(rows[i].id);
					 }
					alert(idsremove); 
					var pageurl = "resbook_base_remove.action?ids="+idsremove;
					$.ajax({ 
			            type:'POST',
			            url:pageurl,
			            success:function(msg){ 
			            	  if(msg=="success"){
			                  	alert("移除成功!");
			                  	refrenshGrid();
			                  } 
			                  else if(responseText=="error"){
			                     alert("移除失败!");
			                  } 
			            }
			        });
           }
        }
	}
function refrenshGrid(){
	$('#metadata_grid').datagrid('reload');
} 
function showPic(sUrl){
		var x,y;
		x = event.clientX;
		y = event.clientY;
		document.getElementById("Layer1").style.left = x;
		document.getElementById("Layer1").style.top = y;
		document.getElementById("Layer1").innerHTML = "<img src='" + sUrl + "' width=160 height=120>";
		document.getElementById("Layer1").style.display = "block";
	}
	function hiddenPic(){
		document.getElementById("Layer1").innerHTML = "";
		document.getElementById("Layer1").style.display = "none";
	} 	