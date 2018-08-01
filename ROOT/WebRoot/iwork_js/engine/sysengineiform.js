//打开表单设计器
function openPageDesgine(title,url){
			//window.parent.addTab(title+'_',url,'');
			var pageUrl = url;
			art.dialog.open(pageUrl,{
					id:'formDesgineWinDiv',
					cover:true,
					title:'表单模型设计['+title+']',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1000,
					cache:false,
					lock: true,
					height:500, 
					iconTitle:false,
					extendDrag:true,
					autoSize:true
				});
}