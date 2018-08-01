function redirectUrl(url){
	window.location = url;
}
function openOpinion(pageUrl){
	art.dialog.open(pageUrl,{
			id:'addressDialog', 
			title:"常用意见",
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width: '100%',
			height: '100%',
			close:function(){
				$("#sendbtn").focus();
			}
		 });
}
//返回待办事宜列表
function backTodolist(){
	parent.window.location.href="wechat_process_todo.action"; 
}

function showTip(info){
	alert(info);
}
//加载系统提示
function showSysTips(){
	$.post("sysmq_showtip.action",{type:"tip"},function(data){
         if(data!=''){
        		alert(data);
         }
    });
}
function radio_book(defaultField) { 
	  var url ="radiobook_index.action?1=1"; 
		if(defaultField!=''){
			url=url+"&defaultField="+defaultField;
		}
		//获得input内容
		var v = document.getElementById(defaultField);
		if(v.value!=""){  
			var val  =v.value;
			url=url+"&input="+val+""; 
		}
		
		alert(url);
		art.dialog.open(url,{
			id:"radioBookDialog",
			title: '单选地址簿',
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width:'100%',
			height:'100%'
		 });
}