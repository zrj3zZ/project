$(function(){	                        
		$('#metadata_grid').datagrid({
	             	url:"resbook_space_gridjson.action",
					loadMsg: "正在加载数据...",
					fitColumns: true,
					rownumbers:true,
					singleSelect:true,
					onDblClickRow:function(){ 
					    var node=$('#metadata_grid').datagrid('getSelected');
					    	editSpace(node.id);
	               		},
					columns:[[ 
						{field:'id',title:'空间ID',width:40,align:'center'},
						{field:'spacename',title:'空间名称',width:40,align:'center'},
						{field:'type',title:'预定类型',width:70,align:'center'},
						{field:'cycle',title:'预定周期',width:70,align:'center'},
						{field:'manager',title:'管理员',width:30,align:'center'},
						{field:'status',title:'状态',width:50,align:'center'},
						{field:'memo',title:'提示信息',width:70,align:'center'},
						{field:'operate',title:'操作',width:70,align:'center'}
					]]
				});	
			
		});	
//新增空间		
function addSpace(){
	var pageUrl = 'url:resbook_space_add.action';
	art.dialog.open(pageUrl,{
				id:'addSpaceWinDiv',
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
function editSpace(id){
	var pageUrl = 'url:resbook_space_edit.action?id='+id;
	art.dialog.open(pageUrl,{
		id:'editSpaceWinDiv',
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
	dg.ShowDialog();
}
//删除空间
function removeSpace(){
		var node=$('#metadata_grid').datagrid('getSelected');
		 if(node==null){
		   $.messager.alert('警告','请选择要删除的空间！');
		   }else{
			   if(confirm("确认删除当前模型吗?")){
					var pageurl = "resbook_space_remove.action?id="+node.id;
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

//内容管理
function contentf(cid,spacename){
		
		var url='loadContentM.action?cspaceid='+cid;
		var subtitle=spacename+"内容管理";
				window.parent.addTab(subtitle,url,'');
		}
//基础信息
function basef(cid,spacename){		
		var url="resbook_base_index.action?spaceId="+cid+"&spacename="+encodeURI(spacename);	 			
		var subtitle=spacename+"基础信息";
				window.parent.addTab(subtitle,url,'');	
}